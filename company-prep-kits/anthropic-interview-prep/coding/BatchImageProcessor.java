/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Batch Image Processor" / "Image Processing Pipeline"
 *           (see batch-image-processor-prompt.txt)
 *
 * For every (image, transformations.json) pair: load the image, apply the
 * JSON-listed transforms IN ORDER, and write the result via get_output_path.
 * Six transforms: grayscale, flip_horizontal(ly), flip_vertical(ly),
 *                 scale(factor), blur(radius), rotate(angle).
 *
 * This single file contains BOTH parts the interview walks through:
 *   Part 1  processImagesSequential()        — naive, correctness-first.
 *   Part 2  processImagesParallel()          — thread pool + decode-once. *** the follow-up ***
 *           processImagesParallelFineGrained()— per-(image,json) variant w/ decode cache.
 *   processImages()                          — canonical interface entry point.
 *
 * LIBRARY CHOICE: pure JDK — java.awt / javax.imageio for imaging, plus a tiny
 * embedded JSON parser — so it compiles & runs with plain javac/java, no deps.
 * (In a real round you'd reach for Gson/Jackson for JSON and possibly the
 *  TwelveMonkeys ImageIO plugins for exotic formats; kept dependency-free here.)
 *
 * *** THE KEY TALKING POINT (why Java differs from the Python reference) ***
 *   The Python solution MUST use ProcessPoolExecutor to get real parallelism,
 *   because the GIL serializes CPU-bound Python bytecode across threads.
 *   The JVM has NO GIL, so a plain fixed thread pool gives TRUE multi-core
 *   parallelism for these CPU-bound transforms — no process spawning, no
 *   pickling/copying decoded pixels across a process boundary.
 *
 * Other "optimal" decisions (see inline notes):
 *   - Decode each source image EXACTLY ONCE (not once per transform file).
 *   - Share the decoded image read-only across tasks (transforms never mutate).
 *   - Separable Gaussian blur: two 1-D passes => O(n*r) instead of O(n*r^2).
 *   - Normalize everything to TYPE_INT_RGB so blur/rotate/JPEG-write are uniform.
 *
 * `main` with no args runs a self-test on synthetic images: it proves the
 * parallel outputs are BYTE-IDENTICAL to the sequential ones and prints
 * throughput. With args it runs on real directories:
 *
 *   Compile        : javac BatchImageProcessor.java
 *   Run self-test  : java BatchImageProcessor
 *   Run on dirs    : java BatchImageProcessor <imageDir> <transformDir> <outDir> [seq|par|fine]
 *
 * Verified on OpenJDK Corretto 21.
 * ============================================================================
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class BatchImageProcessor {

    // =========================================================================
    // SECTION 0 — Transform spec model
    // A record is enough: type + the (at most one) numeric parameter it uses.
    // =========================================================================
    record Transform(String type, double factor, double angle, double radius) {}

    /** Recognized image file extensions (lowercase, no dot). */
    private static final Set<String> IMAGE_EXTS = Set.of("png", "jpg", "jpeg", "bmp", "gif", "tiff");

    // =========================================================================
    // SECTION 1 — Transformation primitives (SHARED by Part 1 & Part 2)
    //
    // CONTRACT: each takes a TYPE_INT_RGB image and returns a NEW TYPE_INT_RGB
    // image. They NEVER mutate the input. That immutability is what makes it
    // safe to share one decoded source image, read-only, across many threads.
    // =========================================================================

    /** grayscale: colorimetric RGB->luma via the gray color space, kept as RGB (R=G=B). */
    static BufferedImage grayscale(BufferedImage src) {
        BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        // ColorConvertOp(cs) with a non-null dst routes src -> CS_GRAY -> dst(RGB).
        new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(src, dst);
        return dst;
    }

    /** flip_horizontal (mirror): swap the source X corners -> exact, no interpolation. */
    static BufferedImage flipHorizontal(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        //          dst(0,0)->(w,h)        src(w,0)->(0,h)  [right edge maps to left]
        g.drawImage(src, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dst;
    }

    /** flip_vertical: swap the source Y corners. */
    static BufferedImage flipVertical(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        //          dst(0,0)->(w,h)        src(0,h)->(w,0)  [bottom edge maps to top]
        g.drawImage(src, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dst;
    }

    /** scale by factor (e.g. 0.5 = half). Bilinear: good speed/quality balance. */
    static BufferedImage scale(BufferedImage src, double factor) {
        int w = Math.max(1, (int) Math.round(src.getWidth() * factor));
        int h = Math.max(1, (int) Math.round(src.getHeight() * factor));
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        // (Heavy DOWNSCALES alias with a single bilinear pass; progressive halving
        //  or VALUE_INTERPOLATION_BICUBIC improves quality at some CPU cost.)
        return dst;
    }

    /**
     * rotate by angle degrees, expanding the canvas to fit (PIL's expand=True).
     * Counter-clockwise to match the spec: Java's image space is y-DOWN, where a
     * positive AffineTransform angle is CW, so we rotate by -angle. Exposed corners
     * are left at the default fill (black), matching PIL's default for RGB.
     */
    static BufferedImage rotate(BufferedImage src, double angleDeg) {
        double rads = Math.toRadians(angleDeg);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = src.getWidth(), h = src.getHeight();
        int newW = (int) Math.round(w * cos + h * sin);   // rotated bounding box
        int newH = (int) Math.round(w * sin + h * cos);
        BufferedImage dst = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        AffineTransform at = new AffineTransform();
        at.translate(newW / 2.0, newH / 2.0);   // 3) move rotated image to new center
        at.rotate(-rads);                        // 2) rotate (negative => CCW in y-down)
        at.translate(-w / 2.0, -h / 2.0);        // 1) rotate about the OLD center
        g.drawImage(src, at, null);
        g.dispose();
        return dst;
    }

    /**
     * Gaussian blur of the given radius. SEPARABLE: a 2-D Gaussian == a horizontal
     * 1-D pass followed by a vertical 1-D pass, so cost is O(n*r) per image instead
     * of O(n*r^2) for a full 2-D kernel. We edge-replicate-pad first so border
     * pixels blur against real neighbors (no dark/halo rim), then crop back.
     */
    static BufferedImage blur(BufferedImage src, int radius) {
        if (radius <= 0) return copy(src);
        float[] k = gaussianKernel1D(radius);
        int half = k.length / 2;
        BufferedImage padded = padReplicate(src, half);
        BufferedImage pass1 = new ConvolveOp(new Kernel(k.length, 1, k), ConvolveOp.EDGE_NO_OP, null).filter(padded, null);
        BufferedImage pass2 = new ConvolveOp(new Kernel(1, k.length, k), ConvolveOp.EDGE_NO_OP, null).filter(pass1, null);
        // The unprocessed EDGE_NO_OP border falls entirely in the padding -> crop it away.
        return copy(pass2.getSubimage(half, half, src.getWidth(), src.getHeight()));
        // For very large radii, an O(n) approach (3x box-blur via integral image, as
        // PIL actually does, or a Deriche/IIR recursive filter) is asymptotically better.
    }

    /** Normalized 1-D Gaussian kernel; treat radius as sigma, cover +/-3 sigma. */
    static float[] gaussianKernel1D(int radius) {
        double sigma = radius;
        int half = Math.max(1, (int) Math.ceil(3 * sigma));
        int size = 2 * half + 1;
        float[] k = new float[size];
        double twoSigmaSq = 2 * sigma * sigma, sum = 0;
        for (int i = 0; i < size; i++) {
            int x = i - half;
            double v = Math.exp(-(x * x) / twoSigmaSq);
            k[i] = (float) v;
            sum += v;
        }
        for (int i = 0; i < size; i++) k[i] /= sum;   // normalize -> preserves brightness
        return k;
    }

    /** Grow the image by `pad` on every side, replicating edge pixels (clamp). */
    static BufferedImage padReplicate(BufferedImage src, int pad) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w + 2 * pad, h + 2 * pad, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, pad, pad, null);                                              // center
        g.drawImage(src, pad, 0, pad + w, pad, 0, 0, w, 1, null);                       // top edge
        g.drawImage(src, pad, pad + h, pad + w, h + 2 * pad, 0, h - 1, w, h, null);     // bottom edge
        g.drawImage(src, 0, pad, pad, pad + h, 0, 0, 1, h, null);                       // left edge
        g.drawImage(src, pad + w, pad, w + 2 * pad, pad + h, w - 1, 0, w, h, null);     // right edge
        g.drawImage(src, 0, 0, pad, pad, 0, 0, 1, 1, null);                             // TL corner
        g.drawImage(src, pad + w, 0, w + 2 * pad, pad, w - 1, 0, w, 1, null);           // TR corner
        g.drawImage(src, 0, pad + h, pad, h + 2 * pad, 0, h - 1, 1, h, null);           // BL corner
        g.drawImage(src, pad + w, pad + h, w + 2 * pad, h + 2 * pad, w - 1, h - 1, w, h, null); // BR corner
        g.dispose();
        return out;
    }

    /** Independent TYPE_INT_RGB copy of (a possibly shared sub-) image. */
    static BufferedImage copy(BufferedImage src) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return out;
    }

    /** Dispatch one transform. Accepts both the *_horizontal and *_horizontally aliases. */
    static BufferedImage applyOne(BufferedImage img, Transform t) {
        return switch (t.type()) {
            case "grayscale"                          -> grayscale(img);
            case "flip_horizontal", "flip_horizontally" -> flipHorizontal(img);
            case "flip_vertical",   "flip_vertically"   -> flipVertical(img);
            case "scale"                              -> scale(img, t.factor());
            case "blur"                               -> blur(img, (int) Math.round(t.radius()));
            case "rotate"                             -> rotate(img, t.angle());
            default -> throw new IllegalArgumentException("Unknown transform: " + t.type());
        };
    }

    /** Apply the full sequence. Returns a NEW image (input untouched). */
    static BufferedImage applyAll(BufferedImage img, List<Transform> transforms) {
        BufferedImage cur = img;
        for (Transform t : transforms) cur = applyOne(cur, t);
        return cur;
    }

    // =========================================================================
    // SECTION 2 — JSON loading + file I/O helpers
    // =========================================================================

    /** Read {"transformations":[...]} into a List<Transform>. */
    @SuppressWarnings("unchecked")
    static List<Transform> loadTransforms(Path jsonPath) throws IOException {
        Object root = Json.parse(Files.readString(jsonPath));
        Map<String, Object> obj = (Map<String, Object>) root;
        List<Object> list = (List<Object>) obj.get("transformations");
        List<Transform> out = new ArrayList<>();
        if (list == null) return out;
        for (Object o : list) {
            Map<String, Object> m = (Map<String, Object>) o;
            out.add(new Transform(
                    (String) m.get("type"),
                    num(m.get("factor")),
                    num(m.get("angle")),
                    num(m.get("radius"))));
        }
        return out;
    }

    private static double num(Object o) { return o == null ? 0.0 : ((Number) o).doubleValue(); }

    static List<Path> listImages(String dir) throws IOException {
        try (Stream<Path> s = Files.list(Paths.get(dir))) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> IMAGE_EXTS.contains(ext(p)))
                    .sorted().toList();
        }
    }

    static List<Path> listJson(String dir) throws IOException {
        try (Stream<Path> s = Files.list(Paths.get(dir))) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> ext(p).equals("json"))
                    .sorted().toList();
        }
    }

    static String ext(Path p) {
        String n = p.getFileName().toString();
        int d = n.lastIndexOf('.');
        return d < 0 ? "" : n.substring(d + 1).toLowerCase();
    }

    static String stripExt(String name) {
        int d = name.lastIndexOf('.');
        return d < 0 ? name : name.substring(0, d);
    }

    /** Decode to a uniform TYPE_INT_RGB image (flattening any alpha onto white). */
    static BufferedImage decodeRGB(Path imagePath) throws IOException {
        BufferedImage raw = ImageIO.read(imagePath.toFile());
        if (raw == null) throw new IOException("Unsupported or corrupt image: " + imagePath);
        if (raw.getType() == BufferedImage.TYPE_INT_RGB) return raw;
        BufferedImage rgb = new BufferedImage(raw.getWidth(), raw.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, rgb.getWidth(), rgb.getHeight());
        g.drawImage(raw, 0, 0, null);
        g.dispose();
        return rgb;
    }

    /** Write the image; format is inferred from the output extension. */
    static void save(BufferedImage img, String outputPath) throws IOException {
        Path p = Paths.get(outputPath);
        if (p.getParent() != null) Files.createDirectories(p.getParent());
        String fmt = ext(p);
        if (fmt.isEmpty()) fmt = "png";
        if (fmt.equals("jpg")) fmt = "jpeg";          // ImageIO registers JPEG as "jpeg"
        if (!ImageIO.write(img, fmt, p.toFile()))     // TYPE_INT_RGB has no alpha -> JPEG is fine
            throw new IOException("No ImageIO writer for format: " + fmt);
    }

    /** Default get_output_path: <out>/<imageBase>__<jsonBase>.<imageExt>. */
    static String defaultOutputPath(String imagePath, String jsonPath, String outputDir) {
        String imgBase = stripExt(Paths.get(imagePath).getFileName().toString());
        String jsonBase = stripExt(Paths.get(jsonPath).getFileName().toString());
        String imgExt = ext(Paths.get(imagePath));
        if (imgExt.isEmpty()) imgExt = "png";
        return Paths.get(outputDir, imgBase + "__" + jsonBase + "." + imgExt).toString();
    }

    // =========================================================================
    // SECTION 3 — PART 1: sequential implementation (correctness-first)
    //
    // Mirrors the reference exactly: for each json, for each image, decode +
    // apply + save. Simple and obviously correct -> verify on small_images.
    // BUT note the waste: the SAME image is decoded once PER json file. With T
    // transform files that is T redundant decodes per image -> Part 2 fixes it.
    // =========================================================================
    static void processImagesSequential(String imageDir, String transformationDir, String outputDir,
                                        BiFunction<String, String, String> getOutputPath) throws IOException {
        List<Path> images = listImages(imageDir);
        List<Path> jsons = listJson(transformationDir);
        for (Path json : jsons) {
            List<Transform> transforms = loadTransforms(json);
            for (Path image : images) {
                try {
                    BufferedImage src = decodeRGB(image);                  // re-decoded for every json :(
                    BufferedImage out = applyAll(src, transforms);
                    save(out, getOutputPath.apply(image.toString(), json.toString()));
                } catch (IOException e) {
                    System.err.println("Failed " + image.getFileName() + " / " + json.getFileName()
                            + ": " + e.getMessage());                      // one bad file must not abort the batch
                }
            }
        }
    }

    // =========================================================================
    // SECTION 4 — PART 2: parallel implementation (performance)   *** follow-up ***
    //
    // CHANGES FROM PART 1, and why each matters:
    //   [1] Fixed thread pool sized to CPU cores. The JVM has NO GIL, so threads
    //       run these CPU-bound transforms with TRUE parallelism. (Python needs
    //       ProcessPoolExecutor here; we don't pay process/pickle overhead.)
    //   [2] Task granularity = ONE PER IMAGE. Each task decodes its image ONCE
    //       and then runs every transform sequence on it -> kills the redundant
    //       re-decode from Part 1 (the classic "decode once" rewrite).
    //   [3] Parse all transform JSONs ONCE up front and reuse the specs (no
    //       per-image JSON re-parsing).
    //   [4] The decoded source is shared READ-ONLY across the inner json loop;
    //       safe because every primitive allocates a new image.
    //   Memory is naturally bounded to ~nThreads decoded images in flight.
    // =========================================================================
    static void processImagesParallel(String imageDir, String transformationDir, String outputDir,
                                      BiFunction<String, String, String> getOutputPath) throws IOException {
        final List<Path> images = listImages(imageDir);
        final List<Path> jsons = listJson(transformationDir);

        final List<List<Transform>> specs = new ArrayList<>();          // [3] parse once
        for (Path j : jsons) specs.add(loadTransforms(j));

        final int nThreads = Runtime.getRuntime().availableProcessors(); // [1]
        final ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        final List<Future<?>> futures = new ArrayList<>();

        for (final Path image : images) {                                // [2] one task per image
            futures.add(pool.submit(() -> {
                try {
                    BufferedImage src = decodeRGB(image);                // decode ONCE
                    for (int s = 0; s < specs.size(); s++) {
                        BufferedImage out = applyAll(src, specs.get(s)); // [4] src read-only -> reuse
                        save(out, getOutputPath.apply(image.toString(), jsons.get(s).toString()));
                    }
                } catch (IOException e) {
                    System.err.println("Failed " + image.getFileName() + ": " + e.getMessage());
                }
            }));
        }
        pool.shutdown();
        awaitAll(futures);                                               // block until everything is done
    }

    // -------------------------------------------------------------------------
    // PART 2 (alternative) — finer-grained: one task per (image, json) pair.
    // Better load-balancing when #images < #cores or image sizes are skewed.
    // Decode-once is preserved by a concurrent cache: the FIRST task needing an
    // image decodes it (computeIfAbsent is atomic), the rest reuse it.
    // TRADE-OFF: the cache can hold ALL decoded images at once -> more memory.
    // -------------------------------------------------------------------------
    static void processImagesParallelFineGrained(String imageDir, String transformationDir, String outputDir,
                                                 BiFunction<String, String, String> getOutputPath) throws IOException {
        final List<Path> images = listImages(imageDir);
        final List<Path> jsons = listJson(transformationDir);
        final List<List<Transform>> specs = new ArrayList<>();
        for (Path j : jsons) specs.add(loadTransforms(j));

        final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final List<Future<?>> futures = new ArrayList<>();
        final ConcurrentHashMap<Path, BufferedImage> decoded = new ConcurrentHashMap<>();

        for (final Path image : images) {
            for (int s = 0; s < specs.size(); s++) {
                final int si = s;
                futures.add(pool.submit(() -> {
                    try {
                        BufferedImage src = decoded.computeIfAbsent(image, BatchImageProcessor::decodeRGBUnchecked);
                        BufferedImage out = applyAll(src, specs.get(si));
                        save(out, getOutputPath.apply(image.toString(), jsons.get(si).toString()));
                    } catch (IOException | UncheckedIOException e) {
                        System.err.println("Failed " + image.getFileName() + ": " + e.getMessage());
                    }
                }));
            }
        }
        pool.shutdown();
        awaitAll(futures);
        // To bound memory for sets that don't fit in RAM: evict an image from
        // `decoded` once its last transform finishes (per-image AtomicInteger
        // countdown), or just use the per-image task model above.
    }

    /** Canonical interface entry point — defaults to the optimized parallel impl. */
    static void processImages(String imageDir, String transformationDir, String outputDir,
                              BiFunction<String, String, String> getOutputPath) throws IOException {
        processImagesParallel(imageDir, transformationDir, outputDir, getOutputPath);
    }

    private static BufferedImage decodeRGBUnchecked(Path p) {
        try { return decodeRGB(p); } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    /** Wait for all tasks; surface (but don't rethrow) per-task failures. */
    private static void awaitAll(List<Future<?>> futures) {
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                System.err.println("Task error: " + e.getCause());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // =========================================================================
    // SECTION 5 — Minimal embedded JSON parser (so the file is dependency-free).
    // Recursive descent over object/array/string/number/bool/null. In a real
    // interview: `new Gson().fromJson(reader, Spec.class)` and move on.
    // =========================================================================
    static final class Json {
        private final String s;
        private int i;
        private Json(String s) { this.s = s; }

        static Object parse(String s) {
            Json p = new Json(s);
            p.ws();
            Object v = p.value();
            p.ws();
            return v;
        }

        private void ws() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }

        private Object value() {
            char c = s.charAt(i);
            return switch (c) {
                case '{' -> obj();
                case '[' -> arr();
                case '"' -> str();
                case 't' -> { i += 4; yield Boolean.TRUE; }
                case 'f' -> { i += 5; yield Boolean.FALSE; }
                case 'n' -> { i += 4; yield null; }
                default  -> number();
            };
        }

        private Map<String, Object> obj() {
            Map<String, Object> m = new LinkedHashMap<>();
            i++; ws();                                  // skip '{'
            if (s.charAt(i) == '}') { i++; return m; }
            while (true) {
                ws();
                String k = str();
                ws(); i++; ws();                        // skip ':'
                m.put(k, value());
                ws();
                if (s.charAt(i++) == '}') break;        // else it was ',' -> next entry
            }
            return m;
        }

        private List<Object> arr() {
            List<Object> a = new ArrayList<>();
            i++; ws();                                  // skip '['
            if (s.charAt(i) == ']') { i++; return a; }
            while (true) {
                ws();
                a.add(value());
                ws();
                if (s.charAt(i++) == ']') break;        // else ',' -> next element
            }
            return a;
        }

        private String str() {
            StringBuilder b = new StringBuilder();
            i++;                                        // skip opening quote
            while (true) {
                char c = s.charAt(i++);
                if (c == '"') break;
                if (c == '\\') {
                    char e = s.charAt(i++);
                    switch (e) {
                        case '"' -> b.append('"');
                        case '\\' -> b.append('\\');
                        case '/' -> b.append('/');
                        case 'n' -> b.append('\n');
                        case 't' -> b.append('\t');
                        case 'r' -> b.append('\r');
                        case 'b' -> b.append('\b');
                        case 'f' -> b.append('\f');
                        case 'u' -> { b.append((char) Integer.parseInt(s.substring(i, i + 4), 16)); i += 4; }
                        default -> b.append(e);
                    }
                } else {
                    b.append(c);
                }
            }
            return b.toString();
        }

        private Double number() {
            int start = i;
            while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) i++;
            return Double.parseDouble(s.substring(start, i));
        }
    }

    // =========================================================================
    // SECTION 6 — Self-test harness (NOT part of the interview answer).
    // No args  -> generate synthetic images + transform JSONs in a temp dir,
    //             run all three impls, prove parallel == sequential BYTE-FOR-BYTE,
    //             and print throughput.
    // With args -> run on real directories.
    // =========================================================================
    public static void main(String[] args) throws Exception {
        ImageIO.setUseCache(false);                     // we have RAM; skip ImageIO disk cache
        if (args.length >= 3) { runOnDirs(args); return; }

        System.out.println("No dirs given -> running built-in self-test on synthetic images.\n");
        Path root = Files.createTempDirectory("bip-selftest-");
        try {
            Path imgDir = Files.createDirectories(root.resolve("images"));
            Path txDir = Files.createDirectories(root.resolve("transforms"));
            Path outSeq = root.resolve("out_seq"), outPar = root.resolve("out_par"), outFine = root.resolve("out_fine");

            // Deterministic, asymmetric images so flips/rotates are non-trivial.
            final int nImg = 10, dim = 512;
            for (int k = 0; k < nImg; k++)
                ImageIO.write(synthImage(dim, dim, k), "png", imgDir.resolve("img" + k + ".png").toFile());

            // Three configs exercising all six transforms (and the *_ly aliases).
            Files.writeString(txDir.resolve("t1.json"), """
                { "transformations": [
                    { "type": "grayscale" },
                    { "type": "scale", "factor": 0.5 },
                    { "type": "rotate", "angle": 90 } ] }""");
            Files.writeString(txDir.resolve("t2.json"), """
                { "transformations": [
                    { "type": "flip_horizontally" },
                    { "type": "blur", "radius": 3 },
                    { "type": "flip_vertically" } ] }""");
            Files.writeString(txDir.resolve("t3.json"), """
                { "transformations": [
                    { "type": "rotate", "angle": 33.5 },
                    { "type": "scale", "factor": 1.5 },
                    { "type": "grayscale" } ] }""");

            BiFunction<String, String, String> seqP = (i, j) -> defaultOutputPath(i, j, outSeq.toString());
            BiFunction<String, String, String> parP = (i, j) -> defaultOutputPath(i, j, outPar.toString());
            BiFunction<String, String, String> finP = (i, j) -> defaultOutputPath(i, j, outFine.toString());

            int images = listImages(imgDir.toString()).size();
            int jsons = listJson(txDir.toString()).size();
            int ops = images * jsons;

            long s0 = System.nanoTime();
            processImagesSequential(imgDir.toString(), txDir.toString(), outSeq.toString(), seqP);
            double seqSec = (System.nanoTime() - s0) / 1e9;

            long p0 = System.nanoTime();
            processImagesParallel(imgDir.toString(), txDir.toString(), outPar.toString(), parP);
            double parSec = (System.nanoTime() - p0) / 1e9;

            processImagesParallelFineGrained(imgDir.toString(), txDir.toString(), outFine.toString(), finP);

            // Correctness: every output exists AND parallel == sequential byte-for-byte.
            boolean ok = true;
            int compared = 0;
            for (Path img : listImages(imgDir.toString())) {
                for (Path json : listJson(txDir.toString())) {
                    Path a = Paths.get(seqP.apply(img.toString(), json.toString()));
                    Path b = Paths.get(parP.apply(img.toString(), json.toString()));
                    Path c = Paths.get(finP.apply(img.toString(), json.toString()));
                    if (!Files.exists(a) || !Files.exists(b) || !Files.exists(c)) {
                        System.out.println("MISSING output for " + img.getFileName() + " / " + json.getFileName());
                        ok = false; continue;
                    }
                    if (Files.mismatch(a, b) != -1) { System.out.println("DIFF seq vs par : " + a.getFileName()); ok = false; }
                    if (Files.mismatch(a, c) != -1) { System.out.println("DIFF seq vs fine: " + a.getFileName()); ok = false; }
                    compared++;
                }
            }

            System.out.printf("images=%d  transforms=%d  outputs/impl=%d  cores=%d%n",
                    images, jsons, ops, Runtime.getRuntime().availableProcessors());
            System.out.printf("Part 1 sequential : %.3fs  (%.1f img-ops/s)%n", seqSec, ops / seqSec);
            System.out.printf("Part 2 parallel   : %.3fs  (%.1f img-ops/s)  speedup x%.2f%n",
                    parSec, ops / parSec, seqSec / parSec);
            System.out.printf("Verified %d outputs byte-identical across all 3 implementations.%n", compared);
            System.out.println(ok ? "\nALL CHECKS PASSED" : "\nSOME CHECKS FAILED");
        } finally {
            deleteRecursively(root);
        }
    }

    /** Run the pipeline on real directories and report throughput. */
    static void runOnDirs(String[] args) throws Exception {
        String imageDir = args[0], transformDir = args[1], outputDir = args[2];
        String mode = args.length > 3 ? args[3] : "par";
        BiFunction<String, String, String> outPath = (i, j) -> defaultOutputPath(i, j, outputDir);
        int images = listImages(imageDir).size(), jsons = listJson(transformDir).size();
        long t0 = System.nanoTime();
        switch (mode) {
            case "seq" -> processImagesSequential(imageDir, transformDir, outputDir, outPath);
            case "fine" -> processImagesParallelFineGrained(imageDir, transformDir, outputDir, outPath);
            default -> processImagesParallel(imageDir, transformDir, outputDir, outPath);
        }
        double sec = (System.nanoTime() - t0) / 1e9;
        int ops = images * jsons;
        System.out.printf("mode=%s  images=%d  transforms=%d  outputs=%d  time=%.2fs  throughput=%.1f img-ops/s%n",
                mode, images, jsons, ops, sec, sec > 0 ? ops / sec : 0.0);
    }

    /** Deterministic asymmetric test image: RGB gradient + a white box + a diagonal. */
    static BufferedImage synthImage(int w, int h, int seed) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int r = (x * 255) / w, g = (y * 255) / h, b = ((x + y + seed * 40) & 0xFF);
                img.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        Graphics2D gr = img.createGraphics();
        gr.setColor(Color.WHITE); gr.fillRect(4, 4, w / 4, h / 6);
        gr.setColor(Color.BLACK); gr.drawLine(0, 0, w - 1, h / 2);
        gr.dispose();
        return img;
    }

    static void deleteRecursively(Path root) {
        try (Stream<Path> walk = Files.walk(root)) {
            walk.sorted(Comparator.reverseOrder()).forEach(p -> {
                try { Files.deleteIfExists(p); } catch (IOException ignored) { }
            });
        } catch (IOException ignored) { }
    }
}

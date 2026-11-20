import java.util.*;

/**
 * Robinhood — "Word Frequency in a Large Paragraph"
 * Source: 1point3acres d4e34cd5-46fe-55f5-9eff-4fd1b728f694
 *
 * Split a large string on spaces and punctuation, tally word frequencies.
 *   - case-insensitive (Hello == hello == HELLO)
 *   - a word is [A-Za-z0-9]+   (punctuation is a delimiter, never part of a word)
 *   - sort by frequency DESC, then lexicographically ASC for ties
 *   - output "word frequency" per line
 *
 * Given example: "hello world, hello computer"  =>  hello 2 / computer 1 / world 1
 *                ⭐ note "computer" before "world": equal counts, so lex ASC.
 *
 * ---------------------------------------------------------------------------
 * THE TWO THINGS THAT GET GRADED
 * 1. The TIE-BREAK has two levels. Sorting by count alone passes the eyeball
 *    test and fails the sample, because computer/world both have count 1.
 * 2. "Large string" + "optimise memory" means DON'T call
 *    s.toLowerCase().split(regex). That allocates a full lowercase copy of the
 *    input AND an array of every token at once — roughly 3x the input in peak
 *    memory. Stream it instead.
 * ---------------------------------------------------------------------------
 */
public class WordFrequency {

    /* ===================================================================
     * PART 1 — The obvious version. Correct, and memory-hostile.
     *
     * Write it, name its cost, replace it. Two full copies of the input plus a
     * String[] of every token. Fine for a paragraph, wrong for "large".
     * =================================================================== */
    static Map<String, Integer> part1(String s) {
        Map<String, Integer> freq = new HashMap<>();
        for (String w : s.toLowerCase().split("[^a-z0-9]+"))   // allocates a lot
            if (!w.isEmpty()) freq.merge(w, 1, Integer::sum);
        return freq;
    }

    /* ===================================================================
     * PART 2 — ⭐ Single-pass hand-rolled tokenizer. THE ANSWER.
     *
     * CHANGES FROM PART 1:
     *   - no toLowerCase() on the whole input; lowercase per character while
     *     scanning, so peak memory is one word rather than one input copy
     *   - no split(); walk the string once, emitting substrings only for real
     *     words
     *   - O(n) time, O(distinct words) space
     *
     * The scan is the classic two-phase loop: skip non-word chars, then consume
     * word chars.
     * =================================================================== */
    static Map<String, Integer> part2(String s) {
        Map<String, Integer> freq = new HashMap<>();
        int n = s.length(), i = 0;
        StringBuilder sb = new StringBuilder(32);

        while (i < n) {
            while (i < n && !isWordChar(s.charAt(i))) i++;      // skip delimiters
            if (i >= n) break;
            sb.setLength(0);
            while (i < n && isWordChar(s.charAt(i))) {
                sb.append(toLowerAscii(s.charAt(i)));           // lowercase as we go
                i++;
            }
            freq.merge(sb.toString(), 1, Integer::sum);
        }
        return freq;
    }

    /** [A-Za-z0-9] exactly as specified — deliberately NOT Character.isLetterOrDigit,
     *  which is true for accented letters, other scripts and non-ASCII digits. The
     *  spec says [A-Za-z0-9]; using the Unicode-aware method silently changes the
     *  tokenisation. If the interviewer wants Unicode words, ASK, don't assume. */
    private static boolean isWordChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }
    private static char toLowerAscii(char c) {
        return (c >= 'A' && c <= 'Z') ? (char) (c + 32) : c;
    }

    /* ===================================================================
     * PART 3 — ⭐ The two-level sort and the required output format.
     *
     * CHANGES FROM PART 2:
     *   - sorts entries by count DESC then key ASC
     *   - formats "word frequency" per line
     *
     * The comparator IS the answer to this part. Missing the second level is
     * the most common failure on this problem.
     * =================================================================== */
    static List<String> part3(String s) {
        Map<String, Integer> freq = part2(s);
        List<Map.Entry<String, Integer>> es = new ArrayList<>(freq.entrySet());
        es.sort(Comparator
                .comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed()  // count DESC
                .thenComparing(Map.Entry::getKey));                                       // then lex ASC
        List<String> out = new ArrayList<>(es.size());
        for (var e : es) out.add(e.getKey() + " " + e.getValue());
        return out;
    }

    /* ===================================================================
     * PART 4 — Top-K, and the scale follow-up.
     *
     * CHANGES FROM PART 3:
     *   - when only the top K matter, a bounded min-heap gives O(m log K)
     *     instead of O(m log m), and O(K) space instead of O(m)
     *
     * ⚠️ The heap comparator must be the INVERSE of the output comparator, so
     * the element we evict is the worst one: smallest count, and among equal
     * counts the lexicographically LARGEST. Getting that inversion wrong is
     * subtle and silent.
     *
     * If they push further on scale ("the input doesn't fit in memory"):
     * shard by hash(word) across workers, tally locally, then merge — the
     * counts are a commutative monoid so partial tallies combine cleanly. Only
     * the final top-K needs a merge step.
     * =================================================================== */
    static List<String> part4TopK(String s, int k) {
        Map<String, Integer> freq = part2(s);
        Comparator<Map.Entry<String, Integer>> worstFirst = Comparator
                .comparingInt((Map.Entry<String, Integer> e) -> e.getValue())   // count ASC
                .thenComparing(Map.Entry::getKey, Comparator.reverseOrder());   // key DESC

        PriorityQueue<Map.Entry<String, Integer>> heap = new PriorityQueue<>(worstFirst);
        for (var e : freq.entrySet()) {
            heap.offer(e);
            if (heap.size() > k) heap.poll();          // evict the worst
        }
        List<Map.Entry<String, Integer>> top = new ArrayList<>(heap);
        top.sort(Comparator
                .comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed()
                .thenComparing(Map.Entry::getKey));
        List<String> out = new ArrayList<>();
        for (var e : top) out.add(e.getKey() + " " + e.getValue());
        return out;
    }

    // ======================= demo / self-check =======================
    public static void main(String[] args) {
        String ex = "hello world, hello computer";
        List<String> got = part3(ex);
        List<String> want = List.of("hello 2", "computer 1", "world 1");
        System.out.println("part3 = " + got);
        System.out.println("want  = " + want + " -> " + (got.equals(want) ? "PASS" : "FAIL"));
        System.out.println("  ⭐ 'computer' before 'world' proves the lex tie-break fired");

        System.out.println("\n--- case insensitivity ---");
        System.out.println(part3("Hello HELLO hello hELLo") + " -> "
                + (part3("Hello HELLO hello hELLo").equals(List.of("hello 4")) ? "PASS" : "FAIL"));

        System.out.println("\n--- punctuation is a delimiter, digits are word chars ---");
        System.out.println("'don't'      -> " + part3("don't"));
        System.out.println("  (spec says [A-Za-z0-9]+ so the apostrophe SPLITS: don, t)");
        System.out.println("'abc123 abc' -> " + part3("abc123 abc"));
        System.out.println("'a--b__c'    -> " + part3("a--b__c") + "  (underscore is NOT a word char)");
        System.out.println("'...!!!'     -> " + part3("...!!!") + "  (no words)");
        System.out.println("''           -> " + part3(""));
        System.out.println("'   x   '    -> " + part3("   x   "));

        System.out.println("\n--- part1 and part2 agree ---");
        String mixed = "The quick, brown FOX; jumps over the lazy dog. THE fox!";
        System.out.println("  " + part3(mixed));
        boolean agree = new TreeMap<>(part1(mixed)).equals(new TreeMap<>(part2(mixed)));
        System.out.println("  part1 == part2 -> " + (agree ? "PASS" : "FAIL"));

        System.out.println("\n--- part4 top-K ---");
        String k = "a a a b b c c d e";     // a=3, b=2, c=2, d=1, e=1
        System.out.println("  all    = " + part3(k));
        System.out.println("  top 3  = " + part4TopK(k, 3));
        boolean topOk = part4TopK(k, 3).equals(List.of("a 3", "b 2", "c 2"));
        System.out.println("  expect [a 3, b 2, c 2] -> " + (topOk ? "PASS" : "FAIL"));
        // tie at the K boundary: d and e both have 1, lex ASC must pick d
        System.out.println("  top 4  = " + part4TopK(k, 4)
                + " -> " + (part4TopK(k, 4).get(3).equals("d 1") ? "PASS — boundary tie broken by lex" : "FAIL"));

        System.out.println("\n--- scale: ~12 MB input ---");
        StringBuilder big = new StringBuilder();
        Random rnd = new Random(3);
        String[] vocab = new String[2000];
        for (int i = 0; i < vocab.length; i++) vocab[i] = "w" + i;
        for (int i = 0; i < 1_500_000; i++) big.append(vocab[rnd.nextInt(vocab.length)]).append(", ");
        String input = big.toString();
        System.out.println("  input length = " + input.length() + " chars");

        long t0 = System.currentTimeMillis();
        Map<String, Integer> viaSplit = part1(input);
        long t1 = System.currentTimeMillis();
        Map<String, Integer> viaScan = part2(input);
        long t2 = System.currentTimeMillis();
        System.out.println("  part1 split()    " + (t1 - t0) + " ms");
        System.out.println("  part2 tokenizer  " + (t2 - t1) + " ms");
        System.out.println("  same counts?     " + (viaSplit.equals(viaScan) ? "PASS" : "FAIL")
                + "   (" + viaScan.size() + " distinct words)");
        System.out.println("  top 3            " + part4TopK(input, 3));
    }
}

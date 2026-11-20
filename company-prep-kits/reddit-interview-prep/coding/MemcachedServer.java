import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pseudo-Memcached Protocol Server.
 *
 * A simplified, memcached-like in-memory key/value store spoken over TCP.
 * Clients issue line-based, ASCII, case-sensitive commands terminated by '\n':
 *
 *   set <key> <byte_count>\n<byte_count raw bytes>\n      -> STORED\n
 *   get <key> [<key> ...]\n                               -> zero or more
 *       VALUE <key> <byte_count>\n<byte_count raw bytes>\n  blocks, then END\n
 *
 * The single most important property is that VALUES ARE BYTE-COUNT FRAMED, not
 * delimiter framed: after a `set` we read EXACTLY byte_count bytes (which may
 * themselves contain '\n' or arbitrary binary), then consume one trailing
 * newline. That is why we cannot use BufferedReader here -- its hidden
 * read-ahead would steal bytes that belong to a value's payload. Instead we own
 * a small byte buffer (see ProtocolReader) so that line reads and byte-exact
 * reads draw from the same buffer and never over-consume.
 *
 * Run:  javac MemcachedServer.java && java MemcachedServer [port]
 * Test: printf 'set foo 3\r\nbar\r\nget foo\r\n' | nc 127.0.0.1 11211
 */
public class MemcachedServer {

    /** Largest value we will store for a single key (guards against OOM / DoS). */
    private static final long MAX_VALUE_BYTES = 8L * 1024 * 1024; // 8 MiB
    /** memcached's conventional default port. */
    private static final int DEFAULT_PORT = 11211;

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        KeyValueStore store = new KeyValueStore();
        // Thread-per-connection via a pool: simple, correct, plenty for this exercise.
        // (On Java 21+ you could drop in Executors.newVirtualThreadPerTaskExecutor()
        //  to cheaply scale to very many idle-but-open connections.)
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("pseudo-memcached listening on port " + port);
            while (true) {
                Socket client = serverSocket.accept();      // blocks for the next client
                pool.submit(new ClientHandler(client, store));
            }
        }
    }

    // ------------------------------------------------------------------
    // Shared, thread-safe store. Values are raw bytes so binary is preserved.
    // ConcurrentHashMap gives us atomic, lock-free get/put per key.
    // ------------------------------------------------------------------
    static final class KeyValueStore {
        private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();

        byte[] get(String key)             { return map.get(key); }
        void   put(String key, byte[] val) { map.put(key, val); }
    }

    // ------------------------------------------------------------------
    // One instance per connection. Loops reading commands until the client
    // disconnects (or sends a frame we cannot resynchronise from).
    // ------------------------------------------------------------------
    static final class ClientHandler implements Runnable {
        private final Socket socket;
        private final KeyValueStore store;

        ClientHandler(Socket socket, KeyValueStore store) {
            this.socket = socket;
            this.store = store;
        }

        @Override
        public void run() {
            try (Socket s = socket) {
                s.setTcpNoDelay(true); // flush small replies immediately (disable Nagle)
                ProtocolReader in = new ProtocolReader(s.getInputStream(), 8192);
                BufferedOutputStream out = new BufferedOutputStream(s.getOutputStream(), 8192);

                String line;
                while ((line = in.readLine()) != null) {   // null == client closed the stream
                    handleCommand(line, in, out);
                    out.flush(); // exactly one flush per command -> the client gets a full reply
                }
            } catch (IOException e) {
                // Client disconnected, or sent a truncated/unframable request.
                // Nothing left to do but let this connection's thread finish.
            }
        }

        /** Parse the command line and dispatch. */
        private void handleCommand(String line, ProtocolReader in, OutputStream out) throws IOException {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                return; // ignore blank lines
            }
            // Commands are ASCII and space-delimited: "<cmd> <arg1> <arg2> ...".
            String[] parts = trimmed.split("\\s+");
            switch (parts[0]) {                    // case-sensitive on purpose
                case "get": handleGet(parts, out);     break;
                case "set": handleSet(parts, in, out); break;
                default:    writeAscii(out, "ERROR\n"); // unknown command
            }
        }

        /**
         * get <key1> [<key2> ...]
         *
         * Emits one "VALUE <key> <n>\n<n bytes>\n" block for each EXISTING key,
         * in request order, followed by a single END\n. Missing keys contribute
         * nothing. A single key is just the N==1 case of the multi-key form.
         */
        private void handleGet(String[] parts, OutputStream out) throws IOException {
            if (parts.length < 2) {                     // need at least one key
                writeAscii(out, "ERROR\n");
                return;
            }
            // Validate all keys up front so we never emit a partial response and
            // then abort with an error midway through.
            for (int i = 1; i < parts.length; i++) {
                if (!isValidKey(parts[i])) {
                    writeAscii(out, "ERROR\n");
                    return;
                }
            }
            for (int i = 1; i < parts.length; i++) {
                String key = parts[i];
                byte[] value = store.get(key);
                if (value != null) {                    // skip missing keys entirely
                    writeAscii(out, "VALUE " + key + " " + value.length + "\n");
                    out.write(value);                   // the raw value bytes, verbatim
                    writeAscii(out, "\n");              // newline that ends this data block
                }
            }
            writeAscii(out, "END\n");                    // one END terminates the whole reply
        }

        /**
         * set <key> <byte_count>\n<byte_count bytes>\n  ->  STORED\n
         *
         * We ALWAYS consume the framed payload (bytes + trailing newline) before
         * replying, even on a bad key, so the TCP stream stays byte-aligned for
         * whatever command follows.
         */
        private void handleSet(String[] parts, ProtocolReader in, OutputStream out) throws IOException {
            if (parts.length != 3) {
                writeAscii(out, "ERROR\n");
                return;
            }
            String key = parts[1];
            long byteCount = parseByteCount(parts[2]);

            if (byteCount < 0) {
                // Length is unparseable -> we don't know where the payload ends,
                // so we cannot stay framed. Report the error and read the next line.
                writeAscii(out, "ERROR\n");
                return;
            }
            if (byteCount > MAX_VALUE_BYTES) {
                // Too large to cache, but still DRAIN it (without allocating a
                // giant array) + the trailing newline so the next command frames.
                in.skipExactly(byteCount);
                in.consumeNewline();
                writeAscii(out, "SERVER_ERROR object too large for cache\n");
                return;
            }

            // The heart of the protocol: read EXACTLY byteCount bytes (which may
            // contain '\n' or raw binary), then consume the terminating newline.
            byte[] value = in.readExactly((int) byteCount);
            in.consumeNewline();

            if (!isValidKey(key)) {
                // Payload already consumed above, so the stream is still framed.
                writeAscii(out, "ERROR\n");
                return;
            }
            store.put(key, value);
            writeAscii(out, "STORED\n");
        }
    }

    // ------------------------------------------------------------------
    // Small helpers
    // ------------------------------------------------------------------

    /** Keys must be non-empty and consist only of [A-Za-z0-9_-]. Allocation-free. */
    private static boolean isValidKey(String key) {
        if (key.isEmpty()) return false;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            boolean ok = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                      || (c >= '0' && c <= '9') || c == '_' || c == '-';
            if (!ok) return false;
        }
        return true;
    }

    /** Parse a non-negative byte count; return -1 if it is not a valid count. */
    private static long parseByteCount(String s) {
        try {
            long n = Long.parseLong(s);
            return n < 0 ? -1 : n;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Write an ASCII control/response string to the client. */
    private static void writeAscii(OutputStream out, String s) throws IOException {
        out.write(s.getBytes(StandardCharsets.US_ASCII));
    }

    // ------------------------------------------------------------------
    // ProtocolReader
    //
    // A hand-rolled buffered reader over the socket InputStream that supports
    // BOTH line reads (for command lines) and byte-exact reads (for values)
    // out of the SAME buffer. This is the key to correct framing: a standard
    // BufferedReader reads ahead and decodes characters, so after reading a
    // command line it would have already pulled the value's bytes out of the
    // stream (and mangled any non-ASCII). Owning the buffer lets each call
    // consume precisely the bytes it needs and no more.
    // ------------------------------------------------------------------
    static final class ProtocolReader implements Closeable {
        private final InputStream in;
        private final byte[] buf;
        private int pos;    // index of next unread byte in buf
        private int limit;  // number of valid bytes currently in buf

        ProtocolReader(InputStream in, int bufferSize) {
            this.in = in;
            this.buf = new byte[bufferSize];
        }

        /** Refill buf from the underlying stream. Returns false at EOF. */
        private boolean fill() throws IOException {
            pos = 0;
            limit = in.read(buf, 0, buf.length);   // only called when buffer is empty
            if (limit <= 0) { limit = 0; return false; }
            return true;
        }

        private int readByte() throws IOException {
            if (pos >= limit && !fill()) throw new EOFException("unexpected end of stream");
            return buf[pos++] & 0xFF;
        }

        /**
         * Read one command line terminated by '\n'. The '\n' (and an optional
         * preceding '\r', so telnet/CRLF clients work too) is consumed but not
         * returned. Returns null at a clean end-of-stream.
         */
        String readLine() throws IOException {
            ByteArrayOutputStream line = null;
            while (true) {
                if (pos >= limit && !fill()) {
                    if (line == null) return null;   // clean EOF, nothing buffered
                    break;                            // EOF right after a partial final line
                }
                byte b = buf[pos++];
                if (b == '\n') break;
                if (line == null) line = new ByteArrayOutputStream(64);
                line.write(b);
            }
            if (line == null) return "";             // an empty line ("\n" on its own)
            String s = new String(line.toByteArray(), StandardCharsets.US_ASCII);
            return s.endsWith("\r") ? s.substring(0, s.length() - 1) : s;
        }

        /** Read EXACTLY n bytes into a fresh array, or throw EOFException. */
        byte[] readExactly(int n) throws IOException {
            byte[] out = new byte[n];
            int off = 0;
            while (off < n) {                        // loop because one read() may return < n
                if (pos >= limit && !fill())
                    throw new EOFException("stream ended after " + off + " of " + n + " bytes");
                int toCopy = Math.min(limit - pos, n - off);
                System.arraycopy(buf, pos, out, off, toCopy);
                pos += toCopy;
                off += toCopy;
            }
            return out;
        }

        /** Discard EXACTLY n bytes without allocating storage for them. */
        void skipExactly(long n) throws IOException {
            long remaining = n;
            while (remaining > 0) {
                if (pos >= limit && !fill())
                    throw new EOFException("stream ended while skipping payload");
                int toSkip = (int) Math.min(limit - pos, remaining);
                pos += toSkip;
                remaining -= toSkip;
            }
        }

        /** Consume the newline that terminates a value payload ('\n' or '\r\n'). */
        void consumeNewline() throws IOException {
            int b = readByte();
            if (b == '\r') b = readByte();
            if (b != '\n')
                throw new IOException("expected newline after value payload, got byte " + b);
        }

        @Override public void close() throws IOException { in.close(); }
    }
}

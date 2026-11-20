import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reddit — Staff Software Engineer, Coding Interview
 * Problem: Word Search  (LeetCode 79 family)
 *
 * One file, every part of the "family". Each part is optimal and self-contained;
 * DFS helpers are suffixed per-part so nothing collides.
 *
 *   Part 1  exist(board, word)          -> boolean       [the asked problem]
 *   Part 2  findWords(board, words)     -> List<String>  [Word Search II, Trie]
 *   Part 3  findPath(board, word)       -> List<int[]>   [return the actual path]
 *   Part 4  existWithReuse(board, word) -> boolean       [cells reusable -> memo/DP]
 *
 * Run:  javac WordSearch.java && java WordSearch
 */
public class WordSearch {

    // 4-directional moves shared by all parts: up, down, left, right.
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // =====================================================================
    // Part 1 — Word Search (the asked problem)
    // ---------------------------------------------------------------------
    // Try every cell as a start; from a first-char match, DFS + backtrack.
    // Visited cells are marked IN PLACE with a sentinel ('#') — the grid holds
    // only letters, so '#' can never be a real match. That removes the need for
    // a separate visited[][] matrix: we spend only O(word length) stack space.
    //
    // Time:  O(M * N * 3^L)  (M*N starts; <=3 fresh directions per step, L deep)
    // Space: O(L)            (recursion depth; in-place marking, no visited[][])
    // =====================================================================
    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || board[0].length == 0) return false;
        if (word == null || word.isEmpty()) return true; // empty word is vacuously present

        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Micro-optimization: only launch a DFS where the first char matches.
                if (board[r][c] == word.charAt(0) && dfsExist(board, word, r, c, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsExist(char[][] board, String word, int r, int c, int idx) {
        // Out of bounds -> this path fails.
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        // Char mismatch. (Visited cells hold '#', so they mismatch here too.)
        if (board[r][c] != word.charAt(idx)) return false;
        // Matched the final character -> the whole word is present.
        if (idx == word.length() - 1) return true;

        char original = board[r][c];
        board[r][c] = '#';                 // mark visited for the current path

        boolean found = false;
        for (int[] d : DIRS) {             // try all four neighbors for word[idx+1]
            if (dfsExist(board, word, r + d[0], c + d[1], idx + 1)) {
                found = true;
                break;
            }
        }

        board[r][c] = original;            // backtrack: unmark so other paths can use it
        return found;
    }

    // =====================================================================
    // Part 2 — Word Search II  (find ALL of `words` present on the board)
    // ---------------------------------------------------------------------
    // CHANGES vs Part 1:
    //   * Running Part 1 once per word re-scans the whole board K times. Instead
    //     build a TRIE of all words and DFS ONCE per cell, walking the Trie in
    //     lockstep, so shared prefixes are matched a single time.
    //   * The Trie node stores the finished word, so we never rebuild strings.
    //   * Two prunings: (a) stop the instant no Trie branch continues with the
    //     current char; (b) delete exhausted Trie leaves so later DFS stops sooner.
    //   * Uses a Map<Character,..> Trie so it handles mixed case (the stated
    //     constraint). For a fixed small alphabet a TrieNode[52] array is faster.
    //
    // Time:  ~O(M * N * 4 * 3^(Lmax-1)) worst case, but Trie sharing + pruning
    //        make it dramatically faster than K independent Part-1 searches.
    // Space: O(sum of word lengths) for the Trie + O(Lmax) recursion.
    // =====================================================================
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word;   // non-null ONLY at a node that terminates a word
    }

    public List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0
                || words == null || words.length == 0) {
            return result;
        }

        TrieNode root = buildTrie(words);
        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dfsTrie(board, r, c, root, result);
            }
        }
        return result;
    }

    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode node = root;
            for (int i = 0; i < w.length(); i++) {
                char ch = w.charAt(i);
                node = node.children.computeIfAbsent(ch, k -> new TrieNode());
            }
            node.word = w;   // mark end-of-word with the full string
        }
        return root;
    }

    private void dfsTrie(char[][] board, int r, int c, TrieNode parent, List<String> result) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;
        char ch = board[r][c];
        if (ch == '#') return;                     // already on the current path

        TrieNode node = parent.children.get(ch);
        if (node == null) return;                  // no word continues here -> prune

        if (node.word != null) {                   // reached a complete word
            result.add(node.word);
            node.word = null;                      // null it out so we never add duplicates
        }

        board[r][c] = '#';                         // mark visited
        for (int[] d : DIRS) {
            dfsTrie(board, r + d[0], c + d[1], node, result);
        }
        board[r][c] = ch;                          // backtrack

        // Prune: if this node holds no word and has no children left, detach it
        // from its parent so future traversals never revisit this dead branch.
        if (node.word == null && node.children.isEmpty()) {
            parent.children.remove(ch);
        }
    }

    // =====================================================================
    // Part 3 — Return the actual path of cells (not just true/false)
    // ---------------------------------------------------------------------
    // CHANGES vs Part 1:
    //   * Carry a `path` list. Push the cell right after it matches; on a dead
    //     end pop it (backtrack); on a full match return WITHOUT popping so the
    //     completed path survives as the recursion unwinds. Returns [] if absent.
    // Time / Space: same as Part 1, plus O(L) for the returned path.
    // =====================================================================
    public List<int[]> findPath(char[][] board, String word) {
        List<int[]> path = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0
                || word == null || word.isEmpty()) {
            return path;
        }
        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == word.charAt(0) && dfsPath(board, word, r, c, 0, path)) {
                    return path;          // first full match wins
                }
            }
        }
        return path;                      // empty -> not found
    }

    private boolean dfsPath(char[][] board, String word, int r, int c, int idx, List<int[]> path) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        path.add(new int[]{r, c});        // CHANGE: record this matched cell
        if (idx == word.length() - 1) return true;   // keep path on success (no pop)

        char original = board[r][c];
        board[r][c] = '#';
        for (int[] d : DIRS) {
            if (dfsPath(board, word, r + d[0], c + d[1], idx + 1, path)) {
                board[r][c] = original;
                return true;
            }
        }
        board[r][c] = original;
        path.remove(path.size() - 1);     // CHANGE: dead end -> undo the record
        return false;
    }

    // =====================================================================
    // Part 4 — Variant: the SAME cell MAY be reused
    // ---------------------------------------------------------------------
    // KEY INSIGHT: In Parts 1-3 we CANNOT memoize on (r, c, idx) because the
    // answer depends on which cells the current path already consumed (history).
    // Once reuse is allowed that history disappears: "can I finish word[idx..]
    // starting at (r,c)?" becomes a pure function of (r, c, idx) -> we can
    // MEMOIZE, and the exponential search collapses to polynomial DP.
    //
    // Time:  O(M * N * L)  (each (r,c,idx) state is solved once)
    // Space: O(M * N * L)  memo + O(L) recursion. (idx strictly increases, so
    //        even with reuse the recursion is acyclic and always terminates.)
    // =====================================================================
    public boolean existWithReuse(char[][] board, String word) {
        if (board == null || board.length == 0 || board[0].length == 0) return false;
        if (word == null || word.isEmpty()) return true;

        int rows = board.length, cols = board[0].length, L = word.length();
        byte[][][] memo = new byte[rows][cols][L];   // 0 unknown, 1 true, 2 false
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == word.charAt(0) && canReuse(board, word, r, c, 0, memo)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canReuse(char[][] b, String w, int r, int c, int idx, byte[][][] memo) {
        if (r < 0 || r >= b.length || c < 0 || c >= b[0].length) return false;
        if (b[r][c] != w.charAt(idx)) return false;
        if (idx == w.length() - 1) return true;
        if (memo[r][c][idx] != 0) return memo[r][c][idx] == 1;   // reuse cached answer

        boolean ans = false;
        for (int[] d : DIRS) {
            if (canReuse(b, w, r + d[0], c + d[1], idx + 1, memo)) { ans = true; break; }
        }
        memo[r][c][idx] = (byte) (ans ? 1 : 2);
        return ans;
    }

    // =====================================================================
    // Demo / self-check  (hand-verified expectations in comments)
    // =====================================================================
    public static void main(String[] args) {
        WordSearch sol = new WordSearch();

        char[][] board = {
            {'A', 'B', 'C', 'D'},
            {'S', 'A', 'A', 'T'},
            {'A', 'C', 'A', 'E'}
        };

        System.out.println("Part 1 — exist(board, word):");
        System.out.println("  CAT   -> " + sol.exist(board, "CAT")   + "   (expect true)");
        System.out.println("  BAT   -> " + sol.exist(board, "BAT")   + "   (expect false)");
        System.out.println("  ABCD  -> " + sol.exist(board, "ABCD")  + "   (expect true)");
        System.out.println("  ABCDA -> " + sol.exist(board, "ABCDA") + "   (expect false)");

        System.out.println("\nPart 2 — findWords(board, words):");
        String[] words = {"CAT", "ABCD", "SACA", "AAB", "XYZ"};
        System.out.println("  words=" + java.util.Arrays.toString(words));
        System.out.println("  found -> " + sol.findWords(board, words)
                + "   (expect CAT, ABCD, SACA, AAB in some order; XYZ absent)");

        System.out.println("\nPart 3 — findPath(board, word):");
        System.out.println("  CAT path -> " + pathToString(sol.findPath(board, "CAT"))
                + "   (expect (0,2)->(1,2)->(1,3))");
        System.out.println("  BAT path -> " + pathToString(sol.findPath(board, "BAT"))
                + "   (expect [] not found)");

        System.out.println("\nPart 4 — existWithReuse(board, word):");
        System.out.println("  ABABAB (reuse)    -> " + sol.existWithReuse(board, "ABABAB")
                + "   (expect true)");
        System.out.println("  ABABAB (no reuse) -> " + sol.exist(board, "ABABAB")
                + "   (expect false; only one 'B' exists)");
        System.out.println("  XYZ    (reuse)    -> " + sol.existWithReuse(board, "XYZ")
                + "   (expect false)");
    }

    private static String pathToString(List<int[]> path) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append("(").append(path.get(i)[0]).append(",").append(path.get(i)[1]).append(")");
        }
        return sb.append("]").toString();
    }
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Word Search II  —  LeetCode 212 family
 * ------------------------------------------------------------------------
 * Given a 2-D lowercase grid `board` (<= 12 x 12) and up to 30,000 words
 * (each length <= 10), return every word that can be traced along a path of
 * horizontally / vertically adjacent cells, using each cell at most once
 * within a single word.
 *
 * WHY NOT NAIVE:
 *   Searching the grid separately for each word is
 *       O(#words * #cells * 4^L)          (L = word length)
 *   which explodes for 30,000 words.
 *
 * KEY IDEA (both parts):
 *   Put ALL words into a trie, then run ONE DFS over the grid that walks the
 *   trie in lockstep. The instant the letters on the current path stop
 *   spelling a valid trie prefix, we abandon that branch. Grid-traversal cost
 *   becomes INDEPENDENT of the number of words — only trie construction scales
 *   with the word count. That is exactly what makes 30,000 words tractable.
 *
 *   - Part 1: trie + single grid DFS + de-dup.
 *   - Part 2: everything in Part 1, PLUS trie leaf-pruning that physically
 *             shrinks the trie as words are found (a strong constant-factor
 *             speedup, especially with a large, prefix-heavy word list).
 *
 * COMPLEXITY (both parts, same Big-O):
 *   Build trie : O(sum of word lengths)            <= 30,000 * 10 = 3e5
 *   Grid DFS   : O(M * N * 4 * 3^(L-1))
 *                (from each of M*N start cells: 4 first moves, then <= 3 new
 *                 directions per step because the previous cell is marked used;
 *                 depth is capped at L because the trie has no nodes beyond
 *                 the longest word). With M,N <= 12, L <= 10 this is tiny.
 * ------------------------------------------------------------------------
 */
public class WordSearchII {

    /* =====================================================================
     * PART 1 — Trie + single grid DFS   (asymptotically optimal)
     * ===================================================================== */
    static class Part1Solution {

        /** Trie node over the 26-letter lowercase alphabet. */
        private static class TrieNode {
            TrieNode[] children = new TrieNode[26]; // one slot per 'a'..'z'
            String word = null;                     // set ONLY at a word's final char
        }

        private char[][] board;
        private int rows, cols;
        private List<String> result;

        public List<String> findWords(char[][] board, String[] words) {
            result = new ArrayList<>();
            if (board == null || board.length == 0 || board[0].length == 0) return result;
            this.board = board;
            this.rows  = board.length;
            this.cols  = board[0].length;

            TrieNode root = buildTrie(words);

            // Try to start a match at every cell; the trie prunes dead branches.
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    dfs(r, c, root);
                }
            }
            return result;
        }

        /** Insert each word; stash the full word string at its terminal node. */
        private TrieNode buildTrie(String[] words) {
            TrieNode root = new TrieNode();
            for (String w : words) {
                TrieNode node = root;
                for (int i = 0; i < w.length(); i++) {
                    int idx = w.charAt(i) - 'a';
                    if (node.children[idx] == null) node.children[idx] = new TrieNode();
                    node = node.children[idx];
                }
                node.word = w; // marks end-of-word AND records which word it is
            }
            return root;
        }

        /**
         * DFS from cell (r,c) while sitting on trie node `parent`.
         * Descends into parent.children[letter]; returns immediately if that
         * child is absent (prefix not in the dictionary) — this is the pruning.
         */
        private void dfs(int r, int c, TrieNode parent) {
            char ch = board[r][c];
            if (ch == '#') return;                    // cell already used on this path
            TrieNode node = parent.children[ch - 'a'];
            if (node == null) return;                 // prefix falls off the trie -> prune

            if (node.word != null) {                  // a complete word ends here
                result.add(node.word);
                node.word = null;                     // de-dup: emit each word at most once
            }

            board[r][c] = '#';                        // mark visited (restored on backtrack)
            if (r > 0)        dfs(r - 1, c, node);    // up
            if (r + 1 < rows) dfs(r + 1, c, node);    // down
            if (c > 0)        dfs(r, c - 1, node);    // left
            if (c + 1 < cols) dfs(r, c + 1, node);    // right
            board[r][c] = ch;                         // backtrack: restore the cell
        }
    }

    /* =====================================================================
     * PART 2 — Part 1 + trie LEAF-PRUNING
     *
     * WHAT CHANGED vs PART 1 (3 spots, each marked  // <-- NEW):
     *   1. TrieNode gains `int childCount` (number of live children).
     *   2. buildTrie() increments childCount whenever it creates a child.
     *   3. dfs() ends by DELETING `node` from its parent once the node has no
     *      children left and marks no (still-unfound) word. That subtree is
     *      spent, so detaching it makes every later DFS branch die one step
     *      sooner. The deletions cascade upward, one recursion frame at a time.
     *
     * Same Big-O as Part 1, but a large practical speedup: as words are found,
     * the trie literally shrinks, so most later start cells hit a null child
     * almost immediately.
     *
     * WHY PRUNING IS SAFE:
     *   childCount == 0  => there are no words anywhere below this node.
     *   word == null     => no (unfound) word ends at this node either.
     *   Together they mean the entire subtree is exhausted — nothing left to
     *   find through it — so it can be detached without losing any answer.
     * ===================================================================== */
    static class Part2Solution {

        private static class TrieNode {
            TrieNode[] children = new TrieNode[26];
            String word = null;
            int childCount = 0;                       // <-- NEW: live child count
        }

        private char[][] board;
        private int rows, cols;
        private List<String> result;

        public List<String> findWords(char[][] board, String[] words) {
            result = new ArrayList<>();
            if (board == null || board.length == 0 || board[0].length == 0) return result;
            this.board = board;
            this.rows  = board.length;
            this.cols  = board[0].length;

            TrieNode root = buildTrie(words);
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    dfs(r, c, root);
                }
            }
            return result;
        }

        private TrieNode buildTrie(String[] words) {
            TrieNode root = new TrieNode();
            for (String w : words) {
                TrieNode node = root;
                for (int i = 0; i < w.length(); i++) {
                    int idx = w.charAt(i) - 'a';
                    if (node.children[idx] == null) {
                        node.children[idx] = new TrieNode();
                        node.childCount++;            // <-- NEW: keep the count in sync
                    }
                    node = node.children[idx];
                }
                node.word = w;
            }
            return root;
        }

        private void dfs(int r, int c, TrieNode parent) {
            char ch = board[r][c];
            if (ch == '#') return;
            int idx = ch - 'a';
            TrieNode node = parent.children[idx];
            if (node == null) return;

            if (node.word != null) {
                result.add(node.word);
                node.word = null;
            }

            board[r][c] = '#';
            if (r > 0)        dfs(r - 1, c, node);
            if (r + 1 < rows) dfs(r + 1, c, node);
            if (c > 0)        dfs(r, c - 1, node);
            if (c + 1 < cols) dfs(r, c + 1, node);
            board[r][c] = ch;

            // <-- NEW: leaf-prune. If this node is now childless and marks no
            //          word, its whole subtree is exhausted -> detach it. The
            //          parent's count drops, so the parent may become prunable
            //          when ITS own dfs frame reaches this same check.
            if (node.childCount == 0 && node.word == null) {
                parent.children[idx] = null;
                parent.childCount--;
            }
        }
    }

    /* ============================ demo / self-check ====================== */
    public static void main(String[] args) {
        char[][] board1 = {
            {'a', 'b', 'c', 'd'},
            {'s', 'a', 'a', 't'},
            {'a', 'c', 'k', 'e'},
            {'a', 'c', 'd', 'n'}
        };
        String[] words1 = {"bat", "cat", "back", "backend", "stack"}; // expect: back, backend, cat

        char[][] board2 = {
            {'x', 'o'},
            {'x', 'o'}
        };
        String[] words2 = {"xoxo"};                                   // expect: (none)

        System.out.println("PART 1  (trie + single DFS)");
        System.out.println("  Example 1 -> " + sorted(new Part1Solution().findWords(copy(board1), words1)));
        System.out.println("  Example 2 -> " + sorted(new Part1Solution().findWords(copy(board2), words2)));

        System.out.println("PART 2  (trie + single DFS + leaf pruning)");
        System.out.println("  Example 1 -> " + sorted(new Part2Solution().findWords(copy(board1), words1)));
        System.out.println("  Example 2 -> " + sorted(new Part2Solution().findWords(copy(board2), words2)));
    }

    /** DFS restores the board, but we copy anyway so each demo run is independent. */
    private static char[][] copy(char[][] src) {
        char[][] out = new char[src.length][];
        for (int i = 0; i < src.length; i++) out[i] = src[i].clone();
        return out;
    }

    /** Sort only for a deterministic printout — the problem accepts any order. */
    private static List<String> sorted(List<String> xs) {
        Collections.sort(xs);
        return xs;
    }
}

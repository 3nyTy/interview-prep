/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Tokenize"  (see tokenize-prompt.txt)
 *
 * A vocab maps token strings -> integer ids, plus a sentinel "UNK" -> -1.
 * Tokenize a text string into ids; detokenize ids back to text.
 *
 * This single file contains ALL parts the interview walks through:
 *   PART 1  tokenizeV1 / detokenize  — the ORIGINAL code (first/shortest-match),
 *                                      ported from Python so we can RUN it and
 *                                      demonstrate its data-loss bugs.
 *   PART 2  tokenizeV2               — another engineer's attempted UNK "fix";
 *                                      ported so we can show it is STILL broken.
 *   PART 3a tokenizeLongest          — proper longest-match tokenizer, bounded by
 *                                      max vocab length, per-char UNK fallback.
 *   PART 3b tokenizeLongestDedup     — 3a + collapse runs of consecutive UNKs.
 *   PART 3c tokenizeTrie             — same longest-match semantics, trie-
 *                                      accelerated from O(n·L²) to O(n·L).
 *   PART 3d tokenizeOptimal          — DP segmentation that truly minimises the
 *                                      token count (beats greedy longest-match).
 *
 * `main` runs each part on adversarial fixtures, prints the behaviour, and
 * asserts the expected results (incl. the prompt's apple/app and appbbbapp
 * cases). Verified on OpenJDK Corretto 21.
 *
 *   Compile       : javac Tokenizer.java
 *   Run self-test : java Tokenizer
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {

    /** The sentinel string; by convention it lives in vocab as "UNK" -> -1 and must NEVER be matched. */
    private static final String UNK_KEY = "UNK";

    private static int unkId(Map<String, Integer> vocab) {
        return vocab.getOrDefault(UNK_KEY, -1);
    }

    // =========================================================================
    // PART 1 — ORIGINAL code (ported from the prompt's Python).
    // Greedy FIRST/SHORTEST match: grow `key` one char at a time and emit the
    // moment it lands in vocab. BUGS this demonstrates:
    //   (a) any char not covered by vocab makes `key` grow forever -> the
    //       trailing `key` is SILENTLY DROPPED at the end (data loss).
    //   (b) it is first-match, not longest-match: vocab {"a","ab"} + "ab"
    //       emits [a] then drops "b".
    //   (c) no UNK handling at all.
    // =========================================================================
    static List<Integer> tokenizeV1(String text, Map<String, Integer> vocab) {
        List<Integer> tokens = new ArrayList<>();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            key.append(text.charAt(i));
            if (vocab.containsKey(key.toString())) {
                tokens.add(vocab.get(key.toString()));
                key.setLength(0);            // reset on match
            }
        }
        // BUG: if key is non-empty here (an in-progress / unmatchable run) it is
        // thrown away with no token emitted -> the round trip loses characters.
        return tokens;
    }

    /** detokenize: invert the vocab and concatenate. Shared by all parts. */
    static String detokenize(List<Integer> tokens, Map<String, Integer> vocab) {
        Map<Integer, String> reversed = new HashMap<>();
        for (Map.Entry<String, Integer> e : vocab.entrySet()) {
            reversed.put(e.getValue(), e.getKey());   // id -> token string (UNK id -> "UNK")
        }
        StringBuilder sb = new StringBuilder();
        for (int t : tokens) {
            sb.append(reversed.getOrDefault(t, UNK_KEY));
        }
        return sb.toString();
    }

    // =========================================================================
    // PART 2 — another engineer's attempted FIX (ported from the prompt).
    // CHANGE FROM PART 1 (only this block was added):
    //     if (key.length() > 0) tokens.add(unkId);   // emit ONE UNK at the end
    // Plus "UNK":-1 was added to vocab.
    //
    // WHY IT'S STILL BROKEN (shown in main):
    //   - It only catches a TRAILING unmatched key; mid-string unknowns are not
    //     handled until the loop ends.
    //   - Once a mismatch starts, `key` keeps absorbing chars — including ones
    //     that ARE in vocab — and the whole run collapses to a SINGLE UNK, so
    //     valid tokens inside the run (e.g. the "b" in "ebe") are swallowed.
    //   - Worse, "UNK" is now a vocab key, so a literal "UNK" in the text would
    //     itself be matched as the sentinel (sentinel participating in matching).
    // =========================================================================
    static List<Integer> tokenizeV2(String text, Map<String, Integer> vocab) {
        List<Integer> tokens = new ArrayList<>();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            key.append(text.charAt(i));
            if (vocab.containsKey(key.toString())) {
                tokens.add(vocab.get(key.toString()));
                key.setLength(0);
            }
        }
        if (key.length() > 0) {                       // NEW vs PART 1
            tokens.add(unkId(vocab));                 // NEW: one UNK for the whole trailing run
        }
        return tokens;
    }

    // =========================================================================
    // PART 3a — PROPER longest-match tokenizer.   *** the real implementation ***
    // CHANGES FROM PART 2:
    //   - Drive by an explicit cursor `i`, not an accumulating `key`.
    //   - At each position take the LONGEST vocab prefix (maximal munch), not the
    //     first/shortest -> "apple" tokenizes to [apple], not [app]+...
    //   - Bound the inner scan by maxLen = longest matchable key (don't scan to
    //     end of text).  [1p3a "bound by max vocab length" iteration]
    //   - If nothing matches, emit UNK for exactly ONE char and advance by 1, so
    //     unknown chars never swallow neighbouring valid tokens.
    //   - The UNK sentinel is EXCLUDED from matching (never matched literally).
    // Complexity: O(n · maxLen²) (substring + hash lookup per candidate).
    // =========================================================================
    static List<Integer> tokenizeLongest(String text, Map<String, Integer> vocab) {
        List<Integer> tokens = new ArrayList<>();
        int unk = unkId(vocab);
        int maxLen = maxMatchableLen(vocab);

        int i = 0, n = text.length();
        while (i < n) {
            int matchedId = unk, matchedLen = 0;
            int hi = Math.min(n, i + maxLen);
            // Scan longest -> shortest; the first hit is the longest match.
            for (int j = hi; j > i; j--) {
                String sub = text.substring(i, j);
                if (!UNK_KEY.equals(sub) && vocab.containsKey(sub)) {  // sentinel excluded
                    matchedId = vocab.get(sub);
                    matchedLen = j - i;
                    break;
                }
            }
            if (matchedLen > 0) {
                tokens.add(matchedId);
                i += matchedLen;
            } else {
                tokens.add(unk);    // unknown: emit UNK for ONE char only
                i += 1;
            }
        }
        return tokens;
    }

    // =========================================================================
    // PART 3b — longest-match + COLLAPSE consecutive UNKs.
    // CHANGE FROM PART 3a: a run of UNK ids folds into a single UNK in the output
    //   ("appbbbapp" -> [1,-1,-1,-1,1] becomes [1,-1,1]).  [1p3a dedup iteration]
    // Implemented as a post-process pass (clear and easy to reason about; an
    // in-loop "skip emit if previous was UNK" gives the same result in one pass).
    // =========================================================================
    static List<Integer> tokenizeLongestDedup(String text, Map<String, Integer> vocab) {
        int unk = unkId(vocab);
        List<Integer> base = tokenizeLongest(text, vocab);   // reuse 3a
        List<Integer> out = new ArrayList<>();
        for (int t : base) {
            boolean prevWasUnk = !out.isEmpty() && out.get(out.size() - 1) == unk;
            if (t == unk && prevWasUnk) continue;            // skip repeats in a UNK run
            out.add(t);
        }
        return out;
    }

    // =========================================================================
    // PART 3c — TRIE-accelerated longest match (identical output to 3a, faster).
    // CHANGE FROM PART 3a: replace the per-position substring scan with a single
    // walk down a prefix tree built once from the vocab. Each position descends
    // at most maxLen edges -> overall O(n · maxLen), no substring allocation.
    // This is the "textbook" asymptotic improvement the interviewer looks for.
    // =========================================================================
    static final class TrieNode {
        final Map<Character, TrieNode> children = new HashMap<>();
        boolean isToken = false;
        int id;
    }

    static final class Trie {
        final TrieNode root = new TrieNode();
        /** Insert every vocab entry EXCEPT the UNK sentinel (it must not be matchable). */
        Trie(Map<String, Integer> vocab) {
            for (Map.Entry<String, Integer> e : vocab.entrySet()) {
                if (UNK_KEY.equals(e.getKey())) continue;
                TrieNode cur = root;
                for (char c : e.getKey().toCharArray()) {
                    cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
                }
                cur.isToken = true;
                cur.id = e.getValue();
            }
        }
    }

    static List<Integer> tokenizeTrie(String text, Map<String, Integer> vocab, Trie trie) {
        List<Integer> tokens = new ArrayList<>();
        int unk = unkId(vocab);
        int i = 0, n = text.length();
        while (i < n) {
            TrieNode cur = trie.root;
            int bestLen = 0, bestId = unk;
            int j = i;
            // Walk down the trie as far as the text allows, remembering the
            // deepest node that is a complete token (= longest match).
            while (j < n && cur.children.containsKey(text.charAt(j))) {
                cur = cur.children.get(text.charAt(j));
                j++;
                if (cur.isToken) {
                    bestLen = j - i;
                    bestId = cur.id;
                }
            }
            if (bestLen > 0) {
                tokens.add(bestId);
                i += bestLen;
            } else {
                tokens.add(unk);
                i += 1;
            }
        }
        return tokens;
    }

    // =========================================================================
    // PART 3d — OPTIMAL segmentation via dynamic programming.
    // Greedy longest-match is NOT always best: vocab {"aaa","aa","a","ab"} +
    // "aab" -> longest grabs "aa" then is stuck with "b" => [aa, UNK]; but
    // [a, ab] covers everything with NO unknowns. So the meaningful objective is
    // LEXICOGRAPHIC: minimise #UNK first (maximise real coverage), then #tokens.
    //
    // We fold that pair into one integer cost = unkCount*BIG + tokenCount, with
    // BIG = n+1 (token count can never exceed n), so one UNK always outweighs any
    // number of extra real tokens. Every char is coverable by UNK, so a solution
    // always exists. dp[i] = min cost for suffix text[i:]; reconstruct via cut/idAt.
    // Complexity O(n · maxLen) states×transitions (same substring caveat as 3a).
    // =========================================================================
    static List<Integer> tokenizeOptimal(String text, Map<String, Integer> vocab) {
        int n = text.length();
        int unk = unkId(vocab);
        int maxLen = maxMatchableLen(vocab);
        final long BIG = n + 1L;        // weight: one UNK dominates the token count

        long[] dp = new long[n + 1];    // dp[i] = min (unk*BIG + tokens) for suffix at i
        int[] cut = new int[n + 1];     // chosen token length at i
        int[] idAt = new int[n + 1];    // chosen token id at i
        dp[n] = 0;

        for (int i = n - 1; i >= 0; i--) {
            // Option 1: treat one char as UNK (always available) -> adds BIG + 1.
            dp[i] = dp[i + 1] + BIG + 1;
            cut[i] = 1;
            idAt[i] = unk;
            // Option 2: any real vocab token starting here -> adds just 1.
            int hi = Math.min(n, i + maxLen);
            for (int j = i + 1; j <= hi; j++) {
                String sub = text.substring(i, j);
                if (!UNK_KEY.equals(sub) && vocab.containsKey(sub) && dp[j] + 1 < dp[i]) {
                    dp[i] = dp[j] + 1;
                    cut[i] = j - i;
                    idAt[i] = vocab.get(sub);
                }
            }
        }
        // Reconstruct the chosen segmentation.
        List<Integer> tokens = new ArrayList<>();
        for (int i = 0; i < n; i += cut[i]) {
            tokens.add(idAt[i]);
        }
        return tokens;
    }

    /** Longest key length among MATCHABLE tokens (UNK sentinel excluded). 0 if none. */
    private static int maxMatchableLen(Map<String, Integer> vocab) {
        int maxLen = 0;
        for (String k : vocab.keySet()) {
            if (!UNK_KEY.equals(k)) maxLen = Math.max(maxLen, k.length());
        }
        return maxLen;
    }

    // =========================================================================
    // Self-test harness (not part of the interview answer)
    // =========================================================================
    public static void main(String[] args) {
        boolean ok = true;

        // ---- vocab from the original prompt (no UNK) for PART 1 demonstration ----
        Map<String, Integer> v0 = orderedVocab("a", 1, "b", 2, "cd", 3);

        System.out.println("=== PART 1: original code is lossy (first/shortest-match, drops tail) ===");
        List<Integer> p1 = tokenizeV1("acdebe", v0);
        System.out.println("  tokenizeV1(\"acdebe\") = " + p1 + "  detok = \"" + detokenize(p1, v0) + "\"");
        System.out.println("  -> 'e','b','e' silently dropped; round trip \"acdebe\" -> \"acd\". ");
        ok &= eq(p1, 1, 3);

        // ---- vocab WITH UNK for PART 2 ----
        Map<String, Integer> v1 = orderedVocab("a", 1, "b", 2, "cd", 3, UNK_KEY, -1);

        System.out.println("\n=== PART 2: the UNK \"fix\" is still broken ===");
        List<Integer> p2a = tokenizeV2("acdebe", v1);
        System.out.println("  tokenizeV2(\"acdebe\") = " + p2a + "  detok = \"" + detokenize(p2a, v1) + "\"");
        System.out.println("  -> whole \"ebe\" run collapses to ONE UNK; the valid 'b' is swallowed.");
        ok &= eq(p2a, 1, 3, -1);
        List<Integer> p2b = tokenizeV2("xab", v1);
        System.out.println("  tokenizeV2(\"xab\")    = " + p2b + "  detok = \"" + detokenize(p2b, v1) + "\"");
        System.out.println("  -> leading unknown 'x' eats everything; valid \"a\",\"b\" lost -> single UNK.");
        ok &= eq(p2b, -1);

        System.out.println("\n=== PART 3a: longest-match + per-char UNK keeps valid tokens ===");
        List<Integer> p3 = tokenizeLongest("acdebe", v1);
        System.out.println("  tokenizeLongest(\"acdebe\") = " + p3 + "  detok = \"" + detokenize(p3, v1) + "\"");
        System.out.println("  -> [a, cd, UNK(e), b, UNK(e)] : 'b' preserved, each unknown char is its own UNK.");
        ok &= eq(p3, 1, 3, -1, 2, -1);

        // prompt's app/apple vocab — verifies true longest match
        Map<String, Integer> v2 = orderedVocab("app", 1, "apple", 2, UNK_KEY, -1);
        ok &= check("tokenizeLongest(\"apple\")", tokenizeLongest("apple", v2), v2, 2);
        ok &= check("tokenizeLongest(\"bbb\")", tokenizeLongest("bbb", v2), v2, -1, -1, -1);
        ok &= check("tokenizeLongest(\"appbbbapp\")", tokenizeLongest("appbbbapp", v2), v2, 1, -1, -1, -1, 1);

        System.out.println("\n=== PART 3b: collapse consecutive UNKs ===");
        List<Integer> p3b = tokenizeLongestDedup("appbbbapp", v2);
        System.out.println("  tokenizeLongestDedup(\"appbbbapp\") = " + p3b);
        System.out.println("  -> [1,-1,-1,-1,1] folds to [1,-1,1].");
        ok &= eq(p3b, 1, -1, 1);

        System.out.println("\n=== PART 3c: trie acceleration (same output as 3a, O(n·L)) ===");
        Trie trie = new Trie(v2);
        List<Integer> p3c = tokenizeTrie("appbbbapp", v2, trie);
        System.out.println("  tokenizeTrie(\"appbbbapp\") = " + p3c + "  (matches 3a: "
                + p3c.equals(tokenizeLongest("appbbbapp", v2)) + ")");
        ok &= p3c.equals(tokenizeLongest("appbbbapp", v2));

        System.out.println("\n=== PART 3d: DP optimal beats greedy longest-match ===");
        Map<String, Integer> v3 = orderedVocab("aaa", 1, "aa", 2, "a", 3, "ab", 4, UNK_KEY, -1);
        List<Integer> greedy = tokenizeLongest("aab", v3);
        List<Integer> optimal = tokenizeOptimal("aab", v3);
        System.out.println("  vocab={aaa:1,aa:2,a:3,ab:4}, text=\"aab\"");
        System.out.println("  longest-match = " + greedy + "  (\"aa\"+UNK : 2 tokens, 1 UNK)");
        System.out.println("  DP optimal    = " + optimal + "  (\"a\"+\"ab\" : 2 tokens, 0 UNK)");
        ok &= eq(greedy, 2, -1) && eq(optimal, 3, 4);   // optimal avoids the UNK entirely

        System.out.println("\n=== edge cases ===");
        ok &= check("empty text", tokenizeLongest("", v2), v2);                  // -> []
        Map<String, Integer> onlyUnk = orderedVocab(UNK_KEY, -1);
        ok &= check("vocab is only UNK, text \"xy\"", tokenizeLongest("xy", onlyUnk), onlyUnk, -1, -1);
        // round trip is lossless when every char is covered by real tokens...
        String rtApple = detokenize(tokenizeLongest("apple", v2), v2);
        boolean rtOk = rtApple.equals("apple");
        System.out.printf("  [%s] round-trip \"apple\"              = \"%s\"%n", rtOk ? "PASS" : "FAIL", rtApple);
        ok &= rtOk;
        // ...but NOT when unknown chars appear: 'e' -> "UNK" is irreversible (the
        // documented round-trip limitation; literal "UNK" in text is indistinguishable).
        String rtLossy = detokenize(tokenizeLongest("acdebe", v1), v1);
        System.out.println("  (lossy round-trip) detok(tokenize(\"acdebe\")) = \"" + rtLossy
                + "\"  -> unknown 'e' became literal \"UNK\", not recoverable.");

        System.out.println(ok ? "\nALL TESTS PASSED ✅" : "\nTEST FAILED ❌");
        if (!ok) System.exit(1);
    }

    // -------- tiny test helpers --------

    /** Build an insertion-ordered vocab from (key, id) pairs. */
    private static Map<String, Integer> orderedVocab(Object... kv) {
        Map<String, Integer> m = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) m.put((String) kv[i], (Integer) kv[i + 1]);
        return m;
    }

    private static boolean eq(List<Integer> actual, int... expected) {
        if (actual.size() != expected.length) return false;
        for (int i = 0; i < expected.length; i++) if (actual.get(i) != expected[i]) return false;
        return true;
    }

    private static boolean check(String name, List<Integer> actual, Map<String, Integer> vocab, int... expected) {
        boolean pass = eq(actual, expected);
        System.out.printf("  [%s] %-34s = %s%n", pass ? "PASS" : "FAIL", name, actual);
        return pass;
    }
}

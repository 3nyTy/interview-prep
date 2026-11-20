import java.util.Random;

/**
 * Reddit — Staff SWE coding round
 * Problem: Shortest Palindrome (LeetCode 214)
 *
 * Given a string s, add characters ONLY to the FRONT to make it a palindrome.
 * Return the shortest palindrome buildable this way.
 *   "aacecaaa" -> "aaacecaaa"
 *   "abcd"     -> "dcbabcd"
 * Constraints: 0 <= |s| <= 5*10^4, lowercase letters.
 *
 * KEY INSIGHT (shared by all parts):
 *   Only the FRONT may grow, so the tail of s stays put. The longest chunk of s
 *   that is ALREADY a palindrome and sits at the START — the longest palindromic
 *   PREFIX P — can be kept as the palindrome's center. Everything after it,
 *   suffix R (s = P + R), must be mirrored onto the front:
 *
 *        reverse(R) + P + R
 *
 *   is a palindrome (R's mirror on the left, P is its own mirror in the middle),
 *   and it is SHORTEST because we added exactly |R| = |s| - |P| chars, and
 *   maximizing |P| minimizes that. So the whole problem reduces to:
 *   "find the length L of the longest palindromic prefix of s" — then the answer
 *   is reverse(s[L..]) + s.  Everything below is just different ways to find L,
 *   plus one generalization.
 *
 * All parts are O(n) time. Part 1 = KMP (O(n) space), Part 2 = rolling hash
 * (O(1) extra space), Part 3 = front-OR-back generalization reusing Part 1.
 */
public class ShortestPalindrome {

    // =====================================================================
    // PART 1 — Core solution via the KMP failure function (the asked problem).
    //
    // Finding L naively (test every prefix for palindrome-ness) is O(n^2) and
    // TLEs at n = 5*10^4. The linear trick: build
    //        combined = s + '#' + reverse(s)
    // and compute its KMP failure function f (f[i] = length of the longest
    // proper prefix of combined[0..i] that is also a suffix). The value at the
    // LAST index is the longest prefix of s that equals a suffix of reverse(s)
    // — and "prefix of s == suffix of reverse(s)" means precisely "that prefix
    // of s is a palindrome". So f[last] == L, the longest palindromic prefix.
    //
    // The '#' separator (any char outside [a-z]) blocks a match from spanning
    // the seam, guaranteeing L <= n so s.substring(L) is always valid.
    // =====================================================================
    public static String shortestPalindrome(String s) {
        int n = s.length();
        if (n <= 1) return s;                       // "" and single char are already palindromes

        int L = longestPalindromicPrefixLenKMP(s);  // longest palindromic prefix length
        // Mirror the leftover suffix s[L..] onto the front.
        String toPrepend = new StringBuilder(s.substring(L)).reverse().toString();
        return toPrepend + s;
    }

    /** Longest palindromic PREFIX length of s, in O(n) via KMP over s + '#' + reverse(s). */
    private static int longestPalindromicPrefixLenKMP(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        String combined = s + '#' + rev;            // '#' not in [a-z] -> caps result at n
        int m = combined.length();

        int[] f = new int[m];                        // KMP failure (prefix-function) array
        for (int i = 1; i < m; i++) {
            int j = f[i - 1];                        // length matched so far
            // Fall back through the failure links until a match or the start.
            while (j > 0 && combined.charAt(i) != combined.charAt(j)) j = f[j - 1];
            if (combined.charAt(i) == combined.charAt(j)) j++;
            f[i] = j;
        }
        return f[m - 1];                             // == longest palindromic prefix length
    }

    // =====================================================================
    // PART 2 — Same problem, ALTERNATIVE technique: rolling (polynomial) hash.
    // Explicitly the "or rolling hash" path the prompt hints at, and a common
    // "can you do it another way / can you cut the space?" follow-up.
    //
    // CHANGES FROM PART 1:
    //   * The WRAPPER is identical (reverse(s[L..]) + s). Only how we find L
    //     changes: instead of a KMP failure ARRAY (O(n) space), we scan once
    //     keeping two rolling hashes and use O(1) extra space.
    //   * As i advances, maintain:
    //       forward hash  FH of s[0..i] read left->right, and
    //       backward hash BH of s[0..i] read right->left.
    //     s[0..i] is a palindrome  <=>  FH == BH. Track the largest such i+1.
    //     Recurrences:  FH = FH*B + c ;  BH = BH + c*B^i  (B^i tracked in `pow`).
    //   * Hashing can theoretically collide, so we use DOUBLE hashing (two
    //     independent prime moduli); collision odds are ~n^2/M ~ negligible for
    //     n<=5*10^4. KMP (Part 1) has NO collision risk, so it's the safer
    //     default to lead with; this is the "here's the trade-off" variant.
    // =====================================================================
    public static String shortestPalindromeHash(String s) {
        int n = s.length();
        if (n <= 1) return s;

        int L = longestPalindromicPrefixLenHash(s); // same L, computed by hashing
        String toPrepend = new StringBuilder(s.substring(L)).reverse().toString();
        return toPrepend + s;                        // identical wrapper to Part 1
    }

    /** Longest palindromic PREFIX length via O(n) time, O(1) extra space double hashing. */
    private static int longestPalindromicPrefixLenHash(String s) {
        final long B1 = 131, M1 = 1_000_000_007L;    // two independent (base, modulus) pairs
        final long B2 = 137, M2 = 998_244_353L;
        long fh1 = 0, bh1 = 0, fh2 = 0, bh2 = 0;     // forward/backward hashes (2 mods)
        long pow1 = 1, pow2 = 1;                     // B^i for the backward hash's new term
        int L = 0;

        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a' + 1;           // +1 so 'a' != 0 (avoids leading-zero clashes)
            fh1 = (fh1 * B1 + c) % M1;               // append c as least-significant digit
            fh2 = (fh2 * B2 + c) % M2;
            bh1 = (bh1 + c * pow1) % M1;             // prepend c as most-significant digit
            bh2 = (bh2 + c * pow2) % M2;
            if (fh1 == bh1 && fh2 == bh2) L = i + 1; // both agree -> s[0..i] is a palindrome
            pow1 = (pow1 * B1) % M1;
            pow2 = (pow2 * B2) % M2;
        }
        return L;
    }

    // =====================================================================
    // PART 3 — Generalization: allow adding to the FRONT *or* the BACK, and
    // return the shortest palindrome overall. Classic "what if you could also
    // append?" follow-up.
    //
    // CHANGES FROM PART 1:
    //   * Front-add cost is driven by the longest palindromic PREFIX (Part 1).
    //     Back-add is the mirror image: it is driven by the longest palindromic
    //     SUFFIX, and longest-palindromic-suffix(s) == longest-palindromic-
    //     prefix(reverse(s)). So we call the SAME KMP primitive TWICE (on s and
    //     on reverse(s)) — no new algorithm needed.
    //   * Added chars = n - L. Pick the cheaper side; tie -> front (keeps the
    //     original problem's convention). Front answer = reverse(s[Lpre..]) + s;
    //     back answer = s + reverse(s[..n-Lsuf]).
    // =====================================================================
    public static String shortestPalindromeFrontOrBack(String s) {
        int n = s.length();
        if (n <= 1) return s;

        int lPre = longestPalindromicPrefixLenKMP(s);                 // for front-add (reused primitive)
        String rev = new StringBuilder(s).reverse().toString();
        int lSuf = longestPalindromicPrefixLenKMP(rev);               // == longest palindromic SUFFIX of s

        int addFront = n - lPre;                                      // chars to prepend
        int addBack  = n - lSuf;                                      // chars to append

        if (addFront <= addBack) {                                    // tie -> prefer front
            return new StringBuilder(s.substring(lPre)).reverse().append(s).toString();
        } else {
            return new StringBuilder(s)
                    .append(new StringBuilder(s.substring(0, n - lSuf)).reverse())
                    .toString();
        }
    }

    // =====================================================================
    // Test harness: given examples + brute-force cross-check + randomized fuzz.
    // Brute force (O(n^2)) is only for VALIDATION, never for the real answer.
    // =====================================================================
    private static boolean isPalindrome(String t) {
        int i = 0, j = t.length() - 1;
        while (i < j) if (t.charAt(i++) != t.charAt(j--)) return false;
        return true;
    }

    /** Naive longest palindromic prefix length — the reference for correctness. */
    private static int bruteLPP(String s) {
        int best = 0;
        for (int L = 1; L <= s.length(); L++) if (isPalindrome(s.substring(0, L))) best = L;
        return best;
    }

    private static String bruteFront(String s) {
        int L = bruteLPP(s);
        return new StringBuilder(s.substring(L)).reverse().toString() + s;
    }

    private static boolean check(String name, boolean cond) {
        System.out.printf("[%s] %s%n", cond ? "PASS" : "FAIL", name);
        return cond;
    }

    public static void main(String[] args) {
        boolean ok = true;

        // ---- Given examples ----
        ok &= check("ex1 aacecaaa -> aaacecaaa", shortestPalindrome("aacecaaa").equals("aaacecaaa"));
        ok &= check("ex2 abcd -> dcbabcd",       shortestPalindrome("abcd").equals("dcbabcd"));

        // ---- Edge cases ----
        ok &= check("empty",        shortestPalindrome("").equals(""));
        ok &= check("single",       shortestPalindrome("a").equals("a"));
        ok &= check("all same",     shortestPalindrome("aaaa").equals("aaaa"));
        ok &= check("already pal",  shortestPalindrome("racecar").equals("racecar"));
        ok &= check("no shared",    shortestPalindrome("abc").equals("cbabc"));

        // ---- Part 2 must equal Part 1 on the examples ----
        ok &= check("hash==kmp ex1", shortestPalindromeHash("aacecaaa").equals("aaacecaaa"));
        ok &= check("hash==kmp ex2", shortestPalindromeHash("abcd").equals("dcbabcd"));

        // ---- Part 3: front-or-back ----
        // "abac": longest pal prefix = "aba" (add 'c' at front? no) — front adds 1 ("cabac"),
        // longest pal suffix = "a"; back adds 3. So front wins.
        ok &= check("p3 abac front", shortestPalindromeFrontOrBack("abac").equals("cabac"));
        // "aacecaaa": front adds 1 -> "aaacecaaa" (front strictly better than back).
        ok &= check("p3 aacecaaa",   shortestPalindromeFrontOrBack("aacecaaa").equals("aaacecaaa"));

        // ---- Randomized fuzz: KMP == rolling hash == brute force, and results valid ----
        Random rnd = new Random(42);
        int trials = 20_000;
        for (int t = 0; t < trials; t++) {
            int len = rnd.nextInt(13);                       // 0..12
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) sb.append((char) ('a' + rnd.nextInt(3))); // small alphabet -> more palindromes
            String s = sb.toString();

            String kmp = shortestPalindrome(s);
            String hash = shortestPalindromeHash(s);
            String brute = bruteFront(s);

            if (!kmp.equals(brute) || !hash.equals(brute)) {
                ok = false;
                System.out.printf("[FAIL] fuzz s=%s kmp=%s hash=%s brute=%s%n", s, kmp, hash, brute);
                break;
            }
            // Front-only result must be a palindrome that ends with s.
            if (!s.isEmpty() && (!isPalindrome(kmp) || !kmp.endsWith(s))) {
                ok = false;
                System.out.printf("[FAIL] invariant s=%s kmp=%s%n", s, kmp);
                break;
            }
            // Part 3 result must be a palindrome no longer than the front-only result.
            String fob = shortestPalindromeFrontOrBack(s);
            if (!s.isEmpty() && (!isPalindrome(fob) || fob.length() > kmp.length()
                                 || !(fob.endsWith(s) || fob.startsWith(s)))) {
                ok = false;
                System.out.printf("[FAIL] p3 s=%s fob=%s (front=%s)%n", s, fob, kmp);
                break;
            }
        }
        ok &= check("randomized fuzz (20k) kmp==hash==brute + invariants", ok);

        System.out.println(ok ? "\nALL TESTS PASSED" : "\nSOME TESTS FAILED");
    }
}

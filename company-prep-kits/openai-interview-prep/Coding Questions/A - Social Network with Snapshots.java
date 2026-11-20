import java.util.*;

/**
 * Social Network with Snapshots — 3-part incremental coding question (Java 21).
 *
 * ----------------------------------------------------------------------------
 * WHAT EACH PART DOES
 * ----------------------------------------------------------------------------
 * Part 1: A SocialNetwork managing users, follow relationships, and immutable
 *         point-in-time snapshots. Snapshot.isFollowing answers whether a follow
 *         edge existed at snapshot time. The crucial design point is that
 *         createSnapshot DEEP-COPIES each user's followee set, so later mutations
 *         to the live network can never corrupt a previously taken snapshot.
 *
 * Part 2: Adds Snapshot.getFollowing (who a user follows) and
 *         Snapshot.getFollowers (who follows a user). To make getFollowers O(F)
 *         instead of an O(E) scan, the snapshot builds a REVERSE INDEX (followee
 *         -> set of followers) once, at construction time.
 *
 * Part 3: Adds Snapshot.recommend(user, k) = a friend-of-friend recommender.
 *         For each user U follows, look at who THEY follow; count how many of U's
 *         followees follow each candidate (2-hop mutual-connection count); exclude
 *         U itself and anyone U already follows; return the top-K candidates by
 *         count descending (ties broken arbitrarily).
 *
 * ----------------------------------------------------------------------------
 * COMPLEXITY SUMMARY  (U = users, E = edges, F = followees/followers of the
 *                      queried user, G = avg followees per followed user,
 *                      C = unique candidate count, K = top-K parameter)
 * ----------------------------------------------------------------------------
 *   addUser          time O(1)            space O(1)
 *   follow           time O(1)            space O(1)
 *   createSnapshot   time O(U + E)        space O(U + E)
 *   isFollowing      time O(1)            space O(1)
 *   getFollowing     time O(F)            space O(F)
 *   getFollowers     time O(F)            space O(F)
 *   recommend        time O(F*G + C log C) [Counter/most_common is C log K via
 *                    heapq.nlargest; we sort all entries -> C log C]  space O(C)
 *
 * ----------------------------------------------------------------------------
 * STRUCTURE / SELF-CONTAINMENT CONTRACT
 * ----------------------------------------------------------------------------
 * The top-level class SocialNetworkWithSnapshots is package-private. It contains
 * three INDEPENDENT static nested classes Part1, Part2, Part3. Each Part fully
 * RE-DECLARES its own SocialNetwork + Snapshot + helpers and references NO other
 * part — the code is intentionally DUPLICATED across parts, not shared. As a
 * result, you can copy any single PartN class into its own file (rename it to a
 * top-level `class PartN`), keep the `import java.util.*;` line at the top, and
 * it will compile and run unchanged.
 *
 * Run it:
 *   javac "A - Social Network with Snapshots.java"
 *   java SocialNetworkWithSnapshots            # runs all three parts
 *   java SocialNetworkWithSnapshots part1      # runs only Part 1 (also part2/part3)
 *
 * ASSERTIONS: this file is meant to run with a plain `java` (no -ea), so the bare
 * `assert` keyword would be silently skipped. Instead each Part defines its own
 * check(cond, msg) helper that throws AssertionError on failure. Every example
 * from the problem statement is embedded as a check(...) call.
 *
 * JAVA TRANSLATION NOTES (from the reference Python):
 *   Python ValueError                 -> IllegalArgumentException
 *   Python set / dict[str,set[str]]   -> Set<String> / Map<String,Set<String>>
 *   `x in s.get(k, set())`            -> s.getOrDefault(k, Set.of()).contains(x)
 *                                        (Set.of() is an immutable empty set we only read)
 *   list(some_set)                    -> new ArrayList<>(someSet)
 *   Counter[c] += 1                   -> counts.merge(c, 1, Integer::sum)
 *   Counter.most_common(k)            -> sort entries by count desc, take first k
 */
class SocialNetworkWithSnapshots {

    /**
     * Dispatcher. With no args, runs all three parts and prints a final banner.
     * With an arg, runs only the requested part via a Java 21 arrow switch.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Part1.main(args);
            Part2.main(args);
            Part3.main(args);
            System.out.println("ALL PARTS PASSED");
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(args);
            case "part2" -> Part2.main(args);
            case "part3" -> Part3.main(args);
            default -> {
                System.err.println("Usage: java SocialNetworkWithSnapshots [part1|part2|part3]");
                System.err.println("No (or unknown) part given -> running all parts.");
                Part1.main(args);
                Part2.main(args);
                Part3.main(args);
                System.out.println("ALL PARTS PASSED");
            }
        }
    }

    // ========================================================================
    // PART 1 — Users, Follows & immutable Snapshots (isFollowing)
    // Self-contained: copy this class to its own file as `class Part1` (keep the
    // `import java.util.*;` line) and it compiles & runs unchanged.
    // ========================================================================
    static final class Part1 {

        /** Tiny assertion helper (we run without -ea, so bare `assert` won't work). */
        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }

        /** The live, mutable social network. */
        static final class SocialNetwork {
            // Set of known user ids.
            private final Set<String> users = new HashSet<>();
            // user -> set of users they follow. Part 1 only needs membership tests,
            // so a plain HashSet for the inner sets is sufficient.
            private final Map<String, Set<String>> follows = new HashMap<>();

            /** Add a user; O(1). Throws if the user already exists. */
            void addUser(String userId) {
                if (users.contains(userId)) {
                    throw new IllegalArgumentException("User '" + userId + "' already exists.");
                }
                users.add(userId);
                follows.put(userId, new HashSet<>());
            }

            /**
             * Make follower follow followee; O(1).
             * Throws if either user is unknown. Self-follow and duplicates are no-ops.
             */
            void follow(String follower, String followee) {
                if (!users.contains(follower) || !users.contains(followee)) {
                    throw new IllegalArgumentException("Both users must exist.");
                }
                if (follower.equals(followee)) return;     // cannot follow yourself
                follows.get(follower).add(followee);        // duplicate add is a no-op
            }

            /** Immutable snapshot of the current follow graph; O(U + E). */
            Snapshot createSnapshot() {
                return new Snapshot(follows);
            }
        }

        /** An immutable point-in-time view of the follow graph. */
        static final class Snapshot {
            private final Map<String, Set<String>> follows = new HashMap<>();

            /**
             * Deep-copy the per-user followee sets so that future mutations to the
             * live network cannot corrupt this snapshot.
             */
            Snapshot(Map<String, Set<String>> liveFollows) {
                for (Map.Entry<String, Set<String>> e : liveFollows.entrySet()) {
                    follows.put(e.getKey(), new HashSet<>(e.getValue()));
                }
            }

            /** True iff follower -> followee exists in this snapshot; O(1). */
            boolean isFollowing(String follower, String followee) {
                return follows.getOrDefault(follower, Set.of()).contains(followee);
            }
        }

        public static void main(String[] args) {
            SocialNetwork network = new SocialNetwork();
            network.addUser("A");
            network.addUser("B");
            network.addUser("C");

            network.follow("A", "B");
            network.follow("B", "C");

            Snapshot snapshot1 = network.createSnapshot();

            check(snapshot1.isFollowing("A", "B"), "A should follow B in snapshot1");
            check(snapshot1.isFollowing("B", "C"), "B should follow C in snapshot1");
            check(!snapshot1.isFollowing("A", "C"), "A does not follow C");
            check(!snapshot1.isFollowing("B", "A"), "B does not follow A (not mutual)");

            // New follows after snapshot1 must NOT appear in snapshot1.
            network.follow("A", "C");
            check(!snapshot1.isFollowing("A", "C"), "snapshot1 must be immutable");

            Snapshot snapshot2 = network.createSnapshot();
            check(snapshot2.isFollowing("A", "C"), "snapshot2 has the new follow");

            // --- Robustness checks ---
            // Duplicate addUser throws.
            boolean threwDup = false;
            try { network.addUser("A"); } catch (IllegalArgumentException ex) { threwDup = true; }
            check(threwDup, "addUser on existing user must throw");

            // follow with a missing user throws.
            boolean threwMissing = false;
            try { network.follow("A", "Z"); } catch (IllegalArgumentException ex) { threwMissing = true; }
            check(threwMissing, "follow with unknown user must throw");

            // Self-follow is a no-op.
            network.follow("A", "A");
            Snapshot selfSnap = network.createSnapshot();
            check(!selfSnap.isFollowing("A", "A"), "self-follow must be a no-op");

            System.out.println("Part1: all checks passed");
        }
    }

    // ========================================================================
    // PART 2 — Followers & Following lists (getFollowing / getFollowers)
    //
    // CHANGES FROM PART 1:
    //   * Inner follow sets are now LinkedHashSet so list outputs follow insertion
    //     order (membership tests stay O(1)). Order is a nicety only — the spec's
    //     return type is a list with no order guarantee, so verification compares
    //     as SETS.
    //   * Snapshot now also builds a REVERSE INDEX (followee -> followers) at
    //     construction time so getFollowers is O(F) rather than an O(E) scan.
    //   * Adds getFollowing / getFollowers.
    //
    // Self-contained: copy this class to its own file as `class Part2` (keep the
    // `import java.util.*;` line) and it compiles & runs unchanged.
    // ========================================================================
    static final class Part2 {

        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }

        /** Order-insensitive comparison: the list contents equal the expected set. */
        private static boolean sameSet(List<String> actual, Set<String> expected) {
            return new HashSet<>(actual).equals(expected);
        }

        static final class SocialNetwork {
            private final Set<String> users = new HashSet<>();
            // LinkedHashSet preserves insertion order for nicer list outputs.
            private final Map<String, Set<String>> follows = new HashMap<>();

            void addUser(String userId) {
                if (users.contains(userId)) {
                    throw new IllegalArgumentException("User '" + userId + "' already exists.");
                }
                users.add(userId);
                follows.put(userId, new LinkedHashSet<>());
            }

            void follow(String follower, String followee) {
                if (!users.contains(follower) || !users.contains(followee)) {
                    throw new IllegalArgumentException("Both users must exist.");
                }
                if (follower.equals(followee)) return;
                follows.get(follower).add(followee);
            }

            Snapshot createSnapshot() {
                return new Snapshot(follows);
            }
        }

        static final class Snapshot {
            // Forward index: user -> followees.
            private final Map<String, Set<String>> follows = new HashMap<>();
            // Reverse index: user -> followers (built once, at construction).
            private final Map<String, Set<String>> followers = new HashMap<>();

            Snapshot(Map<String, Set<String>> liveFollows) {
                // Deep-copy forward edges (LinkedHashSet preserves order).
                for (Map.Entry<String, Set<String>> e : liveFollows.entrySet()) {
                    follows.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
                }
                // Build the reverse index from the copied forward edges.
                for (Map.Entry<String, Set<String>> e : follows.entrySet()) {
                    String user = e.getKey();
                    for (String followee : e.getValue()) {
                        followers.computeIfAbsent(followee, x -> new LinkedHashSet<>()).add(user);
                    }
                }
            }

            boolean isFollowing(String follower, String followee) {
                return follows.getOrDefault(follower, Set.of()).contains(followee);
            }

            /** All users user_id follows; [] if none. O(F). */
            List<String> getFollowing(String userId) {
                return new ArrayList<>(follows.getOrDefault(userId, Set.of()));
            }

            /** All users that follow user_id; [] if none. O(F). */
            List<String> getFollowers(String userId) {
                return new ArrayList<>(followers.getOrDefault(userId, Set.of()));
            }
        }

        public static void main(String[] args) {
            SocialNetwork network = new SocialNetwork();
            network.addUser("A");
            network.addUser("B");
            network.addUser("C");
            network.addUser("D");

            network.follow("A", "B");
            network.follow("A", "C");
            network.follow("B", "C");
            network.follow("D", "A");

            Snapshot snapshot = network.createSnapshot();

            // Primary checks: set-equality (list order is not contractually guaranteed).
            check(sameSet(snapshot.getFollowing("A"), Set.of("B", "C")), "getFollowing(A) == {B,C}");
            check(sameSet(snapshot.getFollowing("B"), Set.of("C")),      "getFollowing(B) == {C}");
            check(sameSet(snapshot.getFollowing("C"), Set.of()),         "getFollowing(C) == {}");

            check(sameSet(snapshot.getFollowers("C"), Set.of("A", "B")), "getFollowers(C) == {A,B}");
            check(sameSet(snapshot.getFollowers("A"), Set.of("D")),      "getFollowers(A) == {D}");
            check(sameSet(snapshot.getFollowers("D"), Set.of()),         "getFollowers(D) == {}");

            // Bonus: because we use LinkedHashSet, insertion order is in fact preserved,
            // so these exact-order checks also hold (set-equality above is the contract).
            check(snapshot.getFollowing("A").equals(List.of("B", "C")), "getFollowing(A) insertion order == [B,C]");
            check(snapshot.getFollowers("C").equals(List.of("A", "B")), "getFollowers(C) insertion order == [A,B]");

            System.out.println("Part2: all checks passed");
        }
    }

    // ========================================================================
    // PART 3 — Follow recommendations (recommend)
    //
    // CHANGES FROM PART 2:
    //   * Adds Snapshot.recommend(userId, k): friend-of-friend recommender.
    //     For each user U follows, count how many of U's followees follow each
    //     candidate; exclude U and anyone U already follows; return top-K by
    //     count descending. Ties are broken arbitrarily.
    //   * Everything from Part 2 (forward + reverse index, getFollowing/
    //     getFollowers) is re-declared here unchanged.
    //
    // Self-contained: copy this class to its own file as `class Part3` (keep the
    // `import java.util.*;` line) and it compiles & runs unchanged.
    // ========================================================================
    static final class Part3 {

        private static void check(boolean cond, String msg) {
            if (!cond) throw new AssertionError(msg);
        }

        static final class SocialNetwork {
            private final Set<String> users = new HashSet<>();
            private final Map<String, Set<String>> follows = new HashMap<>();

            void addUser(String userId) {
                if (users.contains(userId)) {
                    throw new IllegalArgumentException("User '" + userId + "' already exists.");
                }
                users.add(userId);
                follows.put(userId, new LinkedHashSet<>());
            }

            void follow(String follower, String followee) {
                if (!users.contains(follower) || !users.contains(followee)) {
                    throw new IllegalArgumentException("Both users must exist.");
                }
                if (follower.equals(followee)) return;
                follows.get(follower).add(followee);
            }

            Snapshot createSnapshot() {
                return new Snapshot(follows);
            }
        }

        static final class Snapshot {
            private final Map<String, Set<String>> follows = new HashMap<>();
            private final Map<String, Set<String>> followers = new HashMap<>();

            Snapshot(Map<String, Set<String>> liveFollows) {
                for (Map.Entry<String, Set<String>> e : liveFollows.entrySet()) {
                    follows.put(e.getKey(), new LinkedHashSet<>(e.getValue()));
                }
                for (Map.Entry<String, Set<String>> e : follows.entrySet()) {
                    String user = e.getKey();
                    for (String followee : e.getValue()) {
                        followers.computeIfAbsent(followee, x -> new LinkedHashSet<>()).add(user);
                    }
                }
            }

            boolean isFollowing(String follower, String followee) {
                return follows.getOrDefault(follower, Set.of()).contains(followee);
            }

            List<String> getFollowing(String userId) {
                return new ArrayList<>(follows.getOrDefault(userId, Set.of()));
            }

            List<String> getFollowers(String userId) {
                return new ArrayList<>(followers.getOrDefault(userId, Set.of()));
            }

            /**
             * Recommend up to k users for userId to follow, ranked by how many of
             * userId's followees also follow the candidate (a 2-hop count).
             *
             * Steps: (1) take userId's followees; (2) for each, look at who they
             * follow; (3) count occurrences per candidate; (4) skip userId itself
             * and anyone userId already follows; (5) return the top-k by count desc
             * (ties arbitrary).
             *
             * Handles k <= 0 (empty), k larger than the candidate set (returns all),
             * and unknown / no-followee users (empty).
             *
             * Time O(F*G + C log C), space O(C).  (The reference's most_common(k) is
             * C log K via heapq.nlargest; we sort all candidates -> C log C. A size-k
             * min-heap would give the C log K bound if K << C.)
             */
            List<String> recommend(String userId, int k) {
                if (k <= 0) return new ArrayList<>();

                Set<String> following = follows.getOrDefault(userId, Set.of());
                Map<String, Integer> counts = new HashMap<>();

                for (String followee : following) {
                    for (String candidate : follows.getOrDefault(followee, Set.of())) {
                        if (candidate.equals(userId)) continue;       // exclude self
                        if (following.contains(candidate)) continue;  // exclude already-followed
                        counts.merge(candidate, 1, Integer::sum);
                    }
                }

                // Sort candidates by count descending (ties: arbitrary order).
                List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
                entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

                int limit = Math.min(k, entries.size());
                List<String> result = new ArrayList<>(limit);
                for (int i = 0; i < limit; i++) {
                    result.add(entries.get(i).getKey());
                }
                return result;
            }
        }

        public static void main(String[] args) {
            SocialNetwork network = new SocialNetwork();
            for (String user : new String[]{"A", "B", "C", "D", "E", "F"}) {
                network.addUser(user);
            }

            network.follow("A", "B");
            network.follow("A", "C");
            network.follow("B", "D");
            network.follow("B", "E");
            network.follow("C", "D");
            network.follow("C", "F");

            Snapshot snapshot = network.createSnapshot();

            // recommend(A, 2): D has count 2 (must be first); second is E or F (count 1).
            List<String> rec2 = snapshot.recommend("A", 2);
            check(rec2.size() == 2, "recommend(A,2) must return exactly 2");
            check(rec2.get(0).equals("D"), "recommend(A,2) highest must be D");
            check(rec2.get(1).equals("E") || rec2.get(1).equals("F"), "recommend(A,2) second is E or F");
            // Never recommend self or already-followed users.
            check(!rec2.contains("A") && !rec2.contains("B") && !rec2.contains("C"),
                  "recommend(A,2) must exclude A, B, C");

            // recommend(A, 10): all 3 candidates, D first; tail {E,F} in any order.
            List<String> rec10 = snapshot.recommend("A", 10);
            check(rec10.size() == 3, "recommend(A,10) must return all 3 candidates");
            check(rec10.get(0).equals("D"), "recommend(A,10) highest must be D");
            check(new HashSet<>(rec10).equals(Set.of("D", "E", "F")), "recommend(A,10) set == {D,E,F}");
            check(!rec10.contains("A") && !rec10.contains("B") && !rec10.contains("C"),
                  "recommend(A,10) must exclude A, B, C");

            // Edge cases.
            check(snapshot.recommend("A", 0).isEmpty(), "k=0 returns empty");
            check(snapshot.recommend("D", 5).isEmpty(), "user with no followees -> empty");
            check(snapshot.recommend("Z", 5).isEmpty(), "unknown user -> empty");

            System.out.println("Part3: all checks passed");
        }
    }
}

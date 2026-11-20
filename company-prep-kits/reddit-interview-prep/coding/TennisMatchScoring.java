import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tennis Match Scoring — multi-part object-oriented design.
 * (Reddit's most common Staff phone-screen coding prompt.)
 *
 * ============================================================================
 * THE KEY OOD SIGNAL — a strict ownership hierarchy, one rule per layer:
 *
 *     Match  owns  Set[]   owns  Game[]   owns  the deuce/advantage state machine
 *
 *   Every layer:
 *     - accepts a point for a player index (0 or 1),
 *     - decides its own completion using ONLY its own rule,
 *     - rolls the child over on completion and bubbles the result up,
 *     - DERIVES its human-readable score on read (never stores the display text).
 *
 *   Resisting the urge to inline set/game state into Match is exactly what the
 *   interviewer is watching for; it is also what lets Part 3 (BO3/BO5) land in
 *   one line instead of a refactor.
 *
 * PLAYER NAMES live exactly once (a shared String[2]); lower layers keep a
 * reference so they can render "advantage <name>" without copying names around.
 *
 * OUTPUT CONVENTION (decided up front to avoid the classic flip-flop the
 * interviewer punishes):
 *     - structured getters return raw ints:  getPoints / getGamesWon /
 *       getSetsWon / getWinner  (this is the MODEL)
 *     - render() / liveScore() return formatted strings  (this is the VIEW)
 * ============================================================================
 */
public class TennisMatchScoring {

    // ========================================================================
    // PART 1 — Game: the 0/15/30/40 → deuce → advantage → game state machine.
    //
    // Correctness idea: store ONLY the raw point counts; derive everything
    // (deuce / advantage / winner) on read. There is no "displayed score" field
    // to keep in sync, so the #1 bug magnet simply cannot occur.
    // ========================================================================
    static class Game {
        private static final String[] LABELS = {"0", "15", "30", "40"};

        protected final String[] names;          // shared reference {p0, p1}
        protected final int[] points = new int[2];
        protected int winner = -1;                // -1 = in progress; else 0 / 1

        Game(String[] names) { this.names = names; }

        /** Points needed (with a lead of >= 2) to win. Normal game = 4. */
        protected int winThreshold() { return 4; }

        /** Record a point for player p (0 or 1). Throws if the game is decided. */
        void pointWonBy(int p) {
            if (isOver())
                throw new IllegalStateException("game already won by " + names[winner]);
            points[p]++;
            // ONE shared win rule (reused verbatim by the tiebreak): reach the
            // threshold AND lead by >= 2. This single line encodes deuce + adv:
            //   40-40  -> neither has lead 2, keep playing (deuce)
            //   adv    -> leader has +1, not +2 yet, keep playing
            //   game   -> leader reaches +2
            if (points[p] >= winThreshold() && points[p] - points[1 - p] >= 2)
                winner = p;
        }

        boolean isOver()    { return winner != -1; }
        int     getWinner() { return winner; }
        int[]   getPoints() { return new int[]{points[0], points[1]}; }

        /** Displayed score, derived from the raw counts. */
        String render() {
            if (isOver()) return names[winner] + " won the game";
            // Deuce / advantage exist ONLY when both players have >= 3 points.
            if (points[0] >= 3 && points[1] >= 3) {
                if (points[0] == points[1]) return "deuce";               // 40-40, 5-5, ...
                return "advantage " + names[points[0] > points[1] ? 0 : 1];
            }
            // Otherwise both counts are in 0..3 (a value >= 4 here is impossible:
            // it would mean a >= 2 lead, i.e. the game is already over), so the
            // label map is always safe to index.
            return LABELS[points[0]] + "-" + LABELS[points[1]];
        }
    }

    // ========================================================================
    // PART 2 — TiebreakGame + Set.
    //
    // CHANGE vs Part 1:
    //   (a) TiebreakGame is a Game SUBTYPE that overrides ONLY the win
    //       threshold (4 -> 7) and rendering (raw numbers, not 15/30/40).
    //       The entire point/lead/win state machine is inherited unchanged —
    //       the tiebreak is NOT a conditional bolted onto the normal path.
    //   (b) Set layers game tracking on top: first to 6 games with a >= 2 lead,
    //       else 6-6 -> tiebreak -> 7-6.
    // ========================================================================
    static class TiebreakGame extends Game {
        TiebreakGame(String[] names) { super(names); }

        @Override protected int winThreshold() { return 7; }  // first to 7, win by 2

        @Override String render() {
            if (isOver()) return names[winner] + " won the tiebreak";
            return "tiebreak " + points[0] + "-" + points[1];  // raw 1,2,3,... not 15/30/40
        }
    }

    static class Set {
        protected final String[] names;
        private final List<Game> games = new ArrayList<>();   // the Set OWNS its games
        private final int[] gamesWon = new int[2];
        private Game current;
        private int winner = -1;

        // [Part 5] presentational metadata — players change ends after odd games.
        private int gamesCompletedInSet = 0;
        private boolean endsSwapped = false;

        Set(String[] names) {
            this.names = names;
            current = new Game(names);        // a set always OPENS with a normal game
            games.add(current);
        }

        void pointWonBy(int p) {
            if (isOver())
                throw new IllegalStateException("set already won by " + names[winner]);
            current.pointWonBy(p);
            if (current.isOver()) onGameComplete(current.getWinner());
        }

        private void onGameComplete(int w) {
            gamesWon[w]++;
            gamesCompletedInSet++;
            if (gamesCompletedInSet % 2 == 1) endsSwapped = !endsSwapped;   // [Part 5]

            int a = gamesWon[w], b = gamesWon[1 - w];
            boolean normalWin   = a >= 6 && a - b >= 2;   // 6-0 .. 6-4, or 7-5
            boolean tiebreakWin = a == 7 && b == 6;       // 7-6 can ONLY arise from a tiebreak
            if (normalWin || tiebreakWin) {
                winner = w;                                // set over; do NOT open a new game
            } else {
                current = nextGame();
                games.add(current);
            }
        }

        /** At exactly 6-6 the next game is a tiebreak; otherwise a normal game. */
        private Game nextGame() {
            return (gamesWon[0] == 6 && gamesWon[1] == 6)
                    ? new TiebreakGame(names)
                    : new Game(names);
        }

        boolean isOver()      { return winner != -1; }
        int     getWinner()   { return winner; }
        int[]   getGamesWon() { return new int[]{gamesWon[0], gamesWon[1]}; }
        Game    currentGame() { return current; }
        boolean endsSwapped() { return endsSwapped; }      // [Part 5]

        String render() {
            String g = "games " + gamesWon[0] + "-" + gamesWon[1];
            if (isOver()) return g + " (" + names[winner] + " won the set)";
            return g + ", " + current.render();
        }
    }

    // ========================================================================
    // PART 3 — Match (best-of-N).
    //
    // CHANGE vs Part 2: the ONLY format knob is setsToWin = ceil(N/2), fixed
    // once here (BO3 -> 2, BO5 -> 3). No layer below ever branches on the
    // format — that is the whole point of parameterizing at construction.
    // ========================================================================
    static class Match {
        private final String[] names;
        private final int setsToWin;                        // ceil(N/2)
        private final List<Set> sets = new ArrayList<>();   // the Match OWNS its sets
        private final int[] setsWon = new int[2];
        private Set current;
        private int winner = -1;

        // [Part 5]
        private final int firstServer = 0;                       // who serves game 1
        private final List<String> history = new ArrayList<>();  // score after each point

        Match(String p0, String p1, int bestOf) {
            if (bestOf % 2 == 0) throw new IllegalArgumentException("bestOf must be odd");
            this.names = new String[]{p0, p1};
            this.setsToWin = bestOf / 2 + 1;    // 3 -> 2, 5 -> 3  == ceil(bestOf/2)
            this.current = new Set(names);
            sets.add(current);
        }

        /** Core entry point: record a point for player index p (0 or 1). */
        void pointWonBy(int p) {
            if (isOver())
                throw new IllegalStateException("match already won by " + names[winner]);
            current.pointWonBy(p);
            if (current.isOver()) {
                int sw = current.getWinner();
                setsWon[sw]++;
                if (setsWon[sw] == setsToWin) winner = sw;          // ceil(N/2) sets -> match over
                else { current = new Set(names); sets.add(current); }
            }
            history.add(liveScore());          // [Part 5] snapshot AFTER this point
        }

        /** Convenience overload: accept a player NAME (handles "alice"/"bob" prompts). */
        void pointWonBy(String name) { pointWonBy(resolve(name)); }
        private int resolve(String name) {
            if (name.equals(names[0])) return 0;
            if (name.equals(names[1])) return 1;
            throw new IllegalArgumentException("unknown player: " + name);
        }

        boolean isOver()     { return winner != -1; }
        int     getWinner()  { return winner; }
        int[]   getSetsWon() { return new int[]{setsWon[0], setsWon[1]}; }

        // ---- [Part 5] serving rotation: server changes every game; DERIVED, not stored ----
        int gamesCompletedInMatch() {
            int t = 0;
            for (Set s : sets) { int[] g = s.getGamesWon(); t += g[0] + g[1]; }
            return t;   // gamesWon counts completed games only, across all sets
        }
        int currentServer() { return (firstServer + gamesCompletedInMatch()) % 2; }

        // ---- [Part 5] history query: full score after the k-th point (1-indexed) ----
        String scoreAfter(int k) {
            if (k < 1 || k > history.size())
                throw new IndexOutOfBoundsException("no point #" + k);
            return history.get(k - 1);
        }

        String liveScore() {
            String s = "sets " + setsWon[0] + "-" + setsWon[1];
            if (isOver()) return s + " | " + names[winner] + " wins the match";
            return s + " | " + current.render() + " | server: " + names[currentServer()];
        }

        /** PART 4 — in-class driver: play "AABBA…" and print the score after each point. */
        void runTests(String seq) {
            for (int i = 0; i < seq.length(); i++) {
                if (isOver()) { System.out.println("  (match over; ignoring rest of sequence)"); break; }
                char c = seq.charAt(i);
                pointWonBy(c == 'A' ? 0 : 1);
                System.out.printf("  after point %2d (%c): %s%n", i + 1, c, liveScore());
            }
        }
    }

    // ========================================================================
    // ALTERNATE CANONICAL VARIANT — numeric Game + rendering Match.
    //
    // A commonly-seen shorter shape: split into (1) a numeric point-counting API
    // and (2) a human-readable renderer, replacing the advantage counter with a
    // DEUCE-COLLAPSE normalization: the instant both counts are >= 4 and equal,
    // reset both to 3. So 4-4 -> 3-3 and tied states never persist above deuce;
    // the game can only be decided from a 4-3 / 3-4 -> +1 step. O(1) time & space,
    // no advantage bookkeeping.
    // ========================================================================
    static class NumericGame {
        private final String p1, p2;
        private final int[] c = new int[2];   // raw counts (player1, player2)
        private String result = null;         // winner name once decided, else null

        NumericGame(String p1, String p2) { this.p1 = p1; this.p2 = p2; }

        void addScore(String player) {
            if (result != null)
                throw new IllegalStateException("game already decided: " + result);
            int i = player.equals(p1) ? 0 : player.equals(p2) ? 1 : -1;
            if (i < 0) throw new IllegalArgumentException("unknown player: " + player);
            c[i]++;
            // Deuce-collapse: snap any tied-above-deuce state back to 40-40.
            if (c[0] >= 4 && c[1] >= 4 && c[0] == c[1]) { c[0] = 3; c[1] = 3; }
            // Win requires max >= 4 AND lead >= 2. The max>=4 guard is essential:
            // without it, an early 2-0 would falsely end the game.
            if (Math.max(c[0], c[1]) >= 4 && Math.abs(c[0] - c[1]) >= 2)
                result = c[0] > c[1] ? p1 : p2;
        }

        int[]  getScore()  { return new int[]{c[0], c[1]}; }   // raw tuple (NUMERIC layer)
        String getResult() { return result; }                  // winner name or null
    }

    static class RenderingMatch {
        private static final String[] LABEL = {"love", "15", "30", "40"};
        private final String p1, p2;
        private final NumericGame game;

        RenderingMatch(String p1, String p2) {
            this.p1 = p1; this.p2 = p2; this.game = new NumericGame(p1, p2);
        }

        void   pointWonBy(String player) { game.addScore(player); }             // delegates
        String result() { String w = game.getResult(); return w == null ? null : "winner " + w; }

        String score() {                                        // string (VIEW layer)
            String w = game.getResult();
            if (w != null) return "winner " + w;
            int[] c = game.getScore();
            int a = c[0], b = c[1];
            if (a >= 3 && b >= 3) return a == b ? "deuce" : "advantage " + (a > b ? p1 : p2);
            return LABEL[a] + "-" + LABEL[b];
        }
    }

    // ========================================================================
    // PART 4 — self-checking tests + demonstrations.
    // ========================================================================
    static int testsRun = 0, testsPassed = 0;
    static void check(String label, boolean cond) {
        testsRun++;
        if (cond) testsPassed++;
        System.out.println((cond ? "  PASS  " : "  FAIL  ") + label);
    }

    /** Silent feed for assertions; maps 'A'->0, 'B'->1; stops early if the match ends. */
    static void feed(Match m, String seq) {
        for (int i = 0; i < seq.length() && !m.isOver(); i++)
            m.pointWonBy(seq.charAt(i) == 'A' ? 0 : 1);
    }

    /** Make p0 win `a` games and p1 win `b` games (each a love game), alternating. */
    static void winGames(Set s, int a, int b) {
        while ((a > 0 || b > 0) && !s.isOver()) {
            if (a > 0) { for (int i = 0; i < 4 && !s.isOver(); i++) s.pointWonBy(0); a--; }
            if (b > 0 && !s.isOver()) { for (int i = 0; i < 4 && !s.isOver(); i++) s.pointWonBy(1); b--; }
        }
    }

    static void testPart1() {
        System.out.println("PART 1 — Game (deuce / advantage):");
        String[] n = {"A", "B"};

        // Love game: four straight points.
        Game love = new Game(n);
        love.pointWonBy(0); check("15-0", love.render().equals("15-0"));
        love.pointWonBy(0); check("30-0", love.render().equals("30-0"));
        love.pointWonBy(0); check("40-0", love.render().equals("40-0"));
        love.pointWonBy(0); check("A wins the love game", love.isOver() && love.getWinner() == 0);

        // Deuce cycle: A,B,A,A -> (3,1) ; B,B -> deuce ; adv A ; back to deuce ; adv B ; B wins.
        Game g = new Game(n);
        g.pointWonBy(0); g.pointWonBy(1); g.pointWonBy(0); g.pointWonBy(0);
        check("points (3,1)", Arrays.equals(g.getPoints(), new int[]{3, 1}));
        check("render 40-15", g.render().equals("40-15"));
        g.pointWonBy(1); g.pointWonBy(1);
        check("deuce (40-40)", g.render().equals("deuce"));
        g.pointWonBy(0); check("advantage A", g.render().equals("advantage A"));
        g.pointWonBy(1); check("back to deuce (raw 4-4)", g.render().equals("deuce"));
        g.pointWonBy(1); check("advantage B", g.render().equals("advantage B"));
        g.pointWonBy(1); check("B wins from advantage", g.isOver() && g.getWinner() == 1);

        boolean threw = false;
        try { g.pointWonBy(0); } catch (IllegalStateException e) { threw = true; }
        check("throws when scoring after game over", threw);
    }

    static void testPart2() {
        System.out.println("PART 2 — Set (games + tiebreak):");
        String[] n = {"A", "B"};

        // 6-0 set.
        Set s1 = new Set(n);
        for (int i = 0; i < 24 && !s1.isOver(); i++) s1.pointWonBy(0);
        check("set 6-0", s1.isOver() && s1.getWinner() == 0
                && Arrays.equals(s1.getGamesWon(), new int[]{6, 0}));

        // 7-5 set (must reach 6-5 without winning, then 7-5).
        Set s2 = new Set(n);
        winGames(s2, 5, 5);
        winGames(s2, 1, 0);
        check("6-5 not over yet", !s2.isOver() && Arrays.equals(s2.getGamesWon(), new int[]{6, 5}));
        winGames(s2, 1, 0);
        check("set 7-5", s2.isOver() && s2.getWinner() == 0
                && Arrays.equals(s2.getGamesWon(), new int[]{7, 5}));

        // 7-6 via tiebreak.
        Set s3 = new Set(n);
        winGames(s3, 5, 5);
        winGames(s3, 1, 0);   // 6-5
        winGames(s3, 0, 1);   // 6-6 -> tiebreak begins
        check("tiebreak game started at 6-6", s3.currentGame() instanceof TiebreakGame);
        for (int i = 0; i < 7 && !s3.isOver(); i++) s3.pointWonBy(1);   // B wins tiebreak 7-0
        check("set 7-6 via tiebreak (B)", s3.isOver() && s3.getWinner() == 1
                && Arrays.equals(s3.getGamesWon(), new int[]{6, 7}));
    }

    static void testPart3() {
        System.out.println("PART 3 — Match (BO3 / BO5):");
        Match bo3 = new Match("A", "B", 3);
        for (int i = 0; i < 300 && !bo3.isOver(); i++) bo3.pointWonBy(0);
        check("BO3: A wins 2-0 in sets", bo3.isOver() && bo3.getWinner() == 0
                && Arrays.equals(bo3.getSetsWon(), new int[]{2, 0}));

        Match bo5 = new Match("A", "B", 5);
        for (int i = 0; i < 300 && !bo5.isOver(); i++) bo5.pointWonBy(0);
        check("BO5: A wins 3-0 in sets", bo5.isOver() && bo5.getWinner() == 0
                && Arrays.equals(bo5.getSetsWon(), new int[]{3, 0}));
    }

    static void testPart4and5() {
        System.out.println("PART 4 — in-class runTests driver (prints score after each point):");
        new Match("A", "B", 3).runTests("AAAABBBB");    // A love game, then B love game -> 1-1

        System.out.println("PART 5 — history query + serving rotation:");
        Match h = new Match("A", "B", 3);
        feed(h, "AAAA");    // A wins game 1 (points 1..4)
        check("scoreAfter(1) shows 15-0",      h.scoreAfter(1).contains("15-0"));
        check("scoreAfter(4) shows games 1-0", h.scoreAfter(4).contains("games 1-0"));
        check("server flips to B for game 2",  h.currentServer() == 1);
    }

    static void testAlternate() {
        System.out.println("ALTERNATE — NumericGame + RenderingMatch (deuce-collapse):");

        // Numeric layer, matching the prompt's example.
        NumericGame ng = new NumericGame("p1", "p2");
        ng.addScore("p1"); ng.addScore("p2"); ng.addScore("p1"); ng.addScore("p1");
        check("numeric (3,1), no result",
                Arrays.equals(ng.getScore(), new int[]{3, 1}) && ng.getResult() == null);
        ng.addScore("p2"); ng.addScore("p2");
        check("numeric deuce (3,3)", Arrays.equals(ng.getScore(), new int[]{3, 3}));
        ng.addScore("p1");
        check("numeric advantage (4,3)", Arrays.equals(ng.getScore(), new int[]{4, 3}));
        ng.addScore("p2");
        check("numeric collapse 4-4 -> (3,3)", Arrays.equals(ng.getScore(), new int[]{3, 3}));

        // Render layer, matching the prompt's example.
        RenderingMatch rm = new RenderingMatch("alice", "bob");
        rm.pointWonBy("alice");
        check("render 15-love", rm.score().equals("15-love"));
        rm.pointWonBy("bob"); rm.pointWonBy("bob"); rm.pointWonBy("bob");
        rm.pointWonBy("alice"); rm.pointWonBy("alice");                 // -> 3,3
        check("render deuce", rm.score().equals("deuce"));
        rm.pointWonBy("alice");
        check("render advantage alice", rm.score().equals("advantage alice"));
        rm.pointWonBy("bob");                                          // 4-4 collapses to 3-3
        check("render deuce again (collapse)", rm.score().equals("deuce"));
        rm.pointWonBy("bob"); rm.pointWonBy("bob");
        check("render winner bob",
                "winner bob".equals(rm.result()) && "winner bob".equals(rm.score()));
    }

    public static void main(String[] args) {
        testPart1();
        testPart2();
        testPart3();
        testPart4and5();
        testAlternate();
        System.out.printf("%n==== %d / %d checks passed ====%n", testsPassed, testsRun);
    }
}

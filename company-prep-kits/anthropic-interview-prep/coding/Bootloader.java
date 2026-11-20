/*
 * ============================================================================
 * Anthropic — Staff Software Engineer — Coding Round
 * Question: "Bootloader"  (see bootloader-prompt.txt)
 *
 * A file holds one instruction per line: "<op> <signed-int>", where op is:
 *   plus v : acc += v, then pc += 1
 *   next v : value ignored for control flow, pc += 1
 *   jump v : pc += v   (relative jump)
 * pc starts at 0, acc starts at 0. The program HALTS when either:
 *   - pc moves past the last instruction (pc >= n)  -> terminated, OR
 *   - pc reaches an instruction already executed     -> infinite loop.
 * IMPORTANT (commonly misread): return the accumulator reached by actually
 * executing the path — NOT the sum of all plus values in the file.
 *
 * This single file contains BOTH parts the interview walks through:
 *   PART 1  run() / findLoop()        — simulate; detect the loop; return the
 *                                       first index hit twice AND the full cycle.
 *   PART 2  repair()                  — exactly one instruction has jump/next
 *                                       swapped; flip the one that makes the
 *                                       program terminate; return its acc.
 *                                       Brute force O(n^2): the expected answer.
 *           repairOptimal()           — same result in O(n): precompute which
 *                                       indices reach the end, then one walk.
 *
 * `main` runs the prompt's exact example (loop [3,4,2]; repair -> 6), the
 * classic 9-line boot example (repair -> 8), and edge cases, asserting all
 * results and that brute-force and optimal agree. Verified on Corretto 21.
 *
 *   Compile       : javac Bootloader.java
 *   Run self-test : java Bootloader
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bootloader {

    private static final String PLUS = "plus", NEXT = "next", JUMP = "jump";

    /** One parsed instruction: an operation and its integer operand. */
    record Instruction(String op, int value) {}

    /**
     * Outcome of a simulation.
     * @param terminated true if pc ran past the end; false if a loop was detected
     * @param acc        accumulator value when the run stopped
     * @param repeatedPc first index about to execute a 2nd time (-1 if terminated)
     * @param trace      indices in execution order (does NOT include the repeat)
     */
    record RunResult(boolean terminated, int acc, int repeatedPc, List<Integer> trace) {}

    /** Parse "<op> <signed-int>" lines (blank lines skipped). Integer.parseInt handles a leading '+'. */
    static List<Instruction> parse(List<String> lines) {
        List<Instruction> program = new ArrayList<>();
        for (String raw : lines) {
            String line = raw.strip();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            program.add(new Instruction(parts[0], Integer.parseInt(parts[1])));
        }
        return program;
    }

    // =========================================================================
    // PART 1 — Simulate and detect the loop.
    // Record each index in a visited set BEFORE executing it; the first index
    // already in the set is where the loop closes. O(n) time / O(n) space:
    // each instruction is executed at most once.
    // =========================================================================
    static RunResult run(List<Instruction> program) {
        int pc = 0, acc = 0, n = program.size();
        Set<Integer> visited = new HashSet<>();
        List<Integer> trace = new ArrayList<>();

        while (pc < n) {
            if (pc < 0) throw new IllegalStateException("pc moved before the first instruction");
            if (!visited.add(pc)) {              // add() == false -> pc already executed -> loop
                return new RunResult(false, acc, pc, trace);
            }
            trace.add(pc);

            Instruction ins = program.get(pc);
            switch (ins.op()) {
                case PLUS -> { acc += ins.value(); pc += 1; }
                case NEXT -> pc += 1;            // operand ignored while op is 'next'
                case JUMP -> pc += ins.value();
                default   -> throw new IllegalArgumentException("Unknown op: " + ins.op());
            }
        }
        return new RunResult(true, acc, -1, trace);   // pc >= n -> terminated cleanly
    }

    /**
     * Full loop body: the cycle of indices from the first occurrence of the
     * repeated index through the end of the visited trace. Empty if it terminates.
     * For the prompt example this is [3, 4, 2].
     */
    static List<Integer> findLoop(List<Instruction> program) {
        RunResult r = run(program);
        if (r.terminated()) return List.of();
        int start = r.trace().indexOf(r.repeatedPc());      // where the cycle closes
        return new ArrayList<>(r.trace().subList(start, r.trace().size()));
    }

    // =========================================================================
    // PART 2 — Repair the single swapped instruction (brute force).
    // *** the expected interview answer ***
    //
    // CHANGES FROM PART 1:
    //   1. flip(): a 'jump' should have been 'next' or vice versa; 'plus' is
    //      never the culprit, so it is left untouched.
    //   2. runWithPatch(): same simulator as run(), but the instruction at
    //      patchIndex is flipped on the fly. It also returns terminated=false
    //      (instead of throwing) if a flipped jump sends pc < 0, so a bad
    //      candidate is simply skipped rather than crashing the search.
    //   3. repair(): only the indices on the ORIGINAL looping trace can be the
    //      culprit (an instruction never executed can't cause the loop), so we
    //      try flipping just those — re-running from the start to VALIDATE each
    //      candidate — and return the acc of the version that terminates.
    // Time O(n^2): up to n candidates each re-run an O(n) simulation.
    // =========================================================================
    static Instruction flip(Instruction ins) {              // change #1
        return switch (ins.op()) {
            case JUMP -> new Instruction(NEXT, ins.value());
            case NEXT -> new Instruction(JUMP, ins.value());
            default   -> ins;                               // 'plus' is never corrupted
        };
    }

    static RunResult runWithPatch(List<Instruction> program, int patchIndex) {  // change #2
        int pc = 0, acc = 0, n = program.size();
        Set<Integer> visited = new HashSet<>();

        while (pc < n) {
            if (pc < 0) return new RunResult(false, acc, -1, List.of());   // invalid -> skip candidate
            if (!visited.add(pc)) return new RunResult(false, acc, pc, List.of());

            Instruction ins = program.get(pc);
            if (pc == patchIndex) ins = flip(ins);          // the one-line patch
            switch (ins.op()) {
                case PLUS -> { acc += ins.value(); pc += 1; }
                case NEXT -> pc += 1;
                case JUMP -> pc += ins.value();
                default   -> throw new IllegalArgumentException("Unknown op: " + ins.op());
            }
        }
        return new RunResult(true, acc, -1, List.of());
    }

    /** @return [badInstructionIndex, accumulatorAfterRepair]; throws if no single flip fixes it. */
    static int[] repair(List<Instruction> program) {        // change #3
        RunResult original = run(program);
        if (original.terminated()) throw new IllegalStateException("Program already terminates");

        for (int i : original.trace()) {                    // culprit must be on the executed path
            String op = program.get(i).op();
            if (!op.equals(JUMP) && !op.equals(NEXT)) continue;   // 'plus' can't be the bug
            RunResult patched = runWithPatch(program, i);
            if (patched.terminated()) return new int[]{i, patched.acc()};   // validated by rerun
        }
        throw new IllegalStateException("No single jump/next flip repairs the program");
    }

    // =========================================================================
    // PART 2 (optimal) — same result in O(n) instead of O(n^2).
    //
    // KEY INSIGHT: flipping index i and rerunning terminates IFF, after the
    // flip redirects pc to `target`, normal execution from `target` reaches the
    // end. So precompute reachesEnd[]/accFromEnd[] over the NORMAL graph once,
    // then walk the original path a single time and test each flip in O(1).
    //
    // Why it's safe: every index on the original looping trace has
    // reachesEnd=false (the original program never terminates from there), so a
    // flip whose target has reachesEnd=true provably escapes the loop, and the
    // post-target path can't pass back through i. acc = (plus values accumulated
    // up to i) + (plus values from target to the end).
    // =========================================================================
    private int[] state;          // 0=unvisited,1=on-stack,2=done  (functional-graph DFS)
    private boolean[] reachesEnd; // can normal execution from i run past the end?
    private long[] accFromEnd;    // sum of plus values from i to the end (valid iff reachesEnd[i])

    int[] repairOptimal(List<Instruction> program) {
        int n = program.size();
        state = new int[n];
        reachesEnd = new boolean[n];
        accFromEnd = new long[n];
        for (int i = 0; i < n; i++) if (state[i] == 0) terminationDfs(program, i);  // precompute O(n)

        Set<Integer> visited = new HashSet<>();
        long accBefore = 0;        // accumulator just BEFORE executing the current pc
        int pc = 0;
        while (pc >= 0 && pc < n && visited.add(pc)) {
            Instruction ins = program.get(pc);
            // Try the flip at this position (O(1) thanks to the precomputed table).
            if (ins.op().equals(JUMP)) {                     // jump -> next: target = pc + 1
                int target = pc + 1;
                if (terminates(target, n)) return new int[]{pc, (int) (accBefore + accFrom(target, n))};
            } else if (ins.op().equals(NEXT)) {              // next -> jump: target = pc + value
                int target = pc + ins.value();
                if (terminates(target, n)) return new int[]{pc, (int) (accBefore + accFrom(target, n))};
            }
            // Otherwise advance normally.
            switch (ins.op()) {
                case PLUS -> { accBefore += ins.value(); pc += 1; }
                case NEXT -> pc += 1;
                case JUMP -> pc += ins.value();
                default   -> throw new IllegalArgumentException("Unknown op: " + ins.op());
            }
        }
        if (pc >= n) throw new IllegalStateException("Program already terminates");
        throw new IllegalStateException("No single jump/next flip repairs the program");
    }

    /** Memoized DFS over the out-degree-1 normal graph: fills reachesEnd[i] and accFromEnd[i]. */
    private boolean terminationDfs(List<Instruction> program, int i) {
        int n = program.size();
        if (state[i] == 1) return false;          // back-edge on current path -> on a cycle
        if (state[i] == 2) return reachesEnd[i];
        state[i] = 1;

        Instruction ins = program.get(i);
        int next = ins.op().equals(JUMP) ? i + ins.value() : i + 1;  // 'plus'/'next' both go i+1
        long here = ins.op().equals(PLUS) ? ins.value() : 0;

        boolean ok;
        long acc = here;
        if (next >= n) {                          // steps past the end -> terminates
            ok = true;
        } else if (next < 0) {                    // invalid -> cannot terminate this way
            ok = false;
        } else {
            ok = terminationDfs(program, next);
            if (ok) acc += accFromEnd[next];
        }
        reachesEnd[i] = ok;
        accFromEnd[i] = ok ? acc : 0;
        state[i] = 2;
        return ok;
    }

    private boolean terminates(int target, int n) {          // stepping to target reaches the end?
        return target >= n || (target >= 0 && reachesEnd[target]);
    }
    private long accFrom(int target, int n) {                // plus values from target to the end
        return target >= n ? 0 : accFromEnd[target];
    }

    // =========================================================================
    // Self-test harness (not part of the interview answer)
    // =========================================================================
    public static void main(String[] args) {
        boolean ok = true;
        Bootloader bl = new Bootloader();

        // ---- prompt's exact example: loops; loop body is [3,4,2]; repair -> acc 6 ----
        List<String> ex = List.of(
            "plus +1",   // 0
            "jump +2",   // 1
            "plus +100", // 2
            "next +2",   // 3
            "jump -2",   // 4
            "plus +5");  // 5
        List<Instruction> p = parse(ex);

        System.out.println("=== PART 1: detect the loop ===");
        RunResult r = run(p);
        List<Integer> loop = findLoop(p);
        System.out.println("  terminated=" + r.terminated() + " repeatedPc=" + r.repeatedPc()
                + " trace=" + r.trace() + "  loop=" + loop);
        ok &= !r.terminated() && r.repeatedPc() == 3 && loop.equals(List.of(3, 4, 2));

        System.out.println("\n=== PART 2: repair the swapped instruction ===");
        int[] fix = repair(p);
        int[] fixOpt = bl.repairOptimal(p);
        System.out.println("  brute force : bad index=" + fix[0]    + ", acc=" + fix[1]
                + "   (flip 3 'next+2'->'jump+2', program terminates)");
        System.out.println("  optimal O(n): bad index=" + fixOpt[0] + ", acc=" + fixOpt[1]);
        ok &= fix[0] == 3 && fix[1] == 6 && Arrays.equals(fix, fixOpt);

        // ---- classic 9-line boot example (jump->next repair at index 7, acc 8) ----
        System.out.println("\n=== extra: classic 9-line program (jump->next repair) ===");
        List<Instruction> p2 = parse(List.of(
            "next +0",  // 0
            "plus +1",  // 1
            "jump +4",  // 2
            "plus +3",  // 3
            "jump -3",  // 4
            "plus -99", // 5
            "plus +1",  // 6
            "jump -4",  // 7  <- corrupted: should be 'next'
            "plus +6"));// 8
        int[] f2 = repair(p2);
        int[] f2o = bl.repairOptimal(p2);
        System.out.println("  loop body=" + findLoop(p2));
        System.out.println("  brute force : bad index=" + f2[0]  + ", acc=" + f2[1]);
        System.out.println("  optimal O(n): bad index=" + f2o[0] + ", acc=" + f2o[1]);
        ok &= f2[0] == 7 && f2[1] == 8 && Arrays.equals(f2, f2o);

        // ---- edge: already-terminating program -> findLoop empty, acc is the path value ----
        System.out.println("\n=== edge: program that already terminates ===");
        List<Instruction> p3 = parse(List.of("plus +1", "jump +2", "plus +99", "plus +5"));
        RunResult r3 = run(p3);   // 0:plus(+1)->1, 1:jump+2->3, 3:plus(+5)->4 end ; index 2 skipped
        System.out.println("  terminated=" + r3.terminated() + " acc=" + r3.acc()
                + " loop=" + findLoop(p3) + "  (note: +99 at idx 2 is NEVER executed)");
        ok &= r3.terminated() && r3.acc() == 6 && findLoop(p3).isEmpty();

        // ---- edge: a jump that overshoots far past the end still terminates ----
        List<Instruction> p4 = parse(List.of("plus +7", "jump +50"));
        RunResult r4 = run(p4);
        ok &= r4.terminated() && r4.acc() == 7;
        System.out.println("  overshoot jump terminates: acc=" + r4.acc() + " (expected 7)");

        System.out.println(ok ? "\nALL TESTS PASSED ✅" : "\nTEST FAILED ❌");
        if (!ok) System.exit(1);
    }
}

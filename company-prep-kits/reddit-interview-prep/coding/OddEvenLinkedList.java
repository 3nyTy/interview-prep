import java.util.ArrayList;
import java.util.List;

/**
 * Reddit — Staff SWE coding round
 * Problem: Odd Even Linked List (LeetCode 328 family)
 *
 * Given the head of a singly linked list, group all ODD-indexed nodes together
 * followed by the EVEN-indexed nodes (1-based: first node is odd). Preserve the
 * relative order within each group. Return the reordered head.
 *
 * All parts below run in ONE pass, O(n) time, O(1) extra space (pointer splicing,
 * no new nodes allocated). Each part highlights exactly what changed vs the prior.
 */
public class OddEvenLinkedList {

    /** Singly-linked list node. */
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // =====================================================================
    // PART 1 — Core problem: group by INDEX parity (1-based).
    // Input  [1,2,3,4,5]        -> [1,3,5, 2,4]
    // Input  [2,1,3,5,6,4,7]    -> [2,3,6,7, 1,5,4]
    //
    // Idea: weave two chains as we walk once.
    //   - `odd`  builds the chain of nodes at positions 1,3,5,...
    //   - `even` builds the chain of nodes at positions 2,4,6,...
    //   We keep `evenHead` so we can splice the even chain onto the tail of
    //   the odd chain at the end. The invariant each iteration: `odd` and
    //   `even` are the current tails of their respective chains, and
    //   `even.next` is the next odd node (or null).
    // =====================================================================
    public static ListNode oddEvenList(ListNode head) {
        // 0 or 1 node: already grouped, nothing to weave.
        if (head == null || head.next == null) return head;

        ListNode odd = head;            // tail of the odd-index chain
        ListNode even = head.next;      // tail of the even-index chain
        ListNode evenHead = even;       // remember where evens start, to reattach later

        // `even` and `even.next` being non-null guarantees there is at least
        // one more odd node (even.next) to pull in, so both advances are safe.
        while (even != null && even.next != null) {
            odd.next = even.next;       // link current odd tail to the next odd node
            odd = odd.next;             // advance odd tail
            even.next = odd.next;       // link current even tail to the next even node
            even = even.next;           // advance even tail (may become null -> loop ends)
        }

        odd.next = evenHead;            // stitch the even chain after the odd chain
        return head;                    // head is unchanged (it is the first odd node)
    }

    // =====================================================================
    // PART 2 — Follow-up: group by NODE VALUE parity instead of index.
    // "Put all nodes with ODD values first, then all EVEN values, order kept."
    // Input  [1,2,3,4,5]  -> [1,3,5, 2,4]     (same output here by coincidence)
    // Input  [2,1,4,3,6]  -> [1,3, 2,4,6]
    //
    // CHANGES FROM PART 1:
    //   * We can no longer seed `odd=head`/`even=head.next` by position — the
    //     first node might be even-valued. Instead we use two DUMMY heads and
    //     append each node to whichever bucket its VALUE selects.
    //   * The branch key is `cur.val % 2` (value), not the position counter.
    //   * Must null-terminate the even tail at the end (Part 1 didn't need to,
    //     because there the last real node was always reached via the weave).
    // =====================================================================
    public static ListNode groupByValueParity(ListNode head) {
        // Dummy heads let us append without special-casing the first node of each bucket.
        ListNode oddDummy = new ListNode(0);
        ListNode evenDummy = new ListNode(0);
        ListNode oddTail = oddDummy, evenTail = evenDummy;

        for (ListNode cur = head; cur != null; cur = cur.next) {
            if ((cur.val & 1) == 1) {   // odd VALUE  -> odd bucket
                oddTail.next = cur;
                oddTail = cur;
            } else {                    // even VALUE -> even bucket
                evenTail.next = cur;
                evenTail = cur;
            }
        }

        evenTail.next = null;               // terminate the list (dangling ref must be cut)
        oddTail.next = evenDummy.next;      // odd chain, then even chain
        return oddDummy.next;               // real head is first odd-valued node (or first even if none)
    }

    // =====================================================================
    // PART 3 — Generalization: group into K buckets by (index-1) % k, 1-based.
    // Concatenate bucket 0, then bucket 1, ..., then bucket k-1, order preserved.
    // For k = 2 this reduces to Part 1 (bucket 0 = odd indices, bucket 1 = even).
    // Input [1,2,3,4,5,6,7], k=3 -> [1,4,7, 2,5, 3,6]
    //
    // CHANGES FROM PART 1:
    //   * Two chains -> K chains, tracked by arrays of dummy heads and tails,
    //     so the approach scales to any k in O(n) time / O(k) extra pointers.
    //   * Bucket is chosen by a rotating counter `pos % k` instead of a
    //     hard-coded odd/even split.
    //   * Stitch buckets 0..k-1 in order, skipping empty ones, and terminate.
    // =====================================================================
    public static ListNode groupByIndexModK(ListNode head, int k) {
        if (k <= 1 || head == null) return head;   // k<=1 is a no-op

        ListNode[] dummy = new ListNode[k];         // dummy head per bucket
        ListNode[] tail = new ListNode[k];          // running tail per bucket
        for (int i = 0; i < k; i++) {
            dummy[i] = new ListNode(0);
            tail[i] = dummy[i];
        }

        int pos = 0;                                // 0-based position counter
        for (ListNode cur = head; cur != null; cur = cur.next) {
            int b = pos % k;                        // which bucket this node belongs to
            tail[b].next = cur;
            tail[b] = cur;
            pos++;
        }

        // Chain the buckets together in order: 0 -> 1 -> ... -> k-1.
        ListNode resultHead = null, resultTail = null;
        for (int i = 0; i < k; i++) {
            if (dummy[i].next == null) continue;    // skip empty bucket
            if (resultHead == null) {
                resultHead = dummy[i].next;
            } else {
                resultTail.next = dummy[i].next;
            }
            resultTail = tail[i];                   // new overall tail is this bucket's tail
        }
        if (resultTail != null) resultTail.next = null;   // terminate final chain
        return resultHead;
    }

    // =====================================================================
    // Test harness
    // =====================================================================
    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) { t.next = new ListNode(v); t = t.next; }
        return dummy.next;
    }

    private static List<Integer> toList(ListNode h) {
        List<Integer> out = new ArrayList<>();
        for (; h != null; h = h.next) out.add(h.val);
        return out;
    }

    private static void check(String name, List<Integer> got, List<Integer> want) {
        String status = got.equals(want) ? "PASS" : "FAIL";
        System.out.printf("[%s] %-28s got=%s want=%s%n", status, name, got, want);
    }

    public static void main(String[] args) {
        // ---- Part 1 ----
        check("p1: example1", toList(oddEvenList(build(1, 2, 3, 4, 5))), List.of(1, 3, 5, 2, 4));
        check("p1: example2", toList(oddEvenList(build(2, 1, 3, 5, 6, 4, 7))), List.of(2, 3, 6, 7, 1, 5, 4));
        check("p1: empty", toList(oddEvenList(build())), List.of());
        check("p1: single", toList(oddEvenList(build(9))), List.of(9));
        check("p1: two", toList(oddEvenList(build(1, 2))), List.of(1, 2));

        // ---- Part 2: group by VALUE parity ----
        check("p2: mixed", toList(groupByValueParity(build(2, 1, 4, 3, 6))), List.of(1, 3, 2, 4, 6));
        check("p2: all-even", toList(groupByValueParity(build(2, 4, 6))), List.of(2, 4, 6));
        check("p2: all-odd", toList(groupByValueParity(build(1, 3, 5))), List.of(1, 3, 5));
        check("p2: empty", toList(groupByValueParity(build())), List.of());

        // ---- Part 3: group by index % k ----
        check("p3: k=3", toList(groupByIndexModK(build(1, 2, 3, 4, 5, 6, 7), 3)), List.of(1, 4, 7, 2, 5, 3, 6));
        check("p3: k=2 == part1", toList(groupByIndexModK(build(1, 2, 3, 4, 5), 2)), List.of(1, 3, 5, 2, 4));
        check("p3: k=1 no-op", toList(groupByIndexModK(build(1, 2, 3), 1)), List.of(1, 2, 3));
        check("p3: k>len", toList(groupByIndexModK(build(1, 2), 5)), List.of(1, 2));
    }
}

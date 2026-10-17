// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.internal.residency;

import java.util.BitSet;
import java.util.SplittableRandom;

/** Independent bounded scalar/BitSet oracle; no Display, mock SWT or native dependency. */
public final class SparseMirrorContract {
    private SparseMirrorContract() { }
    private static long checks;
    private static void equal(long expected, long actual, String detail) {
        checks++;
        if (expected != actual) throw new AssertionError(detail + ": " + expected + " != " + actual);
    }
    private static ItemBitSet fixture(int origin, int kind, BitSet reference) {
        var bits = new ItemBitSet();
        if (kind == ItemBitSet.ARRAY) {
            for (int i : new int[] {0, 17, 4096, 32767, 65535}) {
                bits.set(origin + i, true); reference.set(i);
            }
        } else if (kind == ItemBitSet.RUN) {
            bits.setRange(origin, origin + 128, true); reference.set(0, 128);
            bits.setRange(origin + 65472, origin + 65535, true); reference.set(65472, 65535);
            bits.set(origin + 65535, true); reference.set(65535);
        } else {
            for (int i = 0; i <= ItemBitSet.ARRAY_MAX; i++) {
                bits.set(origin + 2 * i, true); reference.set(2 * i);
            }
            for (int i = 65472; i < 65536; i++) {
                bits.set(origin + i, true); reference.set(i);
            }
        }
        equal(1, bits.get(origin + 65535) ? 1 : 0, "fixture maximum bit");
        return bits;
    }
    /** Runs all ordinary observations and both compressed endpoint counterexamples. */
    public static long verify() {
        checks = 0;
        for (int origin : new int[] {0, 65536, 1 << 30, Integer.MAX_VALUE & ~65535}) {
            for (int kind : new int[] {ItemBitSet.ARRAY, ItemBitSet.BITMAP, ItemBitSet.RUN}) {
                var reference = new BitSet(65536);
                var bits = fixture(origin, kind, reference);
                int maximum = Math.min(65536, Integer.MAX_VALUE - origin);
                var random = new SplittableRandom(0x54A9L + origin + kind);
                for (int q = 0; q < 4000; q++) {
                    int from = random.nextInt(maximum + 1);
                    int to = from + random.nextInt(maximum - from + 1);
                    equal(Math.min(to, reference.nextClearBit(from)) + (long) origin,
                            bits.runEnd(origin + from, origin + to), "run endpoint");
                    int next = reference.nextSetBit(from);
                    equal(next >= 0 && next < to ? origin + (long) next : -1,
                            bits.next(origin + from, origin + to), "next set");
                    equal(reference.get(from, to).cardinality(), bits.count(origin + from, origin + to), "count");
                    equal(reference.get(from) ? 1 : 0, bits.get(origin + from) ? 1 : 0, "point");
                }
                equal(origin + (long) Math.min(65535, reference.nextClearBit(65520)),
                        bits.runEnd(origin + 65520, origin + 65535), "clipped tail");
                equal(1, bits.get(origin + 65535) ? 1 : 0, "inclusive maximum point");
            }
        }
        var bits = new ItemBitSet();
        var oracle = new BitSet();
        var random = new SplittableRandom(0xF00D);
        int size = 512;
        int[][] scratch = {new int[16]};
        for (int op = 0; op < 4000; op++) {
            int at = random.nextInt(size);
            switch (op % 4) {
                case 0 -> { boolean on = random.nextBoolean(); bits.set(at, on); oracle.set(at, on); }
                case 1 -> {
                    int end = Math.min(size, at + 16); boolean on = random.nextBoolean();
                    bits.setRange(at, end, on); oracle.set(at, end, on);
                }
                case 2 -> {
                    bits.shift(at, 1, size, scratch);
                    for (int i = size; i > at; i--) oracle.set(i, oracle.get(i - 1));
                    oracle.clear(at); size++;
                }
                default -> {
                    bits.set(at, false); bits.shift(at + 1, -1, size, scratch);
                    for (int i = at; i < size - 1; i++) oracle.set(i, oracle.get(i + 1));
                    oracle.clear(--size);
                }
            }
            equal(oracle.cardinality(), bits.count(0, size), "mutation count");
            for (int i = 0; i < size; i++) equal(oracle.get(i) ? 1 : 0, bits.get(i) ? 1 : 0, "mutation point");
        }
        return checks;
    }
    /** The original Nebula mirror returns a negative value for each of these two legal queries. */
    public static void witness(boolean defectExpected) {
        for (int kind : new int[] {ItemBitSet.BITMAP, ItemBitSet.RUN}) {
            var bits = fixture(Integer.MAX_VALUE & ~65535, kind, new BitSet());
            int actual = bits.runEnd(Integer.MAX_VALUE - 15, Integer.MAX_VALUE);
            boolean defective = actual != Integer.MAX_VALUE;
            if (defective != defectExpected) throw new AssertionError("counterexample expectation: " + actual);
            System.out.println("ENDPOINT_WITNESS encoding=" + kind + " actual=" + actual + " defective=" + defective);
        }
    }
    public static void main(String[] args) {
        if (args.length == 1) { witness(Boolean.parseBoolean(args[0])); return; }
        System.out.println("SPARSE_CONTRACT checks=" + verify());
    }
}

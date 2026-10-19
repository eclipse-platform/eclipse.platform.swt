/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.residency;

import java.util.Arrays;
import java.util.SplittableRandom;

/** Independent scalar/query oracle; direct helper proof, not a native widget test. */
public final class ViewportQueryContract {
    private static long checks;
    private static long root = 0x9e3779b97f4a7c15L;
    private ViewportQueryContract() { }
    private static void equal(int expected, int actual) {
        checks++;
        if (expected != actual) throw new AssertionError("query " + checks + ": " + expected + " != " + actual);
        root = Long.rotateLeft(root, 7) ^ actual ^ checks;
    }
    private static void require(boolean value) { equal(1, value ? 1 : 0); }
    private static int scalarCount(boolean[] values, int from, int to) {
        int n = 0;
        for (int i = from; i < to; i++) if (values[i]) n++;
        return n;
    }
    private static int scalarNext(boolean[] values, int from) {
        if (from < 0 || from >= values.length) return -1;
        for (int i = from; i < values.length; i++) if (values[i]) return i;
        return -1;
    }
    private static void query(ItemLanes lanes, boolean[][] expected, int from, int to) {
        int bytes = lanes.bytes();
        int count = lanes.count;
        int[][] roots = lanes.patterns.clone();
        for (int lane = 0; lane < expected.length; lane++) {
            equal(scalarCount(expected[lane], from, to), lanes.countRange(lane, from, to));
            equal(scalarNext(expected[lane], from), lanes.nextSet(lane, from));
            equal(-1, lanes.nextSet(lane, -1));
            equal(-1, lanes.nextSet(lane, expected[lane].length));
        }
        int[] output = new int[to - from + 3];
        Arrays.fill(output, 0x12345678);
        lanes.gather(from, to, output);
        for (int row = from; row < to; row++) {
            int bits = 0;
            for (int lane = 0; lane < expected.length; lane++) if (expected[lane][row]) bits |= 1 << lane;
            equal(bits, output[row - from]);
            equal(bits, lanes.gather(row));
        }
        for (int i = to - from; i < output.length; i++) equal(0x12345678, output[i]);
        equal(bytes, lanes.bytes()); equal(count, lanes.count);
        for (int i = 0; i < roots.length; i++) require(roots[i] == lanes.patterns[i]);
    }
    private static void phasedPatterns() {
        int[] masks = {0, -1, 1, Integer.MIN_VALUE, 0xaaaaaaaa, 0x55555555, 0x13579bdf};
        for (int stride = 1; stride <= 32; stride++) for (int cut = 0; cut < stride; cut++) {
            for (int mask : masks) {
                ItemLanes lanes = new ItemLanes(1); lanes.setCount(112);
                boolean[][] expected = new boolean[1][112];
                lanes.setPattern(0, 3, 107, stride, mask);
                for (int i = 3; i < 107; i++) expected[0][i] = ((mask >>> ((i - 3) % stride)) & 1) != 0;
                lanes.setRange(0, 3, 3 + cut, false); Arrays.fill(expected[0], 3, 3 + cut, false);
                // Overrides inside a pattern and an explicit value in a gap.
                lanes.set(0, 71, !expected[0][71]); expected[0][71] = !expected[0][71];
                lanes.set(0, 1, true); expected[0][1] = true;
                for (int from = 0; from <= 112; from++) {
                    equal(scalarCount(expected[0], from, 112), lanes.countRange(0, from, 112));
                    equal(scalarNext(expected[0], from), lanes.nextSet(0, from));
                }
                query(lanes, expected, 0, 12); query(lanes, expected, 60, 80);
                query(lanes, expected, 103, 112); query(lanes, expected, 112, 112);
            }
        }
    }
    private static void mutations() {
        SplittableRandom random = new SplittableRandom(0x20261014L);
        ItemLanes lanes = new ItemLanes(3); lanes.setCount(96);
        boolean[][] expected = new boolean[3][96];
        for (int step = 0; step < 1800; step++) {
            int n = expected[0].length, lane = random.nextInt(3), at = random.nextInt(n + 1);
            int end = at + random.nextInt(n - at + 1), operation = random.nextInt(6);
            switch (operation) {
                case 0 -> {
                    int stride = random.nextInt(1, 33), mask = random.nextInt();
                    lanes.setPattern(lane, at, end, stride, mask);
                    for (int i = at; i < end; i++) expected[lane][i] = ((mask >>> ((i - at) % stride)) & 1) != 0;
                }
                case 1 -> { boolean on = random.nextBoolean(); lanes.setRange(lane, at, end, on); Arrays.fill(expected[lane], at, end, on); }
                case 2 -> { if (at < n) { boolean on = random.nextBoolean(); lanes.set(lane, at, on); expected[lane][at] = on; } }
                case 3 -> {
                    if (n < 160) {
                        lanes.insert(at);
                        for (int k = 0; k < 3; k++) {
                            boolean[] a = new boolean[n + 1];
                            System.arraycopy(expected[k], 0, a, 0, at);
                            System.arraycopy(expected[k], at, a, at + 1, n - at); expected[k] = a;
                        }
                    }
                }
                case 4 -> {
                    if (at < n) {
                        lanes.remove(at);
                        for (int k = 0; k < 3; k++) {
                            boolean[] a = new boolean[n - 1]; System.arraycopy(expected[k], 0, a, 0, at);
                            System.arraycopy(expected[k], at + 1, a, at, n - at - 1); expected[k] = a;
                        }
                    }
                }
                default -> {
                    int size = random.nextInt(40, 161); lanes.setCount(size);
                    for (int k = 0; k < 3; k++) expected[k] = Arrays.copyOf(expected[k], size);
                }
            }
            n = expected[0].length;
            for (int q = 0; q < 4; q++) {
                int from = random.nextInt(n + 1), to = from + random.nextInt(Math.min(25, n - from) + 1);
                query(lanes, expected, from, to);
            }
        }
    }
    private static ItemLanes prefixFixture() {
        // Direct installation is test-only: normalized records avoid quadratic fixture construction.
        ItemLanes lanes = new ItemLanes(1); int n = 65536;
        lanes.setCount(n * 8 + 4); lanes.patterns[0] = new int[n * ItemLanes.P]; lanes.patternCount[0] = n;
        for (int k = 0; k < n; k++) {
            int off = k * ItemLanes.P;
            lanes.patterns[0][off] = k * 8 + 2; lanes.patterns[0][off + 1] = k * 8 + 5;
            lanes.patterns[0][off + 2] = 2; lanes.patterns[0][off + 3] = 1; lanes.patterns[0][off + 4] = k & 1;
        }
        return lanes;
    }
    private static void distantAndBoundaries() {
        ItemLanes lanes = prefixFixture(); int end = lanes.count;
        int[] p = lanes.patterns[0]; int bytes = lanes.bytes();
        for (int from = end - 80; from <= end; from++) {
            int count = 0, first = -1;
            for (int row = from; row < end; row++) {
                int k = row / 8, local = row % 8;
                boolean on = k < 65536 && local >= 2 && local < 5 && (((local - 2 + (k & 1)) & 1) == 0);
                if (on) { count++; if (first < 0) first = row; }
            }
            equal(count, lanes.countRange(0, from, end)); equal(first, lanes.nextSet(0, from));
        }
        require(p == lanes.patterns[0]); equal(bytes, lanes.bytes());
        lanes = new ItemLanes(1); lanes.setCount(Integer.MAX_VALUE);
        int base = Integer.MAX_VALUE - 160;
        boolean[] expected = new boolean[160];
        lanes.setPattern(0, base + 9, Integer.MAX_VALUE, 3, 5);
        for (int i = 9; i < 160; i++) expected[i] = ((5 >>> ((i - 9) % 3)) & 1) != 0;
        lanes.setRange(0, base + 40, base + 51, false); Arrays.fill(expected, 40, 51, false);
        lanes.set(0, base + 2, true); expected[2] = true;
        for (int from = 0; from <= 160; from++) {
            equal(scalarCount(expected, from, 160), lanes.countRange(0, base + from, Integer.MAX_VALUE));
            int next = scalarNext(expected, from);
            equal(next < 0 ? -1 : base + next, lanes.nextSet(0, base + from));
        }
        equal(0, lanes.countRange(-7, 3, 3)); equal(-1, lanes.nextSet(-7, -1));
        equal(-1, lanes.nextSet(-7, Integer.MAX_VALUE));
        try { lanes.gather(0, 0, null); throw new AssertionError("null accepted"); } catch (NullPointerException expectedFailure) { checks++; }
    }
    private static void bench() {
        ItemLanes lanes = prefixFixture(); int from = lanes.count - 3; long total = 0;
        for (int i = 0; i < 200; i++) total += lanes.countRange(0, from, from + 1) + lanes.nextSet(0, from);
        for (int sample = 0; sample < 5; sample++) {
            long start = System.nanoTime();
            for (int i = 0; i < 3000; i++) total += lanes.countRange(0, from, from + 1) + lanes.nextSet(0, from);
            System.out.println("PREFIX_PROBE queries=6000 ns=" + (System.nanoTime() - start) + " sink=" + total);
        }
    }
    public static void verify() {
        checks = 0; root = 0x9e3779b97f4a7c15L;
        phasedPatterns(); mutations(); distantAndBoundaries();
        System.out.println("QUERY_CONTRACT checks=" + checks + " root=" + Long.toHexString(root));
    }
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--bench")) bench(); else if (args.length == 0) verify();
        else throw new IllegalArgumentException("[--bench]");
    }
}

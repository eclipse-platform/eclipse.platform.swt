/* Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0 */
package org.eclipse.swt.widgets;

import java.math.BigInteger;
import java.util.Random;

/** Independent rational oracle for the existing one-axis geometry; no native-pixel claim. */
public final class ScrollbarFullRangeProof {
    private static final int MAX = Integer.MAX_VALUE;
    private static final String[] NAMES = {"mulDiv", "hasThumb", "thumbLength", "thumbStart", "selectionAt", "hit"};
    private static final long[] failures = new long[NAMES.length];
    private static long checks;
    private static long ordinaryFailures;
    private static long digest = 0x53575447454f4dL;
    private static boolean strict;
    private static boolean ordinary;

    private ScrollbarFullRangeProof() { }

    // BigInteger is deliberately test-only. It does not reproduce the product's intermediates.
    private static int rational(long a, long b, long c) {
        if (c == 0) return 0;
        BigInteger product = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        BigInteger denominator = BigInteger.valueOf(c).abs();
        BigInteger[] qr = product.abs().divideAndRemainder(denominator);
        if (qr[1].shiftLeft(1).compareTo(denominator) >= 0) qr[0] = qr[0].add(BigInteger.ONE);
        return ((product.signum() < 0) == (c < 0) ? qr[0] : qr[0].negate()).intValue();
    }

    private static void eq(int kind, int expected, int actual) {
        checks++;
        digest = Long.rotateLeft(digest, 9) ^ expected;
        if (expected != actual) {
            failures[kind]++;
            if (ordinary) ordinaryFailures++;
            if (strict) throw new AssertionError(NAMES[kind] + " expected=" + expected + " actual=" + actual);
        }
    }

    private static int length(int track, int minThumb, int min, int max, int thumb) {
        return track < minThumb ? 0 : Math.max(minThumb, rational(track, (long) thumb + 1, (long) max - min + 1));
    }

    private static int start(int track, int minThumb, int min, int max, int thumb, int selected) {
        long last = (long) max - thumb;
        return last <= min ? -1 : rational((long) track - length(track, minThumb, min, max, thumb),
                (long) selected - min, last - min);
    }

    private static int selection(int position, int track, int minThumb, int min, int max, int thumb) {
        long last = (long) max - thumb;
        long free = (long) track - length(track, minThumb, min, max, thumb);
        if (last <= min || free <= 0) return min;
        return min + rational(Math.max(0L, Math.min(free, position)), last - min, free);
    }

    private static int hit(int position, int arrow, int track, int minThumb, int min, int max, int thumb, int selected) {
        BigInteger pos = BigInteger.valueOf(position);
        BigInteger shaftEnd = BigInteger.valueOf(arrow).add(BigInteger.valueOf(track));
        BigInteger barEnd = shaftEnd.add(BigInteger.valueOf(arrow));
        if (position < 0 || pos.compareTo(barEnd) >= 0) return 0;
        if (position < arrow) return 1;
        if (pos.compareTo(shaftEnd) >= 0) return 5;
        int size = length(track, minThumb, min, max, thumb);
        if (size == 0) return 0;
        int begin = start(track, minThumb, min, max, thumb, selected);
        BigInteger offset = pos.subtract(BigInteger.valueOf(arrow));
        if (offset.compareTo(BigInteger.valueOf(begin)) < 0) return 2;
        if (offset.compareTo(BigInteger.valueOf(begin).add(BigInteger.valueOf(size))) < 0) return 3;
        return 4;
    }

    private static int clamp(long n) { return (int) Math.max(Integer.MIN_VALUE, Math.min(MAX, n)); }

    private static void geometry(int track, int minThumb, int min, int max, int thumb, int selected, int arrow, int drag) {
        if (track < 0 || minThumb <= 0 || min < 0 || max <= min || thumb < 1 || thumb > max - min
                || selected < min || selected > max - thumb || arrow < 0) throw new AssertionError("invalid test state");
        eq(1, track >= minThumb ? 1 : 0, ViewportScrollBarGeometry.hasThumb(track, minThumb) ? 1 : 0);
        int expectedLength = length(track, minThumb, min, max, thumb);
        eq(2, expectedLength, ViewportScrollBarGeometry.thumbLength(track, minThumb, min, max, thumb));
        int expectedStart = start(track, minThumb, min, max, thumb, selected);
        eq(3, expectedStart, ViewportScrollBarGeometry.thumbStart(track, minThumb, min, max, thumb, selected));
        int[] drags = {Integer.MIN_VALUE, -1, 0, drag, track, MAX};
        for (int d : drags)
            eq(4, selection(d, track, minThumb, min, max, thumb),
                    ViewportScrollBarGeometry.selectionAt(d, track, minThumb, min, max, thumb));
        int[] positions = {-1, 0, clamp((long) arrow - 1), arrow, clamp((long) arrow + track - 1),
                clamp((long) arrow + track), clamp(2L * arrow + track - 1), clamp(2L * arrow + track),
                clamp((long) arrow + expectedStart - 1), clamp((long) arrow + expectedStart),
                clamp((long) arrow + expectedStart + expectedLength - 1),
                clamp((long) arrow + expectedStart + expectedLength), MAX};
        for (int p : positions)
            eq(5, hit(p, arrow, track, minThumb, min, max, thumb, selected),
                    ViewportScrollBarGeometry.hit(p, arrow, track, minThumb, min, max, thumb, selected));
    }

    private static void witnesses() {
        eq(2, 100, ViewportScrollBarGeometry.thumbLength(100, 8, 0, MAX, MAX - 1));
        eq(2, 100, ViewportScrollBarGeometry.thumbLength(100, 8, 0, MAX, MAX));
        eq(3, 0, ViewportScrollBarGeometry.thumbStart(100, 8, 0, MAX, MAX - 1, 1));
        eq(4, 0, ViewportScrollBarGeometry.selectionAt(92, 100, 8, 0, MAX, MAX - 1));
        eq(5, 1, ViewportScrollBarGeometry.hit(0, 16, MAX, 8, 0, 100, 10, 0));
        eq(5, 1, ViewportScrollBarGeometry.hit(0, MAX, 100, 8, 0, 100, 10, 0));
        eq(5, 1, ViewportScrollBarGeometry.hit(MAX - 1, MAX, 100, 8, 0, 100, 10, 0));
    }

    private static void arithmetic() {
        int[] edge = {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -100000, -65, -3, -2, -1,
                0, 1, 2, 3, 63, 64, 100000, MAX - 1, MAX};
        for (int a : edge) for (int b : edge) for (int c : edge)
            eq(0, rational(a, b, c), ViewportScrollBarGeometry.mulDiv(a, b, c));
        Random r = new Random(0x4d554c444956L);
        for (int i = 0; i < 100000; i++) {
            int a = r.nextInt(), b = r.nextInt(), c = r.nextInt();
            eq(0, rational(a, b, c), ViewportScrollBarGeometry.mulDiv(a, b, c));
        }
    }

    private static void boundaries() {
        int[] mins = {0, 1, 7, 100000, MAX / 2, MAX - 8, MAX - 1};
        int[] tracks = {0, 1, 7, 8, 9, 31, 100, 100000, MAX / 2, MAX - 1, MAX};
        for (int min : mins) for (int max : new int[] {min + 1, MAX}) {
            int range = max - min;
            for (int thumb : new int[] {1, Math.max(1, range / 2), Math.max(1, range - 1), range})
                for (int selected : new int[] {min, min + (range - thumb) / 2, max - thumb})
                    for (int track : tracks) for (int minimumThumb : new int[] {1, 8, 12, 16, 24, 32})
                        for (int arrow : new int[] {0, 1, 16, MAX / 2, MAX})
                            geometry(track, minimumThumb, min, max, thumb, selected, arrow, track / 2);
        }
    }

    private static void random(boolean extreme) {
        ordinary = !extreme;
        Random r = new Random(extreme ? 0x46554c4c52414e47L : 0x4f5244494e415259L);
        for (int i = 0; i < 100000; i++) {
            int min = extreme ? r.nextInt(MAX) : r.nextInt(1000);
            if (extreme && (i & 7) == 0) min = 0;
            int max = extreme ? MAX - r.nextInt(Math.min(32, MAX - min)) : min + 1 + r.nextInt(100000);
            int range = max - min;
            int thumb = 1 + r.nextInt(range);
            if (extreme && (i & 3) == 0) thumb = range;
            if (extreme && (i & 3) == 1) thumb = Math.max(1, range - 1);
            int selected = min + r.nextInt(range - thumb + 1);
            int track = extreme ? r.nextInt(MAX) : r.nextInt(1000);
            int arrow = extreme ? r.nextInt(MAX) : r.nextInt(40);
            geometry(track, 1 + r.nextInt(32), min, max, thumb, selected, arrow, r.nextInt());
        }
        ordinary = false;
    }

    public static void main(String[] args) {
        if (args.length != 1 || !(args[0].equals("strict") || args[0].equals("baseline")))
            throw new IllegalArgumentException("strict|baseline required");
        strict = args[0].equals("strict");
        witnesses(); arithmetic(); boundaries(); random(false); random(true);
        if (ordinaryFailures != 0) throw new AssertionError("ordinary compatibility failures=" + ordinaryFailures);
        if (!strict && (failures[2] == 0 || failures[5] == 0)) throw new AssertionError("baseline witness lost");
        System.out.println("GEOMETRY_" + (strict ? "PASS" : "BASELINE_REPRODUCED") + " checks=" + checks
                + " ordinaryFailures=" + ordinaryFailures + " oracleDigest=" + Long.toUnsignedString(digest, 16));
        for (int i = 0; i < NAMES.length; i++) System.out.println(NAMES[i] + " mismatches=" + failures[i]);
    }
}

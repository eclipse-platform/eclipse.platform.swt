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
package org.eclipse.swt.tests.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Exact numeric regression against the original four-corner algorithm, not a widget double. */
public final class ViewportAffineBoundsContract {
    private ViewportAffineBoundsContract() { }
    private static final long[] COORDINATES = {Long.MIN_VALUE, Long.MIN_VALUE + 1,
        -(1L << 53) - 1, -(1L << 53), -1, 0, 1, (1L << 53) - 1,
        1L << 53, (1L << 53) + 1, Long.MAX_VALUE - 1, Long.MAX_VALUE};
    private static final long[] EXTENTS = {0, 1, 2, 17, (1L << 53) - 1, 1L << 53, Long.MAX_VALUE};
    private static final double[] NUMBERS = {0.0, -0.0, 1, -1, 0.5, -0.5, 1.25,
        Double.MIN_VALUE, -Double.MIN_VALUE, Double.MIN_NORMAL, Double.MAX_VALUE,
        -Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
        Double.NaN, Double.longBitsToDouble(0x7ff8000000000031L)};
    private static long checks;
    private static long cases;
    private static long trace;

    private record Corner(double x, double y) { }
    private static Corner corner(double[] m, double x, double y) {
        return new Corner(m[0] * x + m[2] * y + m[4], m[1] * x + m[3] * y + m[5]);
    }
    // Frozen original arithmetic: deliberately retain Point-shaped intermediates in the oracle.
    private static double[] original(double[] m, long x, long y, long w, long h) {
        Corner p0 = corner(m, x, y);
        Corner p1 = corner(m, (double)x + w, y);
        Corner p2 = corner(m, x, (double)y + h);
        Corner p3 = corner(m, (double)x + w, (double)y + h);
        double minX = Math.min(Math.min(p0.x, p1.x), Math.min(p2.x, p3.x));
        double minY = Math.min(Math.min(p0.y, p1.y), Math.min(p2.y, p3.y));
        double maxX = Math.max(Math.max(p0.x, p1.x), Math.max(p2.x, p3.x));
        double maxY = Math.max(Math.max(p0.y, p1.y), Math.max(p2.y, p3.y));
        return new double[] {minX, minY, maxX - minX, maxY - minY};
    }
    private static void same(double expected, double actual, String why) {
        ++checks;
        // Non-NaN bits, notably signed zero, must match. NaN payload propagation is not a Java contract.
        long eb = Double.doubleToLongBits(expected), ab = Double.doubleToLongBits(actual);
        if (eb != ab) throw new AssertionError(why + ": " + Long.toHexString(eb) + " != " + Long.toHexString(ab));
        trace = Long.rotateLeft(trace, 7) ^ ab;
    }
    private static void runCase(Constructor<?> ctor, Method map, Method[] getters,
                                double[] m, long x, long y, long w, long h) throws Exception {
        Object transform = ctor.newInstance(m[0], m[1], m[2], m[3], m[4], m[5]);
        Object output = map.invoke(transform, x, y, w, h);
        double[] expected = original(m, x, y, w, h);
        for (int i = 0; i < 4; i++) same(expected[i], ((Double)getters[i].invoke(output)).doubleValue(), "corner " + i);
        cases++;
    }
    private static long mix(long x) {
        x ^= x >>> 30; x *= 0xbf58476d1ce4e5b9L;
        x ^= x >>> 27; x *= 0x94d049bb133111ebL;
        return x ^ (x >>> 31);
    }
    /** The same method runs unchanged on baseline and generated product classpaths. */
    public static void verify() throws Exception {
        checks = 0; cases = 0; trace = 0;
        Class<?> type = Class.forName("org.eclipse.swt.widgets.ViewportAffineTransform");
        Constructor<?> ctor = type.getDeclaredConstructor(double.class, double.class, double.class,
                double.class, double.class, double.class);
        ctor.setAccessible(true);
        Method map = type.getDeclaredMethod("mapBounds", long.class, long.class, long.class, long.class);
        map.setAccessible(true);
        Class<?> bounds = Class.forName("org.eclipse.swt.widgets.ViewportAffineTransform$Bounds");
        Method[] getters = {bounds.getDeclaredMethod("x"), bounds.getDeclaredMethod("y"),
            bounds.getDeclaredMethod("width"), bounds.getDeclaredMethod("height")};
        for (Method getter : getters) getter.setAccessible(true);
        double[][] matrices = {{1,0,0,1,0,0}, {0,1,-1,0,17,-9}, {1.25,-.25,.5,2,-0.0,.5},
            {-1,0,0,-1,0,0}, {0,-0.0,-0.0,0,-0.0,-0.0}};
        for (double[] m : matrices) for (long x : COORDINATES) for (long y : COORDINATES)
            for (long w : EXTENTS) for (long h : EXTENTS) runCase(ctor, map, getters, m, x, y, w, h);
        // Exhaustive pair interaction of all six coefficient positions and exceptional values.
        for (int i = 0; i < 6; i++) for (int j = i; j < 6; j++)
            for (double v : NUMBERS) for (double u : NUMBERS) {
                double[] m = {1,0,0,1,0,0}; m[i] = v; m[j] = u;
                runCase(ctor, map, getters, m, Long.MAX_VALUE, Long.MIN_VALUE, 17, Long.MAX_VALUE);
                runCase(ctor, map, getters, m, 0, 0, 0, 0);
            }
        // Preserve the complete earlier corpus; add translation-only NaN/zero/precision cases.
        for (double tx : NUMBERS) for (double ty : NUMBERS) for (long x : COORDINATES) {
            runCase(ctor, map, getters, new double[] {1,-0.0,-0.0,1,tx,ty},
                    x, -x, Long.MAX_VALUE, Long.MAX_VALUE);
            runCase(ctor, map, getters, new double[] {1,0,0,1,tx,ty}, x, x, 0, 0);
        }
        // Deterministic generated raw-bit inputs; no random sharding or external corpus.
        for (long k = 1; k <= 30000; k++) {
            double[] m = new double[6];
            for (int i = 0; i < 6; i++) m[i] = Double.longBitsToDouble(mix(k * 17 + i));
            runCase(ctor, map, getters, m, mix(k), mix(k + 1), mix(k + 2) & Long.MAX_VALUE,
                    mix(k + 3) & Long.MAX_VALUE);
        }
        Object identity = ctor.newInstance(1d, 0d, 0d, 1d, 0d, 0d);
        for (long[] bad : new long[][] {{-1,0}, {0,-1}, {-1,-1}, {Long.MIN_VALUE,0}}) {
            try { map.invoke(identity, 0L, 0L, bad[0], bad[1]); throw new AssertionError("missing negative bounds refusal"); }
            catch (InvocationTargetException failure) {
                Throwable cause = failure.getCause(); checks++;
                if (!(cause instanceof IllegalArgumentException) || !"negative bounds".equals(cause.getMessage()))
                    throw new AssertionError("changed negative bounds contract", cause);
            }
        }
        System.out.println("AFFINE_ACTUAL_SOURCE_PASS cases=" + cases + " checks=" + checks
                + " trace=" + Long.toHexString(trace));
    }
    public static void main(String[] args) throws Exception { verify(); }
}

// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Headless differential contracts for variable row extents: every query is compared with a
 * naive linear-scan oracle written from the documented semantics. No native widget fixtures.
 */
public class Test_VirtualRowExtents {
	private static final int AUTO = 0, START = 1, CENTER = 2, END = 3;

	/** Reflective handle on the package-private owner. */
	private static final class Extents {
		final Object target;

		Extents(int estimate) {
			try {
				Constructor<?> c = Class.forName("org.eclipse.swt.widgets.VirtualRowExtents").getDeclaredConstructor(int.class);
				c.setAccessible(true);
				target = c.newInstance(estimate);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof RuntimeException r) throw r;
				throw new IllegalStateException(e.getCause());
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}

		Object call(String name, Class<?>[] types, Object... args) {
			try {
				Method m = target.getClass().getDeclaredMethod(name, types);
				m.setAccessible(true);
				return m.invoke(target, args);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof RuntimeException r) throw r;
				throw new IllegalStateException(e.getCause());
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}

		int rows() { return (Integer) call("rowCount", new Class<?>[0]); }
		int measured() { return (Integer) call("measuredCount", new Class<?>[0]); }
		int extentOf(int r) { return (Integer) call("extentOf", new Class<?>[] {int.class}, r); }
		boolean isMeasured(int r) { return (Boolean) call("isMeasured", new Class<?>[] {int.class}, r); }
		void setEstimate(int e) { call("setEstimate", new Class<?>[] {int.class}, e); }
		void reset(int n) { call("reset", new Class<?>[] {int.class}, n); }
		void setExtent(int r, int e) { call("setExtent", new Class<?>[] {int.class, int.class}, r, e); }
		void clearExtent(int r) { call("clearExtent", new Class<?>[] {int.class}, r); }
		void clearFrom(int r) { call("clearFrom", new Class<?>[] {int.class}, r); }
		void insert(int i, int n) { call("insert", new Class<?>[] {int.class, int.class}, i, n); }
		void remove(int i, int n) { call("remove", new Class<?>[] {int.class, int.class}, i, n); }
		long offsetOf(int r) { return (Long) call("offsetOf", new Class<?>[] {int.class}, r); }
		long total() { return (Long) call("totalExtent", new Class<?>[0]); }
		int rowAt(long o) { return (Integer) call("rowAt", new Class<?>[] {long.class}, o); }
		int[] visible(long o, int v) {
			int[] out = new int[2];
			call("visibleRange", new Class<?>[] {long.class, int.class, int[].class}, o, v, out);
			return out;
		}
		long scrollFor(int r, int align, long cur, int v) {
			return (Long) call("scrollOffsetFor", new Class<?>[] {int.class, int.class, long.class, int.class}, r, align, cur, v);
		}
		long anchored(int r, int e, long off, boolean back) {
			return (Long) call("setExtentAnchored", new Class<?>[] {int.class, int.class, long.class, boolean.class}, r, e, off, back);
		}
	}

	/** Naive oracle over plain arrays. */
	private static final class Oracle {
		int estimate;
		int[] measured = new int[0]; // -1 = unmeasured

		Oracle(int estimate) { this.estimate = estimate; }

		int count() { return measured.length; }
		int eff(int r) { return measured[r] < 0 ? estimate : measured[r]; }
		long offset(int r) { long s = 0; for (int i = 0; i < r; i++) s += eff(i); return s; }
		long total() { return offset(count()); }
		int measuredCount() { int n = 0; for (int m : measured) if (m >= 0) n++; return n; }

		int rowAt(long off) {
			if (count() == 0) return -1;
			if (off <= 0) return 0;
			long s = 0;
			for (int i = 0; i < count(); i++) {
				s += eff(i);
				if (off < s) return i;
			}
			return count() - 1;
		}

		void insert(int index, int n) {
			int[] next = new int[count() + n];
			System.arraycopy(measured, 0, next, 0, index);
			Arrays.fill(next, index, index + n, -1);
			System.arraycopy(measured, index, next, index + n, count() - index);
			measured = next;
		}

		void remove(int index, int n) {
			int[] next = new int[count() - n];
			System.arraycopy(measured, 0, next, 0, index);
			System.arraycopy(measured, index + n, next, index, count() - index - n);
			measured = next;
		}

		long clamp(long off, int viewport) { return Math.max(0, Math.min(off, Math.max(0, total() - viewport))); }

		long scrollFor(int r, int align, long cur, int v) {
			long s = offset(r);
			long e = s + eff(r);
			long t;
			switch (align) {
				case START -> t = s;
				case END -> t = e - v;
				case CENTER -> t = s + Math.floorDiv(eff(r) - v, 2);
				default -> t = (s >= cur && e <= cur + v) ? cur : (s < cur ? s : e - v);
			}
			return clamp(t, v);
		}
	}

	private static void same(Extents x, Oracle o, Random rnd, String ctx) {
		assertEquals(o.count(), x.rows(), ctx);
		assertEquals(o.measuredCount(), x.measured(), ctx);
		assertEquals(o.total(), x.total(), ctx);
		int n = o.count();
		int step = Math.max(1, n / 40);
		for (int r = 0; r <= n; r += step) assertEquals(o.offset(r), x.offsetOf(r), ctx + " offsetOf " + r);
		for (int r = 0; r < n; r += step) {
			assertEquals(o.eff(r), x.extentOf(r), ctx + " extentOf " + r);
			assertEquals(o.measured[r] >= 0, x.isMeasured(r), ctx + " isMeasured " + r);
		}
		long total = o.total();
		long[] probes = {-5, 0, 1, total - 1, total, total + 7, rnd.nextInt((int) Math.max(1, total + 5))};
		for (long p : probes) assertEquals(o.rowAt(p), x.rowAt(p), ctx + " rowAt " + p);
		int viewport = rnd.nextInt(60);
		long off = rnd.nextInt((int) Math.max(1, total + 5)) - 3;
		int[] range = x.visible(off, viewport);
		if (n == 0) {
			assertArrayEquals(new int[] {0, 0}, range, ctx);
		} else {
			int first = o.rowAt(off);
			int end = viewport == 0 ? first : o.rowAt(off + viewport - 1) + 1;
			assertArrayEquals(new int[] {first, end}, range, ctx + " visible");
		}
		if (n > 0) {
			int row = rnd.nextInt(n);
			long cur = rnd.nextInt((int) Math.max(1, total)) ;
			for (int align : new int[] {AUTO, START, CENTER, END}) {
				assertEquals(o.scrollFor(row, align, cur, viewport), x.scrollFor(row, align, cur, viewport), ctx + " scroll align " + align);
			}
		}
	}

	private static void apply(Extents x, Oracle o, Random rnd) {
		int n = o.count();
		int pick = rnd.nextInt(100);
		if (pick < 35 && n > 0) {
			int r = rnd.nextInt(n);
			int e = rnd.nextInt(5) == 0 ? 0 : rnd.nextInt(40);
			x.setExtent(r, e);
			o.measured[r] = e;
		} else if (pick < 45 && n > 0) {
			int r = rnd.nextInt(n);
			x.clearExtent(r);
			o.measured[r] = -1;
		} else if (pick < 50) {
			int r = rnd.nextInt(n + 1);
			x.clearFrom(r);
			for (int i = r; i < n; i++) o.measured[i] = -1;
		} else if (pick < 66) {
			int i = rnd.nextInt(n + 1);
			int k = rnd.nextInt(6);
			x.insert(i, k);
			o.insert(i, k);
		} else if (pick < 80 && n > 0) {
			int i = rnd.nextInt(n);
			int k = rnd.nextInt(Math.min(6, n - i) + 1);
			x.remove(i, k);
			o.remove(i, k);
		} else if (pick < 84) {
			int e = 1 + rnd.nextInt(30);
			x.setEstimate(e);
			o.estimate = e;
		} else if (pick < 86) {
			int k = rnd.nextInt(50);
			x.reset(k);
			o.measured = new int[k];
			Arrays.fill(o.measured, -1);
		} else if (n > 0) {
			int r = rnd.nextInt(n);
			int e = rnd.nextInt(40);
			long off = rnd.nextInt((int) Math.max(1, o.total() + 5));
			boolean back = rnd.nextBoolean();
			boolean first = o.measured[r] < 0;
			long start = o.offset(r);
			long end = start + o.eff(r);
			long delta = (long) e - o.eff(r);
			boolean above = first ? start < off : end <= off && !back;
			long expected = (delta != 0 && above) ? delta : 0;
			assertEquals(expected, x.anchored(r, e, off, back), "anchor");
			o.measured[r] = e;
		}
	}

	@Test
	public void randomOperationsAgreeWithLinearScanOracle() {
		for (int seed = 1; seed <= 60; seed++) {
			Random rnd = new Random(seed);
			int estimate = 1 + rnd.nextInt(20);
			Extents x = new Extents(estimate);
			Oracle o = new Oracle(estimate);
			x.reset(rnd.nextInt(40));
			o.measured = new int[x.rows()];
			Arrays.fill(o.measured, -1);
			for (int step = 0; step < 400; step++) {
				apply(x, o, rnd);
				same(x, o, rnd, "seed=" + seed + " step=" + step);
			}
		}
	}

	@Test
	public void invalidArgumentsAreRejectedWithoutChangingState() {
		Extents x = new Extents(10);
		x.reset(5);
		assertThrows(IllegalArgumentException.class, () -> new Extents(0));
		assertThrows(IllegalArgumentException.class, () -> x.setEstimate(0));
		assertThrows(IllegalArgumentException.class, () -> x.reset(-1));
		assertThrows(IllegalArgumentException.class, () -> x.setExtent(5, 1));
		assertThrows(IllegalArgumentException.class, () -> x.setExtent(-1, 1));
		assertThrows(IllegalArgumentException.class, () -> x.setExtent(0, -2));
		assertThrows(IllegalArgumentException.class, () -> x.insert(6, 1));
		assertThrows(IllegalArgumentException.class, () -> x.insert(0, -1));
		assertThrows(IllegalArgumentException.class, () -> x.remove(3, 3));
		assertThrows(IllegalArgumentException.class, () -> x.clearFrom(6));
		assertThrows(IllegalArgumentException.class, () -> x.offsetOf(6));
		assertThrows(IllegalArgumentException.class, () -> x.visible(0, -1));
		assertThrows(IllegalArgumentException.class, () -> x.scrollFor(0, 9, 0, 10));
		assertEquals(5, x.rows());
		assertEquals(50, x.total());
		assertEquals(-1, new Extents(3).rowAt(10));
	}

	@Test
	public void measurementReplacesTheEstimateAndAnchoringFollowsTheDonorRules() {
		Extents x = new Extents(10);
		x.reset(100); // total 1000
		assertEquals(1000, x.total());
		// first measurement of a row that starts above the offset: whole change is returned
		assertEquals(30, x.anchored(2, 40, 500, false));
		assertEquals(1030, x.total());
		// re-measure of a row entirely above the offset, scrolling forward: returned
		assertEquals(-15, x.anchored(2, 25, 500, false));
		// the same re-measure while scrolling backward: not returned, size still recorded
		assertEquals(0, x.anchored(2, 35, 500, true));
		assertEquals(35, x.extentOf(2));
		// a row spanning the offset is not compensated on re-measure
		long start = x.offsetOf(2);
		assertEquals(0, x.anchored(2, 45, start + 5, false));
		// first measurement of a row below the offset: nothing to compensate
		assertEquals(0, x.anchored(50, 70, 100, false));
	}

	@Test
	public void alignmentChoosesTheSmallestMoveForAuto() {
		Extents x = new Extents(10);
		x.reset(100);
		assertEquals(500, x.scrollFor(50, AUTO, 500, 100)); // fully visible: stay
		assertEquals(500, x.scrollFor(50, AUTO, 800, 100)); // above: start
		assertEquals(410, x.scrollFor(50, AUTO, 100, 100)); // below: end
		assertEquals(500, x.scrollFor(50, START, 0, 100));
		assertEquals(410, x.scrollFor(50, END, 0, 100));
		assertEquals(455, x.scrollFor(50, CENTER, 0, 100));
		assertEquals(900, x.scrollFor(99, START, 0, 100)); // clamped to the last full page
		assertEquals(0, x.scrollFor(0, END, 0, 100));
	}

	@Test
	public void largeListStaysConsistentWithBinarySearchOnPrefixSums() {
		int rows = 1_000_000;
		Random rnd = new Random(42);
		Extents x = new Extents(20);
		x.reset(rows);
		int[] eff = new int[rows];
		Arrays.fill(eff, 20);
		for (int i = 0; i < 200_000; i++) {
			int r = rnd.nextInt(rows);
			int e = rnd.nextInt(80);
			x.setExtent(r, e);
			eff[r] = e;
		}
		long[] prefix = new long[rows + 1];
		for (int i = 0; i < rows; i++) prefix[i + 1] = prefix[i] + eff[i];
		assertEquals(prefix[rows], x.total());
		for (int i = 0; i < 2000; i++) {
			int r = rnd.nextInt(rows + 1);
			assertEquals(prefix[r], x.offsetOf(r));
			long off = (long) (rnd.nextDouble() * prefix[rows]);
			int expected = Arrays.binarySearch(prefix, off);
			if (expected < 0) expected = -expected - 2;
			while (expected + 1 < rows && prefix[expected + 1] <= off) expected++;
			assertEquals(Math.min(expected, rows - 1), x.rowAt(off));
		}
		x.remove(0, rows / 2);
		assertEquals(rows / 2, x.rows());
		assertEquals(prefix[rows] - prefix[rows / 2], x.total());
	}
}

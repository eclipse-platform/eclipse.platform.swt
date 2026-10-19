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
import java.util.Arrays;
import java.util.SplittableRandom;

/** Measures four bit-lane layouts for ItemResidency over N items and L lanes. */
public final class LaneLayoutBench {
	static final int N = 10_000_000, L = 20, SEL = 5, CHK = 10, WINDOWS = 20_000, ROWS = 64;
	static long sink;

	public static void main(String[] a) {
		int[] starts = new SplittableRandom(7).ints(WINDOWS, 0, N - ROWS).toArray();
		int[] probes = new SplittableRandom(9).ints(1_000_000, 0, N).toArray();
		System.out.println("layout\top\tmedian_ms\tbytes");
		String order = a.length > 0 ? a[0] : "ABCD";
		for (char c : order.toCharArray()) {
			switch (c) {
				case 'A' -> run("A_lane_long", new A(), starts, probes);
				case 'B' -> run("B_lane_int", new B(), starts, probes);
				case 'C' -> run("C_interleaved_int", new C(), starts, probes);
				default -> run("D_item_int", new D(), starts, probes);
			}
		}
		System.out.println("sink\t" + sink);
	}

	interface Lay { void setRange(int lane, int a, int b, boolean on); int count(int lane, int a, int b);
		int gather(int item); void set(int lane, int item); boolean get(int lane, int item); long bytes(); }

	static void run(String name, Lay l, int[] starts, int[] probes) {
		op(name, "range_set_clear_8M", l, () -> { l.setRange(SEL, 1_000_000, 9_000_000, true); l.setRange(SEL, 1_000_000, 9_000_000, false); });
		for (int i = 0; i < N; i += 3) l.set(CHK, i);
		op(name, "range_count_10M", l, () -> sink += l.count(CHK, 0, N));
		op(name, "viewport_gather_20k_x64", l, () -> { long s = 0; for (int w : starts) for (int r = 0; r < ROWS; r++) s += l.gather(w + r); sink += s; });
		op(name, "random_set_get_1M", l, () -> { int s = 0; for (int p : probes) { l.set(7, p); if (l.get(7, p)) s++; } sink += s; });
	}

	static void op(String name, String op, Lay l, Runnable r) {
		long[] t = new long[7];
		for (int i = 0; i < 3; i++) r.run();
		for (int i = 0; i < t.length; i++) { long s = System.nanoTime(); r.run(); t[i] = System.nanoTime() - s; }
		Arrays.sort(t);
		System.out.printf("%s\t%s\t%.3f\t%d%n", name, op, t[t.length / 2] / 1e6, l.bytes());
	}

	static final class A implements Lay {
		final long[][] w = new long[L][(N + 63) >>> 6];
		public void setRange(int lane, int a, int b, boolean on) { long[] x = w[lane]; int fw = a >>> 6, lw = (b - 1) >>> 6;
			long fm = -1L << a, lm = -1L >>> -b; if (fw == lw) { m(x, fw, fm & lm, on); return; }
			m(x, fw, fm, on); Arrays.fill(x, fw + 1, lw, on ? -1L : 0L); m(x, lw, lm, on); }
		static void m(long[] x, int i, long mask, boolean on) { x[i] = on ? x[i] | mask : x[i] & ~mask; }
		public int count(int lane, int a, int b) { int c = 0; long[] x = w[lane]; for (int i = a >>> 6; i <= (b - 1) >>> 6; i++) c += Long.bitCount(x[i]); return c; }
		public int gather(int item) { int f = 0, wi = item >>> 6; for (int l = 0; l < L; l++) f |= (int) ((w[l][wi] >>> item) & 1L) << l; return f; }
		public void set(int lane, int item) { w[lane][item >>> 6] |= 1L << item; }
		public boolean get(int lane, int item) { return (w[lane][item >>> 6] & (1L << item)) != 0; }
		public long bytes() { return (long) L * w[0].length * 8; }
	}

	static final class B implements Lay {
		final int[][] w = new int[L][(N + 31) >>> 5];
		public void setRange(int lane, int a, int b, boolean on) { int[] x = w[lane]; int fw = a >>> 5, lw = (b - 1) >>> 5;
			int fm = -1 << a, lm = -1 >>> -b; if (fw == lw) { m(x, fw, fm & lm, on); return; }
			m(x, fw, fm, on); Arrays.fill(x, fw + 1, lw, on ? -1 : 0); m(x, lw, lm, on); }
		static void m(int[] x, int i, int mask, boolean on) { x[i] = on ? x[i] | mask : x[i] & ~mask; }
		public int count(int lane, int a, int b) { int c = 0; int[] x = w[lane]; for (int i = a >>> 5; i <= (b - 1) >>> 5; i++) c += Integer.bitCount(x[i]); return c; }
		public int gather(int item) { int f = 0, wi = item >>> 5; for (int l = 0; l < L; l++) f |= ((w[l][wi] >>> item) & 1) << l; return f; }
		public void set(int lane, int item) { w[lane][item >>> 5] |= 1 << item; }
		public boolean get(int lane, int item) { return (w[lane][item >>> 5] & (1 << item)) != 0; }
		public long bytes() { return (long) L * w[0].length * 4; }
	}

	static final class C implements Lay {
		final int[] w = new int[((N + 31) >>> 5) * L];
		public void setRange(int lane, int a, int b, boolean on) { int fw = a >>> 5, lw = (b - 1) >>> 5;
			int fm = -1 << a, lm = -1 >>> -b; if (fw == lw) { m(fw * L + lane, fm & lm, on); return; }
			m(fw * L + lane, fm, on); int v = on ? -1 : 0; for (int i = fw + 1; i < lw; i++) w[i * L + lane] = v; m(lw * L + lane, lm, on); }
		void m(int i, int mask, boolean on) { w[i] = on ? w[i] | mask : w[i] & ~mask; }
		public int count(int lane, int a, int b) { int c = 0; for (int i = a >>> 5; i <= (b - 1) >>> 5; i++) c += Integer.bitCount(w[i * L + lane]); return c; }
		public int gather(int item) { int f = 0, base = (item >>> 5) * L; for (int l = 0; l < L; l++) f |= ((w[base + l] >>> item) & 1) << l; return f; }
		public void set(int lane, int item) { w[(item >>> 5) * L + lane] |= 1 << item; }
		public boolean get(int lane, int item) { return (w[(item >>> 5) * L + lane] & (1 << item)) != 0; }
		public long bytes() { return (long) w.length * 4; }
	}

	static final class D implements Lay {
		final int[] w = new int[N];
		public void setRange(int lane, int a, int b, boolean on) { int m = 1 << lane; if (on) for (int i = a; i < b; i++) w[i] |= m; else for (int i = a; i < b; i++) w[i] &= ~m; }
		public int count(int lane, int a, int b) { int c = 0; for (int i = a; i < b; i++) c += (w[i] >>> lane) & 1; return c; }
		public int gather(int item) { return w[item]; }
		public void set(int lane, int item) { w[item] |= 1 << lane; }
		public boolean get(int lane, int item) { return (w[item] & (1 << lane)) != 0; }
		public long bytes() { return (long) w.length * 4; }
	}
}

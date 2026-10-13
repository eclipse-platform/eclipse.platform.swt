// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import java.lang.management.*;
import java.util.*;

import org.eclipse.swt.internal.residency.*;

/**
 * Bounded-heap stress of the item-residency engine (run with -Xmx256m, one JVM per scale):
 * {@code flat N} scrolls a flat list of N items (select-all, check-all, every-3rd pattern, a
 * 200,000-row scroll region with every 10th item caller-owned, scroll-back) and {@code tree}
 * drives a 1,001,001,000-node tree (fan-out 1000/1000/1000). Prints one TSV row of measurements.
 * Swap goes through the ported TieredSwapCache to the Java FileSwapStore.
 */
public final class ItemResidencyStress {
	static final int COLUMNS = 2, REGION = 200_000, VISIBLE = 50, OVERSCAN = 25;
	static long peakHeap;
	static int [] latency = new int [1 << 16];
	static int latencies;

	/** Spill decorator timing every swap-in read (microseconds). */
	static final class Timed implements ItemResidency.Spill {
		final ItemResidency.Spill delegate;

		Timed (ItemResidency.Spill delegate) {
			this.delegate = delegate;
		}

		@Override
		public int write (byte [] data, int length) {
			return delegate.write (data, length);
		}

		@Override
		public byte [] read (int record) {
			long t = System.nanoTime ();
			byte [] b = delegate.read (record);
			if (latencies == latency.length) latency = Arrays.copyOf (latency, latency.length * 2);
			latency [latencies++] = (int) ((System.nanoTime () - t) / 1000);
			return b;
		}

		@Override
		public void free (int record) {
			delegate.free (record);
		}

		@Override
		public void close () {
			delegate.close ();
		}
	}

	/** Deterministic synchronous source; counts requests for items in the scroll region. */
	static final class Source implements ItemResidency.DataSource {
		final int regionStart;
		final int [] regionRequests = new int [REGION];
		long requests;

		Source (int regionStart) {
			this.regionStart = regionStart;
		}

		@Override
		public void request (ItemResidency e, int from, int to) {
			for (int item = from; item < to; item++) {
				requests++;
				int r = item - regionStart;
				if (r >= 0 && r < REGION) regionRequests [r]++;
				e.beginData (item);
				e.setText (item, 0, "row " + item);
				e.setText (item, 1, "cell " + (item ^ 0x5A5A));
				e.setInt (item, ItemResidency.I_FOREGROUND, 0xFF000000 | (item * 2654435 & 0xFFFFFF));
				e.setInt (item, ItemResidency.I_ROW_HEIGHT, 18 + (item & 3));
				e.endData (item);
			}
		}
	}

	public static void main (String [] args) throws Exception {
		String kind = args [0];
		long t0 = System.nanoTime ();
		String row = kind.equals ("tree") ? tree () : flat (Integer.parseInt (args [1]));
		System.out.println (row + "\t" + (System.nanoTime () - t0) / 1_000_000);
	}

	static ItemResidency engine (int count, ItemResidency.DataSource source, ItemSwapSpill spill) {
		int flags = ItemResidency.RESIDENCY_SWAP | ItemResidency.RESIDENCY_DESTROY | ItemResidency.PRESSURE_SAMPLED;
		ItemResidency e = new ItemResidency (flags, COLUMNS, 20_000, source, new Timed (spill), ItemSwapPressure.system (spill));
		e.setSampleEvery (4096);
		e.setCount (count);
		e.setPrefetch (VISIBLE);
		return e;
	}

	static String flat (int n) throws Exception {
		int regionStart = n / 2 - REGION / 2 < 0 ? 0 : n / 2 - REGION / 2;
		int region = Math.min (REGION, n - regionStart);
		Source source = new Source (regionStart);
		ItemSwapSpill spill = ItemSwapSpill.temporary (8L << 20);
		ItemResidency e = engine (n, source, spill);
		e.setRange (ItemResidency.LANE_SELECTED, 0, n, true);
		e.setRange (ItemResidency.LANE_CHECKED, 0, n, true);
		e.setPattern (ItemResidency.LANE_GRAYED, 0, n, 3, 0b001);
		boolean [] edited = new boolean [region];
		e.setViewport (0, VISIBLE, OVERSCAN);
		e.setViewport (n / 2, VISIBLE, OVERSCAN);
		e.setViewport (n - VISIBLE, VISIBLE, OVERSCAN);
		sample ();
		for (int first = regionStart; first < regionStart + region - VISIBLE; first += VISIBLE) {
			e.setViewport (first, VISIBLE, OVERSCAN);
			for (int item = first; item < first + VISIBLE; item++) {
				if ((item - regionStart) % 10 == 0) {
					e.setText (item, 1, "edited " + item);
					edited [item - regionStart] = true;
				}
			}
			if ((first & 0xFFFF) < VISIBLE) sample ();
		}
		String [] before = snapshot (e, regionStart, region);
		e.setViewport (0, VISIBLE, OVERSCAN);
		e.applyPressure (0);
		sample ();
		int coldBefore = countTier (e, regionStart, region, ItemResidency.TIER_COLD);
		int [] requestsBefore = source.regionRequests.clone ();
		latencies = 0;
		e.applyPressure (1000);
		for (int first = regionStart + region - VISIBLE; first >= regionStart; first -= VISIBLE) {
			e.setViewport (first, VISIBLE, OVERSCAN);
		}
		int refires = 0;
		for (int r = 0; r < region; r++) if (edited [r]) refires += source.regionRequests [r] - requestsBefore [r];
		String [] after = snapshot (e, regionStart, region);
		boolean repro = Arrays.equals (before, after);
		int selected = e.countRange (ItemResidency.LANE_SELECTED, 0, n), grayed = e.countRange (ItemResidency.LANE_GRAYED, 0, n);
		return measure ("flat", n, e, spill, coldBefore, refires, repro, selected, grayed, source.requests);
	}

	static String tree () throws Exception {
		ItemTreeShape shape = new ItemTreeShape (new int [] {1000, 1000, 1000});
		int keys = shape.keyCount ();
		Source source = new Source (Integer.MAX_VALUE);
		ItemSwapSpill spill = ItemSwapSpill.temporary (8L << 20);
		ItemResidency e = engine (keys, source, spill);
		shape.expandToDepth (2);
		int rows = shape.rowCount ();
		int [] runs = new int [2 * (VISIBLE + 2 * OVERSCAN)];
		int middle = rows / 2;
		viewport (e, shape, runs, middle);
		for (int r = 0; r < VISIBLE; r += 5) e.setText (shape.key (middle + r), 1, "edited " + r);
		String [] before = snapshot (e, shape, middle);
		viewport (e, shape, runs, rows - VISIBLE);
		e.applyPressure (0);
		e.applyPressure (1000);
		viewport (e, shape, runs, middle);
		boolean repro = Arrays.equals (before, snapshot (e, shape, middle));
		e.setRange (ItemResidency.LANE_SELECTED, 0, keys, true);
		e.setPattern (ItemResidency.LANE_CHECKED, 0, keys, 3, 0b001);
		int selected = e.countRange (ItemResidency.LANE_SELECTED, 0, keys);
		int checked = e.countRange (ItemResidency.LANE_CHECKED, 0, keys);
		int middleKey = shape.key (middle), collapse = shape.parent (middleKey) < 0 ? middleKey : shape.parent (middleKey);
		shape.setExpanded (collapse, false);
		int collapsedRows = shape.rowCount ();
		shape.setExpanded (collapse, true);
		shape.expandToDepth (0);
		shape.setExpanded (500, true);
		shape.setExpanded (shape.childKey (500, 500), true);
		int pathRows = shape.rowCount ();
		viewport (e, shape, runs, pathRows - VISIBLE);
		sample ();
		String extra = rows + "\t" + collapsedRows + "\t" + pathRows + "\t" + shape.pathNodes () + "\t" + checked;
		return measure ("tree", keys, e, spill, 0, 0, repro, selected, checked, source.requests) + "\t" + extra;
	}

	static void viewport (ItemResidency e, ItemTreeShape shape, int [] runs, int first) {
		int start = Math.max (0, first - OVERSCAN);
		int count = shape.keyRuns (start, VISIBLE + 2 * OVERSCAN, runs);
		e.setViewportRuns (runs, count, count);
		sample ();
	}

	static String [] snapshot (ItemResidency e, int start, int length) {
		String [] out = new String [length / 997 + 1];
		for (int i = 0, item = start; item < start + length; i++, item += 997) out [i] = describe (e, item);
		return out;
	}

	static String [] snapshot (ItemResidency e, ItemTreeShape shape, int first) {
		String [] out = new String [VISIBLE];
		for (int r = 0; r < VISIBLE; r++) out [r] = describe (e, shape.key (first + r));
		return out;
	}

	static String describe (ItemResidency e, int item) {
		return item + "|" + Integer.toHexString (e.gather (item) & 0xFFFF) + "|" + e.getText (item, 0) + "|" + e.getText (item, 1)
				+ "|" + e.getInt (item, ItemResidency.I_FOREGROUND) + "|" + e.getInt (item, ItemResidency.I_ROW_HEIGHT);
	}

	static int countTier (ItemResidency e, int start, int length, int tier) {
		int c = 0;
		for (int item = start; item < start + length; item++) if (e.tier (item) == tier) c++;
		return c;
	}

	static void sample () {
		long used = ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ().getUsed ();
		peakHeap = Math.max (peakHeap, used);
	}

	static String measure (String kind, int n, ItemResidency e, ItemSwapSpill spill, int cold, int refires, boolean repro,
			int selected, int pattern, long requests) throws Exception {
		System.gc ();
		Thread.sleep (200);
		long heap = ManagementFactory.getMemoryMXBean ().getHeapMemoryUsage ().getUsed ();
		int [] l = Arrays.copyOf (latency, latencies);
		Arrays.sort (l);
		String p50 = l.length == 0 ? "-" : String.valueOf (l [l.length / 2]);
		String p95 = l.length == 0 ? "-" : String.valueOf (l [(int) (l.length * 0.95)]);
		String p99 = l.length == 0 ? "-" : String.valueOf (l [(int) (l.length * 0.99)]);
		long [] rss = rss ();
		return String.join ("\t", kind, String.valueOf (n), String.valueOf (heap), String.valueOf (peakHeap),
				String.valueOf (rss [0]), String.valueOf (rss [1]), String.valueOf (e.stat (ItemResidency.STAT_LANE_BYTES)),
				String.valueOf (e.stat (ItemResidency.STAT_DIRECTORY_BYTES)), String.valueOf (e.stat (ItemResidency.STAT_SLOT_BYTES)),
				String.valueOf (e.stat (ItemResidency.STAT_SWAP_OUT)), String.valueOf (e.stat (ItemResidency.STAT_SWAP_OUT_WRITES)),
				String.valueOf (e.stat (ItemResidency.STAT_SWAP_IN)), String.valueOf (e.stat (ItemResidency.STAT_DESTROYED)),
				String.valueOf (e.stat (ItemResidency.STAT_RECREATED)), String.valueOf (e.stat (ItemResidency.STAT_MATERIALIZED)),
				String.valueOf (requests), String.valueOf (spill.fileBytes ()), String.valueOf (latencies), p50, p95, p99,
				String.valueOf (cold), String.valueOf (refires), String.valueOf (repro), String.valueOf (selected), String.valueOf (pattern),
				String.valueOf (e.stat (ItemResidency.STAT_PRESSURE_SAMPLES)), String.valueOf (e.stat (ItemResidency.STAT_MATERIALIZED_DURING_PAINT)));
	}

	/** Working set and peak working set of this process (Windows), via PowerShell. */
	static long [] rss () {
		try {
			long pid = ProcessHandle.current ().pid ();
			Process p = new ProcessBuilder ("powershell", "-NoProfile", "-Command",
					"$p=Get-Process -Id " + pid + "; [string]$p.WorkingSet64 + ' ' + [string]$p.PeakWorkingSet64").redirectErrorStream (true).start ();
			String out = new String (p.getInputStream ().readAllBytes ()).trim ();
			p.waitFor ();
			String [] parts = out.split ("\\s+");
			return new long [] {Long.parseLong (parts [0]), Long.parseLong (parts [1])};
		} catch (Exception e) {
			return new long [] {-1, -1};
		}
	}
}

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

import static org.eclipse.swt.tests.junit.ItemResidencyFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.internal.PackedHeapGrowth;
import org.junit.jupiter.api.Test;

/**
 * Differential contracts of the sparse state layer: Roaring-style containers against
 * {@link BitSet}, pattern rules with exception precedence, the lazy tree shape against a naive
 * materialized tree (1,010,100 nodes), and Xmx-aware growth under simulated low headroom.
 */
public class Test_ItemResidencyLanes {

	@Test
	public void bitSetMatchesJavaBitSetUnderRandomOperations () {
		Object set = newInstance ("ItemBitSet");
		BitSet ref = new BitSet ();
		Random random = new Random (3);
		int limit = 600_000;
		for (int op = 0; op < 20_000; op++) {
			int a = random.nextInt (limit), kind = random.nextInt (10);
			if (kind < 5) {
				boolean on = random.nextBoolean ();
				call (set, "set", a, on);
				ref.set (a, on);
			} else {
				int b = Math.min (limit, a + random.nextInt (kind < 8 ? 300 : 150_000));
				boolean on = random.nextBoolean ();
				call (set, "setRange", a, b, on);
				ref.set (a, b, on);
			}
			if (op % 2000 == 0) assertSame (set, ref, limit);
		}
		assertSame (set, ref, limit);
		int [][] scratch = {new int [4]};
		call (set, "shift", 1000, 1, limit + 1, scratch);
		BitSet shifted = new BitSet ();
		for (int i = ref.nextSetBit (0); i >= 0; i = ref.nextSetBit (i + 1)) shifted.set (i < 1000 ? i : i + 1);
		assertSame (set, shifted, limit + 1);
	}

	private static void assertSame (Object set, BitSet ref, int limit) {
		assertEquals (ref.cardinality (), i (set, "count", 0, limit));
		for (int i = ref.nextSetBit (0), j = i (set, "next", 0, limit); i >= 0 || j >= 0;
				i = ref.nextSetBit (i + 1), j = i (set, "next", j + 1, limit)) {
			assertEquals (i, j, "next");
		}
		Random random = new Random (limit);
		for (int probe = 0; probe < 2000; probe++) {
			int a = random.nextInt (limit), b = Math.min (limit, a + random.nextInt (70_000));
			assertEquals (ref.get (a), b (set, "get", a));
			assertEquals (ref.get (a, b).cardinality (), i (set, "count", a, b));
		}
	}

	@Test
	public void patternsAreRulesWithExceptionPrecedenceAndExactCounts () {
		Object e = engine (0, 1, 64, null, null, null);
		int n = 1_000_000_000;
		call (e, "setCount", n);
		int selected = k ("LANE_SELECTED"), checked = k ("LANE_CHECKED");
		Object lanes = field (e, "lanes");
		int before = i (lanes, "bytes");
		call (e, "setPattern", selected, 0, n, 3, 0b001);
		assertTrue (i (lanes, "bytes") - before < 512, "every 3rd row over 1B items is O(1) memory");
		assertEquals (333_333_334, i (e, "countRange", selected, 0, n));
		assertTrue (b (e, "bit", selected, 999_999_999));
		assertFalse (b (e, "bit", selected, 999_999_998));
		call (e, "setBit", selected, 3, false);
		call (e, "setBit", selected, 4, true);
		assertFalse (b (e, "bit", selected, 3), "exception wins over pattern");
		assertTrue (b (e, "bit", selected, 4));
		assertEquals (333_333_334, i (e, "countRange", selected, 0, n));
		assertEquals (0, i (e, "nextSet", selected, 0));
		assertEquals (4, i (e, "nextSet", selected, 1));
		assertEquals (6, i (e, "nextSet", selected, 5));
		call (e, "setRange", selected, 10, 20, true);
		assertEquals (10, i (e, "countRange", selected, 10, 20), "explicit range cuts the pattern");
		assertEquals (1, i (e, "countRange", selected, 20, 24), "pattern resumes with its phase after the cut: only 21");
		call (e, "setPattern", checked, 500, 600, 4, 0b0101);
		assertEquals (50, i (e, "countRange", checked, 0, n));
		call (e, "insert", 501);
		assertTrue (b (e, "bit", checked, 500));
		assertFalse (b (e, "bit", checked, 501), "inserted item is default");
		assertTrue (b (e, "bit", checked, 503), "pattern phase follows the shifted items");
		assertEquals (50, i (e, "countRange", checked, 0, n));
		call (e, "remove", 500);
		assertFalse (b (e, "bit", checked, 500));
		assertTrue (b (e, "bit", checked, 502));
		assertEquals (49, i (e, "countRange", checked, 0, n));
	}

	@Test
	public void patternLanesMatchAReferenceModel () {
		int n = 50_000;
		Object e = engine (0, 1, 64, null, null, null);
		call (e, "setCount", n);
		int lane = k ("LANE_SELECTED");
		boolean [] ref = new boolean [n];
		Random random = new Random (8);
		for (int op = 0; op < 600; op++) {
			int a = random.nextInt (n), b = Math.min (n, a + random.nextInt (5000));
			switch (random.nextInt (4)) {
				case 0 -> {
					int stride = 1 + random.nextInt (32), mask = random.nextInt ();
					call (e, "setPattern", lane, a, b, stride, mask);
					for (int i = a; i < b; i++) ref [i] = (mask >>> ((i - a) % stride) & 1) != 0;
				}
				case 1 -> {
					boolean on = random.nextBoolean ();
					call (e, "setRange", lane, a, b, on);
					Arrays.fill (ref, a, b, on);
				}
				default -> {
					boolean on = random.nextBoolean ();
					call (e, "setBit", lane, a, on);
					ref [a] = on;
				}
			}
		}
		int total = 0;
		for (int i = 0; i < n; i++) {
			assertEquals (ref [i], b (e, "bit", lane, i), "item " + i);
			if (ref [i]) total++;
		}
		assertEquals (total, i (e, "countRange", lane, 0, n));
		int expected = -1;
		for (int i = 0; i < n; i++) {
			if (ref [i]) {
				expected = i;
				break;
			}
		}
		assertEquals (expected, i (e, "nextSet", lane, 0));
	}

	@Test
	public void treeShapeMatchesNaiveTreeOnOneMillionNodes () {
		int [] fanout = {100, 100, 100};
		Object shape = newInstance ("ItemTreeShape", (Object) fanout);
		Naive naive = new Naive (fanout);
		Random random = new Random (21);
		check (shape, naive);
		call (shape, "expandToDepth", 1);
		naive.depthRule (1);
		check (shape, naive);
		for (int op = 0; op < 60; op++) {
			int key = random.nextInt (naive.keys);
			if (naive.children (key) == 0) continue;
			boolean expanded = random.nextBoolean ();
			call (shape, "setExpanded", key, expanded);
			naive.expanded [key] = expanded;
			if (op % 15 == 0) check (shape, naive);
		}
		check (shape, naive);
		call (shape, "expandToDepth", 2);
		naive.depthRule (2);
		check (shape, naive);
		assertEquals (0, i (shape, "pathNodes"), "depth rule is O(1)");
	}

	private static void check (Object shape, Naive naive) {
		int [] rows = naive.rows ();
		assertEquals (rows.length, i (shape, "rowCount"));
		for (int r = 0; r < rows.length; r += rows.length > 200_000 ? 7 : 1) {
			assertEquals (rows [r], i (shape, "key", r), "row " + r);
		}
		int [] rowOf = new int [naive.keys];
		Arrays.fill (rowOf, -1);
		for (int r = 0; r < rows.length; r++) rowOf [rows [r]] = r;
		for (int key = 0; key < naive.keys; key += 13) {
			assertEquals (rowOf [key], i (shape, "row", key), "key " + key);
			assertEquals (naive.children (key) > 0 && naive.expanded [key], b (shape, "isExpanded", key), "expanded " + key);
		}
	}

	/** Materialized reference: boolean per key, DFS for visible rows. */
	static final class Naive {
		final int [] fanout, start;
		final int keys;
		final boolean [] expanded;

		Naive (int [] fanout) {
			this.fanout = fanout;
			start = new int [fanout.length];
			int s = 0, n = 1;
			for (int d = 0; d < fanout.length; d++) {
				n *= fanout [d];
				start [d] = s;
				s += n;
			}
			keys = s;
			expanded = new boolean [keys];
		}

		int depth (int key) {
			int d = fanout.length - 1;
			while (d > 0 && start [d] > key) d--;
			return d;
		}

		int children (int key) {
			int d = depth (key);
			return d + 1 < fanout.length ? fanout [d + 1] : 0;
		}

		int child (int key, int c) {
			int d = depth (key) + 1;
			return start [d] + (key - start [d - 1]) * fanout [d] + c;
		}

		void depthRule (int depth) {
			for (int key = 0; key < keys; key++) expanded [key] = depth (key) < depth && children (key) > 0;
		}

		int [] rows () {
			int [] out = new int [keys];
			int [] n = {0};
			for (int r = 0; r < fanout [0]; r++) visit (r, out, n);
			return Arrays.copyOf (out, n [0]);
		}

		private void visit (int key, int [] out, int [] n) {
			out [n [0]++] = key;
			if (!expanded [key]) return;
			for (int c = 0; c < children (key); c++) visit (child (key, c), out, n);
		}
	}

	@Test
	public void growthUsesExactLengthUnderLowHeadroom () {
		assertEquals (15, PackedHeapGrowth.choose (10, 11, 4, 1L << 30, 1L << 20), "healthy heap grows geometrically");
		assertEquals (11, PackedHeapGrowth.choose (10, 11, 4, 1L << 30, (1L << 30) - (1L << 20)), "low headroom grows exactly");
		PackedHeapGrowth.sampler (new PackedHeapGrowth.Sampler () {
			@Override
			public long maxBytes () {
				return 256L << 20;
			}

			@Override
			public long usedBytes () {
				return (256L << 20) - (2L << 20);
			}
		});
		try {
			Object e = engine (0, 1, 1 << 20, null, null, null);
			call (e, "setCount", 100);
			int exactBefore = PackedHeapGrowth.exactChoices ();
			for (int item = 0; item < 40; item++) call (e, "setText", item, 0, "t" + item);
			Object slots = field (e, "slots");
			assertEquals (40, (int) (Integer) field (slots, "capacity") >= 40 ? 40 : -1);
			assertTrue (PackedHeapGrowth.exactChoices () > exactBefore, "engine growth went through the helper");
			assertTrue ((Integer) field (slots, "capacity") <= 41, "no geometric slack under low headroom");
		} finally {
			PackedHeapGrowth.sampler (null);
		}
	}
}

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

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3RowExtents (enhanced M3 API, leaf 16). Headless. The oracle is a plain list of
 * extents (null for unmeasured rows) with naive prefix sums.
 */
public class Test_M3RowExtents {

	/* the oracle */
	static final class Naive {
		final ArrayList<Integer> rows = new ArrayList<> ();
		int estimate;

		Naive (int count, int estimate) {
			this.estimate = estimate;
			for (int i = 0; i < count; i++) rows.add (null);
		}

		int extent (int row) {
			Integer value = rows.get (row);
			return value == null ? estimate : value;
		}

		long offset (int row) {
			long sum = 0;
			for (int i = 0; i < row; i++) sum += extent (i);
			return sum;
		}
	}

	private static void assertAgrees (Naive naive, M3RowExtents extents, String what) {
		assertEquals (naive.rows.size (), extents.rowCount (), what);
		for (int row = 0; row <= naive.rows.size (); row++) assertEquals (naive.offset (row), extents.offsetOf (row), what + " offset " + row);
		for (int row = 0; row < naive.rows.size (); row++) {
			assertEquals (naive.extent (row), extents.extentOf (row), what + " extent " + row);
			assertEquals (naive.rows.get (row) != null, extents.isMeasured (row), what + " measured " + row);
			/* rowAt answers row 0 for offset 0 even when leading rows have no extent */
			if (naive.extent (row) > 0 && (row == 0 || naive.offset (row) > 0)) assertEquals (row, extents.rowAt (naive.offset (row)), what + " rowAt " + row);
		}
		assertEquals (naive.offset (naive.rows.size ()), extents.totalExtent ());
	}

	@Test
	public void test_randomEditsAgreeWithNaivePrefixSums () {
		Random random = new Random (13);
		M3RowExtents extents = new M3RowExtents (40, 20);
		Naive naive = new Naive (40, 20);
		for (int step = 0; step < 600; step++) {
			int count = naive.rows.size ();
			switch (random.nextInt (7)) {
				case 0, 1, 2 -> {
					if (count == 0) break;
					int row = random.nextInt (count), extent = random.nextInt (60);
					extents.setExtent (row, extent);
					naive.rows.set (row, extent);
				}
				case 3 -> {
					if (count == 0) break;
					int row = random.nextInt (count);
					extents.clearExtent (row);
					naive.rows.set (row, null);
				}
				case 4 -> {
					int index = random.nextInt (count + 1), rows = random.nextInt (4);
					extents.insert (index, rows);
					for (int i = 0; i < rows; i++) naive.rows.add (index, null);
				}
				case 5 -> {
					if (count == 0) break;
					int index = random.nextInt (count), rows = random.nextInt (Math.min (3, count - index) + 1);
					extents.remove (index, rows);
					for (int i = 0; i < rows; i++) naive.rows.remove (index);
				}
				default -> {
					int estimate = 1 + random.nextInt (30);
					extents.setEstimate (estimate);
					naive.estimate = estimate;
				}
			}
			if (step % 25 == 0) assertAgrees (naive, extents, "step " + step);
		}
		assertAgrees (naive, extents, "end");
		int from = Math.min (5, naive.rows.size ());
		extents.clearFrom (from);
		for (int i = from; i < naive.rows.size (); i++) naive.rows.set (i, null);
		assertAgrees (naive, extents, "clearFrom");
		extents.reset (7);
		assertAgrees (new Naive (7, naive.estimate), extents, "reset");
		assertEquals (0, extents.measuredCount ());
	}

	@Test
	public void test_visibleRangeIsExact () {
		M3RowExtents extents = new M3RowExtents (100, 10);
		for (int row = 0; row < 100; row += 3) extents.setExtent (row, 25);
		for (long offset = 0; offset < extents.totalExtent (); offset += 17) {
			for (int viewport : new int [] {1, 50, 333}) {
				M3ItemRange range = extents.visibleRange (offset, viewport);
				for (int row = 0; row < 100; row++) {
					long start = extents.offsetOf (row), end = start + extents.extentOf (row);
					boolean intersects = end > offset && start < offset + viewport;
					assertEquals (intersects, range.contains (row), "row " + row + " at " + offset + "+" + viewport);
				}
			}
		}
		assertTrue (new M3RowExtents (0, 10).visibleRange (0, 100).isEmpty ());
		assertEquals (-1, new M3RowExtents (0, 10).rowAt (5));
	}

	@Test
	public void test_anchoringKeepsTheContentInView () {
		M3RowExtents extents = new M3RowExtents (100, 10);
		long offset = extents.offsetOf (50);
		/* first measurement above the offset: move by the whole change */
		assertEquals (15, extents.setExtentAnchored (10, 25, offset, false));
		offset += 15;
		assertEquals (extents.offsetOf (50), offset, "row 50 stays at the top");
		/* below the offset: nothing moves */
		assertEquals (0, extents.setExtentAnchored (80, 40, offset, false));
		/* re-measure above while scrolling backward: the user's motion wins */
		assertEquals (0, extents.setExtentAnchored (10, 5, offset, true));
		/* re-measure above, not scrolling backward: move by the change */
		assertEquals (25, extents.setExtentAnchored (10, 30, offset, false));
	}

	@Test
	public void test_scrollOffsets () {
		M3RowExtents extents = new M3RowExtents (100, 10);
		extents.setExtent (40, 100);
		assertEquals (400, extents.scrollOffsetFor (40, M3RowExtents.ALIGN_START, 0, 50));
		assertEquals (500 - 50, extents.scrollOffsetFor (40, M3RowExtents.ALIGN_END, 0, 50));
		assertEquals (400 + (100 - 50) / 2, extents.scrollOffsetFor (40, M3RowExtents.ALIGN_CENTER, 0, 50));
		assertEquals (420, extents.scrollOffsetFor (40, M3RowExtents.ALIGN_AUTO, 420, 50), "revealed: no move");
		assertEquals (extents.maxScrollOffset (50), extents.scrollOffsetFor (99, M3RowExtents.ALIGN_START, 0, 50), "clamped");
		assertEquals (extents.totalExtent () - 50, extents.maxScrollOffset (50));
		assertTrue (extents.toString ().startsWith ("M3RowExtents [100 rows, 1 measured"));
	}

	@Test
	public void test_argumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> new M3RowExtents (-1, 10));
		assertThrows (IllegalArgumentException.class, () -> new M3RowExtents (5, 0));
		M3RowExtents extents = new M3RowExtents (5, 10);
		assertThrows (IllegalArgumentException.class, () -> extents.setEstimate (0));
		assertThrows (IllegalArgumentException.class, () -> extents.setExtent (5, 1));
		assertThrows (IllegalArgumentException.class, () -> extents.setExtent (0, -1));
		assertThrows (IllegalArgumentException.class, () -> extents.extentOf (-1));
		assertThrows (IllegalArgumentException.class, () -> extents.isMeasured (5));
		assertThrows (IllegalArgumentException.class, () -> extents.clearExtent (5));
		assertThrows (IllegalArgumentException.class, () -> extents.clearFrom (6));
		assertThrows (IllegalArgumentException.class, () -> extents.insert (6, 1));
		assertThrows (IllegalArgumentException.class, () -> extents.insert (0, -1));
		assertThrows (IllegalArgumentException.class, () -> extents.remove (4, 2));
		assertThrows (IllegalArgumentException.class, () -> extents.remove (0, -1));
		assertThrows (IllegalArgumentException.class, () -> extents.reset (-1));
		assertThrows (IllegalArgumentException.class, () -> extents.offsetOf (6));
		assertThrows (IllegalArgumentException.class, () -> extents.visibleRange (0, -1));
		assertThrows (IllegalArgumentException.class, () -> extents.scrollOffsetFor (0, 9, 0, 10));
		assertThrows (IllegalArgumentException.class, () -> extents.scrollOffsetFor (5, M3RowExtents.ALIGN_AUTO, 0, 10));
		assertThrows (IllegalArgumentException.class, () -> extents.setExtentAnchored (5, 1, 0, false));
		assertThrows (IllegalArgumentException.class, () -> extents.setExtentAnchored (0, -1, 0, false));
	}
}

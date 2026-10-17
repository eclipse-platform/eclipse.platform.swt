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

import static org.eclipse.swt.tests.junit.VirtualTreeReflection.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * The logical viewport of a virtual Tree (<code>VirtualTreeViewport</code>): geometry validation, top row,
 * scroll bar projection (also past the integer range), paint window, overscan memory policy and generations.
 * Headless.
 */
public class Test_VirtualTreeViewport {
	private static final Class<?> [] GEOMETRY = {int.class, int.class};
	private static final Class<?> [] POLICY = {int.class, int.class, int.class, long.class, long.class};

	private static Object roots (int count) {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, count);
		return topology;
	}

	private static Object viewport (Object topology) {
		return constructTyped ("VirtualTreeViewport", new Class<?> [] {type ("VirtualTreeVisibleProjection")}, projection (topology));
	}

	private static Object viewport (Object topology, int overscan) {
		return constructTyped ("VirtualTreeViewport", new Class<?> [] {type ("VirtualTreeVisibleProjection"), int.class}, projection (topology), overscan);
	}

	private static void geometry (Object viewport, int rowExtent, int viewportExtent) {
		callTyped (viewport, "configureGeometry", GEOMETRY, rowExtent, viewportExtent);
	}

	private static long top (Object viewport) {
		return l (call (viewport, "topRow"));
	}

	private static long generation (Object viewport) {
		return l (call (viewport, "generation"));
	}

	private static int overscan (int wanted, int visible, int cap, long maxHeap, long usedHeap) {
		return i (callStatic ("VirtualTreeViewport", "adaptiveOverscan", POLICY, wanted, visible, cap, maxHeap, usedHeap));
	}

	@Test
	public void constructionRejectsAMissingProjectionAndNegativeOverscan () {
		Class<?> projectionType = type ("VirtualTreeVisibleProjection");
		assertEquals ("projection", assertThrows (IllegalArgumentException.class,
				() -> constructTyped ("VirtualTreeViewport", new Class<?> [] {projectionType}, (Object) null)).getMessage ());
		Object projection = projection (roots (3));
		assertEquals ("negative overscan", assertThrows (IllegalArgumentException.class,
				() -> constructTyped ("VirtualTreeViewport", new Class<?> [] {projectionType, int.class}, projection, -1)).getMessage ());
		assertEquals (8, staticField ("VirtualTreeViewport", "DEFAULT_OVERSCAN_ROWS"));
		assertEquals (16_384, staticField ("VirtualTreeViewport", "MAX_PAINT_ROWS"));
	}

	@Test
	public void invalidGeometryIsRejectedBeforeAnyChange () {
		Object viewport = viewport (roots (100));
		geometry (viewport, 20, 400);
		assertEquals (20, call (viewport, "visibleRows"));
		assertEquals ("invalid viewport geometry", assertThrows (IllegalArgumentException.class, () -> geometry (viewport, 0, 400)).getMessage ());
		assertEquals ("invalid viewport geometry", assertThrows (IllegalArgumentException.class, () -> geometry (viewport, 20, -1)).getMessage ());
		Object big = viewport (roots (20_000));
		geometry (big, 20, 400);
		assertEquals ("visible tree exceeds bounded projection", assertThrows (IllegalArgumentException.class, () -> geometry (big, 1, 20_000)).getMessage ());
		assertEquals (20, call (big, "visibleRows"), "a rejected resize keeps the previous geometry");
		assertEquals (100, call (viewport, "scrollbarMaximum"));
	}

	@Test
	public void emptyModelsAndEmptyBodiesHaveNoVisibleRows () {
		Object empty = viewport (roots (0));
		geometry (empty, 20, 400);
		assertEquals (0, call (empty, "visibleRows"));
		assertEquals (0L, call (empty, "visibleRowCount"));
		assertEquals (0, call (empty, "paintRowCount"));
		assertEquals (0, ((Object []) call (empty, "visibleWindow")).length);
		Object closed = viewport (roots (50));
		geometry (closed, 20, 0);
		assertEquals (0, call (closed, "visibleRows"));
		/* without visible rows ensureVisible makes the row the top row */
		call (closed, "ensureVisible", 30L);
		assertEquals (30L, top (closed));
	}

	@Test
	public void geometryChangesCountAsGenerations () {
		Object viewport = viewport (roots (100));
		geometry (viewport, 20, 400);
		long g0 = generation (viewport);
		geometry (viewport, 20, 400);
		assertEquals (g0, generation (viewport), "the same geometry is no change");
		geometry (viewport, 20, 380);
		long g1 = generation (viewport);
		assertTrue (g1 > g0, "a new body height is a change");
		geometry (viewport, 19, 380);
		long g2 = generation (viewport);
		assertTrue (g2 > g1, "a new row height is a change");
		call (viewport, "setTopRow", 1_000L);
		assertEquals (80L, top (viewport), "the top row is clamped to the last page");
		geometry (viewport, 19, 760);
		assertEquals (60L, top (viewport), "a taller body moves the top row up");
		assertTrue (generation (viewport) > g2);
	}

	@Test
	public void logicalRangeChangesClampTheTopRow () {
		Object topology = roots (100);
		Object viewport = viewport (topology);
		geometry (viewport, 20, 400);
		call (viewport, "setTopRow", 70L);
		call (topology, "setChildCount", ROOT, 200);
		call (viewport, "refreshLogicalRange");
		assertEquals (70L, top (viewport), "growth keeps the top row");
		assertEquals (200L, call (viewport, "visibleRowCount"));
		long g = generation (viewport);
		call (topology, "setChildCount", ROOT, 50);
		call (viewport, "refreshLogicalRange");
		assertEquals (30L, top (viewport), "a shrink clamps the top row to the last page");
		assertTrue (generation (viewport) > g);
	}

	@Test
	public void topRowMovesOnlyWhenItChanges () {
		Object viewport = viewport (roots (100));
		geometry (viewport, 20, 400);
		call (viewport, "setTopRow", 10L);
		long g = generation (viewport);
		call (viewport, "setTopRow", 10L);
		assertEquals (g, generation (viewport), "setting the same top row is no change");
		call (viewport, "setTopRow", -5L);
		assertEquals (0L, top (viewport));
		assertTrue (generation (viewport) > g);
	}

	@Test
	public void materializedTopRowsFollowTheProjection () {
		Object topology = roots (10);
		call (topology, "bind", 0, ROOT, 5);
		call (topology, "setChildCount", 0, 4);
		call (topology, "bind", 1, 0, 2);
		Object viewport = viewport (topology);
		geometry (viewport, 20, 60);
		call (viewport, "setTopMaterializedId", 0);
		assertEquals (5L, top (viewport));
		call (viewport, "setTopMaterializedId", 1);
		assertEquals (5L, top (viewport), "a row under a collapsed parent has no visible row");
		call (viewport, "setTopMaterializedId", 42);
		assertEquals (5L, top (viewport), "an unknown id has no visible row");
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		call (viewport, "refreshLogicalRange");
		call (viewport, "setTopMaterializedId", 1);
		assertEquals (8L, top (viewport), "row 5 + 1 + child index 2");
	}

	@Test
	public void ensureVisibleScrollsTheRowJustIntoView () {
		Object viewport = viewport (roots (100));
		geometry (viewport, 20, 200);
		assertEquals ("row outside viewport model", assertThrows (IllegalArgumentException.class, () -> call (viewport, "ensureVisible", -1L)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (viewport, "ensureVisible", 100L));
		call (viewport, "ensureVisible", 50L);
		assertEquals (41L, top (viewport), "from above: the row becomes the last visible row");
		call (viewport, "ensureVisible", 45L);
		assertEquals (41L, top (viewport), "a visible row does not scroll");
		call (viewport, "ensureVisible", 12L);
		assertEquals (12L, top (viewport), "from below: the row becomes the top row");
	}

	@Test
	public void scrollBarProjectsTheLogicalRows () {
		Object viewport = viewport (roots (100));
		geometry (viewport, 20, 400);
		assertFalse (z (call (viewport, "usesScaledNativeScrollbar")));
		assertEquals (100, call (viewport, "scrollbarMaximum"));
		assertEquals (20, call (viewport, "scrollbarThumb"));
		assertEquals (20, call (viewport, "scrollbarPageIncrement"));
		call (viewport, "setScrollbarSelection", 33);
		assertEquals (33L, top (viewport));
		assertEquals (33, call (viewport, "scrollbarSelection"));
	}

	@Test
	public void rowsPastTheIntegerRangeUseAScaledBar () {
		Object topology = roots (Integer.MAX_VALUE);
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "setChildCount", 0, 1_000);
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		Object viewport = viewport (topology);
		geometry (viewport, 20, 400);
		long rows = Integer.MAX_VALUE + 1_000L;
		assertEquals (rows, call (viewport, "visibleRowCount"));
		assertTrue (z (call (viewport, "usesScaledNativeScrollbar")));
		int maximum = i (call (viewport, "scrollbarMaximum")), thumb = i (call (viewport, "scrollbarThumb"));
		assertEquals (Integer.MAX_VALUE, maximum);
		call (viewport, "setScrollbarSelection", maximum - thumb);
		assertEquals (rows - 20, top (viewport), "the last bar position is the last page");
		assertEquals (maximum - thumb, call (viewport, "scrollbarSelection"));
		call (viewport, "ensureVisible", rows - 1);
		assertEquals (rows - 20, top (viewport));
	}

	@Test
	public void paintWindowIsTheVisibleRowsAndTheOverscan () {
		Object topology = roots (100);
		Object viewport = viewport (topology, 3);
		geometry (viewport, 20, 200);
		call (viewport, "setTopRow", 40L);
		assertEquals (37L, call (viewport, "firstPaintRow"));
		assertEquals (16, call (viewport, "paintRowCount"));
		assertEquals (10, ((Object []) call (viewport, "visibleWindow")).length);
		assertEquals (16, ((Object []) call (viewport, "paintWindow")).length);
		Object residency = residency ();
		call (viewport, "paintResidency", residency);
		assertEquals (16, call (residency, "paintCount"));
		assertEquals (37L, call (residency, "firstPaintRow"));
		long presentation = l (call (viewport, "presentationGeneration"));
		call (topology, "bind", 0, ROOT, 41);
		call (topology, "flag", 0, bit ("CHECKED"), true);
		assertTrue (l (call (viewport, "presentationGeneration")) > presentation, "a flag change is a presentation change");
	}

	@Test
	public void overscanPolicyKeepsAHeapReserve () {
		for (int [] invalid : new int [] [] {{-1, 1, 1}, {1, -1, 1}, {1, 1, -1}}) {
			assertEquals ("invalid tree viewport memory policy", assertThrows (IllegalArgumentException.class,
					() -> overscan (invalid [0], invalid [1], invalid [2], 1L << 30, 0)).getMessage ());
		}
		assertThrows (IllegalArgumentException.class, () -> overscan (8, 20, 100, 0, 0));
		assertThrows (IllegalArgumentException.class, () -> overscan (8, 20, 100, 1L << 30, -1));
		assertThrows (IllegalArgumentException.class, () -> overscan (8, 20, 100, 1L << 30, (1L << 30) + 1));
		assertEquals (0, overscan (8, 0, 100, 1L << 30, 0), "no visible rows: no overscan");
		assertEquals (0, overscan (0, 20, 100, 1L << 30, 0), "no overscan wanted");
		assertEquals (8, overscan (8, 20, 100, 1L << 30, 0));
		assertEquals (5, overscan (8, 20, 5, 1L << 30, 0), "capped per side");
		/* a small heap keeps a reserve of 1 MiB; a large one a sixteenth */
		assertEquals (0, overscan (8, 20, 100, 1L << 20, 0), "free memory within the reserve");
		assertEquals (0, overscan (8, 300, 100, 2L << 20, 0), "the visible rows alone exceed the free memory");
		assertEquals (3, overscan (8, 1, 100, (1L << 20) + 7 * 4096, 0), "(7 rows - 1 visible) / 2 per side");
		assertEquals (0, overscan (8, 20, 100, 1L << 30, (1L << 30) - (1L << 26)), "used up to the sixteenth");
	}
}

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
 * The caller-owned residency buffer of the Tree projection
 * (<code>VirtualTreeVisibleProjection.Residency</code>): the dense physical preorder with its ancestor
 * closure, every per-entry lane, physical navigation and paths, the native snapshot, expander hints,
 * facade binding and the preorder invariants. Headless.
 */
public class Test_VirtualTreeResidency {
	/*
	 * Roots 0..5 (6 logical roots). Root index 1 is id 0, expanded, 3 children: (0,0) is id 1,
	 * expanded, 2 children of which (1,1) is id 3; (0,2) is id 2, collapsed, 5 children. Visible rows:
	 * 0 cold root, 1 id0, 2 id1, 3 cold (1,0), 4 id3, 5 cold (0,1), 6 id2, 7..10 cold roots 2..5.
	 */
	private Object topology;

	private Object forest () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 6);
		call (topology, "bind", 0, ROOT, 1);
		call (topology, "setChildCount", 0, 3);
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		call (topology, "bind", 1, 0, 0);
		call (topology, "setChildCount", 1, 2);
		call (topology, "flag", 1, bit ("EXPANDED"), true);
		call (topology, "bind", 3, 1, 1);
		call (topology, "bind", 2, 0, 2);
		call (topology, "setChildCount", 2, 5);
		return topology;
	}

	/** The window of visible rows 3..8 with its ancestor closure id1, id0. */
	private Object window () {
		topology = forest ();
		return residencyWindow (projection (topology), 3, 6);
	}

	private int [] lane (Object residency, String accessor) {
		int size = i (call (residency, "size"));
		int [] lane = new int [size];
		for (int entry = 0; entry < size; entry++) lane [entry] = i (call (residency, accessor, entry));
		return lane;
	}

	@Test
	public void theWindowIsTheAncestorClosurePlusThePaintRowsInPreorder () {
		Object residency = window ();
		assertEquals (8, call (residency, "size"));
		assertEquals (6, call (residency, "paintCount"));
		assertEquals (3, call (residency, "rootCount"));
		assertEquals (3L, call (residency, "firstPaintRow"));
		assertEquals (call (topology, "generation"), call (residency, "sourceGeneration"));
		assertArrayEquals (new int [] {ROOT, 0, 1, 1, 0, 0, ROOT, ROOT}, lane (residency, "parentId"));
		assertArrayEquals (new int [] {1, 0, 0, 1, 1, 2, 2, 3}, lane (residency, "childIndex"));
		assertArrayEquals (new int [] {0, 1, -1, 3, -1, 2, -1, -1}, lane (residency, "materializedId"));
		assertArrayEquals (new int [] {0, 1, 2, 2, 1, 1, 0, 0}, lane (residency, "depth"));
		assertArrayEquals (new int [] {-1, 0, 1, 1, 0, 0, -1, -1}, lane (residency, "physicalParent"));
		assertArrayEquals (new int [] {0, 0, 0, 1, 1, 2, 1, 2}, lane (residency, "physicalIndex"));
		assertArrayEquals (new int [] {3, 2, 0, 0, 0, 0, 0, 0}, lane (residency, "childCount"));
		for (int entry = 0; entry < 8; entry++) {
			assertEquals (entry + 1L, l (call (residency, "visibleRow", entry)), "visible row of entry " + entry);
			assertFalse (z (call (residency, "isHint", entry)));
		}
		for (int offset = 0; offset < 6; offset++) assertEquals (offset + 2, call (residency, "paintEntry", offset));
	}

	@Test
	public void entriesAreFoundByIdCoordinateAndPhysicalPlace () {
		Object residency = window ();
		assertEquals (3, call (residency, "entryForMaterializedId", 3));
		assertEquals (0, call (residency, "entryForMaterializedId", 0));
		assertEquals (-1, call (residency, "entryForMaterializedId", -1));
		assertEquals (-1, call (residency, "entryForMaterializedId", 99));
		assertEquals (5, call (residency, "entryForCoordinate", 0, 2));
		assertEquals (6, call (residency, "entryForCoordinate", ROOT, 2));
		assertEquals (-1, call (residency, "entryForCoordinate", ROOT, 0));
		assertEquals (0, call (residency, "physicalChild", -1, 0));
		assertEquals (6, call (residency, "physicalChild", -1, 1));
		assertEquals (7, call (residency, "physicalChild", -1, 2));
		assertEquals (-1, call (residency, "physicalChild", -1, 3));
		assertEquals (-1, call (residency, "physicalChild", -1, -1));
		assertEquals (1, call (residency, "physicalChild", 0, 0));
		assertEquals (4, call (residency, "physicalChild", 0, 1));
		assertEquals (5, call (residency, "physicalChild", 0, 2));
		assertEquals (3, call (residency, "physicalChild", 1, 1));
		assertEquals (-1, call (residency, "physicalChild", 3, 0));
	}

	@Test
	public void physicalPathsRoundTrip () {
		Object residency = window ();
		int [] path = new int [4];
		for (int entry = 0; entry < 8; entry++) {
			int length = i (call (residency, "physicalPath", entry, path));
			assertEquals (i (call (residency, "depth", entry)) + 1, length);
			assertEquals (entry, call (residency, "entryAtPhysicalPath", path, length), "entry " + entry);
		}
		assertEquals (i (call (residency, "physicalPath", 3, path)), 3);
		assertArrayEquals (new int [] {0, 0, 1, 0}, path);
		assertEquals (-1, call (residency, "entryAtPhysicalPath", new int [] {0, 5}, 2));
		assertEquals (-1, call (residency, "entryAtPhysicalPath", new int [] {9}, 1));
	}

	@Test
	public void theNativeSnapshotCarriesTheFourLanes () {
		Object residency = window ();
		int [] snapshot = (int []) call (residency, "nativeModelSnapshot");
		assertEquals (2 + 8 * 4, snapshot.length);
		assertEquals (8, snapshot [0]);
		assertEquals (3, snapshot [1]);
		int [] expectedParents = {-1, 0, 1, 1, 0, 0, -1, -1};
		int [] expectedIndices = {0, 0, 0, 1, 1, 2, 1, 2};
		int [] expectedCounts = {3, 2, 0, 0, 0, 0, 0, 0};
		int [] expectedIds = {0, 1, -1, 3, -1, 2, -1, -1};
		for (int entry = 0; entry < 8; entry++) {
			assertEquals (expectedParents [entry], snapshot [2 + entry]);
			assertEquals (expectedIndices [entry], snapshot [2 + 8 + entry]);
			assertEquals (expectedCounts [entry], snapshot [2 + 16 + entry]);
			assertEquals (expectedIds [entry], snapshot [2 + 24 + entry]);
		}
	}

	@Test
	public void expanderHintsGiveCollapsedParentsOneNonPaintChild () {
		window ();
		/* a materialized leaf with a known zero count (root index 2) gets no hint */
		call (topology, "bind", 4, ROOT, 2);
		call (topology, "setChildCount", 4, 0);
		Object residency = residencyWindow (projection (topology), 3, 6);
		callTyped (residency, "addExpanderHints", new Class<?> [] {type ("VirtualTreeTopology")}, topology);
		/* id2 (entry 5) is collapsed with 5 known children and none resident: one hint child */
		assertEquals (9, call (residency, "size"));
		assertEquals (6, call (residency, "paintCount"), "hints are not paint rows");
		assertEquals (3, call (residency, "rootCount"));
		assertTrue (z (call (residency, "isHint", 8)));
		assertEquals (2, call (residency, "parentId", 8));
		assertEquals (0, call (residency, "childIndex", 8));
		assertEquals (-2, call (residency, "materializedId", 8));
		assertEquals (2, call (residency, "depth", 8));
		assertEquals (5, call (residency, "physicalParent", 8));
		assertEquals (1, call (residency, "childCount", 5));
		assertEquals (8, call (residency, "physicalChild", 5, 0));
		assertEquals (-1L, call (residency, "visibleRow", 8));
		/* id0 and id1 have resident children, id3's count is unknown, cold rows have no id: no hints */
		callTyped (residency, "addExpanderHints", new Class<?> [] {type ("VirtualTreeTopology")}, topology);
		assertEquals (9, call (residency, "size"), "the hint child suppresses a second hint");
	}

	@Test
	public void facadesBindToColdRowsButNeverToHints () {
		Object residency = window ();
		call (residency, "bindFacade", 2, 42);
		assertEquals (42, call (residency, "materializedId", 2));
		assertEquals (2, call (residency, "entryForMaterializedId", 42));
		assertEquals ("facade binding", assertThrows (IllegalArgumentException.class,
				() -> call (residency, "bindFacade", 2, -1)).getMessage ());
		callTyped (residency, "addExpanderHints", new Class<?> [] {type ("VirtualTreeTopology")}, topology);
		assertThrows (IllegalArgumentException.class, () -> call (residency, "bindFacade", 8, 7), "a hint takes no facade");
	}

	@Test
	public void entryAndOffsetBoundsAreEnforced () {
		Object residency = window ();
		for (int entry : new int [] {-1, 8}) {
			final int bad = entry;
			assertEquals ("resident entry", assertThrows (IllegalArgumentException.class, () -> call (residency, "isHint", bad)).getMessage ());
			assertThrows (IllegalArgumentException.class, () -> call (residency, "depth", bad));
			assertThrows (IllegalArgumentException.class, () -> call (residency, "visibleRow", bad));
			assertThrows (IllegalArgumentException.class, () -> call (residency, "physicalIndex", bad));
			assertThrows (IllegalArgumentException.class, () -> call (residency, "childCount", bad));
		}
		/* -1 is the root level, not an entry: only a positive out-of-range parent entry is rejected */
		assertThrows (IllegalArgumentException.class, () -> call (residency, "physicalChild", 8, 0));
		for (int offset : new int [] {-1, 6}) {
			final int bad = offset;
			assertEquals ("paint offset", assertThrows (IllegalArgumentException.class, () -> call (residency, "paintEntry", bad)).getMessage ());
		}
		Class<?> [] pathTypes = {int [].class, int.class};
		assertEquals ("physical path", assertThrows (IllegalArgumentException.class,
				() -> callTyped (residency, "entryAtPhysicalPath", pathTypes, (Object) null, 1)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (residency, "entryAtPhysicalPath", new int [] {0}, 0));
		assertThrows (IllegalArgumentException.class, () -> call (residency, "entryAtPhysicalPath", new int [] {0}, 2));
		assertEquals ("path capacity", assertThrows (IllegalArgumentException.class,
				() -> call (residency, "physicalPath", 3, new int [2])).getMessage ());
		assertThrows (IllegalArgumentException.class,
				() -> callTyped (residency, "physicalPath", new Class<?> [] {int.class, int [].class}, 3, (Object) null));
	}

	@Test
	public void aReusedBufferForgetsThePreviousWindow () {
		Object residency = window ();
		Object projection = projection (topology);
		callTyped (projection, "residencyWindow", new Class<?> [] {long.class, int.class, residency.getClass ()}, 0L, 2, residency);
		assertEquals (2, call (residency, "size"));
		assertEquals (2, call (residency, "paintCount"));
		assertEquals (0L, call (residency, "firstPaintRow"));
		assertEquals (2, call (residency, "rootCount"));
		assertArrayEquals (new int [] {ROOT, ROOT}, lane (residency, "parentId"));
		assertArrayEquals (new int [] {0, 1}, lane (residency, "childIndex"));
		callTyped (projection, "residencyWindow", new Class<?> [] {long.class, int.class, residency.getClass ()}, 20L, 4, residency);
		assertEquals (0, call (residency, "size"), "a window past the rows is empty");
		assertEquals (20L, call (residency, "firstPaintRow"));
	}

	/* the ancestor closure of the first paint row must walk to the root; a wrong depth is an inconsistency */
	@Test
	public void inconsistentDirectAppendsAreRejected () {
		window ();
		Object projection = projection (topology);
		Class<?> [] append = {projection.getClass (), int.class, int.class, int.class, int.class, long.class};
		Object fresh = residency ();
		assertEquals ("projection ancestry mismatch", assertThrows (IllegalStateException.class,
				() -> callTyped (fresh, "appendPaintRow", append, projection, 1, 0, -1, 1, 3L)).getMessage ());
		Object window = residency ();
		callTyped (window, "appendPaintRow", append, projection, 0, 0, 1, 1, 2L);
		assertEquals (2, call (window, "size"), "the ancestor id0 was appended before the paint row");
		assertEquals ("non-contiguous projection window", assertThrows (IllegalStateException.class,
				() -> callTyped (window, "appendPaintRow", append, projection, 2, 0, 3, 1, 3L)).getMessage ());
	}
}

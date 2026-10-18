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
 * Argument validation and defensive invariants of the headless Tree viewport classes
 * (<code>VirtualTreeTopology</code>, <code>VirtualTreeVisibleProjection</code>,
 * <code>VirtualTreeResidentPlan</code>, <code>VirtualTreeViewport</code>): every guard throws the
 * documented failure and leaves the state untouched. Headless.
 */
public class Test_VirtualTreeCoverage {
	static final Class<?> [] PLAN = {int.class, int [].class, int [].class, int [].class};
	static final Class<?> [] COMMIT = {int.class, int [].class, int [].class, int [].class, long [].class};

	/* ---------------------------------------------------------------- topology */

	@Test
	public void bindRejectsInvalidIdsParentsAndCycles () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 4);
		assertEquals ("negative tree id", assertThrows (IllegalArgumentException.class, () -> call (topology, "bind", -1, ROOT, 0)).getMessage ());
		assertEquals ("invalid parent id", assertThrows (IllegalArgumentException.class, () -> call (topology, "bind", 0, -2, 0)).getMessage ());
		assertEquals ("negative child index", assertThrows (IllegalArgumentException.class, () -> call (topology, "bind", 0, ROOT, -1)).getMessage ());
		assertEquals ("unknown tree id 9", assertThrows (IllegalArgumentException.class, () -> call (topology, "bind", 0, 9, 0)).getMessage ());
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "setChildCount", 0, 2);
		call (topology, "bind", 1, 0, 0);
		call (topology, "setChildCount", 1, 1);
		assertEquals ("duplicate materialized tree coordinate",
				assertThrows (IllegalStateException.class, () -> call (topology, "bind", 2, 0, 0)).getMessage ());
		assertEquals ("cyclic tree parent", assertThrows (IllegalArgumentException.class, () -> call (topology, "bind", 0, 1, 0)).getMessage ());
		long generation = l (call (topology, "generation"));
		call (topology, "bind", 1, 0, 0);
		assertEquals (generation, call (topology, "generation"), "re-binding the same coordinate is a no-op");
	}

	@Test
	public void rebindingMovesAnExpandedSubtreeWithItsVisibleRows () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 5);
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "setChildCount", 0, 3);
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		assertEquals (8L, call (topology, "visibleRowCount"));
		call (topology, "bind", 0, ROOT, 4);
		assertEquals (8L, call (topology, "visibleRowCount"), "the subtree's extra rows follow the move");
		assertEquals (4, call (topology, "childIndex", 0));
		/* a collapsed node moves without a visible contribution */
		call (topology, "flag", 0, bit ("EXPANDED"), false);
		call (topology, "bind", 0, ROOT, 2);
		assertEquals (5L, call (topology, "visibleRowCount"));
		/* a move to another parent */
		call (topology, "bind", 1, ROOT, 1);
		call (topology, "setChildCount", 1, 1);
		call (topology, "bind", 2, 0, 0);
		call (topology, "bind", 2, 1, 0);
		assertEquals (1, call (topology, "parentId", 2));
		assertEquals (-1, call (topology, "firstMaterializedChildId", 0));
	}

	@Test
	public void coordinateRemovalValidatesItsRange () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		assertEquals ("negative child index", assertThrows (IllegalArgumentException.class,
				() -> call (topology, "removeCoordinate", ROOT, -1)).getMessage ());
		assertThrows (IndexOutOfBoundsException.class, () -> call (topology, "removeCoordinate", ROOT, 2));
		assertThrows (IllegalArgumentException.class, () -> call (topology, "removeCoordinate", 7, 0));
		assertEquals ("negative child index", assertThrows (IllegalArgumentException.class,
				() -> call (topology, "insertCoordinate", ROOT, -1, 0)).getMessage ());
		call (topology, "removeCoordinate", ROOT, 1);
		assertEquals (1, call (topology, "childCount", ROOT), "removing a cold coordinate only shrinks the count");
		call (topology, "releaseSubtree", 42);
		assertEquals (1, call (topology, "childCount", ROOT), "releasing an absent subtree is a no-op");
	}

	@Test
	public void insertsIntoUnknownCountsLeaveThemUnknown () {
		Object topology = topology ();
		call (topology, "insertCoordinate", ROOT, 0, 5);
		assertFalse (z (call (topology, "childCountKnown", ROOT)), "an insert does not invent the root count");
		assertTrue (z (call (topology, "contains", 5)));
		call (topology, "setChildCount", ROOT, 2);
		call (topology, "insertCoordinate", 5, 0, 6);
		assertFalse (z (call (topology, "childCountKnown", 5)), "an insert does not invent a parent count");
		assertEquals (9, call (topology, "idCapacity"), "the id lanes grew by half past id 6");
		assertFalse (z (call (topology, "contains", 4096)), "ids past the capacity are absent");
	}

	@Test
	public void unknownCountsAndAbsentParentsAnswerTheirContracts () {
		Object topology = topology ();
		assertEquals ("root child count unknown", assertThrows (IllegalStateException.class, () -> call (topology, "childCount", ROOT)).getMessage ());
		assertEquals ("negative child count", assertThrows (IllegalArgumentException.class, () -> call (topology, "setChildCount", ROOT, -1)).getMessage ());
		int [] snapshot = (int []) call (topology, "nativeModelSnapshot");
		assertEquals (0, snapshot [1], "an unknown root count is published as empty");
		call (topology, "setChildCount", ROOT, 3);
		call (topology, "bind", 0, ROOT, 0);
		assertEquals ("child count unknown", assertThrows (IllegalStateException.class, () -> call (topology, "childCount", 0)).getMessage ());
		assertFalse (z (call (topology, "childCountKnown", 9)), "an absent parent has no known count");
		assertEquals (-1, call (topology, "materializedChildId", 9, 0), "an absent parent has no materialized children");
		assertEquals (-1, call (topology, "materializedChildId", 0, -1));
	}

	@Test
	public void wholesaleStateWritesTrackExpansionAndPresentation () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "setChildCount", 0, 3);
		long expanded = bit ("EXPANDED"), checked = bit ("CHECKED");
		long state = l (call (topology, "state", 0));
		long generation = l (call (topology, "generation")), presentation = l (call (topology, "presentationGeneration"));
		call (topology, "state", 0, state);
		assertEquals (presentation, call (topology, "presentationGeneration"), "writing the same state is a no-op");
		call (topology, "state", 0, state | checked);
		assertEquals (generation, call (topology, "generation"), "a presentation flag does not move the geometry revision");
		assertTrue (l (call (topology, "presentationGeneration")) > presentation);
		call (topology, "state", 0, state | checked | expanded);
		assertTrue (l (call (topology, "generation")) > generation, "an expansion through the mask is a geometry revision");
		assertEquals (5L, call (topology, "visibleRowCount"));
		assertThrows (IllegalArgumentException.class, () -> call (topology, "state", 9, 0L));
		assertThrows (IllegalArgumentException.class, () -> call (topology, "state", 9));
		assertThrows (IllegalArgumentException.class, () -> call (topology, "flag", 9, checked, true));
		assertThrows (IllegalArgumentException.class, () -> call (topology, "flag", 9, checked));
	}

	@Test
	public void expandedUnknownCountsContributeNoRows () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 3);
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		assertEquals (3L, call (topology, "visibleRowCount"), "expansion without a count adds nothing");
		call (topology, "bind", 1, 0, 0);
		call (topology, "setChildCount", 1, 2);
		call (topology, "flag", 1, bit ("EXPANDED"), true);
		assertEquals (3L, call (topology, "visibleRowCount"), "rows below an unknown count stay hidden");
		Object projection = projection (topology);
		assertEquals (2L, call (projection, "visibleIndexOfCoordinate", ROOT, 2), "an expanded unknown count offsets nothing");
		call (topology, "setChildCount", 0, 1);
		assertEquals (6L, call (topology, "visibleRowCount"));
	}

	@Test
	public void defensiveTopologyChecksHoldOutsideTheirCallers () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		call (topology, "bind", 0, ROOT, 0);
		assertFalse (z (call (topology, "contains", -1)), "a negative id is never present");
		assertFalse (z (callTyped (topology, "subtreeHasFlag", new Class<?> [] {int.class, long.class}, 9, bit ("PINNED"))),
				"an absent subtree has no flags");
		callTyped (topology, "discardSubtreeDetached", new Class<?> [] {int.class}, 9);
		callTyped (topology, "propagateVisibleContribution", new Class<?> [] {int.class, long.class}, 0, 0L);
		assertEquals (2L, call (topology, "visibleRowCount"), "a zero contribution changes nothing");
		callTyped (topology, "adjustKnownChildCount", new Class<?> [] {int.class, int.class}, 9, 1);
		callTyped (topology, "shiftSiblingIndices", new Class<?> [] {int.class, int.class, int.class}, ROOT, 0, 0);
		callTyped (topology, "unlink", new Class<?> [] {int.class}, 9);
		assertEquals (2, call (topology, "childCount", ROOT), "absent parents and zero deltas change nothing");
		assertEquals (0, call (topology, "childIndex", 0));
		call (topology, "forgetSubtree", 9);
		assertEquals (1, call (topology, "materializedCount"));
	}

	/* the chain detector: a sibling chain that lost a bound id is reported, never walked past */
	@Test
	public void aCorruptedSiblingChainIsDetected () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 3);
		call (topology, "bind", 0, ROOT, 0);
		call (topology, "bind", 1, ROOT, 1);
		setField (topology, "rootFirstChildId", 1);
		assertEquals ("materialized tree sibling chain is inconsistent", assertThrows (IllegalStateException.class,
				() -> call (topology, "bind", 0, ROOT, 2)).getMessage ());
	}

	/* ---------------------------------------------------------------- projection */

	@Test
	public void projectionGuardsItsArguments () {
		assertEquals ("topology", assertThrows (IllegalArgumentException.class,
				() -> constructTyped ("VirtualTreeVisibleProjection", new Class<?> [] {type ("VirtualTreeTopology")}, (Object) null)).getMessage ());
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		Object projection = projection (topology);
		assertEquals (0L, call (projection, "presentationGeneration"));
		assertEquals ("visible row outside projection", assertThrows (IllegalArgumentException.class, () -> call (projection, "rowAt", 2L)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (projection, "rowAt", -1L));
		assertEquals ("negative window", assertThrows (IllegalArgumentException.class, () -> call (projection, "window", -1L, 1)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (projection, "window", 0L, -1));
		Object residency = residency ();
		Class<?> [] window = {long.class, int.class, residency.getClass ()};
		assertEquals ("residency window", assertThrows (IllegalArgumentException.class,
				() -> callTyped (projection, "residencyWindow", window, -1L, 1, residency)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> callTyped (projection, "residencyWindow", window, 0L, -1, residency));
		assertThrows (IllegalArgumentException.class, () -> callTyped (projection, "residencyWindow", window, 0L, 1, (Object) null));
		assertEquals ("logical coordinate", assertThrows (IllegalArgumentException.class,
				() -> call (projection, "visibleIndexOfCoordinate", ROOT, 2)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (projection, "visibleIndexOfCoordinate", ROOT, -1));
		assertThrows (IllegalArgumentException.class, () -> call (projection, "visibleIndexOfCoordinate", 9, 0));
		assertEquals (-1L, call (projection, "visibleIndexOf", 9), "an absent id has no visible row");
		assertEquals (0L, call (projection, "visibleChildren", 9), "an absent parent projects no children");
	}

	@Test
	public void coordinatesBeyondTheirParentCountStayInvisible () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		/* a materialized coordinate past the announced count: possible between a shrink and its window move */
		call (topology, "bind", 0, ROOT, 5);
		Object projection = projection (topology);
		Object row = call (projection, "rowAt", 0L);
		assertEquals (ROOT, call (row, "parentId"));
		assertEquals (0, call (row, "childIndex"));
		assertEquals (-1, call (row, "materializedId"));
		Object [] rows = (Object []) call (projection, "window", 0L, 5);
		assertEquals (2, rows.length, "the window clamps to the announced count");
		assertEquals (1, call (rows [1], "childIndex"));
	}

	@Test
	public void projectionOverflowGuardsHoldOutsideTheirCallers () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 2);
		call (topology, "bind", 0, ROOT, 5);
		Object projection = projection (topology);
		Class<?> [] fill = {long.class, int.class, type ("VirtualTreeVisibleProjection$Row") .arrayType (), type ("VirtualTreeVisibleProjection$Residency")};
		Object [] rows = (Object []) java.lang.reflect.Array.newInstance (type ("VirtualTreeVisibleProjection$Row"), 3);
		assertEquals ("projection overflow", assertThrows (IllegalStateException.class,
				() -> callTyped (projection, "fillWindow", fill, 0L, 3, rows, (Object) null)).getMessage ());
		Class<?> [] rowAt = {int.class, long.class, int.class};
		assertEquals ("projection overflow", assertThrows (IllegalStateException.class,
				() -> callTyped (projection, "rowAt", rowAt, ROOT, 7L, 0)).getMessage ());
	}

	/* ---------------------------------------------------------------- resident plan */

	@Test
	public void planValidatesItsWindowArrays () {
		Object plan = construct ("VirtualTreeResidentPlan");
		int [] two = new int [2], one = new int [1];
		assertEquals ("window arrays", assertThrows (IllegalArgumentException.class,
				() -> callTyped (plan, "plan", PLAN, -1, two, two, two)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> callTyped (plan, "plan", PLAN, 2, one, two, two));
		assertThrows (IllegalArgumentException.class, () -> callTyped (plan, "plan", PLAN, 2, two, one, two));
		assertThrows (IllegalArgumentException.class, () -> callTyped (plan, "plan", PLAN, 2, two, two, one));
		assertEquals ("preorder", assertThrows (IllegalArgumentException.class,
				() -> callTyped (plan, "plan", PLAN, 2, two, two, new int [] {-1, 1})).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> callTyped (plan, "plan", PLAN, 1, two, two, new int [] {-2, 0}));
		callTyped (plan, "plan", PLAN, 2, new int [] {ROOT, ROOT}, new int [] {0, 1}, new int [] {-1, -1});
		assertEquals (2, call (plan, "plannedCount"));
		for (int entry : new int [] {-1, 2}) {
			final int bad = entry;
			assertEquals ("planned entry", assertThrows (IllegalArgumentException.class, () -> call (plan, "plannedHandle", bad)).getMessage ());
			assertThrows (IllegalArgumentException.class, () -> call (plan, "previousSibling", bad));
			assertEquals ("retired index", assertThrows (IllegalArgumentException.class, () -> call (plan, "retired", bad)).getMessage ());
		}
	}

	@Test
	public void commitsValidateTheirHandles () {
		Object plan = construct ("VirtualTreeResidentPlan");
		int [] roots = {ROOT, ROOT}, indices = {0, 1}, parents = {-1, -1};
		assertEquals ("commit without residency plan", assertThrows (IllegalStateException.class,
				() -> callTyped (plan, "commit", new Class<?> [] {long [].class}, new long [2])).getMessage ());
		callTyped (plan, "plan", PLAN, 2, roots, indices, parents);
		assertEquals ("commit arrays", assertThrows (IllegalArgumentException.class,
				() -> callTyped (plan, "commit", COMMIT, 1, roots, indices, parents, new long [2])).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> callTyped (plan, "commit", COMMIT, 2, roots, indices, parents, new long [1]));
		assertEquals ("uncommitted native row 1", assertThrows (IllegalArgumentException.class,
				() -> callTyped (plan, "commit", COMMIT, 2, roots, indices, parents, new long [] {7, 0})).getMessage ());
		callTyped (plan, "commit", COMMIT, 2, roots, indices, parents, new long [] {7, 8});
		assertEquals (2, call (plan, "size"));
		assertEquals ("resident entry", assertThrows (IllegalArgumentException.class, () -> call (plan, "handle", 2)).getMessage ());
		assertThrows (IllegalArgumentException.class, () -> call (plan, "keyOf", -1));
		assertThrows (IllegalArgumentException.class, () -> call (plan, "removeSubtree", 2));
		assertEquals (0L, call (plan, "handleOf", ROOT, 5));
		assertEquals (-1, call (plan, "entryOf", 99L));
	}

	@Test
	public void aResidencyBufferPlansAndCommitsConveniently () {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 4);
		call (topology, "bind", 0, ROOT, 1);
		call (topology, "setChildCount", 0, 1);
		call (topology, "flag", 0, bit ("EXPANDED"), true);
		Object residency = residencyWindow (projection (topology), 0L, 5);
		Object plan = construct ("VirtualTreeResidentPlan");
		callTyped (plan, "plan", new Class<?> [] {residency.getClass ()}, residency);
		int count = i (call (plan, "plannedCount"));
		assertEquals (5, count);
		long [] handles = new long [count];
		for (int entry = 0; entry < count; entry++) {
			assertEquals (0L, call (plan, "plannedHandle", entry));
			handles [entry] = 100 + entry;
		}
		callTyped (plan, "commit", new Class<?> [] {long [].class}, (Object) handles);
		assertEquals (5, call (plan, "size"));
		assertEquals (100L, call (plan, "handle", 0));
		assertEquals (102L, call (plan, "handleOf", 0, 0), "the expanded child is resident");
		/* a duplicate coordinate in a planned window reuses the handle only once */
		callTyped (plan, "plan", PLAN, 2, new int [] {ROOT, ROOT}, new int [] {1, 1}, new int [] {-1, -1});
		assertEquals (101L, call (plan, "plannedHandle", 0));
		assertEquals (0L, call (plan, "plannedHandle", 1));
	}

	@Test
	public void siblingShiftsRewriteOnlyMatchingKeys () {
		Object plan = construct ("VirtualTreeResidentPlan");
		int [] roots = {ROOT, ROOT, 5}, indices = {3, 4, 0}, parents = {-1, -1, 1};
		callTyped (plan, "plan", PLAN, 3, roots, indices, parents);
		callTyped (plan, "commit", COMMIT, 3, roots, indices, parents, new long [] {7, 8, 9});
		call (plan, "shiftSiblings", ROOT, 4, 0);
		assertEquals (7L, call (plan, "handleOf", ROOT, 3), "a zero shift is a no-op");
		call (plan, "shiftSiblings", 42, 0, 1);
		assertEquals (7L, call (plan, "handleOf", ROOT, 3), "no key of this parent: nothing moves");
		call (plan, "shiftSiblings", ROOT, 4, 2);
		assertEquals (7L, call (plan, "handleOf", ROOT, 3));
		assertEquals (8L, call (plan, "handleOf", ROOT, 6));
		assertEquals (9L, call (plan, "handleOf", 5, 0), "another parent's child keeps its key");
		assertEquals ("negative child index", assertThrows (IllegalArgumentException.class,
				() -> call (plan, "shiftSiblings", ROOT, 0, -4)).getMessage ());
		call (plan, "clear");
		assertEquals (0, call (plan, "size"));
		assertEquals (0, call (plan, "plannedCount"));
		assertEquals (0L, call (plan, "handleOf", ROOT, 3));
	}

	@Test
	public void subtreeRemovalRenumbersTheSurvivingParents () {
		Object plan = construct ("VirtualTreeResidentPlan");
		/* preorder: rootA, rootB, B/child, rootC, C/child */
		int [] parentIds = {ROOT, ROOT, 20, ROOT, 30};
		int [] indices = {0, 1, 0, 2, 0};
		int [] parents = {-1, -1, 1, -1, 3};
		callTyped (plan, "plan", PLAN, 5, parentIds, indices, parents);
		callTyped (plan, "commit", COMMIT, 5, parentIds, indices, parents, new long [] {1, 2, 3, 4, 5});
		call (plan, "removeSubtree", 1);
		assertEquals (3, call (plan, "size"));
		assertEquals (1L, call (plan, "handle", 0));
		assertEquals (4L, call (plan, "handle", 1));
		assertEquals (5L, call (plan, "handle", 2));
		assertEquals (1, call (plan, "entryOf", 4L));
		assertEquals (2, call (plan, "entryOf", 5L), "the child follows its renumbered parent");
		call (plan, "removeSubtree", 1);
		assertEquals (1, call (plan, "size"), "removing the last subtree leaves the head");
		assertEquals (1L, call (plan, "handle", 0));
	}

	/* ---------------------------------------------------------------- viewport internals */

	@Test
	public void viewportBudgetGuardsHoldOutsideTheirCallers () throws Exception {
		Object topology = topology ();
		call (topology, "setChildCount", ROOT, 20_000);
		Object viewport = constructTyped ("VirtualTreeViewport",
				new Class<?> [] {type ("VirtualTreeVisibleProjection")}, projection (topology));
		/* the paint budget is validated before the metrics change; an unvalidated write is still refused */
		Object metrics = field (viewport, "scrollMetrics");
		callTyped (metrics, "configure", new Class<?> [] {long.class, int.class, int.class}, 20_000L, 1, 20_000);
		assertEquals ("visible tree exceeds bounded projection", assertThrows (IllegalArgumentException.class,
				() -> call (viewport, "refreshOverscanForMemoryPressure")).getMessage ());
		Class<?> [] budget = {long.class, int.class, int.class};
		assertEquals ("negative logical rows", assertThrows (IllegalArgumentException.class,
				() -> callStatic ("VirtualTreeViewport", "validateVisibleBudget", budget, -1L, 1, 1)).getMessage ());
		Class<?> [] add = {long.class, int.class};
		assertEquals (Long.MAX_VALUE, callStatic ("VirtualTreeViewport", "saturatedAdd", add, Long.MAX_VALUE - 1, 5));
		assertEquals (10L, callStatic ("VirtualTreeViewport", "saturatedAdd", add, 7L, 3));
		assertEquals (4L, callStatic ("VirtualTreeViewport", "saturatedAdd", add, 7L, -3));
	}
}

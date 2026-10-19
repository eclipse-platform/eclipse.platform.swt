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
import java.lang.reflect.*;
import java.util.*;
import org.junit.jupiter.api.Test;

/** Removal and flag-query oracles independent of the packed topology implementation. */
public class Test_VirtualTreeTopology {
	private static final int DEPTH = 20_000;
	private static Object call (Object target, String name, Object... args) throws Exception {
		Class<?> [] types = Arrays.stream (args).map (v -> v instanceof Integer ? int.class
				: v instanceof Long ? long.class : v instanceof Boolean ? boolean.class : v.getClass ())
				.toArray (Class<?> []::new);
		Method method = target.getClass ().getDeclaredMethod (name, types);
		method.setAccessible (true);
		try { return method.invoke (target, args); }
		catch (InvocationTargetException failure) {
			if (failure.getCause () instanceof Error error) throw error;
			if (failure.getCause () instanceof Exception exception) throw exception;
			throw failure;
		}
	}
	private static Object topology () throws Exception {
		Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets.VirtualTreeTopology").getDeclaredConstructor ();
		constructor.setAccessible (true); return constructor.newInstance ();
	}
	private static long bit (String name) throws Exception {
		Field field = Class.forName ("org.eclipse.swt.widgets.VirtualItemState").getDeclaredField (name);
		field.setAccessible (true); return field.getLong (null);
	}
	private static Object chain () throws Exception {
		Object tree = topology (); call (tree, "setChildCount", -1, 2);
		for (int id = 0; id < DEPTH; id++) {
			call (tree, "bind", id, id - 1, 0);
			call (tree, "setChildCount", id, id + 1 == DEPTH ? 0 : 1);
		}
		call (tree, "bind", DEPTH, -1, 1);
		call (tree, "setChildCount", DEPTH, 0);
		long expanded = bit ("EXPANDED");
		for (int id = DEPTH - 1; id >= 0; id--) call (tree, "flag", id, expanded, true);
		call (tree, "flag", DEPTH, bit ("PINNED"), true);
		return tree;
	}

	@Test
	public void deepFlagQueryStopsAtSubtreeBoundary () throws Exception {
		Object tree = chain (); long pinned = bit ("PINNED");
		assertEquals (-1, call (tree, "highestChildIndexWithSubtreeFlag", 0, pinned));
		call (tree, "flag", DEPTH - 1, pinned, true);
		assertEquals (0, call (tree, "highestChildIndexWithSubtreeFlag", 0, pinned));
		assertEquals (1, call (tree, "highestChildIndexWithSubtreeFlag", -1, pinned));
		assertEquals (DEPTH + 1L, call (tree, "visibleRowCount"));
	}

	@Test
	public void deepForgetPreservesLogicalCoordinateAndOtherBranch () throws Exception {
		Object tree = chain (); call (tree, "forgetSubtree", 0);
		assertEquals (1, call (tree, "materializedCount"));
		assertEquals (2, call (tree, "childCount", -1));
		assertEquals (2L, call (tree, "visibleRowCount"));
		assertEquals (1, call (tree, "childIndex", DEPTH));
		assertEquals (true, call (tree, "flag", DEPTH, bit ("PINNED")));
		assertEquals (DEPTH, call (tree, "firstMaterializedChildId", -1));
		for (int id = 0; id < DEPTH; id++) assertEquals (false, call (tree, "contains", id));
		call (tree, "forgetSubtree", 0); // Absent subtree remains a no-op.
		call (tree, "bind", 0, -1, 0); // Freed stable ids remain reusable with clean state.
		assertEquals (0L, call (tree, "state", 0));
		assertEquals (false, call (tree, "childCountKnown", 0));
	}

	@Test
	public void deepReleaseShiftsOnlySurvivingSiblingCoordinates () throws Exception {
		Object tree = chain (); call (tree, "releaseSubtree", 0);
		assertEquals (1, call (tree, "materializedCount"));
		assertEquals (1, call (tree, "childCount", -1));
		assertEquals (1L, call (tree, "visibleRowCount"));
		assertEquals (0, call (tree, "childIndex", DEPTH));
		assertEquals (true, call (tree, "flag", DEPTH, bit ("PINNED")));
	}

	@Test
	public void deepPruneRetainsParentAndRefreshesVisibleWeights () throws Exception {
		Object tree = chain (); call (tree, "setChildCount", 0, 0);
		assertEquals (2, call (tree, "materializedCount"));
		assertEquals (2L, call (tree, "visibleRowCount"));
		assertEquals (0, call (tree, "childCount", 0));
		assertEquals (-1, call (tree, "firstMaterializedChildId", 0));
		assertEquals (false, call (tree, "flag", 0, bit ("HAS_CHILDREN")));
		call (tree, "setChildCount", -1, 0);
		assertEquals (0, call (tree, "materializedCount"));
		assertEquals (0L, call (tree, "visibleRowCount"));
	}

	private static final class Node {
		int id, count, coordinate; Node parent; boolean pinned, expanded;
		List<Node> children = new ArrayList<> ();
	}
	private static Node model (Random random, Node parent, int coordinate, int depth, List<Node> all) {
		Node n = new Node (); n.id = all.size () - 1; all.add (n); n.parent = parent; n.coordinate = coordinate;
		n.count = depth == 0 ? 5 : depth == 4 ? 0 : random.nextInt (6);
		n.pinned = random.nextBoolean (); n.expanded = random.nextBoolean ();
		for (int i = 0; i < n.count; i++) if (random.nextBoolean ()) n.children.add (model (random, n, i, depth + 1, all));
		return n;
	}
	private static void populate (Object tree, Node n) throws Exception {
		call (tree, "setChildCount", n.id, n.count);
		for (Node child : n.children) { call (tree, "bind", child.id, n.id, child.coordinate); populate (tree, child); }
		if (n.id >= 0) {
			call (tree, "flag", n.id, bit ("PINNED"), n.pinned);
			call (tree, "flag", n.id, bit ("EXPANDED"), n.expanded);
		}
	}
	private static long visible (Node n) {
		long rows = n.count;
		for (Node child : n.children) if (child.expanded) rows += visible (child);
		return rows;
	}
	private static boolean pinned (Node n) { return n.pinned || n.children.stream ().anyMatch (Test_VirtualTreeTopology::pinned); }
	private static int verify (Object tree, Node n, Set<Integer> alive) throws Exception {
		if (n.id >= 0) {
			alive.add (n.id);
			assertEquals (n.parent.id, call (tree, "parentId", n.id));
			assertEquals (n.coordinate, call (tree, "childIndex", n.id));
			assertEquals (n.pinned, call (tree, "flag", n.id, bit ("PINNED")));
			assertEquals (n.expanded, call (tree, "flag", n.id, bit ("EXPANDED")));
		}
		assertEquals (n.count, call (tree, "childCount", n.id));
		int highest = -1, cursor = (int)call (tree, "firstMaterializedChildId", n.id);
		for (Node child : n.children) {
			assertEquals (child.id, cursor); cursor = (int)call (tree, "nextMaterializedSiblingId", cursor);
			if (pinned (child)) highest = child.coordinate;
			verify (tree, child, alive);
		}
		assertEquals (-1, cursor);
		assertEquals (highest, call (tree, "highestChildIndexWithSubtreeFlag", n.id, bit ("PINNED")));
		return alive.size ();
	}

	@Test
	public void boundedRemovalPermutationsMatchIndependentModelAndNativeSnapshot () throws Exception {
		for (int seed = 0; seed < 32; seed++) for (int operation = 0; operation < 3; operation++) {
			Random random = new Random (0x54524545L + seed); List<Node> all = new ArrayList<> ();
			Node root = model (random, null, -1, 0, all); Object tree = topology (); populate (tree, root);
			if (all.size () == 1) continue;
			Node victim = all.get (1 + random.nextInt (all.size () - 1)); Node parent = victim.parent;
			if (operation == 2) {
				call (tree, "setChildCount", parent.id, victim.coordinate);
				parent.count = victim.coordinate;
				parent.children.removeIf (child -> child.coordinate >= parent.count);
			} else {
				call (tree, operation == 0 ? "forgetSubtree" : "releaseSubtree", victim.id);
				parent.children.remove (victim);
				if (operation == 1) {
					parent.count--;
					for (Node sibling : parent.children) if (sibling.coordinate > victim.coordinate) sibling.coordinate--;
				}
			}
			Set<Integer> alive = new HashSet<> (); assertEquals (verify (tree, root, alive), call (tree, "materializedCount"));
			assertEquals (visible (root), call (tree, "visibleRowCount"));
			int [] snapshot = (int [])call (tree, "nativeModelSnapshot"); int capacity = snapshot [0];
			assertEquals (root.count, snapshot [1]);
			for (Node n : all) if (n.id >= 0) {
				assertEquals (alive.contains (n.id), call (tree, "contains", n.id));
				assertEquals (alive.contains (n.id) ? n.parent.id : Integer.MIN_VALUE, snapshot [2 + n.id]);
				assertEquals (alive.contains (n.id) ? n.coordinate : -1, snapshot [2 + capacity + n.id]);
				assertEquals (alive.contains (n.id) ? n.count : -1, snapshot [2 + 2 * capacity + n.id]);
			}
		}
	}

	private static void collectAlive (Node node, List<Node> alive) {
		alive.add (node);
		for (Node child : node.children) collectAlive (child, alive);
	}

	@Test
	public void seededSiblingInsertRemovePreservesBranchesAndNativeColumns () throws Exception {
		for (int seed = 0; seed < 32; seed++) {
			Random random = new Random (0x5349424cL + seed);
			List<Node> all = new ArrayList<> ();
			Node root = model (random, null, -1, 0, all);
			Object tree = topology (); populate (tree, root);
			for (int step = 0; step < 128; step++) {
				List<Node> live = new ArrayList<> (); collectAlive (root, live);
				Node parent = live.get (random.nextInt (live.size ()));
				if (parent.count == 0 || random.nextBoolean ()) {
					int coordinate = random.nextInt (parent.count + 1);
					Node child = new Node ();
					child.id = 512 + 7 * step; child.parent = parent; child.coordinate = coordinate;
					child.count = random.nextInt (4); child.expanded = random.nextBoolean ();
					child.pinned = random.nextBoolean ();
					call (tree, "insertCoordinate", parent.id, coordinate, child.id);
					call (tree, "setChildCount", child.id, child.count);
					call (tree, "flag", child.id, bit ("EXPANDED"), child.expanded);
					call (tree, "flag", child.id, bit ("PINNED"), child.pinned);
					for (Node sibling : parent.children) if (sibling.coordinate >= coordinate) sibling.coordinate++;
					parent.count++; parent.children.add (child);
					parent.children.sort (Comparator.comparingInt (n -> n.coordinate)); all.add (child);
				} else {
					int coordinate = random.nextInt (parent.count);
					call (tree, "removeCoordinate", parent.id, coordinate);
					parent.children.removeIf (child -> child.coordinate == coordinate);
					for (Node sibling : parent.children) if (sibling.coordinate > coordinate) sibling.coordinate--;
					parent.count--;
				}
				Set<Integer> alive = new HashSet<> ();
				assertEquals (verify (tree, root, alive), call (tree, "materializedCount"));
				assertEquals (visible (root), call (tree, "visibleRowCount"));
				int [] snapshot = (int [])call (tree, "nativeModelSnapshot"); int capacity = snapshot [0];
				assertEquals (2 + 3 * capacity, snapshot.length);
				assertEquals (root.count, snapshot [1]);
				for (Node node : all) if (node.id >= 0) {
					boolean present = alive.contains (node.id);
					assertEquals (present, call (tree, "contains", node.id));
					assertEquals (present ? node.parent.id : Integer.MIN_VALUE, snapshot [2 + node.id]);
					assertEquals (present ? node.coordinate : -1, snapshot [2 + capacity + node.id]);
					assertEquals (present ? node.count : -1, snapshot [2 + 2 * capacity + node.id]);
				}
			}
		}
	}

	@Test
	public void siblingOverflowRetainsIdOrderedPartialUpdates () throws Exception {
		Object tree = topology (); call (tree, "setChildCount", -1, Integer.MAX_VALUE);
		call (tree, "bind", 0, -1, 10);
		call (tree, "bind", 1, -1, Integer.MAX_VALUE);
		call (tree, "bind", 2, -1, 5);
		call (tree, "bind", 63, -1, 30);
		long generation = (long)call (tree, "generation");
		assertThrows (ArithmeticException.class, () -> call (tree, "insertCoordinate", -1, 0, 3));
		assertEquals (11, call (tree, "childIndex", 0));
		assertEquals (Integer.MAX_VALUE, call (tree, "childIndex", 1));
		assertEquals (5, call (tree, "childIndex", 2));
		assertEquals (30, call (tree, "childIndex", 63));
		assertEquals (false, call (tree, "contains", 3));
		assertEquals (generation + 1, call (tree, "generation"));
		assertEquals (Integer.MAX_VALUE, call (tree, "childCount", -1));
	}

	@Test
	public void maximumCoordinateReleaseRetainsExistingBoundaryBehavior () throws Exception {
		Object tree = topology (); call (tree, "setChildCount", -1, Integer.MAX_VALUE);
		call (tree, "bind", 0, -1, Integer.MAX_VALUE);
		call (tree, "bind", 1, -1, 0); call (tree, "bind", 63, -1, 100);
		call (tree, "releaseSubtree", 0);
		// Preserve the existing boundary behavior; changing it needs a separate contract decision.
		assertEquals (-1, call (tree, "childIndex", 1));
		assertEquals (99, call (tree, "childIndex", 63));
		assertEquals (Integer.MAX_VALUE - 1, call (tree, "childCount", -1));
	}

	@Test
	public void shiftGenerationAndUnknownParentFailureAreStable () throws Exception {
		Object tree = topology (); call (tree, "setChildCount", -1, 8);
		call (tree, "bind", 63, -1, 7); call (tree, "bind", 1, -1, 3);
		long generation = (long)call (tree, "generation");
		call (tree, "insertCoordinate", -1, 2, 2);
		assertEquals (generation + 3, call (tree, "generation"));
		assertEquals (4, call (tree, "childIndex", 1)); assertEquals (8, call (tree, "childIndex", 63));
		int [] snapshot = (int [])call (tree, "nativeModelSnapshot");
		generation = (long)call (tree, "generation");
		assertThrows (IllegalArgumentException.class, () -> call (tree, "insertCoordinate", 1000, 0, 3));
		assertArrayEquals (snapshot, (int [])call (tree, "nativeModelSnapshot"));
		assertEquals (generation, call (tree, "generation"));
		call (tree, "removeCoordinate", -1, 2);
		assertEquals (3, call (tree, "childIndex", 1)); assertEquals (7, call (tree, "childIndex", 63));
	}
}

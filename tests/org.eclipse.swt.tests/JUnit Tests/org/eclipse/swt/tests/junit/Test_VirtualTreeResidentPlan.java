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

/**
 * Headless oracle of the native window diff of a viewport Tree: a simulated native tree
 * applies each plan (top-most deletes, then preorder inserts after the previous physical
 * sibling) and must then hold exactly the next window, in order, with every surviving row
 * keeping its native handle.
 */
public class Test_VirtualTreeResidentPlan {
	static final int ROOT = -1;

	final Object plan = construct ();
	long nextHandle = 1;
	/* simulated native tree: handle -> key, parent handle, ordered children */
	final Map<Long, Long> nativeKey = new HashMap<> ();
	final Map<Long, Long> nativeParent = new HashMap<> ();
	final Map<Long, List<Long>> nativeChildren = new HashMap<> ();

	static Object construct () {
		try {
			Constructor<?> c = Class.forName ("org.eclipse.swt.widgets.VirtualTreeResidentPlan").getDeclaredConstructor ();
			c.setAccessible (true);
			return c.newInstance ();
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	Object call (String name, Class<?> [] types, Object... args) {
		try {
			Method m = plan.getClass ().getDeclaredMethod (name, types);
			m.setAccessible (true);
			return m.invoke (plan, args);
		} catch (InvocationTargetException e) {
			if (e.getCause () instanceof RuntimeException r) throw r;
			throw new AssertionError (e.getCause ());
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static final Class<?> [] PLAN = {int.class, int [].class, int [].class, int [].class};
	static final Class<?> [] COMMIT = {int.class, int [].class, int [].class, int [].class, long [].class};

	static long key (int parentId, int childIndex) {
		return ((long) parentId << 32) | (childIndex & 0xFFFFFFFFL);
	}

	/** A window: dense preorder rows with logical parent id, child index and physical parent entry. */
	record Window (int [] parentIds, int [] childIndices, int [] physicalParents) {
		int size () {
			return parentIds.length;
		}
	}

	/** Applies the plan to the simulated native tree, commits it and returns the native handles. */
	long [] move (Window next) {
		int n = next.size ();
		call ("plan", PLAN, n, next.parentIds, next.childIndices, next.physicalParents);
		int retired = (Integer) call ("retiredCount", new Class<?> [0]);
		for (int i = 0; i < retired; i++) {
			long handle = (Long) call ("retired", new Class<?> [] {int.class}, i);
			Long parent = nativeParent.get (handle);
			assertNotNull (parent, "retired row is native");
			if (parent != 0) assertTrue (nativeKey.containsKey (parent), "retired rows are top-most");
			deleteNative (handle);
		}
		long [] handles = new long [n];
		for (int entry = 0; entry < n; entry++) {
			long reused = (Long) call ("plannedHandle", new Class<?> [] {int.class}, entry);
			if (reused != 0) {
				assertEquals (key (next.parentIds [entry], next.childIndices [entry]), nativeKey.get (reused), "a reused row keeps its coordinate");
				handles [entry] = reused;
				continue;
			}
			int physicalParent = next.physicalParents [entry];
			long parentHandle = physicalParent == -1 ? 0 : handles [physicalParent];
			int previous = (Integer) call ("previousSibling", new Class<?> [] {int.class}, entry);
			long after = previous == -1 ? 0 : handles [previous];
			handles [entry] = insertNative (parentHandle, after, key (next.parentIds [entry], next.childIndices [entry]));
		}
		call ("commit", COMMIT, n, next.parentIds, next.childIndices, next.physicalParents, handles);
		return handles;
	}

	void deleteNative (long handle) {
		for (long child : new ArrayList<> (nativeChildren.getOrDefault (handle, List.of ()))) deleteNative (child);
		nativeChildren.remove (handle);
		long parent = nativeParent.remove (handle);
		nativeKey.remove (handle);
		nativeChildren.get (parent).remove (handle);
	}

	long insertNative (long parent, long after, long key) {
		long handle = nextHandle++;
		List<Long> siblings = nativeChildren.computeIfAbsent (parent, p -> new ArrayList<> ());
		int index = after == 0 ? 0 : siblings.indexOf (after) + 1;
		assertTrue (after == 0 || index > 0, "previous sibling is native");
		siblings.add (index, handle);
		nativeKey.put (handle, key);
		nativeParent.put (handle, parent);
		return handle;
	}

	List<Long> nativePreorder () {
		List<Long> out = new ArrayList<> ();
		Deque<Long> stack = new ArrayDeque<> ();
		List<Long> roots = nativeChildren.getOrDefault (0L, List.of ());
		for (int i = roots.size () - 1; i >= 0; i--) stack.push (roots.get (i));
		while (!stack.isEmpty ()) {
			long h = stack.pop ();
			out.add (nativeKey.get (h));
			List<Long> children = nativeChildren.getOrDefault (h, List.of ());
			for (int i = children.size () - 1; i >= 0; i--) stack.push (children.get (i));
		}
		return out;
	}

	static List<Long> keys (Window w) {
		List<Long> out = new ArrayList<> ();
		for (int i = 0; i < w.size (); i++) out.add (key (w.parentIds [i], w.childIndices [i]));
		return out;
	}

	static Window flat (int first, int count) {
		int [] parents = new int [count], indices = new int [count], physical = new int [count];
		Arrays.fill (parents, ROOT);
		Arrays.fill (physical, -1);
		for (int i = 0; i < count; i++) indices [i] = first + i;
		return new Window (parents, indices, physical);
	}

	@Test
	public void firstWindowInsertsEveryRowAndRetiresNothing () {
		long [] handles = move (flat (0, 10));
		assertEquals (10, nativeKey.size ());
		assertEquals (keys (flat (0, 10)), nativePreorder ());
		assertEquals (handles [3], (Long) call ("handleOf", new Class<?> [] {int.class, int.class}, ROOT, 3));
		assertEquals (3, (Integer) call ("entryOf", new Class<?> [] {long.class}, handles [3]));
	}

	@Test
	public void scrollingKeepsOverlapAndRetiresOnlyTheLeavingRows () {
		long [] first = move (flat (0, 10));
		long [] second = move (flat (5, 10));
		for (int i = 0; i < 5; i++) assertEquals (first [5 + i], second [i], "overlapping row " + (5 + i) + " keeps its native handle");
		assertEquals (keys (flat (5, 10)), nativePreorder ());
		assertEquals (10, nativeKey.size ());
	}

	@Test
	public void retiringAParentDeletesOnlyTheParent () {
		/* root 3 (topology id 7) with children 100..104, then far away roots 50..52 */
		Window nested = new Window (new int [] {ROOT, 7, 7, 7, 7, 7}, new int [] {3, 100, 101, 102, 103, 104},
				new int [] {-1, 0, 0, 0, 0, 0});
		move (nested);
		call ("plan", PLAN, 3, new int [] {ROOT, ROOT, ROOT}, new int [] {50, 51, 52}, new int [] {-1, -1, -1});
		assertEquals (1, (Integer) call ("retiredCount", new Class<?> [0]), "children of a retired parent go with it");
	}

	@Test
	public void ancestorPathSurvivesWhileItsChildWindowSlides () {
		Window a = new Window (new int [] {ROOT, 7, 7, 7}, new int [] {3, 100, 101, 102}, new int [] {-1, 0, 0, 0});
		Window b = new Window (new int [] {ROOT, 7, 7, 7}, new int [] {3, 102, 103, 104}, new int [] {-1, 0, 0, 0});
		long [] first = move (a);
		long [] second = move (b);
		assertEquals (first [0], second [0], "the ancestor keeps its native row");
		assertEquals (first [3], second [1], "child 102 keeps its native row");
		assertEquals (keys (b), nativePreorder ());
	}

	/* an insert or removal before resident siblings: the plan and the simulated native rows change keys only */
	void shiftSiblings (int parentId, int fromIndex, int delta) {
		call ("shiftSiblings", new Class<?> [] {int.class, int.class, int.class}, parentId, fromIndex, delta);
		for (Map.Entry<Long, Long> row : nativeKey.entrySet ()) {
			long key = row.getValue ();
			if ((int) (key >> 32) == parentId && (int) key >= fromIndex) row.setValue (key (parentId, (int) key + delta));
		}
	}

	void removeResident (long handle) {
		int entry = (Integer) call ("entryOf", new Class<?> [] {long.class}, handle);
		deleteNative (handle);
		call ("removeSubtree", new Class<?> [] {int.class}, entry);
	}

	@Test
	public void insertBeforeResidentRowsReusesEveryRowItKeeps () {
		long [] first = move (flat (0, 10));
		shiftSiblings (ROOT, 3, 1);
		long [] second = move (flat (0, 10));
		for (int i = 0; i < 3; i++) assertEquals (first [i], second [i], "row " + i + " before the insert keeps its native row");
		for (int i = 3; i < 9; i++) assertEquals (first [i], second [i + 1], "shifted row " + i + " keeps its native row");
		assertEquals (keys (flat (0, 10)), nativePreorder ());
		assertEquals (10, nativeKey.size ());
	}

	@Test
	public void removingAResidentRowKeepsTheOthers () {
		long [] first = move (flat (0, 10));
		removeResident (first [3]);
		shiftSiblings (ROOT, 4, -1);
		long [] second = move (flat (0, 10));
		for (int i = 0; i < 3; i++) assertEquals (first [i], second [i]);
		for (int i = 4; i < 10; i++) assertEquals (first [i], second [i - 1], "shifted row " + i + " keeps its native row");
		assertEquals (keys (flat (0, 10)), nativePreorder ());
	}

	@Test
	public void removingAResidentParentRemovesItsSubtreeFromThePlan () {
		/* roots 2, 3 (id 7, children 100..102), 4, 5 */
		Window before = new Window (new int [] {ROOT, ROOT, 7, 7, 7, ROOT, ROOT}, new int [] {2, 3, 100, 101, 102, 4, 5},
				new int [] {-1, -1, 1, 1, 1, -1, -1});
		long [] first = move (before);
		removeResident (first [1]);
		assertEquals (3, (Integer) call ("size", new Class<?> [0]));
		assertEquals (2, (Integer) call ("entryOf", new Class<?> [] {long.class}, first [6]), "rows after the subtree move up");
		shiftSiblings (ROOT, 4, -1);
		long [] second = move (flat (2, 4));
		assertEquals (first [0], second [0]);
		assertEquals (first [5], second [1], "root 4 is now root 3 and keeps its native row");
		assertEquals (first [6], second [2]);
		assertEquals (keys (flat (2, 4)), nativePreorder ());
	}

	@Test
	public void seededWindowMovesOverARandomForestMatchTheNextWindowExactly () {
		Random random = new Random (0x54524545L);
		/* logical forest: node = (parentId, childIndex); every node gets id = its preorder rank */
		List<int []> preorder = new ArrayList<> (); /* {parentId, childIndex, depth, id} */
		buildForest (random, ROOT, 0, preorder, new int [] {0});
		for (int move = 0; move < 2_000; move++) {
			int start = random.nextInt (preorder.size ());
			int length = 1 + random.nextInt (40);
			Window w = windowAt (preorder, start, length);
			move (w);
			assertEquals (keys (w), nativePreorder (), "move " + move);
			assertEquals (w.size (), nativeKey.size (), "no stale native rows after move " + move);
		}
	}

	static void buildForest (Random random, int parentId, int depth, List<int []> out, int [] nextId) {
		int children = depth == 0 ? 60 : depth > 3 ? 0 : random.nextInt (6);
		for (int i = 0; i < children; i++) {
			int id = nextId [0]++;
			out.add (new int [] {parentId, i, depth, id});
			if (random.nextInt (3) == 0) buildForest (random, id, depth + 1, out, nextId);
		}
	}

	/** Residency shape: the ancestor path of the first row, then a contiguous preorder run. */
	static Window windowAt (List<int []> preorder, int start, int length) {
		int end = Math.min (preorder.size (), start + length);
		List<int []> rows = new ArrayList<> ();
		Map<Integer, int []> byId = new HashMap<> ();
		for (int [] node : preorder) byId.put (node [3], node);
		Deque<int []> path = new ArrayDeque<> ();
		for (int parent = preorder.get (start) [0]; parent != ROOT; parent = byId.get (parent) [0]) path.push (byId.get (parent));
		rows.addAll (path);
		rows.addAll (preorder.subList (start, end));
		int n = rows.size ();
		int [] parents = new int [n], indices = new int [n], physical = new int [n];
		Map<Integer, Integer> entryOfId = new HashMap<> ();
		for (int i = 0; i < n; i++) {
			int [] node = rows.get (i);
			parents [i] = node [0];
			indices [i] = node [1];
			Integer parentEntry = entryOfId.get (node [0]);
			physical [i] = node [0] == ROOT ? -1 : parentEntry;
			entryOfId.put (node [3], i);
		}
		return new Window (parents, indices, physical);
	}
}

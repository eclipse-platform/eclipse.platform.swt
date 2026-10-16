/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

/**
 * Differential proof of the order-statistic sibling index of the virtual Tree
 * projection. Seeded random scripts insert, materialize, dematerialize, move,
 * expand, collapse and resize children at random depths, up to 100,000
 * materialized siblings, and every {@code rowAt}, {@code visibleIndexOf},
 * {@code visibleIndexOfCoordinate}, {@code window} and residency answer is
 * compared with the linear sibling walk that the index replaced, kept here as
 * a test-only reference. Headless: no Display is created.
 */
public class Test_VirtualTreeOrderStatistic {
	static final int ROOT = -1;

	record Row (int parentId, int childIndex, int materializedId, int depth) { }

	/** JIT-friendly access to the package-private topology and projection. */
	static final class Api {
		static final long EXPANDED;
		private static final MethodHandle NEW_TOPOLOGY, NEW_PROJECTION, NEW_RESIDENCY, BIND, INSERT, REMOVE,
				RELEASE, FORGET, SET_COUNT, SET_FLAG, FLAG, COUNT_KNOWN, COUNT, FIRST, NEXT, INDEX, PARENT,
				CONTAINS, VISIBLE_CHILDREN, CHILD_ID, ROW_AT, WINDOW, RESIDENCY_WINDOW, INDEX_OF,
				INDEX_OF_COORDINATE, ROW_COUNT, ROW_PARENT, ROW_INDEX, ROW_ID, ROW_DEPTH, PAINT_COUNT,
				PAINT_ENTRY, ENTRY_PARENT, ENTRY_INDEX, ENTRY_ID, ENTRY_DEPTH, ENTRY_ROW;

		static {
			try {
				Class<?> state = type ("VirtualItemState"), topology = type ("VirtualTreeTopology");
				Class<?> projection = type ("VirtualTreeVisibleProjection");
				Class<?> row = type ("VirtualTreeVisibleProjection$Row");
				Class<?> residency = type ("VirtualTreeVisibleProjection$Residency");
				Field expanded = state.getDeclaredField ("EXPANDED");
				expanded.setAccessible (true);
				EXPANDED = expanded.getLong (null);
				NEW_TOPOLOGY = constructor (topology);
				NEW_PROJECTION = constructor (projection, topology);
				NEW_RESIDENCY = constructor (residency);
				BIND = method (topology, "bind", int.class, int.class, int.class);
				INSERT = method (topology, "insertCoordinate", int.class, int.class, int.class);
				REMOVE = method (topology, "removeCoordinate", int.class, int.class);
				RELEASE = method (topology, "releaseSubtree", int.class);
				FORGET = method (topology, "forgetSubtree", int.class);
				SET_COUNT = method (topology, "setChildCount", int.class, int.class);
				SET_FLAG = method (topology, "flag", int.class, long.class, boolean.class);
				FLAG = method (topology, "flag", int.class, long.class);
				COUNT_KNOWN = method (topology, "childCountKnown", int.class);
				COUNT = method (topology, "childCount", int.class);
				FIRST = method (topology, "firstMaterializedChildId", int.class);
				NEXT = method (topology, "nextMaterializedSiblingId", int.class);
				INDEX = method (topology, "childIndex", int.class);
				PARENT = method (topology, "parentId", int.class);
				CONTAINS = method (topology, "contains", int.class);
				VISIBLE_CHILDREN = method (topology, "visibleChildrenRowCount", int.class);
				CHILD_ID = method (topology, "materializedChildId", int.class, int.class);
				ROW_AT = method (projection, "rowAt", long.class);
				WINDOW = method (projection, "window", long.class, int.class);
				RESIDENCY_WINDOW = method (projection, "residencyWindow", long.class, int.class, residency);
				INDEX_OF = method (projection, "visibleIndexOf", int.class);
				INDEX_OF_COORDINATE = method (projection, "visibleIndexOfCoordinate", int.class, int.class);
				ROW_COUNT = method (projection, "visibleRowCount");
				ROW_PARENT = method (row, "parentId");
				ROW_INDEX = method (row, "childIndex");
				ROW_ID = method (row, "materializedId");
				ROW_DEPTH = method (row, "depth");
				PAINT_COUNT = method (residency, "paintCount");
				PAINT_ENTRY = method (residency, "paintEntry", int.class);
				ENTRY_PARENT = method (residency, "parentId", int.class);
				ENTRY_INDEX = method (residency, "childIndex", int.class);
				ENTRY_ID = method (residency, "materializedId", int.class);
				ENTRY_DEPTH = method (residency, "depth", int.class);
				ENTRY_ROW = method (residency, "visibleRow", int.class);
			} catch (ReflectiveOperationException e) {
				throw new ExceptionInInitializerError (e);
			}
		}

		private static Class<?> type (String name) throws ClassNotFoundException {
			return Class.forName ("org.eclipse.swt.widgets." + name);
		}

		private static MethodHandle constructor (Class<?> owner, Class<?>... parameters)
				throws ReflectiveOperationException {
			Constructor<?> constructor = owner.getDeclaredConstructor (parameters);
			constructor.setAccessible (true);
			return erase (MethodHandles.lookup ().unreflectConstructor (constructor));
		}

		private static MethodHandle method (Class<?> owner, String name, Class<?>... parameters)
				throws ReflectiveOperationException {
			Method method = owner.getDeclaredMethod (name, parameters);
			method.setAccessible (true);
			return erase (MethodHandles.lookup ().unreflect (method));
		}

		/* Package-private types cannot appear at the call site; they become Object. */
		private static MethodHandle erase (MethodHandle handle) {
			MethodType type = handle.type ();
			for (int i = 0; i < type.parameterCount (); i++) {
				if (!type.parameterType (i).isPrimitive ()) type = type.changeParameterType (i, Object.class);
			}
			if (!type.returnType ().isPrimitive ()) type = type.changeReturnType (Object.class);
			return handle.asType (type);
		}

		private static RuntimeException rethrow (Throwable failure) {
			if (failure instanceof RuntimeException runtime) throw runtime;
			if (failure instanceof Error error) throw error;
			throw new IllegalStateException (failure);
		}

		static Object newTopology () {
			try { return (Object)NEW_TOPOLOGY.invokeExact (); } catch (Throwable e) { throw rethrow (e); }
		}
		static Object newProjection (Object topology) {
			try { return (Object)NEW_PROJECTION.invokeExact (topology); } catch (Throwable e) { throw rethrow (e); }
		}
		static Object newResidency () {
			try { return (Object)NEW_RESIDENCY.invokeExact (); } catch (Throwable e) { throw rethrow (e); }
		}
		static void bind (Object t, int id, int parentId, int index) {
			try { BIND.invokeExact (t, id, parentId, index); } catch (Throwable e) { throw rethrow (e); }
		}
		static void insertCoordinate (Object t, int parentId, int index, int id) {
			try { INSERT.invokeExact (t, parentId, index, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static void removeCoordinate (Object t, int parentId, int index) {
			try { REMOVE.invokeExact (t, parentId, index); } catch (Throwable e) { throw rethrow (e); }
		}
		static void releaseSubtree (Object t, int id) {
			try { RELEASE.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static void forgetSubtree (Object t, int id) {
			try { FORGET.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static void setChildCount (Object t, int parentId, int count) {
			try { SET_COUNT.invokeExact (t, parentId, count); } catch (Throwable e) { throw rethrow (e); }
		}
		static void expand (Object t, int id, boolean value) {
			try { SET_FLAG.invokeExact (t, id, EXPANDED, value); } catch (Throwable e) { throw rethrow (e); }
		}
		static boolean expanded (Object t, int id) {
			try { return (boolean)FLAG.invokeExact (t, id, EXPANDED); } catch (Throwable e) { throw rethrow (e); }
		}
		static boolean countKnown (Object t, int id) {
			try { return (boolean)COUNT_KNOWN.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int count (Object t, int id) {
			try { return (int)COUNT.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int first (Object t, int id) {
			try { return (int)FIRST.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int next (Object t, int id) {
			try { return (int)NEXT.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int index (Object t, int id) {
			try { return (int)INDEX.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int parent (Object t, int id) {
			try { return (int)PARENT.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static boolean contains (Object t, int id) {
			try { return (boolean)CONTAINS.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static long visibleChildren (Object t, int id) {
			try { return (long)VISIBLE_CHILDREN.invokeExact (t, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static int childId (Object t, int parentId, int index) {
			try { return (int)CHILD_ID.invokeExact (t, parentId, index); } catch (Throwable e) { throw rethrow (e); }
		}
		static Object rowAtRaw (Object p, long row) {
			try { return (Object)ROW_AT.invokeExact (p, row); } catch (Throwable e) { throw rethrow (e); }
		}
		static Row rowAt (Object p, long row) {
			return row (rowAtRaw (p, row));
		}
		static List<Row> window (Object p, long first, int length) {
			Object [] rows;
			try { rows = (Object [])(Object)WINDOW.invokeExact (p, first, length); } catch (Throwable e) { throw rethrow (e); }
			List<Row> result = new ArrayList<> (rows.length);
			for (Object value : rows) result.add (row (value));
			return result;
		}
		static void residencyWindow (Object p, long first, int length, Object residency) {
			try { RESIDENCY_WINDOW.invokeExact (p, first, length, residency); } catch (Throwable e) { throw rethrow (e); }
		}
		static long indexOf (Object p, int id) {
			try { return (long)INDEX_OF.invokeExact (p, id); } catch (Throwable e) { throw rethrow (e); }
		}
		static long indexOfCoordinate (Object p, int parentId, int index) {
			try { return (long)INDEX_OF_COORDINATE.invokeExact (p, parentId, index); } catch (Throwable e) { throw rethrow (e); }
		}
		static long rowCount (Object p) {
			try { return (long)ROW_COUNT.invokeExact (p); } catch (Throwable e) { throw rethrow (e); }
		}
		static int rowChildIndex (Object row) {
			try { return (int)ROW_INDEX.invokeExact (row); } catch (Throwable e) { throw rethrow (e); }
		}
		static Row row (Object row) {
			try {
				return new Row ((int)ROW_PARENT.invokeExact (row), (int)ROW_INDEX.invokeExact (row),
						(int)ROW_ID.invokeExact (row), (int)ROW_DEPTH.invokeExact (row));
			} catch (Throwable e) { throw rethrow (e); }
		}
		/** Paint rows of a residency buffer, each with its absolute visible row checked. */
		static List<Row> paintRows (Object residency, long first) {
			try {
				int count = (int)PAINT_COUNT.invokeExact (residency);
				List<Row> result = new ArrayList<> (count);
				for (int offset = 0; offset < count; offset++) {
					int entry = (int)PAINT_ENTRY.invokeExact (residency, offset);
					assertEquals (first + offset, (long)ENTRY_ROW.invokeExact (residency, entry));
					result.add (new Row ((int)ENTRY_PARENT.invokeExact (residency, entry),
							(int)ENTRY_INDEX.invokeExact (residency, entry), (int)ENTRY_ID.invokeExact (residency, entry),
							(int)ENTRY_DEPTH.invokeExact (residency, entry)));
				}
				return result;
			} catch (Throwable e) { throw rethrow (e); }
		}
	}

	/**
	 * The linear sibling walk that the sibling index replaced, kept verbatim
	 * apart from the access path. Every query is O(k) per level.
	 */
	static final class LinearReference {
		private LinearReference () {
		}

		static long visibleChildren (Object t, int parentId) {
			return Api.countKnown (t, parentId) ? Api.visibleChildren (t, parentId) : 0;
		}

		static Row rowAt (Object t, long visibleIndex) {
			int parentId = ROOT, depth = 0;
			long row = visibleIndex;
			while (true) {
				int logicalCount = Api.count (t, parentId);
				int coordinate = 0;
				int descendantId = -1;
				for (int id = Api.first (t, parentId); id >= 0; id = Api.next (t, id)) {
					int index = Api.index (t, id);
					if (index >= logicalCount) break;
					int coldGap = index - coordinate;
					if (row < coldGap) return new Row (parentId, Math.addExact (coordinate, (int)row), -1, depth);
					row -= coldGap;
					if (row == 0) return new Row (parentId, index, id, depth);
					row--;
					if (Api.expanded (t, id) && Api.countKnown (t, id)) {
						long descendants = visibleChildren (t, id);
						if (row < descendants) {
							descendantId = id;
							break;
						}
						row -= descendants;
					}
					coordinate = index + 1;
				}
				if (descendantId >= 0) {
					parentId = descendantId;
					depth = Math.incrementExact (depth);
					continue;
				}
				long childIndex = Math.addExact ((long)coordinate, row);
				if (childIndex >= logicalCount) throw new IllegalStateException ("projection overflow");
				return new Row (parentId, Math.toIntExact (childIndex), -1, depth);
			}
		}

		static long offsetWithinParent (Object t, int parentId, int targetChildIndex) {
			long offset = targetChildIndex;
			for (int id = Api.first (t, parentId); id >= 0; id = Api.next (t, id)) {
				int index = Api.index (t, id);
				if (index >= targetChildIndex) break;
				if (Api.expanded (t, id) && Api.countKnown (t, id)) {
					offset = Math.addExact (offset, visibleChildren (t, id));
				}
			}
			return offset;
		}

		static long visibleIndexOf (Object t, int materializedId) {
			if (!Api.contains (t, materializedId)) return -1;
			long offset = 0;
			int id = materializedId;
			while (true) {
				int parentId = Api.parent (t, id);
				offset = Math.addExact (offset, offsetWithinParent (t, parentId, Api.index (t, id)));
				if (parentId == ROOT) return offset;
				if (!Api.expanded (t, parentId)) return -1;
				offset = Math.addExact (offset, 1);
				id = parentId;
			}
		}

		static long visibleIndexOfCoordinate (Object t, int parentId, int childIndex) {
			long base = parentId == ROOT ? 0 : Math.addExact (visibleIndexOf (t, parentId), 1);
			return Math.addExact (base, offsetWithinParent (t, parentId, childIndex));
		}

		static List<Row> window (Object t, long firstVisible, int rowCount) {
			long total = visibleChildren (t, ROOT);
			if (firstVisible >= total || rowCount == 0) return List.of ();
			int length = (int)Math.min ((long)rowCount, total - firstVisible);
			List<Row> rows = new ArrayList<> (length);
			long skip = firstVisible;
			int parentId = ROOT, coordinate = 0, depth = 0;
			int nextId = Api.first (t, parentId);
			while (rows.size () < length) {
				int logicalCount = Api.count (t, parentId);
				int coldEnd = nextId >= 0 ? Math.min (logicalCount, Api.index (t, nextId)) : logicalCount;
				if (coordinate < coldEnd) {
					int omitted = (int)Math.min (skip, (long)coldEnd - coordinate);
					coordinate += omitted;
					skip -= omitted;
					int emit = Math.min (coldEnd - coordinate, length - rows.size ());
					for (int i = 0; i < emit; i++) rows.add (new Row (parentId, coordinate++, -1, depth));
					if (rows.size () == length) return rows;
				}
				if (nextId >= 0 && coordinate < logicalCount) {
					int id = nextId;
					nextId = Api.next (t, id);
					int index = coordinate++;
					if (skip > 0) {
						skip--;
					} else {
						rows.add (new Row (parentId, index, id, depth));
						if (rows.size () == length) return rows;
					}
					if (Api.expanded (t, id) && Api.countKnown (t, id)) {
						long descendants = visibleChildren (t, id);
						if (skip >= descendants) {
							skip -= descendants;
						} else {
							depth++;
							parentId = id;
							coordinate = 0;
							nextId = Api.first (t, id);
						}
					}
					continue;
				}
				if (depth == 0) throw new IllegalStateException ("projection overflow");
				depth--;
				coordinate = Api.index (t, parentId) + 1;
				nextId = Api.next (t, parentId);
				parentId = Api.parent (t, parentId);
			}
			return rows;
		}
	}

	/** A seeded random script over one topology; every step keeps the topology within its contract. */
	static final class Model {
		final Object topology = Api.newTopology ();
		final Object projection = Api.newProjection (topology);
		final Random random;
		final List<Integer> ids = new ArrayList<> ();
		final List<Integer> free = new ArrayList<> ();
		final int rootLimit, childLimit;
		final boolean resizeRoot;
		int nextId;
		long checks;

		/** A root limit of 0 keeps the caller's root child count: the script never resizes the root. */
		Model (long seed, int rootLimit, int childLimit) {
			random = new Random (seed);
			this.rootLimit = rootLimit;
			this.childLimit = childLimit;
			resizeRoot = rootLimit > 0;
			if (resizeRoot) Api.setChildCount (topology, ROOT, random.nextInt (rootLimit));
		}

		int newId () {
			if (!free.isEmpty () && random.nextInt (4) == 0) {
				int id = free.remove (free.size () - 1);
				if (!Api.contains (topology, id)) return id;
			}
			return nextId++;
		}

		/** A random present id, or -1; ids discarded by earlier steps are purged on the way. */
		int present () {
			while (!ids.isEmpty ()) {
				int slot = random.nextInt (ids.size ());
				int id = ids.get (slot);
				if (Api.contains (topology, id)) return id;
				ids.set (slot, ids.get (ids.size () - 1));
				ids.remove (ids.size () - 1);
				free.add (id);
			}
			return -1;
		}

		/** ROOT or a random present id whose child count is known. */
		int knownParent () {
			for (int attempt = 0; attempt < 4; attempt++) {
				int id = random.nextInt (3) == 0 ? ROOT : present ();
				if (id == ROOT || Api.countKnown (topology, id)) return id;
			}
			return ROOT;
		}

		void materialize (int parentId, int index) {
			int id = newId ();
			Api.bind (topology, id, parentId, index);
			ids.add (id);
			if (random.nextBoolean ()) Api.setChildCount (topology, id, random.nextInt (childLimit));
			if (random.nextInt (3) != 0) Api.expand (topology, id, true);
		}

		String step () {
			int op = random.nextInt (10);
			switch (op) {
				case 0: {
					int parentId = resizeRoot && random.nextInt (4) == 0 ? ROOT : present ();
					if (parentId == ROOT && !resizeRoot) return "noop";
					int count = random.nextInt (parentId == ROOT ? rootLimit : childLimit);
					Api.setChildCount (topology, parentId, count);
					return "setChildCount " + parentId + " " + count;
				}
				case 1: case 2: case 3: {
					int parentId = knownParent ();
					int count = Api.count (topology, parentId);
					if (count == 0) return "noop";
					int index = random.nextInt (count);
					if (Api.childId (topology, parentId, index) >= 0) return "noop";
					materialize (parentId, index);
					return "bind " + parentId + " " + index;
				}
				case 4: {
					int id = present ();
					if (id < 0) return "noop";
					Api.forgetSubtree (topology, id);
					return "forgetSubtree " + id;
				}
				case 5: case 6: {
					int id = present ();
					if (id < 0) return "noop";
					boolean value = random.nextInt (5) < 3;
					Api.expand (topology, id, value);
					return "expand " + id + " " + value;
				}
				case 7: {
					int parentId = knownParent ();
					int index = random.nextInt (Api.count (topology, parentId) + 1);
					int id = newId ();
					Api.insertCoordinate (topology, parentId, index, id);
					ids.add (id);
					return "insertCoordinate " + parentId + " " + index + " " + id;
				}
				case 8: {
					int parentId = knownParent ();
					int count = Api.count (topology, parentId);
					if (count == 0) return "noop";
					int index = random.nextInt (count);
					if (random.nextBoolean ()) {
						Api.removeCoordinate (topology, parentId, index);
						return "removeCoordinate " + parentId + " " + index;
					}
					int id = Api.childId (topology, parentId, index);
					if (id < 0) return "noop";
					Api.releaseSubtree (topology, id);
					return "releaseSubtree " + id;
				}
				default:
					return move ();
			}
		}

		private String move () {
			int id = present ();
			int parentId = knownParent ();
			if (id < 0) return "noop";
			int count = Api.count (topology, parentId);
			if (count == 0) return "noop";
			int index = random.nextInt (count);
			if (Api.childId (topology, parentId, index) >= 0) return "noop";
			for (int ancestor = parentId; ancestor != ROOT; ancestor = Api.parent (topology, ancestor)) {
				if (ancestor == id) return "noop";
			}
			Api.bind (topology, id, parentId, index);
			return "move " + id + " " + parentId + " " + index;
		}

		/** Compares sampled answers of the projection with the linear reference. */
		void verify (String context, int rowSamples, int idSamples, int windowSamples) {
			long total = Api.rowCount (projection);
			if (total <= rowSamples) {
				for (long row = 0; row < total; row++) checkRow (context, row);
			} else {
				checkRow (context, 0);
				checkRow (context, total - 1);
				for (int i = 0; i < rowSamples; i++) checkRow (context, (long)(random.nextDouble () * total));
			}
			for (int i = 0; i < idSamples && !ids.isEmpty (); i++) {
				int id = ids.get (random.nextInt (ids.size ()));
				checks++;
				assertEquals (LinearReference.visibleIndexOf (topology, id), Api.indexOf (projection, id),
						context + " visibleIndexOf " + id);
				int parentId = random.nextBoolean () || !Api.contains (topology, id) ? ROOT : id;
				if (!Api.countKnown (topology, parentId) || Api.count (topology, parentId) == 0) continue;
				int index = random.nextInt (Api.count (topology, parentId));
				checks++;
				assertEquals (LinearReference.visibleIndexOfCoordinate (topology, parentId, index),
						Api.indexOfCoordinate (projection, parentId, index),
						context + " visibleIndexOfCoordinate " + parentId + " " + index);
			}
			for (int i = 0; i < windowSamples; i++) checkWindow (context, total);
		}

		private void checkRow (String context, long row) {
			checks++;
			assertEquals (LinearReference.rowAt (topology, row), Api.rowAt (projection, row), context + " rowAt " + row);
		}

		private void checkWindow (String context, long total) {
			long first = (long)(random.nextDouble () * (total + 2));
			int length = new int [] {0, 1, 2, 7, 31, 64, 200} [random.nextInt (7)];
			List<Row> expected = LinearReference.window (topology, first, length);
			checks++;
			assertEquals (expected, Api.window (projection, first, length), context + " window " + first + " " + length);
			Object residency = Api.newResidency ();
			Api.residencyWindow (projection, first, length, residency);
			checks++;
			assertEquals (expected, Api.paintRows (residency, first), context + " residencyWindow " + first + " " + length);
		}
	}

	@Test
	public void randomScriptsAtRandomDepthsMatchTheLinearWalk () {
		long checks = 0;
		for (int seed = 0; seed < 32; seed++) {
			Model model = new Model (0x4F53L * 7919 + seed, 48, 8);
			StringBuilder log = new StringBuilder ();
			for (int step = 0; step < 300; step++) {
				String op = model.step ();
				if (log.length () < 4096) log.append (op).append ("; ");
				model.verify ("seed=" + seed + " step=" + step + " ops=" + log, 64, 24, 3);
			}
			checks += model.checks;
		}
		assertTrue (checks > 50_000, "checks=" + checks);
	}

	@Test
	public void hundredThousandMaterializedSiblingsMatchTheLinearWalk () {
		int siblings = 100_000;
		Model model = new Model (0x5157L, 0, 6);
		Object topology = model.topology;
		Api.setChildCount (topology, ROOT, 2 * siblings);
		int [] indices = new int [2 * siblings];
		Arrays.setAll (indices, i -> i);
		for (int i = indices.length - 1; i > 0; i--) {
			int j = model.random.nextInt (i + 1), swap = indices [i];
			indices [i] = indices [j];
			indices [j] = swap;
		}
		for (int i = 0; i < siblings; i++) {
			model.materialize (ROOT, indices [i]);
		}
		int rootMaterialized = 0;
		for (int id = Api.first (topology, ROOT); id >= 0; id = Api.next (topology, id)) rootMaterialized++;
		assertEquals (siblings, rootMaterialized);
		model.verify ("built", 200, 200, 10);
		for (int round = 0; round < 12; round++) {
			for (int op = 0; op < 40; op++) {
				String applied = model.step ();
				assertNotNull (applied);
			}
			model.verify ("round=" + round, 100, 100, 6);
		}
		assertTrue (model.checks > 2_000, "checks=" + model.checks);
	}
}

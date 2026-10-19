/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

/** Coordinate visibility is checked against an independent eager preorder. */
public class Test_VirtualTreeCoordinateVisibility {

	private record Coordinate (int parent, int index) { }

	private static final class Node {
		final int id;
		final int count;
		final Map<Integer, Node> children = new TreeMap<> ();
		boolean expanded;

		Node (int id, int count, boolean expanded) {
			this.id = id;
			this.count = count;
			this.expanded = expanded;
		}
	}

	private static Object call (Object target, String name, Object... arguments) throws Exception {
		Class<?> [] types = Arrays.stream (arguments).map (value -> value instanceof Integer ? int.class
				: value instanceof Long ? long.class : value instanceof Boolean ? boolean.class : value.getClass ())
				.toArray (Class<?> []::new);
		Method method = target.getClass ().getDeclaredMethod (name, types);
		method.setAccessible (true);
		try {
			return method.invoke (target, arguments);
		} catch (InvocationTargetException failure) {
			if (failure.getCause () instanceof Exception exception) throw exception;
			if (failure.getCause () instanceof Error error) throw error;
			throw failure;
		}
	}

	private static Object construct (String name, Object... arguments) throws Exception {
		Class<?> type = Class.forName ("org.eclipse.swt.widgets." + name);
		Constructor<?> constructor = type.getDeclaredConstructor (
				Arrays.stream (arguments).map (Object::getClass).toArray (Class<?> []::new));
		constructor.setAccessible (true);
		return constructor.newInstance (arguments);
	}

	private static long expandedBit () throws Exception {
		Field field = Class.forName ("org.eclipse.swt.widgets.VirtualItemState").getDeclaredField ("EXPANDED");
		field.setAccessible (true);
		return field.getLong (null);
	}

	@Test
	public void collapsedParentsAndHiddenAncestorsHaveNoVisibleChildCoordinates () throws Exception {
		Object topology = construct ("VirtualTreeTopology");
		call (topology, "setChildCount", -1, 6);
		call (topology, "bind", 0, -1, 1);
		call (topology, "setChildCount", 0, 4);
		call (topology, "bind", 1, 0, 1);
		call (topology, "setChildCount", 1, 3);
		call (topology, "flag", 1, expandedBit (), true);
		Object projection = construct ("VirtualTreeVisibleProjection", topology);
		long generation = (long)call (topology, "generation");
		assertEquals (-1L, call (projection, "visibleIndexOfCoordinate", 0, 0), "collapsed immediate parent");
		assertEquals (-1L, call (projection, "visibleIndexOfCoordinate", 1, 2), "expanded parent below collapsed ancestor");
		assertEquals (2L, call (projection, "visibleIndexOfCoordinate", -1, 2), "visible root sibling");
		assertEquals (generation, call (topology, "generation"), "lookup cannot mutate topology");
		assertEquals (2, call (topology, "materializedCount"), "cold coordinates do not create facades");

		call (topology, "flag", 0, expandedBit (), true);
		assertEquals (2L, call (projection, "visibleIndexOfCoordinate", 0, 0));
		assertEquals (6L, call (projection, "visibleIndexOfCoordinate", 1, 2));
		call (topology, "flag", 1, expandedBit (), false);
		assertEquals (-1L, call (projection, "visibleIndexOfCoordinate", 1, 2));
		assertEquals (3L, call (projection, "visibleIndexOfCoordinate", 0, 1), "the collapsed parent itself remains visible");
		call (topology, "flag", 0, expandedBit (), false);
		assertEquals (-1L, call (projection, "visibleIndexOfCoordinate", 0, 3));
	}

	@Test
	public void longRootOffsetsAndInvalidCoordinateContractsRemainExact () throws Exception {
		Object topology = construct ("VirtualTreeTopology");
		call (topology, "setChildCount", -1, Integer.MAX_VALUE);
		call (topology, "bind", 0, -1, 1);
		call (topology, "setChildCount", 0, Integer.MAX_VALUE);
		call (topology, "flag", 0, expandedBit (), true);
		Object projection = construct ("VirtualTreeVisibleProjection", topology);
		assertEquals (2L + Integer.MAX_VALUE, call (projection, "visibleIndexOfCoordinate", -1, 2));
		assertEquals (1L + Integer.MAX_VALUE, call (projection, "visibleIndexOfCoordinate", 0, Integer.MAX_VALUE - 1));
		call (topology, "flag", 0, expandedBit (), false);
		assertEquals (-1L, call (projection, "visibleIndexOfCoordinate", 0, Integer.MAX_VALUE - 1));
		assertEquals ((long)Integer.MAX_VALUE - 1, call (projection, "visibleIndexOfCoordinate", -1, Integer.MAX_VALUE - 1));
		assertThrows (IllegalArgumentException.class, () -> call (projection, "visibleIndexOfCoordinate", 0, -1));
		assertThrows (IllegalArgumentException.class, () -> call (projection, "visibleIndexOfCoordinate", 0, Integer.MAX_VALUE));
		call (topology, "bind", 1, -1, 3);
		assertThrows (IllegalArgumentException.class, () -> call (projection, "visibleIndexOfCoordinate", 1, 0), "unknown child count remains invalid");
		assertEquals (2, call (topology, "materializedCount"));
	}

	@Test
	public void seededExpansionHistoriesMatchIndependentEagerPreorder () throws Exception {
		long bit = expandedBit ();
		for (int seed = 0; seed < 32; seed++) {
			Random random = new Random (0x535754564953L + seed);
			List<Node> nodes = new ArrayList<> ();
			Node root = makeNode (random, -1, 0, nodes);
			Object topology = construct ("VirtualTreeTopology");
			bind (topology, root, bit);
			Object projection = construct ("VirtualTreeVisibleProjection", topology);
			for (int step = 0; step < 32; step++) {
				if (nodes.size () > 1) {
					Node changed = nodes.get (1 + random.nextInt (nodes.size () - 1));
					changed.expanded = !changed.expanded;
					call (topology, "flag", changed.id, bit, changed.expanded);
				}
				Map<Coordinate, Long> visible = new HashMap<> ();
				enumerate (root, visible);
				assertEquals ((long)visible.size (), call (projection, "visibleRowCount"));
				for (Node parent : nodes) {
					for (int index = 0; index < parent.count; index++) {
						Coordinate coordinate = new Coordinate (parent.id, index);
						assertEquals (visible.getOrDefault (coordinate, -1L),
								call (projection, "visibleIndexOfCoordinate", parent.id, index),
								"seed=" + seed + " step=" + step + " coordinate=" + coordinate);
					}
				}
				assertEquals (nodes.size () - 1, call (topology, "materializedCount"));
			}
		}
	}

	private static Node makeNode (Random random, int id, int depth, List<Node> nodes) {
		Node node = new Node (id, depth == 4 ? 0 : 2 + random.nextInt (4), id == -1 || random.nextBoolean ());
		nodes.add (node);
		for (int index = 0; index < node.count; index++) {
			if (random.nextBoolean ()) node.children.put (index, makeNode (random, nodes.size () - 1, depth + 1, nodes));
		}
		return node;
	}

	private static void bind (Object topology, Node node, long bit) throws Exception {
		call (topology, "setChildCount", node.id, node.count);
		for (var entry : node.children.entrySet ()) {
			call (topology, "bind", entry.getValue ().id, node.id, entry.getKey ());
			bind (topology, entry.getValue (), bit);
			call (topology, "flag", entry.getValue ().id, bit, entry.getValue ().expanded);
		}
	}

	private static void enumerate (Node parent, Map<Coordinate, Long> visible) {
		if (!parent.expanded) return;
		for (int index = 0; index < parent.count; index++) {
			visible.put (new Coordinate (parent.id, index), (long)visible.size ());
			Node child = parent.children.get (index);
			if (child != null) enumerate (child, visible);
		}
	}
}

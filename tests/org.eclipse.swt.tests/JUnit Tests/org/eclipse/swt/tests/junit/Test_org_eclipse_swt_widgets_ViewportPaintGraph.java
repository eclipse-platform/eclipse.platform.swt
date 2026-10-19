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

import org.junit.jupiter.api.*;

public class Test_org_eclipse_swt_widgets_ViewportPaintGraph {

	@Test
	public void test_horizontalWindowMatchesIndependentPixelRaster () throws Exception {
		Random random = new Random (0x48574e44);
		for (int round = 0; round < 180; round++) {
			int count = random.nextInt (33);
			long [] widths = new long [count];
			int [] order = new int [count];
			int total = 0;
			for (int column = 0; column < count; column++) {
				widths [column] = random.nextInt (4) == 0 ? 0 : random.nextInt (65);
				total += (int)widths [column];
				order [column] = column;
			}
			for (int i = count - 1; i > 0; i--) {
				int swap = random.nextInt (i + 1), value = order [i];
				order [i] = order [swap];
				order [swap] = value;
			}
			// Independent oracle: enumerate every small logical pixel, never binary-search.
			int [] pixels = new int [total];
			int [] starts = new int [count];
			int cursor = 0;
			List<Integer> positive = new ArrayList<> ();
			for (int column : order) {
				starts [column] = cursor;
				if (widths [column] > 0) positive.add (column);
				for (int pixel = 0; pixel < widths [column]; pixel++) pixels [cursor++] = column;
			}
			HorizontalFixture fixture = new HorizontalFixture (widths, order);
			assertEquals ((long)total, fixture.call ("contentWidth"));
			for (int step = 0; step < 8; step++) {
				long request = random.nextInt (total + 101) - 50L;
				int viewport = random.nextInt (101), overscan = random.nextInt (17);
				boolean rtl = (step & 1) != 0;
				fixture.call ("setViewport", request, viewport, overscan, rtl);
				int origin = (int)Math.max (0, Math.min (request, Math.max (0, total - viewport)));
				int start = viewport == 0 ? origin : Math.max (0, origin - overscan);
				int end = viewport == 0 ? origin : Math.min (total, origin + viewport + overscan);
				assertEquals ((long)origin, fixture.call ("origin"));
				assertEquals ((long)start, fixture.call ("paintStart"));
				assertEquals ((long)end, fixture.call ("paintEndExclusive"));
				assertEquals (end - start, fixture.call ("residentWidth"));
				assertTrue (end - start <= viewport + 2 * overscan);
				List<Integer> expected = new ArrayList<> ();
				for (int column : positive) {
					boolean occupied = false;
					for (int pixel = start; pixel < end && !occupied; pixel++) occupied = pixels [pixel] == column;
					if (occupied) expected.add (column);
				}
				List<Integer> actual = new ArrayList<> ();
				int first = (int)fixture.call ("firstSlot"), last = (int)fixture.call ("endSlot");
				for (int slot = first; slot < last; slot++) actual.add ((int)fixture.call ("columnAtSlot", slot));
				assertEquals (expected, actual, "resident columns must exclude every zero-width/offscreen column");
				for (int column = 0; column < count; column++) {
					assertEquals ((long)starts [column], fixture.call ("columnStart", column));
					assertEquals (widths [column], fixture.call ("columnWidth", column));
					long physicalLeft = rtl ? origin + viewport - starts [column] - widths [column] : starts [column] - origin;
					assertEquals (physicalLeft, fixture.call ("viewportX", column));
					int firstPixel = -1, lastPixel = -1;
					for (int pixel = start; pixel < end; pixel++) {
						if (pixels [pixel] == column) {
							if (firstPixel < 0) firstPixel = pixel;
							lastPixel = pixel;
						}
					}
					int projectedWidth = firstPixel < 0 ? 0 : lastPixel + 1 - firstPixel;
					int projectedX = firstPixel < 0 ? -1 : rtl ? end - 1 - lastPixel : firstPixel - start;
					long source = firstPixel < 0 ? -1 : rtl ? starts [column] + widths [column] - 1 - lastPixel : firstPixel - starts [column];
					assertEquals (projectedWidth, fixture.call ("residentColumnWidth", column));
					assertEquals (projectedX, fixture.call ("residentX", column));
					assertEquals (source, fixture.call ("sourceOffset", column));
					if (firstPixel >= 0) {
						long projectedViewportLeft = (int)fixture.call ("residentViewportX") + projectedX;
						assertEquals (physicalLeft + source, projectedViewportLeft);
					}
				}
				for (int x = -1; x <= viewport; x++) {
					int logical = origin + (rtl ? viewport - 1 - x : x);
					boolean hit = x >= 0 && x < viewport && logical >= 0 && logical < pixels.length;
					assertEquals (hit ? (long)logical : -1L, fixture.call ("logicalAtViewportPixel", x));
					assertEquals (hit ? pixels [logical] : -1, fixture.call ("columnAtViewportPixel", x));
				}
			}
		}
	}

	@Test
	public void test_horizontalWindowKeepsLongPrecisionAtHugePartialColumns () throws Exception {
		long firstWidth = Long.MAX_VALUE - 1000;
		HorizontalFixture fixture = new HorizontalFixture (
				new long [] {firstWidth, 0, 800, 200}, new int [] {0, 1, 2, 3});
		fixture.call ("setViewport", firstWidth - 7, 32, 3, false);
		assertEquals (Long.MAX_VALUE, fixture.call ("contentWidth"));
		assertEquals (firstWidth - 10, fixture.call ("paintStart"));
		assertEquals (firstWidth + 28, fixture.call ("paintEndExclusive"));
		assertEquals (38, fixture.call ("residentWidth"));
		assertEquals (10, fixture.call ("residentColumnWidth", 0));
		assertEquals (28, fixture.call ("residentColumnWidth", 2));
		assertEquals (7L, fixture.call ("viewportX", 2));
		assertEquals (0, fixture.call ("columnAtViewportPixel", 6));
		assertEquals (2, fixture.call ("columnAtViewportPixel", 7));
		assertEquals (firstWidth, fixture.call ("logicalAtViewportPixel", 7));
		fixture.call ("setViewport", firstWidth - 7, 32, 3, true);
		assertEquals (28, fixture.call ("residentX", 0));
		assertEquals (0, fixture.call ("residentX", 2));
		assertEquals (0L, fixture.call ("sourceOffset", 0));
		assertEquals (772L, fixture.call ("sourceOffset", 2));
		assertEquals (2, fixture.call ("columnAtViewportPixel", 24));
		assertEquals (0, fixture.call ("columnAtViewportPixel", 25));
		fixture.call ("setViewport", Long.MAX_VALUE, 32, 20, false);
		assertEquals (Long.MAX_VALUE - 32, fixture.call ("origin"));
		assertEquals (Long.MAX_VALUE, fixture.call ("paintEndExclusive"));
		assertEquals (52, fixture.call ("residentWidth"));
		assertEquals (Long.MAX_VALUE - 1, fixture.call ("logicalAtViewportPixel", 31));
		assertEquals (-1L, fixture.call ("logicalAtViewportPixel", 32));

		// Actual SWT column widths are int; their aggregate must still be long.
		fixture.call ("setColumns", new long [] {Integer.MAX_VALUE, Integer.MAX_VALUE, 19}, new int [] {2, 0, 1});
		fixture.call ("setViewport", 2L * Integer.MAX_VALUE, 16, 4, false);
		assertEquals (2L * Integer.MAX_VALUE + 19, fixture.call ("contentWidth"));
		assertEquals (23, fixture.call ("residentWidth"), "trailing overscan clips at the logical content end");
		assertEquals (1, fixture.call ("columnAtViewportPixel", 0));
	}

	@Test
	public void test_horizontalWindowRejectsInvalidGeometryAtomically () throws Exception {
		HorizontalFixture fixture = new HorizontalFixture (new long [] {20, 30}, new int [] {1, 0});
		fixture.call ("setViewport", 8L, 10, 2, true);
		long generation = (long)fixture.call ("generation"), geometry = (long)fixture.call ("geometryGeneration");
		Object [] arrays = fixture.arrays ();
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setColumns", new long [] {20, -1}, new int [] {0, 1}));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setColumns", new long [] {20, 30}, new int [] {0, 0}));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setColumns", new long [] {20, 30}, new int [] {0, 2}));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setColumns", new long [] {20, 30}, new int [] {0}));
		assertThrows (ArithmeticException.class, () -> fixture.call ("setColumns", new long [] {Long.MAX_VALUE, 1}, new int [] {1, 0}));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setViewport", 0L, -1, 0, false));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setViewport", 0L, 1, -1, false));
		assertThrows (IllegalArgumentException.class, () -> fixture.call ("setViewport", 0L, Integer.MAX_VALUE, 1, false));
		assertEquals (50L, fixture.call ("contentWidth"));
		assertEquals (8L, fixture.call ("origin"));
		assertEquals (true, fixture.call ("rightToLeft"));
		assertEquals (generation, fixture.call ("generation"));
		assertEquals (geometry, fixture.call ("geometryGeneration"));
		assertArrayEquals (arrays, fixture.arrays ());
	}

	@Test
	public void test_horizontalWindowReusesDirectoryAndSkipsLongZeroWidthRuns () throws Exception {
		int count = 100_003;
		long [] widths = new long [count];
		int [] order = new int [count];
		for (int i = 0; i < count; i++) order [i] = i;
		widths [0] = 5;
		widths [count - 2] = 1_000_000;
		widths [count - 1] = 8;
		HorizontalFixture fixture = new HorizontalFixture (widths, order);
		long geometry = (long)fixture.call ("geometryGeneration");
		Object [] arrays = fixture.arrays ();
		assertEquals (3, ((long [])arrays [3]).length, "only positive-width ends belong in the seek directory");
		assertEquals (false, fixture.call ("setColumns", widths.clone (), order.clone ()));
		for (int step = 0; step < 2_000; step++) {
			fixture.call ("setViewport", step * 499L, 40, 8, (step & 1) != 0);
			int first = (int)fixture.call ("firstSlot"), end = (int)fixture.call ("endSlot");
			assertTrue (end - first <= 3);
			assertTrue ((int)fixture.call ("residentWidth") <= 56);
		}
		Object [] after = fixture.arrays ();
		for (int i = 0; i < arrays.length; i++) assertSame (arrays [i], after [i], "scrolling must not rebuild column prefixes");
		assertEquals (geometry, fixture.call ("geometryGeneration"));
		widths [0] = 99;
		order [0] = count - 1;
		assertEquals (5L, fixture.call ("columnWidth", 0), "caller arrays must not mutate retained geometry");
		assertEquals (0, fixture.call ("columnAtDisplayIndex", 0));
	}

	@Test
	public void test_horizontalWindowRevealEmptyResizeAndReorder () throws Exception {
		HorizontalFixture fixture = new HorizontalFixture (new long [] {20, 0, 200, 30}, new int [] {3, 1, 0, 2});
		fixture.call ("setViewport", 0L, 40, 3, false);
		assertEquals (true, fixture.call ("ensureColumnVisible", 0));
		assertEquals (10L, fixture.call ("origin"));
		assertEquals (false, fixture.call ("ensureColumnVisible", 1));
		assertEquals (true, fixture.call ("ensureColumnVisible", 2));
		assertEquals (50L, fixture.call ("origin"));
		assertEquals (false, fixture.call ("ensureColumnVisible", 2));
		fixture.call ("setViewport", 70L, 0, 3, true);
		assertEquals (0, fixture.call ("residentWidth"));
		assertEquals (0, fixture.call ("endSlot"));
		assertEquals (-1, fixture.call ("columnAtViewportPixel", 0));
		assertEquals (false, fixture.call ("ensureColumnVisible", 3));
		fixture.call ("setViewport", 200L, 40, 3, true);
		fixture.call ("setColumns", new long [] {0, 0}, new int [] {1, 0});
		assertEquals (0L, fixture.call ("origin"));
		assertEquals (0, fixture.call ("residentWidth"));
		assertEquals (-1, fixture.call ("columnAtViewportPixel", 39));
		fixture.call ("setColumns", new long [] {10, 20}, new int [] {1, 0});
		assertEquals (40, fixture.call ("viewportWidth"));
		assertEquals (10, fixture.call ("residentViewportX"), "short RTL content aligns to the leading edge");
		assertEquals (-1, fixture.call ("columnAtViewportPixel", 9));
		assertEquals (0, fixture.call ("columnAtViewportPixel", 10));
		assertEquals (1, fixture.call ("columnAtViewportPixel", 39));
		long geometry = (long)fixture.call ("geometryGeneration");
		fixture.call ("setColumns", new long [] {10, 20}, new int [] {0, 1});
		assertEquals (geometry + 1, fixture.call ("geometryGeneration"));
		assertEquals (0, fixture.call ("columnAtViewportPixel", 39));
	}

	private static final class HorizontalFixture {
		final Object window;

		HorizontalFixture (long [] widths, int [] order) throws Exception {
			Class<?> type = Class.forName ("org.eclipse.swt.widgets.VirtualViewportPlanner");
			Constructor<?> constructor = type.getDeclaredConstructor ();
			constructor.setAccessible (true);
			Method horizontal = type.getDeclaredMethod ("horizontal");
			horizontal.setAccessible (true);
			window = horizontal.invoke (constructor.newInstance ());
			call ("setColumns", widths, order);
		}

		Object call (String name, Object... arguments) throws Exception {
			Class<?> [] types = new Class<?> [arguments.length];
			for (int i = 0; i < types.length; i++) {
				Object value = arguments [i];
				types [i] = value instanceof Integer ? int.class : value instanceof Long ? long.class
						: value instanceof Boolean ? boolean.class : value.getClass ();
			}
			Method method = window.getClass ().getDeclaredMethod (name, types);
			method.setAccessible (true);
			try {
				return method.invoke (window, arguments);
			} catch (InvocationTargetException exception) {
				if (exception.getCause () instanceof Error error) throw error;
				if (exception.getCause () instanceof Exception cause) throw cause;
				throw exception;
			}
		}

		Object [] arrays () throws Exception {
			String [] names = {"widths", "order", "starts", "positiveEnds", "positiveColumns"};
			Object [] values = new Object [names.length];
			for (int i = 0; i < names.length; i++) {
				Field field = window.getClass ().getDeclaredField (names [i]);
				field.setAccessible (true);
				values [i] = field.get (window);
			}
			return values;
		}
	}

	@Test
	public void test_affineViewportTranslationAndInverse() throws Exception {
		Class<?> type = Class.forName ("org.eclipse.swt.widgets.ViewportAffineTransform");
		Method translation = type.getDeclaredMethod ("translation", double.class, double.class);
		Method map = type.getDeclaredMethod ("map", double.class, double.class);
		Method inverse = type.getDeclaredMethod ("inverse");
		Method mapBounds = type.getDeclaredMethod (
				"mapBounds", long.class, long.class, long.class, long.class);
        for (Method method : new Method[]{translation, map, inverse, mapBounds}) {
            method.setAccessible(true);
        }

		Object transform = translation.invoke (null, -5_000_000d, -8_000_000d);
		Object point = map.invoke (transform, 5_000_010d, 8_000_020d);
		assertEquals (10d, component (point, "x"), 0d);
		assertEquals (20d, component (point, "y"), 0d);

		Object inverseTransform = inverse.invoke (transform);
		Object logical = map.invoke (inverseTransform, 10d, 20d);
		assertEquals (5_000_010d, component (logical, "x"), 0d);
		assertEquals (8_000_020d, component (logical, "y"), 0d);

		Object bounds = mapBounds.invoke (
				transform, 5_000_000L, 8_000_000L, 100L, 40L);
		assertEquals (0d, component (bounds, "x"), 0d);
		assertEquals (0d, component (bounds, "y"), 0d);
		assertEquals (100d, component (bounds, "width"), 0d);
		assertEquals (40d, component (bounds, "height"), 0d);
	}

	@Test
	public void test_paintGraphDirtyBoundsLayersAndDependencies() throws Exception {
		Class<?> graphType = Class.forName ("org.eclipse.swt.widgets.ViewportPaintGraph");
		Constructor<?> constructor = graphType.getDeclaredConstructor ();
		constructor.setAccessible (true);
		Object graph = constructor.newInstance ();

		Class<?> layers = Class.forName ("org.eclipse.swt.widgets.ViewportLayerState");
		int body = constant (layers, "BODY");
		int header = constant (layers, "HEADER");
		int feedback = constant (layers, "FEEDBACK");

		Method addNode = graphType.getDeclaredMethod (
				"addNode", long.class, int.class, long.class, long.class, long.class, long.class);
		Method addDependency = graphType.getDeclaredMethod ("addDependency", int.class, int.class);
		Method markAllClean = graphType.getDeclaredMethod ("markAllClean");
		Method invalidateBounds = graphType.getDeclaredMethod (
				"invalidateBounds",
				long.class, long.class, long.class, long.class, int.class);
		Method invalidateLayers = graphType.getDeclaredMethod ("invalidateLayers", int.class);
		Method isDirty = graphType.getDeclaredMethod ("isDirty", int.class);
		Method dirtyCount = graphType.getDeclaredMethod ("dirtyCount");
		for (Method method : new Method[] {
				addNode, addDependency, markAllClean, invalidateBounds,
				invalidateLayers, isDirty, dirtyCount}) {
			method.setAccessible (true);
		}

		int row0 = (Integer)addNode.invoke (graph, 1L, body, 0L, 0L, 500L, 20L);
		int row1 = (Integer)addNode.invoke (graph, 2L, body, 0L, 20L, 500L, 20L);
		int columnHeader = (Integer)addNode.invoke (graph, 3L, header, 0L, 0L, 500L, 24L);
		int hover = (Integer)addNode.invoke (graph, 4L, feedback, 0L, 20L, 500L, 20L);
		addDependency.invoke (graph, row1, hover);
		markAllClean.invoke (graph);

		invalidateBounds.invoke (graph, 0L, 20L, 500L, 20L, body);
		assertFalse ((Boolean)isDirty.invoke (graph, row0));
		assertTrue ((Boolean)isDirty.invoke (graph, row1));
		assertTrue ((Boolean)isDirty.invoke (graph, hover),
				"dependent feedback must become dirty with its source row");
		assertFalse ((Boolean)isDirty.invoke (graph, columnHeader));
		assertEquals (2, ((Integer)dirtyCount.invoke (graph)).intValue ());

		markAllClean.invoke (graph);
		invalidateLayers.invoke (graph, header);
		assertTrue ((Boolean)isDirty.invoke (graph, columnHeader));
		assertFalse ((Boolean)isDirty.invoke (graph, row0));
		assertFalse ((Boolean)isDirty.invoke (graph, row1));
		assertFalse ((Boolean)isDirty.invoke (graph, hover));
	}

	@Test
	public void test_layerPolicyVerticalScrollLeavesHeaderClean() throws Exception {
		Class<?> layerType = Class.forName ("org.eclipse.swt.widgets.ViewportLayerState");
		Constructor<?> constructor = layerType.getDeclaredConstructor ();
		constructor.setAccessible (true);
		Object state = constructor.newInstance ();
		Method initialize = layerType.getDeclaredMethod ("initialize", double.class, double.class);
		Method scrollTo = layerType.getDeclaredMethod ("scrollTo", double.class, double.class);
		initialize.setAccessible (true);
		scrollTo.setAccessible (true);

		int body = constant (layerType, "BODY");
		int frozen = constant (layerType, "FROZEN");
		int header = constant (layerType, "HEADER");
		int scrollbar = constant (layerType, "SCROLLBAR");
		int editor = constant (layerType, "EDITOR");
		int feedback = constant (layerType, "FEEDBACK");

		initialize.invoke (state, 0d, 0d);
		int verticalDirty = (Integer)scrollTo.invoke (state, 0d, 400d);
		// Match the shared ViewportRuntime contract: editor/feedback overlays track the body.
		assertEquals (body | frozen | editor | scrollbar | feedback, verticalDirty);
		assertEquals (0, verticalDirty & header,
				"vertical body scrolling must not repaint the stationary header plane");

		int horizontalDirty = (Integer)scrollTo.invoke (state, 120d, 400d);
		assertEquals (body | header | editor | scrollbar | feedback, horizontalDirty);
		assertEquals (0, scrollTo.invoke (state, 120d, 400d), "unchanged origin must not dirty layers");
	}

	private static int constant (Class<?> type, String name) throws Exception {
		Field field = type.getDeclaredField (name);
		field.setAccessible (true);
		return field.getInt (null);
	}

	private static double component (Object record, String name) throws Exception {
		Method method = record.getClass ().getDeclaredMethod (name);
		method.setAccessible (true);
		return ((Double)method.invoke (record)).doubleValue ();
	}
	@Test
	public void test_dirtyIntermediateDoesNotHideCleanDependant () throws Exception {
		DirtyFixture fixture = new DirtyFixture (3);
		fixture.edge (0, 1);
		fixture.edge (1, 2);
		fixture.call ("markClean", new Class<?>[] {int.class}, 2);
		fixture.invalidate (0);
		assertTrue (fixture.dirty (2), "a previously painted dependant must be invalidated again");
	}

	@Test
	public void test_partialPaintAndRepeatedInvalidationMatchReachabilityOracle () throws Exception {
		int count = 193; // Exercise multiple packed words and a partial final word.
		DirtyFixture fixture = new DirtyFixture (count);
		boolean [][] edges = new boolean [count][count];
		boolean [] expected = new boolean [count];
		Arrays.fill (expected, true);
		Random random = new Random (0x535754);
		for (int source = 0; source < count; source++) {
			for (int target = source + 1; target < count; target++) {
				if (random.nextInt (40) == 0) {
					edges [source][target] = true;
					fixture.edge (source, target);
					fixture.edge (source, target); // Duplicate edges must not amplify work.
				}
			}
		}
		for (int pass = 0; pass < 200; pass++) {
			for (int node = 0; node < count; node++) {
				if (random.nextInt (4) == 0) {
					fixture.call ("markClean", new Class<?>[] {int.class}, node);
					expected [node] = false;
				}
			}
			int seed = random.nextInt (count);
			fixture.invalidate (seed);
			boolean [] visited = new boolean [count];
			ArrayDeque<Integer> queue = new ArrayDeque<> ();
			queue.add (seed);
			while (!queue.isEmpty ()) {
				int node = queue.remove ();
                if (visited [node]) {
                    continue;
                }
				visited [node] = true;
				expected [node] = true;
				for (int target = 0; target < count; target++) {
                    if (edges [node][target]) {
                        queue.add(target);
                    }
				}
			}
			int dirtyCount = 0;
			for (int node = 0; node < count; node++) {
				assertEquals (expected [node], fixture.dirty (node), "pass=" + pass + " node=" + node);
                if (expected [node]) {
                    dirtyCount++;
                }
			}
			assertEquals (dirtyCount, fixture.call ("dirtyCount", new Class<?>[0]));
		}
		fixture.call ("markAllClean", new Class<?>[0]);
		fixture.call ("invalidateLayers", new Class<?>[] {int.class}, fixture.body);
		assertEquals (count, fixture.call ("dirtyCount", new Class<?>[0]));
	}

	private static final class DirtyFixture {
		final Class<?> type = Class.forName ("org.eclipse.swt.widgets.ViewportPaintGraph");
		final Object graph;
		final int body = constant (Class.forName ("org.eclipse.swt.widgets.ViewportLayerState"), "BODY");

		DirtyFixture (int count) throws Exception {
			Constructor<?> constructor = type.getDeclaredConstructor ();
			constructor.setAccessible (true);
			graph = constructor.newInstance ();
			for (int node = 0; node < count; node++) {
				call ("addNode", new Class<?>[] {long.class, int.class, long.class, long.class, long.class, long.class},
						(long)node, body, node * 10L, 0L, 10L, 10L);
			}
		}

		Object call (String name, Class<?>[] signature, Object... args) throws Exception {
			Method method = type.getDeclaredMethod (name, signature);
			method.setAccessible (true);
			return method.invoke (graph, args);
		}

		void edge (int source, int target) throws Exception {
			call ("addDependency", new Class<?>[] {int.class, int.class}, source, target);
		}

		void invalidate (int seed) throws Exception {
			call ("invalidateBounds", new Class<?>[] {long.class, long.class, long.class, long.class, int.class},
					seed * 10L, 0L, 10L, 10L, body);
		}

		boolean dirty (int node) throws Exception {
			return (Boolean)call ("isDirty", new Class<?>[] {int.class}, node);
		}
	}

}

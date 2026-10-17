/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.Test;

/** Contract regressions for the shared model and all three widget backends. */
public class Test_ViewportRewriteContracts {

	private static Object construct(String name, Object... arguments) throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.widgets." + name);
		Constructor<?> constructor = type.getDeclaredConstructor(types(arguments));
		constructor.setAccessible(true);
		return constructor.newInstance(arguments);
	}

	private static Class<?>[] types(Object[] arguments) {
		return Arrays.stream(arguments).map(value -> {
            if (value instanceof Integer) {
                return int.class;
            }
            if (value instanceof Long) {
                return long.class;
            }
            if (value instanceof Boolean) {
                return boolean.class;
            }
			return value.getClass();
		}).toArray(Class<?>[]::new);
	}

	private static Object call(Object receiver, String name, Object... arguments) throws Exception {
		Method method = receiver.getClass().getDeclaredMethod(name, types(arguments));
		method.setAccessible(true);
		try {
			return method.invoke(receiver, arguments);
		} catch (InvocationTargetException exception) {
            if (exception.getCause() instanceof Error error) {
                throw error;
            }
            if (exception.getCause() instanceof Exception cause) {
                throw cause;
            }
			throw exception;
		}
	}

	private static long stateBit(String name) throws Exception {
		Field field = Class.forName("org.eclipse.swt.widgets.VirtualItemState").getDeclaredField(name);
		field.setAccessible(true);
		return field.getLong(null);
	}

	@Test
	public void sparseProjectionKeepsTenMillionColdCoordinates() throws Exception {
		Object topology = construct("VirtualTreeTopology");
		call(topology, "setChildCount", -1, 10_000_000);
		call(topology, "bind", 0, -1, 5_000_000);
		call(topology, "setChildCount", 0, 1_000_000);
		call(topology, "flag", 0, stateBit("EXPANDED"), true);
		Object projection = construct("VirtualTreeVisibleProjection", topology);
		assertEquals(11_000_000L, call(projection, "visibleRowCount"));
		Object row = call(projection, "rowAt", 5_999_999L);
		assertEquals(0, call(row, "parentId"));
		assertEquals(999_998, call(row, "childIndex"));
		assertEquals(-1, call(row, "materializedId"));
		assertEquals(1, call(row, "depth"));
		Object[] window = (Object[])call(projection, "window", 10_999_990L, 26);
		assertEquals(10, window.length);
	}

	@Test
	public void deepProjectionUsesIterationAndRetainsHiddenState() throws Exception {
		int depth = 12_000;
		Object topology = construct("VirtualTreeTopology");
		call(topology, "setChildCount", -1, 1);
		for (int id = 0; id < depth; id++) {
			call(topology, "bind", id, id - 1, 0);
			call(topology, "setChildCount", id, id + 1 == depth ? 0 : 1);
		}
		long expanded = stateBit("EXPANDED");
        for (int id = depth - 1; id >= 0; id--) {
            call(topology, "flag", id, expanded, true);
        }
		Object projection = construct("VirtualTreeVisibleProjection", topology);
		assertEquals((long)depth, call(projection, "visibleRowCount"));
		Object last = call(projection, "rowAt", (long)depth - 1);
		assertEquals(depth - 1, call(last, "materializedId"));
		assertEquals(depth - 1, call(last, "depth"));
		assertEquals((long)depth - 1, call(projection, "visibleIndexOf", depth - 1));
		call(topology, "setChildCount", depth - 1, 1);
		assertEquals((long)depth + 1, call(projection, "visibleRowCount"));
		call(topology, "setChildCount", depth - 1, 0);
		call(topology, "flag", 0, expanded, false);
		assertEquals(1L, call(projection, "visibleRowCount"));
		assertEquals(-1L, call(projection, "visibleIndexOf", depth - 1));
		call(topology, "flag", 0, expanded, true);
		assertEquals((long)depth, call(projection, "visibleRowCount"));
	}

	@Test
	public void rejectedTopologyRebindPreservesCoordinatesAndAggregates() throws Exception {
		Object topology = construct("VirtualTreeTopology");
		call(topology, "setChildCount", -1, 2);
		call(topology, "bind", 0, -1, 0);
		call(topology, "bind", 1, -1, 1);
		call(topology, "setChildCount", 0, 1);
		call(topology, "bind", 2, 0, 0);
		call(topology, "setChildCount", 2, 0);
		call(topology, "flag", 0, stateBit("EXPANDED"), true);
		assertThrows(IllegalStateException.class, () -> call(topology, "bind", 0, -1, 1));
		assertThrows(IllegalArgumentException.class, () -> call(topology, "bind", 0, 2, 0));
		assertEquals(-1, call(topology, "parentId", 0));
		assertEquals(0, call(topology, "childIndex", 0));
		assertEquals(0, call(topology, "firstMaterializedChildId", -1));
		assertEquals(1, call(topology, "nextMaterializedSiblingId", 0));
		assertEquals(3L, call(topology, "visibleRowCount"));
	}

	@Test
	public void projectionMatchesPreorderAfterCountAndExpansionChanges() throws Exception {
		Object topology = construct("VirtualTreeTopology");
		call(topology, "setChildCount", -1, 17);
		int[] coordinates = {1, 4, 10, 15};
        for (int id = 0; id < coordinates.length; id++) {
            call(topology, "bind", id, -1, coordinates[id]);
        }
		Object projection = construct("VirtualTreeVisibleProjection", topology);
		Random random = new Random(0x535754);
		long expandedBit = stateBit("EXPANDED");
		for (int round = 0; round < 100; round++) {
			int[] counts = new int[coordinates.length];
			boolean[] expanded = new boolean[coordinates.length];
			for (int id = 0; id < coordinates.length; id++) {
				counts[id] = random.nextInt(8);
				expanded[id] = random.nextBoolean();
				call(topology, "setChildCount", id, counts[id]);
				call(topology, "flag", id, expandedBit, expanded[id]);
			}
			long index = 0;
			for (int root = 0; root < 17; root++) {
				int id = Arrays.binarySearch(coordinates, root);
				Object row = call(projection, "rowAt", index);
				assertEquals(-1, call(row, "parentId"));
				assertEquals(root, call(row, "childIndex"));
				assertEquals(id < 0 ? -1 : id, call(row, "materializedId"));
                if (id >= 0) {
                    assertEquals(index, call(projection, "visibleIndexOf", id));
                }
				index++;
				if (id >= 0 && expanded[id]) {
					for (int child = 0; child < counts[id]; child++) {
						row = call(projection, "rowAt", index++);
						assertEquals(id, call(row, "parentId"));
						assertEquals(child, call(row, "childIndex"));
						assertEquals(-1, call(row, "materializedId"));
						assertEquals(1, call(row, "depth"));
					}
				}
			}
			assertEquals(index, call(projection, "visibleRowCount"));
		}
	}

	@Test
	public void virtualTableTextDirectionPreservesSparseItemsAndExplicitPinning() throws Exception {
		Display display = Display.getCurrent();
		boolean ownsDisplay = display == null;
        if (ownsDisplay) {
            display = new Display();
        }
		try {
			Shell shell = new Shell(display);
			try {
				Table table = new Table(shell, SWT.VIRTUAL | SWT.CHECK);
				table.setItemCount(1_000_000);
				TableItem exposed = table.getItem(900_000);
				TableItem explicit = new TableItem(table, SWT.NONE, 3);
				exposed.setText("retained");
				exposed.setChecked(true);
				table.setTextDirection(SWT.AUTO_TEXT_DIRECTION);
				table.setTextDirection(SWT.RIGHT_TO_LEFT);
				table.setTextDirection(SWT.LEFT_TO_RIGHT);
				assertSame(exposed, table.getItem(900_001));
				assertEquals("retained", exposed.getText());
				assertTrue(exposed.getChecked());
				Field storageField = Table.class.getDeclaredField("virtualItems");
				storageField.setAccessible(true);
				Object storage = storageField.get(table);
				assertTrue((int)call(storage, "size") < 32);
				Method flag = storage.getClass().getDeclaredMethod("flagOfIdentity", Object.class, long.class);
				flag.setAccessible(true);
				assertEquals(true, flag.invoke(storage, explicit, stateBit("PINNED")));
			} finally {
				shell.dispose();
			}
		} finally {
            if (ownsDisplay) {
                display.dispose();
            }
		}
	}

	@Test
	public void virtualColumnsPackAndMeasureListenerKeepExposedIdentity() {
		Display display = Display.getCurrent();
		boolean ownsDisplay = display == null;
        if (ownsDisplay) {
            display = new Display();
        }
		try {
			Shell shell = new Shell(display);
			try {
				Table table = new Table(shell, SWT.VIRTUAL);
				table.setItemCount(4096);
				TableColumn column = new TableColumn(table, SWT.NONE);
				column.setText("Table header");
				TableItem item = table.getItem(3000);
				item.setText("Materialized width sample");
				table.addListener(SWT.MeasureItem, event -> event.width = Math.max(80, event.width));
				column.pack();
				assertTrue(column.getWidth() > 0);
				assertSame(item, table.getItem(3000));
				Tree tree = new Tree(shell, SWT.VIRTUAL);
				tree.setItemCount(4096);
				TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
				treeColumn.setText("Tree header");
				TreeItem treeItem = tree.getItem(3000);
				treeItem.setText("Materialized tree width sample");
				tree.addListener(SWT.MeasureItem, event -> event.width = Math.max(80, event.width));
				treeColumn.pack();
				assertTrue(treeColumn.getWidth() > 0);
				assertSame(treeItem, tree.getItem(3000));
			} finally {
				shell.dispose();
			}
		} finally {
            if (ownsDisplay) {
                display.dispose();
            }
		}
	}
	@Test
	public void viewportScrollbarVisibilityConvergesAcrossAxes() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.widgets.ViewportScrollLayout");
		Method solve = type.getDeclaredMethod(
				"solve",
				int.class, int.class, int.class,
				long.class, int.class, long.class,
				int.class, int.class, int.class, int.class);
		solve.setAccessible(true);

		Field autoField = type.getDeclaredField("AUTO");
		Field neverField = type.getDeclaredField("NEVER");
		autoField.setAccessible(true);
		neverField.setAccessible(true);
		int auto = autoField.getInt(null);
		int never = neverField.getInt(null);

		Object horizontalForcesVertical = solve.invoke(
				null, 100, 100, 20,
				4L, 25, 105L,
				16, 16, auto, auto);
		assertEquals(true, call(horizontalForcesVertical, "horizontalVisible"));
		assertEquals(true, call(horizontalForcesVertical, "verticalVisible"));
		assertEquals(true, call(horizontalForcesVertical, "cornerVisible"));
		assertEquals(84, call(horizontalForcesVertical, "bodyWidth"));
		assertEquals(64, call(horizontalForcesVertical, "bodyHeight"));
		assertEquals(3, call(horizontalForcesVertical, "visibleRows"));
		assertEquals(84, call(horizontalForcesVertical, "headerWidth"));

		Object verticalForcesHorizontal = solve.invoke(
				null, 100, 100, 20,
				5L, 20, 90L,
				16, 16, auto, auto);
		assertEquals(true, call(verticalForcesHorizontal, "verticalVisible"));
		assertEquals(true, call(verticalForcesHorizontal, "horizontalVisible"));
		assertEquals(84, call(verticalForcesHorizontal, "bodyWidth"));
		assertEquals(64, call(verticalForcesHorizontal, "bodyHeight"));

		Object policySuppressed = solve.invoke(
				null, 100, 100, 20,
				1_000_000L, 22, 10_000L,
				16, 16, never, never);
		assertEquals(false, call(policySuppressed, "horizontalVisible"));
		assertEquals(false, call(policySuppressed, "verticalVisible"));
		assertEquals(100, call(policySuppressed, "bodyWidth"));
		assertEquals(80, call(policySuppressed, "bodyHeight"));

		Object huge = solve.invoke(
				null, 640, 480, 24,
				Long.MAX_VALUE, 64, Long.MAX_VALUE,
				16, 16, auto, auto);
		assertEquals(Long.MAX_VALUE, call(huge, "estimatedContentHeight"));
		assertEquals(true, call(huge, "horizontalVisible"));
		assertEquals(true, call(huge, "verticalVisible"));
	}


	@Test
	public void sharedViewportRuntimeSolvesPixelsAndTracksLayerDirtiness() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.internal.ViewportRuntime");
		Object runtime = type.getDeclaredConstructor().newInstance();
		Method initialize = type.getMethod("initializeOrigin", double.class, double.class);
		Method scrollTo = type.getMethod("scrollTo", double.class, double.class);
		Method solvePixels = type.getMethod(
				"solvePixels",
				int.class, int.class, long.class, long.class,
				int.class, int.class, int.class, int.class);

		int auto = type.getField("AUTO").getInt(null);
		int never = type.getField("NEVER").getInt(null);
		int body = type.getField("BODY").getInt(null);
		int frozen = type.getField("FROZEN").getInt(null);
		int header = type.getField("HEADER").getInt(null);
		int editor = type.getField("EDITOR").getInt(null);
		int scrollbar = type.getField("SCROLLBAR").getInt(null);
		int feedback = type.getField("FEEDBACK").getInt(null);

		initialize.invoke(runtime, 0.0, 0.0);
		assertEquals(body | header | editor | scrollbar | feedback,
				scrollTo.invoke(runtime, 5.0, 0.0));
		assertEquals(body | frozen | editor | scrollbar | feedback,
				scrollTo.invoke(runtime, 5.0, 7.0));
		assertEquals(0, scrollTo.invoke(runtime, 5.0, 7.0));

		Object horizontalForcesVertical = solvePixels.invoke(
				null, 100, 100, 105L, 90L, 16, 16, auto, auto);
		assertEquals(true, call(horizontalForcesVertical, "horizontalVisible"));
		assertEquals(true, call(horizontalForcesVertical, "verticalVisible"));
		assertEquals(84, call(horizontalForcesVertical, "bodyWidth"));
		assertEquals(84, call(horizontalForcesVertical, "bodyHeight"));

		Object verticalForcesHorizontal = solvePixels.invoke(
				null, 100, 100, 90L, 105L, 16, 16, auto, auto);
		assertEquals(true, call(verticalForcesHorizontal, "horizontalVisible"));
		assertEquals(true, call(verticalForcesHorizontal, "verticalVisible"));

		Object suppressed = solvePixels.invoke(
				null, 100, 100, Long.MAX_VALUE, Long.MAX_VALUE,
				16, 16, never, never);
		assertEquals(false, call(suppressed, "horizontalVisible"));
		assertEquals(false, call(suppressed, "verticalVisible"));
		assertEquals(100, call(suppressed, "bodyWidth"));
		assertEquals(100, call(suppressed, "bodyHeight"));
	}


	@Test
	public void sharedViewportRowWindowOwnsFlatAndTreeVisibleOverscanMath() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.internal.ViewportRuntime$RowWindow");
		Object window = type.getDeclaredConstructor().newInstance();

		Method setLogicalCount = type.getMethod("setLogicalCount", long.class);
		Method setViewport = type.getMethod("setViewport", long.class, int.class);
		Method setOverscanRows = type.getMethod("setOverscanRows", int.class);
		Method insert = type.getMethod("insert", long.class, long.class);
		Method remove = type.getMethod("remove", long.class, long.class);
		Method ensureVisible = type.getMethod("ensureVisible", long.class);
		Method isVisible = type.getMethod("isVisible", long.class);
		Method isPaintCandidate = type.getMethod("isPaintCandidate", long.class);

		setLogicalCount.invoke(window, 10_000_000_000L);
		setViewport.invoke(window, 5_000_000_000L, 40);
		setOverscanRows.invoke(window, 8);

		assertEquals(5_000_000_000L, call(window, "firstVisible"));
		assertEquals(40, call(window, "visibleCount"));
		assertEquals(4_999_999_992L, call(window, "paintStart"));
		assertEquals(5_000_000_048L, call(window, "paintEndExclusive"));
		assertEquals(56, call(window, "paintCount"));
		assertEquals(true, isVisible.invoke(window, 5_000_000_039L));
		assertEquals(false, isVisible.invoke(window, 5_000_000_040L));
		assertEquals(true, isPaintCandidate.invoke(window, 4_999_999_992L));
		assertEquals(false, isPaintCandidate.invoke(window, 4_999_999_991L));

		insert.invoke(window, 0L, 7L);
		assertEquals(5_000_000_007L, call(window, "firstVisible"));
		remove.invoke(window, 0L, 3L);
		assertEquals(5_000_000_004L, call(window, "firstVisible"));

		ensureVisible.invoke(window, 7_000_000_000L);
		assertEquals(6_999_999_961L, call(window, "firstVisible"));
		assertEquals(true, isVisible.invoke(window, 7_000_000_000L));

		setLogicalCount.invoke(window, Long.MAX_VALUE);
		setViewport.invoke(window, Long.MAX_VALUE - 4, 40);
		assertEquals(Long.MAX_VALUE - 4, call(window, "firstVisible"));
		assertEquals(4, call(window, "visibleCount"));
		assertEquals(Long.MAX_VALUE, call(window, "paintEndExclusive"));

		assertThrows(IllegalArgumentException.class,
				() -> call(window, "setLogicalCount", -1L));
		assertThrows(IllegalArgumentException.class,
				() -> call(window, "setOverscanRows", -1));
	}



	@Test
	public void sharedViewportRowWindowRetainsRequestedCapacityAcrossClippingAndEmptyModels() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.internal.ViewportRuntime$RowWindow");
		Object window = type.getDeclaredConstructor().newInstance();

		call(window, "setLogicalCount", 100L);
		call(window, "setViewport", 95L, 40);
		assertEquals(95L, call(window, "firstVisible"));
		assertEquals(5, call(window, "visibleCount"),
				"actual coverage clips at the short logical tail");

		call(window, "ensureVisible", 50L);
		assertEquals(50L, call(window, "firstVisible"));
		assertEquals(40, call(window, "visibleCount"),
				"tail clipping must not erase requested viewport capacity");

		call(window, "setLogicalCount", 0L);
		assertEquals(0L, call(window, "firstVisible"));
		assertEquals(0, call(window, "visibleCount"));

		call(window, "setLogicalCount", 100L);
		call(window, "ensureVisible", 99L);
		assertEquals(60L, call(window, "firstVisible"),
				"empty-model clamping must retain the prior 40-row viewport capacity");
		assertEquals(40, call(window, "visibleCount"));
		assertEquals(true, call(window, "isVisible", 99L));
	}

	@Test
	public void sharedViewportRowWindowRejectsOverflowAtomically() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.internal.ViewportRuntime$RowWindow");
		Object window = type.getDeclaredConstructor().newInstance();

		call(window, "setLogicalCount", Long.MAX_VALUE - 2L);
		call(window, "setViewport", 100L, 20);
		long beforeCount = (long)call(window, "logicalCount");
		long beforeFirst = (long)call(window, "firstVisible");
		int beforeVisible = (int)call(window, "visibleCount");
		long beforeGeneration = (long)call(window, "generation");

		assertThrows(ArithmeticException.class,
				() -> call(window, "insert", 0L, 3L));

		assertEquals(beforeCount, call(window, "logicalCount"));
		assertEquals(beforeFirst, call(window, "firstVisible"),
				"rejected overflow must not shift the viewport origin");
		assertEquals(beforeVisible, call(window, "visibleCount"));
		assertEquals(beforeGeneration, call(window, "generation"),
				"rejected overflow must be observationally atomic");
	}

	@Test
	public void sharedViewportSelectionKeepsSparseAndComplementStateAcrossShifts() throws Exception {
		Class<?> type = Class.forName("org.eclipse.swt.internal.ViewportRuntime$Selection");
		Object selection = type.getDeclaredConstructor().newInstance();

		call(selection, "setLogicalCount", 1_000_000);
		call(selection, "setSelected", 10, true);
		call(selection, "selectRange", 100, 110);
		assertEquals(11, call(selection, "selectedCount"));
		assertEquals(2, call(selection, "rangeCount"));
		assertEquals(false, call(selection, "complementMode"));

		call(selection, "insert", 5, 3);
		assertEquals(true, call(selection, "isSelected", 13));
		assertEquals(true, call(selection, "isSelected", 103));
		assertEquals(1_000_003, call(selection, "logicalCount"));

		call(selection, "remove", 0, 2);
		assertEquals(true, call(selection, "isSelected", 11));
		assertEquals(true, call(selection, "isSelected", 101));
		assertEquals(1_000_001, call(selection, "logicalCount"));

		call(selection, "selectAll");
		assertEquals(true, call(selection, "complementMode"));
		assertEquals(1_000_001, call(selection, "selectedCount"));
		call(selection, "deselectRange", 500_000, 900_000);
		assertEquals(600_001, call(selection, "selectedCount"));
		assertEquals(1, call(selection, "rangeCount"));
		assertEquals(500_000, call(selection, "rangeStart", 0));
		assertEquals(900_000, call(selection, "rangeEndExclusive", 0));

		call(selection, "insert", 700_000, 5);
		assertEquals(600_001, call(selection, "selectedCount"),
				"inserted coordinates must remain unselected in complement mode");
		assertEquals(false, call(selection, "isSelected", 700_000));
		call(selection, "remove", 700_000, 5);
		assertEquals(600_001, call(selection, "selectedCount"));

		assertThrows(IllegalArgumentException.class,
				() -> call(selection, "setSelected", 1_000_001, true));
	}


}

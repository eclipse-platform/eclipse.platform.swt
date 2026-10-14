/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleTableEvent;
import org.eclipse.swt.accessibility.AccessibleTableListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Table
 *
 * @see org.eclipse.swt.widgets.Table
 */
public class Test_org_eclipse_swt_widgets_Table extends Test_org_eclipse_swt_widgets_Composite {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	makeCleanEnvironment(false); // by default, use multi-select table.
}


private static boolean isGtk3() throws Exception {
	return "gtk".equals(SWT.getPlatform())
			&& !Class.forName("org.eclipse.swt.internal.gtk.GTK").getField("GTK4").getBoolean(null);
}

private Table newGtkViewportTable(int style, String mode) {
	String property = "org.eclipse.swt.internal.gtk.virtualTableBoundedView";
	String previous = System.getProperty(property);
	System.setProperty(property, mode);
	try {
		return new Table(shell, style);
	} finally {
		if (previous == null) System.clearProperty(property);
		else System.setProperty(property, previous);
	}
}

private void requireBoundedGtkView(Table table) throws Exception {
	Method bounded = Table.class.getDeclaredMethod("usesBoundedVirtualView");
	bounded.setAccessible(true);
	assertTrue((Boolean) bounded.invoke(table), "the explicitly enabled table must attach a bounded viewport model");
}

/** The native owner is sampled independently of Java's sparse facade storage. */
private static int attachedGtkTableRows(Table table) throws Exception {
	Class<?> os = Class.forName("org.eclipse.swt.internal.gtk.OS");
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	long widget = Table.class.getField("handle").getLong(table);
	long[] model = {0};
	os.getMethod("g_object_get", long.class, byte[].class, long[].class, long.class)
			.invoke(null, widget, new byte[] {109, 111, 100, 101, 108, 0}, model, 0L);
	assertTrue(model[0] != 0);
	try {
		return (Integer) gtk.getMethod("gtk_tree_model_iter_n_children", long.class, long.class)
				.invoke(null, model[0], 0L);
	} finally {
		os.getMethod("g_object_unref", long.class).invoke(null, model[0]);
	}
}

private Table newGtkHorizontalTable(int style, boolean horizontal) {
	String property = "org.eclipse.swt.internal.gtk.virtualHorizontalViewport";
	String previous = System.getProperty(property);
	System.setProperty(property, Boolean.toString(horizontal));
	try {
		return newGtkViewportTable(style, "true");
	} finally {
		if (previous == null) System.clearProperty(property);
		else System.setProperty(property, previous);
	}
}

private static Object tableInternal(Table table, String name) throws Exception {
	Method method = Table.class.getDeclaredMethod(name);
	method.setAccessible(true);
	return method.invoke(table);
}

private static long tableLong(Table table, String name) throws Exception {
	Field field = Table.class.getDeclaredField(name);
	field.setAccessible(true);
	return field.getLong(table);
}

/** Observe native GdkWindows, not Java's proposed column widths or planner. */
private static List<Integer> nativeTableSurfaceWidths(Table table) throws Exception {
	Class<?> gtk3 = Class.forName("org.eclipse.swt.internal.gtk3.GTK3");
	Class<?> gdk = Class.forName("org.eclipse.swt.internal.gtk.GDK");
	Class<?> os = Class.forName("org.eclipse.swt.internal.gtk.OS");
	long window = (Long) gtk3.getMethod("gtk_widget_get_window", long.class)
			.invoke(null, Table.class.getField("handle").getLong(table));
	assertTrue(window != 0, "the native surface observer requires a realized GtkTreeView");
	List<Integer> widths = new ArrayList<>();
	List<Long> pending = new ArrayList<>();
	pending.add(window);
	for (int index = 0; index < pending.size(); index++) {
		long children = (Long) gdk.getMethod("gdk_window_get_children", long.class).invoke(null, pending.get(index));
		try {
			for (long node = children; node != 0; node = (Long) os.getMethod("g_list_next", long.class).invoke(null, node)) {
				long child = (Long) os.getMethod("g_list_data", long.class).invoke(null, node);
				widths.add((Integer) gdk.getMethod("gdk_window_get_width", long.class).invoke(null, child));
				pending.add(child);
			}
		} finally {
			os.getMethod("g_list_free", long.class).invoke(null, children);
		}
	}
	assertTrue(widths.size() >= 2, "both GtkTreeView header and body native windows must be observed");
	return widths;
}

private static Rectangle nativeHeaderContent(TableColumn column) throws Exception {
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Class<?> gtk3 = Class.forName("org.eclipse.swt.internal.gtk3.GTK3");
	Class<?> allocationClass = Class.forName("org.eclipse.swt.internal.gtk.GtkAllocation");
	Field button = TableColumn.class.getDeclaredField("buttonHandle");
	button.setAccessible(true);
	long child = (Long) gtk3.getMethod("gtk_bin_get_child", long.class).invoke(null, button.getLong(column));
	Object allocation = allocationClass.getConstructor().newInstance();
	gtk.getMethod("gtk_widget_get_allocation", long.class, allocationClass).invoke(null, child, allocation);
	return new Rectangle(allocationClass.getField("x").getInt(allocation), allocationClass.getField("y").getInt(allocation),
			allocationClass.getField("width").getInt(allocation), allocationClass.getField("height").getInt(allocation));
}

@Test
public void test_virtualGtkHorizontalDefaultIsolationAndShortContentParity() throws Exception {
	if (!isGtk3()) return;
	for (int direction : new int[] {SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT}) {
		Table reference = newGtkHorizontalTable(SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL | direction, false);
		Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL | direction, true);
		assertFalse((Boolean) tableInternal(reference, "usesBoundedHorizontalView"));
		assertTrue((Boolean) tableInternal(projected, "usesBoundedHorizontalView"));
		for (Table current : new Table[] {reference, projected}) {
			current.setBounds(current == reference ? 0 : 520, 0, 500, 220);
			current.setHeaderVisible(true);
			for (int index = 0; index < 2; index++) {
				TableColumn column = new TableColumn(current, SWT.LEFT);
				column.setText("Heading " + index);
				column.setWidth(100);
			}
			current.getColumn(1).setAlignment(SWT.CENTER);
			assertEquals(SWT.CENTER, current.getColumn(1).getAlignment(), "alignment must work before the first directory rebuild");
			current.setItemCount(2);
			current.getItem(0).setText(new String[] {"first", "second"});
		}
		shell.setSize(1040, 300);
		shell.open();
		SwtTestUtil.processEvents();
		for (int index = 0; index < 2; index++) {
			assertEquals(reference.getColumn(index).getWidth(), projected.getColumn(index).getWidth(),
					"requested widths and GTK's allocated last-column expansion have different lifetimes");
			assertEquals(reference.getItem(0).getBounds(index), projected.getItem(0).getBounds(index));
			assertEquals(nativeHeaderContent(reference.getColumn(index)), nativeHeaderContent(projected.getColumn(index)),
					"LTR/RTL short-content header allocation must match native GTK");
		}
		reference.getColumn(1).setAlignment(SWT.RIGHT);
		projected.getColumn(1).setAlignment(SWT.RIGHT);
		assertEquals(SWT.RIGHT, reference.getColumn(1).getAlignment(), "the default path must retain setAlignment");
		assertEquals(SWT.RIGHT, projected.getColumn(1).getAlignment());
		reference.dispose();
		projected.dispose();
	}
}

@Test
public void test_virtualGtkHorizontalBoundsNativeHeaderAndBodyAtDistantColumns() throws Exception {
	if (!isGtk3()) return;
	Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, true);
	projected.setBounds(0, 0, 360, 220);
	projected.setHeaderVisible(true);
	for (int width : new int[] {2_000_000, 3_000_000}) {
		TableColumn column = new TableColumn(projected, SWT.LEFT);
		column.setText("Full logical heading");
		column.setWidth(width);
	}
	projected.setItemCount(1_000_000);
	projected.setSelection(new int[] {12, 900_000});
	Table reference = newGtkHorizontalTable(SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL, false);
	reference.setBounds(380, 0, 360, 220);
	reference.setHeaderVisible(true);
	TableColumn referenceColumn = new TableColumn(reference, SWT.NONE);
	referenceColumn.setWidth(2048);
	reference.setItemCount(4);
	shell.setSize(780, 300);
	shell.open();
	SwtTestUtil.processEvents();
	assertTrue(nativeTableSurfaceWidths(reference).stream().anyMatch(width -> width > 360 + 128),
			"the native observer must reject an ordinary wide GtkTreeView surface");
	for (int origin : new int[] {900_000, 2_500_000, 1_999_900, 0}) {
		projected.getHorizontalBar().setSelection(origin);
		projected.setTopIndex(900_000);
		SwtTestUtil.processEvents();
		assertEquals(origin, projected.getHorizontalBar().getSelection());
		assertEquals(900_000, projected.getTopIndex());
		assertEquals(2_000_000, projected.getColumn(0).getWidth());
		assertEquals(3_000_000, projected.getColumn(1).getWidth());
		assertEquals(2, projected.getColumnCount());
		assertTrue(nativeTableSurfaceWidths(projected).stream().allMatch(width -> width <= 360 + 128),
				"a single huge intersecting column must not allocate a huge header or body window");
		assertTrue(attachedGtkTableRows(projected) < 128);
		assertArrayEquals(new int[] {12, 900_000}, projected.getSelectionIndices());
		Rectangle row = projected.getItem(900_000).getBounds(origin < 2_000_000 ? 0 : 1);
		assertSame(projected.getItem(900_000), projected.getItem(new Point(100, row.y + row.height / 2)));
	}
	projected.setColumnOrder(new int[] {1, 0});
	projected.showColumn(projected.getColumn(0));
	SwtTestUtil.processEvents();
	assertArrayEquals(new int[] {1, 0}, projected.getColumnOrder());
	assertTrue(nativeTableSurfaceWidths(projected).stream().allMatch(width -> width <= 360 + 128));
	projected.getColumn(0).setWidth(0);
	projected.getColumn(1).setWidth(0);
	SwtTestUtil.processEvents();
	assertEquals(0, projected.getColumn(0).getWidth());
	assertEquals(0, projected.getColumn(1).getWidth());
	assertTrue(nativeTableSurfaceWidths(projected).stream().allMatch(width -> width <= 360 + 128),
			"zero logical widths must not retain a previous native content width");
}

@Test
public void test_virtualGtkHorizontalRetainsCrossRowRendererAllocation() throws Exception {
	if (!isGtk3()) return;
	org.eclipse.swt.graphics.Image small = new org.eclipse.swt.graphics.Image(shell.getDisplay(), 7, 9);
	org.eclipse.swt.graphics.Image large = new org.eclipse.swt.graphics.Image(shell.getDisplay(), 41, 19);
	try {
		for (int direction : new int[] {SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT}) {
			Table reference = newGtkHorizontalTable(SWT.VIRTUAL | SWT.CHECK | SWT.H_SCROLL | direction, false);
			Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.CHECK | SWT.H_SCROLL | direction, true);
			for (Table current : new Table[] {reference, projected}) {
				current.setBounds(current == reference ? 0 : 520, 0, 500, 220);
				for (int alignment : new int[] {SWT.LEFT, SWT.CENTER, SWT.RIGHT}) {
					TableColumn column = new TableColumn(current, alignment);
					column.setWidth(150);
				}
				current.setItemCount(2);
				for (int row = 0; row < 2; row++) {
					current.getItem(row).setText(new String[] {row == 0 ? "short" : "longer first column", "center " + row, "right " + row});
					for (int col = 0; col < 3; col++) current.getItem(row).setImage(col, row == 0 ? small : large);
				}
			}
			shell.setSize(1040, 300);
			shell.open();
			SwtTestUtil.processEvents();
			for (int row = 0; row < 2; row++) {
				for (int col = 0; col < 3; col++) {
					assertEquals(reference.getItem(row).getImageBounds(col), projected.getItem(row).getImageBounds(col));
					assertEquals(reference.getItem(row).getTextBounds(col), projected.getItem(row).getTextBounds(col),
							"the projected allocation must retain GTK's column-wide renderer context");
				}
			}
			reference.dispose();
			projected.dispose();
		}
	} finally {
		small.dispose();
		large.dispose();
	}
}

@Test
public void test_virtualGtkHorizontalDefersRendererRepackDuringMeasure() throws Exception {
	if (!isGtk3()) return;
	Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.CHECK | SWT.H_SCROLL, true);
	projected.setBounds(0, 0, 360, 220);
	TableColumn first = new TableColumn(projected, SWT.LEFT);
	first.setWidth(800);
	TableColumn changed = new TableColumn(projected, SWT.LEFT);
	changed.setWidth(500);
	projected.setItemCount(2);
	projected.getItem(0).setText(new String[] {"first", "second"});
	boolean[] mutated = {false};
	projected.addListener(SWT.MeasureItem, event -> {
		if (mutated[0]) return;
		mutated[0] = true;
		changed.setAlignment(SWT.RIGHT);
		TableColumn inserted = new TableColumn(projected, SWT.LEFT, 0);
		inserted.setWidth(90);
		changed.dispose();
	});
	shell.setSize(400, 280);
	shell.open();
	SwtTestUtil.processEvents();
	assertTrue(mutated[0], "a genuine native renderer measurement must exercise the reentrant mutation");
	assertEquals(2, projected.getColumnCount());
	assertSame(first, projected.getColumn(1));
	assertEquals(90, projected.getColumn(0).getWidth());
	Field pending = Table.class.getDeclaredField("virtualRenderersDirty");
	pending.setAccessible(true);
	assertFalse(pending.getBoolean(projected), "physical renderer repacking must finish after the callback unwinds");
	assertEquals(0, tableLong(projected, "virtualCallbackDepth"));
	assertTrue(nativeTableSurfaceWidths(projected).stream().allMatch(width -> width <= 360 + 128));
}

@Test
public void test_virtualGtkHorizontalFarBoundsSaturateWithoutWrapping() throws Exception {
	if (!isGtk3()) return;
	for (int direction : new int[] {SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT}) {
		Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.H_SCROLL | direction, true);
		projected.setBounds(0, 0, 360, 220);
		for (int index = 0; index < 3; index++) {
			TableColumn column = new TableColumn(projected, SWT.LEFT);
			column.setWidth(1_200_000_000);
		}
		projected.setItemCount(1);
		TableItem item = projected.getItem(0);
		item.setText(new String[] {"first", "second", "far"});
		shell.setSize(400, 280);
		shell.open();
		SwtTestUtil.processEvents();
		Method origin = Table.class.getDeclaredMethod("virtualSetHorizontalOrigin", long.class);
		origin.setAccessible(true);
		origin.invoke(projected, 0L);
		SwtTestUtil.processEvents();
		for (Rectangle bounds : new Rectangle[] {item.getBounds(2), item.getTextBounds(2), item.getImageBounds(2)}) {
			assertEquals(Integer.MAX_VALUE, bounds.x, "far logical X must saturate after mirroring and renderer offsets");
			assertTrue(bounds.width >= 0);
		}
		origin.invoke(projected, 2_500_000_000L);
		SwtTestUtil.processEvents();
		for (Rectangle bounds : new Rectangle[] {item.getBounds(0), item.getTextBounds(0), item.getImageBounds(0)}) {
			assertEquals(Integer.MIN_VALUE, bounds.x, "a far negative logical X must not wrap to a visible positive coordinate");
			assertTrue(bounds.width >= 0);
		}
		projected.dispose();
	}
}

@Test
public void test_virtualGtkHorizontalProviderCanReplaceHitColumn() throws Exception {
	if (!isGtk3()) return;
	for (boolean implicit : new boolean[] {false, true}) {
		Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.CHECK | SWT.H_SCROLL, true);
		projected.setBounds(0, 0, 360, 220);
		TableColumn original = implicit ? null : new TableColumn(projected, SWT.LEFT);
		if (original != null) original.setWidth(600);
		projected.setItemCount(4);
		shell.setSize(400, 280);
		shell.open();
		SwtTestUtil.processEvents();
		TableItem item = projected.getItem(0);
		Rectangle bounds = item.getBounds(0);
		boolean[] replaced = {false};
		projected.addListener(SWT.SetData, event -> {
			if (replaced[0]) return;
			replaced[0] = true;
			if (original != null) original.dispose();
			TableColumn replacement = new TableColumn(projected, SWT.LEFT);
			replacement.setWidth(600);
		});
		projected.clear(0);
		Method toggle = Table.class.getDeclaredMethod("virtualToggleAt", int.class, int.class);
		toggle.setAccessible(true);
		toggle.invoke(projected, 4, bounds.y + bounds.height / 2);
		assertTrue(replaced[0], "the hit must invoke SetData before attempting checkbox activation");
		assertFalse(projected.isDisposed());
		assertFalse(item.isDisposed());
		assertFalse(item.getChecked(), "a replaced hit target must cancel activation");
		SwtTestUtil.processEvents();
		assertEquals(1, projected.getColumnCount());
		assertEquals(600, projected.getColumn(0).getWidth());
		projected.dispose();
	}
}

@Test
public void test_virtualGtkHorizontalRejectsClippedCheckboxHit() throws Exception {
	if (!isGtk3()) return;
	Table projected = newGtkHorizontalTable(SWT.VIRTUAL | SWT.CHECK | SWT.H_SCROLL, true);
	projected.setBounds(0, 0, 360, 220);
	TableColumn column = new TableColumn(projected, SWT.LEFT);
	column.setWidth(2_000_000);
	projected.setItemCount(4);
	TableItem row = projected.getItem(0);
	row.setText("logical checkbox remains at the start of its full column");
	int[] checks = {0};
	projected.addListener(SWT.Selection, event -> { if (event.detail == SWT.CHECK) checks[0]++; });
	shell.setSize(400, 280);
	shell.open();
	SwtTestUtil.processEvents();
	projected.getHorizontalBar().setSelection(900_000);
	SwtTestUtil.processEvents();
	Method toggle = Table.class.getDeclaredMethod("virtualToggleAt", int.class, int.class);
	toggle.setAccessible(true);
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	int nativeOffset = ((Double) gtk.getMethod("gtk_adjustment_get_value", long.class)
			.invoke(null, tableLong(projected, "virtualHorizontalAdjustment"))).intValue();
	Rectangle bounds = row.getBounds(0);
	toggle.invoke(projected, nativeOffset + 4, bounds.y + bounds.height / 2);
	SwtTestUtil.processEvents();
	assertFalse(row.getChecked(), "a native projected leading edge is not the logical checkbox location");
	assertEquals(0, checks[0]);
}

@Test
public void test_virtualGtkBoundedTableOwnsLogicalRangeAndAttachedRows() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.MULTI | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	virtualTable.setBounds(0, 0, 360, 220);
	shell.setSize(400, 280);
	shell.open();
	SwtTestUtil.processEvents();
	assertEquals(0, virtualTable.getListeners(SWT.SetData).length,
			"the logical range and scrolling must work without a provider");

	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Field model = Table.class.getDeclaredField("modelHandle");
	Field adjustment = Table.class.getDeclaredField("verticalAdjustment");
	Field extent = Table.class.getDeclaredField("virtualRowExtent");
	model.setAccessible(true);
	adjustment.setAccessible(true);
	extent.setAccessible(true);
	assertEquals(1_000_000, gtk.getMethod("gtk_tree_model_iter_n_children", long.class, long.class)
			.invoke(null, model.getLong(virtualTable), 0L));
	assertEquals(1_000_000d * extent.getInt(virtualTable), (Double) gtk
			.getMethod("gtk_adjustment_get_upper", long.class).invoke(null, adjustment.getLong(virtualTable)), 0.01);
	for (int index : new int[] {750_000, 999_900, 10}) {
		virtualTable.setTopIndex(index);
		SwtTestUtil.processEvents();
		assertEquals(index, virtualTable.getTopIndex());
		int nativeRows = attachedGtkTableRows(virtualTable);
		assertTrue(nativeRows > 0 && nativeRows < 128,
				"a distant logical seek must replace the physical window: " + nativeRows);
		TableItem top = virtualTable.getItem(index);
		Rectangle cell = top.getBounds(0);
		assertSame(top, virtualTable.getItem(new Point(cell.x + 4, cell.y + Math.max(1, cell.height / 2))),
				"native hit testing must translate the current physical window to logical coordinates");
	}
	int target = 600_000;
	virtualTable.getVerticalBar().setSelection(target * extent.getInt(virtualTable));
	SwtTestUtil.processEvents();
	assertEquals(target, virtualTable.getTopIndex(), "programmatic scrollbar selection must update the logical viewport");
	virtualTable.setItemCount(7);
	SwtTestUtil.processEvents();
	assertEquals(7, virtualTable.getItemCount());
	assertTrue(virtualTable.getTopIndex() < 7);
	assertTrue(attachedGtkTableRows(virtualTable) <= 7);
}

@Test
public void test_virtualGtkBoundedTableSizingDoesNotPopulateColdLogicalRows() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setBounds(0, 0, 360, 220);
	virtualTable.setItemCount(1_000_000);
	virtualTable.setTopIndex(750_000);
	virtualTable.clearAll();
	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	Method size = storage.getClass().getDeclaredMethod("size");
	size.setAccessible(true);
	int before = (Integer) size.invoke(storage);
	virtualTable.addListener(SWT.SetData, event -> {
		throw new AssertionError("geometry requested payload for logical row " + event.index);
	});
	assertTrue(virtualTable.getItemHeight() > 0);
	Method geometry = Table.class.getDeclaredMethod("updateVirtualViewportGeometry");
	geometry.setAccessible(true);
	geometry.invoke(virtualTable);
	assertEquals(750_000, virtualTable.getTopIndex());
	assertEquals(before, size.invoke(storage), "sizing must not allocate a facade for a cold logical row");
	assertTrue(attachedGtkTableRows(virtualTable) < 128);
	Field extent = Table.class.getDeclaredField("virtualRowExtent");
	Field remainder = Table.class.getDeclaredField("virtualPixelRemainder");
	extent.setAccessible(true);
	remainder.setAccessible(true);
	extent.setInt(virtualTable, 1_001);
	remainder.setDouble(virtualTable, 1_000);
	geometry.invoke(virtualTable);
	assertTrue(remainder.getDouble(virtualTable) >= 0 && remainder.getDouble(virtualTable) < extent.getInt(virtualTable),
			"shrinking row metrics must normalize the partial-row offset");
	assertEquals(750_000, virtualTable.getTopIndex(), "a row-height change must preserve the logical anchor");
}

@Test
public void test_virtualGtkBoundedTablePreferredSizeKeepsLogicalContentHeight() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	for (int i = 0; i < 3; i++) {
		TableColumn column = new TableColumn(virtualTable, SWT.NONE);
		column.setText("Column " + i);
		column.setWidth(90);
	}
	virtualTable.setHeaderVisible(true);
	virtualTable.setBounds(0, 0, 360, 220);
	virtualTable.setItemCount(10_000);
	virtualTable.setTopIndex(7_500);
	Point preferred = virtualTable.computeSize(360, SWT.DEFAULT);
	assertTrue(preferred.y >= 10_000L * virtualTable.getItemHeight(),
			"preferred content height must describe the logical table, not just its attached rows");
	assertEquals(220, virtualTable.getBounds().height, "a preferred-size query must not expand the physical widget");
	assertEquals(7_500, virtualTable.getTopIndex());
	assertTrue(attachedGtkTableRows(virtualTable) < 128);
}

@Test
public void test_virtualGtkBoundedTableSelectionUsesFullLogicalDomain() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.MULTI | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	Method size = storage.getClass().getDeclaredMethod("size");
	size.setAccessible(true);
	int before = (Integer) size.invoke(storage);
	virtualTable.selectAll();
	virtualTable.deselect(700_000, 900_000);
	virtualTable.deselect(new int[] {0, 999_999});
	assertEquals(799_997, virtualTable.getSelectionCount());
	assertEquals(1, virtualTable.getSelectionIndex());
	assertTrue(virtualTable.isSelected(699_999));
	assertFalse(virtualTable.isSelected(700_000));
	assertFalse(virtualTable.isSelected(900_000));
	assertTrue(virtualTable.isSelected(900_001));
	assertEquals(before, size.invoke(storage), "range/complement selection must not allocate one facade per selected row");

	TableItem retained = virtualTable.getItem(950_000);
	retained.setText("retained");
	TableItem inserted = new TableItem(virtualTable, SWT.NONE, 500_000);
	assertEquals(1_000_001, virtualTable.getItemCount());
	assertEquals(799_997, virtualTable.getSelectionCount());
	assertFalse(virtualTable.isSelected(500_000), "an inserted row starts unselected");
	assertSame(retained, virtualTable.getItem(950_001));
	assertTrue(virtualTable.isSelected(950_001));
	virtualTable.remove(100);
	assertEquals(799_996, virtualTable.getSelectionCount());
	assertSame(retained, virtualTable.getItem(950_000));
	assertSame(inserted, virtualTable.getItem(499_999));

	virtualTable.setSelection(new int[] {750_000, 999_995});
	virtualTable.setTopIndex(800_000);
	virtualTable.select(800_001);
	virtualTable.deselect(800_001);
	assertEquals(800_000, virtualTable.getTopIndex(), "projecting a selection must not move the logical viewport");
	assertArrayEquals(new int[] {750_000, 999_995}, virtualTable.getSelectionIndices());
	assertEquals(2, virtualTable.getSelection().length);
	assertTrue((Integer) size.invoke(storage) < 128);
	assertTrue(attachedGtkTableRows(virtualTable) < 128);
}

@Test
public void test_virtualGtkBoundedTableKeyboardSelectionCrossesPhysicalWindow() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.MULTI | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	virtualTable.setBounds(0, 0, 360, 220);
	virtualTable.setSelection(100);
	Class<?> gdk = Class.forName("org.eclipse.swt.internal.gtk.GDK");
	Method navigate = Table.class.getDeclaredMethod("virtualNavigate", int.class, int.class);
	navigate.setAccessible(true);
	navigate.invoke(virtualTable, gdk.getField("GDK_End").getInt(null), gdk.getField("GDK_SHIFT_MASK").getInt(null));
	assertEquals(999_900, virtualTable.getSelectionCount());
	assertEquals(100, virtualTable.getSelectionIndex());
	assertTrue(virtualTable.isSelected(999_999));
	assertTrue(virtualTable.getTopIndex() > 999_800);
	navigate.invoke(virtualTable, gdk.getField("GDK_Home").getInt(null), gdk.getField("GDK_CONTROL_MASK").getInt(null));
	assertEquals(0, virtualTable.getTopIndex());
	assertEquals(999_900, virtualTable.getSelectionCount(), "control navigation must move focus without changing selection");
	assertFalse(virtualTable.isSelected(0));
	assertTrue(attachedGtkTableRows(virtualTable) < 128);
}

@Test
public void test_virtualGtkBoundedTableProviderCanDisposeDuringWindowChange() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setBounds(0, 0, 360, 220);
	virtualTable.setItemCount(1_000_000);
	shell.setSize(400, 280);
	shell.open();
	virtualTable.setSelection(900_000);
	SwtTestUtil.processEvents();
	virtualTable.setTopIndex(0);
	SwtTestUtil.processEvents();
	virtualTable.clearAll();
	virtualTable.addListener(SWT.SetData, event -> virtualTable.dispose());
	virtualTable.setTopIndex(900_000);
	SwtTestUtil.processEvents();
	assertTrue(virtualTable.isDisposed(), "the bounded view must tolerate provider disposal while replacing and painting its window");
}

@Test
public void test_virtualGtkBoundedTableProviderRemovalClearsRendererIterLeases() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.addListener(SWT.MeasureItem, event -> { });
	TableColumn column = new TableColumn(virtualTable, SWT.NONE);
	column.setWidth(120);
	virtualTable.setItemCount(3);
	TableItem item = virtualTable.getItem(1);
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Class<?> os = Class.forName("org.eclipse.swt.internal.gtk.OS");
	Field modelField = Table.class.getDeclaredField("virtualViewModel");
	modelField.setAccessible(true);
	long model = modelField.getLong(virtualTable);
	long columnHandle = TableColumn.class.getField("handle").getLong(column);
	Method textMethod = Table.class.getDeclaredMethod("getTextRenderer", long.class);
	Method imageMethod = Table.class.getDeclaredMethod("getPixbufRenderer", long.class);
	textMethod.setAccessible(true);
	imageMethod.setAccessible(true);
	long text = (Long) textMethod.invoke(virtualTable, columnHandle);
	long image = (Long) imageMethod.invoke(virtualTable, columnHandle);
	assertTrue(text != 0 && image != 0);
	Field itemQuark = shell.getDisplay().getClass().getDeclaredField("SWT_OBJECT_INDEX2");
	itemQuark.setAccessible(true);
	int quark = itemQuark.getInt(null);
	Method data = Table.class.getDeclaredMethod("cellDataProc", long.class, long.class, long.class, long.class, long.class);
	data.setAccessible(true);
	long iter = (Long) os.getMethod("g_malloc", long.class)
			.invoke(null, ((Integer) gtk.getMethod("GtkTreeIter_sizeof").invoke(null)).longValue());
	assertTrue(iter != 0);
	try {
		assertTrue((Boolean) gtk.getMethod("gtk_tree_model_iter_nth_child", long.class, long.class, long.class, int.class)
				.invoke(null, model, iter, 0L, 1));
		data.invoke(virtualTable, columnHandle, image, model, iter, 0L);
		assertEquals(TableItem.class.getField("handle").getLong(item),
				os.getMethod("g_object_get_qdata", long.class, int.class).invoke(null, image, quark));
		virtualTable.clear(1);
		int[] providers = {0};
		virtualTable.addListener(SWT.SetData, event -> { providers[0]++; virtualTable.setItemCount(0); });
		data.invoke(virtualTable, columnHandle, text, model, iter, 0L);
		assertEquals(1, providers[0]);
		assertTrue(item.isDisposed());
		assertEquals(0L, os.getMethod("g_object_get_qdata", long.class, int.class).invoke(null, text, quark));
		assertEquals(0L, os.getMethod("g_object_get_qdata", long.class, int.class).invoke(null, image, quark),
				"removing an item must clear renderer pointers before its semantic iterator is freed");
		Method measure = Table.class.getDeclaredMethod("sendMeasureEvent", long.class, long.class, long.class);
		measure.setAccessible(true);
		measure.invoke(virtualTable, text, 0L, 0L);
	} finally {
		os.getMethod("g_free", long.class).invoke(null, iter);
	}
	SwtTestUtil.processEvents();
	assertEquals(0, virtualTable.getItemCount());
	assertEquals(0, attachedGtkTableRows(virtualTable));
}

@Test
public void test_virtualGtkTableProviderFailureReleasesRemovalBoundary() throws Exception {
	if (!isGtk3()) return;
	for (String mode : new String[] {"false", "true"}) {
		Table virtualTable = newGtkViewportTable(SWT.VIRTUAL, mode);
		virtualTable.setItemCount(3);
		TableItem failed = virtualTable.getItem(1);
		RuntimeException marker = new RuntimeException("provider failure");
		Listener provider = event -> { throw marker; };
		virtualTable.addListener(SWT.SetData, provider);
		assertSame(marker, assertThrows(RuntimeException.class, failed::getText));
		virtualTable.removeListener(SWT.SetData, provider);
		virtualTable.removeAll();
		assertTrue(failed.isDisposed());
		virtualTable.setItemCount(1);
		virtualTable.addListener(SWT.SetData, event -> ((TableItem) event.item).setText("recovered"));
		assertEquals("recovered", virtualTable.getItem(0).getText());
		virtualTable.dispose();
	}
}

@Test
public void test_virtualGtkBoundedTableDefersWindowReplacementAcrossNestedProviders() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.V_SCROLL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	TableItem outer = virtualTable.getItem(10), inner = virtualTable.getItem(11);
	Field current = Table.class.getDeclaredField("currentItem");
	Field start = Table.class.getDeclaredField("virtualViewStart");
	current.setAccessible(true);
	start.setAccessible(true);
	int previousStart = start.getInt(virtualTable);
	virtualTable.addListener(SWT.SetData, event -> {
		TableItem item = (TableItem) event.item;
		if (item == outer) {
			assertEquals("inner", inner.getText());
			try {
				assertSame(outer, current.get(virtualTable));
				virtualTable.setItemCount(1_000_001);
				virtualTable.setTopIndex(750_000);
				assertEquals(previousStart, start.getInt(virtualTable),
						"the native snapshot must stay attached until the outer provider returns");
			} catch (IllegalAccessException failure) {
				throw new AssertionError(failure);
			}
			item.setText("outer");
		} else {
			item.setText(item == inner ? "inner" : "row " + event.index);
		}
	});
	assertEquals("outer", outer.getText());
	assertNull(current.get(virtualTable));
	SwtTestUtil.processEvents();
	assertEquals(750_000, virtualTable.getTopIndex());
	assertTrue(start.getInt(virtualTable) > 749_000);
	assertSame(outer, virtualTable.getItem(10));
	assertSame(inner, virtualTable.getItem(11));
	assertTrue(attachedGtkTableRows(virtualTable) < 128);
}

@Test
public void test_virtualGtkTableColumnPackReadsSparsePopulatedStorage() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	TableColumn column = new TableColumn(virtualTable, SWT.NONE);
	TableItem distant = virtualTable.getItem(900_000);
	String text = "Offscreen populated logical row ".repeat(8);
	distant.setText(text);
	Listener provider = event -> { throw new AssertionError("packing requested cold row " + event.index); };
	virtualTable.addListener(SWT.SetData, provider);
	int expected;
	GC gc = new GC(virtualTable);
	try { expected = gc.textExtent(text).x; }
	finally { gc.dispose(); }
	column.pack();
	assertTrue(column.getWidth() >= expected, "column packing must read populated rows from VirtualItemStorage and its cache state");
	int wide = column.getWidth();
	distant.setText("small");
	column.pack();
	assertTrue(column.getWidth() < wide);
	virtualTable.removeListener(SWT.SetData, provider);
	assertSame(distant, virtualTable.getItem(900_000));
}

@Test
@SuppressWarnings("unchecked")
public void test_virtualGtkBoundedTableAccessibleIncludesPartialBottomRow() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000);
	virtualTable.setTopIndex(100);
	Field extent = Table.class.getDeclaredField("virtualRowExtent");
	Field body = Table.class.getDeclaredField("virtualBodyExtent");
	Field remainder = Table.class.getDeclaredField("virtualPixelRemainder");
	extent.setAccessible(true);
	body.setAccessible(true);
	remainder.setAccessible(true);
	extent.setInt(virtualTable, 24);
	body.setInt(virtualTable, 40);
	remainder.setDouble(virtualTable, 20);
	Method cellAt = Table.class.getDeclaredMethod("virtualAccessibleCell", TableItem.class, int.class);
	cellAt.setAccessible(true);
	Field listeners = Accessible.class.getDeclaredField("accessibleControlListeners");
	listeners.setAccessible(true);
	for (int row : new int[] {100, 101, 102, 103}) {
		Accessible cell = (Accessible) cellAt.invoke(virtualTable, virtualTable.getItem(row), 0);
		AccessibleControlEvent state = new AccessibleControlEvent(cell);
		for (AccessibleControlListener listener : (List<AccessibleControlListener>) listeners.get(cell)) listener.getState(state);
		assertEquals(row == 103, (state.detail & ACC.STATE_OFFSCREEN) != 0,
				"pixel interval [-20, 40) exposes three 24-pixel rows, including the partial bottom row " + row);
	}
}

@Test
@SuppressWarnings("unchecked")
public void test_virtualGtkBoundedTableAccessibleCountsAndRanksStayLogical() throws Exception {
	if (!isGtk3()) return;
	Table virtualTable = newGtkViewportTable(SWT.VIRTUAL | SWT.MULTI, "true");
	requireBoundedGtkView(virtualTable);
	virtualTable.setItemCount(1_000_000);
	virtualTable.selectAll();
	virtualTable.deselect(5, 10);
	Accessible accessible = virtualTable.getAccessible();
	Field listeners = Accessible.class.getDeclaredField("accessibleTableListeners");
	listeners.setAccessible(true);
	List<AccessibleTableListener> tableListeners = (List<AccessibleTableListener>) listeners.get(accessible);
	assertNotNull(tableListeners);
	AccessibleTableEvent event = new AccessibleTableEvent(accessible);
	for (AccessibleTableListener listener : tableListeners) listener.getRowCount(event);
	assertEquals(1_000_000, event.count);
	for (AccessibleTableListener listener : tableListeners) listener.getSelectedRowCount(event);
	assertEquals(999_994, event.count);
	event.row = 750_000;
	for (AccessibleTableListener listener : tableListeners) listener.isRowSelected(event);
	assertTrue(event.isSelected);
	event.row = 7;
	for (AccessibleTableListener listener : tableListeners) listener.isRowSelected(event);
	assertFalse(event.isSelected);
	Method rowAt = Table.class.getDeclaredMethod("virtualAccessibleSelectedRowAt", int.class);
	rowAt.setAccessible(true);
	assertEquals(11, rowAt.invoke(virtualTable, 5));
	assertEquals(999_999, rowAt.invoke(virtualTable, 999_993));
	assertTrue(attachedGtkTableRows(virtualTable) < 128,
			"accessibility counts and selected-rank queries must not expand the physical model");
}

@Test
public void test_virtualTableEditorTracksPinnedItemAcrossViewportScroll() {
	Table virtualTable = new Table(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTable.setSize(320, 180);
	virtualTable.setItemCount(256);
	TableItem edited = virtualTable.getItem(200);
	edited.setText("edited");

	org.eclipse.swt.custom.TableEditor cellEditor =
			new org.eclipse.swt.custom.TableEditor(virtualTable);
	cellEditor.grabHorizontal = true;
	Text control = new Text(virtualTable, SWT.NONE);
	cellEditor.setEditor(control, edited, 0);

	virtualTable.setTopIndex(196);
	ScrollBar vertical = virtualTable.getVerticalBar();
    if (vertical != null) {
        vertical.notifyListeners(SWT.Selection, new Event());
    }
	cellEditor.layout();

	assertSame(edited, cellEditor.getItem());
	assertSame(edited, virtualTable.getItem(200),
			"viewport scroll must not rebind an exposed TableItem facade");
	Rectangle cell = edited.getBounds(0);
	Rectangle overlay = control.getBounds();
	assertEquals(cell.y, overlay.y,
			"editor overlay must follow the logical row after viewport scroll");
	assertTrue(overlay.height >= cell.height);

	cellEditor.dispose();
	control.dispose();
	virtualTable.dispose();
}

@Test
public void test_virtualDndProjectionPinsSparseTableFacade() throws Exception {
	Table virtualTable = new Table(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTable.setItemCount(100_000);
	shell.setLayout(new FillLayout());
	shell.setSize(320, 180);
	shell.open();
	SwtTestUtil.processEvents();

	virtualTable.setTopIndex(50_000);
	SwtTestUtil.processEvents();

	DropTargetEffect effect = new DropTargetEffect(virtualTable);
	org.eclipse.swt.graphics.Point global =
			virtualTable.toDisplay(5, Math.max(1, virtualTable.getItemHeight() / 2));
	org.eclipse.swt.widgets.Widget hit = effect.getItem(global.x, global.y);
	TableItem item = assertInstanceOf(TableItem.class, hit);
	assertSame(item, effect.getItem(global.x, global.y),
			"DND projection must preserve the same logical TableItem facade");

	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	assertNotNull(storage);

	Method size = storage.getClass().getDeclaredMethod("size");
	size.setAccessible(true);
	int materialized = (Integer) size.invoke(storage);
	assertTrue(materialized < 128,
			"DND hit projection must remain viewport-bounded for a 100K-row virtual Table");

	Class<?> stateClass = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	Field pinnedField = stateClass.getDeclaredField("PINNED");
	pinnedField.setAccessible(true);
	long pinned = pinnedField.getLong(null);
	Method stateOfIdentity = storage.getClass().getDeclaredMethod("stateOfIdentity", Object.class);
	stateOfIdentity.setAccessible(true);
	long state = (Long) stateOfIdentity.invoke(storage, item);
	assertTrue((state & pinned) != 0,
			"DropTargetEvent item projection must pin the exposed virtual TableItem identity");

	virtualTable.dispose();
}


@Test
public void test_virtualDndHitPinsColdTableCoordinateWithoutDenseMaterialization() throws Exception {
	Table virtualTable = new Table(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTable.setBounds(0, 0, 320, 180);
	virtualTable.setItemCount(4_096);
	shell.setSize(360, 240);
	shell.open();
	while (shell.getDisplay().readAndDispatch()) {
		// drain native layout/paint work before coordinate hit testing
	}

	virtualTable.setTopIndex(2_000);
	int top = virtualTable.getTopIndex();
	int rowHeight = Math.max(1, virtualTable.getItemHeight());
	// Cocoa client coordinates retain the scrolled document origin.
	org.eclipse.swt.graphics.Rectangle viewport = virtualTable.getClientArea();
	org.eclipse.swt.graphics.Point firstRowPoint = new org.eclipse.swt.graphics.Point(
			viewport.x + 4, viewport.y + Math.max(1, rowHeight / 2));
	assertTrue(viewport.contains(firstRowPoint), "DND probe must lie in the visible client area");
	org.eclipse.swt.graphics.Point displayPoint = virtualTable.toDisplay(firstRowPoint);
	assertEquals(firstRowPoint, virtualTable.toControl(displayPoint),
			"screen hit coordinates must round-trip through the native client origin");

	org.eclipse.swt.dnd.DropTargetEffect effect =
			new org.eclipse.swt.dnd.DropTargetEffect(virtualTable);
	TableItem hit = (TableItem) effect.getItem(displayPoint.x, displayPoint.y);

	assertNotNull(hit);
	assertEquals(top, virtualTable.indexOf(hit),
			"DND hit-testing must resolve the logical visible row");
	assertSame(hit, effect.getItem(displayPoint.x, displayPoint.y),
			"pending native scroll must not rebind an exposed facade");
	org.eclipse.swt.graphics.Point secondRowPoint = new org.eclipse.swt.graphics.Point(
			firstRowPoint.x, firstRowPoint.y + rowHeight);
	assertTrue(viewport.contains(secondRowPoint), "adjacent DND probe must remain visible");
	org.eclipse.swt.graphics.Point nextPoint = virtualTable.toDisplay(secondRowPoint);
	TableItem nextHit = (TableItem) effect.getItem(nextPoint.x, nextPoint.y);
	assertNotNull(nextHit);
	assertEquals(top + 1, virtualTable.indexOf(nextHit),
			"row offsets must be retained while the native scroll is pending");
	org.junit.jupiter.api.Assertions.assertNull(virtualTable.getItem(new org.eclipse.swt.graphics.Point(-100, -100)),
			"logical translation must retain native clipping");

	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	Method stateOfIdentity = storage.getClass().getDeclaredMethod("stateOfIdentity", Object.class);
	stateOfIdentity.setAccessible(true);
	long state = ((Number) stateOfIdentity.invoke(storage, hit)).longValue();

	Field pinnedField = Class.forName("org.eclipse.swt.widgets.VirtualItemState")
			.getDeclaredField("PINNED");
	pinnedField.setAccessible(true);
	long pinned = pinnedField.getLong(null);
	assertTrue((state & pinned) != 0,
			"a TableItem exposed by DND hit-testing must be pinned");

	Field sizeField = storage.getClass().getDeclaredField("size");
	sizeField.setAccessible(true);
	assertTrue(sizeField.getInt(storage) < 128,
			"DND hit-testing must stay within the visible sparse frontier");

	virtualTable.setTopIndex(0);
	assertSame(hit, virtualTable.getItem(top),
			"scrolling away must not rebind a DND-exposed TableItem facade");
	virtualTable.dispose();
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	assertThrows(IllegalArgumentException.class, () -> new Table(null, 0), "No exception thrown for parent == null");
}

@Override
@Test
public void test_computeSizeIIZ() {
}



@Test
public void test_virtualScrollMetricsUseLogicalRowsAndSampleExtent() throws Exception {
	Class<?> type = Class.forName("org.eclipse.swt.widgets.VirtualScrollMetrics");
	Constructor<?> constructor = type.getDeclaredConstructor();
	constructor.setAccessible(true);
	Object metrics = constructor.newInstance();

	Method configure = type.getDeclaredMethod("configure", int.class, int.class, int.class);
	Method visibleRows = type.getDeclaredMethod("visibleRows");
	Method maximum = type.getDeclaredMethod("maximum");
	Method thumb = type.getDeclaredMethod("thumb");
	Method pageIncrement = type.getDeclaredMethod("pageIncrement");
	Method maxTopRow = type.getDeclaredMethod("maxTopRow");
	Method clampTopRow = type.getDeclaredMethod("clampTopRow", int.class);
	Method estimatedContentExtent = type.getDeclaredMethod("estimatedContentExtent");
	for (Method method : new Method[] {
			configure, visibleRows, maximum, thumb, pageIncrement,
			maxTopRow, clampTopRow, estimatedContentExtent}) {
		method.setAccessible(true);
	}

	configure.invoke(metrics, 1_000_000, 22, 440);
	assertEquals(20, visibleRows.invoke(metrics));
	assertEquals(1_000_000, maximum.invoke(metrics),
			"scrollbar maximum must stay in logical row units");
	assertEquals(20, thumb.invoke(metrics));
	assertEquals(20, pageIncrement.invoke(metrics));
	assertEquals(999_980L, maxTopRow.invoke(metrics));
	assertEquals(999_980, clampTopRow.invoke(metrics, Integer.MAX_VALUE));
	assertEquals(22_000_000L, estimatedContentExtent.invoke(metrics));

	configure.invoke(metrics, Integer.MAX_VALUE, 64, 640);
	assertEquals(Integer.MAX_VALUE, maximum.invoke(metrics),
			"huge logical models must not convert pixel extent into scrollbar maximum");
	assertEquals(10, visibleRows.invoke(metrics));
	assertTrue((Long) estimatedContentExtent.invoke(metrics) > Integer.MAX_VALUE,
			"estimated pixel extent may exceed int while row-coordinate scrollbar remains valid");

	Method configureLong = type.getDeclaredMethod("configure", long.class, int.class, int.class);
	Method logicalRowCount = type.getDeclaredMethod("logicalRowCount");
	Method selectionForTopRow = type.getDeclaredMethod("selectionForTopRow", long.class);
	Method topRowForSelection = type.getDeclaredMethod("topRowForSelection", int.class);
	for (Method method : new Method[] {
		configureLong, logicalRowCount, selectionForTopRow, topRowForSelection}) {
		method.setAccessible(true);
	}

	long hugeRows = 6_000_000_000L;
	configureLong.invoke(metrics, hugeRows, 24, 480);
	assertEquals(hugeRows, logicalRowCount.invoke(metrics));
	assertEquals(Integer.MAX_VALUE, maximum.invoke(metrics));
	assertEquals(20, visibleRows.invoke(metrics));
	assertEquals(144_000_000_000L, estimatedContentExtent.invoke(metrics));

	long requestedTop = 3_000_000_000L;
	int projectedSelection = (Integer) selectionForTopRow.invoke(metrics, requestedTop);
	long roundTripTop = (Long) topRowForSelection.invoke(metrics, projectedSelection);
	assertTrue(Math.abs(roundTripTop - requestedTop) <= 4,
			"integer scrollbar projection must round-trip a huge logical row coordinate within one projected step");
}

@Test
public void test_viewportLayerStateSeparatesHeaderFromVerticalScroll() throws Exception {
	Class<?> type = Class.forName("org.eclipse.swt.widgets.ViewportLayerState");
	Constructor<?> constructor = type.getDeclaredConstructor();
	constructor.setAccessible(true);
	Object layers = constructor.newInstance();

	Method initialize = type.getDeclaredMethod("initialize", double.class, double.class);
	Method scrollTo = type.getDeclaredMethod("scrollTo", double.class, double.class);
	initialize.setAccessible(true);
	scrollTo.setAccessible(true);

	Field bodyField = type.getDeclaredField("BODY");
	Field frozenField = type.getDeclaredField("FROZEN");
	Field headerField = type.getDeclaredField("HEADER");
	Field scrollBarField = type.getDeclaredField("SCROLLBAR");
	Field editorField = type.getDeclaredField("EDITOR");
	Field feedbackField = type.getDeclaredField("FEEDBACK");
	for (Field field : new Field[] {bodyField, frozenField, headerField, scrollBarField, editorField, feedbackField}) {
		field.setAccessible(true);
	}
	int body = bodyField.getInt(null);
	int frozen = frozenField.getInt(null);
	int header = headerField.getInt(null);
	int scrollBar = scrollBarField.getInt(null);
	int editor = editorField.getInt(null);
	int feedback = feedbackField.getInt(null);

	initialize.invoke(layers, 10d, 20d);
	int vertical = (Integer) scrollTo.invoke(layers, 10d, 25d);
	assertEquals(body | frozen | scrollBar | editor | feedback, vertical,
			"vertical scrolling must not invalidate the column-header plane");

	int horizontal = (Integer) scrollTo.invoke(layers, 15d, 25d);
	assertEquals(body | header | scrollBar | editor | feedback, horizontal,
			"horizontal scrolling must invalidate the scroll-coupled header plane");

	int unchanged = (Integer) scrollTo.invoke(layers, 15d, 25d);
	assertEquals(0, unchanged, "unchanged viewport origin must not dirty any plane");
}

@Test
public void test_virtualItemResidencyDoesNotScaleWithLogicalCount() throws Exception {
	Table virtualTable = new Table(shell, SWT.VIRTUAL);
	virtualTable.setItemCount(4096);

	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	assertNotNull(storage, "all virtual Table backends must use the shared sparse owner");
	Field valuesField = storage.getClass().getDeclaredField("values");
	valuesField.setAccessible(true);
	Object[] backing = (Object[]) valuesField.get(storage);
	assertTrue(backing.length <= 16, "virtual Table must not allocate one Java slot per logical row");

	TableItem last = virtualTable.getItem(4095);
	assertSame(last, virtualTable.getItem(4095));
	backing = (Object[]) valuesField.get(storage);
	assertTrue(backing.length <= 16, "materializing one distant row must keep residency sparse");
}


@Test
public void test_virtualPackedStateFollowsLogicalInsertAndRemove() throws Exception {
	Table virtualTable = new Table(shell, SWT.VIRTUAL | SWT.CHECK);
	virtualTable.setItemCount(32);
	TableItem marked = virtualTable.getItem(20);
	marked.setChecked(true);
	marked.setGrayed(true);

	Field storageField = Table.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTable);
	Field stateMasksField = storage.getClass().getDeclaredField("stateMasks");
	stateMasksField.setAccessible(true);
	Field indicesField = storage.getClass().getDeclaredField("indices");
	indicesField.setAccessible(true);
	Field sizeField = storage.getClass().getDeclaredField("size");
	sizeField.setAccessible(true);

	long[] before = (long[]) stateMasksField.get(storage);
	int[] beforeIndices = (int[]) indicesField.get(storage);
	int beforeSize = sizeField.getInt(storage);
	int markedSlot = -1;
	for (int i = 0; i < beforeSize; i++) {
		if (beforeIndices[i] == 20) {
			markedSlot = i;
			break;
		}
	}
	assertTrue(markedSlot >= 0);
	long markedState = before[markedSlot];
	assertTrue(markedState != 0, "virtual semantic state must be represented by a packed mask");
	assertTrue(before.length <= 16, "packed state capacity must follow materialized residency");

	TableItem inserted = new TableItem(virtualTable, SWT.NONE, 3);
	long[] afterInsert = (long[]) stateMasksField.get(storage);
	int[] afterInsertIndices = (int[]) indicesField.get(storage);
	int afterInsertSize = sizeField.getInt(storage);
	int shiftedSlot = -1;
	for (int i = 0; i < afterInsertSize; i++) {
		if (afterInsertIndices[i] == 21) {
			shiftedSlot = i;
			break;
		}
	}
	assertTrue(shiftedSlot >= 0);
	assertEquals(markedState, afterInsert[shiftedSlot],
			"packed state must move with the logical item");
	assertSame(marked, virtualTable.getItem(21));
	assertTrue(marked.getChecked());
	assertTrue(marked.getGrayed());

	inserted.dispose();
	long[] afterRemove = (long[]) stateMasksField.get(storage);
	int[] afterRemoveIndices = (int[]) indicesField.get(storage);
	int afterRemoveSize = sizeField.getInt(storage);
	int restoredSlot = -1;
	for (int i = 0; i < afterRemoveSize; i++) {
		if (afterRemoveIndices[i] == 20) {
			restoredSlot = i;
			break;
		}
	}
	assertTrue(restoredSlot >= 0);
	assertEquals(markedState, afterRemove[restoredSlot],
			"packed state must shift back with logical removal");
	assertSame(marked, virtualTable.getItem(20));
	assertTrue(marked.getChecked());
	assertTrue(marked.getGrayed());
}

@Test
public void test_virtualMaterializedIdentityTracksLogicalRemovals() {
	Table virtualTable = new Table(shell, SWT.VIRTUAL);
	virtualTable.setItemCount(32);
	TableItem five = virtualTable.getItem(5);
	TableItem twenty = virtualTable.getItem(20);

	virtualTable.remove(0, 2);
	assertEquals(2, virtualTable.indexOf(five));
	assertEquals(17, virtualTable.indexOf(twenty));
	assertSame(five, virtualTable.getItem(2));
	assertSame(twenty, virtualTable.getItem(17));
}

@Test
public void test_virtualExplicitInsertionShiftsMaterializedIdentity() {
	Table virtualTable = new Table(shell, SWT.VIRTUAL);
	virtualTable.setItemCount(16);
	TableItem ten = virtualTable.getItem(10);

	TableItem inserted = new TableItem(virtualTable, SWT.NONE, 3);
	assertEquals(17, virtualTable.getItemCount());
	assertEquals(3, virtualTable.indexOf(inserted));
	assertEquals(11, virtualTable.indexOf(ten));
	assertSame(ten, virtualTable.getItem(11));
}

@Test
public void test_deselect$I() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(new int[] {0, 3});
	assertEquals(2, table.getSelectionCount());

	table.deselect(new int[] {1, 2});
	assertEquals(2, table.getSelectionCount());

	table.deselect(new int[] {1, 3, 5});
	assertEquals(1, table.getSelectionCount());

	table.deselect(new int[] {9, 3, 0});
	assertEquals(0, table.getSelectionCount());

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	assertThrows(IllegalArgumentException.class, () -> table.deselect(null), "No exception thrown for selection == null");
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {});
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {-1, 100, -1000});
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {2, -1, 1, 100, 2});
	assertEquals(number-2, table.getSelectionCount());

	table.deselect(new int[] {2, -1, 1, 100, 2});
	assertEquals(number-2, table.getSelectionCount());

	table.deselect(new int[] {2, -1, 3, 100, 2});
	assertEquals(number-3, table.getSelectionCount());
}

@Test
public void test_deselectAll() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertEquals(0, table.getSelectionCount());
	table.select(new int[] {2, 4, 5, 10});

	assertEquals(4, table.getSelectionCount());

	table.deselectAll();
	assertEquals(0, table.getSelectionCount());

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	table.deselectAll();
	assertEquals(0, table.getSelectionCount());
}

@Test
public void test_deselectI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
	}
	assertEquals(0, table.getSelectionCount());

	table.deselect(0);
	assertEquals(0, table.getSelectionCount());

	table.select(new int[] {0, 3, 6});
	assertEquals(3, table.getSelectionCount());

	table.deselect(0);
	assertEquals(2, table.getSelectionCount());

	table.deselect(0);
	assertEquals(2, table.getSelectionCount());

	table.deselect(5);
	assertEquals(2, table.getSelectionCount());

	table.deselect(3);
	assertEquals(1, table.getSelectionCount());

	table.deselect(100);
	assertEquals(1, table.getSelectionCount());
}

@Test
public void test_deselectII() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(new int[] {0, 3, 6});
	assertEquals(3, table.getSelectionCount());

	table.deselect(7, 10);
	assertEquals(3, table.getSelectionCount());

	table.deselect(6, 10);
	assertEquals(2, table.getSelectionCount());

	table.deselect(1, 10);
	assertEquals(1, table.getSelectionCount());

	table.deselect(0, 10);
	assertEquals(0, table.getSelectionCount());

	table.deselect(0, 100);
	assertEquals(0, table.getSelectionCount());

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	table.deselect(-10, 2);
	assertEquals(number-3, table.getSelectionCount());
	table.selectAll();

	table.deselect(-10, 2000);
	assertEquals(0, table.getSelectionCount());
	table.selectAll();

	table.deselect(2000, -10);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(0, number-1);
	assertEquals(0, table.getSelectionCount());
	table.selectAll();

	table.deselect(0, 0);
	assertEquals(number-1, table.getSelectionCount());
	table.selectAll();

	table.deselect(number-1, number-1);
	assertEquals(number-1, table.getSelectionCount());
	table.selectAll();
	table.deselect(-1, -1);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(number, number);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();
}

@Test
public void test_getColumnCount() {
	assertEquals(0, table.getColumnCount());
	TableColumn column0 = new TableColumn(table, SWT.NONE);
	assertEquals(1, table.getColumnCount());
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	assertEquals(2, table.getColumnCount());
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	assertEquals(3, table.getColumnCount());
	column0.dispose();
	assertEquals(2, table.getColumnCount());
	column1.dispose();
	assertEquals(1, table.getColumnCount());
	column2.dispose();
	assertEquals(0, table.getColumnCount());
}

@Test
public void test_getColumnI() {
	assertThrows(IllegalArgumentException.class, () -> table.getColumn(0), "No exception thrown for index out of range");
	TableColumn column0 = new TableColumn(table, SWT.LEFT);
	assertThrows(IllegalArgumentException.class, () -> table.getColumn(1), "No exception thrown for index out of range");
	assertEquals(column0, table.getColumn(0));
	TableColumn column1 = new TableColumn(table, SWT.LEFT);
	assertEquals(column1, table.getColumn(1));
	column1.dispose();
	assertThrows(IllegalArgumentException.class, () -> table.getColumn(1), "No exception thrown for index out of range");
	column0.dispose();
	assertThrows(IllegalArgumentException.class, () -> table.getColumn(0), "No exception thrown for index out of range");
}

@Test
public void test_getColumns() {
	assertEquals(0, table.getColumns().length);
	TableColumn column0 = new TableColumn(table, SWT.LEFT);
	TableColumn[] columns = table.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column0, columns[0]);
	column0.dispose();
	assertEquals(0, table.getColumns().length);
	column0 = new TableColumn(table, SWT.LEFT);
	TableColumn column1 = new TableColumn(table, SWT.RIGHT, 1);
	columns = table.getColumns();
	assertEquals(2, columns.length);
	assertEquals(column0, columns[0]);
	assertEquals(column1, columns[1]);
	column0.dispose();
	columns = table.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column1, columns[0]);
	column1.dispose();
	assertEquals(0, table.getColumns().length);
}

@Test
public void test_getGridLineWidth() {
	// execute method but there is no way to check the value since it may be anything including 0
	table.getGridLineWidth();
}

@Test
public void test_getHeaderHeight() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getHeaderHeight(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table))");
		}
		return;
	}
	assertEquals(0, table.getHeaderHeight());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderHeight() > 0);
	table.setHeaderVisible(false);
	assertEquals(0, table.getHeaderHeight());
}

@Test
public void test_getItemCount() {
	int[] cases = {0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals(cases[j], table.getItemCount());
		table.removeAll();
	}

	// note: SWT.SINGLE
	makeCleanEnvironment(true);
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals(cases[j], table.getItemCount());
		table.removeAll();
	}
}

@Test
public void test_getItemHeight() {
	assertTrue(table.getItemHeight() > 0);
	new TableItem(table, 0);
	assertTrue(table.getItemHeight() > 0);
}

@Test
public void test_getItemI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

    for (int i = 0; i < number; i++) {
        assertEquals(items[i], table.getItem(i));
    }
	assertThrows(IllegalArgumentException.class, () -> table.getItem(number), "No exception thrown for illegal index argument");

	assertThrows(IllegalArgumentException.class, () -> table.getItem(number+1), "No exception thrown for illegal index argument");

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	for (int i = 0; i < number; i++) {
		assertEquals(items[i], table.getItem(i));
	}
	assertThrows(IllegalArgumentException.class, () -> table.getItem(number), "No exception thrown for illegal index argument");

	assertThrows(IllegalArgumentException.class, () -> table.getItem(number+1), "No exception thrown for illegal index argument");
}

@Test
public void test_getItems() {
	int[] cases = {0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals(cases[j], table.getItems().length);
		table.removeAll();
	}

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TableItem ti = new TableItem(table, 0);
			ti.setText(String.valueOf(i));
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			assertEquals(String.valueOf(i), items[i].getText());
		}
		table.removeAll();
	}

	// note SWT.SINGLE
	makeCleanEnvironment(true);

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals(cases[j], table.getItems().length);
		table.removeAll();
	}

	makeCleanEnvironment(true);

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TableItem ti = new TableItem(table, 0);
			ti.setText(String.valueOf(i));
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			assertEquals(String.valueOf(i), items[i].getText());
		}
		table.removeAll();
	}
}

@Test
public void test_getSelection() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new TableItem[] {items[2], items[10], items[number-1]}, table.getSelection());

	table.setSelection(items);
	assertArrayEquals(items, table.getSelection());

	table.setSelection(items[0]);
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[10]});
	assertArrayEquals(new TableItem[] {items[10]}, table.getSelection());

	table.setSelection(new TableItem[]{items[number-1]});
	assertArrayEquals(new TableItem[] {items[number-1]}, table.getSelection());

	table.setSelection(new TableItem[]{items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());

	table.setSelection(new TableItem[]{items[10], items[number-1], items[2]});
	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(items);
	assertArrayEquals(new TableItem[] {}, table.getSelection());
}

@Test
public void test_getSelectionCount() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(3, table.getSelectionCount());

	table.setSelection(items[2]);
	assertEquals(1, table.getSelectionCount());

	table.setSelection(items);
	assertEquals(number, table.getSelectionCount());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(items);
	assertEquals(0, table.getSelectionCount());
}

@Test
public void test_getSelectionIndex() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(2, table.getSelectionIndex());

	table.setSelection(items[10]);
	assertEquals(10, table.getSelectionIndex());

	table.setSelection(items);
	assertEquals(0, table.getSelectionIndex());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2]});
	assertEquals(2, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(number - 1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(10, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(items);
	assertEquals(-1, table.getSelectionIndex());
}

@Test
public void test_getSelectionIndices() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new int[]{2, 10, number-1}, table.getSelectionIndices()); // 10 < number

	int[] all = new int[number];
    for (int i = 0; i < number; i++) {
        all[i] = i;
    }
	table.setSelection(items);
	assertArrayEquals(all, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2]});
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[number-1]});
	assertArrayEquals(new int[]{number-1}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[10]});
	assertArrayEquals(new int[]{10}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(items);
	assertArrayEquals(new int[]{}, table.getSelectionIndices());
}

@Test
public void test_indexOfLorg_eclipse_swt_widgets_TableItem() {
	int number = 20;
	TableItem[] items = new TableItem[number];

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(i, table.indexOf(items[i]));
	}

	for (int i = 0; i < number; i++) {
		assertThrows(IllegalArgumentException.class, () -> table.indexOf((TableItem)null), "No exception thrown for tableItem == null");
	}

	// another table
	Table table_2 = new Table(shell, 0);
	TableItem[] items_2 = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items_2[i] = new TableItem(table_2, 0);
		items_2[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(-1, table.indexOf(items_2[i]));
	}

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	number = 20;
	items = new TableItem[number];

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(i, table.indexOf(items[i]));
	}

	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertThrows(IllegalArgumentException.class, () -> table.indexOf((TableItem)null), "No exception thrown for tableItem == null");
	}

	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	// another table
	table_2 = new Table(shell, 0);
	items_2 = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items_2[i] = new TableItem(table_2, 0);
		items_2[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(-1, table.indexOf(items_2[i]));
	}
}

@Test
public void test_isSelectedI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
    for (int i = 0; i < number; i++) {
        assertTrue(!table.isSelected(i));
    }
	table.setSelection(new TableItem[] {items[2], items[number-1], items[10]});
	for (int i = 0; i < number; i++) {
        if (i == 2 || i == number - 1 || i == 10) {
            assertTrue(table.isSelected(i));
        } else {
            assertTrue(!table.isSelected(i));
        }
	}

	table.setSelection(items[0]);
	for (int i = 0; i < number; i++) {
        if (i == 0) {
            assertTrue(table.isSelected(i));
        } else {
            assertTrue(!table.isSelected(i));
        }
	}


	table.setSelection(items);
    for (int i = 0; i < number; i++) {
        assertTrue(table.isSelected(i));
    }

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
    for (int i = 0; i < number; i++) {
        assertTrue(!table.isSelected(i));
    }
	table.setSelection(new TableItem[] {items[10]});
	for (int i = 0; i < number; i++) {
        if (i == 10) {
            assertTrue(table.isSelected(i));
        } else {
            assertTrue(!table.isSelected(i));
        }
	}

	table.setSelection(items);
	for (int i = 0; i < number; i++){
		assertTrue(!table.isSelected(i));
	}
}

@Test
public void test_remove$I() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(null), "No exception thrown for tableItems == null");

	assertThrows(IllegalArgumentException.class, () -> table.remove(new int[] {2, 1, 0, -100, 5, 5, 2, 1, 0, 0, 0}), "No exception thrown for illegal index arguments");

	assertThrows(IllegalArgumentException.class, () -> table.remove(new int[] {2, 1, 0, number, 5, 5, 2, 1, 0, 0, 0}), "No exception thrown for illegal index arguments");

	table.remove(new int[] {});

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertTrue(!items[2].isDisposed());
	table.remove(new int[] {2});
	assertTrue(items[2].isDisposed());
	assertEquals(number-1, table.getItemCount());

	assertTrue(!items[number-1].isDisposed());
	table.remove(new int[] {number-2});
	assertTrue(items[number-1].isDisposed());
	assertEquals(number-2, table.getItemCount());

	assertTrue(!items[3].isDisposed());
	table.remove(new int[] {2});
	assertTrue(items[3].isDisposed());
	assertEquals(number-3, table.getItemCount());

	assertTrue(!items[0].isDisposed());
	table.remove(new int[] {0});
	assertTrue(items[0].isDisposed());
	assertEquals(number-4, table.getItemCount());
}

@Test
public void test_removeAll() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.removeAll();

	assertEquals(0, table.getItemCount());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.removeAll();
	table.removeAll();

	assertArrayEquals(new TableItem[] {}, table.getItems());
}

@Test
public void test_removeII() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(-number, number + 100), "No exception thrown for illegal index range");

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(2, 3);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[4]}, table.getItems());
	// Make sure the removed items are disposed and other items are not.
	assertFalse(items[0].isDisposed());
	assertFalse(items[1].isDisposed());
	assertTrue(items[2].isDisposed());
	assertTrue(items[3].isDisposed());
	assertFalse(items[4].isDisposed());

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(2, 100), "No exception thrown for illegal index range");
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(2, number), "No exception thrown for illegal index range");
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(2, number-1);
	assertArrayEquals(new TableItem[] {items[0], items[1]}, table.getItems());
	for (int i = 0; i < 2; i++) {
		assertFalse(items[i].isDisposed());
	}
	for (int i = 2; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(0, 3);
	assertArrayEquals(new TableItem[] {items[4]}, table.getItems());
	for (int i = 0; i <= 3; i++) {
		assertTrue(items[i].isDisposed());
	}
	for (int i = 4; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(0, number-1);
	assertArrayEquals(new TableItem[] {}, table.getItems());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.remove(new int[] {});
	assertEquals(number, table.getItemCount());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(-20, -10), "No exception thrown for illegal index range");
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(20, 40), "No exception thrown for illegal index range");
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(200, 40);
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(2, 2);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[3], items[4]}, table.getItems());
	assertTrue(items[2].isDisposed());
	for (int i = 0; i < number && i != 2; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(0, 0);
	assertArrayEquals(new TableItem[]{items[1], items[2], items[3], items[4]}, table.getItems());
	assertTrue(items[0].isDisposed());
	for (int i = 1; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.remove(4, 4);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[2], items[3]}, table.getItems());
	assertTrue(items[4].isDisposed());
	for (int i = 0; i < number && i != 4; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertEquals(number, table.getItemCount());
	assertThrows(IllegalArgumentException.class, () -> table.remove(-10, 2), "No exception thrown for illegal index range");
	assertEquals(number, table.getItemCount());
	if (SwtTestUtil.fCheckSWTPolicy) {
		table.remove(10, 2);
		assertEquals(number, table.getItemCount());
	}
	table.remove(0, 2);
	assertEquals(number - 3, table.getItemCount());
	assertArrayEquals(new TableItem[] {items[3], items[4]}, table.getItems());
	for (int i = 0; i < 2; i++) {
		assertTrue(items[i].isDisposed());
	}
	for (int i = 3; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}
	assertThrows(IllegalArgumentException.class, () -> table.remove(1, 200), "No exception thrown for illegal index range");
	assertEquals(number - 3, table.getItemCount());
	assertArrayEquals(new TableItem[] {items[3], items[4]}, table.getItems());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.remove(0, number-1);
	assertEquals(0, table.getItemCount());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(number, number), "No exception thrown for illegal index range");

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.remove(number, number + 100), "No exception thrown for illegal index range");

	makeCleanEnvironment(false);

	int largerNumber = 15;
	items = new TableItem[largerNumber];
    for (int i = 0; i < largerNumber; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.remove(new int[] {2, 1, 0, 5, 5});
	assertEquals(largerNumber-4, table.getItemCount());
	assertTrue(items[0].isDisposed());
	assertTrue(items[1].isDisposed());
	assertTrue(items[2].isDisposed());
	assertFalse(items[3].isDisposed());
	assertFalse(items[4].isDisposed());
	assertTrue(items[5].isDisposed());
}

@Test
public void test_select$I() {
	assertThrows(IllegalArgumentException.class, () -> table.select(null), "No exception thrown for selection == null");

	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(new int[] {2, 10, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {number, 0, number-1});
	assertArrayEquals(new int[] {0, number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {number, 0, -1});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(new int[] {10});
	assertArrayEquals(new int[] {0, 10}, table.getSelectionIndices());

	table.select(new int[] {2});
	assertArrayEquals(new int[] {0, 2, 10}, table.getSelectionIndices());

	table.select(new int[] {14});
	assertArrayEquals(new int[] {0, 2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {15});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {4, 4, 4});
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertThrows(IllegalArgumentException.class, () -> table.select(null), "No exception thrown for selection == null");
	assertEquals(0, table.getSelectionCount());

	table.select(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(new int[] {10});
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(new int[] {2});
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());

	table.select(new int[] {14});
	assertArrayEquals(new int[] {14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {15});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {4, 4, 4});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_selectAll() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertArrayEquals(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertArrayEquals(new int[]{}, table.getSelectionIndices());
}

@Test
public void test_selectI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.select(7);
	assertArrayEquals(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertArrayEquals(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(0);
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(1);
	assertArrayEquals(new int[] {1}, table.getSelectionIndices());

	table.select(10);
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1);
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

}

@Test
public void test_selectII() {
	int number = 15;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.select(7);
	assertArrayEquals(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertArrayEquals(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	table.select(4, 10);
	assertArrayEquals(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 14}, table.getSelectionIndices());

	table.select(4, 14);
	assertArrayEquals(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(0, 7);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(9, 5);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(-100, 1000);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();
	assertEquals(0, table.getSelectionCount());
	table.select(0, 1000);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();

	table.select(0, number-1);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();

	table.select(0, 0);
	assertEquals(1, table.getSelectionCount());
	table.deselectAll();

	table.select(number-1, number);
	assertEquals(1, table.getSelectionCount());
	table.deselectAll();

	table.select(number, number);
	assertEquals(0, table.getSelectionCount());
	table.deselectAll();

	// note: SWT.SINGLE
	makeCleanEnvironment(true);
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.select(0, 0);
	assertEquals(1, table.getSelectionCount());
	assertEquals(0, table.getSelectionIndex());
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(4, 4);
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	table.select(10, 10);
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1, number-1);
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number, number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(0, number-1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(4, 5);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(5, 4);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, -1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_setColumnOrder$I() {
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	table.setColumnOrder(new int[0]);
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(null), "No exception thrown for null argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[1]), "No exception thrown for invalid argument");

	TableColumn column0 = new TableColumn(table, SWT.NONE);
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	assertArrayEquals(table.getColumnOrder(), new int[]{0, 1, 2});
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(null), "No exception thrown for null argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[0]), "No exception thrown for invalid argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[]{0,1}), "No exception thrown for invalid argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[]{0, 1, 2, 3}), "No exception thrown for invalid argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[]{0, 0, 1}), "No exception thrown for invalid argument");
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[]{3, 0, 1}), "No exception thrown for invalid argument");
	table.setColumnOrder(new int[]{2, 1, 0});
	assertArrayEquals(table.getColumnOrder(), new int[] {2, 1, 0});
	column2.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[] {1, 0});
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[]{0, 1, 2}), "No exception thrown for invalid argument");
	column1.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[]{0});
	column0.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	assertThrows(IllegalArgumentException.class, () -> table.setColumnOrder(new int[1]), "No exception thrown for invalid argument");
	Table table2 = new Table(table.getParent(), SWT.NONE);
	table2.dispose();
	assertThrows(SWTException.class, () -> table2.getColumnOrder(), "No exception thrown for widget is Disposed");
	assertThrows(SWTException.class, () -> table2.setColumnOrder(new int[0]), "No exception thrown for widget is Disposed");
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

@Test
public void test_setHeaderBackgroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(table.getHeaderBackground());
	Color color = new Color(12, 34, 56);
	table.setHeaderBackground(color);
	assertEquals(color, table.getHeaderBackground());
	table.setHeaderBackground(null);
	assertFalse(table.getHeaderBackground().equals(color));
}

@Test
public void test_setHeaderForegroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(table.getHeaderForeground());
	Color color = new Color(12, 34, 56);
	table.setHeaderForeground(color);
	assertEquals(color, table.getHeaderForeground());
	table.setHeaderForeground(null);
	assertFalse(table.getHeaderForeground().equals(color));
}

@Test
public void test_setHeaderVisibleZ() {
	assertFalse(table.getHeaderVisible());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderVisible());
	table.setHeaderVisible(false);
	assertFalse(table.getHeaderVisible());
}

@Test
public void test_setItemCountI() {
	table.removeAll();
	assertEquals(0, table.getItemCount());
	for (int i=0; i<8; i++) {
		new TableItem(table, SWT.NULL);
		assertEquals(i+1, table.getItemCount());
	}
	assertEquals(8, table.getItemCount());
	assertEquals(4, table.indexOf(table.getItems()[4]));
	table.getItem(1).dispose();
	assertEquals(7, table.getItemCount());
	new TableItem (table, SWT.NULL, 0);
	assertEquals(1, table.indexOf(table.getItems()[1]));
	assertEquals(8, table.getItemCount());
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	table.setItemCount(-1);
	assertEquals(0, table.getItemCount());
	table.setItemCount(10);
	assertEquals(10, table.getItemCount());
	table.getItem(1).dispose();
	assertEquals(9, table.getItemCount());
	assertEquals(4, table.indexOf(table.getItems()[4]));
	table.setItemCount(3);
	assertEquals(3, table.getItemCount());
	assertThrows(IllegalArgumentException.class, () -> table.getItem(4), "No exception thrown for illegal index argument");
	table.setItemCount(40);
	assertEquals(40, table.getItemCount());
	table.getItem(39);
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	assertThrows(IllegalArgumentException.class, () -> table.getItem(39), "No exception thrown for illegal index argument");
}

@Test
public void test_setLinesVisibleZ() {
	assertFalse(table.getLinesVisible());
	table.setLinesVisible(true);
	assertTrue(table.getLinesVisible());
	table.setLinesVisible(false);
	assertFalse(table.getLinesVisible());
}

@Override
@Test
public void test_setRedrawZ() {
}

@Test
public void test_setSelection$I() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.setSelection((int[]) null), "No exception thrown for selection == null");

	table.setSelection(new int[]{});
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new int[]{0, 3, 2});
	assertArrayEquals(new int[]{0, 2, 3}, table.getSelectionIndices());

	table.setSelection(new int[]{3, 2, 1});
	assertArrayEquals(new int[]{1, 2, 3}, table.getSelectionIndices());

	table.setSelection(new int[]{1, 4, 0});
	assertArrayEquals(new int[]{0, 1, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{0, 4, 0});
	assertArrayEquals(new int[]{0, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{2, 3, 4});
	assertArrayEquals(new int[]{2, 3, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{4, 4, 4, 4, 4, 4, 4});
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(new int[]{4});
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	assertThrows(IllegalArgumentException.class, () -> table.setSelection((int[]) null), "No exception thrown for selection range == null");
	assertEquals(0, table.getSelectionCount());

	table.setSelection(new int[] {});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.setSelection(new int[] {2});
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());

	table.setSelection(new int[] {4});
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	table.setSelection(new int[] {number-1});
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.setSelection(new int[] {number});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0, 3, 2});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {3, 2, 1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {4, 4, 4, 4, 4, 4, 4});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_setSelection$Lorg_eclipse_swt_widgets_TableItem() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	assertThrows(IllegalArgumentException.class, () -> table.setSelection((TableItem[]) null), "No exception thrown for selection range == null");
	assertEquals(0, table.getSelectionCount());

	assertThrows(IllegalArgumentException.class, () -> table.setSelection((TableItem) null), "No exception thrown for selection == null");
	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(items[0]);
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TableItem[]{items[0], items[2], items[3]}, table.getSelection());
	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TableItem[]{items[1], items[2], items[3]}, table.getSelection());

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TableItem[]{items[0], items[1], items[4]}, table.getSelection());
	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TableItem[]{items[0], items[4]}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TableItem[]{items[2], items[3], items[4]}, table.getSelection());

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());
	table.setSelection(new TableItem[]{items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());

	makeCleanEnvironment(false);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.setSelection(new TableItem[] {items[0]});
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertArrayEquals(new TableItem[] {items[3]}, table.getSelection());

	table.setSelection(new TableItem[] {items[4]});
	assertArrayEquals(new TableItem[] {items[4]}, table.getSelection());
	table.setSelection(new TableItem[] {items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());
	table.setSelection(new TableItem[] {items[1]});
	assertArrayEquals(new TableItem[] {items[1]}, table.getSelection());

	// test single-selection table
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.setSelection(new TableItem[]{});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());

	table.setSelection(new TableItem[] {items[0]});
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertArrayEquals(new TableItem[] {items[3]}, table.getSelection());

	table.setSelection(new TableItem[] {items[4]});
	assertArrayEquals(new TableItem[] {items[4]}, table.getSelection());

	table.setSelection(new TableItem[] {items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());

	table.setSelection(new TableItem[] {items[1]});
	assertArrayEquals(new TableItem[] {items[1]}, table.getSelection());

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());
}

@Test
public void test_setSelectionI() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.setSelection(0);
	assertArrayEquals(new int[]{0}, table.getSelectionIndices());
	table.setSelection(3);
	assertArrayEquals(new int[]{3}, table.getSelectionIndices());
	table.setSelection(4);
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1);
	assertArrayEquals(new int[]{1}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.setSelection(0);
	assertArrayEquals(new int[]{0}, table.getSelectionIndices());

	table.setSelection(3);
	assertArrayEquals(new int[]{3}, table.getSelectionIndices());

	table.setSelection(4);
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1);
	assertArrayEquals(new int[]{1}, table.getSelectionIndices());

}

@Test
public void test_setSelectionII() {
	int number = 5;
	TableItem[] items = new TableItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }

	table.setSelection(0, 1);
	assertArrayEquals(new int[]{0, 1}, table.getSelectionIndices());

	table.setSelection(2, 4);
	assertArrayEquals(new int[]{2, 3, 4}, table.getSelectionIndices());

	table.setSelection(3, 4);
	assertArrayEquals(new int[]{3, 4}, table.getSelectionIndices());

	table.setSelection(5, 4);
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(2, 2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1, 4);
	assertArrayEquals(new int[]{1, 2, 3, 4}, table.getSelectionIndices());

	table.setSelection(0, 4);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

    for (int i = 0; i < number; i++) {
        items[i] = new TableItem(table, 0);
    }
	table.setSelection(0, 1);
    if (SwtTestUtil.fCheckSWTPolicy) {
        assertArrayEquals(new int[]{1}, table.getSelectionIndices());
    }
	table.setSelection(2, 4);
    if (SwtTestUtil.fCheckSWTPolicy) {
        assertArrayEquals(new int[]{4}, table.getSelectionIndices());
    }
	table.setSelection(5, 4);
    if (SwtTestUtil.fCheckSWTPolicy) {
        assertArrayEquals(new int[]{}, table.getSelectionIndices());
    }
	table.setSelection(2, 2);
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());
	table.setSelection(1, 4);
    if (SwtTestUtil.fCheckSWTPolicy) {
        assertArrayEquals(new int[]{4}, table.getSelectionIndices());
    }
	table.setSelection(0, 4);
    if (SwtTestUtil.fCheckSWTPolicy) {
        assertArrayEquals(new int[]{4}, table.getSelectionIndices());
    }
}

@Test
public void test_showSelection() {
	table.showSelection();
}

/* custom */
protected Table table;

private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
    if (table != null) {
        table.dispose();
    }
	table = new Table(shell, single?SWT.SINGLE:SWT.MULTI);
	setWidget(table);
}

private void createTable(List<String> events) {
	makeCleanEnvironment(false);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	for (int col = 0; col < 3; col++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Col " + col);
		column.setWidth(50);
		hookExpectedEvents(column, getTestName(), events);
	}
	for (int row = 0; row < 3; row++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		hookExpectedEvents(item, getTestName(), events);
	}
}

@Test
public void test_consistency_KeySelection() {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MouseSelection() {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyPrePackShell();
	consistencyEvent(20, table.getHeaderHeight() + table.getItemHeight()*2,
					 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DoubleClick () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyPrePackShell();
	consistencyEvent(20, table.getHeaderHeight()+ table.getItemHeight() + 5, 1, 0,
					 ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

@Test
public void test_consistency_EnterSelection () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MenuDetect () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(20, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DragDetect () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_Virtual() {
	table.dispose();
	table = new Table(shell, SWT.VIRTUAL | SWT.BORDER);
	setWidget(table);

	int count = 1_000_000;
	int visibleCount = 10;

	shell.setLayout(new FillLayout());
	final int[] dataCounter = { 0 };
	table.addListener(SWT.SetData, event -> {
		TableItem item = (TableItem) event.item;
		int index = table.indexOf(item);
		item.setText("Item " + index);
		dataCounter[0]++;
	});

	shell.setSize (200, table.getItemHeight() * visibleCount);
	shell.open ();
	table.setItemCount(count);

	long end = System.currentTimeMillis() + 3000;
	while (!shell.isDisposed() && System.currentTimeMillis() < end) {
		if (!shell.getDisplay().readAndDispatch ()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// the "* 3" allows some surplus for platforms that pre-fetch items to improve scrolling performance:
	assertTrue(dataCounter[0] > visibleCount / 2 && dataCounter[0] <= visibleCount * 3);
}

@Test
public void test_setTopIndex() {
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	table.setSize(50,50);
	shell.open();
	table.setTopIndex(5);
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	shell.setVisible(false);
	assertEquals(5, table.getTopIndex());
}
}

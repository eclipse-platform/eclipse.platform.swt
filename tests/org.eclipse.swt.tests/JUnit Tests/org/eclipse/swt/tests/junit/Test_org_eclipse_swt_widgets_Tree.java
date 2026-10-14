/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

import static java.lang.System.currentTimeMillis;
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
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tree
 *
 * @see org.eclipse.swt.widgets.Tree
 */
public class Test_org_eclipse_swt_widgets_Tree extends Test_org_eclipse_swt_widgets_Composite {

private Tree tree;

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.MULTI);
	setWidget(tree);
}


private Tree newVirtualTreeWithGtkLogicalMode(int style, String mode) {
	String property = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	String previous = System.getProperty(property);
	if (mode == null) System.clearProperty(property);
	else System.setProperty(property, mode);
	try {
		return new Tree(shell, style);
	} finally {
		if (previous == null) System.clearProperty(property);
		else System.setProperty(property, previous);
	}
}

private static boolean isGtk3() throws Exception {
	return "gtk".equals(SWT.getPlatform())
			&& !Class.forName("org.eclipse.swt.internal.gtk.GTK").getField("GTK4").getBoolean(null);
}

/** Samples the attached model, rather than the separate full logical model. */
private static int attachedGtkRootCount(Tree tree) throws Exception {
	Class<?> os = Class.forName("org.eclipse.swt.internal.gtk.OS");
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	long widget = Tree.class.getField("handle").getLong(tree);
	long[] model = {0};
	os.getMethod("g_object_get", long.class, byte[].class, long[].class, long.class)
			.invoke(null, widget, new byte[] {109, 111, 100, 101, 108, 0}, model, 0L);
	assertTrue(model[0] != 0, "the GtkTreeView must have an attached model");
	try {
		return (Integer) gtk.getMethod("gtk_tree_model_iter_n_children", long.class, long.class)
				.invoke(null, model[0], 0L);
	} finally {
		os.getMethod("g_object_unref", long.class).invoke(null, model[0]);
	}
}

@Test
public void test_virtualGtkSetDataExceptionAllowsRemovalAndRetry() throws Exception {
	if (!isGtk3()) return;
	for (String mode : new String[] {"true", "false"}) {
		Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, mode);
		virtualTree.setItemCount(3);
		TreeItem failedItem = virtualTree.getItem(1);
		RuntimeException failure = new RuntimeException("provider failure");
		Listener provider = event -> { throw failure; };
		virtualTree.addListener(SWT.SetData, provider);
		assertSame(failure, assertThrows(RuntimeException.class, failedItem::getText));
		virtualTree.removeListener(SWT.SetData, provider);

		// A completed callback, including an exceptional one, must not forbid removal.
		virtualTree.removeAll();
		assertTrue(failedItem.isDisposed());
		assertEquals(0, virtualTree.getItemCount());
		virtualTree.setItemCount(3);
		virtualTree.addListener(SWT.SetData, event -> ((TreeItem) event.item).setText("recovered"));
		assertEquals("recovered", virtualTree.getItem(2).getText());
		virtualTree.dispose();
	}
}

@Test
public void test_virtualGtkNestedSetDataRetainsOuterProviderContext() throws Exception {
	if (!isGtk3()) return;
	Field currentItem = Tree.class.getDeclaredField("currentItem");
	currentItem.setAccessible(true);
	for (String mode : new String[] {"true", "false"}) {
		Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, mode);
		virtualTree.setItemCount(2);
		TreeItem outer = virtualTree.getItem(0);
		TreeItem inner = virtualTree.getItem(1);
		List<Integer> requested = new ArrayList<>();
		virtualTree.addListener(SWT.SetData, event -> {
			requested.add(event.index);
			TreeItem item = (TreeItem) event.item;
			if (item == outer) {
				assertEquals("inner", inner.getText());
				try {
					assertSame(outer, currentItem.get(virtualTree),
							"nested providers must retain the outer reconciliation boundary");
				} catch (IllegalAccessException failure) {
					throw new AssertionError(failure);
				}
				item.setText("outer");
			} else {
				item.setText("inner");
			}
		});
		assertEquals("outer", outer.getText());
		assertEquals(List.of(0, 1), requested);
		assertNull(currentItem.get(virtualTree), "the outermost callback must release the boundary");
		virtualTree.removeAll();
		virtualTree.dispose();
	}
}

@Test
public void test_virtualGtkSetDataDisposalReleasesCallbackState() throws Exception {
	if (!isGtk3()) return;
	for (String mode : new String[] {"true", "false"}) {
		Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, mode);
		virtualTree.setItemCount(1);
		TreeItem item = virtualTree.getItem(0);
		virtualTree.addListener(SWT.SetData, event -> virtualTree.dispose());
		org.eclipse.swt.SWTException failure = assertThrows(org.eclipse.swt.SWTException.class, item::getText);
		assertEquals(SWT.ERROR_WIDGET_DISPOSED, failure.code);
		assertTrue(virtualTree.isDisposed());
		Field currentItem = Tree.class.getDeclaredField("currentItem");
		currentItem.setAccessible(true);
		assertNull(currentItem.get(virtualTree));
	}
}

@Test
public void test_virtualGtk3LogicalModeOwnsFullRangeAndBoundsAttachedModel() throws Exception {
	if (!isGtk3()) return;
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL | SWT.V_SCROLL, "true");
	Method boundedView = Tree.class.getDeclaredMethod("usesBoundedVirtualView");
	boundedView.setAccessible(true);
	assertTrue((Boolean) boundedView.invoke(virtualTree),
			"the explicit logical mode must separate the full model from the bounded GTK view");
	assertEquals(0, virtualTree.getListeners(SWT.SetData).length);
	virtualTree.setItemCount(1_000_000);
	virtualTree.setBounds(0, 0, 360, 220);
	shell.setSize(400, 280);
	shell.open();
	SwtTestUtil.processEvents();

	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Field logicalModel = Tree.class.getDeclaredField("modelHandle");
	Field logicalAdjustment = Tree.class.getDeclaredField("verticalAdjustment");
	Field rowExtent = Tree.class.getDeclaredField("virtualRowExtent");
	logicalModel.setAccessible(true);
	logicalAdjustment.setAccessible(true);
	rowExtent.setAccessible(true);
	Method childCount = gtk.getMethod("gtk_tree_model_iter_n_children", long.class, long.class);
	Method upper = gtk.getMethod("gtk_adjustment_get_upper", long.class);
	Method scroll = gtk.getMethod("gtk_adjustment_set_value", long.class, double.class);
	assertEquals(1_000_000, childCount.invoke(null, logicalModel.getLong(virtualTree), 0L));
	assertEquals(1_000_000d * rowExtent.getInt(virtualTree),
			(Double) upper.invoke(null, logicalAdjustment.getLong(virtualTree)), 0.01);
	int initialRows = attachedGtkRootCount(virtualTree);
	assertTrue(initialRows > 0 && initialRows < 128,
			"only the visible window and overscan belong in the attached native model: " + initialRows);

	for (int index : new int[] {750_000, 999_900, 10}) {
		scroll.invoke(null, logicalAdjustment.getLong(virtualTree), (double) index * rowExtent.getInt(virtualTree));
		SwtTestUtil.processEvents();
		assertSame(virtualTree.getItem(index), virtualTree.getTopItem(),
				"the logical scrollbar must reach a distant row without extending a native prefix");
		assertEquals(1_000_000, virtualTree.getItemCount());
		int nativeRows = attachedGtkRootCount(virtualTree);
		assertTrue(nativeRows > 0 && nativeRows < 128,
				"distant seeks must replace the bounded native window: " + nativeRows);
	}
	virtualTree.setItemCount(7);
	SwtTestUtil.processEvents();
	assertEquals(7, virtualTree.getItemCount());
	assertTrue(virtualTree.indexOf(virtualTree.getTopItem()) < 7);
	assertTrue(attachedGtkRootCount(virtualTree) <= 7);
}

@Test
public void test_virtualGtk3ColumnPackMeasuresPopulatedSparseRowsWithoutProviders() throws Exception {
	if (!isGtk3()) return;
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "true");
	virtualTree.setItemCount(1_000_000);
	TreeColumn column = new TreeColumn(virtualTree, SWT.NONE);
	TreeItem distant = virtualTree.getItem(750_000);
	distant.setText("parent");
	distant.setItemCount(1_000_000);
	TreeItem child = distant.getItem(900_000);
	String text = "Retained content beyond the visible window ".repeat(8);
	child.setText(text);
	distant.setExpanded(true);
	Listener coldProvider = event -> {
		throw new AssertionError("column packing requested cold logical row " + event.index);
	};
	virtualTree.addListener(SWT.SetData, coldProvider);
	int textWidth;
	GC gc = new GC(virtualTree);
	try {
		textWidth = gc.textExtent(text).x;
	} finally {
		gc.dispose();
	}
	column.pack();
	assertTrue(column.getWidth() >= textWidth,
			"packing must read populated rows from the packed cache state, including distant children");
	assertEquals(1_000_000, virtualTree.getItemCount());
	assertEquals(1_000_000, distant.getItemCount());
	assertSame(child, distant.getItem(900_000));
	int wide = column.getWidth();
	child.setText("small");
	column.pack();
	assertTrue(column.getWidth() < wide, "packing must use current payloads and release a stale width");
	virtualTree.removeListener(SWT.SetData, coldProvider);
}

@Test
public void test_virtualGtk3ComputeSizeDoesNotEnumerateColdLogicalRange() throws Exception {
	if (!isGtk3()) return;
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "true");
	virtualTree.setItemCount(1_000_000);
	int[] requested = {0};
	virtualTree.addListener(SWT.SetData, event -> {
		assertTrue(++requested[0] < 128, "preferred size must not traverse the cold logical domain");
		((TreeItem) event.item).setText("row " + event.index);
	});
	virtualTree.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	assertEquals(1_000_000, virtualTree.getItemCount());
	Field topologyField = Tree.class.getDeclaredField("virtualTopology");
	topologyField.setAccessible(true);
	Object topology = topologyField.get(virtualTree);
	Method materializedCount = topology.getClass().getDeclaredMethod("materializedCount");
	materializedCount.setAccessible(true);
	assertTrue((Integer) materializedCount.invoke(topology) < 128,
			"native preferred size may prepare a viewport, but must not materialize a logical prefix");
}


@Test
public void test_virtualGtk3LogicalNativeModelKeepsDistantAccessSparseAndStable() throws Exception {
	if (!"gtk".equals(SWT.getPlatform())) return;
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Field gtk4Field = gtk.getDeclaredField("GTK4");
	gtk4Field.setAccessible(true);
	if (gtk4Field.getBoolean(null)) return;

	String property = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	String previous = System.getProperty(property);
	System.setProperty(property, "true");
	try {
		Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.MULTI | SWT.V_SCROLL | SWT.CHECK);
		virtualTree.setItemCount(1_000_000);
		shell.setLayout(new FillLayout());
		shell.setSize(360, 220);

		Method usesVirtualNativeModel = Tree.class.getDeclaredMethod("usesVirtualNativeModel");
		usesVirtualNativeModel.setAccessible(true);
		assertTrue((Boolean) usesVirtualNativeModel.invoke(virtualTree),
				"the full GTK3 logical native model must accept an explicit true override");

		Field topologyField = Tree.class.getDeclaredField("virtualTopology");
		topologyField.setAccessible(true);
		Object topology = topologyField.get(virtualTree);
		Method materializedCount = topology.getClass().getDeclaredMethod("materializedCount");
		materializedCount.setAccessible(true);
		// Include the top anchor that mutations preserve in the cold baseline.
		TreeItem initialTop = virtualTree.getTopItem();
		assertNotNull(initialTop);
		int coldFacades = ((Integer) materializedCount.invoke(topology)).intValue();
		assertTrue(coldFacades < 128, "native view preparation must be bounded: " + coldFacades);

		TreeItem distant = virtualTree.getItem(750_000);
		distant.setText("distant");
		distant.setItemCount(16);
		TreeItem child = distant.getItem(5);
		child.setText("child");
		// Counts must be visible through JNI immediately, without pumping the UI.
		distant.setItemCount(32);
		assertSame(child, distant.getItem(5));
		TreeItem removed = distant.getItem(31);
		removed.setItemCount(4);
		TreeItem removedLeaf = removed.getItem(2);
		distant.setItemCount(8);
		assertTrue(removed.isDisposed());
		assertTrue(removedLeaf.isDisposed());
		assertSame(child, distant.getItem(5));
		assertThrows(IllegalArgumentException.class, () -> distant.getItem(8));
		distant.setItemCount(16);
		int touchedFacades = ((Integer) materializedCount.invoke(topology)).intValue();
		assertTrue(touchedFacades <= coldFacades + 2,
				"unpainted lookup may add only the retained branch and child: "
				+ coldFacades + " -> " + touchedFacades);
		shell.open();
		SwtTestUtil.processEvents();
		distant.setExpanded(true);
		virtualTree.setSelection(distant);
		virtualTree.setTopItem(distant);
		SwtTestUtil.processEvents();

		int paintedFacades = ((Integer) materializedCount.invoke(topology)).intValue();
		assertTrue(paintedFacades < 128,
				"painted distant access must stay within the small viewport budget: " + paintedFacades);

		TreeItem inserted = new TreeItem(virtualTree, SWT.NONE, 3);
		inserted.setText("inserted");
		SwtTestUtil.processEvents();

		assertSame(initialTop, virtualTree.getItem(0));
		assertSame(distant, virtualTree.getItem(750_001),
				"snapshot refresh must preserve exposed facade identity after coordinate shift");
		assertSame(child, distant.getItem(5));
		assertTrue(distant.getExpanded(),
				"snapshot refresh must restore expansion state");
		assertArrayEquals(new TreeItem[] {distant}, virtualTree.getSelection(),
				"snapshot refresh must restore selection");
		assertSame(distant, virtualTree.getTopItem(),
				"snapshot refresh must restore the logical top item");
		int shiftedFacades = ((Integer) materializedCount.invoke(topology)).intValue();
		assertTrue(shiftedFacades < 128,
				"explicit insertion must not materialize the cold million-row prefix: " + shiftedFacades);

		distant.setChecked(true);
		distant.setGrayed(true);
		Display display = virtualTree.getDisplay();
		distant.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		distant.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		assertTrue(distant.getChecked());
		assertTrue(distant.getGrayed());

		TreeColumn firstColumn = new TreeColumn(virtualTree, SWT.NONE);
		firstColumn.setText("logical");
		TreeColumn secondColumn = new TreeColumn(virtualTree, SWT.NONE);
		secondColumn.setText("temporary");
		secondColumn.dispose();
		assertEquals(1, virtualTree.getColumnCount(),
				"column mutation must not replace the logical native model");

		distant.removeAll();
		assertEquals(0, distant.getItemCount(),
				"TreeItem.removeAll must mutate topology without GtkTreeStore");
		virtualTree.removeAll();
		assertEquals(0, virtualTree.getItemCount());
		assertEquals(0, ((Integer) materializedCount.invoke(topology)).intValue());
	} finally {
		if (previous == null) System.clearProperty(property);
		else System.setProperty(property, previous);
	}
}


@Test
public void test_virtualDndProjectionPinsSparseTreeFacade() throws Exception {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTree.setItemCount(100_000);
	shell.setLayout(new FillLayout());
	shell.setSize(320, 180);
	shell.open();
	SwtTestUtil.processEvents();

	DropTargetEffect effect = new DropTargetEffect(virtualTree);
	org.eclipse.swt.graphics.Point global =
			virtualTree.toDisplay(5, Math.max(1, virtualTree.getItemHeight() / 2));
	org.eclipse.swt.widgets.Widget hit = effect.getItem(global.x, global.y);
	TreeItem item = assertInstanceOf(TreeItem.class, hit);
	assertSame(item, effect.getItem(global.x, global.y),
			"DND projection must preserve the same logical TreeItem facade");

	Class<?> stateClass = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	Field pinnedField = stateClass.getDeclaredField("PINNED");
	pinnedField.setAccessible(true);
	long pinned = pinnedField.getLong(null);

	if ("cocoa".equals(SWT.getPlatform())) {
		Field storageField = Tree.class.getDeclaredField("virtualItems");
		storageField.setAccessible(true);
		Object storage = storageField.get(virtualTree);
		assertNotNull(storage);

		Method size = storage.getClass().getDeclaredMethod("size");
		size.setAccessible(true);
		assertTrue((Integer) size.invoke(storage) < 128,
				"DND hit projection must remain viewport-bounded for a 100K-root Cocoa Tree");

		Method stateOfIdentity =
				storage.getClass().getDeclaredMethod("stateOfIdentity", Object.class);
		stateOfIdentity.setAccessible(true);
		long state = (Long) stateOfIdentity.invoke(storage, item);
		assertTrue((state & pinned) != 0,
				"DND projection must pin the exposed Cocoa TreeItem identity");
	} else {
		Field topologyField = Tree.class.getDeclaredField("virtualTopology");
		topologyField.setAccessible(true);
		Object topology = topologyField.get(virtualTree);
		assertNotNull(topology);

		Method materializedCount = topology.getClass().getDeclaredMethod("materializedCount");
		materializedCount.setAccessible(true);
		assertTrue((Integer) materializedCount.invoke(topology) < 256,
				"DND hit projection must remain viewport-bounded for a 100K-root virtual Tree");

		Method virtualItemId = Tree.class.getDeclaredMethod("virtualItemId", TreeItem.class);
		virtualItemId.setAccessible(true);
		int id = (Integer) virtualItemId.invoke(virtualTree, item);
		assertTrue(id >= 0);

		Method flag = topology.getClass().getDeclaredMethod("flag", int.class, long.class);
		flag.setAccessible(true);
		assertTrue((Boolean) flag.invoke(topology, id, pinned),
				"DND projection must pin the exposed virtual TreeItem identity");
	}

	virtualTree.dispose();
}

@Test
public void test_virtualTreeEditorTracksPinnedItemAcrossViewportAndCollapse() {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTree.setSize(320, 180);
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(256);
	TreeItem edited = root.getItem(128);
	edited.setText("edited");
	root.setExpanded(true);

	org.eclipse.swt.custom.TreeEditor cellEditor =
			new org.eclipse.swt.custom.TreeEditor(virtualTree);
	cellEditor.grabHorizontal = true;
	Text control = new Text(virtualTree, SWT.NONE);
	cellEditor.setEditor(control, edited, 0);

	virtualTree.setTopItem(edited);
	ScrollBar vertical = virtualTree.getVerticalBar();
    if (vertical != null) {
        vertical.notifyListeners(SWT.Selection, new Event());
    }
	cellEditor.layout();

	assertSame(edited, cellEditor.getItem());
	assertSame(edited, root.getItem(128));
	Rectangle cell = edited.getBounds(0);
	Rectangle overlay = control.getBounds();
	assertEquals(cell.y, overlay.y,
			"editor overlay must follow the logical tree row after viewport scroll");

	root.setExpanded(false);
	assertFalse(edited.isDisposed(),
			"collapse compaction must preserve the TreeItem facade held by TreeEditor");
	assertSame(edited, cellEditor.getItem());

	root.setExpanded(true);
	virtualTree.setTopItem(edited);
    if (vertical != null) {
        vertical.notifyListeners(SWT.Selection, new Event());
    }
	cellEditor.layout();
	assertSame(edited, root.getItem(128),
			"re-expansion must restore the same editor-bound TreeItem facade");
	assertEquals(edited.getBounds(0).y, control.getBounds().y);

	cellEditor.dispose();
	control.dispose();
	virtualTree.dispose();
}


@Test
public void test_virtualDndHitPinsColdTreeChildAcrossCollapseCompaction() throws Exception {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTree.setBounds(0, 0, 320, 200);
	virtualTree.setItemCount(1);
	shell.setSize(360, 260);
	shell.open();
	while (shell.getDisplay().readAndDispatch()) {
		// drain native layout/paint work before coordinate hit testing
	}

	org.eclipse.swt.dnd.DropTargetEffect effect =
			new org.eclipse.swt.dnd.DropTargetEffect(virtualTree);
	int rowHeight = Math.max(1, virtualTree.getItemHeight());
	org.eclipse.swt.graphics.Point rootPoint =
			virtualTree.toDisplay(4, Math.max(1, rowHeight / 2));
	TreeItem root = (TreeItem) effect.getItem(rootPoint.x, rootPoint.y);
	assertNotNull(root);
	assertEquals(0, virtualTree.indexOf(root));

	root.setItemCount(1_024);
	root.setExpanded(true);
	while (shell.getDisplay().readAndDispatch()) {
		// allow native expansion to publish the first child row
	}

	org.eclipse.swt.graphics.Point childPoint =
			virtualTree.toDisplay(12, rowHeight + Math.max(1, rowHeight / 2));
	TreeItem child = (TreeItem) effect.getItem(childPoint.x, childPoint.y);
	assertNotNull(child);
	assertSame(root, child.getParentItem());

	int residentCount;
	if ("cocoa".equals(SWT.getPlatform())) {
		Method pinnedMethod = child.getClass().getDeclaredMethod("isVirtualFacadePinned");
		pinnedMethod.setAccessible(true);
		assertTrue((Boolean) pinnedMethod.invoke(child),
				"a TreeItem exposed by DND hit-testing must be pinned");

		Method storageMethod = Tree.class.getDeclaredMethod("virtualStorage", TreeItem.class);
		storageMethod.setAccessible(true);
		Object storage = storageMethod.invoke(virtualTree, root);
		Field sizeField = storage.getClass().getDeclaredField("size");
		sizeField.setAccessible(true);
		residentCount = sizeField.getInt(storage);
	} else {
		Field topologyField = Tree.class.getDeclaredField("virtualTopology");
		topologyField.setAccessible(true);
		Object topology = topologyField.get(virtualTree);
		assertNotNull(topology);

		Method itemId = Tree.class.getDeclaredMethod("virtualItemId", TreeItem.class);
		itemId.setAccessible(true);
		int childId = ((Number) itemId.invoke(virtualTree, child)).intValue();

		Method stateMethod = topology.getClass().getDeclaredMethod("state", int.class);
		stateMethod.setAccessible(true);
		long state = ((Number) stateMethod.invoke(topology, childId)).longValue();

		Field pinnedField = Class.forName("org.eclipse.swt.widgets.VirtualItemState")
				.getDeclaredField("PINNED");
		pinnedField.setAccessible(true);
		long pinned = pinnedField.getLong(null);
		assertTrue((state & pinned) != 0,
				"a TreeItem exposed by DND hit-testing must be pinned");

		Method materializedCount = topology.getClass().getDeclaredMethod("materializedCount");
		materializedCount.setAccessible(true);
		residentCount = ((Number) materializedCount.invoke(topology)).intValue();
	}
	assertTrue(residentCount < 128,
			"DND hit-testing must not materialize the 1K cold sibling range");

	root.setExpanded(false);
	assertFalse(child.isDisposed(),
			"collapse compaction must preserve a DND-exposed pinned child facade");
	root.setExpanded(true);
	assertSame(child, root.getItem(0),
			"re-expansion must restore the same DND-exposed TreeItem facade");

	virtualTree.dispose();
}

@Test
public void test_virtualTreeNativeModelSnapshotStaysSparseAcrossTenMillionRoots() throws Exception {
	Class<?> topologyType = Class.forName("org.eclipse.swt.widgets.VirtualTreeTopology");
	Constructor<?> topologyConstructor = topologyType.getDeclaredConstructor();
	topologyConstructor.setAccessible(true);
	Object topology = topologyConstructor.newInstance();

	Field rootField = topologyType.getDeclaredField("ROOT");
	rootField.setAccessible(true);
	int root = rootField.getInt(null);

	Method setChildCount = topologyType.getDeclaredMethod("setChildCount", int.class, int.class);
	Method bind = topologyType.getDeclaredMethod("bind", int.class, int.class, int.class);
	Method snapshotMethod = topologyType.getDeclaredMethod("nativeModelSnapshot");
	for (Method method : new Method[] {setChildCount, bind, snapshotMethod}) {
		method.setAccessible(true);
	}

	setChildCount.invoke(topology, root, 10_000_000);
	bind.invoke(topology, 2, root, 9);
	setChildCount.invoke(topology, 2, 3);
	bind.invoke(topology, 7, 2, 1);

	int [] snapshot = (int []) snapshotMethod.invoke(topology);
	int capacity = snapshot [0];
	assertEquals(10_000_000, snapshot [1]);
	assertTrue(capacity < 64,
			"native logical Tree snapshot must scale with sparse topology capacity, not logical roots");
	assertEquals(2 + capacity * 3, snapshot.length);

	int parentOffset = 2;
	int indexOffset = 2 + capacity;
	int countOffset = 2 + capacity * 2;
	assertEquals(root, snapshot [parentOffset + 2]);
	assertEquals(9, snapshot [indexOffset + 2]);
	assertEquals(3, snapshot [countOffset + 2]);
	assertEquals(2, snapshot [parentOffset + 7]);
	assertEquals(1, snapshot [indexOffset + 7]);
	assertEquals(-1, snapshot [countOffset + 7],
			"unobserved child count stays unknown in the rebuildable native snapshot");
}

@Test
public void test_virtualTreeVisibleProjectionSkipsColdLogicalRanges() throws Exception {
	Class<?> topologyType = Class.forName("org.eclipse.swt.widgets.VirtualTreeTopology");
	Constructor<?> topologyConstructor = topologyType.getDeclaredConstructor();
	topologyConstructor.setAccessible(true);
	Object topology = topologyConstructor.newInstance();

	Field rootField = topologyType.getDeclaredField("ROOT");
	rootField.setAccessible(true);
	int root = rootField.getInt(null);

	Method bind = topologyType.getDeclaredMethod("bind", int.class, int.class, int.class);
	Method setChildCount = topologyType.getDeclaredMethod("setChildCount", int.class, int.class);
	Method flag = topologyType.getDeclaredMethod("flag", int.class, long.class, boolean.class);
	Method insertCoordinate = topologyType.getDeclaredMethod("insertCoordinate", int.class, int.class, int.class);
	Method releaseSubtree = topologyType.getDeclaredMethod("releaseSubtree", int.class);
	Method materializedCount = topologyType.getDeclaredMethod("materializedCount");
	for (Method method : new Method[] {
			bind, setChildCount, flag, insertCoordinate, releaseSubtree, materializedCount}) {
		method.setAccessible(true);
	}

	Class<?> stateType = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	Field expandedField = stateType.getDeclaredField("EXPANDED");
	expandedField.setAccessible(true);
	long expanded = expandedField.getLong(null);

	setChildCount.invoke(topology, root, 10_000_000);
	bind.invoke(topology, 1, root, 5);
	setChildCount.invoke(topology, 1, 3);
	flag.invoke(topology, 1, expanded, true);

	bind.invoke(topology, 2, root, 1_000);
	setChildCount.invoke(topology, 2, 2);

	bind.invoke(topology, 3, 1, 1);
	setChildCount.invoke(topology, 3, 2);
	flag.invoke(topology, 3, expanded, true);

	assertEquals(3, ((Integer) materializedCount.invoke(topology)).intValue(),
			"ten million logical roots must retain only the observed topology nodes");

	Class<?> projectionType = Class.forName("org.eclipse.swt.widgets.VirtualTreeVisibleProjection");
	Constructor<?> projectionConstructor = projectionType.getDeclaredConstructor(topologyType);
	projectionConstructor.setAccessible(true);
	Object projection = projectionConstructor.newInstance(topology);

	Method visibleRowCount = projectionType.getDeclaredMethod("visibleRowCount");
	Method rowAt = projectionType.getDeclaredMethod("rowAt", long.class);
	Method visibleIndexOf = projectionType.getDeclaredMethod("visibleIndexOf", int.class);
	visibleRowCount.setAccessible(true);
	rowAt.setAccessible(true);
	visibleIndexOf.setAccessible(true);

	assertEquals(10_000_005L, ((Long) visibleRowCount.invoke(projection)).longValue());
	assertEquals(5L, ((Long) visibleIndexOf.invoke(projection, 1)).longValue());
	assertEquals(7L, ((Long) visibleIndexOf.invoke(projection, 3)).longValue());
	assertEquals(1_005L, ((Long) visibleIndexOf.invoke(projection, 2)).longValue());

	Object rootFive = rowAt.invoke(projection, 5L);
	Class<?> rowType = rootFive.getClass();
	Method rowParent = rowType.getDeclaredMethod("parentId");
	Method rowChild = rowType.getDeclaredMethod("childIndex");
	Method rowId = rowType.getDeclaredMethod("materializedId");
	Method rowDepth = rowType.getDeclaredMethod("depth");
    for (Method method : new Method[]{rowParent, rowChild, rowId, rowDepth}) {
        method.setAccessible(true);
    }

	assertEquals(root, ((Integer) rowParent.invoke(rootFive)).intValue());
	assertEquals(5, ((Integer) rowChild.invoke(rootFive)).intValue());
	assertEquals(1, ((Integer) rowId.invoke(rootFive)).intValue());
	assertEquals(0, ((Integer) rowDepth.invoke(rootFive)).intValue());

	Object nested = rowAt.invoke(projection, 8L);
	assertEquals(3, ((Integer) rowParent.invoke(nested)).intValue());
	assertEquals(0, ((Integer) rowChild.invoke(nested)).intValue());
	assertEquals(-1, ((Integer) rowId.invoke(nested)).intValue());
	assertEquals(2, ((Integer) rowDepth.invoke(nested)).intValue());

	Object afterExpandedSubtree = rowAt.invoke(projection, 11L);
	assertEquals(root, ((Integer) rowParent.invoke(afterExpandedSubtree)).intValue());
	assertEquals(6, ((Integer) rowChild.invoke(afterExpandedSubtree)).intValue());
	assertEquals(-1, ((Integer) rowId.invoke(afterExpandedSubtree)).intValue());

	flag.invoke(topology, 1, expanded, false);
	assertEquals(10_000_000L, ((Long) visibleRowCount.invoke(projection)).longValue());
	assertEquals(-1L, ((Long) visibleIndexOf.invoke(projection, 3)).longValue());
	Object collapsedNext = rowAt.invoke(projection, 6L);
	assertEquals(root, ((Integer) rowParent.invoke(collapsedNext)).intValue());
	assertEquals(6, ((Integer) rowChild.invoke(collapsedNext)).intValue());

	flag.invoke(topology, 1, expanded, true);
	insertCoordinate.invoke(topology, root, 2, 4);
	assertEquals(10_000_006L, ((Long) visibleRowCount.invoke(projection)).longValue());
	assertEquals(6L, ((Long) visibleIndexOf.invoke(projection, 1)).longValue());
	assertEquals(8L, ((Long) visibleIndexOf.invoke(projection, 3)).longValue());
	assertEquals(1_006L, ((Long) visibleIndexOf.invoke(projection, 2)).longValue());

	releaseSubtree.invoke(topology, 4);
	assertEquals(10_000_005L, ((Long) visibleRowCount.invoke(projection)).longValue());
	assertEquals(5L, ((Long) visibleIndexOf.invoke(projection, 1)).longValue());

	setChildCount.invoke(topology, root, 5);
	assertEquals(5L, ((Long) visibleRowCount.invoke(projection)).longValue());
	assertEquals(0, ((Integer) materializedCount.invoke(topology)).intValue(),
			"root shrink must prune materialized coordinates outside the logical range");
}


@Test
public void test_virtualGtkAndWin32VisibleProjectionTracksExpansionIndependentlyOfResidency() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(100);
	TreeItem root = virtualTree.getItem(5);
	root.setItemCount(3);
	TreeItem child = root.getItem(1);
	child.setItemCount(2);

	Method visibleRows = Tree.class.getDeclaredMethod("virtualVisibleRowCount");
	visibleRows.setAccessible(true);

	assertEquals(100L, ((Long) visibleRows.invoke(virtualTree)).longValue());
	root.setExpanded(true);
	assertEquals(103L, ((Long) visibleRows.invoke(virtualTree)).longValue());
	child.setExpanded(true);
	assertEquals(105L, ((Long) visibleRows.invoke(virtualTree)).longValue());

	root.setExpanded(false);
	assertEquals(100L, ((Long) visibleRows.invoke(virtualTree)).longValue(),
			"collapsed descendants must leave the logical visible-row projection");
	assertEquals(100, virtualTree.getItemCount());
	assertEquals(3, root.getItemCount());
	assertEquals(2, child.getItemCount(),
			"logical child counts remain independent of current native visibility/residency");
}

@Test
public void test_virtualGtkCollapseAllDoesNotMaterializeColdRoots() throws Exception {
	if (!"gtk".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.V_SCROLL);
	virtualTree.setItemCount(4096);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(4);
	TreeItem child = root.getItem(0);
	root.setExpanded(true);

	Field topologyField = Tree.class.getDeclaredField("virtualTopology");
	topologyField.setAccessible(true);
	Object topology = topologyField.get(virtualTree);
	assertNotNull(topology);
	Method materializedCount = topology.getClass().getDeclaredMethod("materializedCount");
	materializedCount.setAccessible(true);
	int before = ((Number) materializedCount.invoke(topology)).intValue();
	assertTrue(before < 32, "fixture must begin with only the touched virtual topology");

	virtualTree.collapseAll();

	int after = ((Number) materializedCount.invoke(topology)).intValue();
	assertTrue(after <= before,
			"collapseAll must traverse the existing GTK virtual topology without materializing cold roots");
	assertSame(root, virtualTree.getItem(0));
	assertSame(child, root.getItem(0));
	assertEquals(4096, virtualTree.getItemCount());
	assertEquals(4, root.getItemCount());
}

@Test
public void test_virtualItemResidencyDoesNotScaleWithLogicalCount() throws Exception {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(4096);

	Field itemsField = Tree.class.getDeclaredField("items");
	itemsField.setAccessible(true);
	TreeItem[] backing = (TreeItem[]) itemsField.get(virtualTree);
	assertTrue(backing.length <= 16, "virtual Tree must not allocate one Java slot per logical root");

	TreeItem last = virtualTree.getItem(4095);
	assertSame(last, virtualTree.getItem(4095));
}

@Test
public void test_virtualChildResidencyDoesNotScaleWithLogicalCount() throws Exception {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(4096);

	Object itemOwner = "cocoa".equals(SWT.getPlatform()) ? root : virtualTree;
	Field itemsField = itemOwner.getClass().getDeclaredField("items");
	itemsField.setAccessible(true);
	TreeItem[] backing = (TreeItem[]) itemsField.get(itemOwner);
	assertTrue(backing.length <= 16, "virtual TreeItem must not allocate one Java slot per logical child");

	TreeItem last = root.getItem(4095);
	assertSame(last, root.getItem(4095));
	backing = (TreeItem[]) itemsField.get(itemOwner);
	assertTrue(backing.length <= 16, "materializing one distant child must keep branch residency sparse");
}

@Test
public void test_virtualBranchResidencyDoesNotScaleWithLogicalChildCount() throws Exception {
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(4096);

	if ("cocoa".equals(SWT.getPlatform())) {
		Field itemsField = TreeItem.class.getDeclaredField("items");
		itemsField.setAccessible(true);
		TreeItem[] backing = (TreeItem[]) itemsField.get(root);
		assertTrue(backing.length <= 16, "Cocoa virtual TreeItem must not allocate one Java slot per logical child");
	}

	TreeItem last = root.getItem(4095);
	assertSame(last, root.getItem(4095));
}



@Test
public void test_virtualTreeVisibleRowCountUsesLogicalCountsPlusSparseExpansion() throws Exception {
	Class<?> topologyType = Class.forName("org.eclipse.swt.widgets.VirtualTreeTopology");
	var constructor = topologyType.getDeclaredConstructor();
	constructor.setAccessible(true);
	Object topology = constructor.newInstance();

	Method bind = topologyType.getDeclaredMethod("bind", int.class, int.class, int.class);
	Method setChildCount = topologyType.getDeclaredMethod("setChildCount", int.class, int.class);
	Method flag = topologyType.getDeclaredMethod("flag", int.class, long.class, boolean.class);
	Method visibleRowCount = topologyType.getDeclaredMethod("visibleRowCount");
	Method releaseSubtree = topologyType.getDeclaredMethod("releaseSubtree", int.class);
	for (Method method : new Method[] {bind, setChildCount, flag, visibleRowCount, releaseSubtree}) {
		method.setAccessible(true);
	}

	Field visibleExtraRowsField = topologyType.getDeclaredField("visibleExtraRows");
	Field childVisibleExtraSumsField = topologyType.getDeclaredField("childVisibleExtraSums");
	Field rootVisibleExtraRowsField = topologyType.getDeclaredField("rootVisibleExtraRows");
	for (Field field : new Field[] {
		visibleExtraRowsField, childVisibleExtraSumsField, rootVisibleExtraRowsField}) {
		field.setAccessible(true);
	}

	Class<?> stateType = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	Field expandedField = stateType.getDeclaredField("EXPANDED");
	expandedField.setAccessible(true);
	long expanded = expandedField.getLong(null);

	setChildCount.invoke(topology, -1, 1_000_000);
	assertEquals(1_000_000L, visibleRowCount.invoke(topology),
			"cold logical children contribute one visible row each without materialization");

	bind.invoke(topology, 0, -1, 100);
	setChildCount.invoke(topology, 0, 100);
	assertEquals(1_000_000L, visibleRowCount.invoke(topology),
			"a collapsed materialized root must not add descendant rows");

	flag.invoke(topology, 0, expanded, true);
	assertEquals(1_000_100L, visibleRowCount.invoke(topology),
			"expanding one sparse root adds only its logical direct children");

	bind.invoke(topology, 1, 0, 20);
	setChildCount.invoke(topology, 1, 50);
	flag.invoke(topology, 1, expanded, true);
	assertEquals(1_000_150L, visibleRowCount.invoke(topology),
			"nested sparse expansion adds descendant rows without materializing cold siblings");

	bind.invoke(topology, 2, -1, 500);
	setChildCount.invoke(topology, 2, 10);
	flag.invoke(topology, 2, expanded, true);
	assertEquals(1_000_160L, visibleRowCount.invoke(topology));

	long[] visibleExtraRows = (long[]) visibleExtraRowsField.get(topology);
	long[] childVisibleExtraSums = (long[]) childVisibleExtraSumsField.get(topology);
	assertEquals(150L, visibleExtraRows[0],
			"expanded root aggregate includes direct logical rows plus expanded child extras");
	assertEquals(50L, childVisibleExtraSums[0]);
	assertEquals(160L, rootVisibleExtraRowsField.getLong(topology));

	flag.invoke(topology, 0, expanded, false);
	assertEquals(1_000_010L, visibleRowCount.invoke(topology),
			"collapsing a branch removes all of its visible descendants from the logical range");
	visibleExtraRows = (long[]) visibleExtraRowsField.get(topology);
	childVisibleExtraSums = (long[]) childVisibleExtraSumsField.get(topology);
	assertEquals(0L, visibleExtraRows[0],
			"collapsed node contributes no visible descendant rows to its parent");
	assertEquals(50L, childVisibleExtraSums[0],
			"hidden child aggregate is retained for O(depth) re-expansion");
	assertEquals(10L, rootVisibleExtraRowsField.getLong(topology));

	flag.invoke(topology, 0, expanded, true);
	assertEquals(1_000_160L, visibleRowCount.invoke(topology));
	assertEquals(160L, rootVisibleExtraRowsField.getLong(topology));

	setChildCount.invoke(topology, 1, 70);
	assertEquals(1_000_180L, visibleRowCount.invoke(topology),
			"child-count growth propagates only through expanded ancestors");
	visibleExtraRows = (long[]) visibleExtraRowsField.get(topology);
	childVisibleExtraSums = (long[]) childVisibleExtraSumsField.get(topology);
	assertEquals(70L, visibleExtraRows[1]);
	assertEquals(70L, childVisibleExtraSums[0]);
	assertEquals(170L, visibleExtraRows[0]);

	releaseSubtree.invoke(topology, 1);
	assertEquals(1_000_109L, visibleRowCount.invoke(topology),
			"subtree release removes its aggregate and the direct child row exactly once");
	assertEquals(109L, rootVisibleExtraRowsField.getLong(topology));
}


@Test
public void test_virtualTreeViewportUsesLogicalRowsAndBoundedOverscan() throws Exception {
	Class<?> topologyType = Class.forName("org.eclipse.swt.widgets.VirtualTreeTopology");
	Constructor<?> topologyConstructor = topologyType.getDeclaredConstructor();
	topologyConstructor.setAccessible(true);
	Object topology = topologyConstructor.newInstance();

	Method setChildCount = topologyType.getDeclaredMethod("setChildCount", int.class, int.class);
	Method bind = topologyType.getDeclaredMethod("bind", int.class, int.class, int.class);
	Method flag = topologyType.getDeclaredMethod("flag", int.class, long.class, boolean.class);
	setChildCount.setAccessible(true);
	bind.setAccessible(true);
	flag.setAccessible(true);

	Field rootField = topologyType.getDeclaredField("ROOT");
	rootField.setAccessible(true);
	int root = rootField.getInt(null);

	Class<?> stateType = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	Field expandedField = stateType.getDeclaredField("EXPANDED");
	expandedField.setAccessible(true);
	long expanded = expandedField.getLong(null);

	setChildCount.invoke(topology, root, 10_000_000);
	bind.invoke(topology, 0, root, 100);
	setChildCount.invoke(topology, 0, 1_000);
	flag.invoke(topology, 0, expanded, true);

	Class<?> projectionType = Class.forName("org.eclipse.swt.widgets.VirtualTreeVisibleProjection");
	Constructor<?> projectionConstructor = projectionType.getDeclaredConstructor(topologyType);
	projectionConstructor.setAccessible(true);
	Object projection = projectionConstructor.newInstance(topology);

	Class<?> viewportType = Class.forName("org.eclipse.swt.widgets.VirtualTreeViewport");
	Constructor<?> viewportConstructor = viewportType.getDeclaredConstructor(projectionType);
	viewportConstructor.setAccessible(true);
	Object viewport = viewportConstructor.newInstance(projection);

	Method configure = viewportType.getDeclaredMethod("configureGeometry", int.class, int.class);
	Method setTopRow = viewportType.getDeclaredMethod("setTopRow", long.class);
	Method topRow = viewportType.getDeclaredMethod("topRow");
	Method visibleRows = viewportType.getDeclaredMethod("visibleRows");
	Method firstPaintRow = viewportType.getDeclaredMethod("firstPaintRow");
	Method paintRowCount = viewportType.getDeclaredMethod("paintRowCount");
	Method visibleWindow = viewportType.getDeclaredMethod("visibleWindow");
	Method paintWindow = viewportType.getDeclaredMethod("paintWindow");
	Method maximum = viewportType.getDeclaredMethod("scrollbarMaximum");
	Method thumb = viewportType.getDeclaredMethod("scrollbarThumb");
	Method selection = viewportType.getDeclaredMethod("scrollbarSelection");
	for (Method method : new Method[] {
			configure, setTopRow, topRow, visibleRows, firstPaintRow,
			paintRowCount, visibleWindow, paintWindow, maximum, thumb, selection}) {
		method.setAccessible(true);
	}

	configure.invoke(viewport, 20, 200);
	assertEquals(10, visibleRows.invoke(viewport));
	assertEquals(10_001_000, maximum.invoke(viewport));
	assertEquals(10, thumb.invoke(viewport));

	setTopRow.invoke(viewport, 5_000_000L);
	assertEquals(5_000_000L, topRow.invoke(viewport));
	assertEquals(4_999_992L, firstPaintRow.invoke(viewport));
	assertEquals(26, paintRowCount.invoke(viewport),
			"paint window must be visible rows plus bounded eight-row overscan on each side");
	assertEquals(10, ((Object[]) visibleWindow.invoke(viewport)).length);
	assertEquals(26, ((Object[]) paintWindow.invoke(viewport)).length);
	assertEquals(5_000_000, selection.invoke(viewport));

	// Prove logical coordinates can exceed the native int scrollbar range.
	setChildCount.invoke(topology, root, Integer.MAX_VALUE);
	setChildCount.invoke(topology, 0, Integer.MAX_VALUE);
	configure.invoke(viewport, 20, 200);
	setTopRow.invoke(viewport, 3_000_000_000L);
	assertEquals(Integer.MAX_VALUE, maximum.invoke(viewport));
	assertTrue((Long) topRow.invoke(viewport) > Integer.MAX_VALUE,
			"logical top row must remain exact even when native scrollbar range saturates");
}

@Test
public void test_virtualGtkAndWin32SetTopItemMirrorsLogicalTopRow() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(200);
	shell.setLayout(new FillLayout());
	shell.setSize(240, 180);
	shell.open();
	SwtTestUtil.processEvents();

	TreeItem item = virtualTree.getItem(120);
	virtualTree.setTopItem(item);
	SwtTestUtil.processEvents();

	Method topRowMethod = Tree.class.getDeclaredMethod("virtualViewportTopRow");
	topRowMethod.setAccessible(true);
	long topRow = (Long) topRowMethod.invoke(virtualTree);
	assertEquals(120L, topRow,
			"setTopItem must mirror the public top item into logical visible-row coordinates");
	assertSame(item, virtualTree.getTopItem());
}

@Test
public void test_virtualGtkAndWin32TopologyStaysSparseAndTracksCoordinates() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(64);
	TreeItem root = virtualTree.getItem(20);
	root.setItemCount(32);
	TreeItem child = root.getItem(12);

	Field topologyField = Tree.class.getDeclaredField("virtualTopology");
	topologyField.setAccessible(true);
	Object topology = topologyField.get(virtualTree);
	assertNotNull(topology);

	Field treeItemsField = Tree.class.getDeclaredField("items");
	treeItemsField.setAccessible(true);
	TreeItem[] materialized = (TreeItem[]) treeItemsField.get(virtualTree);
	int rootId = -1, childId = -1;
	for (int id = 0; id < materialized.length; id++) {
        if (materialized[id] == root) {
            rootId = id;
        }
        if (materialized[id] == child) {
            childId = id;
        }
	}
	assertTrue(rootId >= 0);
	assertTrue(childId >= 0);

	Field parentIdsField = topology.getClass().getDeclaredField("parentIds");
	Field childIndicesField = topology.getClass().getDeclaredField("childIndices");
	Field childCountsField = topology.getClass().getDeclaredField("childCounts");
	Field rootChildCountField = topology.getClass().getDeclaredField("rootChildCount");
	Field materializedCountField = topology.getClass().getDeclaredField("materializedCount");
	for (Field field : new Field[] {
			parentIdsField, childIndicesField, childCountsField,
			rootChildCountField, materializedCountField}) {
		field.setAccessible(true);
	}

	int[] parentIds = (int[]) parentIdsField.get(topology);
	int[] childIndices = (int[]) childIndicesField.get(topology);
	int[] childCounts = (int[]) childCountsField.get(topology);
	assertEquals(64, rootChildCountField.getInt(topology));
	assertEquals(32, childCounts[rootId]);
	assertEquals(-1, parentIds[rootId]);
	assertEquals(20, childIndices[rootId]);
	assertEquals(rootId, parentIds[childId]);
	assertEquals(12, childIndices[childId]);
	assertTrue(materializedCountField.getInt(topology) <= 2,
			"logical child counts must not allocate topology slots for cold rows on " + platform);

	TreeItem insertedRoot = new TreeItem(virtualTree, SWT.NONE, 3);
	assertEquals(21, childIndices[rootId],
			"root coordinate must shift with insertion before it");
	assertEquals(65, rootChildCountField.getInt(topology));
	insertedRoot.dispose();
	assertEquals(20, childIndices[rootId]);
	assertEquals(64, rootChildCountField.getInt(topology));

	TreeItem insertedChild = new TreeItem(root, SWT.NONE, 3);
	assertEquals(13, childIndices[childId],
			"child coordinate must shift with sibling insertion");
	assertEquals(33, childCounts[rootId]);
	insertedChild.dispose();
	assertEquals(12, childIndices[childId]);
	assertEquals(32, childCounts[rootId]);

	root.setItemCount(5);
	assertEquals(5, root.getItemCount());
	assertTrue(child.isDisposed(), "materialized child outside the shrunken logical range must be disposed");
	assertEquals(5, childCounts[rootId]);
}




@Test
public void test_virtualGtkInsertAndAppendUseLogicalCountBeyondResidentPrefix() {
    if (!"gtk".equals(SWT.getPlatform())) {
        return;
    }
	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(2_000);
	TreeItem inserted = new TreeItem(virtualTree, SWT.NONE, 1_000);
	assertEquals(2_001, virtualTree.getItemCount());
	assertSame(inserted, virtualTree.getItem(1_000));
	TreeItem appended = new TreeItem(virtualTree, SWT.NONE);
	assertEquals(2_002, virtualTree.getItemCount());
	assertSame(appended, virtualTree.getItem(2_001));
	inserted.dispose();
	assertEquals(2_001, virtualTree.getItemCount());
	assertSame(appended, virtualTree.getItem(2_000));
}

@Test
public void test_virtualGtkQueuedFrontierRespectsShrunkLogicalCount() throws Exception {
    if (!"gtk".equals(SWT.getPlatform())) {
        return;
    }
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "false");
	virtualTree.setItemCount(10_000);
	TreeItem edge = virtualTree.getItem(250);
	Method request = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
	Method resident = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
	request.setAccessible(true);
	resident.setAccessible(true);
	request.invoke(virtualTree, edge);
	virtualTree.setItemCount(10);
	SwtTestUtil.processEvents();
	assertEquals(10, virtualTree.getItemCount());
	assertEquals(10, resident.invoke(virtualTree, 0L));
}

@Test
public void test_virtualGtkQueuedFrontierDoesNotRegrowCollapsedBranch() throws Exception {
    if (!"gtk".equals(SWT.getPlatform())) {
        return;
    }
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "false");
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(10_000);
	root.setExpanded(true);
	TreeItem edge = root.getItem(250);
	Method request = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
	Method resident = Tree.class.getDeclaredMethod("virtualResidentChildCount", TreeItem.class);
	request.setAccessible(true);
	resident.setAccessible(true);
	request.invoke(virtualTree, edge);
	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertFalse(root.getExpanded());
	assertEquals(10_000, root.getItemCount());
	assertEquals(251, resident.invoke(virtualTree, root));
	assertSame(edge, root.getItem(250));
}

@Test
public void test_gtkSetItemCountZeroRestoresRedraw() throws Exception {
    if (!"gtk".equals(SWT.getPlatform())) {
        return;
    }
	Tree regular = new Tree(shell, SWT.NONE);
	regular.setItemCount(2);
	regular.setItemCount(0);
	Field redraw = org.eclipse.swt.widgets.Control.class.getDeclaredField("drawCount");
	redraw.setAccessible(true);
	assertEquals(0, redraw.getInt(regular));
	assertEquals(0, regular.getItemCount());
}

@Test
public void test_virtualGtkNativeFrontierIsBoundedAndGrowsOnDemand() throws Exception {
	if (!"gtk".equals(SWT.getPlatform())) return;
	Class<?> gtk = Class.forName("org.eclipse.swt.internal.gtk.GTK");
	Field gtk4Field = gtk.getDeclaredField("GTK4");
	gtk4Field.setAccessible(true);
	if (gtk4Field.getBoolean(null)) return;

	String property = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	String previous = System.getProperty(property);
	System.setProperty(property, "false");
	try {
		Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
		virtualTree.setItemCount(1_000_000);
		assertEquals(1_000_000, virtualTree.getItemCount(),
				"logical root count must not be reduced to the native resident prefix");

		Method usesVirtualNativeModel = Tree.class.getDeclaredMethod("usesVirtualNativeModel");
		usesVirtualNativeModel.setAccessible(true);
		assertFalse((Boolean) usesVirtualNativeModel.invoke(virtualTree),
				"the legacy GtkTreeStore frontier must remain available through an explicit false override");

		Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
		Method requestFrontier = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
		residentCount.setAccessible(true);
		requestFrontier.setAccessible(true);

		assertEquals(256, residentCount.invoke(virtualTree, 0L),
				"one million logical roots must start with one bounded native frontier");

		TreeItem nearEdge = virtualTree.getItem(250);
		assertSame(nearEdge, virtualTree.getItem(250));
		assertEquals(256, residentCount.invoke(virtualTree, 0L));

		requestFrontier.invoke(virtualTree, nearEdge);
		SwtTestUtil.processEvents();
		assertEquals(512, residentCount.invoke(virtualTree, 0L),
				"near-edge demand must reveal exactly one additional native chunk");
		assertEquals(1_000_000, virtualTree.getItemCount());

		TreeItem distant = virtualTree.getItem(1_023);
		assertSame(distant, virtualTree.getItem(1_023));
		assertEquals(1_024, residentCount.invoke(virtualTree, 0L),
				"explicit indexed access may synchronously reconstruct only through its coordinate");
		assertEquals(1_000_000, virtualTree.getItemCount());
	} finally {
		if (previous == null) System.clearProperty(property);
		else System.setProperty(property, previous);
	}
}


@Test
public void test_virtualWin32NativeFrontierIsBoundedAndGrowsOnDemand() throws Exception {
	if (!"win32".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(1_000_000);
	assertEquals(1_000_000, virtualTree.getItemCount(),
			"logical root count must not be reduced to the native resident prefix");

	Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
	Method requestFrontier = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
	residentCount.setAccessible(true);
	requestFrontier.setAccessible(true);

	assertEquals(256, residentCount.invoke(virtualTree, 0L),
			"one million logical roots must start with one bounded Win32 native frontier");

	TreeItem nearEdge = virtualTree.getItem(250);
	assertSame(nearEdge, virtualTree.getItem(250));
	assertEquals(256, residentCount.invoke(virtualTree, 0L));

	requestFrontier.invoke(virtualTree, nearEdge);
	SwtTestUtil.processEvents();
	assertEquals(512, residentCount.invoke(virtualTree, 0L),
			"near-edge demand must reveal exactly one additional native chunk");
	assertEquals(1_000_000, virtualTree.getItemCount());

	TreeItem distant = virtualTree.getItem(1_023);
	assertSame(distant, virtualTree.getItem(1_023));
	assertEquals(1_024, residentCount.invoke(virtualTree, 0L),
			"explicit indexed access may synchronously reconstruct only through its coordinate");
	assertEquals(1_000_000, virtualTree.getItemCount());
}

@Test
public void test_virtualWin32RootInsertionRestoresOnlyRequiredPrefix() throws Exception {
	if (!"win32".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(10_000);
	Method resident = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
	resident.setAccessible(true);
	assertEquals(256, resident.invoke(virtualTree, 0L));

	TreeItem inserted = new TreeItem(virtualTree, SWT.NONE, 1_000);
	assertEquals(10_001, virtualTree.getItemCount());
	assertEquals(1_000, virtualTree.indexOf(inserted));
	assertSame(inserted, virtualTree.getItem(1_000));
	assertEquals(1_001, resident.invoke(virtualTree, 0L),
			"indexed root insertion should reconstruct only through its predecessor");
}

@Test
public void test_virtualWin32GetItemsExplicitlyMaterializesLogicalRoots() throws Exception {
	if (!"win32".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(1_024);
	Method resident = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
	resident.setAccessible(true);
	assertEquals(256, resident.invoke(virtualTree, 0L));

	TreeItem[] all = virtualTree.getItems();
	assertEquals(1_024, all.length);
	assertEquals(1_024, resident.invoke(virtualTree, 0L),
			"getItems remains the explicit full-materialization API");
}

@Test
public void test_virtualWin32QueuedFrontierHonorsLogicalShrink() throws Exception {
	if (!"win32".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(10_000);
	TreeItem edge = virtualTree.getItem(250);
	Method request = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
	Method resident = Tree.class.getDeclaredMethod("virtualResidentChildCount", long.class);
	request.setAccessible(true);
	resident.setAccessible(true);

	request.invoke(virtualTree, edge);
	virtualTree.setItemCount(10);
	SwtTestUtil.processEvents();

	assertEquals(10, virtualTree.getItemCount());
	assertEquals(10, resident.invoke(virtualTree, 0L),
			"queued frontier work must not restore rows beyond the shrunken logical range");
}

@Test
public void test_virtualGtkFrontierGrowthPreservesExposedPrefixAndDemandIndices() throws Exception {
	if (!"gtk".equals(SWT.getPlatform())) {
		return;
	}
	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL | SWT.CHECK | SWT.MULTI, "false");
	java.util.List<Integer> requested = new java.util.ArrayList<>();
	virtualTree.addListener(SWT.SetData, event -> {
		requested.add(event.index);
		((TreeItem) event.item).setText("row " + event.index);
	});
	virtualTree.setItemCount(20_000);
	TreeItem first = virtualTree.getItem(0);
	TreeItem tail = virtualTree.getItem(255);
	first.setText("first");
	tail.setText("tail");
	tail.setChecked(true);
	tail.setGrayed(true);
	tail.setItemCount(20_000);
	TreeItem child = tail.getItem(0);
	child.setText("child");
	tail.setExpanded(true);
	virtualTree.setSelection(new TreeItem[] {first, tail});
	int[] selectionEvents = {0};
	virtualTree.addListener(SWT.Selection, event -> selectionEvents[0]++);

	TreeItem distant = virtualTree.getItem(10_000);
	assertEquals("row 10000", distant.getText());
	TreeItem distantChild = tail.getItem(10_000);
	assertEquals("row 10000", distantChild.getText());
	assertSame(first, virtualTree.getItem(0));
	assertSame(tail, virtualTree.getItem(255));
	assertSame(child, tail.getItem(0));
	assertSame(distant, virtualTree.getItem(10_000));
	assertSame(distantChild, tail.getItem(10_000));
	assertEquals(10_000, virtualTree.indexOf(distant));
	assertEquals(10_000, tail.indexOf(distantChild));
	assertEquals(20_000, virtualTree.getItemCount());
	assertEquals(20_000, tail.getItemCount());
	assertEquals("first", first.getText());
	assertEquals("tail", tail.getText());
	assertEquals("child", child.getText());
	assertTrue(tail.getChecked());
	assertTrue(tail.getGrayed());
	assertTrue(tail.getExpanded());
	assertArrayEquals(new TreeItem[] {first, tail}, virtualTree.getSelection());
	assertEquals(0, selectionEvents[0]);
	assertEquals(java.util.List.of(10_000, 10_000), requested,
			"cold frontier creation must not request placeholder payloads");
}

@Test
public void test_virtualGtkCollapsedChildStartsAtSentinelAndExpansionStaysBounded() throws Exception {
    if (!"gtk".equals(SWT.getPlatform())) {
        return;
    }

	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "false");
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(10_000);

	Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", TreeItem.class);
	Method requestFrontier = Tree.class.getDeclaredMethod("requestVirtualFrontier", TreeItem.class);
	residentCount.setAccessible(true);
	requestFrontier.setAccessible(true);

	assertEquals(10_000, root.getItemCount());
	assertEquals(1, residentCount.invoke(virtualTree, root),
			"a cold collapsed branch needs one native expander sentinel");

	root.setExpanded(true);
	assertTrue(root.getExpanded());
	assertEquals(256, residentCount.invoke(virtualTree, root),
			"expansion must reveal one bounded native frontier, not all 10K logical children");
	assertEquals(10_000, root.getItemCount());

	TreeItem nearEdge = root.getItem(250);
	requestFrontier.invoke(virtualTree, nearEdge);
	SwtTestUtil.processEvents();
	assertEquals(512, residentCount.invoke(virtualTree, root),
			"render demand near the child frontier grows one batch");
	assertSame(nearEdge, root.getItem(250));
	assertEquals(10_000, root.getItemCount());
}

@Test
public void test_virtualGtkPresentationStateSurvivesDynamicColumns() {
	if (!"gtk".equals(SWT.getPlatform())) return;

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.CHECK);
	new TreeColumn(virtualTree, SWT.NONE);
	new TreeColumn(virtualTree, SWT.NONE);
	virtualTree.setItemCount(2);

	TreeItem item = virtualTree.getItem(0);
	Display display = virtualTree.getDisplay();
	Image image = new Image(display, 4, 4);
	try {
		Color rowBackground = display.getSystemColor(SWT.COLOR_RED);
		Color rowForeground = display.getSystemColor(SWT.COLOR_BLUE);
		Color cellBackground = display.getSystemColor(SWT.COLOR_YELLOW);
		Color cellForeground = display.getSystemColor(SWT.COLOR_DARK_GREEN);

		item.setText(0, "root");
		item.setText(1, "detail");
		item.setImage(1, image);
		item.setBackground(rowBackground);
		item.setForeground(rowForeground);
		item.setBackground(1, cellBackground);
		item.setForeground(1, cellForeground);
		item.setFont(1, virtualTree.getFont());
		item.setChecked(true);
		item.setGrayed(true);

		assertEquals("root", item.getText(0));
		assertEquals("detail", item.getText(1));
		assertSame(image, item.getImage(1));
		assertSame(rowBackground, item.getBackground());
		assertSame(rowForeground, item.getForeground());
		assertSame(cellBackground, item.getBackground(1));
		assertSame(cellForeground, item.getForeground(1));
		assertSame(virtualTree.getFont(), item.getFont(1));
		assertTrue(item.getChecked());
		assertTrue(item.getGrayed());

		TreeColumn inserted = new TreeColumn(virtualTree, SWT.NONE, 1);
		assertEquals("", item.getText(1), "new column must receive empty virtual presentation state");
		assertEquals("detail", item.getText(2), "old column state must shift with its logical column");
		assertSame(image, item.getImage(2));
		assertSame(cellBackground, item.getBackground(2));
		assertSame(cellForeground, item.getForeground(2));

		inserted.dispose();
		assertEquals("detail", item.getText(1));
		assertSame(image, item.getImage(1));
		assertSame(cellBackground, item.getBackground(1));
		assertSame(cellForeground, item.getForeground(1));

		virtualTree.clear(0, false);
		assertEquals("", item.getText(0));
		assertEquals("", item.getText(1));
		assertNull(item.getImage(1));
		assertFalse(item.getChecked());
		assertFalse(item.getGrayed());
		assertEquals(virtualTree.getBackground(), item.getBackground());
		assertEquals(virtualTree.getForeground(), item.getForeground());
	} finally {
		image.dispose();
	}
}

@Test
public void test_virtualGtkAndWin32CollapseCompactsNativeTailAndRestoresOnExpand() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "false");
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(1_000);

	Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", TreeItem.class);
	residentCount.setAccessible(true);

	assertEquals(1_000, root.getItemCount());
	assertEquals(1, residentCount.invoke(virtualTree, root),
			platform + " cold child branches keep one native expander sentinel");

	TreeItem pinned = root.getItem(10);
	pinned.setText("pinned");
	assertSame(pinned, root.getItem(10));

	root.setExpanded(true);
	assertTrue(root.getExpanded());
	assertEquals(256, residentCount.invoke(virtualTree, root),
			platform + " expansion restores one frontier chunk rather than the full logical branch");

	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertFalse(root.getExpanded());
	assertEquals(1_000, root.getItemCount(),
			"collapse must preserve the logical child count");
	assertEquals(11, residentCount.invoke(virtualTree, root),
			"collapsed residency must retain only the prefix through the highest pinned subtree");
	assertSame(pinned, root.getItem(10),
			"an exposed TreeItem facade must survive collapsed-tail compaction");
	assertEquals("pinned", pinned.getText());

	root.setExpanded(true);
	assertTrue(root.getExpanded());
	assertEquals(256, residentCount.invoke(virtualTree, root),
			platform + " re-expansion restores only the bounded frontier");
	assertSame(pinned, root.getItem(10));

	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertEquals(11, residentCount.invoke(virtualTree, root),
			"repeated collapse must converge to the same bounded residency");
}

@Test
public void test_virtualGtkAndWin32CollapseKeepsOneSentinelWhenNoChildFacadeEscapes() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = newVirtualTreeWithGtkLogicalMode(SWT.VIRTUAL, "false");
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(2_000);

	Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", TreeItem.class);
	residentCount.setAccessible(true);

	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertEquals(2_000, root.getItemCount());
	assertEquals(1, residentCount.invoke(virtualTree, root),
			"a collapsed branch with no exposed child facade needs only one native sentinel row");

	TreeItem last = root.getItem(1_999);
	assertSame(last, root.getItem(1_999));
	assertEquals(2_000, residentCount.invoke(virtualTree, root),
			"explicit indexed access may reconstruct the requested native coordinate while collapsed");
	assertEquals(2_000, root.getItemCount());
}


@Test
public void test_virtualWin32IndexedInsertRestoresOnlyRequiredCollapsedPrefix() throws Exception {
    if (!"win32".equals(SWT.getPlatform())) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL);
	virtualTree.setItemCount(1);
	TreeItem root = virtualTree.getItem(0);
	root.setItemCount(2_000);

	Method residentCount = Tree.class.getDeclaredMethod("virtualResidentChildCount", TreeItem.class);
	residentCount.setAccessible(true);

	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertEquals(1, residentCount.invoke(virtualTree, root));

	TreeItem inserted = new TreeItem(root, SWT.NONE, 100);
	assertEquals(2_001, root.getItemCount());
	assertEquals(100, root.indexOf(inserted));
	assertSame(inserted, root.getItem(100));
	assertEquals(101, residentCount.invoke(virtualTree, root),
			"indexed insertion should restore only the prefix required to identify the logical insertion point");

	root.setExpanded(false);
	SwtTestUtil.processEvents();
	assertEquals(101, residentCount.invoke(virtualTree, root),
			"explicitly constructed facade is pinned and must remain resident after collapse");
}

@Test
public void test_virtualGtkAndWin32PackedStateLivesInTopologyAndSurvivesCoordinateShift() throws Exception {
	String platform = SWT.getPlatform();
    if (!("gtk".equals(platform) || "win32".equals(platform))) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.CHECK);
	virtualTree.setItemCount(32);
	TreeItem marked = virtualTree.getItem(20);
	marked.setItemCount(1);
	marked.setText("marked");
	marked.setChecked(true);
	marked.setGrayed(true);
	marked.setExpanded(true);

	Field treeItemsField = Tree.class.getDeclaredField("items");
	treeItemsField.setAccessible(true);
	TreeItem[] items = (TreeItem[]) treeItemsField.get(virtualTree);
	int markedId = -1;
	for (int id = 0; id < items.length; id++) {
		if (items[id] == marked) {
			markedId = id;
			break;
		}
	}
	assertTrue(markedId >= 0);

	Field topologyField = Tree.class.getDeclaredField("virtualTopology");
	topologyField.setAccessible(true);
	Object topology = topologyField.get(virtualTree);
	Field stateMasksField = topology.getClass().getDeclaredField("stateMasks");
	Field childIndicesField = topology.getClass().getDeclaredField("childIndices");
	stateMasksField.setAccessible(true);
	childIndicesField.setAccessible(true);

	Class<?> stateType = Class.forName("org.eclipse.swt.widgets.VirtualItemState");
	long expected = 0;
	for (String name : new String[] {"CACHED", "CHECKED", "GRAYED", "EXPANDED", "PINNED"}) {
		Field field = stateType.getDeclaredField(name);
		field.setAccessible(true);
		expected |= field.getLong(null);
	}

	long[] masks = (long[]) stateMasksField.get(topology);
	int[] childIndices = (int[]) childIndicesField.get(topology);
	assertEquals(expected, masks[markedId] & expected,
			"virtual semantic state must be authoritative in the packed topology lane");
	assertEquals(20, childIndices[markedId]);
	assertTrue(marked.getChecked());
	assertTrue(marked.getGrayed());
	assertTrue(marked.getExpanded());

	TreeItem inserted = new TreeItem(virtualTree, SWT.NONE, 3);
	masks = (long[]) stateMasksField.get(topology);
	childIndices = (int[]) childIndicesField.get(topology);
	assertEquals(21, childIndices[markedId]);
	assertEquals(expected, masks[markedId] & expected,
			"packed state must move with the same exposed TreeItem coordinate");
	assertSame(marked, virtualTree.getItem(21));

	inserted.dispose();
	masks = (long[]) stateMasksField.get(topology);
	childIndices = (int[]) childIndicesField.get(topology);
	assertEquals(20, childIndices[markedId]);
	assertEquals(expected, masks[markedId] & expected);
	assertSame(marked, virtualTree.getItem(20));

	virtualTree.clear(virtualTree.indexOf(marked), false);
	masks = (long[]) stateMasksField.get(topology);
	Field expandedField = stateType.getDeclaredField("EXPANDED");
	expandedField.setAccessible(true);
	long expanded = expandedField.getLong(null);
	assertEquals(expanded, masks[markedId] & expanded,
			"clear must preserve expansion while clearing virtual item data state");
	assertFalse(marked.getChecked());
	assertFalse(marked.getGrayed());
	assertTrue(marked.getExpanded());
}

@Test
public void test_virtualPackedStateFollowsLogicalInsertAndRemoveOnCocoa() throws Exception {
    if (!"cocoa".equals(SWT.getPlatform())) {
        return;
    }

	Tree virtualTree = new Tree(shell, SWT.VIRTUAL | SWT.CHECK);
	virtualTree.setItemCount(32);
	TreeItem marked = virtualTree.getItem(20);
	marked.setChecked(true);
	marked.setGrayed(true);

	Field storageField = Tree.class.getDeclaredField("virtualItems");
	storageField.setAccessible(true);
	Object storage = storageField.get(virtualTree);
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
	assertTrue(markedSlot >= 0, "materialized logical row must exist in sparse storage");
	long markedState = before[markedSlot];
	assertTrue(markedState != 0, "materialized virtual state must be represented by a packed mask");
	assertTrue(before.length <= 16, "packed state capacity must follow materialized residency, not logical count");

	TreeItem inserted = new TreeItem(virtualTree, SWT.NONE, 3);
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
	assertTrue(shiftedSlot >= 0, "shifted logical row must remain materialized");
	assertEquals(markedState, afterInsert[shiftedSlot], "packed state must move with the shifted logical item");
	assertSame(marked, virtualTree.getItem(21));
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
	assertTrue(restoredSlot >= 0, "restored logical row must remain materialized");
	assertEquals(markedState, afterRemove[restoredSlot], "packed state must shift back with logical removal");
	assertSame(marked, virtualTree.getItem(20));
	assertTrue(marked.getChecked());
	assertTrue(marked.getGrayed());
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	assertThrows(IllegalArgumentException.class, () -> new Tree(null, 0), "No exception thrown for parent == null");

	int[] cases = {0, SWT.BORDER};
    for (int style : cases) {
        tree = new Tree(shell, style);
    }

	cases = new int[]{0, 10, 100};
	for (int count : cases) {
		for (int i = 0; i < count; i++) {
			new TreeItem(tree, 0);
		}
		assertEquals(count, tree.getItemCount());
		tree.removeAll();
	}
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Test
public void test_deselectAll() {
	int number = 15;
	TreeItem[] items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertEquals(0, tree.getSelectionCount());
	tree.setSelection(new TreeItem[] {items[2], items[4], items[5], items[10]});

	assertEquals(4, tree.getSelectionCount());

	tree.deselectAll();
	assertEquals(0, tree.getSelectionCount());

	tree.selectAll();
	assertEquals(number, tree.getSelectionCount());

	tree.deselectAll();
	assertEquals(0, tree.getSelectionCount());
}

@Test
public void test_getColumnCount() {
	assertEquals(0, tree.getColumnCount());
	TreeColumn column0 = new TreeColumn(tree, SWT.NONE);
	assertEquals(1, tree.getColumnCount());
	TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
	assertEquals(2, tree.getColumnCount());
	TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
	assertEquals(3, tree.getColumnCount());
	column0.dispose();
	assertEquals(2, tree.getColumnCount());
	column1.dispose();
	assertEquals(1, tree.getColumnCount());
	column2.dispose();
	assertEquals(0, tree.getColumnCount());
}

@Test
public void test_getColumnI() {
	assertThrows(IllegalArgumentException.class, () -> tree.getColumn(0), "No exception thrown for index out of range");
	TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
	assertThrows(IllegalArgumentException.class, () -> tree.getColumn(1), "No exception thrown for index out of range");
	assertEquals(column0, tree.getColumn(0));
	TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
	assertEquals(column1, tree.getColumn(1));
	column1.dispose();
	assertThrows(IllegalArgumentException.class, () -> tree.getColumn(1), "No exception thrown for index out of range");
	column0.dispose();
	assertThrows(IllegalArgumentException.class, () -> tree.getColumn(0), "No exception thrown for index out of range");
}

@Test
public void test_getColumns() {
	assertEquals(0, tree.getColumns().length);
	TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
	TreeColumn[] columns = tree.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column0, columns[0]);
	column0.dispose();
	assertEquals(0, tree.getColumns().length);
	column0 = new TreeColumn(tree, SWT.LEFT);
	TreeColumn column1 = new TreeColumn(tree, SWT.RIGHT, 1);
	columns = tree.getColumns();
	assertEquals(2, columns.length);
	assertEquals(column0, columns[0]);
	assertEquals(column1, columns[1]);
	column0.dispose();
	columns = tree.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column1, columns[0]);
	column1.dispose();
	assertEquals(0, tree.getColumns().length);
}

@Test
public void test_getGridLineWidth() {
	tree.getGridLineWidth();
}

@Test
public void test_getHeaderHeight() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getHeaderHeight(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Tree)");
		}
		return;
	}
	assertEquals(0, tree.getHeaderHeight());
	tree.setHeaderVisible(true);
	assertTrue(tree.getHeaderHeight() > 0);
	tree.setHeaderVisible(false);
	assertEquals(0, tree.getHeaderHeight());
}

@Test
public void test_getItemHeight() {
	assertTrue(tree.getItemHeight() > 0);
	new TreeItem(tree, 0);
	assertTrue(tree.getItemHeight() > 0);
}

@Test
public void test_getItemI() {
	int number = 15;
	TreeItem[] items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

    for (int i = 0; i < number; i++) {
        assertEquals(items[i], tree.getItem(i));
    }
	assertThrows(IllegalArgumentException.class, () -> tree.getItem(number), "No exception thrown for illegal index argument");

	assertThrows(IllegalArgumentException.class, () -> tree.getItem(number+1), "No exception thrown for illegal index argument");

	assertThrows(IllegalArgumentException.class, () -> tree.getItem(-1), "No exception thrown for illegal index argument");
}

@Test
public void test_getItems() {
	int[] cases = {0, 10, 100};
	TreeItem [][] items = new TreeItem [cases.length][];
	for (int j = 0; j < cases.length; j++) {
		items [j] = new TreeItem [cases [j]];
	}
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TreeItem ti = new TreeItem(tree, 0);
			items [j][i] = ti;
		}
		assertArrayEquals(items[j], tree.getItems());
		tree.removeAll();
		assertEquals(0, tree.getItemCount());
	}

	makeCleanEnvironment(false);

	for (int count : cases) {
		for (int i = 0; i < count; i++) {
			TreeItem ti = new TreeItem(tree, 0);
			ti.setText(String.valueOf(i));
		}
		TreeItem[] items2 = tree.getItems();
		for (int i = 0; i < items2.length; i++) {
			assertEquals(String.valueOf(i), items2[i].getText());
		}
		tree.removeAll();
		assertEquals(0, tree.getItemCount());
	}
}

@Test
public void test_getParentItem() {
	assertNull(tree.getParentItem());
}

@Test
public void test_getSelectionCount() {
	int number = 15;
	TreeItem[] items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[2]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[number-1]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[10]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[2], items[number-1], items[10]});
	assertEquals(3, tree.getSelectionCount());

	tree.setSelection(items);
	assertEquals(15, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{});
	assertEquals(0, tree.getSelectionCount());


	makeCleanEnvironment(true); // use single-selection tree.

	items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[2]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[number-1]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[10]});
	assertEquals(1, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[2], items[number-1], items[10]});
	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(items);
	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{});
	assertEquals(0, tree.getSelectionCount());
}

@Test
public void test_removeAll() {
	tree.removeAll();
	assertEquals(0, tree.getItemCount());

	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}
	assertEquals(number, tree.getItemCount());

	tree.removeAll();
	assertEquals(0, tree.getItemCount());
}

@Test
public void test_selectAll() {
	int number = 5;
	TreeItem[] items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertEquals(0, tree.getSelectionCount());
	tree.selectAll();
	assertEquals(number, tree.getSelectionCount());

	makeCleanEnvironment(true); // single-selection tree

	items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertEquals(0, tree.getSelectionCount());
	tree.selectAll();
	assertEquals(0, tree.getSelectionCount());
}

@Test
public void test_setHeaderBackgroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(tree.getHeaderBackground());
	Color color = new Color(12, 34, 56);
	tree.setHeaderBackground(color);
	assertEquals(color, tree.getHeaderBackground());
	tree.setHeaderBackground(null);
	assertFalse(tree.getHeaderBackground().equals(color));
}

@Test
public void test_setHeaderForegroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(tree.getHeaderForeground());
	Color color = new Color(12, 34, 56);
	tree.setHeaderForeground(color);
	assertEquals(color, tree.getHeaderForeground());
	tree.setHeaderForeground(null);
	assertFalse(tree.getHeaderForeground().equals(color));
}

@Test
public void test_setHeaderVisibleZ() {
	assertFalse(tree.getHeaderVisible());
	tree.setHeaderVisible(true);
	assertTrue(tree.getHeaderVisible());
	tree.setHeaderVisible(false);
	assertFalse(tree.getHeaderVisible());
}

@Test
public void test_setItemCountI() {
	tree.removeAll();
	assertEquals(0, tree.getItemCount());
	for (int i=0; i<8; i++) {
		new TreeItem(tree, SWT.NULL);
		assertEquals(i+1, tree.getItemCount());
	}
	assertEquals(8, tree.getItemCount());
	assertEquals(4, tree.indexOf(tree.getItems()[4]));
	tree.getItem(1).dispose();
	assertEquals(7, tree.getItemCount());
	new TreeItem (tree, SWT.NULL, 0);
	assertEquals(1, tree.indexOf(tree.getItems()[1]));
	assertEquals(8, tree.getItemCount());
	tree.setItemCount(0);
	assertEquals(0, tree.getItemCount());
	tree.setItemCount(0);
	assertEquals(0, tree.getItemCount());
	tree.setItemCount(-1);
	assertEquals(0, tree.getItemCount());
	tree.setItemCount(10);
	assertEquals(10, tree.getItemCount());
	tree.getItem(1).dispose();
	assertEquals(9, tree.getItemCount());
	assertEquals(4, tree.indexOf(tree.getItems()[4]));
	tree.setItemCount(3);
	assertEquals(3, tree.getItemCount());
	assertThrows(IllegalArgumentException.class, () -> tree.getItem(4), "No exception thrown for illegal index argument");
	tree.setItemCount(40);
	assertEquals(40, tree.getItemCount());
	tree.getItem(39);
	tree.setItemCount(0);
	assertEquals(0, tree.getItemCount());
	assertThrows(IllegalArgumentException.class, () -> tree.getItem(39), "No exception thrown for illegal index argument");
}

@Test
public void test_setLinesVisibleZ() {
	assertFalse(tree.getLinesVisible());
	tree.setLinesVisible(true);
	assertTrue(tree.getLinesVisible());
	tree.setLinesVisible(false);
	assertFalse(tree.getLinesVisible());
}

@Override
@Test
public void test_setRedrawZ() {
}

@Test
public void test_setSelection$Lorg_eclipse_swt_widgets_TreeItem() {
	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}

	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[5], items[16], items[19]});
	assertArrayEquals(new TreeItem[] {items[5], items[16], items[19]}, tree.getSelection());

	tree.setSelection(items);
	assertArrayEquals(items, tree.getSelection());

	tree.setSelection(tree.getItems());
	assertArrayEquals(tree.getItems(), tree.getSelection());

	tree.setSelection(new TreeItem[] {});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());
	assertEquals(0, tree.getSelectionCount());

	assertThrows(IllegalArgumentException.class, () -> tree.setSelection((TreeItem[]) null), "No exception thrown for items == null");

	tree.setSelection(new TreeItem[]{null});
	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[10]});
	assertArrayEquals(new TreeItem[] {items[10]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[number-1]});
	assertArrayEquals(new TreeItem[] {items[number-1]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[2]});
	assertArrayEquals(new TreeItem[] {items[2]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[10], items[number-1], items[2]});
	assertArrayEquals(new TreeItem[] {items[2], items[10], items[number - 1]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TreeItem[]{items[0], items[2], items[3]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TreeItem[]{items[1], items[2], items[3]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TreeItem[]{items[0], items[1], items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TreeItem[]{items[0], items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TreeItem[]{items[2], items[3], items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TreeItem[]{items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[0]});
	assertArrayEquals(new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertArrayEquals(new TreeItem[] {items[3]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[4]});
	assertArrayEquals(new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertArrayEquals(new TreeItem[] {items[2]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[1]});
	assertArrayEquals(new TreeItem[] {items[1]}, tree.getSelection());

	tree.removeAll();
	tree.setSelection(new TreeItem[] {});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());


	makeCleanEnvironment(true); // single-selection tree

	items = new TreeItem[number];
    for (int i = 0; i < number; i++) {
        items[i] = new TreeItem(tree, 0);
    }

	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[5], items[16], items[19]});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(items);
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(tree.getItems());
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[] {});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());
	assertEquals(0, tree.getSelectionCount());

	assertThrows(IllegalArgumentException.class, () -> tree.setSelection((TreeItem[]) null), "No exception thrown for items == null");

	tree.setSelection(new TreeItem[]{items[10]});
	assertArrayEquals(new TreeItem[] {items[10]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[number-1]});
	assertArrayEquals(new TreeItem[] {items[number-1]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[2]});
	assertArrayEquals(new TreeItem[] {items[2]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[10], items[number-1], items[2]});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TreeItem[]{}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[0]});
	assertArrayEquals(new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertArrayEquals(new TreeItem[] {items[3]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[4]});
	assertArrayEquals(new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertArrayEquals(new TreeItem[] {items[2]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[1]});
	assertArrayEquals(new TreeItem[] {items[1]}, tree.getSelection());

	tree.removeAll();
	tree.setSelection(new TreeItem[] {});
	assertArrayEquals(new TreeItem[] {}, tree.getSelection());
}

@Test
public void test_setSelection$Lorg_eclipse_swt_widgets_TreeItem_nested() {
	TreeItem[][][] items = new TreeItem[3][5][4];
	for (int r = 0; r < 3; r++) {
		TreeItem root = new TreeItem(tree, 0);
		for (int c = 0; c < 5; c++) {
			TreeItem child = new TreeItem(root, 0);
			items[r][c][0] = child;
			for (int g = 1; g < 4; g++) {
				items[r][c][g] = new TreeItem(child, 0);
			}
		}
	}
	TreeItem[] roots = tree.getItems();

	if (SwtTestUtil.isGTK) {
		tree.setSelection(new TreeItem[] {items[2][1][3], items[0][4][1]});
		assertTrue(items[2][1][0].getExpanded());
		assertTrue(items[0][4][0].getExpanded());
		assertArrayEquals(new TreeItem[] {items[0][4][1], items[2][1][3]}, tree.getSelection());
	}
	// Other platforms only reveal the first item, so expand all parents
	for (TreeItem root : roots) {
		root.setExpanded(true);
        for (TreeItem child : root.getItems()) {
            child.setExpanded(true);
        }
	}

	TreeItem[] selection = {items[2][1][3], roots[0], items[0][4][1], items[0][4][2], items[0][1][0]};
	tree.setSelection(selection);
	assertArrayEquals(new TreeItem[] {roots[0], items[0][1][0], items[0][4][1], items[0][4][2], items[2][1][3]}, tree.getSelection());

	tree.setSelection(tree.getSelection());
	assertArrayEquals(new TreeItem[] {roots[0], items[0][1][0], items[0][4][1], items[0][4][2], items[2][1][3]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[0][4][2], items[1][3][0], items[2][1][3], items[2][1][1]});
	assertArrayEquals(new TreeItem[] {items[0][4][2], items[1][3][0], items[2][1][1], items[2][1][3]}, tree.getSelection());

	assertEquals(items[2][1][0], items[2][1][3].getParentItem());
	assertEquals(roots[1], items[1][3][0].getParentItem());
	assertNull(roots[2].getParentItem());
}

@Test
public void test_setTopItemLorg_eclipse_swt_widgets_TreeItem() {
	tree.removeAll();
	for (int i = 0; i < 10; i++) {
		new TreeItem(tree, 0);
	}
	TreeItem top = new TreeItem(tree, 0);
	for (int i = 0; i < 10; i++) {
		new TreeItem(tree, 0);
	}
	tree.setSize(50,50);
	shell.open();
	tree.setTopItem(top);
	for (int i = 0; i < 10; i++) {
		new TreeItem(tree, 0);
	}
	TreeItem top2 = tree.getTopItem();
	shell.setVisible(false);
	assertEquals(top, top2);
	shell.setVisible(true);
	try {
		assertThrows(IllegalArgumentException.class, () -> tree.setTopItem(null), "No exception thrown for item == null");
	} finally {
		shell.setVisible (false);
	}
}

@Test
public void test_showItemLorg_eclipse_swt_widgets_TreeItem() {
	assertThrows(IllegalArgumentException.class, () -> tree.showItem(null), "No exception thrown for item == null");

	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}
    for (int i = 0; i < number; i++) {
        tree.showItem(items[i]);
    }

	tree.removeAll();

	makeCleanEnvironment(false);
	//showing somebody else's items

	items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}

	Tree tree2 = new Tree(shell, 0);
	TreeItem[] items2 = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items2[i] = new TreeItem(tree2, 0);
	}

    for (int i = 0; i < number; i++) {
        tree.showItem(items2[i]);
    }

	tree.removeAll();
}

@Test
public void test_addTreeListenerTreeCollapsedAdapterLorg_eclipse_swt_events_TreeListener() {
	TreeListener listener = TreeListener.treeCollapsedAdapter(e -> eventOccurred = true);
	tree.addTreeListener(listener);
	eventOccurred = false;

	tree.notifyListeners(SWT.Collapse, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	tree.notifyListeners(SWT.Expand, new Event());
	assertFalse(eventOccurred);

	tree.removeTreeListener(listener);
	eventOccurred = false;

	tree.notifyListeners(SWT.Collapse, new Event());
	assertFalse(eventOccurred);

	tree.notifyListeners(SWT.Expand, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_addTreeListenerTreeExpandedAdapterLorg_eclipse_swt_events_TreeListener() {
	TreeListener listener = TreeListener.treeExpandedAdapter(e -> eventOccurred = true);
	tree.addTreeListener(listener);
	eventOccurred = false;

	tree.notifyListeners(SWT.Expand, new Event());
	assertTrue(eventOccurred);

	eventOccurred = false;

	tree.notifyListeners(SWT.Collapse, new Event());
	assertFalse(eventOccurred);

	tree.removeTreeListener(listener);
	eventOccurred = false;

	tree.notifyListeners(SWT.Expand, new Event());
	assertFalse(eventOccurred);

	tree.notifyListeners(SWT.Collapse, new Event());
	assertFalse(eventOccurred);
}

@Test
public void test_showSelection() {
	TreeItem item;

	tree.showSelection();
	item = new TreeItem(tree, 0);
	tree.setSelection(new TreeItem[]{item});
	tree.showSelection();
}

/**
 * Clean up the environment for a new test.
 *
 * @param single true if the new tree should be a single-selection one,
 * otherwise use multi-selection.
 */
private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
    if (tree != null) {
        tree.dispose();
    }
	tree = new Tree(shell, single?SWT.SINGLE:SWT.MULTI);
	setWidget(tree);
}

private void createTree(List<String> events) {
	makeCleanEnvironment(true);
	for (int i = 0; i < 3; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("TreeItem" + i);
		for (int j = 0; j < 4; j++) {
			TreeItem ti = new TreeItem(item, SWT.NONE);
			ti.setText("TreeItem" + i + j);
			hookExpectedEvents(ti, getTestName(), events);
		}
		hookExpectedEvents(item, getTestName(), events);
	}
}

@Test
public void test_consistency_KeySelection() {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MouseSelection() {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(30, 30, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_MouseExpand() {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(11, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_KeyExpand() {
	List<String> events = new ArrayList<>();
	createTree(events);
	int code=SWT.ARROW_RIGHT;
    if (SwtTestUtil.isGTK) {
        code = SWT.KEYPAD_ADD;
    }
	consistencyEvent(0, code, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_DoubleClick () {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyPrePackShell();
	consistencyEvent(20, tree.getItemHeight()*2, 1, 0,
					 ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

@Test
public void test_consistency_EnterSelection () {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_SpaceSelection () {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MenuDetect () {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(50, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DragDetect () {
	List<String> events = new ArrayList<>();
	createTree(events);
	consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_disposeItemNotTriggerSelection() {
	Display display = shell.getDisplay();
	shell.setLayout(new FillLayout());
	Tree tree = new Tree (shell, SWT.BORDER);
	for (int i=0; i<4; i++) {
		TreeItem iItem = new TreeItem (tree, 0);
		iItem.setText ("TreeItem (0) -" + i);
		for (int j=0; j<4; j++) {
			TreeItem jItem = new TreeItem (iItem, 0);
			jItem.setText ("TreeItem (1) -" + j);
			for (int k=0; k<4; k++) {
				TreeItem kItem = new TreeItem (jItem, 0);
				kItem.setText ("TreeItem (2) -" + k);
				for (int l=0; l<4; l++) {
					TreeItem lItem = new TreeItem(kItem, 0);
					lItem.setText ("TreeItem (3) -" + l);
				}
			}
		}
	}
	final boolean [] selectionCalled = { false };
	tree.addListener(SWT.Selection, event -> {
		selectionCalled [0] = true;
	});

	final TreeItem firstNode = tree.getItem(0);
	firstNode.setExpanded(true);
	tree.setSelection(firstNode.getItem(3));

	shell.setSize(200, 200);
	shell.open();

	display.timerExec(1000, () -> {
		if (shell.isDisposed()) {
			return;
		}

		final TreeItem[] selection = tree.getSelection();
		if (selection.length != 1) {
			return;
		}

		final TreeItem item = selection[0];
		final TreeItem parentItem = item.getParentItem();
		if (parentItem == null) {
			return;
		}

		tree.deselectAll();
		item.dispose();

	});

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

	assertFalse(selectionCalled[0]);
}

@Test
public void test_Virtual() {
	tree.dispose();
	tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);
	setWidget(tree);

	int count = 10000;
	int visibleCount = 10;

	shell.setLayout(new FillLayout());
	final TreeItem[] top = { null };
	final int[] dataCounter = { 0 };
	tree.addListener(SWT.SetData, event -> {
		TreeItem item = (TreeItem) event.item;
		if (item.getParentItem() == null) {
			top[0] = item;
			item.setText("top");
		} else {
			if (top[0] == null) {
				top[0] = tree.getItem(0);
			}
			if (top[0] != null) {
				int index = top[0].indexOf(item);
				item.setText("Item " + index);
			}
		}
		dataCounter[0]++;
	});

	tree.setItemCount(1);
	shell.setSize (200, tree.getItemHeight() * visibleCount);
	shell.open ();
	TreeItem item0 = tree.getItem(0);
	item0.setItemCount(count);
	item0.setExpanded(true);

	long end = System.currentTimeMillis() + 3000;
	Display display = shell.getDisplay();
	while (!display.isDisposed() && System.currentTimeMillis() < end) {
		if (!shell.getDisplay().readAndDispatch ()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// temp code to capture screenshot
	if (SwtTestUtil.isCocoa) {
		// check if setData is called for root item
		assertTrue(top[0] != null);
	}


	// the "* 3" allows some surplus for platforms that pre-fetch items to improve scrolling performance:
	assertTrue(dataCounter[0] > visibleCount / 2 && dataCounter[0] <= visibleCount * 3);
}

/** Ensure setText() and setImage() can be set from SetData handler.
@see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/678">Issue 678</a>
**/
@Test
public void test_setData() {
	tree.dispose();
	disposedIntentionally = true;
	Image image = new Image(Display.getCurrent(), 20, 20);
	try {
		shell.setSize(200, 200);
		shell.setLayout(new FillLayout());
		shell.open();
		waitUntilIdle();
		for (int i = 0; i < 200; i++) {
			Tree tree = new Tree(shell, SWT.VIRTUAL);
			tree.addListener(SWT.SetData, e -> {
				TreeItem item = (TreeItem) e.item;
				item.setText(0, "A");
				item.setImage(image); // <-- this is the critical line!
			});
			waitUntilIdle(); // slightly increase crash probability by preventing unrelated background processing on the next line
			tree.setItemCount(1);

			waitUntilIdle(); // may crash while processing asynchronous events

			assertEquals("A", tree.getItem(0).getText(0));
			assertEquals(image, tree.getItem(0).getImage());
			tree.dispose();
		}
	} finally {
		image.dispose();
	}
}

private void waitUntilIdle() {
	long lastActive = currentTimeMillis();
	while (true) {
		if (Thread.interrupted()) {
			throw new AssertionError();
		}
		if (Display.getCurrent().readAndDispatch()) {
			lastActive = currentTimeMillis();
		} else {
			if (lastActive + 10 < currentTimeMillis()) {
				return;
			}
			Thread.yield();
		}
	}
}

@Test
public void test_emptinessChanged() {
	int NOT_EMPTY = 0;
	int EMPTY = 1;
	int[] count = { 0, 0 };
	tree.addListener(SWT.EmptinessChanged, e -> ++count[e.detail] );

	// Create first item. Expected one NOT_EMPTY event.
	TreeItem item1 = new TreeItem(tree, SWT.NONE);
	assertEquals(1, count[NOT_EMPTY]);
	assertEquals(0, count[EMPTY]);

	// Create second item. Expected no further event.
	TreeItem item2 = new TreeItem(tree, SWT.NONE);
	assertEquals(1, count[NOT_EMPTY]);
	assertEquals(0, count[EMPTY]);

	// Remove one item. Expected no further event.
	item1.dispose();
	assertEquals(1, count[NOT_EMPTY]);
	assertEquals(0, count[EMPTY]);

	// Remove last item. Expected one EMPTY event.
	item2.dispose();
	assertEquals(1, count[NOT_EMPTY]);
	assertEquals(1, count[EMPTY]);

	// Create first item. Expected one more NOT_EMPTY event.
	item1 = new TreeItem(tree, SWT.NONE);
	assertEquals(2, count[NOT_EMPTY]);
	assertEquals(1, count[EMPTY]);

	// Create second item as child of the first item. Expected no further event.
	item2 = new TreeItem(item1, SWT.NONE);
	assertEquals(2, count[NOT_EMPTY]);
	assertEquals(1, count[EMPTY]);

	// Remove both items. Expected one more EMPTY event.
	item1.dispose();
	assertEquals(2, count[NOT_EMPTY]);
	assertEquals(2, count[EMPTY]);

	// Noop. Expected no further event.
	item2.dispose();
	assertEquals(2, count[NOT_EMPTY]);
	assertEquals(2, count[EMPTY]);
}

private void testTreeRegularAndVirtual(Runnable runnable) {
	testTreeRegularAndVirtual(SWT.NONE, runnable);
}

private void testTreeRegularAndVirtual(int style, Runnable runnable) {
	if (style != SWT.NONE) {
		tree.dispose();
		tree = new Tree(shell, style);
		setWidget(tree);
	}
	runnable.run();

	tree.dispose();
	tree = new Tree(shell, SWT.VIRTUAL | style);
	setWidget(tree);

	runnable.run();
}

@Test
public void test_setItemCount_itemOrderRoot() {
	testTreeRegularAndVirtual(() -> {
		// Test root items
		{
			Tree parent = tree;

			// Create items
			{
				new TreeItem (parent, 0).setText ("0");
				new TreeItem (parent, 0).setText ("2");
				new TreeItem (parent, 0, 1).setText ("1");

				parent.setItemCount (6);

				parent.getItem (3).setText ("3");
				parent.getItem (4).setText ("4");
				parent.getItem (5).setText ("5");

				new TreeItem (parent, 0).setText ("6");
				new TreeItem (parent, 0).setText ("8");
				new TreeItem (parent, 0, 7).setText ("7");

				parent.setItemCount (12);

				parent.getItem (9).setText ("9");
				parent.getItem (10).setText ("10");
				parent.getItem (11).setText ("11");
			}

			// Test items
			TreeItem[] items = parent.getItems();
			assertEquals(12, items.length);
			for (int iItem = 0; iItem < items.length; iItem++) {
				assertEquals(Integer.toString(iItem), items[iItem].getText());
			}
		}

		// Test child items
		{
			TreeItem parent = tree.getItem(0);

			// Create items
			{
				new TreeItem(parent, 0).setText("0");
				new TreeItem(parent, 0).setText("2");
				new TreeItem(parent, 0, 1).setText("1");

				parent.setItemCount(6);

				parent.getItem(3).setText("3");
				parent.getItem(4).setText("4");
				parent.getItem(5).setText("5");

				new TreeItem(parent, 0).setText("6");
				new TreeItem(parent, 0).setText("8");
				new TreeItem(parent, 0, 7).setText("7");

				parent.setItemCount(12);

				parent.getItem(9).setText("9");
				parent.getItem(10).setText("10");
				parent.getItem(11).setText("11");
			}

			// Test items
			TreeItem[] items = parent.getItems();
			assertEquals(12, items.length);
			for (int iItem = 0; iItem < items.length; iItem++) {
				assertEquals(Integer.toString(iItem), items[iItem].getText());
			}
		}
	});
}

@Test
public void test_setItemCount_indexOf() {
	testTreeRegularAndVirtual(() -> {
		tree.setItemCount(10);
		TreeItem item_0 = tree.getItem(0);
		TreeItem item_2 = tree.getItem(2);

		item_0.setItemCount(10);
		item_0.setExpanded(true);
		TreeItem item_0_4 = item_0.getItem(4);

		// Issue 287
		{
			// Bug requires Tree to be visible
			if (!shell.getVisible()) {
				shell.setLayout(new FillLayout());
				shell.pack();
				shell.open();
			} else {
				tree.pack();
			}
			SwtTestUtil.processEvents();

			tree.setItemCount(5);
			tree.setItemCount(10);

			// Bug requires 'TVN_GETDISPINFO' to be processed
			SwtTestUtil.processEvents();

			assertEquals(2, tree.indexOf(item_2));
		}

		// Issue 333
		{
			item_0.setItemCount(5);   // This causes cached index to be reset
			item_0.indexOf(item_0_4); // This causes index to be cached again
			item_0.setItemCount(10);  // This causes index to be recalculated
			assertEquals(4, item_0.indexOf(item_0_4));
		}
	});
}

@Test
public void test_setItemCount_itemCount() {
	testTreeRegularAndVirtual(() -> {
		// Test root items
		tree.setItemCount(10);
		assertEquals(10, tree.getItemCount());
		tree.setItemCount(20);
		assertEquals(20, tree.getItemCount());
		tree.setItemCount(5);
		assertEquals(5, tree.getItemCount());

		// Test child items
		TreeItem item_0 = tree.getItem(0);
		item_0.setItemCount(10);
		assertEquals(10, item_0.getItemCount());
		item_0.setItemCount(20);
		assertEquals(20, item_0.getItemCount());
		item_0.setItemCount(5);
		assertEquals(5, item_0.getItemCount());
	});
}

@Test
public void test_setItemCount_itemCount2() {
	testTreeRegularAndVirtual(() -> {
		// Test root items
		tree.setItemCount(10);
		tree.getItem(5).dispose();
		new TreeItem(tree, 0, 0);
		assertEquals(10, tree.getItemCount());

		// Test child items
		TreeItem item_0 = tree.getItem(0);
		item_0.setItemCount(10);
		item_0.getItem(5).dispose();
		new TreeItem(item_0, 0, 0);
		assertEquals(10, item_0.getItemCount());
	});
}

@Test
public void test_bulkExpansionModelApi() {
	testTreeRegularAndVirtual(SWT.MULTI, () -> {
		TreeItem root0 = new TreeItem(tree, SWT.NONE);
		TreeItem child00 = new TreeItem(root0, SWT.NONE);
		TreeItem grand000 = new TreeItem(child00, SWT.NONE);
		TreeItem root1 = new TreeItem(tree, SWT.NONE);
		TreeItem child10 = new TreeItem(root1, SWT.NONE);
		new TreeItem(child10, SWT.NONE);

		tree.expandAll();
		assertTrue(root0.getExpanded());
		assertTrue(child00.getExpanded());
		assertTrue(root1.getExpanded());
		assertTrue(child10.getExpanded());

		tree.collapseAll();
		assertFalse(root0.getExpanded());
		assertFalse(child00.getExpanded());
		assertFalse(root1.getExpanded());
		assertFalse(child10.getExpanded());

		tree.expandToLevel(1);
		assertFalse(root0.getExpanded(), "level 1 is the implicit Tree root, matching JFace semantics");
		assertFalse(root1.getExpanded());

		tree.expandToLevel(2);
		assertTrue(root0.getExpanded());
		assertTrue(root1.getExpanded());
		assertFalse(child00.getExpanded());
		assertFalse(child10.getExpanded());

		tree.collapseAll();
		tree.expandToLevel(root0, 2);
		assertTrue(root0.getExpanded());
		assertTrue(child00.getExpanded());
		assertFalse(root1.getExpanded());

		tree.collapseToLevel(root0, 1);
		assertFalse(root0.getExpanded());
		assertTrue(child00.getExpanded(), "one level collapses only the subtree root");

		tree.setSelection(new TreeItem[] {root0, root1});
		tree.expandSelectionToLevel(1);
		assertTrue(root0.getExpanded());
		assertTrue(root1.getExpanded());
		tree.collapseSelection();
		assertFalse(root0.getExpanded());
		assertFalse(root1.getExpanded());

		assertSame(grand000, child00.getItem(0), "bulk expansion must not replace an exposed item facade");
		assertThrows(IllegalArgumentException.class, () -> tree.expandToLevel(-2));
		assertThrows(IllegalArgumentException.class, () -> tree.collapseToLevel(-2));
	});
}


@Test
public void test_virtualViewportPresentationBitsRemainWidgetOwnedWithoutNativeShapeRebuild() throws Exception {
	Class<?> topologyClass = Class.forName ("org.eclipse.swt.widgets.VirtualTreeTopology");
	Constructor<?> create = topologyClass.getDeclaredConstructor ();
	create.setAccessible (true);
	Object topology = create.newInstance ();
	Method childCount = topologyClass.getDeclaredMethod ("setChildCount", int.class, int.class);
	Method bind = topologyClass.getDeclaredMethod ("bind", int.class, int.class, int.class);
	Method setFlag = topologyClass.getDeclaredMethod ("flag", int.class, long.class, boolean.class);
	Method geometry = topologyClass.getDeclaredMethod ("generation");
	Method state = topologyClass.getDeclaredMethod ("presentationGeneration");
	for (Method method : new Method[] {childCount, bind, setFlag, geometry, state}) method.setAccessible (true);
	childCount.invoke (topology, -1, 10);
	bind.invoke (topology, 0, -1, 0);
	Class<?> itemState = Class.forName ("org.eclipse.swt.widgets.VirtualItemState");
	Field checkedField = itemState.getDeclaredField ("CHECKED");
	checkedField.setAccessible (true);
	long checked = checkedField.getLong (null);
	long originalGeometry = ((Number)geometry.invoke (topology)).longValue ();
	long originalPresentation = ((Number)state.invoke (topology)).longValue ();
	setFlag.invoke (topology, 0, checked, true);
	assertEquals (originalGeometry, ((Number)geometry.invoke (topology)).longValue (),
			"a checkbox flag must not rebuild a native Tree model");
	assertEquals (originalPresentation + 1L, ((Number)state.invoke (topology)).longValue (),
			"the parent must expose a new flag epoch");
	setFlag.invoke (topology, 0, checked, true);
	assertEquals (originalPresentation + 1L, ((Number)state.invoke (topology)).longValue (),
			"idempotent setChecked does not change the parent");
	setFlag.invoke (topology, 0, checked, false);
	assertEquals (originalPresentation + 2L, ((Number)state.invoke (topology)).longValue ());
}

@Test
public void test_virtualTreeViewportProjectsMoreThanIntegerMaxRowsWithoutPrefixMaterialization() throws Exception {
	Class<?> topologyClass = Class.forName ("org.eclipse.swt.widgets.VirtualTreeTopology");
	Constructor<?> createTopology = topologyClass.getDeclaredConstructor ();
	createTopology.setAccessible (true);
	Object topology = createTopology.newInstance ();
	Method childCount = topologyClass.getDeclaredMethod ("setChildCount", int.class, int.class);
	Method bind = topologyClass.getDeclaredMethod ("bind", int.class, int.class, int.class);
	Method flag = topologyClass.getDeclaredMethod ("flag", int.class, long.class, boolean.class);
	for (Method method : new Method[] {childCount, bind, flag}) method.setAccessible (true);
	childCount.invoke (topology, -1, Integer.MAX_VALUE);
	bind.invoke (topology, 0, -1, 0);
	childCount.invoke (topology, 0, 24);
	Class<?> flags = Class.forName ("org.eclipse.swt.widgets.VirtualItemState");
	Field expandedField = flags.getDeclaredField ("EXPANDED");
	expandedField.setAccessible (true);
	flag.invoke (topology, 0, expandedField.getLong (null), true);
	Class<?> projectionClass = Class.forName ("org.eclipse.swt.widgets.VirtualTreeVisibleProjection");
	Constructor<?> createProjection = projectionClass.getDeclaredConstructor (topologyClass);
	createProjection.setAccessible (true);
	Object projection = createProjection.newInstance (topology);
	Class<?> viewportClass = Class.forName ("org.eclipse.swt.widgets.VirtualTreeViewport");
	Constructor<?> createViewport = viewportClass.getDeclaredConstructor (projectionClass);
	createViewport.setAccessible (true);
	Object viewport = createViewport.newInstance (projection);
	Method configure = viewportClass.getDeclaredMethod ("configureGeometry", int.class, int.class);
	Method scaled = viewportClass.getDeclaredMethod ("usesScaledNativeScrollbar");
	Method maximum = viewportClass.getDeclaredMethod ("scrollbarMaximum");
	Method thumb = viewportClass.getDeclaredMethod ("scrollbarThumb");
	Method topRow = viewportClass.getDeclaredMethod ("topRow");
	Method setSelection = viewportClass.getDeclaredMethod ("setScrollbarSelection", int.class);
	Method visibleRows = viewportClass.getDeclaredMethod ("visibleRows");
	Method totalRows = viewportClass.getDeclaredMethod ("visibleRowCount");
	for (Method method : new Method[] {
			configure, scaled, maximum, thumb, topRow, setSelection, visibleRows, totalRows
	}) method.setAccessible (true);
	configure.invoke (viewport, 20, 200);
	long total = ((Number)totalRows.invoke (viewport)).longValue ();
	assertEquals ((long)Integer.MAX_VALUE + 24L, total);
	assertTrue ((Boolean)scaled.invoke (viewport));
	assertEquals (Integer.MAX_VALUE, ((Number)maximum.invoke (viewport)).intValue ());
	int endSelection = ((Number)maximum.invoke (viewport)).intValue ()
			- ((Number)thumb.invoke (viewport)).intValue ();
	setSelection.invoke (viewport, endSelection);
	assertEquals (total - ((Number)visibleRows.invoke (viewport)).intValue (),
			((Number)topRow.invoke (viewport)).longValue (),
			"native scrollbar's last position must reach exact long tail");
	Method materializedCount = topologyClass.getDeclaredMethod ("materializedCount");
	materializedCount.setAccessible (true);
	assertEquals (1, ((Number)materializedCount.invoke (topology)).intValue (),
			"never allocate a native prefix for the logical range");
}

@Test
public void test_virtualTreeViewportMemoryPressureOnlyTrimsTransientOverscan() throws Exception {
	Class<?> viewport = Class.forName ("org.eclipse.swt.widgets.VirtualTreeViewport");
	Method budget = viewport.getDeclaredMethod ("adaptiveOverscan",
			int.class, int.class, int.class, long.class, long.class);
	budget.setAccessible (true);
	long heap = 256L << 20;
	assertEquals (8, ((Number)budget.invoke (null, 8, 25, 8000, heap, 32L << 20)).intValue ());
	assertEquals (0, ((Number)budget.invoke (null, 8, 25, 8000, heap, heap - (1L << 20))).intValue ());
	assertEquals (2, ((Number)budget.invoke (null, 8, 25, 2, heap, 32L << 20)).intValue ());
	assertEquals (0, ((Number)budget.invoke (null, 8, 0, 8000, heap, 32L << 20)).intValue ());
}

@Test
public void test_virtualGtk3CheckedFlagUsesExistingTreeListenersAndParentBits() throws Exception {
	if (!isGtk3()) return;
	Tree widget = newVirtualTreeWithGtkLogicalMode (SWT.VIRTUAL | SWT.CHECK | SWT.V_SCROLL, "true");
	try {
		widget.setItemCount (3);
		TreeItem item = widget.getItem (1);
		int [] selections = {0};
		widget.addListener (SWT.Selection, event -> selections [0]++);
		Field topologyField = Tree.class.getDeclaredField ("virtualTopology");
		topologyField.setAccessible (true);
		Object topology = topologyField.get (widget);
		Method shape = topology.getClass ().getDeclaredMethod ("generation");
		Method bits = topology.getClass ().getDeclaredMethod ("presentationGeneration");
		shape.setAccessible (true);
		bits.setAccessible (true);
		long shapeBefore = ((Number)shape.invoke (topology)).longValue ();
		long bitsBefore = ((Number)bits.invoke (topology)).longValue ();
		item.setChecked (true);
		assertTrue (item.getChecked ());
		assertEquals (shapeBefore, ((Number)shape.invoke (topology)).longValue (),
				"presentation flags must not recreate the GTK native model");
		assertTrue (((Number)bits.invoke (topology)).longValue () > bitsBefore);
		assertEquals (0, selections [0],
				"programmatic checkbox changes must not synthesize SWT.Selection");
	} finally {
		widget.dispose ();
	}
}


}

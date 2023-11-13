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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.test.Screenshots;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tree
 *
 * @see org.eclipse.swt.widgets.Tree
 */
public class Test_org_eclipse_swt_widgets_Tree extends Test_org_eclipse_swt_widgets_Composite {

@Rule
public TestName testName = new TestName();

@Override
@Before
public void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.MULTI);
	setWidget(tree);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		tree = new Tree(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.BORDER};
	for (int style : cases)
		tree = new Tree(shell, style);

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
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

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
	try {
		tree.getColumn(0);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	TreeColumn column0 = new TreeColumn(tree, SWT.LEFT);
	try {
		tree.getColumn(1);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(column0, tree.getColumn(0));
	TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
	assertEquals(column1, tree.getColumn(1));
	column1.dispose();
	try {
		tree.getColumn(1);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	column0.dispose();
	try {
		tree.getColumn(0);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
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
	assertTrue(":a: Item height is 0", tree.getItemHeight() > 0);
	new TreeItem(tree, 0);
	assertTrue(":b: Item height is 0", tree.getItemHeight() > 0);
}

@Test
public void test_getItemI() {
	int number = 15;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	for (int i = 0; i < number; i++)
		assertEquals("i=" + i, items[i], tree.getItem(i));
	try {
		tree.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		tree.getItem(number+1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		tree.getItem(-1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
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
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

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
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

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
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	assertEquals(0, tree.getSelectionCount());
	tree.selectAll();
	assertEquals(number, tree.getSelectionCount());

	makeCleanEnvironment(true); // single-selection tree

	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

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
	try {
		tree.getItem(4);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
	tree.setItemCount(40);
	assertEquals(40, tree.getItemCount());
	tree.getItem(39);
	tree.setItemCount(0);
	assertEquals(0, tree.getItemCount());
	try {
		tree.getItem(39);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
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

	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}

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
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

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

	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}

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
	try {
		shell.setVisible(true);
		tree.setTopItem(null);
		fail("No exception thrown for item == null");
	} catch (IllegalArgumentException e) {
	} finally {
		shell.setVisible (false);
	}
}

@Test
public void test_showItemLorg_eclipse_swt_widgets_TreeItem() {
	try {
		tree.showItem(null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}

	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}
	for(int i=0; i<number; i++)
		tree.showItem(items[i]);

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

	for(int i=0; i<number; i++)
		tree.showItem(items2[i]);

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

/* custom */
public Tree tree;

/**
 * Clean up the environment for a new test.
 *
 * @param single true if the new tree should be a single-selection one,
 * otherwise use multi-selection.
 */
private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
	if (tree != null) tree.dispose();
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
	if(SwtTestUtil.isGTK)
		code = SWT.KEYPAD_ADD;
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
		Screenshots.takeScreenshot(getClass(), testName.getMethodName());
		// check if setData is called for root item
		assertTrue("SetData not called for top item", top[0] != null);
	}


	// the "* 3" allows some surplus for platforms that pre-fetch items to improve scrolling performance:
	assertTrue("SetData callback count not in range: " + dataCounter[0],
			dataCounter[0] > visibleCount / 2 && dataCounter[0] <= visibleCount * 3);
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
	runnable.run();

	tree.dispose();
	tree = new Tree(shell, SWT.VIRTUAL);
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

private double measureGetItemNanos(int childCount) {
	tree.setItemCount(1);
	TreeItem parent = tree.getItem(0);
	parent.setItemCount(childCount);
	return measureNanos(() -> parent.getItem(childCount - 1));
}

/**
 * Execution time of <code> TreeItem.getItem(int index)</code> should not depend on <code>index</code>
 * @see https://github.com/eclipse-platform/eclipse.platform.swt/issues/882
*/
@Test
public void test_getItemNoGrowth() {
	testTreeRegularAndVirtual(() -> {
		assertConstant("getItem() execution time", this::measureGetItemNanos);
	});
}

private double measureGetItemCountNanos(int childCount) {
	tree.setItemCount(1);
	TreeItem parent = tree.getItem(0);
	parent.setItemCount(childCount);
	return measureNanos(parent::getItemCount);
}

/**
 * Execution time of <code> TreeItem.getItemCount()</code> should not depend on child count.
 * @see https://github.com/eclipse-platform/eclipse.platform.swt/issues/882
*/
@Test
public void test_getItemCountNoGrowth() {
	testTreeRegularAndVirtual(() -> {
		assertConstant("itemCount execution time", this::measureGetItemCountNanos);
	});
}


void buildBinaryTree(TreeItem parent, int totalChildCount) {
	if (totalChildCount <= 0) {
		return;
	}
	int leftCount = totalChildCount / 2;
	int rightCount = totalChildCount - leftCount;
	if (leftCount > 1) {
		buildBinaryTree(new TreeItem(parent, SWT.NONE), leftCount - 1);
	}
	if (rightCount > 1) {
		buildBinaryTree(new TreeItem(parent, SWT.NONE), rightCount - 1);
	}
}

void depthFirstTraverse(TreeItem parent) {
	int count = parent.getItemCount();
	for (int i = 0; i < count ; i++) {
		depthFirstTraverse(parent.getItem(i));
	}
}

private void breadthFirstTraverse(TreeItem parent) {
	Deque<TreeItem> queue = new ArrayDeque<>();
	queue.add(parent);
	while (!queue.isEmpty()) {
		parent = queue.removeFirst();
		int count = parent.getItemCount();
		for (int i = 0; i < count ; i++) {
			queue.add(parent.getItem(i));
		}
	}
}


/**
 * Measures time required to do one operation
 * @return nanoseconds
 */
private double measureNanos(Runnable operation) {
	// warmup and calibration - we measure, how many iterations we can approximately do in a second
	long warmupStop = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);
	long iterationCount = 0;
	while (System.nanoTime() < warmupStop) {
		System.nanoTime();
		operation.run();
		iterationCount++;
	}
	if (iterationCount < 100) {
		iterationCount = 100;
	}

	long start = System.nanoTime();
	for (int i = 0; i < iterationCount; i++) {
		operation.run();
	}
	long stop = System.nanoTime();
	long elapsed = stop-start;
	return ((double)elapsed) / iterationCount;
}

private double measureBinaryDepthFirstTraverse(int totalChildCount) {
	TreeItem root = new TreeItem(tree, SWT.NONE);
	buildBinaryTree(root, totalChildCount - 1);
	return measureNanos(() -> depthFirstTraverse(root));
}

private void assertConstant(String message, IntFunction<Double> function) {
	function.apply(1000); // warmmup
	double elapsed_100 = function.apply(100);
	double elapsed_100000 = function.apply(100000);
	double ratio = elapsed_100000 / elapsed_100;
	String error = String.format( "%s should be constant. But:\nTime for 100 elements: %f ns\nTime for 100000 elements: %f ns\nRatio: %f\n", message, elapsed_100, elapsed_100000, ratio);
	assertTrue(error,  (elapsed_100000 <= 10 && elapsed_100 <= 10) || ratio < 2);
}

private void assertLinear(String message, IntFunction<Double> function) {
	function.apply(1000); // warmmup
	double elapsed_100 = function.apply(100);
	double elapsed_100000 = function.apply(100000);
	double ratio = elapsed_100000 / elapsed_100;
	String error = String.format( "%s should be linear. But:\nTime for 100 elements: %f ns\nTime for 100000 elements: %f ns\nRatio: %f\n", message, elapsed_100, elapsed_100000, ratio);
	assertTrue(error,  (elapsed_100000 <= 100 && elapsed_100 <= 100) || ratio < 2000);
}

@Test
public void test_binaryDepthFirstTraversalLinearGrowth() {
	testTreeRegularAndVirtual(() -> {
		assertLinear("Depth first traversal", this::measureBinaryDepthFirstTraverse);
	});
}

private void buildWideTree(TreeItem root, int totalChildCount) {
	int parentCount = (int) Math.sqrt(totalChildCount);
	int childCount = parentCount - 1;

	root.setItemCount(parentCount);
	totalChildCount -= parentCount;
	for (TreeItem parent: root.getItems()) {
		parent.setItemCount(childCount);
		totalChildCount -= childCount;
	}
	if (totalChildCount > 0) {
		TreeItem parent = new TreeItem(root, SWT.NONE);
		if (totalChildCount > 1) {
			parent.setItemCount(totalChildCount - 1);
		}
	}
}

private double measureWideDepthFirstTraverse(int totalChildCount) {
	TreeItem root = new TreeItem(tree, SWT.NONE);
	buildWideTree(root, totalChildCount - 1);
	return measureNanos(() -> depthFirstTraverse(root));
}

@Test
@Ignore("https://github.com/eclipse-platform/eclipse.platform.swt/issues/882 Wide tree depth first traversal is still  bad")
public void test_wideDepthFirstTraversalLinearGrowth() {
	testTreeRegularAndVirtual(() -> {
		assertLinear("Depth first traversal", this::measureWideDepthFirstTraverse);
	});
}

private double measureWideBreadthFirstTraverse(int totalChildCount) {
	TreeItem root = new TreeItem(tree, SWT.NONE);
	buildWideTree(root, totalChildCount - 1);
	return measureNanos(() -> breadthFirstTraverse(root));
}

@Test
public void test_wideBreadthFirstTraversalLinearGrowth() {
	testTreeRegularAndVirtual(() -> {
		assertLinear("Depth first traversal", this::measureWideBreadthFirstTraverse);
	});
}

}

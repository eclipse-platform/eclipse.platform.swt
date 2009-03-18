/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.Vector;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tree
 *
 * @see org.eclipse.swt.widgets.Tree
 */
public class Test_org_eclipse_swt_widgets_Tree extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_Tree(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.MULTI);
	setWidget(tree);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		tree = new Tree(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.BORDER};
	for (int i = 0; i < cases.length; i++)
		tree = new Tree(shell, cases[i]);

	cases = new int[]{0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TreeItem(tree, 0);
		}
		assertEquals(cases[j], tree.getItemCount());
		tree.removeAll();
	}
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_addTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_addTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

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

public void test_getGridLineWidth() {
	tree.getGridLineWidth();
}

public void test_getHeaderHeight() {
	assertEquals(0, tree.getHeaderHeight());
	tree.setHeaderVisible(true);
	assertTrue(tree.getHeaderHeight() > 0);
	tree.setHeaderVisible(false);
	assertEquals(0, tree.getHeaderHeight());
}

public void test_getHeaderVisible() {
	// tested in test_setHeaderVisibleZ
}

public void test_getItemCount() {
	//tested in test_setItemCountI
}

public void test_getItemHeight() {
	assertTrue(":a: Item height is 0", tree.getItemHeight() > 0);
	new TreeItem(tree, 0);
	assertTrue(":b: Item height is 0", tree.getItemHeight() > 0);	
}

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

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

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
		assertEquals(items[j], tree.getItems());
		tree.removeAll();
		assertEquals(0, tree.getItemCount());
	}

	makeCleanEnvironment(false);
	
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
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

public void test_getLinesVisible() {
	// tested in test_setHeaderVisibleZ
}

public void test_getParentItem() {
	assertNull(tree.getParentItem());
}

public void test_getSelection() {
	// Tested in setSelection.
}

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

public void test_getTopItem() {
// tested in test_setTopItemLorg_eclipse_swt_widgets_TreeItem
}

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

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_removeTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_removeTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

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

public void test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ() {
	warnUnimpl("Test test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ not written");
}

public void test_setHeaderVisibleZ() {
	assertFalse(tree.getHeaderVisible());
	tree.setHeaderVisible(true);
	assertTrue(tree.getHeaderVisible());
	tree.setHeaderVisible(false);
	assertFalse(tree.getHeaderVisible());
}

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
	tree.removeAll();
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
}

public void test_setLinesVisibleZ() {
	if (SwtJunit.isCarbon) {
		// carbon does not support lines
		tree.getLinesVisible();
		return;
	}
	assertFalse(tree.getLinesVisible());
	tree.setLinesVisible(true);
	assertTrue(tree.getLinesVisible());
	tree.setLinesVisible(false);
	assertFalse(tree.getLinesVisible());
}

public void test_setRedrawZ() {
	warnUnimpl("Test test_setRedrawZ not written");
}

public void test_setSelection$Lorg_eclipse_swt_widgets_TreeItem() {
	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}

	assertEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[5], items[16], items[19]});
	assertSame(new TreeItem[] {items[5], items[16], items[19]}, tree.getSelection());

	tree.setSelection(items);
	assertSame(items, tree.getSelection());
	
	tree.setSelection(tree.getItems());
	assertSame(tree.getItems(), tree.getSelection());
	
	tree.setSelection(new TreeItem[] {});
	assertEquals(new TreeItem[] {}, tree.getSelection());
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
	assertEquals(new TreeItem[] {items[10]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[number-1]});
	assertEquals(new TreeItem[] {items[number-1]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[2]});
	assertEquals(new TreeItem[] {items[2]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[10], items[number-1], items[2]});
	assertSame(new TreeItem[] {items[2], items[10], items[number - 1]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertSame(new TreeItem[]{items[0], items[2], items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertSame(new TreeItem[]{items[1], items[2], items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertSame(new TreeItem[]{items[0], items[1], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertSame(new TreeItem[]{items[0], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertSame(new TreeItem[]{items[2], items[3], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(new TreeItem[]{items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[0]});
	assertEquals(new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertEquals(new TreeItem[] {items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[4]});
	assertEquals(new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertEquals(new TreeItem[] {items[2]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[1]});
	assertEquals(new TreeItem[] {items[1]}, tree.getSelection());
	
	tree.removeAll();
	tree.setSelection(new TreeItem[] {});
	assertEquals(new TreeItem[] {}, tree.getSelection());
	

	makeCleanEnvironment(true); // single-selection tree
	
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	assertEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[5], items[16], items[19]});
	assertEquals(new TreeItem[] {}, tree.getSelection());

	tree.setSelection(items);
	assertEquals(new TreeItem[] {}, tree.getSelection());
	
	tree.setSelection(tree.getItems());
	assertEquals(new TreeItem[] {}, tree.getSelection());
	
	tree.setSelection(new TreeItem[] {});
	assertEquals(new TreeItem[] {}, tree.getSelection());
	assertEquals(0, tree.getSelectionCount());
	
	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	} 
	catch (IllegalArgumentException e) {
	}

	tree.setSelection(new TreeItem[]{items[10]});
	assertEquals(new TreeItem[] {items[10]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[number-1]});
	assertEquals(new TreeItem[] {items[number-1]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[2]});
	assertEquals(new TreeItem[] {items[2]}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[10], items[number-1], items[2]});
	assertEquals(new TreeItem[] {}, tree.getSelection());
	
	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(new TreeItem[]{}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[0]});
	assertEquals(new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertEquals(new TreeItem[] {items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[4]});
	assertEquals(new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertEquals(new TreeItem[] {items[2]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[1]});
	assertEquals(new TreeItem[] {items[1]}, tree.getSelection());
	
	tree.removeAll();
	tree.setSelection(new TreeItem[] {});
	assertEquals(new TreeItem[] {}, tree.getSelection());
}

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

public void test_showColumnLorg_eclipse_swt_widgets_TreeColumn() {
	warnUnimpl("Test test_showColumnLorg_eclipse_swt_widgets_TreeColumn not written");
}

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

public void test_showSelection() {
	TreeItem item;
	
	tree.showSelection();
	item = new TreeItem(tree, 0);
	tree.setSelection(new TreeItem[]{item});
	tree.showSelection();	
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Tree((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_addTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_getColumnCount");
	methodNames.addElement("test_getColumnI");
	methodNames.addElement("test_getColumns");
	methodNames.addElement("test_getGridLineWidth");
	methodNames.addElement("test_getHeaderHeight");
	methodNames.addElement("test_getHeaderVisible");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItemI");
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getLinesVisible");
	methodNames.addElement("test_getParentItem");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getTopItem");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setHeaderVisibleZ");
	methodNames.addElement("test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ");
	methodNames.addElement("test_setItemCountI");
	methodNames.addElement("test_setLinesVisibleZ");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_setTopItemLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_showColumnLorg_eclipse_swt_widgets_TreeColumn");
	methodNames.addElement("test_showItemLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_showSelection");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_SpaceSelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_MouseExpand");
	methodNames.addElement("test_consistency_KeyExpand");
	methodNames.addElement("test_consistency_DoubleClick");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addTreeListenerLorg_eclipse_swt_events_TreeListener")) test_addTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_getColumnCount")) test_getColumnCount();
	else if (getName().equals("test_getColumnI")) test_getColumnI();
	else if (getName().equals("test_getColumns")) test_getColumns();
	else if (getName().equals("test_getGridLineWidth")) test_getGridLineWidth();
	else if (getName().equals("test_getHeaderHeight")) test_getHeaderHeight();
	else if (getName().equals("test_getHeaderVisible")) test_getHeaderVisible();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItemI")) test_getItemI();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getLinesVisible")) test_getLinesVisible();
	else if (getName().equals("test_getParentItem")) test_getParentItem();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getTopItem")) test_getTopItem();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener")) test_removeTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setHeaderVisibleZ")) test_setHeaderVisibleZ();
	else if (getName().equals("test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ")) test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ();
	else if (getName().equals("test_setItemCountI")) test_setItemCountI();
	else if (getName().equals("test_setLinesVisibleZ")) test_setLinesVisibleZ();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_widgets_TreeItem")) test_setSelection$Lorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_setTopItemLorg_eclipse_swt_widgets_TreeItem")) test_setTopItemLorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_showColumnLorg_eclipse_swt_widgets_TreeColumn")) test_showColumnLorg_eclipse_swt_widgets_TreeColumn();
	else if (getName().equals("test_showItemLorg_eclipse_swt_widgets_TreeItem")) test_showItemLorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_SpaceSelection")) test_consistency_SpaceSelection();
	else if (getName().equals("test_consistency_MouseExpand")) test_consistency_MouseExpand();
	else if (getName().equals("test_consistency_KeyExpand")) test_consistency_KeyExpand();
	else if (getName().equals("test_consistency_DoubleClick")) test_consistency_DoubleClick();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
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

private void createTree(Vector events) {
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

public void test_consistency_KeySelection() {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(30, 30, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_MouseExpand() {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(11, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_KeyExpand() {
    Vector events = new Vector();
    createTree(events);
    int code=SWT.ARROW_RIGHT;
    if(SwtJunit.isGTK)
        code = SWT.KEYPAD_ADD;
    consistencyEvent(0, code, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_DoubleClick () {
    Vector events = new Vector();
    createTree(events);
    consistencyPrePackShell();
    consistencyEvent(20, tree.getItemHeight()*2, 1, 0, 
            	     ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

public void test_consistency_EnterSelection () {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_SpaceSelection () {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(50, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createTree(events);
    consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

}

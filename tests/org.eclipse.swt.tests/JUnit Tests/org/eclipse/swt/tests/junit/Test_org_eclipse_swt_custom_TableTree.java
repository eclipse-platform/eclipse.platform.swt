/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTree
 *
 * @see org.eclipse.swt.custom.TableTree
 */
public class Test_org_eclipse_swt_custom_TableTree extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_TableTree(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tableTree = new TableTree(shell, style = SWT.MULTI);
	setWidget(tableTree);
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_addTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_addTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_deselectAll() {
	warnUnimpl("Test test_deselectAll not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemHeight() {
	warnUnimpl("Test test_getItemHeight not written");
}

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getSelection() {
	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	assertEquals("MULTI: After adding items, but before selecting any", new TableTreeItem[] {}, tableTree.getSelection());

	// getSelection() is further tested in test_selectAll and test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem
}

public void test_getSelectionCount() {
	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	assertEquals("MULTI: After adding items, but before selecting any", 0, tableTree.getSelectionCount());
	
	// getSelectionCount() is further tested in test_selectAll and test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem
}

public void test_getTable() {
	warnUnimpl("Test test_getTable not written");
}

public void test_indexOfLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_removeAll() {
	warnUnimpl("Test test_removeAll not written");
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_removeTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_removeTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_getChildren() {
	/* Overriding test_getChildren from Test_org_eclipse_swt_widgets_Composite
	 * to do nothing, because the child of a TableTree is always a Table.
	 */
}

public void test_selectAll() {
	/* FUTURE: Should also add sub-nodes, and test both single and multi with those.
	 * i.e. subitems[i] = new TableTreeItem(items[i], SWT.NONE); */

	selectAll_helper("Empty table tree", 0, new TableTreeItem[] {});

	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	selectAll_helper("selectAll()", number, items);

	
	/* Now run the same tests on a single-select TableTree. */
	singleSelect();
	selectAll_helper("Empty table tree", 0, new TableTreeItem[] {});

	items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	selectAll_helper("selectAll()", 0, new TableTreeItem[] {});
}

public void test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem() {
	/* FUTURE: Should also add sub-nodes, and test both single and multi with those.
	 * i.e. subitems[i] = new TableTreeItem(items[i], SWT.NONE); */

	setSelection_helper("Select no items in empty table tree", new TableTreeItem[] {}, 0, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("MULTI: No exception thrown for selecting null in empty table tree");
	} 
	catch (IllegalArgumentException e) {
	}
	
	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, 0);
	}
	
	setSelection_helper("Select no items in table tree with items", new TableTreeItem[] {}, 0, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("MULTI: No exception thrown for selecting null in table tree with items");
	} 
	catch (IllegalArgumentException e) {
	}

	for (int i = 0; i < number; i++) {
		setSelection_helper("Select item " + i, new TableTreeItem[] {items[i]}, 1, new TableTreeItem[] {items[i]});
	}
	setSelection_helper("Select items", items, number, items);
	setSelection_helper("Select tableTree.getItems()", tableTree.getItems(), number, tableTree.getItems());
	setSelection_helper("Select 2 contiguous items", new TableTreeItem[] {items[0], items[1]}, 2, new TableTreeItem[] {items[0], items[1]});
	setSelection_helper("Select 2 non-contiguous items", new TableTreeItem[] {items[3], items[6]}, 2, new TableTreeItem[] {items[3], items[6]});
	setSelection_helper("Select 3 contiguous items", new TableTreeItem[] {items[2], items[3], items[4]}, 3, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 non-contiguous items", new TableTreeItem[] {items[2], items[5], items[7]}, 3, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select 3 unordered contiguous items", new TableTreeItem[] {items[4], items[2], items[3]}, 3, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 unordered non-contiguous items", new TableTreeItem[] {items[5], items[2], items[7]}, 3, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select 3 reverse-order contiguous items", new TableTreeItem[] {items[4], items[3], items[2]}, 3, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 reverse-order non-contiguous items", new TableTreeItem[] {items[7], items[5], items[2]}, 3, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select same item twice", new TableTreeItem[] {items[0], items[4], items[0]}, 2, new TableTreeItem[] {items[0], items[4]});
	setSelection_helper("Select same item multiple times", new TableTreeItem[] {items[4], items[4], items[4], items[4], items[4], items[4]}, 1, new TableTreeItem[] {items[4]});
	setSelection_helper("Select multiple items multiple times", new TableTreeItem[] {items[4], items[0], items[2], items[4], items[4], items[0], items[4], items[2]}, 3, new TableTreeItem[] {items[0], items[2], items[4]});

	
	/* Now run the same tests on a single-select TableTree. */
	singleSelect();
	
	setSelection_helper("Select no items in empty table tree", new TableTreeItem[] {}, 0, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("SINGLE: No exception thrown for selecting null in empty table tree");
	} 
	catch (IllegalArgumentException e) {
	}
	
	items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, 0);
	}
	
	setSelection_helper("Select no items in table tree with items", new TableTreeItem[] {}, 0, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("SINGLE: No exception thrown for selecting null in table tree with items");
	} 
	catch (IllegalArgumentException e) {
	}

	for (int i = 0; i < number; i++) {
		setSelection_helper("Select item " + i, new TableTreeItem[] {items[i]}, 1, new TableTreeItem[] {items[i]});
	}
	setSelection_helper("Select items", items, 0, new TableTreeItem[] {});
	setSelection_helper("Select tableTree.getItems()", tableTree.getItems(), 0, new TableTreeItem[] {});
	setSelection_helper("Select 2 contiguous items", new TableTreeItem[] {items[0], items[1]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 2 non-contiguous items", new TableTreeItem[] {items[3], items[6]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 contiguous items", new TableTreeItem[] {items[2], items[3], items[4]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 non-contiguous items", new TableTreeItem[] {items[2], items[5], items[7]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 unordered contiguous items", new TableTreeItem[] {items[4], items[2], items[3]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 unordered non-contiguous items", new TableTreeItem[] {items[5], items[2], items[7]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 reverse-order contiguous items", new TableTreeItem[] {items[4], items[3], items[2]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select 3 reverse-order non-contiguous items", new TableTreeItem[] {items[7], items[5], items[2]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select same item twice", new TableTreeItem[] {items[0], items[4], items[0]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select same item multiple times", new TableTreeItem[] {items[4], items[4], items[4], items[4], items[4], items[4]}, 0, new TableTreeItem[] {});
	setSelection_helper("Select multiple items multiple times", new TableTreeItem[] {items[4], items[0], items[2], items[4], items[4], items[0], items[4], items[2]}, 0, new TableTreeItem[] {});
}

public void test_showItemLorg_eclipse_swt_custom_TableTreeItem() {
	warnUnimpl("Test test_showItemLorg_eclipse_swt_custom_TableTreeItem not written");
}

public void test_showSelection() {
	warnUnimpl("Test test_showSelection not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TableTree((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_addTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getTable");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_showItemLorg_eclipse_swt_custom_TableTreeItem");
	methodNames.addElement("test_showSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addTreeListenerLorg_eclipse_swt_events_TreeListener")) test_addTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getTable")) test_getTable();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_custom_TableTreeItem")) test_indexOfLorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener")) test_removeTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem")) test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_showItemLorg_eclipse_swt_custom_TableTreeItem")) test_showItemLorg_eclipse_swt_custom_TableTreeItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else super.runTest();
}

/* custom */
private TableTree tableTree;
private int style;

/*
 * Sets a single-select TableTree as the test widget.
 * Note: This method must be private or protected so that the auto-gen tool keeps it.
 */
private void singleSelect() {
	tableTree.dispose();
	tableTree = new TableTree(shell, style = SWT.SINGLE);
	setWidget(tableTree);
}

/*
 * Used in test_selectAll.
 * Note: This method must be private or protected so that the auto-gen tool keeps it.
 */
private void selectAll_helper(String message, int expectedCount, TableTreeItem[] expectedSelection) {
	tableTree.selectAll();
	message = (style == SWT.MULTI ? "MULTI" : "SINGLE") + ": " + message;
	assertEquals(message, expectedCount, tableTree.getSelectionCount());
	assertEquals(message, expectedSelection, tableTree.getSelection());	
}

/*
 * Used in test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem.
 * Note: This method must be private or protected so that the auto-gen tool keeps it.
 */
private void setSelection_helper(String message, TableTreeItem[] itemsToSelect, int expectedCount, TableTreeItem[] expectedSelection) {
	tableTree.setSelection(itemsToSelect);
	message = (style == SWT.MULTI ? "MULTI" : "SINGLE") + ": " + message;
	assertEquals(message, expectedCount, tableTree.getSelectionCount());
	assertEquals(message, expectedSelection, tableTree.getSelection());	
}
}

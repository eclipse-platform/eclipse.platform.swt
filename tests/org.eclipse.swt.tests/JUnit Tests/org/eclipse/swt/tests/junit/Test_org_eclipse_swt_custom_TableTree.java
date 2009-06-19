/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.TableColumn;

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

	selectAll_helper("Empty table tree", new TableTreeItem[] {});

	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	selectAll_helper("selectAll()", items);

	
	/* Now run the same tests on a single-select TableTree. */
	singleSelect();
	selectAll_helper("Empty table tree", new TableTreeItem[] {});

	items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	selectAll_helper("selectAll()", new TableTreeItem[] {});
}

public void test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem() {
	/* FUTURE: Should also add sub-nodes, and test both single and multi with those.
	 * i.e. subitems[i] = new TableTreeItem(items[i], SWT.NONE); */

	setSelection_helper("Select no items in empty table tree", new TableTreeItem[] {}, new TableTreeItem[] {});
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
	
	setSelection_helper("Select no items in table tree with items", new TableTreeItem[] {}, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("MULTI: No exception thrown for selecting null in table tree with items");
	} 
	catch (IllegalArgumentException e) {
	}

	for (int i = 0; i < number; i++) {
		setSelection_helper("Select item " + i, new TableTreeItem[] {items[i]}, new TableTreeItem[] {items[i]});
	}
	setSelection_helper("Select items", items, items);
	setSelection_helper("Select tableTree.getItems()", tableTree.getItems(), tableTree.getItems());
	setSelection_helper("Select 2 contiguous items", new TableTreeItem[] {items[0], items[1]}, new TableTreeItem[] {items[0], items[1]});
	setSelection_helper("Select 2 non-contiguous items", new TableTreeItem[] {items[3], items[6]}, new TableTreeItem[] {items[3], items[6]});
	setSelection_helper("Select 3 contiguous items", new TableTreeItem[] {items[2], items[3], items[4]}, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 non-contiguous items", new TableTreeItem[] {items[2], items[5], items[7]}, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select 3 unordered contiguous items", new TableTreeItem[] {items[4], items[2], items[3]}, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 unordered non-contiguous items", new TableTreeItem[] {items[5], items[2], items[7]}, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select 3 reverse-order contiguous items", new TableTreeItem[] {items[4], items[3], items[2]}, new TableTreeItem[] {items[2], items[3], items[4]});
	setSelection_helper("Select 3 reverse-order non-contiguous items", new TableTreeItem[] {items[7], items[5], items[2]}, new TableTreeItem[] {items[2], items[5], items[7]});
	setSelection_helper("Select same item twice", new TableTreeItem[] {items[0], items[4], items[0]}, new TableTreeItem[] {items[0], items[4]});
	setSelection_helper("Select same item multiple times", new TableTreeItem[] {items[4], items[4], items[4], items[4], items[4], items[4]}, new TableTreeItem[] {items[4]});
	setSelection_helper("Select multiple items multiple times", new TableTreeItem[] {items[4], items[0], items[2], items[4], items[4], items[0], items[4], items[2]}, new TableTreeItem[] {items[0], items[2], items[4]});

	
	/* Now run the same tests on a single-select TableTree. */
	singleSelect();
	
	setSelection_helper("Select no items in empty table tree", new TableTreeItem[] {}, new TableTreeItem[] {});
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
	
	setSelection_helper("Select no items in table tree with items", new TableTreeItem[] {}, new TableTreeItem[] {});
	try {
		tableTree.setSelection((TableTreeItem[]) null);
		fail("SINGLE: No exception thrown for selecting null in table tree with items");
	} 
	catch (IllegalArgumentException e) {
	}

	for (int i = 0; i < number; i++) {
		setSelection_helper("Select item " + i, new TableTreeItem[] {items[i]}, new TableTreeItem[] {items[i]});
	}
	setSelection_helper("Select items", items, new TableTreeItem[] {});
	setSelection_helper("Select tableTree.getItems()", tableTree.getItems(), new TableTreeItem[] {});
	setSelection_helper("Select 2 contiguous items", new TableTreeItem[] {items[0], items[1]}, new TableTreeItem[] {});
	setSelection_helper("Select 2 non-contiguous items", new TableTreeItem[] {items[3], items[6]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 contiguous items", new TableTreeItem[] {items[2], items[3], items[4]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 non-contiguous items", new TableTreeItem[] {items[2], items[5], items[7]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 unordered contiguous items", new TableTreeItem[] {items[4], items[2], items[3]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 unordered non-contiguous items", new TableTreeItem[] {items[5], items[2], items[7]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 reverse-order contiguous items", new TableTreeItem[] {items[4], items[3], items[2]}, new TableTreeItem[] {});
	setSelection_helper("Select 3 reverse-order non-contiguous items", new TableTreeItem[] {items[7], items[5], items[2]}, new TableTreeItem[] {});
	setSelection_helper("Select same item twice", new TableTreeItem[] {items[0], items[4], items[0]}, new TableTreeItem[] {});
	setSelection_helper("Select same item multiple times", new TableTreeItem[] {items[4], items[4], items[4], items[4], items[4], items[4]}, new TableTreeItem[] {});
	setSelection_helper("Select multiple items multiple times", new TableTreeItem[] {items[4], items[0], items[2], items[4], items[4], items[0], items[4], items[2]}, new TableTreeItem[] {});
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
private void selectAll_helper(String message, TableTreeItem[] expectedSelection) {
	tableTree.selectAll();
	message = (style == SWT.MULTI ? "MULTI" : "SINGLE") + ": " + message;
	assertEquals(message, expectedSelection.length, tableTree.getSelectionCount());
	assertSame(message, expectedSelection, tableTree.getSelection());
}

/*
 * Used in test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem.
 * Note: This method must be private or protected so that the auto-gen tool keeps it.
 */
private void setSelection_helper(String message, TableTreeItem[] itemsToSelect, TableTreeItem[] expectedSelection) {
	tableTree.setSelection(itemsToSelect);
	message = (style == SWT.MULTI ? "MULTI" : "SINGLE") + ": " + message;
	assertEquals(message, expectedSelection.length, tableTree.getSelectionCount());
	assertSame(message, expectedSelection, tableTree.getSelection());	
}

private void createTableTree(Vector events, boolean traverse) {
    String test = getTestName();
    tableTree = new TableTree(shell, SWT.BORDER | SWT.SINGLE);
	for (int col = 0; col < 3; col++) {
		TableColumn column = new TableColumn(tableTree.getTable(), SWT.NONE);
		column.setText("Col " + col);
		column.setWidth(70);
		hookExpectedEvents(column, test, events);
	}
	for (int node = 0; node < 4; node++) {
		TableTreeItem item = new TableTreeItem(tableTree, SWT.NONE);
		for (int col = 0; col < 3; col++) {
			item.setText(col, "TTItem" + node + col);
		}
		hookExpectedEvents(item, test, events);
		TableTreeItem subitem = new TableTreeItem(item, SWT.NONE);
		for (int col = 0; col < 3; col++) {
			subitem.setText(col, "TTSub" + node + col);
		}
		hookExpectedEvents(subitem, test, events);
	}
	String[] types = (String[])ConsistencyUtility.eventOrdering.get("TableTreeTable");
	if(!traverse) {
	    String[] temp = new String[types.length -1];
	    System.arraycopy(types, 0, temp, 0, types.length-1);
	    types = temp;
	}
	hookExpectedEvents(tableTree.getTable(), types, events);
	setWidget(tableTree);
}

public void test_consistency_KeySelection() {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(30, 30, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_MouseExpand() {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(11, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_KeyExpand() {
    Vector events = new Vector();
    createTableTree(events, true);
    tableTree.setSelection(new TableTreeItem[] { tableTree.getItems()[0]});
    int code=SWT.ARROW_RIGHT;
    if(SwtJunit.isGTK)
        code = SWT.KEYPAD_ADD;
    consistencyEvent(0, code, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_DoubleClick () {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyPrePackShell();
    consistencyEvent(20, tableTree.getItemHeight()*2, 1, 0, 
            	     ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

public void test_consistency_EnterSelection () {
    Vector events = new Vector();
    createTableTree(events, false);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_SpaceSelection () {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(50, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createTableTree(events, true);
    consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

}

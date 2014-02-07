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


import static org.junit.Assert.assertArrayEquals;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TableTree
 *
 * @see org.eclipse.swt.custom.TableTree
 */
@SuppressWarnings("deprecation")
public class Test_org_eclipse_swt_custom_TableTree extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_TableTree(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	tableTree = new TableTree(shell, style = SWT.MULTI);
	setWidget(tableTree);
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
}

public void test_getSelection() {
	int number = 8;
	TableTreeItem[] items = new TableTreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableTreeItem(tableTree, SWT.NONE);
	}
	assertArrayEquals("MULTI: After adding items, but before selecting any", new TableTreeItem[] {}, tableTree.getSelection());

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

@Override
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
	assertArrayEquals(message, expectedSelection, tableTree.getSelection());
}

/*
 * Used in test_setSelection$Lorg_eclipse_swt_custom_TableTreeItem.
 * Note: This method must be private or protected so that the auto-gen tool keeps it.
 */
private void setSelection_helper(String message, TableTreeItem[] itemsToSelect, TableTreeItem[] expectedSelection) {
	tableTree.setSelection(itemsToSelect);
	message = (style == SWT.MULTI ? "MULTI" : "SINGLE") + ": " + message;
	assertEquals(message, expectedSelection.length, tableTree.getSelectionCount());
	assertArrayEquals(message, expectedSelection, tableTree.getSelection());	
}

private void createTableTree(Vector<String> events, boolean traverse) {
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
	String[] types = ConsistencyUtility.eventOrdering.get("TableTreeTable");
	if(!traverse) {
	    String[] temp = new String[types.length -1];
	    System.arraycopy(types, 0, temp, 0, types.length-1);
	    types = temp;
	}
	hookExpectedEvents(tableTree.getTable(), types, events);
	setWidget(tableTree);
}

public void test_consistency_KeySelection() {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MouseSelection() {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(30, 30, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_MouseExpand() {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(11, 10, 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_KeyExpand() {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    tableTree.setSelection(new TableTreeItem[] { tableTree.getItems()[0]});
    int code=SWT.ARROW_RIGHT;
    if(SwtTestUtil.isGTK)
        code = SWT.KEYPAD_ADD;
    consistencyEvent(0, code, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_DoubleClick () {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyPrePackShell();
    consistencyEvent(20, tableTree.getItemHeight()*2, 1, 0, 
            	     ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

public void test_consistency_EnterSelection () {
    Vector<String> events = new Vector<String>();
    createTableTree(events, false);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_SpaceSelection () {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(' ', 32, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(50, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector<String> events = new Vector<String>();
    createTableTree(events, true);
    consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

}

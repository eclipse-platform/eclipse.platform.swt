package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Tree
 *
 * @see org.eclipse.swt.widgets.Tree
 */
public class Test_org_eclipse_swt_widgets_Tree extends Test_org_eclipse_swt_widgets_Composite {

public Tree tree;

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

protected void tearDown() {
	super.tearDown();
}

/**
 * Clean up the environment for a new test.
 * 
 * @param single true if the new tree should be a single-selection one,
 * otherwise use multi-selection.
 */
private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
	tree.dispose();
	tree = new Tree(shell, single?SWT.SINGLE:SWT.MULTI);
	setWidget(tree);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
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
			TreeItem ti = new TreeItem(tree, 0);
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

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

public void test_getItemCount() {
	warnUnimpl("Test test_getItemCount not written");
}

public void test_getItemHeight() {
	assertTrue(":a: Item height is 0", tree.getItemHeight() > 0);
	new TreeItem(tree, 0);
	assertTrue(":b: Item height is 0", tree.getItemHeight() > 0);	
}

public void test_getItems(){
	int[] cases = {0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TreeItem ti = new TreeItem(tree, 0);
		}
		assertEquals(cases[j], tree.getItems().length);
		tree.removeAll();
		assertEquals(0, tree.getItemCount());
	}

	makeCleanEnvironment(false);
	
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TreeItem ti = new TreeItem(tree, 0);
			ti.setText(String.valueOf(i));
		}
		TreeItem[] items = tree.getItems();
		for (int i = 0; i < items.length; i++) {
			assertEquals(String.valueOf(i), items[i].getText());
		}
		tree.removeAll();
		assertEquals(0, tree.getItemCount());
	}
}

public void test_getParentItem() {
	assertNull(tree.getParentItem());
}

public void test_getSelection() {
	warnUnimpl("Test test_getSelection not written");
}

public void test_getSelectionCount(){
	makeCleanEnvironment(true); // use single-selection tree.

	int number = 15;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[2], items[number-1], items[10]});
	assertEquals(1, tree.getSelectionCount());
	
	tree.setSelection(items);
	assertEquals(1, tree.getSelectionCount());
}

public void test_getTopItem() {
// tested in test_setTopItemLorg_eclipse_swt_widgets_TreeItem
}

public void test_removeAll() {
	tree.removeAll();

	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}
	tree.removeAll();
	assertEquals(0, tree.getItemCount());
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_removeTreeListenerLorg_eclipse_swt_events_TreeListener() {
	warnUnimpl("Test test_removeTreeListenerLorg_eclipse_swt_events_TreeListener not written");
}

public void test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ() {
	warnUnimpl("Test test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ not written");
}

public void test_selectAll(){
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

public void test_setRedrawZ() {
	warnUnimpl("Test test_setRedrawZ not written");
}

public void test_setSelection$Lorg_eclipse_swt_widgets_TreeItem(){
	int number = 20;
	TreeItem[] items = new TreeItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TreeItem(tree, 0);
	}
	TreeItem[] itemsToBeSelected = {items[5], items[16], items[19]};
	tree.setSelection(itemsToBeSelected);
	TreeItem[] selItems = tree.getSelection();
	int selLength = selItems.length;
	if (fCheckMultiSelectTree) {
		assertTrue(":len:" + String.valueOf(selLength), selLength != itemsToBeSelected.length);
		for (int i = 0; i < selLength; i++)
			if (selItems[i] == itemsToBeSelected[i])
				fail();
	}
	tree.setSelection(tree.getItems());
	assertEquals(":b:", items, tree.getItems());
	tree.setSelection(new TreeItem[] {});
	if (fCheckMultiSelectTree)
		assertEquals(":c:", new TreeItem[] {}, tree.getItems());
	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	} 
	catch (IllegalArgumentException e) {
	}
	tree.removeAll();
	
	makeCleanEnvironment(false);
	
	number = 5;
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);
	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(0, tree.getSelectionCount());
	
	makeCleanEnvironment(false);
	
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	tree.setSelection(new TreeItem[]{});
	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertEquals(":b:", new TreeItem[]{items[0], items[2], items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertEquals(":c:", new TreeItem[]{items[1], items[2], items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertEquals(":d:", new TreeItem[]{items[0], items[1], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertEquals(":e:", new TreeItem[]{items[0], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertEquals(":f:", new TreeItem[]{items[2], items[3], items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(":g:", new TreeItem[]{items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4]});
	assertEquals(":h:", new TreeItem[]{items[4]}, tree.getSelection());	

	makeCleanEnvironment(false);
	
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	tree.setSelection(new TreeItem[] {items[0]});
	assertEquals(":a:", new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertEquals(":b:", new TreeItem[] {items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[4]});
	assertEquals(":c:", new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertEquals(":d:", new TreeItem[] {items[2]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[1]});
	assertEquals(":e:", new TreeItem[] {items[1]}, tree.getSelection());
	
	makeCleanEnvironment(true); // single-selection tree
	
	number = 15;
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	assertEquals(":a:", new TreeItem[] {}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[10], items[number-1], items[2]});
	assertEquals(":b:", new TreeItem[] {items[10]}, tree.getSelection());
	
	tree.setSelection(items);
	assertEquals(":c:", new TreeItem[] {items[0]}, tree.getSelection());

	makeCleanEnvironment(true); //single-selection tree
		
	number = 5;
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);
	try {
		tree.setSelection((TreeItem[]) null);
		fail("No exception thrown for items == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(0, tree.getSelectionCount());

	makeCleanEnvironment(true); // single-selection tree
	
	number = 5;
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	tree.setSelection(new TreeItem[]{});
	assertEquals(0, tree.getSelectionCount());

	tree.setSelection(new TreeItem[]{items[0], items[3], items[2]});
	assertEquals(":b:", new TreeItem[]{items[0]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[3], items[2], items[1]});
	assertEquals(":c:", new TreeItem[]{items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[1], items[4], items[0]});
	assertEquals(":d:", new TreeItem[]{items[1]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[0], items[4], items[0]});
	assertEquals(":e:", new TreeItem[]{items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[]{items[2], items[3], items[4]});
	assertEquals(":f:", new TreeItem[]{items[2]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(":g:", new TreeItem[]{items[4]}, tree.getSelection());	

	tree.setSelection(new TreeItem[]{items[4]});
	assertEquals(":h:", new TreeItem[]{items[4]}, tree.getSelection());	

	makeCleanEnvironment(true); //single-selection tree
	
	number = 5;
	items = new TreeItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TreeItem(tree, 0);

	tree.setSelection(new TreeItem[] {items[0]});
	assertEquals(":a:", new TreeItem[] {items[0]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[3]});
	assertEquals(":b:", new TreeItem[] {items[3]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[4]});
	assertEquals(":c:", new TreeItem[] {items[4]}, tree.getSelection());

	tree.setSelection(new TreeItem[] {items[2]});
	assertEquals(":d:", new TreeItem[] {items[2]}, tree.getSelection());	

	tree.setSelection(new TreeItem[] {items[1]});
	assertEquals(":e:", new TreeItem[] {items[1]}, tree.getSelection());	
}

public void test_setTopItemLorg_eclipse_swt_widgets_TreeItem() {
	tree.removeAll();
	for (int i = 0; i < 10; i++) {
		TreeItem item = new TreeItem(tree, 0);	
	}
	TreeItem top = new TreeItem(tree, 0);
	for (int i = 0; i < 10; i++) {
		TreeItem item = new TreeItem(tree, 0);	
	}
	tree.setTopItem(top);
	for (int i = 0; i < 10; i++) {
		TreeItem item = new TreeItem(tree, 0);	
	}
	TreeItem top2 = tree.getTopItem();
	assertEquals(top, top2);
	try {
		tree.setTopItem(null);
		fail("No exception thrown for item == null");
	}
	catch (IllegalArgumentException e) {
	}		
}

public void test_showItemLorg_eclipse_swt_widgets_TreeItem(){
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
	};

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
	methodNames.addElement("test_getItemLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItemHeight");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getParentItem");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getTopItem");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener");
	methodNames.addElement("test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_setTopItemLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_showItemLorg_eclipse_swt_widgets_TreeItem");
	methodNames.addElement("test_showSelection");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addTreeListenerLorg_eclipse_swt_events_TreeListener")) test_addTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_getItemLorg_eclipse_swt_graphics_Point")) test_getItemLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItemHeight")) test_getItemHeight();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getParentItem")) test_getParentItem();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getTopItem")) test_getTopItem();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeTreeListenerLorg_eclipse_swt_events_TreeListener")) test_removeTreeListenerLorg_eclipse_swt_events_TreeListener();
	else if (getName().equals("test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ")) test_setInsertMarkLorg_eclipse_swt_widgets_TreeItemZ();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_widgets_TreeItem")) test_setSelection$Lorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_setTopItemLorg_eclipse_swt_widgets_TreeItem")) test_setTopItemLorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_showItemLorg_eclipse_swt_widgets_TreeItem")) test_showItemLorg_eclipse_swt_widgets_TreeItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else super.runTest();
}
}

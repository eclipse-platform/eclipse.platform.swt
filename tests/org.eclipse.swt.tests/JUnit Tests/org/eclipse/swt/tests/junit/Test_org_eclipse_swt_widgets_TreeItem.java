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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TreeItem
 *
 * @see org.eclipse.swt.widgets.TreeItem
 */
public class Test_org_eclipse_swt_widgets_TreeItem extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TreeItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	tree = new Tree(shell, 0);
	treeItem = new TreeItem(tree, 0);
	setWidget(treeItem);
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeI() {
	try {
		new TreeItem((TreeItem)null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	for (int i=0; i<10; i++) {
		new TreeItem(tree, 0);	
	}
	assertEquals(11, tree.getItemCount());
	new TreeItem(tree, 0, 5);	
	assertEquals(12, tree.getItemCount());
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeII() {
	try {
		new TreeItem(tree, 0, 5);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeItemI() {
	for (int i = 0; i < 10; i++) {
		new TreeItem(treeItem, 0);
	}
	assertEquals(10, treeItem.getItemCount());
	new TreeItem(treeItem, 0, 5);
	assertEquals(1, tree.getItemCount());
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeItemII() {
	try {
		new TreeItem(treeItem, 0, 5);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {}
	assertEquals(1, tree.getItemCount());
}

public void test_getBackground() {
	// tested in test_setBackgroundLorg_eclipse_swt_graphics_Color
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getChecked() {
	Tree newTree = new Tree(shell, SWT.CHECK);
	TreeItem tItem = new TreeItem(newTree,0);
	assertEquals(false, tItem.getChecked());
	tItem.setChecked(true);
	assertTrue(tItem.getChecked());
	tItem.setChecked(false);
	assertEquals(false, tItem.getChecked());
}

public void test_getExpanded() {
	assertEquals(false, treeItem.getExpanded());
	// there must be at least one subitem before you can set the treeitem expanded
	new TreeItem(treeItem, 0);
	treeItem.setExpanded(true);
	assertTrue(treeItem.getExpanded());
	treeItem.setExpanded(false);
	assertEquals(false, treeItem.getExpanded());
}

public void test_getForeground() {
	// tested in test_setForegroundLorg_eclipse_swt_graphics_Color
}

public void test_getGrayed() {
	warnUnimpl("Test test_getGrayed not written");
}

public void test_getItemCount() {
	for (int i = 0; i < 10; i++) {
		assertEquals(i, treeItem.getItemCount());
		new TreeItem(treeItem, 0);
	}
	assertTrue("b: ", treeItem.getItemCount() == 10);
}

public void test_getItems() {
	if (fCheckBogusTestCases) {
		int[] cases = {2, 10, 100};
		for (int j = 0; j < cases.length; j++) {
			for (int i = 0; i < cases[j]; i++) {
				TreeItem ti = new TreeItem(tree, 0);
			}
			assertEquals(cases[j], tree.getItems().length);
			tree.removeAll();
			assertEquals(0, tree.getItemCount());
		}
	}
}

public void test_getParent() {
	assertEquals(tree, treeItem.getParent());
}

public void test_getParentItem() {
	TreeItem tItem = new TreeItem(treeItem, SWT.NULL);
	assertEquals(treeItem, tItem.getParentItem());
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(treeItem.getDisplay(), 255, 0, 0);
	treeItem.setBackground(color);
	assertEquals(color, treeItem.getBackground());
	treeItem.setBackground(null);
	assertEquals(tree.getBackground(),treeItem.getBackground());
	color.dispose();
	try { 
		treeItem.setBackground(color);
		fail("No exception thrown for color disposed");		
	} catch (IllegalArgumentException e) {
	}
}


public void test_setCheckedZ() {
	assertEquals(false, treeItem.getChecked());
	
	treeItem.setChecked(true);
	assertEquals(false, treeItem.getChecked());

	Tree t = new Tree(shell, SWT.CHECK);
	TreeItem ti = new TreeItem(t, SWT.NULL);
	ti.setChecked(true);
	assertTrue(ti.getChecked());
	
	ti.setChecked(false);
	assertEquals(false, ti.getChecked());
	t.dispose();
}

public void test_setExpandedZ() {
	assertEquals(false, treeItem.getExpanded());
	
	// there must be at least one subitem before you can set the treeitem expanded
	treeItem.setExpanded(true);
	assertEquals(false, treeItem.getExpanded());


	new TreeItem(treeItem, SWT.NULL);
	treeItem.setExpanded(true);
	assertTrue(treeItem.getExpanded());
	treeItem.setExpanded(false);
	assertEquals(false, treeItem.getExpanded());
		
	TreeItem ti = new TreeItem(treeItem, SWT.NULL);
	ti.setExpanded(true);
	treeItem.setExpanded(false);
	assertEquals(false, ti.getExpanded());
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(treeItem.getDisplay(), 255, 0, 0);
	treeItem.setForeground(color);
	assertEquals(color, treeItem.getForeground());
	treeItem.setForeground(null);
	assertEquals(tree.getForeground(),treeItem.getForeground());
	color.dispose();
	try { 
		treeItem.setForeground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setGrayedZ() {
	warnUnimpl("Test test_setGrayedZ not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setTextLjava_lang_String() {
	try {
		treeItem.setText(null);		
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_TreeItem((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeItemI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_TreeItemII");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getChecked");
	methodNames.addElement("test_getExpanded");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getGrayed");
	methodNames.addElement("test_getItemCount");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getParentItem");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setCheckedZ");
	methodNames.addElement("test_setExpandedZ");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setGrayedZ");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeI")) test_ConstructorLorg_eclipse_swt_widgets_TreeI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeII")) test_ConstructorLorg_eclipse_swt_widgets_TreeII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeItemI")) test_ConstructorLorg_eclipse_swt_widgets_TreeItemI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_TreeItemII")) test_ConstructorLorg_eclipse_swt_widgets_TreeItemII();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getChecked")) test_getChecked();
	else if (getName().equals("test_getExpanded")) test_getExpanded();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getGrayed")) test_getGrayed();
	else if (getName().equals("test_getItemCount")) test_getItemCount();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getParentItem")) test_getParentItem();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setCheckedZ")) test_setCheckedZ();
	else if (getName().equals("test_setExpandedZ")) test_setExpandedZ();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setGrayedZ")) test_setGrayedZ();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}

/* custom */
TreeItem treeItem;
Tree tree;
}

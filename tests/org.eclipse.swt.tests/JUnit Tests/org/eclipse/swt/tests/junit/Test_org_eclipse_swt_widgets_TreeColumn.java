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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TreeColumn
 *
 * @see org.eclipse.swt.widgets.TreeColumn
 */
public class Test_org_eclipse_swt_widgets_TreeColumn extends Test_org_eclipse_swt_widgets_Item {

public Test_org_eclipse_swt_widgets_TreeColumn(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.SINGLE);		
	treeColumn = new TreeColumn(tree, SWT.NULL);	
	setWidget(treeColumn);
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeI() {
	try {
		new TreeColumn(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_TreeII() {
	try {
		new TreeColumn(null, SWT.NULL, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TreeColumn(tree, SWT.NULL, -1);
		fail("No exception thrown for index == -1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new TreeColumn(tree, SWT.NULL, 2);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	try {
		treeColumn.addSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getWidth() {
	int testWidth = 42;
	
//	try {
//		treeColumn.setWidth(-1);
//		assertTrue("No exception thrown", false);
//	}
//	catch (IllegalArgumentException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}
//	catch (SWTException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}

	treeColumn.setWidth(0);
	assertTrue(":a: width=" + treeColumn.getWidth() + " should be=" + 0, treeColumn.getWidth() == 0);

	treeColumn.setWidth(testWidth);
	assertTrue(":b: width=" + treeColumn.getWidth() + " should be=" + testWidth, treeColumn.getWidth() == testWidth);

	treeColumn.setWidth(testWidth);
	assertTrue(":c: width=" + treeColumn.getWidth() + " should be=" + testWidth, treeColumn.getWidth() == testWidth);
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	SelectionListener listener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {}};

	treeColumn.removeSelectionListener(listener);	
	treeColumn.addSelectionListener(listener);		
	treeColumn.removeSelectionListener(listener);	
	try {
		treeColumn.removeSelectionListener(null);		
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setAlignmentI() {
	TreeColumn column2;
	
	assertTrue(":a:", treeColumn.getAlignment() == SWT.LEFT);

	treeColumn.setAlignment(-1);
	assertTrue(":b:", treeColumn.getAlignment() == SWT.LEFT);

	treeColumn.setAlignment(SWT.RIGHT);
	assertTrue(
		":c: Should not be allowed to set alignment of the first column", 
		treeColumn.getAlignment() == SWT.LEFT);

	column2 = new TreeColumn(tree, SWT.NULL);
	column2.setAlignment(SWT.RIGHT);
	assertTrue(":d:", column2.getAlignment() == SWT.RIGHT);
	
	column2.setAlignment(SWT.CENTER);
	assertTrue(":e:", column2.getAlignment() == SWT.CENTER);	

	column2.setAlignment(SWT.LEFT);
	assertTrue(":f:", column2.getAlignment() == SWT.LEFT);	
}

@Override
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

public void test_setResizableZ() {
	assertTrue(":a:", treeColumn.getResizable() == true);

	treeColumn.setResizable(false);
	assertTrue(":b:", treeColumn.getResizable() == false);

	treeColumn.setResizable(false);
	assertTrue(":c:", treeColumn.getResizable() == false);

	treeColumn.setResizable(true);
	assertTrue(":d:", treeColumn.getResizable() == true);
}

@Override
public void test_setTextLjava_lang_String() {
	assertEquals(":a:", treeColumn.getText(), "");

	treeColumn.setText("text");
	assertEquals(":b:", treeColumn.getText(), "text");

	try {
		treeColumn.setText(null);
		fail("No exception thrown for column header == null");
	}
	catch (IllegalArgumentException e) {
	}
}

/* custom */
protected TreeColumn treeColumn;
protected Tree tree;
}

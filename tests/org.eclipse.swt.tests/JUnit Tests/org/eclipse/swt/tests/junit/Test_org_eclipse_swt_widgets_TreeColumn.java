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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TreeColumn
 *
 * @see org.eclipse.swt.widgets.TreeColumn
 */
public class Test_org_eclipse_swt_widgets_TreeColumn extends Test_org_eclipse_swt_widgets_Item {

@Override
@Before
public void setUp() {
	super.setUp();
	tree = new Tree(shell, SWT.SINGLE);
	treeColumn = new TreeColumn(tree, SWT.NULL);
	setWidget(treeColumn);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_TreeI() {
	try {
		new TreeColumn(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
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

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	try {
		treeColumn.addSelectionListener(null);
		fail("No exception thrown for selectionListener == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	treeColumn.addSelectionListener(listener);
	treeColumn.notifyListeners(SWT.Selection, new Event());
	assertTrue(listenerCalled);

	listenerCalled = false;
	treeColumn.removeSelectionListener(listener);
	treeColumn.notifyListeners(SWT.Selection, new Event());
	assertFalse(listenerCalled);
}

@Test
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
	assertEquals(":a: width=" + treeColumn.getWidth() + " should be=" + 0, 0, treeColumn.getWidth());

	treeColumn.setWidth(testWidth);
	assertEquals(":b: width=" + treeColumn.getWidth() + " should be=" + testWidth, testWidth, treeColumn.getWidth());

	treeColumn.setWidth(testWidth);
	assertEquals(":c: width=" + treeColumn.getWidth() + " should be=" + testWidth, testWidth, treeColumn.getWidth());
}

@Test
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

@Test
public void test_setAlignmentI() {
	TreeColumn column2;

	assertEquals(":a:", SWT.LEFT, treeColumn.getAlignment());

	treeColumn.setAlignment(-1);
	assertEquals(":b:", SWT.LEFT, treeColumn.getAlignment());

	treeColumn.setAlignment(SWT.RIGHT);
	assertEquals(
			":c: Should not be allowed to set alignment of the first column",
			SWT.LEFT, treeColumn.getAlignment());

	column2 = new TreeColumn(tree, SWT.NULL);
	column2.setAlignment(SWT.RIGHT);
	assertEquals(":d:", SWT.RIGHT, column2.getAlignment());

	column2.setAlignment(SWT.CENTER);
	assertEquals(":e:", SWT.CENTER, column2.getAlignment());

	column2.setAlignment(SWT.LEFT);
	assertEquals(":f:", SWT.LEFT, column2.getAlignment());
}

@Override
@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
}

@Test
public void test_setResizableZ() {
	assertTrue(":a:", treeColumn.getResizable());

	treeColumn.setResizable(false);
	assertFalse(":b:", treeColumn.getResizable());

	treeColumn.setResizable(false);
	assertFalse(":c:", treeColumn.getResizable());

	treeColumn.setResizable(true);
	assertTrue(":d:", treeColumn.getResizable());
}

@Override
@Test
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

@Test
public void test_ColumnOrder() {
	Tree tree = new Tree(shell, 0);
	List<TreeColumn> treeColumns = new ArrayList<>();
	try {
		for (;treeColumns.size()<12;) { // create
			int[] columnOrder = tree.getColumnOrder();
			assertEquals(treeColumns.size(), columnOrder.length);
			assertEquals(treeColumns.size(), tree.getColumnCount());
			for (int i = 0; i < treeColumns.size(); i++) {
				assertEquals(i, columnOrder[i]);
			}
			TreeColumn treeColumn = new TreeColumn(tree, SWT.NULL);
			treeColumns.add(treeColumn);
		}

		{ // reverse order
			int[] reversedColumnOrder = new int[treeColumns.size()];
			for (int i = 0; i < treeColumns.size(); i++) {
				reversedColumnOrder[i] = treeColumns.size() - i - 1;
			}
			tree.setColumnOrder(reversedColumnOrder);
			reversedColumnOrder = null;
			int[] columnOrder = tree.getColumnOrder();
			assertEquals(treeColumns.size(), columnOrder.length);
			assertEquals(treeColumns.size(), tree.getColumnCount());
			for (int i = 0; i < treeColumns.size(); i++) {
				assertEquals(treeColumns.size() - i - 1, columnOrder[i]);
			}
		}
		for (;!treeColumns.isEmpty();) { // remove
			TreeColumn treeColumn = treeColumns.get(treeColumns.size() / 2);
			treeColumn.dispose();
			treeColumns.remove(treeColumn);
			int[] columnOrder = tree.getColumnOrder();
			assertEquals(treeColumns.size(), columnOrder.length);
			assertEquals(treeColumns.size(), tree.getColumnCount());
			for (int i = 0; i < treeColumns.size(); i++) {
				// still reversed
				assertEquals(treeColumns.size() - i - 1, columnOrder[i]);
			}
		}
	} finally {
		treeColumns.forEach(TreeColumn::dispose);
		tree.dispose();
	}
}
/* custom */
protected TreeColumn treeColumn;
protected Tree tree;
}

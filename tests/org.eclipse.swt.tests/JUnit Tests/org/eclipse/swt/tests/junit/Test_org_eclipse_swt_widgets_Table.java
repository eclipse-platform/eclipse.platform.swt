/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Table
 *
 * @see org.eclipse.swt.widgets.Table
 */
public class Test_org_eclipse_swt_widgets_Table extends Test_org_eclipse_swt_widgets_Composite {

@Override
@Before
public void setUp() {
	super.setUp();
	makeCleanEnvironment(false); // by default, use multi-select table.
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new Table(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Override
@Test
public void test_computeSizeIIZ() {
}

@Test
public void test_deselect$I() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
	items[i] = new TableItem(table, 0);

	table.select(new int[] {0, 3});
	assertEquals(2, table.getSelectionCount());

	table.deselect(new int[] {1, 2});
	assertEquals(2, table.getSelectionCount());

	table.deselect(new int[] {1, 3, 5});
	assertEquals(1, table.getSelectionCount());

	table.deselect(new int[] {9, 3, 0});
	assertEquals(0, table.getSelectionCount());

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	try{
		table.deselect(null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {});
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {-1, 100, -1000});
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(new int[] {2, -1, 1, 100, 2});
	assertEquals(number-2, table.getSelectionCount());

	table.deselect(new int[] {2, -1, 1, 100, 2});
	assertEquals(number-2, table.getSelectionCount());

	table.deselect(new int[] {2, -1, 3, 100, 2});
	assertEquals(number-3, table.getSelectionCount());
}

@Test
public void test_deselectAll() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(0, table.getSelectionCount());
	table.select(new int[] {2, 4, 5, 10});

	assertEquals(4, table.getSelectionCount());

	table.deselectAll();
	assertEquals(0, table.getSelectionCount());

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	table.deselectAll();
	assertEquals(0, table.getSelectionCount());
}

@Test
public void test_deselectI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
	}
	assertEquals(0, table.getSelectionCount());

	table.deselect(0);
	assertEquals(0, table.getSelectionCount());

	table.select(new int[] {0, 3, 6});
	assertEquals(3, table.getSelectionCount());

	table.deselect(0);
	assertEquals(2, table.getSelectionCount());

	table.deselect(0);
	assertEquals(2, table.getSelectionCount());

	table.deselect(5);
	assertEquals(2, table.getSelectionCount());

	table.deselect(3);
	assertEquals(1, table.getSelectionCount());

	table.deselect(100);
	assertEquals(1, table.getSelectionCount());
}

@Test
public void test_deselectII() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(new int[] {0, 3, 6});
	assertEquals(3, table.getSelectionCount());

	table.deselect(7, 10);
	assertEquals(3, table.getSelectionCount());

	table.deselect(6, 10);
	assertEquals(2, table.getSelectionCount());

	table.deselect(1, 10);
	assertEquals(1, table.getSelectionCount());

	table.deselect(0, 10);
	assertEquals(0, table.getSelectionCount());

	table.deselect(0, 100);
	assertEquals(0, table.getSelectionCount());

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.selectAll();
	assertEquals(number, table.getSelectionCount());

	table.deselect(-10, 2);
	assertEquals(number-3, table.getSelectionCount());
	table.selectAll();

	table.deselect(-10, 2000);
	assertEquals(0, table.getSelectionCount());
	table.selectAll();

	table.deselect(2000, -10);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(0, number-1);
	assertEquals(0, table.getSelectionCount());
	table.selectAll();

	table.deselect(0, 0);
	assertEquals(number-1, table.getSelectionCount());
	table.selectAll();

	table.deselect(number-1, number-1);
	assertEquals(number-1, table.getSelectionCount());
	table.selectAll();
	table.deselect(-1, -1);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();

	table.deselect(number, number);
	assertEquals(number, table.getSelectionCount());
	table.selectAll();
}

@Test
public void test_getColumnCount() {
	assertEquals(0, table.getColumnCount());
	TableColumn column0 = new TableColumn(table, SWT.NONE);
	assertEquals(1, table.getColumnCount());
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	assertEquals(2, table.getColumnCount());
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	assertEquals(3, table.getColumnCount());
	column0.dispose();
	assertEquals(2, table.getColumnCount());
	column1.dispose();
	assertEquals(1, table.getColumnCount());
	column2.dispose();
	assertEquals(0, table.getColumnCount());
}

@Test
public void test_getColumnI() {
	try {
		table.getColumn(0);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	TableColumn column0 = new TableColumn(table, SWT.LEFT);
	try {
		table.getColumn(1);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	assertEquals(column0, table.getColumn(0));
	TableColumn column1 = new TableColumn(table, SWT.LEFT);
	assertEquals(column1, table.getColumn(1));
	column1.dispose();
	try {
		table.getColumn(1);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
	column0.dispose();
	try {
		table.getColumn(0);
		fail("No exception thrown for index out of range");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getColumns() {
	assertEquals(0, table.getColumns().length);
	TableColumn column0 = new TableColumn(table, SWT.LEFT);
	TableColumn[] columns = table.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column0, columns[0]);
	column0.dispose();
	assertEquals(0, table.getColumns().length);
	column0 = new TableColumn(table, SWT.LEFT);
	TableColumn column1 = new TableColumn(table, SWT.RIGHT, 1);
	columns = table.getColumns();
	assertEquals(2, columns.length);
	assertEquals(column0, columns[0]);
	assertEquals(column1, columns[1]);
	column0.dispose();
	columns = table.getColumns();
	assertEquals(1, columns.length);
	assertEquals(column1, columns[0]);
	column1.dispose();
	assertEquals(0, table.getColumns().length);
}

@Test
public void test_getGridLineWidth() {
	// execute method but there is no way to check the value since it may be anything including 0
	table.getGridLineWidth();
}

@Test
public void test_getHeaderHeight() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getHeaderHeight(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table))");
		}
		return;
	}
	assertEquals(0, table.getHeaderHeight());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderHeight() > 0);
	table.setHeaderVisible(false);
	assertEquals(0, table.getHeaderHeight());
}

@Test
public void test_getItemCount() {
	int[] cases = {0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals("j="+ j, cases[j], table.getItemCount());
		table.removeAll();
	}

	// note: SWT.SINGLE
	makeCleanEnvironment(true);
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals("j="+ j, cases[j], table.getItemCount());
		table.removeAll();
	}
}

@Test
public void test_getItemHeight() {
	assertTrue(":a: Item height <= 0", table.getItemHeight() > 0);
	new TableItem(table, 0);
	assertTrue(":b: Item height <= 0", table.getItemHeight() > 0);
}

@Test
public void test_getItemI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	for (int i = 0; i < number; i++)
		assertEquals("i=" + i, items[i], table.getItem(i));
	try {
		table.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		table.getItem(number+1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	for (int i = 0; i < number; i++) {
		assertEquals("i=" + i, items[i], table.getItem(i));
	}
	try {
		table.getItem(number);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		table.getItem(number+1);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_getItems() {
	int[] cases = {0, 10, 100};
	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals("j=" + j, cases[j], table.getItems().length);
		table.removeAll();
	}

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TableItem ti = new TableItem(table, 0);
			ti.setText(String.valueOf(i));
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			assertEquals("j=" + j + ", i=" + i, String.valueOf(i), items[i].getText());
		}
		table.removeAll();
	}

	// note SWT.SINGLE
	makeCleanEnvironment(true);

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			new TableItem(table, 0);
		}
		assertEquals("j=" + j, cases[j], table.getItems().length);
		table.removeAll();
	}

	makeCleanEnvironment(true);

	for (int j = 0; j < cases.length; j++) {
		for (int i = 0; i < cases[j]; i++) {
			TableItem ti = new TableItem(table, 0);
			ti.setText(String.valueOf(i));
		}
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			assertEquals("j=" + j + ", i=" + i, String.valueOf(i), items[i].getText());
		}
		table.removeAll();
	}
}

@Test
public void test_getSelection() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new TableItem[] {items[2], items[10], items[number-1]}, table.getSelection());

	table.setSelection(items);
	assertArrayEquals(items, table.getSelection());

	table.setSelection(items[0]);
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[10]});
	assertArrayEquals(new TableItem[] {items[10]}, table.getSelection());

	table.setSelection(new TableItem[]{items[number-1]});
	assertArrayEquals(new TableItem[] {items[number-1]}, table.getSelection());

	table.setSelection(new TableItem[]{items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());

	table.setSelection(new TableItem[]{items[10], items[number-1], items[2]});
	assertArrayEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(items);
	assertArrayEquals(new TableItem[] {}, table.getSelection());
}

@Test
public void test_getSelectionCount() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(3, table.getSelectionCount());

	table.setSelection(items[2]);
	assertEquals(1, table.getSelectionCount());

	table.setSelection(items);
	assertEquals(number, table.getSelectionCount());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(items);
	assertEquals(0, table.getSelectionCount());
}

@Test
public void test_getSelectionIndex() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(2, table.getSelectionIndex());

	table.setSelection(items[10]);
	assertEquals(10, table.getSelectionIndex());

	table.setSelection(items);
	assertEquals(0, table.getSelectionIndex());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2]});
	assertEquals(2, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(number - 1, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(10, table.getSelectionIndex());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(-1, table.getSelectionIndex());

	table.setSelection(items);
	assertEquals(-1, table.getSelectionIndex());
}

@Test
public void test_getSelectionIndices() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new int[]{2, 10, number-1}, table.getSelectionIndices()); // 10 < number

	int[] all = new int[number];
	for (int i = 0; i<number; i++)
		all[i]=i;
	table.setSelection(items);
	assertArrayEquals(all, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2]});
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[number-1]});
	assertArrayEquals(new int[]{number-1}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[10]});
	assertArrayEquals(new int[]{10}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(items);
	assertArrayEquals(new int[]{}, table.getSelectionIndices());
}

@Test
public void test_indexOfLorg_eclipse_swt_widgets_TableItem() {
	int number = 20;
	TableItem[] items = new TableItem[number];

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(i, table.indexOf(items[i]));
	}

	for (int i = 0; i < number; i++) {
		try {
			table.indexOf((TableItem)null);
			fail("No exception thrown for tableItem == null");
		}
		catch (IllegalArgumentException e) {
		}
	}

	// another table
	Table table_2 = new Table(shell, 0);
	TableItem[] items_2 = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items_2[i] = new TableItem(table_2, 0);
		items_2[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals("i=" + i, -1, table.indexOf(items_2[i]));
	}

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	number = 20;
	items = new TableItem[number];

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals(i, table.indexOf(items[i]));
	}

	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		try {
			table.indexOf((TableItem)null);
			fail("No exception thrown for tableItem == null");
		}
		catch (IllegalArgumentException e) {
		}
	}

	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++) {
		items[i] = new TableItem(table, 0);
		items[i].setText(String.valueOf(i));
	}

	// another table
	table_2 = new Table(shell, 0);
	items_2 = new TableItem[number];
	for (int i = 0; i < number; i++) {
		items_2[i] = new TableItem(table_2, 0);
		items_2[i].setText(String.valueOf(i));
	}

	for (int i = 0; i < number; i++) {
		assertEquals("i=" + i, -1, table.indexOf(items_2[i]));
	}
}

@Test
public void test_isSelectedI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	for (int i = 0; i < number; i++)
		assertTrue(":a:" + i, !table.isSelected(i));
	table.setSelection(new TableItem[] {items[2], items[number-1], items[10]});
	for (int i = 0; i < number; i++) {
		if (i == 2 || i == number-1 || i == 10)
			assertTrue(":b:" + i, table.isSelected(i));
		else
			assertTrue(":b:" + i, !table.isSelected(i));
	}

	table.setSelection(items[0]);
	for (int i = 0; i < number; i++) {
		if (i == 0)
			assertTrue(":b:" + i, table.isSelected(i));
		else
			assertTrue(":b:" + i, !table.isSelected(i));
	}


	table.setSelection(items);
	for (int i = 0; i < number; i++)
		assertTrue(":c:" + i, table.isSelected(i));

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	for (int i = 0; i < number; i++)
		assertTrue(":d:" + i, !table.isSelected(i));
	table.setSelection(new TableItem[] {items[10]});
	for (int i = 0; i < number; i++) {
		if (i == 10)
			assertTrue(":e:" + i, table.isSelected(i));
		else
			assertTrue(":e:" + i, !table.isSelected(i));
	}

	table.setSelection(items);
	for (int i = 0; i < number; i++){
		assertTrue(":f:" + i, !table.isSelected(i));
	}
}

@Test
public void test_remove$I() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(null);
		fail("No exception thrown for tableItems == null");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		table.remove(new int[] {2, 1, 0, -100, 5, 5, 2, 1, 0, 0, 0});
		fail("No exception thrown for illegal index arguments");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		table.remove(new int[] {2, 1, 0, number, 5, 5, 2, 1, 0, 0, 0});
		fail("No exception thrown for illegal index arguments");
	}
	catch (IllegalArgumentException e) {
	}

	table.remove(new int[] {});

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertTrue(":a:", !items[2].isDisposed());
	table.remove(new int[] {2});
	assertTrue(":b:", items[2].isDisposed());
	assertEquals(number-1, table.getItemCount());

	assertTrue(":c:", !items[number-1].isDisposed());
	table.remove(new int[] {number-2});
	assertTrue(":d:", items[number-1].isDisposed());
	assertEquals(number-2, table.getItemCount());

	assertTrue(":e:", !items[3].isDisposed());
	table.remove(new int[] {2});
	assertTrue(":f:", items[3].isDisposed());
	assertEquals(number-3, table.getItemCount());

	assertTrue(":g:", !items[0].isDisposed());
	table.remove(new int[] {0});
	assertTrue(":h:", items[0].isDisposed());
	assertEquals(number-4, table.getItemCount());
}

@Test
public void test_removeAll() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.removeAll();

	assertEquals(0, table.getItemCount());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.removeAll();
	table.removeAll();

	assertArrayEquals(new TableItem[] {}, table.getItems());
}

@Test
public void test_removeII() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(-number, number + 100);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(2, 3);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[4]}, table.getItems());
	// Make sure the removed items are disposed and other items are not.
	assertFalse(items[0].isDisposed());
	assertFalse(items[1].isDisposed());
	assertTrue(items[2].isDisposed());
	assertTrue(items[3].isDisposed());
	assertFalse(items[4].isDisposed());

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(2, 100);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(2, number);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(2, number-1);
	assertArrayEquals(new TableItem[] {items[0], items[1]}, table.getItems());
	for (int i = 0; i < 2; i++) {
		assertFalse(items[i].isDisposed());
	}
	for (int i = 2; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, 3);
	assertArrayEquals(new TableItem[] {items[4]}, table.getItems());
	for (int i = 0; i <= 3; i++) {
		assertTrue(items[i].isDisposed());
	}
	for (int i = 4; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, number-1);
	assertArrayEquals(new TableItem[] {}, table.getItems());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.remove(new int[] {});
	assertEquals(number, table.getItemCount());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(-20, -10);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(20, 40);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(200, 40);
	assertArrayEquals(items, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(2, 2);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[3], items[4]}, table.getItems());
	assertTrue(items[2].isDisposed());
	for (int i = 0; i < number && i != 2; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, 0);
	assertArrayEquals(new TableItem[]{items[1], items[2], items[3], items[4]}, table.getItems());
	assertTrue(items[0].isDisposed());
	for (int i = 1; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(4, 4);
	assertArrayEquals(new TableItem[]{items[0], items[1], items[2], items[3]}, table.getItems());
	assertTrue(items[4].isDisposed());
	for (int i = 0; i < number && i != 4; i++) {
		assertFalse(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	assertEquals(number, table.getItemCount());
	try {
		table.remove(-10, 2);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(number, table.getItemCount());
	if (SwtTestUtil.fCheckSWTPolicy) {
		table.remove(10, 2);
		assertEquals(number, table.getItemCount());
	}
	table.remove(0, 2);
	assertEquals(number - 3, table.getItemCount());
	assertArrayEquals(new TableItem[] {items[3], items[4]}, table.getItems());
	for (int i = 0; i < 2; i++) {
		assertTrue(items[i].isDisposed());
	}
	for (int i = 3; i < number; i++) {
		assertFalse(items[i].isDisposed());
	}
	try {
		table.remove(1, 200);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(number - 3, table.getItemCount());
	assertArrayEquals(new TableItem[] {items[3], items[4]}, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.remove(0, number-1);
	assertEquals(0, table.getItemCount());
	for (int i = 0; i <= number-1; i++) {
		assertTrue(items[i].isDisposed());
	}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(number, number);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(number, number + 100);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}

	makeCleanEnvironment(false);

	number = 15;
	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.remove(new int[] {2, 1, 0, 5, 5});
	assertEquals(number-4, table.getItemCount());
	assertTrue(items[0].isDisposed());
	assertTrue(items[1].isDisposed());
	assertTrue(items[2].isDisposed());
	assertFalse(items[3].isDisposed());
	assertFalse(items[4].isDisposed());
	assertTrue(items[5].isDisposed());
}

@Test
public void test_select$I() {
	try {
		table.select(null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(new int[] {2, 10, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {number, 0, number-1});
	assertArrayEquals(new int[] {0, number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {number, 0, -1});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(new int[] {10});
	assertArrayEquals(new int[] {0, 10}, table.getSelectionIndices());

	table.select(new int[] {2});
	assertArrayEquals(new int[] {0, 2, 10}, table.getSelectionIndices());

	table.select(new int[] {14});
	assertArrayEquals(new int[] {0, 2, 10, 14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {15});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {4, 4, 4});
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	try {
		table.select(null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(0, table.getSelectionCount());
	}

	table.select(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(new int[] {10});
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(new int[] {2});
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());

	table.select(new int[] {14});
	assertArrayEquals(new int[] {14}, table.getSelectionIndices());

	table.deselectAll();
	table.select(new int[] {15});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(new int[] {4, 4, 4});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_selectAll() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertArrayEquals(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertArrayEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertArrayEquals(new int[]{}, table.getSelectionIndices());
}

@Test
public void test_selectI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.select(7);
	assertArrayEquals(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertArrayEquals(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(0);
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(1);
	assertArrayEquals(new int[] {1}, table.getSelectionIndices());

	table.select(10);
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1);
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

}

@Test
public void test_selectII() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(new int[] {10, 2, 14});
	assertArrayEquals(new int[] {2, 10, 14}, table.getSelectionIndices());

	table.select(7);
	assertArrayEquals(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertArrayEquals(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	table.select(4, 10);
	assertArrayEquals(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 14}, table.getSelectionIndices());

	table.select(4, 14);
	assertArrayEquals(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(0, 7);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(9, 5);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(-100, 1000);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();
	assertEquals(0, table.getSelectionCount());
	table.select(0, 1000);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();

	table.select(0, number-1);
	assertEquals(number, table.getSelectionCount());
	table.deselectAll();

	table.select(0, 0);
	assertEquals(1, table.getSelectionCount());
	table.deselectAll();

	table.select(number-1, number);
	assertEquals(1, table.getSelectionCount());
	table.deselectAll();

	table.select(number, number);
	assertEquals(0, table.getSelectionCount());
	table.deselectAll();

	// note: SWT.SINGLE
	makeCleanEnvironment(true);
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(0, 0);
	assertEquals(1, table.getSelectionCount());
	assertEquals(0, table.getSelectionIndex());
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.select(4, 4);
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	table.select(10, 10);
	assertArrayEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1, number-1);
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number, number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(0, number-1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, number);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(4, 5);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(5, 4);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, -1);
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_setColumnOrder$I() {
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	table.setColumnOrder(new int[0]);
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	try {
		table.setColumnOrder(null);
		fail("No exception thrown for null argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[1]);
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}

	TableColumn column0 = new TableColumn(table, SWT.NONE);
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	TableColumn column2 = new TableColumn(table, SWT.NONE);
	assertArrayEquals(table.getColumnOrder(), new int[]{0, 1, 2});
	try {
		table.setColumnOrder(null);
		fail("No exception thrown for null argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[0]);
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[]{0,1});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[]{0, 1, 2, 3});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[]{0, 0, 1});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	try {
		table.setColumnOrder(new int[]{3, 0, 1});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	table.setColumnOrder(new int[]{2, 1, 0});
	assertArrayEquals(table.getColumnOrder(), new int[] {2, 1, 0});
	column2.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[] {1, 0});
	try {
		table.setColumnOrder(new int[]{0, 1, 2});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	column1.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[]{0});
	column0.dispose();
	assertArrayEquals(table.getColumnOrder(), new int[0]);
	try {
		table.setColumnOrder(new int[1]);
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	Table table2 = new Table(table.getParent(), SWT.NONE);
	table2.dispose();
	try {
		table2.getColumnOrder();
		fail("No exception thrown for widget is Disposed");
	} catch (SWTException ex) {}
	try {
		table2.setColumnOrder(new int[0]);
		fail("No exception thrown for widget is Disposed");
	} catch (SWTException ex) {}
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
}

@Test
public void test_setHeaderBackgroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(table.getHeaderBackground());
	Color color = new Color(12, 34, 56);
	table.setHeaderBackground(color);
	assertEquals(color, table.getHeaderBackground());
	table.setHeaderBackground(null);
	assertFalse(table.getHeaderBackground().equals(color));
	color.dispose();
}

@Test
public void test_setHeaderForegroundLorg_eclipse_swt_graphics_Color() {
	assertNotNull(table.getHeaderForeground());
	Color color = new Color(12, 34, 56);
	table.setHeaderForeground(color);
	assertEquals(color, table.getHeaderForeground());
	table.setHeaderForeground(null);
	assertFalse(table.getHeaderForeground().equals(color));
	color.dispose();
}

@Test
public void test_setHeaderVisibleZ() {
	assertFalse(table.getHeaderVisible());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderVisible());
	table.setHeaderVisible(false);
	assertFalse(table.getHeaderVisible());
}

@Test
public void test_setItemCountI() {
	table.removeAll();
	assertEquals(0, table.getItemCount());
	for (int i=0; i<8; i++) {
		new TableItem(table, SWT.NULL);
		assertEquals(i+1, table.getItemCount());
	}
	assertEquals(8, table.getItemCount());
	assertEquals(4, table.indexOf(table.getItems()[4]));
	table.getItem(1).dispose();
	assertEquals(7, table.getItemCount());
	new TableItem (table, SWT.NULL, 0);
	assertEquals(1, table.indexOf(table.getItems()[1]));
	assertEquals(8, table.getItemCount());
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	table.setItemCount(-1);
	assertEquals(0, table.getItemCount());
	table.setItemCount(10);
	assertEquals(10, table.getItemCount());
	table.getItem(1).dispose();
	assertEquals(9, table.getItemCount());
	assertEquals(4, table.indexOf(table.getItems()[4]));
	table.setItemCount(3);
	assertEquals(3, table.getItemCount());
	try {
		table.getItem(4);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
	table.setItemCount(40);
	assertEquals(40, table.getItemCount());
	table.getItem(39);
	table.setItemCount(0);
	assertEquals(0, table.getItemCount());
	try {
		table.getItem(39);
		fail("No exception thrown for illegal index argument");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setLinesVisibleZ() {
	assertFalse(table.getLinesVisible());
	table.setLinesVisible(true);
	assertTrue(table.getLinesVisible());
	table.setLinesVisible(false);
	assertFalse(table.getLinesVisible());
}

@Override
@Test
public void test_setRedrawZ() {
}

@Test
public void test_setSelection$I() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.setSelection((int[]) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	table.setSelection(new int[]{});
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new int[]{0, 3, 2});
	assertArrayEquals(new int[]{0, 2, 3}, table.getSelectionIndices());

	table.setSelection(new int[]{3, 2, 1});
	assertArrayEquals(new int[]{1, 2, 3}, table.getSelectionIndices());

	table.setSelection(new int[]{1, 4, 0});
	assertArrayEquals(new int[]{0, 1, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{0, 4, 0});
	assertArrayEquals(new int[]{0, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{2, 3, 4});
	assertArrayEquals(new int[]{2, 3, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{4, 4, 4, 4, 4, 4, 4});
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(new int[]{4});
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	try {
		table.setSelection((int[]) null);
		fail("No exception thrown for selection range == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(0, table.getSelectionCount());
	}

	table.setSelection(new int[] {});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0});
	assertArrayEquals(new int[] {0}, table.getSelectionIndices());

	table.setSelection(new int[] {2});
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());

	table.setSelection(new int[] {4});
	assertArrayEquals(new int[] {4}, table.getSelectionIndices());

	table.setSelection(new int[] {number-1});
	assertArrayEquals(new int[] {number-1}, table.getSelectionIndices());

	table.setSelection(new int[] {number});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {-1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0, 3, 2});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {3, 2, 1});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {4, 4, 4, 4, 4, 4, 4});
	assertArrayEquals(new int[] {}, table.getSelectionIndices());
}

@Test
public void test_setSelection$Lorg_eclipse_swt_widgets_TableItem() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.setSelection((TableItem[]) null);
		fail("No exception thrown for selection range == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(0, table.getSelectionCount());
	}

	try {
		table.setSelection((TableItem) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}
	finally {
		assertEquals(0, table.getSelectionCount());
	}

	table.setSelection(new TableItem[]{});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(items[0]);
	assertEquals(1, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TableItem[]{items[0], items[2], items[3]}, table.getSelection());
	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TableItem[]{items[1], items[2], items[3]}, table.getSelection());

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TableItem[]{items[0], items[1], items[4]}, table.getSelection());
	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TableItem[]{items[0], items[4]}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TableItem[]{items[2], items[3], items[4]}, table.getSelection());

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());
	table.setSelection(new TableItem[]{items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(new TableItem[] {items[0]});
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertArrayEquals(new TableItem[] {items[3]}, table.getSelection());

	table.setSelection(new TableItem[] {items[4]});
	assertArrayEquals(new TableItem[] {items[4]}, table.getSelection());
	table.setSelection(new TableItem[] {items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());
	table.setSelection(new TableItem[] {items[1]});
	assertArrayEquals(new TableItem[] {items[1]}, table.getSelection());

	// test single-selection table
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(new TableItem[]{});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[0], items[3], items[2]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[4]});
	assertArrayEquals(new TableItem[]{items[4]}, table.getSelection());

	table.setSelection(new TableItem[] {items[0]});
	assertArrayEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertArrayEquals(new TableItem[] {items[3]}, table.getSelection());

	table.setSelection(new TableItem[] {items[4]});
	assertArrayEquals(new TableItem[] {items[4]}, table.getSelection());

	table.setSelection(new TableItem[] {items[2]});
	assertArrayEquals(new TableItem[] {items[2]}, table.getSelection());

	table.setSelection(new TableItem[] {items[1]});
	assertArrayEquals(new TableItem[] {items[1]}, table.getSelection());

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertArrayEquals(new TableItem[]{}, table.getSelection());
}

@Test
public void test_setSelectionI() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0);
	assertArrayEquals(new int[]{0}, table.getSelectionIndices());
	table.setSelection(3);
	assertArrayEquals(new int[]{3}, table.getSelectionIndices());
	table.setSelection(4);
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1);
	assertArrayEquals(new int[]{1}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0);
	assertArrayEquals(new int[]{0}, table.getSelectionIndices());

	table.setSelection(3);
	assertArrayEquals(new int[]{3}, table.getSelectionIndices());

	table.setSelection(4);
	assertArrayEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1);
	assertArrayEquals(new int[]{1}, table.getSelectionIndices());

}

@Test
public void test_setSelectionII() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0, 1);
	assertArrayEquals(new int[]{0, 1}, table.getSelectionIndices());

	table.setSelection(2, 4);
	assertArrayEquals(new int[]{2, 3, 4}, table.getSelectionIndices());

	table.setSelection(3, 4);
	assertArrayEquals(new int[]{3, 4}, table.getSelectionIndices());

	table.setSelection(5, 4);
	assertArrayEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(2, 2);
	assertArrayEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(1, 4);
	assertArrayEquals(new int[]{1, 2, 3, 4}, table.getSelectionIndices());

	table.setSelection(0, 4);
	assertArrayEquals(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.setSelection(0, 1);
	if (SwtTestUtil.fCheckSWTPolicy)
		assertArrayEquals(new int[] {1}, table.getSelectionIndices());
	table.setSelection(2, 4);
	if (SwtTestUtil.fCheckSWTPolicy)
		assertArrayEquals(new int[] {4}, table.getSelectionIndices());
	table.setSelection(5, 4);
	if (SwtTestUtil.fCheckSWTPolicy)
		assertArrayEquals(new int[] {}, table.getSelectionIndices());
	table.setSelection(2, 2);
	assertArrayEquals(new int[] {2}, table.getSelectionIndices());
	table.setSelection(1, 4);
	if (SwtTestUtil.fCheckSWTPolicy)
		assertArrayEquals(new int[] {4}, table.getSelectionIndices());
	table.setSelection(0, 4);
	if (SwtTestUtil.fCheckSWTPolicy)
		assertArrayEquals(new int[] {4}, table.getSelectionIndices());
}

@Test
public void test_showSelection() {
	table.showSelection();
}

/* custom */
protected Table table;

private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
	if (table != null) table.dispose();
	table = new Table(shell, single?SWT.SINGLE:SWT.MULTI);
	setWidget(table);
}

private void createTable(List<String> events) {
	makeCleanEnvironment(false);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	for (int col = 0; col < 3; col++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Col " + col);
		column.setWidth(50);
		hookExpectedEvents(column, getTestName(), events);
	}
	for (int row = 0; row < 3; row++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		hookExpectedEvents(item, getTestName(), events);
	}
}

@Test
public void test_consistency_KeySelection() {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MouseSelection() {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyPrePackShell();
	consistencyEvent(20, table.getHeaderHeight() + table.getItemHeight()*2,
					 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DoubleClick () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyPrePackShell();
	consistencyEvent(20, table.getHeaderHeight()+ table.getItemHeight() + 5, 1, 0,
					 ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

@Test
public void test_consistency_EnterSelection () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

@Test
public void test_consistency_MenuDetect () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(20, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

@Test
public void test_consistency_DragDetect () {
	List<String> events = new ArrayList<>();
	createTable(events);
	consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

@Test
public void test_Virtual() {
	table.dispose();
	table = new Table(shell, SWT.VIRTUAL | SWT.BORDER);
	setWidget(table);

	int count = 1_000_000;
	int visibleCount = 10;

	shell.setLayout(new FillLayout());
	final int[] dataCounter = { 0 };
	table.addListener(SWT.SetData, event -> {
		TableItem item = (TableItem) event.item;
		int index = table.indexOf(item);
		item.setText("Item " + index);
		dataCounter[0]++;
	});

	shell.setSize (200, table.getItemHeight() * visibleCount);
	shell.open ();
	table.setItemCount(count);

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
	// the "* 2" allows some surplus for platforms that pre-fetch items to improve scrolling performance:
	assertTrue("SetData callback count not in range: " + dataCounter[0],
			dataCounter[0] > visibleCount / 2 && dataCounter[0] <= visibleCount * 2);
}

@Test
public void test_setTopIndex() {
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	table.setSize(50,50);
	shell.open();
	table.setTopIndex(5);
	for (int i = 0; i < 10; i++) {
		new TableItem(table, 0);
	}
	shell.setVisible(false);
	assertEquals(5, table.getTopIndex());
}
}
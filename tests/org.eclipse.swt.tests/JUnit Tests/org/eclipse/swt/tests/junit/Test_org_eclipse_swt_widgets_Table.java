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
 * Automated Test Suite for class org.eclipse.swt.widgets.Table
 *
 * @see org.eclipse.swt.widgets.Table
 */
public class Test_org_eclipse_swt_widgets_Table extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_widgets_Table(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	makeCleanEnvironment(false); // by default, use multi-select table.	
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new Table(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

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

public void test_getColumnOrder() {
	//in test_setAColumnOrder$I
}

public void test_getGridLineWidth() {
	// execute method but there is no way to check the value since it may be anything including 0
	table.getGridLineWidth();
}

public void test_getHeaderHeight() {
	assertEquals(0, table.getHeaderHeight());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderHeight() > 0);
	table.setHeaderVisible(false);
	assertEquals(0, table.getHeaderHeight());
}

public void test_getHeaderVisible() {
	// tested in test_setHeaderVisibleZ
}

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

public void test_getItemHeight() {
	assertTrue(":a: Item height <= 0", table.getItemHeight() > 0);
	new TableItem(table, 0);
	assertTrue(":b: Item height <= 0", table.getItemHeight() > 0);
}

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

public void test_getItemLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_getItemLorg_eclipse_swt_graphics_Point not written");
}

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

public void test_getLinesVisible() {
	// tested in test_setLinesVisibleZ
}

public void test_getSelection() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertSame(new TableItem[] {items[2], items[10], items[number-1]}, table.getSelection());
	
	table.setSelection(items);
	assertSame(items, table.getSelection());
	
	table.setSelection(items[0]);
	assertSame(new TableItem[] {items[0]}, table.getSelection());
	
	// note: SWT.SINGLE
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new TableItem[] {}, table.getSelection());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(new TableItem[] {items[10]}, table.getSelection());
	
	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(new TableItem[] {items[number-1]}, table.getSelection());
	
	table.setSelection(new TableItem[]{items[2]});
	assertEquals(new TableItem[] {items[2]}, table.getSelection());
	
	table.setSelection(new TableItem[]{items[10], items[number-1], items[2]});
	assertEquals(new TableItem[] {}, table.getSelection());
	
	table.setSelection(items);
	assertEquals(new TableItem[] {}, table.getSelection());
}

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

public void test_getSelectionIndices() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new int[]{}, table.getSelectionIndices());
	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertSame(new int[]{2, 10, number-1}, table.getSelectionIndices()); // 10 < number

	int[] all = new int[number];
	for (int i = 0; i<number; i++)
		all[i]=i;
	table.setSelection(items);
	assertSame(all, table.getSelectionIndices());

	// note: SWT.SINGLE
	makeCleanEnvironment(true);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2]});
	assertEquals(new int[]{2}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[number-1]});
	assertEquals(new int[]{number-1}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[10]});
	assertEquals(new int[]{10}, table.getSelectionIndices());

	table.setSelection(new TableItem[]{items[2], items[number-1], items[10]});
	assertEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(items);
	assertEquals(new int[]{}, table.getSelectionIndices());
}

public void test_getTopIndex() {
	warnUnimpl("Test test_getTopIndex not written");
}

public void test_indexOfLorg_eclipse_swt_widgets_TableColumn() {
	warnUnimpl("Test test_indexOfLorg_eclipse_swt_widgets_TableColumn not written");
}

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

public void test_removeAll() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.removeAll();

	makeCleanEnvironment(false);
		
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.removeAll();
	table.removeAll();
}

public void test_removeI() {
	warnUnimpl("Test test_removeI not written");
}

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
	assertEquals(new TableItem[]{items[0], items[1], items[4]}, table.getItems());
	
	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(2, 100);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(items, table.getItems());
	
	makeCleanEnvironment(false);

	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(2, number);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(items, table.getItems());
	
	makeCleanEnvironment(false);
	
	items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(2, number-1);
	assertEquals(new TableItem[] {items[0], items[1]}, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, 3);
	assertEquals(new TableItem[] {items[4]}, table.getItems());

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, number-1);
	assertEquals(new TableItem[] {}, table.getItems());
	
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
	assertEquals(items, table.getItems());
	
	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	try {
		table.remove(20, 40);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(items, table.getItems());
	
	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(200, 40);
	assertEquals(items, table.getItems());

	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(2, 2);
	assertEquals(new TableItem[]{items[0], items[1], items[3], items[4]}, table.getItems());

	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(0, 0);
	assertEquals(new TableItem[]{items[1], items[2], items[3], items[4]}, table.getItems());

	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.remove(4, 4);
	assertEquals(new TableItem[]{items[0], items[1], items[2], items[3]}, table.getItems());
	
	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	assertEquals(number, table.getItemCount());
	try {
		table.remove(-10, 2);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(number, table.getItemCount());
	if (fCheckSWTPolicy) {
		table.remove(10, 2);
		assertEquals(number, table.getItemCount());
	}
	table.remove(0, 2);
	assertEquals(number - 3, table.getItemCount());
	assertEquals(new TableItem[] {items[3], items[4]}, table.getItems());
	try {
		table.remove(1, 200);
		fail("No exception thrown for illegal index range");
	} catch (IllegalArgumentException e) {}
	assertEquals(number - 3, table.getItemCount());
	assertEquals(new TableItem[] {items[3], items[4]}, table.getItems());
	
	makeCleanEnvironment(false);
	
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.remove(0, number-1);
	assertEquals(0, table.getItemCount());

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
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	warnUnimpl("Test test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener not written");
}

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
	assertSame(new int[] {2, 10, 14}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {10, 2, 14});
	assertSame(new int[] {2, 10, 14}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {number, 0, number-1});
	assertSame(new int[] {0, number-1}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {number, 0, -1});
	assertSame(new int[] {0}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {0});
	assertSame(new int[] {0}, table.getSelectionIndices());
	
	table.select(new int[] {10});
	assertSame(new int[] {0, 10}, table.getSelectionIndices());
	
	table.select(new int[] {2});
	assertSame(new int[] {0, 2, 10}, table.getSelectionIndices());
	
	table.select(new int[] {14});
	assertSame(new int[] {0, 2, 10, 14}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {15});
	assertSame(new int[] {}, table.getSelectionIndices());
	
	table.select(new int[] {-1});
	assertSame(new int[] {}, table.getSelectionIndices());
	
	table.select(new int[] {4, 4, 4});
	assertSame(new int[] {4}, table.getSelectionIndices());

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
	assertSame(new int[] {0}, table.getSelectionIndices());
	
	table.select(new int[] {10});
	assertSame(new int[] {10}, table.getSelectionIndices());
	
	table.select(new int[] {2});
	assertSame(new int[] {2}, table.getSelectionIndices());
	
	table.select(new int[] {14});
	assertSame(new int[] {14}, table.getSelectionIndices());
	
	table.deselectAll();
	table.select(new int[] {15});
	assertSame(new int[] {}, table.getSelectionIndices());
	
	table.select(new int[] {-1});
	assertSame(new int[] {}, table.getSelectionIndices());
	
	table.select(new int[] {10, 2, 14});
	assertSame(new int[] {}, table.getSelectionIndices());
	
	table.select(new int[] {4, 4, 4});
	assertSame(new int[] {}, table.getSelectionIndices());
}

public void test_selectAll() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertSame(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());
	
	// test single-selection table
	makeCleanEnvironment(true);
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	assertEquals(new int[]{}, table.getSelectionIndices());
	table.selectAll();
	assertEquals(new int[]{}, table.getSelectionIndices());
}

public void test_selectI() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	
	table.select(new int[] {10, 2, 14});
	assertSame(new int[] {2, 10, 14}, table.getSelectionIndices());
	
	table.select(7);
	assertSame(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertSame(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	// note: SWT.SINGLE	
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.select(0);
	assertEquals(new int[] {0}, table.getSelectionIndices());

	table.select(1);
	assertEquals(new int[] {1}, table.getSelectionIndices());

	table.select(10);
	assertEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1);
	assertEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1);
	assertEquals(new int[] {}, table.getSelectionIndices());

}

public void test_selectII() {
	int number = 15;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	
	table.select(new int[] {10, 2, 14});
	assertSame(new int[] {2, 10, 14}, table.getSelectionIndices());
	
	table.select(7);
	assertSame(new int[]{2, 7, 10, 14}, table.getSelectionIndices());

	table.select(0);
	assertSame(new int[]{0, 2, 7, 10, 14}, table.getSelectionIndices());

	table.select(4, 10);
	assertSame(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 14}, table.getSelectionIndices());

	table.select(4, 14);
	assertSame(new int[]{0, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(0, 7);
	assertSame(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

	table.select(9, 5);
	assertSame(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, table.getSelectionIndices());

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
	assertEquals(new int[] {0}, table.getSelectionIndices());

	table.select(4, 4);
	assertEquals(new int[] {4}, table.getSelectionIndices());

	table.select(10, 10);
	assertEquals(new int[] {10}, table.getSelectionIndices());

	table.select(number-1, number-1);
	assertEquals(new int[] {number-1}, table.getSelectionIndices());

	table.deselectAll();
	table.select(number, number);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(0, number-1);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, number);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(4, 5);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(5, 4);
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.select(-1, -1);
	assertEquals(new int[] {}, table.getSelectionIndices());
}

public void test_setColumnOrder$I() {
	assertEquals(table.getColumnOrder(), new int[0]);
	table.setColumnOrder(new int[0]);
	assertEquals(table.getColumnOrder(), new int[0]);
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
	assertEquals(table.getColumnOrder(), new int[]{0, 1, 2});
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
	assertEquals(table.getColumnOrder(), new int[] {2, 1, 0});
	column2.dispose();
	assertEquals(table.getColumnOrder(), new int[] {1, 0});
	try {
		table.setColumnOrder(new int[]{0, 1, 2});
		fail("No exception thrown for invalid argument");
	} catch (IllegalArgumentException ex) {}
	column1.dispose();
	assertEquals(table.getColumnOrder(), new int[]{0});
	column0.dispose();
	assertEquals(table.getColumnOrder(), new int[0]);
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

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setHeaderVisibleZ() {
	assertFalse(table.getHeaderVisible());
	table.setHeaderVisible(true);
	assertTrue(table.getHeaderVisible());
	table.setHeaderVisible(false);
	assertFalse(table.getHeaderVisible());
}

public void test_setLinesVisibleZ() {
	if (SwtJunit.isCarbon) {
		/* only carbon versions >= 10.4 support Table lines */
		assertFalse(table.getLinesVisible());
		return;
	}
	assertFalse(table.getLinesVisible());
	table.setLinesVisible(true);
	assertTrue(table.getLinesVisible());
	table.setLinesVisible(false);
	assertFalse(table.getLinesVisible());
}

public void test_setRedrawZ() {
	warnUnimpl("Test test_setRedrawZ not written");
}

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
	assertEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(new int[]{0, 3, 2});
	assertSame(new int[]{0, 2, 3}, table.getSelectionIndices());	

	table.setSelection(new int[]{3, 2, 1});
	assertSame(new int[]{1, 2, 3}, table.getSelectionIndices());

	table.setSelection(new int[]{1, 4, 0});
	assertSame(new int[]{0, 1, 4}, table.getSelectionIndices());
	
	table.setSelection(new int[]{0, 4, 0});
	assertSame(new int[]{0, 4}, table.getSelectionIndices());	

	table.setSelection(new int[]{2, 3, 4});
	assertSame(new int[]{2, 3, 4}, table.getSelectionIndices());

	table.setSelection(new int[]{4, 4, 4, 4, 4, 4, 4});
	assertEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(new int[]{4});
	assertEquals(new int[]{4}, table.getSelectionIndices());
	
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
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0});
	assertEquals(new int[] {0}, table.getSelectionIndices());

	table.setSelection(new int[] {2});
	assertEquals(new int[] {2}, table.getSelectionIndices());

	table.setSelection(new int[] {4});
	assertEquals(new int[] {4}, table.getSelectionIndices());

	table.setSelection(new int[] {number-1});
	assertEquals(new int[] {number-1}, table.getSelectionIndices());

	table.setSelection(new int[] {number});
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {-1});
	assertEquals(new int[] {}, table.getSelectionIndices());

	table.setSelection(new int[] {0, 3, 2});
	assertEquals(new int[] {}, table.getSelectionIndices());
	
	table.setSelection(new int[] {3, 2, 1});
	assertEquals(new int[] {}, table.getSelectionIndices());
	
	table.setSelection(new int[] {4, 4, 4, 4, 4, 4, 4});
	assertEquals(new int[] {}, table.getSelectionIndices());
}

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
	assertSame(new TableItem[]{items[0], items[2], items[3]}, table.getSelection());	
	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertSame(new TableItem[]{items[1], items[2], items[3]}, table.getSelection());	

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertSame(new TableItem[]{items[0], items[1], items[4]}, table.getSelection());	
	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertSame(new TableItem[]{items[0], items[4]}, table.getSelection());	

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertSame(new TableItem[]{items[2], items[3], items[4]}, table.getSelection());	

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(new TableItem[]{items[4]}, table.getSelection());	
	table.setSelection(new TableItem[]{items[4]});
	assertEquals(new TableItem[]{items[4]}, table.getSelection());	

	makeCleanEnvironment(false);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(new TableItem[] {items[0]});
	assertEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertEquals(new TableItem[] {items[3]}, table.getSelection());	

	table.setSelection(new TableItem[] {items[4]});
	assertEquals(new TableItem[] {items[4]}, table.getSelection());
	table.setSelection(new TableItem[] {items[2]});
	assertEquals(new TableItem[] {items[2]}, table.getSelection());	
	table.setSelection(new TableItem[] {items[1]});
	assertEquals(new TableItem[] {items[1]}, table.getSelection());

	// test single-selection table
	makeCleanEnvironment(true);

	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(new TableItem[]{});
	assertEquals(0, table.getSelectionCount());

	table.setSelection(new TableItem[]{items[0], items[3], items[2]});
	assertEquals(new TableItem[]{}, table.getSelection());	

	table.setSelection(new TableItem[]{items[3], items[2], items[1]});
	assertEquals(new TableItem[]{}, table.getSelection());	

	table.setSelection(new TableItem[]{items[1], items[4], items[0]});
	assertEquals(new TableItem[]{}, table.getSelection());	

	table.setSelection(new TableItem[]{items[0], items[4], items[0]});
	assertEquals(new TableItem[]{}, table.getSelection());

	table.setSelection(new TableItem[]{items[2], items[3], items[4]});
	assertEquals(new TableItem[]{}, table.getSelection());	

	table.setSelection(new TableItem[]{items[4]});
	assertEquals(new TableItem[]{items[4]}, table.getSelection());
	
	table.setSelection(new TableItem[] {items[0]});
	assertEquals(new TableItem[] {items[0]}, table.getSelection());

	table.setSelection(new TableItem[] {items[3]});
	assertEquals(new TableItem[] {items[3]}, table.getSelection());	

	table.setSelection(new TableItem[] {items[4]});
	assertEquals(new TableItem[] {items[4]}, table.getSelection());

	table.setSelection(new TableItem[] {items[2]});
	assertEquals(new TableItem[] {items[2]}, table.getSelection());	

	table.setSelection(new TableItem[] {items[1]});
	assertEquals(new TableItem[] {items[1]}, table.getSelection());			

	table.setSelection(new TableItem[]{items[4], items[4], items[4], items[4], items[4], items[4]});
	assertEquals(new TableItem[]{}, table.getSelection());	
}

public void test_setSelectionI() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0);
	assertEquals(new int[]{0}, table.getSelectionIndices());
	table.setSelection(3);
	assertEquals(new int[]{3}, table.getSelectionIndices());	
	table.setSelection(4);
	assertEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertEquals(new int[]{2}, table.getSelectionIndices());	

	table.setSelection(1);
	assertEquals(new int[]{1}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);
		
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0);
	assertEquals(new int[]{0}, table.getSelectionIndices());

	table.setSelection(3);
	assertEquals(new int[]{3}, table.getSelectionIndices());	

	table.setSelection(4);
	assertEquals(new int[]{4}, table.getSelectionIndices());

	table.setSelection(2);
	assertEquals(new int[]{2}, table.getSelectionIndices());	

	table.setSelection(1);
	assertEquals(new int[]{1}, table.getSelectionIndices());	
	
}

public void test_setSelectionII() {
	int number = 5;
	TableItem[] items = new TableItem[number];
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);

	table.setSelection(0, 1);
	assertSame(new int[]{0, 1}, table.getSelectionIndices());

	table.setSelection(2, 4);
	assertSame(new int[]{2, 3, 4}, table.getSelectionIndices());	

	table.setSelection(3, 4);
	assertSame(new int[]{3, 4}, table.getSelectionIndices());	

	table.setSelection(5, 4);
	assertEquals(new int[]{}, table.getSelectionIndices());

	table.setSelection(2, 2);
	assertEquals(new int[]{2}, table.getSelectionIndices());	

	table.setSelection(1, 4);
	assertSame(new int[]{1, 2, 3, 4}, table.getSelectionIndices());

	table.setSelection(0, 4);
	assertSame(new int[]{0, 1, 2, 3, 4}, table.getSelectionIndices());

	// test single-selection table
	makeCleanEnvironment(true);
		
	for (int i = 0; i < number; i++)
		items[i] = new TableItem(table, 0);
	table.setSelection(0, 1);
	if (fCheckSWTPolicy)
		assertEquals(new int[] {1}, table.getSelectionIndices());
	table.setSelection(2, 4);
	if (fCheckSWTPolicy)
		assertEquals(new int[] {4}, table.getSelectionIndices());
	table.setSelection(5, 4);
	if (fCheckSWTPolicy)
		assertEquals(new int[] {}, table.getSelectionIndices());
	table.setSelection(2, 2);
	assertEquals(new int[] {2}, table.getSelectionIndices());
	table.setSelection(1, 4);
	if (fCheckSWTPolicy)
		assertEquals(new int[] {4}, table.getSelectionIndices());
	table.setSelection(0, 4);
	if (fCheckSWTPolicy)
		assertEquals(new int[] {4}, table.getSelectionIndices());
}

public void test_setTopIndexI() {
	warnUnimpl("Test test_setTopIndexI not written");
}

public void test_showColumnLorg_eclipse_swt_widgets_TableColumn() {
	warnUnimpl("Test test_showColumnLorg_eclipse_swt_widgets_TableColumn not written");
}

public void test_showItemLorg_eclipse_swt_widgets_TableItem() {
	warnUnimpl("Test test_showItemLorg_eclipse_swt_widgets_TableItem not written");
}

public void test_showSelection() {
	table.showSelection();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Table((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_deselect$I");
	methodNames.addElement("test_deselectAll");
	methodNames.addElement("test_deselectI");
	methodNames.addElement("test_deselectII");
	methodNames.addElement("test_getColumnCount");
	methodNames.addElement("test_getColumnI");
	methodNames.addElement("test_getColumnOrder");
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
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getSelectionIndex");
	methodNames.addElement("test_getSelectionIndices");
	methodNames.addElement("test_getTopIndex");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_TableColumn");
	methodNames.addElement("test_indexOfLorg_eclipse_swt_widgets_TableItem");
	methodNames.addElement("test_isSelectedI");
	methodNames.addElement("test_remove$I");
	methodNames.addElement("test_removeAll");
	methodNames.addElement("test_removeI");
	methodNames.addElement("test_removeII");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_select$I");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_selectI");
	methodNames.addElement("test_selectII");
	methodNames.addElement("test_setColumnOrder$I");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setHeaderVisibleZ");
	methodNames.addElement("test_setLinesVisibleZ");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSelection$I");
	methodNames.addElement("test_setSelection$Lorg_eclipse_swt_widgets_TableItem");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setSelectionII");
	methodNames.addElement("test_setTopIndexI");
	methodNames.addElement("test_showColumnLorg_eclipse_swt_widgets_TableColumn");
	methodNames.addElement("test_showItemLorg_eclipse_swt_widgets_TableItem");
	methodNames.addElement("test_showSelection");
	methodNames.addElement("test_consistency_KeySelection");
	methodNames.addElement("test_consistency_MouseSelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_DoubleClick");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_deselect$I")) test_deselect$I();
	else if (getName().equals("test_deselectAll")) test_deselectAll();
	else if (getName().equals("test_deselectI")) test_deselectI();
	else if (getName().equals("test_deselectII")) test_deselectII();
	else if (getName().equals("test_getColumnCount")) test_getColumnCount();
	else if (getName().equals("test_getColumnI")) test_getColumnI();
	else if (getName().equals("test_getColumnOrder")) test_getColumnOrder();
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
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getSelectionIndex")) test_getSelectionIndex();
	else if (getName().equals("test_getSelectionIndices")) test_getSelectionIndices();
	else if (getName().equals("test_getTopIndex")) test_getTopIndex();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_TableColumn")) test_indexOfLorg_eclipse_swt_widgets_TableColumn();
	else if (getName().equals("test_indexOfLorg_eclipse_swt_widgets_TableItem")) test_indexOfLorg_eclipse_swt_widgets_TableItem();
	else if (getName().equals("test_isSelectedI")) test_isSelectedI();
	else if (getName().equals("test_remove$I")) test_remove$I();
	else if (getName().equals("test_removeAll")) test_removeAll();
	else if (getName().equals("test_removeI")) test_removeI();
	else if (getName().equals("test_removeII")) test_removeII();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_select$I")) test_select$I();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_selectI")) test_selectI();
	else if (getName().equals("test_selectII")) test_selectII();
	else if (getName().equals("test_setColumnOrder$I")) test_setColumnOrder$I();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setHeaderVisibleZ")) test_setHeaderVisibleZ();
	else if (getName().equals("test_setLinesVisibleZ")) test_setLinesVisibleZ();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSelection$I")) test_setSelection$I();
	else if (getName().equals("test_setSelection$Lorg_eclipse_swt_widgets_TableItem")) test_setSelection$Lorg_eclipse_swt_widgets_TableItem();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setSelectionII")) test_setSelectionII();
	else if (getName().equals("test_setTopIndexI")) test_setTopIndexI();
	else if (getName().equals("test_showColumnLorg_eclipse_swt_widgets_TableColumn")) test_showColumnLorg_eclipse_swt_widgets_TableColumn();
	else if (getName().equals("test_showItemLorg_eclipse_swt_widgets_TableItem")) test_showItemLorg_eclipse_swt_widgets_TableItem();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else if (getName().equals("test_consistency_KeySelection")) test_consistency_KeySelection();
	else if (getName().equals("test_consistency_MouseSelection")) test_consistency_MouseSelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_DoubleClick")) test_consistency_DoubleClick();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
protected Table table;

private void makeCleanEnvironment(boolean single) {
// this method must be private or protected so the auto-gen tool keeps it
	if (table != null) table.dispose();
	table = new Table(shell, single?SWT.SINGLE:SWT.MULTI);
	setWidget(table);	
}

private void createTable(Vector events) {
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

public void test_consistency_KeySelection() {
    Vector events = new Vector();
    createTable(events);
    consistencyEvent(0, SWT.ARROW_DOWN, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MouseSelection() {
    Vector events = new Vector();
    createTable(events);
    consistencyPrePackShell();
    consistencyEvent(20, table.getHeaderHeight() + table.getItemHeight()*2, 
            		 1, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DoubleClick () {
    Vector events = new Vector();
    createTable(events);
    consistencyPrePackShell();
    consistencyEvent(20, table.getHeaderHeight()+ table.getItemHeight() + 5, 1, 0, 
            	     ConsistencyUtility.MOUSE_DOUBLECLICK, events);
}

public void test_consistency_EnterSelection () {
    Vector events = new Vector();
    createTable(events);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS, events);
}

public void test_consistency_MenuDetect () {
    Vector events = new Vector();
    createTable(events);
    consistencyEvent(20, 25, 3, 0, ConsistencyUtility.MOUSE_CLICK, events);
}

public void test_consistency_DragDetect () {
    Vector events = new Vector();
    createTable(events);
    consistencyEvent(30, 20, 50, 30, ConsistencyUtility.MOUSE_DRAG, events);
}

}

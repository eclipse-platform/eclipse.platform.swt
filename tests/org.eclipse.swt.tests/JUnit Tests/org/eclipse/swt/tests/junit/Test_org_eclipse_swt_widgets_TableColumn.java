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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.TableColumn
 *
 * @see org.eclipse.swt.widgets.TableColumn
 */
public class Test_org_eclipse_swt_widgets_TableColumn extends Test_org_eclipse_swt_widgets_Item {

	@Override
	@Before
	public void setUp() {
		super.setUp();
		table = new Table(shell, SWT.SINGLE);
		tableColumn = new TableColumn(table, SWT.NULL);
		setWidget(tableColumn);
	}

	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_TableI() {
		try {
			new TableColumn(null, SWT.NULL);
			fail("No exception thrown for parent == null");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_ConstructorLorg_eclipse_swt_widgets_TableII() {
		try {
			new TableColumn(null, SWT.NULL, 0);
			fail("No exception thrown for parent == null");
		} catch (IllegalArgumentException e) {
		}

		try {
			new TableColumn(table, SWT.NULL, -1);
			fail("No exception thrown for index == -1");
		} catch (IllegalArgumentException e) {
		}

		try {
			new TableColumn(table, SWT.NULL, 2);
			fail("No exception thrown for illegal index argument");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
		try {
			tableColumn.addSelectionListener(null);
			fail("No exception thrown for selectionListener == null");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_setHorizontalBarSelection() {

		for (int i = 0; i < 1; i++) {
			new TableItem(table, SWT.None);
		}

		for (int i = 0; i < 10; i++) {
			var c = new TableColumn(table, SWT.None);

			c.setWidth(200);
		}

		table.setSize(50, 30);

		table.getHorizontalBar().setSelection(300);
		table.getHorizontalBar().notifyListeners(SWT.Selection, new Event());

		var its = table.getItems();

		assertTrue(its[0].getBounds().x < 0);
	}

	@Test
	public void test_addSelectionListenerWidgetSelectedListenerLorg_eclipse_swt_events_SelectionListener() {
		listenerCalled = false;
		SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

		tableColumn.addSelectionListener(listener);
		tableColumn.notifyListeners(SWT.Selection, new Event());
		assertTrue(listenerCalled);

		listenerCalled = false;
		tableColumn.removeSelectionListener(listener);
		tableColumn.notifyListeners(SWT.Selection, new Event());
		assertFalse(listenerCalled);
	}

	@Test
	public void test_getWidth() {
		int testWidth = 42;

//	try {
//		tableColumn.setWidth(-1);
//		assertTrue("No exception thrown", false);
//	}
//	catch (IllegalArgumentException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}
//	catch (SWTException e) {
//		assertTrue("Wrong error thrown: " + e, e.getMessage().equals("Argument not valid"));
//	}

		tableColumn.setWidth(0);
		assertEquals(":a: width=" + tableColumn.getWidth() + " should be=" + 0, 0, tableColumn.getWidth());

		tableColumn.setWidth(testWidth);
		assertEquals(":b: width=" + tableColumn.getWidth() + " should be=" + testWidth, testWidth,
				tableColumn.getWidth());

		tableColumn.setWidth(testWidth);
		assertEquals(":c: width=" + tableColumn.getWidth() + " should be=" + testWidth, testWidth,
				tableColumn.getWidth());
	}

	@Test
	public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		};

		tableColumn.removeSelectionListener(listener);
		tableColumn.addSelectionListener(listener);
		tableColumn.removeSelectionListener(listener);
		try {
			tableColumn.removeSelectionListener(null);
			fail("No exception thrown for selectionListener == null");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void test_setAlignmentI() {
		TableColumn column2;

		assertEquals(":a:", SWT.LEFT, tableColumn.getAlignment());

		tableColumn.setAlignment(-1);
		assertEquals(":b:", SWT.LEFT, tableColumn.getAlignment());

		tableColumn.setAlignment(SWT.RIGHT);
		assertEquals(":c: Should not be allowed to set alignment of the first column", SWT.LEFT,
				tableColumn.getAlignment());

		column2 = new TableColumn(table, SWT.NULL);
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
	public void test_setMoveableZ() {
		assertFalse(":a:", tableColumn.getMoveable());

		tableColumn.setMoveable(true);
		assertTrue(":b:", tableColumn.getMoveable());

		tableColumn.setMoveable(true);
		assertTrue(":c:", tableColumn.getMoveable());

		tableColumn.setMoveable(false);
		assertFalse(":d:", tableColumn.getMoveable());

		TableColumn tableColumn2 = new TableColumn(tableColumn.getParent(), SWT.NONE);
		tableColumn2.dispose();
		try {
			tableColumn2.getMoveable();
			fail("No exception thrown for widget is Disposed");
		} catch (SWTException ex) {
		}
		try {
			tableColumn2.setMoveable(true);
			fail("No exception thrown for widget is Disposed");
		} catch (SWTException ex) {
		}
	}

	@Test
	public void test_setResizableZ() {
		assertTrue(":a:", tableColumn.getResizable());

		tableColumn.setResizable(false);
		assertFalse(":b:", tableColumn.getResizable());

		tableColumn.setResizable(false);
		assertFalse(":c:", tableColumn.getResizable());

		tableColumn.setResizable(true);
		assertTrue(":d:", tableColumn.getResizable());
	}

	@Override
	@Test
	public void test_setTextLjava_lang_String() {
		assertEquals(":a:", tableColumn.getText(), "");

		tableColumn.setText("text");
		assertEquals(":b:", tableColumn.getText(), "text");

		try {
			tableColumn.setText(null);
			fail("No exception thrown for column header == null");
		} catch (IllegalArgumentException e) {
		}
	}

	/* custom */
	protected TableColumn tableColumn;
	protected Table table;
}

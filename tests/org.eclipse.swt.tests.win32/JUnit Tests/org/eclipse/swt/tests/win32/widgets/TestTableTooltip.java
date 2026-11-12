/*******************************************************************************
 * Copyright (c) 2026 Eclipse contributors and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.internal.win32.LVHITTESTINFO;
import org.eclipse.swt.internal.win32.NMTTDISPINFO;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.POINT;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestTableTooltip {

	/** Rejects a real Windows row hit whose subitem lies outside the table. */
	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 22 })
	public void testOffTableTooltip(int columns) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		FontData data = display.getSystemFont().getFontData()[0];
		Font font = new Font(display, data.getName(), data.getHeight(), SWT.BOLD);
		try {
			shell.setSize(800, 400);
			Table table = createTable(shell, columns);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("A long cell label for the tooltip");
			item.setFont(0, font);
			int[] invalidMeasures = { 0 };
			int[] validMeasures = { 0 };
			table.addListener(SWT.MeasureItem, event -> {
				if (event.index < 0) {
					invalidMeasures[0]++;
				} else {
					validMeasures[0]++;
				}
			});

			// Move only the hidden shell: do not move the user's mouse or inject hit-test results.
			LVHITTESTINFO hit = positionMessage(shell, table, -10);
			assertEquals(-1, hit.iSubItem);
			sendTooltip(table);
			assertEquals(0, invalidMeasures[0], "An invalid hit must not reach cell measurement");

			hit = positionMessage(shell, table, 10);
			assertEquals(0, hit.iSubItem);
			validMeasures[0] = 0;
			sendTooltip(table);
			assertTrue(validMeasures[0] > 0, "Valid cell tooltips must still reach measurement");
		} finally {
			shell.dispose();
			font.dispose();
		}
	}

	/** Invalid font indices retain the existing row-font fallback. */
	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 22 })
	public void testCellFontBounds(int columns) throws Exception {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		FontData data = display.getSystemFont().getFontData()[0];
		Font rowFont = new Font(display, data.getName(), data.getHeight(), SWT.NORMAL);
		Font cellFont = new Font(display, data.getName(), data.getHeight(), SWT.BOLD);
		try {
			Table table = createTable(shell, columns);
			TableItem item = new TableItem(table, SWT.NONE);
			Method lookup = TableItem.class.getDeclaredMethod("fontHandle", int.class);
			lookup.setAccessible(true);
			assertEquals(-1L, lookup.invoke(item, -1));
			item.setFont(rowFont);
			Object rowHandle = lookup.invoke(item, 0);
			assertNotEquals(-1L, rowHandle);
			item.setFont(0, cellFont);
			Object cellHandle = lookup.invoke(item, 0);
			assertNotEquals(rowHandle, cellHandle);
			assertEquals(rowHandle, lookup.invoke(item, -1));
			assertEquals(rowHandle, lookup.invoke(item, Math.max(1, columns)));
			assertEquals(cellHandle, lookup.invoke(item, 0));
			item.setFont(null);
			assertEquals(-1L, lookup.invoke(item, -1));
			assertEquals(-1L, lookup.invoke(item, Math.max(1, columns)));
			assertEquals(cellHandle, lookup.invoke(item, 0));
		} finally {
			shell.dispose();
			rowFont.dispose();
			cellFont.dispose();
		}
	}

	/** Creates either explicit columns or SWT's implicit first column. */
	private static Table createTable(Shell shell, int columns) {
		Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.NO_SCROLL);
		table.setBounds(0, 0, 600, 250);
		table.setHeaderVisible(true);
		for (int i = 0; i < columns; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column " + i);
			column.setWidth(80);
		}
		return table;
	}

	/** Places the actual last-message position at first-row height in client coordinates. */
	private static LVHITTESTINFO positionMessage(Shell shell, Table table, int x) {
		POINT message = new POINT();
		OS.POINTSTOPOINT(message, OS.GetMessagePos());
		OS.ScreenToClient(table.handle, message);
		RECT bounds = new RECT();
		OS.GetWindowRect(shell.handle, bounds);
		int y = table.getHeaderHeight() + table.getItemHeight() / 2;
		OS.SetWindowPos(shell.handle, 0, bounds.left + message.x - x, bounds.top + message.y - y,
				0, 0, OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE);
		OS.POINTSTOPOINT(message, OS.GetMessagePos());
		OS.ScreenToClient(table.handle, message);
		LVHITTESTINFO hit = new LVHITTESTINFO();
		hit.x = message.x;
		hit.y = message.y;
		assertEquals(0, OS.SendMessage(table.handle, OS.LVM_SUBITEMHITTEST, 0, hit));
		assertEquals(0, hit.iItem);
		return hit;
	}

	/** Exercises native WM_NOTIFY dispatch with a real tooltip notification structure. */
	private static void sendTooltip(Table table) {
		NMTTDISPINFO info = new NMTTDISPINFO();
		info.hwndFrom = OS.SendMessage(table.handle, OS.LVM_GETTOOLTIPS, 0, 0);
		info.code = OS.TTN_GETDISPINFO;
		long heap = OS.GetProcessHeap();
		long memory = OS.HeapAlloc(heap, OS.HEAP_ZERO_MEMORY, NMTTDISPINFO.sizeof);
		assertNotEquals(0, memory);
		try {
			OS.MoveMemory(memory, info, NMTTDISPINFO.sizeof);
			OS.SendMessage(table.handle, OS.WM_NOTIFY, 0, memory);
		} finally {
			OS.HeapFree(heap, 0, memory);
		}
	}
}

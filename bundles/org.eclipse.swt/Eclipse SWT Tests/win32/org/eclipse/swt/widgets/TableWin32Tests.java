/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     vogella GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.win32.OS;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Pins how a Table paints an unfocused selection in the dark theme. */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class TableWin32Tests {

	private static final int COLUMN_WIDTH = 120;
	private static final String DARK_THEME_KEY = "org.eclipse.swt.internal.win32.useDarkModeExplorerTheme";
	private static final RGB DARK_BACKGROUND = new RGB(47, 47, 47);
	private static final RGB DARK_UNFOCUSED_SELECTION = new RGB(63, 63, 63);

	private Display display;
	private Shell shell;
	private Button focusTarget;
	private Table table;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		display.setData(DARK_THEME_KEY, true);
		shell = new Shell(display);
		focusTarget = new Button(shell, SWT.PUSH);
		focusTarget.setBounds(0, 0, 50, 20);
	}

	@AfterEach
	public void tearDown() {
		// The preferred app mode behind this key is process wide.
		display.setData(DARK_THEME_KEY, false);
		shell.dispose();
		display.dispose();
	}

	@Test
	public void test_fullSelectionFillsEveryColumn() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.FULL_SELECTION);
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_fullSelectionWithEraseItemListenerFillsEveryColumn() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.FULL_SELECTION);
		table.addListener(SWT.EraseItem, event -> {});
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_noFullSelectionFillsOnlyTheFirstColumn() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.NONE);
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_BACKGROUND, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_hideSelectionIsNotFilled() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		assertNotEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
	}

	@Test
	public void test_checkTableFillsBesideTheCheckbox() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.FULL_SELECTION | SWT.CHECK);
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(1));
	}

	@Test
	public void test_tableWithoutColumnsIsFilled() {
		assumeTrue(OS.IsDarkModeAvailable());
		openTable(SWT.FULL_SELECTION, 0);
		table.getItem(1).setText("text");
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(table.getItem(1).getBounds().x + 1));
	}

	private void openTable(int style) {
		openTable(style, 2);
	}

	private void openTable(int style, int columns) {
		table = new Table(shell, style);
		table.setBounds(0, 30, 3 * COLUMN_WIDTH, 150);
		table.setBackground(new Color(DARK_BACKGROUND));
		for (int i = 0; i < columns; i++) {
			new TableColumn(table, SWT.NONE).setWidth(COLUMN_WIDTH);
		}
		for (int i = 0; i < 3; i++) {
			new TableItem(table, SWT.NONE);
		}
		table.setSelection(1);
		shell.setSize(4 * COLUMN_WIDTH, 250);
		shell.open();
		focusTarget.setFocus();
		while (display.readAndDispatch()) {
			// process all pending events
		}
		// The real pointer may rest on the row.
		OS.SendMessage(table.handle, OS.WM_MOUSELEAVE, 0, 0);
	}

	/** Returns the color of the selected row at x, just above the text. */
	private RGB rgbAt(int x) {
		table.redraw();
		table.update();
		Rectangle clientArea = table.getClientArea();
		Image image = new Image(display, clientArea.width, clientArea.height);
		try {
			GC gc = new GC(table);
			try {
				gc.copyArea(image, 0, 0);
			} finally {
				gc.dispose();
			}
			ImageData data = image.getImageData();
			return data.palette.getRGB(data.getPixel(x, table.getItem(1).getBounds().y + 1));
		} finally {
			image.dispose();
		}
	}
}

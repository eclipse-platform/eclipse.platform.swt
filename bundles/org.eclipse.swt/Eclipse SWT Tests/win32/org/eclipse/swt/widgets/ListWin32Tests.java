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
 *     Lars Vogel - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

/**
 * Checks the pixels Windows paints for a {@link List} in the dark theme.
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class ListWin32Tests {

	private static final int SELECTED_ROW = 1;
	private static final int MIN_TEXT_CONTRAST = 100;

	private Display display;
	private Shell shell;
	private List list;
	private Button button;

	@BeforeEach
	void setUp() {
		assumeTrue(OS.IsDarkModeAvailable());
		display = Display.getDefault();
		display.setDarkThemePreferred(true);
		shell = new Shell(display);
		shell.setLayout(new GridLayout());
	}

	@AfterEach
	void tearDown() {
		if (display != null) {
			// Resets the process-wide dark mode for the other tests.
			display.setDarkThemePreferred(false);
			display.dispose();
		}
	}

	@Test
	void testSelectedRowShowsTextInDarkTheme() {
		createList(SWT.SINGLE);
		list.setFocus();
		processEvents();
		assertRowShowsText();
	}

	@Test
	void testSelectedRowShowsTextInDarkThemeWithoutFocus() {
		createList(SWT.MULTI);
		button.setFocus();
		processEvents();
		assertRowShowsText();
	}

	@Test
	void testScrollBarIsDark() {
		createList(SWT.SINGLE);
		assertTrue(luminance(dominantColor(captureList(), scrollBarArea())) < 128, "scrollbar not dark");
	}

	private void createList(int style) {
		list = new List(shell, style | SWT.BORDER | SWT.V_SCROLL);
		String[] items = new String[20];
		Arrays.setAll(items, i -> "item number " + i);
		list.setItems(items);
		list.setLayoutData(new GridData(200, 100));
		list.setBackground(new Color(0x30, 0x30, 0x30));
		list.setForeground(new Color(0xEE, 0xEE, 0xEE));
		list.select(SELECTED_ROW);
		button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		shell.pack();
		shell.open();
		processEvents();
	}

	private void assertRowShowsText() {
		int[][] pixels = captureList();
		RECT row = rowArea(SELECTED_ROW);
		int rowColor = dominantColor(pixels, row);
		assertNotEquals(dominantColor(pixels, rowArea(SELECTED_ROW + 1)), rowColor, "row not highlighted");
		int fill = luminance(rowColor);
		int contrast = 0;
		for (int color : colorsIn(pixels, row).keySet()) {
			contrast = Math.max(contrast, Math.abs(luminance(color) - fill));
		}
		assertTrue(contrast >= MIN_TEXT_CONTRAST, "selected row shows no text, contrast " + contrast);
	}

	private RECT rowArea(int index) {
		RECT row = new RECT();
		OS.SendMessage(list.handle, OS.LB_GETITEMRECT, index, row);
		POINT origin = clientOriginInWindow();
		row.left += origin.x;
		row.right += origin.x;
		row.top += origin.y;
		row.bottom += origin.y;
		return row;
	}

	private RECT scrollBarArea() {
		RECT client = new RECT();
		OS.GetClientRect(list.handle, client);
		RECT window = new RECT();
		OS.GetWindowRect(list.handle, window);
		POINT origin = clientOriginInWindow();
		RECT area = new RECT();
		area.left = origin.x + client.right;
		area.right = window.right - window.left - origin.x;
		area.top = origin.y;
		area.bottom = origin.y + client.bottom;
		return area;
	}

	/** PrintWindow captures the whole window, while list messages use client coordinates. */
	private POINT clientOriginInWindow() {
		RECT window = new RECT();
		OS.GetWindowRect(list.handle, window);
		POINT origin = new POINT();
		OS.ClientToScreen(list.handle, origin);
		origin.x -= window.left;
		origin.y -= window.top;
		return origin;
	}

	/** Captures the list in physical pixels, independent of the zoom of SWT images. */
	private int[][] captureList() {
		RECT window = new RECT();
		OS.GetWindowRect(list.handle, window);
		int width = window.right - window.left;
		int height = window.bottom - window.top;
		long screenDC = OS.GetDC(0);
		long memoryDC = OS.CreateCompatibleDC(screenDC);
		long bitmap = OS.CreateCompatibleBitmap(screenDC, width, height);
		long oldBitmap = OS.SelectObject(memoryDC, bitmap);
		OS.ReleaseDC(0, screenDC);
		try {
			// Without PW_RENDERFULLCONTENT a child list box captures black.
			OS.PrintWindow(list.handle, memoryDC, OS.PW_RENDERFULLCONTENT);
			int[][] pixels = new int[height][width];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					pixels[y][x] = OS.GetPixel(memoryDC, x, y);
				}
			}
			return pixels;
		} finally {
			OS.SelectObject(memoryDC, oldBitmap);
			OS.DeleteObject(bitmap);
			OS.DeleteDC(memoryDC);
		}
	}

	private static Map<Integer, Integer> colorsIn(int[][] pixels, RECT area) {
		Map<Integer, Integer> colors = new HashMap<>();
		for (int y = area.top; y < area.bottom; y++) {
			for (int x = area.left; x < area.right; x++) {
				colors.merge(pixels[y][x], 1, Integer::sum);
			}
		}
		return colors;
	}

	private static int dominantColor(int[][] pixels, RECT area) {
		return Collections.max(colorsIn(pixels, area).entrySet(), Map.Entry.comparingByValue()).getKey();
	}

	/** Takes a COLORREF, which stores red in the lowest byte. */
	private static int luminance(int color) {
		return ((color & 0xFF) * 299 + (color >> 8 & 0xFF) * 587 + (color >> 16 & 0xFF) * 114) / 1000;
	}

	private void processEvents() {
		long end = System.currentTimeMillis() + 300;
		while (System.currentTimeMillis() < end) {
			while (display.readAndDispatch()) {
				// drain
			}
			Thread.yield();
		}
	}
}

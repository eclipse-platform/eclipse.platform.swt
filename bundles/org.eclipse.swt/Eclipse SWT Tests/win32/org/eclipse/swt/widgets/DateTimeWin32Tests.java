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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.win32.MONITORINFO;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Checks what a {@link DateTime} paints in dark mode and with custom colors. */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class DateTimeWin32Tests {
	private static final String DARK_MODE_KEY = "org.eclipse.swt.internal.win32.useDarkModeExplorerTheme";
	private static final RGB DARK_BACKGROUND = new RGB(0x20, 0x20, 0x20);
	private static final RGB CUSTOM_BACKGROUND = new RGB(0, 60, 90);
	private static final RGB CUSTOM_FOREGROUND = new RGB(255, 220, 120);
	private static final RGB BLACK = new RGB(0, 0, 0);
	private static final int WHITE_PIXEL = 0xFFFFFF;
	private static final int BLACK_PIXEL = 0x000000;
	private static final int BG = 0x003C5A;
	private static final int FG = 0xFFDC78;
	private static final int HIGHLIGHT = 0x0078D7;
	private static final int MCM_GETCOLOR = OS.MCM_FIRST + 11;
	private static final int DTM_CLOSEMONTHCAL = OS.DTM_FIRST + 13;
	private static final int FOREGROUND_TOLERANCE = 30;
	private static final int MIN_TEXT_PIXELS = 5;

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		// the preferred app mode behind this key is process wide
		display.setData(DARK_MODE_KEY, Boolean.FALSE);
		display.dispose();
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_darkModeBackground(int style) {
		display.setData(DARK_MODE_KEY, Boolean.TRUE);
		assumeTrue(OS.IsDarkModeAvailable(), "dark mode is not available");
		DateTime dateTime = new DateTime(shell, style);
		assertEquals(DARK_BACKGROUND, dateTime.getBackground().getRGB());
		assertEquals(DARK_BACKGROUND, renderedBackground(dateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_customBackgroundWithoutDarkMode(int style) {
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		assertEquals(CUSTOM_BACKGROUND, renderedBackground(dateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_customBackgroundWinsOverDarkMode(int style) {
		display.setData(DARK_MODE_KEY, Boolean.TRUE);
		assumeTrue(OS.IsDarkModeAvailable(), "dark mode is not available");
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		assertEquals(CUSTOM_BACKGROUND, renderedBackground(dateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_transparentBackgroundKeepsDarkDefault(int style) {
		display.setData(DARK_MODE_KEY, Boolean.TRUE);
		assumeTrue(OS.IsDarkModeAvailable(), "dark mode is not available");
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(200, 0, 0, 0));
		assertEquals(DARK_BACKGROUND, renderedBackground(dateTime));
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.TIME })
	public void test_resetBackgroundIgnoresInheritDefault(int style) {
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setBackground(new Color(200, 0, 0));
		DateTime dateTime = new DateTime(shell, style);
		RGB defaultBackground = dateTime.getBackground().getRGB();
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		dateTime.setBackground(null);
		assertEquals(defaultBackground, dateTime.getBackground().getRGB());
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.CALENDAR, SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_customForegroundIsRendered(int style) {
		DateTime dateTime = customColored(style);
		RECT size = new RECT();
		OS.GetClientRect(dateTime.handle, size);
		// a capture scaled down to points blurs the thin strokes at a high zoom
		assertTrue(foregroundPixelCount(printedPixels(dateTime, size)) >= MIN_TEXT_PIXELS, "no text in the foreground color");
	}

	@Test
	public void test_dropDownCalendarUsesCustomColors() {
		DateTime dateTime = new DateTime(shell, SWT.DATE | SWT.DROP_DOWN);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		dateTime.setForeground(new Color(CUSTOM_FOREGROUND));
		renderedBackground(dateTime);
		OS.SendMessage(dateTime.handle, OS.WM_KEYDOWN, OS.VK_F4, 0);
		long calendar = OS.SendMessage(dateTime.handle, OS.DTM_GETMONTHCAL, 0, 0);
		assertNotEquals(0, calendar, "the drop-down did not open");
		while (display.readAndDispatch()) {
			// let the popup finish its layout
		}
		try {
			assertEquals(colorRef(CUSTOM_BACKGROUND), OS.SendMessage(calendar, MCM_GETCOLOR, OS.MCSC_MONTHBK, 0));
			assertEquals(colorRef(CUSTOM_FOREGROUND), OS.SendMessage(calendar, MCM_GETCOLOR, OS.MCSC_TEXT, 0));

			RECT calendarSize = new RECT(), popupSize = new RECT();
			OS.GetClientRect(calendar, calendarSize);
			OS.GetClientRect(OS.GetParent(calendar), popupSize);
			assertEquals(popupSize.right, calendarSize.right, "the popup would show a light margin");
			assertEquals(popupSize.bottom, calendarSize.bottom, "the popup would show a light margin");

			ImageData rendered = printCalendar(calendar, calendarSize);
			assertEquals(CUSTOM_BACKGROUND, mostFrequentColor(rendered));
			assertTrue(foregroundPixelCount(rendered) >= MIN_TEXT_PIXELS, "no text in the foreground color");
		} finally {
			OS.SendMessage(dateTime.handle, DTM_CLOSEMONTHCAL, 0, 0);
		}
		assertEquals(0, OS.SendMessage(dateTime.handle, OS.DTM_GETMONTHCAL, 0, 0));
	}

	/** The fitted popup can be taller than the one Windows placed, so it must neither grow off the monitor nor cover the field. */
	@Test
	public void test_dropDownStaysOnMonitor() {
		DateTime dateTime = new DateTime(shell, SWT.DATE | SWT.DROP_DOWN);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		renderedBackground(dateTime);
		RECT shellRect = new RECT(), control = new RECT(), popup = new RECT();
		OS.GetWindowRect(shell.handle, shellRect);
		OS.GetWindowRect(dateTime.handle, control);
		int controlBottomInShell = control.bottom - shellRect.top;
		MONITORINFO monitor = new MONITORINFO();
		monitor.cbSize = MONITORINFO.sizeof;
		OS.GetMonitorInfo(OS.MonitorFromWindow(shell.handle, OS.MONITOR_DEFAULTTONEAREST), monitor);
		int popupHeight = openDropDown(dateTime, popup);
		for (int gap = popupHeight + 2; gap > popupHeight * 7 / 8; gap -= 2) {
			OS.SetWindowPos(shell.handle, 0, shellRect.left, monitor.rcWork_bottom - gap - controlBottomInShell, 0, 0,
					OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE);
			openDropDown(dateTime, popup);
			assertTrue(popup.bottom <= monitor.rcWork_bottom, "popup ends at " + popup.bottom + " for a gap of " + gap);
			OS.GetWindowRect(dateTime.handle, control);
			assertTrue(popup.bottom <= control.top || popup.top >= control.bottom, "popup covers the field for a gap of " + gap);
		}
	}

	/** Opens and closes the drop-down, stores its fitted popup bounds and returns their height. */
	private int openDropDown(DateTime dateTime, RECT popup) {
		OS.SendMessage(dateTime.handle, OS.WM_KEYDOWN, OS.VK_F4, 0);
		long calendar = OS.SendMessage(dateTime.handle, OS.DTM_GETMONTHCAL, 0, 0);
		assertNotEquals(0, calendar, "the drop-down did not open");
		while (display.readAndDispatch()) {
			// let the popup finish its layout
		}
		OS.GetWindowRect(OS.GetParent(calendar), popup);
		OS.SendMessage(dateTime.handle, DTM_CLOSEMONTHCAL, 0, 0);
		return popup.bottom - popup.top;
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_fieldBorderIsFlat(int style) {
		DateTime dateTime = customColored(style);
		RECT size = new RECT();
		OS.GetClientRect(dateTime.handle, size);
		int[] pixels = printedPixels(dateTime, size);
		int width = size.right, height = size.bottom;
		int ring = DateTime.blend(colorRef(CUSTOM_BACKGROUND), colorRef(CUSTOM_FOREGROUND), 35);
		assertEquals(ring, pixels[width / 2], "top");
		assertEquals(ring, pixels[height / 2 * width], "left");
		assertEquals(ring, pixels[(height - 1) * width + width / 2], "bottom");
		assertEquals(ring, pixels[height / 2 * width + width - 1], "right");
		assertEquals(colorRef(CUSTOM_BACKGROUND), pixels[width + width / 2], "inside the top");
		assertEquals(colorRef(CUSTOM_BACKGROUND), pixels[(height - 2) * width + width / 2], "inside the bottom");
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.NONE, SWT.RIGHT_TO_LEFT })
	public void test_dropDownButtonBlendsIntoField(int orientation) {
		DateTime dateTime = customColored(SWT.DATE | SWT.DROP_DOWN | orientation);
		RECT size = new RECT();
		OS.GetClientRect(dateTime.handle, size);
		int[] pixels = printedPixels(dateTime, size);
		int width = size.right, height = size.bottom, bg = colorRef(CUSTOM_BACKGROUND);
		int edge = dateTime.getSystemMetrics(OS.SM_CXEDGE), buttonWidth = dateTime.getSystemMetrics(OS.SM_CXVSCROLL);
		// rows above and below the content cross the whole field, so a misplaced button rectangle leaves its edge here
		for (int x = edge; x < width - edge; x++) {
			assertEquals(bg, pixels[edge * width + x], "top row at " + x);
			assertEquals(bg, pixels[(height - edge - 1) * width + x], "bottom row at " + x);
		}
		int left = orientation == SWT.RIGHT_TO_LEFT ? edge : width - edge - buttonWidth;
		int other = orientation == SWT.RIGHT_TO_LEFT ? width - edge - buttonWidth : edge;
		assertTrue(countNotBackground(pixels, width, left, buttonWidth, height, edge) > 0, "no arrow");
		assertTrue(countNotBackground(pixels, width, other, buttonWidth, height, edge) > 0, "no date");
		for (int y = edge; y < height - edge; y++) {
			assertEquals(bg, pixels[y * width + left], "button side at " + y);
			assertEquals(bg, pixels[y * width + left + buttonWidth - 1], "button side at " + y);
		}
		// only the arrow may differ from the field, a leftover button face would show here
		for (int y = edge; y < height - edge; y++) {
			for (int x = left; x < left + buttonWidth; x++) {
				int pixel = pixels[y * width + x];
				assertTrue(pixel == bg || isForeground(pixel), "button face at " + x + "," + y);
			}
		}
	}

	private DateTime customColored(int style) {
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		dateTime.setForeground(new Color(CUSTOM_FOREGROUND));
		renderedBackground(dateTime);
		return dateTime;
	}

	private static int countNotBackground(int[] pixels, int width, int left, int columns, int height, int edge) {
		int bg = colorRef(CUSTOM_BACKGROUND), count = 0;
		for (int y = edge; y < height - edge; y++) {
			for (int x = left; x < left + columns; x++) {
				if (pixels[y * width + x] != bg) count++;
			}
		}
		return count;
	}

	private static int foregroundPixelCount(int[] colorRefs) {
		int count = 0;
		for (int pixel : colorRefs) {
			if (isForeground(pixel)) count++;
		}
		return count;
	}

	private static boolean isForeground(int colorRef) {
		return distance(new RGB(colorRef & 0xFF, colorRef >> 8 & 0xFF, colorRef >> 16 & 0xFF), CUSTOM_FOREGROUND) <= FOREGROUND_TOLERANCE;
	}

	/** Prints the control and returns its client area in pixels as color references. */
	private int[] printedPixels(DateTime dateTime, RECT size) {
		// the size is in pixels, while an image is created in points
		int zoom = shell.getMonitor().getZoom();
		Image image = new Image(display, (size.right * 100 + zoom - 1) / zoom, (size.bottom * 100 + zoom - 1) / zoom);
		GC gc = new GC(image);
		try {
			assertTrue(dateTime.print(gc));
			int[] pixels = new int[size.right * size.bottom];
			for (int y = 0, i = 0; y < size.bottom; y++) {
				for (int x = 0; x < size.right; x++, i++) {
					pixels[i] = OS.GetPixel(gc.handle, x, y) & 0xFFFFFF;
				}
			}
			return pixels;
		} finally {
			gc.dispose();
			image.dispose();
		}
	}

	@Test
	public void test_flattenButtonKeepsArrow() {
		int e = 9, f = 5, a = 7, b = 3;
		int[] pixels = { //
				0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, e, e, e, e, e, 0, //
				0, 0, e, f, f, f, e, 0, //
				0, 0, e, f, a, f, e, 0, //
				0, 0, e, f, f, f, e, 0, //
				0, 0, e, e, e, e, e, 0, //
				0, 0, 0, 0, 0, 0, 0, 0 };
		DateTime.flattenButton(pixels, 8, 7, 1, 5, false, b);
		assertArrayEquals(new int[] { //
				0, 0, 0, 0, 0, 0, 0, 0, //
				0, 0, b, b, b, b, b, 0, //
				0, 0, b, b, b, b, b, 0, //
				0, 0, b, b, a, b, b, 0, //
				0, 0, b, b, b, b, b, 0, //
				0, 0, b, b, b, b, b, 0, //
				0, 0, 0, 0, 0, 0, 0, 0 }, pixels);
	}

	@Test
	public void test_flattenButtonMirrored() {
		int e = 9, b = 3;
		int[] pixels = { //
				0, 0, 0, 0, 0, 0, 0, //
				0, e, e, e, e, 0, 0, //
				0, e, 5, 5, e, 0, 0, //
				0, e, e, e, e, 0, 0, //
				0, 0, 0, 0, 0, 0, 0 };
		DateTime.flattenButton(pixels, 7, 5, 1, 4, true, b);
		assertArrayEquals(new int[] { //
				0, 0, 0, 0, 0, 0, 0, //
				0, b, b, b, b, 0, 0, //
				0, b, b, b, b, 0, 0, //
				0, b, b, b, b, 0, 0, //
				0, 0, 0, 0, 0, 0, 0 }, pixels);
	}

	@Test
	public void test_flattenBorderKeepsContent() {
		int r = 1, b = 2, c = 9;
		int[] pixels = new int[25];
		Arrays.fill(pixels, c);
		DateTime.flattenBorder(pixels, 5, 5, 2, r, b);
		assertArrayEquals(new int[] { //
				r, r, r, r, r, //
				r, b, b, b, r, //
				r, b, c, b, r, //
				r, b, b, b, r, //
				r, r, r, r, r }, pixels);
	}

	@Test
	public void test_printPaintsCustomBorder() {
		// the date time picker has no client edge, it draws its border as part of the client area
		DateTime dateTime = new DateTime(shell, SWT.CALENDAR | SWT.BORDER);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		dateTime.setForeground(new Color(CUSTOM_FOREGROUND));
		renderedBackground(dateTime);
		Point size = dateTime.getSize();
		Image image = new Image(display, size.x, size.y);
		try {
			GC gc = new GC(image);
			OS.SendMessage(dateTime.handle, OS.WM_PRINT, gc.handle, OS.PRF_NONCLIENT | OS.PRF_CLIENT | OS.PRF_ERASEBKGND);
			int ring = DateTime.blend(colorRef(CUSTOM_BACKGROUND), colorRef(CUSTOM_FOREGROUND), 35);
			assertEquals(ring, OS.GetPixel(gc.handle, 0, 0) & 0xFFFFFF);
			assertEquals(colorRef(CUSTOM_BACKGROUND), OS.GetPixel(gc.handle, 1, 1) & 0xFFFFFF);
			gc.dispose();
		} finally {
			image.dispose();
		}
	}

	private ImageData printCalendar(long calendar, RECT size) {
		// the size is in pixels, while an image is created in points
		int zoom = shell.getMonitor().getZoom();
		Image image = new Image(display, size.right * 100 / zoom, size.bottom * 100 / zoom);
		try {
			GC gc = new GC(image);
			OS.SendMessage(calendar, OS.WM_PRINT, gc.handle, OS.PRF_CLIENT | OS.PRF_ERASEBKGND);
			gc.dispose();
			return image.getImageData();
		} finally {
			image.dispose();
		}
	}

	private static int colorRef(RGB rgb) {
		return rgb.blue << 16 | rgb.green << 8 | rgb.red;
	}

	private static int foregroundPixelCount(ImageData data) {
		int count = 0;
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				if (distance(data.palette.getRGB(data.getPixel(x, y)), CUSTOM_FOREGROUND) <= FOREGROUND_TOLERANCE) {
					count++;
				}
			}
		}
		return count;
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.DATE, SWT.DATE | SWT.DROP_DOWN, SWT.TIME })
	public void test_customBackgroundWhileDisabled(int style) {
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		dateTime.setEnabled(false);
		assertEquals(CUSTOM_BACKGROUND, renderedBackground(dateTime));
	}

	@Test
	public void test_recolorMapsSystemColorsExactly() {
		int[] pixels = { WHITE_PIXEL, BLACK_PIXEL };
		recolor(pixels, 2, 1, WHITE_PIXEL, BLACK_PIXEL, -1);
		assertArrayEquals(new int[] { BG, FG }, pixels);
	}

	@Test
	public void test_recolorHandlesDarkContrastTheme() {
		int window = 0x202020;
		int[] pixels = { window, WHITE_PIXEL };
		recolor(pixels, 2, 1, window, WHITE_PIXEL, -1);
		assertArrayEquals(new int[] { BG, FG }, pixels);
	}

	@Test
	public void test_recolorHandlesLightContrastTheme() {
		int window = 0xFFFAEF, text = 0x3D3D3D;
		int[] pixels = { window, text };
		recolor(pixels, 2, 1, window, text, -1);
		assertArrayEquals(new int[] { BG, FG }, pixels);
	}

	@Test
	public void test_recolorHandlesEqualSystemChannel() {
		// yellow text on black has no contrast in the blue channel
		int yellow = 0xFFFF00;
		int[] pixels = { BLACK_PIXEL, yellow };
		recolor(pixels, 2, 1, BLACK_PIXEL, yellow, -1);
		assertArrayEquals(new int[] { BG, FG }, pixels);
	}

	@Test
	public void test_recolorKeepsSelectedField() {
		int h = HIGHLIGHT, w = WHITE_PIXEL;
		int[] pixels = { //
				w, h, h, h, h, w, //
				w, h, w, h, h, w, //
				w, h, h, h, h, w };
		recolor(pixels, 6, 3, WHITE_PIXEL, BLACK_PIXEL, HIGHLIGHT);
		assertArrayEquals(new int[] { //
				BG, h, h, h, h, BG, //
				BG, h, w, h, h, BG, //
				BG, h, h, h, h, BG }, pixels);
	}

	@ParameterizedTest
	@ValueSource(ints = { SWT.DATE, SWT.TIME })
	public void test_focusedFieldKeepsHighlight(int style) {
		DateTime dateTime = new DateTime(shell, style);
		dateTime.setBackground(new Color(CUSTOM_BACKGROUND));
		renderedBackground(dateTime);
		dateTime.forceFocus();
		assumeTrue(OS.GetFocus() == dateTime.handle, "the shell did not get the focus");
		RGB highlight = display.getSystemColor(SWT.COLOR_LIST_SELECTION).getRGB();
		assertTrue(colorCount(dateTime, highlight) > 0, "no selected field");
		assertEquals(CUSTOM_BACKGROUND, renderedBackground(dateTime));
	}

	private int colorCount(DateTime dateTime, RGB color) {
		ImageData data = capture(dateTime);
		int count = 0;
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				if (color.equals(data.palette.getRGB(data.getPixel(x, y)))) {
					count++;
				}
			}
		}
		return count;
	}

	private ImageData capture(DateTime dateTime) {
		Point size = dateTime.getSize();
		Image image = new Image(display, size.x, size.y);
		try {
			GC gc = new GC(image);
			assertTrue(dateTime.print(gc));
			gc.dispose();
			return image.getImageData();
		} finally {
			image.dispose();
		}
	}

	@Test
	public void test_recolorIgnoresIsolatedHighlightPixels() {
		int h = HIGHLIGHT, w = WHITE_PIXEL;
		int[] pixels = { //
				h, h, h, h, w, w, w, w, //
				w, w, w, w, w, w, w, h };
		recolor(pixels, 8, 2, WHITE_PIXEL, BLACK_PIXEL, HIGHLIGHT);
		assertEquals(h, pixels[0]);
		assertEquals(BG, pixels[4]);
		assertEquals(BG, pixels[8 + 4]);
	}

	@Test
	public void test_recolorWithoutSelectionKeepsNothing() {
		int h = HIGHLIGHT;
		int[] pixels = { h, h, h, h };
		recolor(pixels, 4, 1, WHITE_PIXEL, BLACK_PIXEL, -1);
		for (int pixel : pixels) {
			assertNotEquals(h, pixel);
		}
	}

	private static void recolor(int[] pixels, int width, int height, int window, int text, int highlight) {
		DateTime.recolor(pixels, width, height, window, text, highlight, BG, FG);
	}

	private static int distance(RGB a, RGB b) {
		return Math.abs(a.red - b.red) + Math.abs(a.green - b.green) + Math.abs(a.blue - b.blue);
	}

	/** Returns the most frequent color of the rendered control. */
	private RGB renderedBackground(DateTime dateTime) {
		Point size = dateTime.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		dateTime.setSize(size);
		shell.setSize(size.x + 100, size.y + 100);
		shell.open();
		// print() yields a solid black image for any control while a new shell is still being shown
		long deadline = System.currentTimeMillis() + 5000;
		RGB rendered;
		do {
			while (display.readAndDispatch()) {
				// let the control paint
			}
			rendered = mostFrequentColor(dateTime);
		} while (rendered.equals(BLACK) && System.currentTimeMillis() < deadline);
		return rendered;
	}

	private RGB mostFrequentColor(DateTime dateTime) {
		return mostFrequentColor(capture(dateTime));
	}

	private static RGB mostFrequentColor(ImageData data) {
		Map<RGB, Integer> counts = new HashMap<>();
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				counts.merge(data.palette.getRGB(data.getPixel(x, y)), 1, Integer::sum);
			}
		}
		return counts.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey();
	}
}

/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.WithMonitorSpecificScalingExtension;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Automated regression tests for the opt-in line colors, native fallback and
 * Explorer separators. The widget package allows access to native header handles
 * and package-private drawing helpers for pixel assertions.
 *
 * For interactive visual checks, run the manual snippet
 * Issue3790_TableTreeLineColors in org.eclipse.swt.tests.win32.snippets.
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class LineColorsWin32Tests {

	private static final RGB BACKGROUND = new RGB(47, 47, 47);
	private static final RGB HEADER = new RGB(96, 102, 106);
	private static final RGB GRID = new RGB(65, 70, 73);
	private Display display;
	private Shell shell;
	private Table table;
	private Tree tree;

	@BeforeEach
	void setUp() {
		display = Display.getDefault();
		display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", true);
		shell = new Shell(display);
		table = new Table(shell, SWT.FULL_SELECTION | SWT.MULTI);
		tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);
		table.setBounds(0, 0, 260, 180);
		tree.setBounds(270, 0, 260, 180);
		for (int i = 0; i < 3; i++) {
			new TableColumn(table, SWT.NONE).setWidth(110);
			new TreeColumn(tree, SWT.NONE).setWidth(110);
		}
		for (int i = 0; i < 30; i++) {
			new TableItem(table, SWT.NONE);
			new TreeItem(tree, SWT.NONE);
		}
		table.setHeaderVisible(true);
		tree.setHeaderVisible(true);
		for (Control control : new Control[] {table, tree}) {
			control.setBackground(new Color(BACKGROUND));
			setColor(control, "headerLineColor", HEADER);
			setColor(control, "gridLineColor", GRID);
		}
		table.setLinesVisible(true);
		tree.setLinesVisible(true);
		shell.setSize(560, 240);
		shell.open();
		flush();
	}

	@AfterEach
	void tearDown() {
		display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", false);
		shell.dispose();
		display.dispose();
	}

	@Test
	void customGridReplacesExplorerSeparators() {
		assertGrid(table, 0, GRID);
		assertGrid(tree, 0, GRID);
	}

	@Test
	void gridFollowsDpiChange() {
		DPITestUtil.changeDPIZoom(shell, 200);
		flush();
		assertGrid(table, 0, GRID);
		assertGrid(tree, 0, GRID);
	}

	@Test
	void changingGridColorDoesNotChangeHeaderColor() {
		RGB changed = new RGB(80, 130, 150);
		setColor(table, "gridLineColor", changed);
		setColor(tree, "gridLineColor", changed);
		flush();
		assertGrid(table, 0, changed);
		assertGrid(tree, 0, changed);
		assertHeader(table.hwndHeader, HEADER);
		assertHeader(tree.hwndHeader, HEADER);
	}

	@Test
	void resettingTableColorRestoresNativeGridAndPreservesVisibility() {
		assertTrue(table.getLinesVisible());
		assertFalse(nativeTableLines());
		setColor(table, "gridLineColor", null);
		assertTrue(table.getLinesVisible());
		assertTrue(nativeTableLines());
		setColor(table, "gridLineColor", GRID);
		table.setLinesVisible(false);
		assertFalse(table.getLinesVisible());
		assertFalse(nativeTableLines());
		setColor(table, "gridLineColor", null);
		assertFalse(table.getLinesVisible());
		assertFalse(nativeTableLines());
	}

	@Test
	void colorsCanBeConfiguredAfterLinesAreEnabled() {
		setColor(table, "gridLineColor", null);
		table.setLinesVisible(true);
		assertTrue(nativeTableLines());
		setColor(table, "gridLineColor", GRID);
		assertTrue(table.getLinesVisible());
		assertFalse(nativeTableLines());
		assertGrid(table, 0, GRID);
	}

	@Test
	void gridFollowsReorderedResizedAndScrolledColumns() {
		table.setColumnOrder(new int[] {1, 0, 2});
		tree.setColumnOrder(new int[] {1, 0, 2});
		table.getColumn(1).setWidth(150);
		tree.getColumn(1).setWidth(150);
		table.setTopIndex(10);
		tree.setTopItem(tree.getItem(10));
		table.showColumn(table.getColumn(2));
		tree.showColumn(tree.getColumn(2));
		flush();
		assertGrid(table, 0, GRID);
		assertGrid(tree, 0, GRID);
	}

	@Test
	void emptyControlsStillPaintTheirGrid() {
		table.removeAll();
		tree.removeAll();
		flush();
		assertGrid(table, 0, GRID);
		assertGrid(tree, 0, GRID);
	}

	@Test
	void headerOnlyColorDoesNotEnableGrid() {
		table.setLinesVisible(false);
		tree.setLinesVisible(false);
		setColor(table, "gridLineColor", null);
		setColor(tree, "gridLineColor", null);
		flush();
		assertHeader(table.hwndHeader, HEADER);
		assertHeader(tree.hwndHeader, HEADER);
		assertFalse(table.getLinesVisible());
		assertFalse(tree.getLinesVisible());
	}

	@Test
	void tableHeaderColorOverridesAndResetsToDisplaySetting() {
		RGB fallback = new RGB(120, 80, 100);
		table.setHeaderBackground(new Color(BACKGROUND));
		display.setData("org.eclipse.swt.internal.win32.Table.headerLineColor", new Color(fallback));
		assertHeader(table.hwndHeader, HEADER);
		setColor(table, "headerLineColor", null);
		flush();
		assertHeader(table.hwndHeader, fallback);
	}

	@Test
	void invalidColorLeavesThePreviousSettingIntact() {
		for (Control control : new Control[] {table, tree}) {
			String key = key(control, "gridLineColor");
			Object previous = control.getData(key);
			assertThrows(IllegalArgumentException.class, () -> control.setData(key, "invalid"));
			assertSame(previous, control.getData(key));
			assertGrid(control, 0, GRID);
		}
	}

	@Test
	void customGridPreservesUnfocusedDarkSelection() {
		org.junit.jupiter.api.Assumptions.assumeTrue(OS.IsDarkModeAvailable());
		Button focusTarget = new Button(shell, SWT.PUSH);
		focusTarget.setBounds(0, 190, 60, 20);
		table.setSelection(1);
		tree.setSelection(tree.getItem(1));
		focusTarget.setFocus();
		flush();
		OS.SendMessage(table.handle, OS.WM_MOUSELEAVE, 0, 0);
		OS.SendMessage(tree.handle, OS.WM_MOUSELEAVE, 0, 0);
		RGB selection = new RGB(63, 63, 63);
		assertSelectedRow(table, table.getItem(1).getBounds(0), selection);
		assertSelectedRow(tree, tree.getItem(1).getBounds(0), selection);
	}

	@Test
	void customGridDoesNotAffectAnotherWidget() {
		Table other = new Table(shell, SWT.NONE);
		other.setLinesVisible(true);
		assertTrue((OS.SendMessage(other.handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0) & OS.LVS_EX_GRIDLINES) != 0);
		assertFalse(nativeTableLines());
	}

	@Test
	void customGridPreservesSortColumnBackgroundBelowItems() {
		table.setSortColumn(table.getColumn(0));
		table.setSortDirection(SWT.UP);
		table.setItemCount(1);
		flush();
		long dc = OS.GetDC(table.handle);
		try {
			int pixel = OS.GetPixel(dc, 20, 100);
			assertEquals(table.getSortColumnPixel(), pixel, "Sorted column background below the last row");
		} finally {
			OS.ReleaseDC(table.handle, dc);
		}
	}

	private static String key(Control control, String suffix) {
		return "org.eclipse.swt.internal.win32." + (control instanceof Table ? "Table." : "Tree.") + suffix;
	}

	private void setColor(Control control, String suffix, RGB color) {
		control.setData(key(control, suffix), color == null ? null : new Color(color));
	}

	private boolean nativeTableLines() {
		return (OS.SendMessage(table.handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0) & OS.LVS_EX_GRIDLINES) != 0;
	}

	private void flush() {
		while (display.readAndDispatch()) {
			// Drain paint and resize events.
		}
		table.update();
		tree.update();
	}

	private void assertHeader(long header, RGB expected) {
		OS.UpdateWindow(header);
		RECT rect = new RECT();
		OS.GetClientRect(header, rect);
		long dc = OS.GetDC(header);
		try {
			int pixel = OS.GetPixel(dc, 10, rect.bottom - 1);
			assertEquals(expected, Color.win32_new(display, pixel).getRGB(), "Header bottom separator");
		} finally {
			OS.ReleaseDC(header, dc);
		}
	}

	private void assertSelectedRow(Control control, Rectangle bounds, RGB expected) {
		control.redraw();
		control.update();
		Image image = new Image(display, 220, bounds.height);
		try {
			GC gc = new GC(control);
			try {
				gc.copyArea(image, 0, bounds.y);
			} finally {
				gc.dispose();
			}
			ImageData pixels = image.getImageData();
			assertEquals(expected, pixels.palette.getRGB(pixels.getPixel(100, 1)));
			assertEquals(expected, pixels.palette.getRGB(pixels.getPixel(200, 1)));
		} finally {
			image.dispose();
		}
	}

	private void assertGrid(Control control, int column, RGB expected) {
		control.redraw();
		control.update();
		int x;
		int y;
		if (control instanceof Table t) {
			RECT rect = new RECT();
			OS.SendMessage(t.hwndHeader, OS.HDM_GETITEMRECT, column, rect);
			OS.MapWindowPoints(t.hwndHeader, t.handle, rect, 2);
			x = rect.right - 1;
			y = t.getHeaderHeightInPixels() + 2;
		} else {
			Tree t = (Tree) control;
			RECT rect = new RECT();
			OS.SendMessage(t.hwndHeader, OS.HDM_GETITEMRECT, column, rect);
			OS.MapWindowPoints(t.hwndHeader, t.handle, rect, 2);
			x = rect.right - 1;
			y = 2;
		}
		Rectangle client = ((Composite) control).getClientArea();
		int zoom = control.getAutoscalingZoom();
		x -= DPIUtil.pointToPixel(client.x, zoom);
		y -= DPIUtil.pointToPixel(client.y, zoom);
		Image image = new Image(display, client.width, client.height);
		try {
			GC gc = new GC(control);
			try {
				gc.copyArea(image, client.x, client.y);
			} finally {
				gc.dispose();
			}
			ImageData pixels = image.getImageData(zoom);
			assertTrue(x > 0 && x < pixels.width - 1, control.getClass().getSimpleName() + " separator outside capture");
			assertEquals(expected, pixels.palette.getRGB(pixels.getPixel(x, y)), "Grid separator");
			int width = DPIUtil.pointToPixel(1, zoom);
			assertEquals(BACKGROUND, pixels.palette.getRGB(pixels.getPixel(x - width, y)), "No adjacent Explorer separator");
			assertEquals(BACKGROUND, pixels.palette.getRGB(pixels.getPixel(x + 1, y)), "Single grid stroke");
			int horizontalLines = 0;
			for (int row = y + 2; row < pixels.height - 2; row++) {
				if (expected.equals(pixels.palette.getRGB(pixels.getPixel(x - 10, row)))) horizontalLines++;
			}
			assertTrue(horizontalLines >= 2, "Horizontal grid lines must also be painted");
		} finally {
			image.dispose();
		}
	}
}

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/** Pins how a Tree paints hover and selection states, including a hover through native messages public API cannot send. */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class TreeWin32Tests {

	private static final int COLUMNS = 3;
	private static final int COLUMN_WIDTH = 120;
	private static final String DARK_THEME_KEY = "org.eclipse.swt.internal.win32.useDarkModeExplorerTheme";
	private static final RGB DARK_BACKGROUND = new RGB(47, 47, 47);
	private static final RGB DARK_UNFOCUSED_SELECTION = new RGB(63, 63, 63);

	private Display display;
	private Shell shell;
	private Button focusTarget;
	private Tree paintedTree;
	private TreeItem paintedItem;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
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
	public void test_hoverIsPainted() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		// The real pointer may rest on the row.
		OS.SendMessage(paintedTree.handle, OS.WM_MOUSELEAVE, 0, 0);
		int[] normal = captureColumn(0);
		hover();
		assertFalse(Arrays.equals(normal, captureColumn(0)), "the simulated hover did not reach the tree");
	}

	@Test
	public void test_hoveredSelectedRowHasOneBackground() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setSelection(paintedItem);
		hover();
		assertArrayEquals(captureColumn(0), captureColumn(1));
		assertArrayEquals(captureColumn(0), captureColumn(2));
	}

	@Test
	public void test_hoveredSelectedRowReportsHotForAllColumns() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		Set<Integer> hotColumns = new TreeSet<>();
		paintedTree.addListener(SWT.EraseItem, event -> {
			if (event.item == paintedItem && (event.detail & SWT.HOT) != 0) {
				hotColumns.add(event.index);
			}
		});
		paintedTree.setSelection(paintedItem);
		hover();
		paintedTree.redraw();
		paintedTree.update();
		assertEquals(allColumns(), hotColumns);
		assertArrayEquals(captureColumn(0), captureColumn(1));
	}

	@Test
	public void test_clearingHotAppliesToTheWholeRow() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.addListener(SWT.EraseItem, event -> event.detail &= ~SWT.HOT);
		paintedTree.setSelection(paintedItem);
		int[] selected = captureColumn(1);
		hover();
		assertArrayEquals(selected, captureColumn(0));
		assertArrayEquals(selected, captureColumn(1));
	}

	@Test
	public void test_hoveredRowWithEraseItemListenerHasOneBackground() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.addListener(SWT.EraseItem, event -> {});
		hover();
		assertArrayEquals(captureColumn(0), captureColumn(1));
		assertArrayEquals(captureColumn(0), captureColumn(2));
	}

	@Test
	public void test_unfocusedSelectedRowHasOneBackground() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		assertNotEquals(paintedTree.handle, OS.GetFocus(), "the tree kept the focus");
		assertArrayEquals(captureColumn(0), captureColumn(1));
		hover();
		assertArrayEquals(captureColumn(0), captureColumn(1));
	}

	@Test
	public void test_reorderedColumnsHaveOneBackground() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.addListener(SWT.EraseItem, event -> {});
		paintedTree.setColumnOrder(new int[] { 2, 0, 1 });
		paintedTree.setSelection(paintedItem);
		hover();
		int[] rowEnd = capture(paintedTree.getClientArea().width - 5);
		assertArrayEquals(rowEnd, captureColumn(0));
		assertArrayEquals(rowEnd, captureColumn(1));
		assertArrayEquals(rowEnd, captureColumn(2));
	}

	@Test
	public void test_eraseItemListenerKeepsHoveredSelectionWithoutFullSelection() {
		openPaintedTree(SWT.MULTI);
		paintedItem.setText("text");
		int[] unselected = captureRowTop();
		paintedTree.setSelection(paintedItem);
		// The real pointer may rest on the row.
		OS.SendMessage(paintedTree.handle, OS.WM_MOUSELEAVE, 0, 0);
		int[] selected = captureRowTop();
		hover();
		int[] painted = captureRowTop();
		assertFalse(Arrays.equals(unselected, painted), "the selection was not painted");
		assertFalse(Arrays.equals(selected, painted), "the simulated hover did not reach the tree");
		paintedTree.addListener(SWT.EraseItem, event -> {});
		int[] withListener = captureRowTop();
		// When scaled, the paint paths draw the selection border differently and up to a pixel apart.
		int[] extent = selectionExtent(unselected, painted);
		int[] extentWithListener = selectionExtent(unselected, withListener);
		assertEquals(extent[0], extentWithListener[0], 1);
		assertEquals(extent[1], extentWithListener[1], 1);
		assertArrayEquals(Arrays.copyOfRange(painted, extent[0] + 3, extent[1] - 2), Arrays.copyOfRange(withListener, extent[0] + 3, extent[1] - 2));
	}

	/** Returns the first and last index at which the row differs from the unselected row. */
	private static int[] selectionExtent(int[] unselected, int[] row) {
		int first = 0;
		while (first < row.length - 1 && row[first] == unselected[first]) {
			first++;
		}
		int last = row.length - 1;
		while (last > first && row[last] == unselected[last]) {
			last--;
		}
		return new int[] { first, last };
	}

	@Test
	public void test_darkUnfocusedSelectionFillsTheRow() {
		assumeTrue(OS.IsDarkModeAvailable());
		display.setData(DARK_THEME_KEY, true);
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_darkUnfocusedSelectionFillsTheRowWithEraseItemListener() {
		assumeTrue(OS.IsDarkModeAvailable());
		display.setData(DARK_THEME_KEY, true);
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.addListener(SWT.EraseItem, event -> {});
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_darkFocusedSelectionIsNotTheUnfocusedFill() {
		assumeTrue(OS.IsDarkModeAvailable());
		display.setData(DARK_THEME_KEY, true);
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		paintedTree.setSelection(paintedItem);
		assumeTrue(OS.GetFocus() == paintedTree.handle);
		assertNotEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
	}

	@Test
	public void test_darkUnfocusedSelectionShowsOnLightBackground() {
		assumeTrue(OS.IsDarkModeAvailable());
		display.setData(DARK_THEME_KEY, true);
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setBackground(new Color(255, 255, 255));
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		RGB fill = new RGB(235, 235, 235);
		assertEquals(fill, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(fill, rgbAt(COLUMN_WIDTH + 10));
	}

	@Test
	public void test_lightThemeSelectionGetsNoFill() {
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		assertNotEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
	}

	@Test
	public void test_darkUnfocusedSelectionFillsTreeWithoutColumns() {
		assumeTrue(OS.IsDarkModeAvailable());
		display.setData(DARK_THEME_KEY, true);
		openPaintedTree(SWT.MULTI, 0);
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		paintedItem.setText("text");
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(paintedItem.getBounds().x + 1));
	}

	@Test
	public void test_themeSwitchAppliesToExistingTree() {
		assumeTrue(OS.IsDarkModeAvailable());
		openPaintedTree(SWT.FULL_SELECTION | SWT.MULTI);
		paintedTree.setSelection(paintedItem);
		focusTarget.setFocus();
		display.setData(DARK_THEME_KEY, true);
		paintedTree.setBackground(new Color(DARK_BACKGROUND));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH - 10));
		assertEquals(DARK_UNFOCUSED_SELECTION, rgbAt(COLUMN_WIDTH + 10));
	}

	private static Set<Integer> allColumns() {
		Set<Integer> columns = new TreeSet<>();
		for (int i = 0; i < COLUMNS; i++) {
			columns.add(i);
		}
		return columns;
	}

	/** Opens a focused tree without an EraseItem listener, because adding one changes how the tree paints. */
	private void openPaintedTree(int style) {
		openPaintedTree(style, COLUMNS);
	}

	private void openPaintedTree(int style, int columns) {
		paintedTree = new Tree(shell, style);
		paintedTree.setBounds(0, 30, 4 * COLUMN_WIDTH, 150);
		for (int i = 0; i < columns; i++) {
			new TreeColumn(paintedTree, SWT.NONE).setWidth(COLUMN_WIDTH);
		}
		for (int i = 0; i < 3; i++) {
			new TreeItem(paintedTree, SWT.NONE);
		}
		paintedItem = paintedTree.getItem(1);
		shell.setSize(5 * COLUMN_WIDTH, 250);
		shell.open();
		paintedTree.setFocus();
		while (display.readAndDispatch()) {
			// process all pending events
		}
		// The real pointer may rest on the row.
		OS.SendMessage(paintedTree.handle, OS.WM_MOUSELEAVE, 0, 0);
	}

	/** Hovers the item's label with a message, so that the real mouse pointer is not involved. Without FULL_SELECTION only the label counts as the item. */
	private void hover() {
		RECT label = paintedItem.getBounds(0, true, false, false);
		OS.SendMessage(paintedTree.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM((label.left + label.right) / 2, (label.top + label.bottom) / 2));
	}

	/** Returns the pixels of the hovered item's row, from top to bottom, in the middle of a column. */
	private int[] captureColumn(int column) {
		return capture(COLUMN_WIDTH * column + COLUMN_WIDTH - 10);
	}

	private int[] capture(int x) {
		ImageData data = captureTree();
		Rectangle bounds = paintedItem.getBounds(0);
		int[] pixels = new int[bounds.height];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = data.getPixel(x, bounds.y + i);
		}
		return pixels;
	}

	/** Returns the color of the painted item's row at x, just above the text. */
	private RGB rgbAt(int x) {
		ImageData data = captureTree();
		return data.palette.getRGB(data.getPixel(x, paintedItem.getBounds(0).y + 1));
	}

	/** Returns the pixels across the whole row, just above the text. */
	private int[] captureRowTop() {
		ImageData data = captureTree();
		int[] pixels = new int[data.width];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = data.getPixel(i, paintedItem.getBounds(0).y + 1);
		}
		return pixels;
	}

	private ImageData captureTree() {
		// No events are dispatched here: the WM_MOUSELEAVE of the simulated hover would end it.
		paintedTree.redraw();
		paintedTree.update();
		Rectangle clientArea = paintedTree.getClientArea();
		Image image = new Image(display, clientArea.width, clientArea.height);
		try {
			GC gc = new GC(paintedTree);
			try {
				gc.copyArea(image, 0, 0);
			} finally {
				gc.dispose();
			}
			return image.getImageData();
		} finally {
			image.dispose();
		}
	}
}

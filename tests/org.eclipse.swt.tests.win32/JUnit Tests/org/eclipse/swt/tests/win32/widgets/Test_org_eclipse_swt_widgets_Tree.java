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
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.tests.win32.SwtWin32TestUtil;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Pins the selected and hot state that a multi-column Tree reports in EraseItem and paints. */
public class Test_org_eclipse_swt_widgets_Tree {

	private static final int COLUMNS = 3;
	private static final int PAINT_TIMEOUT_MS = 2000;
	private static final int COLUMN_WIDTH = 120;

	private record Erase(TreeItem item, int column, int detail) {
	}

	private final List<Erase> erases = new ArrayList<>();
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
		shell.dispose();
	}

	@Test
	public void test_fullSelection_reportsSelectedInEveryColumn() throws InterruptedException {
		Tree tree = createOpenTree(SWT.FULL_SELECTION);
		TreeItem selected = tree.getItem(1);
		tree.setSelection(selected);

		paint(tree);

		assertEquals(allColumns(), columnsWith(selected, SWT.SELECTED));
		assertEquals(Set.of(), columnsWith(tree.getItem(0), SWT.SELECTED));
	}

	@Test
	public void test_noFullSelection_reportsSelectedOnlyInFirstColumn() throws InterruptedException {
		Tree tree = createOpenTree(SWT.NONE);
		TreeItem selected = tree.getItem(1);
		tree.setSelection(selected);

		paint(tree);

		assertEquals(Set.of(0), columnsWith(selected, SWT.SELECTED));
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
		// At 125% the selection edges of the two paint paths differ by a pixel.
		assumeTrue(DPIUtil.getZoomForAutoscaleProperty(paintedTree.getMonitor().getZoom()) == 100);
		paintedItem.setText("text");
		int[] unselected = captureRowTop();
		paintedTree.setSelection(paintedItem);
		hover();
		int[] painted = captureRowTop();
		assertFalse(Arrays.equals(unselected, painted), "the selection was not painted");
		paintedTree.addListener(SWT.EraseItem, event -> {});
		assertArrayEquals(painted, captureRowTop());
	}

	private Tree createOpenTree(int style) throws InterruptedException {
		Tree tree = new Tree(shell, style);
		tree.setBounds(0, 30, 330, 150);
		tree.setHeaderVisible(true);
		for (int i = 0; i < COLUMNS; i++) {
			TreeColumn column = new TreeColumn(tree, SWT.LEFT);
			column.setText("Column " + i);
			column.setWidth(100);
		}
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(new String[] { "a" + i, "b" + i, "c" + i });
		}
		tree.addListener(SWT.EraseItem, e -> erases.add(new Erase((TreeItem) e.item, e.index, e.detail)));
		shell.setSize(400, 300);
		shell.open();
		// The selected state of a focused item also depends on the DC colors, so keep the focus elsewhere.
		focusTarget.setFocus();
		SwtWin32TestUtil.processEvents(display, PAINT_TIMEOUT_MS, () -> !erases.isEmpty());
		return tree;
	}

	private void paint(Tree tree) throws InterruptedException {
		erases.clear();
		tree.redraw();
		tree.update();
		SwtWin32TestUtil.processEvents(display, PAINT_TIMEOUT_MS, () -> !erases.isEmpty());
		assertFalse(erases.isEmpty(), "Tree was not painted");
	}

	private Set<Integer> columnsWith(TreeItem item, int flag) {
		Set<Integer> columns = new TreeSet<>();
		for (Erase erase : erases) {
			if (erase.item() == item && (erase.detail() & flag) != 0) {
				columns.add(erase.column());
			}
		}
		return columns;
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
		paintedTree = new Tree(shell, style);
		paintedTree.setBounds(0, 30, 4 * COLUMN_WIDTH, 150);
		for (int i = 0; i < COLUMNS; i++) {
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
	}

	/** Hovers the item with a message, so that the real mouse pointer is not involved. */
	private void hover() {
		Rectangle bounds = paintedItem.getBounds(0);
		int zoom = DPIUtil.getZoomForAutoscaleProperty(paintedTree.getMonitor().getZoom());
		int x = DPIUtil.pointToPixel(bounds.x + bounds.width / 2, zoom);
		int y = DPIUtil.pointToPixel(bounds.y + bounds.height / 2, zoom);
		OS.SendMessage(paintedTree.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM(x, y));
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

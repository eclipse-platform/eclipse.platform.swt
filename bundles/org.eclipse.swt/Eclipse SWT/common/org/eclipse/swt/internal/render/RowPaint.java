/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.render;

import org.eclipse.swt.graphics.*;

/**
 * One row of a drawn plane, as its source (Tree, Table or List) lays it out: the row rectangle, the slots of the
 * first column (expander, check box), the cells in display order and the state bits. A plane reuses one instance
 * for every row of every frame; the arrays grow with the column count and are never shrunk.
 * <p>
 * Geometry is in device pixels of the plane and comes from the behaviour plane, so what is drawn is where the
 * widget answers hit tests and bounds. Colours are <code>0xRRGGBB</code>, or <code>-1</code> for the default.
 * </p>
 */
public final class RowPaint {
	/** State bits. */
	public static final int SELECTED = 1, CARET = 1 << 1, HOT = 1 << 2, EXPANDED = 1 << 3, CHILDREN = 1 << 4,
		CHECKED = 1 << 5, GRAYED = 1 << 6, CHECKBOX = 1 << 7, FULL_SELECTION = 1 << 8, FOCUSED = 1 << 9,
		FOCUS_CUES = 1 << 10, DROP = 1 << 11, GRID_LINES = 1 << 12, SYSTEM_SELECTION = 1 << 13,
		/* cell text wider than its box ends with "..." (a list view's cells) */
		ELLIPSIS = 1 << 14;

	/** The row (line) rectangle. */
	public int x, y, width, height;
	/** The selection rectangle on the row (x and width; the height is the row's). */
	public int selectionX, selectionWidth;
	/** The expander slot (width 0: none). */
	public int expanderX, expanderWidth;
	/** The check box slot (width 0: none). */
	public int checkX, checkWidth;
	/** State bits, see the constants. */
	public int state;
	/** Depth of a tree row (0 for roots and for Table and List rows), its indent step, and the row index. */
	public int depth, indent, index;
	/** Defaults of the widget for this frame. */
	public int foreground, background;
	public long font;
	/**
	 * Selection colours of a control the platform does not theme (a list box): with {@link #SYSTEM_SELECTION}, the
	 * native look fills the selection with these instead of drawing the selection part.
	 */
	public int systemSelectionBackground = -1, systemSelectionForeground = -1;
	/**
	 * Grid of a platform list view, drawn by the native look with {@link #GRID_LINES}: its line colour (-1: none; the
	 * theme's grid on each cell's last pixel, as the Tree draws its own) and its column detail colour (-1: none). A
	 * list view draws the column detail on a cell's last pixel, its column line one pixel right of the cell and the row
	 * line on the row's last pixel over both.
	 */
	public int listGrid = -1, listColumnDetail = -1;
	/** Cell of the sort column, or -1. */
	public int sortCell = -1;
	public int sortBackground = -1;
	/** Number of cells in this row (at least 1). */
	public int cells;
	/** Per cell, in display order: cell box, text box, image box. */
	public int [] cellX = new int [1], cellWidth = new int [1], textX = new int [1], textWidth = new int [1],
		imageX = new int [1], imageWidth = new int [1], align = new int [1];
	/** Per cell: text, image, font handle (or -1), foreground and background (or -1). */
	public String [] text = new String [1];
	public Image [] image = new Image [1];
	public long [] cellFont = new long [1];
	public int [] cellForeground = new int [1], cellBackground = new int [1];
	/** Scratch polygon points (x, y pairs) for looks that draw shapes, reused for every row. */
	public final int [] points = new int [16];

	/** Makes room for <code>count</code> cells and clears the per-cell values. */
	public void cells (int count) {
		if (cellX.length < count) {
			int n = Math.max (count, cellX.length * 2);
			cellX = new int [n];
			cellWidth = new int [n];
			textX = new int [n];
			textWidth = new int [n];
			imageX = new int [n];
			imageWidth = new int [n];
			align = new int [n];
			text = new String [n];
			image = new Image [n];
			cellFont = new long [n];
			cellForeground = new int [n];
			cellBackground = new int [n];
		}
		cells = count;
		for (int i = 0; i < count; i++) {
			text [i] = null;
			image [i] = null;
			cellFont [i] = -1;
			cellForeground [i] = cellBackground [i] = -1;
			imageWidth [i] = 0;
			align [i] = 0;
		}
	}

	/** True when a state bit is set. */
	public boolean is (int bit) {
		return (state & bit) != 0;
	}

	/** Converts a win32 <code>COLORREF</code> (<code>0x00BBGGRR</code>) to <code>0xRRGGBB</code>; -1 stays -1. */
	public static int rgbOf (int colorRef) {
		if (colorRef == -1) return -1;
		return (colorRef & 0xFF) << 16 | colorRef & 0xFF00 | colorRef >> 16 & 0xFF;
	}
}

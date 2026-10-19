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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Paints one row of a drawn plane (Tree, Table or List) through a {@link RowCanvas}, in a fixed order: row
 * background, cell backgrounds, selection, expander, check box, per cell image and text, grid lines, focus.
 * One renderer serves every row widget; the theme decides the colours and how selection, expanders and check
 * boxes look (platform parts, Swing Metal, or Initiative 31). The renderer holds no widget state; it reads the
 * {@link RowPaint} its plane filled.
 */
public final class RowRenderer {
	private final Theme theme;

	public RowRenderer (Theme theme) {
		this.theme = theme;
	}

	/** The theme this renderer paints with. */
	public Theme theme () {
		return theme;
	}

	/** Paints a row: the theme's row painter when it has one, else {@link #paintDefault}. */
	public void paint (RowCanvas canvas, RowPaint row) {
		if (theme.rowPainter != null) {
			theme.rowPainter.paint (canvas, row, theme, this);
		} else {
			paintDefault (canvas, row);
		}
	}

	/** Paints a row in the default order: background, selection, expander, check box, cells, grid lines, focus. */
	public void paintDefault (RowCanvas canvas, RowPaint row) {
		background (canvas, row);
		selection (canvas, row);
		expander (canvas, row);
		checkBox (canvas, row);
		for (int cell = 0; cell < row.cells; cell++) {
			if (theme.cellPainter != null) {
				theme.cellPainter.paint (canvas, row, cell, theme);
			} else {
				paintCell (canvas, row, cell);
			}
		}
		gridLines (canvas, row);
		focus (canvas, row);
	}

	/** Fills the area of a plane that has no row (below the last row). */
	public void paintEmpty (RowCanvas canvas, RowPaint frame, int x, int y, int width, int height) {
		canvas.fill (x, y, width, height, background (frame));
	}

	private int background (RowPaint row) {
		return theme.background != -1 ? theme.background : row.background;
	}

	private int foreground (RowPaint row) {
		return theme.foreground != -1 ? theme.foreground : row.foreground;
	}

	private void background (RowCanvas canvas, RowPaint row) {
		canvas.fill (row.x, row.y, row.width, row.height, background (row));
		for (int cell = 0; cell < row.cells; cell++) {
			int color = row.cellBackground [cell];
			if (color == -1 && cell == row.sortCell) color = row.sortBackground;
			if (color != -1) canvas.fill (row.cellX [cell], row.y, row.cellWidth [cell], row.height, color);
		}
	}

	private void selection (RowCanvas canvas, RowPaint row) {
		boolean selected = row.is (RowPaint.SELECTED) || row.is (RowPaint.DROP), hot = row.is (RowPaint.HOT);
		if (!selected && !hot || row.selectionWidth <= 0) return;
		if (theme.parts && row.is (RowPaint.SYSTEM_SELECTION)) {
			if (selected) canvas.fill (row.selectionX, row.y, row.selectionWidth, row.height, row.systemSelectionBackground);
		} else if (theme.parts) {
			int state = selected ? hot ? RowCanvas.SELECTION_HOT_SELECTED : row.is (RowPaint.FOCUSED) ? RowCanvas.SELECTION_SELECTED : RowCanvas.SELECTION_NO_FOCUS : RowCanvas.SELECTION_HOT;
			canvas.part (RowCanvas.PART_SELECTION, state, row.selectionX, row.y, row.selectionWidth, row.height);
		} else {
			/* Initiative 31 paints the selection over the hover; Swing has no hover */
			int color = selected ? row.is (RowPaint.FOCUSED) ? theme.selectionBackground : theme.selectionInactiveBackground : theme.hotBackground;
			canvas.fill (row.selectionX, row.y, row.selectionWidth, row.height, color);
		}
	}

	private void expander (RowCanvas canvas, RowPaint row) {
		if (!row.is (RowPaint.CHILDREN) || row.expanderWidth <= 0) return;
		boolean open = row.is (RowPaint.EXPANDED);
		if (theme.expanderOpen != null) {
			glyph (canvas, open ? theme.expanderOpen : theme.expanderClosed, row.expanderX, row.y, row.expanderWidth, row.height);
			return;
		}
		switch (theme.look) {
			case Theme.LOOK_NATIVE -> {
				int state = (open ? RowCanvas.EXPANDER_OPEN : RowCanvas.EXPANDER_CLOSED) | (row.is (RowPaint.HOT) ? RowCanvas.HOT : 0);
				canvas.part (RowCanvas.PART_EXPANDER, state, row.expanderX, row.y, row.expanderWidth, row.height);
			}
			case Theme.LOOK_INITIATIVE31 -> Initiative31Look.expander (canvas, theme, row, open);
			default -> metalHandle (canvas, row, open);
		}
	}

	/* Metal handle: a 9 x 9 box with a minus (open) or a plus (closed), and the line to the label */
	private void metalHandle (RowCanvas canvas, RowPaint row, boolean open) {
		int size = 9, cx = row.expanderX + row.expanderWidth / 2, cy = row.y + row.height / 2;
		int left = cx - size / 2, top = cy - size / 2;
		canvas.fill (cx + size / 2 + 1, cy, Math.max (0, row.expanderX + row.expanderWidth - (cx + size / 2 + 1)), 1, theme.hash);
		canvas.fill (left, top, size, size, theme.border);
		canvas.fill (left + 1, top + 1, size - 2, size - 2, 0xFFFFFF);
		canvas.fill (left + 2, cy, size - 4, 1, theme.foreground);
		if (!open) canvas.fill (cx, top + 2, 1, size - 4, theme.foreground);
	}

	private void checkBox (RowCanvas canvas, RowPaint row) {
		if (!row.is (RowPaint.CHECKBOX) || row.checkWidth <= 0) return;
		boolean checked = row.is (RowPaint.CHECKED), grayed = row.is (RowPaint.GRAYED);
		if (theme.checkOn != null) {
			glyph (canvas, checked ? grayed ? theme.checkMixed : theme.checkOn : theme.checkOff, row.checkX, row.y, row.checkWidth, row.height);
			return;
		}
		switch (theme.look) {
			case Theme.LOOK_NATIVE -> {
				int state = checked ? grayed ? RowCanvas.CHECKBOX_MIXED : RowCanvas.CHECKBOX_CHECKED : RowCanvas.CHECKBOX_UNCHECKED;
				canvas.part (RowCanvas.PART_CHECKBOX, state, row.checkX, row.y, row.checkWidth, row.height);
			}
			case Theme.LOOK_INITIATIVE31 -> Initiative31Look.checkBox (canvas, theme, row, checked, grayed);
			default -> metalCheckBox (canvas, row, checked, grayed);
		}
	}

	/* Metal check box: 13 x 13, border, white, a check mark of two strokes */
	private void metalCheckBox (RowCanvas canvas, RowPaint row, boolean checked, boolean grayed) {
		int size = Math.min (13, Math.min (row.checkWidth, row.height) - 2);
		int left = row.checkX + (row.checkWidth - size) / 2, top = row.y + (row.height - size) / 2;
		canvas.fill (left, top, size, size, theme.border);
		canvas.fill (left + 1, top + 1, size - 2, size - 2, grayed ? 0xEEEEEE : 0xFFFFFF);
		if (!checked) return;
		int mark = grayed ? 0x808080 : theme.checkFill;
		for (int i = 0; i < 3; i++) canvas.fill (left + 3 + i, top + size / 2 + i - 1, 1, 2, mark);
		for (int i = 0; i < size - 7; i++) canvas.fill (left + 6 + i, top + size / 2 + 1 - i, 1, 2, mark);
	}

	/* a theme glyph image, at its own size, centred in its slot */
	private static void glyph (RowCanvas canvas, ImageData image, int x, int y, int width, int height) {
		canvas.imageData (image, x + (width - image.width) / 2, y + (height - image.height) / 2, image.width, image.height);
	}

	/** Paints the default content of a cell: its image, then its text (for cell painters that add to it). */
	public void paintCell (RowCanvas canvas, RowPaint row, int cell) {
		if (row.image [cell] != null && row.imageWidth [cell] > 0) {
			canvas.image (row.image [cell], row.imageX [cell], row.y, row.imageWidth [cell], row.height);
		}
		String text = row.text [cell];
		if (text == null || text.isEmpty () || row.textWidth [cell] <= 0) return;
		int color = row.cellForeground [cell] != -1 ? row.cellForeground [cell] : foreground (row);
		if (theme.parts && row.is (RowPaint.SYSTEM_SELECTION) && row.is (RowPaint.SELECTED)) color = row.systemSelectionForeground;
		if (theme.selectionForeground != -1 && row.is (RowPaint.SELECTED)) color = theme.selectionForeground;
		long font = row.cellFont [cell] != -1 ? row.cellFont [cell] : row.font;
		int align = row.align [cell] == 0 ? SWT.LEFT : row.align [cell];
		int inset = align == SWT.LEFT ? theme.textInset : 0;
		canvas.text (text, font, color, row.textX [cell] + inset, row.y, row.textWidth [cell] - inset, row.height,
			align | (row.is (RowPaint.ELLIPSIS) ? RowCanvas.ELLIPSIS : 0));
	}

	private void gridLines (RowCanvas canvas, RowPaint row) {
		if (!row.is (RowPaint.GRID_LINES)) return;
		if (theme.parts && row.listGrid != -1) {
			listGridLines (canvas, row);
			return;
		}
		for (int cell = 0; cell < row.cells; cell++) {
			canvas.fill (row.cellX [cell] + row.cellWidth [cell] - 1, row.y, 1, row.height, theme.grid);
		}
		canvas.fill (row.x, row.y + row.height - 1, row.width, 1, theme.grid);
	}

	/* the native look of a list view's grid: the column detail on each cell's last pixel, the column line one pixel right of the cell, the row line over both */
	private static void listGridLines (RowCanvas canvas, RowPaint row) {
		for (int cell = 0; cell < row.cells; cell++) {
			int right = row.cellX [cell] + row.cellWidth [cell];
			if (row.listColumnDetail != -1) canvas.fill (right - 1, row.y, 1, row.height, row.listColumnDetail);
			canvas.fill (right, row.y, 1, row.height, row.listGrid);
		}
		canvas.fill (row.x, row.y + row.height - 1, row.width, 1, row.listGrid);
	}

	private void focus (RowCanvas canvas, RowPaint row) {
		if (theme.look == Theme.LOOK_INITIATIVE31) {
			/* Initiative 31 draws the focus on every selected row of the focus control */
			if (row.is (RowPaint.SELECTED) && row.is (RowPaint.FOCUSED) && row.selectionWidth > 0) canvas.focus (row.selectionX, row.y, row.selectionWidth, row.height);
			return;
		}
		if (!row.is (RowPaint.CARET) || !row.is (RowPaint.FOCUSED) || !row.is (RowPaint.FOCUS_CUES) || row.selectionWidth <= 0) return;
		if (theme.parts) {
			canvas.focus (row.selectionX, row.y, row.selectionWidth, row.height);
		} else {
			int x = row.selectionX, y = row.y, w = row.selectionWidth, h = row.height, c = theme.focusColor;
			canvas.fill (x, y, w, 1, c);
			canvas.fill (x, y + h - 1, w, 1, c);
			canvas.fill (x, y, 1, h, c);
			canvas.fill (x + w - 1, y, 1, h, c);
		}
	}
}

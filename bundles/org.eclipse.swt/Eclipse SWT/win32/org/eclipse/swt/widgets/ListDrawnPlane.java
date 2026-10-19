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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The drawn plane of a win32 List in the <code>custom</code>, <code>m3</code> and <code>gpu</code> modes
 * (docs/m3/RENDER_MODES.md; see {@link DrawnPlane}). The list box is the behaviour plane; its entries hold the
 * resident window. A row is one cell: the entry's text (the item store's text for an owner-data list), its
 * selection and the caret. A list box is not themed by the platform, so the native look fills the selection with
 * the system highlight colours, as the list box does.
 */
final class ListDrawnPlane extends DrawnPlane {
	/** The List's startup mode, read once: a drawn plane exists only for <code>custom</code>, <code>m3</code> and <code>gpu</code>. */
	static final WidgetMode MODE = WidgetMode.of ("List", WidgetMode.VIEWPORT, WidgetMode.CUSTOM, WidgetMode.M3, WidgetMode.GPU);
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.List");

	final List list;
	private final RECT bounds = new RECT ();
	private char [] text = new char [64];

	private ListDrawnPlane (List list) {
		super (list, MODE, "List", List.ListProc, Display.TREEVIEW);
		this.list = list;
		LOG.debug (() -> "List drawn plane: mode " + MODE + ", theme " + renderer.theme ().name + ", backend " + canvas.getClass ().getSimpleName ());
	}

	/** The drawn plane of a new List, or null when the List's mode does not draw (viewport, original). */
	static ListDrawnPlane install (List list) {
		return drawn (MODE) ? new ListDrawnPlane (list) : null;
	}

	@Override
	void drawRows (long hwnd) {
		row.foreground = RowPaint.rgbOf (list.getForegroundPixel ());
		row.background = RowPaint.rgbOf (list.getBackgroundPixel ());
		row.font = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
		row.systemSelectionBackground = RowPaint.rgbOf (OS.GetSysColor (OS.COLOR_HIGHLIGHT));
		row.systemSelectionForeground = RowPaint.rgbOf (OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT));
		row.sortCell = -1;
		int count = (int) OS.SendMessage (hwnd, OS.LB_GETCOUNT, 0, 0);
		int top = (int) OS.SendMessage (hwnd, OS.LB_GETTOPINDEX, 0, 0);
		int bottom = damage.top;
		if (count > 0 && top >= 0 && OS.SendMessage (hwnd, OS.LB_GETITEMRECT, top, bounds) != OS.LB_ERR) {
			int height = Math.max (1, bounds.bottom - bounds.top);
			int caret = (int) OS.SendMessage (hwnd, OS.LB_GETCARETINDEX, 0, 0);
			boolean single = (list.style & SWT.SINGLE) != 0;
			int current = single ? (int) OS.SendMessage (hwnd, OS.LB_GETCURSEL, 0, 0) : -1;
			int common = commonState (hwnd);
			for (int index = top + Math.max (0, (damage.top - bounds.top) / height); index < count; index++) {
				if (OS.SendMessage (hwnd, OS.LB_GETITEMRECT, index, bounds) == OS.LB_ERR || bounds.top >= damage.bottom) break;
				boolean selected = single ? index == current : OS.SendMessage (hwnd, OS.LB_GETSEL, index, 0) > 0;
				fillRow (hwnd, index, common | (selected ? RowPaint.SELECTED : 0) | (index == caret ? RowPaint.CARET : 0));
				renderer.paint (canvas, row);
				bottom = bounds.bottom;
			}
		}
		if (bottom < damage.bottom) renderer.paintEmpty (canvas, row, damage.left, bottom, damage.right - damage.left, damage.bottom - bottom);
	}

	private int commonState (long hwnd) {
		int state = RowPaint.SYSTEM_SELECTION | RowPaint.FULL_SELECTION;
		if (OS.GetFocus () == hwnd) state |= RowPaint.FOCUSED;
		if ((OS.SendMessage (hwnd, OS.WM_QUERYUISTATE, 0, 0) & OS.UISF_HIDEFOCUS) == 0) state |= RowPaint.FOCUS_CUES;
		return state;
	}

	private void fillRow (long hwnd, int index, int state) {
		row.x = bounds.left;
		row.y = bounds.top;
		row.width = Math.max (bounds.right, client.right) - bounds.left;
		row.height = bounds.bottom - bounds.top;
		row.index = index;
		row.state = state;
		row.expanderWidth = row.checkWidth = 0;
		row.cells (1);
		row.cellX [0] = row.selectionX = bounds.left;
		row.cellWidth [0] = row.selectionWidth = bounds.right - bounds.left;
		/* the renderer insets text by two pixels, where the list box draws it */
		row.textX [0] = bounds.left;
		row.textWidth [0] = Math.max (0, bounds.right - row.textX [0]);
		row.text [0] = text (hwnd, index);
	}

	/* the entry's text: the item store's for an owner-data list box, else the list box's own */
	private String text (long hwnd, int index) {
		if (list.itemStore != null) return list.itemStore.getItem (index);
		int length = (int) OS.SendMessage (hwnd, OS.LB_GETTEXTLEN, index, 0);
		if (length <= 0) return null;
		if (text.length < length + 1) text = new char [Math.max (length + 1, text.length * 2)];
		OS.SendMessage (hwnd, OS.LB_GETTEXT, index, text);
		return new String (text, 0, length);
	}
}

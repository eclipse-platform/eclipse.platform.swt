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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The drawn plane of a win32 Tree in the <code>custom</code>, <code>skija</code> and <code>gpu</code> modes
 * (docs/m3/RENDER_MODES.md, stage 1).
 * <p>
 * The Tree keeps its viewport (or, for a plain Tree, its native rows) exactly as in the <code>viewport</code>
 * mode: the TreeView is the behaviour plane that holds the resident window, takes input, owns the caret and
 * answers geometry. It never paints to the screen. Each <code>WM_PAINT</code> and <code>WM_PRINTCLIENT</code>
 * first lets the TreeView paint the damage into the back buffer, for its side effects only (SetData of the rows
 * coming into view, label measurement, custom draw), and then the drawing core paints the same damage over it:
 * the rows of the resident window that intersect the damage, laid out with the TreeView's own geometry.
 * </p>
 */
final class TreeDrawnPlane {
	/** The Tree's startup mode, read once: a drawn plane exists only for <code>custom</code>, <code>skija</code> and <code>gpu</code>. */
	static final WidgetMode MODE = WidgetMode.of ("Tree", WidgetMode.VIEWPORT, WidgetMode.CUSTOM, WidgetMode.M3, WidgetMode.GPU);
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.Tree");
	private static final int TVM_GETINDENT = 0x1106, I_CHILDRENCALLBACK = -1;

	final Tree tree;
	private final BackBuffer buffer = new BackBuffer ();
	private final BufferCanvas canvas;
	private final RowRenderer renderer = new RowRenderer (RowTheme.of ("Tree"));
	private final RowPaint row = new RowPaint ();
	private final PAINTSTRUCT ps = new PAINTSTRUCT ();
	private final RECT damage = new RECT (), client = new RECT (), line = new RECT (), label = new RECT ();
	private final TVITEM tvItem = new TVITEM ();
	/* first-column slots left of the label, measured once by native hit testing (see measureSlots) */
	private long slotKey = Long.MIN_VALUE;
	private int expanderLeft, expanderRight, checkLeft, checkRight;
	private boolean expanderKnown, checkKnown;

	private TreeDrawnPlane (Tree tree) {
		this.tree = tree;
		canvas = RowBackends.create (MODE, "Tree", tree.handle, Display.TREEVIEW, tree::getAutoscalingZoom, hdc -> {
			GCData data = new GCData ();
			data.device = tree.display;
			return tree.createNewGC (hdc, data);
		});
		LOG.debug (() -> "Tree drawn plane: mode " + MODE + ", theme " + renderer.theme ().name + ", backend " + canvas.getClass ().getSimpleName ());
	}

	/** The drawn plane of a new Tree, or null when the Tree's mode does not draw (viewport, original). */
	static TreeDrawnPlane install (Tree tree) {
		if (MODE != WidgetMode.CUSTOM && MODE != WidgetMode.M3 && MODE != WidgetMode.GPU) return null;
		if (TreeViewport.nativeReference) return null;
		return new TreeDrawnPlane (tree);
	}

	/** True for the messages this plane paints: the TreeView's paint and print-client. */
	boolean paints (long hwnd, int msg) {
		return hwnd == tree.handle && (msg == OS.WM_PAINT || msg == OS.WM_PRINTCLIENT);
	}

	/**
	 * Paints the damage of a <code>WM_PAINT</code> (with or without a device context) or a
	 * <code>WM_PRINTCLIENT</code>: behaviour pass into the back buffer, drawn pass over it, present.
	 */
	long paint (long hwnd, int msg, long wParam, long lParam) {
		boolean own = msg == OS.WM_PAINT && wParam == 0;
		long target = own ? OS.BeginPaint (hwnd, ps) : wParam;
		try {
			OS.GetClientRect (hwnd, client);
			if (own) {
				OS.SetRect (damage, ps.left, ps.top, ps.right, ps.bottom);
			} else {
				OS.GetClipBox (target, damage);
			}
			if (!OS.IntersectRect (damage, damage, client)) return 0;
			buffer.ensure (target, client.right, client.bottom);
			behaviour (hwnd, msg, lParam);
			if (tree.isDisposed ()) return 0;
			draw (hwnd);
			buffer.present (target, damage.left, damage.top, damage.right - damage.left, damage.bottom - damage.top);
		} finally {
			if (own) OS.EndPaint (hwnd, ps);
		}
		return 0;
	}

	/* the TreeView paints the damage into the back buffer: SetData, label measurement and custom draw happen as natively */
	private void behaviour (long hwnd, int msg, long lParam) {
		long dc = buffer.dc;
		int saved = OS.SaveDC (dc);
		OS.IntersectClipRect (dc, damage.left, damage.top, damage.right, damage.bottom);
		if (msg == OS.WM_PRINTCLIENT) {
			OS.CallWindowProc (Tree.TreeProc, hwnd, OS.WM_PRINTCLIENT, dc, lParam);
		} else {
			OS.CallWindowProc (Tree.TreeProc, hwnd, OS.WM_PAINT, dc, 0);
		}
		OS.RestoreDC (dc, saved);
	}

	/* the drawing core paints the rows of the resident window that intersect the damage */
	private void draw (long hwnd) {
		canvas.target (buffer);
		canvas.begin (damage.left, damage.top, damage.right - damage.left, damage.bottom - damage.top);
		try {
			frameDefaults (hwnd);
			int bottom = damage.top;
			long hItem = firstDamagedRow (hwnd);
			long caret = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			while (hItem != 0) {
				if (!OS.TreeView_GetItemRect (hwnd, hItem, line, false) || line.top >= damage.bottom) break;
				fillRow (hwnd, hItem, caret);
				renderer.paint (canvas, row);
				bottom = line.bottom;
				hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
			}
			if (bottom < damage.bottom) renderer.paintEmpty (canvas, row, damage.left, bottom, damage.right - damage.left, damage.bottom - bottom);
		} finally {
			canvas.end ();
		}
	}

	private void frameDefaults (long hwnd) {
		row.foreground = RowPaint.rgbOf (tree.getForegroundPixel ());
		row.background = RowPaint.rgbOf (tree.getBackgroundPixel ());
		row.font = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
		row.sortBackground = tree.sortColumn != null && tree.sortDirection != SWT.NONE ? RowPaint.rgbOf (tree.getSortColumnPixel ()) : -1;
	}

	/* the first row at or below the damage's top: the rows have one height, so it is arithmetic from the first visible row */
	private long firstDamagedRow (long hwnd) {
		long hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
		if (hItem == 0 || !OS.TreeView_GetItemRect (hwnd, hItem, line, false)) return hItem;
		int height = Math.max (1, line.bottom - line.top);
		for (int skip = Math.max (0, (damage.top - line.top) / height); skip > 0 && hItem != 0; skip--) {
			hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
		}
		return hItem;
	}

	private void fillRow (long hwnd, long hItem, long caret) {
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE | OS.TVIF_PARAM | OS.TVIF_CHILDREN;
		tvItem.stateMask = OS.TVIS_SELECTED | OS.TVIS_EXPANDED | OS.TVIS_STATEIMAGEMASK | OS.TVIS_DROPHILITED;
		tvItem.hItem = hItem;
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		int id = (int) tvItem.lParam;
		/* read the facade only: no facade is created and no SetData is sent while drawing */
		TreeItem item = id >= 0 && tree.items != null && id < tree.items.length ? tree.items [id] : null;
		OS.TreeView_GetItemRect (hwnd, hItem, label, true);
		row.x = line.left;
		row.y = line.top;
		row.width = line.right - line.left;
		row.height = line.bottom - line.top;
		row.state = state (hwnd, hItem, caret);
		measureSlots (hwnd, hItem);
		row.expanderX = label.left + expanderLeft;
		row.expanderWidth = expanderKnown ? expanderRight - expanderLeft : 0;
		row.checkX = label.left + checkLeft;
		row.checkWidth = checkKnown ? checkRight - checkLeft : 0;
		if (tree.hwndHeader != 0 && tree.columnCount > 0) {
			columns (item);
		} else {
			firstColumnOnly (item);
		}
	}

	private int state (long hwnd, long hItem, long caret) {
		int state = 0, bits = tvItem.state;
		if ((bits & OS.TVIS_SELECTED) != 0) state |= RowPaint.SELECTED;
		if ((bits & OS.TVIS_DROPHILITED) != 0) state |= RowPaint.DROP;
		if ((bits & OS.TVIS_EXPANDED) != 0) state |= RowPaint.EXPANDED;
		int children = tvItem.cChildren;
		if (children > 0 || (children == OS.I_CHILDRENAUTO || children == I_CHILDRENCALLBACK) && OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem) != 0) {
			state |= RowPaint.CHILDREN;
		}
		if (hItem == caret) state |= RowPaint.CARET;
		if (OS.GetFocus () == hwnd) state |= RowPaint.FOCUSED;
		if ((OS.SendMessage (hwnd, OS.WM_QUERYUISTATE, 0, 0) & OS.UISF_HIDEFOCUS) == 0) state |= RowPaint.FOCUS_CUES;
		if ((tree.style & SWT.FULL_SELECTION) != 0) state |= RowPaint.FULL_SELECTION;
		if (tree.linesVisible && tree.hwndHeader != 0 && tree.columnCount > 0) state |= RowPaint.GRID_LINES;
		if ((tree.style & SWT.CHECK) != 0) {
			int image = (bits & OS.TVIS_STATEIMAGEMASK) >> 12;
			state |= RowPaint.CHECKBOX;
			if (image == 2 || image == 4) state |= RowPaint.CHECKED;
			if (image >= 3) state |= RowPaint.GRAYED;
		}
		return state;
	}

	/* a Tree without columns: one cell, the label */
	private void firstColumnOnly (TreeItem item) {
		row.cells (1);
		row.sortCell = -1;
		row.cellX [0] = label.left;
		row.cellWidth [0] = label.right - label.left;
		row.textX [0] = label.left;
		row.textWidth [0] = label.right - label.left;
		row.align [0] = SWT.LEFT;
		row.selectionX = row.is (RowPaint.FULL_SELECTION) ? row.x : label.left;
		row.selectionWidth = row.is (RowPaint.FULL_SELECTION) ? row.width : label.right - label.left;
		if (item == null) return;
		row.text [0] = item.text;
		cellStyle (item, 0, 0);
		image (item, 0, 0, item.image);
	}

	/* a Tree with columns: one cell per column in display order, boxes from the Tree's own geometry */
	private void columns (TreeItem item) {
		int count = tree.columnCount;
		row.cells (count);
		row.sortCell = -1;
		int right = row.x;
		for (int order = 0; order < count; order++) {
			int index = (int) OS.SendMessage (tree.hwndHeader, OS.HDM_ORDERTOINDEX, order, 0);
			RECT header = tree.getColumnRect (index);
			if (header == null) continue;
			row.cellX [order] = header.left;
			row.cellWidth [order] = header.right - header.left;
			right = Math.max (right, header.right);
			row.align [order] = tree.columns [index].style & (SWT.LEFT | SWT.CENTER | SWT.RIGHT);
			if (tree.columns [index] == tree.sortColumn) row.sortCell = order;
			if (index == 0) {
				row.textX [order] = label.left;
				row.textWidth [order] = Math.min (label.right, header.right) - label.left;
			} else {
				row.textX [order] = header.left;
				row.textWidth [order] = header.right - header.left;
			}
			if (item == null) continue;
			row.text [order] = index == 0 ? item.text : item.strings != null ? item.strings [index] : null;
			cellStyle (item, index, order);
			image (item, index, order, index == 0 ? item.image : item.images != null ? item.images [index] : null);
		}
		boolean full = row.is (RowPaint.FULL_SELECTION);
		row.selectionX = full ? row.x : label.left;
		row.selectionWidth = full ? right - row.x : label.right - label.left;
	}

	private void cellStyle (TreeItem item, int index, int cell) {
		int foreground = item.cellForeground != null ? item.cellForeground [index] : -1;
		int background = item.cellBackground != null ? item.cellBackground [index] : -1;
		row.cellForeground [cell] = RowPaint.rgbOf (foreground != -1 ? foreground : item.foreground);
		row.cellBackground [cell] = RowPaint.rgbOf (background != -1 ? background : item.background);
		row.cellFont [cell] = item.fontHandle (index);
	}

	private void image (TreeItem item, int index, int cell, Image image) {
		if (image == null) return;
		RECT box = item.getBounds (index, false, true, false, false, true, 0);
		row.image [cell] = image;
		row.imageX [cell] = box.left;
		row.imageWidth [cell] = box.right - box.left;
		if (index != 0) {
			/* the text follows the image in a column cell */
			int end = row.textX [cell] + row.textWidth [cell];
			row.textX [cell] = box.right + Tree.INSET;
			row.textWidth [cell] = Math.max (0, end - row.textX [cell]);
		}
	}

	/*
	 * The expander and check box slots left of the label, measured by native hit testing (TVHT_ONITEMBUTTON,
	 * TVHT_ONITEMSTATEICON) as offsets from the label's left edge. They depend only on the item height, the
	 * indent and the image lists, so they are measured again only when one of those changes.
	 */
	private void measureSlots (long hwnd, long hItem) {
		long images = OS.SendMessage (hwnd, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		long states = OS.SendMessage (hwnd, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
		int indent = (int) OS.SendMessage (hwnd, TVM_GETINDENT, 0, 0);
		long key = (images * 31 + states) * 31 + ((long) indent << 16 | row.height);
		if (key != slotKey) {
			slotKey = key;
			expanderKnown = checkKnown = false;
		}
		boolean needExpander = !expanderKnown && row.is (RowPaint.CHILDREN), needCheck = !checkKnown && row.is (RowPaint.CHECKBOX);
		if (!needExpander && !needCheck) return;
		TVHITTESTINFO hit = new TVHITTESTINFO ();
		hit.y = row.y + row.height / 2;
		int from = Math.max (0, label.left - 4 * Math.max (indent, row.height) - 64), buttonFrom = -1, buttonTo = -1, stateFrom = -1, stateTo = -1;
		for (int x = from; x < label.left; x++) {
			hit.x = x;
			hit.flags = 0;
			if (OS.SendMessage (hwnd, OS.TVM_HITTEST, 0, hit) != hItem) continue;
			if ((hit.flags & OS.TVHT_ONITEMBUTTON) != 0) {
				if (buttonFrom < 0) buttonFrom = x;
				buttonTo = x + 1;
			} else if ((hit.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
				if (stateFrom < 0) stateFrom = x;
				stateTo = x + 1;
			}
		}
		if (needExpander && buttonFrom >= 0) {
			expanderLeft = buttonFrom - label.left;
			expanderRight = buttonTo - label.left;
			expanderKnown = true;
		}
		if (needCheck && stateFrom >= 0) {
			checkLeft = stateFrom - label.left;
			checkRight = stateTo - label.left;
			checkKnown = true;
		}
		LOG.trace (() -> "Tree drawn plane slots: expander " + expanderLeft + ".." + expanderRight + " (" + expanderKnown + "), check " + checkLeft + ".." + checkRight + " (" + checkKnown + ")");
	}

	/** Frees the back buffer and the backend. */
	void release () {
		buffer.release ();
		canvas.dispose ();
	}
}

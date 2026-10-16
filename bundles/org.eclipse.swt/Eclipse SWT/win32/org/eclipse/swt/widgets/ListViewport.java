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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Viewport of a win32 List (a List without <code>SWT.VIRTUAL</code>).
 * <p>
 * The List keeps its logical rows in {@link VirtualListModel} (text, widest item, selection)
 * and answers its public API from it. The native list box is an ordinary string list box that
 * holds only the resident window: the rows on screen and the overscan
 * ({@link VirtualRowWindowPlan}). It is created without <code>WS_VSCROLL</code>, so it never
 * writes the vertical bar; the viewport sets that bar to the logical rows
 * ({@link #updateBar()}). The list box scrolls natively inside its window
 * ({@link #positionPlane()}), so its pixels are the stock ones.
 * </p><p>
 * The native rules reproduced here (top-row clamp, caret and anchor after inserts and
 * removes, scrolling of the selection, scroll-bar values) are measured on the stock list box,
 * see <code>docs/m3/LIST_MEDIATED_VIEWPORT.md</code>.
 * </p><p>
 * Only window-bounded native work runs here: inserting, deleting and selecting at most one
 * window of native rows per change.
 * </p>
 */
final class ListViewport {
	/** The viewport is the default List mode; <code>-Dswt.widget.mode.List=original</code> (or the global <code>swt.widget.mode</code>) selects the native List at startup. */
	static final boolean ENABLED = WidgetMode.of ("List", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;
	/** The documented LB_SETANCHORINDEX (OS.LB_SETANCHORINDEX is not this message). */
	static final int LB_SETANCHORINDEX = 0x19c;
	static final int MIN_OVERSCAN = 16;
	private static final System.Logger LOG = System.getLogger ("org.eclipse.swt.widgets.List");

	final List list;
	final VirtualListModel model;
	private final VirtualRowWindowPlan plan = new VirtualRowWindowPlan ();
	private final boolean single, hScroll;
	/* logical top row, caret and anchor (native semantics: the anchor may be -1) */
	private int top, caret, anchor = -1;
	private int itemHeight = 1, clientHeight, page;
	/* horizontal extent written to the list box, as LB_SETHORIZONTALEXTENT keeps it */
	private int extent;
	/* user input reached the list box: resident rows are the authority until captured */
	private boolean nativeDirty;
	/* the lanes changed: resident selection, caret and anchor must be written */
	private boolean selectionDirty = true;
	private boolean busy, relayout;
	/* native caret entry chosen for an off-window caret, -1 when the caret is resident */
	private int parkedCaret = -1;
	private int nativeAnchor = -1;
	private int barMax = Integer.MIN_VALUE, barPage = -1, barPos = -1;
	private boolean barVisible;

	ListViewport (List list) {
		this.list = list;
		this.single = (list.style & SWT.SINGLE) != 0;
		this.hScroll = (list.style & SWT.H_SCROLL) != 0;
		this.model = new VirtualListModel (list::measureStoreText, true);
	}

	/** Whether a List with this style is a viewport (decided before its handle exists). */
	static boolean mediates (int style) {
		return ENABLED && (style & SWT.VIRTUAL) == 0;
	}

	/* ---------------------------------------------------------------- layout */

	/** Takes over the (still empty) list box. */
	void install () {
		OS.SendMessage (list.handle, OS.LB_RESETCONTENT, 0, 0);
		plan.clear ();
		layout ();
	}

	/** Recomputes geometry, moves the resident window if needed, writes the lanes, positions the plane and updates the bar. */
	void layout () {
		if (list.isDisposed ()) return;
		if (busy) {
			relayout = true;
			return;
		}
		busy = true;
		try {
			/* showing or hiding a bar changes the client area: lay out again until it is stable */
			for (int pass = 0; pass < 3; pass++) {
				relayout = false;
				measureGeometry ();
				top = clampTop (top);
				ensureWindow ();
				if (selectionDirty) {
					writeLanes ();
				} else {
					positionPlane ();
				}
				updateBar ();
				if (!relayout) break;
			}
		} finally {
			busy = false;
		}
	}

	/* selection, caret and anchor of the resident rows, then the top row: one repaint */
	private void writeLanes () {
		long handle = list.handle;
		boolean redraw = list.getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		try {
			syncResidentSelection ();
			restoreFocus ();
			selectionDirty = false;
			positionPlane ();
		} finally {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
		}
	}

	private void measureGeometry () {
		int height = (int) OS.SendMessage (list.handle, OS.LB_GETITEMHEIGHT, 0, 0);
		itemHeight = Math.max (1, height);
		RECT rect = new RECT ();
		OS.GetClientRect (list.handle, rect);
		clientHeight = Math.max (0, rect.bottom - rect.top);
		page = clientHeight / itemHeight;
	}

	/** Largest top row: the native list box keeps a full page below it (at least one row). */
	private int clampTop (int row) {
		int count = model.getItemCount ();
		return Math.max (0, Math.min (row, count - Math.max (1, page)));
	}

	private int overscan () {
		return Math.max (MIN_OVERSCAN, 2 * Math.max (1, page));
	}

	/* rows that must be resident: the page, the partial row and half the overscan on each side */
	private void ensureWindow () {
		int count = model.getItemCount ();
		int visible = Math.max (1, page) + 1, margin = overscan () / 2;
		int needFirst = Math.max (0, top - margin), needEnd = Math.min (count, top + visible + margin);
		int wantFirst = Math.max (0, top - overscan ()), wantEnd = Math.min (count, top + visible + overscan ());
		boolean covered = plan.covers (needFirst, needEnd) && plan.end () <= count;
		boolean bloated = plan.size () > (wantEnd - wantFirst) + 2 * overscan ();
		if (covered && !bloated && !(count == 0 && plan.size () != 0)) return;
		rebuild (wantFirst, wantEnd - wantFirst);
	}

	/** Moves the native window: retire head and tail, insert head and tail, commit, then write the lanes. */
	private void rebuild (int nextFirst, int nextSize) {
		long handle = list.handle;
		captureResidentState ();
		plan.plan (nextFirst, nextSize);
		boolean redraw = list.getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		try {
			if (plan.replaced ()) {
				OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
			} else {
				for (int i = 0; i < plan.retiredHead (); i++) OS.SendMessage (handle, OS.LB_DELETESTRING, 0, 0);
				int remaining = plan.size () - plan.retiredHead ();
				for (int i = 0; i < plan.retiredTail (); i++) OS.SendMessage (handle, OS.LB_DELETESTRING, --remaining, 0);
			}
			int head = plan.insertedHead ();
			for (int i = 0; i < head; i++) insertNative (i, nextFirst + i);
			int tail = plan.insertedTail ();
			for (int i = 0; i < tail; i++) appendNative (nextFirst + nextSize - tail + i);
			plan.commit ();
			syncResidentSelection ();
			restoreFocus ();
			selectionDirty = false;
			positionPlane ();
		} finally {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
		}
		LOG.log (System.Logger.Level.TRACE, () -> "List window: rows " + plan.first () + "+" + plan.size () + " of " + model.getItemCount ()
				+ ", top " + top + " (" + plan.touched () + " native rows touched)");
	}

	private void insertNative (int entry, int row) {
		int length = list.storeChars (model.getItem (row), false);
		list.textBuffer [length] = 0;
		long result = OS.SendMessage (list.handle, OS.LB_INSERTSTRING, entry, list.textBuffer);
		if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) list.error (SWT.ERROR_ITEM_NOT_ADDED);
	}

	private void appendNative (int row) {
		insertNative ((int) OS.SendMessage (list.handle, OS.LB_GETCOUNT, 0, 0), row);
	}

	/** Puts the native top row of the window on the logical top row; the list box scrolls (and blits) natively. */
	void positionPlane () {
		if (plan.size () == 0) return;
		int wanted = Math.max (0, Math.min (top - plan.first (), plan.size () - 1));
		if ((int) OS.SendMessage (list.handle, OS.LB_GETTOPINDEX, 0, 0) != wanted) {
			OS.SendMessage (list.handle, OS.LB_SETTOPINDEX, wanted, 0);
		}
	}

	/**
	 * The vertical bar with the stock list box's values: range 0..count-1, page = fully
	 * visible rows (at most the row count), position = top row; shown when the rows do not fit.
	 */
	void updateBar () {
		int count = model.getItemCount ();
		int max = Math.max (0, count - 1), pageSize = count == 0 ? 0 : Math.min (count, page), pos = count == 0 ? 0 : top;
		boolean visible = count > page;
		if (max == barMax && pageSize == barPage && pos == barPos && visible == barVisible) return;
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE | OS.SIF_POS;
		info.nMin = 0;
		info.nMax = max;
		info.nPage = pageSize;
		info.nPos = pos;
		OS.SetScrollInfo (list.handle, OS.SB_VERT, info, true);
		boolean shown = (OS.GetWindowLong (list.handle, OS.GWL_STYLE) & OS.WS_VSCROLL) != 0;
		if (shown != visible) OS.ShowScrollBar (list.handle, OS.SB_VERT, visible);
		barMax = max;
		barPage = pageSize;
		barPos = pos;
		barVisible = visible;
	}

	/* ---------------------------------------------------------------- resident state */

	/** After user input the resident rows are the authority for top, caret, anchor and their selection: copy them into the lanes. */
	void captureResidentState () {
		if (!nativeDirty) return;
		nativeDirty = false;
		int size = plan.size ();
		if (size == 0) return;
		long handle = list.handle;
		int first = plan.first ();
		int nativeTop = (int) OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
		if (nativeTop >= 0 && nativeTop < size) top = first + nativeTop;
		int nativeCaret = (int) OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
		if (nativeCaret >= 0 && nativeCaret < size && nativeCaret != parkedCaret) {
			caret = first + nativeCaret;
			parkedCaret = -1;
		}
		int nativeAnchorNow = (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
		if (nativeAnchorNow != nativeAnchor) {
			anchor = nativeAnchorNow >= 0 && nativeAnchorNow < size ? first + nativeAnchorNow : -1;
			nativeAnchor = nativeAnchorNow;
		}
		if (single) {
			int selected = (int) OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
			int [] indices = model.getSelectionIndices ();
			int logical = indices.length == 0 ? -1 : indices [0];
			if (selected >= 0 && selected < size) {
				if (logical != first + selected) {
					model.deselectAll ();
					model.select (first + selected);
				}
			} else if (logical >= first && logical < first + size) {
				model.deselectAll ();
			}
			return;
		}
		int [] selected = new int [size];
		int n = (int) OS.SendMessage (handle, OS.LB_GETSELITEMS, size, selected);
		model.deselect (first, first + size - 1);
		for (int i = 0; i < n; i++) model.select (first + selected [i]);
	}

	/** Writes the lane selection of every resident row natively (programmatic: no LBN_SELCHANGE). */
	private void syncResidentSelection () {
		int size = plan.size ();
		if (size == 0 || single) return;
		long handle = list.handle;
		int first = plan.first ();
		int [] selected = new int [size];
		int n = (int) OS.SendMessage (handle, OS.LB_GETSELITEMS, size, selected);
		boolean [] have = new boolean [size];
		for (int i = 0; i < n; i++) if (selected [i] >= 0 && selected [i] < size) have [selected [i]] = true;
		for (int entry = 0; entry < size; entry++) {
			boolean want = model.isSelected (first + entry);
			if (want != have [entry]) OS.SendMessage (handle, OS.LB_SETSEL, want ? 1 : 0, entry);
		}
	}

	/**
	 * Puts the native caret on the logical caret row, or parks it on a resident row that is not
	 * painted when the caret row is not resident; in MULTI the anchor follows. Selection-neutral:
	 * a single-selection list box can only move its caret with its selection, so the selection is
	 * shifted back into place by deleting and re-inserting the rows between them, as the stock
	 * list box itself leaves caret and selection apart after inserts and removes.
	 */
	private void restoreFocus () {
		int size = plan.size ();
		if (size == 0) {
			parkedCaret = -1;
			return;
		}
		int first = plan.first ();
		int entry = caret - first;
		parkedCaret = -1;
		if (entry < 0 || entry >= size) {
			entry = entry < 0 ? 0 : size - 1;
			parkedCaret = entry;
		}
		long handle = list.handle;
		if (single) {
			int [] indices = model.getSelectionIndices ();
			int selection = indices.length == 0 ? -1 : indices [0] - first;
			if (selection >= size) selection = -1;
			if (selection < 0 && indices.length != 0) {
				/* the selected row is not resident: nothing is selected natively */
				selection = -1;
			}
			setSingleState (entry, selection);
			return;
		}
		if ((int) OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0) != entry) {
			OS.SendMessage (handle, OS.LB_SETCARETINDEX, entry, 0);
		}
		int anchorEntry = anchor - first;
		if (anchorEntry >= 0 && anchorEntry < size && (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0) != anchorEntry) {
			OS.SendMessage (handle, LB_SETANCHORINDEX, anchorEntry, 0);
		}
		nativeAnchor = (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
	}

	/* single selection: caret on entry c, selection on entry s (or none) */
	private void setSingleState (int c, int s) {
		long handle = list.handle;
		int nativeCaret = (int) OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
		int nativeSelection = (int) OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (nativeCaret == c && nativeSelection == s) return;
		boolean structural = s != -1 && s != c;
		boolean redraw = structural && list.getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		try {
			writeSingleState (c, s);
		} finally {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
		}
		nativeAnchor = (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
	}

	private void writeSingleState (int c, int s) {
		long handle = list.handle;
		if (s == -1) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, c, 0);
			OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
		} else if (s == c) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, c, 0);
		} else if (s > c) {
			/* the selected row moves up to the caret entry, is selected there, then the rows in between come back in front of it */
			for (int i = c; i < s; i++) OS.SendMessage (handle, OS.LB_DELETESTRING, c, 0);
			OS.SendMessage (handle, OS.LB_SETCURSEL, c, 0);
			for (int i = s - 1; i >= c; i--) insertNative (c, plan.first () + i);
		} else {
			/* blank rows push the selected row down to the caret entry; removing them moves the selection back, not the caret */
			char [] empty = {0};
			for (int i = s; i < c; i++) OS.SendMessage (handle, OS.LB_INSERTSTRING, s, empty);
			OS.SendMessage (handle, OS.LB_SETCURSEL, c, 0);
			for (int i = s; i < c; i++) OS.SendMessage (handle, OS.LB_DELETESTRING, s, 0);
		}
	}

	/* ---------------------------------------------------------------- input */

	/** Before the list box handles a message: user input makes the resident rows the authority. */
	void beforeNative (int msg, long wParam) {
		switch (msg) {
			case OS.WM_LBUTTONDOWN, OS.WM_LBUTTONDBLCLK -> {
				sync ();
				/* MULTI (extended): a click without Ctrl replaces the whole selection, off-window rows included */
				if (!single && OS.GetKeyState (OS.VK_CONTROL) >= 0) clearOffWindowSelection ();
			}
			case OS.WM_KEYDOWN -> {
				sync ();
				if (!single && OS.GetKeyState (OS.VK_CONTROL) >= 0 && isNavigationKey ((int) wParam)) clearOffWindowSelection ();
			}
			default -> {}
		}
	}

	private static boolean isNavigationKey (int key) {
		return key == OS.VK_UP || key == OS.VK_DOWN || key == OS.VK_PRIOR || key == OS.VK_NEXT || key == OS.VK_HOME
				|| key == OS.VK_END || key == OS.VK_LEFT || key == OS.VK_RIGHT;
	}

	private void clearOffWindowSelection () {
		int first = plan.first (), end = plan.end (), count = model.getItemCount ();
		if (first > 0) model.deselect (0, first - 1);
		if (end < count) model.deselect (end, count - 1);
	}

	/** After the list box handled a message: copy user changes into the lanes and let the window follow. */
	void afterNative (int msg) {
		switch (msg) {
			case OS.WM_KEYDOWN, OS.WM_CHAR, OS.WM_LBUTTONDOWN, OS.WM_LBUTTONUP, OS.WM_LBUTTONDBLCLK, OS.WM_MOUSEMOVE, OS.WM_TIMER,
					OS.WM_SIZE -> {
				nativeDirty = true;
				captureResidentState ();
				layout ();
			}
			case OS.WM_SETFONT -> fontChanged ();
			default -> {}
		}
	}

	/** Mediated WM_VSCROLL, with the stock list box's steps (line 1, page = fully visible rows - 1) and a 32-bit thumb. */
	long onVScroll (long wParam) {
		sync ();
		int code = OS.LOWORD (wParam);
		int row = top;
		switch (code) {
			case OS.SB_LINEDOWN -> row = top + 1;
			case OS.SB_LINEUP -> row = top - 1;
			case OS.SB_PAGEDOWN -> row = top + Math.max (1, page - 1);
			case OS.SB_PAGEUP -> row = top - Math.max (1, page - 1);
			case OS.SB_TOP -> row = 0;
			case OS.SB_BOTTOM -> row = model.getItemCount ();
			case OS.SB_THUMBTRACK, OS.SB_THUMBPOSITION -> {
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_TRACKPOS;
				OS.GetScrollInfo (list.handle, OS.SB_VERT, info);
				row = (info.nTrackPos & 0xFFFF) == OS.HIWORD (wParam) ? info.nTrackPos : OS.HIWORD (wParam);
			}
			default -> {
				return 0;
			}
		}
		setTopRow (row);
		return 0;
	}

	/**
	 * Mouse wheel: the list box computes the lines itself (wheel settings, partial deltas); the
	 * resident window has at least one overscan on each side, so its native top moves exactly as
	 * the stock one, and the logical top follows.
	 */
	long onWheel (long hwnd, long wParam, long lParam) {
		sync ();
		int first = plan.first ();
		long code = OS.CallWindowProc (List.ListProc, hwnd, OS.WM_MOUSEWHEEL, wParam, lParam);
		int nativeTop = (int) OS.SendMessage (list.handle, OS.LB_GETTOPINDEX, 0, 0);
		if (plan.size () > 0 && nativeTop >= 0) setTopRow (first + nativeTop);
		return code;
	}

	/** Home and End move over logical rows (the window holds neither end); other keys stay native. */
	LRESULT onKeyDown (long wParam) {
		int key = (int) wParam;
		if (key != OS.VK_HOME && key != OS.VK_END) return null;
		if (OS.GetKeyState (OS.VK_SHIFT) < 0 || OS.GetKeyState (OS.VK_CONTROL) < 0) return null;
		sync ();
		int count = model.getItemCount ();
		if (count == 0) return LRESULT.ZERO;
		int target = key == OS.VK_HOME ? 0 : count - 1;
		boolean changed = caret != target || model.getSelectionCount () != 1 || !model.isSelected (target);
		model.deselectAll ();
		model.select (target);
		caret = target;
		anchor = target;
		top = key == OS.VK_HOME ? 0 : count;
		selectionDirty = true;
		layout ();
		if (changed) list.sendSelectionEvent (SWT.Selection);
		return LRESULT.ZERO;
	}

	void setTopRow (int row) {
		top = clampTop (row);
		layout ();
	}

	/** Copies pending user changes into the lanes; every mediated API call starts here. */
	void sync () {
		if (nativeDirty) captureResidentState ();
	}

	/* ---------------------------------------------------------------- items */

	static String nativeText (String string) {
		int nul = string.indexOf ('\0');
		return nul == -1 ? string : string.substring (0, nul);
	}

	int getItemCount () {
		return model.getItemCount ();
	}

	String getItem (int index) {
		if (index < 0 || index >= model.getItemCount ()) list.error (SWT.ERROR_INVALID_RANGE);
		return model.getItem (index);
	}

	String [] getItems () {
		return model.getItems ();
	}

	/** <code>LB_FINDSTRINGEXACT</code> followed by an exact comparison: the first equal row at or after <code>start</code>. */
	int indexOf (String string, int start) {
		return model.indexOf (string, start);
	}

	void add (String string, int index) {
		sync ();
		int count = model.getItemCount ();
		if (index < 0 || index > count) list.error (SWT.ERROR_INVALID_RANGE);
		String text = nativeText (string);
		model.add (text, index);
		/* the list box keeps the caret, the anchor and the top row index; the selection shifts */
		int entry = plan.inserted (index, 1);
		if (entry >= 0) insertNative (entry, index);
		if (hScroll) extent = Math.max (extent, model.measureItem (index) + List.INSET);
		selectionDirty = true;
		layout ();
		writeExtent ();
	}

	void setItemCount (int count) {
		sync ();
		int itemCount = model.getItemCount ();
		if (count < itemCount) {
			remove (count, itemCount - 1);
			return;
		}
		for (int i = itemCount; i < count; i++) {
			model.add ("", i);
			int entry = plan.inserted (i, 1);
			if (entry >= 0) insertNative (entry, i);
		}
		if (hScroll && count > itemCount) extent = Math.max (extent, List.INSET);
		selectionDirty = true;
		layout ();
		writeExtent ();
	}

	/* one native LB_DELETESTRING: caret and anchor as the stock list box moves them */
	private void deleteRow (int index) {
		int countAfter = model.getItemCount () - 1;
		if (index == caret && caret > 0) caret--;
		if (caret >= countAfter) caret = Math.max (0, countAfter - 1);
		if (anchor >= index) anchor = -1;
		model.remove (index);
		if (plan.removed (index, 1) > 0) OS.SendMessage (list.handle, OS.LB_DELETESTRING, plan.removedEntry (index), 0);
	}

	void remove (int index) {
		sync ();
		if (index < 0 || index >= model.getItemCount ()) list.error (SWT.ERROR_INVALID_RANGE);
		int width = hScroll ? model.measureItem (index) : 0;
		int topIndex = top;
		deleteRow (index);
		if (index < topIndex) topIndex--;
		finishRemove (topIndex, width);
	}

	void remove (int start, int end) {
		sync ();
		int count = model.getItemCount ();
		if (!(0 <= start && start <= end && end < count)) list.error (SWT.ERROR_INVALID_RANGE);
		if (start == 0 && end == count - 1) {
			removeAll ();
			return;
		}
		int width = 0;
		if (hScroll) for (int i = start; i <= end; i++) width = Math.max (width, model.measureItem (i));
		int topIndex = top;
		for (int i = start; i <= end; i++) deleteRow (start);
		if (end < topIndex) topIndex -= end - start + 1;
		finishRemove (topIndex, width);
	}

	void remove (int [] indices) {
		sync ();
		int [] sorted = indices.clone ();
		java.util.Arrays.sort (sorted);
		int count = model.getItemCount ();
		int low = sorted [0], high = sorted [sorted.length - 1];
		if (!(0 <= low && low <= high && high < count)) list.error (SWT.ERROR_INVALID_RANGE);
		int topIndex = top, topCount = 0, width = 0, last = -1;
		for (int i = sorted.length - 1; i >= 0; i--) {
			int index = sorted [i];
			if (index == last) continue;
			if (hScroll) width = Math.max (width, model.measureItem (index));
			deleteRow (index);
			if (index < topIndex) topCount++;
			last = index;
		}
		finishRemove (topIndex - topCount, width);
	}

	/* stock remove: the top row index is set again (and clamped); the extent is recomputed when the widest row went */
	private void finishRemove (int topIndex, int removedWidth) {
		top = topIndex;
		if (hScroll && removedWidth + List.INSET >= extent) extent = model.getWidestWidth () + List.INSET;
		selectionDirty = true;
		layout ();
		writeExtent ();
	}

	void removeAll () {
		sync ();
		model.removeAll ();
		OS.SendMessage (list.handle, OS.LB_RESETCONTENT, 0, 0);
		plan.clear ();
		/* LB_RESETCONTENT: caret and top row 0, the anchor is kept */
		top = caret = 0;
		if (hScroll) extent = 0;
		selectionDirty = true;
		layout ();
		writeExtent ();
	}

	void setItems (String [] items) {
		sync ();
		String [] texts = new String [items.length];
		for (int i = 0; i < items.length; i++) texts [i] = nativeText (items [i]);
		model.setItems (texts);
		OS.SendMessage (list.handle, OS.LB_RESETCONTENT, 0, 0);
		plan.clear ();
		top = caret = 0;
		if (hScroll) extent = model.getWidestWidth () + List.INSET;
		selectionDirty = true;
		layout ();
		writeExtent ();
	}

	/** Stock setScrollWidth (): the widest row plus the inset, measured with the current font. */
	void recomputeExtent () {
		if (!hScroll) return;
		extent = model.getWidestWidth () + List.INSET;
		writeExtent ();
	}

	/** WM_SETFONT: widths and item height change; the top row index is kept. */
	void fontChanged () {
		model.invalidateWidths ();
		selectionDirty = true;
		layout ();
	}

	private void writeExtent () {
		if (hScroll && (int) OS.SendMessage (list.handle, OS.LB_GETHORIZONTALEXTENT, 0, 0) != extent) {
			OS.SendMessage (list.handle, OS.LB_SETHORIZONTALEXTENT, extent, 0);
		}
	}

	/** Bidi auto direction changed: the resident rows are added again (with or without the direction mark). */
	void reloadResidentRows () {
		int first = plan.first (), size = plan.size ();
		OS.SendMessage (list.handle, OS.LB_RESETCONTENT, 0, 0);
		for (int i = 0; i < size; i++) insertNative (i, first + i);
		selectionDirty = true;
		layout ();
	}

	/* ---------------------------------------------------------------- top row */

	int getTopIndex () {
		sync ();
		return top;
	}

	/** Stock setTopIndex: an out-of-range index is clamped to the rows, then the list box keeps a full page below the top. */
	void setTopIndex (int index) {
		sync ();
		int count = model.getItemCount ();
		if (count == 0) return;
		setTopRow (Math.min (count - 1, Math.max (0, index)));
	}

	/** The row scrolled just into view, as LB_SETCURSEL (SINGLE) and LB_SETCARETINDEX (MULTI) scroll it. */
	private void scrollIntoView (int index) {
		if (index < top) {
			top = index;
		} else if (index >= top + page) {
			top = !single && page == 0 && clientHeight > 0 ? index : index - page + 1;
		}
		top = clampTop (top);
	}

	/** Stock showSelection: the first selected row, centred when it is not visible. */
	void showSelection () {
		sync ();
		list.forceResize ();
		measureGeometry ();
		int index = firstSelected ();
		int count = model.getItemCount ();
		if (index == -1 || count == 0) return;
		int visibleCount = Math.max (page, 1);
		int bottomIndex = Math.min (top + visibleCount, count) - 1;
		if (top <= index && index <= bottomIndex) return;
		setTopRow (Math.min (Math.max (index - (visibleCount / 2), 0), count - 1));
	}

	/* ---------------------------------------------------------------- selection */

	private int firstSelected () {
		int [] indices = model.getSelectionIndices ();
		return indices.length == 0 ? -1 : indices [0];
	}

	/** Stock select (index, scroll): LB_SETCURSEL (SINGLE) or LB_SETSEL (MULTI, the anchor moves; the caret is put back). */
	void select (int index, boolean scroll) {
		sync ();
		if (index < 0 || index >= model.getItemCount ()) return;
		if (single) {
			model.deselectAll ();
			model.select (index);
			caret = index;
			if (scroll) scrollIntoView (index);
		} else {
			model.select (index);
			anchor = index;
			if (scroll) caret = index;
		}
		selectionDirty = true;
		layout ();
	}

	/** Stock select (start, end, scroll) of a MULTI list: LB_SELITEMRANGEEX, caret and anchor unchanged. */
	void select (int start, int end, boolean scroll) {
		if (start == end) {
			select (start, scroll);
			return;
		}
		sync ();
		model.select (start, end);
		selectionDirty = true;
		layout ();
		if (scroll) showSelection ();
	}

	/** Public setSelection (start, end), after its deselectAll and argument checks. */
	void setSelectionRange (int start, int end) {
		sync ();
		int count = model.getItemCount ();
		if (count == 0 || start >= count) return;
		start = Math.max (0, start);
		end = Math.min (end, count - 1);
		if (single) {
			select (start, true);
		} else {
			select (start, end, true);
			setFocusIndex (start);
		}
	}

	/** Public select (start, end). */
	void selectRange (int start, int end) {
		sync ();
		if (end < 0 || start > end || (single && start != end)) return;
		int count = model.getItemCount ();
		if (count == 0 || start >= count) return;
		start = Math.max (0, start);
		end = Math.min (end, count - 1);
		if (single) {
			select (start, false);
		} else {
			select (start, end, false);
		}
	}

	void selectAll () {
		sync ();
		if (single) return;
		model.selectAll ();
		selectionDirty = true;
		layout ();
	}

	void deselect (int index) {
		sync ();
		if (single) {
			if (firstSelected () == index) model.deselectAll ();
		} else {
			model.deselect (index);
		}
		selectionDirty = true;
		layout ();
	}

	void deselect (int [] indices) {
		sync ();
		if (single) {
			int selected = firstSelected ();
			if (selected == -1) return;
			for (int index : indices) {
				if (index == selected) {
					model.deselectAll ();
					break;
				}
			}
		} else {
			for (int index : indices) model.deselect (index);
		}
		selectionDirty = true;
		layout ();
	}

	void deselect (int start, int end) {
		sync ();
		if (start > end) return;
		if (single) {
			int selected = firstSelected ();
			if (selected != -1 && start <= selected && selected <= end) model.deselectAll ();
		} else {
			int count = model.getItemCount ();
			if (start < 0 && end < 0) return;
			if (start >= count && end >= count) return;
			model.deselect (start, end);
		}
		selectionDirty = true;
		layout ();
	}

	void deselectAll () {
		sync ();
		model.deselectAll ();
		selectionDirty = true;
		layout ();
	}

	boolean isSelected (int index) {
		sync ();
		return model.isSelected (index);
	}

	int getSelectionCount () {
		sync ();
		return single ? Math.min (1, model.getSelectionCount ()) : model.getSelectionCount ();
	}

	/** Stock getSelectionIndex: SINGLE the selected row; MULTI the caret when it is selected, otherwise the first selected row. */
	int getSelectionIndex () {
		sync ();
		if (model.getSelectionCount () == 0) return -1;
		if (!single && model.isSelected (caret)) return caret;
		return firstSelected ();
	}

	int [] getSelectionIndices () {
		sync ();
		return model.getSelectionIndices ();
	}

	/** Stock getFocusIndex: the caret, -1 for an empty list. */
	int getFocusIndex () {
		sync ();
		return model.getItemCount () == 0 ? -1 : caret;
	}

	/** Stock setFocusIndex (MULTI): LB_SETCARETINDEX moves the caret and scrolls it into view. */
	void setFocusIndex (int index) {
		sync ();
		if (!(0 <= index && index < model.getItemCount ())) return;
		if (single) {
			/* a single-selection list box ignores LB_SETCARETINDEX while a row is selected */
			if (model.getSelectionCount () != 0) return;
		}
		caret = index;
		scrollIntoView (index);
		selectionDirty = true;
		layout ();
	}

	/* ---------------------------------------------------------------- size and menu */

	/** Stock computeSize over the logical rows: the horizontal extent (H_SCROLL) or the widest row, and one item height per row. */
	Point computeSize (Point hintInPoints, int zoom) {
		sync ();
		Point hintInPixels = Win32DPIUtils.pointToPixelAsSufficientlyLargeSize (hintInPoints, zoom);
		int width = 0, height = 0;
		if (hintInPoints.x == SWT.DEFAULT) {
			width = hScroll ? (int) OS.SendMessage (list.handle, OS.LB_GETHORIZONTALEXTENT, 0, 0) - List.INSET : model.getWidestWidth ();
		}
		if (hintInPoints.y == SWT.DEFAULT) {
			height = model.getItemCount () * (int) OS.SendMessage (list.handle, OS.LB_GETITEMHEIGHT, 0, 0);
		}
		if (width == 0) width = Widget.DEFAULT_WIDTH;
		if (height == 0) height = Widget.DEFAULT_HEIGHT;
		if (hintInPoints.x != SWT.DEFAULT) width = hintInPixels.x;
		if (hintInPoints.y != SWT.DEFAULT) height = hintInPixels.y;
		int border = list.getBorderWidthInPixels ();
		width += border * 2 + List.INSET;
		height += border * 2;
		if ((list.style & SWT.V_SCROLL) != 0) width += list.getSystemMetrics (OS.SM_CXVSCROLL);
		if (hScroll) height += list.getSystemMetrics (OS.SM_CYHSCROLL);
		return new Point (width, height);
	}

	/** Stock updateMenuLocation over logical rows: below the caret row's text, inside the client area. */
	void updateMenuLocation (Event event) {
		sync ();
		Rectangle clientArea = list.getClientAreaInPixels ();
		int x = clientArea.x, y = clientArea.y;
		int focusIndex = getFocusIndex ();
		if (focusIndex != -1) {
			x = Math.max (x, model.measureItem (focusIndex) / 2);
			x = Math.min (x, clientArea.x + clientArea.width);
			y = Math.max (y, (focusIndex - top + 1) * itemHeight);
			y = Math.min (y, clientArea.y + clientArea.height);
		}
		Point pt = list.toDisplayInPixels (x, y);
		int zoom = list.getAutoscalingZoom ();
		event.setLocation (DPIUtil.pixelToPoint (pt.x, zoom), DPIUtil.pixelToPoint (pt.y, zoom));
	}

	/** Number of native rows (the resident window), for tests and diagnostics. */
	int residentCount () {
		return plan.size ();
	}
}

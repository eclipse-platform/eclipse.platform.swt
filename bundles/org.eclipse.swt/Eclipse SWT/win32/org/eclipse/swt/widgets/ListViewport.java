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
import org.eclipse.swt.accessibility.*;
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
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.List");

	final List list;
	final VirtualListModel model;
	private final VirtualRowWindowPlan plan = new VirtualRowWindowPlan ();
	/* SINGLE; extended = MULTI without SWT.SIMPLE (LBS_EXTENDEDSEL: Shift ranges, a click replaces the selection) */
	private final boolean single, extended, hScroll;
	/* logical top row, caret and anchor (native semantics: the anchor may be -1) */
	private int top, caret, anchor = -1;
	/*
	 * End of the extended list box's last range (anchor .. rangeEnd): the row of the last click or
	 * keyboard move, or the row LB_SETSEL selected. Ctrl+Shift+click replaces that range. The list box
	 * keeps it internally and LB_SETANCHORINDEX resets it, so it is a lane here.
	 */
	private int rangeEnd = -1;
	private int itemHeight = 1, clientHeight, page;
	/* horizontal extent written to the list box, as LB_SETHORIZONTALEXTENT keeps it */
	private int extent;
	/* user input reached the list box: resident rows are the authority until captured */
	private boolean nativeDirty;
	/* the selection lane changed: the resident rows' selection must be written */
	private boolean selectionDirty = true;
	/* caret or anchor (or the window under them) changed: they must be put back natively */
	private boolean focusDirty = true;
	private boolean busy, relayout;
	private int mirroredTop = -1;
	/* native caret entry chosen for an off-window caret, -1 when the caret is resident */
	private int parkedCaret = -1;
	private int nativeAnchor = -1;
	/* what the stock list box last did to its vertical bar */
	static final int BAR_NONE = 0, BAR_DEFAULT = 1, BAR_SET = 2;
	private int barState = BAR_NONE;
	/*
	 * Build loops (add and remove, item 5): the bar's values are written at once (the API and the list box read
	 * them), its painting waits for the event loop, once per burst; the extent the list box holds is cached.
	 */
	private boolean deferBarPaint, barPaintPending;
	private int writtenExtent = -1;
	/* scratch structures of layout (), reused so that scrolling allocates nothing */
	private final RECT clientRect = new RECT ();
	private final SCROLLINFO barInfo = new SCROLLINFO ();

	ListViewport (List list) {
		this.list = list;
		this.single = (list.style & SWT.SINGLE) != 0;
		this.extended = !single && (list.style & SWT.SIMPLE) == 0;
		this.hScroll = (list.style & SWT.H_SCROLL) != 0;
		this.model = new VirtualListModel (list::measureStoreText, true);
		barInfo.cbSize = SCROLLINFO.sizeof;
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
				writeLanes ();
				updateBar ();
				if (!relayout) break;
			}
			/* the List's shared viewport runtime mirrors the public top row, as in the original mode */
			if (mirroredTop != top) {
				list.viewportRuntime.scrollTo (0, top);
				mirroredTop = top;
			}
		} finally {
			busy = false;
		}
	}

	/*
	 * Selection of the resident rows (LB_SETSEL repaints only the rows it changes), then caret
	 * and anchor, then the top row. A caret move may scroll the list box; it runs with drawing
	 * suspended and is followed by one repaint.
	 */
	private void writeLanes () {
		if (selectionDirty) {
			syncResidentSelection ();
			selectionDirty = false;
			focusDirty = true;
		}
		if (focusDirty) {
			focusDirty = false;
			if (!focusMatches ()) {
				long handle = list.handle;
				boolean redraw = list.getDrawing () && OS.IsWindowVisible (handle);
				if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
				try {
					restoreFocus ();
					positionPlane ();
				} finally {
					if (redraw) {
						OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
						OS.InvalidateRect (handle, null, true);
					}
				}
			}
		}
		positionPlane ();
	}

	private void measureGeometry () {
		int height = (int) OS.SendMessage (list.handle, OS.LB_GETITEMHEIGHT, 0, 0);
		itemHeight = Math.max (1, height);
		RECT rect = clientRect;
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
		/* while a drag holds the mouse, the native rows under it are not renumbered (growForDrag made room) */
		if (OS.GetCapture () == list.handle && plan.size () > 0 && plan.end () <= count) return;
		boolean bloated = plan.size () > (wantEnd - wantFirst) + 2 * overscan ();
		if (covered && !bloated && !(count == 0 && plan.size () != 0)) return;
		/* a jump keeps no resident row: build only the rows input needs now; the next move grows the window to the overscan */
		boolean jump = plan.size () == 0 || wantEnd <= plan.first () || wantFirst >= plan.end ();
		if (jump) {
			rebuild (needFirst, needEnd - needFirst);
		} else {
			rebuild (wantFirst, wantEnd - wantFirst);
		}
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
			selectionDirty = focusDirty = false;
			positionPlane ();
		} finally {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
		}
		if (LOG.isTraceEnabled ()) LOG.trace (() -> "List window: rows " + plan.first () + "+" + plan.size () + " of " + model.getItemCount ()
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
	 * The vertical bar with the stock list box's values. The list box writes
	 * <code>0 .. count - 1</code>, page = fully visible rows and position = top row on every
	 * change (Windows caps the page at the range and shows the bar when it can scroll), except
	 * that <code>LB_RESETCONTENT</code> frees the bar (BAR_NONE: every value reads 0) and the
	 * first row added afterwards leaves the default bar (BAR_DEFAULT: 0..100, page 0, hidden).
	 */
	void updateBar () {
		int count = model.getItemCount ();
		int max = barState == BAR_SET ? count - 1 : barState == BAR_DEFAULT ? 100 : 0;
		int pageSize = barState == BAR_SET ? page : 0, pos = barState == BAR_SET ? top : 0;
		long handle = list.handle;
		SCROLLINFO info = barInfo;
		info.fMask = OS.SIF_ALL;
		boolean ok = OS.GetScrollInfo (handle, OS.SB_VERT, info);
		boolean shown = (OS.GetWindowLong (handle, OS.GWL_STYLE) & OS.WS_VSCROLL) != 0;
		/* the values Windows keeps for this SCROLLINFO */
		int keptMax = Math.max (0, max), keptPage = Math.min (pageSize, keptMax + 1);
		int keptPos = Math.max (0, Math.min (pos, keptMax - Math.max (keptPage - 1, 0)));
		boolean visible = barState == BAR_SET && keptPage <= keptMax;
		boolean same = ok ? info.nMin == 0 && info.nMax == keptMax && info.nPage == keptPage && info.nPos == keptPos
				: barState == BAR_NONE;
		if (same && shown == visible) return;
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE | OS.SIF_POS;
		info.nMin = 0;
		info.nMax = max;
		info.nPage = pageSize;
		info.nPos = pos;
		boolean paint = !deferBarPaint || shown != visible;
		OS.SetScrollInfo (handle, OS.SB_VERT, info, paint);
		if (barState != BAR_SET) OS.ShowScrollBar (handle, OS.SB_VERT, false);
		if (!paint) scheduleBarPaint ();
	}

	/* one frame repaint when the event loop runs after a burst of item changes */
	private void scheduleBarPaint () {
		if (barPaintPending) return;
		barPaintPending = true;
		list.display.asyncExec (() -> {
			barPaintPending = false;
			if (!list.isDisposed ()) OS.RedrawWindow (list.handle, null, 0, OS.RDW_FRAME | OS.RDW_INVALIDATE);
		});
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
		if (anchor < 0 && size >= 2 && (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0) != -1) clearNativeAnchor ();
		if ((int) OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0) != entry) {
			OS.SendMessage (handle, OS.LB_SETCARETINDEX, entry, 0);
		}
		int anchorEntry = anchor - first;
		if (anchorEntry >= 0 && anchorEntry < size && (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0) != anchorEntry) {
			OS.SendMessage (handle, LB_SETANCHORINDEX, anchorEntry, 0);
		}
		nativeAnchor = (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
	}

	/* whether the native caret (and anchor, and the single selection) already are where restoreFocus would put them */
	private boolean focusMatches () {
		int size = plan.size ();
		if (size == 0) return true;
		long handle = list.handle;
		int first = plan.first ();
		int entry = caret - first;
		if (entry < 0 || entry >= size) entry = entry < 0 ? 0 : size - 1;
		if ((int) OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0) != entry) return false;
		if (single) {
			int selected = firstSelected () - first;
			if (selected < 0 || selected >= size) selected = -1;
			return (int) OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0) == selected;
		}
		int anchorEntry = anchor - first, nativeAnchorNow = (int) OS.SendMessage (handle, OS.LB_GETANCHORINDEX, 0, 0);
		if (anchor < 0) return nativeAnchorNow == -1 || size < 2;
		return anchorEntry < 0 || anchorEntry >= size || nativeAnchorNow == anchorEntry;
	}

	/*
	 * The list box has no message that clears its anchor; deleting a row at or before it does
	 * (ListRulesOracle2). The first resident row is deleted and inserted again, and its selection
	 * is put back with LB_SELITEMRANGEEX, which leaves the anchor alone.
	 */
	private void clearNativeAnchor () {
		long handle = list.handle;
		boolean selected0 = OS.SendMessage (handle, OS.LB_GETSEL, 0, 0) > 0, selected1 = OS.SendMessage (handle, OS.LB_GETSEL, 1, 0) > 0;
		OS.SendMessage (handle, OS.LB_DELETESTRING, 0, 0);
		insertNative (0, plan.first ());
		if (selected0) {
			OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, 0, 1);
			if (!selected1) OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, 1, 1);
		}
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

	/**
	 * Before the list box handles a message. User input makes the resident rows the authority;
	 * an extended (MULTI) click or drag that the list box cannot see whole (the anchor row is not
	 * resident) is applied here over logical rows.
	 * @return true when the message was handled and the list box must not see it
	 */
	boolean beforeNative (int msg, long wParam, long lParam) {
		switch (msg) {
			case OS.WM_LBUTTONDOWN, OS.WM_LBUTTONDBLCLK -> {
				sync ();
				/* the list box may fall back on its caret or anchor (a click below the last row selects the anchor row) */
				int row = rowAt (lParam);
				boolean exact = prepareNative (row >= 0 ? row : caret, row >= 0 ? row : caret);
				if (!extended) return false;
				boolean ctrl = OS.GetKeyState (OS.VK_CONTROL) < 0, shift = OS.GetKeyState (OS.VK_SHIFT) < 0;
				if (ctrl && shift && row >= 0) {
					replaceRange (row);
					return true;
				}
				if (shift && !exact) {
					if (row >= 0) clickRange (row, ctrl);
					return row >= 0;
				}
				/* a click without Ctrl, or with Shift, replaces the whole selection, off-window rows included */
				if (!ctrl || shift) clearOffWindowSelection ();
				/* a drag may scroll natively: give the list box rows to scroll into */
				if (msg == OS.WM_LBUTTONDOWN) growForDrag ();
			}
			case OS.WM_KEYDOWN -> {
				sync ();
				/* navigation and Space replace the whole selection, except Ctrl alone (SWT moves only the caret) */
				boolean ctrlOnly = OS.GetKeyState (OS.VK_CONTROL) < 0 && OS.GetKeyState (OS.VK_SHIFT) >= 0;
				if (extended && !ctrlOnly && (isNavigationKey ((int) wParam) || wParam == OS.VK_SPACE)) clearOffWindowSelection ();
			}
			default -> {}
		}
		return false;
	}

	/** Rows apart within which a user action is left to the list box (the window is grown to hold them). */
	static final int NATIVE_REACH = 2000;

	/**
	 * Before the list box handles user input: the resident window holds every row the list box
	 * may look at (the visible rows, the caret, the anchor, the selected row of a single-selection
	 * list and the given rows), and the native caret, anchor and selection are the logical ones, so
	 * the list box applies its own rules. Answers false when those rows are too far apart; the
	 * caller then applies the measured rules over logical rows.
	 */
	boolean prepareNative (int low, int high) {
		sync ();
		int count = model.getItemCount ();
		if (count == 0) return true;
		int from = top, to = Math.min (count, top + Math.max (1, page) + 1);
		int selected = single ? firstSelected () : -1;
		int [] rows = {caret, anchor >= 0 ? anchor : caret, selected >= 0 ? selected : caret, low, high};
		for (int row : rows) {
			int clamped = Math.max (0, Math.min (count - 1, row));
			from = Math.min (from, clamped);
			to = Math.max (to, clamped + 1);
		}
		if (to - from > NATIVE_REACH) return false;
		int margin = Math.max (1, page) + 1;
		int first = Math.max (0, from - margin), end = Math.min (count, to + margin);
		if (!plan.covers (first, end)) {
			rebuild (first, end - first);
		} else {
			focusDirty = true;
			writeLanes ();
		}
		return true;
	}

	/** The LB_ITEMFROMPOINT message (not in OS). */
	static final int LB_ITEMFROMPOINT = 0x1a9;

	/* the logical row under a client point, or -1 below the last row */
	private int rowAt (long lParam) {
		long result = OS.SendMessage (list.handle, LB_ITEMFROMPOINT, 0, lParam);
		if (OS.HIWORD (result) != 0 || plan.size () == 0) return -1;
		int entry = OS.LOWORD (result);
		return entry < plan.size () ? plan.first () + entry : -1;
	}

	/*
	 * Shift+click with the anchor outside the window: the rows from the anchor to the clicked
	 * row become the selection (Ctrl keeps the other selected rows); the caret moves, the anchor
	 * stays, as the list box does when it holds both rows.
	 */
	private void clickRange (int row, boolean ctrl) {
		int count = model.getItemCount ();
		/* Shift and Ctrl+Shift alike: the rows from the anchor to the clicked row replace the selection; without an anchor none */
		model.deselectAll ();
		if (anchor >= 0 && anchor < count) model.select (Math.min (anchor, row), Math.max (anchor, row));
		caret = row;
		scrollIntoView (row);
		selectionDirty = true;
		layout ();
		list.sendSelectionEvent (SWT.Selection);
	}

	/*
	 * Ctrl+Shift+click of an extended list (ListReplayProbe, official SWT 3.135.0): the last range
	 * (anchor .. rangeEnd) is deselected, then the rows from the anchor to the clicked row take the
	 * anchor row's former selection state; other rows keep theirs. Without an anchor only the caret
	 * moves. The list box sends LBN_SELCHANGE either way.
	 */
	private void replaceRange (int row) {
		int count = model.getItemCount ();
		if (anchor < 0 || anchor >= count) {
			/* without an anchor the selection is cleared and the clicked row becomes the anchor */
			model.deselectAll ();
			anchor = row;
		} else {
			boolean selected = model.isSelected (anchor);
			int end = rangeEnd >= 0 && rangeEnd < count ? rangeEnd : anchor;
			model.deselect (Math.min (anchor, end), Math.max (anchor, end));
			if (selected) {
				model.select (Math.min (anchor, row), Math.max (anchor, row));
			} else {
				model.deselect (Math.min (anchor, row), Math.max (anchor, row));
			}
		}
		caret = row;
		rangeEnd = row;
		if (OS.GetFocus () != list.handle) OS.SetFocus (list.handle);
		selectionDirty = true;
		layout ();
		list.sendSelectionEvent (SWT.Selection);
	}

	/* rows a drag may auto-scroll into without the window moving (moving it would renumber the native rows under the drag) */
	static final int DRAG_ROWS = 500;

	private void growForDrag () {
		int count = model.getItemCount ();
		int wantFirst = Math.max (0, top - DRAG_ROWS), wantEnd = Math.min (count, top + Math.max (1, page) + 1 + DRAG_ROWS);
		if (plan.covers (wantFirst, wantEnd)) return;
		rebuild (wantFirst, wantEnd - wantFirst);
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
	void afterNative (int msg, long wParam) {
		if (msg == OS.WM_MOUSEMOVE && (wParam & OS.MK_LBUTTON) == 0) return;
		switch (msg) {
			case OS.WM_KEYDOWN, OS.WM_CHAR, OS.WM_LBUTTONDOWN, OS.WM_LBUTTONUP, OS.WM_LBUTTONDBLCLK, OS.WM_MOUSEMOVE, OS.WM_TIMER,
					OS.WM_SIZE -> {
				nativeDirty = true;
				captureResidentState ();
				if (msg != OS.WM_SIZE && msg != OS.WM_CHAR) rangeEnd = caret;
				layout ();
				if (msg != OS.WM_SIZE && msg != OS.WM_MOUSEMOVE && msg != OS.WM_TIMER) notifyAccessibility ();
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
				SCROLLINFO info = barInfo;
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

	/**
	 * Keys whose target or anchor may lie outside the resident window move over logical rows
	 * with the list box's rules (ListRulesOracle): Home and End; Prior and Next (caret -/+
	 * (page - 1), then scrolled just into view); Up and Down when the caret row is not resident.
	 * Without modifiers the target becomes the only selected row and the anchor; with Shift an
	 * extended list selects the rows from the anchor to the target. Ctrl keys of an extended
	 * list move only the caret, as SWT's own handler in List.WM_KEYDOWN does, over logical rows.
	 * Keys over resident rows stay native.
	 */
	LRESULT onKeyDown (long wParam) {
		int key = (int) wParam;
		boolean ctrl = OS.GetKeyState (OS.VK_CONTROL) < 0, shift = OS.GetKeyState (OS.VK_SHIFT) < 0;
		if (extended && ctrl && !shift) return onControlKey (key);
		if (key == OS.VK_SPACE && !single && (!ctrl || shift)) {
			if (prepareNative (caret, caret)) return null;
			return onSpace (shift);
		}
		if (key != OS.VK_HOME && key != OS.VK_END && key != OS.VK_PRIOR && key != OS.VK_NEXT && key != OS.VK_UP && key != OS.VK_DOWN) return null;
		sync ();
		int reach = Math.max (1, page) + 1;
		int low = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_PRIOR -> caret - reach;
			default -> caret - 1;
		};
		int high = switch (key) {
			case OS.VK_END -> model.getItemCount () - 1;
			case OS.VK_NEXT -> caret + reach;
			default -> caret + 1;
		};
		if (prepareNative (low, high)) return null;
		/* SINGLE and SIMPLE lists navigate the same with or without modifiers */
		if (!extended) ctrl = shift = false;
		boolean extend = extended && shift;
		boolean resident = plan.entryOf (caret) >= 0 && parkedCaret == -1;
		boolean anchorResident = plan.entryOf (anchor) >= 0;
		if ((key == OS.VK_UP || key == OS.VK_DOWN) && resident && (!extend || anchorResident)) return null;
		/* Ctrl+Shift over rows the list box holds stays native */
		if (ctrl && (key == OS.VK_UP || key == OS.VK_DOWN) && resident && anchorResident) return null;
		int count = model.getItemCount ();
		if (count == 0) return LRESULT.ZERO;
		int step = Math.max (1, page - 1);
		int target = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_END -> count - 1;
			case OS.VK_PRIOR -> caret - step;
			case OS.VK_NEXT -> caret + step;
			case OS.VK_UP -> caret - 1;
			default -> caret + 1;
		};
		target = Math.max (0, Math.min (count - 1, target));
		if (!single && !extended) {
			/* SIMPLE (LBS_MULTIPLESEL): the keys move the caret only; the list box still notifies */
			boolean moved = caret != target;
			caret = target;
			if (key == OS.VK_HOME) {
				top = 0;
			} else if (key == OS.VK_END) {
				top = count;
			} else {
				scrollIntoView (target);
			}
			focusDirty = true;
			layout ();
			if (moved) list.sendSelectionEvent (SWT.Selection);
			return LRESULT.ZERO;
		}
		int from = extend ? (anchor >= 0 && anchor < count ? anchor : caret) : target;
		boolean changed = caret != target || !model.isSelected (target) || model.getSelectionCount () != Math.abs (target - from) + 1;
		model.deselectAll ();
		model.select (Math.min (from, target), Math.max (from, target));
		caret = target;
		rangeEnd = target;
		if (!extend) anchor = target;
		if (key == OS.VK_HOME) {
			top = 0;
		} else if (key == OS.VK_END) {
			top = count;
		} else {
			scrollIntoView (target);
		}
		selectionDirty = true;
		layout ();
		if (changed) list.sendSelectionEvent (SWT.Selection);
		return LRESULT.ZERO;
	}

	/*
	 * Space with the caret row outside the window (over resident rows the list box handles it):
	 * SIMPLE toggles the caret row; extended selects only the caret row (Shift: the rows from
	 * the anchor to it), as a click on it does.
	 */
	private LRESULT onSpace (boolean shift) {
		sync ();
		if (plan.entryOf (caret) >= 0 && parkedCaret == -1) return null;
		int count = model.getItemCount ();
		if (caret < 0 || caret >= count) return LRESULT.ZERO;
		if (!extended) {
			if (model.isSelected (caret)) {
				model.deselect (caret);
			} else {
				model.select (caret);
			}
		} else {
			int from = shift && anchor >= 0 && anchor < count ? anchor : caret;
			model.deselectAll ();
			model.select (Math.min (from, caret), Math.max (from, caret));
			if (!shift) anchor = caret;
		}
		selectionDirty = true;
		layout ();
		list.sendSelectionEvent (SWT.Selection);
		return LRESULT.ZERO;
	}

	/*
	 * SWT's Ctrl key handling of an extended list (List.WM_KEYDOWN), over logical rows: the caret
	 * moves without changing the selection (LB_SETCARETINDEX, which scrolls it into view); Space is
	 * left to WM_CHAR.
	 */
	private LRESULT onControlKey (int key) {
		sync ();
		int count = model.getItemCount ();
		int pageSize = Math.max (2, clientHeight / itemHeight);
		int newIndex = switch (key) {
			case OS.VK_SPACE -> -2;
			case OS.VK_UP -> Math.max (0, caret - 1);
			case OS.VK_DOWN -> Math.max (0, caret + 1);
			case OS.VK_PRIOR -> caret != top ? top : Math.max (0, top - (pageSize - 1));
			case OS.VK_NEXT -> {
				int bottom = top + pageSize - 1;
				yield Math.min (count - 1, caret != bottom ? bottom : bottom + pageSize - 1);
			}
			case OS.VK_HOME -> 0;
			case OS.VK_END -> count - 1;
			default -> -1;
		};
		if (newIndex == -1) return null;
		if (newIndex == -2) return LRESULT.ZERO;
		int uiState = (int) OS.SendMessage (list.handle, OS.WM_QUERYUISTATE, 0, 0);
		if ((uiState & OS.UISF_HIDEFOCUS) != 0) {
			OS.SendMessage (list.handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			OS.InvalidateRect (list.handle, null, true);
		}
		if (0 <= newIndex && newIndex < count) {
			caret = newIndex;
			scrollIntoView (newIndex);
			focusDirty = true;
			layout ();
		}
		return LRESULT.ZERO;
	}

	/**
	 * Type-ahead over the logical rows with the list box's rule: each character moves to the
	 * next row after the caret (wrapping) whose text starts with it, ignoring case, and selects
	 * it as a keyboard move does; no match changes nothing.
	 */
	LRESULT onChar (long wParam) {
		char key = (char) wParam;
		boolean ctrl = OS.GetKeyState (OS.VK_CONTROL) < 0, shift = OS.GetKeyState (OS.VK_SHIFT) < 0;
		if (key == ' ' && extended && ctrl && !shift) return toggleCaret ();
		if (key <= ' ' || ctrl) return null;
		sync ();
		int count = model.getItemCount ();
		if (count == 0) return LRESULT.ZERO;
		char upper = Character.toUpperCase (key), lower = Character.toLowerCase (key);
		for (int step = 1; step <= count; step++) {
			int row = (caret + step) % count;
			String text = model.getItem (row);
			if (text.isEmpty ()) continue;
			char first = text.charAt (0);
			if (first != key && Character.toUpperCase (first) != upper && Character.toLowerCase (first) != lower) continue;
			caret = row;
			scrollIntoView (row);
			if (!single && !extended) {
				/* SIMPLE: the caret moves, the selection stays; the list box still notifies */
				focusDirty = true;
				layout ();
				list.sendSelectionEvent (SWT.Selection);
				return LRESULT.ZERO;
			}
			model.deselectAll ();
			model.select (row);
			anchor = row;
			selectionDirty = true;
			layout ();
			list.sendSelectionEvent (SWT.Selection);
			return LRESULT.ZERO;
		}
		return LRESULT.ZERO;
	}

	/*
	 * SWT's Ctrl+Space (List.WM_CHAR) over logical rows: the caret row's selection toggles.
	 * Selecting moves the anchor to it (LB_SETSEL); deselecting does not, and SWT's
	 * LB_SETANCHORINDEX there is OS.LB_SETANCHORINDEX (0xf19c), which the list box ignores.
	 */
	private LRESULT toggleCaret () {
		sync ();
		if (caret < 0 || caret >= model.getItemCount ()) return null;
		if (model.isSelected (caret)) {
			model.deselect (caret);
		} else {
			model.select (caret);
			anchor = rangeEnd = caret;
		}
		selectionDirty = true;
		layout ();
		list.sendSelectionEvent (SWT.Selection);
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
		barState = barState == BAR_NONE ? BAR_DEFAULT : BAR_SET;
		/* the list box keeps the caret, the anchor and the top row index; the selection shifts */
		int first = plan.first ();
		int entry = plan.inserted (index, 1);
		if (entry >= 0) insertNative (entry, index);
		if (hScroll) extent = Math.max (extent, model.measureItem (index) + List.INSET);
		/* a row added after the window leaves the native rows, caret and anchor as they are */
		if (entry >= 0 || plan.first () != first) focusDirty = true;
		deferBarPaint = true;
		try {
			layout ();
		} finally {
			deferBarPaint = false;
		}
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
			barState = barState == BAR_NONE ? BAR_DEFAULT : BAR_SET;
			model.add ("", i);
			int entry = plan.inserted (i, 1);
			if (entry >= 0) insertNative (entry, i);
		}
		if (hScroll && count > itemCount) extent = Math.max (extent, List.INSET);
		focusDirty = true;
		layout ();
		writeExtent ();
	}

	/* one native LB_DELETESTRING: caret and anchor as the stock list box moves them */
	private void deleteRow (int index) {
		barState = BAR_SET;
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
		focusDirty = true;
		deferBarPaint = true;
		try {
			layout ();
		} finally {
			deferBarPaint = false;
		}
		writeExtent ();
	}

	void removeAll () {
		sync ();
		model.removeAll ();
		OS.SendMessage (list.handle, OS.LB_RESETCONTENT, 0, 0);
		plan.clear ();
		/* LB_RESETCONTENT: caret and top row 0, the anchor is kept, the bar is freed */
		top = caret = 0;
		barState = BAR_NONE;
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
		/* a visible list redraws once at the end (the bar is written); a hidden one adds row by row */
		if (list.getDrawing () && OS.IsWindowVisible (list.handle)) {
			barState = BAR_SET;
		} else {
			barState = texts.length == 0 ? BAR_NONE : texts.length == 1 ? BAR_DEFAULT : BAR_SET;
		}
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
		focusDirty = true;
		layout ();
	}

	private void writeExtent () {
		if (!hScroll || extent == writtenExtent) return;
		if ((int) OS.SendMessage (list.handle, OS.LB_GETHORIZONTALEXTENT, 0, 0) != extent) {
			OS.SendMessage (list.handle, OS.LB_SETHORIZONTALEXTENT, extent, 0);
		}
		writtenExtent = extent;
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
		int entry = plan.entryOf (index);
		if (single) {
			model.deselectAll ();
			model.select (index);
			caret = index;
			if (scroll) scrollIntoView (index);
			if (entry >= 0) mirror (OS.LB_SETCURSEL, entry, 0);
		} else {
			model.select (index);
			anchor = rangeEnd = index;
			if (scroll) caret = index;
			if (entry >= 0) mirror (OS.LB_SETSEL, 1, entry);
		}
		selectionDirty = true;
		layout ();
	}

	/*
	 * The resident rows get the message the original List sends for the same call, so the list box's
	 * internal state (its anchor, caret and last range, which user input reads later) evolves as in
	 * the original. Drawing is suspended: the list box may scroll; positionPlane puts the top back.
	 */
	private void mirror (int message, long wParam, long lParam) {
		long handle = list.handle;
		boolean redraw = list.getDrawing () && OS.IsWindowVisible (handle);
		if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		try {
			OS.SendMessage (handle, message, wParam, lParam);
			positionPlane ();
		} finally {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
		}
		focusDirty = true;
	}

	/* a logical range clamped to the resident rows, as native entries {from, to}, or null */
	private int [] residentRange (int start, int end) {
		int from = Math.max (start, plan.first ()), to = Math.min (end, plan.end () - 1);
		return from > to ? null : new int [] {from - plan.first (), to - plan.first ()};
	}

	/** Stock select (start, end, scroll) of a MULTI list: LB_SELITEMRANGEEX, caret and anchor unchanged. */
	void select (int start, int end, boolean scroll) {
		if (start == end) {
			select (start, scroll);
			return;
		}
		sync ();
		model.select (start, end);
		int [] range = residentRange (start, end);
		if (range != null) {
			/* LB_SELITEMRANGEEX deselects when both ends are equal: one resident row is selected as a range of one */
			if (range [0] == range [1]) {
				mirror (LB_SELITEMRANGE, 1, OS.MAKELPARAM (range [0], range [1]));
			} else {
				mirror (OS.LB_SELITEMRANGEEX, range [0], range [1]);
			}
		}
		selectionDirty = true;
		layout ();
		if (scroll) showSelection ();
	}

	/** LB_SELITEMRANGE (wParam: select, lParam: first and last row); not in OS. */
	static final int LB_SELITEMRANGE = 0x19b;

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
		if (plan.size () > 0) mirror (OS.LB_SETSEL, 1, -1);
		selectionDirty = true;
		layout ();
	}

	void deselect (int index) {
		sync ();
		int entry = plan.entryOf (index);
		if (single) {
			if (firstSelected () == index) {
				model.deselectAll ();
				if (entry >= 0) mirror (OS.LB_SETCURSEL, -1, 0);
			}
		} else {
			model.deselect (index);
			if (entry >= 0) mirror (OS.LB_SETSEL, 0, entry);
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
					if (plan.entryOf (selected) >= 0) mirror (OS.LB_SETCURSEL, -1, 0);
					break;
				}
			}
		} else {
			for (int index : indices) {
				model.deselect (index);
				int entry = index == -1 ? -1 : plan.entryOf (index);
				if (entry >= 0) mirror (OS.LB_SETSEL, 0, entry);
			}
		}
		selectionDirty = true;
		layout ();
	}

	void deselect (int start, int end) {
		sync ();
		if (start > end) return;
		if (single) {
			int selected = firstSelected ();
			if (selected != -1 && start <= selected && selected <= end) {
				model.deselectAll ();
				if (plan.entryOf (selected) >= 0) mirror (OS.LB_SETCURSEL, -1, 0);
			}
		} else {
			int count = model.getItemCount ();
			if (start < 0 && end < 0) return;
			if (start >= count && end >= count) return;
			model.deselect (start, end);
			int [] range = residentRange (Math.max (0, start), Math.min (count - 1, end));
			if (range != null) mirror (OS.LB_SELITEMRANGEEX, range [1], range [0]);
		}
		selectionDirty = true;
		layout ();
	}

	void deselectAll () {
		sync ();
		model.deselectAll ();
		if (plan.size () > 0) mirror (single ? OS.LB_SETCURSEL : OS.LB_SETSEL, single ? -1 : 0, single ? 0 : -1);
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
		int entry = plan.entryOf (index);
		if (entry >= 0) mirror (OS.LB_SETCARETINDEX, entry, 0);
		focusDirty = true;
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

	/* ---------------------------------------------------------------- accessibility */

	private boolean accessibilityInstalled;

	/**
	 * MSAA over logical rows. The list box's own accessible proxy sees only the resident rows; SWT's
	 * Accessible asks these listeners after the proxy, so row count, names, roles, states, bounds,
	 * hit tests, focus and selection answer for every row. Installed when an accessibility client
	 * first asks for the list (WM_GETOBJECT).
	 */
	void installAccessibility () {
		if (accessibilityInstalled) return;
		accessibilityInstalled = true;
		Accessible accessible = list.getAccessible ();
		accessible.addAccessibleListener (new AccessibleAdapter () {
			@Override
			public void getName (AccessibleEvent e) {
				if (isRow (e.childID)) e.result = model.getItem (e.childID);
			}
		});
		accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
			@Override
			public void getChildCount (AccessibleControlEvent e) {
				sync ();
				e.detail = model.getItemCount ();
			}

			@Override
			public void getChildren (AccessibleControlEvent e) {
				sync ();
				Object [] children = new Object [model.getItemCount ()];
				for (int i = 0; i < children.length; i++) children [i] = Integer.valueOf (i);
				e.children = children;
			}

			@Override
			public void getChildAtPoint (AccessibleControlEvent e) {
				sync ();
				Point point = list.toControl (e.x, e.y);
				Rectangle client = list.getClientArea ();
				if (!client.contains (point)) {
					e.childID = list.getBounds ().contains (list.getParent ().toControl (e.x, e.y)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
					return;
				}
				int pixels = DPIUtil.pointToPixel (point.y, list.getAutoscalingZoom ());
				int row = top + pixels / itemHeight;
				e.childID = row < model.getItemCount () ? row : ACC.CHILDID_SELF;
			}

			@Override
			public void getLocation (AccessibleControlEvent e) {
				if (!isRow (e.childID)) return;
				sync ();
				int zoom = list.getAutoscalingZoom ();
				RECT client = new RECT ();
				OS.GetClientRect (list.handle, client);
				Point origin = list.toDisplayInPixels (0, (e.childID - top) * itemHeight);
				e.x = DPIUtil.pixelToPoint (origin.x, zoom);
				e.y = DPIUtil.pixelToPoint (origin.y, zoom);
				e.width = DPIUtil.pixelToPoint (client.right - client.left, zoom);
				e.height = DPIUtil.pixelToPoint (itemHeight, zoom);
			}

			@Override
			public void getFocus (AccessibleControlEvent e) {
				sync ();
				if (!list.isFocusControl ()) {
					e.childID = ACC.CHILDID_NONE;
				} else {
					e.childID = model.getItemCount () > 0 ? caret : ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getRole (AccessibleControlEvent e) {
				if (isRow (e.childID)) e.detail = ACC.ROLE_LISTITEM;
			}

			@Override
			public void getState (AccessibleControlEvent e) {
				if (!isRow (e.childID)) return;
				sync ();
				int row = e.childID, state = ACC.STATE_SELECTABLE | ACC.STATE_FOCUSABLE;
				if (model.isSelected (row)) state |= ACC.STATE_SELECTED;
				if (row == caret && list.isFocusControl ()) state |= ACC.STATE_FOCUSED;
				if (row < top || row > top + Math.max (1, page)) state |= ACC.STATE_INVISIBLE | ACC.STATE_OFFSCREEN;
				e.detail = state;
			}

			@Override
			public void getSelection (AccessibleControlEvent e) {
				sync ();
				int [] selected = model.getSelectionIndices ();
				if (selected.length == 0) {
					e.childID = ACC.CHILDID_NONE;
				} else if (selected.length == 1) {
					e.childID = selected [0];
				} else {
					e.childID = ACC.CHILDID_MULTIPLE;
					Object [] children = new Object [selected.length];
					for (int i = 0; i < selected.length; i++) children [i] = Integer.valueOf (selected [i]);
					e.children = children;
				}
			}
		});
	}

	private boolean isRow (int childID) {
		return childID >= 0 && childID < model.getItemCount ();
	}

	/*
	 * After user input the list box has raised its own focus and selection events with resident
	 * row numbers; the logical caret row follows, so an accessibility client ends on the right row.
	 */
	private void notifyAccessibility () {
		if (!accessibilityInstalled || !list.isFocusControl () || model.getItemCount () == 0) return;
		Accessible accessible = list.getAccessible ();
		accessible.selectionChanged ();
		accessible.setFocus (caret);
	}

	/** Number of native rows (the resident window), for tests and diagnostics. */
	int residentCount () {
		return plan.size ();
	}
}

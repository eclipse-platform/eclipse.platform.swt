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

import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Viewport of a win32 drop-down Combo (<code>SWT.DROP_DOWN</code>, editable or
 * <code>SWT.READ_ONLY</code>).
 * <p>
 * The Combo keeps its logical rows in {@link VirtualListModel} and answers its item API from
 * it. The combo's own list box (the <code>ComboLBox</code> that drops down) holds only the
 * resident window ({@link VirtualRowWindowPlan}): the selected row, the caret and, while the
 * list is dropped, the visible rows, each with an overscan. Everything else is the native combo:
 * the edit field or the selected-item area, the drop and close, hover tracking, keys, the wheel,
 * type-ahead and the edit-text match all run natively on the window, after the viewport made the
 * window hold every row the native rule can reach (the target of a key, the first match of a
 * search). The resident window is native index <code>0 .. size - 1</code> for logical rows
 * <code>first .. first + size - 1</code>; a row that is not resident reads as native -1, which
 * is where the native rules start from row 0, so the window is placed to start at row 0 then.
 * </p><p>
 * The list box is subclassed ({@link #listProc}): its vertical bar is written with the logical
 * values after the list box writes its own (the bar of the stock drop-down: <code>0 .. count -
 * 1</code>, the page, the top row), its scroll-bar codes move the logical top row, and its
 * searches and keys are prepared before the list box sees them. The rules are measured on the
 * stock combo (<code>ComboRulesOracle</code>, <code>ComboMessageOracle</code>), see
 * <code>docs/m3/LIST_MEDIATED_VIEWPORT.md</code>.
 * </p>
 */
final class ComboViewport {
	/** The viewport is the default Combo mode; <code>-Dswt.widget.mode.Combo=original</code> (or the global <code>swt.widget.mode</code>) selects the native Combo at startup. */
	static final boolean ENABLED = WidgetMode.of ("Combo", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;
	static final int MIN_OVERSCAN = 16;
	/** Rows apart that one window holds together (the selection, the caret and the rows an action needs); internal, tests lower it. */
	static final int NATIVE_REACH = Math.max (1, Integer.getInteger ("swt.viewport.combo.reach", 2000));
	/** List-box messages the combo sends (not in OS): prefix search, start and end of a drop's tracking. */
	static final int LB_FINDSTRING = 0x18f, LB_CARETON = 0x1a3, LBCB_STARTTRACK = 0x1ae, LBCB_ENDTRACK = 0x1af, WM_NCDESTROY = 0x82;
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.Combo");
	private static final ConcurrentHashMap<Long, ComboViewport> LISTS = new ConcurrentHashMap<> ();
	private static Callback listCallback;

	final Combo combo;
	final VirtualListModel model;
	private final VirtualRowWindowPlan plan = new VirtualRowWindowPlan ();
	private final boolean readOnly;
	private long hwndList, listWindowProc;
	private boolean installed, busy;
	/* the selected row when the list box no longer holds it (scrolled away while dropped), -1 otherwise */
	private int parked = -1;
	/*
	 * The list box's recorded drop entry (ComboTrackProbe, ComboReplayProbe): LBCB_STARTTRACK and every
	 * LB_SETCURSEL of a row record a native index, which inserts and deletes do not move; a cancel restores it and
	 * a commit sends CBN_SELCHANGE when the selection is at another index. In stock that index is a logical index,
	 * so it is kept here as recordedRow (a raw index, -1 unknown) with the window origin it was recorded under:
	 * while the window keeps that origin the list box answers like stock, after a window move the viewport does.
	 */
	private int recordedRow = -1, recordedFirst;
	/* between LBCB_STARTTRACK and LBCB_ENDTRACK (the combo also ends a tracking that never started when it drops) */
	private boolean tracking;
	private boolean selectionChangeOwed;
	/*
	 * The list box's caret (its focus row, where keys and type-ahead start) in logical rows. Inserts
	 * and removes do not move it as they move the selection (the stock list box keeps its index), so
	 * it is a lane of its own; parkedCaret is the native entry standing in for a caret outside the window.
	 * -1: no caret (LB_CARETON without a selection, which the combo sends when it gets the focus).
	 */
	private int caret, parkedCaret = -1;
	private int itemHeight = 1, page = 1;
	/*
	 * What the list box last did to its vertical bar (ListViewport's rule, ListBarOracle2): LB_RESETCONTENT
	 * frees it, the first row added afterwards leaves the default bar, every later insert or delete writes it.
	 * The viewport writes the logical bar only in the last state, so the freed and default bars stay the stock ones.
	 */
	static final int BAR_NONE = 0, BAR_DEFAULT = 1, BAR_SET = 2;
	private int barState = BAR_NONE;
	/* the result of the last CB_SETCURSEL in logical rows, as Combo.select compares it (0 when Verify vetoed) */
	private int lastSetResult = -1;
	private long measureDC, measureFont;

	ComboViewport (Combo combo) {
		this.combo = combo;
		this.readOnly = (combo.style & SWT.READ_ONLY) != 0;
		this.model = new VirtualListModel (this::measure, true);
	}

	/** Whether a Combo with this style is a viewport: drop-down combos (editable or read-only), not <code>SWT.SIMPLE</code>. */
	static boolean mediates (int style) {
		return ENABLED && (style & SWT.SIMPLE) == 0;
	}

	/* ---------------------------------------------------------------- layout */

	/** Takes over the (still empty) drop-down list box. */
	void install () {
		COMBOBOXINFO info = new COMBOBOXINFO ();
		info.cbSize = COMBOBOXINFO.sizeof;
		if (!OS.GetComboBoxInfo (combo.handle, info) || info.hwndList == 0) {
			combo.viewport = null;
			return;
		}
		synchronized (LISTS) {
			if (listCallback == null) listCallback = new Callback (ComboViewport.class, "listProc", 4);
		}
		long address = listCallback.getAddress ();
		if (address == 0) {
			combo.viewport = null;
			return;
		}
		hwndList = info.hwndList;
		listWindowProc = OS.GetWindowLongPtr (hwndList, OS.GWLP_WNDPROC);
		LISTS.put (hwndList, this);
		OS.SetWindowLongPtr (hwndList, OS.GWLP_WNDPROC, address);
		installed = true;
		plan.clear ();
		layout ();
	}

	/**
	 * Whether the viewport mediates: segments and automatic text direction rewrite every native row
	 * (Combo.applyListSegments), so they put every row into the list box and the combo is native from then on.
	 */
	boolean active () {
		if (installed && (combo.hooks (SWT.Segments) || combo.filters (SWT.Segments) || (combo.state & Widget.HAS_AUTO_DIRECTION) != 0)) uninstall ();
		return installed;
	}

	/** Puts every row into the list box, keeps the selection and hands the combo back to its native implementation. */
	void uninstall () {
		if (!installed) return;
		int selection = selection ();
		rebuild (0, model.getItemCount ());
		if (selection >= 0 && (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETCURSEL, 0, 0) != selection) {
			OS.CallWindowProc (Combo.ComboProc, combo.handle, OS.CB_SETCURSEL, selection, 0);
		}
		release ();
		combo.viewport = null;
		LOG.debug (() -> "Combo viewport handed back to the native combo (segments or automatic direction)");
	}

	private void release () {
		if (!installed) return;
		installed = false;
		if (OS.GetWindowLongPtr (hwndList, OS.GWLP_WNDPROC) == listCallback.getAddress ()) {
			OS.SetWindowLongPtr (hwndList, OS.GWLP_WNDPROC, listWindowProc);
		}
		LISTS.remove (hwndList);
	}

	/** Moves the resident window to the selection (closed) or to the visible rows (dropped) and writes the bar. */
	void layout () {
		if (busy || !installed) return;
		measureGeometry ();
		if (isDropped ()) {
			int topRow = topRow ();
			cover (topRow, topRow + page);
		} else {
			int selection = selection ();
			int anchor = selection >= 0 ? selection : Math.max (0, caret);
			cover (anchor, anchor);
		}
		syncFocus ();
		updateBar ();
	}

	private void measureGeometry () {
		int height = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETITEMHEIGHT, 0, 0);
		itemHeight = Math.max (1, height);
		RECT rect = new RECT ();
		OS.GetClientRect (hwndList, rect);
		page = Math.max (1, (rect.bottom - rect.top) / itemHeight);
	}

	/* rows of a dropped list: what the list box shows now or the visible item count it drops with */
	private int pageMax () {
		return Math.max (page, Math.max (1, combo.visibleCount));
	}

	private int overscan () {
		return Math.max (MIN_OVERSCAN, 2 * pageMax ());
	}

	private boolean isDropped () {
		return OS.IsWindowVisible (hwndList);
	}

	/**
	 * Makes the window hold rows <code>low .. high</code> with half an overscan around them (a row 0
	 * or a last row there is then native row 0 or the native last row), and, when they fit within
	 * {@link #NATIVE_REACH}, the selection, the caret and (dropped) the row a cancelled drop
	 * restores; the window holds at least a dropped page.
	 */
	private void cover (int low, int high) {
		int count = model.getItemCount ();
		if (count == 0) {
			if (plan.size () != 0) rebuild (0, 0);
			return;
		}
		int from = clamp (Math.min (low, high)), to = clamp (Math.max (low, high)) + 1;
		int [] optional = {selection (), caret, isDropped () ? recordedRow : -1};
		for (int row : optional) {
			if (row < 0 || row >= count) continue;
			int f = Math.min (from, row), t = Math.max (to, row + 1);
			if (t - f <= NATIVE_REACH) {
				from = f;
				to = t;
			}
		}
		int margin = overscan ();
		int needFirst = Math.max (0, from - margin / 2), needEnd = Math.min (count, to + margin / 2);
		boolean covered = plan.covers (needFirst, needEnd) && plan.end () <= count && plan.size () >= Math.min (count, pageMax () + 1);
		int wantFirst = Math.max (0, from - margin), wantEnd = Math.min (count, to + margin);
		int wantSize = Math.min (count, pageMax () + 1 + 2 * margin);
		if (wantEnd - wantFirst < wantSize) {
			wantFirst = Math.max (0, Math.min (wantFirst, wantEnd - wantSize));
			wantEnd = Math.min (count, wantFirst + wantSize);
		}
		boolean bloated = plan.size () > (wantEnd - wantFirst) + 2 * margin;
		if (covered && !bloated) return;
		rebuild (wantFirst, wantEnd - wantFirst);
	}

	private int clamp (int row) {
		return Math.max (0, Math.min (model.getItemCount () - 1, row));
	}

	/** Moves the native window: retire head and tail, insert head and tail, commit, then put the selection, the caret and the top row back. */
	private void rebuild (int nextFirst, int nextSize) {
		int selection = selection (), topRow = topRow ();
		boolean dropped = isDropped ();
		plan.plan (nextFirst, nextSize);
		busy = true;
		if (dropped) OS.DefWindowProc (hwndList, OS.WM_SETREDRAW, 0, 0);
		try {
			/* row by row, never LB_RESETCONTENT: a reset would free the bar, which the stock list box does only on reset */
			for (int i = 0; i < plan.retiredHead (); i++) deleteNative (0);
			int remaining = plan.size () - plan.retiredHead ();
			for (int i = 0; i < plan.retiredTail (); i++) deleteNative (--remaining);
			int head = plan.insertedHead ();
			for (int i = 0; i < head; i++) insertNative (i, nextFirst + i);
			int tail = plan.insertedTail ();
			int at = (int) OS.SendMessage (hwndList, OS.LB_GETCOUNT, 0, 0);
			for (int i = 0; i < tail; i++) insertNative (at + i, nextFirst + nextSize - tail + i);
			plan.commit ();
			restoreFocus (selection);
			if (dropped) positionPlane (topRow);
		} finally {
			if (dropped) {
				OS.DefWindowProc (hwndList, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (hwndList, null, true);
			}
			busy = false;
		}
		updateBar ();
		if (LOG.isTraceEnabled ()) LOG.trace (() -> "Combo window: rows " + plan.first () + "+" + plan.size () + " of " + model.getItemCount ()
				+ (dropped ? ", dropped, top " + topRow : ", closed") + " (" + plan.touched () + " native rows touched)");
	}

	private void insertNative (int entry, int row) {
		TCHAR buffer = new TCHAR (model.getItem (row), true);
		long result = OS.SendMessage (hwndList, OS.LB_INSERTSTRING, entry, buffer);
		if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) combo.error (SWT.ERROR_ITEM_NOT_ADDED);
		inserted ();
	}

	private void deleteNative (int entry) {
		OS.SendMessage (hwndList, OS.LB_DELETESTRING, entry, 0);
		barState = BAR_SET;
	}

	/* a row went into the list box: the freed bar becomes the default one, the default one a written one */
	private void inserted () {
		barState = barState == BAR_NONE ? BAR_DEFAULT : BAR_SET;
	}

	/*
	 * The selected row and the caret where the window holds them; a selection outside it is parked,
	 * a caret outside it stands on the nearest window edge (parkedCaret).
	 */
	private void restoreFocus (int selection) {
		int size = plan.size ();
		parkedCaret = -1;
		if (size == 0) {
			parked = selection;
			return;
		}
		int c = caret < 0 ? -1 : caret - plan.first ();
		if (caret >= 0 && (c < 0 || c >= size)) {
			c = c < 0 ? 0 : size - 1;
			parkedCaret = c;
		}
		int s = selection >= 0 ? plan.entryOf (selection) : -1;
		parked = selection >= 0 && s < 0 ? selection : -1;
		setSingleState (c, s);
	}

	/* puts caret and selection back natively when the window or an insert or remove moved them */
	private void syncFocus () {
		int size = plan.size ();
		if (size == 0) return;
		int c = caret < 0 ? -1 : caret - plan.first ();
		if (caret >= 0 && (c < 0 || c >= size)) c = parkedCaret >= 0 ? parkedCaret : c < 0 ? 0 : size - 1;
		int selection = selection ();
		int s = selection >= 0 ? plan.entryOf (selection) : -1;
		if (s >= 0 && c == -1) c = s;
		if ((int) OS.SendMessage (hwndList, OS.LB_GETCARETINDEX, 0, 0) == c && (int) OS.SendMessage (hwndList, OS.LB_GETCURSEL, 0, 0) == s) return;
		boolean dropped = isDropped ();
		busy = true;
		if (dropped) OS.DefWindowProc (hwndList, OS.WM_SETREDRAW, 0, 0);
		int topRow = topRow ();
		try {
			restoreFocus (selection);
			if (dropped) positionPlane (topRow);
		} finally {
			if (dropped) {
				OS.DefWindowProc (hwndList, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (hwndList, null, true);
			}
			busy = false;
		}
	}

	/*
	 * Single selection: caret on entry c, selection on entry s (or none). LB_SETCURSEL moves both and
	 * LB_SETCARETINDEX fails while a row is selected, so a selection apart from the caret is shifted into
	 * place by deleting and re-inserting the rows between them (ListViewport.writeSingleState).
	 */
	private void setSingleState (int c, int s) {
		int nativeCaret = (int) OS.SendMessage (hwndList, OS.LB_GETCARETINDEX, 0, 0);
		int nativeSelection = (int) OS.SendMessage (hwndList, OS.LB_GETCURSEL, 0, 0);
		if (s >= 0 && c == -1) c = s;
		if (nativeCaret == c && nativeSelection == s) return;
		if (c == -1) {
			/* no caret: LB_CARETON resets it when nothing is selected (ComboFocus) */
			if (nativeSelection != -1) setCursel (-1);
			OS.SendMessage (hwndList, LB_CARETON, 0, 0);
		} else if (s == -1) {
			setCursel (c);
			setCursel (-1);
		} else if (s == c) {
			setCursel (c);
		} else if (s > c) {
			for (int i = c; i < s; i++) deleteNative (c);
			setCursel (c);
			for (int i = s - 1; i >= c; i--) insertNative (c, plan.first () + i);
		} else {
			char [] empty = {0};
			for (int i = s; i < c; i++) {
				OS.SendMessage (hwndList, OS.LB_INSERTSTRING, s, empty);
				inserted ();
			}
			setCursel (c);
			for (int i = s; i < c; i++) deleteNative (s);
		}
	}

	/* LB_SETCURSEL of a row also records it for the drop (ComboTrackProbe) */
	private void setCursel (int entry) {
		OS.SendMessage (hwndList, OS.LB_SETCURSEL, entry, 0);
		if (entry >= 0) record (entry);
	}

	private void record (int entry) {
		recordedRow = plan.first () + entry;
		recordedFirst = plan.first ();
	}

	/* after native input: the list box's caret is the logical one (unless it is the stand-in for a caret outside the window) */
	private void captureCaret () {
		int size = plan.size ();
		if (size == 0) return;
		int entry = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETCARETINDEX, 0, 0);
		if (entry == -1) {
			caret = -1;
			parkedCaret = -1;
		} else if (entry >= 0 && entry < size && entry != parkedCaret) {
			caret = plan.first () + entry;
			parkedCaret = -1;
		}
	}

	/** Puts the native top row of the window on a logical row (dropped list). */
	private void positionPlane (int topRow) {
		if (plan.size () == 0) return;
		int wanted = Math.max (0, Math.min (topRow - plan.first (), plan.size () - 1));
		if ((int) OS.SendMessage (hwndList, OS.LB_GETTOPINDEX, 0, 0) != wanted) OS.SendMessage (hwndList, OS.LB_SETTOPINDEX, wanted, 0);
	}

	/**
	 * The vertical bar of the list box with the stock values (0 .. count - 1, the fully visible rows,
	 * the logical top row). The list box writes its own bar on every change; it is overwritten with
	 * the logical values once the list box writes it (BAR_SET).
	 */
	void updateBar () {
		int count = model.getItemCount ();
		if (!installed || barState != BAR_SET || plan.size () == 0 || count == 0) return;
		SCROLLINFO info = new SCROLLINFO ();
		info.cbSize = SCROLLINFO.sizeof;
		info.fMask = OS.SIF_ALL;
		OS.GetScrollInfo (hwndList, OS.SB_VERT, info);
		/* the page the list box writes: its fully visible rows (the default bar of a reset list has none) */
		measureGeometry ();
		int max = count - 1, pageSize = Math.min (page, max + 1);
		int pos = Math.max (0, Math.min (topRow (), max - Math.max (pageSize - 1, 0)));
		if (info.nMin == 0 && info.nMax == max && info.nPage == pageSize && info.nPos == pos) return;
		info.fMask = OS.SIF_RANGE | OS.SIF_PAGE | OS.SIF_POS;
		info.nMin = 0;
		info.nMax = max;
		info.nPage = pageSize;
		info.nPos = pos;
		OS.SetScrollInfo (hwndList, OS.SB_VERT, info, true);
	}

	/* ---------------------------------------------------------------- resident state */

	/** The selected logical row (CB_GETCURSEL): the list box's selection, or the parked one. */
	int selection () {
		if (!installed || plan.size () == 0) return parked;
		int entry = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETCURSEL, 0, 0);
		if (entry >= 0 && entry < plan.size ()) {
			parked = -1;
			return plan.first () + entry;
		}
		return parked;
	}

	private int topRow () {
		if (plan.size () == 0) return 0;
		int entry = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETTOPINDEX, 0, 0);
		return plan.first () + Math.max (0, Math.min (entry, plan.size () - 1));
	}

	/** The native index of a row after the window was made to hold it. */
	private int residentEntry (int row) {
		cover (row, row);
		return plan.entryOf (row);
	}

	/* ---------------------------------------------------------------- the list box */

	/** Window procedure of every subclassed drop-down list box. */
	static long listProc (long hwnd, long msg, long wParam, long lParam) {
		ComboViewport viewport = LISTS.get (hwnd);
		if (viewport == null) return OS.DefWindowProc (hwnd, (int) msg, wParam, lParam);
		return viewport.listMessage (hwnd, (int) msg, wParam, lParam);
	}

	private long listMessage (long hwnd, int msg, long wParam, long lParam) {
		long proc = listWindowProc;
		if (busy || !installed) return OS.CallWindowProc (proc, hwnd, msg, wParam, lParam);
		switch (msg) {
			case WM_NCDESTROY -> {
				release ();
				return OS.CallWindowProc (proc, hwnd, msg, wParam, lParam);
			}
			case OS.WM_VSCROLL -> {
				return onVScroll (wParam);
			}
			case OS.WM_MOUSEWHEEL -> {
				return onWheel (hwnd, wParam, lParam);
			}
			case OS.WM_KEYDOWN -> onKeyDown (wParam);
			case OS.WM_CHAR -> onChar (wParam);
			case LB_FINDSTRING, OS.LB_FINDSTRINGEXACT -> {
				return onFind (hwnd, msg, wParam, lParam);
			}
			case LBCB_ENDTRACK -> {
				return onEndTrack (hwnd, wParam, lParam);
			}
			default -> {}
		}
		long code = OS.CallWindowProc (proc, hwnd, msg, wParam, lParam);
		if (installed) afterNative (msg, wParam);
		return code;
	}

	/** After the list box handled a message of the combo or of the user: track the row a cancel restores, follow the dropped rows, write the bar. */
	void afterNative (int msg, long wParam) {
		switch (msg) {
			case LBCB_STARTTRACK -> {
				tracking = true;
				int entry = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETCURSEL, 0, 0);
				if (entry >= 0) {
					record (entry);
				} else {
					recordedRow = -1;
				}
				updateBar ();
			}
			case OS.LB_SETCURSEL -> {
				/* LB_SETCURSEL (-1) leaves the recorded entry (a cancel after typing restores it, ComboReplayProbe) */
				int entry = (int) wParam;
				if (entry >= 0 && entry < plan.size ()) record (entry);
				parked = -1;
				captureCaret ();
				updateBar ();
			}
			case OS.WM_TIMER, OS.WM_MOUSEMOVE, OS.WM_LBUTTONUP -> {
				captureCaret ();
				if (isDropped ()) layout ();
			}
			case OS.LB_RESETCONTENT -> {
				/* LB_RESETCONTENT: caret and top row 0, the bar freed (Combo.removeAll and the combo's own resets) */
				caret = 0;
				parkedCaret = -1;
				barState = BAR_NONE;
				recordedRow = -1;
			}
			case OS.LB_INSERTSTRING, OS.LB_ADDSTRING -> {
				inserted ();
				updateBar ();
			}
			case OS.LB_DELETESTRING -> {
				barState = BAR_SET;
				updateBar ();
			}
			case OS.WM_KEYDOWN, OS.WM_CHAR, OS.WM_LBUTTONDOWN, OS.LB_SETCARETINDEX, LB_CARETON -> {
				captureCaret ();
				updateBar ();
			}
			case OS.WM_SIZE, OS.WM_WINDOWPOSCHANGED, OS.WM_SHOWWINDOW, OS.LB_SETTOPINDEX -> updateBar ();
			default -> {}
		}
	}

	/** Scroll-bar codes of the dropped list over logical rows: line 1, page = visible rows - 1, a 32-bit thumb. */
	long onVScroll (long wParam) {
		measureGeometry ();
		int code = OS.LOWORD (wParam);
		int topRow = topRow ();
		int row;
		switch (code) {
			case OS.SB_LINEDOWN -> row = topRow + 1;
			case OS.SB_LINEUP -> row = topRow - 1;
			case OS.SB_PAGEDOWN -> row = topRow + Math.max (1, page - 1);
			case OS.SB_PAGEUP -> row = topRow - Math.max (1, page - 1);
			case OS.SB_TOP -> row = 0;
			case OS.SB_BOTTOM -> row = model.getItemCount ();
			case OS.SB_THUMBTRACK, OS.SB_THUMBPOSITION -> {
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_TRACKPOS;
				OS.GetScrollInfo (hwndList, OS.SB_VERT, info);
				row = (info.nTrackPos & 0xFFFF) == OS.HIWORD (wParam) ? info.nTrackPos : OS.HIWORD (wParam);
			}
			default -> {
				return 0;
			}
		}
		/* the list box writes its bar on every scroll code, also from the default bar of a freshly reset list */
		barState = BAR_SET;
		setTopRow (row);
		return 0;
	}

	/** The dropped list's top row: at most a full page above the last row, as the list box keeps it. */
	void setTopRow (int row) {
		measureGeometry ();
		int count = model.getItemCount ();
		int topRow = Math.max (0, Math.min (row, count - page));
		cover (topRow, topRow + page);
		busy = true;
		try {
			positionPlane (topRow);
		} finally {
			busy = false;
		}
		updateBar ();
	}

	/**
	 * The wheel: the list box computes the lines itself (wheel settings, partial deltas) within a
	 * window that holds them, then the window follows the new top row.
	 */
	long onWheel (long hwnd, long wParam, long lParam) {
		measureGeometry ();
		int [] lines = new int [1];
		OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, lines, 0);
		int delta = Math.abs ((short) OS.HIWORD (wParam));
		int reach = lines [0] < 0 || lines [0] > page ? page : lines [0];
		reach = reach * (delta / 120 + 1);
		int topRow = topRow ();
		cover (topRow - reach, topRow + page + reach);
		long code = OS.CallWindowProc (listWindowProc, hwnd, OS.WM_MOUSEWHEEL, wParam, lParam);
		layout ();
		return code;
	}

	/*
	 * Keys the combo passes to its list box (closed: Up, Down, Left, Right, Prior, Next, Home, End and
	 * the wheel as Up and Down; dropped: the same). The list box moves from its selection, or from
	 * row 0 without one, by one row, by the list box's page - 1, or to the first or the last row; a
	 * dropped list puts the new row on top. The window is made to hold all of that natively.
	 */
	private void onKeyDown (long wParam) {
		int key = (int) wParam;
		if (key != OS.VK_UP && key != OS.VK_DOWN && key != OS.VK_LEFT && key != OS.VK_RIGHT && key != OS.VK_PRIOR && key != OS.VK_NEXT
				&& key != OS.VK_HOME && key != OS.VK_END) return;
		int count = model.getItemCount ();
		if (count == 0) return;
		measureGeometry ();
		int selection = selection ();
		int base = selection >= 0 ? selection : 0, reach = page + 1;
		int low = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_END -> count - 1;
			case OS.VK_PRIOR -> base - reach;
			case OS.VK_NEXT -> base;
			default -> base - 1;
		};
		int high = switch (key) {
			case OS.VK_HOME -> 0;
			case OS.VK_END -> count - 1;
			case OS.VK_PRIOR -> base;
			case OS.VK_NEXT -> base + reach;
			default -> base + 1;
		};
		if (selection < 0) low = 0;
		if (isDropped ()) high += page + 1;
		cover (low, high);
	}

	/* type-ahead of a read-only combo: the next row after the selection (wrapping) whose text starts with the character */
	private void onChar (long wParam) {
		char key = (char) wParam;
		if (key < ' ' || !readOnly) return;
		int count = model.getItemCount ();
		if (count == 0) return;
		int selection = selection ();
		char upper = Character.toUpperCase (key), lower = Character.toLowerCase (key);
		for (int step = 1; step <= count; step++) {
			int row = selection < 0 ? step - 1 : (selection + step) % count;
			String text = model.getItem (row);
			if (text.isEmpty ()) continue;
			char first = text.charAt (0);
			if (first != key && Character.toUpperCase (first) != upper && Character.toLowerCase (first) != lower) continue;
			measureGeometry ();
			cover (row, row + (isDropped () ? page + 1 : 0));
			return;
		}
	}

	/*
	 * LB_FINDSTRING (prefix) and LB_FINDSTRINGEXACT (whole text), both ignoring case, from the combo:
	 * the edit-text match of an editable combo when it drops and while the user types. The window is
	 * made to hold the first logical match and the rows after it, so the native search finds it.
	 */
	private long onFind (long hwnd, int msg, long wParam, long lParam) {
		int start = (int) wParam;
		int from = start >= 0 && start < plan.size () ? plan.first () + start : -1;
		String text = "";
		if (lParam != 0) {
			int length = OS.wcslen (lParam);
			char [] chars = new char [length];
			OS.MoveMemory (chars, lParam, length * 2);
			text = new String (chars);
		}
		int match = find (text, from, msg == OS.LB_FINDSTRINGEXACT);
		/* the combo puts caret and top on the match, or on row 0 when there is none */
		int anchor = Math.max (0, match);
		measureGeometry ();
		cover (anchor, anchor + pageMax () + 1);
		if (from >= 0) start = plan.entryOf (from);
		long code = OS.CallWindowProc (listWindowProc, hwnd, msg, start, lParam);
		if (LOG.isTraceEnabled ()) {
			int found = (int) code;
			LOG.trace (() -> "Combo search '" + ((msg == LB_FINDSTRING) ? "prefix" : "exact") + "': logical " + match + ", native " + found + " of window " + plan.first ());
		}
		return code;
	}

	/** First row after <code>from</code> (wrapping; -1 searches from row 0) whose text starts with, or equals, the text, ignoring case. */
	int find (String text, int from, boolean exact) {
		int count = model.getItemCount ();
		for (int step = 1; step <= count; step++) {
			int row = from < 0 ? step - 1 : (from + step) % count;
			String item = model.getItem (row);
			if (exact ? item.equalsIgnoreCase (text) : item.regionMatches (true, 0, text, 0, text.length ())) return row;
		}
		return -1;
	}

	/*
	 * End of a drop (ComboTrackProbe). A cancel (0) makes the list box restore its recorded entry,
	 * which after window moves may name another row: the row selected when the drop started (or
	 * by the last keyboard move) is put back. A commit (1) keeps the list box's selection (a parked
	 * one is made resident first) and the list box sends CBN_SELCHANGE when its recorded entry is
	 * not the selected one: the recorded entry is set so that this answers whether the logical
	 * selection changed, or the notification is sent here when the list box cannot tell.
	 */
	private long onEndTrack (long hwnd, long wParam, long lParam) {
		boolean wasTracking = tracking;
		tracking = false;
		if (wParam == 0) {
			long code = OS.CallWindowProc (listWindowProc, hwnd, LBCB_ENDTRACK, wParam, lParam);
			/* a cancel restores the recorded index only when a tracking was started (ComboReplayProbe: drop of a closed combo) */
			if (wasTracking && recordedRow >= 0 && plan.first () != recordedFirst) {
				/* the list box restored its raw entry under another window origin: put back the row stock restores */
				int entry = recordedRow < model.getItemCount () ? residentEntry (recordedRow) : -1;
				busy = true;
				try {
					setCursel (entry);
				} finally {
					busy = false;
				}
				parked = -1;
			}
			updateBar ();
			return code;
		}
		/* selection () first: a key of a closed combo selects natively before the combo ends its tracking */
		if (selection () >= 0 && parked >= 0) residentEntry (parked);
		if (recordedRow >= 0 && plan.first () != recordedFirst) {
			int current = (int) OS.CallWindowProc (listWindowProc, hwndList, OS.LB_GETCURSEL, 0, 0);
			int row = current >= 0 ? plan.first () + current : -1;
			boolean stockNotifies = recordedRow != row;
			boolean nativeNotifies = recordedRow - recordedFirst != current;
			if (!stockNotifies && nativeNotifies && current >= 0) {
				busy = true;
				try {
					setCursel (current);
				} finally {
					busy = false;
				}
			}
			long code = OS.CallWindowProc (listWindowProc, hwnd, LBCB_ENDTRACK, wParam, lParam);
			updateBar ();
			if (stockNotifies && !nativeNotifies) oweSelectionChange ();
			return code;
		}
		long code = OS.CallWindowProc (listWindowProc, hwnd, LBCB_ENDTRACK, wParam, lParam);
		updateBar ();
		return code;
	}

	/*
	 * The logical selection changed during the drop but the list box may not see it (its recorded entry names
	 * the same native row after window moves). A click commits with CBN_SELCHANGE after CBN_CLOSEUP, so the
	 * notification is owed until the event loop runs: a CBN_SELCHANGE of the combo before that pays it.
	 */
	private void oweSelectionChange () {
		if (selectionChangeOwed) return;
		selectionChangeOwed = true;
		combo.display.asyncExec (() -> {
			if (!selectionChangeOwed) return;
			selectionChangeOwed = false;
			if (!combo.isDisposed () && combo.viewport == this) combo.wmCommandChild (OS.CBN_SELCHANGE << 16, combo.handle);
		});
	}

	/** Notifications of the combo (Combo.wmCommandChild): CBN_DROPDOWN prepares the window, CBN_SELCHANGE pays an owed one. */
	void onNotification (int code) {
		switch (code) {
			case OS.CBN_DROPDOWN -> beforeDrop ();
			case OS.CBN_SELCHANGE -> selectionChangeOwed = false;
			default -> {}
		}
	}

	/** CBN_DROPDOWN: the list box is about to drop; it will show the selection (read-only) or the edit text's first match on top. */
	void beforeDrop () {
		int count = model.getItemCount ();
		if (count == 0) return;
		measureGeometry ();
		int anchor;
		if (readOnly) {
			anchor = Math.max (0, selection ());
		} else {
			String text = combo.getText ();
			anchor = text.isEmpty () ? 0 : Math.max (0, find (text, -1, false));
		}
		cover (anchor, anchor + pageMax () + 1);
	}

	/* ---------------------------------------------------------------- items */

	int getItemCount () {
		return model.getItemCount ();
	}

	String getItem (int index) {
		if (index < 0 || index >= model.getItemCount ()) combo.error (SWT.ERROR_INVALID_RANGE);
		return model.getItem (index);
	}

	/**
	 * Combo.indexOf: the first equal row at or after <code>start</code>; -1 when there is none or the start is out of
	 * range, as the Javadoc says. Stock compares an empty string row by row from a negative start and fails there
	 * with ERROR_INVALID_RANGE (upstream defect COMBO-1, list-viewport/UPSTREAM_DEFECTS.tsv).
	 */
	int indexOf (String string, int start) {
		if (start < 0) return -1;
		if (string.isEmpty ()) {
			int count = model.getItemCount ();
			for (int i = start; i < count; i++) {
				if (string.equals (getItem (i))) return i;
			}
			return -1;
		}
		return model.indexOf (string, start);
	}

	void add (String string, int index) {
		int count = model.getItemCount ();
		if (index < 0 || index > count) combo.error (SWT.ERROR_INVALID_RANGE);
		/* a dropped list keeps its top row index over an insert: let the list box insert it */
		coverForEdit (Math.min (index, Math.max (0, count - 1)), Math.min (index, Math.max (0, count - 1)));
		model.add (ListViewport.nativeText (string), index);
		if (parked >= index) parked++;
		int entry = plan.inserted (index, 1);
		if (entry >= 0) {
			busy = true;
			try {
				insertNative (entry, index);
			} finally {
				busy = false;
			}
		}
		layout ();
		combo.setScrollWidth (new TCHAR (string, true).chars, true);
	}

	/* removes logical rows start .. end; resident rows through the combo, which clears its text when the selected row goes */
	private void deleteRows (int start, int end) {
		int removed = end - start + 1;
		boolean dropped = isDropped ();
		boolean nativeEdit = coverForEdit (start, end);
		int topBefore = dropped ? topRow () : 0;
		/* each stock CB_DELETESTRING at start: the caret steps back when it is the deleted row, then stays inside the list */
		for (int i = 0, countAfter = model.getItemCount () - 1; i < removed; i++, countAfter--) {
			if (start == caret && caret > 0) caret--;
			if (caret >= countAfter) caret = Math.max (0, countAfter - 1);
		}
		model.remove (start, end);
		if (parked > end) {
			parked -= removed;
		} else if (parked >= start) {
			parked = -1;
		}
		int resident = plan.removed (start, removed);
		if (resident > 0) {
			int entry = plan.removedEntry (start);
			busy = true;
			try {
				for (int i = 0; i < resident; i++) OS.CallWindowProc (Combo.ComboProc, combo.handle, OS.CB_DELETESTRING, entry, 0);
				barState = BAR_SET;
			} finally {
				busy = false;
			}
		}
		if (dropped && !nativeEdit && model.getItemCount () > 0) afterDelete (topBefore);
	}

	/*
	 * An item change of a dropped list: the list box keeps its top row index and may scroll its caret into view,
	 * which only the native list box does exactly (a top row beyond count - page stays). The window is made to hold
	 * the changed rows with the visible page when they lie within the reach; answers whether it does.
	 */
	private boolean coverForEdit (int start, int end) {
		if (!isDropped () || model.getItemCount () == 0) return false;
		measureGeometry ();
		int topRow = topRow ();
		int low = Math.min (start, topRow), high = Math.max (end, topRow + page);
		if (high - low > NATIVE_REACH) return false;
		cover (low, high);
		return true;
	}

	/*
	 * LB_DELETESTRING keeps the top row index (within the list) and then scrolls the caret into view
	 * (ComboReplayProbe: caret 161, top 34, a row deleted below the page: top 142); a dropped list shows it.
	 */
	private void afterDelete (int topBefore) {
		measureGeometry ();
		int count = model.getItemCount ();
		int topRow = Math.max (0, Math.min (topBefore, count - 1));
		if (caret >= 0 && caret < count) {
			if (caret < topRow) {
				topRow = caret;
			} else if (caret >= topRow + page) {
				topRow = caret - page + 1;
			}
		}
		setTopRow (topRow);
	}

	/** Combo.remove (int, boolean) over logical rows. */
	void remove (int index, boolean notify) {
		if (index < 0 || index >= model.getItemCount ()) combo.error (SWT.ERROR_INVALID_RANGE);
		String text = model.getItem (index);
		int length = OS.GetWindowTextLength (combo.handle);
		deleteRows (index, index);
		/* the combo had exactly one item (Combo.remove: Bug#440671) */
		if (model.getItemCount () == 0) OS.SendMessage (combo.handle, OS.CB_RESETCONTENT, 0, 0);
		layout ();
		combo.setScrollWidth (new TCHAR (text, true).chars, false);
		if (notify && length != OS.GetWindowTextLength (combo.handle)) combo.sendEvent (SWT.Modify);
	}

	/** Combo.remove (int, int) over logical rows. */
	void remove (int start, int end) {
		int count = model.getItemCount ();
		if (!(0 <= start && start <= end && end < count)) combo.error (SWT.ERROR_INVALID_RANGE);
		int textLength = OS.GetWindowTextLength (combo.handle);
		int newWidth = beginMeasure ();
		try {
			for (int i = start; i <= end; i++) newWidth = Math.max (newWidth, measure (model.getItem (i)));
		} finally {
			endMeasure ();
		}
		deleteRows (start, end);
		if (model.getItemCount () == 0) OS.SendMessage (combo.handle, OS.CB_RESETCONTENT, 0, 0);
		layout ();
		combo.setScrollWidth (newWidth, false);
		if (textLength != OS.GetWindowTextLength (combo.handle)) combo.sendEvent (SWT.Modify);
	}

	/** Before Combo.removeAll resets the combo: the logical rows go. */
	void removeAll () {
		model.removeAll ();
		plan.clear ();
		parked = parkedCaret = recordedRow = -1;
		caret = 0;
	}

	/** Combo.setItems over logical rows. */
	void setItems (String [] items) {
		combo.setScrollWidth (0);
		busy = true;
		try {
			OS.CallWindowProc (Combo.ComboProc, combo.handle, OS.CB_RESETCONTENT, 0, 0);
		} finally {
			busy = false;
		}
		barState = BAR_NONE;
		recordedRow = -1;
		plan.clear ();
		parked = parkedCaret = -1;
		caret = 0;
		String [] texts = new String [items.length];
		for (int i = 0; i < items.length; i++) texts [i] = ListViewport.nativeText (items [i]);
		model.setItems (texts);
		int newWidth = widest ();
		layout ();
		combo.setScrollWidth (newWidth + 3);
		combo.sendEvent (SWT.Modify);
	}

	/** Combo.setScrollWidth () over logical rows: the widest row (measured once per font) + 3. */
	void recomputeScrollWidth () {
		combo.setScrollWidth (widest () + 3);
	}

	private int widest () {
		if (model.getItemCount () == 0) return 0;
		beginMeasure ();
		try {
			return model.getWidestWidth ();
		} finally {
			endMeasure ();
		}
	}

	/* a DC with the combo's font for a run of measurements; widths measured with another font are forgotten */
	private int beginMeasure () {
		long hDC = OS.GetDC (combo.handle);
		long font = OS.SendMessage (combo.handle, OS.WM_GETFONT, 0, 0);
		if (font != 0) OS.SelectObject (hDC, font);
		if (font != measureFont) {
			model.invalidateWidths ();
			measureFont = font;
		}
		measureDC = hDC;
		return 0;
	}

	private void endMeasure () {
		long hDC = measureDC;
		measureDC = 0;
		OS.ReleaseDC (combo.handle, hDC);
	}

	/* the width Combo.setScrollWidth measures: DrawText, single line, no prefix */
	private int measure (String text) {
		boolean own = measureDC == 0;
		if (own) beginMeasure ();
		try {
			RECT rect = new RECT ();
			char [] chars = (text + '\0').toCharArray ();
			OS.DrawText (measureDC, chars, -1, rect, OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX);
			return rect.right - rect.left;
		} finally {
			if (own) endMeasure ();
		}
	}

	/* ---------------------------------------------------------------- selection */

	int getSelectionIndex () {
		return selection ();
	}

	/** Combo.select over logical rows: the row is made resident, CB_SETCURSEL runs through the combo (and its Verify), Modify when the selection changed. */
	void select (int index) {
		if (0 <= index && index < model.getItemCount ()) {
			int selection = selection ();
			int entry = residentEntry (index);
			int code = (int) OS.SendMessage (combo.handle, OS.CB_SETCURSEL, entry, 0);
			if (code != OS.CB_ERR && lastSetResult != selection) combo.sendEvent (SWT.Modify);
		}
	}

	/**
	 * Combo.deselect over logical rows. An index out of range is ignored, as the Javadoc says; stock compares -1
	 * with "no selection" and sends Modify (upstream defect COMBO-2, list-viewport/UPSTREAM_DEFECTS.tsv).
	 */
	void deselect (int index) {
		if (index < 0 || index >= model.getItemCount ()) return;
		if (index != selection ()) return;
		OS.SendMessage (combo.handle, OS.CB_SETCURSEL, -1, 0);
		combo.sendEvent (SWT.Modify);
		combo.clearSegments (false);
		combo.clearSegmentsCount--;
	}

	/**
	 * CB_SETCURSEL reaching the combo's window procedure (native index): Combo.windowProc's Verify
	 * of a read-only combo over logical rows, then the native combo.
	 */
	long onSetCursel (long wParam, long lParam) {
		int entry = (int) wParam;
		if (readOnly && (combo.hooks (SWT.Verify) || combo.filters (SWT.Verify))) {
			String oldText = combo.getText (), newText = null;
			if (entry == -1) {
				newText = "";
			} else if (0 <= entry && entry < plan.size ()) {
				newText = model.getItem (plan.first () + entry);
			}
			if (newText != null && !newText.equals (oldText)) {
				int length = OS.GetWindowTextLength (combo.handle);
				oldText = newText;
				newText = combo.verifyText (newText, 0, length, null);
				if (newText == null) {
					lastSetResult = 0;
					return 0;
				}
				if (!newText.equals (oldText)) {
					int index = indexOf (newText, 0);
					if (index != -1 && (entry < 0 || index != plan.first () + entry)) entry = residentEntry (index);
				}
			}
		}
		parked = -1;
		long code = OS.CallWindowProc (Combo.ComboProc, combo.handle, OS.CB_SETCURSEL, entry, lParam);
		lastSetResult = (int) code == OS.CB_ERR ? OS.CB_ERR : plan.first () + (int) code;
		updateBar ();
		return code;
	}

	int residentCount () {
		return plan.size ();
	}
}

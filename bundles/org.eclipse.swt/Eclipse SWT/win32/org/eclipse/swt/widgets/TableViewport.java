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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Viewport of a plain (not <code>SWT.VIRTUAL</code>) win32 Table.
 * <p>
 * The ListView of a mediated Table is owner data (<code>LVS_OWNERDATA</code>): the native control
 * keeps the row count, the selection, the focus and the selection mark, and asks for text, images,
 * indent and check state through <code>LVN_GETDISPINFO</code> for the rows it paints. It holds no
 * native item per row, so its own scroll bar already covers every logical row, and its keyboard,
 * caret, anchor and scrolling stay native. A <code>SWT.VIRTUAL</code> Table always was owner data
 * and keeps its own path; this class gives a plain Table the same native realization while it keeps
 * the plain contract: the application creates the items, <code>SetData</code> is never sent and the
 * native type-ahead finds rows by their text.
 * </p><p>
 * The native rules reproduced here were measured against a ListView with native items (oracle in
 * <code>docs/m3/table-viewport/evidence</code>): inserting and deleting rows shift the selection,
 * focus and mark identically, so structural edits stay one native message per row. Three rules
 * need mediation: the type-ahead (<code>LVN_ODFINDITEM</code>), a count shrink (the focus and mark
 * move to the new last row) and the column autosize (owner data measures only the rows in view).
 * </p><p>
 * Only window-bounded native work runs here, except where the native ListView with items does
 * the same row scan: the type-ahead and the column autosize.
 * </p>
 */
final class TableViewport {
	/** The viewport is the default Table mode; <code>-Dswt.widget.mode.Table=original</code> (or the global <code>swt.widget.mode</code>) selects the native Table at startup. */
	static final boolean ENABLED = WidgetMode.of ("Table", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;
	private static final System.Logger LOG = System.getLogger ("org.eclipse.swt.widgets.Table");
	static final int LVM_GETSELECTIONMARK = OS.LVM_FIRST + 66;
	/* LVFINDINFO flags; the ListView's type-ahead asks with LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP (0x26) */
	static final int LVFI_STRING = 0x2, LVFI_SUBSTRING = 0x4, LVFI_PARTIAL = 0x8, LVFI_WRAP = 0x20;
	/* NMLVFINDITEM: NMHDR, int iStart, then LVFINDINFO (flags, psz, ...) aligned to a pointer */
	static final int FIND_START = NMHDR.sizeof, FIND_FLAGS = NMHDR.sizeof + C.PTR_SIZEOF, FIND_STRING = FIND_FLAGS + C.PTR_SIZEOF;
	/* the ListView's incremental search string has at most this many characters */
	static final int FIND_MAX = 260;

	final Table table;

	TableViewport (Table table) {
		this.table = table;
	}

	/* ---------------------------------------------------------------- native notifications */

	/**
	 * Owner-data type-ahead (LVN_ODFINDITEM). The ListView keeps the incremental search string,
	 * its timeout and the start row; it asks for the first row from the start row, wrapping when
	 * asked, whose text begins with the string (or equals it without <code>LVFI_SUBSTRING</code>
	 * or <code>LVFI_PARTIAL</code>), ignoring case: the rule that reproduces the native search over
	 * native items.
	 */
	LRESULT findItem (long lParam) {
		int [] ints = new int [1];
		OS.MoveMemory (ints, lParam + FIND_START, 4);
		final int from = ints [0];
		OS.MoveMemory (ints, lParam + FIND_FLAGS, 4);
		int flags = ints [0];
		long [] pointer = new long [1];
		OS.MoveMemory (pointer, lParam + FIND_STRING, C.PTR_SIZEOF);
		int count = (int) OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
		if (count == 0 || pointer [0] == 0 || (flags & (LVFI_STRING | LVFI_SUBSTRING | LVFI_PARTIAL)) == 0) return new LRESULT (-1);
		String string = nativeString (pointer [0]);
		/* LVFI_SUBSTRING is the newer name of LVFI_PARTIAL: both ask for a prefix match */
		boolean partial = (flags & (LVFI_SUBSTRING | LVFI_PARTIAL)) != 0, wrap = (flags & LVFI_WRAP) != 0;
		int start = Math.max (0, Math.min (from, count));
		for (int k = 0; k < count; k++) {
			int row = start + k;
			if (row >= count) {
				if (!wrap) break;
				row -= count;
			}
			TableItem item = table._getItem (row, false);
			String text = item != null ? item.text : "";
			if (partial ? text.regionMatches (true, 0, string, 0, string.length ()) : text.equalsIgnoreCase (string)) {
				final int found = row;
				LOG.log (System.Logger.Level.TRACE, () -> "Table type-ahead '" + string + "' from " + from + ": row " + found);
				return new LRESULT (row);
			}
		}
		return new LRESULT (-1);
	}

	/* a NUL-terminated native string, read one character at a time so no read crosses its end */
	private static String nativeString (long psz) {
		StringBuilder result = new StringBuilder ();
		char [] c = new char [1];
		for (int i = 0; i < FIND_MAX; i++) {
			OS.MoveMemory (c, psz + 2L * i, 2);
			if (c [0] == 0) break;
			result.append (c [0]);
		}
		return result.toString ();
	}

	/**
	 * Owner-data range change (LVN_ODSTATECHANGED): shift-click, Shift+arrow and marquee
	 * selections arrive here instead of one LVN_ITEMCHANGED per row; a selection change makes
	 * the pending Selection event, as LVN_ITEMCHANGED does.
	 */
	void stateChanged (long lParam) {
		if (table.ignoreSelect) return;
		NMLVODSTATECHANGE change = new NMLVODSTATECHANGE ();
		OS.MoveMemory (change, lParam, NMLVODSTATECHANGE.sizeof);
		if (((change.uOldState ^ change.uNewState) & OS.LVIS_SELECTED) != 0) table.wasSelected = true;
	}

	/* ---------------------------------------------------------------- mediated API */

	/**
	 * Mediated <code>setItemCount</code> of a plain Table, in the phases of the Tree viewport:
	 * plan (old against new count), retire (release the items beyond a smaller count), commit
	 * (one <code>LVM_SETITEMCOUNT</code> instead of one native message per row), bind (create the
	 * items of a larger count: a plain Table answers every row with an item, as stock SWT does) and
	 * restore focus (rule measured on native rows deleted from the end: a focus or mark at or
	 * beyond the new end moves to the new last row; owner data would drop the focus instead).
	 */
	void setItemCount (int count) {
		long hwnd = table.handle;
		int itemCount = (int) OS.SendMessage (hwnd, OS.LVM_GETITEMCOUNT, 0, 0);
		if (count == itemCount) return;
		table.setDeferResize (true);
		table.setRedraw (false);
		int focus = (int) OS.SendMessage (hwnd, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
		int mark = (int) OS.SendMessage (hwnd, LVM_GETSELECTIONMARK, 0, 0);
		for (int index = count; index < itemCount; index++) {
			TableItem item = table._getItem (index, false);
			if (item != null && !item.isDisposed ()) item.release (false);
		}
		table._setItemCount (count, itemCount);
		for (int index = itemCount; index < count; index++) {
			table.items [index] = new TableItem (table, SWT.NONE, index, false);
		}
		boolean oldSelect = table.ignoreSelect, oldShrink = table.ignoreShrink;
		table.ignoreSelect = table.ignoreShrink = true;
		try {
			OS.SendMessage (hwnd, OS.LVM_SETITEMCOUNT, count, OS.LVSICF_NOINVALIDATEALL | OS.LVSICF_NOSCROLL);
			if (count < itemCount) restoreFocus (focus, mark, count);
		} finally {
			table.ignoreSelect = oldSelect;
			table.ignoreShrink = oldShrink;
		}
		/* as for a virtual Table: a count of zero does not redraw by itself */
		if (count == 0 && itemCount != 0) OS.InvalidateRect (hwnd, null, true);
		table.setRedraw (true);
		if (itemCount == 0) table.setScrollWidth (null, false);
		table.setDeferResize (false);
		LOG.log (System.Logger.Level.TRACE, () -> "Table count " + itemCount + " -> " + count + " (one native count change)");
	}

	/*
	 * After a count shrink. Setting the native focus also moves the selection mark, so the mark
	 * is put back afterwards: it stays where it was unless it lay beyond the new end.
	 */
	private void restoreFocus (int focus, int mark, int count) {
		if (count == 0) return;
		long hwnd = table.handle;
		if (focus >= count) {
			LVITEM lvItem = new LVITEM ();
			lvItem.state = lvItem.stateMask = OS.LVIS_FOCUSED;
			OS.SendMessage (hwnd, OS.LVM_SETITEMSTATE, count - 1, lvItem);
		}
		OS.SendMessage (hwnd, OS.LVM_SETSELECTIONMARK, 0, mark >= count ? count - 1 : mark);
	}

	/* ---------------------------------------------------------------- column width */

	/**
	 * Before the native column autosize of <code>TableColumn.pack</code>: an owner-data ListView
	 * measures only the rows in view, so the row with the widest estimate (text width with the
	 * Table's font, plus image and indent) is scrolled into view first. Redraw is off while the
	 * autosize runs.
	 * @return the top row to restore with {@link #restoreTop(int)}, or -1 when nothing moved
	 */
	int revealWidest (int column) {
		long hwnd = table.handle;
		int count = (int) OS.SendMessage (hwnd, OS.LVM_GETITEMCOUNT, 0, 0);
		if (count == 0) return -1;
		int widest = widestRow (column, count);
		int top = (int) OS.SendMessage (hwnd, OS.LVM_GETTOPINDEX, 0, 0);
		int page = Math.max (1, (int) OS.SendMessage (hwnd, OS.LVM_GETCOUNTPERPAGE, 0, 0));
		if (widest < 0 || top <= widest && widest < top + page) return -1;
		OS.SendMessage (hwnd, OS.LVM_ENSUREVISIBLE, widest, 0);
		return top;
	}

	/**
	 * Puts the top row back after {@link #revealWidest(int)}. Redraw is still off there, and
	 * <code>LVM_ENSUREVISIBLE</code> scrolls by the least amount: from below, the old top becomes
	 * the first row in view; from above, the last row of its page becomes the last one.
	 */
	void restoreTop (int top) {
		long hwnd = table.handle;
		int current = (int) OS.SendMessage (hwnd, OS.LVM_GETTOPINDEX, 0, 0);
		if (current == top) return;
		int page = Math.max (1, (int) OS.SendMessage (hwnd, OS.LVM_GETCOUNTPERPAGE, 0, 0));
		int count = (int) OS.SendMessage (hwnd, OS.LVM_GETITEMCOUNT, 0, 0);
		int row = current > top ? top : Math.min (count - 1, top + page - 1);
		OS.SendMessage (hwnd, OS.LVM_ENSUREVISIBLE, row, 0);
	}

	private int widestRow (int column, int count) {
		long hwnd = table.handle;
		int imageWidth = 0;
		long hImageList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
		if (hImageList != 0) {
			int [] cx = new int [1], cy = new int [1];
			OS.ImageList_GetIconSize (hImageList, cx, cy);
			imageWidth = cx [0];
		}
		int widest = -1, best = -1;
		for (int row = 0; row < count; row++) {
			TableItem item = table._getItem (row, false);
			if (item == null) continue;
			int width = textWidth (cellText (item, column));
			if (column == 0) {
				if (item.image != null) width += imageWidth;
				width += item.imageIndent * imageWidth;
			} else if (item.images != null && column < item.images.length && item.images [column] != null) {
				width += imageWidth;
			}
			if (width > best) {
				best = width;
				widest = row;
			}
		}
		return widest;
	}

	private static String cellText (TableItem item, int column) {
		if (column == 0) return item.text;
		String [] strings = item.strings;
		return strings != null && column < strings.length && strings [column] != null ? strings [column] : "";
	}

	private int textWidth (String text) {
		if (text.isEmpty ()) return 0;
		char [] buffer = new char [text.length () + 1];
		text.getChars (0, text.length (), buffer, 0);
		return (int) OS.SendMessage (table.handle, OS.LVM_GETSTRINGWIDTH, 0, buffer);
	}
}

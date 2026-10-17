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

import java.lang.ref.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
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
 * Rows and facades: the state of every row lives in columnar lanes (text, image, font and colours
 * per cell; font, colours, indent and check bits per row). A TableItem is a proxy of its row,
 * made on demand and loaded from the lanes; its setters write back. Paint answers rows from the
 * lanes without facades, so a Table of a million rows holds no TableItem per row. The most recent
 * facades and those carrying state the lanes do not keep (listeners, data) are held strongly; the
 * others weakly, so a row keeps its TableItem while the application holds it. A facade keeps its
 * row as a hint: <code>indexOf</code> checks it in O(1) and renumbers lazily from the first row an
 * insert or removal moved.
 * </p><p>
 * Only window-bounded native work runs here, except where the native ListView with items does
 * the same row scan: the type-ahead and the column autosize.
 * </p>
 */
final class TableViewport {
	/** The viewport is the default Table mode; <code>-Dswt.widget.mode.Table=original</code> (or the global <code>swt.widget.mode</code>) selects the native Table at startup. */
	static final boolean ENABLED = WidgetMode.of ("Table", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;
	/** LVN_ODCACHEHINT, the rows an owner-data ListView is about to paint (NMLVCACHEHINT: NMHDR, iFrom, iTo). */
	static final int LVN_ODCACHEHINT = OS.LVN_FIRST - 13;
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.widgets.Table");
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
			String text = (String) cells.get (TEXT, 0, row);
			if (text == null) text = "";
			if (partial ? text.regionMatches (true, 0, string, 0, string.length ()) : text.equalsIgnoreCase (string)) {
				final int found = row;
				if (LOG.isTraceEnabled ()) LOG.trace (() -> "Table type-ahead '" + string + "' from " + from + ": row " + found);
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
	/*
	 * Selection events of input (T-Table-7). Owner data notifies a range (LVN_ODSTATECHANGED) or every row
	 * (LVN_ITEMCHANGED with iItem -1) whether or not a row's selected state changes, where stock rows notify only
	 * the rows that change: Ctrl+Shift+End over a selected range, or a Ctrl+Shift click in it, sends no Selection on
	 * stock rows. Table.callWindowProc compares the selection before and after a message that can change it and keeps
	 * the Selection event only when it changed. The signature hashes the first SIGNATURE_LIMIT selected rows and the
	 * count, so a message costs at most that many LVM_GETNEXTITEM; beyond, a change that keeps the count and the first
	 * rows is not seen and the notification decides, as before.
	 */
	static final long NO_SIGNATURE = Long.MIN_VALUE;
	static final int SIGNATURE_LIMIT = 4096;

	/** The selection signature before a message that can change the selection, otherwise NO_SIGNATURE. */
	long selectionBefore (int msg, long wParam) {
		switch (msg) {
			case OS.WM_KEYDOWN:
			case OS.WM_SYSKEYDOWN:
			case OS.WM_CHAR:
			case OS.WM_LBUTTONDOWN:
			case OS.WM_LBUTTONUP:
			case OS.WM_LBUTTONDBLCLK:
			case OS.WM_RBUTTONDOWN:
			case OS.WM_RBUTTONUP:
			case OS.WM_RBUTTONDBLCLK:
			case OS.WM_MBUTTONDOWN:
			case OS.WM_MBUTTONUP:
			case OS.WM_XBUTTONDOWN:
			case OS.WM_XBUTTONUP:
				return selectionSignature ();
			case OS.WM_MOUSEMOVE:
				/* a marquee or a drag selects while a button is down */
				return (wParam & (OS.MK_LBUTTON | OS.MK_RBUTTON)) != 0 ? selectionSignature () : NO_SIGNATURE;
			default:
				return NO_SIGNATURE;
		}
	}

	/** True when a notified selection change after a message is a real one (see {@link #selectionBefore(int, long)}). */
	boolean selectionChanged (long before) {
		return before == NO_SIGNATURE || selectionSignature () != before;
	}

	long selectionSignature () {
		long hwnd = table.handle;
		long hash = OS.SendMessage (hwnd, OS.LVM_GETSELECTEDCOUNT, 0, 0);
		int index = -1;
		for (int seen = 0; seen < SIGNATURE_LIMIT; seen++) {
			index = (int) OS.SendMessage (hwnd, OS.LVM_GETNEXTITEM, index, OS.LVNI_SELECTED);
			if (index == -1) break;
			hash = hash * 1_000_003L + index + 1;
		}
		return hash == NO_SIGNATURE ? NO_SIGNATURE + 1 : hash;
	}

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
	 * (one <code>LVM_SETITEMCOUNT</code> instead of one native message per row), bind (the rows of a
	 * larger count get their items on demand, see {@link #facade(int, boolean)}) and
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
		for (int index = count; index < Math.min (itemCount, table.items.length); index++) {
			TableItem item = live (index);
			if (item != null && !item.isDisposed ()) item.release (false);
		}
		/* rows of a larger count get their facades on demand (facade): none is made here */
		table._setItemCount (count, itemCount);
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
		if (LOG.isTraceEnabled ()) LOG.trace (() -> "Table count " + itemCount + " -> " + count + " (one native count change)");
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

	/* ---------------------------------------------------------------- rows and facades */

	/*
	 * Row store. The state of every row lives in columnar lanes: per column text, image, font,
	 * background and foreground; per row font, background, foreground and image indent; check and
	 * gray bits. A facade (TableItem) is a proxy of its row: made on demand and loaded from the lanes
	 * (hydrate), and every setter writes its state back (store), so the lanes are always the state of
	 * the row and a facade can be dropped and made again.
	 */
	static final int TEXT = 0, IMAGE = 1, CELL_FONT = 2;
	static final int CELL_BACKGROUND = 0, CELL_FOREGROUND = 1;
	static final int ROW_FONT = 0;
	static final int ROW_BACKGROUND = 0, ROW_FOREGROUND = 1, INDENT = 2;
	static final int CHECKED = 0, GRAYED = 1, DIRECTION = 2, DIRECTION_AUTO = 3, DIRECTION_FLIP = 4;
	final VirtualCellLanes cells = new VirtualCellLanes (3, new int [] {-1, -1}, 1);
	final VirtualCellLanes rowLanes = new VirtualCellLanes (1, new int [] {-1, -1, 0}, 1);
	final VirtualBitLanes flags = new VirtualBitLanes (5);

	/*
	 * Facades. items [row] holds the facades that are held strongly: pinned ones (listeners, data or a
	 * style other than the text direction: state the lanes do not keep) and the RECENT most recently made
	 * or used ones. Every other facade is held weakly (weak [row]): while the application holds it, the
	 * row answers the same TableItem; once nothing holds it, it can be collected and the row answers a
	 * new facade loaded from the lanes, which no caller can tell apart.
	 */
	static final int RECENT = 1024;
	private WeakReference<TableItem> [] weak;
	private final TableItem [] recent = new TableItem [RECENT];
	private int recentNext;
	/* facades loaded for paint (one per nesting depth), never handed to application code */
	private final TableItem [] paintFacades = new TableItem [4];

	/*
	 * Facades hold their row as a hint (TableItem.viewportRow). Rows below this index have exact
	 * hints; an insert or removal lowers it to the first moved row, and indexOf renumbers lazily
	 * from here, so a loop over indexOf costs O(n) in total instead of O(n) per call.
	 */
	private int exactBelow;
	/* depth of LVN_GETDISPINFO and custom draw: a row without a facade is painted from a paint facade */
	int painting;

	/** Rows <code>[index, index + count)</code> were inserted (the Table's item array is shifted by the Table). */
	void rowsInserted (int index, int count) {
		widths.insertUnmeasured (index, count);
		cells.insertRows (index, count);
		rowLanes.insertRows (index, count);
		flags.insert (index, count);
		int length = cells.length ();
		if (weak != null && index < weak.length) {
			ensureWeak (length);
			System.arraycopy (weak, index, weak, index + count, length - count - index);
			Arrays.fill (weak, index, index + count, null);
		}
		moved (index);
	}

	/** Rows <code>[index, index + count)</code> were removed. */
	void rowsRemoved (int index, int count) {
		widths.remove (index, count);
		int length = cells.length ();
		cells.removeRows (index, count);
		rowLanes.removeRows (index, count);
		flags.remove (index, count);
		if (weak != null && index < weak.length) {
			int end = Math.min (length, weak.length);
			if (index + count < end) System.arraycopy (weak, index + count, weak, index, end - index - count);
			Arrays.fill (weak, Math.max (index, end - count), end, null);
		}
		moved (index);
	}

	/** The row count changed to <code>count</code>: rows beyond a smaller count are dropped. */
	void rowsResized (int count) {
		int length = cells.length ();
		if (count == 0) {
			widths.clear ();
		} else if (count < length) {
			widths.remove (count, length - count);
		} else {
			widths.insertUnmeasured (length, count - length);
		}
		cells.setLength (count);
		rowLanes.setLength (count);
		flags.setLength (count);
		if (weak != null && count < length) Arrays.fill (weak, Math.min (count, weak.length), Math.min (length, weak.length), null);
		if (count == 0) {
			weak = null;
			Arrays.fill (recent, null);
		}
		moved (Math.min (count, length));
	}

	private void moved (int index) {
		if (index < exactBelow) exactBelow = Math.max (0, index);
	}

	/**
	 * A column is being inserted at <code>index</code>: called right after the Table shifted the
	 * facades' arrays, before it counts the new column. A first column does not move any state.
	 */
	void columnInserted (int index) {
		if (table.columnCount >= 1) cells.insertColumn (index);
	}

	/** The column at <code>index</code> was removed: called right after the Table shifted the facades' arrays. */
	void columnRemoved (int index) {
		if (table.columnCount >= 1) {
			cells.removeColumn (index);
		} else {
			/* the last column: text and image of the row stay, the cell fonts and colours go */
			cells.dropLane (CELL_FONT, 0);
			cells.dropIntLane (CELL_BACKGROUND, 0);
			cells.dropIntLane (CELL_FOREGROUND, 0);
		}
	}

	/** Whether a row holds state that is not the default. */
	boolean hasState (int row) {
		return cells.hasState (row) || rowLanes.hasState (row) || flags.get (CHECKED, row) || flags.get (GRAYED, row);
	}

	/** Writes the state of a facade to its row (called at the end of every TableItem setter). */
	void store (TableItem item) {
		if (item == paintFacades [0] || item == paintFacades [1] || item == paintFacades [2] || item == paintFacades [3]) return;
		int row = indexOf (item);
		if (row < 0) return;
		int columns = cells.columns ();
		Object oldText = cells.get (TEXT, 0, row), oldCellFont = cells.get (CELL_FONT, 0, row), oldFont = rowLanes.get (ROW_FONT, 0, row);
		cells.set (TEXT, 0, row, item.text == null || item.text.isEmpty () ? null : item.text);
		cells.set (IMAGE, 0, row, item.image);
		for (int column = 0; column < columns; column++) {
			if (column > 0) {
				cells.set (TEXT, column, row, item.strings != null && column < item.strings.length ? item.strings [column] : null);
				cells.set (IMAGE, column, row, item.images != null && column < item.images.length ? item.images [column] : null);
			}
			cells.set (CELL_FONT, column, row, item.cellFont != null && column < item.cellFont.length ? item.cellFont [column] : null);
			cells.setInt (CELL_BACKGROUND, column, row, item.cellBackground != null && column < item.cellBackground.length ? item.cellBackground [column] : -1);
			cells.setInt (CELL_FOREGROUND, column, row, item.cellForeground != null && column < item.cellForeground.length ? item.cellForeground [column] : -1);
		}
		rowLanes.set (ROW_FONT, 0, row, item.font);
		rowLanes.setInt (ROW_BACKGROUND, 0, row, item.background);
		rowLanes.setInt (ROW_FOREGROUND, 0, row, item.foreground);
		rowLanes.setInt (INDENT, 0, row, item.imageIndent);
		flags.set (CHECKED, row, item.checked);
		flags.set (GRAYED, row, item.grayed);
		if (!Objects.equals (oldText, cells.get (TEXT, 0, row)) || oldCellFont != cells.get (CELL_FONT, 0, row) || oldFont != item.font) {
			widths.setUnmeasured (row);
		}
	}

	/* Loads the state of a row into a facade: the arrays exist exactly where a setter would have made them. */
	private void hydrate (TableItem item, int row) {
		int columns = cells.columns ();
		String text = (String) cells.get (TEXT, 0, row);
		item.text = text == null ? "" : text;
		item.image = (Image) cells.get (IMAGE, 0, row);
		String [] strings = null;
		Image [] images = null;
		Font [] cellFont = null;
		int [] cellBackground = null, cellForeground = null;
		for (int column = 0; column < columns; column++) {
			if (column > 0 && cells.get (TEXT, column, row) != null) {
				if (strings == null) {
					strings = new String [columns];
					strings [0] = item.text;
				}
				strings [column] = (String) cells.get (TEXT, column, row);
			}
			if (column > 0 && cells.get (IMAGE, column, row) != null) {
				if (images == null) {
					images = new Image [columns];
					images [0] = item.image;
				}
				images [column] = (Image) cells.get (IMAGE, column, row);
			}
			if (cells.get (CELL_FONT, column, row) != null) {
				if (cellFont == null) cellFont = new Font [columns];
				cellFont [column] = (Font) cells.get (CELL_FONT, column, row);
			}
			if (cells.getInt (CELL_BACKGROUND, column, row) != -1) {
				if (cellBackground == null) Arrays.fill (cellBackground = new int [columns], -1);
				cellBackground [column] = cells.getInt (CELL_BACKGROUND, column, row);
			}
			if (cells.getInt (CELL_FOREGROUND, column, row) != -1) {
				if (cellForeground == null) Arrays.fill (cellForeground = new int [columns], -1);
				cellForeground [column] = cells.getInt (CELL_FOREGROUND, column, row);
			}
		}
		item.strings = strings;
		item.images = images;
		item.cellFont = cellFont;
		item.cellBackground = cellBackground;
		item.cellForeground = cellForeground;
		item.font = (Font) rowLanes.get (ROW_FONT, 0, row);
		item.background = rowLanes.getInt (ROW_BACKGROUND, 0, row);
		item.foreground = rowLanes.getInt (ROW_FOREGROUND, 0, row);
		item.imageIndent = rowLanes.getInt (INDENT, 0, row);
		item.checked = flags.get (CHECKED, row);
		item.grayed = flags.get (GRAYED, row);
		item.viewportRow = row;
		item.updateTextDirection (!flags.get (DIRECTION, row) ? 0 : flags.get (DIRECTION_AUTO, row) ? Widget.AUTO_TEXT_DIRECTION : flags.get (DIRECTION_FLIP, row) ? SWT.FLIP_TEXT_DIRECTION : 0);
	}

	/**
	 * Table.updateTextDirection gives every item the Table's text direction, but its loop sees only the
	 * strongly held facades: the rows keep the direction in bits, set word-wise (facades loaded later get
	 * it), and the weakly held facades get it now, in one pass over the weak slots as the stock loop is one
	 * pass over the items. Rows inserted later have none, as stock items made later have none.
	 */
	void textDirectionChanged (int direction) {
		int length = cells.length ();
		flags.setRange (DIRECTION, 0, length, true);
		flags.setRange (DIRECTION_AUTO, 0, length, direction == Widget.AUTO_TEXT_DIRECTION);
		flags.setRange (DIRECTION_FLIP, 0, length, direction == SWT.FLIP_TEXT_DIRECTION);
		if (weak == null) return;
		for (int row = 0; row < Math.min (weak.length, length); row++) {
			TableItem item = table.items [row] == null && weak [row] != null ? weak [row].get () : null;
			if (item != null) item.updateTextDirection (direction);
		}
	}

	/** Mediated <code>indexOf</code>: the facade's row hint when exact, otherwise a lazy renumbering from the first moved row. */
	int indexOf (TableItem item) {
		int count = Math.min (table.items.length, cells.length ());
		int row = item.viewportRow;
		if (0 <= row && row < exactBelow && row < count && live (row) == item) return row;
		for (int index = exactBelow; index < count; index++) {
			TableItem candidate = live (index);
			if (candidate != null) candidate.viewportRow = index;
			exactBelow = index + 1;
			if (candidate == item) return index;
		}
		return -1;
	}

	/* the facade of a row that exists now, held strongly or weakly; never made */
	private TableItem live (int row) {
		TableItem item = table.items [row];
		if (item != null) return item;
		WeakReference<TableItem> reference = weak != null && row < weak.length ? weak [row] : null;
		if (reference == null) return null;
		item = reference.get ();
		if (item == null) weak [row] = null;
		return item;
	}

	/**
	 * The facade of a row (<code>Table._getItem</code>). A live facade is answered as it is. Otherwise
	 * a facade is made and loaded from the lanes when one is asked for (<code>create</code>), or when
	 * the row holds state (callers that walk existing items, such as <code>clear</code>, must see it),
	 * unless the Table is being disposed. Paint gets a paint facade loaded from the lanes instead, unless
	 * a MeasureItem, EraseItem or PaintItem listener needs a real one. While the native control inserts
	 * or deletes a row, no facade is made.
	 */
	TableItem facade (int index, boolean create) {
		if (index < 0 || index >= cells.length () || index >= table.items.length) return null;
		TableItem item = live (index);
		if (item != null) {
			if (create && table.items [index] == null) hold (index, item);
			return item;
		}
		boolean state = hasState (index);
		if (painting > 0 && (create || state) && !table.pagePaintNeedsFacade ()) return paintFacade (index);
		if (!create && (!state || (table.state & Widget.DISPOSE_SENT) != 0)) return null;
		if (table.ignoreShrink) return null;
		item = new TableItem (table, SWT.NONE, -1, false);
		hydrate (item, index);
		hold (index, item);
		return item;
	}

	private TableItem paintFacade (int index) {
		int depth = Math.min (paintFacades.length, painting) - 1;
		TableItem item = paintFacades [depth];
		if (item == null) item = paintFacades [depth] = new TableItem (table, SWT.NONE, -1, false);
		hydrate (item, index);
		return item;
	}

	/* holds a facade strongly as one of the RECENT most recent; the oldest one is let go */
	private void hold (int row, TableItem item) {
		table.items [row] = item;
		if (weak != null && row < weak.length) weak [row] = null;
		TableItem oldest = recent [recentNext];
		recent [recentNext] = item;
		recentNext = (recentNext + 1) % RECENT;
		if (oldest != null && oldest != item) release (oldest);
	}

	/* a facade leaves the strong hold unless it is pinned: from now on only the application can keep it */
	private void release (TableItem item) {
		if (item.isDisposed () || hasListeners (item) || item.data != null || (item.style & ~SWT.FLIP_TEXT_DIRECTION) != 0 || item == table.currentItem) return;
		for (TableItem other : recent) {
			if (other == item) return;
		}
		int row = indexOf (item);
		if (row < 0 || table.items [row] != item) return;
		table.items [row] = null;
		ensureWeak (cells.length ());
		weak [row] = new WeakReference<> (item);
	}

	/* listeners other than the ZoomChanged listener every Item registers for itself */
	private static boolean hasListeners (TableItem item) {
		EventTable events = item.eventTable;
		if (events == null || events.types == null) return false;
		for (int i = 0; i < events.types.length; i++) {
			if (events.types [i] != 0 && events.types [i] != SWT.ZoomChanged && events.listeners [i] != null) return true;
		}
		return false;
	}

	@SuppressWarnings ("unchecked")
	private void ensureWeak (int length) {
		if (weak != null && weak.length >= length) return;
		int capacity = VirtualGrowth.capacity (weak == null ? 0 : weak.length, Math.max (length, 16), 4);
		weak = weak == null ? new WeakReference [capacity] : Arrays.copyOf (weak, capacity);
	}

	/** A facade made by the application (<code>new TableItem</code>) is held like a facade made on demand. */
	void created (TableItem item) {
		int row = indexOf (item);
		if (row >= 0) hold (row, item);
	}

	/**
	 * LVN_ODCACHEHINT: the rows the ListView is about to paint. Their live facades are held strongly
	 * again, so painting them neither makes nor loads facades.
	 */
	void cacheHint (long lParam) {
		int [] range = new int [2];
		OS.MoveMemory (range, lParam + NMHDR.sizeof, 8);
		int to = Math.min (range [1], cells.length () - 1);
		for (int row = Math.max (0, range [0]); row <= to; row++) {
			if (table.items [row] == null) {
				TableItem item = live (row);
				if (item != null) hold (row, item);
			}
		}
	}

	/** Mediated <code>getItems</code> of a plain Table: every row answers its facade. */
	void items (TableItem [] result, int count) {
		for (int index = 0; index < count; index++) result [index] = facade (index, true);
	}

	/** Mediated dispose of a facade: its row is found in O(1) instead of by a scan; the native row is deleted as before. */
	void destroyItem (TableItem item) {
		int count = (int) OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
		int index = indexOf (item);
		if (index < 0) return;
		table.setDeferResize (true);
		table.ignoreSelect = table.ignoreShrink = true;
		long code = OS.SendMessage (table.handle, OS.LVM_DELETEITEM, index, 0);
		table.ignoreSelect = table.ignoreShrink = false;
		if (code == 0) table.error (SWT.ERROR_ITEM_NOT_REMOVED);
		table._removeItem (index, count);
		if (--count == 0) table.setTableEmpty ();
		table.setDeferResize (false);
	}

	/**
	 * Custom draw (NM_CUSTOMDRAW) of the plain Table's rows, run as the Table runs it, while rows
	 * without a facade are painted from paint facades: painting creates no facade unless a
	 * MeasureItem, EraseItem or PaintItem listener needs one.
	 * @return the result of the draw stage, as the Table returns it
	 */
	LRESULT customDraw (NMLVCUSTOMDRAW nmcd, long wParam, long lParam) {
		painting++;
		try {
			return switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT -> table.CDDS_PREPAINT (nmcd, wParam, lParam);
				case OS.CDDS_ITEMPREPAINT -> table.CDDS_ITEMPREPAINT (nmcd, wParam, lParam);
				case OS.CDDS_ITEMPOSTPAINT -> table.CDDS_ITEMPOSTPAINT (nmcd, wParam, lParam);
				case OS.CDDS_SUBITEMPREPAINT -> table.CDDS_SUBITEMPREPAINT (nmcd, wParam, lParam);
				case OS.CDDS_SUBITEMPOSTPAINT -> table.CDDS_SUBITEMPOSTPAINT (nmcd, wParam, lParam);
				case OS.CDDS_POSTPAINT -> table.CDDS_POSTPAINT (nmcd, wParam, lParam);
				default -> null;
			};
		} finally {
			painting--;
		}
	}

	/** The custom draw stages the Table answers (the others go to its superclass). */
	static boolean drawsStage (NMLVCUSTOMDRAW nmcd) {
		return switch (nmcd.dwDrawStage) {
			case OS.CDDS_PREPAINT, OS.CDDS_ITEMPREPAINT, OS.CDDS_ITEMPOSTPAINT, OS.CDDS_SUBITEMPREPAINT, OS.CDDS_SUBITEMPOSTPAINT,
					OS.CDDS_POSTPAINT -> true;
			default -> false;
		};
	}

	/* ---------------------------------------------------------------- appends and the native count */

	/** Answer of {@link #beforeMessage(int, long, long)} for a message the ListView handles itself. */
	static final long PASS = Long.MIN_VALUE;
	/*
	 * Appended rows that the native control does not count yet: the native count is nativeCount, the
	 * logical one nativeCount + pending. Appending is one native count change per burst instead of one
	 * native insert per row: an append at the end changes neither selection, focus nor mark (oracle
	 * case A), so the count is brought up to date before any message that could observe it.
	 */
	private int pending, nativeCount;
	private boolean flushQueued;
	private final Runnable flushLater = this::flushQueued;

	private void flushQueued () {
		flushQueued = false;
		if (!table.isDisposed ()) flush ();
	}

	/**
	 * Every message to the ListView passes here first (Table.windowProc). An append is deferred;
	 * LVM_GETITEMCOUNT answers the logical count; queries that do not depend on the rows pass;
	 * any other message first brings the native count up to date.
	 */
	long beforeMessage (int msg, long wParam, long lParam) {
		if (msg == OS.LVM_INSERTITEM && appending (lParam)) return defer (lParam);
		if (pending == 0) return PASS;
		switch (msg) {
			case OS.LVM_GETITEMCOUNT:
				return nativeCount + pending;
			case OS.LVM_GETSTRINGWIDTH:
			case OS.LVM_GETCOLUMNWIDTH:
			case OS.LVM_GETIMAGELIST:
			case OS.LVM_GETEXTENDEDLISTVIEWSTYLE:
			case OS.LVM_GETHEADER:
			case OS.WM_GETFONT:
				return PASS;
			case OS.LVM_SETCOLUMNWIDTH:
				/* an explicit width does not depend on the rows; an autosize measures them */
				if (lParam >= 0) return PASS;
				break;
		}
		flush ();
		return PASS;
	}

	/* an LVM_INSERTITEM at the end of a plain Table, outside a paint */
	private boolean appending (long lParam) {
		if (painting > 0 || lParam == 0) return false;
		int [] index = new int [1];
		OS.MoveMemory (index, lParam + 4, 4);
		if (pending == 0) nativeCount = (int) OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
		return index [0] == nativeCount + pending;
	}

	private long defer (long lParam) {
		int [] index = new int [1];
		OS.MoveMemory (index, lParam + 4, 4);
		pending++;
		if (!flushQueued) {
			flushQueued = true;
			table.display.asyncExec (flushLater);
		}
		return index [0];
	}

	/** Brings the native count up to date with one LVM_SETITEMCOUNT (the rows were appended). */
	void flush () {
		if (pending == 0) return;
		int count = nativeCount + pending;
		pending = 0;
		nativeCount = count;
		boolean oldSelect = table.ignoreSelect;
		table.ignoreSelect = true;
		try {
			OS.SendMessage (table.handle, OS.LVM_SETITEMCOUNT, count, OS.LVSICF_NOINVALIDATEALL | OS.LVSICF_NOSCROLL);
		} finally {
			table.ignoreSelect = oldSelect;
		}
		if (LOG.isTraceEnabled ()) LOG.trace (() -> "Table appends flushed: native count " + count);
	}

	/** Whether a facade's row is an append the native control does not count yet (nothing to repaint). */
	boolean pending (TableItem item) {
		if (pending == 0) return false;
		int row = indexOf (item);
		return row >= nativeCount;
	}

	/* ---------------------------------------------------------------- column-less scroll width */

	/*
	 * Width of every row's first-column text with its font, measured once per text, font or table
	 * font change (VirtualListWidth, lazy), so the column-less scroll width does not measure every row
	 * again: stock SWT measures all rows for every full recomputation.
	 */
	private final VirtualListWidth widths = new VirtualListWidth (text -> measureRow ());
	private int measuringRow;

	private int measureRow () {
		String text = (String) cells.get (TEXT, 0, measuringRow);
		if (text == null || text.isEmpty ()) return 0;
		Font font = (Font) cells.get (CELL_FONT, 0, measuringRow);
		if (font == null) font = (Font) rowLanes.get (ROW_FONT, 0, measuringRow);
		long hwnd = table.handle;
		if (font == null) {
			TCHAR buffer = new TCHAR (text, true);
			return (int) OS.SendMessage (hwnd, OS.LVM_GETSTRINGWIDTH, 0, buffer);
		}
		long hDC = OS.GetDC (hwnd);
		long oldFont = OS.SelectObject (hDC, SWTFontProvider.getFontHandle (font, table.nativeZoom));
		char [] chars = text.toCharArray ();
		RECT rect = new RECT ();
		OS.DrawText (hDC, chars, chars.length, rect, OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX);
		OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (hwnd, hDC);
		return rect.right - rect.left;
	}

	/** The Table's font changed: every row is measured again on demand. */
	void fontChanged () {
		widths.setAllUnmeasured (cells.length ());
	}

	/**
	 * Column-less <code>setScrollWidth (null, ...)</code>: the arithmetic of the stock Table over the
	 * widest measured row and the largest image indent, measuring only rows not measured since their
	 * text or font changed.
	 */
	boolean widenToWidestRow () {
		long hwnd = table.handle;
		widths.measurePending (row -> {
			/* the measure function reads the row's lanes (text and font), the string only names it */
			measuringRow = row;
			return "";
		});
		int newWidth = widths.maxWidth ();
		int imageIndent = Math.max (0, rowLanes.maxInt (INDENT, 0));
		if (newWidth == 0) {
			char [] buffer = {' ', '\0'};
			newWidth = (int) OS.SendMessage (hwnd, OS.LVM_GETSTRINGWIDTH, 0, buffer);
		}
		long hStateList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
		if (hStateList != 0) {
			int [] cx = new int [1], cy = new int [1];
			OS.ImageList_GetIconSize (hStateList, cx, cy);
			newWidth += cx [0] + Table.INSET;
		}
		long hImageList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
		if (hImageList != 0) {
			int [] cx = new int [1], cy = new int [1];
			OS.ImageList_GetIconSize (hImageList, cx, cy);
			newWidth += (imageIndent + 1) * cx [0];
		} else {
			newWidth++;
		}
		newWidth += Table.INSET * 2 + DPIUtil.pointToPixel (Table.VISTA_EXTRA, table.getAutoscalingZoom ());
		int oldWidth = (int) OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, 0, 0);
		if (newWidth > oldWidth) {
			table.setScrollWidth (newWidth);
			return true;
		}
		return false;
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
		/* a row's text is measured with its own font (cell font, then item font), as the native autosize does through custom draw */
		long hDC = 0;
		try {
			for (int row = 0; row < Math.min (count, cells.length ()); row++) {
				String text = (String) cells.get (TEXT, column, row);
				Font font = (Font) cells.get (CELL_FONT, column, row);
				if (font == null) font = (Font) rowLanes.get (ROW_FONT, 0, row);
				int width;
				if (text == null || text.isEmpty ()) {
					width = 0;
				} else if (font == null) {
					width = textWidth (text);
				} else {
					if (hDC == 0) hDC = OS.GetDC (hwnd);
					width = fontTextWidth (hDC, font, text);
				}
				/* the first column reserves the icon for every row once there is an image list; a later column only for a row with an image */
				if (column != 0 && cells.get (IMAGE, column, row) != null) width += imageWidth;
				if (column == 0) width += rowLanes.getInt (INDENT, 0, row) * imageWidth;
				if (width > best) {
					best = width;
					widest = row;
				}
			}
		} finally {
			if (hDC != 0) OS.ReleaseDC (hwnd, hDC);
		}
		return widest;
	}

	private int fontTextWidth (long hDC, Font font, String text) {
		long oldFont = OS.SelectObject (hDC, SWTFontProvider.getFontHandle (font, table.nativeZoom));
		char [] chars = text.toCharArray ();
		RECT rect = new RECT ();
		OS.DrawText (hDC, chars, chars.length, rect, OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX);
		OS.SelectObject (hDC, oldFont);
		return rect.right - rect.left;
	}

	private int textWidth (String text) {
		if (text.isEmpty ()) return 0;
		char [] buffer = new char [text.length () + 1];
		text.getChars (0, text.length (), buffer, 0);
		return (int) OS.SendMessage (table.handle, OS.LVM_GETSTRINGWIDTH, 0, buffer);
	}
}

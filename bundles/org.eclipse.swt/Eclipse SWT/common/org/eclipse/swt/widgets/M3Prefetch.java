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

/**
 * Prefetch ranges of <code>Table</code>, <code>List</code> and <code>Tree</code> (enhanced M3
 * API): which rows the widget is about to show, and which it no longer holds.
 * <p>
 * A listener added here receives events of type {@link #EVENT} when the widget's resident
 * window (see {@link M3Residency}) moves: first {@link #CANCEL} for the rows that left the window,
 * then {@link #PREFETCH} for the rows that entered it, each as at most two ranges in
 * <code>event.start</code> and <code>event.end</code> (inclusive; a Tree uses the visible rows of
 * {@link M3RowMath}). Scrolling by k rows announces k rows. Ranges are delivered, never items,
 * so a data source can fetch in bulk before <code>SWT.SetData</code> asks row by row.
 * {@link #setWindow(Table, int, int)} widens the announced range around the resident window.
 * </p>
 * <p>
 * The events follow the viewport windows: a Table's window comes from painting (the native cache
 * hint), so a Table that is not shown announces nothing; without a viewport (the original mode)
 * every row is resident from the start and nothing is announced. The win32 widgets send the
 * events.
 * </p>
 * <p>
 * Listeners may be called while the widget paints: they should start fetches, not change the
 * widget's items.
 * </p>
 *
 * @since 3.138
 */
public final class M3Prefetch {

	/** The event type: outside the range of the SWT event types. */
	public static final int EVENT = 0x4D330002;
	/** <code>event.detail</code>: the rows from <code>start</code> to <code>end</code> entered the window. */
	public static final int PREFETCH = 0;
	/** <code>event.detail</code>: the rows from <code>start</code> to <code>end</code> left the window. */
	public static final int CANCEL = 1;

	private static final String KEY = "org.eclipse.swt.widgets.M3Prefetch"; //$NON-NLS-1$

	/* The latch of a widget: the last announced range [first, last] and the two margins. */
	private static final class Latch {
		int first, last = -1;
		int before, after;
	}

	private M3Prefetch () {
	}

	/**
	 * Adds a listener for the prefetch ranges of the table.
	 *
	 * @param table the table
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static void addListener (Table table, Listener listener) {
		add (table, listener);
	}

	/**
	 * Adds a listener for the prefetch ranges of the list.
	 *
	 * @param list the list
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static void addListener (List list, Listener listener) {
		add (list, listener);
	}

	/**
	 * Adds a listener for the prefetch ranges (visible rows) of the tree.
	 *
	 * @param tree the tree
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static void addListener (Tree tree, Listener listener) {
		add (tree, listener);
	}

	/**
	 * Removes a listener added with {@link #addListener(Table, Listener)}.
	 *
	 * @param table the table
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static void removeListener (Table table, Listener listener) {
		remove (table, listener);
	}

	/**
	 * Removes a listener added with {@link #addListener(List, Listener)}.
	 *
	 * @param list the list
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static void removeListener (List list, Listener listener) {
		remove (list, listener);
	}

	/**
	 * Removes a listener added with {@link #addListener(Tree, Listener)}.
	 *
	 * @param tree the tree
	 * @param listener the listener
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static void removeListener (Tree tree, Listener listener) {
		remove (tree, listener);
	}

	/**
	 * Widens the announced range of the table: <code>rowsBefore</code> rows above and
	 * <code>rowsAfter</code> rows below its resident window. Takes effect at the next window move.
	 *
	 * @param table the table
	 * @param rowsBefore the rows above the window, not negative
	 * @param rowsAfter the rows below the window, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if a margin is negative</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static void setWindow (Table table, int rowsBefore, int rowsAfter) {
		window (table, rowsBefore, rowsAfter);
	}

	/**
	 * Widens the announced range of the list (see {@link #setWindow(Table, int, int)}).
	 *
	 * @param list the list
	 * @param rowsBefore the rows above the window, not negative
	 * @param rowsAfter the rows below the window, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if a margin is negative</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static void setWindow (List list, int rowsBefore, int rowsAfter) {
		window (list, rowsBefore, rowsAfter);
	}

	/**
	 * Widens the announced range of the tree (see {@link #setWindow(Table, int, int)}).
	 *
	 * @param tree the tree
	 * @param rowsBefore the visible rows above the window, not negative
	 * @param rowsAfter the visible rows below the window, not negative
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if a margin is negative</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static void setWindow (Tree tree, int rowsBefore, int rowsAfter) {
		window (tree, rowsBefore, rowsAfter);
	}

	/* ------------------------------------------------------------------ widget seam */

	/* The resident window of a viewport moved: announces the difference to the last range. */
	static void windowChanged (Control widget) {
		if (!widget.hooks (EVENT) || widget.isDisposed ()) return;
		Latch latch = latch (widget);
		if (latch == null) return;
		M3ItemRange resident = resident (widget);
		int count = rows (widget);
		int first = 0, last = -1;
		if (!resident.isEmpty ()) {
			first = Math.max (0, resident.start () - latch.before);
			last = (int) Math.min (count - 1L, (long) resident.end () + latch.after);
		}
		int oldFirst = latch.first, oldLast = latch.last;
		if (first == oldFirst && last == oldLast) return;
		latch.first = first;
		latch.last = last;
		/* rows that left (old minus new), then rows that entered (new minus old) */
		difference (widget, CANCEL, oldFirst, oldLast, first, last);
		difference (widget, PREFETCH, first, last, oldFirst, oldLast);
	}

	/* ------------------------------------------------------------------ helpers */

	/* Sends [from, to] minus [cutFirst, cutLast]: at most two ranges, below and above the cut. */
	private static void difference (Control widget, int detail, int from, int to, int cutFirst, int cutLast) {
		if (to < from) return;
		if (cutLast < cutFirst || cutLast < from || cutFirst > to) {
			send (widget, detail, from, to);
			return;
		}
		send (widget, detail, from, cutFirst - 1);
		send (widget, detail, cutLast + 1, to);
	}

	private static void send (Control widget, int detail, int start, int end) {
		if (end < start || widget.isDisposed ()) return;
		Event event = new Event ();
		event.detail = detail;
		event.start = start;
		event.end = end;
		widget.sendEvent (EVENT, event);
	}

	private static void add (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		boolean first = !widget.hooks (EVENT);
		widget.addListener (EVENT, listener);
		Latch latch = latch (widget);
		if (latch == null) widget.setData (KEY, latch = new Latch ());
		/* the first listener hears the current window as a whole */
		if (first) {
			latch.first = 0;
			latch.last = -1;
			windowChanged (widget);
		}
	}

	private static void remove (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		widget.removeListener (EVENT, listener);
	}

	private static void window (Control widget, int before, int after) {
		if (widget == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		if (before < 0 || after < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		Latch latch = latch (widget);
		if (latch == null) widget.setData (KEY, latch = new Latch ());
		latch.before = before;
		latch.after = after;
	}

	private static Latch latch (Widget widget) {
		return widget.getData (KEY) instanceof Latch latch ? latch : null;
	}

	/* the resident window, or EMPTY without a viewport (every row is resident from the start) */
	private static M3ItemRange resident (Control widget) {
		if (widget instanceof Table table) return table.m3ResidentRange () == null ? M3ItemRange.EMPTY : M3Residency.residentRange (table);
		if (widget instanceof Tree tree) return tree.m3ResidentRange () == null ? M3ItemRange.EMPTY : M3Residency.residentRange (tree);
		List list = (List) widget;
		return list.m3ResidentRange () == null ? M3ItemRange.EMPTY : M3Residency.residentRange (list);
	}

	private static int rows (Control widget) {
		if (widget instanceof Table table) return table.getItemCount ();
		if (widget instanceof Tree tree) return (int) Math.min (Integer.MAX_VALUE, M3RowMath.visibleRowCount (tree));
		return ((List) widget).getItemCount ();
	}
}

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
 * Uniform emptiness notification of <code>Tree</code>, <code>Table</code> and <code>List</code>
 * (enhanced M3 API).
 * <p>
 * A listener added here receives an event of type {@link #EVENT} each time the item count of the
 * widget (for a Tree, its root item count) crosses zero: <code>detail</code> is
 * {@link #BECAME_EMPTY} when the last item went away and {@link #BECAME_NON_EMPTY} when the first
 * item arrived, and <code>count</code> is the item count after the change. Every structural path
 * counts: creating and disposing items, <code>remove</code>, <code>removeAll</code>,
 * <code>setItemCount</code> and, for a List, <code>add</code> and <code>setItems</code>. A change
 * that keeps the widget on the same side of zero sends nothing, and one operation sends at most
 * one event (<code>List.setItem</code> on a one-item list reports no transient empty state).
 * </p>
 * <p>
 * The stock <code>SWT.EmptinessChanged</code> event of a Tree keeps its own rules (first item
 * created, last item disposed); {@link #EVENT} is a separate type, so both can be listened to.
 * The answers are the same in every startup mode. The win32 widgets send the event; on other
 * platforms the listener is kept but not yet called.
 * </p>
 *
 * @since 3.138
 */
public final class M3Emptiness {

	/** The event type: outside the range of the SWT event types, distinct from <code>SWT.EmptinessChanged</code>. */
	public static final int EVENT = 0x4D330001;
	/** <code>event.detail</code> when the first item arrived (the value of the stock event). */
	public static final int BECAME_NON_EMPTY = 0;
	/** <code>event.detail</code> when the last item went away (the value of the stock event). */
	public static final int BECAME_EMPTY = 1;

	private static final String KEY = "org.eclipse.swt.widgets.M3Emptiness"; //$NON-NLS-1$

	/* The latch of a listened widget: the emptiness last announced, the open holds, and the dispatch state. */
	private static final class Latch {
		boolean empty;
		int holds;
		boolean sending;
		boolean again;
	}

	private M3Emptiness () {
	}

	/**
	 * Adds a listener that is called when the tree's root item count crosses zero.
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
	 * Adds a listener that is called when the table's item count crosses zero.
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
	 * Adds a listener that is called when the list's item count crosses zero.
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

	/* ------------------------------------------------------------------ widget seam */

	/* Whether the widget is listened to; the cheap gate the widgets test before costlier checks. */
	static boolean watched (Widget widget) {
		return widget.hooks (EVENT);
	}

	/* End of a structural change: announces a zero crossing of the item count (getItemCount). */
	static void changed (Control widget) {
		if (!widget.hooks (EVENT) || widget.isDisposed ()) return;
		Latch latch = latch (widget);
		if (latch == null || latch.holds > 0) return;
		announce (widget, latch, count (widget) == 0);
	}

	/* The same, with an emptiness the widget tests more cheaply than its item count. */
	static void changed (Control widget, boolean empty) {
		if (!widget.hooks (EVENT) || widget.isDisposed ()) return;
		Latch latch = latch (widget);
		if (latch == null || latch.holds > 0) return;
		announce (widget, latch, empty);
	}

	/* Start of a compound change whose intermediate states are not announced. */
	static void hold (Control widget) {
		Latch latch = widget.hooks (EVENT) && !widget.isDisposed () && widget.isValidThread () ? latch (widget) : null;
		if (latch != null) latch.holds++;
	}

	/* End of a compound change (in a finally block): announces the net crossing, if any. */
	static void release (Control widget) {
		if (!widget.hooks (EVENT) || widget.isDisposed () || !widget.isValidThread ()) return;
		Latch latch = latch (widget);
		if (latch != null && latch.holds > 0) latch.holds--;
		changed (widget);
	}

	/* ------------------------------------------------------------------ helpers */

	/*
	 * Sends the crossing. A listener that changes the widget again is not answered from inside the
	 * dispatch (later listeners would see the states out of order): the change is noted, and once
	 * every listener has the event, the net state is announced if it differs.
	 */
	private static void announce (Control widget, Latch latch, boolean empty) {
		if (latch.sending) {
			latch.again = true;
			return;
		}
		latch.sending = true;
		try {
			while (empty != latch.empty) {
				latch.empty = empty;
				latch.again = false;
				Event event = new Event ();
				event.detail = empty ? BECAME_EMPTY : BECAME_NON_EMPTY;
				event.count = empty ? 0 : count (widget);
				widget.sendEvent (EVENT, event);
				if (!latch.again || widget.isDisposed () || latch (widget) != latch) break;
				empty = count (widget) == 0;
			}
		} finally {
			latch.sending = false;
		}
	}

	private static void add (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		if (latch (widget) == null) {
			Latch latch = new Latch ();
			latch.empty = count (widget) == 0;
			widget.setData (KEY, latch);
		}
		widget.addListener (EVENT, listener);
	}

	private static void remove (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		widget.removeListener (EVENT, listener);
		if (!widget.hooks (EVENT)) widget.setData (KEY, null);
	}

	private static Latch latch (Widget widget) {
		return widget.getData (KEY) instanceof Latch latch ? latch : null;
	}

	private static int count (Control widget) {
		if (widget instanceof Tree tree) return tree.getItemCount ();
		if (widget instanceof Table table) return table.getItemCount ();
		return ((List) widget).getItemCount ();
	}
}

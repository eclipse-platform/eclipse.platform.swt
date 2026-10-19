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
 * Visible-range observer of <code>Table</code>, <code>List</code> and <code>Tree</code>
 * (enhanced M3 API): which rows came into view and which left it.
 * <p>
 * A listener added here receives events of type {@link #EVENT}: first {@link #LEFT} for the rows
 * that left the visible range, then {@link #ENTERED} for the rows that entered it, each as at most
 * two ranges in <code>event.start</code> and <code>event.end</code> (inclusive; a Tree uses the
 * visible rows of {@link M3RowMath}). The visible range is
 * <code>M3ItemSelection.getVisibleRange</code>. The first listener hears the current range as
 * entered. Ranges are delivered, never items.
 * </p>
 * <p>
 * The range is checked when the widget scrolls, resizes, expands or collapses a row (once, at
 * the end of the event), and immediately when a viewport moves its window. In the original
 * mode a <code>setTopIndex</code> that is not followed by any of these is announced at the next
 * one. The win32 widgets drive the viewport checks.
 * </p>
 *
 * @since 3.138
 */
public final class M3VisibilityObserver {

	/** The event type: outside the range of the SWT event types. */
	public static final int EVENT = 0x4D330003;
	/** <code>event.detail</code>: the rows from <code>start</code> to <code>end</code> came into view. */
	public static final int ENTERED = 0;
	/** <code>event.detail</code>: the rows from <code>start</code> to <code>end</code> left the view. */
	public static final int LEFT = 1;

	private static final String KEY = "org.eclipse.swt.widgets.M3VisibilityObserver"; //$NON-NLS-1$

	/* The latch of a listened widget: the last announced range, and the trigger listener itself. */
	private static final class Latch implements Listener {
		final Control widget;
		int first, last = -1;
		boolean scheduled;

		Latch (Control widget) {
			this.widget = widget;
		}

		/* a trigger: check once at the end of the current event */
		@Override
		public void handleEvent (Event event) {
			if (scheduled || widget.isDisposed ()) return;
			scheduled = true;
			widget.getDisplay ().asyncExec (() -> {
				scheduled = false;
				if (!widget.isDisposed ()) check (widget, this);
			});
		}
	}

	private M3VisibilityObserver () {
	}

	/**
	 * Adds a listener for the visible range of the table.
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
	 * Adds a listener for the visible range of the list.
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
	 * Adds a listener for the visible rows of the tree.
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

	/* ------------------------------------------------------------------ widget seam */

	/* A viewport moved its window or the rows changed: check the visible range now. */
	static void changed (Control widget) {
		if (!widget.hooks (EVENT) || widget.isDisposed ()) return;
		Latch latch = latch (widget);
		if (latch != null) check (widget, latch);
	}

	/* ------------------------------------------------------------------ helpers */

	private static void check (Control widget, Latch latch) {
		M3ItemRange visible = visible (widget);
		int first = visible.isEmpty () ? 0 : visible.start (), last = visible.isEmpty () ? -1 : visible.end ();
		int oldFirst = latch.first, oldLast = latch.last;
		if (first == oldFirst && last == oldLast) return;
		latch.first = first;
		latch.last = last;
		M3Prefetch.difference (widget, EVENT, LEFT, oldFirst, oldLast, first, last);
		M3Prefetch.difference (widget, EVENT, ENTERED, first, last, oldFirst, oldLast);
	}

	private static M3ItemRange visible (Control widget) {
		if (widget instanceof Table table) return M3ItemSelection.getVisibleRange (table);
		if (widget instanceof Tree tree) return M3ItemSelection.getVisibleRange (tree);
		return M3ItemSelection.getVisibleRange ((List) widget);
	}

	private static void add (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		Latch latch = latch (widget);
		if (latch == null) {
			latch = new Latch (widget);
			widget.setData (KEY, latch);
			widget.addListener (SWT.Resize, latch);
			if (widget instanceof Tree) {
				widget.addListener (SWT.Expand, latch);
				widget.addListener (SWT.Collapse, latch);
			}
			ScrollBar bar = ((Scrollable) widget).getVerticalBar ();
			if (bar != null) bar.addListener (SWT.Selection, latch);
			widget.addListener (SWT.MouseVerticalWheel, latch);
			widget.addListener (SWT.KeyDown, latch);
		}
		widget.addListener (EVENT, listener);
		check (widget, latch);
	}

	private static void remove (Control widget, Listener listener) {
		if (widget == null || listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.checkWidget ();
		widget.removeListener (EVENT, listener);
		Latch latch = latch (widget);
		if (latch == null || widget.hooks (EVENT)) return;
		widget.removeListener (SWT.Resize, latch);
		widget.removeListener (SWT.Expand, latch);
		widget.removeListener (SWT.Collapse, latch);
		ScrollBar bar = ((Scrollable) widget).getVerticalBar ();
		if (bar != null) bar.removeListener (SWT.Selection, latch);
		widget.removeListener (SWT.MouseVerticalWheel, latch);
		widget.removeListener (SWT.KeyDown, latch);
		widget.setData (KEY, null);
	}

	private static Latch latch (Widget widget) {
		return widget.getData (KEY) instanceof Latch latch ? latch : null;
	}
}

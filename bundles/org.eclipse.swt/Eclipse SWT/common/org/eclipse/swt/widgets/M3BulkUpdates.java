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
 * Bulk updates of <code>Tree</code>, <code>Table</code> and <code>List</code> (enhanced M3 API).
 * <p>
 * {@link #run(Table, Runnable)} runs a group of edits (adds, removes, <code>setItemCount</code>,
 * text and selection changes) as one update: the viewport and scroll bar planes of the widget
 * are frozen while the edits run (see <code>Control.beginBatch</code>), so nothing is painted
 * in between even if the edits dispatch events, and at the end the widget repaints only what
 * changed, once. An {@link M3Emptiness} listener hears at most one crossing, with the final
 * count. The resulting state is the one the same edits produce without the call.
 * </p>
 * <p>
 * Calls nest. An exception thrown by the edits leaves nothing frozen and propagates unchanged.
 * With the viewports the native work is already planned (a Table append burst becomes one
 * native count change; List and Tree touch only their window); in the original mode the stock
 * per-item native calls remain and only their painting is coalesced.
 * </p>
 *
 * @since 3.138
 */
public final class M3BulkUpdates {

	/* the planes a structural edit changes; the main plane (layout, chrome) stays live */
	private static final int PLANES = SWT.PLANE_VIEWPORT | SWT.PLANE_SCROLLBARS;

	private M3BulkUpdates () {
	}

	/**
	 * Runs the edits on the tree as one update.
	 *
	 * @param tree the tree
	 * @param edits the edits
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the tree or the edits are null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static void run (Tree tree, Runnable edits) {
		bulk (tree, edits);
	}

	/**
	 * Runs the edits on the table as one update.
	 *
	 * @param table the table
	 * @param edits the edits
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the table or the edits are null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static void run (Table table, Runnable edits) {
		bulk (table, edits);
	}

	/**
	 * Runs the edits on the list as one update.
	 *
	 * @param list the list
	 * @param edits the edits
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the list or the edits are null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the list has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the list</li>
	 * </ul>
	 */
	public static void run (List list, Runnable edits) {
		bulk (list, edits);
	}

	private static void bulk (Control widget, Runnable edits) {
		if (widget == null || edits == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		widget.beginBatch (PLANES);
		M3Emptiness.hold (widget);
		try {
			edits.run ();
		} finally {
			/* the edits may have disposed the widget: then there is nothing left to resume */
			if (!widget.isDisposed ()) {
				try {
					M3Emptiness.release (widget);
				} finally {
					widget.endBatch (PLANES);
				}
			}
		}
	}
}

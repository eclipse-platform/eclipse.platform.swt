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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer PopUpFilterUI, ColumnFilterUI and
 *         ViewerFilterUI (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * The column filter popup of a Table or Tree (enhanced M3 API).
 * <p>
 * A right click on a column header (<code>SWT.MenuDetect</code> over the header) opens a small
 * tool window under the column with the column's filter: an enablement choice, the match type
 * (wildcard, regular expression, contains, starts with, equals, and their negations), the pattern,
 * and tools for the default pattern, pasting the clipboard and closing. Edits apply to the
 * {@link M3ColumnFilter} after a short delay; Return applies at once and closes, Escape closes. The
 * popup also closes when the control is clicked, when the window moves, or when it is deactivated.
 * Columns with an active criterion show a filter mark in their header.
 * </p>
 *
 * @since 3.138
 */
public final class M3FilterPopup {

	private static final String KEY = "org.eclipse.swt.m3.M3FilterPopup"; //$NON-NLS-1$

	private final Composite control;
	private final M3ColumnFilter filter;
	private final Listener listener = this::handleEvent;
	private final M3RowView.Listener marks = change -> marks ();
	private Shell shell;
	private M3FilterEditPanel panel;
	private int column = -1;

	private M3FilterPopup (Composite control, M3ColumnFilter filter) {
		this.control = control;
		this.filter = filter;
		control.addListener (SWT.MenuDetect, listener);
		control.addListener (SWT.MouseDown, listener);
		control.addListener (SWT.Dispose, listener);
		control.getShell ().addListener (SWT.Move, listener);
		control.getShell ().addListener (SWT.Resize, listener);
		filter.addListener (marks);
		control.setData (KEY, this);
		marks ();
	}

	/**
	 * Installs the filter popup on the column headers of a table.
	 *
	 * @param table the table
	 * @param filter the filter of the table's columns
	 * @return the popup
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the table has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the table</li>
	 * </ul>
	 */
	public static M3FilterPopup install (Table table, M3ColumnFilter filter) {
		return install ((Composite) M3Checks.nonNull (table), filter);
	}

	/**
	 * Installs the filter popup on the column headers of a tree.
	 *
	 * @param tree the tree
	 * @param filter the filter of the tree's columns
	 * @return the popup
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the tree has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the tree</li>
	 * </ul>
	 */
	public static M3FilterPopup install (Tree tree, M3ColumnFilter filter) {
		return install ((Composite) M3Checks.nonNull (tree), filter);
	}

	private static M3FilterPopup install (Composite control, M3ColumnFilter filter) {
		M3Checks.nonNull (filter);
		if (control.getData (KEY) instanceof M3FilterPopup old) old.dispose ();
		return new M3FilterPopup (control, filter);
	}

	/**
	 * Opens the popup for a column, under its header (closing a popup that is open).
	 *
	 * @param column the column
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column is not a column of both the control and the filter</li>
	 * </ul>
	 */
	public void open (int column) {
		M3Checks.range (column, Math.min (M3Columns.count (control), filter.getColumnCount ()));
		close ();
		this.column = column;
		shell = new Shell (control.getShell (), SWT.ON_TOP | SWT.TOOL);
		shell.setLayout (new FillLayout ());
		panel = new M3FilterEditPanel (shell, filter, column, M3Columns.column (control, column).getText (), this::close, this::marks);
		shell.addListener (SWT.Deactivate, listener);
		shell.pack ();
		shell.setLocation (location (shell.getSize ()));
		shell.setVisible (true);
		panel.pattern.setFocus ();
	}

	/* under the column header, kept inside the monitor's client area (TV_5.0 PopUpFilterUI) */
	private Point location (Point size) {
		Point location = control.toDisplay (Math.max (0, M3Columns.left (control, column)), M3Columns.headerHeight (control));
		Rectangle bounds = control.getMonitor ().getClientArea ();
		location.x = Math.max (bounds.x, Math.min (location.x, bounds.x + bounds.width - size.x));
		location.y = Math.max (bounds.y, Math.min (location.y, bounds.y + bounds.height - size.y));
		return location;
	}

	/** Closes the popup if it is open; edits not yet applied are applied first. */
	public void close () {
		if (shell == null) return;
		Shell closing = shell;
		shell = null;
		column = -1;
		panel.apply ();
		panel = null;
		closing.dispose ();
	}

	/**
	 * Returns whether the popup is open.
	 *
	 * @return whether it is open
	 */
	public boolean isOpen () {
		return shell != null;
	}

	/**
	 * Returns the column whose filter is open, or -1.
	 *
	 * @return the open column, or -1
	 */
	public int getColumn () {
		return column;
	}

	/**
	 * Returns the popup window while it is open, or <code>null</code>.
	 *
	 * @return the window, or <code>null</code>
	 */
	public Shell getShell () {
		return shell;
	}

	/** Removes the popup from the control (closing it when open). */
	public void dispose () {
		close ();
		filter.removeListener (marks);
		if (control.isDisposed ()) return;
		control.removeListener (SWT.MenuDetect, listener);
		control.removeListener (SWT.MouseDown, listener);
		control.removeListener (SWT.Dispose, listener);
		control.getShell ().removeListener (SWT.Move, listener);
		control.getShell ().removeListener (SWT.Resize, listener);
		control.setData (KEY, null);
	}

	private void handleEvent (Event event) {
		switch (event.type) {
			case SWT.MenuDetect: menuDetected (event); break;
			case SWT.Dispose: dispose (); break;
			case SWT.Deactivate: control.getDisplay ().asyncExec (this::deactivated); break;
			default: close (); break;
		}
	}

	/* a right click on the header opens the column's filter instead of the control's menu */
	private void menuDetected (Event event) {
		Point point = control.toControl (event.x, event.y);
		if (event.detail != SWT.MENU_MOUSE || point.y < 0 || point.y >= M3Columns.headerHeight (control)) return;
		int at = M3Columns.columnAt (control, point.x);
		if (at < 0 || at >= filter.getColumnCount ()) return;
		event.doit = false;
		open (at);
	}

	private void deactivated () {
		if (shell != null && control.getDisplay ().getActiveShell () != shell) close ();
	}

	private void marks () {
		if (control.isDisposed ()) return;
		M3HeaderDecoration decoration = M3HeaderDecoration.of (control);
		for (int i = 0, n = Math.min (M3Columns.count (control), filter.getColumnCount ()); i < n; i++) {
			decoration.setFiltered (i, filter.isActive (i));
		}
	}
}

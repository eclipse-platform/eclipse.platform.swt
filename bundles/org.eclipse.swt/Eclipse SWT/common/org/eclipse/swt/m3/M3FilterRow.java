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
package org.eclipse.swt.m3;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A filter row for a Table or Tree: one text field per column, aligned with the columns
 * (enhanced M3 API).
 * <p>
 * Place the row above the control with any layout. Each field follows its column's position,
 * width and order and the control's horizontal scrolling. Typing in a field sets the column's
 * criterion of the {@link M3ColumnFilter} to "contains, ignoring case" (commas separate
 * alternatives, see {@link M3TextMatch}) after a short delay; an
 * empty field removes it. Call {@link #refresh()} after columns are created or disposed.
 * </p>
 *
 * @since 3.138
 */
public final class M3FilterRow extends Composite {

	static final int DELAY = 250;

	private final Composite control;
	private final M3ColumnFilter filter;
	private final java.util.List<Text> texts = new ArrayList<> ();
	private final Listener listener = this::handleEvent;
	private final Runnable apply = this::apply;
	private final boolean [] pending;

	/**
	 * Constructs a filter row for a table.
	 *
	 * @param parent the parent of the row
	 * @param table the table whose columns the row follows
	 * @param filter the filter of the table's columns
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 * </ul>
	 */
	public M3FilterRow (Composite parent, Table table, M3ColumnFilter filter) {
		this (parent, (Composite) M3Checks.nonNull (table), filter);
	}

	/**
	 * Constructs a filter row for a tree.
	 *
	 * @param parent the parent of the row
	 * @param tree the tree whose columns the row follows
	 * @param filter the filter of the tree's columns
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if an argument is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 * </ul>
	 */
	public M3FilterRow (Composite parent, Tree tree, M3ColumnFilter filter) {
		this (parent, (Composite) M3Checks.nonNull (tree), filter);
	}

	private M3FilterRow (Composite parent, Composite control, M3ColumnFilter filter) {
		super (parent, SWT.NONE);
		this.control = control;
		this.filter = M3Checks.nonNull (filter);
		pending = new boolean [filter.getColumnCount ()];
		control.addListener (SWT.Resize, listener);
		control.addListener (SWT.Dispose, listener);
		ScrollBar bar = control.getHorizontalBar ();
		if (bar != null) bar.addListener (SWT.Selection, listener);
		addListener (SWT.Resize, listener);
		addListener (SWT.Dispose, listener);
		refresh ();
	}

	/** Creates the fields of new columns and lays the fields out again. */
	public void refresh () {
		checkWidget ();
		int count = Math.min (M3Columns.count (control), filter.getColumnCount ());
		while (texts.size () > count) texts.remove (texts.size () - 1).dispose ();
		while (texts.size () < count) {
			Text text = new Text (this, SWT.SINGLE | SWT.BORDER | SWT.SEARCH);
			text.setData (Integer.valueOf (texts.size ()));
			text.addListener (SWT.Modify, listener);
			texts.add (text);
		}
		for (int i = 0; i < count; i++) {
			Item column = M3Columns.column (control, i);
			column.removeListener (SWT.Resize, listener);
			column.removeListener (SWT.Move, listener);
			column.addListener (SWT.Resize, listener);
			column.addListener (SWT.Move, listener);
		}
		layoutFields ();
	}

	/**
	 * Returns the field of a column.
	 *
	 * @param column the column
	 * @return the field
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the column has no field</li>
	 * </ul>
	 */
	public Text getText (int column) {
		return texts.get (M3Checks.range (column, texts.size ()));
	}

	@Override
	public Point computeSize (int wHint, int hHint, boolean changed) {
		checkWidget ();
		int height = texts.isEmpty () ? 0 : texts.get (0).computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
		return new Point (wHint == SWT.DEFAULT ? control.getSize ().x : wHint, hHint == SWT.DEFAULT ? height : hHint);
	}

	/* each field at its column's left edge and width, in the row's coordinates */
	private void layoutFields () {
		int offset = toControl (control.toDisplay (0, 0)).x, height = getClientArea ().height;
		for (int i = 0; i < texts.size (); i++) {
			Item column = M3Columns.column (control, i);
			texts.get (i).setBounds (offset + M3Columns.left (control, i), 0, M3Columns.width (column), height);
		}
	}

	private void handleEvent (Event event) {
		if (event.type == SWT.Dispose) {
			disposed (event);
		} else if (event.type == SWT.Modify) {
			pending [(Integer) event.widget.getData ()] = true;
			getDisplay ().timerExec (DELAY, apply);
		} else if (!isDisposed ()) {
			layoutFields ();
		}
	}

	private void disposed (Event event) {
		if (event.widget == control) {
			if (!isDisposed ()) dispose ();
			return;
		}
		getDisplay ().timerExec (-1, apply);
		if (control.isDisposed ()) return;
		control.removeListener (SWT.Resize, listener);
		control.removeListener (SWT.Dispose, listener);
		ScrollBar bar = control.getHorizontalBar ();
		if (bar != null) bar.removeListener (SWT.Selection, listener);
		for (int i = 0, n = M3Columns.count (control); i < n; i++) {
			M3Columns.column (control, i).removeListener (SWT.Resize, listener);
			M3Columns.column (control, i).removeListener (SWT.Move, listener);
		}
	}

	/* applies the fields that changed since the last apply */
	void apply () {
		if (isDisposed ()) return;
		getDisplay ().timerExec (-1, apply);
		for (int i = 0; i < texts.size (); i++) {
			if (!pending [i]) continue;
			pending [i] = false;
			String text = texts.get (i).getText ();
			filter.setText (i, text.isEmpty () ? null : text, M3TextMatch.CONTAINS | M3TextMatch.IGNORE_CASE);
			M3HeaderDecoration.of (control).setFiltered (i, filter.isActive (i));
		}
	}
}

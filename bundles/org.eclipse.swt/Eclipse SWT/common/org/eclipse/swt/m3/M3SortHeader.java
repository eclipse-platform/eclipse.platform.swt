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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/*
 * Connects the column headers of a Table or Tree to an M3ColumnSort: a column Selection (header
 * click) toggles the column, with Shift accumulating (NatTable accumulate pattern, Shift per the
 * user). After every change of the sort keys the primary key gets the native sort indicator and,
 * with two or more keys, every sorted column shows its direction and priority number through the
 * shared header decoration. Installing again picks up columns created since; each column is
 * hooked once.
 */
final class M3SortHeader implements Listener, M3RowView.Listener {

	private static final String KEY = "org.eclipse.swt.m3.M3SortHeader"; //$NON-NLS-1$

	private final M3ColumnSort sort;
	private final Composite control;

	private M3SortHeader (M3ColumnSort sort, Composite control) {
		this.sort = sort;
		this.control = control;
	}

	static void install (M3ColumnSort sort, Composite control) {
		Object old = control.getData (KEY);
		M3SortHeader header = old instanceof M3SortHeader installed && installed.sort == sort ? installed : null;
		if (header == null) {
			if (old instanceof M3SortHeader installed) installed.uninstall ();
			header = new M3SortHeader (sort, control);
			control.setData (KEY, header);
			sort.addListener (header);
			control.addListener (SWT.Dispose, header);
		}
		for (int i = 0, n = M3Columns.count (control); i < n; i++) {
			Item column = M3Columns.column (control, i);
			if (column.getData (KEY) != header) {
				column.setData (KEY, header);
				column.addListener (SWT.Selection, header);
			}
		}
		header.indicate ();
	}

	private void uninstall () {
		sort.removeListener (this);
		control.removeListener (SWT.Dispose, this);
		for (int i = 0, n = M3Columns.count (control); i < n; i++) {
			Item column = M3Columns.column (control, i);
			column.removeListener (SWT.Selection, this);
			column.setData (KEY, null);
		}
	}

	@Override
	public void handleEvent (Event event) {
		if (event.type == SWT.Dispose) {
			sort.removeListener (this);
			return;
		}
		int column = M3Columns.indexOf (control, (Item) event.widget);
		if (column < sort.getColumnCount ()) sort.toggle (column, (event.stateMask & SWT.SHIFT) != 0);
	}

	@Override
	public void viewChanged (M3RowChange change) {
		if (change.kind () == M3RowChange.ORDER && !control.isDisposed ()) indicate ();
	}

	/* native indicator for the primary key; numbered marks when two or more keys are active */
	private void indicate () {
		int keys = sort.getSortKeyCount ();
		M3Columns.setSortIndicator (control, keys == 0 ? -1 : sort.getSortColumn (0), keys == 0 ? SWT.NONE : sort.getSortDirection (0));
		M3HeaderDecoration decoration = M3HeaderDecoration.of (control);
		for (int i = 0, n = Math.min (M3Columns.count (control), sort.getColumnCount ()); i < n; i++) {
			int priority = sort.getPriority (i);
			boolean numbered = keys > 1 && priority >= 0;
			decoration.setSort (i, numbered ? sort.getDirection (i) : SWT.NONE, numbered ? priority + 1 : 0);
		}
	}
}

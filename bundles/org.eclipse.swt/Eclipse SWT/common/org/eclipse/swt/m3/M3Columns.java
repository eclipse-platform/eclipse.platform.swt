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

import org.eclipse.swt.widgets.*;

/* The column API that Table and Tree share without a common type (a Table is a Tree of depth 1). */
final class M3Columns {

	private M3Columns () {
	}

	static int count (Composite control) {
		return control instanceof Table table ? table.getColumnCount () : ((Tree) control).getColumnCount ();
	}

	static Item column (Composite control, int index) {
		return control instanceof Table table ? table.getColumn (index) : ((Tree) control).getColumn (index);
	}

	static int indexOf (Composite control, Item column) {
		return control instanceof Table table ? table.indexOf ((TableColumn) column) : ((Tree) control).indexOf ((TreeColumn) column);
	}

	static int width (Item column) {
		return column instanceof TableColumn tableColumn ? tableColumn.getWidth () : ((TreeColumn) column).getWidth ();
	}

	static int [] order (Composite control) {
		return control instanceof Table table ? table.getColumnOrder () : ((Tree) control).getColumnOrder ();
	}

	static int headerHeight (Composite control) {
		if (control instanceof Table table) return table.getHeaderVisible () ? table.getHeaderHeight () : 0;
		Tree tree = (Tree) control;
		return tree.getHeaderVisible () ? tree.getHeaderHeight () : 0;
	}

	static void setSortIndicator (Composite control, int column, int direction) {
		if (control instanceof Table table) {
			table.setSortColumn (column < 0 ? null : table.getColumn (column));
			table.setSortDirection (direction);
		} else {
			Tree tree = (Tree) control;
			tree.setSortColumn (column < 0 ? null : tree.getColumn (column));
			tree.setSortDirection (direction);
		}
	}

	/* the x of the first column in control coordinates: the client area origin minus the horizontal scroll (a Table scrolls inside its client area, a win32 Tree moves its item window under the header) */
	static int origin (Composite control) {
		ScrollBar bar = control.getHorizontalBar ();
		return control.getClientArea ().x - (bar == null ? 0 : bar.getSelection ());
	}

	/* the left edge of a column in control coordinates, following the column order and the horizontal scroll */
	static int left (Composite control, int column) {
		int x = origin (control);
		for (int index : order (control)) {
			if (index == column) break;
			x += width (column (control, index));
		}
		return x;
	}

	/* the column under x (control coordinates), or -1 */
	static int columnAt (Composite control, int x) {
		int left = origin (control);
		for (int index : order (control)) {
			int right = left + width (column (control, index));
			if (x >= left && x < right) return index;
			left = right;
		}
		return -1;
	}
}

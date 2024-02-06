/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import java.util.*;

import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;

/**
 * This class is used to describe a table for objects that have an accessible
 * role of ACC.ROLE_TABLE, but aren't implemented like NSTableViews. That means they
 * report back their children as a list of cells in row-major order instead of a list of
 * rows with cells as children of those rows. The assumption is that the first 'row'
 * of cells (cell 0 to cell 'column-count - 1') are the column headers of the table.

 * Note that this class is NOT a subclass of Accessible, because it is not a child of the
 * table accessible. It exists to factor out the logic when working with a lightweight table
 * that doesn't conform to Cocoa's expectations. Specifically, it reports back a list of row
 * and column objects and a header as its children, and identifies which cell the mouse is
 * over.
 */
class TableAccessibleDelegate {

	Map<Integer, AccessibleTableColumn> childColumnToIdMap = new HashMap<>();
	Map<Integer, AccessibleTableRow> childRowToIdMap = new HashMap<>();
	Accessible tableAccessible;
	AccessibleTableHeader headerAccessible;

	public TableAccessibleDelegate(Accessible accessible) {
		tableAccessible = accessible;

		tableAccessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildCount(AccessibleControlEvent e) {
				/* Return the number of row and column children.
				 * If the CTable2 is being used as a list (i.e. if columns.length == 0) then add 1 column child.
				 * If there is a column header (i.e. if columns.length > 0) then add 1 "column header" child.
				 */
				e.detail = childColumnToIdMap.size() + childRowToIdMap.size();
				if (childColumnToIdMap.size() > 1) e.detail++;
			}
			@Override
			public void getChildren(AccessibleControlEvent e) {
				int childCount = childColumnToIdMap.size() + childRowToIdMap.size();
				if (childColumnToIdMap.size() > 1) childCount++;
				Accessible[] children = new Accessible[childCount];
				int childIndex = 0;

				for (AccessibleTableRow row : childRowToIdMap.values()) {
					children[childIndex++] = row;
				}
				for (AccessibleTableColumn col : childColumnToIdMap.values()) {
					children[childIndex++] = col;
				}

				if (childColumnToIdMap.size() > 1) children[childIndex] = headerAccessible();

				e.children = children;
			}

			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				NSPoint testPoint = new NSPoint();
				testPoint.x = e.x;
				Monitor primaryMonitor = Display.getCurrent().getPrimaryMonitor();
				testPoint.y = primaryMonitor.getBounds().height - e.y;

				for (AccessibleTableRow row : childRowToIdMap.values()) {
					NSValue locationValue = new NSValue(row.getPositionAttribute(ACC.CHILDID_SELF).id);
					NSPoint location = locationValue.pointValue();

					NSValue sizeValue = new NSValue(row.getSizeAttribute(ACC.CHILDID_SELF));
					NSSize size = sizeValue.sizeValue();

					if (location.y < testPoint.y && testPoint.y < (location.y + size.height)) {
						AccessibleControlEvent e2 = new AccessibleControlEvent(e.getSource());
						e2.x = (int) testPoint.x;
						e2.y = (int) testPoint.y;
						row.getChildAtPoint(e);
						break;
					}
				}
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				int state = ACC.STATE_NORMAL | ACC.STATE_FOCUSABLE | ACC.STATE_SELECTABLE;

				AccessibleTableEvent event = new AccessibleTableEvent(this);
				for (int i = 0; i < tableAccessible.accessibleTableListeners.size(); i++) {
					AccessibleTableListener listener = tableAccessible.accessibleTableListeners.get(i);
					listener.getSelectedRows(event);
				}

				if (event.selected != null) {
					int[] selected = event.selected;

					for (int i = 0; i < selected.length; i++) {
						if (selected[i] == tableAccessible.index) {
							state |= ACC.STATE_SELECTED;
							break;
						}
					}
				}

				NSNumber focusedObject = (NSNumber)tableAccessible.getFocusedAttribute(ACC.CHILDID_SELF);
				if (focusedObject.boolValue()) {
					state |= ACC.STATE_FOCUSED;
				}

				e.detail = state;
			}
		});

		tableAccessible.addAccessibleTableListener(new AccessibleTableAdapter() {
			@Override
			public void getColumnCount(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);

				for (int i = 0; i < tableAccessible.accessibleTableListeners.size(); i++) {
					AccessibleTableListener listener = tableAccessible.accessibleTableListeners.get(i);
					if (listener != this) listener.getColumnCount(event);
				}

				e.count = event.count;
			}
			@Override
			public void getColumn(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				getColumns(event);
				e.accessible = event.accessibles[e.column];
			}
			@Override
			public void getColumns(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				getColumnCount(event);

				// This happens if the table listeners just report back a column count but don't have
				// distinct objects for each of the column.
				// When that happens we need to make 'fake' accessibles that represent the rows.
				if (event.count != childColumnToIdMap.size()) {
					childColumnToIdMap.clear();
				}

				Accessible[] columns = new Accessible[event.count];

				for (int i = 0; i < event.count; i++) {
					columns[i] = childColumnToOs(i);
				}

				int columnCount = childColumnToIdMap.size() > 0 ? childColumnToIdMap.size() : 1;
				Accessible[] accessibles = new Accessible[columnCount];
				for (int i = 0; i < columnCount; i++) {
					accessibles[i] = childColumnToIdMap.get(i);
				}
				e.accessibles = accessibles;
			}
			@Override
			public void getColumnHeader(AccessibleTableEvent e) {
				e.accessible = (childColumnToIdMap.size() > 1 ? headerAccessible() : null);
			}
			@Override
			public void getRowCount(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);

				for (int i = 0; i < tableAccessible.accessibleTableListeners.size(); i++) {
					AccessibleTableListener listener = tableAccessible.accessibleTableListeners.get(i);
					if (listener != this) listener.getRowCount(event);
				}

				e.count = event.count;
			}
			@Override
			public void getRow(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				getRows(event);
				e.accessible = event.accessibles[e.row];
			}
			@Override
			public void getRows(AccessibleTableEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				getRowCount(event);

				// This happens if the table listeners just report back a column count but don't have
				// distinct objects for each of the column.
				// When that happens we need to make 'fake' accessibles that represent the rows.
				if (event.count != childRowToIdMap.size()) {
					childRowToIdMap.clear();
				}

				Accessible[] rows = new Accessible[event.count];

				for (int i = 0; i < event.count; i++) {
					rows[i] = childRowToOs(i);
				}

				int columnCount = childRowToIdMap.size() > 0 ? childRowToIdMap.size() : 1;
				Accessible[] accessibles = new Accessible[columnCount];
				for (int i = 0; i < columnCount; i++) {
					accessibles[i] = childRowToIdMap.get(i);
				}
				e.accessibles = accessibles;
			}
		});

	}

	Accessible childColumnToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) {
			return tableAccessible;
		}

		/* Check cache for childID, if found, return corresponding osChildID. */
		AccessibleTableColumn childRef = childColumnToIdMap.get(childID);

		if (childRef == null) {
			childRef = new AccessibleTableColumn(tableAccessible, childID);
			childColumnToIdMap.put(childID, childRef);
		}

		return childRef;
	}

	Accessible childRowToOs(int childID) {
		if (childID == ACC.CHILDID_SELF) {
			return tableAccessible;
		}

		/* Check cache for childID, if found, return corresponding osChildID. */
		AccessibleTableRow childRef = childRowToIdMap.get(childID);

		if (childRef == null) {
			childRef = new AccessibleTableRow(tableAccessible, childID);
			childRowToIdMap.put(Integer.valueOf(childID), childRef);
		}

		return childRef;
	}

	AccessibleTableHeader headerAccessible() {
		if (headerAccessible == null) headerAccessible = new AccessibleTableHeader(tableAccessible, ACC.CHILDID_SELF);
		return headerAccessible;
	}

	void release() {
		if (childRowToIdMap != null) {
			for (AccessibleTableRow delegate : childRowToIdMap.values()) {
				SWTAccessibleDelegate childDelegate = delegate.delegate;
				if (childDelegate != null) {
					childDelegate.internal_dispose_SWTAccessibleDelegate();
					childDelegate.release();
				}
			}

			childRowToIdMap.clear();
			childRowToIdMap = null;
		}

		if (childColumnToIdMap != null) {
			for (AccessibleTableColumn delegate : childColumnToIdMap.values()) {
				SWTAccessibleDelegate childDelegate = delegate.delegate;
				if (childDelegate != null) {
					childDelegate.internal_dispose_SWTAccessibleDelegate();
					childDelegate.release();
				}
			}

			childColumnToIdMap.clear();
			childColumnToIdMap = null;
		}
	}

	void reset() {
		release();
		childColumnToIdMap = new HashMap<>();
		childRowToIdMap = new HashMap<>();
	}
}

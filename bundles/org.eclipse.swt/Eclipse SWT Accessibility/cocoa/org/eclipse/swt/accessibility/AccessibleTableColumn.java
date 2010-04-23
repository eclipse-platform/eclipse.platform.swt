/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;

import org.eclipse.swt.internal.cocoa.*;

/**
 * This class is used to describe a table column for objects that have an accessible
 * role of ACC.ROLE_TABLE, but aren't implemented like NSTableViews. 
 *
 * Instances of this class represent one column in a table. Cocoa accessibility expects
 * columns to report their location, number of rows, and elements in those rows.
 * 
 * @see TableAccessibleDelegate
 */
class AccessibleTableColumn extends Accessible {

	public AccessibleTableColumn(Accessible accessible, int childID) {
		super(accessible);
		index = childID;
		
		addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getLocation(AccessibleControlEvent e) {
				Accessible[] cells = AccessibleTableColumn.this.getColumnCells();

				// Ask first row for position.
				AccessibleControlEvent event = new AccessibleControlEvent(this);
				event.childID = ACC.CHILDID_SELF;
				event.width = -1;
				Accessible child = cells[0];
				
				for (int i = 0; i < child.accessibleControlListeners.size(); i++) {
					AccessibleControlListener listener = (AccessibleControlListener) child.accessibleControlListeners.elementAt(i);
					listener.getLocation(event);
				}

				// Ask all children for size.
				int height = 0;
				int width = 0;
				for (int j = 0; j < cells.length; j++) {
					NSValue sizeObj = (NSValue)cells[j].getSizeAttribute(ACC.CHILDID_SELF);
					NSSize size = sizeObj.sizeValue();
					if (size.width > width) width = (int) size.width;
					height += size.height;
				}
				
				e.x = event.x;
				e.y = event.y;
				e.width = width;
				e.height = height;
			}
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COLUMN;
			}
		});
		addAccessibleTableListener(new AccessibleTableAdapter() {
			public void getRowCount(AccessibleTableEvent e) {
				e.count = getColumnCells().length;
			}
			public void getRow(AccessibleTableEvent e) {
				int index = e.row;
				Accessible[] children = getColumnCells();
				int count = children.length;
				if (0 <= index && index < count) {
					e.accessible = children[index];
				}
			}
			public void getRows(AccessibleTableEvent e) {
				e.accessibles = getColumnCells();
			}
		});
	}
	
	private Accessible[] getColumnCells() {
		int validRowCount = Math.max (1, parent.getRowCount());
		Accessible[] cells = new Accessible[validRowCount];
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < validRowCount; i++) {
			event.column = index;
			event.row = i;
	
			for (int j = 0; j < parent.accessibleTableListeners.size(); j++) {
				AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(j);
				listener.getCell(event);
			}
	
			cells[i] = event.accessible;
		}
		return cells;
	}

}

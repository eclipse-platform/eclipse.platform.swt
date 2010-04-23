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
 * Instances of this class represent one row in a table. Cocoa accessibility expects
 * rows to report their location, and assumes the cells of the table are children of the rows.
 * 
 * @see TableAccessibleDelegate
 */
class AccessibleTableRow extends Accessible {

	public AccessibleTableRow(Accessible accessible, int childID) {
		super(accessible);
		index = childID;
		
		addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = Math.max (1, parent.getColumnCount());
			}
			public void getChildren(AccessibleControlEvent e) {
				int validColumnCount = Math.max (1, parent.getColumnCount());
				Object[] children = new Object[validColumnCount];
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				for (int i = 0; i < validColumnCount; i++) {
					event.column = i;
					event.row = index;
					for (int j = 0; j < parent.accessibleTableListeners.size(); j++) {
						AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(j);
						listener.getCell(event);
					}
					
					if (event.accessible != null) {
						event.accessible.parent = AccessibleTableRow.this;
					}
					
					children[i] = event.accessible;
				}
				
				e.children = children;
			}			
			public void getLocation(AccessibleControlEvent e) {
				int validColumnCount = Math.max (1, parent.getColumnCount());
				Accessible[] children = new Accessible[validColumnCount];
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				for (int i = 0; i < validColumnCount; i++) {
					event.column = i;
					event.row = index;

					for (int j = 0; j < parent.accessibleTableListeners.size(); j++) {
						AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(j);
						listener.getCell(event);
					}

					children[i] = event.accessible;
				}

				// Ask first child for position.
				NSValue positionObj = (NSValue)children[0].getPositionAttribute(ACC.CHILDID_SELF);
				NSPoint position = positionObj.pointValue();
				
				// Ask all children for size.
				int height = 0;
				int width = 0;
				for (int j = 0; j < children.length; j++) {
					NSValue sizeObj = (NSValue)children[j].getSizeAttribute(ACC.CHILDID_SELF);
					NSSize size = sizeObj.sizeValue();
					if (size.height > height) height = (int) size.height;
					width += size.width;
				}
				e.x = (int) position.x;
				// Flip y coordinate for Cocoa.
				NSArray screens = NSScreen.screens();
				NSScreen screen = new NSScreen(screens.objectAtIndex(0));
				NSRect frame = screen.frame();
				e.y = (int) (frame.height - position.y - height);
				e.width = width;
				e.height = height;
			}

			public void getRole(AccessibleControlEvent e) {
				int childID = e.childID;
				if (childID == ACC.CHILDID_SELF) {
					e.detail = ACC.ROLE_ROW;
				} else {
					e.detail = ACC.ROLE_TABLECELL;
				}
			}

			public void getFocus(AccessibleControlEvent e) {
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				event.column = 0;
				event.row = index;
				for (int j = 0; j < parent.accessibleTableListeners.size(); j++) {
					AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(j);
					listener.getCell(event);
				}
				if (event.accessible != null) {
					NSNumber focusedObj = (NSNumber) event.accessible.getFocusedAttribute(ACC.CHILDID_SELF);
					e.childID = focusedObj.boolValue() ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
				} else {
					e.childID = ACC.CHILDID_NONE;
				}
			}
		});
		
		addAccessibleTableListener(new AccessibleTableAdapter() {
			public void isColumnSelected(AccessibleTableEvent e) {
				e.isSelected = false;
			}
			public void isRowSelected(AccessibleTableEvent e) {
				// Delegate to the parent table.
				AccessibleTableEvent event = new AccessibleTableEvent(this);
				event.row = e.row;
				for (int i = 0; i < parent.accessibleTableListeners.size(); i++) {
					AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(i);
					listener.isRowSelected(event);
				}
				e.isSelected = event.isSelected;
			}			
		});
	}
	
	void getChildAtPoint(AccessibleControlEvent e) {
		int validColumnCount = Math.max (1, parent.getColumnCount());
		Accessible[] children = new Accessible[validColumnCount];
		AccessibleTableEvent event = new AccessibleTableEvent(this);
		for (int i = 0; i < validColumnCount; i++) {
			event.column = i;
			event.row = index;

			for (int j = 0; j < parent.accessibleTableListeners.size(); j++) {
				AccessibleTableListener listener = (AccessibleTableListener)parent.accessibleTableListeners.elementAt(j);
				listener.getCell(event);
			}

			children[i] = event.accessible;
		}
		
		for (int j = 0; j < children.length; j++) {
			NSValue positionObj = (NSValue)children[j].getPositionAttribute(index);
			NSPoint position = positionObj.pointValue();
			
			NSValue sizeObj = (NSValue)children[j].getSizeAttribute(index);
			NSSize size = sizeObj.sizeValue();

			if (position.x <= e.x && e.x <= position.x + size.width) {
				children[j].parent = this;
				e.accessible = children[j];
				break;
			}
		}
	}
}

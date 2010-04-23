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
 * This class is used to describe a table header for objects that have an accessible
 * role of ACC.ROLE_TABLE, but aren't implemented like NSTableViews. That means they
 * report back their children as a list of cells in row-major order instead of a list of
 * rows with cells as children of those rows. The assumption is that the first 'row'
 * of cells (cell 0 to cell 'column-count - 1') are the column headers of the table.
 *
 * This class works with the parent control to act as the header section of the table, 
 * and reports the cells in the header so that screen readers (VoiceOver, mainly) can
 * identify the column of the cell that the VoiceOver cursor is reading.
 */
class AccessibleTableHeader extends Accessible {

	public AccessibleTableHeader(Accessible accessible, int childID) {
		super(accessible);
		index = childID;

		addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getChildren(AccessibleControlEvent e) {
				int validColumnCount = Math.max (1, parent.getColumnCount());
				Accessible[] children = new Accessible[validColumnCount];
				AccessibleControlEvent event = new AccessibleControlEvent(this);

				for (int i = 0; i < validColumnCount; i++) {
					event.childID = i;
					event.detail = ACC.CHILDID_CHILD_AT_INDEX;

					for (int j = 0; j < parent.accessibleControlListeners.size(); j++) {
						AccessibleControlListener listener = (AccessibleControlListener) parent.accessibleControlListeners.elementAt(j);
						listener.getChild(event);
					}

					event.accessible.parent = AccessibleTableHeader.this;
					children[i] = event.accessible;
				}
				e.children = children; 
			}
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = Math.max (1, parent.getColumnCount());
			}
			public void getLocation(AccessibleControlEvent e) {
				int validColumnCount = Math.max (1, parent.getColumnCount());
				Accessible[] children = new Accessible[validColumnCount];
				AccessibleControlEvent event = new AccessibleControlEvent(this);

				for (int i = 0; i < validColumnCount; i++) {
					event.childID = i;
					event.detail = ACC.CHILDID_CHILD_AT_INDEX;

					for (int j = 0; j < parent.accessibleControlListeners.size(); j++) {
						AccessibleControlListener listener = (AccessibleControlListener) parent.accessibleControlListeners.elementAt(j);
						listener.getChild(event);
					}

					event.accessible.parent = AccessibleTableHeader.this;
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
				e.detail = ACC.ROLE_TABLECOLUMNHEADER;
			}
		});
	}

}

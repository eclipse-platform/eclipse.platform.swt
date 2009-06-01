/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTableHeaderCell extends NSTextFieldCell {

public NSTableHeaderCell() {
	super();
}

public NSTableHeaderCell(int /*long*/ id) {
	super(id);
}

public NSTableHeaderCell(id id) {
	super(id);
}

public void drawSortIndicatorWithFrame(NSRect cellFrame, NSView controlView, boolean ascending, int /*long*/ priority) {
	OS.objc_msgSend(this.id, OS.sel_drawSortIndicatorWithFrame_inView_ascending_priority_, cellFrame, controlView != null ? controlView.id : 0, ascending, priority);
}

public NSRect sortIndicatorRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sortIndicatorRectForBounds_, theRect);
	return result;
}

}

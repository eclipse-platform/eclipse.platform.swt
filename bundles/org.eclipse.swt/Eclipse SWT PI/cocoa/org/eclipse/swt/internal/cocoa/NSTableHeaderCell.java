/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTableHeaderCell extends NSTextFieldCell {

public NSTableHeaderCell() {
	super();
}

public NSTableHeaderCell(long id) {
	super(id);
}

public NSTableHeaderCell(id id) {
	super(id);
}

public void drawSortIndicatorWithFrame(NSRect cellFrame, NSView controlView, boolean ascending, long priority) {
	OS.objc_msgSend(this.id, OS.sel_drawSortIndicatorWithFrame_inView_ascending_priority_, cellFrame, controlView != null ? controlView.id : 0, ascending, priority);
}

public NSRect sortIndicatorRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sortIndicatorRectForBounds_, theRect);
	return result;
}

}

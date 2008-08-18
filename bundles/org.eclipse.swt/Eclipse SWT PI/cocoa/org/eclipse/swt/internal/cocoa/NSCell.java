/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSCell extends NSObject {

public NSCell() {
	super();
}

public NSCell(int /*long*/ id) {
	super(id);
}

public NSCell(id id) {
	super(id);
}

public void drawInteriorWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawInteriorWithFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
}

public NSRect drawingRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawingRectForBounds_, theRect);
	return result;
}

public void setAllowsMixedState(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_, flag);
}

public void setAttributedStringValue(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringValue_, obj != null ? obj.id : 0);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public void setWraps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWraps_, flag);
}

public NSString title() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public boolean wraps() {
	return OS.objc_msgSend(this.id, OS.sel_wraps) != 0;
}

}

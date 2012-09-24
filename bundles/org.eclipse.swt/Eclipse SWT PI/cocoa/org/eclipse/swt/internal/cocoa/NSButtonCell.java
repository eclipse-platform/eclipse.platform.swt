/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSButtonCell extends NSActionCell {

public NSButtonCell() {
	super();
}

public NSButtonCell(long /*int*/ id) {
	super(id);
}

public NSButtonCell(id id) {
	super(id);
}

public NSColor backgroundColor() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public void drawImage(NSImage image, NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawImage_withFrame_inView_, image != null ? image.id : 0, frame, controlView != null ? controlView.id : 0);
}

public NSRect drawTitle(NSAttributedString title, NSRect frame, NSView controlView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawTitle_withFrame_inView_, title != null ? title.id : 0, frame, controlView != null ? controlView.id : 0);
	return result;
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setButtonType(long /*int*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_, aType);
}

public void setHighlightsBy(long /*int*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightsBy_, aType);
}

public void setImagePosition(long /*int*/ aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_, aPosition);
}

public NSString title() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}

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

public class NSButtonCell extends NSActionCell {

public NSButtonCell() {
	super();
}

public NSButtonCell(int /*long*/ id) {
	super(id);
}

public NSButtonCell(id id) {
	super(id);
}

public void drawImage(NSImage image, NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawImage_withFrame_inView_, image != null ? image.id : 0, frame, controlView != null ? controlView.id : 0);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setButtonType(int /*long*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_, aType);
}

public void setImagePosition(int /*long*/ aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_, aPosition);
}

public NSString title() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}

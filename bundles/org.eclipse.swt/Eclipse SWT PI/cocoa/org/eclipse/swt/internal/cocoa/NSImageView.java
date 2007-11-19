/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSImageView extends NSControl {

public NSImageView() {
	super();
}

public NSImageView(int id) {
	super(id);
}

public boolean allowsCutCopyPaste() {
	return OS.objc_msgSend(this.id, OS.sel_allowsCutCopyPaste) != 0;
}

public boolean animates() {
	return OS.objc_msgSend(this.id, OS.sel_animates) != 0;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public int imageAlignment() {
	return OS.objc_msgSend(this.id, OS.sel_imageAlignment);
}

public int imageFrameStyle() {
	return OS.objc_msgSend(this.id, OS.sel_imageFrameStyle);
}

public int imageScaling() {
	return OS.objc_msgSend(this.id, OS.sel_imageScaling);
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public void setAllowsCutCopyPaste(boolean allow) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsCutCopyPaste_1, allow);
}

public void setAnimates(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAnimates_1, flag);
}

public void setEditable(boolean yn) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, yn);
}

public void setImage(NSImage newImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, newImage != null ? newImage.id : 0);
}

public void setImageAlignment(int newAlign) {
	OS.objc_msgSend(this.id, OS.sel_setImageAlignment_1, newAlign);
}

public void setImageFrameStyle(int newStyle) {
	OS.objc_msgSend(this.id, OS.sel_setImageFrameStyle_1, newStyle);
}

public void setImageScaling(int newScaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_1, newScaling);
}

}

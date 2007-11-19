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

public class NSColorPicker extends NSObject {

public NSColorPicker() {
	super();
}

public NSColorPicker(int id) {
	super(id);
}

public void attachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_attachColorList_1, colorList != null ? colorList.id : 0);
}

public NSString buttonToolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_buttonToolTip);
	return result != 0 ? new NSString(result) : null;
}

public NSColorPanel colorPanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorPanel);
	return result != 0 ? new NSColorPanel(result) : null;
}

public void detachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_detachColorList_1, colorList != null ? colorList.id : 0);
}

public NSColorPicker initWithPickerMask(int mask, NSColorPanel owningColorPanel) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPickerMask_1colorPanel_1, mask, owningColorPanel != null ? owningColorPanel.id : 0);
	return result != 0 ? this : null;
}

public void insertNewButtonImage(NSImage newButtonImage, NSButtonCell buttonCell) {
	OS.objc_msgSend(this.id, OS.sel_insertNewButtonImage_1in_1, newButtonImage != null ? newButtonImage.id : 0, buttonCell != null ? buttonCell.id : 0);
}

public NSSize minContentSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minContentSize);
	return result;
}

public NSImage provideNewButtonImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_provideNewButtonImage);
	return result != 0 ? new NSImage(result) : null;
}

public void setMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setMode_1, mode);
}

public void viewSizeChanged(id sender) {
	OS.objc_msgSend(this.id, OS.sel_viewSizeChanged_1, sender != null ? sender.id : 0);
}

}

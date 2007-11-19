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

public class NSColorPanel extends NSPanel {

public NSColorPanel() {
	super();
}

public NSColorPanel(int id) {
	super(id);
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public float alpha() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alpha);
}

public void attachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_attachColorList_1, colorList != null ? colorList.id : 0);
}

public NSColor color() {
	int result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void detachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_detachColorList_1, colorList != null ? colorList.id : 0);
}

public static boolean dragColor(NSColor color, NSEvent theEvent, NSView sourceView) {
	return OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_dragColor_1withEvent_1fromView_1, color != null ? color.id : 0, theEvent != null ? theEvent.id : 0, sourceView != null ? sourceView.id : 0) != 0;
}

public boolean isContinuous() {
	return OS.objc_msgSend(this.id, OS.sel_isContinuous) != 0;
}

public int mode() {
	return OS.objc_msgSend(this.id, OS.sel_mode);
}

public void setAccessoryView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, aView != null ? aView.id : 0);
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_1, color != null ? color.id : 0);
}

public void setContinuous(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContinuous_1, flag);
}

public void setMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setMode_1, mode);
}

public static void setPickerMask(int mask) {
	OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_setPickerMask_1, mask);
}

public static void setPickerMode(int mode) {
	OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_setPickerMode_1, mode);
}

public void setShowsAlpha(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsAlpha_1, flag);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, anObject != null ? anObject.id : 0);
}

public static NSColorPanel sharedColorPanel() {
	int result = OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_sharedColorPanel);
	return result != 0 ? new NSColorPanel(result) : null;
}

public static boolean sharedColorPanelExists() {
	return OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_sharedColorPanelExists) != 0;
}

public boolean showsAlpha() {
	return OS.objc_msgSend(this.id, OS.sel_showsAlpha) != 0;
}

}

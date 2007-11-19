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

public class NSStatusItem extends NSObject {

public NSStatusItem() {
	super();
}

public NSStatusItem(int id) {
	super(id);
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSImage alternateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSAttributedString attributedTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public int doubleAction() {
	return OS.objc_msgSend(this.id, OS.sel_doubleAction);
}

public void drawStatusBarBackgroundInRect(NSRect rect, boolean highlight) {
	OS.objc_msgSend(this.id, OS.sel_drawStatusBarBackgroundInRect_1withHighlight_1, rect, highlight);
}

public boolean highlightMode() {
	return OS.objc_msgSend(this.id, OS.sel_highlightMode) != 0;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public float length() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_length);
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public void popUpStatusItemMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_popUpStatusItemMenu_1, menu != null ? menu.id : 0);
}

public int sendActionOn(int mask) {
	return OS.objc_msgSend(this.id, OS.sel_sendActionOn_1, mask);
}

public void setAction(int action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, action);
}

public void setAlternateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateImage_1, image != null ? image.id : 0);
}

public void setAttributedTitle(NSAttributedString title) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_1, title != null ? title.id : 0);
}

public void setDoubleAction(int action) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleAction_1, action);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, enabled);
}

public void setHighlightMode(boolean highlightMode) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightMode_1, highlightMode);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setLength(float length) {
	OS.objc_msgSend(this.id, OS.sel_setLength_1, length);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, menu != null ? menu.id : 0);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, target != null ? target.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, title != null ? title.id : 0);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1, toolTip != null ? toolTip.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public NSStatusBar statusBar() {
	int result = OS.objc_msgSend(this.id, OS.sel_statusBar);
	return result != 0 ? new NSStatusBar(result) : null;
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSString toolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTip);
	return result != 0 ? new NSString(result) : null;
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}

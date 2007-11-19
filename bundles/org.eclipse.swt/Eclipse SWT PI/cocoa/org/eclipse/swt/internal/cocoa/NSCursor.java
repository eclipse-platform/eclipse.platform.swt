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

public class NSCursor extends NSObject {

public NSCursor() {
	super();
}

public NSCursor(int id) {
	super(id);
}

public static NSCursor IBeamCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_IBeamCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor arrowCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_arrowCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor closedHandCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_closedHandCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor crosshairCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_crosshairCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor currentCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_currentCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor disappearingItemCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_disappearingItemCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static void hide() {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_hide);
}

public NSPoint hotSpot() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_hotSpot);
	return result;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSCursor initWithImage_foregroundColorHint_backgroundColorHint_hotSpot_(NSImage newImage, NSColor fg, NSColor bg, NSPoint hotSpot) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithImage_1foregroundColorHint_1backgroundColorHint_1hotSpot_1, newImage != null ? newImage.id : 0, fg != null ? fg.id : 0, bg != null ? bg.id : 0, hotSpot);
	return result != 0 ? this : null;
}

public NSCursor initWithImage_hotSpot_(NSImage newImage, NSPoint aPoint) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithImage_1hotSpot_1, newImage != null ? newImage.id : 0, aPoint);
	return result != 0 ? this : null;
}

public boolean isSetOnMouseEntered() {
	return OS.objc_msgSend(this.id, OS.sel_isSetOnMouseEntered) != 0;
}

public boolean isSetOnMouseExited() {
	return OS.objc_msgSend(this.id, OS.sel_isSetOnMouseExited) != 0;
}

public void mouseEntered(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_1, theEvent != null ? theEvent.id : 0);
}

public void mouseExited(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_1, theEvent != null ? theEvent.id : 0);
}

public static NSCursor openHandCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_openHandCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor pointingHandCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_pointingHandCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public void pop() {
	OS.objc_msgSend(this.id, OS.sel_pop);
}

public static void static_pop() {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_pop);
}

public void push() {
	OS.objc_msgSend(this.id, OS.sel_push);
}

public static NSCursor resizeDownCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeDownCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeLeftCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeLeftRightCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftRightCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeRightCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeRightCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeUpCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeUpDownCursor() {
	int result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpDownCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public static void setHiddenUntilMouseMoves(boolean flag) {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_setHiddenUntilMouseMoves_1, flag);
}

public void setOnMouseEntered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setOnMouseEntered_1, flag);
}

public void setOnMouseExited(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setOnMouseExited_1, flag);
}

public static void unhide() {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_unhide);
}

}

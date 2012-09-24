/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSCursor extends NSObject {

public NSCursor() {
	super();
}

public NSCursor(long /*int*/ id) {
	super(id);
}

public NSCursor(id id) {
	super(id);
}

public static NSCursor IBeamCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_IBeamCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor arrowCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_arrowCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor crosshairCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_crosshairCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor currentCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_currentCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public NSPoint hotSpot() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_hotSpot);
	return result;
}

public NSCursor initWithImage(NSImage newImage, NSPoint aPoint) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithImage_hotSpot_, newImage != null ? newImage.id : 0, aPoint);
	return result == this.id ? this : (result != 0 ? new NSCursor(result) : null);
}

public static NSCursor pointingHandCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_pointingHandCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static void pop() {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_pop);
}

public void push() {
	OS.objc_msgSend(this.id, OS.sel_push);
}

public static NSCursor resizeDownCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeDownCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeLeftCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeLeftRightCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeLeftRightCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeRightCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeRightCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeUpCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public static NSCursor resizeUpDownCursor() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCursor, OS.sel_resizeUpDownCursor);
	return result != 0 ? new NSCursor(result) : null;
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public static void setHiddenUntilMouseMoves(boolean flag) {
	OS.objc_msgSend(OS.class_NSCursor, OS.sel_setHiddenUntilMouseMoves_, flag);
}

public void setOnMouseEntered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setOnMouseEntered_, flag);
}

}

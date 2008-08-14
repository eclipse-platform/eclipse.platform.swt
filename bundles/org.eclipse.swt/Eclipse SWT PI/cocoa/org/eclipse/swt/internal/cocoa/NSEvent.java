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

public class NSEvent extends NSObject {

public NSEvent() {
	super();
}

public NSEvent(int /*long*/ id) {
	super(id);
}

public NSEvent(id id) {
	super(id);
}

public int buttonNumber() {
	return OS.objc_msgSend(this.id, OS.sel_buttonNumber);
}

public NSString characters() {
	int result = OS.objc_msgSend(this.id, OS.sel_characters);
	return result != 0 ? new NSString(result) : null;
}

public NSString charactersIgnoringModifiers() {
	int result = OS.objc_msgSend(this.id, OS.sel_charactersIgnoringModifiers);
	return result != 0 ? new NSString(result) : null;
}

public int clickCount() {
	return OS.objc_msgSend(this.id, OS.sel_clickCount);
}

public float deltaX() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_deltaX);
}

public float deltaY() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_deltaY);
}

public short keyCode() {
	return (short)OS.objc_msgSend(this.id, OS.sel_keyCode);
}

public NSPoint locationInWindow() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationInWindow);
	return result;
}

public int modifierFlags() {
	return OS.objc_msgSend(this.id, OS.sel_modifierFlags);
}

public static NSPoint mouseLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, OS.class_NSEvent, OS.sel_mouseLocation);
	return result;
}

public static NSEvent otherEventWithType(int type, NSPoint location, int flags, double time, int wNum, NSGraphicsContext context, short subtype, int d1, int d2) {
	int result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_, type, location, flags, time, wNum, context != null ? context.id : 0, subtype, d1, d2);
	return result != 0 ? new NSEvent(result) : null;
}

public int type() {
	return OS.objc_msgSend(this.id, OS.sel_type);
}

public NSWindow window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}

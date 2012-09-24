/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

public NSEvent(long /*int*/ id) {
	super(id);
}

public NSEvent(id id) {
	super(id);
}

public long /*int*/ CGEvent() {
	return OS.objc_msgSend(this.id, OS.sel_CGEvent);
}

public long /*int*/ buttonNumber() {
	return OS.objc_msgSend(this.id, OS.sel_buttonNumber);
}

public NSString characters() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_characters);
	return result != 0 ? new NSString(result) : null;
}

public NSString charactersIgnoringModifiers() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_charactersIgnoringModifiers);
	return result != 0 ? new NSString(result) : null;
}

public long /*int*/ clickCount() {
	return OS.objc_msgSend(this.id, OS.sel_clickCount);
}

public double /*float*/ deltaX() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_deltaX);
}

public double /*float*/ deltaY() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_deltaY);
}

public static NSEvent enterExitEventWithType(long /*int*/ type, NSPoint location, long /*int*/ flags, double time, long /*int*/ wNum, NSGraphicsContext context, long /*int*/ eNum, long /*int*/ tNum, long /*int*/ data) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_enterExitEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_trackingNumber_userData_, type, location, flags, time, wNum, context != null ? context.id : 0, eNum, tNum, data);
	return result != 0 ? new NSEvent(result) : null;
}

public short keyCode() {
	return (short)OS.objc_msgSend(this.id, OS.sel_keyCode);
}

public NSPoint locationInWindow() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationInWindow);
	return result;
}

public double /*float*/ magnification() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_magnification);
}

public long /*int*/ modifierFlags() {
	return OS.objc_msgSend(this.id, OS.sel_modifierFlags);
}

public static NSPoint mouseLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, OS.class_NSEvent, OS.sel_mouseLocation);
	return result;
}

public static NSEvent otherEventWithType(long /*int*/ type, NSPoint location, long /*int*/ flags, double time, long /*int*/ wNum, NSGraphicsContext context, short subtype, long /*int*/ d1, long /*int*/ d2) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_, type, location, flags, time, wNum, context != null ? context.id : 0, subtype, d1, d2);
	return result != 0 ? new NSEvent(result) : null;
}

public float rotation() {
	return OS.objc_msgSend_floatret(this.id, OS.sel_rotation);
}

public double timestamp() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timestamp);
}

public NSSet touchesMatchingPhase(long /*int*/ phase, NSView view) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_touchesMatchingPhase_inView_, phase, view != null ? view.id : 0);
	return result != 0 ? new NSSet(result) : null;
}

public long /*int*/ type() {
	return OS.objc_msgSend(this.id, OS.sel_type);
}

public NSWindow window() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}

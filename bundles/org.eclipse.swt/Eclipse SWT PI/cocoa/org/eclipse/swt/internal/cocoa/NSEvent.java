/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSEvent extends NSObject {

public NSEvent() {
	super();
}

public NSEvent(long id) {
	super(id);
}

public NSEvent(id id) {
	super(id);
}

public long CGEvent() {
	return OS.objc_msgSend(this.id, OS.sel_CGEvent);
}

public long buttonNumber() {
	return OS.objc_msgSend(this.id, OS.sel_buttonNumber);
}

public NSString characters() {
	long result = OS.objc_msgSend(this.id, OS.sel_characters);
	return result != 0 ? new NSString(result) : null;
}

public NSString charactersIgnoringModifiers() {
	long result = OS.objc_msgSend(this.id, OS.sel_charactersIgnoringModifiers);
	return result != 0 ? new NSString(result) : null;
}

public long clickCount() {
	return OS.objc_msgSend(this.id, OS.sel_clickCount);
}

public double deltaX() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_deltaX);
}

public double deltaY() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_deltaY);
}

public static double doubleClickInterval() {
	return OS.objc_msgSend_fpret(OS.class_NSEvent, OS.sel_doubleClickInterval);
}

public static NSEvent enterExitEventWithType(long type, NSPoint location, long flags, double time, long wNum, NSGraphicsContext context, long eNum, long tNum, long data) {
	long result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_enterExitEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_trackingNumber_userData_, type, location, flags, time, wNum, context != null ? context.id : 0, eNum, tNum, data);
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

public double magnification() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_magnification);
}

public long modifierFlags() {
	return OS.objc_msgSend(this.id, OS.sel_modifierFlags);
}

public static NSPoint mouseLocation() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, OS.class_NSEvent, OS.sel_mouseLocation);
	return result;
}

public static NSEvent otherEventWithType(long type, NSPoint location, long flags, double time, long wNum, NSGraphicsContext context, short subtype, long d1, long d2) {
	long result = OS.objc_msgSend(OS.class_NSEvent, OS.sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_, type, location, flags, time, wNum, context != null ? context.id : 0, subtype, d1, d2);
	return result != 0 ? new NSEvent(result) : null;
}

public long phase() {
	return OS.objc_msgSend(this.id, OS.sel_phase);
}

public static long pressedMouseButtons() {
	return OS.objc_msgSend(OS.class_NSEvent, OS.sel_pressedMouseButtons);
}

public float rotation() {
	return OS.objc_msgSend_floatret(this.id, OS.sel_rotation);
}

public double timestamp() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timestamp);
}

public NSSet touchesMatchingPhase(long phase, NSView view) {
	long result = OS.objc_msgSend(this.id, OS.sel_touchesMatchingPhase_inView_, phase, view != null ? view.id : 0);
	return result != 0 ? new NSSet(result) : null;
}

public long type() {
	return OS.objc_msgSend(this.id, OS.sel_type);
}

public NSWindow window() {
	long result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSNumber extends NSValue {

public NSNumber() {
	super();
}

public NSNumber(long id) {
	super(id);
}

public NSNumber(id id) {
	super(id);
}

public boolean boolValue() {
	return OS.objc_msgSend_bool(this.id, OS.sel_boolValue);
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public float floatValue() {
	return OS.objc_msgSend_floatret(this.id, OS.sel_floatValue);
}

public int intValue() {
	return (int)OS.objc_msgSend(this.id, OS.sel_intValue);
}

public long integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public static NSNumber numberWithBool(boolean value) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithBool_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithDouble(double value) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithDouble_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInt(int value) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithInt_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSNumber numberWithInteger(long value) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_numberWithInteger_, value);
	return result != 0 ? new NSNumber(result) : null;
}

public static NSValue valueWithPoint(NSPoint point) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_valueWithPoint_, point);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRange(NSRange range) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_valueWithRange_, range);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithRect(NSRect rect) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_valueWithRect_, rect);
	return result != 0 ? new NSValue(result) : null;
}

public static NSValue valueWithSize(NSSize size) {
	long result = OS.objc_msgSend(OS.class_NSNumber, OS.sel_valueWithSize_, size);
	return result != 0 ? new NSValue(result) : null;
}

}

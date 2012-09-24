/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSNumberFormatter extends NSFormatter {

public NSNumberFormatter() {
	super();
}

public NSNumberFormatter(long /*int*/ id) {
	super(id);
}

public NSNumberFormatter(id id) {
	super(id);
}

public boolean allowsFloats() {
	return OS.objc_msgSend_bool(this.id, OS.sel_allowsFloats);
}

public boolean alwaysShowsDecimalSeparator() {
	return OS.objc_msgSend_bool(this.id, OS.sel_alwaysShowsDecimalSeparator);
}

public NSString decimalSeparator() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_decimalSeparator);
	return result != 0 ? new NSString(result) : null;
}

public NSNumber maximum() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_maximum);
	return result != 0 ? new NSNumber(result) : null;
}

public long /*int*/ maximumFractionDigits() {
	return OS.objc_msgSend(this.id, OS.sel_maximumFractionDigits);
}

public long /*int*/ maximumIntegerDigits() {
	return OS.objc_msgSend(this.id, OS.sel_maximumIntegerDigits);
}

public NSNumber minimum() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_minimum);
	return result != 0 ? new NSNumber(result) : null;
}

public void setAllowsFloats(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsFloats_, flag);
}

public void setMaximum(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximum_, number != null ? number.id : 0);
}

public void setMaximumFractionDigits(long /*int*/ number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumFractionDigits_, number);
}

public void setMaximumIntegerDigits(long /*int*/ number) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumIntegerDigits_, number);
}

public void setMinimum(NSNumber number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimum_, number != null ? number.id : 0);
}

public void setMinimumFractionDigits(long /*int*/ number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumFractionDigits_, number);
}

public void setMinimumIntegerDigits(long /*int*/ number) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumIntegerDigits_, number);
}

public void setNumberStyle(long /*int*/ style) {
	OS.objc_msgSend(this.id, OS.sel_setNumberStyle_, style);
}

public void setPartialStringValidationEnabled(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setPartialStringValidationEnabled_, b);
}

}

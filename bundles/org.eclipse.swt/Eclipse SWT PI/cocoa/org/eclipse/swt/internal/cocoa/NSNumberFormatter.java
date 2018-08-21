/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public void setAllowsFloats(boolean allowsFloats) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsFloats_, allowsFloats);
}

public void setMaximum(NSNumber maximum) {
	OS.objc_msgSend(this.id, OS.sel_setMaximum_, maximum != null ? maximum.id : 0);
}

public void setMaximumFractionDigits(long /*int*/ maximumFractionDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumFractionDigits_, maximumFractionDigits);
}

public void setMaximumIntegerDigits(long /*int*/ maximumIntegerDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumIntegerDigits_, maximumIntegerDigits);
}

public void setMinimum(NSNumber minimum) {
	OS.objc_msgSend(this.id, OS.sel_setMinimum_, minimum != null ? minimum.id : 0);
}

public void setMinimumFractionDigits(long /*int*/ minimumFractionDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumFractionDigits_, minimumFractionDigits);
}

public void setMinimumIntegerDigits(long /*int*/ minimumIntegerDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumIntegerDigits_, minimumIntegerDigits);
}

public void setNumberStyle(long /*int*/ numberStyle) {
	OS.objc_msgSend(this.id, OS.sel_setNumberStyle_, numberStyle);
}

public void setPartialStringValidationEnabled(boolean partialStringValidationEnabled) {
	OS.objc_msgSend(this.id, OS.sel_setPartialStringValidationEnabled_, partialStringValidationEnabled);
}

}

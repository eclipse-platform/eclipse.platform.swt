/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public NSNumberFormatter(long id) {
	super(id);
}

public NSNumberFormatter(id id) {
	super(id);
}

public NSString decimalSeparator() {
	long result = OS.objc_msgSend(this.id, OS.sel_decimalSeparator);
	return result != 0 ? new NSString(result) : null;
}

public void setAllowsFloats(boolean allowsFloats) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsFloats_, allowsFloats);
}

public void setMaximum(NSNumber maximum) {
	OS.objc_msgSend(this.id, OS.sel_setMaximum_, maximum != null ? maximum.id : 0);
}

public void setMaximumFractionDigits(long maximumFractionDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumFractionDigits_, maximumFractionDigits);
}

public void setMaximumIntegerDigits(long maximumIntegerDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumIntegerDigits_, maximumIntegerDigits);
}

public void setMinimum(NSNumber minimum) {
	OS.objc_msgSend(this.id, OS.sel_setMinimum_, minimum != null ? minimum.id : 0);
}

public void setMinimumFractionDigits(long minimumFractionDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumFractionDigits_, minimumFractionDigits);
}

public void setMinimumIntegerDigits(long minimumIntegerDigits) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumIntegerDigits_, minimumIntegerDigits);
}

public void setNumberStyle(long numberStyle) {
	OS.objc_msgSend(this.id, OS.sel_setNumberStyle_, numberStyle);
}

public void setPartialStringValidationEnabled(boolean partialStringValidationEnabled) {
	OS.objc_msgSend(this.id, OS.sel_setPartialStringValidationEnabled_, partialStringValidationEnabled);
}

}

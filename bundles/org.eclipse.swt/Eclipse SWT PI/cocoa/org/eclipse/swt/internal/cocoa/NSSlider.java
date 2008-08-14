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

public class NSSlider extends NSControl {

public NSSlider() {
	super();
}

public NSSlider(int /*long*/ id) {
	super(id);
}

public NSSlider(id id) {
	super(id);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setMaxValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_, aDouble);
}

public void setMinValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_, aDouble);
}

}

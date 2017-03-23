/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public NSSlider(long /*int*/ id) {
	super(id);
}

public NSSlider(id id) {
	super(id);
}

public double /*float*/ knobThickness() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_knobThickness);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setMaxValue(double maxValue) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_, maxValue);
}

public void setMinValue(double minValue) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_, minValue);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSSlider, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSSlider, OS.sel_setCellClass_, factoryId);
}

}

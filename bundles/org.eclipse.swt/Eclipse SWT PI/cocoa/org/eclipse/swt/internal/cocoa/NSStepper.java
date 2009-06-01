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

public class NSStepper extends NSControl {

public NSStepper() {
	super();
}

public NSStepper(int /*long*/ id) {
	super(id);
}

public NSStepper(id id) {
	super(id);
}

public double increment() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_increment);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setIncrement(double increment) {
	OS.objc_msgSend(this.id, OS.sel_setIncrement_, increment);
}

public void setMaxValue(double maxValue) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_, maxValue);
}

public void setMinValue(double minValue) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_, minValue);
}

public void setValueWraps(boolean valueWraps) {
	OS.objc_msgSend(this.id, OS.sel_setValueWraps_, valueWraps);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSStepper, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSStepper, OS.sel_setCellClass_, factoryId);
}

}

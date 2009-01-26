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

public class NSProgressIndicator extends NSView {

public NSProgressIndicator() {
	super();
}

public NSProgressIndicator(int /*long*/ id) {
	super(id);
}

public NSProgressIndicator(id id) {
	super(id);
}

public int /*long*/ controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public void setControlSize(int /*long*/ size) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, size);
}

public void setDoubleValue(double doubleValue) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_, doubleValue);
}

public void setIndeterminate(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIndeterminate_, flag);
}

public void setMaxValue(double newMaximum) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_, newMaximum);
}

public void setMinValue(double newMinimum) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_, newMinimum);
}

public void sizeToFit() {
	OS.objc_msgSend(this.id, OS.sel_sizeToFit);
}

public void startAnimation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_startAnimation_, sender != null ? sender.id : 0);
}

public void stopAnimation(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopAnimation_, sender != null ? sender.id : 0);
}

}

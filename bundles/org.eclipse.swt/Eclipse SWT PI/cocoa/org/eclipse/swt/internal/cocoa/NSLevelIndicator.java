/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSLevelIndicator extends NSControl {

public NSLevelIndicator() {
	super();
}

public NSLevelIndicator(int id) {
	super(id);
}

public double criticalValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_criticalValue);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public int numberOfMajorTickMarks() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfMajorTickMarks);
}

public int numberOfTickMarks() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfTickMarks);
}

public NSRect rectOfTickMarkAtIndex(int index) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfTickMarkAtIndex_1, index);
	return result;
}

public void setCriticalValue(double criticalValue) {
	OS.objc_msgSend(this.id, OS.sel_setCriticalValue_1, criticalValue);
}

public void setMaxValue(double maxValue) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_1, maxValue);
}

public void setMinValue(double minValue) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_1, minValue);
}

public void setNumberOfMajorTickMarks(int count) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfMajorTickMarks_1, count);
}

public void setNumberOfTickMarks(int count) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfTickMarks_1, count);
}

public void setTickMarkPosition(int position) {
	OS.objc_msgSend(this.id, OS.sel_setTickMarkPosition_1, position);
}

public void setWarningValue(double warningValue) {
	OS.objc_msgSend(this.id, OS.sel_setWarningValue_1, warningValue);
}

public int tickMarkPosition() {
	return OS.objc_msgSend(this.id, OS.sel_tickMarkPosition);
}

public double tickMarkValueAtIndex(int index) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_tickMarkValueAtIndex_1, index);
}

public double warningValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_warningValue);
}

}

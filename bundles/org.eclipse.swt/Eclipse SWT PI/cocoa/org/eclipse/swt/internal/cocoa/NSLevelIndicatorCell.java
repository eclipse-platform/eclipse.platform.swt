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

public class NSLevelIndicatorCell extends NSActionCell {

public NSLevelIndicatorCell() {
	super();
}

public NSLevelIndicatorCell(int id) {
	super(id);
}

public double criticalValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_criticalValue);
}

public id initWithLevelIndicatorStyle(int levelIndicatorStyle) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLevelIndicatorStyle_1, levelIndicatorStyle);
	return result != 0 ? new id(result) : null;
}

public int levelIndicatorStyle() {
	return OS.objc_msgSend(this.id, OS.sel_levelIndicatorStyle);
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

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setLevelIndicatorStyle(int levelIndicatorStyle) {
	OS.objc_msgSend(this.id, OS.sel_setLevelIndicatorStyle_1, levelIndicatorStyle);
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

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

public class NSSlider extends NSControl {

public NSSlider() {
	super();
}

public NSSlider(int id) {
	super(id);
}

public boolean acceptsFirstMouse(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstMouse_1, theEvent != null ? theEvent.id : 0) != 0;
}

public boolean allowsTickMarkValuesOnly() {
	return OS.objc_msgSend(this.id, OS.sel_allowsTickMarkValuesOnly) != 0;
}

public double altIncrementValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_altIncrementValue);
}

public double closestTickMarkValueToValue(double value) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_closestTickMarkValueToValue_1, value);
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public int indexOfTickMarkAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfTickMarkAtPoint_1, point);
}

public int isVertical() {
	return OS.objc_msgSend(this.id, OS.sel_isVertical);
}

public float knobThickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_knobThickness);
}

public double maxValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_maxValue);
}

public double minValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_minValue);
}

public int numberOfTickMarks() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfTickMarks);
}

public NSRect rectOfTickMarkAtIndex(int index) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectOfTickMarkAtIndex_1, index);
	return result;
}

public void setAllowsTickMarkValuesOnly(boolean yorn) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsTickMarkValuesOnly_1, yorn);
}

public void setAltIncrementValue(double incValue) {
	OS.objc_msgSend(this.id, OS.sel_setAltIncrementValue_1, incValue);
}

public void setImage(NSImage backgroundImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, backgroundImage != null ? backgroundImage.id : 0);
}

public void setKnobThickness(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setKnobThickness_1, aFloat);
}

public void setMaxValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setMaxValue_1, aDouble);
}

public void setMinValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setMinValue_1, aDouble);
}

public void setNumberOfTickMarks(int count) {
	OS.objc_msgSend(this.id, OS.sel_setNumberOfTickMarks_1, count);
}

public void setTickMarkPosition(int position) {
	OS.objc_msgSend(this.id, OS.sel_setTickMarkPosition_1, position);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleCell(NSCell aCell) {
	OS.objc_msgSend(this.id, OS.sel_setTitleCell_1, aCell != null ? aCell.id : 0);
}

public void setTitleColor(NSColor newColor) {
	OS.objc_msgSend(this.id, OS.sel_setTitleColor_1, newColor != null ? newColor.id : 0);
}

public void setTitleFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setTitleFont_1, fontObj != null ? fontObj.id : 0);
}

public int tickMarkPosition() {
	return OS.objc_msgSend(this.id, OS.sel_tickMarkPosition);
}

public double tickMarkValueAtIndex(int index) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_tickMarkValueAtIndex_1, index);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public id titleCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleCell);
	return result != 0 ? new id(result) : null;
}

public NSColor titleColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSFont titleFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_titleFont);
	return result != 0 ? new NSFont(result) : null;
}

}

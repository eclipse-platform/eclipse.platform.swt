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

public class NSScroller extends NSControl {

public NSScroller() {
	super();
}

public NSScroller(int id) {
	super(id);
}

public int arrowsPosition() {
	return OS.objc_msgSend(this.id, OS.sel_arrowsPosition);
}

public void checkSpaceForParts() {
	OS.objc_msgSend(this.id, OS.sel_checkSpaceForParts);
}

public int controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int controlTint() {
	return OS.objc_msgSend(this.id, OS.sel_controlTint);
}

public void drawArrow(int whichArrow, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_drawArrow_1highlight_1, whichArrow, flag);
}

public void drawKnob() {
	OS.objc_msgSend(this.id, OS.sel_drawKnob);
}

public void drawKnobSlotInRect(NSRect slotRect, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_drawKnobSlotInRect_1highlight_1, slotRect, flag);
}

public void drawParts() {
	OS.objc_msgSend(this.id, OS.sel_drawParts);
}

public void highlight(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_highlight_1, flag);
}

public int hitPart() {
	return OS.objc_msgSend(this.id, OS.sel_hitPart);
}

public float knobProportion() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_knobProportion);
}

public NSRect rectForPart(int partCode) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForPart_1, partCode);
	return result;
}

public static float scrollerWidth() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidth);
}

public static float scrollerWidthForControlSize(int controlSize) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidthForControlSize_1, controlSize);
}

public void setArrowsPosition(int where) {
	OS.objc_msgSend(this.id, OS.sel_setArrowsPosition_1, where);
}

public void setControlSize(int controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, controlSize);
}

public void setControlTint(int controlTint) {
	OS.objc_msgSend(this.id, OS.sel_setControlTint_1, controlTint);
}

public void setFloatValue(float aFloat, float proportion) {
	OS.objc_msgSend(this.id, OS.sel_setFloatValue_1knobProportion_1, aFloat, proportion);
}

public void setKnobProportion(float proportion) {
	OS.objc_msgSend(this.id, OS.sel_setKnobProportion_1, proportion);
}

public int testPart(NSPoint thePoint) {
	return OS.objc_msgSend(this.id, OS.sel_testPart_1, thePoint);
}

public void trackKnob(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_trackKnob_1, theEvent != null ? theEvent.id : 0);
}

public void trackScrollButtons(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_trackScrollButtons_1, theEvent != null ? theEvent.id : 0);
}

public int usableParts() {
	return OS.objc_msgSend(this.id, OS.sel_usableParts);
}

}

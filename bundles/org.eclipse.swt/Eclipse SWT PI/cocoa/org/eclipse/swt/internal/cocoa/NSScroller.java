/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSScroller extends NSControl {

public NSScroller() {
	super();
}

public NSScroller(int /*long*/ id) {
	super(id);
}

public NSScroller(id id) {
	super(id);
}

public int /*long*/ controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int /*long*/ hitPart() {
	return OS.objc_msgSend(this.id, OS.sel_hitPart);
}

public float /*double*/ knobProportion() {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_knobProportion);
}

public NSRect rectForPart(int /*long*/ partCode) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForPart_, partCode);
	return result;
}

public static float /*double*/ scrollerWidth() {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidth);
}

public static float /*double*/ scrollerWidthForControlSize(int /*long*/ controlSize) {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidthForControlSize_, controlSize);
}

public void setControlSize(int /*long*/ controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setKnobProportion(float /*double*/ proportion) {
	OS.objc_msgSend(this.id, OS.sel_setKnobProportion_, proportion);
}

public int /*long*/ testPart(NSPoint thePoint) {
	return OS.objc_msgSend(this.id, OS.sel_testPart_, thePoint);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSScroller, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSScroller, OS.sel_setCellClass_, factoryId);
}

}

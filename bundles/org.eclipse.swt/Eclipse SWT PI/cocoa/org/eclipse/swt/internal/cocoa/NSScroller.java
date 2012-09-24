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

public NSScroller(long /*int*/ id) {
	super(id);
}

public NSScroller(id id) {
	super(id);
}

public long /*int*/ controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public long /*int*/ hitPart() {
	return OS.objc_msgSend(this.id, OS.sel_hitPart);
}

public double /*float*/ knobProportion() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_knobProportion);
}

public NSRect rectForPart(long /*int*/ partCode) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForPart_, partCode);
	return result;
}

public static double /*float*/ scrollerWidth() {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidth);
}

public static double /*float*/ scrollerWidthForControlSize(long /*int*/ controlSize) {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidthForControlSize_, controlSize);
}

public void setControlSize(long /*int*/ controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setKnobProportion(double /*float*/ proportion) {
	OS.objc_msgSend(this.id, OS.sel_setKnobProportion_, proportion);
}

public long /*int*/ testPart(NSPoint thePoint) {
	return OS.objc_msgSend(this.id, OS.sel_testPart_, thePoint);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSScroller, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSScroller, OS.sel_setCellClass_, factoryId);
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSScroller extends NSControl {

public NSScroller() {
	super();
}

public NSScroller(long id) {
	super(id);
}

public NSScroller(id id) {
	super(id);
}

public long controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public long hitPart() {
	return OS.objc_msgSend(this.id, OS.sel_hitPart);
}

public static boolean isCompatibleWithOverlayScrollers() {
	return OS.objc_msgSend_bool(OS.class_NSScroller, OS.sel_isCompatibleWithOverlayScrollers);
}

public double knobProportion() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_knobProportion);
}

public NSRect rectForPart(long partCode) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForPart_, partCode);
	return result;
}

public static double scrollerWidthForControlSize(long controlSize) {
	return OS.objc_msgSend_fpret(OS.class_NSScroller, OS.sel_scrollerWidthForControlSize_, controlSize);
}

public void setControlSize(long controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setKnobProportion(double proportion) {
	OS.objc_msgSend(this.id, OS.sel_setKnobProportion_, proportion);
}

public long testPart(NSPoint thePoint) {
	return OS.objc_msgSend(this.id, OS.sel_testPart_, thePoint);
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSScroller, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSScroller, OS.sel_setCellClass_, factoryId);
}

}

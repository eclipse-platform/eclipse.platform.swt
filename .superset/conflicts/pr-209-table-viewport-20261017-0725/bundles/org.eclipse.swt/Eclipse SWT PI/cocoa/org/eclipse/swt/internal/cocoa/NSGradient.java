/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSGradient extends NSObject {

public NSGradient() {
	super();
}

public NSGradient(long id) {
	super(id);
}

public NSGradient(id id) {
	super(id);
}

public void drawFromPoint(NSPoint startingPoint, NSPoint endingPoint, long options) {
	OS.objc_msgSend(this.id, OS.sel_drawFromPoint_toPoint_options_, startingPoint, endingPoint, options);
}

public void drawInBezierPath(NSBezierPath path, double angle) {
	OS.objc_msgSend(this.id, OS.sel_drawInBezierPath_angle_, path != null ? path.id : 0, angle);
}

public void drawInRect(NSRect rect, double angle) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_angle_, rect, angle);
}

public NSGradient initWithColors(NSArray colorArray) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithColors_, colorArray != null ? colorArray.id : 0);
	return result == this.id ? this : (result != 0 ? new NSGradient(result) : null);
}

public NSGradient initWithStartingColor(NSColor startingColor, NSColor endingColor) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithStartingColor_endingColor_, startingColor != null ? startingColor.id : 0, endingColor != null ? endingColor.id : 0);
	return result == this.id ? this : (result != 0 ? new NSGradient(result) : null);
}

}

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

public class NSGradient extends NSObject {

public NSGradient() {
	super();
}

public NSGradient(int id) {
	super(id);
}

public NSColorSpace colorSpace() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public void drawFromCenter(NSPoint startCenter, float startRadius, NSPoint endCenter, float endRadius, int options) {
	OS.objc_msgSend(this.id, OS.sel_drawFromCenter_1radius_1toCenter_1radius_1options_1, startCenter, startRadius, endCenter, endRadius, options);
}

public void drawFromPoint(NSPoint startingPoint, NSPoint endingPoint, int options) {
	OS.objc_msgSend(this.id, OS.sel_drawFromPoint_1toPoint_1options_1, startingPoint, endingPoint, options);
}

public void drawInBezierPath_angle_(NSBezierPath path, float angle) {
	OS.objc_msgSend(this.id, OS.sel_drawInBezierPath_1angle_1, path != null ? path.id : 0, angle);
}

public void drawInBezierPath_relativeCenterPosition_(NSBezierPath path, NSPoint relativeCenterPosition) {
	OS.objc_msgSend(this.id, OS.sel_drawInBezierPath_1relativeCenterPosition_1, path != null ? path.id : 0, relativeCenterPosition);
}

public void drawInRect_angle_(NSRect rect, float angle) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_1angle_1, rect, angle);
}

public void drawInRect_relativeCenterPosition_(NSRect rect, NSPoint relativeCenterPosition) {
	OS.objc_msgSend(this.id, OS.sel_drawInRect_1relativeCenterPosition_1, rect, relativeCenterPosition);
}

public void getColor(int color, int location, int index) {
	OS.objc_msgSend(this.id, OS.sel_getColor_1location_1atIndex_1, color, location, index);
}

public NSGradient initWithColors_(NSArray colorArray) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColors_1, colorArray != null ? colorArray.id : 0);
	return result != 0 ? this : null;
}

public NSGradient initWithColors_atLocations_colorSpace_(NSArray colorArray, int locations, NSColorSpace colorSpace) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColors_1atLocations_1colorSpace_1, colorArray != null ? colorArray.id : 0, locations, colorSpace != null ? colorSpace.id : 0);
	return result != 0 ? this : null;
}

public NSGradient initWithColorsAndLocations(NSColor initWithColorsAndLocations) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithColorsAndLocations_1, initWithColorsAndLocations != null ? initWithColorsAndLocations.id : 0);
	return result != 0 ? this : null;
}

public NSGradient initWithStartingColor(NSColor startingColor, NSColor endingColor) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithStartingColor_1endingColor_1, startingColor != null ? startingColor.id : 0, endingColor != null ? endingColor.id : 0);
	return result != 0 ? this : null;
}

public NSColor interpolatedColorAtLocation(float location) {
	int result = OS.objc_msgSend(this.id, OS.sel_interpolatedColorAtLocation_1, location);
	return result != 0 ? new NSColor(result) : null;
}

public int numberOfColorStops() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfColorStops);
}

}

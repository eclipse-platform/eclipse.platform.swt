/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSBezierPath extends NSObject {

public NSBezierPath() {
	super();
}

public NSBezierPath(int /*long*/ id) {
	super(id);
}

public NSBezierPath(id id) {
	super(id);
}

public void addClip() {
	OS.objc_msgSend(this.id, OS.sel_addClip);
}

public void appendBezierPath(NSBezierPath path) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPath_, path != null ? path.id : 0);
}

public void appendBezierPathWithArcWithCenter(NSPoint center, float /*double*/ radius, float /*double*/ startAngle, float /*double*/ endAngle) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_, center, radius, startAngle, endAngle);
}

public void appendBezierPathWithArcWithCenter(NSPoint center, float /*double*/ radius, float /*double*/ startAngle, float /*double*/ endAngle, boolean clockwise) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_clockwise_, center, radius, startAngle, endAngle, clockwise);
}

public void appendBezierPathWithGlyphs(int /*long*/ glyphs, int /*long*/ count, NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithGlyphs_count_inFont_, glyphs, count, font != null ? font.id : 0);
}

public void appendBezierPathWithOvalInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithOvalInRect_, rect);
}

public void appendBezierPathWithRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithRect_, rect);
}

public void appendBezierPathWithRoundedRect(NSRect rect, float /*double*/ xRadius, float /*double*/ yRadius) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithRoundedRect_xRadius_yRadius_, rect, xRadius, yRadius);
}

public static NSBezierPath bezierPath() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPath);
	return result != 0 ? new NSBezierPath(result) : null;
}

public NSBezierPath bezierPathByFlatteningPath() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_bezierPathByFlatteningPath);
	return result == this.id ? this : (result != 0 ? new NSBezierPath(result) : null);
}

public static NSBezierPath bezierPathWithRect(NSRect rect) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPathWithRect_, rect);
	return result != 0 ? new NSBezierPath(result) : null;
}

public NSRect bounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_bounds);
	return result;
}

public void closePath() {
	OS.objc_msgSend(this.id, OS.sel_closePath);
}

public boolean containsPoint(NSPoint point) {
	return OS.objc_msgSend_bool(this.id, OS.sel_containsPoint_, point);
}

public NSRect controlPointBounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_controlPointBounds);
	return result;
}

public NSPoint currentPoint() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_currentPoint);
	return result;
}

public void curveToPoint(NSPoint endPoint, NSPoint controlPoint1, NSPoint controlPoint2) {
	OS.objc_msgSend(this.id, OS.sel_curveToPoint_controlPoint1_controlPoint2_, endPoint, controlPoint1, controlPoint2);
}

public static float /*double*/ defaultFlatness() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSBezierPath, OS.sel_defaultFlatness);
}

public int /*long*/ elementAtIndex(int /*long*/ index, int /*long*/ points) {
	return OS.objc_msgSend(this.id, OS.sel_elementAtIndex_associatedPoints_, index, points);
}

public int /*long*/ elementCount() {
	return OS.objc_msgSend(this.id, OS.sel_elementCount);
}

public void fill() {
	OS.objc_msgSend(this.id, OS.sel_fill);
}

public static void fillRect(NSRect rect) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_fillRect_, rect);
}

public boolean isEmpty() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEmpty);
}

public void lineToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_lineToPoint_, point);
}

public void moveToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_moveToPoint_, point);
}

public void removeAllPoints() {
	OS.objc_msgSend(this.id, OS.sel_removeAllPoints);
}

public void setClip() {
	OS.objc_msgSend(this.id, OS.sel_setClip);
}

public static void setDefaultFlatness(float /*double*/ flatness) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultFlatness_, flatness);
}

public void setLineCapStyle(int /*long*/ lineCapStyle) {
	OS.objc_msgSend(this.id, OS.sel_setLineCapStyle_, lineCapStyle);
}

public void setLineDash(float[] /*double[]*/ pattern, int /*long*/ count, float /*double*/ phase) {
	OS.objc_msgSend(this.id, OS.sel_setLineDash_count_phase_, pattern, count, phase);
}

public void setLineJoinStyle(int /*long*/ lineJoinStyle) {
	OS.objc_msgSend(this.id, OS.sel_setLineJoinStyle_, lineJoinStyle);
}

public void setLineWidth(float /*double*/ lineWidth) {
	OS.objc_msgSend(this.id, OS.sel_setLineWidth_, lineWidth);
}

public void setMiterLimit(float /*double*/ miterLimit) {
	OS.objc_msgSend(this.id, OS.sel_setMiterLimit_, miterLimit);
}

public void setWindingRule(int /*long*/ windingRule) {
	OS.objc_msgSend(this.id, OS.sel_setWindingRule_, windingRule);
}

public void stroke() {
	OS.objc_msgSend(this.id, OS.sel_stroke);
}

public static void strokeRect(NSRect rect) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_strokeRect_, rect);
}

public void transformUsingAffineTransform(NSAffineTransform transform) {
	OS.objc_msgSend(this.id, OS.sel_transformUsingAffineTransform_, transform != null ? transform.id : 0);
}

}

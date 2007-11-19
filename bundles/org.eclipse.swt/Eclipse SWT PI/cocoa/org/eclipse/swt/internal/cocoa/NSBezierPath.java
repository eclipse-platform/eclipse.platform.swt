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

public class NSBezierPath extends NSObject {

public NSBezierPath() {
	super();
}

public NSBezierPath(int id) {
	super(id);
}

public void addClip() {
	OS.objc_msgSend(this.id, OS.sel_addClip);
}

public void appendBezierPath(NSBezierPath path) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPath_1, path != null ? path.id : 0);
}

public void appendBezierPathWithArcFromPoint(NSPoint point1, NSPoint point2, float radius) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithArcFromPoint_1toPoint_1radius_1, point1, point2, radius);
}

public void appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_(NSPoint center, float radius, float startAngle, float endAngle) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithArcWithCenter_1radius_1startAngle_1endAngle_1, center, radius, startAngle, endAngle);
}

public void appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_clockwise_(NSPoint center, float radius, float startAngle, float endAngle, boolean clockwise) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithArcWithCenter_1radius_1startAngle_1endAngle_1clockwise_1, center, radius, startAngle, endAngle, clockwise);
}

public void appendBezierPathWithGlyph(int glyph, NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithGlyph_1inFont_1, glyph, font != null ? font.id : 0);
}

public void appendBezierPathWithGlyphs(int glyphs, int count, NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithGlyphs_1count_1inFont_1, glyphs, count, font != null ? font.id : 0);
}

public void appendBezierPathWithOvalInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithOvalInRect_1, rect);
}

public void appendBezierPathWithPackedGlyphs(int packedGlyphs) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithPackedGlyphs_1, packedGlyphs);
}

public void appendBezierPathWithPoints(int points, int count) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithPoints_1count_1, points, count);
}

public void appendBezierPathWithRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithRect_1, rect);
}

public void appendBezierPathWithRoundedRect(NSRect rect, float xRadius, float yRadius) {
	OS.objc_msgSend(this.id, OS.sel_appendBezierPathWithRoundedRect_1xRadius_1yRadius_1, rect, xRadius, yRadius);
}

public static NSBezierPath bezierPath() {
	int result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPath);
	return result != 0 ? new NSBezierPath(result) : null;
}

public NSBezierPath bezierPathByFlatteningPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_bezierPathByFlatteningPath);
	return result == this.id ? this : (result != 0 ? new NSBezierPath(result) : null);
}

public NSBezierPath bezierPathByReversingPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_bezierPathByReversingPath);
	return result == this.id ? this : (result != 0 ? new NSBezierPath(result) : null);
}

public static NSBezierPath bezierPathWithOvalInRect(NSRect rect) {
	int result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPathWithOvalInRect_1, rect);
	return result != 0 ? new NSBezierPath(result) : null;
}

public static NSBezierPath bezierPathWithRect(NSRect rect) {
	int result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPathWithRect_1, rect);
	return result != 0 ? new NSBezierPath(result) : null;
}

public static NSBezierPath bezierPathWithRoundedRect(NSRect rect, float xRadius, float yRadius) {
	int result = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPathWithRoundedRect_1xRadius_1yRadius_1, rect, xRadius, yRadius);
	return result != 0 ? new NSBezierPath(result) : null;
}

public NSRect bounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_bounds);
	return result;
}

public boolean cachesBezierPath() {
	return OS.objc_msgSend(this.id, OS.sel_cachesBezierPath) != 0;
}

public static void clipRect(NSRect rect) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_clipRect_1, rect);
}

public void closePath() {
	OS.objc_msgSend(this.id, OS.sel_closePath);
}

public boolean containsPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_containsPoint_1, point) != 0;
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
	OS.objc_msgSend(this.id, OS.sel_curveToPoint_1controlPoint1_1controlPoint2_1, endPoint, controlPoint1, controlPoint2);
}

public static float defaultFlatness() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSBezierPath, OS.sel_defaultFlatness);
}

public static int defaultLineCapStyle() {
	return OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_defaultLineCapStyle);
}

public static int defaultLineJoinStyle() {
	return OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_defaultLineJoinStyle);
}

public static float defaultLineWidth() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSBezierPath, OS.sel_defaultLineWidth);
}

public static float defaultMiterLimit() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSBezierPath, OS.sel_defaultMiterLimit);
}

public static int defaultWindingRule() {
	return OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_defaultWindingRule);
}

public static void drawPackedGlyphs(int packedGlyphs, NSPoint point) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_drawPackedGlyphs_1atPoint_1, packedGlyphs, point);
}

public int elementAtIndex_(int index) {
	return OS.objc_msgSend(this.id, OS.sel_elementAtIndex_1, index);
}

public int elementAtIndex_associatedPoints_(int index, int points) {
	return OS.objc_msgSend(this.id, OS.sel_elementAtIndex_1associatedPoints_1, index, points);
}

public int elementCount() {
	return OS.objc_msgSend(this.id, OS.sel_elementCount);
}

public void fill() {
	OS.objc_msgSend(this.id, OS.sel_fill);
}

public static void fillRect(NSRect rect) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_fillRect_1, rect);
}

public float flatness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_flatness);
}

public void getLineDash(int pattern, int count, int phase) {
	OS.objc_msgSend(this.id, OS.sel_getLineDash_1count_1phase_1, pattern, count, phase);
}

public boolean isEmpty() {
	return OS.objc_msgSend(this.id, OS.sel_isEmpty) != 0;
}

public int lineCapStyle() {
	return OS.objc_msgSend(this.id, OS.sel_lineCapStyle);
}

public int lineJoinStyle() {
	return OS.objc_msgSend(this.id, OS.sel_lineJoinStyle);
}

public void lineToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_lineToPoint_1, point);
}

public float lineWidth() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineWidth);
}

public float miterLimit() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_miterLimit);
}

public void moveToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_moveToPoint_1, point);
}

public void relativeCurveToPoint(NSPoint endPoint, NSPoint controlPoint1, NSPoint controlPoint2) {
	OS.objc_msgSend(this.id, OS.sel_relativeCurveToPoint_1controlPoint1_1controlPoint2_1, endPoint, controlPoint1, controlPoint2);
}

public void relativeLineToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_relativeLineToPoint_1, point);
}

public void relativeMoveToPoint(NSPoint point) {
	OS.objc_msgSend(this.id, OS.sel_relativeMoveToPoint_1, point);
}

public void removeAllPoints() {
	OS.objc_msgSend(this.id, OS.sel_removeAllPoints);
}

public void setAssociatedPoints(int points, int index) {
	OS.objc_msgSend(this.id, OS.sel_setAssociatedPoints_1atIndex_1, points, index);
}

public void setCachesBezierPath(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCachesBezierPath_1, flag);
}

public void setClip() {
	OS.objc_msgSend(this.id, OS.sel_setClip);
}

public static void setDefaultFlatness(float flatness) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultFlatness_1, flatness);
}

public static void setDefaultLineCapStyle(int lineCapStyle) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultLineCapStyle_1, lineCapStyle);
}

public static void setDefaultLineJoinStyle(int lineJoinStyle) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultLineJoinStyle_1, lineJoinStyle);
}

public static void setDefaultLineWidth(float lineWidth) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultLineWidth_1, lineWidth);
}

public static void setDefaultMiterLimit(float limit) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultMiterLimit_1, limit);
}

public static void setDefaultWindingRule(int windingRule) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_setDefaultWindingRule_1, windingRule);
}

public void setFlatness(float flatness) {
	OS.objc_msgSend(this.id, OS.sel_setFlatness_1, flatness);
}

public void setLineCapStyle(int lineCapStyle) {
	OS.objc_msgSend(this.id, OS.sel_setLineCapStyle_1, lineCapStyle);
}

public void setLineDash(float[] pattern, int count, float phase) {
	OS.objc_msgSend(this.id, OS.sel_setLineDash_1count_1phase_1, pattern, count, phase);
}

public void setLineJoinStyle(int lineJoinStyle) {
	OS.objc_msgSend(this.id, OS.sel_setLineJoinStyle_1, lineJoinStyle);
}

public void setLineWidth(float lineWidth) {
	OS.objc_msgSend(this.id, OS.sel_setLineWidth_1, lineWidth);
}

public void setMiterLimit(float miterLimit) {
	OS.objc_msgSend(this.id, OS.sel_setMiterLimit_1, miterLimit);
}

public void setWindingRule(int windingRule) {
	OS.objc_msgSend(this.id, OS.sel_setWindingRule_1, windingRule);
}

public void stroke() {
	OS.objc_msgSend(this.id, OS.sel_stroke);
}

public static void strokeLineFromPoint(NSPoint point1, NSPoint point2) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_strokeLineFromPoint_1toPoint_1, point1, point2);
}

public static void strokeRect(NSRect rect) {
	OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_strokeRect_1, rect);
}

public void transformUsingAffineTransform(NSAffineTransform transform) {
	OS.objc_msgSend(this.id, OS.sel_transformUsingAffineTransform_1, transform != null ? transform.id : 0);
}

public int windingRule() {
	return OS.objc_msgSend(this.id, OS.sel_windingRule);
}

}

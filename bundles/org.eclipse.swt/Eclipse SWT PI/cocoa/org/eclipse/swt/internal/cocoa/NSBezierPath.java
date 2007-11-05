package org.eclipse.swt.internal.cocoa;

public class NSBezierPath extends NSObject {

public NSBezierPath(int id) {
	super(id);
}

public void appendBezierPath(NSBezierPath path) {
	OS.objc_msgSend(id, OS.sel_appendBezierPath_1, path != null ? path.id : 0);
}

public void appendBezierPathWithRect(NSRect rect) {
	OS.objc_msgSend(id, OS.sel_appendBezierPathWithRect_1, rect);
}

public void appendBezierPathWithOvalInRect(NSRect rect) {
	OS.objc_msgSend(id, OS.sel_appendBezierPathWithOvalInRect_1, rect);
}

public void appendBezierPathWithRoundedRect(NSRect rect, float xRadius, float yRadius) {
	OS.objc_msgSend(id, OS.sel_appendBezierPathWithRoundedRect_1xRadius_1yRadius_1, rect, xRadius, yRadius);
}

public static NSBezierPath bezierPath() {
	int id = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPath);
	return id != 0 ? new NSBezierPath(id) : null;
}

public static NSBezierPath bezierPathWithOvalInRect(NSRect rect) {
	int id = OS.objc_msgSend(OS.class_NSBezierPath, OS.sel_bezierPathWithOvalInRect_1, rect);
	return id != 0 ? new NSBezierPath(id) : null;
}

public void closePath() {
	OS.objc_msgSend(id, OS.sel_closePath);
}

public NSRect controlPointBounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, id, OS.sel_controlPointBounds);
	return result;
}

public void curveToPoint(NSPoint pt, NSPoint ct1, NSPoint ct2) {
	OS.objc_msgSend(id, OS.sel_curveToPoint_1controlPoint1_1controlPoint2_1, pt, ct1, ct2);
}

public NSPoint currentPoint() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, id, OS.sel_currentPoint);
	return result;
}

public void fill() {
	OS.objc_msgSend(id, OS.sel_fill);
}

public void lineToPoint(NSPoint pt) {
	OS.objc_msgSend(id, OS.sel_lineToPoint_1, pt);
}

public void moveToPoint(NSPoint pt) {
	OS.objc_msgSend(id, OS.sel_moveToPoint_1, pt);
}

public void removeAllPoints() {
	OS.objc_msgSend(id, OS.sel_removeAllPoints);
}

public void setLineWidth(float width) {
	OS.objc_msgSend(id, OS.sel_setLineWidth_1, width);
}

public void stroke() {
	OS.objc_msgSend(id, OS.sel_stroke);
}
}

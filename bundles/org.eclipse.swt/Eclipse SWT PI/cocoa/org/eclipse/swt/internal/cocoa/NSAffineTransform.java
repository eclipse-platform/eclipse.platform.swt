package org.eclipse.swt.internal.cocoa;

public class NSAffineTransform extends NSObject {

public NSAffineTransform(int id) {
	super(id);
}

public static NSAffineTransform transform() {
	int id = OS.objc_msgSend(OS.class_NSAffineTransform, OS.sel_transform);
	return id != 0 ? new NSAffineTransform(id) : null;
}

public void invert() {
	OS.objc_msgSend(id, OS.sel_invert);
}

public void prependTransform(NSAffineTransform transform) {
	OS.objc_msgSend(id, OS.sel_prependTransform_1, transform != null ? transform.id : 0);
}

public void rotateByDegrees(float angle) {
	OS.objc_msgSend(id, OS.sel_rotateByDegrees_1, angle);
}

public void scale(float sx, float sy) {
	OS.objc_msgSend(id, OS.sel_scaleXBy_1yBy_1, sx, sy);
}

public void setTransformStruct(float[] struct) {
	OS.objc_msgSend(id, OS.sel_setTransformStruct_1, struct);
}

public NSPoint transformPoint(NSPoint point) {
	OS.objc_msgSend_stret(point, id, OS.sel_transformPoint_1, point);
	return point;
}

public float[] transformStruct() {
	float[] result = new float[6];
	OS.objc_msgSend_stret(result, id, OS.sel_transformStruct);
	return result;
}

public void translate(float dx, float dy) {
	OS.objc_msgSend(id, OS.sel_translateXBy_1yBy_1, dx, dy);
}

}

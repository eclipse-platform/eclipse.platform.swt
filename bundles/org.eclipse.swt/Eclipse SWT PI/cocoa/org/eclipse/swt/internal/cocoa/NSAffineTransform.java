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

public class NSAffineTransform extends NSObject {

public NSAffineTransform() {
	super();
}

public NSAffineTransform(int id) {
	super(id);
}

public void appendTransform(NSAffineTransform transform) {
	OS.objc_msgSend(this.id, OS.sel_appendTransform_1, transform != null ? transform.id : 0);
}

public NSAffineTransform initWithTransform(NSAffineTransform transform) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTransform_1, transform != null ? transform.id : 0);
	return result != 0 ? this : null;
}

public void invert() {
	OS.objc_msgSend(this.id, OS.sel_invert);
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public void concat() {
	OS.objc_msgSend(this.id, OS.sel_concat);
}

public void prependTransform(NSAffineTransform transform) {
	OS.objc_msgSend(this.id, OS.sel_prependTransform_1, transform != null ? transform.id : 0);
}

public void rotateByDegrees(float angle) {
	OS.objc_msgSend(this.id, OS.sel_rotateByDegrees_1, angle);
}

public void rotateByRadians(float angle) {
	OS.objc_msgSend(this.id, OS.sel_rotateByRadians_1, angle);
}

public void scaleBy(float scale) {
	OS.objc_msgSend(this.id, OS.sel_scaleBy_1, scale);
}

public void scaleXBy(float scaleX, float scaleY) {
	OS.objc_msgSend(this.id, OS.sel_scaleXBy_1yBy_1, scaleX, scaleY);
}

public void setTransformStruct(NSAffineTransformStruct transformStruct) {
	OS.objc_msgSend(this.id, OS.sel_setTransformStruct_1, transformStruct);
}

public static NSAffineTransform transform() {
	int result = OS.objc_msgSend(OS.class_NSAffineTransform, OS.sel_transform);
	return result != 0 ? new NSAffineTransform(result) : null;
}

public NSPoint transformPoint(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformPoint_1, aPoint);
	return result;
}

public NSSize transformSize(NSSize aSize) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformSize_1, aSize);
	return result;
}

public NSAffineTransformStruct transformStruct() {
	NSAffineTransformStruct result = new NSAffineTransformStruct();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformStruct);
	return result;
}

public void translateXBy(float deltaX, float deltaY) {
	OS.objc_msgSend(this.id, OS.sel_translateXBy_1yBy_1, deltaX, deltaY);
}

}

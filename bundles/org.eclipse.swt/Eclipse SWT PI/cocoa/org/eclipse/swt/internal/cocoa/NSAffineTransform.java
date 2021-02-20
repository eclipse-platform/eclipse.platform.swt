/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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

public class NSAffineTransform extends NSObject {

public NSAffineTransform() {
	super();
}

public NSAffineTransform(long id) {
	super(id);
}

public NSAffineTransform(id id) {
	super(id);
}

public void concat() {
	OS.objc_msgSend(this.id, OS.sel_concat);
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public NSAffineTransform init() {
	long result = OS.objc_msgSend(this.id, OS.sel_init);
	return result == this.id ? this : (result != 0 ? new NSAffineTransform(result) : null);
}

public void invert() {
	OS.objc_msgSend(this.id, OS.sel_invert);
}

public void prependTransform(NSAffineTransform transform) {
	OS.objc_msgSend(this.id, OS.sel_prependTransform_, transform != null ? transform.id : 0);
}

public void rotateByDegrees(double angle) {
	OS.objc_msgSend(this.id, OS.sel_rotateByDegrees_, angle);
}

public void scaleXBy(double scaleX, double scaleY) {
	OS.objc_msgSend(this.id, OS.sel_scaleXBy_yBy_, scaleX, scaleY);
}

public void setTransformStruct(NSAffineTransformStruct transformStruct) {
	OS.objc_msgSend(this.id, OS.sel_setTransformStruct_, transformStruct);
}

public static NSAffineTransform transform() {
	long result = OS.objc_msgSend(OS.class_NSAffineTransform, OS.sel_transform);
	return result != 0 ? new NSAffineTransform(result) : null;
}

public NSPoint transformPoint(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformPoint_, aPoint);
	return result;
}

public NSSize transformSize(NSSize aSize) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformSize_, aSize);
	return result;
}

public NSAffineTransformStruct transformStruct() {
	NSAffineTransformStruct result = new NSAffineTransformStruct();
	OS.objc_msgSend_stret(result, this.id, OS.sel_transformStruct);
	return result;
}

public void translateXBy(double deltaX, double deltaY) {
	OS.objc_msgSend(this.id, OS.sel_translateXBy_yBy_, deltaX, deltaY);
}

}

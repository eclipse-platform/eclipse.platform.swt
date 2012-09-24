/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSGraphicsContext extends NSObject {

public NSGraphicsContext() {
	super();
}

public NSGraphicsContext(long /*int*/ id) {
	super(id);
}

public NSGraphicsContext(id id) {
	super(id);
}

public static NSGraphicsContext currentContext() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_currentContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public void flushGraphics() {
	OS.objc_msgSend(this.id, OS.sel_flushGraphics);
}

public static NSGraphicsContext graphicsContextWithBitmapImageRep(NSBitmapImageRep bitmapRep) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithBitmapImageRep_, bitmapRep != null ? bitmapRep.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithGraphicsPort(long /*int*/ graphicsPort, boolean initialFlippedState) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithGraphicsPort_flipped_, graphicsPort, initialFlippedState);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithWindow(NSWindow window) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithWindow_, window != null ? window.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public long /*int*/ graphicsPort() {
	return OS.objc_msgSend(this.id, OS.sel_graphicsPort);
}

public long /*int*/ imageInterpolation() {
	return OS.objc_msgSend(this.id, OS.sel_imageInterpolation);
}

public boolean isDrawingToScreen() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isDrawingToScreen);
}

public boolean isFlipped() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFlipped);
}

public static void static_restoreGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_restoreGraphicsState);
}

public void restoreGraphicsState() {
	OS.objc_msgSend(this.id, OS.sel_restoreGraphicsState);
}

public static void static_saveGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_saveGraphicsState);
}

public void saveGraphicsState() {
	OS.objc_msgSend(this.id, OS.sel_saveGraphicsState);
}

public void setCompositingOperation(long /*int*/ operation) {
	OS.objc_msgSend(this.id, OS.sel_setCompositingOperation_, operation);
}

public static void setCurrentContext(NSGraphicsContext context) {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_setCurrentContext_, context != null ? context.id : 0);
}

public void setImageInterpolation(long /*int*/ interpolation) {
	OS.objc_msgSend(this.id, OS.sel_setImageInterpolation_, interpolation);
}

public void setPatternPhase(NSPoint phase) {
	OS.objc_msgSend(this.id, OS.sel_setPatternPhase_, phase);
}

public void setShouldAntialias(boolean antialias) {
	OS.objc_msgSend(this.id, OS.sel_setShouldAntialias_, antialias);
}

public boolean shouldAntialias() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldAntialias);
}

}

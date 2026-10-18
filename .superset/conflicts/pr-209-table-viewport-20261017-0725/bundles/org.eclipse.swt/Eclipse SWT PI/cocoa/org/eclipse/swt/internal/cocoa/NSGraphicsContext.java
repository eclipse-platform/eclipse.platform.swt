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

public class NSGraphicsContext extends NSObject {

public NSGraphicsContext() {
	super();
}

public NSGraphicsContext(long id) {
	super(id);
}

public NSGraphicsContext(id id) {
	super(id);
}

public static NSGraphicsContext currentContext() {
	long result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_currentContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public void flushGraphics() {
	OS.objc_msgSend(this.id, OS.sel_flushGraphics);
}

public static NSGraphicsContext graphicsContextWithBitmapImageRep(NSBitmapImageRep bitmapRep) {
	long result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithBitmapImageRep_, bitmapRep != null ? bitmapRep.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithGraphicsPort(long graphicsPort, boolean initialFlippedState) {
	long result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithGraphicsPort_flipped_, graphicsPort, initialFlippedState);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithWindow(NSWindow window) {
	long result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithWindow_, window != null ? window.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public long graphicsPort() {
	return OS.objc_msgSend(this.id, OS.sel_graphicsPort);
}

public long imageInterpolation() {
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

public void setCompositingOperation(long compositingOperation) {
	OS.objc_msgSend(this.id, OS.sel_setCompositingOperation_, compositingOperation);
}

public static void setCurrentContext(NSGraphicsContext context) {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_setCurrentContext_, context != null ? context.id : 0);
}

public void setImageInterpolation(long imageInterpolation) {
	OS.objc_msgSend(this.id, OS.sel_setImageInterpolation_, imageInterpolation);
}

public void setPatternPhase(NSPoint patternPhase) {
	OS.objc_msgSend(this.id, OS.sel_setPatternPhase_, patternPhase);
}

public void setShouldAntialias(boolean shouldAntialias) {
	OS.objc_msgSend(this.id, OS.sel_setShouldAntialias_, shouldAntialias);
}

public boolean shouldAntialias() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldAntialias);
}

}

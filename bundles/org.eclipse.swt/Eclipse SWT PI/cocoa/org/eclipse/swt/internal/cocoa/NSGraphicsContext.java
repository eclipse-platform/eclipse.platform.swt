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

public class NSGraphicsContext extends NSObject {

public NSGraphicsContext() {
	super();
}

public NSGraphicsContext(int id) {
	super(id);
}

//public CIContext CIContext() {
//	int result = OS.objc_msgSend(this.id, OS.sel_CIContext);
//	return result != 0 ? new CIContext(result) : null;
//}

public NSDictionary attributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public int colorRenderingIntent() {
	return OS.objc_msgSend(this.id, OS.sel_colorRenderingIntent);
}

public int compositingOperation() {
	return OS.objc_msgSend(this.id, OS.sel_compositingOperation);
}

public static NSGraphicsContext currentContext() {
	int result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_currentContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static boolean currentContextDrawingToScreen() {
	return OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_currentContextDrawingToScreen) != 0;
}

public void flushGraphics() {
	OS.objc_msgSend(this.id, OS.sel_flushGraphics);
}

public id focusStack() {
	int result = OS.objc_msgSend(this.id, OS.sel_focusStack);
	return result != 0 ? new id(result) : null;
}

public static NSGraphicsContext graphicsContextWithAttributes(NSDictionary attributes) {
	int result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithAttributes_1, attributes != null ? attributes.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithBitmapImageRep(NSBitmapImageRep bitmapRep) {
	int result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithBitmapImageRep_1, bitmapRep != null ? bitmapRep.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithGraphicsPort(int graphicsPort, boolean initialFlippedState) {
	int result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithGraphicsPort_1flipped_1, graphicsPort, initialFlippedState);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSGraphicsContext graphicsContextWithWindow(NSWindow window) {
	int result = OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_graphicsContextWithWindow_1, window != null ? window.id : 0);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public int graphicsPort() {
	return OS.objc_msgSend(this.id, OS.sel_graphicsPort);
}

public int imageInterpolation() {
	return OS.objc_msgSend(this.id, OS.sel_imageInterpolation);
}

public boolean isDrawingToScreen() {
	return OS.objc_msgSend(this.id, OS.sel_isDrawingToScreen) != 0;
}

public boolean isFlipped() {
	return OS.objc_msgSend(this.id, OS.sel_isFlipped) != 0;
}

public NSPoint patternPhase() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_patternPhase);
	return result;
}

public void restoreGraphicsState() {
	OS.objc_msgSend(this.id, OS.sel_restoreGraphicsState);
}

public static void static_restoreGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_restoreGraphicsState);
}

public void saveGraphicsState() {
	OS.objc_msgSend(this.id, OS.sel_saveGraphicsState);
}

public static void static_saveGraphicsState() {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_saveGraphicsState);
}

public void setColorRenderingIntent(int renderingIntent) {
	OS.objc_msgSend(this.id, OS.sel_setColorRenderingIntent_1, renderingIntent);
}

public void setCompositingOperation(int operation) {
	OS.objc_msgSend(this.id, OS.sel_setCompositingOperation_1, operation);
}

public static void setCurrentContext(NSGraphicsContext context) {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_setCurrentContext_1, context != null ? context.id : 0);
}

public void setFocusStack(id stack) {
	OS.objc_msgSend(this.id, OS.sel_setFocusStack_1, stack != null ? stack.id : 0);
}

public static void setGraphicsState(int gState) {
	OS.objc_msgSend(OS.class_NSGraphicsContext, OS.sel_setGraphicsState_1, gState);
}

public void setImageInterpolation(int interpolation) {
	OS.objc_msgSend(this.id, OS.sel_setImageInterpolation_1, interpolation);
}

public void setPatternPhase(NSPoint phase) {
	OS.objc_msgSend(this.id, OS.sel_setPatternPhase_1, phase);
}

public void setShouldAntialias(boolean antialias) {
	OS.objc_msgSend(this.id, OS.sel_setShouldAntialias_1, antialias);
}

public boolean shouldAntialias() {
	return OS.objc_msgSend(this.id, OS.sel_shouldAntialias) != 0;
}

}

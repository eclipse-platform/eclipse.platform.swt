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

public class NSOpenGLContext extends NSObject {

public NSOpenGLContext() {
	super();
}

public NSOpenGLContext(int id) {
	super(id);
}

public int CGLContextObj() {
	return OS.objc_msgSend(this.id, OS.sel_CGLContextObj);
}

public static void clearCurrentContext() {
	OS.objc_msgSend(OS.class_NSOpenGLContext, OS.sel_clearCurrentContext);
}

public void clearDrawable() {
	OS.objc_msgSend(this.id, OS.sel_clearDrawable);
}

public void copyAttributesFromContext(NSOpenGLContext context, int mask) {
	OS.objc_msgSend(this.id, OS.sel_copyAttributesFromContext_1withMask_1, context != null ? context.id : 0, mask);
}

public void createTexture(int target, NSView view, int format) {
	OS.objc_msgSend(this.id, OS.sel_createTexture_1fromView_1internalFormat_1, target, view != null ? view.id : 0, format);
}

public static NSOpenGLContext currentContext() {
	int result = OS.objc_msgSend(OS.class_NSOpenGLContext, OS.sel_currentContext);
	return result != 0 ? new NSOpenGLContext(result) : null;
}

public int currentVirtualScreen() {
	return OS.objc_msgSend(this.id, OS.sel_currentVirtualScreen);
}

public void flushBuffer() {
	OS.objc_msgSend(this.id, OS.sel_flushBuffer);
}

public void getValues(int vals, int param) {
	OS.objc_msgSend(this.id, OS.sel_getValues_1forParameter_1, vals, param);
}

public NSOpenGLContext initWithFormat(NSOpenGLPixelFormat format, NSOpenGLContext share) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_1shareContext_1, format != null ? format.id : 0, share != null ? share.id : 0);
	return result != 0 ? this : null;
}

public void makeCurrentContext() {
	OS.objc_msgSend(this.id, OS.sel_makeCurrentContext);
}

public NSOpenGLPixelBuffer pixelBuffer() {
	int result = OS.objc_msgSend(this.id, OS.sel_pixelBuffer);
	return result != 0 ? new NSOpenGLPixelBuffer(result) : null;
}

public int pixelBufferCubeMapFace() {
	return OS.objc_msgSend(this.id, OS.sel_pixelBufferCubeMapFace);
}

public int pixelBufferMipMapLevel() {
	return OS.objc_msgSend(this.id, OS.sel_pixelBufferMipMapLevel);
}

public void setCurrentVirtualScreen(int screen) {
	OS.objc_msgSend(this.id, OS.sel_setCurrentVirtualScreen_1, screen);
}

public void setFullScreen() {
	OS.objc_msgSend(this.id, OS.sel_setFullScreen);
}

public void setOffScreen(int baseaddr, int width, int height, int rowbytes) {
	OS.objc_msgSend(this.id, OS.sel_setOffScreen_1width_1height_1rowbytes_1, baseaddr, width, height, rowbytes);
}

public void setPixelBuffer(NSOpenGLPixelBuffer pixelBuffer, int face, int level, int screen) {
	OS.objc_msgSend(this.id, OS.sel_setPixelBuffer_1cubeMapFace_1mipMapLevel_1currentVirtualScreen_1, pixelBuffer != null ? pixelBuffer.id : 0, face, level, screen);
}

public void setTextureImageToPixelBuffer(NSOpenGLPixelBuffer pixelBuffer, int source) {
	OS.objc_msgSend(this.id, OS.sel_setTextureImageToPixelBuffer_1colorBuffer_1, pixelBuffer != null ? pixelBuffer.id : 0, source);
}

public void setValues(int vals, int param) {
	OS.objc_msgSend(this.id, OS.sel_setValues_1forParameter_1, vals, param);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public void update() {
	OS.objc_msgSend(this.id, OS.sel_update);
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}

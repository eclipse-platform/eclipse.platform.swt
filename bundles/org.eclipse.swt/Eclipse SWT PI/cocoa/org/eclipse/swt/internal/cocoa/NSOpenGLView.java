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

public class NSOpenGLView extends NSView {

public NSOpenGLView() {
	super();
}

public NSOpenGLView(int id) {
	super(id);
}

public void clearGLContext() {
	OS.objc_msgSend(this.id, OS.sel_clearGLContext);
}

public static NSOpenGLPixelFormat defaultPixelFormat() {
	int result = OS.objc_msgSend(OS.class_NSOpenGLView, OS.sel_defaultPixelFormat);
	return result != 0 ? new NSOpenGLPixelFormat(result) : null;
}

public NSOpenGLView initWithFrame(NSRect frameRect, NSOpenGLPixelFormat format) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1pixelFormat_1, frameRect, format != null ? format.id : 0);
	return result != 0 ? this : null;
}

public NSOpenGLContext openGLContext() {
	int result = OS.objc_msgSend(this.id, OS.sel_openGLContext);
	return result != 0 ? new NSOpenGLContext(result) : null;
}

public NSOpenGLPixelFormat pixelFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_pixelFormat);
	return result != 0 ? new NSOpenGLPixelFormat(result) : null;
}

public void prepareOpenGL() {
	OS.objc_msgSend(this.id, OS.sel_prepareOpenGL);
}

public void reshape() {
	OS.objc_msgSend(this.id, OS.sel_reshape);
}

public void setOpenGLContext(NSOpenGLContext context) {
	OS.objc_msgSend(this.id, OS.sel_setOpenGLContext_1, context != null ? context.id : 0);
}

public void setPixelFormat(NSOpenGLPixelFormat pixelFormat) {
	OS.objc_msgSend(this.id, OS.sel_setPixelFormat_1, pixelFormat != null ? pixelFormat.id : 0);
}

public void update() {
	OS.objc_msgSend(this.id, OS.sel_update);
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSOpenGLView extends NSView {

public NSOpenGLView() {
	super();
}

public NSOpenGLView(int /*long*/ id) {
	super(id);
}

public NSOpenGLView(id id) {
	super(id);
}

public void clearGLContext() {
	OS.objc_msgSend(this.id, OS.sel_clearGLContext);
}

public id initWithFrame(NSRect frameRect, NSOpenGLPixelFormat format) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_pixelFormat_, frameRect, format != null ? format.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSOpenGLContext openGLContext() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_openGLContext);
	return result != 0 ? new NSOpenGLContext(result) : null;
}

public void setOpenGLContext(NSOpenGLContext context) {
	OS.objc_msgSend(this.id, OS.sel_setOpenGLContext_, context != null ? context.id : 0);
}

public void setPixelFormat(NSOpenGLPixelFormat pixelFormat) {
	OS.objc_msgSend(this.id, OS.sel_setPixelFormat_, pixelFormat != null ? pixelFormat.id : 0);
}

}

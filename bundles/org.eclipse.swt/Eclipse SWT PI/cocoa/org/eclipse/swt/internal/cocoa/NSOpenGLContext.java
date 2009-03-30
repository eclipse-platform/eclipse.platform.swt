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

public class NSOpenGLContext extends NSObject {

public NSOpenGLContext() {
	super();
}

public NSOpenGLContext(int /*long*/ id) {
	super(id);
}

public NSOpenGLContext(id id) {
	super(id);
}

public static void clearCurrentContext() {
	OS.objc_msgSend(OS.class_NSOpenGLContext, OS.sel_clearCurrentContext);
}

public static NSOpenGLContext currentContext() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSOpenGLContext, OS.sel_currentContext);
	return result != 0 ? new NSOpenGLContext(result) : null;
}

public void flushBuffer() {
	OS.objc_msgSend(this.id, OS.sel_flushBuffer);
}

public id initWithFormat(NSOpenGLPixelFormat format, NSOpenGLContext share) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFormat_shareContext_, format != null ? format.id : 0, share != null ? share.id : 0);
	return result != 0 ? new id(result) : null;
}

public void makeCurrentContext() {
	OS.objc_msgSend(this.id, OS.sel_makeCurrentContext);
}

}

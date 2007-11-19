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

public class NSOpenGLPixelFormat extends NSObject {

public NSOpenGLPixelFormat() {
	super();
}

public NSOpenGLPixelFormat(int id) {
	super(id);
}

public int CGLPixelFormatObj() {
	return OS.objc_msgSend(this.id, OS.sel_CGLPixelFormatObj);
}

public NSData attributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributes);
	return result != 0 ? new NSData(result) : null;
}

public void getValues(int vals, int attrib, int screen) {
	OS.objc_msgSend(this.id, OS.sel_getValues_1forAttribute_1forVirtualScreen_1, vals, attrib, screen);
}

public NSOpenGLPixelFormat initWithAttributes(int[] attribs) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithAttributes_1, attribs);
	return result != 0 ? this : null;
}

public NSOpenGLPixelFormat initWithData(NSData attribs) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithData_1, attribs != null ? attribs.id : 0);
	return result != 0 ? this : null;
}

public int numberOfVirtualScreens() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfVirtualScreens);
}

public void setAttributes(NSData attribs) {
	OS.objc_msgSend(this.id, OS.sel_setAttributes_1, attribs != null ? attribs.id : 0);
}

}

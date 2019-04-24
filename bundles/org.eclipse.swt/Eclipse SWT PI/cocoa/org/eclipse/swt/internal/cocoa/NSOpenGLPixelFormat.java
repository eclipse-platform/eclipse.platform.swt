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

public class NSOpenGLPixelFormat extends NSObject {

public NSOpenGLPixelFormat() {
	super();
}

public NSOpenGLPixelFormat(long id) {
	super(id);
}

public NSOpenGLPixelFormat(id id) {
	super(id);
}

public void getValues(long[] vals, int attrib, int screen) {
	OS.objc_msgSend(this.id, OS.sel_getValues_forAttribute_forVirtualScreen_, vals, attrib, screen);
}

public NSOpenGLPixelFormat initWithAttributes(int[] attribs) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithAttributes_, attribs);
	return result == this.id ? this : (result != 0 ? new NSOpenGLPixelFormat(result) : null);
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.internal.gtk;

import org.eclipse.swt.opengl.Library;

public class XGTK {
	
	static {
		Library.loadLibrary("xgtk");
	}
	
	public static final synchronized native int gdk_x11_gc_get_xdisplay(int gc);
	public static final synchronized native int gdk_x11_drawable_get_xid(int gc);
	public static final synchronized native int XDefaultScreen(int display);
	public static final synchronized native int XDefaultScreenOfDisplay(int display);
	public static final synchronized native int XDefaultDepthOfScreen(int screen);
	public static final synchronized native int XFree(int address);
	public static final synchronized native void free(int ptr);
	public static final synchronized native int malloc(int size);
}

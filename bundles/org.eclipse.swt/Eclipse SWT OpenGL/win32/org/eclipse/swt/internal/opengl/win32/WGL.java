/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.opengl.win32;

import org.eclipse.swt.internal.*;

public class WGL extends Platform {
	static {
		Library.loadLibrary("swt-wgl");
	}

	public static final int PFD_TYPE_RGBA        = 0;
	public static final int PFD_MAIN_PLANE       = 0;
	public static final int PFD_DOUBLEBUFFER            = 0x00000001;
	public static final int PFD_STEREO                  = 0x00000002;
	public static final int PFD_DRAW_TO_WINDOW          = 0x00000004;
	public static final int PFD_SUPPORT_OPENGL          = 0x00000020;

/**
 * @param hdc cast=(HDC)
 * @param ppfd flags=no_out
 */
public static final native int ChoosePixelFormat(long hdc, PIXELFORMATDESCRIPTOR ppfd);
/**
 * @param hdc cast=(HDC)
 * @param ppfd flags=no_in
 */
public static final native int DescribePixelFormat(long hdc, int iPixelFormat, int nBytes, PIXELFORMATDESCRIPTOR ppfd);
/**
 * @param hdc cast=(HDC)
 * @param ppfd flags=no_out
 */
public static final native boolean SetPixelFormat(long hdc, int iPixelFormat, PIXELFORMATDESCRIPTOR ppfd);
/** @param hdc cast=(HDC) */
public static final native boolean SwapBuffers(long hdc);
/** @param hdc cast=(HDC) */
public static final native long wglCreateContext(long hdc);
/** @param hglrc cast=(HGLRC) */
public static final native boolean wglDeleteContext(long hglrc);
public static final native long wglGetCurrentContext();
/**
 * @param hdc cast=(HDC)
 * @param hglrc cast=(HGLRC)
 */
public static final native boolean wglMakeCurrent(long hdc, long hglrc);
/**
 * @param hglrc1 cast=(HGLRC)
 * @param hglrc2 cast=(HGLRC)
 */
public static final native boolean wglShareLists(long hglrc1, long hglrc2);

}

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
package org.eclipse.swt.opengl;

import org.eclipse.swt.*;
import org.eclipse.swt.opengl.internal.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

public class GLCanvas extends Canvas {
	int glHandle;

public GLCanvas (Composite parent, int style, GLFormatData data) {
	super (parent, style);		
	if (data == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);

	PIXELFORMATDESCRIPTOR pfd = new PIXELFORMATDESCRIPTOR ();
	pfd.nSize = (short) PIXELFORMATDESCRIPTOR.sizeof;
	pfd.nVersion = 1;
	pfd.dwFlags = WGL.PFD_DRAW_TO_WINDOW | WGL.PFD_SUPPORT_OPENGL;
	pfd.dwLayerMask = WGL.PFD_MAIN_PLANE;
	pfd.iPixelType = (byte) WGL.PFD_TYPE_RGBA;
	pfd.cColorBits = (byte) data.bufferSize;
	if (data.doubleBuffer) pfd.dwFlags |= WGL.PFD_DOUBLEBUFFER;
	if (data.stereo) pfd.dwFlags |= WGL.PFD_STEREO;
	pfd.cRedBits = (byte) data.redSize;
	pfd.cGreenBits = (byte) data.greenSize;
	pfd.cBlueBits = (byte) data.blueSize;
	pfd.cAlphaBits = (byte) data.depthSize;
	pfd.cStencilBits = (byte) data.stencilSize;
	pfd.cAccumRedBits = (byte) data.accumRedSize;
	pfd.cAccumGreenBits = (byte) data.accumGreenSize;
	pfd.cAccumBlueBits = (byte) data.accumBlueSize;
	pfd.cAccumAlphaBits = (byte) data.accumAlphaSize;
	pfd.cAccumBits = (byte) (pfd.cAccumRedBits + pfd.cAccumGreenBits + pfd.cAccumBlueBits + pfd.cAccumAlphaBits);
	//FIXME - use wglChoosePixelFormatARB
//	if (data.sampleBuffers > 0) {
//		wglAttrib [pos++] = WGL.WGL_SAMPLE_BUFFERS_ARB;
//		wglAttrib [pos++] = data.sampleBuffers;
//	}
//	if (data.samples > 0) {
//		wglAttrib [pos++] = WGL.WGL_SAMPLES_ARB;
//		wglAttrib [pos++] = data.samples;
//	}

	int hDC = OS.GetDC (handle);
	int pixelFormat = WGL.ChoosePixelFormat (hDC, pfd);
	if (pixelFormat == 0) {		
		OS.ReleaseDC (handle, hDC);
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	if (!WGL.SetPixelFormat (hDC, pixelFormat, pfd)) {
		OS.ReleaseDC (handle, hDC);
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	glHandle = WGL.wglCreateContext (hDC);
	if (glHandle == 0) {
		OS.ReleaseDC (handle, hDC);
		SWT.error (SWT.ERROR_NO_HANDLES);
	}
	OS.ReleaseDC (handle, hDC);
}

public boolean isCurrent () {
	checkWidget ();
	return WGL.wglGetCurrentContext () == handle;
}

public void setCurrent () {
	checkWidget ();
	if (WGL.wglGetCurrentContext () == handle) return;
	int hDC = OS.GetDC (handle);
	WGL.wglMakeCurrent (hDC, glHandle);
	OS.ReleaseDC (handle, hDC);
}

public void swapBuffers () {
	checkWidget ();
	int hDC = OS.GetDC(handle);
	WGL.SwapBuffers (hDC);
	OS.ReleaseDC (handle, hDC);
}
}

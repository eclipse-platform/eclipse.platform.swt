/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.opengl.carbon.*;

public class GLCanvas extends Canvas {	
	int context;
	int pixelFormat;
	static final int MAX_ATTRIBUTES = 32;

public GLCanvas (Composite parent, int style, GLData data) {
	super (parent, style);
	if (data == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int aglAttrib [] = new int [MAX_ATTRIBUTES];
	int pos = 0;
	aglAttrib [pos++] = AGL.AGL_RGBA;
	if (data.doubleBuffer) aglAttrib [pos++] = AGL.AGL_DOUBLEBUFFER;
	if (data.stereo) aglAttrib [pos++] = AGL.AGL_STEREO;
	if (data.redSize > 0) {
		aglAttrib [pos++] = AGL.AGL_RED_SIZE;
		aglAttrib [pos++] = data.redSize;
	}
	if (data.greenSize > 0) {
		aglAttrib [pos++] = AGL.AGL_GREEN_SIZE;
		aglAttrib [pos++] = data.greenSize;
	}
	if (data.blueSize > 0) {
		aglAttrib [pos++] = AGL.AGL_BLUE_SIZE;
		aglAttrib [pos++] = data.blueSize;
	}
	if (data.alphaSize > 0) {
		aglAttrib [pos++] = AGL.AGL_ALPHA_SIZE;
		aglAttrib [pos++] = data.alphaSize;
	}
	if (data.depthSize > 0) {
		aglAttrib [pos++] = AGL.AGL_DEPTH_SIZE;
		aglAttrib [pos++] = data.depthSize;
	}
	if (data.stencilSize > 0) {
		aglAttrib [pos++] = AGL.AGL_STENCIL_SIZE;
		aglAttrib [pos++] = data.stencilSize;
	}
	if (data.accumRedSize > 0) {
		aglAttrib [pos++] = AGL.AGL_ACCUM_RED_SIZE;
		aglAttrib [pos++] = data.accumRedSize;
	}
	if (data.accumGreenSize > 0) {
		aglAttrib [pos++] = AGL.AGL_ACCUM_GREEN_SIZE;
		aglAttrib [pos++] = data.accumGreenSize;
	}
	if (data.accumBlueSize > 0) {
		aglAttrib [pos++] = AGL.AGL_ACCUM_BLUE_SIZE;
		aglAttrib [pos++] = data.accumBlueSize;
	}
	if (data.accumAlphaSize > 0) {
		aglAttrib [pos++] = AGL.AGL_ACCUM_ALPHA_SIZE;
		aglAttrib [pos++] = data.accumAlphaSize;
	}
	if (data.sampleBuffers > 0) {
		aglAttrib [pos++] = AGL.AGL_SAMPLE_BUFFERS_ARB;
		aglAttrib [pos++] = data.sampleBuffers;
	}
	if (data.samples > 0) {
		aglAttrib [pos++] = AGL.AGL_SAMPLES_ARB;
		aglAttrib [pos++] = data.samples;
	}
	aglAttrib [pos++] = AGL.AGL_NONE;
	pixelFormat = AGL.aglChoosePixelFormat (0, 0, aglAttrib);
//	context = AGL.aglCreateContext (pixelFormat, share == null ? 0 : share.context);
	context = AGL.aglCreateContext (pixelFormat, 0);
	int window = OS.GetControlOwner (handle);
	int port = OS.GetWindowPort (window);
	AGL.aglSetDrawable (context, port);

	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
			case SWT.Dispose:
				AGL.aglDestroyContext (context);
				break;
			case SWT.Resize:
			case SWT.Hide:
			case SWT.Show:
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						fixBounds();
					}
				});
				break;
			}
		}
	};
	addListener (SWT.Resize, listener);
	Shell shell = getShell();
	shell.addListener(SWT.Resize, listener);
	shell.addListener(SWT.Show, listener);
	shell.addListener(SWT.Hide, listener);
	Control c = this;
	do {
		c.addListener(SWT.Show, listener);
		c.addListener(SWT.Hide, listener);
		c = c.getParent();
	} while (c != shell);
	addListener (SWT.Dispose, listener);
}

void fixBounds () {
	GCData data = new GCData();
	int gc = internal_new_GC(data);
	Rect bounds = new Rect();
	OS.GetRegionBounds (data.visibleRgn, bounds);
	int width = bounds.right - bounds.left;
	int height = bounds.bottom - bounds.top;
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	int port = OS.GetWindowPort (window);
	OS.GetPortBounds (port, rect);
	int [] glbounds = new int [4];
	glbounds[0] = bounds.left;
	glbounds[1] = rect.bottom - rect.top - bounds.top - height;
	glbounds[2] = width;
	glbounds[3] = height;
	AGL.aglSetInteger (context, AGL.AGL_BUFFER_RECT, glbounds);
	AGL.aglEnable (context, AGL.AGL_BUFFER_RECT);
	AGL.aglSetInteger (context, AGL.AGL_CLIP_REGION, data.visibleRgn);
	AGL.aglUpdateContext (context);

	internal_dispose_GC(gc, data);
}

public void swapBuffers () {
	checkWidget ();
	AGL.aglSwapBuffers (context);
}

public boolean isCurrent () {
	checkWidget ();
	return AGL.aglGetCurrentContext () == context;
}

public void setCurrent () {
	checkWidget ();
	if (AGL.aglGetCurrentContext () != context) {
		AGL.aglSetCurrentContext (context);
	}
}
}

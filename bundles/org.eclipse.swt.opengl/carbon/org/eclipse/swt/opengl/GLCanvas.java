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
	int glContext;
	static final int MAX_ATTRIBUTES = 32;

public GLCanvas (Composite parent, int style, GLFormatData data) {
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
	int pixelFormat = AGL.aglChoosePixelFormat (0, 0, aglAttrib);
	glContext = AGL.aglCreateContext (pixelFormat, 0);
	int window = OS.GetControlOwner (handle);
	int port = OS.GetWindowPort (window);
	AGL.aglSetDrawable (glContext, port);

	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
			case SWT.Resize: handleResize (event); break;
			}
		}
	};	
	addListener (SWT.Resize, listener);
}

void handleResize (Event event) {
	Rectangle bounds = getBounds ();
	AGL.aglUpdateContext (glContext);
	int[] glbounds = new int[4];
	glbounds[0] = bounds.x;
	glbounds[1] = bounds.y;
	glbounds[2] = bounds.width;
	glbounds[3] = bounds.height;
	AGL.aglSetInteger (glContext, AGL.AGL_BUFFER_RECT, glbounds);
	AGL.aglEnable (glContext, AGL.AGL_BUFFER_RECT);
}

public boolean isCurrent () {
	return AGL.aglGetCurrentContext () == glContext;
}

public void setCurrent () {
	if (AGL.aglGetCurrentContext () == glContext) return;
	AGL.aglSetCurrentContext (glContext);
}

public void swapBuffers () {
	AGL.aglSwapBuffers (glContext);
}
}

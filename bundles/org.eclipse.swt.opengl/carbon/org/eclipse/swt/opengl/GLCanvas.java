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

/**
 * GLCanvas is a widget capable of displaying OpenGL content.
 */

public class GLCanvas extends Canvas {	
	int context;
	int pixelFormat;
	static final int MAX_ATTRIBUTES = 32;

/**
 * Create a GLCanvas widget using the attributes described in the GLData
 * object provided.
 *
 * @param parent a composite widget
 * @param style the bitwise OR'ing of widget styles
 * @param data the requested attributes of the GLCanvas
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the data is null
 *     <li>ERROR_UNSUPPORTED_DEPTH when the requested attributes cannot be provided</ul> 
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 *     <li>ERROR_INTERFACE_NOT_FOUND when unable to create callbacks for OLE Interfaces
 *     <li>ERROR_INVALID_CLASSID
 * </ul>
 */
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
				AGL.aglDestroyPixelFormat (pixelFormat);
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
	GCData data = new GCData ();
	int gc = internal_new_GC (data);
	Rect bounds = new Rect ();
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
	internal_dispose_GC (gc, data);
}

/**
 * Returns a GLData object describing the created context.
 *  
 * @return GLData description of the OpenGL context attributes
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public GLData getGLData () {
	checkWidget ();
	GLData data = new GLData ();
	int [] value = new int [1];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_DOUBLEBUFFER, value);
	data.doubleBuffer = value [0] != 0;
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_STEREO, value);
	data.stereo = value [0] != 0;
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_RED_SIZE, value);
	data.redSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_GREEN_SIZE, value);
	data.greenSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_BLUE_SIZE, value);
	data.blueSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ALPHA_SIZE, value);
	data.alphaSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_DEPTH_SIZE, value);
	data.depthSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_STENCIL_SIZE, value);
	data.stencilSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_RED_SIZE, value);
	data.accumRedSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_GREEN_SIZE, value);
	data.accumGreenSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_BLUE_SIZE, value);
	data.accumBlueSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_ALPHA_SIZE, value);
	data.accumAlphaSize = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_SAMPLE_BUFFERS_ARB, value);
	data.sampleBuffers = value [0];
	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_SAMPLES_ARB, value);
	data.samples = value [0];
	return data;
}

/**
 * Returns a boolean indicating whether the receiver's OpenGL context
 * is the current context.
 *  
 * @return true if the receiver holds the current OpenGL context,
 * false otherwise
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isCurrent () {
	checkWidget ();
	return AGL.aglGetCurrentContext () == context;
}

/**
 * Sets the OpenGL context associated with this GLCanvas to be the
 * current GL context.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCurrent () {
	checkWidget ();
	if (AGL.aglGetCurrentContext () != context) {
		AGL.aglSetCurrentContext (context);
	}
}

/**
 * Swaps the front and back color buffers.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void swapBuffers () {
	checkWidget ();
	AGL.aglSwapBuffers (context);
}
}

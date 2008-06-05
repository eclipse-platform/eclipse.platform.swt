/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.opengl.GLData;

/**
 * GLCanvas is a widget capable of displaying OpenGL content.
 * 
 * @see GLData
 * @see <a href="http://www.eclipse.org/swt/snippets/#opengl">OpenGL snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.2
 */

public class GLCanvas extends Canvas {
	NSOpenGLView glView;
	NSOpenGLPixelFormat pixelFormat;
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
 * </ul>
 */
public GLCanvas (Composite parent, int style, GLData data) {
	super (parent, style);
	if (data == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int attrib [] = new int [MAX_ATTRIBUTES];
	int pos = 0;
	//TODO this is not working
//	attrib [pos++] = OS.AGL_RGBA;
	if (data.doubleBuffer) attrib [pos++] = OS.NSOpenGLPFADoubleBuffer;
	if (data.stereo) attrib [pos++] = OS.NSOpenGLPFAStereo;
//	if (data.redSize > 0) {
//		attrib [pos++] = OS.AGL_RED_SIZE;
//		attrib [pos++] = data.redSize;
//	}
//	if (data.greenSize > 0) {
//		attrib [pos++] = OS.AGL_GREEN_SIZE;
//		attrib [pos++] = data.greenSize;
//	}
//	if (data.blueSize > 0) {
//		attrib [pos++] = OS.AGL_BLUE_SIZE;
//		attrib [pos++] = data.blueSize;
//	}
	if (data.alphaSize > 0) {
		attrib [pos++] = OS.NSOpenGLPFAAlphaSize;
		attrib [pos++] = data.alphaSize;
	}
	if (data.depthSize > 0) {
		attrib [pos++] = OS.NSOpenGLPFADepthSize;
		attrib [pos++] = data.depthSize;
	}
	if (data.stencilSize > 0) {
		attrib [pos++] = OS.NSOpenGLPFAStencilSize;
		attrib [pos++] = data.stencilSize;
	}
//	if (data.accumRedSize > 0) {
//		attrib [pos++] = OS.AGL_ACCUM_RED_SIZE;
//		attrib [pos++] = data.accumRedSize;
//	}
//	if (data.accumGreenSize > 0) {
//		attrib [pos++] = OS.AGL_ACCUM_GREEN_SIZE;
//		attrib [pos++] = data.accumGreenSize;
//	}
//	if (data.accumBlueSize > 0) {
//		attrib [pos++] = OS.AGL_ACCUM_BLUE_SIZE;
//		attrib [pos++] = data.accumBlueSize;
//	}
//	if (data.accumAlphaSize > 0) {
//		attrib [pos++] = OS.AGL_ACCUM_ALPHA_SIZE;
//		attrib [pos++] = data.accumAlphaSize;
//	}
	if (data.sampleBuffers > 0) {
		attrib [pos++] = OS.NSOpenGLPFASampleBuffers;
		attrib [pos++] = data.sampleBuffers;
	}
	if (data.samples > 0) {
		attrib [pos++] = OS.NSOpenGLPFASamples;
		attrib [pos++] = data.samples;
	}
	attrib [pos++] = 0;
	pixelFormat = (NSOpenGLPixelFormat)new NSOpenGLPixelFormat().alloc();
	if (pixelFormat == null) {		
		dispose ();
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	pixelFormat.initWithAttributes(attrib);
	
	glView = (NSOpenGLView)new NSOpenGLView().alloc();
	if (glView == null) {		
		dispose ();
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	glView.initWithFrame(parent.view.bounds(), pixelFormat);
	glView.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
	parent.view.addSubview_(glView);

	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose:
					if (glView != null) {
						glView.clearGLContext();
						glView.release();
					}
					glView = null;
					if (pixelFormat != null) pixelFormat.release();
					pixelFormat = null;
					break;
			}
		}
	};
	addListener (SWT.Dispose, listener);
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
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_DOUBLEBUFFER, value);
//	data.doubleBuffer = value [0] != 0;
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_STEREO, value);
//	data.stereo = value [0] != 0;
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_RED_SIZE, value);
//	data.redSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_GREEN_SIZE, value);
//	data.greenSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_BLUE_SIZE, value);
//	data.blueSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ALPHA_SIZE, value);
//	data.alphaSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_DEPTH_SIZE, value);
//	data.depthSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_STENCIL_SIZE, value);
//	data.stencilSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_RED_SIZE, value);
//	data.accumRedSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_GREEN_SIZE, value);
//	data.accumGreenSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_BLUE_SIZE, value);
//	data.accumBlueSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_ACCUM_ALPHA_SIZE, value);
//	data.accumAlphaSize = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_SAMPLE_BUFFERS_ARB, value);
//	data.sampleBuffers = value [0];
//	AGL.aglDescribePixelFormat (pixelFormat, AGL.AGL_SAMPLES_ARB, value);
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
	return NSOpenGLContext.currentContext().id == glView.openGLContext().id;
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
	glView.openGLContext().makeCurrentContext();
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
	glView.openGLContext().flushBuffer();
}
}

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
package org.eclipse.swt.internal.opengl.carbon;

import org.eclipse.swt.internal.*;

public class AGL extends Platform {
	static {
		Library.loadLibrary("swt-agl");
	}

	/* Attributes */
	public static final int AGL_NONE = 0;
	public static final int AGL_BUFFER_SIZE = 2;
	public static final int AGL_LEVEL = 3;
	public static final int AGL_RGBA = 4;
	public static final int AGL_DOUBLEBUFFER = 5;
	public static final int AGL_STEREO = 6;
	public static final int AGL_AUX_BUFFERS = 7;
	public static final int AGL_RED_SIZE = 8;
	public static final int AGL_GREEN_SIZE = 9;
	public static final int AGL_BLUE_SIZE = 10;
	public static final int AGL_ALPHA_SIZE = 11;
	public static final int AGL_DEPTH_SIZE = 12;
	public static final int AGL_STENCIL_SIZE = 13;
	public static final int AGL_ACCUM_RED_SIZE = 14;
	public static final int AGL_ACCUM_GREEN_SIZE = 15;
	public static final int AGL_ACCUM_BLUE_SIZE = 16;
	public static final int AGL_ACCUM_ALPHA_SIZE = 17;
	
	public static final int AGL_SAMPLE_BUFFERS_ARB = 55;
	public static final int AGL_SAMPLES_ARB = 56;
	
	/* Integer parameters */
	public static final int AGL_BUFFER_RECT = 202;
	public static final int AGL_SWAP_INTERVAL = 222;
	public static final int AGL_BUFFER_NAME = 231;
	public static final int AGL_CLIP_REGION = 254;

/**
 * @param gdevs cast=(const AGLDevice *)
 * @param attribs cast=(const GLint *)
 */
public static final native int aglChoosePixelFormat(int gdevs, int ndev, int[] attribs);
/**
 * @param pix cast=(AGLPixelFormat)
 * @param share cast=(AGLContext)
 */
public static final native int aglCreateContext(int pix, int share);
/**
 * @param pix cast=(AGLPixelFormat)
 * @param attrib cast=(GLint)
 * @param value cast=(GLint *)
 */
public static final native boolean aglDescribePixelFormat(int pix, int attrib, int[] value);
/** @param ctx cast=(AGLContext) */
public static final native boolean aglDestroyContext(int ctx);
/** @param pix cast=(AGLPixelFormat) */
public static final native void aglDestroyPixelFormat(int pix);
/**
 * @param ctx cast=(AGLContext)
 * @param pname cast=(GLenum)
 */
public static final native boolean aglEnable(int ctx, int pname);
public static final native int aglGetCurrentContext();
/** @param ctx cast=(AGLContext) */
public static final native int aglGetDrawable(int ctx);
/** @param ctx cast=(AGLContext) */
public static final native boolean aglSetCurrentContext(int ctx);
/**
 * @param ctx cast=(AGLContext)
 * @param draw cast=(AGLDrawable)
 */
public static final native boolean aglSetDrawable(int ctx, int draw);
/**
 * @param ctx cast=(AGLContext)
 * @param pname cast=(GLenum)
 * @param params cast=(const GLint *)
 */
public static final native boolean aglSetInteger(int ctx, int pname, int[] params);
/**
 * @param ctx cast=(AGLContext)
 * @param pname cast=(GLenum)
 * @param param cast=(const GLint *)
 */
public static final native boolean aglSetInteger(int ctx, int pname, int param);
/** @param ctx cast=(AGLContext) */
public static final native void aglSwapBuffers(int ctx);
/** @param ctx cast=(AGLContext) */
public static final native boolean aglUpdateContext(int ctx);
}

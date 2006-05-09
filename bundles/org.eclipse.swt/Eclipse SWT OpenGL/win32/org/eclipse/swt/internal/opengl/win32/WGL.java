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
package org.eclipse.swt.internal.opengl.win32;

import org.eclipse.swt.internal.Library;

public class WGL {	
	static {
		Library.loadLibrary("swt-wgl");
	}
	
	public static final int WGL_FONT_LINES      = 0;
	public static final int WGL_FONT_POLYGONS   = 1;
	
	/* LAYERPLANEDESCRIPTOR flags */
	public static final int LPD_DOUBLEBUFFER        = 0x00000001;
	public static final int LPD_STEREO              = 0x00000002;
	public static final int LPD_SUPPORT_GDI         = 0x00000010;
	public static final int LPD_SUPPORT_OPENGL      = 0x00000020;
	public static final int LPD_SHARE_DEPTH         = 0x00000040;
	public static final int LPD_SHARE_STENCIL       = 0x00000080;
	public static final int LPD_SHARE_ACCUM         = 0x00000100;
	public static final int LPD_SWAP_EXCHANGE       = 0x00000200;
	public static final int LPD_SWAP_COPY           = 0x00000400;
	public static final int LPD_TRANSPARENT         = 0x00001000;
	
	public static final int LPD_TYPE_RGBA        = 0;
	public static final int LPD_TYPE_COLORINDEX  = 1;
	
	/* wglSwapLayerBuffers flags */
	public static final int WGL_SWAP_MAIN_PLANE     = 0x00000001;
	public static final int WGL_SWAP_OVERLAY1       = 0x00000002;
	public static final int WGL_SWAP_OVERLAY2       = 0x00000004;
	public static final int WGL_SWAP_OVERLAY3       = 0x00000008;
	public static final int WGL_SWAP_OVERLAY4       = 0x00000010;
	public static final int WGL_SWAP_OVERLAY5       = 0x00000020;
	public static final int WGL_SWAP_OVERLAY6       = 0x00000040;
	public static final int WGL_SWAP_OVERLAY7       = 0x00000080;
	public static final int WGL_SWAP_OVERLAY8       = 0x00000100;
	public static final int WGL_SWAP_OVERLAY9       = 0x00000200;
	public static final int WGL_SWAP_OVERLAY10      = 0x00000400;
	public static final int WGL_SWAP_OVERLAY11      = 0x00000800;
	public static final int WGL_SWAP_OVERLAY12      = 0x00001000;
	public static final int WGL_SWAP_OVERLAY13      = 0x00002000;
	public static final int WGL_SWAP_OVERLAY14      = 0x00004000;
	public static final int WGL_SWAP_OVERLAY15      = 0x00008000;
	public static final int WGL_SWAP_UNDERLAY1      = 0x00010000;
	public static final int WGL_SWAP_UNDERLAY2      = 0x00020000;
	public static final int WGL_SWAP_UNDERLAY3      = 0x00040000;
	public static final int WGL_SWAP_UNDERLAY4      = 0x00080000;
	public static final int WGL_SWAP_UNDERLAY5      = 0x00100000;
	public static final int WGL_SWAP_UNDERLAY6      = 0x00200000;
	public static final int WGL_SWAP_UNDERLAY7      = 0x00400000;
	public static final int WGL_SWAP_UNDERLAY8      = 0x00800000;
	public static final int WGL_SWAP_UNDERLAY9      = 0x01000000;
	public static final int WGL_SWAP_UNDERLAY10     = 0x02000000;
	public static final int WGL_SWAP_UNDERLAY11     = 0x04000000;
	public static final int WGL_SWAP_UNDERLAY12     = 0x08000000;
	public static final int WGL_SWAP_UNDERLAY13     = 0x10000000;
	public static final int WGL_SWAP_UNDERLAY14     = 0x20000000;
	public static final int WGL_SWAP_UNDERLAY15     = 0x40000000;
	
	/* pixel types */
	public static final int PFD_TYPE_RGBA        = 0;
	public static final int PFD_TYPE_COLORINDEX  = 1;
	
	/* layer types */
	public static final int PFD_MAIN_PLANE       = 0;
	public static final int PFD_OVERLAY_PLANE    = 1;
	public static final int PFD_UNDERLAY_PLANE   = (-1);
	
	/* PIXELFORMATDESCRIPTOR flags */
	public static final int PFD_DOUBLEBUFFER            = 0x00000001;
	public static final int PFD_STEREO                  = 0x00000002;
	public static final int PFD_DRAW_TO_WINDOW          = 0x00000004;
	public static final int PFD_DRAW_TO_BITMAP          = 0x00000008;
	public static final int PFD_SUPPORT_GDI             = 0x00000010;
	public static final int PFD_SUPPORT_OPENGL          = 0x00000020;
	public static final int PFD_GENERIC_FORMAT          = 0x00000040;
	public static final int PFD_NEED_PALETTE            = 0x00000080;
	public static final int PFD_NEED_SYSTEM_PALETTE     = 0x00000100;
	public static final int PFD_SWAP_EXCHANGE           = 0x00000200;
	public static final int PFD_SWAP_COPY               = 0x00000400;
	public static final int PFD_SWAP_LAYER_BUFFERS      = 0x00000800;
	public static final int PFD_GENERIC_ACCELERATED     = 0x00001000;
	public static final int PFD_SUPPORT_DIRECTDRAW      = 0x00002000;
	
	/* PIXELFORMATDESCRIPTOR flags for use in ChoosePixelFormat only */
	public static final int PFD_DEPTH_DONTCARE          = 0x20000000;
	public static final int PFD_DOUBLEBUFFER_DONTCARE   = 0x40000000;
	public static final int PFD_STEREO_DONTCARE         = 0x80000000;

public static final native int ChoosePixelFormat(int hdc, PIXELFORMATDESCRIPTOR ppfd);
public static final native int DescribePixelFormat(int hdc, int iPixelFormat, int nBytes, PIXELFORMATDESCRIPTOR ppfd);
public static final native int GetPixelFormat(int hdc);
public static final native boolean SetPixelFormat(int hdc, int iPixelFormat, PIXELFORMATDESCRIPTOR ppfd);
public static final native boolean SwapBuffers(int hdc);
public static final native boolean wglCopyContext(int hglrcSrc, int hglrcDst, int mask);
public static final native int wglCreateContext(int hdc);
public static final native int wglCreateLayerContext(int hdc, int iLayerPlane);
public static final native boolean wglDeleteContext(int hglrc);
public static final native int wglGetCurrentContext();
public static final native int wglGetCurrentDC();
public static final native int wglGetProcAddress(byte[] lpszProc);
public static final native boolean wglMakeCurrent(int hdc, int hglrc);
public static final native boolean wglShareLists(int hglrc1, int hglrc2);
public static final native boolean wglDescribeLayerPlane(int hdc, int iPixelFormat, int iLayerPlane, int nBytes, LAYERPLANEDESCRIPTOR plpd);
public static final native int wglSetLayerPaletteEntries(int hdc, int iLayerPlane, int iStart, int cEntries, int[] pcr);
public static final native int wglGetLayerPaletteEntries(int hdc, int iLayerPlane, int iStart, int cEntries, int[] pcr);
public static final native boolean wglRealizeLayerPalette(int hdc, int iLayerPlane, boolean bRealize);
public static final native boolean wglSwapLayerBuffers(int hdc, int fuPlanes);
}

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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.internal.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of <code>GLContext</code> are used to draw on swt <code>Drawable</code>s 
 * through invocations of provided OpenGL functions.
 * <p>
 * Application code must explicitly invoke the <code>GLContext.dispose ()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 */
public class GLContext {
	int handle;
	int hDC;
	GCData data;
	Drawable drawable;
	int selectBufferPtr = 0;

	static final int MSB_FIRST = 1;
	static final int LSB_FIRST = 2;

	/**
	 * Constructs a new instance of this class which has been
	 * configured to draw on the specified drawable.
	 * <p>
	 * You must dispose the <code>GLContext</code> when it is no longer required. 
	 * </p>
	 * 
	 * @param drawable the drawable to draw on
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
	 *    <li>ERROR_UNSUPPORTED_DEPTH - if the current display depth is not supported</li>
	 * </ul>
	 */
	public GLContext (Drawable drawable) {
		if (drawable == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		this.drawable = drawable;
		this.data = new GCData ();
		hDC = drawable.internal_new_GC (data);
		if (hDC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
		int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
		int depth = bits * planes;
		PIXELFORMATDESCRIPTOR pfd = new PIXELFORMATDESCRIPTOR ();
		pfd.nSize = (short) PIXELFORMATDESCRIPTOR.sizeof;
		pfd.nVersion = 1;
		pfd.dwFlags =
			WGL.PFD_DRAW_TO_WINDOW
				| WGL.PFD_SUPPORT_OPENGL
				| WGL.PFD_DOUBLEBUFFER;
		pfd.dwLayerMask = WGL.PFD_MAIN_PLANE;
		pfd.iPixelType = (byte) WGL.PFD_TYPE_RGBA;
		pfd.cColorBits = (byte) depth;
		pfd.cDepthBits = (byte) depth;
		pfd.cAccumBits = 0;
		pfd.cStencilBits = 0;
		int pixelFormat = WGL.ChoosePixelFormat (hDC, pfd);
		if (pixelFormat == 0) {
			drawable.internal_dispose_GC (hDC, data);
			SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		if (!WGL.SetPixelFormat (hDC, pixelFormat, pfd)) {
			drawable.internal_dispose_GC (hDC, data);
			SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		handle = WGL.wglCreateContext (hDC);
		if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	}

	public ImageData convertImageData (ImageData source) {
		PaletteData palette = new PaletteData (0xff0000, 0xff00, 0xff);
		ImageData newSource = new ImageData (source.width, source.height, 24, palette);

		ImageDataUtil.blit (
			1,
			source.data,
			source.depth,
			source.bytesPerLine,
			(source.depth != 16) ? MSB_FIRST : LSB_FIRST,
			0,
			0,
			source.width,
			source.height,
			source.palette.redMask,
			source.palette.greenMask,
			source.palette.blueMask,
			255,
			null,
			0,
			0,
			0,
			newSource.data,
			newSource.depth,
			newSource.bytesPerLine,
			(newSource.depth != 16) ? MSB_FIRST : LSB_FIRST,
			0,
			0,
			newSource.width,
			newSource.height,
			newSource.palette.redMask,
			newSource.palette.greenMask,
			newSource.palette.blueMask,
			false,
			true);

		return newSource;
	}

	/**
	 * Disposes of the operating system resources associated with
	 * the receiver. Applications must dispose of all <code>GLContext</code>s
	 * that they allocate.
	 */
	public void dispose () {
		if (handle == 0) return;
		if (WGL.wglGetCurrentContext () == handle) {
			WGL.wglMakeCurrent (0, 0);
		}
		WGL.wglDeleteContext (handle);
		handle = 0;

		// drawable may be disposed
		try {
			drawable.internal_dispose_GC (hDC, data);
		} catch (SWTException e) {
		}
		int hHeap = OS.GetProcessHeap ();
		if (selectBufferPtr != 0) {
			OS.HeapFree (hHeap, 0, selectBufferPtr);
		}
		hDC = 0;
		drawable = null;
		data.device = null;
		data = null;
	}

	public int[] getSelectBuffer (int selectBufferPtr, int[] selectBuffer) {
		OS.MoveMemory (selectBuffer, selectBufferPtr, selectBuffer.length * 4);
		return selectBuffer;
	}

	public int getSelectBufferPtr (int[] selectBuffer) {
		if (selectBufferPtr == 0) {
			int hHeap = OS.GetProcessHeap ();
			selectBufferPtr =
				OS.HeapAlloc (
					hHeap,
					OS.HEAP_ZERO_MEMORY,
					selectBuffer.length * 4);
		}
		OS.MoveMemory (selectBufferPtr, selectBuffer, selectBuffer.length * 4);
		return selectBufferPtr;
	}

	/**
	 * Returns a boolean indicating whether the receiver is the current
	 * <code>GLContext</code>.
	 *  
	 * @return true if the receiver is the current <code>GLContext</code>,
	 * false otherwise
	 * @exception SWTError <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED if the receiver is disposed</li>
	 * </ul>
	 */
	public boolean isCurrent () {
		if (isDisposed ()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
		return WGL.wglGetCurrentContext () == handle;
	}

	/**
	 * Returns a boolean indicating whether the <code>GLContext</code> has been
	 * disposed.
	 * <p>
	 * This method gets the dispose state for the <code>GLContext</code>.
	 * When a <code>GLContext</code> has been disposed, it is an error to
	 * invoke any other method using the <code>GLContext</code>.
	 *
	 * @return true if the <code>GLContext</code> is disposed, false otherwise
	 */
	public boolean isDisposed () {
		return handle == 0;
	}

	/**
	 * Loads the specified bitmap font.
	 * 
	 * @param fdata
	 * @param device
	 * @param base
	 * @param first
	 * @param count
	 */
	public void loadBitmapFont (FontData fdata,	Device device, int base, int first,	int count) {
		Font font = new Font (device, fdata);
		int oldFont = OS.SelectObject (hDC, font.handle);
		WGL.wglUseFontBitmaps (hDC, first, count, base);
		OS.SelectObject (hDC, oldFont);
		font.dispose ();
	}

	/**
	 * Loads the specified outline font.
	 * 
	 * @param fdata
	 * @param device
	 * @param base
	 * @param first
	 * @param count
	 * @param deviation
	 * @param extrusion
	 * @param format
	 * @param lpgmf
	 */
	public void loadOutlineFont (FontData fdata, Device device, int base, int first,
	int count, float deviation, float extrusion, int format, GLYPHMETRICSFLOAT[] lpgmf) {
		int ptr = 0;
		int hHeap = 0;
		if (lpgmf != null && lpgmf.length == count + 1) {
			hHeap = OS.GetProcessHeap ();
			ptr =
				OS.HeapAlloc (
					hHeap,
					OS.HEAP_ZERO_MEMORY,
					count * GLYPHMETRICSFLOAT.sizeof);
		}

		if (format == GL.GL_POLYGON) {
			format = WGL.WGL_FONT_POLYGONS;
		} 
		if (format == GL.GL_LINE) {
			format = WGL.WGL_FONT_LINES;
		} 

		Font font = new Font (device, fdata);
		int oldFont = OS.SelectObject (hDC, font.handle);
		WGL.wglUseFontOutlines (hDC, first, count, base, deviation, extrusion, format, ptr);
		OS.SelectObject (hDC, oldFont);
		font.dispose ();
		if (ptr != 0) {
			int sizeof = GLYPHMETRICSFLOAT.sizeof;
			for (int i = 0; i < lpgmf.length; i++) {
				WGL.MoveMemory (lpgmf [i], ptr + (sizeof * i), sizeof);
			};
			OS.HeapFree (hHeap, 0, ptr);
		}
	}

	/**
	 * Resizes the receiver.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void resize (int x, int y, int width, int height) {
		if (height == 0) height = 1;
		GL.glViewport (0, 0, width, height);
		float nRange = 200.0f;
		GL.glMatrixMode (GL.GL_PROJECTION);
		GL.glLoadIdentity ();
		// load projection
		GLU.gluPerspective (45.0f, (float) width / (float) height, 0.1f, 100.0f);
		GL.glMatrixMode (GL.GL_MODELVIEW);
		GL.glLoadIdentity ();
	}

	/**
	 * Sets the receiver to be the current <code>GLContext</code>.
	 * 
 	 * @exception SWTError <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED if the receiver is disposed</li>
	 * </ul>
	 */
	public void setCurrent () {
		if (isDisposed ()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
		if (WGL.wglGetCurrentContext () == handle) return;
		WGL.wglMakeCurrent (hDC, handle);
	}

	/**
	 * Swaps the receiver's buffers.
	 * 
	 * @exception SWTError <ul>
	 *    <li>ERROR_GRAPHIC_DISPOSED if the receiver is disposed</li>
	 * </ul>
	 */
	public void swapBuffers () {
		if (isDisposed ()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
		WGL.SwapBuffers (hDC);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.opengl.internal.motif.*;

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
	int gc;
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
		gc = drawable.internal_new_GC (data);
		if (gc == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int xDisplay = data.display;
		int screen = OS.XDefaultScreen (xDisplay);
		int depth = OS.XDefaultDepthOfScreen (OS.XDefaultScreenOfDisplay (xDisplay));
		int attrib [] = {
			XGL.GLX_LEVEL,
			0,
			XGL.GLX_RGBA,
			XGL.GLX_DOUBLEBUFFER,
//			XGL.GLX_DEPTH_SIZE,
//			depth,
			0
		};
		int infoPtr = XGL.glXChooseVisual (xDisplay, screen, attrib);
		if (infoPtr == 0) SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
		XVisualInfo info = new XVisualInfo ();
		XGL.memmove (info, infoPtr, XVisualInfo.sizeof);
		OS.XFree (infoPtr);
		handle = XGL.glXCreateContext (xDisplay, info, 0, false);
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
		int xDisplay = data.display;
		if (XGL.glXGetCurrentContext () == handle) {
			XGL.glXMakeCurrent (xDisplay, 0, 0);
		}
		if (selectBufferPtr != 0) OS.XtFree (selectBufferPtr);
		XGL.glXDestroyContext (xDisplay, handle);
		handle = 0;
		// drawable may be disposed
		try {
			drawable.internal_dispose_GC (gc, data);
		} catch (SWTException e) {
		}
		gc = 0;
		data.display = data.drawable = data.colormap = 0;
		/*data.fontList =*/ data.clipRgn = data.renderTable = 0;
		drawable = null;
		data.device = null;
		data.image = null;
		//data.codePage = null;
		data = null;
	}

	public int[] getSelectBuffer (int selectBufferPtr, int[] selectBuffer) {
		OS.memmove (selectBuffer, selectBufferPtr, selectBuffer.length * 4);
		return selectBuffer;
	}

	public int getSelectBufferPtr (int[] selectBuffer) {
		if (selectBufferPtr == 0) {
			selectBufferPtr = OS.XtMalloc (selectBuffer.length * 4);
		}
		OS.memmove (selectBufferPtr, selectBuffer, selectBuffer.length * 4);
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
		if (isDisposed ()) 	SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
		return XGL.glXGetCurrentContext () == handle;
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
	public void loadBitmapFont (FontData fdata, Device device, int base, int startIndex, int length) {
		/* Temporary code, due some problems when running on UTF-8 loadBitmapFont ()
		 * is restrict to works only for ascii. 
		 * Note: en_US.ISO8859-1 also code be used.
		 */
		fdata.setLocale ("C");
		Font font = new Font (device, fdata);
		int fontList = font.handle;
		int[] buffer = new int [1];
		if (!OS.XmFontListInitFontContext (buffer, fontList)) return;
		int context = buffer [0];
		XFontStruct fontStruct = new XFontStruct ();
		int fontListEntry;
		int[] fontStructPtr = new int [1];
		int[] fontNamePtr = new int [1];
		int xfont = 0;
		// go through each entry in the font list
		while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
			int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
			if (buffer [0] == OS.XmFONT_IS_FONT) {
				// FontList contains a single font
				OS.memmove (fontStruct, fontPtr, 20 * 4);
				xfont = fontStruct.fid;
			} else {
				// FontList contains a fontSet
				int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
				int[] fontStructs = new int [nFonts];
				OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
				// Go through each fontStruct in the font set.
				for (int i = 0; i < nFonts; i++) {
					OS.memmove (fontStruct, fontStructs [i], XFontStruct.sizeof);
					xfont = fontStruct.fid;
				}
			}
		}
		if (xfont != 0) {
			XGL.glXUseXFont (xfont, startIndex, length, base);
		}
		font.dispose ();
		OS.XmFontListFreeFontContext (context);
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
		// stub
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
		GL.glViewport (x, y, width, height);
		GL.glMatrixMode (GL.GL_PROJECTION);
		GL.glLoadIdentity ();
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
		if (XGL.glXGetCurrentContext () == handle) return;
		XGL.glXMakeCurrent (data.display, data.drawable, handle);
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
		XGL.glXSwapBuffers (data.display, data.drawable);
	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.opengl.internal.gtk.*;
import org.eclipse.swt.widgets.*;

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
	Widget widget;
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
		this.data = new GCData ();
		gc = drawable.internal_new_GC (data);
		if (gc == 0) SWT.error (SWT.ERROR_NO_HANDLES);

//		int attrib [] = {
////			GTKGLEXT.GLX_LEVEL,
////			0,
////			GTKGLEXT.GDK_GL_MODE_RGBA,
//			GTKGLEXT.GDK_GL_MODE_DOUBLE,
//			GTKGLEXT.GDK_GL_MODE_DEPTH,
//			24,//			depth,
//			0
//		};

		// adding to next line causes badness: XGL.GDK_GL_MODE_INDEX |
		int mode = GTKGLEXT.GDK_GL_MODE_RGB | GTKGLEXT.GDK_GL_MODE_DOUBLE | GTKGLEXT.GDK_GL_MODE_DEPTH;
		int glconfig = GTKGLEXT.gdk_gl_config_new_by_mode (mode);
//		int glconfig = GTKGLEXT.gdk_gl_config_new (attrib);
		// for now assume that the Drawable is a Widget
		this.widget = (Widget) drawable;

		OS.gtk_widget_unrealize (widget.handle);
		boolean success = GTKGLEXT.gtk_widget_set_gl_capability (
			widget.handle,
			glconfig,
			0,
			true,
			GTKGLEXT.GDK_GL_RGBA_TYPE);
		OS.gtk_widget_realize(widget.handle);
		
		handle = GTKGLEXT.gtk_widget_create_gl_context (widget.handle, 0, true, GTKGLEXT.GDK_GL_RGBA_TYPE);
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

// the following causes problems
//		int current = GTKGLEXT.gdk_gl_context_get_current ();  
//		if (current == handle) {
//			GTKGLEXT.gdk_gl_drawable_make_current (0, 0);
//		}
		
//		int xDisplay = data.display;
//		if (XGL.glXGetCurrentContext () == handle) {
//			XGL.glXMakeCurrent (xDisplay, 0, 0);
//		}
		if (selectBufferPtr != 0) OS.g_free(selectBufferPtr);
		GTKGLEXT.gdk_gl_context_destroy (handle); 		
//		XGL.glXDestroyContext (xDisplay, handle);

		handle = 0;
//		gdkDrawable = 0;
		// drawable may be disposed
		try {
			((Drawable)widget).internal_dispose_GC (gc, data);
		} catch (SWTException e) {
		}
		gc = 0;
		data.context = data.drawable = data.layout = 0;
		data.font = data.clipRgn = data.lineStyle = 0;
		widget = null;
		data.device = null;
		data.image = null;
		data = null;
	}

	public int[] getSelectBuffer (int selectBufferPtr, int[] selectBuffer) {
		OS.memmove (selectBuffer, selectBufferPtr, selectBuffer.length * 4);
		return selectBuffer;
	}

	public int getSelectBufferPtr (int[] selectBuffer) {
		if (selectBufferPtr == 0) {
			selectBufferPtr = OS.g_malloc (selectBuffer.length * 4);
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
		return GTKGLEXT.gdk_gl_context_get_current () == handle;  
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
		Font font = new Font (device, fdata);
//		int oldFont = OS.SelectObject (hDC, font.handle);
		GTKGLEXT.gdk_gl_font_use_pango_font (font.handle, startIndex, length, base);
//		OS.SelectObject (hDC, oldFont);
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
		int currentContext = GTKGLEXT.gdk_gl_context_get_current ();
		if (currentContext == handle) return;
		if (currentContext != 0) {
			int drawable = GTKGLEXT.gdk_gl_context_get_gl_drawable(currentContext);
			GTKGLEXT.gdk_gl_drawable_gl_end (drawable);
		}
		int gdkDrawable = GTKGLEXT.gtk_widget_get_gl_drawable (widget.handle);
		GTKGLEXT.gdk_gl_drawable_gl_begin (gdkDrawable, handle);
		GTKGLEXT.gdk_gl_drawable_make_current (gdkDrawable, handle);
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
		int gdkDrawable = GTKGLEXT.gtk_widget_get_gl_drawable (widget.handle); 
		GTKGLEXT.gdk_gl_drawable_swap_buffers (gdkDrawable);
	}
}

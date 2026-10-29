/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.internal.opengl.glx.*;
import org.eclipse.swt.widgets.*;

/**
 * GLCanvas is a widget capable of displaying OpenGL content.
 *
 * @see GLData
 * @see <a href="https://eclipse.dev/eclipse/swt/snippets/#opengl">OpenGL snippets</a>
 * @see <a href="https://eclipse.dev/eclipse/swt/">Sample code and further information</a>
 *
 * @since 3.2
 */

public class GLCanvas extends Canvas {
	long context;
	long xWindow;
	long glWindow;
	XVisualInfo vinfo;
	static final int MAX_ATTRIBUTES = 32;

	/*
	 * On Wayland and on GTK4 there is no GLX, so a GtkGLArea is used instead.
	 * It renders into its own framebuffer object, which GTK composites into
	 * the window. It has 8 bit color channels and optional 24 bit depth and
	 * 8 bit stencil buffers, so requesting more, or accumulation or stereo
	 * buffers, fails. Multisampling is a preference, so a request for it
	 * is ignored and getGLData() reports none. The shareContext of the
	 * GLData is ignored.
	 *
	 * GDK only creates core profile contexts, so the fixed function pipeline
	 * (glBegin, glColor, the matrix stack) is not available. On GTK3 the
	 * GDK_GL=legacy environment variable makes GDK create compatibility
	 * profile contexts instead; GTK4 has no equivalent. The default
	 * framebuffer is the one of the area, so binding framebuffer 0 or
	 * selecting GL_BACK with glDrawBuffer does not draw to the screen.
	 */
	long glArea;
	Callback renderCallback, resizeCallback;
	int renderHandler, resizeHandler;
	boolean hasAlpha, hasDepth, hasStencil;
	boolean inRender, swapPending, resized, renderQueued;

/**
 * Create a GLCanvas widget using the attributes described in the GLData
 * object provided.
 *
 * @param parent a composite widget
 * @param style the bitwise OR'ing of widget styles
 * @param data the requested attributes of the GLCanvas
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the data is null</li>
 *     <li>ERROR_UNSUPPORTED_DEPTH when the requested attributes cannot be provided</li>
 * </ul>
 */
public GLCanvas (Composite parent, int style, GLData data) {
	super (parent, style);
	if (data == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (GTK.GTK4 || OS.isWayland ()) {
		createGLArea (data);
	} else {
		createGLX (data);
	}
}

void createGLArea (GLData data) {
	if (data.redSize > 8 || data.greenSize > 8 || data.blueSize > 8 || data.alphaSize > 8
			|| data.depthSize > 24 || data.stencilSize > 8 || data.stereo
			|| data.accumRedSize > 0 || data.accumGreenSize > 0 || data.accumBlueSize > 0 || data.accumAlphaSize > 0) {
		dispose ();
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	/* The area always has alpha on GTK4 */
	hasAlpha = GTK.GTK4 || data.alphaSize > 0;
	hasDepth = data.depthSize > 0;
	hasStencil = data.stencilSize > 0;
	glArea = GTK.gtk_gl_area_new ();
	if (glArea == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (!GTK.GTK4) GTK3.gtk_gl_area_set_has_alpha (glArea, hasAlpha);
	GTK.gtk_gl_area_set_has_depth_buffer (glArea, hasDepth);
	GTK.gtk_gl_area_set_has_stencil_buffer (glArea, hasStencil);
	if (GTK.GTK4) {
		/* GDK prefers OpenGL ES, but GLCanvas users expect desktop OpenGL */
		if (GTK.GTK_VERSION >= OS.VERSION (4, 12, 0)) GTK4.gtk_gl_area_set_allowed_apis (glArea, GTK4.GDK_GL_API_GL);
		OS.swt_fixed_add (handle, glArea);
		/* Let the input go to the canvas */
		GTK4.gtk_widget_set_can_target (glArea, false);
	} else {
		/* The input only window of the area selects no events, so they reach the canvas */
		GTK3.gtk_container_add (handle, glArea);
	}
	GTK.gtk_widget_set_visible (glArea, true);

	renderCallback = new Callback (this, "renderProc", 3); //$NON-NLS-1$
	resizeCallback = new Callback (this, "resizeProc", 4); //$NON-NLS-1$
	renderHandler = OS.g_signal_connect (glArea, Converter.wcsToMbcs ("render", true), renderCallback.getAddress (), 0); //$NON-NLS-1$
	resizeHandler = OS.g_signal_connect (glArea, Converter.wcsToMbcs ("resize", true), resizeCallback.getAddress (), 0); //$NON-NLS-1$

	Listener listener = event -> {
		switch (event.type) {
		case SWT.Paint:
			/*
			* The content of the area is only kept until GTK draws it, so
			* paint only from the render signal and drop the paint of the
			* canvas itself, which would draw into a buffer GTK replaces.
			*/
			if (!inRender) event.type = SWT.None;
			break;
		case SWT.Resize:
			Rectangle clientArea = getClientArea ();
			OS.swt_fixed_move (handle, glArea, clientArea.x, clientArea.y);
			OS.swt_fixed_resize (handle, glArea, clientArea.width, clientArea.height);
			if (GTK.GTK4) {
				/* The canvas is allocated before the Resize event, so allocate the area too */
				GtkAllocation allocation = new GtkAllocation ();
				allocation.x = clientArea.x;
				allocation.y = clientArea.y;
				allocation.width = clientArea.width;
				allocation.height = clientArea.height;
				GTK.gtk_widget_get_preferred_size (glArea, null, null);
				GTK4.gtk_widget_size_allocate (glArea, allocation, -1);
			}
			break;
		case SWT.MouseDown:
			/*
			* On GTK3 a click only focuses a canvas without native children,
			* and the area is one, so focus the canvas here as Composite does.
			*/
			if (event.button == 1 && (getStyle () & SWT.NO_FOCUS) == 0
					&& (isListening (SWT.KeyDown) || isListening (SWT.KeyUp)) && getChildren ().length == 0) {
				setFocus ();
			}
			break;
		case SWT.Dispose:
			/* The area itself is destroyed together with the canvas */
			OS.g_signal_handler_disconnect (glArea, renderHandler);
			OS.g_signal_handler_disconnect (glArea, resizeHandler);
			renderCallback.dispose ();
			resizeCallback.dispose ();
			break;
		}
	};
	addListener (SWT.Paint, listener);
	addListener (SWT.Resize, listener);
	if (!GTK.GTK4) addListener (SWT.MouseDown, listener);
	addListener (SWT.Dispose, listener);

	GTK.gtk_widget_realize (glArea);
	if (GTK.gtk_gl_area_get_error (glArea) != 0 || GTK.gtk_gl_area_get_context (glArea) == 0) {
		dispose ();
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
}

long renderProc (long area, long context, long user_data) {
	boolean drawn = swapPending && !resized;
	swapPending = resized = false;
	/* The application already drew the frame through setCurrent() and swapBuffers() */
	if (drawn) return 1;
	Image image = new Image (getDisplay (), 1, 1);
	GC gc = new GC (image);
	Event event = new Event ();
	event.gc = gc;
	event.count = 1;
	Rectangle clientArea = getClientArea ();
	event.width = clientArea.width;
	event.height = clientArea.height;
	inRender = true;
	try {
		notifyListeners (SWT.Paint, event);
	} finally {
		inRender = false;
		gc.dispose ();
		image.dispose ();
	}
	return 1;
}

long resizeProc (long area, long width, long height, long user_data) {
	/* The buffers have been reallocated and lost their content */
	resized = true;
	return 0;
}

void createGLX (GLData data) {
	int glxAttrib [] = new int [MAX_ATTRIBUTES];
	int pos = 0;
	glxAttrib [pos++] = GLX.GLX_RGBA;
	if (data.doubleBuffer) glxAttrib [pos++] = GLX.GLX_DOUBLEBUFFER;
	if (data.stereo) glxAttrib [pos++] = GLX.GLX_STEREO;
	if (data.redSize > 0) {
		glxAttrib [pos++] = GLX.GLX_RED_SIZE;
		glxAttrib [pos++] = data.redSize;
	}
	if (data.greenSize > 0) {
		glxAttrib [pos++] = GLX.GLX_GREEN_SIZE;
		glxAttrib [pos++] = data.greenSize;
	}
	if (data.blueSize > 0) {
		glxAttrib [pos++] = GLX.GLX_BLUE_SIZE;
		glxAttrib [pos++] = data.blueSize;
	}
	if (data.alphaSize > 0) {
		glxAttrib [pos++] = GLX.GLX_ALPHA_SIZE;
		glxAttrib [pos++] = data.alphaSize;
	}
	if (data.depthSize > 0) {
		glxAttrib [pos++] = GLX.GLX_DEPTH_SIZE;
		glxAttrib [pos++] = data.depthSize;
	}
	if (data.stencilSize > 0) {
		glxAttrib [pos++] = GLX.GLX_STENCIL_SIZE;
		glxAttrib [pos++] = data.stencilSize;
	}
	if (data.accumRedSize > 0) {
		glxAttrib [pos++] = GLX.GLX_ACCUM_RED_SIZE;
		glxAttrib [pos++] = data.accumRedSize;
	}
	if (data.accumGreenSize > 0) {
		glxAttrib [pos++] = GLX.GLX_ACCUM_GREEN_SIZE;
		glxAttrib [pos++] = data.accumGreenSize;
	}
	if (data.accumBlueSize > 0) {
		glxAttrib [pos++] = GLX.GLX_ACCUM_BLUE_SIZE;
		glxAttrib [pos++] = data.accumBlueSize;
	}
	if (data.accumAlphaSize > 0) {
		glxAttrib [pos++] = GLX.GLX_ACCUM_ALPHA_SIZE;
		glxAttrib [pos++] = data.accumAlphaSize;
	}
	if (data.sampleBuffers > 0) {
		glxAttrib [pos++] = GLX.GLX_SAMPLE_BUFFERS;
		glxAttrib [pos++] = data.sampleBuffers;
	}
	if (data.samples > 0) {
		glxAttrib [pos++] = GLX.GLX_SAMPLES;
		glxAttrib [pos++] = data.samples;
	}
	glxAttrib [pos++] = 0;
	GTK.gtk_widget_realize (handle);
	long window = GTK3.gtk_widget_get_window (handle);

	long xDisplay = GDK.gdk_x11_display_get_xdisplay(GDK.gdk_window_get_display(window));
	long infoPtr = GLX.glXChooseVisual (xDisplay, OS.XDefaultScreen (xDisplay), glxAttrib);
	if (infoPtr == 0) {
		dispose ();
		SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	vinfo = new XVisualInfo ();
	GLX.memmove (vinfo, infoPtr, XVisualInfo.sizeof);
	OS.XFree (infoPtr);
	long screen = GDK.gdk_screen_get_default ();
	long gdkvisual = GDK.gdk_x11_screen_lookup_visual (screen, vinfo.visualid);
	long share = data.shareContext != null ? data.shareContext.context : 0;
	context = GLX.glXCreateContext (xDisplay, vinfo, share, true);
	if (context == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	GdkWindowAttr attrs = new GdkWindowAttr ();
	attrs.width = 1;
	attrs.height = 1;
	attrs.event_mask = GDK.GDK_KEY_PRESS_MASK | GDK.GDK_KEY_RELEASE_MASK |
		GDK.GDK_FOCUS_CHANGE_MASK | GDK.GDK_POINTER_MOTION_MASK |
		GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK |
		GDK.GDK_ENTER_NOTIFY_MASK | GDK.GDK_LEAVE_NOTIFY_MASK |
		GDK.GDK_EXPOSURE_MASK | GDK.GDK_POINTER_MOTION_HINT_MASK;
	attrs.window_type = GDK.GDK_WINDOW_CHILD;
	attrs.visual = gdkvisual;
	glWindow = GTK3.gdk_window_new (window, attrs, GDK.GDK_WA_VISUAL);
	GDK.gdk_window_set_user_data (glWindow, handle);
	if ((getStyle () & SWT.NO_BACKGROUND) != 0) {
		//TODO: implement this on GTK3 as pixmaps are gone.
	}

	if (GTK.GTK4) {
		// TODO: Enable when the GdkWindow to GdkSurface changes are in
		//xWindow = GDK.gdk_x11_surface_get_xid(glWindow);
	} else {
		xWindow = GDK.gdk_x11_window_get_xid (glWindow);
	}

	GDK.gdk_window_show (glWindow);

	Listener listener = event -> {
		switch (event.type) {
		case SWT.Paint:
			/**
			* Bug in MESA.  MESA does some nasty sort of polling to try
			* and ensure that their buffer sizes match the current X state.
			* This state can be updated using glViewport().
			* FIXME: There has to be a better way of doing this.
			*/
			int [] viewport = new int [4];
			GLX.glGetIntegerv (GLX.GL_VIEWPORT, viewport);
			GLX.glViewport (viewport [0],viewport [1],viewport [2],viewport [3]);
			break;
		case SWT.Resize:
			Rectangle clientArea = getClientArea();
			GDK.gdk_window_move (glWindow, clientArea.x, clientArea.y);
			GDK.gdk_window_resize (glWindow, clientArea.width, clientArea.height);
			break;
		case SWT.Dispose:
			long window1 = GTK3.gtk_widget_get_window (handle);
			long xDisplay1 = gdk_x11_display_get_xdisplay (window1);
			if (context != 0) {
				if (GLX.glXGetCurrentContext () == context) {
					GLX.glXMakeCurrent (xDisplay1, 0, 0);
				}
				GLX.glXDestroyContext (xDisplay1, context);
				context = 0;
			}
			if (glWindow != 0) {
				GDK.gdk_window_destroy (glWindow);
				glWindow = 0;
			}
			break;
		}
	};
	addListener (SWT.Resize, listener);
	addListener (SWT.Paint, listener);
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
	if (glArea != 0) {
		/*
		* GtkGLArea has no queryable configuration, these are the formats
		* it allocates: GL_RGBA8 (GL_RGB8 without alpha on GTK3) and
		* GL_DEPTH_COMPONENT24 or a packed GL_DEPTH24_STENCIL8, whose depth is
		* only attached when it was requested. Drawing goes to a buffer that
		* is shown after swapBuffers(), so it is reported as double buffered.
		*/
		data.doubleBuffer = true;
		data.redSize = data.greenSize = data.blueSize = 8;
		data.alphaSize = hasAlpha ? 8 : 0;
		data.depthSize = hasDepth ? 24 : 0;
		data.stencilSize = hasStencil ? 8 : 0;
		return data;
	}
	long window = GTK3.gtk_widget_get_window (handle);
	long xDisplay = gdk_x11_display_get_xdisplay (window);
	int [] value = new int [1];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_DOUBLEBUFFER, value);
	data.doubleBuffer = value [0] != 0;
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_STEREO, value);
	data.stereo = value [0] != 0;
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_RED_SIZE, value);
	data.redSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_GREEN_SIZE, value);
	data.greenSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_BLUE_SIZE, value);
	data.blueSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_ALPHA_SIZE, value);
	data.alphaSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_DEPTH_SIZE, value);
	data.depthSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_STENCIL_SIZE, value);
	data.stencilSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_ACCUM_RED_SIZE, value);
	data.accumRedSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_ACCUM_GREEN_SIZE, value);
	data.accumGreenSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_ACCUM_BLUE_SIZE, value);
	data.accumBlueSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_ACCUM_ALPHA_SIZE, value);
	data.accumAlphaSize = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_SAMPLE_BUFFERS, value);
	data.sampleBuffers = value [0];
	GLX.glXGetConfig (xDisplay, vinfo, GLX.GLX_SAMPLES, value);
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
	if (glArea != 0) return GDK.gdk_gl_context_get_current () == GTK.gtk_gl_area_get_context (glArea);
	return GLX.glXGetCurrentContext () == context;
}

@Override
public void redraw () {
	super.redraw ();
	/* A frame from a swapBuffers() that GTK has not shown yet must not replace the repaint */
	swapPending = false;
	/* On GTK4 a redraw of the canvas does not snapshot the area again */
	queueRender ();
}

@Override
public void redraw (int x, int y, int width, int height, boolean all) {
	super.redraw (x, y, width, height, all);
	swapPending = false;
	queueRender ();
}

void queueRender () {
	if (glArea == 0) return;
	if (GTK.GTK4 && inRender) {
		/*
		* GTK4 drops a render queued while the area renders, which a Paint
		* listener asking for the next frame does, so queue it after the frame.
		*/
		if (!renderQueued) {
			renderQueued = true;
			getDisplay ().asyncExec (() -> {
				renderQueued = false;
				if (!isDisposed ()) GTK.gtk_gl_area_queue_render (glArea);
			});
		}
		return;
	}
	GTK.gtk_gl_area_queue_render (glArea);
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
	if (glArea != 0) {
		/*
		* Also binds the framebuffer of the area, which GTK may have
		* replaced or unbound since, so do it even when already current.
		*/
		GTK.gtk_gl_area_attach_buffers (glArea);
		return;
	}
	if (GLX.glXGetCurrentContext () == context) return;
	long window = GTK3.gtk_widget_get_window (handle);
	long xDisplay = gdk_x11_display_get_xdisplay (window);
	GLX.glXMakeCurrent (xDisplay, xWindow, context);
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
	if (glArea != 0) {
		/* GTK shows the frame after the render signal, or at the next frame when drawn outside of it */
		if (!inRender) {
			swapPending = true;
			GTK.gtk_gl_area_queue_render (glArea);
		}
		return;
	}
	long window = GTK3.gtk_widget_get_window (handle);
	long xDisplay = gdk_x11_display_get_xdisplay (window);
	GLX.glXSwapBuffers (xDisplay, xWindow);
}

private long gdk_x11_display_get_xdisplay(long window) {
	return GDK.gdk_x11_display_get_xdisplay(GDK.gdk_window_get_display(window));
}
}

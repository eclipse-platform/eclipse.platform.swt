package org.eclipse.swt.opengl;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.opengl.gtk.*;

public class GLCanvas extends Canvas {
	private int /*long*/ xdisplay;
	private int /*long*/ xid;
	private int /*long*/ context;
	private int /*long*/ glWindow;

	private static final int MAX_ATTRIBUTES = 32;

public GLCanvas (Composite parent, int style, GLFormatData data) {
	super (parent, style);	
	if (data == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int glxAttrib [] = new int [MAX_ATTRIBUTES];
	int pos = 0;

	int /*long*/ window = OS.GTK_WIDGET_WINDOW (handle);
	xdisplay = OS.gdk_x11_drawable_get_xdisplay (window);
	int xscreen = OS.XDefaultScreen (xdisplay);

	glxAttrib [pos++] = GLX.GLX_LEVEL;
	glxAttrib [pos++] = 0;
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
	glxAttrib [pos++] = GLX.GLX_NONE;	
	int infoPtr = GLX.glXChooseVisual (xdisplay, xscreen, glxAttrib);
	if (infoPtr == 0) SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	XVisualInfo info = new XVisualInfo ();
	GLX.memmove (info, infoPtr, XVisualInfo.sizeof);
	OS.XFree (infoPtr);
	int /*long*/ screen = OS.gdk_screen_get_default ();
	int /*long*/ gdkvisual = OS.gdk_x11_screen_lookup_visual (screen, info.visualid);
	context = GLX.glXCreateContext (xdisplay, info, 0, true);
	if (context == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	GdkWindowAttr attrs = new GdkWindowAttr ();
	attrs.width = 1;
	attrs.height = 1;
	attrs.window_type = OS.GDK_WINDOW_CHILD;
	attrs.visual = gdkvisual;
	glWindow = OS.gdk_window_new (window, attrs, OS.GDK_WA_VISUAL);
	xid = OS.gdk_x11_drawable_get_xid (glWindow);
	OS.gdk_window_show (glWindow);

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
	GL.glViewport (0, 0, bounds.width, bounds.height);
	OS.gdk_window_resize (glWindow, bounds.width, bounds.height);
}

public boolean isCurrent () {
	return GLX.glXGetCurrentContext () == context;
}

public void setCurrent () {
	if (GLX.glXGetCurrentContext () == context) return;
	GLX.glXMakeCurrent (xdisplay, xid, context);
}

public void swapBuffers () {
	GLX.glXSwapBuffers (xdisplay, xid);
}
}

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
package org.eclipse.swt.opengl.internal.gtk;

import org.eclipse.swt.opengl.Library;

public class GTKGLEXT {
	
	static {
		Library.loadLibrary("gl");
		GTKGLEXT.gtk_gl_init (0, 0);
	}

	public static final native int gdk_gl_config_new (int [] mode);
	public static final native int gdk_gl_config_new_by_mode (int mode);
	public static final native void gdk_gl_context_destroy (int glContext);
	public static final native int gdk_gl_context_get_current ();
	public static final native int gdk_gl_context_get_gl_drawable (int glContext);
	public static final native boolean gdk_gl_drawable_gl_begin (int glDrawable, int glContext);
	public static final native void gdk_gl_drawable_gl_end (int glDrawable);
	public static final native void gdk_gl_drawable_make_current (int glDrawable, int glContext);
	public static final native void gdk_gl_drawable_swap_buffers (int glDrawable);
	public static final native void gdk_gl_font_use_pango_font (int font_desc, int first, int count, int list_base);
	public static final native void gtk_gl_init (int argc, int argv);
	public static final native int gtk_widget_create_gl_context (int widget, int share_list, boolean direct, int render_type);
	public static final native int gtk_widget_get_gl_context (int widget);
	public static final native int gtk_widget_get_gl_drawable (int widget);
	public static final native int gtk_widget_get_gl_window (int widget);
	public static final native boolean gtk_widget_set_gl_capability (int widget, int glconfig, int share_list, boolean direct, int render_type);
	public static final int GDK_GL_MODE_DEPTH = 1 << 4;
	public static final int GDK_GL_MODE_DOUBLE = 1 << 1;
	public static final int GDK_GL_MODE_INDEX = 1 << 0;
	public static final int GDK_GL_MODE_RGB = 0;
	public static final int GDK_GL_MODE_RGBA = 0;
	public static final int GDK_GL_RGBA_TYPE = 0x8014;
}

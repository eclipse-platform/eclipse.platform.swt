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
#include "swt.h"
#include <gtk/gtkgl.h>
#include <gdk/gdkgl.h>

#ifndef NO_gdk_1gl_1config_1new
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1config_1new
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gdk_1gl_1config_1new\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)gdk_gl_config_new(lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_gdk_1gl_1config_1new_1by_1mode
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1config_1new_1by_1mode
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gl_1config_1new_1by_1mode\n")

	return (jint)gdk_gl_config_new_by_mode(arg0);
}
#endif

#ifndef NO_gdk_1gl_1context_1destroy
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1context_1destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gl_1context_1destroy\n")

	gdk_gl_context_destroy((GdkGLContext *)arg0);
}
#endif

#ifndef NO_gdk_1gl_1context_1get_1current
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1context_1get_1current
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gdk_1gl_1context_1get_1current\n")

	return (jint)gdk_gl_context_get_current();
}
#endif

#ifndef NO_gdk_1gl_1context_1get_1gl_1drawable
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1context_1get_1gl_1drawable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gl_1context_1get_1gl_1drawable\n")

	return (jint)gdk_gl_context_get_gl_drawable((GdkGLContext *)arg0);
}
#endif

#ifndef NO_gdk_1gl_1drawable_1gl_1begin
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1drawable_1gl_1begin
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gl_1drawable_1gl_1begin\n")

	return (jboolean)gdk_gl_drawable_gl_begin((GdkGLDrawable *)arg0, (GdkGLContext *)arg1);
}
#endif

#ifndef NO_gdk_1gl_1drawable_1gl_1end
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1drawable_1gl_1end
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gl_1drawable_1gl_1end\n")

	gdk_gl_drawable_gl_end((GdkGLDrawable *)arg0);
}
#endif

#ifndef NO_gdk_1gl_1drawable_1make_1current
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1drawable_1make_1current
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gdk_1gl_1drawable_1make_1current\n")

	gdk_gl_drawable_make_current((GdkGLDrawable *)arg0, (GdkGLContext *)arg1);
}
#endif

#ifndef NO_gdk_1gl_1drawable_1swap_1buffers
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1drawable_1swap_1buffers
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gdk_1gl_1drawable_1swap_1buffers\n")

	gdk_gl_drawable_swap_buffers((GdkGLDrawable *)arg0);
}
#endif

#ifndef NO_gdk_1gl_1font_1use_1pango_1font
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gdk_1gl_1font_1use_1pango_1font
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gdk_1gl_1font_1use_1pango_1font\n")

	gdk_gl_font_use_pango_font((PangoFontDescription *)arg0, arg1, arg2, arg3);
}
#endif

#ifndef NO_gtk_1gl_1init
JNIEXPORT void JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1gl_1init
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gtk_1gl_1init\n")

	gtk_gl_init((int *)arg0, (char ***)arg1);
}
#endif

#ifndef NO_gtk_1widget_1create_1gl_1context
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1widget_1create_1gl_1context
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	DEBUG_CALL("gtk_1widget_1create_1gl_1context\n")

	return (jint)gtk_widget_create_gl_context((GtkWidget *)arg0, (GdkGLContext *)arg1, arg2, arg3);
}
#endif

#ifndef NO_gtk_1widget_1get_1gl_1context
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1widget_1get_1gl_1context
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1gl_1context\n")

	return (jint)gtk_widget_get_gl_context((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1widget_1get_1gl_1drawable
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1widget_1get_1gl_1drawable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1gl_1drawable\n")

	return (jint)gtk_widget_get_gl_drawable((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1widget_1get_1gl_1window
JNIEXPORT jint JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1widget_1get_1gl_1window
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gtk_1widget_1get_1gl_1window\n")

	return (jint)gtk_widget_get_gl_window((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1widget_1set_1gl_1capability
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_opengl_internal_gtk_GTKGLEXT_gtk_1widget_1set_1gl_1capability
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jint arg4)
{
	DEBUG_CALL("gtk_1widget_1set_1gl_1capability\n")

	return (jboolean)gtk_widget_set_gl_capability((GtkWidget *)arg0, (GdkGLConfig *)arg1, (GdkGLContext *)arg2, arg3, arg4);
}
#endif

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
#include "swt.h"
#include <gdk/gdkx.h>
#include <stdlib.h>

#define XGTK_NATIVE(func) Java_org_eclipse_swt_opengl_internal_gtk_XGTK_##func

#ifndef NO_free
JNIEXPORT void JNICALL XGTK_NATIVE(free)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "free\n")
	free((char *)arg0);
	NATIVE_EXIT(env, that, "free\n")
}
#endif

#ifndef NO_malloc
JNIEXPORT jint JNICALL XGTK_NATIVE(malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "malloc\n")
	rc = (jint)malloc(arg0);
	NATIVE_EXIT(env, that, "malloc\n")
	return rc;
}
#endif

#ifndef NO_gdk_1x11_1gc_1get_1xdisplay
JNIEXPORT int JNICALL XGTK_NATIVE(gdk_1x11_1gc_1get_1xdisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gdk_1x11_1gc_1get_1xdisplay\n")
	rc = (jint)gdk_x11_gc_get_xdisplay((GdkGC *)arg0);
	NATIVE_EXIT(env, that, "gdk_1x11_1gc_1get_1xdisplay\n")
	return rc;
}
#endif

#ifndef NO_gdk_1x11_1drawable_1get_1xid
JNIEXPORT int JNICALL XGTK_NATIVE(gdk_1x11_1drawable_1get_1xid)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gdk_1x11_1drawable_1get_1xid\n")
	rc = (jint)gdk_x11_drawable_get_xid((GdkDrawable *)arg0);
	NATIVE_EXIT(env, that, "gdk_1x11_1drawable_1get_1xid\n")
	return rc;
}
#endif

#ifndef NO_XDefaultScreen
JNIEXPORT jint JNICALL XGTK_NATIVE(XDefaultScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultScreen\n")
	rc = (jint)XDefaultScreen((Display *)arg0);
	NATIVE_EXIT(env, that, "XDefaultScreen\n")
	return rc;
}
#endif

#ifndef NO_XDefaultScreenOfDisplay
JNIEXPORT jint JNICALL XGTK_NATIVE(XDefaultScreenOfDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultScreenOfDisplay\n")
	rc = (jint)XDefaultScreenOfDisplay((Display *)arg0);
	NATIVE_EXIT(env, that, "XDefaultScreenOfDisplay\n")
	return rc;
}
#endif

#ifndef NO_XDefaultDepthOfScreen
JNIEXPORT jint JNICALL XGTK_NATIVE(XDefaultDepthOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultDepthOfScreen\n")
	rc = (jint)XDefaultDepthOfScreen((Screen *)arg0);
	NATIVE_EXIT(env, that, "XDefaultDepthOfScreen\n")
	return rc;
}
#endif

#ifndef NO_XFree
JNIEXPORT jint JNICALL XGTK_NATIVE(XFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XFree\n")
	rc = (jint)XFree((char *)arg0);
	NATIVE_EXIT(env, that, "XFree\n")
	return rc;
}
#endif

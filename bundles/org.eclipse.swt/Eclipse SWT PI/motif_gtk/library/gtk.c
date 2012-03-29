/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "gtk_structs.h"
#include "gtk_stats.h"

#ifndef GTK_NATIVE
#define GTK_NATIVE(func) Java_org_eclipse_swt_internal_gtk_GTK_##func
#endif

#ifndef NO__1GTK_1WIDGET_1HEIGHT
JNIEXPORT jint JNICALL GTK_NATIVE(_1GTK_1WIDGET_1HEIGHT)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1HEIGHT_FUNC);
	rc = (jint)GTK_WIDGET_HEIGHT((GtkWidget *)arg0);
	GTK_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1HEIGHT_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1WIDTH
JNIEXPORT jint JNICALL GTK_NATIVE(_1GTK_1WIDGET_1WIDTH)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1WIDTH_FUNC);
	rc = (jint)GTK_WIDGET_WIDTH((GtkWidget *)arg0);
	GTK_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1WIDTH_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1connect
JNIEXPORT jint JNICALL GTK_NATIVE(_1g_1signal_1connect)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1g_1signal_1connect_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)g_signal_connect((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	GTK_NATIVE_EXIT(env, that, _1g_1signal_1connect_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1events_1pending
JNIEXPORT jint JNICALL GTK_NATIVE(_1gtk_1events_1pending)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1gtk_1events_1pending_FUNC);
	rc = (jint)gtk_events_pending();
	GTK_NATIVE_EXIT(env, that, _1gtk_1events_1pending_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1init_1check
JNIEXPORT jboolean JNICALL GTK_NATIVE(_1gtk_1init_1check)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jboolean rc = 0;
	GTK_NATIVE_ENTER(env, that, _1gtk_1init_1check_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_init_check((int *)lparg0, (char ***)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1init_1check_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1main
JNIEXPORT void JNICALL GTK_NATIVE(_1gtk_1main)
	(JNIEnv *env, jclass that)
{
	GTK_NATIVE_ENTER(env, that, _1gtk_1main_FUNC);
	gtk_main();
	GTK_NATIVE_EXIT(env, that, _1gtk_1main_FUNC);
}
#endif

#ifndef NO__1gtk_1main_1iteration
JNIEXPORT jint JNICALL GTK_NATIVE(_1gtk_1main_1iteration)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1gtk_1main_1iteration_FUNC);
	rc = (jint)gtk_main_iteration();
	GTK_NATIVE_EXIT(env, that, _1gtk_1main_1iteration_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1plug_1new
JNIEXPORT jint JNICALL GTK_NATIVE(_1gtk_1plug_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1gtk_1plug_1new_FUNC);
	rc = (jint)gtk_plug_new(arg0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1plug_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1destroy
JNIEXPORT void JNICALL GTK_NATIVE(_1gtk_1widget_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	GTK_NATIVE_ENTER(env, that, _1gtk_1widget_1destroy_FUNC);
	gtk_widget_destroy((GtkWidget *)arg0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1widget_1destroy_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1show
JNIEXPORT void JNICALL GTK_NATIVE(_1gtk_1widget_1show)
	(JNIEnv *env, jclass that, jint arg0)
{
	GTK_NATIVE_ENTER(env, that, _1gtk_1widget_1show_FUNC);
	gtk_widget_show((GtkWidget *)arg0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1widget_1show_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1show_1now
JNIEXPORT void JNICALL GTK_NATIVE(_1gtk_1widget_1show_1now)
	(JNIEnv *env, jclass that, jint arg0)
{
	GTK_NATIVE_ENTER(env, that, _1gtk_1widget_1show_1now_FUNC);
	gtk_widget_show_now((GtkWidget *)arg0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1widget_1show_1now_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1new
JNIEXPORT jint JNICALL GTK_NATIVE(_1gtk_1window_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	GTK_NATIVE_ENTER(env, that, _1gtk_1window_1new_FUNC);
	rc = (jint)gtk_window_new((GtkWindowType)arg0);
	GTK_NATIVE_EXIT(env, that, _1gtk_1window_1new_FUNC);
	return rc;
}
#endif


#include "swt.h"

#include <stdio.h>
#include <assert.h>

#include <gtk/gtk.h>
#include <gdk/gdk.h>

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_GTK_##func

JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1HEIGHT)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	rc = (jint)((GtkWidget *)arg0)->allocation.height;
	return rc;
}

JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1WIDTH)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	rc = (jint)((GtkWidget *)arg0)->allocation.width;
	return rc;
}

#ifndef NO_g_1signal_1connect
JNIEXPORT jint JNICALL OS_NATIVE(g_1signal_1connect)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)g_signal_connect((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

#ifndef NO_gtk_1events_1pending
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1events_1pending)
	(JNIEnv *env, jclass that)
{
	return (jint)gtk_events_pending();
}
#endif

#ifndef NO_gtk_1init_1check
JNIEXPORT jboolean JNICALL OS_NATIVE(gtk_1init_1check)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jboolean rc;
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)gtk_init_check((int *)lparg0, (char ***)lparg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

JNIEXPORT void JNICALL OS_NATIVE(gtk_1main)
	(JNIEnv *env, jclass that)
{
	gtk_main();
}

#ifndef NO_gtk_1main_1iteration
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1main_1iteration)
	(JNIEnv *env, jclass that)
{
	return (jint)gtk_main_iteration();
}
#endif

#ifndef NO_gtk_1plug_1new
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1plug_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	rc = (jint)gtk_plug_new(arg0);
	return rc;
}
#endif

#ifndef NO_gtk_1widget_1destroy
JNIEXPORT void JNICALL OS_NATIVE(gtk_1widget_1destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	gtk_widget_destroy((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1widget_1show
JNIEXPORT void JNICALL OS_NATIVE(gtk_1widget_1show)
	(JNIEnv *env, jclass that, jint arg0)
{
	gtk_widget_show((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1widget_1show_1now
JNIEXPORT void JNICALL OS_NATIVE(gtk_1widget_1show_1now)
	(JNIEnv *env, jclass that, jint arg0)
{
	gtk_widget_show_now((GtkWidget *)arg0);
}
#endif

#ifndef NO_gtk_1window_1new
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1window_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	rc = (jint)gtk_window_new((GtkWindowType)arg0);
	return rc;
}
#endif

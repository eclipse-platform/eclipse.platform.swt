/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
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
#include "os_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_OS_##func

#ifndef NO_GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II
JNIEXPORT void JNICALL OS_NATIVE(GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II\n")
	((GtkAccelLabel *)arg0)->accel_string = (gchar *)arg1;
	NATIVE_EXIT(env, that, "GTK_1ACCEL_1LABEL_1ACCEL_1STRING__II\n")
}
#endif

#ifndef NO_GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I\n")
	rc = (jint)((GtkAccelLabel *)arg0)->accel_string;
	NATIVE_EXIT(env, that, "GTK_1ACCEL_1LABEL_1ACCEL_1STRING__I\n")
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1HSCROLLBAR
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1HSCROLLBAR)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1SCROLLED_1WINDOW_1HSCROLLBAR\n")
	rc = (jint)((GtkScrolledWindow *)arg0)->hscrollbar;
	NATIVE_EXIT(env, that, "GTK_1SCROLLED_1WINDOW_1HSCROLLBAR\n")
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING\n")
	#define DEFAULT_SCROLLBAR_SPACING  3
	rc = (jint)(GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing >= 0 ?
		GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing : DEFAULT_SCROLLBAR_SPACING);
	NATIVE_EXIT(env, that, "GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING\n")
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1VSCROLLBAR
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1VSCROLLBAR)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1SCROLLED_1WINDOW_1VSCROLLBAR\n")
	rc = (jint)((GtkScrolledWindow *)arg0)->vscrollbar;
	NATIVE_EXIT(env, that, "GTK_1SCROLLED_1WINDOW_1VSCROLLBAR\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1HEIGHT
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1HEIGHT)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1HEIGHT\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.height;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1HEIGHT\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1WIDTH
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1WIDTH)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1WIDTH\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.width;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1WIDTH\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1WINDOW
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1WINDOW)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1WINDOW\n")
	rc = (jint)((GtkWidget *)arg0)->window;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1WINDOW\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1X
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1X)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1X\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.x;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1X\n")
	return rc;
}
#endif

#ifndef NO_GTK_1ENTRY_1IM_1CONTEXT
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1ENTRY_1IM_1CONTEXT
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "OS_GTK_1ENTRY_1IM_1CONTEXT\n")
	rc = (jint)((GtkEntry *)arg0)->im_context;
	NATIVE_EXIT(env, that, "OS_GTK_1ENTRY_1IM_1CONTEXT\n")
	return rc;
}
#endif

#ifndef NO_GTK_1TEXTVIEW_1IM_1CONTEXT
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1TEXTVIEW_1IM_1CONTEXT)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1TEXTVIEW_1IM_1CONTEXT\n")
	rc = (jint)((GtkTextView *)arg0)->im_context;
	NATIVE_EXIT(env, that, "GTK_1TEXTVIEW_1IM_1CONTEXT\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1Y
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1Y)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1Y\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.y;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1Y\n")
	return rc;
}
#endif

#ifndef NO_GtkTreeIter_1sizeof
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GtkTreeIter_1sizeof
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkTreeIter_1sizeof\n")
	rc = (jint)sizeof(GtkTreeIter);
	NATIVE_EXIT(env, that, "GtkTreeIter_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_g_1list_1next__II
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1next__II
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "g_1list_1next__II\n")
	((GList *)arg0)->next = (GList *)arg1;
	NATIVE_EXIT(env, that, "g_1list_1next__II\n")
}
#endif

#ifndef NO_g_1list_1previous__II
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1previous__II
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "g_1list_1previous__II\n")
	((GList *)arg0)->prev = (GList *)arg1;
	NATIVE_EXIT(env, that, "g_1list_1previous__II\n")
}
#endif

#ifndef NO_gdk_1screen_1get_1default
JNIEXPORT jint JNICALL OS_NATIVE(gdk_1screen_1get_1default)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "gdk_1screen_1get_1default\n")
	//rc = (jint)gdk_screen_get_default();
	{
	void *handle = NULL;
	GdkScreen* (*fptr)();
	rc = 0;
	handle = dlopen("libgdk-x11-2.0.so", RTLD_LAZY);
	if (handle != NULL) {
		fptr = (GdkScreen* (*)())dlsym(handle, "gdk_screen_get_default");
		if (fptr != NULL) {
			rc = (jint)(*fptr)();
		}
	}
	}
	NATIVE_EXIT(env, that, "gdk_1screen_1get_1default\n")
	return rc;
}
#endif

#ifndef NO_gdk_1screen_1get_1monitor_1geometry
JNIEXPORT void JNICALL OS_NATIVE(gdk_1screen_1get_1monitor_1geometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkRectangle _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "gdk_1screen_1get_1monitor_1geometry\n")
	if (arg2) lparg2 = getGdkRectangleFields(env, arg2, &_arg2);
	//gdk_screen_get_monitor_geometry((GdkScreen *)arg0, arg1, lparg2);
	{
	void *handle = NULL;
	void (*fptr)(GdkScreen *, gint, GdkRectangle *);
	handle = dlopen("libgdk-x11-2.0.so", RTLD_LAZY);
	if (handle != NULL) {
		fptr = (void (*)(GdkScreen *,  gint, GdkRectangle *))dlsym(handle, "gdk_screen_get_monitor_geometry");
		if (fptr != NULL) {
			(*fptr)((GdkScreen *)arg0, arg1, lparg2);
		}
	}
	}
	if (arg2) setGdkRectangleFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "gdk_1screen_1get_1monitor_1geometry\n")
}
#endif

#ifndef NO_gdk_1screen_1get_1n_1monitors
JNIEXPORT jint JNICALL OS_NATIVE(gdk_1screen_1get_1n_1monitors)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gdk_1screen_1get_1n_1monitors\n")
	//rc = (jint)gdk_screen_get_n_monitors((GdkScreen *)arg0);
	{
	void *handle = NULL;
	gint (*fptr)(GdkScreen *);
	rc = 0;
	handle = dlopen("libgdk-x11-2.0.so", RTLD_LAZY);
	if (handle != NULL) {
		fptr = (gint (*)(GdkScreen *))dlsym(handle, "gdk_screen_get_n_monitors");
		if (fptr != NULL) {
			rc = (jint)(*fptr)((GdkScreen *)arg0);
		}
	}
	}
	NATIVE_EXIT(env, that, "gdk_1screen_1get_1n_1monitors\n")
	return rc;
}
#endif

#ifndef NO_gtk_1rc_1style_1set_1xthickness
JNIEXPORT void JNICALL OS_NATIVE(gtk_1rc_1style_1set_1xthickness)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1set_1xthickness\n")
	((GtkRcStyle *)arg0)->xthickness = arg1;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1set_1xthickness\n")
}
#endif

#ifndef NO_gtk_1rc_1style_1set_1ythickness
JNIEXPORT void JNICALL OS_NATIVE(gtk_1rc_1style_1set_1ythickness)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1set_1ythickness\n")
	((GtkRcStyle *)arg0)->ythickness = arg1;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1set_1ythickness\n")
}
#endif


#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2\n")
	if (arg1) getGtkAdjustmentFields(env, arg1, (GtkAdjustment *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I\n")
	if (arg0) setGtkFileSelectionFields(env, arg0, (GtkFileSelection *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2\n")
	if (arg1) getGtkStyleFields(env, arg1, (GtkStyle *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n")
	if (arg0) setGtkColorSelectionDialogFields(env, arg0, (GtkColorSelectionDialog *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I\n")
	if (arg0) setGtkStyleFields(env, arg0, (GtkStyle *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I\n")
	if (arg0) setGtkComboFields(env, arg0, (GtkCombo *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I\n")
	if (arg0) setGtkAdjustmentFields(env, arg0, (GtkAdjustment *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I\n")
}
#endif


#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I\n")
	if (arg0) setGtkFixedFields(env, arg0, (GtkFixed *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2\n")
	if (arg1) getGtkFixedFields(env, arg1, (GtkFixed *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I\n")
	if (arg0) setGdkVisualFields(env, arg0, (GdkVisual *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I\n")
	if (arg0) setGdkImageFields(env, arg0, (GdkImage *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I\n")
}
#endif

#ifndef NO_AtkObjectFactory_1sizeof
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_AtkObjectFactory_1sizeof
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactory_1sizeof\n")
	rc = (jint)sizeof(AtkObjectFactory);
	NATIVE_EXIT(env, that, "AtkObjectFactory_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_AtkObjectFactoryClass_1sizeof
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_AtkObjectFactoryClass_1sizeof
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "AtkObjectFactoryClass_1sizeof\n")
	rc = (jint)sizeof(AtkObjectFactoryClass);
	NATIVE_EXIT(env, that, "AtkObjectFactoryClass_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_call__II
typedef jint (*call_P1) (jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "call__II\n")
	return ((call_P1)arg0)(arg1);
	NATIVE_EXIT(env, that, "call__II\n")
}
#endif

#ifndef NO_call__III
typedef jint (*call_P2) (jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "call__III\n")
	return ((call_P2)arg0)(arg1, arg2);
	NATIVE_EXIT(env, that, "call__III\n")
}
#endif

#ifndef NO_call__IIII
typedef jint (*call_P3) (jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "call__IIII\n")
	return ((call_P3)arg0)(arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "call__IIII\n")
}
#endif

#ifndef NO_call__IIIII
typedef jint (*call_P4) (jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "call__IIIII\n")
	return ((call_P4)arg0)(arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "call__IIIII\n")
}
#endif

#ifndef NO_call__IIIIII
typedef jint (*call_P5) (jint, jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "call__IIIIII\n")
	return ((call_P5)arg0)(arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "call__IIIIII\n")
}
#endif

#ifndef NO_call__IIIIIII
typedef jint (*call_P6) (jint, jint, jint, jint, jint, jint);
JNIEXPORT jint JNICALL OS_NATIVE(call__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "call__IIIIIII\n")
	return ((call_P6)arg0)(arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "call__IIIIIII\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkActionIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkActionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkActionIface_2I\n")
	if (arg0) setAtkActionIfaceFields(env, arg0, (AtkActionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkActionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkComponentIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkComponentIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkComponentIface_2I\n")
	if (arg0) setAtkComponentIfaceFields(env, arg0, (AtkComponentIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkComponentIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectClass_2I\n")
	if (arg0) setAtkObjectClassFields(env, arg0, (AtkObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2I\n")
	if (arg0) setAtkObjectFactoryClassFields(env, arg0, (AtkObjectFactoryClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkSelectionIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkSelectionIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkSelectionIface_2I\n")
	if (arg0) setAtkSelectionIfaceFields(env, arg0, (AtkSelectionIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkSelectionIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_AtkTextIface_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_AtkTextIface_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkTextIface_2I\n")
	if (arg0) setAtkTextIfaceFields(env, arg0, (AtkTextIface *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_AtkTextIface_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I\n")
	if (arg0) setGObjectClassFields(env, arg0, (GObjectClass *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkAccessible_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkAccessible_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAccessible_2I\n")
	if (arg0) setGtkAccessibleFields(env, arg0, (GtkAccessible *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAccessible_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkActionIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkActionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkActionIface_2\n")
	if (arg1) getAtkActionIfaceFields(env, arg1, (AtkActionIface*)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkActionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkComponentIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkComponentIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkComponentIface_2\n")
	if (arg1) getAtkComponentIfaceFields(env, arg1, (AtkComponentIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkComponentIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectClass_2\n")
	if (arg1) getAtkObjectClassFields(env, arg1, (AtkObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2\n")
	if (arg1) getAtkObjectFactoryClassFields(env, arg1, (AtkObjectFactoryClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkObjectFactoryClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkSelectionIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkSelectionIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkSelectionIface_2\n")
	if (arg1) getAtkSelectionIfaceFields(env, arg1, (AtkSelectionIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkSelectionIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_AtkTextIface_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_AtkTextIface_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkTextIface_2\n")
	if (arg1) getAtkTextIfaceFields(env, arg1, (AtkTextIface *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_AtkTextIface_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2\n")
	if (arg1) getGObjectClassFields(env, arg1, (GObjectClass *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkAccessible_2
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkAccessible_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAccessible_2\n")
	if (arg1) getGtkAccessibleFields(env, arg1, (GtkAccessible *)arg0);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAccessible_2\n")
}
#endif

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

#ifndef NO_GDK_1WINDOWING_1X11
JNIEXPORT jboolean JNICALL OS_NATIVE(GDK_1WINDOWING_1X11)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "GDK_1WINDOWING_1X11\n")
#ifdef GDK_WINDOWING_X11
	rc = (jboolean)1;
#else
	rc = (jboolean)0;
#endif	
	NATIVE_EXIT(env, that, "GDK_1WINDOWING_1X11\n")
	return rc;
}
#endif

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

#ifndef NO_GdkColor_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkColor_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkColor_1sizeof\n")
	rc = (jint)sizeof(GdkColor);
	NATIVE_EXIT(env, that, "GdkColor_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkDragContext_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkDragContext_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkDragContext_1sizeof\n")
	rc = (jint)sizeof(GdkDragContext);
	NATIVE_EXIT(env, that, "GdkDragContext_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventButton_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventButton_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventButton_1sizeof\n")
	rc = (jint)sizeof(GdkEventButton);
	NATIVE_EXIT(env, that, "GdkEventButton_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventCrossing_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventCrossing_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventCrossing_1sizeof\n")
	rc = (jint)sizeof(GdkEventCrossing);
	NATIVE_EXIT(env, that, "GdkEventCrossing_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventExpose_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventExpose_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventExpose_1sizeof\n")
	rc = (jint)sizeof(GdkEventExpose);
	NATIVE_EXIT(env, that, "GdkEventExpose_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventFocus_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventFocus_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventFocus_1sizeof\n")
	rc = (jint)sizeof(GdkEventFocus);
	NATIVE_EXIT(env, that, "GdkEventFocus_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventKey_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventKey_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventKey_1sizeof\n")
	rc = (jint)sizeof(GdkEventKey);
	NATIVE_EXIT(env, that, "GdkEventKey_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEventWindowState_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventWindowState_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEventWindowState_1sizeof\n")
	rc = (jint)sizeof(GdkEventWindowState);
	NATIVE_EXIT(env, that, "GdkEventWindowState_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkEvent_1sizeof\n")
	rc = (jint)sizeof(GdkEvent);
	NATIVE_EXIT(env, that, "GdkEvent_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkGCValues_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkGCValues_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkGCValues_1sizeof\n")
	rc = (jint)sizeof(GdkGCValues);
	NATIVE_EXIT(env, that, "GdkGCValues_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkImage_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkImage_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkImage_1sizeof\n")
	rc = (jint)sizeof(GdkImage);
	NATIVE_EXIT(env, that, "GdkImage_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkRectangle_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkRectangle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkRectangle_1sizeof\n")
	rc = (jint)sizeof(GdkRectangle);
	NATIVE_EXIT(env, that, "GdkRectangle_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GdkVisual_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkVisual_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GdkVisual_1sizeof\n")
	rc = (jint)sizeof(GdkVisual);
	NATIVE_EXIT(env, that, "GdkVisual_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkAdjustment_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkAdjustment_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkAdjustment_1sizeof\n")
	rc = (jint)sizeof(GtkAdjustment);
	NATIVE_EXIT(env, that, "GtkAdjustment_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkAllocation_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkAllocation_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkAllocation_1sizeof\n")
	rc = (jint)sizeof(GtkAllocation);
	NATIVE_EXIT(env, that, "GtkAllocation_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkColorSelectionDialog_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkColorSelectionDialog_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkColorSelectionDialog_1sizeof\n")
	rc = (jint)sizeof(GtkColorSelectionDialog);
	NATIVE_EXIT(env, that, "GtkColorSelectionDialog_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkCombo_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCombo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkCombo_1sizeof\n")
	rc = (jint)sizeof(GtkCombo);
	NATIVE_EXIT(env, that, "GtkCombo_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkFileSelection_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkFileSelection_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkFileSelection_1sizeof\n")
	rc = (jint)sizeof(GtkFileSelection);
	NATIVE_EXIT(env, that, "GtkFileSelection_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkFixed_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkFixed_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkFixed_1sizeof\n")
	rc = (jint)sizeof(GtkFixed);
	NATIVE_EXIT(env, that, "GtkFixed_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkRequisition_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkRequisition_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkRequisition_1sizeof\n")
	rc = (jint)sizeof(GtkRequisition);
	NATIVE_EXIT(env, that, "GtkRequisition_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkSelectionData_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkSelectionData_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkSelectionData_1sizeof\n")
	rc = (jint)sizeof(GtkSelectionData);
	NATIVE_EXIT(env, that, "GtkSelectionData_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkTargetEntry_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTargetEntry_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkTargetEntry_1sizeof\n")
	rc = (jint)sizeof(GtkTargetEntry);
	NATIVE_EXIT(env, that, "GtkTargetEntry_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_GtkTargetPair_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTargetPair_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GtkTargetPair_1sizeof\n")
	rc = (jint)sizeof(GtkTargetPair);
	NATIVE_EXIT(env, that, "GtkTargetPair_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_PangoAttribute_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoAttribute_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PangoAttribute_1sizeof\n")
	rc = (jint)sizeof(PangoAttribute);
	NATIVE_EXIT(env, that, "PangoAttribute_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_PangoRectangle_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoRectangle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PangoRectangle_1sizeof\n")
	rc = (jint)sizeof(PangoRectangle);
	NATIVE_EXIT(env, that, "PangoRectangle_1sizeof\n")
	return rc;
}
#endif

#ifndef NO_PTR_1sizeof
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PTR_1sizeof
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PTR_1sizeof\n")
	rc = (jint)sizeof(SWT_PTR);
	NATIVE_EXIT(env, that, "PTR_1sizeof\n")
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

#ifndef NO_XWindowChanges_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XWindowChanges_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "XWindowChanges_1sizeof\n")
	rc = (jint)sizeof(XWindowChanges);
	NATIVE_EXIT(env, that, "XWindowChanges_1sizeof\n")
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

#ifndef NO_gdk_1screen_1get_1monitor_1at_1window
JNIEXPORT jint JNICALL OS_NATIVE(gdk_1screen_1get_1monitor_1at_1window)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "gdk_1screen_1get_1monitor_1at_1window\n")
	//rc = (jint)gdk_1screen_1get_1monitor_1at_1window((GdkScreen *)arg0, (GkdWindow *)arg1);
	{
	void *handle = NULL;
	gint (*fptr)(GdkScreen *, GdkWindow *);
	rc = 0;
	handle = dlopen("libgdk-x11-2.0.so", RTLD_LAZY);
	if (handle != NULL) {
		fptr = (gint (*)(GdkScreen *, GdkWindow *))dlsym(handle, "gdk_screen_get_monitor_at_window");
		if (fptr != NULL) {
			rc = (jint)(*fptr)((GdkScreen *)arg0, (GdkWindow *)arg1);
		}
	}
	}
	NATIVE_EXIT(env, that, "gdk_1screen_1get_1monitor_1at_1window\n")
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

#ifndef NO_gtk_1rc_1style_1get_1bg_1pixmap_1name
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1rc_1style_1get_1bg_1pixmap_1name)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1get_1bg_1pixmap_1name\n")
	rc =  (jint) ((GtkRcStyle *)arg0)->bg_pixmap_name[arg1] ;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1get_1bg_1pixmap_1name\n")
	return rc;
}
#endif

#ifndef NO_gtk_1rc_1style_1get_1color_1flags
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1rc_1style_1get_1color_1flags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1get_1color_1flags\n")
	rc =  (jint) ((GtkRcStyle *)arg0)->color_flags[arg1] ;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1get_1color_1flags\n")
	return rc;
}
#endif

#ifndef NO_gtk_1rc_1style_1set_1bg
JNIEXPORT void JNICALL OS_NATIVE(gtk_1rc_1style_1set_1bg)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1set_1bg\n")
	if (arg2) lparg2 = getGdkColorFields(env, arg2, &_arg2);
	((GtkRcStyle *)arg0)->bg[arg1] = (GdkColor)_arg2;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1set_1bg\n")
}
#endif
	
#ifndef NO_gtk_1rc_1style_1set_1bg_1pixmap_1name
JNIEXPORT void JNICALL OS_NATIVE(gtk_1rc_1style_1set_1bg_1pixmap_1name)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1set_1bg_1pixmap_1name\n")
	((GtkRcStyle *)arg0)->bg_pixmap_name[arg1] = (char *)arg2;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1set_1bg_1pixmap_1name\n")
}
#endif

#ifndef NO_gtk_1rc_1style_1set_1color_1flags
JNIEXPORT void JNICALL OS_NATIVE(gtk_1rc_1style_1set_1color_1flags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "gtk_1rc_1style_1set_1color_1flags\n")
	((GtkRcStyle *)arg0)->color_flags[arg1] = arg2;
	NATIVE_EXIT(env, that, "gtk_1rc_1style_1set_1color_1flags\n")
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

#ifndef NO_gtk_1style_1get_1base
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1base)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1base\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->base[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1base\n")
}
#endif

#ifndef NO_gtk_1style_1get_1bg
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1bg)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1bg\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->bg[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1bg\n")
}
#endif

#ifndef NO_gtk_1style_1get_1black
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1black)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1black\n")
	if (arg1) setGdkColorFields(env, arg1, &((GtkStyle *)arg0)->black);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1black\n")
}
#endif

#ifndef NO_gtk_1style_1get_1dark
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1dark)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1dark\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->dark[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1dark\n")
}
#endif

#ifndef NO_gtk_1style_1get_1fg
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1fg)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1fg\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->fg[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1fg\n")
}
#endif

#ifndef NO_gtk_1style_1get_1font_1desc
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1style_1get_1font_1desc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gtk_1style_1get_1font_1desc\n")
	rc = (jint)((GtkStyle *)arg0)->font_desc;
	NATIVE_EXIT(env, that, "gtk_1style_1get_1font_1desc\n")
	return rc;
}
#endif

#ifndef NO_gtk_1style_1get_1light
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1light)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1light\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->light[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1light\n")
}
#endif

#ifndef NO_gtk_1style_1get_1text
JNIEXPORT void JNICALL OS_NATIVE(gtk_1style_1get_1text)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NATIVE_ENTER(env, that, "gtk_1style_1get_1text\n")
	if (arg2) setGdkColorFields(env, arg2, &((GtkStyle *)arg0)->text[arg1]);
	NATIVE_EXIT(env, that, "gtk_1style_1get_1text\n")
}
#endif

#ifndef NO_gtk_1style_1get_1xthickness
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1style_1get_1xthickness)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gtk_1style_1get_1xthickness\n")
	rc = (jint)((GtkStyle *)arg0)->xthickness;
	NATIVE_EXIT(env, that, "gtk_1style_1get_1xthickness\n")
	return rc;
}
#endif

#ifndef NO_gtk_1style_1get_1ythickness
JNIEXPORT jint JNICALL OS_NATIVE(gtk_1style_1get_1ythickness)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "gtk_1style_1get_1ythickness\n")
	rc = (jint)((GtkStyle *)arg0)->ythickness;
	NATIVE_EXIT(env, that, "gtk_1style_1get_1ythickness\n")
	return rc;
}
#endif

#ifndef NO_gtk_1tree_1view_1column_1cell_1get_1position
JNIEXPORT jboolean JNICALL OS_NATIVE(gtk_1tree_1view_1column_1cell_1get_1position)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "gtk_1tree_1view_1column_1cell_1get_1position\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	//rc = (jboolean)gtk_tree_view_column_cell_get_position((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gint *)lparg2, (gint *)lparg3);
	{
	void *handle = NULL;
	gboolean (*fptr)(GtkTreeViewColumn *, GtkCellRenderer *, gint *, gint*);
	rc = 0;
	handle = dlopen("libgtk-x11-2.0.so", RTLD_LAZY);
	if (handle != NULL) {
		fptr = (gboolean (*)(GtkTreeViewColumn *, GtkCellRenderer *, gint *, gint*))dlsym(handle, "gtk_tree_view_column_cell_get_position");
		if (fptr != NULL) {
			rc = (jboolean)(*fptr)((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gint *)lparg2, (gint *)lparg3);
		}
	}
	}
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "gtk_1tree_1view_1column_1cell_1get_1position\n")
	return rc;
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

#ifndef NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n")
	if (arg0) setGtkColorSelectionDialogFields(env, arg0, (GtkColorSelectionDialog *)arg1);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n")
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

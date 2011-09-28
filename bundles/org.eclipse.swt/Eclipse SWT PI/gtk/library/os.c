/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others. All rights reserved.
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
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_OS_##func

#if (!defined(NO_Call__IIII) && !defined(JNI64)) || (!defined(NO_Call__JJII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(Call__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(Call__JJII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, Call__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, Call__JJII_FUNC);
#endif
	rc = (jint)((jint (*)())arg0)(arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, Call__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, Call__JJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_Call__IIIJ) && !defined(JNI64)) || (!defined(NO_Call__JJIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jlong JNICALL OS_NATIVE(Call__IIIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3)
#else
JNIEXPORT jlong JNICALL OS_NATIVE(Call__JJIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3)
#endif
{
	jlong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, Call__IIIJ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, Call__JJIJ_FUNC);
#endif
	rc = (jlong)((jlong (*)())arg0)(arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, Call__IIIJ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, Call__JJIJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_GDK_1EVENT_1TYPE
JNIEXPORT jint JNICALL OS_NATIVE(GDK_1EVENT_1TYPE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GDK_1EVENT_1TYPE_FUNC);
	rc = (jint)GDK_EVENT_TYPE((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, GDK_1EVENT_1TYPE_FUNC);
	return rc;
}
#endif

#ifndef NO_GDK_1EVENT_1WINDOW
JNIEXPORT jintLong JNICALL OS_NATIVE(GDK_1EVENT_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GDK_1EVENT_1WINDOW_FUNC);
	rc = (jintLong)GDK_EVENT_WINDOW((GdkEventAny *)arg0);
	OS_NATIVE_EXIT(env, that, GDK_1EVENT_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO_GInterfaceInfo_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GInterfaceInfo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GInterfaceInfo_1sizeof_FUNC);
	rc = (jint)GInterfaceInfo_sizeof();
	OS_NATIVE_EXIT(env, that, GInterfaceInfo_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GPollFD_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GPollFD_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GPollFD_1sizeof_FUNC);
	rc = (jint)GPollFD_sizeof();
	OS_NATIVE_EXIT(env, that, GPollFD_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1ACCEL_1LABEL_1GET_1ACCEL_1STRING
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1ACCEL_1LABEL_1GET_1ACCEL_1STRING)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1ACCEL_1LABEL_1GET_1ACCEL_1STRING_FUNC);
	rc = (jintLong)GTK_ACCEL_LABEL_GET_ACCEL_STRING((GtkAccelLabel *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1ACCEL_1LABEL_1GET_1ACCEL_1STRING_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1ACCEL_1LABEL_1SET_1ACCEL_1STRING
JNIEXPORT void JNICALL OS_NATIVE(GTK_1ACCEL_1LABEL_1SET_1ACCEL_1STRING)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1ACCEL_1LABEL_1SET_1ACCEL_1STRING_FUNC);
	GTK_ACCEL_LABEL_SET_ACCEL_STRING((GtkAccelLabel *)arg0, (gchar *)arg1);
	OS_NATIVE_EXIT(env, that, GTK_1ACCEL_1LABEL_1SET_1ACCEL_1STRING_FUNC);
}
#endif

#ifndef NO_GTK_1ENTRY_1IM_1CONTEXT
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1ENTRY_1IM_1CONTEXT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1ENTRY_1IM_1CONTEXT_FUNC);
	rc = (jintLong)GTK_ENTRY_IM_CONTEXT((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1ENTRY_1IM_1CONTEXT_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1HAS_1STEPPER_1A
JNIEXPORT jboolean JNICALL OS_NATIVE(GTK_1RANGE_1HAS_1STEPPER_1A)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1HAS_1STEPPER_1A_FUNC);
	rc = (jboolean)GTK_RANGE_HAS_STEPPER_A((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1HAS_1STEPPER_1A_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1HAS_1STEPPER_1B
JNIEXPORT jboolean JNICALL OS_NATIVE(GTK_1RANGE_1HAS_1STEPPER_1B)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1HAS_1STEPPER_1B_FUNC);
	rc = (jboolean)GTK_RANGE_HAS_STEPPER_B((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1HAS_1STEPPER_1B_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1HAS_1STEPPER_1C
JNIEXPORT jboolean JNICALL OS_NATIVE(GTK_1RANGE_1HAS_1STEPPER_1C)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1HAS_1STEPPER_1C_FUNC);
	rc = (jboolean)GTK_RANGE_HAS_STEPPER_C((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1HAS_1STEPPER_1C_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1HAS_1STEPPER_1D
JNIEXPORT jboolean JNICALL OS_NATIVE(GTK_1RANGE_1HAS_1STEPPER_1D)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1HAS_1STEPPER_1D_FUNC);
	rc = (jboolean)GTK_RANGE_HAS_STEPPER_D((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1HAS_1STEPPER_1D_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1SLIDER_1END
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1RANGE_1SLIDER_1END)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1SLIDER_1END_FUNC);
	rc = (jint)GTK_RANGE_SLIDER_END((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1SLIDER_1END_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1RANGE_1SLIDER_1START
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1RANGE_1SLIDER_1START)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1RANGE_1SLIDER_1START_FUNC);
	rc = (jint)GTK_RANGE_SLIDER_START((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1RANGE_1SLIDER_1START_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1HSCROLLBAR
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1HSCROLLBAR)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1SCROLLED_1WINDOW_1HSCROLLBAR_FUNC);
	rc = (jintLong)GTK_SCROLLED_WINDOW_HSCROLLBAR((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1SCROLLED_1WINDOW_1HSCROLLBAR_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING_FUNC);
	rc = (jint)GTK_SCROLLED_WINDOW_SCROLLBAR_SPACING((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1SCROLLED_1WINDOW_1SCROLLBAR_1SPACING_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1SCROLLED_1WINDOW_1VSCROLLBAR
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1SCROLLED_1WINDOW_1VSCROLLBAR)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1SCROLLED_1WINDOW_1VSCROLLBAR_FUNC);
	rc = (jintLong)GTK_SCROLLED_WINDOW_VSCROLLBAR((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1SCROLLED_1WINDOW_1VSCROLLBAR_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1TEXTVIEW_1IM_1CONTEXT
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1TEXTVIEW_1IM_1CONTEXT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1TEXTVIEW_1IM_1CONTEXT_FUNC);
	rc = (jintLong)GTK_TEXTVIEW_IM_CONTEXT((GtkTextView *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1TEXTVIEW_1IM_1CONTEXT_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1TOOLTIPS_1SET_1ACTIVE
JNIEXPORT void JNICALL OS_NATIVE(GTK_1TOOLTIPS_1SET_1ACTIVE)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1TOOLTIPS_1SET_1ACTIVE_FUNC);
	GTK_TOOLTIPS_SET_ACTIVE((GtkTooltips *)arg0, (GtkTooltipsData *)arg1);
	OS_NATIVE_EXIT(env, that, GTK_1TOOLTIPS_1SET_1ACTIVE_FUNC);
}
#endif

#ifndef NO_GTK_1TOOLTIPS_1TIP_1WINDOW
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1TOOLTIPS_1TIP_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1TOOLTIPS_1TIP_1WINDOW_FUNC);
	rc = (jintLong)GTK_TOOLTIPS_TIP_WINDOW((GtkTooltips *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1TOOLTIPS_1TIP_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1HEIGHT
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1HEIGHT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1HEIGHT_FUNC);
	rc = (jint)GTK_WIDGET_HEIGHT((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1HEIGHT_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1REQUISITION_1HEIGHT
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1REQUISITION_1HEIGHT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1REQUISITION_1HEIGHT_FUNC);
	rc = (jint)GTK_WIDGET_REQUISITION_HEIGHT((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1REQUISITION_1HEIGHT_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1REQUISITION_1WIDTH
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1REQUISITION_1WIDTH)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1REQUISITION_1WIDTH_FUNC);
	rc = (jint)GTK_WIDGET_REQUISITION_WIDTH((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1REQUISITION_1WIDTH_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1SET_1HEIGHT
JNIEXPORT void JNICALL OS_NATIVE(GTK_1WIDGET_1SET_1HEIGHT)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1SET_1HEIGHT_FUNC);
	GTK_WIDGET_SET_HEIGHT((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1SET_1HEIGHT_FUNC);
}
#endif

#ifndef NO_GTK_1WIDGET_1SET_1WIDTH
JNIEXPORT void JNICALL OS_NATIVE(GTK_1WIDGET_1SET_1WIDTH)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1SET_1WIDTH_FUNC);
	GTK_WIDGET_SET_WIDTH((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1SET_1WIDTH_FUNC);
}
#endif

#ifndef NO_GTK_1WIDGET_1SET_1X
JNIEXPORT void JNICALL OS_NATIVE(GTK_1WIDGET_1SET_1X)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1SET_1X_FUNC);
	GTK_WIDGET_SET_X((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1SET_1X_FUNC);
}
#endif

#ifndef NO_GTK_1WIDGET_1SET_1Y
JNIEXPORT void JNICALL OS_NATIVE(GTK_1WIDGET_1SET_1Y)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1SET_1Y_FUNC);
	GTK_WIDGET_SET_Y((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1SET_1Y_FUNC);
}
#endif

#ifndef NO_GTK_1WIDGET_1WIDTH
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1WIDTH)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1WIDTH_FUNC);
	rc = (jint)GTK_WIDGET_WIDTH((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1WIDTH_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1WINDOW
JNIEXPORT jintLong JNICALL OS_NATIVE(GTK_1WIDGET_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1WINDOW_FUNC);
	rc = (jintLong)GTK_WIDGET_WINDOW((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1X
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1X)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1X_FUNC);
	rc = (jint)GTK_WIDGET_X((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1X_FUNC);
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1Y
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1Y)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTK_1WIDGET_1Y_FUNC);
	rc = (jint)GTK_WIDGET_Y((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, GTK_1WIDGET_1Y_FUNC);
	return rc;
}
#endif

#ifndef NO_GTypeInfo_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GTypeInfo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTypeInfo_1sizeof_FUNC);
	rc = (jint)GTypeInfo_sizeof();
	OS_NATIVE_EXIT(env, that, GTypeInfo_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GTypeQuery_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GTypeQuery_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GTypeQuery_1sizeof_FUNC);
	rc = (jint)GTypeQuery_sizeof();
	OS_NATIVE_EXIT(env, that, GTypeQuery_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1OBJECT_1CLASS_1CONSTRUCTOR
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1OBJECT_1CLASS_1CONSTRUCTOR)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1OBJECT_1CLASS_1CONSTRUCTOR_FUNC);
	rc = (jintLong)G_OBJECT_CLASS_CONSTRUCTOR((GObjectClass *)arg0);
	OS_NATIVE_EXIT(env, that, G_1OBJECT_1CLASS_1CONSTRUCTOR_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1OBJECT_1CLASS_1SET_1CONSTRUCTOR
JNIEXPORT void JNICALL OS_NATIVE(G_1OBJECT_1CLASS_1SET_1CONSTRUCTOR)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, G_1OBJECT_1CLASS_1SET_1CONSTRUCTOR_FUNC);
	G_OBJECT_CLASS_SET_CONSTRUCTOR((GObjectClass *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, G_1OBJECT_1CLASS_1SET_1CONSTRUCTOR_FUNC);
}
#endif

#ifndef NO_G_1TYPE_1BOOLEAN
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1TYPE_1BOOLEAN)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1TYPE_1BOOLEAN_FUNC);
	rc = (jintLong)G_TYPE_BOOLEAN;
	OS_NATIVE_EXIT(env, that, G_1TYPE_1BOOLEAN_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1TYPE_1DOUBLE
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1TYPE_1DOUBLE)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1TYPE_1DOUBLE_FUNC);
	rc = (jintLong)G_TYPE_DOUBLE;
	OS_NATIVE_EXIT(env, that, G_1TYPE_1DOUBLE_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1TYPE_1FLOAT
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1TYPE_1FLOAT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1TYPE_1FLOAT_FUNC);
	rc = (jintLong)G_TYPE_FLOAT;
	OS_NATIVE_EXIT(env, that, G_1TYPE_1FLOAT_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1TYPE_1INT
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1TYPE_1INT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1TYPE_1INT_FUNC);
	rc = (jintLong)G_TYPE_INT;
	OS_NATIVE_EXIT(env, that, G_1TYPE_1INT_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1TYPE_1INT64
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1TYPE_1INT64)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1TYPE_1INT64_FUNC);
	rc = (jintLong)G_TYPE_INT64;
	OS_NATIVE_EXIT(env, that, G_1TYPE_1INT64_FUNC);
	return rc;
}
#endif

#ifndef NO_G_1VALUE_1TYPE
JNIEXPORT jintLong JNICALL OS_NATIVE(G_1VALUE_1TYPE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, G_1VALUE_1TYPE_FUNC);
	rc = (jintLong)G_VALUE_TYPE(arg0);
	OS_NATIVE_EXIT(env, that, G_1VALUE_1TYPE_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkColor_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkColor_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkColor_1sizeof_FUNC);
	rc = (jint)GdkColor_sizeof();
	OS_NATIVE_EXIT(env, that, GdkColor_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkDragContext_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkDragContext_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkDragContext_1sizeof_FUNC);
	rc = (jint)GdkDragContext_sizeof();
	OS_NATIVE_EXIT(env, that, GdkDragContext_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventAny_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventAny_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventAny_1sizeof_FUNC);
	rc = (jint)GdkEventAny_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventAny_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventButton_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventButton_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventButton_1sizeof_FUNC);
	rc = (jint)GdkEventButton_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventButton_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventCrossing_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventCrossing_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventCrossing_1sizeof_FUNC);
	rc = (jint)GdkEventCrossing_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventCrossing_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventExpose_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventExpose_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventExpose_1sizeof_FUNC);
	rc = (jint)GdkEventExpose_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventExpose_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventFocus_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventFocus_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventFocus_1sizeof_FUNC);
	rc = (jint)GdkEventFocus_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventFocus_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventKey_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventKey_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventKey_1sizeof_FUNC);
	rc = (jint)GdkEventKey_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventKey_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventMotion_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventMotion_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventMotion_1sizeof_FUNC);
	rc = (jint)GdkEventMotion_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventMotion_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventProperty_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventProperty_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventProperty_1sizeof_FUNC);
	rc = (jint)GdkEventProperty_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventProperty_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventScroll_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventScroll_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventScroll_1sizeof_FUNC);
	rc = (jint)GdkEventScroll_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventScroll_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventVisibility_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventVisibility_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventVisibility_1sizeof_FUNC);
	rc = (jint)GdkEventVisibility_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventVisibility_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEventWindowState_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEventWindowState_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEventWindowState_1sizeof_FUNC);
	rc = (jint)GdkEventWindowState_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEventWindowState_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkEvent_1sizeof_FUNC);
	rc = (jint)GdkEvent_sizeof();
	OS_NATIVE_EXIT(env, that, GdkEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkGCValues_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkGCValues_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkGCValues_1sizeof_FUNC);
	rc = (jint)GdkGCValues_sizeof();
	OS_NATIVE_EXIT(env, that, GdkGCValues_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkGeometry_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkGeometry_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkGeometry_1sizeof_FUNC);
	rc = (jint)GdkGeometry_sizeof();
	OS_NATIVE_EXIT(env, that, GdkGeometry_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkImage_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkImage_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkImage_1sizeof_FUNC);
	rc = (jint)GdkImage_sizeof();
	OS_NATIVE_EXIT(env, that, GdkImage_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkRectangle_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkRectangle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkRectangle_1sizeof_FUNC);
	rc = (jint)GdkRectangle_sizeof();
	OS_NATIVE_EXIT(env, that, GdkRectangle_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkVisual_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkVisual_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkVisual_1sizeof_FUNC);
	rc = (jint)GdkVisual_sizeof();
	OS_NATIVE_EXIT(env, that, GdkVisual_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GdkWindowAttr_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GdkWindowAttr_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdkWindowAttr_1sizeof_FUNC);
	rc = (jint)GdkWindowAttr_sizeof();
	OS_NATIVE_EXIT(env, that, GdkWindowAttr_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkAdjustment_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkAdjustment_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkAdjustment_1sizeof_FUNC);
	rc = (jint)GtkAdjustment_sizeof();
	OS_NATIVE_EXIT(env, that, GtkAdjustment_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkAllocation_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkAllocation_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkAllocation_1sizeof_FUNC);
	rc = (jint)GtkAllocation_sizeof();
	OS_NATIVE_EXIT(env, that, GtkAllocation_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkBorder_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkBorder_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkBorder_1sizeof_FUNC);
	rc = (jint)GtkBorder_sizeof();
	OS_NATIVE_EXIT(env, that, GtkBorder_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererPixbufClass_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererPixbufClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererPixbufClass_1sizeof_FUNC);
	rc = (jint)GtkCellRendererPixbufClass_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererPixbufClass_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererPixbuf_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererPixbuf_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererPixbuf_1sizeof_FUNC);
	rc = (jint)GtkCellRendererPixbuf_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererPixbuf_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererTextClass_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererTextClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererTextClass_1sizeof_FUNC);
	rc = (jint)GtkCellRendererTextClass_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererTextClass_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererText_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererText_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererText_1sizeof_FUNC);
	rc = (jint)GtkCellRendererText_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererText_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererToggleClass_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererToggleClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererToggleClass_1sizeof_FUNC);
	rc = (jint)GtkCellRendererToggleClass_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererToggleClass_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCellRendererToggle_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCellRendererToggle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCellRendererToggle_1sizeof_FUNC);
	rc = (jint)GtkCellRendererToggle_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCellRendererToggle_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkColorSelectionDialog_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkColorSelectionDialog_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkColorSelectionDialog_1sizeof_FUNC);
	rc = (jint)GtkColorSelectionDialog_sizeof();
	OS_NATIVE_EXIT(env, that, GtkColorSelectionDialog_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkCombo_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkCombo_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkCombo_1sizeof_FUNC);
	rc = (jint)GtkCombo_sizeof();
	OS_NATIVE_EXIT(env, that, GtkCombo_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkFileSelection_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkFileSelection_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkFileSelection_1sizeof_FUNC);
	rc = (jint)GtkFileSelection_sizeof();
	OS_NATIVE_EXIT(env, that, GtkFileSelection_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkFixedClass_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkFixedClass_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkFixedClass_1sizeof_FUNC);
	rc = (jint)GtkFixedClass_sizeof();
	OS_NATIVE_EXIT(env, that, GtkFixedClass_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkFixed_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkFixed_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkFixed_1sizeof_FUNC);
	rc = (jint)GtkFixed_sizeof();
	OS_NATIVE_EXIT(env, that, GtkFixed_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkRequisition_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkRequisition_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkRequisition_1sizeof_FUNC);
	rc = (jint)GtkRequisition_sizeof();
	OS_NATIVE_EXIT(env, that, GtkRequisition_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkSelectionData_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkSelectionData_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkSelectionData_1sizeof_FUNC);
	rc = (jint)GtkSelectionData_sizeof();
	OS_NATIVE_EXIT(env, that, GtkSelectionData_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkTargetEntry_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTargetEntry_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkTargetEntry_1sizeof_FUNC);
	rc = (jint)GtkTargetEntry_sizeof();
	OS_NATIVE_EXIT(env, that, GtkTargetEntry_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkTargetPair_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTargetPair_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkTargetPair_1sizeof_FUNC);
	rc = (jint)GtkTargetPair_sizeof();
	OS_NATIVE_EXIT(env, that, GtkTargetPair_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkTextIter_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTextIter_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkTextIter_1sizeof_FUNC);
	rc = (jint)GtkTextIter_sizeof();
	OS_NATIVE_EXIT(env, that, GtkTextIter_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GtkTreeIter_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GtkTreeIter_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GtkTreeIter_1sizeof_FUNC);
	rc = (jint)GtkTreeIter_sizeof();
	OS_NATIVE_EXIT(env, that, GtkTreeIter_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoAttrColor_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoAttrColor_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoAttrColor_1sizeof_FUNC);
	rc = (jint)PangoAttrColor_sizeof();
	OS_NATIVE_EXIT(env, that, PangoAttrColor_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoAttrInt_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoAttrInt_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoAttrInt_1sizeof_FUNC);
	rc = (jint)PangoAttrInt_sizeof();
	OS_NATIVE_EXIT(env, that, PangoAttrInt_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoAttribute_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoAttribute_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoAttribute_1sizeof_FUNC);
	rc = (jint)PangoAttribute_sizeof();
	OS_NATIVE_EXIT(env, that, PangoAttribute_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoItem_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoItem_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoItem_1sizeof_FUNC);
	rc = (jint)PangoItem_sizeof();
	OS_NATIVE_EXIT(env, that, PangoItem_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoLayoutLine_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoLayoutLine_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoLayoutLine_1sizeof_FUNC);
	rc = (jint)PangoLayoutLine_sizeof();
	OS_NATIVE_EXIT(env, that, PangoLayoutLine_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoLayoutRun_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoLayoutRun_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoLayoutRun_1sizeof_FUNC);
	rc = (jint)PangoLayoutRun_sizeof();
	OS_NATIVE_EXIT(env, that, PangoLayoutRun_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoLogAttr_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoLogAttr_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoLogAttr_1sizeof_FUNC);
	rc = (jint)PangoLogAttr_sizeof();
	OS_NATIVE_EXIT(env, that, PangoLogAttr_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PangoRectangle_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PangoRectangle_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PangoRectangle_1sizeof_FUNC);
	rc = (jint)PangoRectangle_sizeof();
	OS_NATIVE_EXIT(env, that, PangoRectangle_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_RTLD_1GLOBAL
JNIEXPORT jint JNICALL OS_NATIVE(RTLD_1GLOBAL)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RTLD_1GLOBAL_FUNC);
	rc = (jint)RTLD_GLOBAL;
	OS_NATIVE_EXIT(env, that, RTLD_1GLOBAL_FUNC);
	return rc;
}
#endif

#ifndef NO_RTLD_1LAZY
JNIEXPORT jint JNICALL OS_NATIVE(RTLD_1LAZY)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RTLD_1LAZY_FUNC);
	rc = (jint)RTLD_LAZY;
	OS_NATIVE_EXIT(env, that, RTLD_1LAZY_FUNC);
	return rc;
}
#endif

#ifndef NO_RTLD_1NOW
JNIEXPORT jint JNICALL OS_NATIVE(RTLD_1NOW)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RTLD_1NOW_FUNC);
	rc = (jint)RTLD_NOW;
	OS_NATIVE_EXIT(env, that, RTLD_1NOW_FUNC);
	return rc;
}
#endif

#ifndef NO_XAnyEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XAnyEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XAnyEvent_1sizeof_FUNC);
	rc = (jint)XAnyEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XAnyEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XClientMessageEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XClientMessageEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XClientMessageEvent_1sizeof_FUNC);
	rc = (jint)XClientMessageEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XClientMessageEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XCrossingEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XCrossingEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCrossingEvent_1sizeof_FUNC);
	rc = (jint)XCrossingEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XCrossingEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XEvent_1sizeof_FUNC);
	rc = (jint)XEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XExposeEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XExposeEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XExposeEvent_1sizeof_FUNC);
	rc = (jint)XExposeEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XExposeEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XFocusChangeEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XFocusChangeEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XFocusChangeEvent_1sizeof_FUNC);
	rc = (jint)XFocusChangeEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XFocusChangeEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XRenderPictureAttributes_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XRenderPictureAttributes_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XRenderPictureAttributes_1sizeof_FUNC);
	rc = (jint)XRenderPictureAttributes_sizeof();
	OS_NATIVE_EXIT(env, that, XRenderPictureAttributes_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XVisibilityEvent_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XVisibilityEvent_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XVisibilityEvent_1sizeof_FUNC);
	rc = (jint)XVisibilityEvent_sizeof();
	OS_NATIVE_EXIT(env, that, XVisibilityEvent_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_XWindowChanges_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XWindowChanges_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XWindowChanges_1sizeof_FUNC);
	rc = (jint)XWindowChanges_sizeof();
	OS_NATIVE_EXIT(env, that, XWindowChanges_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_X_1EVENT_1TYPE
JNIEXPORT jint JNICALL OS_NATIVE(X_1EVENT_1TYPE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, X_1EVENT_1TYPE_FUNC);
	rc = (jint)X_EVENT_TYPE((XEvent *)arg0);
	OS_NATIVE_EXIT(env, that, X_1EVENT_1TYPE_FUNC);
	return rc;
}
#endif

#ifndef NO_X_1EVENT_1WINDOW
JNIEXPORT jintLong JNICALL OS_NATIVE(X_1EVENT_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, X_1EVENT_1WINDOW_FUNC);
	rc = (jintLong)X_EVENT_WINDOW((XAnyEvent *)arg0);
	OS_NATIVE_EXIT(env, that, X_1EVENT_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO__1Call
JNIEXPORT jint JNICALL OS_NATIVE(_1Call)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1Call_FUNC);
	rc = (jint)((jint (*)())arg0)(arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1Call_FUNC);
	return rc;
}
#endif

#ifndef NO__1FcConfigAppFontAddFile
JNIEXPORT jboolean JNICALL OS_NATIVE(_1FcConfigAppFontAddFile)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1FcConfigAppFontAddFile_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)FcConfigAppFontAddFile(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, FcConfigAppFontAddFile)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1FcConfigAppFontAddFile_FUNC);
	return rc;
}
#endif

#ifndef NO__1GDK_1DISPLAY
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GDK_1DISPLAY)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GDK_1DISPLAY_FUNC);
	rc = (jintLong)GDK_DISPLAY();
	OS_NATIVE_EXIT(env, that, _1GDK_1DISPLAY_FUNC);
	return rc;
}
#endif

#ifndef NO__1GDK_1PIXMAP_1XID
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GDK_1PIXMAP_1XID)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GDK_1PIXMAP_1XID_FUNC);
	rc = (jintLong)GDK_PIXMAP_XID((GdkPixmap *)arg0);
	OS_NATIVE_EXIT(env, that, _1GDK_1PIXMAP_1XID_FUNC);
	return rc;
}
#endif

#ifndef NO__1GDK_1ROOT_1PARENT
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GDK_1ROOT_1PARENT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GDK_1ROOT_1PARENT_FUNC);
	rc = (jintLong)GDK_ROOT_PARENT();
	OS_NATIVE_EXIT(env, that, _1GDK_1ROOT_1PARENT_FUNC);
	return rc;
}
#endif

#ifndef NO__1GDK_1TYPE_1COLOR
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GDK_1TYPE_1COLOR)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GDK_1TYPE_1COLOR_FUNC);
	rc = (jintLong)GDK_TYPE_COLOR;
	OS_NATIVE_EXIT(env, that, _1GDK_1TYPE_1COLOR_FUNC);
	return rc;
}
#endif

#ifndef NO__1GDK_1TYPE_1PIXBUF
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GDK_1TYPE_1PIXBUF)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GDK_1TYPE_1PIXBUF_FUNC);
	rc = (jintLong)GDK_TYPE_PIXBUF;
	OS_NATIVE_EXIT(env, that, _1GDK_1TYPE_1PIXBUF_FUNC);
	return rc;
}
#endif

#ifndef NO__1GString_1len
JNIEXPORT jint JNICALL OS_NATIVE(_1GString_1len)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1GString_1len_FUNC);
	rc = (jint)((GString *)arg0)->len;
	OS_NATIVE_EXIT(env, that, _1GString_1len_FUNC);
	return rc;
}
#endif

#ifndef NO__1GString_1str
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GString_1str)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GString_1str_FUNC);
	rc = (jintLong)((GString *)arg0)->str;
	OS_NATIVE_EXIT(env, that, _1GString_1str_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1ACCEL_1LABEL
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1ACCEL_1LABEL)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1ACCEL_1LABEL_FUNC);
	rc = (jboolean)GTK_IS_ACCEL_LABEL(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1ACCEL_1LABEL_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1BUTTON
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1BUTTON)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1BUTTON_FUNC);
	rc = (jboolean)GTK_IS_BUTTON(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1BUTTON_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1CELL_1RENDERER_1PIXBUF
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1CELL_1RENDERER_1PIXBUF)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1CELL_1RENDERER_1PIXBUF_FUNC);
	rc = (jboolean)GTK_IS_CELL_RENDERER_PIXBUF(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1CELL_1RENDERER_1PIXBUF_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1CELL_1RENDERER_1TEXT
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1CELL_1RENDERER_1TEXT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1CELL_1RENDERER_1TEXT_FUNC);
	rc = (jboolean)GTK_IS_CELL_RENDERER_TEXT(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1CELL_1RENDERER_1TEXT_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1CELL_1RENDERER_1TOGGLE
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1CELL_1RENDERER_1TOGGLE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1CELL_1RENDERER_1TOGGLE_FUNC);
	rc = (jboolean)GTK_IS_CELL_RENDERER_TOGGLE(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1CELL_1RENDERER_1TOGGLE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1CONTAINER
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1CONTAINER)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1CONTAINER_FUNC);
	rc = (jboolean)GTK_IS_CONTAINER(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1CONTAINER_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1IMAGE_1MENU_1ITEM
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1IMAGE_1MENU_1ITEM)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1IMAGE_1MENU_1ITEM_FUNC);
	rc = (jboolean)GTK_IS_IMAGE_MENU_ITEM(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1IMAGE_1MENU_1ITEM_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1LABEL
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1LABEL)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1LABEL_FUNC);
	rc = (jboolean)GTK_IS_LABEL(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1LABEL_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1MENU_1ITEM
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1MENU_1ITEM)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1MENU_1ITEM_FUNC);
	rc = (jboolean)GTK_IS_MENU_ITEM(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1MENU_1ITEM_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1PLUG
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1PLUG)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1PLUG_FUNC);
	rc = (jboolean)GTK_IS_PLUG(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1PLUG_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1SCROLLED_1WINDOW
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1SCROLLED_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1SCROLLED_1WINDOW_FUNC);
	rc = (jboolean)GTK_IS_SCROLLED_WINDOW(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1SCROLLED_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1SOCKET
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1SOCKET)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1SOCKET_FUNC);
	rc = (jboolean)GTK_IS_SOCKET(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1SOCKET_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1IS_1WINDOW
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1IS_1WINDOW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1IS_1WINDOW_FUNC);
	rc = (jboolean)GTK_IS_WINDOW(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1IS_1WINDOW_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1STOCK_1CANCEL
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1STOCK_1CANCEL)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1STOCK_1CANCEL_FUNC);
	rc = (jintLong)GTK_STOCK_CANCEL;
	OS_NATIVE_EXIT(env, that, _1GTK_1STOCK_1CANCEL_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1STOCK_1OK
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1STOCK_1OK)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1STOCK_1OK_FUNC);
	rc = (jintLong)GTK_STOCK_OK;
	OS_NATIVE_EXIT(env, that, _1GTK_1STOCK_1OK_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1CELL_1RENDERER_1PIXBUF
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1CELL_1RENDERER_1PIXBUF)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1PIXBUF_FUNC);
	rc = (jintLong)GTK_TYPE_CELL_RENDERER_PIXBUF;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1PIXBUF_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1CELL_1RENDERER_1TEXT
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1CELL_1RENDERER_1TEXT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1TEXT_FUNC);
	rc = (jintLong)GTK_TYPE_CELL_RENDERER_TEXT;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1TEXT_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1CELL_1RENDERER_1TOGGLE
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1CELL_1RENDERER_1TOGGLE)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1TOGGLE_FUNC);
	rc = (jintLong)GTK_TYPE_CELL_RENDERER_TOGGLE;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1CELL_1RENDERER_1TOGGLE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1FIXED
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1FIXED)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1FIXED_FUNC);
	rc = (jintLong)GTK_TYPE_FIXED;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1FIXED_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1MENU
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1MENU)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1MENU_FUNC);
	rc = (jintLong)GTK_TYPE_MENU;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1MENU_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1TYPE_1WIDGET
JNIEXPORT jintLong JNICALL OS_NATIVE(_1GTK_1TYPE_1WIDGET)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1TYPE_1WIDGET_FUNC);
	rc = (jintLong)GTK_TYPE_WIDGET;
	OS_NATIVE_EXIT(env, that, _1GTK_1TYPE_1WIDGET_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1FLAGS
JNIEXPORT jint JNICALL OS_NATIVE(_1GTK_1WIDGET_1FLAGS)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1FLAGS_FUNC);
	rc = (jint)GTK_WIDGET_FLAGS(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1FLAGS_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1HAS_1DEFAULT
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1HAS_1DEFAULT)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1HAS_1DEFAULT_FUNC);
	rc = (jboolean)GTK_WIDGET_HAS_DEFAULT(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1HAS_1DEFAULT_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1HAS_1FOCUS
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1HAS_1FOCUS)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1HAS_1FOCUS_FUNC);
	rc = (jboolean)GTK_WIDGET_HAS_FOCUS(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1HAS_1FOCUS_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1IS_1SENSITIVE
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1IS_1SENSITIVE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1IS_1SENSITIVE_FUNC);
	rc = (jboolean)GTK_WIDGET_IS_SENSITIVE(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1IS_1SENSITIVE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1MAPPED
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1MAPPED)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1MAPPED_FUNC);
	rc = (jboolean)GTK_WIDGET_MAPPED(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1MAPPED_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1SENSITIVE
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1SENSITIVE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1SENSITIVE_FUNC);
	rc = (jboolean)GTK_WIDGET_SENSITIVE(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1SENSITIVE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1SET_1FLAGS
JNIEXPORT void JNICALL OS_NATIVE(_1GTK_1WIDGET_1SET_1FLAGS)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1SET_1FLAGS_FUNC);
	GTK_WIDGET_SET_FLAGS(arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1SET_1FLAGS_FUNC);
}
#endif

#ifndef NO__1GTK_1WIDGET_1STATE
JNIEXPORT jint JNICALL OS_NATIVE(_1GTK_1WIDGET_1STATE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1STATE_FUNC);
	rc = (jint)GTK_WIDGET_STATE(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1STATE_FUNC);
	return rc;
}
#endif

#ifndef NO__1GTK_1WIDGET_1UNSET_1FLAGS
JNIEXPORT void JNICALL OS_NATIVE(_1GTK_1WIDGET_1UNSET_1FLAGS)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1UNSET_1FLAGS_FUNC);
	GTK_WIDGET_UNSET_FLAGS(arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1UNSET_1FLAGS_FUNC);
}
#endif

#ifndef NO__1GTK_1WIDGET_1VISIBLE
JNIEXPORT jboolean JNICALL OS_NATIVE(_1GTK_1WIDGET_1VISIBLE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1GTK_1WIDGET_1VISIBLE_FUNC);
	rc = (jboolean)GTK_WIDGET_VISIBLE(arg0);
	OS_NATIVE_EXIT(env, that, _1GTK_1WIDGET_1VISIBLE_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1OBJECT_1CLASS
JNIEXPORT jintLong JNICALL OS_NATIVE(_1G_1OBJECT_1CLASS)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1OBJECT_1CLASS_FUNC);
	rc = (jintLong)G_OBJECT_CLASS(arg0);
	OS_NATIVE_EXIT(env, that, _1G_1OBJECT_1CLASS_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1OBJECT_1GET_1CLASS
JNIEXPORT jintLong JNICALL OS_NATIVE(_1G_1OBJECT_1GET_1CLASS)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1OBJECT_1GET_1CLASS_FUNC);
	rc = (jintLong)G_OBJECT_GET_CLASS(arg0);
	OS_NATIVE_EXIT(env, that, _1G_1OBJECT_1GET_1CLASS_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1OBJECT_1TYPE
JNIEXPORT jintLong JNICALL OS_NATIVE(_1G_1OBJECT_1TYPE)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1OBJECT_1TYPE_FUNC);
	rc = (jintLong)G_OBJECT_TYPE(arg0);
	OS_NATIVE_EXIT(env, that, _1G_1OBJECT_1TYPE_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1OBJECT_1TYPE_1NAME
JNIEXPORT jintLong JNICALL OS_NATIVE(_1G_1OBJECT_1TYPE_1NAME)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1OBJECT_1TYPE_1NAME_FUNC);
	rc = (jintLong)G_OBJECT_TYPE_NAME(arg0);
	OS_NATIVE_EXIT(env, that, _1G_1OBJECT_1TYPE_1NAME_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1TYPE_1CHECK_1INSTANCE_1TYPE
JNIEXPORT jboolean JNICALL OS_NATIVE(_1G_1TYPE_1CHECK_1INSTANCE_1TYPE)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1TYPE_1CHECK_1INSTANCE_1TYPE_FUNC);
	rc = (jboolean)G_TYPE_CHECK_INSTANCE_TYPE(arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1G_1TYPE_1CHECK_1INSTANCE_1TYPE_FUNC);
	return rc;
}
#endif

#ifndef NO__1G_1TYPE_1STRING
JNIEXPORT jintLong JNICALL OS_NATIVE(_1G_1TYPE_1STRING)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1G_1TYPE_1STRING_FUNC);
	rc = (jintLong)G_TYPE_STRING;
	OS_NATIVE_EXIT(env, that, _1G_1TYPE_1STRING_FUNC);
	return rc;
}
#endif

#ifndef NO__1PANGO_1PIXELS
JNIEXPORT jint JNICALL OS_NATIVE(_1PANGO_1PIXELS)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1PANGO_1PIXELS_FUNC);
	rc = (jint)PANGO_PIXELS(arg0);
	OS_NATIVE_EXIT(env, that, _1PANGO_1PIXELS_FUNC);
	return rc;
}
#endif

#ifndef NO__1PANGO_1TYPE_1FONT_1DESCRIPTION
JNIEXPORT jintLong JNICALL OS_NATIVE(_1PANGO_1TYPE_1FONT_1DESCRIPTION)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1PANGO_1TYPE_1FONT_1DESCRIPTION_FUNC);
	rc = (jintLong)PANGO_TYPE_FONT_DESCRIPTION;
	OS_NATIVE_EXIT(env, that, _1PANGO_1TYPE_1FONT_1DESCRIPTION_FUNC);
	return rc;
}
#endif

#ifndef NO__1PANGO_1TYPE_1LAYOUT
JNIEXPORT jintLong JNICALL OS_NATIVE(_1PANGO_1TYPE_1LAYOUT)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1PANGO_1TYPE_1LAYOUT_FUNC);
	rc = (jintLong)PANGO_TYPE_LAYOUT;
	OS_NATIVE_EXIT(env, that, _1PANGO_1TYPE_1LAYOUT_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCheckIfEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XCheckIfEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckIfEvent_FUNC);
	rc = (jboolean)XCheckIfEvent((Display *)arg0, (XEvent *)arg1, (Bool (*)())arg2, (XPointer)arg3);
	OS_NATIVE_EXIT(env, that, _1XCheckIfEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCheckMaskEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XCheckMaskEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckMaskEvent_FUNC);
	rc = (jboolean)XCheckMaskEvent((Display *)arg0, (long)arg1, (XEvent *)arg2);
	OS_NATIVE_EXIT(env, that, _1XCheckMaskEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCheckWindowEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XCheckWindowEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckWindowEvent_FUNC);
	rc = (jboolean)XCheckWindowEvent((Display *)arg0, (Window)arg1, (long)arg2, (XEvent *)arg3);
	OS_NATIVE_EXIT(env, that, _1XCheckWindowEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultRootWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XDefaultRootWindow)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultRootWindow_FUNC);
	rc = (jintLong)XDefaultRootWindow((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultRootWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultScreen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultScreen_FUNC);
	rc = (jint)XDefaultScreen((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFlush
JNIEXPORT void JNICALL OS_NATIVE(_1XFlush)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFlush_FUNC);
	XFlush((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XFlush_FUNC);
}
#endif

#ifndef NO__1XFree
JNIEXPORT void JNICALL OS_NATIVE(_1XFree)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFree_FUNC);
	XFree((void *)arg0);
	OS_NATIVE_EXIT(env, that, _1XFree_FUNC);
}
#endif

#ifndef NO__1XGetSelectionOwner
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XGetSelectionOwner)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetSelectionOwner_FUNC);
	rc = (jintLong)XGetSelectionOwner((Display *)arg0, (Atom)arg1);
	OS_NATIVE_EXIT(env, that, _1XGetSelectionOwner_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetWindowProperty
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XGetWindowProperty)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jboolean arg5, jintLong arg6, jintLongArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jintLongArray arg11)
{
	jintLong *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jintLong *lparg11=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetWindowProperty_FUNC);
	if (arg7) if ((lparg7 = (*env)->GetIntLongArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = (*env)->GetIntLongArrayElements(env, arg11, NULL)) == NULL) goto fail;
	rc = (jintLong)XGetWindowProperty((Display *)arg0, (Window)arg1, (Atom)arg2, arg3, arg4, arg5, (Atom)arg6, (Atom*)lparg7, (int *)lparg8, (unsigned long *)lparg9, (unsigned long *)lparg10, (unsigned char **)lparg11);
fail:
	if (arg11 && lparg11) (*env)->ReleaseIntLongArrayElements(env, arg11, lparg11, 0);
	if (arg10 && lparg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntLongArrayElements(env, arg7, lparg7, 0);
	OS_NATIVE_EXIT(env, that, _1XGetWindowProperty_FUNC);
	return rc;
}
#endif

#ifndef NO__1XInternAtom
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XInternAtom)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XInternAtom_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)XInternAtom((Display *)arg0, (char *)lparg1, (Bool)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XInternAtom_FUNC);
	return rc;
}
#endif

#ifndef NO__1XKeysymToKeycode
JNIEXPORT jint JNICALL OS_NATIVE(_1XKeysymToKeycode)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XKeysymToKeycode_FUNC);
	rc = (jint)XKeysymToKeycode((Display *)arg0, (KeySym)arg1);
	OS_NATIVE_EXIT(env, that, _1XKeysymToKeycode_FUNC);
	return rc;
}
#endif

#ifndef NO__1XListProperties
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XListProperties)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XListProperties_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)XListProperties((Display *)arg0, (Window)arg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XListProperties_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryPointer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XQueryPointer_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)XQueryPointer((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (int *)lparg4, (int *)lparg5, (int *)lparg6, (int *)lparg7, (unsigned int *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XQueryPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryTree
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryTree)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4, jintArray arg5)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XQueryTree_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XQueryTree((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (Window **)lparg4, (unsigned int *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XQueryTree_FUNC);
	return rc;
}
#endif

#ifndef NO__1XReconfigureWMWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XReconfigureWMWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4)
{
	XWindowChanges _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XReconfigureWMWindow_FUNC);
	if (arg4) if ((lparg4 = getXWindowChangesFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XReconfigureWMWindow((Display *)arg0, (Window)arg1, arg2, arg3, lparg4);
fail:
	OS_NATIVE_EXIT(env, that, _1XReconfigureWMWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderComposite
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderComposite)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	OS_NATIVE_ENTER(env, that, _1XRenderComposite_FUNC);
/*
	XRenderComposite(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderComposite)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint, jintLong, jintLong, jintLong, jint, jint, jint, jint, jint, jint, jint, jint))fp)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderComposite_FUNC);
}
#endif

#ifndef NO__1XRenderCreatePicture
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderCreatePicture)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jobject arg4)
{
	XRenderPictureAttributes _arg4, *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderCreatePicture_FUNC);
	if (arg4) if ((lparg4 = getXRenderPictureAttributesFields(env, arg4, &_arg4)) == NULL) goto fail;
/*
	rc = (jintLong)XRenderCreatePicture(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderCreatePicture)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong, jintLong, jintLong, XRenderPictureAttributes *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	OS_NATIVE_EXIT(env, that, _1XRenderCreatePicture_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFindStandardFormat
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderFindStandardFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderFindStandardFormat_FUNC);
/*
	rc = (jintLong)XRenderFindStandardFormat(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderFindStandardFormat)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFindStandardFormat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFindVisualFormat
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderFindVisualFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderFindVisualFormat_FUNC);
/*
	rc = (jintLong)XRenderFindVisualFormat(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderFindVisualFormat)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFindVisualFormat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFreePicture
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderFreePicture)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1XRenderFreePicture_FUNC);
/*
	XRenderFreePicture(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderFreePicture)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFreePicture_FUNC);
}
#endif

#ifndef NO__1XRenderQueryExtension
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XRenderQueryExtension)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderQueryExtension_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)XRenderQueryExtension(arg0, lparg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderQueryExtension)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jint *, jint *))fp)(arg0, lparg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderQueryExtension_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderQueryVersion
JNIEXPORT jint JNICALL OS_NATIVE(_1XRenderQueryVersion)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderQueryVersion_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)XRenderQueryVersion(arg0, lparg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderQueryVersion)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint *, jint *))fp)(arg0, lparg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderQueryVersion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderSetPictureClipRectangles
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderSetPictureClipRectangles)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jshortArray arg4, jint arg5)
{
	jshort *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1XRenderSetPictureClipRectangles_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	XRenderSetPictureClipRectangles(arg0, arg1, arg2, arg3, lparg4, arg5);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderSetPictureClipRectangles)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jshort *, jint))fp)(arg0, arg1, arg2, arg3, lparg4, arg5);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderSetPictureClipRectangles_FUNC);
}
#endif

#ifndef NO__1XRenderSetPictureTransform
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderSetPictureTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1XRenderSetPictureTransform_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	XRenderSetPictureTransform(arg0, arg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, XRenderSetPictureTransform)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jint *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderSetPictureTransform_FUNC);
}
#endif

#ifndef NO__1XSendEvent
JNIEXPORT jint JNICALL OS_NATIVE(_1XSendEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2, jintLong arg3, jintLong arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSendEvent_FUNC);
	rc = (jint)XSendEvent((Display *)arg0, (Window)arg1, arg2, arg3, (XEvent *)arg4);
	OS_NATIVE_EXIT(env, that, _1XSendEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetErrorHandler
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XSetErrorHandler)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetErrorHandler_FUNC);
	rc = (jintLong)XSetErrorHandler((XErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, _1XSetErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetIOErrorHandler
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XSetIOErrorHandler)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetIOErrorHandler_FUNC);
	rc = (jintLong)XSetIOErrorHandler((XIOErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, _1XSetIOErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetInputFocus)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetInputFocus_FUNC);
	rc = (jint)XSetInputFocus((Display *)arg0, (Window)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XSetInputFocus_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetSelectionOwner
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XSetSelectionOwner)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetSelectionOwner_FUNC);
	rc = (jintLong)XSetSelectionOwner((Display *)arg0, (Atom)arg1, arg2, (Time)arg3);
	OS_NATIVE_EXIT(env, that, _1XSetSelectionOwner_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetTransientForHint
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetTransientForHint)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetTransientForHint_FUNC);
	rc = (jint)XSetTransientForHint((Display *)arg0, (Window)arg1, (Window)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetTransientForHint_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSynchronize
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XSynchronize)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSynchronize_FUNC);
	rc = (jintLong)XSynchronize((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XSynchronize_FUNC);
	return rc;
}
#endif

#ifndef NO__1XTestFakeButtonEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeButtonEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jintLong arg3)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeButtonEvent_FUNC);
	XTestFakeButtonEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, _1XTestFakeButtonEvent_FUNC);
}
#endif

#ifndef NO__1XTestFakeKeyEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeKeyEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jintLong arg3)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeKeyEvent_FUNC);
	XTestFakeKeyEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, _1XTestFakeKeyEvent_FUNC);
}
#endif

#ifndef NO__1XTestFakeMotionEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeMotionEvent)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintLong arg4)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeMotionEvent_FUNC);
	XTestFakeMotionEvent((Display *)arg0, arg1, arg2, arg3, (unsigned long)arg4);
	OS_NATIVE_EXIT(env, that, _1XTestFakeMotionEvent_FUNC);
}
#endif

#ifndef NO__1XWarpPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XWarpPointer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XWarpPointer_FUNC);
	rc = (jint)XWarpPointer((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1XWarpPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1access
JNIEXPORT jint JNICALL OS_NATIVE(_1access)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1access_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)access((const char*)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1access_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1object_1add_1relationship
JNIEXPORT jboolean JNICALL OS_NATIVE(_1atk_1object_1add_1relationship)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1atk_1object_1add_1relationship_FUNC);
/*
	rc = (jboolean)atk_object_add_relationship((AtkObject *)arg0, (AtkRelationType)arg1, (AtkObject *)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, atk_object_add_relationship)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(AtkObject *, AtkRelationType, AtkObject *))fp)((AtkObject *)arg0, (AtkRelationType)arg1, (AtkObject *)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1atk_1object_1add_1relationship_FUNC);
	return rc;
}
#endif

#ifndef NO__1atk_1object_1remove_1relationship
JNIEXPORT jboolean JNICALL OS_NATIVE(_1atk_1object_1remove_1relationship)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1atk_1object_1remove_1relationship_FUNC);
/*
	rc = (jboolean)atk_object_remove_relationship((AtkObject *)arg0, (AtkRelationType)arg1, (AtkObject *)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, atk_object_remove_relationship)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(AtkObject *, AtkRelationType, AtkObject *))fp)((AtkObject *)arg0, (AtkRelationType)arg1, (AtkObject *)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1atk_1object_1remove_1relationship_FUNC);
	return rc;
}
#endif

#ifndef NO__1call
JNIEXPORT jintLong JNICALL OS_NATIVE(_1call)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jintLong arg7)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1call_FUNC);
	rc = (jintLong)((jintLong (*)())arg0)(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, _1call_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlclose
JNIEXPORT jint JNICALL OS_NATIVE(_1dlclose)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlclose_FUNC);
	rc = (jint)dlclose((void *)arg0);
	OS_NATIVE_EXIT(env, that, _1dlclose_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlopen
JNIEXPORT jintLong JNICALL OS_NATIVE(_1dlopen)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlopen_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)dlopen((const char *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1dlopen_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlsym
JNIEXPORT jintLong JNICALL OS_NATIVE(_1dlsym)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlsym_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)dlsym((void *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1dlsym_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1create_1from_1commandline
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1create_1from_1commandline)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1create_1from_1commandline_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_app_info_create_from_commandline(lparg0, lparg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_create_from_commandline)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *, jbyte *, jintLong, jintLong))fp)(lparg0, lparg1, arg2, arg3);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1create_1from_1commandline_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1all
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1all)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1all_FUNC);
/*
	rc = (jintLong)g_app_info_get_all();
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_all)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1all_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1default_1for_1type
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1default_1for_1type)
	(JNIEnv *env, jclass that, jbyteArray arg0, jboolean arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1default_1for_1type_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_app_info_get_default_for_type(lparg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_default_for_type)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *, jboolean))fp)(lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1default_1for_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1executable
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1executable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1executable_FUNC);
/*
	rc = (jintLong)g_app_info_get_executable(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_executable)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1executable_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1icon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1icon)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1icon_FUNC);
/*
	rc = (jintLong)g_app_info_get_icon(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_icon)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1id
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1id)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1id_FUNC);
/*
	rc = (jintLong)g_app_info_get_id(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_id)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1id_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1get_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1app_1info_1get_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1get_1name_FUNC);
/*
	rc = (jintLong)g_app_info_get_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_get_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1launch
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1app_1info_1launch)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1launch_FUNC);
/*
	rc = (jboolean)g_app_info_launch(arg0, arg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_launch)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1launch_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1launch_1default_1for_1uri
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1app_1info_1launch_1default_1for_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1launch_1default_1for_1uri_FUNC);
/*
	rc = (jboolean)g_app_info_launch_default_for_uri(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_launch_default_for_uri)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1launch_1default_1for_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1should_1show
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1app_1info_1should_1show)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1should_1show_FUNC);
/*
	rc = (jboolean)g_app_info_should_show(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_should_show)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1should_1show_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1app_1info_1supports_1uris
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1app_1info_1supports_1uris)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1app_1info_1supports_1uris_FUNC);
/*
	rc = (jboolean)g_app_info_supports_uris(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_app_info_supports_uris)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1app_1info_1supports_1uris_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1cclosure_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1cclosure_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1cclosure_1new_FUNC);
	rc = (jintLong)g_cclosure_new((GCallback)arg0, (gpointer)arg1, (GClosureNotify)arg2);
	OS_NATIVE_EXIT(env, that, _1g_1cclosure_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1closure_1ref
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1closure_1ref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1closure_1ref_FUNC);
	rc = (jintLong)g_closure_ref((GClosure *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1closure_1ref_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1closure_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1g_1closure_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1closure_1unref_FUNC);
	g_closure_unref((GClosure *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1closure_1unref_FUNC);
}
#endif

#ifndef NO__1g_1content_1type_1equals
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1content_1type_1equals)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1content_1type_1equals_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)g_content_type_equals(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_content_type_equals)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1content_1type_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1content_1type_1get_1mime_1type
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1content_1type_1get_1mime_1type)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1content_1type_1get_1mime_1type_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_content_type_get_mime_type(lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_content_type_get_mime_type)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *))fp)(lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1content_1type_1get_1mime_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1content_1type_1is_1a
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1content_1type_1is_1a)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1content_1type_1is_1a_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)g_content_type_is_a(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_content_type_is_a)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1content_1type_1is_1a_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1content_1types_1get_1registered
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1content_1types_1get_1registered)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1content_1types_1get_1registered_FUNC);
/*
	rc = (jintLong)g_content_types_get_registered();
*/
	{
		OS_LOAD_FUNCTION(fp, g_content_types_get_registered)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1content_1types_1get_1registered_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1data_1input_1stream_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1data_1input_1stream_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1data_1input_1stream_1new_FUNC);
/*
	rc = (jintLong)g_data_input_stream_new(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_data_input_stream_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1data_1input_1stream_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1data_1input_1stream_1read_1line
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1data_1input_1stream_1read_1line)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintLong arg2, jintLong arg3)
{
	jint *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1data_1input_1stream_1read_1line_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_data_input_stream_read_line(arg0, lparg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, g_data_input_stream_read_line)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jint *, jintLong, jintLong))fp)(arg0, lparg1, arg2, arg3);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1data_1input_1stream_1read_1line_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1desktop_1app_1info_1new_1from_1filename
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1desktop_1app_1info_1new_1from_1filename)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1desktop_1app_1info_1new_1from_1filename_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_desktop_app_info_new_from_filename(lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_desktop_app_info_new_from_filename)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *))fp)(lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1desktop_1app_1info_1new_1from_1filename_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1get_1path
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1get_1path)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1get_1path_FUNC);
/*
	rc = (jintLong)g_file_get_path(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_get_path)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1file_1get_1path_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1get_1uri
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1get_1uri_FUNC);
/*
	rc = (jintLong)g_file_get_uri(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_get_uri)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1file_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1icon_1get_1file
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1icon_1get_1file)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1icon_1get_1file_FUNC);
/*
	rc = (jintLong)g_file_icon_get_file(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_icon_get_file)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1file_1icon_1get_1file_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1info_1get_1content_1type
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1info_1get_1content_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1info_1get_1content_1type_FUNC);
/*
	rc = (jintLong)g_file_info_get_content_type(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_info_get_content_type)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1file_1info_1get_1content_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1info_1get_1modification_1time
JNIEXPORT void JNICALL OS_NATIVE(_1g_1file_1info_1get_1modification_1time)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1g_1file_1info_1get_1modification_1time_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	g_file_info_get_modification_time(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_info_get_modification_time)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1file_1info_1get_1modification_1time_FUNC);
}
#endif

#ifndef NO__1g_1file_1new_1for_1path
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1new_1for_1path)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1new_1for_1path_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_file_new_for_path(lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_new_for_path)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *))fp)(lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1file_1new_1for_1path_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1new_1for_1uri
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1new_1for_1uri)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1new_1for_1uri_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_file_new_for_uri(lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_new_for_uri)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *))fp)(lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1file_1new_1for_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1query_1info
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1query_1info)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3, jintLong arg4)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1query_1info_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_file_query_info(arg0, lparg1, arg2, arg3, arg4);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_query_info)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jbyte *, jintLong, jintLong, jintLong))fp)(arg0, lparg1, arg2, arg3, arg4);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1file_1query_1info_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1read
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1file_1read)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1read_FUNC);
/*
	rc = (jintLong)g_file_read(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_read)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1file_1read_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1file_1test
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1file_1test)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1file_1test_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)g_file_test(lparg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_file_test)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jbyte *, jint))fp)(lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1file_1test_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1filename_1from_1uri
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1filename_1from_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1filename_1from_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)g_filename_from_uri((const char *)arg0, (char **)lparg1, (GError **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1filename_1from_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1filename_1from_1utf8
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1filename_1from_1utf8)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1filename_1from_1utf8_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)g_filename_from_utf8((const gchar *)arg0, (gssize)arg1, (gsize *)lparg2, (gsize *)lparg3, (GError **)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1filename_1from_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1filename_1to_1uri
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1filename_1to_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1filename_1to_1uri_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)g_filename_to_uri((const char *)arg0, (const char *)arg1, (GError **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1filename_1to_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1filename_1to_1utf8
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1filename_1to_1utf8)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1filename_1to_1utf8_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)g_filename_to_utf8((const gchar *)arg0, (gssize)arg1, (gsize *)lparg2, (gsize *)lparg3, (GError **)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1filename_1to_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1free
JNIEXPORT void JNICALL OS_NATIVE(_1g_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1free_FUNC);
	g_free((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1free_FUNC);
}
#endif

#ifndef NO__1g_1icon_1new_1for_1string
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1icon_1new_1for_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLongArray arg1)
{
	jbyte *lparg0=NULL;
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1icon_1new_1for_1string_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)g_icon_new_for_string(lparg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, g_icon_new_for_string)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *, jintLong *))fp)(lparg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1icon_1new_1for_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1icon_1to_1string
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1icon_1to_1string)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1icon_1to_1string_FUNC);
/*
	rc = (jintLong)g_icon_to_string(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, g_icon_to_string)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1g_1icon_1to_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1idle_1add
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1idle_1add)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1idle_1add_FUNC);
	rc = (jint)g_idle_add((GSourceFunc)arg0, (gpointer)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1idle_1add_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1append
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1append_FUNC);
	rc = (jintLong)g_list_append((GList *)arg0, (gpointer)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1append_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1data
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1data)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1data_FUNC);
	rc = (jintLong)g_list_data((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1free
JNIEXPORT void JNICALL OS_NATIVE(_1g_1list_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1list_1free_FUNC);
	g_list_free((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1free_FUNC);
}
#endif

#ifndef NO__1g_1list_1free_11
JNIEXPORT void JNICALL OS_NATIVE(_1g_1list_1free_11)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1list_1free_11_FUNC);
	g_list_free_1((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1free_11_FUNC);
}
#endif

#ifndef NO__1g_1list_1last
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1last)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1last_FUNC);
	rc = (jintLong)g_list_last((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1last_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1length
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1list_1length)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1length_FUNC);
	rc = (jint)g_list_length((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1length_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1next
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1next)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1next_FUNC);
	rc = (jintLong)g_list_next(arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1next_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1nth
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1nth)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1nth_FUNC);
	rc = (jintLong)g_list_nth((GList *)arg0, (guint)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1nth_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1nth_1data
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1nth_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1nth_1data_FUNC);
	rc = (jintLong)g_list_nth_data((GList *)arg0, (guint)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1nth_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1prepend
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1prepend)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1prepend_FUNC);
	rc = (jintLong)g_list_prepend((GList *)arg0, (gpointer)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1prepend_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1previous
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1previous)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1previous_FUNC);
	rc = (jintLong)g_list_previous(arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1previous_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1remove_1link
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1remove_1link)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1remove_1link_FUNC);
	rc = (jintLong)g_list_remove_link((GList *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1remove_1link_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1reverse
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1list_1reverse)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1list_1reverse_FUNC);
	rc = (jintLong)g_list_reverse((GList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1list_1reverse_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1set_1next
JNIEXPORT void JNICALL OS_NATIVE(_1g_1list_1set_1next)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1g_1list_1set_1next_FUNC);
	g_list_set_next((GList *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1set_1next_FUNC);
}
#endif

#ifndef NO__1g_1list_1set_1previous
JNIEXPORT void JNICALL OS_NATIVE(_1g_1list_1set_1previous)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1g_1list_1set_1previous_FUNC);
	g_list_set_previous((GList *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1list_1set_1previous_FUNC);
}
#endif

#ifndef NO__1g_1locale_1from_1utf8
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1locale_1from_1utf8)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1locale_1from_1utf8_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)g_locale_from_utf8((const gchar *)arg0, (gssize)arg1, (gsize *)lparg2, (gsize *)lparg3, (GError **)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1locale_1from_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1locale_1to_1utf8
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1locale_1to_1utf8)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1locale_1to_1utf8_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)g_locale_to_utf8((const gchar *)arg0, (gssize)arg1, (gsize *)lparg2, (gsize *)lparg3, (GError **)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1locale_1to_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1log_1default_1handler
JNIEXPORT void JNICALL OS_NATIVE(_1g_1log_1default_1handler)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	OS_NATIVE_ENTER(env, that, _1g_1log_1default_1handler_FUNC);
	g_log_default_handler((gchar *)arg0, (GLogLevelFlags)arg1, (gchar *)arg2, (gpointer)arg3);
	OS_NATIVE_EXIT(env, that, _1g_1log_1default_1handler_FUNC);
}
#endif

#ifndef NO__1g_1log_1remove_1handler
JNIEXPORT void JNICALL OS_NATIVE(_1g_1log_1remove_1handler)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1g_1log_1remove_1handler_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	g_log_remove_handler((gchar *)lparg0, (gint)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1log_1remove_1handler_FUNC);
}
#endif

#ifndef NO__1g_1log_1set_1handler
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1log_1set_1handler)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1log_1set_1handler_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)g_log_set_handler((gchar *)lparg0, (GLogLevelFlags)arg1, (GLogFunc)arg2, (gpointer)arg3);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1log_1set_1handler_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1acquire
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1main_1context_1acquire)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1acquire_FUNC);
	rc = (jboolean)g_main_context_acquire((GMainContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1acquire_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1check
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1main_1context_1check)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1check_FUNC);
	rc = (jint)g_main_context_check((GMainContext *)arg0, arg1, (GPollFD *)arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1check_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1main_1context_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1default_FUNC);
	rc = (jintLong)g_main_context_default();
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1get_1poll_1func
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1main_1context_1get_1poll_1func)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1get_1poll_1func_FUNC);
	rc = (jintLong)g_main_context_get_poll_func((GMainContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1get_1poll_1func_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1iteration
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1main_1context_1iteration)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1iteration_FUNC);
	rc = (jboolean)g_main_context_iteration((GMainContext *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1iteration_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1pending
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1main_1context_1pending)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1pending_FUNC);
	rc = (jboolean)g_main_context_pending((GMainContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1pending_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1prepare
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1main_1context_1prepare)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1prepare_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)g_main_context_prepare((GMainContext *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1prepare_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1query
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1main_1context_1query)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jintLong arg3, jint arg4)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1query_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)g_main_context_query((GMainContext *)arg0, arg1, lparg2, (GPollFD *)arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1query_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1main_1context_1release
JNIEXPORT void JNICALL OS_NATIVE(_1g_1main_1context_1release)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1main_1context_1release_FUNC);
	g_main_context_release((GMainContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1main_1context_1release_FUNC);
}
#endif

#ifndef NO__1g_1malloc
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1malloc)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1malloc_FUNC);
	rc = (jintLong)g_malloc((gulong)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1malloc_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1g_1object_1get__I_3B_3II) && !defined(JNI64)) || (!defined(NO__1g_1object_1get__J_3B_3IJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1get__I_3B_3II)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintArray arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1get__J_3B_3IJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1get__I_3B_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1get__J_3B_3IJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	g_object_get((GObject *)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1get__I_3B_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1get__J_3B_3IJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1get__I_3B_3JI) && !defined(JNI64)) || (!defined(NO__1g_1object_1get__J_3B_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1get__I_3B_3JI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlongArray arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1get__J_3B_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlongArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	jlong *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1get__I_3B_3JI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1get__J_3B_3JJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	g_object_get((GObject *)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) (*env)->ReleaseLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1get__I_3B_3JI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1get__J_3B_3JJ_FUNC);
#endif
}
#endif

#ifndef NO__1g_1object_1get_1qdata
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1object_1get_1qdata)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1object_1get_1qdata_FUNC);
	rc = (jintLong)g_object_get_qdata((GObject *)arg0, (GQuark)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1object_1get_1qdata_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1object_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1object_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1object_1new_FUNC);
	rc = (jintLong)g_object_new((GType)arg0, (const gchar *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1object_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1object_1notify
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1notify)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1g_1object_1notify_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_object_notify((GObject *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1object_1notify_FUNC);
}
#endif

#ifndef NO__1g_1object_1ref
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1object_1ref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1object_1ref_FUNC);
	rc = (jintLong)g_object_ref((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1object_1ref_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1g_1object_1set__I_3BFI) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3BFJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3BFI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jfloat arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3BFJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jfloat arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3BFI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3BFJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, arg2, (const gchar *)NULL);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3BFI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3BFJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1set__I_3BII) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3BIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3BII)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3BIJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3BII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3BIJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, arg2, (const gchar *)NULL);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3BII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3BIJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1set__I_3BJI) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3BJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3BJI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlong arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3BJJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlong arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3BJI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3BJJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, arg2, (const gchar *)NULL);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3BJI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3BJJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1set__I_3BLorg_eclipse_swt_internal_gtk_GdkColor_2I) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3BLorg_eclipse_swt_internal_gtk_GdkColor_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3BLorg_eclipse_swt_internal_gtk_GdkColor_2I)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3BLorg_eclipse_swt_internal_gtk_GdkColor_2J)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	GdkColor _arg2, *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3BLorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3BLorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3BLorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3BLorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1set__I_3BZI) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3BZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3BZI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jboolean arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3BZJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jboolean arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3BZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3BZJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, arg2, (const gchar *)NULL);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3BZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3BZJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1object_1set__I_3B_3BI) && !defined(JNI64)) || (!defined(NO__1g_1object_1set__J_3B_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__I_3B_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set__J_3B_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__I_3B_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1object_1set__J_3B_3BJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	g_object_set((gpointer)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__I_3B_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1object_1set__J_3B_3BJ_FUNC);
#endif
}
#endif

#ifndef NO__1g_1object_1set_1qdata
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1set_1qdata)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1g_1object_1set_1qdata_FUNC);
	g_object_set_qdata((GObject *)arg0, (GQuark)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1g_1object_1set_1qdata_FUNC);
}
#endif

#ifndef NO__1g_1object_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1g_1object_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1object_1unref_FUNC);
	g_object_unref((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1object_1unref_FUNC);
}
#endif

#ifndef NO__1g_1quark_1from_1string
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1quark_1from_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1quark_1from_1string_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)g_quark_from_string((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1quark_1from_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1set_1prgname
JNIEXPORT void JNICALL OS_NATIVE(_1g_1set_1prgname)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1g_1set_1prgname_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	g_set_prgname((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1set_1prgname_FUNC);
}
#endif

#ifndef NO__1g_1signal_1add_1emission_1hook
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1signal_1add_1emission_1hook)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintLong arg2, jintLong arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1add_1emission_1hook_FUNC);
	rc = (jintLong)g_signal_add_emission_hook((guint)arg0, (GQuark)arg1, (GSignalEmissionHook)arg2, (gpointer)arg3, (GDestroyNotify)arg4);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1add_1emission_1hook_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1connect
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1connect)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1connect_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)g_signal_connect((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1connect_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1connect_1after
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1connect_1after)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1connect_1after_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)g_signal_connect_after((gpointer)arg0, (const gchar *)lparg1, (GCallback)arg2, (gpointer)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1connect_1after_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1connect_1closure
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1connect_1closure)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jboolean arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1connect_1closure_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)g_signal_connect_closure((gpointer)arg0, (const gchar *)lparg1, (GClosure *)arg2, (gboolean)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1connect_1closure_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1connect_1closure_1by_1id
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1connect_1closure_1by_1id)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLong arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1connect_1closure_1by_1id_FUNC);
	rc = (jint)g_signal_connect_closure_by_id((gpointer)arg0, (guint)arg1, (GQuark)arg2, (GClosure *)arg3, (gboolean)arg4);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1connect_1closure_1by_1id_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1g_1signal_1emit_1by_1name__I_3B) && !defined(JNI64)) || (!defined(NO__1g_1signal_1emit_1by_1name__J_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__I_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__J_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__I_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__J_3B_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_signal_emit_by_name((gpointer)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__I_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__J_3B_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1signal_1emit_1by_1name__I_3BI) && !defined(JNI64)) || (!defined(NO__1g_1signal_1emit_1by_1name__J_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__I_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__J_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__I_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__J_3BJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_signal_emit_by_name((gpointer)arg0, (const gchar *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__I_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__J_3BJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1signal_1emit_1by_1name__I_3BII) && !defined(JNI64)) || (!defined(NO__1g_1signal_1emit_1by_1name__J_3BJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__I_3BII)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__J_3BJJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__I_3BII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__J_3BJJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_signal_emit_by_name((gpointer)arg0, (const gchar *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__I_3BII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__J_3BJJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1signal_1emit_1by_1name__I_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2) && !defined(JNI64)) || (!defined(NO__1g_1signal_1emit_1by_1name__J_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__I_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__J_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2)
#endif
{
	jbyte *lparg1=NULL;
	GdkRectangle _arg2, *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__I_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__J_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getGdkRectangleFields(env, arg2, &_arg2)) == NULL) goto fail;
	g_signal_emit_by_name((gpointer)arg0, (const gchar *)lparg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkRectangleFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__I_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__J_3BLorg_eclipse_swt_internal_gtk_GdkRectangle_2_FUNC);
#endif
}
#endif

#if (!defined(NO__1g_1signal_1emit_1by_1name__I_3B_3B) && !defined(JNI64)) || (!defined(NO__1g_1signal_1emit_1by_1name__J_3B_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__I_3B_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1emit_1by_1name__J_3B_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
#endif
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__I_3B_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1signal_1emit_1by_1name__J_3B_3B_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	g_signal_emit_by_name((gpointer)arg0, (const gchar *)lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__I_3B_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1signal_1emit_1by_1name__J_3B_3B_FUNC);
#endif
}
#endif

#ifndef NO__1g_1signal_1handler_1disconnect
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1handler_1disconnect)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1g_1signal_1handler_1disconnect_FUNC);
	g_signal_handler_disconnect((gpointer)arg0, (gulong)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1handler_1disconnect_FUNC);
}
#endif

#ifndef NO__1g_1signal_1handler_1find
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1handler_1find)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintLong arg4, jintLong arg5, jintLong arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1handler_1find_FUNC);
	rc = (jint)g_signal_handler_find((gpointer)arg0, arg1, arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1handler_1find_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1handlers_1block_1matched
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1handlers_1block_1matched)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintLong arg4, jintLong arg5, jintLong arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1handlers_1block_1matched_FUNC);
	rc = (jint)g_signal_handlers_block_matched((gpointer)arg0, (GSignalMatchType)arg1, (guint)arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1handlers_1block_1matched_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1handlers_1disconnect_1matched
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1handlers_1disconnect_1matched)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintLong arg4, jintLong arg5, jintLong arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1handlers_1disconnect_1matched_FUNC);
	rc = (jint)g_signal_handlers_disconnect_matched((gpointer)arg0, (GSignalMatchType)arg1, (guint)arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1handlers_1disconnect_1matched_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1handlers_1unblock_1matched
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1handlers_1unblock_1matched)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintLong arg4, jintLong arg5, jintLong arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1handlers_1unblock_1matched_FUNC);
	rc = (jint)g_signal_handlers_unblock_matched((gpointer)arg0, (GSignalMatchType)arg1, (guint)arg2, (GQuark)arg3, (GClosure *)arg4, (gpointer)arg5, (gpointer)arg6);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1handlers_1unblock_1matched_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1lookup
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1signal_1lookup)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1lookup_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)g_signal_lookup((const gchar *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1lookup_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1signal_1remove_1emission_1hook
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1remove_1emission_1hook)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1g_1signal_1remove_1emission_1hook_FUNC);
	g_signal_remove_emission_hook((guint)arg0, (gulong)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1remove_1emission_1hook_FUNC);
}
#endif

#ifndef NO__1g_1signal_1stop_1emission_1by_1name
JNIEXPORT void JNICALL OS_NATIVE(_1g_1signal_1stop_1emission_1by_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1g_1signal_1stop_1emission_1by_1name_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	g_signal_stop_emission_by_name((gpointer)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1g_1signal_1stop_1emission_1by_1name_FUNC);
}
#endif

#ifndef NO__1g_1slist_1append
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1slist_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1slist_1append_FUNC);
	rc = (jintLong)g_slist_append((GSList *)arg0, (gpointer)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1slist_1append_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1slist_1data
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1slist_1data)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1slist_1data_FUNC);
	rc = (jintLong)g_slist_data((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1slist_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1slist_1free
JNIEXPORT void JNICALL OS_NATIVE(_1g_1slist_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1slist_1free_FUNC);
	g_slist_free((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1slist_1free_FUNC);
}
#endif

#ifndef NO__1g_1slist_1length
JNIEXPORT jint JNICALL OS_NATIVE(_1g_1slist_1length)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1slist_1length_FUNC);
	rc = (jint)g_slist_length((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1slist_1length_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1slist_1next
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1slist_1next)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1slist_1next_FUNC);
	rc = (jintLong)g_slist_next((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1slist_1next_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1source_1remove
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1source_1remove)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1source_1remove_FUNC);
	rc = (jboolean)g_source_remove((guint)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1source_1remove_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1strfreev
JNIEXPORT void JNICALL OS_NATIVE(_1g_1strfreev)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1strfreev_FUNC);
	g_strfreev((gchar **)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1strfreev_FUNC);
}
#endif

#ifndef NO__1g_1strtod
JNIEXPORT jdouble JNICALL OS_NATIVE(_1g_1strtod)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1strtod_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jdouble)g_strtod((const gchar *)arg0, (gchar **)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1strtod_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1thread_1init
JNIEXPORT void JNICALL OS_NATIVE(_1g_1thread_1init)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1thread_1init_FUNC);
	g_thread_init((GThreadFunctions *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1thread_1init_FUNC);
}
#endif

#ifndef NO__1g_1thread_1supported
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1thread_1supported)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1thread_1supported_FUNC);
	rc = (jboolean)g_thread_supported();
	OS_NATIVE_EXIT(env, that, _1g_1thread_1supported_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1add_1interface_1static
JNIEXPORT void JNICALL OS_NATIVE(_1g_1type_1add_1interface_1static)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1g_1type_1add_1interface_1static_FUNC);
	g_type_add_interface_static((GType)arg0, (GType)arg1, (const GInterfaceInfo *)arg2);
	OS_NATIVE_EXIT(env, that, _1g_1type_1add_1interface_1static_FUNC);
}
#endif

#ifndef NO__1g_1type_1class_1peek
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1class_1peek)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1class_1peek_FUNC);
	rc = (jintLong)g_type_class_peek((GType)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1class_1peek_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1class_1peek_1parent
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1class_1peek_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1class_1peek_1parent_FUNC);
	rc = (jintLong)g_type_class_peek_parent((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1class_1peek_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1class_1ref
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1class_1ref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1class_1ref_FUNC);
	rc = (jintLong)g_type_class_ref((GType)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1class_1ref_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1class_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1g_1type_1class_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1g_1type_1class_1unref_FUNC);
	g_type_class_unref((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1class_1unref_FUNC);
}
#endif

#ifndef NO__1g_1type_1from_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1from_1name)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1from_1name_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)g_type_from_name((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1from_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1interface_1peek_1parent
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1interface_1peek_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1interface_1peek_1parent_FUNC);
	rc = (jintLong)g_type_interface_peek_parent((gpointer)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1interface_1peek_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1is_1a
JNIEXPORT jboolean JNICALL OS_NATIVE(_1g_1type_1is_1a)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1is_1a_FUNC);
	rc = (jboolean)g_type_is_a((GType)arg0, (GType)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1type_1is_1a_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1name_FUNC);
	rc = (jintLong)g_type_name((GType)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1parent
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1parent_FUNC);
	rc = (jintLong)g_type_parent((GType)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1type_1query
JNIEXPORT void JNICALL OS_NATIVE(_1g_1type_1query)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1g_1type_1query_FUNC);
	g_type_query((GType)arg0, (GTypeQuery *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1type_1query_FUNC);
}
#endif

#ifndef NO__1g_1type_1register_1static
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1type_1register_1static)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1type_1register_1static_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)g_type_register_static((GType)arg0, (const gchar *)lparg1, (const GTypeInfo *)arg2, (GTypeFlags)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1g_1type_1register_1static_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf16_1offset_1to_1pointer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf16_1offset_1to_1pointer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf16_1offset_1to_1pointer_FUNC);
	rc = (jintLong)g_utf16_offset_to_pointer((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf16_1offset_1to_1pointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf16_1offset_1to_1utf8_1offset
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf16_1offset_1to_1utf8_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf16_1offset_1to_1utf8_1offset_FUNC);
	rc = (jintLong)g_utf16_offset_to_utf8_offset((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf16_1offset_1to_1utf8_1offset_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf16_1pointer_1to_1offset
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf16_1pointer_1to_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf16_1pointer_1to_1offset_FUNC);
	rc = (jintLong)g_utf16_pointer_to_offset((const gchar *)arg0, (const gchar *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf16_1pointer_1to_1offset_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf16_1strlen
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf16_1strlen)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf16_1strlen_FUNC);
	rc = (jintLong)g_utf16_strlen((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf16_1strlen_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf16_1to_1utf8
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf16_1to_1utf8)
	(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
{
	jchar *lparg0=NULL;
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf16_1to_1utf8_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)g_utf16_to_utf8((const gunichar2 *)lparg0, (glong)arg1, (glong *)lparg2, (glong *)lparg3, (GError **)lparg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
		if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1g_1utf16_1to_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf8_1offset_1to_1pointer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1offset_1to_1pointer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1offset_1to_1pointer_FUNC);
	rc = (jintLong)g_utf8_offset_to_pointer((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1offset_1to_1pointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf8_1offset_1to_1utf16_1offset
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1offset_1to_1utf16_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1offset_1to_1utf16_1offset_FUNC);
	rc = (jintLong)g_utf8_offset_to_utf16_offset((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1offset_1to_1utf16_1offset_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf8_1pointer_1to_1offset
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1pointer_1to_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1pointer_1to_1offset_FUNC);
	rc = (jintLong)g_utf8_pointer_to_offset((const gchar *)arg0, (const gchar *)arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1pointer_1to_1offset_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1utf8_1strlen
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1strlen)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1strlen_FUNC);
	rc = (jintLong)g_utf8_strlen((const gchar *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1strlen_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1g_1utf8_1to_1utf16__II_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1g_1utf8_1to_1utf16__JJ_3J_3J_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1to_1utf16__II_3I_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1to_1utf16__JJ_3J_3J_3J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
#endif
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1to_1utf16__II_3I_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1to_1utf16__JJ_3J_3J_3J_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)g_utf8_to_utf16((const gchar *)arg0, (glong)arg1, (glong *)lparg2, (glong *)lparg3, (GError **)lparg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1to_1utf16__II_3I_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1to_1utf16__JJ_3J_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1g_1utf8_1to_1utf16___3BI_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1g_1utf8_1to_1utf16___3BJ_3J_3J_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1to_1utf16___3BI_3I_3I_3I)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1utf8_1to_1utf16___3BJ_3J_3J_3J)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3, jintLongArray arg4)
#endif
{
	jbyte *lparg0=NULL;
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1to_1utf16___3BI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1g_1utf8_1to_1utf16___3BJ_3J_3J_3J_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)g_utf8_to_utf16((const gchar *)lparg0, (glong)arg1, (glong *)lparg2, (glong *)lparg3, (GError **)lparg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
		if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
		if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1to_1utf16___3BI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1g_1utf8_1to_1utf16___3BJ_3J_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1g_1value_1peek_1pointer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1g_1value_1peek_1pointer)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1g_1value_1peek_1pointer_FUNC);
	rc = (jintLong)g_value_peek_pointer((const GValue *)arg0);
	OS_NATIVE_EXIT(env, that, _1g_1value_1peek_1pointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1atom_1intern
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1atom_1intern)
	(JNIEnv *env, jclass that, jbyteArray arg0, jboolean arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1atom_1intern_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)gdk_atom_intern((const gchar *)lparg0, arg1);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1atom_1intern_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1atom_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1atom_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1atom_1name_FUNC);
	rc = (jintLong)gdk_atom_name((GdkAtom)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1atom_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1beep
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1beep)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1beep_FUNC);
	gdk_beep();
	OS_NATIVE_EXIT(env, that, _1gdk_1beep_FUNC);
}
#endif

#ifndef NO__1gdk_1bitmap_1create_1from_1data
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1bitmap_1create_1from_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1bitmap_1create_1from_1data_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)gdk_bitmap_create_from_data((GdkWindow *)arg0, (const gchar *)lparg1, (gint)arg2, (gint)arg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1bitmap_1create_1from_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1cairo_1create
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1cairo_1create)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1cairo_1create_FUNC);
/*
	rc = (jintLong)gdk_cairo_create(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_cairo_create)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1cairo_1create_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1cairo_1region
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1cairo_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1cairo_1region_FUNC);
/*
	gdk_cairo_region(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_cairo_region)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1cairo_1region_FUNC);
}
#endif

#ifndef NO__1gdk_1cairo_1reset_1clip
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1cairo_1reset_1clip)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1cairo_1reset_1clip_FUNC);
/*
	gdk_cairo_reset_clip(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_cairo_reset_clip)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1cairo_1reset_1clip_FUNC);
}
#endif

#ifndef NO__1gdk_1cairo_1set_1source_1color
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1cairo_1set_1source_1color)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1cairo_1set_1source_1color_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	gdk_cairo_set_source_color(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_cairo_set_source_color)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, GdkColor *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setGdkColorFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1cairo_1set_1source_1color_FUNC);
}
#endif

#ifndef NO__1gdk_1color_1white
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1color_1white)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1color_1white_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	rc = (jboolean)gdk_color_white((GdkColormap *)arg0, (GdkColor *)lparg1);
fail:
	if (arg1 && lparg1) setGdkColorFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1color_1white_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1colormap_1alloc_1color
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1colormap_1alloc_1color)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jboolean arg2, jboolean arg3)
{
	GdkColor _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1colormap_1alloc_1color_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)gdk_colormap_alloc_color((GdkColormap *)arg0, (GdkColor *)lparg1, (gboolean)arg2, (gboolean)arg3);
fail:
	if (arg1 && lparg1) setGdkColorFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1colormap_1alloc_1color_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1colormap_1free_1colors
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1colormap_1free_1colors)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1colormap_1free_1colors_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_colormap_free_colors((GdkColormap *)arg0, (GdkColor *)lparg1, (gint)arg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1colormap_1free_1colors_FUNC);
}
#endif

#ifndef NO__1gdk_1colormap_1get_1system
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1colormap_1get_1system)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1colormap_1get_1system_FUNC);
	rc = (jintLong)gdk_colormap_get_system();
	OS_NATIVE_EXIT(env, that, _1gdk_1colormap_1get_1system_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1colormap_1query_1color
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1colormap_1query_1color)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1colormap_1query_1color_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gdk_colormap_query_color((GdkColormap *)arg0, (gulong)arg1, (GdkColor *)lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1colormap_1query_1color_FUNC);
}
#endif

#ifndef NO__1gdk_1cursor_1destroy
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1cursor_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1cursor_1destroy_FUNC);
	gdk_cursor_destroy((GdkCursor *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1cursor_1destroy_FUNC);
}
#endif

#ifndef NO__1gdk_1cursor_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1cursor_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1cursor_1new_FUNC);
	rc = (jintLong)gdk_cursor_new((GdkCursorType)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1cursor_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1cursor_1new_1from_1pixbuf
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1cursor_1new_1from_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1cursor_1new_1from_1pixbuf_FUNC);
/*
	rc = (jintLong)gdk_cursor_new_from_pixbuf(arg0, arg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_cursor_new_from_pixbuf)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1cursor_1new_1from_1pixbuf_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1cursor_1new_1from_1pixmap
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1cursor_1new_1from_1pixmap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	GdkColor _arg2, *lparg2=NULL;
	GdkColor _arg3, *lparg3=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1cursor_1new_1from_1pixmap_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGdkColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)gdk_cursor_new_from_pixmap((GdkPixmap *)arg0, (GdkPixmap *)arg1, (GdkColor *)lparg2, (GdkColor *)lparg3, (gint)arg4, (gint)arg5);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1cursor_1new_1from_1pixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1display_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1display_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1display_1get_1default_FUNC);
/*
	rc = (jintLong)gdk_display_get_default();
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_display_get_default)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1display_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1display_1supports_1cursor_1color
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1display_1supports_1cursor_1color)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1display_1supports_1cursor_1color_FUNC);
/*
	rc = (jboolean)gdk_display_supports_cursor_color(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_display_supports_cursor_color)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1display_1supports_1cursor_1color_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1drag_1status
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1drag_1status)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1drag_1status_FUNC);
	gdk_drag_status((GdkDragContext *)arg0, (GdkDragAction)arg1, (guint32)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1drag_1status_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1arc
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1arc)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1arc_FUNC);
	gdk_draw_arc((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1arc_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1drawable
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1drawable)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1drawable_FUNC);
	gdk_draw_drawable((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkDrawable *)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1drawable_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1image
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1image)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1image_FUNC);
	gdk_draw_image((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkImage *)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1image_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1layout
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1layout)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1layout_FUNC);
	gdk_draw_layout((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (PangoLayout *)arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1layout_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1layout_1with_1colors
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1layout_1with_1colors)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4, jobject arg5, jobject arg6)
{
	GdkColor _arg5, *lparg5=NULL;
	GdkColor _arg6, *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1layout_1with_1colors_FUNC);
	if (arg5) if ((lparg5 = getGdkColorFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getGdkColorFields(env, arg6, &_arg6)) == NULL) goto fail;
	gdk_draw_layout_with_colors((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (PangoLayout *)arg4, lparg5, lparg6);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1layout_1with_1colors_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1line
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1line)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1line_FUNC);
	gdk_draw_line((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1line_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1lines
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1lines)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1lines_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	}
	gdk_draw_lines((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkPoint *)lparg2, (gint)arg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	} else
#endif
	{
		if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1lines_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1pixbuf
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1pixbuf_FUNC);
/*
	gdk_draw_pixbuf((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkPixbuf *)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8, (GdkRgbDither)arg9, (gint)arg10, (gint)arg11);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_draw_pixbuf)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkDrawable *, GdkGC *, GdkPixbuf *, gint, gint, gint, gint, gint, gint, GdkRgbDither, gint, gint))fp)((GdkDrawable *)arg0, (GdkGC *)arg1, (GdkPixbuf *)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6, (gint)arg7, (gint)arg8, (GdkRgbDither)arg9, (gint)arg10, (gint)arg11);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1pixbuf_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1point
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1point)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1point_FUNC);
	gdk_draw_point((GdkDrawable *)arg0, (GdkGC *)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1point_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1polygon
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1polygon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1polygon_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	gdk_draw_polygon((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (GdkPoint *)lparg3, (gint)arg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, JNI_ABORT);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1polygon_FUNC);
}
#endif

#ifndef NO__1gdk_1draw_1rectangle
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1draw_1rectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1draw_1rectangle_FUNC);
	gdk_draw_rectangle((GdkDrawable *)arg0, (GdkGC *)arg1, (gint)arg2, (gint)arg3, (gint)arg4, (gint)arg5, (gint)arg6);
	OS_NATIVE_EXIT(env, that, _1gdk_1draw_1rectangle_FUNC);
}
#endif

#ifndef NO__1gdk_1drawable_1get_1depth
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1drawable_1get_1depth)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1drawable_1get_1depth_FUNC);
	rc = (jint)gdk_drawable_get_depth((GdkDrawable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1drawable_1get_1depth_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1drawable_1get_1image
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1drawable_1get_1image)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1drawable_1get_1image_FUNC);
	rc = (jintLong)gdk_drawable_get_image((GdkDrawable *)arg0, (gint)arg1, (gint)arg2, (gint)arg3, (gint)arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1drawable_1get_1image_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1drawable_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1drawable_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1drawable_1get_1size_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
		if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	}
	gdk_drawable_get_size((GdkDrawable *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
		if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1drawable_1get_1size_FUNC);
}
#endif

#ifndef NO__1gdk_1drawable_1get_1visible_1region
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1drawable_1get_1visible_1region)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1drawable_1get_1visible_1region_FUNC);
	rc = (jintLong)gdk_drawable_get_visible_region((GdkDrawable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1drawable_1get_1visible_1region_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1error_1trap_1pop
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1error_1trap_1pop)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1error_1trap_1pop_FUNC);
	rc = (jint)gdk_error_trap_pop();
	OS_NATIVE_EXIT(env, that, _1gdk_1error_1trap_1pop_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1error_1trap_1push
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1error_1trap_1push)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1error_1trap_1push_FUNC);
	gdk_error_trap_push();
	OS_NATIVE_EXIT(env, that, _1gdk_1error_1trap_1push_FUNC);
}
#endif

#ifndef NO__1gdk_1event_1copy
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1event_1copy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1copy_FUNC);
	rc = (jintLong)gdk_event_copy((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1copy_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1event_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1free_FUNC);
	gdk_event_free((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1free_FUNC);
}
#endif

#ifndef NO__1gdk_1event_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1event_1get)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_FUNC);
	rc = (jintLong)gdk_event_get();
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1get_1coords
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1event_1get_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_1coords_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_event_get_coords((GdkEvent *)arg0, (gdouble *)lparg1, (gdouble *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_1coords_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1get_1graphics_1expose
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1event_1get_1graphics_1expose)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_1graphics_1expose_FUNC);
	rc = (jintLong)gdk_event_get_graphics_expose((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_1graphics_1expose_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1get_1root_1coords
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1event_1get_1root_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jdoubleArray arg2)
{
	jdouble *lparg1=NULL;
	jdouble *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_1root_1coords_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_event_get_root_coords((GdkEvent *)arg0, (gdouble *)lparg1, (gdouble *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_1root_1coords_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1get_1state
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1event_1get_1state)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_1state_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_event_get_state((GdkEvent *)arg0, (GdkModifierType *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_1state_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1get_1time
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1event_1get_1time)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1get_1time_FUNC);
	rc = (jint)gdk_event_get_time((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1get_1time_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1handler_1set
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1event_1handler_1set)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1handler_1set_FUNC);
	gdk_event_handler_set((GdkEventFunc)arg0, (gpointer)arg1, (GDestroyNotify)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1handler_1set_FUNC);
}
#endif

#ifndef NO__1gdk_1event_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1event_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1new_FUNC);
	rc = (jintLong)gdk_event_new(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1peek
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1event_1peek)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1peek_FUNC);
	rc = (jintLong)gdk_event_peek();
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1peek_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1event_1put
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1event_1put)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1event_1put_FUNC);
	gdk_event_put((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1event_1put_FUNC);
}
#endif

#ifndef NO__1gdk_1flush
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1flush)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1flush_FUNC);
	gdk_flush();
	OS_NATIVE_EXIT(env, that, _1gdk_1flush_FUNC);
}
#endif

#ifndef NO__1gdk_1free_1text_1list
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1free_1text_1list)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1free_1text_1list_FUNC);
	gdk_free_text_list((gchar **)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1free_1text_1list_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1get_1values
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1get_1values)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkGCValues _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1get_1values_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gdk_gc_get_values((GdkGC *)arg0, (GdkGCValues *)lparg1);
fail:
	if (arg1 && lparg1) setGdkGCValuesFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1get_1values_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1gc_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1new_FUNC);
	rc = (jintLong)gdk_gc_new((GdkDrawable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1gc_1set_1background
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1background)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1background_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_gc_set_background((GdkGC *)arg0, (GdkColor *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1background_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1clip_1mask
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1clip_1mask)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1clip_1mask_FUNC);
	gdk_gc_set_clip_mask((GdkGC *)arg0, (GdkBitmap *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1clip_1mask_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1clip_1origin
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1clip_1origin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1clip_1origin_FUNC);
	gdk_gc_set_clip_origin((GdkGC *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1clip_1origin_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1clip_1rectangle
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1clip_1rectangle)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1clip_1rectangle_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_gc_set_clip_rectangle((GdkGC *)arg0, (GdkRectangle *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1clip_1rectangle_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1clip_1region
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1clip_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1clip_1region_FUNC);
	gdk_gc_set_clip_region((GdkGC *)arg0, (GdkRegion *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1clip_1region_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1dashes
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1dashes)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1dashes_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	}
	gdk_gc_set_dashes((GdkGC *)arg0, (gint)arg1, (gint8 *)lparg2, (gint)arg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	} else
#endif
	{
		if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1dashes_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1exposures
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1exposures)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1exposures_FUNC);
	gdk_gc_set_exposures((GdkGC *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1exposures_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1fill
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1fill)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1fill_FUNC);
	gdk_gc_set_fill((GdkGC *)arg0, (GdkFill)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1fill_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1foreground
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1foreground)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1foreground_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_gc_set_foreground((GdkGC *)arg0, (GdkColor *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1foreground_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1function
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1function)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1function_FUNC);
	gdk_gc_set_function((GdkGC *)arg0, (GdkFunction)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1function_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1line_1attributes
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1line_1attributes)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1line_1attributes_FUNC);
	gdk_gc_set_line_attributes((GdkGC *)arg0, (gint)arg1, (GdkLineStyle)arg2, (GdkCapStyle)arg3, (GdkJoinStyle)arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1line_1attributes_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1stipple
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1stipple)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1stipple_FUNC);
	gdk_gc_set_stipple((GdkGC *)arg0, (GdkPixmap *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1stipple_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1subwindow
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1subwindow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1subwindow_FUNC);
	gdk_gc_set_subwindow((GdkGC *)arg0, (GdkSubwindowMode)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1subwindow_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1tile
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1tile)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1tile_FUNC);
	gdk_gc_set_tile((GdkGC *)arg0, (GdkPixmap *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1tile_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1ts_1origin
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1ts_1origin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1ts_1origin_FUNC);
	gdk_gc_set_ts_origin((GdkGC *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1ts_1origin_FUNC);
}
#endif

#ifndef NO__1gdk_1gc_1set_1values
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1gc_1set_1values)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	GdkGCValues _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1gc_1set_1values_FUNC);
	if (arg1) if ((lparg1 = getGdkGCValuesFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_gc_set_values((GdkGC *)arg0, (GdkGCValues *)lparg1, (GdkGCValuesMask)arg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1gc_1set_1values_FUNC);
}
#endif

#ifndef NO__1gdk_1keyboard_1ungrab
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1keyboard_1ungrab)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1keyboard_1ungrab_FUNC);
	gdk_keyboard_ungrab(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1keyboard_1ungrab_FUNC);
}
#endif

#ifndef NO__1gdk_1keymap_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1keymap_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1keymap_1get_1default_FUNC);
	rc = (jintLong)gdk_keymap_get_default();
	OS_NATIVE_EXIT(env, that, _1gdk_1keymap_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1keymap_1translate_1keyboard_1state
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1keymap_1translate_1keyboard_1state)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1keymap_1translate_1keyboard_1state_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_keymap_translate_keyboard_state((GdkKeymap*)arg0, arg1, (GdkModifierType)arg2, arg3, (guint*)lparg4, (gint*)lparg5, (gint*)lparg6, (GdkModifierType *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1keymap_1translate_1keyboard_1state_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1keyval_1to_1lower
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1keyval_1to_1lower)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1keyval_1to_1lower_FUNC);
	rc = (jint)gdk_keyval_to_lower(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1keyval_1to_1lower_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1keyval_1to_1unicode
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1keyval_1to_1unicode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1keyval_1to_1unicode_FUNC);
	rc = (jint)gdk_keyval_to_unicode(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1keyval_1to_1unicode_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pango_1attr_1embossed_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pango_1attr_1embossed_1new)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pango_1attr_1embossed_1new_FUNC);
	rc = (jintLong)gdk_pango_attr_embossed_new(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pango_1attr_1embossed_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pango_1context_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pango_1context_1get)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pango_1context_1get_FUNC);
	rc = (jintLong)gdk_pango_context_get();
	OS_NATIVE_EXIT(env, that, _1gdk_1pango_1context_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pango_1context_1set_1colormap
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pango_1context_1set_1colormap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pango_1context_1set_1colormap_FUNC);
	gdk_pango_context_set_colormap((PangoContext *)arg0, (GdkColormap *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1pango_1context_1set_1colormap_FUNC);
}
#endif

#ifndef NO__1gdk_1pango_1layout_1get_1clip_1region
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pango_1layout_1get_1clip_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pango_1layout_1get_1clip_1region_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_pango_layout_get_clip_region((PangoLayout *)arg0, arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pango_1layout_1get_1clip_1region_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1copy_1area
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pixbuf_1copy_1area)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1copy_1area_FUNC);
	gdk_pixbuf_copy_area((GdkPixbuf *)arg0, arg1, arg2, arg3, arg4, (GdkPixbuf *)arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1copy_1area_FUNC);
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1from_1drawable
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1from_1drawable)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1from_1drawable_FUNC);
	rc = (jintLong)gdk_pixbuf_get_from_drawable((GdkPixbuf *)arg0, (GdkDrawable *)arg1, (GdkColormap *)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1from_1drawable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1has_1alpha
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1has_1alpha)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1has_1alpha_FUNC);
	rc = (jboolean)gdk_pixbuf_get_has_alpha((const GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1has_1alpha_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1height
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1height)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1height_FUNC);
	rc = (jint)gdk_pixbuf_get_height((const GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1pixels
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1pixels)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1pixels_FUNC);
	rc = (jintLong)gdk_pixbuf_get_pixels((const GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1pixels_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1rowstride
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1rowstride)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1rowstride_FUNC);
	rc = (jint)gdk_pixbuf_get_rowstride((const GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1rowstride_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1get_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1pixbuf_1get_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1get_1width_FUNC);
	rc = (jint)gdk_pixbuf_get_width((const GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1get_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1loader_1close
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1pixbuf_1loader_1close)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1loader_1close_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_pixbuf_loader_close((GdkPixbufLoader *)arg0, (GError **)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1loader_1close_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1loader_1get_1pixbuf
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1loader_1get_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1loader_1get_1pixbuf_FUNC);
	rc = (jintLong)gdk_pixbuf_loader_get_pixbuf((GdkPixbufLoader *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1loader_1get_1pixbuf_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1loader_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1loader_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1loader_1new_FUNC);
	rc = (jintLong)gdk_pixbuf_loader_new();
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1loader_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1loader_1write
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1pixbuf_1loader_1write)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3)
{
	jintLong *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1loader_1write_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_pixbuf_loader_write((GdkPixbufLoader *)arg0, (const guchar *)arg1, (gsize)arg2, (GError **)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1loader_1write_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1new)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1new_FUNC);
	rc = (jintLong)gdk_pixbuf_new((GdkColorspace)arg0, (gboolean)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1new_1from_1file
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1new_1from_1file)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLongArray arg1)
{
	jbyte *lparg0=NULL;
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1new_1from_1file_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_pixbuf_new_from_file((const char *)lparg0, (GError**)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1new_1from_1file_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1render_1pixmap_1and_1mask
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pixbuf_1render_1pixmap_1and_1mask)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2, jint arg3)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1render_1pixmap_1and_1mask_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gdk_pixbuf_render_pixmap_and_mask((GdkPixbuf *)arg0, (GdkDrawable **)lparg1, (GdkBitmap **)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1render_1pixmap_1and_1mask_FUNC);
}
#endif

#ifndef NO__1gdk_1pixbuf_1render_1to_1drawable
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pixbuf_1render_1to_1drawable)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1render_1to_1drawable_FUNC);
	gdk_pixbuf_render_to_drawable((GdkPixbuf *)arg0, (GdkDrawable *)arg1, (GdkGC *)arg2, arg3, arg4, arg5, arg6, arg7, arg8, (GdkRgbDither)arg9, arg10, arg11);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1render_1to_1drawable_FUNC);
}
#endif

#ifndef NO__1gdk_1pixbuf_1render_1to_1drawable_1alpha
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pixbuf_1render_1to_1drawable_1alpha)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1render_1to_1drawable_1alpha_FUNC);
	gdk_pixbuf_render_to_drawable_alpha((GdkPixbuf *)arg0, (GdkDrawable *)arg1, arg2, arg3, arg4, arg5, arg6, arg7, (GdkPixbufAlphaMode)arg8, arg9, (GdkRgbDither)arg10, arg11, arg12);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1render_1to_1drawable_1alpha_FUNC);
}
#endif

#ifndef NO__1gdk_1pixbuf_1save_1to_1bufferv
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1pixbuf_1save_1to_1bufferv)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2, jbyteArray arg3, jintLongArray arg4, jintLongArray arg5, jintLongArray arg6)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	jbyte *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong *lparg5=NULL;
	jintLong *lparg6=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1save_1to_1bufferv_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)gdk_pixbuf_save_to_bufferv((GdkPixbuf *)arg0, (gchar **)lparg1, (gsize *)lparg2, (const char *)lparg3, lparg4, lparg5, (GError **)lparg6);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_pixbuf_save_to_bufferv)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(GdkPixbuf *, gchar **, gsize *, const char *, jintLong *, jintLong *, GError **))fp)((GdkPixbuf *)arg0, (gchar **)lparg1, (gsize *)lparg2, (const char *)lparg3, lparg4, lparg5, (GError **)lparg6);
		}
	}
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntLongArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntLongArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1save_1to_1bufferv_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixbuf_1scale
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pixbuf_1scale)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jdouble arg6, jdouble arg7, jdouble arg8, jdouble arg9, jint arg10)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1scale_FUNC);
	gdk_pixbuf_scale((const GdkPixbuf *)arg0, (GdkPixbuf *)arg1, arg2, arg3, arg4, arg5, (double)arg6, (double)arg7, (double)arg8, (double)arg9, arg10);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1scale_FUNC);
}
#endif

#ifndef NO__1gdk_1pixbuf_1scale_1simple
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixbuf_1scale_1simple)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixbuf_1scale_1simple_FUNC);
	rc = (jintLong)gdk_pixbuf_scale_simple((const GdkPixbuf *)arg0, arg1, arg2, (GdkInterpType)arg3);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixbuf_1scale_1simple_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixmap_1foreign_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixmap_1foreign_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixmap_1foreign_1new_FUNC);
	rc = (jintLong)gdk_pixmap_foreign_new(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixmap_1foreign_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pixmap_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1pixmap_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pixmap_1new_FUNC);
	rc = (jintLong)gdk_pixmap_new((GdkWindow *)arg0, (gint)arg1, (gint)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1gdk_1pixmap_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pointer_1grab
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1pointer_1grab)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1, jint arg2, jintLong arg3, jintLong arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pointer_1grab_FUNC);
	rc = (jint)gdk_pointer_grab((GdkWindow *)arg0, (gboolean)arg1, (GdkEventMask)arg2, (GdkWindow *)arg3, (GdkCursor *)arg4, (guint32)arg5);
	OS_NATIVE_EXIT(env, that, _1gdk_1pointer_1grab_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pointer_1is_1grabbed
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1pointer_1is_1grabbed)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1pointer_1is_1grabbed_FUNC);
	rc = (jboolean)gdk_pointer_is_grabbed();
	OS_NATIVE_EXIT(env, that, _1gdk_1pointer_1is_1grabbed_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1pointer_1ungrab
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1pointer_1ungrab)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1pointer_1ungrab_FUNC);
	gdk_pointer_ungrab((guint32)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1pointer_1ungrab_FUNC);
}
#endif

#ifndef NO__1gdk_1property_1get
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1property_1get)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jintLongArray arg6, jintArray arg7, jintArray arg8, jintLongArray arg9)
{
	jintLong *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jintLong *lparg9=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1property_1get_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntLongArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_property_get((GdkWindow *)arg0, (GdkAtom)arg1, (GdkAtom)arg2, arg3, arg4, arg5, (GdkAtom *)lparg6, (gint *)lparg7, (gint *)lparg8, (guchar **)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntLongArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntLongArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1property_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1destroy
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1destroy_FUNC);
	gdk_region_destroy((GdkRegion *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1destroy_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1empty
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1region_1empty)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1empty_FUNC);
	rc = (jboolean)gdk_region_empty((GdkRegion *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1empty_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1get_1clipbox
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1get_1clipbox)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1get_1clipbox_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gdk_region_get_clipbox((GdkRegion *)arg0, (GdkRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setGdkRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1get_1clipbox_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1get_1rectangles
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1get_1rectangles)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2)
{
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1get_1rectangles_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gdk_region_get_rectangles((GdkRegion *)arg0, (GdkRectangle **)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1get_1rectangles_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1intersect
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1intersect)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1intersect_FUNC);
	gdk_region_intersect((GdkRegion *)arg0, (GdkRegion *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1intersect_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1region_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1new_FUNC);
	rc = (jintLong)gdk_region_new();
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1offset
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1offset_FUNC);
	gdk_region_offset((GdkRegion *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1offset_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1point_1in
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1region_1point_1in)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1point_1in_FUNC);
	rc = (jboolean)gdk_region_point_in((GdkRegion *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1point_1in_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1polygon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1region_1polygon)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1polygon_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_region_polygon((GdkPoint *)lparg0, arg1, (GdkFillRule)arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1polygon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1rect_1in
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1region_1rect_1in)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1rect_1in_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)gdk_region_rect_in((GdkRegion *)arg0, (GdkRectangle *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1rect_1in_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1rectangle
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1region_1rectangle)
	(JNIEnv *env, jclass that, jobject arg0)
{
	GdkRectangle _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1rectangle_FUNC);
	if (arg0) if ((lparg0 = getGdkRectangleFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)gdk_region_rectangle(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1rectangle_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1region_1subtract
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1subtract)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1subtract_FUNC);
	gdk_region_subtract((GdkRegion *)arg0, (GdkRegion *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1subtract_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1union
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1union)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1union_FUNC);
	gdk_region_union((GdkRegion *)arg0, (GdkRegion *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1union_FUNC);
}
#endif

#ifndef NO__1gdk_1region_1union_1with_1rect
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1region_1union_1with_1rect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1region_1union_1with_1rect_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_region_union_with_rect((GdkRegion *)arg0, (GdkRectangle *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1region_1union_1with_1rect_FUNC);
}
#endif

#ifndef NO__1gdk_1rgb_1init
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1rgb_1init)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1rgb_1init_FUNC);
	gdk_rgb_init();
	OS_NATIVE_EXIT(env, that, _1gdk_1rgb_1init_FUNC);
}
#endif

#ifndef NO__1gdk_1screen_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1screen_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1default_FUNC);
/*
	rc = (jintLong)gdk_screen_get_default();
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_default)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1get_1monitor_1at_1point
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1get_1monitor_1at_1point)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1monitor_1at_1point_FUNC);
/*
	rc = (jint)gdk_screen_get_monitor_at_point((GdkScreen *)arg0, (gint)arg1, (gint)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_monitor_at_point)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GdkScreen *, gint, gint))fp)((GdkScreen *)arg0, (gint)arg1, (gint)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1monitor_1at_1point_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1get_1monitor_1at_1window
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1get_1monitor_1at_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1monitor_1at_1window_FUNC);
/*
	rc = (jint)gdk_screen_get_monitor_at_window((GdkScreen *)arg0, (GdkWindow *)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_monitor_at_window)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GdkScreen *, GdkWindow *))fp)((GdkScreen *)arg0, (GdkWindow *)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1monitor_1at_1window_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1get_1monitor_1geometry
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1screen_1get_1monitor_1geometry)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkRectangle _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1monitor_1geometry_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
/*
	gdk_screen_get_monitor_geometry((GdkScreen *)arg0, arg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_monitor_geometry)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkScreen *, jint, GdkRectangle *))fp)((GdkScreen *)arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) setGdkRectangleFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1monitor_1geometry_FUNC);
}
#endif

#ifndef NO__1gdk_1screen_1get_1n_1monitors
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1get_1n_1monitors)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1n_1monitors_FUNC);
/*
	rc = (jint)gdk_screen_get_n_monitors((GdkScreen *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_n_monitors)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GdkScreen *))fp)((GdkScreen *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1n_1monitors_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1get_1number
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1get_1number)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1get_1number_FUNC);
/*
	rc = (jint)gdk_screen_get_number((GdkScreen *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_screen_get_number)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GdkScreen *))fp)((GdkScreen *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1get_1number_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1height
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1height)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1height_FUNC);
	rc = (jint)gdk_screen_height();
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1width)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1width_FUNC);
	rc = (jint)gdk_screen_width();
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1screen_1width_1mm
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1screen_1width_1mm)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1screen_1width_1mm_FUNC);
	rc = (jint)gdk_screen_width_mm();
	OS_NATIVE_EXIT(env, that, _1gdk_1screen_1width_1mm_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1set_1program_1class
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1set_1program_1class)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1set_1program_1class_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	gdk_set_program_class((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1set_1program_1class_FUNC);
}
#endif

#ifndef NO__1gdk_1text_1property_1to_1utf8_1list
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1text_1property_1to_1utf8_1list)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3, jintLongArray arg4)
{
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1text_1property_1to_1utf8_1list_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)gdk_text_property_to_utf8_list((GdkAtom)arg0, arg1, (guchar *)arg2, arg3, (gchar ***)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1text_1property_1to_1utf8_1list_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1unicode_1to_1keyval
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1unicode_1to_1keyval)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1unicode_1to_1keyval_FUNC);
	rc = (jint)gdk_unicode_to_keyval(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1unicode_1to_1keyval_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1utf8_1to_1compound_1text
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1utf8_1to_1compound_1text)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLongArray arg1, jintArray arg2, jintLongArray arg3, jintArray arg4)
{
	jbyte *lparg0=NULL;
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1utf8_1to_1compound_1text_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)gdk_utf8_to_compound_text((const gchar *)lparg0, (GdkAtom *)lparg1, (gint *)lparg2, (guchar **)lparg3, (gint *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1utf8_1to_1compound_1text_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1utf8_1to_1string_1target
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1utf8_1to_1string_1target)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1utf8_1to_1string_1target_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_utf8_to_string_target((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1utf8_1to_1string_1target_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1visual_1get_1system
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1visual_1get_1system)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1visual_1get_1system_FUNC);
	rc = (jintLong)gdk_visual_get_system();
	OS_NATIVE_EXIT(env, that, _1gdk_1visual_1get_1system_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1add_1filter
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1add_1filter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1add_1filter_FUNC);
	gdk_window_add_filter((GdkWindow *)arg0, (GdkFilterFunc)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1add_1filter_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1at_1pointer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1at_1pointer)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1at_1pointer_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_window_at_pointer((gint *)lparg0, (gint *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1at_1pointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1begin_1paint_1rect
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1begin_1paint_1rect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1begin_1paint_1rect_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_window_begin_paint_rect((GdkWindow *)arg0, (GdkRectangle *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1begin_1paint_1rect_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1clear_1area
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1clear_1area)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1clear_1area_FUNC);
	gdk_window_clear_area((GdkWindow *)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1clear_1area_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1destroy
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1destroy_FUNC);
	gdk_window_destroy((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1destroy_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1end_1paint
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1end_1paint)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1end_1paint_FUNC);
	gdk_window_end_paint((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1end_1paint_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1focus
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1focus)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1focus_FUNC);
	gdk_window_focus((GdkWindow *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1focus_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1freeze_1updates
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1freeze_1updates)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1freeze_1updates_FUNC);
	gdk_window_freeze_updates((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1freeze_1updates_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1get_1children
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1get_1children)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1children_FUNC);
	rc = (jintLong)gdk_window_get_children((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1children_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1get_1events
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1window_1get_1events)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1events_FUNC);
	rc = (jint)gdk_window_get_events((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1events_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1get_1frame_1extents
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1get_1frame_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1frame_1extents_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gdk_window_get_frame_extents((GdkWindow *)arg0, (GdkRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setGdkRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1frame_1extents_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1get_1internal_1paint_1info
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1get_1internal_1paint_1info)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2, jintArray arg3)
{
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1internal_1paint_1info_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gdk_window_get_internal_paint_info((GdkWindow *)arg0, (GdkDrawable **)lparg1, (gint *)lparg2, (gint *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1internal_1paint_1info_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1get_1origin
JNIEXPORT jint JNICALL OS_NATIVE(_1gdk_1window_1get_1origin)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1origin_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)gdk_window_get_origin((GdkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1origin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1get_1parent
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1get_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1parent_FUNC);
	rc = (jintLong)gdk_window_get_parent((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1get_1pointer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1get_1pointer)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1pointer_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)gdk_window_get_pointer((GdkWindow *)arg0, (gint *)lparg1, (gint *)lparg2, (GdkModifierType *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1pointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1get_1position
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1get_1position)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1position_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gdk_window_get_position((GdkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1position_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1get_1user_1data
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1get_1user_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1get_1user_1data_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gdk_window_get_user_data((GdkWindow *)arg0, (gpointer *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1get_1user_1data_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1hide
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1hide)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1hide_FUNC);
	gdk_window_hide((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1hide_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1invalidate_1rect
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1invalidate_1rect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jboolean arg2)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1invalidate_1rect_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	gdk_window_invalidate_rect((GdkWindow *)arg0, (GdkRectangle *)lparg1, (gboolean)arg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1invalidate_1rect_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1invalidate_1region
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1invalidate_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1invalidate_1region_FUNC);
	gdk_window_invalidate_region((GdkWindow *)arg0, (GdkRegion *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1invalidate_1region_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1is_1viewable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1window_1is_1viewable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1is_1viewable_FUNC);
	rc = (jboolean)gdk_window_is_viewable((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1is_1viewable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1is_1visible
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gdk_1window_1is_1visible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1is_1visible_FUNC);
	rc = (jboolean)gdk_window_is_visible((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1is_1visible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1lookup
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1lookup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1lookup_FUNC);
	rc = (jintLong)gdk_window_lookup(arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1lookup_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1lower
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1lower)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1lower_FUNC);
	gdk_window_lower((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1lower_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1move
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1move)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1move_FUNC);
	gdk_window_move((GdkWindow *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1move_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1move_1resize
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1move_1resize)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1move_1resize_FUNC);
	gdk_window_move_resize((GdkWindow *)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1move_1resize_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1window_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	GdkWindowAttr _arg1, *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1new_FUNC);
	if (arg1) if ((lparg1 = getGdkWindowAttrFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)gdk_window_new((GdkWindow *)arg0, lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1window_1process_1all_1updates
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1process_1all_1updates)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1process_1all_1updates_FUNC);
	gdk_window_process_all_updates();
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1process_1all_1updates_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1process_1updates
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1process_1updates)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1process_1updates_FUNC);
	gdk_window_process_updates((GdkWindow *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1process_1updates_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1raise
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1raise)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1raise_FUNC);
	gdk_window_raise((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1raise_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1remove_1filter
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1remove_1filter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1remove_1filter_FUNC);
	gdk_window_remove_filter((GdkWindow *)arg0, (GdkFilterFunc)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1remove_1filter_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1resize
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1resize)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1resize_FUNC);
	gdk_window_resize((GdkWindow *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1resize_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1restack
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1restack)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1restack_FUNC);
/*
	gdk_window_restack((GdkWindow *)arg0, (GdkWindow *)arg1, (gboolean)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_window_restack)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkWindow *, GdkWindow *, gboolean))fp)((GdkWindow *)arg0, (GdkWindow *)arg1, (gboolean)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1restack_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1scroll
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1scroll)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1scroll_FUNC);
	gdk_window_scroll((GdkWindow *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1scroll_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1accept_1focus
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1accept_1focus)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1accept_1focus_FUNC);
/*
	gdk_window_set_accept_focus((GdkWindow *)arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_window_set_accept_focus)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkWindow *, gboolean))fp)((GdkWindow *)arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1accept_1focus_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1back_1pixmap
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1back_1pixmap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1back_1pixmap_FUNC);
	gdk_window_set_back_pixmap((GdkWindow *)arg0, (GdkPixmap *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1back_1pixmap_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1cursor
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1cursor)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1cursor_FUNC);
	gdk_window_set_cursor((GdkWindow *)arg0, (GdkCursor *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1cursor_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1debug_1updates
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1debug_1updates)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1debug_1updates_FUNC);
	gdk_window_set_debug_updates((gboolean)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1debug_1updates_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1decorations
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1decorations)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1decorations_FUNC);
	gdk_window_set_decorations((GdkWindow *)arg0, (GdkWMDecoration)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1decorations_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1events
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1events)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1events_FUNC);
	gdk_window_set_events((GdkWindow *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1events_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1icon
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1icon_FUNC);
	gdk_window_set_icon((GdkWindow *)arg0, (GdkWindow *)arg1, (GdkPixmap *)arg2, (GdkBitmap *)arg3);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1icon_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1icon_1list
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1icon_1list)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1icon_1list_FUNC);
	gdk_window_set_icon_list((GdkWindow *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1icon_1list_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1keep_1above
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1keep_1above)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1keep_1above_FUNC);
/*
	gdk_window_set_keep_above((GdkWindow *)arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_window_set_keep_above)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkWindow *, gboolean))fp)((GdkWindow *)arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1keep_1above_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1override_1redirect
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1override_1redirect)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1override_1redirect_FUNC);
	gdk_window_set_override_redirect((GdkWindow *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1override_1redirect_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1set_1user_1data
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1set_1user_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1set_1user_1data_FUNC);
	gdk_window_set_user_data((GdkWindow *)arg0, (gpointer)arg1);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1set_1user_1data_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1shape_1combine_1region
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1shape_1combine_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1shape_1combine_1region_FUNC);
	gdk_window_shape_combine_region((GdkWindow *)arg0, (GdkRegion *)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1shape_1combine_1region_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1show
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1show)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1show_FUNC);
	gdk_window_show((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1show_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1show_1unraised
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1show_1unraised)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1show_1unraised_FUNC);
	gdk_window_show_unraised((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1show_1unraised_FUNC);
}
#endif

#ifndef NO__1gdk_1window_1thaw_1updates
JNIEXPORT void JNICALL OS_NATIVE(_1gdk_1window_1thaw_1updates)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gdk_1window_1thaw_1updates_FUNC);
	gdk_window_thaw_updates((GdkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1window_1thaw_1updates_FUNC);
}
#endif

#ifndef NO__1gdk_1x11_1atom_1to_1xatom
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1atom_1to_1xatom)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1atom_1to_1xatom_FUNC);
	rc = (jintLong)gdk_x11_atom_to_xatom((GdkAtom)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1atom_1to_1xatom_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1colormap_1get_1xcolormap
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1colormap_1get_1xcolormap)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1colormap_1get_1xcolormap_FUNC);
	rc = (jintLong)gdk_x11_colormap_get_xcolormap((GdkColormap *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1colormap_1get_1xcolormap_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1drawable_1get_1xdisplay
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1drawable_1get_1xdisplay)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1drawable_1get_1xdisplay_FUNC);
	rc = (jintLong)gdk_x11_drawable_get_xdisplay((GdkDrawable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1drawable_1get_1xdisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1drawable_1get_1xid
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1drawable_1get_1xid)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1drawable_1get_1xid_FUNC);
	rc = (jintLong)gdk_x11_drawable_get_xid((GdkDrawable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1drawable_1get_1xid_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1screen_1get_1window_1manager_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1screen_1get_1window_1manager_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1screen_1get_1window_1manager_1name_FUNC);
/*
	rc = (jintLong)gdk_x11_screen_get_window_manager_name((GdkScreen *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_x11_screen_get_window_manager_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(GdkScreen *))fp)((GdkScreen *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1screen_1get_1window_1manager_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1screen_1lookup_1visual
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1screen_1lookup_1visual)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1screen_1lookup_1visual_FUNC);
/*
	rc = (jintLong)gdk_x11_screen_lookup_visual((GdkScreen *)arg0, (VisualID)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_x11_screen_lookup_visual)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(GdkScreen *, VisualID))fp)((GdkScreen *)arg0, (VisualID)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1screen_1lookup_1visual_FUNC);
	return rc;
}
#endif

#ifndef NO__1gdk_1x11_1visual_1get_1xvisual
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gdk_1x11_1visual_1get_1xvisual)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gdk_1x11_1visual_1get_1xvisual_FUNC);
	rc = (jintLong)gdk_x11_visual_get_xvisual((GdkVisual *)arg0);
	OS_NATIVE_EXIT(env, that, _1gdk_1x11_1visual_1get_1xvisual_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1accel_1group_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1accel_1group_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1accel_1group_1new_FUNC);
	rc = (jintLong)gtk_accel_group_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1accel_1group_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1accel_1groups_1activate
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1accel_1groups_1activate)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1accel_1groups_1activate_FUNC);
	rc = (jboolean)gtk_accel_groups_activate((GObject *)arg0, (guint)arg1, (GdkModifierType)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1accel_1groups_1activate_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1accel_1label_1set_1accel_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1accel_1label_1set_1accel_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1accel_1label_1set_1accel_1widget_FUNC);
	gtk_accel_label_set_accel_widget((GtkAccelLabel *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1accel_1label_1set_1accel_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1accelerator_1get_1default_1mod_1mask
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1accelerator_1get_1default_1mod_1mask)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1accelerator_1get_1default_1mod_1mask_FUNC);
	rc = (jint)gtk_accelerator_get_default_mod_mask();
	OS_NATIVE_EXIT(env, that, _1gtk_1accelerator_1get_1default_1mod_1mask_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1accelerator_1parse
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1accelerator_1parse)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1accelerator_1parse_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_accelerator_parse((const gchar *)arg0, (guint *)lparg1, (GdkModifierType *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1accelerator_1parse_FUNC);
}
#endif

#ifndef NO__1gtk_1adjustment_1changed
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1adjustment_1changed)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1adjustment_1changed_FUNC);
	gtk_adjustment_changed((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1adjustment_1changed_FUNC);
}
#endif

#ifndef NO__1gtk_1adjustment_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1adjustment_1new)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1adjustment_1new_FUNC);
	rc = (jintLong)gtk_adjustment_new((gdouble)arg0, (gdouble)arg1, (gdouble)arg2, (gdouble)arg3, (gdouble)arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1gtk_1adjustment_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1adjustment_1set_1value
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1adjustment_1set_1value)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1adjustment_1set_1value_FUNC);
	gtk_adjustment_set_value((GtkAdjustment *)arg0, (gdouble)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1adjustment_1set_1value_FUNC);
}
#endif

#ifndef NO__1gtk_1adjustment_1value_1changed
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1adjustment_1value_1changed)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1adjustment_1value_1changed_FUNC);
	gtk_adjustment_value_changed((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1adjustment_1value_1changed_FUNC);
}
#endif

#ifndef NO__1gtk_1arrow_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1arrow_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1arrow_1new_FUNC);
	rc = (jintLong)gtk_arrow_new((GtkArrowType)arg0, (GtkShadowType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1arrow_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1arrow_1set
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1arrow_1set)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1arrow_1set_FUNC);
	gtk_arrow_set((GtkArrow *)arg0, (GtkArrowType)arg1, (GtkShadowType)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1arrow_1set_FUNC);
}
#endif

#ifndef NO__1gtk_1bin_1get_1child
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1bin_1get_1child)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1bin_1get_1child_FUNC);
	rc = (jintLong)gtk_bin_get_child((GtkBin *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1bin_1get_1child_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1border_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1border_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1border_1free_FUNC);
	gtk_border_free((GtkBorder *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1border_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1box_1set_1child_1packing
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1box_1set_1child_1packing)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2, jboolean arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1box_1set_1child_1packing_FUNC);
	gtk_box_set_child_packing((GtkBox *)arg0, (GtkWidget *)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1gtk_1box_1set_1child_1packing_FUNC);
}
#endif

#ifndef NO__1gtk_1box_1set_1spacing
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1box_1set_1spacing)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1box_1set_1spacing_FUNC);
	gtk_box_set_spacing((GtkBox *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1box_1set_1spacing_FUNC);
}
#endif

#ifndef NO__1gtk_1button_1clicked
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1button_1clicked)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1button_1clicked_FUNC);
	gtk_button_clicked((GtkButton *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1button_1clicked_FUNC);
}
#endif

#ifndef NO__1gtk_1button_1get_1relief
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1button_1get_1relief)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1button_1get_1relief_FUNC);
	rc = (jint)gtk_button_get_relief((GtkButton *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1button_1get_1relief_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1button_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1button_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1button_1new_FUNC);
	rc = (jintLong)gtk_button_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1button_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1button_1set_1relief
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1button_1set_1relief)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1button_1set_1relief_FUNC);
	gtk_button_set_relief((GtkButton *)arg0, (GtkReliefStyle)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1button_1set_1relief_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1clear_1marks
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1clear_1marks)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1clear_1marks_FUNC);
/*
	gtk_calendar_clear_marks((GtkCalendar *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_clear_marks)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *))fp)((GtkCalendar *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1clear_1marks_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1display_1options
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1display_1options)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1display_1options_FUNC);
/*
	gtk_calendar_display_options((GtkCalendar *)arg0, (GtkCalendarDisplayOptions)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_display_options)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *, GtkCalendarDisplayOptions))fp)((GtkCalendar *)arg0, (GtkCalendarDisplayOptions)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1display_1options_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1get_1date
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1get_1date)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1get_1date_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	gtk_calendar_get_date((GtkCalendar *)arg0, (guint *)lparg1, (guint *)lparg2, (guint *)lparg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_get_date)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *, guint *, guint *, guint *))fp)((GtkCalendar *)arg0, (guint *)lparg1, (guint *)lparg2, (guint *)lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1get_1date_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1mark_1day
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1mark_1day)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1mark_1day_FUNC);
/*
	gtk_calendar_mark_day((GtkCalendar *)arg0, (guint)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_mark_day)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *, guint))fp)((GtkCalendar *)arg0, (guint)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1mark_1day_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1calendar_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1new_FUNC);
/*
	rc = (jintLong)gtk_calendar_new();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1calendar_1select_1day
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1select_1day)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1select_1day_FUNC);
/*
	gtk_calendar_select_day((GtkCalendar *)arg0, (guint)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_select_day)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *, guint))fp)((GtkCalendar *)arg0, (guint)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1select_1day_FUNC);
}
#endif

#ifndef NO__1gtk_1calendar_1select_1month
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1calendar_1select_1month)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1select_1month_FUNC);
/*
	rc = (jboolean)gtk_calendar_select_month((GtkCalendar *)arg0, (guint)arg1, (guint)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_select_month)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(GtkCalendar *, guint, guint))fp)((GtkCalendar *)arg0, (guint)arg1, (guint)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1select_1month_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1calendar_1set_1display_1options
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1calendar_1set_1display_1options)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1calendar_1set_1display_1options_FUNC);
/*
	gtk_calendar_set_display_options((GtkCalendar *)arg0, (GtkCalendarDisplayOptions)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_calendar_set_display_options)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkCalendar *, GtkCalendarDisplayOptions))fp)((GtkCalendar *)arg0, (GtkCalendarDisplayOptions)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1calendar_1set_1display_1options_FUNC);
}
#endif

#ifndef NO__1gtk_1cell_1layout_1clear
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1cell_1layout_1clear)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1layout_1clear_FUNC);
/*
	gtk_cell_layout_clear(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_cell_layout_clear)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1layout_1clear_FUNC);
}
#endif

#ifndef NO__1gtk_1cell_1layout_1pack_1start
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1cell_1layout_1pack_1start)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1layout_1pack_1start_FUNC);
/*
	gtk_cell_layout_pack_start(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_cell_layout_pack_start)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jboolean))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1layout_1pack_1start_FUNC);
}
#endif

#ifndef NO__1gtk_1cell_1renderer_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1cell_1renderer_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
{
	GdkRectangle _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1renderer_1get_1size_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_cell_renderer_get_size((GtkCellRenderer *)arg0, (GtkWidget *)arg1, (GdkRectangle *)lparg2, (gint *)lparg3, (gint *)lparg4, (gint *)lparg5, (gint *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGdkRectangleFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1renderer_1get_1size_FUNC);
}
#endif

#ifndef NO__1gtk_1cell_1renderer_1pixbuf_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1cell_1renderer_1pixbuf_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1renderer_1pixbuf_1new_FUNC);
	rc = (jintLong)gtk_cell_renderer_pixbuf_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1renderer_1pixbuf_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1cell_1renderer_1text_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1cell_1renderer_1text_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1renderer_1text_1new_FUNC);
	rc = (jintLong)gtk_cell_renderer_text_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1renderer_1text_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1cell_1renderer_1toggle_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1cell_1renderer_1toggle_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1cell_1renderer_1toggle_1new_FUNC);
	rc = (jintLong)gtk_cell_renderer_toggle_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1cell_1renderer_1toggle_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1check_1button_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1check_1button_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1check_1button_1new_FUNC);
	rc = (jintLong)gtk_check_button_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1check_1button_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1check_1menu_1item_1get_1active
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1check_1menu_1item_1get_1active)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1check_1menu_1item_1get_1active_FUNC);
	rc = (jboolean)gtk_check_menu_item_get_active((GtkCheckMenuItem *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1check_1menu_1item_1get_1active_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1check_1menu_1item_1new_1with_1label
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1check_1menu_1item_1new_1with_1label)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1check_1menu_1item_1new_1with_1label_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_check_menu_item_new_with_label((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1check_1menu_1item_1new_1with_1label_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1check_1menu_1item_1set_1active
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1check_1menu_1item_1set_1active)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1check_1menu_1item_1set_1active_FUNC);
	gtk_check_menu_item_set_active((GtkCheckMenuItem *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1check_1menu_1item_1set_1active_FUNC);
}
#endif

#ifndef NO__1gtk_1check_1version
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1check_1version)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1check_1version_FUNC);
	rc = (jintLong)gtk_check_version(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1check_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1clipboard_1clear
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1clipboard_1clear)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1clear_FUNC);
	gtk_clipboard_clear((GtkClipboard *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1clear_FUNC);
}
#endif

#ifndef NO__1gtk_1clipboard_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1clipboard_1get)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1get_FUNC);
	rc = (jintLong)gtk_clipboard_get((GdkAtom)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1clipboard_1set_1can_1store
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1clipboard_1set_1can_1store)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1set_1can_1store_FUNC);
/*
	gtk_clipboard_set_can_store((GtkClipboard *)arg0, (const GtkTargetEntry *)arg1, (gint)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_clipboard_set_can_store)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkClipboard *, const GtkTargetEntry *, gint))fp)((GtkClipboard *)arg0, (const GtkTargetEntry *)arg1, (gint)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1set_1can_1store_FUNC);
}
#endif

#ifndef NO__1gtk_1clipboard_1set_1with_1data
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1clipboard_1set_1with_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jintLong arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1set_1with_1data_FUNC);
	rc = (jboolean)gtk_clipboard_set_with_data((GtkClipboard *)arg0, (const GtkTargetEntry *)arg1, (guint)arg2, (GtkClipboardGetFunc)arg3, (GtkClipboardClearFunc)arg4, (GObject *)arg5);
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1set_1with_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1clipboard_1set_1with_1owner
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1clipboard_1set_1with_1owner)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jintLong arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1set_1with_1owner_FUNC);
	rc = (jboolean)gtk_clipboard_set_with_owner((GtkClipboard *)arg0, (const GtkTargetEntry *)arg1, (guint)arg2, (GtkClipboardGetFunc)arg3, (GtkClipboardClearFunc)arg4, (GObject *)arg5);
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1set_1with_1owner_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1clipboard_1store
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1clipboard_1store)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1store_FUNC);
/*
	gtk_clipboard_store((GtkClipboard *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_clipboard_store)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkClipboard *))fp)((GtkClipboard *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1store_FUNC);
}
#endif

#ifndef NO__1gtk_1clipboard_1wait_1for_1contents
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1clipboard_1wait_1for_1contents)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1clipboard_1wait_1for_1contents_FUNC);
	rc = (jintLong)gtk_clipboard_wait_for_contents((GtkClipboard *)arg0, (GdkAtom)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1clipboard_1wait_1for_1contents_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1color_1selection_1dialog_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1color_1selection_1dialog_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1color_1selection_1dialog_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_color_selection_dialog_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1color_1selection_1dialog_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1color_1selection_1get_1current_1color
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1color_1selection_1get_1current_1color)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1color_1selection_1get_1current_1color_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gtk_color_selection_get_current_color((GtkColorSelection *)arg0, (GdkColor *)lparg1);
fail:
	if (arg1 && lparg1) setGdkColorFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1color_1selection_1get_1current_1color_FUNC);
}
#endif

#ifndef NO__1gtk_1color_1selection_1set_1current_1color
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1color_1selection_1set_1current_1color)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1color_1selection_1set_1current_1color_FUNC);
	if (arg1) if ((lparg1 = getGdkColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	gtk_color_selection_set_current_color((GtkColorSelection *)arg0, (GdkColor *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1color_1selection_1set_1current_1color_FUNC);
}
#endif

#ifndef NO__1gtk_1color_1selection_1set_1has_1palette
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1color_1selection_1set_1has_1palette)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1color_1selection_1set_1has_1palette_FUNC);
	gtk_color_selection_set_has_palette((GtkColorSelection *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1color_1selection_1set_1has_1palette_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1entry_1new_1text
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1combo_1box_1entry_1new_1text)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1entry_1new_1text_FUNC);
/*
	rc = (jintLong)gtk_combo_box_entry_new_text();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_entry_new_text)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1entry_1new_1text_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1combo_1box_1get_1active
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1combo_1box_1get_1active)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1get_1active_FUNC);
/*
	rc = (jint)gtk_combo_box_get_active(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_get_active)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1get_1active_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1combo_1box_1get_1model
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1combo_1box_1get_1model)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1get_1model_FUNC);
/*
	rc = (jintLong)gtk_combo_box_get_model(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_get_model)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1get_1model_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1combo_1box_1insert_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1insert_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1insert_1text_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	gtk_combo_box_insert_text(arg0, arg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_insert_text)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint, jbyte *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1insert_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1new_1text
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1combo_1box_1new_1text)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1new_1text_FUNC);
/*
	rc = (jintLong)gtk_combo_box_new_text();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_new_text)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1new_1text_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1combo_1box_1popdown
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1popdown)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1popdown_FUNC);
/*
	gtk_combo_box_popdown(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_popdown)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1popdown_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1popup
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1popup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1popup_FUNC);
/*
	gtk_combo_box_popup(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_popup)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1popup_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1remove_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1remove_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1remove_1text_FUNC);
/*
	gtk_combo_box_remove_text(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_remove_text)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1remove_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1set_1active
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1set_1active)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1set_1active_FUNC);
/*
	gtk_combo_box_set_active(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_set_active)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1set_1active_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1box_1set_1focus_1on_1click
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1box_1set_1focus_1on_1click)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1box_1set_1focus_1on_1click_FUNC);
/*
	gtk_combo_box_set_focus_on_click(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_combo_box_set_focus_on_click)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1box_1set_1focus_1on_1click_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1disable_1activate
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1disable_1activate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1disable_1activate_FUNC);
	gtk_combo_disable_activate((GtkCombo *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1disable_1activate_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1combo_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1new_FUNC);
	rc = (jintLong)gtk_combo_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1combo_1set_1case_1sensitive
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1set_1case_1sensitive)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1set_1case_1sensitive_FUNC);
	gtk_combo_set_case_sensitive((GtkCombo *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1set_1case_1sensitive_FUNC);
}
#endif

#ifndef NO__1gtk_1combo_1set_1popdown_1strings
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1combo_1set_1popdown_1strings)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1combo_1set_1popdown_1strings_FUNC);
	gtk_combo_set_popdown_strings((GtkCombo *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1combo_1set_1popdown_1strings_FUNC);
}
#endif

#ifndef NO__1gtk_1container_1add
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1container_1add)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1add_FUNC);
	gtk_container_add((GtkContainer *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1add_FUNC);
}
#endif

#ifndef NO__1gtk_1container_1forall
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1container_1forall)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1forall_FUNC);
	gtk_container_forall((GtkContainer *)arg0, (GtkCallback)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1forall_FUNC);
}
#endif

#ifndef NO__1gtk_1container_1get_1border_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1container_1get_1border_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1get_1border_1width_FUNC);
	rc = (jint)gtk_container_get_border_width((GtkContainer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1get_1border_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1container_1get_1children
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1container_1get_1children)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1get_1children_FUNC);
	rc = (jintLong)gtk_container_get_children((GtkContainer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1get_1children_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1container_1remove
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1container_1remove)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1remove_FUNC);
	gtk_container_remove((GtkContainer *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1remove_FUNC);
}
#endif

#ifndef NO__1gtk_1container_1resize_1children
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1container_1resize_1children)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1resize_1children_FUNC);
	gtk_container_resize_children((GtkContainer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1resize_1children_FUNC);
}
#endif

#ifndef NO__1gtk_1container_1set_1border_1width
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1container_1set_1border_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1container_1set_1border_1width_FUNC);
	gtk_container_set_border_width((GtkContainer *)arg0, (guint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1container_1set_1border_1width_FUNC);
}
#endif

#ifndef NO__1gtk_1dialog_1add_1button
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1dialog_1add_1button)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1dialog_1add_1button_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_dialog_add_button((GtkDialog *)arg0, (const gchar *)lparg1, (gint)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1dialog_1add_1button_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1dialog_1run
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1dialog_1run)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1dialog_1run_FUNC);
	rc = (jint)gtk_dialog_run((GtkDialog *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1dialog_1run_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1drag_1begin
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1drag_1begin)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1begin_FUNC);
	rc = (jintLong)gtk_drag_begin((GtkWidget *)arg0, (GtkTargetList *)arg1, (GdkDragAction)arg2, (gint)arg3, (GdkEvent *)arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1begin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1drag_1check_1threshold
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1drag_1check_1threshold)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1check_1threshold_FUNC);
	rc = (jboolean)gtk_drag_check_threshold((GtkWidget *)arg0, (gint)arg1, (gint)arg2, (gint)arg3, (gint)arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1check_1threshold_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1drag_1dest_1find_1target
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1drag_1dest_1find_1target)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1dest_1find_1target_FUNC);
	rc = (jintLong)gtk_drag_dest_find_target((GtkWidget *)arg0, (GdkDragContext *)arg1, (GtkTargetList *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1dest_1find_1target_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1drag_1dest_1set
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1drag_1dest_1set)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1dest_1set_FUNC);
	gtk_drag_dest_set((GtkWidget *)arg0, (GtkDestDefaults)arg1, (const GtkTargetEntry *)arg2, (gint)arg3, (GdkDragAction)arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1dest_1set_FUNC);
}
#endif

#ifndef NO__1gtk_1drag_1dest_1unset
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1drag_1dest_1unset)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1dest_1unset_FUNC);
	gtk_drag_dest_unset((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1dest_1unset_FUNC);
}
#endif

#ifndef NO__1gtk_1drag_1finish
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1drag_1finish)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1finish_FUNC);
	gtk_drag_finish((GdkDragContext *)arg0, (gboolean)arg1, (gboolean)arg2, (guint32)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1finish_FUNC);
}
#endif

#ifndef NO__1gtk_1drag_1get_1data
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1drag_1get_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1get_1data_FUNC);
	gtk_drag_get_data((GtkWidget *)arg0, (GdkDragContext *)arg1, (GdkAtom)arg2, (guint32)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1get_1data_FUNC);
}
#endif

#ifndef NO__1gtk_1drag_1set_1icon_1pixbuf
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1drag_1set_1icon_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1drag_1set_1icon_1pixbuf_FUNC);
	gtk_drag_set_icon_pixbuf((GdkDragContext *)arg0, (GdkPixbuf *)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1drag_1set_1icon_1pixbuf_FUNC);
}
#endif

#ifndef NO__1gtk_1drawing_1area_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1drawing_1area_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1drawing_1area_1new_FUNC);
	rc = (jintLong)gtk_drawing_area_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1drawing_1area_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1editable_1copy_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1copy_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1copy_1clipboard_FUNC);
	gtk_editable_copy_clipboard((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1copy_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1cut_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1cut_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1cut_1clipboard_FUNC);
	gtk_editable_cut_clipboard((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1cut_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1delete_1selection
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1delete_1selection)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1delete_1selection_FUNC);
	gtk_editable_delete_selection((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1delete_1selection_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1delete_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1delete_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1delete_1text_FUNC);
	gtk_editable_delete_text((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1delete_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1get_1chars
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1editable_1get_1chars)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1get_1chars_FUNC);
	rc = (jintLong)gtk_editable_get_chars((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1get_1chars_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1editable_1get_1editable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1editable_1get_1editable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1get_1editable_FUNC);
	rc = (jboolean)gtk_editable_get_editable((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1get_1editable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1editable_1get_1position
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1editable_1get_1position)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1get_1position_FUNC);
	rc = (jint)gtk_editable_get_position((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1get_1position_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1editable_1get_1selection_1bounds
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1editable_1get_1selection_1bounds)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1get_1selection_1bounds_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_editable_get_selection_bounds((GtkEditable *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1get_1selection_1bounds_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1editable_1insert_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1insert_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1insert_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_editable_insert_text((GtkEditable *)arg0, (gchar *)lparg1, (gint)arg2, (gint *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1insert_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1paste_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1paste_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1paste_1clipboard_FUNC);
	gtk_editable_paste_clipboard((GtkEditable *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1paste_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1select_1region
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1select_1region)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1select_1region_FUNC);
	gtk_editable_select_region((GtkEditable *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1select_1region_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1set_1editable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1set_1editable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1set_1editable_FUNC);
	gtk_editable_set_editable((GtkEditable *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1set_1editable_FUNC);
}
#endif

#ifndef NO__1gtk_1editable_1set_1position
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1editable_1set_1position)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1editable_1set_1position_FUNC);
	gtk_editable_set_position((GtkEditable *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1editable_1set_1position_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1get_1inner_1border
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1entry_1get_1inner_1border)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1inner_1border_FUNC);
/*
	rc = (jintLong)gtk_entry_get_inner_border(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_entry_get_inner_border)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1inner_1border_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1get_1invisible_1char
JNIEXPORT jchar JNICALL OS_NATIVE(_1gtk_1entry_1get_1invisible_1char)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jchar rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1invisible_1char_FUNC);
	rc = (jchar)gtk_entry_get_invisible_char((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1invisible_1char_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1get_1layout
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1entry_1get_1layout)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1layout_FUNC);
	rc = (jintLong)gtk_entry_get_layout((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1layout_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1get_1layout_1offsets
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1get_1layout_1offsets)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1layout_1offsets_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_entry_get_layout_offsets((GtkEntry *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1layout_1offsets_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1get_1max_1length
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1entry_1get_1max_1length)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1max_1length_FUNC);
	rc = (jint)gtk_entry_get_max_length((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1max_1length_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1get_1text
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1entry_1get_1text)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1text_FUNC);
	rc = (jintLong)gtk_entry_get_text((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1text_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1get_1visibility
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1entry_1get_1visibility)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1get_1visibility_FUNC);
	rc = (jboolean)gtk_entry_get_visibility((GtkEntry *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1get_1visibility_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1entry_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1new_FUNC);
	rc = (jintLong)gtk_entry_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1entry_1set_1activates_1default
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1activates_1default)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1activates_1default_FUNC);
	gtk_entry_set_activates_default((GtkEntry *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1activates_1default_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1alignment
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1alignment)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1alignment_FUNC);
/*
	gtk_entry_set_alignment((GtkEntry *)arg0, (gfloat)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_entry_set_alignment)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkEntry *, gfloat))fp)((GtkEntry *)arg0, (gfloat)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1alignment_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1has_1frame
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1has_1frame)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1has_1frame_FUNC);
	gtk_entry_set_has_frame((GtkEntry *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1has_1frame_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1icon_1from_1stock
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1icon_1from_1stock)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1icon_1from_1stock_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	gtk_entry_set_icon_from_stock(arg0, arg1, lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_entry_set_icon_from_stock)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint, jbyte *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1icon_1from_1stock_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1icon_1sensitive
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1icon_1sensitive)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1icon_1sensitive_FUNC);
/*
	gtk_entry_set_icon_sensitive(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_entry_set_icon_sensitive)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint, jboolean))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1icon_1sensitive_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1invisible_1char
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1invisible_1char)
	(JNIEnv *env, jclass that, jintLong arg0, jchar arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1invisible_1char_FUNC);
	gtk_entry_set_invisible_char((GtkEntry *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1invisible_1char_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1max_1length
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1max_1length)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1max_1length_FUNC);
	gtk_entry_set_max_length((GtkEntry *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1max_1length_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_entry_set_text((GtkEntry *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1set_1visibility
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1entry_1set_1visibility)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1set_1visibility_FUNC);
	gtk_entry_set_visibility((GtkEntry *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1set_1visibility_FUNC);
}
#endif

#ifndef NO__1gtk_1entry_1text_1index_1to_1layout_1index
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1entry_1text_1index_1to_1layout_1index)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1entry_1text_1index_1to_1layout_1index_FUNC);
/*
	rc = (jint)gtk_entry_text_index_to_layout_index(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_entry_text_index_to_layout_index)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1entry_1text_1index_1to_1layout_1index_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1enumerate_1printers
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1enumerate_1printers)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1enumerate_1printers_FUNC);
/*
	gtk_enumerate_printers(arg0, (gpointer)arg1, (GDestroyNotify)arg2, (gboolean)arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_enumerate_printers)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, gpointer, GDestroyNotify, gboolean))fp)(arg0, (gpointer)arg1, (GDestroyNotify)arg2, (gboolean)arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1enumerate_1printers_FUNC);
}
#endif

#ifndef NO__1gtk_1events_1pending
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1events_1pending)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1events_1pending_FUNC);
	rc = (jint)gtk_events_pending();
	OS_NATIVE_EXIT(env, that, _1gtk_1events_1pending_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1expander_1get_1expanded
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1expander_1get_1expanded)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1get_1expanded_FUNC);
/*
	rc = (jboolean)gtk_expander_get_expanded(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_get_expanded)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1get_1expanded_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1expander_1get_1label_1widget
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1expander_1get_1label_1widget)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1get_1label_1widget_FUNC);
/*
	rc = (jintLong)gtk_expander_get_label_widget(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_get_label_widget)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1get_1label_1widget_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1expander_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1expander_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_expander_new((const gchar *)lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(const gchar *))fp)((const gchar *)lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1expander_1set_1expanded
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1expander_1set_1expanded)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1set_1expanded_FUNC);
/*
	gtk_expander_set_expanded(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_set_expanded)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1set_1expanded_FUNC);
}
#endif

#ifndef NO__1gtk_1expander_1set_1label
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1expander_1set_1label)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1set_1label_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_expander_set_label(arg0, (const gchar *)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_set_label)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, const gchar *))fp)(arg0, (const gchar *)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1set_1label_FUNC);
}
#endif

#ifndef NO__1gtk_1expander_1set_1label_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1expander_1set_1label_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1expander_1set_1label_1widget_FUNC);
/*
	gtk_expander_set_label_widget(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_expander_set_label_widget)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1expander_1set_1label_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1add_1filter
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1add_1filter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1add_1filter_FUNC);
/*
	gtk_file_chooser_add_filter(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_add_filter)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1add_1filter_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1current_1folder
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1current_1folder)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1current_1folder_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_current_folder(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_current_folder)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1current_1folder_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1filename
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1filename)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1filename_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_filename(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_filename)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1filename_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1filenames
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1filenames)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1filenames_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_filenames(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_filenames)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1filenames_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1filter
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1filter)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1filter_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_filter(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_filter)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1filter_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1uri
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1uri_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_uri(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_uri)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1get_1uris
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1chooser_1get_1uris)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1get_1uris_FUNC);
/*
	rc = (jintLong)gtk_file_chooser_get_uris(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_get_uris)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1get_1uris_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1current_1folder
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1current_1folder)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1current_1folder_FUNC);
/*
	gtk_file_chooser_set_current_folder(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_current_folder)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1current_1folder_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1current_1folder_1uri
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1current_1folder_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1current_1folder_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_file_chooser_set_current_folder_uri(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_current_folder_uri)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1current_1folder_1uri_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1current_1name
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1current_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1current_1name_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_file_chooser_set_current_name(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_current_name)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1current_1name_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1do_1overwrite_1confirmation
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1do_1overwrite_1confirmation)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1do_1overwrite_1confirmation_FUNC);
/*
	gtk_file_chooser_set_do_overwrite_confirmation(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_do_overwrite_confirmation)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1do_1overwrite_1confirmation_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1extra_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1extra_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1extra_1widget_FUNC);
/*
	gtk_file_chooser_set_extra_widget(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_extra_widget)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1extra_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1filename
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1filename)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1filename_FUNC);
/*
	gtk_file_chooser_set_filename(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_filename)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1filename_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1filter
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1filter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1filter_FUNC);
/*
	gtk_file_chooser_set_filter(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_filter)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1filter_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1local_1only
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1local_1only)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1local_1only_FUNC);
/*
	gtk_file_chooser_set_local_only(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_local_only)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1local_1only_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1select_1multiple
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1select_1multiple)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1select_1multiple_FUNC);
/*
	gtk_file_chooser_set_select_multiple(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_select_multiple)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1select_1multiple_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1chooser_1set_1uri
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1chooser_1set_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1set_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_file_chooser_set_uri(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_chooser_set_uri)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1set_1uri_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1filter_1add_1pattern
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1filter_1add_1pattern)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1filter_1add_1pattern_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_file_filter_add_pattern(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_filter_add_pattern)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1filter_1add_1pattern_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1filter_1get_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1filter_1get_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1filter_1get_1name_FUNC);
/*
	rc = (jintLong)gtk_file_filter_get_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_filter_get_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1filter_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1filter_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1filter_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1filter_1new_FUNC);
/*
	rc = (jintLong)gtk_file_filter_new();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_filter_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1filter_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1filter_1set_1name
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1filter_1set_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1filter_1set_1name_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_file_filter_set_name(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_file_filter_set_name)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1filter_1set_1name_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1selection_1get_1filename
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1selection_1get_1filename)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1get_1filename_FUNC);
	rc = (jintLong)gtk_file_selection_get_filename((GtkFileSelection *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1get_1filename_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1selection_1get_1selections
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1selection_1get_1selections)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1get_1selections_FUNC);
	rc = (jintLong)gtk_file_selection_get_selections((GtkFileSelection *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1get_1selections_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1selection_1hide_1fileop_1buttons
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1selection_1hide_1fileop_1buttons)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1hide_1fileop_1buttons_FUNC);
	gtk_file_selection_hide_fileop_buttons((GtkFileSelection *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1hide_1fileop_1buttons_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1selection_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1file_1selection_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_file_selection_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1selection_1set_1filename
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1selection_1set_1filename)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1set_1filename_FUNC);
	gtk_file_selection_set_filename((GtkFileSelection *)arg0, (const gchar *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1set_1filename_FUNC);
}
#endif

#ifndef NO__1gtk_1file_1selection_1set_1select_1multiple
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1file_1selection_1set_1select_1multiple)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1selection_1set_1select_1multiple_FUNC);
	gtk_file_selection_set_select_multiple((GtkFileSelection *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1selection_1set_1select_1multiple_FUNC);
}
#endif

#ifndef NO__1gtk_1fixed_1move
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1fixed_1move)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1fixed_1move_FUNC);
	gtk_fixed_move((GtkFixed *)arg0, (GtkWidget *)arg1, (gint)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1fixed_1move_FUNC);
}
#endif

#ifndef NO__1gtk_1fixed_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1fixed_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1fixed_1new_FUNC);
	rc = (jintLong)gtk_fixed_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1fixed_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1fixed_1set_1has_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1fixed_1set_1has_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1fixed_1set_1has_1window_FUNC);
	gtk_fixed_set_has_window((GtkFixed *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1fixed_1set_1has_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1font_1selection_1dialog_1get_1font_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1font_1selection_1dialog_1get_1font_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1font_1selection_1dialog_1get_1font_1name_FUNC);
	rc = (jintLong)gtk_font_selection_dialog_get_font_name((GtkFontSelectionDialog *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1font_1selection_1dialog_1get_1font_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1font_1selection_1dialog_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1font_1selection_1dialog_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1font_1selection_1dialog_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_font_selection_dialog_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1font_1selection_1dialog_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1font_1selection_1dialog_1set_1font_1name
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1font_1selection_1dialog_1set_1font_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1font_1selection_1dialog_1set_1font_1name_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_font_selection_dialog_set_font_name((GtkFontSelectionDialog *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1font_1selection_1dialog_1set_1font_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1frame_1get_1label_1widget
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1frame_1get_1label_1widget)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1frame_1get_1label_1widget_FUNC);
	rc = (jintLong)gtk_frame_get_label_widget((GtkFrame *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1frame_1get_1label_1widget_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1frame_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1frame_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1frame_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_frame_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1frame_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1frame_1set_1label
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1frame_1set_1label)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1frame_1set_1label_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_frame_set_label((GtkFrame *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1frame_1set_1label_FUNC);
}
#endif

#ifndef NO__1gtk_1frame_1set_1label_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1frame_1set_1label_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1frame_1set_1label_1widget_FUNC);
	gtk_frame_set_label_widget((GtkFrame *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1frame_1set_1label_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1frame_1set_1shadow_1type
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1frame_1set_1shadow_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1frame_1set_1shadow_1type_FUNC);
	gtk_frame_set_shadow_type((GtkFrame *)arg0, (GtkShadowType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1frame_1set_1shadow_1type_FUNC);
}
#endif

#ifndef NO__1gtk_1get_1current_1event
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1get_1current_1event)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1get_1current_1event_FUNC);
	rc = (jintLong)gtk_get_current_event();
	OS_NATIVE_EXIT(env, that, _1gtk_1get_1current_1event_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1get_1current_1event_1state
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1get_1current_1event_1state)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1get_1current_1event_1state_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_get_current_event_state((GdkModifierType*)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1get_1current_1event_1state_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1get_1current_1event_1time
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1get_1current_1event_1time)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1get_1current_1event_1time_FUNC);
	rc = (jint)gtk_get_current_event_time();
	OS_NATIVE_EXIT(env, that, _1gtk_1get_1current_1event_1time_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1get_1default_1language
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1get_1default_1language)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1get_1default_1language_FUNC);
	rc = (jintLong)gtk_get_default_language();
	OS_NATIVE_EXIT(env, that, _1gtk_1get_1default_1language_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1get_1event_1widget
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1get_1event_1widget)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1get_1event_1widget_FUNC);
	rc = (jintLong)gtk_get_event_widget((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1get_1event_1widget_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1grab_1add
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1grab_1add)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1grab_1add_FUNC);
	gtk_grab_add((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1grab_1add_FUNC);
}
#endif

#ifndef NO__1gtk_1grab_1get_1current
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1grab_1get_1current)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1grab_1get_1current_FUNC);
	rc = (jintLong)gtk_grab_get_current();
	OS_NATIVE_EXIT(env, that, _1gtk_1grab_1get_1current_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1grab_1remove
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1grab_1remove)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1grab_1remove_FUNC);
	gtk_grab_remove((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1grab_1remove_FUNC);
}
#endif

#ifndef NO__1gtk_1hbox_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1hbox_1new)
	(JNIEnv *env, jclass that, jboolean arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1hbox_1new_FUNC);
	rc = (jintLong)gtk_hbox_new((gboolean)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1hbox_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1hscale_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1hscale_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1hscale_1new_FUNC);
	rc = (jintLong)gtk_hscale_new((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1hscale_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1hscrollbar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1hscrollbar_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1hscrollbar_1new_FUNC);
	rc = (jintLong)gtk_hscrollbar_new((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1hscrollbar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1hseparator_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1hseparator_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1hseparator_1new_FUNC);
	rc = (jintLong)gtk_hseparator_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1hseparator_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1factory_1lookup_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1factory_1lookup_1default)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1factory_1lookup_1default_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_icon_factory_lookup_default((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1factory_1lookup_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1info_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1icon_1info_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1info_1free_FUNC);
/*
	gtk_icon_info_free(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_icon_info_free)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1info_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1icon_1info_1load_1icon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1info_1load_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1info_1load_1icon_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_icon_info_load_icon(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_icon_info_load_icon)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1info_1load_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1set_1render_1icon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1set_1render_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jintLong arg6)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1set_1render_1icon_FUNC);
	rc = (jintLong)gtk_icon_set_render_icon((GtkIconSet *)arg0, (GtkStyle *)arg1, (GtkTextDirection)arg2, (GtkStateType)arg3, (GtkIconSize)arg4, (GtkWidget *)arg5, (const char *)arg6);
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1set_1render_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1source_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1icon_1source_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1source_1free_FUNC);
	gtk_icon_source_free((GtkIconSource *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1source_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1icon_1source_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1source_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1source_1new_FUNC);
	rc = (jintLong)gtk_icon_source_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1source_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1source_1set_1pixbuf
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1icon_1source_1set_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1source_1set_1pixbuf_FUNC);
	gtk_icon_source_set_pixbuf((GtkIconSource *)arg0, (GdkPixbuf *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1source_1set_1pixbuf_FUNC);
}
#endif

#ifndef NO__1gtk_1icon_1theme_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1theme_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1theme_1get_1default_FUNC);
/*
	rc = (jintLong)gtk_icon_theme_get_default();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_icon_theme_get_default)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1theme_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1icon_1theme_1lookup_1by_1gicon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1icon_1theme_1lookup_1by_1gicon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1icon_1theme_1lookup_1by_1gicon_FUNC);
/*
	rc = (jintLong)gtk_icon_theme_lookup_by_gicon(arg0, arg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_icon_theme_lookup_by_gicon)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1icon_1theme_1lookup_1by_1gicon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1im_1context_1filter_1keypress
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1im_1context_1filter_1keypress)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1filter_1keypress_FUNC);
	rc = (jboolean)gtk_im_context_filter_keypress((GtkIMContext *)arg0, (GdkEventKey *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1filter_1keypress_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1im_1context_1focus_1in
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1focus_1in)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1focus_1in_FUNC);
	gtk_im_context_focus_in((GtkIMContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1focus_1in_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1context_1focus_1out
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1focus_1out)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1focus_1out_FUNC);
	gtk_im_context_focus_out((GtkIMContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1focus_1out_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1context_1get_1preedit_1string
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1get_1preedit_1string)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2, jintArray arg3)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1get_1preedit_1string_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_im_context_get_preedit_string((GtkIMContext *)arg0, (gchar **)lparg1, (PangoAttrList **)lparg2, (gint *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1get_1preedit_1string_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1context_1get_1type
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1im_1context_1get_1type)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1get_1type_FUNC);
	rc = (jintLong)gtk_im_context_get_type();
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1get_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1im_1context_1reset
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1reset)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1reset_FUNC);
	gtk_im_context_reset((GtkIMContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1reset_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1context_1set_1client_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1set_1client_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1set_1client_1window_FUNC);
	gtk_im_context_set_client_window((GtkIMContext *)arg0, (GdkWindow *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1set_1client_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1context_1set_1cursor_1location
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1context_1set_1cursor_1location)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1context_1set_1cursor_1location_FUNC);
	if (arg1) if ((lparg1 = getGdkRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	gtk_im_context_set_cursor_location((GtkIMContext *)arg0, (GdkRectangle *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1context_1set_1cursor_1location_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1multicontext_1append_1menuitems
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1im_1multicontext_1append_1menuitems)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1multicontext_1append_1menuitems_FUNC);
	gtk_im_multicontext_append_menuitems((GtkIMMulticontext *)arg0, (GtkMenuShell *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1multicontext_1append_1menuitems_FUNC);
}
#endif

#ifndef NO__1gtk_1im_1multicontext_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1im_1multicontext_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1im_1multicontext_1new_FUNC);
	rc = (jintLong)gtk_im_multicontext_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1im_1multicontext_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1image_1menu_1item_1new_1with_1label
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1image_1menu_1item_1new_1with_1label)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1menu_1item_1new_1with_1label_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_image_menu_item_new_with_label((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1menu_1item_1new_1with_1label_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1image_1menu_1item_1set_1image
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1image_1menu_1item_1set_1image)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1menu_1item_1set_1image_FUNC);
	gtk_image_menu_item_set_image((GtkImageMenuItem *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1menu_1item_1set_1image_FUNC);
}
#endif

#ifndef NO__1gtk_1image_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1image_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1new_FUNC);
	rc = (jintLong)gtk_image_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1image_1new_1from_1pixbuf
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1image_1new_1from_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1new_1from_1pixbuf_FUNC);
	rc = (jintLong)gtk_image_new_from_pixbuf((GdkPixbuf *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1new_1from_1pixbuf_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1image_1new_1from_1pixmap
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1image_1new_1from_1pixmap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1new_1from_1pixmap_FUNC);
	rc = (jintLong)gtk_image_new_from_pixmap((GdkPixmap *)arg0, (GdkBitmap *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1new_1from_1pixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1image_1set_1from_1pixbuf
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1image_1set_1from_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1set_1from_1pixbuf_FUNC);
	gtk_image_set_from_pixbuf((GtkImage *)arg0, (GdkPixbuf *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1set_1from_1pixbuf_FUNC);
}
#endif

#ifndef NO__1gtk_1image_1set_1from_1pixmap
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1image_1set_1from_1pixmap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1image_1set_1from_1pixmap_FUNC);
	gtk_image_set_from_pixmap((GtkImage *)arg0, (GdkBitmap *)arg1, (GdkBitmap *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1image_1set_1from_1pixmap_FUNC);
}
#endif

#ifndef NO__1gtk_1init_1check
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1init_1check)
	(JNIEnv *env, jclass that, jintLongArray arg0, jintLongArray arg1)
{
	jintLong *lparg0=NULL;
	jintLong *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1init_1check_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_init_check((int *)lparg0, (char ***)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntLongArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1init_1check_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1label_1get_1layout
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1label_1get_1layout)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1get_1layout_FUNC);
	rc = (jintLong)gtk_label_get_layout((GtkLabel *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1get_1layout_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1label_1get_1mnemonic_1keyval
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1label_1get_1mnemonic_1keyval)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1get_1mnemonic_1keyval_FUNC);
	rc = (jint)gtk_label_get_mnemonic_keyval((GtkLabel *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1get_1mnemonic_1keyval_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1label_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1label_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_label_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1label_1new_1with_1mnemonic
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1label_1new_1with_1mnemonic)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1new_1with_1mnemonic_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_label_new_with_mnemonic((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1new_1with_1mnemonic_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1label_1set_1attributes
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1attributes)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1attributes_FUNC);
	gtk_label_set_attributes((GtkLabel *)arg0, (PangoAttrList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1attributes_FUNC);
}
#endif

#ifndef NO__1gtk_1label_1set_1justify
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1justify)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1justify_FUNC);
	gtk_label_set_justify((GtkLabel *)arg0, (GtkJustification)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1justify_FUNC);
}
#endif

#ifndef NO__1gtk_1label_1set_1line_1wrap
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1line_1wrap)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1line_1wrap_FUNC);
	gtk_label_set_line_wrap((GtkLabel *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1line_1wrap_FUNC);
}
#endif

#ifndef NO__1gtk_1label_1set_1line_1wrap_1mode
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1line_1wrap_1mode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1line_1wrap_1mode_FUNC);
/*
	gtk_label_set_line_wrap_mode(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_label_set_line_wrap_mode)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1line_1wrap_1mode_FUNC);
}
#endif

#if (!defined(NO__1gtk_1label_1set_1text__II) && !defined(JNI64)) || (!defined(NO__1gtk_1label_1set_1text__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1text__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1text__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1text__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1text__JJ_FUNC);
#endif
	gtk_label_set_text((GtkLabel *)arg0, (const gchar *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1text__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1text__JJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1label_1set_1text__I_3B) && !defined(JNI64)) || (!defined(NO__1gtk_1label_1set_1text__J_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1text__I_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1text__J_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1text__I_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1text__J_3B_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_label_set_text((GtkLabel *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1text__I_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1text__J_3B_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1label_1set_1text_1with_1mnemonic
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1label_1set_1text_1with_1mnemonic)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1label_1set_1text_1with_1mnemonic_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_label_set_text_with_mnemonic((GtkLabel *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1label_1set_1text_1with_1mnemonic_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1append_1items
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1append_1items)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1append_1items_FUNC);
	gtk_list_append_items((GtkList *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1append_1items_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1clear_1items
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1clear_1items)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1clear_1items_FUNC);
	gtk_list_clear_items((GtkList *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1clear_1items_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1insert_1items
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1insert_1items)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1insert_1items_FUNC);
	gtk_list_insert_items((GtkList *)arg0, (GList *)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1insert_1items_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1item_1new_1with_1label
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1list_1item_1new_1with_1label)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1item_1new_1with_1label_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_list_item_new_with_label((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1item_1new_1with_1label_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1list_1remove_1items
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1remove_1items)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1remove_1items_FUNC);
	gtk_list_remove_items((GtkList *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1remove_1items_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1select_1item
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1select_1item)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1select_1item_FUNC);
	gtk_list_select_item((GtkList *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1select_1item_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1store_1append
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1append_FUNC);
	gtk_list_store_append((GtkListStore *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1append_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1store_1clear
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1clear)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1clear_FUNC);
	gtk_list_store_clear((GtkListStore *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1clear_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1store_1insert
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1insert)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1insert_FUNC);
	gtk_list_store_insert((GtkListStore *)arg0, (GtkTreeIter *)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1insert_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1store_1newv
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1list_1store_1newv)
	(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1newv_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_list_store_newv((gint)arg0, (GType *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1newv_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1list_1store_1remove
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1remove)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1remove_FUNC);
	gtk_list_store_remove((GtkListStore *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1remove_FUNC);
}
#endif

#if (!defined(NO__1gtk_1list_1store_1set__IIIII) && !defined(JNI64)) || (!defined(NO__1gtk_1list_1store_1set__JJIIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__IIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__JJIIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__IIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__JJIIJ_FUNC);
#endif
	gtk_list_store_set((GtkListStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__IIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__JJIIJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1list_1store_1set__IIIJI) && !defined(JNI64)) || (!defined(NO__1gtk_1list_1store_1set__JJIJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__IIIJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__JJIJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__IIIJI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__JJIJJ_FUNC);
#endif
	gtk_list_store_set((GtkListStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__IIIJI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__JJIJJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1list_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I) && !defined(JNI64)) || (!defined(NO__1gtk_1list_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4)
#endif
{
	GdkColor _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
	if (arg3) if ((lparg3 = getGdkColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	gtk_list_store_set((GtkListStore *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1list_1store_1set__IIIZI) && !defined(JNI64)) || (!defined(NO__1gtk_1list_1store_1set__JJIZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__IIIZI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jboolean arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__JJIZJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jboolean arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__IIIZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__JJIZJ_FUNC);
#endif
	gtk_list_store_set((GtkListStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__IIIZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__JJIZJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1list_1store_1set__III_3BI) && !defined(JNI64)) || (!defined(NO__1gtk_1list_1store_1set__JJI_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__III_3BI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1store_1set__JJI_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintLong arg4)
#endif
{
	jbyte *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__III_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1store_1set__JJI_3BJ_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_list_store_set((GtkListStore *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__III_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1store_1set__JJI_3BJ_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1list_1unselect_1all
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1unselect_1all)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1unselect_1all_FUNC);
	gtk_list_unselect_all((GtkList *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1unselect_1all_FUNC);
}
#endif

#ifndef NO__1gtk_1list_1unselect_1item
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1list_1unselect_1item)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1list_1unselect_1item_FUNC);
	gtk_list_unselect_item((GtkList *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1list_1unselect_1item_FUNC);
}
#endif

#ifndef NO__1gtk_1main
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1main)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1main_FUNC);
	gtk_main();
	OS_NATIVE_EXIT(env, that, _1gtk_1main_FUNC);
}
#endif

#ifndef NO__1gtk_1main_1do_1event
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1main_1do_1event)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1main_1do_1event_FUNC);
	gtk_main_do_event((GdkEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1main_1do_1event_FUNC);
}
#endif

#ifndef NO__1gtk_1main_1iteration
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1main_1iteration)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1main_1iteration_FUNC);
	rc = (jint)gtk_main_iteration();
	OS_NATIVE_EXIT(env, that, _1gtk_1main_1iteration_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1major_1version
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1major_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1major_1version_FUNC);
	rc = (jint)gtk_major_version;
	OS_NATIVE_EXIT(env, that, _1gtk_1major_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1menu_1bar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1menu_1bar_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1bar_1new_FUNC);
	rc = (jintLong)gtk_menu_bar_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1bar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1menu_1get_1attach_1widget
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1menu_1get_1attach_1widget)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1get_1attach_1widget_FUNC);
	rc = (jintLong)gtk_menu_get_attach_widget((GtkMenu *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1get_1attach_1widget_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1menu_1item_1get_1submenu
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1menu_1item_1get_1submenu)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1item_1get_1submenu_FUNC);
	rc = (jintLong)gtk_menu_item_get_submenu((GtkMenuItem *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1item_1get_1submenu_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1menu_1item_1remove_1submenu
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1item_1remove_1submenu)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1item_1remove_1submenu_FUNC);
	gtk_menu_item_remove_submenu((GtkMenuItem *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1item_1remove_1submenu_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1item_1set_1submenu
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1item_1set_1submenu)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1item_1set_1submenu_FUNC);
	gtk_menu_item_set_submenu((GtkMenuItem *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1item_1set_1submenu_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1menu_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1new_FUNC);
	rc = (jintLong)gtk_menu_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1menu_1popdown
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1popdown)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1popdown_FUNC);
	gtk_menu_popdown((GtkMenu *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1popdown_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1popup
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1popup)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1popup_FUNC);
	gtk_menu_popup((GtkMenu *)arg0, (GtkWidget *)arg1, (GtkWidget *)arg2, (GtkMenuPositionFunc)arg3, (gpointer)arg4, (guint)arg5, (guint32)arg6);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1popup_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1shell_1deactivate
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1shell_1deactivate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1shell_1deactivate_FUNC);
	gtk_menu_shell_deactivate((GtkMenuShell *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1shell_1deactivate_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1shell_1insert
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1shell_1insert)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1shell_1insert_FUNC);
	gtk_menu_shell_insert((GtkMenuShell *)arg0, (GtkWidget *)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1shell_1insert_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1shell_1select_1item
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1shell_1select_1item)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1shell_1select_1item_FUNC);
	gtk_menu_shell_select_item((GtkMenuShell *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1shell_1select_1item_FUNC);
}
#endif

#ifndef NO__1gtk_1menu_1shell_1set_1take_1focus
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1menu_1shell_1set_1take_1focus)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1menu_1shell_1set_1take_1focus_FUNC);
/*
	gtk_menu_shell_set_take_focus((GtkMenuShell *)arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_menu_shell_set_take_focus)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkMenuShell *, gboolean))fp)((GtkMenuShell *)arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1menu_1shell_1set_1take_1focus_FUNC);
}
#endif

#ifndef NO__1gtk_1message_1dialog_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1message_1dialog_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1message_1dialog_1new_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_message_dialog_new((GtkWindow *)arg0, (GtkDialogFlags)arg1, (GtkMessageType)arg2, (GtkButtonsType)arg3, (const gchar *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1message_1dialog_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1micro_1version
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1micro_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1micro_1version_FUNC);
	rc = (jint)gtk_micro_version;
	OS_NATIVE_EXIT(env, that, _1gtk_1micro_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1minor_1version
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1minor_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1minor_1version_FUNC);
	rc = (jint)gtk_minor_version;
	OS_NATIVE_EXIT(env, that, _1gtk_1minor_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1misc_1set_1alignment
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1misc_1set_1alignment)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1misc_1set_1alignment_FUNC);
	gtk_misc_set_alignment((GtkMisc *)arg0, (gfloat)arg1, (gfloat)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1misc_1set_1alignment_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1get_1current_1page
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1notebook_1get_1current_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1get_1current_1page_FUNC);
	rc = (jint)gtk_notebook_get_current_page((GtkNotebook *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1get_1current_1page_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1notebook_1get_1scrollable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1notebook_1get_1scrollable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1get_1scrollable_FUNC);
	rc = (jboolean)gtk_notebook_get_scrollable((GtkNotebook *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1get_1scrollable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1notebook_1insert_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1insert_1page)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1insert_1page_FUNC);
	gtk_notebook_insert_page((GtkNotebook *)arg0, (GtkWidget *)arg1, (GtkWidget *)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1insert_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1notebook_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1new_FUNC);
	rc = (jintLong)gtk_notebook_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1notebook_1next_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1next_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1next_1page_FUNC);
	gtk_notebook_next_page((GtkNotebook *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1next_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1prev_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1prev_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1prev_1page_FUNC);
	gtk_notebook_prev_page((GtkNotebook *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1prev_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1remove_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1remove_1page)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1remove_1page_FUNC);
	gtk_notebook_remove_page((GtkNotebook *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1remove_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1set_1current_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1set_1current_1page)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1set_1current_1page_FUNC);
	gtk_notebook_set_current_page((GtkNotebook *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1set_1current_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1set_1scrollable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1set_1scrollable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1set_1scrollable_FUNC);
	gtk_notebook_set_scrollable((GtkNotebook *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1set_1scrollable_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1set_1show_1tabs
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1set_1show_1tabs)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1set_1show_1tabs_FUNC);
	gtk_notebook_set_show_tabs((GtkNotebook *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1set_1show_1tabs_FUNC);
}
#endif

#ifndef NO__1gtk_1notebook_1set_1tab_1pos
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1notebook_1set_1tab_1pos)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1notebook_1set_1tab_1pos_FUNC);
	gtk_notebook_set_tab_pos((GtkNotebook *)arg0, (GtkPositionType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1notebook_1set_1tab_1pos_FUNC);
}
#endif

#ifndef NO__1gtk_1object_1sink
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1object_1sink)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1object_1sink_FUNC);
	gtk_object_sink((GtkObject *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1object_1sink_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1bottom_1margin
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1bottom_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1bottom_1margin_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_bottom_margin(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_bottom_margin)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1bottom_1margin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1left_1margin
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1left_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1left_1margin_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_left_margin(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_left_margin)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1left_1margin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1orientation
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1orientation_FUNC);
/*
	rc = (jint)gtk_page_setup_get_orientation(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_orientation)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1orientation_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1page_1height
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1page_1height)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1page_1height_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_page_height(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_page_height)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1page_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1page_1width
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1page_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1page_1width_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_page_width(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_page_width)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1page_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1paper_1height
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1paper_1height)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1paper_1height_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_paper_height(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_paper_height)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1paper_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1paper_1size
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1paper_1size)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1paper_1size_FUNC);
/*
	rc = (jintLong)gtk_page_setup_get_paper_size(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_paper_size)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1paper_1size_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1paper_1width
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1paper_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1paper_1width_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_paper_width(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_paper_width)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1paper_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1right_1margin
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1right_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1right_1margin_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_right_margin(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_right_margin)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1right_1margin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1get_1top_1margin
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1page_1setup_1get_1top_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1get_1top_1margin_FUNC);
/*
	rc = (jdouble)gtk_page_setup_get_top_margin(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_get_top_margin)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1get_1top_1margin_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1page_1setup_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1new_FUNC);
/*
	rc = (jintLong)gtk_page_setup_new();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1bottom_1margin
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1bottom_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1bottom_1margin_FUNC);
/*
	gtk_page_setup_set_bottom_margin(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_bottom_margin)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble, jint))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1bottom_1margin_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1left_1margin
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1left_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1left_1margin_FUNC);
/*
	gtk_page_setup_set_left_margin(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_left_margin)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble, jint))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1left_1margin_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1orientation
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1orientation_FUNC);
/*
	gtk_page_setup_set_orientation(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_orientation)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1orientation_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1paper_1size
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1paper_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1paper_1size_FUNC);
/*
	gtk_page_setup_set_paper_size(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_paper_size)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1paper_1size_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1right_1margin
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1right_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1right_1margin_FUNC);
/*
	gtk_page_setup_set_right_margin(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_right_margin)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble, jint))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1right_1margin_FUNC);
}
#endif

#ifndef NO__1gtk_1page_1setup_1set_1top_1margin
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1page_1setup_1set_1top_1margin)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1page_1setup_1set_1top_1margin_FUNC);
/*
	gtk_page_setup_set_top_margin(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_page_setup_set_top_margin)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble, jint))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1page_1setup_1set_1top_1margin_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1arrow
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1arrow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jboolean arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1arrow_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_arrow((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10, arg11, arg12);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1arrow_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1box
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1box)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1box_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_box((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1box_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1box_1gap
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1box_1gap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1box_1gap_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_box_gap((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (gchar *)lparg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1box_1gap_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1check
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1check)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1check_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_check((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1check_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1expander
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1expander)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4, jbyteArray arg5, jint arg6, jint arg7, jint arg8)
{
	GdkRectangle _arg3, *lparg3=NULL;
	jbyte *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1expander_FUNC);
	if (arg3) if ((lparg3 = getGdkRectangleFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_paint_expander((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, lparg3, (GtkWidget *)arg4, (const gchar *)lparg5, arg6, arg7, arg8);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) setGdkRectangleFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1expander_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1extension
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1extension)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1extension_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_extension((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (gchar *)lparg6, arg7, arg8, arg9, arg10, arg11);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1extension_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1flat_1box
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1flat_1box)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1flat_1box_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_flat_box((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1flat_1box_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1focus
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1focus)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4, jbyteArray arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	GdkRectangle _arg3, *lparg3=NULL;
	jbyte *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1focus_FUNC);
	if (arg3) if ((lparg3 = getGdkRectangleFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_paint_focus((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, lparg3, (GtkWidget *)arg4, (const gchar *)lparg5, arg6, arg7, arg8, arg9);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1focus_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1handle
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1handle)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1handle_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_handle((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10, arg11);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1handle_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1hline
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1hline)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4, jbyteArray arg5, jint arg6, jint arg7, jint arg8)
{
	GdkRectangle _arg3, *lparg3=NULL;
	jbyte *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1hline_FUNC);
	if (arg3) if ((lparg3 = getGdkRectangleFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_paint_hline((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, lparg3, (GtkWidget *)arg4, (const gchar *)lparg5, arg6, arg7, arg8);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) setGdkRectangleFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1hline_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1layout
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1layout)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jboolean arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jintLong arg9)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1layout_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_layout((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, (PangoLayout *)arg9);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1layout_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1option
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1option)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1option_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_option((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1option_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1shadow
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1shadow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1shadow_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_shadow((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1shadow_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1shadow_1gap
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1shadow_1gap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1shadow_1gap_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_shadow_gap((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (gchar *)lparg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1shadow_1gap_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1slider
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1slider)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1slider_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_slider((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10, arg11);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1slider_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1tab
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1tab)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jbyteArray arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	GdkRectangle _arg4, *lparg4=NULL;
	jbyte *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1tab_FUNC);
	if (arg4) if ((lparg4 = getGdkRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	gtk_paint_tab((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, arg3, lparg4, (GtkWidget *)arg5, (const gchar *)lparg6, arg7, arg8, arg9, arg10);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGdkRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1tab_FUNC);
}
#endif

#ifndef NO__1gtk_1paint_1vline
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paint_1vline)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4, jbyteArray arg5, jint arg6, jint arg7, jint arg8)
{
	GdkRectangle _arg3, *lparg3=NULL;
	jbyte *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1paint_1vline_FUNC);
	if (arg3) if ((lparg3 = getGdkRectangleFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_paint_vline((GtkStyle *)arg0, (GdkWindow *)arg1, arg2, lparg3, (GtkWidget *)arg4, (const gchar *)lparg5, arg6, arg7, arg8);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) setGdkRectangleFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1paint_1vline_FUNC);
}
#endif

#ifndef NO__1gtk_1paper_1size_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1paper_1size_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1free_FUNC);
/*
	gtk_paper_size_free(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_free)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1paper_1size_1get_1display_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1get_1display_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1get_1display_1name_FUNC);
/*
	rc = (jintLong)gtk_paper_size_get_display_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_get_display_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1get_1display_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1get_1height
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1paper_1size_1get_1height)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1get_1height_FUNC);
/*
	rc = (jdouble)gtk_paper_size_get_height(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_get_height)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1get_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1get_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1get_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1get_1name_FUNC);
/*
	rc = (jintLong)gtk_paper_size_get_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_get_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1get_1ppd_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1get_1ppd_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1get_1ppd_1name_FUNC);
/*
	rc = (jintLong)gtk_paper_size_get_ppd_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_get_ppd_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1get_1ppd_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1get_1width
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1paper_1size_1get_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1get_1width_FUNC);
/*
	rc = (jdouble)gtk_paper_size_get_width(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_get_width)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1get_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1is_1custom
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1paper_1size_1is_1custom)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1is_1custom_FUNC);
/*
	rc = (jboolean)gtk_paper_size_is_custom(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_is_custom)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1is_1custom_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_paper_size_new(lparg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *))fp)(lparg0);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1new_1custom
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1new_1custom)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jdouble arg2, jdouble arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1new_1custom_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_paper_size_new_custom(lparg0, lparg1, arg2, arg3, arg4);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_new_custom)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *, jbyte *, jdouble, jdouble, jint))fp)(lparg0, lparg1, arg2, arg3, arg4);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1new_1custom_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1paper_1size_1new_1from_1ppd
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1paper_1size_1new_1from_1ppd)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jdouble arg2, jdouble arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1paper_1size_1new_1from_1ppd_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_paper_size_new_from_ppd(lparg0, lparg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_paper_size_new_from_ppd)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jbyte *, jbyte *, jdouble, jdouble))fp)(lparg0, lparg1, arg2, arg3);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1paper_1size_1new_1from_1ppd_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1plug_1get_1id
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1plug_1get_1id)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1plug_1get_1id_FUNC);
	rc = (jintLong)gtk_plug_get_id((GtkPlug *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1plug_1get_1id_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1plug_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1plug_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1plug_1new_FUNC);
	rc = (jintLong)gtk_plug_new(arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1plug_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1get_1printer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1job_1get_1printer)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1get_1printer_FUNC);
/*
	rc = (jintLong)gtk_print_job_get_printer(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_get_printer)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1get_1printer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1get_1settings
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1job_1get_1settings)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1get_1settings_FUNC);
/*
	rc = (jintLong)gtk_print_job_get_settings(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_get_settings)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1get_1settings_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1get_1status
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1job_1get_1status)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1get_1status_FUNC);
/*
	rc = (jint)gtk_print_job_get_status(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_get_status)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1get_1status_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1get_1surface
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1job_1get_1surface)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1get_1surface_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_print_job_get_surface(arg0, (GError **)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_get_surface)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, GError **))fp)(arg0, (GError **)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1get_1surface_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1get_1title
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1job_1get_1title)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1get_1title_FUNC);
/*
	rc = (jintLong)gtk_print_job_get_title(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_get_title)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1get_1title_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1job_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_print_job_new((const gchar *)lparg0, arg1, arg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(const gchar *, jintLong, jintLong, jintLong))fp)((const gchar *)lparg0, arg1, arg2, arg3);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1job_1send
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1job_1send)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1send_FUNC);
/*
	gtk_print_job_send(arg0, arg1, (gpointer)arg2, (GDestroyNotify)arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_send)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, gpointer, GDestroyNotify))fp)(arg0, arg1, (gpointer)arg2, (GDestroyNotify)arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1send_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1job_1set_1source_1file
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1print_1job_1set_1source_1file)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLongArray arg2)
{
	jbyte *lparg1=NULL;
	jintLong *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1job_1set_1source_1file_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)gtk_print_job_set_source_file(arg0, (const gchar *)lparg1, (GError **)lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_job_set_source_file)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, const gchar *, GError **))fp)(arg0, (const gchar *)lparg1, (GError **)lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1job_1set_1source_1file_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1foreach
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1foreach)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1foreach_FUNC);
/*
	gtk_print_settings_foreach(arg0, arg1, (gpointer)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_foreach)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, gpointer))fp)(arg0, arg1, (gpointer)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1foreach_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1settings_1get)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_print_settings_get(arg0, (const gchar *)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, const gchar *))fp)(arg0, (const gchar *)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1collate
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1collate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1collate_FUNC);
/*
	rc = (jboolean)gtk_print_settings_get_collate(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_collate)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1collate_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1duplex
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1duplex)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1duplex_FUNC);
/*
	rc = (jint)gtk_print_settings_get_duplex(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_duplex)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1duplex_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1n_1copies
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1n_1copies)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1n_1copies_FUNC);
/*
	rc = (jint)gtk_print_settings_get_n_copies(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_n_copies)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1n_1copies_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1orientation
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1orientation_FUNC);
/*
	rc = (jint)gtk_print_settings_get_orientation(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_orientation)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1orientation_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1page_1ranges
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1page_1ranges)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1page_1ranges_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_print_settings_get_page_ranges(arg0, (gint *)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_page_ranges)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, gint *))fp)(arg0, (gint *)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1page_1ranges_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1paper_1height
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1paper_1height)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1paper_1height_FUNC);
/*
	rc = (jdouble)gtk_print_settings_get_paper_height(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_paper_height)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1paper_1height_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1paper_1width
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1paper_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1paper_1width_FUNC);
/*
	rc = (jdouble)gtk_print_settings_get_paper_width(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_paper_width)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1paper_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1print_1pages
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1print_1pages)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1print_1pages_FUNC);
/*
	rc = (jint)gtk_print_settings_get_print_pages(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_print_pages)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1print_1pages_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1printer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1printer)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1printer_FUNC);
/*
	rc = (jintLong)gtk_print_settings_get_printer(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_printer)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1printer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1get_1resolution
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1settings_1get_1resolution)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1get_1resolution_FUNC);
/*
	rc = (jint)gtk_print_settings_get_resolution(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_get_resolution)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1get_1resolution_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1settings_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1new_FUNC);
/*
	rc = (jintLong)gtk_print_settings_new();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1settings_1set
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	gtk_print_settings_set(arg0, (const gchar *)lparg1, (const gchar *)lparg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, const gchar *, const gchar *))fp)(arg0, (const gchar *)lparg1, (const gchar *)lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1collate
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1collate)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1collate_FUNC);
/*
	gtk_print_settings_set_collate(arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_collate)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, gboolean))fp)(arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1collate_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1duplex
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1duplex)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1duplex_FUNC);
/*
	gtk_print_settings_set_duplex(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_duplex)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1duplex_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1n_1copies
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1n_1copies)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1n_1copies_FUNC);
/*
	gtk_print_settings_set_n_copies(arg0, (gint)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_n_copies)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, gint))fp)(arg0, (gint)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1n_1copies_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1orientation
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1orientation_FUNC);
/*
	gtk_print_settings_set_orientation(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_orientation)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1orientation_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1page_1ranges
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1page_1ranges)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1page_1ranges_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_print_settings_set_page_ranges(arg0, lparg1, (gint)arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_page_ranges)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint *, gint))fp)(arg0, lparg1, (gint)arg2);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1page_1ranges_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1print_1pages
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1print_1pages)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1print_1pages_FUNC);
/*
	gtk_print_settings_set_print_pages(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_print_pages)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1print_1pages_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1settings_1set_1printer
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1settings_1set_1printer)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1settings_1set_1printer_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_print_settings_set_printer(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_settings_set_printer)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1settings_1set_1printer_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1get_1current_1page
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1get_1current_1page)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1get_1current_1page_FUNC);
/*
	rc = (jint)gtk_print_unix_dialog_get_current_page(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_get_current_page)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1get_1current_1page_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1get_1page_1setup
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1get_1page_1setup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1get_1page_1setup_FUNC);
/*
	rc = (jintLong)gtk_print_unix_dialog_get_page_setup(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_get_page_setup)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1get_1page_1setup_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1get_1selected_1printer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1get_1selected_1printer)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1get_1selected_1printer_FUNC);
/*
	rc = (jintLong)gtk_print_unix_dialog_get_selected_printer(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_get_selected_printer)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1get_1selected_1printer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1get_1settings
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1get_1settings)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1get_1settings_FUNC);
/*
	rc = (jintLong)gtk_print_unix_dialog_get_settings(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_get_settings)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1get_1settings_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_print_unix_dialog_new((const gchar *)lparg0, (GtkWindow *)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(const gchar *, GtkWindow *))fp)((const gchar *)lparg0, (GtkWindow *)arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1set_1current_1page
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1set_1current_1page)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1set_1current_1page_FUNC);
/*
	gtk_print_unix_dialog_set_current_page(arg0, (gint)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_set_current_page)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, gint))fp)(arg0, (gint)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1set_1current_1page_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1set_1embed_1page_1setup
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1set_1embed_1page_1setup)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1set_1embed_1page_1setup_FUNC);
/*
	gtk_print_unix_dialog_set_embed_page_setup(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_set_embed_page_setup)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1set_1embed_1page_1setup_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1set_1manual_1capabilities
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1set_1manual_1capabilities)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1set_1manual_1capabilities_FUNC);
/*
	gtk_print_unix_dialog_set_manual_capabilities(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_set_manual_capabilities)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1set_1manual_1capabilities_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1set_1page_1setup
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1set_1page_1setup)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1set_1page_1setup_FUNC);
/*
	gtk_print_unix_dialog_set_page_setup(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_set_page_setup)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1set_1page_1setup_FUNC);
}
#endif

#ifndef NO__1gtk_1print_1unix_1dialog_1set_1settings
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1print_1unix_1dialog_1set_1settings)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1print_1unix_1dialog_1set_1settings_FUNC);
/*
	gtk_print_unix_dialog_set_settings(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_print_unix_dialog_set_settings)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1print_1unix_1dialog_1set_1settings_FUNC);
}
#endif

#ifndef NO__1gtk_1printer_1get_1backend
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1printer_1get_1backend)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1printer_1get_1backend_FUNC);
/*
	rc = (jintLong)gtk_printer_get_backend(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_printer_get_backend)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1printer_1get_1backend_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1printer_1get_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1printer_1get_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1printer_1get_1name_FUNC);
/*
	rc = (jintLong)gtk_printer_get_name(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_printer_get_name)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1printer_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1printer_1is_1default
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1printer_1is_1default)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1printer_1is_1default_FUNC);
/*
	rc = (jboolean)gtk_printer_is_default(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_printer_is_default)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1printer_1is_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1progress_1bar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1progress_1bar_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1progress_1bar_1new_FUNC);
	rc = (jintLong)gtk_progress_bar_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1progress_1bar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1progress_1bar_1pulse
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1progress_1bar_1pulse)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1progress_1bar_1pulse_FUNC);
	gtk_progress_bar_pulse((GtkProgressBar *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1progress_1bar_1pulse_FUNC);
}
#endif

#ifndef NO__1gtk_1progress_1bar_1set_1fraction
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1progress_1bar_1set_1fraction)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1progress_1bar_1set_1fraction_FUNC);
	gtk_progress_bar_set_fraction((GtkProgressBar *)arg0, (gdouble)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1progress_1bar_1set_1fraction_FUNC);
}
#endif

#ifndef NO__1gtk_1progress_1bar_1set_1orientation
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1progress_1bar_1set_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1progress_1bar_1set_1orientation_FUNC);
	gtk_progress_bar_set_orientation((GtkProgressBar *)arg0, (GtkProgressBarOrientation)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1progress_1bar_1set_1orientation_FUNC);
}
#endif

#ifndef NO__1gtk_1radio_1button_1get_1group
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1radio_1button_1get_1group)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1radio_1button_1get_1group_FUNC);
	rc = (jintLong)gtk_radio_button_get_group((GtkRadioButton *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1radio_1button_1get_1group_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1radio_1button_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1radio_1button_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1radio_1button_1new_FUNC);
	rc = (jintLong)gtk_radio_button_new((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1radio_1button_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1radio_1menu_1item_1get_1group
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1radio_1menu_1item_1get_1group)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1radio_1menu_1item_1get_1group_FUNC);
	rc = (jintLong)gtk_radio_menu_item_get_group((GtkRadioMenuItem *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1radio_1menu_1item_1get_1group_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1radio_1menu_1item_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1radio_1menu_1item_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1radio_1menu_1item_1new_FUNC);
	rc = (jintLong)gtk_radio_menu_item_new((GSList *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1radio_1menu_1item_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1radio_1menu_1item_1new_1with_1label
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1radio_1menu_1item_1new_1with_1label)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1radio_1menu_1item_1new_1with_1label_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_radio_menu_item_new_with_label((GSList *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1radio_1menu_1item_1new_1with_1label_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1range_1get_1adjustment
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1range_1get_1adjustment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1range_1get_1adjustment_FUNC);
	rc = (jintLong)gtk_range_get_adjustment((GtkRange *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1range_1get_1adjustment_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1range_1set_1increments
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1range_1set_1increments)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1range_1set_1increments_FUNC);
	gtk_range_set_increments((GtkRange *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1range_1set_1increments_FUNC);
}
#endif

#ifndef NO__1gtk_1range_1set_1inverted
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1range_1set_1inverted)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1range_1set_1inverted_FUNC);
	gtk_range_set_inverted((GtkRange *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1range_1set_1inverted_FUNC);
}
#endif

#ifndef NO__1gtk_1range_1set_1range
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1range_1set_1range)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1range_1set_1range_FUNC);
	gtk_range_set_range((GtkRange *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1range_1set_1range_FUNC);
}
#endif

#ifndef NO__1gtk_1range_1set_1value
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1range_1set_1value)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1range_1set_1value_FUNC);
	gtk_range_set_value((GtkRange *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1range_1set_1value_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1parse_1string
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1parse_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1parse_1string_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	gtk_rc_parse_string((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1parse_1string_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1style_1get_1bg_1pixmap_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1rc_1style_1get_1bg_1pixmap_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1get_1bg_1pixmap_1name_FUNC);
	rc = (jintLong)gtk_rc_style_get_bg_pixmap_name((GtkRcStyle *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1get_1bg_1pixmap_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1rc_1style_1get_1color_1flags
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1rc_1style_1get_1color_1flags)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1get_1color_1flags_FUNC);
	rc = (jint)gtk_rc_style_get_color_flags((GtkRcStyle *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1get_1color_1flags_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1rc_1style_1set_1bg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1style_1set_1bg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1set_1bg_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_rc_style_set_bg((GtkRcStyle *)arg0, arg1, lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1set_1bg_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1style_1set_1bg_1pixmap_1name
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1style_1set_1bg_1pixmap_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1set_1bg_1pixmap_1name_FUNC);
	gtk_rc_style_set_bg_pixmap_name((GtkRcStyle *)arg0, arg1, (char *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1set_1bg_1pixmap_1name_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1style_1set_1color_1flags
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1style_1set_1color_1flags)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1set_1color_1flags_FUNC);
	gtk_rc_style_set_color_flags((GtkRcStyle *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1set_1color_1flags_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1style_1set_1fg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1style_1set_1fg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1set_1fg_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_rc_style_set_fg((GtkRcStyle *)arg0, arg1, lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1set_1fg_FUNC);
}
#endif

#ifndef NO__1gtk_1rc_1style_1set_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1rc_1style_1set_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1rc_1style_1set_1text_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_rc_style_set_text((GtkRcStyle *)arg0, arg1, lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1rc_1style_1set_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1scale_1set_1digits
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scale_1set_1digits)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scale_1set_1digits_FUNC);
	gtk_scale_set_digits((GtkScale *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scale_1set_1digits_FUNC);
}
#endif

#ifndef NO__1gtk_1scale_1set_1draw_1value
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scale_1set_1draw_1value)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scale_1set_1draw_1value_FUNC);
	gtk_scale_set_draw_value((GtkScale *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scale_1set_1draw_1value_FUNC);
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1add_1with_1viewport
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1add_1with_1viewport)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1add_1with_1viewport_FUNC);
	gtk_scrolled_window_add_with_viewport((GtkScrolledWindow *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1add_1with_1viewport_FUNC);
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1get_1hadjustment
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1get_1hadjustment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1get_1hadjustment_FUNC);
	rc = (jintLong)gtk_scrolled_window_get_hadjustment((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1get_1hadjustment_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1get_1policy
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1get_1policy)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1get_1policy_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_scrolled_window_get_policy((GtkScrolledWindow *)arg0, (GtkPolicyType *)lparg1, (GtkPolicyType *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1get_1policy_FUNC);
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1get_1shadow_1type
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1get_1shadow_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1get_1shadow_1type_FUNC);
	rc = (jint)gtk_scrolled_window_get_shadow_type((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1get_1shadow_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1get_1vadjustment
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1get_1vadjustment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1get_1vadjustment_FUNC);
	rc = (jintLong)gtk_scrolled_window_get_vadjustment((GtkScrolledWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1get_1vadjustment_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1new_FUNC);
	rc = (jintLong)gtk_scrolled_window_new((GtkAdjustment *)arg0, (GtkAdjustment *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1set_1placement
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1set_1placement)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1set_1placement_FUNC);
	gtk_scrolled_window_set_placement((GtkScrolledWindow *)arg0, (GtkCornerType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1set_1placement_FUNC);
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1set_1policy
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1set_1policy)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1set_1policy_FUNC);
	gtk_scrolled_window_set_policy((GtkScrolledWindow *)arg0, (GtkPolicyType)arg1, (GtkPolicyType)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1set_1policy_FUNC);
}
#endif

#ifndef NO__1gtk_1scrolled_1window_1set_1shadow_1type
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1scrolled_1window_1set_1shadow_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1scrolled_1window_1set_1shadow_1type_FUNC);
	gtk_scrolled_window_set_shadow_type((GtkScrolledWindow *)arg0, (GtkShadowType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1scrolled_1window_1set_1shadow_1type_FUNC);
}
#endif

#ifndef NO__1gtk_1selection_1data_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1selection_1data_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1selection_1data_1free_FUNC);
	gtk_selection_data_free((GtkSelectionData *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1selection_1data_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1selection_1data_1set
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1selection_1data_1set)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1selection_1data_1set_FUNC);
	gtk_selection_data_set((GtkSelectionData *)arg0, (GdkAtom)arg1, (gint)arg2, (const guchar *)arg3, (gint)arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1selection_1data_1set_FUNC);
}
#endif

#ifndef NO__1gtk_1separator_1menu_1item_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1separator_1menu_1item_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1separator_1menu_1item_1new_FUNC);
	rc = (jintLong)gtk_separator_menu_item_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1separator_1menu_1item_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1set_1locale
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1set_1locale)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1set_1locale_FUNC);
	rc = (jintLong)gtk_set_locale();
	OS_NATIVE_EXIT(env, that, _1gtk_1set_1locale_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1settings_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1settings_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1settings_1get_1default_FUNC);
	rc = (jintLong)gtk_settings_get_default();
	OS_NATIVE_EXIT(env, that, _1gtk_1settings_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1socket_1get_1id
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1socket_1get_1id)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1socket_1get_1id_FUNC);
	rc = (jintLong)gtk_socket_get_id((GtkSocket *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1socket_1get_1id_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1socket_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1socket_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1socket_1new_FUNC);
	rc = (jintLong)gtk_socket_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1socket_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1spin_1button_1configure
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1configure)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1configure_FUNC);
	gtk_spin_button_configure((GtkSpinButton*)arg0, (GtkAdjustment *)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1configure_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1get_1adjustment
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1spin_1button_1get_1adjustment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1get_1adjustment_FUNC);
	rc = (jintLong)gtk_spin_button_get_adjustment((GtkSpinButton*)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1get_1adjustment_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1spin_1button_1get_1digits
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1spin_1button_1get_1digits)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1get_1digits_FUNC);
	rc = (jint)gtk_spin_button_get_digits((GtkSpinButton*)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1get_1digits_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1spin_1button_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1spin_1button_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1new_FUNC);
	rc = (jintLong)gtk_spin_button_new((GtkAdjustment *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1spin_1button_1set_1digits
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1set_1digits)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1set_1digits_FUNC);
	gtk_spin_button_set_digits((GtkSpinButton*)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1set_1digits_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1set_1increments
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1set_1increments)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1set_1increments_FUNC);
	gtk_spin_button_set_increments((GtkSpinButton*)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1set_1increments_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1set_1range
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1set_1range)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1, jdouble arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1set_1range_FUNC);
	gtk_spin_button_set_range((GtkSpinButton*)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1set_1range_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1set_1value
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1set_1value)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1set_1value_FUNC);
	gtk_spin_button_set_value((GtkSpinButton*)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1set_1value_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1set_1wrap
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1set_1wrap)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1set_1wrap_FUNC);
	gtk_spin_button_set_wrap((GtkSpinButton*)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1set_1wrap_FUNC);
}
#endif

#ifndef NO__1gtk_1spin_1button_1update
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1spin_1button_1update)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1spin_1button_1update_FUNC);
	gtk_spin_button_update((GtkSpinButton*)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1spin_1button_1update_FUNC);
}
#endif

#ifndef NO__1gtk_1status_1icon_1get_1geometry
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1status_1icon_1get_1geometry)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3)
{
	GdkRectangle _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1get_1geometry_FUNC);
	if (arg2) if ((lparg2 = getGdkRectangleFields(env, arg2, &_arg2)) == NULL) goto fail;
/*
	rc = (jboolean)gtk_status_icon_get_geometry(arg0, arg1, lparg2, arg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_get_geometry)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jintLong, GdkRectangle *, jintLong))fp)(arg0, arg1, lparg2, arg3);
		}
	}
fail:
	if (arg2 && lparg2) setGdkRectangleFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1get_1geometry_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1status_1icon_1get_1visible
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1status_1icon_1get_1visible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1get_1visible_FUNC);
/*
	rc = (jboolean)gtk_status_icon_get_visible(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_get_visible)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1get_1visible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1status_1icon_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1status_1icon_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1new_FUNC);
/*
	rc = (jintLong)gtk_status_icon_new();
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1status_1icon_1position_1menu_1func
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1status_1icon_1position_1menu_1func)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1position_1menu_1func_FUNC);
	rc = (jintLong)gtk_status_icon_position_menu_func();
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1position_1menu_1func_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1status_1icon_1set_1from_1pixbuf
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1status_1icon_1set_1from_1pixbuf)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1set_1from_1pixbuf_FUNC);
/*
	gtk_status_icon_set_from_pixbuf(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_set_from_pixbuf)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1set_1from_1pixbuf_FUNC);
}
#endif

#ifndef NO__1gtk_1status_1icon_1set_1tooltip
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1status_1icon_1set_1tooltip)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1set_1tooltip_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_status_icon_set_tooltip(arg0, lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_set_tooltip)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jbyte *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1set_1tooltip_FUNC);
}
#endif

#ifndef NO__1gtk_1status_1icon_1set_1visible
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1status_1icon_1set_1visible)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1status_1icon_1set_1visible_FUNC);
/*
	gtk_status_icon_set_visible(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_status_icon_set_visible)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1status_1icon_1set_1visible_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1base
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1base)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1base_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_base((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1base_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1bg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1bg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1bg_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_bg((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1bg_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1bg_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1bg_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1bg_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_bg_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1bg_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1black
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1black)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1black_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gtk_style_get_black((GtkStyle *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setGdkColorFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1black_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1black_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1black_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1black_1gc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_style_get_black_gc((GtkStyle *)arg0, (GdkGC **)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1black_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1dark
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1dark)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1dark_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_dark((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1dark_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1dark_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1dark_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1dark_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_dark_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1dark_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1fg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1fg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1fg_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_fg((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1fg_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1fg_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1fg_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1fg_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_fg_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1fg_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1font_1desc
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1style_1get_1font_1desc)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1font_1desc_FUNC);
	rc = (jintLong)gtk_style_get_font_desc((GtkStyle *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1font_1desc_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1style_1get_1light
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1light)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1light_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_light((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1light_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1light_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1light_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1light_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_light_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1light_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1mid_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1mid_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1mid_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_mid_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1mid_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1text_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_style_get_text((GtkStyle *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setGdkColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1text_1aa_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1text_1aa_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1text_1aa_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_text_aa_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1text_1aa_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1text_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1text_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1text_1gc_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_style_get_text_gc((GtkStyle *)arg0, arg1, (GdkGC **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1text_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1white_1gc
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1style_1get_1white_1gc)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1white_1gc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_style_get_white_gc((GtkStyle *)arg0, (GdkGC **)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1white_1gc_FUNC);
}
#endif

#ifndef NO__1gtk_1style_1get_1xthickness
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1style_1get_1xthickness)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1xthickness_FUNC);
	rc = (jint)gtk_style_get_xthickness((GtkStyle *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1xthickness_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1style_1get_1ythickness
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1style_1get_1ythickness)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1get_1ythickness_FUNC);
	rc = (jint)gtk_style_get_ythickness((GtkStyle *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1get_1ythickness_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1style_1render_1icon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1style_1render_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1style_1render_1icon_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_style_render_icon((GtkStyle *)arg0, (GtkIconSource *)arg1, arg2, arg3, arg4, (GtkWidget *)arg5, (const gchar *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1style_1render_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1target_1list_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1target_1list_1new)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1target_1list_1new_FUNC);
	rc = (jintLong)gtk_target_list_new((const GtkTargetEntry *)arg0, (guint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1target_1list_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1target_1list_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1target_1list_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1target_1list_1unref_FUNC);
	gtk_target_list_unref((GtkTargetList *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1target_1list_1unref_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1copy_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1copy_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1copy_1clipboard_FUNC);
	gtk_text_buffer_copy_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1copy_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1cut_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1cut_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1cut_1clipboard_FUNC);
	gtk_text_buffer_cut_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1cut_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1delete
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1delete)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1delete_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_delete((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1delete_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1bounds
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1bounds)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1bounds_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_get_bounds((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1bounds_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1char_1count
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1char_1count)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1char_1count_FUNC);
	rc = (jint)gtk_text_buffer_get_char_count((GtkTextBuffer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1char_1count_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1end_1iter
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1end_1iter)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1end_1iter_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_get_end_iter((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1end_1iter_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1insert
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1insert)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1insert_FUNC);
	rc = (jintLong)gtk_text_buffer_get_insert((GtkTextBuffer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1insert_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1iter_1at_1line
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1iter_1at_1line)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1line_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_get_iter_at_line((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (gint)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1line_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1iter_1at_1mark
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1iter_1at_1mark)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1mark_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_get_iter_at_mark((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextMark *)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1mark_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1iter_1at_1offset
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1iter_1at_1offset)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1offset_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_get_iter_at_offset((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (gint)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1iter_1at_1offset_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1line_1count
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1line_1count)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1line_1count_FUNC);
	rc = (jint)gtk_text_buffer_get_line_count((GtkTextBuffer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1line_1count_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1selection_1bound
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1selection_1bound)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1selection_1bound_FUNC);
	rc = (jintLong)gtk_text_buffer_get_selection_bound((GtkTextBuffer *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1selection_1bound_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1selection_1bounds
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1selection_1bounds)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1selection_1bounds_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_text_buffer_get_selection_bounds((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1selection_1bounds_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1buffer_1get_1text
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1buffer_1get_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jboolean arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1get_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_text_buffer_get_text((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (GtkTextIter *)lparg2, (gboolean)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1get_1text_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1gtk_1text_1buffer_1insert__II_3BI) && !defined(JNI64)) || (!defined(NO__1gtk_1text_1buffer_1insert__JJ_3BI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1insert__II_3BI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jint arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1insert__JJ_3BI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jint arg3)
#endif
{
	jbyte *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1insert__II_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1insert__JJ_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_insert((GtkTextBuffer *)arg0, (GtkTextIter *)arg1, (const gchar *)lparg2, (gint)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1insert__II_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1insert__JJ_3BI_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1text_1buffer_1insert__I_3B_3BI) && !defined(JNI64)) || (!defined(NO__1gtk_1text_1buffer_1insert__J_3B_3BI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1insert__I_3B_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1insert__J_3B_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
#endif
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1insert__I_3B_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1insert__J_3B_3BI_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_insert((GtkTextBuffer *)arg0, (GtkTextIter *)lparg1, (const gchar *)lparg2, (gint)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1insert__I_3B_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1insert__J_3B_3BI_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1text_1buffer_1move_1mark
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1move_1mark)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1move_1mark_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_move_mark((GtkTextBuffer *)arg0, (GtkTextMark *)arg1, (const GtkTextIter *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1move_1mark_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1paste_1clipboard
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1paste_1clipboard)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jboolean arg3)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1paste_1clipboard_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_text_buffer_paste_clipboard((GtkTextBuffer *)arg0, (GtkClipboard *)arg1, (GtkTextIter *)lparg2, (gboolean)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1paste_1clipboard_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1place_1cursor
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1place_1cursor)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1place_1cursor_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_place_cursor((GtkTextBuffer *)arg0, (const GtkTextIter *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1place_1cursor_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1buffer_1set_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1buffer_1set_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1buffer_1set_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_buffer_set_text((GtkTextBuffer *)arg0, (const gchar *)lparg1, (gint)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1buffer_1set_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1iter_1get_1line
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1text_1iter_1get_1line)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1iter_1get_1line_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)gtk_text_iter_get_line((const GtkTextIter *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1iter_1get_1line_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1iter_1get_1offset
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1text_1iter_1get_1offset)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1iter_1get_1offset_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)gtk_text_iter_get_offset((const GtkTextIter *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1iter_1get_1offset_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1buffer_1to_1window_1coords
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1buffer_1to_1window_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1buffer_1to_1window_1coords_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_text_view_buffer_to_window_coords((GtkTextView *)arg0, (GtkTextWindowType)arg1, (gint)arg2, (gint)arg3, (gint *)lparg4, (gint *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1buffer_1to_1window_1coords_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1buffer
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1buffer)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1buffer_FUNC);
	rc = (jintLong)gtk_text_view_get_buffer((GtkTextView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1buffer_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1editable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1editable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1editable_FUNC);
	rc = (jboolean)gtk_text_view_get_editable((GtkTextView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1editable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1iter_1at_1location
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1iter_1at_1location)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1iter_1at_1location_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_text_view_get_iter_at_location((GtkTextView *)arg0, (GtkTextIter *)lparg1, (gint)arg2, (gint)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1iter_1at_1location_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1iter_1location
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1iter_1location)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	GdkRectangle _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1iter_1location_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	gtk_text_view_get_iter_location((GtkTextView *)arg0, (const GtkTextIter *)lparg1, (GdkRectangle *)lparg2);
fail:
	if (arg2 && lparg2) setGdkRectangleFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1iter_1location_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1line_1at_1y
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1line_1at_1y)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1line_1at_1y_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_text_view_get_line_at_y((GtkTextView *)arg0, (GtkTextIter *)lparg1, (gint)arg2, (gint *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1line_1at_1y_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1visible_1rect
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1visible_1rect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1visible_1rect_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gtk_text_view_get_visible_rect((GtkTextView *)arg0, (GdkRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setGdkRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1visible_1rect_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1get_1window
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1view_1get_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1get_1window_FUNC);
	rc = (jintLong)gtk_text_view_get_window((GtkTextView *)arg0, (GtkTextWindowType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1get_1window_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1text_1view_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1new_FUNC);
	rc = (jintLong)gtk_text_view_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1scroll_1mark_1onscreen
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1scroll_1mark_1onscreen)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1scroll_1mark_1onscreen_FUNC);
	gtk_text_view_scroll_mark_onscreen((GtkTextView *)arg0, (GtkTextMark *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1scroll_1mark_1onscreen_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1scroll_1to_1iter
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1text_1view_1scroll_1to_1iter)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jdouble arg2, jboolean arg3, jdouble arg4, jdouble arg5)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1scroll_1to_1iter_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_text_view_scroll_to_iter((GtkTextView *)arg0, (GtkTextIter *)lparg1, (gdouble)arg2, (gboolean)arg3, (gdouble)arg4, (gdouble)arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1scroll_1to_1iter_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1text_1view_1set_1editable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1set_1editable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1set_1editable_FUNC);
	gtk_text_view_set_editable((GtkTextView *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1set_1editable_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1set_1justification
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1set_1justification)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1set_1justification_FUNC);
	gtk_text_view_set_justification((GtkTextView *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1set_1justification_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1set_1tabs
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1set_1tabs)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1set_1tabs_FUNC);
	gtk_text_view_set_tabs((GtkTextView *)arg0, (PangoTabArray *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1set_1tabs_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1set_1wrap_1mode
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1set_1wrap_1mode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1set_1wrap_1mode_FUNC);
	gtk_text_view_set_wrap_mode((GtkTextView *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1set_1wrap_1mode_FUNC);
}
#endif

#ifndef NO__1gtk_1text_1view_1window_1to_1buffer_1coords
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1text_1view_1window_1to_1buffer_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1text_1view_1window_1to_1buffer_1coords_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_text_view_window_to_buffer_coords((GtkTextView *)arg0, (GtkTextWindowType)arg1, (gint)arg2, (gint)arg3, (gint *)lparg4, (gint *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1text_1view_1window_1to_1buffer_1coords_FUNC);
}
#endif

#ifndef NO__1gtk_1timeout_1add
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1timeout_1add)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1timeout_1add_FUNC);
	rc = (jint)gtk_timeout_add((guint32)arg0, (GtkFunction)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1timeout_1add_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1timeout_1remove
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1timeout_1remove)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1timeout_1remove_FUNC);
	gtk_timeout_remove((guint)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1timeout_1remove_FUNC);
}
#endif

#ifndef NO__1gtk_1toggle_1button_1get_1active
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1toggle_1button_1get_1active)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1get_1active_FUNC);
	rc = (jboolean)gtk_toggle_button_get_active((GtkToggleButton *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1get_1active_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1toggle_1button_1get_1inconsistent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1toggle_1button_1get_1inconsistent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1get_1inconsistent_FUNC);
	rc = (jboolean)gtk_toggle_button_get_inconsistent((GtkToggleButton *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1get_1inconsistent_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1toggle_1button_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1toggle_1button_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1new_FUNC);
	rc = (jintLong)gtk_toggle_button_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1toggle_1button_1set_1active
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1toggle_1button_1set_1active)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1set_1active_FUNC);
	gtk_toggle_button_set_active((GtkToggleButton *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1set_1active_FUNC);
}
#endif

#ifndef NO__1gtk_1toggle_1button_1set_1inconsistent
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1toggle_1button_1set_1inconsistent)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1set_1inconsistent_FUNC);
	gtk_toggle_button_set_inconsistent((GtkToggleButton *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1set_1inconsistent_FUNC);
}
#endif

#ifndef NO__1gtk_1toggle_1button_1set_1mode
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1toggle_1button_1set_1mode)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1toggle_1button_1set_1mode_FUNC);
	gtk_toggle_button_set_mode((GtkToggleButton *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1toggle_1button_1set_1mode_FUNC);
}
#endif

#ifndef NO__1gtk_1toolbar_1insert_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1toolbar_1insert_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1toolbar_1insert_1widget_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_toolbar_insert_widget((GtkToolbar *)arg0, (GtkWidget *)arg1, (const char *)lparg2, (const char *)lparg3, (gint)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1toolbar_1insert_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1toolbar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1toolbar_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1toolbar_1new_FUNC);
	rc = (jintLong)gtk_toolbar_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1toolbar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1toolbar_1set_1orientation
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1toolbar_1set_1orientation)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1toolbar_1set_1orientation_FUNC);
	gtk_toolbar_set_orientation((GtkToolbar *)arg0, (GtkOrientation)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1toolbar_1set_1orientation_FUNC);
}
#endif

#ifndef NO__1gtk_1tooltip_1trigger_1tooltip_1query
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tooltip_1trigger_1tooltip_1query)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltip_1trigger_1tooltip_1query_FUNC);
/*
	gtk_tooltip_trigger_tooltip_query((GdkDisplay*)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_tooltip_trigger_tooltip_query)
		if (fp) {
			((void (CALLING_CONVENTION*)(GdkDisplay*))fp)((GdkDisplay*)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltip_1trigger_1tooltip_1query_FUNC);
}
#endif

#ifndef NO__1gtk_1tooltips_1data_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tooltips_1data_1get)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1data_1get_FUNC);
	rc = (jintLong)gtk_tooltips_data_get((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1data_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tooltips_1disable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tooltips_1disable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1disable_FUNC);
	gtk_tooltips_disable((GtkTooltips *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1disable_FUNC);
}
#endif

#ifndef NO__1gtk_1tooltips_1enable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tooltips_1enable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1enable_FUNC);
	gtk_tooltips_enable((GtkTooltips *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1enable_FUNC);
}
#endif

#ifndef NO__1gtk_1tooltips_1force_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tooltips_1force_1window)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1force_1window_FUNC);
	gtk_tooltips_force_window((GtkTooltips *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1force_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1tooltips_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tooltips_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1new_FUNC);
	rc = (jintLong)gtk_tooltips_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tooltips_1set_1tip
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tooltips_1set_1tip)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tooltips_1set_1tip_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_tooltips_set_tip((GtkTooltips *)arg0, (GtkWidget *)arg1, (const gchar *)lparg2, (const gchar *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tooltips_1set_1tip_FUNC);
}
#endif

#if (!defined(NO__1gtk_1tree_1model_1get__III_3II) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1model_1get__JJI_3IJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1model_1get__III_3II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintArray arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1model_1get__JJI_3IJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintArray arg3, jintLong arg4)
#endif
{
	jint *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get__III_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get__JJI_3IJ_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_tree_model_get((GtkTreeModel *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get__III_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get__JJI_3IJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1tree_1model_1get__III_3JI) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1model_1get__JJI_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1model_1get__III_3JI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlongArray arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1model_1get__JJI_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlongArray arg3, jintLong arg4)
#endif
{
	jlong *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get__III_3JI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get__JJI_3JJ_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_tree_model_get((GtkTreeModel *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get__III_3JI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get__JJI_3JJ_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1tree_1model_1get_1iter
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1model_1get_1iter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get_1iter_FUNC);
	rc = (jboolean)gtk_tree_model_get_iter((GtkTreeModel *)arg0, (GtkTreeIter *)arg1, (GtkTreePath *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get_1iter_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1get_1iter_1first
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1model_1get_1iter_1first)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get_1iter_1first_FUNC);
	rc = (jboolean)gtk_tree_model_get_iter_first((GtkTreeModel *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get_1iter_1first_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1get_1n_1columns
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1model_1get_1n_1columns)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get_1n_1columns_FUNC);
	rc = (jint)gtk_tree_model_get_n_columns((GtkTreeModel *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get_1n_1columns_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1get_1path
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1model_1get_1path)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get_1path_FUNC);
	rc = (jintLong)gtk_tree_model_get_path((GtkTreeModel *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get_1path_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1get_1type
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1model_1get_1type)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1get_1type_FUNC);
	rc = (jintLong)gtk_tree_model_get_type();
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1get_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1iter_1children
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1model_1iter_1children)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1iter_1children_FUNC);
	rc = (jboolean)gtk_tree_model_iter_children((GtkTreeModel *)arg0, (GtkTreeIter *)arg1, (GtkTreeIter *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1iter_1children_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1iter_1n_1children
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1model_1iter_1n_1children)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1iter_1n_1children_FUNC);
	rc = (jint)gtk_tree_model_iter_n_children((GtkTreeModel *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1iter_1n_1children_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1iter_1next
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1model_1iter_1next)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1iter_1next_FUNC);
	rc = (jboolean)gtk_tree_model_iter_next((GtkTreeModel *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1iter_1next_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1model_1iter_1nth_1child
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1model_1iter_1nth_1child)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1model_1iter_1nth_1child_FUNC);
	rc = (jboolean)gtk_tree_model_iter_nth_child((GtkTreeModel *)arg0, (GtkTreeIter *)arg1, (GtkTreeIter *)arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1model_1iter_1nth_1child_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1append_1index
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1path_1append_1index)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1append_1index_FUNC);
	gtk_tree_path_append_index((GtkTreePath *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1append_1index_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1path_1compare
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1compare)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1compare_FUNC);
	rc = (jintLong)gtk_tree_path_compare((const GtkTreePath *)arg0, (const GtkTreePath *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1compare_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1down
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1path_1down)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1down_FUNC);
	gtk_tree_path_down((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1down_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1path_1free
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1path_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1free_FUNC);
	gtk_tree_path_free((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1free_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1path_1get_1depth
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1path_1get_1depth)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1get_1depth_FUNC);
	rc = (jint)gtk_tree_path_get_depth((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1get_1depth_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1get_1indices
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1get_1indices)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1get_1indices_FUNC);
	rc = (jintLong)gtk_tree_path_get_indices((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1get_1indices_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1new_FUNC);
	rc = (jintLong)gtk_tree_path_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1new_1first
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1new_1first)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1new_1first_FUNC);
	rc = (jintLong)gtk_tree_path_new_first();
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1new_1first_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1gtk_1tree_1path_1new_1from_1string__I) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1path_1new_1from_1string__J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1new_1from_1string__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1new_1from_1string__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1new_1from_1string__I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1new_1from_1string__J_FUNC);
#endif
	rc = (jintLong)gtk_tree_path_new_from_string((const gchar *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1new_1from_1string__I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1new_1from_1string__J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1new_1from_1string___3B
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1path_1new_1from_1string___3B)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1new_1from_1string___3B_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_tree_path_new_from_string((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1new_1from_1string___3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1next
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1path_1next)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1next_FUNC);
	gtk_tree_path_next((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1next_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1path_1prev
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1path_1prev)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1prev_FUNC);
	rc = (jboolean)gtk_tree_path_prev((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1prev_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1path_1up
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1path_1up)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1path_1up_FUNC);
	rc = (jboolean)gtk_tree_path_up((GtkTreePath *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1path_1up_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1selection_1count_1selected_1rows
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1selection_1count_1selected_1rows)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1count_1selected_1rows_FUNC);
/*
	rc = (jint)gtk_tree_selection_count_selected_rows((GtkTreeSelection *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_tree_selection_count_selected_rows)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GtkTreeSelection *))fp)((GtkTreeSelection *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1count_1selected_1rows_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1selection_1get_1selected
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1selection_1get_1selected)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLong arg2)
{
	jintLong *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1get_1selected_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_tree_selection_get_selected((GtkTreeSelection *)arg0, (GtkTreeModel **)lparg1, (GtkTreeIter *)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1get_1selected_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1selection_1get_1selected_1rows
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1selection_1get_1selected_1rows)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1get_1selected_1rows_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gtk_tree_selection_get_selected_rows((GtkTreeSelection *)arg0, (GtkTreeModel **)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_tree_selection_get_selected_rows)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(GtkTreeSelection *, GtkTreeModel **))fp)((GtkTreeSelection *)arg0, (GtkTreeModel **)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1get_1selected_1rows_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1selection_1path_1is_1selected
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1selection_1path_1is_1selected)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1path_1is_1selected_FUNC);
	rc = (jboolean)gtk_tree_selection_path_is_selected((GtkTreeSelection *)arg0, (GtkTreePath *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1path_1is_1selected_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1selection_1select_1all
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1select_1all)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1select_1all_FUNC);
	gtk_tree_selection_select_all((GtkTreeSelection *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1select_1all_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1selection_1select_1iter
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1select_1iter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1select_1iter_FUNC);
	gtk_tree_selection_select_iter((GtkTreeSelection *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1select_1iter_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1selection_1selected_1foreach
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1selected_1foreach)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1selected_1foreach_FUNC);
	gtk_tree_selection_selected_foreach((GtkTreeSelection *)arg0, (GtkTreeSelectionForeachFunc)arg1, (gpointer)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1selected_1foreach_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1selection_1set_1mode
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1set_1mode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1set_1mode_FUNC);
	gtk_tree_selection_set_mode((GtkTreeSelection *)arg0, (GtkSelectionMode)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1set_1mode_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1selection_1unselect_1all
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1unselect_1all)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1unselect_1all_FUNC);
	gtk_tree_selection_unselect_all((GtkTreeSelection *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1unselect_1all_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1selection_1unselect_1iter
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1selection_1unselect_1iter)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1selection_1unselect_1iter_FUNC);
	gtk_tree_selection_unselect_iter((GtkTreeSelection *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1selection_1unselect_1iter_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1store_1append
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1append_FUNC);
	gtk_tree_store_append((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, (GtkTreeIter *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1append_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1store_1clear
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1clear)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1clear_FUNC);
	gtk_tree_store_clear((GtkTreeStore *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1clear_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1store_1insert
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1insert)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1insert_FUNC);
	gtk_tree_store_insert((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, (GtkTreeIter *)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1insert_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1store_1newv
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1store_1newv)
	(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1newv_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_tree_store_newv(arg0, (GType *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1newv_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1store_1remove
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1remove)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1remove_FUNC);
	gtk_tree_store_remove((GtkTreeStore *)arg0, (GtkTreeIter *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1remove_FUNC);
}
#endif

#if (!defined(NO__1gtk_1tree_1store_1set__IIIII) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1store_1set__JJIIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__IIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__JJIIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__IIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__JJIIJ_FUNC);
#endif
	gtk_tree_store_set((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__IIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__JJIIJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1tree_1store_1set__IIIJI) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1store_1set__JJIJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__IIIJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__JJIJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jlong arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__IIIJI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__JJIJJ_FUNC);
#endif
	gtk_tree_store_set((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__IIIJI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__JJIJJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1tree_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3, jintLong arg4)
#endif
{
	GdkColor _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
	if (arg3) if ((lparg3 = getGdkColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	gtk_tree_store_set((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__IIILorg_eclipse_swt_internal_gtk_GdkColor_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__JJILorg_eclipse_swt_internal_gtk_GdkColor_2J_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1tree_1store_1set__IIIZI) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1store_1set__JJIZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__IIIZI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jboolean arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__JJIZJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jboolean arg3, jintLong arg4)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__IIIZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__JJIZJ_FUNC);
#endif
	gtk_tree_store_set((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__IIIZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__JJIZJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1tree_1store_1set__III_3BI) && !defined(JNI64)) || (!defined(NO__1gtk_1tree_1store_1set__JJI_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__III_3BI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1store_1set__JJI_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintLong arg4)
#endif
{
	jbyte *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__III_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1store_1set__JJI_3BJ_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_tree_store_set((GtkTreeStore *)arg0, (GtkTreeIter *)arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__III_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1store_1set__JJI_3BJ_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1tree_1view_1collapse_1row
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1collapse_1row)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1collapse_1row_FUNC);
	rc = (jboolean)gtk_tree_view_collapse_row((GtkTreeView *)arg0, (GtkTreePath *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1collapse_1row_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1add_1attribute
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1add_1attribute)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1add_1attribute_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_tree_view_column_add_attribute((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (const gchar *)lparg2, (gint)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1add_1attribute_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1cell_1get_1position
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1cell_1get_1position)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1cell_1get_1position_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)gtk_tree_view_column_cell_get_position((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gint *)lparg2, (gint *)lparg3);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_tree_view_column_cell_get_position)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(GtkTreeViewColumn *, GtkCellRenderer *, gint *, gint *))fp)((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gint *)lparg2, (gint *)lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1cell_1get_1position_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1cell_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1cell_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	GdkRectangle _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1cell_1get_1size_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_tree_view_column_cell_get_size((GtkTreeViewColumn *)arg0, (GdkRectangle *)lparg1, (gint *)lparg2, (gint *)lparg3, (gint *)lparg4, (gint *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setGdkRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1cell_1get_1size_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1cell_1set_1cell_1data
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1cell_1set_1cell_1data)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3, jboolean arg4)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1cell_1set_1cell_1data_FUNC);
	gtk_tree_view_column_cell_set_cell_data((GtkTreeViewColumn *)arg0, (GtkTreeModel *)arg1, (GtkTreeIter *)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1cell_1set_1cell_1data_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1clear
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1clear)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1clear_FUNC);
	gtk_tree_view_column_clear((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1clear_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1cell_1renderers
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1cell_1renderers)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1cell_1renderers_FUNC);
	rc = (jintLong)gtk_tree_view_column_get_cell_renderers((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1cell_1renderers_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1fixed_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1fixed_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1fixed_1width_FUNC);
	rc = (jint)gtk_tree_view_column_get_fixed_width((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1fixed_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1reorderable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1reorderable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1reorderable_FUNC);
	rc = (jboolean)gtk_tree_view_column_get_reorderable((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1reorderable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1resizable
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1resizable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1resizable_FUNC);
	rc = (jboolean)gtk_tree_view_column_get_resizable((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1resizable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1sizing
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1sizing)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1sizing_FUNC);
	rc = (jint)gtk_tree_view_column_get_sizing((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1sizing_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1sort_1indicator
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1sort_1indicator)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1sort_1indicator_FUNC);
	rc = (jboolean)gtk_tree_view_column_get_sort_indicator((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1sort_1indicator_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1sort_1order
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1sort_1order)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1sort_1order_FUNC);
	rc = (jint)gtk_tree_view_column_get_sort_order((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1sort_1order_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1spacing
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1spacing)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1spacing_FUNC);
	rc = (jint)gtk_tree_view_column_get_spacing((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1spacing_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1visible
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1visible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1visible_FUNC);
	rc = (jboolean)gtk_tree_view_column_get_visible((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1visible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1get_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1get_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1get_1width_FUNC);
	rc = (jint)gtk_tree_view_column_get_width((GtkTreeViewColumn *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1get_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1new_FUNC);
	rc = (jintLong)gtk_tree_view_column_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1pack_1end
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1pack_1end)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1pack_1end_FUNC);
	gtk_tree_view_column_pack_end((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1pack_1end_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1pack_1start
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1pack_1start)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1pack_1start_FUNC);
	gtk_tree_view_column_pack_start((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1pack_1start_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1alignment
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1alignment)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1alignment_FUNC);
	gtk_tree_view_column_set_alignment((GtkTreeViewColumn *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1alignment_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1cell_1data_1func
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1cell_1data_1func)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1cell_1data_1func_FUNC);
	gtk_tree_view_column_set_cell_data_func((GtkTreeViewColumn *)arg0, (GtkCellRenderer *)arg1, (GtkTreeCellDataFunc)arg2, (gpointer)arg3, (GtkDestroyNotify)arg4);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1cell_1data_1func_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1clickable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1clickable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1clickable_FUNC);
	gtk_tree_view_column_set_clickable((GtkTreeViewColumn *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1clickable_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1fixed_1width
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1fixed_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1fixed_1width_FUNC);
	gtk_tree_view_column_set_fixed_width((GtkTreeViewColumn *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1fixed_1width_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1min_1width
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1min_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1min_1width_FUNC);
	gtk_tree_view_column_set_min_width((GtkTreeViewColumn *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1min_1width_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1reorderable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1reorderable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1reorderable_FUNC);
	gtk_tree_view_column_set_reorderable((GtkTreeViewColumn *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1reorderable_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1resizable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1resizable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1resizable_FUNC);
	gtk_tree_view_column_set_resizable((GtkTreeViewColumn *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1resizable_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1sizing
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1sizing)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1sizing_FUNC);
	gtk_tree_view_column_set_sizing((GtkTreeViewColumn *)arg0, (GtkTreeViewColumnSizing)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1sizing_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1sort_1indicator
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1sort_1indicator)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1sort_1indicator_FUNC);
	gtk_tree_view_column_set_sort_indicator((GtkTreeViewColumn *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1sort_1indicator_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1sort_1order
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1sort_1order)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1sort_1order_FUNC);
	gtk_tree_view_column_set_sort_order((GtkTreeViewColumn *)arg0, (GtkSortType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1sort_1order_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1title
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1title)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1title_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_tree_view_column_set_title((GtkTreeViewColumn *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1title_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1visible
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1visible)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1visible_FUNC);
	gtk_tree_view_column_set_visible((GtkTreeViewColumn *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1visible_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1column_1set_1widget
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1column_1set_1widget)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1column_1set_1widget_FUNC);
	gtk_tree_view_column_set_widget((GtkTreeViewColumn *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1column_1set_1widget_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1create_1row_1drag_1icon
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1create_1row_1drag_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1create_1row_1drag_1icon_FUNC);
	rc = (jintLong)gtk_tree_view_create_row_drag_icon((GtkTreeView *)arg0, (GtkTreePath *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1create_1row_1drag_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1expand_1row
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1expand_1row)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1expand_1row_FUNC);
	rc = (jboolean)gtk_tree_view_expand_row((GtkTreeView *)arg0, (GtkTreePath *)arg1, (gboolean)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1expand_1row_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1background_1area
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1background_1area)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
{
	GdkRectangle _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1background_1area_FUNC);
	if (arg3) if ((lparg3 = getGdkRectangleFields(env, arg3, &_arg3)) == NULL) goto fail;
	gtk_tree_view_get_background_area((GtkTreeView *)arg0, (GtkTreePath *)arg1, (GtkTreeViewColumn *)arg2, (GdkRectangle *)lparg3);
fail:
	if (arg3 && lparg3) setGdkRectangleFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1background_1area_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1bin_1window
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1bin_1window)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1bin_1window_FUNC);
	rc = (jintLong)gtk_tree_view_get_bin_window((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1bin_1window_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1cell_1area
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1cell_1area)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
{
	GdkRectangle _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1cell_1area_FUNC);
	if (arg3) if ((lparg3 = &_arg3) == NULL) goto fail;
	gtk_tree_view_get_cell_area((GtkTreeView *)arg0, (GtkTreePath *)arg1, (GtkTreeViewColumn *)arg2, (GdkRectangle *)lparg3);
fail:
	if (arg3 && lparg3) setGdkRectangleFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1cell_1area_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1column
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1column)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1column_FUNC);
	rc = (jintLong)gtk_tree_view_get_column((GtkTreeView *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1column_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1columns
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1columns)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1columns_FUNC);
	rc = (jintLong)gtk_tree_view_get_columns((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1columns_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1cursor
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1cursor)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1cursor_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_tree_view_get_cursor((GtkTreeView *)arg0, (GtkTreePath **)lparg1, (GtkTreeViewColumn **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1cursor_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1expander_1column
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1expander_1column)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1expander_1column_FUNC);
	rc = (jintLong)gtk_tree_view_get_expander_column((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1expander_1column_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1headers_1visible
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1headers_1visible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1headers_1visible_FUNC);
	rc = (jboolean)gtk_tree_view_get_headers_visible((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1headers_1visible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1path_1at_1pos
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1path_1at_1pos)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLongArray arg3, jintLongArray arg4, jintArray arg5, jintArray arg6)
{
	jintLong *lparg3=NULL;
	jintLong *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1path_1at_1pos_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_tree_view_get_path_at_pos((GtkTreeView *)arg0, (gint)arg1, (gint)arg2, (GtkTreePath **)lparg3, (GtkTreeViewColumn **)lparg4, (gint *)lparg5, (gint *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1path_1at_1pos_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1rules_1hint
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1rules_1hint)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1rules_1hint_FUNC);
	rc = (jboolean)gtk_tree_view_get_rules_hint((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1rules_1hint_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1selection
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1selection)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1selection_FUNC);
	rc = (jintLong)gtk_tree_view_get_selection((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1selection_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1get_1visible_1rect
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1get_1visible_1rect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GdkRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1get_1visible_1rect_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gtk_tree_view_get_visible_rect((GtkTreeView *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setGdkRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1get_1visible_1rect_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1insert_1column
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1tree_1view_1insert_1column)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1insert_1column_FUNC);
	rc = (jint)gtk_tree_view_insert_column((GtkTreeView *)arg0, (GtkTreeViewColumn *)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1insert_1column_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1move_1column_1after
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1move_1column_1after)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1move_1column_1after_FUNC);
	gtk_tree_view_move_column_after((GtkTreeView *)arg0, (GtkTreeViewColumn *)arg1, (GtkTreeViewColumn *)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1move_1column_1after_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1new_1with_1model
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1tree_1view_1new_1with_1model)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1new_1with_1model_FUNC);
	rc = (jintLong)gtk_tree_view_new_with_model((GtkTreeModel *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1new_1with_1model_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1remove_1column
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1remove_1column)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1remove_1column_FUNC);
	gtk_tree_view_remove_column((GtkTreeView *)arg0, (GtkTreeViewColumn *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1remove_1column_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1row_1expanded
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1tree_1view_1row_1expanded)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1row_1expanded_FUNC);
	rc = (jboolean)gtk_tree_view_row_expanded((GtkTreeView *)arg0, (GtkTreePath *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1row_1expanded_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1tree_1view_1scroll_1to_1cell
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1scroll_1to_1cell)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3, jfloat arg4, jfloat arg5)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1scroll_1to_1cell_FUNC);
	gtk_tree_view_scroll_to_cell((GtkTreeView *)arg0, (GtkTreePath *)arg1, (GtkTreeViewColumn *)arg2, (gboolean)arg3, (gfloat)arg4, (gfloat)arg5);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1scroll_1to_1cell_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1scroll_1to_1point
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1scroll_1to_1point)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1scroll_1to_1point_FUNC);
	gtk_tree_view_scroll_to_point((GtkTreeView *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1scroll_1to_1point_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1cursor
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1cursor)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1cursor_FUNC);
	gtk_tree_view_set_cursor((GtkTreeView *)arg0, (GtkTreePath *)arg1, (GtkTreeViewColumn *)arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1cursor_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1drag_1dest_1row
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1drag_1dest_1row)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1drag_1dest_1row_FUNC);
	gtk_tree_view_set_drag_dest_row((GtkTreeView *)arg0, (GtkTreePath *)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1drag_1dest_1row_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1enable_1search
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1enable_1search)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1enable_1search_FUNC);
	gtk_tree_view_set_enable_search((GtkTreeView *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1enable_1search_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1grid_1lines
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1grid_1lines)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1grid_1lines_FUNC);
/*
	gtk_tree_view_set_grid_lines((GtkTreeView*)arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_tree_view_set_grid_lines)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkTreeView*, jint))fp)((GtkTreeView*)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1grid_1lines_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1headers_1visible
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1headers_1visible)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1headers_1visible_FUNC);
	gtk_tree_view_set_headers_visible((GtkTreeView *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1headers_1visible_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1model
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1model)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1model_FUNC);
	gtk_tree_view_set_model((GtkTreeView *)arg0, (GtkTreeModel *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1model_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1rules_1hint
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1rules_1hint)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1rules_1hint_FUNC);
	gtk_tree_view_set_rules_hint((GtkTreeView *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1rules_1hint_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1set_1search_1column
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1set_1search_1column)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1set_1search_1column_FUNC);
	gtk_tree_view_set_search_column((GtkTreeView *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1set_1search_1column_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1tree_1to_1widget_1coords
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1tree_1to_1widget_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1tree_1to_1widget_1coords_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	gtk_tree_view_tree_to_widget_coords((GtkTreeView *)arg0, (gint)arg1, (gint)arg2, (gint *)lparg3, (gint *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1tree_1to_1widget_1coords_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1unset_1rows_1drag_1dest
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1unset_1rows_1drag_1dest)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1unset_1rows_1drag_1dest_FUNC);
	gtk_tree_view_unset_rows_drag_dest((GtkTreeView *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1unset_1rows_1drag_1dest_FUNC);
}
#endif

#ifndef NO__1gtk_1tree_1view_1widget_1to_1tree_1coords
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1tree_1view_1widget_1to_1tree_1coords)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1tree_1view_1widget_1to_1tree_1coords_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	gtk_tree_view_widget_to_tree_coords((GtkTreeView *)arg0, arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1tree_1view_1widget_1to_1tree_1coords_FUNC);
}
#endif

#ifndef NO__1gtk_1vbox_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1vbox_1new)
	(JNIEnv *env, jclass that, jboolean arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1vbox_1new_FUNC);
	rc = (jintLong)gtk_vbox_new((gboolean)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1vbox_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1viewport_1get_1shadow_1type
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1viewport_1get_1shadow_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1viewport_1get_1shadow_1type_FUNC);
	rc = (jint)gtk_viewport_get_shadow_type((GtkViewport *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1viewport_1get_1shadow_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1viewport_1set_1shadow_1type
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1viewport_1set_1shadow_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1viewport_1set_1shadow_1type_FUNC);
	gtk_viewport_set_shadow_type((GtkViewport *)arg0, (GtkShadowType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1viewport_1set_1shadow_1type_FUNC);
}
#endif

#ifndef NO__1gtk_1vscale_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1vscale_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1vscale_1new_FUNC);
	rc = (jintLong)gtk_vscale_new((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1vscale_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1vscrollbar_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1vscrollbar_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1vscrollbar_1new_FUNC);
	rc = (jintLong)gtk_vscrollbar_new((GtkAdjustment *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1vscrollbar_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1vseparator_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1vseparator_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1vseparator_1new_FUNC);
	rc = (jintLong)gtk_vseparator_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1vseparator_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1add_1accelerator
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1add_1accelerator)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1add_1accelerator_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_widget_add_accelerator((GtkWidget *)arg0, (const gchar *)lparg1, (GtkAccelGroup *)arg2, (guint)arg3, (GdkModifierType)arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1add_1accelerator_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1add_1events
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1add_1events)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1add_1events_FUNC);
	gtk_widget_add_events((GtkWidget *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1add_1events_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1child_1focus
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1child_1focus)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1child_1focus_FUNC);
	rc = (jboolean)gtk_widget_child_focus((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1child_1focus_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1gtk_1widget_1create_1pango_1layout__II) && !defined(JNI64)) || (!defined(NO__1gtk_1widget_1create_1pango_1layout__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1create_1pango_1layout__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1create_1pango_1layout__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1create_1pango_1layout__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1create_1pango_1layout__JJ_FUNC);
#endif
	rc = (jintLong)gtk_widget_create_pango_layout((GtkWidget *)arg0, (const gchar *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1create_1pango_1layout__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1create_1pango_1layout__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1gtk_1widget_1create_1pango_1layout__I_3B) && !defined(JNI64)) || (!defined(NO__1gtk_1widget_1create_1pango_1layout__J_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1create_1pango_1layout__I_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1create_1pango_1layout__J_3B)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
#endif
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1create_1pango_1layout__I_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1create_1pango_1layout__J_3B_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)gtk_widget_create_pango_layout((GtkWidget *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1create_1pango_1layout__I_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1create_1pango_1layout__J_3B_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1destroy
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1destroy_FUNC);
	gtk_widget_destroy((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1destroy_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1event
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1event)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1event_FUNC);
	rc = (jboolean)gtk_widget_event((GtkWidget *)arg0, (GdkEvent *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1event_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1accessible
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1accessible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1accessible_FUNC);
	rc = (jintLong)gtk_widget_get_accessible((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1accessible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1child_1visible
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1get_1child_1visible)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1child_1visible_FUNC);
	rc = (jboolean)gtk_widget_get_child_visible((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1child_1visible_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1default_1direction
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1widget_1get_1default_1direction)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1default_1direction_FUNC);
	rc = (jint)gtk_widget_get_default_direction();
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1default_1direction_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1default_1style
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1default_1style)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1default_1style_FUNC);
	rc = (jintLong)gtk_widget_get_default_style();
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1default_1style_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1direction
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1widget_1get_1direction)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1direction_FUNC);
	rc = (jint)gtk_widget_get_direction((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1direction_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1events
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1widget_1get_1events)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1events_FUNC);
	rc = (jint)gtk_widget_get_events((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1events_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1modifier_1style
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1modifier_1style)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1modifier_1style_FUNC);
	rc = (jintLong)gtk_widget_get_modifier_style((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1modifier_1style_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1pango_1context
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1pango_1context)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1pango_1context_FUNC);
	rc = (jintLong)gtk_widget_get_pango_context((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1pango_1context_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1parent
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1parent_FUNC);
	rc = (jintLong)gtk_widget_get_parent((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1parent_1window
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1parent_1window)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1parent_1window_FUNC);
	rc = (jintLong)gtk_widget_get_parent_window((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1parent_1window_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1size_1request
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1get_1size_1request)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1size_1request_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_widget_get_size_request((GtkWidget *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1size_1request_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1get_1style
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1style)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1style_FUNC);
	rc = (jintLong)gtk_widget_get_style((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1style_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1get_1toplevel
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1widget_1get_1toplevel)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1get_1toplevel_FUNC);
	rc = (jintLong)gtk_widget_get_toplevel((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1get_1toplevel_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1grab_1focus
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1grab_1focus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1grab_1focus_FUNC);
	gtk_widget_grab_focus((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1grab_1focus_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1hide
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1hide)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1hide_FUNC);
	gtk_widget_hide((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1hide_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1is_1composited
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1is_1composited)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1is_1composited_FUNC);
/*
	rc = (jboolean)gtk_widget_is_composited((GtkWidget *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_widget_is_composited)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(GtkWidget *))fp)((GtkWidget *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1is_1composited_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1is_1focus
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1is_1focus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1is_1focus_FUNC);
	rc = (jboolean)gtk_widget_is_focus((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1is_1focus_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1map
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1map)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1map_FUNC);
	gtk_widget_map((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1map_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1mnemonic_1activate
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1mnemonic_1activate)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1mnemonic_1activate_FUNC);
	rc = (jboolean)gtk_widget_mnemonic_activate((GtkWidget *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1mnemonic_1activate_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1modify_1base
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1base)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1base_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_widget_modify_base((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1base_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1modify_1bg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1bg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1bg_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_widget_modify_bg((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1bg_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1modify_1fg
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1fg)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1fg_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_widget_modify_fg((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1fg_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1modify_1font
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1font)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1font_FUNC);
	gtk_widget_modify_font((GtkWidget *)arg0, (PangoFontDescription *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1font_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1modify_1style
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1style)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1style_FUNC);
	gtk_widget_modify_style((GtkWidget *)arg0, (GtkRcStyle *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1style_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1modify_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1modify_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	GdkColor _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1modify_1text_FUNC);
	if (arg2) if ((lparg2 = getGdkColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_widget_modify_text((GtkWidget *)arg0, (GtkStateType)arg1, (GdkColor *)lparg2);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1modify_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1queue_1resize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1queue_1resize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1queue_1resize_FUNC);
	gtk_widget_queue_resize((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1queue_1resize_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1realize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1realize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1realize_FUNC);
	gtk_widget_realize((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1realize_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1remove_1accelerator
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1remove_1accelerator)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1remove_1accelerator_FUNC);
	gtk_widget_remove_accelerator((GtkWidget *)arg0, (GtkAccelGroup *)arg1, (guint)arg2, (GdkModifierType)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1remove_1accelerator_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1reparent
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1reparent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1reparent_FUNC);
	gtk_widget_reparent((GtkWidget *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1reparent_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1send_1expose
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1widget_1send_1expose)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1send_1expose_FUNC);
	rc = (jint)gtk_widget_send_expose((GtkWidget *)arg0, (GdkEvent *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1send_1expose_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1set_1app_1paintable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1app_1paintable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1app_1paintable_FUNC);
	gtk_widget_set_app_paintable((GtkWidget *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1app_1paintable_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1default_1direction
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1default_1direction)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1default_1direction_FUNC);
	gtk_widget_set_default_direction((GtkTextDirection)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1default_1direction_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1direction
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1direction)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1direction_FUNC);
	gtk_widget_set_direction((GtkWidget *)arg0, (GtkTextDirection)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1direction_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1double_1buffered
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1double_1buffered)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1double_1buffered_FUNC);
	gtk_widget_set_double_buffered((GtkWidget *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1double_1buffered_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1name
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1name)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1name_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_widget_set_name((GtkWidget *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1name_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1parent_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1parent_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1parent_1window_FUNC);
	gtk_widget_set_parent_window((GtkWidget *)arg0, (GdkWindow *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1parent_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1redraw_1on_1allocate
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1redraw_1on_1allocate)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1redraw_1on_1allocate_FUNC);
	gtk_widget_set_redraw_on_allocate((GtkWidget *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1redraw_1on_1allocate_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1sensitive
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1sensitive)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1sensitive_FUNC);
	gtk_widget_set_sensitive((GtkWidget *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1sensitive_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1size_1request
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1size_1request)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1size_1request_FUNC);
	gtk_widget_set_size_request((GtkWidget *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1size_1request_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1state
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1state)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1state_FUNC);
	gtk_widget_set_state((GtkWidget *)arg0, (GtkStateType)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1state_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1style
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1style)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1style_FUNC);
	gtk_widget_set_style((GtkWidget *)arg0, (GtkStyle *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1style_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1set_1tooltip_1text
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1set_1tooltip_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1set_1tooltip_1text_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	gtk_widget_set_tooltip_text((GtkWidget *)arg0, (const gchar *)lparg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_widget_set_tooltip_text)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkWidget *, const gchar *))fp)((GtkWidget *)arg0, (const gchar *)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1set_1tooltip_1text_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1shape_1combine_1mask
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1shape_1combine_1mask)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1shape_1combine_1mask_FUNC);
	gtk_widget_shape_combine_mask((GtkWidget *)arg0, (GdkBitmap *)arg1, (gint)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1shape_1combine_1mask_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1show
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1show)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1show_FUNC);
	gtk_widget_show((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1show_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1show_1now
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1show_1now)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1show_1now_FUNC);
	gtk_widget_show_now((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1show_1now_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1size_1allocate
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1size_1allocate)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GtkAllocation _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1size_1allocate_FUNC);
	if (arg1) if ((lparg1 = getGtkAllocationFields(env, arg1, &_arg1)) == NULL) goto fail;
	gtk_widget_size_allocate((GtkWidget *)arg0, (GtkAllocation *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1size_1allocate_FUNC);
}
#endif

#ifndef NO__1gtk_1widget_1size_1request
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1size_1request)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	GtkRequisition _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1size_1request_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	gtk_widget_size_request((GtkWidget *)arg0, (GtkRequisition *)lparg1);
fail:
	if (arg1 && lparg1) setGtkRequisitionFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1size_1request_FUNC);
}
#endif

#if (!defined(NO__1gtk_1widget_1style_1get__I_3B_3II) && !defined(JNI64)) || (!defined(NO__1gtk_1widget_1style_1get__J_3B_3IJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1style_1get__I_3B_3II)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintArray arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1style_1get__J_3B_3IJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1style_1get__I_3B_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1style_1get__J_3B_3IJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_widget_style_get((GtkWidget *)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1style_1get__I_3B_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1style_1get__J_3B_3IJ_FUNC);
#endif
}
#endif

#if (!defined(NO__1gtk_1widget_1style_1get__I_3B_3JI) && !defined(JNI64)) || (!defined(NO__1gtk_1widget_1style_1get__J_3B_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1style_1get__I_3B_3JI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlongArray arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1style_1get__J_3B_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jlongArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg1=NULL;
	jlong *lparg2=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1style_1get__I_3B_3JI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1style_1get__J_3B_3JJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_widget_style_get((GtkWidget *)arg0, (const gchar *)lparg1, lparg2, (const gchar *)NULL);
fail:
	if (arg2 && lparg2) (*env)->ReleaseLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1style_1get__I_3B_3JI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1style_1get__J_3B_3JJ_FUNC);
#endif
}
#endif

#ifndef NO__1gtk_1widget_1translate_1coordinates
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1widget_1translate_1coordinates)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1translate_1coordinates_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_widget_translate_coordinates((GtkWidget *)arg0, (GtkWidget *)arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1translate_1coordinates_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1widget_1unrealize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1widget_1unrealize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1widget_1unrealize_FUNC);
	gtk_widget_unrealize((GtkWidget *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1widget_1unrealize_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1activate_1default
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1window_1activate_1default)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1activate_1default_FUNC);
	rc = (jboolean)gtk_window_activate_default((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1activate_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1add_1accel_1group
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1add_1accel_1group)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1add_1accel_1group_FUNC);
	gtk_window_add_accel_group((GtkWindow *)arg0, (GtkAccelGroup *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1add_1accel_1group_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1deiconify
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1deiconify)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1deiconify_FUNC);
	gtk_window_deiconify((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1deiconify_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1fullscreen
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1fullscreen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1fullscreen_FUNC);
	gtk_window_fullscreen((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1fullscreen_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1get_1focus
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1get_1focus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1focus_FUNC);
	rc = (jintLong)gtk_window_get_focus((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1focus_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1group
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1get_1group)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1group_FUNC);
/*
	rc = (jintLong)gtk_window_get_group((GtkWindow *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_window_get_group)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(GtkWindow *))fp)((GtkWindow *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1group_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1icon_1list
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1get_1icon_1list)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1icon_1list_FUNC);
	rc = (jintLong)gtk_window_get_icon_list((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1icon_1list_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1mnemonic_1modifier
JNIEXPORT jint JNICALL OS_NATIVE(_1gtk_1window_1get_1mnemonic_1modifier)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1mnemonic_1modifier_FUNC);
	rc = (jint)gtk_window_get_mnemonic_modifier((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1mnemonic_1modifier_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1modal
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1window_1get_1modal)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1modal_FUNC);
	rc = (jboolean)gtk_window_get_modal((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1modal_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1opacity
JNIEXPORT jdouble JNICALL OS_NATIVE(_1gtk_1window_1get_1opacity)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1opacity_FUNC);
/*
	rc = (jdouble)gtk_window_get_opacity((GtkWindow *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_window_get_opacity)
		if (fp) {
			rc = (jdouble)((jdouble (CALLING_CONVENTION*)(GtkWindow *))fp)((GtkWindow *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1opacity_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1get_1position
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1get_1position)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1position_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_window_get_position((GtkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1position_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1get_1size_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	gtk_window_get_size((GtkWindow *)arg0, (gint *)lparg1, (gint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1get_1size_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1group_1add_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1group_1add_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1group_1add_1window_FUNC);
	gtk_window_group_add_window((GtkWindowGroup*)arg0, (GtkWindow*)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1group_1add_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1group_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1group_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1group_1new_FUNC);
	rc = (jintLong)gtk_window_group_new();
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1group_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1group_1remove_1window
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1group_1remove_1window)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1group_1remove_1window_FUNC);
	gtk_window_group_remove_window((GtkWindowGroup*)arg0, (GtkWindow*)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1group_1remove_1window_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1iconify
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1iconify)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1iconify_FUNC);
	gtk_window_iconify((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1iconify_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1is_1active
JNIEXPORT jboolean JNICALL OS_NATIVE(_1gtk_1window_1is_1active)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1is_1active_FUNC);
	rc = (jboolean)gtk_window_is_active((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1is_1active_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1list_1toplevels
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1list_1toplevels)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1list_1toplevels_FUNC);
	rc = (jintLong)gtk_window_list_toplevels();
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1list_1toplevels_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1maximize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1maximize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1maximize_FUNC);
	gtk_window_maximize((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1maximize_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1move
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1move)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1move_FUNC);
	gtk_window_move((GtkWindow *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1move_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1gtk_1window_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1new_FUNC);
	rc = (jintLong)gtk_window_new((GtkWindowType)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gtk_1window_1present
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1present)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1present_FUNC);
	gtk_window_present((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1present_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1remove_1accel_1group
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1remove_1accel_1group)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1remove_1accel_1group_FUNC);
	gtk_window_remove_accel_group((GtkWindow *)arg0, (GtkAccelGroup *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1remove_1accel_1group_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1resize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1resize)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1resize_FUNC);
	gtk_window_resize((GtkWindow *)arg0, (gint)arg1, (gint)arg2);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1resize_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1default
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1default)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1default_FUNC);
	gtk_window_set_default((GtkWindow *)arg0, (GtkWidget *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1default_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1destroy_1with_1parent
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1destroy_1with_1parent)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1destroy_1with_1parent_FUNC);
	gtk_window_set_destroy_with_parent((GtkWindow *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1destroy_1with_1parent_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1geometry_1hints
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1geometry_1hints)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3)
{
	GdkGeometry _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1geometry_1hints_FUNC);
	if (arg2) if ((lparg2 = getGdkGeometryFields(env, arg2, &_arg2)) == NULL) goto fail;
	gtk_window_set_geometry_hints((GtkWindow *)arg0, (GtkWidget *)arg1, lparg2, arg3);
fail:
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1geometry_1hints_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1icon_1list
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1icon_1list)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1icon_1list_FUNC);
	gtk_window_set_icon_list((GtkWindow *)arg0, (GList *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1icon_1list_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1keep_1below
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1keep_1below)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1keep_1below_FUNC);
/*
	gtk_window_set_keep_below((GtkWindow *)arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_window_set_keep_below)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkWindow *, gboolean))fp)((GtkWindow *)arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1keep_1below_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1modal
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1modal)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1modal_FUNC);
	gtk_window_set_modal((GtkWindow *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1modal_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1opacity
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1opacity)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1opacity_FUNC);
/*
	gtk_window_set_opacity((GtkWindow *)arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_window_set_opacity)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkWindow *, jdouble))fp)((GtkWindow *)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1opacity_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1resizable
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1resizable)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1resizable_FUNC);
	gtk_window_set_resizable((GtkWindow *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1resizable_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1skip_1taskbar_1hint
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1skip_1taskbar_1hint)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1skip_1taskbar_1hint_FUNC);
/*
	gtk_window_set_skip_taskbar_hint((GtkWindow *)arg0, (gboolean)arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gtk_window_set_skip_taskbar_hint)
		if (fp) {
			((void (CALLING_CONVENTION*)(GtkWindow *, gboolean))fp)((GtkWindow *)arg0, (gboolean)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1skip_1taskbar_1hint_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1title
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1title)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1title_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_window_set_title((GtkWindow *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1title_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1transient_1for
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1transient_1for)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1transient_1for_FUNC);
	gtk_window_set_transient_for((GtkWindow *)arg0, (GtkWindow *)arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1transient_1for_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1set_1type_1hint
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1set_1type_1hint)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1set_1type_1hint_FUNC);
	gtk_window_set_type_hint((GtkWindow *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1set_1type_1hint_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1unfullscreen
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1unfullscreen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1unfullscreen_FUNC);
	gtk_window_unfullscreen((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1unfullscreen_FUNC);
}
#endif

#ifndef NO__1gtk_1window_1unmaximize
JNIEXPORT void JNICALL OS_NATIVE(_1gtk_1window_1unmaximize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1gtk_1window_1unmaximize_FUNC);
	gtk_window_unmaximize((GtkWindow *)arg0);
	OS_NATIVE_EXIT(env, that, _1gtk_1window_1unmaximize_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1background_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1background_1new)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1background_1new_FUNC);
	rc = (jintLong)pango_attr_background_new(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1background_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1font_1desc_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1font_1desc_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1font_1desc_1new_FUNC);
	rc = (jintLong)pango_attr_font_desc_new((const PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1font_1desc_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1foreground_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1foreground_1new)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1foreground_1new_FUNC);
	rc = (jintLong)pango_attr_foreground_new(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1foreground_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1iterator_1destroy
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1attr_1iterator_1destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1iterator_1destroy_FUNC);
	pango_attr_iterator_destroy((PangoAttrIterator *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1iterator_1destroy_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1iterator_1get
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1iterator_1get)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1iterator_1get_FUNC);
	rc = (jintLong)pango_attr_iterator_get((PangoAttrIterator *)arg0, (PangoAttrType)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1iterator_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1iterator_1get_1attrs
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1iterator_1get_1attrs)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1iterator_1get_1attrs_FUNC);
	rc = (jintLong)pango_attr_iterator_get_attrs((PangoAttrIterator *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1iterator_1get_1attrs_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1iterator_1next
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1attr_1iterator_1next)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1iterator_1next_FUNC);
	rc = (jboolean)pango_attr_iterator_next((PangoAttrIterator *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1iterator_1next_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1iterator_1range
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1attr_1iterator_1range)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1iterator_1range_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_attr_iterator_range((PangoAttrIterator *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1iterator_1range_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1list_1change
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1attr_1list_1change)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1list_1change_FUNC);
	pango_attr_list_change((PangoAttrList *)arg0, (PangoAttribute *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1list_1change_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1list_1get_1iterator
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1list_1get_1iterator)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1list_1get_1iterator_FUNC);
	rc = (jintLong)pango_attr_list_get_iterator((PangoAttrList *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1list_1get_1iterator_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1list_1insert
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1attr_1list_1insert)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1list_1insert_FUNC);
	pango_attr_list_insert((PangoAttrList *)arg0, (PangoAttribute *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1list_1insert_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1list_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1list_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1list_1new_FUNC);
	rc = (jintLong)pango_attr_list_new();
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1list_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1list_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1attr_1list_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1list_1unref_FUNC);
	pango_attr_list_unref((PangoAttrList *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1list_1unref_FUNC);
}
#endif

#ifndef NO__1pango_1attr_1rise_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1rise_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1rise_1new_FUNC);
	rc = (jintLong)pango_attr_rise_new(arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1rise_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1shape_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1shape_1new)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	PangoRectangle _arg0, *lparg0=NULL;
	PangoRectangle _arg1, *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1shape_1new_FUNC);
	if (arg0) if ((lparg0 = getPangoRectangleFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPangoRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)pango_attr_shape_new(lparg0, lparg1);
fail:
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1shape_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1strikethrough_1color_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1strikethrough_1color_1new)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1strikethrough_1color_1new_FUNC);
/*
	rc = (jintLong)pango_attr_strikethrough_color_new(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_attr_strikethrough_color_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jshort, jshort, jshort))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1strikethrough_1color_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1strikethrough_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1strikethrough_1new)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1strikethrough_1new_FUNC);
	rc = (jintLong)pango_attr_strikethrough_new(arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1strikethrough_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1underline_1color_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1underline_1color_1new)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1underline_1color_1new_FUNC);
/*
	rc = (jintLong)pango_attr_underline_color_new(arg0, arg1, arg2);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_attr_underline_color_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jshort, jshort, jshort))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1underline_1color_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1underline_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1underline_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1underline_1new_FUNC);
	rc = (jintLong)pango_attr_underline_new(arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1underline_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1attr_1weight_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1attr_1weight_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1attr_1weight_1new_FUNC);
	rc = (jintLong)pango_attr_weight_new(arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1attr_1weight_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1context_1get_1font_1options
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1cairo_1context_1get_1font_1options)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1context_1get_1font_1options_FUNC);
/*
	rc = (jintLong)pango_cairo_context_get_font_options((PangoContext *)arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_context_get_font_options)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(PangoContext *))fp)((PangoContext *)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1context_1get_1font_1options_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1context_1set_1font_1options
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1cairo_1context_1set_1font_1options)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1context_1set_1font_1options_FUNC);
/*
	pango_cairo_context_set_font_options((PangoContext *)arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_context_set_font_options)
		if (fp) {
			((void (CALLING_CONVENTION*)(PangoContext *, jintLong))fp)((PangoContext *)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1context_1set_1font_1options_FUNC);
}
#endif

#ifndef NO__1pango_1cairo_1create_1layout
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1cairo_1create_1layout)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1create_1layout_FUNC);
/*
	rc = (jintLong)pango_cairo_create_layout(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_create_layout)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1create_1layout_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1font_1map_1create_1context
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1cairo_1font_1map_1create_1context)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1font_1map_1create_1context_FUNC);
/*
	rc = (jintLong)pango_cairo_font_map_create_context(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_font_map_create_context)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1font_1map_1create_1context_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1font_1map_1get_1default
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1cairo_1font_1map_1get_1default)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1font_1map_1get_1default_FUNC);
/*
	rc = (jintLong)pango_cairo_font_map_get_default();
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_font_map_get_default)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1font_1map_1get_1default_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1font_1map_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1cairo_1font_1map_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1font_1map_1new_FUNC);
/*
	rc = (jintLong)pango_cairo_font_map_new();
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_font_map_new)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1font_1map_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1cairo_1font_1map_1set_1resolution
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1cairo_1font_1map_1set_1resolution)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1font_1map_1set_1resolution_FUNC);
/*
	pango_cairo_font_map_set_resolution(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_font_map_set_resolution)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jdouble))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1font_1map_1set_1resolution_FUNC);
}
#endif

#ifndef NO__1pango_1cairo_1layout_1path
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1cairo_1layout_1path)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1layout_1path_FUNC);
/*
	pango_cairo_layout_path(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_layout_path)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1layout_1path_FUNC);
}
#endif

#ifndef NO__1pango_1cairo_1show_1layout
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1cairo_1show_1layout)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1cairo_1show_1layout_FUNC);
/*
	pango_cairo_show_layout(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_cairo_show_layout)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1cairo_1show_1layout_FUNC);
}
#endif

#ifndef NO__1pango_1context_1get_1base_1dir
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1context_1get_1base_1dir)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1context_1get_1base_1dir_FUNC);
	rc = (jint)pango_context_get_base_dir((PangoContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1get_1base_1dir_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1context_1get_1language
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1context_1get_1language)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1context_1get_1language_FUNC);
	rc = (jintLong)pango_context_get_language((PangoContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1get_1language_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1context_1get_1metrics
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1context_1get_1metrics)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1context_1get_1metrics_FUNC);
	rc = (jintLong)pango_context_get_metrics((PangoContext *)arg0, (const PangoFontDescription *)arg1, (PangoLanguage *)arg2);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1get_1metrics_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1context_1list_1families
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1context_1list_1families)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2)
{
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1context_1list_1families_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_context_list_families((PangoContext *)arg0, (PangoFontFamily ***)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1list_1families_FUNC);
}
#endif

#ifndef NO__1pango_1context_1set_1base_1dir
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1context_1set_1base_1dir)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1context_1set_1base_1dir_FUNC);
	pango_context_set_base_dir((PangoContext *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1set_1base_1dir_FUNC);
}
#endif

#ifndef NO__1pango_1context_1set_1language
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1context_1set_1language)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1context_1set_1language_FUNC);
	pango_context_set_language((PangoContext *)arg0, (PangoLanguage *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1context_1set_1language_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1copy
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1description_1copy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1copy_FUNC);
	rc = (jintLong)pango_font_description_copy((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1copy_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1free
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1free_FUNC);
	pango_font_description_free((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1free_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1from_1string
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1description_1from_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1from_1string_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)pango_font_description_from_string((const char *)lparg0);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1from_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1family
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1description_1get_1family)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1family_FUNC);
	rc = (jintLong)pango_font_description_get_family((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1family_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1size
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1description_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1size_FUNC);
	rc = (jint)pango_font_description_get_size((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1size_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1stretch
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1description_1get_1stretch)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1stretch_FUNC);
	rc = (jint)pango_font_description_get_stretch((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1stretch_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1style
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1description_1get_1style)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1style_FUNC);
	rc = (jint)pango_font_description_get_style((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1style_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1variant
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1description_1get_1variant)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1variant_FUNC);
	rc = (jint)pango_font_description_get_variant((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1variant_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1get_1weight
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1description_1get_1weight)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1get_1weight_FUNC);
	rc = (jint)pango_font_description_get_weight((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1get_1weight_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1description_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1new_FUNC);
	rc = (jintLong)pango_font_description_new();
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1description_1set_1family
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1family)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1family_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	pango_font_description_set_family((PangoFontDescription *)arg0, (const char *)lparg1);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1family_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1set_1size
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1size_FUNC);
	pango_font_description_set_size((PangoFontDescription *)arg0, (gint)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1size_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1set_1stretch
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1stretch)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1stretch_FUNC);
	pango_font_description_set_stretch((PangoFontDescription *)arg0, (PangoStretch)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1stretch_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1set_1style
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1style)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1style_FUNC);
	pango_font_description_set_style((PangoFontDescription *)arg0, (PangoStyle)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1style_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1set_1variant
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1variant)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1variant_FUNC);
	pango_font_description_set_variant((PangoFontDescription *)arg0, (PangoVariant)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1variant_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1set_1weight
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1description_1set_1weight)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1set_1weight_FUNC);
	pango_font_description_set_weight((PangoFontDescription *)arg0, (PangoWeight)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1set_1weight_FUNC);
}
#endif

#ifndef NO__1pango_1font_1description_1to_1string
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1description_1to_1string)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1description_1to_1string_FUNC);
	rc = (jintLong)pango_font_description_to_string((PangoFontDescription *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1description_1to_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1face_1describe
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1face_1describe)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1face_1describe_FUNC);
	rc = (jintLong)pango_font_face_describe((PangoFontFace *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1face_1describe_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1family_1get_1name
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1family_1get_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1family_1get_1name_FUNC);
	rc = (jintLong)pango_font_family_get_name((PangoFontFamily *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1family_1get_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1family_1list_1faces
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1family_1list_1faces)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2)
{
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1family_1list_1faces_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_font_family_list_faces((PangoFontFamily *)arg0, (PangoFontFace ***)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1family_1list_1faces_FUNC);
}
#endif

#ifndef NO__1pango_1font_1get_1metrics
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1font_1get_1metrics)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1get_1metrics_FUNC);
	rc = (jintLong)pango_font_get_metrics((PangoFont *)arg0, (PangoLanguage *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1get_1metrics_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1approximate_1char_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1approximate_1char_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1approximate_1char_1width_FUNC);
	rc = (jint)pango_font_metrics_get_approximate_char_width((PangoFontMetrics *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1approximate_1char_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1ascent
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1ascent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1ascent_FUNC);
	rc = (jint)pango_font_metrics_get_ascent((PangoFontMetrics *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1ascent_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1descent
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1descent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1descent_FUNC);
	rc = (jint)pango_font_metrics_get_descent((PangoFontMetrics *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1descent_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1strikethrough_1position
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1strikethrough_1position)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1strikethrough_1position_FUNC);
/*
	rc = (jint)pango_font_metrics_get_strikethrough_position(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_font_metrics_get_strikethrough_position)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1strikethrough_1position_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1strikethrough_1thickness
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1strikethrough_1thickness)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1strikethrough_1thickness_FUNC);
/*
	rc = (jint)pango_font_metrics_get_strikethrough_thickness(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_font_metrics_get_strikethrough_thickness)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1strikethrough_1thickness_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1underline_1position
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1underline_1position)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1underline_1position_FUNC);
/*
	rc = (jint)pango_font_metrics_get_underline_position(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_font_metrics_get_underline_position)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1underline_1position_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1get_1underline_1thickness
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1font_1metrics_1get_1underline_1thickness)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1get_1underline_1thickness_FUNC);
/*
	rc = (jint)pango_font_metrics_get_underline_thickness(arg0);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_font_metrics_get_underline_thickness)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1get_1underline_1thickness_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1font_1metrics_1unref
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1font_1metrics_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1font_1metrics_1unref_FUNC);
	pango_font_metrics_unref((PangoFontMetrics *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1font_1metrics_1unref_FUNC);
}
#endif

#ifndef NO__1pango_1language_1from_1string
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1language_1from_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1language_1from_1string_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)pango_language_from_string((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, _1pango_1language_1from_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1context_1changed
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1context_1changed)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1context_1changed_FUNC);
	pango_layout_context_changed((PangoLayout *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1context_1changed_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1get_1alignment
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1get_1alignment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1alignment_FUNC);
	rc = (jint)pango_layout_get_alignment((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1alignment_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1attributes
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1attributes)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1attributes_FUNC);
	rc = (jintLong)pango_layout_get_attributes((PangoLayout *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1attributes_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1context
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1context)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1context_FUNC);
	rc = (jintLong)pango_layout_get_context((PangoLayout *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1context_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1indent
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1get_1indent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1indent_FUNC);
	rc = (jint)pango_layout_get_indent((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1indent_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1iter
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1iter)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1iter_FUNC);
	rc = (jintLong)pango_layout_get_iter((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1iter_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1justify
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1layout_1get_1justify)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1justify_FUNC);
	rc = (jboolean)pango_layout_get_justify((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1justify_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1line
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1line)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1line_FUNC);
	rc = (jintLong)pango_layout_get_line((PangoLayout *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1line_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1line_1count
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1get_1line_1count)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1line_1count_FUNC);
	rc = (jint)pango_layout_get_line_count((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1line_1count_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1log_1attrs
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1get_1log_1attrs)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2)
{
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1log_1attrs_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_layout_get_log_attrs((PangoLayout*)arg0, (PangoLogAttr **)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1log_1attrs_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1get_1size
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1size_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_layout_get_size((PangoLayout *)arg0, (int *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1size_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1get_1spacing
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1get_1spacing)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1spacing_FUNC);
	rc = (jint)pango_layout_get_spacing((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1spacing_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1tabs
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1tabs)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1tabs_FUNC);
	rc = (jintLong)pango_layout_get_tabs((PangoLayout*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1tabs_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1text
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1get_1text)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1text_FUNC);
	rc = (jintLong)pango_layout_get_text((PangoLayout *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1text_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1get_1width
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1get_1width)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1get_1width_FUNC);
	rc = (jint)pango_layout_get_width((PangoLayout *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1get_1width_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1index_1to_1pos
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1index_1to_1pos)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	PangoRectangle _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1index_1to_1pos_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	pango_layout_index_to_pos((PangoLayout*)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setPangoRectangleFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1index_1to_1pos_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1iter_1free
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1iter_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1free_FUNC);
	pango_layout_iter_free((PangoLayoutIter*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1free_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1iter_1get_1index
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1layout_1iter_1get_1index)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1get_1index_FUNC);
	rc = (jint)pango_layout_iter_get_index((PangoLayoutIter*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1get_1index_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1iter_1get_1line_1extents
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1iter_1get_1line_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jobject arg2)
{
	PangoRectangle _arg1, *lparg1=NULL;
	PangoRectangle _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1get_1line_1extents_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	pango_layout_iter_get_line_extents((PangoLayoutIter*)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setPangoRectangleFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPangoRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1get_1line_1extents_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1iter_1get_1run
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1iter_1get_1run)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1get_1run_FUNC);
	rc = (jintLong)pango_layout_iter_get_run((PangoLayoutIter*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1get_1run_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1iter_1next_1line
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1layout_1iter_1next_1line)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1next_1line_FUNC);
	rc = (jboolean)pango_layout_iter_next_line((PangoLayoutIter*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1next_1line_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1iter_1next_1run
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1layout_1iter_1next_1run)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1iter_1next_1run_FUNC);
	rc = (jboolean)pango_layout_iter_next_run((PangoLayoutIter*)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1iter_1next_1run_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1line_1get_1extents
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1line_1get_1extents)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jobject arg2)
{
	PangoRectangle _arg1, *lparg1=NULL;
	PangoRectangle _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1line_1get_1extents_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	pango_layout_line_get_extents((PangoLayoutLine*)arg0, (PangoRectangle *)lparg1, (PangoRectangle *)lparg2);
fail:
	if (arg2 && lparg2) setPangoRectangleFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPangoRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1line_1get_1extents_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1line_1x_1to_1index
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1layout_1line_1x_1to_1index)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1line_1x_1to_1index_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)pango_layout_line_x_to_index((PangoLayoutLine*)arg0, arg1, (int *)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1line_1x_1to_1index_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1layout_1new)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1new_FUNC);
	rc = (jintLong)pango_layout_new((PangoContext *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1layout_1set_1alignment
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1alignment)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1alignment_FUNC);
	pango_layout_set_alignment((PangoLayout *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1alignment_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1attributes
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1attributes)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1attributes_FUNC);
	pango_layout_set_attributes((PangoLayout *)arg0, (PangoAttrList *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1attributes_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1auto_1dir
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1auto_1dir)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1auto_1dir_FUNC);
/*
	pango_layout_set_auto_dir(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, pango_layout_set_auto_dir)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jboolean))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1auto_1dir_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1font_1description
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1font_1description)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1font_1description_FUNC);
	pango_layout_set_font_description((PangoLayout *)arg0, (PangoFontDescription *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1font_1description_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1indent
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1indent)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1indent_FUNC);
	pango_layout_set_indent((PangoLayout*)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1indent_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1justify
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1justify)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1justify_FUNC);
	pango_layout_set_justify((PangoLayout*)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1justify_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1single_1paragraph_1mode
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1single_1paragraph_1mode)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1single_1paragraph_1mode_FUNC);
	pango_layout_set_single_paragraph_mode((PangoLayout *)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1single_1paragraph_1mode_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1spacing
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1spacing)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1spacing_FUNC);
	pango_layout_set_spacing((PangoLayout *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1spacing_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1tabs
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1tabs)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1tabs_FUNC);
	pango_layout_set_tabs((PangoLayout *)arg0, (PangoTabArray *)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1tabs_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1text
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1text)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1text_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	pango_layout_set_text((PangoLayout *)arg0, (const char *)lparg1, (int)arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1text_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1width
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1width)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1width_FUNC);
	pango_layout_set_width((PangoLayout *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1width_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1set_1wrap
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1layout_1set_1wrap)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1set_1wrap_FUNC);
	pango_layout_set_wrap((PangoLayout *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1set_1wrap_FUNC);
}
#endif

#ifndef NO__1pango_1layout_1xy_1to_1index
JNIEXPORT jboolean JNICALL OS_NATIVE(_1pango_1layout_1xy_1to_1index)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1layout_1xy_1to_1index_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)pango_layout_xy_to_index((PangoLayout *)arg0, arg1, arg2, (int *)lparg3, (int *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1layout_1xy_1to_1index_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1tab_1array_1free
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1tab_1array_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, _1pango_1tab_1array_1free_FUNC);
	pango_tab_array_free((PangoTabArray *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1tab_1array_1free_FUNC);
}
#endif

#ifndef NO__1pango_1tab_1array_1get_1size
JNIEXPORT jint JNICALL OS_NATIVE(_1pango_1tab_1array_1get_1size)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1tab_1array_1get_1size_FUNC);
	rc = (jint)pango_tab_array_get_size((PangoTabArray *)arg0);
	OS_NATIVE_EXIT(env, that, _1pango_1tab_1array_1get_1size_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1tab_1array_1get_1tabs
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1tab_1array_1get_1tabs)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1pango_1tab_1array_1get_1tabs_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	pango_tab_array_get_tabs((PangoTabArray *)arg0, (PangoTabAlign **)lparg1, (int **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1pango_1tab_1array_1get_1tabs_FUNC);
}
#endif

#ifndef NO__1pango_1tab_1array_1new
JNIEXPORT jintLong JNICALL OS_NATIVE(_1pango_1tab_1array_1new)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1pango_1tab_1array_1new_FUNC);
	rc = (jintLong)pango_tab_array_new((gint)arg0, (gboolean)arg1);
	OS_NATIVE_EXIT(env, that, _1pango_1tab_1array_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1pango_1tab_1array_1set_1tab
JNIEXPORT void JNICALL OS_NATIVE(_1pango_1tab_1array_1set_1tab)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1pango_1tab_1array_1set_1tab_FUNC);
	pango_tab_array_set_tab((PangoTabArray *)arg0, (gint)arg1, (PangoTabAlign)arg2, (gint)arg3);
	OS_NATIVE_EXIT(env, that, _1pango_1tab_1array_1set_1tab_FUNC);
}
#endif

#ifndef NO_g_1main_1context_1wakeup
JNIEXPORT void JNICALL OS_NATIVE(g_1main_1context_1wakeup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, g_1main_1context_1wakeup_FUNC);
	g_main_context_wakeup((GMainContext *)arg0);
	OS_NATIVE_EXIT(env, that, g_1main_1context_1wakeup_FUNC);
}
#endif

#ifndef NO_g_1value_1get_1double
JNIEXPORT jdouble JNICALL OS_NATIVE(g_1value_1get_1double)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, g_1value_1get_1double_FUNC);
	rc = (jdouble)g_value_get_double((GValue *)arg0);
	OS_NATIVE_EXIT(env, that, g_1value_1get_1double_FUNC);
	return rc;
}
#endif

#ifndef NO_g_1value_1get_1float
JNIEXPORT jfloat JNICALL OS_NATIVE(g_1value_1get_1float)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jfloat rc = 0;
	OS_NATIVE_ENTER(env, that, g_1value_1get_1float_FUNC);
	rc = (jfloat)g_value_get_float((GValue *)arg0);
	OS_NATIVE_EXIT(env, that, g_1value_1get_1float_FUNC);
	return rc;
}
#endif

#ifndef NO_g_1value_1get_1int
JNIEXPORT jint JNICALL OS_NATIVE(g_1value_1get_1int)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, g_1value_1get_1int_FUNC);
	rc = (jint)g_value_get_int((GValue *)arg0);
	OS_NATIVE_EXIT(env, that, g_1value_1get_1int_FUNC);
	return rc;
}
#endif

#ifndef NO_g_1value_1get_1int64
JNIEXPORT jlong JNICALL OS_NATIVE(g_1value_1get_1int64)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, g_1value_1get_1int64_FUNC);
	rc = (jlong)g_value_get_int64((GValue *)arg0);
	OS_NATIVE_EXIT(env, that, g_1value_1get_1int64_FUNC);
	return rc;
}
#endif

#ifndef NO_g_1value_1init
JNIEXPORT jintLong JNICALL OS_NATIVE(g_1value_1init)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, g_1value_1init_FUNC);
	rc = (jintLong)g_value_init((GValue *)arg0, (GType)arg1);
	OS_NATIVE_EXIT(env, that, g_1value_1init_FUNC);
	return rc;
}
#endif

#ifndef NO_g_1value_1set_1double
JNIEXPORT void JNICALL OS_NATIVE(g_1value_1set_1double)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	OS_NATIVE_ENTER(env, that, g_1value_1set_1double_FUNC);
	g_value_set_double((GValue *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, g_1value_1set_1double_FUNC);
}
#endif

#ifndef NO_g_1value_1set_1float
JNIEXPORT void JNICALL OS_NATIVE(g_1value_1set_1float)
	(JNIEnv *env, jclass that, jintLong arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, g_1value_1set_1float_FUNC);
	g_value_set_float((GValue *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, g_1value_1set_1float_FUNC);
}
#endif

#ifndef NO_g_1value_1set_1int
JNIEXPORT void JNICALL OS_NATIVE(g_1value_1set_1int)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, g_1value_1set_1int_FUNC);
	g_value_set_int((GValue *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, g_1value_1set_1int_FUNC);
}
#endif

#ifndef NO_g_1value_1set_1int64
JNIEXPORT void JNICALL OS_NATIVE(g_1value_1set_1int64)
	(JNIEnv *env, jclass that, jintLong arg0, jlong arg1)
{
	OS_NATIVE_ENTER(env, that, g_1value_1set_1int64_FUNC);
	g_value_set_int64((GValue *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, g_1value_1set_1int64_FUNC);
}
#endif

#ifndef NO_g_1value_1unset
JNIEXPORT void JNICALL OS_NATIVE(g_1value_1unset)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, g_1value_1unset_FUNC);
	g_value_unset((GValue *)arg0);
	OS_NATIVE_EXIT(env, that, g_1value_1unset_FUNC);
}
#endif

#ifndef NO_gdk_1threads_1enter
JNIEXPORT void JNICALL OS_NATIVE(gdk_1threads_1enter)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, gdk_1threads_1enter_FUNC);
	gdk_threads_enter();
	OS_NATIVE_EXIT(env, that, gdk_1threads_1enter_FUNC);
}
#endif

#ifndef NO_gdk_1threads_1init
JNIEXPORT void JNICALL OS_NATIVE(gdk_1threads_1init)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, gdk_1threads_1init_FUNC);
	gdk_threads_init();
	OS_NATIVE_EXIT(env, that, gdk_1threads_1init_FUNC);
}
#endif

#ifndef NO_gdk_1threads_1leave
JNIEXPORT void JNICALL OS_NATIVE(gdk_1threads_1leave)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, gdk_1threads_1leave_FUNC);
	gdk_threads_leave();
	OS_NATIVE_EXIT(env, that, gdk_1threads_1leave_FUNC);
}
#endif

#ifndef NO_gdk_1threads_1set_1lock_1functions
JNIEXPORT void JNICALL OS_NATIVE(gdk_1threads_1set_1lock_1functions)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, gdk_1threads_1set_1lock_1functions_FUNC);
/*
	gdk_threads_set_lock_functions(arg0, arg1);
*/
	{
		OS_LOAD_FUNCTION(fp, gdk_threads_set_lock_functions)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, gdk_1threads_1set_1lock_1functions_FUNC);
}
#endif

#ifndef NO_localeconv_1decimal_1point
JNIEXPORT jintLong JNICALL OS_NATIVE(localeconv_1decimal_1point)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, localeconv_1decimal_1point_FUNC);
	rc = (jintLong)localeconv_decimal_point();
	OS_NATIVE_EXIT(env, that, localeconv_1decimal_1point_FUNC);
	return rc;
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	GInterfaceInfo _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getGInterfaceInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GInterfaceInfo_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GObjectClass_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GObjectClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GObjectClass_2_FUNC);
#endif
	if (arg1) getGObjectClassFields(env, arg1, (GObjectClass *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GObjectClass_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GObjectClass_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GTypeInfo_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GTypeInfo_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GTypeInfo_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GTypeInfo_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	GTypeInfo _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GTypeInfo_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GTypeInfo_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getGTypeInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GTypeInfo_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GTypeInfo_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GdkEventButton_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GdkEventButton_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	GdkEventButton _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventButton_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getGdkEventButtonFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventButton_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GdkEventExpose_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GdkEventExpose_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	GdkEventExpose _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventExpose_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getGdkEventExposeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventExpose_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GdkEventMotion_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GdkEventMotion_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	GdkEventMotion _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventMotion_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getGdkEventMotionFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GdkEventMotion_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkAdjustment_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GtkAdjustment_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkAdjustment_2_FUNC);
#endif
	if (arg1) getGtkAdjustmentFields(env, arg1, (GtkAdjustment *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkAdjustment_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2_FUNC);
#endif
	if (arg1) getGtkCellRendererClassFields(env, arg1, (GtkCellRendererClass *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkFixed_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GtkFixed_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkFixed_2_FUNC);
#endif
	if (arg1) getGtkFixedFields(env, arg1, (GtkFixed *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkFixed_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkFixed_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	GtkTargetEntry _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getGtkTargetEntryFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkWidgetClass_2) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkWidgetClass_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_GtkWidgetClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_GtkWidgetClass_2)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkWidgetClass_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkWidgetClass_2_FUNC);
#endif
	if (arg1) getGtkWidgetClassFields(env, arg1, (GtkWidgetClass *)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_GtkWidgetClass_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_GtkWidgetClass_2_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_PangoAttribute_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_PangoAttribute_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_PangoAttribute_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_PangoAttribute_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	PangoAttribute _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_PangoAttribute_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_PangoAttribute_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getPangoAttributeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_PangoAttribute_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_PangoAttribute_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_XClientMessageEvent_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_XClientMessageEvent_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_XClientMessageEvent_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_XClientMessageEvent_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	XClientMessageEvent _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XClientMessageEvent_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XClientMessageEvent_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getXClientMessageEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XClientMessageEvent_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XClientMessageEvent_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_XCrossingEvent_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_XCrossingEvent_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_XCrossingEvent_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_XCrossingEvent_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	XCrossingEvent _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XCrossingEvent_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XCrossingEvent_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getXCrossingEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XCrossingEvent_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XCrossingEvent_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	XExposeEvent _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getXExposeEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	XFocusChangeEvent _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getXFocusChangeEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2J_FUNC);
#endif
	if (arg0) setGObjectClassFields(env, arg0, (GObjectClass *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GObjectClass_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GTypeQuery _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGTypeQueryFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GTypeQuery_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkColor _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkColorFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkDragContext _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkDragContextFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventAny _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventAnyFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventAny_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventButton _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventButtonFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventCrossing _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventCrossingFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventExpose _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventExposeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventFocus _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventFocusFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventKey _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventKeyFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventMotion _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventMotionFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2J_FUNC);
#endif
	if (arg0) setGdkEventPropertyFields(env, arg0, (GdkEventProperty *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventProperty_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventScroll _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventScrollFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventScroll_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventVisibility _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventVisibilityFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventVisibility_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEventWindowState _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventWindowStateFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkEvent _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkEventFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkEvent_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2J_FUNC);
#endif
	if (arg0) setGdkImageFields(env, arg0, (GdkImage *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GdkRectangle _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGdkRectangleFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2J_FUNC);
#endif
	if (arg0) setGdkVisualFields(env, arg0, (GdkVisual *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2J_FUNC);
#endif
	if (arg0) setGtkAdjustmentFields(env, arg0, (GtkAdjustment *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GtkBorder _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGtkBorderFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkBorder_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2J_FUNC);
#endif
	if (arg0) setGtkCellRendererClassFields(env, arg0, (GtkCellRendererClass *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCellRendererClass_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2J_FUNC);
#endif
	if (arg0) setGtkColorSelectionDialogFields(env, arg0, (GtkColorSelectionDialog *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2J_FUNC);
#endif
	if (arg0) setGtkComboFields(env, arg0, (GtkCombo *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2J_FUNC);
#endif
	if (arg0) setGtkFileSelectionFields(env, arg0, (GtkFileSelection *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2J_FUNC);
#endif
	if (arg0) setGtkFixedFields(env, arg0, (GtkFixed *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkFixed_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GtkSelectionData _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGtkSelectionDataFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	GtkTargetPair _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGtkTargetPairFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkTargetPair_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2I) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2J_FUNC);
#endif
	if (arg0) setGtkWidgetClassFields(env, arg0, (GtkWidgetClass *)arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_GtkWidgetClass_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoAttrColor _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoAttrColorFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrColor_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoAttrInt _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoAttrIntFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttrInt_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoAttribute _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoAttributeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoAttribute_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoItem _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoItemFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoItem_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoLayoutLine _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoLayoutLineFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutLine_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoLayoutRun _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoLayoutRunFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLayoutRun_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	PangoLogAttr _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPangoLogAttrFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_PangoLogAttr_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	XCrossingEvent _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXCrossingEventFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	XExposeEvent _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXExposeEventFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	XFocusChangeEvent _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXFocusChangeEventFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	XVisibilityEvent _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXVisibilityEventFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2JJ_FUNC);
#endif
}
#endif

#ifndef NO_realpath
JNIEXPORT jintLong JNICALL OS_NATIVE(realpath)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, realpath_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)realpath((const char *)lparg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, realpath_FUNC);
	return rc;
}
#endif


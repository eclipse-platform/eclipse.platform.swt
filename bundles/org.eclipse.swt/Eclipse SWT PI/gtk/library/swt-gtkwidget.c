/*
 * Copyright (c) IBM Corp. 2000, 2002.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

/**
 * SWT OS natives implementation: gtk_widget_* functions.
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

/* MACROS */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1TYPE
  (JNIEnv *env, jclass that, jint wid)
{
	return GTK_WIDGET_TYPE((GtkWidget*)wid);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1FLAGS
  (JNIEnv *env, jclass that, jint wid)
{
	return (jint) GTK_WIDGET_FLAGS((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1HAS_1FOCUS
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_HAS_FOCUS((GtkWidget*)wid);
}

/* Temporary code.  These private fields should not be accessed at all. */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1WINDOW
  (JNIEnv *env, jclass that, jint wid)
{
	return (jint) ((GtkWidget*)wid)->window;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1PARENT
  (JNIEnv *env, jclass that, jint wid)
{
	return (jint) ((GtkWidget*)wid)->parent;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SET_1FLAGS
  (JNIEnv *env, jclass that, jint wid, jint flag)
{
	GTK_WIDGET_SET_FLAGS((GtkWidget*)wid, flag);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1UNSET_1FLAGS
  (JNIEnv *env, jclass that, jint wid, jint flag)
{
	GTK_WIDGET_UNSET_FLAGS((GtkWidget*)wid, flag);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1NO_1WINDOW
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_NO_WINDOW((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SENSITIVE
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_SENSITIVE((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1IS_1SENSITIVE
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_IS_SENSITIVE((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1TOPLEVEL
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_TOPLEVEL((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1REALISED
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_REALISED((GtkWidget*)wid);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1VISIBLE
  (JNIEnv *env, jclass that, jint wid)
{
	return (jboolean) GTK_WIDGET_VISIBLE((GtkWidget*)wid);
}

/* Functions */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1destroy
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_destroy((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_show((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1now
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_show_now((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1all
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_show_all((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1hide
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_hide((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1realize
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_realize((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1queue_1draw
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_queue_draw((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1request
  (JNIEnv *env, jclass that, jint widget, jobject requisition)
{
	DECL_GLOB(pGlob)
	GtkRequisition requisition_struct, *requisition1 = NULL;
	if (requisition) {
		requisition1 = &requisition_struct;
		cacheGtkRequisitionFids(env, requisition, &PGLOB(GtkRequisitionFc));
		getGtkRequisitionFields(env, requisition, requisition1, &PGLOB(GtkRequisitionFc));
	}
	gtk_widget_size_request((GtkWidget*)widget, (GtkRequisition*)requisition1);
	if (requisition) {
		setGtkRequisitionFields(env, requisition, requisition1, &PGLOB(GtkRequisitionFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1allocate
  (JNIEnv *env, jclass that, jint widget, jobject allocation)
{
	DECL_GLOB(pGlob)
	GtkAllocation allocation_struct, *allocation1 = NULL;
	if (allocation) {
		allocation1 = &allocation_struct;
		cacheGtkAllocationFids(env, allocation, &PGLOB(GtkAllocationFc));
		getGtkAllocationFields(env, allocation, allocation1, &PGLOB(GtkAllocationFc));
	}
	gtk_widget_size_allocate((GtkWidget*)widget, (GtkAllocation*)allocation1);
	if (allocation) {
		setGtkAllocationFields(env, allocation, allocation1, &PGLOB(GtkAllocationFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1accelerator
  (JNIEnv *env, jclass that, jint widget, jbyteArray accel_signal, jint accel_group, jint accel_key, jint accel_mods, jint accel_flags)
{
	jbyte *accel_signal1 = NULL;
	if (accel_signal) {
		accel_signal1 = (*env)->GetByteArrayElements(env, accel_signal, NULL);
	}
	gtk_widget_add_accelerator((GtkWidget*)widget, (gchar*)accel_signal1, (GtkAccelGroup*)accel_group, accel_key, accel_mods, (GtkAccelFlags)accel_flags);
	if (accel_signal) {
		(*env)->ReleaseByteArrayElements(env, accel_signal, accel_signal1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1remove_1accelerator
  (JNIEnv *env, jclass that, jint widget, jint accel_group, jint accel_key, jint accel_mods)
{
	gtk_widget_remove_accelerator((GtkWidget*)widget, (GtkAccelGroup*)accel_group, accel_key, accel_mods);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1event
  (JNIEnv *env, jclass that, jint widget, jint event)
{
	return (jint)gtk_widget_event((GtkWidget*)widget, (GdkEvent*)event);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1reparent
  (JNIEnv *env, jclass that, jint widget, jint new_parent)
{
	gtk_widget_reparent((GtkWidget*)widget, (GtkWidget*)new_parent);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1grab_1focus
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_grab_focus((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1state
  (JNIEnv *env, jclass that, jint widget, jint state)
{
	gtk_widget_set_state((GtkWidget*)widget, (GtkStateType)state);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1sensitive
  (JNIEnv *env, jclass that, jint widget, jboolean sensitive)
{
	gtk_widget_set_sensitive((GtkWidget*)widget, (gboolean)sensitive);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1parent
  (JNIEnv *env, jclass that, jint widget, jint parent)
{
	gtk_widget_set_parent((GtkWidget*)widget, (GtkWidget*)parent);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1uposition
  (JNIEnv *env, jclass that, jint widget, jint x, jint y)
{
	gtk_widget_set_uposition((GtkWidget*)widget, (gint)x, (gint)y);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1usize
  (JNIEnv *env, jclass that, jint widget, jint width, jint height)
{
	gtk_widget_set_usize((GtkWidget*)widget, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1events
  (JNIEnv *env, jclass that, jint widget, jint events)
{
	gtk_widget_add_events((GtkWidget*)widget, (gint)events);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1style
  (JNIEnv *env, jclass that, jint widget, jint style)
{
	gtk_widget_set_style((GtkWidget*)widget, (GtkStyle*)style);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1ensure_1style
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_ensure_style((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1font
  (JNIEnv *env, jclass that, jint widget, jint font)
{
	gtk_widget_modify_font((GtkWidget*)widget, (PangoFontDescription*)font);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1style
  (JNIEnv *env, jclass that, jint widget)
{
	return (jint)gtk_widget_get_style((GtkWidget*)widget);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1default_1style
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_widget_get_default_style();
}


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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1pango_1context
  (JNIEnv *env, jclass that, jint widget)
{
	return (jint)gtk_widget_get_pango_context((GtkWidget *)widget);
}

/*
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1translate_1coordinates
  (JNIEnv *env, jclass that, jint src_widget, jint dest_widget, jint src_x, jint src_y, jintArray dest_x, jintArray dest_y)
{
	jboolean result;
	jint * dest_x1 = NULL;
	jint * dest_y1 = NULL;
	if (dest_x) {
		dest_x1 = (*env)->GetIntArrayElements(env, dest_x, NULL);
	}
	if (dest_y) {
		dest_y1 = (*env)->GetIntArrayElements(env, dest_y, NULL);
	}
	result = (jboolean) gtk_widget_translate_coordinates((GtkWidget*)src_widget, (GtkWidget*)dest_widget, (gint)src_x, (gint) src_y, (gint *)dest_x1, (gint *)dest_y1);
	if (dest_x) {
		(*env)->ReleaseIntArrayElements(env, dest_x, dest_x1, 0);
	}
	if (dest_y) {
		(*env)->ReleaseIntArrayElements(env, dest_y, dest_y1, 0);
	}
	return result;
}
*/

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1size_1request
  (JNIEnv *env, jclass that, jint widget, jint width, jint height)
{
	gtk_widget_set_size_request((GtkWidget*)widget, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1size_1request
  (JNIEnv *env, jclass that, jint widget, jintArray width, jintArray height)
{
	jboolean result;
	jint * width1 = NULL;
	jint * height1 = NULL;
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (height) {
		height1 = (*env)->GetIntArrayElements(env, height, NULL);
	}
	gtk_widget_get_size_request((GtkWidget*)widget, (gint *)width1, (gint *)height1);
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (height) {
		(*env)->ReleaseIntArrayElements(env, height, height1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1child_1requisition
  (JNIEnv *env, jclass that, jint widget, jobject requisition)
{
	DECL_GLOB(pGlob)
	GtkRequisition requisition_struct, *requisition1 = NULL;
	if (requisition) {
		requisition1 = &requisition_struct;
		cacheGtkRequisitionFids(env, requisition, &PGLOB(GtkRequisitionFc));
		getGtkRequisitionFields(env, requisition, requisition1, &PGLOB(GtkRequisitionFc));
	}
	gtk_widget_get_child_requisition((GtkWidget*)widget, (GtkRequisition*)requisition1);
	if (requisition) {
		setGtkRequisitionFields(env, requisition, requisition1, &PGLOB(GtkRequisitionFc));
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1X
  (JNIEnv *env, jclass that, jint wid)
{
	return ((GtkWidget*)wid)->allocation.x;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1Y
  (JNIEnv *env, jclass that, jint wid)
{
	return ((GtkWidget*)wid)->allocation.y;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1WIDTH
  (JNIEnv *env, jclass that, jint wid)
{
	return ((GtkWidget*)wid)->allocation.width;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1HEIGHT
  (JNIEnv *env, jclass that, jint wid)
{
	return ((GtkWidget*)wid)->allocation.height;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1resize_1children
  (JNIEnv *env, jclass that, jint container)
{
	gtk_container_resize_children((GtkContainer*)container);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1set_1resize_1mode
  (JNIEnv *env, jclass that, jint container, jint mode)
{
	gtk_container_set_resize_mode((GtkContainer*)container, (gint)mode);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_fixed_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1move
  (JNIEnv *env, jclass that, jint fixed, jint child, jint x, jint y)
{
	gtk_fixed_move((GtkFixed*)fixed, (GtkWidget*)child, (gint)x, (gint)y);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1set_1has_1window
  (JNIEnv *env, jclass that, jint fixed, jboolean has_window)
{
	gtk_fixed_set_has_window((GtkFixed*)fixed, (gboolean)has_window);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1add_1with_1viewport
  (JNIEnv *env, jclass that, jint scrolled_window, jint child)
{
	gtk_scrolled_window_add_with_viewport((GtkScrolledWindow*)scrolled_window, (GtkWidget*)child);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1BIN_1SET_1CHILD
  (JNIEnv *env, jclass that, jint bin, jint child)
{
	((GtkBin*)bin)->child = (GtkWidget*)child;
}

JNIEXPORT jdouble JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1get_1value
  (JNIEnv *env, jclass that, jint widget)
{
	return (jdouble)gtk_adjustment_get_value((GtkAdjustment*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1bg
  (JNIEnv *env, jclass that, jint widget, jint state, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gtk_widget_modify_bg((GtkWidget*)widget, (GtkStateType)state, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1modify_1fg
  (JNIEnv *env, jclass that, jint widget, jint state, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gtk_widget_modify_fg((GtkWidget*)widget, (GtkStateType)state, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1set_1shadow_1type
  (JNIEnv *env, jclass that, jint scroll, jint type)
{
	gtk_scrolled_window_set_shadow_type((GtkScrolledWindow*)scroll, (GtkShadowType)type);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1set_1editable
  (JNIEnv *env, jclass that, jint entry, jboolean editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_text_set_editable");
#endif

	gtk_text_set_editable((GtkEntry*)entry, (gboolean)editable);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1queue_1resize
  (JNIEnv *env, jclass that, jint widget)
{
	gtk_widget_queue_resize((GtkWidget*)widget);
}


/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_select_region
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1select_1region
  (JNIEnv *env, jclass that, jint editable, jint start, jint end)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_select_region");
#endif

	gtk_editable_select_region((GtkEditable*)editable, (gint)start, (gint)end);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_insert_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1insert_1text
  (JNIEnv *env, jclass that, jint editable, jbyteArray new_text, jint new_text_length, jintArray position)
{
	jbyte *new_text1 = NULL;
	jint *position1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_insert_text");
#endif

	if (new_text) {
		new_text1 = (*env)->GetByteArrayElements(env, new_text, NULL);
	}
	if (position) {
		position1 = (*env)->GetIntArrayElements(env, position, NULL);
	}
	gtk_editable_insert_text((GtkEditable*)editable, (gchar*)new_text1, (gint)new_text_length, (gint*)position1);
	if (new_text) {
		(*env)->ReleaseByteArrayElements(env, new_text, new_text1, 0);
	}
	if (position) {
		(*env)->ReleaseIntArrayElements(env, position, position1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_delete_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1delete_1text
  (JNIEnv *env, jclass that, jint editable, jint start_pos, jint end_pos)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_delete_text");
#endif

	gtk_editable_delete_text((GtkEditable*)editable, (gint)start_pos, (gint)end_pos);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_get_chars
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1chars
  (JNIEnv *env, jclass that, jint editable, jint start_pos, jint end_pos)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_get_chars");
#endif

	return (jint)gtk_editable_get_chars((GtkEditable*)editable, (gint)start_pos, (gint)end_pos);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_delete_selection
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1delete_1selection
  (JNIEnv *env, jclass that, jint editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_delete_selection");
#endif

	gtk_editable_delete_selection((GtkEditable*)editable);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_set_position
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1set_1position
  (JNIEnv *env, jclass that, jint editable, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_set_position");
#endif

	gtk_editable_set_position((GtkEditable*)editable, (gint)position);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_get_position
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1get_1position
  (JNIEnv *env, jclass that, jint editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_get_position");
#endif

	return (jint)gtk_editable_get_position((GtkEditable*)editable);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_editable_set_editable
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1editable_1set_1editable
  (JNIEnv *env, jclass that, jint editable, jboolean is_editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_editable_set_editable");
#endif

	gtk_editable_set_editable((GtkEditable*)editable, (gboolean)is_editable);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1redraw_1on_1allocate
  (JNIEnv *env, jclass that, jint widget, jboolean redraw)
{
	gtk_widget_set_redraw_on_allocate((GtkWidget*)widget, (gboolean)redraw);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1MAPPED
  (JNIEnv *env, jclass that, jint wid)
{
	return GTK_WIDGET_MAPPED((GtkWidget*)wid);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1send_1expose
  (JNIEnv *env, jclass that, jint wid, jint event)
{
	return gtk_widget_send_expose((GtkWidget*)wid, (GdkEvent*)event);
}


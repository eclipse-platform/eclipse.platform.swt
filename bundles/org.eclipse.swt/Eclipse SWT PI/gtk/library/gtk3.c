/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

/* Note: This file was auto-generated by org.eclipse.swt.tools.internal.JNIGenerator */
/* DO NOT EDIT - your changes will be lost. */

#include "swt.h"
#include "gtk3_structs.h"
#include "gtk3_stats.h"

#ifndef GTK3_NATIVE
#define GTK3_NATIVE(func) Java_org_eclipse_swt_internal_gtk3_GTK3_##func
#endif

#ifndef NO_gtk_1accel_1label_1new
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1accel_1label_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1accel_1label_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jlong)gtk_accel_label_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1accel_1label_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1accel_1label_1set_1accel
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1accel_1label_1set_1accel)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1, jint arg2)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1accel_1label_1set_1accel_FUNC);
	gtk_accel_label_set_accel((GtkAccelLabel *)arg0, (guint)arg1, (GdkModifierType)arg2);
	GTK3_NATIVE_EXIT(env, that, gtk_1accel_1label_1set_1accel_FUNC);
}
#endif

#ifndef NO_gtk_1accel_1label_1set_1accel_1widget
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1accel_1label_1set_1accel_1widget)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1accel_1label_1set_1accel_1widget_FUNC);
	gtk_accel_label_set_accel_widget((GtkAccelLabel *)arg0, (GtkWidget *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1accel_1label_1set_1accel_1widget_FUNC);
}
#endif

#ifndef NO_gtk_1bin_1get_1child
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1bin_1get_1child)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1bin_1get_1child_FUNC);
	rc = (jlong)gtk_bin_get_child((GtkBin *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1bin_1get_1child_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1box_1pack_1end
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1box_1pack_1end)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jboolean arg2, jboolean arg3, jint arg4)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1box_1pack_1end_FUNC);
	gtk_box_pack_end((GtkBox *)arg0, (GtkWidget *)arg1, (gboolean)arg2, (gboolean)arg3, (guint)arg4);
	GTK3_NATIVE_EXIT(env, that, gtk_1box_1pack_1end_FUNC);
}
#endif

#ifndef NO_gtk_1box_1reorder_1child
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1box_1reorder_1child)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jint arg2)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1box_1reorder_1child_FUNC);
	gtk_box_reorder_child((GtkBox *)arg0, (GtkWidget *)arg1, (gint)arg2);
	GTK3_NATIVE_EXIT(env, that, gtk_1box_1reorder_1child_FUNC);
}
#endif

#ifndef NO_gtk_1box_1set_1child_1packing
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1box_1set_1child_1packing)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jboolean arg2, jboolean arg3, jint arg4, jint arg5)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1box_1set_1child_1packing_FUNC);
	gtk_box_set_child_packing((GtkBox *)arg0, (GtkWidget *)arg1, arg2, arg3, arg4, arg5);
	GTK3_NATIVE_EXIT(env, that, gtk_1box_1set_1child_1packing_FUNC);
}
#endif

#ifndef NO_gtk_1button_1set_1image
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1button_1set_1image)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1button_1set_1image_FUNC);
	gtk_button_set_image((GtkButton *)arg0, (GtkWidget *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1button_1set_1image_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1get_1date
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1calendar_1get_1date)
	(JNIEnv *env, jclass that, jlong arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	GTK3_NATIVE_ENTER(env, that, gtk_1calendar_1get_1date_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	gtk_calendar_get_date((GtkCalendar *)arg0, (guint *)lparg1, (guint *)lparg2, (guint *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1calendar_1get_1date_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1select_1day
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1calendar_1select_1day)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1calendar_1select_1day_FUNC);
	gtk_calendar_select_day((GtkCalendar *)arg0, (guint)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1calendar_1select_1day_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1select_1month
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1calendar_1select_1month)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1, jint arg2)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1calendar_1select_1month_FUNC);
	gtk_calendar_select_month((GtkCalendar *)arg0, (guint)arg1, (guint)arg2);
	GTK3_NATIVE_EXIT(env, that, gtk_1calendar_1select_1month_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1set_1display_1options
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1calendar_1set_1display_1options)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1calendar_1set_1display_1options_FUNC);
	gtk_calendar_set_display_options((GtkCalendar *)arg0, (GtkCalendarDisplayOptions)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1calendar_1set_1display_1options_FUNC);
}
#endif

#ifndef NO_gtk_1container_1add
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1container_1add)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1add_FUNC);
	gtk_container_add((GtkContainer *)arg0, (GtkWidget *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1add_FUNC);
}
#endif

#ifndef NO_gtk_1container_1forall
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1container_1forall)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1forall_FUNC);
	gtk_container_forall((GtkContainer *)arg0, (GtkCallback)arg1, (gpointer)arg2);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1forall_FUNC);
}
#endif

#ifndef NO_gtk_1container_1get_1border_1width
JNIEXPORT jint JNICALL GTK3_NATIVE(gtk_1container_1get_1border_1width)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jint rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1get_1border_1width_FUNC);
	rc = (jint)gtk_container_get_border_width((GtkContainer *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1get_1border_1width_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1container_1get_1children
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1container_1get_1children)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1get_1children_FUNC);
	rc = (jlong)gtk_container_get_children((GtkContainer *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1get_1children_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1container_1propagate_1draw
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1container_1propagate_1draw)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1propagate_1draw_FUNC);
	gtk_container_propagate_draw((GtkContainer *)arg0, (GtkWidget *)arg1, (cairo_t *)arg2);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1propagate_1draw_FUNC);
}
#endif

#ifndef NO_gtk_1container_1remove
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1container_1remove)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1remove_FUNC);
	gtk_container_remove((GtkContainer *)arg0, (GtkWidget *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1remove_FUNC);
}
#endif

#ifndef NO_gtk_1container_1set_1border_1width
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1container_1set_1border_1width)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1container_1set_1border_1width_FUNC);
	gtk_container_set_border_width((GtkContainer *)arg0, (guint)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1container_1set_1border_1width_FUNC);
}
#endif

#ifndef NO_gtk_1dialog_1run
JNIEXPORT jint JNICALL GTK3_NATIVE(gtk_1dialog_1run)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jint rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1dialog_1run_FUNC);
	rc = (jint)gtk_dialog_run((GtkDialog *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1dialog_1run_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1drag_1begin_1with_1coordinates
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1drag_1begin_1with_1coordinates)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jint arg2, jint arg3, jlong arg4, jint arg5, jint arg6)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1begin_1with_1coordinates_FUNC);
	rc = (jlong)gtk_drag_begin_with_coordinates((GtkWidget *)arg0, (GtkTargetList *)arg1, (GdkDragAction)arg2, (gint)arg3, (GdkEvent *)arg4, (gint)arg5, (gint)arg6);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1begin_1with_1coordinates_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1drag_1check_1threshold
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1drag_1check_1threshold)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1check_1threshold_FUNC);
	rc = (jboolean)gtk_drag_check_threshold((GtkWidget *)arg0, (gint)arg1, (gint)arg2, (gint)arg3, (gint)arg4);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1check_1threshold_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1drag_1dest_1set
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1drag_1dest_1set)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1, jlong arg2, jint arg3, jint arg4)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1dest_1set_FUNC);
	gtk_drag_dest_set((GtkWidget *)arg0, (GtkDestDefaults)arg1, (const GtkTargetEntry *)arg2, (gint)arg3, (GdkDragAction)arg4);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1dest_1set_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1dest_1unset
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1drag_1dest_1unset)
	(JNIEnv *env, jclass that, jlong arg0)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1dest_1unset_FUNC);
	gtk_drag_dest_unset((GtkWidget *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1dest_1unset_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1finish
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1drag_1finish)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1, jboolean arg2, jint arg3)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1finish_FUNC);
	gtk_drag_finish((GdkDragContext *)arg0, (gboolean)arg1, (gboolean)arg2, (guint32)arg3);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1finish_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1get_1data
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1drag_1get_1data)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, jint arg3)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1get_1data_FUNC);
	gtk_drag_get_data((GtkWidget *)arg0, (GdkDragContext *)arg1, (GdkAtom)arg2, (guint32)arg3);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1get_1data_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1set_1icon_1surface
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1drag_1set_1icon_1surface)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1drag_1set_1icon_1surface_FUNC);
	gtk_drag_set_icon_surface((GdkDragContext *)arg0, (cairo_surface_t *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1drag_1set_1icon_1surface_FUNC);
}
#endif

#ifndef NO_gtk_1events_1pending
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1events_1pending)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1events_1pending_FUNC);
	rc = (jboolean)gtk_events_pending();
	GTK3_NATIVE_EXIT(env, that, gtk_1events_1pending_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1filename
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1file_1chooser_1get_1filename)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1filename_FUNC);
	rc = (jlong)gtk_file_chooser_get_filename((GtkFileChooser *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1filename_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1filenames
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1file_1chooser_1get_1filenames)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1filenames_FUNC);
	rc = (jlong)gtk_file_chooser_get_filenames((GtkFileChooser *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1filenames_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1uri
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1file_1chooser_1get_1uri)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1uri_FUNC);
	rc = (jlong)gtk_file_chooser_get_uri((GtkFileChooser *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1uris
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1file_1chooser_1get_1uris)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1uris_FUNC);
	rc = (jlong)gtk_file_chooser_get_uris((GtkFileChooser *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1uris_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1current_1folder
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1current_1folder)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1current_1folder_FUNC);
	gtk_file_chooser_set_current_folder((GtkFileChooser *)arg0, (const gchar *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1current_1folder_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1current_1folder_1uri
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1current_1folder_1uri)
	(JNIEnv *env, jclass that, jlong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1current_1folder_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_file_chooser_set_current_folder_uri((GtkFileChooser *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1current_1folder_1uri_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1do_1overwrite_1confirmation
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1do_1overwrite_1confirmation)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1do_1overwrite_1confirmation_FUNC);
	gtk_file_chooser_set_do_overwrite_confirmation((GtkFileChooser *)arg0, (gboolean)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1do_1overwrite_1confirmation_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1extra_1widget
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1extra_1widget)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1extra_1widget_FUNC);
	gtk_file_chooser_set_extra_widget((GtkFileChooser *)arg0, (GtkWidget *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1extra_1widget_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1filename
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1filename)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1filename_FUNC);
	gtk_file_chooser_set_filename((GtkFileChooser *)arg0, (const gchar *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1filename_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1local_1only
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1local_1only)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1local_1only_FUNC);
	gtk_file_chooser_set_local_only((GtkFileChooser *)arg0, (gboolean)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1local_1only_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1uri
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1file_1chooser_1set_1uri)
	(JNIEnv *env, jclass that, jlong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	GTK3_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gtk_file_chooser_set_uri((GtkFileChooser *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1uri_FUNC);
}
#endif

#ifndef NO_gtk_1get_1current_1event
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1get_1current_1event)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1get_1current_1event_FUNC);
	rc = (jlong)gtk_get_current_event();
	GTK3_NATIVE_EXIT(env, that, gtk_1get_1current_1event_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1get_1current_1event_1state
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1get_1current_1event_1state)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1get_1current_1event_1state_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_get_current_event_state((GdkModifierType*)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1get_1current_1event_1state_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1get_1event_1widget
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1get_1event_1widget)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1get_1event_1widget_FUNC);
	rc = (jlong)gtk_get_event_widget((GdkEvent *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1get_1event_1widget_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1grab_1add
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1grab_1add)
	(JNIEnv *env, jclass that, jlong arg0)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1grab_1add_FUNC);
	gtk_grab_add((GtkWidget *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1grab_1add_FUNC);
}
#endif

#ifndef NO_gtk_1grab_1get_1current
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1grab_1get_1current)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1grab_1get_1current_FUNC);
	rc = (jlong)gtk_grab_get_current();
	GTK3_NATIVE_EXIT(env, that, gtk_1grab_1get_1current_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1grab_1remove
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1grab_1remove)
	(JNIEnv *env, jclass that, jlong arg0)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1grab_1remove_FUNC);
	gtk_grab_remove((GtkWidget *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1grab_1remove_FUNC);
}
#endif

#ifndef NO_gtk_1hsv_1to_1rgb
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1hsv_1to_1rgb)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdoubleArray arg3, jdoubleArray arg4, jdoubleArray arg5)
{
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	jdouble *lparg5=NULL;
	GTK3_NATIVE_ENTER(env, that, gtk_1hsv_1to_1rgb_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetDoubleArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_hsv_to_rgb((gdouble)arg0, (gdouble)arg1, (gdouble)arg2, (gdouble *)lparg3, (gdouble *)lparg4, (gdouble *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseDoubleArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1hsv_1to_1rgb_FUNC);
}
#endif

#ifndef NO_gtk_1im_1context_1filter_1keypress
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1im_1context_1filter_1keypress)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1im_1context_1filter_1keypress_FUNC);
	rc = (jboolean)gtk_im_context_filter_keypress((GtkIMContext *)arg0, (GdkEventKey *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1im_1context_1filter_1keypress_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1init_1check
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1init_1check)
	(JNIEnv *env, jclass that, jlongArray arg0, jlongArray arg1)
{
	jlong *lparg0=NULL;
	jlong *lparg1=NULL;
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1init_1check_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)gtk_init_check((int *)lparg0, (char ***)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseLongArrayElements(env, arg0, lparg0, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1init_1check_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1main_1do_1event
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1main_1do_1event)
	(JNIEnv *env, jclass that, jlong arg0)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1main_1do_1event_FUNC);
	gtk_main_do_event((GdkEvent *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1main_1do_1event_FUNC);
}
#endif

#ifndef NO_gtk_1main_1iteration_1do
JNIEXPORT jboolean JNICALL GTK3_NATIVE(gtk_1main_1iteration_1do)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jboolean rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1main_1iteration_1do_FUNC);
	rc = (jboolean)gtk_main_iteration_do(arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1main_1iteration_1do_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1rgb_1to_1hsv
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1rgb_1to_1hsv)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdoubleArray arg3, jdoubleArray arg4, jdoubleArray arg5)
{
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	jdouble *lparg5=NULL;
	GTK3_NATIVE_ENTER(env, that, gtk_1rgb_1to_1hsv_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetDoubleArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_rgb_to_hsv((gdouble)arg0, (gdouble)arg1, (gdouble)arg2, (gdouble *)lparg3, (gdouble *)lparg4, (gdouble *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseDoubleArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	GTK3_NATIVE_EXIT(env, that, gtk_1rgb_1to_1hsv_FUNC);
}
#endif

#ifndef NO_gtk_1widget_1get_1accessible
JNIEXPORT jlong JNICALL GTK3_NATIVE(gtk_1widget_1get_1accessible)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK3_NATIVE_ENTER(env, that, gtk_1widget_1get_1accessible_FUNC);
	rc = (jlong)gtk_widget_get_accessible((GtkWidget *)arg0);
	GTK3_NATIVE_EXIT(env, that, gtk_1widget_1get_1accessible_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1window_1set_1icon_1list
JNIEXPORT void JNICALL GTK3_NATIVE(gtk_1window_1set_1icon_1list)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK3_NATIVE_ENTER(env, that, gtk_1window_1set_1icon_1list_FUNC);
	gtk_window_set_icon_list((GtkWindow *)arg0, (GList *)arg1);
	GTK3_NATIVE_EXIT(env, that, gtk_1window_1set_1icon_1list_FUNC);
}
#endif


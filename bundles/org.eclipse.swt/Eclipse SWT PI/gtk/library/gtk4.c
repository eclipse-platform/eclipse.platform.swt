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
#include "gtk4_structs.h"
#include "gtk4_stats.h"

#ifndef GTK4_NATIVE
#define GTK4_NATIVE(func) Java_org_eclipse_swt_internal_gtk4_GTK4_##func
#endif

#ifndef NO_gdk_1content_1formats_1builder_1add_1mime_1type
JNIEXPORT void JNICALL GTK4_NATIVE(gdk_1content_1formats_1builder_1add_1mime_1type)
	(JNIEnv *env, jclass that, jlong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	GTK4_NATIVE_ENTER(env, that, gdk_1content_1formats_1builder_1add_1mime_1type_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	gdk_content_formats_builder_add_mime_type((GdkContentFormatsBuilder *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	GTK4_NATIVE_EXIT(env, that, gdk_1content_1formats_1builder_1add_1mime_1type_FUNC);
}
#endif

#ifndef NO_gdk_1content_1formats_1builder_1free_1to_1formats
JNIEXPORT jlong JNICALL GTK4_NATIVE(gdk_1content_1formats_1builder_1free_1to_1formats)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gdk_1content_1formats_1builder_1free_1to_1formats_FUNC);
	rc = (jlong)gdk_content_formats_builder_free_to_formats((GdkContentFormatsBuilder *)arg0);
	GTK4_NATIVE_EXIT(env, that, gdk_1content_1formats_1builder_1free_1to_1formats_FUNC);
	return rc;
}
#endif

#ifndef NO_gdk_1content_1formats_1builder_1new
JNIEXPORT jlong JNICALL GTK4_NATIVE(gdk_1content_1formats_1builder_1new)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gdk_1content_1formats_1builder_1new_FUNC);
	rc = (jlong)gdk_content_formats_builder_new();
	GTK4_NATIVE_EXIT(env, that, gdk_1content_1formats_1builder_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_gdk_1toplevel_1focus
JNIEXPORT void JNICALL GTK4_NATIVE(gdk_1toplevel_1focus)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	GTK4_NATIVE_ENTER(env, that, gdk_1toplevel_1focus_FUNC);
	gdk_toplevel_focus((GdkToplevel *)arg0, arg1);
	GTK4_NATIVE_EXIT(env, that, gdk_1toplevel_1focus_FUNC);
}
#endif

#ifndef NO_gdk_1toplevel_1get_1state
JNIEXPORT jint JNICALL GTK4_NATIVE(gdk_1toplevel_1get_1state)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jint rc = 0;
	GTK4_NATIVE_ENTER(env, that, gdk_1toplevel_1get_1state_FUNC);
	rc = (jint)gdk_toplevel_get_state((GdkToplevel *)arg0);
	GTK4_NATIVE_EXIT(env, that, gdk_1toplevel_1get_1state_FUNC);
	return rc;
}
#endif

#ifndef NO_gdk_1toplevel_1lower
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gdk_1toplevel_1lower)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gdk_1toplevel_1lower_FUNC);
	rc = (jboolean)gdk_toplevel_lower((GdkToplevel *)arg0);
	GTK4_NATIVE_EXIT(env, that, gdk_1toplevel_1lower_FUNC);
	return rc;
}
#endif

#ifndef NO_gdk_1toplevel_1set_1icon_1list
JNIEXPORT void JNICALL GTK4_NATIVE(gdk_1toplevel_1set_1icon_1list)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gdk_1toplevel_1set_1icon_1list_FUNC);
	gdk_toplevel_set_icon_list((GdkToplevel *)arg0, (GList *)arg1);
	GTK4_NATIVE_EXIT(env, that, gdk_1toplevel_1set_1icon_1list_FUNC);
}
#endif

#ifndef NO_gtk_1box_1append
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1box_1append)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1box_1append_FUNC);
	gtk_box_append((GtkBox *)arg0, (GtkWidget *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1box_1append_FUNC);
}
#endif

#ifndef NO_gtk_1box_1insert_1child_1after
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1box_1insert_1child_1after)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1box_1insert_1child_1after_FUNC);
	gtk_box_insert_child_after((GtkBox *)arg0, (GtkWidget *)arg1, (GtkWidget *)arg2);
	GTK4_NATIVE_EXIT(env, that, gtk_1box_1insert_1child_1after_FUNC);
}
#endif

#ifndef NO_gtk_1box_1prepend
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1box_1prepend)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1box_1prepend_FUNC);
	gtk_box_prepend((GtkBox *)arg0, (GtkWidget *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1box_1prepend_FUNC);
}
#endif

#ifndef NO_gtk_1button_1new_1from_1icon_1name
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1button_1new_1from_1icon_1name)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1button_1new_1from_1icon_1name_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jlong)gtk_button_new_from_icon_name((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GTK4_NATIVE_EXIT(env, that, gtk_1button_1new_1from_1icon_1name_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1button_1set_1child
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1button_1set_1child)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1button_1set_1child_FUNC);
	gtk_button_set_child((GtkButton *)arg0, (GtkWidget *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1button_1set_1child_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1get_1date
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1calendar_1get_1date)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1calendar_1get_1date_FUNC);
	rc = (jlong)gtk_calendar_get_date((GtkCalendar *)arg0);
	GTK4_NATIVE_EXIT(env, that, gtk_1calendar_1get_1date_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1calendar_1select_1day
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1calendar_1select_1day)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1calendar_1select_1day_FUNC);
	gtk_calendar_select_day((GtkCalendar *)arg0, (GDateTime *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1calendar_1select_1day_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1set_1show_1day_1names
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1calendar_1set_1show_1day_1names)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1calendar_1set_1show_1day_1names_FUNC);
	gtk_calendar_set_show_day_names((GtkCalendar *)arg0, (gboolean)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1calendar_1set_1show_1day_1names_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1set_1show_1heading
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1calendar_1set_1show_1heading)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1calendar_1set_1show_1heading_FUNC);
	gtk_calendar_set_show_heading((GtkCalendar *)arg0, (gboolean)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1calendar_1set_1show_1heading_FUNC);
}
#endif

#ifndef NO_gtk_1calendar_1set_1show_1week_1numbers
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1calendar_1set_1show_1week_1numbers)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1calendar_1set_1show_1week_1numbers_FUNC);
	gtk_calendar_set_show_week_numbers((GtkCalendar *)arg0, (gboolean)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1calendar_1set_1show_1week_1numbers_FUNC);
}
#endif

#ifndef NO_gtk_1check_1button_1get_1active
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gtk_1check_1button_1get_1active)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1check_1button_1get_1active_FUNC);
	rc = (jboolean)gtk_check_button_get_active((GtkCheckButton *)arg0);
	GTK4_NATIVE_EXIT(env, that, gtk_1check_1button_1get_1active_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1check_1button_1set_1active
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1check_1button_1set_1active)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1check_1button_1set_1active_FUNC);
	gtk_check_button_set_active((GtkCheckButton *)arg0, arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1check_1button_1set_1active_FUNC);
}
#endif

#ifndef NO_gtk_1check_1button_1set_1group
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1check_1button_1set_1group)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1check_1button_1set_1group_FUNC);
	gtk_check_button_set_group((GtkCheckButton *)arg0, (GtkCheckButton *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1check_1button_1set_1group_FUNC);
}
#endif

#ifndef NO_gtk_1check_1button_1set_1inconsistent
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1check_1button_1set_1inconsistent)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1check_1button_1set_1inconsistent_FUNC);
	gtk_check_button_set_inconsistent((GtkCheckButton *)arg0, arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1check_1button_1set_1inconsistent_FUNC);
}
#endif

#ifndef NO_gtk_1check_1button_1set_1use_1underline
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1check_1button_1set_1use_1underline)
	(JNIEnv *env, jclass that, jlong arg0, jboolean arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1check_1button_1set_1use_1underline_FUNC);
	gtk_check_button_set_use_underline((GtkCheckButton *)arg0, arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1check_1button_1set_1use_1underline_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1source_1new
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1drag_1source_1new)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1drag_1source_1new_FUNC);
	rc = (jlong)gtk_drag_source_new();
	GTK4_NATIVE_EXIT(env, that, gtk_1drag_1source_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1drag_1source_1set_1actions
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1drag_1source_1set_1actions)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1drag_1source_1set_1actions_FUNC);
	gtk_drag_source_set_actions((GtkDragSource *)arg0, (GdkDragAction)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1drag_1source_1set_1actions_FUNC);
}
#endif

#ifndef NO_gtk_1drag_1source_1set_1icon
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1drag_1source_1set_1icon)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jint arg2, jint arg3)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1drag_1source_1set_1icon_FUNC);
	gtk_drag_source_set_icon((GtkDragSource *)arg0, (GdkPaintable *)arg1, arg2, arg3);
	GTK4_NATIVE_EXIT(env, that, gtk_1drag_1source_1set_1icon_FUNC);
}
#endif

#ifndef NO_gtk_1drop_1target_1async_1new
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1drop_1target_1async_1new)
	(JNIEnv *env, jclass that, jlong arg0, jint arg1)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1drop_1target_1async_1new_FUNC);
	rc = (jlong)gtk_drop_target_async_new((GdkContentFormats *)arg0, (GdkDragAction)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1drop_1target_1async_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1drop_1target_1async_1set_1formats
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1drop_1target_1async_1set_1formats)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1drop_1target_1async_1set_1formats_FUNC);
	gtk_drop_target_async_set_formats((GtkDropTargetAsync *)arg0, (GdkContentFormats *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1drop_1target_1async_1set_1formats_FUNC);
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1file
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1file_1chooser_1get_1file)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1file_FUNC);
	rc = (jlong)gtk_file_chooser_get_file((GtkFileChooser *)arg0);
	GTK4_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1file_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1get_1files
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1file_1chooser_1get_1files)
	(JNIEnv *env, jclass that, jlong arg0)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1file_1chooser_1get_1files_FUNC);
	rc = (jlong)gtk_file_chooser_get_files((GtkFileChooser *)arg0);
	GTK4_NATIVE_EXIT(env, that, gtk_1file_1chooser_1get_1files_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1current_1folder
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gtk_1file_1chooser_1set_1current_1folder)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1current_1folder_FUNC);
	rc = (jboolean)gtk_file_chooser_set_current_folder((GtkFileChooser *)arg0, (GFile *)arg1, (GError **)arg2);
	GTK4_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1current_1folder_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1file_1chooser_1set_1file
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gtk_1file_1chooser_1set_1file)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1file_1chooser_1set_1file_FUNC);
	rc = (jboolean)gtk_file_chooser_set_file((GtkFileChooser *)arg0, (GFile *)arg1, (GError **)arg2);
	GTK4_NATIVE_EXIT(env, that, gtk_1file_1chooser_1set_1file_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1hsv_1to_1rgb
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1hsv_1to_1rgb)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloatArray arg3, jfloatArray arg4, jfloatArray arg5)
{
	jfloat *lparg3=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg5=NULL;
	GTK4_NATIVE_ENTER(env, that, gtk_1hsv_1to_1rgb_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetFloatArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_hsv_to_rgb(arg0, arg1, arg2, (float *)lparg3, (float *)lparg4, (float *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseFloatArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	GTK4_NATIVE_EXIT(env, that, gtk_1hsv_1to_1rgb_FUNC);
}
#endif

#ifndef NO_gtk_1im_1context_1filter_1keypress
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gtk_1im_1context_1filter_1keypress)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1im_1context_1filter_1keypress_FUNC);
	rc = (jboolean)gtk_im_context_filter_keypress((GtkIMContext *)arg0, (GdkEvent *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1im_1context_1filter_1keypress_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1init_1check
JNIEXPORT jboolean JNICALL GTK4_NATIVE(gtk_1init_1check)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1init_1check_FUNC);
	rc = (jboolean)gtk_init_check();
	GTK4_NATIVE_EXIT(env, that, gtk_1init_1check_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1picture_1new
JNIEXPORT jlong JNICALL GTK4_NATIVE(gtk_1picture_1new)
	(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	GTK4_NATIVE_ENTER(env, that, gtk_1picture_1new_FUNC);
	rc = (jlong)gtk_picture_new();
	GTK4_NATIVE_EXIT(env, that, gtk_1picture_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_gtk_1picture_1set_1paintable
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1picture_1set_1paintable)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1)
{
	GTK4_NATIVE_ENTER(env, that, gtk_1picture_1set_1paintable_FUNC);
	gtk_picture_set_paintable((GtkPicture *)arg0, (GdkPaintable *)arg1);
	GTK4_NATIVE_EXIT(env, that, gtk_1picture_1set_1paintable_FUNC);
}
#endif

#ifndef NO_gtk_1rgb_1to_1hsv
JNIEXPORT void JNICALL GTK4_NATIVE(gtk_1rgb_1to_1hsv)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloatArray arg3, jfloatArray arg4, jfloatArray arg5)
{
	jfloat *lparg3=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg5=NULL;
	GTK4_NATIVE_ENTER(env, that, gtk_1rgb_1to_1hsv_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetFloatArrayElements(env, arg5, NULL)) == NULL) goto fail;
	gtk_rgb_to_hsv(arg0, arg1, arg2, (float *)lparg3, (float *)lparg4, (float *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseFloatArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	GTK4_NATIVE_EXIT(env, that, gtk_1rgb_1to_1hsv_FUNC);
}
#endif


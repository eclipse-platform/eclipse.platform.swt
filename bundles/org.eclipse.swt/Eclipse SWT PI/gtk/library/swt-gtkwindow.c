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
 * SWT OS natives implementation: gtk_window_* functions.
 * This file also contains all dialog functions (e.g.,
 * gtk_color_selection_*).
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>


/*
 * gtk_window_*
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1new
  (JNIEnv *env, jclass that, jint type)
{
	return (jint)gtk_window_new((GtkWindowType)type);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1title
  (JNIEnv *env, jclass that, jint window, jbyteArray title)
{
	jbyte *title1 = NULL;
	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	gtk_window_set_title((GtkWindow*)window, (gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1policy
  (JNIEnv *env, jclass that, jint window, jint allow_shrink, jint allow_grow, jint auto_shrink)
{
	gtk_window_set_policy((GtkWindow*)window, (gint)allow_shrink, (gint)allow_grow, (gint)auto_shrink);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1resizable
  (JNIEnv *env, jclass that, jint window, jboolean resizable)
{
	gtk_window_set_resizable((GtkWindow*)window, (gboolean)resizable);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1get_1position
  (JNIEnv *env, jclass that, jint window, jintArray px, jintArray py)
{
  jint *px1 = NULL;
  jint *py1 = NULL;
  if (px) px1 = (*env)->GetIntArrayElements(env, px, NULL);
  if (py) py1 = (*env)->GetIntArrayElements(env, py, NULL);
  gtk_window_get_position((GtkWindow*)window, (gint*)px1, (gint*)py1);
  if (px) (*env)->ReleaseIntArrayElements(env, px, px1, 0);
  if (py) (*env)->ReleaseIntArrayElements(env, py, py1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1get_1size
  (JNIEnv *env, jclass that, jint window, jintArray px, jintArray py)
{
  jint *px1 = NULL;
  jint *py1 = NULL;
  if (px) px1 = (*env)->GetIntArrayElements(env, px, NULL);
  if (py) py1 = (*env)->GetIntArrayElements(env, py, NULL);
  gtk_window_get_size((GtkWindow*)window, (gint*)px1, (gint*)py1);
  if (px) (*env)->ReleaseByteArrayElements(env, px, px1, 0);
  if (py) (*env)->ReleaseByteArrayElements(env, py, py1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1move
  (JNIEnv *env, jclass that, jint window, jint x, jint y)
{
	gtk_window_move((GtkWindow*)window, x, y);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1resize
  (JNIEnv *env, jclass that, jint window, jint width, jint height)
{
	gtk_window_resize((GtkWindow*)window, width, height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1transient_1for
  (JNIEnv *env, jclass that, jint window, jint parent)
{
	gtk_window_set_transient_for((GtkWindow*)window, (GtkWindow*)parent);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1modal
  (JNIEnv *env, jclass that, jint window, jboolean modal)
{
	gtk_window_set_modal((GtkWindow*)window, modal);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1add_1accel_1group
  (JNIEnv *env, jclass that, jint window, jint accel_group)
{
	gtk_window_add_accel_group((GtkWindow*)window, (GtkAccelGroup*)accel_group);
}






/*
 *  DIALOGS
 */

/*
 *  Color selection
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1set_1color
  (JNIEnv *env, jclass that, jint colorsel, jdoubleArray color)
{
	jdouble *color1 = NULL;
	if (color) {
		color1 = (*env)->GetDoubleArrayElements(env, color, NULL);
	}
	gtk_color_selection_set_color((GtkColorSelection*)colorsel, (gdouble*)color1);
	if (color) {
		(*env)->ReleaseDoubleArrayElements(env, color, color1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1get_1color
  (JNIEnv *env, jclass that, jint colorsel, jdoubleArray color)
{
	jdouble *color1 = NULL;
	if (color) {
		color1 = (*env)->GetDoubleArrayElements(env, color, NULL);
	}
	gtk_color_selection_get_color((GtkColorSelection*)colorsel, (gdouble*)color1);
	if (color) {
		(*env)->ReleaseDoubleArrayElements(env, color, color1, 0);
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1dialog_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_color_selection_dialog_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1COLOR_1SELECTION_1DIALOG_1OK_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkColorSelectionDialog*)fsd)->ok_button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1COLOR_1SELECTION_1DIALOG_1CANCEL_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkColorSelectionDialog*)fsd)->cancel_button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1COLOR_1SELECTION_1DIALOG_1HELP_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkColorSelectionDialog*)fsd)->help_button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1COLOR_1SELECTION_1DIALOG_1COLORSEL
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkColorSelectionDialog*)fsd)->colorsel);
}


/*
 * gtk_dialog_*
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1dialog_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_dialog_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1DIALOG_1VBOX
  (JNIEnv *env, jclass that, jint fsd)
{
    return (jint) (((GtkDialog*)fsd)->vbox);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1DIALOG_1ACTION_1AREA
  (JNIEnv *env, jclass that, jint fsd)
{
    return (jint) (((GtkDialog*)fsd)->action_area);
}


/*
 * gtk_file_selection_*
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_file_selection_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1set_1filename
  (JNIEnv *env, jclass that, jint filesel, jbyteArray filename)
{
	jbyte *filename1 = NULL;
	if (filename) {
		filename1 = (*env)->GetByteArrayElements(env, filename, NULL);
	}
	gtk_file_selection_set_filename((GtkFileSelection*)filesel, (gchar*)filename1);
	if (filename) {
		(*env)->ReleaseByteArrayElements(env, filename, filename1, 0);
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1get_1filename
  (JNIEnv *env, jclass that, jint filesel)
{
	return (jint)gtk_file_selection_get_filename((GtkFileSelection*)filesel);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1complete
  (JNIEnv *env, jclass that, jint filesel, jbyteArray pattern)
{
	jbyte *pattern1 = NULL;
	if (pattern) {
		pattern1 = (*env)->GetByteArrayElements(env, pattern, NULL);
	}
	gtk_file_selection_complete((GtkFileSelection*)filesel, (gchar*)pattern1);
	if (pattern) {
		(*env)->ReleaseByteArrayElements(env, pattern, pattern1, 0);
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1FILE_1SELECTION_1OK_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkFileSelection*)fsd)->ok_button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1FILE_1SELECTION_1CANCEL_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bug 70745
     */
    return (jint) (((GtkFileSelection*)fsd)->cancel_button);
}



/*
 * gtk_font_selection_*
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_font_selection_dialog_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1get_1font_1name
  (JNIEnv *env, jclass that, jint fsd)
{
	return (jint)gtk_font_selection_dialog_get_font_name((GtkFontSelectionDialog*)fsd);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1set_1font_1name
  (JNIEnv *env, jclass that, jint fsd, jbyteArray fontname)
{
	jboolean rc;
	jbyte *fontname1 = NULL;
	if (fontname) {
		fontname1 = (*env)->GetByteArrayElements(env, fontname, NULL);
	}
	rc = (jboolean)gtk_font_selection_dialog_set_font_name((GtkFontSelectionDialog*)fsd, (gchar*)fontname1);
	if (fontname) {
		(*env)->ReleaseByteArrayElements(env, fontname, fontname1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1FONT_1SELECTION_1DIALOG_1OK_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bugs 70742, 70745
     */
    return (jint) (((GtkFontSelectionDialog*)fsd)->ok_button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1FONT_1SELECTION_1DIALOG_1CANCEL_1BUTTON
  (JNIEnv *env, jclass that, jint fsd)
{
    /*
     * GTK bugs 70742, 70745
     */
    return (jint) (((GtkFontSelectionDialog*)fsd)->cancel_button);
}

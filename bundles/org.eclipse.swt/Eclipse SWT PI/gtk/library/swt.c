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
 * SWT OS natives implementation.
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>


/*
 * General
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1version
  (JNIEnv *env, jclass that, jint required_major, jint required_minor, jint required_micro)
{
	return (jint)gtk_check_version(required_major, required_minor, required_micro);
}

/*
 * Main loop
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1set_1locale
  (JNIEnv *env, jclass that)
{
  return (jint)gtk_set_locale();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1init_1check
  (JNIEnv *env, jclass that, jintArray argc, jintArray argv)
{
  /*
   * Temporary code, pending resolution of GTK bug 70984.
   */
  static char* targs[2];
  static char** targv;
  static int targc = 1;
  static char *targv0 = "swt";
  static char *targv1 = (char*)0;
  targs[0] = targv0; targs[1] = targv1;
  targv = &targs;
  return gtk_init_check(&targc, &targv);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_events_pending
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1events_1pending
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_events_pending");
#endif

	return (jint)gtk_events_pending();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_main
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_main");
#endif

	gtk_main();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_main_quit
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main_1quit
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_main_quit");
#endif

	gtk_main_quit();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main_1iteration
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_main_iteration();
}


/*
 * GLIB
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1signal_1connect
  (JNIEnv *env, jclass that, jint instance, jstring signel, jint handler, jint data)
{
  jbyte *signal1;
  signal1 = (*env)->GetStringUTFChars(env, signal, NULL);
  g_signal_connect(instance, signal, handler, data);
  (*env)->ReleaseStringUTFChars(env, signal, signal1);
}






/*
 *  Others - FIXME: please classify
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1add
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_grab_add");
#endif

	gtk_grab_add((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_grab_get_current
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1get_1current
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_grab_get_current");
#endif

	return (jint)gtk_grab_get_current();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_grab_remove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1grab_1remove
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_grab_remove");
#endif

	gtk_grab_remove((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_timeout_add
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1timeout_1add
  (JNIEnv *env, jclass that, jint interval, jint function, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_timeout_add");
#endif

	return (jint)gtk_timeout_add((guint32)interval, (GtkFunction)function, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_timeout_remove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1timeout_1remove
  (JNIEnv *env, jclass that, jint timeout_handler_id)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_timeout_remove");
#endif

	gtk_timeout_remove(timeout_handler_id);
}

/*
 *  Styles
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1draw_1check
  (JNIEnv *env, jclass that, jint style, jint window, jint state_type, jint shadow_type, jint x, jint y, jint width, jint height)
{
	gtk_draw_check((GtkStyle*)style, (GdkWindow *) window, (GtkStateType)state_type, (GtkShadowType)shadow_type, (gint) x, (gint) y, (gint) width, (gint) height);
}


/*
 *  Logging
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1default_1handler
  (JNIEnv *env, jclass that, jint log_domain, jint log_levels, jint message, jint unused_data)
{
	g_log_default_handler((gchar *)log_domain, (GLogLevelFlags)log_levels, (gchar *)message, (gpointer)unused_data);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1set_1handler
  (JNIEnv *env, jclass that, jbyteArray log_domain, jint log_levels, jint log_func, jint user_data)
{
	jint rc;
	jbyte *log_domain1 = NULL;
	if (log_domain) {
		log_domain1 = (*env)->GetByteArrayElements(env, log_domain, NULL);
	}
	rc = (jint) g_log_set_handler((gchar *)log_domain1, (GLogLevelFlags)log_levels, (GLogFunc) log_func, (gpointer) user_data);
	if (log_domain) {
		(*env)->ReleaseByteArrayElements(env, log_domain, log_domain1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1remove_1handler
  (JNIEnv *env, jclass that, jbyteArray log_domain, jint handler_id)
{
	jbyte *log_domain1 = NULL;

	if (log_domain) {
		log_domain1 = (*env)->GetByteArrayElements(env, log_domain, NULL);
	}
	g_log_remove_handler((gchar *)log_domain1, handler_id);
	if (log_domain) {
		(*env)->ReleaseByteArrayElements(env, log_domain, log_domain1, 0);
	}
}


/*
 *  GtkObject
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1unref
  (JNIEnv *env, jclass that, jint object)
{
	gtk_object_unref((GtkObject*)object);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1destroy
  (JNIEnv *env, jclass that, jint object)
{
	gtk_object_destroy((GtkObject*)object);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1get_1data_1by_1id
  (JNIEnv *env, jclass that, jint object, jint data_id)
{
	jint result;
	return (jint) gtk_object_get_data_by_id((GtkObject*)object, (GQuark) data_id);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1set_1data_1by_1id
  (JNIEnv *env, jclass that, jint object, jint data_id, jint data)
{
	gtk_object_set_data_by_id ((GtkObject*)object, (GQuark) data_id, (gpointer) data);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1quark_1from_1string
  (JNIEnv *env, jclass that, jbyteArray string)
{
	jint result;
	jbyte *string1 = NULL;
	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	result = g_quark_from_string((gchar *) string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return result;
}




/*
 *  Signals
 */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1block_1by_1data
  (JNIEnv *env, jclass that, jint object, jint data)
{
	gtk_signal_handler_block_by_data((GtkObject*)object, (gpointer) data);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1unblock_1by_1data
  (JNIEnv *env, jclass that, jint object, jint data)
{
	gtk_signal_handler_unblock_by_data((GtkObject*)object, (gpointer) data);
}



































/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_drawing_area_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drawing_1area_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_drawing_area_new");
#endif

	return (jint)gtk_drawing_area_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_drawing_area_size
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1drawing_1area_1size
  (JNIEnv *env, jclass that, jint darea, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_drawing_area_size");
#endif

	gtk_drawing_area_size((GtkDrawingArea*)darea, (gint)width, (gint)height);
}


























/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_list_clear_items
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1list_1clear_1items
  (JNIEnv *env, jclass that, jint list, jint start, jint end)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_list_clear_items");
#endif

	gtk_list_clear_items((GtkList*)list, (gint)start, (gint)end);
}


/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_list_select_item
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1list_1select_1item
  (JNIEnv *env, jclass that, jint list, jint item)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_list_select_item");
#endif

	gtk_list_select_item((GtkList*)list, (gint)item);
}





/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_new");
#endif

	return (jint)gtk_menu_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_insert
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1insert
  (JNIEnv *env, jclass that, jint menu, jint child, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_insert");
#endif

	gtk_menu_insert((GtkMenu*)menu, (GtkWidget*)child, (gint)position);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_popup
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1popup
  (JNIEnv *env, jclass that, jint menu, jint parent_menu_shell, jint parent_menu_item, jint func, jint data, jint button, jint activate_time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_popup");
#endif

	gtk_menu_popup((GtkMenu*)menu, (GtkWidget*)parent_menu_shell, (GtkWidget*)parent_menu_item, (GtkMenuPositionFunc)func, (gpointer)data, button, (guint32)activate_time);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_popdown
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1popdown
  (JNIEnv *env, jclass that, jint menu)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_popdown");
#endif

	gtk_menu_popdown((GtkMenu*)menu);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_bar_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1bar_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_bar_new");
#endif

	return (jint)gtk_menu_bar_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_bar_insert
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1bar_1insert
  (JNIEnv *env, jclass that, jint menu_bar, jint child, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_bar_insert");
#endif

	gtk_menu_bar_insert((GtkMenuBar*)menu_bar, (GtkWidget*)child, (gint)position);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_item_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_item_new");
#endif

	return (jint)gtk_menu_item_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_item_new_with_label
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1new_1with_1label
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_item_new_with_label");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_menu_item_new_with_label((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_item_set_submenu
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1set_1submenu
  (JNIEnv *env, jclass that, jint menu_item, jint submenu)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_item_set_submenu");
#endif

	gtk_menu_item_set_submenu((GtkMenuItem*)menu_item, (GtkWidget*)submenu);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_menu_item_remove_submenu
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1menu_1item_1remove_1submenu
  (JNIEnv *env, jclass that, jint menu_item)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_menu_item_remove_submenu");
#endif

	gtk_menu_item_remove_submenu((GtkMenuItem*)menu_item);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_misc_set_alignment
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1misc_1set_1alignment
  (JNIEnv *env, jclass that, jint misc, jfloat xalign, jfloat yalign)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_misc_set_alignment");
#endif

	gtk_misc_set_alignment((GtkMisc*)misc, xalign, yalign);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_new");
#endif

	return (jint)gtk_notebook_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_append_page
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1append_1page
  (JNIEnv *env, jclass that, jint notebook, jint child, jint tab_label)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_append_page");
#endif

	gtk_notebook_append_page((GtkNotebook*)notebook, (GtkWidget*)child, (GtkWidget*)tab_label);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_remove_page
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1remove_1page
  (JNIEnv *env, jclass that, jint notebook, jint page_num)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_remove_page");
#endif

	gtk_notebook_remove_page((GtkNotebook*)notebook, (gint)page_num);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_get_current_page
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1get_1current_1page
  (JNIEnv *env, jclass that, jint notebook)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_get_current_page");
#endif

	return (jint)gtk_notebook_get_current_page((GtkNotebook*)notebook);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_set_page
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1set_1page
  (JNIEnv *env, jclass that, jint notebook, jint page_num)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_set_page");
#endif

	gtk_notebook_set_page((GtkNotebook*)notebook, (gint)page_num);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_notebook_set_show_tabs
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1notebook_1set_1show_1tabs
  (JNIEnv *env, jclass that, jint notebook, jboolean show_tabs)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_notebook_set_show_tabs");
#endif

	gtk_notebook_set_show_tabs((GtkNotebook*)notebook, (gboolean)show_tabs);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_object_ref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1ref
  (JNIEnv *env, jclass that, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_ref");
#endif

	gtk_object_ref((GtkObject*)object);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_object_set_user_data
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1set_1user_1data
  (JNIEnv *env, jclass that, jint object, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_set_user_data");
#endif

	gtk_object_set_user_data((GtkObject*)object, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_object_get_user_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1get_1user_1data
  (JNIEnv *env, jclass that, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_get_user_data");
#endif

	return (jint)gtk_object_get_user_data((GtkObject*)object);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_pixmap_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1pixmap_1new
  (JNIEnv *env, jclass that, jint pixmap, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_pixmap_new");
#endif

	return (jint)gtk_pixmap_new((GdkPixmap*)pixmap, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_pixmap_set
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1pixmap_1set
  (JNIEnv *env, jclass that, jint pixmap, jint val, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_pixmap_set");
#endif

	gtk_pixmap_set((GtkPixmap*)pixmap, (GdkPixmap*)val, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_progress_configure
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1configure
  (JNIEnv *env, jclass that, jint progress, jfloat value, jfloat min, jfloat max)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_progress_configure");
#endif

	gtk_progress_configure((GtkProgress*)progress, value, min, max);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_progress_bar_new_with_adjustment
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_progress_bar_new");
#endif

	return (jint)gtk_progress_bar_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_progress_bar_set_bar_style
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1set_1bar_1style
  (JNIEnv *env, jclass that, jint pbar, jint style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_progress_bar_set_bar_style");
#endif

	gtk_progress_bar_set_bar_style((GtkProgressBar*)pbar, (GtkProgressBarStyle)style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_progress_bar_set_orientation
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1progress_1bar_1set_1orientation
  (JNIEnv *env, jclass that, jint pbar, jint orientation)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_progress_bar_set_orientation");
#endif

	gtk_progress_bar_set_orientation((GtkProgressBar*)pbar, (GtkProgressBarOrientation)orientation);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_radio_button_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1button_1new
  (JNIEnv *env, jclass that, jint group)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_radio_button_new");
#endif

	return (jint)gtk_radio_button_new((GSList*)group);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_radio_button_group
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1button_1group
  (JNIEnv *env, jclass that, jint radio_button)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_radio_button_group");
#endif

	return (jint)gtk_radio_button_group((GtkRadioButton*)radio_button);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_radio_menu_item_new_with_label
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1radio_1menu_1item_1new_1with_1label
  (JNIEnv *env, jclass that, jint group, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_radio_menu_item_new_with_label");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_radio_menu_item_new_with_label((GSList*)group, (gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_range_get_adjustment
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1range_1get_1adjustment
  (JNIEnv *env, jclass that, jint range)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_range_get_adjustment");
#endif

	return (jint)gtk_range_get_adjustment((GtkRange*)range);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scale_set_digits
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scale_1set_1digits
  (JNIEnv *env, jclass that, jint scale, jint digits)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scale_set_digits");
#endif

	gtk_scale_set_digits((GtkScale*)scale, (gint)digits);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scale_set_draw_value
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scale_1set_1draw_1value
  (JNIEnv *env, jclass that, jint scale, jboolean draw_value)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scale_set_draw_value");
#endif

	gtk_scale_set_draw_value((GtkScale*)scale, (gboolean)draw_value);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scale_set_value_pos
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scale_1set_1value_1pos
  (JNIEnv *env, jclass that, jint scale, jint pos)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scale_set_value_pos");
#endif

	gtk_scale_set_value_pos((GtkScale*)scale, (GtkPositionType)pos);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scrolled_window_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1new
  (JNIEnv *env, jclass that, jint hadjustment, jint vadjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scrolled_window_new");
#endif

	return (jint)gtk_scrolled_window_new((GtkAdjustment*)hadjustment, (GtkAdjustment*)vadjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scrolled_window_get_hadjustment
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1hadjustment
  (JNIEnv *env, jclass that, jint scrolled_window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scrolled_window_get_hadjustment");
#endif

	return (jint)gtk_scrolled_window_get_hadjustment((GtkScrolledWindow*)scrolled_window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scrolled_window_get_vadjustment
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1get_1vadjustment
  (JNIEnv *env, jclass that, jint scrolled_window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scrolled_window_get_vadjustment");
#endif

	return (jint)gtk_scrolled_window_get_vadjustment((GtkScrolledWindow*)scrolled_window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_scrolled_window_set_policy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1scrolled_1window_1set_1policy
  (JNIEnv *env, jclass that, jint scrolled_window, jint hscrollbar_policy, jint vscrollbar_policy)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_scrolled_window_set_policy");
#endif

	gtk_scrolled_window_set_policy((GtkScrolledWindow*)scrolled_window, (GtkPolicyType)hscrollbar_policy, (GtkPolicyType)vscrollbar_policy);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_selection_owner_set
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1selection_1owner_1set
  (JNIEnv *env, jclass that, jint widget, jint selection, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_selection_owner_set");
#endif

	return (jint)gtk_selection_owner_set((GtkWidget*)widget, (GdkAtom)selection, (guint32)time);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_selection_convert
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1selection_1convert
  (JNIEnv *env, jclass that, jint widget, jint selection, jint target, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_selection_convert");
#endif

	return (jint)gtk_selection_convert((GtkWidget*)widget, (GdkAtom)selection, (GdkAtom)target, (guint32)time);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_signal_emit_stop_by_name
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1emit_1stop_1by_1name
  (JNIEnv *env, jclass that, jint object, jbyteArray name)
{
	jbyte *name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_emit_stop_by_name");
#endif

	if (name) {
		name1 = (*env)->GetByteArrayElements(env, name, NULL);
	}
	gtk_signal_emit_stop_by_name((GtkObject*)object, (gchar*)name1);
	if (name) {
		(*env)->ReleaseByteArrayElements(env, name, name1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_signal_connect
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1connect
  (JNIEnv *env, jclass that, jint object, jbyteArray name, jint func, jint func_data)
{
	jint rc;
	jbyte *name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_connect");
#endif

	if (name) {
		name1 = (*env)->GetByteArrayElements(env, name, NULL);
	}
	rc = (jint)gtk_signal_connect((GtkObject*)object, (gchar*)name1, (GtkSignalFunc)func, (gpointer)func_data);
	if (name) {
		(*env)->ReleaseByteArrayElements(env, name, name1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_signal_connect_after
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1connect_1after
  (JNIEnv *env, jclass that, jint object, jbyteArray name, jint func, jint func_data)
{
	jint rc;
	jbyte *name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_connect_after");
#endif

	if (name) {
		name1 = (*env)->GetByteArrayElements(env, name, NULL);
	}
	rc = (jint)gtk_signal_connect_after((GtkObject*)object, (gchar*)name1, (GtkSignalFunc)func, (gpointer)func_data);
	if (name) {
		(*env)->ReleaseByteArrayElements(env, name, name1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_signal_handler_block_by_func
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1block_1by_1func
  (JNIEnv *env, jclass that, jint object, jint func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_handler_block_by_func");
#endif

	gtk_signal_handler_block_by_func((GtkObject*)object, (GtkSignalFunc)func, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_signal_handler_unblock_by_func
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1unblock_1by_1func
  (JNIEnv *env, jclass that, jint object, jint func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_handler_unblock_by_func");
#endif

	gtk_signal_handler_unblock_by_func((GtkObject*)object, (GtkSignalFunc)func, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_style_copy
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1style_1copy
  (JNIEnv *env, jclass that, jint style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_style_copy");
#endif

	return (jint)gtk_style_copy((GtkStyle*)style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_style_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1style_1unref
  (JNIEnv *env, jclass that, jint style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_style_unref");
#endif

	gtk_style_unref((GtkStyle*)style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_text_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1new
  (JNIEnv *env, jclass that, jint hadj, jint vadj)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_text_new");
#endif

	return (jint)gtk_text_new((GtkAdjustment*)hadj, (GtkAdjustment*)vadj);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_text_set_word_wrap
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1set_1word_1wrap
  (JNIEnv *env, jclass that, jint text, jint word_wrap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_text_set_word_wrap");
#endif

	gtk_text_set_word_wrap((GtkText*)text, (gint)word_wrap);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_text_get_length
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1text_1get_1length
  (JNIEnv *env, jclass that, jint text)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_text_get_length");
#endif

	return (jint)gtk_text_get_length((GtkText*)text);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toggle_button_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toggle_button_new");
#endif

	return (jint)gtk_toggle_button_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toggle_button_set_active
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1set_1active
  (JNIEnv *env, jclass that, jint toggle_button, jboolean is_active)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toggle_button_set_active");
#endif

	gtk_toggle_button_set_active((GtkToggleButton*)toggle_button, (gboolean)is_active);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toggle_button_get_active
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toggle_1button_1get_1active
  (JNIEnv *env, jclass that, jint toggle_button)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toggle_button_get_active");
#endif

	return (jboolean)gtk_toggle_button_get_active((GtkToggleButton*)toggle_button);
}


/*
 * Toolbar
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1TOOLBAR_1CHILD_1SPACE
  (JNIEnv *env, jclass that)
{
  return (jint)GTK_TOOLBAR_CHILD_SPACE;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1TOOLBAR_1CHILD_1BUTTON
  (JNIEnv *env, jclass that)
{
  return (jint)GTK_TOOLBAR_CHILD_BUTTON;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1TOOLBAR_1CHILD_1TOGGLEBUTTON
  (JNIEnv *env, jclass that)
{
  return (jint)GTK_TOOLBAR_CHILD_TOGGLEBUTTON;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1TOOLBAR_1CHILD_1RADIOBUTTON
  (JNIEnv *env, jclass that)
{
  return (jint)GTK_TOOLBAR_CHILD_RADIOBUTTON;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1TOOLBAR_1CHILD_1WIDGET
  (JNIEnv *env, jclass that)
{
  return (jint)GTK_TOOLBAR_CHILD_WIDGET;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_toolbar_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1element
  (JNIEnv *env, jclass that, jint toolbar, jint type, jint widget, jbyteArray text, jbyteArray tooltip_text, jbyteArray tooltip_private_text, jint icon, jint callback, jint user_data, jint position)
{
	jint rc;
	jbyte *text1 = NULL;
	jbyte *tooltip_text1 = NULL;
	jbyte *tooltip_private_text1 = NULL;

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	if (tooltip_text) {
		tooltip_text1 = (*env)->GetByteArrayElements(env, tooltip_text, NULL);
	}
	if (tooltip_private_text) {
		tooltip_private_text1 = (*env)->GetByteArrayElements(env, tooltip_private_text, NULL);
	}
	rc = (jint)gtk_toolbar_insert_element((GtkToolbar*)toolbar, (GtkToolbarChildType)type, (GtkWidget*)widget, (char*)text1, (char*)tooltip_text1, (char*)tooltip_private_text1, (GtkWidget*)icon, (GtkSignalFunc)callback, (gpointer)user_data, (gint)position);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
	if (tooltip_text) {
		(*env)->ReleaseByteArrayElements(env, tooltip_text, tooltip_text1, 0);
	}
	if (tooltip_private_text) {
		(*env)->ReleaseByteArrayElements(env, tooltip_private_text, tooltip_private_text1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1widget
  (JNIEnv *env, jclass that, jint toolbar, jint widget, jbyteArray tooltip_text, jbyteArray tooltip_private_text, jint position)
{
	jbyte *tooltip_text1 = NULL;
	jbyte *tooltip_private_text1 = NULL;

	if (tooltip_text) {
		tooltip_text1 = (*env)->GetByteArrayElements(env, tooltip_text, NULL);
	}
	if (tooltip_private_text) {
		tooltip_private_text1 = (*env)->GetByteArrayElements(env, tooltip_private_text, NULL);
	}
	gtk_toolbar_insert_widget((GtkToolbar*)toolbar, (GtkWidget*)widget, (char*)tooltip_text1, (char*)tooltip_private_text1, (gint)position);
	if (tooltip_text) {
		(*env)->ReleaseByteArrayElements(env, tooltip_text, tooltip_text1, 0);
	}
	if (tooltip_private_text) {
		(*env)->ReleaseByteArrayElements(env, tooltip_private_text, tooltip_private_text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toolbar_set_orientation
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1set_1orientation
  (JNIEnv *env, jclass that, jint toolbar, jint orientation)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toolbar_set_orientation");
#endif

	gtk_toolbar_set_orientation((GtkToolbar*)toolbar, (GtkOrientation)orientation);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toolbar_set_button_relief
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1set_1button_1relief
  (JNIEnv *env, jclass that, jint toolbar, jint relief)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toolbar_set_button_relief");
#endif

	gtk_toolbar_set_button_relief((GtkToolbar*)toolbar, (GtkReliefStyle)relief);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_tooltips_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_tooltips_new");
#endif

	return (jint)gtk_tooltips_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_tooltips_set_tip
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1tooltips_1set_1tip
  (JNIEnv *env, jclass that, jint tooltips, jint widget, jbyteArray tip_text, jbyteArray tip_private)
{
	jbyte *tip_text1 = NULL;
	jbyte *tip_private1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_tooltips_set_tip");
#endif

	if (tip_text) {
		tip_text1 = (*env)->GetByteArrayElements(env, tip_text, NULL);
	}
	if (tip_private) {
		tip_private1 = (*env)->GetByteArrayElements(env, tip_private, NULL);
	}
	gtk_tooltips_set_tip((GtkTooltips*)tooltips, (GtkWidget*)widget, (gchar*)tip_text1, (gchar*)tip_private1);
	if (tip_text) {
		(*env)->ReleaseByteArrayElements(env, tip_text, tip_text1, 0);
	}
	if (tip_private) {
		(*env)->ReleaseByteArrayElements(env, tip_private, tip_private1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_vbox_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vbox_1new
  (JNIEnv *env, jclass that, jboolean homogeneous, jint spacing)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_vbox_new");
#endif

	return (jint)gtk_vbox_new((gboolean)homogeneous, (gint)spacing);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_vscale_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vscale_1new
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_vscale_new");
#endif

	return (jint)gtk_vscale_new((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_vscrollbar_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vscrollbar_1new
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_vscrollbar_new");
#endif

	return (jint)gtk_vscrollbar_new((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_vseparator_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1vseparator_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_vseparator_new");
#endif

	return (jint)gtk_vseparator_new();
}



/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_list_free
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1free
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_list_free");
#endif

	g_list_free((GList*)list);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_list_append
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1append
  (JNIEnv *env, jclass that, jint list, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_list_append");
#endif

	return (jint)g_list_append((GList*)list, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_list_nth
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1nth
  (JNIEnv *env, jclass that, jint list, jint n)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_list_nth");
#endif

	return (jint)g_list_nth((GList*)list, n);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_list_length
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1length
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_list_length");
#endif

	return (jint)g_list_length((GList*)list);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_list_nth_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1list_1nth_1data
  (JNIEnv *env, jclass that, jint list, jint n)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_list_nth_data");
#endif

	return (jint)g_list_nth_data((GList*)list, n);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_slist_nth
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1slist_1nth
  (JNIEnv *env, jclass that, jint list, jint n)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_slist_nth");
#endif

	return (jint)g_slist_nth((GSList*)list, n);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_slist_length
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1slist_1length
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_slist_length");
#endif

	return (jint)g_slist_length((GSList*)list);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_slist_nth_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1slist_1nth_1data
  (JNIEnv *env, jclass that, jint list, jint n)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_slist_nth_data");
#endif

	return (jint)g_slist_nth_data((GSList*)list, n);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_malloc
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1malloc
  (JNIEnv *env, jclass that, jint size)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_malloc");
#endif

	return (jint)g_malloc((gulong)size);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_free
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1free
  (JNIEnv *env, jclass that, jint mem)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_free");
#endif

	g_free((gpointer)mem);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_strdup
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1strdup
  (JNIEnv *env, jclass that, jbyteArray str)
{
	jint rc;
	jbyte *str1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_strdup");
#endif

	if (str) {
		str1 = (*env)->GetByteArrayElements(env, str, NULL);
	}
	rc = (jint)g_strdup((gchar*)str1);
	if (str) {
		(*env)->ReleaseByteArrayElements(env, str, str1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_get_home_dir
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1get_1home_1dir
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_get_home_dir");
#endif

	return (jint)g_get_home_dir();
}




/* ***NON-GTK*** */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_strlen
 (JNIEnv *env, jclass that, jint string)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "strlen\n");
#endif

	return (jint)strlen((char*)string);
}


/*
 * Class:     org_eclipse_swt_internal_gtk_OS
 * Method:    XListFonts
 * Signature: ([BI[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_XListFonts
  (JNIEnv *env, jclass that, jbyteArray pattern, jint maxnames, jintArray actual_count_return)
{
    jbyte *pattern1 = NULL;
    jint *actual_count_return1=NULL;
    jint rc;

    if (pattern)    
        pattern1 = (*env)->GetByteArrayElements(env, pattern, NULL);
    if (actual_count_return)    
        actual_count_return1 = (*env)->GetIntArrayElements(env, actual_count_return, NULL);
    
    rc = (jint) XListFonts(GDK_DISPLAY(), (char *)pattern1, maxnames, (int *)actual_count_return1);

    if (pattern)
        (*env)->ReleaseByteArrayElements(env, pattern, pattern1, 0);
    if (actual_count_return)
        (*env)->ReleaseIntArrayElements(env, actual_count_return, actual_count_return1, 0);
    return rc;
}


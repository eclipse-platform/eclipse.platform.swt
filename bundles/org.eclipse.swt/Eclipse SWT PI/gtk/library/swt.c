/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
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

/*#define PRINT_FAILED_RCODES*/
#define NDEBUG

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_row_is_visible
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1row_1is_1visible
  (JNIEnv *env, jclass that, jint clist, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_row_is_visible");
#endif

	return gtk_clist_row_is_visible((GtkCList *)clist, (gint)row);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_log_default_handler
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1default_1handler
  (JNIEnv *env, jclass that, jint log_domain, jint log_levels, jint message, jint unused_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_log_default_handler");
#endif

	g_log_default_handler((gchar *)log_domain, (GLogLevelFlags)log_levels, (gchar *)message, (gpointer)unused_data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_log_set_handler
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1set_1handler
  (JNIEnv *env, jclass that, jbyteArray log_domain, jint log_levels, jint log_func, jint user_data)
{
	jint rc;
	jbyte *log_domain1 = NULL;
	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_log_set_handler");
#endif

	if (log_domain) {
		log_domain1 = (*env)->GetByteArrayElements(env, log_domain, NULL);
	}
	rc = (jint) g_log_set_handler((gchar *)log_domain1, (GLogLevelFlags)log_levels, (GLogFunc) log_func, (gpointer) user_data);
	if (log_domain) {
		(*env)->ReleaseByteArrayElements(env, log_domain, log_domain1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	g_log_remove_handler
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1log_1remove_1handler
  (JNIEnv *env, jclass that, jbyteArray log_domain, jint handler_id)
{
	jbyte *log_domain1 = NULL;
	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_log_remove_handler");
#endif

	if (log_domain) {
		log_domain1 = (*env)->GetByteArrayElements(env, log_domain, NULL);
	}
	g_log_remove_handler((gchar *)log_domain1, handler_id);
	if (log_domain) {
		(*env)->ReleaseByteArrayElements(env, log_domain, log_domain1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	GTK_WIDGET_TYPE
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1TYPE
  (JNIEnv *env, jclass that, jint wid)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_TYPE");
#endif

	return GTK_WIDGET_TYPE((GtkWidget*)wid);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_get_type
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1get_1type
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_get_type");
#endif

	return gtk_label_get_type ();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_object_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1unref
  (JNIEnv *env, jclass that, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_unref");
#endif

	gtk_object_unref((GtkObject*)object);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_object_ref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1destroy
  (JNIEnv *env, jclass that, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_destroy");
#endif

	gtk_object_destroy((GtkObject*)object);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1get_1data_1by_1id
  (JNIEnv *env, jclass that, jint object, jint data_id)
{
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_get_data_by_id");
#endif
	return (jint) gtk_object_get_data_by_id((GtkObject*)object, (GQuark) data_id);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1object_1set_1data_1by_1id
  (JNIEnv *env, jclass that, jint object, jint data_id, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_object_set_data_by_id");
#endif

	gtk_object_set_data_by_id ((GtkObject*)object, (GQuark) data_id, (gpointer) data);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_g_1quark_1from_1string
  (JNIEnv *env, jclass that, jbyteArray string)
{
	jint result;
	jbyte *string1 = NULL;
	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "g_quark_from_string");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	result = g_quark_from_string((gchar *) string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return result;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1block_1by_1data
  (JNIEnv *env, jclass that, jint object, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_handler_block_by_data");
#endif

	gtk_signal_handler_block_by_data((GtkObject*)object, (gpointer) data);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1signal_1handler_1unblock_1by_1data
  (JNIEnv *env, jclass that, jint object, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_signal_handler_unblock_by_data");
#endif

	gtk_signal_handler_unblock_by_data((GtkObject*)object, (gpointer) data);
}


JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1rgb_1init
  (JNIEnv *env, jclass cl)
{
    gdk_rgb_init();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1FLAGS
  (JNIEnv *env, jclass that, jint wid)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_FLAGS");
#endif

	return (jint) GTK_WIDGET_FLAGS((GtkWidget*)wid);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SET_1FLAGS
  (JNIEnv *env, jclass that, jint wid, jint flag)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_SET_FLAGS");
#endif

	GTK_WIDGET_SET_FLAGS((GtkWidget*)wid, flag);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1UNSET_1FLAGS
  (JNIEnv *env, jclass that, jint wid, jint flag)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_UNSET_FLAGS");
#endif

	GTK_WIDGET_UNSET_FLAGS((GtkWidget*)wid, flag);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1NO_1WINDOW
  (JNIEnv *env, jclass that, jint wid)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_NO_WINDOW");
#endif

	return (jboolean) GTK_WIDGET_NO_WINDOW((GtkWidget*)wid);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	GDK_ROOT_PARENT
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1ROOT_1PARENT
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GDK_ROOT_PARENT");
#endif

	return (jint) GDK_ROOT_PARENT();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1SENSITIVE
  (JNIEnv *env, jclass that, jint wid)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_SENSITIVE");
#endif

	return (jboolean) GTK_WIDGET_SENSITIVE((GtkWidget*)wid);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_GTK_1WIDGET_1IS_1SENSITIVE
  (JNIEnv *env, jclass that, jint wid)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "GTK_WIDGET_IS_SENSITIVE");
#endif

	return (jboolean) GTK_WIDGET_IS_SENSITIVE((GtkWidget*)wid);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_accel_group_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1group_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_accel_group_new");
#endif

	return (jint)gtk_accel_group_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_accel_group_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1group_1unref
  (JNIEnv *env, jclass that, jint accel_group)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_accel_group_unref");
#endif

	gtk_accel_group_unref((GtkAccelGroup*)accel_group);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_adjustment_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1new
  (JNIEnv *env, jclass that, jfloat value, jfloat lower, jfloat upper, jfloat step_increment, jfloat page_increment, jfloat page_size)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_adjustment_new");
#endif

	return (jint)gtk_adjustment_new(value, lower, upper, step_increment, page_increment, page_size);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_adjustment_changed
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1changed
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_adjustment_changed");
#endif

	gtk_adjustment_changed((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_adjustment_value_changed
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1value_1changed
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_adjustment_value_changed");
#endif

	gtk_adjustment_value_changed((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_adjustment_set_value
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1set_1value
  (JNIEnv *env, jclass that, jint adjustment, jfloat value)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_adjustment_set_value");
#endif

	gtk_adjustment_set_value((GtkAdjustment*)adjustment, value);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_arrow_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1arrow_1new
  (JNIEnv *env, jclass that, jint arrow_type, jint shadow_type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_arrow_new");
#endif

	return (jint)gtk_arrow_new((GtkArrowType)arrow_type, (GtkShadowType)shadow_type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_arrow_set
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1arrow_1set
  (JNIEnv *env, jclass that, jint arrow, jint arrow_type, jint shadow_type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_arrow_set");
#endif

	gtk_arrow_set((GtkArrow*)arrow, (GtkArrowType)arrow_type, (GtkShadowType)shadow_type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_box_pack_start
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1start
  (JNIEnv *env, jclass that, jint box, jint child, jboolean expand, jboolean fill, jint padding)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_box_pack_start");
#endif

	gtk_box_pack_start((GtkBox*)box, (GtkWidget*)child, (gboolean)expand, (gboolean)fill, padding);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_box_pack_end
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1end
  (JNIEnv *env, jclass that, jint box, jint child, jboolean expand, jboolean fill, jint padding)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_box_pack_end");
#endif

	gtk_box_pack_end((GtkBox*)box, (GtkWidget*)child, (gboolean)expand, (gboolean)fill, padding);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_button_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_button_new");
#endif

	return (jint)gtk_button_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_button_new_with_label
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1new_1with_1label
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_button_new_with_label");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_button_new_with_label((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_check_button_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1button_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_check_button_new");
#endif

	return (jint)gtk_check_button_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_check_menu_item_new_with_label
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1new_1with_1label
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_check_menu_item_new_with_label");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_check_menu_item_new_with_label((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_check_menu_item_set_active
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1set_1active
  (JNIEnv *env, jclass that, jint check_menu_item, jboolean is_active)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_check_menu_item_set_active");
#endif

	gtk_check_menu_item_set_active((GtkCheckMenuItem*)check_menu_item, (gboolean)is_active);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_check_menu_item_set_show_toggle
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1menu_1item_1set_1show_1toggle
  (JNIEnv *env, jclass that, jint menu_item, jboolean always)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_check_menu_item_set_show_toggle");
#endif

	gtk_check_menu_item_set_show_toggle((GtkCheckMenuItem*)menu_item, (gboolean)always);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_color_selection_set_color
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1set_1color
  (JNIEnv *env, jclass that, jint colorsel, jdoubleArray color)
{
	jdouble *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_color_selection_set_color");
#endif

	if (color) {
		color1 = (*env)->GetDoubleArrayElements(env, color, NULL);
	}
	gtk_color_selection_set_color((GtkColorSelection*)colorsel, (gdouble*)color1);
	if (color) {
		(*env)->ReleaseDoubleArrayElements(env, color, color1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_color_selection_get_color
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1get_1color
  (JNIEnv *env, jclass that, jint colorsel, jdoubleArray color)
{
	jdouble *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_color_selection_get_color");
#endif

	if (color) {
		color1 = (*env)->GetDoubleArrayElements(env, color, NULL);
	}
	gtk_color_selection_get_color((GtkColorSelection*)colorsel, (gdouble*)color1);
	if (color) {
		(*env)->ReleaseDoubleArrayElements(env, color, color1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_color_selection_dialog_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1color_1selection_1dialog_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_color_selection_dialog_new");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_color_selection_dialog_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_combo_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_combo_new");
#endif

	return (jint)gtk_combo_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_combo_set_popdown_strings
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1combo_1set_1popdown_1strings
  (JNIEnv *env, jclass that, jint combo, jint strings)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_combo_set_popdown_strings");
#endif

	gtk_combo_set_popdown_strings((GtkCombo*)combo, (GList*)strings);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_container_add
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1add
  (JNIEnv *env, jclass that, jint container, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_container_add");
#endif

	gtk_container_add((GtkContainer*)container, (GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_container_remove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1remove
  (JNIEnv *env, jclass that, jint container, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_container_remove");
#endif

	gtk_container_remove((GtkContainer*)container, (GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_container_children
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1children
  (JNIEnv *env, jclass that, jint container)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_container_children");
#endif

	return (jint)gtk_container_children((GtkContainer*)container);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1new
  (JNIEnv *env, jclass that, jint columns, jint tree_column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_new");
#endif

	return (jint)gtk_ctree_new((gint)columns, (gint)tree_column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_insert_node
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1insert_1node
  (JNIEnv *env, jclass that, jint ctree, jint parent, jint sibling, jintArray text, jbyte spacing, jint pixmap_closed, jint mask_closed, jint pixmap_opened, jint mask_opened, jboolean is_leaf, jboolean expanded)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_insert_node");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_ctree_insert_node((GtkCTree*)ctree, (GtkCTreeNode*)parent, (GtkCTreeNode*)sibling, (gchar**)text1, (guint8)spacing, (GdkPixmap*)pixmap_closed, (GdkBitmap*)mask_closed, (GdkPixmap*)pixmap_opened, (GdkBitmap*)mask_opened, (gboolean)is_leaf, (gboolean)expanded);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_remove_node
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1remove_1node
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_remove_node");
#endif

	gtk_ctree_remove_node((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_post_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1post_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node, jint func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_post_recursive");
#endif

	gtk_ctree_post_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node, (GtkCTreeFunc)func, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_is_viewable
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1viewable
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_is_viewable");
#endif

	return (jboolean)gtk_ctree_is_viewable((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_nth
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1nth
  (JNIEnv *env, jclass that, jint ctree, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_nth");
#endif

	return (jint)gtk_ctree_node_nth((GtkCTree*)ctree, row);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_is_hot_spot
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1hot_1spot
  (JNIEnv *env, jclass that, jint ctree, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_is_hot_spot");
#endif

	return (jboolean)gtk_ctree_is_hot_spot((GtkCTree*)ctree, (gint)x, (gint)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_expand
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1expand
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_expand");
#endif

	gtk_ctree_expand((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_collapse
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1collapse
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_collapse");
#endif

	gtk_ctree_collapse((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_select
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_select");
#endif

	gtk_ctree_select((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_select_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_select_recursive");
#endif

	gtk_ctree_select_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_unselect_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1unselect_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_unselect_recursive");
#endif

	gtk_ctree_unselect_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_set_node_info
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1set_1node_1info
  (JNIEnv *env, jclass that, jint ctree, jint node, jbyteArray text, jbyte spacing, jint pixmap_closed, jint mask_closed, jint pixmap_opened, jint mask_opened, jboolean is_leaf, jboolean expanded)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_set_node_info");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_ctree_set_node_info((GtkCTree*)ctree, (GtkCTreeNode*)node, (gchar*)text1, (guint8)spacing, (GdkPixmap*)pixmap_closed, (GdkBitmap*)mask_closed, (GdkPixmap*)pixmap_opened, (GdkBitmap*)mask_opened, (gboolean)is_leaf, (gboolean)expanded);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_get_node_info
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1get_1node_1info
  (JNIEnv *env, jclass that, jint ctree, jint node, jintArray text, jbyteArray spacing, jintArray pixmap_closed, jintArray mask_closed, jintArray pixmap_opened, jintArray mask_opened, jbooleanArray is_leaf, jbooleanArray expanded)
{
	jint rc;
	jint *text1 = NULL;
	jbyte *spacing1 = NULL;
	jint *pixmap_closed1 = NULL;
	jint *mask_closed1 = NULL;
	jint *pixmap_opened1 = NULL;
	jint *mask_opened1 = NULL;
	jboolean *is_leaf1 = NULL;
	jboolean *expanded1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_get_node_info");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	if (spacing) {
		spacing1 = (*env)->GetByteArrayElements(env, spacing, NULL);
	}
	if (pixmap_closed) {
		pixmap_closed1 = (*env)->GetIntArrayElements(env, pixmap_closed, NULL);
	}
	if (mask_closed) {
		mask_closed1 = (*env)->GetIntArrayElements(env, mask_closed, NULL);
	}
	if (pixmap_opened) {
		pixmap_opened1 = (*env)->GetIntArrayElements(env, pixmap_opened, NULL);
	}
	if (mask_opened) {
		mask_opened1 = (*env)->GetIntArrayElements(env, mask_opened, NULL);
	}
	if (is_leaf) {
		is_leaf1 = (*env)->GetBooleanArrayElements(env, is_leaf, NULL);
	}
	if (expanded) {
		expanded1 = (*env)->GetBooleanArrayElements(env, expanded, NULL);
	}
	rc = (jint)gtk_ctree_get_node_info((GtkCTree*)ctree, (GtkCTreeNode*)node, (gchar**)text1, (guint8*)spacing1, (GdkPixmap**)pixmap_closed1, (GdkBitmap**)mask_closed1, (GdkPixmap**)pixmap_opened1, (GdkBitmap**)mask_opened1, (gboolean*)is_leaf1, (gboolean*)expanded1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	if (spacing) {
		(*env)->ReleaseByteArrayElements(env, spacing, spacing1, 0);
	}
	if (pixmap_closed) {
		(*env)->ReleaseIntArrayElements(env, pixmap_closed, pixmap_closed1, 0);
	}
	if (mask_closed) {
		(*env)->ReleaseIntArrayElements(env, mask_closed, mask_closed1, 0);
	}
	if (pixmap_opened) {
		(*env)->ReleaseIntArrayElements(env, pixmap_opened, pixmap_opened1, 0);
	}
	if (mask_opened) {
		(*env)->ReleaseIntArrayElements(env, mask_opened, mask_opened1, 0);
	}
	if (is_leaf) {
		(*env)->ReleaseBooleanArrayElements(env, is_leaf, is_leaf1, 0);
	}
	if (expanded) {
		(*env)->ReleaseBooleanArrayElements(env, expanded, expanded1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_get_row_style
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1style
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_get_row_style");
#endif

	return (jint)gtk_ctree_node_get_row_style((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_set_row_data
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1set_1row_1data
  (JNIEnv *env, jclass that, jint ctree, jint node, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_set_row_data");
#endif

	gtk_ctree_node_set_row_data((GtkCTree*)ctree, (GtkCTreeNode*)node, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_get_row_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1data
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_get_row_data");
#endif

	return (jint)gtk_ctree_node_get_row_data((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_moveto
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1moveto
  (JNIEnv *env, jclass that, jint ctree, jint node, jint column, jfloat row_align, jfloat col_align)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_moveto");
#endif

	gtk_ctree_node_moveto((GtkCTree*)ctree, (GtkCTreeNode*)node, (gint)column, row_align, col_align);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_is_visible
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1is_1visible
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_is_visible");
#endif

	return (jint)gtk_ctree_node_is_visible((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_dialog_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1dialog_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_dialog_new");
#endif

	GtkDialog* dialog = (GtkDialog*)gtk_dialog_new();
	return (jint)dialog;
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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_new");
#endif

	return (jint)gtk_entry_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_set_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1text
  (JNIEnv *env, jclass that, jint entry, jbyteArray text)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_set_text");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_entry_set_text((GtkEntry*)entry, (gchar*)text1);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_append_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1append_1text
  (JNIEnv *env, jclass that, jint entry, jbyteArray text)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_append_text");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_entry_append_text((GtkEntry*)entry, (gchar*)text1);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_get_text
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1get_1text
  (JNIEnv *env, jclass that, jint entry)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_get_text");
#endif

	return (jint)gtk_entry_get_text((GtkEntry*)entry);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_set_visibility
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1visibility
  (JNIEnv *env, jclass that, jint entry, jboolean visible)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_set_visibility");
#endif

	gtk_entry_set_visibility((GtkEntry*)entry, (gboolean)visible);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_set_editable
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1editable
  (JNIEnv *env, jclass that, jint entry, jboolean editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_set_editable");
#endif

	gtk_entry_set_editable((GtkEntry*)entry, (gboolean)editable);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_entry_set_max_length
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1entry_1set_1max_1length
  (JNIEnv *env, jclass that, jint entry, jshort max)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_entry_set_max_length");
#endif

	gtk_entry_set_max_length((GtkEntry*)entry, (guint16)max);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_event_box_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1event_1box_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_event_box_new");
#endif

	return (jint)gtk_event_box_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_file_selection_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_file_selection_new");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_file_selection_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_file_selection_set_filename
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1set_1filename
  (JNIEnv *env, jclass that, jint filesel, jbyteArray filename)
{
	jbyte *filename1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_file_selection_set_filename");
#endif

	if (filename) {
		filename1 = (*env)->GetByteArrayElements(env, filename, NULL);
	}
	gtk_file_selection_set_filename((GtkFileSelection*)filesel, (gchar*)filename1);
	if (filename) {
		(*env)->ReleaseByteArrayElements(env, filename, filename1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_file_selection_get_filename
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1get_1filename
  (JNIEnv *env, jclass that, jint filesel)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_file_selection_get_filename");
#endif

	return (jint)gtk_file_selection_get_filename((GtkFileSelection*)filesel);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_file_selection_complete
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1file_1selection_1complete
  (JNIEnv *env, jclass that, jint filesel, jbyteArray pattern)
{
	jbyte *pattern1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_file_selection_complete");
#endif

	if (pattern) {
		pattern1 = (*env)->GetByteArrayElements(env, pattern, NULL);
	}
	gtk_file_selection_complete((GtkFileSelection*)filesel, (gchar*)pattern1);
	if (pattern) {
		(*env)->ReleaseByteArrayElements(env, pattern, pattern1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_fixed_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_fixed_new");
#endif

	return (jint)gtk_fixed_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_fixed_put
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1put
  (JNIEnv *env, jclass that, jint fixed, jint widget, jshort x, jshort y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_fixed_put");
#endif

	gtk_fixed_put((GtkFixed*)fixed, (GtkWidget*)widget, (gint16)x, (gint16)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_fixed_move
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1fixed_1move
  (JNIEnv *env, jclass that, jint fixed, jint widget, jshort x, jshort y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_fixed_move");
#endif

	gtk_fixed_move((GtkFixed*)fixed, (GtkWidget*)widget, (gint16)x, (gint16)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_font_selection_set_font_name
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1set_1font_1name
  (JNIEnv *env, jclass that, jint fontsel, jbyteArray fontname)
{
	jboolean rc;
	jbyte *fontname1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_font_selection_set_font_name");
#endif

	if (fontname) {
		fontname1 = (*env)->GetByteArrayElements(env, fontname, NULL);
	}
	rc = (jboolean)gtk_font_selection_set_font_name((GtkFontSelection*)fontsel, (gchar*)fontname1);
	if (fontname) {
		(*env)->ReleaseByteArrayElements(env, fontname, fontname1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_font_selection_dialog_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1new
  (JNIEnv *env, jclass that, jbyteArray title)
{
	jint rc;
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_font_selection_dialog_new");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	rc = (jint)gtk_font_selection_dialog_new((gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_font_selection_dialog_get_font_name
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1get_1font_1name
  (JNIEnv *env, jclass that, jint fsd)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_font_selection_dialog_get_font_name");
#endif

	return (jint)gtk_font_selection_dialog_get_font_name((GtkFontSelectionDialog*)fsd);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_font_selection_dialog_set_font_name
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1font_1selection_1dialog_1set_1font_1name
  (JNIEnv *env, jclass that, jint fsd, jbyteArray fontname)
{
	jboolean rc;
	jbyte *fontname1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_font_selection_dialog_set_font_name");
#endif

	if (fontname) {
		fontname1 = (*env)->GetByteArrayElements(env, fontname, NULL);
	}
	rc = (jboolean)gtk_font_selection_dialog_set_font_name((GtkFontSelectionDialog*)fsd, (gchar*)fontname1);
	if (fontname) {
		(*env)->ReleaseByteArrayElements(env, fontname, fontname1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_frame_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1new
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_frame_new");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_frame_new((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_frame_set_label
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1label
  (JNIEnv *env, jclass that, jint frame, jbyteArray label)
{
	jbyte *label1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_frame_set_label");
#endif

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	gtk_frame_set_label((GtkFrame*)frame, (gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_frame_set_shadow_type
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1shadow_1type
  (JNIEnv *env, jclass that, jint frame, jint type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_frame_set_shadow_type");
#endif

	gtk_frame_set_shadow_type((GtkFrame*)frame, (GtkShadowType)type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_hbox_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hbox_1new
  (JNIEnv *env, jclass that, jboolean homogeneous, jint spacing)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_hbox_new");
#endif

	return (jint)gtk_hbox_new((gboolean)homogeneous, (gint)spacing);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_hscale_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hscale_1new
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_hscale_new");
#endif

	return (jint)gtk_hscale_new((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_hscrollbar_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hscrollbar_1new
  (JNIEnv *env, jclass that, jint adjustment)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_hscrollbar_new");
#endif

	return (jint)gtk_hscrollbar_new((GtkAdjustment*)adjustment);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_hseparator_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hseparator_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_hseparator_new");
#endif

	return (jint)gtk_hseparator_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1new
  (JNIEnv *env, jclass that, jbyteArray str)
{
	jint rc;
	jbyte *str1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_new");
#endif

	if (str) {
		str1 = (*env)->GetByteArrayElements(env, str, NULL);
	}
	rc = (jint)gtk_label_new((gchar*)str1);
	if (str) {
		(*env)->ReleaseByteArrayElements(env, str, str1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_set_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1text
  (JNIEnv *env, jclass that, jint label, jbyteArray str)
{
	jbyte *str1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_set_text");
#endif

	if (str) {
		str1 = (*env)->GetByteArrayElements(env, str, NULL);
	}
	gtk_label_set_text((GtkLabel*)label, (gchar*)str1);
	if (str) {
		(*env)->ReleaseByteArrayElements(env, str, str1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_set_justify
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1justify
  (JNIEnv *env, jclass that, jint label, jint jtype)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_set_justify");
#endif

	gtk_label_set_justify((GtkLabel*)label, (GtkJustification)jtype);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_set_pattern
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1pattern
  (JNIEnv *env, jclass that, jint label, jbyteArray pattern)
{
	jbyte *pattern1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_set_pattern");
#endif

	if (pattern) {
		pattern1 = (*env)->GetByteArrayElements(env, pattern, NULL);
	}
	gtk_label_set_pattern((GtkLabel*)label, (gchar*)pattern1);
	if (pattern) {
		(*env)->ReleaseByteArrayElements(env, pattern, pattern1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_set_line_wrap
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1set_1line_1wrap
  (JNIEnv *env, jclass that, jint label, jboolean wrap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_set_line_wrap");
#endif

	gtk_label_set_line_wrap((GtkLabel*)label, (gboolean)wrap);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_label_parse_uline
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1parse_1uline
  (JNIEnv *env, jclass that, jint label, jbyteArray string)
{
	jint rc;
	jbyte *string1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_label_parse_uline");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	rc = (jint)gtk_label_parse_uline((GtkLabel*)label, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return rc;
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
 * Method:	gtk_check_version
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1version
  (JNIEnv *env, jclass that, jint required_major, jint required_minor, jint required_micro)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_check_version");
#endif

	return (jint)gtk_check_version(required_major, required_minor, required_micro);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_init_check
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1init_1check
  (JNIEnv *env, jclass that, jintArray argc, jintArray argv)
{
	jboolean rc;
	jint *argc1 = NULL;
	jint *argv1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_init_check");
#endif

	if (argc) {
		argc1 = (*env)->GetIntArrayElements(env, argc, NULL);
	}
	if (argv) {
		argv1 = (*env)->GetIntArrayElements(env, argv, NULL);
	}
	rc = (jboolean)gtk_init_check((int*)argc1, (char***)argv1);
	if (argc) {
		(*env)->ReleaseIntArrayElements(env, argc, argc1, 0);
	}
	if (argv) {
		(*env)->ReleaseIntArrayElements(env, argv, argv1, 0);
	}
	return rc;
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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_main_iteration
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1main_1iteration
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_main_iteration");
#endif

	return (jint)gtk_main_iteration();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_grab_add
 * Signature:	
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
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toolbar_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1new
  (JNIEnv *env, jclass that, jint orientation, jint style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toolbar_new");
#endif

	return (jint)gtk_toolbar_new((GtkOrientation)orientation, (GtkToolbarStyle)style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toolbar_insert_element
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1element
  (JNIEnv *env, jclass that, jint toolbar, jint type, jint widget, jbyteArray text, jbyteArray tooltip_text, jbyteArray tooltip_private_text, jint icon, jint callback, jint user_data, jint position)
{
	jint rc;
	jbyte *text1 = NULL;
	jbyte *tooltip_text1 = NULL;
	jbyte *tooltip_private_text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toolbar_insert_element");
#endif

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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_toolbar_insert_widget
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1toolbar_1insert_1widget
  (JNIEnv *env, jclass that, jint toolbar, jint widget, jbyteArray tooltip_text, jbyteArray tooltip_private_text, jint position)
{
	jbyte *tooltip_text1 = NULL;
	jbyte *tooltip_private_text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_toolbar_insert_widget");
#endif

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
 * Method:	gtk_widget_destroy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1destroy
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_destroy");
#endif

	gtk_widget_destroy((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_show
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_show");
#endif

	gtk_widget_show((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_show_now
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1now
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_show_now");
#endif

	gtk_widget_show_now((GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1show_1all
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_show_all");
#endif

	gtk_widget_show_all((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_hide
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1hide
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_hide");
#endif

	gtk_widget_hide((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_realize
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1realize
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_realize");
#endif

	gtk_widget_realize((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_queue_draw
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1queue_1draw
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_queue_draw");
#endif

	gtk_widget_queue_draw((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_size_request
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1request
  (JNIEnv *env, jclass that, jint widget, jobject requisition)
{
	DECL_GLOB(pGlob)
	GtkRequisition requisition_struct, *requisition1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_size_request");
#endif

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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_size_allocate
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1size_1allocate
  (JNIEnv *env, jclass that, jint widget, jobject allocation)
{
	DECL_GLOB(pGlob)
	GtkAllocation allocation_struct, *allocation1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_size_allocate");
#endif
	
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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_add_accelerator
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1accelerator
  (JNIEnv *env, jclass that, jint widget, jbyteArray accel_signal, jint accel_group, jint accel_key, jint accel_mods, jint accel_flags)
{
	jbyte *accel_signal1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_add_accelerator");
#endif

	if (accel_signal) {
		accel_signal1 = (*env)->GetByteArrayElements(env, accel_signal, NULL);
	}
	gtk_widget_add_accelerator((GtkWidget*)widget, (gchar*)accel_signal1, (GtkAccelGroup*)accel_group, accel_key, accel_mods, (GtkAccelFlags)accel_flags);
	if (accel_signal) {
		(*env)->ReleaseByteArrayElements(env, accel_signal, accel_signal1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_remove_accelerator
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1remove_1accelerator
  (JNIEnv *env, jclass that, jint widget, jint accel_group, jint accel_key, jint accel_mods)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_remove_accelerator");
#endif

	gtk_widget_remove_accelerator((GtkWidget*)widget, (GtkAccelGroup*)accel_group, accel_key, accel_mods);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_event
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1event
  (JNIEnv *env, jclass that, jint widget, jint event)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_event");
#endif

	return (jint)gtk_widget_event((GtkWidget*)widget, (GdkEvent*)event);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_reparent
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1reparent
  (JNIEnv *env, jclass that, jint widget, jint new_parent)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_reparent");
#endif

	gtk_widget_reparent((GtkWidget*)widget, (GtkWidget*)new_parent);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_grab_focus
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1grab_1focus
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_grab_focus");
#endif

	gtk_widget_grab_focus((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_state
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1state
  (JNIEnv *env, jclass that, jint widget, jint state)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_state");
#endif

	gtk_widget_set_state((GtkWidget*)widget, (GtkStateType)state);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_sensitive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1sensitive
  (JNIEnv *env, jclass that, jint widget, jboolean sensitive)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_sensitive");
#endif

	gtk_widget_set_sensitive((GtkWidget*)widget, (gboolean)sensitive);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_parent
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1parent
  (JNIEnv *env, jclass that, jint widget, jint parent)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_parent");
#endif

	gtk_widget_set_parent((GtkWidget*)widget, (GtkWidget*)parent);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_uposition
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1uposition
  (JNIEnv *env, jclass that, jint widget, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_uposition");
#endif

	gtk_widget_set_uposition((GtkWidget*)widget, (gint)x, (gint)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_usize
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1usize
  (JNIEnv *env, jclass that, jint widget, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_usize");
#endif

	gtk_widget_set_usize((GtkWidget*)widget, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_add_events
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1add_1events
  (JNIEnv *env, jclass that, jint widget, jint events)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_add_events");
#endif

	gtk_widget_add_events((GtkWidget*)widget, (gint)events);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_set_style
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1set_1style
  (JNIEnv *env, jclass that, jint widget, jint style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_set_style");
#endif

	gtk_widget_set_style((GtkWidget*)widget, (GtkStyle*)style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_ensure_style
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1ensure_1style
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_ensure_style");
#endif

	gtk_widget_ensure_style((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_get_style
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1style
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_get_style");
#endif

	return (jint)gtk_widget_get_style((GtkWidget*)widget);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_widget_get_default_style
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1widget_1get_1default_1style
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_widget_get_default_style");
#endif

	return (jint)gtk_widget_get_default_style();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_window_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1new
  (JNIEnv *env, jclass that, jint type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_new");
#endif

	return (jint)gtk_window_new((GtkWindowType)type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_window_set_title
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1title
  (JNIEnv *env, jclass that, jint window, jbyteArray title)
{
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_set_title");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	gtk_window_set_title((GtkWindow*)window, (gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1transient_1for
  (JNIEnv *env, jclass that, jint window, jint parent)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_set_transient_for");
#endif
	gtk_window_set_transient_for((GtkWindow*)window, (GtkWindow*)parent);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1modal
  (JNIEnv *env, jclass that, jint window, jboolean modal)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_set_modal");
#endif
	gtk_window_set_modal((GtkWindow*)window, modal);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_window_set_policy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1set_1policy
  (JNIEnv *env, jclass that, jint window, jint allow_shrink, jint allow_grow, jint auto_shrink)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_set_policy");
#endif

	gtk_window_set_policy((GtkWindow*)window, (gint)allow_shrink, (gint)allow_grow, (gint)auto_shrink);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_window_add_accel_group
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1window_1add_1accel_1group
  (JNIEnv *env, jclass that, jint window, jint accel_group)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_window_add_accel_group");
#endif

	gtk_window_add_accel_group((GtkWindow*)window, (GtkAccelGroup*)accel_group);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_event_get
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_event_get");
#endif

	return (jint)gdk_event_get();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_event_get_graphics_expose
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1graphics_1expose
  (JNIEnv *env, jclass that, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_event_get_graphics_expose");
#endif

	return (jint)gdk_event_get_graphics_expose((GdkWindow*)window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_event_free
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1free
  (JNIEnv *env, jclass that, jint event)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_event_free");
#endif

	gdk_event_free((GdkEvent*)event);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_time_get
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1time_1get
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_time_get");
#endif

	return (jint)gdk_time_get();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_screen_width
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_screen_width");
#endif

	return (jint)gdk_screen_width();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_screen_height
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1height
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_screen_height");
#endif

	return (jint)gdk_screen_height();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_screen_width_mm
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width_1mm
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_screen_width_mm");
#endif

	return (jint)gdk_screen_width_mm();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_flush
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1flush
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_flush");
#endif

	gdk_flush();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_beep
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1beep
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_beep");
#endif

	gdk_beep();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_visual_get_system
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1visual_1get_1system
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_visual_get_system");
#endif

	return (jint)gdk_visual_get_system();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_at_pointer
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1at_1pointer
  (JNIEnv *env, jclass that, jintArray win_x, jintArray win_y)
{
	jint rc;
	jint *win_x1 = NULL;
	jint *win_y1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_at_pointer");
#endif

	if (win_x) {
		win_x1 = (*env)->GetIntArrayElements(env, win_x, NULL);
	}
	if (win_y) {
		win_y1 = (*env)->GetIntArrayElements(env, win_y, NULL);
	}
	rc = (jint)gdk_window_at_pointer((gint*)win_x1, (gint*)win_y1);
	if (win_x) {
		(*env)->ReleaseIntArrayElements(env, win_x, win_x1, 0);
	}
	if (win_y) {
		(*env)->ReleaseIntArrayElements(env, win_y, win_y1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_clear_area
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1clear_1area
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_clear_area");
#endif

	gdk_window_clear_area((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_clear_area_e
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1clear_1area_1e
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_clear_area_e");
#endif

	gdk_window_clear_area_e((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_copy_area
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1copy_1area
  (JNIEnv *env, jclass that, jint window, jint gc, jint x, jint y, jint source_window, jint source_x, jint source_y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_copy_area");
#endif

	gdk_window_copy_area((GdkWindow*)window, (GdkGC*)gc, (gint)x, (gint)y, (GdkWindow*)source_window, (gint)source_x, (gint)source_y, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_raise
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1raise
  (JNIEnv *env, jclass that, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_raise");
#endif

	gdk_window_raise((GdkWindow*)window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_lower
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1lower
  (JNIEnv *env, jclass that, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_lower");
#endif

	gdk_window_lower((GdkWindow*)window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_set_user_data
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1user_1data
  (JNIEnv *env, jclass that, jint window, jint user_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_set_user_data");
#endif

	gdk_window_set_user_data((GdkWindow*)window, (gpointer)user_data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_set_cursor
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1cursor
  (JNIEnv *env, jclass that, jint window, jint cursor)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_set_cursor");
#endif

	gdk_window_set_cursor((GdkWindow*)window, (GdkCursor*)cursor);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_get_user_data
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1user_1data
  (JNIEnv *env, jclass that, jint window, jintArray data)
{
	jint *data1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_get_user_data");
#endif

	if (data) {
		data1 = (*env)->GetIntArrayElements(env, data, NULL);
	}
	gdk_window_get_user_data((GdkWindow*)window, (gpointer*)data1);
	if (data) {
		(*env)->ReleaseIntArrayElements(env, data, data1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_get_geometry
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1geometry
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y, jintArray width, jintArray height, jintArray depth)
{
	jint *x1 = NULL;
	jint *y1 = NULL;
	jint *width1 = NULL;
	jint *height1 = NULL;
	jint *depth1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_get_geometry");
#endif

	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (height) {
		height1 = (*env)->GetIntArrayElements(env, height, NULL);
	}
	if (depth) {
		depth1 = (*env)->GetIntArrayElements(env, depth, NULL);
	}
	gdk_window_get_geometry((GdkWindow*)window, (gint*)x1, (gint*)y1, (gint*)width1, (gint*)height1, (gint*)depth1);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (height) {
		(*env)->ReleaseIntArrayElements(env, height, height1, 0);
	}
	if (depth) {
		(*env)->ReleaseIntArrayElements(env, depth, depth1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_get_origin
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1origin
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y)
{
	jint rc;
	jint *x1 = NULL;
	jint *y1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_get_origin");
#endif

	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	rc = (jint)gdk_window_get_origin((GdkWindow*)window, (gint*)x1, (gint*)y1);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_get_pointer
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1pointer
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y, jint mask)
{
	jint rc;
	jint *x1 = NULL;
	jint *y1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_get_pointer");
#endif

	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	rc = (jint)gdk_window_get_pointer((GdkWindow*)window, (gint*)x1, (gint*)y1, (GdkModifierType*)mask);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_set_icon
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1icon
  (JNIEnv *env, jclass that, jint window, jint icon_window, jint pixmap, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_set_icon");
#endif

	gdk_window_set_icon((GdkWindow*)window, (GdkWindow*)icon_window, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_window_set_decorations
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1decorations
  (JNIEnv *env, jclass that, jint window, jint decorations)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_set_decorations");
#endif

	gdk_window_set_decorations((GdkWindow*)window, (GdkWMDecoration)decorations);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1show
  (JNIEnv *env, jclass that, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_window_show");
#endif

	gdk_window_show((GdkWindow*)window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_cursor_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new
  (JNIEnv *env, jclass that, jint cursor_type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_cursor_new");
#endif

	return (jint)gdk_cursor_new((GdkCursorType)cursor_type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_cursor_new_from_pixmap
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new_1from_1pixmap
  (JNIEnv *env, jclass that, jint source, jint mask, jobject fg, jobject bg, jint x, jint y)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkColor fg_struct, *fg1 = NULL;
	GdkColor bg_struct, *bg1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_cursor_new_from_pixmap");
#endif

	if (fg) {
		fg1 = &fg_struct;
		cacheGdkColorFids(env, fg, &PGLOB(GdkColorFc));
		getGdkColorFields(env, fg, fg1, &PGLOB(GdkColorFc));
	}
	if (bg) {
		bg1 = &bg_struct;
		cacheGdkColorFids(env, bg, &PGLOB(GdkColorFc));
		getGdkColorFields(env, bg, bg1, &PGLOB(GdkColorFc));
	}
	rc = (jint)gdk_cursor_new_from_pixmap((GdkPixmap*)source, (GdkPixmap*)mask, (GdkColor*)fg1, (GdkColor*)bg1, (gint)x, (gint)y);
	if (fg) {
		setGdkColorFields(env, fg, fg1, &PGLOB(GdkColorFc));
	}
	if (bg) {
		setGdkColorFields(env, bg, bg1, &PGLOB(GdkColorFc));
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_cursor_destroy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1destroy
  (JNIEnv *env, jclass that, jint cursor)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_cursor_destroy");
#endif

	gdk_cursor_destroy((GdkCursor*)cursor);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1new
  (JNIEnv *env, jclass that, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_new");
#endif

	return (jint)gdk_gc_new((GdkWindow*)window);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1unref
  (JNIEnv *env, jclass that, jint gc)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_unref");
#endif

	gdk_gc_unref((GdkGC*)gc);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_destroy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1destroy
  (JNIEnv *env, jclass that, jint gc)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_destroy");
#endif

	gdk_gc_destroy((GdkGC*)gc);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_get_values
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1get_1values
  (JNIEnv *env, jclass that, jint gc, jobject values)
{
	DECL_GLOB(pGlob)
	GdkGCValues values_struct, *values1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_get_values");
#endif

	if (values) {
		values1 = &values_struct;
		cacheGdkGCValuesFids(env, values, &PGLOB(GdkGCValuesFc));
		getGdkGCValuesFields(env, values, values1, &PGLOB(GdkGCValuesFc));
	}
	gdk_gc_get_values((GdkGC*)gc, (GdkGCValues*)values1);
	if (values) {
		setGdkGCValuesFields(env, values, values1, &PGLOB(GdkGCValuesFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_foreground
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1foreground
  (JNIEnv *env, jclass that, jint gc, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_foreground");
#endif

	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_gc_set_foreground((GdkGC*)gc, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_background
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1background
  (JNIEnv *env, jclass that, jint gc, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_background");
#endif

	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_gc_set_background((GdkGC*)gc, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_font
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1font
  (JNIEnv *env, jclass that, jint gc, jint font)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_font");
#endif

	gdk_gc_set_font((GdkGC*)gc, (GdkFont*)font);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_function
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1function
  (JNIEnv *env, jclass that, jint gc, jint function)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_function");
#endif

	gdk_gc_set_function((GdkGC*)gc, (GdkFunction)function);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_fill
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1fill
  (JNIEnv *env, jclass that, jint gc, jint fill)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_fill");
#endif

	gdk_gc_set_fill((GdkGC*)gc, (GdkFill)fill);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_stipple
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1stipple
  (JNIEnv *env, jclass that, jint gc, jint stipple)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_stipple");
#endif

	gdk_gc_set_stipple((GdkGC*)gc, (GdkPixmap*)stipple);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_clip_mask
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1mask
  (JNIEnv *env, jclass that, jint gc, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_clip_mask");
#endif

	gdk_gc_set_clip_mask((GdkGC*)gc, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_clip_rectangle
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1rectangle
  (JNIEnv *env, jclass that, jint gc, jobject rectangle)
{
	DECL_GLOB(pGlob)
	GdkRectangle rectangle_struct, *rectangle1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_clip_rectangle");
#endif

	if (rectangle) {
		rectangle1 = &rectangle_struct;
		cacheGdkRectangleFids(env, rectangle, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
	gdk_gc_set_clip_rectangle((GdkGC*)gc, (GdkRectangle*)rectangle1);
	if (rectangle) {
		setGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_clip_region
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1region
  (JNIEnv *env, jclass that, jint gc, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_clip_region");
#endif

	gdk_gc_set_clip_region((GdkGC*)gc, (GdkRegion*)region);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_subwindow
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1subwindow
  (JNIEnv *env, jclass that, jint gc, jint mode)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_subwindow");
#endif

	gdk_gc_set_subwindow((GdkGC*)gc, (GdkSubwindowMode)mode);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_exposures
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1exposures
  (JNIEnv *env, jclass that, jint gc, jboolean exposures)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_exposures");
#endif

	gdk_gc_set_exposures((GdkGC*)gc, (gboolean)exposures);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_line_attributes
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1line_1attributes
  (JNIEnv *env, jclass that, jint gc, jint line_width, jint line_style, jint cap_style, jint join_style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_line_attributes");
#endif

	gdk_gc_set_line_attributes((GdkGC*)gc, (gint)line_width, (GdkLineStyle)line_style, (GdkCapStyle)cap_style, (GdkJoinStyle)join_style);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_gc_set_dashes
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1dashes
  (JNIEnv *env, jclass that, jint gc, jint dash_offset, jbyteArray dash_list, jint n)
{
	jbyte *dash_list1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_gc_set_dashes");
#endif

	if (dash_list) {
		dash_list1 = (*env)->GetByteArrayElements(env, dash_list, NULL);
	}
	gdk_gc_set_dashes((GdkGC*)gc, (gint)dash_offset, (gint8*)dash_list1, (gint)n);
	if (dash_list) {
		(*env)->ReleaseByteArrayElements(env, dash_list, dash_list1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_pixmap_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixmap_1new
  (JNIEnv *env, jclass that, jint window, jint width, jint height, jint depth)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_pixmap_new");
#endif

	return (jint)gdk_pixmap_new((GdkWindow*)window, (gint)width, (gint)height, (gint)depth);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_bitmap_create_from_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1bitmap_1create_1from_1data
  (JNIEnv *env, jclass that, jint window, jbyteArray data, jint width, jint height)
{
	jint rc;
	jbyte *data1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_bitmap_create_from_data");
#endif

	if (data) {
		data1 = (*env)->GetByteArrayElements(env, data, NULL);
	}
	rc = (jint)gdk_bitmap_create_from_data((GdkWindow*)window, (gchar*)data1, (gint)width, (gint)height);
	if (data) {
		(*env)->ReleaseByteArrayElements(env, data, data1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_pixmap_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixmap_1unref
  (JNIEnv *env, jclass that, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_pixmap_unref");
#endif

	gdk_pixmap_unref((GdkPixmap*)pixmap);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_bitmap_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1bitmap_1unref
  (JNIEnv *env, jclass that, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_bitmap_unref");
#endif

	gdk_bitmap_unref((GdkBitmap*)pixmap);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_image_get
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_image_get");
#endif

	return (jint)gdk_image_get((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_image_get_pixel
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get_1pixel
  (JNIEnv *env, jclass that, jint image, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_image_get_pixel");
#endif

	return (jint)gdk_image_get_pixel((GdkImage*)image, (gint)x, (gint)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_colormap_get_system
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1get_1system
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_colormap_get_system");
#endif

	return (jint)gdk_colormap_get_system();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_color_free
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1free
  (JNIEnv *env, jclass that, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_color_free");
#endif

	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_color_free((GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_colors_free
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colors_1free
  (JNIEnv *env, jclass that, jint colormap, jintArray pixels, jint npixels, jint planes)
{
	jint *pixels1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_colors_free");
#endif

	if (pixels) {
		pixels1 = (*env)->GetIntArrayElements(env, pixels, NULL);
	}
	gdk_colors_free((GdkColormap*)colormap, (gulong*)pixels1, (gint)npixels, (gulong)planes);
	if (pixels) {
		(*env)->ReleaseIntArrayElements(env, pixels, pixels1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_color_white
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1white
  (JNIEnv *env, jclass that, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	jboolean rc;
	GdkColor color_struct, *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_color_white");
#endif

	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	rc = (jboolean)gdk_color_white((GdkColormap*)colormap, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_color_alloc
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1alloc
  (JNIEnv *env, jclass that, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	jboolean rc;
	GdkColor color_struct, *color1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_color_alloc");
#endif

	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	rc = (jboolean)gdk_color_alloc((GdkColormap*)colormap, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_font_load
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1load
  (JNIEnv *env, jclass that, jbyteArray font_name)
{
	jint rc;
	jbyte *font_name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_font_load");
#endif

	if (font_name) {
		font_name1 = (*env)->GetByteArrayElements(env, font_name, NULL);
	}
	rc = (jint)gdk_font_load((gchar*)font_name1);
	if (font_name) {
		(*env)->ReleaseByteArrayElements(env, font_name, font_name1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_font_ref
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1ref
  (JNIEnv *env, jclass that, jint font)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_font_ref");
#endif

	return (jint)gdk_font_ref((GdkFont*)font);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_font_unref
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1unref
  (JNIEnv *env, jclass that, jint font)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_font_unref");
#endif

	gdk_font_unref((GdkFont*)font);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_font_equal
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1equal
  (JNIEnv *env, jclass that, jint fonta, jint fontb)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_font_equal");
#endif

	return (jboolean)gdk_font_equal((GdkFont*)fonta, (GdkFont*)fontb);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_string_width
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1width
  (JNIEnv *env, jclass that, jint font, jbyteArray string)
{
	jint rc;
	jbyte *string1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_string_width");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	rc = (jint)gdk_string_width((GdkFont*)font, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_char_width
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1char_1width
  (JNIEnv *env, jclass that, jint font, jbyte character)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_char_width");
#endif

	return (jint)gdk_char_width((GdkFont*)font, (gchar)character);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_string_height
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1height
  (JNIEnv *env, jclass that, jint font, jbyteArray string)
{
	jint rc;
	jbyte *string1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_string_height");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	rc = (jint)gdk_string_height((GdkFont*)font, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_string_extents
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1extents
  (JNIEnv *env, jclass that, jint font, jbyteArray string, jintArray lbearing, jintArray rbearing, jintArray width, jintArray ascent, jintArray descent)
{
	jbyte *string1 = NULL;
	jint *lbearing1 = NULL;
	jint *rbearing1 = NULL;
	jint *width1 = NULL;
	jint *ascent1 = NULL;
	jint *descent1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_string_extents");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	if (lbearing) {
		lbearing1 = (*env)->GetIntArrayElements(env, lbearing, NULL);
	}
	if (rbearing) {
		rbearing1 = (*env)->GetIntArrayElements(env, rbearing, NULL);
	}
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (ascent) {
		ascent1 = (*env)->GetIntArrayElements(env, ascent, NULL);
	}
	if (descent) {
		descent1 = (*env)->GetIntArrayElements(env, descent, NULL);
	}
	gdk_string_extents((GdkFont*)font, (gchar*)string1, (gint*)lbearing1, (gint*)rbearing1, (gint*)width1, (gint*)ascent1, (gint*)descent1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	if (lbearing) {
		(*env)->ReleaseIntArrayElements(env, lbearing, lbearing1, 0);
	}
	if (rbearing) {
		(*env)->ReleaseIntArrayElements(env, rbearing, rbearing1, 0);
	}
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (ascent) {
		(*env)->ReleaseIntArrayElements(env, ascent, ascent1, 0);
	}
	if (descent) {
		(*env)->ReleaseIntArrayElements(env, descent, descent1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_line
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1line
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint x1, jint y1, jint x2, jint y2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_line");
#endif

	gdk_draw_line((GdkDrawable*)drawable, (GdkGC*)gc, (gint)x1, (gint)y1, (gint)x2, (gint)y2);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_rectangle
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1rectangle
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_rectangle");
#endif

	gdk_draw_rectangle((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (gint)x, (gint)y, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_arc
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1arc
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jint x, jint y, jint width, jint height, jint angle1, jint angle2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_arc");
#endif

	gdk_draw_arc((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (gint)x, (gint)y, (gint)width, (gint)height, (gint)angle1, (gint)angle2);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_polygon
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1polygon
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jshortArray points, jint npoints)
{
	jshort *points1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_polygon");
#endif

	if (points) {
		points1 = (*env)->GetShortArrayElements(env, points, NULL);
	}
	gdk_draw_polygon((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (GdkPoint*)points1, (gint)npoints);
	if (points) {
		(*env)->ReleaseShortArrayElements(env, points, points1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_string
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1string
  (JNIEnv *env, jclass that, jint drawable, jint font, jint gc, jint x, jint y, jbyteArray string)
{
	jbyte *string1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_string");
#endif

	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	gdk_draw_string((GdkDrawable*)drawable, (GdkFont*)font, (GdkGC*)gc, (gint)x, (gint)y, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_pixmap
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1pixmap
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint src, jint xsrc, jint ysrc, jint xdest, jint ydest, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_pixmap");
#endif

	gdk_draw_pixmap((GdkDrawable*)drawable, (GdkGC*)gc, (GdkDrawable*)src, (gint)xsrc, (gint)ysrc, (gint)xdest, (gint)ydest, (gint)width, (gint)height);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_draw_lines
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1lines
  (JNIEnv *env, jclass that, jint drawable, jint gc, jshortArray points, jint npoints)
{
	jshort *points1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_draw_lines");
#endif

	if (points) {
		points1 = (*env)->GetShortArrayElements(env, points, NULL);
	}
	gdk_draw_lines((GdkDrawable*)drawable, (GdkGC*)gc, (GdkPoint*)points1, (gint)npoints);
	if (points) {
		(*env)->ReleaseShortArrayElements(env, points, points1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_atom_intern
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1atom_1intern
  (JNIEnv *env, jclass that, jbyteArray atom_name, jint only_if_exists)
{
	jint rc;
	jbyte *atom_name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_atom_intern");
#endif

	if (atom_name) {
		atom_name1 = (*env)->GetByteArrayElements(env, atom_name, NULL);
	}
	rc = (jint)gdk_atom_intern((gchar*)atom_name1, (gint)only_if_exists);
	if (atom_name) {
		(*env)->ReleaseByteArrayElements(env, atom_name, atom_name1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1new
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_new");
#endif

	return (jint)gdk_region_new();
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_destroy
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1destroy
  (JNIEnv *env, jclass that, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_destroy");
#endif

	gdk_region_destroy((GdkRegion*)region);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_get_clipbox
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1get_1clipbox
  (JNIEnv *env, jclass that, jint region, jobject rectangle)
{
	DECL_GLOB(pGlob)
	GdkRectangle rectangle_struct, *rectangle1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_get_clipbox");
#endif

	if (rectangle) {
		rectangle1 = &rectangle_struct;
		cacheGdkRectangleFids(env, rectangle, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
	gdk_region_get_clipbox((GdkRegion*)region, (GdkRectangle*)rectangle1);
	if (rectangle) {
		setGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_empty
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1empty
  (JNIEnv *env, jclass that, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_empty");
#endif

	return (jboolean)gdk_region_empty((GdkRegion*)region);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_equal
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1equal
  (JNIEnv *env, jclass that, jint region1, jint region2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_equal");
#endif

	return (jboolean)gdk_region_equal((GdkRegion*)region1, (GdkRegion*)region2);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_point_in
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1point_1in
  (JNIEnv *env, jclass that, jint region, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_point_in");
#endif

	return (jboolean)gdk_region_point_in((GdkRegion*)region, (int)x, (int)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_rect_in
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1rect_1in
  (JNIEnv *env, jclass that, jint region, jobject rect)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkRectangle rect_struct, *rect1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_rect_in");
#endif

	if (rect) {
		rect1 = &rect_struct;
		cacheGdkRectangleFids(env, rect, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	rc = (jint)gdk_region_rect_in((GdkRegion*)region, (GdkRectangle*)rect1);
	if (rect) {
		setGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_region_union_with_rect
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1union_1with_1rect
  (JNIEnv *env, jclass that, jint region, jobject rect)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkRectangle rect_struct, *rect1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_region_union_with_rect");
#endif

	if (rect) {
		rect1 = &rect_struct;
		cacheGdkRectangleFids(env, rect, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	rc = (jint)gdk_region_union_with_rect((GdkRegion*)region, (GdkRectangle*)rect1);
	if (rect) {
		setGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_regions_union
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1regions_1union
  (JNIEnv *env, jclass that, jint source1, jint source2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_regions_union");
#endif

	return (jint)gdk_regions_union((GdkRegion*)source1, (GdkRegion*)source2);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gdk_regions_subtract
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1regions_1subtract
  (JNIEnv *env, jclass that, jint source1, jint source2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gdk_regions_subtract");
#endif

	return (jint)gdk_regions_subtract((GdkRegion*)source1, (GdkRegion*)source2);
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

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1new
  (JNIEnv *env, jclass that, jint columns)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_new");
#endif

	return (jint)gtk_clist_new((gint)columns);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_shadow_type
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1shadow_1type
  (JNIEnv *env, jclass that, jint clist, jint type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_shadow_type");
#endif

	gtk_clist_set_shadow_type((GtkCList*)clist, (GtkShadowType)type);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_selection_mode
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1selection_1mode
  (JNIEnv *env, jclass that, jint clist, jint mode)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_selection_mode");
#endif

	gtk_clist_set_selection_mode((GtkCList*)clist, (GtkSelectionMode)mode);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_freeze
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1freeze
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_freeze");
#endif

	gtk_clist_freeze((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_thaw
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1thaw
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_thaw");
#endif

	gtk_clist_thaw((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_column_titles_show
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1show
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_column_titles_show");
#endif

	gtk_clist_column_titles_show((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_column_titles_hide
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1hide
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_column_titles_hide");
#endif

	gtk_clist_column_titles_hide((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_column_title_passive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1title_1passive
  (JNIEnv *env, jclass that, jint clist, jint column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_column_title_passive");
#endif

	gtk_clist_column_title_passive((GtkCList*)clist, (gint)column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_column_titles_passive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1passive
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_column_titles_passive");
#endif

	gtk_clist_column_titles_passive((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_title
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1title
  (JNIEnv *env, jclass that, jint clist, jint column, jbyteArray title)
{
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_title");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	gtk_clist_set_column_title((GtkCList*)clist, (gint)column, (gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_justification
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1justification
  (JNIEnv *env, jclass that, jint clist, jint column, jint justification)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_justification");
#endif

	gtk_clist_set_column_justification((GtkCList*)clist, (gint)column, (GtkJustification)justification);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_visibility
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1visibility
  (JNIEnv *env, jclass that, jint clist, jint column, jboolean visible)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_visibility");
#endif

	gtk_clist_set_column_visibility((GtkCList*)clist, (gint)column, (gboolean)visible);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_resizeable
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1resizeable
  (JNIEnv *env, jclass that, jint clist, jint column, jboolean resizeable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_resizeable");
#endif

	gtk_clist_set_column_resizeable((GtkCList*)clist, (gint)column, (gboolean)resizeable);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_width
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1width
  (JNIEnv *env, jclass that, jint clist, jint column, jint width)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_width");
#endif

	gtk_clist_set_column_width((GtkCList*)clist, (gint)column, (gint)width);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_moveto
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1moveto
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jfloat row_align, jfloat col_align)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_moveto");
#endif

	gtk_clist_moveto((GtkCList*)clist, (gint)row, (gint)column, row_align, col_align);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1text
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jbyteArray text)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_text");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_clist_set_text((GtkCList*)clist, (gint)row, (gint)column, (gchar*)text1);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_get_text
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1text
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_get_text");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_get_text((GtkCList*)clist, (gint)row, (gint)column, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_pixmap
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1pixmap
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jint pixmap, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_pixmap");
#endif

	gtk_clist_set_pixmap((GtkCList*)clist, (gint)row, (gint)column, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_pixtext
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1pixtext
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jbyteArray text, jbyte spacing, jint pixmap, jint mask)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_pixtext");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_clist_set_pixtext((GtkCList*)clist, (gint)row, (gint)column, (gchar*)text1, (guint8)spacing, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_append
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1append
  (JNIEnv *env, jclass that, jint clist, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_append");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_append((GtkCList*)clist, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_insert
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1insert
  (JNIEnv *env, jclass that, jint clist, jint row, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_insert");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_insert((GtkCList*)clist, (gint)row, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_remove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1remove
  (JNIEnv *env, jclass that, jint clist, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_remove");
#endif

	gtk_clist_remove((GtkCList*)clist, (gint)row);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_select_row
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1row
  (JNIEnv *env, jclass that, jint clist, jint row, jint column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_select_row");
#endif

	gtk_clist_select_row((GtkCList*)clist, (gint)row, (gint)column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_unselect_row
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1row
  (JNIEnv *env, jclass that, jint clist, jint row, jint column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_unselect_row");
#endif

	gtk_clist_unselect_row((GtkCList*)clist, (gint)row, (gint)column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_clear
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1clear
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_clear");
#endif

	gtk_clist_clear((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_get_selection_info
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1selection_1info
  (JNIEnv *env, jclass that, jint clist, jint x, jint y, jintArray row, jintArray column)
{
	jint rc;
	jint *row1 = NULL;
	jint *column1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_get_selection_info");
#endif

	if (row) {
		row1 = (*env)->GetIntArrayElements(env, row, NULL);
	}
	if (column) {
		column1 = (*env)->GetIntArrayElements(env, column, NULL);
	}
	rc = (jint)gtk_clist_get_selection_info((GtkCList*)clist, (gint)x, (gint)y, (gint*)row1, (gint*)column1);
	if (row) {
		(*env)->ReleaseIntArrayElements(env, row, row1, 0);
	}
	if (column) {
		(*env)->ReleaseIntArrayElements(env, column, column1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_select_all
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1all
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_select_all");
#endif

	gtk_clist_select_all((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_unselect_all
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1all
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_unselect_all");
#endif

	gtk_clist_unselect_all((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkColor_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkColor src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkColorFids(env, src, &PGLOB(GdkColorFc));
		getGdkColorFields(env, src, src1, &PGLOB(GdkColorFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkColor_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkColor dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkColorFids(env, dest, &PGLOB(GdkColorFc));
		setGdkColorFields(env, dest, dest1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkDialog_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkDialog dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkDialog_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkDialogFids(env, dest, &PGLOB(GtkDialogFc));
		setGtkDialogFields(env, dest, dest1, &PGLOB(GtkDialogFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyleClass_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkStyleClass dest_struct, *dest1 = NULL;

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkStyleClassFids(env, dest, &PGLOB(GtkStyleClassFc));
		setGtkStyleClassFields(env, dest, dest1, &PGLOB(GtkStyleClassFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventKey_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkEventKeyFids(env, src, &PGLOB(GdkEventKeyFc));
		getGdkEventKeyFields(env, src, src1, &PGLOB(GdkEventKeyFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkEventKey_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkEventKeyFids(env, dest, &PGLOB(GdkEventKeyFc));
		setGdkEventKeyFields(env, dest, dest1, &PGLOB(GdkEventKeyFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkEventButtonFids(env, src, &PGLOB(GdkEventButtonFc));
		getGdkEventButtonFields(env, src, src1, &PGLOB(GdkEventButtonFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkEventButtonFids(env, dest, &PGLOB(GdkEventButtonFc));
		setGdkEventButtonFields(env, dest, dest1, &PGLOB(GdkEventButtonFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkEventMotionFids(env, src, &PGLOB(GdkEventMotionFc));
		getGdkEventMotionFields(env, src, src1, &PGLOB(GdkEventMotionFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkEventMotion_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkEventMotionFids(env, dest, &PGLOB(GdkEventMotionFc));
		setGdkEventMotionFields(env, dest, dest1, &PGLOB(GdkEventMotionFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkEventExposeFids(env, src, &PGLOB(GdkEventExposeFc));
		getGdkEventExposeFields(env, src, src1, &PGLOB(GdkEventExposeFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkEventExpose_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkEvent dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkEventExposeFids(env, dest, &PGLOB(GdkEventExposeFc));
		setGdkEventExposeFields(env, dest, dest1, &PGLOB(GdkEventExposeFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkFont_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkFont src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkFontFids(env, src, &PGLOB(GdkFontFc));
		getGdkFontFields(env, src, src1, &PGLOB(GdkFontFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkFont_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkFont_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkFont dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkFont_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkFontFids(env, dest, &PGLOB(GdkFontFc));
		setGdkFontFields(env, dest, dest1, &PGLOB(GdkFontFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkGCValues_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkGCValues src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkGCValuesFids(env, src, &PGLOB(GdkGCValuesFc));
		getGdkGCValuesFields(env, src, src1, &PGLOB(GdkGCValuesFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkGCValues_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkGCValues_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkGCValues dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkGCValues_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkGCValuesFids(env, dest, &PGLOB(GdkGCValuesFc));
		setGdkGCValuesFields(env, dest, dest1, &PGLOB(GdkGCValuesFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkImage_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkImage src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkImageFids(env, src, &PGLOB(GdkImageFc));
		getGdkImageFields(env, src, src1, &PGLOB(GdkImageFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkImage_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkImage dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkImageFids(env, dest, &PGLOB(GdkImageFc));
		setGdkImageFields(env, dest, dest1, &PGLOB(GdkImageFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkPoint_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkPoint src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkPointFids(env, src, &PGLOB(GdkPointFc));
		getGdkPointFields(env, src, src1, &PGLOB(GdkPointFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkPoint_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkPoint_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkPoint dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkPoint_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkPointFids(env, dest, &PGLOB(GdkPointFc));
		setGdkPointFields(env, dest, dest1, &PGLOB(GdkPointFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkRectangle_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkRectangle src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkRectangleFids(env, src, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, src, src1, &PGLOB(GdkRectangleFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkRectangle_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkRectangle dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkRectangle_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkRectangleFids(env, dest, &PGLOB(GdkRectangleFc));
		setGdkRectangleFields(env, dest, dest1, &PGLOB(GdkRectangleFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GdkVisual_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GdkVisual src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGdkVisualFids(env, src, &PGLOB(GdkVisualFc));
		getGdkVisualFields(env, src, src1, &PGLOB(GdkVisualFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GdkVisual_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GdkVisual dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGdkVisualFids(env, dest, &PGLOB(GdkVisualFc));
		setGdkVisualFields(env, dest, dest1, &PGLOB(GdkVisualFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkAllocation_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkAllocation src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkAllocationFids(env, src, &PGLOB(GtkAllocationFc));
		getGtkAllocationFields(env, src, src1, &PGLOB(GtkAllocationFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAllocation_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkAllocation_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkAllocation dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAllocation_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkAllocationFids(env, dest, &PGLOB(GtkAllocationFc));
		setGtkAllocationFields(env, dest, dest1, &PGLOB(GtkAllocationFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkArg_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkArg src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkArgFids(env, src, &PGLOB(GtkArgFc));
		getGtkArgFields(env, src, src1, &PGLOB(GtkArgFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkArg_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkArg_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkArg dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkArg_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkArgFids(env, dest, &PGLOB(GtkArgFc));
		setGtkArgFields(env, dest, dest1, &PGLOB(GtkArgFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkBin_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkBin src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkBinFids(env, src, &PGLOB(GtkBinFc));
		getGtkBinFields(env, src, src1, &PGLOB(GtkBinFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkBin_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkBin_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkBin dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkBin_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkBinFids(env, dest, &PGLOB(GtkBinFc));
		setGtkBinFields(env, dest, dest1, &PGLOB(GtkBinFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCList_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCList src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCListFids(env, src, &PGLOB(GtkCListFc));
		getGtkCListFields(env, src, src1, &PGLOB(GtkCListFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCList_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCList_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCList dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCList_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCListFids(env, dest, &PGLOB(GtkCListFc));
		setGtkCListFields(env, dest, dest1, &PGLOB(GtkCListFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkMenu_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkMenu src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkMenuFids(env, src, &PGLOB(GtkMenuFc));
		getGtkMenuFields(env, src, src1, &PGLOB(GtkMenuFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkMenu_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkMenu_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkMenu dest_struct={0}, *dest1 = NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkMenu_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkMenuFids(env, dest, &PGLOB(GtkMenuFc));
		setGtkMenuFields(env, dest, dest1, &PGLOB(GtkMenuFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCTree_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCTree src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCTreeFids(env, src, &PGLOB(GtkCTreeFc));
		getGtkCTreeFields(env, src, src1, &PGLOB(GtkCTreeFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCTree_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCTree_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCTree dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCTree_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCTreeFids(env, dest, &PGLOB(GtkCTreeFc));
		setGtkCTreeFields(env, dest, dest1, &PGLOB(GtkCTreeFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkColorSelectionDialog src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkColorSelectionDialogFids(env, src, &PGLOB(GtkColorSelectionDialogFc));
		getGtkColorSelectionDialogFields(env, src, src1, &PGLOB(GtkColorSelectionDialogFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkColorSelectionDialog dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkColorSelectionDialog_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkColorSelectionDialogFids(env, dest, &PGLOB(GtkColorSelectionDialogFc));
		setGtkColorSelectionDialogFields(env, dest, dest1, &PGLOB(GtkColorSelectionDialogFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCombo_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCombo src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkComboFids(env, src, &PGLOB(GtkComboFc));
		getGtkComboFields(env, src, src1, &PGLOB(GtkComboFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCombo_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCombo dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkComboFids(env, dest, &PGLOB(GtkComboFc));
		setGtkComboFields(env, dest, dest1, &PGLOB(GtkComboFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkContainer_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkContainer src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkContainerFids(env, src, &PGLOB(GtkContainerFc));
		getGtkContainerFields(env, src, src1, &PGLOB(GtkContainerFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkContainer_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkContainer_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkContainer dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkContainer_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkContainerFids(env, dest, &PGLOB(GtkContainerFc));
		setGtkContainerFields(env, dest, dest1, &PGLOB(GtkContainerFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkData_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkData src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkDataFids(env, src, &PGLOB(GtkDataFc));
		getGtkDataFields(env, src, src1, &PGLOB(GtkDataFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkData_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkData_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkData dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkData_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkDataFids(env, dest, &PGLOB(GtkDataFc));
		setGtkDataFields(env, dest, dest1, &PGLOB(GtkDataFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkEditable_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkEditable src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkEditableFids(env, src, &PGLOB(GtkEditableFc));
		getGtkEditableFields(env, src, src1, &PGLOB(GtkEditableFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkEditable_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkEditable_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkEditable dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkEditable_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkEditableFids(env, dest, &PGLOB(GtkEditableFc));
		setGtkEditableFields(env, dest, dest1, &PGLOB(GtkEditableFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkText_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkText src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkTextFids(env, src, &PGLOB(GtkTextFc));
		getGtkTextFields(env, src, src1, &PGLOB(GtkTextFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkText_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkText_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkText dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkText_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkTextFids(env, dest, &PGLOB(GtkTextFc));
		setGtkTextFields(env, dest, dest1, &PGLOB(GtkTextFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_text_inquiry
 * Signature:	
 * Not implemented yet;
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_text_inquiry__Lorg_eclipse_swt_internal_gtk_GtkText_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{

}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkFileSelection_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFileSelection src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkFileSelectionFids(env, src, &PGLOB(GtkFileSelectionFc));
		getGtkFileSelectionFields(env, src, src1, &PGLOB(GtkFileSelectionFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkFileSelection_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFileSelection dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFileSelection_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkFileSelectionFids(env, dest, &PGLOB(GtkFileSelectionFc));
		setGtkFileSelectionFields(env, dest, dest1, &PGLOB(GtkFileSelectionFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkFontSelectionDialog_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFontSelectionDialog src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkFontSelectionDialogFids(env, src, &PGLOB(GtkFontSelectionDialogFc));
		getGtkFontSelectionDialogFields(env, src, src1, &PGLOB(GtkFontSelectionDialogFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkFontSelectionDialog_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkFontSelectionDialog_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFontSelectionDialog dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFontSelectionDialog_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkFontSelectionDialogFids(env, dest, &PGLOB(GtkFontSelectionDialogFc));
		setGtkFontSelectionDialogFields(env, dest, dest1, &PGLOB(GtkFontSelectionDialogFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkObject_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkObject src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkObjectFids(env, src, &PGLOB(GtkObjectFc));
		getGtkObjectFields(env, src, src1, &PGLOB(GtkObjectFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkObject_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkObject_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkObject dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkObject_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkObjectFids(env, dest, &PGLOB(GtkObjectFc));
		setGtkObjectFields(env, dest, dest1, &PGLOB(GtkObjectFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkProgress_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkProgress src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkProgressFids(env, src, &PGLOB(GtkProgressFc));
		getGtkProgressFields(env, src, src1, &PGLOB(GtkProgressFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkProgress_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkProgress_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkProgress dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkProgress_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkProgressFids(env, dest, &PGLOB(GtkProgressFc));
		setGtkProgressFields(env, dest, dest1, &PGLOB(GtkProgressFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkProgressBar_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkProgressBar src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkProgressBarFids(env, src, &PGLOB(GtkProgressBarFc));
		getGtkProgressBarFields(env, src, src1, &PGLOB(GtkProgressBarFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkProgressBar_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkProgressBar_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkProgressBar dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkProgressBar_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkProgressBarFids(env, dest, &PGLOB(GtkProgressBarFc));
		setGtkProgressBarFields(env, dest, dest1, &PGLOB(GtkProgressBarFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkRequisition_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkRequisition src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkRequisitionFids(env, src, &PGLOB(GtkRequisitionFc));
		getGtkRequisitionFields(env, src, src1, &PGLOB(GtkRequisitionFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkRequisition_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkRequisition_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkRequisition dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkRequisition_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkRequisitionFids(env, dest, &PGLOB(GtkRequisitionFc));
		setGtkRequisitionFields(env, dest, dest1, &PGLOB(GtkRequisitionFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkStyle src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkStyleFids(env, src, &PGLOB(GtkStyleFc));
		getGtkStyleFields(env, src, src1, &PGLOB(GtkStyleFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkStyle dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkStyleFids(env, dest, &PGLOB(GtkStyleFc));
		setGtkStyleFields(env, dest, dest1, &PGLOB(GtkStyleFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkWidget_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkWidget src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkWidgetFids(env, src, &PGLOB(GtkWidgetFc));
		getGtkWidgetFields(env, src, src1, &PGLOB(GtkWidgetFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkWidget_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkWidget_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkWidget dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkWidget_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkWidgetFids(env, dest, &PGLOB(GtkWidgetFc));
		setGtkWidgetFields(env, dest, dest1, &PGLOB(GtkWidgetFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkFrame_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFrame src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkFrameFids(env, src, &PGLOB(GtkFrameFc));
		getGtkFrameFields(env, src, src1, &PGLOB(GtkFrameFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkFrame_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkFrame_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkFrame dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkFrame_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkFrameFids(env, dest, &PGLOB(GtkFrameFc));
		setGtkFrameFields(env, dest, dest1, &PGLOB(GtkFrameFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkWindow_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkWindow src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkWindowFids(env, src, &PGLOB(GtkWindowFc));
		getGtkWindowFields(env, src, src1, &PGLOB(GtkWindowFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkWindow_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkWindow_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkWindow dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkWindow_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkWindowFids(env, dest, &PGLOB(GtkWindowFc));
		setGtkWindowFields(env, dest, dest1, &PGLOB(GtkWindowFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCheckMenuItem_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCheckMenuItem src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCheckMenuItemFids(env, src, &PGLOB(GtkCheckMenuItemFc));
		getGtkCheckMenuItemFields(env, src, src1, &PGLOB(GtkCheckMenuItemFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCheckMenuItem_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCheckMenuItem_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCheckMenuItem dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCheckMenuItem_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCheckMenuItemFids(env, dest, &PGLOB(GtkCheckMenuItemFc));
		setGtkCheckMenuItemFields(env, dest, dest1, &PGLOB(GtkCheckMenuItemFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkAdjustment src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkAdjustmentFids(env, src, &PGLOB(GtkAdjustmentFc));
		getGtkAdjustmentFields(env, src, src1, &PGLOB(GtkAdjustmentFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkAdjustment dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkAdjustmentFids(env, dest, &PGLOB(GtkAdjustmentFc));
		setGtkAdjustmentFields(env, dest, dest1, &PGLOB(GtkAdjustmentFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkBox_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkBox src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkBoxFids(env, src, &PGLOB(GtkBoxFc));
		getGtkBoxFields(env, src, src1, &PGLOB(GtkBoxFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkBox_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkBox_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkBox dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkBox_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkBoxFids(env, dest, &PGLOB(GtkBoxFc));
		setGtkBoxFields(env, dest, dest1, &PGLOB(GtkBoxFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkHBox_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkHBox src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkHBoxFids(env, src, &PGLOB(GtkHBoxFc));
		getGtkHBoxFields(env, src, src1, &PGLOB(GtkHBoxFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkHBox_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkHBox_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkHBox dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkHBox_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkHBoxFids(env, dest, &PGLOB(GtkHBoxFc));
		setGtkHBoxFields(env, dest, dest1, &PGLOB(GtkHBoxFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkMenuItem_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkMenuItem src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkMenuItemFids(env, src, &PGLOB(GtkMenuItemFc));
		getGtkMenuItemFields(env, src, src1, &PGLOB(GtkMenuItemFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkMenuItem_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkMenuItem_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkMenuItem dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkMenuItem_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkMenuItemFids(env, dest, &PGLOB(GtkMenuItemFc));
		setGtkMenuItemFields(env, dest, dest1, &PGLOB(GtkMenuItemFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCListRow_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCListRow src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCListRowFids(env, src, &PGLOB(GtkCListRowFc));
		getGtkCListRowFields(env, src, src1, &PGLOB(GtkCListRowFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCListRow_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCListRow_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCListRow dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCListRow_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCListRowFids(env, dest, &PGLOB(GtkCListRowFc));
		setGtkCListRowFields(env, dest, dest1, &PGLOB(GtkCListRowFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCListColumn_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCListColumn src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCListColumnFids(env, src, &PGLOB(GtkCListColumnFc));
		getGtkCListColumnFields(env, src, src1, &PGLOB(GtkCListColumnFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCListColumn_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCListColumn_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCListColumn dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCListColumn_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCListColumnFids(env, dest, &PGLOB(GtkCListColumnFc));
		setGtkCListColumnFields(env, dest, dest1, &PGLOB(GtkCListColumnFc));
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCTreeRow_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCTreeRow src_struct={0}, *src1 = NULL;
	if (src) {
		src1 = &src_struct;
		cacheGtkCTreeRowFids(env, src, &PGLOB(GtkCTreeRowFc));
		getGtkCTreeRowFields(env, src, src1, &PGLOB(GtkCTreeRowFc));
	}
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_gtk_GtkCTreeRow_2I\n");
#endif

	memmove((void*)dest, (void*)src1, count);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	memmove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCTreeRow_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	GtkCTreeRow dest_struct={0}, *dest1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_gtk_GtkCTreeRow_2II\n");
#endif

	memmove((void*)&dest_struct, (void*)src, count);
	if (dest) {
		dest1 = &dest_struct;
		cacheGtkCTreeRowFids(env, dest, &PGLOB(GtkCTreeRowFc));
		setGtkCTreeRowFields(env, dest, dest1, &PGLOB(GtkCTreeRowFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3BI
  (JNIEnv *env, jclass that, jint dest, jbyteArray src, jint count)
{
	jbyte *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__I_3BI\n");
#endif

	if (src) {
		src1 = (*env)->GetByteArrayElements(env, src, NULL);
		memmove((void*)dest, (void*)src1, count);
		(*env)->ReleaseByteArrayElements(env, src, src1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3II
  (JNIEnv *env, jclass that, jint dest, jintArray src, jint count)
{
	jint *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__I_3II\n");
#endif

	if (src) {
		src1 = (*env)->GetIntArrayElements(env, src, NULL);
		memmove((void*)dest, (void*)src1, count);
		(*env)->ReleaseIntArrayElements(env, src, src1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3III
  (JNIEnv *env, jclass that, jintArray dest, jint src, jint count)
{
	jint *dest1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__III\n");
#endif

	if (dest) {
		dest1 = (*env)->GetIntArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src, count);
		(*env)->ReleaseIntArrayElements(env, dest, dest1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3BII
  (JNIEnv *env, jclass that, jbyteArray dest, jint src, jint count)
{
	jbyte *dest1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3BII\n");
#endif

	if (dest) {
		dest1 = (*env)->GetByteArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src, count);
		(*env)->ReleaseByteArrayElements(env, dest, dest1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3I_3BI
  (JNIEnv *env, jclass that, jintArray dest, jbyteArray src, jint count)
{
	jint *dest1;
	jbyte *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3I_3BI\n");
#endif

	if (src && dest) {
		dest1 = (*env)->GetIntArrayElements(env, dest, NULL);
		src1 = (*env)->GetByteArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src1, count);
		(*env)->ReleaseIntArrayElements(env, dest, dest1, 0);
		(*env)->ReleaseByteArrayElements(env, src, src1, 0);
	}
}

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


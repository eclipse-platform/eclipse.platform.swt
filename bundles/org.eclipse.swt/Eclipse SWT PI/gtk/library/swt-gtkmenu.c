
#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

/*
 *  Accelerators
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1group_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_accel_group_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1group_1unref
  (JNIEnv *env, jclass that, jint accel_group)
{
	gtk_accel_group_unref((GtkAccelGroup*)accel_group);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1accel_1groups_1activate
  (JNIEnv *env, jclass that, jint accel_group, jint accel_key, jint accel_mods)
{
	return (jboolean) gtk_accel_groups_activate(GTK_OBJECT((GtkWindow*)accel_group),
	                                            accel_key,
						    accel_mods);
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






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

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>


/*
 * Labels
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1get_1type
  (JNIEnv *env, jclass that)
{
  return gtk_label_get_type ();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1label_1new
  (JNIEnv *env, jclass that, jstring str)
{
  jint rc;
  const jbyte *str1 = NULL;

  if (str!=NULL) {
    str1 = (*env)->GetStringUTFChars(env, str, NULL);
    if (str1==NULL) return 0;
  }
  rc = (jint) gtk_label_new((gchar*)str1);
  if (str!=NULL) (*env)->ReleaseStringUTFChars(env, str, str1);
  return rc;
}

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
 *  Buttons
 */





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



JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1button_1new_1with_1label
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;

	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_button_new_with_label((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1check_1button_1new
  (JNIEnv *env, jclass that)
{

	return (jint)gtk_check_button_new();
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
 *  Others
 */





JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1new
  (JNIEnv *env, jclass that, jfloat value, jfloat lower, jfloat upper, jfloat step_increment, jfloat page_increment, jfloat page_size)
{
	return (jint)gtk_adjustment_new(value, lower, upper, step_increment, page_increment, page_size);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1adjustment_1changed
  (JNIEnv *env, jclass that, jint adjustment)
{
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



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
#include <pango/pango.h>

/*
 * macros
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1WEIGHT_1NORMAL
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_WEIGHT_NORMAL;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1WEIGHT_1BOLD
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_WEIGHT_BOLD;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1STYLE_1NORMAL
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_STYLE_NORMAL;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1STYLE_1ITALIC
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_STYLE_ITALIC;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1STYLE_1OBLIQUE
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_STYLE_OBLIQUE;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1SCALE
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_SCALE;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_PANGO_1STRETCH_1NORMAL
  (JNIEnv *env, jclass that)
{
	return (jint)PANGO_STRETCH_NORMAL;
}


/*
 * functions
 */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1new
  (JNIEnv *env, jclass that)
{
	return (jint)pango_font_description_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1from_1string
  (JNIEnv *env, jclass that, jstring str)
{
   const jbyte *str1;
   jint answer;
   str1 = (*env)->GetStringUTFChars(env, str, NULL);
   if (str1==NULL) return (jint)0;
   answer = (jint)pango_font_description_from_string(str1);
   (*env)->ReleaseStringUTFChars(env, str, str1);
   return answer;
}


JNIEXPORT jstring JNICALL
Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1to_1string
  (JNIEnv *env, jclass that, jint descr)
{
   jstring answer;
   char *canswer = pango_font_description_to_string((PangoFontDescription*)descr);
   answer = (*env)->NewStringUTF(env, canswer);
   g_free(canswer);
   return answer;
}

JNIEXPORT jboolean JNICALL
Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1equal
  (JNIEnv *env, jclass that, jint descr1, jint descr2)
{
   return (jboolean) pango_font_description_equal ((PangoFontDescription*)descr1, (PangoFontDescription*)descr2);
}

JNIEXPORT void JNICALL
Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1free
  (JNIEnv *env, jclass that, jint descr)
{
   pango_font_description_free((PangoFontDescription*)descr);
}

JNIEXPORT jstring JNICALL
Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1family
  (JNIEnv *env, jclass that, jint descr)
{
   const char *canswer = pango_font_description_get_family((PangoFontDescription*)descr);
   return (*env)->NewStringUTF(env, canswer);
   /* don't free */
}

JNIEXPORT void JNICALL
Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1family
  (JNIEnv *env, jclass that, jint descr, jstring family)
{
   const jbyte *family1 = (*env)->GetStringUTFChars(env, family, NULL);
   if (family1==NULL) return;
   pango_font_description_set_family((PangoFontDescription*)descr, family1);
   (*env)->ReleaseStringUTFChars(env, family, family1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1size
  (JNIEnv *env, jclass that, jint descr)
{
   return (jint)pango_font_description_get_size((PangoFontDescription*)descr);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1style
  (JNIEnv *env, jclass that, jint descr)
{
   return (jint)pango_font_description_get_style((PangoFontDescription*)descr);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1get_1weight
  (JNIEnv *env, jclass that, jint descr)
{
   return (jint)pango_font_description_get_weight((PangoFontDescription*)descr);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1size
  (JNIEnv *env, jclass that, jint descr, jint size)
{
   pango_font_description_set_size((PangoFontDescription*)descr, size);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1stretch
  (JNIEnv *env, jclass that, jint descr, jint stretch)
{
   pango_font_description_set_weight((PangoFontDescription*)descr, stretch);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1style
  (JNIEnv *env, jclass that, jint descr, jint style)
{
   pango_font_description_set_weight((PangoFontDescription*)descr, style);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1description_1set_1weight
  (JNIEnv *env, jclass that, jint descr, jint weight)
{
   pango_font_description_set_weight((PangoFontDescription*)descr, weight);
}

/* contexts */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1new
  (JNIEnv *env, jclass that)
{
   return (jint)pango_context_new();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1get_1font_1description
  (JNIEnv *env, jclass that, jint context)
{
   return (jint)pango_context_get_font_description((PangoContext*)context);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1set_1font_1description
  (JNIEnv *env, jclass that, jint context, jint descr)
{
   pango_context_set_font_description((PangoContext*)context, (PangoFontDescription*)descr);
}

JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1context_1get_1metrics
  (JNIEnv *env, jclass that, jint context, jint descr)
{
   return (jint)pango_context_get_metrics((PangoContext*)context, (PangoFontDescription*)descr, NULL);
}

/* metrics */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1ascent
  (JNIEnv *env, jclass that, jint metrics)
{
   return (jint)pango_font_metrics_get_ascent((PangoFontMetrics*)metrics);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1descent
  (JNIEnv *env, jclass that, jint metrics)
{
   return (jint)pango_font_metrics_get_descent((PangoFontMetrics*)metrics);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_pango_1font_1metrics_1get_1approximate_1char_1width
  (JNIEnv *env, jclass that, jint metrics)
{
   return (jint)pango_font_metrics_get_approximate_char_width((PangoFontMetrics*)metrics);
}


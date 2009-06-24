/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_photon_OS_##func

#ifndef NO_PfDecomposeStemToID
JNIEXPORT jint JNICALL OS_NATIVE(PfDecomposeStemToID)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfDecomposeStemToID_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PfDecomposeStemToID(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PfDecomposeStemToID_FUNC);
	return rc;
}
#endif

#ifndef NO_PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III
JNIEXPORT jint JNICALL OS_NATIVE(PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PfExtentText(lparg0, lparg1, (const char *)arg2, (const char *)arg3, arg4);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III_FUNC);
	return rc;
}
#endif

#ifndef NO_PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI
JNIEXPORT jint JNICALL OS_NATIVE(PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PfExtentText(lparg0, lparg1, (const char *)lparg2, (const char *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_PfExtentWideText
JNIEXPORT jint JNICALL OS_NATIVE(PfExtentWideText)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jbyteArray arg2, jcharArray arg3, jint arg4)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfExtentWideText_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PfExtentWideText(lparg0, lparg1, (const char *)lparg2, (const uint16_t *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PfExtentWideText_FUNC);
	return rc;
}
#endif

#ifndef NO_PfFontDescription
JNIEXPORT jint JNICALL OS_NATIVE(PfFontDescription)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfFontDescription_FUNC);
	rc = (jint)PfFontDescription((FontID *)arg0);
	OS_NATIVE_EXIT(env, that, PfFontDescription_FUNC);
	return rc;
}
#endif

#ifndef NO_PfFontFlags
JNIEXPORT jint JNICALL OS_NATIVE(PfFontFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfFontFlags_FUNC);
	rc = (jint)PfFontFlags((FontID *)arg0);
	OS_NATIVE_EXIT(env, that, PfFontFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_PfFontSize
JNIEXPORT jint JNICALL OS_NATIVE(PfFontSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfFontSize_FUNC);
	rc = (jint)PfFontSize((FontID *)arg0);
	OS_NATIVE_EXIT(env, that, PfFontSize_FUNC);
	return rc;
}
#endif

#ifndef NO_PfFreeFont
JNIEXPORT jint JNICALL OS_NATIVE(PfFreeFont)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfFreeFont_FUNC);
	rc = (jint)PfFreeFont((FontID *)arg0);
	OS_NATIVE_EXIT(env, that, PfFreeFont_FUNC);
	return rc;
}
#endif

#ifndef NO_PfGenerateFontName
JNIEXPORT jint JNICALL OS_NATIVE(PfGenerateFontName)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfGenerateFontName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PfGenerateFontName((char const *)lparg0, arg1, arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PfGenerateFontName_FUNC);
	return rc;
}
#endif

#ifndef NO_PfLoadMetrics
JNIEXPORT jint JNICALL OS_NATIVE(PfLoadMetrics)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfLoadMetrics_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PfLoadMetrics((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PfLoadMetrics_FUNC);
	return rc;
}
#endif

#ifndef NO_PfQueryFontInfo
JNIEXPORT jint JNICALL OS_NATIVE(PfQueryFontInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1)
{
	jbyte *lparg0=NULL;
	FontQueryInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfQueryFontInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getFontQueryInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PfQueryFontInfo((const char *)lparg0, (FontQueryInfo *)lparg1);
fail:
	if (arg1 && lparg1) setFontQueryInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PfQueryFontInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_PfQueryFonts
JNIEXPORT jint JNICALL OS_NATIVE(PfQueryFonts)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PfQueryFonts_FUNC);
	rc = (jint)PfQueryFonts(arg0, arg1, (FontDetails *)arg2, arg3);
	OS_NATIVE_EXIT(env, that, PfQueryFonts_FUNC);
	return rc;
}
#endif

#ifndef NO_PgAlphaOff
JNIEXPORT void JNICALL OS_NATIVE(PgAlphaOff)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, PgAlphaOff_FUNC);
	PgAlphaOff();
	OS_NATIVE_EXIT(env, that, PgAlphaOff_FUNC);
}
#endif

#ifndef NO_PgAlphaOn
JNIEXPORT void JNICALL OS_NATIVE(PgAlphaOn)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, PgAlphaOn_FUNC);
	PgAlphaOn();
	OS_NATIVE_EXIT(env, that, PgAlphaOn_FUNC);
}
#endif

#ifndef NO_PgCreateGC
JNIEXPORT jint JNICALL OS_NATIVE(PgCreateGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgCreateGC_FUNC);
	rc = (jint)PgCreateGC(arg0);
	OS_NATIVE_EXIT(env, that, PgCreateGC_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDestroyGC
JNIEXPORT void JNICALL OS_NATIVE(PgDestroyGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PgDestroyGC_FUNC);
	PgDestroyGC((PhGC_t *)arg0);
	OS_NATIVE_EXIT(env, that, PgDestroyGC_FUNC);
}
#endif

#ifndef NO_PgDrawArc
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawArc)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawArc_FUNC);
	if (arg0) if ((lparg0 = getPhPoint_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PgDrawArc(lparg0, lparg1, arg2, arg3, arg4);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhPoint_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawArc_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawArrow
JNIEXPORT void JNICALL OS_NATIVE(PgDrawArrow)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jint arg2, jint arg3)
{
	PhRect_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgDrawArrow_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	PgDrawArrow(lparg0, arg1, (PgColor_t)arg2, arg3);
fail:
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawArrow_FUNC);
}
#endif

#ifndef NO_PgDrawBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawBitmap_FUNC);
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getPhDim_tFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)PgDrawBitmap((void const *)arg0, (int)arg1, (PhPoint_t *)lparg2, (PhPoint_t *)lparg3, (int)arg4, (long)arg5);
fail:
	if (arg3 && lparg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PgDrawBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawEllipse
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawEllipse)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawEllipse_FUNC);
	if (arg0) if ((lparg0 = getPhPoint_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PgDrawEllipse(lparg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhPoint_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawEllipse_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawGradient
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawGradient)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jbyteArray arg10)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg10=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawGradient_FUNC);
	if (arg0) if ((lparg0 = getPhPoint_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg10) if ((lparg10 = (*env)->GetByteArrayElements(env, arg10, NULL)) == NULL) goto fail;
	rc = (jint)PgDrawGradient(lparg0, lparg1, arg2, arg3, arg4, (PgColor_t)arg5, (PgColor_t)arg6, (PgColor_t)arg7, (PgColor_t)arg8, arg9, (unsigned char *)lparg10);
fail:
	if (arg10 && lparg10) (*env)->ReleaseByteArrayElements(env, arg10, lparg10, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhPoint_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawGradient_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawILine
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawILine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawILine_FUNC);
	rc = (jint)PgDrawILine(arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, PgDrawILine_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawIPixel
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawIPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawIPixel_FUNC);
	rc = (jint)PgDrawIPixel(arg0, arg1);
	OS_NATIVE_EXIT(env, that, PgDrawIPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawIRect
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawIRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawIRect_FUNC);
	rc = (jint)PgDrawIRect(arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, PgDrawIRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawImage
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawImage_FUNC);
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getPhDim_tFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)PgDrawImage((void const *)arg0, arg1, lparg2, lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PgDrawImage_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawMultiTextArea
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawMultiTextArea)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawMultiTextArea_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhRect_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PgDrawMultiTextArea((char *)lparg0, arg1, lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgDrawMultiTextArea_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawPhImageRectmx
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawPhImageRectmx)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jint arg3)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawPhImageRectmx_FUNC);
	if (arg0) if ((lparg0 = getPhPoint_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhRect_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PgDrawPhImageRectmx(lparg0, (PhImage_t const *)arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg0 && lparg0) setPhPoint_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawPhImageRectmx_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawPolygon
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawPolygon)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jobject arg2, jint arg3)
{
	jshort *lparg0=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawPolygon_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PgDrawPolygon((PhPoint_t const *)lparg0, arg1, (PhPoint_t const *)lparg2, arg3);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgDrawPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawRoundRect
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawRoundRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawRoundRect_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PgDrawRoundRect((PhRect_t const *)lparg0, (PhPoint_t const *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgDrawRoundRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawTImage
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawTImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawTImage_FUNC);
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getPhDim_tFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)PgDrawTImage((void const *)arg0, arg1, (PhPoint_t const *)lparg2, (PhDim_t const *)lparg3, arg4, arg5, (void const *)arg6, arg7);
fail:
	if (arg3 && lparg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PgDrawTImage_FUNC);
	return rc;
}
#endif

#ifndef NO_PgDrawText
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawText)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jobject arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgDrawText_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PgDrawText((char const *)lparg0, arg1, (PhPoint_t *)lparg2, arg3);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgDrawText_FUNC);
	return rc;
}
#endif

#ifndef NO_PgExtentMultiText
JNIEXPORT jint JNICALL OS_NATIVE(PgExtentMultiText)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jint arg5)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgExtentMultiText_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PgExtentMultiText((PhRect_t *)lparg0, (PhPoint_t *)lparg1, (char *)lparg2, (char *)lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgExtentMultiText_FUNC);
	return rc;
}
#endif

#ifndef NO_PgFlush
JNIEXPORT jint JNICALL OS_NATIVE(PgFlush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgFlush_FUNC);
	rc = (jint)PgFlush();
	OS_NATIVE_EXIT(env, that, PgFlush_FUNC);
	return rc;
}
#endif

#ifndef NO_PgGetVideoMode
JNIEXPORT jint JNICALL OS_NATIVE(PgGetVideoMode)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PgDisplaySettings_t _arg0={0}, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgGetVideoMode_FUNC);
	if (arg0) if ((lparg0 = getPgDisplaySettings_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PgGetVideoMode((PgDisplaySettings_t *)lparg0);
fail:
	if (arg0 && lparg0) setPgDisplaySettings_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgGetVideoMode_FUNC);
	return rc;
}
#endif

#ifndef NO_PgGetVideoModeInfo
JNIEXPORT jint JNICALL OS_NATIVE(PgGetVideoModeInfo)
	(JNIEnv *env, jclass that, jshort arg0, jobject arg1)
{
	PgVideoModeInfo_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgGetVideoModeInfo_FUNC);
	if (arg1) if ((lparg1 = getPgVideoModeInfo_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PgGetVideoModeInfo(arg0, (PgVideoModeInfo_t *)lparg1);
fail:
	if (arg1 && lparg1) setPgVideoModeInfo_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PgGetVideoModeInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_PgReadScreen
JNIEXPORT jint JNICALL OS_NATIVE(PgReadScreen)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	PhRect_t _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgReadScreen_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PgReadScreen((PhRect_t *)lparg0, (void *)arg1);
fail:
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgReadScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_PgReadScreenSize
JNIEXPORT jint JNICALL OS_NATIVE(PgReadScreenSize)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhRect_t _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgReadScreenSize_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PgReadScreenSize((PhRect_t *)lparg0);
fail:
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgReadScreenSize_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetAlpha
JNIEXPORT void JNICALL OS_NATIVE(PgSetAlpha)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbyte arg3, jbyte arg4)
{
	PgMap_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, PgSetAlpha_FUNC);
	if (arg1) if ((lparg1 = getPgMap_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	PgSetAlpha(arg0, (PgMap_t const *)lparg1, (PgGradient_t const *)arg2, arg3, arg4);
fail:
	if (arg1 && lparg1) setPgMap_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PgSetAlpha_FUNC);
}
#endif

#ifndef NO_PgSetClipping
JNIEXPORT void JNICALL OS_NATIVE(PgSetClipping)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PgSetClipping_FUNC);
	PgSetClipping(arg0, (PhRect_t const *)arg1);
	OS_NATIVE_EXIT(env, that, PgSetClipping_FUNC);
}
#endif

#ifndef NO_PgSetDrawBufferSize
JNIEXPORT jint JNICALL OS_NATIVE(PgSetDrawBufferSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetDrawBufferSize_FUNC);
	rc = (jint)PgSetDrawBufferSize(arg0);
	OS_NATIVE_EXIT(env, that, PgSetDrawBufferSize_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetDrawMode
JNIEXPORT jint JNICALL OS_NATIVE(PgSetDrawMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetDrawMode_FUNC);
	rc = (jint)PgSetDrawMode(arg0);
	OS_NATIVE_EXIT(env, that, PgSetDrawMode_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetFillColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetFillColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetFillColor_FUNC);
	rc = (jint)PgSetFillColor((PgColor_t)arg0);
	OS_NATIVE_EXIT(env, that, PgSetFillColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetFillTransPat
JNIEXPORT void JNICALL OS_NATIVE(PgSetFillTransPat)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgSetFillTransPat_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	PgSetFillTransPat((PgPattern_t)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgSetFillTransPat_FUNC);
}
#endif

#ifndef NO_PgSetFont
JNIEXPORT void JNICALL OS_NATIVE(PgSetFont)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgSetFont_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	PgSetFont((char const *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgSetFont_FUNC);
}
#endif

#ifndef NO_PgSetGC
JNIEXPORT jint JNICALL OS_NATIVE(PgSetGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetGC_FUNC);
	rc = (jint)PgSetGC((PhGC_t *)arg0);
	OS_NATIVE_EXIT(env, that, PgSetGC_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetMultiClip
JNIEXPORT jint JNICALL OS_NATIVE(PgSetMultiClip)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetMultiClip_FUNC);
	rc = (jint)PgSetMultiClip(arg0, (PhRect_t const *)arg1);
	OS_NATIVE_EXIT(env, that, PgSetMultiClip_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetMultiClipTiles
JNIEXPORT jint JNICALL OS_NATIVE(PgSetMultiClipTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetMultiClipTiles_FUNC);
	rc = (jint)PgSetMultiClipTiles(arg0);
	OS_NATIVE_EXIT(env, that, PgSetMultiClipTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetPalette
JNIEXPORT jint JNICALL OS_NATIVE(PgSetPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jshort arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetPalette_FUNC);
	rc = (jint)PgSetPalette((PgColor_t const *)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, PgSetPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetRegion
JNIEXPORT void JNICALL OS_NATIVE(PgSetRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PgSetRegion_FUNC);
	PgSetRegion((PhRid_t)arg0);
	OS_NATIVE_EXIT(env, that, PgSetRegion_FUNC);
}
#endif

#ifndef NO_PgSetStrokeCap
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeCap)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetStrokeCap_FUNC);
	rc = (jint)PgSetStrokeCap(arg0);
	OS_NATIVE_EXIT(env, that, PgSetStrokeCap_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetStrokeColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetStrokeColor_FUNC);
	rc = (jint)PgSetStrokeColor((PgColor_t)arg0);
	OS_NATIVE_EXIT(env, that, PgSetStrokeColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetStrokeDash
JNIEXPORT void JNICALL OS_NATIVE(PgSetStrokeDash)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgSetStrokeDash_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	PgSetStrokeDash((unsigned char const *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PgSetStrokeDash_FUNC);
}
#endif

#ifndef NO_PgSetStrokeJoin
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeJoin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetStrokeJoin_FUNC);
	rc = (jint)PgSetStrokeJoin(arg0);
	OS_NATIVE_EXIT(env, that, PgSetStrokeJoin_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetStrokeWidth
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetStrokeWidth_FUNC);
	rc = (jint)PgSetStrokeWidth(arg0);
	OS_NATIVE_EXIT(env, that, PgSetStrokeWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetTextColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgSetTextColor_FUNC);
	rc = (jint)PgSetTextColor((PgColor_t)arg0);
	OS_NATIVE_EXIT(env, that, PgSetTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PgSetTextXORColor
JNIEXPORT void JNICALL OS_NATIVE(PgSetTextXORColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PgSetTextXORColor_FUNC);
	PgSetTextXORColor((PgColor_t)arg0, (PgColor_t)arg1);
	OS_NATIVE_EXIT(env, that, PgSetTextXORColor_FUNC);
}
#endif

#ifndef NO_PgSetTranslation
JNIEXPORT void JNICALL OS_NATIVE(PgSetTranslation)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	PhPoint_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgSetTranslation_FUNC);
	if (arg0) if ((lparg0 = getPhPoint_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	PgSetTranslation(lparg0, arg1);
fail:
	if (arg0 && lparg0) setPhPoint_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgSetTranslation_FUNC);
}
#endif

#ifndef NO_PgSetUserClip
JNIEXPORT void JNICALL OS_NATIVE(PgSetUserClip)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhRect_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, PgSetUserClip_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	PgSetUserClip((PhRect_t const *)lparg0);
fail:
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PgSetUserClip_FUNC);
}
#endif

#ifndef NO_PgShmemCreate
JNIEXPORT jint JNICALL OS_NATIVE(PgShmemCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgShmemCreate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PgShmemCreate(arg0, (char const *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PgShmemCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_PgShmemDestroy
JNIEXPORT jint JNICALL OS_NATIVE(PgShmemDestroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PgShmemDestroy_FUNC);
	rc = (jint)PgShmemDestroy((void *)arg0);
	OS_NATIVE_EXIT(env, that, PgShmemDestroy_FUNC);
	return rc;
}
#endif

#ifndef NO_PhAddMergeTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhAddMergeTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhAddMergeTiles_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PhAddMergeTiles((PhTile_t *)arg0, (PhTile_t *)arg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PhAddMergeTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhAreaToRect
JNIEXPORT void JNICALL OS_NATIVE(PhAreaToRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	PhArea_t _arg0, *lparg0=NULL;
	PhRect_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, PhAreaToRect_FUNC);
	if (arg0) if ((lparg0 = getPhArea_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	PhAreaToRect((PhArea_t const *)lparg0, (PhRect_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhArea_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PhAreaToRect_FUNC);
}
#endif

#ifndef NO_PhBlit
JNIEXPORT void JNICALL OS_NATIVE(PhBlit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, PhBlit_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	PhBlit((PhRid_t)arg0, (const PhRect_t *)lparg1, (const PhPoint_t *)lparg2);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PhBlit_FUNC);
}
#endif

#ifndef NO_PhClipTilings
JNIEXPORT jint JNICALL OS_NATIVE(PhClipTilings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipTilings_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PhClipTilings((PhTile_t *)arg0, (PhTile_t *)arg1, (PhTile_t **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PhClipTilings_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardCopy
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardCopy)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardCopy_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PhClipboardCopy(arg0, arg1, (PhClipHeader const *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PhClipboardCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardCopyString
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardCopyString)
	(JNIEnv *env, jclass that, jshort arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardCopyString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PhClipboardCopyString(arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PhClipboardCopyString_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteFinish
JNIEXPORT void JNICALL OS_NATIVE(PhClipboardPasteFinish)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PhClipboardPasteFinish_FUNC);
	PhClipboardPasteFinish((void *)arg0);
	OS_NATIVE_EXIT(env, that, PhClipboardPasteFinish_FUNC);
}
#endif

#ifndef NO_PhClipboardPasteStart
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteStart)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardPasteStart_FUNC);
	rc = (jint)PhClipboardPasteStart(arg0);
	OS_NATIVE_EXIT(env, that, PhClipboardPasteStart_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteString
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteString)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardPasteString_FUNC);
	rc = (jint)PhClipboardPasteString(arg0);
	OS_NATIVE_EXIT(env, that, PhClipboardPasteString_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteType
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteType)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardPasteType_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PhClipboardPasteType((void *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PhClipboardPasteType_FUNC);
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteTypeN
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteTypeN)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhClipboardPasteTypeN_FUNC);
	rc = (jint)PhClipboardPasteTypeN((void *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PhClipboardPasteTypeN_FUNC);
	return rc;
}
#endif

#ifndef NO_PhCoalesceTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhCoalesceTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhCoalesceTiles_FUNC);
	rc = (jint)PhCoalesceTiles((PhTile_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhCoalesceTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhCopyTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhCopyTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhCopyTiles_FUNC);
	rc = (jint)PhCopyTiles((PhTile_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhCopyTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(PhCreateImage)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	PhImage_t _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhCreateImage_FUNC);
	if (arg0) if ((lparg0 = getPhImage_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PhCreateImage((PhImage_t *)lparg0, arg1, arg2, arg3, (PgColor_t const *)arg4, arg5, arg6);
fail:
	if (arg0 && lparg0) setPhImage_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PhCreateImage_FUNC);
	return rc;
}
#endif

#ifndef NO_PhDCSetCurrent
JNIEXPORT jint JNICALL OS_NATIVE(PhDCSetCurrent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhDCSetCurrent_FUNC);
	rc = (jint)PhDCSetCurrent((void *)arg0);
	OS_NATIVE_EXIT(env, that, PhDCSetCurrent_FUNC);
	return rc;
}
#endif

#ifndef NO_PhDeTranslateTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhDeTranslateTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhDeTranslateTiles_FUNC);
	rc = (jint)PhDeTranslateTiles((PhTile_t *)arg0, (PhPoint_t const *)arg1);
	OS_NATIVE_EXIT(env, that, PhDeTranslateTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhEventNext
JNIEXPORT jint JNICALL OS_NATIVE(PhEventNext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhEventNext_FUNC);
	rc = (jint)PhEventNext((void *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PhEventNext_FUNC);
	return rc;
}
#endif

#ifndef NO_PhEventPeek
JNIEXPORT jint JNICALL OS_NATIVE(PhEventPeek)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhEventPeek_FUNC);
	rc = (jint)PhEventPeek((void *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PhEventPeek_FUNC);
	return rc;
}
#endif

#ifndef NO_PhFreeTiles
JNIEXPORT void JNICALL OS_NATIVE(PhFreeTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PhFreeTiles_FUNC);
	PhFreeTiles((PhTile_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhFreeTiles_FUNC);
}
#endif

#ifndef NO_PhGetData
JNIEXPORT jint JNICALL OS_NATIVE(PhGetData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhGetData_FUNC);
	rc = (jint)PhGetData((PhEvent_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhGetData_FUNC);
	return rc;
}
#endif

#ifndef NO_PhGetMsgSize
JNIEXPORT jint JNICALL OS_NATIVE(PhGetMsgSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhGetMsgSize_FUNC);
	rc = (jint)PhGetMsgSize((PhEvent_t const *)arg0);
	OS_NATIVE_EXIT(env, that, PhGetMsgSize_FUNC);
	return rc;
}
#endif

#ifndef NO_PhGetRects
JNIEXPORT jint JNICALL OS_NATIVE(PhGetRects)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhGetRects_FUNC);
	rc = (jint)PhGetRects((PhEvent_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhGetRects_FUNC);
	return rc;
}
#endif

#ifndef NO_PhInitDrag
JNIEXPORT jint JNICALL OS_NATIVE(PhInitDrag)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jobject arg5, jobject arg6, jobject arg7, jobject arg8, jshortArray arg9)
{
	PhRect_t _arg2, *lparg2=NULL;
	PhRect_t _arg3, *lparg3=NULL;
	PhDim_t _arg5, *lparg5=NULL;
	PhDim_t _arg6, *lparg6=NULL;
	PhDim_t _arg7, *lparg7=NULL;
	PhPoint_t _arg8, *lparg8=NULL;
	jshort *lparg9=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhInitDrag_FUNC);
	if (arg2) if ((lparg2 = getPhRect_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getPhRect_tFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getPhDim_tFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getPhDim_tFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getPhDim_tFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getPhPoint_tFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetShortArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)PhInitDrag((PhRid_t)arg0, arg1, (const PhRect_t *)lparg2, (const PhRect_t *)lparg3, arg4, (PhDim_t *)lparg5, (PhDim_t *)lparg6, (PhDim_t *)lparg7, (PhPoint_t *)lparg8, (PhCursorDescription_t *)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseShortArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) setPhPoint_tFields(env, arg8, lparg8);
	if (arg7 && lparg7) setPhDim_tFields(env, arg7, lparg7);
	if (arg6 && lparg6) setPhDim_tFields(env, arg6, lparg6);
	if (arg5 && lparg5) setPhDim_tFields(env, arg5, lparg5);
	if (arg3 && lparg3) setPhRect_tFields(env, arg3, lparg3);
	if (arg2 && lparg2) setPhRect_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PhInitDrag_FUNC);
	return rc;
}
#endif

#ifndef NO_PhInputGroup
JNIEXPORT jint JNICALL OS_NATIVE(PhInputGroup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhInputGroup_FUNC);
	rc = (jint)PhInputGroup((PhEvent_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhInputGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_PhIntersectTilings
JNIEXPORT jint JNICALL OS_NATIVE(PhIntersectTilings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhIntersectTilings_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PhIntersectTilings((PhTile_t const *)arg0, (PhTile_t const *)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PhIntersectTilings_FUNC);
	return rc;
}
#endif

#ifndef NO_PhKeyToMb
JNIEXPORT jint JNICALL OS_NATIVE(PhKeyToMb)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1)
{
	jbyte *lparg0=NULL;
	PhKeyEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhKeyToMb_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhKeyEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PhKeyToMb((char *)lparg0, lparg1);
fail:
	if (arg1 && lparg1) setPhKeyEvent_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PhKeyToMb_FUNC);
	return rc;
}
#endif

#ifndef NO_PhMakeGhostBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PhMakeGhostBitmap)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhMakeGhostBitmap_FUNC);
	rc = (jint)PhMakeGhostBitmap((PhImage_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhMakeGhostBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_PhMakeTransBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PhMakeTransBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhMakeTransBitmap_FUNC);
	rc = (jint)PhMakeTransBitmap((PhImage_t *)arg0, (PgColor_t)arg1);
	OS_NATIVE_EXIT(env, that, PhMakeTransBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_PhMergeTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhMergeTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhMergeTiles_FUNC);
	rc = (jint)PhMergeTiles((PhTile_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhMergeTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhMoveCursorAbs
JNIEXPORT void JNICALL OS_NATIVE(PhMoveCursorAbs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, PhMoveCursorAbs_FUNC);
	PhMoveCursorAbs(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, PhMoveCursorAbs_FUNC);
}
#endif

#ifndef NO_PhQueryCursor
JNIEXPORT jint JNICALL OS_NATIVE(PhQueryCursor)
	(JNIEnv *env, jclass that, jshort arg0, jobject arg1)
{
	PhCursorInfo_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhQueryCursor_FUNC);
	if (arg1) if ((lparg1 = getPhCursorInfo_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PhQueryCursor(arg0, (PhCursorInfo_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhCursorInfo_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PhQueryCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_PhQueryRids
JNIEXPORT jint JNICALL OS_NATIVE(PhQueryRids)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6, jintArray arg7, jint arg8)
{
	PhRect_t _arg6, *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhQueryRids_FUNC);
	if (arg6) if ((lparg6 = getPhRect_tFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)PhQueryRids(arg0, (PhRid_t)arg1, arg2, arg3, arg4, (PhRid_t)arg5, (const PhRect_t *)lparg6, (PhRid_t *)lparg7, arg8);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) setPhRect_tFields(env, arg6, lparg6);
	OS_NATIVE_EXIT(env, that, PhQueryRids_FUNC);
	return rc;
}
#endif

#ifndef NO_PhRectIntersect
JNIEXPORT jint JNICALL OS_NATIVE(PhRectIntersect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhRectIntersect_FUNC);
	rc = (jint)PhRectIntersect((PhRect_t *)arg0, (PhRect_t const *)arg1);
	OS_NATIVE_EXIT(env, that, PhRectIntersect_FUNC);
	return rc;
}
#endif

#ifndef NO_PhRectUnion__II
JNIEXPORT jint JNICALL OS_NATIVE(PhRectUnion__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhRectUnion__II_FUNC);
	rc = (jint)PhRectUnion((PhRect_t *)arg0, (PhRect_t const *)arg1);
	OS_NATIVE_EXIT(env, that, PhRectUnion__II_FUNC);
	return rc;
}
#endif

#ifndef NO_PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	if (arg0) if ((lparg0 = getPhRect_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PhRectUnion((PhRect_t *)lparg0, (PhRect_t const *)lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	return rc;
}
#endif

#ifndef NO_PhRectsToTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhRectsToTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhRectsToTiles_FUNC);
	rc = (jint)PhRectsToTiles((PhRect_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PhRectsToTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhRegionQuery
JNIEXPORT jint JNICALL OS_NATIVE(PhRegionQuery)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jint arg3, jint arg4)
{
	PhRegion_t _arg1, *lparg1=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhRegionQuery_FUNC);
	if (arg1) if ((lparg1 = getPhRegion_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhRect_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PhRegionQuery((PhRid_t)arg0, (PhRegion_t *)lparg1, (PhRect_t *)lparg2, (void *)arg3, arg4);
fail:
	if (arg2 && lparg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPhRegion_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PhRegionQuery_FUNC);
	return rc;
}
#endif

#ifndef NO_PhReleaseImage
JNIEXPORT void JNICALL OS_NATIVE(PhReleaseImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PhReleaseImage_FUNC);
	PhReleaseImage((PhImage_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhReleaseImage_FUNC);
}
#endif

#ifndef NO_PhSortTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhSortTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhSortTiles_FUNC);
	rc = (jint)PhSortTiles((PhTile_t *)arg0);
	OS_NATIVE_EXIT(env, that, PhSortTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhTilesToRects
JNIEXPORT jint JNICALL OS_NATIVE(PhTilesToRects)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhTilesToRects_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PhTilesToRects((PhTile_t *)arg0, (int *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PhTilesToRects_FUNC);
	return rc;
}
#endif

#ifndef NO_PhTranslateTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhTranslateTiles)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhTranslateTiles_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PhTranslateTiles((PhTile_t *)arg0, (PhPoint_t const *)lparg1);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PhTranslateTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PhWindowQueryVisible
JNIEXPORT jint JNICALL OS_NATIVE(PhWindowQueryVisible)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	PhRect_t _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PhWindowQueryVisible_FUNC);
	if (arg3) if ((lparg3 = getPhRect_tFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)PhWindowQueryVisible(arg0, (PhRid_t)arg1, arg2, (PhRect_t *)lparg3);
fail:
	if (arg3 && lparg3) setPhRect_tFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, PhWindowQueryVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_PiCropImage
JNIEXPORT jint JNICALL OS_NATIVE(PiCropImage)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PiCropImage_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PiCropImage((PhImage_t *)arg0, (PhRect_t const *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PiCropImage_FUNC);
	return rc;
}
#endif

#ifndef NO_PiDuplicateImage
JNIEXPORT jint JNICALL OS_NATIVE(PiDuplicateImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PiDuplicateImage_FUNC);
	rc = (jint)PiDuplicateImage((PhImage_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PiDuplicateImage_FUNC);
	return rc;
}
#endif

#ifndef NO_PmMemCreateMC
JNIEXPORT jint JNICALL OS_NATIVE(PmMemCreateMC)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhDim_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PmMemCreateMC_FUNC);
	if (arg1) if ((lparg1 = getPhDim_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PmMemCreateMC((PhImage_t *)arg0, (PhDim_t *)lparg1, (PhPoint_t *)lparg2);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPhDim_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PmMemCreateMC_FUNC);
	return rc;
}
#endif

#ifndef NO_PmMemFlush
JNIEXPORT jint JNICALL OS_NATIVE(PmMemFlush)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PmMemFlush_FUNC);
	rc = (jint)PmMemFlush((PmMemoryContext_t *)arg0, (PhImage_t *)arg1);
	OS_NATIVE_EXIT(env, that, PmMemFlush_FUNC);
	return rc;
}
#endif

#ifndef NO_PmMemReleaseMC
JNIEXPORT void JNICALL OS_NATIVE(PmMemReleaseMC)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PmMemReleaseMC_FUNC);
	PmMemReleaseMC((PmMemoryContext_t *)arg0);
	OS_NATIVE_EXIT(env, that, PmMemReleaseMC_FUNC);
}
#endif

#ifndef NO_PmMemStart
JNIEXPORT jint JNICALL OS_NATIVE(PmMemStart)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PmMemStart_FUNC);
	rc = (jint)PmMemStart((PmMemoryContext_t *)arg0);
	OS_NATIVE_EXIT(env, that, PmMemStart_FUNC);
	return rc;
}
#endif

#ifndef NO_PmMemStop
JNIEXPORT jint JNICALL OS_NATIVE(PmMemStop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PmMemStop_FUNC);
	rc = (jint)PmMemStop((PmMemoryContext_t *)arg0);
	OS_NATIVE_EXIT(env, that, PmMemStop_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAddCallback
JNIEXPORT void JNICALL OS_NATIVE(PtAddCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PtAddCallback_FUNC);
	PtAddCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, PtAddCallback_FUNC);
}
#endif

#ifndef NO_PtAddEventHandler
JNIEXPORT void JNICALL OS_NATIVE(PtAddEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PtAddEventHandler_FUNC);
	PtAddEventHandler((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, PtAddEventHandler_FUNC);
}
#endif

#ifndef NO_PtAddFilterCallback
JNIEXPORT void JNICALL OS_NATIVE(PtAddFilterCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PtAddFilterCallback_FUNC);
	PtAddFilterCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, PtAddFilterCallback_FUNC);
}
#endif

#ifndef NO_PtAddHotkeyHandler
JNIEXPORT void JNICALL OS_NATIVE(PtAddHotkeyHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, PtAddHotkeyHandler_FUNC);
	PtAddHotkeyHandler((PtWidget_t *)arg0, arg1, arg2, arg3, (void *)arg4, (PtCallbackF_t *)arg5);
	OS_NATIVE_EXIT(env, that, PtAddHotkeyHandler_FUNC);
}
#endif

#ifndef NO_PtAlert
JNIEXPORT jint JNICALL OS_NATIVE(PtAlert)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jint arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7, jintArray arg8, jint arg9, jint arg10, jint arg11)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAlert_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)PtAlert((PtWidget_t *)arg0, (PhPoint_t const *)lparg1, (char const *)lparg2, (PhImage_t const *)arg3, (char const *)lparg4, (char const *)lparg5, arg6, (char const **)lparg7, (char const **)lparg8, arg9, arg10, arg11);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtAlert_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppAddInput
JNIEXPORT jint JNICALL OS_NATIVE(PtAppAddInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAppAddInput_FUNC);
	rc = (jint)PtAppAddInput((PtAppContext_t)arg0, (pid_t)arg1, (PtInputCallbackProc_t)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, PtAppAddInput_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppAddWorkProc
JNIEXPORT jint JNICALL OS_NATIVE(PtAppAddWorkProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAppAddWorkProc_FUNC);
	rc = (jint)PtAppAddWorkProc((PtAppContext_t)arg0, (PtWorkProc_t)arg1, (void *)arg2);
	OS_NATIVE_EXIT(env, that, PtAppAddWorkProc_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppCreatePulse
JNIEXPORT jint JNICALL OS_NATIVE(PtAppCreatePulse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAppCreatePulse_FUNC);
	rc = (jint)PtAppCreatePulse((PtAppContext_t)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PtAppCreatePulse_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppDeletePulse
JNIEXPORT jint JNICALL OS_NATIVE(PtAppDeletePulse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAppDeletePulse_FUNC);
	rc = (jint)PtAppDeletePulse((PtAppContext_t)arg0, (pid_t)arg1);
	OS_NATIVE_EXIT(env, that, PtAppDeletePulse_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppProcessEvent
JNIEXPORT void JNICALL OS_NATIVE(PtAppProcessEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PtAppProcessEvent_FUNC);
	PtAppProcessEvent((PtAppContext_t)arg0);
	OS_NATIVE_EXIT(env, that, PtAppProcessEvent_FUNC);
}
#endif

#ifndef NO_PtAppPulseTrigger
JNIEXPORT jint JNICALL OS_NATIVE(PtAppPulseTrigger)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtAppPulseTrigger_FUNC);
	rc = (jint)PtAppPulseTrigger((PtAppContext_t)arg0, (pid_t)arg1);
	OS_NATIVE_EXIT(env, that, PtAppPulseTrigger_FUNC);
	return rc;
}
#endif

#ifndef NO_PtAppRemoveInput
JNIEXPORT void JNICALL OS_NATIVE(PtAppRemoveInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtAppRemoveInput_FUNC);
	PtAppRemoveInput((PtAppContext_t)arg0, (PtInputId_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtAppRemoveInput_FUNC);
}
#endif

#ifndef NO_PtAppRemoveWorkProc
JNIEXPORT void JNICALL OS_NATIVE(PtAppRemoveWorkProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtAppRemoveWorkProc_FUNC);
	PtAppRemoveWorkProc((PtAppContext_t)arg0, (PtWorkProcId_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtAppRemoveWorkProc_FUNC);
}
#endif

#ifndef NO_PtBeep
JNIEXPORT jint JNICALL OS_NATIVE(PtBeep)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtBeep_FUNC);
	rc = (jint)PtBeep();
	OS_NATIVE_EXIT(env, that, PtBeep_FUNC);
	return rc;
}
#endif

#ifndef NO_PtBlit
JNIEXPORT jint JNICALL OS_NATIVE(PtBlit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtBlit_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PtBlit((PtWidget_t const *)arg0, (PhRect_t const *)lparg1, (PhPoint_t const *)lparg2);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtBlit_FUNC);
	return rc;
}
#endif

#ifndef NO_PtBlockAllWindows
JNIEXPORT jint JNICALL OS_NATIVE(PtBlockAllWindows)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtBlockAllWindows_FUNC);
	rc = (jint)PtBlockAllWindows((PtWidget_t *)arg0, arg1, (PgColor_t)arg2);
	OS_NATIVE_EXIT(env, that, PtBlockAllWindows_FUNC);
	return rc;
}
#endif

#ifndef NO_PtBlockWindow
JNIEXPORT jint JNICALL OS_NATIVE(PtBlockWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtBlockWindow_FUNC);
	rc = (jint)PtBlockWindow((PtWidget_t *)arg0, arg1, (PgColor_t)arg2);
	OS_NATIVE_EXIT(env, that, PtBlockWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_PtButton
JNIEXPORT jint JNICALL OS_NATIVE(PtButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtButton_FUNC);
	rc = (jint)PtButton;
	OS_NATIVE_EXIT(env, that, PtButton_FUNC);
	return rc;
}
#endif

#ifndef NO_PtCalcBorder
JNIEXPORT void JNICALL OS_NATIVE(PtCalcBorder)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, PtCalcBorder_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	PtCalcBorder((PtWidget_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtCalcBorder_FUNC);
}
#endif

#ifndef NO_PtCalcCanvas
JNIEXPORT jint JNICALL OS_NATIVE(PtCalcCanvas)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtCalcCanvas_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtCalcCanvas((PtWidget_t *)arg0, (PhRect_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtCalcCanvas_FUNC);
	return rc;
}
#endif

#ifndef NO_PtClippedBlit
JNIEXPORT jint JNICALL OS_NATIVE(PtClippedBlit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtClippedBlit_FUNC);
	if (arg2) if ((lparg2 = getPhPoint_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PtClippedBlit((PtWidget_t const *)arg0, (PhTile_t const *)arg1, (PhPoint_t const *)lparg2, (PhTile_t const *)arg3);
fail:
	if (arg2 && lparg2) setPhPoint_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PtClippedBlit_FUNC);
	return rc;
}
#endif

#ifndef NO_PtColorSelect
JNIEXPORT jint JNICALL OS_NATIVE(PtColorSelect)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	PtColorSelectInfo_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtColorSelect_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPtColorSelectInfo_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PtColorSelect((PtWidget_t *)arg0, (char *)lparg1, lparg2);
fail:
	if (arg2 && lparg2) setPtColorSelectInfo_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtColorSelect_FUNC);
	return rc;
}
#endif

#ifndef NO_PtComboBox
JNIEXPORT jint JNICALL OS_NATIVE(PtComboBox)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtComboBox_FUNC);
	rc = (jint)PtComboBox;
	OS_NATIVE_EXIT(env, that, PtComboBox_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtContainer)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainer_FUNC);
	rc = (jint)PtContainer;
	OS_NATIVE_EXIT(env, that, PtContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerFindFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFindFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerFindFocus_FUNC);
	rc = (jint)PtContainerFindFocus((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtContainerFindFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerFocusNext
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFocusNext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerFocusNext_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtContainerFocusNext((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtContainerFocusNext_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerFocusPrev
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFocusPrev)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerFocusPrev_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtContainerFocusPrev((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtContainerFocusPrev_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerGiveFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerGiveFocus)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerGiveFocus_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtContainerGiveFocus((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtContainerGiveFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerHold
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerHold)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerHold_FUNC);
	rc = (jint)PtContainerHold((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtContainerHold_FUNC);
	return rc;
}
#endif

#ifndef NO_PtContainerRelease
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtContainerRelease_FUNC);
	rc = (jint)PtContainerRelease((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtContainerRelease_FUNC);
	return rc;
}
#endif

#ifndef NO_PtCreateAppContext
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateAppContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtCreateAppContext_FUNC);
	rc = (jint)PtCreateAppContext();
	OS_NATIVE_EXIT(env, that, PtCreateAppContext_FUNC);
	return rc;
}
#endif

#ifndef NO_PtCreateWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtCreateWidget_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PtCreateWidget((PtWidgetClassRef_t *)arg0, (PtWidget_t *)arg1, arg2, (PtArg_t const *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, PtCreateWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtCreateWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateWidgetClass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtCreateWidgetClass_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PtCreateWidgetClass((PtWidgetClassRef_t *)arg0, arg1, arg2, (PtArg_t const *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, PtCreateWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_PtDamageExtent
JNIEXPORT jint JNICALL OS_NATIVE(PtDamageExtent)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtDamageExtent_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtDamageExtent((PtWidget_t *)arg0, (PhRect_t const *)lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtDamageExtent_FUNC);
	return rc;
}
#endif

#ifndef NO_PtDamageWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtDamageWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtDamageWidget_FUNC);
	rc = (jint)PtDamageWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtDamageWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtDestroyWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtDestroyWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtDestroyWidget_FUNC);
	rc = (jint)PtDestroyWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtDestroyWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtDisjoint
JNIEXPORT jint JNICALL OS_NATIVE(PtDisjoint)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtDisjoint_FUNC);
	rc = (jint)PtDisjoint;
	OS_NATIVE_EXIT(env, that, PtDisjoint_FUNC);
	return rc;
}
#endif

#ifndef NO_PtEnter
JNIEXPORT jint JNICALL OS_NATIVE(PtEnter)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtEnter_FUNC);
	rc = (jint)PtEnter(arg0);
	OS_NATIVE_EXIT(env, that, PtEnter_FUNC);
	return rc;
}
#endif

#ifndef NO_PtEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(PtEventHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtEventHandler_FUNC);
	rc = (jint)PtEventHandler((PhEvent_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtEventHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_PtExtentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtExtentWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtExtentWidget_FUNC);
	rc = (jint)PtExtentWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtExtentWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtExtentWidgetFamily
JNIEXPORT jint JNICALL OS_NATIVE(PtExtentWidgetFamily)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtExtentWidgetFamily_FUNC);
	rc = (jint)PtExtentWidgetFamily((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtExtentWidgetFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_PtFileSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtFileSelection)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jbyteArray arg6, jbyteArray arg7, jobject arg8, jint arg9)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	PtFileSelectionInfo_t _arg8={0}, *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtFileSelection_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetByteArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getPtFileSelectionInfo_tFields(env, arg8, &_arg8)) == NULL) goto fail;
	rc = (jint)PtFileSelection((PtWidget_t *)arg0, (PhPoint_t const *)lparg1, (char const *)lparg2, (char const *)lparg3, (char const *)lparg4, (char const *)lparg5, (char const *)lparg6, (char const *)lparg7, (PtFileSelectionInfo_t *)lparg8, arg9);
fail:
	if (arg8 && lparg8) setPtFileSelectionInfo_tFields(env, arg8, lparg8);
	if (arg7 && lparg7) (*env)->ReleaseByteArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtFileSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_PtFindDisjoint
JNIEXPORT jint JNICALL OS_NATIVE(PtFindDisjoint)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtFindDisjoint_FUNC);
	rc = (jint)PtFindDisjoint((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtFindDisjoint_FUNC);
	return rc;
}
#endif

#ifndef NO_PtFlush
JNIEXPORT jint JNICALL OS_NATIVE(PtFlush)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtFlush_FUNC);
	rc = (jint)PtFlush();
	OS_NATIVE_EXIT(env, that, PtFlush_FUNC);
	return rc;
}
#endif

#ifndef NO_PtFontSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtFontSelection)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jint arg5, jbyteArray arg6)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtFontSelection_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)PtFontSelection((PtWidget_t *)arg0, (const PhPoint_t *)lparg1, (const char *)lparg2, (const char *)lparg3, arg4, arg5, (const char *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtFontSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_PtForwardWindowEvent
JNIEXPORT jint JNICALL OS_NATIVE(PtForwardWindowEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhWindowEvent_t _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtForwardWindowEvent_FUNC);
	if (arg0) if ((lparg0 = getPhWindowEvent_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PtForwardWindowEvent((PhWindowEvent_t const *)lparg0);
fail:
	if (arg0 && lparg0) setPhWindowEvent_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PtForwardWindowEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_PtFrameSize
JNIEXPORT void JNICALL OS_NATIVE(PtFrameSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, PtFrameSize_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	PtFrameSize(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PtFrameSize_FUNC);
}
#endif

#ifndef NO_PtGetAbsPosition
JNIEXPORT void JNICALL OS_NATIVE(PtGetAbsPosition)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, PtGetAbsPosition_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	PtGetAbsPosition((PtWidget_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtGetAbsPosition_FUNC);
}
#endif

#ifndef NO_PtGetResources
JNIEXPORT jint JNICALL OS_NATIVE(PtGetResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGetResources_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PtGetResources((PtWidget_t *)arg0, arg1, (PtArg_t *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PtGetResources_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGetVisibleTiles
JNIEXPORT jint JNICALL OS_NATIVE(PtGetVisibleTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGetVisibleTiles_FUNC);
	rc = (jint)PtGetVisibleTiles(arg0);
	OS_NATIVE_EXIT(env, that, PtGetVisibleTiles_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusNext
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusNext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGlobalFocusNext_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtGlobalFocusNext((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtGlobalFocusNext_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusNextContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusNextContainer)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGlobalFocusNextContainer_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtGlobalFocusNextContainer((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtGlobalFocusNextContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusPrev
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusPrev)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGlobalFocusPrev_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtGlobalFocusPrev((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtGlobalFocusPrev_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusPrevContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusPrevContainer)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGlobalFocusPrevContainer_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtGlobalFocusPrevContainer((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtGlobalFocusPrevContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_PtGroup
JNIEXPORT jint JNICALL OS_NATIVE(PtGroup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtGroup_FUNC);
	rc = (jint)PtGroup;
	OS_NATIVE_EXIT(env, that, PtGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_PtHit
JNIEXPORT jint JNICALL OS_NATIVE(PtHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PhRect_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtHit_FUNC);
	if (arg2) if ((lparg2 = getPhRect_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PtHit(( PtWidget_t *)arg0, arg1, (PhRect_t const *)lparg2);
fail:
	if (arg2 && lparg2) setPhRect_tFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PtHit_FUNC);
	return rc;
}
#endif

#ifndef NO_PtHold
JNIEXPORT jint JNICALL OS_NATIVE(PtHold)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtHold_FUNC);
	rc = (jint)PtHold();
	OS_NATIVE_EXIT(env, that, PtHold_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInflateBalloon
JNIEXPORT jint JNICALL OS_NATIVE(PtInflateBalloon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtInflateBalloon_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)PtInflateBalloon((PtWidget_t *)arg0, (PtWidget_t *)arg1, arg2, (char const *)lparg3, (char const *)lparg4, (PgColor_t)arg5, (PgColor_t)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, PtInflateBalloon_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInit
JNIEXPORT jint JNICALL OS_NATIVE(PtInit)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtInit_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PtInit((char const *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PtInit_FUNC);
	return rc;
}
#endif

#ifndef NO_PtIsFocused
JNIEXPORT jint JNICALL OS_NATIVE(PtIsFocused)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtIsFocused_FUNC);
	rc = (jint)PtIsFocused((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtIsFocused_FUNC);
	return rc;
}
#endif

#ifndef NO_PtLabel
JNIEXPORT jint JNICALL OS_NATIVE(PtLabel)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtLabel_FUNC);
	rc = (jint)PtLabel;
	OS_NATIVE_EXIT(env, that, PtLabel_FUNC);
	return rc;
}
#endif

#ifndef NO_PtLeave
JNIEXPORT jint JNICALL OS_NATIVE(PtLeave)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtLeave_FUNC);
	rc = (jint)PtLeave(arg0);
	OS_NATIVE_EXIT(env, that, PtLeave_FUNC);
	return rc;
}
#endif

#ifndef NO_PtList
JNIEXPORT jint JNICALL OS_NATIVE(PtList)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtList_FUNC);
	rc = (jint)PtList;
	OS_NATIVE_EXIT(env, that, PtList_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListAddItems
JNIEXPORT jint JNICALL OS_NATIVE(PtListAddItems)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtListAddItems_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PtListAddItems((PtWidget_t *)arg0, (const char **)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtListAddItems_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListDeleteAllItems
JNIEXPORT jint JNICALL OS_NATIVE(PtListDeleteAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtListDeleteAllItems_FUNC);
	rc = (jint)PtListDeleteAllItems((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtListDeleteAllItems_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListDeleteItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListDeleteItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtListDeleteItemPos_FUNC);
	rc = (jint)PtListDeleteItemPos((PtWidget_t *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, PtListDeleteItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListGotoPos
JNIEXPORT void JNICALL OS_NATIVE(PtListGotoPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtListGotoPos_FUNC);
	PtListGotoPos((PtWidget_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PtListGotoPos_FUNC);
}
#endif

#ifndef NO_PtListItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListItemPos)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtListItemPos_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PtListItemPos((PtWidget_t *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtListItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListReplaceItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListReplaceItemPos)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtListReplaceItemPos_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PtListReplaceItemPos((PtWidget_t *)arg0, (const char **)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtListReplaceItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_PtListSelectPos
JNIEXPORT void JNICALL OS_NATIVE(PtListSelectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtListSelectPos_FUNC);
	PtListSelectPos((PtWidget_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PtListSelectPos_FUNC);
}
#endif

#ifndef NO_PtListUnselectPos
JNIEXPORT void JNICALL OS_NATIVE(PtListUnselectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtListUnselectPos_FUNC);
	PtListUnselectPos((PtWidget_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PtListUnselectPos_FUNC);
}
#endif

#ifndef NO_PtMainLoop
JNIEXPORT void JNICALL OS_NATIVE(PtMainLoop)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, PtMainLoop_FUNC);
	PtMainLoop();
	OS_NATIVE_EXIT(env, that, PtMainLoop_FUNC);
}
#endif

#ifndef NO_PtMenu
JNIEXPORT jint JNICALL OS_NATIVE(PtMenu)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtMenu_FUNC);
	rc = (jint)PtMenu;
	OS_NATIVE_EXIT(env, that, PtMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_PtMenuBar
JNIEXPORT jint JNICALL OS_NATIVE(PtMenuBar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtMenuBar_FUNC);
	rc = (jint)PtMenuBar;
	OS_NATIVE_EXIT(env, that, PtMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO_PtMenuButton
JNIEXPORT jint JNICALL OS_NATIVE(PtMenuButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtMenuButton_FUNC);
	rc = (jint)PtMenuButton;
	OS_NATIVE_EXIT(env, that, PtMenuButton_FUNC);
	return rc;
}
#endif

#ifndef NO_PtMultiText
JNIEXPORT jint JNICALL OS_NATIVE(PtMultiText)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtMultiText_FUNC);
	rc = (jint)PtMultiText;
	OS_NATIVE_EXIT(env, that, PtMultiText_FUNC);
	return rc;
}
#endif

#ifndef NO_PtNextTopLevelWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtNextTopLevelWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtNextTopLevelWidget_FUNC);
	rc = (jint)PtNextTopLevelWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtNextTopLevelWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtNumericInteger
JNIEXPORT jint JNICALL OS_NATIVE(PtNumericInteger)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtNumericInteger_FUNC);
	rc = (jint)PtNumericInteger;
	OS_NATIVE_EXIT(env, that, PtNumericInteger_FUNC);
	return rc;
}
#endif

#ifndef NO_PtPane
JNIEXPORT jint JNICALL OS_NATIVE(PtPane)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtPane_FUNC);
	rc = (jint)PtPane;
	OS_NATIVE_EXIT(env, that, PtPane_FUNC);
	return rc;
}
#endif

#ifndef NO_PtPanelGroup
JNIEXPORT jint JNICALL OS_NATIVE(PtPanelGroup)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtPanelGroup_FUNC);
	rc = (jint)PtPanelGroup;
	OS_NATIVE_EXIT(env, that, PtPanelGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_PtPositionMenu
JNIEXPORT void JNICALL OS_NATIVE(PtPositionMenu)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, PtPositionMenu_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	PtPositionMenu((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhEvent_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtPositionMenu_FUNC);
}
#endif

#ifndef NO_PtProgress
JNIEXPORT jint JNICALL OS_NATIVE(PtProgress)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtProgress_FUNC);
	rc = (jint)PtProgress;
	OS_NATIVE_EXIT(env, that, PtProgress_FUNC);
	return rc;
}
#endif

#ifndef NO_PtReParentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtReParentWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtReParentWidget_FUNC);
	rc = (jint)PtReParentWidget((PtWidget_t *)arg0, (PtWidget_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtReParentWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtRealizeWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtRealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtRealizeWidget_FUNC);
	rc = (jint)PtRealizeWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtRealizeWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtRegion
JNIEXPORT jint JNICALL OS_NATIVE(PtRegion)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtRegion_FUNC);
	rc = (jint)PtRegion;
	OS_NATIVE_EXIT(env, that, PtRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_PtRelease
JNIEXPORT jint JNICALL OS_NATIVE(PtRelease)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtRelease_FUNC);
	rc = (jint)PtRelease();
	OS_NATIVE_EXIT(env, that, PtRelease_FUNC);
	return rc;
}
#endif

#ifndef NO_PtRemoveCallback
JNIEXPORT void JNICALL OS_NATIVE(PtRemoveCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, PtRemoveCallback_FUNC);
	PtRemoveCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, PtRemoveCallback_FUNC);
}
#endif

#ifndef NO_PtRemoveHotkeyHandler
JNIEXPORT void JNICALL OS_NATIVE(PtRemoveHotkeyHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, PtRemoveHotkeyHandler_FUNC);
	PtRemoveHotkeyHandler((PtWidget_t *)arg0, arg1, arg2, arg3, (void *)arg4, (PtCallbackF_t *)arg5);
	OS_NATIVE_EXIT(env, that, PtRemoveHotkeyHandler_FUNC);
}
#endif

#ifndef NO_PtScrollArea
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollArea)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtScrollArea_FUNC);
	rc = (jint)PtScrollArea;
	OS_NATIVE_EXIT(env, that, PtScrollArea_FUNC);
	return rc;
}
#endif

#ifndef NO_PtScrollContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollContainer)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtScrollContainer_FUNC);
	rc = (jint)PtScrollContainer;
	OS_NATIVE_EXIT(env, that, PtScrollContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_PtScrollbar
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollbar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtScrollbar_FUNC);
	rc = (jint)PtScrollbar;
	OS_NATIVE_EXIT(env, that, PtScrollbar_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSendEventToWidget
JNIEXPORT void JNICALL OS_NATIVE(PtSendEventToWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, PtSendEventToWidget_FUNC);
	PtSendEventToWidget((PtWidget_t *)arg0, (PhEvent_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtSendEventToWidget_FUNC);
}
#endif

#ifndef NO_PtSeparator
JNIEXPORT jint JNICALL OS_NATIVE(PtSeparator)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSeparator_FUNC);
	rc = (jint)PtSeparator;
	OS_NATIVE_EXIT(env, that, PtSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSetAreaFromWidgetCanvas
JNIEXPORT jint JNICALL OS_NATIVE(PtSetAreaFromWidgetCanvas)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhArea_t _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSetAreaFromWidgetCanvas_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPhArea_tFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PtSetAreaFromWidgetCanvas((PtWidget_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setPhArea_tFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtSetAreaFromWidgetCanvas_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSetParentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtSetParentWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSetParentWidget_FUNC);
	rc = (jint)PtSetParentWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtSetParentWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSetResource
JNIEXPORT jint JNICALL OS_NATIVE(PtSetResource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSetResource_FUNC);
	rc = (jint)PtSetResource((PtWidget_t *)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, PtSetResource_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSetResources
JNIEXPORT jint JNICALL OS_NATIVE(PtSetResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSetResources_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PtSetResources((PtWidget_t *)arg0, arg1, (PtArg_t *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PtSetResources_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSlider
JNIEXPORT jint JNICALL OS_NATIVE(PtSlider)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSlider_FUNC);
	rc = (jint)PtSlider;
	OS_NATIVE_EXIT(env, that, PtSlider_FUNC);
	return rc;
}
#endif

#ifndef NO_PtSuperClassDraw
JNIEXPORT void JNICALL OS_NATIVE(PtSuperClassDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, PtSuperClassDraw_FUNC);
	PtSuperClassDraw((PtWidgetClassRef_t *)arg0, (PtWidget_t *)arg1, (PhTile_t const *)arg2);
	OS_NATIVE_EXIT(env, that, PtSuperClassDraw_FUNC);
}
#endif

#ifndef NO_PtSyncWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtSyncWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtSyncWidget_FUNC);
	rc = (jint)PtSyncWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtSyncWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtText
JNIEXPORT jint JNICALL OS_NATIVE(PtText)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtText_FUNC);
	rc = (jint)PtText;
	OS_NATIVE_EXIT(env, that, PtText_FUNC);
	return rc;
}
#endif

#ifndef NO_PtTextGetSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtTextGetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtTextGetSelection_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PtTextGetSelection((PtWidget_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtTextGetSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_PtTextModifyText__IIIIII
JNIEXPORT jint JNICALL OS_NATIVE(PtTextModifyText__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtTextModifyText__IIIIII_FUNC);
	rc = (jint)PtTextModifyText((PtWidget_t *)arg0, arg1, arg2, arg3, (char const *)arg4, arg5);
	OS_NATIVE_EXIT(env, that, PtTextModifyText__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_PtTextModifyText__IIII_3BI
JNIEXPORT jint JNICALL OS_NATIVE(PtTextModifyText__IIII_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtTextModifyText__IIII_3BI_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)PtTextModifyText((PtWidget_t *)arg0, arg1, arg2, arg3, (char const *)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, PtTextModifyText__IIII_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_PtTextSetSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtTextSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtTextSetSelection_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PtTextSetSelection((PtWidget_t *)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PtTextSetSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_PtTimer
JNIEXPORT jint JNICALL OS_NATIVE(PtTimer)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtTimer_FUNC);
	rc = (jint)PtTimer;
	OS_NATIVE_EXIT(env, that, PtTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_PtToggleButton
JNIEXPORT jint JNICALL OS_NATIVE(PtToggleButton)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtToggleButton_FUNC);
	rc = (jint)PtToggleButton;
	OS_NATIVE_EXIT(env, that, PtToggleButton_FUNC);
	return rc;
}
#endif

#ifndef NO_PtToolbar
JNIEXPORT jint JNICALL OS_NATIVE(PtToolbar)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtToolbar_FUNC);
	rc = (jint)PtToolbar;
	OS_NATIVE_EXIT(env, that, PtToolbar_FUNC);
	return rc;
}
#endif

#ifndef NO_PtUnblockWindows
JNIEXPORT void JNICALL OS_NATIVE(PtUnblockWindows)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PtUnblockWindows_FUNC);
	PtUnblockWindows((PtBlockedList_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtUnblockWindows_FUNC);
}
#endif

#ifndef NO_PtUnrealizeWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtUnrealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtUnrealizeWidget_FUNC);
	rc = (jint)PtUnrealizeWidget((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtUnrealizeWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_PtValidParent
JNIEXPORT jint JNICALL OS_NATIVE(PtValidParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtValidParent_FUNC);
	rc = (jint)PtValidParent((PtWidget_t *)arg0, (PtWidgetClassRef_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtValidParent_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWebClient
JNIEXPORT jint JNICALL OS_NATIVE(PtWebClient)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWebClient_FUNC);
	rc = (jint)PtWebClient;
	OS_NATIVE_EXIT(env, that, PtWebClient_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetArea
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetArea)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhArea_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetArea_FUNC);
	if (arg1) if ((lparg1 = getPhArea_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtWidgetArea((PtWidget_t *)arg0, (PhArea_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhArea_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtWidgetArea_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetBrotherBehind
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetBrotherBehind)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetBrotherBehind_FUNC);
	rc = (jint)PtWidgetBrotherBehind((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetBrotherBehind_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetBrotherInFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetBrotherInFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetBrotherInFront_FUNC);
	rc = (jint)PtWidgetBrotherInFront((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetBrotherInFront_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetCanvas__II
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetCanvas__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetCanvas__II_FUNC);
	rc = (jint)PtWidgetCanvas((PtWidget_t *)arg0, (PhRect_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtWidgetCanvas__II_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtWidgetCanvas((PtWidget_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetChildBack
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetChildBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetChildBack_FUNC);
	rc = (jint)PtWidgetChildBack((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetChildBack_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetChildFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetChildFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetChildFront_FUNC);
	rc = (jint)PtWidgetChildFront((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetChildFront_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetClass_FUNC);
	rc = (jint)PtWidgetClass((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetExtent__II
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetExtent__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetExtent__II_FUNC);
	rc = (jint)PtWidgetExtent((PtWidget_t *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PtWidgetExtent__II_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtWidgetExtent((PtWidget_t *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPhRect_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetFlags
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetFlags_FUNC);
	rc = (jint)PtWidgetFlags((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetInsert
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetInsert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetInsert_FUNC);
	rc = (jint)PtWidgetInsert((PtWidget_t *)arg0, (PtWidget_t *)arg1, arg2);
	OS_NATIVE_EXIT(env, that, PtWidgetInsert_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetIsClassMember
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetIsClassMember)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetIsClassMember_FUNC);
	rc = (jint)PtWidgetIsClassMember((PtWidget_t *)arg0, (PtWidgetClassRef_t *)arg1);
	OS_NATIVE_EXIT(env, that, PtWidgetIsClassMember_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetIsRealized
JNIEXPORT jboolean JNICALL OS_NATIVE(PtWidgetIsRealized)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetIsRealized_FUNC);
	rc = (jboolean)PtWidgetIsRealized((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetIsRealized_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetOffset
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetOffset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetOffset_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtWidgetOffset((PtWidget_t *)arg0, (PhPoint_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhPoint_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtWidgetOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetParent
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetParent_FUNC);
	rc = (jint)PtWidgetParent((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetPreferredSize
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetPreferredSize)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhDim_t _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetPreferredSize_FUNC);
	if (arg1) if ((lparg1 = getPhDim_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PtWidgetPreferredSize((PtWidget_t *)arg0, (PhDim_t *)lparg1);
fail:
	if (arg1 && lparg1) setPhDim_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PtWidgetPreferredSize_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetRid
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetRid)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetRid_FUNC);
	rc = (jint)PtWidgetRid((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetRid_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetToBack
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetToBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetToBack_FUNC);
	rc = (jint)PtWidgetToBack((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetToBack_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWidgetToFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWidgetToFront_FUNC);
	rc = (jint)PtWidgetToFront((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWidgetToFront_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWindow
JNIEXPORT jint JNICALL OS_NATIVE(PtWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWindow_FUNC);
	rc = (jint)PtWindow;
	OS_NATIVE_EXIT(env, that, PtWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWindowFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtWindowFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWindowFocus_FUNC);
	rc = (jint)PtWindowFocus((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWindowFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWindowGetState
JNIEXPORT jint JNICALL OS_NATIVE(PtWindowGetState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PtWindowGetState_FUNC);
	rc = (jint)PtWindowGetState((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWindowGetState_FUNC);
	return rc;
}
#endif

#ifndef NO_PtWindowToBack
JNIEXPORT void JNICALL OS_NATIVE(PtWindowToBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PtWindowToBack_FUNC);
	PtWindowToBack((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWindowToBack_FUNC);
}
#endif

#ifndef NO_PtWindowToFront
JNIEXPORT void JNICALL OS_NATIVE(PtWindowToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, PtWindowToFront_FUNC);
	PtWindowToFront((PtWidget_t *)arg0);
	OS_NATIVE_EXIT(env, that, PtWindowToFront_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PgAlpha_t _arg1={0}, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPgAlpha_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhArea_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhArea_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhCursorDef_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhCursorDef_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhEvent_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhImage_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhImage_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhPoint_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhPoint_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhPointerEvent_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhPointerEvent_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhRect_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhTile_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPhTile_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PtTextCallback_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPtTextCallback_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PtWebClient2Data_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PtWebClient2Data_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PtWebClient2Data_t _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_photon_PtWebClient2Data_1t_2I_FUNC);
	if (arg1) if ((lparg1 = getPtWebClient2Data_tFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove(arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) setPtWebClient2Data_tFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_photon_PtWebClient2Data_1t_2I_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FontDetails _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setFontDetailsFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PgAlpha_t _arg0={0}, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPgAlpha_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PgMap_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPgMap_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhClipHeader _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhClipHeaderFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhEvent_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhEvent_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhImage_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhImage_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhKeyEvent_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhKeyEvent_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhPointerEvent_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhPointerEvent_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhRect_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhRect_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhTile_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhTile_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhWindowEvent_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPhWindowEvent_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtCallbackInfo_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtCallbackInfo_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtScrollbarCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtScrollbarCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtTextCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtTextCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebDataReqCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = getPtWebDataReqCallback_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtWebDataReqCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebMetaDataCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebMetaDataCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebMetaDataCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebMetaDataCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = getPtWebMetaDataCallback_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtWebMetaDataCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebMetaDataCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebStatusCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = getPtWebStatusCallback_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtWebStatusCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebWindowCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebWindowCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebWindowCallback_t _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebWindowCallback_1t_2II_FUNC);
	if (arg0) if ((lparg0 = getPtWebWindowCallback_tFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPtWebWindowCallback_tFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_photon_PtWebWindowCallback_1t_2II_FUNC);
}
#endif

#ifndef NO_memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	PhClipHeader _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPhClipHeaderFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setPhClipHeaderFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I_FUNC);
}
#endif

#ifndef NO_strdup
JNIEXPORT jint JNICALL OS_NATIVE(strdup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, strdup_FUNC);
	rc = (jint)strdup((const char *)arg0);
	OS_NATIVE_EXIT(env, that, strdup_FUNC);
	return rc;
}
#endif

#ifndef NO_uname
JNIEXPORT jint JNICALL OS_NATIVE(uname)
	(JNIEnv *env, jclass that, jobject arg0)
{
	utsname _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, uname_FUNC);
	if (arg0) if ((lparg0 = getutsnameFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)uname((utsname *)lparg0);
fail:
	if (arg0 && lparg0) setutsnameFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, uname_FUNC);
	return rc;
}
#endif


/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "os_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_photon_OS_##func

#ifndef NO_PfDecomposeStemToID
JNIEXPORT jint JNICALL OS_NATIVE(PfDecomposeStemToID)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PfDecomposeStemToID\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)PfDecomposeStemToID(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PfDecomposeStemToID\n")
	return rc;
}
#endif

#ifndef NO_PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III
JNIEXPORT jint JNICALL OS_NATIVE(PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PfExtentText(lparg0, lparg1, (const char *)arg2, (const char *)arg3, arg4);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)PfExtentText(lparg0, lparg1, (const char *)lparg2, (const char *)lparg3, arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PfExtentWideText\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)PfExtentWideText(lparg0, lparg1, (const char *)lparg2, (const uint16_t *)lparg3, arg4);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PfExtentWideText\n")
	return rc;
}
#endif

#ifndef NO_PfFontDescription
JNIEXPORT jint JNICALL OS_NATIVE(PfFontDescription)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PfFontDescription\n")
	rc = (jint)PfFontDescription((FontID *)arg0);
	NATIVE_EXIT(env, that, "PfFontDescription\n")
	return rc;
}
#endif

#ifndef NO_PfFontFlags
JNIEXPORT jint JNICALL OS_NATIVE(PfFontFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PfFontFlags\n")
	rc = (jint)PfFontFlags((FontID *)arg0);
	NATIVE_EXIT(env, that, "PfFontFlags\n")
	return rc;
}
#endif

#ifndef NO_PfFontSize
JNIEXPORT jint JNICALL OS_NATIVE(PfFontSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PfFontSize\n")
	rc = (jint)PfFontSize((FontID *)arg0);
	NATIVE_EXIT(env, that, "PfFontSize\n")
	return rc;
}
#endif

#ifndef NO_PfFreeFont
JNIEXPORT jint JNICALL OS_NATIVE(PfFreeFont)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PfFreeFont\n")
	rc = (jint)PfFreeFont((FontID *)arg0);
	NATIVE_EXIT(env, that, "PfFreeFont\n")
	return rc;
}
#endif

#ifndef NO_PfGenerateFontName
JNIEXPORT jint JNICALL OS_NATIVE(PfGenerateFontName)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PfGenerateFontName\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)PfGenerateFontName((char const *)lparg0, arg1, arg2, (char *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PfGenerateFontName\n")
	return rc;
}
#endif

#ifndef NO_PfLoadMetrics
JNIEXPORT jint JNICALL OS_NATIVE(PfLoadMetrics)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PfLoadMetrics\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)PfLoadMetrics((const char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PfLoadMetrics\n")
	return rc;
}
#endif

#ifndef NO_PfQueryFontInfo
JNIEXPORT jint JNICALL OS_NATIVE(PfQueryFontInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1)
{
	jbyte *lparg0=NULL;
	FontQueryInfo _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PfQueryFontInfo\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = getFontQueryInfoFields(env, arg1, &_arg1);
	rc = (jint)PfQueryFontInfo((const char *)lparg0, (FontQueryInfo *)lparg1);
	if (arg1) setFontQueryInfoFields(env, arg1, lparg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PfQueryFontInfo\n")
	return rc;
}
#endif

#ifndef NO_PfQueryFonts
JNIEXPORT jint JNICALL OS_NATIVE(PfQueryFonts)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PfQueryFonts\n")
	rc = (jint)PfQueryFonts(arg0, arg1, (FontDetails *)arg2, arg3);
	NATIVE_EXIT(env, that, "PfQueryFonts\n")
	return rc;
}
#endif

#ifndef NO_PgAlphaOff
JNIEXPORT void JNICALL OS_NATIVE(PgAlphaOff)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "PgAlphaOff\n")
	PgAlphaOff();
	NATIVE_EXIT(env, that, "PgAlphaOff\n")
}
#endif

#ifndef NO_PgAlphaOn
JNIEXPORT void JNICALL OS_NATIVE(PgAlphaOn)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "PgAlphaOn\n")
	PgAlphaOn();
	NATIVE_EXIT(env, that, "PgAlphaOn\n")
}
#endif

#ifndef NO_PgCreateGC
JNIEXPORT jint JNICALL OS_NATIVE(PgCreateGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgCreateGC\n")
	rc = (jint)PgCreateGC(arg0);
	NATIVE_EXIT(env, that, "PgCreateGC\n")
	return rc;
}
#endif

#ifndef NO_PgDestroyGC
JNIEXPORT void JNICALL OS_NATIVE(PgDestroyGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PgDestroyGC\n")
	PgDestroyGC((PhGC_t *)arg0);
	NATIVE_EXIT(env, that, "PgDestroyGC\n")
}
#endif

#ifndef NO_PgDrawArc
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawArc)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawArc\n")
	if (arg0) lparg0 = getPhPoint_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PgDrawArc(lparg0, lparg1, arg2, arg3, arg4);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhPoint_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawArc\n")
	return rc;
}
#endif

#ifndef NO_PgDrawArrow
JNIEXPORT void JNICALL OS_NATIVE(PgDrawArrow)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jint arg2, jint arg3)
{
	PhRect_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "PgDrawArrow\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	PgDrawArrow(lparg0, arg1, (PgColor_t)arg2, arg3);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawArrow\n")
}
#endif

#ifndef NO_PgDrawBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawBitmap\n")
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getPhDim_tFields(env, arg3, &_arg3);
	rc = (jint)PgDrawBitmap((void const *)arg0, (int)arg1, (PhPoint_t *)lparg2, (PhPoint_t *)lparg3, (int)arg4, (long)arg5);
	if (arg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PgDrawBitmap\n")
	return rc;
}
#endif

#ifndef NO_PgDrawEllipse
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawEllipse)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawEllipse\n")
	if (arg0) lparg0 = getPhPoint_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PgDrawEllipse(lparg0, lparg1, arg2);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhPoint_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawEllipse\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawGradient\n")
	if (arg0) lparg0 = getPhPoint_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg10) lparg10 = (*env)->GetByteArrayElements(env, arg10, NULL);
	rc = (jint)PgDrawGradient(lparg0, lparg1, arg2, arg3, arg4, (PgColor_t)arg5, (PgColor_t)arg6, (PgColor_t)arg7, (PgColor_t)arg8, arg9, (unsigned char *)lparg10);
	if (arg10) (*env)->ReleaseByteArrayElements(env, arg10, lparg10, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhPoint_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawGradient\n")
	return rc;
}
#endif

#ifndef NO_PgDrawILine
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawILine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawILine\n")
	rc = (jint)PgDrawILine(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "PgDrawILine\n")
	return rc;
}
#endif

#ifndef NO_PgDrawIPixel
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawIPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawIPixel\n")
	rc = (jint)PgDrawIPixel(arg0, arg1);
	NATIVE_EXIT(env, that, "PgDrawIPixel\n")
	return rc;
}
#endif

#ifndef NO_PgDrawIRect
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawIRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawIRect\n")
	rc = (jint)PgDrawIRect(arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "PgDrawIRect\n")
	return rc;
}
#endif

#ifndef NO_PgDrawImage
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawImage\n")
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getPhDim_tFields(env, arg3, &_arg3);
	rc = (jint)PgDrawImage((void const *)arg0, arg1, lparg2, lparg3, arg4, arg5);
	if (arg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PgDrawImage\n")
	return rc;
}
#endif

#ifndef NO_PgDrawMultiTextArea
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawMultiTextArea)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawMultiTextArea\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = getPhRect_tFields(env, arg2, &_arg2);
	rc = (jint)PgDrawMultiTextArea((char *)lparg0, arg1, lparg2, arg3, arg4, arg5);
	if (arg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgDrawMultiTextArea\n")
	return rc;
}
#endif

#ifndef NO_PgDrawPhImageRectmx
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawPhImageRectmx)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jint arg3)
{
	PhPoint_t _arg0, *lparg0=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawPhImageRectmx\n")
	if (arg0) lparg0 = getPhPoint_tFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getPhRect_tFields(env, arg2, &_arg2);
	rc = (jint)PgDrawPhImageRectmx(lparg0, (PhImage_t const *)arg1, lparg2, arg3);
	if (arg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg0) setPhPoint_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawPhImageRectmx\n")
	return rc;
}
#endif

#ifndef NO_PgDrawPolygon
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawPolygon)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jobject arg2, jint arg3)
{
	jshort *lparg0=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawPolygon\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	rc = (jint)PgDrawPolygon((PhPoint_t const *)lparg0, arg1, (PhPoint_t const *)lparg2, arg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgDrawPolygon\n")
	return rc;
}
#endif

#ifndef NO_PgDrawRoundRect
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawRoundRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawRoundRect\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PgDrawRoundRect((PhRect_t const *)lparg0, (PhPoint_t const *)lparg1, arg2);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgDrawRoundRect\n")
	return rc;
}
#endif

#ifndef NO_PgDrawTImage
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawTImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	PhPoint_t _arg2, *lparg2=NULL;
	PhDim_t _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawTImage\n")
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getPhDim_tFields(env, arg3, &_arg3);
	rc = (jint)PgDrawTImage((void const *)arg0, arg1, (PhPoint_t const *)lparg2, (PhDim_t const *)lparg3, arg4, arg5, (void const *)arg6, arg7);
	if (arg3) setPhDim_tFields(env, arg3, lparg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PgDrawTImage\n")
	return rc;
}
#endif

#ifndef NO_PgDrawText
JNIEXPORT jint JNICALL OS_NATIVE(PgDrawText)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jobject arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgDrawText\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	rc = (jint)PgDrawText((char const *)lparg0, arg1, (PhPoint_t *)lparg2, arg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgDrawText\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PgExtentMultiText\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)PgExtentMultiText((PhRect_t *)lparg0, (PhPoint_t *)lparg1, (char *)lparg2, (char *)lparg3, arg4, arg5);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgExtentMultiText\n")
	return rc;
}
#endif

#ifndef NO_PgFlush
JNIEXPORT jint JNICALL OS_NATIVE(PgFlush)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgFlush\n")
	rc = (jint)PgFlush();
	NATIVE_EXIT(env, that, "PgFlush\n")
	return rc;
}
#endif

#ifndef NO_PgGetVideoMode
JNIEXPORT jint JNICALL OS_NATIVE(PgGetVideoMode)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PgDisplaySettings_t _arg0={0}, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgGetVideoMode\n")
	if (arg0) lparg0 = getPgDisplaySettings_tFields(env, arg0, &_arg0);
	rc = (jint)PgGetVideoMode((PgDisplaySettings_t *)lparg0);
	if (arg0) setPgDisplaySettings_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgGetVideoMode\n")
	return rc;
}
#endif

#ifndef NO_PgGetVideoModeInfo
JNIEXPORT jint JNICALL OS_NATIVE(PgGetVideoModeInfo)
	(JNIEnv *env, jclass that, jshort arg0, jobject arg1)
{
	PgVideoModeInfo_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgGetVideoModeInfo\n")
	if (arg1) lparg1 = getPgVideoModeInfo_tFields(env, arg1, &_arg1);
	rc = (jint)PgGetVideoModeInfo(arg0, (PgVideoModeInfo_t *)lparg1);
	if (arg1) setPgVideoModeInfo_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PgGetVideoModeInfo\n")
	return rc;
}
#endif

#ifndef NO_PgReadScreen
JNIEXPORT jint JNICALL OS_NATIVE(PgReadScreen)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	PhRect_t _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgReadScreen\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	rc = (jint)PgReadScreen((PhRect_t *)lparg0, (void *)arg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgReadScreen\n")
	return rc;
}
#endif

#ifndef NO_PgReadScreenSize
JNIEXPORT jint JNICALL OS_NATIVE(PgReadScreenSize)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhRect_t _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgReadScreenSize\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	rc = (jint)PgReadScreenSize((PhRect_t *)lparg0);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgReadScreenSize\n")
	return rc;
}
#endif

#ifndef NO_PgSetAlpha
JNIEXPORT void JNICALL OS_NATIVE(PgSetAlpha)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbyte arg3, jbyte arg4)
{
	PgMap_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "PgSetAlpha\n")
	if (arg1) lparg1 = getPgMap_tFields(env, arg1, &_arg1);
	PgSetAlpha(arg0, (PgMap_t const *)lparg1, (PgGradient_t const *)arg2, arg3, arg4);
	if (arg1) setPgMap_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PgSetAlpha\n")
}
#endif

#ifndef NO_PgSetClipping
JNIEXPORT void JNICALL OS_NATIVE(PgSetClipping)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PgSetClipping\n")
	PgSetClipping(arg0, (PhRect_t const *)arg1);
	NATIVE_EXIT(env, that, "PgSetClipping\n")
}
#endif

#ifndef NO_PgSetDrawBufferSize
JNIEXPORT jint JNICALL OS_NATIVE(PgSetDrawBufferSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetDrawBufferSize\n")
	rc = (jint)PgSetDrawBufferSize(arg0);
	NATIVE_EXIT(env, that, "PgSetDrawBufferSize\n")
	return rc;
}
#endif

#ifndef NO_PgSetDrawMode
JNIEXPORT jint JNICALL OS_NATIVE(PgSetDrawMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetDrawMode\n")
	rc = (jint)PgSetDrawMode(arg0);
	NATIVE_EXIT(env, that, "PgSetDrawMode\n")
	return rc;
}
#endif

#ifndef NO_PgSetFillColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetFillColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetFillColor\n")
	rc = (jint)PgSetFillColor((PgColor_t)arg0);
	NATIVE_EXIT(env, that, "PgSetFillColor\n")
	return rc;
}
#endif

#ifndef NO_PgSetFillTransPat
JNIEXPORT void JNICALL OS_NATIVE(PgSetFillTransPat)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "PgSetFillTransPat\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	PgSetFillTransPat((PgPattern_t)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgSetFillTransPat\n")
}
#endif

#ifndef NO_PgSetFont
JNIEXPORT void JNICALL OS_NATIVE(PgSetFont)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "PgSetFont\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	PgSetFont((char const *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgSetFont\n")
}
#endif

#ifndef NO_PgSetGC
JNIEXPORT jint JNICALL OS_NATIVE(PgSetGC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetGC\n")
	rc = (jint)PgSetGC((PhGC_t *)arg0);
	NATIVE_EXIT(env, that, "PgSetGC\n")
	return rc;
}
#endif

#ifndef NO_PgSetMultiClip
JNIEXPORT jint JNICALL OS_NATIVE(PgSetMultiClip)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetMultiClip\n")
	rc = (jint)PgSetMultiClip(arg0, (PhRect_t const *)arg1);
	NATIVE_EXIT(env, that, "PgSetMultiClip\n")
	return rc;
}
#endif

#ifndef NO_PgSetPalette
JNIEXPORT jint JNICALL OS_NATIVE(PgSetPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jshort arg3, jint arg4, jint arg5)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetPalette\n")
	rc = (jint)PgSetPalette((PgColor_t const *)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "PgSetPalette\n")
	return rc;
}
#endif

#ifndef NO_PgSetRegion
JNIEXPORT void JNICALL OS_NATIVE(PgSetRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PgSetRegion\n")
	PgSetRegion((PhRid_t)arg0);
	NATIVE_EXIT(env, that, "PgSetRegion\n")
}
#endif

#ifndef NO_PgSetStrokeCap
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeCap)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetStrokeCap\n")
	rc = (jint)PgSetStrokeCap(arg0);
	NATIVE_EXIT(env, that, "PgSetStrokeCap\n")
	return rc;
}
#endif

#ifndef NO_PgSetStrokeColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetStrokeColor\n")
	rc = (jint)PgSetStrokeColor((PgColor_t)arg0);
	NATIVE_EXIT(env, that, "PgSetStrokeColor\n")
	return rc;
}
#endif

#ifndef NO_PgSetStrokeDash
JNIEXPORT void JNICALL OS_NATIVE(PgSetStrokeDash)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "PgSetStrokeDash\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	PgSetStrokeDash((unsigned char const *)lparg0, arg1, arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PgSetStrokeDash\n")
}
#endif

#ifndef NO_PgSetStrokeWidth
JNIEXPORT jint JNICALL OS_NATIVE(PgSetStrokeWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetStrokeWidth\n")
	rc = (jint)PgSetStrokeWidth(arg0);
	NATIVE_EXIT(env, that, "PgSetStrokeWidth\n")
	return rc;
}
#endif

#ifndef NO_PgSetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(PgSetTextColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgSetTextColor\n")
	rc = (jint)PgSetTextColor((PgColor_t)arg0);
	NATIVE_EXIT(env, that, "PgSetTextColor\n")
	return rc;
}
#endif

#ifndef NO_PgSetTextXORColor
JNIEXPORT void JNICALL OS_NATIVE(PgSetTextXORColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PgSetTextXORColor\n")
	PgSetTextXORColor((PgColor_t)arg0, (PgColor_t)arg1);
	NATIVE_EXIT(env, that, "PgSetTextXORColor\n")
}
#endif

#ifndef NO_PgSetUserClip
JNIEXPORT void JNICALL OS_NATIVE(PgSetUserClip)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhRect_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "PgSetUserClip\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	PgSetUserClip((PhRect_t const *)lparg0);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PgSetUserClip\n")
}
#endif

#ifndef NO_PgShmemCreate
JNIEXPORT jint JNICALL OS_NATIVE(PgShmemCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PgShmemCreate\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)PgShmemCreate(arg0, (char const *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PgShmemCreate\n")
	return rc;
}
#endif

#ifndef NO_PgShmemDestroy
JNIEXPORT jint JNICALL OS_NATIVE(PgShmemDestroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PgShmemDestroy\n")
	rc = (jint)PgShmemDestroy((void *)arg0);
	NATIVE_EXIT(env, that, "PgShmemDestroy\n")
	return rc;
}
#endif

#ifndef NO_PhAddMergeTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhAddMergeTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhAddMergeTiles\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PhAddMergeTiles((PhTile_t *)arg0, (PhTile_t *)arg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PhAddMergeTiles\n")
	return rc;
}
#endif

#ifndef NO_PhAreaToRect
JNIEXPORT void JNICALL OS_NATIVE(PhAreaToRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	PhArea_t _arg0, *lparg0=NULL;
	PhRect_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "PhAreaToRect\n")
	if (arg0) lparg0 = getPhArea_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	PhAreaToRect((PhArea_t const *)lparg0, (PhRect_t *)lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	if (arg0) setPhArea_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PhAreaToRect\n")
}
#endif

#ifndef NO_PhBlit
JNIEXPORT void JNICALL OS_NATIVE(PhBlit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "PhBlit\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	PhBlit((PhRid_t)arg0, (const PhRect_t *)lparg1, (const PhPoint_t *)lparg2);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PhBlit\n")
}
#endif

#ifndef NO_PhClipTilings
JNIEXPORT jint JNICALL OS_NATIVE(PhClipTilings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhClipTilings\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PhClipTilings((PhTile_t *)arg0, (PhTile_t *)arg1, (PhTile_t **)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PhClipTilings\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardCopy
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardCopy)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardCopy\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)PhClipboardCopy(arg0, arg1, (PhClipHeader const *)lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PhClipboardCopy\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardCopyString
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardCopyString)
	(JNIEnv *env, jclass that, jshort arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardCopyString\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)PhClipboardCopyString(arg0, (const char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PhClipboardCopyString\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteFinish
JNIEXPORT void JNICALL OS_NATIVE(PhClipboardPasteFinish)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PhClipboardPasteFinish\n")
	PhClipboardPasteFinish((void *)arg0);
	NATIVE_EXIT(env, that, "PhClipboardPasteFinish\n")
}
#endif

#ifndef NO_PhClipboardPasteStart
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteStart)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardPasteStart\n")
	rc = (jint)PhClipboardPasteStart(arg0);
	NATIVE_EXIT(env, that, "PhClipboardPasteStart\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteString
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteString)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardPasteString\n")
	rc = (jint)PhClipboardPasteString(arg0);
	NATIVE_EXIT(env, that, "PhClipboardPasteString\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteType
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteType)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardPasteType\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)PhClipboardPasteType((void *)arg0, lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PhClipboardPasteType\n")
	return rc;
}
#endif

#ifndef NO_PhClipboardPasteTypeN
JNIEXPORT jint JNICALL OS_NATIVE(PhClipboardPasteTypeN)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhClipboardPasteTypeN\n")
	rc = (jint)PhClipboardPasteTypeN((void *)arg0, arg1);
	NATIVE_EXIT(env, that, "PhClipboardPasteTypeN\n")
	return rc;
}
#endif

#ifndef NO_PhCoalesceTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhCoalesceTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhCoalesceTiles\n")
	rc = (jint)PhCoalesceTiles((PhTile_t *)arg0);
	NATIVE_EXIT(env, that, "PhCoalesceTiles\n")
	return rc;
}
#endif

#ifndef NO_PhCopyTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhCopyTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhCopyTiles\n")
	rc = (jint)PhCopyTiles((PhTile_t *)arg0);
	NATIVE_EXIT(env, that, "PhCopyTiles\n")
	return rc;
}
#endif

#ifndef NO_PhCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(PhCreateImage)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	PhImage_t _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhCreateImage\n")
	if (arg0) lparg0 = getPhImage_tFields(env, arg0, &_arg0);
	rc = (jint)PhCreateImage((PhImage_t *)lparg0, arg1, arg2, arg3, (PgColor_t const *)arg4, arg5, arg6);
	if (arg0) setPhImage_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PhCreateImage\n")
	return rc;
}
#endif

#ifndef NO_PhDCSetCurrent
JNIEXPORT jint JNICALL OS_NATIVE(PhDCSetCurrent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhDCSetCurrent\n")
	rc = (jint)PhDCSetCurrent((void *)arg0);
	NATIVE_EXIT(env, that, "PhDCSetCurrent\n")
	return rc;
}
#endif

#ifndef NO_PhDeTranslateTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhDeTranslateTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhDeTranslateTiles\n")
	rc = (jint)PhDeTranslateTiles((PhTile_t *)arg0, (PhPoint_t const *)arg1);
	NATIVE_EXIT(env, that, "PhDeTranslateTiles\n")
	return rc;
}
#endif

#ifndef NO_PhEventNext
JNIEXPORT jint JNICALL OS_NATIVE(PhEventNext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhEventNext\n")
	rc = (jint)PhEventNext((void *)arg0, arg1);
	NATIVE_EXIT(env, that, "PhEventNext\n")
	return rc;
}
#endif

#ifndef NO_PhEventPeek
JNIEXPORT jint JNICALL OS_NATIVE(PhEventPeek)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhEventPeek\n")
	rc = (jint)PhEventPeek((void *)arg0, arg1);
	NATIVE_EXIT(env, that, "PhEventPeek\n")
	return rc;
}
#endif

#ifndef NO_PhFreeTiles
JNIEXPORT void JNICALL OS_NATIVE(PhFreeTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PhFreeTiles\n")
	PhFreeTiles((PhTile_t *)arg0);
	NATIVE_EXIT(env, that, "PhFreeTiles\n")
}
#endif

#ifndef NO_PhGetData
JNIEXPORT jint JNICALL OS_NATIVE(PhGetData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhGetData\n")
	rc = (jint)PhGetData((PhEvent_t *)arg0);
	NATIVE_EXIT(env, that, "PhGetData\n")
	return rc;
}
#endif

#ifndef NO_PhGetMsgSize
JNIEXPORT jint JNICALL OS_NATIVE(PhGetMsgSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhGetMsgSize\n")
	rc = (jint)PhGetMsgSize((PhEvent_t const *)arg0);
	NATIVE_EXIT(env, that, "PhGetMsgSize\n")
	return rc;
}
#endif

#ifndef NO_PhGetRects
JNIEXPORT jint JNICALL OS_NATIVE(PhGetRects)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhGetRects\n")
	rc = (jint)PhGetRects((PhEvent_t *)arg0);
	NATIVE_EXIT(env, that, "PhGetRects\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PhInitDrag\n")
	if (arg2) lparg2 = getPhRect_tFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getPhRect_tFields(env, arg3, &_arg3);
	if (arg5) lparg5 = getPhDim_tFields(env, arg5, &_arg5);
	if (arg6) lparg6 = getPhDim_tFields(env, arg6, &_arg6);
	if (arg7) lparg7 = getPhDim_tFields(env, arg7, &_arg7);
	if (arg8) lparg8 = getPhPoint_tFields(env, arg8, &_arg8);
	if (arg9) lparg9 = (*env)->GetShortArrayElements(env, arg9, NULL);
	rc = (jint)PhInitDrag((PhRid_t)arg0, arg1, (const PhRect_t *)lparg2, (const PhRect_t *)lparg3, arg4, (PhDim_t *)lparg5, (PhDim_t *)lparg6, (PhDim_t *)lparg7, (PhPoint_t *)lparg8, (PhCursorDescription_t *)lparg9);
	if (arg9) (*env)->ReleaseShortArrayElements(env, arg9, lparg9, 0);
	if (arg8) setPhPoint_tFields(env, arg8, lparg8);
	if (arg7) setPhDim_tFields(env, arg7, lparg7);
	if (arg6) setPhDim_tFields(env, arg6, lparg6);
	if (arg5) setPhDim_tFields(env, arg5, lparg5);
	if (arg3) setPhRect_tFields(env, arg3, lparg3);
	if (arg2) setPhRect_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PhInitDrag\n")
	return rc;
}
#endif

#ifndef NO_PhInputGroup
JNIEXPORT jint JNICALL OS_NATIVE(PhInputGroup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhInputGroup\n")
	rc = (jint)PhInputGroup((PhEvent_t *)arg0);
	NATIVE_EXIT(env, that, "PhInputGroup\n")
	return rc;
}
#endif

#ifndef NO_PhIntersectTilings
JNIEXPORT jint JNICALL OS_NATIVE(PhIntersectTilings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhIntersectTilings\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)PhIntersectTilings((PhTile_t const *)arg0, (PhTile_t const *)arg1, lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PhIntersectTilings\n")
	return rc;
}
#endif

#ifndef NO_PhKeyToMb
JNIEXPORT jint JNICALL OS_NATIVE(PhKeyToMb)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1)
{
	jbyte *lparg0=NULL;
	PhKeyEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhKeyToMb\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = getPhKeyEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PhKeyToMb((char *)lparg0, lparg1);
	if (arg1) setPhKeyEvent_tFields(env, arg1, lparg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PhKeyToMb\n")
	return rc;
}
#endif

#ifndef NO_PhMakeGhostBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PhMakeGhostBitmap)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhMakeGhostBitmap\n")
	rc = (jint)PhMakeGhostBitmap((PhImage_t *)arg0);
	NATIVE_EXIT(env, that, "PhMakeGhostBitmap\n")
	return rc;
}
#endif

#ifndef NO_PhMakeTransBitmap
JNIEXPORT jint JNICALL OS_NATIVE(PhMakeTransBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhMakeTransBitmap\n")
	rc = (jint)PhMakeTransBitmap((PhImage_t *)arg0, (PgColor_t)arg1);
	NATIVE_EXIT(env, that, "PhMakeTransBitmap\n")
	return rc;
}
#endif

#ifndef NO_PhMergeTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhMergeTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhMergeTiles\n")
	rc = (jint)PhMergeTiles((PhTile_t *)arg0);
	NATIVE_EXIT(env, that, "PhMergeTiles\n")
	return rc;
}
#endif

#ifndef NO_PhMoveCursorAbs
JNIEXPORT void JNICALL OS_NATIVE(PhMoveCursorAbs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "PhMoveCursorAbs\n")
	PhMoveCursorAbs(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "PhMoveCursorAbs\n")
}
#endif

#ifndef NO_PhQueryCursor
JNIEXPORT jint JNICALL OS_NATIVE(PhQueryCursor)
	(JNIEnv *env, jclass that, jshort arg0, jobject arg1)
{
	PhCursorInfo_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhQueryCursor\n")
	if (arg1) lparg1 = getPhCursorInfo_tFields(env, arg1, &_arg1);
	rc = (jint)PhQueryCursor(arg0, (PhCursorInfo_t *)lparg1);
	if (arg1) setPhCursorInfo_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PhQueryCursor\n")
	return rc;
}
#endif

#ifndef NO_PhQueryRids
JNIEXPORT jint JNICALL OS_NATIVE(PhQueryRids)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6, jintArray arg7, jint arg8)
{
	PhRect_t _arg6, *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhQueryRids\n")
	if (arg6) lparg6 = getPhRect_tFields(env, arg6, &_arg6);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)PhQueryRids(arg0, (PhRid_t)arg1, arg2, arg3, arg4, (PhRid_t)arg5, (const PhRect_t *)lparg6, (PhRid_t *)lparg7, arg8);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) setPhRect_tFields(env, arg6, lparg6);
	NATIVE_EXIT(env, that, "PhQueryRids\n")
	return rc;
}
#endif

#ifndef NO_PhRectIntersect
JNIEXPORT jint JNICALL OS_NATIVE(PhRectIntersect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhRectIntersect\n")
	rc = (jint)PhRectIntersect((PhRect_t *)arg0, (PhRect_t const *)arg1);
	NATIVE_EXIT(env, that, "PhRectIntersect\n")
	return rc;
}
#endif

#ifndef NO_PhRectUnion__II
JNIEXPORT jint JNICALL OS_NATIVE(PhRectUnion__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhRectUnion__II\n")
	rc = (jint)PhRectUnion((PhRect_t *)arg0, (PhRect_t const *)arg1);
	NATIVE_EXIT(env, that, "PhRectUnion__II\n")
	return rc;
}
#endif

#ifndef NO_PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	PhRect_t _arg0, *lparg0=NULL;
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	if (arg0) lparg0 = getPhRect_tFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PhRectUnion((PhRect_t *)lparg0, (PhRect_t const *)lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	return rc;
}
#endif

#ifndef NO_PhRectsToTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhRectsToTiles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhRectsToTiles\n")
	rc = (jint)PhRectsToTiles((PhRect_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PhRectsToTiles\n")
	return rc;
}
#endif

#ifndef NO_PhRegionQuery
JNIEXPORT jint JNICALL OS_NATIVE(PhRegionQuery)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jint arg3, jint arg4)
{
	PhRegion_t _arg1, *lparg1=NULL;
	PhRect_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhRegionQuery\n")
	if (arg1) lparg1 = getPhRegion_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPhRect_tFields(env, arg2, &_arg2);
	rc = (jint)PhRegionQuery((PhRid_t)arg0, (PhRegion_t *)lparg1, (PhRect_t *)lparg2, (void *)arg3, arg4);
	if (arg2) setPhRect_tFields(env, arg2, lparg2);
	if (arg1) setPhRegion_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PhRegionQuery\n")
	return rc;
}
#endif

#ifndef NO_PhReleaseImage
JNIEXPORT void JNICALL OS_NATIVE(PhReleaseImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PhReleaseImage\n")
	PhReleaseImage((PhImage_t *)arg0);
	NATIVE_EXIT(env, that, "PhReleaseImage\n")
}
#endif

#ifndef NO_PhSortTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhSortTiles)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhSortTiles\n")
	rc = (jint)PhSortTiles((PhTile_t *)arg0);
	NATIVE_EXIT(env, that, "PhSortTiles\n")
	return rc;
}
#endif

#ifndef NO_PhTilesToRects
JNIEXPORT jint JNICALL OS_NATIVE(PhTilesToRects)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhTilesToRects\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PhTilesToRects((PhTile_t *)arg0, (int *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PhTilesToRects\n")
	return rc;
}
#endif

#ifndef NO_PhTranslateTiles
JNIEXPORT jint JNICALL OS_NATIVE(PhTranslateTiles)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhTranslateTiles\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PhTranslateTiles((PhTile_t *)arg0, (PhPoint_t const *)lparg1);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PhTranslateTiles\n")
	return rc;
}
#endif

#ifndef NO_PhWindowQueryVisible
JNIEXPORT jint JNICALL OS_NATIVE(PhWindowQueryVisible)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	PhRect_t _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PhWindowQueryVisible\n")
	if (arg3) lparg3 = getPhRect_tFields(env, arg3, &_arg3);
	rc = (jint)PhWindowQueryVisible(arg0, (PhRid_t)arg1, arg2, (PhRect_t *)lparg3);
	if (arg3) setPhRect_tFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "PhWindowQueryVisible\n")
	return rc;
}
#endif

#ifndef NO_PiCropImage
JNIEXPORT jint JNICALL OS_NATIVE(PiCropImage)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PiCropImage\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PiCropImage((PhImage_t *)arg0, (PhRect_t const *)lparg1, arg2);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PiCropImage\n")
	return rc;
}
#endif

#ifndef NO_PiDuplicateImage
JNIEXPORT jint JNICALL OS_NATIVE(PiDuplicateImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PiDuplicateImage\n")
	rc = (jint)PiDuplicateImage((PhImage_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PiDuplicateImage\n")
	return rc;
}
#endif

#ifndef NO_PmMemCreateMC
JNIEXPORT jint JNICALL OS_NATIVE(PmMemCreateMC)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhDim_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PmMemCreateMC\n")
	if (arg1) lparg1 = getPhDim_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	rc = (jint)PmMemCreateMC((PhImage_t *)arg0, (PhDim_t *)lparg1, (PhPoint_t *)lparg2);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1) setPhDim_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PmMemCreateMC\n")
	return rc;
}
#endif

#ifndef NO_PmMemFlush
JNIEXPORT jint JNICALL OS_NATIVE(PmMemFlush)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PmMemFlush\n")
	rc = (jint)PmMemFlush((PmMemoryContext_t *)arg0, (PhImage_t *)arg1);
	NATIVE_EXIT(env, that, "PmMemFlush\n")
	return rc;
}
#endif

#ifndef NO_PmMemReleaseMC
JNIEXPORT void JNICALL OS_NATIVE(PmMemReleaseMC)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PmMemReleaseMC\n")
	PmMemReleaseMC((PmMemoryContext_t *)arg0);
	NATIVE_EXIT(env, that, "PmMemReleaseMC\n")
}
#endif

#ifndef NO_PmMemStart
JNIEXPORT jint JNICALL OS_NATIVE(PmMemStart)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PmMemStart\n")
	rc = (jint)PmMemStart((PmMemoryContext_t *)arg0);
	NATIVE_EXIT(env, that, "PmMemStart\n")
	return rc;
}
#endif

#ifndef NO_PmMemStop
JNIEXPORT jint JNICALL OS_NATIVE(PmMemStop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PmMemStop\n")
	rc = (jint)PmMemStop((PmMemoryContext_t *)arg0);
	NATIVE_EXIT(env, that, "PmMemStop\n")
	return rc;
}
#endif

#ifndef NO_PtAddCallback
JNIEXPORT void JNICALL OS_NATIVE(PtAddCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "PtAddCallback\n")
	PtAddCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "PtAddCallback\n")
}
#endif

#ifndef NO_PtAddEventHandler
JNIEXPORT void JNICALL OS_NATIVE(PtAddEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "PtAddEventHandler\n")
	PtAddEventHandler((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "PtAddEventHandler\n")
}
#endif

#ifndef NO_PtAddFilterCallback
JNIEXPORT void JNICALL OS_NATIVE(PtAddFilterCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "PtAddFilterCallback\n")
	PtAddFilterCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "PtAddFilterCallback\n")
}
#endif

#ifndef NO_PtAddHotkeyHandler
JNIEXPORT void JNICALL OS_NATIVE(PtAddHotkeyHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "PtAddHotkeyHandler\n")
	PtAddHotkeyHandler((PtWidget_t *)arg0, arg1, arg2, arg3, (void *)arg4, (PtCallbackF_t *)arg5);
	NATIVE_EXIT(env, that, "PtAddHotkeyHandler\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PtAlert\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)PtAlert((PtWidget_t *)arg0, (PhPoint_t const *)lparg1, (char const *)lparg2, (PhImage_t const *)arg3, (char const *)lparg4, (char const *)lparg5, arg6, (char const **)lparg7, (char const **)lparg8, arg9, arg10, arg11);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtAlert\n")
	return rc;
}
#endif

#ifndef NO_PtAppAddInput
JNIEXPORT jint JNICALL OS_NATIVE(PtAppAddInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtAppAddInput\n")
	rc = (jint)PtAppAddInput((PtAppContext_t)arg0, (pid_t)arg1, (PtInputCallbackProc_t)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "PtAppAddInput\n")
	return rc;
}
#endif

#ifndef NO_PtAppAddWorkProc
JNIEXPORT jint JNICALL OS_NATIVE(PtAppAddWorkProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtAppAddWorkProc\n")
	rc = (jint)PtAppAddWorkProc((PtAppContext_t)arg0, (PtWorkProc_t)arg1, (void *)arg2);
	NATIVE_EXIT(env, that, "PtAppAddWorkProc\n")
	return rc;
}
#endif

#ifndef NO_PtAppCreatePulse
JNIEXPORT jint JNICALL OS_NATIVE(PtAppCreatePulse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtAppCreatePulse\n")
	rc = (jint)PtAppCreatePulse((PtAppContext_t)arg0, arg1);
	NATIVE_EXIT(env, that, "PtAppCreatePulse\n")
	return rc;
}
#endif

#ifndef NO_PtAppDeletePulse
JNIEXPORT jint JNICALL OS_NATIVE(PtAppDeletePulse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtAppDeletePulse\n")
	rc = (jint)PtAppDeletePulse((PtAppContext_t)arg0, (pid_t)arg1);
	NATIVE_EXIT(env, that, "PtAppDeletePulse\n")
	return rc;
}
#endif

#ifndef NO_PtAppProcessEvent
JNIEXPORT void JNICALL OS_NATIVE(PtAppProcessEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PtAppProcessEvent\n")
	PtAppProcessEvent((PtAppContext_t)arg0);
	NATIVE_EXIT(env, that, "PtAppProcessEvent\n")
}
#endif

#ifndef NO_PtAppPulseTrigger
JNIEXPORT jint JNICALL OS_NATIVE(PtAppPulseTrigger)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtAppPulseTrigger\n")
	rc = (jint)PtAppPulseTrigger((PtAppContext_t)arg0, (pid_t)arg1);
	NATIVE_EXIT(env, that, "PtAppPulseTrigger\n")
	return rc;
}
#endif

#ifndef NO_PtAppRemoveInput
JNIEXPORT void JNICALL OS_NATIVE(PtAppRemoveInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtAppRemoveInput\n")
	PtAppRemoveInput((PtAppContext_t)arg0, (PtInputId_t *)arg1);
	NATIVE_EXIT(env, that, "PtAppRemoveInput\n")
}
#endif

#ifndef NO_PtAppRemoveWorkProc
JNIEXPORT void JNICALL OS_NATIVE(PtAppRemoveWorkProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtAppRemoveWorkProc\n")
	PtAppRemoveWorkProc((PtAppContext_t)arg0, (PtWorkProcId_t *)arg1);
	NATIVE_EXIT(env, that, "PtAppRemoveWorkProc\n")
}
#endif

#ifndef NO_PtBeep
JNIEXPORT jint JNICALL OS_NATIVE(PtBeep)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtBeep\n")
	rc = (jint)PtBeep();
	NATIVE_EXIT(env, that, "PtBeep\n")
	return rc;
}
#endif

#ifndef NO_PtBlit
JNIEXPORT jint JNICALL OS_NATIVE(PtBlit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtBlit\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	rc = (jint)PtBlit((PtWidget_t const *)arg0, (PhRect_t const *)lparg1, (PhPoint_t const *)lparg2);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtBlit\n")
	return rc;
}
#endif

#ifndef NO_PtBlockAllWindows
JNIEXPORT jint JNICALL OS_NATIVE(PtBlockAllWindows)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtBlockAllWindows\n")
	rc = (jint)PtBlockAllWindows((PtWidget_t *)arg0, arg1, (PgColor_t)arg2);
	NATIVE_EXIT(env, that, "PtBlockAllWindows\n")
	return rc;
}
#endif

#ifndef NO_PtBlockWindow
JNIEXPORT jint JNICALL OS_NATIVE(PtBlockWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtBlockWindow\n")
	rc = (jint)PtBlockWindow((PtWidget_t *)arg0, arg1, (PgColor_t)arg2);
	NATIVE_EXIT(env, that, "PtBlockWindow\n")
	return rc;
}
#endif

#ifndef NO_PtButton
JNIEXPORT jint JNICALL OS_NATIVE(PtButton)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtButton\n")
	rc = (jint)PtButton;
	NATIVE_EXIT(env, that, "PtButton\n")
	return rc;
}
#endif

#ifndef NO_PtCalcBorder
JNIEXPORT void JNICALL OS_NATIVE(PtCalcBorder)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "PtCalcBorder\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	PtCalcBorder((PtWidget_t *)arg0, lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtCalcBorder\n")
}
#endif

#ifndef NO_PtCalcCanvas
JNIEXPORT jint JNICALL OS_NATIVE(PtCalcCanvas)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtCalcCanvas\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PtCalcCanvas((PtWidget_t *)arg0, (PhRect_t *)lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtCalcCanvas\n")
	return rc;
}
#endif

#ifndef NO_PtClippedBlit
JNIEXPORT jint JNICALL OS_NATIVE(PtClippedBlit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	PhPoint_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtClippedBlit\n")
	if (arg2) lparg2 = getPhPoint_tFields(env, arg2, &_arg2);
	rc = (jint)PtClippedBlit((PtWidget_t const *)arg0, (PhTile_t const *)arg1, (PhPoint_t const *)lparg2, (PhTile_t const *)arg3);
	if (arg2) setPhPoint_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PtClippedBlit\n")
	return rc;
}
#endif

#ifndef NO_PtColorSelect
JNIEXPORT jint JNICALL OS_NATIVE(PtColorSelect)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	PtColorSelectInfo_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtColorSelect\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getPtColorSelectInfo_tFields(env, arg2, &_arg2);
	rc = (jint)PtColorSelect((PtWidget_t *)arg0, (char *)lparg1, lparg2);
	if (arg2) setPtColorSelectInfo_tFields(env, arg2, lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtColorSelect\n")
	return rc;
}
#endif

#ifndef NO_PtComboBox
JNIEXPORT jint JNICALL OS_NATIVE(PtComboBox)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtComboBox\n")
	rc = (jint)PtComboBox;
	NATIVE_EXIT(env, that, "PtComboBox\n")
	return rc;
}
#endif

#ifndef NO_PtContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtContainer)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtContainer\n")
	rc = (jint)PtContainer;
	NATIVE_EXIT(env, that, "PtContainer\n")
	return rc;
}
#endif

#ifndef NO_PtContainerFindFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFindFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerFindFocus\n")
	rc = (jint)PtContainerFindFocus((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtContainerFindFocus\n")
	return rc;
}
#endif

#ifndef NO_PtContainerFocusNext
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFocusNext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerFocusNext\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtContainerFocusNext((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtContainerFocusNext\n")
	return rc;
}
#endif

#ifndef NO_PtContainerFocusPrev
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerFocusPrev)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerFocusPrev\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtContainerFocusPrev((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtContainerFocusPrev\n")
	return rc;
}
#endif

#ifndef NO_PtContainerGiveFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerGiveFocus)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerGiveFocus\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtContainerGiveFocus((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtContainerGiveFocus\n")
	return rc;
}
#endif

#ifndef NO_PtContainerHold
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerHold)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerHold\n")
	rc = (jint)PtContainerHold((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtContainerHold\n")
	return rc;
}
#endif

#ifndef NO_PtContainerRelease
JNIEXPORT jint JNICALL OS_NATIVE(PtContainerRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtContainerRelease\n")
	rc = (jint)PtContainerRelease((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtContainerRelease\n")
	return rc;
}
#endif

#ifndef NO_PtCreateAppContext
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateAppContext)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtCreateAppContext\n")
	rc = (jint)PtCreateAppContext();
	NATIVE_EXIT(env, that, "PtCreateAppContext\n")
	return rc;
}
#endif

#ifndef NO_PtCreateWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtCreateWidget\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)PtCreateWidget((PtWidgetClassRef_t *)arg0, (PtWidget_t *)arg1, arg2, (PtArg_t const *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "PtCreateWidget\n")
	return rc;
}
#endif

#ifndef NO_PtCreateWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(PtCreateWidgetClass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtCreateWidgetClass\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)PtCreateWidgetClass((PtWidgetClassRef_t *)arg0, arg1, arg2, (PtArg_t const *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "PtCreateWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_PtDamageExtent
JNIEXPORT jint JNICALL OS_NATIVE(PtDamageExtent)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtDamageExtent\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PtDamageExtent((PtWidget_t *)arg0, (PhRect_t const *)lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtDamageExtent\n")
	return rc;
}
#endif

#ifndef NO_PtDamageWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtDamageWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtDamageWidget\n")
	rc = (jint)PtDamageWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtDamageWidget\n")
	return rc;
}
#endif

#ifndef NO_PtDestroyWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtDestroyWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtDestroyWidget\n")
	rc = (jint)PtDestroyWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtDestroyWidget\n")
	return rc;
}
#endif

#ifndef NO_PtDisjoint
JNIEXPORT jint JNICALL OS_NATIVE(PtDisjoint)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtDisjoint\n")
	rc = (jint)PtDisjoint;
	NATIVE_EXIT(env, that, "PtDisjoint\n")
	return rc;
}
#endif

#ifndef NO_PtEnter
JNIEXPORT jint JNICALL OS_NATIVE(PtEnter)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtEnter\n")
	rc = (jint)PtEnter(arg0);
	NATIVE_EXIT(env, that, "PtEnter\n")
	return rc;
}
#endif

#ifndef NO_PtEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(PtEventHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtEventHandler\n")
	rc = (jint)PtEventHandler((PhEvent_t *)arg0);
	NATIVE_EXIT(env, that, "PtEventHandler\n")
	return rc;
}
#endif

#ifndef NO_PtExtentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtExtentWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtExtentWidget\n")
	rc = (jint)PtExtentWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtExtentWidget\n")
	return rc;
}
#endif

#ifndef NO_PtExtentWidgetFamily
JNIEXPORT jint JNICALL OS_NATIVE(PtExtentWidgetFamily)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtExtentWidgetFamily\n")
	rc = (jint)PtExtentWidgetFamily((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtExtentWidgetFamily\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PtFileSelection\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetByteArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = getPtFileSelectionInfo_tFields(env, arg8, &_arg8);
	rc = (jint)PtFileSelection((PtWidget_t *)arg0, (PhPoint_t const *)lparg1, (char const *)lparg2, (char const *)lparg3, (char const *)lparg4, (char const *)lparg5, (char const *)lparg6, (char const *)lparg7, (PtFileSelectionInfo_t *)lparg8, arg9);
	if (arg8) setPtFileSelectionInfo_tFields(env, arg8, lparg8);
	if (arg7) (*env)->ReleaseByteArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtFileSelection\n")
	return rc;
}
#endif

#ifndef NO_PtFindDisjoint
JNIEXPORT jint JNICALL OS_NATIVE(PtFindDisjoint)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtFindDisjoint\n")
	rc = (jint)PtFindDisjoint((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtFindDisjoint\n")
	return rc;
}
#endif

#ifndef NO_PtFlush
JNIEXPORT jint JNICALL OS_NATIVE(PtFlush)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtFlush\n")
	rc = (jint)PtFlush();
	NATIVE_EXIT(env, that, "PtFlush\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "PtFontSelection\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)PtFontSelection((PtWidget_t *)arg0, (const PhPoint_t *)lparg1, (const char *)lparg2, (const char *)lparg3, arg4, arg5, (const char *)lparg6);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtFontSelection\n")
	return rc;
}
#endif

#ifndef NO_PtForwardWindowEvent
JNIEXPORT jint JNICALL OS_NATIVE(PtForwardWindowEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PhWindowEvent_t _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtForwardWindowEvent\n")
	if (arg0) lparg0 = getPhWindowEvent_tFields(env, arg0, &_arg0);
	rc = (jint)PtForwardWindowEvent((PhWindowEvent_t const *)lparg0);
	if (arg0) setPhWindowEvent_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PtForwardWindowEvent\n")
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
	NATIVE_ENTER(env, that, "PtFrameSize\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	PtFrameSize(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PtFrameSize\n")
}
#endif

#ifndef NO_PtGetAbsPosition
JNIEXPORT void JNICALL OS_NATIVE(PtGetAbsPosition)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	NATIVE_ENTER(env, that, "PtGetAbsPosition\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	PtGetAbsPosition((PtWidget_t *)arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtGetAbsPosition\n")
}
#endif

#ifndef NO_PtGetResources
JNIEXPORT jint JNICALL OS_NATIVE(PtGetResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtGetResources\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PtGetResources((PtWidget_t *)arg0, arg1, (PtArg_t *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PtGetResources\n")
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusNext
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusNext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtGlobalFocusNext\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtGlobalFocusNext((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtGlobalFocusNext\n")
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusNextContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusNextContainer)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtGlobalFocusNextContainer\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtGlobalFocusNextContainer((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtGlobalFocusNextContainer\n")
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusPrev
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusPrev)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtGlobalFocusPrev\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtGlobalFocusPrev((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtGlobalFocusPrev\n")
	return rc;
}
#endif

#ifndef NO_PtGlobalFocusPrevContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtGlobalFocusPrevContainer)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtGlobalFocusPrevContainer\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	rc = (jint)PtGlobalFocusPrevContainer((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtGlobalFocusPrevContainer\n")
	return rc;
}
#endif

#ifndef NO_PtGroup
JNIEXPORT jint JNICALL OS_NATIVE(PtGroup)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtGroup\n")
	rc = (jint)PtGroup;
	NATIVE_EXIT(env, that, "PtGroup\n")
	return rc;
}
#endif

#ifndef NO_PtHit
JNIEXPORT jint JNICALL OS_NATIVE(PtHit)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PhRect_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtHit\n")
	if (arg2) lparg2 = getPhRect_tFields(env, arg2, &_arg2);
	rc = (jint)PtHit(( PtWidget_t *)arg0, arg1, (PhRect_t const *)lparg2);
	if (arg2) setPhRect_tFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PtHit\n")
	return rc;
}
#endif

#ifndef NO_PtHold
JNIEXPORT jint JNICALL OS_NATIVE(PtHold)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtHold\n")
	rc = (jint)PtHold();
	NATIVE_EXIT(env, that, "PtHold\n")
	return rc;
}
#endif

#ifndef NO_PtInflateBalloon
JNIEXPORT jint JNICALL OS_NATIVE(PtInflateBalloon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtInflateBalloon\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)PtInflateBalloon((PtWidget_t *)arg0, (PtWidget_t *)arg1, arg2, (char const *)lparg3, (char const *)lparg4, (PgColor_t)arg5, (PgColor_t)arg6);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "PtInflateBalloon\n")
	return rc;
}
#endif

#ifndef NO_PtInit
JNIEXPORT jint JNICALL OS_NATIVE(PtInit)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtInit\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)PtInit((char const *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PtInit\n")
	return rc;
}
#endif

#ifndef NO_PtIsFocused
JNIEXPORT jint JNICALL OS_NATIVE(PtIsFocused)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtIsFocused\n")
	rc = (jint)PtIsFocused((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtIsFocused\n")
	return rc;
}
#endif

#ifndef NO_PtLabel
JNIEXPORT jint JNICALL OS_NATIVE(PtLabel)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtLabel\n")
	rc = (jint)PtLabel;
	NATIVE_EXIT(env, that, "PtLabel\n")
	return rc;
}
#endif

#ifndef NO_PtLeave
JNIEXPORT jint JNICALL OS_NATIVE(PtLeave)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtLeave\n")
	rc = (jint)PtLeave(arg0);
	NATIVE_EXIT(env, that, "PtLeave\n")
	return rc;
}
#endif

#ifndef NO_PtList
JNIEXPORT jint JNICALL OS_NATIVE(PtList)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtList\n")
	rc = (jint)PtList;
	NATIVE_EXIT(env, that, "PtList\n")
	return rc;
}
#endif

#ifndef NO_PtListAddItems
JNIEXPORT jint JNICALL OS_NATIVE(PtListAddItems)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtListAddItems\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PtListAddItems((PtWidget_t *)arg0, (const char **)lparg1, arg2, arg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtListAddItems\n")
	return rc;
}
#endif

#ifndef NO_PtListDeleteAllItems
JNIEXPORT jint JNICALL OS_NATIVE(PtListDeleteAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtListDeleteAllItems\n")
	rc = (jint)PtListDeleteAllItems((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtListDeleteAllItems\n")
	return rc;
}
#endif

#ifndef NO_PtListDeleteItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListDeleteItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtListDeleteItemPos\n")
	rc = (jint)PtListDeleteItemPos((PtWidget_t *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "PtListDeleteItemPos\n")
	return rc;
}
#endif

#ifndef NO_PtListGotoPos
JNIEXPORT void JNICALL OS_NATIVE(PtListGotoPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtListGotoPos\n")
	PtListGotoPos((PtWidget_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PtListGotoPos\n")
}
#endif

#ifndef NO_PtListItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListItemPos)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtListItemPos\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)PtListItemPos((PtWidget_t *)arg0, (const char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtListItemPos\n")
	return rc;
}
#endif

#ifndef NO_PtListReplaceItemPos
JNIEXPORT jint JNICALL OS_NATIVE(PtListReplaceItemPos)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtListReplaceItemPos\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PtListReplaceItemPos((PtWidget_t *)arg0, (const char **)lparg1, arg2, arg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtListReplaceItemPos\n")
	return rc;
}
#endif

#ifndef NO_PtListSelectPos
JNIEXPORT void JNICALL OS_NATIVE(PtListSelectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtListSelectPos\n")
	PtListSelectPos((PtWidget_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PtListSelectPos\n")
}
#endif

#ifndef NO_PtListUnselectPos
JNIEXPORT void JNICALL OS_NATIVE(PtListUnselectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtListUnselectPos\n")
	PtListUnselectPos((PtWidget_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PtListUnselectPos\n")
}
#endif

#ifndef NO_PtMainLoop
JNIEXPORT void JNICALL OS_NATIVE(PtMainLoop)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "PtMainLoop\n")
	PtMainLoop();
	NATIVE_EXIT(env, that, "PtMainLoop\n")
}
#endif

#ifndef NO_PtMenu
JNIEXPORT jint JNICALL OS_NATIVE(PtMenu)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtMenu\n")
	rc = (jint)PtMenu;
	NATIVE_EXIT(env, that, "PtMenu\n")
	return rc;
}
#endif

#ifndef NO_PtMenuBar
JNIEXPORT jint JNICALL OS_NATIVE(PtMenuBar)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtMenuBar\n")
	rc = (jint)PtMenuBar;
	NATIVE_EXIT(env, that, "PtMenuBar\n")
	return rc;
}
#endif

#ifndef NO_PtMenuButton
JNIEXPORT jint JNICALL OS_NATIVE(PtMenuButton)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtMenuButton\n")
	rc = (jint)PtMenuButton;
	NATIVE_EXIT(env, that, "PtMenuButton\n")
	return rc;
}
#endif

#ifndef NO_PtMultiText
JNIEXPORT jint JNICALL OS_NATIVE(PtMultiText)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtMultiText\n")
	rc = (jint)PtMultiText;
	NATIVE_EXIT(env, that, "PtMultiText\n")
	return rc;
}
#endif

#ifndef NO_PtNextTopLevelWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtNextTopLevelWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtNextTopLevelWidget\n")
	rc = (jint)PtNextTopLevelWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtNextTopLevelWidget\n")
	return rc;
}
#endif

#ifndef NO_PtPane
JNIEXPORT jint JNICALL OS_NATIVE(PtPane)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtPane\n")
	rc = (jint)PtPane;
	NATIVE_EXIT(env, that, "PtPane\n")
	return rc;
}
#endif

#ifndef NO_PtPanelGroup
JNIEXPORT jint JNICALL OS_NATIVE(PtPanelGroup)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtPanelGroup\n")
	rc = (jint)PtPanelGroup;
	NATIVE_EXIT(env, that, "PtPanelGroup\n")
	return rc;
}
#endif

#ifndef NO_PtPositionMenu
JNIEXPORT void JNICALL OS_NATIVE(PtPositionMenu)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhEvent_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "PtPositionMenu\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	PtPositionMenu((PtWidget_t *)arg0, (PhEvent_t *)lparg1);
	if (arg1) setPhEvent_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtPositionMenu\n")
}
#endif

#ifndef NO_PtProgress
JNIEXPORT jint JNICALL OS_NATIVE(PtProgress)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtProgress\n")
	rc = (jint)PtProgress;
	NATIVE_EXIT(env, that, "PtProgress\n")
	return rc;
}
#endif

#ifndef NO_PtReParentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtReParentWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtReParentWidget\n")
	rc = (jint)PtReParentWidget((PtWidget_t *)arg0, (PtWidget_t *)arg1);
	NATIVE_EXIT(env, that, "PtReParentWidget\n")
	return rc;
}
#endif

#ifndef NO_PtRealizeWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtRealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtRealizeWidget\n")
	rc = (jint)PtRealizeWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtRealizeWidget\n")
	return rc;
}
#endif

#ifndef NO_PtRegion
JNIEXPORT jint JNICALL OS_NATIVE(PtRegion)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtRegion\n")
	rc = (jint)PtRegion;
	NATIVE_EXIT(env, that, "PtRegion\n")
	return rc;
}
#endif

#ifndef NO_PtRelease
JNIEXPORT jint JNICALL OS_NATIVE(PtRelease)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtRelease\n")
	rc = (jint)PtRelease();
	NATIVE_EXIT(env, that, "PtRelease\n")
	return rc;
}
#endif

#ifndef NO_PtRemoveCallback
JNIEXPORT void JNICALL OS_NATIVE(PtRemoveCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "PtRemoveCallback\n")
	PtRemoveCallback((PtWidget_t *)arg0, arg1, (PtCallbackF_t *)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "PtRemoveCallback\n")
}
#endif

#ifndef NO_PtRemoveHotkeyHandler
JNIEXPORT void JNICALL OS_NATIVE(PtRemoveHotkeyHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "PtRemoveHotkeyHandler\n")
	PtRemoveHotkeyHandler((PtWidget_t *)arg0, arg1, arg2, arg3, (void *)arg4, (PtCallbackF_t *)arg5);
	NATIVE_EXIT(env, that, "PtRemoveHotkeyHandler\n")
}
#endif

#ifndef NO_PtScrollArea
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollArea)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtScrollArea\n")
	rc = (jint)PtScrollArea;
	NATIVE_EXIT(env, that, "PtScrollArea\n")
	return rc;
}
#endif

#ifndef NO_PtScrollContainer
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollContainer)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtScrollContainer\n")
	rc = (jint)PtScrollContainer;
	NATIVE_EXIT(env, that, "PtScrollContainer\n")
	return rc;
}
#endif

#ifndef NO_PtScrollbar
JNIEXPORT jint JNICALL OS_NATIVE(PtScrollbar)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtScrollbar\n")
	rc = (jint)PtScrollbar;
	NATIVE_EXIT(env, that, "PtScrollbar\n")
	return rc;
}
#endif

#ifndef NO_PtSendEventToWidget
JNIEXPORT void JNICALL OS_NATIVE(PtSendEventToWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "PtSendEventToWidget\n")
	PtSendEventToWidget((PtWidget_t *)arg0, (PhEvent_t *)arg1);
	NATIVE_EXIT(env, that, "PtSendEventToWidget\n")
}
#endif

#ifndef NO_PtSeparator
JNIEXPORT jint JNICALL OS_NATIVE(PtSeparator)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtSeparator\n")
	rc = (jint)PtSeparator;
	NATIVE_EXIT(env, that, "PtSeparator\n")
	return rc;
}
#endif

#ifndef NO_PtSetAreaFromWidgetCanvas
JNIEXPORT jint JNICALL OS_NATIVE(PtSetAreaFromWidgetCanvas)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	PhArea_t _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtSetAreaFromWidgetCanvas\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPhArea_tFields(env, arg2, &_arg2);
	rc = (jint)PtSetAreaFromWidgetCanvas((PtWidget_t *)arg0, lparg1, lparg2);
	if (arg2) setPhArea_tFields(env, arg2, lparg2);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtSetAreaFromWidgetCanvas\n")
	return rc;
}
#endif

#ifndef NO_PtSetParentWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtSetParentWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtSetParentWidget\n")
	rc = (jint)PtSetParentWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtSetParentWidget\n")
	return rc;
}
#endif

#ifndef NO_PtSetResource
JNIEXPORT jint JNICALL OS_NATIVE(PtSetResource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtSetResource\n")
	rc = (jint)PtSetResource((PtWidget_t *)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "PtSetResource\n")
	return rc;
}
#endif

#ifndef NO_PtSetResources
JNIEXPORT jint JNICALL OS_NATIVE(PtSetResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtSetResources\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PtSetResources((PtWidget_t *)arg0, arg1, (PtArg_t *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PtSetResources\n")
	return rc;
}
#endif

#ifndef NO_PtSlider
JNIEXPORT jint JNICALL OS_NATIVE(PtSlider)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtSlider\n")
	rc = (jint)PtSlider;
	NATIVE_EXIT(env, that, "PtSlider\n")
	return rc;
}
#endif

#ifndef NO_PtSuperClassDraw
JNIEXPORT void JNICALL OS_NATIVE(PtSuperClassDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "PtSuperClassDraw\n")
	PtSuperClassDraw((PtWidgetClassRef_t *)arg0, (PtWidget_t *)arg1, (PhTile_t const *)arg2);
	NATIVE_EXIT(env, that, "PtSuperClassDraw\n")
}
#endif

#ifndef NO_PtSyncWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtSyncWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtSyncWidget\n")
	rc = (jint)PtSyncWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtSyncWidget\n")
	return rc;
}
#endif

#ifndef NO_PtText
JNIEXPORT jint JNICALL OS_NATIVE(PtText)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtText\n")
	rc = (jint)PtText;
	NATIVE_EXIT(env, that, "PtText\n")
	return rc;
}
#endif

#ifndef NO_PtTextGetSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtTextGetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtTextGetSelection\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PtTextGetSelection((PtWidget_t *)arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtTextGetSelection\n")
	return rc;
}
#endif

#ifndef NO_PtTextModifyText__IIIIII
JNIEXPORT jint JNICALL OS_NATIVE(PtTextModifyText__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtTextModifyText__IIIIII\n")
	rc = (jint)PtTextModifyText((PtWidget_t *)arg0, arg1, arg2, arg3, (char const *)arg4, arg5);
	NATIVE_EXIT(env, that, "PtTextModifyText__IIIIII\n")
	return rc;
}
#endif

#ifndef NO_PtTextModifyText__IIII_3BI
JNIEXPORT jint JNICALL OS_NATIVE(PtTextModifyText__IIII_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtTextModifyText__IIII_3BI\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)PtTextModifyText((PtWidget_t *)arg0, arg1, arg2, arg3, (char const *)lparg4, arg5);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "PtTextModifyText__IIII_3BI\n")
	return rc;
}
#endif

#ifndef NO_PtTextSetSelection
JNIEXPORT jint JNICALL OS_NATIVE(PtTextSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtTextSetSelection\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PtTextSetSelection((PtWidget_t *)arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PtTextSetSelection\n")
	return rc;
}
#endif

#ifndef NO_PtTimer
JNIEXPORT jint JNICALL OS_NATIVE(PtTimer)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtTimer\n")
	rc = (jint)PtTimer;
	NATIVE_EXIT(env, that, "PtTimer\n")
	return rc;
}
#endif

#ifndef NO_PtToggleButton
JNIEXPORT jint JNICALL OS_NATIVE(PtToggleButton)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtToggleButton\n")
	rc = (jint)PtToggleButton;
	NATIVE_EXIT(env, that, "PtToggleButton\n")
	return rc;
}
#endif

#ifndef NO_PtToolbar
JNIEXPORT jint JNICALL OS_NATIVE(PtToolbar)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtToolbar\n")
	rc = (jint)PtToolbar;
	NATIVE_EXIT(env, that, "PtToolbar\n")
	return rc;
}
#endif

#ifndef NO_PtUnblockWindows
JNIEXPORT void JNICALL OS_NATIVE(PtUnblockWindows)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PtUnblockWindows\n")
	PtUnblockWindows((PtBlockedList_t *)arg0);
	NATIVE_EXIT(env, that, "PtUnblockWindows\n")
}
#endif

#ifndef NO_PtUnrealizeWidget
JNIEXPORT jint JNICALL OS_NATIVE(PtUnrealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtUnrealizeWidget\n")
	rc = (jint)PtUnrealizeWidget((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtUnrealizeWidget\n")
	return rc;
}
#endif

#ifndef NO_PtValidParent
JNIEXPORT jint JNICALL OS_NATIVE(PtValidParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtValidParent\n")
	rc = (jint)PtValidParent((PtWidget_t *)arg0, (PtWidgetClassRef_t *)arg1);
	NATIVE_EXIT(env, that, "PtValidParent\n")
	return rc;
}
#endif

#ifndef NO_PtWebClient
JNIEXPORT jint JNICALL OS_NATIVE(PtWebClient)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWebClient\n")
	rc = (jint)PtWebClient;
	NATIVE_EXIT(env, that, "PtWebClient\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetArea
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetArea)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhArea_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetArea\n")
	if (arg1) lparg1 = getPhArea_tFields(env, arg1, &_arg1);
	rc = (jint)PtWidgetArea((PtWidget_t *)arg0, (PhArea_t *)lparg1);
	if (arg1) setPhArea_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtWidgetArea\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetBrotherBehind
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetBrotherBehind)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetBrotherBehind\n")
	rc = (jint)PtWidgetBrotherBehind((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetBrotherBehind\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetBrotherInFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetBrotherInFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetBrotherInFront\n")
	rc = (jint)PtWidgetBrotherInFront((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetBrotherInFront\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetCanvas__II
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetCanvas__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetCanvas__II\n")
	rc = (jint)PtWidgetCanvas((PtWidget_t *)arg0, (PhRect_t *)arg1);
	NATIVE_EXIT(env, that, "PtWidgetCanvas__II\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PtWidgetCanvas((PtWidget_t *)arg0, lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetChildBack
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetChildBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetChildBack\n")
	rc = (jint)PtWidgetChildBack((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetChildBack\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetChildFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetChildFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetChildFront\n")
	rc = (jint)PtWidgetChildFront((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetChildFront\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetClass\n")
	rc = (jint)PtWidgetClass((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetExtent__II
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetExtent__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetExtent__II\n")
	rc = (jint)PtWidgetExtent((PtWidget_t *)arg0, arg1);
	NATIVE_EXIT(env, that, "PtWidgetExtent__II\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhRect_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	rc = (jint)PtWidgetExtent((PtWidget_t *)arg0, lparg1);
	if (arg1) setPhRect_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetFlags
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetFlags\n")
	rc = (jint)PtWidgetFlags((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetFlags\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetInsert
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetInsert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetInsert\n")
	rc = (jint)PtWidgetInsert((PtWidget_t *)arg0, (PtWidget_t *)arg1, arg2);
	NATIVE_EXIT(env, that, "PtWidgetInsert\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetIsClassMember
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetIsClassMember)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetIsClassMember\n")
	rc = (jint)PtWidgetIsClassMember((PtWidget_t *)arg0, (PtWidgetClassRef_t *)arg1);
	NATIVE_EXIT(env, that, "PtWidgetIsClassMember\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetIsRealized
JNIEXPORT jboolean JNICALL OS_NATIVE(PtWidgetIsRealized)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PtWidgetIsRealized\n")
	rc = (jboolean)PtWidgetIsRealized((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetIsRealized\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetOffset
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetOffset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhPoint_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetOffset\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	rc = (jint)PtWidgetOffset((PtWidget_t *)arg0, (PhPoint_t *)lparg1);
	if (arg1) setPhPoint_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtWidgetOffset\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetParent
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetParent\n")
	rc = (jint)PtWidgetParent((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetParent\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetPreferredSize
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetPreferredSize)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PhDim_t _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetPreferredSize\n")
	if (arg1) lparg1 = getPhDim_tFields(env, arg1, &_arg1);
	rc = (jint)PtWidgetPreferredSize((PtWidget_t *)arg0, (PhDim_t *)lparg1);
	if (arg1) setPhDim_tFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PtWidgetPreferredSize\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetRid
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetRid)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetRid\n")
	rc = (jint)PtWidgetRid((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetRid\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetToBack
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetToBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetToBack\n")
	rc = (jint)PtWidgetToBack((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetToBack\n")
	return rc;
}
#endif

#ifndef NO_PtWidgetToFront
JNIEXPORT jint JNICALL OS_NATIVE(PtWidgetToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWidgetToFront\n")
	rc = (jint)PtWidgetToFront((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWidgetToFront\n")
	return rc;
}
#endif

#ifndef NO_PtWindow
JNIEXPORT jint JNICALL OS_NATIVE(PtWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWindow\n")
	rc = (jint)PtWindow;
	NATIVE_EXIT(env, that, "PtWindow\n")
	return rc;
}
#endif

#ifndef NO_PtWindowFocus
JNIEXPORT jint JNICALL OS_NATIVE(PtWindowFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWindowFocus\n")
	rc = (jint)PtWindowFocus((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWindowFocus\n")
	return rc;
}
#endif

#ifndef NO_PtWindowGetState
JNIEXPORT jint JNICALL OS_NATIVE(PtWindowGetState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PtWindowGetState\n")
	rc = (jint)PtWindowGetState((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWindowGetState\n")
	return rc;
}
#endif

#ifndef NO_PtWindowToBack
JNIEXPORT void JNICALL OS_NATIVE(PtWindowToBack)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PtWindowToBack\n")
	PtWindowToBack((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWindowToBack\n")
}
#endif

#ifndef NO_PtWindowToFront
JNIEXPORT void JNICALL OS_NATIVE(PtWindowToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PtWindowToFront\n")
	PtWindowToFront((PtWidget_t *)arg0);
	NATIVE_EXIT(env, that, "PtWindowToFront\n")
}
#endif

#ifndef NO_free
JNIEXPORT void JNICALL OS_NATIVE(free)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "free\n")
	free((void *)arg0);
	NATIVE_EXIT(env, that, "free\n")
}
#endif

#ifndef NO_getenv
JNIEXPORT jint JNICALL OS_NATIVE(getenv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "getenv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)getenv((const char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "getenv\n")
	return rc;
}
#endif

#ifndef NO_malloc
JNIEXPORT jint JNICALL OS_NATIVE(malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "malloc\n")
	rc = (jint)malloc((size_t)arg0);
	NATIVE_EXIT(env, that, "malloc\n")
	return rc;
}
#endif

#ifndef NO_memmove__III
JNIEXPORT void JNICALL OS_NATIVE(memmove__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "memmove__III\n")
	memmove((void *)arg0, (const void *)arg1, arg2);
	NATIVE_EXIT(env, that, "memmove__III\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PgAlpha_t _arg1={0}, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I\n")
	if (arg1) lparg1 = getPgAlpha_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhArea_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I\n")
	if (arg1) lparg1 = getPhArea_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhCursorDef_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I\n")
	if (arg1) lparg1 = getPhCursorDef_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhEvent_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I\n")
	if (arg1) lparg1 = getPhEvent_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhImage_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I\n")
	if (arg1) lparg1 = getPhImage_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhPoint_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I\n")
	if (arg1) lparg1 = getPhPoint_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhPointerEvent_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I\n")
	if (arg1) lparg1 = getPhPointerEvent_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhRect_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I\n")
	if (arg1) lparg1 = getPhRect_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PhTile_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I\n")
	if (arg1) lparg1 = getPhTile_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PtTextCallback_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I\n")
	if (arg1) lparg1 = getPtTextCallback_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_photon_PtWebClientData_1t_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_photon_PtWebClientData_1t_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PtWebClientData_t _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PtWebClientData_1t_2I\n")
	if (arg1) lparg1 = getPtWebClientData_tFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_photon_PtWebClientData_1t_2I\n")
}
#endif

#ifndef NO_memmove__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3BI\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3BI\n")
}
#endif

#ifndef NO_memmove__I_3II
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3II\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FontDetails _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setFontDetailsFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PgAlpha_t _arg0={0}, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPgAlpha_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PgMap_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPgMap_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhClipHeader _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhClipHeaderFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhEvent_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhEvent_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhImage_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhImage_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhKeyEvent_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhKeyEvent_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhPointerEvent_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhPointerEvent_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhRect_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhRect_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhTile_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhTile_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PhWindowEvent_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPhWindowEvent_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtCallbackInfo_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPtCallbackInfo_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtScrollbarCallback_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPtScrollbarCallback_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtTextCallback_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPtTextCallback_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebDataReqCallback_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II\n")
	if (arg0) lparg0 = getPtWebDataReqCallback_tFields(env, arg0, &_arg0);
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPtWebDataReqCallback_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtWebDataReqCallback_1t_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PtWebStatusCallback_t _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II\n")
	if (arg0) lparg0 = getPtWebStatusCallback_tFields(env, arg0, &_arg0);
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) setPtWebStatusCallback_tFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_photon_PtWebStatusCallback_1t_2II\n")
}
#endif

#ifndef NO_memmove___3BII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3BII\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3BII\n")
}
#endif

#ifndef NO_memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	PhClipHeader _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = getPhClipHeaderFields(env, arg1, &_arg1);
	memmove((void *)lparg0, (const void *)lparg1, arg2);
	if (arg1) setPhClipHeaderFields(env, arg1, lparg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I\n")
}
#endif

#ifndef NO_memmove___3III
JNIEXPORT void JNICALL OS_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3III\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3III\n")
}
#endif

#ifndef NO_memmove___3SII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3SII)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3SII\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3SII\n")
}
#endif

#ifndef NO_memset
JNIEXPORT void JNICALL OS_NATIVE(memset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "memset\n")
	memset((void *)arg0, arg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memset\n")
}
#endif

#ifndef NO_strdup
JNIEXPORT jint JNICALL OS_NATIVE(strdup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "strdup\n")
	rc = (jint)strdup((const char *)arg0);
	NATIVE_EXIT(env, that, "strdup\n")
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL OS_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "strlen\n")
	rc = (jint)strlen((const char*)arg0);
	NATIVE_EXIT(env, that, "strlen\n")
	return rc;
}
#endif

#ifndef NO_uname
JNIEXPORT jint JNICALL OS_NATIVE(uname)
	(JNIEnv *env, jclass that, jobject arg0)
{
	utsname _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "uname\n")
	if (arg0) lparg0 = getutsnameFields(env, arg0, &_arg0);
	rc = (jint)uname((utsname *)lparg0);
	if (arg0) setutsnameFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "uname\n")
	return rc;
}
#endif


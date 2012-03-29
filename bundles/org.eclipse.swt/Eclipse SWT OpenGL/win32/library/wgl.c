/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "wgl_structs.h"
#include "wgl_stats.h"

#ifndef WGL_NATIVE
#define WGL_NATIVE(func) Java_org_eclipse_swt_internal_opengl_win32_WGL_##func
#endif

#ifndef NO_ChoosePixelFormat
JNIEXPORT jint JNICALL WGL_NATIVE(ChoosePixelFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	PIXELFORMATDESCRIPTOR _arg1, *lparg1=NULL;
	jint rc = 0;
	WGL_NATIVE_ENTER(env, that, ChoosePixelFormat_FUNC);
	if (arg1) if ((lparg1 = getPIXELFORMATDESCRIPTORFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)ChoosePixelFormat((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPIXELFORMATDESCRIPTORFields(env, arg1, lparg1);
	WGL_NATIVE_EXIT(env, that, ChoosePixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_DescribePixelFormat
JNIEXPORT jint JNICALL WGL_NATIVE(DescribePixelFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	PIXELFORMATDESCRIPTOR _arg3, *lparg3=NULL;
	jint rc = 0;
	WGL_NATIVE_ENTER(env, that, DescribePixelFormat_FUNC);
	if (arg3) if ((lparg3 = getPIXELFORMATDESCRIPTORFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)DescribePixelFormat((HDC)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setPIXELFORMATDESCRIPTORFields(env, arg3, lparg3);
	WGL_NATIVE_EXIT(env, that, DescribePixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPixelFormat
JNIEXPORT jint JNICALL WGL_NATIVE(GetPixelFormat)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WGL_NATIVE_ENTER(env, that, GetPixelFormat_FUNC);
	rc = (jint)GetPixelFormat((HDC)arg0);
	WGL_NATIVE_EXIT(env, that, GetPixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPixelFormat
JNIEXPORT jboolean JNICALL WGL_NATIVE(SetPixelFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	PIXELFORMATDESCRIPTOR _arg2, *lparg2=NULL;
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, SetPixelFormat_FUNC);
	if (arg2) if ((lparg2 = getPIXELFORMATDESCRIPTORFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SetPixelFormat((HDC)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setPIXELFORMATDESCRIPTORFields(env, arg2, lparg2);
	WGL_NATIVE_EXIT(env, that, SetPixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_SwapBuffers
JNIEXPORT jboolean JNICALL WGL_NATIVE(SwapBuffers)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, SwapBuffers_FUNC);
	rc = (jboolean)SwapBuffers((HDC)arg0);
	WGL_NATIVE_EXIT(env, that, SwapBuffers_FUNC);
	return rc;
}
#endif

#ifndef NO_wglCopyContext
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglCopyContext)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglCopyContext_FUNC);
	rc = (jboolean)wglCopyContext((HGLRC)arg0, (HGLRC)arg1, arg2);
	WGL_NATIVE_EXIT(env, that, wglCopyContext_FUNC);
	return rc;
}
#endif

#ifndef NO_wglCreateContext
JNIEXPORT jintLong JNICALL WGL_NATIVE(wglCreateContext)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WGL_NATIVE_ENTER(env, that, wglCreateContext_FUNC);
	rc = (jintLong)wglCreateContext((HDC)arg0);
	WGL_NATIVE_EXIT(env, that, wglCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_wglCreateLayerContext
JNIEXPORT jintLong JNICALL WGL_NATIVE(wglCreateLayerContext)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	WGL_NATIVE_ENTER(env, that, wglCreateLayerContext_FUNC);
	rc = (jintLong)wglCreateLayerContext((HDC)arg0, arg1);
	WGL_NATIVE_EXIT(env, that, wglCreateLayerContext_FUNC);
	return rc;
}
#endif

#ifndef NO_wglDeleteContext
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglDeleteContext)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglDeleteContext_FUNC);
	rc = (jboolean)wglDeleteContext((HGLRC)arg0);
	WGL_NATIVE_EXIT(env, that, wglDeleteContext_FUNC);
	return rc;
}
#endif

#ifndef NO_wglDescribeLayerPlane
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglDescribeLayerPlane)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	LAYERPLANEDESCRIPTOR _arg4, *lparg4=NULL;
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglDescribeLayerPlane_FUNC);
	if (arg4) if ((lparg4 = getLAYERPLANEDESCRIPTORFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jboolean)wglDescribeLayerPlane((HDC)arg0, arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) setLAYERPLANEDESCRIPTORFields(env, arg4, lparg4);
	WGL_NATIVE_EXIT(env, that, wglDescribeLayerPlane_FUNC);
	return rc;
}
#endif

#ifndef NO_wglGetCurrentContext
JNIEXPORT jintLong JNICALL WGL_NATIVE(wglGetCurrentContext)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WGL_NATIVE_ENTER(env, that, wglGetCurrentContext_FUNC);
	rc = (jintLong)wglGetCurrentContext();
	WGL_NATIVE_EXIT(env, that, wglGetCurrentContext_FUNC);
	return rc;
}
#endif

#ifndef NO_wglGetCurrentDC
JNIEXPORT jintLong JNICALL WGL_NATIVE(wglGetCurrentDC)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WGL_NATIVE_ENTER(env, that, wglGetCurrentDC_FUNC);
	rc = (jintLong)wglGetCurrentDC();
	WGL_NATIVE_EXIT(env, that, wglGetCurrentDC_FUNC);
	return rc;
}
#endif

#ifndef NO_wglGetLayerPaletteEntries
JNIEXPORT jint JNICALL WGL_NATIVE(wglGetLayerPaletteEntries)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	WGL_NATIVE_ENTER(env, that, wglGetLayerPaletteEntries_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)wglGetLayerPaletteEntries((HDC)arg0, arg1, arg2, arg3, (COLORREF *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	WGL_NATIVE_EXIT(env, that, wglGetLayerPaletteEntries_FUNC);
	return rc;
}
#endif

#ifndef NO_wglGetProcAddress
JNIEXPORT jintLong JNICALL WGL_NATIVE(wglGetProcAddress)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WGL_NATIVE_ENTER(env, that, wglGetProcAddress_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)wglGetProcAddress(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	WGL_NATIVE_EXIT(env, that, wglGetProcAddress_FUNC);
	return rc;
}
#endif

#ifndef NO_wglMakeCurrent
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglMakeCurrent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglMakeCurrent_FUNC);
	rc = (jboolean)wglMakeCurrent((HDC)arg0, (HGLRC)arg1);
	WGL_NATIVE_EXIT(env, that, wglMakeCurrent_FUNC);
	return rc;
}
#endif

#ifndef NO_wglRealizeLayerPalette
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglRealizeLayerPalette)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglRealizeLayerPalette_FUNC);
	rc = (jboolean)wglRealizeLayerPalette((HDC)arg0, arg1, arg2);
	WGL_NATIVE_EXIT(env, that, wglRealizeLayerPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_wglSetLayerPaletteEntries
JNIEXPORT jint JNICALL WGL_NATIVE(wglSetLayerPaletteEntries)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	WGL_NATIVE_ENTER(env, that, wglSetLayerPaletteEntries_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)wglSetLayerPaletteEntries((HDC)arg0, arg1, arg2, arg3, (COLORREF *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	WGL_NATIVE_EXIT(env, that, wglSetLayerPaletteEntries_FUNC);
	return rc;
}
#endif

#ifndef NO_wglShareLists
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglShareLists)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglShareLists_FUNC);
	rc = (jboolean)wglShareLists((HGLRC)arg0, (HGLRC)arg1);
	WGL_NATIVE_EXIT(env, that, wglShareLists_FUNC);
	return rc;
}
#endif

#ifndef NO_wglSwapLayerBuffers
JNIEXPORT jboolean JNICALL WGL_NATIVE(wglSwapLayerBuffers)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	WGL_NATIVE_ENTER(env, that, wglSwapLayerBuffers_FUNC);
	rc = (jboolean)wglSwapLayerBuffers((HDC)arg0, arg1);
	WGL_NATIVE_EXIT(env, that, wglSwapLayerBuffers_FUNC);
	return rc;
}
#endif


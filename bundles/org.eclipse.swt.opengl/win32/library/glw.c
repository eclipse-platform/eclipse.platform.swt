/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
#include <windows.h>
#include <wingdi.h>
#include "jni.h"
#include "swt.h"
#include "structs.h"

#define WGL_NATIVE(func) Java_org_eclipse_swt_opengl_internal_win32_WGL_##func

JNIEXPORT jint JNICALL WGL_NATIVE(ChoosePixelFormat)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PIXELFORMATDESCRIPTOR _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("ChoosePixelFormat\n")

	if (arg1) lparg1 = getPIXELFORMATDESCRIPTORFields(env, arg1, &_arg1);
	rc = (jint)ChoosePixelFormat((HDC)arg0, (CONST PIXELFORMATDESCRIPTOR *)lparg1);
	if (arg1) setPIXELFORMATDESCRIPTORFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT jint JNICALL WGL_NATIVE(DescribePixelFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	PIXELFORMATDESCRIPTOR _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DescribePixelFormat\n")

	if (arg3) lparg3 = getPIXELFORMATDESCRIPTORFields(env, arg3, &_arg3);
	rc = (jint)DescribePixelFormat((HDC)arg0, arg1, arg2, (LPPIXELFORMATDESCRIPTOR)lparg3);
	if (arg3) setPIXELFORMATDESCRIPTORFields(env, arg3, lparg3);
	return rc;
}

JNIEXPORT jint JNICALL WGL_NATIVE(GetPixelFormat)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetPixelFormat\n")

	return (jint)GetPixelFormat((HDC)arg0);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(SetPixelFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PIXELFORMATDESCRIPTOR _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SetPixelFormat\n")

	if (arg2) lparg2 = getPIXELFORMATDESCRIPTORFields(env, arg2, &_arg2);
	rc = (jboolean)SetPixelFormat((HDC)arg0, arg1, (CONST PIXELFORMATDESCRIPTOR *)lparg2);
	if (arg2) setPIXELFORMATDESCRIPTORFields(env, arg2, lparg2);
	return rc;
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(SwapBuffers)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SwapBuffers\n")

	return (jboolean)SwapBuffers((HDC)arg0);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglCopyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("wglCopyContext\n")

	return (jboolean)wglCopyContext((HGLRC)arg0, (HGLRC)arg1, (UINT)arg2);
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglCreateContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("wglCreateContext\n")

	return (jint)wglCreateContext((HDC)arg0);
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglCreateLayerContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("wglCreateLayerContext\n")

	return (jint)wglCreateLayerContext((HDC)arg0, arg1);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglDeleteContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("wglDeleteContext\n")

	return (jboolean)wglDeleteContext((HGLRC)arg0);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglDescribeLayerPlane)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	LAYERPLANEDESCRIPTOR _arg4, *lparg4=NULL;
	jboolean rc;

	DEBUG_CALL("wglDescribeLayerPlane\n")

	if (arg4) lparg4 = getLAYERPLANEDESCRIPTORFields(env, arg4, &_arg4);
	rc = (jboolean)wglDescribeLayerPlane((HDC)arg0, arg1, arg2, arg3, (LPLAYERPLANEDESCRIPTOR)lparg4);
	if (arg4) setLAYERPLANEDESCRIPTORFields(env, arg4, lparg4);
	return rc;
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglGetCurrentContext)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("wglGetCurrentContext\n")

	return (jint)wglGetCurrentContext();
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglGetCurrentDC)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("wglGetCurrentDC\n")

	return (jint)wglGetCurrentDC();
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglGetLayerPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("wglGetLayerPaletteEntries\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)wglGetLayerPaletteEntries((HDC)arg0, arg1, arg2, arg3, (COLORREF *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglGetProcAddress)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("wglGetProcAddress\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)wglGetProcAddress((LPCSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglMakeCurrent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("wglMakeCurrent\n")

	return (jboolean)wglMakeCurrent((HDC)arg0, (HGLRC)arg1);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglRealizeLayerPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("wglRealizeLayerPalette\n")

	return (jboolean)wglRealizeLayerPalette((HDC)arg0, arg1, (BOOL)arg2);
}

JNIEXPORT jint JNICALL WGL_NATIVE(wglSetLayerPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("wglSetLayerPaletteEntries\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)wglSetLayerPaletteEntries((HDC)arg0, arg1, arg2, arg3, (CONST COLORREF *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglShareLists)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("wglShareLists\n")

	return (jboolean)wglShareLists((HGLRC)arg0, (HGLRC)arg1);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglSwapLayerBuffers)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("wglSwapLayerBuffers\n")

	return (jboolean)wglSwapLayerBuffers((HDC)arg0, arg1);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglUseFontBitmapsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("wglUseFontBitmapsA\n")

	return (jboolean)wglUseFontBitmapsA((HDC)arg0, arg1, arg2, arg3);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglUseFontBitmapsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("wglUseFontBitmapsW\n")

	return (jboolean)wglUseFontBitmapsW((HDC)arg0, arg1, arg2, arg3);
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglUseFontOutlinesA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jfloat arg4, jfloat arg5, jint arg6, jobject arg7)
{
//	GLYPHMETRICSFLOAT _arg7, *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("wglUseFontOutlinesA\n")

//	if (arg7) lparg7 = getGLYPHMETRICSFLOATFields(env, arg7, &_arg7);
	rc = (jboolean)wglUseFontOutlinesA((HDC)arg0, arg1, arg2, arg3, (FLOAT)arg4, (FLOAT)arg5, arg6, (LPGLYPHMETRICSFLOAT)arg7);
//	if (arg7) setGLYPHMETRICSFLOATFields(env, arg7, lparg7);
	return rc;
}

JNIEXPORT jboolean JNICALL WGL_NATIVE(wglUseFontOutlinesW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jfloat arg4, jfloat arg5, jint arg6, jobject arg7)
{
//	GLYPHMETRICSFLOAT _arg7, *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("wglUseFontOutlinesW\n")

//	if (arg7) lparg7 = getGLYPHMETRICSFLOATFields(env, arg7, &_arg7);
	rc = (jboolean)wglUseFontOutlinesW((HDC)arg0, arg1, arg2, arg3, (FLOAT)arg4, (FLOAT)arg5, arg6, (LPGLYPHMETRICSFLOAT)arg7);
//	if (arg7) setGLYPHMETRICSFLOATFields(env, arg7, lparg7);
	return rc;
}

JNIEXPORT void JNICALL WGL_NATIVE(MoveMemoryW__Lorg_eclipse_swt_opengl_internal_win32_GLYPHMETRICSFLOAT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GLYPHMETRICSFLOAT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryW__Lorg_eclipse_swt_opengl_internal_win32_GLYPHMETRICSFLOAT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setGLYPHMETRICSFLOATFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL WGL_NATIVE(MoveMemoryA__Lorg_eclipse_swt_opengl_internal_win32_GLYPHMETRICSFLOAT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GLYPHMETRICSFLOAT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryA__Lorg_eclipse_swt_opengl_internal_win32_GLYPHMETRICSFLOAT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setGLYPHMETRICSFLOATFields(env, arg0, lparg0);
}



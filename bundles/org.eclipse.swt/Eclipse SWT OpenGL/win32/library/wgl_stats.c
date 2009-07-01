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
#include "wgl_stats.h"

#ifdef NATIVE_STATS

int WGL_nativeFunctionCount = 19;
int WGL_nativeFunctionCallCount[19];
char * WGL_nativeFunctionNames[] = {
	"ChoosePixelFormat",
	"DescribePixelFormat",
	"GetPixelFormat",
	"SetPixelFormat",
	"SwapBuffers",
	"wglCopyContext",
	"wglCreateContext",
	"wglCreateLayerContext",
	"wglDeleteContext",
	"wglDescribeLayerPlane",
	"wglGetCurrentContext",
	"wglGetCurrentDC",
	"wglGetLayerPaletteEntries",
	"wglGetProcAddress",
	"wglMakeCurrent",
	"wglRealizeLayerPalette",
	"wglSetLayerPaletteEntries",
	"wglShareLists",
	"wglSwapLayerBuffers",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(WGL_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return WGL_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(WGL_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, WGL_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(WGL_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return WGL_nativeFunctionCallCount[index];
}

#endif

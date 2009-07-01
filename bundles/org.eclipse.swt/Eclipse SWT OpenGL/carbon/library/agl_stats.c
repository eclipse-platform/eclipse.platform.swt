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
#include "agl_stats.h"

#ifdef NATIVE_STATS

int AGL_nativeFunctionCount = 14;
int AGL_nativeFunctionCallCount[14];
char * AGL_nativeFunctionNames[] = {
	"aglChoosePixelFormat",
	"aglCreateContext",
	"aglDescribePixelFormat",
	"aglDestroyContext",
	"aglDestroyPixelFormat",
	"aglEnable",
	"aglGetCurrentContext",
	"aglGetDrawable",
	"aglSetCurrentContext",
	"aglSetDrawable",
	"aglSetInteger__III",
	"aglSetInteger__II_3I",
	"aglSwapBuffers",
	"aglUpdateContext",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(AGL_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return AGL_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(AGL_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, AGL_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(AGL_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return AGL_nativeFunctionCallCount[index];
}

#endif

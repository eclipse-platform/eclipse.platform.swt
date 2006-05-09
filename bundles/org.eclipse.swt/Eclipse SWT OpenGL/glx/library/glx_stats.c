/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "glx_stats.h"

#ifdef NATIVE_STATS

int GLX_nativeFunctionCount = 23;
int GLX_nativeFunctionCallCount[23];
char * GLX_nativeFunctionNames[] = {
	"XVisualInfo_1sizeof",
	"glGetIntegerv",
	"glViewport",
	"glXChooseVisual",
	"glXCopyContext",
	"glXCreateContext",
	"glXCreateGLXPixmap",
	"glXDestroyContext",
	"glXDestroyGLXPixmap",
	"glXGetClientString",
	"glXGetConfig",
	"glXGetCurrentContext",
	"glXGetCurrentDrawable",
	"glXIsDirect",
	"glXMakeCurrent",
	"glXQueryExtension",
	"glXQueryExtensionsString",
	"glXQueryServerString",
	"glXQueryVersion",
	"glXSwapBuffers",
	"glXWaitGL",
	"glXWaitX",
	"memmove",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(GLX_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return GLX_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(GLX_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, GLX_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(GLX_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return GLX_nativeFunctionCallCount[index];
}

#endif

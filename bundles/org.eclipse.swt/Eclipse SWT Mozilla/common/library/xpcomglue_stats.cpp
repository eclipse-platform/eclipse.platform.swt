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
#include "xpcomglue_stats.h"

#ifdef NATIVE_STATS

int XPCOMGlue_nativeFunctionCount = 2;
int XPCOMGlue_nativeFunctionCallCount[2];
char * XPCOMGlue_nativeFunctionNames[] = {
	"XPCOMGlueShutdown",
	"XPCOMGlueStartup",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOMGlue_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return XPCOMGlue_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(XPCOMGlue_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(XPCOMGlue_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOMGlue_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return XPCOMGlue_nativeFunctionCallCount[index];
}

#endif

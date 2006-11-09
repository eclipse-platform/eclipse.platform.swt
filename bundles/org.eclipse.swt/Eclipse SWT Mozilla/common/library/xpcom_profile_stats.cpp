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
#include "xpcom_profile_stats.h"

#ifdef NATIVE_STATS

int XPCOM_PROFILE_nativeFunctionCount = 4;
int XPCOM_PROFILE_nativeFunctionCallCount[4];
char * XPCOM_PROFILE_nativeFunctionNames[] = {
	"NS_1NewProfileDirServiceProvider",
	"ProfileDirServiceProvider_1Register",
	"ProfileDirServiceProvider_1SetProfileDir",
	"ProfileDirServiceProvider_1Shutdown",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOM_1PROFILE_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return XPCOM_PROFILE_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(XPCOM_1PROFILE_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(XPCOM_PROFILE_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOM_1PROFILE_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return XPCOM_PROFILE_nativeFunctionCallCount[index];
}

#endif

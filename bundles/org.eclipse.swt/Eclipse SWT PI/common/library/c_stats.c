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
#include "c_stats.h"

#ifdef NATIVE_STATS

int C_nativeFunctionCount = 4;
int C_nativeFunctionCallCount[4];
char * C_nativeFunctionNames[] = {
	"PTR_1sizeof",
	"free",
	"malloc",
	"strlen",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(C_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return C_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(C_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, C_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(C_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return C_nativeFunctionCallCount[index];
}

#endif

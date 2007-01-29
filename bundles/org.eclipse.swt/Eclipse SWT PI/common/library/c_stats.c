/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

int C_nativeFunctionCount = 23;
int C_nativeFunctionCallCount[23];
char * C_nativeFunctionNames[] = {
	"PTR_1sizeof",
	"free",
	"getenv",
	"malloc",
	"memmove__III",
	"memmove__I_3BI",
	"memmove__I_3CI",
	"memmove__I_3DI",
	"memmove__I_3FI",
	"memmove__I_3II",
	"memmove__I_3JI",
	"memmove__I_3SI",
	"memmove___3BII",
	"memmove___3B_3CI",
	"memmove___3CII",
	"memmove___3DII",
	"memmove___3FII",
	"memmove___3III",
	"memmove___3I_3BI",
	"memmove___3JII",
	"memmove___3SII",
	"memset",
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

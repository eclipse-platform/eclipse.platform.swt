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
#include "c_stats.h"

#ifdef NATIVE_STATS

int C_nativeFunctionCount = 23;
int C_nativeFunctionCallCount[23];
char * C_nativeFunctionNames[] = {
	"PTR_1sizeof",
	"free",
	"getenv",
	"malloc",
#ifndef JNI64
	"memmove__III",
#else
	"memmove__JJJ",
#endif
#ifndef JNI64
	"memmove__I_3BI",
#else
	"memmove__J_3BJ",
#endif
#ifndef JNI64
	"memmove__I_3CI",
#else
	"memmove__J_3CJ",
#endif
#ifndef JNI64
	"memmove__I_3DI",
#else
	"memmove__J_3DJ",
#endif
#ifndef JNI64
	"memmove__I_3FI",
#else
	"memmove__J_3FJ",
#endif
#ifndef JNI64
	"memmove__I_3II",
#else
	"memmove__J_3IJ",
#endif
#ifndef JNI64
	"memmove__I_3JI",
#else
	"memmove__J_3JJ",
#endif
#ifndef JNI64
	"memmove__I_3SI",
#else
	"memmove__J_3SJ",
#endif
#ifndef JNI64
	"memmove___3BII",
#else
	"memmove___3BJJ",
#endif
#ifndef JNI64
	"memmove___3B_3CI",
#else
	"memmove___3B_3CJ",
#endif
#ifndef JNI64
	"memmove___3CII",
#else
	"memmove___3CJJ",
#endif
#ifndef JNI64
	"memmove___3DII",
#else
	"memmove___3DJJ",
#endif
#ifndef JNI64
	"memmove___3FII",
#else
	"memmove___3FJJ",
#endif
#ifndef JNI64
	"memmove___3III",
#else
	"memmove___3IJJ",
#endif
#ifndef JNI64
	"memmove___3I_3BI",
#else
	"memmove___3I_3BJ",
#endif
#ifndef JNI64
	"memmove___3JII",
#else
	"memmove___3JJJ",
#endif
#ifndef JNI64
	"memmove___3SII",
#else
	"memmove___3SJJ",
#endif
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

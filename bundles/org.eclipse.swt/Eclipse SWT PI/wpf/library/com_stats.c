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
#include "com_stats.h"

#ifdef NATIVE_STATS

int COM_nativeFunctionCount = 2;
int COM_nativeFunctionCallCount[2];
char * COM_nativeFunctionNames[] = {
	"OleInitialize",
	"OleUninitialize",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(COM_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return COM_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(COM_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, COM_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(COM_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return COM_nativeFunctionCallCount[index];
}

#endif

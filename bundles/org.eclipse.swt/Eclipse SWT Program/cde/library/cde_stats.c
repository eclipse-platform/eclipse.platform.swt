/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "cde_stats.h"

#ifdef NATIVE_STATS

int CDE_nativeFunctionCount = 11;
int CDE_nativeFunctionCallCount[11];
char * CDE_nativeFunctionNames[] = {
	"_1DtActionInvoke",
	"_1DtAppInitialize",
	"_1DtDbLoad",
	"_1DtDtsDataTypeIsAction",
	"_1DtDtsDataTypeNames",
	"_1DtDtsDataTypeToAttributeValue",
	"_1DtDtsFileToDataType",
	"_1DtDtsFreeAttributeValue",
	"_1DtDtsFreeDataType",
	"_1DtDtsFreeDataTypeNames",
	"_1DtInitialize",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(CDE_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return CDE_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(CDE_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, CDE_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(CDE_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return CDE_nativeFunctionCallCount[index];
}

#endif

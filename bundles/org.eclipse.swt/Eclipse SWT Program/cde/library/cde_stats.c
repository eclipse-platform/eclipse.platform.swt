/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cde_stats.h"

#ifdef NATIVE_STATS

char * CDE_nativeFunctionNames[] = {
	"DtActionArg_1sizeof",
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
	"_1XtAppCreateShell",
	"_1XtCreateApplicationContext",
	"_1XtDisplayInitialize",
	"_1XtRealizeWidget",
	"_1XtResizeWidget",
	"_1XtSetMappedWhenManaged",
	"_1XtToolkitInitialize",
	"_1topLevelShellWidgetClass",
};
#define NATIVE_FUNCTION_COUNT sizeof(CDE_nativeFunctionNames) / sizeof(char*)
int CDE_nativeFunctionCount = NATIVE_FUNCTION_COUNT;
int CDE_nativeFunctionCallCount[NATIVE_FUNCTION_COUNT];

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

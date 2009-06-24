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
#include "win32_stats.h"

#ifdef NATIVE_STATS

int Win32_nativeFunctionCount = 26;
int Win32_nativeFunctionCallCount[26];
char * Win32_nativeFunctionNames[] = {
	"CloseHandle",
	"CreateCursor",
	"CreateIconIndirect",
	"CreateProcessW",
	"DeleteObject",
	"DestroyIcon",
	"EnableWindow",
	"ExtractIconExW",
	"GetCursorPos",
	"GetIconInfo",
	"GetKeyboardState",
	"GetModuleHandleW",
	"GetProcessHeap",
	"HeapAlloc",
	"HeapFree",
	"LoadImage",
	"MapVirtualKeyW",
	"MoveMemory",
	"OleInitialize",
	"OleUninitialize",
	"PROCESS_1INFORMATION_1sizeof",
	"SHELLEXECUTEINFOW_1sizeof",
	"STARTUPINFOW_1sizeof",
	"SetCursorPos",
	"ShellExecuteExW",
	"ToUnicode",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(Win32_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return Win32_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(Win32_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, Win32_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(Win32_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return Win32_nativeFunctionCallCount[index];
}

#endif

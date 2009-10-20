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

#ifdef NATIVE_STATS
extern int Win32_nativeFunctionCount;
extern int Win32_nativeFunctionCallCount[];
extern char* Win32_nativeFunctionNames[];
#define Win32_NATIVE_ENTER(env, that, func) Win32_nativeFunctionCallCount[func]++;
#define Win32_NATIVE_EXIT(env, that, func) 
#else
#ifndef Win32_NATIVE_ENTER
#define Win32_NATIVE_ENTER(env, that, func) 
#endif
#ifndef Win32_NATIVE_EXIT
#define Win32_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	CloseHandle_FUNC,
	CreateCursor_FUNC,
	CreateIconIndirect_FUNC,
	CreateProcessW_FUNC,
	DeleteObject_FUNC,
	DestroyIcon_FUNC,
	EnableWindow_FUNC,
	ExtractIconExW_FUNC,
	GetCursorPos_FUNC,
	GetIconInfo_FUNC,
	GetKeyboardState_FUNC,
	GetModuleHandleW_FUNC,
	GetProcessHeap_FUNC,
	HeapAlloc_FUNC,
	HeapFree_FUNC,
	LoadImage_FUNC,
	MapVirtualKeyW_FUNC,
	MoveMemory_FUNC,
	OleInitialize_FUNC,
	OleUninitialize_FUNC,
	PROCESS_1INFORMATION_1sizeof_FUNC,
	SHELLEXECUTEINFOW_1sizeof_FUNC,
	STARTUPINFOW_1sizeof_FUNC,
	SetCursorPos_FUNC,
	ShellExecuteExW_FUNC,
	ToUnicode_FUNC,
} Win32_FUNCS;

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
#include "win32_structs.h"
#include "win32_stats.h"

#ifndef Win32_NATIVE
#define Win32_NATIVE(func) Java_org_eclipse_swt_internal_win32_Win32_##func
#endif

#ifndef NO_CloseHandle
JNIEXPORT jboolean JNICALL Win32_NATIVE(CloseHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, CloseHandle_FUNC);
	rc = (jboolean)CloseHandle((HANDLE)arg0);
	Win32_NATIVE_EXIT(env, that, CloseHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCursor
JNIEXPORT jint JNICALL Win32_NATIVE(CreateCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, CreateCursor_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) if ((lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL)) == NULL) goto fail;
		if (arg6) if ((lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
		if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	}
	rc = (jint)CreateCursor((HINSTANCE)arg0, arg1, arg2, arg3, arg4, (CONST VOID *)lparg5, (CONST VOID *)lparg6);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6 && lparg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	} else
#endif
	{
		if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, JNI_ABORT);
	}
	Win32_NATIVE_EXIT(env, that, CreateCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateIconIndirect
JNIEXPORT jint JNICALL Win32_NATIVE(CreateIconIndirect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ICONINFO _arg0, *lparg0=NULL;
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, CreateIconIndirect_FUNC);
	if (arg0) if ((lparg0 = getICONINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CreateIconIndirect(lparg0);
fail:
	if (arg0 && lparg0) setICONINFOFields(env, arg0, lparg0);
	Win32_NATIVE_EXIT(env, that, CreateIconIndirect_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateProcessW
JNIEXPORT jboolean JNICALL Win32_NATIVE(CreateProcessW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jboolean arg4, jint arg5, jint arg6, jint arg7, jobject arg8, jobject arg9)
{
	jchar *lparg1=NULL;
	STARTUPINFOW _arg8, *lparg8=NULL;
	PROCESS_INFORMATION _arg9, *lparg9=NULL;
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, CreateProcessW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getSTARTUPINFOWFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = getPROCESS_INFORMATIONFields(env, arg9, &_arg9)) == NULL) goto fail;
	rc = (jboolean)CreateProcessW((LPCWSTR)arg0, (LPWSTR)lparg1, (LPSECURITY_ATTRIBUTES)arg2, (LPSECURITY_ATTRIBUTES)arg3, arg4, (DWORD)arg5, (LPVOID)arg6, (LPCWSTR)arg7, (LPSTARTUPINFOW)lparg8, (LPPROCESS_INFORMATION)lparg9);
fail:
	if (arg9 && lparg9) setPROCESS_INFORMATIONFields(env, arg9, lparg9);
	if (arg8 && lparg8) setSTARTUPINFOWFields(env, arg8, lparg8);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	Win32_NATIVE_EXIT(env, that, CreateProcessW_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteObject
JNIEXPORT jboolean JNICALL Win32_NATIVE(DeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, DeleteObject_FUNC);
	rc = (jboolean)DeleteObject((HGDIOBJ)arg0);
	Win32_NATIVE_EXIT(env, that, DeleteObject_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyIcon
JNIEXPORT jboolean JNICALL Win32_NATIVE(DestroyIcon)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, DestroyIcon_FUNC);
	rc = (jboolean)DestroyIcon((HICON)arg0);
	Win32_NATIVE_EXIT(env, that, DestroyIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableWindow
JNIEXPORT void JNICALL Win32_NATIVE(EnableWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	Win32_NATIVE_ENTER(env, that, EnableWindow_FUNC);
	EnableWindow((HWND)arg0, arg1);
	Win32_NATIVE_EXIT(env, that, EnableWindow_FUNC);
}
#endif

#ifndef NO_ExtractIconExW
JNIEXPORT jint JNICALL Win32_NATIVE(ExtractIconExW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintArray arg2, jintArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, ExtractIconExW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ExtractIconExW((LPCWSTR)lparg0, arg1, (HICON*)lparg2, (HICON*)lparg3, (UINT)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	Win32_NATIVE_EXIT(env, that, ExtractIconExW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCursorPos
JNIEXPORT void JNICALL Win32_NATIVE(GetCursorPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	Win32_NATIVE_ENTER(env, that, GetCursorPos_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	GetCursorPos((LPPOINT)lparg0);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	Win32_NATIVE_EXIT(env, that, GetCursorPos_FUNC);
}
#endif

#ifndef NO_GetIconInfo
JNIEXPORT jboolean JNICALL Win32_NATIVE(GetIconInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ICONINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, GetIconInfo_FUNC);
	if (arg1) if ((lparg1 = getICONINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetIconInfo((HICON)arg0, lparg1);
fail:
	if (arg1 && lparg1) setICONINFOFields(env, arg1, lparg1);
	Win32_NATIVE_EXIT(env, that, GetIconInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyboardState
JNIEXPORT jboolean JNICALL Win32_NATIVE(GetKeyboardState)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, GetKeyboardState_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)GetKeyboardState((PBYTE)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	Win32_NATIVE_EXIT(env, that, GetKeyboardState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleHandleW
JNIEXPORT jint JNICALL Win32_NATIVE(GetModuleHandleW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, GetModuleHandleW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetModuleHandleW((LPCWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	Win32_NATIVE_EXIT(env, that, GetModuleHandleW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcessHeap
JNIEXPORT jint JNICALL Win32_NATIVE(GetProcessHeap)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, GetProcessHeap_FUNC);
	rc = (jint)GetProcessHeap();
	Win32_NATIVE_EXIT(env, that, GetProcessHeap_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapAlloc
JNIEXPORT jint JNICALL Win32_NATIVE(HeapAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, HeapAlloc_FUNC);
	rc = (jint)HeapAlloc((HANDLE)arg0, arg1, arg2);
	Win32_NATIVE_EXIT(env, that, HeapAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapFree
JNIEXPORT jboolean JNICALL Win32_NATIVE(HeapFree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, HeapFree_FUNC);
	rc = (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
	Win32_NATIVE_EXIT(env, that, HeapFree_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadImage
JNIEXPORT jint JNICALL Win32_NATIVE(LoadImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, LoadImage_FUNC);
	rc = (jint)LoadImage((HINSTANCE)arg0, (LPCTSTR)arg1, (UINT)arg2, arg3, arg4, (UINT)arg5);
	Win32_NATIVE_EXIT(env, that, LoadImage_FUNC);
	return rc;
}
#endif

#ifndef NO_MapVirtualKeyW
JNIEXPORT jint JNICALL Win32_NATIVE(MapVirtualKeyW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, MapVirtualKeyW_FUNC);
	rc = (jint)MapVirtualKeyW(arg0, arg1);
	Win32_NATIVE_EXIT(env, that, MapVirtualKeyW_FUNC);
	return rc;
}
#endif

#ifndef NO_MoveMemory
JNIEXPORT void JNICALL Win32_NATIVE(MoveMemory)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	Win32_NATIVE_ENTER(env, that, MoveMemory_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	Win32_NATIVE_EXIT(env, that, MoveMemory_FUNC);
}
#endif

#ifndef NO_OleInitialize
JNIEXPORT jint JNICALL Win32_NATIVE(OleInitialize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, OleInitialize_FUNC);
	rc = (jint)OleInitialize((LPVOID)arg0);
	Win32_NATIVE_EXIT(env, that, OleInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO_OleUninitialize
JNIEXPORT void JNICALL Win32_NATIVE(OleUninitialize)
	(JNIEnv *env, jclass that)
{
	Win32_NATIVE_ENTER(env, that, OleUninitialize_FUNC);
	OleUninitialize();
	Win32_NATIVE_EXIT(env, that, OleUninitialize_FUNC);
}
#endif

#ifndef NO_PROCESS_1INFORMATION_1sizeof
JNIEXPORT jint JNICALL Win32_NATIVE(PROCESS_1INFORMATION_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, PROCESS_1INFORMATION_1sizeof_FUNC);
	rc = (jint)PROCESS_INFORMATION_sizeof();
	Win32_NATIVE_EXIT(env, that, PROCESS_1INFORMATION_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHELLEXECUTEINFOW_1sizeof
JNIEXPORT jint JNICALL Win32_NATIVE(SHELLEXECUTEINFOW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, SHELLEXECUTEINFOW_1sizeof_FUNC);
	rc = (jint)SHELLEXECUTEINFOW_sizeof();
	Win32_NATIVE_EXIT(env, that, SHELLEXECUTEINFOW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_STARTUPINFOW_1sizeof
JNIEXPORT jint JNICALL Win32_NATIVE(STARTUPINFOW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, STARTUPINFOW_1sizeof_FUNC);
	rc = (jint)STARTUPINFOW_sizeof();
	Win32_NATIVE_EXIT(env, that, STARTUPINFOW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCursorPos
JNIEXPORT jint JNICALL Win32_NATIVE(SetCursorPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, SetCursorPos_FUNC);
	rc = (jint)SetCursorPos(arg0, arg1);
	Win32_NATIVE_EXIT(env, that, SetCursorPos_FUNC);
	return rc;
}
#endif

#ifndef NO_ShellExecuteExW
JNIEXPORT jboolean JNICALL Win32_NATIVE(ShellExecuteExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFOW _arg0, *lparg0=NULL;
	jboolean rc = 0;
	Win32_NATIVE_ENTER(env, that, ShellExecuteExW_FUNC);
	if (arg0) if ((lparg0 = getSHELLEXECUTEINFOWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ShellExecuteExW((LPSHELLEXECUTEINFOW)lparg0);
fail:
	if (arg0 && lparg0) setSHELLEXECUTEINFOWFields(env, arg0, lparg0);
	Win32_NATIVE_EXIT(env, that, ShellExecuteExW_FUNC);
	return rc;
}
#endif

#ifndef NO_ToUnicode
JNIEXPORT jint JNICALL Win32_NATIVE(ToUnicode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jcharArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	Win32_NATIVE_ENTER(env, that, ToUnicode_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ToUnicode(arg0, arg1, (PBYTE)lparg2, (LPWSTR)lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	Win32_NATIVE_EXIT(env, that, ToUnicode_FUNC);
	return rc;
}
#endif


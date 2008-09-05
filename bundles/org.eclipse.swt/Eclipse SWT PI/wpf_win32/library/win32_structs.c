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
#include "win32_structs.h"

#ifndef NO_ICONINFO
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;
} ICONINFO_FID_CACHE;

ICONINFO_FID_CACHE ICONINFOFc;

void cacheICONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (ICONINFOFc.cached) return;
	ICONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ICONINFOFc.fIcon = (*env)->GetFieldID(env, ICONINFOFc.clazz, "fIcon", "Z");
	ICONINFOFc.xHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "xHotspot", "I");
	ICONINFOFc.yHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "yHotspot", "I");
	ICONINFOFc.hbmMask = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmMask", "I");
	ICONINFOFc.hbmColor = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmColor", "I");
	ICONINFOFc.cached = 1;
}

ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	lpStruct->fIcon = (*env)->GetBooleanField(env, lpObject, ICONINFOFc.fIcon);
	lpStruct->xHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.xHotspot);
	lpStruct->yHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.yHotspot);
	lpStruct->hbmMask = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmMask);
	lpStruct->hbmColor = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
}
#endif

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFields(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	POINTFc.x = (*env)->GetFieldID(env, POINTFc.clazz, "x", "I");
	POINTFc.y = (*env)->GetFieldID(env, POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, POINTFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, POINTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, POINTFc.y, (jint)lpStruct->y);
}
#endif

#ifndef NO_PROCESS_INFORMATION
typedef struct PROCESS_INFORMATION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hProcess, hThread, dwProcessId, dwThreadId;
} PROCESS_INFORMATION_FID_CACHE;

PROCESS_INFORMATION_FID_CACHE PROCESS_INFORMATIONFc;

void cachePROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject)
{
	if (PROCESS_INFORMATIONFc.cached) return;
	PROCESS_INFORMATIONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PROCESS_INFORMATIONFc.hProcess = (*env)->GetFieldID(env, PROCESS_INFORMATIONFc.clazz, "hProcess", I_J);
	PROCESS_INFORMATIONFc.hThread = (*env)->GetFieldID(env, PROCESS_INFORMATIONFc.clazz, "hThread", I_J);
	PROCESS_INFORMATIONFc.dwProcessId = (*env)->GetFieldID(env, PROCESS_INFORMATIONFc.clazz, "dwProcessId", "I");
	PROCESS_INFORMATIONFc.dwThreadId = (*env)->GetFieldID(env, PROCESS_INFORMATIONFc.clazz, "dwThreadId", "I");
	PROCESS_INFORMATIONFc.cached = 1;
}

PROCESS_INFORMATION *getPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct)
{
	if (!PROCESS_INFORMATIONFc.cached) cachePROCESS_INFORMATIONFields(env, lpObject);
	lpStruct->hProcess = (HANDLE)(*env)->GetIntLongField(env, lpObject, PROCESS_INFORMATIONFc.hProcess);
	lpStruct->hThread = (HANDLE)(*env)->GetIntLongField(env, lpObject, PROCESS_INFORMATIONFc.hThread);
	lpStruct->dwProcessId = (*env)->GetIntField(env, lpObject, PROCESS_INFORMATIONFc.dwProcessId);
	lpStruct->dwThreadId = (*env)->GetIntField(env, lpObject, PROCESS_INFORMATIONFc.dwThreadId);
	return lpStruct;
}

void setPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct)
{
	if (!PROCESS_INFORMATIONFc.cached) cachePROCESS_INFORMATIONFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, PROCESS_INFORMATIONFc.hProcess, (jintLong)lpStruct->hProcess);
	(*env)->SetIntLongField(env, lpObject, PROCESS_INFORMATIONFc.hThread, (jintLong)lpStruct->hThread);
	(*env)->SetIntField(env, lpObject, PROCESS_INFORMATIONFc.dwProcessId, (jint)lpStruct->dwProcessId);
	(*env)->SetIntField(env, lpObject, PROCESS_INFORMATIONFc.dwThreadId, (jint)lpStruct->dwThreadId);
}
#endif

#ifndef NO_SHELLEXECUTEINFOW
typedef struct SHELLEXECUTEINFOW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, hwnd, lpVerb, lpFile, lpParameters, lpDirectory, nShow, hInstApp, lpIDList, lpClass, hkeyClass, dwHotKey, hIcon, hProcess;
} SHELLEXECUTEINFOW_FID_CACHE;

SHELLEXECUTEINFOW_FID_CACHE SHELLEXECUTEINFOWFc;

void cacheSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject)
{
	if (SHELLEXECUTEINFOWFc.cached) return;
	SHELLEXECUTEINFOWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHELLEXECUTEINFOWFc.cbSize = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "cbSize", "I");
	SHELLEXECUTEINFOWFc.fMask = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "fMask", "I");
	SHELLEXECUTEINFOWFc.hwnd = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "hwnd", I_J);
	SHELLEXECUTEINFOWFc.lpVerb = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpVerb", I_J);
	SHELLEXECUTEINFOWFc.lpFile = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpFile", "I");
	SHELLEXECUTEINFOWFc.lpParameters = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpParameters", I_J);
	SHELLEXECUTEINFOWFc.lpDirectory = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpDirectory", I_J);
	SHELLEXECUTEINFOWFc.nShow = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "nShow", "I");
	SHELLEXECUTEINFOWFc.hInstApp = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "hInstApp", I_J);
	SHELLEXECUTEINFOWFc.lpIDList = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpIDList", I_J);
	SHELLEXECUTEINFOWFc.lpClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "lpClass", I_J);
	SHELLEXECUTEINFOWFc.hkeyClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "hkeyClass", I_J);
	SHELLEXECUTEINFOWFc.dwHotKey = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "dwHotKey", "I");
	SHELLEXECUTEINFOWFc.hIcon = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "hIcon", I_J);
	SHELLEXECUTEINFOWFc.hProcess = (*env)->GetFieldID(env, SHELLEXECUTEINFOWFc.clazz, "hProcess", I_J);
	SHELLEXECUTEINFOWFc.cached = 1;
}

SHELLEXECUTEINFOW *getSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct)
{
	if (!SHELLEXECUTEINFOWFc.cached) cacheSHELLEXECUTEINFOWFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOWFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOWFc.fMask);
	lpStruct->hwnd = (HWND)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hwnd);
	lpStruct->lpVerb = (LPWSTR)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpVerb);
	lpStruct->lpFile = (LPWSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOWFc.lpFile);
	lpStruct->lpParameters = (LPWSTR)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpParameters);
	lpStruct->lpDirectory = (LPWSTR)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpDirectory);
	lpStruct->nShow = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOWFc.nShow);
	lpStruct->hInstApp = (HINSTANCE)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hInstApp);
	lpStruct->lpIDList = (LPVOID)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpIDList);
	lpStruct->lpClass = (LPWSTR)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpClass);
	lpStruct->hkeyClass = (HKEY)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hkeyClass);
	lpStruct->dwHotKey = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOWFc.dwHotKey);
	lpStruct->hIcon = (HANDLE)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hIcon);
	lpStruct->hProcess = (HANDLE)(*env)->GetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hProcess);
	return lpStruct;
}

void setSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct)
{
	if (!SHELLEXECUTEINFOWFc.cached) cacheSHELLEXECUTEINFOWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOWFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOWFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hwnd, (jintLong)lpStruct->hwnd);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpVerb, (jintLong)lpStruct->lpVerb);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOWFc.lpFile, (jint)lpStruct->lpFile);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpParameters, (jintLong)lpStruct->lpParameters);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpDirectory, (jintLong)lpStruct->lpDirectory);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOWFc.nShow, (jint)lpStruct->nShow);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hInstApp, (jintLong)lpStruct->hInstApp);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpIDList, (jintLong)lpStruct->lpIDList);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.lpClass, (jintLong)lpStruct->lpClass);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hkeyClass, (jintLong)lpStruct->hkeyClass);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOWFc.dwHotKey, (jint)lpStruct->dwHotKey);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hIcon, (jintLong)lpStruct->hIcon);
	(*env)->SetIntLongField(env, lpObject, SHELLEXECUTEINFOWFc.hProcess, (jintLong)lpStruct->hProcess);
}
#endif

#ifndef NO_STARTUPINFOW
typedef struct STARTUPINFOW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, lpReserved, lpDesktop, lpTitle, dwX, dwY, dwXSize, dwYSize, dwXCountChars, dwYCountChars, dwFillAttribute, dwFlags, wShowWindow, cbReserved2, lpReserved2, hStdInput, hStdOutput, hStdError;
} STARTUPINFOW_FID_CACHE;

STARTUPINFOW_FID_CACHE STARTUPINFOWFc;

void cacheSTARTUPINFOWFields(JNIEnv *env, jobject lpObject)
{
	if (STARTUPINFOWFc.cached) return;
	STARTUPINFOWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	STARTUPINFOWFc.cb = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "cb", "I");
	STARTUPINFOWFc.lpReserved = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "lpReserved", I_J);
	STARTUPINFOWFc.lpDesktop = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "lpDesktop", I_J);
	STARTUPINFOWFc.lpTitle = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "lpTitle", I_J);
	STARTUPINFOWFc.dwX = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwX", "I");
	STARTUPINFOWFc.dwY = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwY", "I");
	STARTUPINFOWFc.dwXSize = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwXSize", "I");
	STARTUPINFOWFc.dwYSize = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwYSize", "I");
	STARTUPINFOWFc.dwXCountChars = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwXCountChars", "I");
	STARTUPINFOWFc.dwYCountChars = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwYCountChars", "I");
	STARTUPINFOWFc.dwFillAttribute = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwFillAttribute", "I");
	STARTUPINFOWFc.dwFlags = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "dwFlags", "I");
	STARTUPINFOWFc.wShowWindow = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "wShowWindow", "S");
	STARTUPINFOWFc.cbReserved2 = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "cbReserved2", "S");
	STARTUPINFOWFc.lpReserved2 = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "lpReserved2", I_J);
	STARTUPINFOWFc.hStdInput = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "hStdInput", I_J);
	STARTUPINFOWFc.hStdOutput = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "hStdOutput", I_J);
	STARTUPINFOWFc.hStdError = (*env)->GetFieldID(env, STARTUPINFOWFc.clazz, "hStdError", I_J);
	STARTUPINFOWFc.cached = 1;
}

STARTUPINFOW *getSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct)
{
	if (!STARTUPINFOWFc.cached) cacheSTARTUPINFOWFields(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.cb);
	lpStruct->lpReserved = (LPWSTR)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.lpReserved);
	lpStruct->lpDesktop = (LPWSTR)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.lpDesktop);
	lpStruct->lpTitle = (LPWSTR)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.lpTitle);
	lpStruct->dwX = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwX);
	lpStruct->dwY = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwY);
	lpStruct->dwXSize = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwXSize);
	lpStruct->dwYSize = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwYSize);
	lpStruct->dwXCountChars = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwXCountChars);
	lpStruct->dwYCountChars = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwYCountChars);
	lpStruct->dwFillAttribute = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwFillAttribute);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, STARTUPINFOWFc.dwFlags);
	lpStruct->wShowWindow = (*env)->GetShortField(env, lpObject, STARTUPINFOWFc.wShowWindow);
	lpStruct->cbReserved2 = (*env)->GetShortField(env, lpObject, STARTUPINFOWFc.cbReserved2);
	lpStruct->lpReserved2 = (LPBYTE)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.lpReserved2);
	lpStruct->hStdInput = (HANDLE)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.hStdInput);
	lpStruct->hStdOutput = (HANDLE)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.hStdOutput);
	lpStruct->hStdError = (HANDLE)(*env)->GetIntLongField(env, lpObject, STARTUPINFOWFc.hStdError);
	return lpStruct;
}

void setSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct)
{
	if (!STARTUPINFOWFc.cached) cacheSTARTUPINFOWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.lpReserved, (jintLong)lpStruct->lpReserved);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.lpDesktop, (jintLong)lpStruct->lpDesktop);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.lpTitle, (jintLong)lpStruct->lpTitle);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwX, (jint)lpStruct->dwX);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwY, (jint)lpStruct->dwY);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwXSize, (jint)lpStruct->dwXSize);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwYSize, (jint)lpStruct->dwYSize);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwXCountChars, (jint)lpStruct->dwXCountChars);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwYCountChars, (jint)lpStruct->dwYCountChars);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwFillAttribute, (jint)lpStruct->dwFillAttribute);
	(*env)->SetIntField(env, lpObject, STARTUPINFOWFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetShortField(env, lpObject, STARTUPINFOWFc.wShowWindow, (jshort)lpStruct->wShowWindow);
	(*env)->SetShortField(env, lpObject, STARTUPINFOWFc.cbReserved2, (jshort)lpStruct->cbReserved2);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.lpReserved2, (jintLong)lpStruct->lpReserved2);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.hStdInput, (jintLong)lpStruct->hStdInput);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.hStdOutput, (jintLong)lpStruct->hStdOutput);
	(*env)->SetIntLongField(env, lpObject, STARTUPINFOWFc.hStdError, (jintLong)lpStruct->hStdError);
}
#endif


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
#include "os_structs.h"

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
	ICONINFOFc.clazz = env->GetObjectClass(lpObject);
	ICONINFOFc.fIcon = env->GetFieldID(ICONINFOFc.clazz, "fIcon", "Z");
	ICONINFOFc.xHotspot = env->GetFieldID(ICONINFOFc.clazz, "xHotspot", "I");
	ICONINFOFc.yHotspot = env->GetFieldID(ICONINFOFc.clazz, "yHotspot", "I");
	ICONINFOFc.hbmMask = env->GetFieldID(ICONINFOFc.clazz, "hbmMask", "I");
	ICONINFOFc.hbmColor = env->GetFieldID(ICONINFOFc.clazz, "hbmColor", "I");
	ICONINFOFc.cached = 1;
}

ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	lpStruct->fIcon = env->GetBooleanField(lpObject, ICONINFOFc.fIcon);
	lpStruct->xHotspot = env->GetIntField(lpObject, ICONINFOFc.xHotspot);
	lpStruct->yHotspot = env->GetIntField(lpObject, ICONINFOFc.yHotspot);
	lpStruct->hbmMask = (HBITMAP)env->GetIntField(lpObject, ICONINFOFc.hbmMask);
	lpStruct->hbmColor = (HBITMAP)env->GetIntField(lpObject, ICONINFOFc.hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	env->SetBooleanField(lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
	env->SetIntField(lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	env->SetIntField(lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	env->SetIntField(lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	env->SetIntField(lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
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
	POINTFc.clazz = env->GetObjectClass(lpObject);
	POINTFc.x = env->GetFieldID(POINTFc.clazz, "x", "I");
	POINTFc.y = env->GetFieldID(POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->x = env->GetIntField(lpObject, POINTFc.x);
	lpStruct->y = env->GetIntField(lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	env->SetIntField(lpObject, POINTFc.x, (jint)lpStruct->x);
	env->SetIntField(lpObject, POINTFc.y, (jint)lpStruct->y);
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
	PROCESS_INFORMATIONFc.clazz = env->GetObjectClass(lpObject);
	PROCESS_INFORMATIONFc.hProcess = env->GetFieldID(PROCESS_INFORMATIONFc.clazz, "hProcess", "I");
	PROCESS_INFORMATIONFc.hThread = env->GetFieldID(PROCESS_INFORMATIONFc.clazz, "hThread", "I");
	PROCESS_INFORMATIONFc.dwProcessId = env->GetFieldID(PROCESS_INFORMATIONFc.clazz, "dwProcessId", "I");
	PROCESS_INFORMATIONFc.dwThreadId = env->GetFieldID(PROCESS_INFORMATIONFc.clazz, "dwThreadId", "I");
	PROCESS_INFORMATIONFc.cached = 1;
}

PROCESS_INFORMATION *getPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct)
{
	if (!PROCESS_INFORMATIONFc.cached) cachePROCESS_INFORMATIONFields(env, lpObject);
	lpStruct->hProcess = (HANDLE)env->GetIntField(lpObject, PROCESS_INFORMATIONFc.hProcess);
	lpStruct->hThread = (HANDLE)env->GetIntField(lpObject, PROCESS_INFORMATIONFc.hThread);
	lpStruct->dwProcessId = env->GetIntField(lpObject, PROCESS_INFORMATIONFc.dwProcessId);
	lpStruct->dwThreadId = env->GetIntField(lpObject, PROCESS_INFORMATIONFc.dwThreadId);
	return lpStruct;
}

void setPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct)
{
	if (!PROCESS_INFORMATIONFc.cached) cachePROCESS_INFORMATIONFields(env, lpObject);
	env->SetIntField(lpObject, PROCESS_INFORMATIONFc.hProcess, (jint)lpStruct->hProcess);
	env->SetIntField(lpObject, PROCESS_INFORMATIONFc.hThread, (jint)lpStruct->hThread);
	env->SetIntField(lpObject, PROCESS_INFORMATIONFc.dwProcessId, (jint)lpStruct->dwProcessId);
	env->SetIntField(lpObject, PROCESS_INFORMATIONFc.dwThreadId, (jint)lpStruct->dwThreadId);
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
	SHELLEXECUTEINFOWFc.clazz = env->GetObjectClass(lpObject);
	SHELLEXECUTEINFOWFc.cbSize = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "cbSize", "I");
	SHELLEXECUTEINFOWFc.fMask = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "fMask", "I");
	SHELLEXECUTEINFOWFc.hwnd = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "hwnd", "I");
	SHELLEXECUTEINFOWFc.lpVerb = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpVerb", "I");
	SHELLEXECUTEINFOWFc.lpFile = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpFile", "I");
	SHELLEXECUTEINFOWFc.lpParameters = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpParameters", "I");
	SHELLEXECUTEINFOWFc.lpDirectory = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpDirectory", "I");
	SHELLEXECUTEINFOWFc.nShow = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "nShow", "I");
	SHELLEXECUTEINFOWFc.hInstApp = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "hInstApp", "I");
	SHELLEXECUTEINFOWFc.lpIDList = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpIDList", "I");
	SHELLEXECUTEINFOWFc.lpClass = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "lpClass", "I");
	SHELLEXECUTEINFOWFc.hkeyClass = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "hkeyClass", "I");
	SHELLEXECUTEINFOWFc.dwHotKey = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "dwHotKey", "I");
	SHELLEXECUTEINFOWFc.hIcon = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "hIcon", "I");
	SHELLEXECUTEINFOWFc.hProcess = env->GetFieldID(SHELLEXECUTEINFOWFc.clazz, "hProcess", "I");
	SHELLEXECUTEINFOWFc.cached = 1;
}

SHELLEXECUTEINFOW *getSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct)
{
	if (!SHELLEXECUTEINFOWFc.cached) cacheSHELLEXECUTEINFOWFields(env, lpObject);
	lpStruct->cbSize = env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.cbSize);
	lpStruct->fMask = env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.fMask);
	lpStruct->hwnd = (HWND)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.hwnd);
	lpStruct->lpVerb = (LPWSTR)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpVerb);
	lpStruct->lpFile = (LPWSTR)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpFile);
	lpStruct->lpParameters = (LPWSTR)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpParameters);
	lpStruct->lpDirectory = (LPWSTR)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpDirectory);
	lpStruct->nShow = env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.nShow);
	lpStruct->hInstApp = (HINSTANCE)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.hInstApp);
	lpStruct->lpIDList = (LPVOID)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpIDList);
	lpStruct->lpClass = (LPWSTR)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.lpClass);
	lpStruct->hkeyClass = (HKEY)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.hkeyClass);
	lpStruct->dwHotKey = env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.dwHotKey);
	lpStruct->hIcon = (HANDLE)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.hIcon);
	lpStruct->hProcess = (HANDLE)env->GetIntField(lpObject, SHELLEXECUTEINFOWFc.hProcess);
	return lpStruct;
}

void setSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct)
{
	if (!SHELLEXECUTEINFOWFc.cached) cacheSHELLEXECUTEINFOWFields(env, lpObject);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.cbSize, (jint)lpStruct->cbSize);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.fMask, (jint)lpStruct->fMask);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.hwnd, (jint)lpStruct->hwnd);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpVerb, (jint)lpStruct->lpVerb);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpFile, (jint)lpStruct->lpFile);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpParameters, (jint)lpStruct->lpParameters);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpDirectory, (jint)lpStruct->lpDirectory);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.nShow, (jint)lpStruct->nShow);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.hInstApp, (jint)lpStruct->hInstApp);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpIDList, (jint)lpStruct->lpIDList);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.lpClass, (jint)lpStruct->lpClass);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.hkeyClass, (jint)lpStruct->hkeyClass);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.dwHotKey, (jint)lpStruct->dwHotKey);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.hIcon, (jint)lpStruct->hIcon);
	env->SetIntField(lpObject, SHELLEXECUTEINFOWFc.hProcess, (jint)lpStruct->hProcess);
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
	STARTUPINFOWFc.clazz = env->GetObjectClass(lpObject);
	STARTUPINFOWFc.cb = env->GetFieldID(STARTUPINFOWFc.clazz, "cb", "I");
	STARTUPINFOWFc.lpReserved = env->GetFieldID(STARTUPINFOWFc.clazz, "lpReserved", "I");
	STARTUPINFOWFc.lpDesktop = env->GetFieldID(STARTUPINFOWFc.clazz, "lpDesktop", "I");
	STARTUPINFOWFc.lpTitle = env->GetFieldID(STARTUPINFOWFc.clazz, "lpTitle", "I");
	STARTUPINFOWFc.dwX = env->GetFieldID(STARTUPINFOWFc.clazz, "dwX", "I");
	STARTUPINFOWFc.dwY = env->GetFieldID(STARTUPINFOWFc.clazz, "dwY", "I");
	STARTUPINFOWFc.dwXSize = env->GetFieldID(STARTUPINFOWFc.clazz, "dwXSize", "I");
	STARTUPINFOWFc.dwYSize = env->GetFieldID(STARTUPINFOWFc.clazz, "dwYSize", "I");
	STARTUPINFOWFc.dwXCountChars = env->GetFieldID(STARTUPINFOWFc.clazz, "dwXCountChars", "I");
	STARTUPINFOWFc.dwYCountChars = env->GetFieldID(STARTUPINFOWFc.clazz, "dwYCountChars", "I");
	STARTUPINFOWFc.dwFillAttribute = env->GetFieldID(STARTUPINFOWFc.clazz, "dwFillAttribute", "I");
	STARTUPINFOWFc.dwFlags = env->GetFieldID(STARTUPINFOWFc.clazz, "dwFlags", "I");
	STARTUPINFOWFc.wShowWindow = env->GetFieldID(STARTUPINFOWFc.clazz, "wShowWindow", "S");
	STARTUPINFOWFc.cbReserved2 = env->GetFieldID(STARTUPINFOWFc.clazz, "cbReserved2", "S");
	STARTUPINFOWFc.lpReserved2 = env->GetFieldID(STARTUPINFOWFc.clazz, "lpReserved2", "I");
	STARTUPINFOWFc.hStdInput = env->GetFieldID(STARTUPINFOWFc.clazz, "hStdInput", "I");
	STARTUPINFOWFc.hStdOutput = env->GetFieldID(STARTUPINFOWFc.clazz, "hStdOutput", "I");
	STARTUPINFOWFc.hStdError = env->GetFieldID(STARTUPINFOWFc.clazz, "hStdError", "I");
	STARTUPINFOWFc.cached = 1;
}

STARTUPINFOW *getSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct)
{
	if (!STARTUPINFOWFc.cached) cacheSTARTUPINFOWFields(env, lpObject);
	lpStruct->cb = env->GetIntField(lpObject, STARTUPINFOWFc.cb);
	lpStruct->lpReserved = (LPWSTR)env->GetIntField(lpObject, STARTUPINFOWFc.lpReserved);
	lpStruct->lpDesktop = (LPWSTR)env->GetIntField(lpObject, STARTUPINFOWFc.lpDesktop);
	lpStruct->lpTitle = (LPWSTR)env->GetIntField(lpObject, STARTUPINFOWFc.lpTitle);
	lpStruct->dwX = env->GetIntField(lpObject, STARTUPINFOWFc.dwX);
	lpStruct->dwY = env->GetIntField(lpObject, STARTUPINFOWFc.dwY);
	lpStruct->dwXSize = env->GetIntField(lpObject, STARTUPINFOWFc.dwXSize);
	lpStruct->dwYSize = env->GetIntField(lpObject, STARTUPINFOWFc.dwYSize);
	lpStruct->dwXCountChars = env->GetIntField(lpObject, STARTUPINFOWFc.dwXCountChars);
	lpStruct->dwYCountChars = env->GetIntField(lpObject, STARTUPINFOWFc.dwYCountChars);
	lpStruct->dwFillAttribute = env->GetIntField(lpObject, STARTUPINFOWFc.dwFillAttribute);
	lpStruct->dwFlags = env->GetIntField(lpObject, STARTUPINFOWFc.dwFlags);
	lpStruct->wShowWindow = env->GetShortField(lpObject, STARTUPINFOWFc.wShowWindow);
	lpStruct->cbReserved2 = env->GetShortField(lpObject, STARTUPINFOWFc.cbReserved2);
	lpStruct->lpReserved2 = (LPBYTE)env->GetIntField(lpObject, STARTUPINFOWFc.lpReserved2);
	lpStruct->hStdInput = (HANDLE)env->GetIntField(lpObject, STARTUPINFOWFc.hStdInput);
	lpStruct->hStdOutput = (HANDLE)env->GetIntField(lpObject, STARTUPINFOWFc.hStdOutput);
	lpStruct->hStdError = (HANDLE)env->GetIntField(lpObject, STARTUPINFOWFc.hStdError);
	return lpStruct;
}

void setSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct)
{
	if (!STARTUPINFOWFc.cached) cacheSTARTUPINFOWFields(env, lpObject);
	env->SetIntField(lpObject, STARTUPINFOWFc.cb, (jint)lpStruct->cb);
	env->SetIntField(lpObject, STARTUPINFOWFc.lpReserved, (jint)lpStruct->lpReserved);
	env->SetIntField(lpObject, STARTUPINFOWFc.lpDesktop, (jint)lpStruct->lpDesktop);
	env->SetIntField(lpObject, STARTUPINFOWFc.lpTitle, (jint)lpStruct->lpTitle);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwX, (jint)lpStruct->dwX);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwY, (jint)lpStruct->dwY);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwXSize, (jint)lpStruct->dwXSize);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwYSize, (jint)lpStruct->dwYSize);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwXCountChars, (jint)lpStruct->dwXCountChars);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwYCountChars, (jint)lpStruct->dwYCountChars);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwFillAttribute, (jint)lpStruct->dwFillAttribute);
	env->SetIntField(lpObject, STARTUPINFOWFc.dwFlags, (jint)lpStruct->dwFlags);
	env->SetShortField(lpObject, STARTUPINFOWFc.wShowWindow, (jshort)lpStruct->wShowWindow);
	env->SetShortField(lpObject, STARTUPINFOWFc.cbReserved2, (jshort)lpStruct->cbReserved2);
	env->SetIntField(lpObject, STARTUPINFOWFc.lpReserved2, (jint)lpStruct->lpReserved2);
	env->SetIntField(lpObject, STARTUPINFOWFc.hStdInput, (jint)lpStruct->hStdInput);
	env->SetIntField(lpObject, STARTUPINFOWFc.hStdOutput, (jint)lpStruct->hStdOutput);
	env->SetIntField(lpObject, STARTUPINFOWFc.hStdError, (jint)lpStruct->hStdError);
}
#endif


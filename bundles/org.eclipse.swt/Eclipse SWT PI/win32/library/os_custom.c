/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_win32_OS_##func

__declspec(dllexport) HRESULT DllGetVersion(DLLVERSIONINFO *dvi);
HRESULT DllGetVersion(DLLVERSIONINFO *dvi)
{
     dvi->dwMajorVersion = SWT_VERSION / 1000;
     dvi->dwMinorVersion = SWT_VERSION % 1000;
     dvi->dwBuildNumber = 0;
     dvi->dwPlatformID = DLLVER_PLATFORM_WINDOWS;
     return 1;
}

HINSTANCE g_hInstance = NULL;
BOOL WINAPI DllMain(HANDLE hInstDLL, DWORD dwReason, LPVOID lpvReserved)
{
	if (dwReason == DLL_PROCESS_ATTACH) {
		if (g_hInstance == NULL) g_hInstance = hInstDLL;
	}
	return TRUE;
}

#ifndef NO_GetLibraryHandle
JNIEXPORT jint JNICALL OS_NATIVE(GetLibraryHandle)
	(JNIEnv *env, jclass that)
{
	jint rc;
	OS_NATIVE_ENTER(env, that, GetLibraryHandle_FUNC)
	rc = (jint)g_hInstance;
	OS_NATIVE_EXIT(env, that, GetLibraryHandle_FUNC)
	return rc;
}
#endif

#ifndef NO_IsPPC
JNIEXPORT jboolean JNICALL OS_NATIVE(IsPPC)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, IsPPC_FUNC)
#ifdef WIN32_PLATFORM_PSPC
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	OS_NATIVE_EXIT(env, that, IsPPC_FUNC)
	return rc;
}
#endif

#ifndef NO_IsSP
JNIEXPORT jboolean JNICALL OS_NATIVE(IsSP)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, IsSP_FUNC)
#ifdef WIN32_PLATFORM_WFSP
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	OS_NATIVE_EXIT(env, that, IsSP_FUNC)
	return rc;
}
#endif

#ifndef NO_SendMessageW__II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	OS_NATIVE_ENTER(env, that, SendMessageW__II_3I_3I_FUNC)
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
#ifdef _WIN32_WCE
	/*
	* Bug on WinCE.  SendMessage can fail (return 0) when being passed references
	* to parameters allocated from the heap. The workaround is to allocate
	* the parameters on the stack and to copy them back to the java array.
	* Observed on Pocket PC WinCE 3.0 with EM_GETSEL and CB_GETEDITSEL messages.
	*/
	switch (arg1) {
		case EM_GETSEL:
		case CB_GETEDITSEL: {
			jint wParam = 0, lParam = 0;
			jint *lpwParam = NULL, *lplParam = NULL;
			if (lparg2 != NULL) lpwParam = &wParam;
			if (lparg3 != NULL) lplParam = &lParam;
			rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lpwParam, (LPARAM)lplParam);
			if (lparg2 != NULL) lparg2[0] = wParam;
			if (lparg3 != NULL) lparg3[0] = lParam;
			break;
		}
		default:
			rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
	}
#else
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
#endif
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageW__II_3I_3I_FUNC)
	return rc;
}
#endif

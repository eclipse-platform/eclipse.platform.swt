/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
    dvi->dwBuildNumber = SWT_REVISION;
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetLibraryHandle)
	(JNIEnv *env, jclass that)
{
	jintLong rc;
	OS_NATIVE_ENTER(env, that, GetLibraryHandle_FUNC)
	rc = (jintLong)g_hInstance;
	OS_NATIVE_EXIT(env, that, GetLibraryHandle_FUNC)
	return rc;
}
#endif

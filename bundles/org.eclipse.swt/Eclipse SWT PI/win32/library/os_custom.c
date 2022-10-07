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
	/* Suppress warnings about unreferenced parameters */
	(void)lpvReserved;

	if (dwReason == DLL_PROCESS_ATTACH) {
		if (g_hInstance == NULL) g_hInstance = hInstDLL;
	}
	return TRUE;
}

#ifndef NO_GetLibraryHandle
JNIEXPORT jlong JNICALL OS_NATIVE(GetLibraryHandle)
	(JNIEnv *env, jclass that)
{
	/* Suppress warnings about unreferenced parameters */
	(void)env;
	(void)that;

	jlong rc;
	OS_NATIVE_ENTER(env, that, GetLibraryHandle_FUNC)
	rc = (jlong)g_hInstance;
	OS_NATIVE_EXIT(env, that, GetLibraryHandle_FUNC)
	return rc;
}
#endif

BOOL Validate_AllowDarkModeForWindow(const BYTE* functionPtr)
{
	/*
	 * 'AllowDarkModeForWindow' is rather long, but it uses
	 * an ATOM value of 0xA91E which is unlikely to change
	 */

#ifdef _M_X64
	/* Win10 builds from 20236 */
	if ((functionPtr[0x52] == 0xBA) &&                      // mov     edx,
	    (*(const DWORD*)(functionPtr + 0x53) == 0xA91E))    //             0A91Eh
	{
		return TRUE;
	}

	/* Win10 builds from 17763 to 19041 */
	if ((functionPtr[0x15] == 0xBA) &&                      // mov     edx,
	    (*(const DWORD*)(functionPtr + 0x16) == 0xA91E))    //             0A91Eh
	{
		return TRUE;
	}

	return FALSE;
#else
	#error Unsupported processor type
#endif
}

typedef BOOL(WINAPI* TYPE_AllowDarkModeForWindow)(HWND a_HWND, BOOL a_Allow);
TYPE_AllowDarkModeForWindow Locate_AllowDarkModeForWindow()
{
	const HMODULE hUxtheme = GetModuleHandle(L"uxtheme.dll");
	if (!hUxtheme)
		return 0;

	/*
	 * Function is only exported by ordinal.
	 * Hopefully one day Microsoft will finally export it by name.
	 */
	const BYTE* candidate = (const BYTE*)GetProcAddress(hUxtheme, MAKEINTRESOURCEA(133));
	if (!candidate)
		return 0;

	/*
	 * In next Windows version, some other function can end up having this ordinal.
	 * Compare function's code to known signature to make sure.
	 */
	if (!Validate_AllowDarkModeForWindow(candidate))
		return 0;

	return (TYPE_AllowDarkModeForWindow)candidate;
}

BOOL Validate_AllowDarkModeForWindowWithTelemetryId(const BYTE* functionPtr)
{
#ifdef _M_X64
	/* This function is rather long, but it uses an ATOM value of 0xA91E which is unlikely to change */

	/* Win10 builds from 21301 */
	if ((functionPtr[0x31] == 0xBA) &&                      // mov      edx,
		(*(const DWORD*)(functionPtr + 0x32) == 0xA91E))    //              0A91Eh
	{
		return TRUE;
	}

	/* Win11 builds from 22621 */
	if ((functionPtr[0x15] == 0xBA) &&                      // mov      edx,
		(*(const DWORD*)(functionPtr + 0x16) == 0xA91E))    //              0A91Eh
	{
		return TRUE;
	}

	return FALSE;
#else
	#error Unsupported processor type
#endif
}

typedef BOOL (WINAPI* TYPE_AllowDarkModeForWindowWithTelemetryId)(HWND a_HWND, BOOL a_Allow, int a_TelemetryID);
TYPE_AllowDarkModeForWindowWithTelemetryId Locate_AllowDarkModeForWindowWithTelemetryId()
{
	const HMODULE hUxtheme = GetModuleHandle(L"uxtheme.dll");
	if (!hUxtheme)
		return 0;

	/*
	 * Function is only exported by ordinal.
	 * Hopefully one day Microsoft will finally export it by name.
	 */
	const BYTE* candidate = (const BYTE*)GetProcAddress(hUxtheme, MAKEINTRESOURCEA(140));
	if (!candidate)
		return 0;

	/*
	 * In next Windows version, some other function can end up having this ordinal.
	 * Compare function's code to known signature to make sure.
	 */
	if (!Validate_AllowDarkModeForWindowWithTelemetryId(candidate))
		return 0;

	return (TYPE_AllowDarkModeForWindowWithTelemetryId)candidate;
}

#ifndef NO_AllowDarkModeForWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(AllowDarkModeForWindow)
(JNIEnv* env, jclass that, jlong arg0, jboolean arg1)
{
	/* Suppress warnings about unreferenced parameters */
	(void)env;
	(void)that;

	/* Cache the search result for performance reasons */
	static TYPE_AllowDarkModeForWindow fn_AllowDarkModeForWindow = 0;
	static TYPE_AllowDarkModeForWindowWithTelemetryId fn_AllowDarkModeForWindowWithTelemetryId = 0;
	static int isInitialized = 0;
	if (!isInitialized)
	{
		fn_AllowDarkModeForWindow = Locate_AllowDarkModeForWindow();
		fn_AllowDarkModeForWindowWithTelemetryId = Locate_AllowDarkModeForWindowWithTelemetryId();
		isInitialized = 1;
	}

	if (fn_AllowDarkModeForWindow)
	{
		jboolean rc = 0;
		OS_NATIVE_ENTER(env, that, AllowDarkModeForWindow_FUNC);
		rc = (jboolean)fn_AllowDarkModeForWindow((HWND)arg0, arg1);
		OS_NATIVE_EXIT(env, that, AllowDarkModeForWindow_FUNC);
		return rc;
	}

	// In Win11, 'AllowDarkModeForWindow' is a thin wrapper for 'AllowDarkModeForWindowWithTelemetryId'.
	// It's hard to verify the wrapper, but it's easy enough to verify the target.
	// For this reason, call 'AllowDarkModeForWindowWithTelemetryId' here.
	if (fn_AllowDarkModeForWindowWithTelemetryId)
	{
		jboolean rc = 0;
		OS_NATIVE_ENTER(env, that, AllowDarkModeForWindow_FUNC);
		rc = (jboolean)fn_AllowDarkModeForWindowWithTelemetryId((HWND)arg0, arg1, 0);
		OS_NATIVE_EXIT(env, that, AllowDarkModeForWindow_FUNC);
		return rc;
	}

	return 0;
}
#endif

BOOL Validate_SetPreferredAppMode(const BYTE* functionPtr)
{
#ifdef _M_X64
	/*
	 * This function is very simple, so validate entire body.
	 * The only thing we don't know is the variable address.
	 */
	const DWORD varOffset1 = *(const DWORD*)(functionPtr + 0x02);
	const DWORD varOffset2 = *(const DWORD*)(functionPtr + 0x08);
	if (varOffset1 != (varOffset2 + 6))
		return FALSE;

	return
		(functionPtr[0x00] == 0x8B) && (functionPtr[0x01] == 0x05) &&   // mov     eax,dword ptr [uxtheme!g_preferredAppMode]
		(functionPtr[0x06] == 0x87) && (functionPtr[0x07] == 0x0D) &&   // xchg    ecx,dword ptr [uxtheme!g_preferredAppMode]
		(functionPtr[0x0C] == 0xC3);                                    // ret
#else
	#error Unsupported processor type
#endif
}

typedef DWORD(WINAPI* TYPE_SetPreferredAppMode)(DWORD value);
TYPE_SetPreferredAppMode Locate_SetPreferredAppMode()
{
	const HMODULE hUxtheme = GetModuleHandle(L"uxtheme.dll");
	if (!hUxtheme)
		return 0;

	/*
	 * Function is only exported by ordinal.
	 * Hopefully one day Microsoft will finally export it by name.
	 */
	const BYTE* candidate = (const BYTE*)GetProcAddress(hUxtheme, MAKEINTRESOURCEA(135));
	if (!candidate)
		return 0;

	/*
	 * In next Windows version, some other function can end up having this ordinal.
	 * Compare function's code to known signature to make sure.
	 */
	if (!Validate_SetPreferredAppMode(candidate))
		return 0;

	return (TYPE_SetPreferredAppMode)candidate;
}

#ifndef NO_SetPreferredAppMode
JNIEXPORT jint JNICALL OS_NATIVE(SetPreferredAppMode)
(JNIEnv* env, jclass that, jint arg0)
{
	/* Suppress warnings about unreferenced parameters */
	(void)env;
	(void)that;

	/* Cache the search result for performance reasons */
	static TYPE_SetPreferredAppMode fn_SetPreferredAppMode = 0;
	static int isInitialized = 0;
	if (!isInitialized)
	{
		fn_SetPreferredAppMode = Locate_SetPreferredAppMode();
		isInitialized = 1;
	}

	if (!fn_SetPreferredAppMode)
		return 0;

	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetPreferredAppMode_FUNC);
	rc = (jint)fn_SetPreferredAppMode(arg0);
	OS_NATIVE_EXIT(env, that, SetPreferredAppMode_FUNC);
	return rc;
}
#endif

jboolean isDarkThemeAvailable() {
    if (!Locate_SetPreferredAppMode())
        return JNI_FALSE;

    if (!Locate_AllowDarkModeForWindow() && !Locate_AllowDarkModeForWindowWithTelemetryId())
        return JNI_FALSE;

    return JNI_TRUE;
}

#ifndef NO_IsDarkModeAvailable
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDarkModeAvailable)
(JNIEnv* env, jclass that)
{
	/* Suppress warnings about unreferenced parameters */
	(void)env;
	(void)that;

	/* Cache the search result for performance reasons */
	static jboolean isAvailable = 0;
	static int isInitialized = 0;
	if (!isInitialized)
	{
		isAvailable = isDarkThemeAvailable();
		isInitialized = 1;
	}

	return isAvailable;
}
#endif

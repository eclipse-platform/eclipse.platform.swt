/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_win32_OS_##func

__declspec(dllexport) HRESULT DllGetVersion(DLLVERSIONINFO *dvi);
HRESULT DllGetVersion(DLLVERSIONINFO *dvi)
{
     dvi->dwMajorVersion = SWT_VERSION / 1000;
     dvi->dwMinorVersion = SWT_VERSION % 1000;
     dvi->dwBuildNumber = SWT_BUILD_NUM;
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
	NATIVE_ENTER(env, that, "GetLibraryHandle\n")
	rc = (jint)g_hInstance;
	NATIVE_EXIT(env, that, "GetLibraryHandle\n")
	return rc;
}
#endif

#ifndef NO_Call
JNIEXPORT jint JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DLLVERSIONINFO _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "Call\n")
	if (arg1) lparg1 = getDLLVERSIONINFOFields(env, arg1, &_arg1);
	rc = (jint)((DLLGETVERSIONPROC)arg0)(lparg1);
	if (arg1) setDLLVERSIONINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "Call\n")
	return rc;
}
#endif

#ifndef NO_EnumDisplayMonitors
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumDisplayMonitors)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "EnumDisplayMonitors\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	//rc = (jboolean)EnumDisplayMonitors((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
	{
		/*
		*  EnumDisplayMonitors is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "EnumDisplayMonitors"))) {
			rc = (jboolean)(fp)((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
		}
    }
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "EnumDisplayMonitors\n")
	return rc;
}
#endif

#ifndef NO_EnumSystemLanguageGroupsA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnumSystemLanguageGroupsA\n")
	//rc = (jboolean)EnumSystemLanguageGroupsA((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
	{
		/*
		*  EnumSystemLanguageGroupsA is a Win2000 or later specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("kernel32.dll")) && (fp=GetProcAddress(hm, "EnumSystemLanguageGroupsA"))) {
			rc = (jboolean)(fp)((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	NATIVE_EXIT(env, that, "EnumSystemLanguageGroupsA\n")
	return rc;
}
#endif

#ifndef NO_EnumSystemLanguageGroupsW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnumSystemLanguageGroupsW\n")
	//rc = (jboolean)EnumSystemLanguageGroupsW((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
	{
		/*
		*  EnumSystemLanguageGroupsW is a Win2000 or later specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("kernel32.dll")) && (fp=GetProcAddress(hm, "EnumSystemLanguageGroupsW"))) {
			rc = (jboolean)(fp)((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	NATIVE_EXIT(env, that, "EnumSystemLanguageGroupsW\n")
	return rc;
}
#endif

#ifndef NO_GetLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetLayout\n")
	//rc = (jint)GetLayout((HDC)arg0);
	{
		/*
		*  GetLayout is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
    	if ((hm=GetModuleHandle("gdi32.dll")) && (fp=GetProcAddress(hm, "GetLayout"))) {
    		rc = (jint)(fp)((HDC)arg0);
    	}
    }
	NATIVE_EXIT(env, that, "GetLayout\n")
	return rc;
}
#endif

#ifndef NO_GetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMenuInfo\n")
	if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1);
	//rc = (jboolean)GetMenuInfo((HMENU)arg0, lparg1);
	{
		/*
		*  GetMenuInfo is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "GetMenuInfo"))) {
        	rc = (jboolean)(fp)((HMENU)arg0, lparg1);
        }
	}
	if (arg1) setMENUINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetMenuInfo\n")
	return rc;
}
#endif

#ifndef NO_GetMonitorInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMonitorInfoA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMonitorInfoA\n")
	if (arg1) lparg1 = getMONITORINFOFields(env, arg1, &_arg1);
	//rc = (jboolean)GetMonitorInfoA((HMONITOR)arg0, (LPMONITORINFO)lparg1);
	{
		/*
		*  GetMonitorInfoA is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "GetMonitorInfoA"))) {
			rc = (jboolean)(fp)((HMONITOR)arg0, (LPMONITORINFO)lparg1);
		}
	}
	if (arg1) setMONITORINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetMonitorInfoA\n")
	return rc;
}
#endif

#ifndef NO_GetMonitorInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMonitorInfoW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMonitorInfoW\n")
	if (arg1) lparg1 = getMONITORINFOFields(env, arg1, &_arg1);
	//rc = (jboolean)GetMonitorInfoW((HMONITOR)arg0, (LPMONITORINFO)lparg1);
	{
		/*
		*  GetMonitorInfoW is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "GetMonitorInfoW"))) {
			rc = (jboolean)(fp)((HMONITOR)arg0, (LPMONITORINFO)lparg1);
		}
	}
	if (arg1) setMONITORINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetMonitorInfoW\n")
	return rc;
}
#endif

#ifndef NO_GradientFill
JNIEXPORT jboolean JNICALL OS_NATIVE(GradientFill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "GradientFill\n")
	//rc = (jboolean)GradientFill((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
	{
		/*
		*  GradientFill is a Win2000 and Win98 specific call
		*  If you link it into swt.dll, a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if (!(hm = GetModuleHandle("msimg32.dll"))) hm = LoadLibrary("msimg32.dll");
		if (hm && (fp = GetProcAddress(hm, "GradientFill"))) {
			rc = (jboolean)fp((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
		}
	}
	NATIVE_EXIT(env, that, "GradientFill\n")
	return rc;
}
#endif

#ifndef NO_IsPPC
JNIEXPORT jboolean JNICALL OS_NATIVE(IsPPC)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsPPC\n")
#ifdef WIN32_PLATFORM_PSPC
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	NATIVE_EXIT(env, that, "IsPPC\n")
	return rc;
}
#endif

#ifndef NO_IsSP
JNIEXPORT jboolean JNICALL OS_NATIVE(IsSP)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsSP\n")
#ifdef WIN32_PLATFORM_WFSP
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	NATIVE_EXIT(env, that, "IsSP\n")
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
	NATIVE_ENTER(env, that, "SendMessageW__II_3I_3I\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
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
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageW__II_3I_3I\n")
	return rc;
}
#endif

#ifndef NO_SetLayout
JNIEXPORT jint JNICALL OS_NATIVE(SetLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetLayout\n")
	//rc = (jint)SetLayout((HDC)arg0, (DWORD)arg1);
	{
		/*
		*  SetLayout is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("gdi32.dll")) && (fp=GetProcAddress(hm, "SetLayout"))) {
			rc = (jint)(fp)((HDC)arg0, (DWORD)arg1);
		}
	}
	NATIVE_EXIT(env, that, "SetLayout\n")
	return rc;
}
#endif

#ifndef NO_SetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetMenuInfo\n")
	if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1);
	//rc = (jboolean)SetMenuInfo((HMENU)arg0, lparg1);
	{
		/*
		*  SetMenuInfo is a Win2000 and Win98 specific call
		*  If you link it into swt.dll a system modal entry point not found dialog will
		*  appear as soon as swt.dll is loaded. Here we check for the entry point and
		*  only do the call if it exists.
		*/
		HMODULE hm;
		FARPROC fp;
		if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "SetMenuInfo"))) {
			rc = (jboolean)(fp)((HMENU)arg0, lparg1);
		}
	}
	NATIVE_EXIT(env, that, "SetMenuInfo\n")
	return rc;
}
#endif

#ifndef NO_VtblCall
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "VtblCall\n")
	typedef jint (STDMETHODCALLTYPE *P_OLE_FN_2)(jint, jint);
	{
		P_OLE_FN_2 fn;
		fn = (P_OLE_FN_2)(*(int **)arg1)[arg0];
		rc = fn(arg1, arg2);
    }
	NATIVE_EXIT(env, that, "VtblCall\n")
	return rc;
}
#endif

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * SWT OS natives implementation.
 */ 

#include "swt.h"
#include "structs.h"

#ifdef _WIN32_WCE
#define NO_AbortDoc
#define NO_ActivateKeyboardLayout
#define NO_Arc
#define NO_CallNextHookEx
#define NO_CallWindowProcA
#define NO_CharLowerA
#define NO_CharUpperA
#define NO_ChooseColorA
#define NO_ChooseFontA
#define NO_ChooseFontW
#define NO_CopyImage
#define NO_CreateAcceleratorTableA
#define NO_CreateCursor
#define NO_CreateDCA
#define NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2
#define NO_CreateFontIndirectA__I
#define NO_CreateWindowExA
#define NO_DefWindowProcA
#define NO_DestroyCursor
#define NO_DispatchMessageA
#define NO_DragDetect
#define NO_DragFinish
#define NO_DragQueryFileA
#define NO_DragQueryFileW
#define NO_DrawStateA
#define NO_DrawStateW
#define NO_DrawTextA
#define NO_EnableScrollBar
#define NO_EndDoc
#define NO_EndPage
#define NO_EnumFontFamiliesA
#define NO_EnumSystemLanguageGroupsA
#define NO_EnumSystemLocalesA
#define NO_ExpandEnvironmentStringsW
#define NO_ExpandEnvironmentStringsA
#define NO_ExtTextOutA
#define NO_ExtractIconExA
#define NO_FindWindowA
#define NO_GetCharABCWidthsA
#define NO_GetCharABCWidthsW
#define NO_GetCharWidthA
#define NO_GetCharWidthW
#define NO_GetCharacterPlacementA
#define NO_GetCharacterPlacementW
#define NO_GetClassInfoA
#define NO_GetClipboardFormatNameA
#define NO_GetDIBColorTable
#define NO_GetDIBits
#define NO_GetFileTitleA
#define NO_GetFileTitleW
#define NO_GetFontLanguageInfo
#define NO_GetIconInfo
#define NO_GetKeyboardLayout
#define NO_GetKeyboardLayoutList
#define NO_GetKeyboardState
#define NO_GetLastActivePopup
#define NO_GetLocaleInfoA
#define NO_GetMenu
#define NO_GetMenuDefaultItem
#define NO_GetMenuInfo
#define NO_GetMenuItemCount
#define NO_GetMenuItemInfoA
#define NO_GetMessageA
#define NO_GetMessageTime
#define NO_GetModuleHandleA
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
#define NO_GetOpenFileNameA
#define NO_GetProfileStringA
#define NO_GetProfileStringW
#define NO_GetROP2
#define NO_GetSaveFileNameA
#define NO_GetTextCharset
#define NO_GetTextExtentPoint32A
#define NO_GetTextMetricsA
#define NO_GetVersionExA
#define NO_GetWindowLongA
#define NO_GetWindowPlacement
#define NO_GetWindowTextA
#define NO_GetWindowTextLengthA
#define NO_GradientFill
#define NO_ImmGetCompositionFontA
#define NO_ImmGetCompositionStringA
#define NO_ImmSetCompositionFontA
#define NO_InsertMenuA
#define NO_InsertMenuItemA
#define NO_InsertMenuItemW
#define NO_InvalidateRgn
#define NO_IsIconic
#define NO_IsZoomed
#define NO_LineTo
#define NO_LoadBitmapA
#define NO_LoadCursorA
#define NO_LoadIconA
#define NO_LoadImageA
#define NO_LoadLibraryA
#define NO_MapVirtualKeyA
#define NO_MessageBoxA
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
#define NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
#define NO_MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
#define NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
#define NO_MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
#define NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II
#define NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I
#define NO_MoveToEx
#define NO_OleInitialize
#define NO_OleUninitialize
#define NO_PeekMessageA
#define NO_Pie
#define NO_PostMessageA
#define NO_PostThreadMessageA
#define NO_PrintDlgA
#define NO_PrintDlgW
#define NO_RedrawWindow
#define NO_RegEnumKeyExA
#define NO_RegOpenKeyExA
#define NO_RegQueryInfoKeyA
#define NO_RegQueryValueExA
#define NO_RegisterClassA
#define NO_RegisterClipboardFormatA
#define NO_SHBrowseForFolderA
#define NO_SHBrowseForFolderW
#define NO_SHGetPathFromIDListA
#define NO_SHGetPathFromIDListW
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
#define NO_SendMessageA__IIII
#define NO_SendMessageA__III_3S
#define NO_SendMessageA__III_3I
#define NO_SendMessageA__III_3B
#define NO_SendMessageA__II_3II
#define NO_SendMessageA__II_3I_3I
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
#define NO_SetDIBColorTable
#define NO_SetMenu
#define NO_SetMenuDefaultItem
#define NO_SetMenuInfo
#define NO_SetMenuItemInfoA
#define NO_SetStretchBltMode
#define NO_SetTextAlign
#define NO_SetWindowLongA
#define NO_SetWindowPlacement
#define NO_SetWindowTextA
#define NO_SetWindowsHookExA
#define NO_SetWindowsHookExW
#define NO_ShellExecuteExA
#define NO_ShowOwnedPopups
#define NO_ShowScrollBar
#define NO_StartDocA
#define NO_StartDocW
#define NO_StartPage
#define NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
#define NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
#define NO_SystemParametersInfoA__II_3II
#define NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
#define NO_ToAscii
#define NO_ToUnicode
#define NO_TrackMouseEvent
#define NO_TranslateAcceleratorA
#define NO_UnhookWindowsHookEx
#define NO_UnregisterClassA
#define NO_VkKeyScanA
#define NO_VkKeyScanW
#define NO_WaitMessage
#define NO_WindowFromDC

#define ChooseColorW ChooseColor
#define CHOOSECOLORW CHOOSECOLOR
#define LPCHOOSECOLORW LPCHOOSECOLOR
#define FONTENUMPROCW FONTENUMPROC
#define ShellExecuteExW ShellExecuteEx
#define LPSHELLEXECUTEINFOW LPSHELLEXECUTEINFO
#endif /* _WIN32_WCE */

#ifndef WIN32_PLATFORM_HPC2000
#define NO_CommandBar_1AddAdornments
#define NO_CommandBar_1Create
#define NO_CommandBar_1DrawMenuBar
#define NO_CommandBar_1Height
#define NO_CommandBar_1InsertMenubarEx
#define NO_CommandBar_1Show
#endif /* WIN32_PLATFORM_HPC2000 */

#if !defined(WIN32_PLATFORM_PSPC) && !defined(WIN32_PLATFORM_WFSP)
#define NO_SHCreateMenuBar
#define NO_SHSetAppKeyWndAssoc
#endif /* WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP */

#ifdef WIN32_PLATFORM_WFSP
#define NO_CommDlgExtendedError
#define NO_ChooseColorW
#endif /* WIN32_PLATFORM_WFSP */

#ifndef WIN32_PLATFORM_PSPC
#define NO_SHHandleWMSettingChange
#endif /* WIN32_PLATFORM_PSPC */

#ifndef WIN32_PLATFORM_WFSP
#define NO_SHSendBackToFocusWindow
#endif /* WIN32_PLATFORM_WFSP */

#ifndef _WIN32_WCE
#define NO_CommandBar_1Destroy
#define NO_TransparentImage
#endif /* _WIN32_WCE */

/* Export the dll version info call */

__declspec(dllexport) HRESULT DllGetVersion(DLLVERSIONINFO *dvi);

HRESULT DllGetVersion(DLLVERSIONINFO *dvi)
{
     dvi->dwMajorVersion = SWT_VERSION / 1000;
     dvi->dwMinorVersion = SWT_VERSION % 1000;
     dvi->dwBuildNumber = SWT_BUILD_NUM;
     dvi->dwPlatformID = DLLVER_PLATFORM_WINDOWS;
     return 1;
}

/* Cache the handle to the library */
HINSTANCE g_hInstance = NULL;

BOOL WINAPI DllMain(HANDLE hInstDLL, DWORD dwReason, LPVOID lpvReserved)
{
	if (dwReason == DLL_PROCESS_ATTACH) {
		if (g_hInstance == NULL) g_hInstance = hInstDLL;
	}
	return TRUE;
}

/* Natives */

#define OS_NATIVE(method) Java_org_eclipse_swt_internal_win32_OS_##method

#ifndef NO_AbortDoc
JNIEXPORT jint JNICALL OS_NATIVE(AbortDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("AbortDoc\n")

	return (jint)AbortDoc((HDC)arg0);
}
#endif /* NO_AbortDoc */
#ifndef NO_ActivateKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(ActivateKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ActivateKeyboardLayout\n")

	return (jint)ActivateKeyboardLayout((HKL)arg0, arg1);
}
#endif /* NO_ActivateKeyboardLayout */

#ifndef NO_AdjustWindowRectEx
JNIEXPORT jboolean JNICALL OS_NATIVE(AdjustWindowRectEx)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jboolean arg2, jint arg3)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("AdjustWindowRectEx\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	rc = (jboolean)AdjustWindowRectEx(lparg0, arg1, arg2, arg3);
	if (arg0) setRECTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_AdjustWindowRectEx */

#ifndef NO_Arc
JNIEXPORT jboolean JNICALL OS_NATIVE(Arc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("Arc\n")

	return (jboolean)Arc((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}
#endif /* NO_Arc */

#ifndef NO_BeginDeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(BeginDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BeginDeferWindowPos\n")

	return (jint)BeginDeferWindowPos(arg0);
}
#endif /* NO_BeginDeferWindowPos */

#ifndef NO_BeginPaint
JNIEXPORT jint JNICALL OS_NATIVE(BeginPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("BeginPaint\n")

	if (arg1) lparg1 = &_arg1;
	rc = (jint)BeginPaint((HWND)arg0, lparg1);
	if (arg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_BeginPaint */

#ifndef NO_BitBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(BitBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("BitBlt\n")

	return (jboolean)BitBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8);
}
#endif /* NO_BitBlt */

#ifndef NO_BringWindowToTop
JNIEXPORT jboolean JNICALL OS_NATIVE(BringWindowToTop)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BringWindowToTop\n")

	return (jboolean)BringWindowToTop((HWND)arg0);
}
#endif /* NO_BringWindowToTop */

#ifndef NO_Call
JNIEXPORT jint JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DLLVERSIONINFO _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("Call\n")

	if (arg1) lparg1 = getDLLVERSIONINFOFields(env, arg1, &_arg1);
	rc = (jint)((DLLGETVERSIONPROC)arg0)(lparg1);
	if (arg1) setDLLVERSIONINFOFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_Call */

#ifndef NO_CallNextHookEx
JNIEXPORT jint JNICALL OS_NATIVE(CallNextHookEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CallNextHookEx\n")

	return (jint)CallNextHookEx((HHOOK)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_CallNextHookEx */

#ifndef NO_CallWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CallWindowProcA\n")

	return (jint)CallWindowProcA((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
}
#endif /* NO_CallWindowProcA */

#ifndef NO_CallWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CallWindowProcW\n")

	return (jint)CallWindowProcW((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
}
#endif /* NO_CallWindowProcW */

#ifndef NO_CharLowerA
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharLowerA\n")

	return (jshort)CharLowerA((LPSTR)arg0);
}
#endif /* NO_CharLowerA */

#ifndef NO_CharLowerW
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharLowerW\n")

	return (jshort)CharLowerW((LPWSTR)arg0);
}
#endif /* NO_CharLowerW */

#ifndef NO_CharUpperA
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharUpperA\n")

	return (jshort)CharUpperA((LPSTR)arg0);
}
#endif /* NO_CharUpperA */

#ifndef NO_CharUpperW
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharUpperW\n")

	return (jshort)CharUpperW((LPWSTR)arg0);
}
#endif /* NO_CharUpperW */

#ifndef NO_CheckMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(CheckMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CheckMenuItem\n")

	return (jboolean)CheckMenuItem((HMENU)arg0, (UINT)arg1, (UINT)arg2);
}
#endif /* NO_CheckMenuItem */

#ifndef NO_ChooseColorA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseColorA\n")

	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseColorA(lparg0);
	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ChooseColorA */

#ifndef NO_ChooseColorW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseColorW\n")

	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseColorW((LPCHOOSECOLORW)lparg0);
	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ChooseColorW */

#ifndef NO_ChooseFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseFontA\n")

	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseFontA(lparg0);
	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ChooseFontA */

#ifndef NO_ChooseFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseFontW\n")

	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseFontW((LPCHOOSEFONTW)lparg0);
	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ChooseFontW */

#ifndef NO_ClientToScreen
JNIEXPORT jboolean JNICALL OS_NATIVE(ClientToScreen)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ClientToScreen\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)ClientToScreen((HWND)arg0, lparg1);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ClientToScreen */

#ifndef NO_CloseClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(CloseClipboard)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CloseClipboard\n")

	return (jboolean)CloseClipboard();
}
#endif /* NO_CloseClipboard */

#ifndef NO_CombineRgn
JNIEXPORT jint JNICALL OS_NATIVE(CombineRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CombineRgn\n")

	return (jint)CombineRgn((HRGN)arg0, (HRGN)arg1, (HRGN)arg2, arg3);
}
#endif /* NO_CombineRgn */

#ifndef NO_CommDlgExtendedError
JNIEXPORT jint JNICALL OS_NATIVE(CommDlgExtendedError)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CommDlgExtendedError\n")

	return (jint)CommDlgExtendedError();
}
#endif /* NO_CommDlgExtendedError */

#ifndef NO_CommandBar_1AddAdornments
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1AddAdornments)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CommandBar_1AddAdornments\n")

	return (jboolean)CommandBar_AddAdornments((HWND)arg0, arg1, arg2);
}
#endif /* NO_CommandBar_1AddAdornments */

#ifndef NO_CommandBar_1Create
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CommandBar_1Create\n")

	return (jint)CommandBar_Create((HINSTANCE)arg0, (HWND)arg1, arg2);
}
#endif /* NO_CommandBar_1Create */

#ifndef NO_CommandBar_1Destroy
JNIEXPORT void JNICALL OS_NATIVE(CommandBar_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CommandBar_1Destroy\n")

	CommandBar_Destroy((HWND)arg0);
}
#endif /* NO_CommandBar_1Destroy */

#ifndef NO_CommandBar_1DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CommandBar_1DrawMenuBar\n")

	return (jboolean)CommandBar_DrawMenuBar((HWND)arg0, (WORD)arg1);
}
#endif /* NO_CommandBar_1DrawMenuBar */

#ifndef NO_CommandBar_1Height
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CommandBar_1Height\n")

	return (jint)CommandBar_Height((HWND)arg0);
}
#endif /* NO_CommandBar_1Height */

#ifndef NO_CommandBar_1InsertMenubarEx
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1InsertMenubarEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CommandBar_1InsertMenubarEx\n")

	return (jboolean)CommandBar_InsertMenubarEx((HWND)arg0, (HINSTANCE)arg1, (LPTSTR)arg2, (WORD)arg3);
}
#endif /* NO_CommandBar_1InsertMenubarEx */

#ifndef NO_CommandBar_1Show
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1Show)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("CommandBar_1Show\n")

	return (jboolean)CommandBar_Show((HWND)arg0, (BOOL)arg1);
}
#endif /* NO_CommandBar_1Show */

#ifndef NO_CopyImage
JNIEXPORT jint JNICALL OS_NATIVE(CopyImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CopyImage\n")

	return (jint)CopyImage((HANDLE)arg0, arg1, arg2, arg3, arg4);
}
#endif /* NO_CopyImage */

#ifndef NO_CreateAcceleratorTableA
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateAcceleratorTableA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)CreateAcceleratorTableA((LPACCEL)lparg0, arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_CreateAcceleratorTableA */

#ifndef NO_CreateAcceleratorTableW
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableW)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateAcceleratorTableW\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)CreateAcceleratorTableW((LPACCEL)lparg0, arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_CreateAcceleratorTableW */

#ifndef NO_CreateBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;

	DEBUG_CALL("CreateBitmap\n")

	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)CreateBitmap(arg0, arg1, arg2, arg3, (CONST VOID *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_CreateBitmap */

#ifndef NO_CreateCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(CreateCaret)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CreateCaret\n")

	return (jboolean)CreateCaret((HWND)arg0, (HBITMAP)arg1, arg2, arg3);
}
#endif /* NO_CreateCaret */

#ifndef NO_CreateCompatibleBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CreateCompatibleBitmap\n")

	return (jint)CreateCompatibleBitmap((HDC)arg0, arg1, arg2);
}
#endif /* NO_CreateCompatibleBitmap */

#ifndef NO_CreateCompatibleDC
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateCompatibleDC\n")

	return (jint)CreateCompatibleDC((HDC)arg0);
}
#endif /* NO_CreateCompatibleDC */

#ifndef NO_CreateCursor
JNIEXPORT jint JNICALL OS_NATIVE(CreateCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc;

	DEBUG_CALL("CreateCursor\n")

	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)CreateCursor((HINSTANCE)arg0, arg1, arg2, arg3, arg4, (CONST VOID *)lparg5, (CONST VOID *)lparg6);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_CreateCursor */

#ifndef NO_CreateDCA
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CreateDCA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CreateDCA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)arg2, (CONST DEVMODE *)arg3);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CreateDCA */

#ifndef NO_CreateDCW
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CreateDCW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)CreateDCW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)arg2, (CONST DEVMODEW *)arg3);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CreateDCW */

#ifndef NO_CreateDIBSection
JNIEXPORT jint JNICALL OS_NATIVE(CreateDIBSection)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("CreateDIBSection\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateDIBSection((HDC)arg0, (BITMAPINFO *)lparg1, arg2, (VOID **)lparg3, (HANDLE)arg4, arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_CreateDIBSection */

#ifndef NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTA _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2\n")

	if (arg0) lparg0 = getLOGFONTAFields(env, arg0, &_arg0);
	rc = (jint)CreateFontIndirectA(lparg0);
	if (arg0) setLOGFONTAFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2 */

#ifndef NO_CreateFontIndirectA__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateFontIndirectA__I\n")

	return (jint)CreateFontIndirectA((LPLOGFONTA)arg0);
}
#endif /* NO_CreateFontIndirectA__I */

#ifndef NO_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONT_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONT_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTW _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONT_2\n")

	if (arg0) lparg0 = getLOGFONTWFields(env, arg0, &_arg0);
	rc = (jint)CreateFontIndirectW(lparg0);
	if (arg0) setLOGFONTWFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONT_2 */

#ifndef NO_CreateFontIndirectW__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateFontIndirectW__I\n")

	return (jint)CreateFontIndirectW((LPLOGFONTW)arg0);
}
#endif /* NO_CreateFontIndirectW__I */

#ifndef NO_CreateIconIndirect
JNIEXPORT jint JNICALL OS_NATIVE(CreateIconIndirect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ICONINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateIconIndirect\n")

	if (arg0) lparg0 = getICONINFOFields(env, arg0, &_arg0);
	rc = (jint)CreateIconIndirect(lparg0);
	if (arg0) setICONINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_CreateIconIndirect */

#ifndef NO_CreateMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreateMenu)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CreateMenu\n")

	return (jint)CreateMenu();
}
#endif /* NO_CreateMenu */

#ifndef NO_CreatePalette
JNIEXPORT jint JNICALL OS_NATIVE(CreatePalette)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreatePalette\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)CreatePalette((LOGPALETTE *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_CreatePalette */

#ifndef NO_CreatePatternBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreatePatternBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreatePatternBrush\n")

	return (jint)CreatePatternBrush((HBITMAP)arg0);
}
#endif /* NO_CreatePatternBrush */

#ifndef NO_CreatePen
JNIEXPORT jint JNICALL OS_NATIVE(CreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CreatePen\n")

	return (jint)CreatePen(arg0, arg1, (COLORREF)arg2);
}
#endif /* NO_CreatePen */

#ifndef NO_CreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupMenu)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CreatePopupMenu\n")

	return (jint)CreatePopupMenu();
}
#endif /* NO_CreatePopupMenu */

#ifndef NO_CreateRectRgn
JNIEXPORT jint JNICALL OS_NATIVE(CreateRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CreateRectRgn\n")

	return (jint)CreateRectRgn(arg0, arg1, arg2, arg3);
}
#endif /* NO_CreateRectRgn */

#ifndef NO_CreateSolidBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreateSolidBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateSolidBrush\n")

	return (jint)CreateSolidBrush((COLORREF)arg0);
}
#endif /* NO_CreateSolidBrush */

#ifndef NO_CreateWindowExA
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;

	DEBUG_CALL("CreateWindowExA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11);
	rc = (jint)CreateWindowExA(arg0, (LPSTR)lparg1, lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11);
	return rc;
}
#endif /* NO_CreateWindowExA */

#ifndef NO_CreateWindowExW
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;

	DEBUG_CALL("CreateWindowExW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11);
	rc = (jint)CreateWindowExW(arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11);
	return rc;
}
#endif /* NO_CreateWindowExW */

#ifndef NO_DefWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("DefWindowProcA\n")

	return (jint)DefWindowProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_DefWindowProcA */

#ifndef NO_DefWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("DefWindowProcW\n")

	return (jint)DefWindowProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_DefWindowProcW */

#ifndef NO_DeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(DeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	DEBUG_CALL("DeferWindowPos\n")

	return (jint)DeferWindowPos((HDWP)arg0, (HWND)arg1, (HWND)arg2, arg3, arg4, arg5, arg6, arg7);
}
#endif /* NO_DeferWindowPos */

#ifndef NO_DeleteDC
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DeleteDC\n")

	return (jboolean)DeleteDC((HDC)arg0);
}
#endif /* NO_DeleteDC */

#ifndef NO_DeleteMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("DeleteMenu\n")

	return (jboolean)DeleteMenu((HMENU)arg0, arg1, arg2);
}
#endif /* NO_DeleteMenu */

#ifndef NO_DeleteObject
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DeleteObject\n")

	return (jboolean)DeleteObject((HGDIOBJ)arg0);
}
#endif /* NO_DeleteObject */

#ifndef NO_DestroyAcceleratorTable
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyAcceleratorTable)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyAcceleratorTable\n")

	return (jboolean)DestroyAcceleratorTable((HACCEL)arg0);
}
#endif /* NO_DestroyAcceleratorTable */

#ifndef NO_DestroyCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCaret)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("DestroyCaret\n")

	return (jboolean)DestroyCaret();
}
#endif /* NO_DestroyCaret */

#ifndef NO_DestroyCursor
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyCursor\n")

	return (jboolean)DestroyCursor((HCURSOR)arg0);
}
#endif /* NO_DestroyCursor */

#ifndef NO_DestroyIcon
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyIcon)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyIcon\n")

	return (jboolean)DestroyIcon((HICON)arg0);
}
#endif /* NO_DestroyIcon */

#ifndef NO_DestroyMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyMenu\n")

	return (jboolean)DestroyMenu((HMENU)arg0);
}
#endif /* NO_DestroyMenu */

#ifndef NO_DestroyWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyWindow\n")

	return (jboolean)DestroyWindow((HWND)arg0);
}
#endif /* NO_DestroyWindow */

#ifndef NO_DispatchMessageA
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("DispatchMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	return (jint)DispatchMessageA(lparg0);
}
#endif /* NO_DispatchMessageA */

#ifndef NO_DispatchMessageW
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("DispatchMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	return (jint)DispatchMessageW(lparg0);
}
#endif /* NO_DispatchMessageW */

#ifndef NO_DragDetect
JNIEXPORT jboolean JNICALL OS_NATIVE(DragDetect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;

	DEBUG_CALL("DragDetect\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	return (jboolean)DragDetect((HWND)arg0, *lparg1);
}
#endif /* NO_DragDetect */

#ifndef NO_DragFinish
JNIEXPORT void JNICALL OS_NATIVE(DragFinish)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DragFinish\n")

	DragFinish((HDROP)arg0);
}
#endif /* NO_DragFinish */

#ifndef NO_DragQueryFileA
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("DragQueryFileA\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)DragQueryFileA((HDROP)arg0, arg1, (LPTSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_DragQueryFileA */

#ifndef NO_DragQueryFileW
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("DragQueryFileW\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)DragQueryFileW((HDROP)arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_DragQueryFileW */

#ifndef NO_DrawEdge
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawEdge)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawEdge\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawEdge((HDC)arg0, lparg1, arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_DrawEdge */

#ifndef NO_DrawFocusRect
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFocusRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawFocusRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawFocusRect((HDC)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_DrawFocusRect */

#ifndef NO_DrawFrameControl
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFrameControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawFrameControl\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawFrameControl((HDC)arg0, lparg1, arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_DrawFrameControl */

#ifndef NO_DrawIconEx
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawIconEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("DrawIconEx\n")

	return (jboolean)DrawIconEx((HDC)arg0, arg1, arg2, (HICON)arg3, arg4, arg5, arg6, (HBRUSH)arg7, arg8);
}
#endif /* NO_DrawIconEx */

#ifndef NO_DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DrawMenuBar\n")

	return (jboolean)DrawMenuBar((HWND)arg0);
}
#endif /* NO_DrawMenuBar */

#ifndef NO_DrawStateA
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	DEBUG_CALL("DrawStateA\n")

	return (jboolean)DrawStateA((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
}
#endif /* NO_DrawStateA */

#ifndef NO_DrawStateW
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	DEBUG_CALL("DrawStateW\n")

	return (jboolean)DrawStateW((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
}
#endif /* NO_DrawStateW */

#ifndef NO_DrawTextA
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DrawTextA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)DrawTextA((HDC)arg0, (LPSTR)lparg1, arg2, lparg3, arg4);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) setRECTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_DrawTextA */

#ifndef NO_DrawTextW
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jchar *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DrawTextW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)DrawTextW((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3, arg4);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg3) setRECTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_DrawTextW */

#ifndef NO_Ellipse
JNIEXPORT jboolean JNICALL OS_NATIVE(Ellipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("Ellipse\n")

	return (jboolean)Ellipse((HDC)arg0, arg1, arg2, arg3, arg4);
}
#endif /* NO_Ellipse */

#ifndef NO_EnableMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("EnableMenuItem\n")

	return (jboolean)EnableMenuItem((HMENU)arg0, arg1, arg2);
}
#endif /* NO_EnableMenuItem */

#ifndef NO_EnableScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("EnableScrollBar\n")

	return (jboolean)EnableScrollBar((HWND)arg0, arg1, arg2);
}
#endif /* NO_EnableScrollBar */

#ifndef NO_EnableWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("EnableWindow\n")

	return (jboolean)EnableWindow((HWND)arg0, arg1);
}
#endif /* NO_EnableWindow */

#ifndef NO_EndDeferWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(EndDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndDeferWindowPos\n")

	return (jboolean)EndDeferWindowPos((HDWP)arg0);
}
#endif /* NO_EndDeferWindowPos */

#ifndef NO_EndDoc
JNIEXPORT jint JNICALL OS_NATIVE(EndDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndDoc\n")

	return (jint)EndDoc((HDC)arg0);
}
#endif /* NO_EndDoc */

#ifndef NO_EndPage
JNIEXPORT jint JNICALL OS_NATIVE(EndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndPage\n")

	return (jint)EndPage((HDC)arg0);
}
#endif /* NO_EndPage */

#ifndef NO_EndPaint
JNIEXPORT jint JNICALL OS_NATIVE(EndPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;

	DEBUG_CALL("EndPaint\n")

	if (arg1) lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1);
	return (jint)EndPaint((HWND)arg0, lparg1);
}
#endif /* NO_EndPaint */

#ifndef NO_EnumFontFamiliesA
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("EnumFontFamiliesA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)EnumFontFamiliesA((HDC)arg0, (LPSTR)lparg1, (FONTENUMPROC)arg2, (LPARAM)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_EnumFontFamiliesA */

#ifndef NO_EnumFontFamiliesW
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("EnumFontFamiliesW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)EnumFontFamiliesW((HDC)arg0, (LPCWSTR)lparg1, (FONTENUMPROCW)arg2, (LPARAM)arg3);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_EnumFontFamiliesW */

#ifndef NO_EnumSystemLanguageGroupsA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("EnumSystemLanguageGroupsA\n")

	/* SPECIAL */
    /*
    *  EnumSystemLanguageGroupsA is a Win2000 or later specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("kernel32.dll")) && (fp=GetProcAddress(hm, "EnumSystemLanguageGroupsA"))) {

        return (jboolean)(fp)((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
//		return (jboolean)EnumSystemLanguageGroupsA((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
	}
	return 0;
}
#endif /* NO_EnumSystemLanguageGroupsA */

#ifndef NO_EnumSystemLanguageGroupsW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("EnumSystemLanguageGroupsW\n")

	/* SPECIAL */
    /*
    *  EnumSystemLanguageGroupsW is a Win2000 or later specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("kernel32.dll")) && (fp=GetProcAddress(hm, "EnumSystemLanguageGroupsW"))) {

        return (jboolean)(fp)((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
//		return (jboolean)EnumSystemLanguageGroupsW((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
	}
	return 0;
}
#endif /* NO_EnumSystemLanguageGroupsW */

#ifndef NO_EnumSystemLocalesA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EnumSystemLocalesA\n")

	return (jboolean)EnumSystemLocalesA((LOCALE_ENUMPROCA)arg0, arg1);
}
#endif /* NO_EnumSystemLocalesA */

#ifndef NO_EnumSystemLocalesW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EnumSystemLocalesW\n")

	return (jboolean)EnumSystemLocalesW((LOCALE_ENUMPROCW)arg0, arg1);
}
#endif /* NO_EnumSystemLocalesW */

#ifndef NO_EqualRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	RECT _arg1, *lparg1=NULL;

	DEBUG_CALL("EqualRect\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	return (jboolean)EqualRect((CONST RECT *)lparg0, (CONST RECT *)lparg1);
}
#endif /* NO_EqualRect */

#ifndef NO_EqualRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EqualRgn\n")

	return (jboolean)EqualRgn((HRGN)arg0, (HRGN)arg1);
}
#endif /* NO_EqualRgn */

#ifndef NO_ExpandEnvironmentStringsA
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("ExpandEnvironmentStringsA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)ExpandEnvironmentStringsA(lparg0, lparg1, arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_ExpandEnvironmentStringsA */

#ifndef NO_ExpandEnvironmentStringsW
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("ExpandEnvironmentStringsW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)ExpandEnvironmentStringsW(lparg0, lparg1, arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_ExpandEnvironmentStringsW */

#ifndef NO_ExtTextOutA
JNIEXPORT jboolean JNICALL OS_NATIVE(ExtTextOutA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	RECT _arg4, *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("ExtTextOutA\n")

	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jboolean)ExtTextOutA((HDC)arg0, arg1, arg2, arg3, lparg4, (LPSTR)lparg5, arg6, (CONST INT *)lparg7);
	if (arg4) setRECTFields(env, arg4, lparg4);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_ExtTextOutA */

#ifndef NO_ExtTextOutW
JNIEXPORT jboolean JNICALL OS_NATIVE(ExtTextOutW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jcharArray arg5, jint arg6, jintArray arg7)
{
	RECT _arg4, *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("ExtTextOutW\n")

	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jboolean)ExtTextOutW((HDC)arg0, arg1, arg2, arg3, lparg4, (LPWSTR)lparg5, arg6, (CONST INT*)lparg7);
	if (arg4) setRECTFields(env, arg4, lparg4);
	if (arg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_ExtTextOutW */

#ifndef NO_ExtractIconExA
JNIEXPORT jint JNICALL OS_NATIVE(ExtractIconExA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintArray arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("ExtractIconExA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)ExtractIconExA((LPSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_ExtractIconExA */

#ifndef NO_ExtractIconExW
JNIEXPORT jint JNICALL OS_NATIVE(ExtractIconExW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintArray arg2, jintArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("ExtractIconExW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)ExtractIconExW((LPWSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_ExtractIconExW */

#ifndef NO_FillRect
JNIEXPORT jint JNICALL OS_NATIVE(FillRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FillRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jint)FillRect((HDC)arg0, lparg1, (HBRUSH)arg2);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_FillRect */

#ifndef NO_FindWindowA
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FindWindowA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FindWindowA((LPSTR)lparg0, (LPSTR)lparg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_FindWindowA */

#ifndef NO_FindWindowW
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FindWindowW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)FindWindowW((LPWSTR)lparg0, (LPWSTR)lparg1);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_FindWindowW */

#ifndef NO_FreeLibrary
JNIEXPORT jboolean JNICALL OS_NATIVE(FreeLibrary)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("FreeLibrary\n")

	return (jboolean)FreeLibrary((HMODULE)arg0);
}
#endif /* NO_FreeLibrary */

#ifndef NO_GetACP
JNIEXPORT jint JNICALL OS_NATIVE(GetACP)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetACP\n")

	return (jint)GetACP();
}
#endif /* NO_GetACP */

#ifndef NO_GetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetActiveWindow)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetActiveWindow\n")

	return (jint)GetActiveWindow();
}
#endif /* NO_GetActiveWindow */

#ifndef NO_GetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(GetBkColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetBkColor\n")

	return (jint)GetBkColor((HDC)arg0);
}
#endif /* NO_GetBkColor */

#ifndef NO_GetCapture
JNIEXPORT jint JNICALL OS_NATIVE(GetCapture)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCapture\n")

	return (jint)GetCapture();
}
#endif /* NO_GetCapture */

#ifndef NO_GetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCaretPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetCaretPos\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jboolean)GetCaretPos(lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetCaretPos */

#ifndef NO_GetCharABCWidthsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetCharABCWidthsA\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jboolean)GetCharABCWidthsA((HDC)arg0, arg1, arg2, (LPABC)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetCharABCWidthsA */

#ifndef NO_GetCharABCWidthsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetCharABCWidthsW\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jboolean)GetCharABCWidthsW((HDC)arg0, arg1, arg2, (LPABC)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetCharABCWidthsW */

#ifndef NO_GetCharWidthA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetCharWidthA\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jboolean)GetCharWidthA((HDC)arg0, arg1, arg2, (LPINT)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetCharWidthA */

#ifndef NO_GetCharWidthW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetCharWidthW\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jboolean)GetCharWidthW((HDC)arg0, arg1, arg2, (LPINT)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetCharWidthW */

#ifndef NO_GetCharacterPlacementA
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetCharacterPlacementA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4);
	rc = (jint)GetCharacterPlacementA((HDC)arg0, (LPSTR)lparg1, arg2, arg3, lparg4, arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_GetCharacterPlacementA */

#ifndef NO_GetCharacterPlacementW
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jchar *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetCharacterPlacementW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4);
	rc = (jint)GetCharacterPlacementW((HDC)arg0, (LPWSTR)lparg1, arg2, arg3, (LPGCP_RESULTSW)lparg4, arg5);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_GetCharacterPlacementW */

#ifndef NO_GetClassInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetClassInfoA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2);
	rc = (jboolean)GetClassInfoA((HINSTANCE)arg0, (LPSTR)lparg1, lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetClassInfoA */

#ifndef NO_GetClassInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jobject arg2)
{
	jchar *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetClassInfoW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2);
	rc = (jboolean)GetClassInfoW((HINSTANCE)arg0, (LPWSTR)lparg1, (LPWNDCLASSW)lparg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetClassInfoW */

#ifndef NO_GetClientRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClientRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetClientRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetClientRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetClientRect */

#ifndef NO_GetClipBox
JNIEXPORT jint JNICALL OS_NATIVE(GetClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetClipBox\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jint)GetClipBox((HDC)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetClipBox */

#ifndef NO_GetClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetClipRgn\n")

	return (jint)GetClipRgn((HDC)arg0, (HRGN)arg1);
}
#endif /* NO_GetClipRgn */

#ifndef NO_GetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardData)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetClipboardData\n")

	return (jint)GetClipboardData(arg0);
}
#endif /* NO_GetClipboardData */

#ifndef NO_GetClipboardFormatNameA
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetClipboardFormatNameA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetClipboardFormatNameA(arg0, lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetClipboardFormatNameA */

#ifndef NO_GetClipboardFormatNameW
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetClipboardFormatNameW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)GetClipboardFormatNameW(arg0, (LPWSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetClipboardFormatNameW */

#ifndef NO_GetCurrentObject
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetCurrentObject\n")

	return (jint)GetCurrentObject((HDC)arg0, arg1);
}
#endif /* NO_GetCurrentObject */

#ifndef NO_GetCurrentProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentProcessId)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentProcessId\n")

	return (jint)GetCurrentProcessId();
}
#endif /* NO_GetCurrentProcessId */

#ifndef NO_GetCurrentThreadId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentThreadId)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentThreadId\n")

	return (jint)GetCurrentThreadId();
}
#endif /* NO_GetCurrentThreadId */

#ifndef NO_GetCursor
JNIEXPORT jint JNICALL OS_NATIVE(GetCursor)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCursor\n")

	return (jint)GetCursor();
}
#endif /* NO_GetCursor */

#ifndef NO_GetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCursorPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetCursorPos\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jboolean)GetCursorPos(lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetCursorPos */

#ifndef NO_GetDC
JNIEXPORT jint JNICALL OS_NATIVE(GetDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetDC\n")

	return (jint)GetDC((HWND)arg0);
}
#endif /* NO_GetDC */

#ifndef NO_GetDCEx
JNIEXPORT jint JNICALL OS_NATIVE(GetDCEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetDCEx\n")

	return (jint)GetDCEx((HWND)arg0, (HRGN)arg1, arg2);
}
#endif /* NO_GetDCEx */

#ifndef NO_GetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetDIBColorTable\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetDIBColorTable */

#ifndef NO_GetDIBits
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jint arg6)
{
	jbyte *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetDIBits\n")

	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	rc = (jint)GetDIBits((HDC)arg0, (HBITMAP)arg1, arg2, arg3, (LPVOID)arg4, (LPBITMAPINFO)lparg5, arg6);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_GetDIBits */

#ifndef NO_GetDesktopWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetDesktopWindow)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDesktopWindow\n")

	return (jint)GetDesktopWindow();
}
#endif /* NO_GetDesktopWindow */

#ifndef NO_GetDeviceCaps
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceCaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetDeviceCaps\n")

	return (jint)GetDeviceCaps((HDC)arg0, arg1);
}
#endif /* NO_GetDeviceCaps */

#ifndef NO_GetDialogBaseUnits
JNIEXPORT jint JNICALL OS_NATIVE(GetDialogBaseUnits)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDialogBaseUnits\n")

	return (jint)GetDialogBaseUnits();
}
#endif /* NO_GetDialogBaseUnits */

#ifndef NO_GetDlgItem
JNIEXPORT jint JNICALL OS_NATIVE(GetDlgItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetDlgItem\n")

	return (jint)GetDlgItem((HWND)arg0, arg1);
}
#endif /* NO_GetDlgItem */

#ifndef NO_GetDoubleClickTime
JNIEXPORT jint JNICALL OS_NATIVE(GetDoubleClickTime)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDoubleClickTime\n")

	return (jint)GetDoubleClickTime();
}
#endif /* NO_GetDoubleClickTime */

#ifndef NO_GetFileTitleA
JNIEXPORT jshort JNICALL OS_NATIVE(GetFileTitleA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jshort arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jshort rc;

	DEBUG_CALL("GetFileTitleA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jshort)GetFileTitleA((LPSTR)lparg0, (LPSTR)lparg1, arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetFileTitleA */

#ifndef NO_GetFileTitleW
JNIEXPORT jshort JNICALL OS_NATIVE(GetFileTitleW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jshort arg2)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jshort rc;

	DEBUG_CALL("GetFileTitleW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jshort)GetFileTitleW((LPWSTR)lparg0, (LPWSTR)lparg1, arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetFileTitleW */

#ifndef NO_GetFocus
JNIEXPORT jint JNICALL OS_NATIVE(GetFocus)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetFocus\n")

	return (jint)GetFocus();
}
#endif /* NO_GetFocus */

#ifndef NO_GetFontLanguageInfo
JNIEXPORT jint JNICALL OS_NATIVE(GetFontLanguageInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetFontLanguageInfo\n")

	return (jint)GetFontLanguageInfo((HDC)arg0);
}
#endif /* NO_GetFontLanguageInfo */

#ifndef NO_GetIconInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetIconInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ICONINFO _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetIconInfo\n")

	if (arg1) lparg1 = getICONINFOFields(env, arg1, &_arg1);
	rc = (jboolean)GetIconInfo((HICON)arg0, lparg1);
	if (arg1) setICONINFOFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetIconInfo */

#ifndef NO_GetKeyState
JNIEXPORT jshort JNICALL OS_NATIVE(GetKeyState)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetKeyState\n")

	return (jshort)GetKeyState(arg0);
}
#endif /* NO_GetKeyState */

#ifndef NO_GetKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetKeyboardLayout\n")

	return (jint)GetKeyboardLayout(arg0);
}
#endif /* NO_GetKeyboardLayout */

#ifndef NO_GetKeyboardLayoutList
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayoutList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetKeyboardLayoutList\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyboardLayoutList(arg0, (HKL FAR *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetKeyboardLayoutList */

#ifndef NO_GetKeyboardState
JNIEXPORT jboolean JNICALL OS_NATIVE(GetKeyboardState)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetKeyboardState\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jboolean)GetKeyboardState((PBYTE)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetKeyboardState */

#ifndef NO_GetLastActivePopup
JNIEXPORT jint JNICALL OS_NATIVE(GetLastActivePopup)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetLastActivePopup\n")

	return (jint)GetLastActivePopup((HWND)arg0);
}
#endif /* NO_GetLastActivePopup */

#ifndef NO_GetLastError
JNIEXPORT jint JNICALL OS_NATIVE(GetLastError)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetLastError\n")

	return (jint)GetLastError();
}
#endif /* NO_GetLastError */

#ifndef NO_GetLibraryHandle
JNIEXPORT jint JNICALL OS_NATIVE(GetLibraryHandle)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetLibraryHandle\n")

	return (jint)g_hInstance;
}
#endif /* NO_GetLibraryHandle */

#ifndef NO_GetLocaleInfoA
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetLocaleInfoA\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)GetLocaleInfoA(arg0, arg1, (LPSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetLocaleInfoA */

#ifndef NO_GetLocaleInfoW
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetLocaleInfoW\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)GetLocaleInfoW(arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetLocaleInfoW */

#ifndef NO_GetMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenu\n")

	return (jint)GetMenu((HWND)arg0);
}
#endif /* NO_GetMenu */

#ifndef NO_GetMenuDefaultItem
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetMenuDefaultItem\n")

	return (jint)GetMenuDefaultItem((HMENU)arg0, arg1, arg2);
}
#endif /* NO_GetMenuDefaultItem */

#ifndef NO_GetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = (jboolean)FALSE;
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("GetMenuInfo\n")

	/* SPECIAL */
    /*
    *  GetMenuInfo is a Win2000 and Win98 specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "GetMenuInfo"))) {
    
		if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1);
		
        rc = (jboolean) (fp)((HMENU)arg0, lparg1);
//		rc = (jboolean)GetMenuInfo(arg0, lparg1);

		if (arg1) setMENUINFOFields(env, arg1, lparg1);
	}

	return rc;
}
#endif /* NO_GetMenuInfo */

#ifndef NO_GetMenuItemCount
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenuItemCount\n")

	return (jint)GetMenuItemCount((HMENU)arg0);
}
#endif /* NO_GetMenuItemCount */

#ifndef NO_GetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetMenuItemInfoA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)GetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_GetMenuItemInfoA */

#ifndef NO_GetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetMenuItemInfoW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)GetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_GetMenuItemInfoW */

#ifndef NO_GetMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)GetMessageA(lparg0, (HWND)arg1, arg2, arg3);
	if (arg0) setMSGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetMessageA */

#ifndef NO_GetMessagePos
JNIEXPORT jint JNICALL OS_NATIVE(GetMessagePos)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMessagePos\n")

	return (jint)GetMessagePos();
}
#endif /* NO_GetMessagePos */

#ifndef NO_GetMessageTime
JNIEXPORT jint JNICALL OS_NATIVE(GetMessageTime)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMessageTime\n")

	return (jint)GetMessageTime();
}
#endif /* NO_GetMessageTime */

#ifndef NO_GetMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)GetMessageW(lparg0, (HWND)arg1, arg2, arg3);
	if (arg0) setMSGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetMessageW */

#ifndef NO_GetModuleHandleA
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("GetModuleHandleA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)GetModuleHandleA((LPSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetModuleHandleA */

#ifndef NO_GetModuleHandleW
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;

	DEBUG_CALL("GetModuleHandleW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)GetModuleHandleW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetModuleHandleW */

#ifndef NO_GetNearestPaletteIndex
JNIEXPORT jint JNICALL OS_NATIVE(GetNearestPaletteIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetNearestPaletteIndex\n")

	return (jint)GetNearestPaletteIndex((HPALETTE)arg0, (COLORREF)arg1);
}
#endif /* NO_GetNearestPaletteIndex */

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONT _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2\n")

	if (arg2) lparg2 = getLOGFONTAFields(env, arg2, &_arg2);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGFONTAFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2 */

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")

	if (arg2) lparg2 = getBITMAPFields(env, arg2, &_arg2);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setBITMAPFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2 */

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")

	if (arg2) lparg2 = getDIBSECTIONFields(env, arg2, &_arg2);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setDIBSECTIONFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2 */

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")

	if (arg2) lparg2 = getLOGPENFields(env, arg2, &_arg2);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGPENFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2 */

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")

	if (arg2) lparg2 = getLOGBRUSHFields(env, arg2, &_arg2);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGBRUSHFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2 */

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")

	if (arg2) lparg2 = getBITMAPFields(env, arg2, &_arg2);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setBITMAPFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2 */

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")

	if (arg2) lparg2 = getLOGBRUSHFields(env, arg2, &_arg2);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGBRUSHFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2 */

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONT_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONTW _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONT_2\n")

	if (arg2) lparg2 = getLOGFONTWFields(env, arg2, &_arg2);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGFONTWFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONT_2 */

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")

	if (arg2) lparg2 = getDIBSECTIONFields(env, arg2, &_arg2);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setDIBSECTIONFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2 */

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")

	if (arg2) lparg2 = getLOGPENFields(env, arg2, &_arg2);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGPENFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2 */

#ifndef NO_GetOpenFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetOpenFileNameA\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetOpenFileNameA(lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetOpenFileNameA */

#ifndef NO_GetOpenFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetOpenFileNameW\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetOpenFileNameW((LPOPENFILENAMEW)lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetOpenFileNameW */

#ifndef NO_GetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetPaletteEntries\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetPaletteEntries((HPALETTE)arg0, arg1, arg2, (LPPALETTEENTRY)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetPaletteEntries */

#ifndef NO_GetParent
JNIEXPORT jint JNICALL OS_NATIVE(GetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetParent\n")

	return (jint)GetParent((HWND)arg0);
}
#endif /* NO_GetParent */

#ifndef NO_GetPixel
JNIEXPORT jint JNICALL OS_NATIVE(GetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetPixel\n")

	return (jint)GetPixel((HDC)arg0, arg1, arg2);
}
#endif /* NO_GetPixel */

#ifndef NO_GetProcAddress
JNIEXPORT jint JNICALL OS_NATIVE(GetProcAddress)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetProcAddress\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetProcAddress((HMODULE)arg0, (LPCTSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetProcAddress */

#ifndef NO_GetProcessHeap
JNIEXPORT jint JNICALL OS_NATIVE(GetProcessHeap)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetProcessHeap\n")

	return (jint)GetProcessHeap();
}
#endif /* NO_GetProcessHeap */

#ifndef NO_GetProfileStringA
JNIEXPORT jint JNICALL OS_NATIVE(GetProfileStringA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetProfileStringA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetProfileStringA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)lparg2, (LPSTR)lparg3, arg4);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetProfileStringA */

#ifndef NO_GetProfileStringW
JNIEXPORT jint JNICALL OS_NATIVE(GetProfileStringW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jcharArray arg2, jcharArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetProfileStringW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)GetProfileStringW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)lparg2, (LPWSTR)lparg3, arg4);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetProfileStringW */

#ifndef NO_GetROP2
JNIEXPORT jint JNICALL OS_NATIVE(GetROP2)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetROP2\n")

	return (jint)GetROP2((HDC)arg0);
}
#endif /* NO_GetROP2 */

#ifndef NO_GetRegionData
JNIEXPORT jint JNICALL OS_NATIVE(GetRegionData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetRegionData\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetRegionData((HRGN)arg0, arg1, (RGNDATA *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetRegionData */

#ifndef NO_GetRgnBox
JNIEXPORT jint JNICALL OS_NATIVE(GetRgnBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetRgnBox\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jint)GetRgnBox((HRGN)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetRgnBox */

#ifndef NO_GetSaveFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetSaveFileNameA\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetSaveFileNameA(lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetSaveFileNameA */

#ifndef NO_GetSaveFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetSaveFileNameW\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetSaveFileNameW((LPOPENFILENAMEW)lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetSaveFileNameW */

#ifndef NO_GetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetScrollInfo\n")

	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2);
	rc = (jboolean)GetScrollInfo((HWND)arg0, arg1, lparg2);
	if (arg2) setSCROLLINFOFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_GetScrollInfo */

#ifndef NO_GetStockObject
JNIEXPORT jint JNICALL OS_NATIVE(GetStockObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetStockObject\n")

	return (jint)GetStockObject(arg0);
}
#endif /* NO_GetStockObject */

#ifndef NO_GetSysColor
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSysColor\n")

	return (jint)GetSysColor(arg0);
}
#endif /* NO_GetSysColor */

#ifndef NO_GetSysColorBrush
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColorBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSysColorBrush\n")

	return (jint)GetSysColorBrush(arg0);
}
#endif /* NO_GetSysColorBrush */

#ifndef NO_GetSystemMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMenu)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("GetSystemMenu\n")

	return (jint)GetSystemMenu((HWND)arg0, arg1);
}
#endif /* NO_GetSystemMenu */

#ifndef NO_GetSystemMetrics
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMetrics)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSystemMetrics\n")

	return (jint)GetSystemMetrics(arg0);
}
#endif /* NO_GetSystemMetrics */

#ifndef NO_GetTextCharset
JNIEXPORT jint JNICALL OS_NATIVE(GetTextCharset)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetTextCharset\n")

	return (jint)GetTextCharset((HDC)arg0);
}
#endif /* NO_GetTextCharset */

#ifndef NO_GetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(GetTextColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetTextColor\n")

	return (jint)GetTextColor((HDC)arg0);
}
#endif /* NO_GetTextColor */

#ifndef NO_GetTextExtentPoint32A
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32A)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3)
{
	jbyte *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextExtentPoint32A\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getSIZEFields(env, arg3, &_arg3);
	rc = (jboolean)GetTextExtentPoint32A((HDC)arg0, (LPSTR)lparg1, arg2, lparg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) setSIZEFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_GetTextExtentPoint32A */

#ifndef NO_GetTextExtentPoint32W
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32W)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3)
{
	jchar *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextExtentPoint32W\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getSIZEFields(env, arg3, &_arg3);
	rc = (jboolean)GetTextExtentPoint32W((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg3) setSIZEFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_GetTextExtentPoint32W */

#ifndef NO_GetTextMetricsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICA _arg1={0}, *lparg1=NULL; /* SPECIAL */
	jboolean rc;

	DEBUG_CALL("GetTextMetricsA\n")

	if (arg1) lparg1 = &_arg1;
	rc = (jboolean)GetTextMetricsA((HDC)arg0, lparg1);
	if (arg1) setTEXTMETRICAFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetTextMetricsA */

#ifndef NO_GetTextMetricsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICW _arg1={0}, *lparg1=NULL; /* SPECIAL */
	jboolean rc;

	DEBUG_CALL("GetTextMetricsW\n")

	if (arg1) lparg1 = &_arg1;
	rc = (jboolean)GetTextMetricsW((HDC)arg0, lparg1);
	if (arg1) setTEXTMETRICWFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetTextMetricsW */

#ifndef NO_GetTickCount
JNIEXPORT jint JNICALL OS_NATIVE(GetTickCount)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetTickCount\n")

	return (jint)GetTickCount();
}
#endif /* NO_GetTickCount */

#ifndef NO_GetUpdateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetUpdateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetUpdateRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetUpdateRect((HWND)arg0, (LPRECT)lparg1, (BOOL)arg2);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetUpdateRect */

#ifndef NO_GetUpdateRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetUpdateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("GetUpdateRgn\n")

	return (jint)GetUpdateRgn((HWND)arg0, (HRGN)arg1, arg2);
}
#endif /* NO_GetUpdateRgn */

#ifndef NO_GetVersionExA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOA _arg0={0}, *lparg0=NULL; /* SPECIAL */
	jboolean rc;

	DEBUG_CALL("GetVersionExA\n")

	if (arg0) lparg0 = getOSVERSIONINFOAFields(env, arg0, &_arg0);
	rc = (jboolean)GetVersionExA(lparg0);
	if (arg0) setOSVERSIONINFOAFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetVersionExA */

#ifndef NO_GetVersionExW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOW _arg0={0}, *lparg0=NULL; /* SPECIAL */
	jboolean rc;

	DEBUG_CALL("GetVersionExW\n")

	if (arg0) lparg0 = getOSVERSIONINFOWFields(env, arg0, &_arg0);
	rc = (jboolean)GetVersionExW(lparg0);
	if (arg0) setOSVERSIONINFOWFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_GetVersionExW */

#ifndef NO_GetWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindow\n")

	return (jint)GetWindow((HWND)arg0, arg1);
}
#endif /* NO_GetWindow */

#ifndef NO_GetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindowLongA\n")

	return (jint)GetWindowLongA((HWND)arg0, arg1);
}
#endif /* NO_GetWindowLongA */

#ifndef NO_GetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindowLongW\n")

	return (jint)GetWindowLongW((HWND)arg0, arg1);
}
#endif /* NO_GetWindowLongW */

#ifndef NO_GetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetWindowPlacement\n")

	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1);
	rc = (jboolean)GetWindowPlacement((HWND)arg0, lparg1);
	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetWindowPlacement */

#ifndef NO_GetWindowRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetWindowRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetWindowRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetWindowRect */

#ifndef NO_GetWindowTextA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetWindowTextA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowTextA((HWND)arg0, (LPSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetWindowTextA */

#ifndef NO_GetWindowTextLengthA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthA)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowTextLengthA\n")

	return (jint)GetWindowTextLengthA((HWND)arg0);
}
#endif /* NO_GetWindowTextLengthA */

#ifndef NO_GetWindowTextLengthW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthW)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowTextLengthW\n")

	return (jint)GetWindowTextLengthW((HWND)arg0);
}
#endif /* NO_GetWindowTextLengthW */

#ifndef NO_GetWindowTextW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetWindowTextW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowTextW((HWND)arg0, (LPWSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetWindowTextW */

#ifndef NO_GetWindowThreadProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowThreadProcessId)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetWindowThreadProcessId\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowThreadProcessId((HWND)arg0, (LPDWORD)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetWindowThreadProcessId */

#ifndef NO_GlobalAlloc
JNIEXPORT jint JNICALL OS_NATIVE(GlobalAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GlobalAlloc\n")

	return (jint)GlobalAlloc(arg0, arg1);
}
#endif /* NO_GlobalAlloc */

#ifndef NO_GlobalFree
JNIEXPORT jint JNICALL OS_NATIVE(GlobalFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalFree\n")

	return (jint)GlobalFree((HANDLE)arg0);
}
#endif /* NO_GlobalFree */

#ifndef NO_GlobalLock
JNIEXPORT jint JNICALL OS_NATIVE(GlobalLock)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalLock\n")

	return (jint)GlobalLock((HANDLE)arg0);
}
#endif /* NO_GlobalLock */

#ifndef NO_GlobalSize
JNIEXPORT jint JNICALL OS_NATIVE(GlobalSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalSize\n")

	return (jint)GlobalSize((HANDLE)arg0);
}
#endif /* NO_GlobalSize */

#ifndef NO_GlobalUnlock
JNIEXPORT jboolean JNICALL OS_NATIVE(GlobalUnlock)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalUnlock\n")

	return (jboolean)GlobalUnlock((HANDLE)arg0);
}
#endif /* NO_GlobalUnlock */

#ifndef NO_GradientFill
JNIEXPORT jboolean JNICALL OS_NATIVE(GradientFill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    HMODULE hm;
    FARPROC fp;
 
	DEBUG_CALL("GradientFill\n")

	/* SPECIAL */
    /*
    *  GradientFill is a Win2000 and Win98 specific call
    *  If you link it into swt.dll, a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if (!(hm = GetModuleHandle("msimg32.dll"))) hm = LoadLibrary("msimg32.dll");
    if (hm && (fp = GetProcAddress(hm, "GradientFill"))) {
//		return (jboolean)GradientFill((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
		return (jboolean)fp(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	return (jboolean)FALSE;
}
#endif /* NO_GradientFill */

#ifndef NO_HeapAlloc
JNIEXPORT jint JNICALL OS_NATIVE(HeapAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HeapAlloc\n")

	return (jint)HeapAlloc((HANDLE)arg0, arg1, arg2);
}
#endif /* NO_HeapAlloc */

#ifndef NO_HeapFree
JNIEXPORT jboolean JNICALL OS_NATIVE(HeapFree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HeapFree\n")

	return (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
}
#endif /* NO_HeapFree */

#ifndef NO_HideCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(HideCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HideCaret\n")

	return (jboolean)HideCaret((HWND)arg0);
}
#endif /* NO_HideCaret */

#ifndef NO_ImageList_1Add
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_1Add\n")

	return (jint)ImageList_Add((HIMAGELIST)arg0, (HBITMAP)arg1, (HBITMAP)arg2);
}
#endif /* NO_ImageList_1Add */

#ifndef NO_ImageList_1AddMasked
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1AddMasked)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_1AddMasked\n")

	return (jint)ImageList_AddMasked((HIMAGELIST)arg0, (HBITMAP)arg1, (COLORREF)arg2);
}
#endif /* NO_ImageList_1AddMasked */

#ifndef NO_ImageList_1Create
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("ImageList_1Create\n")

	return (jint)ImageList_Create(arg0, arg1, arg2, arg3, arg4);
}
#endif /* NO_ImageList_1Create */

#ifndef NO_ImageList_1Destroy
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImageList_1Destroy\n")

	return (jboolean)ImageList_Destroy((HIMAGELIST)arg0);
}
#endif /* NO_ImageList_1Destroy */

#ifndef NO_ImageList_1GetIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_1GetIcon\n")

	return (jint)ImageList_GetIcon((HIMAGELIST)arg0, arg1, arg2);
}
#endif /* NO_ImageList_1GetIcon */

#ifndef NO_ImageList_1GetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1GetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("ImageList_1GetIconSize\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)ImageList_GetIconSize((HIMAGELIST)arg0, lparg1, lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_ImageList_1GetIconSize */

#ifndef NO_ImageList_1GetImageCount
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetImageCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImageList_1GetImageCount\n")

	return (jint)ImageList_GetImageCount((HIMAGELIST)arg0);
}
#endif /* NO_ImageList_1GetImageCount */

#ifndef NO_ImageList_1Remove
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImageList_1Remove\n")

	return (jboolean)ImageList_Remove((HIMAGELIST)arg0, arg1);
}
#endif /* NO_ImageList_1Remove */

#ifndef NO_ImageList_1Replace
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Replace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("ImageList_1Replace\n")

	return (jboolean)ImageList_Replace((HIMAGELIST)arg0, arg1, (HBITMAP)arg2, (HBITMAP)arg3);
}
#endif /* NO_ImageList_1Replace */

#ifndef NO_ImageList_1ReplaceIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1ReplaceIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_1ReplaceIcon\n")

	return (jint)ImageList_ReplaceIcon((HIMAGELIST)arg0, arg1, (HICON)arg2);
}
#endif /* NO_ImageList_1ReplaceIcon */

#ifndef NO_ImageList_1SetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1SetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_1SetIconSize\n")

	return (jboolean)ImageList_SetIconSize((HIMAGELIST)arg0, arg1, arg2);
}
#endif /* NO_ImageList_1SetIconSize */

#ifndef NO_ImmAssociateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmAssociateContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImmAssociateContext\n")

	return (jint)ImmAssociateContext((HWND)arg0, (HIMC)arg1);
}
#endif /* NO_ImmAssociateContext */

#ifndef NO_ImmCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmCreateContext)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ImmCreateContext\n")

	return (jint)ImmCreateContext();
}
#endif /* NO_ImmCreateContext */

#ifndef NO_ImmDestroyContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmDestroyContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmDestroyContext\n")

	return (jboolean)ImmDestroyContext((HIMC)arg0);
}
#endif /* NO_ImmDestroyContext */

#ifndef NO_ImmGetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmGetCompositionFontA\n")

	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	rc = (jboolean)ImmGetCompositionFontA((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTAFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ImmGetCompositionFontA */

#ifndef NO_ImmGetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmGetCompositionFontW\n")

	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	rc = (jboolean)ImmGetCompositionFontW((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTWFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ImmGetCompositionFontW */

#ifndef NO_ImmGetCompositionStringA
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("ImmGetCompositionStringA\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)ImmGetCompositionStringA((HIMC)arg0, arg1, (LPSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_ImmGetCompositionStringA */

#ifndef NO_ImmGetCompositionStringW
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("ImmGetCompositionStringW\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_ImmGetCompositionStringW */

#ifndef NO_ImmGetContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetContext\n")

	return (jint)ImmGetContext((HWND)arg0);
}
#endif /* NO_ImmGetContext */

#ifndef NO_ImmGetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("ImmGetConversionStatus\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)ImmGetConversionStatus((HIMC)arg0, lparg1, lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_ImmGetConversionStatus */

#ifndef NO_ImmGetDefaultIMEWnd
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetDefaultIMEWnd)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetDefaultIMEWnd\n")

	return (jint)ImmGetDefaultIMEWnd((HWND)arg0);
}
#endif /* NO_ImmGetDefaultIMEWnd */

#ifndef NO_ImmGetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetOpenStatus\n")

	return (jboolean)ImmGetOpenStatus((HIMC)arg0);
}
#endif /* NO_ImmGetOpenStatus */

#ifndef NO_ImmReleaseContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmReleaseContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImmReleaseContext\n")

	return (jboolean)ImmReleaseContext((HWND)arg0, (HIMC)arg1);
}
#endif /* NO_ImmReleaseContext */

#ifndef NO_ImmSetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;

	DEBUG_CALL("ImmSetCompositionFontA\n")

	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	return (jboolean)ImmSetCompositionFontA((HIMC)arg0, lparg1);
}
#endif /* NO_ImmSetCompositionFontA */

#ifndef NO_ImmSetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;

	DEBUG_CALL("ImmSetCompositionFontW\n")

	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	return (jboolean)ImmSetCompositionFontW((HIMC)arg0, lparg1);
}
#endif /* NO_ImmSetCompositionFontW */

#ifndef NO_ImmSetCompositionWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	COMPOSITIONFORM _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmSetCompositionWindow\n")

	if (arg1) lparg1 = getCOMPOSITIONFORMFields(env, arg1, &_arg1);
	rc = (jboolean)ImmSetCompositionWindow((HIMC)arg0, lparg1);
	if (arg1) setCOMPOSITIONFORMFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ImmSetCompositionWindow */

#ifndef NO_ImmSetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImmSetConversionStatus\n")

	return (jboolean)ImmSetConversionStatus((HIMC)arg0, arg1, arg2);
}
#endif /* NO_ImmSetConversionStatus */

#ifndef NO_ImmSetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("ImmSetOpenStatus\n")

	return (jboolean)ImmSetOpenStatus((HIMC)arg0, arg1);
}
#endif /* NO_ImmSetOpenStatus */

#ifndef NO_InitCommonControls
JNIEXPORT void JNICALL OS_NATIVE(InitCommonControls)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("InitCommonControls\n")

	InitCommonControls();
}
#endif /* NO_InitCommonControls */

#ifndef NO_InitCommonControlsEx
JNIEXPORT jboolean JNICALL OS_NATIVE(InitCommonControlsEx)
	(JNIEnv *env, jclass that, jobject arg0)
{
	INITCOMMONCONTROLSEX _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("InitCommonControlsEx\n")

	if (arg0) lparg0 = getINITCOMMONCONTROLSEXFields(env, arg0, &_arg0);
	rc = (jboolean)InitCommonControlsEx(lparg0);
	if (arg0) setINITCOMMONCONTROLSEXFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_InitCommonControlsEx */

#ifndef NO_InsertMenuA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuA\n")

	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jboolean)InsertMenuA((HMENU)arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_InsertMenuA */

#ifndef NO_InsertMenuItemA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuItemA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)InsertMenuItemA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_InsertMenuItemA */

#ifndef NO_InsertMenuItemW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuItemW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)InsertMenuItemW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_InsertMenuItemW */

#ifndef NO_InsertMenuW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuW\n")

	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jboolean)InsertMenuW((HMENU)arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_InsertMenuW */

#ifndef NO_InvalidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("InvalidateRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)InvalidateRect((HWND)arg0, lparg1, arg2);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_InvalidateRect */

#ifndef NO_InvalidateRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("InvalidateRgn\n")

	return (jboolean)InvalidateRgn((HWND)arg0, (HRGN)arg1, arg2);
}
#endif /* NO_InvalidateRgn */

#ifndef NO_IsDBCSLeadByte
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDBCSLeadByte)
	(JNIEnv *env, jclass that, jbyte arg0)
{
	DEBUG_CALL("IsDBCSLeadByte\n")

	return (jboolean)IsDBCSLeadByte(arg0);
}
#endif /* NO_IsDBCSLeadByte */

#ifndef NO_IsIconic
JNIEXPORT jboolean JNICALL OS_NATIVE(IsIconic)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsIconic\n")

	return (jboolean)IsIconic((HWND)arg0);
}
#endif /* NO_IsIconic */

#ifndef NO_IsPPC
/* SPECIAL */
JNIEXPORT jboolean JNICALL OS_NATIVE(IsPPC)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("IsPPC\n")
#ifdef WIN32_PLATFORM_PSPC
	return TRUE;
#else
	return FALSE;
#endif /* WIN32_PLATFORM_PSPC */
}
#endif /* NO_IsPPC */

#ifndef NO_IsSP
/* SPECIAL */
JNIEXPORT jboolean JNICALL OS_NATIVE(IsSP)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("IsSP\n")
#ifdef WIN32_PLATFORM_WFSP
	return TRUE;
#else
	return FALSE;
#endif /* WIN32_PLATFORM_WFSP */
}
#endif /* NO_IsSP */

#ifndef NO_IsWindowEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowEnabled\n")

	return (jboolean)IsWindowEnabled((HWND)arg0);
}
#endif /* NO_IsWindowEnabled */

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowVisible\n")

	return (jboolean)IsWindowVisible((HWND)arg0);
}
#endif /* NO_IsWindowVisible */

#ifndef NO_IsZoomed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsZoomed)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsZoomed\n")

	return (jboolean)IsZoomed((HWND)arg0);
}
#endif /* NO_IsZoomed */

#ifndef NO_KillTimer
JNIEXPORT jboolean JNICALL OS_NATIVE(KillTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("KillTimer\n")

	return (jboolean)KillTimer((HWND)arg0, arg1);
}
#endif /* NO_KillTimer */

#ifndef NO_LineTo
JNIEXPORT jboolean JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("LineTo\n")

	return (jboolean)LineTo((HDC)arg0, arg1, arg2);
}
#endif /* NO_LineTo */

#ifndef NO_LoadBitmapA
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadBitmapA\n")

	return (jint)LoadBitmapA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif /* NO_LoadBitmapA */

#ifndef NO_LoadBitmapW
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadBitmapW\n")

	return (jint)LoadBitmapW((HINSTANCE)arg0, (LPWSTR)arg1);
}
#endif /* NO_LoadBitmapW */

#ifndef NO_LoadCursorA
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadCursorA\n")

	return (jint)LoadCursorA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif /* NO_LoadCursorA */

#ifndef NO_LoadCursorW
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadCursorW\n")

	return (jint)LoadCursorW((HINSTANCE)arg0, (LPWSTR)arg1);
}
#endif /* NO_LoadCursorW */

#ifndef NO_LoadIconA
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadIconA\n")

	return (jint)LoadIconA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif /* NO_LoadIconA */

#ifndef NO_LoadIconW
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadIconW\n")

	return (jint)LoadIconW((HINSTANCE)arg0, (LPWSTR)arg1);
}
#endif /* NO_LoadIconW */

#ifndef NO_LoadImageA
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("LoadImageA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)LoadImageA((HINSTANCE)arg0, (LPSTR)lparg1, arg2, arg3, arg4, arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_LoadImageA */

#ifndef NO_LoadImageW
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("LoadImageW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)LoadImageW((HINSTANCE)arg0, (LPWSTR)lparg1, arg2, arg3, arg4, arg5);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_LoadImageW */

#ifndef NO_LoadLibraryA
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("LoadLibraryA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)LoadLibraryA((LPSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_LoadLibraryA */

#ifndef NO_LoadLibraryW
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;

	DEBUG_CALL("LoadLibraryW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)LoadLibraryW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_LoadLibraryW */

#ifndef NO_MapVirtualKeyA
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("MapVirtualKeyA\n")

	return (jint)MapVirtualKeyA(arg0, arg1);
}
#endif /* NO_MapVirtualKeyA */

#ifndef NO_MapVirtualKeyW
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("MapVirtualKeyW\n")

	return (jint)MapVirtualKeyW(arg0, arg1);
}
#endif /* NO_MapVirtualKeyW */

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I */

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	POINT _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I\n")

	if (arg2) lparg2 = getPOINTFields(env, arg2, &_arg2);
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
	if (arg2) setPOINTFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I */

#ifndef NO_MessageBeep
JNIEXPORT jboolean JNICALL OS_NATIVE(MessageBeep)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("MessageBeep\n")

	return (jboolean)MessageBeep(arg0);
}
#endif /* NO_MessageBeep */

#ifndef NO_MessageBoxA
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MessageBoxA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)MessageBoxA((HWND)arg0, (LPSTR)lparg1, (LPSTR)lparg2, arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_MessageBoxA */

#ifndef NO_MessageBoxW
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MessageBoxW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)MessageBoxW((HWND)arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_MessageBoxW */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTOOLBAR _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTOOLBARFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DROPFILES _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getDROPFILESFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) setDROPFILESFields(env, arg1, lparg1);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I */

#ifndef NO_MoveMemory___3DII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3DII)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jint arg1, jint arg2)
{
	jdouble *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3DII\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3DII */

#ifndef NO_MoveMemory___3FII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3FII)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3FII\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3FII */

#ifndef NO_MoveMemory___3SII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3SII)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3SII\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3SII */

#ifndef NO_MoveMemory__I_3DI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3DI)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3DI\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3DI */

#ifndef NO_MoveMemory__I_3FI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3FI)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3FI\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3FI */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMREBARCHEVRON _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMREBARCHEVRONFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setMSGFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHEADER _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMHEADERFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getNMTVCUSTOMDRAWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) setNMTVCUSTOMDRAWFields(env, arg1, lparg1);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getNMLVCUSTOMDRAWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) setNMLVCUSTOMDRAWFields(env, arg1, lparg1);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = getNMTVCUSTOMDRAWFields(env, arg0, &_arg0);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTVCUSTOMDRAWFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II */

#ifndef NO_MoveMemory__I_3SI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3SI\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3SI */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMLVCUSTOMDRAWFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHDR _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMHDRFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setMEASUREITEMSTRUCTFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II */

#ifndef NO_MoveMemory___3CII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3CII\n")
	DEBUG_CHECK_NULL(env, arg1)

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3CII */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLISTVIEW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMLISTVIEWFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HELPINFO _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setHELPINFOFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HDITEM _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setHDITEMFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DRAWITEMSTRUCT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setDRAWITEMSTRUCTFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	WINDOWPOS _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getWINDOWPOSFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	TRIVERTEX _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getTRIVERTEXFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	WINDOWPOS _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setWINDOWPOSFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II */

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TVITEM _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setTVITEMFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II */

#ifndef NO_MoveMemory___3BII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3BII\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3BII */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getMEASUREITEMSTRUCTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I */

#ifndef NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	ACCEL _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = getACCELFields(env, arg1, &_arg1);
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I */

#ifndef NO_MoveMemory___3III
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;

	DEBUG_CALL("MoveMemory___3III\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_MoveMemory___3III */

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GRADIENT_RECT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getGRADIENT_RECTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I */

#ifndef NO_MoveMemory__I_3II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3II\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3II */

#ifndef NO_MoveMemory__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3CI\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3CI */

#ifndef NO_MoveMemory__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("MoveMemory__I_3BI\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_MoveMemory__I_3BI */

#ifndef NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I */

#ifndef NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setLOGFONTAFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II */

#ifndef NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOA _arg1={0}, *lparg1=NULL; /* SPECIAL */

	DEBUG_CALL("MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getNMTTDISPINFOAFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I */

#ifndef NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOA _arg0={0}, *lparg0=NULL; /* SPECIAL */

	DEBUG_CALL("MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTTDISPINFOAFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II */

#ifndef NO_MoveMemoryW__Lorg_eclipse_swt_internal_win32_LOGFONT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryW__Lorg_eclipse_swt_internal_win32_LOGFONT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONTW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryW__Lorg_eclipse_swt_internal_win32_LOGFONT_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setLOGFONTWFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemoryW__Lorg_eclipse_swt_internal_win32_LOGFONT_2II */

#ifndef NO_MoveMemoryW__ILorg_eclipse_swt_internal_win32_LOGFONT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryW__ILorg_eclipse_swt_internal_win32_LOGFONT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONTW _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryW__ILorg_eclipse_swt_internal_win32_LOGFONT_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemoryW__ILorg_eclipse_swt_internal_win32_LOGFONT_2I */

#ifndef NO_MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOW _arg1={0}, *lparg1=NULL; /* SPECIAL */

	DEBUG_CALL("MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I\n")
	DEBUG_CHECK_NULL(env, arg0)
	
	if (arg1) lparg1 = getNMTTDISPINFOWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif /* NO_MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I */

#ifndef NO_MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOW _arg0={0}, *lparg0=NULL; /* SPECIAL */

	DEBUG_CALL("MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II\n")
	DEBUG_CHECK_NULL(env, arg1)
	
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTTDISPINFOWFields(env, arg0, lparg0);
}
#endif /* NO_MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II */

#ifndef NO_MoveToEx
JNIEXPORT jboolean JNICALL OS_NATIVE(MoveToEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("MoveToEx\n")

	return (jboolean)MoveToEx((HDC)arg0, arg1, arg2, (LPPOINT)arg3);
}
#endif /* NO_MoveToEx */

#ifndef NO_MsgWaitForMultipleObjectsEx
JNIEXPORT jint JNICALL OS_NATIVE(MsgWaitForMultipleObjectsEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("MsgWaitForMultipleObjectsEx\n")

	return (jint)MsgWaitForMultipleObjectsEx((DWORD)arg0, (LPHANDLE)arg1, (DWORD)arg2, (DWORD)arg3, (DWORD)arg4);
}
#endif /* NO_MsgWaitForMultipleObjectsEx */

#ifndef NO_MultiByteToWideChar__IIII_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__IIII_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jchar *lparg4=NULL;
	jint rc;

	DEBUG_CALL("MultiByteToWideChar__IIII_3CI\n")

	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)arg2, arg3, (LPWSTR)lparg4, arg5);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_MultiByteToWideChar__IIII_3CI */

#ifndef NO_MultiByteToWideChar__II_3BI_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__II_3BI_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg4=NULL;
	jint rc;

	DEBUG_CALL("MultiByteToWideChar__II_3BI_3CI\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)lparg2, arg3, (LPWSTR)lparg4, arg5);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_MultiByteToWideChar__II_3BI_3CI */

#ifndef NO_OleInitialize
JNIEXPORT jint JNICALL OS_NATIVE(OleInitialize)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("OleInitialize\n")

	return (jint)OleInitialize((LPVOID)arg0);
}
#endif /* NO_OleInitialize */

#ifndef NO_OleUninitialize
JNIEXPORT void JNICALL OS_NATIVE(OleUninitialize)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("OleUninitialize\n")

	OleUninitialize();
}
#endif /* NO_OleUninitialize */

#ifndef NO_OpenClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(OpenClipboard)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("OpenClipboard\n")

	return (jboolean)OpenClipboard((HWND)arg0);
}
#endif /* NO_OpenClipboard */

#ifndef NO_PatBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(PatBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("PatBlt\n")

	return (jboolean)PatBlt((HDC)arg0, arg1, arg2, arg3, arg4, arg5);
}
#endif /* NO_PatBlt */

#ifndef NO_PeekMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PeekMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)PeekMessageA(lparg0, (HWND)arg1, arg2, arg3, arg4);
	if (arg0) setMSGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PeekMessageA */

#ifndef NO_PeekMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PeekMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)PeekMessageW(lparg0, (HWND)arg1, arg2, arg3, arg4);
	if (arg0) setMSGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PeekMessageW */

#ifndef NO_Pie
JNIEXPORT jboolean JNICALL OS_NATIVE(Pie)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("Pie\n")

	return (jboolean)Pie((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}
#endif /* NO_Pie */

#ifndef NO_Polygon
JNIEXPORT jboolean JNICALL OS_NATIVE(Polygon)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("Polygon\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)Polygon((HDC)arg0, (CONST POINT *)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_Polygon */

#ifndef NO_Polyline
JNIEXPORT jboolean JNICALL OS_NATIVE(Polyline)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("Polyline\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)Polyline((HDC)arg0, (CONST POINT *)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_Polyline */

#ifndef NO_PostMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostMessageA\n")

	return (jboolean)PostMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_PostMessageA */

#ifndef NO_PostMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostMessageW\n")

	return (jboolean)PostMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_PostMessageW */

#ifndef NO_PostThreadMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostThreadMessageA\n")

	return (jboolean)PostThreadMessageA(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_PostThreadMessageA */

#ifndef NO_PostThreadMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostThreadMessageW\n")

	return (jboolean)PostThreadMessageW(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif /* NO_PostThreadMessageW */

#ifndef NO_PrintDlgA
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PrintDlgA\n")

	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0);
	rc = (jboolean)PrintDlgA(lparg0);
	if (arg0) setPRINTDLGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PrintDlgA */

#ifndef NO_PrintDlgW
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PrintDlgW\n")

	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0);
	rc = (jboolean)PrintDlgW((LPPRINTDLGW)lparg0);
	if (arg0) setPRINTDLGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PrintDlgW */

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("PtInRect\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)PtInRect(lparg0, *lparg1);
	if (arg0) setRECTFields(env, arg0, lparg0);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_PtInRect */

#ifndef NO_PtInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("PtInRegion\n")

	return (jboolean)PtInRegion((HRGN)arg0, arg1, arg2);
}
#endif /* NO_PtInRegion */

#ifndef NO_RealizePalette
JNIEXPORT jint JNICALL OS_NATIVE(RealizePalette)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RealizePalette\n")

	return (jint)RealizePalette((HDC)arg0);
}
#endif /* NO_RealizePalette */

#ifndef NO_RectInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("RectInRegion\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)RectInRegion((HRGN)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_RectInRegion */

#ifndef NO_Rectangle
JNIEXPORT jboolean JNICALL OS_NATIVE(Rectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("Rectangle\n")

	return (jboolean)Rectangle((HDC)arg0, arg1, arg2, arg3, arg4);
}
#endif /* NO_Rectangle */

#ifndef NO_RedrawWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(RedrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("RedrawWindow\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)RedrawWindow((HWND)arg0, lparg1, (HRGN)arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_RedrawWindow */

#ifndef NO_RegCloseKey
JNIEXPORT jint JNICALL OS_NATIVE(RegCloseKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RegCloseKey\n")

	return (jint)RegCloseKey((HKEY)arg0);
}
#endif /* NO_RegCloseKey */

#ifndef NO_RegEnumKeyExA
JNIEXPORT jint JNICALL OS_NATIVE(RegEnumKeyExA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4, jbyteArray arg5, jintArray arg6, jobject arg7)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg6=NULL;
	FILETIME _arg7, *lparg7=NULL;
	jint rc;

	DEBUG_CALL("RegEnumKeyExA\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7);
	rc = (jint)RegEnumKeyExA((HKEY)arg0, arg1, (LPSTR)lparg2, lparg3, lparg4, (LPSTR)lparg5, lparg6, lparg7);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) setFILETIMEFields(env, arg7, lparg7);
	return rc;
}
#endif /* NO_RegEnumKeyExA */

#ifndef NO_RegEnumKeyExW
JNIEXPORT jint JNICALL OS_NATIVE(RegEnumKeyExW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jintArray arg3, jintArray arg4, jcharArray arg5, jintArray arg6, jobject arg7)
{
	jchar *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	FILETIME _arg7, *lparg7=NULL;
	jint rc;

	DEBUG_CALL("RegEnumKeyExW\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7);
	rc = (jint)RegEnumKeyExW((HKEY)arg0, arg1, (LPWSTR)lparg2, lparg3, lparg4, (LPWSTR)lparg5, lparg6, lparg7);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) setFILETIMEFields(env, arg7, lparg7);
	return rc;
}
#endif /* NO_RegEnumKeyExW */

#ifndef NO_RegOpenKeyExA
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("RegOpenKeyExA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)RegOpenKeyExA((HKEY)arg0, (LPSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_RegOpenKeyExA */

#ifndef NO_RegOpenKeyExW
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jchar *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("RegOpenKeyExW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)RegOpenKeyExW((HKEY)arg0, (LPWSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_RegOpenKeyExW */

#ifndef NO_RegQueryInfoKeyA
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryInfoKeyA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jint arg11)
{
	jint *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint rc;

	DEBUG_CALL("RegQueryInfoKeyA\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	if (arg10) lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL);
	rc = (jint)RegQueryInfoKeyA((HKEY)arg0, (LPSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	return rc;
}
#endif /* NO_RegQueryInfoKeyA */

#ifndef NO_RegQueryInfoKeyW
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryInfoKeyW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jint arg11)
{
	jint *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint rc;

	DEBUG_CALL("RegQueryInfoKeyW\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	if (arg10) lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL);
	rc = (jint)RegQueryInfoKeyW((HKEY)arg0, (LPWSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	return rc;
}
#endif /* NO_RegQueryInfoKeyW */

#ifndef NO_RegQueryValueExA
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("RegQueryValueExA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_RegQueryValueExA */

#ifndef NO_RegQueryValueExW
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jintArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("RegQueryValueExW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_RegQueryValueExW */

#ifndef NO_RegisterClassA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClassA\n")

	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0);
	rc = (jint)RegisterClassA(lparg0);
	if (arg0) setWNDCLASSFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_RegisterClassA */

#ifndef NO_RegisterClassW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClassW\n")

	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0);
	rc = (jint)RegisterClassW((LPWNDCLASSW)lparg0);
	if (arg0) setWNDCLASSFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_RegisterClassW */

#ifndef NO_RegisterClipboardFormatA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClipboardFormatA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)RegisterClipboardFormatA((LPTSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_RegisterClipboardFormatA */

#ifndef NO_RegisterClipboardFormatW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClipboardFormatW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)RegisterClipboardFormatW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_RegisterClipboardFormatW */

#ifndef NO_ReleaseCapture
JNIEXPORT jboolean JNICALL OS_NATIVE(ReleaseCapture)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ReleaseCapture\n")

	return (jboolean)ReleaseCapture();
}
#endif /* NO_ReleaseCapture */

#ifndef NO_ReleaseDC
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseDC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ReleaseDC\n")

	return (jint)ReleaseDC((HWND)arg0, (HDC)arg1);
}
#endif /* NO_ReleaseDC */

#ifndef NO_RemoveMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(RemoveMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("RemoveMenu\n")

	return (jboolean)RemoveMenu((HMENU)arg0, arg1, arg2);
}
#endif /* NO_RemoveMenu */

#ifndef NO_RoundRect
JNIEXPORT jboolean JNICALL OS_NATIVE(RoundRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("RoundRect\n")

	return (jboolean)RoundRect((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
}
#endif /* NO_RoundRect */

#ifndef NO_SHBrowseForFolderA
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SHBrowseForFolderA\n")

	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0);
	rc = (jint)SHBrowseForFolderA(lparg0);
	if (arg0) setBROWSEINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_SHBrowseForFolderA */

#ifndef NO_SHBrowseForFolderW
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SHBrowseForFolderW\n")

	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0);
	rc = (jint)SHBrowseForFolderW((LPBROWSEINFOW)lparg0);
	if (arg0) setBROWSEINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_SHBrowseForFolderW */

#ifndef NO_SHCreateMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(SHCreateMenuBar)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHMENUBARINFO _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("SHCreateMenuBar\n")

	if (arg0) lparg0 = getSHMENUBARINFOFields(env, arg0, &_arg0);
	rc = (jboolean)SHCreateMenuBar((PSHMENUBARINFO)lparg0);
	if (arg0) setSHMENUBARINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_SHCreateMenuBar */

#ifndef NO_SHGetMalloc
JNIEXPORT jint JNICALL OS_NATIVE(SHGetMalloc)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SHGetMalloc\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)SHGetMalloc((LPMALLOC *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_SHGetMalloc */

#ifndef NO_SHGetPathFromIDListA
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SHGetPathFromIDListA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)SHGetPathFromIDListA((LPCITEMIDLIST)arg0, (LPSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_SHGetPathFromIDListA */

#ifndef NO_SHGetPathFromIDListW
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SHGetPathFromIDListW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jboolean)SHGetPathFromIDListW((LPCITEMIDLIST)arg0, (LPWSTR)lparg1);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_SHGetPathFromIDListW */

#ifndef NO_SHHandleWMSettingChange
JNIEXPORT jboolean JNICALL OS_NATIVE(SHHandleWMSettingChange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SHACTIVATEINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("SHHandleWMSettingChange\n")

	if (arg3) lparg3 = getSHACTIVATEINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SHHandleWMSettingChange((HWND)arg0, arg1, arg2, lparg3);
	if (arg3) setSHACTIVATEINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SHHandleWMSettingChange */

#ifndef NO_SHSendBackToFocusWindow
JNIEXPORT void JNICALL OS_NATIVE(SHSendBackToFocusWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SHSendBackToFocusWindow\n")

	SHSendBackToFocusWindow(arg0, arg1, arg2);
}
#endif /* NO_SHSendBackToFocusWindow */

#ifndef NO_SHSetAppKeyWndAssoc
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSetAppKeyWndAssoc)
	(JNIEnv *env, jclass that, jbyte arg0, jint arg1)
{
	DEBUG_CALL("SHSetAppKeyWndAssoc\n")

	return (jboolean)SHSetAppKeyWndAssoc((BYTE)arg0, (HWND)arg1);
}
#endif /* NO_SHSetAppKeyWndAssoc */

#ifndef NO_ScreenToClient
JNIEXPORT jboolean JNICALL OS_NATIVE(ScreenToClient)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ScreenToClient\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)ScreenToClient((HWND)arg0, lparg1);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ScreenToClient */

#ifndef NO_ScrollWindowEx
JNIEXPORT jint JNICALL OS_NATIVE(ScrollWindowEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jobject arg6, jint arg7)
{
	RECT _arg3, *lparg3=NULL;
	RECT _arg4, *lparg4=NULL;
	RECT _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("ScrollWindowEx\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6);
	rc = (jint)ScrollWindowEx((HWND)arg0, arg1, arg2, lparg3, lparg4, (HRGN)arg5, lparg6, arg7);
	if (arg3) setRECTFields(env, arg3, lparg3);
	if (arg4) setRECTFields(env, arg4, lparg4);
	if (arg6) setRECTFields(env, arg6, lparg6);
	return rc;
}
#endif /* NO_ScrollWindowEx */

#ifndef NO_SelectClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(SelectClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SelectClipRgn\n")

	return (jint)SelectClipRgn((HDC)arg0, (HRGN)arg1);
}
#endif /* NO_SelectClipRgn */

#ifndef NO_SelectObject
JNIEXPORT jint JNICALL OS_NATIVE(SelectObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SelectObject\n")

	return (jint)SelectObject((HDC)arg0, (HGDIOBJ)arg1);
}
#endif /* NO_SelectObject */

#ifndef NO_SelectPalette
JNIEXPORT jint JNICALL OS_NATIVE(SelectPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("SelectPalette\n")

	return (jint)SelectPalette((HDC)arg0, (HPALETTE)arg1, arg2);
}
#endif /* NO_SelectPalette */

#ifndef NO_SendMessageA__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__III_3I\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageA__III_3I */

#ifndef NO_SendMessageA__III_3B
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__III_3B\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageA__III_3B */

#ifndef NO_SendMessageA__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_SendMessageA__II_3II */

#ifndef NO_SendMessageA__II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__II_3I_3I\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageA__II_3I_3I */

#ifndef NO_SendMessageA__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SendMessageA__IIII\n")

	return (jint)SendMessageA((HWND)arg0, arg1, arg2, arg3);
}
#endif /* NO_SendMessageA__IIII */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")

	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")

	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")

	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setRECTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")

	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")

	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")

	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTCITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")

	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTOOLINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")

	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")

	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2 */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")

	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2 */

#ifndef NO_SendMessageA__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__III_3S\n")

	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageA__III_3S */

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")

	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVCOLUMNFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2 */

#ifndef NO_SendMessageW__II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);

#ifdef _WIN32_WCE
	/* SPECIAL */
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
#endif /* _WIN32_WCE */

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);

	return rc;
}
#endif /* NO_SendMessageW__II_3I_3I */

#ifndef NO_SendMessageW__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_SendMessageW__II_3II */

#ifndef NO_SendMessageW__III_3C
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__III_3C\n")

	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageW__III_3C */

#ifndef NO_SendMessageW__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__III_3I\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageW__III_3I */

#ifndef NO_SendMessageW__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__III_3S\n")

	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SendMessageW__III_3S */

#ifndef NO_SendMessageW__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SendMessageW__IIII\n")

	return (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)arg3);
}
#endif /* NO_SendMessageW__IIII */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")

	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")

	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")

	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setRECTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")

	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")

	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")

	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTCITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")

	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTOOLINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")

	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")

	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")

	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setTVITEMFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2 */

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")

	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);
	if (arg3) setLVCOLUMNFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2 */

#ifndef NO_SetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(SetActiveWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetActiveWindow\n")

	return (jint)SetActiveWindow((HWND)arg0);
}
#endif /* NO_SetActiveWindow */

#ifndef NO_SetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(SetBkColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetBkColor\n")

	return (jint)SetBkColor((HDC)arg0, (COLORREF)arg1);
}
#endif /* NO_SetBkColor */

#ifndef NO_SetBkMode
JNIEXPORT jint JNICALL OS_NATIVE(SetBkMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetBkMode\n")

	return (jint)SetBkMode((HDC)arg0, arg1);
}
#endif /* NO_SetBkMode */

#ifndef NO_SetCapture
JNIEXPORT jint JNICALL OS_NATIVE(SetCapture)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetCapture\n")

	return (jint)SetCapture((HWND)arg0);
}
#endif /* NO_SetCapture */

#ifndef NO_SetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCaretPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetCaretPos\n")

	return (jboolean)SetCaretPos(arg0, arg1);
}
#endif /* NO_SetCaretPos */

#ifndef NO_SetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(SetClipboardData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetClipboardData\n")

	return (jint)SetClipboardData(arg0, (HANDLE)arg1);
}
#endif /* NO_SetClipboardData */

#ifndef NO_SetCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetCursor\n")

	return (jint)SetCursor((HCURSOR)arg0);
}
#endif /* NO_SetCursor */

#ifndef NO_SetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCursorPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetCursorPos\n")

	return (jboolean)SetCursorPos(arg0, arg1);
}
#endif /* NO_SetCursorPos */

#ifndef NO_SetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(SetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SetDIBColorTable\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)SetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SetDIBColorTable */

#ifndef NO_SetFocus
JNIEXPORT jint JNICALL OS_NATIVE(SetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetFocus\n")

	return (jint)SetFocus((HWND)arg0);
}
#endif /* NO_SetFocus */

#ifndef NO_SetForegroundWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(SetForegroundWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetForegroundWindow\n")

	return (jboolean)SetForegroundWindow((HWND)arg0);
}
#endif /* NO_SetForegroundWindow */

#ifndef NO_SetMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetMenu\n")

	return (jboolean)SetMenu((HWND)arg0, (HMENU)arg1);
}
#endif /* NO_SetMenu */

#ifndef NO_SetMenuDefaultItem
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetMenuDefaultItem\n")

	return (jboolean)SetMenuDefaultItem((HMENU)arg0, arg1, arg2);
}
#endif /* NO_SetMenuDefaultItem */

#ifndef NO_SetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = (jboolean)FALSE;
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("SetMenuInfo\n")
	
	/* SPECIAL */
    /*
    *  SetMenuInfo is a Win2000 and Win98 specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "SetMenuInfo"))) {

		if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1);

        rc = (jboolean) (fp)((HMENU)arg0, lparg1);
//		rc = (jboolean)SetMenuInfo(arg0, lparg1);

		if (arg1) setMENUINFOFields(env, arg1, lparg1);
	}

	return rc;
}
#endif /* NO_SetMenuInfo */

#ifndef NO_SetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("SetMenuItemInfoA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SetMenuItemInfoA */

#ifndef NO_SetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("SetMenuItemInfoW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_SetMenuItemInfoW */

#ifndef NO_SetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(SetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SetPaletteEntries\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)SetPaletteEntries((HPALETTE)arg0, arg1, arg2, (PALETTEENTRY *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_SetPaletteEntries */

#ifndef NO_SetParent
JNIEXPORT jint JNICALL OS_NATIVE(SetParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetParent\n")

	return (jint)SetParent((HWND)arg0, (HWND)arg1);
}
#endif /* NO_SetParent */

#ifndef NO_SetPixel
JNIEXPORT jint JNICALL OS_NATIVE(SetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetPixel\n")

	return (jint)SetPixel((HDC)arg0, arg1, arg2, arg3);
}
#endif /* NO_SetPixel */

#ifndef NO_SetROP2
JNIEXPORT jint JNICALL OS_NATIVE(SetROP2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetROP2\n")

	return (jint)SetROP2((HDC)arg0, arg1);
}
#endif /* NO_SetROP2 */

#ifndef NO_SetRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("SetRect\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	rc = (jboolean)SetRect(lparg0, arg1, arg2, arg3, arg4);
	if (arg0) setRECTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_SetRect */

#ifndef NO_SetRectRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("SetRectRgn\n")

	return (jboolean)SetRectRgn((HRGN)arg0, arg1, arg2, arg3, arg4);
}
#endif /* NO_SetRectRgn */

#ifndef NO_SetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SetScrollInfo\n")

	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2);
	rc = (jboolean)SetScrollInfo((HWND)arg0, arg1, lparg2, arg3);
	if (arg2) setSCROLLINFOFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_SetScrollInfo */

#ifndef NO_SetStretchBltMode
JNIEXPORT jint JNICALL OS_NATIVE(SetStretchBltMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetStretchBltMode\n")

	return (jint)SetStretchBltMode((HDC)arg0, arg1);
}
#endif /* NO_SetStretchBltMode */

#ifndef NO_SetTextAlign
JNIEXPORT jint JNICALL OS_NATIVE(SetTextAlign)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetTextAlign\n")

	return (jint)SetTextAlign((HDC)arg0, arg1);
}
#endif /* NO_SetTextAlign */

#ifndef NO_SetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(SetTextColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetTextColor\n")

	return (jint)SetTextColor((HDC)arg0, (COLORREF)arg1);
}
#endif /* NO_SetTextColor */

#ifndef NO_SetTimer
JNIEXPORT jint JNICALL OS_NATIVE(SetTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetTimer\n")

	return (jint)SetTimer((HWND)arg0, arg1, arg2, (TIMERPROC)arg3);
}
#endif /* NO_SetTimer */

#ifndef NO_SetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetWindowLongA\n")

	return (jint)SetWindowLongA((HWND)arg0, arg1, arg2);
}
#endif /* NO_SetWindowLongA */

#ifndef NO_SetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetWindowLongW\n")

	return (jint)SetWindowLongW((HWND)arg0, arg1, arg2);
}
#endif /* NO_SetWindowLongW */

#ifndef NO_SetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SetWindowPlacement\n")

	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1);
	rc = (jboolean)SetWindowPlacement((HWND)arg0, lparg1);
	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_SetWindowPlacement */

#ifndef NO_SetWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("SetWindowPos\n")

	return (jboolean)SetWindowPos((HWND)arg0, (HWND)arg1, arg2, arg3, arg4, arg5, arg6);
}
#endif /* NO_SetWindowPos */

#ifndef NO_SetWindowTextA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SetWindowTextA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)SetWindowTextA((HWND)arg0, (LPSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_SetWindowTextA */

#ifndef NO_SetWindowTextW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SetWindowTextW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jboolean)SetWindowTextW((HWND)arg0, (LPWSTR)lparg1);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_SetWindowTextW */

#ifndef NO_SetWindowsHookExA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetWindowsHookExA\n")

	return (jint)SetWindowsHookExA(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
}
#endif /* NO_SetWindowsHookExA */

#ifndef NO_SetWindowsHookExW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetWindowsHookExW\n")

	return (jint)SetWindowsHookExW(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
}
#endif /* NO_SetWindowsHookExW */

#ifndef NO_ShellExecuteExA
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ShellExecuteExA\n")

	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0);
	rc = (jboolean)ShellExecuteExA(lparg0);
	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ShellExecuteExA */

#ifndef NO_ShellExecuteExW
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ShellExecuteExW\n")

	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0);
	rc = (jboolean)ShellExecuteExW((LPSHELLEXECUTEINFOW)lparg0);
	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_ShellExecuteExW */

#ifndef NO_ShowCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ShowCaret\n")

	return (jboolean)ShowCaret((HWND)arg0);
}
#endif /* NO_ShowCaret */

#ifndef NO_ShowOwnedPopups
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowOwnedPopups)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("ShowOwnedPopups\n")

	return (jboolean)ShowOwnedPopups((HWND)arg0, arg1);
}
#endif /* NO_ShowOwnedPopups */

#ifndef NO_ShowScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("ShowScrollBar\n")

	return (jboolean)ShowScrollBar((HWND)arg0, arg1, arg2);
}
#endif /* NO_ShowScrollBar */

#ifndef NO_ShowWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ShowWindow\n")

	return (jboolean)ShowWindow((HWND)arg0, arg1);
}
#endif /* NO_ShowWindow */

#ifndef NO_StartDocA
JNIEXPORT jint JNICALL OS_NATIVE(StartDocA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("StartDocA\n")

	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1);
	rc = (jint)StartDocA((HDC)arg0, lparg1);
	if (arg1) setDOCINFOFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_StartDocA */

#ifndef NO_StartDocW
JNIEXPORT jint JNICALL OS_NATIVE(StartDocW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("StartDocW\n")

	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1);
	rc = (jint)StartDocW((HDC)arg0, (LPDOCINFOW)lparg1);
	if (arg1) setDOCINFOFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_StartDocW */

#ifndef NO_StartPage
JNIEXPORT jint JNICALL OS_NATIVE(StartPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("StartPage\n")

	return (jint)StartPage((HDC)arg0);
}
#endif /* NO_StartPage */

#ifndef NO_StretchBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(StretchBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	DEBUG_CALL("StretchBlt\n")

	return (jboolean)StretchBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8, arg9, arg10);
}
#endif /* NO_StretchBlt */

#ifndef NO_SystemParametersInfoA__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_SystemParametersInfoA__II_3II */

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I */

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSA _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I\n")

	if (arg2) lparg2 = getNONCLIENTMETRICSAFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) setNONCLIENTMETRICSAFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I */

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSW _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I\n")

	if (arg2) lparg2 = getNONCLIENTMETRICSWFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) setNONCLIENTMETRICSWFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I */

#ifndef NO_SystemParametersInfoW__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_SystemParametersInfoW__II_3II */

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I */

#ifndef NO_ToAscii
JNIEXPORT jint JNICALL OS_NATIVE(ToAscii)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jshortArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("ToAscii\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)ToAscii(arg0, arg1, (PBYTE)lparg2, (LPWORD)lparg3, arg4);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_ToAscii */

#ifndef NO_ToUnicode
JNIEXPORT jint JNICALL OS_NATIVE(ToUnicode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jcharArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc;

	DEBUG_CALL("ToUnicode\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)ToUnicode(arg0, arg1, (PBYTE)lparg2, (LPWSTR)lparg3, arg4, arg5);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_ToUnicode */

#ifndef NO_TrackMouseEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackMouseEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	TRACKMOUSEEVENT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("TrackMouseEvent\n")

	if (arg0) lparg0 = getTRACKMOUSEEVENTFields(env, arg0, &_arg0);
	rc = (jboolean)_TrackMouseEvent(lparg0);
	if (arg0) setTRACKMOUSEEVENTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_TrackMouseEvent */

#ifndef NO_TrackPopupMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackPopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6)
{
	RECT _arg6, *lparg6=NULL;
	jboolean rc;

	DEBUG_CALL("TrackPopupMenu\n")

	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6);
	rc = (jboolean)TrackPopupMenu((HMENU)arg0, arg1, arg2, arg3, arg4, (HWND)arg5, lparg6);
	if (arg6) setRECTFields(env, arg6, lparg6);
	return rc;
}
#endif /* NO_TrackPopupMenu */

#ifndef NO_TranslateAcceleratorA
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TranslateAcceleratorA\n")

	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2);
	rc = (jint)TranslateAcceleratorA((HWND)arg0, (HACCEL)arg1, lparg2);
	if (arg2) setMSGFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_TranslateAcceleratorA */

#ifndef NO_TranslateAcceleratorW
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TranslateAcceleratorW\n")

	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2);
	rc = (jint)TranslateAcceleratorW((HWND)arg0, (HACCEL)arg1, lparg2);
	if (arg2) setMSGFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_TranslateAcceleratorW */

#ifndef NO_TranslateCharsetInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateCharsetInfo)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("TranslateCharsetInfo\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)TranslateCharsetInfo((DWORD *)arg0, (LPCHARSETINFO)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_TranslateCharsetInfo */

#ifndef NO_TranslateMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateMessage)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("TranslateMessage\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)TranslateMessage(lparg0);
	if (arg0) setMSGFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_TranslateMessage */

#ifndef NO_TransparentImage
JNIEXPORT jboolean JNICALL OS_NATIVE(TransparentImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	DEBUG_CALL("TransparentImage\n")

	return (jboolean)TransparentImage((HDC)arg0, arg1, arg2, arg3, arg4, (HANDLE)arg5, arg6, arg7, arg8, arg9, (COLORREF)arg10);
}
#endif /* NO_TransparentImage */

#ifndef NO_UnhookWindowsHookEx
JNIEXPORT jboolean JNICALL OS_NATIVE(UnhookWindowsHookEx)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("UnhookWindowsHookEx\n")

	return (jboolean)UnhookWindowsHookEx((HHOOK)arg0);
}
#endif /* NO_UnhookWindowsHookEx */

#ifndef NO_UnregisterClassA
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("UnregisterClassA\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jboolean)UnregisterClassA((LPSTR)lparg0, (HINSTANCE)arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_UnregisterClassA */

#ifndef NO_UnregisterClassW
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1)
{
	jchar *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("UnregisterClassW\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jboolean)UnregisterClassW((LPWSTR)lparg0, (HINSTANCE)arg1);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_UnregisterClassW */

#ifndef NO_UpdateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(UpdateWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("UpdateWindow\n")

	return (jboolean)UpdateWindow((HWND)arg0);
}
#endif /* NO_UpdateWindow */

#ifndef NO_ValidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(ValidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ValidateRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)ValidateRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ValidateRect */

#ifndef NO_VkKeyScanA
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("VkKeyScanA\n")

	return (jshort)VkKeyScanA((TCHAR)arg0);
}
#endif /* NO_VkKeyScanA */

#ifndef NO_VkKeyScanW
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("VkKeyScanW\n")

	return (jshort)VkKeyScanW((WCHAR)arg0);
}
#endif /* NO_VkKeyScanW */

#ifndef NO_VtblCall
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	P_OLE_FN_2 fn;
	
	DEBUG_CALL("VtblCall\n")

    fn = (P_OLE_FN_2)(*(int **)arg1)[arg0];

    return fn(arg1, arg2);
}
#endif /* NO_VtblCall */

#ifndef NO_WaitMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(WaitMessage)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("WaitMessage\n")

	return (jboolean)WaitMessage();
}
#endif /* NO_WaitMessage */

#ifndef NO_WideCharToMultiByte__II_3CI_3BI_3B_3Z
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CI_3BI_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
{
	jchar *lparg2=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc;

	DEBUG_CALL("WideCharToMultiByte__II_3CI_3BI_3B_3Z\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL);
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)lparg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_WideCharToMultiByte__II_3CI_3BI_3B_3Z */

#ifndef NO_WideCharToMultiByte__II_3CIII_3B_3Z
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CIII_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
{
	jchar *lparg2=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc;

	DEBUG_CALL("WideCharToMultiByte__II_3CIII_3B_3Z\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL);
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)arg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_WideCharToMultiByte__II_3CIII_3B_3Z */

#ifndef NO_WindowFromDC
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("WindowFromDC\n")

	return (jint)WindowFromDC((HDC)arg0);
}
#endif /* NO_WindowFromDC */

#ifndef NO_WindowFromPoint
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromPoint)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("WindowFromPoint\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jint)WindowFromPoint(*lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_WindowFromPoint */


/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * SWT OS natives implementation.
 */ 

#include "swt.h"
#include "structs.h"

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

/* Natives */

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_AbortDoc
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("AbortDoc\n")

	return (jint)AbortDoc((HDC)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ActivateKeyboardLayout
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ActivateKeyboardLayout\n")

	return (jint)ActivateKeyboardLayout((HKL)arg0, arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_AdjustWindowRectEx
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jboolean arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("AdjustWindowRectEx\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0, &PGLOB(RECTFc));

	rc = (jboolean)AdjustWindowRectEx(lparg0, arg1, arg2, arg3);

	if (arg0) setRECTFields(env, arg0, lparg0, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Arc
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("Arc\n")

	return (jboolean)Arc((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_BeginDeferWindowPos
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BeginDeferWindowPos\n")

	return (jint)BeginDeferWindowPos(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_BeginPaint
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("BeginPaint\n")

	if (arg1) lparg1 = &_arg1;

	rc = (jint)BeginPaint((HWND)arg0, lparg1);

	if (arg1) setPAINTSTRUCTFields(env, arg1, lparg1, &PGLOB(PAINTSTRUCTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_BitBlt
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("BitBlt\n")

	return (jboolean)BitBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_BringWindowToTop
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BringWindowToTop\n")

	return (jboolean)BringWindowToTop((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_Call
	(JNIEnv *env, jclass that, jint address, jobject arg0)
{
	DECL_GLOB(pGlob)
	DLLVERSIONINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("Call\n")

	if (arg0) lparg0 = getDLLVERSIONINFOFields(env, arg0, &_arg0, &PGLOB(DLLVERSIONINFOFc));

	rc = (jint)((DLLGETVERSIONPROC)address)(lparg0);

	if (arg0) setDLLVERSIONINFOFields(env, arg0, lparg0, &PGLOB(DLLVERSIONINFOFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CallNextHookEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CallNextHookEx\n")

	return (jint)CallNextHookEx((HHOOK)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CallWindowProcA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CallWindowProcA\n")

	return (jint)CallWindowProcA((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CallWindowProcW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CallWindowProcW\n")

	return (jint)CallWindowProcW((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
}

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_CharLowerA
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharLowerA\n")

	return (jshort)CharLowerA((LPSTR)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_CharLowerW
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharLowerW\n")

	return (jshort)CharLowerW((LPWSTR)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_CharUpperA
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharUpperA\n")

	return (jshort)CharUpperA((LPSTR)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_CharUpperW
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharUpperW\n")

	return (jshort)CharUpperW((LPWSTR)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CheckMenuItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CheckMenuItem\n")

	return (jboolean)CheckMenuItem((HMENU)arg0, (UINT)arg1, (UINT)arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ChooseColorA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseColorA\n")

	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0, &PGLOB(CHOOSECOLORFc));

	rc = (jboolean)ChooseColorA(lparg0);

	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0, &PGLOB(CHOOSECOLORFc));

	return rc;
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE
#define ChooseColorW ChooseColor
#define CHOOSECOLORW CHOOSECOLOR
#define LPCHOOSECOLORW LPCHOOSECOLOR
#endif
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ChooseColorW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseColorW\n")

	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0, &PGLOB(CHOOSECOLORFc));

	rc = (jboolean)ChooseColorW((LPCHOOSECOLORW)lparg0);

	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0, &PGLOB(CHOOSECOLORFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ChooseFontA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseFontA\n")

	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0, &PGLOB(CHOOSEFONTFc));

	rc = (jboolean)ChooseFontA(lparg0);

	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0, &PGLOB(CHOOSEFONTFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ChooseFontW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ChooseFontW\n")

	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0, &PGLOB(CHOOSEFONTFc));

	rc = (jboolean)ChooseFontW((LPCHOOSEFONTW)lparg0);

	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0, &PGLOB(CHOOSEFONTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ClientToScreen
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ClientToScreen\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1, &PGLOB(POINTFc));

	rc = (jboolean)ClientToScreen((HWND)arg0, lparg1);

	if (arg1) setPOINTFields(env, arg1, lparg1, &PGLOB(POINTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CloseClipboard
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CloseClipboard\n")

	return (jboolean)CloseClipboard();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CombineRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CombineRgn\n")

	return (jint)CombineRgn((HRGN)arg0, (HRGN)arg1, (HRGN)arg2, arg3);
}

#ifdef _WIN32_WCE 
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1Create
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CommandBar_Create\n")

	return (jint)CommandBar_Create((HINSTANCE)arg0, (HWND)arg1, (int)arg2);
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE 
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1Destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CommandBar_Destroy\n")

	return (jboolean)CommandBar_Destroy((HWND)arg0);
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE 
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1DrawMenuBar
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CommandBar_DrawMenuBar\n")

	return (jboolean)CommandBar_DrawMenuBar((HWND)arg0, (WORD)arg1);
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE 
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1Height
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CommandBar_Height\n")

	return (jint)CommandBar_Height((HWND)arg0);
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE 
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1InsertMenubarEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CommandBar_InsertMenubarEx\n")

	return (jboolean)CommandBar_InsertMenubarEx((HWND)arg0, (HINSTANCE)arg1, (LPTSTR)arg2, (WORD)arg3);
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE 
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CommandBar_1Show
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("CommandBar_Show\n")

	return (jboolean)CommandBar_Show((HWND)arg0, (BOOL)arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CommDlgExtendedError
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CommDlgExtendedError\n")

	return (jint)CommDlgExtendedError();
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CopyImage
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("CopyImage\n")

	return (jint)CopyImage((HANDLE)arg0, arg1, arg2, arg3, arg4);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateAcceleratorTableA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateAcceleratorTableW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateBitmap
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateCaret
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CreateCaret\n")

	return (jboolean)CreateCaret((HWND)arg0, (HBITMAP)arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateCompatibleBitmap
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CreateCompatibleBitmap\n")

	return (jint)CreateCompatibleBitmap((HDC)arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateCompatibleDC
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateCompatibleDC\n")

	return (jint)CreateCompatibleDC((HDC)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateCursor
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateDCA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateDCW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateDIBSection
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONT_2
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateFontIndirectA\n")

	if (arg0) lparg0 = getLOGFONTAFields(env, arg0, &_arg0, &PGLOB(LOGFONTFc));

	rc = (jint)CreateFontIndirectA(lparg0);

	if (arg0) setLOGFONTAFields(env, arg0, lparg0, &PGLOB(LOGFONTFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateFontIndirectA__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateFontIndirectA\n")

	return (jint)CreateFontIndirectA((LPLOGFONTA)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONT_2
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateFontIndirectW\n")

	if (arg0) lparg0 = getLOGFONTWFields(env, arg0, &_arg0, &PGLOB(LOGFONTFc));

	rc = (jint)CreateFontIndirectW(lparg0);

	if (arg0) setLOGFONTWFields(env, arg0, lparg0, &PGLOB(LOGFONTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateFontIndirectW__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateFontIndirectW\n")

	return (jint)CreateFontIndirectW((LPLOGFONTW)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateIconIndirect
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	ICONINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("CreateIconIndirect\n")

	if (arg0) lparg0 = getICONINFOFields(env, arg0, &_arg0, &PGLOB(ICONINFOFc));

	rc = (jint)CreateIconIndirect(lparg0);

	if (arg0) setICONINFOFields(env, arg0, lparg0, &PGLOB(ICONINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateMenu
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CreateMenu\n")

	return (jint)CreateMenu();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreatePalette
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreatePatternBrush
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreatePatternBrush\n")

	return (jint)CreatePatternBrush((HBITMAP)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreatePen
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("CreatePen\n")

	return (jint)CreatePen(arg0, arg1, (COLORREF)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreatePopupMenu
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CreatePopupMenu\n")

	return (jint)CreatePopupMenu();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateRectRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CreateRectRgn\n")

	return (jint)CreateRectRgn(arg0, arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateSolidBrush
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CreateSolidBrush\n")

	return (jint)CreateSolidBrush((COLORREF)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateWindowExA
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	DECL_GLOB(pGlob)
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;

	DEBUG_CALL("CreateWindowExA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11, &PGLOB(CREATESTRUCTFc));

	rc = (jint)CreateWindowExA(arg0, (LPSTR)lparg1, (LPSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11, &PGLOB(CREATESTRUCTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_CreateWindowExW
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	DECL_GLOB(pGlob)
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;

	DEBUG_CALL("CreateWindowExW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11, &PGLOB(CREATESTRUCTFc));

	rc = (jint)CreateWindowExW(arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11, &PGLOB(CREATESTRUCTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DefWindowProcA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("DefWindowProcA\n")

	return (jint)DefWindowProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DefWindowProcW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("DefWindowProcW\n")

	return (jint)DefWindowProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DeferWindowPos
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	DEBUG_CALL("DeferWindowPos\n")

	return (jint)DeferWindowPos((HDWP)arg0, (HWND)arg1, (HWND)arg2, arg3, arg4, arg5, arg6, arg7);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DeleteDC
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DeleteDC\n")

	return (jboolean)DeleteDC((HDC)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DeleteMenu
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("DeleteMenu\n")

	return (jboolean)DeleteMenu((HMENU)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DeleteObject
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DeleteObject\n")

	return (jboolean)DeleteObject((HGDIOBJ)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyAcceleratorTable
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyAcceleratorTable\n")

	return (jboolean)DestroyAcceleratorTable((HACCEL)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyCaret
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("DestroyCaret\n")

	return (jboolean)DestroyCaret();
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyCursor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyCursor\n")

	return (jboolean)DestroyCursor((HCURSOR)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyIcon
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyIcon\n")

	return (jboolean)DestroyIcon((HICON)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyMenu\n")

	return (jboolean)DestroyMenu((HMENU)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DestroyWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DestroyWindow\n")

	return (jboolean)DestroyWindow((HWND)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DispatchMessageA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("DispatchMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	return (jint)DispatchMessageA(lparg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DispatchMessageW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("DispatchMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	return (jint)DispatchMessageW(lparg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DragDetect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	POINT _arg1, *lparg1=NULL;

	DEBUG_CALL("DragDetect\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1, &PGLOB(POINTFc));

	return (jboolean)DragDetect((HWND)arg0, *lparg1);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_DragFinish
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DragFinish\n")

	DragFinish((HDROP)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DragQueryFileA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DragQueryFileW
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
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawEdge
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawEdge\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)DrawEdge((HDC)arg0, lparg1, arg2, arg3);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawFocusRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawFocusRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)DrawFocusRect((HDC)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawFrameControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("DrawFrameControl\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)DrawFrameControl((HDC)arg0, lparg1, arg2, arg3);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawIconEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("DrawIconEx\n")

	return (jboolean)DrawIconEx((HDC)arg0, arg1, arg2, (HICON)arg3, arg4, arg5, arg6, (HBRUSH)arg7, arg8);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawMenuBar
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DrawMenuBar\n")

	return (jboolean)DrawMenuBar((HWND)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawStateA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	DEBUG_CALL("DrawStateA\n")

	return (jboolean)DrawStateA((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawStateW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	DEBUG_CALL("DrawStateW\n")

	return (jboolean)DrawStateW((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawTextA
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3, jint arg4)
{
	DECL_GLOB(pGlob)
	jbyte *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DrawTextA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3, &PGLOB(RECTFc));

	rc = (jint)DrawTextA((HDC)arg0, (LPSTR)lparg1, arg2, lparg3, arg4);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) setRECTFields(env, arg3, lparg3, &PGLOB(RECTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_DrawTextW
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3, jint arg4)
{
	DECL_GLOB(pGlob)
	jchar *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DrawTextW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3, &PGLOB(RECTFc));

	rc = (jint)DrawTextW((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3, arg4);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg3) setRECTFields(env, arg3, lparg3, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Ellipse
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("Ellipse\n")

	return (jboolean)Ellipse((HDC)arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EnableMenuItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("EnableMenuItem\n")

	return (jboolean)EnableMenuItem((HMENU)arg0, arg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EnableScrollBar
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("EnableScrollBar\n")

	return (jboolean)EnableScrollBar((HWND)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EnableWindow
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("EnableWindow\n")

	return (jboolean)EnableWindow((HWND)arg0, arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EndDeferWindowPos
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndDeferWindowPos\n")

	return (jboolean)EndDeferWindowPos((HDWP)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_EndDoc
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndDoc\n")

	return (jint)EndDoc((HDC)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_EndPage
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndPage\n")

	return (jint)EndPage((HDC)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_EndPaint
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	PAINTSTRUCT _arg1, *lparg1=NULL;

	DEBUG_CALL("EndPaint\n")

	if (arg1) lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1, &PGLOB(PAINTSTRUCTFc));

	return (jint)EndPaint((HWND)arg0, lparg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_EnumFontFamiliesA
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
#endif // _WIN32_WCE

#ifdef _WIN32_WCE
#define FONTENUMPROCW FONTENUMPROC
#endif // _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_EnumFontFamiliesW
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

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EnumSystemLocalesA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EnumSystemLocalesA\n")

	return (jboolean)EnumSystemLocalesA((LOCALE_ENUMPROCA)arg0, arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EnumSystemLocalesW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EnumSystemLocalesW\n")

	return (jboolean)EnumSystemLocalesW((LOCALE_ENUMPROCW)arg0, arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_EqualRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EqualRgn\n")

	return (jboolean)EqualRgn((HRGN)arg0, (HRGN)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ExtTextOutA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	DECL_GLOB(pGlob)
	RECT _arg4, *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("ExtTextOutA\n")

	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4, &PGLOB(RECTFc));
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);

	rc = (jboolean)ExtTextOutA((HDC)arg0, arg1, arg2, arg3, lparg4, (LPSTR)lparg5, arg6, (CONST INT*)lparg7);
	
	if (arg4) setRECTFields(env, arg4, &_arg4, &PGLOB(RECTFc));
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	
	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ExtTextOutW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jcharArray arg5, jint arg6, jintArray arg7)
{
	DECL_GLOB(pGlob)
	RECT _arg4, *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;

	DEBUG_CALL("ExtTextOutW\n")

	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4, &PGLOB(RECTFc));
	if (arg5) lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);

	rc = (jboolean)ExtTextOutW((HDC)arg0, arg1, arg2, arg3, lparg4, (LPWSTR)lparg5, arg6, (CONST INT*)lparg7);

	if (arg4) setRECTFields(env, arg4, &_arg4, &PGLOB(RECTFc));
	if (arg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	
	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ExtractIconExA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ExtractIconExW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_FillRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FillRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jint)FillRect((HDC)arg0, lparg1, (HBRUSH)arg2);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_FindWindowA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_FindWindowW
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_FreeLibrary
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("FreeLibrary\n")

	return (jboolean)FreeLibrary((HMODULE)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetACP
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetACP\n")

	return (jint)GetACP();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetActiveWindow
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetActiveWindow\n")

	return (jint)GetActiveWindow();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetBkColor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetBkColor\n")

	return (jint)GetBkColor((HDC)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCapture
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCapture\n")

	return (jint)GetCapture();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCaretPos
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	POINT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetCaretPos\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0, &PGLOB(POINTFc));

	rc = (jboolean)GetCaretPos(lparg0);

	if (arg0) setPOINTFields(env, arg0, lparg0, &PGLOB(POINTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharABCWidthsA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharABCWidthsW
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharWidthA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharWidthW
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharacterPlacementA
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	DECL_GLOB(pGlob)
	jbyte *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetCharacterPlacementA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4, &PGLOB(GCP_RESULTSFc));

	rc = (jint)GetCharacterPlacementA((HDC)arg0, (LPSTR)lparg1, arg2, arg3, lparg4, arg5);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4, &PGLOB(GCP_RESULTSFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCharacterPlacementW
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	DECL_GLOB(pGlob)
	jchar *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetCharacterPlacementW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4, &PGLOB(GCP_RESULTSFc));

	rc = (jint)GetCharacterPlacementW((HDC)arg0, (LPWSTR)lparg1, arg2, arg3, (LPGCP_RESULTSW)lparg4, arg5);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4, &PGLOB(GCP_RESULTSFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClassInfoA
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	jbyte *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetClassInfoA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2, &PGLOB(WNDCLASSFc));

	rc = (jboolean)GetClassInfoA((HINSTANCE)arg0, (LPSTR)lparg1, lparg2);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2, &PGLOB(WNDCLASSFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClassInfoW
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	jchar *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetClassInfoW\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2, &PGLOB(WNDCLASSFc));

	rc = (jboolean)GetClassInfoW((HINSTANCE)arg0, (LPWSTR)lparg1, (LPWNDCLASSW)lparg2);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2, &PGLOB(WNDCLASSFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClientRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetClientRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)GetClientRect((HWND)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClipBox
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetClipBox\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jint)GetClipBox((HDC)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClipRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetClipRgn\n")

	return (jint)GetClipRgn((HDC)arg0, (HRGN)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClipboardData
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetClipboardData\n")

	return (jint)GetClipboardData(arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClipboardFormatNameA
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetClipboardFormatNameA\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);

	rc = (jint)GetClipboardFormatNameA(arg0, (LPTSTR)lparg1, arg2);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetClipboardFormatNameW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCurrentObject
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetCurrentObject\n")

	return (jint)GetCurrentObject((HDC)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCurrentProcessId
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentProcessId\n")

	return (jint)GetCurrentProcessId();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCurrentThreadId
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentThreadId\n")

	return (jint)GetCurrentThreadId();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCursor
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCursor\n")

	return (jint)GetCursor();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetCursorPos
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	POINT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetCursorPos\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0, &PGLOB(POINTFc));

	rc = (jboolean)GetCursorPos(lparg0);

	if (arg0) setPOINTFields(env, arg0, lparg0, &PGLOB(POINTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDC
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetDC\n")

	return (jint)GetDC((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDCEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetDCEx\n")

	return (jint)GetDCEx((HWND)arg0, (HRGN)arg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDIBColorTable
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDIBits
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDesktopWindow
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDesktopWindow\n")

	return (jint)GetDesktopWindow();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDeviceCaps
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetDeviceCaps\n")

	return (jint)GetDeviceCaps((HDC)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDialogBaseUnits
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDialogBaseUnits\n")

	return (jint)GetDialogBaseUnits();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDlgItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetDlgItem\n")

	return (jint)GetDlgItem((HWND)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetDoubleClickTime
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDoubleClickTime\n")

	return (jint)GetDoubleClickTime();
}

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_GetFileTitleA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_GetFileTitleW
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetFocus
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetFocus\n")

	return (jint)GetFocus();
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetFontLanguageInfo
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetFontLanguageInfo\n")

	return (jint)GetFontLanguageInfo((HDC)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetIconInfo
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	ICONINFO _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetIconInfo\n")

	if (arg1) lparg1 = getICONINFOFields(env, arg1, &_arg1, &PGLOB(ICONINFOFc));

	rc = (jboolean)GetIconInfo((HICON)arg0, lparg1);

	if (arg1) setICONINFOFields(env, arg1, lparg1, &PGLOB(ICONINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_GetKeyState
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetKeyState\n")

	return (jshort)GetKeyState(arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetKeyboardLayout
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetKeyboardLayout\n")

	return (jint)GetKeyboardLayout(arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetKeyboardLayoutList
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetKeyboardState
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetLastActivePopup
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetLastActivePopup\n")

	return (jint)GetLastActivePopup((HWND)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetLastError
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetLastError\n")

	return (jint)GetLastError();
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetLocaleInfoA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetLocaleInfoW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenu\n")

	return (jint)GetMenu((HWND)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenuDefaultItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetMenuDefaultItem\n")

	return (jint)GetMenuDefaultItem((HMENU)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenuInfo
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = (jboolean)FALSE;
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("GetMenuInfo\n")

    /*
    *  GetMenuInfo is a Win2000 and Win98 specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "GetMenuInfo"))) {
    
		if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1, &PGLOB(MENUINFOFc));
		
        rc = (jboolean) (fp)((HMENU)arg0, lparg1);
//		rc = (jboolean)GetMenuInfo(arg0, lparg1);

		if (arg1) setMENUINFOFields(env, arg1, lparg1, &PGLOB(MENUINFOFc));
	}

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenuItemCount
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenuItemCount\n")

	return (jint)GetMenuItemCount((HMENU)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenuItemInfoA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetMenuItemInfoA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)GetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMenuItemInfoW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetMenuItemInfoW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)GetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMessageA
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	rc = (jboolean)GetMessageA(lparg0, (HWND)arg1, arg2, arg3);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMessagePos
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMessagePos\n")

	return (jint)GetMessagePos();
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMessageTime
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMessageTime\n")

	return (jint)GetMessageTime();
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetMessageW
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	rc = (jboolean)GetMessageW(lparg0, (HWND)arg1, arg2, arg3);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetModuleHandleA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetModuleHandleW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetNearestPaletteIndex
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetNearestPaletteIndex\n")

	return (jint)GetNearestPaletteIndex((HPALETTE)arg0, (COLORREF)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA\n")

	if (arg2) lparg2 = getLOGPENFields(env, arg2, &_arg2, &PGLOB(LOGPENFc));

	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGPENFields(env, arg2, lparg2, &PGLOB(LOGPENFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA\n")

	if (arg2) lparg2 = getLOGFONTAFields(env, arg2, &_arg2, &PGLOB(LOGFONTFc));

	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGFONTAFields(env, arg2, lparg2, &PGLOB(LOGFONTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA\n")

	if (arg2) lparg2 = getLOGBRUSHFields(env, arg2, &_arg2, &PGLOB(LOGBRUSHFc));

	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGBRUSHFields(env, arg2, lparg2, &PGLOB(LOGBRUSHFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA\n")

	if (arg2) lparg2 = getDIBSECTIONFields(env, arg2, &_arg2, &PGLOB(DIBSECTIONFc));

	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setDIBSECTIONFields(env, arg2, lparg2, &PGLOB(DIBSECTIONFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	BITMAP _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectA\n")

	if (arg2) lparg2 = getBITMAPFields(env, arg2, &_arg2, &PGLOB(BITMAPFc));

	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setBITMAPFields(env, arg2, lparg2, &PGLOB(BITMAPFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW\n")

	if (arg2) lparg2 = getLOGPENFields(env, arg2, &_arg2, &PGLOB(LOGPENFc));

	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGPENFields(env, arg2, lparg2, &PGLOB(LOGPENFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW\n")

	if (arg2) lparg2 = getLOGFONTWFields(env, arg2, &_arg2, &PGLOB(LOGFONTFc));

	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGFONTWFields(env, arg2, lparg2, &PGLOB(LOGFONTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW\n")

	if (arg2) lparg2 = getLOGBRUSHFields(env, arg2, &_arg2, &PGLOB(LOGBRUSHFc));

	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setLOGBRUSHFields(env, arg2, lparg2, &PGLOB(LOGBRUSHFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW\n")

	if (arg2) lparg2 = getDIBSECTIONFields(env, arg2, &_arg2, &PGLOB(DIBSECTIONFc));

	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setDIBSECTIONFields(env, arg2, lparg2, &PGLOB(DIBSECTIONFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	BITMAP _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetObjectW\n")

	if (arg2) lparg2 = getBITMAPFields(env, arg2, &_arg2, &PGLOB(BITMAPFc));

	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);

	if (arg2) setBITMAPFields(env, arg2, lparg2, &PGLOB(BITMAPFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetOpenFileNameA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetOpenFileNameA\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0, &PGLOB(OPENFILENAMEFc));

	rc = (jboolean)GetOpenFileNameA(lparg0);

	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0, &PGLOB(OPENFILENAMEFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetOpenFileNameW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetOpenFileNameW\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0, &PGLOB(OPENFILENAMEFc));

	rc = (jboolean)GetOpenFileNameW((LPOPENFILENAMEW)lparg0);

	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0, &PGLOB(OPENFILENAMEFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetPaletteEntries
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetParent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetParent\n")

	return (jint)GetParent((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetPixel
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("GetPixel\n")

	return (jint)GetPixel((HDC)arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetProcAddress
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetProcessHeap
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetProcessHeap\n")

	return (jint)GetProcessHeap();
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetProfileStringA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetProfileStringW
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetROP2
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetROP2\n")

	return (jint)GetROP2((HDC)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetRegionData
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetRgnBox
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetRgnBox\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jint)GetRgnBox((HRGN)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSaveFileNameA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetSaveFileNameA\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0, &PGLOB(OPENFILENAMEFc));

	rc = (jboolean)GetSaveFileNameA(lparg0);

	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0, &PGLOB(OPENFILENAMEFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSaveFileNameW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetSaveFileNameW\n")

	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0, &PGLOB(OPENFILENAMEFc));

	rc = (jboolean)GetSaveFileNameW((LPOPENFILENAMEW)lparg0);

	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0, &PGLOB(OPENFILENAMEFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetScrollInfo
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("GetScrollInfo\n")

	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2, &PGLOB(SCROLLINFOFc));

	rc = (jboolean)GetScrollInfo((HWND)arg0, arg1, lparg2);

	if (arg2) setSCROLLINFOFields(env, arg2, lparg2, &PGLOB(SCROLLINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetStockObject
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetStockObject\n")

	return (jint)GetStockObject(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSysColor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSysColor\n")

	return (jint)GetSysColor(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSysColorBrush
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSysColorBrush\n")

	return (jint)GetSysColorBrush(arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSystemMenu
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("GetSystemMenu\n")

	return (jint)GetSystemMenu((HWND)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetSystemMetrics
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetSystemMetrics\n")

	return (jint)GetSystemMetrics(arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextCharset
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetTextCharset\n")

	return (jint)GetTextCharset((HDC)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextColor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetTextColor\n")

	return (jint)GetTextColor((HDC)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextExtentPoint32A
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	jbyte *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextExtentPoint32A\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getSIZEFields(env, arg3, &_arg3, &PGLOB(SIZEFc));

	rc = (jboolean)GetTextExtentPoint32A((HDC)arg0, (LPSTR)lparg1, arg2, lparg3);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg3) setSIZEFields(env, arg3, lparg3, &PGLOB(SIZEFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextExtentPoint32W
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	jchar *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextExtentPoint32W\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = getSIZEFields(env, arg3, &_arg3, &PGLOB(SIZEFc));

	rc = (jboolean)GetTextExtentPoint32W((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg3) setSIZEFields(env, arg3, lparg3, &PGLOB(SIZEFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextMetricsA
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	TEXTMETRICA _arg1={0}, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextMetricsA\n")

	if (arg1) lparg1 = &_arg1;

	rc = (jboolean)GetTextMetricsA((HDC)arg0, lparg1);

	if (arg1) setTEXTMETRICAFields(env, arg1, lparg1, &PGLOB(TEXTMETRICFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTextMetricsW
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	TEXTMETRICW _arg1={0}, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetTextMetricsW\n")

	if (arg1) lparg1 = &_arg1;

	rc = (jboolean)GetTextMetricsW((HDC)arg0, lparg1);

	if (arg1) setTEXTMETRICWFields(env, arg1, lparg1, &PGLOB(TEXTMETRICFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetTickCount
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetTickCount\n")

	return (jint)GetTickCount();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetUpdateRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("GetUpdateRgn\n")

	return (jint)GetUpdateRgn((HWND)arg0, (HRGN)arg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetVersionExA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OSVERSIONINFOA _arg0={0}, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetVersionExA\n")

	if (arg0) lparg0 = getOSVERSIONINFOAFields(env, arg0, &_arg0, &PGLOB(OSVERSIONINFOFc));

	rc = (jboolean)GetVersionExA(lparg0);

	if (arg0) setOSVERSIONINFOAFields(env, arg0, lparg0, &PGLOB(OSVERSIONINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetVersionExW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	OSVERSIONINFOW _arg0={0}, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("GetVersionExW\n")

	if (arg0) lparg0 = getOSVERSIONINFOWFields(env, arg0, &_arg0, &PGLOB(OSVERSIONINFOFc));

	rc = (jboolean)GetVersionExW(lparg0);

	if (arg0) setOSVERSIONINFOWFields(env, arg0, lparg0, &PGLOB(OSVERSIONINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindow
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindow\n")

	return (jint)GetWindow((HWND)arg0, arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowLongA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindowLongA\n")

	return (jint)GetWindowLongA((HWND)arg0, arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowLongW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetWindowLongW\n")

	return (jint)GetWindowLongW((HWND)arg0, arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowPlacement
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetWindowPlacement\n")

	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1, &PGLOB(WINDOWPLACEMENTFc));

	rc = (jboolean)GetWindowPlacement((HWND)arg0, lparg1);

	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1, &PGLOB(WINDOWPLACEMENTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("GetWindowRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)GetWindowRect((HWND)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowTextA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowTextLengthA
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowTextLengthA\n")

	return (jint)GetWindowTextLengthA((HWND)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowTextLengthW
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowTextLengthW\n")

	return (jint)GetWindowTextLengthW((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowTextW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GetWindowThreadProcessId
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GlobalAlloc
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GlobalAlloc\n")

	return (jint)GlobalAlloc(arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GlobalFree
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalFree\n")

	return (jint)GlobalFree((HANDLE)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GlobalLock
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalLock\n")

	return (jint)GlobalLock((HANDLE)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_GlobalSize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalSize\n")

	return (jint)GlobalSize((HANDLE)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GlobalUnlock
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GlobalUnlock\n")

	return (jboolean)GlobalUnlock((HANDLE)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_GradientFill
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    HMODULE hm;
    FARPROC fp;
 
	DEBUG_CALL("GradientFill\n")

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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_HeapAlloc
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HeapAlloc\n")

	return (jint)HeapAlloc((HANDLE)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_HeapFree
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HeapFree\n")

	return (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_HideCaret
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HideCaret\n")

	return (jboolean)HideCaret((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1AddMasked
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_AddMasked\n")

	return (jint)ImageList_AddMasked((HIMAGELIST)arg0, (HBITMAP)arg1, (COLORREF)arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1Create
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("ImageList_Create\n")

	return (jint)ImageList_Create(arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1Destroy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImageList_Destroy\n")

	return (jboolean)ImageList_Destroy((HIMAGELIST)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1GetIcon
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_GetIcon\n")

	return (jint)ImageList_GetIcon((HIMAGELIST)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1GetIconSize
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("ImageList_GetIconSize\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);

	rc = (jboolean)ImageList_GetIconSize((HIMAGELIST)arg0, lparg1, lparg2);

	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1GetImageCount
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImageList_GetImageCount\n")

	return (jint)ImageList_GetImageCount((HIMAGELIST)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1Remove
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImageList_Remove\n")

	return (jboolean)ImageList_Remove((HIMAGELIST)arg0, arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1Replace
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("ImageList_Replace\n")

	return (jboolean)ImageList_Replace((HIMAGELIST)arg0, arg1, (HBITMAP)arg2, (HBITMAP)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1ReplaceIcon
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_ReplaceIcon\n")

	return (jint)ImageList_ReplaceIcon((HIMAGELIST)arg0, arg1, (HICON)arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImageList_1SetIconSize
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImageList_SetIconSize\n")

	return (jboolean)ImageList_SetIconSize((HIMAGELIST)arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmAssociateContext
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImmAssociateContext\n")

	return (jint)ImmAssociateContext((HWND)arg0, (HIMC)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmCreateContext
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ImmCreateContext\n")

	return (jint)ImmCreateContext();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmDestroyContext
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmDestroyContext\n")

	return (jboolean)ImmDestroyContext((HIMC)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetCompositionFontA
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmGetCompositionFontA\n")

	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	rc = (jboolean)ImmGetCompositionFontA((HIMC)arg0, lparg1);

	if (arg1) setLOGFONTAFields(env, arg1, lparg1, &PGLOB(LOGFONTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetCompositionFontW
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmGetCompositionFontW\n")

	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	rc = (jboolean)ImmGetCompositionFontW((HIMC)arg0, lparg1);

	if (arg1) setLOGFONTWFields(env, arg1, lparg1, &PGLOB(LOGFONTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetCompositionStringA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetCompositionStringW
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetContext
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetContext\n")

	return (jint)ImmGetContext((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetConversionStatus
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetDefaultIMEWnd
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetDefaultIMEWnd\n")

	return (jint)ImmGetDefaultIMEWnd((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmGetOpenStatus
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ImmGetOpenStatus\n")

	return (jboolean)ImmGetOpenStatus((HIMC)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmReleaseContext
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ImmReleaseContext\n")

	return (jboolean)ImmReleaseContext((HWND)arg0, (HIMC)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmSetCompositionFontA
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg1, *lparg1=NULL;

	DEBUG_CALL("ImmSetCompositionFontA\n")

	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	return (jboolean)ImmSetCompositionFontA((HIMC)arg0, lparg1);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmSetCompositionFontW
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg1, *lparg1=NULL;

	DEBUG_CALL("ImmSetCompositionFontW\n")

	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	return (jboolean)ImmSetCompositionFontW((HIMC)arg0, lparg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmSetCompositionWindow
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	COMPOSITIONFORM _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ImmSetCompositionWindow\n")

	if (arg1) lparg1 = getCOMPOSITIONFORMFields(env, arg1, &_arg1, &PGLOB(COMPOSITIONFORMFc));

	rc = (jboolean)ImmSetCompositionWindow((HIMC)arg0, lparg1);

	if (arg1) setCOMPOSITIONFORMFields(env, arg1, lparg1, &PGLOB(COMPOSITIONFORMFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmSetConversionStatus
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("ImmSetConversionStatus\n")

	return (jboolean)ImmSetConversionStatus((HIMC)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ImmSetOpenStatus
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("ImmSetOpenStatus\n")

	return (jboolean)ImmSetOpenStatus((HIMC)arg0, arg1);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_InitCommonControls
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("InitCommonControls\n")

	InitCommonControls();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InitCommonControlsEx
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	INITCOMMONCONTROLSEX _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("InitCommonControlsEx\n")

	if (arg0) lparg0 = getINITCOMMONCONTROLSEXFields(env, arg0, &_arg0, &PGLOB(INITCOMMONCONTROLSEXFc));

	rc = (jboolean)InitCommonControlsEx(lparg0);

	if (arg0) setINITCOMMONCONTROLSEXFields(env, arg0, lparg0, &PGLOB(INITCOMMONCONTROLSEXFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InsertMenuA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InsertMenuItemA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuItemA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)InsertMenuItemA((HMENU)arg0, arg1, arg2, lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InsertMenuItemW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("InsertMenuItemW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)InsertMenuItemW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InsertMenuW
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InvalidateRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("InvalidateRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)InvalidateRect((HWND)arg0, lparg1, arg2);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_InvalidateRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("InvalidateRgn\n")

	return (jboolean)InvalidateRgn((HWND)arg0, (HRGN)arg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_IsDBCSLeadByte
	(JNIEnv *env, jclass that, jbyte arg0)
{
	DEBUG_CALL("IsDBCSLeadByte\n")

	return (jboolean)IsDBCSLeadByte(arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_IsIconic
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsIconic\n")

	return (jboolean)IsIconic((HWND)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_IsWindowEnabled
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowEnabled\n")

	return (jboolean)IsWindowEnabled((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_IsWindowVisible
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowVisible\n")

	return (jboolean)IsWindowVisible((HWND)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_IsZoomed
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsZoomed\n")

	return (jboolean)IsZoomed((HWND)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_KillTimer
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("KillTimer\n")

	return (jboolean)KillTimer((HWND)arg0, arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_LineTo
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("LineTo\n")

	return (jboolean)LineTo((HDC)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadBitmapA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadBitmapA\n")

	return (jint)LoadBitmapA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadBitmapW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadBitmapW\n")

	return (jint)LoadBitmapW((HINSTANCE)arg0, (LPWSTR)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadCursorA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadCursorA\n")

	return (jint)LoadCursorA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadCursorW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadCursorW\n")

	return (jint)LoadCursorW((HINSTANCE)arg0, (LPWSTR)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadIconA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadIconA\n")

	return (jint)LoadIconA((HINSTANCE)arg0, (LPSTR)arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadIconW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("LoadIconW\n")

	return (jint)LoadIconW((HINSTANCE)arg0, (LPWSTR)arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadImageA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadImageW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadLibraryA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_LoadLibraryW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MapVirtualKeyA
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("MapVirtualKeyA\n")

	return (jint)MapVirtualKeyA(arg0, arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MapVirtualKeyW
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("MapVirtualKeyW\n")

	return (jint)MapVirtualKeyW(arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MapWindowPoints\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2, &PGLOB(RECTFc));

	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);

	if (arg2) setRECTFields(env, arg2, lparg2, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	POINT _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("MapWindowPoints\n")

	if (arg2) lparg2 = getPOINTFields(env, arg2, &_arg2, &PGLOB(POINTFc));

	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);

	if (arg2) setPOINTFields(env, arg2, lparg2, &PGLOB(POINTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_MessageBeep
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("MessageBeep\n")

	return (jboolean)MessageBeep(arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MessageBoxA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MessageBoxW
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

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3SI
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3FI
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3DI
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3SII
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3FII
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3DII
	(JNIEnv *env, jclass that, jdoubleArray arg0, jint arg1, jint arg2)
{
	jdouble *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	DROPFILES _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getDROPFILESFields(env, arg1, &_arg1, &PGLOB(DROPFILESFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) setDROPFILESFields(env, arg1, lparg1, &PGLOB(DROPFILESFc));
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	WINDOWPOS _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setWINDOWPOSFields(env, arg0, lparg0, &PGLOB(WINDOWPOSFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	TVITEM _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setTVITEMFields(env, arg0, lparg0, &PGLOB(TVITEMFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMTOOLBAR _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMTOOLBARFields(env, arg0, lparg0, &PGLOB(NMTOOLBARFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMLISTVIEW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMLISTVIEWFields(env, arg0, lparg0, &PGLOB(NMLISTVIEWFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMHEADER _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMHEADERFields(env, arg0, lparg0, &PGLOB(NMHEADERFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMHDR _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMHDRFields(env, arg0, lparg0, &PGLOB(NMHDRFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	MEASUREITEMSTRUCT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setMEASUREITEMSTRUCTFields(env, arg0, lparg0, &PGLOB(MEASUREITEMSTRUCTFc));
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	HELPINFO _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setHELPINFOFields(env, arg0, lparg0, &PGLOB(HELPINFOFc));
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	HDITEM _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setHDITEMFields(env, arg0, lparg0, &PGLOB(HDITEMFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	DRAWITEMSTRUCT _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setDRAWITEMSTRUCTFields(env, arg0, lparg0, &PGLOB(DRAWITEMSTRUCTFc));
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	TRIVERTEX _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getTRIVERTEXFields(env, arg1, &_arg1, &PGLOB(TRIVERTEXFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	WINDOWPOS _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getWINDOWPOSFields(env, arg1, &_arg1, &PGLOB(WINDOWPOSFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	MEASUREITEMSTRUCT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getMEASUREITEMSTRUCTFields(env, arg1, &_arg1, &PGLOB(MEASUREITEMSTRUCTFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	GRADIENT_RECT _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = getGRADIENT_RECTFields(env, arg1, &_arg1, &PGLOB(GRADIENT_RECTFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3II
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3CI
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__I_3BI
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);

	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3III
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	jbyte *lparg0=NULL;
	ACCEL _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = getACCELFields(env, arg1, &_arg1, &PGLOB(ACCELFc));

	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);

	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3BII
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory___3CII
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;

	DEBUG_CALL("MoveMemory\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryA__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMTTDISPINFOA _arg0={0}, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryA\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMTTDISPINFOAFields(env, arg0, lparg0, &PGLOB(NMTTDISPINFOFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryA__Lorg_eclipse_swt_internal_win32_LOGFONT_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryA\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setLOGFONTAFields(env, arg0, lparg0, &PGLOB(LOGFONTFc));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryA__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMTTDISPINFOA _arg1={0}, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryA\n")

	if (arg1) lparg1 = getNMTTDISPINFOAFields(env, arg1, &_arg1, &PGLOB(NMTTDISPINFOFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryA__ILorg_eclipse_swt_internal_win32_LOGFONT_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTA _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryA\n")

	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryW__Lorg_eclipse_swt_internal_win32_NMTTDISPINFO_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMTTDISPINFOW _arg0={0}, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryW\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setNMTTDISPINFOWFields(env, arg0, lparg0, &PGLOB(NMTTDISPINFOFc));
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryW__Lorg_eclipse_swt_internal_win32_LOGFONT_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg0, *lparg0=NULL;

	DEBUG_CALL("MoveMemoryW\n")

	if (arg0) lparg0 = &_arg0;

	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);

	if (arg0) setLOGFONTWFields(env, arg0, lparg0, &PGLOB(LOGFONTFc));
}

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryW__ILorg_eclipse_swt_internal_win32_NMTTDISPINFO_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	NMTTDISPINFOW _arg1={0}, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryW\n")

	if (arg1) lparg1 = getNMTTDISPINFOWFields(env, arg1, &_arg1, &PGLOB(NMTTDISPINFOFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemoryW__ILorg_eclipse_swt_internal_win32_LOGFONT_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DECL_GLOB(pGlob)
	LOGFONTW _arg1, *lparg1=NULL;

	DEBUG_CALL("MoveMemoryW\n")

	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1, &PGLOB(LOGFONTFc));

	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveToEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("MoveToEx\n")

	return (jboolean)MoveToEx((HDC)arg0, arg1, arg2, (LPPOINT)arg3);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_MultiByteToWideChar
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg4=NULL;
	jint rc;

	DEBUG_CALL("MultiByteToWideChar\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);

	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)lparg2, arg3, (LPWSTR)lparg4, arg5);

	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_OleInitialize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("OleInitialize\n")

	return (jint)OleInitialize((LPVOID)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_OleUninitialize
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("OleUninitialize\n")

	OleUninitialize();
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_OpenClipboard
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("OpenClipboard\n")

	return (jboolean)OpenClipboard((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PatBlt
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("PatBlt\n")

	return (jboolean)PatBlt((HDC)arg0, arg1, arg2, arg3, arg4, arg5);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PeekMessageA
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PeekMessageA\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	rc = (jboolean)PeekMessageA(lparg0, (HWND)arg1, arg2, arg3, arg4);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PeekMessageW
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PeekMessageW\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	rc = (jboolean)PeekMessageW(lparg0, (HWND)arg1, arg2, arg3, arg4);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Pie
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("Pie\n")

	return (jboolean)Pie((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Polygon
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Polyline
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

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PostMessageA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostMessageA\n")

	return (jboolean)PostMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PostMessageW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostMessageW\n")

	return (jboolean)PostMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PostThreadMessageA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostThreadMessageA\n")

	return (jboolean)PostThreadMessageA(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PostThreadMessageW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("PostThreadMessageW\n")

	return (jboolean)PostThreadMessageW(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PrintDlgA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PrintDlgA\n")

	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0, &PGLOB(PRINTDLGFc));

	rc = (jboolean)PrintDlgA(lparg0);

	if (arg0) setPRINTDLGFields(env, arg0, lparg0, &PGLOB(PRINTDLGFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PrintDlgW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PrintDlgW\n")

	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0, &PGLOB(PRINTDLGFc));

	rc = (jboolean)PrintDlgW((LPPRINTDLGW)lparg0);

	if (arg0) setPRINTDLGFields(env, arg0, lparg0, &PGLOB(PRINTDLGFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PtInRect
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg0, *lparg0=NULL;
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("PtInRect\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0, &PGLOB(RECTFc));
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1, &PGLOB(POINTFc));

	rc = (jboolean)PtInRect(lparg0, *lparg1);

	if (arg0) setRECTFields(env, arg0, lparg0, &PGLOB(RECTFc));
	if (arg1) setPOINTFields(env, arg1, lparg1, &PGLOB(POINTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_PtInRegion
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("PtInRegion\n")

	return (jboolean)PtInRegion((HRGN)arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RealizePalette
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RealizePalette\n")

	return (jint)RealizePalette((HDC)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_RectInRegion
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("RectInRegion\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)RectInRegion((HRGN)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_Rectangle
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("Rectangle\n")

	return (jboolean)Rectangle((HDC)arg0, arg1, arg2, arg3, arg4);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_RedrawWindow
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("RedrawWindow\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)RedrawWindow((HWND)arg0, lparg1, (HRGN)arg2, arg3);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegCloseKey
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RegCloseKey\n")

	return (jint)RegCloseKey((HKEY)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegEnumKeyExA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4, jbyteArray arg5, jintArray arg6, jobject arg7)
{
	DECL_GLOB(pGlob)
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
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7, &PGLOB(FILETIMEFc));

	rc = (jint)RegEnumKeyExA((HKEY)arg0, arg1, (LPSTR)lparg2, lparg3, lparg4, (LPSTR)lparg5, lparg6, lparg7);

	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) setFILETIMEFields(env, arg7, lparg7, &PGLOB(FILETIMEFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegEnumKeyExW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jintArray arg3, jintArray arg4, jcharArray arg5, jintArray arg6, jobject arg7)
{
	DECL_GLOB(pGlob)
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
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7, &PGLOB(FILETIMEFc));

	rc = (jint)RegEnumKeyExW((HKEY)arg0, arg1, (LPWSTR)lparg2, lparg3, lparg4, (LPWSTR)lparg5, lparg6, lparg7);

	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg7) setFILETIMEFields(env, arg7, lparg7, &PGLOB(FILETIMEFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegOpenKeyExA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegOpenKeyExW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegQueryInfoKeyA
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

	rc = (jint)RegQueryInfoKeyA((HKEY)arg0, (LPTSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);

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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegQueryInfoKeyW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegQueryValueExA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegQueryValueExW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegisterClassA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClassA\n")

	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0, &PGLOB(WNDCLASSFc));

	rc = (jint)RegisterClassA(lparg0);

	if (arg0) setWNDCLASSFields(env, arg0, lparg0, &PGLOB(WNDCLASSFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegisterClassW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("RegisterClassW\n")

	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0, &PGLOB(WNDCLASSFc));

	rc = (jint)RegisterClassW((LPWNDCLASSW)lparg0);

	if (arg0) setWNDCLASSFields(env, arg0, lparg0, &PGLOB(WNDCLASSFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegisterClipboardFormatA
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_RegisterClipboardFormatW
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ReleaseCapture
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ReleaseCapture\n")

	return (jboolean)ReleaseCapture();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ReleaseDC
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ReleaseDC\n")

	return (jint)ReleaseDC((HWND)arg0, (HDC)arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_RemoveMenu
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("RemoveMenu\n")

	return (jboolean)RemoveMenu((HMENU)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_RoundRect
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("RoundRect\n")

	return (jboolean)RoundRect((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SHBrowseForFolderA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SHBrowseForFolderA\n")

	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0, &PGLOB(BROWSEINFOFc));

	rc = (jint)SHBrowseForFolderA(lparg0);

	if (arg0) setBROWSEINFOFields(env, arg0, lparg0, &PGLOB(BROWSEINFOFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SHBrowseForFolderW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SHBrowseForFolderW\n")

	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0, &PGLOB(BROWSEINFOFc));

	rc = (jint)SHBrowseForFolderW((LPBROWSEINFOW)lparg0);

	if (arg0) setBROWSEINFOFields(env, arg0, lparg0, &PGLOB(BROWSEINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SHGetMalloc
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

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SHGetPathFromIDListA
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SHGetPathFromIDListW
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
#endif // _WIN32_WCE

#ifdef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SHSetAppKeyWndAssoc
	(JNIEnv *env, jclass that, jbyte arg0, jint arg1)
{
	DEBUG_CALL("SHSetAppKeyWndAssoc\n")

	return (jboolean) SHSetAppKeyWndAssoc((BYTE)arg0, (HWND)arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ScreenToClient
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	POINT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ScreenToClient\n")

	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1, &PGLOB(POINTFc));

	rc = (jboolean)ScreenToClient((HWND)arg0, lparg1);

	if (arg1) setPOINTFields(env, arg1, lparg1, &PGLOB(POINTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ScrollWindowEx
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jobject arg6, jint arg7)
{
	DECL_GLOB(pGlob)
	RECT _arg3, *lparg3=NULL;
	RECT _arg4, *lparg4=NULL;
	RECT _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("ScrollWindowEx\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3, &PGLOB(RECTFc));
	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4, &PGLOB(RECTFc));
	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6, &PGLOB(RECTFc));

	rc = (jint)ScrollWindowEx((HWND)arg0, arg1, arg2, lparg3, lparg4, (HRGN)arg5, lparg6, arg7);

	if (arg3) setRECTFields(env, arg3, lparg3, &PGLOB(RECTFc));
	if (arg4) setRECTFields(env, arg4, lparg4, &PGLOB(RECTFc));
	if (arg6) setRECTFields(env, arg6, lparg6, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SelectClipRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SelectClipRgn\n")

	return (jint)SelectClipRgn((HDC)arg0, (HRGN)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SelectObject
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SelectObject\n")

	return (jint)SelectObject((HDC)arg0, (HGDIOBJ)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SelectPalette
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("SelectPalette\n")

	return (jint)SelectPalette((HDC)arg0, (HPALETTE)arg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3, &PGLOB(TVITEMFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVITEMFields(env, arg3, lparg3, &PGLOB(TVITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3, &PGLOB(TVINSERTSTRUCTFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3, &PGLOB(TVINSERTSTRUCTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3, &PGLOB(TVHITTESTINFOFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3, &PGLOB(TVHITTESTINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3, &PGLOB(TOOLINFOFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTOOLINFOFields(env, arg3, lparg3, &PGLOB(TOOLINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TCITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3, &PGLOB(TCITEMFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTCITEMFields(env, arg3, lparg3, &PGLOB(TCITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3, &PGLOB(TBBUTTONINFOFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3, &PGLOB(TBBUTTONINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3, &PGLOB(TBBUTTONFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTBBUTTONFields(env, arg3, lparg3, &PGLOB(TBBUTTONFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3, &PGLOB(RECTFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setRECTFields(env, arg3, lparg3, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3, &PGLOB(REBARBANDINFOFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3, &PGLOB(REBARBANDINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3, &PGLOB(LVITEMFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVITEMFields(env, arg3, lparg3, &PGLOB(LVITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3, &PGLOB(LVHITTESTINFOFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3, &PGLOB(LVHITTESTINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3, &PGLOB(LVCOLUMNFc));

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVCOLUMNFields(env, arg3, lparg3, &PGLOB(LVCOLUMNFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__IIII
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SendMessageA\n")

	return (jint)SendMessageA((HWND)arg0, arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__III_3S
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__III_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__III_3B
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageA((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__II_3II
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);

	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageA__II_3I_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageA\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3, &PGLOB(TVITEMFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVITEMFields(env, arg3, lparg3, &PGLOB(TVITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3, &PGLOB(TVINSERTSTRUCTFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3, &PGLOB(TVINSERTSTRUCTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3, &PGLOB(TVHITTESTINFOFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3, &PGLOB(TVHITTESTINFOFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3, &PGLOB(TOOLINFOFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTOOLINFOFields(env, arg3, lparg3, &PGLOB(TOOLINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TCITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3, &PGLOB(TCITEMFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTCITEMFields(env, arg3, lparg3, &PGLOB(TCITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3, &PGLOB(TBBUTTONINFOFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3, &PGLOB(TBBUTTONINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3, &PGLOB(TBBUTTONFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setTBBUTTONFields(env, arg3, lparg3, &PGLOB(TBBUTTONFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3, &PGLOB(RECTFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setRECTFields(env, arg3, lparg3, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3, &PGLOB(REBARBANDINFOFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3, &PGLOB(REBARBANDINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVITEM _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3, &PGLOB(LVITEMFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVITEMFields(env, arg3, lparg3, &PGLOB(LVITEMFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3, &PGLOB(LVHITTESTINFOFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3, &PGLOB(LVHITTESTINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3, &PGLOB(LVCOLUMNFc));

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) setLVCOLUMNFields(env, arg3, lparg3, &PGLOB(LVCOLUMNFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__IIII
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SendMessageW\n")

	return (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__III_3S
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__III_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__III_3C
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);

	rc = (jint)SendMessageW((HWND)arg0, arg1, arg2, (LPARAM)lparg3);

	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__II_3II
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);

	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessageW__II_3I_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("SendMessageW\n")

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
			rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)&wParam, (LPARAM)&lParam);
			lparg2[0] = wParam;  lparg3[0] = lParam;
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetActiveWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetActiveWindow\n")

	return (jint)SetActiveWindow((HWND)arg0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetBkColor
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetBkColor\n")

	return (jint)SetBkColor((HDC)arg0, (COLORREF)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetBkMode
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetBkMode\n")

	return (jint)SetBkMode((HDC)arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetCapture
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetCapture\n")

	return (jint)SetCapture((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetCaretPos
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetCaretPos\n")

	return (jboolean)SetCaretPos(arg0, arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetClipboardData
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetClipboardData\n")

	return (jint)SetClipboardData(arg0, (HANDLE)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetCursor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetCursor\n")

	return (jint)SetCursor((HCURSOR)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetCursorPos
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetCursorPos\n")

	return (jboolean)SetCursorPos(arg0, arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetDIBColorTable
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
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetFocus
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetFocus\n")

	return (jint)SetFocus((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetForegroundWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetForegroundWindow\n")

	return (jboolean)SetForegroundWindow((HWND)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetMenu
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetMenu\n")

	return (jboolean)SetMenu((HWND)arg0, (HMENU)arg1);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetMenuDefaultItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetMenuDefaultItem\n")

	return (jboolean)SetMenuDefaultItem((HMENU)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetMenuInfo
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = (jboolean)FALSE;
    HMODULE hm;
    FARPROC fp;

	DEBUG_CALL("SetMenuInfo\n")
	
    /*
    *  SetMenuInfo is a Win2000 and Win98 specific call
    *  If you link it into swt.dll a system modal entry point not found dialog will
    *  appear as soon as swt.dll is loaded. Here we check for the entry point and
    *  only do the call if it exists.
    */
    if ((hm=GetModuleHandle("user32.dll")) && (fp=GetProcAddress(hm, "SetMenuInfo"))) {

		if (arg1) lparg1 = getMENUINFOFields(env, arg1, &_arg1, &PGLOB(MENUINFOFc));

        rc = (jboolean) (fp)((HMENU)arg0, lparg1);
//		rc = (jboolean)SetMenuInfo(arg0, lparg1);

		if (arg1) setMENUINFOFields(env, arg1, lparg1, &PGLOB(MENUINFOFc));
	}

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetMenuItemInfoA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("SetMenuItemInfoA\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)SetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetMenuItemInfoW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;

	DEBUG_CALL("SetMenuItemInfoW\n")

	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3, &PGLOB(MENUITEMINFOFc));

	rc = (jboolean)SetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);

	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3, &PGLOB(MENUITEMINFOFc));

	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetPaletteEntries
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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetParent
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetParent\n")

	return (jint)SetParent((HWND)arg0, (HWND)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetPixel
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetPixel\n")

	return (jint)SetPixel((HDC)arg0, arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetROP2
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetROP2\n")

	return (jint)SetROP2((HDC)arg0, arg1);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetRect
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DECL_GLOB(pGlob)
	RECT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("SetRect\n")

	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0, &PGLOB(RECTFc));

	rc = (jboolean)SetRect(lparg0, arg1, arg2, arg3, arg4);

	if (arg0) setRECTFields(env, arg0, lparg0, &PGLOB(RECTFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetRectRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("SetRectRgn\n")

	return (jboolean)SetRectRgn((HRGN)arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetScrollInfo
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3)
{
	DECL_GLOB(pGlob)
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SetScrollInfo\n")

	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2, &PGLOB(SCROLLINFOFc));

	rc = (jboolean)SetScrollInfo((HWND)arg0, arg1, lparg2, arg3);

	if (arg2) setSCROLLINFOFields(env, arg2, lparg2, &PGLOB(SCROLLINFOFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetStretchBltMode
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetStretchBltMode\n")

	return (jint)SetStretchBltMode((HDC)arg0, arg1);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetTextAlign
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetTextAlign\n")

	return (jint)SetTextAlign((HDC)arg0, arg1);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetTextColor
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetTextColor\n")

	return (jint)SetTextColor((HDC)arg0, (COLORREF)arg1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetTimer
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetTimer\n")

	return (jint)SetTimer((HWND)arg0, arg1, arg2, (TIMERPROC)arg3);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowLongA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetWindowLongA\n")

	return (jint)SetWindowLongA((HWND)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowLongW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetWindowLongW\n")

	return (jint)SetWindowLongW((HWND)arg0, arg1, arg2);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowPlacement
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("SetWindowPlacement\n")

	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1, &PGLOB(WINDOWPLACEMENTFc));

	rc = (jboolean)SetWindowPlacement((HWND)arg0, lparg1);

	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1, &PGLOB(WINDOWPLACEMENTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowPos
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("SetWindowPos\n")

	return (jboolean)SetWindowPos((HWND)arg0, (HWND)arg1, arg2, arg3, arg4, arg5, arg6);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowTextA
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
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowTextW
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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowsHookExA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetWindowsHookExA\n")

	return (jint)SetWindowsHookExA(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SetWindowsHookExW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetWindowsHookExW\n")

	return (jint)SetWindowsHookExW(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShellExecuteExA
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ShellExecuteExA\n")

	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0, &PGLOB(SHELLEXECUTEINFOFc));

	rc = (jboolean)ShellExecuteExA(lparg0);

	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0, &PGLOB(SHELLEXECUTEINFOFc));

	return rc;
}
#endif // _WIN32_WCE

#ifdef _WIN32_WCE
#define ShellExecuteExW ShellExecuteEx
#define LPSHELLEXECUTEINFOW LPSHELLEXECUTEINFO
#endif // _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShellExecuteExW
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("ShellExecuteExW\n")

	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0, &PGLOB(SHELLEXECUTEINFOFc));

	rc = (jboolean)ShellExecuteExW((LPSHELLEXECUTEINFOW)lparg0);

	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0, &PGLOB(SHELLEXECUTEINFOFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShowCaret
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ShowCaret\n")

	return (jboolean)ShowCaret((HWND)arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShowOwnedPopups
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("ShowOwnedPopups\n")

	return (jboolean)ShowOwnedPopups((HWND)arg0, arg1);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShowScrollBar
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("ShowScrollBar\n")

	return (jboolean)ShowScrollBar((HWND)arg0, arg1, arg2);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ShowWindow
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("ShowWindow\n")

	return (jboolean)ShowWindow((HWND)arg0, arg1);
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_StartDocA
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("StartDocA\n")

	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1, &PGLOB(DOCINFOFc));

	rc = (jint)StartDocA((HDC)arg0, lparg1);

	if (arg1) setDOCINFOFields(env, arg1, lparg1, &PGLOB(DOCINFOFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_StartDocW
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("StartDocW\n")

	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1, &PGLOB(DOCINFOFc));

	rc = (jint)StartDocW((HDC)arg0, (LPDOCINFOW)lparg1);

	if (arg1) setDOCINFOFields(env, arg1, lparg1, &PGLOB(DOCINFOFc));

	return rc;
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_StartPage
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("StartPage\n")

	return (jint)StartPage((HDC)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_StretchBlt
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	DEBUG_CALL("StretchBlt\n")

	return (jboolean)StretchBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8, arg9, arg10);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2, &PGLOB(RECTFc));

	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);

	if (arg2) setRECTFields(env, arg2, lparg2, &PGLOB(RECTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	RECT _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I\n")

	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2, &PGLOB(RECTFc));

	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);

	if (arg2) setRECTFields(env, arg2, lparg2, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
    (JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	NONCLIENTMETRICSA _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I\n")

	if (arg2) lparg2 = getNONCLIENTMETRICSAFields(env, arg2, &_arg2, &PGLOB(NONCLIENTMETRICSFc));

	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);

	if (arg2) setNONCLIENTMETRICSAFields(env, arg2, lparg2, &PGLOB(NONCLIENTMETRICSFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I
    (JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	NONCLIENTMETRICSW _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICS_2I\n")

	if (arg2) lparg2 = getNONCLIENTMETRICSWFields(env, arg2, &_arg2, &PGLOB(NONCLIENTMETRICSFc));

	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);

	if (arg2) setNONCLIENTMETRICSWFields(env, arg2, lparg2, &PGLOB(NONCLIENTMETRICSFc));

	return rc;
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoA__II_3II
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoA__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);

	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_SystemParametersInfoW__II_3II
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	DECL_GLOB(pGlob)
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SystemParametersInfoW__II_3II\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);

	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);

	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ToAscii
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_ToUnicode
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
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_TrackMouseEvent
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	TRACKMOUSEEVENT _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("TrackMouseEvent\n")

	if (arg0) lparg0 = getTRACKMOUSEEVENTFields(env, arg0, &_arg0, &PGLOB(TRACKMOUSEEVENTFc));

	rc = (jboolean)_TrackMouseEvent(lparg0);

	if (arg0) setTRACKMOUSEEVENTFields(env, arg0, lparg0, &PGLOB(TRACKMOUSEEVENTFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_TrackPopupMenu
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6)
{
	DECL_GLOB(pGlob)
	RECT _arg6, *lparg6=NULL;
	jboolean rc;

	DEBUG_CALL("TrackPopupMenu\n")

	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6, &PGLOB(RECTFc));

	rc = (jboolean)TrackPopupMenu((HMENU)arg0, arg1, arg2, arg3, arg4, (HWND)arg5, lparg6);

	if (arg6) setRECTFields(env, arg6, lparg6, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_TranslateAcceleratorA
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	MSG _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TranslateAcceleratorA\n")

	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2, &PGLOB(MSGFc));

	rc = (jint)TranslateAcceleratorA((HWND)arg0, (HACCEL)arg1, lparg2);

	if (arg2) setMSGFields(env, arg2, lparg2, &PGLOB(MSGFc));

	return rc;
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_TranslateAcceleratorW
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DECL_GLOB(pGlob)
	MSG _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TranslateAcceleratorW\n")

	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2, &PGLOB(MSGFc));

	rc = (jint)TranslateAcceleratorW((HWND)arg0, (HACCEL)arg1, lparg2);

	if (arg2) setMSGFields(env, arg2, lparg2, &PGLOB(MSGFc));

	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_TranslateCharsetInfo
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_TranslateMessage
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	MSG _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("TranslateMessage\n")

	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0, &PGLOB(MSGFc));

	rc = (jboolean)TranslateMessage(lparg0);

	if (arg0) setMSGFields(env, arg0, lparg0, &PGLOB(MSGFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_UnhookWindowsHookEx
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("UnhookWindowsHookEx\n")

	return (jboolean)UnhookWindowsHookEx((HHOOK)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_UnregisterClassA
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
#endif // _WIN32_WCE

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_UnregisterClassW
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

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_UpdateWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("UpdateWindow\n")

	return (jboolean)UpdateWindow((HWND)arg0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_ValidateRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
	RECT _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ValidateRect\n")

	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1, &PGLOB(RECTFc));

	rc = (jboolean)ValidateRect((HWND)arg0, lparg1);

	if (arg1) setRECTFields(env, arg1, lparg1, &PGLOB(RECTFc));

	return rc;
}

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_VkKeyScanA
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("VkKeyScanA\n")

	return (jshort)VkKeyScanA((TCHAR)arg0);
}
#endif // _WIN32_WCE

#ifndef _WIN32_WCE
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_win32_OS_VkKeyScanW
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("VkKeyScanW\n")

	return (jshort)VkKeyScanW((WCHAR)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_VtblCall
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0)
{
    P_OLE_FN_2 fn;

	DEBUG_CALL("VtblCall\n")

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0);
}

#ifndef _WIN32_WCE
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_win32_OS_WaitMessage
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("WaitMessage\n")

	return (jboolean)WaitMessage();
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_WideCharToMultiByte
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
{
	jchar *lparg2=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc;

	DEBUG_CALL("WideCharToMultiByte\n")

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

#ifndef _WIN32_WCE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_WindowFromDC
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("WindowFromDC\n")

	return (jint)WindowFromDC((HDC)arg0);
}
#endif // _WIN32_WCE

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_WindowFromPoint
	(JNIEnv *env, jclass that, jobject arg0)
{
	DECL_GLOB(pGlob)
	POINT _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("WindowFromPoint\n")

	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0, &PGLOB(POINTFc));

	rc = (jint)WindowFromPoint(*lparg0);

	if (arg0) setPOINTFields(env, arg0, lparg0, &PGLOB(POINTFc));

	return rc;
}

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
#include "os_structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_win32_OS_##func

#ifndef NO_AbortDoc
JNIEXPORT jint JNICALL OS_NATIVE(AbortDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "AbortDoc\n")
	rc = (jint)AbortDoc((HDC)arg0);
	NATIVE_EXIT(env, that, "AbortDoc\n")
	return rc;
}
#endif

#ifndef NO_ActivateKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(ActivateKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "ActivateKeyboardLayout\n")
	rc = (jint)ActivateKeyboardLayout((HKL)arg0, arg1);
	NATIVE_EXIT(env, that, "ActivateKeyboardLayout\n")
	return rc;
}
#endif

#ifndef NO_AdjustWindowRectEx
JNIEXPORT jboolean JNICALL OS_NATIVE(AdjustWindowRectEx)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jboolean arg2, jint arg3)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "AdjustWindowRectEx\n")
	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	rc = (jboolean)AdjustWindowRectEx(lparg0, arg1, arg2, arg3);
	if (arg0) setRECTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "AdjustWindowRectEx\n")
	return rc;
}
#endif

#ifndef NO_Arc
JNIEXPORT jboolean JNICALL OS_NATIVE(Arc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "Arc\n")
	rc = (jboolean)Arc((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "Arc\n")
	return rc;
}
#endif

#ifndef NO_BeginDeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(BeginDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "BeginDeferWindowPos\n")
	rc = (jint)BeginDeferWindowPos(arg0);
	NATIVE_EXIT(env, that, "BeginDeferWindowPos\n")
	return rc;
}
#endif

#ifndef NO_BeginPaint
JNIEXPORT jint JNICALL OS_NATIVE(BeginPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "BeginPaint\n")
	if (arg1) lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1);
	rc = (jint)BeginPaint((HWND)arg0, lparg1);
	if (arg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "BeginPaint\n")
	return rc;
}
#endif

#ifndef NO_BitBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(BitBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "BitBlt\n")
	rc = (jboolean)BitBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "BitBlt\n")
	return rc;
}
#endif

#ifndef NO_BringWindowToTop
JNIEXPORT jboolean JNICALL OS_NATIVE(BringWindowToTop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "BringWindowToTop\n")
	rc = (jboolean)BringWindowToTop((HWND)arg0);
	NATIVE_EXIT(env, that, "BringWindowToTop\n")
	return rc;
}
#endif

#ifndef NO_CallNextHookEx
JNIEXPORT jint JNICALL OS_NATIVE(CallNextHookEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CallNextHookEx\n")
	rc = (jint)CallNextHookEx((HHOOK)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "CallNextHookEx\n")
	return rc;
}
#endif

#ifndef NO_CallWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "CallWindowProcA\n")
	rc = (jint)CallWindowProcA((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "CallWindowProcA\n")
	return rc;
}
#endif

#ifndef NO_CallWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "CallWindowProcW\n")
	rc = (jint)CallWindowProcW((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "CallWindowProcW\n")
	return rc;
}
#endif

#ifndef NO_CharLowerA
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CharLowerA\n")
	rc = (jshort)CharLowerA((LPSTR)arg0);
	NATIVE_EXIT(env, that, "CharLowerA\n")
	return rc;
}
#endif

#ifndef NO_CharLowerW
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CharLowerW\n")
	rc = (jshort)CharLowerW((LPWSTR)arg0);
	NATIVE_EXIT(env, that, "CharLowerW\n")
	return rc;
}
#endif

#ifndef NO_CharUpperA
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CharUpperA\n")
	rc = (jshort)CharUpperA((LPSTR)arg0);
	NATIVE_EXIT(env, that, "CharUpperA\n")
	return rc;
}
#endif

#ifndef NO_CharUpperW
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CharUpperW\n")
	rc = (jshort)CharUpperW((LPWSTR)arg0);
	NATIVE_EXIT(env, that, "CharUpperW\n")
	return rc;
}
#endif

#ifndef NO_CheckMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(CheckMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CheckMenuItem\n")
	rc = (jboolean)CheckMenuItem((HMENU)arg0, (UINT)arg1, (UINT)arg2);
	NATIVE_EXIT(env, that, "CheckMenuItem\n")
	return rc;
}
#endif

#ifndef NO_ChooseColorA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ChooseColorA\n")
	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseColorA(lparg0);
	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ChooseColorA\n")
	return rc;
}
#endif

#ifndef NO_ChooseColorW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ChooseColorW\n")
	if (arg0) lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseColorW((LPCHOOSECOLORW)lparg0);
	if (arg0) setCHOOSECOLORFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ChooseColorW\n")
	return rc;
}
#endif

#ifndef NO_ChooseFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ChooseFontA\n")
	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseFontA(lparg0);
	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ChooseFontA\n")
	return rc;
}
#endif

#ifndef NO_ChooseFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ChooseFontW\n")
	if (arg0) lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0);
	rc = (jboolean)ChooseFontW((LPCHOOSEFONTW)lparg0);
	if (arg0) setCHOOSEFONTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ChooseFontW\n")
	return rc;
}
#endif

#ifndef NO_ClientToScreen
JNIEXPORT jboolean JNICALL OS_NATIVE(ClientToScreen)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ClientToScreen\n")
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)ClientToScreen((HWND)arg0, lparg1);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ClientToScreen\n")
	return rc;
}
#endif

#ifndef NO_CloseClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(CloseClipboard)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CloseClipboard\n")
	rc = (jboolean)CloseClipboard();
	NATIVE_EXIT(env, that, "CloseClipboard\n")
	return rc;
}
#endif

#ifndef NO_CombineRgn
JNIEXPORT jint JNICALL OS_NATIVE(CombineRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CombineRgn\n")
	rc = (jint)CombineRgn((HRGN)arg0, (HRGN)arg1, (HRGN)arg2, arg3);
	NATIVE_EXIT(env, that, "CombineRgn\n")
	return rc;
}
#endif

#ifndef NO_CommDlgExtendedError
JNIEXPORT jint JNICALL OS_NATIVE(CommDlgExtendedError)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CommDlgExtendedError\n")
	rc = (jint)CommDlgExtendedError();
	NATIVE_EXIT(env, that, "CommDlgExtendedError\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1AddAdornments
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1AddAdornments)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CommandBar_1AddAdornments\n")
	rc = (jboolean)CommandBar_AddAdornments((HWND)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "CommandBar_1AddAdornments\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1Create
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "CommandBar_1Create\n")
	rc = (jint)CommandBar_Create((HINSTANCE)arg0, (HWND)arg1, arg2);
	NATIVE_EXIT(env, that, "CommandBar_1Create\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1Destroy
JNIEXPORT void JNICALL OS_NATIVE(CommandBar_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CommandBar_1Destroy\n")
	CommandBar_Destroy((HWND)arg0);
	NATIVE_EXIT(env, that, "CommandBar_1Destroy\n")
}
#endif

#ifndef NO_CommandBar_1DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CommandBar_1DrawMenuBar\n")
	rc = (jboolean)CommandBar_DrawMenuBar((HWND)arg0, (WORD)arg1);
	NATIVE_EXIT(env, that, "CommandBar_1DrawMenuBar\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1Height
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CommandBar_1Height\n")
	rc = (jint)CommandBar_Height((HWND)arg0);
	NATIVE_EXIT(env, that, "CommandBar_1Height\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1InsertMenubarEx
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1InsertMenubarEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CommandBar_1InsertMenubarEx\n")
	rc = (jboolean)CommandBar_InsertMenubarEx((HWND)arg0, (HINSTANCE)arg1, (LPTSTR)arg2, (WORD)arg3);
	NATIVE_EXIT(env, that, "CommandBar_1InsertMenubarEx\n")
	return rc;
}
#endif

#ifndef NO_CommandBar_1Show
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1Show)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CommandBar_1Show\n")
	rc = (jboolean)CommandBar_Show((HWND)arg0, (BOOL)arg1);
	NATIVE_EXIT(env, that, "CommandBar_1Show\n")
	return rc;
}
#endif

#ifndef NO_CopyImage
JNIEXPORT jint JNICALL OS_NATIVE(CopyImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "CopyImage\n")
	rc = (jint)CopyImage((HANDLE)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "CopyImage\n")
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableA
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateAcceleratorTableA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)CreateAcceleratorTableA((LPACCEL)lparg0, arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CreateAcceleratorTableA\n")
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableW
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableW)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateAcceleratorTableW\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)CreateAcceleratorTableW((LPACCEL)lparg0, arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CreateAcceleratorTableW\n")
	return rc;
}
#endif

#ifndef NO_CreateBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateBitmap\n")
	if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	rc = (jint)CreateBitmap(arg0, arg1, arg2, arg3, (CONST VOID *)lparg4);
	if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, JNI_ABORT);
	NATIVE_EXIT(env, that, "CreateBitmap\n")
	return rc;
}
#endif

#ifndef NO_CreateCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(CreateCaret)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "CreateCaret\n")
	rc = (jboolean)CreateCaret((HWND)arg0, (HBITMAP)arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "CreateCaret\n")
	return rc;
}
#endif

#ifndef NO_CreateCompatibleBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateCompatibleBitmap\n")
	rc = (jint)CreateCompatibleBitmap((HDC)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "CreateCompatibleBitmap\n")
	return rc;
}
#endif

#ifndef NO_CreateCompatibleDC
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateCompatibleDC\n")
	rc = (jint)CreateCompatibleDC((HDC)arg0);
	NATIVE_EXIT(env, that, "CreateCompatibleDC\n")
	return rc;
}
#endif

#ifndef NO_CreateCursor
JNIEXPORT jint JNICALL OS_NATIVE(CreateCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateCursor\n")
	if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
	rc = (jint)CreateCursor((HINSTANCE)arg0, arg1, arg2, arg3, arg4, (CONST VOID *)lparg5, (CONST VOID *)lparg6);
	if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, JNI_ABORT);
	if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	NATIVE_EXIT(env, that, "CreateCursor\n")
	return rc;
}
#endif

#ifndef NO_CreateDCA
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateDCA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CreateDCA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)arg2, (CONST DEVMODE *)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CreateDCA\n")
	return rc;
}
#endif

#ifndef NO_CreateDCW
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateDCW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)CreateDCW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)arg2, (CONST DEVMODEW *)arg3);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CreateDCW\n")
	return rc;
}
#endif

#ifndef NO_CreateDIBSection
JNIEXPORT jint JNICALL OS_NATIVE(CreateDIBSection)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateDIBSection\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)CreateDIBSection((HDC)arg0, (BITMAPINFO *)lparg1, arg2, (VOID **)lparg3, (HANDLE)arg4, arg5);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "CreateDIBSection\n")
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectA__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateFontIndirectA__I\n")
	rc = (jint)CreateFontIndirectA((LPLOGFONTA)arg0);
	NATIVE_EXIT(env, that, "CreateFontIndirectA__I\n")
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTA _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2\n")
	if (arg0) lparg0 = getLOGFONTAFields(env, arg0, &_arg0);
	rc = (jint)CreateFontIndirectA(lparg0);
	NATIVE_EXIT(env, that, "CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2\n")
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectW__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateFontIndirectW__I\n")
	rc = (jint)CreateFontIndirectW((LPLOGFONTW)arg0);
	NATIVE_EXIT(env, that, "CreateFontIndirectW__I\n")
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTW _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2\n")
	if (arg0) lparg0 = getLOGFONTWFields(env, arg0, &_arg0);
	rc = (jint)CreateFontIndirectW(lparg0);
	NATIVE_EXIT(env, that, "CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2\n")
	return rc;
}
#endif

#ifndef NO_CreateIconIndirect
JNIEXPORT jint JNICALL OS_NATIVE(CreateIconIndirect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ICONINFO _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateIconIndirect\n")
	if (arg0) lparg0 = getICONINFOFields(env, arg0, &_arg0);
	rc = (jint)CreateIconIndirect(lparg0);
	NATIVE_EXIT(env, that, "CreateIconIndirect\n")
	return rc;
}
#endif

#ifndef NO_CreateMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreateMenu)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateMenu\n")
	rc = (jint)CreateMenu();
	NATIVE_EXIT(env, that, "CreateMenu\n")
	return rc;
}
#endif

#ifndef NO_CreatePalette
JNIEXPORT jint JNICALL OS_NATIVE(CreatePalette)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePalette\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	rc = (jint)CreatePalette((LOGPALETTE *)lparg0);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	NATIVE_EXIT(env, that, "CreatePalette\n")
	return rc;
}
#endif

#ifndef NO_CreatePatternBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreatePatternBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreatePatternBrush\n")
	rc = (jint)CreatePatternBrush((HBITMAP)arg0);
	NATIVE_EXIT(env, that, "CreatePatternBrush\n")
	return rc;
}
#endif

#ifndef NO_CreatePen
JNIEXPORT jint JNICALL OS_NATIVE(CreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreatePen\n")
	rc = (jint)CreatePen(arg0, arg1, (COLORREF)arg2);
	NATIVE_EXIT(env, that, "CreatePen\n")
	return rc;
}
#endif

#ifndef NO_CreatePolygonRgn
JNIEXPORT jint JNICALL OS_NATIVE(CreatePolygonRgn)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePolygonRgn\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)CreatePolygonRgn((CONST POINT *)lparg0, arg1, arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CreatePolygonRgn\n")
	return rc;
}
#endif

#ifndef NO_CreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupMenu)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreatePopupMenu\n")
	rc = (jint)CreatePopupMenu();
	NATIVE_EXIT(env, that, "CreatePopupMenu\n")
	return rc;
}
#endif

#ifndef NO_CreateRectRgn
JNIEXPORT jint JNICALL OS_NATIVE(CreateRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateRectRgn\n")
	rc = (jint)CreateRectRgn(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "CreateRectRgn\n")
	return rc;
}
#endif

#ifndef NO_CreateSolidBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreateSolidBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CreateSolidBrush\n")
	rc = (jint)CreateSolidBrush((COLORREF)arg0);
	NATIVE_EXIT(env, that, "CreateSolidBrush\n")
	return rc;
}
#endif

#ifndef NO_CreateStreamOnHGlobal
JNIEXPORT jint JNICALL OS_NATIVE(CreateStreamOnHGlobal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateStreamOnHGlobal\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CreateStreamOnHGlobal((HGLOBAL)arg0, (BOOL)arg1, (LPSTREAM *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "CreateStreamOnHGlobal\n")
	return rc;
}
#endif

#ifndef NO_CreateWindowExA
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateWindowExA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11);
	rc = (jint)CreateWindowExA(arg0, (LPSTR)lparg1, lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateWindowExA\n")
	return rc;
}
#endif

#ifndef NO_CreateWindowExW
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jobject arg11)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateWindowExW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg11) lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11);
	rc = (jint)CreateWindowExW(arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
	if (arg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateWindowExW\n")
	return rc;
}
#endif

#ifndef NO_DefFrameProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefFrameProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefFrameProcA\n")
	rc = (jint)DefFrameProcA((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	NATIVE_EXIT(env, that, "DefFrameProcA\n")
	return rc;
}
#endif

#ifndef NO_DefFrameProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefFrameProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefFrameProcW\n")
	rc = (jint)DefFrameProcW((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	NATIVE_EXIT(env, that, "DefFrameProcW\n")
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefMDIChildProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefMDIChildProcA\n")
	rc = (jint)DefMDIChildProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "DefMDIChildProcA\n")
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefMDIChildProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefMDIChildProcW\n")
	rc = (jint)DefMDIChildProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "DefMDIChildProcW\n")
	return rc;
}
#endif

#ifndef NO_DefWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefWindowProcA\n")
	rc = (jint)DefWindowProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "DefWindowProcA\n")
	return rc;
}
#endif

#ifndef NO_DefWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "DefWindowProcW\n")
	rc = (jint)DefWindowProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "DefWindowProcW\n")
	return rc;
}
#endif

#ifndef NO_DeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(DeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc;
	NATIVE_ENTER(env, that, "DeferWindowPos\n")
	rc = (jint)DeferWindowPos((HDWP)arg0, (HWND)arg1, (HWND)arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "DeferWindowPos\n")
	return rc;
}
#endif

#ifndef NO_DeleteDC
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DeleteDC\n")
	rc = (jboolean)DeleteDC((HDC)arg0);
	NATIVE_EXIT(env, that, "DeleteDC\n")
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DeleteMenu\n")
	rc = (jboolean)DeleteMenu((HMENU)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "DeleteMenu\n")
	return rc;
}
#endif

#ifndef NO_DeleteObject
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DeleteObject\n")
	rc = (jboolean)DeleteObject((HGDIOBJ)arg0);
	NATIVE_EXIT(env, that, "DeleteObject\n")
	return rc;
}
#endif

#ifndef NO_DestroyAcceleratorTable
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyAcceleratorTable)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyAcceleratorTable\n")
	rc = (jboolean)DestroyAcceleratorTable((HACCEL)arg0);
	NATIVE_EXIT(env, that, "DestroyAcceleratorTable\n")
	return rc;
}
#endif

#ifndef NO_DestroyCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCaret)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyCaret\n")
	rc = (jboolean)DestroyCaret();
	NATIVE_EXIT(env, that, "DestroyCaret\n")
	return rc;
}
#endif

#ifndef NO_DestroyCursor
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyCursor\n")
	rc = (jboolean)DestroyCursor((HCURSOR)arg0);
	NATIVE_EXIT(env, that, "DestroyCursor\n")
	return rc;
}
#endif

#ifndef NO_DestroyIcon
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyIcon)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyIcon\n")
	rc = (jboolean)DestroyIcon((HICON)arg0);
	NATIVE_EXIT(env, that, "DestroyIcon\n")
	return rc;
}
#endif

#ifndef NO_DestroyMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyMenu\n")
	rc = (jboolean)DestroyMenu((HMENU)arg0);
	NATIVE_EXIT(env, that, "DestroyMenu\n")
	return rc;
}
#endif

#ifndef NO_DestroyWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DestroyWindow\n")
	rc = (jboolean)DestroyWindow((HWND)arg0);
	NATIVE_EXIT(env, that, "DestroyWindow\n")
	return rc;
}
#endif

#ifndef NO_DispatchMessageA
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DispatchMessageA\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jint)DispatchMessageA(lparg0);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DispatchMessageA\n")
	return rc;
}
#endif

#ifndef NO_DispatchMessageW
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DispatchMessageW\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jint)DispatchMessageW(lparg0);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DispatchMessageW\n")
	return rc;
}
#endif

#ifndef NO_DragDetect
JNIEXPORT jboolean JNICALL OS_NATIVE(DragDetect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "DragDetect\n")
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)DragDetect((HWND)arg0, *lparg1);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "DragDetect\n")
	return rc;
}
#endif

#ifndef NO_DragFinish
JNIEXPORT void JNICALL OS_NATIVE(DragFinish)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DragFinish\n")
	DragFinish((HDROP)arg0);
	NATIVE_EXIT(env, that, "DragFinish\n")
}
#endif

#ifndef NO_DragQueryFileA
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DragQueryFileA\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)DragQueryFileA((HDROP)arg0, arg1, (LPTSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "DragQueryFileA\n")
	return rc;
}
#endif

#ifndef NO_DragQueryFileW
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DragQueryFileW\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)DragQueryFileW((HDROP)arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "DragQueryFileW\n")
	return rc;
}
#endif

#ifndef NO_DrawEdge
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawEdge)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawEdge\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawEdge((HDC)arg0, lparg1, arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "DrawEdge\n")
	return rc;
}
#endif

#ifndef NO_DrawFocusRect
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFocusRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawFocusRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawFocusRect((HDC)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "DrawFocusRect\n")
	return rc;
}
#endif

#ifndef NO_DrawFrameControl
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFrameControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawFrameControl\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)DrawFrameControl((HDC)arg0, lparg1, arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "DrawFrameControl\n")
	return rc;
}
#endif

#ifndef NO_DrawIconEx
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawIconEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawIconEx\n")
	rc = (jboolean)DrawIconEx((HDC)arg0, arg1, arg2, (HICON)arg3, arg4, arg5, arg6, (HBRUSH)arg7, arg8);
	NATIVE_EXIT(env, that, "DrawIconEx\n")
	return rc;
}
#endif

#ifndef NO_DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawMenuBar\n")
	rc = (jboolean)DrawMenuBar((HWND)arg0);
	NATIVE_EXIT(env, that, "DrawMenuBar\n")
	return rc;
}
#endif

#ifndef NO_DrawStateA
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawStateA\n")
	rc = (jboolean)DrawStateA((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
	NATIVE_EXIT(env, that, "DrawStateA\n")
	return rc;
}
#endif

#ifndef NO_DrawStateW
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "DrawStateW\n")
	rc = (jboolean)DrawStateW((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
	NATIVE_EXIT(env, that, "DrawStateW\n")
	return rc;
}
#endif

#ifndef NO_DrawTextA
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawTextA\n")
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jint)DrawTextA((HDC)arg0, (LPSTR)lparg1, arg2, lparg3, arg4);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg3) setRECTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "DrawTextA\n")
	return rc;
}
#endif

#ifndef NO_DrawTextW
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jchar *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawTextW\n")
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jint)DrawTextW((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3, arg4);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg3) setRECTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "DrawTextW\n")
	return rc;
}
#endif

#ifndef NO_Ellipse
JNIEXPORT jboolean JNICALL OS_NATIVE(Ellipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "Ellipse\n")
	rc = (jboolean)Ellipse((HDC)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "Ellipse\n")
	return rc;
}
#endif

#ifndef NO_EnableMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnableMenuItem\n")
	rc = (jboolean)EnableMenuItem((HMENU)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "EnableMenuItem\n")
	return rc;
}
#endif

#ifndef NO_EnableScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnableScrollBar\n")
	rc = (jboolean)EnableScrollBar((HWND)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "EnableScrollBar\n")
	return rc;
}
#endif

#ifndef NO_EnableWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnableWindow\n")
	rc = (jboolean)EnableWindow((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "EnableWindow\n")
	return rc;
}
#endif

#ifndef NO_EndDeferWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(EndDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EndDeferWindowPos\n")
	rc = (jboolean)EndDeferWindowPos((HDWP)arg0);
	NATIVE_EXIT(env, that, "EndDeferWindowPos\n")
	return rc;
}
#endif

#ifndef NO_EndDoc
JNIEXPORT jint JNICALL OS_NATIVE(EndDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "EndDoc\n")
	rc = (jint)EndDoc((HDC)arg0);
	NATIVE_EXIT(env, that, "EndDoc\n")
	return rc;
}
#endif

#ifndef NO_EndPage
JNIEXPORT jint JNICALL OS_NATIVE(EndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "EndPage\n")
	rc = (jint)EndPage((HDC)arg0);
	NATIVE_EXIT(env, that, "EndPage\n")
	return rc;
}
#endif

#ifndef NO_EndPaint
JNIEXPORT jint JNICALL OS_NATIVE(EndPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "EndPaint\n")
	if (arg1) lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1);
	rc = (jint)EndPaint((HWND)arg0, lparg1);
	if (arg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "EndPaint\n")
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesA
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "EnumFontFamiliesA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)EnumFontFamiliesA((HDC)arg0, (LPSTR)lparg1, (FONTENUMPROC)arg2, (LPARAM)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "EnumFontFamiliesA\n")
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesW
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "EnumFontFamiliesW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)EnumFontFamiliesW((HDC)arg0, (LPCWSTR)lparg1, (FONTENUMPROCW)arg2, (LPARAM)arg3);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "EnumFontFamiliesW\n")
	return rc;
}
#endif

#ifndef NO_EnumSystemLocalesA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnumSystemLocalesA\n")
	rc = (jboolean)EnumSystemLocalesA((LOCALE_ENUMPROCA)arg0, arg1);
	NATIVE_EXIT(env, that, "EnumSystemLocalesA\n")
	return rc;
}
#endif

#ifndef NO_EnumSystemLocalesW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EnumSystemLocalesW\n")
	rc = (jboolean)EnumSystemLocalesW((LOCALE_ENUMPROCW)arg0, arg1);
	NATIVE_EXIT(env, that, "EnumSystemLocalesW\n")
	return rc;
}
#endif

#ifndef NO_EqualRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "EqualRect\n")
	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)EqualRect((CONST RECT *)lparg0, (CONST RECT *)lparg1);
	NATIVE_EXIT(env, that, "EqualRect\n")
	return rc;
}
#endif

#ifndef NO_EqualRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EqualRgn\n")
	rc = (jboolean)EqualRgn((HRGN)arg0, (HRGN)arg1);
	NATIVE_EXIT(env, that, "EqualRgn\n")
	return rc;
}
#endif

#ifndef NO_ExpandEnvironmentStringsA
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ExpandEnvironmentStringsA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)ExpandEnvironmentStringsA(lparg0, lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ExpandEnvironmentStringsA\n")
	return rc;
}
#endif

#ifndef NO_ExpandEnvironmentStringsW
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ExpandEnvironmentStringsW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)ExpandEnvironmentStringsW(lparg0, lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ExpandEnvironmentStringsW\n")
	return rc;
}
#endif

#ifndef NO_ExtTextOutA
JNIEXPORT jboolean JNICALL OS_NATIVE(ExtTextOutA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	RECT _arg4, *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ExtTextOutA\n")
	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
	rc = (jboolean)ExtTextOutA((HDC)arg0, arg1, arg2, arg3, lparg4, (LPSTR)lparg5, arg6, (CONST INT *)lparg7);
	if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, JNI_ABORT);
	if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	NATIVE_EXIT(env, that, "ExtTextOutA\n")
	return rc;
}
#endif

#ifndef NO_ExtTextOutW
JNIEXPORT jboolean JNICALL OS_NATIVE(ExtTextOutW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jcharArray arg5, jint arg6, jintArray arg7)
{
	RECT _arg4, *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg7=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ExtTextOutW\n")
	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
	rc = (jboolean)ExtTextOutW((HDC)arg0, arg1, arg2, arg3, lparg4, (LPWSTR)lparg5, arg6, (CONST INT *)lparg7);
	if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, JNI_ABORT);
	if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	NATIVE_EXIT(env, that, "ExtTextOutW\n")
	return rc;
}
#endif

#ifndef NO_ExtractIconExA
JNIEXPORT jint JNICALL OS_NATIVE(ExtractIconExA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintArray arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ExtractIconExA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)ExtractIconExA((LPSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ExtractIconExA\n")
	return rc;
}
#endif

#ifndef NO_ExtractIconExW
JNIEXPORT jint JNICALL OS_NATIVE(ExtractIconExW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintArray arg2, jintArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ExtractIconExW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)ExtractIconExW((LPWSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ExtractIconExW\n")
	return rc;
}
#endif

#ifndef NO_FillRect
JNIEXPORT jint JNICALL OS_NATIVE(FillRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FillRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jint)FillRect((HDC)arg0, lparg1, (HBRUSH)arg2);
	NATIVE_EXIT(env, that, "FillRect\n")
	return rc;
}
#endif

#ifndef NO_FindWindowA
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FindWindowA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FindWindowA((LPSTR)lparg0, (LPSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FindWindowA\n")
	return rc;
}
#endif

#ifndef NO_FindWindowW
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FindWindowW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)FindWindowW((LPWSTR)lparg0, (LPWSTR)lparg1);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FindWindowW\n")
	return rc;
}
#endif

#ifndef NO_FreeLibrary
JNIEXPORT jboolean JNICALL OS_NATIVE(FreeLibrary)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "FreeLibrary\n")
	rc = (jboolean)FreeLibrary((HMODULE)arg0);
	NATIVE_EXIT(env, that, "FreeLibrary\n")
	return rc;
}
#endif

#ifndef NO_GetACP
JNIEXPORT jint JNICALL OS_NATIVE(GetACP)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetACP\n")
	rc = (jint)GetACP();
	NATIVE_EXIT(env, that, "GetACP\n")
	return rc;
}
#endif

#ifndef NO_GetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetActiveWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetActiveWindow\n")
	rc = (jint)GetActiveWindow();
	NATIVE_EXIT(env, that, "GetActiveWindow\n")
	return rc;
}
#endif

#ifndef NO_GetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(GetBkColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetBkColor\n")
	rc = (jint)GetBkColor((HDC)arg0);
	NATIVE_EXIT(env, that, "GetBkColor\n")
	return rc;
}
#endif

#ifndef NO_GetCapture
JNIEXPORT jint JNICALL OS_NATIVE(GetCapture)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCapture\n")
	rc = (jint)GetCapture();
	NATIVE_EXIT(env, that, "GetCapture\n")
	return rc;
}
#endif

#ifndef NO_GetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCaretPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCaretPos\n")
	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jboolean)GetCaretPos(lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetCaretPos\n")
	return rc;
}
#endif

#ifndef NO_GetCharABCWidthsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCharABCWidthsA\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jboolean)GetCharABCWidthsA((HDC)arg0, arg1, arg2, (LPABC)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetCharABCWidthsA\n")
	return rc;
}
#endif

#ifndef NO_GetCharABCWidthsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCharABCWidthsW\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jboolean)GetCharABCWidthsW((HDC)arg0, arg1, arg2, (LPABC)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetCharABCWidthsW\n")
	return rc;
}
#endif

#ifndef NO_GetCharWidthA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCharWidthA\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jboolean)GetCharWidthA((HDC)arg0, arg1, arg2, (LPINT)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetCharWidthA\n")
	return rc;
}
#endif

#ifndef NO_GetCharWidthW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCharWidthW\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jboolean)GetCharWidthW((HDC)arg0, arg1, arg2, (LPINT)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetCharWidthW\n")
	return rc;
}
#endif

#ifndef NO_GetCharacterPlacementA
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetCharacterPlacementA\n")
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jint)GetCharacterPlacementA((HDC)arg0, (LPSTR)lparg1, arg2, arg3, lparg4, arg5);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetCharacterPlacementA\n")
	return rc;
}
#endif

#ifndef NO_GetCharacterPlacementW
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jchar *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetCharacterPlacementW\n")
	if (arg4) lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jint)GetCharacterPlacementW((HDC)arg0, (LPWSTR)lparg1, arg2, arg3, (LPGCP_RESULTSW)lparg4, arg5);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg4) setGCP_RESULTSFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetCharacterPlacementW\n")
	return rc;
}
#endif

#ifndef NO_GetClassInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetClassInfoA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2);
	rc = (jboolean)GetClassInfoA((HINSTANCE)arg0, (LPSTR)lparg1, lparg2);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetClassInfoA\n")
	return rc;
}
#endif

#ifndef NO_GetClassInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jobject arg2)
{
	jchar *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetClassInfoW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = getWNDCLASSFields(env, arg2, &_arg2);
	rc = (jboolean)GetClassInfoW((HINSTANCE)arg0, (LPWSTR)lparg1, (LPWNDCLASSW)lparg2);
	if (arg2) setWNDCLASSFields(env, arg2, lparg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetClassInfoW\n")
	return rc;
}
#endif

#ifndef NO_GetClientRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClientRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetClientRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetClientRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetClientRect\n")
	return rc;
}
#endif

#ifndef NO_GetClipBox
JNIEXPORT jint JNICALL OS_NATIVE(GetClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetClipBox\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jint)GetClipBox((HDC)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetClipBox\n")
	return rc;
}
#endif

#ifndef NO_GetClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetClipRgn\n")
	rc = (jint)GetClipRgn((HDC)arg0, (HRGN)arg1);
	NATIVE_EXIT(env, that, "GetClipRgn\n")
	return rc;
}
#endif

#ifndef NO_GetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetClipboardData\n")
	rc = (jint)GetClipboardData(arg0);
	NATIVE_EXIT(env, that, "GetClipboardData\n")
	return rc;
}
#endif

#ifndef NO_GetClipboardFormatNameA
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetClipboardFormatNameA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetClipboardFormatNameA(arg0, lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetClipboardFormatNameA\n")
	return rc;
}
#endif

#ifndef NO_GetClipboardFormatNameW
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetClipboardFormatNameW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)GetClipboardFormatNameW(arg0, (LPWSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetClipboardFormatNameW\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentObject
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentObject\n")
	rc = (jint)GetCurrentObject((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "GetCurrentObject\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentProcessId)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentProcessId\n")
	rc = (jint)GetCurrentProcessId();
	NATIVE_EXIT(env, that, "GetCurrentProcessId\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentThreadId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentThreadId)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentThreadId\n")
	rc = (jint)GetCurrentThreadId();
	NATIVE_EXIT(env, that, "GetCurrentThreadId\n")
	return rc;
}
#endif

#ifndef NO_GetCursor
JNIEXPORT jint JNICALL OS_NATIVE(GetCursor)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCursor\n")
	rc = (jint)GetCursor();
	NATIVE_EXIT(env, that, "GetCursor\n")
	return rc;
}
#endif

#ifndef NO_GetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCursorPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetCursorPos\n")
	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jboolean)GetCursorPos(lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetCursorPos\n")
	return rc;
}
#endif

#ifndef NO_GetDC
JNIEXPORT jint JNICALL OS_NATIVE(GetDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDC\n")
	rc = (jint)GetDC((HWND)arg0);
	NATIVE_EXIT(env, that, "GetDC\n")
	return rc;
}
#endif

#ifndef NO_GetDCEx
JNIEXPORT jint JNICALL OS_NATIVE(GetDCEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDCEx\n")
	rc = (jint)GetDCEx((HWND)arg0, (HRGN)arg1, arg2);
	NATIVE_EXIT(env, that, "GetDCEx\n")
	return rc;
}
#endif

#ifndef NO_GetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDIBColorTable\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)GetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetDIBColorTable\n")
	return rc;
}
#endif

#ifndef NO_GetDIBits
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jint arg6)
{
	jbyte *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDIBits\n")
	if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	rc = (jint)GetDIBits((HDC)arg0, (HBITMAP)arg1, arg2, arg3, (LPVOID)arg4, (LPBITMAPINFO)lparg5, arg6);
	if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "GetDIBits\n")
	return rc;
}
#endif

#ifndef NO_GetDesktopWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetDesktopWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDesktopWindow\n")
	rc = (jint)GetDesktopWindow();
	NATIVE_EXIT(env, that, "GetDesktopWindow\n")
	return rc;
}
#endif

#ifndef NO_GetDeviceCaps
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceCaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDeviceCaps\n")
	rc = (jint)GetDeviceCaps((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "GetDeviceCaps\n")
	return rc;
}
#endif

#ifndef NO_GetDialogBaseUnits
JNIEXPORT jint JNICALL OS_NATIVE(GetDialogBaseUnits)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDialogBaseUnits\n")
	rc = (jint)GetDialogBaseUnits();
	NATIVE_EXIT(env, that, "GetDialogBaseUnits\n")
	return rc;
}
#endif

#ifndef NO_GetDlgItem
JNIEXPORT jint JNICALL OS_NATIVE(GetDlgItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDlgItem\n")
	rc = (jint)GetDlgItem((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "GetDlgItem\n")
	return rc;
}
#endif

#ifndef NO_GetDoubleClickTime
JNIEXPORT jint JNICALL OS_NATIVE(GetDoubleClickTime)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDoubleClickTime\n")
	rc = (jint)GetDoubleClickTime();
	NATIVE_EXIT(env, that, "GetDoubleClickTime\n")
	return rc;
}
#endif

#ifndef NO_GetFocus
JNIEXPORT jint JNICALL OS_NATIVE(GetFocus)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetFocus\n")
	rc = (jint)GetFocus();
	NATIVE_EXIT(env, that, "GetFocus\n")
	return rc;
}
#endif

#ifndef NO_GetFontLanguageInfo
JNIEXPORT jint JNICALL OS_NATIVE(GetFontLanguageInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetFontLanguageInfo\n")
	rc = (jint)GetFontLanguageInfo((HDC)arg0);
	NATIVE_EXIT(env, that, "GetFontLanguageInfo\n")
	return rc;
}
#endif

#ifndef NO_GetIconInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetIconInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ICONINFO _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetIconInfo\n")
	if (arg1) lparg1 = &_arg1;
	rc = (jboolean)GetIconInfo((HICON)arg0, lparg1);
	if (arg1) setICONINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetIconInfo\n")
	return rc;
}
#endif

#ifndef NO_GetKeyNameTextA
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyNameTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetKeyNameTextA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyNameTextA(arg0, (LPSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetKeyNameTextA\n")
	return rc;
}
#endif

#ifndef NO_GetKeyNameTextW
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyNameTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetKeyNameTextW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyNameTextW(arg0, (LPWSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetKeyNameTextW\n")
	return rc;
}
#endif

#ifndef NO_GetKeyState
JNIEXPORT jshort JNICALL OS_NATIVE(GetKeyState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetKeyState\n")
	rc = (jshort)GetKeyState(arg0);
	NATIVE_EXIT(env, that, "GetKeyState\n")
	return rc;
}
#endif

#ifndef NO_GetKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetKeyboardLayout\n")
	rc = (jint)GetKeyboardLayout(arg0);
	NATIVE_EXIT(env, that, "GetKeyboardLayout\n")
	return rc;
}
#endif

#ifndef NO_GetKeyboardLayoutList
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayoutList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetKeyboardLayoutList\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyboardLayoutList(arg0, (HKL FAR *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetKeyboardLayoutList\n")
	return rc;
}
#endif

#ifndef NO_GetKeyboardState
JNIEXPORT jboolean JNICALL OS_NATIVE(GetKeyboardState)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetKeyboardState\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jboolean)GetKeyboardState((PBYTE)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetKeyboardState\n")
	return rc;
}
#endif

#ifndef NO_GetLastActivePopup
JNIEXPORT jint JNICALL OS_NATIVE(GetLastActivePopup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetLastActivePopup\n")
	rc = (jint)GetLastActivePopup((HWND)arg0);
	NATIVE_EXIT(env, that, "GetLastActivePopup\n")
	return rc;
}
#endif

#ifndef NO_GetLastError
JNIEXPORT jint JNICALL OS_NATIVE(GetLastError)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetLastError\n")
	rc = (jint)GetLastError();
	NATIVE_EXIT(env, that, "GetLastError\n")
	return rc;
}
#endif

#ifndef NO_GetLocaleInfoA
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetLocaleInfoA\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)GetLocaleInfoA(arg0, arg1, (LPSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetLocaleInfoA\n")
	return rc;
}
#endif

#ifndef NO_GetLocaleInfoW
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetLocaleInfoW\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)GetLocaleInfoW(arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetLocaleInfoW\n")
	return rc;
}
#endif

#ifndef NO_GetMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMenu\n")
	rc = (jint)GetMenu((HWND)arg0);
	NATIVE_EXIT(env, that, "GetMenu\n")
	return rc;
}
#endif

#ifndef NO_GetMenuDefaultItem
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuDefaultItem\n")
	rc = (jint)GetMenuDefaultItem((HMENU)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "GetMenuDefaultItem\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemCount
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuItemCount\n")
	rc = (jint)GetMenuItemCount((HMENU)arg0);
	NATIVE_EXIT(env, that, "GetMenuItemCount\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMenuItemInfoA\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)GetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetMenuItemInfoA\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMenuItemInfoW\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)GetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetMenuItemInfoW\n")
	return rc;
}
#endif

#ifndef NO_GetMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMessageA\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)GetMessageA(lparg0, (HWND)arg1, arg2, arg3);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetMessageA\n")
	return rc;
}
#endif

#ifndef NO_GetMessagePos
JNIEXPORT jint JNICALL OS_NATIVE(GetMessagePos)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMessagePos\n")
	rc = (jint)GetMessagePos();
	NATIVE_EXIT(env, that, "GetMessagePos\n")
	return rc;
}
#endif

#ifndef NO_GetMessageTime
JNIEXPORT jint JNICALL OS_NATIVE(GetMessageTime)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMessageTime\n")
	rc = (jint)GetMessageTime();
	NATIVE_EXIT(env, that, "GetMessageTime\n")
	return rc;
}
#endif

#ifndef NO_GetMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetMessageW\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)GetMessageW(lparg0, (HWND)arg1, arg2, arg3);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetMessageW\n")
	return rc;
}
#endif

#ifndef NO_GetModuleHandleA
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetModuleHandleA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)GetModuleHandleA((LPSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetModuleHandleA\n")
	return rc;
}
#endif

#ifndef NO_GetModuleHandleW
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetModuleHandleW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)GetModuleHandleW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetModuleHandleW\n")
	return rc;
}
#endif

#ifndef NO_GetNearestPaletteIndex
JNIEXPORT jint JNICALL OS_NATIVE(GetNearestPaletteIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetNearestPaletteIndex\n")
	rc = (jint)GetNearestPaletteIndex((HPALETTE)arg0, (COLORREF)arg1);
	NATIVE_EXIT(env, that, "GetNearestPaletteIndex\n")
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setBITMAPFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setDIBSECTIONFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGBRUSHFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONTA _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGFONTAFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGPENFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setBITMAPFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setDIBSECTIONFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGBRUSHFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONTW _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGFONTWFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2\n")
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")
	if (arg2) lparg2 = &_arg2;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
	if (arg2) setLOGPENFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2\n")
	return rc;
}
#endif

#ifndef NO_GetOpenFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetOpenFileNameA\n")
	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetOpenFileNameA(lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetOpenFileNameA\n")
	return rc;
}
#endif

#ifndef NO_GetOpenFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetOpenFileNameW\n")
	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetOpenFileNameW((LPOPENFILENAMEW)lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetOpenFileNameW\n")
	return rc;
}
#endif

#ifndef NO_GetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetPaletteEntries\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)GetPaletteEntries((HPALETTE)arg0, arg1, arg2, (LPPALETTEENTRY)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetPaletteEntries\n")
	return rc;
}
#endif

#ifndef NO_GetParent
JNIEXPORT jint JNICALL OS_NATIVE(GetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetParent\n")
	rc = (jint)GetParent((HWND)arg0);
	NATIVE_EXIT(env, that, "GetParent\n")
	return rc;
}
#endif

#ifndef NO_GetPixel
JNIEXPORT jint JNICALL OS_NATIVE(GetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetPixel\n")
	rc = (jint)GetPixel((HDC)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "GetPixel\n")
	return rc;
}
#endif

#ifndef NO_GetProcAddress
JNIEXPORT jint JNICALL OS_NATIVE(GetProcAddress)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetProcAddress\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetProcAddress((HMODULE)arg0, (LPCTSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetProcAddress\n")
	return rc;
}
#endif

#ifndef NO_GetProcessHeap
JNIEXPORT jint JNICALL OS_NATIVE(GetProcessHeap)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetProcessHeap\n")
	rc = (jint)GetProcessHeap();
	NATIVE_EXIT(env, that, "GetProcessHeap\n")
	return rc;
}
#endif

#ifndef NO_GetProfileStringA
JNIEXPORT jint JNICALL OS_NATIVE(GetProfileStringA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetProfileStringA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetProfileStringA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)lparg2, (LPSTR)lparg3, arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetProfileStringA\n")
	return rc;
}
#endif

#ifndef NO_GetProfileStringW
JNIEXPORT jint JNICALL OS_NATIVE(GetProfileStringW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jcharArray arg2, jcharArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetProfileStringW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)GetProfileStringW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)lparg2, (LPWSTR)lparg3, arg4);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetProfileStringW\n")
	return rc;
}
#endif

#ifndef NO_GetROP2
JNIEXPORT jint JNICALL OS_NATIVE(GetROP2)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetROP2\n")
	rc = (jint)GetROP2((HDC)arg0);
	NATIVE_EXIT(env, that, "GetROP2\n")
	return rc;
}
#endif

#ifndef NO_GetRegionData
JNIEXPORT jint JNICALL OS_NATIVE(GetRegionData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetRegionData\n")
	if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	rc = (jint)GetRegionData((HRGN)arg0, arg1, (RGNDATA *)lparg2);
	if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetRegionData\n")
	return rc;
}
#endif

#ifndef NO_GetRgnBox
JNIEXPORT jint JNICALL OS_NATIVE(GetRgnBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetRgnBox\n")
	if (arg1) lparg1 = &_arg1;
	rc = (jint)GetRgnBox((HRGN)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetRgnBox\n")
	return rc;
}
#endif

#ifndef NO_GetSaveFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetSaveFileNameA\n")
	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetSaveFileNameA(lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetSaveFileNameA\n")
	return rc;
}
#endif

#ifndef NO_GetSaveFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetSaveFileNameW\n")
	if (arg0) lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0);
	rc = (jboolean)GetSaveFileNameW((LPOPENFILENAMEW)lparg0);
	if (arg0) setOPENFILENAMEFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetSaveFileNameW\n")
	return rc;
}
#endif

#ifndef NO_GetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetScrollInfo\n")
	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2);
	rc = (jboolean)GetScrollInfo((HWND)arg0, arg1, lparg2);
	if (arg2) setSCROLLINFOFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetScrollInfo\n")
	return rc;
}
#endif

#ifndef NO_GetStockObject
JNIEXPORT jint JNICALL OS_NATIVE(GetStockObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetStockObject\n")
	rc = (jint)GetStockObject(arg0);
	NATIVE_EXIT(env, that, "GetStockObject\n")
	return rc;
}
#endif

#ifndef NO_GetSysColor
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetSysColor\n")
	rc = (jint)GetSysColor(arg0);
	NATIVE_EXIT(env, that, "GetSysColor\n")
	return rc;
}
#endif

#ifndef NO_GetSysColorBrush
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColorBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetSysColorBrush\n")
	rc = (jint)GetSysColorBrush(arg0);
	NATIVE_EXIT(env, that, "GetSysColorBrush\n")
	return rc;
}
#endif

#ifndef NO_GetSystemMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMenu)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetSystemMenu\n")
	rc = (jint)GetSystemMenu((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "GetSystemMenu\n")
	return rc;
}
#endif

#ifndef NO_GetSystemMetrics
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMetrics)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetSystemMetrics\n")
	rc = (jint)GetSystemMetrics(arg0);
	NATIVE_EXIT(env, that, "GetSystemMetrics\n")
	return rc;
}
#endif

#ifndef NO_GetSystemPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetSystemPaletteEntries\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)GetSystemPaletteEntries((HDC)arg0, (UINT)arg1, (UINT)arg2, (LPPALETTEENTRY)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetSystemPaletteEntries\n")
	return rc;
}
#endif

#ifndef NO_GetTextCharset
JNIEXPORT jint JNICALL OS_NATIVE(GetTextCharset)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetTextCharset\n")
	rc = (jint)GetTextCharset((HDC)arg0);
	NATIVE_EXIT(env, that, "GetTextCharset\n")
	return rc;
}
#endif

#ifndef NO_GetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(GetTextColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetTextColor\n")
	rc = (jint)GetTextColor((HDC)arg0);
	NATIVE_EXIT(env, that, "GetTextColor\n")
	return rc;
}
#endif

#ifndef NO_GetTextExtentPoint32A
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32A)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3)
{
	jbyte *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetTextExtentPoint32A\n")
	if (arg3) lparg3 = &_arg3;
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jboolean)GetTextExtentPoint32A((HDC)arg0, (LPSTR)lparg1, arg2, lparg3);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg3) setSIZEFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetTextExtentPoint32A\n")
	return rc;
}
#endif

#ifndef NO_GetTextExtentPoint32W
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32W)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3)
{
	jchar *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetTextExtentPoint32W\n")
	if (arg3) lparg3 = &_arg3;
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jboolean)GetTextExtentPoint32W((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg3) setSIZEFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetTextExtentPoint32W\n")
	return rc;
}
#endif

#ifndef NO_GetTextMetricsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICA _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetTextMetricsA\n")
	if (arg1) lparg1 = &_arg1;
	rc = (jboolean)GetTextMetricsA((HDC)arg0, lparg1);
	if (arg1) setTEXTMETRICAFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetTextMetricsA\n")
	return rc;
}
#endif

#ifndef NO_GetTextMetricsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICW _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetTextMetricsW\n")
	if (arg1) lparg1 = &_arg1;
	rc = (jboolean)GetTextMetricsW((HDC)arg0, lparg1);
	if (arg1) setTEXTMETRICWFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetTextMetricsW\n")
	return rc;
}
#endif

#ifndef NO_GetTickCount
JNIEXPORT jint JNICALL OS_NATIVE(GetTickCount)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetTickCount\n")
	rc = (jint)GetTickCount();
	NATIVE_EXIT(env, that, "GetTickCount\n")
	return rc;
}
#endif

#ifndef NO_GetUpdateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetUpdateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetUpdateRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetUpdateRect((HWND)arg0, (LPRECT)lparg1, (BOOL)arg2);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetUpdateRect\n")
	return rc;
}
#endif

#ifndef NO_GetUpdateRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetUpdateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetUpdateRgn\n")
	rc = (jint)GetUpdateRgn((HWND)arg0, (HRGN)arg1, arg2);
	NATIVE_EXIT(env, that, "GetUpdateRgn\n")
	return rc;
}
#endif

#ifndef NO_GetVersionExA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOA _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetVersionExA\n")
	if (arg0) lparg0 = getOSVERSIONINFOAFields(env, arg0, &_arg0);
	rc = (jboolean)GetVersionExA(lparg0);
	if (arg0) setOSVERSIONINFOAFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetVersionExA\n")
	return rc;
}
#endif

#ifndef NO_GetVersionExW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOW _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetVersionExW\n")
	if (arg0) lparg0 = getOSVERSIONINFOWFields(env, arg0, &_arg0);
	rc = (jboolean)GetVersionExW(lparg0);
	if (arg0) setOSVERSIONINFOWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetVersionExW\n")
	return rc;
}
#endif

#ifndef NO_GetWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindow\n")
	rc = (jint)GetWindow((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "GetWindow\n")
	return rc;
}
#endif

#ifndef NO_GetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowLongA\n")
	rc = (jint)GetWindowLongA((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "GetWindowLongA\n")
	return rc;
}
#endif

#ifndef NO_GetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowLongW\n")
	rc = (jint)GetWindowLongW((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "GetWindowLongW\n")
	return rc;
}
#endif

#ifndef NO_GetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetWindowPlacement\n")
	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1);
	rc = (jboolean)GetWindowPlacement((HWND)arg0, lparg1);
	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetWindowPlacement\n")
	return rc;
}
#endif

#ifndef NO_GetWindowRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "GetWindowRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)GetWindowRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetWindowRect\n")
	return rc;
}
#endif

#ifndef NO_GetWindowRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowRgn\n")
	rc = (jint)GetWindowRgn((HWND)arg0, (HRGN)arg1);
	NATIVE_EXIT(env, that, "GetWindowRgn\n")
	return rc;
}
#endif

#ifndef NO_GetWindowTextA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowTextA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowTextA((HWND)arg0, (LPSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowTextA\n")
	return rc;
}
#endif

#ifndef NO_GetWindowTextLengthA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthA)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowTextLengthA\n")
	rc = (jint)GetWindowTextLengthA((HWND)arg0);
	NATIVE_EXIT(env, that, "GetWindowTextLengthA\n")
	return rc;
}
#endif

#ifndef NO_GetWindowTextLengthW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthW)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowTextLengthW\n")
	rc = (jint)GetWindowTextLengthW((HWND)arg0);
	NATIVE_EXIT(env, that, "GetWindowTextLengthW\n")
	return rc;
}
#endif

#ifndef NO_GetWindowTextW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowTextW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowTextW((HWND)arg0, (LPWSTR)lparg1, arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowTextW\n")
	return rc;
}
#endif

#ifndef NO_GetWindowThreadProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowThreadProcessId)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowThreadProcessId\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowThreadProcessId((HWND)arg0, (LPDWORD)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowThreadProcessId\n")
	return rc;
}
#endif

#ifndef NO_GlobalAlloc
JNIEXPORT jint JNICALL OS_NATIVE(GlobalAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GlobalAlloc\n")
	rc = (jint)GlobalAlloc(arg0, arg1);
	NATIVE_EXIT(env, that, "GlobalAlloc\n")
	return rc;
}
#endif

#ifndef NO_GlobalFree
JNIEXPORT jint JNICALL OS_NATIVE(GlobalFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GlobalFree\n")
	rc = (jint)GlobalFree((HANDLE)arg0);
	NATIVE_EXIT(env, that, "GlobalFree\n")
	return rc;
}
#endif

#ifndef NO_GlobalLock
JNIEXPORT jint JNICALL OS_NATIVE(GlobalLock)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GlobalLock\n")
	rc = (jint)GlobalLock((HANDLE)arg0);
	NATIVE_EXIT(env, that, "GlobalLock\n")
	return rc;
}
#endif

#ifndef NO_GlobalSize
JNIEXPORT jint JNICALL OS_NATIVE(GlobalSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GlobalSize\n")
	rc = (jint)GlobalSize((HANDLE)arg0);
	NATIVE_EXIT(env, that, "GlobalSize\n")
	return rc;
}
#endif

#ifndef NO_GlobalUnlock
JNIEXPORT jboolean JNICALL OS_NATIVE(GlobalUnlock)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "GlobalUnlock\n")
	rc = (jboolean)GlobalUnlock((HANDLE)arg0);
	NATIVE_EXIT(env, that, "GlobalUnlock\n")
	return rc;
}
#endif

#ifndef NO_HeapAlloc
JNIEXPORT jint JNICALL OS_NATIVE(HeapAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "HeapAlloc\n")
	rc = (jint)HeapAlloc((HANDLE)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "HeapAlloc\n")
	return rc;
}
#endif

#ifndef NO_HeapFree
JNIEXPORT jboolean JNICALL OS_NATIVE(HeapFree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "HeapFree\n")
	rc = (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
	NATIVE_EXIT(env, that, "HeapFree\n")
	return rc;
}
#endif

#ifndef NO_HideCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(HideCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "HideCaret\n")
	rc = (jboolean)HideCaret((HWND)arg0);
	NATIVE_EXIT(env, that, "HideCaret\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1Add
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1Add\n")
	rc = (jint)ImageList_Add((HIMAGELIST)arg0, (HBITMAP)arg1, (HBITMAP)arg2);
	NATIVE_EXIT(env, that, "ImageList_1Add\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1AddMasked
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1AddMasked)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1AddMasked\n")
	rc = (jint)ImageList_AddMasked((HIMAGELIST)arg0, (HBITMAP)arg1, (COLORREF)arg2);
	NATIVE_EXIT(env, that, "ImageList_1AddMasked\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1Create
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1Create\n")
	rc = (jint)ImageList_Create(arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "ImageList_1Create\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1Destroy
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImageList_1Destroy\n")
	rc = (jboolean)ImageList_Destroy((HIMAGELIST)arg0);
	NATIVE_EXIT(env, that, "ImageList_1Destroy\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1GetIcon\n")
	rc = (jint)ImageList_GetIcon((HIMAGELIST)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ImageList_1GetIcon\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1GetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImageList_1GetIconSize\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)ImageList_GetIconSize((HIMAGELIST)arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "ImageList_1GetIconSize\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1GetImageCount
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetImageCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1GetImageCount\n")
	rc = (jint)ImageList_GetImageCount((HIMAGELIST)arg0);
	NATIVE_EXIT(env, that, "ImageList_1GetImageCount\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1Remove
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImageList_1Remove\n")
	rc = (jboolean)ImageList_Remove((HIMAGELIST)arg0, arg1);
	NATIVE_EXIT(env, that, "ImageList_1Remove\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1Replace
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Replace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImageList_1Replace\n")
	rc = (jboolean)ImageList_Replace((HIMAGELIST)arg0, arg1, (HBITMAP)arg2, (HBITMAP)arg3);
	NATIVE_EXIT(env, that, "ImageList_1Replace\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1ReplaceIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1ReplaceIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImageList_1ReplaceIcon\n")
	rc = (jint)ImageList_ReplaceIcon((HIMAGELIST)arg0, arg1, (HICON)arg2);
	NATIVE_EXIT(env, that, "ImageList_1ReplaceIcon\n")
	return rc;
}
#endif

#ifndef NO_ImageList_1SetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1SetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImageList_1SetIconSize\n")
	rc = (jboolean)ImageList_SetIconSize((HIMAGELIST)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ImageList_1SetIconSize\n")
	return rc;
}
#endif

#ifndef NO_ImmAssociateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmAssociateContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImmAssociateContext\n")
	rc = (jint)ImmAssociateContext((HWND)arg0, (HIMC)arg1);
	NATIVE_EXIT(env, that, "ImmAssociateContext\n")
	return rc;
}
#endif

#ifndef NO_ImmCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmCreateContext)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImmCreateContext\n")
	rc = (jint)ImmCreateContext();
	NATIVE_EXIT(env, that, "ImmCreateContext\n")
	return rc;
}
#endif

#ifndef NO_ImmDestroyContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmDestroyContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmDestroyContext\n")
	rc = (jboolean)ImmDestroyContext((HIMC)arg0);
	NATIVE_EXIT(env, that, "ImmDestroyContext\n")
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmGetCompositionFontA\n")
	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	rc = (jboolean)ImmGetCompositionFontA((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTAFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ImmGetCompositionFontA\n")
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmGetCompositionFontW\n")
	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	rc = (jboolean)ImmGetCompositionFontW((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTWFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ImmGetCompositionFontW\n")
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionStringA
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ImmGetCompositionStringA\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)ImmGetCompositionStringA((HIMC)arg0, arg1, (LPSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ImmGetCompositionStringA\n")
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionStringW
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ImmGetCompositionStringW\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ImmGetCompositionStringW\n")
	return rc;
}
#endif

#ifndef NO_ImmGetContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImmGetContext\n")
	rc = (jint)ImmGetContext((HWND)arg0);
	NATIVE_EXIT(env, that, "ImmGetContext\n")
	return rc;
}
#endif

#ifndef NO_ImmGetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmGetConversionStatus\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)ImmGetConversionStatus((HIMC)arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "ImmGetConversionStatus\n")
	return rc;
}
#endif

#ifndef NO_ImmGetDefaultIMEWnd
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetDefaultIMEWnd)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ImmGetDefaultIMEWnd\n")
	rc = (jint)ImmGetDefaultIMEWnd((HWND)arg0);
	NATIVE_EXIT(env, that, "ImmGetDefaultIMEWnd\n")
	return rc;
}
#endif

#ifndef NO_ImmGetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmGetOpenStatus\n")
	rc = (jboolean)ImmGetOpenStatus((HIMC)arg0);
	NATIVE_EXIT(env, that, "ImmGetOpenStatus\n")
	return rc;
}
#endif

#ifndef NO_ImmReleaseContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmReleaseContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmReleaseContext\n")
	rc = (jboolean)ImmReleaseContext((HWND)arg0, (HIMC)arg1);
	NATIVE_EXIT(env, that, "ImmReleaseContext\n")
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmSetCompositionFontA\n")
	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	rc = (jboolean)ImmSetCompositionFontA((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTAFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ImmSetCompositionFontA\n")
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmSetCompositionFontW\n")
	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	rc = (jboolean)ImmSetCompositionFontW((HIMC)arg0, lparg1);
	if (arg1) setLOGFONTWFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ImmSetCompositionFontW\n")
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	COMPOSITIONFORM _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmSetCompositionWindow\n")
	if (arg1) lparg1 = getCOMPOSITIONFORMFields(env, arg1, &_arg1);
	rc = (jboolean)ImmSetCompositionWindow((HIMC)arg0, lparg1);
	if (arg1) setCOMPOSITIONFORMFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ImmSetCompositionWindow\n")
	return rc;
}
#endif

#ifndef NO_ImmSetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmSetConversionStatus\n")
	rc = (jboolean)ImmSetConversionStatus((HIMC)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ImmSetConversionStatus\n")
	return rc;
}
#endif

#ifndef NO_ImmSetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ImmSetOpenStatus\n")
	rc = (jboolean)ImmSetOpenStatus((HIMC)arg0, arg1);
	NATIVE_EXIT(env, that, "ImmSetOpenStatus\n")
	return rc;
}
#endif

#ifndef NO_InitCommonControls
JNIEXPORT void JNICALL OS_NATIVE(InitCommonControls)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "InitCommonControls\n")
	InitCommonControls();
	NATIVE_EXIT(env, that, "InitCommonControls\n")
}
#endif

#ifndef NO_InitCommonControlsEx
JNIEXPORT jboolean JNICALL OS_NATIVE(InitCommonControlsEx)
	(JNIEnv *env, jclass that, jobject arg0)
{
	INITCOMMONCONTROLSEX _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InitCommonControlsEx\n")
	if (arg0) lparg0 = getINITCOMMONCONTROLSEXFields(env, arg0, &_arg0);
	rc = (jboolean)InitCommonControlsEx(lparg0);
	if (arg0) setINITCOMMONCONTROLSEXFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "InitCommonControlsEx\n")
	return rc;
}
#endif

#ifndef NO_InsertMenuA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InsertMenuA\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jboolean)InsertMenuA((HMENU)arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "InsertMenuA\n")
	return rc;
}
#endif

#ifndef NO_InsertMenuItemA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InsertMenuItemA\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)InsertMenuItemA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "InsertMenuItemA\n")
	return rc;
}
#endif

#ifndef NO_InsertMenuItemW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InsertMenuItemW\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)InsertMenuItemW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "InsertMenuItemW\n")
	return rc;
}
#endif

#ifndef NO_InsertMenuW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InsertMenuW\n")
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jboolean)InsertMenuW((HMENU)arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "InsertMenuW\n")
	return rc;
}
#endif

#ifndef NO_InvalidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "InvalidateRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)InvalidateRect((HWND)arg0, lparg1, arg2);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "InvalidateRect\n")
	return rc;
}
#endif

#ifndef NO_InvalidateRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "InvalidateRgn\n")
	rc = (jboolean)InvalidateRgn((HWND)arg0, (HRGN)arg1, arg2);
	NATIVE_EXIT(env, that, "InvalidateRgn\n")
	return rc;
}
#endif

#ifndef NO_IsDBCSLeadByte
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDBCSLeadByte)
	(JNIEnv *env, jclass that, jbyte arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsDBCSLeadByte\n")
	rc = (jboolean)IsDBCSLeadByte(arg0);
	NATIVE_EXIT(env, that, "IsDBCSLeadByte\n")
	return rc;
}
#endif

#ifndef NO_IsIconic
JNIEXPORT jboolean JNICALL OS_NATIVE(IsIconic)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsIconic\n")
	rc = (jboolean)IsIconic((HWND)arg0);
	NATIVE_EXIT(env, that, "IsIconic\n")
	return rc;
}
#endif

#ifndef NO_IsWindowEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsWindowEnabled\n")
	rc = (jboolean)IsWindowEnabled((HWND)arg0);
	NATIVE_EXIT(env, that, "IsWindowEnabled\n")
	return rc;
}
#endif

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsWindowVisible\n")
	rc = (jboolean)IsWindowVisible((HWND)arg0);
	NATIVE_EXIT(env, that, "IsWindowVisible\n")
	return rc;
}
#endif

#ifndef NO_IsZoomed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsZoomed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsZoomed\n")
	rc = (jboolean)IsZoomed((HWND)arg0);
	NATIVE_EXIT(env, that, "IsZoomed\n")
	return rc;
}
#endif

#ifndef NO_KillTimer
JNIEXPORT jboolean JNICALL OS_NATIVE(KillTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "KillTimer\n")
	rc = (jboolean)KillTimer((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "KillTimer\n")
	return rc;
}
#endif

#ifndef NO_LineTo
JNIEXPORT jboolean JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "LineTo\n")
	rc = (jboolean)LineTo((HDC)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "LineTo\n")
	return rc;
}
#endif

#ifndef NO_LoadBitmapA
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadBitmapA\n")
	rc = (jint)LoadBitmapA((HINSTANCE)arg0, (LPSTR)arg1);
	NATIVE_EXIT(env, that, "LoadBitmapA\n")
	return rc;
}
#endif

#ifndef NO_LoadBitmapW
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadBitmapW\n")
	rc = (jint)LoadBitmapW((HINSTANCE)arg0, (LPWSTR)arg1);
	NATIVE_EXIT(env, that, "LoadBitmapW\n")
	return rc;
}
#endif

#ifndef NO_LoadCursorA
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadCursorA\n")
	rc = (jint)LoadCursorA((HINSTANCE)arg0, (LPSTR)arg1);
	NATIVE_EXIT(env, that, "LoadCursorA\n")
	return rc;
}
#endif

#ifndef NO_LoadCursorW
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadCursorW\n")
	rc = (jint)LoadCursorW((HINSTANCE)arg0, (LPWSTR)arg1);
	NATIVE_EXIT(env, that, "LoadCursorW\n")
	return rc;
}
#endif

#ifndef NO_LoadIconA
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadIconA\n")
	rc = (jint)LoadIconA((HINSTANCE)arg0, (LPSTR)arg1);
	NATIVE_EXIT(env, that, "LoadIconA\n")
	return rc;
}
#endif

#ifndef NO_LoadIconW
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "LoadIconW\n")
	rc = (jint)LoadIconW((HINSTANCE)arg0, (LPWSTR)arg1);
	NATIVE_EXIT(env, that, "LoadIconW\n")
	return rc;
}
#endif

#ifndef NO_LoadImageA
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "LoadImageA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)LoadImageA((HINSTANCE)arg0, (LPSTR)lparg1, arg2, arg3, arg4, arg5);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "LoadImageA\n")
	return rc;
}
#endif

#ifndef NO_LoadImageW
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "LoadImageW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)LoadImageW((HINSTANCE)arg0, (LPWSTR)lparg1, arg2, arg3, arg4, arg5);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "LoadImageW\n")
	return rc;
}
#endif

#ifndef NO_LoadLibraryA
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "LoadLibraryA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)LoadLibraryA((LPSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "LoadLibraryA\n")
	return rc;
}
#endif

#ifndef NO_LoadLibraryW
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "LoadLibraryW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)LoadLibraryW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "LoadLibraryW\n")
	return rc;
}
#endif

#ifndef NO_MapVirtualKeyA
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "MapVirtualKeyA\n")
	rc = (jint)MapVirtualKeyA(arg0, arg1);
	NATIVE_EXIT(env, that, "MapVirtualKeyA\n")
	return rc;
}
#endif

#ifndef NO_MapVirtualKeyW
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "MapVirtualKeyW\n")
	rc = (jint)MapVirtualKeyW(arg0, arg1);
	NATIVE_EXIT(env, that, "MapVirtualKeyW\n")
	return rc;
}
#endif

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	POINT _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I\n")
	if (arg2) lparg2 = getPOINTFields(env, arg2, &_arg2);
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
	if (arg2) setPOINTFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I\n")
	return rc;
}
#endif

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	return rc;
}
#endif

#ifndef NO_MessageBeep
JNIEXPORT jboolean JNICALL OS_NATIVE(MessageBeep)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "MessageBeep\n")
	rc = (jboolean)MessageBeep(arg0);
	NATIVE_EXIT(env, that, "MessageBeep\n")
	return rc;
}
#endif

#ifndef NO_MessageBoxA
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MessageBoxA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)MessageBoxA((HWND)arg0, (LPSTR)lparg1, (LPSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "MessageBoxA\n")
	return rc;
}
#endif

#ifndef NO_MessageBoxW
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MessageBoxW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)MessageBoxW((HWND)arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "MessageBoxW\n")
	return rc;
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DROPFILES _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I\n")
	if (arg1) lparg1 = getDROPFILESFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GRADIENT_RECT _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I\n")
	if (arg1) lparg1 = getGRADIENT_RECTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONTA _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I\n")
	if (arg1) lparg1 = getLOGFONTAFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONTW _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I\n")
	if (arg1) lparg1 = getLOGFONTWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I\n")
	if (arg1) lparg1 = getMEASUREITEMSTRUCTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MSG _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I\n")
	if (arg1) lparg1 = getMSGFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I\n")
	if (arg1) lparg1 = getNMLVCUSTOMDRAWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMLVDISPINFO _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I\n")
	if (arg1) lparg1 = getNMLVDISPINFOFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOA _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I\n")
	if (arg1) lparg1 = getNMTTDISPINFOAFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOW _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I\n")
	if (arg1) lparg1 = getNMTTDISPINFOWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I\n")
	if (arg1) lparg1 = getNMTVCUSTOMDRAWFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	TRIVERTEX _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I\n")
	if (arg1) lparg1 = getTRIVERTEXFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I\n")
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	WINDOWPOS _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I\n")
	if (arg1) lparg1 = getWINDOWPOSFields(env, arg1, &_arg1);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	NATIVE_EXIT(env, that, "MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I\n")
}
#endif

#ifndef NO_MoveMemory__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3BI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3BI\n")
}
#endif

#ifndef NO_MoveMemory__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3CI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3CI\n")
}
#endif

#ifndef NO_MoveMemory__I_3DI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3DI)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3DI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3DI\n")
}
#endif

#ifndef NO_MoveMemory__I_3FI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3FI)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3FI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3FI\n")
}
#endif

#ifndef NO_MoveMemory__I_3II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3II\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3II\n")
}
#endif

#ifndef NO_MoveMemory__I_3SI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__I_3SI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "MoveMemory__I_3SI\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI)
	(JNIEnv *env, jclass that, jobject arg0, jbyteArray arg1, jint arg2)
{
	BITMAPINFOHEADER _arg0, *lparg0=NULL;
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI\n")
	if (arg0) lparg0 = &_arg0;
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg0) setBITMAPINFOHEADERFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DRAWITEMSTRUCT _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setDRAWITEMSTRUCTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HDITEM _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setHDITEMFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HELPINFO _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setHELPINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONTA _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setLOGFONTAFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONTW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setLOGFONTWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setMEASUREITEMSTRUCTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MSG _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMCUSTOMDRAW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMCUSTOMDRAWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHDR _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMHDRFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHEADER _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMHEADERFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLISTVIEW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMLISTVIEWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMLVCUSTOMDRAWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVDISPINFO _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMLVDISPINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMREBARCHEVRON _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMREBARCHEVRONFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMRGINFO _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMRGINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTOOLBAR _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTOOLBARFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOA _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTTDISPINFOAFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTTDISPINFOWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setNMTVCUSTOMDRAWFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TVITEM _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setTVITEMFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II\n")
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	WINDOWPOS _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II\n")
	if (arg0) lparg0 = &_arg0;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) setWINDOWPOSFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II\n")
}
#endif

#ifndef NO_MoveMemory___3BII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3BII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3BII\n")
}
#endif

#ifndef NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	ACCEL _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I\n")
	if (arg1) lparg1 = getACCELFields(env, arg1, &_arg1);
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I\n")
}
#endif

#ifndef NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	BITMAPINFOHEADER _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I\n")
	if (arg1) lparg1 = getBITMAPINFOHEADERFields(env, arg1, &_arg1);
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I\n")
}
#endif

#ifndef NO_MoveMemory___3CII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3CII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3CII\n")
}
#endif

#ifndef NO_MoveMemory___3DII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3DII)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jint arg1, jint arg2)
{
	jdouble *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3DII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3DII\n")
}
#endif

#ifndef NO_MoveMemory___3FII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3FII)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3FII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3FII\n")
}
#endif

#ifndef NO_MoveMemory___3III
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3III\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3III\n")
}
#endif

#ifndef NO_MoveMemory___3SII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3SII)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	NATIVE_ENTER(env, that, "MoveMemory___3SII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "MoveMemory___3SII\n")
}
#endif

#ifndef NO_MoveToEx
JNIEXPORT jboolean JNICALL OS_NATIVE(MoveToEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "MoveToEx\n")
	rc = (jboolean)MoveToEx((HDC)arg0, arg1, arg2, (LPPOINT)arg3);
	NATIVE_EXIT(env, that, "MoveToEx\n")
	return rc;
}
#endif

#ifndef NO_MsgWaitForMultipleObjectsEx
JNIEXPORT jint JNICALL OS_NATIVE(MsgWaitForMultipleObjectsEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "MsgWaitForMultipleObjectsEx\n")
	rc = (jint)MsgWaitForMultipleObjectsEx((DWORD)arg0, (LPHANDLE)arg1, (DWORD)arg2, (DWORD)arg3, (DWORD)arg4);
	NATIVE_EXIT(env, that, "MsgWaitForMultipleObjectsEx\n")
	return rc;
}
#endif

#ifndef NO_MultiByteToWideChar__IIII_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__IIII_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jchar *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MultiByteToWideChar__IIII_3CI\n")
	if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)arg2, arg3, (LPWSTR)lparg4, arg5);
	if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "MultiByteToWideChar__IIII_3CI\n")
	return rc;
}
#endif

#ifndef NO_MultiByteToWideChar__II_3BI_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__II_3BI_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MultiByteToWideChar__II_3BI_3CI\n")
	if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)lparg2, arg3, (LPWSTR)lparg4, arg5);
	if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	NATIVE_EXIT(env, that, "MultiByteToWideChar__II_3BI_3CI\n")
	return rc;
}
#endif

#ifndef NO_OleInitialize
JNIEXPORT jint JNICALL OS_NATIVE(OleInitialize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "OleInitialize\n")
	rc = (jint)OleInitialize((LPVOID)arg0);
	NATIVE_EXIT(env, that, "OleInitialize\n")
	return rc;
}
#endif

#ifndef NO_OleUninitialize
JNIEXPORT void JNICALL OS_NATIVE(OleUninitialize)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "OleUninitialize\n")
	OleUninitialize();
	NATIVE_EXIT(env, that, "OleUninitialize\n")
}
#endif

#ifndef NO_OpenClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(OpenClipboard)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "OpenClipboard\n")
	rc = (jboolean)OpenClipboard((HWND)arg0);
	NATIVE_EXIT(env, that, "OpenClipboard\n")
	return rc;
}
#endif

#ifndef NO_PatBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(PatBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PatBlt\n")
	rc = (jboolean)PatBlt((HDC)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "PatBlt\n")
	return rc;
}
#endif

#ifndef NO_PeekMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PeekMessageA\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)PeekMessageA(lparg0, (HWND)arg1, arg2, arg3, arg4);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PeekMessageA\n")
	return rc;
}
#endif

#ifndef NO_PeekMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PeekMessageW\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)PeekMessageW(lparg0, (HWND)arg1, arg2, arg3, arg4);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PeekMessageW\n")
	return rc;
}
#endif

#ifndef NO_Pie
JNIEXPORT jboolean JNICALL OS_NATIVE(Pie)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "Pie\n")
	rc = (jboolean)Pie((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "Pie\n")
	return rc;
}
#endif

#ifndef NO_Polygon
JNIEXPORT jboolean JNICALL OS_NATIVE(Polygon)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "Polygon\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jboolean)Polygon((HDC)arg0, (CONST POINT *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "Polygon\n")
	return rc;
}
#endif

#ifndef NO_Polyline
JNIEXPORT jboolean JNICALL OS_NATIVE(Polyline)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "Polyline\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	rc = (jboolean)Polyline((HDC)arg0, (CONST POINT *)lparg1, arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "Polyline\n")
	return rc;
}
#endif

#ifndef NO_PostMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PostMessageA\n")
	rc = (jboolean)PostMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "PostMessageA\n")
	return rc;
}
#endif

#ifndef NO_PostMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PostMessageW\n")
	rc = (jboolean)PostMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "PostMessageW\n")
	return rc;
}
#endif

#ifndef NO_PostThreadMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PostThreadMessageA\n")
	rc = (jboolean)PostThreadMessageA(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "PostThreadMessageA\n")
	return rc;
}
#endif

#ifndef NO_PostThreadMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PostThreadMessageW\n")
	rc = (jboolean)PostThreadMessageW(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "PostThreadMessageW\n")
	return rc;
}
#endif

#ifndef NO_PrintDlgA
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PrintDlgA\n")
	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0);
	rc = (jboolean)PrintDlgA(lparg0);
	if (arg0) setPRINTDLGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PrintDlgA\n")
	return rc;
}
#endif

#ifndef NO_PrintDlgW
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PrintDlgW\n")
	if (arg0) lparg0 = getPRINTDLGFields(env, arg0, &_arg0);
	rc = (jboolean)PrintDlgW((LPPRINTDLGW)lparg0);
	if (arg0) setPRINTDLGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PrintDlgW\n")
	return rc;
}
#endif

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	POINT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PtInRect\n")
	if (arg0) lparg0 = getRECTFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)PtInRect(lparg0, *lparg1);
	NATIVE_EXIT(env, that, "PtInRect\n")
	return rc;
}
#endif

#ifndef NO_PtInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "PtInRegion\n")
	rc = (jboolean)PtInRegion((HRGN)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "PtInRegion\n")
	return rc;
}
#endif

#ifndef NO_RealizePalette
JNIEXPORT jint JNICALL OS_NATIVE(RealizePalette)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RealizePalette\n")
	rc = (jint)RealizePalette((HDC)arg0);
	NATIVE_EXIT(env, that, "RealizePalette\n")
	return rc;
}
#endif

#ifndef NO_RectInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "RectInRegion\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)RectInRegion((HRGN)arg0, lparg1);
	NATIVE_EXIT(env, that, "RectInRegion\n")
	return rc;
}
#endif

#ifndef NO_Rectangle
JNIEXPORT jboolean JNICALL OS_NATIVE(Rectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "Rectangle\n")
	rc = (jboolean)Rectangle((HDC)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "Rectangle\n")
	return rc;
}
#endif

#ifndef NO_RedrawWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(RedrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "RedrawWindow\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)RedrawWindow((HWND)arg0, lparg1, (HRGN)arg2, arg3);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "RedrawWindow\n")
	return rc;
}
#endif

#ifndef NO_RegCloseKey
JNIEXPORT jint JNICALL OS_NATIVE(RegCloseKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RegCloseKey\n")
	rc = (jint)RegCloseKey((HKEY)arg0);
	NATIVE_EXIT(env, that, "RegCloseKey\n")
	return rc;
}
#endif

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
	NATIVE_ENTER(env, that, "RegEnumKeyExA\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7);
	rc = (jint)RegEnumKeyExA((HKEY)arg0, arg1, (LPSTR)lparg2, lparg3, lparg4, (LPSTR)lparg5, lparg6, lparg7);
	if (arg7) setFILETIMEFields(env, arg7, lparg7);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "RegEnumKeyExA\n")
	return rc;
}
#endif

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
	NATIVE_ENTER(env, that, "RegEnumKeyExW\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = getFILETIMEFields(env, arg7, &_arg7);
	rc = (jint)RegEnumKeyExW((HKEY)arg0, arg1, (LPWSTR)lparg2, lparg3, lparg4, (LPWSTR)lparg5, lparg6, lparg7);
	if (arg7) setFILETIMEFields(env, arg7, lparg7);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "RegEnumKeyExW\n")
	return rc;
}
#endif

#ifndef NO_RegOpenKeyExA
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegOpenKeyExA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)RegOpenKeyExA((HKEY)arg0, (LPSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "RegOpenKeyExA\n")
	return rc;
}
#endif

#ifndef NO_RegOpenKeyExW
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jchar *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegOpenKeyExW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)RegOpenKeyExW((HKEY)arg0, (LPWSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "RegOpenKeyExW\n")
	return rc;
}
#endif

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
	NATIVE_ENTER(env, that, "RegQueryInfoKeyA\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	if (arg10) lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL);
	rc = (jint)RegQueryInfoKeyA((HKEY)arg0, (LPSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
	if (arg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "RegQueryInfoKeyA\n")
	return rc;
}
#endif

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
	NATIVE_ENTER(env, that, "RegQueryInfoKeyW\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	if (arg10) lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL);
	rc = (jint)RegQueryInfoKeyW((HKEY)arg0, (LPWSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
	if (arg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "RegQueryInfoKeyW\n")
	return rc;
}
#endif

#ifndef NO_RegQueryValueExA
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegQueryValueExA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "RegQueryValueExA\n")
	return rc;
}
#endif

#ifndef NO_RegQueryValueExW
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jintArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegQueryValueExW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "RegQueryValueExW\n")
	return rc;
}
#endif

#ifndef NO_RegisterClassA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegisterClassA\n")
	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0);
	rc = (jint)RegisterClassA(lparg0);
	if (arg0) setWNDCLASSFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "RegisterClassA\n")
	return rc;
}
#endif

#ifndef NO_RegisterClassW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegisterClassW\n")
	if (arg0) lparg0 = getWNDCLASSFields(env, arg0, &_arg0);
	rc = (jint)RegisterClassW((LPWNDCLASSW)lparg0);
	if (arg0) setWNDCLASSFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "RegisterClassW\n")
	return rc;
}
#endif

#ifndef NO_RegisterClipboardFormatA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegisterClipboardFormatA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)RegisterClipboardFormatA((LPTSTR)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "RegisterClipboardFormatA\n")
	return rc;
}
#endif

#ifndef NO_RegisterClipboardFormatW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RegisterClipboardFormatW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jint)RegisterClipboardFormatW((LPWSTR)lparg0);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "RegisterClipboardFormatW\n")
	return rc;
}
#endif

#ifndef NO_ReleaseCapture
JNIEXPORT jboolean JNICALL OS_NATIVE(ReleaseCapture)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ReleaseCapture\n")
	rc = (jboolean)ReleaseCapture();
	NATIVE_EXIT(env, that, "ReleaseCapture\n")
	return rc;
}
#endif

#ifndef NO_ReleaseDC
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseDC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "ReleaseDC\n")
	rc = (jint)ReleaseDC((HWND)arg0, (HDC)arg1);
	NATIVE_EXIT(env, that, "ReleaseDC\n")
	return rc;
}
#endif

#ifndef NO_RemoveMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(RemoveMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "RemoveMenu\n")
	rc = (jboolean)RemoveMenu((HMENU)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "RemoveMenu\n")
	return rc;
}
#endif

#ifndef NO_RoundRect
JNIEXPORT jboolean JNICALL OS_NATIVE(RoundRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "RoundRect\n")
	rc = (jboolean)RoundRect((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "RoundRect\n")
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderA
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SHBrowseForFolderA\n")
	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0);
	rc = (jint)SHBrowseForFolderA(lparg0);
	if (arg0) setBROWSEINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SHBrowseForFolderA\n")
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderW
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SHBrowseForFolderW\n")
	if (arg0) lparg0 = getBROWSEINFOFields(env, arg0, &_arg0);
	rc = (jint)SHBrowseForFolderW((LPBROWSEINFOW)lparg0);
	if (arg0) setBROWSEINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SHBrowseForFolderW\n")
	return rc;
}
#endif

#ifndef NO_SHCreateMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(SHCreateMenuBar)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHMENUBARINFO _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SHCreateMenuBar\n")
	if (arg0) lparg0 = getSHMENUBARINFOFields(env, arg0, &_arg0);
	rc = (jboolean)SHCreateMenuBar((PSHMENUBARINFO)lparg0);
	if (arg0) setSHMENUBARINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SHCreateMenuBar\n")
	return rc;
}
#endif

#ifndef NO_SHGetMalloc
JNIEXPORT jint JNICALL OS_NATIVE(SHGetMalloc)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SHGetMalloc\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)SHGetMalloc((LPMALLOC *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "SHGetMalloc\n")
	return rc;
}
#endif

#ifndef NO_SHGetPathFromIDListA
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SHGetPathFromIDListA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)SHGetPathFromIDListA((LPCITEMIDLIST)arg0, (LPSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "SHGetPathFromIDListA\n")
	return rc;
}
#endif

#ifndef NO_SHGetPathFromIDListW
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SHGetPathFromIDListW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jboolean)SHGetPathFromIDListW((LPCITEMIDLIST)arg0, (LPWSTR)lparg1);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "SHGetPathFromIDListW\n")
	return rc;
}
#endif

#ifndef NO_SHHandleWMSettingChange
JNIEXPORT jboolean JNICALL OS_NATIVE(SHHandleWMSettingChange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SHACTIVATEINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SHHandleWMSettingChange\n")
	if (arg3) lparg3 = getSHACTIVATEINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SHHandleWMSettingChange((HWND)arg0, arg1, arg2, lparg3);
	if (arg3) setSHACTIVATEINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SHHandleWMSettingChange\n")
	return rc;
}
#endif

#ifndef NO_SHRecognizeGesture
JNIEXPORT jint JNICALL OS_NATIVE(SHRecognizeGesture)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHRGINFO _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SHRecognizeGesture\n")
	if (arg0) lparg0 = getSHRGINFOFields(env, arg0, &_arg0);
	rc = (jint)SHRecognizeGesture(lparg0);
	if (arg0) setSHRGINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SHRecognizeGesture\n")
	return rc;
}
#endif

#ifndef NO_SHSendBackToFocusWindow
JNIEXPORT void JNICALL OS_NATIVE(SHSendBackToFocusWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "SHSendBackToFocusWindow\n")
	SHSendBackToFocusWindow(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SHSendBackToFocusWindow\n")
}
#endif

#ifndef NO_SHSetAppKeyWndAssoc
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSetAppKeyWndAssoc)
	(JNIEnv *env, jclass that, jbyte arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SHSetAppKeyWndAssoc\n")
	rc = (jboolean)SHSetAppKeyWndAssoc((BYTE)arg0, (HWND)arg1);
	NATIVE_EXIT(env, that, "SHSetAppKeyWndAssoc\n")
	return rc;
}
#endif

#ifndef NO_SHSipPreference
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSipPreference)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SHSipPreference\n")
	rc = (jboolean)SHSipPreference((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "SHSipPreference\n")
	return rc;
}
#endif

#ifndef NO_ScreenToClient
JNIEXPORT jboolean JNICALL OS_NATIVE(ScreenToClient)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ScreenToClient\n")
	if (arg1) lparg1 = getPOINTFields(env, arg1, &_arg1);
	rc = (jboolean)ScreenToClient((HWND)arg0, lparg1);
	if (arg1) setPOINTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ScreenToClient\n")
	return rc;
}
#endif

#ifndef NO_ScrollWindowEx
JNIEXPORT jint JNICALL OS_NATIVE(ScrollWindowEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jobject arg6, jint arg7)
{
	RECT _arg3, *lparg3=NULL;
	RECT _arg4, *lparg4=NULL;
	RECT _arg6, *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ScrollWindowEx\n")
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	if (arg4) lparg4 = getRECTFields(env, arg4, &_arg4);
	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6);
	rc = (jint)ScrollWindowEx((HWND)arg0, arg1, arg2, lparg3, lparg4, (HRGN)arg5, lparg6, arg7);
	if (arg6) setRECTFields(env, arg6, lparg6);
	if (arg4) setRECTFields(env, arg4, lparg4);
	if (arg3) setRECTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "ScrollWindowEx\n")
	return rc;
}
#endif

#ifndef NO_SelectClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(SelectClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SelectClipRgn\n")
	rc = (jint)SelectClipRgn((HDC)arg0, (HRGN)arg1);
	NATIVE_EXIT(env, that, "SelectClipRgn\n")
	return rc;
}
#endif

#ifndef NO_SelectObject
JNIEXPORT jint JNICALL OS_NATIVE(SelectObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SelectObject\n")
	rc = (jint)SelectObject((HDC)arg0, (HGDIOBJ)arg1);
	NATIVE_EXIT(env, that, "SelectObject\n")
	return rc;
}
#endif

#ifndef NO_SelectPalette
JNIEXPORT jint JNICALL OS_NATIVE(SelectPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SelectPalette\n")
	rc = (jint)SelectPalette((HDC)arg0, (HPALETTE)arg1, arg2);
	NATIVE_EXIT(env, that, "SelectPalette\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIII\n")
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIII\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")
	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVCOLUMNFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")
	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")
	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")
	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2\n")
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setRECTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")
	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")
	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")
	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTCITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")
	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTOOLINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")
	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")
	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")
	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3B
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__III_3B\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageA__III_3B\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__III_3I\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageA__III_3I\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__III_3S\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageA__III_3S\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__II_3II\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SendMessageA__II_3II\n")
	return rc;
}
#endif

#ifndef NO_SendMessageA__II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageA__II_3I_3I\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SendMessageA__II_3I_3I\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIII\n")
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIII\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")
	if (arg3) lparg3 = getLVCOLUMNFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVCOLUMNFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")
	if (arg3) lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")
	if (arg3) lparg3 = getLVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setLVITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")
	if (arg3) lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setREBARBANDINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2\n")
	if (arg3) lparg3 = getRECTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setRECTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")
	if (arg3) lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")
	if (arg3) lparg3 = getTBBUTTONFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTBBUTTONFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")
	if (arg3) lparg3 = getTCITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTCITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")
	if (arg3) lparg3 = getTOOLINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTOOLINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")
	if (arg3) lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")
	if (arg3) lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")
	if (arg3) lparg3 = getTVITEMFields(env, arg3, &_arg3);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) setTVITEMFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3C
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__III_3C\n")
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageW__III_3C\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__III_3I\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageW__III_3I\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__III_3S\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "SendMessageW__III_3S\n")
	return rc;
}
#endif

#ifndef NO_SendMessageW__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SendMessageW__II_3II\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SendMessageW__II_3II\n")
	return rc;
}
#endif

#ifndef NO_SetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(SetActiveWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetActiveWindow\n")
	rc = (jint)SetActiveWindow((HWND)arg0);
	NATIVE_EXIT(env, that, "SetActiveWindow\n")
	return rc;
}
#endif

#ifndef NO_SetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(SetBkColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetBkColor\n")
	rc = (jint)SetBkColor((HDC)arg0, (COLORREF)arg1);
	NATIVE_EXIT(env, that, "SetBkColor\n")
	return rc;
}
#endif

#ifndef NO_SetBkMode
JNIEXPORT jint JNICALL OS_NATIVE(SetBkMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetBkMode\n")
	rc = (jint)SetBkMode((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "SetBkMode\n")
	return rc;
}
#endif

#ifndef NO_SetCapture
JNIEXPORT jint JNICALL OS_NATIVE(SetCapture)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetCapture\n")
	rc = (jint)SetCapture((HWND)arg0);
	NATIVE_EXIT(env, that, "SetCapture\n")
	return rc;
}
#endif

#ifndef NO_SetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCaretPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetCaretPos\n")
	rc = (jboolean)SetCaretPos(arg0, arg1);
	NATIVE_EXIT(env, that, "SetCaretPos\n")
	return rc;
}
#endif

#ifndef NO_SetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(SetClipboardData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetClipboardData\n")
	rc = (jint)SetClipboardData(arg0, (HANDLE)arg1);
	NATIVE_EXIT(env, that, "SetClipboardData\n")
	return rc;
}
#endif

#ifndef NO_SetCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetCursor\n")
	rc = (jint)SetCursor((HCURSOR)arg0);
	NATIVE_EXIT(env, that, "SetCursor\n")
	return rc;
}
#endif

#ifndef NO_SetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCursorPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetCursorPos\n")
	rc = (jboolean)SetCursorPos(arg0, arg1);
	NATIVE_EXIT(env, that, "SetCursorPos\n")
	return rc;
}
#endif

#ifndef NO_SetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(SetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetDIBColorTable\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)SetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, JNI_ABORT);
	NATIVE_EXIT(env, that, "SetDIBColorTable\n")
	return rc;
}
#endif

#ifndef NO_SetErrorMode
JNIEXPORT jint JNICALL OS_NATIVE(SetErrorMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetErrorMode\n")
	rc = (jint)SetErrorMode(arg0);
	NATIVE_EXIT(env, that, "SetErrorMode\n")
	return rc;
}
#endif

#ifndef NO_SetFocus
JNIEXPORT jint JNICALL OS_NATIVE(SetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetFocus\n")
	rc = (jint)SetFocus((HWND)arg0);
	NATIVE_EXIT(env, that, "SetFocus\n")
	return rc;
}
#endif

#ifndef NO_SetForegroundWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(SetForegroundWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetForegroundWindow\n")
	rc = (jboolean)SetForegroundWindow((HWND)arg0);
	NATIVE_EXIT(env, that, "SetForegroundWindow\n")
	return rc;
}
#endif

#ifndef NO_SetMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetMenu\n")
	rc = (jboolean)SetMenu((HWND)arg0, (HMENU)arg1);
	NATIVE_EXIT(env, that, "SetMenu\n")
	return rc;
}
#endif

#ifndef NO_SetMenuDefaultItem
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetMenuDefaultItem\n")
	rc = (jboolean)SetMenuDefaultItem((HMENU)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetMenuDefaultItem\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetMenuItemInfoA\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SetMenuItemInfoA\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetMenuItemInfoW\n")
	if (arg3) lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3);
	rc = (jboolean)SetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
	if (arg3) setMENUITEMINFOFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "SetMenuItemInfoW\n")
	return rc;
}
#endif

#ifndef NO_SetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(SetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetPaletteEntries\n")
	if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	rc = (jint)SetPaletteEntries((HPALETTE)arg0, arg1, arg2, (PALETTEENTRY *)lparg3);
	if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, JNI_ABORT);
	NATIVE_EXIT(env, that, "SetPaletteEntries\n")
	return rc;
}
#endif

#ifndef NO_SetParent
JNIEXPORT jint JNICALL OS_NATIVE(SetParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetParent\n")
	rc = (jint)SetParent((HWND)arg0, (HWND)arg1);
	NATIVE_EXIT(env, that, "SetParent\n")
	return rc;
}
#endif

#ifndef NO_SetPixel
JNIEXPORT jint JNICALL OS_NATIVE(SetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetPixel\n")
	rc = (jint)SetPixel((HDC)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "SetPixel\n")
	return rc;
}
#endif

#ifndef NO_SetROP2
JNIEXPORT jint JNICALL OS_NATIVE(SetROP2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetROP2\n")
	rc = (jint)SetROP2((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "SetROP2\n")
	return rc;
}
#endif

#ifndef NO_SetRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetRect\n")
	if (arg0) lparg0 = &_arg0;
	rc = (jboolean)SetRect(lparg0, arg1, arg2, arg3, arg4);
	if (arg0) setRECTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SetRect\n")
	return rc;
}
#endif

#ifndef NO_SetRectRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetRectRgn\n")
	rc = (jboolean)SetRectRgn((HRGN)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "SetRectRgn\n")
	return rc;
}
#endif

#ifndef NO_SetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetScrollInfo\n")
	if (arg2) lparg2 = getSCROLLINFOFields(env, arg2, &_arg2);
	rc = (jboolean)SetScrollInfo((HWND)arg0, arg1, lparg2, arg3);
	if (arg2) setSCROLLINFOFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SetScrollInfo\n")
	return rc;
}
#endif

#ifndef NO_SetStretchBltMode
JNIEXPORT jint JNICALL OS_NATIVE(SetStretchBltMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetStretchBltMode\n")
	rc = (jint)SetStretchBltMode((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "SetStretchBltMode\n")
	return rc;
}
#endif

#ifndef NO_SetTextAlign
JNIEXPORT jint JNICALL OS_NATIVE(SetTextAlign)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetTextAlign\n")
	rc = (jint)SetTextAlign((HDC)arg0, arg1);
	NATIVE_EXIT(env, that, "SetTextAlign\n")
	return rc;
}
#endif

#ifndef NO_SetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(SetTextColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetTextColor\n")
	rc = (jint)SetTextColor((HDC)arg0, (COLORREF)arg1);
	NATIVE_EXIT(env, that, "SetTextColor\n")
	return rc;
}
#endif

#ifndef NO_SetTimer
JNIEXPORT jint JNICALL OS_NATIVE(SetTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetTimer\n")
	rc = (jint)SetTimer((HWND)arg0, arg1, arg2, (TIMERPROC)arg3);
	NATIVE_EXIT(env, that, "SetTimer\n")
	return rc;
}
#endif

#ifndef NO_SetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowLongA\n")
	rc = (jint)SetWindowLongA((HWND)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetWindowLongA\n")
	return rc;
}
#endif

#ifndef NO_SetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowLongW\n")
	rc = (jint)SetWindowLongW((HWND)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetWindowLongW\n")
	return rc;
}
#endif

#ifndef NO_SetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetWindowPlacement\n")
	if (arg1) lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1);
	rc = (jboolean)SetWindowPlacement((HWND)arg0, lparg1);
	if (arg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetWindowPlacement\n")
	return rc;
}
#endif

#ifndef NO_SetWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "SetWindowPos\n")
	rc = (jboolean)SetWindowPos((HWND)arg0, (HWND)arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "SetWindowPos\n")
	return rc;
}
#endif

#ifndef NO_SetWindowRgn
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowRgn\n")
	rc = (jint)SetWindowRgn((HWND)arg0, (HRGN)arg1, arg2);
	NATIVE_EXIT(env, that, "SetWindowRgn\n")
	return rc;
}
#endif

#ifndef NO_SetWindowTextA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetWindowTextA\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)SetWindowTextA((HWND)arg0, (LPSTR)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "SetWindowTextA\n")
	return rc;
}
#endif

#ifndef NO_SetWindowTextW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SetWindowTextW\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jboolean)SetWindowTextW((HWND)arg0, (LPWSTR)lparg1);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "SetWindowTextW\n")
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowsHookExA\n")
	rc = (jint)SetWindowsHookExA(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	NATIVE_EXIT(env, that, "SetWindowsHookExA\n")
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowsHookExW\n")
	rc = (jint)SetWindowsHookExW(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	NATIVE_EXIT(env, that, "SetWindowsHookExW\n")
	return rc;
}
#endif

#ifndef NO_ShellExecuteExA
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ShellExecuteExA\n")
	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0);
	rc = (jboolean)ShellExecuteExA(lparg0);
	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ShellExecuteExA\n")
	return rc;
}
#endif

#ifndef NO_ShellExecuteExW
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ShellExecuteExW\n")
	if (arg0) lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0);
	rc = (jboolean)ShellExecuteExW((LPSHELLEXECUTEINFOW)lparg0);
	if (arg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ShellExecuteExW\n")
	return rc;
}
#endif

#ifndef NO_ShowCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ShowCaret\n")
	rc = (jboolean)ShowCaret((HWND)arg0);
	NATIVE_EXIT(env, that, "ShowCaret\n")
	return rc;
}
#endif

#ifndef NO_ShowOwnedPopups
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowOwnedPopups)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ShowOwnedPopups\n")
	rc = (jboolean)ShowOwnedPopups((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "ShowOwnedPopups\n")
	return rc;
}
#endif

#ifndef NO_ShowScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ShowScrollBar\n")
	rc = (jboolean)ShowScrollBar((HWND)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ShowScrollBar\n")
	return rc;
}
#endif

#ifndef NO_ShowWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "ShowWindow\n")
	rc = (jboolean)ShowWindow((HWND)arg0, arg1);
	NATIVE_EXIT(env, that, "ShowWindow\n")
	return rc;
}
#endif

#ifndef NO_SipGetInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SipGetInfo)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SIPINFO _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SipGetInfo\n")
	if (arg0) lparg0 = getSIPINFOFields(env, arg0, &_arg0);
	rc = (jboolean)SipGetInfo(lparg0);
	if (arg0) setSIPINFOFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SipGetInfo\n")
	return rc;
}
#endif

#ifndef NO_StartDocA
JNIEXPORT jint JNICALL OS_NATIVE(StartDocA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "StartDocA\n")
	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1);
	rc = (jint)StartDocA((HDC)arg0, lparg1);
	if (arg1) setDOCINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "StartDocA\n")
	return rc;
}
#endif

#ifndef NO_StartDocW
JNIEXPORT jint JNICALL OS_NATIVE(StartDocW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "StartDocW\n")
	if (arg1) lparg1 = getDOCINFOFields(env, arg1, &_arg1);
	rc = (jint)StartDocW((HDC)arg0, (LPDOCINFOW)lparg1);
	if (arg1) setDOCINFOFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "StartDocW\n")
	return rc;
}
#endif

#ifndef NO_StartPage
JNIEXPORT jint JNICALL OS_NATIVE(StartPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "StartPage\n")
	rc = (jint)StartPage((HDC)arg0);
	NATIVE_EXIT(env, that, "StartPage\n")
	return rc;
}
#endif

#ifndef NO_StretchBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(StretchBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "StretchBlt\n")
	rc = (jboolean)StretchBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8, arg9, arg10);
	NATIVE_EXIT(env, that, "StretchBlt\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSA _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I\n")
	if (arg2) lparg2 = getNONCLIENTMETRICSAFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) setNONCLIENTMETRICSAFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoA__II_3II\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SystemParametersInfoA__II_3II\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSW _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I\n")
	if (arg2) lparg2 = getNONCLIENTMETRICSWFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) setNONCLIENTMETRICSWFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	if (arg2) lparg2 = getRECTFields(env, arg2, &_arg2);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) setRECTFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I\n")
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SystemParametersInfoW__II_3II\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SystemParametersInfoW__II_3II\n")
	return rc;
}
#endif

#ifndef NO_ToAscii
JNIEXPORT jint JNICALL OS_NATIVE(ToAscii)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jshortArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ToAscii\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)ToAscii(arg0, arg1, (PBYTE)lparg2, (LPWORD)lparg3, arg4);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ToAscii\n")
	return rc;
}
#endif

#ifndef NO_ToUnicode
JNIEXPORT jint JNICALL OS_NATIVE(ToUnicode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jcharArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ToUnicode\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL);
	rc = (jint)ToUnicode(arg0, arg1, (PBYTE)lparg2, (LPWSTR)lparg3, arg4, arg5);
	if (arg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ToUnicode\n")
	return rc;
}
#endif

#ifndef NO_TrackMouseEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackMouseEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	TRACKMOUSEEVENT _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "TrackMouseEvent\n")
	if (arg0) lparg0 = getTRACKMOUSEEVENTFields(env, arg0, &_arg0);
	rc = (jboolean)TrackMouseEvent(lparg0);
	if (arg0) setTRACKMOUSEEVENTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "TrackMouseEvent\n")
	return rc;
}
#endif

#ifndef NO_TrackPopupMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackPopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6)
{
	RECT _arg6, *lparg6=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "TrackPopupMenu\n")
	if (arg6) lparg6 = getRECTFields(env, arg6, &_arg6);
	rc = (jboolean)TrackPopupMenu((HMENU)arg0, arg1, arg2, arg3, arg4, (HWND)arg5, lparg6);
	if (arg6) setRECTFields(env, arg6, lparg6);
	NATIVE_EXIT(env, that, "TrackPopupMenu\n")
	return rc;
}
#endif

#ifndef NO_TranslateAcceleratorA
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TranslateAcceleratorA\n")
	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2);
	rc = (jint)TranslateAcceleratorA((HWND)arg0, (HACCEL)arg1, lparg2);
	if (arg2) setMSGFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "TranslateAcceleratorA\n")
	return rc;
}
#endif

#ifndef NO_TranslateAcceleratorW
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TranslateAcceleratorW\n")
	if (arg2) lparg2 = getMSGFields(env, arg2, &_arg2);
	rc = (jint)TranslateAcceleratorW((HWND)arg0, (HACCEL)arg1, lparg2);
	if (arg2) setMSGFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "TranslateAcceleratorW\n")
	return rc;
}
#endif

#ifndef NO_TranslateCharsetInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateCharsetInfo)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "TranslateCharsetInfo\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jboolean)TranslateCharsetInfo((DWORD *)arg0, (LPCHARSETINFO)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "TranslateCharsetInfo\n")
	return rc;
}
#endif

#ifndef NO_TranslateMDISysAccel
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateMDISysAccel)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MSG _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "TranslateMDISysAccel\n")
	if (arg1) lparg1 = getMSGFields(env, arg1, &_arg1);
	rc = (jboolean)TranslateMDISysAccel((HWND)arg0, (LPMSG)lparg1);
	if (arg1) setMSGFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TranslateMDISysAccel\n")
	return rc;
}
#endif

#ifndef NO_TranslateMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateMessage)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "TranslateMessage\n")
	if (arg0) lparg0 = getMSGFields(env, arg0, &_arg0);
	rc = (jboolean)TranslateMessage(lparg0);
	if (arg0) setMSGFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "TranslateMessage\n")
	return rc;
}
#endif

#ifndef NO_TransparentImage
JNIEXPORT jboolean JNICALL OS_NATIVE(TransparentImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "TransparentImage\n")
	rc = (jboolean)TransparentImage((HDC)arg0, arg1, arg2, arg3, arg4, (HANDLE)arg5, arg6, arg7, arg8, arg9, (COLORREF)arg10);
	NATIVE_EXIT(env, that, "TransparentImage\n")
	return rc;
}
#endif

#ifndef NO_UnhookWindowsHookEx
JNIEXPORT jboolean JNICALL OS_NATIVE(UnhookWindowsHookEx)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "UnhookWindowsHookEx\n")
	rc = (jboolean)UnhookWindowsHookEx((HHOOK)arg0);
	NATIVE_EXIT(env, that, "UnhookWindowsHookEx\n")
	return rc;
}
#endif

#ifndef NO_UnregisterClassA
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "UnregisterClassA\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jboolean)UnregisterClassA((LPSTR)lparg0, (HINSTANCE)arg1);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "UnregisterClassA\n")
	return rc;
}
#endif

#ifndef NO_UnregisterClassW
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1)
{
	jchar *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "UnregisterClassW\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	rc = (jboolean)UnregisterClassW((LPWSTR)lparg0, (HINSTANCE)arg1);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "UnregisterClassW\n")
	return rc;
}
#endif

#ifndef NO_UpdateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(UpdateWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "UpdateWindow\n")
	rc = (jboolean)UpdateWindow((HWND)arg0);
	NATIVE_EXIT(env, that, "UpdateWindow\n")
	return rc;
}
#endif

#ifndef NO_ValidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(ValidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ValidateRect\n")
	if (arg1) lparg1 = getRECTFields(env, arg1, &_arg1);
	rc = (jboolean)ValidateRect((HWND)arg0, lparg1);
	if (arg1) setRECTFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ValidateRect\n")
	return rc;
}
#endif

#ifndef NO_VkKeyScanA
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "VkKeyScanA\n")
	rc = (jshort)VkKeyScanA((TCHAR)arg0);
	NATIVE_EXIT(env, that, "VkKeyScanA\n")
	return rc;
}
#endif

#ifndef NO_VkKeyScanW
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "VkKeyScanW\n")
	rc = (jshort)VkKeyScanW((WCHAR)arg0);
	NATIVE_EXIT(env, that, "VkKeyScanW\n")
	return rc;
}
#endif

#ifndef NO_WaitMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(WaitMessage)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "WaitMessage\n")
	rc = (jboolean)WaitMessage();
	NATIVE_EXIT(env, that, "WaitMessage\n")
	return rc;
}
#endif

#ifndef NO_WideCharToMultiByte__II_3CIII_3B_3Z
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CIII_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
{
	jchar *lparg2=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "WideCharToMultiByte__II_3CIII_3B_3Z\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL);
	if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)arg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
	if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	if (arg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "WideCharToMultiByte__II_3CIII_3B_3Z\n")
	return rc;
}
#endif

#ifndef NO_WideCharToMultiByte__II_3CI_3BI_3B_3Z
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CI_3BI_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
{
	jchar *lparg2=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "WideCharToMultiByte__II_3CI_3BI_3B_3Z\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL);
	if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)lparg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
	if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	if (arg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "WideCharToMultiByte__II_3CI_3BI_3B_3Z\n")
	return rc;
}
#endif

#ifndef NO_WindowFromDC
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "WindowFromDC\n")
	rc = (jint)WindowFromDC((HDC)arg0);
	NATIVE_EXIT(env, that, "WindowFromDC\n")
	return rc;
}
#endif

#ifndef NO_WindowFromPoint
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromPoint)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "WindowFromPoint\n")
	if (arg0) lparg0 = getPOINTFields(env, arg0, &_arg0);
	rc = (jint)WindowFromPoint(*lparg0);
	if (arg0) setPOINTFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "WindowFromPoint\n")
	return rc;
}
#endif


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

#ifndef NO_AbortDoc
JNIEXPORT jint JNICALL OS_NATIVE(AbortDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AbortDoc_FUNC);
	rc = (jint)AbortDoc((HDC)arg0);
	OS_NATIVE_EXIT(env, that, AbortDoc_FUNC);
	return rc;
}
#endif

#ifndef NO_ActivateKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(ActivateKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ActivateKeyboardLayout_FUNC);
	rc = (jint)ActivateKeyboardLayout((HKL)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ActivateKeyboardLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_AdjustWindowRectEx
JNIEXPORT jboolean JNICALL OS_NATIVE(AdjustWindowRectEx)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jboolean arg2, jint arg3)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AdjustWindowRectEx_FUNC);
	if (arg0) if ((lparg0 = getRECTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)AdjustWindowRectEx(lparg0, arg1, arg2, arg3);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AdjustWindowRectEx_FUNC);
	return rc;
}
#endif

#ifndef NO_AlphaBlend
JNIEXPORT jboolean JNICALL OS_NATIVE(AlphaBlend)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	BLENDFUNCTION _arg10, *lparg10=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AlphaBlend_FUNC);
	if (arg10) if ((lparg10 = getBLENDFUNCTIONFields(env, arg10, &_arg10)) == NULL) goto fail;
/*
	rc = (jboolean)AlphaBlend(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, *lparg10);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(AlphaBlend_LIB);
			if (hm) fp = GetProcAddress(hm, "AlphaBlend");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, *lparg10);
		}
	}
fail:
	if (arg10 && lparg10) setBLENDFUNCTIONFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, AlphaBlend_FUNC);
	return rc;
}
#endif

#ifndef NO_Arc
JNIEXPORT jboolean JNICALL OS_NATIVE(Arc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Arc_FUNC);
	rc = (jboolean)Arc((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, Arc_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginDeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(BeginDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BeginDeferWindowPos_FUNC);
	rc = (jint)BeginDeferWindowPos(arg0);
	OS_NATIVE_EXIT(env, that, BeginDeferWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginPaint
JNIEXPORT jint JNICALL OS_NATIVE(BeginPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BeginPaint_FUNC);
	if (arg1) if ((lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)BeginPaint((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, BeginPaint_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginPath
JNIEXPORT jboolean JNICALL OS_NATIVE(BeginPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, BeginPath_FUNC);
	rc = (jboolean)BeginPath((HDC)arg0);
	OS_NATIVE_EXIT(env, that, BeginPath_FUNC);
	return rc;
}
#endif

#ifndef NO_BitBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(BitBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, BitBlt_FUNC);
	rc = (jboolean)BitBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, BitBlt_FUNC);
	return rc;
}
#endif

#ifndef NO_BringWindowToTop
JNIEXPORT jboolean JNICALL OS_NATIVE(BringWindowToTop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, BringWindowToTop_FUNC);
	rc = (jboolean)BringWindowToTop((HWND)arg0);
	OS_NATIVE_EXIT(env, that, BringWindowToTop_FUNC);
	return rc;
}
#endif

#ifndef NO_Call
JNIEXPORT jint JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DLLVERSIONINFO _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Call_FUNC);
	if (arg1) if ((lparg1 = getDLLVERSIONINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)((DLLGETVERSIONPROC)arg0)(lparg1);
fail:
	if (arg1 && lparg1) setDLLVERSIONINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, Call_FUNC);
	return rc;
}
#endif

#ifndef NO_CallNextHookEx
JNIEXPORT jint JNICALL OS_NATIVE(CallNextHookEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CallNextHookEx_FUNC);
	rc = (jint)CallNextHookEx((HHOOK)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, CallNextHookEx_FUNC);
	return rc;
}
#endif

#ifndef NO_CallWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CallWindowProcA_FUNC);
	rc = (jint)CallWindowProcA((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CallWindowProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_CallWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(CallWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CallWindowProcW_FUNC);
	rc = (jint)CallWindowProcW((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CallWindowProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_CharLowerA
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, CharLowerA_FUNC);
	rc = (jshort)CharLowerA((LPSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharLowerA_FUNC);
	return rc;
}
#endif

#ifndef NO_CharLowerW
JNIEXPORT jshort JNICALL OS_NATIVE(CharLowerW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, CharLowerW_FUNC);
	rc = (jshort)CharLowerW((LPWSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharLowerW_FUNC);
	return rc;
}
#endif

#ifndef NO_CharUpperA
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, CharUpperA_FUNC);
	rc = (jshort)CharUpperA((LPSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharUpperA_FUNC);
	return rc;
}
#endif

#ifndef NO_CharUpperW
JNIEXPORT jshort JNICALL OS_NATIVE(CharUpperW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, CharUpperW_FUNC);
	rc = (jshort)CharUpperW((LPWSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharUpperW_FUNC);
	return rc;
}
#endif

#ifndef NO_CheckMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(CheckMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CheckMenuItem_FUNC);
	rc = (jboolean)CheckMenuItem((HMENU)arg0, (UINT)arg1, (UINT)arg2);
	OS_NATIVE_EXIT(env, that, CheckMenuItem_FUNC);
	return rc;
}
#endif

#ifndef NO_ChooseColorA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ChooseColorA_FUNC);
	if (arg0) if ((lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ChooseColorA(lparg0);
fail:
	if (arg0 && lparg0) setCHOOSECOLORFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ChooseColorA_FUNC);
	return rc;
}
#endif

#ifndef NO_ChooseColorW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseColorW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSECOLOR _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ChooseColorW_FUNC);
	if (arg0) if ((lparg0 = getCHOOSECOLORFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ChooseColorW((LPCHOOSECOLORW)lparg0);
fail:
	if (arg0 && lparg0) setCHOOSECOLORFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ChooseColorW_FUNC);
	return rc;
}
#endif

#ifndef NO_ChooseFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ChooseFontA_FUNC);
	if (arg0) if ((lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ChooseFontA(lparg0);
fail:
	if (arg0 && lparg0) setCHOOSEFONTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ChooseFontA_FUNC);
	return rc;
}
#endif

#ifndef NO_ChooseFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ChooseFontW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CHOOSEFONT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ChooseFontW_FUNC);
	if (arg0) if ((lparg0 = getCHOOSEFONTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ChooseFontW((LPCHOOSEFONTW)lparg0);
fail:
	if (arg0 && lparg0) setCHOOSEFONTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ChooseFontW_FUNC);
	return rc;
}
#endif

#ifndef NO_ClientToScreen
JNIEXPORT jboolean JNICALL OS_NATIVE(ClientToScreen)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ClientToScreen_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ClientToScreen((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ClientToScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(CloseClipboard)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CloseClipboard_FUNC);
	rc = (jboolean)CloseClipboard();
	OS_NATIVE_EXIT(env, that, CloseClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseThemeData
JNIEXPORT jint JNICALL OS_NATIVE(CloseThemeData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CloseThemeData_FUNC);
/*
	rc = (jint)CloseThemeData((HTHEME)arg0);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(CloseThemeData_LIB);
			if (hm) fp = GetProcAddress(hm, "CloseThemeData");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HTHEME)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, CloseThemeData_FUNC);
	return rc;
}
#endif

#ifndef NO_CoCreateInstance
JNIEXPORT jint JNICALL OS_NATIVE(CoCreateInstance)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CoCreateInstance_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoCreateInstance((REFCLSID)lparg0, (LPUNKNOWN)arg1, arg2, (REFIID)lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CoCreateInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_CombineRgn
JNIEXPORT jint JNICALL OS_NATIVE(CombineRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CombineRgn_FUNC);
	rc = (jint)CombineRgn((HRGN)arg0, (HRGN)arg1, (HRGN)arg2, arg3);
	OS_NATIVE_EXIT(env, that, CombineRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_CommDlgExtendedError
JNIEXPORT jint JNICALL OS_NATIVE(CommDlgExtendedError)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CommDlgExtendedError_FUNC);
	rc = (jint)CommDlgExtendedError();
	OS_NATIVE_EXIT(env, that, CommDlgExtendedError_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1AddAdornments
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1AddAdornments)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1AddAdornments_FUNC);
	rc = (jboolean)CommandBar_AddAdornments((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CommandBar_1AddAdornments_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Create
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1Create_FUNC);
	rc = (jint)CommandBar_Create((HINSTANCE)arg0, (HWND)arg1, arg2);
	OS_NATIVE_EXIT(env, that, CommandBar_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Destroy
JNIEXPORT void JNICALL OS_NATIVE(CommandBar_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CommandBar_1Destroy_FUNC);
	CommandBar_Destroy((HWND)arg0);
	OS_NATIVE_EXIT(env, that, CommandBar_1Destroy_FUNC);
}
#endif

#ifndef NO_CommandBar_1DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1DrawMenuBar_FUNC);
	rc = (jboolean)CommandBar_DrawMenuBar((HWND)arg0, (WORD)arg1);
	OS_NATIVE_EXIT(env, that, CommandBar_1DrawMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Height
JNIEXPORT jint JNICALL OS_NATIVE(CommandBar_1Height)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1Height_FUNC);
	rc = (jint)CommandBar_Height((HWND)arg0);
	OS_NATIVE_EXIT(env, that, CommandBar_1Height_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1InsertMenubarEx
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1InsertMenubarEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1InsertMenubarEx_FUNC);
	rc = (jboolean)CommandBar_InsertMenubarEx((HWND)arg0, (HINSTANCE)arg1, (LPTSTR)arg2, (WORD)arg3);
	OS_NATIVE_EXIT(env, that, CommandBar_1InsertMenubarEx_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Show
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1Show)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1Show_FUNC);
	rc = (jboolean)CommandBar_Show((HWND)arg0, (BOOL)arg1);
	OS_NATIVE_EXIT(env, that, CommandBar_1Show_FUNC);
	return rc;
}
#endif

#ifndef NO_CopyImage
JNIEXPORT jint JNICALL OS_NATIVE(CopyImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CopyImage_FUNC);
	rc = (jint)CopyImage((HANDLE)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CopyImage_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableA
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateAcceleratorTableA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CreateAcceleratorTableA((LPACCEL)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateAcceleratorTableA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableW
JNIEXPORT jint JNICALL OS_NATIVE(CreateAcceleratorTableW)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateAcceleratorTableW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CreateAcceleratorTableW((LPACCEL)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateAcceleratorTableW_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateBitmap_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jint)CreateBitmap(arg0, arg1, arg2, arg3, (CONST VOID *)lparg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, JNI_ABORT);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, CreateBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(CreateCaret)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCaret_FUNC);
	rc = (jboolean)CreateCaret((HWND)arg0, (HBITMAP)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, CreateCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCompatibleBitmap
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCompatibleBitmap_FUNC);
	rc = (jint)CreateCompatibleBitmap((HDC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CreateCompatibleBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCompatibleDC
JNIEXPORT jint JNICALL OS_NATIVE(CreateCompatibleDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCompatibleDC_FUNC);
	rc = (jint)CreateCompatibleDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, CreateCompatibleDC_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCursor
JNIEXPORT jint JNICALL OS_NATIVE(CreateCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCursor_FUNC);
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
	OS_NATIVE_EXIT(env, that, CreateCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateDCA
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDCA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateDCA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)arg2, (CONST DEVMODE *)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateDCA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateDCW
JNIEXPORT jint JNICALL OS_NATIVE(CreateDCW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDCW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateDCW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)arg2, (CONST DEVMODEW *)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateDCW_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateDIBSection
JNIEXPORT jint JNICALL OS_NATIVE(CreateDIBSection)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDIBSection_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)CreateDIBSection((HDC)arg0, (BITMAPINFO *)lparg1, arg2, (VOID **)lparg3, (HANDLE)arg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, CreateDIBSection_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectA__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectA__I_FUNC);
	rc = (jint)CreateFontIndirectA((LPLOGFONTA)arg0);
	OS_NATIVE_EXIT(env, that, CreateFontIndirectA__I_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTA _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	if (arg0) if ((lparg0 = getLOGFONTAFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CreateFontIndirectA(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectW__I
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectW__I_FUNC);
	rc = (jint)CreateFontIndirectW((LPLOGFONTW)arg0);
	OS_NATIVE_EXIT(env, that, CreateFontIndirectW__I_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2
JNIEXPORT jint JNICALL OS_NATIVE(CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTW _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	if (arg0) if ((lparg0 = getLOGFONTWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CreateFontIndirectW(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateIconIndirect
JNIEXPORT jint JNICALL OS_NATIVE(CreateIconIndirect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ICONINFO _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateIconIndirect_FUNC);
	if (arg0) if ((lparg0 = getICONINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CreateIconIndirect(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateIconIndirect_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreateMenu)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateMenu_FUNC);
	rc = (jint)CreateMenu();
	OS_NATIVE_EXIT(env, that, CreateMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePalette
JNIEXPORT jint JNICALL OS_NATIVE(CreatePalette)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePalette_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	rc = (jint)CreatePalette((LOGPALETTE *)lparg0);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, JNI_ABORT);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, CreatePalette_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePatternBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreatePatternBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePatternBrush_FUNC);
	rc = (jint)CreatePatternBrush((HBITMAP)arg0);
	OS_NATIVE_EXIT(env, that, CreatePatternBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePen
JNIEXPORT jint JNICALL OS_NATIVE(CreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePen_FUNC);
	rc = (jint)CreatePen(arg0, arg1, (COLORREF)arg2);
	OS_NATIVE_EXIT(env, that, CreatePen_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePolygonRgn
JNIEXPORT jint JNICALL OS_NATIVE(CreatePolygonRgn)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePolygonRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CreatePolygonRgn((CONST POINT *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreatePolygonRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupMenu)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePopupMenu_FUNC);
	rc = (jint)CreatePopupMenu();
	OS_NATIVE_EXIT(env, that, CreatePopupMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateRectRgn
JNIEXPORT jint JNICALL OS_NATIVE(CreateRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateRectRgn_FUNC);
	rc = (jint)CreateRectRgn(arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, CreateRectRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateSolidBrush
JNIEXPORT jint JNICALL OS_NATIVE(CreateSolidBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateSolidBrush_FUNC);
	rc = (jint)CreateSolidBrush((COLORREF)arg0);
	OS_NATIVE_EXIT(env, that, CreateSolidBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateStreamOnHGlobal
JNIEXPORT jint JNICALL OS_NATIVE(CreateStreamOnHGlobal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateStreamOnHGlobal_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CreateStreamOnHGlobal((HGLOBAL)arg0, (BOOL)arg1, (LPSTREAM *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CreateStreamOnHGlobal_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateWindowExA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jint)CreateWindowExA(arg0, (LPSTR)lparg1, lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
fail:
	if (arg11 && lparg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateWindowExA_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateWindowExW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jint)CreateWindowExW(arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
fail:
	if (arg11 && lparg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateWindowExW_FUNC);
	return rc;
}
#endif

#ifndef NO_DefFrameProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefFrameProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefFrameProcA_FUNC);
	rc = (jint)DefFrameProcA((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	OS_NATIVE_EXIT(env, that, DefFrameProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefFrameProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefFrameProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefFrameProcW_FUNC);
	rc = (jint)DefFrameProcW((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	OS_NATIVE_EXIT(env, that, DefFrameProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefMDIChildProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefMDIChildProcA_FUNC);
	rc = (jint)DefMDIChildProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefMDIChildProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefMDIChildProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefMDIChildProcW_FUNC);
	rc = (jint)DefMDIChildProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefMDIChildProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DefWindowProcA
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefWindowProcA_FUNC);
	rc = (jint)DefWindowProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefWindowProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefWindowProcW
JNIEXPORT jint JNICALL OS_NATIVE(DefWindowProcW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DefWindowProcW_FUNC);
	rc = (jint)DefWindowProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefWindowProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DeferWindowPos
JNIEXPORT jint JNICALL OS_NATIVE(DeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DeferWindowPos_FUNC);
	rc = (jint)DeferWindowPos((HDWP)arg0, (HWND)arg1, (HWND)arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, DeferWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteDC
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteDC_FUNC);
	rc = (jboolean)DeleteDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, DeleteDC_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteMenu_FUNC);
	rc = (jboolean)DeleteMenu((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, DeleteMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteObject
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteObject_FUNC);
	rc = (jboolean)DeleteObject((HGDIOBJ)arg0);
	OS_NATIVE_EXIT(env, that, DeleteObject_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyAcceleratorTable
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyAcceleratorTable)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyAcceleratorTable_FUNC);
	rc = (jboolean)DestroyAcceleratorTable((HACCEL)arg0);
	OS_NATIVE_EXIT(env, that, DestroyAcceleratorTable_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCaret)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyCaret_FUNC);
	rc = (jboolean)DestroyCaret();
	OS_NATIVE_EXIT(env, that, DestroyCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyCursor
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyCursor_FUNC);
	rc = (jboolean)DestroyCursor((HCURSOR)arg0);
	OS_NATIVE_EXIT(env, that, DestroyCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyIcon
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyIcon)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyIcon_FUNC);
	rc = (jboolean)DestroyIcon((HICON)arg0);
	OS_NATIVE_EXIT(env, that, DestroyIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyMenu_FUNC);
	rc = (jboolean)DestroyMenu((HMENU)arg0);
	OS_NATIVE_EXIT(env, that, DestroyMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_DestroyWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(DestroyWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyWindow_FUNC);
	rc = (jboolean)DestroyWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, DestroyWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatchMessageA
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatchMessageA_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DispatchMessageA(lparg0);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DispatchMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatchMessageW
JNIEXPORT jint JNICALL OS_NATIVE(DispatchMessageW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DispatchMessageW_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DispatchMessageW(lparg0);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DispatchMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_DragDetect
JNIEXPORT jboolean JNICALL OS_NATIVE(DragDetect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DragDetect_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)DragDetect((HWND)arg0, *lparg1);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DragDetect_FUNC);
	return rc;
}
#endif

#ifndef NO_DragFinish
JNIEXPORT void JNICALL OS_NATIVE(DragFinish)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DragFinish_FUNC);
	DragFinish((HDROP)arg0);
	OS_NATIVE_EXIT(env, that, DragFinish_FUNC);
}
#endif

#ifndef NO_DragQueryFileA
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragQueryFileA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)DragQueryFileA((HDROP)arg0, arg1, (LPTSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, DragQueryFileA_FUNC);
	return rc;
}
#endif

#ifndef NO_DragQueryFileW
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DragQueryFileW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)DragQueryFileW((HDROP)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, DragQueryFileW_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawEdge
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawEdge)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawEdge_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)DrawEdge((HDC)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DrawEdge_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawFocusRect
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFocusRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawFocusRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)DrawFocusRect((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DrawFocusRect_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawFrameControl
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawFrameControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawFrameControl_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)DrawFrameControl((HDC)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DrawFrameControl_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawIconEx
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawIconEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawIconEx_FUNC);
	rc = (jboolean)DrawIconEx((HDC)arg0, arg1, arg2, (HICON)arg3, arg4, arg5, arg6, (HBRUSH)arg7, arg8);
	OS_NATIVE_EXIT(env, that, DrawIconEx_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawMenuBar)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawMenuBar_FUNC);
	rc = (jboolean)DrawMenuBar((HWND)arg0);
	OS_NATIVE_EXIT(env, that, DrawMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawStateA
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawStateA_FUNC);
	rc = (jboolean)DrawStateA((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, DrawStateA_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawStateW
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawStateW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawStateW_FUNC);
	rc = (jboolean)DrawStateW((HDC)arg0, (HBRUSH)arg1, (DRAWSTATEPROC)arg2, (LPARAM)arg3, (WPARAM)arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, DrawStateW_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawTextA
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawTextA_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jint)DrawTextA((HDC)arg0, (LPSTR)lparg1, arg2, lparg3, arg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, DrawTextA_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawTextW
JNIEXPORT jint JNICALL OS_NATIVE(DrawTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3, jint arg4)
{
	jchar *lparg1=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawTextW_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jint)DrawTextW((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3, arg4);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, DrawTextW_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeBackground
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeBackground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	RECT _arg4, *lparg4=NULL;
	RECT _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeBackground_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
/*
	rc = (jint)DrawThemeBackground((HTHEME)arg0, (HDC)arg1, arg2, arg3, (const RECT *)lparg4, (const RECT *)lparg5);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(DrawThemeBackground_LIB);
			if (hm) fp = GetProcAddress(hm, "DrawThemeBackground");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HTHEME)arg0, (HDC)arg1, arg2, arg3, (const RECT *)lparg4, (const RECT *)lparg5);
		}
	}
fail:
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, DrawThemeBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_Ellipse
JNIEXPORT jboolean JNICALL OS_NATIVE(Ellipse)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Ellipse_FUNC);
	rc = (jboolean)Ellipse((HDC)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, Ellipse_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnableMenuItem_FUNC);
	rc = (jboolean)EnableMenuItem((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, EnableMenuItem_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnableScrollBar_FUNC);
	rc = (jboolean)EnableScrollBar((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, EnableScrollBar_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(EnableWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnableWindow_FUNC);
	rc = (jboolean)EnableWindow((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, EnableWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_EndDeferWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(EndDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EndDeferWindowPos_FUNC);
	rc = (jboolean)EndDeferWindowPos((HDWP)arg0);
	OS_NATIVE_EXIT(env, that, EndDeferWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_EndDoc
JNIEXPORT jint JNICALL OS_NATIVE(EndDoc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EndDoc_FUNC);
	rc = (jint)EndDoc((HDC)arg0);
	OS_NATIVE_EXIT(env, that, EndDoc_FUNC);
	return rc;
}
#endif

#ifndef NO_EndPage
JNIEXPORT jint JNICALL OS_NATIVE(EndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EndPage_FUNC);
	rc = (jint)EndPage((HDC)arg0);
	OS_NATIVE_EXIT(env, that, EndPage_FUNC);
	return rc;
}
#endif

#ifndef NO_EndPaint
JNIEXPORT jint JNICALL OS_NATIVE(EndPaint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EndPaint_FUNC);
	if (arg1) if ((lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)EndPaint((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, EndPaint_FUNC);
	return rc;
}
#endif

#ifndef NO_EndPath
JNIEXPORT jboolean JNICALL OS_NATIVE(EndPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EndPath_FUNC);
	rc = (jboolean)EndPath((HDC)arg0);
	OS_NATIVE_EXIT(env, that, EndPath_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumDisplayMonitors
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumDisplayMonitors)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumDisplayMonitors_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)EnumDisplayMonitors((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(EnumDisplayMonitors_LIB);
			if (hm) fp = GetProcAddress(hm, "EnumDisplayMonitors");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
		}
	}
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, EnumDisplayMonitors_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesA
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EnumFontFamiliesA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)EnumFontFamiliesA((HDC)arg0, (LPSTR)lparg1, (FONTENUMPROC)arg2, (LPARAM)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, EnumFontFamiliesA_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesExA
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesExA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EnumFontFamiliesExA_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTAFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)EnumFontFamiliesExA((HDC)arg0, (LPLOGFONTA)lparg1, (FONTENUMPROCA)arg2, (LPARAM)arg3, arg4);
fail:
	if (arg1 && lparg1) setLOGFONTAFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, EnumFontFamiliesExA_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesExW
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesExW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EnumFontFamiliesExW_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTWFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)EnumFontFamiliesExW((HDC)arg0, (LPLOGFONTW)lparg1, (FONTENUMPROCW)arg2, (LPARAM)arg3, arg4);
fail:
	if (arg1 && lparg1) setLOGFONTWFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, EnumFontFamiliesExW_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesW
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EnumFontFamiliesW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)EnumFontFamiliesW((HDC)arg0, (LPCWSTR)lparg1, (FONTENUMPROCW)arg2, (LPARAM)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, EnumFontFamiliesW_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLanguageGroupsA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLanguageGroupsA_FUNC);
/*
	rc = (jboolean)EnumSystemLanguageGroupsA((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(EnumSystemLanguageGroupsA_LIB);
			if (hm) fp = GetProcAddress(hm, "EnumSystemLanguageGroupsA");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, EnumSystemLanguageGroupsA_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLanguageGroupsW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLanguageGroupsW_FUNC);
/*
	rc = (jboolean)EnumSystemLanguageGroupsW((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(EnumSystemLanguageGroupsW_LIB);
			if (hm) fp = GetProcAddress(hm, "EnumSystemLanguageGroupsW");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, EnumSystemLanguageGroupsW_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLocalesA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLocalesA_FUNC);
	rc = (jboolean)EnumSystemLocalesA((LOCALE_ENUMPROCA)arg0, arg1);
	OS_NATIVE_EXIT(env, that, EnumSystemLocalesA_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLocalesW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLocalesW_FUNC);
	rc = (jboolean)EnumSystemLocalesW((LOCALE_ENUMPROCW)arg0, arg1);
	OS_NATIVE_EXIT(env, that, EnumSystemLocalesW_FUNC);
	return rc;
}
#endif

#ifndef NO_EqualRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EqualRect_FUNC);
	if (arg0) if ((lparg0 = getRECTFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)EqualRect((CONST RECT *)lparg0, (CONST RECT *)lparg1);
fail:
	OS_NATIVE_EXIT(env, that, EqualRect_FUNC);
	return rc;
}
#endif

#ifndef NO_EqualRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EqualRgn_FUNC);
	rc = (jboolean)EqualRgn((HRGN)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, EqualRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_ExcludeClipRect
JNIEXPORT jint JNICALL OS_NATIVE(ExcludeClipRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExcludeClipRect_FUNC);
	rc = (jint)ExcludeClipRect((HDC)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, ExcludeClipRect_FUNC);
	return rc;
}
#endif

#ifndef NO_ExpandEnvironmentStringsA
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExpandEnvironmentStringsA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ExpandEnvironmentStringsA(lparg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExpandEnvironmentStringsA_FUNC);
	return rc;
}
#endif

#ifndef NO_ExpandEnvironmentStringsW
JNIEXPORT jint JNICALL OS_NATIVE(ExpandEnvironmentStringsW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExpandEnvironmentStringsW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ExpandEnvironmentStringsW(lparg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExpandEnvironmentStringsW_FUNC);
	return rc;
}
#endif

#ifndef NO_ExtCreatePen
JNIEXPORT jint JNICALL OS_NATIVE(ExtCreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jintArray arg4)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtCreatePen_FUNC);
	if (arg2) if ((lparg2 = getLOGBRUSHFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ExtCreatePen(arg0, arg1, (CONST LOGBRUSH *)lparg2, arg3, (CONST DWORD *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ExtCreatePen_FUNC);
	return rc;
}
#endif

#ifndef NO_ExtCreateRegion
JNIEXPORT jint JNICALL OS_NATIVE(ExtCreateRegion)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jintArray arg2)
{
	jfloat *lparg0=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtCreateRegion_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ExtCreateRegion((XFORM *)lparg0, arg1, (CONST RGNDATA *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtCreateRegion_FUNC);
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
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ExtTextOutA_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) if ((lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL)) == NULL) goto fail;
		if (arg7) if ((lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
		if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)ExtTextOutA((HDC)arg0, arg1, arg2, arg3, lparg4, (LPSTR)lparg5, arg6, (CONST INT *)lparg7);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg7 && lparg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	} else
#endif
	{
		if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, ExtTextOutA_FUNC);
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
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ExtTextOutW_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) if ((lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL)) == NULL) goto fail;
		if (arg7) if ((lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg5) if ((lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL)) == NULL) goto fail;
		if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)ExtTextOutW((HDC)arg0, arg1, arg2, arg3, lparg4, (LPWSTR)lparg5, arg6, (CONST INT *)lparg7);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg7 && lparg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, JNI_ABORT);
	} else
#endif
	{
		if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, JNI_ABORT);
		if (arg5 && lparg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, ExtTextOutW_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtractIconExA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ExtractIconExA((LPSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtractIconExA_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtractIconExW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ExtractIconExW((LPWSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtractIconExW_FUNC);
	return rc;
}
#endif

#ifndef NO_FillPath
JNIEXPORT jboolean JNICALL OS_NATIVE(FillPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FillPath_FUNC);
	rc = (jboolean)FillPath((HDC)arg0);
	OS_NATIVE_EXIT(env, that, FillPath_FUNC);
	return rc;
}
#endif

#ifndef NO_FillRect
JNIEXPORT jint JNICALL OS_NATIVE(FillRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FillRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)FillRect((HDC)arg0, lparg1, (HBRUSH)arg2);
fail:
	OS_NATIVE_EXIT(env, that, FillRect_FUNC);
	return rc;
}
#endif

#ifndef NO_FindWindowA
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FindWindowA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)FindWindowA((LPSTR)lparg0, (LPSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FindWindowA_FUNC);
	return rc;
}
#endif

#ifndef NO_FindWindowW
JNIEXPORT jint JNICALL OS_NATIVE(FindWindowW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FindWindowW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)FindWindowW((LPWSTR)lparg0, (LPWSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FindWindowW_FUNC);
	return rc;
}
#endif

#ifndef NO_FormatMessageA
JNIEXPORT jint JNICALL OS_NATIVE(FormatMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormatMessageA_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)FormatMessageA(arg0, (LPCVOID)arg1, arg2, arg3, (LPSTR)lparg4, arg5, (va_list*)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, FormatMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_FormatMessageW
JNIEXPORT jint JNICALL OS_NATIVE(FormatMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormatMessageW_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)FormatMessageW(arg0, (LPCVOID)arg1, arg2, arg3, (LPWSTR)lparg4, arg5, (va_list*)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, FormatMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_FreeLibrary
JNIEXPORT jboolean JNICALL OS_NATIVE(FreeLibrary)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FreeLibrary_FUNC);
	rc = (jboolean)FreeLibrary((HMODULE)arg0);
	OS_NATIVE_EXIT(env, that, FreeLibrary_FUNC);
	return rc;
}
#endif

#ifndef NO_GdiSetBatchLimit
JNIEXPORT jint JNICALL OS_NATIVE(GdiSetBatchLimit)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GdiSetBatchLimit_FUNC);
	rc = (jint)GdiSetBatchLimit((DWORD)arg0);
	OS_NATIVE_EXIT(env, that, GdiSetBatchLimit_FUNC);
	return rc;
}
#endif

#ifndef NO_GetACP
JNIEXPORT jint JNICALL OS_NATIVE(GetACP)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetACP_FUNC);
	rc = (jint)GetACP();
	OS_NATIVE_EXIT(env, that, GetACP_FUNC);
	return rc;
}
#endif

#ifndef NO_GetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetActiveWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetActiveWindow_FUNC);
	rc = (jint)GetActiveWindow();
	OS_NATIVE_EXIT(env, that, GetActiveWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(GetBkColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetBkColor_FUNC);
	rc = (jint)GetBkColor((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetBkColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCapture
JNIEXPORT jint JNICALL OS_NATIVE(GetCapture)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCapture_FUNC);
	rc = (jint)GetCapture();
	OS_NATIVE_EXIT(env, that, GetCapture_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCaretPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCaretPos_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetCaretPos(lparg0);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetCaretPos_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharABCWidthsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharABCWidthsA_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetCharABCWidthsA((HDC)arg0, arg1, arg2, (LPABC)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetCharABCWidthsA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharABCWidthsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharABCWidthsW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharABCWidthsW_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetCharABCWidthsW((HDC)arg0, arg1, arg2, (LPABC)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetCharABCWidthsW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharWidthA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharWidthA_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetCharWidthA((HDC)arg0, arg1, arg2, (LPINT)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetCharWidthA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharWidthW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCharWidthW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharWidthW_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetCharWidthW((HDC)arg0, arg1, arg2, (LPINT)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetCharWidthW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharacterPlacementA
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharacterPlacementA_FUNC);
	if (arg4) if ((lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetCharacterPlacementA((HDC)arg0, (LPSTR)lparg1, arg2, arg3, lparg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg4 && lparg4) setGCP_RESULTSFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetCharacterPlacementA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCharacterPlacementW
JNIEXPORT jint JNICALL OS_NATIVE(GetCharacterPlacementW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	jchar *lparg1=NULL;
	GCP_RESULTS _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCharacterPlacementW_FUNC);
	if (arg4) if ((lparg4 = getGCP_RESULTSFields(env, arg4, &_arg4)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetCharacterPlacementW((HDC)arg0, (LPWSTR)lparg1, arg2, arg3, (LPGCP_RESULTSW)lparg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg4 && lparg4) setGCP_RESULTSFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetCharacterPlacementW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClassInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jobject arg2)
{
	jbyte *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetClassInfoA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getWNDCLASSFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)GetClassInfoA((HINSTANCE)arg0, (LPSTR)lparg1, lparg2);
fail:
	if (arg2 && lparg2) setWNDCLASSFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClassInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClassInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClassInfoW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jobject arg2)
{
	jchar *lparg1=NULL;
	WNDCLASS _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetClassInfoW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getWNDCLASSFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)GetClassInfoW((HINSTANCE)arg0, (LPWSTR)lparg1, (LPWNDCLASSW)lparg2);
fail:
	if (arg2 && lparg2) setWNDCLASSFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClassInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClassNameA
JNIEXPORT jint JNICALL OS_NATIVE(GetClassNameA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClassNameA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetClassNameA((HWND)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClassNameA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClassNameW
JNIEXPORT jint JNICALL OS_NATIVE(GetClassNameW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClassNameW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetClassNameW((HWND)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClassNameW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClientRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetClientRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetClientRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetClientRect((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetClientRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipBox
JNIEXPORT jint JNICALL OS_NATIVE(GetClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipBox_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetClipBox((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetClipBox_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipRgn_FUNC);
	rc = (jint)GetClipRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, GetClipRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipboardData_FUNC);
	rc = (jint)GetClipboardData(arg0);
	OS_NATIVE_EXIT(env, that, GetClipboardData_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipboardFormatNameA
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipboardFormatNameA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetClipboardFormatNameA(arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClipboardFormatNameA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipboardFormatNameW
JNIEXPORT jint JNICALL OS_NATIVE(GetClipboardFormatNameW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipboardFormatNameW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetClipboardFormatNameW(arg0, (LPWSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetClipboardFormatNameW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetComboBoxInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetComboBoxInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	COMBOBOXINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetComboBoxInfo_FUNC);
	if (arg1) if ((lparg1 = getCOMBOBOXINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetComboBoxInfo((HWND)arg0, lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetComboBoxInfo_LIB);
			if (hm) fp = GetProcAddress(hm, "GetComboBoxInfo");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HWND)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setCOMBOBOXINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetComboBoxInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentObject
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentObject_FUNC);
	rc = (jint)GetCurrentObject((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetCurrentObject_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentProcessId)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentProcessId_FUNC);
	rc = (jint)GetCurrentProcessId();
	OS_NATIVE_EXIT(env, that, GetCurrentProcessId_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentThreadId
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentThreadId)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentThreadId_FUNC);
	rc = (jint)GetCurrentThreadId();
	OS_NATIVE_EXIT(env, that, GetCurrentThreadId_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCursor
JNIEXPORT jint JNICALL OS_NATIVE(GetCursor)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCursor_FUNC);
	rc = (jint)GetCursor();
	OS_NATIVE_EXIT(env, that, GetCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(GetCursorPos)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetCursorPos_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetCursorPos(lparg0);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetCursorPos_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDC
JNIEXPORT jint JNICALL OS_NATIVE(GetDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDC_FUNC);
	rc = (jint)GetDC((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetDC_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDCEx
JNIEXPORT jint JNICALL OS_NATIVE(GetDCEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDCEx_FUNC);
	rc = (jint)GetDCEx((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetDCEx_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDIBColorTable_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetDIBColorTable_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDIBits
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jint arg6)
{
	jbyte *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDIBits_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) if ((lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetDIBits((HDC)arg0, (HBITMAP)arg1, arg2, arg3, (LPVOID)arg4, (LPBITMAPINFO)lparg5, arg6);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5 && lparg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
	} else
#endif
	{
		if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	}
	OS_NATIVE_EXIT(env, that, GetDIBits_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDesktopWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetDesktopWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDesktopWindow_FUNC);
	rc = (jint)GetDesktopWindow();
	OS_NATIVE_EXIT(env, that, GetDesktopWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDeviceCaps
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceCaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDeviceCaps_FUNC);
	rc = (jint)GetDeviceCaps((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetDeviceCaps_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDialogBaseUnits
JNIEXPORT jint JNICALL OS_NATIVE(GetDialogBaseUnits)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDialogBaseUnits_FUNC);
	rc = (jint)GetDialogBaseUnits();
	OS_NATIVE_EXIT(env, that, GetDialogBaseUnits_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDlgItem
JNIEXPORT jint JNICALL OS_NATIVE(GetDlgItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDlgItem_FUNC);
	rc = (jint)GetDlgItem((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetDlgItem_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDoubleClickTime
JNIEXPORT jint JNICALL OS_NATIVE(GetDoubleClickTime)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDoubleClickTime_FUNC);
	rc = (jint)GetDoubleClickTime();
	OS_NATIVE_EXIT(env, that, GetDoubleClickTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFocus
JNIEXPORT jint JNICALL OS_NATIVE(GetFocus)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFocus_FUNC);
	rc = (jint)GetFocus();
	OS_NATIVE_EXIT(env, that, GetFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFontLanguageInfo
JNIEXPORT jint JNICALL OS_NATIVE(GetFontLanguageInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFontLanguageInfo_FUNC);
	rc = (jint)GetFontLanguageInfo((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetFontLanguageInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetForegroundWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetForegroundWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetForegroundWindow_FUNC);
	rc = (jint)GetForegroundWindow();
	OS_NATIVE_EXIT(env, that, GetForegroundWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetGUIThreadInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetGUIThreadInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GUITHREADINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetGUIThreadInfo_FUNC);
	if (arg1) if ((lparg1 = getGUITHREADINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetGUIThreadInfo((DWORD)arg0, (LPGUITHREADINFO)lparg1);
fail:
	if (arg1 && lparg1) setGUITHREADINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetGUIThreadInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetIconInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ICONINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetIconInfo_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	rc = (jboolean)GetIconInfo((HICON)arg0, lparg1);
fail:
	if (arg1 && lparg1) setICONINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetIconInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyNameTextA
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyNameTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyNameTextA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetKeyNameTextA(arg0, (LPSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetKeyNameTextA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyNameTextW
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyNameTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyNameTextW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetKeyNameTextW(arg0, (LPWSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetKeyNameTextW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyState
JNIEXPORT jshort JNICALL OS_NATIVE(GetKeyState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyState_FUNC);
	rc = (jshort)GetKeyState(arg0);
	OS_NATIVE_EXIT(env, that, GetKeyState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardLayout_FUNC);
	rc = (jint)GetKeyboardLayout(arg0);
	OS_NATIVE_EXIT(env, that, GetKeyboardLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyboardLayoutList
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayoutList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardLayoutList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetKeyboardLayoutList(arg0, (HKL FAR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetKeyboardLayoutList_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyboardState
JNIEXPORT jboolean JNICALL OS_NATIVE(GetKeyboardState)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardState_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)GetKeyboardState((PBYTE)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetKeyboardState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLastActivePopup
JNIEXPORT jint JNICALL OS_NATIVE(GetLastActivePopup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLastActivePopup_FUNC);
	rc = (jint)GetLastActivePopup((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetLastActivePopup_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLastError
JNIEXPORT jint JNICALL OS_NATIVE(GetLastError)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLastError_FUNC);
	rc = (jint)GetLastError();
	OS_NATIVE_EXIT(env, that, GetLastError_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLayout_FUNC);
/*
	rc = (jint)GetLayout((HDC)arg0);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetLayout_LIB);
			if (hm) fp = GetProcAddress(hm, "GetLayout");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HDC)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, GetLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLocaleInfoA
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLocaleInfoA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetLocaleInfoA(arg0, arg1, (LPSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetLocaleInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLocaleInfoW
JNIEXPORT jint JNICALL OS_NATIVE(GetLocaleInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLocaleInfoW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetLocaleInfoW(arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetLocaleInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenu_FUNC);
	rc = (jint)GetMenu((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuBarInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuBarInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	MENUBARINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuBarInfo_FUNC);
	if (arg3) if ((lparg3 = getMENUBARINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
/*
	rc = (jboolean)GetMenuBarInfo(arg0, arg1, arg2, lparg3);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetMenuBarInfo_LIB);
			if (hm) fp = GetProcAddress(hm, "GetMenuBarInfo");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp(arg0, arg1, arg2, lparg3);
		}
	}
fail:
	if (arg3 && lparg3) setMENUBARINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetMenuBarInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuDefaultItem
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuDefaultItem_FUNC);
	rc = (jint)GetMenuDefaultItem((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetMenuDefaultItem_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuInfo_FUNC);
	if (arg1) if ((lparg1 = getMENUINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMenuInfo((HMENU)arg0, lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetMenuInfo_LIB);
			if (hm) fp = GetProcAddress(hm, "GetMenuInfo");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HMENU)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setMENUINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetMenuInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemCount
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemCount_FUNC);
	rc = (jint)GetMenuItemCount((HMENU)arg0);
	OS_NATIVE_EXIT(env, that, GetMenuItemCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemInfoA_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)GetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetMenuItemInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemInfoW_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)GetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetMenuItemInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuItemRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemRect_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)GetMenuItemRect((HWND)arg0, (HMENU)arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetMenuItemRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMessageA_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetMessageA(lparg0, (HWND)arg1, arg2, arg3);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMessagePos
JNIEXPORT jint JNICALL OS_NATIVE(GetMessagePos)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMessagePos_FUNC);
	rc = (jint)GetMessagePos();
	OS_NATIVE_EXIT(env, that, GetMessagePos_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMessageTime
JNIEXPORT jint JNICALL OS_NATIVE(GetMessageTime)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMessageTime_FUNC);
	rc = (jint)GetMessageTime();
	OS_NATIVE_EXIT(env, that, GetMessageTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMessageW_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetMessageW(lparg0, (HWND)arg1, arg2, arg3);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMetaRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetMetaRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMetaRgn_FUNC);
	rc = (jint)GetMetaRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, GetMetaRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleHandleA
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleHandleA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetModuleHandleA((LPSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetModuleHandleA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleHandleW
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleHandleW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleHandleW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetModuleHandleW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetModuleHandleW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMonitorInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMonitorInfoA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMonitorInfoA_FUNC);
	if (arg1) if ((lparg1 = getMONITORINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMonitorInfoA((HMONITOR)arg0, (LPMONITORINFO)lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetMonitorInfoA_LIB);
			if (hm) fp = GetProcAddress(hm, "GetMonitorInfoA");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HMONITOR)arg0, (LPMONITORINFO)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setMONITORINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetMonitorInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMonitorInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMonitorInfoW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMonitorInfoW_FUNC);
	if (arg1) if ((lparg1 = getMONITORINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMonitorInfoW((HMONITOR)arg0, (LPMONITORINFO)lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetMonitorInfoW_LIB);
			if (hm) fp = GetProcAddress(hm, "GetMonitorInfoW");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HMONITOR)arg0, (LPMONITORINFO)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setMONITORINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetMonitorInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetNearestPaletteIndex
JNIEXPORT jint JNICALL OS_NATIVE(GetNearestPaletteIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetNearestPaletteIndex_FUNC);
	rc = (jint)GetNearestPaletteIndex((HPALETTE)arg0, (COLORREF)arg1);
	OS_NATIVE_EXIT(env, that, GetNearestPaletteIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__III
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__III_FUNC);
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, (LPVOID)arg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__III_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setBITMAPFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setDIBSECTIONFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	EXTLOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setEXTLOGPENFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONTA _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGFONTAFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGPENFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__III
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__III_FUNC);
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, (LPVOID)arg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__III_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setBITMAPFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setDIBSECTIONFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	EXTLOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setEXTLOGPENFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGFONTW _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGFONTWFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGPENFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetOpenFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetOpenFileNameA_FUNC);
	if (arg0) if ((lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetOpenFileNameA(lparg0);
fail:
	if (arg0 && lparg0) setOPENFILENAMEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetOpenFileNameA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetOpenFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetOpenFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetOpenFileNameW_FUNC);
	if (arg0) if ((lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetOpenFileNameW((LPOPENFILENAMEW)lparg0);
fail:
	if (arg0 && lparg0) setOPENFILENAMEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetOpenFileNameW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPaletteEntries_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetPaletteEntries((HPALETTE)arg0, arg1, arg2, (LPPALETTEENTRY)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetPaletteEntries_FUNC);
	return rc;
}
#endif

#ifndef NO_GetParent
JNIEXPORT jint JNICALL OS_NATIVE(GetParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetParent_FUNC);
	rc = (jint)GetParent((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPixel
JNIEXPORT jint JNICALL OS_NATIVE(GetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPixel_FUNC);
	rc = (jint)GetPixel((HDC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPolyFillMode
JNIEXPORT jint JNICALL OS_NATIVE(GetPolyFillMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPolyFillMode_FUNC);
	rc = (jint)GetPolyFillMode((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetPolyFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcAddress
JNIEXPORT jint JNICALL OS_NATIVE(GetProcAddress)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetProcAddress_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetProcAddress((HMODULE)arg0, (LPCTSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetProcAddress_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcessHeap
JNIEXPORT jint JNICALL OS_NATIVE(GetProcessHeap)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetProcessHeap_FUNC);
	rc = (jint)GetProcessHeap();
	OS_NATIVE_EXIT(env, that, GetProcessHeap_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetProfileStringA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetProfileStringA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)lparg2, (LPSTR)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetProfileStringA_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetProfileStringW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetProfileStringW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)lparg2, (LPWSTR)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetProfileStringW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPropA
JNIEXPORT jint JNICALL OS_NATIVE(GetPropA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPropA_FUNC);
	rc = (jint)GetPropA((HWND)arg0, (LPCTSTR)arg1);
	OS_NATIVE_EXIT(env, that, GetPropA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPropW
JNIEXPORT jint JNICALL OS_NATIVE(GetPropW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPropW_FUNC);
	rc = (jint)GetPropW((HWND)arg0, (LPCWSTR)arg1);
	OS_NATIVE_EXIT(env, that, GetPropW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetROP2
JNIEXPORT jint JNICALL OS_NATIVE(GetROP2)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetROP2_FUNC);
	rc = (jint)GetROP2((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetROP2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetRandomRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetRandomRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetRandomRgn_FUNC);
	rc = (jint)GetRandomRgn((HDC)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetRandomRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetRegionData
JNIEXPORT jint JNICALL OS_NATIVE(GetRegionData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetRegionData_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetRegionData((HRGN)arg0, arg1, (RGNDATA *)lparg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	OS_NATIVE_EXIT(env, that, GetRegionData_FUNC);
	return rc;
}
#endif

#ifndef NO_GetRgnBox
JNIEXPORT jint JNICALL OS_NATIVE(GetRgnBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetRgnBox_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	rc = (jint)GetRgnBox((HRGN)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetRgnBox_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSaveFileNameA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetSaveFileNameA_FUNC);
	if (arg0) if ((lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetSaveFileNameA(lparg0);
fail:
	if (arg0 && lparg0) setOPENFILENAMEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetSaveFileNameA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSaveFileNameW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetSaveFileNameW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OPENFILENAME _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetSaveFileNameW_FUNC);
	if (arg0) if ((lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetSaveFileNameW((LPOPENFILENAMEW)lparg0);
fail:
	if (arg0 && lparg0) setOPENFILENAMEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetSaveFileNameW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrollInfo_FUNC);
	if (arg2) if ((lparg2 = getSCROLLINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)GetScrollInfo((HWND)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setSCROLLINFOFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetScrollInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetStockObject
JNIEXPORT jint JNICALL OS_NATIVE(GetStockObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetStockObject_FUNC);
	rc = (jint)GetStockObject(arg0);
	OS_NATIVE_EXIT(env, that, GetStockObject_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSysColor
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSysColor_FUNC);
	rc = (jint)GetSysColor(arg0);
	OS_NATIVE_EXIT(env, that, GetSysColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSysColorBrush
JNIEXPORT jint JNICALL OS_NATIVE(GetSysColorBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSysColorBrush_FUNC);
	rc = (jint)GetSysColorBrush(arg0);
	OS_NATIVE_EXIT(env, that, GetSysColorBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemDefaultUILanguage
JNIEXPORT jshort JNICALL OS_NATIVE(GetSystemDefaultUILanguage)
	(JNIEnv *env, jclass that)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetSystemDefaultUILanguage_FUNC);
/*
	rc = (jshort)GetSystemDefaultUILanguage();
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GetSystemDefaultUILanguage_LIB);
			if (hm) fp = GetProcAddress(hm, "GetSystemDefaultUILanguage");
			initialized = 1;
		}
		if (fp) {
			rc = (jshort)fp();
		}
	}
	OS_NATIVE_EXIT(env, that, GetSystemDefaultUILanguage_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMenu)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSystemMenu_FUNC);
	rc = (jint)GetSystemMenu((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetSystemMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemMetrics
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemMetrics)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSystemMetrics_FUNC);
	rc = (jint)GetSystemMetrics(arg0);
	OS_NATIVE_EXIT(env, that, GetSystemMetrics_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetSystemPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSystemPaletteEntries_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)GetSystemPaletteEntries((HDC)arg0, (UINT)arg1, (UINT)arg2, (LPPALETTEENTRY)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	}
	OS_NATIVE_EXIT(env, that, GetSystemPaletteEntries_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextCharset
JNIEXPORT jint JNICALL OS_NATIVE(GetTextCharset)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextCharset_FUNC);
	rc = (jint)GetTextCharset((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetTextCharset_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(GetTextColor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextColor_FUNC);
	rc = (jint)GetTextColor((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextExtentPoint32A
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32A)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3)
{
	jbyte *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextExtentPoint32A_FUNC);
	if (arg3) if ((lparg3 = &_arg3) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetTextExtentPoint32A((HDC)arg0, (LPSTR)lparg1, arg2, lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetTextExtentPoint32A_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextExtentPoint32W
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextExtentPoint32W)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jobject arg3)
{
	jchar *lparg1=NULL;
	SIZE _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextExtentPoint32W_FUNC);
	if (arg3) if ((lparg3 = &_arg3) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)GetTextExtentPoint32W((HDC)arg0, (LPWSTR)lparg1, arg2, lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetTextExtentPoint32W_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextMetricsA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICA _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextMetricsA_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	rc = (jboolean)GetTextMetricsA((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setTEXTMETRICAFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetTextMetricsA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTextMetricsW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetTextMetricsW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TEXTMETRICW _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetTextMetricsW_FUNC);
	if (arg1) if ((lparg1 = &_arg1) == NULL) goto fail;
	rc = (jboolean)GetTextMetricsW((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setTEXTMETRICWFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetTextMetricsW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTickCount
JNIEXPORT jint JNICALL OS_NATIVE(GetTickCount)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTickCount_FUNC);
	rc = (jint)GetTickCount();
	OS_NATIVE_EXIT(env, that, GetTickCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GetUpdateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetUpdateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetUpdateRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetUpdateRect((HWND)arg0, (LPRECT)lparg1, (BOOL)arg2);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetUpdateRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetUpdateRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetUpdateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetUpdateRgn_FUNC);
	rc = (jint)GetUpdateRgn((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetUpdateRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOA _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExA_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOAFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExA(lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExW
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOW _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExW_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExW(lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindow_FUNC);
	rc = (jint)GetWindow((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowLongA_FUNC);
	rc = (jint)GetWindowLongA((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindowLongA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowLongW_FUNC);
	rc = (jint)GetWindowLongW((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindowLongW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowOrgEx
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowOrgEx)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowOrgEx_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetWindowOrgEx((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetWindowOrgEx_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowPlacement_FUNC);
	if (arg1) if ((lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetWindowPlacement((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetWindowPlacement_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWindowRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)GetWindowRect((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetWindowRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowRgn
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowRgn_FUNC);
	rc = (jint)GetWindowRgn((HWND)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, GetWindowRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowTextA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowTextA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowTextA((HWND)arg0, (LPSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowTextA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowTextLengthA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthA)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowTextLengthA_FUNC);
	rc = (jint)GetWindowTextLengthA((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowTextLengthA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowTextLengthW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextLengthW)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowTextLengthW_FUNC);
	rc = (jint)GetWindowTextLengthW((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowTextLengthW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowTextW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowTextW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowTextW((HWND)arg0, (LPWSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowTextW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowThreadProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowThreadProcessId)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowThreadProcessId_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowThreadProcessId((HWND)arg0, (LPDWORD)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowThreadProcessId_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWorldTransform
JNIEXPORT jboolean JNICALL OS_NATIVE(GetWorldTransform)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetWorldTransform_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)GetWorldTransform((HDC)arg0, (LPXFORM)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWorldTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalAddAtomA
JNIEXPORT jint JNICALL OS_NATIVE(GlobalAddAtomA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalAddAtomA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GlobalAddAtomA((LPCTSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GlobalAddAtomA_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalAddAtomW
JNIEXPORT jint JNICALL OS_NATIVE(GlobalAddAtomW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalAddAtomW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GlobalAddAtomW((LPCWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GlobalAddAtomW_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalAlloc
JNIEXPORT jint JNICALL OS_NATIVE(GlobalAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalAlloc_FUNC);
	rc = (jint)GlobalAlloc(arg0, arg1);
	OS_NATIVE_EXIT(env, that, GlobalAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalFree
JNIEXPORT jint JNICALL OS_NATIVE(GlobalFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalFree_FUNC);
	rc = (jint)GlobalFree((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalFree_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalLock
JNIEXPORT jint JNICALL OS_NATIVE(GlobalLock)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalLock_FUNC);
	rc = (jint)GlobalLock((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalLock_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalSize
JNIEXPORT jint JNICALL OS_NATIVE(GlobalSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalSize_FUNC);
	rc = (jint)GlobalSize((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalUnlock
JNIEXPORT jboolean JNICALL OS_NATIVE(GlobalUnlock)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalUnlock_FUNC);
	rc = (jboolean)GlobalUnlock((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalUnlock_FUNC);
	return rc;
}
#endif

#ifndef NO_GradientFill
JNIEXPORT jboolean JNICALL OS_NATIVE(GradientFill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GradientFill_FUNC);
/*
	rc = (jboolean)GradientFill((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(GradientFill_LIB);
			if (hm) fp = GetProcAddress(hm, "GradientFill");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
		}
	}
	OS_NATIVE_EXIT(env, that, GradientFill_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapAlloc
JNIEXPORT jint JNICALL OS_NATIVE(HeapAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HeapAlloc_FUNC);
	rc = (jint)HeapAlloc((HANDLE)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, HeapAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapFree
JNIEXPORT jboolean JNICALL OS_NATIVE(HeapFree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HeapFree_FUNC);
	rc = (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
	OS_NATIVE_EXIT(env, that, HeapFree_FUNC);
	return rc;
}
#endif

#ifndef NO_HideCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(HideCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HideCaret_FUNC);
	rc = (jboolean)HideCaret((HWND)arg0);
	OS_NATIVE_EXIT(env, that, HideCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_IIDFromString
JNIEXPORT jint JNICALL OS_NATIVE(IIDFromString)
	(JNIEnv *env, jclass that, jcharArray arg0, jbyteArray arg1)
{
	jchar *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IIDFromString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)IIDFromString((LPOLESTR)lparg0, (LPIID)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, IIDFromString_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Add
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Add)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Add_FUNC);
	rc = (jint)ImageList_Add((HIMAGELIST)arg0, (HBITMAP)arg1, (HBITMAP)arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1Add_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1AddMasked
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1AddMasked)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1AddMasked_FUNC);
	rc = (jint)ImageList_AddMasked((HIMAGELIST)arg0, (HBITMAP)arg1, (COLORREF)arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1AddMasked_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Create
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Create_FUNC);
	rc = (jint)ImageList_Create(arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, ImageList_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Destroy
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Destroy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Destroy_FUNC);
	rc = (jboolean)ImageList_Destroy((HIMAGELIST)arg0);
	OS_NATIVE_EXIT(env, that, ImageList_1Destroy_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetIcon_FUNC);
	rc = (jint)ImageList_GetIcon((HIMAGELIST)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1GetIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1GetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetIconSize_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)ImageList_GetIconSize((HIMAGELIST)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ImageList_1GetIconSize_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetImageCount
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetImageCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetImageCount_FUNC);
	rc = (jint)ImageList_GetImageCount((HIMAGELIST)arg0);
	OS_NATIVE_EXIT(env, that, ImageList_1GetImageCount_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Remove
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Remove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Remove_FUNC);
	rc = (jboolean)ImageList_Remove((HIMAGELIST)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ImageList_1Remove_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Replace
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Replace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Replace_FUNC);
	rc = (jboolean)ImageList_Replace((HIMAGELIST)arg0, arg1, (HBITMAP)arg2, (HBITMAP)arg3);
	OS_NATIVE_EXIT(env, that, ImageList_1Replace_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1ReplaceIcon
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1ReplaceIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1ReplaceIcon_FUNC);
	rc = (jint)ImageList_ReplaceIcon((HIMAGELIST)arg0, arg1, (HICON)arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1ReplaceIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1SetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1SetIconSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1SetIconSize_FUNC);
	rc = (jboolean)ImageList_SetIconSize((HIMAGELIST)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1SetIconSize_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmAssociateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmAssociateContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmAssociateContext_FUNC);
	rc = (jint)ImmAssociateContext((HWND)arg0, (HIMC)arg1);
	OS_NATIVE_EXIT(env, that, ImmAssociateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmCreateContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmCreateContext_FUNC);
	rc = (jint)ImmCreateContext();
	OS_NATIVE_EXIT(env, that, ImmCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmDestroyContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmDestroyContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmDestroyContext_FUNC);
	rc = (jboolean)ImmDestroyContext((HIMC)arg0);
	OS_NATIVE_EXIT(env, that, ImmDestroyContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmDisableTextFrameService
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmDisableTextFrameService)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmDisableTextFrameService_FUNC);
/*
	rc = (jboolean)ImmDisableTextFrameService(arg0);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(ImmDisableTextFrameService_LIB);
			if (hm) fp = GetProcAddress(hm, "ImmDisableTextFrameService");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, ImmDisableTextFrameService_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetCompositionFontA_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTAFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmGetCompositionFontA((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setLOGFONTAFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmGetCompositionFontA_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetCompositionFontW_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTWFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmGetCompositionFontW((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setLOGFONTWFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmGetCompositionFontW_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionStringA
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringA((HIMC)arg0, arg1, (LPSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringA_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionStringW
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetContext
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetContext_FUNC);
	rc = (jint)ImmGetContext((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetConversionStatus_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)ImmGetConversionStatus((HIMC)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ImmGetConversionStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetDefaultIMEWnd
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetDefaultIMEWnd)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetDefaultIMEWnd_FUNC);
	rc = (jint)ImmGetDefaultIMEWnd((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetDefaultIMEWnd_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetOpenStatus_FUNC);
	rc = (jboolean)ImmGetOpenStatus((HIMC)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetOpenStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmReleaseContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmReleaseContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmReleaseContext_FUNC);
	rc = (jboolean)ImmReleaseContext((HWND)arg0, (HIMC)arg1);
	OS_NATIVE_EXIT(env, that, ImmReleaseContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTA _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetCompositionFontA_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTAFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmSetCompositionFontA((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setLOGFONTAFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmSetCompositionFontA_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionFontW
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	LOGFONTW _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetCompositionFontW_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTWFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmSetCompositionFontW((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setLOGFONTWFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmSetCompositionFontW_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	COMPOSITIONFORM _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetCompositionWindow_FUNC);
	if (arg1) if ((lparg1 = getCOMPOSITIONFORMFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmSetCompositionWindow((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setCOMPOSITIONFORMFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmSetCompositionWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetConversionStatus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetConversionStatus_FUNC);
	rc = (jboolean)ImmSetConversionStatus((HIMC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImmSetConversionStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetOpenStatus)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetOpenStatus_FUNC);
	rc = (jboolean)ImmSetOpenStatus((HIMC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ImmSetOpenStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_InitCommonControls
JNIEXPORT void JNICALL OS_NATIVE(InitCommonControls)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, InitCommonControls_FUNC);
	InitCommonControls();
	OS_NATIVE_EXIT(env, that, InitCommonControls_FUNC);
}
#endif

#ifndef NO_InitCommonControlsEx
JNIEXPORT jboolean JNICALL OS_NATIVE(InitCommonControlsEx)
	(JNIEnv *env, jclass that, jobject arg0)
{
	INITCOMMONCONTROLSEX _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InitCommonControlsEx_FUNC);
	if (arg0) if ((lparg0 = getINITCOMMONCONTROLSEXFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)InitCommonControlsEx(lparg0);
fail:
	if (arg0 && lparg0) setINITCOMMONCONTROLSEXFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, InitCommonControlsEx_FUNC);
	return rc;
}
#endif

#ifndef NO_InsertMenuA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InsertMenuA_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)InsertMenuA((HMENU)arg0, arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, InsertMenuA_FUNC);
	return rc;
}
#endif

#ifndef NO_InsertMenuItemA
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InsertMenuItemA_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)InsertMenuItemA((HMENU)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, InsertMenuItemA_FUNC);
	return rc;
}
#endif

#ifndef NO_InsertMenuItemW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuItemW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InsertMenuItemW_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)InsertMenuItemW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, InsertMenuItemW_FUNC);
	return rc;
}
#endif

#ifndef NO_InsertMenuW
JNIEXPORT jboolean JNICALL OS_NATIVE(InsertMenuW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InsertMenuW_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)InsertMenuW((HMENU)arg0, arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, InsertMenuW_FUNC);
	return rc;
}
#endif

#ifndef NO_IntersectClipRect
JNIEXPORT jint JNICALL OS_NATIVE(IntersectClipRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IntersectClipRect_FUNC);
	rc = (jint)IntersectClipRect((HDC)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, IntersectClipRect_FUNC);
	return rc;
}
#endif

#ifndef NO_IntersectRect
JNIEXPORT jboolean JNICALL OS_NATIVE(IntersectRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	RECT _arg0, *lparg0=NULL;
	RECT _arg1, *lparg1=NULL;
	RECT _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IntersectRect_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)IntersectRect(lparg0, lparg1, lparg2);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, IntersectRect_FUNC);
	return rc;
}
#endif

#ifndef NO_InvalidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InvalidateRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)InvalidateRect((HWND)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, InvalidateRect_FUNC);
	return rc;
}
#endif

#ifndef NO_InvalidateRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(InvalidateRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InvalidateRgn_FUNC);
	rc = (jboolean)InvalidateRgn((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, InvalidateRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_IsAppThemed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsAppThemed)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsAppThemed_FUNC);
/*
	rc = (jboolean)IsAppThemed();
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(IsAppThemed_LIB);
			if (hm) fp = GetProcAddress(hm, "IsAppThemed");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp();
		}
	}
	OS_NATIVE_EXIT(env, that, IsAppThemed_FUNC);
	return rc;
}
#endif

#ifndef NO_IsDBCSLeadByte
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDBCSLeadByte)
	(JNIEnv *env, jclass that, jbyte arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsDBCSLeadByte_FUNC);
	rc = (jboolean)IsDBCSLeadByte(arg0);
	OS_NATIVE_EXIT(env, that, IsDBCSLeadByte_FUNC);
	return rc;
}
#endif

#ifndef NO_IsHungAppWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(IsHungAppWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsHungAppWindow_FUNC);
/*
	rc = (jboolean)IsHungAppWindow((HWND)arg0);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(IsHungAppWindow_LIB);
			if (hm) fp = GetProcAddress(hm, "IsHungAppWindow");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HWND)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, IsHungAppWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_IsIconic
JNIEXPORT jboolean JNICALL OS_NATIVE(IsIconic)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsIconic_FUNC);
	rc = (jboolean)IsIconic((HWND)arg0);
	OS_NATIVE_EXIT(env, that, IsIconic_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowEnabled_FUNC);
	rc = (jboolean)IsWindowEnabled((HWND)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowVisible_FUNC);
	rc = (jboolean)IsWindowVisible((HWND)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_IsZoomed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsZoomed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsZoomed_FUNC);
	rc = (jboolean)IsZoomed((HWND)arg0);
	OS_NATIVE_EXIT(env, that, IsZoomed_FUNC);
	return rc;
}
#endif

#ifndef NO_KillTimer
JNIEXPORT jboolean JNICALL OS_NATIVE(KillTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KillTimer_FUNC);
	rc = (jboolean)KillTimer((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, KillTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_LineTo
JNIEXPORT jboolean JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, LineTo_FUNC);
	rc = (jboolean)LineTo((HDC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, LineTo_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadBitmapA
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadBitmapA_FUNC);
	rc = (jint)LoadBitmapA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadBitmapA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadBitmapW
JNIEXPORT jint JNICALL OS_NATIVE(LoadBitmapW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadBitmapW_FUNC);
	rc = (jint)LoadBitmapW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadBitmapW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadCursorA
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadCursorA_FUNC);
	rc = (jint)LoadCursorA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadCursorA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadCursorW
JNIEXPORT jint JNICALL OS_NATIVE(LoadCursorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadCursorW_FUNC);
	rc = (jint)LoadCursorW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadCursorW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadIconA
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadIconA_FUNC);
	rc = (jint)LoadIconA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadIconA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadIconW
JNIEXPORT jint JNICALL OS_NATIVE(LoadIconW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadIconW_FUNC);
	rc = (jint)LoadIconW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadIconW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadImageA__IIIIII
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageA__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadImageA__IIIIII_FUNC);
	rc = (jint)LoadImageA((HINSTANCE)arg0, (LPSTR)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, LoadImageA__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadImageA__I_3BIIII
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageA__I_3BIIII)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadImageA__I_3BIIII_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)LoadImageA((HINSTANCE)arg0, (LPSTR)lparg1, arg2, arg3, arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, LoadImageA__I_3BIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadImageW__IIIIII
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageW__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadImageW__IIIIII_FUNC);
	rc = (jint)LoadImageW((HINSTANCE)arg0, (LPWSTR)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, LoadImageW__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadImageW__I_3CIIII
JNIEXPORT jint JNICALL OS_NATIVE(LoadImageW__I_3CIIII)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadImageW__I_3CIIII_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)LoadImageW((HINSTANCE)arg0, (LPWSTR)lparg1, arg2, arg3, arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, LoadImageW__I_3CIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadLibraryA
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadLibraryA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)LoadLibraryA((LPSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LoadLibraryA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadLibraryW
JNIEXPORT jint JNICALL OS_NATIVE(LoadLibraryW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadLibraryW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)LoadLibraryW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LoadLibraryW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadStringA
JNIEXPORT jint JNICALL OS_NATIVE(LoadStringA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadStringA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)LoadStringA((HINSTANCE)arg0, arg1, (LPSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, LoadStringA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadStringW
JNIEXPORT jint JNICALL OS_NATIVE(LoadStringW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LoadStringW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)LoadStringW((HINSTANCE)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, LoadStringW_FUNC);
	return rc;
}
#endif

#ifndef NO_LocalFree
JNIEXPORT jint JNICALL OS_NATIVE(LocalFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LocalFree_FUNC);
	rc = (jint)LocalFree((HLOCAL)arg0);
	OS_NATIVE_EXIT(env, that, LocalFree_FUNC);
	return rc;
}
#endif

#ifndef NO_MapVirtualKeyA
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MapVirtualKeyA_FUNC);
	rc = (jint)MapVirtualKeyA(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MapVirtualKeyA_FUNC);
	return rc;
}
#endif

#ifndef NO_MapVirtualKeyW
JNIEXPORT jint JNICALL OS_NATIVE(MapVirtualKeyW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MapVirtualKeyW_FUNC);
	rc = (jint)MapVirtualKeyW(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MapVirtualKeyW_FUNC);
	return rc;
}
#endif

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	POINT _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
	if (arg2) if ((lparg2 = getPOINTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
fail:
	if (arg2 && lparg2) setPOINTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_MessageBeep
JNIEXPORT jboolean JNICALL OS_NATIVE(MessageBeep)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, MessageBeep_FUNC);
	rc = (jboolean)MessageBeep(arg0);
	OS_NATIVE_EXIT(env, that, MessageBeep_FUNC);
	return rc;
}
#endif

#ifndef NO_MessageBoxA
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MessageBoxA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)MessageBoxA((HWND)arg0, (LPSTR)lparg1, (LPSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, MessageBoxA_FUNC);
	return rc;
}
#endif

#ifndef NO_MessageBoxW
JNIEXPORT jint JNICALL OS_NATIVE(MessageBoxW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MessageBoxW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)MessageBoxW((HWND)arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, MessageBoxW_FUNC);
	return rc;
}
#endif

#ifndef NO_MonitorFromWindow
JNIEXPORT jint JNICALL OS_NATIVE(MonitorFromWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MonitorFromWindow_FUNC);
/*
	rc = (jint)MonitorFromWindow(arg0, arg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(MonitorFromWindow_LIB);
			if (hm) fp = GetProcAddress(hm, "MonitorFromWindow");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, MonitorFromWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DROPFILES _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
	if (arg1) if ((lparg1 = getDROPFILESFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GRADIENT_RECT _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
	if (arg1) if ((lparg1 = getGRADIENT_RECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	KEYBDINPUT _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
	if (arg1) if ((lparg1 = getKEYBDINPUTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONTA _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTAFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	LOGFONTW _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
	if (arg1) if ((lparg1 = getLOGFONTWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
	if (arg1) if ((lparg1 = getMEASUREITEMSTRUCTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MINMAXINFO _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
	if (arg1) if ((lparg1 = getMINMAXINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MOUSEINPUT _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
	if (arg1) if ((lparg1 = getMOUSEINPUTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	MSG _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
	if (arg1) if ((lparg1 = getMSGFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
	if (arg1) if ((lparg1 = getNMLVCUSTOMDRAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMLVDISPINFO _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
	if (arg1) if ((lparg1 = getNMLVDISPINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOA _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
	if (arg1) if ((lparg1 = getNMTTDISPINFOAFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTTDISPINFOW _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
	if (arg1) if ((lparg1 = getNMTTDISPINFOWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
	if (arg1) if ((lparg1 = getNMTVCUSTOMDRAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NMTVDISPINFO _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
	if (arg1) if ((lparg1 = getNMTVDISPINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RECT _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	TRIVERTEX _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
	if (arg1) if ((lparg1 = getTRIVERTEXFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	UDACCEL _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
	if (arg1) if ((lparg1 = getUDACCELFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	WINDOWPOS _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
	if (arg1) if ((lparg1 = getWINDOWPOSFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3BI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3BI_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3CI_FUNC);
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
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3CI_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3DI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3DI)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3DI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3DI_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3FI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3FI)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3FI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3FI_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3II_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3II_FUNC);
}
#endif

#ifndef NO_MoveMemory__I_3SI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3SI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3SI_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI)
	(JNIEnv *env, jclass that, jobject arg0, jbyteArray arg1, jint arg2)
{
	BITMAPINFOHEADER _arg0, *lparg0=NULL;
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg0 && lparg0) setBITMAPINFOHEADERFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DRAWITEMSTRUCT _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDRAWITEMSTRUCTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	EXTLOGPEN _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setEXTLOGPENFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HDITEM _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setHDITEMFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HELPINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setHELPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONTA _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setLOGFONTAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	LOGFONTW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setLOGFONTWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MEASUREITEMSTRUCT _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMEASUREITEMSTRUCTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MINMAXINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMINMAXINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	MSG _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMCUSTOMDRAW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMCUSTOMDRAWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHDR _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMHDRFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMHEADER _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMHEADERFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLINK _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLINKFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLISTVIEW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLISTVIEWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVCUSTOMDRAW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVCUSTOMDRAWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVDISPINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVDISPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMLVFINDITEM _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVFINDITEMFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMREBARCHEVRON _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMREBARCHEVRONFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMREBARCHILDSIZE _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMREBARCHILDSIZEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMRGINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMRGINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTBHOTITEM _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTBHOTITEMFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTOOLBAR _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTOOLBARFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOA _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTTDISPINFOAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTTDISPINFOW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTTDISPINFOWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTVCUSTOMDRAW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTVCUSTOMDRAWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMTVDISPINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTVDISPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NMUPDOWN _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMUPDOWNFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	POINT _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	SCRIPT_ITEM _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_ITEMFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	SCRIPT_LOGATTR _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_LOGATTRFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	SCRIPT_PROPERTIES _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_PROPERTIESFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TEXTMETRICA _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTEXTMETRICAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TEXTMETRICW _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTEXTMETRICWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TVITEM _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTVITEMFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	UDACCEL _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setUDACCELFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	WINDOWPOS _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setWINDOWPOSFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory___3BII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3BII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3BII_FUNC);
}
#endif

#ifndef NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	ACCEL _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I_FUNC);
	if (arg1) if ((lparg1 = getACCELFields(env, arg1, &_arg1)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I)
	(JNIEnv *env, jclass that, jbyteArray arg0, jobject arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	BITMAPINFOHEADER _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I_FUNC);
	if (arg1) if ((lparg1 = getBITMAPINFOHEADERFields(env, arg1, &_arg1)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory___3CII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3CII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3CII_FUNC);
}
#endif

#ifndef NO_MoveMemory___3DII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3DII)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jint arg1, jint arg2)
{
	jdouble *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3DII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3DII_FUNC);
}
#endif

#ifndef NO_MoveMemory___3FII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3FII)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3FII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3FII_FUNC);
}
#endif

#ifndef NO_MoveMemory___3III
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3III_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3III_FUNC);
}
#endif

#ifndef NO_MoveMemory___3SII
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3SII)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory___3SII_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, MoveMemory___3SII_FUNC);
}
#endif

#ifndef NO_MoveToEx
JNIEXPORT jboolean JNICALL OS_NATIVE(MoveToEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, MoveToEx_FUNC);
	rc = (jboolean)MoveToEx((HDC)arg0, arg1, arg2, (LPPOINT)arg3);
	OS_NATIVE_EXIT(env, that, MoveToEx_FUNC);
	return rc;
}
#endif

#ifndef NO_MsgWaitForMultipleObjectsEx
JNIEXPORT jint JNICALL OS_NATIVE(MsgWaitForMultipleObjectsEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MsgWaitForMultipleObjectsEx_FUNC);
	rc = (jint)MsgWaitForMultipleObjectsEx((DWORD)arg0, (LPHANDLE)arg1, (DWORD)arg2, (DWORD)arg3, (DWORD)arg4);
	OS_NATIVE_EXIT(env, that, MsgWaitForMultipleObjectsEx_FUNC);
	return rc;
}
#endif

#ifndef NO_MultiByteToWideChar__IIII_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__IIII_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MultiByteToWideChar__IIII_3CI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)arg2, arg3, (LPWSTR)lparg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	}
	OS_NATIVE_EXIT(env, that, MultiByteToWideChar__IIII_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO_MultiByteToWideChar__II_3BI_3CI
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__II_3BI_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jcharArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MultiByteToWideChar__II_3BI_3CI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jint)MultiByteToWideChar(arg0, arg1, (LPCSTR)lparg2, arg3, (LPWSTR)lparg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
		if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, MultiByteToWideChar__II_3BI_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO_NotifyWinEvent
JNIEXPORT void JNICALL OS_NATIVE(NotifyWinEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, NotifyWinEvent_FUNC);
/*
	NotifyWinEvent((DWORD)arg0, (HWND)arg1, (LONG)arg2, (LONG)arg3);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(NotifyWinEvent_LIB);
			if (hm) fp = GetProcAddress(hm, "NotifyWinEvent");
			initialized = 1;
		}
		if (fp) {
			fp((DWORD)arg0, (HWND)arg1, (LONG)arg2, (LONG)arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, NotifyWinEvent_FUNC);
}
#endif

#ifndef NO_OffsetRect
JNIEXPORT jboolean JNICALL OS_NATIVE(OffsetRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, OffsetRect_FUNC);
	if (arg0) if ((lparg0 = getRECTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)OffsetRect(lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, OffsetRect_FUNC);
	return rc;
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT jint JNICALL OS_NATIVE(OffsetRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OffsetRgn_FUNC);
	rc = (jint)OffsetRgn((HRGN)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, OffsetRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_OleInitialize
JNIEXPORT jint JNICALL OS_NATIVE(OleInitialize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OleInitialize_FUNC);
	rc = (jint)OleInitialize((LPVOID)arg0);
	OS_NATIVE_EXIT(env, that, OleInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO_OleUninitialize
JNIEXPORT void JNICALL OS_NATIVE(OleUninitialize)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, OleUninitialize_FUNC);
	OleUninitialize();
	OS_NATIVE_EXIT(env, that, OleUninitialize_FUNC);
}
#endif

#ifndef NO_OpenClipboard
JNIEXPORT jboolean JNICALL OS_NATIVE(OpenClipboard)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, OpenClipboard_FUNC);
	rc = (jboolean)OpenClipboard((HWND)arg0);
	OS_NATIVE_EXIT(env, that, OpenClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OpenThemeData
JNIEXPORT jint JNICALL OS_NATIVE(OpenThemeData)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OpenThemeData_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jint)OpenThemeData((HWND)arg0, (LPCWSTR)lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(OpenThemeData_LIB);
			if (hm) fp = GetProcAddress(hm, "OpenThemeData");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HWND)arg0, (LPCWSTR)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, OpenThemeData_FUNC);
	return rc;
}
#endif

#ifndef NO_PRIMARYLANGID
JNIEXPORT jshort JNICALL OS_NATIVE(PRIMARYLANGID)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, PRIMARYLANGID_FUNC);
	rc = (jshort)PRIMARYLANGID(arg0);
	OS_NATIVE_EXIT(env, that, PRIMARYLANGID_FUNC);
	return rc;
}
#endif

#ifndef NO_PatBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(PatBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PatBlt_FUNC);
	rc = (jboolean)PatBlt((HDC)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, PatBlt_FUNC);
	return rc;
}
#endif

#ifndef NO_PeekMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageA)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PeekMessageA_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)PeekMessageA(lparg0, (HWND)arg1, arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PeekMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_PeekMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PeekMessageW)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PeekMessageW_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)PeekMessageW(lparg0, (HWND)arg1, arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PeekMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_Pie
JNIEXPORT jboolean JNICALL OS_NATIVE(Pie)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Pie_FUNC);
	rc = (jboolean)Pie((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, Pie_FUNC);
	return rc;
}
#endif

#ifndef NO_Polygon
JNIEXPORT jboolean JNICALL OS_NATIVE(Polygon)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Polygon_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)Polygon((HDC)arg0, (CONST POINT *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, Polygon_FUNC);
	return rc;
}
#endif

#ifndef NO_Polyline
JNIEXPORT jboolean JNICALL OS_NATIVE(Polyline)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Polyline_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	rc = (jboolean)Polyline((HDC)arg0, (CONST POINT *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, Polyline_FUNC);
	return rc;
}
#endif

#ifndef NO_PostMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PostMessageA_FUNC);
	rc = (jboolean)PostMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, PostMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_PostMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PostMessageW_FUNC);
	rc = (jboolean)PostMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, PostMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_PostThreadMessageA
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PostThreadMessageA_FUNC);
	rc = (jboolean)PostThreadMessageA(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, PostThreadMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_PostThreadMessageW
JNIEXPORT jboolean JNICALL OS_NATIVE(PostThreadMessageW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PostThreadMessageW_FUNC);
	rc = (jboolean)PostThreadMessageW(arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, PostThreadMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_PrintDlgA
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PrintDlgA_FUNC);
	if (arg0) if ((lparg0 = getPRINTDLGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)PrintDlgA(lparg0);
fail:
	if (arg0 && lparg0) setPRINTDLGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PrintDlgA_FUNC);
	return rc;
}
#endif

#ifndef NO_PrintDlgW
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintDlgW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	PRINTDLG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PrintDlgW_FUNC);
	if (arg0) if ((lparg0 = getPRINTDLGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)PrintDlgW((LPPRINTDLGW)lparg0);
fail:
	if (arg0 && lparg0) setPRINTDLGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PrintDlgW_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	RECT _arg0, *lparg0=NULL;
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRect_FUNC);
	if (arg0) if ((lparg0 = getRECTFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)PtInRect(lparg0, *lparg1);
fail:
	OS_NATIVE_EXIT(env, that, PtInRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRegion_FUNC);
	rc = (jboolean)PtInRegion((HRGN)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, PtInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_RealizePalette
JNIEXPORT jint JNICALL OS_NATIVE(RealizePalette)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RealizePalette_FUNC);
	rc = (jint)RealizePalette((HDC)arg0);
	OS_NATIVE_EXIT(env, that, RealizePalette_FUNC);
	return rc;
}
#endif

#ifndef NO_RectInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RectInRegion_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)RectInRegion((HRGN)arg0, lparg1);
fail:
	OS_NATIVE_EXIT(env, that, RectInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_Rectangle
JNIEXPORT jboolean JNICALL OS_NATIVE(Rectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Rectangle_FUNC);
	rc = (jboolean)Rectangle((HDC)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, Rectangle_FUNC);
	return rc;
}
#endif

#ifndef NO_RedrawWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(RedrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RedrawWindow_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)RedrawWindow((HWND)arg0, lparg1, (HRGN)arg2, arg3);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, RedrawWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_RegCloseKey
JNIEXPORT jint JNICALL OS_NATIVE(RegCloseKey)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegCloseKey_FUNC);
	rc = (jint)RegCloseKey((HKEY)arg0);
	OS_NATIVE_EXIT(env, that, RegCloseKey_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegEnumKeyExA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getFILETIMEFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)RegEnumKeyExA((HKEY)arg0, arg1, (LPSTR)lparg2, lparg3, lparg4, (LPSTR)lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) setFILETIMEFields(env, arg7, lparg7);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, RegEnumKeyExA_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegEnumKeyExW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetCharArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getFILETIMEFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)RegEnumKeyExW((HKEY)arg0, arg1, (LPWSTR)lparg2, lparg3, lparg4, (LPWSTR)lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) setFILETIMEFields(env, arg7, lparg7);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseCharArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, RegEnumKeyExW_FUNC);
	return rc;
}
#endif

#ifndef NO_RegOpenKeyExA
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegOpenKeyExA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)RegOpenKeyExA((HKEY)arg0, (LPSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegOpenKeyExA_FUNC);
	return rc;
}
#endif

#ifndef NO_RegOpenKeyExW
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jint arg3, jintArray arg4)
{
	jchar *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegOpenKeyExW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)RegOpenKeyExW((HKEY)arg0, (LPWSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegOpenKeyExW_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryInfoKeyA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryInfoKeyA((HKEY)arg0, (LPSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
fail:
	if (arg10 && lparg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, RegQueryInfoKeyA_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryInfoKeyW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryInfoKeyW((HKEY)arg0, (LPWSTR)arg1, lparg2, (LPDWORD)arg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9, lparg10, (PFILETIME)arg11);
fail:
	if (arg10 && lparg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, RegQueryInfoKeyW_FUNC);
	return rc;
}
#endif

#ifndef NO_RegQueryValueExA__I_3BI_3I_3B_3I
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__I_3BI_3I_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__I_3BI_3I_3B_3I_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__I_3BI_3I_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_RegQueryValueExA__I_3BI_3I_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__I_3BI_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__I_3BI_3I_3I_3I_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__I_3BI_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_RegQueryValueExW__I_3CI_3I_3C_3I
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__I_3CI_3I_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jintArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__I_3CI_3I_3C_3I_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__I_3CI_3I_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_RegQueryValueExW__I_3CI_3I_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__I_3CI_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__I_3CI_3I_3I_3I_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, lparg3, (LPBYTE)lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__I_3CI_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterClassA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterClassA_FUNC);
	if (arg0) if ((lparg0 = getWNDCLASSFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)RegisterClassA(lparg0);
fail:
	if (arg0 && lparg0) setWNDCLASSFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, RegisterClassA_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterClassW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClassW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	WNDCLASS _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterClassW_FUNC);
	if (arg0) if ((lparg0 = getWNDCLASSFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)RegisterClassW((LPWNDCLASSW)lparg0);
fail:
	if (arg0 && lparg0) setWNDCLASSFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, RegisterClassW_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterClipboardFormatA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterClipboardFormatA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)RegisterClipboardFormatA((LPTSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, RegisterClipboardFormatA_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterClipboardFormatW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterClipboardFormatW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterClipboardFormatW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)RegisterClipboardFormatW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, RegisterClipboardFormatW_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterWindowMessageA
JNIEXPORT jint JNICALL OS_NATIVE(RegisterWindowMessageA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterWindowMessageA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)RegisterWindowMessageA((LPTSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, RegisterWindowMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterWindowMessageW
JNIEXPORT jint JNICALL OS_NATIVE(RegisterWindowMessageW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterWindowMessageW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)RegisterWindowMessageW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, RegisterWindowMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseCapture
JNIEXPORT jboolean JNICALL OS_NATIVE(ReleaseCapture)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ReleaseCapture_FUNC);
	rc = (jboolean)ReleaseCapture();
	OS_NATIVE_EXIT(env, that, ReleaseCapture_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseDC
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseDC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReleaseDC_FUNC);
	rc = (jint)ReleaseDC((HWND)arg0, (HDC)arg1);
	OS_NATIVE_EXIT(env, that, ReleaseDC_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(RemoveMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveMenu_FUNC);
	rc = (jboolean)RemoveMenu((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, RemoveMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_RemovePropA
JNIEXPORT jint JNICALL OS_NATIVE(RemovePropA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemovePropA_FUNC);
	rc = (jint)RemovePropA((HWND)arg0, (LPCTSTR)arg1);
	OS_NATIVE_EXIT(env, that, RemovePropA_FUNC);
	return rc;
}
#endif

#ifndef NO_RemovePropW
JNIEXPORT jint JNICALL OS_NATIVE(RemovePropW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemovePropW_FUNC);
	rc = (jint)RemovePropW((HWND)arg0, (LPCWSTR)arg1);
	OS_NATIVE_EXIT(env, that, RemovePropW_FUNC);
	return rc;
}
#endif

#ifndef NO_RestoreDC
JNIEXPORT jboolean JNICALL OS_NATIVE(RestoreDC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RestoreDC_FUNC);
	rc = (jboolean)RestoreDC((HDC)arg0, (int)arg1);
	OS_NATIVE_EXIT(env, that, RestoreDC_FUNC);
	return rc;
}
#endif

#ifndef NO_RoundRect
JNIEXPORT jboolean JNICALL OS_NATIVE(RoundRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RoundRect_FUNC);
	rc = (jboolean)RoundRect((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, RoundRect_FUNC);
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderA
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHBrowseForFolderA_FUNC);
	if (arg0) if ((lparg0 = getBROWSEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)SHBrowseForFolderA(lparg0);
fail:
	if (arg0 && lparg0) setBROWSEINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SHBrowseForFolderA_FUNC);
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderW
JNIEXPORT jint JNICALL OS_NATIVE(SHBrowseForFolderW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHBrowseForFolderW_FUNC);
	if (arg0) if ((lparg0 = getBROWSEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)SHBrowseForFolderW((LPBROWSEINFOW)lparg0);
fail:
	if (arg0 && lparg0) setBROWSEINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SHBrowseForFolderW_FUNC);
	return rc;
}
#endif

#ifndef NO_SHCreateMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(SHCreateMenuBar)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHMENUBARINFO _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHCreateMenuBar_FUNC);
	if (arg0) if ((lparg0 = getSHMENUBARINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)SHCreateMenuBar((PSHMENUBARINFO)lparg0);
fail:
	if (arg0 && lparg0) setSHMENUBARINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SHCreateMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetMalloc
JNIEXPORT jint JNICALL OS_NATIVE(SHGetMalloc)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetMalloc_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)SHGetMalloc((LPMALLOC *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SHGetMalloc_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetPathFromIDListA
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetPathFromIDListA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)SHGetPathFromIDListA((LPCITEMIDLIST)arg0, (LPSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SHGetPathFromIDListA_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetPathFromIDListW
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetPathFromIDListW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)SHGetPathFromIDListW((LPCITEMIDLIST)arg0, (LPWSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SHGetPathFromIDListW_FUNC);
	return rc;
}
#endif

#ifndef NO_SHHandleWMSettingChange
JNIEXPORT jboolean JNICALL OS_NATIVE(SHHandleWMSettingChange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SHACTIVATEINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHHandleWMSettingChange_FUNC);
	if (arg3) if ((lparg3 = getSHACTIVATEINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SHHandleWMSettingChange((HWND)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setSHACTIVATEINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SHHandleWMSettingChange_FUNC);
	return rc;
}
#endif

#ifndef NO_SHRecognizeGesture
JNIEXPORT jint JNICALL OS_NATIVE(SHRecognizeGesture)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHRGINFO _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHRecognizeGesture_FUNC);
	if (arg0) if ((lparg0 = getSHRGINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)SHRecognizeGesture(lparg0);
fail:
	if (arg0 && lparg0) setSHRGINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SHRecognizeGesture_FUNC);
	return rc;
}
#endif

#ifndef NO_SHSendBackToFocusWindow
JNIEXPORT void JNICALL OS_NATIVE(SHSendBackToFocusWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, SHSendBackToFocusWindow_FUNC);
	SHSendBackToFocusWindow(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SHSendBackToFocusWindow_FUNC);
}
#endif

#ifndef NO_SHSetAppKeyWndAssoc
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSetAppKeyWndAssoc)
	(JNIEnv *env, jclass that, jbyte arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHSetAppKeyWndAssoc_FUNC);
	rc = (jboolean)SHSetAppKeyWndAssoc((BYTE)arg0, (HWND)arg1);
	OS_NATIVE_EXIT(env, that, SHSetAppKeyWndAssoc_FUNC);
	return rc;
}
#endif

#ifndef NO_SHSipPreference
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSipPreference)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHSipPreference_FUNC);
	rc = (jboolean)SHSipPreference((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SHSipPreference_FUNC);
	return rc;
}
#endif

#ifndef NO_SaveDC
JNIEXPORT jint JNICALL OS_NATIVE(SaveDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SaveDC_FUNC);
	rc = (jint)SaveDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, SaveDC_FUNC);
	return rc;
}
#endif

#ifndef NO_ScreenToClient
JNIEXPORT jboolean JNICALL OS_NATIVE(ScreenToClient)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ScreenToClient_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ScreenToClient((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ScreenToClient_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptBreak
JNIEXPORT jint JNICALL OS_NATIVE(ScriptBreak)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jobject arg2, jint arg3)
{
	jchar *lparg0=NULL;
	SCRIPT_ANALYSIS _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptBreak_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getSCRIPT_ANALYSISFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ScriptBreak((const WCHAR *)lparg0, arg1, (const SCRIPT_ANALYSIS *)lparg2, (SCRIPT_LOGATTR *)arg3);
fail:
	if (arg2 && lparg2) setSCRIPT_ANALYSISFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ScriptBreak_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptCPtoX
JNIEXPORT jint JNICALL OS_NATIVE(ScriptCPtoX)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jobject arg7, jintArray arg8)
{
	SCRIPT_ANALYSIS _arg7, *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptCPtoX_FUNC);
	if (arg7) if ((lparg7 = getSCRIPT_ANALYSISFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ScriptCPtoX(arg0, arg1, arg2, arg3, (const WORD *)arg4, (const SCRIPT_VISATTR *)arg5, (const int *)arg6, (const SCRIPT_ANALYSIS *)lparg7, (int *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) setSCRIPT_ANALYSISFields(env, arg7, lparg7);
	OS_NATIVE_EXIT(env, that, ScriptCPtoX_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptCacheGetHeight
JNIEXPORT jint JNICALL OS_NATIVE(ScriptCacheGetHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptCacheGetHeight_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ScriptCacheGetHeight((HDC)arg0, (SCRIPT_CACHE *)arg1, (long *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ScriptCacheGetHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptFreeCache
JNIEXPORT jint JNICALL OS_NATIVE(ScriptFreeCache)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptFreeCache_FUNC);
	rc = (jint)ScriptFreeCache((SCRIPT_CACHE *)arg0);
	OS_NATIVE_EXIT(env, that, ScriptFreeCache_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptGetFontProperties
JNIEXPORT jint JNICALL OS_NATIVE(ScriptGetFontProperties)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	SCRIPT_FONTPROPERTIES _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptGetFontProperties_FUNC);
	if (arg2) if ((lparg2 = getSCRIPT_FONTPROPERTIESFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ScriptGetFontProperties((HDC)arg0, (SCRIPT_CACHE *)arg1, (SCRIPT_FONTPROPERTIES *)lparg2);
fail:
	if (arg2 && lparg2) setSCRIPT_FONTPROPERTIESFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ScriptGetFontProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptGetLogicalWidths
JNIEXPORT jint JNICALL OS_NATIVE(ScriptGetLogicalWidths)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	SCRIPT_ANALYSIS _arg0, *lparg0=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptGetLogicalWidths_FUNC);
	if (arg0) if ((lparg0 = getSCRIPT_ANALYSISFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)ScriptGetLogicalWidths((const SCRIPT_ANALYSIS *)lparg0, arg1, arg2, (const int *)arg3, (const WORD *)arg4, (const SCRIPT_VISATTR *)arg5, (int *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg0 && lparg0) setSCRIPT_ANALYSISFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ScriptGetLogicalWidths_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptGetProperties
JNIEXPORT jint JNICALL OS_NATIVE(ScriptGetProperties)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptGetProperties_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ScriptGetProperties((const SCRIPT_PROPERTIES ***)lparg0, (int *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ScriptGetProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptItemize
JNIEXPORT jint JNICALL OS_NATIVE(ScriptItemize)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jintArray arg6)
{
	jchar *lparg0=NULL;
	SCRIPT_CONTROL _arg3, *lparg3=NULL;
	SCRIPT_STATE _arg4, *lparg4=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptItemize_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSCRIPT_CONTROLFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getSCRIPT_STATEFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)ScriptItemize((const WCHAR *)lparg0, arg1, arg2, (const SCRIPT_CONTROL *)lparg3, (const SCRIPT_STATE *)lparg4, (SCRIPT_ITEM *)arg5, (int *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setSCRIPT_STATEFields(env, arg4, lparg4);
	if (arg3 && lparg3) setSCRIPT_CONTROLFields(env, arg3, lparg3);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ScriptItemize_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptLayout
JNIEXPORT jint JNICALL OS_NATIVE(ScriptLayout)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptLayout_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ScriptLayout(arg0, (const BYTE *)lparg1, (int *)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ScriptLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptPlace
JNIEXPORT jint JNICALL OS_NATIVE(ScriptPlace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jint arg6, jint arg7, jintArray arg8)
{
	SCRIPT_ANALYSIS _arg5, *lparg5=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptPlace_FUNC);
	if (arg5) if ((lparg5 = getSCRIPT_ANALYSISFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ScriptPlace((HDC)arg0, (SCRIPT_CACHE *)arg1, (const WORD *)arg2, arg3, (const SCRIPT_VISATTR *)arg4, (SCRIPT_ANALYSIS *)lparg5, (int *)arg6, (GOFFSET *)arg7, (ABC *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg5 && lparg5) setSCRIPT_ANALYSISFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, ScriptPlace_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptShape
JNIEXPORT jint JNICALL OS_NATIVE(ScriptShape)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jobject arg5, jint arg6, jint arg7, jint arg8, jintArray arg9)
{
	jchar *lparg2=NULL;
	SCRIPT_ANALYSIS _arg5, *lparg5=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptShape_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getSCRIPT_ANALYSISFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)ScriptShape((HDC)arg0, (SCRIPT_CACHE *)arg1, (const WCHAR *)lparg2, arg3, arg4, (SCRIPT_ANALYSIS *)lparg5, (WORD *)arg6, (WORD *)arg7, (SCRIPT_VISATTR *)arg8, (int *)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg5 && lparg5) setSCRIPT_ANALYSISFields(env, arg5, lparg5);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ScriptShape_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptTextOut
JNIEXPORT jint JNICALL OS_NATIVE(ScriptTextOut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jobject arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jint arg13)
{
	RECT _arg5, *lparg5=NULL;
	SCRIPT_ANALYSIS _arg6, *lparg6=NULL;
	jint *lparg12=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptTextOut_FUNC);
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getSCRIPT_ANALYSISFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg12) if ((lparg12 = (*env)->GetIntArrayElements(env, arg12, NULL)) == NULL) goto fail;
	rc = (jint)ScriptTextOut((const HDC)arg0, (SCRIPT_CACHE *)arg1, arg2, arg3, arg4, (const RECT *)lparg5, (const SCRIPT_ANALYSIS *)lparg6, (const WCHAR *)arg7, arg8, (const WORD *)arg9, arg10, (const int *)arg11, (const int *)lparg12, (const GOFFSET *)arg13);
fail:
	if (arg12 && lparg12) (*env)->ReleaseIntArrayElements(env, arg12, lparg12, 0);
	if (arg6 && lparg6) setSCRIPT_ANALYSISFields(env, arg6, lparg6);
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, ScriptTextOut_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptXtoCP
JNIEXPORT jint JNICALL OS_NATIVE(ScriptXtoCP)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6, jintArray arg7, jintArray arg8)
{
	SCRIPT_ANALYSIS _arg6, *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptXtoCP_FUNC);
	if (arg6) if ((lparg6 = getSCRIPT_ANALYSISFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ScriptXtoCP(arg0, arg1, arg2, (const WORD *)arg3, (const SCRIPT_VISATTR *)arg4, (const int *)arg5, (const SCRIPT_ANALYSIS *)lparg6, (int *)lparg7, (int *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) setSCRIPT_ANALYSISFields(env, arg6, lparg6);
	OS_NATIVE_EXIT(env, that, ScriptXtoCP_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScrollWindowEx_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getRECTFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)ScrollWindowEx((HWND)arg0, arg1, arg2, lparg3, lparg4, (HRGN)arg5, lparg6, arg7);
fail:
	if (arg6 && lparg6) setRECTFields(env, arg6, lparg6);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, ScrollWindowEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectClipRgn
JNIEXPORT jint JNICALL OS_NATIVE(SelectClipRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectClipRgn_FUNC);
	rc = (jint)SelectClipRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, SelectClipRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectObject
JNIEXPORT jint JNICALL OS_NATIVE(SelectObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectObject_FUNC);
	rc = (jint)SelectObject((HDC)arg0, (HGDIOBJ)arg1);
	OS_NATIVE_EXIT(env, that, SelectObject_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectPalette
JNIEXPORT jint JNICALL OS_NATIVE(SelectPalette)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectPalette_FUNC);
	rc = (jint)SelectPalette((HDC)arg0, (HPALETTE)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SelectPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_SendInput
JNIEXPORT jint JNICALL OS_NATIVE(SendInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendInput_FUNC);
	rc = (jint)SendInput(arg0, (LPINPUT)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SendInput_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIII_FUNC);
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	BUTTON_IMAGELIST _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
	if (arg3) if ((lparg3 = getBUTTON_IMAGELISTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setBUTTON_IMAGELISTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	HDITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
	if (arg3) if ((lparg3 = getHDITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	HDLAYOUT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
	if (arg3) if ((lparg3 = getHDLAYOUTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDLAYOUTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
	if (arg3) if ((lparg3 = getLITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
	if (arg3) if ((lparg3 = getLVCOLUMNFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVCOLUMNFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
	if (arg3) if ((lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
	if (arg3) if ((lparg3 = getLVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	MARGINS _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
	if (arg3) if ((lparg3 = getMARGINSFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMARGINSFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
	if (arg3) if ((lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setREBARBANDINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SIZE _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
	if (arg3) if ((lparg3 = getTBBUTTONFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
	if (arg3) if ((lparg3 = getTCITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTOOLINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTOOLINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
	if (arg3) if ((lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
	if (arg3) if ((lparg3 = getTVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	UDACCEL _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
	if (arg3) if ((lparg3 = getUDACCELFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setUDACCELFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3B
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3B_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3S_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__II_3II_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SendMessageA__II_3II_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageA__II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageA__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageA__II_3I_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SendMessageA__II_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIII
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIII_FUNC);
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	BUTTON_IMAGELIST _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
	if (arg3) if ((lparg3 = getBUTTON_IMAGELISTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setBUTTON_IMAGELISTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	HDITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
	if (arg3) if ((lparg3 = getHDITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	HDLAYOUT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
	if (arg3) if ((lparg3 = getHDLAYOUTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDLAYOUTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
	if (arg3) if ((lparg3 = getLITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
	if (arg3) if ((lparg3 = getLVCOLUMNFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVCOLUMNFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
	if (arg3) if ((lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVHITTESTINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	LVITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
	if (arg3) if ((lparg3 = getLVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	MARGINS _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
	if (arg3) if ((lparg3 = getMARGINSFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMARGINSFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
	if (arg3) if ((lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setREBARBANDINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SIZE _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TBBUTTON _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
	if (arg3) if ((lparg3 = getTBBUTTONFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TCITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
	if (arg3) if ((lparg3 = getTCITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TOOLINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTOOLINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTOOLINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
	if (arg3) if ((lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVHITTESTINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
	if (arg3) if ((lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	TVITEM _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
	if (arg3) if ((lparg3 = getTVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVITEMFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	UDACCEL _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
	if (arg3) if ((lparg3 = getUDACCELFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setUDACCELFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3C
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3C_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3I
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__III_3S
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__III_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3S_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_SendMessageW__II_3II
JNIEXPORT jint JNICALL OS_NATIVE(SendMessageW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendMessageW__II_3II_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SendMessageW__II_3II_FUNC);
	return rc;
}
#endif

#ifndef NO_SetActiveWindow
JNIEXPORT jint JNICALL OS_NATIVE(SetActiveWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetActiveWindow_FUNC);
	rc = (jint)SetActiveWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetActiveWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(SetBkColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetBkColor_FUNC);
	rc = (jint)SetBkColor((HDC)arg0, (COLORREF)arg1);
	OS_NATIVE_EXIT(env, that, SetBkColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetBkMode
JNIEXPORT jint JNICALL OS_NATIVE(SetBkMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetBkMode_FUNC);
	rc = (jint)SetBkMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetBkMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCapture
JNIEXPORT jint JNICALL OS_NATIVE(SetCapture)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetCapture_FUNC);
	rc = (jint)SetCapture((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetCapture_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCaretPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCaretPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetCaretPos_FUNC);
	rc = (jboolean)SetCaretPos(arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetCaretPos_FUNC);
	return rc;
}
#endif

#ifndef NO_SetClipboardData
JNIEXPORT jint JNICALL OS_NATIVE(SetClipboardData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetClipboardData_FUNC);
	rc = (jint)SetClipboardData(arg0, (HANDLE)arg1);
	OS_NATIVE_EXIT(env, that, SetClipboardData_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetCursor_FUNC);
	rc = (jint)SetCursor((HCURSOR)arg0);
	OS_NATIVE_EXIT(env, that, SetCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCursorPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetCursorPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetCursorPos_FUNC);
	rc = (jboolean)SetCursorPos(arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetCursorPos_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(SetDIBColorTable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDIBColorTable_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)SetDIBColorTable((HDC)arg0, arg1, arg2, (RGBQUAD *)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, JNI_ABORT);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, SetDIBColorTable_FUNC);
	return rc;
}
#endif

#ifndef NO_SetErrorMode
JNIEXPORT jint JNICALL OS_NATIVE(SetErrorMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetErrorMode_FUNC);
	rc = (jint)SetErrorMode(arg0);
	OS_NATIVE_EXIT(env, that, SetErrorMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetFocus
JNIEXPORT jint JNICALL OS_NATIVE(SetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetFocus_FUNC);
	rc = (jint)SetFocus((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_SetForegroundWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(SetForegroundWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetForegroundWindow_FUNC);
	rc = (jboolean)SetForegroundWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetForegroundWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetGraphicsMode
JNIEXPORT jint JNICALL OS_NATIVE(SetGraphicsMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetGraphicsMode_FUNC);
	rc = (jint)SetGraphicsMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetGraphicsMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetLayout
JNIEXPORT jint JNICALL OS_NATIVE(SetLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetLayout_FUNC);
/*
	rc = (jint)SetLayout((HDC)arg0, (DWORD)arg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(SetLayout_LIB);
			if (hm) fp = GetProcAddress(hm, "SetLayout");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HDC)arg0, (DWORD)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, SetLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenu_FUNC);
	rc = (jboolean)SetMenu((HWND)arg0, (HMENU)arg1);
	OS_NATIVE_EXIT(env, that, SetMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuDefaultItem
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuDefaultItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuDefaultItem_FUNC);
	rc = (jboolean)SetMenuDefaultItem((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetMenuDefaultItem_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuInfo_FUNC);
	if (arg1) if ((lparg1 = getMENUINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)SetMenuInfo((HMENU)arg0, lparg1);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(SetMenuInfo_LIB);
			if (hm) fp = GetProcAddress(hm, "SetMenuInfo");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp((HMENU)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setMENUINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetMenuInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemInfoA_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetMenuItemInfoA((HMENU)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetMenuItemInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemInfoW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenuItemInfoW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	MENUITEMINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemInfoW_FUNC);
	if (arg3) if ((lparg3 = getMENUITEMINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetMenuItemInfoW((HMENU)arg0, arg1, arg2, (LPMENUITEMINFOW)lparg3);
fail:
	if (arg3 && lparg3) setMENUITEMINFOFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetMenuItemInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMetaRgn
JNIEXPORT jint JNICALL OS_NATIVE(SetMetaRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMetaRgn_FUNC);
	rc = (jint)SetMetaRgn((HDC)arg0);
	OS_NATIVE_EXIT(env, that, SetMetaRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(SetPaletteEntries)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetPaletteEntries_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jint)SetPaletteEntries((HPALETTE)arg0, arg1, arg2, (PALETTEENTRY *)lparg3);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, JNI_ABORT);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, JNI_ABORT);
	}
	OS_NATIVE_EXIT(env, that, SetPaletteEntries_FUNC);
	return rc;
}
#endif

#ifndef NO_SetParent
JNIEXPORT jint JNICALL OS_NATIVE(SetParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetParent_FUNC);
	rc = (jint)SetParent((HWND)arg0, (HWND)arg1);
	OS_NATIVE_EXIT(env, that, SetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPixel
JNIEXPORT jint JNICALL OS_NATIVE(SetPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetPixel_FUNC);
	rc = (jint)SetPixel((HDC)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, SetPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPolyFillMode
JNIEXPORT jint JNICALL OS_NATIVE(SetPolyFillMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetPolyFillMode_FUNC);
	rc = (jint)SetPolyFillMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetPolyFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPropA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetPropA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetPropA_FUNC);
	rc = (jboolean)SetPropA((HWND)arg0, (LPCTSTR)arg1, (HANDLE)arg2);
	OS_NATIVE_EXIT(env, that, SetPropA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPropW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetPropW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetPropW_FUNC);
	rc = (jboolean)SetPropW((HWND)arg0, (LPCWSTR)arg1, (HANDLE)arg2);
	OS_NATIVE_EXIT(env, that, SetPropW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetROP2
JNIEXPORT jint JNICALL OS_NATIVE(SetROP2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetROP2_FUNC);
	rc = (jint)SetROP2((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetROP2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	RECT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetRect_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	rc = (jboolean)SetRect(lparg0, arg1, arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SetRect_FUNC);
	return rc;
}
#endif

#ifndef NO_SetRectRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(SetRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetRectRgn_FUNC);
	rc = (jboolean)SetRectRgn((HRGN)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, SetRectRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_SetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SetScrollInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3)
{
	SCROLLINFO _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetScrollInfo_FUNC);
	if (arg2) if ((lparg2 = getSCROLLINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SetScrollInfo((HWND)arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setSCROLLINFOFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SetScrollInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_SetStretchBltMode
JNIEXPORT jint JNICALL OS_NATIVE(SetStretchBltMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetStretchBltMode_FUNC);
	rc = (jint)SetStretchBltMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetStretchBltMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetTextAlign
JNIEXPORT jint JNICALL OS_NATIVE(SetTextAlign)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetTextAlign_FUNC);
	rc = (jint)SetTextAlign((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetTextAlign_FUNC);
	return rc;
}
#endif

#ifndef NO_SetTextColor
JNIEXPORT jint JNICALL OS_NATIVE(SetTextColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetTextColor_FUNC);
	rc = (jint)SetTextColor((HDC)arg0, (COLORREF)arg1);
	OS_NATIVE_EXIT(env, that, SetTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetTimer
JNIEXPORT jint JNICALL OS_NATIVE(SetTimer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetTimer_FUNC);
	rc = (jint)SetTimer((HWND)arg0, arg1, arg2, (TIMERPROC)arg3);
	OS_NATIVE_EXIT(env, that, SetTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowLongA_FUNC);
	rc = (jint)SetWindowLongA((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetWindowLongA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowLongW_FUNC);
	rc = (jint)SetWindowLongW((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetWindowLongW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowOrgEx
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowOrgEx)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	POINT _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowOrgEx_FUNC);
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetWindowOrgEx((HDC)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetWindowOrgEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowPlacement
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPlacement)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	WINDOWPLACEMENT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowPlacement_FUNC);
	if (arg1) if ((lparg1 = getWINDOWPLACEMENTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)SetWindowPlacement((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setWINDOWPLACEMENTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetWindowPlacement_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowPos_FUNC);
	rc = (jboolean)SetWindowPos((HWND)arg0, (HWND)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, SetWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowRgn
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowRgn_FUNC);
	rc = (jint)SetWindowRgn((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetWindowRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowTextA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowTextA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)SetWindowTextA((HWND)arg0, (LPSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SetWindowTextA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowTextW
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowTextW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowTextW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)SetWindowTextW((HWND)arg0, (LPWSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SetWindowTextW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowsHookExA_FUNC);
	rc = (jint)SetWindowsHookExA(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	OS_NATIVE_EXIT(env, that, SetWindowsHookExA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowsHookExW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowsHookExW_FUNC);
	rc = (jint)SetWindowsHookExW(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	OS_NATIVE_EXIT(env, that, SetWindowsHookExW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWorldTransform
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWorldTransform)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWorldTransform_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)SetWorldTransform((HDC)arg0, (XFORM *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SetWorldTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_ShellExecuteExA
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShellExecuteExA_FUNC);
	if (arg0) if ((lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ShellExecuteExA(lparg0);
fail:
	if (arg0 && lparg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ShellExecuteExA_FUNC);
	return rc;
}
#endif

#ifndef NO_ShellExecuteExW
JNIEXPORT jboolean JNICALL OS_NATIVE(ShellExecuteExW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SHELLEXECUTEINFO _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShellExecuteExW_FUNC);
	if (arg0) if ((lparg0 = getSHELLEXECUTEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)ShellExecuteExW((LPSHELLEXECUTEINFOW)lparg0);
fail:
	if (arg0 && lparg0) setSHELLEXECUTEINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ShellExecuteExW_FUNC);
	return rc;
}
#endif

#ifndef NO_Shell_1NotifyIconA
JNIEXPORT jboolean JNICALL OS_NATIVE(Shell_1NotifyIconA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NOTIFYICONDATAA _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Shell_1NotifyIconA_FUNC);
	if (arg1) if ((lparg1 = getNOTIFYICONDATAAFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)Shell_NotifyIconA(arg0, lparg1);
fail:
	if (arg1 && lparg1) setNOTIFYICONDATAAFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, Shell_1NotifyIconA_FUNC);
	return rc;
}
#endif

#ifndef NO_Shell_1NotifyIconW
JNIEXPORT jboolean JNICALL OS_NATIVE(Shell_1NotifyIconW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NOTIFYICONDATAW _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Shell_1NotifyIconW_FUNC);
	if (arg1) if ((lparg1 = getNOTIFYICONDATAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)Shell_NotifyIconW(arg0, lparg1);
fail:
	if (arg1 && lparg1) setNOTIFYICONDATAWFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, Shell_1NotifyIconW_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowCaret)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShowCaret_FUNC);
	rc = (jboolean)ShowCaret((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ShowCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowOwnedPopups
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowOwnedPopups)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShowOwnedPopups_FUNC);
	rc = (jboolean)ShowOwnedPopups((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ShowOwnedPopups_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowScrollBar
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShowScrollBar_FUNC);
	rc = (jboolean)ShowScrollBar((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ShowScrollBar_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShowWindow_FUNC);
	rc = (jboolean)ShowWindow((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ShowWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SipGetInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(SipGetInfo)
	(JNIEnv *env, jclass that, jobject arg0)
{
	SIPINFO _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SipGetInfo_FUNC);
	if (arg0) if ((lparg0 = getSIPINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)SipGetInfo(lparg0);
fail:
	if (arg0 && lparg0) setSIPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SipGetInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_StartDocA
JNIEXPORT jint JNICALL OS_NATIVE(StartDocA)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StartDocA_FUNC);
	if (arg1) if ((lparg1 = getDOCINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)StartDocA((HDC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setDOCINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, StartDocA_FUNC);
	return rc;
}
#endif

#ifndef NO_StartDocW
JNIEXPORT jint JNICALL OS_NATIVE(StartDocW)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DOCINFO _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StartDocW_FUNC);
	if (arg1) if ((lparg1 = getDOCINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)StartDocW((HDC)arg0, (LPDOCINFOW)lparg1);
fail:
	if (arg1 && lparg1) setDOCINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, StartDocW_FUNC);
	return rc;
}
#endif

#ifndef NO_StartPage
JNIEXPORT jint JNICALL OS_NATIVE(StartPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, StartPage_FUNC);
	rc = (jint)StartPage((HDC)arg0);
	OS_NATIVE_EXIT(env, that, StartPage_FUNC);
	return rc;
}
#endif

#ifndef NO_StretchBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(StretchBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, StretchBlt_FUNC);
	rc = (jboolean)StretchBlt((HDC)arg0, arg1, arg2, arg3, arg4, (HDC)arg5, arg6, arg7, arg8, arg9, arg10);
	OS_NATIVE_EXIT(env, that, StretchBlt_FUNC);
	return rc;
}
#endif

#ifndef NO_StrokePath
JNIEXPORT jboolean JNICALL OS_NATIVE(StrokePath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, StrokePath_FUNC);
	rc = (jboolean)StrokePath((HDC)arg0);
	OS_NATIVE_EXIT(env, that, StrokePath_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	HIGHCONTRAST _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC);
	if (arg2) if ((lparg2 = getHIGHCONTRASTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setHIGHCONTRASTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSA _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I_FUNC);
	if (arg2) if ((lparg2 = getNONCLIENTMETRICSAFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setNONCLIENTMETRICSAFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoA__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoA__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoA__II_3II_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoA(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoA__II_3II_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	HIGHCONTRAST _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC);
	if (arg2) if ((lparg2 = getHIGHCONTRASTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setHIGHCONTRASTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NONCLIENTMETRICSW _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I_FUNC);
	if (arg2) if ((lparg2 = getNONCLIENTMETRICSWFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setNONCLIENTMETRICSWFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_SystemParametersInfoW__II_3II
JNIEXPORT jboolean JNICALL OS_NATIVE(SystemParametersInfoW__II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SystemParametersInfoW__II_3II_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)SystemParametersInfoW(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SystemParametersInfoW__II_3II_FUNC);
	return rc;
}
#endif

#ifndef NO_ToAscii
JNIEXPORT jint JNICALL OS_NATIVE(ToAscii)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jshortArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToAscii_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ToAscii(arg0, arg1, (PBYTE)lparg2, (LPWORD)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ToAscii_FUNC);
	return rc;
}
#endif

#ifndef NO_ToUnicode
JNIEXPORT jint JNICALL OS_NATIVE(ToUnicode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jcharArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ToUnicode_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ToUnicode(arg0, arg1, (PBYTE)lparg2, (LPWSTR)lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ToUnicode_FUNC);
	return rc;
}
#endif

#ifndef NO_TrackMouseEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackMouseEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	TRACKMOUSEEVENT _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TrackMouseEvent_FUNC);
	if (arg0) if ((lparg0 = getTRACKMOUSEEVENTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)TrackMouseEvent(lparg0);
fail:
	if (arg0 && lparg0) setTRACKMOUSEEVENTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, TrackMouseEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_TrackPopupMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(TrackPopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6)
{
	RECT _arg6, *lparg6=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TrackPopupMenu_FUNC);
	if (arg6) if ((lparg6 = getRECTFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jboolean)TrackPopupMenu((HMENU)arg0, arg1, arg2, arg3, arg4, (HWND)arg5, lparg6);
fail:
	if (arg6 && lparg6) setRECTFields(env, arg6, lparg6);
	OS_NATIVE_EXIT(env, that, TrackPopupMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_TranslateAcceleratorA
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TranslateAcceleratorA_FUNC);
	if (arg2) if ((lparg2 = getMSGFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)TranslateAcceleratorA((HWND)arg0, (HACCEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) setMSGFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, TranslateAcceleratorA_FUNC);
	return rc;
}
#endif

#ifndef NO_TranslateAcceleratorW
JNIEXPORT jint JNICALL OS_NATIVE(TranslateAcceleratorW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TranslateAcceleratorW_FUNC);
	if (arg2) if ((lparg2 = getMSGFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)TranslateAcceleratorW((HWND)arg0, (HACCEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) setMSGFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, TranslateAcceleratorW_FUNC);
	return rc;
}
#endif

#ifndef NO_TranslateCharsetInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateCharsetInfo)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TranslateCharsetInfo_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)TranslateCharsetInfo((DWORD *)arg0, (LPCHARSETINFO)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, TranslateCharsetInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_TranslateMDISysAccel
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateMDISysAccel)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MSG _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TranslateMDISysAccel_FUNC);
	if (arg1) if ((lparg1 = getMSGFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)TranslateMDISysAccel((HWND)arg0, (LPMSG)lparg1);
fail:
	if (arg1 && lparg1) setMSGFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, TranslateMDISysAccel_FUNC);
	return rc;
}
#endif

#ifndef NO_TranslateMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(TranslateMessage)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TranslateMessage_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)TranslateMessage(lparg0);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, TranslateMessage_FUNC);
	return rc;
}
#endif

#ifndef NO_TransparentBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(TransparentBlt)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TransparentBlt_FUNC);
/*
	rc = (jboolean)TransparentBlt(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(TransparentBlt_LIB);
			if (hm) fp = GetProcAddress(hm, "TransparentBlt");
			initialized = 1;
		}
		if (fp) {
			rc = (jboolean)fp(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		}
	}
	OS_NATIVE_EXIT(env, that, TransparentBlt_FUNC);
	return rc;
}
#endif

#ifndef NO_TransparentImage
JNIEXPORT jboolean JNICALL OS_NATIVE(TransparentImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TransparentImage_FUNC);
	rc = (jboolean)TransparentImage((HDC)arg0, arg1, arg2, arg3, arg4, (HANDLE)arg5, arg6, arg7, arg8, arg9, (COLORREF)arg10);
	OS_NATIVE_EXIT(env, that, TransparentImage_FUNC);
	return rc;
}
#endif

#ifndef NO_UnhookWindowsHookEx
JNIEXPORT jboolean JNICALL OS_NATIVE(UnhookWindowsHookEx)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UnhookWindowsHookEx_FUNC);
	rc = (jboolean)UnhookWindowsHookEx((HHOOK)arg0);
	OS_NATIVE_EXIT(env, that, UnhookWindowsHookEx_FUNC);
	return rc;
}
#endif

#ifndef NO_UnregisterClassA
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UnregisterClassA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)UnregisterClassA((LPSTR)lparg0, (HINSTANCE)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, UnregisterClassA_FUNC);
	return rc;
}
#endif

#ifndef NO_UnregisterClassW
JNIEXPORT jboolean JNICALL OS_NATIVE(UnregisterClassW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1)
{
	jchar *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UnregisterClassW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)UnregisterClassW((LPWSTR)lparg0, (HINSTANCE)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, UnregisterClassW_FUNC);
	return rc;
}
#endif

#ifndef NO_UpdateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(UpdateWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UpdateWindow_FUNC);
	rc = (jboolean)UpdateWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, UpdateWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ValidateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(ValidateRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ValidateRect_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ValidateRect((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ValidateRect_FUNC);
	return rc;
}
#endif

#ifndef NO_VkKeyScanA
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanA)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, VkKeyScanA_FUNC);
	rc = (jshort)VkKeyScanA((TCHAR)arg0);
	OS_NATIVE_EXIT(env, that, VkKeyScanA_FUNC);
	return rc;
}
#endif

#ifndef NO_VkKeyScanW
JNIEXPORT jshort JNICALL OS_NATIVE(VkKeyScanW)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, VkKeyScanW_FUNC);
	rc = (jshort)VkKeyScanW((WCHAR)arg0);
	OS_NATIVE_EXIT(env, that, VkKeyScanW_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VtblCall__II_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint))(*(jint **)arg1)[arg0])(arg1);
	OS_NATIVE_EXIT(env, that, VtblCall__II_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VtblCall__III_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2);
	OS_NATIVE_EXIT(env, that, VtblCall__III_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIII_3I
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VtblCall__IIIII_3I_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, VtblCall__IIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CII_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II_3CII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jchar *lparg2=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, VtblCall__II_3CII_3I_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, VtblCall__II_3CII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_WaitMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(WaitMessage)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WaitMessage_FUNC);
	rc = (jboolean)WaitMessage();
	OS_NATIVE_EXIT(env, that, WaitMessage_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WideCharToMultiByte__II_3CIII_3B_3Z_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	}
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)arg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	} else
#endif
	{
		if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, JNI_ABORT);
	}
	if (arg7 && lparg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, WideCharToMultiByte__II_3CIII_3B_3Z_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WideCharToMultiByte__II_3CI_3BI_3B_3Z_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetBooleanArrayElements(env, arg7, NULL)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) if ((lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
		if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jint)WideCharToMultiByte(arg0, arg1, (LPCWSTR)lparg2, arg3, (LPSTR)lparg4, arg5, (LPCSTR)lparg6, (LPBOOL)lparg7);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4 && lparg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg2 && lparg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, JNI_ABORT);
	} else
#endif
	{
		if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
		if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, JNI_ABORT);
	}
	if (arg7 && lparg7) (*env)->ReleaseBooleanArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, WideCharToMultiByte__II_3CI_3BI_3B_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowFromDC
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromDC)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WindowFromDC_FUNC);
	rc = (jint)WindowFromDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, WindowFromDC_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowFromPoint
JNIEXPORT jint JNICALL OS_NATIVE(WindowFromPoint)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WindowFromPoint_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)WindowFromPoint(*lparg0);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, WindowFromPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL OS_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((const char *)arg0);
	OS_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif

#ifndef NO_wcslen
JNIEXPORT jint JNICALL OS_NATIVE(wcslen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, wcslen_FUNC);
	rc = (jint)wcslen((const wchar_t *)arg0);
	OS_NATIVE_EXIT(env, that, wcslen_FUNC);
	return rc;
}
#endif


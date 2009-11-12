/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_win32_OS_##func

#ifndef NO_ACCEL_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(ACCEL_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ACCEL_1sizeof_FUNC);
	rc = (jint)ACCEL_sizeof();
	OS_NATIVE_EXIT(env, that, ACCEL_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_ACTCTX_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(ACTCTX_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ACTCTX_1sizeof_FUNC);
	rc = (jint)ACTCTX_sizeof();
	OS_NATIVE_EXIT(env, that, ACTCTX_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_AbortDoc
JNIEXPORT jint JNICALL OS_NATIVE(AbortDoc)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AbortDoc_FUNC);
	rc = (jint)AbortDoc((HDC)arg0);
	OS_NATIVE_EXIT(env, that, AbortDoc_FUNC);
	return rc;
}
#endif

#ifndef NO_ActivateActCtx
JNIEXPORT jboolean JNICALL OS_NATIVE(ActivateActCtx)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ActivateActCtx_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)ActivateActCtx(arg0, (ULONG_PTR*)lparg1);
*/
	{
		LOAD_FUNCTION(fp, ActivateActCtx)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, ULONG_PTR*))fp)(arg0, (ULONG_PTR*)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ActivateActCtx_FUNC);
	return rc;
}
#endif

#ifndef NO_ActivateKeyboardLayout
JNIEXPORT jintLong JNICALL OS_NATIVE(ActivateKeyboardLayout)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ActivateKeyboardLayout_FUNC);
	rc = (jintLong)ActivateKeyboardLayout((HKL)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ActivateKeyboardLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_AddFontResourceExA
JNIEXPORT jint JNICALL OS_NATIVE(AddFontResourceExA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintLong arg2)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AddFontResourceExA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jint)AddFontResourceExA(lparg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, AddFontResourceExA)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jbyte *, jint, jintLong))fp)(lparg0, arg1, arg2);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, AddFontResourceExA_FUNC);
	return rc;
}
#endif

#ifndef NO_AddFontResourceExW
JNIEXPORT jint JNICALL OS_NATIVE(AddFontResourceExW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintLong arg2)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AddFontResourceExW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jint)AddFontResourceExW(lparg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, AddFontResourceExW)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jchar *, jint, jintLong))fp)(lparg0, arg1, arg2);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, AddFontResourceExW_FUNC);
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

#ifndef NO_AllowSetForegroundWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(AllowSetForegroundWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AllowSetForegroundWindow_FUNC);
/*
	rc = (jboolean)AllowSetForegroundWindow(arg0);
*/
	{
		LOAD_FUNCTION(fp, AllowSetForegroundWindow)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jint))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, AllowSetForegroundWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_AlphaBlend
JNIEXPORT jboolean JNICALL OS_NATIVE(AlphaBlend)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	BLENDFUNCTION _arg10, *lparg10=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AlphaBlend_FUNC);
	if (arg10) if ((lparg10 = getBLENDFUNCTIONFields(env, arg10, &_arg10)) == NULL) goto fail;
/*
	rc = (jboolean)AlphaBlend(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, *lparg10);
*/
	{
		LOAD_FUNCTION(fp, AlphaBlend)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jint, jint, jint, jint, jintLong, jint, jint, jint, jint, BLENDFUNCTION))fp)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, *lparg10);
		}
	}
fail:
	if (arg10 && lparg10) setBLENDFUNCTIONFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, AlphaBlend_FUNC);
	return rc;
}
#endif

#ifndef NO_AnimateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(AnimateWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AnimateWindow_FUNC);
/*
	rc = (jboolean)AnimateWindow((HWND)arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, AnimateWindow)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, jint, jint))fp)((HWND)arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, AnimateWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_Arc
JNIEXPORT jboolean JNICALL OS_NATIVE(Arc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, Arc_FUNC);
	rc = (jboolean)Arc((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, Arc_FUNC);
	return rc;
}
#endif

#ifndef NO_AssocQueryStringA
JNIEXPORT jint JNICALL OS_NATIVE(AssocQueryStringA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AssocQueryStringA_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
/*
	rc = (jint)AssocQueryStringA(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
*/
	{
		LOAD_FUNCTION(fp, AssocQueryStringA)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jbyte *, jbyte *, jbyte *, jint *))fp)(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
		}
	}
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, AssocQueryStringA_FUNC);
	return rc;
}
#endif

#ifndef NO_AssocQueryStringW
JNIEXPORT jint JNICALL OS_NATIVE(AssocQueryStringW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AssocQueryStringW_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
/*
	rc = (jint)AssocQueryStringW(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
*/
	{
		LOAD_FUNCTION(fp, AssocQueryStringW)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jchar *, jchar *, jchar *, jint *))fp)(arg0, arg1, lparg2, lparg3, lparg4, lparg5);
		}
	}
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, AssocQueryStringW_FUNC);
	return rc;
}
#endif

#ifndef NO_AttachThreadInput
JNIEXPORT jboolean JNICALL OS_NATIVE(AttachThreadInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AttachThreadInput_FUNC);
	rc = (jboolean)AttachThreadInput((DWORD)arg0, (DWORD)arg1, arg2);
	OS_NATIVE_EXIT(env, that, AttachThreadInput_FUNC);
	return rc;
}
#endif

#ifndef NO_BITMAPINFOHEADER_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BITMAPINFOHEADER_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BITMAPINFOHEADER_1sizeof_FUNC);
	rc = (jint)BITMAPINFOHEADER_sizeof();
	OS_NATIVE_EXIT(env, that, BITMAPINFOHEADER_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BITMAP_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BITMAP_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BITMAP_1sizeof_FUNC);
	rc = (jint)BITMAP_sizeof();
	OS_NATIVE_EXIT(env, that, BITMAP_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BLENDFUNCTION_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BLENDFUNCTION_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BLENDFUNCTION_1sizeof_FUNC);
	rc = (jint)BLENDFUNCTION_sizeof();
	OS_NATIVE_EXIT(env, that, BLENDFUNCTION_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BP_1PAINTPARAMS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BP_1PAINTPARAMS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BP_1PAINTPARAMS_1sizeof_FUNC);
	rc = (jint)BP_PAINTPARAMS_sizeof();
	OS_NATIVE_EXIT(env, that, BP_1PAINTPARAMS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BROWSEINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BROWSEINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BROWSEINFO_1sizeof_FUNC);
	rc = (jint)BROWSEINFO_sizeof();
	OS_NATIVE_EXIT(env, that, BROWSEINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BUTTON_1IMAGELIST_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(BUTTON_1IMAGELIST_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BUTTON_1IMAGELIST_1sizeof_FUNC);
	rc = (jint)BUTTON_IMAGELIST_sizeof();
	OS_NATIVE_EXIT(env, that, BUTTON_1IMAGELIST_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginBufferedPaint
JNIEXPORT jintLong JNICALL OS_NATIVE(BeginBufferedPaint)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2, jobject arg3, jintLongArray arg4)
{
	RECT _arg1, *lparg1=NULL;
	BP_PAINTPARAMS _arg3, *lparg3=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, BeginBufferedPaint_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getBP_PAINTPARAMSFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)BeginBufferedPaint((HDC)arg0, lparg1, arg2, lparg3, (HDC*)lparg4);
*/
	{
		LOAD_FUNCTION(fp, BeginBufferedPaint)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(HDC, RECT *, jint, BP_PAINTPARAMS *, HDC*))fp)((HDC)arg0, lparg1, arg2, lparg3, (HDC*)lparg4);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setBP_PAINTPARAMSFields(env, arg3, lparg3);
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, BeginBufferedPaint_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginDeferWindowPos
JNIEXPORT jintLong JNICALL OS_NATIVE(BeginDeferWindowPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, BeginDeferWindowPos_FUNC);
	rc = (jintLong)BeginDeferWindowPos(arg0);
	OS_NATIVE_EXIT(env, that, BeginDeferWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginPaint
JNIEXPORT jintLong JNICALL OS_NATIVE(BeginPaint)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	PAINTSTRUCT _arg1, *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, BeginPaint_FUNC);
	if (arg1) if ((lparg1 = getPAINTSTRUCTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)BeginPaint((HWND)arg0, lparg1);
fail:
	if (arg1 && lparg1) setPAINTSTRUCTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, BeginPaint_FUNC);
	return rc;
}
#endif

#ifndef NO_BeginPath
JNIEXPORT jboolean JNICALL OS_NATIVE(BeginPath)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7, jint arg8)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, BringWindowToTop_FUNC);
	rc = (jboolean)BringWindowToTop((HWND)arg0);
	OS_NATIVE_EXIT(env, that, BringWindowToTop_FUNC);
	return rc;
}
#endif

#ifndef NO_BufferedPaintInit
JNIEXPORT jint JNICALL OS_NATIVE(BufferedPaintInit)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BufferedPaintInit_FUNC);
/*
	rc = (jint)BufferedPaintInit();
*/
	{
		LOAD_FUNCTION(fp, BufferedPaintInit)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, BufferedPaintInit_FUNC);
	return rc;
}
#endif

#ifndef NO_BufferedPaintSetAlpha
JNIEXPORT jint JNICALL OS_NATIVE(BufferedPaintSetAlpha)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jbyte arg2)
{
	RECT _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BufferedPaintSetAlpha_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jint)BufferedPaintSetAlpha((HPAINTBUFFER)arg0, lparg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, BufferedPaintSetAlpha)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HPAINTBUFFER, RECT *, jbyte))fp)((HPAINTBUFFER)arg0, lparg1, arg2);
		}
	}
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, BufferedPaintSetAlpha_FUNC);
	return rc;
}
#endif

#ifndef NO_BufferedPaintUnInit
JNIEXPORT jint JNICALL OS_NATIVE(BufferedPaintUnInit)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, BufferedPaintUnInit_FUNC);
/*
	rc = (jint)BufferedPaintUnInit();
*/
	{
		LOAD_FUNCTION(fp, BufferedPaintUnInit)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, BufferedPaintUnInit_FUNC);
	return rc;
}
#endif

#ifndef NO_CANDIDATEFORM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CANDIDATEFORM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CANDIDATEFORM_1sizeof_FUNC);
	rc = (jint)CANDIDATEFORM_sizeof();
	OS_NATIVE_EXIT(env, that, CANDIDATEFORM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CHOOSECOLOR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CHOOSECOLOR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CHOOSECOLOR_1sizeof_FUNC);
	rc = (jint)CHOOSECOLOR_sizeof();
	OS_NATIVE_EXIT(env, that, CHOOSECOLOR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CHOOSEFONT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CHOOSEFONT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CHOOSEFONT_1sizeof_FUNC);
	rc = (jint)CHOOSEFONT_sizeof();
	OS_NATIVE_EXIT(env, that, CHOOSEFONT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_COMBOBOXINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(COMBOBOXINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, COMBOBOXINFO_1sizeof_FUNC);
	rc = (jint)COMBOBOXINFO_sizeof();
	OS_NATIVE_EXIT(env, that, COMBOBOXINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_COMPOSITIONFORM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(COMPOSITIONFORM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, COMPOSITIONFORM_1sizeof_FUNC);
	rc = (jint)COMPOSITIONFORM_sizeof();
	OS_NATIVE_EXIT(env, that, COMPOSITIONFORM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CREATESTRUCT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CREATESTRUCT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CREATESTRUCT_1sizeof_FUNC);
	rc = (jint)CREATESTRUCT_sizeof();
	OS_NATIVE_EXIT(env, that, CREATESTRUCT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_Call
JNIEXPORT jint JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(CallNextHookEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CallNextHookEx_FUNC);
	rc = (jintLong)CallNextHookEx((HHOOK)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, CallNextHookEx_FUNC);
	return rc;
}
#endif

#ifndef NO_CallWindowProcA
JNIEXPORT jintLong JNICALL OS_NATIVE(CallWindowProcA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CallWindowProcA_FUNC);
	rc = (jintLong)CallWindowProcA((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CallWindowProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_CallWindowProcW
JNIEXPORT jintLong JNICALL OS_NATIVE(CallWindowProcW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CallWindowProcW_FUNC);
	rc = (jintLong)CallWindowProcW((WNDPROC)arg0, (HWND)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CallWindowProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_CharLowerA
JNIEXPORT jintLong JNICALL OS_NATIVE(CharLowerA)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CharLowerA_FUNC);
	rc = (jintLong)CharLowerA((LPSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharLowerA_FUNC);
	return rc;
}
#endif

#ifndef NO_CharLowerW
JNIEXPORT jintLong JNICALL OS_NATIVE(CharLowerW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CharLowerW_FUNC);
	rc = (jintLong)CharLowerW((LPWSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharLowerW_FUNC);
	return rc;
}
#endif

#ifndef NO_CharUpperA
JNIEXPORT jintLong JNICALL OS_NATIVE(CharUpperA)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CharUpperA_FUNC);
	rc = (jintLong)CharUpperA((LPSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharUpperA_FUNC);
	return rc;
}
#endif

#ifndef NO_CharUpperW
JNIEXPORT jintLong JNICALL OS_NATIVE(CharUpperW)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CharUpperW_FUNC);
	rc = (jintLong)CharUpperW((LPWSTR)arg0);
	OS_NATIVE_EXIT(env, that, CharUpperW_FUNC);
	return rc;
}
#endif

#ifndef NO_CheckMenuItem
JNIEXPORT jboolean JNICALL OS_NATIVE(CheckMenuItem)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#ifndef NO_CloseEnhMetaFile
JNIEXPORT jintLong JNICALL OS_NATIVE(CloseEnhMetaFile)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CloseEnhMetaFile_FUNC);
	rc = (jintLong)CloseEnhMetaFile((HDC)arg0);
	OS_NATIVE_EXIT(env, that, CloseEnhMetaFile_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseHandle
JNIEXPORT jboolean JNICALL OS_NATIVE(CloseHandle)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CloseHandle_FUNC);
	rc = (jboolean)CloseHandle((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, CloseHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseThemeData
JNIEXPORT jint JNICALL OS_NATIVE(CloseThemeData)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CloseThemeData_FUNC);
/*
	rc = (jint)CloseThemeData((HTHEME)arg0);
*/
	{
		LOAD_FUNCTION(fp, CloseThemeData)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HTHEME))fp)((HTHEME)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, CloseThemeData_FUNC);
	return rc;
}
#endif

#ifndef NO_CoCreateInstance
JNIEXPORT jint JNICALL OS_NATIVE(CoCreateInstance)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintLongArray arg4)
{
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CoCreateInstance_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoCreateInstance((REFCLSID)lparg0, (LPUNKNOWN)arg1, arg2, (REFIID)lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CoCreateInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_CoInternetIsFeatureEnabled
JNIEXPORT jint JNICALL OS_NATIVE(CoInternetIsFeatureEnabled)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CoInternetIsFeatureEnabled_FUNC);
/*
	rc = (jint)CoInternetIsFeatureEnabled(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, CoInternetIsFeatureEnabled)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, CoInternetIsFeatureEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_CoInternetSetFeatureEnabled
JNIEXPORT jint JNICALL OS_NATIVE(CoInternetSetFeatureEnabled)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CoInternetSetFeatureEnabled_FUNC);
/*
	rc = (jint)CoInternetSetFeatureEnabled(arg0, arg1, (BOOL)arg2);
*/
	{
		LOAD_FUNCTION(fp, CoInternetSetFeatureEnabled)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, BOOL))fp)(arg0, arg1, (BOOL)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, CoInternetSetFeatureEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_CombineRgn
JNIEXPORT jint JNICALL OS_NATIVE(CombineRgn)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1AddAdornments_FUNC);
	rc = (jboolean)CommandBar_AddAdornments((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CommandBar_1AddAdornments_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Create
JNIEXPORT jintLong JNICALL OS_NATIVE(CommandBar_1Create)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1Create_FUNC);
	rc = (jintLong)CommandBar_Create((HINSTANCE)arg0, (HWND)arg1, arg2);
	OS_NATIVE_EXIT(env, that, CommandBar_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_CommandBar_1Destroy
JNIEXPORT void JNICALL OS_NATIVE(CommandBar_1Destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, CommandBar_1Destroy_FUNC);
	CommandBar_Destroy((HWND)arg0);
	OS_NATIVE_EXIT(env, that, CommandBar_1Destroy_FUNC);
}
#endif

#ifndef NO_CommandBar_1DrawMenuBar
JNIEXPORT jboolean JNICALL OS_NATIVE(CommandBar_1DrawMenuBar)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CommandBar_1Show_FUNC);
	rc = (jboolean)CommandBar_Show((HWND)arg0, (BOOL)arg1);
	OS_NATIVE_EXIT(env, that, CommandBar_1Show_FUNC);
	return rc;
}
#endif

#ifndef NO_CopyImage
JNIEXPORT jintLong JNICALL OS_NATIVE(CopyImage)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CopyImage_FUNC);
	rc = (jintLong)CopyImage((HANDLE)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, CopyImage_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableA
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateAcceleratorTableA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateAcceleratorTableA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateAcceleratorTableA((LPACCEL)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateAcceleratorTableA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateAcceleratorTableW
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateAcceleratorTableW)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateAcceleratorTableW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateAcceleratorTableW((LPACCEL)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateAcceleratorTableW_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateActCtxA
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateActCtxA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ACTCTX _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateActCtxA_FUNC);
	if (arg0) if ((lparg0 = getACTCTXFields(env, arg0, &_arg0)) == NULL) goto fail;
/*
	rc = (jintLong)CreateActCtxA(lparg0);
*/
	{
		LOAD_FUNCTION(fp, CreateActCtxA)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(ACTCTX *))fp)(lparg0);
		}
	}
fail:
	OS_NATIVE_EXIT(env, that, CreateActCtxA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateActCtxW
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateActCtxW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ACTCTX _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateActCtxW_FUNC);
	if (arg0) if ((lparg0 = getACTCTXFields(env, arg0, &_arg0)) == NULL) goto fail;
/*
	rc = (jintLong)CreateActCtxW(lparg0);
*/
	{
		LOAD_FUNCTION(fp, CreateActCtxW)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(ACTCTX *))fp)(lparg0);
		}
	}
fail:
	OS_NATIVE_EXIT(env, that, CreateActCtxW_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateBitmap
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateBitmap_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) if ((lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)CreateBitmap(arg0, arg1, arg2, arg3, (CONST VOID *)lparg4);
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCaret_FUNC);
	rc = (jboolean)CreateCaret((HWND)arg0, (HBITMAP)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, CreateCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCompatibleBitmap
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateCompatibleBitmap)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCompatibleBitmap_FUNC);
	rc = (jintLong)CreateCompatibleBitmap((HDC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CreateCompatibleBitmap_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCompatibleDC
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateCompatibleDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCompatibleDC_FUNC);
	rc = (jintLong)CreateCompatibleDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, CreateCompatibleDC_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCursor
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateCursor)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jintLong rc = 0;
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
	rc = (jintLong)CreateCursor((HINSTANCE)arg0, arg1, arg2, arg3, arg4, (CONST VOID *)lparg5, (CONST VOID *)lparg6);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDCA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDCA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateDCA((LPSTR)lparg0, (LPSTR)lparg1, (LPSTR)arg2, (CONST DEVMODE *)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateDCA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateDCW
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDCW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jintLong arg2, jintLong arg3)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDCW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateDCW((LPWSTR)lparg0, (LPWSTR)lparg1, (LPWSTR)arg2, (CONST DEVMODEW *)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreateDCW_FUNC);
	return rc;
}
#endif

#if (!defined(NO_CreateDIBSection__III_3III) && !defined(JNI64)) || (!defined(NO_CreateDIBSection__JJI_3JJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDIBSection__III_3III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3, jintLong arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDIBSection__JJI_3JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3, jintLong arg4, jint arg5)
#endif
{
	jintLong *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, CreateDIBSection__III_3III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, CreateDIBSection__JJI_3JJI_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)CreateDIBSection((HDC)arg0, (BITMAPINFO *)arg1, arg2, (VOID **)lparg3, (HANDLE)arg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, CreateDIBSection__III_3III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, CreateDIBSection__JJI_3JJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_CreateDIBSection__I_3BI_3III) && !defined(JNI64)) || (!defined(NO_CreateDIBSection__J_3BI_3JJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDIBSection__I_3BI_3III)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintLongArray arg3, jintLong arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateDIBSection__J_3BI_3JJI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jintLongArray arg3, jintLong arg4, jint arg5)
#endif
{
	jbyte *lparg1=NULL;
	jintLong *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, CreateDIBSection__I_3BI_3III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, CreateDIBSection__J_3BI_3JJI_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
		if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)CreateDIBSection((HDC)arg0, (BITMAPINFO *)lparg1, arg2, (VOID **)lparg3, (HANDLE)arg4, arg5);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3 && lparg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, CreateDIBSection__I_3BI_3III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, CreateDIBSection__J_3BI_3JJI_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_CreateEnhMetaFileA
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateEnhMetaFileA)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2, jbyteArray arg3)
{
	jbyte *lparg1=NULL;
	RECT _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateEnhMetaFileA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateEnhMetaFileA((HDC)arg0, (LPCSTR)lparg1, lparg2, (LPCSTR)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateEnhMetaFileA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateEnhMetaFileW
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateEnhMetaFileW)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jobject arg2, jcharArray arg3)
{
	jchar *lparg1=NULL;
	RECT _arg2, *lparg2=NULL;
	jchar *lparg3=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateEnhMetaFileW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)CreateEnhMetaFileW((HDC)arg0, (LPCWSTR)lparg1, lparg2, (LPCWSTR)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateEnhMetaFileW_FUNC);
	return rc;
}
#endif

#if (!defined(NO_CreateFontIndirectA__I) && !defined(JNI64)) || (!defined(NO_CreateFontIndirectA__J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectA__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectA__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, CreateFontIndirectA__I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, CreateFontIndirectA__J_FUNC);
#endif
	rc = (jintLong)CreateFontIndirectA((LPLOGFONTA)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, CreateFontIndirectA__I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, CreateFontIndirectA__J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTA _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	if (arg0) if ((lparg0 = getLOGFONTAFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)CreateFontIndirectA(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
	return rc;
}
#endif

#if (!defined(NO_CreateFontIndirectW__I) && !defined(JNI64)) || (!defined(NO_CreateFontIndirectW__J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectW__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectW__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, CreateFontIndirectW__I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, CreateFontIndirectW__J_FUNC);
#endif
	rc = (jintLong)CreateFontIndirectW((LPLOGFONTW)arg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, CreateFontIndirectW__I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, CreateFontIndirectW__J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	LOGFONTW _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	if (arg0) if ((lparg0 = getLOGFONTWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)CreateFontIndirectW(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateIconIndirect
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateIconIndirect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ICONINFO _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateIconIndirect_FUNC);
	if (arg0) if ((lparg0 = getICONINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)CreateIconIndirect(lparg0);
fail:
	OS_NATIVE_EXIT(env, that, CreateIconIndirect_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateMenu
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateMenu)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateMenu_FUNC);
	rc = (jintLong)CreateMenu();
	OS_NATIVE_EXIT(env, that, CreateMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePalette
JNIEXPORT jintLong JNICALL OS_NATIVE(CreatePalette)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePalette_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	rc = (jintLong)CreatePalette((LOGPALETTE *)lparg0);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(CreatePatternBrush)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePatternBrush_FUNC);
	rc = (jintLong)CreatePatternBrush((HBITMAP)arg0);
	OS_NATIVE_EXIT(env, that, CreatePatternBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePen
JNIEXPORT jintLong JNICALL OS_NATIVE(CreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePen_FUNC);
	rc = (jintLong)CreatePen(arg0, arg1, (COLORREF)arg2);
	OS_NATIVE_EXIT(env, that, CreatePen_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePolygonRgn
JNIEXPORT jintLong JNICALL OS_NATIVE(CreatePolygonRgn)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePolygonRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)CreatePolygonRgn((CONST POINT *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CreatePolygonRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePopupMenu
JNIEXPORT jintLong JNICALL OS_NATIVE(CreatePopupMenu)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePopupMenu_FUNC);
	rc = (jintLong)CreatePopupMenu();
	OS_NATIVE_EXIT(env, that, CreatePopupMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateProcessA
JNIEXPORT jboolean JNICALL OS_NATIVE(CreateProcessA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jboolean arg4, jint arg5, jintLong arg6, jintLong arg7, jobject arg8, jobject arg9)
{
	STARTUPINFO _arg8, *lparg8=NULL;
	PROCESS_INFORMATION _arg9, *lparg9=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CreateProcessA_FUNC);
	if (arg8) if ((lparg8 = getSTARTUPINFOFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = getPROCESS_INFORMATIONFields(env, arg9, &_arg9)) == NULL) goto fail;
	rc = (jboolean)CreateProcessA((LPCSTR)arg0, (LPSTR)arg1, (LPSECURITY_ATTRIBUTES)arg2, (LPSECURITY_ATTRIBUTES)arg3, arg4, arg5, (LPVOID)arg6, (LPSTR)arg7, (LPSTARTUPINFOA)lparg8, (LPPROCESS_INFORMATION)lparg9);
fail:
	if (arg9 && lparg9) setPROCESS_INFORMATIONFields(env, arg9, lparg9);
	if (arg8 && lparg8) setSTARTUPINFOFields(env, arg8, lparg8);
	OS_NATIVE_EXIT(env, that, CreateProcessA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateProcessW
JNIEXPORT jboolean JNICALL OS_NATIVE(CreateProcessW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jboolean arg4, jint arg5, jintLong arg6, jintLong arg7, jobject arg8, jobject arg9)
{
	STARTUPINFO _arg8, *lparg8=NULL;
	PROCESS_INFORMATION _arg9, *lparg9=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CreateProcessW_FUNC);
	if (arg8) if ((lparg8 = getSTARTUPINFOFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = getPROCESS_INFORMATIONFields(env, arg9, &_arg9)) == NULL) goto fail;
	rc = (jboolean)CreateProcessW((LPCWSTR)arg0, (LPWSTR)arg1, (LPSECURITY_ATTRIBUTES)arg2, (LPSECURITY_ATTRIBUTES)arg3, arg4, arg5, (LPVOID)arg6, (LPWSTR)arg7, (LPSTARTUPINFOW)lparg8, (LPPROCESS_INFORMATION)lparg9);
fail:
	if (arg9 && lparg9) setPROCESS_INFORMATIONFields(env, arg9, lparg9);
	if (arg8 && lparg8) setSTARTUPINFOFields(env, arg8, lparg8);
	OS_NATIVE_EXIT(env, that, CreateProcessW_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateRectRgn
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateRectRgn_FUNC);
	rc = (jintLong)CreateRectRgn(arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, CreateRectRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateSolidBrush
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateSolidBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateSolidBrush_FUNC);
	rc = (jintLong)CreateSolidBrush((COLORREF)arg0);
	OS_NATIVE_EXIT(env, that, CreateSolidBrush_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateStreamOnHGlobal
JNIEXPORT jint JNICALL OS_NATIVE(CreateStreamOnHGlobal)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateStreamOnHGlobal_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CreateStreamOnHGlobal((HGLOBAL)arg0, (BOOL)arg1, (LPSTREAM *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CreateStreamOnHGlobal_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateWindowExA
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateWindowExA)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10, jobject arg11)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateWindowExA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jintLong)CreateWindowExA(arg0, (LPSTR)lparg1, lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
fail:
	if (arg11 && lparg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateWindowExA_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateWindowExW
JNIEXPORT jintLong JNICALL OS_NATIVE(CreateWindowExW)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintLong arg8, jintLong arg9, jintLong arg10, jobject arg11)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	CREATESTRUCT _arg11, *lparg11=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CreateWindowExW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = getCREATESTRUCTFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jintLong)CreateWindowExW(arg0, (LPWSTR)lparg1, (LPWSTR)lparg2, arg3, arg4, arg5, arg6, arg7, (HWND)arg8, (HMENU)arg9, (HINSTANCE)arg10, lparg11);
fail:
	if (arg11 && lparg11) setCREATESTRUCTFields(env, arg11, lparg11);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateWindowExW_FUNC);
	return rc;
}
#endif

#ifndef NO_DEVMODEA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DEVMODEA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DEVMODEA_1sizeof_FUNC);
	rc = (jint)DEVMODEA_sizeof();
	OS_NATIVE_EXIT(env, that, DEVMODEA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DEVMODEW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DEVMODEW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DEVMODEW_1sizeof_FUNC);
	rc = (jint)DEVMODEW_sizeof();
	OS_NATIVE_EXIT(env, that, DEVMODEW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DIBSECTION_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DIBSECTION_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DIBSECTION_1sizeof_FUNC);
	rc = (jint)DIBSECTION_sizeof();
	OS_NATIVE_EXIT(env, that, DIBSECTION_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DLLVERSIONINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DLLVERSIONINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DLLVERSIONINFO_1sizeof_FUNC);
	rc = (jint)DLLVERSIONINFO_sizeof();
	OS_NATIVE_EXIT(env, that, DLLVERSIONINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DOCHOSTUIINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DOCHOSTUIINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DOCHOSTUIINFO_1sizeof_FUNC);
	rc = (jint)DOCHOSTUIINFO_sizeof();
	OS_NATIVE_EXIT(env, that, DOCHOSTUIINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DOCINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DOCINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DOCINFO_1sizeof_FUNC);
	rc = (jint)DOCINFO_sizeof();
	OS_NATIVE_EXIT(env, that, DOCINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DPtoLP
JNIEXPORT jboolean JNICALL OS_NATIVE(DPtoLP)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DPtoLP_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)DPtoLP((HDC)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DPtoLP_FUNC);
	return rc;
}
#endif

#ifndef NO_DRAWITEMSTRUCT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DRAWITEMSTRUCT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DRAWITEMSTRUCT_1sizeof_FUNC);
	rc = (jint)DRAWITEMSTRUCT_sizeof();
	OS_NATIVE_EXIT(env, that, DRAWITEMSTRUCT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DROPFILES_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DROPFILES_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DROPFILES_1sizeof_FUNC);
	rc = (jint)DROPFILES_sizeof();
	OS_NATIVE_EXIT(env, that, DROPFILES_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DWM_1BLURBEHIND_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(DWM_1BLURBEHIND_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DWM_1BLURBEHIND_1sizeof_FUNC);
	rc = (jint)DWM_BLURBEHIND_sizeof();
	OS_NATIVE_EXIT(env, that, DWM_1BLURBEHIND_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DefFrameProcA
JNIEXPORT jintLong JNICALL OS_NATIVE(DefFrameProcA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefFrameProcA_FUNC);
	rc = (jintLong)DefFrameProcA((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	OS_NATIVE_EXIT(env, that, DefFrameProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefFrameProcW
JNIEXPORT jintLong JNICALL OS_NATIVE(DefFrameProcW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefFrameProcW_FUNC);
	rc = (jintLong)DefFrameProcW((HWND)arg0, (HWND)arg1, arg2, (WPARAM)arg3, (LPARAM)arg4);
	OS_NATIVE_EXIT(env, that, DefFrameProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcA
JNIEXPORT jintLong JNICALL OS_NATIVE(DefMDIChildProcA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefMDIChildProcA_FUNC);
	rc = (jintLong)DefMDIChildProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefMDIChildProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefMDIChildProcW
JNIEXPORT jintLong JNICALL OS_NATIVE(DefMDIChildProcW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefMDIChildProcW_FUNC);
	rc = (jintLong)DefMDIChildProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefMDIChildProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DefWindowProcA
JNIEXPORT jintLong JNICALL OS_NATIVE(DefWindowProcA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefWindowProcA_FUNC);
	rc = (jintLong)DefWindowProcA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefWindowProcA_FUNC);
	return rc;
}
#endif

#ifndef NO_DefWindowProcW
JNIEXPORT jintLong JNICALL OS_NATIVE(DefWindowProcW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DefWindowProcW_FUNC);
	rc = (jintLong)DefWindowProcW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
	OS_NATIVE_EXIT(env, that, DefWindowProcW_FUNC);
	return rc;
}
#endif

#ifndef NO_DeferWindowPos
JNIEXPORT jintLong JNICALL OS_NATIVE(DeferWindowPos)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DeferWindowPos_FUNC);
	rc = (jintLong)DeferWindowPos((HDWP)arg0, (HWND)arg1, (HWND)arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, DeferWindowPos_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteDC
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteDC_FUNC);
	rc = (jboolean)DeleteDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, DeleteDC_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteEnhMetaFile
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteEnhMetaFile)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteEnhMetaFile_FUNC);
	rc = (jboolean)DeleteEnhMetaFile((HENHMETAFILE)arg0);
	OS_NATIVE_EXIT(env, that, DeleteEnhMetaFile_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DestroyWindow_FUNC);
	rc = (jboolean)DestroyWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, DestroyWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatchMessageA
JNIEXPORT jintLong JNICALL OS_NATIVE(DispatchMessageA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DispatchMessageA_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)DispatchMessageA(lparg0);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DispatchMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_DispatchMessageW
JNIEXPORT jintLong JNICALL OS_NATIVE(DispatchMessageW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	MSG _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, DispatchMessageW_FUNC);
	if (arg0) if ((lparg0 = getMSGFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)DispatchMessageW(lparg0);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DispatchMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_DragDetect
JNIEXPORT jboolean JNICALL OS_NATIVE(DragDetect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, DragFinish_FUNC);
	DragFinish((HDROP)arg0);
	OS_NATIVE_EXIT(env, that, DragFinish_FUNC);
}
#endif

#ifndef NO_DragQueryFileA
JNIEXPORT jint JNICALL OS_NATIVE(DragQueryFileA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jcharArray arg2, jint arg3)
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

#ifndef NO_DrawAnimatedRects
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawAnimatedRects)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2, jobject arg3)
{
	RECT _arg2, *lparg2=NULL;
	RECT _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, DrawAnimatedRects_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)DrawAnimatedRects((HWND)arg0, arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, DrawAnimatedRects_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawEdge
JNIEXPORT jboolean JNICALL OS_NATIVE(DrawEdge)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLong arg3, jint arg4, jint arg5, jint arg6, jintLong arg7, jint arg8)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jobject arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jobject arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
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
		LOAD_FUNCTION(fp, DrawThemeBackground)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HTHEME, HDC, jint, jint, const RECT *, const RECT *))fp)((HTHEME)arg0, (HDC)arg1, arg2, arg3, (const RECT *)lparg4, (const RECT *)lparg5);
		}
	}
fail:
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, DrawThemeBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeEdge
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeEdge)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6, jobject arg7)
{
	RECT _arg4, *lparg4=NULL;
	RECT _arg7, *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeEdge_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getRECTFields(env, arg7, &_arg7)) == NULL) goto fail;
/*
	rc = (jint)DrawThemeEdge(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, lparg7);
*/
	{
		LOAD_FUNCTION(fp, DrawThemeEdge)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, RECT *, jint, jint, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, lparg7);
		}
	}
fail:
	if (arg7 && lparg7) setRECTFields(env, arg7, lparg7);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, DrawThemeEdge_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeIcon
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeIcon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jintLong arg5, jint arg6)
{
	RECT _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeIcon_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
/*
	rc = (jint)DrawThemeIcon(arg0, arg1, arg2, arg3, lparg4, arg5, arg6);
*/
	{
		LOAD_FUNCTION(fp, DrawThemeIcon)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, RECT *, jintLong, jint))fp)(arg0, arg1, arg2, arg3, lparg4, arg5, arg6);
		}
	}
fail:
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, DrawThemeIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeParentBackground
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeParentBackground)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeParentBackground_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
/*
	rc = (jint)DrawThemeParentBackground(arg0, arg1, lparg2);
*/
	{
		LOAD_FUNCTION(fp, DrawThemeParentBackground)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, RECT *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, DrawThemeParentBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeText
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeText)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jint arg6, jint arg7, jobject arg8)
{
	jchar *lparg4=NULL;
	RECT _arg8, *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeText_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getRECTFields(env, arg8, &_arg8)) == NULL) goto fail;
/*
	rc = (jint)DrawThemeText(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, arg7, lparg8);
*/
	{
		LOAD_FUNCTION(fp, DrawThemeText)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jchar *, jint, jint, jint, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, arg7, lparg8);
		}
	}
fail:
	if (arg8 && lparg8) setRECTFields(env, arg8, lparg8);
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, DrawThemeText_FUNC);
	return rc;
}
#endif

#ifndef NO_DwmEnableBlurBehindWindow
JNIEXPORT jint JNICALL OS_NATIVE(DwmEnableBlurBehindWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	DWM_BLURBEHIND _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DwmEnableBlurBehindWindow_FUNC);
	if (arg1) if ((lparg1 = getDWM_BLURBEHINDFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jint)DwmEnableBlurBehindWindow((HWND)arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, DwmEnableBlurBehindWindow)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HWND, DWM_BLURBEHIND *))fp)((HWND)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setDWM_BLURBEHINDFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DwmEnableBlurBehindWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_DwmExtendFrameIntoClientArea
JNIEXPORT jint JNICALL OS_NATIVE(DwmExtendFrameIntoClientArea)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	MARGINS _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DwmExtendFrameIntoClientArea_FUNC);
	if (arg1) if ((lparg1 = getMARGINSFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jint)DwmExtendFrameIntoClientArea((HWND)arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, DwmExtendFrameIntoClientArea)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HWND, MARGINS *))fp)((HWND)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setMARGINSFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DwmExtendFrameIntoClientArea_FUNC);
	return rc;
}
#endif

#ifndef NO_EMREXTCREATEFONTINDIRECTW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(EMREXTCREATEFONTINDIRECTW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EMREXTCREATEFONTINDIRECTW_1sizeof_FUNC);
	rc = (jint)EMREXTCREATEFONTINDIRECTW_sizeof();
	OS_NATIVE_EXIT(env, that, EMREXTCREATEFONTINDIRECTW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_EMR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(EMR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EMR_1sizeof_FUNC);
	rc = (jint)EMR_sizeof();
	OS_NATIVE_EXIT(env, that, EMR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_EXTLOGFONTW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(EXTLOGFONTW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EXTLOGFONTW_1sizeof_FUNC);
	rc = (jint)EXTLOGFONTW_sizeof();
	OS_NATIVE_EXIT(env, that, EXTLOGFONTW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_EXTLOGPEN_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(EXTLOGPEN_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EXTLOGPEN_1sizeof_FUNC);
	rc = (jint)EXTLOGPEN_sizeof();
	OS_NATIVE_EXIT(env, that, EXTLOGPEN_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_Ellipse
JNIEXPORT jboolean JNICALL OS_NATIVE(Ellipse)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnableWindow_FUNC);
	rc = (jboolean)EnableWindow((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, EnableWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_EndBufferedPaint
JNIEXPORT jint JNICALL OS_NATIVE(EndBufferedPaint)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EndBufferedPaint_FUNC);
/*
	rc = (jint)EndBufferedPaint((HPAINTBUFFER)arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, EndBufferedPaint)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HPAINTBUFFER, jboolean))fp)((HPAINTBUFFER)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, EndBufferedPaint_FUNC);
	return rc;
}
#endif

#ifndef NO_EndDeferWindowPos
JNIEXPORT jboolean JNICALL OS_NATIVE(EndDeferWindowPos)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jint arg3)
{
	RECT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumDisplayMonitors_FUNC);
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)EnumDisplayMonitors((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
*/
	{
		LOAD_FUNCTION(fp, EnumDisplayMonitors)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HDC, LPCRECT, MONITORENUMPROC, LPARAM))fp)((HDC)arg0, (LPCRECT)lparg1, (MONITORENUMPROC)arg2, (LPARAM)arg3);
		}
	}
fail:
	if (arg1 && lparg1) setRECTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, EnumDisplayMonitors_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumEnhMetaFile
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumEnhMetaFile)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jobject arg4)
{
	RECT _arg4, *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumEnhMetaFile_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jboolean)EnumEnhMetaFile((HDC)arg0, (HENHMETAFILE)arg1, (ENHMFENUMPROC)arg2, (LPVOID)arg3, lparg4);
fail:
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, EnumEnhMetaFile_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumFontFamiliesA
JNIEXPORT jint JNICALL OS_NATIVE(EnumFontFamiliesA)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jintLong arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jintLong arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLanguageGroupsA_FUNC);
/*
	rc = (jboolean)EnumSystemLanguageGroupsA((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
*/
	{
		LOAD_FUNCTION(fp, EnumSystemLanguageGroupsA)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(LANGUAGEGROUP_ENUMPROCA, jint, LONG_PTR))fp)((LANGUAGEGROUP_ENUMPROCA)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, EnumSystemLanguageGroupsA_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLanguageGroupsW
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLanguageGroupsW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EnumSystemLanguageGroupsW_FUNC);
/*
	rc = (jboolean)EnumSystemLanguageGroupsW((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
*/
	{
		LOAD_FUNCTION(fp, EnumSystemLanguageGroupsW)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(LANGUAGEGROUP_ENUMPROCW, jint, LONG_PTR))fp)((LANGUAGEGROUP_ENUMPROCW)arg0, arg1, (LONG_PTR)arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, EnumSystemLanguageGroupsW_FUNC);
	return rc;
}
#endif

#ifndef NO_EnumSystemLocalesA
JNIEXPORT jboolean JNICALL OS_NATIVE(EnumSystemLocalesA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(ExtCreatePen)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jintArray arg4)
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ExtCreatePen_FUNC);
	if (arg2) if ((lparg2 = getLOGBRUSHFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)ExtCreatePen(arg0, arg1, (CONST LOGBRUSH *)lparg2, arg3, (CONST DWORD *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ExtCreatePen_FUNC);
	return rc;
}
#endif

#ifndef NO_ExtCreateRegion
JNIEXPORT jintLong JNICALL OS_NATIVE(ExtCreateRegion)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jintArray arg2)
{
	jfloat *lparg0=NULL;
	jint *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ExtCreateRegion_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)ExtCreateRegion((XFORM *)lparg0, arg1, (CONST RGNDATA *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtCreateRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_ExtTextOutA
JNIEXPORT jboolean JNICALL OS_NATIVE(ExtTextOutA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jbyteArray arg5, jint arg6, jintArray arg7)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jcharArray arg5, jint arg6, jintArray arg7)
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
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jintLongArray arg2, jintLongArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtractIconExA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ExtractIconExA((LPSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtractIconExA_FUNC);
	return rc;
}
#endif

#ifndef NO_ExtractIconExW
JNIEXPORT jint JNICALL OS_NATIVE(ExtractIconExW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jintLongArray arg2, jintLongArray arg3, jint arg4)
{
	jchar *lparg0=NULL;
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ExtractIconExW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ExtractIconExW((LPWSTR)lparg0, arg1, (HICON FAR *)lparg2, (HICON FAR *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ExtractIconExW_FUNC);
	return rc;
}
#endif

#ifndef NO_FILETIME_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(FILETIME_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FILETIME_1sizeof_FUNC);
	rc = (jint)FILETIME_sizeof();
	OS_NATIVE_EXIT(env, that, FILETIME_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_FillPath
JNIEXPORT jboolean JNICALL OS_NATIVE(FillPath)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(FindWindowA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, FindWindowA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)FindWindowA((LPSTR)lparg0, (LPSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FindWindowA_FUNC);
	return rc;
}
#endif

#ifndef NO_FindWindowW
JNIEXPORT jintLong JNICALL OS_NATIVE(FindWindowW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, FindWindowW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)FindWindowW((LPWSTR)lparg0, (LPWSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FindWindowW_FUNC);
	return rc;
}
#endif

#ifndef NO_FormatMessageA
JNIEXPORT jint JNICALL OS_NATIVE(FormatMessageA)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintLongArray arg4, jint arg5, jintLong arg6)
{
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormatMessageA_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)FormatMessageA(arg0, (LPCVOID)arg1, arg2, arg3, (LPSTR)lparg4, arg5, (va_list*)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, FormatMessageA_FUNC);
	return rc;
}
#endif

#ifndef NO_FormatMessageW
JNIEXPORT jint JNICALL OS_NATIVE(FormatMessageW)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintLongArray arg4, jint arg5, jintLong arg6)
{
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FormatMessageW_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)FormatMessageW(arg0, (LPCVOID)arg1, arg2, arg3, (LPWSTR)lparg4, arg5, (va_list*)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, FormatMessageW_FUNC);
	return rc;
}
#endif

#ifndef NO_FreeLibrary
JNIEXPORT jboolean JNICALL OS_NATIVE(FreeLibrary)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FreeLibrary_FUNC);
	rc = (jboolean)FreeLibrary((HMODULE)arg0);
	OS_NATIVE_EXIT(env, that, FreeLibrary_FUNC);
	return rc;
}
#endif

#ifndef NO_GCP_1RESULTS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GCP_1RESULTS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GCP_1RESULTS_1sizeof_FUNC);
	rc = (jint)GCP_RESULTS_sizeof();
	OS_NATIVE_EXIT(env, that, GCP_1RESULTS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GET_1WHEEL_1DELTA_1WPARAM
JNIEXPORT jint JNICALL OS_NATIVE(GET_1WHEEL_1DELTA_1WPARAM)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GET_1WHEEL_1DELTA_1WPARAM_FUNC);
	rc = (jint)GET_WHEEL_DELTA_WPARAM(arg0);
	OS_NATIVE_EXIT(env, that, GET_1WHEEL_1DELTA_1WPARAM_FUNC);
	return rc;
}
#endif

#ifndef NO_GET_1X_1LPARAM
JNIEXPORT jint JNICALL OS_NATIVE(GET_1X_1LPARAM)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GET_1X_1LPARAM_FUNC);
	rc = (jint)GET_X_LPARAM(arg0);
	OS_NATIVE_EXIT(env, that, GET_1X_1LPARAM_FUNC);
	return rc;
}
#endif

#ifndef NO_GET_1Y_1LPARAM
JNIEXPORT jint JNICALL OS_NATIVE(GET_1Y_1LPARAM)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GET_1Y_1LPARAM_FUNC);
	rc = (jint)GET_Y_LPARAM(arg0);
	OS_NATIVE_EXIT(env, that, GET_1Y_1LPARAM_FUNC);
	return rc;
}
#endif

#ifndef NO_GRADIENT_1RECT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GRADIENT_1RECT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GRADIENT_1RECT_1sizeof_FUNC);
	rc = (jint)GRADIENT_RECT_sizeof();
	OS_NATIVE_EXIT(env, that, GRADIENT_1RECT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GUITHREADINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(GUITHREADINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GUITHREADINFO_1sizeof_FUNC);
	rc = (jint)GUITHREADINFO_sizeof();
	OS_NATIVE_EXIT(env, that, GUITHREADINFO_1sizeof_FUNC);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetActiveWindow)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetActiveWindow_FUNC);
	rc = (jintLong)GetActiveWindow();
	OS_NATIVE_EXIT(env, that, GetActiveWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetAsyncKeyState
JNIEXPORT jshort JNICALL OS_NATIVE(GetAsyncKeyState)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetAsyncKeyState_FUNC);
	rc = (jshort)GetAsyncKeyState(arg0);
	OS_NATIVE_EXIT(env, that, GetAsyncKeyState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(GetBkColor)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetBkColor_FUNC);
	rc = (jint)GetBkColor((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetBkColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCapture
JNIEXPORT jintLong JNICALL OS_NATIVE(GetCapture)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetCapture_FUNC);
	rc = (jintLong)GetCapture();
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jobject arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipRgn_FUNC);
	rc = (jint)GetClipRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, GetClipRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClipboardData
JNIEXPORT jintLong JNICALL OS_NATIVE(GetClipboardData)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetClipboardData_FUNC);
	rc = (jintLong)GetClipboardData(arg0);
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	COMBOBOXINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetComboBoxInfo_FUNC);
	if (arg1) if ((lparg1 = getCOMBOBOXINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetComboBoxInfo((HWND)arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, GetComboBoxInfo)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, COMBOBOXINFO *))fp)((HWND)arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setCOMBOBOXINFOFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetComboBoxInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentObject
JNIEXPORT jintLong JNICALL OS_NATIVE(GetCurrentObject)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentObject_FUNC);
	rc = (jintLong)GetCurrentObject((HDC)arg0, arg1);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetCursor)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetCursor_FUNC);
	rc = (jintLong)GetCursor();
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetDC_FUNC);
	rc = (jintLong)GetDC((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetDC_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDCEx
JNIEXPORT jintLong JNICALL OS_NATIVE(GetDCEx)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetDCEx_FUNC);
	rc = (jintLong)GetDCEx((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetDCEx_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDIBColorTable
JNIEXPORT jint JNICALL OS_NATIVE(GetDIBColorTable)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jbyteArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jintLong arg4, jbyteArray arg5, jint arg6)
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

#ifndef NO_GetDateFormatA
JNIEXPORT jint JNICALL OS_NATIVE(GetDateFormatA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
{
	SYSTEMTIME _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDateFormatA_FUNC);
	if (arg2) if ((lparg2 = getSYSTEMTIMEFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetDateFormatA((LCID)arg0, (DWORD)arg1, lparg2, (LPSTR)lparg3, (LPSTR)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setSYSTEMTIMEFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetDateFormatA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDateFormatW
JNIEXPORT jint JNICALL OS_NATIVE(GetDateFormatW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jcharArray arg3, jcharArray arg4, jint arg5)
{
	SYSTEMTIME _arg2, *lparg2=NULL;
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDateFormatW_FUNC);
	if (arg2) if ((lparg2 = getSYSTEMTIMEFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetDateFormatW((LCID)arg0, (DWORD)arg1, lparg2, (LPWSTR)lparg3, (LPWSTR)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setSYSTEMTIMEFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetDateFormatW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDesktopWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(GetDesktopWindow)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetDesktopWindow_FUNC);
	rc = (jintLong)GetDesktopWindow();
	OS_NATIVE_EXIT(env, that, GetDesktopWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDeviceCaps
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceCaps)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetDlgItem)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetDlgItem_FUNC);
	rc = (jintLong)GetDlgItem((HWND)arg0, arg1);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetFocus)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetFocus_FUNC);
	rc = (jintLong)GetFocus();
	OS_NATIVE_EXIT(env, that, GetFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFontLanguageInfo
JNIEXPORT jint JNICALL OS_NATIVE(GetFontLanguageInfo)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFontLanguageInfo_FUNC);
	rc = (jint)GetFontLanguageInfo((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetFontLanguageInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetForegroundWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(GetForegroundWindow)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetForegroundWindow_FUNC);
	rc = (jintLong)GetForegroundWindow();
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

#ifndef NO_GetGraphicsMode
JNIEXPORT jint JNICALL OS_NATIVE(GetGraphicsMode)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetGraphicsMode_FUNC);
	rc = (jint)GetGraphicsMode((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetGraphicsMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetIconInfo)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetKeyboardLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardLayout_FUNC);
	rc = (jintLong)GetKeyboardLayout(arg0);
	OS_NATIVE_EXIT(env, that, GetKeyboardLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_GetKeyboardLayoutList
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardLayoutList)
	(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardLayoutList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetKeyboardLayoutList(arg0, (HKL FAR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetLastActivePopup)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetLastActivePopup_FUNC);
	rc = (jintLong)GetLastActivePopup((HWND)arg0);
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

#ifndef NO_GetLayeredWindowAttributes
JNIEXPORT jboolean JNICALL OS_NATIVE(GetLayeredWindowAttributes)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jbyteArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetLayeredWindowAttributes_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)GetLayeredWindowAttributes((HWND)arg0, lparg1, lparg2, lparg3);
*/
	{
		LOAD_FUNCTION(fp, GetLayeredWindowAttributes)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, jint *, jbyte *, jint *))fp)((HWND)arg0, lparg1, lparg2, lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetLayeredWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLayout
JNIEXPORT jint JNICALL OS_NATIVE(GetLayout)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetLayout_FUNC);
/*
	rc = (jint)GetLayout((HDC)arg0);
*/
	{
		LOAD_FUNCTION(fp, GetLayout)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HDC))fp)((HDC)arg0);
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

#ifndef NO_GetMapMode
JNIEXPORT jint JNICALL OS_NATIVE(GetMapMode)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMapMode_FUNC);
	rc = (jint)GetMapMode((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetMapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenu
JNIEXPORT jintLong JNICALL OS_NATIVE(GetMenu)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenu_FUNC);
	rc = (jintLong)GetMenu((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuBarInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMenuBarInfo)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	MENUBARINFO _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuBarInfo_FUNC);
	if (arg3) if ((lparg3 = getMENUBARINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
/*
	rc = (jboolean)GetMenuBarInfo(arg0, arg1, arg2, lparg3);
*/
	{
		LOAD_FUNCTION(fp, GetMenuBarInfo)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jint, jint, MENUBARINFO *))fp)(arg0, arg1, arg2, lparg3);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuInfo_FUNC);
	if (arg1) if ((lparg1 = getMENUINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMenuInfo((HMENU)arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, GetMenuInfo)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HMENU, MENUINFO *))fp)((HMENU)arg0, lparg1);
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMetaRgn_FUNC);
	rc = (jint)GetMetaRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, GetMetaRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleFileNameA
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleFileNameA)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleFileNameA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetModuleFileNameA((HMODULE)arg0, (LPSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetModuleFileNameA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleFileNameW
JNIEXPORT jint JNICALL OS_NATIVE(GetModuleFileNameW)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleFileNameW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetModuleFileNameW((HMODULE)arg0, (LPWSTR)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetModuleFileNameW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleHandleA
JNIEXPORT jintLong JNICALL OS_NATIVE(GetModuleHandleA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleHandleA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)GetModuleHandleA((LPSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetModuleHandleA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetModuleHandleW
JNIEXPORT jintLong JNICALL OS_NATIVE(GetModuleHandleW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetModuleHandleW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)GetModuleHandleW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetModuleHandleW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMonitorInfoA
JNIEXPORT jboolean JNICALL OS_NATIVE(GetMonitorInfoA)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMonitorInfoA_FUNC);
	if (arg1) if ((lparg1 = getMONITORINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMonitorInfoA((HMONITOR)arg0, (LPMONITORINFO)lparg1);
*/
	{
		LOAD_FUNCTION(fp, GetMonitorInfoA)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HMONITOR, LPMONITORINFO))fp)((HMONITOR)arg0, (LPMONITORINFO)lparg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	MONITORINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetMonitorInfoW_FUNC);
	if (arg1) if ((lparg1 = getMONITORINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)GetMonitorInfoW((HMONITOR)arg0, (LPMONITORINFO)lparg1);
*/
	{
		LOAD_FUNCTION(fp, GetMonitorInfoW)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HMONITOR, LPMONITORINFO))fp)((HMONITOR)arg0, (LPMONITORINFO)lparg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetNearestPaletteIndex_FUNC);
	rc = (jint)GetNearestPaletteIndex((HPALETTE)arg0, (COLORREF)arg1);
	OS_NATIVE_EXIT(env, that, GetNearestPaletteIndex_FUNC);
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__III) && !defined(JNI64)) || (!defined(NO_GetObjectA__JIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__III)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JIJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JIJ_FUNC);
#endif
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, (LPVOID)arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setBITMAPFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setDIBSECTIONFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	EXTLOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setEXTLOGPENFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGFONTA _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGFONTAFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2) && !defined(JNI64)) || (!defined(NO_GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectA((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGPENFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__III) && !defined(JNI64)) || (!defined(NO_GetObjectW__JIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__III)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JIJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JIJ_FUNC);
#endif
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, (LPVOID)arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	BITMAP _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setBITMAPFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	DIBSECTION _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setDIBSECTIONFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	EXTLOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setEXTLOGPENFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGBRUSH _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGBRUSHFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGFONTW _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGFONTWFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2) && !defined(JNI64)) || (!defined(NO_GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
#endif
{
	LOGPEN _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#endif
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jint)GetObjectW((HGDIOBJ)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setLOGPENFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC);
#endif
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

#ifndef NO_GetOutlineTextMetricsA
JNIEXPORT jint JNICALL OS_NATIVE(GetOutlineTextMetricsA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	OUTLINETEXTMETRICA _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetOutlineTextMetricsA_FUNC);
	if (arg2) if ((lparg2 = getOUTLINETEXTMETRICAFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetOutlineTextMetricsA((HDC)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setOUTLINETEXTMETRICAFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetOutlineTextMetricsA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetOutlineTextMetricsW
JNIEXPORT jint JNICALL OS_NATIVE(GetOutlineTextMetricsW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	OUTLINETEXTMETRICW _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetOutlineTextMetricsW_FUNC);
	if (arg2) if ((lparg2 = getOUTLINETEXTMETRICWFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetOutlineTextMetricsW((HDC)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setOUTLINETEXTMETRICWFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetOutlineTextMetricsW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPaletteEntries
JNIEXPORT jint JNICALL OS_NATIVE(GetPaletteEntries)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jbyteArray arg3)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetParent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetParent_FUNC);
	rc = (jintLong)GetParent((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPath
JNIEXPORT jint JNICALL OS_NATIVE(GetPath)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jbyteArray arg2, jint arg3)
{
	jint *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPath_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetPath((HDC)arg0, (LPPOINT)lparg1, (LPBYTE)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetPath_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPixel
JNIEXPORT jint JNICALL OS_NATIVE(GetPixel)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPolyFillMode_FUNC);
	rc = (jint)GetPolyFillMode((HDC)arg0);
	OS_NATIVE_EXIT(env, that, GetPolyFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcAddress
JNIEXPORT jintLong JNICALL OS_NATIVE(GetProcAddress)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetProcAddress_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)GetProcAddress((HMODULE)arg0, (LPCTSTR)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetProcAddress_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcessHeap
JNIEXPORT jintLong JNICALL OS_NATIVE(GetProcessHeap)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetProcessHeap_FUNC);
	rc = (jintLong)GetProcessHeap();
	OS_NATIVE_EXIT(env, that, GetProcessHeap_FUNC);
	return rc;
}
#endif

#ifndef NO_GetProcessHeaps
JNIEXPORT jint JNICALL OS_NATIVE(GetProcessHeaps)
	(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1)
{
	jintLong *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetProcessHeaps_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetProcessHeaps(arg0, (PHANDLE)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetProcessHeaps_FUNC);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetPropA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetPropA_FUNC);
	rc = (jintLong)GetPropA((HWND)arg0, (LPCTSTR)arg1);
	OS_NATIVE_EXIT(env, that, GetPropA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPropW
JNIEXPORT jintLong JNICALL OS_NATIVE(GetPropW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetPropW_FUNC);
	rc = (jintLong)GetPropW((HWND)arg0, (LPCWSTR)arg1);
	OS_NATIVE_EXIT(env, that, GetPropW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetROP2
JNIEXPORT jint JNICALL OS_NATIVE(GetROP2)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#ifndef NO_GetScrollBarInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetScrollBarInfo)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
{
	SCROLLBARINFO _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrollBarInfo_FUNC);
	if (arg2) if ((lparg2 = getSCROLLBARINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)GetScrollBarInfo((HWND)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setSCROLLBARINFOFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetScrollBarInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrollInfo
JNIEXPORT jboolean JNICALL OS_NATIVE(GetScrollInfo)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2)
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

#ifndef NO_GetStartupInfoA
JNIEXPORT void JNICALL OS_NATIVE(GetStartupInfoA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	STARTUPINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, GetStartupInfoA_FUNC);
	if (arg0) if ((lparg0 = getSTARTUPINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	GetStartupInfoA((LPSTARTUPINFOA)lparg0);
fail:
	if (arg0 && lparg0) setSTARTUPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetStartupInfoA_FUNC);
}
#endif

#ifndef NO_GetStartupInfoW
JNIEXPORT void JNICALL OS_NATIVE(GetStartupInfoW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	STARTUPINFO _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, GetStartupInfoW_FUNC);
	if (arg0) if ((lparg0 = getSTARTUPINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	GetStartupInfoW((LPSTARTUPINFOW)lparg0);
fail:
	if (arg0 && lparg0) setSTARTUPINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetStartupInfoW_FUNC);
}
#endif

#ifndef NO_GetStockObject
JNIEXPORT jintLong JNICALL OS_NATIVE(GetStockObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetStockObject_FUNC);
	rc = (jintLong)GetStockObject(arg0);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GetSysColorBrush)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetSysColorBrush_FUNC);
	rc = (jintLong)GetSysColorBrush(arg0);
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
		LOAD_FUNCTION(fp, GetSystemDefaultUILanguage)
		if (fp) {
			rc = (jshort)((jshort (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, GetSystemDefaultUILanguage_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemMenu
JNIEXPORT jintLong JNICALL OS_NATIVE(GetSystemMenu)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetSystemMenu_FUNC);
	rc = (jintLong)GetSystemMenu((HWND)arg0, arg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jbyteArray arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#ifndef NO_GetThemeBackgroundContentRect
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeBackgroundContentRect)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	RECT _arg4, *lparg4=NULL;
	RECT _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeBackgroundContentRect_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
/*
	rc = (jint)GetThemeBackgroundContentRect(arg0, arg1, arg2, arg3, lparg4, lparg5);
*/
	{
		LOAD_FUNCTION(fp, GetThemeBackgroundContentRect)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, RECT *, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4, lparg5);
		}
	}
fail:
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetThemeBackgroundContentRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeBackgroundExtent
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeBackgroundExtent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	RECT _arg4, *lparg4=NULL;
	RECT _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeBackgroundExtent_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
/*
	rc = (jint)GetThemeBackgroundExtent(arg0, arg1, arg2, arg3, lparg4, lparg5);
*/
	{
		LOAD_FUNCTION(fp, GetThemeBackgroundExtent)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, RECT *, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4, lparg5);
		}
	}
fail:
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, GetThemeBackgroundExtent_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeColor
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeColor)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeColor_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	rc = (jint)GetThemeColor(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		LOAD_FUNCTION(fp, GetThemeColor)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint, jint, jint, jint *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetThemeColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeInt
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeInt)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeInt_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	rc = (jint)GetThemeInt(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		LOAD_FUNCTION(fp, GetThemeInt)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint, jint, jint, jint *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetThemeInt_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeMargins
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMargins)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jobject arg6)
{
	RECT _arg5, *lparg5=NULL;
	MARGINS _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeMargins_FUNC);
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getMARGINSFields(env, arg6, &_arg6)) == NULL) goto fail;
/*
	rc = (jint)GetThemeMargins(arg0, arg1, arg2, arg3, arg4, lparg5, lparg6);
*/
	{
		LOAD_FUNCTION(fp, GetThemeMargins)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jint, RECT *, MARGINS *))fp)(arg0, arg1, arg2, arg3, arg4, lparg5, lparg6);
		}
	}
fail:
	if (arg6 && lparg6) setMARGINSFields(env, arg6, lparg6);
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, GetThemeMargins_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeMetric
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMetric)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeMetric_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
/*
	rc = (jint)GetThemeMetric(arg0, arg1, arg2, arg3, arg4, lparg5);
*/
	{
		LOAD_FUNCTION(fp, GetThemeMetric)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jint, jint *))fp)(arg0, arg1, arg2, arg3, arg4, lparg5);
		}
	}
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, GetThemeMetric_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemePartSize
JNIEXPORT jint JNICALL OS_NATIVE(GetThemePartSize)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jobject arg6)
{
	RECT _arg4, *lparg4=NULL;
	SIZE _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemePartSize_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getSIZEFields(env, arg6, &_arg6)) == NULL) goto fail;
/*
	rc = (jint)GetThemePartSize(arg0, arg1, arg2, arg3, lparg4, arg5, lparg6);
*/
	{
		LOAD_FUNCTION(fp, GetThemePartSize)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, RECT *, jint, SIZE *))fp)(arg0, arg1, arg2, arg3, lparg4, arg5, lparg6);
		}
	}
fail:
	if (arg6 && lparg6) setSIZEFields(env, arg6, lparg6);
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetThemePartSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeRect
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeRect)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	RECT _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeRect_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
/*
	rc = (jint)GetThemeRect(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		LOAD_FUNCTION(fp, GetThemeRect)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint, jint, jint, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetThemeRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeSysSize
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeSysSize)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeSysSize_FUNC);
/*
	rc = (jint)GetThemeSysSize(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, GetThemeSysSize)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, GetThemeSysSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeTextExtent
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeTextExtent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jint arg6, jobject arg7, jobject arg8)
{
	jchar *lparg4=NULL;
	RECT _arg7, *lparg7=NULL;
	RECT _arg8, *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeTextExtent_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getRECTFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getRECTFields(env, arg8, &_arg8)) == NULL) goto fail;
/*
	rc = (jint)GetThemeTextExtent(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, lparg7, lparg8);
*/
	{
		LOAD_FUNCTION(fp, GetThemeTextExtent)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jchar *, jint, jint, RECT *, RECT *))fp)(arg0, arg1, arg2, arg3, lparg4, arg5, arg6, lparg7, lparg8);
		}
	}
fail:
	if (arg8 && lparg8) setRECTFields(env, arg8, lparg8);
	if (arg7 && lparg7) setRECTFields(env, arg7, lparg7);
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetThemeTextExtent_FUNC);
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

#ifndef NO_GetTimeFormatA
JNIEXPORT jint JNICALL OS_NATIVE(GetTimeFormatA)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
{
	SYSTEMTIME _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTimeFormatA_FUNC);
	if (arg2) if ((lparg2 = getSYSTEMTIMEFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetTimeFormatA((LCID)arg0, (DWORD)arg1, lparg2, (LPSTR)lparg3, (LPSTR)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setSYSTEMTIMEFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetTimeFormatA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetTimeFormatW
JNIEXPORT jint JNICALL OS_NATIVE(GetTimeFormatW)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jcharArray arg3, jcharArray arg4, jint arg5)
{
	SYSTEMTIME _arg2, *lparg2=NULL;
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTimeFormatW_FUNC);
	if (arg2) if ((lparg2 = getSYSTEMTIMEFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetTimeFormatW((LCID)arg0, (DWORD)arg1, lparg2, (LPWSTR)lparg3, (LPWSTR)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setSYSTEMTIMEFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetTimeFormatW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetUpdateRect
JNIEXPORT jboolean JNICALL OS_NATIVE(GetUpdateRect)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jboolean arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetUpdateRgn_FUNC);
	rc = (jint)GetUpdateRgn((HWND)arg0, (HRGN)arg1, arg2);
	OS_NATIVE_EXIT(env, that, GetUpdateRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOA _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOAFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExA(lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOEXA _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOEXAFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExA((LPOSVERSIONINFOA)lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOEXAFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOEXW _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOEXWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExW((LPOSVERSIONINFOW)lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOEXWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2
JNIEXPORT jboolean JNICALL OS_NATIVE(GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2)
	(JNIEnv *env, jclass that, jobject arg0)
{
	OSVERSIONINFOW _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2_FUNC);
	if (arg0) if ((lparg0 = getOSVERSIONINFOWFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)GetVersionExW(lparg0);
fail:
	if (arg0 && lparg0) setOSVERSIONINFOWFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(GetWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindow_FUNC);
	rc = (jintLong)GetWindow((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowDC
JNIEXPORT jintLong JNICALL OS_NATIVE(GetWindowDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowDC_FUNC);
	rc = (jintLong)GetWindowDC((HWND)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowDC_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowLongA_FUNC);
	rc = (jint)GetWindowLongA((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindowLongA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongPtrA
JNIEXPORT jintLong JNICALL OS_NATIVE(GetWindowLongPtrA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowLongPtrA_FUNC);
	rc = (jintLong)GetWindowLongPtrA((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindowLongPtrA_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongPtrW
JNIEXPORT jintLong JNICALL OS_NATIVE(GetWindowLongPtrW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowLongPtrW_FUNC);
	rc = (jintLong)GetWindowLongPtrW((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, GetWindowLongPtrW_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowLongW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
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

#ifndef NO_GetWindowTheme
JNIEXPORT jintLong JNICALL OS_NATIVE(GetWindowTheme)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowTheme_FUNC);
/*
	rc = (jintLong)GetWindowTheme((HWND)arg0);
*/
	{
		LOAD_FUNCTION(fp, GetWindowTheme)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(HWND))fp)((HWND)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, GetWindowTheme_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowThreadProcessId
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowThreadProcessId)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(GlobalAlloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalAlloc_FUNC);
	rc = (jintLong)GlobalAlloc(arg0, arg1);
	OS_NATIVE_EXIT(env, that, GlobalAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalFree
JNIEXPORT jintLong JNICALL OS_NATIVE(GlobalFree)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalFree_FUNC);
	rc = (jintLong)GlobalFree((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalFree_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalLock
JNIEXPORT jintLong JNICALL OS_NATIVE(GlobalLock)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, GlobalLock_FUNC);
	rc = (jintLong)GlobalLock((HANDLE)arg0);
	OS_NATIVE_EXIT(env, that, GlobalLock_FUNC);
	return rc;
}
#endif

#ifndef NO_GlobalSize
JNIEXPORT jint JNICALL OS_NATIVE(GlobalSize)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jint arg4, jint arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, GradientFill_FUNC);
/*
	rc = (jboolean)GradientFill((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
*/
	{
		LOAD_FUNCTION(fp, GradientFill)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HDC, PTRIVERTEX, ULONG, PVOID, ULONG, ULONG))fp)((HDC)arg0, (PTRIVERTEX)arg1, (ULONG)arg2, (PVOID)arg3, (ULONG)arg4, (ULONG)arg5);
		}
	}
	OS_NATIVE_EXIT(env, that, GradientFill_FUNC);
	return rc;
}
#endif

#ifndef NO_HDHITTESTINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(HDHITTESTINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HDHITTESTINFO_1sizeof_FUNC);
	rc = (jint)HDHITTESTINFO_sizeof();
	OS_NATIVE_EXIT(env, that, HDHITTESTINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_HDITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(HDITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HDITEM_1sizeof_FUNC);
	rc = (jint)HDITEM_sizeof();
	OS_NATIVE_EXIT(env, that, HDITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_HDLAYOUT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(HDLAYOUT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HDLAYOUT_1sizeof_FUNC);
	rc = (jint)HDLAYOUT_sizeof();
	OS_NATIVE_EXIT(env, that, HDLAYOUT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_HELPINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(HELPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HELPINFO_1sizeof_FUNC);
	rc = (jint)HELPINFO_sizeof();
	OS_NATIVE_EXIT(env, that, HELPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_HIGHCONTRAST_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(HIGHCONTRAST_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIGHCONTRAST_1sizeof_FUNC);
	rc = (jint)HIGHCONTRAST_sizeof();
	OS_NATIVE_EXIT(env, that, HIGHCONTRAST_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_HIWORD
JNIEXPORT jint JNICALL OS_NATIVE(HIWORD)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIWORD_FUNC);
	rc = (jint)HIWORD(arg0);
	OS_NATIVE_EXIT(env, that, HIWORD_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapAlloc
JNIEXPORT jintLong JNICALL OS_NATIVE(HeapAlloc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, HeapAlloc_FUNC);
	rc = (jintLong)HeapAlloc((HANDLE)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, HeapAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapFree
JNIEXPORT jboolean JNICALL OS_NATIVE(HeapFree)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HeapFree_FUNC);
	rc = (jboolean)HeapFree((HANDLE)arg0, arg1, (LPVOID)arg2);
	OS_NATIVE_EXIT(env, that, HeapFree_FUNC);
	return rc;
}
#endif

#ifndef NO_HeapValidate
JNIEXPORT jboolean JNICALL OS_NATIVE(HeapValidate)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HeapValidate_FUNC);
	rc = (jboolean)HeapValidate((HANDLE)arg0, arg1, (LPCVOID)arg2);
	OS_NATIVE_EXIT(env, that, HeapValidate_FUNC);
	return rc;
}
#endif

#ifndef NO_HideCaret
JNIEXPORT jboolean JNICALL OS_NATIVE(HideCaret)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HideCaret_FUNC);
	rc = (jboolean)HideCaret((HWND)arg0);
	OS_NATIVE_EXIT(env, that, HideCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_HitTestThemeBackground
JNIEXPORT jint JNICALL OS_NATIVE(HitTestThemeBackground)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jintLong arg6, jobject arg7, jshortArray arg8)
{
	RECT _arg5, *lparg5=NULL;
	POINT _arg7, *lparg7=NULL;
	jshort *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HitTestThemeBackground_FUNC);
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getPOINTFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetShortArrayElements(env, arg8, NULL)) == NULL) goto fail;
/*
	rc = (jint)HitTestThemeBackground(arg0, arg1, arg2, arg3, arg4, lparg5, arg6, *lparg7, lparg8);
*/
	{
		LOAD_FUNCTION(fp, HitTestThemeBackground)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jint, RECT *, jintLong, POINT, jshort *))fp)(arg0, arg1, arg2, arg3, arg4, lparg5, arg6, *lparg7, lparg8);
		}
	}
fail:
	if (arg8 && lparg8) (*env)->ReleaseShortArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) setPOINTFields(env, arg7, lparg7);
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, HitTestThemeBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_ICONINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(ICONINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ICONINFO_1sizeof_FUNC);
	rc = (jint)ICONINFO_sizeof();
	OS_NATIVE_EXIT(env, that, ICONINFO_1sizeof_FUNC);
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

#ifndef NO_INITCOMMONCONTROLSEX_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(INITCOMMONCONTROLSEX_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, INITCOMMONCONTROLSEX_1sizeof_FUNC);
	rc = (jint)INITCOMMONCONTROLSEX_sizeof();
	OS_NATIVE_EXIT(env, that, INITCOMMONCONTROLSEX_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_INPUT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(INPUT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, INPUT_1sizeof_FUNC);
	rc = (jint)INPUT_sizeof();
	OS_NATIVE_EXIT(env, that, INPUT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Add
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1Add)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1AddMasked_FUNC);
	rc = (jint)ImageList_AddMasked((HIMAGELIST)arg0, (HBITMAP)arg1, (COLORREF)arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1AddMasked_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1BeginDrag
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1BeginDrag)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1BeginDrag_FUNC);
	rc = (jboolean)ImageList_BeginDrag((HIMAGELIST)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, ImageList_1BeginDrag_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Create
JNIEXPORT jintLong JNICALL OS_NATIVE(ImageList_1Create)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Create_FUNC);
	rc = (jintLong)ImageList_Create(arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, ImageList_1Create_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Destroy
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Destroy)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Destroy_FUNC);
	rc = (jboolean)ImageList_Destroy((HIMAGELIST)arg0);
	OS_NATIVE_EXIT(env, that, ImageList_1Destroy_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1DragEnter
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1DragEnter)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1DragEnter_FUNC);
	rc = (jboolean)ImageList_DragEnter((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1DragEnter_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1DragLeave
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1DragLeave)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1DragLeave_FUNC);
	rc = (jboolean)ImageList_DragLeave((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ImageList_1DragLeave_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1DragMove
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1DragMove)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1DragMove_FUNC);
	rc = (jboolean)ImageList_DragMove(arg0, arg1);
	OS_NATIVE_EXIT(env, that, ImageList_1DragMove_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1DragShowNolock
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1DragShowNolock)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1DragShowNolock_FUNC);
	rc = (jboolean)ImageList_DragShowNolock((BOOL)arg0);
	OS_NATIVE_EXIT(env, that, ImageList_1DragShowNolock_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1Draw
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1Draw)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1Draw_FUNC);
	rc = (jboolean)ImageList_Draw((HIMAGELIST)arg0, arg1, (HDC)arg2, arg3, arg4, (UINT)arg5);
	OS_NATIVE_EXIT(env, that, ImageList_1Draw_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1EndDrag
JNIEXPORT void JNICALL OS_NATIVE(ImageList_1EndDrag)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, ImageList_1EndDrag_FUNC);
	ImageList_EndDrag();
	OS_NATIVE_EXIT(env, that, ImageList_1EndDrag_FUNC);
}
#endif

#ifndef NO_ImageList_1GetDragImage
JNIEXPORT jintLong JNICALL OS_NATIVE(ImageList_1GetDragImage)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	POINT _arg0, *lparg0=NULL;
	POINT _arg1, *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetDragImage_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jintLong)ImageList_GetDragImage((POINT *)lparg0, (POINT *)lparg1);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ImageList_1GetDragImage_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIcon
JNIEXPORT jintLong JNICALL OS_NATIVE(ImageList_1GetIcon)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetIcon_FUNC);
	rc = (jintLong)ImageList_GetIcon((HIMAGELIST)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1GetIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetIconSize
JNIEXPORT jboolean JNICALL OS_NATIVE(ImageList_1GetIconSize)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1GetIconSize_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)ImageList_GetIconSize((HIMAGELIST)arg0, (int *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ImageList_1GetIconSize_FUNC);
	return rc;
}
#endif

#ifndef NO_ImageList_1GetImageCount
JNIEXPORT jint JNICALL OS_NATIVE(ImageList_1GetImageCount)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImageList_1SetIconSize_FUNC);
	rc = (jboolean)ImageList_SetIconSize((HIMAGELIST)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ImageList_1SetIconSize_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmAssociateContext
JNIEXPORT jintLong JNICALL OS_NATIVE(ImmAssociateContext)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImmAssociateContext_FUNC);
	rc = (jintLong)ImmAssociateContext((HWND)arg0, (HIMC)arg1);
	OS_NATIVE_EXIT(env, that, ImmAssociateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmCreateContext
JNIEXPORT jintLong JNICALL OS_NATIVE(ImmCreateContext)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImmCreateContext_FUNC);
	rc = (jintLong)ImmCreateContext();
	OS_NATIVE_EXIT(env, that, ImmCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmDestroyContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmDestroyContext)
	(JNIEnv *env, jclass that, jintLong arg0)
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
		LOAD_FUNCTION(fp, ImmDisableTextFrameService)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jint))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, ImmDisableTextFrameService_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetCompositionFontA)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#if (!defined(NO_ImmGetCompositionStringA__II_3BI) && !defined(JNI64)) || (!defined(NO_ImmGetCompositionStringA__JI_3BI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA__II_3BI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA__JI_3BI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
#endif
{
	jbyte *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringA__II_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringA__JI_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringA((HIMC)arg0, arg1, (LPSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringA__II_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringA__JI_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_ImmGetCompositionStringA__II_3II) && !defined(JNI64)) || (!defined(NO_ImmGetCompositionStringA__JI_3II) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA__II_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringA__JI_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jint arg3)
#endif
{
	jint *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringA__II_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringA__JI_3II_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringA((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringA__II_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringA__JI_3II_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_ImmGetCompositionStringW__II_3BI) && !defined(JNI64)) || (!defined(NO_ImmGetCompositionStringW__JI_3BI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__II_3BI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__JI_3BI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
#endif
{
	jbyte *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__II_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__JI_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__II_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__JI_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_ImmGetCompositionStringW__II_3CI) && !defined(JNI64)) || (!defined(NO_ImmGetCompositionStringW__JI_3CI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__II_3CI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jcharArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__JI_3CI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jcharArray arg2, jint arg3)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__II_3CI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__JI_3CI_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__II_3CI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__JI_3CI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_ImmGetCompositionStringW__II_3II) && !defined(JNI64)) || (!defined(NO_ImmGetCompositionStringW__JI_3II) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__II_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(ImmGetCompositionStringW__JI_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jint arg3)
#endif
{
	jint *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__II_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, ImmGetCompositionStringW__JI_3II_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ImmGetCompositionStringW((HIMC)arg0, arg1, (LPWSTR)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__II_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, ImmGetCompositionStringW__JI_3II_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_ImmGetContext
JNIEXPORT jintLong JNICALL OS_NATIVE(ImmGetContext)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetContext_FUNC);
	rc = (jintLong)ImmGetContext((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetConversionStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetConversionStatus)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetConversionStatus_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)ImmGetConversionStatus((HIMC)arg0, (LPDWORD)lparg1, (LPDWORD)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ImmGetConversionStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetDefaultIMEWnd
JNIEXPORT jintLong JNICALL OS_NATIVE(ImmGetDefaultIMEWnd)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetDefaultIMEWnd_FUNC);
	rc = (jintLong)ImmGetDefaultIMEWnd((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetDefaultIMEWnd_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmGetOpenStatus
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmGetOpenStatus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmGetOpenStatus_FUNC);
	rc = (jboolean)ImmGetOpenStatus((HIMC)arg0);
	OS_NATIVE_EXIT(env, that, ImmGetOpenStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmNotifyIME
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmNotifyIME)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmNotifyIME_FUNC);
	rc = (jboolean)ImmNotifyIME((HIMC)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, ImmNotifyIME_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmReleaseContext
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmReleaseContext)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmReleaseContext_FUNC);
	rc = (jboolean)ImmReleaseContext((HWND)arg0, (HIMC)arg1);
	OS_NATIVE_EXIT(env, that, ImmReleaseContext_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetCandidateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCandidateWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	CANDIDATEFORM _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetCandidateWindow_FUNC);
	if (arg1) if ((lparg1 = getCANDIDATEFORMFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ImmSetCandidateWindow((HIMC)arg0, lparg1);
fail:
	if (arg1 && lparg1) setCANDIDATEFORMFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ImmSetCandidateWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ImmSetCompositionFontA
JNIEXPORT jboolean JNICALL OS_NATIVE(ImmSetCompositionFontA)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ImmSetOpenStatus_FUNC);
	rc = (jboolean)ImmSetOpenStatus((HIMC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ImmSetOpenStatus_FUNC);
	return rc;
}
#endif

#ifndef NO_InSendMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(InSendMessage)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InSendMessage_FUNC);
	rc = (jboolean)InSendMessage();
	OS_NATIVE_EXIT(env, that, InSendMessage_FUNC);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLong arg3, jbyteArray arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLong arg3, jcharArray arg4)
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

#ifndef NO_InternetGetCookieA
JNIEXPORT jboolean JNICALL OS_NATIVE(InternetGetCookieA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InternetGetCookieA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)InternetGetCookieA((LPCTSTR)lparg0, (LPCTSTR)lparg1, (LPSTR)lparg2, (LPDWORD)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, InternetGetCookieA_FUNC);
	return rc;
}
#endif

#ifndef NO_InternetGetCookieW
JNIEXPORT jboolean JNICALL OS_NATIVE(InternetGetCookieW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jcharArray arg2, jintArray arg3)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InternetGetCookieW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)InternetGetCookieW((LPCWSTR)lparg0, (LPCWSTR)lparg1, (LPWSTR)lparg2, (LPDWORD)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, InternetGetCookieW_FUNC);
	return rc;
}
#endif

#ifndef NO_InternetSetCookieA
JNIEXPORT jboolean JNICALL OS_NATIVE(InternetSetCookieA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InternetSetCookieA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)InternetSetCookieA((LPCTSTR)lparg0, (LPCTSTR)lparg1, (LPCTSTR)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, InternetSetCookieA_FUNC);
	return rc;
}
#endif

#ifndef NO_InternetSetCookieW
JNIEXPORT jboolean JNICALL OS_NATIVE(InternetSetCookieW)
	(JNIEnv *env, jclass that, jcharArray arg0, jcharArray arg1, jcharArray arg2)
{
	jchar *lparg0=NULL;
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InternetSetCookieW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)InternetSetCookieW((LPCWSTR)lparg0, (LPCWSTR)lparg1, (LPCWSTR)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, InternetSetCookieW_FUNC);
	return rc;
}
#endif

#ifndef NO_InternetSetOption
JNIEXPORT jboolean JNICALL OS_NATIVE(InternetSetOption)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, InternetSetOption_FUNC);
	rc = (jboolean)InternetSetOption((HINTERNET)arg0, arg1, (LPVOID)arg2, arg3);
	OS_NATIVE_EXIT(env, that, InternetSetOption_FUNC);
	return rc;
}
#endif

#ifndef NO_IntersectClipRect
JNIEXPORT jint JNICALL OS_NATIVE(IntersectClipRect)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jboolean arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
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
		LOAD_FUNCTION(fp, IsAppThemed)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, IsAppThemed_FUNC);
	return rc;
}
#endif

#ifndef NO_IsBadReadPtr
JNIEXPORT jboolean JNICALL OS_NATIVE(IsBadReadPtr)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsBadReadPtr_FUNC);
	rc = (jboolean)IsBadReadPtr((LPVOID)arg0, (UINT_PTR)arg1);
	OS_NATIVE_EXIT(env, that, IsBadReadPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_IsBadWritePtr
JNIEXPORT jboolean JNICALL OS_NATIVE(IsBadWritePtr)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsBadWritePtr_FUNC);
	rc = (jboolean)IsBadWritePtr((LPVOID)arg0, (UINT_PTR)arg1);
	OS_NATIVE_EXIT(env, that, IsBadWritePtr_FUNC);
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsHungAppWindow_FUNC);
/*
	rc = (jboolean)IsHungAppWindow((HWND)arg0);
*/
	{
		LOAD_FUNCTION(fp, IsHungAppWindow)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND))fp)((HWND)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, IsHungAppWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_IsIconic
JNIEXPORT jboolean JNICALL OS_NATIVE(IsIconic)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsZoomed_FUNC);
	rc = (jboolean)IsZoomed((HWND)arg0);
	OS_NATIVE_EXIT(env, that, IsZoomed_FUNC);
	return rc;
}
#endif

#ifndef NO_KEYBDINPUT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(KEYBDINPUT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KEYBDINPUT_1sizeof_FUNC);
	rc = (jint)KEYBDINPUT_sizeof();
	OS_NATIVE_EXIT(env, that, KEYBDINPUT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_KillTimer
JNIEXPORT jboolean JNICALL OS_NATIVE(KillTimer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, KillTimer_FUNC);
	rc = (jboolean)KillTimer((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, KillTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_LITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LITEM_1sizeof_FUNC);
	rc = (jint)LITEM_sizeof();
	OS_NATIVE_EXIT(env, that, LITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LOGBRUSH_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LOGBRUSH_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LOGBRUSH_1sizeof_FUNC);
	rc = (jint)LOGBRUSH_sizeof();
	OS_NATIVE_EXIT(env, that, LOGBRUSH_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LOGFONTA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LOGFONTA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LOGFONTA_1sizeof_FUNC);
	rc = (jint)LOGFONTA_sizeof();
	OS_NATIVE_EXIT(env, that, LOGFONTA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LOGFONTW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LOGFONTW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LOGFONTW_1sizeof_FUNC);
	rc = (jint)LOGFONTW_sizeof();
	OS_NATIVE_EXIT(env, that, LOGFONTW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LOGPEN_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LOGPEN_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LOGPEN_1sizeof_FUNC);
	rc = (jint)LOGPEN_sizeof();
	OS_NATIVE_EXIT(env, that, LOGPEN_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LOWORD
JNIEXPORT jint JNICALL OS_NATIVE(LOWORD)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LOWORD_FUNC);
	rc = (jint)LOWORD(arg0);
	OS_NATIVE_EXIT(env, that, LOWORD_FUNC);
	return rc;
}
#endif

#ifndef NO_LPtoDP
JNIEXPORT jboolean JNICALL OS_NATIVE(LPtoDP)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
{
	POINT _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, LPtoDP_FUNC);
	if (arg1) if ((lparg1 = getPOINTFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)LPtoDP((HDC)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) setPOINTFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, LPtoDP_FUNC);
	return rc;
}
#endif

#ifndef NO_LVCOLUMN_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LVCOLUMN_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LVCOLUMN_1sizeof_FUNC);
	rc = (jint)LVCOLUMN_sizeof();
	OS_NATIVE_EXIT(env, that, LVCOLUMN_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LVHITTESTINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LVHITTESTINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LVHITTESTINFO_1sizeof_FUNC);
	rc = (jint)LVHITTESTINFO_sizeof();
	OS_NATIVE_EXIT(env, that, LVHITTESTINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LVINSERTMARK_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LVINSERTMARK_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LVINSERTMARK_1sizeof_FUNC);
	rc = (jint)LVINSERTMARK_sizeof();
	OS_NATIVE_EXIT(env, that, LVINSERTMARK_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LVITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(LVITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LVITEM_1sizeof_FUNC);
	rc = (jint)LVITEM_sizeof();
	OS_NATIVE_EXIT(env, that, LVITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LineTo
JNIEXPORT jboolean JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, LineTo_FUNC);
	rc = (jboolean)LineTo((HDC)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, LineTo_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadBitmapA
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadBitmapA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadBitmapA_FUNC);
	rc = (jintLong)LoadBitmapA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadBitmapA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadBitmapW
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadBitmapW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadBitmapW_FUNC);
	rc = (jintLong)LoadBitmapW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadBitmapW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadCursorA
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadCursorA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadCursorA_FUNC);
	rc = (jintLong)LoadCursorA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadCursorA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadCursorW
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadCursorW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadCursorW_FUNC);
	rc = (jintLong)LoadCursorW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadCursorW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadIconA
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadIconA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadIconA_FUNC);
	rc = (jintLong)LoadIconA((HINSTANCE)arg0, (LPSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadIconA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadIconW
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadIconW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadIconW_FUNC);
	rc = (jintLong)LoadIconW((HINSTANCE)arg0, (LPWSTR)arg1);
	OS_NATIVE_EXIT(env, that, LoadIconW_FUNC);
	return rc;
}
#endif

#if (!defined(NO_LoadImageA__IIIIII) && !defined(JNI64)) || (!defined(NO_LoadImageA__JJIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageA__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageA__JJIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, LoadImageA__IIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, LoadImageA__JJIIII_FUNC);
#endif
	rc = (jintLong)LoadImageA((HINSTANCE)arg0, (LPSTR)arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, LoadImageA__IIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, LoadImageA__JJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_LoadImageA__I_3BIIII) && !defined(JNI64)) || (!defined(NO_LoadImageA__J_3BIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageA__I_3BIIII)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageA__J_3BIIII)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, LoadImageA__I_3BIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, LoadImageA__J_3BIIII_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)LoadImageA((HINSTANCE)arg0, (LPSTR)lparg1, arg2, arg3, arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, LoadImageA__I_3BIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, LoadImageA__J_3BIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_LoadImageW__IIIIII) && !defined(JNI64)) || (!defined(NO_LoadImageW__JJIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageW__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageW__JJIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, LoadImageW__IIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, LoadImageW__JJIIII_FUNC);
#endif
	rc = (jintLong)LoadImageW((HINSTANCE)arg0, (LPWSTR)arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, LoadImageW__IIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, LoadImageW__JJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_LoadImageW__I_3CIIII) && !defined(JNI64)) || (!defined(NO_LoadImageW__J_3CIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageW__I_3CIIII)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadImageW__J_3CIIII)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jchar *lparg1=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, LoadImageW__I_3CIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, LoadImageW__J_3CIIII_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)LoadImageW((HINSTANCE)arg0, (LPWSTR)lparg1, arg2, arg3, arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, LoadImageW__I_3CIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, LoadImageW__J_3CIIII_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_LoadLibraryA
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadLibraryA)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadLibraryA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)LoadLibraryA((LPSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LoadLibraryA_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadLibraryW
JNIEXPORT jintLong JNICALL OS_NATIVE(LoadLibraryW)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LoadLibraryW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)LoadLibraryW((LPWSTR)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LoadLibraryW_FUNC);
	return rc;
}
#endif

#ifndef NO_LoadStringA
JNIEXPORT jint JNICALL OS_NATIVE(LoadStringA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jcharArray arg2, jint arg3)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(LocalFree)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, LocalFree_FUNC);
	rc = (jintLong)LocalFree((HLOCAL)arg0);
	OS_NATIVE_EXIT(env, that, LocalFree_FUNC);
	return rc;
}
#endif

#ifndef NO_LockWindowUpdate
JNIEXPORT jboolean JNICALL OS_NATIVE(LockWindowUpdate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, LockWindowUpdate_FUNC);
	rc = (jboolean)LockWindowUpdate((HWND)arg0);
	OS_NATIVE_EXIT(env, that, LockWindowUpdate_FUNC);
	return rc;
}
#endif

#ifndef NO_MAKELPARAM
JNIEXPORT jintLong JNICALL OS_NATIVE(MAKELPARAM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, MAKELPARAM_FUNC);
	rc = (jintLong)MAKELPARAM(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MAKELPARAM_FUNC);
	return rc;
}
#endif

#ifndef NO_MAKELRESULT
JNIEXPORT jintLong JNICALL OS_NATIVE(MAKELRESULT)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, MAKELRESULT_FUNC);
	rc = (jintLong)MAKELRESULT(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MAKELRESULT_FUNC);
	return rc;
}
#endif

#ifndef NO_MAKEWORD
JNIEXPORT jint JNICALL OS_NATIVE(MAKEWORD)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MAKEWORD_FUNC);
	rc = (jint)MAKEWORD(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MAKEWORD_FUNC);
	return rc;
}
#endif

#ifndef NO_MAKEWPARAM
JNIEXPORT jintLong JNICALL OS_NATIVE(MAKEWPARAM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, MAKEWPARAM_FUNC);
	rc = (jintLong)MAKEWPARAM(arg0, arg1);
	OS_NATIVE_EXIT(env, that, MAKEWPARAM_FUNC);
	return rc;
}
#endif

#ifndef NO_MARGINS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MARGINS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MARGINS_1sizeof_FUNC);
	rc = (jint)MARGINS_sizeof();
	OS_NATIVE_EXIT(env, that, MARGINS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MCHITTESTINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MCHITTESTINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MCHITTESTINFO_1sizeof_FUNC);
	rc = (jint)MCHITTESTINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MCHITTESTINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MCIWndRegisterClass
JNIEXPORT jboolean JNICALL OS_NATIVE(MCIWndRegisterClass)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, MCIWndRegisterClass_FUNC);
/*
	rc = (jboolean)MCIWndRegisterClass();
*/
	{
		LOAD_FUNCTION(fp, MCIWndRegisterClass)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, MCIWndRegisterClass_FUNC);
	return rc;
}
#endif

#ifndef NO_MEASUREITEMSTRUCT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MEASUREITEMSTRUCT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MEASUREITEMSTRUCT_1sizeof_FUNC);
	rc = (jint)MEASUREITEMSTRUCT_sizeof();
	OS_NATIVE_EXIT(env, that, MEASUREITEMSTRUCT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MENUBARINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MENUBARINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MENUBARINFO_1sizeof_FUNC);
	rc = (jint)MENUBARINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MENUBARINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MENUINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MENUINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MENUINFO_1sizeof_FUNC);
	rc = (jint)MENUINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MENUINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MENUITEMINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MENUITEMINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MENUITEMINFO_1sizeof_FUNC);
	rc = (jint)MENUITEMINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MENUITEMINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MINMAXINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MINMAXINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MINMAXINFO_1sizeof_FUNC);
	rc = (jint)MINMAXINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MINMAXINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MONITORINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MONITORINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MONITORINFO_1sizeof_FUNC);
	rc = (jint)MONITORINFO_sizeof();
	OS_NATIVE_EXIT(env, that, MONITORINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MOUSEINPUT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MOUSEINPUT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MOUSEINPUT_1sizeof_FUNC);
	rc = (jint)MOUSEINPUT_sizeof();
	OS_NATIVE_EXIT(env, that, MOUSEINPUT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_MSG_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(MSG_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MSG_1sizeof_FUNC);
	rc = (jint)MSG_sizeof();
	OS_NATIVE_EXIT(env, that, MSG_1sizeof_FUNC);
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

#if (!defined(NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I) && !defined(JNI64)) || (!defined(NO_MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3)
#endif
{
	POINT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = getPOINTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
fail:
	if (arg2 && lparg2) setPOINTFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I) && !defined(JNI64)) || (!defined(NO_MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jint arg3)
#endif
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)MapWindowPoints((HWND)arg0, (HWND)arg1, (LPPOINT)lparg2, arg3);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#endif
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jcharArray arg2, jint arg3)
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

#ifndef NO_ModifyWorldTransform
JNIEXPORT jboolean JNICALL OS_NATIVE(ModifyWorldTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ModifyWorldTransform_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)ModifyWorldTransform((HDC)arg0, (XFORM *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ModifyWorldTransform_FUNC);
	return rc;
}
#endif

#ifndef NO_MonitorFromWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(MonitorFromWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, MonitorFromWindow_FUNC);
/*
	rc = (jintLong)MonitorFromWindow(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, MonitorFromWindow)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, MonitorFromWindow_FUNC);
	return rc;
}
#endif

#if (!defined(NO_MoveMemory__III) && !defined(JNI64)) || (!defined(NO_MoveMemory__JJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
#endif
{
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JJI_FUNC);
#endif
	MoveMemory((PVOID)arg0, (CONST VOID *)arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	DEVMODEA _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getDEVMODEAFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	DEVMODEW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getDEVMODEWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	DOCHOSTUIINFO _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getDOCHOSTUIINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	DROPFILES _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getDROPFILESFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	GRADIENT_RECT _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getGRADIENT_RECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	KEYBDINPUT _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getKEYBDINPUTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	LOGFONTA _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getLOGFONTAFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	LOGFONTW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getLOGFONTWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	MEASUREITEMSTRUCT _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getMEASUREITEMSTRUCTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	MINMAXINFO _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getMINMAXINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	MOUSEINPUT _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getMOUSEINPUTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	MSG _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getMSGFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMLVCUSTOMDRAW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMLVCUSTOMDRAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMLVDISPINFO _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMLVDISPINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMTTCUSTOMDRAW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMTTCUSTOMDRAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMTTDISPINFOA _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMTTDISPINFOAFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMTTDISPINFOW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMTTDISPINFOWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMTVCUSTOMDRAW _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMTVCUSTOMDRAWFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	NMTVDISPINFO _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getNMTVDISPINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	OPENFILENAME _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getOPENFILENAMEFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setOPENFILENAMEFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	RECT _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getRECTFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	SAFEARRAY _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getSAFEARRAYFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	SHDRAGIMAGE _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getSHDRAGIMAGEFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	TRIVERTEX _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getTRIVERTEXFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	UDACCEL _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getUDACCELFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	WINDOWPOS _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getWINDOWPOSFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3BI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3BI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3BI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3BI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3CI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3CI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3CI)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3CI)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
#endif
{
	jchar *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3CI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3CI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3CI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3CI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3DI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3DI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3DI)(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3DI)(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jint arg2)
#endif
{
	jdouble *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3DI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3DI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3DI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3DI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3FI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3FI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3FI)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3FI)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
#endif
{
	jfloat *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3FI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3FI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3FI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3FI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3II) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3II) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3II)(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3II)(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
#endif
{
	jint *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3II_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3II_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3JI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3JI)(JNIEnv *env, jclass that, jintLong arg0, jlongArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3JI)(JNIEnv *env, jclass that, jintLong arg0, jlongArray arg1, jint arg2)
#endif
{
	jlong *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3JI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3JI_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseLongArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3JI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__I_3SI) && !defined(JNI64)) || (!defined(NO_MoveMemory__J_3SI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__I_3SI)(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__J_3SI)(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1, jint arg2)
#endif
{
	jshort *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__I_3SI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__J_3SI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__I_3SI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__J_3SI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	BITMAPINFOHEADER _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setBITMAPINFOHEADERFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI_FUNC);
#endif
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

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	DEVMODEA _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDEVMODEAFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	DEVMODEW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDEVMODEWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	DOCHOSTUIINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDOCHOSTUIINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	DRAWITEMSTRUCT _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDRAWITEMSTRUCTFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	EMREXTCREATEFONTINDIRECTW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setEMREXTCREATEFONTINDIRECTWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	EMR _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setEMRFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	EXTLOGPEN _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setEXTLOGPENFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	HDITEM _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setHDITEMFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	HELPINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setHELPINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	LOGFONTA _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setLOGFONTAFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	LOGFONTW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setLOGFONTWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	MEASUREITEMSTRUCT _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMEASUREITEMSTRUCTFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	MINMAXINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMINMAXINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	MSG _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setMSGFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMCUSTOMDRAW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMCUSTOMDRAWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMHDR _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMHDRFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMHEADER _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMHEADERFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLINK _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLINKFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLISTVIEW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLISTVIEWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLVCUSTOMDRAW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVCUSTOMDRAWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLVDISPINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVDISPINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLVFINDITEM _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVFINDITEMFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMLVODSTATECHANGE _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMLVODSTATECHANGEFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMREBARCHEVRON _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMREBARCHEVRONFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMREBARCHILDSIZE _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMREBARCHILDSIZEFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMRGINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMRGINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTBHOTITEM _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTBHOTITEMFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTOOLBAR _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTOOLBARFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTREEVIEW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTREEVIEWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTTCUSTOMDRAW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTTCUSTOMDRAWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTTDISPINFOA _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTTDISPINFOAFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTTDISPINFOW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTTDISPINFOWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTVCUSTOMDRAW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTVCUSTOMDRAWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTVDISPINFO _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTVDISPINFOFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMTVITEMCHANGE _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMTVITEMCHANGEFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	NMUPDOWN _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNMUPDOWNFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	OFNOTIFY _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getOFNOTIFYFields(env, arg0, &_arg0)) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setOFNOTIFYFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	OPENFILENAME _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getOPENFILENAMEFields(env, arg0, &_arg0)) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setOPENFILENAMEFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	POINT _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI_FUNC);
#endif
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI)
	(JNIEnv *env, jclass that, jobject arg0, jlongArray arg1, jint arg2)
{
	POINT _arg0, *lparg0=NULL;
	jlong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)lparg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseLongArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI_FUNC);
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II)(JNIEnv *env, jclass that, jobject arg0, jintLongArray arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI)(JNIEnv *env, jclass that, jobject arg0, jintLongArray arg1, jint arg2)
#endif
{
	RECT _arg0, *lparg0=NULL;
	jintLong *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getRECTFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	MoveMemory(lparg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	SCRIPT_ITEM _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_ITEMFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	SCRIPT_LOGATTR _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_LOGATTRFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	SCRIPT_PROPERTIES _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSCRIPT_PROPERTIESFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	SHDRAGIMAGE _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSHDRAGIMAGEFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	TEXTMETRICA _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTEXTMETRICAFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	TEXTMETRICW _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTEXTMETRICWFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	TVITEM _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTVITEMFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	UDACCEL _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setUDACCELFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	WINDOWPOS _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setWINDOWPOSFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3BII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3BJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BII)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3BJI)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jint arg2)
#endif
{
	jbyte *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3BII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3BJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3BII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3BJI_FUNC);
#endif
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

#if (!defined(NO_MoveMemory___3CII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3CJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3CII)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3CJI)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jint arg2)
#endif
{
	jchar *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3CII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3CJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3CII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3CJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3DII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3DJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3DII)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3DJI)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jint arg2)
#endif
{
	jdouble *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3DII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3DJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3DII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3DJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3FII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3FJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3FII)(JNIEnv *env, jclass that, jfloatArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3FJI)(JNIEnv *env, jclass that, jfloatArray arg0, jintLong arg1, jint arg2)
#endif
{
	jfloat *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3FII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3FJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3FII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3FJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3III) && !defined(JNI64)) || (!defined(NO_MoveMemory___3IJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3III)(JNIEnv *env, jclass that, jintArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3IJI)(JNIEnv *env, jclass that, jintArray arg0, jintLong arg1, jint arg2)
#endif
{
	jint *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3IJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3IJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3JII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3JJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3JII)(JNIEnv *env, jclass that, jlongArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3JJI)(JNIEnv *env, jclass that, jlongArray arg0, jintLong arg1, jint arg2)
#endif
{
	jlong *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3JII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3JJI_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseLongArrayElements(env, arg0, lparg0, 0);
	}
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3JII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3JJI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory___3SII) && !defined(JNI64)) || (!defined(NO_MoveMemory___3SJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3SII)(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(MoveMemory___3SJI)(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1, jint arg2)
#endif
{
	jshort *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MoveMemory___3SII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MoveMemory___3SJI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MoveMemory___3SII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MoveMemory___3SJI_FUNC);
#endif
}
#endif

#ifndef NO_MoveToEx
JNIEXPORT jboolean JNICALL OS_NATIVE(MoveToEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MsgWaitForMultipleObjectsEx_FUNC);
	rc = (jint)MsgWaitForMultipleObjectsEx((DWORD)arg0, (LPHANDLE)arg1, (DWORD)arg2, (DWORD)arg3, (DWORD)arg4);
	OS_NATIVE_EXIT(env, that, MsgWaitForMultipleObjectsEx_FUNC);
	return rc;
}
#endif

#if (!defined(NO_MultiByteToWideChar__IIII_3CI) && !defined(JNI64)) || (!defined(NO_MultiByteToWideChar__IIJI_3CI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__IIII_3CI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintLong arg2, jint arg3, jcharArray arg4, jint arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(MultiByteToWideChar__IIJI_3CI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintLong arg2, jint arg3, jcharArray arg4, jint arg5)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, MultiByteToWideChar__IIII_3CI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, MultiByteToWideChar__IIJI_3CI_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, MultiByteToWideChar__IIII_3CI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, MultiByteToWideChar__IIJI_3CI_FUNC);
#endif
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

#ifndef NO_NMCUSTOMDRAW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMCUSTOMDRAW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMCUSTOMDRAW_1sizeof_FUNC);
	rc = (jint)NMCUSTOMDRAW_sizeof();
	OS_NATIVE_EXIT(env, that, NMCUSTOMDRAW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMHDR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMHDR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMHDR_1sizeof_FUNC);
	rc = (jint)NMHDR_sizeof();
	OS_NATIVE_EXIT(env, that, NMHDR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMHEADER_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMHEADER_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMHEADER_1sizeof_FUNC);
	rc = (jint)NMHEADER_sizeof();
	OS_NATIVE_EXIT(env, that, NMHEADER_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLINK_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLINK_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLINK_1sizeof_FUNC);
	rc = (jint)NMLINK_sizeof();
	OS_NATIVE_EXIT(env, that, NMLINK_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLISTVIEW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLISTVIEW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLISTVIEW_1sizeof_FUNC);
	rc = (jint)NMLISTVIEW_sizeof();
	OS_NATIVE_EXIT(env, that, NMLISTVIEW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLVCUSTOMDRAW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLVCUSTOMDRAW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLVCUSTOMDRAW_1sizeof_FUNC);
	rc = (jint)NMLVCUSTOMDRAW_sizeof();
	OS_NATIVE_EXIT(env, that, NMLVCUSTOMDRAW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLVDISPINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLVDISPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLVDISPINFO_1sizeof_FUNC);
	rc = (jint)NMLVDISPINFO_sizeof();
	OS_NATIVE_EXIT(env, that, NMLVDISPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLVFINDITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLVFINDITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLVFINDITEM_1sizeof_FUNC);
	rc = (jint)NMLVFINDITEM_sizeof();
	OS_NATIVE_EXIT(env, that, NMLVFINDITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMLVODSTATECHANGE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMLVODSTATECHANGE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMLVODSTATECHANGE_1sizeof_FUNC);
	rc = (jint)NMLVODSTATECHANGE_sizeof();
	OS_NATIVE_EXIT(env, that, NMLVODSTATECHANGE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMREBARCHEVRON_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMREBARCHEVRON_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMREBARCHEVRON_1sizeof_FUNC);
	rc = (jint)NMREBARCHEVRON_sizeof();
	OS_NATIVE_EXIT(env, that, NMREBARCHEVRON_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMREBARCHILDSIZE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMREBARCHILDSIZE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMREBARCHILDSIZE_1sizeof_FUNC);
	rc = (jint)NMREBARCHILDSIZE_sizeof();
	OS_NATIVE_EXIT(env, that, NMREBARCHILDSIZE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMRGINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMRGINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMRGINFO_1sizeof_FUNC);
	rc = (jint)NMRGINFO_sizeof();
	OS_NATIVE_EXIT(env, that, NMRGINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTBHOTITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTBHOTITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTBHOTITEM_1sizeof_FUNC);
	rc = (jint)NMTBHOTITEM_sizeof();
	OS_NATIVE_EXIT(env, that, NMTBHOTITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTOOLBAR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTOOLBAR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTOOLBAR_1sizeof_FUNC);
	rc = (jint)NMTOOLBAR_sizeof();
	OS_NATIVE_EXIT(env, that, NMTOOLBAR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTREEVIEW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTREEVIEW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTREEVIEW_1sizeof_FUNC);
	rc = (jint)NMTREEVIEW_sizeof();
	OS_NATIVE_EXIT(env, that, NMTREEVIEW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTTCUSTOMDRAW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTTCUSTOMDRAW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTTCUSTOMDRAW_1sizeof_FUNC);
	rc = (jint)NMTTCUSTOMDRAW_sizeof();
	OS_NATIVE_EXIT(env, that, NMTTCUSTOMDRAW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTTDISPINFOA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTTDISPINFOA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTTDISPINFOA_1sizeof_FUNC);
	rc = (jint)NMTTDISPINFOA_sizeof();
	OS_NATIVE_EXIT(env, that, NMTTDISPINFOA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTTDISPINFOW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTTDISPINFOW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTTDISPINFOW_1sizeof_FUNC);
	rc = (jint)NMTTDISPINFOW_sizeof();
	OS_NATIVE_EXIT(env, that, NMTTDISPINFOW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTVCUSTOMDRAW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTVCUSTOMDRAW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTVCUSTOMDRAW_1sizeof_FUNC);
	rc = (jint)NMTVCUSTOMDRAW_sizeof();
	OS_NATIVE_EXIT(env, that, NMTVCUSTOMDRAW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTVDISPINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTVDISPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTVDISPINFO_1sizeof_FUNC);
	rc = (jint)NMTVDISPINFO_sizeof();
	OS_NATIVE_EXIT(env, that, NMTVDISPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMTVITEMCHANGE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMTVITEMCHANGE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMTVITEMCHANGE_1sizeof_FUNC);
	rc = (jint)NMTVITEMCHANGE_sizeof();
	OS_NATIVE_EXIT(env, that, NMTVITEMCHANGE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NMUPDOWN_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NMUPDOWN_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NMUPDOWN_1sizeof_FUNC);
	rc = (jint)NMUPDOWN_sizeof();
	OS_NATIVE_EXIT(env, that, NMUPDOWN_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NONCLIENTMETRICSA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NONCLIENTMETRICSA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NONCLIENTMETRICSA_1sizeof_FUNC);
	rc = (jint)NONCLIENTMETRICSA_sizeof();
	OS_NATIVE_EXIT(env, that, NONCLIENTMETRICSA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NONCLIENTMETRICSW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NONCLIENTMETRICSW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NONCLIENTMETRICSW_1sizeof_FUNC);
	rc = (jint)NONCLIENTMETRICSW_sizeof();
	OS_NATIVE_EXIT(env, that, NONCLIENTMETRICSW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NOTIFYICONDATAA_1V2_1SIZE
JNIEXPORT jint JNICALL OS_NATIVE(NOTIFYICONDATAA_1V2_1SIZE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NOTIFYICONDATAA_1V2_1SIZE_FUNC);
	rc = (jint)NOTIFYICONDATAA_V2_SIZE;
	OS_NATIVE_EXIT(env, that, NOTIFYICONDATAA_1V2_1SIZE_FUNC);
	return rc;
}
#endif

#ifndef NO_NOTIFYICONDATAW_1V2_1SIZE
JNIEXPORT jint JNICALL OS_NATIVE(NOTIFYICONDATAW_1V2_1SIZE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NOTIFYICONDATAW_1V2_1SIZE_FUNC);
	rc = (jint)NOTIFYICONDATAW_V2_SIZE;
	OS_NATIVE_EXIT(env, that, NOTIFYICONDATAW_1V2_1SIZE_FUNC);
	return rc;
}
#endif

#ifndef NO_NotifyWinEvent
JNIEXPORT void JNICALL OS_NATIVE(NotifyWinEvent)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, NotifyWinEvent_FUNC);
/*
	NotifyWinEvent((DWORD)arg0, (HWND)arg1, (LONG)arg2, (LONG)arg3);
*/
	{
		LOAD_FUNCTION(fp, NotifyWinEvent)
		if (fp) {
			((void (CALLING_CONVENTION*)(DWORD, HWND, LONG, LONG))fp)((DWORD)arg0, (HWND)arg1, (LONG)arg2, (LONG)arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, NotifyWinEvent_FUNC);
}
#endif

#ifndef NO_OFNOTIFY_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OFNOTIFY_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OFNOTIFY_1sizeof_FUNC);
	rc = (jint)OFNOTIFY_sizeof();
	OS_NATIVE_EXIT(env, that, OFNOTIFY_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OPENFILENAME_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OPENFILENAME_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OPENFILENAME_1sizeof_FUNC);
	rc = (jint)OPENFILENAME_sizeof();
	OS_NATIVE_EXIT(env, that, OPENFILENAME_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OSVERSIONINFOA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OSVERSIONINFOA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OSVERSIONINFOA_1sizeof_FUNC);
	rc = (jint)OSVERSIONINFOA_sizeof();
	OS_NATIVE_EXIT(env, that, OSVERSIONINFOA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OSVERSIONINFOEXA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OSVERSIONINFOEXA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OSVERSIONINFOEXA_1sizeof_FUNC);
	rc = (jint)OSVERSIONINFOEXA_sizeof();
	OS_NATIVE_EXIT(env, that, OSVERSIONINFOEXA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OSVERSIONINFOEXW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OSVERSIONINFOEXW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OSVERSIONINFOEXW_1sizeof_FUNC);
	rc = (jint)OSVERSIONINFOEXW_sizeof();
	OS_NATIVE_EXIT(env, that, OSVERSIONINFOEXW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OSVERSIONINFOW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OSVERSIONINFOW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OSVERSIONINFOW_1sizeof_FUNC);
	rc = (jint)OSVERSIONINFOW_sizeof();
	OS_NATIVE_EXIT(env, that, OSVERSIONINFOW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OUTLINETEXTMETRICA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OUTLINETEXTMETRICA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OUTLINETEXTMETRICA_1sizeof_FUNC);
	rc = (jint)OUTLINETEXTMETRICA_sizeof();
	OS_NATIVE_EXIT(env, that, OUTLINETEXTMETRICA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OUTLINETEXTMETRICW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(OUTLINETEXTMETRICW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OUTLINETEXTMETRICW_1sizeof_FUNC);
	rc = (jint)OUTLINETEXTMETRICW_sizeof();
	OS_NATIVE_EXIT(env, that, OUTLINETEXTMETRICW_1sizeof_FUNC);
	return rc;
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, OpenClipboard_FUNC);
	rc = (jboolean)OpenClipboard((HWND)arg0);
	OS_NATIVE_EXIT(env, that, OpenClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OpenThemeData
JNIEXPORT jintLong JNICALL OS_NATIVE(OpenThemeData)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1)
{
	jchar *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, OpenThemeData_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)OpenThemeData((HWND)arg0, (LPCWSTR)lparg1);
*/
	{
		LOAD_FUNCTION(fp, OpenThemeData)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(HWND, LPCWSTR))fp)((HWND)arg0, (LPCWSTR)lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, OpenThemeData_FUNC);
	return rc;
}
#endif

#ifndef NO_PAINTSTRUCT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PAINTSTRUCT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PAINTSTRUCT_1sizeof_FUNC);
	rc = (jint)PAINTSTRUCT_sizeof();
	OS_NATIVE_EXIT(env, that, PAINTSTRUCT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PANOSE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PANOSE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PANOSE_1sizeof_FUNC);
	rc = (jint)PANOSE_sizeof();
	OS_NATIVE_EXIT(env, that, PANOSE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_POINTSTOPOINT
JNIEXPORT void JNICALL OS_NATIVE(POINTSTOPOINT)
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
{
	POINT _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, POINTSTOPOINT_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	POINTSTOPOINT(*lparg0, arg1);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, POINTSTOPOINT_FUNC);
}
#endif

#ifndef NO_POINT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(POINT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, POINT_1sizeof_FUNC);
	rc = (jint)POINT_sizeof();
	OS_NATIVE_EXIT(env, that, POINT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PRIMARYLANGID
JNIEXPORT jshort JNICALL OS_NATIVE(PRIMARYLANGID)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, PRIMARYLANGID_FUNC);
	rc = (jshort)PRIMARYLANGID(arg0);
	OS_NATIVE_EXIT(env, that, PRIMARYLANGID_FUNC);
	return rc;
}
#endif

#ifndef NO_PRINTDLG_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PRINTDLG_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PRINTDLG_1sizeof_FUNC);
	rc = (jint)PRINTDLG_sizeof();
	OS_NATIVE_EXIT(env, that, PRINTDLG_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PROCESS_1INFORMATION_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(PROCESS_1INFORMATION_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PROCESS_1INFORMATION_1sizeof_FUNC);
	rc = (jint)PROCESS_INFORMATION_sizeof();
	OS_NATIVE_EXIT(env, that, PROCESS_1INFORMATION_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_PatBlt
JNIEXPORT jboolean JNICALL OS_NATIVE(PatBlt)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
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
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintLong arg2, jintLong arg3)
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

#ifndef NO_PrintWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(PrintWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PrintWindow_FUNC);
/*
	rc = (jboolean)PrintWindow((HWND)arg0, (HDC)arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, PrintWindow)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, HDC, jint))fp)((HWND)arg0, (HDC)arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, PrintWindow_FUNC);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRegion_FUNC);
	rc = (jboolean)PtInRegion((HRGN)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, PtInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_REBARBANDINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(REBARBANDINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, REBARBANDINFO_1sizeof_FUNC);
	rc = (jint)REBARBANDINFO_sizeof();
	OS_NATIVE_EXIT(env, that, REBARBANDINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_RECT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(RECT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RECT_1sizeof_FUNC);
	rc = (jint)RECT_sizeof();
	OS_NATIVE_EXIT(env, that, RECT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_RealizePalette
JNIEXPORT jint JNICALL OS_NATIVE(RealizePalette)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4, jbyteArray arg5, jintArray arg6, jobject arg7)
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
	rc = (jint)RegEnumKeyExA((HKEY)arg0, arg1, (LPSTR)lparg2, (LPDWORD)lparg3, (LPDWORD)lparg4, (LPSTR)lparg5, (LPDWORD)lparg6, lparg7);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jcharArray arg2, jintArray arg3, jintArray arg4, jcharArray arg5, jintArray arg6, jobject arg7)
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
	rc = (jint)RegEnumKeyExW((HKEY)arg0, arg1, (LPWSTR)lparg2, (LPDWORD)lparg3, (LPDWORD)lparg4, (LPWSTR)lparg5, (LPDWORD)lparg6, lparg7);
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jint arg2, jint arg3, jintLongArray arg4)
{
	jbyte *lparg1=NULL;
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegOpenKeyExA_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)RegOpenKeyExA((HKEY)arg0, (LPSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegOpenKeyExA_FUNC);
	return rc;
}
#endif

#ifndef NO_RegOpenKeyExW
JNIEXPORT jint JNICALL OS_NATIVE(RegOpenKeyExW)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jint arg3, jintLongArray arg4)
{
	jchar *lparg1=NULL;
	jintLong *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegOpenKeyExW_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)RegOpenKeyExW((HKEY)arg0, (LPWSTR)lparg1, arg2, arg3, (PHKEY)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RegOpenKeyExW_FUNC);
	return rc;
}
#endif

#ifndef NO_RegQueryInfoKeyA
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryInfoKeyA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jintLong arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jintLong arg11)
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
	rc = (jint)RegQueryInfoKeyA((HKEY)arg0, (LPSTR)arg1, (LPDWORD)lparg2, (LPDWORD)arg3, (LPDWORD)lparg4, (LPDWORD)lparg5, (LPDWORD)lparg6, (LPDWORD)lparg7, (LPDWORD)lparg8, (LPDWORD)lparg9, (LPDWORD)lparg10, (PFILETIME)arg11);
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jintLong arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jintLong arg11)
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
	rc = (jint)RegQueryInfoKeyW((HKEY)arg0, (LPWSTR)arg1, (LPDWORD)lparg2, (LPDWORD)arg3, (LPDWORD)lparg4, (LPDWORD)lparg5, (LPDWORD)lparg6, (LPDWORD)lparg7, (LPDWORD)lparg8, (LPDWORD)lparg9, (LPDWORD)lparg10, (PFILETIME)arg11);
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

#if (!defined(NO_RegQueryValueExA__I_3BI_3I_3B_3I) && !defined(JNI64)) || (!defined(NO_RegQueryValueExA__J_3BJ_3I_3B_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__I_3BI_3I_3B_3I)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintArray arg3, jbyteArray arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__J_3BJ_3I_3B_3I)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintArray arg3, jbyteArray arg4, jintArray arg5)
#endif
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__I_3BI_3I_3B_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__J_3BJ_3I_3B_3I_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, (LPDWORD)lparg3, (LPBYTE)lparg4, (LPDWORD)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__I_3BI_3I_3B_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__J_3BJ_3I_3B_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_RegQueryValueExA__I_3BI_3I_3I_3I) && !defined(JNI64)) || (!defined(NO_RegQueryValueExA__J_3BJ_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__I_3BI_3I_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExA__J_3BJ_3I_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#endif
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__I_3BI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, RegQueryValueExA__J_3BJ_3I_3I_3I_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExA((HKEY)arg0, (LPSTR)lparg1, (LPDWORD)arg2, (LPDWORD)lparg3, (LPBYTE)lparg4, (LPDWORD)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__I_3BI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, RegQueryValueExA__J_3BJ_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_RegQueryValueExW__I_3CI_3I_3C_3I) && !defined(JNI64)) || (!defined(NO_RegQueryValueExW__J_3CJ_3I_3C_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__I_3CI_3I_3C_3I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2, jintArray arg3, jcharArray arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__J_3CJ_3I_3C_3I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2, jintArray arg3, jcharArray arg4, jintArray arg5)
#endif
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__I_3CI_3I_3C_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__J_3CJ_3I_3C_3I_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, (LPDWORD)lparg3, (LPBYTE)lparg4, (LPDWORD)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__I_3CI_3I_3C_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__J_3CJ_3I_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_RegQueryValueExW__I_3CI_3I_3I_3I) && !defined(JNI64)) || (!defined(NO_RegQueryValueExW__J_3CJ_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__I_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(RegQueryValueExW__J_3CJ_3I_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#endif
{
	jchar *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__I_3CI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, RegQueryValueExW__J_3CJ_3I_3I_3I_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)RegQueryValueExW((HKEY)arg0, (LPWSTR)lparg1, (LPDWORD)arg2, (LPDWORD)lparg3, (LPBYTE)lparg4, (LPDWORD)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__I_3CI_3I_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, RegQueryValueExW__J_3CJ_3I_3I_3I_FUNC);
#endif
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveMenu_FUNC);
	rc = (jboolean)RemoveMenu((HMENU)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, RemoveMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_RemovePropA
JNIEXPORT jintLong JNICALL OS_NATIVE(RemovePropA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, RemovePropA_FUNC);
	rc = (jintLong)RemovePropA((HWND)arg0, (LPCTSTR)arg1);
	OS_NATIVE_EXIT(env, that, RemovePropA_FUNC);
	return rc;
}
#endif

#ifndef NO_RemovePropW
JNIEXPORT jintLong JNICALL OS_NATIVE(RemovePropW)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, RemovePropW_FUNC);
	rc = (jintLong)RemovePropW((HWND)arg0, (LPCWSTR)arg1);
	OS_NATIVE_EXIT(env, that, RemovePropW_FUNC);
	return rc;
}
#endif

#ifndef NO_ReplyMessage
JNIEXPORT jboolean JNICALL OS_NATIVE(ReplyMessage)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ReplyMessage_FUNC);
	rc = (jboolean)ReplyMessage(arg0);
	OS_NATIVE_EXIT(env, that, ReplyMessage_FUNC);
	return rc;
}
#endif

#ifndef NO_RestoreDC
JNIEXPORT jboolean JNICALL OS_NATIVE(RestoreDC)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RoundRect_FUNC);
	rc = (jboolean)RoundRect((HDC)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, RoundRect_FUNC);
	return rc;
}
#endif

#ifndef NO_SAFEARRAYBOUND_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SAFEARRAYBOUND_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SAFEARRAYBOUND_1sizeof_FUNC);
	rc = (jint)SAFEARRAYBOUND_sizeof();
	OS_NATIVE_EXIT(env, that, SAFEARRAYBOUND_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SAFEARRAY_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SAFEARRAY_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SAFEARRAY_1sizeof_FUNC);
	rc = (jint)SAFEARRAY_sizeof();
	OS_NATIVE_EXIT(env, that, SAFEARRAY_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1ANALYSIS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1ANALYSIS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1ANALYSIS_1sizeof_FUNC);
	rc = (jint)SCRIPT_ANALYSIS_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1ANALYSIS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1CONTROL_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1CONTROL_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1CONTROL_1sizeof_FUNC);
	rc = (jint)SCRIPT_CONTROL_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1CONTROL_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1DIGITSUBSTITUTE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1DIGITSUBSTITUTE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1DIGITSUBSTITUTE_1sizeof_FUNC);
	rc = (jint)SCRIPT_DIGITSUBSTITUTE_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1DIGITSUBSTITUTE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1FONTPROPERTIES_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1FONTPROPERTIES_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1FONTPROPERTIES_1sizeof_FUNC);
	rc = (jint)SCRIPT_FONTPROPERTIES_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1FONTPROPERTIES_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1ITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1ITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1ITEM_1sizeof_FUNC);
	rc = (jint)SCRIPT_ITEM_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1ITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1LOGATTR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1LOGATTR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1LOGATTR_1sizeof_FUNC);
	rc = (jint)SCRIPT_LOGATTR_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1LOGATTR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1PROPERTIES_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1PROPERTIES_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1PROPERTIES_1sizeof_FUNC);
	rc = (jint)SCRIPT_PROPERTIES_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1PROPERTIES_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1STATE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1STATE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1STATE_1sizeof_FUNC);
	rc = (jint)SCRIPT_STATE_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1STATE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCRIPT_1STRING_1ANALYSIS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCRIPT_1STRING_1ANALYSIS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCRIPT_1STRING_1ANALYSIS_1sizeof_FUNC);
	rc = (jint)SCRIPT_STRING_ANALYSIS_sizeof();
	OS_NATIVE_EXIT(env, that, SCRIPT_1STRING_1ANALYSIS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCROLLBARINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCROLLBARINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCROLLBARINFO_1sizeof_FUNC);
	rc = (jint)SCROLLBARINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SCROLLBARINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SCROLLINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SCROLLINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SCROLLINFO_1sizeof_FUNC);
	rc = (jint)SCROLLINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SCROLLINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHACTIVATEINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHACTIVATEINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHACTIVATEINFO_1sizeof_FUNC);
	rc = (jint)SHACTIVATEINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SHACTIVATEINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderA
JNIEXPORT jintLong JNICALL OS_NATIVE(SHBrowseForFolderA)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SHBrowseForFolderA_FUNC);
	if (arg0) if ((lparg0 = getBROWSEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)SHBrowseForFolderA(lparg0);
fail:
	if (arg0 && lparg0) setBROWSEINFOFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SHBrowseForFolderA_FUNC);
	return rc;
}
#endif

#ifndef NO_SHBrowseForFolderW
JNIEXPORT jintLong JNICALL OS_NATIVE(SHBrowseForFolderW)
	(JNIEnv *env, jclass that, jobject arg0)
{
	BROWSEINFO _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SHBrowseForFolderW_FUNC);
	if (arg0) if ((lparg0 = getBROWSEINFOFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)SHBrowseForFolderW((LPBROWSEINFOW)lparg0);
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

#ifndef NO_SHDRAGIMAGE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHDRAGIMAGE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHDRAGIMAGE_1sizeof_FUNC);
	rc = (jint)SHDRAGIMAGE_sizeof();
	OS_NATIVE_EXIT(env, that, SHDRAGIMAGE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHELLEXECUTEINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHELLEXECUTEINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHELLEXECUTEINFO_1sizeof_FUNC);
	rc = (jint)SHELLEXECUTEINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SHELLEXECUTEINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHFILEINFOA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHFILEINFOA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHFILEINFOA_1sizeof_FUNC);
	rc = (jint)SHFILEINFOA_sizeof();
	OS_NATIVE_EXIT(env, that, SHFILEINFOA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHFILEINFOW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHFILEINFOW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHFILEINFOW_1sizeof_FUNC);
	rc = (jint)SHFILEINFOW_sizeof();
	OS_NATIVE_EXIT(env, that, SHFILEINFOW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetFileInfoA
JNIEXPORT jintLong JNICALL OS_NATIVE(SHGetFileInfoA)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	SHFILEINFOA _arg2, *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetFileInfoA_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getSHFILEINFOAFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)SHGetFileInfoA((LPCSTR)lparg0, arg1, (SHFILEINFOA *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setSHFILEINFOAFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SHGetFileInfoA_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetFileInfoW
JNIEXPORT jintLong JNICALL OS_NATIVE(SHGetFileInfoW)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	jchar *lparg0=NULL;
	SHFILEINFOW _arg2, *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetFileInfoW_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getSHFILEINFOWFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)SHGetFileInfoW((LPCWSTR)lparg0, arg1, (SHFILEINFOW *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setSHFILEINFOWFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SHGetFileInfoW_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetFolderPathA
JNIEXPORT jint JNICALL OS_NATIVE(SHGetFolderPathA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetFolderPathA_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SHGetFolderPathA((HWND)arg0, arg1, (HANDLE)arg2, arg3, (LPSTR)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SHGetFolderPathA_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetFolderPathW
JNIEXPORT jint JNICALL OS_NATIVE(SHGetFolderPathW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetFolderPathW_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SHGetFolderPathW((HWND)arg0, arg1, (HANDLE)arg2, arg3, (LPWSTR)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SHGetFolderPathW_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetMalloc
JNIEXPORT jint JNICALL OS_NATIVE(SHGetMalloc)
	(JNIEnv *env, jclass that, jintLongArray arg0)
{
	jintLong *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHGetMalloc_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)SHGetMalloc((LPMALLOC *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntLongArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SHGetMalloc_FUNC);
	return rc;
}
#endif

#ifndef NO_SHGetPathFromIDListA
JNIEXPORT jboolean JNICALL OS_NATIVE(SHGetPathFromIDListA)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
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

#ifndef NO_SHMENUBARINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHMENUBARINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHMENUBARINFO_1sizeof_FUNC);
	rc = (jint)SHMENUBARINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SHMENUBARINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SHRGINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SHRGINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SHRGINFO_1sizeof_FUNC);
	rc = (jint)SHRGINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SHRGINFO_1sizeof_FUNC);
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
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, SHSendBackToFocusWindow_FUNC);
	SHSendBackToFocusWindow(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SHSendBackToFocusWindow_FUNC);
}
#endif

#ifndef NO_SHSetAppKeyWndAssoc
JNIEXPORT jboolean JNICALL OS_NATIVE(SHSetAppKeyWndAssoc)
	(JNIEnv *env, jclass that, jbyte arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SHSipPreference_FUNC);
	rc = (jboolean)SHSipPreference((HWND)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SHSipPreference_FUNC);
	return rc;
}
#endif

#ifndef NO_SIPINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SIPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SIPINFO_1sizeof_FUNC);
	rc = (jint)SIPINFO_sizeof();
	OS_NATIVE_EXIT(env, that, SIPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SIZE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SIZE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SIZE_1sizeof_FUNC);
	rc = (jint)SIZE_sizeof();
	OS_NATIVE_EXIT(env, that, SIZE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_STARTUPINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(STARTUPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, STARTUPINFO_1sizeof_FUNC);
	rc = (jint)STARTUPINFO_sizeof();
	OS_NATIVE_EXIT(env, that, STARTUPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SYSTEMTIME_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(SYSTEMTIME_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SYSTEMTIME_1sizeof_FUNC);
	rc = (jint)SYSTEMTIME_sizeof();
	OS_NATIVE_EXIT(env, that, SYSTEMTIME_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_SaveDC
JNIEXPORT jint JNICALL OS_NATIVE(SaveDC)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#ifndef NO_ScriptApplyDigitSubstitution
JNIEXPORT jint JNICALL OS_NATIVE(ScriptApplyDigitSubstitution)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	SCRIPT_DIGITSUBSTITUTE _arg0, *lparg0=NULL;
	SCRIPT_CONTROL _arg1, *lparg1=NULL;
	SCRIPT_STATE _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptApplyDigitSubstitution_FUNC);
	if (arg0) if ((lparg0 = getSCRIPT_DIGITSUBSTITUTEFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getSCRIPT_CONTROLFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getSCRIPT_STATEFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ScriptApplyDigitSubstitution((const SCRIPT_DIGITSUBSTITUTE*)lparg0, (SCRIPT_CONTROL*)lparg1, (SCRIPT_STATE*)lparg2);
fail:
	if (arg2 && lparg2) setSCRIPT_STATEFields(env, arg2, lparg2);
	if (arg1 && lparg1) setSCRIPT_CONTROLFields(env, arg1, lparg1);
	if (arg0 && lparg0) setSCRIPT_DIGITSUBSTITUTEFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ScriptApplyDigitSubstitution_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptBreak
JNIEXPORT jint JNICALL OS_NATIVE(ScriptBreak)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jobject arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jint arg3, jintLong arg4, jintLong arg5, jintLong arg6, jobject arg7, jintArray arg8)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptFreeCache_FUNC);
	rc = (jint)ScriptFreeCache((SCRIPT_CACHE *)arg0);
	OS_NATIVE_EXIT(env, that, ScriptFreeCache_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptGetCMap
JNIEXPORT jint JNICALL OS_NATIVE(ScriptGetCMap)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jshortArray arg5)
{
	jchar *lparg2=NULL;
	jshort *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptGetCMap_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ScriptGetCMap((HDC)arg0, (SCRIPT_CACHE *)arg1, (const WCHAR *)lparg2, arg3, arg4, (WORD*)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ScriptGetCMap_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptGetFontProperties
JNIEXPORT jint JNICALL OS_NATIVE(ScriptGetFontProperties)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
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
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintArray arg6)
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
	(JNIEnv *env, jclass that, jintLongArray arg0, jintArray arg1)
{
	jintLong *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptGetProperties_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ScriptGetProperties((const SCRIPT_PROPERTIES ***)lparg0, (int *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntLongArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ScriptGetProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptItemize
JNIEXPORT jint JNICALL OS_NATIVE(ScriptItemize)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jintLong arg5, jintArray arg6)
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

#ifndef NO_ScriptJustify
JNIEXPORT jint JNICALL OS_NATIVE(ScriptJustify)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintLong arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptJustify_FUNC);
	rc = (jint)ScriptJustify((SCRIPT_VISATTR *)arg0, (const int *)arg1, arg2, arg3, arg4, (int *)arg5);
	OS_NATIVE_EXIT(env, that, ScriptJustify_FUNC);
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jintLong arg4, jobject arg5, jintLong arg6, jintLong arg7, jintArray arg8)
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

#ifndef NO_ScriptRecordDigitSubstitution
JNIEXPORT jint JNICALL OS_NATIVE(ScriptRecordDigitSubstitution)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	SCRIPT_DIGITSUBSTITUTE _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptRecordDigitSubstitution_FUNC);
	if (arg1) if ((lparg1 = getSCRIPT_DIGITSUBSTITUTEFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)ScriptRecordDigitSubstitution((LCID)arg0, (SCRIPT_DIGITSUBSTITUTE*)lparg1);
fail:
	if (arg1 && lparg1) setSCRIPT_DIGITSUBSTITUTEFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ScriptRecordDigitSubstitution_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptShape
JNIEXPORT jint JNICALL OS_NATIVE(ScriptShape)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jobject arg5, jintLong arg6, jintLong arg7, jintLong arg8, jintArray arg9)
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

#ifndef NO_ScriptStringAnalyse
JNIEXPORT jint JNICALL OS_NATIVE(ScriptStringAnalyse)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jobject arg7, jobject arg8, jintLong arg9, jintLong arg10, jintLong arg11, jintLong arg12)
{
	jchar *lparg1=NULL;
	SCRIPT_CONTROL _arg7, *lparg7=NULL;
	SCRIPT_STATE _arg8, *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptStringAnalyse_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getSCRIPT_CONTROLFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getSCRIPT_STATEFields(env, arg8, &_arg8)) == NULL) goto fail;
	rc = (jint)ScriptStringAnalyse((HDC)arg0, (const void*)lparg1, arg2, arg3, arg4, arg5, arg6, lparg7, lparg8, (const int*)arg9, (SCRIPT_TABDEF*)arg10, (const BYTE*)arg11, (SCRIPT_STRING_ANALYSIS*)arg12);
fail:
	if (arg8 && lparg8) setSCRIPT_STATEFields(env, arg8, lparg8);
	if (arg7 && lparg7) setSCRIPT_CONTROLFields(env, arg7, lparg7);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ScriptStringAnalyse_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptStringFree
JNIEXPORT jint JNICALL OS_NATIVE(ScriptStringFree)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptStringFree_FUNC);
	rc = (jint)ScriptStringFree((SCRIPT_STRING_ANALYSIS*)arg0);
	OS_NATIVE_EXIT(env, that, ScriptStringFree_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptStringOut
JNIEXPORT jint JNICALL OS_NATIVE(ScriptStringOut)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6, jboolean arg7)
{
	RECT _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptStringOut_FUNC);
	if (arg4) if ((lparg4 = getRECTFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)ScriptStringOut(*(SCRIPT_STRING_ANALYSIS*)arg0, arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) setRECTFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, ScriptStringOut_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptTextOut
JNIEXPORT jint JNICALL OS_NATIVE(ScriptTextOut)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jobject arg6, jintLong arg7, jint arg8, jintLong arg9, jint arg10, jintLong arg11, jintLong arg12, jintLong arg13)
{
	RECT _arg5, *lparg5=NULL;
	SCRIPT_ANALYSIS _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ScriptTextOut_FUNC);
	if (arg5) if ((lparg5 = getRECTFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getSCRIPT_ANALYSISFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)ScriptTextOut((const HDC)arg0, (SCRIPT_CACHE *)arg1, arg2, arg3, arg4, (const RECT *)lparg5, (const SCRIPT_ANALYSIS *)lparg6, (const WCHAR *)arg7, arg8, (const WORD *)arg9, arg10, (const int *)arg11, (const int *)arg12, (const GOFFSET *)arg13);
fail:
	if (arg6 && lparg6) setSCRIPT_ANALYSISFields(env, arg6, lparg6);
	if (arg5 && lparg5) setRECTFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, ScriptTextOut_FUNC);
	return rc;
}
#endif

#ifndef NO_ScriptXtoCP
JNIEXPORT jint JNICALL OS_NATIVE(ScriptXtoCP)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintLong arg3, jintLong arg4, jintLong arg5, jobject arg6, jintArray arg7, jintArray arg8)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jintLong arg5, jobject arg6, jint arg7)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SelectClipRgn_FUNC);
	rc = (jint)SelectClipRgn((HDC)arg0, (HRGN)arg1);
	OS_NATIVE_EXIT(env, that, SelectClipRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectObject
JNIEXPORT jintLong JNICALL OS_NATIVE(SelectObject)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SelectObject_FUNC);
	rc = (jintLong)SelectObject((HDC)arg0, (HGDIOBJ)arg1);
	OS_NATIVE_EXIT(env, that, SelectObject_FUNC);
	return rc;
}
#endif

#ifndef NO_SelectPalette
JNIEXPORT jintLong JNICALL OS_NATIVE(SelectPalette)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SelectPalette_FUNC);
	rc = (jintLong)SelectPalette((HDC)arg0, (HPALETTE)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SelectPalette_FUNC);
	return rc;
}
#endif

#ifndef NO_SendInput
JNIEXPORT jint JNICALL OS_NATIVE(SendInput)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendInput_FUNC);
	rc = (jint)SendInput(arg0, (LPINPUT)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SendInput_FUNC);
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIII) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIII)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJJ_FUNC);
#endif
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	BUTTON_IMAGELIST _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getBUTTON_IMAGELISTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setBUTTON_IMAGELISTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDLAYOUT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDLAYOUTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDLAYOUTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVCOLUMNFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVCOLUMNFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVINSERTMARK _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVINSERTMARKFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVINSERTMARKFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	MARGINS _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMARGINSFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMARGINSFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	MCHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMCHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMCHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	POINT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setREBARBANDINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	RECT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SHDRAGIMAGE _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSHDRAGIMAGEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSHDRAGIMAGEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SIZE _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SYSTEMTIME _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSYSTEMTIMEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSYSTEMTIMEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TBBUTTON _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTBBUTTONFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TCHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTCHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TCITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTCITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TOOLINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTOOLINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTOOLINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVSORTCB _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVSORTCBFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVSORTCBFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	UDACCEL _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getUDACCELFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setUDACCELFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__III_3B) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJ_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__III_3B)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jbyteArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJ_3B)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jbyteArray arg3)
#endif
{
	jbyte *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJ_3B_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJ_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__III_3C) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJ_3C) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__III_3C)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jcharArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJ_3C)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jcharArray arg3)
#endif
{
	jchar *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3C_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJ_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3C_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJ_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__III_3I) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__III_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJ_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJ_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__III_3S) && !defined(JNI64)) || (!defined(NO_SendMessageA__JIJ_3S) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__III_3S)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jshortArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JIJ_3S)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jshortArray arg3)
#endif
{
	jshort *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__III_3S_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JIJ_3S_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__III_3S_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JIJ_3S_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__II_3II) && !defined(JNI64)) || (!defined(NO_SendMessageA__JI_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__II_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JI_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2, jintLong arg3)
#endif
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__II_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JI_3JJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__II_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JI_3JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageA__II_3I_3I) && !defined(JNI64)) || (!defined(NO_SendMessageA__JI_3I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__II_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jintArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageA__JI_3I_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintArray arg2, jintArray arg3)
#endif
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageA__II_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageA__JI_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageA((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageA__II_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageA__JI_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIII) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIII)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJJ_FUNC);
#endif
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	BUTTON_IMAGELIST _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getBUTTON_IMAGELISTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setBUTTON_IMAGELISTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	HDLAYOUT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getHDLAYOUTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setHDLAYOUTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVCOLUMN _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVCOLUMNFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVCOLUMNFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVINSERTMARK _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVINSERTMARKFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVINSERTMARKFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	LVITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getLVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setLVITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	MARGINS _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMARGINSFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMARGINSFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	MCHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMCHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setMCHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	POINT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	REBARBANDINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getREBARBANDINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setREBARBANDINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	RECT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SHDRAGIMAGE _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSHDRAGIMAGEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSHDRAGIMAGEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SIZE _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	SYSTEMTIME _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSYSTEMTIMEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setSYSTEMTIMEFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TBBUTTONINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTBBUTTONINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TBBUTTON _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTBBUTTONFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTBBUTTONFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TCHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTCHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TCITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTCITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTCITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TOOLINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTOOLINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTOOLINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVHITTESTINFO _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVHITTESTINFOFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVHITTESTINFOFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVINSERTSTRUCT _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVINSERTSTRUCTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVINSERTSTRUCTFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVITEM _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVITEMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVITEMFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	TVSORTCB _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getTVSORTCBFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setTVSORTCBFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jobject arg3)
#endif
{
	UDACCEL _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getUDACCELFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) setUDACCELFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__III_3C) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJ_3C) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__III_3C)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jcharArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJ_3C)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jcharArray arg3)
#endif
{
	jchar *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3C_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJ_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3C_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJ_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__III_3I) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__III_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJ_3I)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJ_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__III_3S) && !defined(JNI64)) || (!defined(NO_SendMessageW__JIJ_3S) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__III_3S)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jshortArray arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JIJ_3S)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jshortArray arg3)
#endif
{
	jshort *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__III_3S_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JIJ_3S_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)arg2, (LPARAM)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__III_3S_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JIJ_3S_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_SendMessageW__II_3II) && !defined(JNI64)) || (!defined(NO_SendMessageW__JI_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__II_3II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(SendMessageW__JI_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2, jintLong arg3)
#endif
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, SendMessageW__II_3II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, SendMessageW__JI_3JJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)SendMessageW((HWND)arg0, arg1, (WPARAM)lparg2, (LPARAM)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, SendMessageW__II_3II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, SendMessageW__JI_3JJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_SetActiveWindow
JNIEXPORT jintLong JNICALL OS_NATIVE(SetActiveWindow)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetActiveWindow_FUNC);
	rc = (jintLong)SetActiveWindow((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetActiveWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetBkColor
JNIEXPORT jint JNICALL OS_NATIVE(SetBkColor)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetBkMode_FUNC);
	rc = (jint)SetBkMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetBkMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetBrushOrgEx
JNIEXPORT jboolean JNICALL OS_NATIVE(SetBrushOrgEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	POINT _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetBrushOrgEx_FUNC);
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetBrushOrgEx((HDC)arg0, arg1, arg2, (LPPOINT)lparg3);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetBrushOrgEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCapture
JNIEXPORT jintLong JNICALL OS_NATIVE(SetCapture)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetCapture_FUNC);
	rc = (jintLong)SetCapture((HWND)arg0);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(SetClipboardData)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetClipboardData_FUNC);
	rc = (jintLong)SetClipboardData(arg0, (HANDLE)arg1);
	OS_NATIVE_EXIT(env, that, SetClipboardData_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCursor
JNIEXPORT jintLong JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetCursor_FUNC);
	rc = (jintLong)SetCursor((HCURSOR)arg0);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jbyteArray arg3)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(SetFocus)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetFocus_FUNC);
	rc = (jintLong)SetFocus((HWND)arg0);
	OS_NATIVE_EXIT(env, that, SetFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_SetForegroundWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(SetForegroundWindow)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetGraphicsMode_FUNC);
	rc = (jint)SetGraphicsMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetGraphicsMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetLayeredWindowAttributes
JNIEXPORT jboolean JNICALL OS_NATIVE(SetLayeredWindowAttributes)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jbyte arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetLayeredWindowAttributes_FUNC);
/*
	rc = (jboolean)SetLayeredWindowAttributes((HWND)arg0, arg1, arg2, arg3);
*/
	{
		LOAD_FUNCTION(fp, SetLayeredWindowAttributes)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, jint, jbyte, jint))fp)((HWND)arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, SetLayeredWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_SetLayout
JNIEXPORT jint JNICALL OS_NATIVE(SetLayout)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetLayout_FUNC);
/*
	rc = (jint)SetLayout((HDC)arg0, (DWORD)arg1);
*/
	{
		LOAD_FUNCTION(fp, SetLayout)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HDC, DWORD))fp)((HDC)arg0, (DWORD)arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, SetLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMapMode
JNIEXPORT jint JNICALL OS_NATIVE(SetMapMode)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMapMode_FUNC);
	rc = (jint)SetMapMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetMapMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMapperFlags
JNIEXPORT jint JNICALL OS_NATIVE(SetMapperFlags)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMapperFlags_FUNC);
	rc = (jint)SetMapperFlags((HDC)arg0, (DWORD)arg1);
	OS_NATIVE_EXIT(env, that, SetMapperFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(SetMenu)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
{
	MENUINFO _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuInfo_FUNC);
	if (arg1) if ((lparg1 = getMENUINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jboolean)SetMenuInfo((HMENU)arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, SetMenuInfo)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HMENU, MENUINFO *))fp)((HMENU)arg0, lparg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jbyteArray arg3)
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
JNIEXPORT jintLong JNICALL OS_NATIVE(SetParent)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetParent_FUNC);
	rc = (jintLong)SetParent((HWND)arg0, (HWND)arg1);
	OS_NATIVE_EXIT(env, that, SetParent_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPixel
JNIEXPORT jint JNICALL OS_NATIVE(SetPixel)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetPolyFillMode_FUNC);
	rc = (jint)SetPolyFillMode((HDC)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetPolyFillMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetProcessDPIAware
JNIEXPORT jboolean JNICALL OS_NATIVE(SetProcessDPIAware)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetProcessDPIAware_FUNC);
/*
	rc = (jboolean)SetProcessDPIAware();
*/
	{
		LOAD_FUNCTION(fp, SetProcessDPIAware)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, SetProcessDPIAware_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPropA
JNIEXPORT jboolean JNICALL OS_NATIVE(SetPropA)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2, jboolean arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetTextColor_FUNC);
	rc = (jint)SetTextColor((HDC)arg0, (COLORREF)arg1);
	OS_NATIVE_EXIT(env, that, SetTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetTimer
JNIEXPORT jintLong JNICALL OS_NATIVE(SetTimer)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetTimer_FUNC);
	rc = (jintLong)SetTimer((HWND)arg0, arg1, arg2, (TIMERPROC)arg3);
	OS_NATIVE_EXIT(env, that, SetTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_SetViewportExtEx
JNIEXPORT jboolean JNICALL OS_NATIVE(SetViewportExtEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	SIZE _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetViewportExtEx_FUNC);
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetViewportExtEx((HDC)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetViewportExtEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SetViewportOrgEx
JNIEXPORT jboolean JNICALL OS_NATIVE(SetViewportOrgEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	POINT _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetViewportOrgEx_FUNC);
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetViewportOrgEx((HDC)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetViewportOrgEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowExtEx
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWindowExtEx)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
{
	SIZE _arg3, *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowExtEx_FUNC);
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jboolean)SetWindowExtEx((HDC)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, SetWindowExtEx_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongA
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowLongA_FUNC);
	rc = (jint)SetWindowLongA((HWND)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetWindowLongA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongPtrA
JNIEXPORT jintLong JNICALL OS_NATIVE(SetWindowLongPtrA)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowLongPtrA_FUNC);
	rc = (jintLong)SetWindowLongPtrA((HWND)arg0, arg1, (LONG_PTR)arg2);
	OS_NATIVE_EXIT(env, that, SetWindowLongPtrA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongPtrW
JNIEXPORT jintLong JNICALL OS_NATIVE(SetWindowLongPtrW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowLongPtrW_FUNC);
	rc = (jintLong)SetWindowLongPtrW((HWND)arg0, arg1, (LONG_PTR)arg2);
	OS_NATIVE_EXIT(env, that, SetWindowLongPtrW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowLongW
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowLongW)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jobject arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1)
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

#ifndef NO_SetWindowTheme
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowTheme)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jcharArray arg2)
{
	jchar *lparg1=NULL;
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowTheme_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)SetWindowTheme((HWND)arg0, (LPCWSTR)lparg1, (LPCWSTR)lparg2);
*/
	{
		LOAD_FUNCTION(fp, SetWindowTheme)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HWND, LPCWSTR, LPCWSTR))fp)((HWND)arg0, (LPCWSTR)lparg1, (LPCWSTR)lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SetWindowTheme_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExA
JNIEXPORT jintLong JNICALL OS_NATIVE(SetWindowsHookExA)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowsHookExA_FUNC);
	rc = (jintLong)SetWindowsHookExA(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	OS_NATIVE_EXIT(env, that, SetWindowsHookExA_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowsHookExW
JNIEXPORT jintLong JNICALL OS_NATIVE(SetWindowsHookExW)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jint arg3)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowsHookExW_FUNC);
	rc = (jintLong)SetWindowsHookExW(arg0, (HOOKPROC)arg1, (HINSTANCE)arg2, arg3);
	OS_NATIVE_EXIT(env, that, SetWindowsHookExW_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWorldTransform
JNIEXPORT jboolean JNICALL OS_NATIVE(SetWorldTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ShowCaret_FUNC);
	rc = (jboolean)ShowCaret((HWND)arg0);
	OS_NATIVE_EXIT(env, that, ShowCaret_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowCursor
JNIEXPORT jint JNICALL OS_NATIVE(ShowCursor)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ShowCursor_FUNC);
	rc = (jint)ShowCursor(arg0);
	OS_NATIVE_EXIT(env, that, ShowCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowOwnedPopups
JNIEXPORT jboolean JNICALL OS_NATIVE(ShowOwnedPopups)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jboolean arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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

#ifndef NO_TBBUTTONINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TBBUTTONINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TBBUTTONINFO_1sizeof_FUNC);
	rc = (jint)TBBUTTONINFO_sizeof();
	OS_NATIVE_EXIT(env, that, TBBUTTONINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TBBUTTON_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TBBUTTON_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TBBUTTON_1sizeof_FUNC);
	rc = (jint)TBBUTTON_sizeof();
	OS_NATIVE_EXIT(env, that, TBBUTTON_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TCHITTESTINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TCHITTESTINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TCHITTESTINFO_1sizeof_FUNC);
	rc = (jint)TCHITTESTINFO_sizeof();
	OS_NATIVE_EXIT(env, that, TCHITTESTINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TCITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TCITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TCITEM_1sizeof_FUNC);
	rc = (jint)TCITEM_sizeof();
	OS_NATIVE_EXIT(env, that, TCITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TEXTMETRICA_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TEXTMETRICA_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TEXTMETRICA_1sizeof_FUNC);
	rc = (jint)TEXTMETRICA_sizeof();
	OS_NATIVE_EXIT(env, that, TEXTMETRICA_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TEXTMETRICW_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TEXTMETRICW_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TEXTMETRICW_1sizeof_FUNC);
	rc = (jint)TEXTMETRICW_sizeof();
	OS_NATIVE_EXIT(env, that, TEXTMETRICW_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TF_1DA_1COLOR_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TF_1DA_1COLOR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TF_1DA_1COLOR_1sizeof_FUNC);
	rc = (jint)TF_DA_COLOR_sizeof();
	OS_NATIVE_EXIT(env, that, TF_1DA_1COLOR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TF_1DISPLAYATTRIBUTE_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TF_1DISPLAYATTRIBUTE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TF_1DISPLAYATTRIBUTE_1sizeof_FUNC);
	rc = (jint)TF_DISPLAYATTRIBUTE_sizeof();
	OS_NATIVE_EXIT(env, that, TF_1DISPLAYATTRIBUTE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TOOLINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TOOLINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TOOLINFO_1sizeof_FUNC);
	rc = (jint)TOOLINFO_sizeof();
	OS_NATIVE_EXIT(env, that, TOOLINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TRACKMOUSEEVENT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TRACKMOUSEEVENT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TRACKMOUSEEVENT_1sizeof_FUNC);
	rc = (jint)TRACKMOUSEEVENT_sizeof();
	OS_NATIVE_EXIT(env, that, TRACKMOUSEEVENT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TRIVERTEX_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TRIVERTEX_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TRIVERTEX_1sizeof_FUNC);
	rc = (jint)TRIVERTEX_sizeof();
	OS_NATIVE_EXIT(env, that, TRIVERTEX_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TVHITTESTINFO_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TVHITTESTINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TVHITTESTINFO_1sizeof_FUNC);
	rc = (jint)TVHITTESTINFO_sizeof();
	OS_NATIVE_EXIT(env, that, TVHITTESTINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TVINSERTSTRUCT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TVINSERTSTRUCT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TVINSERTSTRUCT_1sizeof_FUNC);
	rc = (jint)TVINSERTSTRUCT_sizeof();
	OS_NATIVE_EXIT(env, that, TVINSERTSTRUCT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TVITEMEX_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TVITEMEX_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TVITEMEX_1sizeof_FUNC);
	rc = (jint)TVITEMEX_sizeof();
	OS_NATIVE_EXIT(env, that, TVITEMEX_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TVITEM_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TVITEM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TVITEM_1sizeof_FUNC);
	rc = (jint)TVITEM_sizeof();
	OS_NATIVE_EXIT(env, that, TVITEM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TVSORTCB_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(TVSORTCB_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TVSORTCB_1sizeof_FUNC);
	rc = (jint)TVSORTCB_sizeof();
	OS_NATIVE_EXIT(env, that, TVSORTCB_1sizeof_FUNC);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jobject arg6)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jint arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TransparentBlt_FUNC);
/*
	rc = (jboolean)TransparentBlt(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
*/
	{
		LOAD_FUNCTION(fp, TransparentBlt)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jint, jint, jint, jint, jintLong, jint, jint, jint, jint, jint))fp)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		}
	}
	OS_NATIVE_EXIT(env, that, TransparentBlt_FUNC);
	return rc;
}
#endif

#ifndef NO_TransparentImage
JNIEXPORT jboolean JNICALL OS_NATIVE(TransparentImage)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TransparentImage_FUNC);
	rc = (jboolean)TransparentImage((HDC)arg0, arg1, arg2, arg3, arg4, (HANDLE)arg5, arg6, arg7, arg8, arg9, (COLORREF)arg10);
	OS_NATIVE_EXIT(env, that, TransparentImage_FUNC);
	return rc;
}
#endif

#ifndef NO_TreeView_1GetItemRect
JNIEXPORT jboolean JNICALL OS_NATIVE(TreeView_1GetItemRect)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jboolean arg3)
{
	RECT _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, TreeView_1GetItemRect_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)TreeView_GetItemRect((HWND)arg0, (HTREEITEM)arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, TreeView_1GetItemRect_FUNC);
	return rc;
}
#endif

#ifndef NO_UDACCEL_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(UDACCEL_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UDACCEL_1sizeof_FUNC);
	rc = (jint)UDACCEL_sizeof();
	OS_NATIVE_EXIT(env, that, UDACCEL_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_UnhookWindowsHookEx
JNIEXPORT jboolean JNICALL OS_NATIVE(UnhookWindowsHookEx)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1)
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

#ifndef NO_UpdateLayeredWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(UpdateLayeredWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4, jobject arg5, jint arg6, jobject arg7, jint arg8)
{
	POINT _arg2, *lparg2=NULL;
	SIZE _arg3, *lparg3=NULL;
	POINT _arg5, *lparg5=NULL;
	BLENDFUNCTION _arg7, *lparg7=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UpdateLayeredWindow_FUNC);
	if (arg2) if ((lparg2 = getPOINTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getPOINTFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getBLENDFUNCTIONFields(env, arg7, &_arg7)) == NULL) goto fail;
/*
	rc = (jboolean)UpdateLayeredWindow((HWND)arg0, (HDC)arg1, lparg2, lparg3, (HDC)arg4, lparg5, (COLORREF)arg6, lparg7, arg8);
*/
	{
		LOAD_FUNCTION(fp, UpdateLayeredWindow)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HWND, HDC, POINT *, SIZE *, HDC, POINT *, COLORREF, BLENDFUNCTION *, jint))fp)((HWND)arg0, (HDC)arg1, lparg2, lparg3, (HDC)arg4, lparg5, (COLORREF)arg6, lparg7, arg8);
		}
	}
fail:
	if (arg7 && lparg7) setBLENDFUNCTIONFields(env, arg7, lparg7);
	if (arg5 && lparg5) setPOINTFields(env, arg5, lparg5);
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	if (arg2 && lparg2) setPOINTFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, UpdateLayeredWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_UpdateWindow
JNIEXPORT jboolean JNICALL OS_NATIVE(UpdateWindow)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

#if (!defined(NO_VtblCall__II) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II)(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong))(*(jintLong **)arg1)[arg0])(arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__III)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIII_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIII_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
#endif
{
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIIII_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJIII_3I_FUNC);
#endif
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIIII_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJIII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIJI_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIJI_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIIJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jint arg4, jlongArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJIJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jint arg4, jlongArray arg5)
#endif
{
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIIJI_3J_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJIJI_3J_FUNC);
#endif
	if (arg5) if ((lparg5 = (*env)->GetLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseLongArrayElements(env, arg5, lparg5, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIIJI_3J_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJIJI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3I_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3J_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__III_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintLongArray arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintLongArray arg3, jintArray arg4)
#endif
{
	jintLong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__III_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJI_3J_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jintLong *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__III_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJI_3J_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIJ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIJ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJII_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJII_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlongArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlongArray arg5)
#endif
{
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIJII_3J_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJJII_3J_FUNC);
#endif
	if (arg5) if ((lparg5 = (*env)->GetLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseLongArrayElements(env, arg5, lparg5, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIJII_3J_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJJII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJJI_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJJI_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIJJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlongArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJJJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlongArray arg5)
#endif
{
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIJJI_3J_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJJJI_3J_FUNC);
#endif
	if (arg5) if ((lparg5 = (*env)->GetLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseLongArrayElements(env, arg5, lparg5, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIJJI_3J_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJJJI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	TF_DISPLAYATTRIBUTE _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getTF_DISPLAYATTRIBUTEFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, TF_DISPLAYATTRIBUTE *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setTF_DISPLAYATTRIBUTEFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIS_3B_3B_3B) && !defined(JNI64)) || (!defined(NO_VtblCall__IJS_3B_3B_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IIS_3B_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJS_3B_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5)
#endif
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__IIS_3B_3B_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJS_3B_3B_3B_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort, jbyte *, jbyte *, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__IIS_3B_3B_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJS_3B_3B_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3B_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3B_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintLongArray arg3)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJ_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintLongArray arg3)
#endif
{
	jbyte *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__II_3B_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJ_3B_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jintLong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__II_3B_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJ_3B_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CII_3I_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CII_3I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II_3CII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJ_3CII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
#endif
{
	jchar *lparg2=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__II_3CII_3I_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJ_3CII_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__II_3CII_3I_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJ_3CII_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2)
#endif
{
	jint *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__II_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJ_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__II_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__II_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2)
#else
JNIEXPORT jint JNICALL OS_NATIVE(VtblCall__IJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2)
#endif
{
	jlong *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, VtblCall__II_3J_FUNC);
#else
	OS_NATIVE_ENTER(env, that, VtblCall__IJ_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseLongArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, VtblCall__II_3J_FUNC);
#else
	OS_NATIVE_EXIT(env, that, VtblCall__IJ_3J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_WINDOWPLACEMENT_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(WINDOWPLACEMENT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WINDOWPLACEMENT_1sizeof_FUNC);
	rc = (jint)WINDOWPLACEMENT_sizeof();
	OS_NATIVE_EXIT(env, that, WINDOWPLACEMENT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_WINDOWPOS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(WINDOWPOS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WINDOWPOS_1sizeof_FUNC);
	rc = (jint)WINDOWPOS_sizeof();
	OS_NATIVE_EXIT(env, that, WINDOWPOS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_WNDCLASS_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(WNDCLASS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, WNDCLASS_1sizeof_FUNC);
	rc = (jint)WNDCLASS_sizeof();
	OS_NATIVE_EXIT(env, that, WNDCLASS_1sizeof_FUNC);
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

#if (!defined(NO_WideCharToMultiByte__II_3CIII_3B_3Z) && !defined(JNI64)) || (!defined(NO_WideCharToMultiByte__II_3CIJI_3B_3Z) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CIII_3B_3Z)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jintLong arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
#else
JNIEXPORT jint JNICALL OS_NATIVE(WideCharToMultiByte__II_3CIJI_3B_3Z)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jintLong arg4, jint arg5, jbyteArray arg6, jbooleanArray arg7)
#endif
{
	jchar *lparg2=NULL;
	jbyte *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, WideCharToMultiByte__II_3CIII_3B_3Z_FUNC);
#else
	OS_NATIVE_ENTER(env, that, WideCharToMultiByte__II_3CIJI_3B_3Z_FUNC);
#endif
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
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, WideCharToMultiByte__II_3CIII_3B_3Z_FUNC);
#else
	OS_NATIVE_EXIT(env, that, WideCharToMultiByte__II_3CIJI_3B_3Z_FUNC);
#endif
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
JNIEXPORT jintLong JNICALL OS_NATIVE(WindowFromDC)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, WindowFromDC_FUNC);
	rc = (jintLong)WindowFromDC((HDC)arg0);
	OS_NATIVE_EXIT(env, that, WindowFromDC_FUNC);
	return rc;
}
#endif

#ifndef NO_WindowFromPoint
JNIEXPORT jintLong JNICALL OS_NATIVE(WindowFromPoint)
	(JNIEnv *env, jclass that, jobject arg0)
{
	POINT _arg0, *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, WindowFromPoint_FUNC);
	if (arg0) if ((lparg0 = getPOINTFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)WindowFromPoint(*lparg0);
fail:
	if (arg0 && lparg0) setPOINTFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, WindowFromPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_wcslen
JNIEXPORT jint JNICALL OS_NATIVE(wcslen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, wcslen_FUNC);
	rc = (jint)wcslen((const wchar_t *)arg0);
	OS_NATIVE_EXIT(env, that, wcslen_FUNC);
	return rc;
}
#endif


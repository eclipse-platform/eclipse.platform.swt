/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_OS_##func

#ifndef NO_CGWarpMouseCursorPosition
JNIEXPORT jint JNICALL OS_NATIVE(CGWarpMouseCursorPosition)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGWarpMouseCursorPosition_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CGWarpMouseCursorPosition(*lparg0);
fail:
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, CGWarpMouseCursorPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseRgn
JNIEXPORT void JNICALL OS_NATIVE(CloseRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CloseRgn_FUNC);
/*
	CloseRgn(arg0);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(CloseRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("CloseRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, CloseRgn_FUNC);
}
#endif

#ifndef NO_CopyRgn
JNIEXPORT void JNICALL OS_NATIVE(CopyRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CopyRgn_FUNC);
/*
	CopyRgn(arg0, arg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(CopyRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("CopyRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, CopyRgn_FUNC);
}
#endif

#ifndef NO_DeleteGlobalRef
JNIEXPORT void JNICALL OS_NATIVE(DeleteGlobalRef)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DeleteGlobalRef_FUNC);
	(*env)->DeleteGlobalRef(env, (jobject)arg0);
	OS_NATIVE_EXIT(env, that, DeleteGlobalRef_FUNC);
}
#endif

#ifndef NO_DiffRgn
JNIEXPORT void JNICALL OS_NATIVE(DiffRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DiffRgn_FUNC);
/*
	DiffRgn(arg0, arg1, arg2);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jint, jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(DiffRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("DiffRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, DiffRgn_FUNC);
}
#endif

#ifndef NO_DisposeRgn
JNIEXPORT void JNICALL OS_NATIVE(DisposeRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeRgn_FUNC);
/*
	DisposeRgn(arg0);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(DisposeRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("DisposeRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, DisposeRgn_FUNC);
}
#endif

#ifndef NO_EmptyRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EmptyRgn_FUNC);
/*
	rc = (jboolean)EmptyRgn(arg0);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef jboolean (*FPTR)(jint);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(EmptyRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("EmptyRgn"));
			initialized = 1;
		}
		if (fptr) {
			rc = (jboolean)(*fptr)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, EmptyRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_Gestalt
JNIEXPORT jint JNICALL OS_NATIVE(Gestalt)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Gestalt_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)Gestalt(arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, Gestalt_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentProcess
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentProcess)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentProcess_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetCurrentProcess((ProcessSerialNumber *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetCurrentProcess_FUNC);
	return rc;
}
#endif

#ifndef NO_GetRegionBounds
JNIEXPORT void JNICALL OS_NATIVE(GetRegionBounds)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetRegionBounds_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	GetRegionBounds(arg0, lparg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jshort *);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(GetRegionBounds_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("GetRegionBounds"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetRegionBounds_FUNC);
}
#endif

#ifndef NO_LineTo
JNIEXPORT void JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, LineTo_FUNC);
/*
	LineTo(arg0, arg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jshort, jshort);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(LineTo_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("LineTo"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, LineTo_FUNC);
}
#endif

#ifndef NO_MoveTo
JNIEXPORT void JNICALL OS_NATIVE(MoveTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, MoveTo_FUNC);
/*
	MoveTo(arg0, arg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jshort, jshort);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(MoveTo_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("MoveTo"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, MoveTo_FUNC);
}
#endif

#ifndef NO_NSAccessibilityPositionAttribute
JNIEXPORT jint JNICALL OS_NATIVE(NSAccessibilityPositionAttribute)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSAccessibilityPositionAttribute_FUNC);
	rc = (jint)NSAccessibilityPositionAttribute;
	OS_NATIVE_EXIT(env, that, NSAccessibilityPositionAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_NSAccessibilitySizeAttribute
JNIEXPORT jint JNICALL OS_NATIVE(NSAccessibilitySizeAttribute)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSAccessibilitySizeAttribute_FUNC);
	rc = (jint)NSAccessibilitySizeAttribute;
	OS_NATIVE_EXIT(env, that, NSAccessibilitySizeAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_NSBackgroundColorAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSBackgroundColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSBackgroundColorAttributeName_FUNC);
	rc = (jint)NSBackgroundColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSBackgroundColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSBaselineOffsetAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSBaselineOffsetAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSBaselineOffsetAttributeName_FUNC);
	rc = (jint)NSBaselineOffsetAttributeName;
	OS_NATIVE_EXIT(env, that, NSBaselineOffsetAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSBitsPerPixelFromDepth
JNIEXPORT jint JNICALL OS_NATIVE(NSBitsPerPixelFromDepth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSBitsPerPixelFromDepth_FUNC);
	rc = (jint)NSBitsPerPixelFromDepth(arg0);
	OS_NATIVE_EXIT(env, that, NSBitsPerPixelFromDepth_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDefaultRunLoopMode
JNIEXPORT jint JNICALL OS_NATIVE(NSDefaultRunLoopMode)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSDefaultRunLoopMode_FUNC);
	rc = (jint)NSDefaultRunLoopMode;
	OS_NATIVE_EXIT(env, that, NSDefaultRunLoopMode_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDeviceRGBColorSpace
JNIEXPORT jint JNICALL OS_NATIVE(NSDeviceRGBColorSpace)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSDeviceRGBColorSpace_FUNC);
	rc = (jint)NSDeviceRGBColorSpace;
	OS_NATIVE_EXIT(env, that, NSDeviceRGBColorSpace_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDeviceResolution
JNIEXPORT jint JNICALL OS_NATIVE(NSDeviceResolution)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSDeviceResolution_FUNC);
	rc = (jint)NSDeviceResolution;
	OS_NATIVE_EXIT(env, that, NSDeviceResolution_FUNC);
	return rc;
}
#endif

#ifndef NO_NSFileTypeForHFSTypeCode
JNIEXPORT jint JNICALL OS_NATIVE(NSFileTypeForHFSTypeCode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSFileTypeForHFSTypeCode_FUNC);
	rc = (jint)NSFileTypeForHFSTypeCode(arg0);
	OS_NATIVE_EXIT(env, that, NSFileTypeForHFSTypeCode_FUNC);
	return rc;
}
#endif

#ifndef NO_NSFontAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSFontAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSFontAttributeName_FUNC);
	rc = (jint)NSFontAttributeName;
	OS_NATIVE_EXIT(env, that, NSFontAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSForegroundColorAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSForegroundColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSForegroundColorAttributeName_FUNC);
	rc = (jint)NSForegroundColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSForegroundColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSLinkAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSLinkAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSLinkAttributeName_FUNC);
	rc = (jint)NSLinkAttributeName;
	OS_NATIVE_EXIT(env, that, NSLinkAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSParagraphStyleAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSParagraphStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSParagraphStyleAttributeName_FUNC);
	rc = (jint)NSParagraphStyleAttributeName;
	OS_NATIVE_EXIT(env, that, NSParagraphStyleAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSStrikethroughColorAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSStrikethroughColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSStrikethroughColorAttributeName_FUNC);
	rc = (jint)NSStrikethroughColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSStrikethroughColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSStrikethroughStyleAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSStrikethroughStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSStrikethroughStyleAttributeName_FUNC);
	rc = (jint)NSStrikethroughStyleAttributeName;
	OS_NATIVE_EXIT(env, that, NSStrikethroughStyleAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSUnderlineColorAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSUnderlineColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSUnderlineColorAttributeName_FUNC);
	rc = (jint)NSUnderlineColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSUnderlineColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSUnderlineStyleAttributeName
JNIEXPORT jint JNICALL OS_NATIVE(NSUnderlineStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSUnderlineStyleAttributeName_FUNC);
	rc = (jint)NSUnderlineStyleAttributeName;
	OS_NATIVE_EXIT(env, that, NSUnderlineStyleAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NewGlobalRef
JNIEXPORT jint JNICALL OS_NATIVE(NewGlobalRef)
	(JNIEnv *env, jclass that, jobject arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewGlobalRef_FUNC);
	rc = (jint)(*env)->NewGlobalRef(env, (jobject)arg0);
	OS_NATIVE_EXIT(env, that, NewGlobalRef_FUNC);
	return rc;
}
#endif

#ifndef NO_NewRgn
JNIEXPORT jint JNICALL OS_NATIVE(NewRgn)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewRgn_FUNC);
/*
	rc = (jint)NewRgn();
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef jint (*FPTR)();
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(NewRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("NewRgn"));
			initialized = 1;
		}
		if (fptr) {
			rc = (jint)(*fptr)();
		}
	}
	OS_NATIVE_EXIT(env, that, NewRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT void JNICALL OS_NATIVE(OffsetRgn)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, OffsetRgn_FUNC);
/*
	OffsetRgn(arg0, arg1, arg2);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jshort, jshort);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(OffsetRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("OffsetRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, OffsetRgn_FUNC);
}
#endif

#ifndef NO_OpenRgn
JNIEXPORT void JNICALL OS_NATIVE(OpenRgn)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, OpenRgn_FUNC);
/*
	OpenRgn();
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)();
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(OpenRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("OpenRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)();
		}
	}
	OS_NATIVE_EXIT(env, that, OpenRgn_FUNC);
}
#endif

#ifndef NO_PtInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRgn)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1)
{
	jshort *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)PtInRgn(lparg0, arg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef jboolean (*FPTR)(jshort *, jint);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(PtInRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("PtInRgn"));
			initialized = 1;
		}
		if (fptr) {
			rc = (jboolean)(*fptr)(lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PtInRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_QDRegionToRects
JNIEXPORT jint JNICALL OS_NATIVE(QDRegionToRects)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDRegionToRects_FUNC);
/*
	rc = (jint)QDRegionToRects(arg0, arg1, arg2, arg3);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef jint (*FPTR)(jint, jint, jint, jint);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(QDRegionToRects_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("QDRegionToRects"));
			initialized = 1;
		}
		if (fptr) {
			rc = (jint)(*fptr)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, QDRegionToRects_FUNC);
	return rc;
}
#endif

#ifndef NO_RectInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRgn)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1)
{
	jshort *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RectInRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)RectInRgn(lparg0, arg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef jboolean (*FPTR)(jshort *, jint);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(RectInRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("RectInRgn"));
			initialized = 1;
		}
		if (fptr) {
			rc = (jboolean)(*fptr)(lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, RectInRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_RectRgn
JNIEXPORT void JNICALL OS_NATIVE(RectRgn)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, RectRgn_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	RectRgn(arg0, lparg1);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jshort *);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(RectRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("RectRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RectRgn_FUNC);
}
#endif

#ifndef NO_SectRgn
JNIEXPORT void JNICALL OS_NATIVE(SectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, SectRgn_FUNC);
/*
	SectRgn(arg0, arg1, arg2);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jint, jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(SectRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("SectRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, SectRgn_FUNC);
}
#endif

#ifndef NO_SetFrontProcess
JNIEXPORT jint JNICALL OS_NATIVE(SetFrontProcess)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetFrontProcess_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)SetFrontProcess((ProcessSerialNumber *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SetFrontProcess_FUNC);
	return rc;
}
#endif

#ifndef NO_SetRect
JNIEXPORT void JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jshortArray arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	jshort *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, SetRect_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	SetRect(lparg0, arg1, arg2, arg3, arg4);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jshort *, jshort, jshort, jshort, jshort);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(SetRect_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("SetRect"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(lparg0, arg1, arg2, arg3, arg4);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SetRect_FUNC);
}
#endif

#ifndef NO_TransformProcessType
JNIEXPORT jint JNICALL OS_NATIVE(TransformProcessType)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TransformProcessType_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)TransformProcessType((ProcessSerialNumber *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, TransformProcessType_FUNC);
	return rc;
}
#endif

#ifndef NO_UnionRgn
JNIEXPORT void JNICALL OS_NATIVE(UnionRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, UnionRgn_FUNC);
/*
	UnionRgn(arg0, arg1, arg2);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jint, jint, jint);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(UnionRgn_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("UnionRgn"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, UnionRgn_FUNC);
}
#endif

#ifndef NO_class_1addIvar
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addIvar)
	(JNIEnv *env, jclass that, jint arg0, jstring arg1, jint arg2, jbyte arg3, jstring arg4)
{
	const char *lparg1= NULL;
	const char *lparg4= NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addIvar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetStringUTFChars(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)class_addIvar((Class)arg0, (const char *)lparg1, (size_t)arg2, arg3, (const char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseStringUTFChars(env, arg4, lparg4);
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, class_1addIvar_FUNC);
	return rc;
}
#endif

#ifndef NO_class_1addMethod
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addMethod)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jstring arg3)
{
	const char *lparg3= NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addMethod_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetStringUTFChars(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)class_addMethod((Class)arg0, (SEL)arg1, (IMP)arg2, (const char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseStringUTFChars(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, class_1addMethod_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NSRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg1) if ((lparg1 = getNSRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
}
#endif

#ifndef NO_objc_1allocateClassPair
JNIEXPORT jint JNICALL OS_NATIVE(objc_1allocateClassPair)
	(JNIEnv *env, jclass that, jint arg0, jstring arg1, jint arg2)
{
	const char *lparg1= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1allocateClassPair_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)objc_allocateClassPair((Class)arg0, (const char *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, objc_1allocateClassPair_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1getClass
JNIEXPORT jint JNICALL OS_NATIVE(objc_1getClass)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1getClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)objc_getClass((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1getClass_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1lookUpClass
JNIEXPORT jint JNICALL OS_NATIVE(objc_1lookUpClass)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1lookUpClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)objc_lookUpClass((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1lookUpClass_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_FUNC);
	rc = (jint)((jint (*)(id, SEL))objc_msgSend)((id)arg0, (SEL)arg1);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIB
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIB)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIB_FUNC);
	rc = (jint)((jint (*)(id, SEL, jbyte))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIB_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IID
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IID_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IID_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIDD
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIDD)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jdouble arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIDD_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble, jdouble))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIDD_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIDI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIDI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIDI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIDI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIDIIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIDIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jint arg3, jint arg4, jint arg5, jboolean arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIDIIIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble, jint, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIDIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIDIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIDIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jint arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIDIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIDIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFFFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFFFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFFFF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jfloat, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFFFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFFFFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFFFFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFFFFF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jfloat, jfloat, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFFFFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jint arg3, jfloat arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__III
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__III_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIDIIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIDIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jint arg5, jint arg6, jboolean arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIDIIIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jdouble, jint, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIDIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIFFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIFFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIFFF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jfloat, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIFFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIFI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIFI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIFI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jfloat, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIFI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIFILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIFILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jint arg4, jobject arg5)
{
	NSPoint _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIFILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg5) if ((lparg5 = getNSPointFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jfloat, jint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSPointFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIFILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIID
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIID_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jdouble))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIID_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIDI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIDI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jdouble arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIDI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jdouble, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIDI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jfloat arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIFII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIFII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jfloat arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIFII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jfloat, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIFII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jfloat arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jobject arg7)
{
	NSPoint _arg7, *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg7) if ((lparg7 = getNSPointFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, *lparg7);
fail:
	if (arg7 && lparg7) setNSPointFields(env, arg7, lparg7);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jobject arg7, jint arg8)
{
	NSRange _arg7, *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg7) if ((lparg7 = getNSRangeFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, *lparg7, arg8);
fail:
	if (arg7 && lparg7) setNSRangeFields(env, arg7, lparg7);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIIZZIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIZZIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jboolean arg7, jboolean arg8, jint arg9, jint arg10, jint arg11)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIZZIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, jboolean, jboolean, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIZZIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIIZZIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIZZIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jboolean arg7, jboolean arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIZZIIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, jint, jboolean, jboolean, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIZZIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIILorg_eclipse_swt_internal_cocoa_NSRect_2FFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIILorg_eclipse_swt_internal_cocoa_NSRect_2FFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jobject arg6, jfloat arg7, jfloat arg8, jfloat arg9)
{
	NSRect _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIILorg_eclipse_swt_internal_cocoa_NSRect_2FFF_FUNC);
	if (arg6) if ((lparg6 = getNSRectFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint, NSRect, jfloat, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, *lparg6, arg7, arg8, arg9);
fail:
	if (arg6 && lparg6) setNSRectFields(env, arg6, lparg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIILorg_eclipse_swt_internal_cocoa_NSRect_2FFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5)
{
	NSPoint _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg5) if ((lparg5 = getNSPointFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSPointFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5)
{
	NSRange _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jint arg6)
{
	NSRange _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, *lparg5, arg6);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIISI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIISI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIISI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jshort, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIISI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIZI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIJ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlong arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIJ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jlong))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIJ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSPoint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jobject arg6)
{
	NSRange _arg4, *lparg4=NULL;
	NSRange _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSRangeFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRange, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, arg5, *lparg6);
fail:
	if (arg6 && lparg6) setNSRangeFields(env, arg6, lparg6);
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	NSRange _arg4, *lparg4=NULL;
	NSPoint _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSPointFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRange, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSPointFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5, jint arg6, jint arg7, jobject arg8)
{
	NSRange _arg4, *lparg4=NULL;
	NSPoint _arg5, *lparg5=NULL;
	NSSize _arg8, *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSPointFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getNSSizeFields(env, arg8, &_arg8)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRange, NSPoint, jint, jint, NSSize))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, *lparg5, arg6, arg7, *lparg8);
fail:
	if (arg8 && lparg8) setNSSizeFields(env, arg8, lparg8);
	if (arg5 && lparg5) setNSPointFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	NSRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	NSRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, NSRect, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, *lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIS
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshort arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIS_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jshort))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIS_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIZI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIZII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIIZII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIZII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jint, jboolean, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIZII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jdouble arg5, jint arg6, jint arg7, jint arg8, jint arg9, jfloat arg10)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIF_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint, jdouble, jint, jint, jint, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jdouble arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIII_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint, jdouble, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIZS
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIZS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jdouble arg5, jint arg6, jint arg7, jint arg8, jint arg9, jboolean arg10, jshort arg11)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIZS_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint, jdouble, jint, jint, jint, jint, jboolean, jshort))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIIZS_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jdouble arg5, jint arg6, jint arg7, jshort arg8, jint arg9, jint arg10)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint, jdouble, jint, jint, jshort, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4)
{
	NSPoint _arg3, *lparg3=NULL;
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jint arg6, jint arg7, jboolean arg8)
{
	NSPoint _arg3, *lparg3=NULL;
	NSSize _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSSizeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, NSSize, jint, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, *lparg4, arg5, arg6, arg7, arg8);
fail:
	if (arg4 && lparg4) setNSSizeFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Z
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jboolean arg4)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Z_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSPoint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Z_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRange, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2IZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jboolean arg5)
{
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2IZ_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRange, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4)
{
	NSRange _arg3, *lparg3=NULL;
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRange, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jboolean arg6)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jobject arg5, jint arg6)
{
	NSRect _arg3, *lparg3=NULL;
	NSRange _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, *lparg5, arg6);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jboolean arg5)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZ_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jboolean arg5, jint arg6)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jboolean arg4, jint arg5)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ZI_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSRect, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2ZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSSize _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSSize))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2Z
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jboolean arg4)
{
	NSSize _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2Z_FUNC);
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, NSSize, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSSize_2Z_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIS
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIS_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jshort))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIS_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIISZZZZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIISZZZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshort arg3, jboolean arg4, jboolean arg5, jboolean arg6, jboolean arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIISZZZZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jshort, jboolean, jboolean, jboolean, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIISZZZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZIII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIZZII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIZZII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZZII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jint, jboolean, jboolean, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZZII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIJ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIJ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jlong))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIJ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIJI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIJI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jlong, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIJI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIJSZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIJSZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jshort arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIJSZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jlong, jshort, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIJSZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILjava_lang_String_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILjava_lang_String_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jstring arg2)
{
	const char *lparg2= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILjava_lang_String_2_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetStringUTFChars(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseStringUTFChars(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILjava_lang_String_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSAffineTransformStruct _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
	if (arg2) if ((lparg2 = getNSAffineTransformStructFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSAffineTransformStruct))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSAffineTransformStructFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSDecimal_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSDecimal_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSDecimal _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSDecimal_2_FUNC);
	if (arg2) if ((lparg2 = getNSDecimalFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSDecimal))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSDecimalFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSDecimal_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2F_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jfloat, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3, jfloat arg4, jfloat arg5, jboolean arg6)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jfloat, jfloat, jfloat, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FLorg_eclipse_swt_internal_cocoa_NSPoint_2FI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FLorg_eclipse_swt_internal_cocoa_NSPoint_2FI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3, jobject arg4, jfloat arg5, jint arg6)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FLorg_eclipse_swt_internal_cocoa_NSPoint_2FI_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jfloat, NSPoint, jfloat, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FLorg_eclipse_swt_internal_cocoa_NSPoint_2FI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2IF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2IF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jfloat arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2IF_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2IF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jfloatArray arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	jfloat *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, jint, jfloat *))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jfloat arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2F_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSPoint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSPoint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jboolean arg5)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IZ_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSPoint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jobject arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSPoint, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jfloat arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSRect, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jfloat arg5)
{
	NSPoint _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint, NSRect, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IFLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IFLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jfloat arg4, jobject arg5, jobject arg6, jobject arg7)
{
	NSRange _arg2, *lparg2=NULL;
	NSRect _arg5, *lparg5=NULL;
	NSRange _arg6, *lparg6=NULL;
	NSPoint _arg7, *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IFLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSRangeFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getNSPointFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jfloat, NSRect, NSRange, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, *lparg5, *lparg6, *lparg7);
fail:
	if (arg7 && lparg7) setNSPointFields(env, arg7, lparg7);
	if (arg6 && lparg6) setNSRangeFields(env, arg6, lparg6);
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IFLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2III
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIII_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIIII_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4)
{
	NSRange _arg2, *lparg2=NULL;
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jobject arg5, jobject arg6)
{
	NSRange _arg2, *lparg2=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSRange _arg5, *lparg5=NULL;
	NSPoint _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSPointFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, NSRect, NSRange, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4, *lparg5, *lparg6);
fail:
	if (arg6 && lparg6) setNSPointFields(env, arg6, lparg6);
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jboolean arg4)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IZ_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSRange _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSRange _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jint arg5)
{
	NSRange _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, NSRange, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3, jint arg4)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ZI_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRange, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2ZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3, jfloat arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2III
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIIII_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint, jint, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jboolean arg5)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jboolean arg5, jint arg6)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4)
{
	NSRect _arg2, *lparg2=NULL;
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jint arg5)
{
	NSRect _arg2, *lparg2=NULL;
	NSRange _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, NSRange, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jboolean arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZ_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jboolean arg4, jint arg5)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jint, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSRect _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSPoint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jobject arg4)
{
	NSRect _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	NSRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSRange, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jobject arg4, jfloat arg5)
{
	NSRect _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	NSRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSRange, NSRect, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4)
{
	NSRect _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jint arg4, jfloat arg5)
{
	NSRect _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSRect, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSRect _arg2, *lparg2=NULL;
	NSSize _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, NSSize))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ZZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jboolean arg3, jboolean arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ZZ_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect, jboolean, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSSize))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSSize, jint))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2IZZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2IZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jboolean arg4, jboolean arg5)
{
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2IZZ_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSSize, jint, jboolean, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2IZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	NSSize _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSSize, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIS
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIS_FUNC);
	rc = (jint)((jint (*)(id, SEL, jshort))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIS_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IISI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IISI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IISI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jshort, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IISI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZI_FUNC);
	rc = (jint)((jint (*)(id, SEL, jboolean, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZII_FUNC);
	rc = (jint)((jint (*)(id, SEL, jboolean, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jboolean, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jboolean, NSRect))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3, jint arg4)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jboolean, NSRect, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIZZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZZ_FUNC);
	rc = (jint)((jint (*)(id, SEL, jboolean, jboolean))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3C
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3C_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3CI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3CI_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jobject arg3)
{
	jchar *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jchar *, NSRange))objc_msgSend)((id)arg0, (SEL)arg1, lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3F
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3F_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jfloat *))objc_msgSend)((id)arg0, (SEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3F_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3FIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3FIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloat arg4)
{
	jfloat *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3FIF_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jfloat *, jint, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3FIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint *))objc_msgSend)((id)arg0, (SEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3III
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3III_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint *, jint, jint))objc_msgSend)((id)arg0, (SEL)arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	struct objc_super _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL))objc_msgSendSuper)((id)lparg0, (SEL)arg1);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	struct objc_super _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint))objc_msgSendSuper)((id)lparg0, (SEL)arg1, arg2);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIII
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	struct objc_super _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIII_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jint))objc_msgSendSuper)((id)lparg0, (SEL)arg1, arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2)
{
	struct objc_super _arg0, *lparg0=NULL;
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSPoint))objc_msgSendSuper)((id)lparg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2)
{
	struct objc_super _arg0, *lparg0=NULL;
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSRect))objc_msgSendSuper)((id)lparg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2)
{
	struct objc_super _arg0, *lparg0=NULL;
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, NSSize))objc_msgSendSuper)((id)lparg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__II
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__II_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL))objc_msgSend_fpret)((id)arg0, (SEL)arg1);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IID
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IID_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jdouble))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IID_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIF
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIF_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jfloat))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__III
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__III_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIIF
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIIF_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint, jfloat))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIIFI
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIIFI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3, jint arg4)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIIFI_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint, jfloat, jint))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIIFI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIII
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIII_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint, jint))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIIIF
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jfloat arg4)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIIIF_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint, jint, jfloat))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIIIZF
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIIIZF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4, jfloat arg5)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIIIZF_FUNC);
	rc = (jdouble)((jdouble (*)(id, SEL, jint, jint, jboolean, jfloat))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIIIZF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IIILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRect _arg3, *lparg3=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jdouble)((jdouble (*)(id, SEL, jint, NSRect))objc_msgSend_fpret)((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSPoint _arg2, *lparg2=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jdouble)((jdouble (*)(id, SEL, NSPoint))objc_msgSend_fpret)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jdouble)((jdouble (*)(id, SEL, NSPoint, jint))objc_msgSend_fpret)((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSRect _arg2, *lparg2=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jdouble)((jdouble (*)(id, SEL, NSRect))objc_msgSend_fpret)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSSize _arg2, *lparg2=NULL;
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jdouble)((jdouble (*)(id, SEL, NSSize))objc_msgSend_fpret)((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSAffineTransformStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
	if (arg0) if ((lparg0 = getNSAffineTransformStructFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSAffineTransformStruct *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSAffineTransformStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSDecimal_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSDecimal_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSDecimal _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSDecimal_2II_FUNC);
	if (arg0) if ((lparg0 = getNSDecimalFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSDecimal *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSDecimalFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSDecimal_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIII_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIIIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIIIII_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, jint, jint, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIIIIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	NSPoint _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, jint, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIISLorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIISLorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jshort arg4, jobject arg5)
{
	NSPoint _arg0, *lparg0=NULL;
	NSRect _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIISLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, jint, jshort, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IIISLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, NSPoint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, NSPoint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSPoint *, id, SEL, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIII_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIII_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIII_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5, arg6);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIIZ
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jboolean arg7)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIIZ_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, jint, jint, jboolean))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIIZ_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIZII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIZII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6, jint arg7, jint arg8)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIZII_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, jint, jboolean, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIIIZII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jint arg6)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, jint, NSRange, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, *lparg5, arg6);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIIILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg4, *lparg4=NULL;
	NSRange _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRangeFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, jint, NSRange, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSRangeFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, NSRange, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, NSRange, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRange _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRange _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRange *, id, SEL, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIII_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, jint, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, *lparg5);
fail:
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jobject arg6, jint arg7)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg5, *lparg5=NULL;
	NSPoint _arg6, *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSPointFields(env, arg6, &_arg6)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, jint, NSRect, NSPoint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, *lparg5, *lparg6, arg7);
fail:
	if (arg6 && lparg6) setNSPointFields(env, arg6, lparg6);
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, jint, jboolean))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5, jint arg6, jobject arg7)
{
	NSRect _arg0, *lparg0=NULL;
	NSPoint _arg4, *lparg4=NULL;
	NSRect _arg5, *lparg5=NULL;
	NSRange _arg7, *lparg7=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getNSRangeFields(env, arg7, &_arg7)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSPoint, NSRect, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, *lparg5, arg6, *lparg7);
fail:
	if (arg7 && lparg7) setNSRangeFields(env, arg7, lparg7);
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSRange _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, arg5);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5, jint arg6)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSPoint _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSPointFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRect, NSPoint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, *lparg5, arg6);
fail:
	if (arg5 && lparg5) setNSPointFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5, jint arg6)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSRect _arg5, *lparg5=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRect, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, *lparg5, arg6);
fail:
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5, jint arg6, jobject arg7)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSRect _arg5, *lparg5=NULL;
	NSRange _arg7, *lparg7=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getNSRectFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getNSRangeFields(env, arg7, &_arg7)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jint, NSRect, NSRect, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, *lparg5, arg6, *lparg7);
fail:
	if (arg7 && lparg7) setNSRangeFields(env, arg7, lparg7);
	if (arg5 && lparg5) setNSRectFields(env, arg5, lparg5);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jobject arg6)
{
	NSRect _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSRange _arg6, *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSRangeFields(env, arg6, &_arg6)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSPoint, NSRect, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, *lparg4, arg5, *lparg6);
fail:
	if (arg6 && lparg6) setNSRangeFields(env, arg6, lparg6);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRect _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRange, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2III
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jint arg6)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRect, jint, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jobject arg6)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	NSRect _arg4, *lparg4=NULL;
	NSRange _arg6, *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getNSRangeFields(env, arg6, &_arg6)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSRect, NSRect, jint, NSRange))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, *lparg4, arg5, *lparg6);
fail:
	if (arg6 && lparg6) setNSRangeFields(env, arg6, lparg6);
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2ILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSSize, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	NSRect _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, NSSize, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIZ
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jboolean arg3)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSRect *, id, SEL, jboolean))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIF
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIF)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jfloat arg3)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIF_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jfloat))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIF_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2III
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2III_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIII
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIII)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIII_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	NSSize _arg0, *lparg0=NULL;
	NSRange _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jint, NSRange, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSSizeFields(env, arg4, &_arg4)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jint, NSSize))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSSizeFields(env, arg4, lparg4);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSSize _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, NSSize))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, NSSize, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jboolean arg4, jboolean arg5, jint arg6)
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, NSSize, jboolean, jboolean, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIZ
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jboolean arg3)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIZ_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	((void (*)(NSSize *, id, SEL, jboolean))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIZ_FUNC);
}
#endif

#ifndef NO_objc_1registerClassPair
JNIEXPORT void JNICALL OS_NATIVE(objc_1registerClassPair)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, objc_1registerClassPair_FUNC);
	objc_registerClassPair((Class)arg0);
	OS_NATIVE_EXIT(env, that, objc_1registerClassPair_FUNC);
}
#endif

#ifndef NO_object_1getClassName
JNIEXPORT jint JNICALL OS_NATIVE(object_1getClassName)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, object_1getClassName_FUNC);
	rc = (jint)object_getClassName((id)arg0);
	OS_NATIVE_EXIT(env, that, object_1getClassName_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1getInstanceVariable
JNIEXPORT jint JNICALL OS_NATIVE(object_1getInstanceVariable)
	(JNIEnv *env, jclass that, jint arg0, jstring arg1, jintArray arg2)
{
	const char *lparg1= NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, object_1getInstanceVariable_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)object_getInstanceVariable((id)arg0, (const char *)lparg1, (void **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, object_1getInstanceVariable_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1setInstanceVariable
JNIEXPORT jint JNICALL OS_NATIVE(object_1setInstanceVariable)
	(JNIEnv *env, jclass that, jint arg0, jstring arg1, jint arg2)
{
	const char *lparg1= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, object_1setInstanceVariable_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)object_setInstanceVariable((id)arg0, (const char *)lparg1, (void *)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, object_1setInstanceVariable_FUNC);
	return rc;
}
#endif

#ifndef NO_sel_1registerName
JNIEXPORT jint JNICALL OS_NATIVE(sel_1registerName)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, sel_1registerName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)sel_registerName((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, sel_1registerName_FUNC);
	return rc;
}
#endif


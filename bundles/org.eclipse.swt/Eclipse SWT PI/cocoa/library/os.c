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

#ifndef NO_CGPoint_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CGPoint_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPoint_1sizeof_FUNC);
	rc = (jint)CGPoint_sizeof();
	OS_NATIVE_EXIT(env, that, CGPoint_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CGRect_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CGRect_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGRect_1sizeof_FUNC);
	rc = (jint)CGRect_sizeof();
	OS_NATIVE_EXIT(env, that, CGRect_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CGSize_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(CGSize_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGSize_1sizeof_FUNC);
	rc = (jint)CGSize_sizeof();
	OS_NATIVE_EXIT(env, that, CGSize_1sizeof_FUNC);
	return rc;
}
#endif

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

#ifndef NO_NSAffineTransformStruct_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NSAffineTransformStruct_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSAffineTransformStruct_1sizeof_FUNC);
	rc = (jint)NSAffineTransformStruct_sizeof();
	OS_NATIVE_EXIT(env, that, NSAffineTransformStruct_1sizeof_FUNC);
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

#ifndef NO_NSCalibratedRGBColorSpace
JNIEXPORT jint JNICALL OS_NATIVE(NSCalibratedRGBColorSpace)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSCalibratedRGBColorSpace_FUNC);
	rc = (jint)NSCalibratedRGBColorSpace;
	OS_NATIVE_EXIT(env, that, NSCalibratedRGBColorSpace_FUNC);
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

#ifndef NO_NSErrorFailingURLStringKey
JNIEXPORT jint JNICALL OS_NATIVE(NSErrorFailingURLStringKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSErrorFailingURLStringKey_FUNC);
	rc = (jint)NSErrorFailingURLStringKey;
	OS_NATIVE_EXIT(env, that, NSErrorFailingURLStringKey_FUNC);
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

#ifndef NO_NSFilenamesPboardType
JNIEXPORT jint JNICALL OS_NATIVE(NSFilenamesPboardType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSFilenamesPboardType_FUNC);
	rc = (jint)NSFilenamesPboardType;
	OS_NATIVE_EXIT(env, that, NSFilenamesPboardType_FUNC);
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

#ifndef NO_NSGetSizeAndAlignment
JNIEXPORT jint JNICALL OS_NATIVE(NSGetSizeAndAlignment)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSGetSizeAndAlignment_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)NSGetSizeAndAlignment((char*)arg0, (NSUInteger*)lparg1, (NSUInteger*)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, NSGetSizeAndAlignment_FUNC);
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

#ifndef NO_NSPoint_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NSPoint_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPoint_1sizeof_FUNC);
	rc = (jint)NSPoint_sizeof();
	OS_NATIVE_EXIT(env, that, NSPoint_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintAllPages
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintAllPages)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintAllPages_FUNC);
	rc = (jint)NSPrintAllPages;
	OS_NATIVE_EXIT(env, that, NSPrintAllPages_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintCopies
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintCopies)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintCopies_FUNC);
	rc = (jint)NSPrintCopies;
	OS_NATIVE_EXIT(env, that, NSPrintCopies_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintFirstPage
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintFirstPage)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintFirstPage_FUNC);
	rc = (jint)NSPrintFirstPage;
	OS_NATIVE_EXIT(env, that, NSPrintFirstPage_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintJobDisposition
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintJobDisposition)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintJobDisposition_FUNC);
	rc = (jint)NSPrintJobDisposition;
	OS_NATIVE_EXIT(env, that, NSPrintJobDisposition_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintLastPage
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintLastPage)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintLastPage_FUNC);
	rc = (jint)NSPrintLastPage;
	OS_NATIVE_EXIT(env, that, NSPrintLastPage_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintMustCollate
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintMustCollate)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintMustCollate_FUNC);
	rc = (jint)NSPrintMustCollate;
	OS_NATIVE_EXIT(env, that, NSPrintMustCollate_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintPreviewJob
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintPreviewJob)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintPreviewJob_FUNC);
	rc = (jint)NSPrintPreviewJob;
	OS_NATIVE_EXIT(env, that, NSPrintPreviewJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSaveJob
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintSaveJob)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSaveJob_FUNC);
	rc = (jint)NSPrintSaveJob;
	OS_NATIVE_EXIT(env, that, NSPrintSaveJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSavePath
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintSavePath)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSavePath_FUNC);
	rc = (jint)NSPrintSavePath;
	OS_NATIVE_EXIT(env, that, NSPrintSavePath_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSpoolJob
JNIEXPORT jint JNICALL OS_NATIVE(NSPrintSpoolJob)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSpoolJob_FUNC);
	rc = (jint)NSPrintSpoolJob;
	OS_NATIVE_EXIT(env, that, NSPrintSpoolJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSRTFPboardType
JNIEXPORT jint JNICALL OS_NATIVE(NSRTFPboardType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSRTFPboardType_FUNC);
	rc = (jint)NSRTFPboardType;
	OS_NATIVE_EXIT(env, that, NSRTFPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSRange_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NSRange_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSRange_1sizeof_FUNC);
	rc = (jint)NSRange_sizeof();
	OS_NATIVE_EXIT(env, that, NSRange_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NSRect_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NSRect_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSRect_1sizeof_FUNC);
	rc = (jint)NSRect_sizeof();
	OS_NATIVE_EXIT(env, that, NSRect_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_NSSearchPathForDirectoriesInDomains
JNIEXPORT jint JNICALL OS_NATIVE(NSSearchPathForDirectoriesInDomains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSSearchPathForDirectoriesInDomains_FUNC);
	rc = (jint)NSSearchPathForDirectoriesInDomains(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, NSSearchPathForDirectoriesInDomains_FUNC);
	return rc;
}
#endif

#ifndef NO_NSSize_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(NSSize_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSSize_1sizeof_FUNC);
	rc = (jint)NSSize_sizeof();
	OS_NATIVE_EXIT(env, that, NSSize_1sizeof_FUNC);
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

#ifndef NO_NSStringPboardType
JNIEXPORT jint JNICALL OS_NATIVE(NSStringPboardType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSStringPboardType_FUNC);
	rc = (jint)NSStringPboardType;
	OS_NATIVE_EXIT(env, that, NSStringPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSTIFFPboardType
JNIEXPORT jint JNICALL OS_NATIVE(NSTIFFPboardType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSTIFFPboardType_FUNC);
	rc = (jint)NSTIFFPboardType;
	OS_NATIVE_EXIT(env, that, NSTIFFPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSTemporaryDirectory
JNIEXPORT jint JNICALL OS_NATIVE(NSTemporaryDirectory)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSTemporaryDirectory_FUNC);
	rc = (jint)NSTemporaryDirectory();
	OS_NATIVE_EXIT(env, that, NSTemporaryDirectory_FUNC);
	return rc;
}
#endif

#ifndef NO_NSURLPboardType
JNIEXPORT jint JNICALL OS_NATIVE(NSURLPboardType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NSURLPboardType_FUNC);
	rc = (jint)NSURLPboardType;
	OS_NATIVE_EXIT(env, that, NSURLPboardType_FUNC);
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

#ifndef NO_class_1addProtocol
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addProtocol)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addProtocol_FUNC);
	rc = (jboolean)class_addProtocol((Class)arg0, (Protocol *)arg1);
	OS_NATIVE_EXIT(env, that, class_1addProtocol_FUNC);
	return rc;
}
#endif

#ifndef NO_instrumentObjcMessageSends
JNIEXPORT void JNICALL OS_NATIVE(instrumentObjcMessageSends)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	OS_NATIVE_ENTER(env, that, instrumentObjcMessageSends_FUNC);
/*
	instrumentObjcMessageSends(arg0);
*/
	{
		static int initialized = 0;
		static CFBundleRef bundle = NULL;
		typedef void (*FPTR)(jboolean);
		static FPTR fptr;
		if (!initialized) {
			if (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(instrumentObjcMessageSends_LIB));
			if (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR("instrumentObjcMessageSends"));
			initialized = 1;
		}
		if (fptr) {
			(*fptr)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, instrumentObjcMessageSends_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGPoint _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGSize _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I_FUNC);
	if (arg1) if ((lparg1 = getCGSizeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGSizeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NSAffineTransformStruct _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I_FUNC);
	if (arg1) if ((lparg1 = getNSAffineTransformStructFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSAffineTransformStructFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NSPoint _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg1) if ((lparg1 = getNSPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NSRange _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg1) if ((lparg1 = getNSRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSRangeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
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

#ifndef NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	NSSize _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
	if (arg1) if ((lparg1 = getNSSizeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSSizeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II_FUNC);
	if (arg0) if ((lparg0 = getCGSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSAffineTransformStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
	if (arg0) if ((lparg0 = getNSAffineTransformStructFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSAffineTransformStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
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

#ifndef NO_objc_1getProtocol
JNIEXPORT jint JNICALL OS_NATIVE(objc_1getProtocol)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1getProtocol_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)objc_getProtocol(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1getProtocol_FUNC);
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

#ifndef NO_objc_1msgSend__II_3B
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3B_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jbyte *))objc_msgSend)((id)arg0, (SEL)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3BI
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__II_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3BI_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jbyte *, jint))objc_msgSend)((id)arg0, (SEL)arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3BI_FUNC);
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

#ifndef NO_objc_1msgSend__JJFD
JNIEXPORT jlong JNICALL OS_NATIVE(objc_1msgSend__JJFD)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jfloat arg2, jdouble arg3)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJFD_FUNC);
	rc = (jlong)((jlong (*)(id, SEL, jfloat, jdouble))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJFD_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__JJI
JNIEXPORT jlong JNICALL OS_NATIVE(objc_1msgSend__JJI)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jint arg2)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJI_FUNC);
	rc = (jlong)((jlong (*)(id, SEL, jint))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJI_FUNC);
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

#ifndef NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5)
{
	struct objc_super _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ_FUNC);
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)((jint (*)(id, SEL, jint, jint, jint, jboolean))objc_msgSendSuper)((id)lparg0, (SEL)arg1, arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ_FUNC);
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

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSAffineTransformStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
	if (arg0) if ((lparg0 = getNSAffineTransformStructFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSAffineTransformStruct *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSAffineTransformStruct (*)(id, SEL))objc_msgSend)((id)arg1, (SEL)arg2);
	}
fail:
	if (arg0 && lparg0) setNSAffineTransformStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSPoint (*)(id, SEL))objc_msgSend)((id)arg1, (SEL)arg2);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
	} else {
		*lparg0 = (*(NSPoint (*)(id, SEL, jint))objc_msgSend)((id)arg1, (SEL)arg2, arg3);
	}
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, id, SEL, NSPoint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
	} else {
		*lparg0 = (*(NSPoint (*)(id, SEL, NSPoint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, id, SEL, NSPoint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSPoint (*)(id, SEL, NSPoint, jint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRange *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSRange (*)(id, SEL))objc_msgSend)((id)arg1, (SEL)arg2);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRange *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
	} else {
		*lparg0 = (*(NSRange (*)(id, SEL, jint))objc_msgSend)((id)arg1, (SEL)arg2, arg3);
	}
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL))objc_msgSend)((id)arg1, (SEL)arg2);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, jint))objc_msgSend)((id)arg1, (SEL)arg2, arg3);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, jint, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, jint, jint))objc_msgSend)((id)arg1, (SEL)arg2, arg3, arg4);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5)
{
	NSRect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, jint, jint, jboolean))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, arg3, arg4, arg5);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, jint, jint, jboolean))objc_msgSend)((id)arg1, (SEL)arg2, arg3, arg4, arg5);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, NSRange, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, NSRange, jint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3, arg4);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, NSRect))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, NSRect))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, id, SEL, NSRect, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(id, SEL, NSRect, jint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, id, SEL))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSSize (*)(id, SEL))objc_msgSend)((id)arg1, (SEL)arg2);
	}
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, id, SEL, NSSize))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3);
	} else {
		*lparg0 = (*(NSSize (*)(id, SEL, NSSize))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, id, SEL, NSSize, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSSize (*)(id, SEL, NSSize, jint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3, arg4);
	}
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
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, id, SEL, NSSize, jboolean, jboolean, jint))objc_msgSend_stret)(lparg0, (id)arg1, (SEL)arg2, *lparg3, arg4, arg5, arg6);
	} else {
		*lparg0 = (*(NSSize (*)(id, SEL, NSSize, jboolean, jboolean, jint))objc_msgSend)((id)arg1, (SEL)arg2, *lparg3, arg4, arg5, arg6);
	}
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
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

#ifndef NO_objc_1super_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(objc_1super_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1super_1sizeof_FUNC);
	rc = (jint)objc_super_sizeof();
	OS_NATIVE_EXIT(env, that, objc_1super_1sizeof_FUNC);
	return rc;
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

#ifndef NO_object_1setClass
JNIEXPORT jint JNICALL OS_NATIVE(object_1setClass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, object_1setClass_FUNC);
	rc = (jint)object_setClass((id)arg0, (Class)arg1);
	OS_NATIVE_EXIT(env, that, object_1setClass_FUNC);
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


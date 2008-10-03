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

#ifndef NO_CFRelease
JNIEXPORT void JNICALL OS_NATIVE(CFRelease)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, CFRelease_FUNC);
	CFRelease((CFTypeRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRelease_FUNC);
}
#endif

#ifndef NO_CFURLCreateStringByAddingPercentEscapes
JNIEXPORT jintLong JNICALL OS_NATIVE(CFURLCreateStringByAddingPercentEscapes)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jint arg4)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateStringByAddingPercentEscapes_FUNC);
	rc = (jintLong)CFURLCreateStringByAddingPercentEscapes((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (CFStringRef)arg3, (CFStringEncoding)arg4);
	OS_NATIVE_EXIT(env, that, CFURLCreateStringByAddingPercentEscapes_FUNC);
	return rc;
}
#endif

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

#ifndef NO_CPSSetProcessName
JNIEXPORT jint JNICALL OS_NATIVE(CPSSetProcessName)
	(JNIEnv *env, jclass that, jintArray arg0, jintLong arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CPSSetProcessName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CPSSetProcessName(lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CPSSetProcessName_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseRgn
JNIEXPORT void JNICALL OS_NATIVE(CloseRgn)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, CloseRgn_FUNC);
/*
	CloseRgn(arg0);
*/
	{
		LOAD_FUNCTION(fp, CloseRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, CloseRgn_FUNC);
}
#endif

#ifndef NO_CopyRgn
JNIEXPORT void JNICALL OS_NATIVE(CopyRgn)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, CopyRgn_FUNC);
/*
	CopyRgn(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, CopyRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, CopyRgn_FUNC);
}
#endif

#ifndef NO_DeleteGlobalRef
JNIEXPORT void JNICALL OS_NATIVE(DeleteGlobalRef)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, DeleteGlobalRef_FUNC);
	(*env)->DeleteGlobalRef(env, (jobject)arg0);
	OS_NATIVE_EXIT(env, that, DeleteGlobalRef_FUNC);
}
#endif

#ifndef NO_DiffRgn
JNIEXPORT void JNICALL OS_NATIVE(DiffRgn)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, DiffRgn_FUNC);
/*
	DiffRgn(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, DiffRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, DiffRgn_FUNC);
}
#endif

#ifndef NO_DisposeRgn
JNIEXPORT void JNICALL OS_NATIVE(DisposeRgn)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeRgn_FUNC);
/*
	DisposeRgn(arg0);
*/
	{
		LOAD_FUNCTION(fp, DisposeRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, DisposeRgn_FUNC);
}
#endif

#ifndef NO_EmptyRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRgn)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EmptyRgn_FUNC);
/*
	rc = (jboolean)EmptyRgn(arg0);
*/
	{
		LOAD_FUNCTION(fp, EmptyRgn)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong))fp)(arg0);
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
	(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetRegionBounds_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	GetRegionBounds(arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, GetRegionBounds)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jshort *))fp)(arg0, lparg1);
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
		LOAD_FUNCTION(fp, LineTo)
		if (fp) {
			((void (CALLING_CONVENTION*)(jshort, jshort))fp)(arg0, arg1);
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
		LOAD_FUNCTION(fp, MoveTo)
		if (fp) {
			((void (CALLING_CONVENTION*)(jshort, jshort))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, MoveTo_FUNC);
}
#endif

#ifndef NO_NSAccessibilityPositionAttribute
JNIEXPORT jintLong JNICALL OS_NATIVE(NSAccessibilityPositionAttribute)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSAccessibilityPositionAttribute_FUNC);
	rc = (jintLong)NSAccessibilityPositionAttribute;
	OS_NATIVE_EXIT(env, that, NSAccessibilityPositionAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_NSAccessibilitySizeAttribute
JNIEXPORT jintLong JNICALL OS_NATIVE(NSAccessibilitySizeAttribute)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSAccessibilitySizeAttribute_FUNC);
	rc = (jintLong)NSAccessibilitySizeAttribute;
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
JNIEXPORT jintLong JNICALL OS_NATIVE(NSBackgroundColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSBackgroundColorAttributeName_FUNC);
	rc = (jintLong)NSBackgroundColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSBackgroundColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSBaselineOffsetAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSBaselineOffsetAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSBaselineOffsetAttributeName_FUNC);
	rc = (jintLong)NSBaselineOffsetAttributeName;
	OS_NATIVE_EXIT(env, that, NSBaselineOffsetAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSBeep
JNIEXPORT void JNICALL OS_NATIVE(NSBeep)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, NSBeep_FUNC);
	NSBeep();
	OS_NATIVE_EXIT(env, that, NSBeep_FUNC);
}
#endif

#ifndef NO_NSBitsPerPixelFromDepth
JNIEXPORT jintLong JNICALL OS_NATIVE(NSBitsPerPixelFromDepth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSBitsPerPixelFromDepth_FUNC);
	rc = (jintLong)NSBitsPerPixelFromDepth((NSWindowDepth)arg0);
	OS_NATIVE_EXIT(env, that, NSBitsPerPixelFromDepth_FUNC);
	return rc;
}
#endif

#ifndef NO_NSCalibratedRGBColorSpace
JNIEXPORT jintLong JNICALL OS_NATIVE(NSCalibratedRGBColorSpace)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSCalibratedRGBColorSpace_FUNC);
	rc = (jintLong)NSCalibratedRGBColorSpace;
	OS_NATIVE_EXIT(env, that, NSCalibratedRGBColorSpace_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDefaultRunLoopMode
JNIEXPORT jintLong JNICALL OS_NATIVE(NSDefaultRunLoopMode)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSDefaultRunLoopMode_FUNC);
	rc = (jintLong)NSDefaultRunLoopMode;
	OS_NATIVE_EXIT(env, that, NSDefaultRunLoopMode_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDeviceRGBColorSpace
JNIEXPORT jintLong JNICALL OS_NATIVE(NSDeviceRGBColorSpace)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSDeviceRGBColorSpace_FUNC);
	rc = (jintLong)NSDeviceRGBColorSpace;
	OS_NATIVE_EXIT(env, that, NSDeviceRGBColorSpace_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDeviceResolution
JNIEXPORT jintLong JNICALL OS_NATIVE(NSDeviceResolution)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSDeviceResolution_FUNC);
	rc = (jintLong)NSDeviceResolution;
	OS_NATIVE_EXIT(env, that, NSDeviceResolution_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDragPboard
JNIEXPORT jintLong JNICALL OS_NATIVE(NSDragPboard)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSDragPboard_FUNC);
	rc = (jintLong)NSDragPboard;
	OS_NATIVE_EXIT(env, that, NSDragPboard_FUNC);
	return rc;
}
#endif

#ifndef NO_NSErrorFailingURLStringKey
JNIEXPORT jintLong JNICALL OS_NATIVE(NSErrorFailingURLStringKey)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSErrorFailingURLStringKey_FUNC);
	rc = (jintLong)NSErrorFailingURLStringKey;
	OS_NATIVE_EXIT(env, that, NSErrorFailingURLStringKey_FUNC);
	return rc;
}
#endif

#ifndef NO_NSFileTypeForHFSTypeCode
JNIEXPORT jintLong JNICALL OS_NATIVE(NSFileTypeForHFSTypeCode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSFileTypeForHFSTypeCode_FUNC);
	rc = (jintLong)NSFileTypeForHFSTypeCode((OSType)arg0);
	OS_NATIVE_EXIT(env, that, NSFileTypeForHFSTypeCode_FUNC);
	return rc;
}
#endif

#ifndef NO_NSFilenamesPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSFilenamesPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSFilenamesPboardType_FUNC);
	rc = (jintLong)NSFilenamesPboardType;
	OS_NATIVE_EXIT(env, that, NSFilenamesPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSFontAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSFontAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSFontAttributeName_FUNC);
	rc = (jintLong)NSFontAttributeName;
	OS_NATIVE_EXIT(env, that, NSFontAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSForegroundColorAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSForegroundColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSForegroundColorAttributeName_FUNC);
	rc = (jintLong)NSForegroundColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSForegroundColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSGetSizeAndAlignment
JNIEXPORT jintLong JNICALL OS_NATIVE(NSGetSizeAndAlignment)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintLongArray arg2)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSGetSizeAndAlignment_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)NSGetSizeAndAlignment((char*)arg0, (NSUInteger*)lparg1, (NSUInteger*)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, NSGetSizeAndAlignment_FUNC);
	return rc;
}
#endif

#ifndef NO_NSHTMLPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSHTMLPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSHTMLPboardType_FUNC);
	rc = (jintLong)NSHTMLPboardType;
	OS_NATIVE_EXIT(env, that, NSHTMLPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSLinkAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSLinkAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSLinkAttributeName_FUNC);
	rc = (jintLong)NSLinkAttributeName;
	OS_NATIVE_EXIT(env, that, NSLinkAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSParagraphStyleAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSParagraphStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSParagraphStyleAttributeName_FUNC);
	rc = (jintLong)NSParagraphStyleAttributeName;
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
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintAllPages)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintAllPages_FUNC);
	rc = (jintLong)NSPrintAllPages;
	OS_NATIVE_EXIT(env, that, NSPrintAllPages_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintCopies
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintCopies)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintCopies_FUNC);
	rc = (jintLong)NSPrintCopies;
	OS_NATIVE_EXIT(env, that, NSPrintCopies_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintFirstPage
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintFirstPage)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintFirstPage_FUNC);
	rc = (jintLong)NSPrintFirstPage;
	OS_NATIVE_EXIT(env, that, NSPrintFirstPage_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintJobDisposition
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintJobDisposition)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintJobDisposition_FUNC);
	rc = (jintLong)NSPrintJobDisposition;
	OS_NATIVE_EXIT(env, that, NSPrintJobDisposition_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintLastPage
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintLastPage)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintLastPage_FUNC);
	rc = (jintLong)NSPrintLastPage;
	OS_NATIVE_EXIT(env, that, NSPrintLastPage_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintMustCollate
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintMustCollate)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintMustCollate_FUNC);
	rc = (jintLong)NSPrintMustCollate;
	OS_NATIVE_EXIT(env, that, NSPrintMustCollate_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintPreviewJob
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintPreviewJob)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintPreviewJob_FUNC);
	rc = (jintLong)NSPrintPreviewJob;
	OS_NATIVE_EXIT(env, that, NSPrintPreviewJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSaveJob
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintSaveJob)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSaveJob_FUNC);
	rc = (jintLong)NSPrintSaveJob;
	OS_NATIVE_EXIT(env, that, NSPrintSaveJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSavePath
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintSavePath)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSavePath_FUNC);
	rc = (jintLong)NSPrintSavePath;
	OS_NATIVE_EXIT(env, that, NSPrintSavePath_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPrintSpoolJob
JNIEXPORT jintLong JNICALL OS_NATIVE(NSPrintSpoolJob)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSPrintSpoolJob_FUNC);
	rc = (jintLong)NSPrintSpoolJob;
	OS_NATIVE_EXIT(env, that, NSPrintSpoolJob_FUNC);
	return rc;
}
#endif

#ifndef NO_NSRTFPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSRTFPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSRTFPboardType_FUNC);
	rc = (jintLong)NSRTFPboardType;
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
JNIEXPORT jintLong JNICALL OS_NATIVE(NSSearchPathForDirectoriesInDomains)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSSearchPathForDirectoriesInDomains_FUNC);
	rc = (jintLong)NSSearchPathForDirectoriesInDomains((NSSearchPathDirectory)arg0, (NSSearchPathDomainMask)arg1, (BOOL)arg2);
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
JNIEXPORT jintLong JNICALL OS_NATIVE(NSStrikethroughColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSStrikethroughColorAttributeName_FUNC);
	rc = (jintLong)NSStrikethroughColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSStrikethroughColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSStrikethroughStyleAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSStrikethroughStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSStrikethroughStyleAttributeName_FUNC);
	rc = (jintLong)NSStrikethroughStyleAttributeName;
	OS_NATIVE_EXIT(env, that, NSStrikethroughStyleAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSStringPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSStringPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSStringPboardType_FUNC);
	rc = (jintLong)NSStringPboardType;
	OS_NATIVE_EXIT(env, that, NSStringPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSTIFFPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSTIFFPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSTIFFPboardType_FUNC);
	rc = (jintLong)NSTIFFPboardType;
	OS_NATIVE_EXIT(env, that, NSTIFFPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSTemporaryDirectory
JNIEXPORT jintLong JNICALL OS_NATIVE(NSTemporaryDirectory)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSTemporaryDirectory_FUNC);
	rc = (jintLong)NSTemporaryDirectory();
	OS_NATIVE_EXIT(env, that, NSTemporaryDirectory_FUNC);
	return rc;
}
#endif

#ifndef NO_NSURLPboardType
JNIEXPORT jintLong JNICALL OS_NATIVE(NSURLPboardType)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSURLPboardType_FUNC);
	rc = (jintLong)NSURLPboardType;
	OS_NATIVE_EXIT(env, that, NSURLPboardType_FUNC);
	return rc;
}
#endif

#ifndef NO_NSUnderlineColorAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSUnderlineColorAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSUnderlineColorAttributeName_FUNC);
	rc = (jintLong)NSUnderlineColorAttributeName;
	OS_NATIVE_EXIT(env, that, NSUnderlineColorAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NSUnderlineStyleAttributeName
JNIEXPORT jintLong JNICALL OS_NATIVE(NSUnderlineStyleAttributeName)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NSUnderlineStyleAttributeName_FUNC);
	rc = (jintLong)NSUnderlineStyleAttributeName;
	OS_NATIVE_EXIT(env, that, NSUnderlineStyleAttributeName_FUNC);
	return rc;
}
#endif

#ifndef NO_NewGlobalRef
JNIEXPORT jintLong JNICALL OS_NATIVE(NewGlobalRef)
	(JNIEnv *env, jclass that, jobject arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NewGlobalRef_FUNC);
	rc = (jintLong)(*env)->NewGlobalRef(env, arg0);
	OS_NATIVE_EXIT(env, that, NewGlobalRef_FUNC);
	return rc;
}
#endif

#ifndef NO_NewRgn
JNIEXPORT jintLong JNICALL OS_NATIVE(NewRgn)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, NewRgn_FUNC);
/*
	rc = (jintLong)NewRgn();
*/
	{
		LOAD_FUNCTION(fp, NewRgn)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, NewRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT void JNICALL OS_NATIVE(OffsetRgn)
	(JNIEnv *env, jclass that, jintLong arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, OffsetRgn_FUNC);
/*
	OffsetRgn(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, OffsetRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jshort, jshort))fp)(arg0, arg1, arg2);
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
		LOAD_FUNCTION(fp, OpenRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)())fp)();
		}
	}
	OS_NATIVE_EXIT(env, that, OpenRgn_FUNC);
}
#endif

#ifndef NO_PtInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRgn)
	(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1)
{
	jshort *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)PtInRgn(lparg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, PtInRgn)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jshort *, jintLong))fp)(lparg0, arg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDRegionToRects_FUNC);
/*
	rc = (jint)QDRegionToRects(arg0, arg1, arg2, arg3);
*/
	{
		LOAD_FUNCTION(fp, QDRegionToRects)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint, jintLong, jintLong))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, QDRegionToRects_FUNC);
	return rc;
}
#endif

#ifndef NO_RectInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRgn)
	(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1)
{
	jshort *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RectInRgn_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)RectInRgn(lparg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, RectInRgn)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jshort *, jintLong))fp)(lparg0, arg1);
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
	(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, RectRgn_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	RectRgn(arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, RectRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jshort *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, RectRgn_FUNC);
}
#endif

#ifndef NO_SectRgn
JNIEXPORT void JNICALL OS_NATIVE(SectRgn)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, SectRgn_FUNC);
/*
	SectRgn(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, SectRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
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
		LOAD_FUNCTION(fp, SetRect)
		if (fp) {
			((void (CALLING_CONVENTION*)(jshort *, jshort, jshort, jshort, jshort))fp)(lparg0, arg1, arg2, arg3, arg4);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SetRect_FUNC);
}
#endif

#ifndef NO_SetThemeCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeCursor_FUNC);
/*
	rc = (jint)SetThemeCursor(arg0);
*/
	{
		LOAD_FUNCTION(fp, SetThemeCursor)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, SetThemeCursor_FUNC);
	return rc;
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, UnionRgn_FUNC);
/*
	UnionRgn(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, UnionRgn)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, UnionRgn_FUNC);
}
#endif

#ifndef NO_class_1addIvar
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addIvar)
	(JNIEnv *env, jclass that, jintLong arg0, jstring arg1, jintLong arg2, jbyte arg3, jstring arg4)
{
	const char *lparg1= NULL;
	const char *lparg4= NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addIvar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetStringUTFChars(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)class_addIvar((Class)arg0, lparg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseStringUTFChars(env, arg4, lparg4);
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, class_1addIvar_FUNC);
	return rc;
}
#endif

#ifndef NO_class_1addMethod
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addMethod)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jstring arg3)
{
	const char *lparg3= NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addMethod_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetStringUTFChars(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)class_addMethod((Class)arg0, (SEL)arg1, (IMP)arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseStringUTFChars(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, class_1addMethod_FUNC);
	return rc;
}
#endif

#ifndef NO_class_1addProtocol
JNIEXPORT jboolean JNICALL OS_NATIVE(class_1addProtocol)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, class_1addProtocol_FUNC);
	rc = (jboolean)class_addProtocol((Class)arg0, (Protocol *)arg1);
	OS_NATIVE_EXIT(env, that, class_1addProtocol_FUNC);
	return rc;
}
#endif

#ifndef NO_class_1getMethodImplementation
JNIEXPORT jintLong JNICALL OS_NATIVE(class_1getMethodImplementation)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, class_1getMethodImplementation_FUNC);
	rc = (jintLong)class_getMethodImplementation((Class)arg0, (SEL)arg1);
	OS_NATIVE_EXIT(env, that, class_1getMethodImplementation_FUNC);
	return rc;
}
#endif

#ifndef NO_getpid
JNIEXPORT jint JNICALL OS_NATIVE(getpid)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, getpid_FUNC);
	rc = (jint)getpid();
	OS_NATIVE_EXIT(env, that, getpid_FUNC);
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
		LOAD_FUNCTION(fp, instrumentObjcMessageSends)
		if (fp) {
			((void (CALLING_CONVENTION*)(jboolean))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, instrumentObjcMessageSends_FUNC);
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	CGPoint _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	CGRect _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	CGSize _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getCGSizeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setCGSizeFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	NSAffineTransformStruct _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getNSAffineTransformStructFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSAffineTransformStructFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	NSPoint _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getNSPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSPointFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	NSRange _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getNSRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSRangeFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	NSRect _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getNSRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSRectFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	NSSize _arg1, *lparg1=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getNSSizeFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) setNSSizeFields(env, arg1, lparg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	CGPoint _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	CGRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	CGSize _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setCGSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSAffineTransformStruct _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSAffineTransformStructFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSRange _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSSize _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC);
#endif
}
#endif

#ifndef NO_objc_1allocateClassPair
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1allocateClassPair)
	(JNIEnv *env, jclass that, jintLong arg0, jstring arg1, jintLong arg2)
{
	const char *lparg1= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1allocateClassPair_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)objc_allocateClassPair((Class)arg0, lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, objc_1allocateClassPair_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1getClass
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1getClass)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1getClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)objc_getClass(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1getClass_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1getProtocol
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1getProtocol)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1getProtocol_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)objc_getProtocol(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1getProtocol_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1lookUpClass
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1lookUpClass)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1lookUpClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)objc_lookUpClass(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1lookUpClass_FUNC);
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong))objc_msgSend)(arg0, arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IID) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IID)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IID_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJD_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jdouble))objc_msgSend)(arg0, arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IID_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIDIIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJDJJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIDIIIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2, jintLong arg3, jintLong arg4, jintLong arg5, jboolean arg6)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJDJJJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jdouble arg2, jintLong arg3, jintLong arg4, jintLong arg5, jboolean arg6)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIDIIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJDJJJZ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jdouble, jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIDIIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJDJJJZ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);
	rc = (jint)((jint (*)(jint, jint, jfloat))objc_msgSend)(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIFF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJDD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIFF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDouble arg2, jfloatDouble arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJDD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDouble arg2, jfloatDouble arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJDD_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jfloatDouble, jfloatDouble))objc_msgSend)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJDD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIFFFF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJDDDD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIFFFF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDouble arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJDDDD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDouble arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFFFF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJDDDD_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jfloatDouble, jfloatDouble, jfloatDouble, jfloatDouble))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFFFF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJDDDD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jfloatDouble arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jfloatDouble arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJD_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jfloatDouble))objc_msgSend)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIIIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jintLong arg7, jintLong arg8)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jintLong arg7, jintLong arg8)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIIIIZZIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJJJZZJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIZZIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jboolean arg7, jboolean arg8, jintLong arg9, jintLong arg10, jintLong arg11)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJJJZZJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jboolean arg7, jboolean arg8, jintLong arg9, jintLong arg10, jintLong arg11)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIZZIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJJJZZJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jboolean, jboolean, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIZZIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJJJZZJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIIIIZZIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJJJZZJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIIIIZZIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jboolean arg7, jboolean arg8, jintLong arg9, jintLong arg10, jintLong arg11, jintLong arg12)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJJJZZJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6, jboolean arg7, jboolean arg8, jintLong arg9, jintLong arg10, jintLong arg11, jintLong arg12)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIIZZIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJJJZZJJJJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jboolean, jboolean, jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIIZZIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJJJZZJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJJZ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jobject arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jobject arg4)
#endif
{
	NSRange _arg4, *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	if (arg4) if ((lparg4 = getNSRangeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, NSRange))objc_msgSend)(arg0, arg1, arg2, arg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRangeFields(env, arg4, lparg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jboolean arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jboolean arg4)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJJZ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSPoint _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, NSPoint))objc_msgSend)(arg0, arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4, jdouble arg5, jintLong arg6, jintLong arg7, jshort arg8, jintLong arg9, jintLong arg10)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4, jdouble arg5, jintLong arg6, jintLong arg7, jshort arg8, jintLong arg9, jintLong arg10)
#endif
{
	NSPoint _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ_FUNC);
#endif
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, NSPoint, jintLong, jdouble, jintLong, jintLong, jshort, jintLong, jintLong))objc_msgSend)(arg0, arg1, arg2, *lparg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2JJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3, jobject arg4, jintLong arg5, jintLong arg6, jintLong arg7, jboolean arg8)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2JJJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3, jobject arg4, jintLong arg5, jintLong arg6, jintLong arg7, jboolean arg8)
#endif
{
	NSPoint _arg3, *lparg3=NULL;
	NSSize _arg4, *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2JJJZ_FUNC);
#endif
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSSizeFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, NSPoint, NSSize, jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2, *lparg3, *lparg4, arg5, arg6, arg7, arg8);
fail:
	if (arg4 && lparg4) setNSSizeFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2JJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSRange _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, NSRange))objc_msgSend)(arg0, arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jboolean arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJJZ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSAffineTransformStruct _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSAffineTransformStructFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSAffineTransformStruct))objc_msgSend)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSAffineTransformStructFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint))objc_msgSend)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, jfloatDouble, jfloatDouble, jfloatDouble))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5, jboolean arg6)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4, jfloatDouble arg5, jboolean arg6)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, jfloatDouble, jfloatDouble, jfloatDouble, jboolean))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jfloatDoubleArray arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jfloatDoubleArray arg4)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	jfloatDouble *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatDoubleArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, jintLong, jfloatDouble *))objc_msgSend)(arg0, arg1, *lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseFloatDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, NSPoint, jintLong))objc_msgSend)(arg0, arg1, *lparg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jobject arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jobject arg4)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	NSPoint _arg4, *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, NSPoint, NSPoint))objc_msgSend)(arg0, arg1, *lparg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4, jfloatDouble arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4, jfloatDouble arg5)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSPoint, NSRect, jintLong, jfloatDouble))objc_msgSend)(arg0, arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSRange _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRange))objc_msgSend)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3)
#endif
{
	NSRange _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRange, jintLong))objc_msgSend)(arg0, arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3)
#endif
{
	NSRange _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRange, NSPoint))objc_msgSend)(arg0, arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect))objc_msgSend)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jfloatDouble))objc_msgSend)(arg0, arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jfloatDouble arg3, jfloatDouble arg4)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jfloatDouble, jfloatDouble))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jintLong))objc_msgSend)(arg0, arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jintLong, jintLong))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4, jboolean arg5, jintLong arg6)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jintLong arg4, jboolean arg5, jintLong arg6)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jintLong, jintLong, jboolean, jintLong))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jboolean arg4, jintLong arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jintLong arg3, jboolean arg4, jintLong arg5)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jintLong, jboolean, jintLong))objc_msgSend)(arg0, arg1, *lparg2, arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, NSPoint))objc_msgSend)(arg0, arg1, *lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jobject arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jobject arg4)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	NSRect _arg4, *lparg4=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, NSRange, NSRect))objc_msgSend)(arg0, arg1, *lparg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4, jfloatDouble arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jobject arg3, jintLong arg4, jfloatDouble arg5)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	NSRect _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, NSRect, jintLong, jfloatDouble))objc_msgSend)(arg0, arg1, *lparg2, *lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jboolean arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2, jboolean arg3)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSRect, jboolean))objc_msgSend)(arg0, arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSSize _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, NSSize))objc_msgSend)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJZ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJZ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jboolean))objc_msgSend)(arg0, arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__IIZI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__IIZI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJZJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2, jintLong arg3)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJZJ_FUNC);
#endif
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jboolean, jintLong))objc_msgSend)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJZJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3B) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3B) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3B)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3B)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2)
#endif
{
	jbyte *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3B_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3B_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jbyte *))objc_msgSend)(arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3B_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3BI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3BI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jintLong arg3)
#endif
{
	jbyte *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3BI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3BJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jbyte *, jintLong))objc_msgSend)(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3BI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3BJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3C) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3C) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3C)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3C)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2)
#endif
{
	jchar *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3C_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3C_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jchar *))objc_msgSend)(arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3C_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3CI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3CJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3CI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3CJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jintLong arg3)
#endif
{
	jchar *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3CI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3CJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jchar *, jintLong))objc_msgSend)(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3CI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3CJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jobject arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jcharArray arg2, jobject arg3)
#endif
{
	jchar *lparg2=NULL;
	NSRange _arg3, *lparg3=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jchar *, NSRange))objc_msgSend)(arg0, arg1, lparg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3F) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3D) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3F)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDoubleArray arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3D)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDoubleArray arg2)
#endif
{
	jfloatDouble *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3F_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3D_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetFloatDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jfloatDouble *))objc_msgSend)(arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatDoubleArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3F_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3D_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3FIF) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3DJD) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3FIF)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDoubleArray arg2, jintLong arg3, jfloatDouble arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3DJD)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jfloatDoubleArray arg2, jintLong arg3, jfloatDouble arg4)
#endif
{
	jfloatDouble *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3FIF_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3DJD_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetFloatDoubleArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jfloatDouble *, jintLong, jfloatDouble))objc_msgSend)(arg0, arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatDoubleArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3FIF_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3DJD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
#endif
{
	jint *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jint *))objc_msgSend)(arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend__II_3III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend__JJ_3JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__II_3III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSend__JJ_3JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLong arg3, jintLong arg4)
#endif
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend__II_3III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJ_3JJJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(jintLong, jintLong, jintLong *, jintLong, jintLong))objc_msgSend)(arg0, arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend__II_3III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJ_3JJJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__JJFD
JNIEXPORT jlong JNICALL OS_NATIVE(objc_1msgSend__JJFD)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jfloat arg2, jdouble arg3)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__JJFD_FUNC);
	rc = (jlong)((jlong (*)(jlong, jlong, jfloat, jdouble))objc_msgSend)(arg0, arg1, arg2, arg3);
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
	rc = (jlong)((jlong (*)(jlong, jlong, jint))objc_msgSend)(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__JJI_FUNC);
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong))objc_msgSendSuper)(lparg0, arg1);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, jintLong))objc_msgSendSuper)(lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, jintLong, jintLong, jintLong, jboolean))objc_msgSendSuper)(lparg0, arg1, arg2, arg3, arg4, arg5);
fail:
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	NSPoint _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, NSPoint))objc_msgSendSuper)(lparg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, NSRect))objc_msgSendSuper)(lparg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2, jintLong arg3)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2, jintLong arg3)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	NSRect _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, NSRect, jintLong))objc_msgSendSuper)(lparg0, arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jintLong JNICALL OS_NATIVE(objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jobject arg2)
#endif
{
	struct objc_super _arg0, *lparg0=NULL;
	NSSize _arg2, *lparg2=NULL;
	jintLong rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getobjc_superFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jintLong)((jintLong (*)(struct objc_super *, jintLong, NSSize))objc_msgSendSuper)(lparg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	if (arg0 && lparg0) setobjc_superFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJ_FUNC);
#endif
	rc = (jboolean)((BOOL (*)(jintLong, jintLong))objc_msgSend_bool)(arg0, arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#endif
{
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJJ_FUNC);
#endif
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, jintLong))objc_msgSend_bool)(arg0, arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__IIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJJJ_FUNC);
#endif
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, jintLong, jintLong))objc_msgSend_bool)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__IIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__IIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#endif
{
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__IIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJJJJ_FUNC);
#endif
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend_bool)(arg0, arg1, arg2, arg3, arg4);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__IIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__IIIIIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJJJJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__IIIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJJJJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
#endif
{
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__IIIIIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJJJJJJ_FUNC);
#endif
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, jintLong, jintLong, jintLong, jintLong, jintLong))objc_msgSend_bool)(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__IIIIIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSPoint _arg2, *lparg2=NULL;
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, NSPoint))objc_msgSend_bool)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jboolean JNICALL OS_NATIVE(objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jobject arg2)
#endif
{
	NSRect _arg2, *lparg2=NULL;
	jboolean rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)((BOOL (*)(jintLong, jintLong, NSRect))objc_msgSend_bool)(arg0, arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1fpret__II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1fpret__JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__II)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#else
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__JJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
#endif
{
	jdouble rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__JJ_FUNC);
#endif
	rc = (jdouble)((jdouble (*)(jintLong, jintLong))objc_msgSend_fpret)(arg0, arg1);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1fpret__III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1fpret__JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#endif
{
	jdouble rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__JJJ_FUNC);
#endif
	rc = (jdouble)((jdouble (*)(jintLong, jintLong, jintLong))objc_msgSend_fpret)(arg0, arg1, arg2);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__JJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1fpret__IIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1fpret__JJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__IIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT jdouble JNICALL OS_NATIVE(objc_1msgSend_1fpret__JJJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	jdouble rc = 0;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1fpret__JJJJ_FUNC);
#endif
	rc = (jdouble)((jdouble (*)(jintLong, jintLong, jintLong, jintLong))objc_msgSend_fpret)(arg0, arg1, arg2, arg3);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1fpret__JJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSAffineTransformStruct _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSAffineTransformStructFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSAffineTransformStruct *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else {
		*lparg0 = (*(NSAffineTransformStruct (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSAffineTransformStructFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, jintLong, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, arg3);
	} else {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong, jintLong))objc_msgSend)(arg1, arg2, arg3);
	}
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, jintLong, jintLong, NSPoint))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3);
	} else {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong, NSPoint))objc_msgSend)(arg1, arg2, *lparg3);
	}
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSPoint *, jintLong, jintLong, NSPoint, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong, NSPoint, jintLong))objc_msgSend)(arg1, arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSRange _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRange *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else {
		*lparg0 = (*(NSRange (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	NSRange _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRange *, jintLong, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, arg3);
	} else {
		*lparg0 = (*(NSRange (*)(jintLong, jintLong, jintLong))objc_msgSend)(arg1, arg2, arg3);
	}
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSRange _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRange *, jintLong, jintLong, NSRect))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3);
	} else {
		*lparg0 = (*(NSRange (*)(jintLong, jintLong, NSRect))objc_msgSend)(arg1, arg2, *lparg3);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3)
#endif
{
	NSRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, arg3);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, jintLong))objc_msgSend)(arg1, arg2, arg3);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
#endif
{
	NSRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, arg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, jintLong, jintLong))objc_msgSend)(arg1, arg2, arg3, arg4);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jboolean arg5)
#endif
{
	NSRect _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, jintLong, jintLong, jboolean))objc_msgSend_stret)(lparg0, arg1, arg2, arg3, arg4, arg5);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, jintLong, jintLong, jboolean))objc_msgSend)(arg1, arg2, arg3, arg4, arg5);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#endif
{
	NSRect _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, NSRange, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, NSRange, jintLong))objc_msgSend)(arg1, arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, NSRect))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, NSRect))objc_msgSend)(arg1, arg2, *lparg3);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#endif
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSRect *, jintLong, jintLong, NSRect, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(jintLong, jintLong, NSRect, jintLong))objc_msgSend)(arg1, arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSSize _arg0, *lparg0=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else {
		*lparg0 = (*(NSSize (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSSize _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, jintLong, jintLong, NSRect))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3);
	} else {
		*lparg0 = (*(NSSize (*)(jintLong, jintLong, NSRect))objc_msgSend)(arg1, arg2, *lparg3);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3)
#endif
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, jintLong, jintLong, NSSize))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3);
	} else {
		*lparg0 = (*(NSSize (*)(jintLong, jintLong, NSSize))objc_msgSend)(arg1, arg2, *lparg3);
	}
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jintLong arg4)
#endif
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, jintLong, jintLong, NSSize, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSSize (*)(jintLong, jintLong, NSSize, jintLong))objc_msgSend)(arg1, arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jboolean arg4, jboolean arg5, jintLong arg6)
#else
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2, jobject arg3, jboolean arg4, jboolean arg5, jintLong arg6)
#endif
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
#ifndef JNI64
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
#else
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		((void (*)(NSSize *, jintLong, jintLong, NSSize, jboolean, jboolean, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2, *lparg3, arg4, arg5, arg6);
	} else {
		*lparg0 = (*(NSSize (*)(jintLong, jintLong, NSSize, jboolean, jboolean, jintLong))objc_msgSend)(arg1, arg2, *lparg3, arg4, arg5, arg6);
	}
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
#ifndef JNI64
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
#else
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ_FUNC);
#endif
}
#endif

#ifndef NO_objc_1registerClassPair
JNIEXPORT void JNICALL OS_NATIVE(objc_1registerClassPair)
	(JNIEnv *env, jclass that, jintLong arg0)
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

#ifndef NO_object_1getClass
JNIEXPORT jintLong JNICALL OS_NATIVE(object_1getClass)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, object_1getClass_FUNC);
	rc = (jintLong)object_getClass((id)arg0);
	OS_NATIVE_EXIT(env, that, object_1getClass_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1getClassName
JNIEXPORT jintLong JNICALL OS_NATIVE(object_1getClassName)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, object_1getClassName_FUNC);
	rc = (jintLong)object_getClassName((id)arg0);
	OS_NATIVE_EXIT(env, that, object_1getClassName_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1getInstanceVariable
JNIEXPORT jintLong JNICALL OS_NATIVE(object_1getInstanceVariable)
	(JNIEnv *env, jclass that, jintLong arg0, jstring arg1, jintLongArray arg2)
{
	const char *lparg1= NULL;
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, object_1getInstanceVariable_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)object_getInstanceVariable((id)arg0, lparg1, (void **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, object_1getInstanceVariable_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1setClass
JNIEXPORT jintLong JNICALL OS_NATIVE(object_1setClass)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, object_1setClass_FUNC);
	rc = (jintLong)object_setClass((id)arg0, (Class)arg1);
	OS_NATIVE_EXIT(env, that, object_1setClass_FUNC);
	return rc;
}
#endif

#ifndef NO_object_1setInstanceVariable
JNIEXPORT jintLong JNICALL OS_NATIVE(object_1setInstanceVariable)
	(JNIEnv *env, jclass that, jintLong arg0, jstring arg1, jintLong arg2)
{
	const char *lparg1= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, object_1setInstanceVariable_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetStringUTFChars(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)object_setInstanceVariable((id)arg0, lparg1, (void *)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseStringUTFChars(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, object_1setInstanceVariable_FUNC);
	return rc;
}
#endif

#ifndef NO_sel_1registerName
JNIEXPORT jintLong JNICALL OS_NATIVE(sel_1registerName)
	(JNIEnv *env, jclass that, jstring arg0)
{
	const char *lparg0= NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, sel_1registerName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetStringUTFChars(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)sel_registerName(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseStringUTFChars(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, sel_1registerName_FUNC);
	return rc;
}
#endif


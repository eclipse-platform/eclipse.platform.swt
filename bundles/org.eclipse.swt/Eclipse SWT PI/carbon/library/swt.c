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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_carbon_OS_##func

#ifndef NO_ActiveNonFloatingWindow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ActiveNonFloatingWindow
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ActiveNonFloatingWindow\n")

	return (jint)ActiveNonFloatingWindow();
}
#endif

#ifndef NO_AddDataBrowserItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AddDataBrowserItems
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("AddDataBrowserItems\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)AddDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_AddDataBrowserItems */

#ifndef NO_AddDataBrowserListViewColumn
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AddDataBrowserListViewColumn
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DataBrowserListViewColumnDesc _arg1={0}, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("AddDataBrowserListViewColumn\n")

	if (arg1) lparg1 = getDataBrowserListViewColumnDescFields(env, arg1, &_arg1);
	rc = (jint)AddDataBrowserListViewColumn((ControlRef)arg0, (DataBrowserListViewColumnDesc *)lparg1, (DataBrowserTableViewColumnIndex)arg2);
	if (arg1) setDataBrowserListViewColumnDescFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_AddDataBrowserListViewColumn */

#ifndef NO_AECountItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AECountItems
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	AEDesc _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("AECountItems\n")

	if (arg0) lparg0 = getAEDescFields(env, arg0, &_arg0);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)AECountItems((const AEDescList *)lparg0, (long *)lparg1);
	if (arg0) setAEDescFields(env, arg0, lparg0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_AECountItems */

#ifndef NO_AEGetNthPtr
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AEGetNthPtr
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	AEDesc _arg0, *lparg0=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc;

	DEBUG_CALL("AEGetNthPtr\n")

	if (arg0) lparg0 = getAEDescFields(env, arg0, &_arg0);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)AEGetNthPtr((const AEDescList *)lparg0, arg1, (DescType)arg2, (AEKeyword *)lparg3, (DescType *)lparg4, (void *)arg5, (Size)arg6, (Size *)lparg7);
	if (arg0) setAEDescFields(env, arg0, lparg0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_AEGetNthPtr */

#ifndef NO_AEProcessAppleEvent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AEProcessAppleEvent
	(JNIEnv *env, jclass that, jobject arg0)
{
	EventRecord _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("AEProcessAppleEvent\n")

	if (arg0) lparg0 = getEventRecordFields(env, arg0, &_arg0);
	rc = (jint)AEProcessAppleEvent((const EventRecord *)lparg0);
	if (arg0) setEventRecordFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_AEProcessAppleEvent */

#ifndef NO_AppendMenuItemTextWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AppendMenuItemTextWithCFString
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc;

	DEBUG_CALL("AppendMenuItemTextWithCFString\n")

	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)AppendMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemAttributes)arg2, (MenuCommand)arg3, (MenuItemIndex *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_AppendMenuItemTextWithCFString */

#ifndef NO_ATSUCreateStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateStyle)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("ATSUCreateStyle\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)ATSUCreateStyle((ATSUStyle *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_ATSUCreateTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateTextLayout)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("ATSUCreateTextLayout\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)ATSUCreateTextLayout((ATSUTextLayout *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_ATSUDisposeStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeStyle)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ATSUDisposeStyle\n")

	return (jint)ATSUDisposeStyle((ATSUStyle)arg0);
}
#endif

#ifndef NO_ATSUDisposeTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeTextLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ATSUDisposeTextLayout\n")

	return (jint)ATSUDisposeTextLayout((ATSUTextLayout)arg0);
}
#endif

#ifndef NO_ATSUDrawText
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDrawText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("ATSUDrawText\n")

	return (jint)ATSUDrawText((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (UniCharCount)arg2, (ATSUTextMeasurement)arg3, (ATSUTextMeasurement)arg4);
}
#endif

#ifndef NO_ATSUGetGlyphBounds
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetGlyphBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6, jint arg7, jintArray arg8)
{
	jint *lparg8=NULL;
	jint rc;

	DEBUG_CALL("ATSUGetGlyphBounds\n")

	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)ATSUGetGlyphBounds((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, (UniCharArrayOffset)arg3, (UniCharCount)arg4, (UInt16)arg5, (ItemCount)arg6, (ATSTrapezoid *)arg7, (ItemCount *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	return rc;
}
#endif

#ifndef NO_ATSUSetAttributes
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("ATSUSetAttributes\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ATSUSetAttributes((ATSUStyle)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_ATSUSetLayoutControls
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetLayoutControls)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("ATSUSetLayoutControls\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ATSUSetLayoutControls((ATSUTextLayout)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_ATSUSetRunStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetRunStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("ATSUSetRunStyle\n")

	return (jint)ATSUSetRunStyle((ATSUTextLayout)arg0, (ATSUStyle)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3);
}
#endif

#ifndef NO_ATSUSetTextPointerLocation
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTextPointerLocation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("ATSUSetTextPointerLocation\n")

	return (jint)ATSUSetTextPointerLocation((ATSUTextLayout)arg0, (ConstUniCharArrayPtr)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3, (UniCharCount)arg4);
}
#endif

#ifndef NO_AutoSizeDataBrowserListViewColumns
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AutoSizeDataBrowserListViewColumns
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("AutoSizeDataBrowserListViewColumns\n")

	return (jint)AutoSizeDataBrowserListViewColumns((ControlRef)arg0);
}
#endif /* NO_AutoSizeDataBrowserListViewColumns */

#ifndef NO_BeginUpdate
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_BeginUpdate
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BeginUpdate\n")

	BeginUpdate((WindowRef)arg0);
}
#endif /* NO_BeginUpdate */

#ifndef NO_BringToFront
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_BringToFront
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("BringToFront\n")

	BringToFront((WindowRef)arg0);
}
#endif /* NO_BringToFront */

#ifndef NO_CFRelease
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFRelease
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CFRelease\n")

	CFRelease((CFTypeRef)arg0);
}
#endif /* NO_CFRelease */

#ifndef NO_CFStringCreateWithCharacters
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringCreateWithCharacters
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CFStringCreateWithCharacters\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)CFStringCreateWithCharacters((CFAllocatorRef)arg0, (const UniChar *)lparg1, (CFIndex)arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CFStringCreateWithCharacters */

#ifndef NO_CFStringGetBytes
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetBytes
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbyte arg3, jboolean arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	CFRange _arg1, *lparg1=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc;

	DEBUG_CALL("CFStringGetBytes\n")

	if (arg1) lparg1 = getCFRangeFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)CFStringGetBytes((CFStringRef)arg0, (CFRange)*lparg1, (CFStringEncoding)arg2, (UInt8)arg3, (Boolean)arg4, (UInt8 *)lparg5, (CFIndex)arg6, (CFIndex *)lparg7);
	if (arg1) setCFRangeFields(env, arg1, lparg1);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif

#ifndef NO_CFStringCreateWithBytes
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringCreateWithBytes
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jboolean arg4)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CFStringCreateWithBytes\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CFStringCreateWithBytes((CFAllocatorRef)arg0, (const UInt8 *)lparg1, (CFIndex)arg2, (CFStringEncoding)arg3, (Boolean)arg4);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

#ifndef NO_CFStringGetCharacters
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetCharacters
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jcharArray arg2)
{
	CFRange _arg1, *lparg1=NULL;
	jchar *lparg2=NULL;

	DEBUG_CALL("CFStringGetCharacters\n")

	if (arg1) lparg1 = getCFRangeFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	CFStringGetCharacters((CFStringRef)arg0, (CFRange)*lparg1, (UniChar *)lparg2);
	if (arg1) setCFRangeFields(env, arg1, lparg1);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
}
#endif /* NO_CFStringGetCharacters */

#ifndef NO_CFStringGetLength
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetLength
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CFStringGetLength\n")

	return (jint)CFStringGetLength((CFStringRef)arg0);
}
#endif /* NO_CFStringGetLength */

#ifndef NO_CFStringGetSystemEncoding
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetSystemEncoding
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CFStringGetSystemEncoding\n")

	return (jint)CFStringGetSystemEncoding();
}
#endif

#ifndef NO_CFURLCopyFileSystemPath
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFURLCopyFileSystemPath
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CFURLCopyFileSystemPath\n")

	return (jint)CFURLCopyFileSystemPath((CFURLRef)arg0, (CFURLPathStyle)arg1);
}
#endif /* NO_CFURLCopyFileSystemPath */

#ifndef NO_CFURLCopyLastPathComponent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFURLCopyLastPathComponent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CFURLCopyLastPathComponent\n")

	return (jint)CFURLCopyLastPathComponent((CFURLRef)arg0);
}
#endif

#ifndef NO_CFURLCreateCopyAppendingPathComponent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFURLCreateCopyAppendingPathComponent
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	DEBUG_CALL("CFURLCreateCopyAppendingPathComponent\n")

	return (jint)CFURLCreateCopyAppendingPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringRef)arg2, (Boolean)arg3);
}
#endif

#ifndef NO_CFURLCreateCopyDeletingLastPathComponent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFURLCreateCopyDeletingLastPathComponent
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CFURLCreateCopyDeletingLastPathComponent\n")

	return (jint)CFURLCreateCopyDeletingLastPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1);
}
#endif

#ifndef NO_CFURLCreateFromFSRef
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFURLCreateFromFSRef
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CFURLCreateFromFSRef\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CFURLCreateFromFSRef((CFAllocatorRef)arg0, (const struct FSRef *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CFURLCreateFromFSRef */

#ifndef NO_CGBitmapContextCreate
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGBitmapContextCreate
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("CGBitmapContextCreate\n")

	return (jint)CGBitmapContextCreate((void *)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6);
}
#endif /* NO_CGBitmapContextCreate */

#ifndef NO_CGColorSpaceCreateDeviceRGB
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGColorSpaceCreateDeviceRGB
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("CGColorSpaceCreateDeviceRGB\n")

	return (jint)CGColorSpaceCreateDeviceRGB();
}
#endif /* NO_CGColorSpaceCreateDeviceRGB */

#ifndef NO_CGColorSpaceRelease
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGColorSpaceRelease
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGColorSpaceRelease\n")

	CGColorSpaceRelease((CGColorSpaceRef)arg0);
}
#endif /* NO_CGColorSpaceRelease */

#ifndef NO_CGContextAddArc
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextAddArc
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jboolean arg6)
{
	DEBUG_CALL("CGContextAddArc\n")

	CGContextAddArc((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4, (float)arg5, (Boolean)arg6);
}
#endif /* NO_CGContextAddArc */

#ifndef NO_CGContextAddArcToPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextAddArcToPoint
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	DEBUG_CALL("CGContextAddArcToPoint\n")

	CGContextAddArcToPoint((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4, (float)arg5);
}
#endif /* NO_CGContextAddArc */

#ifndef NO_CGContextAddLineToPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextAddLineToPoint
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextAddLineToPoint\n")

	CGContextAddLineToPoint((CGContextRef)arg0, arg1, arg2);
}
#endif /* NO_CGContextAddLineToPoint */

#ifndef NO_CGContextAddLines
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextAddLines
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("CGContextAddLines\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextAddLines((CGContextRef)arg0, (const CGPoint *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextAddLines */

#ifndef NO_CGContextBeginPath
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextBeginPath
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextBeginPath\n")

	CGContextBeginPath((CGContextRef)arg0);
}
#endif /* NO_CGContextBeginPath */

#ifndef NO_CGContextClip
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextClip
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextClip\n")

	CGContextClip((CGContextRef)arg0);
}
#endif /* NO_CGContextClip */

#ifndef NO_CGContextClosePath
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextClosePath
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextClosePath\n")

	CGContextClosePath((CGContextRef)arg0);
}
#endif /* NO_CGContextClosePath */

#ifndef NO_CGContextDrawImage
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextDrawImage
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;

	DEBUG_CALL("CGContextDrawImage\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextDrawImage((CGContextRef)arg0, (CGRect)*lparg1, (CGImageRef)arg2);
	if (arg1) setCGRectFields(env, arg1, lparg1);
}
#endif /* NO_CGContextDrawImage */

#ifndef NO_CGContextFillPath
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextFillPath
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextFillPath\n")

	CGContextFillPath((CGContextRef)arg0);
}
#endif /* NO_CGContextFillPath */

#ifndef NO_CGContextFillRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextFillRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;

	DEBUG_CALL("CGContextFillRect\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextFillRect((CGContextRef)arg0, (CGRect)*lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
}
#endif /* NO_CGContextFillRect */

#ifndef NO_CGContextFlush
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextFlush
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextFlush\n")

	CGContextFlush((CGContextRef)arg0);
}
#endif /* NO_CGContextFlush */

#ifndef NO_CGContextGetTextPosition
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextGetTextPosition
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGPoint _arg1, *lparg1=NULL;

	DEBUG_CALL("CGContextGetTextPosition\n")

	if (arg1) lparg1 = getCGPointFields(env, arg1, &_arg1);
	*lparg1 = CGContextGetTextPosition((CGContextRef)arg0);
	if (arg1) setCGPointFields(env, arg1, lparg1);
}
#endif /* NO_CGContextGetTextPosition */

#ifndef NO_CGContextMoveToPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextMoveToPoint
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextMoveToPoint\n")

	CGContextMoveToPoint((CGContextRef)arg0, (float)arg1, (float)arg2);
}
#endif /* NO_CGContextMoveToPoint */

#ifndef NO_CGContextRelease
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextRelease
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextRelease\n")

	CGContextRelease((CGContextRef)arg0);
}
#endif /* NO_CGContextRelease */

#ifndef NO_CGContextRestoreGState
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextRestoreGState
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextRestoreGState\n")

	CGContextRestoreGState((CGContextRef)arg0);
}
#endif /* NO_CGContextRestoreGState */

#ifndef NO_CGContextSaveGState
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSaveGState
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextSaveGState\n")

	CGContextSaveGState((CGContextRef)arg0);
}
#endif /* NO_CGContextSaveGState */

#ifndef NO_CGContextScaleCTM
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextScaleCTM
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextScaleCTM\n")

	CGContextScaleCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
}
#endif /* NO_CGContextScaleCTM */

#ifndef NO_CGContextSelectFont
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSelectFont
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jfloat arg2, jint arg3)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("CGContextSelectFont\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	CGContextSelectFont((CGContextRef)arg0, (const char *)lparg1, (float)arg2, (CGTextEncoding)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextSelectFont */

#ifndef NO_CGContextSetFillColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetFillColor
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("CGContextSetFillColor\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetFillColor((CGContextRef)arg0, (const float *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextSetFillColor */

#ifndef NO_CGContextSetFillColorSpace
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetFillColorSpace
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CGContextSetFillColorSpace\n")

	CGContextSetFillColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
}
#endif /* NO_CGContextSetFillColorSpace */

#ifndef NO_CGContextSetFontSize
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetFontSize
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	DEBUG_CALL("CGContextSetFontSize\n")

	CGContextSetFontSize((CGContextRef)arg0, (float)arg1);
}
#endif /* NO_CGContextSetFontSize */

#ifndef NO_CGContextSetLineDash
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetLineDash
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloatArray arg2, jint arg3)
{
	jfloat *lparg2=NULL;

	DEBUG_CALL("CGContextSetLineDash\n")

	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	CGContextSetLineDash((CGContextRef)arg0, (float)arg1, (const float *)lparg2, (size_t)arg3);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
}
#endif /* NO_CGContextSetLineDash */

#ifndef NO_CGContextSetLineWidth
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetLineWidth
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	DEBUG_CALL("CGContextSetLineWidth\n")

	CGContextSetLineWidth((CGContextRef)arg0, (float)arg1);
}
#endif /* NO_CGContextSetLineWidth */

#ifndef NO_CGContextSetRGBFillColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetRGBFillColor
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	DEBUG_CALL("CGContextSetRGBFillColor\n")

	CGContextSetRGBFillColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
}
#endif /* NO_CGContextSetRGBFillColor */

#ifndef NO_CGContextSetRGBStrokeColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetRGBStrokeColor
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	DEBUG_CALL("CGContextSetRGBStrokeColor\n")

	CGContextSetRGBStrokeColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
}
#endif /* NO_CGContextSetRGBStrokeColor */

#ifndef NO_CGContextSetStrokeColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetStrokeColor
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("CGContextSetStrokeColor\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetStrokeColor((CGContextRef)arg0, (const float *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextSetStrokeColor */

#ifndef NO_CGContextSetStrokeColorSpace
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetStrokeColorSpace
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CGContextSetStrokeColorSpace\n")

	CGContextSetStrokeColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
}
#endif /* NO_CGContextSetStrokeColorSpace */

#ifndef NO_CGContextSetTextDrawingMode
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetTextDrawingMode
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CGContextSetTextDrawingMode\n")

	CGContextSetTextDrawingMode((CGContextRef)arg0, (CGTextDrawingMode)arg1);
}
#endif /* NO_CGContextSetTextDrawingMode */

#ifndef NO_CGContextSetTextMatrix
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetTextMatrix
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	DEBUG_CALL("CGContextSetTextMatrix\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetTextMatrix((CGContextRef)arg0, *(CGAffineTransform *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextSetTextMatrix */

#ifndef NO_CGContextSetTextPosition
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSetTextPosition
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextSetTextPosition\n")

	CGContextSetTextPosition((CGContextRef)arg0, (float)arg1, (float)arg2);
}
#endif /* NO_CGContextSetTextPosition */

#ifndef NO_CGContextShowText
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextShowText
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("CGContextShowText\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	CGContextShowText((CGContextRef)arg0, (const char *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_CGContextShowText */

#ifndef NO_CGContextShowTextAtPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextShowTextAtPoint
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;

	DEBUG_CALL("CGContextShowTextAtPoint\n")

	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	CGContextShowTextAtPoint((CGContextRef)arg0, (float)arg1, (float)arg2, (const char *)lparg3, (size_t)arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
}
#endif /* NO_CGContextShowTextAtPoint */

#ifndef NO_CGContextStrokePath
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextStrokePath
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGContextStrokePath\n")

	CGContextStrokePath((CGContextRef)arg0);
}
#endif /* NO_CGContextStrokePath */

#ifndef NO_CGContextStrokeRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextStrokeRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;

	DEBUG_CALL("CGContextStrokeRect\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextStrokeRect((CGContextRef)arg0, (CGRect)*lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
}
#endif /* NO_CGContextStrokeRect */

#ifndef NO_CGContextTranslateCTM
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextTranslateCTM
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextTranslateCTM\n")

	CGContextTranslateCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
}
#endif /* NO_CGContextTranslateCTM */

#ifndef NO_CGContextSynchronize
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGContextSynchronize
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("CGContextSynchronize\n")

	CGContextSynchronize((CGContextRef)arg0);
}
#endif /* NO_CGContextSynchronize */

#ifndef NO_CGDataProviderCreateWithData
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGDataProviderCreateWithData
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("CGDataProviderCreateWithData\n")

	return (jint)CGDataProviderCreateWithData((void *)arg0, (const void *)arg1, (size_t)arg2, (void *)arg3);
}
#endif /* NO_CGDataProviderCreateWithData */

#ifndef NO_CGDataProviderRelease
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGDataProviderRelease
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGDataProviderRelease\n")

	CGDataProviderRelease((CGDataProviderRef)arg0);
}
#endif /* NO_CGDataProviderRelease */

#ifndef NO_CGImageCreate
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageCreate
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jfloatArray arg8, jboolean arg9, jint arg10)
{
	jfloat *lparg8=NULL;
	jint rc;

	DEBUG_CALL("CGImageCreate\n")

	if (arg8) lparg8 = (*env)->GetFloatArrayElements(env, arg8, NULL);
	rc = (jint)CGImageCreate((size_t)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6, (CGDataProviderRef)arg7, (const float *)lparg8, (Boolean)arg9, (CGColorRenderingIntent)arg10);
	if (arg8) (*env)->ReleaseFloatArrayElements(env, arg8, lparg8, 0);
	return rc;
}
#endif /* NO_CGImageCreate */

#ifndef NO_CGImageGetAlphaInfo
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetAlphaInfo
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetAlphaInfo\n")

	return (jint)CGImageGetAlphaInfo((CGImageRef)arg0);
}
#endif /* NO_CGImageGetAlphaInfo */

#ifndef NO_CGImageGetBitsPerComponent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetBitsPerComponent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetBitsPerComponent\n")

	return (jint)CGImageGetBitsPerComponent((CGImageRef)arg0);
}
#endif /* NO_CGImageGetBitsPerComponent */

#ifndef NO_CGImageGetBitsPerPixel
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetBitsPerPixel
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetBitsPerPixel\n")

	return (jint)CGImageGetBitsPerPixel((CGImageRef)arg0);
}
#endif /* NO_CGImageGetBitsPerPixel */

#ifndef NO_CGImageGetBytesPerRow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetBytesPerRow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetBytesPerRow\n")

	return (jint)CGImageGetBytesPerRow((CGImageRef)arg0);
}
#endif /* NO_CGImageGetBytesPerRow */

#ifndef NO_CGImageGetColorSpace
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetColorSpace
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetColorSpace\n")

	return (jint)CGImageGetColorSpace((CGImageRef)arg0);
}
#endif /* NO_CGImageGetColorSpace */

#ifndef NO_CGImageGetHeight
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetHeight
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetHeight\n")

	return (jint)CGImageGetHeight((CGImageRef)arg0);
}
#endif /* NO_CGImageGetHeight */

#ifndef NO_CGImageGetWidth
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageGetWidth
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageGetWidth\n")

	return (jint)CGImageGetWidth((CGImageRef)arg0);
}
#endif /* NO_CGImageGetWidth */

#ifndef NO_CGImageRelease
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CGImageRelease
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CGImageRelease\n")

	CGImageRelease((CGImageRef)arg0);
}
#endif /* NO_CGImageRelease */

#ifndef NO_CallNextEventHandler
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CallNextEventHandler
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CallNextEventHandler\n")

	return (jint)CallNextEventHandler((EventHandlerCallRef)arg0, (EventRef)arg1);
}
#endif /* NO_CallNextEventHandler */

#ifndef NO_CharWidth
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_CharWidth
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("CharWidth\n")

	return (jshort)CharWidth((CharParameter)arg0);
}
#endif /* NO_CharWidth */

#ifndef NO_ClearCurrentScrap
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClearCurrentScrap
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ClearCurrentScrap\n")

	return (jint)ClearCurrentScrap();
}
#endif /* NO_ClearCurrentScrap */

#ifndef NO_ClearKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(ClearKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ClearKeyboardFocus\n")

	return (jint)ClearKeyboardFocus((WindowRef)arg0);
}
#endif

#ifndef NO_ClipCGContextToRegion
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClipCGContextToRegion
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("ClipCGContextToRegion\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)ClipCGContextToRegion((CGContextRef)arg0, (const Rect *)lparg1, (RgnHandle)arg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ClipCGContextToRegion */

#ifndef NO_CloseDataBrowserContainer
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CloseDataBrowserContainer
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CloseDataBrowserContainer\n")

	return (jint)CloseDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
}
#endif /* NO_CloseDataBrowserContainer */

#ifndef NO_ClosePoly
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClosePoly
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("ClosePoly\n")

	ClosePoly();
}
#endif /* NO_ClosePoly */

#ifndef NO_CollapseWindow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CollapseWindow
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("CollapseWindow\n")

	return (jint)CollapseWindow((WindowRef)arg0, (Boolean)arg1);
}
#endif /* NO_CollapseWindow */

#ifndef NO_ConvertEventRefToEventRecord
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_ConvertEventRefToEventRecord
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	EventRecord _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("ConvertEventRefToEventRecord\n")

	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	rc = (jboolean)ConvertEventRefToEventRecord((EventRef)arg0, (EventRecord *)lparg1);
	if (arg1) setEventRecordFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_ConvertEventRefToEventRecord */

#ifndef NO_CopyBits
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyBits
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jshort arg4, jint arg5)
{
	Rect _arg2, *lparg2=NULL;
	Rect _arg3, *lparg3=NULL;

	DEBUG_CALL("CopyBits\n")

	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getRectFields(env, arg3, &_arg3);
	CopyBits((const BitMap *)arg0, (const BitMap *)arg1, (const Rect *)lparg2, (const Rect *)lparg3, (short)arg4, (RgnHandle)arg5);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg3) setRectFields(env, arg3, lparg3);
}
#endif /* NO_CopyBits */

#ifndef NO_CopyControlTitleAsCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyControlTitleAsCFString
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CopyControlTitleAsCFString\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CopyControlTitleAsCFString((ControlRef)arg0, (CFStringRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CopyControlTitleAsCFString */

#ifndef NO_CopyDeepMask
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyDeepMask
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jobject arg5, jshort arg6, jint arg7)
{
	Rect _arg3, *lparg3=NULL;
	Rect _arg4, *lparg4=NULL;
	Rect _arg5, *lparg5=NULL;

	DEBUG_CALL("CopyDeepMask\n")

	if (arg3) lparg3 = getRectFields(env, arg3, &_arg3);
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	if (arg5) lparg5 = getRectFields(env, arg5, &_arg5);
	CopyDeepMask((const BitMap *)arg0, (const BitMap *)arg1, (const BitMap *)arg2, (const Rect *)lparg3, (const Rect *)lparg4, (const Rect *)lparg5, (short)arg6, (RgnHandle)arg7);
	if (arg3) setRectFields(env, arg3, lparg3);
	if (arg4) setRectFields(env, arg4, lparg4);
	if (arg5) setRectFields(env, arg5, lparg5);
}
#endif /* NO_CopyDeepMask */

#ifndef NO_CopyMenuItemTextAsCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyMenuItemTextAsCFString
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("CopyMenuItemTextAsCFString\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CopyMenuItemTextAsCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_CopyMenuItemTextAsCFString */

#ifndef NO_CopyRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("CopyRgn\n")

	CopyRgn((RgnHandle)arg0, (RgnHandle)arg1);
}
#endif /* NO_CopyRgn */

#ifndef NO_CountMenuItems
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_CountMenuItems
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("CountMenuItems\n")

	return (jshort)CountMenuItems((MenuRef)arg0);
}
#endif /* NO_CountMenuItems */

#ifndef NO_CountSubControls
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CountSubControls
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CountSubControls\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)CountSubControls((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CountSubControls */

#ifndef NO_CreateBevelButtonControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateBevelButtonControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jshort arg4, jint arg5, jshort arg6, jshort arg7, jshort arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc;

	DEBUG_CALL("CreateBevelButtonControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	rc = (jint)CreateBevelButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlBevelThickness)arg3, (ControlBevelButtonBehavior)arg4, (ControlButtonContentInfoPtr)arg5, (SInt16)arg6, (ControlBevelButtonMenuBehavior)arg7, (ControlBevelButtonMenuPlacement)arg8, (ControlRef *)lparg9);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	return rc;
}
#endif

#ifndef NO_CreateCheckBoxControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateCheckBoxControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("CreateCheckBoxControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateCheckBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_CreateCGContextForPort
JNIEXPORT jint JNICALL OS_NATIVE(CreateCGContextForPort)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CreateCGContextForPort\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateCGContextForPort((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

#ifndef NO_CreateDataBrowserControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateDataBrowserControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("CreateDataBrowserControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateDataBrowserControl((WindowRef)arg0, (const Rect *)lparg1, (DataBrowserViewStyle)arg2, (ControlRef *)lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_CreateDataBrowserControl */

#ifndef NO_CreateEvent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateEvent
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("CreateEvent\n")

	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateEvent((CFAllocatorRef)arg0, (UInt32)arg1, (UInt32)arg2, (EventTime)arg3, (EventAttributes)arg4, (EventRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_CreateEvent */

#ifndef NO_CreateGroupBoxControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateGroupBoxControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("CreateGroupBoxControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateGroupBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (Boolean)arg3, (ControlRef *)lparg4);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_CreateNewMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateNewMenu
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("CreateNewMenu\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CreateNewMenu((MenuID)arg0, (MenuAttributes)arg1, (MenuRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_CreateNewMenu */

#ifndef NO_CreateNewWindow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateNewWindow
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	Rect _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("CreateNewWindow\n")

	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateNewWindow((WindowClass)arg0, (WindowAttributes)arg1, (const Rect *)lparg2, (WindowRef *)lparg3);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_CreateNewWindow */

#ifndef NO_CreatePopupArrowControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreatePopupArrowControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("CreatePopupArrowControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreatePopupArrowControl((WindowRef)arg0, (const Rect *)lparg1, (ControlPopupArrowOrientation)arg2, (ControlPopupArrowSize)arg3, (ControlRef *)lparg4);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_CreatePopupButtonControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreatePopupButtonControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jboolean arg4, jshort arg5, jshort arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc;

	DEBUG_CALL("CreatePopupButtonControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)CreatePopupButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt16)arg3, (Boolean)arg4, (SInt16)arg5, (SInt16)arg6, (Style)arg7, (ControlRef *)lparg8);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	return rc;
}
#endif

#ifndef NO_CreateProgressBarControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateProgressBarControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc;

	DEBUG_CALL("CreateProgressBarControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)CreateProgressBarControl((WindowRef)arg0, (const Rect *)lparg1, (SInt32)arg2, (SInt32)arg3, (SInt32) arg4, (Boolean)arg5, (ControlRef *)lparg6);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif NO_CreateProgressBarControl

#ifndef NO_CreatePushButtonControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreatePushButtonControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("CreatePushButtonControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreatePushButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlRef *)lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_CreatePushButtonControl */

#ifndef NO_CreatePushButtonWithIconControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreatePushButtonWithIconControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jobject arg3, jshort arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	ControlButtonContentInfo _arg3, *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("CreatePushButtonWithIconControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = getControlButtonContentInfoFields(env, arg3, &_arg3);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreatePushButtonWithIconControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlButtonContentInfo *)lparg3, (ControlPushButtonIconAlignment)arg4, (ControlRef *)lparg5);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg3) setControlButtonContentInfoFields(env, arg3, lparg3);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_CreateRadioButtonControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateRadioButtonControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("CreateRadioButtonControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateRadioButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_CreateRootControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateRootControl
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CreateRootControl\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateRootControl((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_CreateRootControl */

#ifndef NO_CreateScrollBarControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateScrollBarControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc;

	DEBUG_CALL("CreateScrollBarControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)CreateScrollBarControl((WindowRef)arg0, (const Rect *)lparg1, (SInt32)arg2, (SInt32)arg3, (SInt32) arg4, (ControlSliderOrientation)arg5, (Boolean)arg6, (ControlActionUPP)arg7, (ControlRef *)lparg8);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	return rc;
}
#endif NO_CreateScrollBarControl

#ifndef NO_CreateSeparatorControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateSeparatorControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("CreateSeparatorControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CreateSeparatorControl((WindowRef)arg0, (const Rect *)lparg1, (ControlRef *)lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_CreateSeparatorControl */

#ifndef NO_CreateSliderControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateSliderControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, short arg6, jboolean arg7, jint arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc;

	DEBUG_CALL("CreateSliderControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	rc = (jint)CreateSliderControl((WindowRef)arg0, (const Rect *)lparg1, (SInt32)arg2, (SInt32)arg3, (SInt32) arg4, (ControlSliderOrientation)arg5, (UInt16)arg6, (Boolean)arg7, (ControlActionUPP)arg8, (ControlRef *)lparg9);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	return rc;
}
#endif NO_CreateSliderControl

#ifndef NO_CreateStandardAlert
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateStandardAlert
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	AlertStdCFStringAlertParamRec _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("CreateStandardAlert\n")

	if (arg3) lparg3 = getAlertStdCFStringAlertParamRecFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateStandardAlert((AlertType)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (const AlertStdCFStringAlertParamRec *)lparg3, (DialogRef *)lparg4);
	if (arg3) setAlertStdCFStringAlertParamRecFields(env, arg3, lparg3);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_CreateStaticTextControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateStaticTextControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jobject arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	ControlFontStyleRec _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("CreateStaticTextControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = getControlFontStyleRecFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateStaticTextControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (const ControlFontStyleRec *)lparg3, (ControlRef *)lparg4);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg3) setControlFontStyleRecFields(env, arg3, lparg3);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_CreateStaticTextControl */

#ifndef NO_CreateTabsControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateTabsControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jshort arg4, jint arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc;

	DEBUG_CALL("CreateTabsControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)CreateTabsControl((WindowRef)arg0, (const Rect *)lparg1, (ControlTabSize)arg2, (ControlTabDirection)arg3, (UInt16)arg4, (const ControlTabEntry *)arg5, (ControlRef *)lparg6);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif

#ifndef NO_CreateEditUnicodeTextControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateEditUnicodeTextControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3, jobject arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	ControlFontStyleRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("CreateEditUnicodeTextControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = getControlFontStyleRecFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateEditUnicodeTextControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (Boolean) arg3, (const ControlFontStyleRec *)lparg4, (ControlRef *)lparg5);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg4) setControlFontStyleRecFields(env, arg4, lparg4);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_CreateEditUnicodeTextControl */

#ifndef NO_CreateUserPaneControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateUserPaneControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("CreateUserPaneControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateUserPaneControl((WindowRef)arg0, (const Rect *)lparg1, (UInt32)arg2, (ControlRef *)lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_CreateUserPaneControl */

#ifndef NO_CreateWindowGroup
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateWindowGroup
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("CreateWindowGroup\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateWindowGroup((WindowGroupAttributes)arg0, (WindowGroupRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenu
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("DeleteMenu\n")

	DeleteMenu((MenuID)arg0);
}
#endif /* NO_DeleteMenu */

#ifndef NO_DeleteMenuItem
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenuItem
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("DeleteMenuItem\n")

	DeleteMenuItem((MenuRef)arg0, (short)arg1);
}
#endif /* NO_DeleteMenuItem */

#ifndef NO_DeleteMenuItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenuItems
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	DEBUG_CALL("DeleteMenuItems\n")

	return (jint)DeleteMenuItems((MenuRef)arg0, (MenuItemIndex)arg1, (ItemCount)arg2);
}
#endif /* NO_DeleteMenuItems */

#ifndef NO_DiffRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DiffRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("DiffRgn\n")

	DiffRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
}
#endif /* NO_DiffRgn */

#ifndef NO_DisableControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableControl
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisableControl\n")

	return (jint)DisableControl((ControlRef)arg0);
}
#endif /* NO_DisableControl */

#ifndef NO_DisableMenuCommand
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableMenuCommand
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("DisableMenuCommand\n")

	DisableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
}
#endif /* NO_DisableMenuCommand */

#ifndef NO_DisableMenuItem
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableMenuItem
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("DisableMenuItem\n")

	DisableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
}
#endif /* NO_DisableMenuItem */

#ifndef NO_DisposeControl
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeControl
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeControl\n")

	DisposeControl((ControlRef)arg0);
}
#endif /* NO_DisposeControl */

#ifndef NO_DisposeGWorld
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeGWorld
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeGWorld\n")

	DisposeGWorld((GWorldPtr)arg0);
}
#endif /* NO_DisposeGWorld */

#ifndef NO_DisposeHandle
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeHandle
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeHandle\n")

	DisposeHandle((Handle)arg0);
}
#endif /* NO_DisposeHandle */

#ifndef NO_DisposeMenu
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeMenu\n")

	DisposeMenu((MenuRef)arg0);
}
#endif /* NO_DisposeMenu */

#ifndef NO_DisposePtr
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposePtr
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposePtr\n")

	DisposePtr((Ptr)arg0);
}
#endif /* NO_DisposePtr */

#ifndef NO_DisposeRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeRgn
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeRgn\n")

	DisposeRgn((RgnHandle)arg0);
}
#endif /* NO_DisposeRgn */

#ifndef NO_DisposeWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("DisposeWindow\n")

	DisposeWindow((WindowRef)arg0);
}
#endif /* NO_DisposeWindow */

#ifndef NO_DrawMenuBar
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawMenuBar
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("DrawMenuBar\n")

	DrawMenuBar();
}
#endif /* NO_DrawMenuBar */

#ifndef NO_DrawText
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawText
	(JNIEnv *env, jclass that, jbyteArray arg0, jshort arg1, jshort arg2)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("DrawText\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	DrawText((const void *)lparg0, (short)arg1, (short)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_DrawText */

#ifndef NO_DrawThemeButton
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeButton
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jobject arg2, jobject arg3, jint arg4, jint arg5, jint arg6)
{
	Rect _arg0, *lparg0=NULL;
	ThemeButtonDrawInfo _arg2, *lparg2=NULL;
	ThemeButtonDrawInfo _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("DrawThemeButton\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getThemeButtonDrawInfoFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getThemeButtonDrawInfoFields(env, arg3, &_arg3);
	rc = (jint)DrawThemeButton((Rect *)lparg0, (ThemeButtonKind)arg1, (const ThemeButtonDrawInfo *)lparg2, (const ThemeButtonDrawInfo *)lparg3, (ThemeEraseUPP)arg4, (ThemeButtonDrawUPP)arg5, (UInt32)arg6);
	if (arg0) setRectFields(env, arg0, lparg0);
	if (arg2) setThemeButtonDrawInfoFields(env, arg2, lparg2);
	if (arg3) setThemeButtonDrawInfoFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_DrawThemeButton */

#ifndef NO_DrawThemeEditTextFrame
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeEditTextFrame
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("DrawThemeEditTextFrame\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeEditTextFrame((const Rect *)lparg0, (ThemeDrawState)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_DrawThemeEditTextFrame */

#ifndef NO_DrawThemeFocusRect
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeFocusRect
	(JNIEnv *env, jclass that, jobject arg0, jboolean arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("DrawThemeFocusRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeFocusRect((const Rect *)lparg0, (Boolean)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_DrawThemeFocusRect */

#ifndef NO_DrawThemeSeparator
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeSeparator
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("DrawThemeSeparator\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeSeparator((const Rect *)lparg0, (ThemeDrawState)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_DrawThemeSeparator */

#ifndef NO_DrawThemeTextBox
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeTextBox
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshort arg5, jint arg6)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("DrawThemeTextBox\n")

	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)DrawThemeTextBox((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (const Rect *)lparg4, (SInt16)arg5, (void *)arg6);
	if (arg4) setRectFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_DrawThemeTextBox */

#ifndef NO_EmbedControl
JNIEXPORT jint JNICALL OS_NATIVE(EmbedControl)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EmbedControl\n")

	return (jint)EmbedControl((ControlRef)arg0, (ControlRef)arg1);
}
#endif

#ifndef NO_EmptyRect
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_EmptyRect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("EmptyRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jboolean)EmptyRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_EmptyRect */

#ifndef NO_EmptyRgn
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_EmptyRgn
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EmptyRgn\n")

	return (jboolean)EmptyRgn((RgnHandle)arg0);
}
#endif /* NO_EmptyRgn */

#ifndef NO_EnableControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableControl
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EnableControl\n")

	return (jint)EnableControl((ControlRef)arg0);
}
#endif /* NO_EnableControl */

#ifndef NO_EnableMenuCommand
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableMenuCommand
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("EnableMenuCommand\n")

	EnableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
}
#endif /* NO_EnableMenuCommand */

#ifndef NO_EnableMenuItem
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableMenuItem
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("EnableMenuItem\n")

	EnableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
}
#endif /* NO_EnableMenuItem */

#ifndef NO_EndUpdate
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EndUpdate
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EndUpdate\n")

	EndUpdate((WindowRef)arg0);
}
#endif /* NO_EndUpdate */

#ifndef NO_EqualRect
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_EqualRect
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("EqualRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jboolean)EqualRect((const Rect *)lparg0, (const Rect *)lparg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	if (arg1) setRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_EqualRect */

#ifndef NO_EraseRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EraseRect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("EraseRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	EraseRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_EraseRect */

#ifndef NO_EraseRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EraseRgn
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("EraseRgn\n")

	EraseRgn((RgnHandle)arg0);
}
#endif /* NO_EraseRgn */

#ifndef NO_FPIsFontPanelVisible
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_FPIsFontPanelVisible
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("FPIsFontPanelVisible\n")

	return (jboolean)FPIsFontPanelVisible();
}
#endif

#ifndef NO_FetchFontInfo
JNIEXPORT jint JNICALL OS_NATIVE(FetchFontInfo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jobject arg3)
{
	FontInfo _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("FetchFontInfo\n")

	if (arg3) lparg3 = getFontInfoFields(env, arg3, &_arg3);
	rc = (jint)FetchFontInfo(arg0, arg1, arg2, lparg3);
	if (arg3) setFontInfoFields(env, arg3, lparg3);
	return rc;
}
#endif

#ifndef NO_Fix2Long
JNIEXPORT jint JNICALL OS_NATIVE(Fix2Long)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("Fix2Long\n")
	
	return (jint)Fix2Long((Fixed)arg0);
}
#endif NO_Fix2Long

#ifndef NO_FMCreateFontFamilyIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMCreateFontFamilyIterator)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("FMCreateFontFamilyIterator\n")

	return (jint)FMCreateFontFamilyIterator((const FMFilter *)arg0, (void *)arg1, (OptionBits)arg2, (FMFontFamilyIterator *)arg3);
}
#endif

#ifndef NO_FMCreateFontFamilyInstanceIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMCreateFontFamilyInstanceIterator)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	DEBUG_CALL("FMCreateFontFamilyInstanceIterator\n")

	return (jint)FMCreateFontFamilyInstanceIterator((FMFontFamily)arg0, (FMFontFamilyInstanceIterator *)arg1);
}
#endif

#ifndef NO_FMDisposeFontFamilyIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMDisposeFontFamilyIterator)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("FMDisposeFontFamilyIterator\n")

	return (jint)FMDisposeFontFamilyIterator((FMFontFamilyIterator *)arg0);
}
#endif

#ifndef NO_FMDisposeFontFamilyInstanceIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMDisposeFontFamilyInstanceIterator)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("FMDisposeFontFamilyInstanceIterator\n")

	return (jint)FMDisposeFontFamilyInstanceIterator((FMFontFamilyInstanceIterator *)arg0);
}
#endif

#ifndef NO_FMGetFontFamilyFromName
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_FMGetFontFamilyFromName
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jshort rc;

	DEBUG_CALL("FMGetFontFamilyFromName\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jshort)FMGetFontFamilyFromName((ConstStr255Param)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_FMGetFontFamilyFromName */

#ifndef NO_FMGetFontFamilyName
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FMGetFontFamilyName
	(JNIEnv *env, jclass that, jshort arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FMGetFontFamilyName\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FMGetFontFamilyName(arg0, lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_FMGetFontFamilyName */

#ifndef NO_FMGetFontFromFontFamilyInstance
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFromFontFamilyInstance)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jintArray arg2, jshortArray arg3)
{
	jint *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("FMGetFontFromFontFamilyInstance\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)FMGetFontFromFontFamilyInstance((FMFontFamily)arg0, (FMFontStyle)arg1, (FMFont *)lparg2, (FMFontStyle *)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif

#ifndef NO_FMGetNextFontFamily
JNIEXPORT jint JNICALL OS_NATIVE(FMGetNextFontFamily)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;

	DEBUG_CALL("FMGetNextFontFamily\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)FMGetNextFontFamily((FMFontFamilyIterator *)arg0, (FMFontFamily *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif

#ifndef NO_FMGetNextFontFamilyInstance
JNIEXPORT jint JNICALL OS_NATIVE(FMGetNextFontFamilyInstance)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jshortArray arg2, jshortArray arg3)
{
	jint *lparg1=NULL;
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("FMGetNextFontFamilyInstance\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)FMGetNextFontFamilyInstance((FMFontFamilyInstanceIterator *)arg0, (FMFont *)lparg1, (FMFontStyle *)lparg2, (FMFontSize *)lparg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif

#ifndef NO_FPShowHideFontPanel
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FPShowHideFontPanel
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("FPShowHideFontPanel\n")

	return (jint)FPShowHideFontPanel();
}
#endif

#ifndef NO_FindWindow
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_FindWindow
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	Point _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jshort rc;

	DEBUG_CALL("FindWindow\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jshort)FindWindow((Point)*lparg0, (WindowRef *)lparg1);
	if (arg0) setPointFields(env, arg0, lparg0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_FindWindow */

#ifndef NO_FrameOval
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameOval
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("FrameOval\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameOval((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_FrameOval */

#ifndef NO_FramePoly
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FramePoly
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("FramePoly\n")

	FramePoly((PolyHandle)arg0);
}
#endif /* NO_FramePoly */

#ifndef NO_FrameRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameRect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("FrameRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_FrameRect */

#ifndef NO_FrameRoundRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameRoundRect
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("FrameRoundRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameRoundRect((const Rect *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_FrameRoundRect */

#ifndef NO_FrontWindow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrontWindow
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("FrontWindow\n")

	return (jint)FrontWindow();
}
#endif /* NO_FrontWindow */

#ifndef NO_GetAppFont
JNIEXPORT jshort JNICALL OS_NATIVE(GetAppFont)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetAppFont\n")

	return (jshort)GetAppFont();
}
#endif

#ifndef NO_GetApplicationEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetApplicationEventTarget
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetApplicationEventTarget\n")

	return (jint)GetApplicationEventTarget();
}
#endif /* NO_GetApplicationEventTarget */

#ifndef NO_GetAvailableWindowAttributes
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetAvailableWindowAttributes
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetAvailableWindowAttributes\n")

	return (jint)GetAvailableWindowAttributes((WindowClass)arg0);
}
#endif /* NO_GetAvailableWindowAttributes */

#ifndef NO_GetAvailableWindowPositioningBounds
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetAvailableWindowPositioningBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetAvailableWindowPositioningBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)GetAvailableWindowPositioningBounds((GDHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetAvailableWindowPositioningBounds */

#ifndef NO_GetBestControlRect
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetBestControlRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshortArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetBestControlRect\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetBestControlRect((ControlRef)arg0, (Rect *)lparg1, (SInt16 *)lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetBestControlRect */

#ifndef NO_GetCaretTime
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCaretTime
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCaretTime\n")

	return (jint)GetCaretTime();
}
#endif /* NO_GetCaretTime */

#ifndef NO_GetClip
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetClip
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetClip\n")

	GetClip((RgnHandle)arg0);
}
#endif /* NO_GetClip */

#ifndef NO_GetControl32BitMaximum
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitMaximum
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControl32BitMaximum\n")

	return (jint)GetControl32BitMaximum((ControlRef)arg0);
}
#endif /* NO_GetControl32BitMaximum */

#ifndef NO_GetControl32BitMinimum
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitMinimum
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControl32BitMinimum\n")

	return (jint)GetControl32BitMinimum((ControlRef)arg0);
}
#endif /* NO_GetControl32BitMinimum */

#ifndef NO_GetControl32BitValue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitValue
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControl32BitValue\n")

	return (jint)GetControl32BitValue((ControlRef)arg0);
}
#endif /* NO_GetControl32BitValue */

#ifndef NO_GetControlBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("GetControlBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetControlBounds((ControlRef)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetControlBounds */

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	Rect _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I\n")

	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg4) setRectFields(env, arg4, lparg4);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I */

#ifndef NO_GetControlData__ISII_3I_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISII_3I_3I
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetControlData__ISII_3I_3I\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_GetControlData__ISII_3I_3I */

#ifndef NO_GetControlData__ISII_3S_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISII_3S_3I
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jshortArray arg4, jintArray arg5)
{
	jshort *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetControlData__ISII_3S_3I\n")

	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_GetControlData__ISII_3S_3I */

#ifndef NO_GetControlData__ISII_3B_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISII_3B_3I
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetControlData__ISII_3B_3I\n")

	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_GetControlFeatures
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlFeatures
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetControlFeatures\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetControlFeatures((ControlRef)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetControlFeatures */

#ifndef NO_GetControlEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlEventTarget
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControlEventTarget\n")

	return (jint)GetControlEventTarget((ControlRef)arg0);
}
#endif /* NO_GetControlEventTarget */

#ifndef NO_GetControlOwner
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlOwner
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControlOwner\n")

	return (jint)GetControlOwner((ControlRef)arg0);
}
#endif /* NO_GetControlOwner */

#ifndef NO_GetControlProperty
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlProperty
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetControlProperty\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlProperty((ControlRef)arg0, arg1, arg2, arg3, (UInt32 *)lparg4, (void *)lparg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_GetControlReference
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlReference
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControlReference\n")

	return (jint)GetControlReference((ControlRef)arg0);
}
#endif /* NO_GetControlReference */

#ifndef NO_GetControlRegion
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlRegion
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	DEBUG_CALL("GetControlRegion\n")

	return (jint)GetControlRegion((ControlRef)arg0, (ControlPartCode)arg1, (RgnHandle)arg2);
}
#endif /* NO_GetControlRegion */

#ifndef NO_GetControlValue
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlValue
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControlValue\n")

	return (jshort)GetControlValue((ControlRef)arg0);
}
#endif /* NO_GetControlValue */

#ifndef NO_GetControlViewSize
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlViewSize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetControlViewSize\n")

	return (jint)GetControlViewSize((ControlRef)arg0);
}
#endif /* NO_GetControlViewSize */

#ifndef NO_GetCurrentEventButtonState
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentEventButtonState
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentEventButtonState\n")

	return (jint)GetCurrentEventButtonState();
}
#endif /* NO_GetCurrentEventButtonState */

#ifndef NO_GetCurrentEventLoop
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentEventLoop
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentEventLoop\n")

	return (jint)GetCurrentEventLoop();
}
#endif /* NO_GetCurrentEventLoop */

#ifndef NO_GetCurrentEventKeyModifiers
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentEventKeyModifiers
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentEventKeyModifiers\n")

	return (jint)GetCurrentEventKeyModifiers();
}
#endif /* NO_GetCurrentEventKeyModifiers */

#ifndef NO_GetCurrentEventQueue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentEventQueue
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetCurrentEventQueue\n")

	return (jint)GetCurrentEventQueue();
}
#endif /* NO_GetCurrentEventQueue */

#ifndef NO_GetCurrentProcess
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentProcess
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("GetCurrentProcess\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetCurrentProcess((ProcessSerialNumber *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetCurrentProcess */

#ifndef NO_GetCurrentScrap
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentScrap
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("GetCurrentScrap\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetCurrentScrap((ScrapRef *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetCurrentScrap */

#ifndef NO_GetDataBrowserCallbacks
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserCallbacks
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserCallbacks\n")

	if (arg1) lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1);
	rc = (jint)GetDataBrowserCallbacks((ControlRef)arg0, (DataBrowserCallbacks *)lparg1);
	if (arg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_GetDataBrowserCallbacks */

#ifndef NO_GetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserTableViewColumnPosition
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserTableViewColumnPosition\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewColumnPosition((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserTableViewColumnIndex *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserTableViewColumnPosition */

#ifndef NO_GetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserTableViewNamedColumnWidth
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserTableViewNamedColumnWidth\n")

	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16 *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserTableViewNamedColumnWidth */

#ifndef NO_GetDataBrowserItemCount
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItemCount
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserItemCount\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)GetDataBrowserItemCount((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (UInt32 *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_GetDataBrowserItemCount */

#ifndef NO_GetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItemDataButtonValue
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserItemDataButtonValue\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserItemDataButtonValue((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetDataBrowserItemDataButtonValue */

#ifndef NO_GetDataBrowserItemPartBounds
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItemPartBounds
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserItemPartBounds\n")

	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)GetDataBrowserItemPartBounds((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserPropertyPart)arg3, (Rect *)lparg4);
	if (arg4) setRectFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_GetDataBrowserItemPartBounds */

#ifndef NO_GetDataBrowserItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItems
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("GetDataBrowserItems\n")

	return (jint)GetDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (Handle)arg4);
}
#endif /* NO_GetDataBrowserItems */

#ifndef NO_GetDataBrowserItemState
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItemState
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserItemState\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserItemState((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserItemState *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserItemState */

#ifndef NO_GetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserListViewHeaderBtnHeight
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserListViewHeaderBtnHeight\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetDataBrowserListViewHeaderBtnHeight */

#ifndef NO_GetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserListViewHeaderDesc
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserListViewHeaderDesc\n")

	if (arg2) lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2);
	rc = (jint)GetDataBrowserListViewHeaderDesc((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserListViewHeaderDesc *)lparg2);
	if (arg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewItemID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserTableViewItemID
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserTableViewItemID\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewItemID((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserTableViewItemID */

#ifndef NO_GetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserTableViewItemRow
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserTableViewItemRow\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewItemRow((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserTableViewItemRow */

#ifndef NO_GetDataBrowserTableViewRowHeight
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserTableViewRowHeight
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserTableViewRowHeight\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserTableViewRowHeight((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetDataBrowserTableViewRowHeight */

#ifndef NO_GetDataBrowserScrollBarInset
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserScrollBarInset
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserScrollBarInset\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint) GetDataBrowserScrollBarInset((ControlRef)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetDataBrowserScrollBarInset */

#ifndef NO_GetDataBrowserScrollPosition
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserScrollPosition
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserScrollPosition\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserScrollPosition((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserScrollPosition */

#ifndef NO_GetDataBrowserSelectionAnchor
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserSelectionAnchor
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetDataBrowserSelectionAnchor\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserSelectionAnchor((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetDataBrowserSelectionAnchor */

#ifndef NO_GetDblTime
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDblTime
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDblTime\n")

	return (jint)GetDblTime();
}
#endif /* NO_GetDblTime */

#ifndef NO_GetDefFontSize
JNIEXPORT jshort JNICALL OS_NATIVE(GetDefFontSize)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetDefFontSize\n")

	return (jshort)GetDefFontSize();
}
#endif

#ifndef NO_GetEventClass
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventClass
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetEventClass\n")

	return (jint)GetEventClass((EventRef)arg0);
}
#endif /* NO_GetEventClass */

#ifndef NO_GetEventDispatcherTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventDispatcherTarget
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetEventDispatcherTarget\n")

	return (jint)GetEventDispatcherTarget();
}
#endif /* NO_GetEventDispatcherTarget */

#ifndef NO_GetEventKind
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventKind
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetEventKind\n")

	return (jint)GetEventKind((EventRef)arg0);
}
#endif /* NO_GetEventKind */

#ifndef NO_GetEventParameter__III_3II_3I_3B
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3I_3B
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jbyteArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3I_3B\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_GetEventParameter__III_3II_3I_3B */

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	HICommand _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getHICommandFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) setHICommandFields(env, arg6, lparg6);
	return rc;
}
#endif /* NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2 */

#ifndef NO_GetEventParameter__III_3II_3I_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3I_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3I_3I\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_GetEventParameter__III_3II_3I_3I */

#ifndef NO_GetEventParameter__III_3II_3I_3C
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3I_3C
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jcharArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jchar *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3I_3C\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetCharArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseCharArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_GetEventParameter__III_3II_3I_3C */

#ifndef NO_GetEventParameter__III_3II_3I_3S
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3I_3S
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jshortArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jshort *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3I_3S\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetShortArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseShortArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_GetEventParameter__III_3II_3I_3S */

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	CGPoint _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getCGPointFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) setCGPointFields(env, arg6, lparg6);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	Point _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getPointFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) setPointFields(env, arg6, lparg6);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	RGBColor _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getRGBColorFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) setRGBColorFields(env, arg6, lparg6);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	Rect _arg6, *lparg6=NULL;
	jint rc;

	DEBUG_CALL("GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getRectFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) setRectFields(env, arg6, lparg6);
	return rc;
}
#endif

#ifndef NO_GetEventTime
JNIEXPORT jdouble JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventTime
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetEventTime\n")

	return (jdouble)GetEventTime((EventRef)arg0);
}
#endif /* NO_GetEventTime */

#ifndef NO_GetFontInfo
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetFontInfo
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	DEBUG_CALL("GetFontInfo\n")

	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	GetFontInfo((FontInfo *)lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_GetFontInfo */

#ifndef NO_GetGDevice
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGDevice
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetGDevice\n")

	return (jint)GetGDevice();
}
#endif /* NO_GetGDevice */

#ifndef NO_GetGWorld
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGWorld
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;

	DEBUG_CALL("GetGWorld\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	GetGWorld((CGrafPtr *)lparg0, (GDHandle *)lparg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_GetGWorld */

#ifndef NO_GetGlobalMouse
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGlobalMouse
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;

	DEBUG_CALL("GetGlobalMouse\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	GetGlobalMouse((Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
}
#endif /* NO_GetGlobalMouse */

#ifndef NO_GetIconRef
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetIconRef
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetIconRef\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)GetIconRef((SInt16)arg0, (OSType)arg1, (OSType)arg2, (IconRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif

#ifndef NO_GetHandleSize
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetHandleSize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetHandleSize\n")

	return (jint)GetHandleSize((Handle)arg0);
}
#endif /* NO_GetHandleSize */

#ifndef NO_GetIndMenuItemWithCommandID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetIndMenuItemWithCommandID
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jshortArray arg4)
{
	jint *lparg3=NULL;
	jshort *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetIndMenuItemWithCommandID\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)GetIndMenuItemWithCommandID((MenuRef)arg0, (MenuCommand)arg1, (UInt32)arg2, (MenuRef *)lparg3, (MenuItemIndex *)lparg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_GetIndMenuItemWithCommandID */

#ifndef NO_GetIndexedSubControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetIndexedSubControl
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetIndexedSubControl\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetIndexedSubControl((ControlRef)arg0, (UInt16)arg1, (ControlRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetIndexedSubControl */

#ifndef NO_GetKeyboardFocus
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetKeyboardFocus
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetKeyboardFocus\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyboardFocus((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetKeyboardFocus */

#ifndef NO_GetLastUserEventTime
JNIEXPORT jdouble JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetLastUserEventTime
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetLastUserEventTime\n")

	return (jdouble)GetLastUserEventTime();
}
#endif /* NO_GetLastUserEventTime */

#ifndef NO_GetMainDevice
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMainDevice
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMainDevice\n")

	return (jint)GetMainDevice();
}
#endif /* NO_GetMainDevice */

#ifndef NO_GetMainEventQueue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMainEventQueue
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetMainEventQueue\n")

	return (jint)GetMainEventQueue();
}
#endif /* NO_GetMainEventQueue */

#ifndef NO_GetMenuCommandMark
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuCommandMark
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetMenuCommandMark\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar *)lparg2);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetMenuCommandMark */

#ifndef NO_GetMenuEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuEventTarget
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenuEventTarget\n")

	return (jint)GetMenuEventTarget((MenuRef)arg0);
}
#endif /* NO_GetMenuEventTarget */

#ifndef NO_GetMenuFont
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuFont
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetMenuFont\n")

	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuFont((MenuRef)arg0, (SInt16 *)lparg1, (UInt16 *)lparg2);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetMenuFont */

#ifndef NO_GetMenuID
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuID
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetMenuID\n")

	return (jshort)GetMenuID((MenuRef)arg0);
}
#endif /* NO_GetMenuID */

#ifndef NO_GetMenuItemCommandID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemCommandID
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetMenuItemCommandID\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemCommandID((MenuRef)arg0, (SInt16)arg1, (MenuCommand *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetMenuItemCommandID */

#ifndef NO_GetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemHierarchicalMenu
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetMenuItemHierarchicalMenu\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemHierarchicalMenu((MenuRef)arg0, (SInt16)arg1, (MenuRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetMenuItemHierarchicalMenu */

#ifndef NO_GetMenuItemRefCon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemRefCon
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetMenuItemRefCon\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetMenuItemRefCon */

#ifndef NO_GetMenuTrackingData
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuTrackingData
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MenuTrackingData _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetMenuTrackingData\n")

	if (arg1) lparg1 = getMenuTrackingDataFields(env, arg1, &_arg1);
	rc = (jint)GetMenuTrackingData((MenuRef)arg0, lparg1);
	if (arg1) setMenuTrackingDataFields(env, arg1, lparg1);
	return rc;
}
#endif

#ifndef NO_GetMouse
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMouse
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;

	DEBUG_CALL("GetMouse\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	GetMouse((Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
}
#endif /* NO_GetMouse */

#ifndef NO_GetPixBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPixBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("GetPixBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetPixBounds((PixMapHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetPixBounds */

#ifndef NO_GetPixDepth
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPixDepth
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetPixDepth\n")

	return (jshort)GetPixDepth((PixMapHandle)arg0);
}
#endif /* NO_GetPixDepth */

#ifndef NO_GetPort
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPort
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	DEBUG_CALL("GetPort\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	GetPort((GrafPtr *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_GetPort */

#ifndef NO_GetPortBitMapForCopyBits
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortBitMapForCopyBits
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetPortBitMapForCopyBits\n")

	return (jint)GetPortBitMapForCopyBits((CGrafPtr)arg0);
}
#endif /* NO_GetPortBitMapForCopyBits */

#ifndef NO_GetPortBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("GetPortBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetPortBounds((CGrafPtr)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetPortBounds */

#ifndef NO_GetPortClipRegion
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortClipRegion
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetPortClipRegion\n")

	GetPortClipRegion((CGrafPtr)arg0, (RgnHandle)arg1);
}
#endif /* NO_GetPortClipRegion */

#ifndef NO_GetPortVisibleRegion
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortVisibleRegion
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("GetPortVisibleRegion\n")

	return (jint)GetPortVisibleRegion((CGrafPtr)arg0, (RgnHandle)arg1);
}
#endif /* NO_GetPortVisibleRegion */

#ifndef NO_GetPtrSize
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPtrSize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetPtrSize\n")

	return (jint)GetPtrSize((Ptr)arg0);
}
#endif /* NO_GetPtrSize */

#ifndef NO_GetRegionBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetRegionBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("GetRegionBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetRegionBounds((RgnHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetRegionBounds */

#ifndef NO_GetRootControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetRootControl
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetRootControl\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetRootControl((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetRootControl */

#ifndef NO_GetScrapFlavorCount
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetScrapFlavorCount
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetScrapFlavorCount\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetScrapFlavorCount((ScrapRef)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetScrapFlavorCount */

#ifndef NO_GetScrapFlavorData
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetScrapFlavorData
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jbyteArray arg3)
{
	jint *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc;

	DEBUG_CALL("GetScrapFlavorData\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetScrapFlavorData((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2, (void *)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_GetScrapFlavorData */

#ifndef NO_GetScrapFlavorInfoList
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetScrapFlavorInfoList
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetScrapFlavorInfoList\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetScrapFlavorInfoList((ScrapRef)arg0, (UInt32 *)lparg1, (ScrapFlavorInfo *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetScrapFlavorInfoList */

#ifndef NO_GetScrapFlavorSize
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetScrapFlavorSize
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetScrapFlavorSize\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetScrapFlavorSize((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetScrapFlavorSize */

#ifndef NO_GetSuperControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetSuperControl
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetSuperControl\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetSuperControl((ControlRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetSuperControl */

#ifndef NO_GetThemeDrawingState
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeDrawingState
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("GetThemeDrawingState\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetThemeDrawingState((ThemeDrawingState *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_GetThemeDrawingState */

#ifndef NO_GetThemeFont
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeFont
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jbyteArray arg2, jshortArray arg3, jbyteArray arg4)
{
	jbyte *lparg2=NULL;
	jshort *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc;

	DEBUG_CALL("GetThemeFont\n")

	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)GetThemeFont((ThemeFontID)arg0, (ScriptCode)arg1, (char *)lparg2, (SInt16 *)lparg3, (Style *)lparg4);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_GetThemeFont */

#ifndef NO_GetThemeMetric
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeMetric
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetThemeTextDimensions\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetThemeMetric((ThemeMetric)arg0, (SInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetThemeMetric */

#ifndef NO_GetThemeTextDimensions
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeTextDimensions
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshortArray arg5)
{
	Point _arg4, *lparg4=NULL;
	jshort *lparg5=NULL;
	jint rc;

	DEBUG_CALL("GetThemeTextDimensions\n")

	if (arg4) lparg4 = getPointFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL);
	rc = (jint)GetThemeTextDimensions((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (Point *)lparg4, (SInt16 *)lparg5);
	if (arg4) setPointFields(env, arg4, lparg4);
	if (arg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif

#ifndef NO_GetUserFocusEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetUserFocusEventTarget
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("GetUserFocusEventTarget\n")

	return (jint)GetUserFocusEventTarget();
}
#endif /* NO_GetUserFocusEventTarget */

#ifndef NO_GetWRefCon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWRefCon
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWRefCon\n")

	return (jint)GetWRefCon((WindowRef)arg0);
}
#endif /* NO_GetWRefCon */

#ifndef NO_GetWindowBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowBounds
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;

	DEBUG_CALL("GetWindowBounds\n")

	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	GetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
}
#endif /* NO_GetWindowBounds */

#ifndef NO_GetWindowDefaultButton
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowDefaultButton
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("GetWindowDefaultButton\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowDefaultButton((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_GetWindowDefaultButton */

#ifndef NO_GetWindowEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowEventTarget
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowEventTarget\n")

	return (jint)GetWindowEventTarget((WindowRef)arg0);
}
#endif /* NO_GetWindowEventTarget */

#ifndef NO_GetWindowFromPort
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowFromPort
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowFromPort\n")

	return (jint)GetWindowFromPort((CGrafPtr)arg0);
}
#endif /* NO_GetWindowFromPort */

#ifndef NO_GetWindowGroupOfClass
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowGroupOfClass
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowGroupOfClass\n")

	return (jint)GetWindowGroupOfClass((WindowClass)arg0);
}
#endif

#ifndef NO_GetWindowModality
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowModality
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("GetWindowModality\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetWindowModality((WindowRef)arg0, (WindowModality *)lparg1, (WindowRef *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_GetWindowModality */

#ifndef NO_GetWindowPort
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowPort
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("GetWindowPort\n")

	return (jint)GetWindowPort((WindowRef)arg0);
}
#endif /* NO_GetWindowPort */

#ifndef NO_GetWindowStructureWidths
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowStructureWidths
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("GetWindowStructureWidths\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetWindowStructureWidths((WindowRef)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_GetWindowStructureWidths */

#ifndef NO_HandleControlSetCursor
JNIEXPORT jint JNICALL OS_NATIVE(HandleControlSetCursor)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbooleanArray arg3)
{
	Point _arg1, *lparg1=NULL;
	jboolean *lparg3=NULL;
	jint rc;

	DEBUG_CALL("HandleControlSetCursor\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetBooleanArrayElements(env, arg3, NULL);
	rc = (jint)HandleControlSetCursor((ControlRef)arg0, *(Point *)lparg1, (EventModifiers)arg2, (Boolean *)lparg3);
	if (arg1) setPointFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseBooleanArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif

#ifndef NO_HIComboBoxAppendTextItem
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxAppendTextItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIComboBoxAppendTextItem\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIComboBoxAppendTextItem((HIViewRef)arg0, (CFStringRef)arg1, (CFIndex *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_HIComboBoxAppendTextItem */

#ifndef NO_HIComboBoxCopyTextItemAtIndex
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxCopyTextItemAtIndex
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIComboBoxCopyTextItemAtIndex\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIComboBoxCopyTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_HIComboBoxCopyTextItemAtIndex */

#ifndef NO_HIComboBoxCreate
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxCreate
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jintArray arg5)
{
	CGRect _arg0, *lparg0=NULL;
	ControlFontStyleRec _arg2, *lparg2=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("HIComboBoxCreate\n")

	if (arg0) lparg0 = getCGRectFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getControlFontStyleRecFields(env, arg2, &_arg2);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)HIComboBoxCreate((const HIRect *)lparg0, (CFStringRef)arg1, (const ControlFontStyleRec *)lparg2, (CFArrayRef)arg3, (OptionBits)arg4, (HIViewRef *)lparg5);
	if (arg0) setCGRectFields(env, arg0, lparg0);
	if (arg2) setControlFontStyleRecFields(env, arg2, lparg2);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_HIComboBoxCreate */

#ifndef NO_HIComboBoxGetItemCount
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxGetItemCount
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIComboBoxGetItemCount\n")

	return (jint)HIComboBoxGetItemCount((HIViewRef)arg0);
}
#endif /* NO_HIComboBoxGetItemCount */

#ifndef NO_HIComboBoxInsertTextItemAtIndex
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxInsertTextItemAtIndex
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HIComboBoxInsertTextItemAtIndex\n")

	return (jint)HIComboBoxInsertTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef)arg2);
}
#endif /* NO_HIComboBoxInsertTextItemAtIndex */

#ifndef NO_HIComboBoxRemoveItemAtIndex
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIComboBoxRemoveItemAtIndex
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("HIComboBoxRemoveItemAtIndex\n")

	return (jint)HIComboBoxRemoveItemAtIndex((HIViewRef)arg0, (CFIndex)arg1);
}
#endif /* NO_HIComboBoxRemoveItemAtIndex */

#ifndef NO_HIObjectCopyClassID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIObjectCopyClassID
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIObjectCopyClassID\n")

	return (jint)HIObjectCopyClassID((HIObjectRef)arg0);
}
#endif /* NO_HIObjectCopyClassID */

#ifndef NO_HIObjectCreate
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIObjectCreate
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIObjectCreate\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIObjectCreate((CFStringRef)arg0, (EventRef)arg1, (HIObjectRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_HIObjectCreate */

#ifndef NO_HIObjectRegisterSubclass
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIObjectRegisterSubclass
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jint arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc;

	DEBUG_CALL("HIObjectRegisterSubclass\n")

	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)HIObjectRegisterSubclass((CFStringRef)arg0, (CFStringRef)arg1, (OptionBits)arg2, (EventHandlerUPP)arg3, (UInt32)arg4, (const EventTypeSpec *)lparg5, (void *)arg6, (HIObjectClassRef *)lparg7);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	return rc;
}
#endif /* NO_HIObjectRegisterSubclass */

#ifndef NO_HIViewAddSubview
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewAddSubview
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("HIViewAddSubview\n")

	return (jint)HIViewAddSubview((HIViewRef)arg0, (HIViewRef)arg1);
}
#endif /* NO_HIViewAddSubview */

#ifndef NO_HIViewClick
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewClick
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("HIViewClick\n")

	return (jint)HIViewClick((HIViewRef)arg0, (EventRef)arg1);
}
#endif /* NO_HIViewClick */

#ifndef NO_HIViewConvertPoint
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewConvertPoint
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("HIViewConvertPoint\n")

	if (arg0) lparg0 = getCGPointFields(env, arg0, &_arg0);
	rc = (jint)HIViewConvertPoint((HIPoint *)lparg0, (HIViewRef)arg1, (HIViewRef)arg2);
	if (arg0) setCGPointFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_HIViewConvertPoint */

#ifndef NO_HIViewFindByID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewFindByID
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIViewFindByID\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIViewFindByID((HIViewRef)arg0, *(HIViewID *)arg1, (HIViewRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_HIViewFindByID */

#ifndef NO_HIViewGetFirstSubview
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetFirstSubview
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewGetFirstSubview\n")

	return (jint)HIViewGetFirstSubview((HIViewRef)arg0);
}
#endif

#ifndef NO_HIViewGetFrame
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetFrame
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("HIViewGetFrame\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	rc = (jint)HIViewGetFrame((HIViewRef)arg0, (HIRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_HIViewGetFrame */

#ifndef NO_HIViewGetLastSubview
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetLastSubview
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewGetLastSubview\n")

	return (jint)HIViewGetLastSubview((HIViewRef)arg0);
}
#endif

#ifndef NO_HIViewGetNextView
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetNextView
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewGetNextView\n")

	return (jint)HIViewGetNextView((HIViewRef)arg0);
}
#endif

#ifndef NO_HIViewGetRoot
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetRoot
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewGetRoot\n")

	return (jint)HIViewGetRoot((WindowRef)arg0);
}
#endif /* NO_HIViewGetRoot */

#ifndef NO_HIViewGetSizeConstraints
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetSizeConstraints
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	CGRect _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIViewGetSizeConstraints\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getCGRectFields(env, arg2, &_arg2);
	rc = (jint)HIViewGetSizeConstraints((HIViewRef)arg0, (HISize *)lparg1, (HISize *)lparg2);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	if (arg2) setCGRectFields(env, arg2, lparg2);
	return rc;
}
#endif

#ifndef NO_HIViewGetSubviewHit
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetSubviewHit
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2, jintArray arg3)
{
	CGPoint _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("HIViewGetSubviewHit\n")
	
	if (arg1) lparg1 = getCGPointFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)HIViewGetSubviewHit((HIViewRef)arg0, (CGPoint *)lparg1, (Boolean)arg2, (HIViewRef *)lparg3);
	if (arg1) setCGPointFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_HIViewGetSubviewHit */

#ifndef NO_HIViewGetViewForMouseEvent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewGetViewForMouseEvent
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("HIViewGetViewForMouseEvent\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIViewGetViewForMouseEvent((HIViewRef)arg0, (EventRef)arg1, (HIViewRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_HIViewGetViewForMouseEvent */

#ifndef NO_HIViewIsVisible
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewIsVisible
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewIsVisible\n")

	return (jboolean) HIViewIsVisible((HIViewRef)arg0);
}
#endif /* NO_HIViewIsVisible */

#ifndef NO_HIViewRemoveFromSuperview
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewRemoveFromSuperview
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HIViewRemoveFromSuperview\n")

	return (jint)HIViewRemoveFromSuperview((HIViewRef)arg0);
}
#endif /* NO_HIViewRemoveFromSuperview */

#ifndef NO_HIViewSetBoundsOrigin
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetBoundsOrigin)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	DEBUG_CALL("HIViewSetBoundsOrigin\n")

	return (jint)HIViewSetBoundsOrigin((HIViewRef)arg0, (float)arg1, (float)arg2);
}
#endif

#ifndef NO_HIViewSetDrawingEnabled
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetDrawingEnabled
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("HIViewSetDrawingEnabled\n")

	return (jint)HIViewSetDrawingEnabled((HIViewRef)arg0, (Boolean)arg1);
}
#endif /* NO_HIViewSetDrawingEnabled */

#ifndef NO_HIViewSetFrame
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetFrame
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("HIViewSetFrame\n")

	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	rc = (jint)HIViewSetFrame((HIViewRef)arg0, (const HIRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_HIViewSetFrame */

#ifndef NO_HIViewSetNeedsDisplay
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetNeedsDisplay
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("HIViewSetNeedsDisplay\n")

	return (jint)HIViewSetNeedsDisplay((HIViewRef)arg0, (Boolean)arg1);
}
#endif /* NO_HIViewSetNeedsDisplay */

#ifndef NO_HIViewSetNeedsDisplayInRegion
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetNeedsDisplayInRegion
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("HIViewSetNeedsDisplayInRegion\n")

	return (jint)HIViewSetNeedsDisplayInRegion((HIViewRef)arg0, (RgnHandle)arg1, (Boolean)arg2);
}
#endif /* NO_HIViewSetNeedsDisplayInRegion */

#ifndef NO_HIViewSetVisible
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetVisible
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("HIViewSetVisible\n")

	return (jint)HIViewSetVisible((HIViewRef)arg0, (Boolean)arg1);
}
#endif /* NO_HIViewSetVisible */

#ifndef NO_HIViewSetZOrder
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSetZOrder
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("HIViewSetZOrder\n")

	return (jint)HIViewSetZOrder((HIViewRef)arg0, (HIViewZOrderOp)arg1, (HIViewRef)arg2);
}
#endif /* NO_HIViewSetZOrder */

#ifndef NO_HIViewSimulateClick
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HIViewSimulateClick
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;

	DEBUG_CALL("HIViewSimulateClick\n")

	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)HIViewSimulateClick((HIViewRef)arg0, (HIViewPartCode)arg1, (UInt32)arg2, (ControlPartCode *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_HIViewSimulateClick */

#ifndef NO_HandleControlClick
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_HandleControlClick
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	Point _arg1, *lparg1=NULL;
	jshort rc;

	DEBUG_CALL("HandleControlClick\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	rc = (jshort)HandleControlClick((ControlRef)arg0, (Point)*lparg1, (EventModifiers)arg2, (ControlActionUPP)arg3);
	if (arg1) setPointFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_HandleControlClick */

#ifndef NO_HiWord
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_HiWord
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HiWord\n")

	return (jshort)HiWord(arg0);
}
#endif /* NO_HiWord */

#ifndef NO_HideWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HideWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HideWindow\n")

	HideWindow((WindowRef)arg0);
}
#endif /* NO_HideWindow */

#ifndef NO_HiliteMenu
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HiliteMenu
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("HiliteMenu\n")

	HiliteMenu((MenuID)arg0);
}
#endif /* NO_HiliteMenu */

#ifndef NO_HLock
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HLock
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HLock\n")

	HLock((Handle)arg0);
}
#endif /* NO_HLock */

#ifndef NO_HMGetTagDelay
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HMGetTagDelay
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("HMGetTagDelay\n")
	
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)HMGetTagDelay((Duration *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_HMGetTagDelay */

#ifndef NO_HMHideTag
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HMHideTag
	(JNIEnv *env, jclass that, jintArray arg0)
{

	DEBUG_CALL("HMHideTag\n")
	
	return (jint)HMHideTag();
}
#endif /* NO_HMHideTag */

#ifndef NO_HMSetTagDelay
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HMSetTagDelay
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HMSetTagDelay\n")

	return (jint) HMSetTagDelay((Duration)arg0);
}
#endif /* NO_HMSetTagDelay */

#ifndef NO_HMInstallControlContentCallback
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_HMInstallControlContentCallback
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("HMInstallControlContentCallback\n")

	return (jint) HMInstallControlContentCallback((ControlRef)arg0, (HMControlContentUPP)arg1);
}
#endif /* NO_HMInstallControlContentCallback */

#ifndef NO_HUnlock
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HUnlock
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("HUnlock\n")

	HUnlock((Handle)arg0);
}
#endif /* NO_HUnlock */

#ifndef NO_InitContextualMenus
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitContextualMenus
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("InitContextualMenus\n")

	return (jint)InitContextualMenus();
}
#endif /* NO_InitContextualMenus */

#ifndef NO_InitCursor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitCursor
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("InitCursor\n")

	InitCursor();
}
#endif /* NO_InitCursor */

#ifndef NO_InitDataBrowserCallbacks
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitDataBrowserCallbacks
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCallbacks _arg0={0}, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("InitDataBrowserCallbacks\n")

	if (arg0) lparg0 = getDataBrowserCallbacksFields(env, arg0, &_arg0);
	rc = (jint)InitDataBrowserCallbacks((DataBrowserCallbacks *)lparg0);
	if (arg0) setDataBrowserCallbacksFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_InitDataBrowserCallbacks */

#ifndef NO_InitDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitDataBrowserCustomCallbacks
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCustomCallbacks _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("InitDataBrowserCustomCallbacks\n")

	if (arg0) lparg0 = getDataBrowserCustomCallbacksFields(env, arg0, &_arg0);
	rc = (jint)InitDataBrowserCustomCallbacks(lparg0);
	if (arg0) setDataBrowserCustomCallbacksFields(env, arg0, lparg0);
	return rc;
}
#endif

#ifndef NO_InsertMenu
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InsertMenu
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("InsertMenu\n")

	InsertMenu((MenuRef)arg0, (MenuID)arg1);
}
#endif /* NO_InsertMenu */

#ifndef NO_InsertMenuItemTextWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InsertMenuItemTextWithCFString
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("InsertMenuItemTextWithCFString\n")

	return (jint)InsertMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemIndex)arg2, (MenuItemAttributes)arg3, (MenuCommand)arg4);
}
#endif /* NO_InsertMenuItemTextWithCFString */

#ifndef NO_InstallEventHandler
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InstallEventHandler
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("InstallEventHandler\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)InstallEventHandler((EventTargetRef)arg0, (EventHandlerUPP)arg1, (UInt32)arg2, (const EventTypeSpec *)lparg3, (void *)arg4, (EventHandlerRef *)lparg5);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_InstallEventHandler */

#ifndef NO_InstallEventLoopTimer
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InstallEventLoopTimer
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("InstallEventLoopTimer\n")

	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)InstallEventLoopTimer((EventLoopRef)arg0, (EventTimerInterval)arg1, (EventTimerInterval)arg2, (EventLoopTimerUPP)arg3, (void *)arg4, (EventLoopTimerRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_InstallEventLoopTimer */

#ifndef NO_InvalWindowRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalWindowRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("InvalWindowRect\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	InvalWindowRect((WindowRef)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_InvalWindowRect */

#ifndef NO_InvalWindowRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalWindowRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("InvalWindowRgn\n")

	InvalWindowRgn((WindowRef)arg0, (RgnHandle)arg1);
}
#endif /* NO_InvalWindowRgn */

#ifndef NO_InvertRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvertRect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("InvertRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	InvertRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_InvertRect */

#ifndef NO_InvertRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvertRgn
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("InvertRgn\n")

	InvertRgn((RgnHandle)arg0);
}
#endif /* NO_InvertRgn */

#ifndef NO_IsControlActive
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlActive
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsControlActive\n")

	return (jboolean)IsControlActive((ControlRef)arg0);
}
#endif /* NO_IsControlActive */

#ifndef NO_IsControlEnabled
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlEnabled
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsControlEnabled\n")

	return (jboolean)IsControlEnabled((ControlRef)arg0);
}
#endif /* NO_IsControlEnabled */

#ifndef NO_IsControlVisible
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlVisible
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsControlVisible\n")

	return (jboolean)IsControlVisible((ControlRef)arg0);
}
#endif /* NO_IsControlVisible */

#ifndef NO_IsDataBrowserItemSelected
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsDataBrowserItemSelected
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("IsDataBrowserItemSelected\n")

	return (jboolean)IsDataBrowserItemSelected((ControlRef)arg0, (DataBrowserItemID)arg1);
}
#endif /* NO_IsDataBrowserItemSelected */

#ifndef NO_IsMenuCommandEnabled
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsMenuCommandEnabled
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("IsMenuCommandEnabled\n")

	return (jboolean)IsMenuCommandEnabled((MenuRef)arg0, (MenuCommand)arg1);
}
#endif /* NO_IsMenuCommandEnabled */

#ifndef NO_IsMenuItemEnabled
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsMenuItemEnabled
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("IsMenuItemEnabled\n")

	return (jboolean)IsMenuItemEnabled((MenuRef)arg0, (MenuItemIndex)arg1);
}
#endif /* NO_IsMenuItemEnabled */

#ifndef NO_IsValidControlHandle
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidControlHandle
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsValidControlHandle\n")

	return (jboolean)IsValidControlHandle((ControlRef)arg0);
}
#endif /* NO_IsValidControlHandle */

#ifndef NO_IsValidMenu
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsValidMenu\n")

	return (jboolean)IsValidMenu((MenuRef)arg0);
}
#endif /* NO_IsValidMenu */

#ifndef NO_IsValidWindowPtr
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidWindowPtr
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsValidWindowPtr\n")

	return (jboolean)IsValidWindowPtr((WindowRef)arg0);
}
#endif /* NO_IsValidWindowPtr */

#ifndef NO_IsWindowActive
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsWindowActive
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowActive\n")

	return (jboolean)IsWindowActive((WindowRef)arg0);
}
#endif /* NO_IsWindowActive */

#ifndef NO_IsWindowCollapsed
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsWindowCollapsed
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowCollapsed\n")

	return (jboolean)IsWindowCollapsed((WindowRef)arg0);
}
#endif /* NO_IsWindowCollapsed */

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsWindowVisible
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("IsWindowVisible\n")

	return (jboolean)IsWindowVisible((WindowRef)arg0);
}
#endif /* NO_IsWindowVisible */

#ifndef NO_KillPoly
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_KillPoly
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("KillPoly\n")

	KillPoly((PolyHandle)arg0);
}
#endif /* NO_KillPoly */

#ifndef NO_LineTo
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_LineTo
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	DEBUG_CALL("LineTo\n")

	LineTo((short)arg0, (short)arg1);
}
#endif /* NO_LineTo */

#ifndef NO_LoWord
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_LoWord
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("LoWord\n")

	return (jshort)LoWord(arg0);
}
#endif /* NO_LoWord */

#ifndef NO_LockPortBits
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_LockPortBits
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("LockPortBits\n")

	return (jint)LockPortBits((GrafPtr)arg0);
}
#endif /* NO_LockPortBits */

#ifndef NO_MenuSelect
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_MenuSelect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("MenuSelect\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	rc = (jint)MenuSelect((Point)*lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_MenuSelect */

#ifndef NO_MoveControl
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveControl
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	DEBUG_CALL("MoveControl\n")

	MoveControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
}
#endif /* NO_MoveControl */

#ifndef NO_MoveTo
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveTo
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	DEBUG_CALL("MoveTo\n")

	MoveTo((short)arg0, (short)arg1);
}
#endif /* NO_MoveTo */

#ifndef NO_MoveWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveWindow
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	DEBUG_CALL("MoveWindow\n")

	MoveWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
}
#endif /* NO_MoveWindow */

#ifndef NO_NavCreateChooseFolderDialog
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreateChooseFolderDialog
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("NavCreateChooseFolderDialog\n")

	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)NavCreateChooseFolderDialog((const NavDialogCreationOptions *)lparg0, (NavEventUPP)arg1, (NavObjectFilterUPP)arg2, (void *)arg3, (NavDialogRef *)lparg4);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_NavCreateChooseFolderDialog */

#ifndef NO_NavCreateGetFileDialog
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreateGetFileDialog
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg6=NULL;
	jint rc;

	DEBUG_CALL("NavCreateGetFileDialog\n")

	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)NavCreateGetFileDialog((const NavDialogCreationOptions *)lparg0, (NavTypeListHandle)arg1, (NavEventUPP)arg2, (NavPreviewUPP)arg3, (NavObjectFilterUPP)arg4, (void *)arg5, (NavDialogRef *)lparg6);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	return rc;
}
#endif /* NO_NavCreateGetFileDialog */

#ifndef NO_NavCreatePutFileDialog
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreatePutFileDialog
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg5=NULL;
	jint rc;

	DEBUG_CALL("NavCreatePutFileDialog\n")

	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)NavCreatePutFileDialog((const NavDialogCreationOptions *)lparg0, (OSType)arg1, (OSType)arg2, (NavEventUPP)arg3, (void *)arg4, (NavDialogRef *)lparg5);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_NavCreatePutFileDialog */

#ifndef NO_NavDialogDispose
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogDispose
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NavDialogDispose\n")

	NavDialogDispose((NavDialogRef)arg0);
}
#endif /* NO_NavDialogDispose */

#ifndef NO_NavDialogGetReply
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetReply
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NavReplyRecord _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("NavDialogGetReply\n")

	if (arg1) lparg1 = getNavReplyRecordFields(env, arg1, &_arg1);
	rc = (jint)NavDialogGetReply((NavDialogRef)arg0, (NavReplyRecord *)lparg1);
	if (arg1) setNavReplyRecordFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_NavDialogGetReply */

#ifndef NO_NavDialogGetSaveFileName
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetSaveFileName
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NavDialogGetSaveFileName\n")

	return (jint)NavDialogGetSaveFileName((NavDialogRef)arg0);
}
#endif /* NO_NavDialogGetSaveFileName */

#ifndef NO_NavDialogGetUserAction
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetUserAction
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NavDialogGetUserAction\n")

	return (jint)NavDialogGetUserAction((NavDialogRef)arg0);
}
#endif /* NO_NavDialogGetUserAction */

#ifndef NO_NavDialogRun
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogRun
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NavDialogRun\n")

	return (jint)NavDialogRun((NavDialogRef)arg0);
}
#endif /* NO_NavDialogRun */

#ifndef NO_NavDialogSetSaveFileName
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogSetSaveFileName
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("NavDialogSetSaveFileName\n")

	return (jint)NavDialogSetSaveFileName((NavDialogRef)arg0, (CFStringRef)arg1);
}
#endif /* NO_NavDialogSetSaveFileName */

#ifndef NO_NavGetDefaultDialogCreationOptions
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavGetDefaultDialogCreationOptions
	(JNIEnv *env, jclass that, jobject arg0)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("NavGetDefaultDialogCreationOptions\n")

	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	rc = (jint)NavGetDefaultDialogCreationOptions((NavDialogCreationOptions *)lparg0);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_NavGetDefaultDialogCreationOptions */

#ifndef NO_NewControl
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jboolean arg3, jshort arg4, jshort arg5, jshort arg6, jshort arg7, jint arg8)
{
	Rect _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc;

	DEBUG_CALL("NewControl\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)NewControl((WindowRef)arg0, (const Rect *)lparg1, (ConstStr255Param)lparg2, (Boolean)arg3, (SInt16)arg4, (SInt16)arg5, (SInt16)arg6, (SInt16)arg7, (SInt32)arg8);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_NewControl */

#ifndef NO_NewGWorldFromPtr
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewGWorldFromPtr
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint *lparg0=NULL;
	Rect _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("NewGWorldFromPtr\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	rc = (jint)NewGWorldFromPtr((GWorldPtr *)lparg0, (unsigned long)arg1, (const Rect *)lparg2, (CTabHandle)arg3, (GDHandle)arg4, (GWorldFlags)arg5, (Ptr)arg6, (long)arg7);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg2) setRectFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_NewGWorldFromPtr */

#ifndef NO_NewHandle
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewHandle
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NewHandle\n")

	return (jint)NewHandle((Size)arg0);
}
#endif /* NO_NewHandle */

#ifndef NO_NewHandleClear
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewHandleClear
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NewHandleClear\n")

	return (jint)NewHandleClear((Size)arg0);
}
#endif /* NO_NewHandleClear */

#ifndef NO_NewPtr
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewPtr
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NewPtr\n")

	return (jint)NewPtr((Size)arg0);
}
#endif /* NO_NewPtr */

#ifndef NO_NewPtrClear
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewPtrClear
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("NewPtrClear\n")

	return (jint)NewPtrClear((Size)arg0);
}
#endif /* NO_NewPtrClear */

#ifndef NO_NewRgn
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewRgn
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("NewRgn\n")

	return (jint)NewRgn();
}
#endif /* NO_NewRgn */

#ifndef NO_OffsetRect
JNIEXPORT void JNICALL OS_NATIVE(OffsetRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("OffsetRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	OffsetRect(lparg0, arg1, arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_OffsetRgn
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	DEBUG_CALL("OffsetRgn\n")

	OffsetRgn((RgnHandle)arg0, (short)arg1, (short)arg2);
}
#endif /* NO_OffsetRgn */

#ifndef NO_OpenDataBrowserContainer
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_OpenDataBrowserContainer
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("OpenDataBrowserContainer\n")

	return (jint)OpenDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
}
#endif /* NO_OpenDataBrowserContainer */

#ifndef NO_OpenPoly
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_OpenPoly
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("OpenPoly\n")

	return (jint)OpenPoly();
}
#endif /* NO_OpenPoly */

#ifndef NO_PaintOval
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintOval
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("PaintOval\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintOval((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_PaintOval */

#ifndef NO_PaintPoly
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintPoly
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("PaintPoly\n")

	PaintPoly((PolyHandle)arg0);
}
#endif /* NO_PaintPoly */

#ifndef NO_PaintRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintRect
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("PaintRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_PaintRect */

#ifndef NO_PaintRoundRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintRoundRect
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("PaintRoundRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintRoundRect((const Rect *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_PaintRoundRect */

#ifndef NO_PenSize
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PenSize
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	DEBUG_CALL("PenSize\n")

	PenSize((short)arg0, (short)arg1);
}
#endif /* NO_PenSize */

#ifndef NO_PickColor
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PickColor
	(JNIEnv *env, jclass that, jobject arg0)
{
	ColorPickerInfo _arg0, *lparg0=NULL;
	jint rc;

	DEBUG_CALL("PickColor\n")

	if (arg0) lparg0 = getColorPickerInfoFields(env, arg0, &_arg0);
	rc = (jint)PickColor((ColorPickerInfo *)lparg0);
	if (arg0) setColorPickerInfoFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PickColor */

#ifndef NO_PopUpMenuSelect
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PopUpMenuSelect
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3)
{
	DEBUG_CALL("PopUpMenuSelect\n")

	return (jint)PopUpMenuSelect((MenuRef)arg0, (short)arg1, (short)arg2, (short)arg3);
}
#endif /* NO_PopUpMenuSelect */

#ifndef NO_PostEvent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PostEvent
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	DEBUG_CALL("PostEvent\n")

	return (jint)PostEvent((EventKind)arg0, (UInt32)arg1);
}
#endif /* NO_PostEvent */

#ifndef NO_PostEventToQueue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PostEventToQueue
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	DEBUG_CALL("PostEventToQueue\n")

	return (jint)PostEventToQueue((EventQueueRef)arg0, (EventRef)arg1, (EventPriority)arg2);
}
#endif /* NO_PostEventToQueue */

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_PtInRect
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	Point _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	jboolean rc;

	DEBUG_CALL("PtInRect\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jboolean)PtInRect((Point)*lparg0, (const Rect *)lparg1);
	if (arg0) setPointFields(env, arg0, lparg0);
	if (arg1) setRectFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_PtInRect */

#ifndef NO_PtInRgn
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_PtInRgn
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Point _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("PtInRgn\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	rc = (jboolean)PtInRgn((Point)*lparg0, (RgnHandle)arg1);
	if (arg0) setPointFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_PtInRgn */

#ifndef NO_PutScrapFlavor
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PutScrapFlavor
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;

	DEBUG_CALL("PutScrapFlavor\n")

	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)PutScrapFlavor((ScrapRef)arg0, (ScrapFlavorType)arg1, (ScrapFlavorFlags)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_PutScrapFlavor */

#ifndef NO_QDBeginCGContext
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDBeginCGContext
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("QDBeginCGContext\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)QDBeginCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_QDBeginCGContext */

#ifndef NO_QDEndCGContext
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDEndCGContext
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("QDEndCGContext\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)QDEndCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_QDEndCGContext */

#ifndef NO_QDFlushPortBuffer
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDFlushPortBuffer
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("QDFlushPortBuffer\n")

	QDFlushPortBuffer((CGrafPtr)arg0, (RgnHandle)arg1);
}
#endif /* NO_QDFlushPortBuffer */

#ifndef NO_QDGlobalToLocalPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDGlobalToLocalPoint
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;

	DEBUG_CALL("QDGlobalToLocalPoint\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	QDGlobalToLocalPoint((CGrafPtr)arg0, (Point *)lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
}
#endif /* NO_QDGlobalToLocalPoint */

#ifndef NO_QDLocalToGlobalPoint
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDLocalToGlobalPoint
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;

	DEBUG_CALL("QDLocalToGlobalPoint\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	QDLocalToGlobalPoint((CGrafPtr)arg0, (Point *)lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
}
#endif /* NO_QDLocalToGlobalPoint */

#ifndef NO_QDSetPatternOrigin
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDSetPatternOrigin
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;

	DEBUG_CALL("QDSetPatternOrigin\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	QDSetPatternOrigin((Point)*lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
}
#endif /* NO_QDSetPatternOrigin */

#ifndef NO_QDSwapTextFlags
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDSwapTextFlags
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("QDSwapTextFlags\n")

	return (jint)QDSwapTextFlags((UInt32)arg0);
}
#endif /* NO_QDSwapTextFlags */

#ifndef NO_RGBBackColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RGBBackColor
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;

	DEBUG_CALL("RGBBackColor\n")

	if (arg0) lparg0 = getRGBColorFields(env, arg0, &_arg0);
	RGBBackColor((const RGBColor *)lparg0);
	if (arg0) setRGBColorFields(env, arg0, lparg0);
}
#endif /* NO_RGBBackColor */

#ifndef NO_RGBForeColor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RGBForeColor
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;

	DEBUG_CALL("RGBForeColor\n")

	if (arg0) lparg0 = getRGBColorFields(env, arg0, &_arg0);
	RGBForeColor((const RGBColor *)lparg0);
	if (arg0) setRGBColorFields(env, arg0, lparg0);
}
#endif /* NO_RGBForeColor */

#ifndef NO_ReceiveNextEvent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReceiveNextEvent
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jdouble arg2, jboolean arg3, jintArray arg4)
{
	jint *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("ReceiveNextEvent\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ReceiveNextEvent((UInt32)arg0, (const EventTypeSpec *)lparg1, (EventTimeout)arg2, (Boolean)arg3, (EventRef *)lparg4);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_ReceiveNextEvent */

#ifndef NO_RectInRgn
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_RectInRgn
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc;

	DEBUG_CALL("RectInRgn\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jboolean)RectInRgn((const Rect *)lparg0, (RgnHandle)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	return rc;
}
#endif /* NO_RectInRgn */

#ifndef NO_RectRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RectRgn
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("RectRgn\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	RectRgn((RgnHandle)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_RectRgn */

#ifndef NO_RegisterAppearanceClient
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RegisterAppearanceClient
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("RegisterAppearanceClient\n")

	return (jint)RegisterAppearanceClient();
}
#endif /* NO_RegisterAppearanceClient */

#ifndef NO_ReleaseEvent
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReleaseEvent
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ReleaseEvent\n")

	ReleaseEvent((EventRef)arg0);
}
#endif /* NO_ReleaseEvent */

#ifndef NO_ReleaseMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReleaseMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ReleaseMenu\n")

	return (jint)ReleaseMenu((MenuRef)arg0);
}
#endif /* NO_ReleaseMenu */

#ifndef NO_ReleaseWindowGroup
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReleaseWindowGroup
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ReleaseWindowGroup\n")

	return (jint)ReleaseWindowGroup((WindowGroupRef)arg0);
}
#endif

#ifndef NO_RemoveControlProperty
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveControlProperty
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("RemoveControlProperty\n")

	return (jint)RemoveControlProperty((ControlRef)arg0, arg1, arg2);
}
#endif

#ifndef NO_RemoveDataBrowserItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveDataBrowserItems
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("RemoveDataBrowserItems\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)RemoveDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_RemoveDataBrowserItems */

#ifndef NO_RemoveDataBrowserTableViewColumn
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveDataBrowserTableViewColumn
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("RemoveDataBrowserTableViewColumn\n")

	return (jint)RemoveDataBrowserTableViewColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1);

}
#endif /* NO_RemoveDataBrowserTableViewColumn */

#ifndef NO_RemoveEventHandler
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveEventHandler
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RemoveEventHandler\n")

	return (jint)RemoveEventHandler((EventHandlerRef)arg0);
}
#endif

#ifndef NO_RemoveEventLoopTimer
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveEventLoopTimer
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RemoveEventLoopTimer\n")

	return (jint)RemoveEventLoopTimer((EventLoopTimerRef)arg0);
}
#endif /* NO_RemoveEventLoopTimer */

#ifndef NO_RepositionWindow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RepositionWindow
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("RepositionWindow\n")

	return (jint)RepositionWindow((WindowRef)arg0, (WindowRef)arg1, arg2);
}
#endif

#ifndef NO_RetainMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RetainMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("RetainMenu\n")

	return (jint)RetainMenu((MenuRef)arg0);
}
#endif /* NO_RetainMenu */

#ifndef NO_RevealDataBrowserItem
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RevealDataBrowserItem
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyte arg3)
{
	DEBUG_CALL("RevealDataBrowserItem\n")

	return (jint)RevealDataBrowserItem((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserRevealOptions)arg3);
}
#endif /* NO_RevealDataBrowserItem */

#ifndef NO_RunStandardAlert
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RunStandardAlert
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;

	DEBUG_CALL("RunStandardAlert\n")

	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)RunStandardAlert((DialogRef)arg0, (ModalFilterUPP)arg1, (DialogItemIndex *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_RunStandardAlert */

#ifndef NO_ScrollRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ScrollRect
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("ScrollRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	ScrollRect((const Rect *)lparg0, (short)arg1, (short)arg2, (RgnHandle)arg3);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_ScrollRect */

#ifndef NO_SectRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SectRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	Rect _arg2, *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("SectRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	rc = (jboolean)SectRect(lparg0, lparg1, lparg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) setRectFields(env, arg2, lparg2);
	return rc;
}
#endif

#ifndef NO_SectRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SectRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SectRgn\n")

	SectRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
}
#endif /* NO_SectRgn */

#ifndef NO_SelectWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SelectWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SelectWindow\n")

	SelectWindow((WindowRef)arg0);
}
#endif /* NO_SelectWindow */

#ifndef NO_SendBehind
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SendBehind
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SendBehind\n")

	SendBehind((WindowRef)arg0, (WindowRef)arg1);
}
#endif

#ifndef NO_SendEventToEventTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SendEventToEventTarget
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SendEventToEventTarget\n")

	return (jint)SendEventToEventTarget((EventRef)arg0, (EventTargetRef)arg1);
}
#endif /* NO_SendEventToEventTarget */

#ifndef NO_SetBevelButtonContentInfo
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetBevelButtonContentInfo
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlButtonContentInfo _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("SetBevelButtonContentInfo\n")

	if (arg1) lparg1 = getControlButtonContentInfoFields(env, arg1, &_arg1);
	rc = (jint)SetBevelButtonContentInfo((ControlRef)arg0, (ControlButtonContentInfoPtr)lparg1);
	if (arg1) setControlButtonContentInfoFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_SetBevelButtonContentInfo */

#ifndef NO_SetClip
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetClip
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetClip\n")

	SetClip((RgnHandle)arg0);
}
#endif /* NO_SetClip */

#ifndef NO_SetControl32BitMaximum
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitMaximum
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControl32BitMaximum\n")

	SetControl32BitMaximum((ControlRef)arg0, (SInt32)arg1);
}
#endif /* NO_SetControl32BitMaximum */

#ifndef NO_SetControl32BitMinimum
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitMinimum
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControl32BitMinimum\n")

	SetControl32BitMinimum((ControlRef)arg0, (SInt32)arg1);
}
#endif /* NO_SetControl32BitMinimum */

#ifndef NO_SetControl32BitValue
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitValue
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControl32BitValue\n")

	SetControl32BitValue((ControlRef)arg0, (SInt32)arg1);
}
#endif /* NO_SetControl32BitValue */

#ifndef NO_SetControlAction
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlAction
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControlAction\n")

	SetControlAction((ControlRef)arg0, (ControlActionUPP)arg1);
}
#endif /* NO_SetControlAction */

#ifndef NO_SetControlBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("SetControlBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	SetControlBounds((ControlRef)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_SetControlBounds */

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2\n")

	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setRectFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2 */

#ifndef NO_SetControlData__IIII_3I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIII_3I
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIII_3I\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_SetControlData__IIII_3I */

#ifndef NO_SetControlData__IIIII
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIIII
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("SetControlData__IIIII\n")

	return (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)arg4);
}
#endif /* NO_SetControlData__IIIII */

#ifndef NO_SetControlData__IIII_3S
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIII_3S
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIII_3S\n")

	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_SetControlData__IIII_3S */

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlTabInfoRecV1 _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2\n")

	if (arg4) lparg4 = getControlTabInfoRecV1Fields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setControlTabInfoRecV1Fields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2 */

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlButtonContentInfo _arg4, *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2\n")

	if (arg4) lparg4 = getControlButtonContentInfoFields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setControlButtonContentInfoFields(env, arg4, lparg4);
	return rc;
}
#endif /* NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2 */

#ifndef NO_SetControlData__IIII_3B
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__IIII_3B
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlData__IIII_3B\n")

	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_SetControlData__IIII_3B */

#ifndef NO_SetControlFontStyle
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlFontStyle
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlFontStyleRec _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("SetControlFontStyle\n")

	if (arg1) lparg1 = getControlFontStyleRecFields(env, arg1, &_arg1);
	rc = (jint)SetControlFontStyle((ControlRef)arg0, (const ControlFontStyleRec *)lparg1);
	if (arg1) setControlFontStyleRecFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_SetControlFontStyle */

#ifndef NO_SetControlPopupMenuHandle
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlPopupMenuHandle
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControlPopupMenuHandle\n")

	SetControlPopupMenuHandle((ControlRef)arg0, (MenuRef)arg1);
}
#endif /* NO_SetControlPopupMenuHandle */

#ifndef NO_SetControlProperty
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlProperty
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetControlProperty\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)SetControlProperty((ControlRef)arg0, arg1, arg2, arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif

#ifndef NO_SetControlReference
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlReference
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControlReference\n")

	SetControlReference((ControlRef)arg0, (SInt32)arg1);
}
#endif /* NO_SetControlReference */

#ifndef NO_SetControlTitleWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlTitleWithCFString
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControlTitleWithCFString\n")

	return (jint)SetControlTitleWithCFString((ControlRef)arg0, (CFStringRef)arg1);
}
#endif /* NO_SetControlTitleWithCFString */

#ifndef NO_SetControlViewSize
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlViewSize
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetControlViewSize\n")

	SetControlViewSize((ControlRef)arg0, (SInt32)arg1);
}
#endif /* NO_SetControlViewSize */

#ifndef NO_SetCursor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetCursor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetCursor\n")

	SetCursor((const Cursor *)arg0);
}
#endif /* NO_SetCursor */

#ifndef NO_SetDataBrowserCallbacks
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserCallbacks
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1={0}, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("SetDataBrowserCallbacks\n")

	if (arg1) lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1);
	rc = (jint)SetDataBrowserCallbacks((ControlRef)arg0, (const DataBrowserCallbacks *)lparg1);
	if (arg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_SetDataBrowserCallbacks */

#ifndef NO_SetDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserCustomCallbacks
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCustomCallbacks _arg1={0}, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("SetDataBrowserCustomCallbacks\n")

	if (arg1) lparg1 = getDataBrowserCustomCallbacksFields(env, arg1, &_arg1);
	rc = (jint)SetDataBrowserCustomCallbacks((ControlRef)arg0, (const DataBrowserCustomCallbacks *)lparg1);
	if (arg1) setDataBrowserCustomCallbacksFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_SetDataBrowserCustomCallbacks */

#ifndef NO_SetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserTableViewNamedColumnWidth
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	DEBUG_CALL("SetDataBrowserTableViewNamedColumnWidth\n")

	return (jint)SetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16)arg2);
}
#endif /* NO_SetDataBrowserTableViewNamedColumnWidth */

#ifndef NO_SetDataBrowserHasScrollBars
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserHasScrollBars
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	DEBUG_CALL("SetDataBrowserHasScrollBars\n")

	return (jint)SetDataBrowserHasScrollBars((ControlRef)arg0, (Boolean)arg1, (Boolean)arg2);
}
#endif /* NO_SetDataBrowserHasScrollBars */

#ifndef NO_SetDataBrowserItemDataBooleanValue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataBooleanValue
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("SetDataBrowserItemDataBooleanValue\n")

	return (jint)SetDataBrowserItemDataBooleanValue((DataBrowserItemDataRef)arg0, (Boolean)arg1);
}
#endif /* NO_SetDataBrowserItemDataBooleanValue */

#ifndef NO_SetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataButtonValue
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("SetDataBrowserItemDataButtonValue\n")

	return (jint)SetDataBrowserItemDataButtonValue((DataBrowserItemDataRef)arg0, (ThemeButtonValue)arg1);
}
#endif /* NO_SetDataBrowserItemDataButtonValue */

#ifndef NO_SetDataBrowserItemDataIcon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataIcon
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetDataBrowserItemDataIcon\n")

	return (jint)SetDataBrowserItemDataIcon((DataBrowserItemDataRef)arg0, (IconRef)arg1);
}
#endif /* NO_SetDataBrowserItemDataIcon */

#ifndef NO_SetDataBrowserItemDataItemID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataItemID
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetDataBrowserItemDataItemID\n")

	return (jint)SetDataBrowserItemDataItemID((DataBrowserItemDataRef)arg0, (DataBrowserItemID)arg1);
}
#endif /* NO_SetDataBrowserItemDataItemID */

#ifndef NO_SetDataBrowserItemDataText
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataText
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetDataBrowserItemDataText\n")

	return (jint)SetDataBrowserItemDataText((DataBrowserItemDataRef)arg0, (CFStringRef)arg1);
}
#endif /* NO_SetDataBrowserItemDataText */

#ifndef NO_SetDataBrowserListViewDisclosureColumn
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserListViewDisclosureColumn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("SetDataBrowserListViewDisclosureColumn\n")

	return (jint)SetDataBrowserListViewDisclosureColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (Boolean)arg2);
}
#endif /* NO_SetDataBrowserListViewDisclosureColumn */

#ifndef NO_SetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserListViewHeaderBtnHeight
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	DEBUG_CALL("SetDataBrowserListViewHeaderBtnHeight\n")

	return (jint)SetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, (UInt16)arg1);
}
#endif /* NO_SetDataBrowserListViewHeaderBtnHeight */


#ifndef NO_SetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserListViewHeaderDesc
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SetDataBrowserListViewHeaderDesc\n")

	if (arg2) lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2);
	rc = (jint)SetDataBrowserListViewHeaderDesc((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserListViewHeaderDesc *)lparg2);
	if (arg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserScrollPosition
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserScrollPosition
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetDataBrowserScrollPosition\n")

	return (jint)SetDataBrowserScrollPosition((ControlRef)arg0, (UInt32)arg1, (UInt32)arg2);
}
#endif /* NO_SetDataBrowserScrollPosition */

#ifndef NO_SetDataBrowserSelectedItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserSelectedItems
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("SetDataBrowserSelectedItems\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SetDataBrowserSelectedItems((ControlRef)arg0, (UInt32)arg1, (const DataBrowserItemID *)lparg2, (DataBrowserSetOption)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_SetDataBrowserSelectedItems */

#ifndef NO_SetDataBrowserSelectionFlags
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserSelectionFlags
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetDataBrowserSelectionFlags\n")

	return (jint)SetDataBrowserSelectionFlags((ControlRef)arg0, (DataBrowserSelectionFlags)arg1);
}
#endif /* NO_SetDataBrowserSelectionFlags */

#ifndef NO_SetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserTableViewColumnPosition
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{

	DEBUG_CALL("SetDataBrowserTableViewColumnPosition\n")

	return (jint)SetDataBrowserTableViewColumnPosition((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserTableViewColumnIndex)arg2);

}
#endif /* NO_SetDataBrowserTableViewColumnPosition */

#ifndef NO_SetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserTableViewItemRow
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{

	DEBUG_CALL("SetDataBrowserTableViewItemRow\n")

	return (jint)SetDataBrowserTableViewItemRow((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserTableViewRowIndex)arg2);

}
#endif /* NO_SetDataBrowserTableViewItemRow */

#ifndef NO_SetDataBrowserTarget
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserTarget
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetDataBrowserTarget\n")

	return (jint)SetDataBrowserTarget((ControlRef)arg0, (DataBrowserItemID)arg1);
}
#endif /* NO_SetDataBrowserTarget */

#ifndef NO_SetEventLoopTimerNextFireTime
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetEventLoopTimerNextFireTime
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	DEBUG_CALL("SetEventLoopTimerNextFireTime\n")

	return (jint)SetEventLoopTimerNextFireTime((EventLoopTimerRef)arg0, (EventTimerInterval)arg1);
}
#endif /* NO_SetEventLoopTimerNextFireTime */

#ifndef NO_SetEventParameter
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetEventParameter
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc;

	DEBUG_CALL("SetEventParameter\n")

	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_SetEventParameter */

#ifndef NO_SetFontInfoForSelection
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetFontInfoForSelection
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("SetFontInfoForSelection\n")

	return (jint)SetFontInfoForSelection((OSType)arg0, (UInt32)arg1, (void *)arg2, (HIObjectRef)arg3);
}
#endif

#ifndef NO_SetFrontProcess
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetFrontProcess
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;

	DEBUG_CALL("SetFrontProcess\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)SetFrontProcess((const ProcessSerialNumber *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_SetFrontProcess */

#ifndef NO_SetGWorld
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetGWorld
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetGWorld\n")

	SetGWorld((CGrafPtr)arg0, (GDHandle)arg1);
}
#endif /* NO_SetGWorld */

#ifndef NO_SetKeyboardFocus
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetKeyboardFocus
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	DEBUG_CALL("SetKeyboardFocus\n")

	return (jint)SetKeyboardFocus((WindowRef)arg0, (ControlRef)arg1, (ControlFocusPart)arg2);
}
#endif /* NO_SetKeyboardFocus */

#ifndef NO_SetMenuCommandMark
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuCommandMark
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jchar arg2)
{
	DEBUG_CALL("SetMenuCommandMark\n")

	return (jint)SetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar)arg2);
}
#endif /* NO_SetMenuCommandMark */

#ifndef NO_SetMenuFont
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuFont
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	DEBUG_CALL("SetMenuFont\n")

	return (jint)SetMenuFont((MenuRef)arg0, (SInt16)arg1, (UInt16)arg2);
}
#endif /* NO_SetMenuFont */

#ifndef NO_SetMenuItemCommandKey
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemCommandKey
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2, jchar arg3)
{
	DEBUG_CALL("SetMenuItemCommandKey\n")

	return (jint)SetMenuItemCommandKey((MenuRef)arg0, (MenuItemIndex)arg1, (Boolean)arg2, (UInt16)arg3);
}
#endif /* NO_SetMenuItemCommandKey */

#ifndef NO_SetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemHierarchicalMenu
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	DEBUG_CALL("SetMenuItemHierarchicalMenu\n")

	return (jint)SetMenuItemHierarchicalMenu((MenuRef)arg0, (MenuItemIndex)arg1, (MenuRef)arg2);
}
#endif /* NO_SetMenuItemHierarchicalMenu */

#ifndef NO_SetMenuItemIconHandle
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemIconHandle
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2, jint arg3)
{
	DEBUG_CALL("SetMenuItemIconHandle\n")

	return (jint)SetMenuItemIconHandle((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2, (Handle)arg3);
}
#endif /* NO_SetMenuItemIconHandle */

#ifndef NO_SetMenuItemKeyGlyph
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemKeyGlyph
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	DEBUG_CALL("SetMenuItemKeyGlyph\n")

	return (jint)SetMenuItemKeyGlyph((MenuRef)arg0, (SInt16)arg1, (SInt16)arg2);
}
#endif /* NO_SetMenuItemKeyGlyph */

#ifndef NO_SetMenuItemModifiers
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemModifiers
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2)
{
	DEBUG_CALL("SetMenuItemModifiers\n")

	return (jint)SetMenuItemModifiers((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2);
}
#endif /* NO_SetMenuItemModifiers */

#ifndef NO_SetMenuItemRefCon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemRefCon
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	DEBUG_CALL("SetMenuItemRefCon\n")

	return (jint)SetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32)arg2);
}
#endif /* NO_SetMenuItemRefCon */

#ifndef NO_SetMenuItemTextWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemTextWithCFString
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	DEBUG_CALL("SetMenuItemTextWithCFString\n")

	return (jint)SetMenuItemTextWithCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef)arg2);
}
#endif /* NO_SetMenuItemTextWithCFString */

#ifndef NO_SetMenuTitleWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuTitleWithCFString
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetMenuTitleWithCFString\n")

	return (jint)SetMenuTitleWithCFString((MenuRef)arg0, (CFStringRef)arg1);
}
#endif /* NO_SetMenuTitleWithCFString */

#ifndef NO_SetOrigin
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetOrigin
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	DEBUG_CALL("SetOrigin\n")

	SetOrigin((short)arg0, (short)arg1);
}
#endif /* NO_SetOrigin */

#ifndef NO_SetPort
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPort
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetPort\n")

	SetPort((GrafPtr)arg0);
}
#endif /* NO_SetPort */

#ifndef NO_SetPortBounds
JNIEXPORT void JNICALL OS_NATIVE(SetPortBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("SetPortBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	SetPortBounds((CGrafPtr)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif

#ifndef NO_SetPortWindowPort
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPortWindowPort
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetPortWindowPort\n")

	SetPortWindowPort((WindowRef)arg0);
}
#endif /* NO_SetPortWindowPort */

#ifndef NO_SetPt
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPt
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Point _arg0, *lparg0=NULL;

	DEBUG_CALL("SetPt\n")

	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	SetPt((Point *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setPointFields(env, arg0, lparg0);
}
#endif /* NO_SetPt */

#ifndef NO_SetRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetRect
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("SetRect\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	SetRect((Rect *)lparg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif /* NO_SetRect */

#ifndef NO_SetRectRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetRectRgn
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	DEBUG_CALL("SetRectRgn\n")

	SetRectRgn((RgnHandle)arg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
}
#endif /* NO_SetRectRgn */

#ifndef NO_SetRootMenu
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetRootMenu
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetRootMenu\n")

	return (jint)SetRootMenu((MenuRef)arg0);
}
#endif /* NO_SetRootMenu */

#ifndef NO_SetThemeBackground
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeBackground
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2)
{
	DEBUG_CALL("SetThemeBackground\n")

	return (jint)SetThemeBackground((ThemeBrush)arg0, (SInt16)arg1, (Boolean)arg2);
}
#endif /* NO_SetThemeBackground */

#ifndef NO_SetThemeCursor
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeCursor
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("SetThemeCursor\n")

	return (jint)SetThemeCursor((ThemeCursor)arg0);
}
#endif /* NO_SetThemeCursor */

#ifndef NO_SetThemeDrawingState
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeDrawingState
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("SetThemeDrawingState\n")

	return (jint)SetThemeDrawingState((ThemeDrawingState)arg0, (Boolean)arg1);
}
#endif /* NO_SetThemeDrawingState */

#ifndef NO_SetThemeWindowBackground
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeWindowBackground
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	DEBUG_CALL("SetThemeWindowBackground\n")

	return (jint)SetThemeWindowBackground((WindowRef)arg0, (ThemeBrush)arg1, (Boolean)arg2);
}
#endif /* NO_SetThemeWindowBackground */

#ifndef NO_SetUpControlBackground
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetUpControlBackground
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	DEBUG_CALL("SetUpControlBackground\n")

	return (jint)SetUpControlBackground((ControlRef)arg0, (SInt16)arg1, (Boolean)arg2);
}
#endif /* NO_SetUpControlBackground */

#ifndef NO_SetWRefCon
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWRefCon
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWRefCon\n")

	SetWRefCon((WindowRef)arg0, (long)arg1);
}
#endif /* NO_SetWRefCon */

#ifndef NO_SetWindowActivationScope
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowActivationScope
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowActivationScope\n")

	return (jint)SetWindowActivationScope((WindowRef)arg0, (WindowActivationScope)arg1);
}
#endif /* NO_SetWindowActivationScope */

#ifndef NO_SetWindowBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowBounds
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;

	DEBUG_CALL("SetWindowBounds\n")

	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	SetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
}
#endif /* NO_SetWindowBounds */

#ifndef NO_SetWindowDefaultButton
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowDefaultButton
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowDefaultButton\n")

	return (jint)SetWindowDefaultButton((WindowRef)arg0, (ControlRef)arg1);
}
#endif /* NO_SetWindowDefaultButton */

#ifndef NO_SetWindowGroup
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowGroup
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowGroup\n")

	return (jint)SetWindowGroup((WindowRef)arg0, (WindowGroupRef)arg1);
}
#endif

#ifndef NO_SetWindowGroupOwner
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowGroupOwner
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowGroupOwner\n")

	return (jint)SetWindowGroupOwner((WindowGroupRef)arg0, (WindowRef)arg1);
}
#endif

#ifndef NO_SetWindowGroupParent
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowGroupParent
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowGroupParent\n")

	return (jint)SetWindowGroupParent((WindowGroupRef)arg0, (WindowGroupRef)arg1);
}
#endif

#ifndef NO_SetWindowModality
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowModality
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("SetWindowModality\n")

	return (jint)SetWindowModality((WindowRef)arg0, (WindowModality)arg1, (WindowRef)arg2);
}
#endif /* NO_SetWindowModality */

#ifndef NO_SetWindowTitleWithCFString
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowTitleWithCFString
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SetWindowTitleWithCFString\n")

	return (jint)SetWindowTitleWithCFString((WindowRef)arg0, (CFStringRef)arg1);
}
#endif /* NO_SetWindowTitleWithCFString */

#ifndef NO_ShowWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ShowWindow
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("ShowWindow\n")

	ShowWindow((WindowRef)arg0);
}
#endif /* NO_ShowWindow */

#ifndef NO_SizeControl
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SizeControl
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	DEBUG_CALL("SizeControl\n")

	SizeControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
}
#endif /* NO_SizeControl */

#ifndef NO_SizeWindow
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SizeWindow
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	DEBUG_CALL("SizeWindow\n")

	SizeWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
}
#endif /* NO_SizeWindow */

#ifndef NO_StillDown
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_StillDown
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("StillDown\n")

	return (jboolean)StillDown();
}
#endif /* NO_StillDown */

#ifndef NO_SyncCGContextOriginWithPort
JNIEXPORT jint JNICALL OS_NATIVE(SyncCGContextOriginWithPort)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("SyncCGContextOriginWithPort\n")

	return (jint)SyncCGContextOriginWithPort((CGContextRef)arg0, (CGrafPtr)arg1);
}
#endif

#ifndef NO_SysBeep
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SysBeep
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("SysBeep\n")

	SysBeep((short)arg0);
}
#endif /* NO_SysBeep */

#ifndef NO_TXNActivate
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNActivate
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	DEBUG_CALL("TXNActivate\n")

	return (jint)TXNActivate((TXNObject)arg0, (TXNFrameID)arg1, (TXNScrollBarState)arg2);
}
#endif /* NO_TXNActivate */

#ifndef NO_TXNAdjustCursor
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNAdjustCursor
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("TXNActivate\n")

	TXNAdjustCursor((TXNObject)arg0, (RgnHandle)arg1);
}
#endif /* NO_TXNAdjustCursor */

#ifndef NO_TXNClick
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNClick
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	EventRecord _arg1, *lparg1=NULL;

	DEBUG_CALL("TXNClick\n")

	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	TXNClick((TXNObject)arg0, (const EventRecord *)lparg1);
	if (arg1) setEventRecordFields(env, arg1, lparg1);
}
#endif /* NO_TXNClick */

#ifndef NO_TXNCopy
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNCopy
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNCopy\n")

	return (jint)TXNCopy((TXNObject)arg0);
}
#endif /* NO_TXNCopy */

#ifndef NO_TXNCut
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNCut
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNCut\n")

	return (jint)TXNCut((TXNObject)arg0);
}
#endif /* NO_TXNCut */

#ifndef NO_TXNDataSize
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDataSize
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNDataSize\n")

	return (jint)TXNDataSize((TXNObject)arg0);
}
#endif /* NO_TXNDataSize */

#ifndef NO_TXNDeleteObject
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDeleteObject
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNDeleteObject\n")

	TXNDeleteObject((TXNObject)arg0);
}
#endif /* NO_TXNDeleteObject */

#ifndef NO_TXNDraw
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDraw
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("TXNDraw\n")

	TXNDraw((TXNObject)arg0, (GWorldPtr)arg1);
}
#endif /* NO_TXNDraw */

#ifndef NO_TXNEchoMode
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNEchoMode
	(JNIEnv *env, jclass that, jint arg0, jchar arg1, jint arg2, jboolean arg3)
{
	DEBUG_CALL("TXNEchoMode\n")

	return (jint)TXNEchoMode((TXNObject)arg0, (UniChar)arg1, (TextEncoding)arg2, (Boolean)arg3);
}
#endif /* NO_TXNEchoMode */

#ifndef NO_TXNFocus
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNFocus
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("TXNFocus\n")

	TXNFocus((TXNObject)arg0, (Boolean)arg1);
}
#endif /* NO_TXNFocus */

#ifndef NO_TXNGetRectBounds
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetRectBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jobject arg3)
{
	Rect _arg1, *lparg1=NULL;
	TXNLongRect _arg2, *lparg2=NULL;
	TXNLongRect _arg3, *lparg3=NULL;
	jint rc;

	DEBUG_CALL("TXNGetRectBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getTXNLongRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getTXNLongRectFields(env, arg3, &_arg3);
	rc = (jint)TXNGetRectBounds((TXNObject)arg0, (Rect *)lparg1, (TXNLongRect *)lparg2, (TXNLongRect *)lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) setTXNLongRectFields(env, arg2, lparg2);
	if (arg3) setTXNLongRectFields(env, arg3, lparg3);
	return rc;
}
#endif /* NO_TXNGetRectBounds */

#ifndef NO_TXNGetData
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetData
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("TXNGetData\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetData((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2, (Handle *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_TXNGetData */

#ifndef NO_TXNGetLineCount
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetLineCount
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;

	DEBUG_CALL("TXNGetLineCount\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)TXNGetLineCount((TXNObject)arg0, (ItemCount *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	return rc;
}
#endif /* NO_TXNGetLineCount */

#ifndef NO_TXNGetLineMetrics
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetLineMetrics
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("TXNGetLineMetrics\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetLineMetrics((TXNObject)arg0, (UInt32)arg1, (Fixed *)lparg2, (Fixed *)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_TXNGetLineMetrics*/

#ifndef NO_TXNGetViewRect
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetViewRect
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("TXNGetViewRect\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	TXNGetViewRect((TXNObject)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_TXNGetViewRect */

#ifndef NO_TXNGetTXNObjectControls
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetTXNObjectControls
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("TXNGetTXNObjectControls\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetTXNObjectControls((TXNObject)arg0, (ItemCount)arg1, (const TXNControlTag *)lparg2, (TXNControlData *)lparg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_TXNGetTXNObjectControls */

#ifndef NO_TXNGetSelection
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetSelection
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;

	DEBUG_CALL("TXNGetSelection\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	TXNGetSelection((TXNObject)arg0, (TXNOffset *)lparg1, (TXNOffset *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
}
#endif /* NO_TXNGetSelection */

#ifndef NO_TXNInitTextension
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNInitTextension
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("TXNInitTextension\n")

	return (jint)TXNInitTextension((const TXNMacOSPreferredFontDescription *)arg0, (ItemCount)arg1, (TXNInitOptions)arg2);
}
#endif /* NO_TXNInitTextension */

#ifndef NO_TXNNewObject
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNNewObject
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7, jintArray arg8, jint arg9)
{
	Rect _arg2, *lparg2=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc;

	DEBUG_CALL("TXNNewObject\n")

	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)TXNNewObject((const FSSpec *)arg0, (WindowRef)arg1, (const Rect *)lparg2, (TXNFrameOptions)arg3, (TXNFrameType)arg4, (TXNFileType)arg5, (TXNPermanentTextEncodingType)arg6, (TXNObject *)lparg7, (TXNFrameID *)lparg8, (TXNObjectRefcon)arg9);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	return rc;
}
#endif /* NO_TXNNewObject */

#ifndef NO_TXNOffsetToPoint
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNOffsetToPoint
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	Point _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TXNOffsetToPoint\n")

	if (arg2) lparg2 = getPointFields(env, arg2, &_arg2);
	rc = (jint)TXNOffsetToPoint((TXNObject)arg0, (TXNOffset)arg1, (Point *)lparg2);
	if (arg2) setPointFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_TXNOffsetToPoint */

#ifndef NO_TXNPaste
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNPaste
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNPaste\n")

	return (jint)TXNPaste((TXNObject)arg0);
}
#endif /* NO_TXNPaste */

#ifndef NO_TXNPointToOffset
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNPointToOffset
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	Point _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TXNPointToOffset\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)TXNPointToOffset((TXNObject)arg0, (Point)*lparg1, (TXNOffset *)lparg2);
	if (arg1) setPointFields(env, arg1, lparg1);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_TXNPointToOffset */

#ifndef NO_TXNSelectAll
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSelectAll
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("TXNSelectAll\n")

	TXNSelectAll((TXNObject)arg0);
}
#endif /* NO_TXNSelectAll */

#ifndef NO_TXNSetData
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetData
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg2=NULL;
	jint rc;

	DEBUG_CALL("TXNSetData\n")

	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)TXNSetData((TXNObject)arg0, (TXNDataType)arg1, (const void *)lparg2, (ByteCount)arg3, (TXNOffset)arg4, (TXNOffset)arg5);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif /* NO_TXNSetData */

#ifndef NO_TXNSetFrameBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetFrameBounds
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("TXNSetFrameBounds\n")

	TXNSetFrameBounds((TXNObject)arg0, (SInt32)arg1, (SInt32)arg2, (SInt32)arg3, (SInt32)arg4, (TXNFrameID)arg5);
}
#endif /* NO_TXNSetFrameBounds */

#ifndef NO_TXNSetRectBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetRectBounds
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jboolean arg3)
{
	Rect _arg1, *lparg1=NULL;
	TXNLongRect _arg2, *lparg2=NULL;

	DEBUG_CALL("TXNSetRectBounds\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getTXNLongRectFields(env, arg2, &_arg2);
	TXNSetRectBounds((TXNObject)arg0, (Rect *)lparg1, (TXNLongRect *)lparg2, (Boolean)arg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg2) setTXNLongRectFields(env, arg2, lparg2);
}
#endif /* NO_TXNSetRectBounds */

#ifndef NO_TXNSetSelection
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetSelection
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("TXNSetSelection\n")

	return (jint)TXNSetSelection((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2);
}
#endif /* NO_TXNSetSelection */

#ifndef NO_TXNSetTXNObjectControls
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetTXNObjectControls
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;

	DEBUG_CALL("TXNSetTXNObjectControls\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)TXNSetTXNObjectControls((TXNObject)arg0, (Boolean)arg1, (ItemCount)arg2, (const TXNControlTag *)lparg3, (const TXNControlData *)lparg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	return rc;
}
#endif /* NO_TXNSetTXNObjectControls */

#ifndef NO_TXNShowSelection
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNShowSelection
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("TXNShowSelection\n")

	TXNShowSelection((TXNObject)arg0, (Boolean)arg1);
}
#endif /* NO_TXNShowSelection */

#ifndef NO_TestControl
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_TestControl
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;
	jshort rc;

	DEBUG_CALL("TestControl\n")

	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	rc = (jshort)TestControl((ControlRef)arg0, (Point)*lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
	return rc;
}
#endif /* NO_TestControl */

#ifndef NO_TextFace
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextFace
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("TextFace\n")

	TextFace((StyleParameter)arg0);
}
#endif /* NO_TextFace */

#ifndef NO_TextFont
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextFont
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("TextFont\n")

	TextFont((short)arg0);
}
#endif /* NO_TextFont */

#ifndef NO_TextMode
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextMode
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("TextMode\n")

	TextMode((short)arg0);
}
#endif /* NO_TextMode */

#ifndef NO_TextSize
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextSize
	(JNIEnv *env, jclass that, jshort arg0)
{
	DEBUG_CALL("TextSize\n")

	TextSize((short)arg0);
}
#endif /* NO_TextSize */

#ifndef NO_TextWidth
JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextWidth
	(JNIEnv *env, jclass that, jbyteArray arg0, jshort arg1, jshort arg2)
{
	jbyte *lparg0=NULL;
	jshort rc;

	DEBUG_CALL("TextWidth\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jshort)TextWidth((const void *)lparg0, (short)arg1, (short)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif /* NO_TextWidth */

#ifndef NO_TrackMouseLocationWithOptions
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TrackMouseLocationWithOptions
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jobject arg3, jintArray arg4, jshortArray arg5)
{
	Point _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jshort *lparg5=NULL;
	jint rc;

	DEBUG_CALL("TrackMouseLocation\n")

	if (arg3) lparg3 = getPointFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL);
	rc = (jint)TrackMouseLocationWithOptions((GrafPtr)arg0, (OptionBits)arg1, (EventTimeout)arg2, (Point *)lparg3, (UInt32 *)lparg4, (MouseTrackingResult *)lparg5);
	if (arg3) setPointFields(env, arg3, lparg3);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	return rc;
}
#endif /* NO_TrackMouseLocationWithOptions */

#ifndef NO_UnionRgn
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_UnionRgn
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("UnionRgn\n")

	UnionRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
}
#endif /* NO_UnionRgn */

#ifndef NO_UnlockPortBits
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_UnlockPortBits
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("UnlockPortBits\n")

	return (jint)UnlockPortBits((GrafPtr)arg0);
}
#endif /* NO_UnlockPortBits */

#ifndef NO_UpdateControls
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_UpdateControls
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("UpdateControls\n")

	UpdateControls((WindowRef)arg0, (RgnHandle)arg1);
}
#endif /* NO_UpdateControls */

#ifndef NO_UpdateDataBrowserItems
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_UpdateDataBrowserItems
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("UpdateDataBrowserItems\n")

	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)UpdateDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4, (DataBrowserPropertyID)arg5);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif /* NO_UpdateDataBrowserItems */

#ifndef NO_kHIViewWindowContentID
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_kHIViewWindowContentID
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("kHIViewWindowContentID\n")

	return (jint)&kHIViewWindowContentID;
}
#endif /* NO_kHIViewWindowContentID */

#ifndef NO_memcpy__I_3BI
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__I_3BI
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;

	DEBUG_CALL("memcpy__I_3BI\n")

	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
}
#endif /* NO_memcpy__I_3BI */

#ifndef NO_memcpy__III
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__III
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("memcpy__III\n")

	memcpy((void *)arg0, (const void *)arg1, (size_t)arg2);
}
#endif /* NO_memcpy__III */

#ifndef NO_memcpy___3BII
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy___3BII
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;

	DEBUG_CALL("memcpy___3BII\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_memcpy___3BII */

#ifndef NO_memcpy___3CII
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy___3CII
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;

	DEBUG_CALL("memcpy___3CII\n")

	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_memcpy___3CII */

#ifndef NO_memcpy___3III
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy___3III
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;

	DEBUG_CALL("memcpy___3III\n")

	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
}
#endif /* NO_memcpy___3III */

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	ATSTrapezoid _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II\n")

	if (arg0) lparg0 = getATSTrapezoidFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setATSTrapezoidFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memcpy__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;

	DEBUG_CALL("memcpy__I_3CI\n")

	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
}
#endif

#ifndef NO_memcpy__I_3II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;

	DEBUG_CALL("memcpy__I_3II\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
}
#endif


#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GDevice _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II\n")

	if (arg0) lparg0 = getGDeviceFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGDeviceFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	BitMap _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I\n")

	if (arg1) lparg1 = getBitMapFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setBitMapFields(env, arg1, lparg1);
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Cursor _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I\n")

	if (arg1) lparg1 = getCursorFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setCursorFields(env, arg1, lparg1);
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PixMap _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I\n")

	if (arg1) lparg1 = getPixMapFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setPixMapFields(env, arg1, lparg1);
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PixMap _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II\n")

	if (arg0) lparg0 = getPixMapFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setPixMapFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I\n")

	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setRectFields(env, arg1, lparg1);
}
#endif /* NO_memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I */

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	FontSelectionQDStyle _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I\n")

	if (arg1) lparg1 = getFontSelectionQDStyleFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setFontSelectionQDStyleFields(env, arg1, lparg1);
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FontSelectionQDStyle _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II\n")

	if (arg0) lparg0 = getFontSelectionQDStyleFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setFontSelectionQDStyleFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HMHelpContentRec _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II\n")

	if (arg0) lparg0 = getHMHelpContentRecFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setHMHelpContentRecFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	HMHelpContentRec _arg1, *lparg1=NULL;

	DEBUG_CALL("memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I\n")

	if (arg1) lparg1 = getHMHelpContentRecFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) setHMHelpContentRecFields(env, arg1, lparg1);
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	Rect _arg0, *lparg0=NULL;

	DEBUG_CALL("memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II\n")

	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
}
#endif

#ifndef NO_memset
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_memset
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("memset\n")

	memset((void *)arg0, arg1, arg2);
}
#endif /* NO_memset */

#ifndef NO_ZoomWindowIdeal
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ZoomWindowIdeal
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Point _arg2, *lparg2=NULL;
	jint rc;

	DEBUG_CALL("ZoomWindowIdeal\n")

	if (arg2) lparg2 = getPointFields(env, arg2, &_arg2);
	rc = (jint)ZoomWindowIdeal((WindowRef)arg0, (WindowPartCode)arg1, (Point *)lparg2);
	if (arg2) setPointFields(env, arg2, lparg2);
	return rc;
}
#endif /* NO_ZoomWindowIdeal */

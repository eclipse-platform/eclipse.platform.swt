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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_carbon_OS_##func

#ifndef NO_AECountItems
JNIEXPORT jint JNICALL OS_NATIVE(AECountItems)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	AEDesc _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AECountItems\n")
	if (arg0) lparg0 = getAEDescFields(env, arg0, &_arg0);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)AECountItems((const AEDescList *)lparg0, (long *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0) setAEDescFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "AECountItems\n")
	return rc;
}
#endif

#ifndef NO_AEGetNthPtr
JNIEXPORT jint JNICALL OS_NATIVE(AEGetNthPtr)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	AEDesc _arg0, *lparg0=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AEGetNthPtr\n")
	if (arg0) lparg0 = getAEDescFields(env, arg0, &_arg0);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)AEGetNthPtr((const AEDescList *)lparg0, arg1, (DescType)arg2, (AEKeyword *)lparg3, (DescType *)lparg4, (void *)arg5, (Size)arg6, (Size *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0) setAEDescFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "AEGetNthPtr\n")
	return rc;
}
#endif

#ifndef NO_AEProcessAppleEvent
JNIEXPORT jint JNICALL OS_NATIVE(AEProcessAppleEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	EventRecord _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AEProcessAppleEvent\n")
	if (arg0) lparg0 = getEventRecordFields(env, arg0, &_arg0);
	rc = (jint)AEProcessAppleEvent((const EventRecord *)lparg0);
	if (arg0) setEventRecordFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "AEProcessAppleEvent\n")
	return rc;
}
#endif

#ifndef NO_ATSFontGetPostScriptName
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontGetPostScriptName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSFontGetPostScriptName\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)ATSFontGetPostScriptName((ATSFontRef)arg0, (ATSOptionFlags)arg1, (CFStringRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ATSFontGetPostScriptName\n")
	return rc;
}
#endif

#ifndef NO_ATSUBatchBreakLines
JNIEXPORT jint JNICALL OS_NATIVE(ATSUBatchBreakLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUBatchBreakLines\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ATSUBatchBreakLines((ATSUTextLayout)arg0, arg1, arg2, arg3, (ItemCount *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "ATSUBatchBreakLines\n")
	return rc;
}
#endif

#ifndef NO_ATSUCreateStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateStyle)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUCreateStyle\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)ATSUCreateStyle((ATSUStyle *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ATSUCreateStyle\n")
	return rc;
}
#endif

#ifndef NO_ATSUCreateTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateTextLayout)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUCreateTextLayout\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)ATSUCreateTextLayout((ATSUTextLayout *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ATSUCreateTextLayout\n")
	return rc;
}
#endif

#ifndef NO_ATSUCreateTextLayoutWithTextPtr
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateTextLayoutWithTextPtr)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUCreateTextLayoutWithTextPtr\n")
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)ATSUCreateTextLayoutWithTextPtr((ConstUniCharArrayPtr)arg0, arg1, arg2, arg3, arg4, (const UniCharCount *)lparg5, (ATSUStyle *)lparg6, (ATSUTextLayout *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "ATSUCreateTextLayoutWithTextPtr\n")
	return rc;
}
#endif

#ifndef NO_ATSUDisposeStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeStyle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUDisposeStyle\n")
	rc = (jint)ATSUDisposeStyle((ATSUStyle)arg0);
	NATIVE_EXIT(env, that, "ATSUDisposeStyle\n")
	return rc;
}
#endif

#ifndef NO_ATSUDisposeTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeTextLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUDisposeTextLayout\n")
	rc = (jint)ATSUDisposeTextLayout((ATSUTextLayout)arg0);
	NATIVE_EXIT(env, that, "ATSUDisposeTextLayout\n")
	return rc;
}
#endif

#ifndef NO_ATSUDrawText
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDrawText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUDrawText\n")
	rc = (jint)ATSUDrawText((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (UniCharCount)arg2, (ATSUTextMeasurement)arg3, (ATSUTextMeasurement)arg4);
	NATIVE_EXIT(env, that, "ATSUDrawText\n")
	return rc;
}
#endif

#ifndef NO_ATSUFindFontName
JNIEXPORT jint JNICALL OS_NATIVE(ATSUFindFontName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6, jintArray arg7, jintArray arg8)
{
	jbyte *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUFindFontName\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)ATSUFindFontName((ATSUFontID)arg0, arg1, arg2, arg3, arg4, arg5, (Ptr)lparg6, lparg7, lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "ATSUFindFontName\n")
	return rc;
}
#endif

#ifndef NO_ATSUGetFontIDs
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetFontIDs)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jintArray arg2)
{
	jint *lparg0=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUGetFontIDs\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)ATSUGetFontIDs((ATSUFontID *)lparg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "ATSUGetFontIDs\n")
	return rc;
}
#endif

#ifndef NO_ATSUGetGlyphBounds__IIIIISII_3I
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetGlyphBounds__IIIIISII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6, jint arg7, jintArray arg8)
{
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUGetGlyphBounds__IIIIISII_3I\n")
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)ATSUGetGlyphBounds((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, (UniCharArrayOffset)arg3, arg4, arg5, arg6, (ATSTrapezoid *)arg7, (ItemCount *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "ATSUGetGlyphBounds__IIIIISII_3I\n")
	return rc;
}
#endif

#ifndef NO_ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6, jobject arg7, jintArray arg8)
{
	ATSTrapezoid _arg7, *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I\n")
	if (arg7) lparg7 = getATSTrapezoidFields(env, arg7, &_arg7);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)ATSUGetGlyphBounds((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, (UniCharArrayOffset)arg3, arg4, arg5, arg6, (ATSTrapezoid *)lparg7, (ItemCount *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) setATSTrapezoidFields(env, arg7, lparg7);
	NATIVE_EXIT(env, that, "ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I\n")
	return rc;
}
#endif

#ifndef NO_ATSUGetSoftLineBreaks
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetSoftLineBreaks)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUGetSoftLineBreaks\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)ATSUGetSoftLineBreaks((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (UniCharCount)arg2, (ItemCount)arg3, (UniCharArrayOffset *)lparg4, (ItemCount *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "ATSUGetSoftLineBreaks\n")
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
	NATIVE_ENTER(env, that, "ATSUSetAttributes\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ATSUSetAttributes((ATSUStyle)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ATSUSetAttributes\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetFontFeatures
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetFontFeatures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetFontFeatures\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)ATSUSetFontFeatures((ATSUStyle)arg0, (ItemCount)arg1, (const ATSUFontFeatureType *)lparg2, (const ATSUFontFeatureSelector *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ATSUSetFontFeatures\n")
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
	NATIVE_ENTER(env, that, "ATSUSetLayoutControls\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ATSUSetLayoutControls((ATSUTextLayout)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "ATSUSetLayoutControls\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetRunStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetRunStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetRunStyle\n")
	rc = (jint)ATSUSetRunStyle((ATSUTextLayout)arg0, (ATSUStyle)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3);
	NATIVE_EXIT(env, that, "ATSUSetRunStyle\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetSoftLineBreak
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetSoftLineBreak)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetSoftLineBreak\n")
	rc = (jint)ATSUSetSoftLineBreak((ATSUTextLayout)arg0, arg1);
	NATIVE_EXIT(env, that, "ATSUSetSoftLineBreak\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetTabArray
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTabArray)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetTabArray\n")
	rc = (jint)ATSUSetTabArray((ATSUTextLayout)arg0, (const ATSUTab *)arg1, arg2);
	NATIVE_EXIT(env, that, "ATSUSetTabArray\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetTextPointerLocation
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTextPointerLocation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetTextPointerLocation\n")
	rc = (jint)ATSUSetTextPointerLocation((ATSUTextLayout)arg0, (ConstUniCharArrayPtr)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3, (UniCharCount)arg4);
	NATIVE_EXIT(env, that, "ATSUSetTextPointerLocation\n")
	return rc;
}
#endif

#ifndef NO_ATSUSetTransientFontMatching
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTransientFontMatching)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUSetTransientFontMatching\n")
	rc = (jint)ATSUSetTransientFontMatching((ATSUTextLayout)arg0, arg1);
	NATIVE_EXIT(env, that, "ATSUSetTransientFontMatching\n")
	return rc;
}
#endif

#ifndef NO_ATSUTextDeleted
JNIEXPORT jint JNICALL OS_NATIVE(ATSUTextDeleted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUTextDeleted\n")
	rc = (jint)ATSUTextDeleted((ATSUTextLayout)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ATSUTextDeleted\n")
	return rc;
}
#endif

#ifndef NO_ATSUTextInserted
JNIEXPORT jint JNICALL OS_NATIVE(ATSUTextInserted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "ATSUTextInserted\n")
	rc = (jint)ATSUTextInserted((ATSUTextLayout)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "ATSUTextInserted\n")
	return rc;
}
#endif

#ifndef NO_ActiveNonFloatingWindow
JNIEXPORT jint JNICALL OS_NATIVE(ActiveNonFloatingWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "ActiveNonFloatingWindow\n")
	rc = (jint)ActiveNonFloatingWindow();
	NATIVE_EXIT(env, that, "ActiveNonFloatingWindow\n")
	return rc;
}
#endif

#ifndef NO_AddDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(AddDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AddDataBrowserItems\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)AddDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "AddDataBrowserItems\n")
	return rc;
}
#endif

#ifndef NO_AddDataBrowserListViewColumn
JNIEXPORT jint JNICALL OS_NATIVE(AddDataBrowserListViewColumn)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DataBrowserListViewColumnDesc _arg1={0}, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AddDataBrowserListViewColumn\n")
	if (arg1) lparg1 = getDataBrowserListViewColumnDescFields(env, arg1, &_arg1);
	rc = (jint)AddDataBrowserListViewColumn((ControlRef)arg0, (DataBrowserListViewColumnDesc *)lparg1, (DataBrowserTableViewColumnIndex)arg2);
	if (arg1) setDataBrowserListViewColumnDescFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "AddDataBrowserListViewColumn\n")
	return rc;
}
#endif

#ifndef NO_AddDragItemFlavor
JNIEXPORT jint JNICALL OS_NATIVE(AddDragItemFlavor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AddDragItemFlavor\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)AddDragItemFlavor((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (const void *)lparg3, (Size)arg4, (FlavorFlags)arg5);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "AddDragItemFlavor\n")
	return rc;
}
#endif

#ifndef NO_AppendMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(AppendMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "AppendMenuItemTextWithCFString\n")
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)AppendMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemAttributes)arg2, (MenuCommand)arg3, (MenuItemIndex *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "AppendMenuItemTextWithCFString\n")
	return rc;
}
#endif

#ifndef NO_AutoSizeDataBrowserListViewColumns
JNIEXPORT jint JNICALL OS_NATIVE(AutoSizeDataBrowserListViewColumns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "AutoSizeDataBrowserListViewColumns\n")
	rc = (jint)AutoSizeDataBrowserListViewColumns((ControlRef)arg0);
	NATIVE_EXIT(env, that, "AutoSizeDataBrowserListViewColumns\n")
	return rc;
}
#endif

#ifndef NO_BeginUpdate
JNIEXPORT void JNICALL OS_NATIVE(BeginUpdate)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "BeginUpdate\n")
	BeginUpdate((WindowRef)arg0);
	NATIVE_EXIT(env, that, "BeginUpdate\n")
}
#endif

#ifndef NO_BringToFront
JNIEXPORT void JNICALL OS_NATIVE(BringToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "BringToFront\n")
	BringToFront((WindowRef)arg0);
	NATIVE_EXIT(env, that, "BringToFront\n")
}
#endif

#ifndef NO_CFArrayAppendValue
JNIEXPORT void JNICALL OS_NATIVE(CFArrayAppendValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CFArrayAppendValue\n")
	CFArrayAppendValue((CFMutableArrayRef)arg0, (const void *)arg1);
	NATIVE_EXIT(env, that, "CFArrayAppendValue\n")
}
#endif

#ifndef NO_CFArrayCreateMutable
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayCreateMutable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFArrayCreateMutable\n")
	rc = (jint)CFArrayCreateMutable((CFAllocatorRef)arg0, (CFIndex)arg1, (const CFArrayCallBacks *)arg2);
	NATIVE_EXIT(env, that, "CFArrayCreateMutable\n")
	return rc;
}
#endif

#ifndef NO_CFArrayGetCount
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayGetCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFArrayGetCount\n")
	rc = (jint)CFArrayGetCount((CFArrayRef)arg0);
	NATIVE_EXIT(env, that, "CFArrayGetCount\n")
	return rc;
}
#endif

#ifndef NO_CFArrayGetValueAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayGetValueAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFArrayGetValueAtIndex\n")
	rc = (jint)CFArrayGetValueAtIndex((CFArrayRef)arg0, arg1);
	NATIVE_EXIT(env, that, "CFArrayGetValueAtIndex\n")
	return rc;
}
#endif

#ifndef NO_CFRelease
JNIEXPORT void JNICALL OS_NATIVE(CFRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CFRelease\n")
	CFRelease((CFTypeRef)arg0);
	NATIVE_EXIT(env, that, "CFRelease\n")
}
#endif

#ifndef NO_CFStringCreateWithBytes
JNIEXPORT jint JNICALL OS_NATIVE(CFStringCreateWithBytes)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jboolean arg4)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CFStringCreateWithBytes\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CFStringCreateWithBytes((CFAllocatorRef)arg0, (const UInt8 *)lparg1, (CFIndex)arg2, (CFStringEncoding)arg3, arg4);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CFStringCreateWithBytes\n")
	return rc;
}
#endif

#ifndef NO_CFStringCreateWithCharacters
JNIEXPORT jint JNICALL OS_NATIVE(CFStringCreateWithCharacters)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CFStringCreateWithCharacters\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	rc = (jint)CFStringCreateWithCharacters((CFAllocatorRef)arg0, (const UniChar *)lparg1, (CFIndex)arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CFStringCreateWithCharacters\n")
	return rc;
}
#endif

#ifndef NO_CFStringGetBytes
JNIEXPORT jint JNICALL OS_NATIVE(CFStringGetBytes)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbyte arg3, jboolean arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	CFRange _arg1, *lparg1=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CFStringGetBytes\n")
	if (arg1) lparg1 = getCFRangeFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)CFStringGetBytes((CFStringRef)arg0, *(CFRange *)lparg1, (CFStringEncoding)arg2, (UInt8)arg3, (Boolean)arg4, (UInt8 *)lparg5, (CFIndex)arg6, (CFIndex *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg1) setCFRangeFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CFStringGetBytes\n")
	return rc;
}
#endif

#ifndef NO_CFStringGetCharacters
JNIEXPORT void JNICALL OS_NATIVE(CFStringGetCharacters)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jcharArray arg2)
{
	CFRange _arg1, *lparg1=NULL;
	jchar *lparg2=NULL;
	NATIVE_ENTER(env, that, "CFStringGetCharacters\n")
	if (arg1) lparg1 = getCFRangeFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	CFStringGetCharacters((CFStringRef)arg0, *(CFRange *)lparg1, (UniChar *)lparg2);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1) setCFRangeFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CFStringGetCharacters\n")
}
#endif

#ifndef NO_CFStringGetLength
JNIEXPORT jint JNICALL OS_NATIVE(CFStringGetLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFStringGetLength\n")
	rc = (jint)CFStringGetLength((CFStringRef)arg0);
	NATIVE_EXIT(env, that, "CFStringGetLength\n")
	return rc;
}
#endif

#ifndef NO_CFStringGetSystemEncoding
JNIEXPORT jint JNICALL OS_NATIVE(CFStringGetSystemEncoding)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFStringGetSystemEncoding\n")
	rc = (jint)CFStringGetSystemEncoding();
	NATIVE_EXIT(env, that, "CFStringGetSystemEncoding\n")
	return rc;
}
#endif

#ifndef NO_CFURLCopyFileSystemPath
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCopyFileSystemPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCopyFileSystemPath\n")
	rc = (jint)CFURLCopyFileSystemPath((CFURLRef)arg0, (CFURLPathStyle)arg1);
	NATIVE_EXIT(env, that, "CFURLCopyFileSystemPath\n")
	return rc;
}
#endif

#ifndef NO_CFURLCopyLastPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCopyLastPathComponent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCopyLastPathComponent\n")
	rc = (jint)CFURLCopyLastPathComponent((CFURLRef)arg0);
	NATIVE_EXIT(env, that, "CFURLCopyLastPathComponent\n")
	return rc;
}
#endif

#ifndef NO_CFURLCreateCopyAppendingPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateCopyAppendingPathComponent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCreateCopyAppendingPathComponent\n")
	rc = (jint)CFURLCreateCopyAppendingPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringRef)arg2, (Boolean)arg3);
	NATIVE_EXIT(env, that, "CFURLCreateCopyAppendingPathComponent\n")
	return rc;
}
#endif

#ifndef NO_CFURLCreateCopyDeletingLastPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateCopyDeletingLastPathComponent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCreateCopyDeletingLastPathComponent\n")
	rc = (jint)CFURLCreateCopyDeletingLastPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1);
	NATIVE_EXIT(env, that, "CFURLCreateCopyDeletingLastPathComponent\n")
	return rc;
}
#endif

#ifndef NO_CFURLCreateFromFSRef
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateFromFSRef)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCreateFromFSRef\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)CFURLCreateFromFSRef((CFAllocatorRef)arg0, (const struct FSRef *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CFURLCreateFromFSRef\n")
	return rc;
}
#endif

#ifndef NO_CFURLCreateWithFileSystemPath
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateWithFileSystemPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CFURLCreateWithFileSystemPath\n")
	rc = (jint)CFURLCreateWithFileSystemPath((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFURLPathStyle)arg2, arg3);
	NATIVE_EXIT(env, that, "CFURLCreateWithFileSystemPath\n")
	return rc;
}
#endif

#ifndef NO_CFURLGetFSRef
JNIEXPORT jboolean JNICALL OS_NATIVE(CFURLGetFSRef)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "CFURLGetFSRef\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)CFURLGetFSRef((CFURLRef)arg0, (struct FSRef *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CFURLGetFSRef\n")
	return rc;
}
#endif

#ifndef NO_CGBitmapContextCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGBitmapContextCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGBitmapContextCreate\n")
	rc = (jint)CGBitmapContextCreate((void *)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6);
	NATIVE_EXIT(env, that, "CGBitmapContextCreate\n")
	return rc;
}
#endif

#ifndef NO_CGColorSpaceCreateDeviceRGB
JNIEXPORT jint JNICALL OS_NATIVE(CGColorSpaceCreateDeviceRGB)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGColorSpaceCreateDeviceRGB\n")
	rc = (jint)CGColorSpaceCreateDeviceRGB();
	NATIVE_EXIT(env, that, "CGColorSpaceCreateDeviceRGB\n")
	return rc;
}
#endif

#ifndef NO_CGColorSpaceRelease
JNIEXPORT void JNICALL OS_NATIVE(CGColorSpaceRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGColorSpaceRelease\n")
	CGColorSpaceRelease((CGColorSpaceRef)arg0);
	NATIVE_EXIT(env, that, "CGColorSpaceRelease\n")
}
#endif

#ifndef NO_CGContextAddArc
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddArc)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jboolean arg6)
{
	NATIVE_ENTER(env, that, "CGContextAddArc\n")
	CGContextAddArc((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4, (float)arg5, (Boolean)arg6);
	NATIVE_EXIT(env, that, "CGContextAddArc\n")
}
#endif

#ifndef NO_CGContextAddArcToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddArcToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	NATIVE_ENTER(env, that, "CGContextAddArcToPoint\n")
	CGContextAddArcToPoint((CGContextRef)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "CGContextAddArcToPoint\n")
}
#endif

#ifndef NO_CGContextAddLineToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddLineToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "CGContextAddLineToPoint\n")
	CGContextAddLineToPoint((CGContextRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "CGContextAddLineToPoint\n")
}
#endif

#ifndef NO_CGContextAddLines
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddLines)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextAddLines\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextAddLines((CGContextRef)arg0, (const CGPoint *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextAddLines\n")
}
#endif

#ifndef NO_CGContextBeginPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextBeginPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextBeginPath\n")
	CGContextBeginPath((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextBeginPath\n")
}
#endif

#ifndef NO_CGContextClearRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextClearRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextClearRect\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextClearRect((CGContextRef)arg0, *(CGRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CGContextClearRect\n")
}
#endif

#ifndef NO_CGContextClip
JNIEXPORT void JNICALL OS_NATIVE(CGContextClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextClip\n")
	CGContextClip((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextClip\n")
}
#endif

#ifndef NO_CGContextClosePath
JNIEXPORT void JNICALL OS_NATIVE(CGContextClosePath)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextClosePath\n")
	CGContextClosePath((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextClosePath\n")
}
#endif

#ifndef NO_CGContextDrawImage
JNIEXPORT void JNICALL OS_NATIVE(CGContextDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextDrawImage\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextDrawImage((CGContextRef)arg0, *(CGRect *)lparg1, (CGImageRef)arg2);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CGContextDrawImage\n")
}
#endif

#ifndef NO_CGContextEOFillPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextEOFillPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextEOFillPath\n")
	CGContextEOFillPath((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextEOFillPath\n")
}
#endif

#ifndef NO_CGContextFillPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextFillPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextFillPath\n")
	CGContextFillPath((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextFillPath\n")
}
#endif

#ifndef NO_CGContextFillRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextFillRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextFillRect\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextFillRect((CGContextRef)arg0, *(CGRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CGContextFillRect\n")
}
#endif

#ifndef NO_CGContextFlush
JNIEXPORT void JNICALL OS_NATIVE(CGContextFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextFlush\n")
	CGContextFlush((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextFlush\n")
}
#endif

#ifndef NO_CGContextMoveToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextMoveToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "CGContextMoveToPoint\n")
	CGContextMoveToPoint((CGContextRef)arg0, (float)arg1, (float)arg2);
	NATIVE_EXIT(env, that, "CGContextMoveToPoint\n")
}
#endif

#ifndef NO_CGContextRelease
JNIEXPORT void JNICALL OS_NATIVE(CGContextRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextRelease\n")
	CGContextRelease((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextRelease\n")
}
#endif

#ifndef NO_CGContextRestoreGState
JNIEXPORT void JNICALL OS_NATIVE(CGContextRestoreGState)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextRestoreGState\n")
	CGContextRestoreGState((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextRestoreGState\n")
}
#endif

#ifndef NO_CGContextSaveGState
JNIEXPORT void JNICALL OS_NATIVE(CGContextSaveGState)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextSaveGState\n")
	CGContextSaveGState((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextSaveGState\n")
}
#endif

#ifndef NO_CGContextScaleCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextScaleCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "CGContextScaleCTM\n")
	CGContextScaleCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
	NATIVE_EXIT(env, that, "CGContextScaleCTM\n")
}
#endif

#ifndef NO_CGContextSelectFont
JNIEXPORT void JNICALL OS_NATIVE(CGContextSelectFont)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jfloat arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextSelectFont\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	CGContextSelectFont((CGContextRef)arg0, (const char *)lparg1, (float)arg2, (CGTextEncoding)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextSelectFont\n")
}
#endif

#ifndef NO_CGContextSetFillColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFillColor)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextSetFillColor\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetFillColor((CGContextRef)arg0, (const float *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextSetFillColor\n")
}
#endif

#ifndef NO_CGContextSetFillColorSpace
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFillColorSpace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetFillColorSpace\n")
	CGContextSetFillColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
	NATIVE_EXIT(env, that, "CGContextSetFillColorSpace\n")
}
#endif

#ifndef NO_CGContextSetFont
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetFont\n")
	CGContextSetFont((CGContextRef)arg0, (CGFontRef)arg1);
	NATIVE_EXIT(env, that, "CGContextSetFont\n")
}
#endif

#ifndef NO_CGContextSetFontSize
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFontSize)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetFontSize\n")
	CGContextSetFontSize((CGContextRef)arg0, (float)arg1);
	NATIVE_EXIT(env, that, "CGContextSetFontSize\n")
}
#endif

#ifndef NO_CGContextSetLineCap
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetLineCap\n")
	CGContextSetLineCap((CGContextRef)arg0, arg1);
	NATIVE_EXIT(env, that, "CGContextSetLineCap\n")
}
#endif

#ifndef NO_CGContextSetLineDash
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineDash)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloatArray arg2, jint arg3)
{
	jfloat *lparg2=NULL;
	NATIVE_ENTER(env, that, "CGContextSetLineDash\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	CGContextSetLineDash((CGContextRef)arg0, (float)arg1, (const float *)lparg2, (size_t)arg3);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "CGContextSetLineDash\n")
}
#endif

#ifndef NO_CGContextSetLineWidth
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineWidth)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetLineWidth\n")
	CGContextSetLineWidth((CGContextRef)arg0, (float)arg1);
	NATIVE_EXIT(env, that, "CGContextSetLineWidth\n")
}
#endif

#ifndef NO_CGContextSetRGBFillColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetRGBFillColor)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	NATIVE_ENTER(env, that, "CGContextSetRGBFillColor\n")
	CGContextSetRGBFillColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
	NATIVE_EXIT(env, that, "CGContextSetRGBFillColor\n")
}
#endif

#ifndef NO_CGContextSetRGBStrokeColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetRGBStrokeColor)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	NATIVE_ENTER(env, that, "CGContextSetRGBStrokeColor\n")
	CGContextSetRGBStrokeColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
	NATIVE_EXIT(env, that, "CGContextSetRGBStrokeColor\n")
}
#endif

#ifndef NO_CGContextSetShouldAntialias
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetShouldAntialias)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetShouldAntialias\n")
	CGContextSetShouldAntialias((CGContextRef)arg0, arg1);
	NATIVE_EXIT(env, that, "CGContextSetShouldAntialias\n")
}
#endif

#ifndef NO_CGContextSetShouldSmoothFonts
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetShouldSmoothFonts)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetShouldSmoothFonts\n")
	CGContextSetShouldSmoothFonts((CGContextRef)arg0, arg1);
	NATIVE_EXIT(env, that, "CGContextSetShouldSmoothFonts\n")
}
#endif

#ifndef NO_CGContextSetStrokeColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetStrokeColor)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextSetStrokeColor\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetStrokeColor((CGContextRef)arg0, (const float *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextSetStrokeColor\n")
}
#endif

#ifndef NO_CGContextSetStrokeColorSpace
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetStrokeColorSpace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetStrokeColorSpace\n")
	CGContextSetStrokeColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
	NATIVE_EXIT(env, that, "CGContextSetStrokeColorSpace\n")
}
#endif

#ifndef NO_CGContextSetTextDrawingMode
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextDrawingMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CGContextSetTextDrawingMode\n")
	CGContextSetTextDrawingMode((CGContextRef)arg0, (CGTextDrawingMode)arg1);
	NATIVE_EXIT(env, that, "CGContextSetTextDrawingMode\n")
}
#endif

#ifndef NO_CGContextSetTextMatrix
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextMatrix)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextSetTextMatrix\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	CGContextSetTextMatrix((CGContextRef)arg0, *(CGAffineTransform *)lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextSetTextMatrix\n")
}
#endif

#ifndef NO_CGContextSetTextPosition
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextPosition)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "CGContextSetTextPosition\n")
	CGContextSetTextPosition((CGContextRef)arg0, (float)arg1, (float)arg2);
	NATIVE_EXIT(env, that, "CGContextSetTextPosition\n")
}
#endif

#ifndef NO_CGContextShowText
JNIEXPORT void JNICALL OS_NATIVE(CGContextShowText)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextShowText\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	CGContextShowText((CGContextRef)arg0, (const char *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CGContextShowText\n")
}
#endif

#ifndef NO_CGContextShowTextAtPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextShowTextAtPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	NATIVE_ENTER(env, that, "CGContextShowTextAtPoint\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	CGContextShowTextAtPoint((CGContextRef)arg0, (float)arg1, (float)arg2, (const char *)lparg3, (size_t)arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "CGContextShowTextAtPoint\n")
}
#endif

#ifndef NO_CGContextStrokePath
JNIEXPORT void JNICALL OS_NATIVE(CGContextStrokePath)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextStrokePath\n")
	CGContextStrokePath((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextStrokePath\n")
}
#endif

#ifndef NO_CGContextStrokeRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextStrokeRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "CGContextStrokeRect\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	CGContextStrokeRect((CGContextRef)arg0, *(CGRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CGContextStrokeRect\n")
}
#endif

#ifndef NO_CGContextSynchronize
JNIEXPORT void JNICALL OS_NATIVE(CGContextSynchronize)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGContextSynchronize\n")
	CGContextSynchronize((CGContextRef)arg0);
	NATIVE_EXIT(env, that, "CGContextSynchronize\n")
}
#endif

#ifndef NO_CGContextTranslateCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextTranslateCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "CGContextTranslateCTM\n")
	CGContextTranslateCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
	NATIVE_EXIT(env, that, "CGContextTranslateCTM\n")
}
#endif

#ifndef NO_CGDataProviderCreateWithData
JNIEXPORT jint JNICALL OS_NATIVE(CGDataProviderCreateWithData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGDataProviderCreateWithData\n")
	rc = (jint)CGDataProviderCreateWithData((void *)arg0, (const void *)arg1, (size_t)arg2, (void *)arg3);
	NATIVE_EXIT(env, that, "CGDataProviderCreateWithData\n")
	return rc;
}
#endif

#ifndef NO_CGDataProviderRelease
JNIEXPORT void JNICALL OS_NATIVE(CGDataProviderRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGDataProviderRelease\n")
	CGDataProviderRelease((CGDataProviderRef)arg0);
	NATIVE_EXIT(env, that, "CGDataProviderRelease\n")
}
#endif

#ifndef NO_CGFontCreateWithPlatformFont
JNIEXPORT jint JNICALL OS_NATIVE(CGFontCreateWithPlatformFont)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CGFontCreateWithPlatformFont\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)CGFontCreateWithPlatformFont(lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "CGFontCreateWithPlatformFont\n")
	return rc;
}
#endif

#ifndef NO_CGFontRelease
JNIEXPORT void JNICALL OS_NATIVE(CGFontRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGFontRelease\n")
	CGFontRelease((CGFontRef)arg0);
	NATIVE_EXIT(env, that, "CGFontRelease\n")
}
#endif

#ifndef NO_CGImageCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGImageCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jfloatArray arg8, jboolean arg9, jint arg10)
{
	jfloat *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CGImageCreate\n")
	if (arg8) lparg8 = (*env)->GetFloatArrayElements(env, arg8, NULL);
	rc = (jint)CGImageCreate((size_t)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6, (CGDataProviderRef)arg7, (const float *)lparg8, (Boolean)arg9, (CGColorRenderingIntent)arg10);
	if (arg8) (*env)->ReleaseFloatArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "CGImageCreate\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetAlphaInfo
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetAlphaInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetAlphaInfo\n")
	rc = (jint)CGImageGetAlphaInfo((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetAlphaInfo\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetBitsPerComponent
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBitsPerComponent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetBitsPerComponent\n")
	rc = (jint)CGImageGetBitsPerComponent((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetBitsPerComponent\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetBitsPerPixel
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBitsPerPixel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetBitsPerPixel\n")
	rc = (jint)CGImageGetBitsPerPixel((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetBitsPerPixel\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetBytesPerRow
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBytesPerRow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetBytesPerRow\n")
	rc = (jint)CGImageGetBytesPerRow((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetBytesPerRow\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetColorSpace
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetColorSpace)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetColorSpace\n")
	rc = (jint)CGImageGetColorSpace((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetColorSpace\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetHeight
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetHeight\n")
	rc = (jint)CGImageGetHeight((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetHeight\n")
	return rc;
}
#endif

#ifndef NO_CGImageGetWidth
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "CGImageGetWidth\n")
	rc = (jint)CGImageGetWidth((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageGetWidth\n")
	return rc;
}
#endif

#ifndef NO_CGImageRelease
JNIEXPORT void JNICALL OS_NATIVE(CGImageRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CGImageRelease\n")
	CGImageRelease((CGImageRef)arg0);
	NATIVE_EXIT(env, that, "CGImageRelease\n")
}
#endif

#ifndef NO_CGWarpMouseCursorPosition
JNIEXPORT jint JNICALL OS_NATIVE(CGWarpMouseCursorPosition)
	(JNIEnv *env, jclass that, jobject arg0)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CGWarpMouseCursorPosition\n")
	if (arg0) lparg0 = getCGPointFields(env, arg0, &_arg0);
	rc = (jint)CGWarpMouseCursorPosition(*lparg0);
	if (arg0) setCGPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "CGWarpMouseCursorPosition\n")
	return rc;
}
#endif

#ifndef NO_CallNextEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(CallNextEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CallNextEventHandler\n")
	rc = (jint)CallNextEventHandler((EventHandlerCallRef)arg0, (EventRef)arg1);
	NATIVE_EXIT(env, that, "CallNextEventHandler\n")
	return rc;
}
#endif

#ifndef NO_CharWidth
JNIEXPORT jshort JNICALL OS_NATIVE(CharWidth)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CharWidth\n")
	rc = (jshort)CharWidth((CharParameter)arg0);
	NATIVE_EXIT(env, that, "CharWidth\n")
	return rc;
}
#endif

#ifndef NO_ClearCurrentScrap
JNIEXPORT jint JNICALL OS_NATIVE(ClearCurrentScrap)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "ClearCurrentScrap\n")
	rc = (jint)ClearCurrentScrap();
	NATIVE_EXIT(env, that, "ClearCurrentScrap\n")
	return rc;
}
#endif

#ifndef NO_ClearKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(ClearKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ClearKeyboardFocus\n")
	rc = (jint)ClearKeyboardFocus((WindowRef)arg0);
	NATIVE_EXIT(env, that, "ClearKeyboardFocus\n")
	return rc;
}
#endif

#ifndef NO_ClearMenuBar
JNIEXPORT void JNICALL OS_NATIVE(ClearMenuBar)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "ClearMenuBar\n")
	ClearMenuBar();
	NATIVE_EXIT(env, that, "ClearMenuBar\n")
}
#endif

#ifndef NO_ClipCGContextToRegion
JNIEXPORT jint JNICALL OS_NATIVE(ClipCGContextToRegion)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ClipCGContextToRegion\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)ClipCGContextToRegion((CGContextRef)arg0, (const Rect *)lparg1, (RgnHandle)arg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ClipCGContextToRegion\n")
	return rc;
}
#endif

#ifndef NO_CloseDataBrowserContainer
JNIEXPORT jint JNICALL OS_NATIVE(CloseDataBrowserContainer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CloseDataBrowserContainer\n")
	rc = (jint)CloseDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
	NATIVE_EXIT(env, that, "CloseDataBrowserContainer\n")
	return rc;
}
#endif

#ifndef NO_ClosePoly
JNIEXPORT void JNICALL OS_NATIVE(ClosePoly)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "ClosePoly\n")
	ClosePoly();
	NATIVE_EXIT(env, that, "ClosePoly\n")
}
#endif

#ifndef NO_CloseRgn
JNIEXPORT void JNICALL OS_NATIVE(CloseRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "CloseRgn\n")
	CloseRgn((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "CloseRgn\n")
}
#endif

#ifndef NO_CollapseWindow
JNIEXPORT jint JNICALL OS_NATIVE(CollapseWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "CollapseWindow\n")
	rc = (jint)CollapseWindow((WindowRef)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "CollapseWindow\n")
	return rc;
}
#endif

#ifndef NO_ConvertEventRefToEventRecord
JNIEXPORT jboolean JNICALL OS_NATIVE(ConvertEventRefToEventRecord)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	EventRecord _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "ConvertEventRefToEventRecord\n")
	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	rc = (jboolean)ConvertEventRefToEventRecord((EventRef)arg0, (EventRecord *)lparg1);
	if (arg1) setEventRecordFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "ConvertEventRefToEventRecord\n")
	return rc;
}
#endif

#ifndef NO_ConvertFromPStringToUnicode
JNIEXPORT jint JNICALL OS_NATIVE(ConvertFromPStringToUnicode)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jcharArray arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ConvertFromPStringToUnicode\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)ConvertFromPStringToUnicode((TextToUnicodeInfo)arg0, (ConstStr255Param)lparg1, arg2, lparg3, lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "ConvertFromPStringToUnicode\n")
	return rc;
}
#endif

#ifndef NO_CopyBits
JNIEXPORT void JNICALL OS_NATIVE(CopyBits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jshort arg4, jint arg5)
{
	Rect _arg2, *lparg2=NULL;
	Rect _arg3, *lparg3=NULL;
	NATIVE_ENTER(env, that, "CopyBits\n")
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getRectFields(env, arg3, &_arg3);
	CopyBits((const BitMap *)arg0, (const BitMap *)arg1, (const Rect *)lparg2, (const Rect *)lparg3, (short)arg4, (RgnHandle)arg5);
	if (arg3) setRectFields(env, arg3, lparg3);
	if (arg2) setRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "CopyBits\n")
}
#endif

#ifndef NO_CopyControlTitleAsCFString
JNIEXPORT jint JNICALL OS_NATIVE(CopyControlTitleAsCFString)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CopyControlTitleAsCFString\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CopyControlTitleAsCFString((ControlRef)arg0, (CFStringRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CopyControlTitleAsCFString\n")
	return rc;
}
#endif

#ifndef NO_CopyDeepMask
JNIEXPORT void JNICALL OS_NATIVE(CopyDeepMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jobject arg5, jshort arg6, jint arg7)
{
	Rect _arg3, *lparg3=NULL;
	Rect _arg4, *lparg4=NULL;
	Rect _arg5, *lparg5=NULL;
	NATIVE_ENTER(env, that, "CopyDeepMask\n")
	if (arg3) lparg3 = getRectFields(env, arg3, &_arg3);
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	if (arg5) lparg5 = getRectFields(env, arg5, &_arg5);
	CopyDeepMask((const BitMap *)arg0, (const BitMap *)arg1, (const BitMap *)arg2, (const Rect *)lparg3, (const Rect *)lparg4, (const Rect *)lparg5, (short)arg6, (RgnHandle)arg7);
	if (arg5) setRectFields(env, arg5, lparg5);
	if (arg4) setRectFields(env, arg4, lparg4);
	if (arg3) setRectFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "CopyDeepMask\n")
}
#endif

#ifndef NO_CopyMenuItemTextAsCFString
JNIEXPORT jint JNICALL OS_NATIVE(CopyMenuItemTextAsCFString)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CopyMenuItemTextAsCFString\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CopyMenuItemTextAsCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "CopyMenuItemTextAsCFString\n")
	return rc;
}
#endif

#ifndef NO_CopyRgn
JNIEXPORT void JNICALL OS_NATIVE(CopyRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "CopyRgn\n")
	CopyRgn((RgnHandle)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "CopyRgn\n")
}
#endif

#ifndef NO_CountDragItemFlavors
JNIEXPORT jint JNICALL OS_NATIVE(CountDragItemFlavors)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CountDragItemFlavors\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)CountDragItemFlavors((DragRef)arg0, (DragItemRef)arg1, (UInt16 *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "CountDragItemFlavors\n")
	return rc;
}
#endif

#ifndef NO_CountDragItems
JNIEXPORT jint JNICALL OS_NATIVE(CountDragItems)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CountDragItems\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)CountDragItems((DragRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CountDragItems\n")
	return rc;
}
#endif

#ifndef NO_CountMenuItems
JNIEXPORT jshort JNICALL OS_NATIVE(CountMenuItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "CountMenuItems\n")
	rc = (jshort)CountMenuItems((MenuRef)arg0);
	NATIVE_EXIT(env, that, "CountMenuItems\n")
	return rc;
}
#endif

#ifndef NO_CountSubControls
JNIEXPORT jint JNICALL OS_NATIVE(CountSubControls)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CountSubControls\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)CountSubControls((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CountSubControls\n")
	return rc;
}
#endif

#ifndef NO_CreateBevelButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateBevelButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jshort arg4, jint arg5, jshort arg6, jshort arg7, jshort arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateBevelButtonControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	rc = (jint)CreateBevelButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlBevelThickness)arg3, (ControlBevelButtonBehavior)arg4, (ControlButtonContentInfoPtr)arg5, (SInt16)arg6, (ControlBevelButtonMenuBehavior)arg7, (ControlBevelButtonMenuPlacement)arg8, (ControlRef *)lparg9);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateBevelButtonControl\n")
	return rc;
}
#endif

#ifndef NO_CreateCGContextForPort
JNIEXPORT jint JNICALL OS_NATIVE(CreateCGContextForPort)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateCGContextForPort\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateCGContextForPort((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateCGContextForPort\n")
	return rc;
}
#endif

#ifndef NO_CreateCheckBoxControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateCheckBoxControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateCheckBoxControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateCheckBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateCheckBoxControl\n")
	return rc;
}
#endif

#ifndef NO_CreateDataBrowserControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateDataBrowserControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateDataBrowserControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateDataBrowserControl((WindowRef)arg0, (const Rect *)lparg1, (DataBrowserViewStyle)arg2, (ControlRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateDataBrowserControl\n")
	return rc;
}
#endif

#ifndef NO_CreateEditUnicodeTextControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateEditUnicodeTextControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3, jobject arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	ControlFontStyleRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateEditUnicodeTextControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = getControlFontStyleRecFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateEditUnicodeTextControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, arg3, lparg4, (ControlRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) setControlFontStyleRecFields(env, arg4, lparg4);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateEditUnicodeTextControl\n")
	return rc;
}
#endif

#ifndef NO_CreateEvent
JNIEXPORT jint JNICALL OS_NATIVE(CreateEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateEvent\n")
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateEvent((CFAllocatorRef)arg0, (UInt32)arg1, (UInt32)arg2, (EventTime)arg3, (EventAttributes)arg4, (EventRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "CreateEvent\n")
	return rc;
}
#endif

#ifndef NO_CreateGroupBoxControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateGroupBoxControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateGroupBoxControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateGroupBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (Boolean)arg3, (ControlRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateGroupBoxControl\n")
	return rc;
}
#endif

#ifndef NO_CreateIconControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateIconControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jboolean arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	ControlButtonContentInfo _arg2, *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateIconControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getControlButtonContentInfoFields(env, arg2, &_arg2);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateIconControl((WindowRef)arg0, lparg1, lparg2, arg3, (ControlRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2) setControlButtonContentInfoFields(env, arg2, lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateIconControl\n")
	return rc;
}
#endif

#ifndef NO_CreateNewMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreateNewMenu)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateNewMenu\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CreateNewMenu((MenuID)arg0, (MenuAttributes)arg1, (MenuRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "CreateNewMenu\n")
	return rc;
}
#endif

#ifndef NO_CreateNewWindow
JNIEXPORT jint JNICALL OS_NATIVE(CreateNewWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	Rect _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateNewWindow\n")
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateNewWindow((WindowClass)arg0, (WindowAttributes)arg1, (const Rect *)lparg2, (WindowRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) setRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "CreateNewWindow\n")
	return rc;
}
#endif

#ifndef NO_CreatePopupArrowControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupArrowControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePopupArrowControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreatePopupArrowControl((WindowRef)arg0, (const Rect *)lparg1, (ControlPopupArrowOrientation)arg2, (ControlPopupArrowSize)arg3, (ControlRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreatePopupArrowControl\n")
	return rc;
}
#endif

#ifndef NO_CreatePopupButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jboolean arg4, jshort arg5, jshort arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePopupButtonControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)CreatePopupButtonControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, arg3, arg4, arg5, arg6, arg7, (ControlRef *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreatePopupButtonControl\n")
	return rc;
}
#endif

#ifndef NO_CreateProgressBarControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateProgressBarControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateProgressBarControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)CreateProgressBarControl((WindowRef)arg0, lparg1, arg2, arg3, arg4, arg5, (ControlRef *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateProgressBarControl\n")
	return rc;
}
#endif

#ifndef NO_CreatePushButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePushButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePushButtonControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreatePushButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreatePushButtonControl\n")
	return rc;
}
#endif

#ifndef NO_CreatePushButtonWithIconControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePushButtonWithIconControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jobject arg3, jshort arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	ControlButtonContentInfo _arg3, *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreatePushButtonWithIconControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = getControlButtonContentInfoFields(env, arg3, &_arg3);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreatePushButtonWithIconControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, (ControlButtonContentInfo *)lparg3, (ControlPushButtonIconAlignment)arg4, (ControlRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) setControlButtonContentInfoFields(env, arg3, lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreatePushButtonWithIconControl\n")
	return rc;
}
#endif

#ifndef NO_CreateRadioButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateRadioButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateRadioButtonControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)CreateRadioButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateRadioButtonControl\n")
	return rc;
}
#endif

#ifndef NO_CreateRootControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateRootControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateRootControl\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateRootControl((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateRootControl\n")
	return rc;
}
#endif

#ifndef NO_CreateScrollBarControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateScrollBarControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateScrollBarControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)CreateScrollBarControl((WindowRef)arg0, lparg1, arg2, arg3, arg4, arg5, arg6, (ControlActionUPP)arg7, (ControlRef *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateScrollBarControl\n")
	return rc;
}
#endif

#ifndef NO_CreateSeparatorControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateSeparatorControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateSeparatorControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)CreateSeparatorControl((WindowRef)arg0, lparg1, (ControlRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateSeparatorControl\n")
	return rc;
}
#endif

#ifndef NO_CreateSliderControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateSliderControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jshort arg6, jboolean arg7, jint arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateSliderControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	rc = (jint)CreateSliderControl((WindowRef)arg0, (const Rect *)lparg1, (SInt32)arg2, (SInt32)arg3, (SInt32)arg4, (ControlSliderOrientation)arg5, (UInt16)arg6, (Boolean)arg7, (ControlActionUPP)arg8, (ControlRef *)lparg9);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateSliderControl\n")
	return rc;
}
#endif

#ifndef NO_CreateStandardAlert
JNIEXPORT jint JNICALL OS_NATIVE(CreateStandardAlert)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	AlertStdCFStringAlertParamRec _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateStandardAlert\n")
	if (arg3) lparg3 = getAlertStdCFStringAlertParamRecFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateStandardAlert((AlertType)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (const AlertStdCFStringAlertParamRec *)lparg3, (DialogRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) setAlertStdCFStringAlertParamRecFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "CreateStandardAlert\n")
	return rc;
}
#endif

#ifndef NO_CreateStaticTextControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateStaticTextControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jobject arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	ControlFontStyleRec _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateStaticTextControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = getControlFontStyleRecFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)CreateStaticTextControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, (const ControlFontStyleRec *)lparg3, (ControlRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) setControlFontStyleRecFields(env, arg3, lparg3);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateStaticTextControl\n")
	return rc;
}
#endif

#ifndef NO_CreateTabsControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateTabsControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jshort arg4, jint arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateTabsControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)CreateTabsControl((WindowRef)arg0, (const Rect *)lparg1, (ControlTabSize)arg2, (ControlTabDirection)arg3, (UInt16)arg4, (const ControlTabEntry *)arg5, (ControlRef *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateTabsControl\n")
	return rc;
}
#endif

#ifndef NO_CreateTextToUnicodeInfoByEncoding
JNIEXPORT jint JNICALL OS_NATIVE(CreateTextToUnicodeInfoByEncoding)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateTextToUnicodeInfoByEncoding\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateTextToUnicodeInfoByEncoding((TextEncoding)arg0, (TextToUnicodeInfo *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateTextToUnicodeInfoByEncoding\n")
	return rc;
}
#endif

#ifndef NO_CreateUserPaneControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateUserPaneControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateUserPaneControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)CreateUserPaneControl((WindowRef)arg0, lparg1, arg2, (ControlRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "CreateUserPaneControl\n")
	return rc;
}
#endif

#ifndef NO_CreateWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowGroup)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "CreateWindowGroup\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)CreateWindowGroup((WindowGroupAttributes)arg0, (WindowGroupRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "CreateWindowGroup\n")
	return rc;
}
#endif

#ifndef NO_DMGetFirstScreenDevice
JNIEXPORT jint JNICALL OS_NATIVE(DMGetFirstScreenDevice)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "DMGetFirstScreenDevice\n")
	rc = (jint)DMGetFirstScreenDevice((Boolean)arg0);
	NATIVE_EXIT(env, that, "DMGetFirstScreenDevice\n")
	return rc;
}
#endif

#ifndef NO_DMGetNextScreenDevice
JNIEXPORT jint JNICALL OS_NATIVE(DMGetNextScreenDevice)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "DMGetNextScreenDevice\n")
	rc = (jint)DMGetNextScreenDevice((GDHandle)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "DMGetNextScreenDevice\n")
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT void JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "DeleteMenu\n")
	DeleteMenu((MenuID)arg0);
	NATIVE_EXIT(env, that, "DeleteMenu\n")
}
#endif

#ifndef NO_DeleteMenuItem
JNIEXPORT void JNICALL OS_NATIVE(DeleteMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "DeleteMenuItem\n")
	DeleteMenuItem((MenuRef)arg0, (short)arg1);
	NATIVE_EXIT(env, that, "DeleteMenuItem\n")
}
#endif

#ifndef NO_DeleteMenuItems
JNIEXPORT jint JNICALL OS_NATIVE(DeleteMenuItems)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "DeleteMenuItems\n")
	rc = (jint)DeleteMenuItems((MenuRef)arg0, (MenuItemIndex)arg1, (ItemCount)arg2);
	NATIVE_EXIT(env, that, "DeleteMenuItems\n")
	return rc;
}
#endif

#ifndef NO_DiffRgn
JNIEXPORT void JNICALL OS_NATIVE(DiffRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "DiffRgn\n")
	DiffRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	NATIVE_EXIT(env, that, "DiffRgn\n")
}
#endif

#ifndef NO_DisableControl
JNIEXPORT jint JNICALL OS_NATIVE(DisableControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "DisableControl\n")
	rc = (jint)DisableControl((ControlRef)arg0);
	NATIVE_EXIT(env, that, "DisableControl\n")
	return rc;
}
#endif

#ifndef NO_DisableMenuCommand
JNIEXPORT void JNICALL OS_NATIVE(DisableMenuCommand)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "DisableMenuCommand\n")
	DisableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
	NATIVE_EXIT(env, that, "DisableMenuCommand\n")
}
#endif

#ifndef NO_DisableMenuItem
JNIEXPORT void JNICALL OS_NATIVE(DisableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "DisableMenuItem\n")
	DisableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
	NATIVE_EXIT(env, that, "DisableMenuItem\n")
}
#endif

#ifndef NO_DisposeControl
JNIEXPORT void JNICALL OS_NATIVE(DisposeControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeControl\n")
	DisposeControl((ControlRef)arg0);
	NATIVE_EXIT(env, that, "DisposeControl\n")
}
#endif

#ifndef NO_DisposeDrag
JNIEXPORT jint JNICALL OS_NATIVE(DisposeDrag)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "DisposeDrag\n")
	rc = (jint)DisposeDrag((DragRef)arg0);
	NATIVE_EXIT(env, that, "DisposeDrag\n")
	return rc;
}
#endif

#ifndef NO_DisposeGWorld
JNIEXPORT void JNICALL OS_NATIVE(DisposeGWorld)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeGWorld\n")
	DisposeGWorld((GWorldPtr)arg0);
	NATIVE_EXIT(env, that, "DisposeGWorld\n")
}
#endif

#ifndef NO_DisposeHandle
JNIEXPORT void JNICALL OS_NATIVE(DisposeHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeHandle\n")
	DisposeHandle((Handle)arg0);
	NATIVE_EXIT(env, that, "DisposeHandle\n")
}
#endif

#ifndef NO_DisposeMenu
JNIEXPORT void JNICALL OS_NATIVE(DisposeMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeMenu\n")
	DisposeMenu((MenuRef)arg0);
	NATIVE_EXIT(env, that, "DisposeMenu\n")
}
#endif

#ifndef NO_DisposePtr
JNIEXPORT void JNICALL OS_NATIVE(DisposePtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposePtr\n")
	DisposePtr((Ptr)arg0);
	NATIVE_EXIT(env, that, "DisposePtr\n")
}
#endif

#ifndef NO_DisposeRgn
JNIEXPORT void JNICALL OS_NATIVE(DisposeRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeRgn\n")
	DisposeRgn((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "DisposeRgn\n")
}
#endif

#ifndef NO_DisposeTextToUnicodeInfo
JNIEXPORT jint JNICALL OS_NATIVE(DisposeTextToUnicodeInfo)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DisposeTextToUnicodeInfo\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)DisposeTextToUnicodeInfo((TextToUnicodeInfo *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "DisposeTextToUnicodeInfo\n")
	return rc;
}
#endif

#ifndef NO_DisposeWindow
JNIEXPORT void JNICALL OS_NATIVE(DisposeWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DisposeWindow\n")
	DisposeWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "DisposeWindow\n")
}
#endif

#ifndef NO_DrawControlInCurrentPort
JNIEXPORT void JNICALL OS_NATIVE(DrawControlInCurrentPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "DrawControlInCurrentPort\n")
	DrawControlInCurrentPort((ControlRef)arg0);
	NATIVE_EXIT(env, that, "DrawControlInCurrentPort\n")
}
#endif

#ifndef NO_DrawMenuBar
JNIEXPORT void JNICALL OS_NATIVE(DrawMenuBar)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "DrawMenuBar\n")
	DrawMenuBar();
	NATIVE_EXIT(env, that, "DrawMenuBar\n")
}
#endif

#ifndef NO_DrawText
JNIEXPORT void JNICALL OS_NATIVE(DrawText)
	(JNIEnv *env, jclass that, jbyteArray arg0, jshort arg1, jshort arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "DrawText\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	DrawText((const void *)lparg0, (short)arg1, (short)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "DrawText\n")
}
#endif

#ifndef NO_DrawThemeButton
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeButton)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jobject arg2, jobject arg3, jint arg4, jint arg5, jint arg6)
{
	Rect _arg0, *lparg0=NULL;
	ThemeButtonDrawInfo _arg2, *lparg2=NULL;
	ThemeButtonDrawInfo _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemeButton\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getThemeButtonDrawInfoFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getThemeButtonDrawInfoFields(env, arg3, &_arg3);
	rc = (jint)DrawThemeButton((Rect *)lparg0, (ThemeButtonKind)arg1, (const ThemeButtonDrawInfo *)lparg2, (const ThemeButtonDrawInfo *)lparg3, (ThemeEraseUPP)arg4, (ThemeButtonDrawUPP)arg5, (UInt32)arg6);
	if (arg3) setThemeButtonDrawInfoFields(env, arg3, lparg3);
	if (arg2) setThemeButtonDrawInfoFields(env, arg2, lparg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DrawThemeButton\n")
	return rc;
}
#endif

#ifndef NO_DrawThemeEditTextFrame
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeEditTextFrame)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemeEditTextFrame\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeEditTextFrame((const Rect *)lparg0, (ThemeDrawState)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DrawThemeEditTextFrame\n")
	return rc;
}
#endif

#ifndef NO_DrawThemeFocusRect
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeFocusRect)
	(JNIEnv *env, jclass that, jobject arg0, jboolean arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemeFocusRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeFocusRect((const Rect *)lparg0, (Boolean)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DrawThemeFocusRect\n")
	return rc;
}
#endif

#ifndef NO_DrawThemePopupArrow
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemePopupArrow)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3, jint arg4, jint arg5)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemePopupArrow\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemePopupArrow(lparg0, (ThemeArrowOrientation)arg1, (ThemePopupArrowSize)arg2, (ThemeDrawState)arg3, (ThemeEraseUPP)arg4, (UInt32)arg5);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DrawThemePopupArrow\n")
	return rc;
}
#endif

#ifndef NO_DrawThemeSeparator
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeSeparator)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemeSeparator\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jint)DrawThemeSeparator((const Rect *)lparg0, (ThemeDrawState)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "DrawThemeSeparator\n")
	return rc;
}
#endif

#ifndef NO_DrawThemeTextBox
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeTextBox)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshort arg5, jint arg6)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "DrawThemeTextBox\n")
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)DrawThemeTextBox((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (const Rect *)lparg4, (SInt16)arg5, (void *)arg6);
	if (arg4) setRectFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "DrawThemeTextBox\n")
	return rc;
}
#endif

#ifndef NO_EmbedControl
JNIEXPORT jint JNICALL OS_NATIVE(EmbedControl)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "EmbedControl\n")
	rc = (jint)EmbedControl((ControlRef)arg0, (ControlRef)arg1);
	NATIVE_EXIT(env, that, "EmbedControl\n")
	return rc;
}
#endif

#ifndef NO_EmptyRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "EmptyRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jboolean)EmptyRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "EmptyRect\n")
	return rc;
}
#endif

#ifndef NO_EmptyRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "EmptyRgn\n")
	rc = (jboolean)EmptyRgn((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "EmptyRgn\n")
	return rc;
}
#endif

#ifndef NO_EnableControl
JNIEXPORT jint JNICALL OS_NATIVE(EnableControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "EnableControl\n")
	rc = (jint)EnableControl((ControlRef)arg0);
	NATIVE_EXIT(env, that, "EnableControl\n")
	return rc;
}
#endif

#ifndef NO_EnableMenuCommand
JNIEXPORT void JNICALL OS_NATIVE(EnableMenuCommand)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "EnableMenuCommand\n")
	EnableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
	NATIVE_EXIT(env, that, "EnableMenuCommand\n")
}
#endif

#ifndef NO_EnableMenuItem
JNIEXPORT void JNICALL OS_NATIVE(EnableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "EnableMenuItem\n")
	EnableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
	NATIVE_EXIT(env, that, "EnableMenuItem\n")
}
#endif

#ifndef NO_EndUpdate
JNIEXPORT void JNICALL OS_NATIVE(EndUpdate)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "EndUpdate\n")
	EndUpdate((WindowRef)arg0);
	NATIVE_EXIT(env, that, "EndUpdate\n")
}
#endif

#ifndef NO_EqualRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EqualRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "EqualRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jboolean)EqualRect(lparg0, lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "EqualRect\n")
	return rc;
}
#endif

#ifndef NO_EraseRect
JNIEXPORT void JNICALL OS_NATIVE(EraseRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "EraseRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	EraseRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "EraseRect\n")
}
#endif

#ifndef NO_EraseRgn
JNIEXPORT void JNICALL OS_NATIVE(EraseRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "EraseRgn\n")
	EraseRgn((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "EraseRgn\n")
}
#endif

#ifndef NO_FMCreateFontFamilyInstanceIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMCreateFontFamilyInstanceIterator)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "FMCreateFontFamilyInstanceIterator\n")
	rc = (jint)FMCreateFontFamilyInstanceIterator((FMFontFamily)arg0, (FMFontFamilyInstanceIterator *)arg1);
	NATIVE_EXIT(env, that, "FMCreateFontFamilyInstanceIterator\n")
	return rc;
}
#endif

#ifndef NO_FMCreateFontFamilyIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMCreateFontFamilyIterator)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "FMCreateFontFamilyIterator\n")
	rc = (jint)FMCreateFontFamilyIterator((const FMFilter *)arg0, (void *)arg1, (OptionBits)arg2, (FMFontFamilyIterator *)arg3);
	NATIVE_EXIT(env, that, "FMCreateFontFamilyIterator\n")
	return rc;
}
#endif

#ifndef NO_FMDisposeFontFamilyInstanceIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMDisposeFontFamilyInstanceIterator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "FMDisposeFontFamilyInstanceIterator\n")
	rc = (jint)FMDisposeFontFamilyInstanceIterator((FMFontFamilyInstanceIterator *)arg0);
	NATIVE_EXIT(env, that, "FMDisposeFontFamilyInstanceIterator\n")
	return rc;
}
#endif

#ifndef NO_FMDisposeFontFamilyIterator
JNIEXPORT jint JNICALL OS_NATIVE(FMDisposeFontFamilyIterator)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "FMDisposeFontFamilyIterator\n")
	rc = (jint)FMDisposeFontFamilyIterator((FMFontFamilyIterator *)arg0);
	NATIVE_EXIT(env, that, "FMDisposeFontFamilyIterator\n")
	return rc;
}
#endif

#ifndef NO_FMGetATSFontRefFromFont
JNIEXPORT jint JNICALL OS_NATIVE(FMGetATSFontRefFromFont)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "FMGetATSFontRefFromFont\n")
	rc = (jint)FMGetATSFontRefFromFont(arg0);
	NATIVE_EXIT(env, that, "FMGetATSFontRefFromFont\n")
	return rc;
}
#endif

#ifndef NO_FMGetFontFamilyFromName
JNIEXPORT jshort JNICALL OS_NATIVE(FMGetFontFamilyFromName)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jshort rc;
	NATIVE_ENTER(env, that, "FMGetFontFamilyFromName\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jshort)FMGetFontFamilyFromName((ConstStr255Param)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FMGetFontFamilyFromName\n")
	return rc;
}
#endif

#ifndef NO_FMGetFontFamilyInstanceFromFont
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFamilyInstanceFromFont)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FMGetFontFamilyInstanceFromFont\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)FMGetFontFamilyInstanceFromFont((FMFont)arg0, (FMFontFamily *)lparg1, (FMFontStyle *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FMGetFontFamilyInstanceFromFont\n")
	return rc;
}
#endif

#ifndef NO_FMGetFontFamilyName
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFamilyName)
	(JNIEnv *env, jclass that, jshort arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FMGetFontFamilyName\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FMGetFontFamilyName(arg0, lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FMGetFontFamilyName\n")
	return rc;
}
#endif

#ifndef NO_FMGetFontFromFontFamilyInstance
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFromFontFamilyInstance)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jintArray arg2, jshortArray arg3)
{
	jint *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FMGetFontFromFontFamilyInstance\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)FMGetFontFromFontFamilyInstance((FMFontFamily)arg0, (FMFontStyle)arg1, (FMFont *)lparg2, (FMFontStyle *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "FMGetFontFromFontFamilyInstance\n")
	return rc;
}
#endif

#ifndef NO_FMGetNextFontFamily
JNIEXPORT jint JNICALL OS_NATIVE(FMGetNextFontFamily)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FMGetNextFontFamily\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)FMGetNextFontFamily((FMFontFamilyIterator *)arg0, (FMFontFamily *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FMGetNextFontFamily\n")
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
	NATIVE_ENTER(env, that, "FMGetNextFontFamilyInstance\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)FMGetNextFontFamilyInstance((FMFontFamilyInstanceIterator *)arg0, (FMFont *)lparg1, (FMFontStyle *)lparg2, (FMFontSize *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FMGetNextFontFamilyInstance\n")
	return rc;
}
#endif

#ifndef NO_FPIsFontPanelVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(FPIsFontPanelVisible)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "FPIsFontPanelVisible\n")
	rc = (jboolean)FPIsFontPanelVisible();
	NATIVE_EXIT(env, that, "FPIsFontPanelVisible\n")
	return rc;
}
#endif

#ifndef NO_FPShowHideFontPanel
JNIEXPORT jint JNICALL OS_NATIVE(FPShowHideFontPanel)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "FPShowHideFontPanel\n")
	rc = (jint)FPShowHideFontPanel();
	NATIVE_EXIT(env, that, "FPShowHideFontPanel\n")
	return rc;
}
#endif

#ifndef NO_FSGetCatalogInfo
JNIEXPORT jint JNICALL OS_NATIVE(FSGetCatalogInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5)
{
	jbyte *lparg0=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FSGetCatalogInfo\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	rc = (jint)FSGetCatalogInfo((FSRef *)lparg0, (FSCatalogInfoBitmap)arg1, (FSCatalogInfo *)lparg2, (HFSUniStr255 *)lparg3, (FSSpec *)lparg4, (FSRef *)lparg5);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FSGetCatalogInfo\n")
	return rc;
}
#endif

#ifndef NO_FSpGetFInfo
JNIEXPORT jint JNICALL OS_NATIVE(FSpGetFInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FSpGetFInfo\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FSpGetFInfo((FSSpec *)lparg0, (FInfo *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FSpGetFInfo\n")
	return rc;
}
#endif

#ifndef NO_FSpMakeFSRef
JNIEXPORT jint JNICALL OS_NATIVE(FSpMakeFSRef)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FSpMakeFSRef\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)FSpMakeFSRef((const FSSpec *)lparg0, (FSRef *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FSpMakeFSRef\n")
	return rc;
}
#endif

#ifndef NO_FetchFontInfo
JNIEXPORT jint JNICALL OS_NATIVE(FetchFontInfo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jobject arg3)
{
	FontInfo _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "FetchFontInfo\n")
	if (arg3) lparg3 = getFontInfoFields(env, arg3, &_arg3);
	rc = (jint)FetchFontInfo(arg0, arg1, arg2, lparg3);
	if (arg3) setFontInfoFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "FetchFontInfo\n")
	return rc;
}
#endif

#ifndef NO_FindWindow
JNIEXPORT jshort JNICALL OS_NATIVE(FindWindow)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	Point _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jshort rc;
	NATIVE_ENTER(env, that, "FindWindow\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jshort)FindWindow(*(Point *)lparg0, (WindowRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "FindWindow\n")
	return rc;
}
#endif

#ifndef NO_Fix2Long
JNIEXPORT jint JNICALL OS_NATIVE(Fix2Long)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "Fix2Long\n")
	rc = (jint)Fix2Long(arg0);
	NATIVE_EXIT(env, that, "Fix2Long\n")
	return rc;
}
#endif

#ifndef NO_FrameOval
JNIEXPORT void JNICALL OS_NATIVE(FrameOval)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "FrameOval\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameOval((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "FrameOval\n")
}
#endif

#ifndef NO_FramePoly
JNIEXPORT void JNICALL OS_NATIVE(FramePoly)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "FramePoly\n")
	FramePoly((PolyHandle)arg0);
	NATIVE_EXIT(env, that, "FramePoly\n")
}
#endif

#ifndef NO_FrameRect
JNIEXPORT void JNICALL OS_NATIVE(FrameRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "FrameRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "FrameRect\n")
}
#endif

#ifndef NO_FrameRoundRect
JNIEXPORT void JNICALL OS_NATIVE(FrameRoundRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "FrameRoundRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	FrameRoundRect((const Rect *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "FrameRoundRect\n")
}
#endif

#ifndef NO_FrontWindow
JNIEXPORT jint JNICALL OS_NATIVE(FrontWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "FrontWindow\n")
	rc = (jint)FrontWindow();
	NATIVE_EXIT(env, that, "FrontWindow\n")
	return rc;
}
#endif

#ifndef NO_GetAppFont
JNIEXPORT jshort JNICALL OS_NATIVE(GetAppFont)
	(JNIEnv *env, jclass that)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetAppFont\n")
	rc = (jshort)GetAppFont();
	NATIVE_EXIT(env, that, "GetAppFont\n")
	return rc;
}
#endif

#ifndef NO_GetApplicationEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetApplicationEventTarget)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetApplicationEventTarget\n")
	rc = (jint)GetApplicationEventTarget();
	NATIVE_EXIT(env, that, "GetApplicationEventTarget\n")
	return rc;
}
#endif

#ifndef NO_GetAvailableWindowAttributes
JNIEXPORT jint JNICALL OS_NATIVE(GetAvailableWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetAvailableWindowAttributes\n")
	rc = (jint)GetAvailableWindowAttributes((WindowClass)arg0);
	NATIVE_EXIT(env, that, "GetAvailableWindowAttributes\n")
	return rc;
}
#endif

#ifndef NO_GetAvailableWindowPositioningBounds
JNIEXPORT jint JNICALL OS_NATIVE(GetAvailableWindowPositioningBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetAvailableWindowPositioningBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)GetAvailableWindowPositioningBounds((GDHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetAvailableWindowPositioningBounds\n")
	return rc;
}
#endif

#ifndef NO_GetBestControlRect
JNIEXPORT jint JNICALL OS_NATIVE(GetBestControlRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshortArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetBestControlRect\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetBestControlRect((ControlRef)arg0, (Rect *)lparg1, (SInt16 *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetBestControlRect\n")
	return rc;
}
#endif

#ifndef NO_GetCaretTime
JNIEXPORT jint JNICALL OS_NATIVE(GetCaretTime)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCaretTime\n")
	rc = (jint)GetCaretTime();
	NATIVE_EXIT(env, that, "GetCaretTime\n")
	return rc;
}
#endif

#ifndef NO_GetClip
JNIEXPORT void JNICALL OS_NATIVE(GetClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "GetClip\n")
	GetClip((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "GetClip\n")
}
#endif

#ifndef NO_GetControl32BitMaximum
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitMaximum)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControl32BitMaximum\n")
	rc = (jint)GetControl32BitMaximum((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControl32BitMaximum\n")
	return rc;
}
#endif

#ifndef NO_GetControl32BitMinimum
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitMinimum)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControl32BitMinimum\n")
	rc = (jint)GetControl32BitMinimum((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControl32BitMinimum\n")
	return rc;
}
#endif

#ifndef NO_GetControl32BitValue
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControl32BitValue\n")
	rc = (jint)GetControl32BitValue((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControl32BitValue\n")
	return rc;
}
#endif

#ifndef NO_GetControlBounds
JNIEXPORT void JNICALL OS_NATIVE(GetControlBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetControlBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetControlBounds((ControlRef)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetControlBounds\n")
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	ControlFontStyleRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I\n")
	if (arg4) lparg4 = getControlFontStyleRecFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) setControlFontStyleRecFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I\n")
	return rc;
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	Rect _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I\n")
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) setRectFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I\n")
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3B_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlData__ISII_3B_3I\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "GetControlData__ISII_3B_3I\n")
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlData__ISII_3I_3I\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "GetControlData__ISII_3I_3I\n")
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3S_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3S_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jshortArray arg4, jintArray arg5)
{
	jshort *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlData__ISII_3S_3I\n")
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "GetControlData__ISII_3S_3I\n")
	return rc;
}
#endif

#ifndef NO_GetControlEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetControlEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControlEventTarget\n")
	rc = (jint)GetControlEventTarget((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControlEventTarget\n")
	return rc;
}
#endif

#ifndef NO_GetControlFeatures
JNIEXPORT jint JNICALL OS_NATIVE(GetControlFeatures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlFeatures\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetControlFeatures((ControlRef)arg0, lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetControlFeatures\n")
	return rc;
}
#endif

#ifndef NO_GetControlOwner
JNIEXPORT jint JNICALL OS_NATIVE(GetControlOwner)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControlOwner\n")
	rc = (jint)GetControlOwner((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControlOwner\n")
	return rc;
}
#endif

#ifndef NO_GetControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(GetControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetControlProperty\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)GetControlProperty((ControlRef)arg0, arg1, arg2, arg3, lparg4, lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "GetControlProperty\n")
	return rc;
}
#endif

#ifndef NO_GetControlReference
JNIEXPORT jint JNICALL OS_NATIVE(GetControlReference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControlReference\n")
	rc = (jint)GetControlReference((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControlReference\n")
	return rc;
}
#endif

#ifndef NO_GetControlRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetControlRegion)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControlRegion\n")
	rc = (jint)GetControlRegion((ControlRef)arg0, (ControlPartCode)arg1, (RgnHandle)arg2);
	NATIVE_EXIT(env, that, "GetControlRegion\n")
	return rc;
}
#endif

#ifndef NO_GetControlValue
JNIEXPORT jshort JNICALL OS_NATIVE(GetControlValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetControlValue\n")
	rc = (jshort)GetControlValue((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControlValue\n")
	return rc;
}
#endif

#ifndef NO_GetControlViewSize
JNIEXPORT jint JNICALL OS_NATIVE(GetControlViewSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetControlViewSize\n")
	rc = (jint)GetControlViewSize((ControlRef)arg0);
	NATIVE_EXIT(env, that, "GetControlViewSize\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentEventButtonState
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventButtonState)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentEventButtonState\n")
	rc = (jint)GetCurrentEventButtonState();
	NATIVE_EXIT(env, that, "GetCurrentEventButtonState\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentEventKeyModifiers
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventKeyModifiers)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentEventKeyModifiers\n")
	rc = (jint)GetCurrentEventKeyModifiers();
	NATIVE_EXIT(env, that, "GetCurrentEventKeyModifiers\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentEventLoop
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventLoop)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentEventLoop\n")
	rc = (jint)GetCurrentEventLoop();
	NATIVE_EXIT(env, that, "GetCurrentEventLoop\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentEventQueue
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventQueue)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentEventQueue\n")
	rc = (jint)GetCurrentEventQueue();
	NATIVE_EXIT(env, that, "GetCurrentEventQueue\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentProcess
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentProcess)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentProcess\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetCurrentProcess((ProcessSerialNumber *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetCurrentProcess\n")
	return rc;
}
#endif

#ifndef NO_GetCurrentScrap
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentScrap)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetCurrentScrap\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetCurrentScrap((ScrapRef *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetCurrentScrap\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserCallbacks\n")
	if (arg1) lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1);
	rc = (jint)GetDataBrowserCallbacks((ControlRef)arg0, (DataBrowserCallbacks *)lparg1);
	if (arg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetDataBrowserCallbacks\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemCount
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemCount)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserItemCount\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)GetDataBrowserItemCount((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (UInt32 *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserItemCount\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemDataButtonValue)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserItemDataButtonValue\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserItemDataButtonValue((ControlRef)arg0, lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserItemDataButtonValue\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemPartBounds
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemPartBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserItemPartBounds\n")
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)GetDataBrowserItemPartBounds((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserPropertyPart)arg3, (Rect *)lparg4);
	if (arg4) setRectFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetDataBrowserItemPartBounds\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemState
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemState)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserItemState\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserItemState((ControlRef)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserItemState\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserItems\n")
	rc = (jint)GetDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (Handle)arg4);
	NATIVE_EXIT(env, that, "GetDataBrowserItems\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserListViewHeaderBtnHeight)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserListViewHeaderBtnHeight\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserListViewHeaderBtnHeight\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserListViewHeaderDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserListViewHeaderDesc\n")
	if (arg2) lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2);
	rc = (jint)GetDataBrowserListViewHeaderDesc((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserListViewHeaderDesc *)lparg2);
	if (arg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetDataBrowserListViewHeaderDesc\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserScrollBarInset
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserScrollBarInset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserScrollBarInset\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)GetDataBrowserScrollBarInset((ControlRef)arg0, lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetDataBrowserScrollBarInset\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserScrollPosition
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserScrollPosition)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserScrollPosition\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserScrollPosition((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserScrollPosition\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserSelectionAnchor
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserSelectionAnchor)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserSelectionAnchor\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserSelectionAnchor((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserSelectionAnchor\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserSelectionFlags
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserSelectionFlags)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserSelectionFlags\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserSelectionFlags((ControlRef)arg0, lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserSelectionFlags\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewColumnPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserTableViewColumnPosition\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewColumnPosition((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserTableViewColumnIndex *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserTableViewColumnPosition\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewItemID
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewItemID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserTableViewItemID\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewItemID((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserTableViewItemID\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewItemRow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserTableViewItemRow\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewItemRow((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserTableViewItemRow\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewNamedColumnWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserTableViewNamedColumnWidth\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16 *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserTableViewNamedColumnWidth\n")
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewRowHeight
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewRowHeight)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDataBrowserTableViewRowHeight\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	rc = (jint)GetDataBrowserTableViewRowHeight((ControlRef)arg0, (UInt16 *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDataBrowserTableViewRowHeight\n")
	return rc;
}
#endif

#ifndef NO_GetDblTime
JNIEXPORT jint JNICALL OS_NATIVE(GetDblTime)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDblTime\n")
	rc = (jint)GetDblTime();
	NATIVE_EXIT(env, that, "GetDblTime\n")
	return rc;
}
#endif

#ifndef NO_GetDefFontSize
JNIEXPORT jshort JNICALL OS_NATIVE(GetDefFontSize)
	(JNIEnv *env, jclass that)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetDefFontSize\n")
	rc = (jshort)GetDefFontSize();
	NATIVE_EXIT(env, that, "GetDefFontSize\n")
	return rc;
}
#endif

#ifndef NO_GetDeviceList
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceList)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetDeviceList\n")
	rc = (jint)GetDeviceList();
	NATIVE_EXIT(env, that, "GetDeviceList\n")
	return rc;
}
#endif

#ifndef NO_GetDragAllowableActions
JNIEXPORT jint JNICALL OS_NATIVE(GetDragAllowableActions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDragAllowableActions\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetDragAllowableActions((DragRef)arg0, (DragActions *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDragAllowableActions\n")
	return rc;
}
#endif

#ifndef NO_GetDragDropAction
JNIEXPORT jint JNICALL OS_NATIVE(GetDragDropAction)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDragDropAction\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetDragDropAction((DragRef)arg0, (DragActions *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDragDropAction\n")
	return rc;
}
#endif

#ifndef NO_GetDragItemReferenceNumber
JNIEXPORT jint JNICALL OS_NATIVE(GetDragItemReferenceNumber)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDragItemReferenceNumber\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetDragItemReferenceNumber((DragRef)arg0, arg1, (DragItemRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetDragItemReferenceNumber\n")
	return rc;
}
#endif

#ifndef NO_GetDragModifiers
JNIEXPORT jint JNICALL OS_NATIVE(GetDragModifiers)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDragModifiers\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)GetDragModifiers((DragRef)arg0, (SInt16 *)lparg1, (SInt16 *)lparg2, (SInt16 *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetDragModifiers\n")
	return rc;
}
#endif

#ifndef NO_GetDragMouse
JNIEXPORT jint JNICALL OS_NATIVE(GetDragMouse)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	Point _arg1, *lparg1=NULL;
	Point _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetDragMouse\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getPointFields(env, arg2, &_arg2);
	rc = (jint)GetDragMouse((DragRef)arg0, (Point *)lparg1, (Point *)lparg2);
	if (arg2) setPointFields(env, arg2, lparg2);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetDragMouse\n")
	return rc;
}
#endif

#ifndef NO_GetEventClass
JNIEXPORT jint JNICALL OS_NATIVE(GetEventClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetEventClass\n")
	rc = (jint)GetEventClass((EventRef)arg0);
	NATIVE_EXIT(env, that, "GetEventClass\n")
	return rc;
}
#endif

#ifndef NO_GetEventDispatcherTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetEventDispatcherTarget)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetEventDispatcherTarget\n")
	rc = (jint)GetEventDispatcherTarget();
	NATIVE_EXIT(env, that, "GetEventDispatcherTarget\n")
	return rc;
}
#endif

#ifndef NO_GetEventKind
JNIEXPORT jint JNICALL OS_NATIVE(GetEventKind)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetEventKind\n")
	rc = (jint)GetEventKind((EventRef)arg0);
	NATIVE_EXIT(env, that, "GetEventKind\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	CGPoint _arg6, *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getCGPointFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) setCGPointFields(env, arg6, lparg6);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	HICommand _arg6, *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getHICommandFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) setHICommandFields(env, arg6, lparg6);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	Point _arg6, *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getPointFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) setPointFields(env, arg6, lparg6);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	RGBColor _arg6, *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getRGBColorFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) setRGBColorFields(env, arg6, lparg6);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2\n")
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
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = getRectFields(env, arg6, &_arg6);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) setRectFields(env, arg6, lparg6);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3I_3B
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3I_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jbyteArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3I_3B\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3I_3B\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3I_3C
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3I_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jcharArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jchar *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3I_3C\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetCharArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) (*env)->ReleaseCharArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3I_3C\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3I_3I\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3I_3I\n")
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3I_3S
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3I_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jshortArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jshort *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetEventParameter__III_3II_3I_3S\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetShortArrayElements(env, arg6, NULL);
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
	if (arg6) (*env)->ReleaseShortArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetEventParameter__III_3II_3I_3S\n")
	return rc;
}
#endif

#ifndef NO_GetEventTime
JNIEXPORT jdouble JNICALL OS_NATIVE(GetEventTime)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc;
	NATIVE_ENTER(env, that, "GetEventTime\n")
	rc = (jdouble)GetEventTime((EventRef)arg0);
	NATIVE_EXIT(env, that, "GetEventTime\n")
	return rc;
}
#endif

#ifndef NO_GetFlavorData
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetFlavorData\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)GetFlavorData((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (void *)lparg3, (Size *)lparg4, arg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetFlavorData\n")
	return rc;
}
#endif

#ifndef NO_GetFlavorDataSize
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorDataSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetFlavorDataSize\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)GetFlavorDataSize((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (Size *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetFlavorDataSize\n")
	return rc;
}
#endif

#ifndef NO_GetFlavorType
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetFlavorType\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)GetFlavorType((DragRef)arg0, (DragItemRef)arg1, arg2, (FlavorType *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetFlavorType\n")
	return rc;
}
#endif

#ifndef NO_GetFontInfo
JNIEXPORT void JNICALL OS_NATIVE(GetFontInfo)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	NATIVE_ENTER(env, that, "GetFontInfo\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	GetFontInfo((FontInfo *)lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetFontInfo\n")
}
#endif

#ifndef NO_GetGDevice
JNIEXPORT jint JNICALL OS_NATIVE(GetGDevice)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetGDevice\n")
	rc = (jint)GetGDevice();
	NATIVE_EXIT(env, that, "GetGDevice\n")
	return rc;
}
#endif

#ifndef NO_GetGWorld
JNIEXPORT void JNICALL OS_NATIVE(GetGWorld)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetGWorld\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	GetGWorld((CGrafPtr *)lparg0, (GDHandle *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetGWorld\n")
}
#endif

#ifndef NO_GetGlobalMouse
JNIEXPORT void JNICALL OS_NATIVE(GetGlobalMouse)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "GetGlobalMouse\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	GetGlobalMouse((Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetGlobalMouse\n")
}
#endif

#ifndef NO_GetHandleSize
JNIEXPORT jint JNICALL OS_NATIVE(GetHandleSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetHandleSize\n")
	rc = (jint)GetHandleSize((Handle)arg0);
	NATIVE_EXIT(env, that, "GetHandleSize\n")
	return rc;
}
#endif

#ifndef NO_GetIconRef
JNIEXPORT jint JNICALL OS_NATIVE(GetIconRef)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetIconRef\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)GetIconRef((SInt16)arg0, (OSType)arg1, (OSType)arg2, (IconRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetIconRef\n")
	return rc;
}
#endif

#ifndef NO_GetIndMenuItemWithCommandID
JNIEXPORT jint JNICALL OS_NATIVE(GetIndMenuItemWithCommandID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jshortArray arg4)
{
	jint *lparg3=NULL;
	jshort *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetIndMenuItemWithCommandID\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)GetIndMenuItemWithCommandID((MenuRef)arg0, (MenuCommand)arg1, (UInt32)arg2, (MenuRef *)lparg3, (MenuItemIndex *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "GetIndMenuItemWithCommandID\n")
	return rc;
}
#endif

#ifndef NO_GetIndexedSubControl
JNIEXPORT jint JNICALL OS_NATIVE(GetIndexedSubControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetIndexedSubControl\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetIndexedSubControl((ControlRef)arg0, (UInt16)arg1, (ControlRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetIndexedSubControl\n")
	return rc;
}
#endif

#ifndef NO_GetItemMark
JNIEXPORT void JNICALL OS_NATIVE(GetItemMark)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	NATIVE_ENTER(env, that, "GetItemMark\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	GetItemMark((MenuRef)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetItemMark\n")
}
#endif

#ifndef NO_GetKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetKeyboardFocus\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetKeyboardFocus((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetKeyboardFocus\n")
	return rc;
}
#endif

#ifndef NO_GetLastUserEventTime
JNIEXPORT jdouble JNICALL OS_NATIVE(GetLastUserEventTime)
	(JNIEnv *env, jclass that)
{
	jdouble rc;
	NATIVE_ENTER(env, that, "GetLastUserEventTime\n")
	rc = (jdouble)GetLastUserEventTime();
	NATIVE_EXIT(env, that, "GetLastUserEventTime\n")
	return rc;
}
#endif

#ifndef NO_GetMainDevice
JNIEXPORT jint JNICALL OS_NATIVE(GetMainDevice)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMainDevice\n")
	rc = (jint)GetMainDevice();
	NATIVE_EXIT(env, that, "GetMainDevice\n")
	return rc;
}
#endif

#ifndef NO_GetMainEventQueue
JNIEXPORT jint JNICALL OS_NATIVE(GetMainEventQueue)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMainEventQueue\n")
	rc = (jint)GetMainEventQueue();
	NATIVE_EXIT(env, that, "GetMainEventQueue\n")
	return rc;
}
#endif

#ifndef NO_GetMenuCommandMark
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuCommandMark)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuCommandMark\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar *)lparg2);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetMenuCommandMark\n")
	return rc;
}
#endif

#ifndef NO_GetMenuEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuEventTarget\n")
	rc = (jint)GetMenuEventTarget((MenuRef)arg0);
	NATIVE_EXIT(env, that, "GetMenuEventTarget\n")
	return rc;
}
#endif

#ifndef NO_GetMenuFont
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuFont)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuFont\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuFont((MenuRef)arg0, (SInt16 *)lparg1, (UInt16 *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetMenuFont\n")
	return rc;
}
#endif

#ifndef NO_GetMenuHeight
JNIEXPORT jshort JNICALL OS_NATIVE(GetMenuHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetMenuHeight\n")
	rc = (jshort)GetMenuHeight((MenuRef)arg0);
	NATIVE_EXIT(env, that, "GetMenuHeight\n")
	return rc;
}
#endif

#ifndef NO_GetMenuID
JNIEXPORT jshort JNICALL OS_NATIVE(GetMenuID)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetMenuID\n")
	rc = (jshort)GetMenuID((MenuRef)arg0);
	NATIVE_EXIT(env, that, "GetMenuID\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemCommandID
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemCommandID)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuItemCommandID\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemCommandID((MenuRef)arg0, (SInt16)arg1, (MenuCommand *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetMenuItemCommandID\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemHierarchicalMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuItemHierarchicalMenu\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemHierarchicalMenu((MenuRef)arg0, (SInt16)arg1, (MenuRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetMenuItemHierarchicalMenu\n")
	return rc;
}
#endif

#ifndef NO_GetMenuItemRefCon
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemRefCon)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuItemRefCon\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetMenuItemRefCon\n")
	return rc;
}
#endif

#ifndef NO_GetMenuTrackingData
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuTrackingData)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MenuTrackingData _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetMenuTrackingData\n")
	if (arg1) lparg1 = getMenuTrackingDataFields(env, arg1, &_arg1);
	rc = (jint)GetMenuTrackingData((MenuRef)arg0, lparg1);
	if (arg1) setMenuTrackingDataFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetMenuTrackingData\n")
	return rc;
}
#endif

#ifndef NO_GetMouse
JNIEXPORT void JNICALL OS_NATIVE(GetMouse)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "GetMouse\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	GetMouse((Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "GetMouse\n")
}
#endif

#ifndef NO_GetNextDevice
JNIEXPORT jint JNICALL OS_NATIVE(GetNextDevice)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetNextDevice\n")
	rc = (jint)GetNextDevice((GDHandle)arg0);
	NATIVE_EXIT(env, that, "GetNextDevice\n")
	return rc;
}
#endif

#ifndef NO_GetPixBounds
JNIEXPORT void JNICALL OS_NATIVE(GetPixBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetPixBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetPixBounds((PixMapHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetPixBounds\n")
}
#endif

#ifndef NO_GetPixDepth
JNIEXPORT jshort JNICALL OS_NATIVE(GetPixDepth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "GetPixDepth\n")
	rc = (jshort)GetPixDepth((PixMapHandle)arg0);
	NATIVE_EXIT(env, that, "GetPixDepth\n")
	return rc;
}
#endif

#ifndef NO_GetPort
JNIEXPORT void JNICALL OS_NATIVE(GetPort)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "GetPort\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	GetPort((GrafPtr *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetPort\n")
}
#endif

#ifndef NO_GetPortBitMapForCopyBits
JNIEXPORT jint JNICALL OS_NATIVE(GetPortBitMapForCopyBits)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetPortBitMapForCopyBits\n")
	rc = (jint)GetPortBitMapForCopyBits((CGrafPtr)arg0);
	NATIVE_EXIT(env, that, "GetPortBitMapForCopyBits\n")
	return rc;
}
#endif

#ifndef NO_GetPortBounds
JNIEXPORT void JNICALL OS_NATIVE(GetPortBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetPortBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetPortBounds((CGrafPtr)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetPortBounds\n")
}
#endif

#ifndef NO_GetPortClipRegion
JNIEXPORT void JNICALL OS_NATIVE(GetPortClipRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "GetPortClipRegion\n")
	GetPortClipRegion((CGrafPtr)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "GetPortClipRegion\n")
}
#endif

#ifndef NO_GetPortVisibleRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetPortVisibleRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetPortVisibleRegion\n")
	rc = (jint)GetPortVisibleRegion((CGrafPtr)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "GetPortVisibleRegion\n")
	return rc;
}
#endif

#ifndef NO_GetPtrSize
JNIEXPORT jint JNICALL OS_NATIVE(GetPtrSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetPtrSize\n")
	rc = (jint)GetPtrSize((Ptr)arg0);
	NATIVE_EXIT(env, that, "GetPtrSize\n")
	return rc;
}
#endif

#ifndef NO_GetRegionBounds
JNIEXPORT void JNICALL OS_NATIVE(GetRegionBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetRegionBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetRegionBounds((RgnHandle)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetRegionBounds\n")
}
#endif

#ifndef NO_GetRootControl
JNIEXPORT jint JNICALL OS_NATIVE(GetRootControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetRootControl\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetRootControl((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetRootControl\n")
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorCount
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorCount)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetScrapFlavorCount\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetScrapFlavorCount((ScrapRef)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetScrapFlavorCount\n")
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorData
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jbyteArray arg3)
{
	jint *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetScrapFlavorData\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)GetScrapFlavorData((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2, (void *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetScrapFlavorData\n")
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorInfoList
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorInfoList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetScrapFlavorInfoList\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetScrapFlavorInfoList((ScrapRef)arg0, (UInt32 *)lparg1, (ScrapFlavorInfo *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetScrapFlavorInfoList\n")
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorSize
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetScrapFlavorSize\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetScrapFlavorSize((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetScrapFlavorSize\n")
	return rc;
}
#endif

#ifndef NO_GetScriptManagerVariable
JNIEXPORT jint JNICALL OS_NATIVE(GetScriptManagerVariable)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetScriptManagerVariable\n")
	rc = (jint)GetScriptManagerVariable(arg0);
	NATIVE_EXIT(env, that, "GetScriptManagerVariable\n")
	return rc;
}
#endif

#ifndef NO_GetSuperControl
JNIEXPORT jint JNICALL OS_NATIVE(GetSuperControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetSuperControl\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetSuperControl((ControlRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetSuperControl\n")
	return rc;
}
#endif

#ifndef NO_GetTabContentRect
JNIEXPORT jint JNICALL OS_NATIVE(GetTabContentRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetTabContentRect\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jint)GetTabContentRect((ControlRef)arg0, lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetTabContentRect\n")
	return rc;
}
#endif

#ifndef NO_GetThemeBrushAsColor
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeBrushAsColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2, jobject arg3)
{
	RGBColor _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeBrushAsColor\n")
	if (arg3) lparg3 = getRGBColorFields(env, arg3, &_arg3);
	rc = (jint)GetThemeBrushAsColor(arg0, arg1, arg2, lparg3);
	if (arg3) setRGBColorFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetThemeBrushAsColor\n")
	return rc;
}
#endif

#ifndef NO_GetThemeDrawingState
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeDrawingState)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeDrawingState\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)GetThemeDrawingState((ThemeDrawingState *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "GetThemeDrawingState\n")
	return rc;
}
#endif

#ifndef NO_GetThemeFont
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeFont)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jbyteArray arg2, jshortArray arg3, jbyteArray arg4)
{
	jbyte *lparg2=NULL;
	jshort *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeFont\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)GetThemeFont((ThemeFontID)arg0, (ScriptCode)arg1, (char *)lparg2, (SInt16 *)lparg3, (Style *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "GetThemeFont\n")
	return rc;
}
#endif

#ifndef NO_GetThemeMenuItemExtra
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMenuItemExtra)
	(JNIEnv *env, jclass that, jshort arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeMenuItemExtra\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)GetThemeMenuItemExtra(arg0, lparg1, lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetThemeMenuItemExtra\n")
	return rc;
}
#endif

#ifndef NO_GetThemeMetric
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMetric)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeMetric\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetThemeMetric(arg0, lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetThemeMetric\n")
	return rc;
}
#endif

#ifndef NO_GetThemeTextColor
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeTextColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2, jobject arg3)
{
	RGBColor _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeTextColor\n")
	if (arg3) lparg3 = getRGBColorFields(env, arg3, &_arg3);
	rc = (jint)GetThemeTextColor(arg0, arg1, arg2, lparg3);
	if (arg3) setRGBColorFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "GetThemeTextColor\n")
	return rc;
}
#endif

#ifndef NO_GetThemeTextDimensions
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeTextDimensions)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshortArray arg5)
{
	Point _arg4, *lparg4=NULL;
	jshort *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetThemeTextDimensions\n")
	if (arg4) lparg4 = getPointFields(env, arg4, &_arg4);
	if (arg5) lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL);
	rc = (jint)GetThemeTextDimensions((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (Point *)lparg4, (SInt16 *)lparg5);
	if (arg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg4) setPointFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "GetThemeTextDimensions\n")
	return rc;
}
#endif

#ifndef NO_GetUserFocusEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetUserFocusEventTarget)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetUserFocusEventTarget\n")
	rc = (jint)GetUserFocusEventTarget();
	NATIVE_EXIT(env, that, "GetUserFocusEventTarget\n")
	return rc;
}
#endif

#ifndef NO_GetUserFocusWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetUserFocusWindow)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetUserFocusWindow\n")
	rc = (jint)GetUserFocusWindow();
	NATIVE_EXIT(env, that, "GetUserFocusWindow\n")
	return rc;
}
#endif

#ifndef NO_GetWRefCon
JNIEXPORT jint JNICALL OS_NATIVE(GetWRefCon)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWRefCon\n")
	rc = (jint)GetWRefCon((WindowRef)arg0);
	NATIVE_EXIT(env, that, "GetWRefCon\n")
	return rc;
}
#endif

#ifndef NO_GetWindowActivationScope
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowActivationScope)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowActivationScope\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowActivationScope((WindowRef)arg0, (WindowActivationScope *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowActivationScope\n")
	return rc;
}
#endif

#ifndef NO_GetWindowBounds
JNIEXPORT void JNICALL OS_NATIVE(GetWindowBounds)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "GetWindowBounds\n")
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	GetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "GetWindowBounds\n")
}
#endif

#ifndef NO_GetWindowDefaultButton
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowDefaultButton)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowDefaultButton\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)GetWindowDefaultButton((WindowRef)arg0, (ControlRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowDefaultButton\n")
	return rc;
}
#endif

#ifndef NO_GetWindowEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowEventTarget\n")
	rc = (jint)GetWindowEventTarget((WindowRef)arg0);
	NATIVE_EXIT(env, that, "GetWindowEventTarget\n")
	return rc;
}
#endif

#ifndef NO_GetWindowFromPort
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowFromPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowFromPort\n")
	rc = (jint)GetWindowFromPort((CGrafPtr)arg0);
	NATIVE_EXIT(env, that, "GetWindowFromPort\n")
	return rc;
}
#endif

#ifndef NO_GetWindowGroupOfClass
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowGroupOfClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowGroupOfClass\n")
	rc = (jint)GetWindowGroupOfClass(arg0);
	NATIVE_EXIT(env, that, "GetWindowGroupOfClass\n")
	return rc;
}
#endif

#ifndef NO_GetWindowModality
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowModality)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowModality\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)GetWindowModality((WindowRef)arg0, (WindowModality *)lparg1, (WindowRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "GetWindowModality\n")
	return rc;
}
#endif

#ifndef NO_GetWindowPort
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowPort\n")
	rc = (jint)GetWindowPort((WindowRef)arg0);
	NATIVE_EXIT(env, that, "GetWindowPort\n")
	return rc;
}
#endif

#ifndef NO_GetWindowRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowRegion)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "GetWindowRegion\n")
	rc = (jint)GetWindowRegion((WindowRef)arg0, (WindowRegionCode)arg1, (RgnHandle)arg2);
	NATIVE_EXIT(env, that, "GetWindowRegion\n")
	return rc;
}
#endif

#ifndef NO_GetWindowStructureWidths
JNIEXPORT void JNICALL OS_NATIVE(GetWindowStructureWidths)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "GetWindowStructureWidths\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	GetWindowStructureWidths((WindowRef)arg0, (Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "GetWindowStructureWidths\n")
}
#endif

#ifndef NO_HIComboBoxAppendTextItem
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxAppendTextItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxAppendTextItem\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIComboBoxAppendTextItem((HIViewRef)arg0, (CFStringRef)arg1, (CFIndex *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "HIComboBoxAppendTextItem\n")
	return rc;
}
#endif

#ifndef NO_HIComboBoxCopyTextItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxCopyTextItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxCopyTextItemAtIndex\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIComboBoxCopyTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "HIComboBoxCopyTextItemAtIndex\n")
	return rc;
}
#endif

#ifndef NO_HIComboBoxCreate
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxCreate)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jintArray arg5)
{
	CGRect _arg0, *lparg0=NULL;
	ControlFontStyleRec _arg2, *lparg2=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxCreate\n")
	if (arg0) lparg0 = getCGRectFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getControlFontStyleRecFields(env, arg2, &_arg2);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)HIComboBoxCreate((const HIRect *)lparg0, (CFStringRef)arg1, (const ControlFontStyleRec *)lparg2, (CFArrayRef)arg3, (OptionBits)arg4, (HIViewRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg2) setControlFontStyleRecFields(env, arg2, lparg2);
	if (arg0) setCGRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "HIComboBoxCreate\n")
	return rc;
}
#endif

#ifndef NO_HIComboBoxGetItemCount
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxGetItemCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxGetItemCount\n")
	rc = (jint)HIComboBoxGetItemCount((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIComboBoxGetItemCount\n")
	return rc;
}
#endif

#ifndef NO_HIComboBoxInsertTextItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxInsertTextItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxInsertTextItemAtIndex\n")
	rc = (jint)HIComboBoxInsertTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef)arg2);
	NATIVE_EXIT(env, that, "HIComboBoxInsertTextItemAtIndex\n")
	return rc;
}
#endif

#ifndef NO_HIComboBoxRemoveItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxRemoveItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIComboBoxRemoveItemAtIndex\n")
	rc = (jint)HIComboBoxRemoveItemAtIndex((HIViewRef)arg0, (CFIndex)arg1);
	NATIVE_EXIT(env, that, "HIComboBoxRemoveItemAtIndex\n")
	return rc;
}
#endif

#ifndef NO_HIObjectCopyClassID
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectCopyClassID)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIObjectCopyClassID\n")
	rc = (jint)HIObjectCopyClassID((HIObjectRef)arg0);
	NATIVE_EXIT(env, that, "HIObjectCopyClassID\n")
	return rc;
}
#endif

#ifndef NO_HIObjectCreate
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIObjectCreate\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIObjectCreate((CFStringRef)arg0, (EventRef)arg1, (HIObjectRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "HIObjectCreate\n")
	return rc;
}
#endif

#ifndef NO_HIObjectRegisterSubclass
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectRegisterSubclass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jint arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIObjectRegisterSubclass\n")
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)HIObjectRegisterSubclass((CFStringRef)arg0, (CFStringRef)arg1, (OptionBits)arg2, (EventHandlerUPP)arg3, (UInt32)arg4, (const EventTypeSpec *)lparg5, (void *)arg6, (HIObjectClassRef *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "HIObjectRegisterSubclass\n")
	return rc;
}
#endif

#ifndef NO_HIViewAddSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewAddSubview)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewAddSubview\n")
	rc = (jint)HIViewAddSubview((HIViewRef)arg0, (HIViewRef)arg1);
	NATIVE_EXIT(env, that, "HIViewAddSubview\n")
	return rc;
}
#endif

#ifndef NO_HIViewClick
JNIEXPORT jint JNICALL OS_NATIVE(HIViewClick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewClick\n")
	rc = (jint)HIViewClick((HIViewRef)arg0, (EventRef)arg1);
	NATIVE_EXIT(env, that, "HIViewClick\n")
	return rc;
}
#endif

#ifndef NO_HIViewConvertPoint
JNIEXPORT jint JNICALL OS_NATIVE(HIViewConvertPoint)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewConvertPoint\n")
	if (arg0) lparg0 = getCGPointFields(env, arg0, &_arg0);
	rc = (jint)HIViewConvertPoint((HIPoint *)lparg0, (HIViewRef)arg1, (HIViewRef)arg2);
	if (arg0) setCGPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "HIViewConvertPoint\n")
	return rc;
}
#endif

#ifndef NO_HIViewFindByID
JNIEXPORT jint JNICALL OS_NATIVE(HIViewFindByID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewFindByID\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIViewFindByID((HIViewRef)arg0, *(HIViewID *)arg1, (HIViewRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "HIViewFindByID\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetFirstSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetFirstSubview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetFirstSubview\n")
	rc = (jint)HIViewGetFirstSubview((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIViewGetFirstSubview\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetFrame
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetFrame)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetFrame\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	rc = (jint)HIViewGetFrame((HIViewRef)arg0, (HIRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HIViewGetFrame\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetLastSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetLastSubview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetLastSubview\n")
	rc = (jint)HIViewGetLastSubview((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIViewGetLastSubview\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetNextView
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetNextView)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetNextView\n")
	rc = (jint)HIViewGetNextView((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIViewGetNextView\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetRoot
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetRoot)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetRoot\n")
	rc = (jint)HIViewGetRoot((WindowRef)arg0);
	NATIVE_EXIT(env, that, "HIViewGetRoot\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetSizeConstraints
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetSizeConstraints)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	CGRect _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetSizeConstraints\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getCGRectFields(env, arg2, &_arg2);
	rc = (jint)HIViewGetSizeConstraints((HIViewRef)arg0, (HISize *)lparg1, (HISize *)lparg2);
	if (arg2) setCGRectFields(env, arg2, lparg2);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HIViewGetSizeConstraints\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetSubviewHit
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetSubviewHit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2, jintArray arg3)
{
	CGPoint _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetSubviewHit\n")
	if (arg1) lparg1 = getCGPointFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)HIViewGetSubviewHit((HIViewRef)arg0, (CGPoint *)lparg1, (Boolean)arg2, (HIViewRef *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) setCGPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HIViewGetSubviewHit\n")
	return rc;
}
#endif

#ifndef NO_HIViewGetViewForMouseEvent
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetViewForMouseEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewGetViewForMouseEvent\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)HIViewGetViewForMouseEvent((HIViewRef)arg0, (EventRef)arg1, (HIViewRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "HIViewGetViewForMouseEvent\n")
	return rc;
}
#endif

#ifndef NO_HIViewIsVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(HIViewIsVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "HIViewIsVisible\n")
	rc = (jboolean)HIViewIsVisible((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIViewIsVisible\n")
	return rc;
}
#endif

#ifndef NO_HIViewRemoveFromSuperview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewRemoveFromSuperview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewRemoveFromSuperview\n")
	rc = (jint)HIViewRemoveFromSuperview((HIViewRef)arg0);
	NATIVE_EXIT(env, that, "HIViewRemoveFromSuperview\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetBoundsOrigin
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetBoundsOrigin)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetBoundsOrigin\n")
	rc = (jint)HIViewSetBoundsOrigin((HIViewRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "HIViewSetBoundsOrigin\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetDrawingEnabled
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetDrawingEnabled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetDrawingEnabled\n")
	rc = (jint)HIViewSetDrawingEnabled((HIViewRef)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "HIViewSetDrawingEnabled\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetFrame
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetFrame)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetFrame\n")
	if (arg1) lparg1 = getCGRectFields(env, arg1, &_arg1);
	rc = (jint)HIViewSetFrame((HIViewRef)arg0, (const HIRect *)lparg1);
	if (arg1) setCGRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HIViewSetFrame\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetNeedsDisplay
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetNeedsDisplay)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetNeedsDisplay\n")
	rc = (jint)HIViewSetNeedsDisplay((HIViewRef)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "HIViewSetNeedsDisplay\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetNeedsDisplayInRegion
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetNeedsDisplayInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetNeedsDisplayInRegion\n")
	rc = (jint)HIViewSetNeedsDisplayInRegion((HIViewRef)arg0, (RgnHandle)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "HIViewSetNeedsDisplayInRegion\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetVisible
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetVisible)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetVisible\n")
	rc = (jint)HIViewSetVisible((HIViewRef)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "HIViewSetVisible\n")
	return rc;
}
#endif

#ifndef NO_HIViewSetZOrder
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetZOrder)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSetZOrder\n")
	rc = (jint)HIViewSetZOrder((HIViewRef)arg0, (HIViewZOrderOp)arg1, (HIViewRef)arg2);
	NATIVE_EXIT(env, that, "HIViewSetZOrder\n")
	return rc;
}
#endif

#ifndef NO_HIViewSimulateClick
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSimulateClick)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HIViewSimulateClick\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)HIViewSimulateClick((HIViewRef)arg0, (HIViewPartCode)arg1, (UInt32)arg2, (ControlPartCode *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "HIViewSimulateClick\n")
	return rc;
}
#endif

#ifndef NO_HLock
JNIEXPORT void JNICALL OS_NATIVE(HLock)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "HLock\n")
	HLock((Handle)arg0);
	NATIVE_EXIT(env, that, "HLock\n")
}
#endif

#ifndef NO_HMGetTagDelay
JNIEXPORT jint JNICALL OS_NATIVE(HMGetTagDelay)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HMGetTagDelay\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)HMGetTagDelay(lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "HMGetTagDelay\n")
	return rc;
}
#endif

#ifndef NO_HMHideTag
JNIEXPORT jint JNICALL OS_NATIVE(HMHideTag)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "HMHideTag\n")
	rc = (jint)HMHideTag();
	NATIVE_EXIT(env, that, "HMHideTag\n")
	return rc;
}
#endif

#ifndef NO_HMInstallControlContentCallback
JNIEXPORT void JNICALL OS_NATIVE(HMInstallControlContentCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "HMInstallControlContentCallback\n")
	HMInstallControlContentCallback((ControlRef)arg0, (HMControlContentUPP)arg1);
	NATIVE_EXIT(env, that, "HMInstallControlContentCallback\n")
}
#endif

#ifndef NO_HMSetTagDelay
JNIEXPORT jint JNICALL OS_NATIVE(HMSetTagDelay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "HMSetTagDelay\n")
	rc = (jint)HMSetTagDelay(arg0);
	NATIVE_EXIT(env, that, "HMSetTagDelay\n")
	return rc;
}
#endif

#ifndef NO_HUnlock
JNIEXPORT void JNICALL OS_NATIVE(HUnlock)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "HUnlock\n")
	HUnlock((Handle)arg0);
	NATIVE_EXIT(env, that, "HUnlock\n")
}
#endif

#ifndef NO_HandleControlClick
JNIEXPORT jshort JNICALL OS_NATIVE(HandleControlClick)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	Point _arg1, *lparg1=NULL;
	jshort rc;
	NATIVE_ENTER(env, that, "HandleControlClick\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	rc = (jshort)HandleControlClick((ControlRef)arg0, *lparg1, (EventModifiers)arg2, (ControlActionUPP)arg3);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HandleControlClick\n")
	return rc;
}
#endif

#ifndef NO_HandleControlSetCursor
JNIEXPORT jint JNICALL OS_NATIVE(HandleControlSetCursor)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbooleanArray arg3)
{
	Point _arg1, *lparg1=NULL;
	jboolean *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "HandleControlSetCursor\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetBooleanArrayElements(env, arg3, NULL);
	rc = (jint)HandleControlSetCursor((ControlRef)arg0, *lparg1, (EventModifiers)arg2, (Boolean *)lparg3);
	if (arg3) (*env)->ReleaseBooleanArrayElements(env, arg3, lparg3, 0);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "HandleControlSetCursor\n")
	return rc;
}
#endif

#ifndef NO_HiWord
JNIEXPORT jshort JNICALL OS_NATIVE(HiWord)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "HiWord\n")
	rc = (jshort)HiWord(arg0);
	NATIVE_EXIT(env, that, "HiWord\n")
	return rc;
}
#endif

#ifndef NO_HideWindow
JNIEXPORT void JNICALL OS_NATIVE(HideWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "HideWindow\n")
	HideWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "HideWindow\n")
}
#endif

#ifndef NO_HiliteMenu
JNIEXPORT void JNICALL OS_NATIVE(HiliteMenu)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "HiliteMenu\n")
	HiliteMenu((MenuID)arg0);
	NATIVE_EXIT(env, that, "HiliteMenu\n")
}
#endif

#ifndef NO_InitContextualMenus
JNIEXPORT jint JNICALL OS_NATIVE(InitContextualMenus)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "InitContextualMenus\n")
	rc = (jint)InitContextualMenus();
	NATIVE_EXIT(env, that, "InitContextualMenus\n")
	return rc;
}
#endif

#ifndef NO_InitCursor
JNIEXPORT void JNICALL OS_NATIVE(InitCursor)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "InitCursor\n")
	InitCursor();
	NATIVE_EXIT(env, that, "InitCursor\n")
}
#endif

#ifndef NO_InitDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(InitDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCallbacks _arg0={0}, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InitDataBrowserCallbacks\n")
	if (arg0) lparg0 = getDataBrowserCallbacksFields(env, arg0, &_arg0);
	rc = (jint)InitDataBrowserCallbacks((DataBrowserCallbacks *)lparg0);
	if (arg0) setDataBrowserCallbacksFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "InitDataBrowserCallbacks\n")
	return rc;
}
#endif

#ifndef NO_InitDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(InitDataBrowserCustomCallbacks)
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCustomCallbacks _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InitDataBrowserCustomCallbacks\n")
	if (arg0) lparg0 = getDataBrowserCustomCallbacksFields(env, arg0, &_arg0);
	rc = (jint)InitDataBrowserCustomCallbacks(lparg0);
	if (arg0) setDataBrowserCustomCallbacksFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "InitDataBrowserCustomCallbacks\n")
	return rc;
}
#endif

#ifndef NO_InsertMenu
JNIEXPORT void JNICALL OS_NATIVE(InsertMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "InsertMenu\n")
	InsertMenu((MenuRef)arg0, (MenuID)arg1);
	NATIVE_EXIT(env, that, "InsertMenu\n")
}
#endif

#ifndef NO_InsertMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(InsertMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "InsertMenuItemTextWithCFString\n")
	rc = (jint)InsertMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemIndex)arg2, (MenuItemAttributes)arg3, (MenuCommand)arg4);
	NATIVE_EXIT(env, that, "InsertMenuItemTextWithCFString\n")
	return rc;
}
#endif

#ifndef NO_InstallEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InstallEventHandler\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)InstallEventHandler((EventTargetRef)arg0, (EventHandlerUPP)arg1, (UInt32)arg2, (const EventTypeSpec *)lparg3, (void *)arg4, (EventHandlerRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "InstallEventHandler\n")
	return rc;
}
#endif

#ifndef NO_InstallEventLoopTimer
JNIEXPORT jint JNICALL OS_NATIVE(InstallEventLoopTimer)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InstallEventLoopTimer\n")
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)InstallEventLoopTimer((EventLoopRef)arg0, (EventTimerInterval)arg1, (EventTimerInterval)arg2, (EventLoopTimerUPP)arg3, (void *)arg4, (EventLoopTimerRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "InstallEventLoopTimer\n")
	return rc;
}
#endif

#ifndef NO_InstallReceiveHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallReceiveHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InstallReceiveHandler\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)InstallReceiveHandler((DragReceiveHandlerUPP)arg0, (WindowRef)arg1, (void *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "InstallReceiveHandler\n")
	return rc;
}
#endif

#ifndef NO_InstallTrackingHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallTrackingHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "InstallTrackingHandler\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)InstallTrackingHandler((DragTrackingHandlerUPP)arg0, (WindowRef)arg1, (void *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "InstallTrackingHandler\n")
	return rc;
}
#endif

#ifndef NO_InvalWindowRect
JNIEXPORT void JNICALL OS_NATIVE(InvalWindowRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "InvalWindowRect\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	InvalWindowRect((WindowRef)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "InvalWindowRect\n")
}
#endif

#ifndef NO_InvalWindowRgn
JNIEXPORT void JNICALL OS_NATIVE(InvalWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "InvalWindowRgn\n")
	InvalWindowRgn((WindowRef)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "InvalWindowRgn\n")
}
#endif

#ifndef NO_InvertRect
JNIEXPORT void JNICALL OS_NATIVE(InvertRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "InvertRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	InvertRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "InvertRect\n")
}
#endif

#ifndef NO_InvertRgn
JNIEXPORT void JNICALL OS_NATIVE(InvertRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "InvertRgn\n")
	InvertRgn((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "InvertRgn\n")
}
#endif

#ifndef NO_IsControlActive
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsControlActive\n")
	rc = (jboolean)IsControlActive((ControlRef)arg0);
	NATIVE_EXIT(env, that, "IsControlActive\n")
	return rc;
}
#endif

#ifndef NO_IsControlEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsControlEnabled\n")
	rc = (jboolean)IsControlEnabled((ControlRef)arg0);
	NATIVE_EXIT(env, that, "IsControlEnabled\n")
	return rc;
}
#endif

#ifndef NO_IsControlVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsControlVisible\n")
	rc = (jboolean)IsControlVisible((ControlRef)arg0);
	NATIVE_EXIT(env, that, "IsControlVisible\n")
	return rc;
}
#endif

#ifndef NO_IsDataBrowserItemSelected
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDataBrowserItemSelected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsDataBrowserItemSelected\n")
	rc = (jboolean)IsDataBrowserItemSelected((ControlRef)arg0, (DataBrowserItemID)arg1);
	NATIVE_EXIT(env, that, "IsDataBrowserItemSelected\n")
	return rc;
}
#endif

#ifndef NO_IsMenuCommandEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsMenuCommandEnabled)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsMenuCommandEnabled\n")
	rc = (jboolean)IsMenuCommandEnabled((MenuRef)arg0, (MenuCommand)arg1);
	NATIVE_EXIT(env, that, "IsMenuCommandEnabled\n")
	return rc;
}
#endif

#ifndef NO_IsMenuItemEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsMenuItemEnabled)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsMenuItemEnabled\n")
	rc = (jboolean)IsMenuItemEnabled((MenuRef)arg0, (MenuItemIndex)arg1);
	NATIVE_EXIT(env, that, "IsMenuItemEnabled\n")
	return rc;
}
#endif

#ifndef NO_IsValidControlHandle
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidControlHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsValidControlHandle\n")
	rc = (jboolean)IsValidControlHandle((ControlRef)arg0);
	NATIVE_EXIT(env, that, "IsValidControlHandle\n")
	return rc;
}
#endif

#ifndef NO_IsValidMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsValidMenu\n")
	rc = (jboolean)IsValidMenu((MenuRef)arg0);
	NATIVE_EXIT(env, that, "IsValidMenu\n")
	return rc;
}
#endif

#ifndef NO_IsValidWindowPtr
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidWindowPtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsValidWindowPtr\n")
	rc = (jboolean)IsValidWindowPtr((WindowRef)arg0);
	NATIVE_EXIT(env, that, "IsValidWindowPtr\n")
	return rc;
}
#endif

#ifndef NO_IsWindowActive
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsWindowActive\n")
	rc = (jboolean)IsWindowActive((WindowRef)arg0);
	NATIVE_EXIT(env, that, "IsWindowActive\n")
	return rc;
}
#endif

#ifndef NO_IsWindowCollapsed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowCollapsed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsWindowCollapsed\n")
	rc = (jboolean)IsWindowCollapsed((WindowRef)arg0);
	NATIVE_EXIT(env, that, "IsWindowCollapsed\n")
	return rc;
}
#endif

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "IsWindowVisible\n")
	rc = (jboolean)IsWindowVisible((WindowRef)arg0);
	NATIVE_EXIT(env, that, "IsWindowVisible\n")
	return rc;
}
#endif

#ifndef NO_KeyTranslate
JNIEXPORT jint JNICALL OS_NATIVE(KeyTranslate)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "KeyTranslate\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)KeyTranslate((const void *)arg0, arg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "KeyTranslate\n")
	return rc;
}
#endif

#ifndef NO_KillPoly
JNIEXPORT void JNICALL OS_NATIVE(KillPoly)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "KillPoly\n")
	KillPoly((PolyHandle)arg0);
	NATIVE_EXIT(env, that, "KillPoly\n")
}
#endif

#ifndef NO_LineTo
JNIEXPORT void JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "LineTo\n")
	LineTo((short)arg0, (short)arg1);
	NATIVE_EXIT(env, that, "LineTo\n")
}
#endif

#ifndef NO_LoWord
JNIEXPORT jshort JNICALL OS_NATIVE(LoWord)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc;
	NATIVE_ENTER(env, that, "LoWord\n")
	rc = (jshort)LoWord(arg0);
	NATIVE_EXIT(env, that, "LoWord\n")
	return rc;
}
#endif

#ifndef NO_LockPortBits
JNIEXPORT jint JNICALL OS_NATIVE(LockPortBits)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "LockPortBits\n")
	rc = (jint)LockPortBits((GrafPtr)arg0);
	NATIVE_EXIT(env, that, "LockPortBits\n")
	return rc;
}
#endif

#ifndef NO_Long2Fix
JNIEXPORT jint JNICALL OS_NATIVE(Long2Fix)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "Long2Fix\n")
	rc = (jint)Long2Fix(arg0);
	NATIVE_EXIT(env, that, "Long2Fix\n")
	return rc;
}
#endif

#ifndef NO_MenuSelect
JNIEXPORT jint JNICALL OS_NATIVE(MenuSelect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "MenuSelect\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	rc = (jint)MenuSelect(*(Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "MenuSelect\n")
	return rc;
}
#endif

#ifndef NO_MoveControl
JNIEXPORT void JNICALL OS_NATIVE(MoveControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "MoveControl\n")
	MoveControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
	NATIVE_EXIT(env, that, "MoveControl\n")
}
#endif

#ifndef NO_MoveTo
JNIEXPORT void JNICALL OS_NATIVE(MoveTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "MoveTo\n")
	MoveTo((short)arg0, (short)arg1);
	NATIVE_EXIT(env, that, "MoveTo\n")
}
#endif

#ifndef NO_MoveWindow
JNIEXPORT void JNICALL OS_NATIVE(MoveWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	NATIVE_ENTER(env, that, "MoveWindow\n")
	MoveWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
	NATIVE_EXIT(env, that, "MoveWindow\n")
}
#endif

#ifndef NO_NavCreateChooseFolderDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreateChooseFolderDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NavCreateChooseFolderDialog\n")
	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)NavCreateChooseFolderDialog((const NavDialogCreationOptions *)lparg0, (NavEventUPP)arg1, (NavObjectFilterUPP)arg2, (void *)arg3, (NavDialogRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "NavCreateChooseFolderDialog\n")
	return rc;
}
#endif

#ifndef NO_NavCreateGetFileDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreateGetFileDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NavCreateGetFileDialog\n")
	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)NavCreateGetFileDialog((const NavDialogCreationOptions *)lparg0, (NavTypeListHandle)arg1, (NavEventUPP)arg2, (NavPreviewUPP)arg3, (NavObjectFilterUPP)arg4, (void *)arg5, (NavDialogRef *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "NavCreateGetFileDialog\n")
	return rc;
}
#endif

#ifndef NO_NavCreatePutFileDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreatePutFileDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NavCreatePutFileDialog\n")
	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)NavCreatePutFileDialog((const NavDialogCreationOptions *)lparg0, (OSType)arg1, (OSType)arg2, (NavEventUPP)arg3, (void *)arg4, (NavDialogRef *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "NavCreatePutFileDialog\n")
	return rc;
}
#endif

#ifndef NO_NavDialogDispose
JNIEXPORT void JNICALL OS_NATIVE(NavDialogDispose)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "NavDialogDispose\n")
	NavDialogDispose((NavDialogRef)arg0);
	NATIVE_EXIT(env, that, "NavDialogDispose\n")
}
#endif

#ifndef NO_NavDialogGetReply
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetReply)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NavReplyRecord _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NavDialogGetReply\n")
	if (arg1) lparg1 = getNavReplyRecordFields(env, arg1, &_arg1);
	rc = (jint)NavDialogGetReply((NavDialogRef)arg0, (NavReplyRecord *)lparg1);
	if (arg1) setNavReplyRecordFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "NavDialogGetReply\n")
	return rc;
}
#endif

#ifndef NO_NavDialogGetSaveFileName
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetSaveFileName)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NavDialogGetSaveFileName\n")
	rc = (jint)NavDialogGetSaveFileName((NavDialogRef)arg0);
	NATIVE_EXIT(env, that, "NavDialogGetSaveFileName\n")
	return rc;
}
#endif

#ifndef NO_NavDialogGetUserAction
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetUserAction)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NavDialogGetUserAction\n")
	rc = (jint)NavDialogGetUserAction((NavDialogRef)arg0);
	NATIVE_EXIT(env, that, "NavDialogGetUserAction\n")
	return rc;
}
#endif

#ifndef NO_NavDialogRun
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogRun)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NavDialogRun\n")
	rc = (jint)NavDialogRun((NavDialogRef)arg0);
	NATIVE_EXIT(env, that, "NavDialogRun\n")
	return rc;
}
#endif

#ifndef NO_NavDialogSetSaveFileName
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogSetSaveFileName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "NavDialogSetSaveFileName\n")
	rc = (jint)NavDialogSetSaveFileName((NavDialogRef)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "NavDialogSetSaveFileName\n")
	return rc;
}
#endif

#ifndef NO_NavGetDefaultDialogCreationOptions
JNIEXPORT jint JNICALL OS_NATIVE(NavGetDefaultDialogCreationOptions)
	(JNIEnv *env, jclass that, jobject arg0)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NavGetDefaultDialogCreationOptions\n")
	if (arg0) lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0);
	rc = (jint)NavGetDefaultDialogCreationOptions((NavDialogCreationOptions *)lparg0);
	if (arg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "NavGetDefaultDialogCreationOptions\n")
	return rc;
}
#endif

#ifndef NO_NewControl
JNIEXPORT jint JNICALL OS_NATIVE(NewControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jboolean arg3, jshort arg4, jshort arg5, jshort arg6, jshort arg7, jint arg8)
{
	Rect _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NewControl\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)NewControl((WindowRef)arg0, (const Rect *)lparg1, (ConstStr255Param)lparg2, (Boolean)arg3, (SInt16)arg4, (SInt16)arg5, (SInt16)arg6, (SInt16)arg7, (SInt32)arg8);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "NewControl\n")
	return rc;
}
#endif

#ifndef NO_NewDrag
JNIEXPORT jint JNICALL OS_NATIVE(NewDrag)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NewDrag\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)NewDrag((DragRef *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "NewDrag\n")
	return rc;
}
#endif

#ifndef NO_NewGWorldFromPtr
JNIEXPORT jint JNICALL OS_NATIVE(NewGWorldFromPtr)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint *lparg0=NULL;
	Rect _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "NewGWorldFromPtr\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	rc = (jint)NewGWorldFromPtr((GWorldPtr *)lparg0, (unsigned long)arg1, (const Rect *)lparg2, (CTabHandle)arg3, (GDHandle)arg4, (GWorldFlags)arg5, (Ptr)arg6, (long)arg7);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "NewGWorldFromPtr\n")
	return rc;
}
#endif

#ifndef NO_NewHandle
JNIEXPORT jint JNICALL OS_NATIVE(NewHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NewHandle\n")
	rc = (jint)NewHandle((Size)arg0);
	NATIVE_EXIT(env, that, "NewHandle\n")
	return rc;
}
#endif

#ifndef NO_NewHandleClear
JNIEXPORT jint JNICALL OS_NATIVE(NewHandleClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NewHandleClear\n")
	rc = (jint)NewHandleClear((Size)arg0);
	NATIVE_EXIT(env, that, "NewHandleClear\n")
	return rc;
}
#endif

#ifndef NO_NewPtr
JNIEXPORT jint JNICALL OS_NATIVE(NewPtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NewPtr\n")
	rc = (jint)NewPtr((Size)arg0);
	NATIVE_EXIT(env, that, "NewPtr\n")
	return rc;
}
#endif

#ifndef NO_NewPtrClear
JNIEXPORT jint JNICALL OS_NATIVE(NewPtrClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "NewPtrClear\n")
	rc = (jint)NewPtrClear((Size)arg0);
	NATIVE_EXIT(env, that, "NewPtrClear\n")
	return rc;
}
#endif

#ifndef NO_NewRgn
JNIEXPORT jint JNICALL OS_NATIVE(NewRgn)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "NewRgn\n")
	rc = (jint)NewRgn();
	NATIVE_EXIT(env, that, "NewRgn\n")
	return rc;
}
#endif

#ifndef NO_OffsetRect
JNIEXPORT void JNICALL OS_NATIVE(OffsetRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "OffsetRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	OffsetRect(lparg0, arg1, arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "OffsetRect\n")
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT void JNICALL OS_NATIVE(OffsetRgn)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "OffsetRgn\n")
	OffsetRgn((RgnHandle)arg0, (short)arg1, (short)arg2);
	NATIVE_EXIT(env, that, "OffsetRgn\n")
}
#endif

#ifndef NO_OpenDataBrowserContainer
JNIEXPORT jint JNICALL OS_NATIVE(OpenDataBrowserContainer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "OpenDataBrowserContainer\n")
	rc = (jint)OpenDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
	NATIVE_EXIT(env, that, "OpenDataBrowserContainer\n")
	return rc;
}
#endif

#ifndef NO_OpenPoly
JNIEXPORT jint JNICALL OS_NATIVE(OpenPoly)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "OpenPoly\n")
	rc = (jint)OpenPoly();
	NATIVE_EXIT(env, that, "OpenPoly\n")
	return rc;
}
#endif

#ifndef NO_OpenRgn
JNIEXPORT void JNICALL OS_NATIVE(OpenRgn)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "OpenRgn\n")
	OpenRgn();
	NATIVE_EXIT(env, that, "OpenRgn\n")
}
#endif

#ifndef NO_PMCreatePageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMCreatePageFormat)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMCreatePageFormat\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)PMCreatePageFormat((PMPageFormat *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PMCreatePageFormat\n")
	return rc;
}
#endif

#ifndef NO_PMCreatePrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMCreatePrintSettings)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMCreatePrintSettings\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)PMCreatePrintSettings((PMPrintSettings *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PMCreatePrintSettings\n")
	return rc;
}
#endif

#ifndef NO_PMCreateSession
JNIEXPORT jint JNICALL OS_NATIVE(PMCreateSession)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMCreateSession\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)PMCreateSession((PMPrintSession *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "PMCreateSession\n")
	return rc;
}
#endif

#ifndef NO_PMFlattenPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMFlattenPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMFlattenPageFormat\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMFlattenPageFormat((PMPageFormat)arg0, (Handle *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMFlattenPageFormat\n")
	return rc;
}
#endif

#ifndef NO_PMFlattenPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMFlattenPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMFlattenPrintSettings\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMFlattenPrintSettings((PMPrintSettings)arg0, (Handle *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMFlattenPrintSettings\n")
	return rc;
}
#endif

#ifndef NO_PMGetAdjustedPageRect
JNIEXPORT jint JNICALL OS_NATIVE(PMGetAdjustedPageRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMRect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetAdjustedPageRect\n")
	if (arg1) lparg1 = getPMRectFields(env, arg1, &_arg1);
	rc = (jint)PMGetAdjustedPageRect((PMPageFormat)arg0, (PMRect *)lparg1);
	if (arg1) setPMRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PMGetAdjustedPageRect\n")
	return rc;
}
#endif

#ifndef NO_PMGetAdjustedPaperRect
JNIEXPORT jint JNICALL OS_NATIVE(PMGetAdjustedPaperRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMRect _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetAdjustedPaperRect\n")
	if (arg1) lparg1 = getPMRectFields(env, arg1, &_arg1);
	rc = (jint)PMGetAdjustedPaperRect((PMPageFormat)arg0, (PMRect *)lparg1);
	if (arg1) setPMRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PMGetAdjustedPaperRect\n")
	return rc;
}
#endif

#ifndef NO_PMGetCollate
JNIEXPORT jint JNICALL OS_NATIVE(PMGetCollate)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetCollate\n")
	if (arg1) lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL);
	rc = (jint)PMGetCollate((PMPrintSettings)arg0, lparg1);
	if (arg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetCollate\n")
	return rc;
}
#endif

#ifndef NO_PMGetCopies
JNIEXPORT jint JNICALL OS_NATIVE(PMGetCopies)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetCopies\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMGetCopies((PMPrintSettings)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetCopies\n")
	return rc;
}
#endif

#ifndef NO_PMGetFirstPage
JNIEXPORT jint JNICALL OS_NATIVE(PMGetFirstPage)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetFirstPage\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMGetFirstPage((PMPrintSettings)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetFirstPage\n")
	return rc;
}
#endif

#ifndef NO_PMGetJobNameCFString
JNIEXPORT jint JNICALL OS_NATIVE(PMGetJobNameCFString)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetJobNameCFString\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMGetJobNameCFString((PMPrintSettings)arg0, (CFStringRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetJobNameCFString\n")
	return rc;
}
#endif

#ifndef NO_PMGetLastPage
JNIEXPORT jint JNICALL OS_NATIVE(PMGetLastPage)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetLastPage\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMGetLastPage((PMPrintSettings)arg0, (UInt32 *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetLastPage\n")
	return rc;
}
#endif

#ifndef NO_PMGetPageRange
JNIEXPORT jint JNICALL OS_NATIVE(PMGetPageRange)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetPageRange\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PMGetPageRange((PMPrintSettings)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMGetPageRange\n")
	return rc;
}
#endif

#ifndef NO_PMGetResolution
JNIEXPORT jint JNICALL OS_NATIVE(PMGetResolution)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMResolution _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMGetResolution\n")
	if (arg1) lparg1 = getPMResolutionFields(env, arg1, &_arg1);
	rc = (jint)PMGetResolution((PMPageFormat)arg0, (PMResolution *)lparg1);
	if (arg1) setPMResolutionFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "PMGetResolution\n")
	return rc;
}
#endif

#ifndef NO_PMRelease
JNIEXPORT jint JNICALL OS_NATIVE(PMRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMRelease\n")
	rc = (jint)PMRelease((PMObject)arg0);
	NATIVE_EXIT(env, that, "PMRelease\n")
	return rc;
}
#endif

#ifndef NO_PMSessionBeginDocumentNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionBeginDocumentNoDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionBeginDocumentNoDialog\n")
	rc = (jint)PMSessionBeginDocumentNoDialog((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMPageFormat)arg2);
	NATIVE_EXIT(env, that, "PMSessionBeginDocumentNoDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionBeginPageNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionBeginPageNoDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PMRect _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionBeginPageNoDialog\n")
	if (arg2) lparg2 = getPMRectFields(env, arg2, &_arg2);
	rc = (jint)PMSessionBeginPageNoDialog((PMPrintSession)arg0, (PMPageFormat)arg1, (const PMRect *)lparg2);
	if (arg2) setPMRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "PMSessionBeginPageNoDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionCopyDestinationLocation
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionCopyDestinationLocation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionCopyDestinationLocation\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionCopyDestinationLocation((PMPrintSession)arg0, (PMPrintSettings)arg1, (CFURLRef *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionCopyDestinationLocation\n")
	return rc;
}
#endif

#ifndef NO_PMSessionCreatePrinterList
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionCreatePrinterList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionCreatePrinterList\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)PMSessionCreatePrinterList((PMPrintSession)arg0, (CFArrayRef *)lparg1, (CFIndex *)lparg2, (PMPrinter *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMSessionCreatePrinterList\n")
	return rc;
}
#endif

#ifndef NO_PMSessionDefaultPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionDefaultPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionDefaultPageFormat\n")
	rc = (jint)PMSessionDefaultPageFormat((PMPrintSession)arg0, (PMPageFormat)arg1);
	NATIVE_EXIT(env, that, "PMSessionDefaultPageFormat\n")
	return rc;
}
#endif

#ifndef NO_PMSessionDefaultPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionDefaultPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionDefaultPrintSettings\n")
	rc = (jint)PMSessionDefaultPrintSettings((PMPrintSession)arg0, (PMPrintSettings)arg1);
	NATIVE_EXIT(env, that, "PMSessionDefaultPrintSettings\n")
	return rc;
}
#endif

#ifndef NO_PMSessionEndDocumentNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionEndDocumentNoDialog)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionEndDocumentNoDialog\n")
	rc = (jint)PMSessionEndDocumentNoDialog((PMPrintSession)arg0);
	NATIVE_EXIT(env, that, "PMSessionEndDocumentNoDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionEndPageNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionEndPageNoDialog)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionEndPageNoDialog\n")
	rc = (jint)PMSessionEndPageNoDialog((PMPrintSession)arg0);
	NATIVE_EXIT(env, that, "PMSessionEndPageNoDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionError
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionError)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionError\n")
	rc = (jint)PMSessionError((PMPrintSession)arg0);
	NATIVE_EXIT(env, that, "PMSessionError\n")
	return rc;
}
#endif

#ifndef NO_PMSessionGetDestinationType
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionGetDestinationType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionGetDestinationType\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionGetDestinationType((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMDestinationType *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionGetDestinationType\n")
	return rc;
}
#endif

#ifndef NO_PMSessionGetGraphicsContext
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionGetGraphicsContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionGetGraphicsContext\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionGetGraphicsContext((PMPrintSession)arg0, (CFStringRef)arg1, (void **)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionGetGraphicsContext\n")
	return rc;
}
#endif

#ifndef NO_PMSessionPageSetupDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionPageSetupDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionPageSetupDialog\n")
	if (arg2) lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionPageSetupDialog((PMPrintSession)arg0, (PMPageFormat)arg1, (Boolean *)lparg2);
	if (arg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionPageSetupDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionPrintDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionPrintDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbooleanArray arg3)
{
	jboolean *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionPrintDialog\n")
	if (arg3) lparg3 = (*env)->GetBooleanArrayElements(env, arg3, NULL);
	rc = (jint)PMSessionPrintDialog((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMPageFormat)arg2, (Boolean *)lparg3);
	if (arg3) (*env)->ReleaseBooleanArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "PMSessionPrintDialog\n")
	return rc;
}
#endif

#ifndef NO_PMSessionSetCurrentPrinter
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetCurrentPrinter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionSetCurrentPrinter\n")
	rc = (jint)PMSessionSetCurrentPrinter((PMPrintSession)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "PMSessionSetCurrentPrinter\n")
	return rc;
}
#endif

#ifndef NO_PMSessionSetDestination
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetDestination)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionSetDestination\n")
	rc = (jint)PMSessionSetDestination((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMDestinationType)arg2, (CFStringRef)arg3, (CFURLRef)arg4);
	NATIVE_EXIT(env, that, "PMSessionSetDestination\n")
	return rc;
}
#endif

#ifndef NO_PMSessionSetDocumentFormatGeneration
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetDocumentFormatGeneration)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionSetDocumentFormatGeneration\n")
	rc = (jint)PMSessionSetDocumentFormatGeneration((PMPrintSession)arg0, (CFStringRef)arg1, (CFArrayRef)arg2, (CFTypeRef)arg3);
	NATIVE_EXIT(env, that, "PMSessionSetDocumentFormatGeneration\n")
	return rc;
}
#endif

#ifndef NO_PMSessionSetError
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetError)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionSetError\n")
	rc = (jint)PMSessionSetError((PMPrintSession)arg0, arg1);
	NATIVE_EXIT(env, that, "PMSessionSetError\n")
	return rc;
}
#endif

#ifndef NO_PMSessionUseSheets
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionUseSheets)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionUseSheets\n")
	rc = (jint)PMSessionUseSheets((PMPrintSession)arg0, (WindowRef)arg1, (PMSheetDoneUPP)arg2);
	NATIVE_EXIT(env, that, "PMSessionUseSheets\n")
	return rc;
}
#endif

#ifndef NO_PMSessionValidatePageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionValidatePageFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionValidatePageFormat\n")
	if (arg2) lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionValidatePageFormat((PMPrintSession)arg0, (PMPageFormat)arg1, (Boolean *)lparg2);
	if (arg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionValidatePageFormat\n")
	return rc;
}
#endif

#ifndef NO_PMSessionValidatePrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionValidatePrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMSessionValidatePrintSettings\n")
	if (arg2) lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL);
	rc = (jint)PMSessionValidatePrintSettings((PMPrintSession)arg0, (PMPrintSettings)arg1, (Boolean *)lparg2);
	if (arg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "PMSessionValidatePrintSettings\n")
	return rc;
}
#endif

#ifndef NO_PMSetCollate
JNIEXPORT jint JNICALL OS_NATIVE(PMSetCollate)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSetCollate\n")
	rc = (jint)PMSetCollate((PMPrintSettings)arg0, arg1);
	NATIVE_EXIT(env, that, "PMSetCollate\n")
	return rc;
}
#endif

#ifndef NO_PMSetFirstPage
JNIEXPORT jint JNICALL OS_NATIVE(PMSetFirstPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSetFirstPage\n")
	rc = (jint)PMSetFirstPage((PMPrintSettings)arg0, (UInt32)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "PMSetFirstPage\n")
	return rc;
}
#endif

#ifndef NO_PMSetJobNameCFString
JNIEXPORT jint JNICALL OS_NATIVE(PMSetJobNameCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSetJobNameCFString\n")
	rc = (jint)PMSetJobNameCFString((PMPrintSettings)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "PMSetJobNameCFString\n")
	return rc;
}
#endif

#ifndef NO_PMSetLastPage
JNIEXPORT jint JNICALL OS_NATIVE(PMSetLastPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSetLastPage\n")
	rc = (jint)PMSetLastPage((PMPrintSettings)arg0, (UInt32)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "PMSetLastPage\n")
	return rc;
}
#endif

#ifndef NO_PMSetPageRange
JNIEXPORT jint JNICALL OS_NATIVE(PMSetPageRange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PMSetPageRange\n")
	rc = (jint)PMSetPageRange((PMPrintSettings)arg0, (UInt32)arg1, (UInt32)arg2);
	NATIVE_EXIT(env, that, "PMSetPageRange\n")
	return rc;
}
#endif

#ifndef NO_PMUnflattenPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMUnflattenPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMUnflattenPageFormat\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMUnflattenPageFormat((Handle)arg0, (PMPageFormat *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMUnflattenPageFormat\n")
	return rc;
}
#endif

#ifndef NO_PMUnflattenPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMUnflattenPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PMUnflattenPrintSettings\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)PMUnflattenPrintSettings((Handle)arg0, (PMPrintSettings *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "PMUnflattenPrintSettings\n")
	return rc;
}
#endif

#ifndef NO_PaintOval
JNIEXPORT void JNICALL OS_NATIVE(PaintOval)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "PaintOval\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintOval((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PaintOval\n")
}
#endif

#ifndef NO_PaintPoly
JNIEXPORT void JNICALL OS_NATIVE(PaintPoly)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "PaintPoly\n")
	PaintPoly((PolyHandle)arg0);
	NATIVE_EXIT(env, that, "PaintPoly\n")
}
#endif

#ifndef NO_PaintRect
JNIEXPORT void JNICALL OS_NATIVE(PaintRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "PaintRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintRect((const Rect *)lparg0);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PaintRect\n")
}
#endif

#ifndef NO_PaintRoundRect
JNIEXPORT void JNICALL OS_NATIVE(PaintRoundRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "PaintRoundRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	PaintRoundRect((const Rect *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PaintRoundRect\n")
}
#endif

#ifndef NO_PenSize
JNIEXPORT void JNICALL OS_NATIVE(PenSize)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "PenSize\n")
	PenSize((short)arg0, (short)arg1);
	NATIVE_EXIT(env, that, "PenSize\n")
}
#endif

#ifndef NO_PickColor
JNIEXPORT jint JNICALL OS_NATIVE(PickColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ColorPickerInfo _arg0, *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PickColor\n")
	if (arg0) lparg0 = getColorPickerInfoFields(env, arg0, &_arg0);
	rc = (jint)PickColor((ColorPickerInfo *)lparg0);
	if (arg0) setColorPickerInfoFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PickColor\n")
	return rc;
}
#endif

#ifndef NO_PopUpMenuSelect
JNIEXPORT jint JNICALL OS_NATIVE(PopUpMenuSelect)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "PopUpMenuSelect\n")
	rc = (jint)PopUpMenuSelect((MenuRef)arg0, (short)arg1, (short)arg2, (short)arg3);
	NATIVE_EXIT(env, that, "PopUpMenuSelect\n")
	return rc;
}
#endif

#ifndef NO_PostEvent
JNIEXPORT jint JNICALL OS_NATIVE(PostEvent)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "PostEvent\n")
	rc = (jint)PostEvent((EventKind)arg0, (UInt32)arg1);
	NATIVE_EXIT(env, that, "PostEvent\n")
	return rc;
}
#endif

#ifndef NO_PostEventToQueue
JNIEXPORT jint JNICALL OS_NATIVE(PostEventToQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "PostEventToQueue\n")
	rc = (jint)PostEventToQueue((EventQueueRef)arg0, (EventRef)arg1, (EventPriority)arg2);
	NATIVE_EXIT(env, that, "PostEventToQueue\n")
	return rc;
}
#endif

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	Point _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PtInRect\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	rc = (jboolean)PtInRect(*(Point *)lparg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PtInRect\n")
	return rc;
}
#endif

#ifndef NO_PtInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRgn)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Point _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "PtInRgn\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	rc = (jboolean)PtInRgn(*(Point *)lparg0, (RgnHandle)arg1);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "PtInRgn\n")
	return rc;
}
#endif

#ifndef NO_PutScrapFlavor
JNIEXPORT jint JNICALL OS_NATIVE(PutScrapFlavor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "PutScrapFlavor\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)PutScrapFlavor((ScrapRef)arg0, (ScrapFlavorType)arg1, (ScrapFlavorFlags)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "PutScrapFlavor\n")
	return rc;
}
#endif

#ifndef NO_QDBeginCGContext
JNIEXPORT jint JNICALL OS_NATIVE(QDBeginCGContext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "QDBeginCGContext\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)QDBeginCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "QDBeginCGContext\n")
	return rc;
}
#endif

#ifndef NO_QDEndCGContext
JNIEXPORT jint JNICALL OS_NATIVE(QDEndCGContext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "QDEndCGContext\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)QDEndCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "QDEndCGContext\n")
	return rc;
}
#endif

#ifndef NO_QDFlushPortBuffer
JNIEXPORT void JNICALL OS_NATIVE(QDFlushPortBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "QDFlushPortBuffer\n")
	QDFlushPortBuffer((CGrafPtr)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "QDFlushPortBuffer\n")
}
#endif

#ifndef NO_QDGlobalToLocalPoint
JNIEXPORT void JNICALL OS_NATIVE(QDGlobalToLocalPoint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "QDGlobalToLocalPoint\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	QDGlobalToLocalPoint((CGrafPtr)arg0, (Point *)lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "QDGlobalToLocalPoint\n")
}
#endif

#ifndef NO_QDLocalToGlobalPoint
JNIEXPORT void JNICALL OS_NATIVE(QDLocalToGlobalPoint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "QDLocalToGlobalPoint\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	QDLocalToGlobalPoint((CGrafPtr)arg0, (Point *)lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "QDLocalToGlobalPoint\n")
}
#endif

#ifndef NO_QDSetDirtyRegion
JNIEXPORT jint JNICALL OS_NATIVE(QDSetDirtyRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "QDSetDirtyRegion\n")
	rc = (jint)QDSetDirtyRegion((CGrafPtr)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "QDSetDirtyRegion\n")
	return rc;
}
#endif

#ifndef NO_QDSetPatternOrigin
JNIEXPORT void JNICALL OS_NATIVE(QDSetPatternOrigin)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "QDSetPatternOrigin\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	QDSetPatternOrigin(*(Point *)lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "QDSetPatternOrigin\n")
}
#endif

#ifndef NO_QDSwapTextFlags
JNIEXPORT jint JNICALL OS_NATIVE(QDSwapTextFlags)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "QDSwapTextFlags\n")
	rc = (jint)QDSwapTextFlags((UInt32)arg0);
	NATIVE_EXIT(env, that, "QDSwapTextFlags\n")
	return rc;
}
#endif

#ifndef NO_RGBBackColor
JNIEXPORT void JNICALL OS_NATIVE(RGBBackColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "RGBBackColor\n")
	if (arg0) lparg0 = getRGBColorFields(env, arg0, &_arg0);
	RGBBackColor((const RGBColor *)lparg0);
	if (arg0) setRGBColorFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "RGBBackColor\n")
}
#endif

#ifndef NO_RGBForeColor
JNIEXPORT void JNICALL OS_NATIVE(RGBForeColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "RGBForeColor\n")
	if (arg0) lparg0 = getRGBColorFields(env, arg0, &_arg0);
	RGBForeColor((const RGBColor *)lparg0);
	if (arg0) setRGBColorFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "RGBForeColor\n")
}
#endif

#ifndef NO_ReceiveNextEvent
JNIEXPORT jint JNICALL OS_NATIVE(ReceiveNextEvent)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jdouble arg2, jboolean arg3, jintArray arg4)
{
	jint *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ReceiveNextEvent\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)ReceiveNextEvent((UInt32)arg0, (const EventTypeSpec *)lparg1, (EventTimeout)arg2, (Boolean)arg3, (EventRef *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "ReceiveNextEvent\n")
	return rc;
}
#endif

#ifndef NO_RectInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRgn)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "RectInRgn\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	rc = (jboolean)RectInRgn((const Rect *)lparg0, (RgnHandle)arg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "RectInRgn\n")
	return rc;
}
#endif

#ifndef NO_RectRgn
JNIEXPORT void JNICALL OS_NATIVE(RectRgn)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "RectRgn\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	RectRgn((RgnHandle)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "RectRgn\n")
}
#endif

#ifndef NO_RegisterAppearanceClient
JNIEXPORT jint JNICALL OS_NATIVE(RegisterAppearanceClient)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "RegisterAppearanceClient\n")
	rc = (jint)RegisterAppearanceClient();
	NATIVE_EXIT(env, that, "RegisterAppearanceClient\n")
	return rc;
}
#endif

#ifndef NO_ReleaseEvent
JNIEXPORT void JNICALL OS_NATIVE(ReleaseEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "ReleaseEvent\n")
	ReleaseEvent((EventRef)arg0);
	NATIVE_EXIT(env, that, "ReleaseEvent\n")
}
#endif

#ifndef NO_ReleaseMenu
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ReleaseMenu\n")
	rc = (jint)ReleaseMenu((MenuRef)arg0);
	NATIVE_EXIT(env, that, "ReleaseMenu\n")
	return rc;
}
#endif

#ifndef NO_ReleaseWindow
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ReleaseWindow\n")
	rc = (jint)ReleaseWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "ReleaseWindow\n")
	return rc;
}
#endif

#ifndef NO_ReleaseWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseWindowGroup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ReleaseWindowGroup\n")
	rc = (jint)ReleaseWindowGroup((WindowGroupRef)arg0);
	NATIVE_EXIT(env, that, "ReleaseWindowGroup\n")
	return rc;
}
#endif

#ifndef NO_RemoveControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(RemoveControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveControlProperty\n")
	rc = (jint)RemoveControlProperty((ControlRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "RemoveControlProperty\n")
	return rc;
}
#endif

#ifndef NO_RemoveDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(RemoveDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RemoveDataBrowserItems\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)RemoveDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "RemoveDataBrowserItems\n")
	return rc;
}
#endif

#ifndef NO_RemoveDataBrowserTableViewColumn
JNIEXPORT jint JNICALL OS_NATIVE(RemoveDataBrowserTableViewColumn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveDataBrowserTableViewColumn\n")
	rc = (jint)RemoveDataBrowserTableViewColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1);
	NATIVE_EXIT(env, that, "RemoveDataBrowserTableViewColumn\n")
	return rc;
}
#endif

#ifndef NO_RemoveEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveEventHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveEventHandler\n")
	rc = (jint)RemoveEventHandler((EventHandlerRef)arg0);
	NATIVE_EXIT(env, that, "RemoveEventHandler\n")
	return rc;
}
#endif

#ifndef NO_RemoveEventLoopTimer
JNIEXPORT jint JNICALL OS_NATIVE(RemoveEventLoopTimer)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveEventLoopTimer\n")
	rc = (jint)RemoveEventLoopTimer((EventLoopTimerRef)arg0);
	NATIVE_EXIT(env, that, "RemoveEventLoopTimer\n")
	return rc;
}
#endif

#ifndef NO_RemoveReceiveHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveReceiveHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveReceiveHandler\n")
	rc = (jint)RemoveReceiveHandler((DragReceiveHandlerUPP)arg0, (WindowRef)arg1);
	NATIVE_EXIT(env, that, "RemoveReceiveHandler\n")
	return rc;
}
#endif

#ifndef NO_RemoveTrackingHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveTrackingHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "RemoveTrackingHandler\n")
	rc = (jint)RemoveTrackingHandler((DragTrackingHandlerUPP)arg0, (WindowRef)arg1);
	NATIVE_EXIT(env, that, "RemoveTrackingHandler\n")
	return rc;
}
#endif

#ifndef NO_RepositionWindow
JNIEXPORT jint JNICALL OS_NATIVE(RepositionWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "RepositionWindow\n")
	rc = (jint)RepositionWindow((WindowRef)arg0, (WindowRef)arg1, arg2);
	NATIVE_EXIT(env, that, "RepositionWindow\n")
	return rc;
}
#endif

#ifndef NO_ReshapeCustomWindow
JNIEXPORT jint JNICALL OS_NATIVE(ReshapeCustomWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ReshapeCustomWindow\n")
	rc = (jint)ReshapeCustomWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "ReshapeCustomWindow\n")
	return rc;
}
#endif

#ifndef NO_RetainEvent
JNIEXPORT jint JNICALL OS_NATIVE(RetainEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RetainEvent\n")
	rc = (jint)RetainEvent((EventRef)arg0);
	NATIVE_EXIT(env, that, "RetainEvent\n")
	return rc;
}
#endif

#ifndef NO_RetainMenu
JNIEXPORT jint JNICALL OS_NATIVE(RetainMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RetainMenu\n")
	rc = (jint)RetainMenu((MenuRef)arg0);
	NATIVE_EXIT(env, that, "RetainMenu\n")
	return rc;
}
#endif

#ifndef NO_RetainWindow
JNIEXPORT jint JNICALL OS_NATIVE(RetainWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "RetainWindow\n")
	rc = (jint)RetainWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "RetainWindow\n")
	return rc;
}
#endif

#ifndef NO_RevealDataBrowserItem
JNIEXPORT jint JNICALL OS_NATIVE(RevealDataBrowserItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyte arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "RevealDataBrowserItem\n")
	rc = (jint)RevealDataBrowserItem((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserRevealOptions)arg3);
	NATIVE_EXIT(env, that, "RevealDataBrowserItem\n")
	return rc;
}
#endif

#ifndef NO_RunStandardAlert
JNIEXPORT jint JNICALL OS_NATIVE(RunStandardAlert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "RunStandardAlert\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	rc = (jint)RunStandardAlert((DialogRef)arg0, (ModalFilterUPP)arg1, (DialogItemIndex *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "RunStandardAlert\n")
	return rc;
}
#endif

#ifndef NO_ScrollRect
JNIEXPORT void JNICALL OS_NATIVE(ScrollRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "ScrollRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	ScrollRect((const Rect *)lparg0, (short)arg1, (short)arg2, (RgnHandle)arg3);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "ScrollRect\n")
}
#endif

#ifndef NO_SectRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SectRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	Rect _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "SectRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	rc = (jboolean)SectRect(lparg0, lparg1, lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SectRect\n")
	return rc;
}
#endif

#ifndef NO_SectRgn
JNIEXPORT void JNICALL OS_NATIVE(SectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "SectRgn\n")
	SectRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	NATIVE_EXIT(env, that, "SectRgn\n")
}
#endif

#ifndef NO_SelectWindow
JNIEXPORT void JNICALL OS_NATIVE(SelectWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "SelectWindow\n")
	SelectWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "SelectWindow\n")
}
#endif

#ifndef NO_SendBehind
JNIEXPORT void JNICALL OS_NATIVE(SendBehind)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SendBehind\n")
	SendBehind((WindowRef)arg0, (WindowRef)arg1);
	NATIVE_EXIT(env, that, "SendBehind\n")
}
#endif

#ifndef NO_SendEventToEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(SendEventToEventTarget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SendEventToEventTarget\n")
	rc = (jint)SendEventToEventTarget((EventRef)arg0, (EventTargetRef)arg1);
	NATIVE_EXIT(env, that, "SendEventToEventTarget\n")
	return rc;
}
#endif

#ifndef NO_SetBevelButtonContentInfo
JNIEXPORT jint JNICALL OS_NATIVE(SetBevelButtonContentInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlButtonContentInfo _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetBevelButtonContentInfo\n")
	if (arg1) lparg1 = getControlButtonContentInfoFields(env, arg1, &_arg1);
	rc = (jint)SetBevelButtonContentInfo((ControlRef)arg0, (ControlButtonContentInfoPtr)lparg1);
	if (arg1) setControlButtonContentInfoFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetBevelButtonContentInfo\n")
	return rc;
}
#endif

#ifndef NO_SetClip
JNIEXPORT void JNICALL OS_NATIVE(SetClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "SetClip\n")
	SetClip((RgnHandle)arg0);
	NATIVE_EXIT(env, that, "SetClip\n")
}
#endif

#ifndef NO_SetControl32BitMaximum
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitMaximum)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControl32BitMaximum\n")
	SetControl32BitMaximum((ControlRef)arg0, (SInt32)arg1);
	NATIVE_EXIT(env, that, "SetControl32BitMaximum\n")
}
#endif

#ifndef NO_SetControl32BitMinimum
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitMinimum)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControl32BitMinimum\n")
	SetControl32BitMinimum((ControlRef)arg0, (SInt32)arg1);
	NATIVE_EXIT(env, that, "SetControl32BitMinimum\n")
}
#endif

#ifndef NO_SetControl32BitValue
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControl32BitValue\n")
	SetControl32BitValue((ControlRef)arg0, (SInt32)arg1);
	NATIVE_EXIT(env, that, "SetControl32BitValue\n")
}
#endif

#ifndef NO_SetControlAction
JNIEXPORT void JNICALL OS_NATIVE(SetControlAction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControlAction\n")
	SetControlAction((ControlRef)arg0, (ControlActionUPP)arg1);
	NATIVE_EXIT(env, that, "SetControlAction\n")
}
#endif

#ifndef NO_SetControlBounds
JNIEXPORT void JNICALL OS_NATIVE(SetControlBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "SetControlBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	SetControlBounds((ControlRef)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetControlBounds\n")
}
#endif

#ifndef NO_SetControlColorProc
JNIEXPORT jint JNICALL OS_NATIVE(SetControlColorProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetControlColorProc\n")
	rc = (jint)SetControlColorProc((ControlRef)arg0, (ControlColorUPP)arg1);
	NATIVE_EXIT(env, that, "SetControlColorProc\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIII
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIIII\n")
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)arg4);
	NATIVE_EXIT(env, that, "SetControlData__IIIII\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlButtonContentInfo _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2\n")
	if (arg4) lparg4 = getControlButtonContentInfoFields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setControlButtonContentInfoFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlTabInfoRecV1 _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2\n")
	if (arg4) lparg4 = getControlTabInfoRecV1Fields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setControlTabInfoRecV1Fields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2\n")
	if (arg4) lparg4 = getRectFields(env, arg4, &_arg4);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) setRectFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3B
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIII_3B\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetControlData__IIII_3B\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3I
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIII_3I\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetControlData__IIII_3I\n")
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3S
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlData__IIII_3S\n")
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetControlData__IIII_3S\n")
	return rc;
}
#endif

#ifndef NO_SetControlFontStyle
JNIEXPORT jint JNICALL OS_NATIVE(SetControlFontStyle)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlFontStyleRec _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlFontStyle\n")
	if (arg1) lparg1 = getControlFontStyleRecFields(env, arg1, &_arg1);
	rc = (jint)SetControlFontStyle((ControlRef)arg0, (const ControlFontStyleRec *)lparg1);
	if (arg1) setControlFontStyleRecFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetControlFontStyle\n")
	return rc;
}
#endif

#ifndef NO_SetControlPopupMenuHandle
JNIEXPORT void JNICALL OS_NATIVE(SetControlPopupMenuHandle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControlPopupMenuHandle\n")
	SetControlPopupMenuHandle((ControlRef)arg0, (MenuRef)arg1);
	NATIVE_EXIT(env, that, "SetControlPopupMenuHandle\n")
}
#endif

#ifndef NO_SetControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(SetControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetControlProperty\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)SetControlProperty((ControlRef)arg0, arg1, arg2, arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetControlProperty\n")
	return rc;
}
#endif

#ifndef NO_SetControlReference
JNIEXPORT void JNICALL OS_NATIVE(SetControlReference)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControlReference\n")
	SetControlReference((ControlRef)arg0, (SInt32)arg1);
	NATIVE_EXIT(env, that, "SetControlReference\n")
}
#endif

#ifndef NO_SetControlTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetControlTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetControlTitleWithCFString\n")
	rc = (jint)SetControlTitleWithCFString((ControlRef)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "SetControlTitleWithCFString\n")
	return rc;
}
#endif

#ifndef NO_SetControlViewSize
JNIEXPORT void JNICALL OS_NATIVE(SetControlViewSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetControlViewSize\n")
	SetControlViewSize((ControlRef)arg0, (SInt32)arg1);
	NATIVE_EXIT(env, that, "SetControlViewSize\n")
}
#endif

#ifndef NO_SetControlVisibility
JNIEXPORT jint JNICALL OS_NATIVE(SetControlVisibility)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetControlVisibility\n")
	rc = (jint)SetControlVisibility((ControlRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetControlVisibility\n")
	return rc;
}
#endif

#ifndef NO_SetCursor
JNIEXPORT void JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "SetCursor\n")
	SetCursor((const Cursor *)arg0);
	NATIVE_EXIT(env, that, "SetCursor\n")
}
#endif

#ifndef NO_SetDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1={0}, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserCallbacks\n")
	if (arg1) lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1);
	rc = (jint)SetDataBrowserCallbacks((ControlRef)arg0, (const DataBrowserCallbacks *)lparg1);
	if (arg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetDataBrowserCallbacks\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserCustomCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCustomCallbacks _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserCustomCallbacks\n")
	if (arg1) lparg1 = getDataBrowserCustomCallbacksFields(env, arg1, &_arg1);
	rc = (jint)SetDataBrowserCustomCallbacks((ControlRef)arg0, lparg1);
	if (arg1) setDataBrowserCustomCallbacksFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetDataBrowserCustomCallbacks\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserHasScrollBars
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserHasScrollBars)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserHasScrollBars\n")
	rc = (jint)SetDataBrowserHasScrollBars((ControlRef)arg0, (Boolean)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserHasScrollBars\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataBooleanValue
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataBooleanValue)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserItemDataBooleanValue\n")
	rc = (jint)SetDataBrowserItemDataBooleanValue((DataBrowserItemDataRef)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserItemDataBooleanValue\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataButtonValue)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserItemDataButtonValue\n")
	rc = (jint)SetDataBrowserItemDataButtonValue((DataBrowserItemDataRef)arg0, (ThemeButtonValue)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserItemDataButtonValue\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataIcon
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserItemDataIcon\n")
	rc = (jint)SetDataBrowserItemDataIcon((DataBrowserItemDataRef)arg0, (IconRef)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserItemDataIcon\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataItemID
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataItemID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserItemDataItemID\n")
	rc = (jint)SetDataBrowserItemDataItemID((DataBrowserItemDataRef)arg0, (DataBrowserItemID)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserItemDataItemID\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataText
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserItemDataText\n")
	rc = (jint)SetDataBrowserItemDataText((DataBrowserItemDataRef)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserItemDataText\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewDisclosureColumn
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewDisclosureColumn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserListViewDisclosureColumn\n")
	rc = (jint)SetDataBrowserListViewDisclosureColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserListViewDisclosureColumn\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewHeaderBtnHeight)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserListViewHeaderBtnHeight\n")
	rc = (jint)SetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, (UInt16)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserListViewHeaderBtnHeight\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewHeaderDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserListViewHeaderDesc\n")
	if (arg2) lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2);
	rc = (jint)SetDataBrowserListViewHeaderDesc((ControlRef)arg0, arg1, lparg2);
	if (arg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SetDataBrowserListViewHeaderDesc\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserScrollPosition
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserScrollPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserScrollPosition\n")
	rc = (jint)SetDataBrowserScrollPosition((ControlRef)arg0, (UInt32)arg1, (UInt32)arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserScrollPosition\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSelectedItems
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSelectedItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserSelectedItems\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)SetDataBrowserSelectedItems((ControlRef)arg0, (UInt32)arg1, (const DataBrowserItemID *)lparg2, (DataBrowserSetOption)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "SetDataBrowserSelectedItems\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSelectionFlags
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSelectionFlags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserSelectionFlags\n")
	rc = (jint)SetDataBrowserSelectionFlags((ControlRef)arg0, (DataBrowserSelectionFlags)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserSelectionFlags\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewColumnPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserTableViewColumnPosition\n")
	rc = (jint)SetDataBrowserTableViewColumnPosition((ControlRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserTableViewColumnPosition\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewHiliteStyle
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewHiliteStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserTableViewHiliteStyle\n")
	rc = (jint)SetDataBrowserTableViewHiliteStyle((ControlRef)arg0, arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserTableViewHiliteStyle\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewItemRow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserTableViewItemRow\n")
	rc = (jint)SetDataBrowserTableViewItemRow((ControlRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserTableViewItemRow\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewNamedColumnWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserTableViewNamedColumnWidth\n")
	rc = (jint)SetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16)arg2);
	NATIVE_EXIT(env, that, "SetDataBrowserTableViewNamedColumnWidth\n")
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTarget
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTarget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDataBrowserTarget\n")
	rc = (jint)SetDataBrowserTarget((ControlRef)arg0, (DataBrowserItemID)arg1);
	NATIVE_EXIT(env, that, "SetDataBrowserTarget\n")
	return rc;
}
#endif

#ifndef NO_SetDragAllowableActions
JNIEXPORT jint JNICALL OS_NATIVE(SetDragAllowableActions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDragAllowableActions\n")
	rc = (jint)SetDragAllowableActions((DragRef)arg0, (DragActions)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetDragAllowableActions\n")
	return rc;
}
#endif

#ifndef NO_SetDragDropAction
JNIEXPORT jint JNICALL OS_NATIVE(SetDragDropAction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDragDropAction\n")
	rc = (jint)SetDragDropAction((DragRef)arg0, (DragActions)arg1);
	NATIVE_EXIT(env, that, "SetDragDropAction\n")
	return rc;
}
#endif

#ifndef NO_SetDragInputProc
JNIEXPORT jint JNICALL OS_NATIVE(SetDragInputProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetDragInputProc\n")
	rc = (jint)SetDragInputProc((DragRef)arg0, (DragInputUPP)arg1, (void *)arg2);
	NATIVE_EXIT(env, that, "SetDragInputProc\n")
	return rc;
}
#endif

#ifndef NO_SetEventLoopTimerNextFireTime
JNIEXPORT jint JNICALL OS_NATIVE(SetEventLoopTimerNextFireTime)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetEventLoopTimerNextFireTime\n")
	rc = (jint)SetEventLoopTimerNextFireTime((EventLoopTimerRef)arg0, (EventTimerInterval)arg1);
	NATIVE_EXIT(env, that, "SetEventLoopTimerNextFireTime\n")
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3C
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetEventParameter__IIII_3C\n")
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetEventParameter__IIII_3C\n")
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3S
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetEventParameter__IIII_3S\n")
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "SetEventParameter__IIII_3S\n")
	return rc;
}
#endif

#ifndef NO_SetFontInfoForSelection
JNIEXPORT jint JNICALL OS_NATIVE(SetFontInfoForSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetFontInfoForSelection\n")
	rc = (jint)SetFontInfoForSelection((OSType)arg0, (UInt32)arg1, (void *)arg2, (HIObjectRef)arg3);
	NATIVE_EXIT(env, that, "SetFontInfoForSelection\n")
	return rc;
}
#endif

#ifndef NO_SetFrontProcess
JNIEXPORT jint JNICALL OS_NATIVE(SetFrontProcess)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "SetFrontProcess\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)SetFrontProcess((const ProcessSerialNumber *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "SetFrontProcess\n")
	return rc;
}
#endif

#ifndef NO_SetGWorld
JNIEXPORT void JNICALL OS_NATIVE(SetGWorld)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetGWorld\n")
	SetGWorld((CGrafPtr)arg0, (GDHandle)arg1);
	NATIVE_EXIT(env, that, "SetGWorld\n")
}
#endif

#ifndef NO_SetItemMark
JNIEXPORT void JNICALL OS_NATIVE(SetItemMark)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "SetItemMark\n")
	SetItemMark((MenuRef)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetItemMark\n")
}
#endif

#ifndef NO_SetKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(SetKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetKeyboardFocus\n")
	rc = (jint)SetKeyboardFocus((WindowRef)arg0, (ControlRef)arg1, (ControlFocusPart)arg2);
	NATIVE_EXIT(env, that, "SetKeyboardFocus\n")
	return rc;
}
#endif

#ifndef NO_SetMenuCommandMark
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuCommandMark)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jchar arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuCommandMark\n")
	rc = (jint)SetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar)arg2);
	NATIVE_EXIT(env, that, "SetMenuCommandMark\n")
	return rc;
}
#endif

#ifndef NO_SetMenuFont
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuFont)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuFont\n")
	rc = (jint)SetMenuFont((MenuRef)arg0, (SInt16)arg1, (UInt16)arg2);
	NATIVE_EXIT(env, that, "SetMenuFont\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemCommandKey
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemCommandKey)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2, jchar arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemCommandKey\n")
	rc = (jint)SetMenuItemCommandKey((MenuRef)arg0, (MenuItemIndex)arg1, (Boolean)arg2, (UInt16)arg3);
	NATIVE_EXIT(env, that, "SetMenuItemCommandKey\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemHierarchicalMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemHierarchicalMenu\n")
	rc = (jint)SetMenuItemHierarchicalMenu((MenuRef)arg0, (MenuItemIndex)arg1, (MenuRef)arg2);
	NATIVE_EXIT(env, that, "SetMenuItemHierarchicalMenu\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemIconHandle
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemIconHandle)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemIconHandle\n")
	rc = (jint)SetMenuItemIconHandle((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2, (Handle)arg3);
	NATIVE_EXIT(env, that, "SetMenuItemIconHandle\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemKeyGlyph
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemKeyGlyph)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemKeyGlyph\n")
	rc = (jint)SetMenuItemKeyGlyph((MenuRef)arg0, (SInt16)arg1, (SInt16)arg2);
	NATIVE_EXIT(env, that, "SetMenuItemKeyGlyph\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemModifiers
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemModifiers)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemModifiers\n")
	rc = (jint)SetMenuItemModifiers((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2);
	NATIVE_EXIT(env, that, "SetMenuItemModifiers\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemRefCon
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemRefCon)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemRefCon\n")
	rc = (jint)SetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32)arg2);
	NATIVE_EXIT(env, that, "SetMenuItemRefCon\n")
	return rc;
}
#endif

#ifndef NO_SetMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuItemTextWithCFString\n")
	rc = (jint)SetMenuItemTextWithCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef)arg2);
	NATIVE_EXIT(env, that, "SetMenuItemTextWithCFString\n")
	return rc;
}
#endif

#ifndef NO_SetMenuTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetMenuTitleWithCFString\n")
	rc = (jint)SetMenuTitleWithCFString((MenuRef)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "SetMenuTitleWithCFString\n")
	return rc;
}
#endif

#ifndef NO_SetOrigin
JNIEXPORT void JNICALL OS_NATIVE(SetOrigin)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "SetOrigin\n")
	SetOrigin((short)arg0, (short)arg1);
	NATIVE_EXIT(env, that, "SetOrigin\n")
}
#endif

#ifndef NO_SetPort
JNIEXPORT void JNICALL OS_NATIVE(SetPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "SetPort\n")
	SetPort((GrafPtr)arg0);
	NATIVE_EXIT(env, that, "SetPort\n")
}
#endif

#ifndef NO_SetPortBounds
JNIEXPORT void JNICALL OS_NATIVE(SetPortBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "SetPortBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	SetPortBounds((CGrafPtr)arg0, (const Rect *)lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "SetPortBounds\n")
}
#endif

#ifndef NO_SetPortWindowPort
JNIEXPORT void JNICALL OS_NATIVE(SetPortWindowPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "SetPortWindowPort\n")
	SetPortWindowPort((WindowRef)arg0);
	NATIVE_EXIT(env, that, "SetPortWindowPort\n")
}
#endif

#ifndef NO_SetPt
JNIEXPORT void JNICALL OS_NATIVE(SetPt)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Point _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "SetPt\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	SetPt((Point *)lparg0, (short)arg1, (short)arg2);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SetPt\n")
}
#endif

#ifndef NO_SetRect
JNIEXPORT void JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "SetRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	SetRect((Rect *)lparg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "SetRect\n")
}
#endif

#ifndef NO_SetRectRgn
JNIEXPORT void JNICALL OS_NATIVE(SetRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	NATIVE_ENTER(env, that, "SetRectRgn\n")
	SetRectRgn((RgnHandle)arg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
	NATIVE_EXIT(env, that, "SetRectRgn\n")
}
#endif

#ifndef NO_SetRootMenu
JNIEXPORT jint JNICALL OS_NATIVE(SetRootMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetRootMenu\n")
	rc = (jint)SetRootMenu((MenuRef)arg0);
	NATIVE_EXIT(env, that, "SetRootMenu\n")
	return rc;
}
#endif

#ifndef NO_SetThemeBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeBackground)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetThemeBackground\n")
	rc = (jint)SetThemeBackground((ThemeBrush)arg0, (SInt16)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetThemeBackground\n")
	return rc;
}
#endif

#ifndef NO_SetThemeCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetThemeCursor\n")
	rc = (jint)SetThemeCursor((ThemeCursor)arg0);
	NATIVE_EXIT(env, that, "SetThemeCursor\n")
	return rc;
}
#endif

#ifndef NO_SetThemeDrawingState
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeDrawingState)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetThemeDrawingState\n")
	rc = (jint)SetThemeDrawingState((ThemeDrawingState)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "SetThemeDrawingState\n")
	return rc;
}
#endif

#ifndef NO_SetThemeTextColor
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeTextColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetThemeTextColor\n")
	rc = (jint)SetThemeTextColor(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "SetThemeTextColor\n")
	return rc;
}
#endif

#ifndef NO_SetThemeWindowBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeWindowBackground)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetThemeWindowBackground\n")
	rc = (jint)SetThemeWindowBackground((WindowRef)arg0, (ThemeBrush)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetThemeWindowBackground\n")
	return rc;
}
#endif

#ifndef NO_SetUpControlBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetUpControlBackground)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetUpControlBackground\n")
	rc = (jint)SetUpControlBackground((ControlRef)arg0, (SInt16)arg1, (Boolean)arg2);
	NATIVE_EXIT(env, that, "SetUpControlBackground\n")
	return rc;
}
#endif

#ifndef NO_SetWRefCon
JNIEXPORT void JNICALL OS_NATIVE(SetWRefCon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "SetWRefCon\n")
	SetWRefCon((WindowRef)arg0, (long)arg1);
	NATIVE_EXIT(env, that, "SetWRefCon\n")
}
#endif

#ifndef NO_SetWindowActivationScope
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowActivationScope)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowActivationScope\n")
	rc = (jint)SetWindowActivationScope((WindowRef)arg0, (WindowActivationScope)arg1);
	NATIVE_EXIT(env, that, "SetWindowActivationScope\n")
	return rc;
}
#endif

#ifndef NO_SetWindowBounds
JNIEXPORT void JNICALL OS_NATIVE(SetWindowBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "SetWindowBounds\n")
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	SetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "SetWindowBounds\n")
}
#endif

#ifndef NO_SetWindowDefaultButton
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowDefaultButton)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowDefaultButton\n")
	rc = (jint)SetWindowDefaultButton((WindowRef)arg0, (ControlRef)arg1);
	NATIVE_EXIT(env, that, "SetWindowDefaultButton\n")
	return rc;
}
#endif

#ifndef NO_SetWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroup)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowGroup\n")
	rc = (jint)SetWindowGroup((WindowRef)arg0, (WindowGroupRef)arg1);
	NATIVE_EXIT(env, that, "SetWindowGroup\n")
	return rc;
}
#endif

#ifndef NO_SetWindowGroupOwner
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroupOwner)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowGroupOwner\n")
	rc = (jint)SetWindowGroupOwner((WindowGroupRef)arg0, (WindowRef)arg1);
	NATIVE_EXIT(env, that, "SetWindowGroupOwner\n")
	return rc;
}
#endif

#ifndef NO_SetWindowGroupParent
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroupParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowGroupParent\n")
	rc = (jint)SetWindowGroupParent((WindowGroupRef)arg0, (WindowGroupRef)arg1);
	NATIVE_EXIT(env, that, "SetWindowGroupParent\n")
	return rc;
}
#endif

#ifndef NO_SetWindowModality
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowModality)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowModality\n")
	rc = (jint)SetWindowModality((WindowRef)arg0, (WindowModality)arg1, (WindowRef)arg2);
	NATIVE_EXIT(env, that, "SetWindowModality\n")
	return rc;
}
#endif

#ifndef NO_SetWindowTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SetWindowTitleWithCFString\n")
	rc = (jint)SetWindowTitleWithCFString((WindowRef)arg0, (CFStringRef)arg1);
	NATIVE_EXIT(env, that, "SetWindowTitleWithCFString\n")
	return rc;
}
#endif

#ifndef NO_ShowWindow
JNIEXPORT void JNICALL OS_NATIVE(ShowWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "ShowWindow\n")
	ShowWindow((WindowRef)arg0);
	NATIVE_EXIT(env, that, "ShowWindow\n")
}
#endif

#ifndef NO_SizeControl
JNIEXPORT void JNICALL OS_NATIVE(SizeControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "SizeControl\n")
	SizeControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
	NATIVE_EXIT(env, that, "SizeControl\n")
}
#endif

#ifndef NO_SizeWindow
JNIEXPORT void JNICALL OS_NATIVE(SizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	NATIVE_ENTER(env, that, "SizeWindow\n")
	SizeWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
	NATIVE_EXIT(env, that, "SizeWindow\n")
}
#endif

#ifndef NO_StillDown
JNIEXPORT jboolean JNICALL OS_NATIVE(StillDown)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "StillDown\n")
	rc = (jboolean)StillDown();
	NATIVE_EXIT(env, that, "StillDown\n")
	return rc;
}
#endif

#ifndef NO_SyncCGContextOriginWithPort
JNIEXPORT jint JNICALL OS_NATIVE(SyncCGContextOriginWithPort)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "SyncCGContextOriginWithPort\n")
	rc = (jint)SyncCGContextOriginWithPort((CGContextRef)arg0, (CGrafPtr)arg1);
	NATIVE_EXIT(env, that, "SyncCGContextOriginWithPort\n")
	return rc;
}
#endif

#ifndef NO_SysBeep
JNIEXPORT void JNICALL OS_NATIVE(SysBeep)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "SysBeep\n")
	SysBeep((short)arg0);
	NATIVE_EXIT(env, that, "SysBeep\n")
}
#endif

#ifndef NO_TXNActivate
JNIEXPORT jint JNICALL OS_NATIVE(TXNActivate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNActivate\n")
	rc = (jint)TXNActivate((TXNObject)arg0, (TXNFrameID)arg1, (TXNScrollBarState)arg2);
	NATIVE_EXIT(env, that, "TXNActivate\n")
	return rc;
}
#endif

#ifndef NO_TXNAdjustCursor
JNIEXPORT void JNICALL OS_NATIVE(TXNAdjustCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "TXNAdjustCursor\n")
	TXNAdjustCursor((TXNObject)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "TXNAdjustCursor\n")
}
#endif

#ifndef NO_TXNClick
JNIEXPORT void JNICALL OS_NATIVE(TXNClick)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	EventRecord _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "TXNClick\n")
	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	TXNClick((TXNObject)arg0, (const EventRecord *)lparg1);
	if (arg1) setEventRecordFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNClick\n")
}
#endif

#ifndef NO_TXNCopy
JNIEXPORT jint JNICALL OS_NATIVE(TXNCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNCopy\n")
	rc = (jint)TXNCopy((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNCopy\n")
	return rc;
}
#endif

#ifndef NO_TXNCut
JNIEXPORT jint JNICALL OS_NATIVE(TXNCut)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNCut\n")
	rc = (jint)TXNCut((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNCut\n")
	return rc;
}
#endif

#ifndef NO_TXNDataSize
JNIEXPORT jint JNICALL OS_NATIVE(TXNDataSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNDataSize\n")
	rc = (jint)TXNDataSize((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNDataSize\n")
	return rc;
}
#endif

#ifndef NO_TXNDeleteObject
JNIEXPORT void JNICALL OS_NATIVE(TXNDeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "TXNDeleteObject\n")
	TXNDeleteObject((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNDeleteObject\n")
}
#endif

#ifndef NO_TXNDraw
JNIEXPORT void JNICALL OS_NATIVE(TXNDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "TXNDraw\n")
	TXNDraw((TXNObject)arg0, (GWorldPtr)arg1);
	NATIVE_EXIT(env, that, "TXNDraw\n")
}
#endif

#ifndef NO_TXNEchoMode
JNIEXPORT jint JNICALL OS_NATIVE(TXNEchoMode)
	(JNIEnv *env, jclass that, jint arg0, jchar arg1, jint arg2, jboolean arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNEchoMode\n")
	rc = (jint)TXNEchoMode((TXNObject)arg0, (UniChar)arg1, (TextEncoding)arg2, (Boolean)arg3);
	NATIVE_EXIT(env, that, "TXNEchoMode\n")
	return rc;
}
#endif

#ifndef NO_TXNFocus
JNIEXPORT void JNICALL OS_NATIVE(TXNFocus)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "TXNFocus\n")
	TXNFocus((TXNObject)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "TXNFocus\n")
}
#endif

#ifndef NO_TXNGetData
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNGetData\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetData((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2, (Handle *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "TXNGetData\n")
	return rc;
}
#endif

#ifndef NO_TXNGetLineCount
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetLineCount)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNGetLineCount\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)TXNGetLineCount((TXNObject)arg0, (ItemCount *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "TXNGetLineCount\n")
	return rc;
}
#endif

#ifndef NO_TXNGetLineMetrics
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetLineMetrics)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNGetLineMetrics\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetLineMetrics((TXNObject)arg0, (UInt32)arg1, (Fixed *)lparg2, (Fixed *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "TXNGetLineMetrics\n")
	return rc;
}
#endif

#ifndef NO_TXNGetRectBounds
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetRectBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jobject arg3)
{
	Rect _arg1, *lparg1=NULL;
	TXNLongRect _arg2, *lparg2=NULL;
	TXNLongRect _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNGetRectBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getTXNLongRectFields(env, arg2, &_arg2);
	if (arg3) lparg3 = getTXNLongRectFields(env, arg3, &_arg3);
	rc = (jint)TXNGetRectBounds((TXNObject)arg0, (Rect *)lparg1, (TXNLongRect *)lparg2, (TXNLongRect *)lparg3);
	if (arg3) setTXNLongRectFields(env, arg3, lparg3);
	if (arg2) setTXNLongRectFields(env, arg2, lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNGetRectBounds\n")
	return rc;
}
#endif

#ifndef NO_TXNGetSelection
JNIEXPORT void JNICALL OS_NATIVE(TXNGetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	NATIVE_ENTER(env, that, "TXNGetSelection\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	TXNGetSelection((TXNObject)arg0, (TXNOffset *)lparg1, (TXNOffset *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "TXNGetSelection\n")
}
#endif

#ifndef NO_TXNGetTXNObjectControls
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetTXNObjectControls)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNGetTXNObjectControls\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)TXNGetTXNObjectControls((TXNObject)arg0, (ItemCount)arg1, (const TXNControlTag *)lparg2, (TXNControlData *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "TXNGetTXNObjectControls\n")
	return rc;
}
#endif

#ifndef NO_TXNGetViewRect
JNIEXPORT void JNICALL OS_NATIVE(TXNGetViewRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "TXNGetViewRect\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	TXNGetViewRect((TXNObject)arg0, lparg1);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNGetViewRect\n")
}
#endif

#ifndef NO_TXNInitTextension
JNIEXPORT jint JNICALL OS_NATIVE(TXNInitTextension)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNInitTextension\n")
	rc = (jint)TXNInitTextension((const TXNMacOSPreferredFontDescription *)arg0, (ItemCount)arg1, (TXNInitOptions)arg2);
	NATIVE_EXIT(env, that, "TXNInitTextension\n")
	return rc;
}
#endif

#ifndef NO_TXNNewObject
JNIEXPORT jint JNICALL OS_NATIVE(TXNNewObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7, jintArray arg8, jint arg9)
{
	Rect _arg2, *lparg2=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNNewObject\n")
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)TXNNewObject((const FSSpec *)arg0, (WindowRef)arg1, (const Rect *)lparg2, (TXNFrameOptions)arg3, (TXNFrameType)arg4, (TXNFileType)arg5, (TXNPermanentTextEncodingType)arg6, (TXNObject *)lparg7, (TXNFrameID *)lparg8, (TXNObjectRefcon)arg9);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg2) setRectFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "TXNNewObject\n")
	return rc;
}
#endif

#ifndef NO_TXNOffsetToPoint
JNIEXPORT jint JNICALL OS_NATIVE(TXNOffsetToPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	Point _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNOffsetToPoint\n")
	if (arg2) lparg2 = getPointFields(env, arg2, &_arg2);
	rc = (jint)TXNOffsetToPoint((TXNObject)arg0, (TXNOffset)arg1, (Point *)lparg2);
	if (arg2) setPointFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "TXNOffsetToPoint\n")
	return rc;
}
#endif

#ifndef NO_TXNPaste
JNIEXPORT jint JNICALL OS_NATIVE(TXNPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNPaste\n")
	rc = (jint)TXNPaste((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNPaste\n")
	return rc;
}
#endif

#ifndef NO_TXNPointToOffset
JNIEXPORT jint JNICALL OS_NATIVE(TXNPointToOffset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	Point _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNPointToOffset\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)TXNPointToOffset((TXNObject)arg0, *lparg1, (TXNOffset *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNPointToOffset\n")
	return rc;
}
#endif

#ifndef NO_TXNSelectAll
JNIEXPORT void JNICALL OS_NATIVE(TXNSelectAll)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "TXNSelectAll\n")
	TXNSelectAll((TXNObject)arg0);
	NATIVE_EXIT(env, that, "TXNSelectAll\n")
}
#endif

#ifndef NO_TXNSetBackground
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetBackground)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TXNBackground _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNSetBackground\n")
	if (arg1) lparg1 = getTXNBackgroundFields(env, arg1, &_arg1);
	rc = (jint)TXNSetBackground((TXNObject)arg0, (const TXNBackground *)lparg1);
	if (arg1) setTXNBackgroundFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNSetBackground\n")
	return rc;
}
#endif

#ifndef NO_TXNSetData
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNSetData\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	rc = (jint)TXNSetData((TXNObject)arg0, (TXNDataType)arg1, (const void *)lparg2, (ByteCount)arg3, (TXNOffset)arg4, (TXNOffset)arg5);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "TXNSetData\n")
	return rc;
}
#endif

#ifndef NO_TXNSetFrameBounds
JNIEXPORT void JNICALL OS_NATIVE(TXNSetFrameBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "TXNSetFrameBounds\n")
	TXNSetFrameBounds((TXNObject)arg0, (SInt32)arg1, (SInt32)arg2, (SInt32)arg3, (SInt32)arg4, (TXNFrameID)arg5);
	NATIVE_EXIT(env, that, "TXNSetFrameBounds\n")
}
#endif

#ifndef NO_TXNSetRectBounds
JNIEXPORT void JNICALL OS_NATIVE(TXNSetRectBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jboolean arg3)
{
	Rect _arg1, *lparg1=NULL;
	TXNLongRect _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "TXNSetRectBounds\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getTXNLongRectFields(env, arg2, &_arg2);
	TXNSetRectBounds((TXNObject)arg0, (Rect *)lparg1, (TXNLongRect *)lparg2, (Boolean)arg3);
	if (arg2) setTXNLongRectFields(env, arg2, lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TXNSetRectBounds\n")
}
#endif

#ifndef NO_TXNSetSelection
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNSetSelection\n")
	rc = (jint)TXNSetSelection((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2);
	NATIVE_EXIT(env, that, "TXNSetSelection\n")
	return rc;
}
#endif

#ifndef NO_TXNSetTXNObjectControls
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetTXNObjectControls)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TXNSetTXNObjectControls\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)TXNSetTXNObjectControls((TXNObject)arg0, (Boolean)arg1, (ItemCount)arg2, (const TXNControlTag *)lparg3, (const TXNControlData *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "TXNSetTXNObjectControls\n")
	return rc;
}
#endif

#ifndef NO_TXNSetTypeAttributes
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetTypeAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "TXNSetTypeAttributes\n")
	rc = (jint)TXNSetTypeAttributes((TXNObject)arg0, (ItemCount)arg1, (const TXNTypeAttributes *)arg2, (TXNOffset)arg3, (TXNOffset)arg4);
	NATIVE_EXIT(env, that, "TXNSetTypeAttributes\n")
	return rc;
}
#endif

#ifndef NO_TXNShowSelection
JNIEXPORT void JNICALL OS_NATIVE(TXNShowSelection)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "TXNShowSelection\n")
	TXNShowSelection((TXNObject)arg0, (Boolean)arg1);
	NATIVE_EXIT(env, that, "TXNShowSelection\n")
}
#endif

#ifndef NO_TestControl
JNIEXPORT jshort JNICALL OS_NATIVE(TestControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Point _arg1, *lparg1=NULL;
	jshort rc;
	NATIVE_ENTER(env, that, "TestControl\n")
	if (arg1) lparg1 = getPointFields(env, arg1, &_arg1);
	rc = (jshort)TestControl((ControlRef)arg0, *(Point *)lparg1);
	if (arg1) setPointFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TestControl\n")
	return rc;
}
#endif

#ifndef NO_TextFace
JNIEXPORT void JNICALL OS_NATIVE(TextFace)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "TextFace\n")
	TextFace((StyleParameter)arg0);
	NATIVE_EXIT(env, that, "TextFace\n")
}
#endif

#ifndef NO_TextFont
JNIEXPORT void JNICALL OS_NATIVE(TextFont)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "TextFont\n")
	TextFont((short)arg0);
	NATIVE_EXIT(env, that, "TextFont\n")
}
#endif

#ifndef NO_TextMode
JNIEXPORT void JNICALL OS_NATIVE(TextMode)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "TextMode\n")
	TextMode((short)arg0);
	NATIVE_EXIT(env, that, "TextMode\n")
}
#endif

#ifndef NO_TextSize
JNIEXPORT void JNICALL OS_NATIVE(TextSize)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "TextSize\n")
	TextSize((short)arg0);
	NATIVE_EXIT(env, that, "TextSize\n")
}
#endif

#ifndef NO_TextWidth
JNIEXPORT jshort JNICALL OS_NATIVE(TextWidth)
	(JNIEnv *env, jclass that, jbyteArray arg0, jshort arg1, jshort arg2)
{
	jbyte *lparg0=NULL;
	jshort rc;
	NATIVE_ENTER(env, that, "TextWidth\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jshort)TextWidth((const void *)lparg0, (short)arg1, (short)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "TextWidth\n")
	return rc;
}
#endif

#ifndef NO_TrackDrag
JNIEXPORT jint JNICALL OS_NATIVE(TrackDrag)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	EventRecord _arg1, *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TrackDrag\n")
	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	rc = (jint)TrackDrag((DragRef)arg0, (const EventRecord *)lparg1, (RgnHandle)arg2);
	if (arg1) setEventRecordFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "TrackDrag\n")
	return rc;
}
#endif

#ifndef NO_TrackMouseLocationWithOptions
JNIEXPORT jint JNICALL OS_NATIVE(TrackMouseLocationWithOptions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2, jobject arg3, jintArray arg4, jshortArray arg5)
{
	Point _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jshort *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "TrackMouseLocationWithOptions\n")
	if (arg3) lparg3 = getPointFields(env, arg3, &_arg3);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL);
	rc = (jint)TrackMouseLocationWithOptions((GrafPtr)arg0, (OptionBits)arg1, (EventTimeout)arg2, (Point *)lparg3, (UInt32 *)lparg4, (MouseTrackingResult *)lparg5);
	if (arg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) setPointFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "TrackMouseLocationWithOptions\n")
	return rc;
}
#endif

#ifndef NO_UnionRect
JNIEXPORT void JNICALL OS_NATIVE(UnionRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	Rect _arg2, *lparg2=NULL;
	NATIVE_ENTER(env, that, "UnionRect\n")
	if (arg0) lparg0 = getRectFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getRectFields(env, arg2, &_arg2);
	UnionRect(lparg0, lparg1, lparg2);
	if (arg2) setRectFields(env, arg2, lparg2);
	if (arg1) setRectFields(env, arg1, lparg1);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "UnionRect\n")
}
#endif

#ifndef NO_UnionRgn
JNIEXPORT void JNICALL OS_NATIVE(UnionRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "UnionRgn\n")
	UnionRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	NATIVE_EXIT(env, that, "UnionRgn\n")
}
#endif

#ifndef NO_UnlockPortBits
JNIEXPORT jint JNICALL OS_NATIVE(UnlockPortBits)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "UnlockPortBits\n")
	rc = (jint)UnlockPortBits((GrafPtr)arg0);
	NATIVE_EXIT(env, that, "UnlockPortBits\n")
	return rc;
}
#endif

#ifndef NO_UpdateControls
JNIEXPORT void JNICALL OS_NATIVE(UpdateControls)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "UpdateControls\n")
	UpdateControls((WindowRef)arg0, (RgnHandle)arg1);
	NATIVE_EXIT(env, that, "UpdateControls\n")
}
#endif

#ifndef NO_UpdateDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(UpdateDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "UpdateDataBrowserItems\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)UpdateDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4, (DataBrowserPropertyID)arg5);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "UpdateDataBrowserItems\n")
	return rc;
}
#endif

#ifndef NO_UpgradeScriptInfoToTextEncoding
JNIEXPORT jint JNICALL OS_NATIVE(UpgradeScriptInfoToTextEncoding)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "UpgradeScriptInfoToTextEncoding\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)UpgradeScriptInfoToTextEncoding(arg0, arg1, arg2, lparg3, lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "UpgradeScriptInfoToTextEncoding\n")
	return rc;
}
#endif

#ifndef NO_WaitMouseMoved
JNIEXPORT jboolean JNICALL OS_NATIVE(WaitMouseMoved)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "WaitMouseMoved\n")
	if (arg0) lparg0 = getPointFields(env, arg0, &_arg0);
	rc = (jboolean)WaitMouseMoved(*lparg0);
	if (arg0) setPointFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "WaitMouseMoved\n")
	return rc;
}
#endif

#ifndef NO_X2Fix
JNIEXPORT jint JNICALL OS_NATIVE(X2Fix)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "X2Fix\n")
	rc = (jint)X2Fix(arg0);
	NATIVE_EXIT(env, that, "X2Fix\n")
	return rc;
}
#endif

#ifndef NO_ZoomWindowIdeal
JNIEXPORT jint JNICALL OS_NATIVE(ZoomWindowIdeal)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Point _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "ZoomWindowIdeal\n")
	if (arg2) lparg2 = getPointFields(env, arg2, &_arg2);
	rc = (jint)ZoomWindowIdeal((WindowRef)arg0, (WindowPartCode)arg1, (Point *)lparg2);
	if (arg2) setPointFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "ZoomWindowIdeal\n")
	return rc;
}
#endif

#ifndef NO_kHIViewWindowContentID
JNIEXPORT jint JNICALL OS_NATIVE(kHIViewWindowContentID)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "kHIViewWindowContentID\n")
	rc = (jint)&kHIViewWindowContentID;
	NATIVE_EXIT(env, that, "kHIViewWindowContentID\n")
	return rc;
}
#endif

#ifndef NO_kPMDocumentFormatPDF
JNIEXPORT jint JNICALL OS_NATIVE(kPMDocumentFormatPDF)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "kPMDocumentFormatPDF\n")
	rc = (jint)kPMDocumentFormatPDF;
	NATIVE_EXIT(env, that, "kPMDocumentFormatPDF\n")
	return rc;
}
#endif

#ifndef NO_kPMGraphicsContextCoreGraphics
JNIEXPORT jint JNICALL OS_NATIVE(kPMGraphicsContextCoreGraphics)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "kPMGraphicsContextCoreGraphics\n")
	rc = (jint)kPMGraphicsContextCoreGraphics;
	NATIVE_EXIT(env, that, "kPMGraphicsContextCoreGraphics\n")
	return rc;
}
#endif

#ifndef NO_memcpy__III
JNIEXPORT void JNICALL OS_NATIVE(memcpy__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "memcpy__III\n")
	memcpy((void *)arg0, (const void *)arg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__III\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	ATSUTab _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I\n")
	if (arg1) lparg1 = getATSUTabFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	BitMap _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I\n")
	if (arg1) lparg1 = getBitMapFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_BitMap_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Cursor _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I\n")
	if (arg1) lparg1 = getCursorFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_Cursor_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_EventRecord_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_EventRecord_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	EventRecord _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_EventRecord_2I\n")
	if (arg1) lparg1 = getEventRecordFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_EventRecord_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	FontSelectionQDStyle _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I\n")
	if (arg1) lparg1 = getFontSelectionQDStyleFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	HMHelpContentRec _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I\n")
	if (arg1) lparg1 = getHMHelpContentRecFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PixMap _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I\n")
	if (arg1) lparg1 = getPixMapFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_PixMap_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_RGBColor_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_RGBColor_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RGBColor _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_RGBColor_2I\n")
	if (arg1) lparg1 = getRGBColorFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_RGBColor_2I\n")
}
#endif

#ifndef NO_memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I
JNIEXPORT void JNICALL OS_NATIVE(memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I\n")
	if (arg1) lparg1 = getRectFields(env, arg1, &_arg1);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memcpy__ILorg_eclipse_swt_internal_carbon_Rect_2I\n")
}
#endif

#ifndef NO_memcpy__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__I_3BI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memcpy__I_3BI\n")
}
#endif

#ifndef NO_memcpy__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__I_3CI\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memcpy__I_3CI\n")
}
#endif

#ifndef NO_memcpy__I_3II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy__I_3II\n")
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	memcpy((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memcpy__I_3II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	ATSTrapezoid _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setATSTrapezoidFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FontSelectionQDStyle _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setFontSelectionQDStyleFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_FontSelectionQDStyle_2II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GDevice _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setGDeviceFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_GDevice_2II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HMHelpContentRec _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setHMHelpContentRecFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PixMap _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setPixMapFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_PixMap_2II\n")
}
#endif

#ifndef NO_memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II
JNIEXPORT void JNICALL OS_NATIVE(memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	Rect _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II\n")
	if (arg0) lparg0 = &_arg0;
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setRectFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memcpy__Lorg_eclipse_swt_internal_carbon_Rect_2II\n")
}
#endif

#ifndef NO_memcpy___3BII
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy___3BII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3BII\n")
}
#endif

#ifndef NO_memcpy___3B_3CI
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3B_3CI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jchar *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy___3B_3CI\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	memcpy((void *)lparg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3B_3CI\n")
}
#endif

#ifndef NO_memcpy___3CII
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy___3CII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3CII\n")
}
#endif

#ifndef NO_memcpy___3C_3BI
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3C_3BI)
	(JNIEnv *env, jclass that, jcharArray arg0, jbyteArray arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "memcpy___3C_3BI\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	memcpy((void *)lparg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3C_3BI\n")
}
#endif

#ifndef NO_memcpy___3FII
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3FII)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy___3FII\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3FII\n")
}
#endif

#ifndef NO_memcpy___3III
JNIEXPORT void JNICALL OS_NATIVE(memcpy___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "memcpy___3III\n")
	if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	memcpy((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memcpy___3III\n")
}
#endif

#ifndef NO_memset
JNIEXPORT void JNICALL OS_NATIVE(memset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "memset\n")
	memset((void *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "memset\n")
}
#endif


/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_carbon_OS_##func

#ifndef NO_AECoerceDesc
JNIEXPORT jint JNICALL OS_NATIVE(AECoerceDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	AEDesc _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AECoerceDesc_FUNC);
	if (arg2) if ((lparg2 = getAEDescFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)AECoerceDesc((AEDesc *)arg0, arg1, (AEDesc *)lparg2);
fail:
	if (arg2 && lparg2) setAEDescFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, AECoerceDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_AECountItems
JNIEXPORT jint JNICALL OS_NATIVE(AECountItems)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	AEDesc _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AECountItems_FUNC);
	if (arg0) if ((lparg0 = getAEDescFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)AECountItems((const AEDescList *)lparg0, (long *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setAEDescFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AECountItems_FUNC);
	return rc;
}
#endif

#ifndef NO_AECreateDesc
JNIEXPORT jint JNICALL OS_NATIVE(AECreateDesc)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jobject arg3)
{
	jbyte *lparg1=NULL;
	AEDesc _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AECreateDesc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getAEDescFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)AECreateDesc((DescType)arg0, (const void *)lparg1, (Size)arg2, lparg3);
fail:
	if (arg3 && lparg3) setAEDescFields(env, arg3, lparg3);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, AECreateDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_AEDisposeDesc
JNIEXPORT jint JNICALL OS_NATIVE(AEDisposeDesc)
	(JNIEnv *env, jclass that, jobject arg0)
{
	AEDesc _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEDisposeDesc_FUNC);
	if (arg0) if ((lparg0 = getAEDescFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)AEDisposeDesc(lparg0);
fail:
	if (arg0 && lparg0) setAEDescFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AEDisposeDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_AEGetDescData
JNIEXPORT jint JNICALL OS_NATIVE(AEGetDescData)
	(JNIEnv *env, jclass that, jobject arg0, jbyteArray arg1, jint arg2)
{
	AEDesc _arg0, *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEGetDescData_FUNC);
	if (arg0) if ((lparg0 = getAEDescFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)AEGetDescData((AEDesc *)lparg0, (void *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setAEDescFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AEGetDescData_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEGetNthPtr_FUNC);
	if (arg0) if ((lparg0 = getAEDescFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)AEGetNthPtr((const AEDescList *)lparg0, arg1, (DescType)arg2, (AEKeyword *)lparg3, (DescType *)lparg4, (void *)arg5, (Size)arg6, (Size *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) setAEDescFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AEGetNthPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_AEGetParamDesc
JNIEXPORT jint JNICALL OS_NATIVE(AEGetParamDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	AEDesc _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEGetParamDesc_FUNC);
	if (arg3) if ((lparg3 = getAEDescFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)AEGetParamDesc((const AppleEvent *)arg0, (AEKeyword)arg1, (DescType)arg2, (AEDesc *)lparg3);
fail:
	if (arg3 && lparg3) setAEDescFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, AEGetParamDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_AEInstallEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(AEInstallEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEInstallEventHandler_FUNC);
	rc = (jint)AEInstallEventHandler((AEEventClass)arg0, (AEEventID)arg1, (AEEventHandlerUPP)arg2, (long)arg3, arg4);
	OS_NATIVE_EXIT(env, that, AEInstallEventHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_AEProcessAppleEvent
JNIEXPORT jint JNICALL OS_NATIVE(AEProcessAppleEvent)
	(JNIEnv *env, jclass that, jobject arg0)
{
	EventRecord _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AEProcessAppleEvent_FUNC);
	if (arg0) if ((lparg0 = getEventRecordFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)AEProcessAppleEvent((const EventRecord *)lparg0);
fail:
	if (arg0 && lparg0) setEventRecordFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, AEProcessAppleEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontActivateFromFileSpecification
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontActivateFromFileSpecification)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg0=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontActivateFromFileSpecification_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontActivateFromFileSpecification((const FSSpec *)lparg0, (ATSFontContext)arg1, (ATSFontFormat)arg2, (void *)arg3, (ATSOptionFlags)arg4, (ATSFontContainerRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSFontActivateFromFileSpecification_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontDeactivate
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontDeactivate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontDeactivate_FUNC);
	rc = (jint)ATSFontDeactivate((ATSFontContainerRef)arg0, (void *)arg1, (ATSOptionFlags)arg2);
	OS_NATIVE_EXIT(env, that, ATSFontDeactivate_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontFindFromName
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontFindFromName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontFindFromName_FUNC);
	rc = (jint)ATSFontFindFromName((CFStringRef)arg0, (ATSOptionFlags)arg1);
	OS_NATIVE_EXIT(env, that, ATSFontFindFromName_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontGetHorizontalMetrics
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontGetHorizontalMetrics)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	ATSFontMetrics _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontGetHorizontalMetrics_FUNC);
	if (arg2) if ((lparg2 = getATSFontMetricsFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ATSFontGetHorizontalMetrics((ATSFontRef)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setATSFontMetricsFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ATSFontGetHorizontalMetrics_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontGetName
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontGetName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontGetName_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontGetName((ATSFontRef)arg0, (ATSOptionFlags)arg1, (CFStringRef*)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ATSFontGetName_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontGetPostScriptName
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontGetPostScriptName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontGetPostScriptName_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontGetPostScriptName((ATSFontRef)arg0, (ATSOptionFlags)arg1, (CFStringRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ATSFontGetPostScriptName_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontGetVerticalMetrics
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontGetVerticalMetrics)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	ATSFontMetrics _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontGetVerticalMetrics_FUNC);
	if (arg2) if ((lparg2 = getATSFontMetricsFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ATSFontGetVerticalMetrics((ATSFontRef)arg0, (ATSOptionFlags)arg1, lparg2);
fail:
	if (arg2 && lparg2) setATSFontMetricsFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ATSFontGetVerticalMetrics_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontIteratorCreate
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontIteratorCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontIteratorCreate_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontIteratorCreate((ATSFontContext)arg0, (const ATSFontFilter *)arg1, (void *)arg2, (ATSOptionFlags)arg3, (ATSFontIterator *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, ATSFontIteratorCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontIteratorNext
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontIteratorNext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontIteratorNext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontIteratorNext((ATSFontIterator)arg0, (ATSFontRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ATSFontIteratorNext_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSFontIteratorRelease
JNIEXPORT jint JNICALL OS_NATIVE(ATSFontIteratorRelease)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSFontIteratorRelease_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)ATSFontIteratorRelease((ATSFontIterator *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSFontIteratorRelease_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUBatchBreakLines
JNIEXPORT jint JNICALL OS_NATIVE(ATSUBatchBreakLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUBatchBreakLines_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSUBatchBreakLines((ATSUTextLayout)arg0, arg1, arg2, arg3, (ItemCount *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, ATSUBatchBreakLines_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUCreateStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateStyle)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUCreateStyle_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)ATSUCreateStyle((ATSUStyle *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSUCreateStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUCreateTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUCreateTextLayout)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUCreateTextLayout_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)ATSUCreateTextLayout((ATSUTextLayout *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSUCreateTextLayout_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUCreateTextLayoutWithTextPtr_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)ATSUCreateTextLayoutWithTextPtr((ConstUniCharArrayPtr)arg0, arg1, arg2, arg3, arg4, (const UniCharCount *)lparg5, (ATSUStyle *)lparg6, (ATSUTextLayout *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, ATSUCreateTextLayoutWithTextPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUDirectGetLayoutDataArrayPtrFromTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDirectGetLayoutDataArrayPtrFromTextLayout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUDirectGetLayoutDataArrayPtrFromTextLayout_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSUDirectGetLayoutDataArrayPtrFromTextLayout((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (ATSUDirectDataSelector)arg2, (void *)lparg3, (ItemCount *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUDirectGetLayoutDataArrayPtrFromTextLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUDirectReleaseLayoutDataArrayPtr
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDirectReleaseLayoutDataArrayPtr)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUDirectReleaseLayoutDataArrayPtr_FUNC);
	rc = (jint)ATSUDirectReleaseLayoutDataArrayPtr((ATSULineRef)arg0, (ATSUDirectDataSelector)arg1, (void *)arg2);
	OS_NATIVE_EXIT(env, that, ATSUDirectReleaseLayoutDataArrayPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUDisposeStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeStyle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUDisposeStyle_FUNC);
	rc = (jint)ATSUDisposeStyle((ATSUStyle)arg0);
	OS_NATIVE_EXIT(env, that, ATSUDisposeStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUDisposeTextLayout
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDisposeTextLayout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUDisposeTextLayout_FUNC);
	rc = (jint)ATSUDisposeTextLayout((ATSUTextLayout)arg0);
	OS_NATIVE_EXIT(env, that, ATSUDisposeTextLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUDrawText
JNIEXPORT jint JNICALL OS_NATIVE(ATSUDrawText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUDrawText_FUNC);
	rc = (jint)ATSUDrawText((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (UniCharCount)arg2, (ATSUTextMeasurement)arg3, (ATSUTextMeasurement)arg4);
	OS_NATIVE_EXIT(env, that, ATSUDrawText_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUFindFontFromName
JNIEXPORT jint JNICALL OS_NATIVE(ATSUFindFontFromName)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jbyte *lparg0=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUFindFontFromName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)ATSUFindFontFromName((const void *)lparg0, arg1, arg2, arg3, arg4, arg5, (ATSUFontID *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSUFindFontFromName_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUFindFontName_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ATSUFindFontName((ATSUFontID)arg0, arg1, arg2, arg3, arg4, arg5, (Ptr)lparg6, (ByteCount *)lparg7, (ItemCount *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, ATSUFindFontName_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetFontIDs
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetFontIDs)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jintArray arg2)
{
	jint *lparg0=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetFontIDs_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetFontIDs((ATSUFontID *)lparg0, arg1, (ItemCount *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetFontIDs_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetGlyphBounds__IIIIISII_3I
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetGlyphBounds__IIIIISII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6, jint arg7, jintArray arg8)
{
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetGlyphBounds__IIIIISII_3I_FUNC);
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetGlyphBounds((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, (UniCharArrayOffset)arg3, arg4, arg5, arg6, (ATSTrapezoid *)arg7, (ItemCount *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetGlyphBounds__IIIIISII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jshort arg5, jint arg6, jobject arg7, jintArray arg8)
{
	ATSTrapezoid _arg7, *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I_FUNC);
	if (arg7) if ((lparg7 = getATSTrapezoidFields(env, arg7, &_arg7)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetGlyphBounds((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, (UniCharArrayOffset)arg3, arg4, arg5, arg6, (ATSTrapezoid *)lparg7, (ItemCount *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) setATSTrapezoidFields(env, arg7, lparg7);
	OS_NATIVE_EXIT(env, that, ATSUGetGlyphBounds__IIIIISILorg_eclipse_swt_internal_carbon_ATSTrapezoid_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetLayoutControl
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetLayoutControl)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetLayoutControl_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetLayoutControl((ATSUTextLayout)arg0, (ATSUAttributeTag)arg1, arg2, (ATSUAttributeValuePtr)lparg3, (ByteCount *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetLayoutControl_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetLineControl
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetLineControl)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetLineControl_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetLineControl((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (ATSUAttributeTag)arg2, (ByteCount)arg3, (ATSUAttributeValuePtr)lparg4, (ByteCount *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetLineControl_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetSoftLineBreaks
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetSoftLineBreaks)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetSoftLineBreaks_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetSoftLineBreaks((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (UniCharCount)arg2, (ItemCount)arg3, (UniCharArrayOffset *)lparg4, (ItemCount *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetSoftLineBreaks_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetTextHighlight
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetTextHighlight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetTextHighlight_FUNC);
	rc = (jint)ATSUGetTextHighlight((ATSUTextLayout)arg0, arg1, arg2, arg3, arg4, (RgnHandle)arg5);
	OS_NATIVE_EXIT(env, that, ATSUGetTextHighlight_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGetUnjustifiedBounds
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGetUnjustifiedBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGetUnjustifiedBounds_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGetUnjustifiedBounds((ATSUTextLayout)arg0, arg1, arg2, (ATSUTextMeasurement *)lparg3, (ATSUTextMeasurement *)lparg4, (ATSUTextMeasurement *)lparg5, (ATSUTextMeasurement *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUGetUnjustifiedBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUGlyphGetQuadraticPaths
JNIEXPORT jint JNICALL OS_NATIVE(ATSUGlyphGetQuadraticPaths)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
{
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUGlyphGetQuadraticPaths_FUNC);
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)ATSUGlyphGetQuadraticPaths((ATSUStyle)arg0, (GlyphID)arg1, (ATSQuadraticNewPathUPP)arg2, (ATSQuadraticLineUPP)arg3, (ATSQuadraticCurveUPP)arg4, (ATSQuadraticClosePathUPP)arg5, (void *)arg6, (OSStatus *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	OS_NATIVE_EXIT(env, that, ATSUGlyphGetQuadraticPaths_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUHighlightText
JNIEXPORT jint JNICALL OS_NATIVE(ATSUHighlightText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUHighlightText_FUNC);
	rc = (jint)ATSUHighlightText((ATSUTextLayout)arg0, (ATSUTextMeasurement)arg1, (ATSUTextMeasurement)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, ATSUHighlightText_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUNextCursorPosition
JNIEXPORT jint JNICALL OS_NATIVE(ATSUNextCursorPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUNextCursorPosition_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ATSUNextCursorPosition((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (ATSUCursorMovementType)arg2, (UniCharArrayOffset *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUNextCursorPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUOffsetToPosition
JNIEXPORT jint JNICALL OS_NATIVE(ATSUOffsetToPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jobject arg3, jobject arg4, jbooleanArray arg5)
{
	ATSUCaret _arg3, *lparg3=NULL;
	ATSUCaret _arg4, *lparg4=NULL;
	jboolean *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUOffsetToPosition_FUNC);
	if (arg3) if ((lparg3 = getATSUCaretFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getATSUCaretFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetBooleanArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSUOffsetToPosition((ATSUTextLayout)arg0, arg1, arg2, lparg3, lparg4, (Boolean *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseBooleanArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setATSUCaretFields(env, arg4, lparg4);
	if (arg3 && lparg3) setATSUCaretFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, ATSUOffsetToPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUPositionToOffset
JNIEXPORT jint JNICALL OS_NATIVE(ATSUPositionToOffset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jbooleanArray arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jboolean *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUPositionToOffset_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetBooleanArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSUPositionToOffset((ATSUTextLayout)arg0, arg1, arg2, (UniCharArrayOffset *)lparg3, (Boolean *)lparg4, (UniCharArrayOffset *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseBooleanArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUPositionToOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUPreviousCursorPosition
JNIEXPORT jint JNICALL OS_NATIVE(ATSUPreviousCursorPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUPreviousCursorPosition_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ATSUPreviousCursorPosition((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (ATSUCursorMovementType)arg2, (UniCharArrayOffset *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUPreviousCursorPosition_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetAttributes_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSUSetAttributes((ATSUStyle)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ATSUSetAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetFontFeatures
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetFontFeatures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetFontFeatures_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ATSUSetFontFeatures((ATSUStyle)arg0, (ItemCount)arg1, (const ATSUFontFeatureType *)lparg2, (const ATSUFontFeatureSelector *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ATSUSetFontFeatures_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetHighlightingMethod
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetHighlightingMethod)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	ATSUUnhighlightData _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetHighlightingMethod_FUNC);
	if (arg2) if ((lparg2 = getATSUUnhighlightDataFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ATSUSetHighlightingMethod((ATSUTextLayout)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setATSUUnhighlightDataFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ATSUSetHighlightingMethod_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetLayoutControls_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ATSUSetLayoutControls((ATSUTextLayout)arg0, (ItemCount)arg1, (ATSUAttributeTag *)lparg2, (ByteCount *)lparg3, (ATSUAttributeValuePtr *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ATSUSetLayoutControls_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetLineControls
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetLineControls)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetLineControls_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)ATSUSetLineControls((ATSUTextLayout)arg0, (UniCharArrayOffset)arg1, (ItemCount)arg2, (const ATSUAttributeTag *)lparg3, (const ByteCount *)lparg4, (const ATSUAttributeValuePtr *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, ATSUSetLineControls_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetRunStyle
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetRunStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetRunStyle_FUNC);
	rc = (jint)ATSUSetRunStyle((ATSUTextLayout)arg0, (ATSUStyle)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3);
	OS_NATIVE_EXIT(env, that, ATSUSetRunStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetSoftLineBreak
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetSoftLineBreak)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetSoftLineBreak_FUNC);
	rc = (jint)ATSUSetSoftLineBreak((ATSUTextLayout)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ATSUSetSoftLineBreak_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetTabArray
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTabArray)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetTabArray_FUNC);
	rc = (jint)ATSUSetTabArray((ATSUTextLayout)arg0, (const ATSUTab *)arg1, arg2);
	OS_NATIVE_EXIT(env, that, ATSUSetTabArray_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetTextPointerLocation
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTextPointerLocation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetTextPointerLocation_FUNC);
	rc = (jint)ATSUSetTextPointerLocation((ATSUTextLayout)arg0, (ConstUniCharArrayPtr)arg1, (UniCharArrayOffset)arg2, (UniCharCount)arg3, (UniCharCount)arg4);
	OS_NATIVE_EXIT(env, that, ATSUSetTextPointerLocation_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUSetTransientFontMatching
JNIEXPORT jint JNICALL OS_NATIVE(ATSUSetTransientFontMatching)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUSetTransientFontMatching_FUNC);
	rc = (jint)ATSUSetTransientFontMatching((ATSUTextLayout)arg0, arg1);
	OS_NATIVE_EXIT(env, that, ATSUSetTransientFontMatching_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUTextDeleted
JNIEXPORT jint JNICALL OS_NATIVE(ATSUTextDeleted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUTextDeleted_FUNC);
	rc = (jint)ATSUTextDeleted((ATSUTextLayout)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ATSUTextDeleted_FUNC);
	return rc;
}
#endif

#ifndef NO_ATSUTextInserted
JNIEXPORT jint JNICALL OS_NATIVE(ATSUTextInserted)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ATSUTextInserted_FUNC);
	rc = (jint)ATSUTextInserted((ATSUTextLayout)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, ATSUTextInserted_FUNC);
	return rc;
}
#endif

#ifndef NO_AXNotificationHIObjectNotify
JNIEXPORT void JNICALL OS_NATIVE(AXNotificationHIObjectNotify)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2)
{
	OS_NATIVE_ENTER(env, that, AXNotificationHIObjectNotify_FUNC);
	AXNotificationHIObjectNotify((CFStringRef)arg0, (HIObjectRef)arg1, (UInt64)arg2);
	OS_NATIVE_EXIT(env, that, AXNotificationHIObjectNotify_FUNC);
}
#endif

#ifndef NO_AXUIElementCopyAttributeValue
JNIEXPORT jint JNICALL OS_NATIVE(AXUIElementCopyAttributeValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXUIElementCopyAttributeValue_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)AXUIElementCopyAttributeValue((AXUIElementRef)arg0, (CFStringRef)arg1, (CFTypeRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, AXUIElementCopyAttributeValue_FUNC);
	return rc;
}
#endif

#ifndef NO_AXUIElementCreateWithDataBrowserAndItemInfo
JNIEXPORT jint JNICALL OS_NATIVE(AXUIElementCreateWithDataBrowserAndItemInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserAccessibilityItemInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXUIElementCreateWithDataBrowserAndItemInfo_FUNC);
	if (arg1) if ((lparg1 = getDataBrowserAccessibilityItemInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jint)AXUIElementCreateWithDataBrowserAndItemInfo(arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, AXUIElementCreateWithDataBrowserAndItemInfo)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, DataBrowserAccessibilityItemInfo *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setDataBrowserAccessibilityItemInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, AXUIElementCreateWithDataBrowserAndItemInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_AXUIElementCreateWithHIObjectAndIdentifier
JNIEXPORT jint JNICALL OS_NATIVE(AXUIElementCreateWithHIObjectAndIdentifier)
	(JNIEnv *env, jclass that, jint arg0, jlong arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXUIElementCreateWithHIObjectAndIdentifier_FUNC);
	rc = (jint)AXUIElementCreateWithHIObjectAndIdentifier((HIObjectRef)arg0, (UInt64)arg1);
	OS_NATIVE_EXIT(env, that, AXUIElementCreateWithHIObjectAndIdentifier_FUNC);
	return rc;
}
#endif

#ifndef NO_AXUIElementGetDataBrowserItemInfo
JNIEXPORT jint JNICALL OS_NATIVE(AXUIElementGetDataBrowserItemInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	DataBrowserAccessibilityItemInfo _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXUIElementGetDataBrowserItemInfo_FUNC);
	if (arg3) if ((lparg3 = getDataBrowserAccessibilityItemInfoFields(env, arg3, &_arg3)) == NULL) goto fail;
/*
	rc = (jint)AXUIElementGetDataBrowserItemInfo(arg0, arg1, arg2, lparg3);
*/
	{
		LOAD_FUNCTION(fp, AXUIElementGetDataBrowserItemInfo)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint, DataBrowserAccessibilityItemInfo *))fp)(arg0, arg1, arg2, lparg3);
		}
	}
fail:
	if (arg3 && lparg3) setDataBrowserAccessibilityItemInfoFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, AXUIElementGetDataBrowserItemInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_AXUIElementGetHIObject
JNIEXPORT jint JNICALL OS_NATIVE(AXUIElementGetHIObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXUIElementGetHIObject_FUNC);
	rc = (jint)AXUIElementGetHIObject((AXUIElementRef)arg0);
	OS_NATIVE_EXIT(env, that, AXUIElementGetHIObject_FUNC);
	return rc;
}
#endif

#ifndef NO_AXUIElementGetIdentifier
JNIEXPORT void JNICALL OS_NATIVE(AXUIElementGetIdentifier)
	(JNIEnv *env, jclass that, jint arg0, jlongArray arg1)
{
	jlong *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, AXUIElementGetIdentifier_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	AXUIElementGetIdentifier((AXUIElementRef)arg0, (UInt64 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseLongArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, AXUIElementGetIdentifier_FUNC);
}
#endif

#ifndef NO_AXValueCreate
JNIEXPORT jint JNICALL OS_NATIVE(AXValueCreate)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CFRange _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AXValueCreate_FUNC);
	if (arg1) if ((lparg1 = getCFRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)AXValueCreate((AXValueType)arg0, (CFRange *)lparg1);
fail:
	if (arg1 && lparg1) setCFRangeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, AXValueCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_AXValueGetValue
JNIEXPORT jboolean JNICALL OS_NATIVE(AXValueGetValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CFRange _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, AXValueGetValue_FUNC);
	if (arg2) if ((lparg2 = getCFRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)AXValueGetValue((AXValueRef)arg0, (AXValueType)arg1, (CFRange *)lparg2);
fail:
	if (arg2 && lparg2) setCFRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, AXValueGetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_AcquireFirstMatchingEventInQueue
JNIEXPORT jint JNICALL OS_NATIVE(AcquireFirstMatchingEventInQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AcquireFirstMatchingEventInQueue_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)AcquireFirstMatchingEventInQueue((EventQueueRef)arg0, arg1, (const EventTypeSpec *)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, AcquireFirstMatchingEventInQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_ActivateTSMDocument
JNIEXPORT jint JNICALL OS_NATIVE(ActivateTSMDocument)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ActivateTSMDocument_FUNC);
	rc = (jint)ActivateTSMDocument((TSMDocumentID)arg0);
	OS_NATIVE_EXIT(env, that, ActivateTSMDocument_FUNC);
	return rc;
}
#endif

#ifndef NO_ActiveNonFloatingWindow
JNIEXPORT jint JNICALL OS_NATIVE(ActiveNonFloatingWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ActiveNonFloatingWindow_FUNC);
	rc = (jint)ActiveNonFloatingWindow();
	OS_NATIVE_EXIT(env, that, ActiveNonFloatingWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_AddDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(AddDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AddDataBrowserItems_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)AddDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, AddDataBrowserItems_FUNC);
	return rc;
}
#endif

#ifndef NO_AddDataBrowserListViewColumn
JNIEXPORT jint JNICALL OS_NATIVE(AddDataBrowserListViewColumn)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	DataBrowserListViewColumnDesc _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AddDataBrowserListViewColumn_FUNC);
	if (arg1) if ((lparg1 = getDataBrowserListViewColumnDescFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)AddDataBrowserListViewColumn((ControlRef)arg0, (DataBrowserListViewColumnDesc *)lparg1, (DataBrowserTableViewColumnIndex)arg2);
fail:
	if (arg1 && lparg1) setDataBrowserListViewColumnDescFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, AddDataBrowserListViewColumn_FUNC);
	return rc;
}
#endif

#ifndef NO_AddDragItemFlavor
JNIEXPORT jint JNICALL OS_NATIVE(AddDragItemFlavor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AddDragItemFlavor_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)AddDragItemFlavor((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (const void *)lparg3, (Size)arg4, (FlavorFlags)arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, AddDragItemFlavor_FUNC);
	return rc;
}
#endif

#ifndef NO_AppendMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(AppendMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AppendMenuItemTextWithCFString_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)AppendMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemAttributes)arg2, (MenuCommand)arg3, (MenuItemIndex *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, AppendMenuItemTextWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_AutoSizeDataBrowserListViewColumns
JNIEXPORT jint JNICALL OS_NATIVE(AutoSizeDataBrowserListViewColumns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, AutoSizeDataBrowserListViewColumns_FUNC);
	rc = (jint)AutoSizeDataBrowserListViewColumns((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, AutoSizeDataBrowserListViewColumns_FUNC);
	return rc;
}
#endif

#ifndef NO_BringToFront
JNIEXPORT void JNICALL OS_NATIVE(BringToFront)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, BringToFront_FUNC);
	BringToFront((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, BringToFront_FUNC);
}
#endif

#ifndef NO_CFArrayAppendValue
JNIEXPORT void JNICALL OS_NATIVE(CFArrayAppendValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CFArrayAppendValue_FUNC);
	CFArrayAppendValue((CFMutableArrayRef)arg0, (const void *)arg1);
	OS_NATIVE_EXIT(env, that, CFArrayAppendValue_FUNC);
}
#endif

#ifndef NO_CFArrayCreateMutable
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayCreateMutable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFArrayCreateMutable_FUNC);
	rc = (jint)CFArrayCreateMutable((CFAllocatorRef)arg0, (CFIndex)arg1, (const CFArrayCallBacks *)arg2);
	OS_NATIVE_EXIT(env, that, CFArrayCreateMutable_FUNC);
	return rc;
}
#endif

#ifndef NO_CFArrayGetCount
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayGetCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFArrayGetCount_FUNC);
	rc = (jint)CFArrayGetCount((CFArrayRef)arg0);
	OS_NATIVE_EXIT(env, that, CFArrayGetCount_FUNC);
	return rc;
}
#endif

#ifndef NO_CFArrayGetValueAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(CFArrayGetValueAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFArrayGetValueAtIndex_FUNC);
	rc = (jint)CFArrayGetValueAtIndex((CFArrayRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CFArrayGetValueAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_CFBundleCreateBundlesFromDirectory
JNIEXPORT jint JNICALL OS_NATIVE(CFBundleCreateBundlesFromDirectory)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFBundleCreateBundlesFromDirectory_FUNC);
	rc = (jint)CFBundleCreateBundlesFromDirectory((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, CFBundleCreateBundlesFromDirectory_FUNC);
	return rc;
}
#endif

#ifndef NO_CFBundleGetIdentifier
JNIEXPORT jint JNICALL OS_NATIVE(CFBundleGetIdentifier)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFBundleGetIdentifier_FUNC);
	rc = (jint)CFBundleGetIdentifier((CFBundleRef)arg0);
	OS_NATIVE_EXIT(env, that, CFBundleGetIdentifier_FUNC);
	return rc;
}
#endif

#ifndef NO_CFBundleGetMainBundle
JNIEXPORT jint JNICALL OS_NATIVE(CFBundleGetMainBundle)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFBundleGetMainBundle_FUNC);
	rc = (jint)CFBundleGetMainBundle();
	OS_NATIVE_EXIT(env, that, CFBundleGetMainBundle_FUNC);
	return rc;
}
#endif

#ifndef NO_CFBundleGetPackageInfo
JNIEXPORT void JNICALL OS_NATIVE(CFBundleGetPackageInfo)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CFBundleGetPackageInfo_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CFBundleGetPackageInfo((CFBundleRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFBundleGetPackageInfo_FUNC);
}
#endif

#ifndef NO_CFBundleGetValueForInfoDictionaryKey
JNIEXPORT jint JNICALL OS_NATIVE(CFBundleGetValueForInfoDictionaryKey)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFBundleGetValueForInfoDictionaryKey_FUNC);
	rc = (jint)CFBundleGetValueForInfoDictionaryKey((CFBundleRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, CFBundleGetValueForInfoDictionaryKey_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDataGetBytePtr
JNIEXPORT jint JNICALL OS_NATIVE(CFDataGetBytePtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFDataGetBytePtr_FUNC);
	rc = (jint)CFDataGetBytePtr((CFDataRef)arg0);
	OS_NATIVE_EXIT(env, that, CFDataGetBytePtr_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDataGetBytes
JNIEXPORT void JNICALL OS_NATIVE(CFDataGetBytes)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2)
{
	CFRange _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CFDataGetBytes_FUNC);
	if (arg1) if ((lparg1 = getCFRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CFDataGetBytes((CFDataRef)arg0, *lparg1, (UInt8 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCFRangeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CFDataGetBytes_FUNC);
}
#endif

#ifndef NO_CFDataGetLength
JNIEXPORT jint JNICALL OS_NATIVE(CFDataGetLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFDataGetLength_FUNC);
	rc = (jint)CFDataGetLength((CFDataRef)arg0);
	OS_NATIVE_EXIT(env, that, CFDataGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDictionaryGetValueIfPresent
JNIEXPORT jboolean JNICALL OS_NATIVE(CFDictionaryGetValueIfPresent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CFDictionaryGetValueIfPresent_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)CFDictionaryGetValueIfPresent((CFDictionaryRef)arg0, (const void *)arg1, (const void **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CFDictionaryGetValueIfPresent_FUNC);
	return rc;
}
#endif

#ifndef NO_CFEqual
JNIEXPORT jboolean JNICALL OS_NATIVE(CFEqual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CFEqual_FUNC);
	rc = (jboolean)CFEqual((CFStringRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, CFEqual_FUNC);
	return rc;
}
#endif

#ifndef NO_CFLocaleCopyCurrent
JNIEXPORT jint JNICALL OS_NATIVE(CFLocaleCopyCurrent)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFLocaleCopyCurrent_FUNC);
	rc = (jint)CFLocaleCopyCurrent();
	OS_NATIVE_EXIT(env, that, CFLocaleCopyCurrent_FUNC);
	return rc;
}
#endif

#ifndef NO_CFNumberFormatterCopyProperty
JNIEXPORT jint JNICALL OS_NATIVE(CFNumberFormatterCopyProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFNumberFormatterCopyProperty_FUNC);
	rc = (jint)CFNumberFormatterCopyProperty((CFNumberFormatterRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, CFNumberFormatterCopyProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_CFNumberFormatterCreate
JNIEXPORT jint JNICALL OS_NATIVE(CFNumberFormatterCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFNumberFormatterCreate_FUNC);
	rc = (jint)CFNumberFormatterCreate((CFAllocatorRef)arg0, (CFLocaleRef)arg1, (CFNumberFormatterStyle)arg2);
	OS_NATIVE_EXIT(env, that, CFNumberFormatterCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CFRelease
JNIEXPORT void JNICALL OS_NATIVE(CFRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRelease_FUNC);
	CFRelease((CFTypeRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRelease_FUNC);
}
#endif

#ifndef NO_CFRetain
JNIEXPORT void JNICALL OS_NATIVE(CFRetain)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRetain_FUNC);
	CFRetain((CFTypeRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRetain_FUNC);
}
#endif

#ifndef NO_CFRunLoopAddObserver
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopAddObserver)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopAddObserver_FUNC);
	CFRunLoopAddObserver((CFRunLoopRef)arg0, (CFRunLoopObserverRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, CFRunLoopAddObserver_FUNC);
}
#endif

#ifndef NO_CFRunLoopAddSource
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopAddSource)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopAddSource_FUNC);
	CFRunLoopAddSource((CFRunLoopRef)arg0, (CFRunLoopSourceRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, CFRunLoopAddSource_FUNC);
}
#endif

#ifndef NO_CFRunLoopObserverCreate
JNIEXPORT jint JNICALL OS_NATIVE(CFRunLoopObserverCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFRunLoopObserverCreate_FUNC);
	rc = (jint)CFRunLoopObserverCreate((CFAllocatorRef)arg0, (CFOptionFlags)arg1, arg2, (CFIndex)arg3, (CFRunLoopObserverCallBack)arg4, (CFRunLoopObserverContext *)arg5);
	OS_NATIVE_EXIT(env, that, CFRunLoopObserverCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CFRunLoopObserverInvalidate
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopObserverInvalidate)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopObserverInvalidate_FUNC);
	CFRunLoopObserverInvalidate((CFRunLoopObserverRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRunLoopObserverInvalidate_FUNC);
}
#endif

#ifndef NO_CFRunLoopRunInMode
JNIEXPORT jint JNICALL OS_NATIVE(CFRunLoopRunInMode)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFRunLoopRunInMode_FUNC);
	rc = (jint)CFRunLoopRunInMode((CFStringRef)arg0, (CFTimeInterval)arg1, arg2);
	OS_NATIVE_EXIT(env, that, CFRunLoopRunInMode_FUNC);
	return rc;
}
#endif

#ifndef NO_CFRunLoopSourceCreate
JNIEXPORT jint JNICALL OS_NATIVE(CFRunLoopSourceCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CFRunLoopSourceContext _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFRunLoopSourceCreate_FUNC);
	if (arg2) if ((lparg2 = getCFRunLoopSourceContextFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)CFRunLoopSourceCreate((CFAllocatorRef)arg0, (CFIndex)arg1, lparg2);
fail:
	if (arg2 && lparg2) setCFRunLoopSourceContextFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, CFRunLoopSourceCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CFRunLoopSourceInvalidate
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopSourceInvalidate)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopSourceInvalidate_FUNC);
	CFRunLoopSourceInvalidate((CFRunLoopSourceRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRunLoopSourceInvalidate_FUNC);
}
#endif

#ifndef NO_CFRunLoopSourceSignal
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopSourceSignal)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopSourceSignal_FUNC);
	CFRunLoopSourceSignal((CFRunLoopSourceRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRunLoopSourceSignal_FUNC);
}
#endif

#ifndef NO_CFRunLoopStop
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopStop)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopStop_FUNC);
	CFRunLoopStop((CFRunLoopRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRunLoopStop_FUNC);
}
#endif

#ifndef NO_CFRunLoopWakeUp
JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopWakeUp)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CFRunLoopWakeUp_FUNC);
	CFRunLoopWakeUp((CFRunLoopRef)arg0);
	OS_NATIVE_EXIT(env, that, CFRunLoopWakeUp_FUNC);
}
#endif

#ifndef NO_CFSetAddValue
JNIEXPORT void JNICALL OS_NATIVE(CFSetAddValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CFSetAddValue_FUNC);
	CFSetAddValue((CFMutableSetRef)arg0, (const void *)arg1);
	OS_NATIVE_EXIT(env, that, CFSetAddValue_FUNC);
}
#endif

#ifndef NO_CFSetCreateMutable
JNIEXPORT jint JNICALL OS_NATIVE(CFSetCreateMutable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFSetCreateMutable_FUNC);
	rc = (jint)CFSetCreateMutable((CFAllocatorRef)arg0, (CFIndex)arg1, (const CFSetCallBacks *)arg2);
	OS_NATIVE_EXIT(env, that, CFSetCreateMutable_FUNC);
	return rc;
}
#endif

#ifndef NO_CFSetGetCount
JNIEXPORT jint JNICALL OS_NATIVE(CFSetGetCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFSetGetCount_FUNC);
	rc = (jint)CFSetGetCount((CFMutableSetRef)arg0);
	OS_NATIVE_EXIT(env, that, CFSetGetCount_FUNC);
	return rc;
}
#endif

#ifndef NO_CFSetGetValues
JNIEXPORT void JNICALL OS_NATIVE(CFSetGetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CFSetGetValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CFSetGetValues((CFMutableSetRef)arg0, (const void **)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFSetGetValues_FUNC);
}
#endif

#ifndef NO_CFSetRemoveValue
JNIEXPORT void JNICALL OS_NATIVE(CFSetRemoveValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CFSetRemoveValue_FUNC);
	CFSetRemoveValue((CFMutableSetRef)arg0, (const void *)arg1);
	OS_NATIVE_EXIT(env, that, CFSetRemoveValue_FUNC);
}
#endif

#ifndef NO_CFStringCreateWithBytes
JNIEXPORT jint JNICALL OS_NATIVE(CFStringCreateWithBytes)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jboolean arg4)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringCreateWithBytes_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CFStringCreateWithBytes((CFAllocatorRef)arg0, (const UInt8 *)lparg1, (CFIndex)arg2, (CFStringEncoding)arg3, arg4);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFStringCreateWithBytes_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringCreateWithCharacters__III
JNIEXPORT jint JNICALL OS_NATIVE(CFStringCreateWithCharacters__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringCreateWithCharacters__III_FUNC);
	rc = (jint)CFStringCreateWithCharacters((CFAllocatorRef)arg0, (const UniChar *)arg1, (CFIndex)arg2);
	OS_NATIVE_EXIT(env, that, CFStringCreateWithCharacters__III_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringCreateWithCharacters__I_3CI
JNIEXPORT jint JNICALL OS_NATIVE(CFStringCreateWithCharacters__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringCreateWithCharacters__I_3CI_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CFStringCreateWithCharacters((CFAllocatorRef)arg0, (const UniChar *)lparg1, (CFIndex)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFStringCreateWithCharacters__I_3CI_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringGetBytes_FUNC);
	if (arg1) if ((lparg1 = getCFRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)CFStringGetBytes((CFStringRef)arg0, *(CFRange *)lparg1, (CFStringEncoding)arg2, (UInt8)arg3, (Boolean)arg4, (UInt8 *)lparg5, (CFIndex)arg6, (CFIndex *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg1 && lparg1) setCFRangeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CFStringGetBytes_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringGetCharacters
JNIEXPORT void JNICALL OS_NATIVE(CFStringGetCharacters)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jcharArray arg2)
{
	CFRange _arg1, *lparg1=NULL;
	jchar *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CFStringGetCharacters_FUNC);
	if (arg1) if ((lparg1 = getCFRangeFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CFStringGetCharacters((CFStringRef)arg0, *(CFRange *)lparg1, (UniChar *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCFRangeFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CFStringGetCharacters_FUNC);
}
#endif

#ifndef NO_CFStringGetLength
JNIEXPORT jint JNICALL OS_NATIVE(CFStringGetLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringGetLength_FUNC);
	rc = (jint)CFStringGetLength((CFStringRef)arg0);
	OS_NATIVE_EXIT(env, that, CFStringGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringGetSystemEncoding
JNIEXPORT jint JNICALL OS_NATIVE(CFStringGetSystemEncoding)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFStringGetSystemEncoding_FUNC);
	rc = (jint)CFStringGetSystemEncoding();
	OS_NATIVE_EXIT(env, that, CFStringGetSystemEncoding_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCopyFileSystemPath
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCopyFileSystemPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCopyFileSystemPath_FUNC);
	rc = (jint)CFURLCopyFileSystemPath((CFURLRef)arg0, (CFURLPathStyle)arg1);
	OS_NATIVE_EXIT(env, that, CFURLCopyFileSystemPath_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCopyLastPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCopyLastPathComponent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCopyLastPathComponent_FUNC);
	rc = (jint)CFURLCopyLastPathComponent((CFURLRef)arg0);
	OS_NATIVE_EXIT(env, that, CFURLCopyLastPathComponent_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCopyPathExtension
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCopyPathExtension)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCopyPathExtension_FUNC);
	rc = (jint)CFURLCopyPathExtension((CFURLRef)arg0);
	OS_NATIVE_EXIT(env, that, CFURLCopyPathExtension_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateCopyAppendingPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateCopyAppendingPathComponent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateCopyAppendingPathComponent_FUNC);
	rc = (jint)CFURLCreateCopyAppendingPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringRef)arg2, (Boolean)arg3);
	OS_NATIVE_EXIT(env, that, CFURLCreateCopyAppendingPathComponent_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateCopyAppendingPathExtension
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateCopyAppendingPathExtension)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateCopyAppendingPathExtension_FUNC);
	rc = (jint)CFURLCreateCopyAppendingPathExtension((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, CFURLCreateCopyAppendingPathExtension_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateCopyDeletingLastPathComponent
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateCopyDeletingLastPathComponent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateCopyDeletingLastPathComponent_FUNC);
	rc = (jint)CFURLCreateCopyDeletingLastPathComponent((CFAllocatorRef)arg0, (CFURLRef)arg1);
	OS_NATIVE_EXIT(env, that, CFURLCreateCopyDeletingLastPathComponent_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateData
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateData_FUNC);
	rc = (jint)CFURLCreateData((CFAllocatorRef)arg0, (CFURLRef)arg1, (CFStringEncoding)arg2, (Boolean)arg3);
	OS_NATIVE_EXIT(env, that, CFURLCreateData_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateFromFSRef
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateFromFSRef)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateFromFSRef_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CFURLCreateFromFSRef((CFAllocatorRef)arg0, (const struct FSRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFURLCreateFromFSRef_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateFromFileSystemRepresentation
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateFromFileSystemRepresentation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateFromFileSystemRepresentation_FUNC);
	rc = (jint)CFURLCreateFromFileSystemRepresentation((CFAllocatorRef)arg0, (const UInt8 *)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, CFURLCreateFromFileSystemRepresentation_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateStringByAddingPercentEscapes
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateStringByAddingPercentEscapes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateStringByAddingPercentEscapes_FUNC);
	rc = (jint)CFURLCreateStringByAddingPercentEscapes((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (CFStringRef)arg3, arg4);
	OS_NATIVE_EXIT(env, that, CFURLCreateStringByAddingPercentEscapes_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateStringByReplacingPercentEscapes
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateStringByReplacingPercentEscapes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateStringByReplacingPercentEscapes_FUNC);
	rc = (jint)CFURLCreateStringByReplacingPercentEscapes((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, CFURLCreateStringByReplacingPercentEscapes_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateWithBytes
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateWithBytes)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateWithBytes_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CFURLCreateWithBytes((CFAllocatorRef)arg0, (const UInt8 *)lparg1, (CFIndex)arg2, (CFStringEncoding)arg3, (CFURLRef)arg4);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFURLCreateWithBytes_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateWithFileSystemPath
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateWithFileSystemPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateWithFileSystemPath_FUNC);
	rc = (jint)CFURLCreateWithFileSystemPath((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFURLPathStyle)arg2, arg3);
	OS_NATIVE_EXIT(env, that, CFURLCreateWithFileSystemPath_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateWithString
JNIEXPORT jint JNICALL OS_NATIVE(CFURLCreateWithString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLCreateWithString_FUNC);
	rc = (jint)CFURLCreateWithString((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFURLRef)arg2);
	OS_NATIVE_EXIT(env, that, CFURLCreateWithString_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLGetFSRef
JNIEXPORT jboolean JNICALL OS_NATIVE(CFURLGetFSRef)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CFURLGetFSRef_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)CFURLGetFSRef((CFURLRef)arg0, (struct FSRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CFURLGetFSRef_FUNC);
	return rc;
}
#endif

#ifndef NO_CGBitmapContextCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGBitmapContextCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGBitmapContextCreate_FUNC);
	rc = (jint)CGBitmapContextCreate((void *)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6);
	OS_NATIVE_EXIT(env, that, CGBitmapContextCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CGBitmapContextCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(CGBitmapContextCreateImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGBitmapContextCreateImage_FUNC);
/*
	rc = (jint)CGBitmapContextCreateImage(arg0);
*/
	{
		LOAD_FUNCTION(fp, CGBitmapContextCreateImage)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, CGBitmapContextCreateImage_FUNC);
	return rc;
}
#endif

#ifndef NO_CGColorCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGColorCreate)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGColorCreate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CGColorCreate((CGColorSpaceRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGColorCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CGColorRelease
JNIEXPORT void JNICALL OS_NATIVE(CGColorRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGColorRelease_FUNC);
	CGColorRelease((CGColorRef)arg0);
	OS_NATIVE_EXIT(env, that, CGColorRelease_FUNC);
}
#endif

#ifndef NO_CGColorSpaceCreateDeviceRGB
JNIEXPORT jint JNICALL OS_NATIVE(CGColorSpaceCreateDeviceRGB)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGColorSpaceCreateDeviceRGB_FUNC);
	rc = (jint)CGColorSpaceCreateDeviceRGB();
	OS_NATIVE_EXIT(env, that, CGColorSpaceCreateDeviceRGB_FUNC);
	return rc;
}
#endif

#ifndef NO_CGColorSpaceCreatePattern
JNIEXPORT jint JNICALL OS_NATIVE(CGColorSpaceCreatePattern)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGColorSpaceCreatePattern_FUNC);
	rc = (jint)CGColorSpaceCreatePattern((CGColorSpaceRef)arg0);
	OS_NATIVE_EXIT(env, that, CGColorSpaceCreatePattern_FUNC);
	return rc;
}
#endif

#ifndef NO_CGColorSpaceRelease
JNIEXPORT void JNICALL OS_NATIVE(CGColorSpaceRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGColorSpaceRelease_FUNC);
	CGColorSpaceRelease((CGColorSpaceRef)arg0);
	OS_NATIVE_EXIT(env, that, CGColorSpaceRelease_FUNC);
}
#endif

#ifndef NO_CGContextAddArc
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddArc)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jboolean arg6)
{
	OS_NATIVE_ENTER(env, that, CGContextAddArc_FUNC);
	CGContextAddArc((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4, (float)arg5, (Boolean)arg6);
	OS_NATIVE_EXIT(env, that, CGContextAddArc_FUNC);
}
#endif

#ifndef NO_CGContextAddArcToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddArcToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	OS_NATIVE_ENTER(env, that, CGContextAddArcToPoint_FUNC);
	CGContextAddArcToPoint((CGContextRef)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, CGContextAddArcToPoint_FUNC);
}
#endif

#ifndef NO_CGContextAddLineToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddLineToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, CGContextAddLineToPoint_FUNC);
	CGContextAddLineToPoint((CGContextRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CGContextAddLineToPoint_FUNC);
}
#endif

#ifndef NO_CGContextAddLines
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddLines)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextAddLines_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextAddLines((CGContextRef)arg0, (const CGPoint *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextAddLines_FUNC);
}
#endif

#ifndef NO_CGContextAddPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextAddPath_FUNC);
	CGContextAddPath((CGContextRef)arg0, (CGPathRef)arg1);
	OS_NATIVE_EXIT(env, that, CGContextAddPath_FUNC);
}
#endif

#ifndef NO_CGContextAddRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextAddRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextAddRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	CGContextAddRect((CGContextRef)arg0, *lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextAddRect_FUNC);
}
#endif

#ifndef NO_CGContextBeginPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextBeginPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextBeginPath_FUNC);
	CGContextBeginPath((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextBeginPath_FUNC);
}
#endif

#ifndef NO_CGContextClearRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextClearRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextClearRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	CGContextClearRect((CGContextRef)arg0, *(CGRect *)lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextClearRect_FUNC);
}
#endif

#ifndef NO_CGContextClip
JNIEXPORT void JNICALL OS_NATIVE(CGContextClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextClip_FUNC);
	CGContextClip((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextClip_FUNC);
}
#endif

#ifndef NO_CGContextClosePath
JNIEXPORT void JNICALL OS_NATIVE(CGContextClosePath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextClosePath_FUNC);
	CGContextClosePath((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextClosePath_FUNC);
}
#endif

#ifndef NO_CGContextConcatCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextConcatCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextConcatCTM_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextConcatCTM((CGContextRef)arg0, *(CGAffineTransform *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextConcatCTM_FUNC);
}
#endif

#ifndef NO_CGContextDrawImage
JNIEXPORT void JNICALL OS_NATIVE(CGContextDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextDrawImage_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	CGContextDrawImage((CGContextRef)arg0, *(CGRect *)lparg1, (CGImageRef)arg2);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextDrawImage_FUNC);
}
#endif

#ifndef NO_CGContextDrawShading
JNIEXPORT void JNICALL OS_NATIVE(CGContextDrawShading)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextDrawShading_FUNC);
	CGContextDrawShading((CGContextRef)arg0, (CGShadingRef)arg1);
	OS_NATIVE_EXIT(env, that, CGContextDrawShading_FUNC);
}
#endif

#ifndef NO_CGContextEOClip
JNIEXPORT void JNICALL OS_NATIVE(CGContextEOClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextEOClip_FUNC);
	CGContextEOClip((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextEOClip_FUNC);
}
#endif

#ifndef NO_CGContextEOFillPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextEOFillPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextEOFillPath_FUNC);
	CGContextEOFillPath((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextEOFillPath_FUNC);
}
#endif

#ifndef NO_CGContextFillPath
JNIEXPORT void JNICALL OS_NATIVE(CGContextFillPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextFillPath_FUNC);
	CGContextFillPath((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextFillPath_FUNC);
}
#endif

#ifndef NO_CGContextFillRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextFillRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextFillRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	CGContextFillRect((CGContextRef)arg0, *(CGRect *)lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextFillRect_FUNC);
}
#endif

#ifndef NO_CGContextFlush
JNIEXPORT void JNICALL OS_NATIVE(CGContextFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextFlush_FUNC);
	CGContextFlush((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextFlush_FUNC);
}
#endif

#ifndef NO_CGContextGetInterpolationQuality
JNIEXPORT jint JNICALL OS_NATIVE(CGContextGetInterpolationQuality)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGContextGetInterpolationQuality_FUNC);
	rc = (jint)CGContextGetInterpolationQuality((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextGetInterpolationQuality_FUNC);
	return rc;
}
#endif

#ifndef NO_CGContextMoveToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextMoveToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, CGContextMoveToPoint_FUNC);
	CGContextMoveToPoint((CGContextRef)arg0, (float)arg1, (float)arg2);
	OS_NATIVE_EXIT(env, that, CGContextMoveToPoint_FUNC);
}
#endif

#ifndef NO_CGContextRelease
JNIEXPORT void JNICALL OS_NATIVE(CGContextRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextRelease_FUNC);
	CGContextRelease((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextRelease_FUNC);
}
#endif

#ifndef NO_CGContextRestoreGState
JNIEXPORT void JNICALL OS_NATIVE(CGContextRestoreGState)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextRestoreGState_FUNC);
	CGContextRestoreGState((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextRestoreGState_FUNC);
}
#endif

#ifndef NO_CGContextRotateCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextRotateCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextRotateCTM_FUNC);
	CGContextRotateCTM((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextRotateCTM_FUNC);
}
#endif

#ifndef NO_CGContextSaveGState
JNIEXPORT void JNICALL OS_NATIVE(CGContextSaveGState)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextSaveGState_FUNC);
	CGContextSaveGState((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextSaveGState_FUNC);
}
#endif

#ifndef NO_CGContextScaleCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextScaleCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, CGContextScaleCTM_FUNC);
	CGContextScaleCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
	OS_NATIVE_EXIT(env, that, CGContextScaleCTM_FUNC);
}
#endif

#ifndef NO_CGContextSelectFont
JNIEXPORT void JNICALL OS_NATIVE(CGContextSelectFont)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jfloat arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSelectFont_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextSelectFont((CGContextRef)arg0, (const char *)lparg1, (float)arg2, (CGTextEncoding)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextSelectFont_FUNC);
}
#endif

#ifndef NO_CGContextSetAlpha
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetAlpha)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetAlpha_FUNC);
	CGContextSetAlpha((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetAlpha_FUNC);
}
#endif

#ifndef NO_CGContextSetBlendMode
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetBlendMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetBlendMode_FUNC);
/*
	CGContextSetBlendMode((CGContextRef)arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, CGContextSetBlendMode)
		if (fp) {
			((void (CALLING_CONVENTION*)(CGContextRef, jint))fp)((CGContextRef)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, CGContextSetBlendMode_FUNC);
}
#endif

#ifndef NO_CGContextSetFillColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFillColor)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetFillColor_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextSetFillColor((CGContextRef)arg0, (const float *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetFillColor_FUNC);
}
#endif

#ifndef NO_CGContextSetFillColorSpace
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFillColorSpace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetFillColorSpace_FUNC);
	CGContextSetFillColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetFillColorSpace_FUNC);
}
#endif

#ifndef NO_CGContextSetFillPattern
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFillPattern)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetFillPattern_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CGContextSetFillPattern((CGContextRef)arg0, (CGPatternRef)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetFillPattern_FUNC);
}
#endif

#ifndef NO_CGContextSetFont
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetFont_FUNC);
	CGContextSetFont((CGContextRef)arg0, (CGFontRef)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetFont_FUNC);
}
#endif

#ifndef NO_CGContextSetFontSize
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetFontSize)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetFontSize_FUNC);
	CGContextSetFontSize((CGContextRef)arg0, (float)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetFontSize_FUNC);
}
#endif

#ifndef NO_CGContextSetInterpolationQuality
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetInterpolationQuality)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetInterpolationQuality_FUNC);
	CGContextSetInterpolationQuality((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetInterpolationQuality_FUNC);
}
#endif

#ifndef NO_CGContextSetLineCap
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineCap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetLineCap_FUNC);
	CGContextSetLineCap((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetLineCap_FUNC);
}
#endif

#ifndef NO_CGContextSetLineDash
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineDash)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloatArray arg2, jint arg3)
{
	jfloat *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetLineDash_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CGContextSetLineDash((CGContextRef)arg0, (float)arg1, (const float *)lparg2, (size_t)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetLineDash_FUNC);
}
#endif

#ifndef NO_CGContextSetLineJoin
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineJoin)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetLineJoin_FUNC);
	CGContextSetLineJoin((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetLineJoin_FUNC);
}
#endif

#ifndef NO_CGContextSetLineWidth
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetLineWidth)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetLineWidth_FUNC);
	CGContextSetLineWidth((CGContextRef)arg0, (float)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetLineWidth_FUNC);
}
#endif

#ifndef NO_CGContextSetMiterLimit
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetMiterLimit)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetMiterLimit_FUNC);
	CGContextSetMiterLimit((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetMiterLimit_FUNC);
}
#endif

#ifndef NO_CGContextSetRGBFillColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetRGBFillColor)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	OS_NATIVE_ENTER(env, that, CGContextSetRGBFillColor_FUNC);
	CGContextSetRGBFillColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
	OS_NATIVE_EXIT(env, that, CGContextSetRGBFillColor_FUNC);
}
#endif

#ifndef NO_CGContextSetRGBStrokeColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetRGBStrokeColor)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4)
{
	OS_NATIVE_ENTER(env, that, CGContextSetRGBStrokeColor_FUNC);
	CGContextSetRGBStrokeColor((CGContextRef)arg0, (float)arg1, (float)arg2, (float)arg3, (float)arg4);
	OS_NATIVE_EXIT(env, that, CGContextSetRGBStrokeColor_FUNC);
}
#endif

#ifndef NO_CGContextSetRenderingIntent
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetRenderingIntent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetRenderingIntent_FUNC);
	CGContextSetRenderingIntent((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetRenderingIntent_FUNC);
}
#endif

#ifndef NO_CGContextSetShouldAntialias
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetShouldAntialias)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetShouldAntialias_FUNC);
	CGContextSetShouldAntialias((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetShouldAntialias_FUNC);
}
#endif

#ifndef NO_CGContextSetShouldSmoothFonts
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetShouldSmoothFonts)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetShouldSmoothFonts_FUNC);
	CGContextSetShouldSmoothFonts((CGContextRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetShouldSmoothFonts_FUNC);
}
#endif

#ifndef NO_CGContextSetStrokeColor
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetStrokeColor)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetStrokeColor_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextSetStrokeColor((CGContextRef)arg0, (const float *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetStrokeColor_FUNC);
}
#endif

#ifndef NO_CGContextSetStrokeColorSpace
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetStrokeColorSpace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetStrokeColorSpace_FUNC);
	CGContextSetStrokeColorSpace((CGContextRef)arg0, (CGColorSpaceRef)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetStrokeColorSpace_FUNC);
}
#endif

#ifndef NO_CGContextSetStrokePattern
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetStrokePattern)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetStrokePattern_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	CGContextSetStrokePattern((CGContextRef)arg0, (CGPatternRef)arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetStrokePattern_FUNC);
}
#endif

#ifndef NO_CGContextSetTextDrawingMode
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextDrawingMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CGContextSetTextDrawingMode_FUNC);
	CGContextSetTextDrawingMode((CGContextRef)arg0, (CGTextDrawingMode)arg1);
	OS_NATIVE_EXIT(env, that, CGContextSetTextDrawingMode_FUNC);
}
#endif

#ifndef NO_CGContextSetTextMatrix
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextMatrix)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextSetTextMatrix_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextSetTextMatrix((CGContextRef)arg0, *(CGAffineTransform *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextSetTextMatrix_FUNC);
}
#endif

#ifndef NO_CGContextSetTextPosition
JNIEXPORT void JNICALL OS_NATIVE(CGContextSetTextPosition)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, CGContextSetTextPosition_FUNC);
	CGContextSetTextPosition((CGContextRef)arg0, (float)arg1, (float)arg2);
	OS_NATIVE_EXIT(env, that, CGContextSetTextPosition_FUNC);
}
#endif

#ifndef NO_CGContextShowText
JNIEXPORT void JNICALL OS_NATIVE(CGContextShowText)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextShowText_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGContextShowText((CGContextRef)arg0, (const char *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextShowText_FUNC);
}
#endif

#ifndef NO_CGContextShowTextAtPoint
JNIEXPORT void JNICALL OS_NATIVE(CGContextShowTextAtPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, CGContextShowTextAtPoint_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	CGContextShowTextAtPoint((CGContextRef)arg0, (float)arg1, (float)arg2, (const char *)lparg3, (size_t)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, CGContextShowTextAtPoint_FUNC);
}
#endif

#ifndef NO_CGContextStrokePath
JNIEXPORT void JNICALL OS_NATIVE(CGContextStrokePath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextStrokePath_FUNC);
	CGContextStrokePath((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextStrokePath_FUNC);
}
#endif

#ifndef NO_CGContextStrokeRect
JNIEXPORT void JNICALL OS_NATIVE(CGContextStrokeRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextStrokeRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	CGContextStrokeRect((CGContextRef)arg0, *(CGRect *)lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextStrokeRect_FUNC);
}
#endif

#ifndef NO_CGContextSynchronize
JNIEXPORT void JNICALL OS_NATIVE(CGContextSynchronize)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGContextSynchronize_FUNC);
	CGContextSynchronize((CGContextRef)arg0);
	OS_NATIVE_EXIT(env, that, CGContextSynchronize_FUNC);
}
#endif

#ifndef NO_CGContextTranslateCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextTranslateCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	OS_NATIVE_ENTER(env, that, CGContextTranslateCTM_FUNC);
	CGContextTranslateCTM((CGContextRef)arg0, (float)arg1, (float)arg2);
	OS_NATIVE_EXIT(env, that, CGContextTranslateCTM_FUNC);
}
#endif

#ifndef NO_CGCursorIsVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(CGCursorIsVisible)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CGCursorIsVisible_FUNC);
	rc = (jboolean)CGCursorIsVisible();
	OS_NATIVE_EXIT(env, that, CGCursorIsVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDataProviderCreateWithData
JNIEXPORT jint JNICALL OS_NATIVE(CGDataProviderCreateWithData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDataProviderCreateWithData_FUNC);
	rc = (jint)CGDataProviderCreateWithData((void *)arg0, (const void *)arg1, (size_t)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, CGDataProviderCreateWithData_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDataProviderCreateWithURL
JNIEXPORT jint JNICALL OS_NATIVE(CGDataProviderCreateWithURL)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDataProviderCreateWithURL_FUNC);
	rc = (jint)CGDataProviderCreateWithURL((CFURLRef)arg0);
	OS_NATIVE_EXIT(env, that, CGDataProviderCreateWithURL_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDataProviderRelease
JNIEXPORT void JNICALL OS_NATIVE(CGDataProviderRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGDataProviderRelease_FUNC);
	CGDataProviderRelease((CGDataProviderRef)arg0);
	OS_NATIVE_EXIT(env, that, CGDataProviderRelease_FUNC);
}
#endif

#ifndef NO_CGDisplayBaseAddress
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayBaseAddress)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayBaseAddress_FUNC);
	rc = (jint)CGDisplayBaseAddress((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayBaseAddress_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayBitsPerPixel
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayBitsPerPixel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayBitsPerPixel_FUNC);
	rc = (jint)CGDisplayBitsPerPixel((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayBitsPerPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayBitsPerSample
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayBitsPerSample)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayBitsPerSample_FUNC);
	rc = (jint)CGDisplayBitsPerSample((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayBitsPerSample_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayBytesPerRow
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayBytesPerRow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayBytesPerRow_FUNC);
	rc = (jint)CGDisplayBytesPerRow((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayBytesPerRow_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayHideCursor
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayHideCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayHideCursor_FUNC);
	rc = (jint)CGDisplayHideCursor((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayHideCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayPixelsHigh
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayPixelsHigh)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayPixelsHigh_FUNC);
	rc = (jint)CGDisplayPixelsHigh((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayPixelsHigh_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayPixelsWide
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayPixelsWide)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayPixelsWide_FUNC);
	rc = (jint)CGDisplayPixelsWide((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayPixelsWide_FUNC);
	return rc;
}
#endif

#ifndef NO_CGDisplayShowCursor
JNIEXPORT jint JNICALL OS_NATIVE(CGDisplayShowCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGDisplayShowCursor_FUNC);
	rc = (jint)CGDisplayShowCursor((CGDirectDisplayID)arg0);
	OS_NATIVE_EXIT(env, that, CGDisplayShowCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_CGFontCreateWithPlatformFont
JNIEXPORT jint JNICALL OS_NATIVE(CGFontCreateWithPlatformFont)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGFontCreateWithPlatformFont_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CGFontCreateWithPlatformFont(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CGFontCreateWithPlatformFont_FUNC);
	return rc;
}
#endif

#ifndef NO_CGFontRelease
JNIEXPORT void JNICALL OS_NATIVE(CGFontRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGFontRelease_FUNC);
	CGFontRelease((CGFontRef)arg0);
	OS_NATIVE_EXIT(env, that, CGFontRelease_FUNC);
}
#endif

#ifndef NO_CGFunctionCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGFunctionCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jobject arg5)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;
	CGFunctionCallbacks _arg5, *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGFunctionCreate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getCGFunctionCallbacksFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)CGFunctionCreate((void *)arg0, (size_t)arg1, (const float *)lparg2, (size_t)arg3, (const float *)lparg4, (const CGFunctionCallbacks *)lparg5);
fail:
	if (arg5 && lparg5) setCGFunctionCallbacksFields(env, arg5, lparg5);
	if (arg4 && lparg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CGFunctionCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CGFunctionRelease
JNIEXPORT void JNICALL OS_NATIVE(CGFunctionRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGFunctionRelease_FUNC);
	CGFunctionRelease((CGFunctionRef)arg0);
	OS_NATIVE_EXIT(env, that, CGFunctionRelease_FUNC);
}
#endif

#ifndef NO_CGGetDisplaysWithRect
JNIEXPORT jint JNICALL OS_NATIVE(CGGetDisplaysWithRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	CGRect _arg0, *lparg0=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGGetDisplaysWithRect_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)CGGetDisplaysWithRect(*lparg0, (CGDisplayCount)arg1, (CGDirectDisplayID *)lparg2, (CGDisplayCount *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, CGGetDisplaysWithRect_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGImageCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jfloatArray arg8, jboolean arg9, jint arg10)
{
	jfloat *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageCreate_FUNC);
	if (arg8) if ((lparg8 = (*env)->GetFloatArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)CGImageCreate((size_t)arg0, (size_t)arg1, (size_t)arg2, (size_t)arg3, (size_t)arg4, (CGColorSpaceRef)arg5, (CGImageAlphaInfo)arg6, (CGDataProviderRef)arg7, (const float *)lparg8, (Boolean)arg9, (CGColorRenderingIntent)arg10);
fail:
	if (arg8 && lparg8) (*env)->ReleaseFloatArrayElements(env, arg8, lparg8, 0);
	OS_NATIVE_EXIT(env, that, CGImageCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageCreateWithImageInRect
JNIEXPORT jint JNICALL OS_NATIVE(CGImageCreateWithImageInRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageCreateWithImageInRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
/*
	rc = (jint)CGImageCreateWithImageInRect(arg0, *lparg1);
*/
	{
		LOAD_FUNCTION(fp, CGImageCreateWithImageInRect)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, CGRect))fp)(arg0, *lparg1);
		}
	}
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGImageCreateWithImageInRect_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageCreateWithJPEGDataProvider
JNIEXPORT jint JNICALL OS_NATIVE(CGImageCreateWithJPEGDataProvider)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jboolean arg2, jint arg3)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageCreateWithJPEGDataProvider_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CGImageCreateWithJPEGDataProvider((CGDataProviderRef)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGImageCreateWithJPEGDataProvider_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageCreateWithPNGDataProvider
JNIEXPORT jint JNICALL OS_NATIVE(CGImageCreateWithPNGDataProvider)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jboolean arg2, jint arg3)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageCreateWithPNGDataProvider_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CGImageCreateWithPNGDataProvider((CGDataProviderRef)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGImageCreateWithPNGDataProvider_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetAlphaInfo
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetAlphaInfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetAlphaInfo_FUNC);
	rc = (jint)CGImageGetAlphaInfo((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetAlphaInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetBitsPerComponent
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBitsPerComponent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetBitsPerComponent_FUNC);
	rc = (jint)CGImageGetBitsPerComponent((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetBitsPerComponent_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetBitsPerPixel
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBitsPerPixel)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetBitsPerPixel_FUNC);
	rc = (jint)CGImageGetBitsPerPixel((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetBitsPerPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetBytesPerRow
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetBytesPerRow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetBytesPerRow_FUNC);
	rc = (jint)CGImageGetBytesPerRow((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetBytesPerRow_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetColorSpace
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetColorSpace)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetColorSpace_FUNC);
	rc = (jint)CGImageGetColorSpace((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetColorSpace_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetDataProvider
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetDataProvider)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetDataProvider_FUNC);
	rc = (jint)CGImageGetDataProvider((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetDataProvider_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetHeight
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetHeight_FUNC);
	rc = (jint)CGImageGetHeight((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageGetWidth
JNIEXPORT jint JNICALL OS_NATIVE(CGImageGetWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGImageGetWidth_FUNC);
	rc = (jint)CGImageGetWidth((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageGetWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_CGImageRelease
JNIEXPORT void JNICALL OS_NATIVE(CGImageRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGImageRelease_FUNC);
	CGImageRelease((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, CGImageRelease_FUNC);
}
#endif

#ifndef NO_CGMainDisplayID
JNIEXPORT jint JNICALL OS_NATIVE(CGMainDisplayID)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGMainDisplayID_FUNC);
	rc = (jint)CGMainDisplayID();
	OS_NATIVE_EXIT(env, that, CGMainDisplayID_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPathAddArc
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddArc)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6, jboolean arg7)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddArc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathAddArc((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, arg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddArc_FUNC);
}
#endif

#ifndef NO_CGPathAddCurveToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddCurveToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloat arg6, jfloat arg7)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddCurveToPoint_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathAddCurveToPoint((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, arg2, arg3, arg4, arg5, arg6, arg7);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddCurveToPoint_FUNC);
}
#endif

#ifndef NO_CGPathAddLineToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddLineToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloat arg2, jfloat arg3)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddLineToPoint_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathAddLineToPoint((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddLineToPoint_FUNC);
}
#endif

#ifndef NO_CGPathAddPath
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddPath)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddPath_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathAddPath((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, (CGPathRef)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddPath_FUNC);
}
#endif

#ifndef NO_CGPathAddQuadCurveToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddQuadCurveToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddQuadCurveToPoint_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathAddQuadCurveToPoint((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, arg2, arg3, arg4, arg5);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddQuadCurveToPoint_FUNC);
}
#endif

#ifndef NO_CGPathAddRect
JNIEXPORT void JNICALL OS_NATIVE(CGPathAddRect)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jobject arg2)
{
	jfloat *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGPathAddRect_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	CGPathAddRect((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, *lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathAddRect_FUNC);
}
#endif

#ifndef NO_CGPathApply
JNIEXPORT void JNICALL OS_NATIVE(CGPathApply)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, CGPathApply_FUNC);
	CGPathApply((CGPathRef)arg0, (void *)arg1, (CGPathApplierFunction)arg2);
	OS_NATIVE_EXIT(env, that, CGPathApply_FUNC);
}
#endif

#ifndef NO_CGPathCloseSubpath
JNIEXPORT void JNICALL OS_NATIVE(CGPathCloseSubpath)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGPathCloseSubpath_FUNC);
	CGPathCloseSubpath((CGMutablePathRef)arg0);
	OS_NATIVE_EXIT(env, that, CGPathCloseSubpath_FUNC);
}
#endif

#ifndef NO_CGPathCreateMutable
JNIEXPORT jint JNICALL OS_NATIVE(CGPathCreateMutable)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPathCreateMutable_FUNC);
	rc = (jint)CGPathCreateMutable();
	OS_NATIVE_EXIT(env, that, CGPathCreateMutable_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPathCreateMutableCopy
JNIEXPORT jint JNICALL OS_NATIVE(CGPathCreateMutableCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPathCreateMutableCopy_FUNC);
	rc = (jint)CGPathCreateMutableCopy((CGPathRef)arg0);
	OS_NATIVE_EXIT(env, that, CGPathCreateMutableCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPathIsEmpty
JNIEXPORT jboolean JNICALL OS_NATIVE(CGPathIsEmpty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, CGPathIsEmpty_FUNC);
	rc = (jboolean)CGPathIsEmpty((CGPathRef)arg0);
	OS_NATIVE_EXIT(env, that, CGPathIsEmpty_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPathMoveToPoint
JNIEXPORT void JNICALL OS_NATIVE(CGPathMoveToPoint)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloat arg2, jfloat arg3)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathMoveToPoint_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	CGPathMoveToPoint((CGMutablePathRef)arg0, (const CGAffineTransform *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGPathMoveToPoint_FUNC);
}
#endif

#ifndef NO_CGPathRelease
JNIEXPORT void JNICALL OS_NATIVE(CGPathRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGPathRelease_FUNC);
	CGPathRelease((CGPathRef)arg0);
	OS_NATIVE_EXIT(env, that, CGPathRelease_FUNC);
}
#endif

#ifndef NO_CGPatternCreate
JNIEXPORT jint JNICALL OS_NATIVE(CGPatternCreate)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jfloatArray arg2, jfloat arg3, jfloat arg4, jint arg5, jint arg6, jobject arg7)
{
	CGRect _arg1, *lparg1=NULL;
	jfloat *lparg2=NULL;
	CGPatternCallbacks _arg7, *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPatternCreate_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getCGPatternCallbacksFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)CGPatternCreate((void *)arg0, *lparg1, *(CGAffineTransform *)lparg2, arg3, arg4, (CGPatternTiling)arg5, arg6, (const CGPatternCallbacks *)lparg7);
fail:
	if (arg7 && lparg7) setCGPatternCallbacksFields(env, arg7, lparg7);
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGPatternCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPatternRelease
JNIEXPORT void JNICALL OS_NATIVE(CGPatternRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGPatternRelease_FUNC);
	CGPatternRelease((CGPatternRef)arg0);
	OS_NATIVE_EXIT(env, that, CGPatternRelease_FUNC);
}
#endif

#ifndef NO_CGPostKeyboardEvent
JNIEXPORT jint JNICALL OS_NATIVE(CGPostKeyboardEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPostKeyboardEvent_FUNC);
	rc = (jint)CGPostKeyboardEvent((CGCharCode)arg0, (CGKeyCode)arg1, (boolean_t)arg2);
	OS_NATIVE_EXIT(env, that, CGPostKeyboardEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPostMouseEvent
JNIEXPORT jint JNICALL OS_NATIVE(CGPostMouseEvent)
	(JNIEnv *env, jclass that, jobject arg0, jboolean arg1, jint arg2, jboolean arg3, jboolean arg4, jboolean arg5, jboolean arg6, jboolean arg7)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPostMouseEvent_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)CGPostMouseEvent(*lparg0, (boolean_t)arg1, arg2, (boolean_t)arg3, (boolean_t)arg4, (boolean_t)arg5, (boolean_t)arg6, (boolean_t)arg7);
fail:
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, CGPostMouseEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_CGPostScrollWheelEvent
JNIEXPORT jint JNICALL OS_NATIVE(CGPostScrollWheelEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGPostScrollWheelEvent_FUNC);
	rc = (jint)CGPostScrollWheelEvent(arg0, arg1);
	OS_NATIVE_EXIT(env, that, CGPostScrollWheelEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_CGRectContainsPoint
JNIEXPORT jint JNICALL OS_NATIVE(CGRectContainsPoint)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	CGRect _arg0, *lparg0=NULL;
	CGPoint _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGRectContainsPoint_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)CGRectContainsPoint(*lparg0, *lparg1);
fail:
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, CGRectContainsPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_CGShadingCreateAxial
JNIEXPORT jint JNICALL OS_NATIVE(CGShadingCreateAxial)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jint arg3, jboolean arg4, jboolean arg5)
{
	CGPoint _arg1, *lparg1=NULL;
	CGPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGShadingCreateAxial_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)CGShadingCreateAxial((CGColorSpaceRef)arg0, *lparg1, *lparg2, (CGFunctionRef)arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGShadingCreateAxial_FUNC);
	return rc;
}
#endif

#ifndef NO_CGShadingCreateRadial
JNIEXPORT jint JNICALL OS_NATIVE(CGShadingCreateRadial)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jfloat arg2, jobject arg3, jfloat arg4, jint arg5, jboolean arg6, jboolean arg7)
{
	CGPoint _arg1, *lparg1=NULL;
	CGPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CGShadingCreateRadial_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getCGPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)CGShadingCreateRadial((CGColorSpaceRef)arg0, *lparg1, arg2, *lparg3, arg4, (CGFunctionRef)arg5, arg6, arg7);
fail:
	if (arg3 && lparg3) setCGPointFields(env, arg3, lparg3);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGShadingCreateRadial_FUNC);
	return rc;
}
#endif

#ifndef NO_CGShadingRelease
JNIEXPORT void JNICALL OS_NATIVE(CGShadingRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CGShadingRelease_FUNC);
	CGShadingRelease((CGShadingRef)arg0);
	OS_NATIVE_EXIT(env, that, CGShadingRelease_FUNC);
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

#ifndef NO_CPSEnableForegroundOperation
JNIEXPORT jint JNICALL OS_NATIVE(CPSEnableForegroundOperation)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CPSEnableForegroundOperation_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)CPSEnableForegroundOperation(lparg0, arg1, arg2, arg3, arg4);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CPSEnableForegroundOperation_FUNC);
	return rc;
}
#endif

#ifndef NO_CPSSetProcessName
JNIEXPORT jint JNICALL OS_NATIVE(CPSSetProcessName)
	(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1)
{
	jint *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CPSSetProcessName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CPSSetProcessName(lparg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, CPSSetProcessName_FUNC);
	return rc;
}
#endif

#ifndef NO_CalcMenuSize
JNIEXPORT void JNICALL OS_NATIVE(CalcMenuSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CalcMenuSize_FUNC);
	CalcMenuSize((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, CalcMenuSize_FUNC);
}
#endif

#ifndef NO_Call
JNIEXPORT void JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, Call_FUNC);
	((void (*)())arg0)(arg1, arg2);
	OS_NATIVE_EXIT(env, that, Call_FUNC);
}
#endif

#ifndef NO_CallNextEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(CallNextEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CallNextEventHandler_FUNC);
	rc = (jint)CallNextEventHandler((EventHandlerCallRef)arg0, (EventRef)arg1);
	OS_NATIVE_EXIT(env, that, CallNextEventHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_CancelMenuTracking
JNIEXPORT jint JNICALL OS_NATIVE(CancelMenuTracking)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CancelMenuTracking_FUNC);
	rc = (jint)CancelMenuTracking((MenuRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, CancelMenuTracking_FUNC);
	return rc;
}
#endif

#ifndef NO_ChangeMenuItemAttributes
JNIEXPORT jint JNICALL OS_NATIVE(ChangeMenuItemAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ChangeMenuItemAttributes_FUNC);
	rc = (jint)ChangeMenuItemAttributes((MenuRef)arg0, (MenuItemIndex)arg1, (MenuItemAttributes)arg2, (MenuItemAttributes)arg3);
	OS_NATIVE_EXIT(env, that, ChangeMenuItemAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_ChangeWindowAttributes
JNIEXPORT jint JNICALL OS_NATIVE(ChangeWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ChangeWindowAttributes_FUNC);
	rc = (jint)ChangeWindowAttributes((WindowRef)arg0, (WindowAttributes)arg1, (WindowAttributes)arg2);
	OS_NATIVE_EXIT(env, that, ChangeWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_ClearCurrentScrap
JNIEXPORT jint JNICALL OS_NATIVE(ClearCurrentScrap)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ClearCurrentScrap_FUNC);
	rc = (jint)ClearCurrentScrap();
	OS_NATIVE_EXIT(env, that, ClearCurrentScrap_FUNC);
	return rc;
}
#endif

#ifndef NO_ClearKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(ClearKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ClearKeyboardFocus_FUNC);
	rc = (jint)ClearKeyboardFocus((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, ClearKeyboardFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_ClearMenuBar
JNIEXPORT void JNICALL OS_NATIVE(ClearMenuBar)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, ClearMenuBar_FUNC);
	ClearMenuBar();
	OS_NATIVE_EXIT(env, that, ClearMenuBar_FUNC);
}
#endif

#ifndef NO_ClipCGContextToRegion
JNIEXPORT jint JNICALL OS_NATIVE(ClipCGContextToRegion)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ClipCGContextToRegion_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)ClipCGContextToRegion((CGContextRef)arg0, (const Rect *)lparg1, (RgnHandle)arg2);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ClipCGContextToRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_CloseDataBrowserContainer
JNIEXPORT jint JNICALL OS_NATIVE(CloseDataBrowserContainer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CloseDataBrowserContainer_FUNC);
	rc = (jint)CloseDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
	OS_NATIVE_EXIT(env, that, CloseDataBrowserContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_ClosePicture
JNIEXPORT void JNICALL OS_NATIVE(ClosePicture)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, ClosePicture_FUNC);
	ClosePicture();
	OS_NATIVE_EXIT(env, that, ClosePicture_FUNC);
}
#endif

#ifndef NO_CloseRgn
JNIEXPORT void JNICALL OS_NATIVE(CloseRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, CloseRgn_FUNC);
	CloseRgn((RgnHandle)arg0);
	OS_NATIVE_EXIT(env, that, CloseRgn_FUNC);
}
#endif

#ifndef NO_CollapseWindow
JNIEXPORT jint JNICALL OS_NATIVE(CollapseWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CollapseWindow_FUNC);
	rc = (jint)CollapseWindow((WindowRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, CollapseWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ContextualMenuSelect
JNIEXPORT jint JNICALL OS_NATIVE(ContextualMenuSelect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2, jint arg3, jbyteArray arg4, jobject arg5, jintArray arg6, jshortArray arg7, jshortArray arg8)
{
	Point _arg1, *lparg1=NULL;
	jbyte *lparg4=NULL;
	AEDesc _arg5, *lparg5=NULL;
	jint *lparg6=NULL;
	jshort *lparg7=NULL;
	jshort *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ContextualMenuSelect_FUNC);
	if (arg1) if ((lparg1 = getPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getAEDescFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetShortArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetShortArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)ContextualMenuSelect((MenuRef)arg0, *lparg1, arg2, arg3, (ConstStr255Param)lparg4, lparg5, (UInt32 *)lparg6, (SInt16 *)lparg7, (MenuItemIndex *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseShortArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseShortArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) setAEDescFields(env, arg5, lparg5);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) setPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ContextualMenuSelect_FUNC);
	return rc;
}
#endif

#ifndef NO_ConvertEventRefToEventRecord
JNIEXPORT jboolean JNICALL OS_NATIVE(ConvertEventRefToEventRecord)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	EventRecord _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, ConvertEventRefToEventRecord_FUNC);
	if (arg1) if ((lparg1 = getEventRecordFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)ConvertEventRefToEventRecord((EventRef)arg0, (EventRecord *)lparg1);
fail:
	if (arg1 && lparg1) setEventRecordFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, ConvertEventRefToEventRecord_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ConvertFromPStringToUnicode_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ConvertFromPStringToUnicode((TextToUnicodeInfo)arg0, (ConstStr255Param)lparg1, arg2, (ByteCount *)lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ConvertFromPStringToUnicode_FUNC);
	return rc;
}
#endif

#ifndef NO_ConvertFromUnicodeToPString
JNIEXPORT jint JNICALL OS_NATIVE(ConvertFromUnicodeToPString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jbyteArray arg3)
{
	jchar *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ConvertFromUnicodeToPString_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)ConvertFromUnicodeToPString((UnicodeToTextInfo)arg0, arg1, (ConstUniCharArrayPtr)lparg2, (unsigned char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, ConvertFromUnicodeToPString_FUNC);
	return rc;
}
#endif

#ifndef NO_CopyBits
JNIEXPORT void JNICALL OS_NATIVE(CopyBits)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jshort arg4, jint arg5)
{
	Rect _arg2, *lparg2=NULL;
	Rect _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, CopyBits_FUNC);
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	CopyBits((const BitMap *)arg0, (const BitMap *)arg1, (const Rect *)lparg2, (const Rect *)lparg3, (short)arg4, (RgnHandle)arg5);
fail:
	if (arg3 && lparg3) setRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, CopyBits_FUNC);
}
#endif

#ifndef NO_CopyControlTitleAsCFString
JNIEXPORT jint JNICALL OS_NATIVE(CopyControlTitleAsCFString)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CopyControlTitleAsCFString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CopyControlTitleAsCFString((ControlRef)arg0, (CFStringRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CopyControlTitleAsCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_CopyMenuItemTextAsCFString
JNIEXPORT jint JNICALL OS_NATIVE(CopyMenuItemTextAsCFString)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CopyMenuItemTextAsCFString_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CopyMenuItemTextAsCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CopyMenuItemTextAsCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_CopyRgn
JNIEXPORT void JNICALL OS_NATIVE(CopyRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, CopyRgn_FUNC);
	CopyRgn((RgnHandle)arg0, (RgnHandle)arg1);
	OS_NATIVE_EXIT(env, that, CopyRgn_FUNC);
}
#endif

#ifndef NO_CountDragItemFlavors
JNIEXPORT jint JNICALL OS_NATIVE(CountDragItemFlavors)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CountDragItemFlavors_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CountDragItemFlavors((DragRef)arg0, (DragItemRef)arg1, (UInt16 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CountDragItemFlavors_FUNC);
	return rc;
}
#endif

#ifndef NO_CountDragItems
JNIEXPORT jint JNICALL OS_NATIVE(CountDragItems)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CountDragItems_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CountDragItems((DragRef)arg0, (UInt16 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CountDragItems_FUNC);
	return rc;
}
#endif

#ifndef NO_CountMenuItems
JNIEXPORT jshort JNICALL OS_NATIVE(CountMenuItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, CountMenuItems_FUNC);
	rc = (jshort)CountMenuItems((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, CountMenuItems_FUNC);
	return rc;
}
#endif

#ifndef NO_CountSubControls
JNIEXPORT jint JNICALL OS_NATIVE(CountSubControls)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CountSubControls_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CountSubControls((ControlRef)arg0, (UInt16 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CountSubControls_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateBevelButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateBevelButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jshort arg4, jint arg5, jshort arg6, jshort arg7, jshort arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateBevelButtonControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)CreateBevelButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlBevelThickness)arg3, (ControlBevelButtonBehavior)arg4, (ControlButtonContentInfoPtr)arg5, (SInt16)arg6, (ControlBevelButtonMenuBehavior)arg7, (ControlBevelButtonMenuPlacement)arg8, (ControlRef *)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateBevelButtonControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCGContextForPort
JNIEXPORT jint JNICALL OS_NATIVE(CreateCGContextForPort)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCGContextForPort_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateCGContextForPort((CGrafPtr)arg0, (CGContextRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateCGContextForPort_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateCheckBoxControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateCheckBoxControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateCheckBoxControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)CreateCheckBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateCheckBoxControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateClockControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateClockControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateClockControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreateClockControl((WindowRef)arg0, (const Rect *)lparg1, (ControlClockType)arg2, (ControlClockFlags)arg3, (ControlRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateClockControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateDataBrowserControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateDataBrowserControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateDataBrowserControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)CreateDataBrowserControl((WindowRef)arg0, (const Rect *)lparg1, (DataBrowserViewStyle)arg2, (ControlRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateDataBrowserControl_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateEditUnicodeTextControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getControlFontStyleRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)CreateEditUnicodeTextControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, arg3, lparg4, (ControlRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setControlFontStyleRecFields(env, arg4, lparg4);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateEditUnicodeTextControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateEvent
JNIEXPORT jint JNICALL OS_NATIVE(CreateEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jdouble arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateEvent_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)CreateEvent((CFAllocatorRef)arg0, (UInt32)arg1, (UInt32)arg2, (EventTime)arg3, (EventAttributes)arg4, (EventRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, CreateEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateGroupBoxControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateGroupBoxControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateGroupBoxControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreateGroupBoxControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (Boolean)arg3, (ControlRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateGroupBoxControl_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateIconControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getControlButtonContentInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreateIconControl((WindowRef)arg0, lparg1, lparg2, arg3, (ControlRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setControlButtonContentInfoFields(env, arg2, lparg2);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateIconControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateLittleArrowsControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateLittleArrowsControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateLittleArrowsControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)CreateLittleArrowsControl((WindowRef)arg0, (const Rect *)lparg1, arg2, arg3, arg4, arg5, (ControlRef *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateLittleArrowsControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateNewMenu
JNIEXPORT jint JNICALL OS_NATIVE(CreateNewMenu)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateNewMenu_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CreateNewMenu((MenuID)arg0, (MenuAttributes)arg1, (MenuRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, CreateNewMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateNewWindow
JNIEXPORT jint JNICALL OS_NATIVE(CreateNewWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	Rect _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateNewWindow_FUNC);
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)CreateNewWindow((WindowClass)arg0, (WindowAttributes)arg1, (const Rect *)lparg2, (WindowRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, CreateNewWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePopupArrowControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupArrowControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jintArray arg4)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePopupArrowControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreatePopupArrowControl((WindowRef)arg0, (const Rect *)lparg1, (ControlPopupArrowOrientation)arg2, (ControlPopupArrowSize)arg3, (ControlRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreatePopupArrowControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePopupButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePopupButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jshort arg3, jboolean arg4, jshort arg5, jshort arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePopupButtonControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)CreatePopupButtonControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, arg3, arg4, arg5, arg6, arg7, (ControlRef *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreatePopupButtonControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateProgressBarControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateProgressBarControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateProgressBarControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)CreateProgressBarControl((WindowRef)arg0, lparg1, arg2, arg3, arg4, arg5, (ControlRef *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateProgressBarControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreatePushButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreatePushButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePushButtonControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)CreatePushButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (ControlRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreatePushButtonControl_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreatePushButtonWithIconControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getControlButtonContentInfoFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)CreatePushButtonWithIconControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, (ControlButtonContentInfo *)lparg3, (ControlPushButtonIconAlignment)arg4, (ControlRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) setControlButtonContentInfoFields(env, arg3, lparg3);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreatePushButtonWithIconControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateRadioButtonControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateRadioButtonControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jboolean arg4, jintArray arg5)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateRadioButtonControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)CreateRadioButtonControl((WindowRef)arg0, (const Rect *)lparg1, (CFStringRef)arg2, (SInt32)arg3, (Boolean)arg4, (ControlRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateRadioButtonControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateRootControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateRootControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateRootControl_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateRootControl((WindowRef)arg0, (ControlRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateRootControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateScrollBarControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateScrollBarControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6, jint arg7, jintArray arg8)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateScrollBarControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)CreateScrollBarControl((WindowRef)arg0, lparg1, arg2, arg3, arg4, arg5, arg6, (ControlActionUPP)arg7, (ControlRef *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateScrollBarControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateSeparatorControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateSeparatorControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateSeparatorControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)CreateSeparatorControl((WindowRef)arg0, lparg1, (ControlRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateSeparatorControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateSliderControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateSliderControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jint arg5, jshort arg6, jboolean arg7, jint arg8, jintArray arg9)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateSliderControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)CreateSliderControl((WindowRef)arg0, (const Rect *)lparg1, (SInt32)arg2, (SInt32)arg3, (SInt32)arg4, (ControlSliderOrientation)arg5, (UInt16)arg6, (Boolean)arg7, (ControlActionUPP)arg8, (ControlRef *)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateSliderControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateStandardAlert
JNIEXPORT jint JNICALL OS_NATIVE(CreateStandardAlert)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	AlertStdCFStringAlertParamRec _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateStandardAlert_FUNC);
	if (arg3) if ((lparg3 = getAlertStdCFStringAlertParamRecFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreateStandardAlert((AlertType)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (const AlertStdCFStringAlertParamRec *)lparg3, (DialogRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setAlertStdCFStringAlertParamRecFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, CreateStandardAlert_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateStaticTextControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getControlFontStyleRecFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CreateStaticTextControl((WindowRef)arg0, lparg1, (CFStringRef)arg2, (const ControlFontStyleRec *)lparg3, (ControlRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setControlFontStyleRecFields(env, arg3, lparg3);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateStaticTextControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateTabsControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateTabsControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshort arg2, jshort arg3, jshort arg4, jint arg5, jintArray arg6)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateTabsControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)CreateTabsControl((WindowRef)arg0, (const Rect *)lparg1, (ControlTabSize)arg2, (ControlTabDirection)arg3, (UInt16)arg4, (const ControlTabEntry *)arg5, (ControlRef *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateTabsControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateTextToUnicodeInfoByEncoding
JNIEXPORT jint JNICALL OS_NATIVE(CreateTextToUnicodeInfoByEncoding)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateTextToUnicodeInfoByEncoding_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateTextToUnicodeInfoByEncoding((TextEncoding)arg0, (TextToUnicodeInfo *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateTextToUnicodeInfoByEncoding_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateUnicodeToTextInfoByEncoding
JNIEXPORT jint JNICALL OS_NATIVE(CreateUnicodeToTextInfoByEncoding)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateUnicodeToTextInfoByEncoding_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateUnicodeToTextInfoByEncoding((TextEncoding)arg0, (UnicodeToTextInfo *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateUnicodeToTextInfoByEncoding_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateUserPaneControl
JNIEXPORT jint JNICALL OS_NATIVE(CreateUserPaneControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	Rect _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateUserPaneControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)CreateUserPaneControl((WindowRef)arg0, lparg1, arg2, (ControlRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CreateUserPaneControl_FUNC);
	return rc;
}
#endif

#ifndef NO_CreateWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(CreateWindowGroup)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CreateWindowGroup_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)CreateWindowGroup((WindowGroupAttributes)arg0, (WindowGroupRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CreateWindowGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_DataBrowserChangeAttributes
JNIEXPORT jint JNICALL OS_NATIVE(DataBrowserChangeAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataBrowserChangeAttributes_FUNC);
/*
	rc = (jint)DataBrowserChangeAttributes(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, DataBrowserChangeAttributes)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, DataBrowserChangeAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_DataBrowserGetAttributes
JNIEXPORT jint JNICALL OS_NATIVE(DataBrowserGetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataBrowserGetAttributes_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
/*
	rc = (jint)DataBrowserGetAttributes(arg0, lparg1);
*/
	{
		LOAD_FUNCTION(fp, DataBrowserGetAttributes)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint *))fp)(arg0, lparg1);
		}
	}
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, DataBrowserGetAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_DataBrowserGetMetric
JNIEXPORT jint JNICALL OS_NATIVE(DataBrowserGetMetric)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2, jfloatArray arg3)
{
	jboolean *lparg2=NULL;
	jfloat *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataBrowserGetMetric_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jint)DataBrowserGetMetric(arg0, arg1, lparg2, lparg3);
*/
	{
		LOAD_FUNCTION(fp, DataBrowserGetMetric)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jboolean *, jfloat *))fp)(arg0, arg1, lparg2, lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, DataBrowserGetMetric_FUNC);
	return rc;
}
#endif

#ifndef NO_DataBrowserSetMetric
JNIEXPORT jint JNICALL OS_NATIVE(DataBrowserSetMetric)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jfloat arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DataBrowserSetMetric_FUNC);
/*
	rc = (jint)DataBrowserSetMetric(arg0, arg1, arg2, arg3);
*/
	{
		LOAD_FUNCTION(fp, DataBrowserSetMetric)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jboolean, jfloat))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, DataBrowserSetMetric_FUNC);
	return rc;
}
#endif

#ifndef NO_DeactivateTSMDocument
JNIEXPORT jint JNICALL OS_NATIVE(DeactivateTSMDocument)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DeactivateTSMDocument_FUNC);
	rc = (jint)DeactivateTSMDocument((TSMDocumentID)arg0);
	OS_NATIVE_EXIT(env, that, DeactivateTSMDocument_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteMenu
JNIEXPORT void JNICALL OS_NATIVE(DeleteMenu)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, DeleteMenu_FUNC);
	DeleteMenu((MenuID)arg0);
	OS_NATIVE_EXIT(env, that, DeleteMenu_FUNC);
}
#endif

#ifndef NO_DeleteMenuItem
JNIEXPORT void JNICALL OS_NATIVE(DeleteMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, DeleteMenuItem_FUNC);
	DeleteMenuItem((MenuRef)arg0, (short)arg1);
	OS_NATIVE_EXIT(env, that, DeleteMenuItem_FUNC);
}
#endif

#ifndef NO_DeleteMenuItems
JNIEXPORT jint JNICALL OS_NATIVE(DeleteMenuItems)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteMenuItems_FUNC);
	rc = (jint)DeleteMenuItems((MenuRef)arg0, (MenuItemIndex)arg1, (ItemCount)arg2);
	OS_NATIVE_EXIT(env, that, DeleteMenuItems_FUNC);
	return rc;
}
#endif

#ifndef NO_DeleteTSMDocument
JNIEXPORT jint JNICALL OS_NATIVE(DeleteTSMDocument)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DeleteTSMDocument_FUNC);
	rc = (jint)DeleteTSMDocument((TSMDocumentID)arg0);
	OS_NATIVE_EXIT(env, that, DeleteTSMDocument_FUNC);
	return rc;
}
#endif

#ifndef NO_DiffRgn
JNIEXPORT void JNICALL OS_NATIVE(DiffRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, DiffRgn_FUNC);
	DiffRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	OS_NATIVE_EXIT(env, that, DiffRgn_FUNC);
}
#endif

#ifndef NO_DisableControl
JNIEXPORT jint JNICALL OS_NATIVE(DisableControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DisableControl_FUNC);
	rc = (jint)DisableControl((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, DisableControl_FUNC);
	return rc;
}
#endif

#ifndef NO_DisableMenuCommand
JNIEXPORT void JNICALL OS_NATIVE(DisableMenuCommand)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, DisableMenuCommand_FUNC);
	DisableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
	OS_NATIVE_EXIT(env, that, DisableMenuCommand_FUNC);
}
#endif

#ifndef NO_DisableMenuItem
JNIEXPORT void JNICALL OS_NATIVE(DisableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, DisableMenuItem_FUNC);
	DisableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
	OS_NATIVE_EXIT(env, that, DisableMenuItem_FUNC);
}
#endif

#ifndef NO_DisposeControl
JNIEXPORT void JNICALL OS_NATIVE(DisposeControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeControl_FUNC);
	DisposeControl((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, DisposeControl_FUNC);
}
#endif

#ifndef NO_DisposeDrag
JNIEXPORT jint JNICALL OS_NATIVE(DisposeDrag)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DisposeDrag_FUNC);
	rc = (jint)DisposeDrag((DragRef)arg0);
	OS_NATIVE_EXIT(env, that, DisposeDrag_FUNC);
	return rc;
}
#endif

#ifndef NO_DisposeGWorld
JNIEXPORT void JNICALL OS_NATIVE(DisposeGWorld)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeGWorld_FUNC);
	DisposeGWorld((GWorldPtr)arg0);
	OS_NATIVE_EXIT(env, that, DisposeGWorld_FUNC);
}
#endif

#ifndef NO_DisposeHandle
JNIEXPORT void JNICALL OS_NATIVE(DisposeHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeHandle_FUNC);
	DisposeHandle((Handle)arg0);
	OS_NATIVE_EXIT(env, that, DisposeHandle_FUNC);
}
#endif

#ifndef NO_DisposeMenu
JNIEXPORT void JNICALL OS_NATIVE(DisposeMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeMenu_FUNC);
	DisposeMenu((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, DisposeMenu_FUNC);
}
#endif

#ifndef NO_DisposePtr
JNIEXPORT void JNICALL OS_NATIVE(DisposePtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposePtr_FUNC);
	DisposePtr((Ptr)arg0);
	OS_NATIVE_EXIT(env, that, DisposePtr_FUNC);
}
#endif

#ifndef NO_DisposeRgn
JNIEXPORT void JNICALL OS_NATIVE(DisposeRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeRgn_FUNC);
	DisposeRgn((RgnHandle)arg0);
	OS_NATIVE_EXIT(env, that, DisposeRgn_FUNC);
}
#endif

#ifndef NO_DisposeTextToUnicodeInfo
JNIEXPORT jint JNICALL OS_NATIVE(DisposeTextToUnicodeInfo)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DisposeTextToUnicodeInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)DisposeTextToUnicodeInfo((TextToUnicodeInfo *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, DisposeTextToUnicodeInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_DisposeUnicodeToTextInfo
JNIEXPORT jint JNICALL OS_NATIVE(DisposeUnicodeToTextInfo)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DisposeUnicodeToTextInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)DisposeUnicodeToTextInfo((UnicodeToTextInfo *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, DisposeUnicodeToTextInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_DisposeWindow
JNIEXPORT void JNICALL OS_NATIVE(DisposeWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DisposeWindow_FUNC);
	DisposeWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, DisposeWindow_FUNC);
}
#endif

#ifndef NO_DrawControlInCurrentPort
JNIEXPORT void JNICALL OS_NATIVE(DrawControlInCurrentPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, DrawControlInCurrentPort_FUNC);
	DrawControlInCurrentPort((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, DrawControlInCurrentPort_FUNC);
}
#endif

#ifndef NO_DrawMenuBar
JNIEXPORT void JNICALL OS_NATIVE(DrawMenuBar)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, DrawMenuBar_FUNC);
	DrawMenuBar();
	OS_NATIVE_EXIT(env, that, DrawMenuBar_FUNC);
}
#endif

#ifndef NO_DrawPicture
JNIEXPORT void JNICALL OS_NATIVE(DrawPicture)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, DrawPicture_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	DrawPicture((PicHandle)arg0, (const Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, DrawPicture_FUNC);
}
#endif

#ifndef NO_DrawThemeButton
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeButton)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jobject arg2, jobject arg3, jint arg4, jint arg5, jint arg6)
{
	Rect _arg0, *lparg0=NULL;
	ThemeButtonDrawInfo _arg2, *lparg2=NULL;
	ThemeButtonDrawInfo _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeButton_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getThemeButtonDrawInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getThemeButtonDrawInfoFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)DrawThemeButton((Rect *)lparg0, (ThemeButtonKind)arg1, (const ThemeButtonDrawInfo *)lparg2, (const ThemeButtonDrawInfo *)lparg3, (ThemeEraseUPP)arg4, (ThemeButtonDrawUPP)arg5, (UInt32)arg6);
fail:
	if (arg3 && lparg3) setThemeButtonDrawInfoFields(env, arg3, lparg3);
	if (arg2 && lparg2) setThemeButtonDrawInfoFields(env, arg2, lparg2);
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DrawThemeButton_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeEditTextFrame
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeEditTextFrame)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeEditTextFrame_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DrawThemeEditTextFrame((const Rect *)lparg0, (ThemeDrawState)arg1);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DrawThemeEditTextFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeFocusRect
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeFocusRect)
	(JNIEnv *env, jclass that, jobject arg0, jboolean arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeFocusRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DrawThemeFocusRect((const Rect *)lparg0, (Boolean)arg1);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DrawThemeFocusRect_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemePopupArrow
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemePopupArrow)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3, jint arg4, jint arg5)
{
	Rect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemePopupArrow_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DrawThemePopupArrow(lparg0, (ThemeArrowOrientation)arg1, (ThemePopupArrowSize)arg2, (ThemeDrawState)arg3, (ThemeEraseUPP)arg4, (UInt32)arg5);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DrawThemePopupArrow_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeSeparator
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeSeparator)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeSeparator_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)DrawThemeSeparator((const Rect *)lparg0, (ThemeDrawState)arg1);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, DrawThemeSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO_DrawThemeTextBox
JNIEXPORT jint JNICALL OS_NATIVE(DrawThemeTextBox)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshort arg5, jint arg6)
{
	Rect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, DrawThemeTextBox_FUNC);
	if (arg4) if ((lparg4 = getRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)DrawThemeTextBox((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (const Rect *)lparg4, (SInt16)arg5, (void *)arg6);
fail:
	if (arg4 && lparg4) setRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, DrawThemeTextBox_FUNC);
	return rc;
}
#endif

#ifndef NO_EmbedControl
JNIEXPORT jint JNICALL OS_NATIVE(EmbedControl)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EmbedControl_FUNC);
	rc = (jint)EmbedControl((ControlRef)arg0, (ControlRef)arg1);
	OS_NATIVE_EXIT(env, that, EmbedControl_FUNC);
	return rc;
}
#endif

#ifndef NO_EmptyRect
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EmptyRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)EmptyRect((const Rect *)lparg0);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, EmptyRect_FUNC);
	return rc;
}
#endif

#ifndef NO_EmptyRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(EmptyRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, EmptyRgn_FUNC);
	rc = (jboolean)EmptyRgn((RgnHandle)arg0);
	OS_NATIVE_EXIT(env, that, EmptyRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableControl
JNIEXPORT jint JNICALL OS_NATIVE(EnableControl)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, EnableControl_FUNC);
	rc = (jint)EnableControl((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, EnableControl_FUNC);
	return rc;
}
#endif

#ifndef NO_EnableMenuCommand
JNIEXPORT void JNICALL OS_NATIVE(EnableMenuCommand)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, EnableMenuCommand_FUNC);
	EnableMenuCommand((MenuRef)arg0, (MenuCommand)arg1);
	OS_NATIVE_EXIT(env, that, EnableMenuCommand_FUNC);
}
#endif

#ifndef NO_EnableMenuItem
JNIEXPORT void JNICALL OS_NATIVE(EnableMenuItem)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, EnableMenuItem_FUNC);
	EnableMenuItem((MenuRef)arg0, (MenuItemIndex)arg1);
	OS_NATIVE_EXIT(env, that, EnableMenuItem_FUNC);
}
#endif

#ifndef NO_EraseRect
JNIEXPORT void JNICALL OS_NATIVE(EraseRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, EraseRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	EraseRect((const Rect *)lparg0);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, EraseRect_FUNC);
}
#endif

#ifndef NO_FMGetATSFontRefFromFont
JNIEXPORT jint JNICALL OS_NATIVE(FMGetATSFontRefFromFont)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FMGetATSFontRefFromFont_FUNC);
	rc = (jint)FMGetATSFontRefFromFont(arg0);
	OS_NATIVE_EXIT(env, that, FMGetATSFontRefFromFont_FUNC);
	return rc;
}
#endif

#ifndef NO_FMGetFontFamilyFromName
JNIEXPORT jshort JNICALL OS_NATIVE(FMGetFontFamilyFromName)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, FMGetFontFamilyFromName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jshort)FMGetFontFamilyFromName((ConstStr255Param)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FMGetFontFamilyFromName_FUNC);
	return rc;
}
#endif

#ifndef NO_FMGetFontFamilyInstanceFromFont
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFamilyInstanceFromFont)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FMGetFontFamilyInstanceFromFont_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)FMGetFontFamilyInstanceFromFont((FMFont)arg0, (FMFontFamily *)lparg1, (FMFontStyle *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, FMGetFontFamilyInstanceFromFont_FUNC);
	return rc;
}
#endif

#ifndef NO_FMGetFontFromATSFontRef
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFromATSFontRef)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FMGetFontFromATSFontRef_FUNC);
	rc = (jint)FMGetFontFromATSFontRef(arg0);
	OS_NATIVE_EXIT(env, that, FMGetFontFromATSFontRef_FUNC);
	return rc;
}
#endif

#ifndef NO_FMGetFontFromFontFamilyInstance
JNIEXPORT jint JNICALL OS_NATIVE(FMGetFontFromFontFamilyInstance)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jintArray arg2, jshortArray arg3)
{
	jint *lparg2=NULL;
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FMGetFontFromFontFamilyInstance_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)FMGetFontFromFontFamilyInstance((FMFontFamily)arg0, (FMFontStyle)arg1, (FMFont *)lparg2, (FMFontStyle *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, FMGetFontFromFontFamilyInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_FPIsFontPanelVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(FPIsFontPanelVisible)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FPIsFontPanelVisible_FUNC);
	rc = (jboolean)FPIsFontPanelVisible();
	OS_NATIVE_EXIT(env, that, FPIsFontPanelVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_FPShowHideFontPanel
JNIEXPORT jint JNICALL OS_NATIVE(FPShowHideFontPanel)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FPShowHideFontPanel_FUNC);
	rc = (jint)FPShowHideFontPanel();
	OS_NATIVE_EXIT(env, that, FPShowHideFontPanel_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FSGetCatalogInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)FSGetCatalogInfo((FSRef *)lparg0, (FSCatalogInfoBitmap)arg1, (FSCatalogInfo *)lparg2, (HFSUniStr255 *)lparg3, (FSSpec *)lparg4, (FSRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FSGetCatalogInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_FSpGetFInfo
JNIEXPORT jint JNICALL OS_NATIVE(FSpGetFInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FSpGetFInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)FSpGetFInfo((FSSpec *)lparg0, (FInfo *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FSpGetFInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_FSpMakeFSRef
JNIEXPORT jint JNICALL OS_NATIVE(FSpMakeFSRef)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FSpMakeFSRef_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)FSpMakeFSRef((const FSSpec *)lparg0, (FSRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FSpMakeFSRef_FUNC);
	return rc;
}
#endif

#ifndef NO_FindSpecificEventInQueue
JNIEXPORT jint JNICALL OS_NATIVE(FindSpecificEventInQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FindSpecificEventInQueue_FUNC);
	rc = (jint)FindSpecificEventInQueue((EventQueueRef)arg0, (EventComparatorUPP)arg1, (void *)arg2);
	OS_NATIVE_EXIT(env, that, FindSpecificEventInQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_FindWindow
JNIEXPORT jshort JNICALL OS_NATIVE(FindWindow)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	Point _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, FindWindow_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jshort)FindWindow(*(Point *)lparg0, (WindowRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, FindWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_Fix2Long
JNIEXPORT jint JNICALL OS_NATIVE(Fix2Long)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Fix2Long_FUNC);
	rc = (jint)Fix2Long(arg0);
	OS_NATIVE_EXIT(env, that, Fix2Long_FUNC);
	return rc;
}
#endif

#ifndef NO_Fix2X
JNIEXPORT jdouble JNICALL OS_NATIVE(Fix2X)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, Fix2X_FUNC);
	rc = (jdouble)Fix2X((Fixed)arg0);
	OS_NATIVE_EXIT(env, that, Fix2X_FUNC);
	return rc;
}
#endif

#ifndef NO_FixTSMDocument
JNIEXPORT jint JNICALL OS_NATIVE(FixTSMDocument)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FixTSMDocument_FUNC);
	rc = (jint)FixTSMDocument((TSMDocumentID)arg0);
	OS_NATIVE_EXIT(env, that, FixTSMDocument_FUNC);
	return rc;
}
#endif

#ifndef NO_FrontWindow
JNIEXPORT jint JNICALL OS_NATIVE(FrontWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, FrontWindow_FUNC);
	rc = (jint)FrontWindow();
	OS_NATIVE_EXIT(env, that, FrontWindow_FUNC);
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
	rc = (jint)Gestalt((OSType)arg0, (long *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, Gestalt_FUNC);
	return rc;
}
#endif

#ifndef NO_GetApplicationEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetApplicationEventTarget)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetApplicationEventTarget_FUNC);
	rc = (jint)GetApplicationEventTarget();
	OS_NATIVE_EXIT(env, that, GetApplicationEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetAvailableWindowAttributes
JNIEXPORT jint JNICALL OS_NATIVE(GetAvailableWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetAvailableWindowAttributes_FUNC);
	rc = (jint)GetAvailableWindowAttributes((WindowClass)arg0);
	OS_NATIVE_EXIT(env, that, GetAvailableWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_GetAvailableWindowPositioningBounds
JNIEXPORT jint JNICALL OS_NATIVE(GetAvailableWindowPositioningBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetAvailableWindowPositioningBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetAvailableWindowPositioningBounds((GDHandle)arg0, (Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetAvailableWindowPositioningBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_GetBestControlRect
JNIEXPORT jint JNICALL OS_NATIVE(GetBestControlRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jshortArray arg2)
{
	Rect _arg1, *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetBestControlRect_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetBestControlRect((ControlRef)arg0, (Rect *)lparg1, (SInt16 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetBestControlRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCFRunLoopFromEventLoop
JNIEXPORT jint JNICALL OS_NATIVE(GetCFRunLoopFromEventLoop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCFRunLoopFromEventLoop_FUNC);
	rc = (jint)GetCFRunLoopFromEventLoop((EventLoopRef)arg0);
	OS_NATIVE_EXIT(env, that, GetCFRunLoopFromEventLoop_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCaretTime
JNIEXPORT jint JNICALL OS_NATIVE(GetCaretTime)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCaretTime_FUNC);
	rc = (jint)GetCaretTime();
	OS_NATIVE_EXIT(env, that, GetCaretTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClip
JNIEXPORT void JNICALL OS_NATIVE(GetClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, GetClip_FUNC);
	GetClip((RgnHandle)arg0);
	OS_NATIVE_EXIT(env, that, GetClip_FUNC);
}
#endif

#ifndef NO_GetControl32BitMaximum
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitMaximum)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControl32BitMaximum_FUNC);
	rc = (jint)GetControl32BitMaximum((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControl32BitMaximum_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControl32BitMinimum
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitMinimum)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControl32BitMinimum_FUNC);
	rc = (jint)GetControl32BitMinimum((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControl32BitMinimum_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControl32BitValue
JNIEXPORT jint JNICALL OS_NATIVE(GetControl32BitValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControl32BitValue_FUNC);
	rc = (jint)GetControl32BitValue((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControl32BitValue_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlAction
JNIEXPORT jint JNICALL OS_NATIVE(GetControlAction)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlAction_FUNC);
	rc = (jint)GetControlAction((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlAction_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlBounds
JNIEXPORT void JNICALL OS_NATIVE(GetControlBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetControlBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	GetControlBounds((ControlRef)arg0, (Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetControlBounds_FUNC);
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	ControlEditTextSelectionRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_3I_FUNC);
	if (arg4) if ((lparg4 = getControlEditTextSelectionRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setControlEditTextSelectionRecFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	ControlFontStyleRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I_FUNC);
	if (arg4) if ((lparg4 = getControlFontStyleRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setControlFontStyleRecFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_ControlFontStyleRec_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_LongDateRec_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_LongDateRec_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	LongDateRec _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_LongDateRec_2_3I_FUNC);
	if (arg4) if ((lparg4 = getLongDateRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setLongDateRecFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_LongDateRec_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jobject arg4, jintArray arg5)
{
	Rect _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I_FUNC);
	if (arg4) if ((lparg4 = getRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetControlData__ISIILorg_eclipse_swt_internal_carbon_Rect_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3B_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISII_3B_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetControlData__ISII_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3I_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISII_3I_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetControlData__ISII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlData__ISII_3S_3I
JNIEXPORT jint JNICALL OS_NATIVE(GetControlData__ISII_3S_3I)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jint arg3, jshortArray arg4, jintArray arg5)
{
	jshort *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlData__ISII_3S_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (void *)lparg4, (Size *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetControlData__ISII_3S_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetControlEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlEventTarget_FUNC);
	rc = (jint)GetControlEventTarget((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlFeatures
JNIEXPORT jint JNICALL OS_NATIVE(GetControlFeatures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlFeatures_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetControlFeatures((ControlRef)arg0, (UInt32 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetControlFeatures_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlKind
JNIEXPORT jint JNICALL OS_NATIVE(GetControlKind)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlKind _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlKind_FUNC);
	if (arg1) if ((lparg1 = getControlKindFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetControlKind((ControlRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setControlKindFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetControlKind_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlOwner
JNIEXPORT jint JNICALL OS_NATIVE(GetControlOwner)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlOwner_FUNC);
	rc = (jint)GetControlOwner((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlOwner_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(GetControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlProperty_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetControlProperty((ControlRef)arg0, arg1, arg2, arg3, (UInt32 *)lparg4, (void *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetControlProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlReference
JNIEXPORT jint JNICALL OS_NATIVE(GetControlReference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlReference_FUNC);
	rc = (jint)GetControlReference((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlReference_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetControlRegion)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlRegion_FUNC);
	rc = (jint)GetControlRegion((ControlRef)arg0, (ControlPartCode)arg1, (RgnHandle)arg2);
	OS_NATIVE_EXIT(env, that, GetControlRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlValue
JNIEXPORT jshort JNICALL OS_NATIVE(GetControlValue)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlValue_FUNC);
	rc = (jshort)GetControlValue((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlValue_FUNC);
	return rc;
}
#endif

#ifndef NO_GetControlViewSize
JNIEXPORT jint JNICALL OS_NATIVE(GetControlViewSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetControlViewSize_FUNC);
	rc = (jint)GetControlViewSize((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, GetControlViewSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentEventButtonState
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventButtonState)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentEventButtonState_FUNC);
	rc = (jint)GetCurrentEventButtonState();
	OS_NATIVE_EXIT(env, that, GetCurrentEventButtonState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentEventKeyModifiers
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventKeyModifiers)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentEventKeyModifiers_FUNC);
	rc = (jint)GetCurrentEventKeyModifiers();
	OS_NATIVE_EXIT(env, that, GetCurrentEventKeyModifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentEventLoop
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventLoop)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentEventLoop_FUNC);
	rc = (jint)GetCurrentEventLoop();
	OS_NATIVE_EXIT(env, that, GetCurrentEventLoop_FUNC);
	return rc;
}
#endif

#ifndef NO_GetCurrentEventQueue
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentEventQueue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentEventQueue_FUNC);
	rc = (jint)GetCurrentEventQueue();
	OS_NATIVE_EXIT(env, that, GetCurrentEventQueue_FUNC);
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

#ifndef NO_GetCurrentScrap
JNIEXPORT jint JNICALL OS_NATIVE(GetCurrentScrap)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetCurrentScrap_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetCurrentScrap((ScrapRef *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetCurrentScrap_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserCallbacks_FUNC);
	if (arg1) if ((lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetDataBrowserCallbacks((ControlRef)arg0, (DataBrowserCallbacks *)lparg1);
fail:
	if (arg1 && lparg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetDataBrowserCallbacks_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserHasScrollBars
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserHasScrollBars)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1, jbooleanArray arg2)
{
	jboolean *lparg1=NULL;
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserHasScrollBars_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserHasScrollBars((ControlRef)arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserHasScrollBars_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemCount
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemCount)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserItemCount_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserItemCount((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (UInt32 *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserItemCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemDataButtonValue)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserItemDataButtonValue_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserItemDataButtonValue((ControlRef)arg0, (ThemeButtonValue *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserItemDataButtonValue_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemPartBounds
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemPartBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserItemPartBounds_FUNC);
	if (arg4) if ((lparg4 = getRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)GetDataBrowserItemPartBounds((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserPropertyPart)arg3, (Rect *)lparg4);
fail:
	if (arg4 && lparg4) setRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetDataBrowserItemPartBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItemState
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItemState)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserItemState_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserItemState((ControlRef)arg0, arg1, (DataBrowserItemState *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserItemState_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserItems_FUNC);
	rc = (jint)GetDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (Boolean)arg2, (DataBrowserItemState)arg3, (Handle)arg4);
	OS_NATIVE_EXIT(env, that, GetDataBrowserItems_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserListViewDisclosureColumn
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserListViewDisclosureColumn)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jbooleanArray arg2)
{
	jint *lparg1=NULL;
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserListViewDisclosureColumn_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserListViewDisclosureColumn((ControlRef)arg0, (DataBrowserTableViewColumnID *)lparg1, (Boolean *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserListViewDisclosureColumn_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserListViewHeaderBtnHeight)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserListViewHeaderBtnHeight_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, (UInt16 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserListViewHeaderBtnHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserListViewHeaderDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserListViewHeaderDesc_FUNC);
	if (arg2) if ((lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetDataBrowserListViewHeaderDesc((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserListViewHeaderDesc *)lparg2);
fail:
	if (arg2 && lparg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetDataBrowserListViewHeaderDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserPropertyFlags
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserPropertyFlags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserPropertyFlags_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserPropertyFlags((ControlRef)arg0, (DataBrowserPropertyID)arg1, (DataBrowserPropertyFlags *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserPropertyFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserScrollBarInset
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserScrollBarInset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserScrollBarInset_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetDataBrowserScrollBarInset((ControlRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetDataBrowserScrollBarInset_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserScrollPosition
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserScrollPosition)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserScrollPosition_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserScrollPosition((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserScrollPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserSelectionAnchor
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserSelectionAnchor)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserSelectionAnchor_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserSelectionAnchor((ControlRef)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserSelectionAnchor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserSelectionFlags
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserSelectionFlags)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserSelectionFlags_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserSelectionFlags((ControlRef)arg0, (DataBrowserSelectionFlags *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserSelectionFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserSortProperty
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserSortProperty)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserSortProperty_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserSortProperty((ControlRef)arg0, (DataBrowserPropertyID *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserSortProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewColumnPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserTableViewColumnPosition_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserTableViewColumnPosition((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (DataBrowserTableViewColumnIndex *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserTableViewColumnPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewItemID
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewItemID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserTableViewItemID_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserTableViewItemID((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserTableViewItemID_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewItemRow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserTableViewItemRow_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserTableViewItemRow((ControlRef)arg0, (DataBrowserTableViewRowIndex)arg1, (DataBrowserItemID *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserTableViewItemRow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewNamedColumnWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserTableViewNamedColumnWidth_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserTableViewNamedColumnWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDataBrowserTableViewRowHeight
JNIEXPORT jint JNICALL OS_NATIVE(GetDataBrowserTableViewRowHeight)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDataBrowserTableViewRowHeight_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDataBrowserTableViewRowHeight((ControlRef)arg0, (UInt16 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDataBrowserTableViewRowHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDblTime
JNIEXPORT jint JNICALL OS_NATIVE(GetDblTime)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDblTime_FUNC);
	rc = (jint)GetDblTime();
	OS_NATIVE_EXIT(env, that, GetDblTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDeviceList
JNIEXPORT jint JNICALL OS_NATIVE(GetDeviceList)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDeviceList_FUNC);
	rc = (jint)GetDeviceList();
	OS_NATIVE_EXIT(env, that, GetDeviceList_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDragAllowableActions
JNIEXPORT jint JNICALL OS_NATIVE(GetDragAllowableActions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDragAllowableActions_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDragAllowableActions((DragRef)arg0, (DragActions *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDragAllowableActions_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDragDropAction
JNIEXPORT jint JNICALL OS_NATIVE(GetDragDropAction)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDragDropAction_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetDragDropAction((DragRef)arg0, (DragActions *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDragDropAction_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDragItemReferenceNumber
JNIEXPORT jint JNICALL OS_NATIVE(GetDragItemReferenceNumber)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDragItemReferenceNumber_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetDragItemReferenceNumber((DragRef)arg0, arg1, (DragItemRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetDragItemReferenceNumber_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDragModifiers_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetDragModifiers((DragRef)arg0, (SInt16 *)lparg1, (SInt16 *)lparg2, (SInt16 *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetDragModifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_GetDragMouse
JNIEXPORT jint JNICALL OS_NATIVE(GetDragMouse)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	Point _arg1, *lparg1=NULL;
	Point _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetDragMouse_FUNC);
	if (arg1) if ((lparg1 = getPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetDragMouse((DragRef)arg0, (Point *)lparg1, (Point *)lparg2);
fail:
	if (arg2 && lparg2) setPointFields(env, arg2, lparg2);
	if (arg1 && lparg1) setPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetDragMouse_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventClass
JNIEXPORT jint JNICALL OS_NATIVE(GetEventClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventClass_FUNC);
	rc = (jint)GetEventClass((EventRef)arg0);
	OS_NATIVE_EXIT(env, that, GetEventClass_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventDispatcherTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetEventDispatcherTarget)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventDispatcherTarget_FUNC);
	rc = (jint)GetEventDispatcherTarget();
	OS_NATIVE_EXIT(env, that, GetEventDispatcherTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventKind
JNIEXPORT jint JNICALL OS_NATIVE(GetEventKind)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventKind_FUNC);
	rc = (jint)GetEventKind((EventRef)arg0);
	OS_NATIVE_EXIT(env, that, GetEventKind_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3II
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jint arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3II_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)arg6);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3II_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getCGPointFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setCGPointFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGRect_2
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jobject arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	CGRect _arg6, *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGRect_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getCGRectFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setCGRectFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_CGRect_2_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getHICommandFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setHICommandFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_HICommand_2_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getPointFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setPointFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Point_2_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getRGBColorFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setRGBColorFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_RGBColor_2_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getRectFields(env, arg6, &_arg6)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) setRectFields(env, arg6, lparg6);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3ILorg_eclipse_swt_internal_carbon_Rect_2_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3I_3B_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3I_3B_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3I_3C_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetCharArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseCharArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3I_3C_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3I_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3I_3I_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3I_3S_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetShortArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseShortArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3I_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventParameter__III_3II_3I_3Z
JNIEXPORT jint JNICALL OS_NATIVE(GetEventParameter__III_3II_3I_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5, jbooleanArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jboolean *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventParameter__III_3II_3I_3Z_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetBooleanArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)GetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (EventParamType *)lparg3, (UInt32)arg4, (UInt32 *)lparg5, (void *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseBooleanArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetEventParameter__III_3II_3I_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_GetEventTime
JNIEXPORT jdouble JNICALL OS_NATIVE(GetEventTime)
	(JNIEnv *env, jclass that, jint arg0)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, GetEventTime_FUNC);
	rc = (jdouble)GetEventTime((EventRef)arg0);
	OS_NATIVE_EXIT(env, that, GetEventTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFlavorData
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFlavorData_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetFlavorData((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (void *)lparg3, (Size *)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetFlavorData_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFlavorDataSize
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorDataSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFlavorDataSize_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetFlavorDataSize((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (Size *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetFlavorDataSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFlavorType
JNIEXPORT jint JNICALL OS_NATIVE(GetFlavorType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFlavorType_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetFlavorType((DragRef)arg0, (DragItemRef)arg1, arg2, (FlavorType *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetFlavorType_FUNC);
	return rc;
}
#endif

#ifndef NO_GetFrontProcess
JNIEXPORT jint JNICALL OS_NATIVE(GetFrontProcess)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetFrontProcess_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetFrontProcess((ProcessSerialNumber *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetFrontProcess_FUNC);
	return rc;
}
#endif

#ifndef NO_GetGWorld
JNIEXPORT void JNICALL OS_NATIVE(GetGWorld)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetGWorld_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	GetGWorld((CGrafPtr *)lparg0, (GDHandle *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetGWorld_FUNC);
}
#endif

#ifndef NO_GetGlobalMouse
JNIEXPORT void JNICALL OS_NATIVE(GetGlobalMouse)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, GetGlobalMouse_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	GetGlobalMouse((Point *)lparg0);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetGlobalMouse_FUNC);
}
#endif

#ifndef NO_GetHandleSize
JNIEXPORT jint JNICALL OS_NATIVE(GetHandleSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetHandleSize_FUNC);
	rc = (jint)GetHandleSize((Handle)arg0);
	OS_NATIVE_EXIT(env, that, GetHandleSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconFamilyData
JNIEXPORT jint JNICALL OS_NATIVE(GetIconFamilyData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIconFamilyData_FUNC);
	rc = (jint)GetIconFamilyData((IconFamilyHandle)arg0, (OSType)arg1, (Handle)arg2);
	OS_NATIVE_EXIT(env, that, GetIconFamilyData_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconRef
JNIEXPORT jint JNICALL OS_NATIVE(GetIconRef)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIconRef_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetIconRef((SInt16)arg0, (OSType)arg1, (OSType)arg2, (IconRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetIconRef_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconRefFromFileInfo
JNIEXPORT jint JNICALL OS_NATIVE(GetIconRefFromFileInfo)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7)
{
	jbyte *lparg0=NULL;
	jchar *lparg2=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIconRefFromFileInfo_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)GetIconRefFromFileInfo((const FSRef *)lparg0, arg1, (const UniChar *)lparg2, (FSCatalogInfoBitmap)arg3, (const FSCatalogInfo *)arg4, arg5, (IconRef *)lparg6, (SInt16 *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetIconRefFromFileInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIconRefFromIconFamilyPtr
JNIEXPORT jint JNICALL OS_NATIVE(GetIconRefFromIconFamilyPtr)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIconRefFromIconFamilyPtr_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)GetIconRefFromIconFamilyPtr(arg0, arg1, lparg2);
*/
	{
		LOAD_FUNCTION(fp, GetIconRefFromIconFamilyPtr)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetIconRefFromIconFamilyPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIndMenuItemWithCommandID
JNIEXPORT jint JNICALL OS_NATIVE(GetIndMenuItemWithCommandID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jshortArray arg4)
{
	jint *lparg3=NULL;
	jshort *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIndMenuItemWithCommandID_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetIndMenuItemWithCommandID((MenuRef)arg0, (MenuCommand)arg1, (UInt32)arg2, (MenuRef *)lparg3, (MenuItemIndex *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, GetIndMenuItemWithCommandID_FUNC);
	return rc;
}
#endif

#ifndef NO_GetIndexedSubControl
JNIEXPORT jint JNICALL OS_NATIVE(GetIndexedSubControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetIndexedSubControl_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetIndexedSubControl((ControlRef)arg0, (UInt16)arg1, (ControlRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetIndexedSubControl_FUNC);
	return rc;
}
#endif

#ifndef NO_GetItemMark
JNIEXPORT void JNICALL OS_NATIVE(GetItemMark)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, GetItemMark_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	GetItemMark((MenuRef)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetItemMark_FUNC);
}
#endif

#ifndef NO_GetKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(GetKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetKeyboardFocus_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetKeyboardFocus((WindowRef)arg0, (ControlRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetKeyboardFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_GetLastUserEventTime
JNIEXPORT jdouble JNICALL OS_NATIVE(GetLastUserEventTime)
	(JNIEnv *env, jclass that)
{
	jdouble rc = 0;
	OS_NATIVE_ENTER(env, that, GetLastUserEventTime_FUNC);
	rc = (jdouble)GetLastUserEventTime();
	OS_NATIVE_EXIT(env, that, GetLastUserEventTime_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMBarHeight
JNIEXPORT jint JNICALL OS_NATIVE(GetMBarHeight)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMBarHeight_FUNC);
	rc = (jint)GetMBarHeight();
	OS_NATIVE_EXIT(env, that, GetMBarHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMainDevice
JNIEXPORT jint JNICALL OS_NATIVE(GetMainDevice)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMainDevice_FUNC);
	rc = (jint)GetMainDevice();
	OS_NATIVE_EXIT(env, that, GetMainDevice_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMainEventQueue
JNIEXPORT jint JNICALL OS_NATIVE(GetMainEventQueue)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMainEventQueue_FUNC);
	rc = (jint)GetMainEventQueue();
	OS_NATIVE_EXIT(env, that, GetMainEventQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuCommandMark
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuCommandMark)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuCommandMark_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetMenuCommandMark_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuEventTarget_FUNC);
	rc = (jint)GetMenuEventTarget((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, GetMenuEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuFont
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuFont)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuFont_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetMenuFont((MenuRef)arg0, (SInt16 *)lparg1, (UInt16 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetMenuFont_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuHeight
JNIEXPORT jshort JNICALL OS_NATIVE(GetMenuHeight)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuHeight_FUNC);
	rc = (jshort)GetMenuHeight((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, GetMenuHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuID
JNIEXPORT jshort JNICALL OS_NATIVE(GetMenuID)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuID_FUNC);
	rc = (jshort)GetMenuID((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, GetMenuID_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemCommandID
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemCommandID)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemCommandID_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetMenuItemCommandID((MenuRef)arg0, (SInt16)arg1, (MenuCommand *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetMenuItemCommandID_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemHierarchicalMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemHierarchicalMenu_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetMenuItemHierarchicalMenu((MenuRef)arg0, (SInt16)arg1, (MenuRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetMenuItemHierarchicalMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuItemRefCon
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuItemRefCon)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuItemRefCon_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetMenuItemRefCon_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuTrackingData
JNIEXPORT jint JNICALL OS_NATIVE(GetMenuTrackingData)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	MenuTrackingData _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuTrackingData_FUNC);
	if (arg1) if ((lparg1 = getMenuTrackingDataFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetMenuTrackingData((MenuRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setMenuTrackingDataFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetMenuTrackingData_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMenuWidth
JNIEXPORT jshort JNICALL OS_NATIVE(GetMenuWidth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetMenuWidth_FUNC);
	rc = (jshort)GetMenuWidth((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, GetMenuWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_GetMouse
JNIEXPORT void JNICALL OS_NATIVE(GetMouse)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, GetMouse_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	GetMouse((Point *)lparg0);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetMouse_FUNC);
}
#endif

#ifndef NO_GetNextDevice
JNIEXPORT jint JNICALL OS_NATIVE(GetNextDevice)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetNextDevice_FUNC);
	rc = (jint)GetNextDevice((GDHandle)arg0);
	OS_NATIVE_EXIT(env, that, GetNextDevice_FUNC);
	return rc;
}
#endif

#ifndef NO_GetNextWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetNextWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetNextWindow_FUNC);
	rc = (jint)GetNextWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, GetNextWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPixDepth
JNIEXPORT jshort JNICALL OS_NATIVE(GetPixDepth)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, GetPixDepth_FUNC);
	rc = (jshort)GetPixDepth((PixMapHandle)arg0);
	OS_NATIVE_EXIT(env, that, GetPixDepth_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPort
JNIEXPORT void JNICALL OS_NATIVE(GetPort)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, GetPort_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	GetPort((GrafPtr *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetPort_FUNC);
}
#endif

#ifndef NO_GetPortBitMapForCopyBits
JNIEXPORT jint JNICALL OS_NATIVE(GetPortBitMapForCopyBits)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPortBitMapForCopyBits_FUNC);
	rc = (jint)GetPortBitMapForCopyBits((CGrafPtr)arg0);
	OS_NATIVE_EXIT(env, that, GetPortBitMapForCopyBits_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPortBounds
JNIEXPORT void JNICALL OS_NATIVE(GetPortBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetPortBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	GetPortBounds((CGrafPtr)arg0, (Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetPortBounds_FUNC);
}
#endif

#ifndef NO_GetPreviousWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetPreviousWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPreviousWindow_FUNC);
	rc = (jint)GetPreviousWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, GetPreviousWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetPtrSize
JNIEXPORT jint JNICALL OS_NATIVE(GetPtrSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetPtrSize_FUNC);
	rc = (jint)GetPtrSize((Ptr)arg0);
	OS_NATIVE_EXIT(env, that, GetPtrSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetRegionBounds
JNIEXPORT void JNICALL OS_NATIVE(GetRegionBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetRegionBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	GetRegionBounds((RgnHandle)arg0, (Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetRegionBounds_FUNC);
}
#endif

#ifndef NO_GetRootControl
JNIEXPORT jint JNICALL OS_NATIVE(GetRootControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetRootControl_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetRootControl((WindowRef)arg0, (ControlRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetRootControl_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorCount
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorCount)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrapFlavorCount_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetScrapFlavorCount((ScrapRef)arg0, (UInt32 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetScrapFlavorCount_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorData__II_3I_3B
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorData__II_3I_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jbyteArray arg3)
{
	jint *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrapFlavorData__II_3I_3B_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetScrapFlavorData((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2, (void *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetScrapFlavorData__II_3I_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorData__II_3I_3C
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorData__II_3I_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jcharArray arg3)
{
	jint *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrapFlavorData__II_3I_3C_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)GetScrapFlavorData((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2, (void *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetScrapFlavorData__II_3I_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorInfoList
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorInfoList)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrapFlavorInfoList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetScrapFlavorInfoList((ScrapRef)arg0, (UInt32 *)lparg1, (ScrapFlavorInfo *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetScrapFlavorInfoList_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScrapFlavorSize
JNIEXPORT jint JNICALL OS_NATIVE(GetScrapFlavorSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScrapFlavorSize_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetScrapFlavorSize((ScrapRef)arg0, (ScrapFlavorType)arg1, (Size *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetScrapFlavorSize_FUNC);
	return rc;
}
#endif

#ifndef NO_GetScriptManagerVariable
JNIEXPORT jint JNICALL OS_NATIVE(GetScriptManagerVariable)
	(JNIEnv *env, jclass that, jshort arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetScriptManagerVariable_FUNC);
	rc = (jint)GetScriptManagerVariable(arg0);
	OS_NATIVE_EXIT(env, that, GetScriptManagerVariable_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSuperControl
JNIEXPORT jint JNICALL OS_NATIVE(GetSuperControl)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetSuperControl_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetSuperControl((ControlRef)arg0, (ControlRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetSuperControl_FUNC);
	return rc;
}
#endif

#ifndef NO_GetSystemUIMode
JNIEXPORT void JNICALL OS_NATIVE(GetSystemUIMode)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetSystemUIMode_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	GetSystemUIMode((SystemUIMode *)lparg0, (SystemUIOptions *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetSystemUIMode_FUNC);
}
#endif

#ifndef NO_GetTabContentRect
JNIEXPORT jint JNICALL OS_NATIVE(GetTabContentRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetTabContentRect_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetTabContentRect((ControlRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetTabContentRect_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeBrushAsColor
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeBrushAsColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2, jobject arg3)
{
	RGBColor _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeBrushAsColor_FUNC);
	if (arg3) if ((lparg3 = getRGBColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)GetThemeBrushAsColor(arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setRGBColorFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetThemeBrushAsColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeButtonContentBounds
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeButtonContentBounds)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jobject arg3)
{
	Rect _arg0, *lparg0=NULL;
	ThemeButtonDrawInfo _arg2, *lparg2=NULL;
	Rect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeButtonContentBounds_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getThemeButtonDrawInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)GetThemeButtonContentBounds(lparg0, arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setRectFields(env, arg3, lparg3);
	if (arg2 && lparg2) setThemeButtonDrawInfoFields(env, arg2, lparg2);
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetThemeButtonContentBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeButtonRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeButtonRegion)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jobject arg2, jint arg3)
{
	Rect _arg0, *lparg0=NULL;
	ThemeButtonDrawInfo _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeButtonRegion_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getThemeButtonDrawInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetThemeButtonRegion(lparg0, arg1, lparg2, (RgnHandle)arg3);
fail:
	if (arg2 && lparg2) setThemeButtonDrawInfoFields(env, arg2, lparg2);
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, GetThemeButtonRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeDrawingState
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeDrawingState)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeDrawingState_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)GetThemeDrawingState((ThemeDrawingState *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, GetThemeDrawingState_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeFont_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)GetThemeFont((ThemeFontID)arg0, (ScriptCode)arg1, (unsigned char *)lparg2, (SInt16 *)lparg3, (Style *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, GetThemeFont_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeMenuItemExtra
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMenuItemExtra)
	(JNIEnv *env, jclass that, jshort arg0, jshortArray arg1, jshortArray arg2)
{
	jshort *lparg1=NULL;
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeMenuItemExtra_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetThemeMenuItemExtra(arg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetThemeMenuItemExtra_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeMetric
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeMetric)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeMetric_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetThemeMetric(arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetThemeMetric_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeTextColor
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeTextColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2, jobject arg3)
{
	RGBColor _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeTextColor_FUNC);
	if (arg3) if ((lparg3 = getRGBColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)GetThemeTextColor(arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setRGBColorFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, GetThemeTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_GetThemeTextDimensions
JNIEXPORT jint JNICALL OS_NATIVE(GetThemeTextDimensions)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jboolean arg3, jobject arg4, jshortArray arg5)
{
	Point _arg4, *lparg4=NULL;
	jshort *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetThemeTextDimensions_FUNC);
	if (arg4) if ((lparg4 = getPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)GetThemeTextDimensions((CFStringRef)arg0, (ThemeFontID)arg1, (ThemeDrawState)arg2, (Boolean)arg3, (Point *)lparg4, (SInt16 *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setPointFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, GetThemeTextDimensions_FUNC);
	return rc;
}
#endif

#ifndef NO_GetUserFocusEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetUserFocusEventTarget)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetUserFocusEventTarget_FUNC);
	rc = (jint)GetUserFocusEventTarget();
	OS_NATIVE_EXIT(env, that, GetUserFocusEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetUserFocusWindow
JNIEXPORT jint JNICALL OS_NATIVE(GetUserFocusWindow)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetUserFocusWindow_FUNC);
	rc = (jint)GetUserFocusWindow();
	OS_NATIVE_EXIT(env, that, GetUserFocusWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowActivationScope
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowActivationScope)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowActivationScope_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowActivationScope((WindowRef)arg0, (WindowActivationScope *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowActivationScope_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowAlpha
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowAlpha)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowAlpha_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowAlpha((WindowRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowAlpha_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowBounds
JNIEXPORT void JNICALL OS_NATIVE(GetWindowBounds)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, GetWindowBounds_FUNC);
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	GetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, GetWindowBounds_FUNC);
}
#endif

#ifndef NO_GetWindowClass
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowClass)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowClass_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowClass((WindowRef)arg0, (WindowClass *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowClass_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowDefaultButton
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowDefaultButton)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowDefaultButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowDefaultButton((WindowRef)arg0, (ControlRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowDefaultButton_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowEventTarget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowEventTarget_FUNC);
	rc = (jint)GetWindowEventTarget((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowFromPort
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowFromPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowFromPort_FUNC);
	rc = (jint)GetWindowFromPort((CGrafPtr)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowFromPort_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowGroupOfClass
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowGroupOfClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowGroupOfClass_FUNC);
	rc = (jint)GetWindowGroupOfClass(arg0);
	OS_NATIVE_EXIT(env, that, GetWindowGroupOfClass_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowList
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowList)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowList_FUNC);
	rc = (jint)GetWindowList();
	OS_NATIVE_EXIT(env, that, GetWindowList_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowModality
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowModality)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowModality_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)GetWindowModality((WindowRef)arg0, (WindowModality *)lparg1, (WindowRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, GetWindowModality_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowPort
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowPort_FUNC);
	rc = (jint)GetWindowPort((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, GetWindowPort_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowRegion
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowRegion)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowRegion_FUNC);
	rc = (jint)GetWindowRegion((WindowRef)arg0, (WindowRegionCode)arg1, (RgnHandle)arg2);
	OS_NATIVE_EXIT(env, that, GetWindowRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowResizeLimits
JNIEXPORT jint JNICALL OS_NATIVE(GetWindowResizeLimits)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	CGPoint _arg1, *lparg1=NULL;
	CGPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, GetWindowResizeLimits_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)GetWindowResizeLimits((WindowRef)arg0, (HISize *)lparg1, (HISize *)lparg2);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetWindowResizeLimits_FUNC);
	return rc;
}
#endif

#ifndef NO_GetWindowStructureWidths
JNIEXPORT void JNICALL OS_NATIVE(GetWindowStructureWidths)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, GetWindowStructureWidths_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	GetWindowStructureWidths((WindowRef)arg0, (Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, GetWindowStructureWidths_FUNC);
}
#endif

#ifndef NO_HIComboBoxAppendTextItem
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxAppendTextItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxAppendTextItem_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIComboBoxAppendTextItem((HIViewRef)arg0, (CFStringRef)arg1, (CFIndex *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HIComboBoxAppendTextItem_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxCopyTextItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxCopyTextItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxCopyTextItemAtIndex_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIComboBoxCopyTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HIComboBoxCopyTextItemAtIndex_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxCreate_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getControlFontStyleRecFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)HIComboBoxCreate((const HIRect *)lparg0, (CFStringRef)arg1, (const ControlFontStyleRec *)lparg2, (CFArrayRef)arg3, (OptionBits)arg4, (HIViewRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg2 && lparg2) setControlFontStyleRecFields(env, arg2, lparg2);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIComboBoxCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxGetItemCount
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxGetItemCount)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxGetItemCount_FUNC);
	rc = (jint)HIComboBoxGetItemCount((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIComboBoxGetItemCount_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxInsertTextItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxInsertTextItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxInsertTextItemAtIndex_FUNC);
	rc = (jint)HIComboBoxInsertTextItemAtIndex((HIViewRef)arg0, (CFIndex)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, HIComboBoxInsertTextItemAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxIsListVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(HIComboBoxIsListVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxIsListVisible_FUNC);
/*
	rc = (jboolean)HIComboBoxIsListVisible((HIViewRef)arg0);
*/
	{
		LOAD_FUNCTION(fp, HIComboBoxIsListVisible)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(HIViewRef))fp)((HIViewRef)arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, HIComboBoxIsListVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxRemoveItemAtIndex
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxRemoveItemAtIndex)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxRemoveItemAtIndex_FUNC);
	rc = (jint)HIComboBoxRemoveItemAtIndex((HIViewRef)arg0, (CFIndex)arg1);
	OS_NATIVE_EXIT(env, that, HIComboBoxRemoveItemAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_HIComboBoxSetListVisible
JNIEXPORT jint JNICALL OS_NATIVE(HIComboBoxSetListVisible)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIComboBoxSetListVisible_FUNC);
/*
	rc = (jint)HIComboBoxSetListVisible((HIViewRef)arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, HIComboBoxSetListVisible)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HIViewRef, jboolean))fp)((HIViewRef)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, HIComboBoxSetListVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_HICopyAccessibilityRoleDescription
JNIEXPORT jint JNICALL OS_NATIVE(HICopyAccessibilityRoleDescription)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HICopyAccessibilityRoleDescription_FUNC);
	rc = (jint)HICopyAccessibilityRoleDescription((CFStringRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, HICopyAccessibilityRoleDescription_FUNC);
	return rc;
}
#endif

#ifndef NO_HICreateTransformedCGImage
JNIEXPORT jint JNICALL OS_NATIVE(HICreateTransformedCGImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HICreateTransformedCGImage_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)HICreateTransformedCGImage((CGImageRef)arg0, arg1, (CGImageRef *)lparg2);
*/
	{
		LOAD_FUNCTION(fp, HICreateTransformedCGImage)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(CGImageRef, jint, CGImageRef *))fp)((CGImageRef)arg0, arg1, (CGImageRef *)lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HICreateTransformedCGImage_FUNC);
	return rc;
}
#endif

#ifndef NO_HIGrowBoxViewSetTransparent
JNIEXPORT jint JNICALL OS_NATIVE(HIGrowBoxViewSetTransparent)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIGrowBoxViewSetTransparent_FUNC);
	rc = (jint)HIGrowBoxViewSetTransparent((HIViewRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, HIGrowBoxViewSetTransparent_FUNC);
	return rc;
}
#endif

#ifndef NO_HIObjectCopyClassID
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectCopyClassID)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIObjectCopyClassID_FUNC);
	rc = (jint)HIObjectCopyClassID((HIObjectRef)arg0);
	OS_NATIVE_EXIT(env, that, HIObjectCopyClassID_FUNC);
	return rc;
}
#endif

#ifndef NO_HIObjectCreate
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIObjectCreate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIObjectCreate((CFStringRef)arg0, (EventRef)arg1, (HIObjectRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HIObjectCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HIObjectRegisterSubclass
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectRegisterSubclass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jint arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIObjectRegisterSubclass_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)HIObjectRegisterSubclass((CFStringRef)arg0, (CFStringRef)arg1, (OptionBits)arg2, (EventHandlerUPP)arg3, (UInt32)arg4, (const EventTypeSpec *)lparg5, (void *)arg6, (HIObjectClassRef *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, HIObjectRegisterSubclass_FUNC);
	return rc;
}
#endif

#ifndef NO_HIObjectSetAccessibilityIgnored
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectSetAccessibilityIgnored)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIObjectSetAccessibilityIgnored_FUNC);
	rc = (jint)HIObjectSetAccessibilityIgnored((HIObjectRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, HIObjectSetAccessibilityIgnored_FUNC);
	return rc;
}
#endif

#ifndef NO_HIObjectSetAuxiliaryAccessibilityAttribute
JNIEXPORT jint JNICALL OS_NATIVE(HIObjectSetAuxiliaryAccessibilityAttribute)
	(JNIEnv *env, jclass that, jint arg0, jlong arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIObjectSetAuxiliaryAccessibilityAttribute_FUNC);
	rc = (jint)HIObjectSetAuxiliaryAccessibilityAttribute((HIObjectRef)arg0, (UInt64)arg1, (CFStringRef)arg2, (CFTypeRef)arg3);
	OS_NATIVE_EXIT(env, that, HIObjectSetAuxiliaryAccessibilityAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_HIScrollViewCreate
JNIEXPORT jint JNICALL OS_NATIVE(HIScrollViewCreate)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIScrollViewCreate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)HIScrollViewCreate(arg0, (HIViewRef*)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, HIScrollViewCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HIScrollViewSetScrollBarAutoHide
JNIEXPORT jint JNICALL OS_NATIVE(HIScrollViewSetScrollBarAutoHide)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIScrollViewSetScrollBarAutoHide_FUNC);
	rc = (jint)HIScrollViewSetScrollBarAutoHide((HIViewRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, HIScrollViewSetScrollBarAutoHide_FUNC);
	return rc;
}
#endif

#ifndef NO_HISearchFieldChangeAttributes
JNIEXPORT jint JNICALL OS_NATIVE(HISearchFieldChangeAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HISearchFieldChangeAttributes_FUNC);
	rc = (jint)HISearchFieldChangeAttributes((HIViewRef)arg0, (OptionBits)arg1, (OptionBits)arg2);
	OS_NATIVE_EXIT(env, that, HISearchFieldChangeAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_HISearchFieldCopyDescriptiveText
JNIEXPORT jint JNICALL OS_NATIVE(HISearchFieldCopyDescriptiveText)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HISearchFieldCopyDescriptiveText_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)HISearchFieldCopyDescriptiveText((HIViewRef)arg0, (CFStringRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, HISearchFieldCopyDescriptiveText_FUNC);
	return rc;
}
#endif

#ifndef NO_HISearchFieldCreate
JNIEXPORT jint JNICALL OS_NATIVE(HISearchFieldCreate)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	CGRect _arg0, *lparg0=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HISearchFieldCreate_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)HISearchFieldCreate(lparg0, arg1, (MenuRef)arg2, (CFStringRef)arg3, (HIViewRef*)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HISearchFieldCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HISearchFieldGetAttributes
JNIEXPORT jint JNICALL OS_NATIVE(HISearchFieldGetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HISearchFieldGetAttributes_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)HISearchFieldGetAttributes((HIViewRef)arg0, (OptionBits*)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, HISearchFieldGetAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_HISearchFieldSetDescriptiveText
JNIEXPORT jint JNICALL OS_NATIVE(HISearchFieldSetDescriptiveText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HISearchFieldSetDescriptiveText_FUNC);
	rc = (jint)HISearchFieldSetDescriptiveText((HIViewRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, HISearchFieldSetDescriptiveText_FUNC);
	return rc;
}
#endif

#ifndef NO_HIShapeCreateWithQDRgn
JNIEXPORT jint JNICALL OS_NATIVE(HIShapeCreateWithQDRgn)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIShapeCreateWithQDRgn_FUNC);
/*
	rc = (jint)HIShapeCreateWithQDRgn(arg0);
*/
	{
		LOAD_FUNCTION(fp, HIShapeCreateWithQDRgn)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint))fp)(arg0);
		}
	}
	OS_NATIVE_EXIT(env, that, HIShapeCreateWithQDRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_HIShapeReplacePathInCGContext
JNIEXPORT jint JNICALL OS_NATIVE(HIShapeReplacePathInCGContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIShapeReplacePathInCGContext_FUNC);
/*
	rc = (jint)HIShapeReplacePathInCGContext(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, HIShapeReplacePathInCGContext)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, HIShapeReplacePathInCGContext_FUNC);
	return rc;
}
#endif

#ifndef NO_HITextViewCreate
JNIEXPORT jint JNICALL OS_NATIVE(HITextViewCreate)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jintArray arg3)
{
	CGRect _arg0, *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HITextViewCreate_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)HITextViewCreate(lparg0, arg1, arg2, (HIViewRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HITextViewCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HITextViewGetTXNObject
JNIEXPORT jint JNICALL OS_NATIVE(HITextViewGetTXNObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HITextViewGetTXNObject_FUNC);
	rc = (jint)HITextViewGetTXNObject((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HITextViewGetTXNObject_FUNC);
	return rc;
}
#endif

#ifndef NO_HITextViewSetBackgroundColor
JNIEXPORT jint JNICALL OS_NATIVE(HITextViewSetBackgroundColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HITextViewSetBackgroundColor_FUNC);
	rc = (jint)HITextViewSetBackgroundColor((HIViewRef)arg0, (CGColorRef)arg1);
	OS_NATIVE_EXIT(env, that, HITextViewSetBackgroundColor_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawBackground
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawBackground)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeBackgroundDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawBackground_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeBackgroundDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawBackground(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemeBackgroundDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawButton
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawButton)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jobject arg4)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeButtonDrawInfo _arg1, *lparg1=NULL;
	CGRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawButton_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeButtonDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = &_arg4) == NULL) goto fail;
	rc = (jint)HIThemeDrawButton((const HIRect *)lparg0, (const HIThemeButtonDrawInfo *)lparg1, (CGContextRef)arg2, (HIThemeOrientation)arg3, (HIRect *)lparg4);
fail:
	if (arg4 && lparg4) setCGRectFields(env, arg4, lparg4);
	if (arg1 && lparg1) setHIThemeButtonDrawInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIThemeDrawButton_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawFocusRect
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawFocusRect)
	(JNIEnv *env, jclass that, jobject arg0, jboolean arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawFocusRect_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)HIThemeDrawFocusRect(lparg0, arg1, (CGContextRef)arg2, arg3);
fail:
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawFocusRect_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawFrame
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawFrame)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeFrameDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawFrame_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeFrameDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawFrame(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemeFrameDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawGenericWell
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawGenericWell)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeButtonDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawGenericWell_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeButtonDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawGenericWell((const HIRect *)lparg0, (const HIThemeButtonDrawInfo *)lparg1, (CGContextRef)arg2, (HIThemeOrientation)arg3);
fail:
	if (arg1 && lparg1) setHIThemeButtonDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawGenericWell_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawGroupBox
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawGroupBox)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeGroupBoxDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawGroupBox_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeGroupBoxDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawGroupBox(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemeGroupBoxDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawGroupBox_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawGrowBox
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawGrowBox)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGPoint _arg0, *lparg0=NULL;
	HIThemeGrowBoxDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawGrowBox_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeGrowBoxDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawGrowBox(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemeGrowBoxDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawGrowBox_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawPopupArrow
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawPopupArrow)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemePopupArrowDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawPopupArrow_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemePopupArrowDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawPopupArrow(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemePopupArrowDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawPopupArrow_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawSeparator
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawSeparator)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeSeparatorDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawSeparator_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeSeparatorDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawSeparator(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setHIThemeSeparatorDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawTab
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawTab)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3, jobject arg4)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeTabDrawInfo _arg1, *lparg1=NULL;
	CGRect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawTab_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeTabDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg4) if ((lparg4 = &_arg4) == NULL) goto fail;
	rc = (jint)HIThemeDrawTab((const HIRect *)lparg0, (const HIThemeTabDrawInfo *)lparg1, (CGContextRef)arg2, (HIThemeOrientation)arg3, (HIRect *)lparg4);
fail:
	if (arg4 && lparg4) setCGRectFields(env, arg4, lparg4);
	if (arg1 && lparg1) setHIThemeTabDrawInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIThemeDrawTab_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawTabPane
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawTabPane)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeTabPaneDrawInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawTabPane_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeTabPaneDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawTabPane((const HIRect *)lparg0, (const HIThemeTabPaneDrawInfo *)lparg1, (CGContextRef)arg2, (HIThemeOrientation)arg3);
fail:
	if (arg1 && lparg1) setHIThemeTabPaneDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawTabPane_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawTextBox
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawTextBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2, jint arg3, jint arg4)
{
	CGRect _arg1, *lparg1=NULL;
	HIThemeTextInfo _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawTextBox_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getHIThemeTextInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)HIThemeDrawTextBox((CFStringRef)arg0, lparg1, lparg2, (CGContextRef)arg3, arg4);
fail:
	if (arg2 && lparg2) setHIThemeTextInfoFields(env, arg2, lparg2);
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIThemeDrawTextBox_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeDrawTrack
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeDrawTrack)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jint arg3)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeDrawTrack_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeDrawTrack(lparg0, lparg1, (CGContextRef)arg2, arg3);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeDrawTrack_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetButtonBackgroundBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetButtonBackgroundBounds)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeButtonDrawInfo _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetButtonBackgroundBounds_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeButtonDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)HIThemeGetButtonBackgroundBounds(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) setHIThemeButtonDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetButtonBackgroundBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetButtonContentBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetButtonContentBounds)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	CGRect _arg0, *lparg0=NULL;
	HIThemeButtonDrawInfo _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetButtonContentBounds_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIThemeButtonDrawInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)HIThemeGetButtonContentBounds(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) setHIThemeButtonDrawInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetButtonContentBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetScrollBarTrackRect
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetScrollBarTrackRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jboolean arg2, jobject arg3)
{
	CGRect _arg0, *lparg0=NULL;
	HIScrollBarTrackInfo _arg1, *lparg1=NULL;
	CGRect _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetScrollBarTrackRect_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIScrollBarTrackInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getCGRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)HIThemeGetScrollBarTrackRect(lparg0, lparg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setCGRectFields(env, arg3, lparg3);
	if (arg1 && lparg1) setHIScrollBarTrackInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetScrollBarTrackRect_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTextDimensions
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTextDimensions)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jobject arg2, jfloatArray arg3, jfloatArray arg4, jfloatArray arg5)
{
	HIThemeTextInfo _arg2, *lparg2=NULL;
	jfloat *lparg3=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTextDimensions_FUNC);
	if (arg2) if ((lparg2 = getHIThemeTextInfoFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetFloatArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)HIThemeGetTextDimensions((CFStringRef)arg0, arg1, lparg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseFloatArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setHIThemeTextInfoFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, HIThemeGetTextDimensions_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTrackBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTrackBounds)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTrackBounds_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIThemeGetTrackBounds(lparg0, lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetTrackBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTrackLiveValue
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTrackLiveValue)
	(JNIEnv *env, jclass that, jobject arg0, jfloat arg1, jintArray arg2)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTrackLiveValue_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIThemeGetTrackLiveValue(lparg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetTrackLiveValue_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTrackPartBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTrackPartBounds)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jobject arg2)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTrackPartBounds_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)HIThemeGetTrackPartBounds(lparg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetTrackPartBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTrackThumbPositionFromBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTrackThumbPositionFromBounds)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jfloatArray arg2)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGRect _arg1, *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTrackThumbPositionFromBounds_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIThemeGetTrackThumbPositionFromBounds(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetTrackThumbPositionFromBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeGetTrackThumbPositionFromOffset
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeGetTrackThumbPositionFromOffset)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jfloatArray arg2)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGPoint _arg1, *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeGetTrackThumbPositionFromOffset_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIThemeGetTrackThumbPositionFromOffset((HIThemeTrackDrawInfo *)lparg0, (HIPoint *)lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeGetTrackThumbPositionFromOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeHitTestScrollBarArrows
JNIEXPORT jboolean JNICALL OS_NATIVE(HIThemeHitTestScrollBarArrows)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jboolean arg2, jobject arg3, jobject arg4, jshortArray arg5)
{
	CGRect _arg0, *lparg0=NULL;
	HIScrollBarTrackInfo _arg1, *lparg1=NULL;
	CGPoint _arg3, *lparg3=NULL;
	CGRect _arg4, *lparg4=NULL;
	jshort *lparg5=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeHitTestScrollBarArrows_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getHIScrollBarTrackInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getCGPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getCGRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jboolean)HIThemeHitTestScrollBarArrows(lparg0, lparg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) setCGRectFields(env, arg4, lparg4);
	if (arg3 && lparg3) setCGPointFields(env, arg3, lparg3);
	if (arg1 && lparg1) setHIScrollBarTrackInfoFields(env, arg1, lparg1);
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeHitTestScrollBarArrows_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeHitTestTrack
JNIEXPORT jboolean JNICALL OS_NATIVE(HIThemeHitTestTrack)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jshortArray arg2)
{
	HIThemeTrackDrawInfo _arg0, *lparg0=NULL;
	CGPoint _arg1, *lparg1=NULL;
	jshort *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeHitTestTrack_FUNC);
	if (arg0) if ((lparg0 = getHIThemeTrackDrawInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)HIThemeHitTestTrack(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	if (arg0 && lparg0) setHIThemeTrackDrawInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIThemeHitTestTrack_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeSetFill
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeSetFill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeSetFill_FUNC);
/*
	rc = (jint)HIThemeSetFill((ThemeBrush)arg0, (void *)arg1, (CGContextRef)arg2, (HIThemeOrientation)arg3);
*/
	{
		LOAD_FUNCTION(fp, HIThemeSetFill)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(ThemeBrush, void *, CGContextRef, HIThemeOrientation))fp)((ThemeBrush)arg0, (void *)arg1, (CGContextRef)arg2, (HIThemeOrientation)arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, HIThemeSetFill_FUNC);
	return rc;
}
#endif

#ifndef NO_HIThemeSetTextFill
JNIEXPORT jint JNICALL OS_NATIVE(HIThemeSetTextFill)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIThemeSetTextFill_FUNC);
/*
	rc = (jint)HIThemeSetTextFill(arg0, arg1, arg2, arg3);
*/
	{
		LOAD_FUNCTION(fp, HIThemeSetTextFill)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint, jint))fp)(arg0, arg1, arg2, arg3);
		}
	}
	OS_NATIVE_EXIT(env, that, HIThemeSetTextFill_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewAddSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewAddSubview)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewAddSubview_FUNC);
	rc = (jint)HIViewAddSubview((HIViewRef)arg0, (HIViewRef)arg1);
	OS_NATIVE_EXIT(env, that, HIViewAddSubview_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewChangeAttributes
JNIEXPORT jint JNICALL OS_NATIVE(HIViewChangeAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewChangeAttributes_FUNC);
	rc = (jint)HIViewChangeAttributes((HIViewRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, HIViewChangeAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewChangeFeatures
JNIEXPORT jint JNICALL OS_NATIVE(HIViewChangeFeatures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewChangeFeatures_FUNC);
	rc = (jint)HIViewChangeFeatures((HIViewRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, HIViewChangeFeatures_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewClick
JNIEXPORT jint JNICALL OS_NATIVE(HIViewClick)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewClick_FUNC);
	rc = (jint)HIViewClick((HIViewRef)arg0, (EventRef)arg1);
	OS_NATIVE_EXIT(env, that, HIViewClick_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewConvertPoint
JNIEXPORT jint JNICALL OS_NATIVE(HIViewConvertPoint)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewConvertPoint_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)HIViewConvertPoint((HIPoint *)lparg0, (HIViewRef)arg1, (HIViewRef)arg2);
fail:
	if (arg0 && lparg0) setCGPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIViewConvertPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewConvertRect
JNIEXPORT jint JNICALL OS_NATIVE(HIViewConvertRect)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGRect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewConvertRect_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)HIViewConvertRect(lparg0, (HIViewRef)arg1, (HIViewRef)arg2);
fail:
	if (arg0 && lparg0) setCGRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HIViewConvertRect_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewConvertRegion
JNIEXPORT jint JNICALL OS_NATIVE(HIViewConvertRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewConvertRegion_FUNC);
	rc = (jint)HIViewConvertRegion((RgnHandle)arg0, (HIViewRef)arg1, (HIViewRef)arg2);
	OS_NATIVE_EXIT(env, that, HIViewConvertRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewCreateOffscreenImage
JNIEXPORT jint JNICALL OS_NATIVE(HIViewCreateOffscreenImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	CGRect _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewCreateOffscreenImage_FUNC);
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)HIViewCreateOffscreenImage((HIViewRef)arg0, (OptionBits)arg1, (HIRect *)lparg2, (CGImageRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, HIViewCreateOffscreenImage_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewDrawCGImage
JNIEXPORT jint JNICALL OS_NATIVE(HIViewDrawCGImage)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewDrawCGImage_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewDrawCGImage((CGContextRef)arg0, lparg1, (CGImageRef)arg2);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewDrawCGImage_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewFindByID
JNIEXPORT jint JNICALL OS_NATIVE(HIViewFindByID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewFindByID_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIViewFindByID((HIViewRef)arg0, *(HIViewID *)arg1, (HIViewRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HIViewFindByID_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetBounds
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetBounds_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewGetBounds((HIViewRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewGetBounds_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetFeatures
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetFeatures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetFeatures_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)HIViewGetFeatures((HIViewRef)arg0, (HIViewFeatures *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, HIViewGetFeatures_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetFirstSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetFirstSubview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetFirstSubview_FUNC);
	rc = (jint)HIViewGetFirstSubview((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetFirstSubview_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetFrame
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetFrame)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetFrame_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewGetFrame((HIViewRef)arg0, (HIRect *)lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewGetFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetLastSubview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetLastSubview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetLastSubview_FUNC);
	rc = (jint)HIViewGetLastSubview((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetLastSubview_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetLayoutInfo
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetLayoutInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	HILayoutInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetLayoutInfo_FUNC);
	if (arg1) if ((lparg1 = getHILayoutInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewGetLayoutInfo((HIViewRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setHILayoutInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewGetLayoutInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetNeedsDisplay
JNIEXPORT jboolean JNICALL OS_NATIVE(HIViewGetNeedsDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetNeedsDisplay_FUNC);
	rc = (jboolean)HIViewGetNeedsDisplay((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetNeedsDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetNextView
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetNextView)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetNextView_FUNC);
	rc = (jint)HIViewGetNextView((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetNextView_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetRoot
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetRoot)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetRoot_FUNC);
	rc = (jint)HIViewGetRoot((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetRoot_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetSizeConstraints
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetSizeConstraints)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	CGRect _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetSizeConstraints_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)HIViewGetSizeConstraints((HIViewRef)arg0, (HISize *)lparg1, (HISize *)lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewGetSizeConstraints_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetSubviewHit
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetSubviewHit)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jboolean arg2, jintArray arg3)
{
	CGPoint _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetSubviewHit_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)HIViewGetSubviewHit((HIViewRef)arg0, (CGPoint *)lparg1, (Boolean)arg2, (HIViewRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewGetSubviewHit_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetSuperview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetSuperview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetSuperview_FUNC);
	rc = (jint)HIViewGetSuperview((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewGetSuperview_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewGetViewForMouseEvent
JNIEXPORT jint JNICALL OS_NATIVE(HIViewGetViewForMouseEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewGetViewForMouseEvent_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)HIViewGetViewForMouseEvent((HIViewRef)arg0, (EventRef)arg1, (HIViewRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, HIViewGetViewForMouseEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewIsDrawingEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(HIViewIsDrawingEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewIsDrawingEnabled_FUNC);
	rc = (jboolean)HIViewIsDrawingEnabled((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewIsDrawingEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewIsVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(HIViewIsVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewIsVisible_FUNC);
	rc = (jboolean)HIViewIsVisible((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewIsVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewRegionChanged
JNIEXPORT jint JNICALL OS_NATIVE(HIViewRegionChanged)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewRegionChanged_FUNC);
	rc = (jint)HIViewRegionChanged((HIViewRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, HIViewRegionChanged_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewRemoveFromSuperview
JNIEXPORT jint JNICALL OS_NATIVE(HIViewRemoveFromSuperview)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewRemoveFromSuperview_FUNC);
	rc = (jint)HIViewRemoveFromSuperview((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewRemoveFromSuperview_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewRender
JNIEXPORT jint JNICALL OS_NATIVE(HIViewRender)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewRender_FUNC);
	rc = (jint)HIViewRender((HIViewRef)arg0);
	OS_NATIVE_EXIT(env, that, HIViewRender_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewScrollRect
JNIEXPORT jint JNICALL OS_NATIVE(HIViewScrollRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jfloat arg2, jfloat arg3)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewScrollRect_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewScrollRect((HIViewRef)arg0, lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewScrollRect_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetBoundsOrigin
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetBoundsOrigin)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetBoundsOrigin_FUNC);
	rc = (jint)HIViewSetBoundsOrigin((HIViewRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, HIViewSetBoundsOrigin_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetDrawingEnabled
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetDrawingEnabled)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetDrawingEnabled_FUNC);
	rc = (jint)HIViewSetDrawingEnabled((HIViewRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, HIViewSetDrawingEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetFrame
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetFrame)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetFrame_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewSetFrame((HIViewRef)arg0, (const HIRect *)lparg1);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewSetFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetLayoutInfo
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetLayoutInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	HILayoutInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetLayoutInfo_FUNC);
	if (arg1) if ((lparg1 = getHILayoutInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)HIViewSetLayoutInfo((HIViewRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setHILayoutInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HIViewSetLayoutInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetNeedsDisplay
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetNeedsDisplay)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetNeedsDisplay_FUNC);
	rc = (jint)HIViewSetNeedsDisplay((HIViewRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, HIViewSetNeedsDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetNeedsDisplayInRegion
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetNeedsDisplayInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetNeedsDisplayInRegion_FUNC);
	rc = (jint)HIViewSetNeedsDisplayInRegion((HIViewRef)arg0, (RgnHandle)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, HIViewSetNeedsDisplayInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetVisible
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetVisible)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetVisible_FUNC);
	rc = (jint)HIViewSetVisible((HIViewRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, HIViewSetVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSetZOrder
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSetZOrder)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSetZOrder_FUNC);
	rc = (jint)HIViewSetZOrder((HIViewRef)arg0, (HIViewZOrderOp)arg1, (HIViewRef)arg2);
	OS_NATIVE_EXIT(env, that, HIViewSetZOrder_FUNC);
	return rc;
}
#endif

#ifndef NO_HIViewSimulateClick
JNIEXPORT jint JNICALL OS_NATIVE(HIViewSimulateClick)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2, jshortArray arg3)
{
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HIViewSimulateClick_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)HIViewSimulateClick((HIViewRef)arg0, (HIViewPartCode)arg1, (UInt32)arg2, (ControlPartCode *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, HIViewSimulateClick_FUNC);
	return rc;
}
#endif

#ifndef NO_HIWindowFlush
JNIEXPORT void JNICALL OS_NATIVE(HIWindowFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, HIWindowFlush_FUNC);
	HIWindowFlush((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, HIWindowFlush_FUNC);
}
#endif

#ifndef NO_HIWindowIsDocumentModalTarget
JNIEXPORT jboolean JNICALL OS_NATIVE(HIWindowIsDocumentModalTarget)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, HIWindowIsDocumentModalTarget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)HIWindowIsDocumentModalTarget((WindowRef)arg0, (WindowRef*)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, HIWindowIsDocumentModalTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_HLock
JNIEXPORT void JNICALL OS_NATIVE(HLock)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, HLock_FUNC);
	HLock((Handle)arg0);
	OS_NATIVE_EXIT(env, that, HLock_FUNC);
}
#endif

#ifndef NO_HMDisplayTag
JNIEXPORT jint JNICALL OS_NATIVE(HMDisplayTag)
	(JNIEnv *env, jclass that, jobject arg0)
{
	HMHelpContentRec _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HMDisplayTag_FUNC);
	if (arg0) if ((lparg0 = getHMHelpContentRecFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)HMDisplayTag((const HMHelpContentRec *)lparg0);
fail:
	if (arg0 && lparg0) setHMHelpContentRecFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, HMDisplayTag_FUNC);
	return rc;
}
#endif

#ifndef NO_HMGetTagDelay
JNIEXPORT jint JNICALL OS_NATIVE(HMGetTagDelay)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HMGetTagDelay_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)HMGetTagDelay(lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, HMGetTagDelay_FUNC);
	return rc;
}
#endif

#ifndef NO_HMHideTag
JNIEXPORT jint JNICALL OS_NATIVE(HMHideTag)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HMHideTag_FUNC);
	rc = (jint)HMHideTag();
	OS_NATIVE_EXIT(env, that, HMHideTag_FUNC);
	return rc;
}
#endif

#ifndef NO_HMInstallControlContentCallback
JNIEXPORT void JNICALL OS_NATIVE(HMInstallControlContentCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, HMInstallControlContentCallback_FUNC);
	HMInstallControlContentCallback((ControlRef)arg0, (HMControlContentUPP)arg1);
	OS_NATIVE_EXIT(env, that, HMInstallControlContentCallback_FUNC);
}
#endif

#ifndef NO_HMSetTagDelay
JNIEXPORT jint JNICALL OS_NATIVE(HMSetTagDelay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HMSetTagDelay_FUNC);
	rc = (jint)HMSetTagDelay(arg0);
	OS_NATIVE_EXIT(env, that, HMSetTagDelay_FUNC);
	return rc;
}
#endif

#ifndef NO_HUnlock
JNIEXPORT void JNICALL OS_NATIVE(HUnlock)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, HUnlock_FUNC);
	HUnlock((Handle)arg0);
	OS_NATIVE_EXIT(env, that, HUnlock_FUNC);
}
#endif

#ifndef NO_HandleControlClick
JNIEXPORT jshort JNICALL OS_NATIVE(HandleControlClick)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	Point _arg1, *lparg1=NULL;
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, HandleControlClick_FUNC);
	if (arg1) if ((lparg1 = getPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jshort)HandleControlClick((ControlRef)arg0, *lparg1, (EventModifiers)arg2, (ControlActionUPP)arg3);
fail:
	if (arg1 && lparg1) setPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HandleControlClick_FUNC);
	return rc;
}
#endif

#ifndef NO_HandleControlSetCursor
JNIEXPORT jint JNICALL OS_NATIVE(HandleControlSetCursor)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jbooleanArray arg3)
{
	Point _arg1, *lparg1=NULL;
	jboolean *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, HandleControlSetCursor_FUNC);
	if (arg1) if ((lparg1 = getPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetBooleanArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)HandleControlSetCursor((ControlRef)arg0, *lparg1, (EventModifiers)arg2, (Boolean *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseBooleanArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, HandleControlSetCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_HiWord
JNIEXPORT jshort JNICALL OS_NATIVE(HiWord)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, HiWord_FUNC);
	rc = (jshort)HiWord(arg0);
	OS_NATIVE_EXIT(env, that, HiWord_FUNC);
	return rc;
}
#endif

#ifndef NO_HideWindow
JNIEXPORT void JNICALL OS_NATIVE(HideWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, HideWindow_FUNC);
	HideWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, HideWindow_FUNC);
}
#endif

#ifndef NO_HiliteMenu
JNIEXPORT void JNICALL OS_NATIVE(HiliteMenu)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, HiliteMenu_FUNC);
	HiliteMenu((MenuID)arg0);
	OS_NATIVE_EXIT(env, that, HiliteMenu_FUNC);
}
#endif

#ifndef NO_IconRefToIconFamily
JNIEXPORT jint JNICALL OS_NATIVE(IconRefToIconFamily)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, IconRefToIconFamily_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)IconRefToIconFamily((IconRef)arg0, (IconSelectorValue)arg1, (IconFamilyHandle *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, IconRefToIconFamily_FUNC);
	return rc;
}
#endif

#ifndef NO_InitContextualMenus
JNIEXPORT jint JNICALL OS_NATIVE(InitContextualMenus)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InitContextualMenus_FUNC);
	rc = (jint)InitContextualMenus();
	OS_NATIVE_EXIT(env, that, InitContextualMenus_FUNC);
	return rc;
}
#endif

#ifndef NO_InitCursor
JNIEXPORT void JNICALL OS_NATIVE(InitCursor)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, InitCursor_FUNC);
	InitCursor();
	OS_NATIVE_EXIT(env, that, InitCursor_FUNC);
}
#endif

#ifndef NO_InitDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(InitDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCallbacks _arg0={0}, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InitDataBrowserCallbacks_FUNC);
	if (arg0) if ((lparg0 = getDataBrowserCallbacksFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)InitDataBrowserCallbacks((DataBrowserCallbacks *)lparg0);
fail:
	if (arg0 && lparg0) setDataBrowserCallbacksFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, InitDataBrowserCallbacks_FUNC);
	return rc;
}
#endif

#ifndef NO_InitDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(InitDataBrowserCustomCallbacks)
	(JNIEnv *env, jclass that, jobject arg0)
{
	DataBrowserCustomCallbacks _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InitDataBrowserCustomCallbacks_FUNC);
	if (arg0) if ((lparg0 = getDataBrowserCustomCallbacksFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)InitDataBrowserCustomCallbacks(lparg0);
fail:
	if (arg0 && lparg0) setDataBrowserCustomCallbacksFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, InitDataBrowserCustomCallbacks_FUNC);
	return rc;
}
#endif

#ifndef NO_InsertMenu
JNIEXPORT void JNICALL OS_NATIVE(InsertMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, InsertMenu_FUNC);
	InsertMenu((MenuRef)arg0, (MenuID)arg1);
	OS_NATIVE_EXIT(env, that, InsertMenu_FUNC);
}
#endif

#ifndef NO_InsertMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(InsertMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InsertMenuItemTextWithCFString_FUNC);
	rc = (jint)InsertMenuItemTextWithCFString((MenuRef)arg0, (CFStringRef)arg1, (MenuItemIndex)arg2, (MenuItemAttributes)arg3, (MenuCommand)arg4);
	OS_NATIVE_EXIT(env, that, InsertMenuItemTextWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_InstallEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InstallEventHandler_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)InstallEventHandler((EventTargetRef)arg0, (EventHandlerUPP)arg1, (UInt32)arg2, (const EventTypeSpec *)lparg3, (void *)arg4, (EventHandlerRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, InstallEventHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_InstallEventLoopIdleTimer
JNIEXPORT jint JNICALL OS_NATIVE(InstallEventLoopIdleTimer)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InstallEventLoopIdleTimer_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)InstallEventLoopIdleTimer((EventLoopRef)arg0, (EventTimerInterval)arg1, (EventTimerInterval)arg2, (EventLoopIdleTimerUPP)arg3, (void *)arg4, (EventLoopTimerRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, InstallEventLoopIdleTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_InstallEventLoopTimer
JNIEXPORT jint JNICALL OS_NATIVE(InstallEventLoopTimer)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InstallEventLoopTimer_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)InstallEventLoopTimer((EventLoopRef)arg0, (EventTimerInterval)arg1, (EventTimerInterval)arg2, (EventLoopTimerUPP)arg3, (void *)arg4, (EventLoopTimerRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, InstallEventLoopTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_InstallReceiveHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallReceiveHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InstallReceiveHandler_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)InstallReceiveHandler((DragReceiveHandlerUPP)arg0, (WindowRef)arg1, (void *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, InstallReceiveHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_InstallTrackingHandler
JNIEXPORT jint JNICALL OS_NATIVE(InstallTrackingHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, InstallTrackingHandler_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)InstallTrackingHandler((DragTrackingHandlerUPP)arg0, (WindowRef)arg1, (void *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, InstallTrackingHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_InvalWindowRect
JNIEXPORT void JNICALL OS_NATIVE(InvalWindowRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, InvalWindowRect_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	InvalWindowRect((WindowRef)arg0, (const Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, InvalWindowRect_FUNC);
}
#endif

#ifndef NO_InvalWindowRgn
JNIEXPORT void JNICALL OS_NATIVE(InvalWindowRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, InvalWindowRgn_FUNC);
	InvalWindowRgn((WindowRef)arg0, (RgnHandle)arg1);
	OS_NATIVE_EXIT(env, that, InvalWindowRgn_FUNC);
}
#endif

#ifndef NO_InvertRect
JNIEXPORT void JNICALL OS_NATIVE(InvertRect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, InvertRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	InvertRect((const Rect *)lparg0);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, InvertRect_FUNC);
}
#endif

#ifndef NO_IsControlActive
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsControlActive_FUNC);
	rc = (jboolean)IsControlActive((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, IsControlActive_FUNC);
	return rc;
}
#endif

#ifndef NO_IsControlEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsControlEnabled_FUNC);
	rc = (jboolean)IsControlEnabled((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, IsControlEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_IsControlVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsControlVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsControlVisible_FUNC);
	rc = (jboolean)IsControlVisible((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, IsControlVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_IsDataBrowserItemSelected
JNIEXPORT jboolean JNICALL OS_NATIVE(IsDataBrowserItemSelected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsDataBrowserItemSelected_FUNC);
	rc = (jboolean)IsDataBrowserItemSelected((ControlRef)arg0, (DataBrowserItemID)arg1);
	OS_NATIVE_EXIT(env, that, IsDataBrowserItemSelected_FUNC);
	return rc;
}
#endif

#ifndef NO_IsEventInQueue
JNIEXPORT jboolean JNICALL OS_NATIVE(IsEventInQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsEventInQueue_FUNC);
	rc = (jboolean)IsEventInQueue((EventQueueRef)arg0, (EventRef)arg1);
	OS_NATIVE_EXIT(env, that, IsEventInQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_IsMenuCommandEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsMenuCommandEnabled)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsMenuCommandEnabled_FUNC);
	rc = (jboolean)IsMenuCommandEnabled((MenuRef)arg0, (MenuCommand)arg1);
	OS_NATIVE_EXIT(env, that, IsMenuCommandEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_IsMenuItemEnabled
JNIEXPORT jboolean JNICALL OS_NATIVE(IsMenuItemEnabled)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsMenuItemEnabled_FUNC);
	rc = (jboolean)IsMenuItemEnabled((MenuRef)arg0, (MenuItemIndex)arg1);
	OS_NATIVE_EXIT(env, that, IsMenuItemEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_IsMenuKeyEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(IsMenuKeyEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jshortArray arg4)
{
	jint *lparg3=NULL;
	jshort *lparg4=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsMenuKeyEvent_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)IsMenuKeyEvent((MenuRef)arg0, (EventRef)arg1, (MenuEventOptions)arg2, (MenuRef *)lparg3, (MenuItemIndex *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, IsMenuKeyEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_IsValidControlHandle
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidControlHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsValidControlHandle_FUNC);
	rc = (jboolean)IsValidControlHandle((ControlRef)arg0);
	OS_NATIVE_EXIT(env, that, IsValidControlHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_IsValidMenu
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsValidMenu_FUNC);
	rc = (jboolean)IsValidMenu((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, IsValidMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_IsValidWindowPtr
JNIEXPORT jboolean JNICALL OS_NATIVE(IsValidWindowPtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsValidWindowPtr_FUNC);
	rc = (jboolean)IsValidWindowPtr((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, IsValidWindowPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowActive
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowActive_FUNC);
	rc = (jboolean)IsWindowActive((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowActive_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowCollapsed
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowCollapsed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowCollapsed_FUNC);
	rc = (jboolean)IsWindowCollapsed((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowCollapsed_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowModified
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowModified)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowModified_FUNC);
	rc = (jboolean)IsWindowModified((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowModified_FUNC);
	return rc;
}
#endif

#ifndef NO_IsWindowVisible
JNIEXPORT jboolean JNICALL OS_NATIVE(IsWindowVisible)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, IsWindowVisible_FUNC);
	rc = (jboolean)IsWindowVisible((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, IsWindowVisible_FUNC);
	return rc;
}
#endif

#ifndef NO_JSEvaluateScript
JNIEXPORT jint JNICALL OS_NATIVE(JSEvaluateScript)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, JSEvaluateScript_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)JSEvaluateScript((JSContextRef)arg0, (JSStringRef)arg1, (JSObjectRef)arg2, (JSStringRef)arg3, arg4, (JSValueRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, JSEvaluateScript_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringCreateWithUTF8CString
JNIEXPORT jint JNICALL OS_NATIVE(JSStringCreateWithUTF8CString)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, JSStringCreateWithUTF8CString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)JSStringCreateWithUTF8CString((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, JSStringCreateWithUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringRelease
JNIEXPORT void JNICALL OS_NATIVE(JSStringRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, JSStringRelease_FUNC);
	JSStringRelease((JSStringRef)arg0);
	OS_NATIVE_EXIT(env, that, JSStringRelease_FUNC);
}
#endif

#ifndef NO_KLGetCurrentKeyboardLayout
JNIEXPORT jint JNICALL OS_NATIVE(KLGetCurrentKeyboardLayout)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KLGetCurrentKeyboardLayout_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)KLGetCurrentKeyboardLayout((KeyboardLayoutRef *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, KLGetCurrentKeyboardLayout_FUNC);
	return rc;
}
#endif

#ifndef NO_KLGetKeyboardLayoutProperty
JNIEXPORT jint JNICALL OS_NATIVE(KLGetKeyboardLayoutProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KLGetKeyboardLayoutProperty_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)KLGetKeyboardLayoutProperty((KeyboardLayoutRef)arg0, (KeyboardLayoutPropertyTag)arg1, (const void **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, KLGetKeyboardLayoutProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_KeyTranslate
JNIEXPORT jint JNICALL OS_NATIVE(KeyTranslate)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, KeyTranslate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)KeyTranslate((const void *)arg0, arg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, KeyTranslate_FUNC);
	return rc;
}
#endif

#ifndef NO_KillPicture
JNIEXPORT void JNICALL OS_NATIVE(KillPicture)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, KillPicture_FUNC);
	KillPicture((PicHandle)arg0);
	OS_NATIVE_EXIT(env, that, KillPicture_FUNC);
}
#endif

#ifndef NO_LMGetKbdType
JNIEXPORT jbyte JNICALL OS_NATIVE(LMGetKbdType)
	(JNIEnv *env, jclass that)
{
	jbyte rc = 0;
	OS_NATIVE_ENTER(env, that, LMGetKbdType_FUNC);
	rc = (jbyte)LMGetKbdType();
	OS_NATIVE_EXIT(env, that, LMGetKbdType_FUNC);
	return rc;
}
#endif

#ifndef NO_LSCopyAllRoleHandlersForContentType
JNIEXPORT jint JNICALL OS_NATIVE(LSCopyAllRoleHandlersForContentType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSCopyAllRoleHandlersForContentType_FUNC);
/*
	rc = (jint)LSCopyAllRoleHandlersForContentType((CFStringRef)arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, LSCopyAllRoleHandlersForContentType)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(CFStringRef, jint))fp)((CFStringRef)arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, LSCopyAllRoleHandlersForContentType_FUNC);
	return rc;
}
#endif

#ifndef NO_LSCopyDisplayNameForRef
JNIEXPORT jint JNICALL OS_NATIVE(LSCopyDisplayNameForRef)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintArray arg1)
{
	jbyte *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSCopyDisplayNameForRef_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)LSCopyDisplayNameForRef((const FSRef *)lparg0, (CFStringRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LSCopyDisplayNameForRef_FUNC);
	return rc;
}
#endif

#ifndef NO_LSCopyItemAttribute
JNIEXPORT jint JNICALL OS_NATIVE(LSCopyItemAttribute)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSCopyItemAttribute_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)LSCopyItemAttribute((const FSRef *)lparg0, (LSRolesMask)arg1, (CFStringRef)arg2, (CFTypeRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, LSCopyItemAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_LSFindApplicationForInfo
JNIEXPORT jint JNICALL OS_NATIVE(LSFindApplicationForInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSFindApplicationForInfo_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)LSFindApplicationForInfo((OSType)arg0, (CFStringRef)arg1, (CFStringRef)arg2, (FSRef *)lparg3, (CFURLRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, LSFindApplicationForInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_LSGetApplicationForInfo
JNIEXPORT jint JNICALL OS_NATIVE(LSGetApplicationForInfo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jintArray arg5)
{
	jbyte *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSGetApplicationForInfo_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)LSGetApplicationForInfo((OSType)arg0, (OSType)arg1, (CFStringRef)arg2, (LSRolesMask)arg3, (FSRef *)lparg4, (CFURLRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, LSGetApplicationForInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_LSOpenApplication
JNIEXPORT jint JNICALL OS_NATIVE(LSOpenApplication)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	LSApplicationParameters _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSOpenApplication_FUNC);
	if (arg0) if ((lparg0 = getLSApplicationParametersFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)LSOpenApplication(lparg0, (ProcessSerialNumber *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setLSApplicationParametersFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, LSOpenApplication_FUNC);
	return rc;
}
#endif

#ifndef NO_LSOpenCFURLRef
JNIEXPORT jint JNICALL OS_NATIVE(LSOpenCFURLRef)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSOpenCFURLRef_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)LSOpenCFURLRef((CFURLRef)arg0, (CFURLRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, LSOpenCFURLRef_FUNC);
	return rc;
}
#endif

#ifndef NO_LSOpenURLsWithRole
JNIEXPORT jint JNICALL OS_NATIVE(LSOpenURLsWithRole)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4, jint arg5)
{
	LSApplicationParameters _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LSOpenURLsWithRole_FUNC);
	if (arg3) if ((lparg3 = getLSApplicationParametersFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)LSOpenURLsWithRole((CFArrayRef)arg0, arg1, (const AEKeyDesc *)arg2, (const LSApplicationParameters *)lparg3, (ProcessSerialNumber *)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setLSApplicationParametersFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, LSOpenURLsWithRole_FUNC);
	return rc;
}
#endif

#ifndef NO_LineTo
JNIEXPORT void JNICALL OS_NATIVE(LineTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, LineTo_FUNC);
	LineTo((short)arg0, (short)arg1);
	OS_NATIVE_EXIT(env, that, LineTo_FUNC);
}
#endif

#ifndef NO_LoWord
JNIEXPORT jshort JNICALL OS_NATIVE(LoWord)
	(JNIEnv *env, jclass that, jint arg0)
{
	jshort rc = 0;
	OS_NATIVE_ENTER(env, that, LoWord_FUNC);
	rc = (jshort)LoWord(arg0);
	OS_NATIVE_EXIT(env, that, LoWord_FUNC);
	return rc;
}
#endif

#ifndef NO_Long2Fix
JNIEXPORT jint JNICALL OS_NATIVE(Long2Fix)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Long2Fix_FUNC);
	rc = (jint)Long2Fix(arg0);
	OS_NATIVE_EXIT(env, that, Long2Fix_FUNC);
	return rc;
}
#endif

#ifndef NO_MenuSelect
JNIEXPORT jint JNICALL OS_NATIVE(MenuSelect)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MenuSelect_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)MenuSelect(*(Point *)lparg0);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, MenuSelect_FUNC);
	return rc;
}
#endif

#ifndef NO_MoveControl
JNIEXPORT void JNICALL OS_NATIVE(MoveControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, MoveControl_FUNC);
	MoveControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
	OS_NATIVE_EXIT(env, that, MoveControl_FUNC);
}
#endif

#ifndef NO_MoveTo
JNIEXPORT void JNICALL OS_NATIVE(MoveTo)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	OS_NATIVE_ENTER(env, that, MoveTo_FUNC);
	MoveTo((short)arg0, (short)arg1);
	OS_NATIVE_EXIT(env, that, MoveTo_FUNC);
}
#endif

#ifndef NO_MoveWindow
JNIEXPORT void JNICALL OS_NATIVE(MoveWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, MoveWindow_FUNC);
	MoveWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
	OS_NATIVE_EXIT(env, that, MoveWindow_FUNC);
}
#endif

#ifndef NO_NavCreateChooseFolderDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreateChooseFolderDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavCreateChooseFolderDialog_FUNC);
	if (arg0) if ((lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)NavCreateChooseFolderDialog((const NavDialogCreationOptions *)lparg0, (NavEventUPP)arg1, (NavObjectFilterUPP)arg2, (void *)arg3, (NavDialogRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg0 && lparg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NavCreateChooseFolderDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_NavCreateGetFileDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreateGetFileDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavCreateGetFileDialog_FUNC);
	if (arg0) if ((lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)NavCreateGetFileDialog((const NavDialogCreationOptions *)lparg0, (NavTypeListHandle)arg1, (NavEventUPP)arg2, (NavPreviewUPP)arg3, (NavObjectFilterUPP)arg4, (void *)arg5, (NavDialogRef *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg0 && lparg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NavCreateGetFileDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_NavCreatePutFileDialog
JNIEXPORT jint JNICALL OS_NATIVE(NavCreatePutFileDialog)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavCreatePutFileDialog_FUNC);
	if (arg0) if ((lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)NavCreatePutFileDialog((const NavDialogCreationOptions *)lparg0, (OSType)arg1, (OSType)arg2, (NavEventUPP)arg3, (void *)arg4, (NavDialogRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg0 && lparg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NavCreatePutFileDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_NavCustomControl__IILorg_eclipse_swt_internal_carbon_AEDesc_2
JNIEXPORT jint JNICALL OS_NATIVE(NavCustomControl__IILorg_eclipse_swt_internal_carbon_AEDesc_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	AEDesc _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavCustomControl__IILorg_eclipse_swt_internal_carbon_AEDesc_2_FUNC);
	if (arg2) if ((lparg2 = getAEDescFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)NavCustomControl((NavDialogRef)arg0, (NavCustomControlMessage)arg1, lparg2);
fail:
	if (arg2 && lparg2) setAEDescFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, NavCustomControl__IILorg_eclipse_swt_internal_carbon_AEDesc_2_FUNC);
	return rc;
}
#endif

#ifndef NO_NavCustomControl__IILorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2
JNIEXPORT jint JNICALL OS_NATIVE(NavCustomControl__IILorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NavMenuItemSpec _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavCustomControl__IILorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2_FUNC);
	if (arg2) if ((lparg2 = getNavMenuItemSpecFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)NavCustomControl((NavDialogRef)arg0, (NavCustomControlMessage)arg1, lparg2);
fail:
	if (arg2 && lparg2) setNavMenuItemSpecFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, NavCustomControl__IILorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogDispose
JNIEXPORT void JNICALL OS_NATIVE(NavDialogDispose)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, NavDialogDispose_FUNC);
	NavDialogDispose((NavDialogRef)arg0);
	OS_NATIVE_EXIT(env, that, NavDialogDispose_FUNC);
}
#endif

#ifndef NO_NavDialogGetReply
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetReply)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	NavReplyRecord _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogGetReply_FUNC);
	if (arg1) if ((lparg1 = getNavReplyRecordFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)NavDialogGetReply((NavDialogRef)arg0, (NavReplyRecord *)lparg1);
fail:
	if (arg1 && lparg1) setNavReplyRecordFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, NavDialogGetReply_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogGetSaveFileName
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetSaveFileName)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogGetSaveFileName_FUNC);
	rc = (jint)NavDialogGetSaveFileName((NavDialogRef)arg0);
	OS_NATIVE_EXIT(env, that, NavDialogGetSaveFileName_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogGetUserAction
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogGetUserAction)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogGetUserAction_FUNC);
	rc = (jint)NavDialogGetUserAction((NavDialogRef)arg0);
	OS_NATIVE_EXIT(env, that, NavDialogGetUserAction_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogRun
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogRun)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogRun_FUNC);
	rc = (jint)NavDialogRun((NavDialogRef)arg0);
	OS_NATIVE_EXIT(env, that, NavDialogRun_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogSetFilterTypeIdentifiers
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogSetFilterTypeIdentifiers)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogSetFilterTypeIdentifiers_FUNC);
/*
	rc = (jint)NavDialogSetFilterTypeIdentifiers(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, NavDialogSetFilterTypeIdentifiers)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, NavDialogSetFilterTypeIdentifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDialogSetSaveFileName
JNIEXPORT jint JNICALL OS_NATIVE(NavDialogSetSaveFileName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDialogSetSaveFileName_FUNC);
	rc = (jint)NavDialogSetSaveFileName((NavDialogRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, NavDialogSetSaveFileName_FUNC);
	return rc;
}
#endif

#ifndef NO_NavDisposeReply
JNIEXPORT jint JNICALL OS_NATIVE(NavDisposeReply)
	(JNIEnv *env, jclass that, jobject arg0)
{
	NavReplyRecord _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavDisposeReply_FUNC);
	if (arg0) if ((lparg0 = getNavReplyRecordFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)NavDisposeReply(lparg0);
fail:
	if (arg0 && lparg0) setNavReplyRecordFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NavDisposeReply_FUNC);
	return rc;
}
#endif

#ifndef NO_NavGetDefaultDialogCreationOptions
JNIEXPORT jint JNICALL OS_NATIVE(NavGetDefaultDialogCreationOptions)
	(JNIEnv *env, jclass that, jobject arg0)
{
	NavDialogCreationOptions _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NavGetDefaultDialogCreationOptions_FUNC);
	if (arg0) if ((lparg0 = getNavDialogCreationOptionsFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)NavGetDefaultDialogCreationOptions((NavDialogCreationOptions *)lparg0);
fail:
	if (arg0 && lparg0) setNavDialogCreationOptionsFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NavGetDefaultDialogCreationOptions_FUNC);
	return rc;
}
#endif

#ifndef NO_NewControl
JNIEXPORT jint JNICALL OS_NATIVE(NewControl)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jboolean arg3, jshort arg4, jshort arg5, jshort arg6, jshort arg7, jint arg8)
{
	Rect _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewControl_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)NewControl((WindowRef)arg0, (const Rect *)lparg1, (ConstStr255Param)lparg2, (Boolean)arg3, (SInt16)arg4, (SInt16)arg5, (SInt16)arg6, (SInt16)arg7, (SInt32)arg8);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, NewControl_FUNC);
	return rc;
}
#endif

#ifndef NO_NewDrag
JNIEXPORT jint JNICALL OS_NATIVE(NewDrag)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewDrag_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)NewDrag((DragRef *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, NewDrag_FUNC);
	return rc;
}
#endif

#ifndef NO_NewGWorldFromPtr
JNIEXPORT jint JNICALL OS_NATIVE(NewGWorldFromPtr)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint *lparg0=NULL;
	Rect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewGWorldFromPtr_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)NewGWorldFromPtr((GWorldPtr *)lparg0, (unsigned long)arg1, (const Rect *)lparg2, (CTabHandle)arg3, (GDHandle)arg4, (GWorldFlags)arg5, (Ptr)arg6, (long)arg7);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, NewGWorldFromPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_NewHandle
JNIEXPORT jint JNICALL OS_NATIVE(NewHandle)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewHandle_FUNC);
	rc = (jint)NewHandle((Size)arg0);
	OS_NATIVE_EXIT(env, that, NewHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_NewHandleClear
JNIEXPORT jint JNICALL OS_NATIVE(NewHandleClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewHandleClear_FUNC);
	rc = (jint)NewHandleClear((Size)arg0);
	OS_NATIVE_EXIT(env, that, NewHandleClear_FUNC);
	return rc;
}
#endif

#ifndef NO_NewPtr
JNIEXPORT jint JNICALL OS_NATIVE(NewPtr)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewPtr_FUNC);
	rc = (jint)NewPtr((Size)arg0);
	OS_NATIVE_EXIT(env, that, NewPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_NewPtrClear
JNIEXPORT jint JNICALL OS_NATIVE(NewPtrClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewPtrClear_FUNC);
	rc = (jint)NewPtrClear((Size)arg0);
	OS_NATIVE_EXIT(env, that, NewPtrClear_FUNC);
	return rc;
}
#endif

#ifndef NO_NewRgn
JNIEXPORT jint JNICALL OS_NATIVE(NewRgn)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewRgn_FUNC);
	rc = (jint)NewRgn();
	OS_NATIVE_EXIT(env, that, NewRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_NewTSMDocument
JNIEXPORT jint JNICALL OS_NATIVE(NewTSMDocument)
	(JNIEnv *env, jclass that, jshort arg0, jintArray arg1, jintArray arg2, jlong arg3)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewTSMDocument_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)NewTSMDocument(arg0, (OSType *)lparg1, (TSMDocumentID *)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, NewTSMDocument_FUNC);
	return rc;
}
#endif

#ifndef NO_OffsetRect
JNIEXPORT void JNICALL OS_NATIVE(OffsetRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, OffsetRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	OffsetRect(lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, OffsetRect_FUNC);
}
#endif

#ifndef NO_OffsetRgn
JNIEXPORT void JNICALL OS_NATIVE(OffsetRgn)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, OffsetRgn_FUNC);
	OffsetRgn((RgnHandle)arg0, (short)arg1, (short)arg2);
	OS_NATIVE_EXIT(env, that, OffsetRgn_FUNC);
}
#endif

#ifndef NO_OpenDataBrowserContainer
JNIEXPORT jint JNICALL OS_NATIVE(OpenDataBrowserContainer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OpenDataBrowserContainer_FUNC);
	rc = (jint)OpenDataBrowserContainer((ControlRef)arg0, (DataBrowserItemID)arg1);
	OS_NATIVE_EXIT(env, that, OpenDataBrowserContainer_FUNC);
	return rc;
}
#endif

#ifndef NO_OpenPicture
JNIEXPORT jint JNICALL OS_NATIVE(OpenPicture)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Rect _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, OpenPicture_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)OpenPicture((const Rect *)lparg0);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, OpenPicture_FUNC);
	return rc;
}
#endif

#ifndef NO_OpenRgn
JNIEXPORT void JNICALL OS_NATIVE(OpenRgn)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, OpenRgn_FUNC);
	OpenRgn();
	OS_NATIVE_EXIT(env, that, OpenRgn_FUNC);
}
#endif

#ifndef NO_PMCreatePageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMCreatePageFormat)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMCreatePageFormat_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PMCreatePageFormat((PMPageFormat *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PMCreatePageFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_PMCreatePrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMCreatePrintSettings)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMCreatePrintSettings_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PMCreatePrintSettings((PMPrintSettings *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PMCreatePrintSettings_FUNC);
	return rc;
}
#endif

#ifndef NO_PMCreateSession
JNIEXPORT jint JNICALL OS_NATIVE(PMCreateSession)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMCreateSession_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)PMCreateSession((PMPrintSession *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, PMCreateSession_FUNC);
	return rc;
}
#endif

#ifndef NO_PMFlattenPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMFlattenPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMFlattenPageFormat_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMFlattenPageFormat((PMPageFormat)arg0, (Handle *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMFlattenPageFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_PMFlattenPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMFlattenPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMFlattenPrintSettings_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMFlattenPrintSettings((PMPrintSettings)arg0, (Handle *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMFlattenPrintSettings_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetAdjustedPageRect
JNIEXPORT jint JNICALL OS_NATIVE(PMGetAdjustedPageRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetAdjustedPageRect_FUNC);
	if (arg1) if ((lparg1 = getPMRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PMGetAdjustedPageRect((PMPageFormat)arg0, (PMRect *)lparg1);
fail:
	if (arg1 && lparg1) setPMRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PMGetAdjustedPageRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetAdjustedPaperRect
JNIEXPORT jint JNICALL OS_NATIVE(PMGetAdjustedPaperRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetAdjustedPaperRect_FUNC);
	if (arg1) if ((lparg1 = getPMRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PMGetAdjustedPaperRect((PMPageFormat)arg0, (PMRect *)lparg1);
fail:
	if (arg1 && lparg1) setPMRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PMGetAdjustedPaperRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetCollate
JNIEXPORT jint JNICALL OS_NATIVE(PMGetCollate)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetCollate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetCollate((PMPrintSettings)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetCollate_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetCopies
JNIEXPORT jint JNICALL OS_NATIVE(PMGetCopies)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetCopies_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetCopies((PMPrintSettings)arg0, (UInt32 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetCopies_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetFirstPage
JNIEXPORT jint JNICALL OS_NATIVE(PMGetFirstPage)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetFirstPage_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetFirstPage((PMPrintSettings)arg0, (UInt32 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetFirstPage_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetJobNameCFString
JNIEXPORT jint JNICALL OS_NATIVE(PMGetJobNameCFString)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetJobNameCFString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetJobNameCFString((PMPrintSettings)arg0, (CFStringRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetJobNameCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetLastPage
JNIEXPORT jint JNICALL OS_NATIVE(PMGetLastPage)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetLastPage_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetLastPage((PMPrintSettings)arg0, (UInt32 *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetLastPage_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetOrientation
JNIEXPORT jint JNICALL OS_NATIVE(PMGetOrientation)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetOrientation_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMGetOrientation((PMPageFormat)arg0, (PMOrientation *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetOrientation_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetPageRange
JNIEXPORT jint JNICALL OS_NATIVE(PMGetPageRange)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetPageRange_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMGetPageRange((PMPrintSettings)arg0, (UInt32 *)lparg1, (UInt32 *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMGetPageRange_FUNC);
	return rc;
}
#endif

#ifndef NO_PMGetResolution
JNIEXPORT jint JNICALL OS_NATIVE(PMGetResolution)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	PMResolution _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMGetResolution_FUNC);
	if (arg1) if ((lparg1 = getPMResolutionFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)PMGetResolution((PMPageFormat)arg0, (PMResolution *)lparg1);
fail:
	if (arg1 && lparg1) setPMResolutionFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, PMGetResolution_FUNC);
	return rc;
}
#endif

#ifndef NO_PMPrinterGetOutputResolution
JNIEXPORT jint JNICALL OS_NATIVE(PMPrinterGetOutputResolution)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PMResolution _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMPrinterGetOutputResolution_FUNC);
	if (arg2) if ((lparg2 = getPMResolutionFields(env, arg2, &_arg2)) == NULL) goto fail;
/*
	rc = (jint)PMPrinterGetOutputResolution((PMPrinter)arg0, (PMPrintSettings)arg1, (PMResolution *)lparg2);
*/
	{
		LOAD_FUNCTION(fp, PMPrinterGetOutputResolution)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(PMPrinter, PMPrintSettings, PMResolution *))fp)((PMPrinter)arg0, (PMPrintSettings)arg1, (PMResolution *)lparg2);
		}
	}
fail:
	if (arg2 && lparg2) setPMResolutionFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PMPrinterGetOutputResolution_FUNC);
	return rc;
}
#endif

#ifndef NO_PMRelease
JNIEXPORT jint JNICALL OS_NATIVE(PMRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMRelease_FUNC);
	rc = (jint)PMRelease((PMObject)arg0);
	OS_NATIVE_EXIT(env, that, PMRelease_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionBeginDocumentNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionBeginDocumentNoDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionBeginDocumentNoDialog_FUNC);
	rc = (jint)PMSessionBeginDocumentNoDialog((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMPageFormat)arg2);
	OS_NATIVE_EXIT(env, that, PMSessionBeginDocumentNoDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionBeginPageNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionBeginPageNoDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	PMRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionBeginPageNoDialog_FUNC);
	if (arg2) if ((lparg2 = getPMRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)PMSessionBeginPageNoDialog((PMPrintSession)arg0, (PMPageFormat)arg1, (const PMRect *)lparg2);
fail:
	if (arg2 && lparg2) setPMRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, PMSessionBeginPageNoDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionCopyDestinationLocation
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionCopyDestinationLocation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionCopyDestinationLocation_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionCopyDestinationLocation((PMPrintSession)arg0, (PMPrintSettings)arg1, (CFURLRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionCopyDestinationLocation_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionCreatePrinterList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionCreatePrinterList((PMPrintSession)arg0, (CFArrayRef *)lparg1, (CFIndex *)lparg2, (PMPrinter *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMSessionCreatePrinterList_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionDefaultPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionDefaultPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionDefaultPageFormat_FUNC);
	rc = (jint)PMSessionDefaultPageFormat((PMPrintSession)arg0, (PMPageFormat)arg1);
	OS_NATIVE_EXIT(env, that, PMSessionDefaultPageFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionDefaultPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionDefaultPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionDefaultPrintSettings_FUNC);
	rc = (jint)PMSessionDefaultPrintSettings((PMPrintSession)arg0, (PMPrintSettings)arg1);
	OS_NATIVE_EXIT(env, that, PMSessionDefaultPrintSettings_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionEndDocumentNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionEndDocumentNoDialog)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionEndDocumentNoDialog_FUNC);
	rc = (jint)PMSessionEndDocumentNoDialog((PMPrintSession)arg0);
	OS_NATIVE_EXIT(env, that, PMSessionEndDocumentNoDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionEndPageNoDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionEndPageNoDialog)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionEndPageNoDialog_FUNC);
	rc = (jint)PMSessionEndPageNoDialog((PMPrintSession)arg0);
	OS_NATIVE_EXIT(env, that, PMSessionEndPageNoDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionError
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionError)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionError_FUNC);
	rc = (jint)PMSessionError((PMPrintSession)arg0);
	OS_NATIVE_EXIT(env, that, PMSessionError_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionGetCurrentPrinter
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionGetCurrentPrinter)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionGetCurrentPrinter_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionGetCurrentPrinter((PMPrintSession)arg0, (PMPrinter *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMSessionGetCurrentPrinter_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionGetDestinationType
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionGetDestinationType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionGetDestinationType_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionGetDestinationType((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMDestinationType *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionGetDestinationType_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionGetGraphicsContext
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionGetGraphicsContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionGetGraphicsContext_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionGetGraphicsContext((PMPrintSession)arg0, (CFStringRef)arg1, (void **)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionGetGraphicsContext_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionPageSetupDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionPageSetupDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionPageSetupDialog_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionPageSetupDialog((PMPrintSession)arg0, (PMPageFormat)arg1, (Boolean *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionPageSetupDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionPrintDialog
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionPrintDialog)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbooleanArray arg3)
{
	jboolean *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionPrintDialog_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetBooleanArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionPrintDialog((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMPageFormat)arg2, (Boolean *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseBooleanArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, PMSessionPrintDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionSetCurrentPrinter
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetCurrentPrinter)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionSetCurrentPrinter_FUNC);
	rc = (jint)PMSessionSetCurrentPrinter((PMPrintSession)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, PMSessionSetCurrentPrinter_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionSetDestination
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetDestination)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionSetDestination_FUNC);
	rc = (jint)PMSessionSetDestination((PMPrintSession)arg0, (PMPrintSettings)arg1, (PMDestinationType)arg2, (CFStringRef)arg3, (CFURLRef)arg4);
	OS_NATIVE_EXIT(env, that, PMSessionSetDestination_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionSetDocumentFormatGeneration
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetDocumentFormatGeneration)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionSetDocumentFormatGeneration_FUNC);
	rc = (jint)PMSessionSetDocumentFormatGeneration((PMPrintSession)arg0, (CFStringRef)arg1, (CFArrayRef)arg2, (CFTypeRef)arg3);
	OS_NATIVE_EXIT(env, that, PMSessionSetDocumentFormatGeneration_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionSetError
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionSetError)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionSetError_FUNC);
	rc = (jint)PMSessionSetError((PMPrintSession)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PMSessionSetError_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionUseSheets
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionUseSheets)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionUseSheets_FUNC);
	rc = (jint)PMSessionUseSheets((PMPrintSession)arg0, (WindowRef)arg1, (PMSheetDoneUPP)arg2);
	OS_NATIVE_EXIT(env, that, PMSessionUseSheets_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionValidatePageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionValidatePageFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionValidatePageFormat_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionValidatePageFormat((PMPrintSession)arg0, (PMPageFormat)arg1, (Boolean *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionValidatePageFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSessionValidatePrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMSessionValidatePrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSessionValidatePrintSettings_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)PMSessionValidatePrintSettings((PMPrintSession)arg0, (PMPrintSettings)arg1, (Boolean *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, PMSessionValidatePrintSettings_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetCollate
JNIEXPORT jint JNICALL OS_NATIVE(PMSetCollate)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetCollate_FUNC);
	rc = (jint)PMSetCollate((PMPrintSettings)arg0, arg1);
	OS_NATIVE_EXIT(env, that, PMSetCollate_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetCopies
JNIEXPORT jint JNICALL OS_NATIVE(PMSetCopies)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetCopies_FUNC);
	rc = (jint)PMSetCopies((PMPrintSettings)arg0, (UInt32)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, PMSetCopies_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetFirstPage
JNIEXPORT jint JNICALL OS_NATIVE(PMSetFirstPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetFirstPage_FUNC);
	rc = (jint)PMSetFirstPage((PMPrintSettings)arg0, (UInt32)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, PMSetFirstPage_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetJobNameCFString
JNIEXPORT jint JNICALL OS_NATIVE(PMSetJobNameCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetJobNameCFString_FUNC);
	rc = (jint)PMSetJobNameCFString((PMPrintSettings)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, PMSetJobNameCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetLastPage
JNIEXPORT jint JNICALL OS_NATIVE(PMSetLastPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetLastPage_FUNC);
	rc = (jint)PMSetLastPage((PMPrintSettings)arg0, (UInt32)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, PMSetLastPage_FUNC);
	return rc;
}
#endif

#ifndef NO_PMSetOrientation
JNIEXPORT void JNICALL OS_NATIVE(PMSetOrientation)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, PMSetOrientation_FUNC);
	PMSetOrientation((PMPageFormat)arg0, (PMOrientation)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, PMSetOrientation_FUNC);
}
#endif

#ifndef NO_PMSetPageRange
JNIEXPORT jint JNICALL OS_NATIVE(PMSetPageRange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMSetPageRange_FUNC);
	rc = (jint)PMSetPageRange((PMPrintSettings)arg0, (UInt32)arg1, (UInt32)arg2);
	OS_NATIVE_EXIT(env, that, PMSetPageRange_FUNC);
	return rc;
}
#endif

#ifndef NO_PMShowPrintDialogWithOptions
JNIEXPORT jint JNICALL OS_NATIVE(PMShowPrintDialogWithOptions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbooleanArray arg4)
{
	jboolean *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMShowPrintDialogWithOptions_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetBooleanArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	rc = (jint)PMShowPrintDialogWithOptions(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		LOAD_FUNCTION(fp, PMShowPrintDialogWithOptions)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint, jint, jboolean *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseBooleanArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, PMShowPrintDialogWithOptions_FUNC);
	return rc;
}
#endif

#ifndef NO_PMUnflattenPageFormat
JNIEXPORT jint JNICALL OS_NATIVE(PMUnflattenPageFormat)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMUnflattenPageFormat_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMUnflattenPageFormat((Handle)arg0, (PMPageFormat *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMUnflattenPageFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_PMUnflattenPrintSettings
JNIEXPORT jint JNICALL OS_NATIVE(PMUnflattenPrintSettings)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PMUnflattenPrintSettings_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)PMUnflattenPrintSettings((Handle)arg0, (PMPrintSettings *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, PMUnflattenPrintSettings_FUNC);
	return rc;
}
#endif

#ifndef NO_PickColor
JNIEXPORT jint JNICALL OS_NATIVE(PickColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	ColorPickerInfo _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PickColor_FUNC);
	if (arg0) if ((lparg0 = getColorPickerInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jint)PickColor((ColorPickerInfo *)lparg0);
fail:
	if (arg0 && lparg0) setColorPickerInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PickColor_FUNC);
	return rc;
}
#endif

#ifndef NO_PopUpMenuSelect
JNIEXPORT jint JNICALL OS_NATIVE(PopUpMenuSelect)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PopUpMenuSelect_FUNC);
	rc = (jint)PopUpMenuSelect((MenuRef)arg0, (short)arg1, (short)arg2, (short)arg3);
	OS_NATIVE_EXIT(env, that, PopUpMenuSelect_FUNC);
	return rc;
}
#endif

#ifndef NO_PostEvent
JNIEXPORT jint JNICALL OS_NATIVE(PostEvent)
	(JNIEnv *env, jclass that, jshort arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PostEvent_FUNC);
	rc = (jint)PostEvent((EventKind)arg0, (UInt32)arg1);
	OS_NATIVE_EXIT(env, that, PostEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_PostEventToQueue
JNIEXPORT jint JNICALL OS_NATIVE(PostEventToQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PostEventToQueue_FUNC);
	rc = (jint)PostEventToQueue((EventQueueRef)arg0, (EventRef)arg1, (EventPriority)arg2);
	OS_NATIVE_EXIT(env, that, PostEventToQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInRect
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	Point _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRect_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)PtInRect(*(Point *)lparg0, (const Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PtInRect_FUNC);
	return rc;
}
#endif

#ifndef NO_PtInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(PtInRgn)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Point _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, PtInRgn_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)PtInRgn(*(Point *)lparg0, (RgnHandle)arg1);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, PtInRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_PutScrapFlavor__IIII_3B
JNIEXPORT jint JNICALL OS_NATIVE(PutScrapFlavor__IIII_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PutScrapFlavor__IIII_3B_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)PutScrapFlavor((ScrapRef)arg0, (ScrapFlavorType)arg1, (ScrapFlavorFlags)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, PutScrapFlavor__IIII_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_PutScrapFlavor__IIII_3C
JNIEXPORT jint JNICALL OS_NATIVE(PutScrapFlavor__IIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, PutScrapFlavor__IIII_3C_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)PutScrapFlavor((ScrapRef)arg0, (ScrapFlavorType)arg1, (ScrapFlavorFlags)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, PutScrapFlavor__IIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_QDBeginCGContext
JNIEXPORT jint JNICALL OS_NATIVE(QDBeginCGContext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDBeginCGContext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)QDBeginCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, QDBeginCGContext_FUNC);
	return rc;
}
#endif

#ifndef NO_QDEndCGContext
JNIEXPORT jint JNICALL OS_NATIVE(QDEndCGContext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDEndCGContext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)QDEndCGContext((CGrafPtr)arg0, (CGContextRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, QDEndCGContext_FUNC);
	return rc;
}
#endif

#ifndef NO_QDFlushPortBuffer
JNIEXPORT void JNICALL OS_NATIVE(QDFlushPortBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, QDFlushPortBuffer_FUNC);
	QDFlushPortBuffer((CGrafPtr)arg0, (RgnHandle)arg1);
	OS_NATIVE_EXIT(env, that, QDFlushPortBuffer_FUNC);
}
#endif

#ifndef NO_QDPictCreateWithProvider
JNIEXPORT jint JNICALL OS_NATIVE(QDPictCreateWithProvider)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDPictCreateWithProvider_FUNC);
	rc = (jint)QDPictCreateWithProvider((CGDataProviderRef)arg0);
	OS_NATIVE_EXIT(env, that, QDPictCreateWithProvider_FUNC);
	return rc;
}
#endif

#ifndef NO_QDPictDrawToCGContext
JNIEXPORT jint JNICALL OS_NATIVE(QDPictDrawToCGContext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	CGRect _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDPictDrawToCGContext_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)QDPictDrawToCGContext((CGContextRef)arg0, *(CGRect *)lparg1, (QDPictRef)arg2);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, QDPictDrawToCGContext_FUNC);
	return rc;
}
#endif

#ifndef NO_QDPictRelease
JNIEXPORT void JNICALL OS_NATIVE(QDPictRelease)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, QDPictRelease_FUNC);
	QDPictRelease((QDPictRef)arg0);
	OS_NATIVE_EXIT(env, that, QDPictRelease_FUNC);
}
#endif

#ifndef NO_QDRegionToRects
JNIEXPORT jint JNICALL OS_NATIVE(QDRegionToRects)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, QDRegionToRects_FUNC);
	rc = (jint)QDRegionToRects((RgnHandle)arg0, (QDRegionParseDirection)arg1, (RegionToRectsUPP)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, QDRegionToRects_FUNC);
	return rc;
}
#endif

#ifndef NO_RGBBackColor
JNIEXPORT void JNICALL OS_NATIVE(RGBBackColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, RGBBackColor_FUNC);
	if (arg0) if ((lparg0 = getRGBColorFields(env, arg0, &_arg0)) == NULL) goto fail;
	RGBBackColor((const RGBColor *)lparg0);
fail:
	if (arg0 && lparg0) setRGBColorFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, RGBBackColor_FUNC);
}
#endif

#ifndef NO_RGBForeColor
JNIEXPORT void JNICALL OS_NATIVE(RGBForeColor)
	(JNIEnv *env, jclass that, jobject arg0)
{
	RGBColor _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, RGBForeColor_FUNC);
	if (arg0) if ((lparg0 = getRGBColorFields(env, arg0, &_arg0)) == NULL) goto fail;
	RGBForeColor((const RGBColor *)lparg0);
fail:
	if (arg0 && lparg0) setRGBColorFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, RGBForeColor_FUNC);
}
#endif

#ifndef NO_ReadIconFile
JNIEXPORT jint JNICALL OS_NATIVE(ReadIconFile)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintArray arg1)
{
	jbyte *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReadIconFile_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ReadIconFile((const FSSpec *)lparg0, (IconFamilyHandle *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, ReadIconFile_FUNC);
	return rc;
}
#endif

#ifndef NO_ReceiveNextEvent
JNIEXPORT jint JNICALL OS_NATIVE(ReceiveNextEvent)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jdouble arg2, jboolean arg3, jintArray arg4)
{
	jint *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReceiveNextEvent_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)ReceiveNextEvent((UInt32)arg0, (const EventTypeSpec *)lparg1, (EventTimeout)arg2, (Boolean)arg3, (EventRef *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, ReceiveNextEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_RectInRgn
JNIEXPORT jboolean JNICALL OS_NATIVE(RectInRgn)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1)
{
	Rect _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, RectInRgn_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)RectInRgn((const Rect *)lparg0, (RgnHandle)arg1);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, RectInRgn_FUNC);
	return rc;
}
#endif

#ifndef NO_RectRgn
JNIEXPORT void JNICALL OS_NATIVE(RectRgn)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, RectRgn_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	RectRgn((RgnHandle)arg0, (const Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, RectRgn_FUNC);
}
#endif

#ifndef NO_RegisterAppearanceClient
JNIEXPORT jint JNICALL OS_NATIVE(RegisterAppearanceClient)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RegisterAppearanceClient_FUNC);
	rc = (jint)RegisterAppearanceClient();
	OS_NATIVE_EXIT(env, that, RegisterAppearanceClient_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseEvent
JNIEXPORT void JNICALL OS_NATIVE(ReleaseEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ReleaseEvent_FUNC);
	ReleaseEvent((EventRef)arg0);
	OS_NATIVE_EXIT(env, that, ReleaseEvent_FUNC);
}
#endif

#ifndef NO_ReleaseIconRef
JNIEXPORT void JNICALL OS_NATIVE(ReleaseIconRef)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ReleaseIconRef_FUNC);
	ReleaseIconRef((IconRef)arg0);
	OS_NATIVE_EXIT(env, that, ReleaseIconRef_FUNC);
}
#endif

#ifndef NO_ReleaseMenu
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReleaseMenu_FUNC);
	rc = (jint)ReleaseMenu((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, ReleaseMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseWindow
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReleaseWindow_FUNC);
	rc = (jint)ReleaseWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, ReleaseWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(ReleaseWindowGroup)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReleaseWindowGroup_FUNC);
	rc = (jint)ReleaseWindowGroup((WindowGroupRef)arg0);
	OS_NATIVE_EXIT(env, that, ReleaseWindowGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(RemoveControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveControlProperty_FUNC);
	rc = (jint)RemoveControlProperty((ControlRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, RemoveControlProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(RemoveDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveDataBrowserItems_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)RemoveDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, RemoveDataBrowserItems_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveDataBrowserTableViewColumn
JNIEXPORT jint JNICALL OS_NATIVE(RemoveDataBrowserTableViewColumn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveDataBrowserTableViewColumn_FUNC);
	rc = (jint)RemoveDataBrowserTableViewColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1);
	OS_NATIVE_EXIT(env, that, RemoveDataBrowserTableViewColumn_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveEventFromQueue
JNIEXPORT jint JNICALL OS_NATIVE(RemoveEventFromQueue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveEventFromQueue_FUNC);
	rc = (jint)RemoveEventFromQueue((EventQueueRef)arg0, (EventRef)arg1);
	OS_NATIVE_EXIT(env, that, RemoveEventFromQueue_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveEventHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveEventHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveEventHandler_FUNC);
	rc = (jint)RemoveEventHandler((EventHandlerRef)arg0);
	OS_NATIVE_EXIT(env, that, RemoveEventHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveEventLoopTimer
JNIEXPORT jint JNICALL OS_NATIVE(RemoveEventLoopTimer)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveEventLoopTimer_FUNC);
	rc = (jint)RemoveEventLoopTimer((EventLoopTimerRef)arg0);
	OS_NATIVE_EXIT(env, that, RemoveEventLoopTimer_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveReceiveHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveReceiveHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveReceiveHandler_FUNC);
	rc = (jint)RemoveReceiveHandler((DragReceiveHandlerUPP)arg0, (WindowRef)arg1);
	OS_NATIVE_EXIT(env, that, RemoveReceiveHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_RemoveTrackingHandler
JNIEXPORT jint JNICALL OS_NATIVE(RemoveTrackingHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RemoveTrackingHandler_FUNC);
	rc = (jint)RemoveTrackingHandler((DragTrackingHandlerUPP)arg0, (WindowRef)arg1);
	OS_NATIVE_EXIT(env, that, RemoveTrackingHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_RepositionWindow
JNIEXPORT jint JNICALL OS_NATIVE(RepositionWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RepositionWindow_FUNC);
	rc = (jint)RepositionWindow((WindowRef)arg0, (WindowRef)arg1, arg2);
	OS_NATIVE_EXIT(env, that, RepositionWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_ReshapeCustomWindow
JNIEXPORT jint JNICALL OS_NATIVE(ReshapeCustomWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ReshapeCustomWindow_FUNC);
	rc = (jint)ReshapeCustomWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, ReshapeCustomWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_RestoreApplicationDockTileImage
JNIEXPORT jint JNICALL OS_NATIVE(RestoreApplicationDockTileImage)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RestoreApplicationDockTileImage_FUNC);
	rc = (jint)RestoreApplicationDockTileImage();
	OS_NATIVE_EXIT(env, that, RestoreApplicationDockTileImage_FUNC);
	return rc;
}
#endif

#ifndef NO_RetainEvent
JNIEXPORT jint JNICALL OS_NATIVE(RetainEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RetainEvent_FUNC);
	rc = (jint)RetainEvent((EventRef)arg0);
	OS_NATIVE_EXIT(env, that, RetainEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_RetainMenu
JNIEXPORT jint JNICALL OS_NATIVE(RetainMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RetainMenu_FUNC);
	rc = (jint)RetainMenu((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, RetainMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_RetainWindow
JNIEXPORT jint JNICALL OS_NATIVE(RetainWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RetainWindow_FUNC);
	rc = (jint)RetainWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, RetainWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_RevealDataBrowserItem
JNIEXPORT jint JNICALL OS_NATIVE(RevealDataBrowserItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyte arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RevealDataBrowserItem_FUNC);
	rc = (jint)RevealDataBrowserItem((ControlRef)arg0, (DataBrowserItemID)arg1, (DataBrowserPropertyID)arg2, (DataBrowserRevealOptions)arg3);
	OS_NATIVE_EXIT(env, that, RevealDataBrowserItem_FUNC);
	return rc;
}
#endif

#ifndef NO_RunStandardAlert
JNIEXPORT jint JNICALL OS_NATIVE(RunStandardAlert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, RunStandardAlert_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)RunStandardAlert((DialogRef)arg0, (ModalFilterUPP)arg1, (DialogItemIndex *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, RunStandardAlert_FUNC);
	return rc;
}
#endif

#ifndef NO_SameProcess
JNIEXPORT jint JNICALL OS_NATIVE(SameProcess)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1, jbooleanArray arg2)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	jboolean *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SameProcess_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)SameProcess((ProcessSerialNumber *)lparg0, (ProcessSerialNumber *)lparg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SameProcess_FUNC);
	return rc;
}
#endif

#ifndef NO_ScrollRect
JNIEXPORT void JNICALL OS_NATIVE(ScrollRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jint arg3)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, ScrollRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	ScrollRect((const Rect *)lparg0, (short)arg1, (short)arg2, (RgnHandle)arg3);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, ScrollRect_FUNC);
}
#endif

#ifndef NO_SecPolicySearchCopyNext
JNIEXPORT jint JNICALL OS_NATIVE(SecPolicySearchCopyNext)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SecPolicySearchCopyNext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)SecPolicySearchCopyNext((SecPolicySearchRef)arg0, (SecPolicyRef *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, SecPolicySearchCopyNext_FUNC);
	return rc;
}
#endif

#ifndef NO_SecPolicySearchCreate
JNIEXPORT jint JNICALL OS_NATIVE(SecPolicySearchCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SecPolicySearchCreate_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SecPolicySearchCreate((CSSM_CERT_TYPE)arg0, (CSSM_OID *)arg1, (CSSM_DATA *)arg2, (SecPolicySearchRef *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SecPolicySearchCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_SecTrustCreateWithCertificates
JNIEXPORT jint JNICALL OS_NATIVE(SecTrustCreateWithCertificates)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SecTrustCreateWithCertificates_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)SecTrustCreateWithCertificates((CFArrayRef)arg0, (CFTypeRef)arg1, (SecTrustRef *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SecTrustCreateWithCertificates_FUNC);
	return rc;
}
#endif

#ifndef NO_SectRect
JNIEXPORT jboolean JNICALL OS_NATIVE(SectRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	Rect _arg0, *lparg0=NULL;
	Rect _arg1, *lparg1=NULL;
	Rect _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, SectRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	rc = (jboolean)SectRect(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SectRect_FUNC);
	return rc;
}
#endif

#ifndef NO_SectRgn
JNIEXPORT void JNICALL OS_NATIVE(SectRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, SectRgn_FUNC);
	SectRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	OS_NATIVE_EXIT(env, that, SectRgn_FUNC);
}
#endif

#ifndef NO_SelectWindow
JNIEXPORT void JNICALL OS_NATIVE(SelectWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, SelectWindow_FUNC);
	SelectWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, SelectWindow_FUNC);
}
#endif

#ifndef NO_SendBehind
JNIEXPORT void JNICALL OS_NATIVE(SendBehind)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SendBehind_FUNC);
	SendBehind((WindowRef)arg0, (WindowRef)arg1);
	OS_NATIVE_EXIT(env, that, SendBehind_FUNC);
}
#endif

#ifndef NO_SendEventToEventTarget
JNIEXPORT jint JNICALL OS_NATIVE(SendEventToEventTarget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendEventToEventTarget_FUNC);
	rc = (jint)SendEventToEventTarget((EventRef)arg0, (EventTargetRef)arg1);
	OS_NATIVE_EXIT(env, that, SendEventToEventTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_SendEventToEventTargetWithOptions
JNIEXPORT jint JNICALL OS_NATIVE(SendEventToEventTargetWithOptions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SendEventToEventTargetWithOptions_FUNC);
	rc = (jint)SendEventToEventTargetWithOptions((EventRef)arg0, (EventTargetRef)arg1, arg2);
	OS_NATIVE_EXIT(env, that, SendEventToEventTargetWithOptions_FUNC);
	return rc;
}
#endif

#ifndef NO_SetApplicationDockTileImage
JNIEXPORT jint JNICALL OS_NATIVE(SetApplicationDockTileImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetApplicationDockTileImage_FUNC);
	rc = (jint)SetApplicationDockTileImage((CGImageRef)arg0);
	OS_NATIVE_EXIT(env, that, SetApplicationDockTileImage_FUNC);
	return rc;
}
#endif

#ifndef NO_SetAutomaticControlDragTrackingEnabledForWindow
JNIEXPORT jint JNICALL OS_NATIVE(SetAutomaticControlDragTrackingEnabledForWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetAutomaticControlDragTrackingEnabledForWindow_FUNC);
	rc = (jint)SetAutomaticControlDragTrackingEnabledForWindow((WindowRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, SetAutomaticControlDragTrackingEnabledForWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetBevelButtonContentInfo
JNIEXPORT jint JNICALL OS_NATIVE(SetBevelButtonContentInfo)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlButtonContentInfo _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetBevelButtonContentInfo_FUNC);
	if (arg1) if ((lparg1 = getControlButtonContentInfoFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)SetBevelButtonContentInfo((ControlRef)arg0, (ControlButtonContentInfoPtr)lparg1);
fail:
	if (arg1 && lparg1) setControlButtonContentInfoFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetBevelButtonContentInfo_FUNC);
	return rc;
}
#endif

#ifndef NO_SetClip
JNIEXPORT void JNICALL OS_NATIVE(SetClip)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, SetClip_FUNC);
	SetClip((RgnHandle)arg0);
	OS_NATIVE_EXIT(env, that, SetClip_FUNC);
}
#endif

#ifndef NO_SetControl32BitMaximum
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitMaximum)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControl32BitMaximum_FUNC);
	SetControl32BitMaximum((ControlRef)arg0, (SInt32)arg1);
	OS_NATIVE_EXIT(env, that, SetControl32BitMaximum_FUNC);
}
#endif

#ifndef NO_SetControl32BitMinimum
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitMinimum)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControl32BitMinimum_FUNC);
	SetControl32BitMinimum((ControlRef)arg0, (SInt32)arg1);
	OS_NATIVE_EXIT(env, that, SetControl32BitMinimum_FUNC);
}
#endif

#ifndef NO_SetControl32BitValue
JNIEXPORT void JNICALL OS_NATIVE(SetControl32BitValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControl32BitValue_FUNC);
	SetControl32BitValue((ControlRef)arg0, (SInt32)arg1);
	OS_NATIVE_EXIT(env, that, SetControl32BitValue_FUNC);
}
#endif

#ifndef NO_SetControlAction
JNIEXPORT void JNICALL OS_NATIVE(SetControlAction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControlAction_FUNC);
	SetControlAction((ControlRef)arg0, (ControlActionUPP)arg1);
	OS_NATIVE_EXIT(env, that, SetControlAction_FUNC);
}
#endif

#ifndef NO_SetControlBounds
JNIEXPORT void JNICALL OS_NATIVE(SetControlBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, SetControlBounds_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	SetControlBounds((ControlRef)arg0, (const Rect *)lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetControlBounds_FUNC);
}
#endif

#ifndef NO_SetControlColorProc
JNIEXPORT jint JNICALL OS_NATIVE(SetControlColorProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlColorProc_FUNC);
	rc = (jint)SetControlColorProc((ControlRef)arg0, (ControlColorUPP)arg1);
	OS_NATIVE_EXIT(env, that, SetControlColorProc_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIII
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIII_FUNC);
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)arg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlButtonContentInfo _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2_FUNC);
	if (arg4) if ((lparg4 = getControlButtonContentInfoFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setControlButtonContentInfoFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlButtonContentInfo_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlEditTextSelectionRec _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_FUNC);
	if (arg4) if ((lparg4 = getControlEditTextSelectionRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setControlEditTextSelectionRecFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlEditTextSelectionRec_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	ControlTabInfoRecV1 _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2_FUNC);
	if (arg4) if ((lparg4 = getControlTabInfoRecV1Fields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setControlTabInfoRecV1Fields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_ControlTabInfoRecV1_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_LongDateRec_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_LongDateRec_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	LongDateRec _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_LongDateRec_2_FUNC);
	if (arg4) if ((lparg4 = getLongDateRecFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setLongDateRecFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_LongDateRec_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Rect _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2_FUNC);
	if (arg4) if ((lparg4 = getRectFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setRectFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetControlData__IIIILorg_eclipse_swt_internal_carbon_Rect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3B
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIII_3B_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetControlData__IIII_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3I
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIII_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetControlData__IIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlData__IIII_3S
JNIEXPORT jint JNICALL OS_NATIVE(SetControlData__IIII_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlData__IIII_3S_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetControlData((ControlRef)arg0, (ControlPartCode)arg1, (ResType)arg2, (Size)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetControlData__IIII_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlFontStyle
JNIEXPORT jint JNICALL OS_NATIVE(SetControlFontStyle)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	ControlFontStyleRec _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlFontStyle_FUNC);
	if (arg1) if ((lparg1 = getControlFontStyleRecFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)SetControlFontStyle((ControlRef)arg0, (const ControlFontStyleRec *)lparg1);
fail:
	if (arg1 && lparg1) setControlFontStyleRecFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetControlFontStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlPopupMenuHandle
JNIEXPORT void JNICALL OS_NATIVE(SetControlPopupMenuHandle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControlPopupMenuHandle_FUNC);
	SetControlPopupMenuHandle((ControlRef)arg0, (MenuRef)arg1);
	OS_NATIVE_EXIT(env, that, SetControlPopupMenuHandle_FUNC);
}
#endif

#ifndef NO_SetControlProperty
JNIEXPORT jint JNICALL OS_NATIVE(SetControlProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlProperty_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetControlProperty((ControlRef)arg0, arg1, arg2, arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetControlProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlReference
JNIEXPORT void JNICALL OS_NATIVE(SetControlReference)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControlReference_FUNC);
	SetControlReference((ControlRef)arg0, (SInt32)arg1);
	OS_NATIVE_EXIT(env, that, SetControlReference_FUNC);
}
#endif

#ifndef NO_SetControlTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetControlTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlTitleWithCFString_FUNC);
	rc = (jint)SetControlTitleWithCFString((ControlRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, SetControlTitleWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_SetControlViewSize
JNIEXPORT void JNICALL OS_NATIVE(SetControlViewSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetControlViewSize_FUNC);
	SetControlViewSize((ControlRef)arg0, (SInt32)arg1);
	OS_NATIVE_EXIT(env, that, SetControlViewSize_FUNC);
}
#endif

#ifndef NO_SetControlVisibility
JNIEXPORT jint JNICALL OS_NATIVE(SetControlVisibility)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetControlVisibility_FUNC);
	rc = (jint)SetControlVisibility((ControlRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetControlVisibility_FUNC);
	return rc;
}
#endif

#ifndef NO_SetCursor
JNIEXPORT void JNICALL OS_NATIVE(SetCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, SetCursor_FUNC);
	SetCursor((const Cursor *)arg0);
	OS_NATIVE_EXIT(env, that, SetCursor_FUNC);
}
#endif

#ifndef NO_SetDataBrowserCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCallbacks _arg1={0}, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserCallbacks_FUNC);
	if (arg1) if ((lparg1 = getDataBrowserCallbacksFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)SetDataBrowserCallbacks((ControlRef)arg0, (const DataBrowserCallbacks *)lparg1);
fail:
	if (arg1 && lparg1) setDataBrowserCallbacksFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserCallbacks_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserCustomCallbacks
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserCustomCallbacks)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	DataBrowserCustomCallbacks _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserCustomCallbacks_FUNC);
	if (arg1) if ((lparg1 = getDataBrowserCustomCallbacksFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)SetDataBrowserCustomCallbacks((ControlRef)arg0, lparg1);
fail:
	if (arg1 && lparg1) setDataBrowserCustomCallbacksFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserCustomCallbacks_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserHasScrollBars
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserHasScrollBars)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserHasScrollBars_FUNC);
	rc = (jint)SetDataBrowserHasScrollBars((ControlRef)arg0, (Boolean)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserHasScrollBars_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataBooleanValue
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataBooleanValue)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserItemDataBooleanValue_FUNC);
	rc = (jint)SetDataBrowserItemDataBooleanValue((DataBrowserItemDataRef)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserItemDataBooleanValue_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataButtonValue
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataButtonValue)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserItemDataButtonValue_FUNC);
	rc = (jint)SetDataBrowserItemDataButtonValue((DataBrowserItemDataRef)arg0, (ThemeButtonValue)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserItemDataButtonValue_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataIcon
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataIcon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserItemDataIcon_FUNC);
	rc = (jint)SetDataBrowserItemDataIcon((DataBrowserItemDataRef)arg0, (IconRef)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserItemDataIcon_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataItemID
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataItemID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserItemDataItemID_FUNC);
	rc = (jint)SetDataBrowserItemDataItemID((DataBrowserItemDataRef)arg0, (DataBrowserItemID)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserItemDataItemID_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserItemDataText
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserItemDataText)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserItemDataText_FUNC);
	rc = (jint)SetDataBrowserItemDataText((DataBrowserItemDataRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserItemDataText_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewDisclosureColumn
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewDisclosureColumn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserListViewDisclosureColumn_FUNC);
	rc = (jint)SetDataBrowserListViewDisclosureColumn((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserListViewDisclosureColumn_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewHeaderBtnHeight
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewHeaderBtnHeight)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserListViewHeaderBtnHeight_FUNC);
	rc = (jint)SetDataBrowserListViewHeaderBtnHeight((ControlRef)arg0, (UInt16)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserListViewHeaderBtnHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserListViewHeaderDesc
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserListViewHeaderDesc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	DataBrowserListViewHeaderDesc _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserListViewHeaderDesc_FUNC);
	if (arg2) if ((lparg2 = getDataBrowserListViewHeaderDescFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)SetDataBrowserListViewHeaderDesc((ControlRef)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setDataBrowserListViewHeaderDescFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserListViewHeaderDesc_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserPropertyFlags
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserPropertyFlags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserPropertyFlags_FUNC);
	rc = (jint)SetDataBrowserPropertyFlags((ControlRef)arg0, (DataBrowserPropertyID)arg1, (DataBrowserPropertyFlags)arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserPropertyFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserScrollPosition
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserScrollPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserScrollPosition_FUNC);
	rc = (jint)SetDataBrowserScrollPosition((ControlRef)arg0, (UInt32)arg1, (UInt32)arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserScrollPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSelectedItems
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSelectedItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserSelectedItems_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)SetDataBrowserSelectedItems((ControlRef)arg0, (UInt32)arg1, (const DataBrowserItemID *)lparg2, (DataBrowserSetOption)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, SetDataBrowserSelectedItems_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSelectionFlags
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSelectionFlags)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserSelectionFlags_FUNC);
	rc = (jint)SetDataBrowserSelectionFlags((ControlRef)arg0, (DataBrowserSelectionFlags)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserSelectionFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSortOrder
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSortOrder)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserSortOrder_FUNC);
	rc = (jint)SetDataBrowserSortOrder((ControlRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserSortOrder_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserSortProperty
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserSortProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserSortProperty_FUNC);
	rc = (jint)SetDataBrowserSortProperty((ControlRef)arg0, (DataBrowserPropertyID)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserSortProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewColumnPosition
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewColumnPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTableViewColumnPosition_FUNC);
	rc = (jint)SetDataBrowserTableViewColumnPosition((ControlRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTableViewColumnPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewHiliteStyle
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewHiliteStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTableViewHiliteStyle_FUNC);
	rc = (jint)SetDataBrowserTableViewHiliteStyle((ControlRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTableViewHiliteStyle_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewItemRow
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewItemRow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTableViewItemRow_FUNC);
	rc = (jint)SetDataBrowserTableViewItemRow((ControlRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTableViewItemRow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewNamedColumnWidth
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewNamedColumnWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTableViewNamedColumnWidth_FUNC);
	rc = (jint)SetDataBrowserTableViewNamedColumnWidth((ControlRef)arg0, (DataBrowserTableViewColumnID)arg1, (UInt16)arg2);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTableViewNamedColumnWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTableViewRowHeight
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTableViewRowHeight)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTableViewRowHeight_FUNC);
	rc = (jint)SetDataBrowserTableViewRowHeight((ControlRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTableViewRowHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDataBrowserTarget
JNIEXPORT jint JNICALL OS_NATIVE(SetDataBrowserTarget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDataBrowserTarget_FUNC);
	rc = (jint)SetDataBrowserTarget((ControlRef)arg0, (DataBrowserItemID)arg1);
	OS_NATIVE_EXIT(env, that, SetDataBrowserTarget_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragAllowableActions
JNIEXPORT jint JNICALL OS_NATIVE(SetDragAllowableActions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragAllowableActions_FUNC);
	rc = (jint)SetDragAllowableActions((DragRef)arg0, (DragActions)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetDragAllowableActions_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragDropAction
JNIEXPORT jint JNICALL OS_NATIVE(SetDragDropAction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragDropAction_FUNC);
	rc = (jint)SetDragDropAction((DragRef)arg0, (DragActions)arg1);
	OS_NATIVE_EXIT(env, that, SetDragDropAction_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragImageWithCGImage
JNIEXPORT jint JNICALL OS_NATIVE(SetDragImageWithCGImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	CGPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragImageWithCGImage_FUNC);
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)SetDragImageWithCGImage((DragRef)arg0, (CGImageRef)arg1, (HIPoint *)lparg2, (DragImageFlags)arg3);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SetDragImageWithCGImage_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragInputProc
JNIEXPORT jint JNICALL OS_NATIVE(SetDragInputProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragInputProc_FUNC);
	rc = (jint)SetDragInputProc((DragRef)arg0, (DragInputUPP)arg1, (void *)arg2);
	OS_NATIVE_EXIT(env, that, SetDragInputProc_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragItemFlavorData
JNIEXPORT jint JNICALL OS_NATIVE(SetDragItemFlavorData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragItemFlavorData_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)SetDragItemFlavorData((DragRef)arg0, (DragItemRef)arg1, (FlavorType)arg2, (const void *)lparg3, (Size)arg4, (UInt32)arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, SetDragItemFlavorData_FUNC);
	return rc;
}
#endif

#ifndef NO_SetDragSendProc
JNIEXPORT jint JNICALL OS_NATIVE(SetDragSendProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetDragSendProc_FUNC);
	rc = (jint)SetDragSendProc((DragRef)arg0, (DragSendDataUPP)arg1, (void *)arg2);
	OS_NATIVE_EXIT(env, that, SetDragSendProc_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventLoopTimerNextFireTime
JNIEXPORT jint JNICALL OS_NATIVE(SetEventLoopTimerNextFireTime)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventLoopTimerNextFireTime_FUNC);
	rc = (jint)SetEventLoopTimerNextFireTime((EventLoopTimerRef)arg0, (EventTimerInterval)arg1);
	OS_NATIVE_EXIT(env, that, SetEventLoopTimerNextFireTime_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_CGPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_CGPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	CGPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_CGPoint_2_FUNC);
	if (arg4) if ((lparg4 = getCGPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setCGPointFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_CGPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_HICommand_2
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_HICommand_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	HICommand _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_HICommand_2_FUNC);
	if (arg4) if ((lparg4 = getHICommandFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setHICommandFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_HICommand_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_Point_2
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_Point_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	Point _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_Point_2_FUNC);
	if (arg4) if ((lparg4 = getPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) setPointFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIIILorg_eclipse_swt_internal_carbon_Point_2_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3C
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIII_3C_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3I
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIII_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3S
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jshortArray arg4)
{
	jshort *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIII_3S_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIII_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_SetEventParameter__IIII_3Z
JNIEXPORT jint JNICALL OS_NATIVE(SetEventParameter__IIII_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbooleanArray arg4)
{
	jboolean *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetEventParameter__IIII_3Z_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetBooleanArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SetEventParameter((EventRef)arg0, (EventParamName)arg1, (EventParamType)arg2, (UInt32)arg3, (const void *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseBooleanArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, SetEventParameter__IIII_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_SetFontInfoForSelection
JNIEXPORT jint JNICALL OS_NATIVE(SetFontInfoForSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetFontInfoForSelection_FUNC);
	rc = (jint)SetFontInfoForSelection((OSType)arg0, (UInt32)arg1, (void *)arg2, (void *)arg3);
	OS_NATIVE_EXIT(env, that, SetFontInfoForSelection_FUNC);
	return rc;
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
	rc = (jint)SetFrontProcess((const ProcessSerialNumber *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SetFrontProcess_FUNC);
	return rc;
}
#endif

#ifndef NO_SetFrontProcessWithOptions
JNIEXPORT jint JNICALL OS_NATIVE(SetFrontProcessWithOptions)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetFrontProcessWithOptions_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)SetFrontProcessWithOptions((const ProcessSerialNumber *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, SetFrontProcessWithOptions_FUNC);
	return rc;
}
#endif

#ifndef NO_SetGWorld
JNIEXPORT void JNICALL OS_NATIVE(SetGWorld)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetGWorld_FUNC);
	SetGWorld((CGrafPtr)arg0, (GDHandle)arg1);
	OS_NATIVE_EXIT(env, that, SetGWorld_FUNC);
}
#endif

#ifndef NO_SetHandleSize
JNIEXPORT void JNICALL OS_NATIVE(SetHandleSize)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, SetHandleSize_FUNC);
	SetHandleSize((Handle)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetHandleSize_FUNC);
}
#endif

#ifndef NO_SetIconFamilyData
JNIEXPORT jint JNICALL OS_NATIVE(SetIconFamilyData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetIconFamilyData_FUNC);
	rc = (jint)SetIconFamilyData((IconFamilyHandle)arg0, (OSType)arg1, (Handle)arg2);
	OS_NATIVE_EXIT(env, that, SetIconFamilyData_FUNC);
	return rc;
}
#endif

#ifndef NO_SetItemMark
JNIEXPORT void JNICALL OS_NATIVE(SetItemMark)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, SetItemMark_FUNC);
	SetItemMark((MenuRef)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetItemMark_FUNC);
}
#endif

#ifndef NO_SetKeyboardFocus
JNIEXPORT jint JNICALL OS_NATIVE(SetKeyboardFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetKeyboardFocus_FUNC);
	rc = (jint)SetKeyboardFocus((WindowRef)arg0, (ControlRef)arg1, (ControlFocusPart)arg2);
	OS_NATIVE_EXIT(env, that, SetKeyboardFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuCommandMark
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuCommandMark)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jchar arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuCommandMark_FUNC);
	rc = (jint)SetMenuCommandMark((MenuRef)arg0, (MenuCommand)arg1, (UniChar)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuCommandMark_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuFont
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuFont)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuFont_FUNC);
	rc = (jint)SetMenuFont((MenuRef)arg0, (SInt16)arg1, (UInt16)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuFont_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemCommandKey
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemCommandKey)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2, jchar arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemCommandKey_FUNC);
	rc = (jint)SetMenuItemCommandKey((MenuRef)arg0, (MenuItemIndex)arg1, (Boolean)arg2, (UInt16)arg3);
	OS_NATIVE_EXIT(env, that, SetMenuItemCommandKey_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemHierarchicalMenu
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemHierarchicalMenu)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemHierarchicalMenu_FUNC);
	rc = (jint)SetMenuItemHierarchicalMenu((MenuRef)arg0, (MenuItemIndex)arg1, (MenuRef)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuItemHierarchicalMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemIconHandle
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemIconHandle)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemIconHandle_FUNC);
	rc = (jint)SetMenuItemIconHandle((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2, (Handle)arg3);
	OS_NATIVE_EXIT(env, that, SetMenuItemIconHandle_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemKeyGlyph
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemKeyGlyph)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemKeyGlyph_FUNC);
	rc = (jint)SetMenuItemKeyGlyph((MenuRef)arg0, (SInt16)arg1, (SInt16)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuItemKeyGlyph_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemModifiers
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemModifiers)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jbyte arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemModifiers_FUNC);
	rc = (jint)SetMenuItemModifiers((MenuRef)arg0, (SInt16)arg1, (UInt8)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuItemModifiers_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemRefCon
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemRefCon)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemRefCon_FUNC);
	rc = (jint)SetMenuItemRefCon((MenuRef)arg0, (SInt16)arg1, (UInt32)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuItemRefCon_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuItemTextWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuItemTextWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuItemTextWithCFString_FUNC);
	rc = (jint)SetMenuItemTextWithCFString((MenuRef)arg0, (MenuItemIndex)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, SetMenuItemTextWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_SetMenuTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetMenuTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetMenuTitleWithCFString_FUNC);
	rc = (jint)SetMenuTitleWithCFString((MenuRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, SetMenuTitleWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_SetPort
JNIEXPORT void JNICALL OS_NATIVE(SetPort)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, SetPort_FUNC);
	SetPort((GrafPtr)arg0);
	OS_NATIVE_EXIT(env, that, SetPort_FUNC);
}
#endif

#ifndef NO_SetPt
JNIEXPORT void JNICALL OS_NATIVE(SetPt)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2)
{
	Point _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, SetPt_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	SetPt((Point *)lparg0, (short)arg1, (short)arg2);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SetPt_FUNC);
}
#endif

#ifndef NO_SetRect
JNIEXPORT void JNICALL OS_NATIVE(SetRect)
	(JNIEnv *env, jclass that, jobject arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, SetRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	SetRect((Rect *)lparg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, SetRect_FUNC);
}
#endif

#ifndef NO_SetRectRgn
JNIEXPORT void JNICALL OS_NATIVE(SetRectRgn)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshort arg3, jshort arg4)
{
	OS_NATIVE_ENTER(env, that, SetRectRgn_FUNC);
	SetRectRgn((RgnHandle)arg0, (short)arg1, (short)arg2, (short)arg3, (short)arg4);
	OS_NATIVE_EXIT(env, that, SetRectRgn_FUNC);
}
#endif

#ifndef NO_SetRootMenu
JNIEXPORT jint JNICALL OS_NATIVE(SetRootMenu)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetRootMenu_FUNC);
	rc = (jint)SetRootMenu((MenuRef)arg0);
	OS_NATIVE_EXIT(env, that, SetRootMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_SetSystemUIMode
JNIEXPORT jint JNICALL OS_NATIVE(SetSystemUIMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetSystemUIMode_FUNC);
	rc = (jint)SetSystemUIMode((SystemUIMode)arg0, (SystemUIOptions)arg1);
	OS_NATIVE_EXIT(env, that, SetSystemUIMode_FUNC);
	return rc;
}
#endif

#ifndef NO_SetThemeBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeBackground)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeBackground_FUNC);
	rc = (jint)SetThemeBackground((ThemeBrush)arg0, (SInt16)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetThemeBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_SetThemeCursor
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeCursor)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeCursor_FUNC);
	rc = (jint)SetThemeCursor((ThemeCursor)arg0);
	OS_NATIVE_EXIT(env, that, SetThemeCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetThemeDrawingState
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeDrawingState)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeDrawingState_FUNC);
	rc = (jint)SetThemeDrawingState((ThemeDrawingState)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, SetThemeDrawingState_FUNC);
	return rc;
}
#endif

#ifndef NO_SetThemeTextColor
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeTextColor)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeTextColor_FUNC);
	rc = (jint)SetThemeTextColor(arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, SetThemeTextColor_FUNC);
	return rc;
}
#endif

#ifndef NO_SetThemeWindowBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetThemeWindowBackground)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetThemeWindowBackground_FUNC);
	rc = (jint)SetThemeWindowBackground((WindowRef)arg0, (ThemeBrush)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetThemeWindowBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_SetUpControlBackground
JNIEXPORT jint JNICALL OS_NATIVE(SetUpControlBackground)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jboolean arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetUpControlBackground_FUNC);
	rc = (jint)SetUpControlBackground((ControlRef)arg0, (SInt16)arg1, (Boolean)arg2);
	OS_NATIVE_EXIT(env, that, SetUpControlBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_SetUserFocusWindow
JNIEXPORT jint JNICALL OS_NATIVE(SetUserFocusWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetUserFocusWindow_FUNC);
	rc = (jint)SetUserFocusWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, SetUserFocusWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowActivationScope
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowActivationScope)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowActivationScope_FUNC);
	rc = (jint)SetWindowActivationScope((WindowRef)arg0, (WindowActivationScope)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowActivationScope_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowAlpha
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowAlpha)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowAlpha_FUNC);
	rc = (jint)SetWindowAlpha((WindowRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetWindowAlpha_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowBounds
JNIEXPORT void JNICALL OS_NATIVE(SetWindowBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	Rect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, SetWindowBounds_FUNC);
	if (arg2) if ((lparg2 = getRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	SetWindowBounds((WindowRef)arg0, (WindowRegionCode)arg1, (Rect *)lparg2);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, SetWindowBounds_FUNC);
}
#endif

#ifndef NO_SetWindowDefaultButton
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowDefaultButton)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowDefaultButton_FUNC);
	rc = (jint)SetWindowDefaultButton((WindowRef)arg0, (ControlRef)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowDefaultButton_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowGroup
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroup)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowGroup_FUNC);
	rc = (jint)SetWindowGroup((WindowRef)arg0, (WindowGroupRef)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowGroup_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowGroupOwner
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroupOwner)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowGroupOwner_FUNC);
	rc = (jint)SetWindowGroupOwner((WindowGroupRef)arg0, (WindowRef)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowGroupOwner_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowGroupParent
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowGroupParent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowGroupParent_FUNC);
	rc = (jint)SetWindowGroupParent((WindowGroupRef)arg0, (WindowGroupRef)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowGroupParent_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowModality
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowModality)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowModality_FUNC);
	rc = (jint)SetWindowModality((WindowRef)arg0, (WindowModality)arg1, (WindowRef)arg2);
	OS_NATIVE_EXIT(env, that, SetWindowModality_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowModified
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowModified)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowModified_FUNC);
	rc = (jint)SetWindowModified((WindowRef)arg0, arg1);
	OS_NATIVE_EXIT(env, that, SetWindowModified_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowResizeLimits
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowResizeLimits)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	CGPoint _arg1, *lparg1=NULL;
	CGPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowResizeLimits_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)SetWindowResizeLimits((WindowRef)arg0, (HISize *)lparg1, (HISize *)lparg2);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, SetWindowResizeLimits_FUNC);
	return rc;
}
#endif

#ifndef NO_SetWindowTitleWithCFString
JNIEXPORT jint JNICALL OS_NATIVE(SetWindowTitleWithCFString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, SetWindowTitleWithCFString_FUNC);
	rc = (jint)SetWindowTitleWithCFString((WindowRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, SetWindowTitleWithCFString_FUNC);
	return rc;
}
#endif

#ifndef NO_ShowWindow
JNIEXPORT void JNICALL OS_NATIVE(ShowWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, ShowWindow_FUNC);
	ShowWindow((WindowRef)arg0);
	OS_NATIVE_EXIT(env, that, ShowWindow_FUNC);
}
#endif

#ifndef NO_SizeControl
JNIEXPORT void JNICALL OS_NATIVE(SizeControl)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	OS_NATIVE_ENTER(env, that, SizeControl_FUNC);
	SizeControl((ControlRef)arg0, (SInt16)arg1, (SInt16)arg2);
	OS_NATIVE_EXIT(env, that, SizeControl_FUNC);
}
#endif

#ifndef NO_SizeWindow
JNIEXPORT void JNICALL OS_NATIVE(SizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, SizeWindow_FUNC);
	SizeWindow((WindowRef)arg0, (short)arg1, (short)arg2, (Boolean)arg3);
	OS_NATIVE_EXIT(env, that, SizeWindow_FUNC);
}
#endif

#ifndef NO_StillDown
JNIEXPORT jboolean JNICALL OS_NATIVE(StillDown)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, StillDown_FUNC);
	rc = (jboolean)StillDown();
	OS_NATIVE_EXIT(env, that, StillDown_FUNC);
	return rc;
}
#endif

#ifndef NO_SysBeep
JNIEXPORT void JNICALL OS_NATIVE(SysBeep)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, SysBeep_FUNC);
	SysBeep((short)arg0);
	OS_NATIVE_EXIT(env, that, SysBeep_FUNC);
}
#endif

#ifndef NO_TXNCopy
JNIEXPORT jint JNICALL OS_NATIVE(TXNCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNCopy_FUNC);
	rc = (jint)TXNCopy((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNCut
JNIEXPORT jint JNICALL OS_NATIVE(TXNCut)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNCut_FUNC);
	rc = (jint)TXNCut((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNCut_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNDataSize
JNIEXPORT jint JNICALL OS_NATIVE(TXNDataSize)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNDataSize_FUNC);
	rc = (jint)TXNDataSize((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNDataSize_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNDeleteObject
JNIEXPORT void JNICALL OS_NATIVE(TXNDeleteObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TXNDeleteObject_FUNC);
	TXNDeleteObject((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNDeleteObject_FUNC);
}
#endif

#ifndef NO_TXNEchoMode
JNIEXPORT jint JNICALL OS_NATIVE(TXNEchoMode)
	(JNIEnv *env, jclass that, jint arg0, jchar arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNEchoMode_FUNC);
	rc = (jint)TXNEchoMode((TXNObject)arg0, (UniChar)arg1, (TextEncoding)arg2, (Boolean)arg3);
	OS_NATIVE_EXIT(env, that, TXNEchoMode_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetData
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNGetData_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)TXNGetData((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2, (Handle *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, TXNGetData_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetHIRect
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetHIRect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CGRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNGetHIRect_FUNC);
	if (arg2) if ((lparg2 = getCGRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)TXNGetHIRect((TXNObject)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, TXNGetHIRect_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetLineCount
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetLineCount)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNGetLineCount_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)TXNGetLineCount((TXNObject)arg0, (ItemCount *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, TXNGetLineCount_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetLineMetrics
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetLineMetrics)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNGetLineMetrics_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)TXNGetLineMetrics((TXNObject)arg0, (UInt32)arg1, (Fixed *)lparg2, (Fixed *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, TXNGetLineMetrics_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetSelection
JNIEXPORT void JNICALL OS_NATIVE(TXNGetSelection)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, TXNGetSelection_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	TXNGetSelection((TXNObject)arg0, (TXNOffset *)lparg1, (TXNOffset *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, TXNGetSelection_FUNC);
}
#endif

#ifndef NO_TXNGetTXNObjectControls
JNIEXPORT jint JNICALL OS_NATIVE(TXNGetTXNObjectControls)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNGetTXNObjectControls_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)TXNGetTXNObjectControls((TXNObject)arg0, (ItemCount)arg1, (const TXNControlTag *)lparg2, (TXNControlData *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, TXNGetTXNObjectControls_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNGetViewRect
JNIEXPORT void JNICALL OS_NATIVE(TXNGetViewRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, TXNGetViewRect_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	TXNGetViewRect((TXNObject)arg0, lparg1);
fail:
	if (arg1 && lparg1) setRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, TXNGetViewRect_FUNC);
}
#endif

#ifndef NO_TXNHIPointToOffset
JNIEXPORT jint JNICALL OS_NATIVE(TXNHIPointToOffset)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2)
{
	CGPoint _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNHIPointToOffset_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)TXNHIPointToOffset((TXNObject)arg0, (HIPoint *)lparg1, (TXNOffset *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, TXNHIPointToOffset_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNInitTextension
JNIEXPORT jint JNICALL OS_NATIVE(TXNInitTextension)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNInitTextension_FUNC);
	rc = (jint)TXNInitTextension((const TXNMacOSPreferredFontDescription *)arg0, (ItemCount)arg1, (TXNInitOptions)arg2);
	OS_NATIVE_EXIT(env, that, TXNInitTextension_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNOffsetToHIPoint
JNIEXPORT jint JNICALL OS_NATIVE(TXNOffsetToHIPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CGPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNOffsetToHIPoint_FUNC);
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)TXNOffsetToHIPoint((TXNObject)arg0, (TXNOffset)arg1, (HIPoint *)lparg2);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, TXNOffsetToHIPoint_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNPaste
JNIEXPORT jint JNICALL OS_NATIVE(TXNPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNPaste_FUNC);
	rc = (jint)TXNPaste((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNPaste_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNSelectAll
JNIEXPORT void JNICALL OS_NATIVE(TXNSelectAll)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, TXNSelectAll_FUNC);
	TXNSelectAll((TXNObject)arg0);
	OS_NATIVE_EXIT(env, that, TXNSelectAll_FUNC);
}
#endif

#ifndef NO_TXNSetBackground
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetBackground)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	TXNBackground _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNSetBackground_FUNC);
	if (arg1) if ((lparg1 = getTXNBackgroundFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)TXNSetBackground((TXNObject)arg0, (const TXNBackground *)lparg1);
fail:
	if (arg1 && lparg1) setTXNBackgroundFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, TXNSetBackground_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNSetData
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNSetData_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)TXNSetData((TXNObject)arg0, (TXNDataType)arg1, (const void *)lparg2, (ByteCount)arg3, (TXNOffset)arg4, (TXNOffset)arg5);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, TXNSetData_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNSetFrameBounds
JNIEXPORT void JNICALL OS_NATIVE(TXNSetFrameBounds)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, TXNSetFrameBounds_FUNC);
	TXNSetFrameBounds((TXNObject)arg0, (SInt32)arg1, (SInt32)arg2, (SInt32)arg3, (SInt32)arg4, (TXNFrameID)arg5);
	OS_NATIVE_EXIT(env, that, TXNSetFrameBounds_FUNC);
}
#endif

#ifndef NO_TXNSetSelection
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNSetSelection_FUNC);
	rc = (jint)TXNSetSelection((TXNObject)arg0, (TXNOffset)arg1, (TXNOffset)arg2);
	OS_NATIVE_EXIT(env, that, TXNSetSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNSetTXNObjectControls
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetTXNObjectControls)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNSetTXNObjectControls_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)TXNSetTXNObjectControls((TXNObject)arg0, (Boolean)arg1, (ItemCount)arg2, (const TXNControlTag *)lparg3, (const TXNControlData *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, TXNSetTXNObjectControls_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNSetTypeAttributes
JNIEXPORT jint JNICALL OS_NATIVE(TXNSetTypeAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TXNSetTypeAttributes_FUNC);
	rc = (jint)TXNSetTypeAttributes((TXNObject)arg0, (ItemCount)arg1, (const TXNTypeAttributes *)arg2, (TXNOffset)arg3, (TXNOffset)arg4);
	OS_NATIVE_EXIT(env, that, TXNSetTypeAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_TXNShowSelection
JNIEXPORT void JNICALL OS_NATIVE(TXNShowSelection)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, TXNShowSelection_FUNC);
	TXNShowSelection((TXNObject)arg0, (Boolean)arg1);
	OS_NATIVE_EXIT(env, that, TXNShowSelection_FUNC);
}
#endif

#ifndef NO_TextFace
JNIEXPORT void JNICALL OS_NATIVE(TextFace)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, TextFace_FUNC);
	TextFace((StyleParameter)arg0);
	OS_NATIVE_EXIT(env, that, TextFace_FUNC);
}
#endif

#ifndef NO_TextFont
JNIEXPORT void JNICALL OS_NATIVE(TextFont)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, TextFont_FUNC);
	TextFont((short)arg0);
	OS_NATIVE_EXIT(env, that, TextFont_FUNC);
}
#endif

#ifndef NO_TextSize
JNIEXPORT void JNICALL OS_NATIVE(TextSize)
	(JNIEnv *env, jclass that, jshort arg0)
{
	OS_NATIVE_ENTER(env, that, TextSize_FUNC);
	TextSize((short)arg0);
	OS_NATIVE_EXIT(env, that, TextSize_FUNC);
}
#endif

#ifndef NO_TrackDrag
JNIEXPORT jint JNICALL OS_NATIVE(TrackDrag)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	EventRecord _arg1, *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TrackDrag_FUNC);
	if (arg1) if ((lparg1 = getEventRecordFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)TrackDrag((DragRef)arg0, (const EventRecord *)lparg1, (RgnHandle)arg2);
fail:
	if (arg1 && lparg1) setEventRecordFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, TrackDrag_FUNC);
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
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, TrackMouseLocationWithOptions_FUNC);
	if (arg3) if ((lparg3 = getPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetShortArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)TrackMouseLocationWithOptions((GrafPtr)arg0, (OptionBits)arg1, (EventTimeout)arg2, (Point *)lparg3, (UInt32 *)lparg4, (MouseTrackingResult *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseShortArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setPointFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, TrackMouseLocationWithOptions_FUNC);
	return rc;
}
#endif

#ifndef NO_UCKeyTranslate
JNIEXPORT jint JNICALL OS_NATIVE(UCKeyTranslate)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jint arg7, jintArray arg8, jcharArray arg9)
{
	jint *lparg6=NULL;
	jint *lparg8=NULL;
	jchar *lparg9=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UCKeyTranslate_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetCharArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)UCKeyTranslate((const UCKeyboardLayout *)arg0, (UInt16)arg1, (UInt16)arg2, (UInt32)arg3, (UInt32)arg4, (OptionBits)arg5, (UInt32 *)lparg6, (UniCharCount)arg7, (UniCharCount *)lparg8, (UniChar *)lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseCharArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, UCKeyTranslate_FUNC);
	return rc;
}
#endif

#ifndef NO_UTTypeConformsTo
JNIEXPORT jboolean JNICALL OS_NATIVE(UTTypeConformsTo)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UTTypeConformsTo_FUNC);
	rc = (jboolean)UTTypeConformsTo((CFStringRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, UTTypeConformsTo_FUNC);
	return rc;
}
#endif

#ifndef NO_UTTypeCreateAllIdentifiersForTag
JNIEXPORT jint JNICALL OS_NATIVE(UTTypeCreateAllIdentifiersForTag)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UTTypeCreateAllIdentifiersForTag_FUNC);
	rc = (jint)UTTypeCreateAllIdentifiersForTag((CFStringRef)arg0, (CFStringRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, UTTypeCreateAllIdentifiersForTag_FUNC);
	return rc;
}
#endif

#ifndef NO_UTTypeCreatePreferredIdentifierForTag
JNIEXPORT jint JNICALL OS_NATIVE(UTTypeCreatePreferredIdentifierForTag)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UTTypeCreatePreferredIdentifierForTag_FUNC);
	rc = (jint)UTTypeCreatePreferredIdentifierForTag((CFStringRef)arg0, (CFStringRef)arg1, (CFStringRef)arg2);
	OS_NATIVE_EXIT(env, that, UTTypeCreatePreferredIdentifierForTag_FUNC);
	return rc;
}
#endif

#ifndef NO_UTTypeEqual
JNIEXPORT jboolean JNICALL OS_NATIVE(UTTypeEqual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, UTTypeEqual_FUNC);
	rc = (jboolean)UTTypeEqual((CFStringRef)arg0, (CFStringRef)arg1);
	OS_NATIVE_EXIT(env, that, UTTypeEqual_FUNC);
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
	OS_NATIVE_ENTER(env, that, UnionRect_FUNC);
	if (arg0) if ((lparg0 = getRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	UnionRect(lparg0, lparg1, lparg2);
fail:
	if (arg2 && lparg2) setRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, UnionRect_FUNC);
}
#endif

#ifndef NO_UnionRgn
JNIEXPORT void JNICALL OS_NATIVE(UnionRgn)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, UnionRgn_FUNC);
	UnionRgn((RgnHandle)arg0, (RgnHandle)arg1, (RgnHandle)arg2);
	OS_NATIVE_EXIT(env, that, UnionRgn_FUNC);
}
#endif

#ifndef NO_UpdateDataBrowserItems
JNIEXPORT jint JNICALL OS_NATIVE(UpdateDataBrowserItems)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jint arg5)
{
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UpdateDataBrowserItems_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)UpdateDataBrowserItems((ControlRef)arg0, (DataBrowserItemID)arg1, (UInt32)arg2, (const DataBrowserItemID *)lparg3, (DataBrowserPropertyID)arg4, (DataBrowserPropertyID)arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, UpdateDataBrowserItems_FUNC);
	return rc;
}
#endif

#ifndef NO_UpgradeScriptInfoToTextEncoding
JNIEXPORT jint JNICALL OS_NATIVE(UpgradeScriptInfoToTextEncoding)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UpgradeScriptInfoToTextEncoding_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)UpgradeScriptInfoToTextEncoding((ScriptCode)arg0, (LangCode)arg1, (RegionCode)arg2, (ConstStr255Param)lparg3, (TextEncoding *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, UpgradeScriptInfoToTextEncoding_FUNC);
	return rc;
}
#endif

#ifndef NO_UseInputWindow
JNIEXPORT jint JNICALL OS_NATIVE(UseInputWindow)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, UseInputWindow_FUNC);
	rc = (jint)UseInputWindow((TSMDocumentID)arg0, arg1);
	OS_NATIVE_EXIT(env, that, UseInputWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_WaitMouseMoved
JNIEXPORT jboolean JNICALL OS_NATIVE(WaitMouseMoved)
	(JNIEnv *env, jclass that, jobject arg0)
{
	Point _arg0, *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, WaitMouseMoved_FUNC);
	if (arg0) if ((lparg0 = getPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	rc = (jboolean)WaitMouseMoved(*lparg0);
fail:
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, WaitMouseMoved_FUNC);
	return rc;
}
#endif

#ifndef NO_X2Fix
JNIEXPORT jint JNICALL OS_NATIVE(X2Fix)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, X2Fix_FUNC);
	rc = (jint)X2Fix(arg0);
	OS_NATIVE_EXIT(env, that, X2Fix_FUNC);
	return rc;
}
#endif

#ifndef NO_ZoomWindowIdeal
JNIEXPORT jint JNICALL OS_NATIVE(ZoomWindowIdeal)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jobject arg2)
{
	Point _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ZoomWindowIdeal_FUNC);
	if (arg2) if ((lparg2 = getPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)ZoomWindowIdeal((WindowRef)arg0, (WindowPartCode)arg1, (Point *)lparg2);
fail:
	if (arg2 && lparg2) setPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, ZoomWindowIdeal_FUNC);
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

#ifndef NO_kCFNumberFormatterDecimalSeparator
JNIEXPORT jint JNICALL OS_NATIVE(kCFNumberFormatterDecimalSeparator)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kCFNumberFormatterDecimalSeparator_FUNC);
	rc = (jint)kCFNumberFormatterDecimalSeparator;
	OS_NATIVE_EXIT(env, that, kCFNumberFormatterDecimalSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFRunLoopCommonModes
JNIEXPORT jint JNICALL OS_NATIVE(kCFRunLoopCommonModes)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kCFRunLoopCommonModes_FUNC);
	rc = (jint)kCFRunLoopCommonModes;
	OS_NATIVE_EXIT(env, that, kCFRunLoopCommonModes_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFRunLoopDefaultMode
JNIEXPORT jint JNICALL OS_NATIVE(kCFRunLoopDefaultMode)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kCFRunLoopDefaultMode_FUNC);
	rc = (jint)kCFRunLoopDefaultMode;
	OS_NATIVE_EXIT(env, that, kCFRunLoopDefaultMode_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFTypeArrayCallBacks
JNIEXPORT jint JNICALL OS_NATIVE(kCFTypeArrayCallBacks)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kCFTypeArrayCallBacks_FUNC);
	rc = (jint)&kCFTypeArrayCallBacks;
	OS_NATIVE_EXIT(env, that, kCFTypeArrayCallBacks_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFTypeSetCallBacks
JNIEXPORT jint JNICALL OS_NATIVE(kCFTypeSetCallBacks)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kCFTypeSetCallBacks_FUNC);
	rc = (jint)&kCFTypeSetCallBacks;
	OS_NATIVE_EXIT(env, that, kCFTypeSetCallBacks_FUNC);
	return rc;
}
#endif

#ifndef NO_kFontPanelAttributeSizesKey
JNIEXPORT jint JNICALL OS_NATIVE(kFontPanelAttributeSizesKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kFontPanelAttributeSizesKey_FUNC);
	rc = (jint)kFontPanelAttributeSizesKey;
	OS_NATIVE_EXIT(env, that, kFontPanelAttributeSizesKey_FUNC);
	return rc;
}
#endif

#ifndef NO_kFontPanelAttributeTagsKey
JNIEXPORT jint JNICALL OS_NATIVE(kFontPanelAttributeTagsKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kFontPanelAttributeTagsKey_FUNC);
	rc = (jint)kFontPanelAttributeTagsKey;
	OS_NATIVE_EXIT(env, that, kFontPanelAttributeTagsKey_FUNC);
	return rc;
}
#endif

#ifndef NO_kFontPanelAttributeValuesKey
JNIEXPORT jint JNICALL OS_NATIVE(kFontPanelAttributeValuesKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kFontPanelAttributeValuesKey_FUNC);
	rc = (jint)kFontPanelAttributeValuesKey;
	OS_NATIVE_EXIT(env, that, kFontPanelAttributeValuesKey_FUNC);
	return rc;
}
#endif

#ifndef NO_kFontPanelAttributesKey
JNIEXPORT jint JNICALL OS_NATIVE(kFontPanelAttributesKey)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kFontPanelAttributesKey_FUNC);
	rc = (jint)kFontPanelAttributesKey;
	OS_NATIVE_EXIT(env, that, kFontPanelAttributesKey_FUNC);
	return rc;
}
#endif

#ifndef NO_kHIViewWindowContentID
JNIEXPORT jint JNICALL OS_NATIVE(kHIViewWindowContentID)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kHIViewWindowContentID_FUNC);
	rc = (jint)&kHIViewWindowContentID;
	OS_NATIVE_EXIT(env, that, kHIViewWindowContentID_FUNC);
	return rc;
}
#endif

#ifndef NO_kHIViewWindowGrowBoxID
JNIEXPORT jint JNICALL OS_NATIVE(kHIViewWindowGrowBoxID)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kHIViewWindowGrowBoxID_FUNC);
	rc = (jint)&kHIViewWindowGrowBoxID;
	OS_NATIVE_EXIT(env, that, kHIViewWindowGrowBoxID_FUNC);
	return rc;
}
#endif

#ifndef NO_kLSItemContentType
JNIEXPORT jint JNICALL OS_NATIVE(kLSItemContentType)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kLSItemContentType_FUNC);
	rc = (jint)kLSItemContentType;
	OS_NATIVE_EXIT(env, that, kLSItemContentType_FUNC);
	return rc;
}
#endif

#ifndef NO_kPMDocumentFormatPDF
JNIEXPORT jint JNICALL OS_NATIVE(kPMDocumentFormatPDF)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kPMDocumentFormatPDF_FUNC);
	rc = (jint)kPMDocumentFormatPDF;
	OS_NATIVE_EXIT(env, that, kPMDocumentFormatPDF_FUNC);
	return rc;
}
#endif

#ifndef NO_kPMGraphicsContextCoreGraphics
JNIEXPORT jint JNICALL OS_NATIVE(kPMGraphicsContextCoreGraphics)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kPMGraphicsContextCoreGraphics_FUNC);
	rc = (jint)kPMGraphicsContextCoreGraphics;
	OS_NATIVE_EXIT(env, that, kPMGraphicsContextCoreGraphics_FUNC);
	return rc;
}
#endif

#ifndef NO_kUTTagClassFilenameExtension
JNIEXPORT jint JNICALL OS_NATIVE(kUTTagClassFilenameExtension)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, kUTTagClassFilenameExtension_FUNC);
	rc = (jint)kUTTagClassFilenameExtension;
	OS_NATIVE_EXIT(env, that, kUTTagClassFilenameExtension_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	ATSUTab _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I_FUNC);
	if (arg1) if ((lparg1 = getATSUTabFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_ATSUTab_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_BitMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_BitMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	BitMap _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_BitMap_2I_FUNC);
	if (arg1) if ((lparg1 = getBitMapFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_BitMap_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_Cursor_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_Cursor_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Cursor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_Cursor_2I_FUNC);
	if (arg1) if ((lparg1 = getCursorFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_Cursor_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	HMHelpContentRec _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I_FUNC);
	if (arg1) if ((lparg1 = getHMHelpContentRecFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_HMHelpContentRec_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_PixMap_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_PixMap_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	PixMap _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_PixMap_2I_FUNC);
	if (arg1) if ((lparg1 = getPixMapFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_PixMap_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_RGBColor_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_RGBColor_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	RGBColor _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_RGBColor_2I_FUNC);
	if (arg1) if ((lparg1 = getRGBColorFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_RGBColor_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_carbon_Rect_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_carbon_Rect_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	Rect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_carbon_Rect_2I_FUNC);
	if (arg1) if ((lparg1 = getRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_carbon_Rect_2I_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_ATSLayoutRecord_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_ATSLayoutRecord_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	ATSLayoutRecord _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_ATSLayoutRecord_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setATSLayoutRecordFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_ATSLayoutRecord_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	ATSTrapezoid _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setATSTrapezoidFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_ATSTrapezoid_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_CGPathElement_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_CGPathElement_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	CGPathElement _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_CGPathElement_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setCGPathElementFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_CGPathElement_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_GDevice_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_GDevice_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GDevice _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_GDevice_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGDeviceFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_GDevice_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	HMHelpContentRec _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setHMHelpContentRecFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_HMHelpContentRec_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_NavCBRec_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_NavCBRec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NavCBRec _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavCBRec_2II_FUNC);
	if (arg0) if ((lparg0 = getNavCBRecFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setNavCBRecFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavCBRec_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_NavFileOrFolderInfo_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_NavFileOrFolderInfo_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NavFileOrFolderInfo _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavFileOrFolderInfo_2II_FUNC);
	if (arg0) if ((lparg0 = getNavFileOrFolderInfoFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setNavFileOrFolderInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavFileOrFolderInfo_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NavMenuItemSpec _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2II_FUNC);
	if (arg0) if ((lparg0 = getNavMenuItemSpecFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setNavMenuItemSpecFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_NavMenuItemSpec_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_PixMap_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_PixMap_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	PixMap _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_PixMap_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setPixMapFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_PixMap_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_Point_2_3II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_Point_2_3II)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1, jint arg2)
{
	Point _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_Point_2_3II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	}
	if (arg0 && lparg0) setPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_Point_2_3II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_RGBColor_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_RGBColor_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	RGBColor _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_RGBColor_2II_FUNC);
	if (arg0) if ((lparg0 = getRGBColorFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setRGBColorFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_RGBColor_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_Rect_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_Rect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	Rect _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_Rect_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_Rect_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_carbon_TextRange_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_carbon_TextRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TextRange _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_carbon_TextRange_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setTextRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_carbon_TextRange_2II_FUNC);
}
#endif

#ifndef NO_memmove___3C_3BI
JNIEXPORT void JNICALL OS_NATIVE(memmove___3C_3BI)
	(JNIEnv *env, jclass that, jcharArray arg0, jbyteArray arg1, jint arg2)
{
	jchar *lparg0=NULL;
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3C_3BI_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1 && lparg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, JNI_ABORT);
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
		if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, memmove___3C_3BI_FUNC);
}
#endif

#ifndef NO_memmove___3ILorg_eclipse_swt_internal_carbon_TXNTab_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove___3ILorg_eclipse_swt_internal_carbon_TXNTab_2I)
	(JNIEnv *env, jclass that, jintArray arg0, jobject arg1, jint arg2)
{
	jint *lparg0=NULL;
	TXNTab _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3ILorg_eclipse_swt_internal_carbon_TXNTab_2I_FUNC);
	if (arg1) if ((lparg1 = getTXNTabFields(env, arg1, &_arg1)) == NULL) goto fail;
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0 && lparg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	OS_NATIVE_EXIT(env, that, memmove___3ILorg_eclipse_swt_internal_carbon_TXNTab_2I_FUNC);
}
#endif


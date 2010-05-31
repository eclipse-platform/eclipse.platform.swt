/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_carbon_OS_##func

#ifndef NO_NewGlobalRef
JNIEXPORT jint JNICALL OS_NATIVE(NewGlobalRef)
	(JNIEnv *env, jclass that, jobject arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, NewGlobalRef_FUNC);
	rc = (jint)(*env)->NewGlobalRef(env, arg0);
	OS_NATIVE_EXIT(env, that, NewGlobalRef_FUNC);
	return rc;
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

#ifndef NO_JNIGetObject
JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jobject rc = 0;
	OS_NATIVE_ENTER(env, that, JNIGetObject_FUNC);
	rc = (jobject)arg0;
	OS_NATIVE_EXIT(env, that, JNIGetObject_FUNC);
	return rc;
}
#endif

#ifndef NO_CGAffineTransformConcat
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformConcat)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloatArray arg1, jfloatArray arg2)
{
	jfloat *lparg0=NULL;
	jfloat *lparg1=NULL;
	jfloat *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformConcat_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg2 = CGAffineTransformConcat(*(CGAffineTransform *)lparg0, *(CGAffineTransform *)lparg1);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, JNI_ABORT);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGAffineTransformConcat_FUNC);
}
#endif

#ifndef NO_CGAffineTransformInvert
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformInvert)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloatArray arg1)
{
	jfloat *lparg0=NULL;
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformInvert_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg1 = CGAffineTransformInvert(*(CGAffineTransform *)lparg0);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGAffineTransformInvert_FUNC);
}
#endif

#ifndef NO_CGAffineTransformMake
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformMake)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jfloatArray arg6)
{
	jfloat *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformMake_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetFloatArrayElements(env, arg6, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg6 = CGAffineTransformMake(arg0, arg1, arg2, arg3, arg4, arg5);
fail:
	if (arg6 && lparg6) (*env)->ReleaseFloatArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, CGAffineTransformMake_FUNC);
}
#endif

#ifndef NO_CGAffineTransformRotate
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformRotate)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloat arg1, jfloatArray arg2)
{
	jfloat *lparg0=NULL;
	jfloat *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformRotate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg2 = CGAffineTransformRotate(*(CGAffineTransform *)lparg0, arg1);
fail:
	if (arg2 && lparg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGAffineTransformRotate_FUNC);
}
#endif

#ifndef NO_CGAffineTransformScale
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformScale)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloat arg1, jfloat arg2, jfloatArray arg3)
{
	jfloat *lparg0=NULL;
	jfloat *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformScale_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg3 = CGAffineTransformScale(*(CGAffineTransform *)lparg0, arg1, arg2);
fail:
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGAffineTransformScale_FUNC);
}
#endif

#ifndef NO_CGAffineTransformTranslate
JNIEXPORT void JNICALL OS_NATIVE(CGAffineTransformTranslate)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloat arg1, jfloat arg2, jfloatArray arg3)
{
	jfloat *lparg0=NULL;
	jfloat *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, CGAffineTransformTranslate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg3 = CGAffineTransformTranslate(*(CGAffineTransform *)lparg0, arg1, arg2);
fail:
	if (arg3 && lparg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGAffineTransformTranslate_FUNC);
}
#endif

#ifndef NO_CGContextGetCTM
JNIEXPORT void JNICALL OS_NATIVE(CGContextGetCTM)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextGetCTM_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	*(CGAffineTransform *)lparg1 = CGContextGetCTM((CGContextRef)arg0);
fail:
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGContextGetCTM_FUNC);
}
#endif

#ifndef NO_CGContextGetPathBoundingBox
JNIEXPORT void JNICALL OS_NATIVE(CGContextGetPathBoundingBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextGetPathBoundingBox_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGContextGetPathBoundingBox((CGContextRef)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextGetPathBoundingBox_FUNC);
}
#endif

#ifndef NO_CGContextGetTextPosition
JNIEXPORT void JNICALL OS_NATIVE(CGContextGetTextPosition)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGPoint _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGContextGetTextPosition_FUNC)
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto failTag;
	*lparg1 = CGContextGetTextPosition((CGContextRef)arg0);
failTag:
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGContextGetTextPosition_FUNC)
}
#endif

#ifndef NO_CGDisplayBounds
JNIEXPORT void JNICALL OS_NATIVE(CGDisplayBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGDisplayBounds_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGDisplayBounds((CGDirectDisplayID)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGDisplayBounds_FUNC);
}
#endif

#ifndef NO_CGRectUnion
JNIEXPORT void JNICALL OS_NATIVE(CGRectUnion)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	CGRect _arg0, *lparg0=NULL;
	CGRect _arg1, *lparg1=NULL;
	CGRect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGRectUnion_FUNC);
	if (arg0) if ((lparg0 = getCGRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = &_arg2) == NULL) goto fail;
	*lparg2 = CGRectUnion(*lparg0, *lparg1);
fail:
	if (arg2 && lparg2) setCGRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, CGRectUnion_FUNC);
}
#endif

#ifndef NO_CGPathGetBoundingBox
JNIEXPORT void JNICALL OS_NATIVE(CGPathGetBoundingBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathGetBoundingBox_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGPathGetBoundingBox((CGPathRef)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGPathGetBoundingBox_FUNC);
}
#endif

#ifndef NO_CGPathGetCurrentPoint
JNIEXPORT void JNICALL OS_NATIVE(CGPathGetCurrentPoint)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGPoint _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGPathGetCurrentPoint_FUNC);
	if (arg1) if ((lparg1 = getCGPointFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGPathGetCurrentPoint((CGPathRef)arg0);
fail:
	if (arg1 && lparg1) setCGPointFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGPathGetCurrentPoint_FUNC);
}
#endif

#ifndef NO_CGPointApplyAffineTransform
JNIEXPORT void JNICALL OS_NATIVE(CGPointApplyAffineTransform)
	(JNIEnv *env, jclass that, jobject arg0, jfloatArray arg1, jobject arg2)
{
	CGPoint _arg0, *lparg0=NULL;
	jfloat *lparg1=NULL;
	CGPoint _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGPointApplyAffineTransform_FUNC);
	if (arg0) if ((lparg0 = getCGPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	*(CGPoint *)lparg2 = CGPointApplyAffineTransform(*(CGPoint *)lparg0, *(CGAffineTransform *)lparg1);
fail:
	if (arg2 && lparg2) setCGPointFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, CGPointApplyAffineTransform_FUNC);
}
#endif

#ifndef NO_CGSizeApplyAffineTransform
JNIEXPORT void JNICALL OS_NATIVE(CGSizeApplyAffineTransform)
	(JNIEnv *env, jclass that, jobject arg0, jfloatArray arg1, jobject arg2)
{
	CGSize _arg0, *lparg0=NULL;
	jfloat *lparg1=NULL;
	CGSize _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, CGSizeApplyAffineTransform_FUNC);
	if (arg0) if ((lparg0 = getCGSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getCGSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	*(CGSize *)lparg2 = CGSizeApplyAffineTransform(*(CGSize *)lparg0, *(CGAffineTransform *)lparg1);
fail:
	if (arg2 && lparg2) setCGSizeFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, CGSizeApplyAffineTransform_FUNC);
}
#endif

#ifndef NO__1_1BIG_1ENDIAN_1_1
JNIEXPORT jboolean JNICALL OS_NATIVE(_1_1BIG_1ENDIAN_1_1)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
#ifdef __BIG_ENDIAN__
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	OS_NATIVE_EXIT(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
	return rc;
}
#endif

#ifndef QDPictGetBounds
JNIEXPORT void JNICALL OS_NATIVE(QDPictGetBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, QDPictGetBounds_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = QDPictGetBounds((QDPictRef)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, QDPictGetBounds_FUNC);
}
#endif
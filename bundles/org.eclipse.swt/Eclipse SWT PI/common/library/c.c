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
#include "c_structs.h"
#include "c_stats.h"

#define C_NATIVE(func) Java_org_eclipse_swt_internal_C_##func

#ifndef NO_PTR_1sizeof
JNIEXPORT jint JNICALL C_NATIVE(PTR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, PTR_1sizeof_FUNC);
	rc = (jint)PTR_sizeof();
	C_NATIVE_EXIT(env, that, PTR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_free
JNIEXPORT void JNICALL C_NATIVE(free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	C_NATIVE_ENTER(env, that, free_FUNC);
	free((void *)arg0);
	C_NATIVE_EXIT(env, that, free_FUNC);
}
#endif

#ifndef NO_getenv
JNIEXPORT jintLong JNICALL C_NATIVE(getenv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	C_NATIVE_ENTER(env, that, getenv_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)getenv((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	C_NATIVE_EXIT(env, that, getenv_FUNC);
	return rc;
}
#endif

#ifndef NO_malloc
JNIEXPORT jintLong JNICALL C_NATIVE(malloc)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	C_NATIVE_ENTER(env, that, malloc_FUNC);
	rc = (jintLong)malloc(arg0);
	C_NATIVE_EXIT(env, that, malloc_FUNC);
	return rc;
}
#endif

#if (!defined(NO_memmove__III) && !defined(JNI64)) || (!defined(NO_memmove__JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__III)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__JJJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
#endif
{
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__III_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__JJJ_FUNC);
#endif
	memmove((void *)arg0, (const void *)arg1, (size_t)arg2);
#ifndef JNI64
	C_NATIVE_EXIT(env, that, memmove__III_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__JJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3BI) && !defined(JNI64)) || (!defined(NO_memmove__J_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3BI)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3BJ)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
#endif
{
	jbyte *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3BI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3BJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3BI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3BJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3CI) && !defined(JNI64)) || (!defined(NO_memmove__J_3CJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3CI)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3CJ)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jintLong arg2)
#endif
{
	jchar *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3CI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3CJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3CI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3CJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3DI) && !defined(JNI64)) || (!defined(NO_memmove__J_3DJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3DI)(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3DJ)(JNIEnv *env, jclass that, jintLong arg0, jdoubleArray arg1, jintLong arg2)
#endif
{
	jdouble *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3DI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3DJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3DI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3DJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3FI) && !defined(JNI64)) || (!defined(NO_memmove__J_3FJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3FI)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3FJ)(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jintLong arg2)
#endif
{
	jfloat *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3FI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3FJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3FI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3FJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3II) && !defined(JNI64)) || (!defined(NO_memmove__J_3IJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3II)(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3IJ)(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintLong arg2)
#endif
{
	jint *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3II_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3IJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3II_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3IJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3JI) && !defined(JNI64)) || (!defined(NO_memmove__J_3JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3JI)(JNIEnv *env, jclass that, jintLong arg0, jlongArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3JJ)(JNIEnv *env, jclass that, jintLong arg0, jlongArray arg1, jintLong arg2)
#endif
{
	jlong *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3JI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3JJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3JI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3JJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__I_3SI) && !defined(JNI64)) || (!defined(NO_memmove__J_3SJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3SI)(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove__J_3SJ)(JNIEnv *env, jclass that, jintLong arg0, jshortArray arg1, jintLong arg2)
#endif
{
	jshort *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove__I_3SI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove__J_3SJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	}
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove__I_3SI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove__J_3SJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3BII) && !defined(JNI64)) || (!defined(NO_memmove___3BJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3BII)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3BJJ)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jbyte *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3BII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3BJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3BII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3BJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3B_3CI) && !defined(JNI64)) || (!defined(NO_memmove___3B_3CJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3B_3CI)(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3B_3CJ)(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jintLong arg2)
#endif
{
	jbyte *lparg0=NULL;
	jchar *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3B_3CI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3B_3CJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
		if (arg1) if ((lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
		if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
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
		if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
		if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
#ifndef JNI64
	C_NATIVE_EXIT(env, that, memmove___3B_3CI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3B_3CJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3CII) && !defined(JNI64)) || (!defined(NO_memmove___3CJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3CII)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3CJJ)(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jchar *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3CII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3CJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3CII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3CJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3DII) && !defined(JNI64)) || (!defined(NO_memmove___3DJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3DII)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3DJJ)(JNIEnv *env, jclass that, jdoubleArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jdouble *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3DII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3DJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3DII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3DJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3FII) && !defined(JNI64)) || (!defined(NO_memmove___3FJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3FII)(JNIEnv *env, jclass that, jfloatArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3FJJ)(JNIEnv *env, jclass that, jfloatArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jfloat *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3FII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3FJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3FII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3FJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3III) && !defined(JNI64)) || (!defined(NO_memmove___3IJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3III)(JNIEnv *env, jclass that, jintArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3IJJ)(JNIEnv *env, jclass that, jintArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jint *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3III_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3IJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3III_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3IJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3I_3BI) && !defined(JNI64)) || (!defined(NO_memmove___3I_3BJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3I_3BI)(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3I_3BJ)(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1, jintLong arg2)
#endif
{
	jint *lparg0=NULL;
	jbyte *lparg1=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3I_3BI_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3I_3BJ_FUNC);
#endif
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
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
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
#ifndef JNI64
	C_NATIVE_EXIT(env, that, memmove___3I_3BI_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3I_3BJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3JII) && !defined(JNI64)) || (!defined(NO_memmove___3JJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3JII)(JNIEnv *env, jclass that, jlongArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3JJJ)(JNIEnv *env, jclass that, jlongArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jlong *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3JII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3JJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3JII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3JJJ_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove___3SII) && !defined(JNI64)) || (!defined(NO_memmove___3SJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL C_NATIVE(memmove___3SII)(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL C_NATIVE(memmove___3SJJ)(JNIEnv *env, jclass that, jshortArray arg0, jintLong arg1, jintLong arg2)
#endif
{
	jshort *lparg0=NULL;
#ifndef JNI64
	C_NATIVE_ENTER(env, that, memmove___3SII_FUNC);
#else
	C_NATIVE_ENTER(env, that, memmove___3SJJ_FUNC);
#endif
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) if ((lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL)) == NULL) goto fail;
	} else
#endif
	{
		if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
	}
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
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
	C_NATIVE_EXIT(env, that, memmove___3SII_FUNC);
#else
	C_NATIVE_EXIT(env, that, memmove___3SJJ_FUNC);
#endif
}
#endif

#ifndef NO_memset
JNIEXPORT jintLong JNICALL C_NATIVE(memset)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2)
{
	jintLong rc = 0;
	C_NATIVE_ENTER(env, that, memset_FUNC);
	rc = (jintLong)memset((void *)arg0, arg1, (size_t)arg2);
	C_NATIVE_EXIT(env, that, memset_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL C_NATIVE(strlen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((char *)arg0);
	C_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif


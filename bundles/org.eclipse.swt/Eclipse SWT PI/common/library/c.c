/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
	(JNIEnv *env, jclass that, jint arg0)
{
	C_NATIVE_ENTER(env, that, free_FUNC);
	free((void *)arg0);
	C_NATIVE_EXIT(env, that, free_FUNC);
}
#endif

#ifndef NO_getenv
JNIEXPORT jint JNICALL C_NATIVE(getenv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	C_NATIVE_ENTER(env, that, getenv_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)getenv((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	C_NATIVE_EXIT(env, that, getenv_FUNC);
	return rc;
}
#endif

#ifndef NO_malloc
JNIEXPORT jint JNICALL C_NATIVE(malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, malloc_FUNC);
	rc = (jint)malloc(arg0);
	C_NATIVE_EXIT(env, that, malloc_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__III
JNIEXPORT void JNICALL C_NATIVE(memmove__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	C_NATIVE_ENTER(env, that, memmove__III_FUNC);
	memmove((void *)arg0, (const void *)arg1, (size_t)arg2);
	C_NATIVE_EXIT(env, that, memmove__III_FUNC);
}
#endif

#ifndef NO_memmove__I_3BI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3BI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3BI_FUNC);
}
#endif

#ifndef NO_memmove__I_3CI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3CI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3CI_FUNC);
}
#endif

#ifndef NO_memmove__I_3DI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3DI)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3DI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3DI_FUNC);
}
#endif

#ifndef NO_memmove__I_3FI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3FI)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	jfloat *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3FI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3FI_FUNC);
}
#endif

#ifndef NO_memmove__I_3II
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3II_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3II_FUNC);
}
#endif

#ifndef NO_memmove__I_3JI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3JI)
	(JNIEnv *env, jclass that, jint arg0, jlongArray arg1, jint arg2)
{
	jlong *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3JI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3JI_FUNC);
}
#endif

#ifndef NO_memmove__I_3SI
JNIEXPORT void JNICALL C_NATIVE(memmove__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove__I_3SI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove__I_3SI_FUNC);
}
#endif

#ifndef NO_memmove___3BII
JNIEXPORT void JNICALL C_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3BII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3BII_FUNC);
}
#endif

#ifndef NO_memmove___3B_3CI
JNIEXPORT void JNICALL C_NATIVE(memmove___3B_3CI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jchar *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove___3B_3CI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3B_3CI_FUNC);
}
#endif

#ifndef NO_memmove___3CII
JNIEXPORT void JNICALL C_NATIVE(memmove___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3CII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3CII_FUNC);
}
#endif

#ifndef NO_memmove___3DII
JNIEXPORT void JNICALL C_NATIVE(memmove___3DII)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jint arg1, jint arg2)
{
	jdouble *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3DII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3DII_FUNC);
}
#endif

#ifndef NO_memmove___3FII
JNIEXPORT void JNICALL C_NATIVE(memmove___3FII)
	(JNIEnv *env, jclass that, jfloatArray arg0, jint arg1, jint arg2)
{
	jfloat *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3FII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3FII_FUNC);
}
#endif

#ifndef NO_memmove___3III
JNIEXPORT void JNICALL C_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3III_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3III_FUNC);
}
#endif

#ifndef NO_memmove___3I_3BI
JNIEXPORT void JNICALL C_NATIVE(memmove___3I_3BI)
	(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1, jint arg2)
{
	jint *lparg0=NULL;
	jbyte *lparg1=NULL;
	C_NATIVE_ENTER(env, that, memmove___3I_3BI_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3I_3BI_FUNC);
}
#endif

#ifndef NO_memmove___3JII
JNIEXPORT void JNICALL C_NATIVE(memmove___3JII)
	(JNIEnv *env, jclass that, jlongArray arg0, jint arg1, jint arg2)
{
	jlong *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3JII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3JII_FUNC);
}
#endif

#ifndef NO_memmove___3SII
JNIEXPORT void JNICALL C_NATIVE(memmove___3SII)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	C_NATIVE_ENTER(env, that, memmove___3SII_FUNC);
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
	C_NATIVE_EXIT(env, that, memmove___3SII_FUNC);
}
#endif

#ifndef NO_memset
JNIEXPORT jint JNICALL C_NATIVE(memset)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, memset_FUNC);
	rc = (jint)memset((void *)arg0, arg1, (size_t)arg2);
	C_NATIVE_EXIT(env, that, memset_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL C_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	C_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((char *)arg0);
	C_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif


/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "agl_structs.h"
#include "agl_stats.h"

#ifndef AGL_NATIVE
#define AGL_NATIVE(func) Java_org_eclipse_swt_internal_opengl_carbon_AGL_##func
#endif

#ifndef NO_aglChoosePixelFormat
JNIEXPORT jint JNICALL AGL_NATIVE(aglChoosePixelFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	AGL_NATIVE_ENTER(env, that, aglChoosePixelFormat_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)aglChoosePixelFormat((const AGLDevice *)arg0, arg1, (const GLint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	AGL_NATIVE_EXIT(env, that, aglChoosePixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_aglCreateContext
JNIEXPORT jint JNICALL AGL_NATIVE(aglCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	AGL_NATIVE_ENTER(env, that, aglCreateContext_FUNC);
	rc = (jint)aglCreateContext((AGLPixelFormat)arg0, (AGLContext)arg1);
	AGL_NATIVE_EXIT(env, that, aglCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_aglDescribePixelFormat
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglDescribePixelFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglDescribePixelFormat_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)aglDescribePixelFormat((AGLPixelFormat)arg0, (GLint)arg1, (GLint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	AGL_NATIVE_EXIT(env, that, aglDescribePixelFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_aglDestroyContext
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglDestroyContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglDestroyContext_FUNC);
	rc = (jboolean)aglDestroyContext((AGLContext)arg0);
	AGL_NATIVE_EXIT(env, that, aglDestroyContext_FUNC);
	return rc;
}
#endif

#ifndef NO_aglDestroyPixelFormat
JNIEXPORT void JNICALL AGL_NATIVE(aglDestroyPixelFormat)
	(JNIEnv *env, jclass that, jint arg0)
{
	AGL_NATIVE_ENTER(env, that, aglDestroyPixelFormat_FUNC);
	aglDestroyPixelFormat((AGLPixelFormat)arg0);
	AGL_NATIVE_EXIT(env, that, aglDestroyPixelFormat_FUNC);
}
#endif

#ifndef NO_aglEnable
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglEnable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglEnable_FUNC);
	rc = (jboolean)aglEnable((AGLContext)arg0, (GLenum)arg1);
	AGL_NATIVE_EXIT(env, that, aglEnable_FUNC);
	return rc;
}
#endif

#ifndef NO_aglGetCurrentContext
JNIEXPORT jint JNICALL AGL_NATIVE(aglGetCurrentContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	AGL_NATIVE_ENTER(env, that, aglGetCurrentContext_FUNC);
	rc = (jint)aglGetCurrentContext();
	AGL_NATIVE_EXIT(env, that, aglGetCurrentContext_FUNC);
	return rc;
}
#endif

#ifndef NO_aglGetDrawable
JNIEXPORT jint JNICALL AGL_NATIVE(aglGetDrawable)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	AGL_NATIVE_ENTER(env, that, aglGetDrawable_FUNC);
	rc = (jint)aglGetDrawable((AGLContext)arg0);
	AGL_NATIVE_EXIT(env, that, aglGetDrawable_FUNC);
	return rc;
}
#endif

#ifndef NO_aglSetCurrentContext
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglSetCurrentContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglSetCurrentContext_FUNC);
	rc = (jboolean)aglSetCurrentContext((AGLContext)arg0);
	AGL_NATIVE_EXIT(env, that, aglSetCurrentContext_FUNC);
	return rc;
}
#endif

#ifndef NO_aglSetDrawable
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglSetDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglSetDrawable_FUNC);
	rc = (jboolean)aglSetDrawable((AGLContext)arg0, (AGLDrawable)arg1);
	AGL_NATIVE_EXIT(env, that, aglSetDrawable_FUNC);
	return rc;
}
#endif

#ifndef NO_aglSetInteger__III
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglSetInteger__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglSetInteger__III_FUNC);
	rc = (jboolean)aglSetInteger((AGLContext)arg0, (GLenum)arg1, (const GLint *)arg2);
	AGL_NATIVE_EXIT(env, that, aglSetInteger__III_FUNC);
	return rc;
}
#endif

#ifndef NO_aglSetInteger__II_3I
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglSetInteger__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglSetInteger__II_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)aglSetInteger((AGLContext)arg0, (GLenum)arg1, (const GLint *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	AGL_NATIVE_EXIT(env, that, aglSetInteger__II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_aglSwapBuffers
JNIEXPORT void JNICALL AGL_NATIVE(aglSwapBuffers)
	(JNIEnv *env, jclass that, jint arg0)
{
	AGL_NATIVE_ENTER(env, that, aglSwapBuffers_FUNC);
	aglSwapBuffers((AGLContext)arg0);
	AGL_NATIVE_EXIT(env, that, aglSwapBuffers_FUNC);
}
#endif

#ifndef NO_aglUpdateContext
JNIEXPORT jboolean JNICALL AGL_NATIVE(aglUpdateContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	AGL_NATIVE_ENTER(env, that, aglUpdateContext_FUNC);
	rc = (jboolean)aglUpdateContext((AGLContext)arg0);
	AGL_NATIVE_EXIT(env, that, aglUpdateContext_FUNC);
	return rc;
}
#endif


/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
#include <GL/glx.h>
#include "swt.h"
#include "structs.h"

#define XGL_NATIVE(func) Java_org_eclipse_swt_opengl_internal_motif_XGL_##func

JNIEXPORT jint JNICALL XGL_NATIVE(glXChooseVisual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	DEBUG_CALL("glXChooseVisual\n")

	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)glXChooseVisual((Display *)arg0, arg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXCopyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("glXCopyContext\n")

	glXCopyContext((Display *)arg0, (GLXContext)arg1, (GLXContext)arg2, arg3);
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("glXCreateContext\n")

	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	rc = (jint)glXCreateContext((Display *)arg0, lparg1, (GLXContext)arg2, arg3);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXCreateGLXPixmap)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint rc;

	DEBUG_CALL("glXCreateGLXPixmap\n")

	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	rc = (jint)glXCreateGLXPixmap((Display *)arg0, lparg1, arg2);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXDestroyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXDestroyContext\n")

	glXDestroyContext((Display *)arg0, (GLXContext)arg1);
}

JNIEXPORT void JNICALL XGL_NATIVE(glXDestroyGLXPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXDestroyGLXPixmap\n")

	glXDestroyGLXPixmap((Display *)arg0, arg1);
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetClientString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXGetClientString\n")

	return (jint)glXGetClientString((Display *)arg0, arg1);
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetConfig)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	DEBUG_CALL("glXGetConfig\n")

	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)glXGetConfig((Display *)arg0, lparg1, arg2, (int *)lparg3);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetCurrentContext)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("glXGetCurrentContext\n")

	return (jint)glXGetCurrentContext();
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetCurrentDrawable)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("glXGetCurrentDrawable\n")

	return (jint)glXGetCurrentDrawable();
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXIsDirect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXIsDirect\n")

	return (jboolean)glXIsDirect((Display *)arg0, (GLXContext)arg1);
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXMakeCurrent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("glXMakeCurrent\n")

	return (jboolean)glXMakeCurrent((Display *)arg0, (GLXDrawable)arg1, (GLXContext)arg2);
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXQueryExtension)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("glXQueryExtension\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)glXQueryExtension((Display *)arg0, (int *)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXQueryExtensionsString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXQueryExtensionsString\n")

	return (jint)glXQueryExtensionsString((Display *)arg0, arg1);
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXQueryServerString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("glXQueryServerString\n")

	return (jint)glXQueryServerString((Display *)arg0, arg1, arg2);
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXQueryVersion)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	DEBUG_CALL("glXQueryVersion\n")

	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)glXQueryVersion((Display *)arg0, (int *)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXSwapBuffers)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("glXSwapBuffers\n")

	glXSwapBuffers((Display *)arg0, (GLXDrawable)arg1);
}

JNIEXPORT void JNICALL XGL_NATIVE(glXUseXFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("glXUseXFont\n")

	glXUseXFont(arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL XGL_NATIVE(glXWaitGL)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("glXWaitGL\n")

	glXWaitGL();
}

JNIEXPORT void JNICALL XGL_NATIVE(glXWaitX)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("glXWaitX\n")

	glXWaitX();
}

JNIEXPORT void JNICALL XGL_NATIVE(memmove__Lorg_eclipse_swt_opengl_internal_motif_XVisualInfo_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XVisualInfo _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove__Lorg_eclipse_swt_opengl_internal_motif_XVisualInfo_2II\n")

	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXVisualInfoFields(env, arg0, lparg0);
}

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
#include <GL/glx.h>
#include <string.h>
#include "swt.h"
#include "structs.h"

#define XGL_NATIVE(func) Java_org_eclipse_swt_opengl_internal_gtk_XGL_##func

JNIEXPORT jint JNICALL XGL_NATIVE(glXChooseVisual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "glXChooseVisual\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)glXChooseVisual((Display *)arg0, arg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glXChooseVisual\n")
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXCopyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glXCopyContext\n")
	glXCopyContext((Display *)arg0, (GLXContext)arg1, (GLXContext)arg2, arg3);
	NATIVE_EXIT(env, that, "glXCopyContext\n")
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jboolean arg3)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "glXCreateContext\n")
	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	rc = (jint)glXCreateContext((Display *)arg0, lparg1, (GLXContext)arg2, arg3);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "glXCreateContext\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXCreateGLXPixmap)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "glXCreateGLXPixmap\n")
	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	rc = (jint)glXCreateGLXPixmap((Display *)arg0, lparg1, arg2);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "glXCreateGLXPixmap\n")
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXDestroyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glXDestroyContext\n")
	glXDestroyContext((Display *)arg0, (GLXContext)arg1);
	NATIVE_EXIT(env, that, "glXDestroyContext\n")
}

JNIEXPORT void JNICALL XGL_NATIVE(glXDestroyGLXPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glXDestroyGLXPixmap\n")
	glXDestroyGLXPixmap((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "glXDestroyGLXPixmap\n")
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetClientString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
    jint rc;
	NATIVE_ENTER(env, that, "glXGetClientString\n")
	rc = (jint)glXGetClientString((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "glXGetClientString\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetConfig)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	XVisualInfo _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "glXGetConfig\n")
	if (arg1) lparg1 = getXVisualInfoFields(env, arg1, &_arg1);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)glXGetConfig((Display *)arg0, lparg1, arg2, (int *)lparg3);
	if (arg1) setXVisualInfoFields(env, arg1, lparg1);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glXGetConfig\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetCurrentContext)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "glXGetCurrentContext\n")
	rc = (jint)glXGetCurrentContext();
	NATIVE_EXIT(env, that, "glXGetCurrentContext\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXGetCurrentDrawable)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "glXGetCurrentDrawable\n")
	rc = (jint)glXGetCurrentDrawable();
	NATIVE_EXIT(env, that, "glXGetCurrentDrawable\n")
	return rc;
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXIsDirect)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
    jboolean result;
	NATIVE_ENTER(env, that, "glXIsDirect\n")
	result = (jboolean)glXIsDirect((Display *)arg0, (GLXContext)arg1);
	NATIVE_EXIT(env, that, "glXIsDirect\n")
	return result;
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXMakeCurrent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
    jboolean result;
	NATIVE_ENTER(env, that, "glXMakeCurrent\n")
	result = (jboolean)glXMakeCurrent((Display *)arg0, (GLXDrawable)arg1, (GLXContext)arg2);
	NATIVE_EXIT(env, that, "glXMakeCurrent\n")
	return result;
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXQueryExtension)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	NATIVE_ENTER(env, that, "glXQueryExtension\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)glXQueryExtension((Display *)arg0, (int *)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glXQueryExtension\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXQueryExtensionsString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
    jint rc;
	NATIVE_ENTER(env, that, "glXQueryExtensionsString\n")
	rc = (jint)glXQueryExtensionsString((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "glXQueryExtensionsString\n")
	return rc;
}

JNIEXPORT jint JNICALL XGL_NATIVE(glXQueryServerString)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
    jint rc;
	NATIVE_ENTER(env, that, "glXQueryServerString\n")
	rc = (jint)glXQueryServerString((Display *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glXQueryServerString\n")
	return rc;
}

JNIEXPORT jboolean JNICALL XGL_NATIVE(glXQueryVersion)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;

	NATIVE_ENTER(env, that, "glXQueryVersion\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)glXQueryVersion((Display *)arg0, (int *)lparg1, (int *)lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glXQueryVersion\n")
	return rc;
}

JNIEXPORT void JNICALL XGL_NATIVE(glXSwapBuffers)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glXSwapBuffers\n")
	glXSwapBuffers((Display *)arg0, (GLXDrawable)arg1);
	NATIVE_EXIT(env, that, "glXSwapBuffers\n")
}

JNIEXPORT void JNICALL XGL_NATIVE(glXUseXFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glXUseXFont\n")
	glXUseXFont(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glXUseXFont\n")
}

JNIEXPORT void JNICALL XGL_NATIVE(glXWaitGL)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glXWaitGL\n")
	glXWaitGL();
	NATIVE_EXIT(env, that, "glXWaitGL\n")
}

JNIEXPORT void JNICALL XGL_NATIVE(glXWaitX)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glXWaitX\n")
	glXWaitX();
	NATIVE_EXIT(env, that, "glXWaitX\n")
}

JNIEXPORT void JNICALL XGL_NATIVE(memmove__Lorg_eclipse_swt_opengl_internal_gtk_XVisualInfo_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XVisualInfo _arg0, *lparg0=NULL;

	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_opengl_internal_gtk_XVisualInfo_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXVisualInfoFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_opengl_internal_gtk_XVisualInfo_2II\n")
}

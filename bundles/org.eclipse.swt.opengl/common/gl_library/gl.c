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
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include "swt.h"

#define GL_NATIVE(func) Java_org_eclipse_swt_opengl_GL_##func

JNIEXPORT void JNICALL GL_NATIVE(glAccum)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glAccum\n")
	glAccum(arg0, arg1);
	NATIVE_EXIT(env, that, "glAccum\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glAlphaFunc)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glAlphaFunc\n")
	glAlphaFunc(arg0, arg1);
	NATIVE_EXIT(env, that, "glAlphaFunc\n")
}

JNIEXPORT jboolean JNICALL GL_NATIVE(glAreTexturesResident)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jbooleanArray arg2)
{
	jint *lparg1=NULL;
	jboolean *lparg2=NULL;
	jboolean rc;

	NATIVE_ENTER(env, that, "glAreTexturesResident\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL);
	rc = (jboolean)glAreTexturesResident(arg0, (unsigned int *)lparg1, lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glAreTexturesResident\n")
	return rc;
}

JNIEXPORT void JNICALL GL_NATIVE(glArrayElement)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glArrayElement\n")
	glArrayElement(arg0);
	NATIVE_EXIT(env, that, "glArrayElement\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glBegin)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glBegin\n")
	glBegin(arg0);
	NATIVE_EXIT(env, that, "glBegin\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glBindTexture)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glBindTexture\n")
	glBindTexture(arg0, arg1);
	NATIVE_EXIT(env, that, "glBindTexture\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;

	NATIVE_ENTER(env, that, "glBitmap\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	glBitmap(arg0, arg1, arg2, arg3, arg4, arg5, (unsigned char *)lparg6);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "glBitmap\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glBlendFunc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glBlendFunc\n")
	glBlendFunc(arg0, arg1);
	NATIVE_EXIT(env, that, "glBlendFunc\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCallList)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glCallList\n")
	glCallList(arg0);
	NATIVE_EXIT(env, that, "glCallList\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;

	NATIVE_ENTER(env, that, "glCallLists__II_3B\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	glCallLists(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glCallLists__II_3B\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;

	NATIVE_ENTER(env, that, "glCallLists__II_3C\n")
	if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	glCallLists(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glCallLists__II_3C\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glCallLists__II_3I\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glCallLists(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glCallLists__II_3I\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glClear\n")
	glClear(arg0);
	NATIVE_EXIT(env, that, "glClear\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClearAccum)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glClearAccum\n")
	glClearAccum(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glClearAccum\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClearColor)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glClearColor\n")
	glClearColor(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glClearColor\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClearDepth)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	NATIVE_ENTER(env, that, "glClearDepth\n")
	glClearDepth(arg0);
	NATIVE_EXIT(env, that, "glClearDepth\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClearIndex)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glClearIndex\n")
	glClearIndex(arg0);
	NATIVE_EXIT(env, that, "glClearIndex\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClearStencil)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glClearStencil\n")
	glClearStencil(arg0);
	NATIVE_EXIT(env, that, "glClearStencil\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glClipPlane)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;

	NATIVE_ENTER(env, that, "glClipPlane\n")
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	glClipPlane(arg0, lparg1);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glClipPlane\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	NATIVE_ENTER(env, that, "glColor3b\n")
	glColor3b(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3b\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3bv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glColor3bv(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3bv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glColor3d\n")
	glColor3d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glColor3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glColor3f\n")
	glColor3f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glColor3fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glColor3i\n")
	glColor3i(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glColor3iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glColor3s\n")
	glColor3s(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3ub)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	NATIVE_ENTER(env, that, "glColor3ub\n")
	glColor3ub(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3ub\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3ubv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3ubv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glColor3ubv((unsigned char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3ubv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3ui)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glColor3ui\n")
	glColor3ui(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3ui\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3uiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3uiv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glColor3uiv((unsigned int *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3uiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3us)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glColor3us\n")
	glColor3us(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glColor3us\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor3usv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor3usv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glColor3usv((unsigned short *)lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor3usv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3)
{
	NATIVE_ENTER(env, that, "glColor4b\n")
	glColor4b(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4b\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4bv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glColor4bv(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4bv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glColor4d\n")
	glColor4d(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glColor4dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glColor4f\n")
	glColor4f(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glColor4fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glColor4i\n")
	glColor4i(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glColor4iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glColor4s\n")
	glColor4s(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4ub)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3)
{
	NATIVE_ENTER(env, that, "glColor4ub\n")
	glColor4ub(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4ub\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4ubv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4ubv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glColor4ubv((unsigned char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4ubv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4ui)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glColor4ui\n")
	glColor4ui(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4ui\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4uiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4uiv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glColor4uiv((unsigned int *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4uiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4us)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glColor4us\n")
	glColor4us(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColor4us\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColor4usv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glColor4usv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glColor4usv((unsigned short *)lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glColor4usv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColorMask)
	(JNIEnv *env, jclass that, jboolean arg0, jboolean arg1, jboolean arg2, jboolean arg3)
{
	NATIVE_ENTER(env, that, "glColorMask\n")
	glColorMask(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glColorMask\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColorMaterial)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glColorMaterial\n")
	glColorMaterial(arg0, arg1);
	NATIVE_EXIT(env, that, "glColorMaterial\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glColorPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "glColorPointer\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	glColorPointer(arg0, arg1, arg2, lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glColorPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCopyPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "glCopyPixels\n")
	glCopyPixels(arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "glCopyPixels\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCopyTexImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "glCopyTexImage1D\n")
	glCopyTexImage1D(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "glCopyTexImage1D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCopyTexImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NATIVE_ENTER(env, that, "glCopyTexImage2D\n")
	glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "glCopyTexImage2D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCopyTexSubImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "glCopyTexSubImage1D\n")
	glCopyTexSubImage1D(arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "glCopyTexSubImage1D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCopyTexSubImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	NATIVE_ENTER(env, that, "glCopyTexSubImage2D\n")
	glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "glCopyTexSubImage2D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glCullFace)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glCullFace\n")
	glCullFace(arg0);
	NATIVE_EXIT(env, that, "glCullFace\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDeleteLists)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glDeleteLists\n")
	glDeleteLists(arg0, arg1);
	NATIVE_EXIT(env, that, "glDeleteLists\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDeleteTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glDeleteTextures\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glDeleteTextures(arg0, (unsigned int *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glDeleteTextures\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDepthFunc)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glDepthFunc\n")
	glDepthFunc(arg0);
	NATIVE_EXIT(env, that, "glDepthFunc\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDepthMask)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	NATIVE_ENTER(env, that, "glDepthMask\n")
	glDepthMask(arg0);
	NATIVE_EXIT(env, that, "glDepthMask\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDepthRange)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	NATIVE_ENTER(env, that, "glDepthRange\n")
	glDepthRange(arg0, arg1);
	NATIVE_EXIT(env, that, "glDepthRange\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDisable)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glDisable\n")
	glDisable(arg0);
	NATIVE_EXIT(env, that, "glDisable\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDisableClientState)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glDisableClientState\n")
	glDisableClientState(arg0);
	NATIVE_EXIT(env, that, "glDisableClientState\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDrawArrays)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glDrawArrays\n")
	glDrawArrays(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glDrawArrays\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDrawBuffer)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glDrawBuffer\n")
	glDrawBuffer(arg0);
	NATIVE_EXIT(env, that, "glDrawBuffer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDrawElements)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "glDrawElements\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	glDrawElements(arg0, arg1, arg2, lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glDrawElements\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glDrawPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;

	NATIVE_ENTER(env, that, "glDrawPixels\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	glDrawPixels(arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "glDrawPixels\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlag)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	NATIVE_ENTER(env, that, "glEdgeFlag\n")
	glEdgeFlag(arg0);
	NATIVE_EXIT(env, that, "glEdgeFlag\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlagPointer)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;

	NATIVE_ENTER(env, that, "glEdgeFlagPointer\n")
	if (arg1) lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL);
	glEdgeFlagPointer(arg0, lparg1);
	if (arg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glEdgeFlagPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlagv)
	(JNIEnv *env, jclass that, jbooleanArray arg0)
{
	jboolean *lparg0=NULL;

	NATIVE_ENTER(env, that, "glEdgeFlagv\n")
	if (arg0) lparg0 = (*env)->GetBooleanArrayElements(env, arg0, NULL);
	glEdgeFlagv(lparg0);
	if (arg0) (*env)->ReleaseBooleanArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glEdgeFlagv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEnable)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glEnable\n")
	glEnable(arg0);
	NATIVE_EXIT(env, that, "glEnable\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEnableClientState)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glEnableClientState\n")
	glEnableClientState(arg0);
	NATIVE_EXIT(env, that, "glEnableClientState\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEnd)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glEnd\n")
	glEnd();
	NATIVE_EXIT(env, that, "glEnd\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEndList)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glEndList\n")
	glEndList();
	NATIVE_EXIT(env, that, "glEndList\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1d)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	NATIVE_ENTER(env, that, "glEvalCoord1d\n")
	glEvalCoord1d(arg0);
	NATIVE_EXIT(env, that, "glEvalCoord1d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glEvalCoord1dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glEvalCoord1dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glEvalCoord1dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1f)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glEvalCoord1f\n")
	glEvalCoord1f(arg0);
	NATIVE_EXIT(env, that, "glEvalCoord1f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glEvalCoord1fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glEvalCoord1fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glEvalCoord1fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	NATIVE_ENTER(env, that, "glEvalCoord2d\n")
	glEvalCoord2d(arg0, arg1);
	NATIVE_EXIT(env, that, "glEvalCoord2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glEvalCoord2dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glEvalCoord2dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glEvalCoord2dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glEvalCoord2f\n")
	glEvalCoord2f(arg0, arg1);
	NATIVE_EXIT(env, that, "glEvalCoord2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glEvalCoord2fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glEvalCoord2fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glEvalCoord2fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalMesh1)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glEvalMesh1\n")
	glEvalMesh1(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glEvalMesh1\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalMesh2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "glEvalMesh2\n")
	glEvalMesh2(arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "glEvalMesh2\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalPoint1)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glEvalPoint1\n")
	glEvalPoint1(arg0);
	NATIVE_EXIT(env, that, "glEvalPoint1\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glEvalPoint2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glEvalPoint2\n")
	glEvalPoint2(arg0, arg1);
	NATIVE_EXIT(env, that, "glEvalPoint2\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFeedbackBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glFeedbackBuffer\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glFeedbackBuffer(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glFeedbackBuffer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFinish)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glFinish\n")
	glFinish();
	NATIVE_EXIT(env, that, "glFinish\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFlush)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glFlush\n")
	glFlush();
	NATIVE_EXIT(env, that, "glFlush\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFogf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glFogf\n")
	glFogf(arg0, arg1);
	NATIVE_EXIT(env, that, "glFogf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFogfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	NATIVE_ENTER(env, that, "glFogfv\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	glFogfv(arg0, lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glFogfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFogi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glFogi\n")
	glFogi(arg0, arg1);
	NATIVE_EXIT(env, that, "glFogi\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFogiv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glFogiv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glFogiv(arg0, (GLint *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glFogiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFrontFace)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glFrontFace\n")
	glFrontFace(arg0);
	NATIVE_EXIT(env, that, "glFrontFace\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glFrustum)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	NATIVE_ENTER(env, that, "glFrustum\n")
	glFrustum(arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "glFrustum\n")
}

JNIEXPORT jint JNICALL GL_NATIVE(glGenLists)
	(JNIEnv *env, jclass that, jint arg0)
{
    jint rc;
	NATIVE_ENTER(env, that, "glGenLists\n")
	rc = (jint)glGenLists(arg0);
	NATIVE_EXIT(env, that, "glGenLists\n")
	return rc;
}

JNIEXPORT void JNICALL GL_NATIVE(glGenTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGenTextures\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glGenTextures(arg0, (unsigned int *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGenTextures\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetBooleanv)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetBooleanv\n")
	if (arg1) lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL);
	glGetBooleanv(arg0, lparg1);
	if (arg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetBooleanv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetClipPlane)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetClipPlane\n")
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	glGetClipPlane(arg0, lparg1);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetClipPlane\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetDoublev)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetDoublev\n")
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	glGetDoublev(arg0, lparg1);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetDoublev\n")
}

JNIEXPORT jint JNICALL GL_NATIVE(glGetError)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "glGetError\n")
	rc = (jint)glGetError();
	NATIVE_EXIT(env, that, "glGetError\n")
	return rc;
}

JNIEXPORT void JNICALL GL_NATIVE(glGetFloatv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetFloatv\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	glGetFloatv(arg0, lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetFloatv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetIntegerv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetIntegerv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glGetIntegerv(arg0, (GLint *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetIntegerv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetLightfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetLightfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetLightfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetLightfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetLightiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetLightiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetLightiv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetLightiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetMapdv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetMapdv\n")
	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	glGetMapdv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetMapdv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetMapfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetMapfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetMapfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetMapfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetMapiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetMapiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetMapiv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetMapiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetMaterialfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetMaterialfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetMaterialfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetMaterialfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetMaterialiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetMaterialiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetMaterialiv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetMaterialiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetPixelMapfv\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	glGetPixelMapfv(arg0, lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetPixelMapfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapuiv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetPixelMapuiv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glGetPixelMapuiv(arg0, (unsigned int *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetPixelMapuiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapusv)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetPixelMapusv\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	glGetPixelMapusv(arg0, (unsigned short *)lparg1);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetPixelMapusv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetPointerv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glGetPointerv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glGetPointerv(arg0, (void **)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glGetPointerv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetPolygonStipple)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glGetPolygonStipple\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glGetPolygonStipple((unsigned char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glGetPolygonStipple\n")
}

JNIEXPORT jint JNICALL GL_NATIVE(glGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glGetString\n")
	printf ("%s,\n",glGetString(arg0));
	NATIVE_EXIT(env, that, "glGetString\n")
	return 1;
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexEnvfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexEnvfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetTexEnvfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexEnvfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexEnviv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexEnviv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetTexEnviv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexEnviv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexGendv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexGendv\n")
	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	glGetTexGendv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexGendv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexGenfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexGenfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetTexGenfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexGenfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexGeniv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexGeniv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetTexGeniv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexGeniv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;

	NATIVE_ENTER(env, that, "glGetTexImage\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	glGetTexImage(arg0, arg1, arg2, arg3, lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "glGetTexImage\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexLevelParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloatArray arg3)
{
	jfloat *lparg3=NULL;

	NATIVE_ENTER(env, that, "glGetTexLevelParameterfv\n")
	if (arg3) lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL);
	glGetTexLevelParameterfv(arg0, arg1, arg2, lparg3);
	if (arg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glGetTexLevelParameterfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexLevelParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "glGetTexLevelParameteriv\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	glGetTexLevelParameteriv(arg0, arg1, arg2, (GLint *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glGetTexLevelParameteriv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexParameterfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glGetTexParameterfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexParameterfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glGetTexParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glGetTexParameteriv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glGetTexParameteriv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glGetTexParameteriv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glHint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glHint\n")
	glHint(arg0, arg1);
	NATIVE_EXIT(env, that, "glHint\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glIndexMask\n")
	glIndexMask(arg0);
	NATIVE_EXIT(env, that, "glIndexMask\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glIndexPointer\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glIndexPointer(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glIndexPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexd)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	NATIVE_ENTER(env, that, "glIndexd\n")
	glIndexd(arg0);
	NATIVE_EXIT(env, that, "glIndexd\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexdv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glIndexdv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glIndexdv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glIndexdv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexf)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glIndexf\n")
	glIndexf(arg0);
	NATIVE_EXIT(env, that, "glIndexf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexfv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glIndexfv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glIndexfv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glIndexfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexi)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glIndexi\n")
	glIndexi(arg0);
	NATIVE_EXIT(env, that, "glIndexi\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glIndexiv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glIndexiv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glIndexiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexs)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "glIndexs\n")
	glIndexs(arg0);
	NATIVE_EXIT(env, that, "glIndexs\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glIndexsv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glIndexsv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glIndexsv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glIndexsv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glInitNames)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glInitNames\n")
	glInitNames();
	NATIVE_EXIT(env, that, "glInitNames\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glInterleavedArrays)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glInterleavedArrays\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glInterleavedArrays(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glInterleavedArrays\n")
}

JNIEXPORT jboolean JNICALL GL_NATIVE(glIsEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
    jboolean result;
	NATIVE_ENTER(env, that, "glIsEnabled\n")
	result = (jboolean)glIsEnabled(arg0);
	NATIVE_EXIT(env, that, "glIsEnabled\n")
	return result;
}

JNIEXPORT jboolean JNICALL GL_NATIVE(glIsList)
	(JNIEnv *env, jclass that, jint arg0)
{
    jboolean result;
	NATIVE_ENTER(env, that, "glIsList\n")
	result = (jboolean)glIsList(arg0);
	NATIVE_EXIT(env, that, "glIsList\n")
	return result;
}

JNIEXPORT jboolean JNICALL GL_NATIVE(glIsTexture)
	(JNIEnv *env, jclass that, jint arg0)
{
    jboolean result;
	NATIVE_ENTER(env, that, "glIsTexture\n")
	result = (jboolean)glIsTexture(arg0);
	NATIVE_EXIT(env, that, "glIsTexture\n")
	return result;
}

JNIEXPORT void JNICALL GL_NATIVE(glLightModelf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glLightModelf\n")
	glLightModelf(arg0, arg1);
	NATIVE_EXIT(env, that, "glLightModelf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightModelfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;

	NATIVE_ENTER(env, that, "glLightModelfv\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	glLightModelfv(arg0, lparg1);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glLightModelfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightModeli)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glLightModeli\n")
	glLightModeli(arg0, arg1);
	NATIVE_EXIT(env, that, "glLightModeli\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightModeliv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glLightModeliv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glLightModeliv(arg0, (GLint *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glLightModeliv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glLightf\n")
	glLightf(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glLightf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glLightfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glLightfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glLightfv\n")
}
/*
JNIEXPORT void JNICALL GL_NATIVE(glLightfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat *arg2)
{
	NATIVE_ENTER(env, that, "glLightfv\n")
	glLightfv(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glLightfv\n")
}
*/
JNIEXPORT void JNICALL GL_NATIVE(glLighti)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glLighti\n")
	glLighti(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glLighti\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLightiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glLightiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glLightiv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glLightiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLineStipple)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "glLineStipple\n")
	glLineStipple(arg0, arg1);
	NATIVE_EXIT(env, that, "glLineStipple\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLineWidth)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glLineWidth\n")
	glLineWidth(arg0);
	NATIVE_EXIT(env, that, "glLineWidth\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glListBase)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glListBase\n")
	glListBase(arg0);
	NATIVE_EXIT(env, that, "glListBase\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLoadIdentity)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glLoadIdentity\n")
	glLoadIdentity();
	NATIVE_EXIT(env, that, "glLoadIdentity\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLoadMatrixd)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glLoadMatrixd\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glLoadMatrixd(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glLoadMatrixd\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLoadMatrixf)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glLoadMatrixf\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glLoadMatrixf(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glLoadMatrixf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLoadName)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glLoadName\n")
	glLoadName(arg0);
	NATIVE_EXIT(env, that, "glLoadName\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glLogicOp)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glLogicOp\n")
	glLogicOp(arg0);
	NATIVE_EXIT(env, that, "glLogicOp\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMap2f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jint arg4,
	  jfloat arg5, jfloat arg6, jint arg7, jint arg8, jfloatArray arg9)
{
	jfloat *lparg9=NULL;

	NATIVE_ENTER(env, that, "glMap2f\n")
	if (arg9) lparg9 = (*env)->GetFloatArrayElements(env, arg9, NULL);
	glMap2f(arg0, arg1, arg2, arg3, arg4 ,arg5, arg6, arg7, arg8, lparg9);
	if (arg9) (*env)->ReleaseFloatArrayElements(env, arg9, lparg9, 0);
	NATIVE_EXIT(env, that, "glMap2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMap2d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4,
	  jdouble arg5, jdouble arg6, jint arg7, jint arg8, jdoubleArray arg9)
{
	jdouble *lparg9=NULL;

	NATIVE_ENTER(env, that, "glMap2d\n")
	if (arg9) lparg9 = (*env)->GetDoubleArrayElements(env, arg9, NULL);
	glMap2d(arg0, arg1, arg2, arg3, arg4 ,arg5, arg6, arg7, arg8, lparg9);
	if (arg9) (*env)->ReleaseDoubleArrayElements(env, arg9, lparg9, 0);
	NATIVE_EXIT(env, that, "glMap2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMap1d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdoubleArray arg5)
{
	jdouble *lparg5=NULL;

	NATIVE_ENTER(env, that, "glMap1d\n")
	if (arg5) lparg5 = (*env)->GetDoubleArrayElements(env, arg5, NULL);
	glMap1d(arg0, arg1, arg2, arg3, arg4, lparg5);
	if (arg5) (*env)->ReleaseDoubleArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "glMap1d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMap1f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jint arg4, jfloatArray arg5)
{
	jfloat *lparg5=NULL;

	NATIVE_ENTER(env, that, "glMap1f\n")
	if (arg5) lparg5 = (*env)->GetFloatArrayElements(env, arg5, NULL);
	glMap1f(arg0, arg1, arg2, arg3, arg4, lparg5);
	if (arg5) (*env)->ReleaseFloatArrayElements(env, arg5, lparg5, 0);
	NATIVE_EXIT(env, that, "glMap1f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMapGrid1d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glMapGrid1d\n")
	glMapGrid1d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glMapGrid1d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMapGrid1f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glMapGrid1f\n")
	glMapGrid1f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glMapGrid1f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMapGrid2d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jdouble arg4, jdouble arg5)
{
	NATIVE_ENTER(env, that, "glMapGrid2d\n")
	glMapGrid2d(arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "glMapGrid2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMapGrid2f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jfloat arg4, jfloat arg5)
{
	NATIVE_ENTER(env, that, "glMapGrid2f\n")
	glMapGrid2f(arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "glMapGrid2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMaterialf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glMaterialf\n")
	glMaterialf(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glMaterialf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMaterialfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glMaterialfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glMaterialfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glMaterialfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMateriali)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glMateriali\n")
	glMateriali(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glMateriali\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMaterialiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glMaterialiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glMaterialiv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glMaterialiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMatrixMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glMatrixMode\n")
	glMatrixMode(arg0);
	NATIVE_EXIT(env, that, "glMatrixMode\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMultMatrixd)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glMultMatrixd\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glMultMatrixd(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glMultMatrixd\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glMultMatrixf)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glMultMatrixf\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glMultMatrixf(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glMultMatrixf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNewList)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glNewList\n")
	glNewList(arg0, arg1);
	NATIVE_EXIT(env, that, "glNewList\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	NATIVE_ENTER(env, that, "glNormal3b\n")
	glNormal3b(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glNormal3b\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glNormal3bv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glNormal3bv(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glNormal3bv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glNormal3d\n")
	glNormal3d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glNormal3d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glNormal3dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glNormal3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glNormal3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glNormal3f\n")
	glNormal3f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glNormal3f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glNormal3fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glNormal3fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glNormal3fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glNormal3i\n")
	glNormal3i(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glNormal3i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glNormal3iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glNormal3iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glNormal3iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glNormal3s\n")
	glNormal3s(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glNormal3s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormal3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glNormal3sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glNormal3sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glNormal3sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glNormalPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glNormalPointer\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glNormalPointer(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glNormalPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glOrtho)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	NATIVE_ENTER(env, that, "glOrtho\n")
	glOrtho(arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "glOrtho\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPassThrough)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glPassThrough\n")
	glPassThrough(arg0);
	NATIVE_EXIT(env, that, "glPassThrough\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelMapfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glPixelMapfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glPixelMapfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glPixelMapfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelMapuiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glPixelMapuiv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glPixelMapuiv(arg0, arg1, (unsigned int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glPixelMapuiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelMapusv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;

	NATIVE_ENTER(env, that, "glPixelMapusv\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	glPixelMapusv(arg0, arg1, (unsigned short *)lparg2);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glPixelMapusv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelStoref)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glPixelStoref\n")
	glPixelStoref(arg0, arg1);
	NATIVE_EXIT(env, that, "glPixelStoref\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelStorei)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glPixelStorei\n")
	glPixelStorei(arg0, arg1);
	NATIVE_EXIT(env, that, "glPixelStorei\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelTransferf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glPixelTransferf\n")
	glPixelTransferf(arg0, arg1);
	NATIVE_EXIT(env, that, "glPixelTransferf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelTransferi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glPixelTransferi\n")
	glPixelTransferi(arg0, arg1);
	NATIVE_EXIT(env, that, "glPixelTransferi\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPixelZoom)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glPixelZoom\n")
	glPixelZoom(arg0, arg1);
	NATIVE_EXIT(env, that, "glPixelZoom\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPointSize)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glPointSize\n")
	glPointSize(arg0);
	NATIVE_EXIT(env, that, "glPointSize\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPolygonMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glPolygonMode\n")
	glPolygonMode(arg0, arg1);
	NATIVE_EXIT(env, that, "glPolygonMode\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPolygonOffset)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glPolygonOffset\n")
	glPolygonOffset(arg0, arg1);
	NATIVE_EXIT(env, that, "glPolygonOffset\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPolygonStipple)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;

	NATIVE_ENTER(env, that, "glPolygonStipple\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glPolygonStipple((unsigned char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glPolygonStipple\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPopAttrib)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glPopAttrib\n")
	glPopAttrib();
	NATIVE_EXIT(env, that, "glPopAttrib\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPopClientAttrib)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glPopClientAttrib\n")
	glPopClientAttrib();
	NATIVE_EXIT(env, that, "glPopClientAttrib\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPopMatrix)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glPopMatrix\n")
	glPopMatrix();
	NATIVE_EXIT(env, that, "glPopMatrix\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPopName)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glPopName\n")
	glPopName();
	NATIVE_EXIT(env, that, "glPopName\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPrioritizeTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jfloatArray arg2)
{
	jint *lparg1=NULL;
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glPrioritizeTextures\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glPrioritizeTextures(arg0, (unsigned int *)lparg1, lparg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glPrioritizeTextures\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPushAttrib)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glPushAttrib\n")
	glPushAttrib(arg0);
	NATIVE_EXIT(env, that, "glPushAttrib\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPushClientAttrib)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glPushClientAttrib\n")
	glPushClientAttrib(arg0);
	NATIVE_EXIT(env, that, "glPushClientAttrib\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPushMatrix)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "glPushMatrix\n")
	glPushMatrix();
	NATIVE_EXIT(env, that, "glPushMatrix\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glPushName)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glPushName\n")
	glPushName(arg0);
	NATIVE_EXIT(env, that, "glPushName\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	NATIVE_ENTER(env, that, "glRasterPos2d\n")
	glRasterPos2d(arg0, arg1);
	NATIVE_EXIT(env, that, "glRasterPos2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos2dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glRasterPos2dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos2dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glRasterPos2f\n")
	glRasterPos2f(arg0, arg1);
	NATIVE_EXIT(env, that, "glRasterPos2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos2fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glRasterPos2fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos2fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glRasterPos2i\n")
	glRasterPos2i(arg0, arg1);
	NATIVE_EXIT(env, that, "glRasterPos2i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos2iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glRasterPos2iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos2iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "glRasterPos2s\n")
	glRasterPos2s(arg0, arg1);
	NATIVE_EXIT(env, that, "glRasterPos2s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos2sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glRasterPos2sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos2sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glRasterPos3d\n")
	glRasterPos3d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glRasterPos3d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos3dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glRasterPos3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glRasterPos3f\n")
	glRasterPos3f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glRasterPos3f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos3fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glRasterPos3fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos3fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glRasterPos3i\n")
	glRasterPos3i(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glRasterPos3i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos3iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glRasterPos3iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos3iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glRasterPos3s\n")
	glRasterPos3s(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glRasterPos3s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos3sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glRasterPos3sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos3sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glRasterPos4d\n")
	glRasterPos4d(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRasterPos4d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos4dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glRasterPos4dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos4dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glRasterPos4f\n")
	glRasterPos4f(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRasterPos4f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos4fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glRasterPos4fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos4fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glRasterPos4i\n")
	glRasterPos4i(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRasterPos4i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos4iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glRasterPos4iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos4iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glRasterPos4s\n")
	glRasterPos4s(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRasterPos4s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glRasterPos4sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glRasterPos4sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glRasterPos4sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glReadBuffer)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glReadBuffer\n")
	glReadBuffer(arg0);
	NATIVE_EXIT(env, that, "glReadBuffer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glReadPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;

	NATIVE_ENTER(env, that, "glReadPixels\n")
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "glReadPixels\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectd)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glRectd\n")
	glRectd(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRectd\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectdv)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdoubleArray arg1)
{
	jdouble *lparg0=NULL;
	jdouble *lparg1=NULL;

	NATIVE_ENTER(env, that, "glRectdv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	glRectdv(lparg0, lparg1);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glRectdv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectf)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glRectf\n")
	glRectf(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRectf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectfv)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloatArray arg1)
{
	jfloat *lparg0=NULL;
	jfloat *lparg1=NULL;

	NATIVE_ENTER(env, that, "glRectfv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	glRectfv(lparg0, lparg1);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glRectfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRecti)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glRecti\n")
	glRecti(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRecti\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectiv)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glRectiv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glRectiv((GLint *)lparg0, (GLint *)lparg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glRectiv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRects)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glRects\n")
	glRects(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRects\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRectsv)
	(JNIEnv *env, jclass that, jshortArray arg0, jshortArray arg1)
{
	jshort *lparg0=NULL;
	jshort *lparg1=NULL;

	NATIVE_ENTER(env, that, "glRectsv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	glRectsv(lparg0, lparg1);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glRectsv\n")
}

JNIEXPORT jint JNICALL GL_NATIVE(glRenderMode)
	(JNIEnv *env, jclass that, jint arg0)
{
    jint rc;
	NATIVE_ENTER(env, that, "glRenderMode\n")
	rc = (jint)glRenderMode(arg0);
	NATIVE_EXIT(env, that, "glRenderMode\n")
	return rc;
}

JNIEXPORT void JNICALL GL_NATIVE(glRotated)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glRotated\n")
	glRotated(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRotated\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glRotatef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glRotatef\n")
	glRotatef(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glRotatef\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glScaled)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glScaled\n")
	glScaled(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glScaled\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glScalef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glScalef\n")
	glScalef(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glScalef\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glScissor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glScissor\n")
	glScissor(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glScissor\n")
}

/*
JNIEXPORT void JNICALL GL_NATIVE(glSelectBuffer)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;

	NATIVE_ENTER(env, that, "glSelectBuffer\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glSelectBuffer(arg0, lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "glSelectBuffer\n")
}
*/
 
 
JNIEXPORT void JNICALL GL_NATIVE(glSelectBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint *arg1)
{
	NATIVE_ENTER(env, that, "glSelectBuffer\n")
	glSelectBuffer(arg0, (unsigned int *)arg1);
	NATIVE_EXIT(env, that, "glSelectBuffer\n")
}


JNIEXPORT void JNICALL GL_NATIVE(glShadeModel)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glShadeModel\n")
	glShadeModel(arg0);
	NATIVE_EXIT(env, that, "glShadeModel\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glStencilFunc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glStencilFunc\n")
	glStencilFunc(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glStencilFunc\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glStencilMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glStencilMask\n")
	glStencilMask(arg0);
	NATIVE_EXIT(env, that, "glStencilMask\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glStencilOp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glStencilOp\n")
	glStencilOp(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glStencilOp\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1d)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	NATIVE_ENTER(env, that, "glTexCoord1d\n")
	glTexCoord1d(arg0);
	NATIVE_EXIT(env, that, "glTexCoord1d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord1dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glTexCoord1dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord1dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1f)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	NATIVE_ENTER(env, that, "glTexCoord1f\n")
	glTexCoord1f(arg0);
	NATIVE_EXIT(env, that, "glTexCoord1f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord1fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glTexCoord1fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord1fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1i)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glTexCoord1i\n")
	glTexCoord1i(arg0);
	NATIVE_EXIT(env, that, "glTexCoord1i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord1iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glTexCoord1iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord1iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1s)
	(JNIEnv *env, jclass that, jshort arg0)
{
	NATIVE_ENTER(env, that, "glTexCoord1s\n")
	glTexCoord1s(arg0);
	NATIVE_EXIT(env, that, "glTexCoord1s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord1sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glTexCoord1sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord1sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	NATIVE_ENTER(env, that, "glTexCoord2d\n")
	glTexCoord2d(arg0, arg1);
	NATIVE_EXIT(env, that, "glTexCoord2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord2dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glTexCoord2dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord2dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glTexCoord2f\n")
	glTexCoord2f(arg0, arg1);
	NATIVE_EXIT(env, that, "glTexCoord2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord2fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glTexCoord2fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord2fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glTexCoord2i\n")
	glTexCoord2i(arg0, arg1);
	NATIVE_EXIT(env, that, "glTexCoord2i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord2iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glTexCoord2iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord2iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "glTexCoord2s\n")
	glTexCoord2s(arg0, arg1);
	NATIVE_EXIT(env, that, "glTexCoord2s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord2sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glTexCoord2sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord2sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glTexCoord3d\n")
	glTexCoord3d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexCoord3d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord3dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glTexCoord3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glTexCoord3f\n")
	glTexCoord3f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexCoord3f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord3fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glTexCoord3fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord3fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glTexCoord3i\n")
	glTexCoord3i(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexCoord3i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord3iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glTexCoord3iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord3iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glTexCoord3s\n")
	glTexCoord3s(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexCoord3s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord3sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glTexCoord3sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord3sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glTexCoord4d\n")
	glTexCoord4d(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glTexCoord4d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord4dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glTexCoord4dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord4dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glTexCoord4f\n")
	glTexCoord4f(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glTexCoord4f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord4fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glTexCoord4fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord4fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glTexCoord4i\n")
	glTexCoord4i(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glTexCoord4i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord4iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glTexCoord4iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord4iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glTexCoord4s\n")
	glTexCoord4s(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glTexCoord4s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glTexCoord4sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glTexCoord4sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glTexCoord4sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexCoordPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "glTexCoordPointer\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	glTexCoordPointer(arg0, arg1, arg2, lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glTexCoordPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexEnvf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glTexEnvf\n")
	glTexEnvf(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexEnvf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexEnvfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexEnvfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glTexEnvfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexEnvfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexEnvi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glTexEnvi\n")
	glTexEnvi(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexEnvi\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexEnviv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexEnviv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glTexEnviv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexEnviv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGend)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glTexGend\n")
	glTexGend(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexGend\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGendv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexGendv\n")
	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	glTexGendv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexGendv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGenf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glTexGenf\n")
	glTexGenf(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexGenf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGenfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexGenfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glTexGenfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexGenfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGeni)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glTexGeni\n")
	glTexGeni(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexGeni\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexGeniv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexGeniv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glTexGeniv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexGeniv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
{
	jint *lparg7=NULL;

	NATIVE_ENTER(env, that, "glTexImage1D\n")
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	glTexImage1D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	NATIVE_EXIT(env, that, "glTexImage1D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jbyteArray arg8)
{
	jbyte *lparg8=NULL;

	NATIVE_ENTER(env, that, "glTexImage2D\n")
	if (arg8) lparg8 = (*env)->GetByteArrayElements(env, arg8, NULL);
	glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8);
	if (arg8) (*env)->ReleaseByteArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "glTexImage2D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexParameterf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glTexParameterf\n")
	glTexParameterf(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexParameterf\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexParameterfv\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	glTexParameterfv(arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexParameterfv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexParameteri)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glTexParameteri\n")
	glTexParameteri(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTexParameteri\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;

	NATIVE_ENTER(env, that, "glTexParameteriv\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	glTexParameteriv(arg0, arg1, (GLint *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "glTexParameteriv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexSubImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;

	NATIVE_ENTER(env, that, "glTexSubImage1D\n")
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	glTexSubImage1D(arg0, arg1, arg2, arg3, arg4, arg5, lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "glTexSubImage1D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTexSubImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintArray arg8)
{
	jint *lparg8=NULL;

	NATIVE_ENTER(env, that, "glTexSubImage2D\n")
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "glTexSubImage2D\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTranslated)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glTranslated\n")
	glTranslated(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTranslated\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glTranslatef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glTranslatef\n")
	glTranslatef(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glTranslatef\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	NATIVE_ENTER(env, that, "glVertex2d\n")
	glVertex2d(arg0, arg1);
	NATIVE_EXIT(env, that, "glVertex2d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex2dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glVertex2dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex2dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	NATIVE_ENTER(env, that, "glVertex2f\n")
	glVertex2f(arg0, arg1);
	NATIVE_EXIT(env, that, "glVertex2f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex2fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glVertex2fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex2fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "glVertex2i\n")
	glVertex2i(arg0, arg1);
	NATIVE_EXIT(env, that, "glVertex2i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex2iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glVertex2iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex2iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	NATIVE_ENTER(env, that, "glVertex2s\n")
	glVertex2s(arg0, arg1);
	NATIVE_EXIT(env, that, "glVertex2s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex2sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glVertex2sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex2sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	NATIVE_ENTER(env, that, "glVertex3d\n")
	glVertex3d(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glVertex3d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3dv__I)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex3dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glVertex3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3dv)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "glVertex3dv\n")
	glVertex3dv((double*)arg0);
	NATIVE_EXIT(env, that, "glVertex3dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "glVertex3f\n")
	glVertex3f(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glVertex3f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex3fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glVertex3fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex3fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "glVertex3i\n")
	glVertex3i(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glVertex3i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex3iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glVertex3iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex3iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	NATIVE_ENTER(env, that, "glVertex3s\n")
	glVertex3s(arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "glVertex3s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex3sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glVertex3sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex3sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "glVertex4d\n")
	glVertex4d(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glVertex4d\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex4dv\n")
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glVertex4dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex4dv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	NATIVE_ENTER(env, that, "glVertex4f\n")
	glVertex4f(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glVertex4f\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex4fv\n")
	if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	glVertex4fv(lparg0);
	if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex4fv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glVertex4i\n")
	glVertex4i(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glVertex4i\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex4iv\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	glVertex4iv((GLint *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex4iv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	NATIVE_ENTER(env, that, "glVertex4s\n")
	glVertex4s(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glVertex4s\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertex4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;

	NATIVE_ENTER(env, that, "glVertex4sv\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	glVertex4sv(lparg0);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "glVertex4sv\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glVertexPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "glVertexPointer\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	glVertexPointer(arg0, arg1, arg2, lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "glVertexPointer\n")
}

JNIEXPORT void JNICALL GL_NATIVE(glViewport)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "glViewport\n")
	glViewport(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "glViewport\n")
}

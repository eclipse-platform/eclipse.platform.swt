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
#include <GL/glu.h>
#include "swt.h"

#define GLU_NATIVE(func) Java_org_eclipse_swt_opengl_GLU_##func

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginCurve)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluBeginCurve\n")
	gluBeginCurve((GLUnurbsObj *)arg0);
	NATIVE_EXIT(env, that, "gluBeginCurve\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluBeginPolygon\n")
	gluBeginPolygon((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluBeginPolygon\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluBeginSurface\n")
	gluBeginSurface((GLUnurbs *)arg0);
	NATIVE_EXIT(env, that, "gluBeginSurface\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluBeginTrim\n")
	gluBeginTrim((GLUnurbs *)arg0);
	NATIVE_EXIT(env, that, "gluBeginTrim\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild1DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluBuild1DMipmaps\n")
	rc = (jint)gluBuild1DMipmaps(arg0, arg1, arg2, arg3, arg4, (const void *)arg5);
	NATIVE_EXIT(env, that, "gluBuild1DMipmaps\n")
	return rc;
}

/*
JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild2DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluBuild2DMipmaps\n")
	rc = (jint)gluBuild2DMipmaps(arg0, arg1, arg2, arg3, arg4, arg5, (const void *)arg6);
	NATIVE_EXIT(env, that, "gluBuild2DMipmaps\n")
	return rc;
}
*/

JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild2DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;
	jint rc;
	
	NATIVE_ENTER(env, that, "gluBuild2DMipmaps\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)gluBuild2DMipmaps(arg0, arg1, arg2, arg3, arg4, arg5, (const void *)lparg6);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);	
	NATIVE_EXIT(env, that, "gluBuild2DMipmaps\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluCylinder)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "gluCylinder\n")
	gluCylinder((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "gluCylinder\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteNurbsRenderer)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluDeleteNurbsRenderer\n")
	gluDeleteNurbsRenderer((GLUnurbs *)arg0);
	NATIVE_EXIT(env, that, "gluDeleteNurbsRenderer\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteQuadric)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluDeleteQuadric\n")
	gluDeleteQuadric((GLUquadric *)arg0);
	NATIVE_EXIT(env, that, "gluDeleteQuadric\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteTess)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluDeleteTess\n")
	gluDeleteTess((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluDeleteTess\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "gluDisk\n")
	gluDisk((GLUquadric *)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "gluDisk\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndCurve)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluEndCurve\n")
	gluEndCurve((GLUnurbsObj *)arg0);
	NATIVE_EXIT(env, that, "gluEndCurve\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluEndPolygon\n")
	gluEndPolygon((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluEndPolygon\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluEndSurface\n")
	gluEndSurface((GLUnurbs *)arg0);
	NATIVE_EXIT(env, that, "gluEndSurface\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluEndTrim\n")
	gluEndTrim((GLUnurbs *)arg0);
	NATIVE_EXIT(env, that, "gluEndTrim\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluErrorString)
	(JNIEnv *env, jclass that, jint arg0)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluErrorString\n")
	rc = (jint)gluErrorString(arg0);
	NATIVE_EXIT(env, that, "gluErrorString\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluGetNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "gluGetNurbsProperty\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	gluGetNurbsProperty((GLUnurbs *)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "gluGetNurbsProperty\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluGetString\n")
	rc = (jint)gluGetString(arg0);
	NATIVE_EXIT(env, that, "gluGetString\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluGetTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;

	NATIVE_ENTER(env, that, "gluGetTessProperty\n")
	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	gluGetTessProperty((GLUtesselator *)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "gluGetTessProperty\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluLoadSamplingMatrices)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloatArray arg2, jintArray arg3)
{
	jfloat *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint *lparg3=NULL;

	NATIVE_ENTER(env, that, "gluLoadSamplingMatrices\n")
	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	gluLoadSamplingMatrices((GLUnurbs *)arg0, lparg1, lparg2, (GLint *)lparg3);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "gluLoadSamplingMatrices\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluLookAt)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6, jdouble arg7, jdouble arg8)
{
	NATIVE_ENTER(env, that, "gluLookAt\n")
	gluLookAt(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "gluLookAt\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewNurbsRenderer)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluNewNurbsRenderer\n")
	rc = (jint)gluNewNurbsRenderer();
	NATIVE_EXIT(env, that, "gluNewNurbsRenderer\n")
	return rc;
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewQuadric)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluNewQuadric\n")
	rc = (jint)gluNewQuadric();
	NATIVE_EXIT(env, that, "gluNewQuadric\n")
	return rc;
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewTess)
	(JNIEnv *env, jclass that)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluNewTess\n")
	rc = (jint)gluNewTess();
	NATIVE_EXIT(env, that, "gluNewTess\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNextContour)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gluNextContour\n")
	gluNextContour((GLUtesselator *)arg0, arg1);
	NATIVE_EXIT(env, that, "gluNextContour\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "gluNurbsCallback\n")
	gluNurbsCallback((GLUnurbs *)arg0, arg1, (GLvoid(*))arg2);
	NATIVE_EXIT(env, that, "gluNurbsCallback\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;

	NATIVE_ENTER(env, that, "gluNurbsCurve\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
	gluNurbsCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "gluNurbsCurve\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "gluNurbsProperty\n")
	gluNurbsProperty((GLUnurbs *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "gluNurbsProperty\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsSurface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6, jfloatArray arg7, jint arg8, jint arg9, jint arg10)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg7=NULL;

	NATIVE_ENTER(env, that, "gluNurbsSurface\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
	if (arg7) lparg7 = (*env)->GetFloatArrayElements(env, arg7, NULL);
	gluNurbsSurface((GLUnurbs *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6, lparg7, arg8, arg9, arg10);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg7) (*env)->ReleaseFloatArrayElements(env, arg7, lparg7, 0);
	NATIVE_EXIT(env, that, "gluNurbsSurface\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluOrtho2D)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "gluOrtho2D\n")
	gluOrtho2D(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "gluOrtho2D\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPartialDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6)
{
	NATIVE_ENTER(env, that, "gluPartialDisk\n")
	gluPartialDisk((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "gluPartialDisk\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPerspective)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "gluPerspective\n")
	gluPerspective(arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "gluPerspective\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPickMatrix)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jintArray arg4)
{
	jint *lparg4=NULL;

	NATIVE_ENTER(env, that, "gluPickMatrix\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	gluPickMatrix(arg0, arg1, arg2, arg3, (GLint *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "gluPickMatrix\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluProject)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdoubleArray arg3, jdoubleArray arg4, jintArray arg5, jdoubleArray arg6, jdoubleArray arg7, jdoubleArray arg8)
{
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	jint *lparg5=NULL;
	jdouble *lparg6=NULL;
	jdouble *lparg7=NULL;
	jdouble *lparg8=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "gluProject\n")
	if (arg3) lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetDoubleArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetDoubleArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetDoubleArrayElements(env, arg8, NULL);
	rc = (jint)gluProject(arg0, arg1, arg2, lparg3, lparg4, (GLint *)lparg5, lparg6, lparg7, lparg8);
	if (arg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseDoubleArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseDoubleArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseDoubleArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "gluProject\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPwlCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jint arg4)
{
	jfloat *lparg2=NULL;

	NATIVE_ENTER(env, that, "gluPwlCurve\n")
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	gluPwlCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, arg4);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "gluPwlCurve\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "gluQuadricCallback\n")
	gluQuadricCallback((GLUquadric *)arg0, arg1, (GLvoid(*))arg2);
	NATIVE_EXIT(env, that, "gluQuadricCallback\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricDrawStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gluQuadricDrawStyle\n")
	gluQuadricDrawStyle((GLUquadric *)arg0, arg1);
	NATIVE_EXIT(env, that, "gluQuadricDrawStyle\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricNormals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gluQuadricNormals\n")
	gluQuadricNormals((GLUquadric *)arg0, arg1);
	NATIVE_EXIT(env, that, "gluQuadricNormals\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricOrientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gluQuadricOrientation\n")
	gluQuadricOrientation((GLUquadric *)arg0, arg1);
	NATIVE_EXIT(env, that, "gluQuadricOrientation\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricTexture)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "gluQuadricTexture\n")
	gluQuadricTexture((GLUquadric *)arg0, arg1);
	NATIVE_EXIT(env, that, "gluQuadricTexture\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluScaleImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
    jint rc;
	NATIVE_ENTER(env, that, "gluScaleImage\n")
	rc = (jint)gluScaleImage(arg0, arg1, arg2, arg3, (const void *)arg4, arg5, arg6, arg7, (void *)arg8);
	NATIVE_EXIT(env, that, "gluScaleImage\n")
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluSphere)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "gluSphere\n")
	gluSphere((GLUquadric *)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "gluSphere\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluTessBeginContour\n")
	gluTessBeginContour((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluTessBeginContour\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "gluTessBeginPolygon\n")
	gluTessBeginPolygon((GLUtesselator *)arg0, (void *)arg1);
	NATIVE_EXIT(env, that, "gluTessBeginPolygon\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "gluTessCallback\n")
	gluTessCallback((GLUtesselator *)arg0, arg1, (GLvoid(*))arg2);
	NATIVE_EXIT(env, that, "gluTessCallback\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluTessEndContour\n")
	gluTessEndContour((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluTessEndContour\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "gluTessEndPolygon\n")
	gluTessEndPolygon((GLUtesselator *)arg0);
	NATIVE_EXIT(env, that, "gluTessEndPolygon\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessNormal)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	NATIVE_ENTER(env, that, "gluTessNormal\n")
	gluTessNormal((GLUtesselator *)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "gluTessNormal\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	NATIVE_ENTER(env, that, "gluTessProperty\n")
	gluTessProperty((GLUtesselator *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "gluTessProperty\n")
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessVertex)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;

	NATIVE_ENTER(env, that, "gluTessVertex\n")
	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	gluTessVertex((GLUtesselator *)arg0, lparg1, (void *)arg2);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "gluTessVertex\n")
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluUnProject)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdoubleArray arg3, jdoubleArray arg4, jintArray arg5, jdoubleArray arg6, jdoubleArray arg7, jdoubleArray arg8)
{
	jdouble *lparg3=NULL;
	jdouble *lparg4=NULL;
	jint *lparg5=NULL;
	jdouble *lparg6=NULL;
	jdouble *lparg7=NULL;
	jdouble *lparg8=NULL;
	jint rc;

	NATIVE_ENTER(env, that, "gluUnProject\n")
	if (arg3) lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetDoubleArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetDoubleArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetDoubleArrayElements(env, arg8, NULL);
	rc = (jint)gluUnProject(arg0, arg1, arg2, lparg3, lparg4, (GLint *)lparg5, lparg6, lparg7, lparg8);
	if (arg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	if (arg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg6) (*env)->ReleaseDoubleArrayElements(env, arg6, lparg6, 0);
	if (arg7) (*env)->ReleaseDoubleArrayElements(env, arg7, lparg7, 0);
	if (arg8) (*env)->ReleaseDoubleArrayElements(env, arg8, lparg8, 0);
	NATIVE_EXIT(env, that, "gluUnProject\n")
	return rc;
}

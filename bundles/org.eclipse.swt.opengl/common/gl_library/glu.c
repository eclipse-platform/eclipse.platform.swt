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
	DEBUG_CALL("gluBeginCurve\n")

	gluBeginCurve((GLUnurbsObj *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluBeginPolygon\n")

	gluBeginPolygon((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluBeginSurface\n")

	gluBeginSurface((GLUnurbs *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluBeginTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluBeginTrim\n")

	gluBeginTrim((GLUnurbs *)arg0);
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild1DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("gluBuild1DMipmaps\n")

	return (jint)gluBuild1DMipmaps(arg0, arg1, arg2, arg3, arg4, (const void *)arg5);
}

/*
JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild2DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	DEBUG_CALL("gluBuild2DMipmaps\n")
	
	return (jint)gluBuild2DMipmaps(arg0, arg1, arg2, arg3, arg4, arg5, (const void *)arg6);
}
*/

JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild2DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;
	jint rc;
	
	DEBUG_CALL("gluBuild2DMipmaps\n")
	if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	rc = (jint)gluBuild2DMipmaps(arg0, arg1, arg2, arg3, arg4, arg5, (const void *)lparg6);
	if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);	
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluCylinder)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5)
{
	DEBUG_CALL("gluCylinder\n")

	gluCylinder((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteNurbsRenderer)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluDeleteNurbsRenderer\n")

	gluDeleteNurbsRenderer((GLUnurbs *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteQuadric)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluDeleteQuadric\n")

	gluDeleteQuadric((GLUquadric *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteTess)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluDeleteTess\n")

	gluDeleteTess((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4)
{
	DEBUG_CALL("gluDisk\n")

	gluDisk((GLUquadric *)arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndCurve)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluEndCurve\n")

	gluEndCurve((GLUnurbsObj *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluEndPolygon\n")

	gluEndPolygon((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluEndSurface\n")

	gluEndSurface((GLUnurbs *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluEndTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluEndTrim\n")

	gluEndTrim((GLUnurbs *)arg0);
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluErrorString)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluErrorString\n")

	return (jint)gluErrorString(arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluGetNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;

	DEBUG_CALL("gluGetNurbsProperty\n")

	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	gluGetNurbsProperty((GLUnurbs *)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluGetString\n")

	return (jint)gluGetString(arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluGetTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;

	DEBUG_CALL("gluGetTessProperty\n")

	if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	gluGetTessProperty((GLUtesselator *)arg0, arg1, lparg2);
	if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluLoadSamplingMatrices)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloatArray arg2, jintArray arg3)
{
	jfloat *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint *lparg3=NULL;

	DEBUG_CALL("gluLoadSamplingMatrices\n")

	if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	gluLoadSamplingMatrices((GLUnurbs *)arg0, lparg1, lparg2, (GLint *)lparg3);
	if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluLookAt)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6, jdouble arg7, jdouble arg8)
{
	DEBUG_CALL("gluLookAt\n")

	gluLookAt(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewNurbsRenderer)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gluNewNurbsRenderer\n")

	return (jint)gluNewNurbsRenderer();
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewQuadric)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gluNewQuadric\n")

	return (jint)gluNewQuadric();
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluNewTess)
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gluNewTess\n")

	return (jint)gluNewTess();
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNextContour)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gluNextContour\n")

	gluNextContour((GLUtesselator *)arg0, arg1);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gluNurbsCallback\n")

	gluNurbsCallback((GLUnurbs *)arg0, arg1, (GLvoid(*))arg2);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;

	DEBUG_CALL("gluNurbsCurve\n")

	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
	gluNurbsCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	DEBUG_CALL("gluNurbsProperty\n")

	gluNurbsProperty((GLUnurbs *)arg0, arg1, arg2);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsSurface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6, jfloatArray arg7, jint arg8, jint arg9, jint arg10)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg7=NULL;

	DEBUG_CALL("gluNurbsSurface\n")

	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
	if (arg7) lparg7 = (*env)->GetFloatArrayElements(env, arg7, NULL);
	gluNurbsSurface((GLUnurbs *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6, lparg7, arg8, arg9, arg10);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
	if (arg7) (*env)->ReleaseFloatArrayElements(env, arg7, lparg7, 0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluOrtho2D)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	DEBUG_CALL("gluOrtho2D\n")

	gluOrtho2D(arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPartialDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6)
{
	DEBUG_CALL("gluPartialDisk\n")

	gluPartialDisk((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPerspective)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	DEBUG_CALL("gluPerspective\n")

	gluPerspective(arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPickMatrix)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jintArray arg4)
{
	jint *lparg4=NULL;

	DEBUG_CALL("gluPickMatrix\n")

	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	gluPickMatrix(arg0, arg1, arg2, arg3, (GLint *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
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

	DEBUG_CALL("gluProject\n")

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
	return rc;
}

JNIEXPORT void JNICALL GLU_NATIVE(gluPwlCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jint arg4)
{
	jfloat *lparg2=NULL;

	DEBUG_CALL("gluPwlCurve\n")

	if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	gluPwlCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, arg4);
	if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gluQuadricCallback\n")

	gluQuadricCallback((GLUquadric *)arg0, arg1, (GLvoid(*))arg2);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricDrawStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gluQuadricDrawStyle\n")

	gluQuadricDrawStyle((GLUquadric *)arg0, arg1);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricNormals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gluQuadricNormals\n")

	gluQuadricNormals((GLUquadric *)arg0, arg1);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricOrientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gluQuadricOrientation\n")

	gluQuadricOrientation((GLUquadric *)arg0, arg1);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricTexture)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	DEBUG_CALL("gluQuadricTexture\n")

	gluQuadricTexture((GLUquadric *)arg0, arg1);
}

JNIEXPORT jint JNICALL GLU_NATIVE(gluScaleImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	DEBUG_CALL("gluScaleImage\n")

	return (jint)gluScaleImage(arg0, arg1, arg2, arg3, (const void *)arg4, arg5, arg6, arg7, (void *)arg8);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluSphere)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2, jint arg3)
{
	DEBUG_CALL("gluSphere\n")

	gluSphere((GLUquadric *)arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluTessBeginContour\n")

	gluTessBeginContour((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	DEBUG_CALL("gluTessBeginPolygon\n")

	gluTessBeginPolygon((GLUtesselator *)arg0, (void *)arg1);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	DEBUG_CALL("gluTessCallback\n")

	gluTessCallback((GLUtesselator *)arg0, arg1, (GLvoid(*))arg2);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluTessEndContour\n")

	gluTessEndContour((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gluTessEndPolygon\n")

	gluTessEndPolygon((GLUtesselator *)arg0);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessNormal)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	DEBUG_CALL("gluTessNormal\n")

	gluTessNormal((GLUtesselator *)arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	DEBUG_CALL("gluTessProperty\n")

	gluTessProperty((GLUtesselator *)arg0, arg1, arg2);
}

JNIEXPORT void JNICALL GLU_NATIVE(gluTessVertex)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;

	DEBUG_CALL("gluTessVertex\n")

	if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	gluTessVertex((GLUtesselator *)arg0, lparg1, (void *)arg2);
	if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
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

	DEBUG_CALL("gluUnProject\n")

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
	return rc;
}

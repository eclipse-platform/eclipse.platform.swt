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

#include "swt.h"
#include "gdip_structs.h"
#include "gdip_stats.h"

extern "C" {

#define Gdip_NATIVE(func) Java_org_eclipse_swt_internal_gdip_Gdip_##func

#ifndef NO_Graphics_1DrawLines
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	Point *points = NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawLines_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg2) {
		points = new Point[arg3];
		for (int i=0, j=0; i<arg3; i++, j+=2) {
			Point *point = new Point(lparg2[j], lparg2[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	rc = (jint)((Graphics *)arg0)->DrawLines((Pen *)arg1, points, (INT)arg3);
fail:
	if (lparg2 && points) delete points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawLines_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawPolygon
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	Point *points = NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawPolygon_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg2) {
		points = new Point[arg3];
		for (int i=0, j=0; i<arg3; i++, j+=2) {
			Point *point = new Point(lparg2[j], lparg2[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	rc = (jint)((Graphics *)arg0)->DrawPolygon((Pen *)arg1, points, (INT)arg3);
fail:
	if (lparg2 && points) delete points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPolygon
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4)
{
	Point *points = NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1FillPolygon_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg2) {
		points = new Point[arg3];
		for (int i=0, j=0; i<arg3; i++, j+=2) {
			Point *point = new Point(lparg2[j], lparg2[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	rc = (jint)((Graphics *)arg0)->FillPolygon((Brush *)arg1, points, (INT)arg3, (FillMode)arg4);
fail:
	if (lparg2 && points) delete points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetPathPoints
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPathPoints)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	PointF *points = NULL;
	jfloat *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1GetPathPoints_FUNC);
	if (arg1) if ((lparg1 = env->GetFloatArrayElements(arg1, NULL)) == NULL) goto fail;
	if (lparg1) {
		points = new PointF[arg2];
	}
	rc = (jint)((GraphicsPath *)arg0)->GetPathPoints(points, arg2);
fail:
	if (lparg1 && points) {
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			lparg1[j] = points[i].X;
			lparg1[j + 1] = points[i].Y;
		}
		delete points;
	}
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetPathPoints_FUNC);
	return rc;
}
#endif

#ifndef NO_Matrix_1TransformPoints__I_3FI
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__I_3FI)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jint arg2)
{
	PointF *points = NULL;
	jfloat *lparg1=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Matrix_1TransformPoints__I_3FI_FUNC);
	if (arg1) if ((lparg1 = env->GetFloatArrayElements(arg1, NULL)) == NULL) goto fail;
	if (lparg1) {
		points = new PointF[arg2];
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			PointF *point = new PointF(lparg1[j], lparg1[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	rc = (jint)((Matrix *)arg0)->TransformPoints(points, arg2);
fail:
	if (lparg1 && points) {
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			lparg1[j] = points[i].X;
			lparg1[j + 1] = points[i].Y;
		}
		delete points;
	}
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformPoints__I_3FI_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetSurroundColors
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetSurroundColors)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	Color *colors;
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetSurroundColors_FUNC);
	if (arg1) if ((lparg1 = env->GetIntArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg1 && lparg2) {
		colors = new Color[lparg2[0]];
		for (int i=0; i<lparg2[0]; i++) {
			colors[i] = *(Color *)lparg1[i];
		}
	}
	rc = (jint)((PathGradientBrush *)arg0)->SetSurroundColors((Color *)colors, (INT *)lparg2);
fail:
	if (lparg1 && lparg2 && colors) {
		delete colors;
	}	
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetSurroundColors_FUNC);
	return rc;
}
#endif

}
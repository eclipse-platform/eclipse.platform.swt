/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jint arg3)
{
	Point *points=NULL;
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
	if (lparg2 && points) delete[] points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawLines_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1DrawPolygon
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawPolygon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jint arg3)
{
	Point *points=NULL;
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
	if (lparg2 && points) delete[] points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_Graphics_1FillPolygon
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1FillPolygon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2, jint arg3, jint arg4)
{
	Point *points=NULL;
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
	if (lparg2 && points) delete[] points;
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, JNI_ABORT);
	Gdip_NATIVE_EXIT(env, that, Graphics_1FillPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1GetPathPoints
JNIEXPORT jint JNICALL Gdip_NATIVE(GraphicsPath_1GetPathPoints)
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
{
	PointF *points=NULL;
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
		delete[] points;
	}
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1GetPathPoints_FUNC);
	return rc;
}
#endif


#if (!defined(NO_Matrix_1TransformPoints__I_3FI) && !defined(JNI64)) || (!defined(NO_Matrix_1TransformPoints__J_3FI) && defined(JNI64))
#ifdef JNI64
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__J_3FI)
#else
JNIEXPORT jint JNICALL Gdip_NATIVE(Matrix_1TransformPoints__I_3FI)
#endif
	(JNIEnv *env, jclass that, jintLong arg0, jfloatArray arg1, jint arg2)
{
	PointF *points=NULL;
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
		delete[] points;
	}
	if (arg1 && lparg1) env->ReleaseFloatArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, Matrix_1TransformPoints__I_3FI_FUNC);
	return rc;
}
#endif

#ifndef NO_LinearGradientBrush_1SetInterpolationColors
JNIEXPORT jint JNICALL Gdip_NATIVE(LinearGradientBrush_1SetInterpolationColors)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jfloatArray arg2, jint arg3)
{
	Color *colors=NULL;
	jintLong *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, LinearGradientBrush_1SetInterpolationColors_FUNC);
	if (arg1) if ((lparg1 = env->GetIntLongArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetFloatArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg1) {
		colors = new Color[arg3];
		for (int i=0; i<arg3; i++) {
			colors[i] = *(Color *)lparg1[i];
		}
	}
	rc = (jint)((LinearGradientBrush *)arg0)->SetInterpolationColors(colors, (const REAL *)lparg2, arg3);
fail:
	if (lparg1 && colors) {
		delete[] colors;
	}
	if (arg2 && lparg2) env->ReleaseFloatArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseIntLongArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, LinearGradientBrush_1SetInterpolationColors_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetInterpolationColors
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetInterpolationColors)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jfloatArray arg2, jint arg3)
{
	Color *colors=NULL;
	jintLong *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetInterpolationColors_FUNC);
	if (arg1) if ((lparg1 = env->GetIntLongArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetFloatArrayElements(arg2, NULL)) == NULL) goto fail;
	if (lparg1) {
		colors = new Color[arg3];
		for (int i=0; i<arg3; i++) {
			colors[i] = *(Color *)lparg1[i];
		}
	}
	rc = (jint)((PathGradientBrush *)arg0)->SetInterpolationColors(colors, (const REAL *)lparg2, arg3);
fail:
	if (lparg1 && colors) {
		delete[] colors;
	}
	if (arg2 && lparg2) env->ReleaseFloatArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseIntLongArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetInterpolationColors_FUNC);
	return rc;
}
#endif

#ifndef NO_PathGradientBrush_1SetSurroundColors
JNIEXPORT jint JNICALL Gdip_NATIVE(PathGradientBrush_1SetSurroundColors)
	(JNIEnv *env, jclass that, jintLong arg0, jintLongArray arg1, jintArray arg2)
{
	Color *colors=NULL;
	jintLong *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, PathGradientBrush_1SetSurroundColors_FUNC);
	if (arg1) if ((lparg1 = env->GetIntLongArrayElements(arg1, NULL)) == NULL) goto fail;
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
		delete[] colors;
	}	
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseIntLongArrayElements(arg1, lparg1, 0);
	Gdip_NATIVE_EXIT(env, that, PathGradientBrush_1SetSurroundColors_FUNC);
	return rc;
}
#endif

#ifndef NO_GraphicsPath_1new___3I_3BII
JNIEXPORT jintLong JNICALL Gdip_NATIVE(GraphicsPath_1new___3I_3BII)
	(JNIEnv *env, jclass that, jintArray arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	Point *points=NULL;
	jint *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	Gdip_NATIVE_ENTER(env, that, GraphicsPath_1new___3I_3BII_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	if (lparg0) {
		points = new Point[arg2];
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			Point *point = new Point(lparg0[j], lparg0[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)new GraphicsPath(points, (BYTE *)lparg1, arg2, (FillMode)arg3);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	if (lparg0 && points) delete[] points;
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	Gdip_NATIVE_EXIT(env, that, GraphicsPath_1new___3I_3BII_FUNC);
	return rc;
}
#endif

#if (!defined(NO_Graphics_1DrawDriverString__IIIII_3FII) && !defined(JNI64)) || (!defined(Graphics_1DrawDriverString__JJIJJ_3FIJ) && defined(JNI64))
#ifdef JNI64
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__JJIJJ_3FIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jfloatArray arg5, jint arg6, jintLong arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__JJIJJ_3FIJ)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jfloatArray arg5, jint arg6, jintLong arg7)
#else
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__IIIII_3FII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jfloatArray arg5, jint arg6, jintLong arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1DrawDriverString__IIIII_3FII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4, jfloatArray arg5, jint arg6, jintLong arg7)
#endif
	
{
	PointF *points=NULL;
	jfloat *lparg5=NULL;
	jint rc = 0;
#ifdef JNI64
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawDriverString__JJIJJ_3FIJ_FUNC);
#else
	Gdip_NATIVE_ENTER(env, that, Graphics_1DrawDriverString__IIIII_3FII_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetFloatArrayElements(arg5, NULL)) == NULL) goto fail;
	if (lparg5) {
		points = new PointF[arg2];
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			PointF *point = new PointF(lparg5[j], lparg5[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	rc = (jint)((Graphics *)arg0)->DrawDriverString((const UINT16 *)arg1, arg2, (const Font *)arg3, (const Brush *)arg4, points, arg6, (const Matrix *)arg7);
fail:
	if (arg5 && lparg5) env->ReleaseFloatArrayElements(arg5, lparg5, 0);
	if (lparg5 && points) delete[] points;
#ifdef JNI64
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawDriverString__JJIJJ_3FIJ_FUNC);
#else
	Gdip_NATIVE_EXIT(env, that, Graphics_1DrawDriverString__IIIII_3FII_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_Graphics_1MeasureDriverString
extern "C" JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureDriverString)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jfloatArray arg4, jint arg5, jintLong arg6, jobject arg7);
JNIEXPORT jint JNICALL Gdip_NATIVE(Graphics_1MeasureDriverString)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLong arg3, jfloatArray arg4, jint arg5, jintLong arg6, jobject arg7)
{
	PointF *points=NULL;
	jfloat *lparg4=NULL;
	RectF _arg7, *lparg7=NULL;
	jint rc = 0;
	Gdip_NATIVE_ENTER(env, that, Graphics_1MeasureDriverString_FUNC);
	if (arg4) if ((lparg4 = env->GetFloatArrayElements(arg4, NULL)) == NULL) goto fail;
	if (lparg4) {
		points = new PointF[arg2];
		for (int i=0, j=0; i<arg2; i++, j+=2) {
			PointF *point = new PointF(lparg4[j], lparg4[j + 1]);
			points[i] = *point;
			delete point;
		}
	}
	if (arg7) if ((lparg7 = getRectFFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((Graphics *)arg0)->MeasureDriverString((const UINT16 *)arg1, arg2, (const Font *)arg3, points, arg5, (const Matrix *)arg6, lparg7);
fail:
	if (arg7 && lparg7) setRectFFields(env, arg7, lparg7);
	if (arg4 && lparg4) env->ReleaseFloatArrayElements(arg4, lparg4, 0);
	if (lparg4 && points) delete[] points;
	Gdip_NATIVE_EXIT(env, that, Graphics_1MeasureDriverString_FUNC);
	return rc;
}
#endif

}

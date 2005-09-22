#include "swt.h"
#include "glu_structs.h"
#include "glu_stats.h"

#define GLU_NATIVE(func) Java_org_eclipse_opengl_GLU_##func

#ifndef NO_gluBeginCurve
JNIEXPORT void JNICALL GLU_NATIVE(gluBeginCurve)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluBeginCurve_FUNC);
	gluBeginCurve((GLUnurbs *)arg0);
	GLU_NATIVE_EXIT(env, that, gluBeginCurve_FUNC);
}
#endif

#ifndef NO_gluBeginPolygon
JNIEXPORT void JNICALL GLU_NATIVE(gluBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluBeginPolygon_FUNC);
	gluBeginPolygon((GLUtesselator*)arg0);
	GLU_NATIVE_EXIT(env, that, gluBeginPolygon_FUNC);
}
#endif

#ifndef NO_gluBeginSurface
JNIEXPORT void JNICALL GLU_NATIVE(gluBeginSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluBeginSurface_FUNC);
	gluBeginSurface((GLUnurbs *)arg0);
	GLU_NATIVE_EXIT(env, that, gluBeginSurface_FUNC);
}
#endif

#ifndef NO_gluBeginTrim
JNIEXPORT void JNICALL GLU_NATIVE(gluBeginTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluBeginTrim_FUNC);
	gluBeginTrim((GLUnurbs *)arg0);
	GLU_NATIVE_EXIT(env, that, gluBeginTrim_FUNC);
}
#endif

#ifndef NO_gluBuild1DMipmaps
JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild1DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluBuild1DMipmaps_FUNC);
	rc = (jint)gluBuild1DMipmaps(arg0, arg1, arg2, arg3, arg4, (const void *)arg5);
	GLU_NATIVE_EXIT(env, that, gluBuild1DMipmaps_FUNC);
	return rc;
}
#endif

#ifndef NO_gluBuild2DMipmaps
JNIEXPORT jint JNICALL GLU_NATIVE(gluBuild2DMipmaps)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluBuild2DMipmaps_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
	} else
#endif
	{
		if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	}
	rc = (jint)gluBuild2DMipmaps(arg0, arg1, arg2, arg3, arg4, arg5, lparg6);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
	} else
#endif
	{
		if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluBuild2DMipmaps_FUNC);
	return rc;
}
#endif

#ifndef NO_gluCylinder
JNIEXPORT void JNICALL GLU_NATIVE(gluCylinder)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3, jint arg4, jint arg5)
{
	GLU_NATIVE_ENTER(env, that, gluCylinder_FUNC);
	gluCylinder((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5);
	GLU_NATIVE_EXIT(env, that, gluCylinder_FUNC);
}
#endif

#ifndef NO_gluDeleteNurbsRenderer
JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteNurbsRenderer)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluDeleteNurbsRenderer_FUNC);
	gluDeleteNurbsRenderer((GLUnurbs *)arg0);
	GLU_NATIVE_EXIT(env, that, gluDeleteNurbsRenderer_FUNC);
}
#endif

#ifndef NO_gluDeleteQuadric
JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteQuadric)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluDeleteQuadric_FUNC);
	gluDeleteQuadric((GLUquadric *)arg0);
	GLU_NATIVE_EXIT(env, that, gluDeleteQuadric_FUNC);
}
#endif

#ifndef NO_gluDeleteTess
JNIEXPORT void JNICALL GLU_NATIVE(gluDeleteTess)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluDeleteTess_FUNC);
	gluDeleteTess((GLUtesselator *)arg0);
	GLU_NATIVE_EXIT(env, that, gluDeleteTess_FUNC);
}
#endif

#ifndef NO_gluDisk
JNIEXPORT void JNICALL GLU_NATIVE(gluDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4)
{
	GLU_NATIVE_ENTER(env, that, gluDisk_FUNC);
	gluDisk((GLUquadricObj *)arg0, arg1, arg2, arg3, arg4);
	GLU_NATIVE_EXIT(env, that, gluDisk_FUNC);
}
#endif

#ifndef NO_gluEndCurve
JNIEXPORT void JNICALL GLU_NATIVE(gluEndCurve)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluEndCurve_FUNC);
	gluEndCurve((GLUnurbsObj *)arg0);
	GLU_NATIVE_EXIT(env, that, gluEndCurve_FUNC);
}
#endif

#ifndef NO_gluEndPolygon
JNIEXPORT void JNICALL GLU_NATIVE(gluEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluEndPolygon_FUNC);
	gluEndPolygon((GLUtriangulatorObj *)arg0);
	GLU_NATIVE_EXIT(env, that, gluEndPolygon_FUNC);
}
#endif

#ifndef NO_gluEndSurface
JNIEXPORT void JNICALL GLU_NATIVE(gluEndSurface)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluEndSurface_FUNC);
	gluEndSurface((GLUnurbsObj *)arg0);
	GLU_NATIVE_EXIT(env, that, gluEndSurface_FUNC);
}
#endif

#ifndef NO_gluEndTrim
JNIEXPORT void JNICALL GLU_NATIVE(gluEndTrim)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluEndTrim_FUNC);
	gluEndTrim((GLUnurbsObj *)arg0);
	GLU_NATIVE_EXIT(env, that, gluEndTrim_FUNC);
}
#endif

#ifndef NO_gluErrorString
JNIEXPORT jint JNICALL GLU_NATIVE(gluErrorString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluErrorString_FUNC);
	rc = (jint)gluErrorString(arg0);
	GLU_NATIVE_EXIT(env, that, gluErrorString_FUNC);
	return rc;
}
#endif

#ifndef NO_gluGetNurbsProperty
JNIEXPORT void JNICALL GLU_NATIVE(gluGetNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GLU_NATIVE_ENTER(env, that, gluGetNurbsProperty_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	gluGetNurbsProperty((GLUnurbs *)arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluGetNurbsProperty_FUNC);
}
#endif

#ifndef NO_gluGetString
JNIEXPORT jint JNICALL GLU_NATIVE(gluGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluGetString_FUNC);
	rc = (jint)gluGetString(arg0);
	GLU_NATIVE_EXIT(env, that, gluGetString_FUNC);
	return rc;
}
#endif

#ifndef NO_gluGetTessProperty
JNIEXPORT void JNICALL GLU_NATIVE(gluGetTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;
	GLU_NATIVE_ENTER(env, that, gluGetTessProperty_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	}
	gluGetTessProperty((GLUtesselator *)arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluGetTessProperty_FUNC);
}
#endif

#ifndef NO_gluLoadSamplingMatrices
JNIEXPORT void JNICALL GLU_NATIVE(gluLoadSamplingMatrices)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1, jfloatArray arg2, jintArray arg3)
{
	jfloat *lparg1=NULL;
	jfloat *lparg2=NULL;
	jint *lparg3=NULL;
	GLU_NATIVE_ENTER(env, that, gluLoadSamplingMatrices_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	gluLoadSamplingMatrices((GLUnurbs *)arg0, lparg1, lparg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluLoadSamplingMatrices_FUNC);
}
#endif

#ifndef NO_gluLookAt
JNIEXPORT void JNICALL GLU_NATIVE(gluLookAt)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5, jdouble arg6, jdouble arg7, jdouble arg8)
{
	GLU_NATIVE_ENTER(env, that, gluLookAt_FUNC);
	gluLookAt(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	GLU_NATIVE_EXIT(env, that, gluLookAt_FUNC);
}
#endif

#ifndef NO_gluNewNurbsRenderer
JNIEXPORT jint JNICALL GLU_NATIVE(gluNewNurbsRenderer)
	(JNIEnv *env, jclass that)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluNewNurbsRenderer_FUNC);
	rc = (jint)gluNewNurbsRenderer();
	GLU_NATIVE_EXIT(env, that, gluNewNurbsRenderer_FUNC);
	return rc;
}
#endif

#ifndef NO_gluNewQuadric
JNIEXPORT jint JNICALL GLU_NATIVE(gluNewQuadric)
	(JNIEnv *env, jclass that)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluNewQuadric_FUNC);
	rc = (jint)gluNewQuadric();
	GLU_NATIVE_EXIT(env, that, gluNewQuadric_FUNC);
	return rc;
}
#endif

#ifndef NO_gluNewTess
JNIEXPORT jint JNICALL GLU_NATIVE(gluNewTess)
	(JNIEnv *env, jclass that)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluNewTess_FUNC);
	rc = (jint)gluNewTess();
	GLU_NATIVE_EXIT(env, that, gluNewTess_FUNC);
	return rc;
}
#endif

#ifndef NO_gluNextContour
JNIEXPORT void JNICALL GLU_NATIVE(gluNextContour)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GLU_NATIVE_ENTER(env, that, gluNextContour_FUNC);
	gluNextContour((GLUtesselator *)arg0, arg1);
	GLU_NATIVE_EXIT(env, that, gluNextContour_FUNC);
}
#endif

#ifndef NO_gluNurbsCallback
JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GLU_NATIVE_ENTER(env, that, gluNurbsCallback_FUNC);
	gluNurbsCallback((GLUnurbs *)arg0, arg1, (GLvoid(*))arg2);
	GLU_NATIVE_EXIT(env, that, gluNurbsCallback_FUNC);
}
#endif

#ifndef NO_gluNurbsCurve
JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;
	GLU_NATIVE_ENTER(env, that, gluNurbsCurve_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
		if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
	}
	gluNurbsCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluNurbsCurve_FUNC);
}
#endif

#ifndef NO_gluNurbsProperty
JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GLU_NATIVE_ENTER(env, that, gluNurbsProperty_FUNC);
	gluNurbsProperty((GLUnurbs *)arg0, arg1, arg2);
	GLU_NATIVE_EXIT(env, that, gluNurbsProperty_FUNC);
}
#endif

#ifndef NO_gluNurbsSurface
JNIEXPORT void JNICALL GLU_NATIVE(gluNurbsSurface)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jfloatArray arg4, jint arg5, jint arg6, jfloatArray arg7, jint arg8, jint arg9, jint arg10)
{
	jfloat *lparg2=NULL;
	jfloat *lparg4=NULL;
	jfloat *lparg7=NULL;
	GLU_NATIVE_ENTER(env, that, gluNurbsSurface_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
		if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
		if (arg4) lparg4 = (*env)->GetFloatArrayElements(env, arg4, NULL);
		if (arg7) lparg7 = (*env)->GetFloatArrayElements(env, arg7, NULL);
	}
	gluNurbsSurface((GLUnurbsObj *)arg0, arg1, lparg2, arg3, lparg4, arg5, arg6, lparg7, arg8, arg9, arg10);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, 0);
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg7) (*env)->ReleaseFloatArrayElements(env, arg7, lparg7, 0);
		if (arg4) (*env)->ReleaseFloatArrayElements(env, arg4, lparg4, 0);
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluNurbsSurface_FUNC);
}
#endif

#ifndef NO_gluOrtho2D
JNIEXPORT void JNICALL GLU_NATIVE(gluOrtho2D)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GLU_NATIVE_ENTER(env, that, gluOrtho2D_FUNC);
	gluOrtho2D(arg0, arg1, arg2, arg3);
	GLU_NATIVE_EXIT(env, that, gluOrtho2D_FUNC);
}
#endif

#ifndef NO_gluPartialDisk
JNIEXPORT void JNICALL GLU_NATIVE(gluPartialDisk)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6)
{
	GLU_NATIVE_ENTER(env, that, gluPartialDisk_FUNC);
	gluPartialDisk((GLUquadric *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	GLU_NATIVE_EXIT(env, that, gluPartialDisk_FUNC);
}
#endif

#ifndef NO_gluPerspective
JNIEXPORT void JNICALL GLU_NATIVE(gluPerspective)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GLU_NATIVE_ENTER(env, that, gluPerspective_FUNC);
	gluPerspective(arg0, arg1, arg2, arg3);
	GLU_NATIVE_EXIT(env, that, gluPerspective_FUNC);
}
#endif

#ifndef NO_gluPickMatrix
JNIEXPORT void JNICALL GLU_NATIVE(gluPickMatrix)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	GLU_NATIVE_ENTER(env, that, gluPickMatrix_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	} else
#endif
	{
		if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	}
	gluPickMatrix(arg0, arg1, arg2, arg3, lparg4);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	} else
#endif
	{
		if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluPickMatrix_FUNC);
}
#endif

#ifndef NO_gluProject
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
	GLU_NATIVE_ENTER(env, that, gluProject_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
		if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
		if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
		if (arg8) lparg8 = (*env)->GetPrimitiveArrayCritical(env, arg8, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL);
		if (arg4) lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL);
		if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
		if (arg6) lparg6 = (*env)->GetDoubleArrayElements(env, arg6, NULL);
		if (arg7) lparg7 = (*env)->GetDoubleArrayElements(env, arg7, NULL);
		if (arg8) lparg8 = (*env)->GetDoubleArrayElements(env, arg8, NULL);
	}
	rc = (jint)gluProject(arg0, arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) (*env)->ReleasePrimitiveArrayCritical(env, arg8, lparg8, 0);
		if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, 0);
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
		if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg8) (*env)->ReleaseDoubleArrayElements(env, arg8, lparg8, 0);
		if (arg7) (*env)->ReleaseDoubleArrayElements(env, arg7, lparg7, 0);
		if (arg6) (*env)->ReleaseDoubleArrayElements(env, arg6, lparg6, 0);
		if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
		if (arg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
		if (arg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluProject_FUNC);
	return rc;
}
#endif

#ifndef NO_gluPwlCurve
JNIEXPORT void JNICALL GLU_NATIVE(gluPwlCurve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2, jint arg3, jint arg4)
{
	jfloat *lparg2=NULL;
	GLU_NATIVE_ENTER(env, that, gluPwlCurve_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	gluPwlCurve((GLUnurbs *)arg0, arg1, lparg2, arg3, arg4);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluPwlCurve_FUNC);
}
#endif

#ifndef NO_gluQuadricCallback
JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GLU_NATIVE_ENTER(env, that, gluQuadricCallback_FUNC);
	gluQuadricCallback((GLUquadricObj *)arg0, arg1, (GLvoid(*))arg2);
	GLU_NATIVE_EXIT(env, that, gluQuadricCallback_FUNC);
}
#endif

#ifndef NO_gluQuadricDrawStyle
JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricDrawStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GLU_NATIVE_ENTER(env, that, gluQuadricDrawStyle_FUNC);
	gluQuadricDrawStyle((GLUquadricObj *)arg0, arg1);
	GLU_NATIVE_EXIT(env, that, gluQuadricDrawStyle_FUNC);
}
#endif

#ifndef NO_gluQuadricNormals
JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricNormals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GLU_NATIVE_ENTER(env, that, gluQuadricNormals_FUNC);
	gluQuadricNormals((GLUquadricObj *)arg0, arg1);
	GLU_NATIVE_EXIT(env, that, gluQuadricNormals_FUNC);
}
#endif

#ifndef NO_gluQuadricOrientation
JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricOrientation)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GLU_NATIVE_ENTER(env, that, gluQuadricOrientation_FUNC);
	gluQuadricOrientation((GLUquadric *)arg0, arg1);
	GLU_NATIVE_EXIT(env, that, gluQuadricOrientation_FUNC);
}
#endif

#ifndef NO_gluQuadricTexture
JNIEXPORT void JNICALL GLU_NATIVE(gluQuadricTexture)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	GLU_NATIVE_ENTER(env, that, gluQuadricTexture_FUNC);
	gluQuadricTexture((GLUquadric *)arg0, arg1);
	GLU_NATIVE_EXIT(env, that, gluQuadricTexture_FUNC);
}
#endif

#ifndef NO_gluScaleImage
JNIEXPORT jint JNICALL GLU_NATIVE(gluScaleImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc;
	GLU_NATIVE_ENTER(env, that, gluScaleImage_FUNC);
	rc = (jint)gluScaleImage(arg0, arg1, arg2, arg3, (const void *)arg4, arg5, arg6, arg7, (void *)arg8);
	GLU_NATIVE_EXIT(env, that, gluScaleImage_FUNC);
	return rc;
}
#endif

#ifndef NO_gluSphere
JNIEXPORT void JNICALL GLU_NATIVE(gluSphere)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jint arg2, jint arg3)
{
	GLU_NATIVE_ENTER(env, that, gluSphere_FUNC);
	gluSphere((GLUquadric *)arg0, arg1, arg2, arg3);
	GLU_NATIVE_EXIT(env, that, gluSphere_FUNC);
}
#endif

#ifndef NO_gluTessBeginContour
JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluTessBeginContour_FUNC);
	gluTessBeginContour((GLUtesselator *)arg0);
	GLU_NATIVE_EXIT(env, that, gluTessBeginContour_FUNC);
}
#endif

#ifndef NO_gluTessBeginPolygon
JNIEXPORT void JNICALL GLU_NATIVE(gluTessBeginPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GLU_NATIVE_ENTER(env, that, gluTessBeginPolygon_FUNC);
	gluTessBeginPolygon((GLUtesselator *)arg0, (GLvoid *)arg1);
	GLU_NATIVE_EXIT(env, that, gluTessBeginPolygon_FUNC);
}
#endif

#ifndef NO_gluTessCallback
JNIEXPORT void JNICALL GLU_NATIVE(gluTessCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GLU_NATIVE_ENTER(env, that, gluTessCallback_FUNC);
	gluTessCallback((GLUtesselator *)arg0, arg1, (GLvoid(*))arg2);
	GLU_NATIVE_EXIT(env, that, gluTessCallback_FUNC);
}
#endif

#ifndef NO_gluTessEndContour
JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndContour)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluTessEndContour_FUNC);
	gluTessEndContour((GLUtesselator *)arg0);
	GLU_NATIVE_EXIT(env, that, gluTessEndContour_FUNC);
}
#endif

#ifndef NO_gluTessEndPolygon
JNIEXPORT void JNICALL GLU_NATIVE(gluTessEndPolygon)
	(JNIEnv *env, jclass that, jint arg0)
{
	GLU_NATIVE_ENTER(env, that, gluTessEndPolygon_FUNC);
	gluTessEndPolygon((GLUtesselator *)arg0);
	GLU_NATIVE_EXIT(env, that, gluTessEndPolygon_FUNC);
}
#endif

#ifndef NO_gluTessNormal
JNIEXPORT void JNICALL GLU_NATIVE(gluTessNormal)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GLU_NATIVE_ENTER(env, that, gluTessNormal_FUNC);
	gluTessNormal((GLUtesselator *)arg0, arg1, arg2, arg3);
	GLU_NATIVE_EXIT(env, that, gluTessNormal_FUNC);
}
#endif

#ifndef NO_gluTessProperty
JNIEXPORT void JNICALL GLU_NATIVE(gluTessProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	GLU_NATIVE_ENTER(env, that, gluTessProperty_FUNC);
	gluTessProperty((GLUtesselator *)arg0, arg1, arg2);
	GLU_NATIVE_EXIT(env, that, gluTessProperty_FUNC);
}
#endif

#ifndef NO_gluTessVertex
JNIEXPORT void JNICALL GLU_NATIVE(gluTessVertex)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1, jint arg2)
{
	jdouble *lparg1=NULL;
	GLU_NATIVE_ENTER(env, that, gluTessVertex_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	}
	gluTessVertex((GLUtesselator *)arg0, lparg1, (GLvoid *)arg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluTessVertex_FUNC);
}
#endif

#ifndef NO_gluUnProject
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
	GLU_NATIVE_ENTER(env, that, gluUnProject_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
		if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
		if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
		if (arg8) lparg8 = (*env)->GetPrimitiveArrayCritical(env, arg8, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetDoubleArrayElements(env, arg3, NULL);
		if (arg4) lparg4 = (*env)->GetDoubleArrayElements(env, arg4, NULL);
		if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
		if (arg6) lparg6 = (*env)->GetDoubleArrayElements(env, arg6, NULL);
		if (arg7) lparg7 = (*env)->GetDoubleArrayElements(env, arg7, NULL);
		if (arg8) lparg8 = (*env)->GetDoubleArrayElements(env, arg8, NULL);
	}
	rc = (jint)gluUnProject(arg0, arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) (*env)->ReleasePrimitiveArrayCritical(env, arg8, lparg8, 0);
		if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, 0);
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
		if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg8) (*env)->ReleaseDoubleArrayElements(env, arg8, lparg8, 0);
		if (arg7) (*env)->ReleaseDoubleArrayElements(env, arg7, lparg7, 0);
		if (arg6) (*env)->ReleaseDoubleArrayElements(env, arg6, lparg6, 0);
		if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
		if (arg4) (*env)->ReleaseDoubleArrayElements(env, arg4, lparg4, 0);
		if (arg3) (*env)->ReleaseDoubleArrayElements(env, arg3, lparg3, 0);
	}
	GLU_NATIVE_EXIT(env, that, gluUnProject_FUNC);
	return rc;
}
#endif


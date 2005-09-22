#include "swt.h"
#include "gl_structs.h"
#include "gl_stats.h"

#define GL_NATIVE(func) Java_org_eclipse_opengl_GL_##func

#ifndef NO_glAccum
JNIEXPORT void JNICALL GL_NATIVE(glAccum)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glAccum_FUNC);
	glAccum(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glAccum_FUNC);
}
#endif

#ifndef NO_glAlphaFunc
JNIEXPORT void JNICALL GL_NATIVE(glAlphaFunc)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glAlphaFunc_FUNC);
	glAlphaFunc(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glAlphaFunc_FUNC);
}
#endif

#ifndef NO_glAreTexturesResident
JNIEXPORT jboolean JNICALL GL_NATIVE(glAreTexturesResident)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jbooleanArray arg2)
{
	jint *lparg1=NULL;
	jboolean *lparg2=NULL;
	jboolean rc;
	GL_NATIVE_ENTER(env, that, glAreTexturesResident_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetBooleanArrayElements(env, arg2, NULL);
	}
	rc = (jboolean)glAreTexturesResident(arg0, (const GLuint *)lparg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseBooleanArrayElements(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glAreTexturesResident_FUNC);
	return rc;
}
#endif

#ifndef NO_glArrayElement
JNIEXPORT void JNICALL GL_NATIVE(glArrayElement)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glArrayElement_FUNC);
	glArrayElement(arg0);
	GL_NATIVE_EXIT(env, that, glArrayElement_FUNC);
}
#endif

#ifndef NO_glBegin
JNIEXPORT void JNICALL GL_NATIVE(glBegin)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glBegin_FUNC);
	glBegin(arg0);
	GL_NATIVE_EXIT(env, that, glBegin_FUNC);
}
#endif

#ifndef NO_glBindTexture
JNIEXPORT void JNICALL GL_NATIVE(glBindTexture)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glBindTexture_FUNC);
	glBindTexture(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glBindTexture_FUNC);
}
#endif

#ifndef NO_glBitmap
JNIEXPORT void JNICALL GL_NATIVE(glBitmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5, jbyteArray arg6)
{
	jbyte *lparg6=NULL;
	GL_NATIVE_ENTER(env, that, glBitmap_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
	} else
#endif
	{
		if (arg6) lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL);
	}
	glBitmap(arg0, arg1, arg2, arg3, arg4, arg5, (const GLubyte *)lparg6);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
	} else
#endif
	{
		if (arg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	}
	GL_NATIVE_EXIT(env, that, glBitmap_FUNC);
}
#endif

#ifndef NO_glBlendFunc
JNIEXPORT void JNICALL GL_NATIVE(glBlendFunc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glBlendFunc_FUNC);
	glBlendFunc(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glBlendFunc_FUNC);
}
#endif

#ifndef NO_glCallList
JNIEXPORT void JNICALL GL_NATIVE(glCallList)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glCallList_FUNC);
	glCallList(arg0);
	GL_NATIVE_EXIT(env, that, glCallList_FUNC);
}
#endif

#ifndef NO_glCallLists__II_3B
JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glCallLists__II_3B_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	}
	glCallLists(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glCallLists__II_3B_FUNC);
}
#endif

#ifndef NO_glCallLists__II_3C
JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glCallLists__II_3C_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL);
	}
	glCallLists(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glCallLists__II_3C_FUNC);
}
#endif

#ifndef NO_glCallLists__II_3I
JNIEXPORT void JNICALL GL_NATIVE(glCallLists__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glCallLists__II_3I_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glCallLists(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glCallLists__II_3I_FUNC);
}
#endif

#ifndef NO_glClear
JNIEXPORT void JNICALL GL_NATIVE(glClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glClear_FUNC);
	glClear(arg0);
	GL_NATIVE_EXIT(env, that, glClear_FUNC);
}
#endif

#ifndef NO_glClearAccum
JNIEXPORT void JNICALL GL_NATIVE(glClearAccum)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glClearAccum_FUNC);
	glClearAccum(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glClearAccum_FUNC);
}
#endif

#ifndef NO_glClearColor
JNIEXPORT void JNICALL GL_NATIVE(glClearColor)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glClearColor_FUNC);
	glClearColor(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glClearColor_FUNC);
}
#endif

#ifndef NO_glClearDepth
JNIEXPORT void JNICALL GL_NATIVE(glClearDepth)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	GL_NATIVE_ENTER(env, that, glClearDepth_FUNC);
	glClearDepth(arg0);
	GL_NATIVE_EXIT(env, that, glClearDepth_FUNC);
}
#endif

#ifndef NO_glClearIndex
JNIEXPORT void JNICALL GL_NATIVE(glClearIndex)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glClearIndex_FUNC);
	glClearIndex(arg0);
	GL_NATIVE_EXIT(env, that, glClearIndex_FUNC);
}
#endif

#ifndef NO_glClearStencil
JNIEXPORT void JNICALL GL_NATIVE(glClearStencil)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glClearStencil_FUNC);
	glClearStencil(arg0);
	GL_NATIVE_EXIT(env, that, glClearStencil_FUNC);
}
#endif

#ifndef NO_glClipPlane
JNIEXPORT void JNICALL GL_NATIVE(glClipPlane)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glClipPlane_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	}
	glClipPlane(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glClipPlane_FUNC);
}
#endif

#ifndef NO_glColor3b
JNIEXPORT void JNICALL GL_NATIVE(glColor3b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3b_FUNC);
	glColor3b(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3b_FUNC);
}
#endif

#ifndef NO_glColor3bv
JNIEXPORT void JNICALL GL_NATIVE(glColor3bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3bv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glColor3bv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3bv_FUNC);
}
#endif

#ifndef NO_glColor3d
JNIEXPORT void JNICALL GL_NATIVE(glColor3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3d_FUNC);
	glColor3d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3d_FUNC);
}
#endif

#ifndef NO_glColor3dv
JNIEXPORT void JNICALL GL_NATIVE(glColor3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glColor3dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3dv_FUNC);
}
#endif

#ifndef NO_glColor3f
JNIEXPORT void JNICALL GL_NATIVE(glColor3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3f_FUNC);
	glColor3f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3f_FUNC);
}
#endif

#ifndef NO_glColor3fv
JNIEXPORT void JNICALL GL_NATIVE(glColor3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glColor3fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3fv_FUNC);
}
#endif

#ifndef NO_glColor3i
JNIEXPORT void JNICALL GL_NATIVE(glColor3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3i_FUNC);
	glColor3i(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3i_FUNC);
}
#endif

#ifndef NO_glColor3iv
JNIEXPORT void JNICALL GL_NATIVE(glColor3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glColor3iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3iv_FUNC);
}
#endif

#ifndef NO_glColor3s
JNIEXPORT void JNICALL GL_NATIVE(glColor3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3s_FUNC);
	glColor3s(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3s_FUNC);
}
#endif

#ifndef NO_glColor3sv
JNIEXPORT void JNICALL GL_NATIVE(glColor3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glColor3sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3sv_FUNC);
}
#endif

#ifndef NO_glColor3ub
JNIEXPORT void JNICALL GL_NATIVE(glColor3ub)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3ub_FUNC);
	glColor3ub(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3ub_FUNC);
}
#endif

#ifndef NO_glColor3ubv
JNIEXPORT void JNICALL GL_NATIVE(glColor3ubv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3ubv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glColor3ubv((const GLubyte *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3ubv_FUNC);
}
#endif

#ifndef NO_glColor3ui
JNIEXPORT void JNICALL GL_NATIVE(glColor3ui)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3ui_FUNC);
	glColor3ui(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3ui_FUNC);
}
#endif

#ifndef NO_glColor3uiv
JNIEXPORT void JNICALL GL_NATIVE(glColor3uiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3uiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glColor3uiv((const GLuint *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3uiv_FUNC);
}
#endif

#ifndef NO_glColor3us
JNIEXPORT void JNICALL GL_NATIVE(glColor3us)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glColor3us_FUNC);
	glColor3us(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glColor3us_FUNC);
}
#endif

#ifndef NO_glColor3usv
JNIEXPORT void JNICALL GL_NATIVE(glColor3usv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor3usv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glColor3usv((const GLushort *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor3usv_FUNC);
}
#endif

#ifndef NO_glColor4b
JNIEXPORT void JNICALL GL_NATIVE(glColor4b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4b_FUNC);
	glColor4b(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4b_FUNC);
}
#endif

#ifndef NO_glColor4bv
JNIEXPORT void JNICALL GL_NATIVE(glColor4bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4bv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glColor4bv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4bv_FUNC);
}
#endif

#ifndef NO_glColor4d
JNIEXPORT void JNICALL GL_NATIVE(glColor4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4d_FUNC);
	glColor4d(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4d_FUNC);
}
#endif

#ifndef NO_glColor4dv
JNIEXPORT void JNICALL GL_NATIVE(glColor4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glColor4dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4dv_FUNC);
}
#endif

#ifndef NO_glColor4f
JNIEXPORT void JNICALL GL_NATIVE(glColor4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4f_FUNC);
	glColor4f(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4f_FUNC);
}
#endif

#ifndef NO_glColor4fv
JNIEXPORT void JNICALL GL_NATIVE(glColor4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glColor4fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4fv_FUNC);
}
#endif

#ifndef NO_glColor4i
JNIEXPORT void JNICALL GL_NATIVE(glColor4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4i_FUNC);
	glColor4i(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4i_FUNC);
}
#endif

#ifndef NO_glColor4iv
JNIEXPORT void JNICALL GL_NATIVE(glColor4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glColor4iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4iv_FUNC);
}
#endif

#ifndef NO_glColor4s
JNIEXPORT void JNICALL GL_NATIVE(glColor4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4s_FUNC);
	glColor4s(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4s_FUNC);
}
#endif

#ifndef NO_glColor4ub
JNIEXPORT void JNICALL GL_NATIVE(glColor4ub)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2, jbyte arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4ub_FUNC);
	glColor4ub(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4ub_FUNC);
}
#endif

#ifndef NO_glColor4ubv
JNIEXPORT void JNICALL GL_NATIVE(glColor4ubv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4ubv_FUNC);
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	glColor4ubv((const GLubyte *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GL_NATIVE_EXIT(env, that, glColor4ubv_FUNC);
}
#endif

#ifndef NO_glColor4ui
JNIEXPORT void JNICALL GL_NATIVE(glColor4ui)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4ui_FUNC);
	glColor4ui(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4ui_FUNC);
}
#endif

#ifndef NO_glColor4uiv
JNIEXPORT void JNICALL GL_NATIVE(glColor4uiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4uiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glColor4uiv((const GLuint *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4uiv_FUNC);
}
#endif

#ifndef NO_glColor4us
JNIEXPORT void JNICALL GL_NATIVE(glColor4us)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glColor4us_FUNC);
	glColor4us(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColor4us_FUNC);
}
#endif

#ifndef NO_glColor4usv
JNIEXPORT void JNICALL GL_NATIVE(glColor4usv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glColor4usv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glColor4usv((const GLushort *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glColor4usv_FUNC);
}
#endif

#ifndef NO_glColorMask
JNIEXPORT void JNICALL GL_NATIVE(glColorMask)
	(JNIEnv *env, jclass that, jboolean arg0, jboolean arg1, jboolean arg2, jboolean arg3)
{
	GL_NATIVE_ENTER(env, that, glColorMask_FUNC);
	glColorMask(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glColorMask_FUNC);
}
#endif

#ifndef NO_glColorMaterial
JNIEXPORT void JNICALL GL_NATIVE(glColorMaterial)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glColorMaterial_FUNC);
	glColorMaterial(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glColorMaterial_FUNC);
}
#endif

#ifndef NO_glColorPointer
JNIEXPORT void JNICALL GL_NATIVE(glColorPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glColorPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	glColorPointer(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glColorPointer_FUNC);
}
#endif

#ifndef NO_glCopyPixels
JNIEXPORT void JNICALL GL_NATIVE(glCopyPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	GL_NATIVE_ENTER(env, that, glCopyPixels_FUNC);
	glCopyPixels(arg0, arg1, arg2, arg3, arg4);
	GL_NATIVE_EXIT(env, that, glCopyPixels_FUNC);
}
#endif

#ifndef NO_glCopyTexImage1D
JNIEXPORT void JNICALL GL_NATIVE(glCopyTexImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	GL_NATIVE_ENTER(env, that, glCopyTexImage1D_FUNC);
	glCopyTexImage1D(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	GL_NATIVE_EXIT(env, that, glCopyTexImage1D_FUNC);
}
#endif

#ifndef NO_glCopyTexImage2D
JNIEXPORT void JNICALL GL_NATIVE(glCopyTexImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	GL_NATIVE_ENTER(env, that, glCopyTexImage2D_FUNC);
	glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	GL_NATIVE_EXIT(env, that, glCopyTexImage2D_FUNC);
}
#endif

#ifndef NO_glCopyTexSubImage1D
JNIEXPORT void JNICALL GL_NATIVE(glCopyTexSubImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	GL_NATIVE_ENTER(env, that, glCopyTexSubImage1D_FUNC);
	glCopyTexSubImage1D(arg0, arg1, arg2, arg3, arg4, arg5);
	GL_NATIVE_EXIT(env, that, glCopyTexSubImage1D_FUNC);
}
#endif

#ifndef NO_glCopyTexSubImage2D
JNIEXPORT void JNICALL GL_NATIVE(glCopyTexSubImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	GL_NATIVE_ENTER(env, that, glCopyTexSubImage2D_FUNC);
	glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	GL_NATIVE_EXIT(env, that, glCopyTexSubImage2D_FUNC);
}
#endif

#ifndef NO_glCullFace
JNIEXPORT void JNICALL GL_NATIVE(glCullFace)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glCullFace_FUNC);
	glCullFace(arg0);
	GL_NATIVE_EXIT(env, that, glCullFace_FUNC);
}
#endif

#ifndef NO_glDeleteLists
JNIEXPORT void JNICALL GL_NATIVE(glDeleteLists)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glDeleteLists_FUNC);
	glDeleteLists(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glDeleteLists_FUNC);
}
#endif

#ifndef NO_glDeleteTextures
JNIEXPORT void JNICALL GL_NATIVE(glDeleteTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glDeleteTextures_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glDeleteTextures(arg0, (const GLuint *)lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glDeleteTextures_FUNC);
}
#endif

#ifndef NO_glDepthFunc
JNIEXPORT void JNICALL GL_NATIVE(glDepthFunc)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glDepthFunc_FUNC);
	glDepthFunc(arg0);
	GL_NATIVE_EXIT(env, that, glDepthFunc_FUNC);
}
#endif

#ifndef NO_glDepthMask
JNIEXPORT void JNICALL GL_NATIVE(glDepthMask)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	GL_NATIVE_ENTER(env, that, glDepthMask_FUNC);
	glDepthMask(arg0);
	GL_NATIVE_EXIT(env, that, glDepthMask_FUNC);
}
#endif

#ifndef NO_glDepthRange
JNIEXPORT void JNICALL GL_NATIVE(glDepthRange)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	GL_NATIVE_ENTER(env, that, glDepthRange_FUNC);
	glDepthRange(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glDepthRange_FUNC);
}
#endif

#ifndef NO_glDisable
JNIEXPORT void JNICALL GL_NATIVE(glDisable)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glDisable_FUNC);
	glDisable(arg0);
	GL_NATIVE_EXIT(env, that, glDisable_FUNC);
}
#endif

#ifndef NO_glDisableClientState
JNIEXPORT void JNICALL GL_NATIVE(glDisableClientState)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glDisableClientState_FUNC);
	glDisableClientState(arg0);
	GL_NATIVE_EXIT(env, that, glDisableClientState_FUNC);
}
#endif

#ifndef NO_glDrawArrays
JNIEXPORT void JNICALL GL_NATIVE(glDrawArrays)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glDrawArrays_FUNC);
	glDrawArrays(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glDrawArrays_FUNC);
}
#endif

#ifndef NO_glDrawBuffer
JNIEXPORT void JNICALL GL_NATIVE(glDrawBuffer)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glDrawBuffer_FUNC);
	glDrawBuffer(arg0);
	GL_NATIVE_EXIT(env, that, glDrawBuffer_FUNC);
}
#endif

#ifndef NO_glDrawElements
JNIEXPORT void JNICALL GL_NATIVE(glDrawElements)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glDrawElements_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	glDrawElements(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glDrawElements_FUNC);
}
#endif

#ifndef NO_glDrawPixels
JNIEXPORT void JNICALL GL_NATIVE(glDrawPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	GL_NATIVE_ENTER(env, that, glDrawPixels_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	} else
#endif
	{
		if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	}
	glDrawPixels(arg0, arg1, arg2, arg3, lparg4);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	} else
#endif
	{
		if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	}
	GL_NATIVE_EXIT(env, that, glDrawPixels_FUNC);
}
#endif

#ifndef NO_glEdgeFlag
JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlag)
	(JNIEnv *env, jclass that, jboolean arg0)
{
	GL_NATIVE_ENTER(env, that, glEdgeFlag_FUNC);
	glEdgeFlag(arg0);
	GL_NATIVE_EXIT(env, that, glEdgeFlag_FUNC);
}
#endif

#ifndef NO_glEdgeFlagPointer
JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlagPointer)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glEdgeFlagPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL);
	}
	glEdgeFlagPointer(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glEdgeFlagPointer_FUNC);
}
#endif

#ifndef NO_glEdgeFlagv
JNIEXPORT void JNICALL GL_NATIVE(glEdgeFlagv)
	(JNIEnv *env, jclass that, jbooleanArray arg0)
{
	jboolean *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glEdgeFlagv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetBooleanArrayElements(env, arg0, NULL);
	}
	glEdgeFlagv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseBooleanArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glEdgeFlagv_FUNC);
}
#endif

#ifndef NO_glEnable
JNIEXPORT void JNICALL GL_NATIVE(glEnable)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glEnable_FUNC);
	glEnable(arg0);
	GL_NATIVE_EXIT(env, that, glEnable_FUNC);
}
#endif

#ifndef NO_glEnableClientState
JNIEXPORT void JNICALL GL_NATIVE(glEnableClientState)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glEnableClientState_FUNC);
	glEnableClientState(arg0);
	GL_NATIVE_EXIT(env, that, glEnableClientState_FUNC);
}
#endif

#ifndef NO_glEnd
JNIEXPORT void JNICALL GL_NATIVE(glEnd)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glEnd_FUNC);
	glEnd();
	GL_NATIVE_EXIT(env, that, glEnd_FUNC);
}
#endif

#ifndef NO_glEndList
JNIEXPORT void JNICALL GL_NATIVE(glEndList)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glEndList_FUNC);
	glEndList();
	GL_NATIVE_EXIT(env, that, glEndList_FUNC);
}
#endif

#ifndef NO_glEvalCoord1d
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1d)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	GL_NATIVE_ENTER(env, that, glEvalCoord1d_FUNC);
	glEvalCoord1d(arg0);
	GL_NATIVE_EXIT(env, that, glEvalCoord1d_FUNC);
}
#endif

#ifndef NO_glEvalCoord1dv
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glEvalCoord1dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glEvalCoord1dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glEvalCoord1dv_FUNC);
}
#endif

#ifndef NO_glEvalCoord1f
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1f)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glEvalCoord1f_FUNC);
	glEvalCoord1f(arg0);
	GL_NATIVE_EXIT(env, that, glEvalCoord1f_FUNC);
}
#endif

#ifndef NO_glEvalCoord1fv
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord1fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glEvalCoord1fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glEvalCoord1fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glEvalCoord1fv_FUNC);
}
#endif

#ifndef NO_glEvalCoord2d
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	GL_NATIVE_ENTER(env, that, glEvalCoord2d_FUNC);
	glEvalCoord2d(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glEvalCoord2d_FUNC);
}
#endif

#ifndef NO_glEvalCoord2dv
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glEvalCoord2dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glEvalCoord2dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glEvalCoord2dv_FUNC);
}
#endif

#ifndef NO_glEvalCoord2f
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glEvalCoord2f_FUNC);
	glEvalCoord2f(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glEvalCoord2f_FUNC);
}
#endif

#ifndef NO_glEvalCoord2fv
JNIEXPORT void JNICALL GL_NATIVE(glEvalCoord2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glEvalCoord2fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glEvalCoord2fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glEvalCoord2fv_FUNC);
}
#endif

#ifndef NO_glEvalMesh1
JNIEXPORT void JNICALL GL_NATIVE(glEvalMesh1)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glEvalMesh1_FUNC);
	glEvalMesh1(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glEvalMesh1_FUNC);
}
#endif

#ifndef NO_glEvalMesh2
JNIEXPORT void JNICALL GL_NATIVE(glEvalMesh2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	GL_NATIVE_ENTER(env, that, glEvalMesh2_FUNC);
	glEvalMesh2(arg0, arg1, arg2, arg3, arg4);
	GL_NATIVE_EXIT(env, that, glEvalMesh2_FUNC);
}
#endif

#ifndef NO_glEvalPoint1
JNIEXPORT void JNICALL GL_NATIVE(glEvalPoint1)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glEvalPoint1_FUNC);
	glEvalPoint1(arg0);
	GL_NATIVE_EXIT(env, that, glEvalPoint1_FUNC);
}
#endif

#ifndef NO_glEvalPoint2
JNIEXPORT void JNICALL GL_NATIVE(glEvalPoint2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glEvalPoint2_FUNC);
	glEvalPoint2(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glEvalPoint2_FUNC);
}
#endif

#ifndef NO_glFeedbackBuffer
JNIEXPORT void JNICALL GL_NATIVE(glFeedbackBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glFeedbackBuffer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glFeedbackBuffer(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glFeedbackBuffer_FUNC);
}
#endif

#ifndef NO_glFinish
JNIEXPORT void JNICALL GL_NATIVE(glFinish)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glFinish_FUNC);
	glFinish();
	GL_NATIVE_EXIT(env, that, glFinish_FUNC);
}
#endif

#ifndef NO_glFlush
JNIEXPORT void JNICALL GL_NATIVE(glFlush)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glFlush_FUNC);
	glFlush();
	GL_NATIVE_EXIT(env, that, glFlush_FUNC);
}
#endif

#ifndef NO_glFogf
JNIEXPORT void JNICALL GL_NATIVE(glFogf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glFogf_FUNC);
	glFogf(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glFogf_FUNC);
}
#endif

#ifndef NO_glFogfv
JNIEXPORT void JNICALL GL_NATIVE(glFogfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glFogfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	}
	glFogfv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glFogfv_FUNC);
}
#endif

#ifndef NO_glFogi
JNIEXPORT void JNICALL GL_NATIVE(glFogi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glFogi_FUNC);
	glFogi(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glFogi_FUNC);
}
#endif

#ifndef NO_glFogiv
JNIEXPORT void JNICALL GL_NATIVE(glFogiv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glFogiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glFogiv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glFogiv_FUNC);
}
#endif

#ifndef NO_glFrontFace
JNIEXPORT void JNICALL GL_NATIVE(glFrontFace)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glFrontFace_FUNC);
	glFrontFace(arg0);
	GL_NATIVE_EXIT(env, that, glFrontFace_FUNC);
}
#endif

#ifndef NO_glFrustum
JNIEXPORT void JNICALL GL_NATIVE(glFrustum)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	GL_NATIVE_ENTER(env, that, glFrustum_FUNC);
	glFrustum(arg0, arg1, arg2, arg3, arg4, arg5);
	GL_NATIVE_EXIT(env, that, glFrustum_FUNC);
}
#endif

#ifndef NO_glGenLists
JNIEXPORT jint JNICALL GL_NATIVE(glGenLists)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	GL_NATIVE_ENTER(env, that, glGenLists_FUNC);
	rc = (jint)glGenLists(arg0);
	GL_NATIVE_EXIT(env, that, glGenLists_FUNC);
	return rc;
}
#endif

#ifndef NO_glGenTextures
JNIEXPORT void JNICALL GL_NATIVE(glGenTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGenTextures_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glGenTextures(arg0, (GLuint *)lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGenTextures_FUNC);
}
#endif

#ifndef NO_glGetBooleanv
JNIEXPORT void JNICALL GL_NATIVE(glGetBooleanv)
	(JNIEnv *env, jclass that, jint arg0, jbooleanArray arg1)
{
	jboolean *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetBooleanv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetBooleanArrayElements(env, arg1, NULL);
	}
	glGetBooleanv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseBooleanArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetBooleanv_FUNC);
}
#endif

#ifndef NO_glGetClipPlane
JNIEXPORT void JNICALL GL_NATIVE(glGetClipPlane)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetClipPlane_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	}
	glGetClipPlane(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetClipPlane_FUNC);
}
#endif

#ifndef NO_glGetDoublev
JNIEXPORT void JNICALL GL_NATIVE(glGetDoublev)
	(JNIEnv *env, jclass that, jint arg0, jdoubleArray arg1)
{
	jdouble *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetDoublev_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	}
	glGetDoublev(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetDoublev_FUNC);
}
#endif

#ifndef NO_glGetError
JNIEXPORT jint JNICALL GL_NATIVE(glGetError)
	(JNIEnv *env, jclass that)
{
	jint rc;
	GL_NATIVE_ENTER(env, that, glGetError_FUNC);
	rc = (jint)glGetError();
	GL_NATIVE_EXIT(env, that, glGetError_FUNC);
	return rc;
}
#endif

#ifndef NO_glGetFloatv
JNIEXPORT void JNICALL GL_NATIVE(glGetFloatv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetFloatv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	}
	glGetFloatv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetFloatv_FUNC);
}
#endif

#ifndef NO_glGetIntegerv
JNIEXPORT void JNICALL GL_NATIVE(glGetIntegerv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetIntegerv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glGetIntegerv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetIntegerv_FUNC);
}
#endif

#ifndef NO_glGetLightfv
JNIEXPORT void JNICALL GL_NATIVE(glGetLightfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetLightfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetLightfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetLightfv_FUNC);
}
#endif

#ifndef NO_glGetLightiv
JNIEXPORT void JNICALL GL_NATIVE(glGetLightiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetLightiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetLightiv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetLightiv_FUNC);
}
#endif

#ifndef NO_glGetMapdv
JNIEXPORT void JNICALL GL_NATIVE(glGetMapdv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetMapdv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	}
	glGetMapdv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetMapdv_FUNC);
}
#endif

#ifndef NO_glGetMapfv
JNIEXPORT void JNICALL GL_NATIVE(glGetMapfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetMapfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetMapfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetMapfv_FUNC);
}
#endif

#ifndef NO_glGetMapiv
JNIEXPORT void JNICALL GL_NATIVE(glGetMapiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetMapiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetMapiv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetMapiv_FUNC);
}
#endif

#ifndef NO_glGetMaterialfv
JNIEXPORT void JNICALL GL_NATIVE(glGetMaterialfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetMaterialfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetMaterialfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetMaterialfv_FUNC);
}
#endif

#ifndef NO_glGetMaterialiv
JNIEXPORT void JNICALL GL_NATIVE(glGetMaterialiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetMaterialiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetMaterialiv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetMaterialiv_FUNC);
}
#endif

#ifndef NO_glGetPixelMapfv
JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetPixelMapfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	}
	glGetPixelMapfv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetPixelMapfv_FUNC);
}
#endif

#ifndef NO_glGetPixelMapuiv
JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapuiv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetPixelMapuiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glGetPixelMapuiv(arg0, (GLuint *)lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetPixelMapuiv_FUNC);
}
#endif

#ifndef NO_glGetPixelMapusv
JNIEXPORT void JNICALL GL_NATIVE(glGetPixelMapusv)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1)
{
	jshort *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetPixelMapusv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	}
	glGetPixelMapusv(arg0, (GLushort *)lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetPixelMapusv_FUNC);
}
#endif

#ifndef NO_glGetPointerv
JNIEXPORT void JNICALL GL_NATIVE(glGetPointerv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glGetPointerv_FUNC);
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	glGetPointerv(arg0, (GLvoid **)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	GL_NATIVE_EXIT(env, that, glGetPointerv_FUNC);
}
#endif

#ifndef NO_glGetPolygonStipple
JNIEXPORT void JNICALL GL_NATIVE(glGetPolygonStipple)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glGetPolygonStipple_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glGetPolygonStipple((GLubyte *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetPolygonStipple_FUNC);
}
#endif

#ifndef NO_glGetString
JNIEXPORT jint JNICALL GL_NATIVE(glGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	GL_NATIVE_ENTER(env, that, glGetString_FUNC);
	rc = (jint)glGetString(arg0);
	GL_NATIVE_EXIT(env, that, glGetString_FUNC);
	return rc;
}
#endif

#ifndef NO_glGetTexEnvfv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexEnvfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexEnvfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetTexEnvfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexEnvfv_FUNC);
}
#endif

#ifndef NO_glGetTexEnviv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexEnviv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexEnviv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetTexEnviv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexEnviv_FUNC);
}
#endif

#ifndef NO_glGetTexGendv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexGendv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexGendv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	}
	glGetTexGendv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexGendv_FUNC);
}
#endif

#ifndef NO_glGetTexGenfv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexGenfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexGenfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetTexGenfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexGenfv_FUNC);
}
#endif

#ifndef NO_glGetTexGeniv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexGeniv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexGeniv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetTexGeniv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexGeniv_FUNC);
}
#endif

#ifndef NO_glGetTexImage
JNIEXPORT void JNICALL GL_NATIVE(glGetTexImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexImage_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) lparg4 = (*env)->GetPrimitiveArrayCritical(env, arg4, NULL);
	} else
#endif
	{
		if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	}
	glGetTexImage(arg0, arg1, arg2, arg3, lparg4);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg4) (*env)->ReleasePrimitiveArrayCritical(env, arg4, lparg4, 0);
	} else
#endif
	{
		if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexImage_FUNC);
}
#endif

#ifndef NO_glGetTexLevelParameterfv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexLevelParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloatArray arg3)
{
	jfloat *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexLevelParameterfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetFloatArrayElements(env, arg3, NULL);
	}
	glGetTexLevelParameterfv(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseFloatArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexLevelParameterfv_FUNC);
}
#endif

#ifndef NO_glGetTexLevelParameteriv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexLevelParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexLevelParameteriv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	glGetTexLevelParameteriv(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexLevelParameteriv_FUNC);
}
#endif

#ifndef NO_glGetTexParameterfv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexParameterfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glGetTexParameterfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexParameterfv_FUNC);
}
#endif

#ifndef NO_glGetTexParameteriv
JNIEXPORT void JNICALL GL_NATIVE(glGetTexParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glGetTexParameteriv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glGetTexParameteriv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glGetTexParameteriv_FUNC);
}
#endif

#ifndef NO_glHint
JNIEXPORT void JNICALL GL_NATIVE(glHint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glHint_FUNC);
	glHint(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glHint_FUNC);
}
#endif

#ifndef NO_glIndexMask
JNIEXPORT void JNICALL GL_NATIVE(glIndexMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glIndexMask_FUNC);
	glIndexMask(arg0);
	GL_NATIVE_EXIT(env, that, glIndexMask_FUNC);
}
#endif

#ifndef NO_glIndexPointer
JNIEXPORT void JNICALL GL_NATIVE(glIndexPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glIndexPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glIndexPointer(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glIndexPointer_FUNC);
}
#endif

#ifndef NO_glIndexd
JNIEXPORT void JNICALL GL_NATIVE(glIndexd)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	GL_NATIVE_ENTER(env, that, glIndexd_FUNC);
	glIndexd(arg0);
	GL_NATIVE_EXIT(env, that, glIndexd_FUNC);
}
#endif

#ifndef NO_glIndexdv
JNIEXPORT void JNICALL GL_NATIVE(glIndexdv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glIndexdv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glIndexdv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glIndexdv_FUNC);
}
#endif

#ifndef NO_glIndexf
JNIEXPORT void JNICALL GL_NATIVE(glIndexf)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glIndexf_FUNC);
	glIndexf(arg0);
	GL_NATIVE_EXIT(env, that, glIndexf_FUNC);
}
#endif

#ifndef NO_glIndexfv
JNIEXPORT void JNICALL GL_NATIVE(glIndexfv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glIndexfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glIndexfv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glIndexfv_FUNC);
}
#endif

#ifndef NO_glIndexi
JNIEXPORT void JNICALL GL_NATIVE(glIndexi)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glIndexi_FUNC);
	glIndexi(arg0);
	GL_NATIVE_EXIT(env, that, glIndexi_FUNC);
}
#endif

#ifndef NO_glIndexiv
JNIEXPORT void JNICALL GL_NATIVE(glIndexiv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glIndexiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glIndexiv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glIndexiv_FUNC);
}
#endif

#ifndef NO_glIndexs
JNIEXPORT void JNICALL GL_NATIVE(glIndexs)
	(JNIEnv *env, jclass that, jshort arg0)
{
	GL_NATIVE_ENTER(env, that, glIndexs_FUNC);
	glIndexs(arg0);
	GL_NATIVE_EXIT(env, that, glIndexs_FUNC);
}
#endif

#ifndef NO_glIndexsv
JNIEXPORT void JNICALL GL_NATIVE(glIndexsv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glIndexsv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glIndexsv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glIndexsv_FUNC);
}
#endif

#ifndef NO_glInitNames
JNIEXPORT void JNICALL GL_NATIVE(glInitNames)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glInitNames_FUNC);
	glInitNames();
	GL_NATIVE_EXIT(env, that, glInitNames_FUNC);
}
#endif

#ifndef NO_glInterleavedArrays
JNIEXPORT void JNICALL GL_NATIVE(glInterleavedArrays)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glInterleavedArrays_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glInterleavedArrays(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glInterleavedArrays_FUNC);
}
#endif

#ifndef NO_glIsEnabled
JNIEXPORT jboolean JNICALL GL_NATIVE(glIsEnabled)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	GL_NATIVE_ENTER(env, that, glIsEnabled_FUNC);
	rc = (jboolean)glIsEnabled(arg0);
	GL_NATIVE_EXIT(env, that, glIsEnabled_FUNC);
	return rc;
}
#endif

#ifndef NO_glIsList
JNIEXPORT jboolean JNICALL GL_NATIVE(glIsList)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	GL_NATIVE_ENTER(env, that, glIsList_FUNC);
	rc = (jboolean)glIsList(arg0);
	GL_NATIVE_EXIT(env, that, glIsList_FUNC);
	return rc;
}
#endif

#ifndef NO_glIsTexture
JNIEXPORT jboolean JNICALL GL_NATIVE(glIsTexture)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	GL_NATIVE_ENTER(env, that, glIsTexture_FUNC);
	rc = (jboolean)glIsTexture(arg0);
	GL_NATIVE_EXIT(env, that, glIsTexture_FUNC);
	return rc;
}
#endif

#ifndef NO_glLightModelf
JNIEXPORT void JNICALL GL_NATIVE(glLightModelf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glLightModelf_FUNC);
	glLightModelf(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glLightModelf_FUNC);
}
#endif

#ifndef NO_glLightModelfv
JNIEXPORT void JNICALL GL_NATIVE(glLightModelfv)
	(JNIEnv *env, jclass that, jint arg0, jfloatArray arg1)
{
	jfloat *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glLightModelfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	}
	glLightModelfv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glLightModelfv_FUNC);
}
#endif

#ifndef NO_glLightModeli
JNIEXPORT void JNICALL GL_NATIVE(glLightModeli)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glLightModeli_FUNC);
	glLightModeli(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glLightModeli_FUNC);
}
#endif

#ifndef NO_glLightModeliv
JNIEXPORT void JNICALL GL_NATIVE(glLightModeliv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glLightModeliv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glLightModeliv(arg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glLightModeliv_FUNC);
}
#endif

#ifndef NO_glLightf
JNIEXPORT void JNICALL GL_NATIVE(glLightf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glLightf_FUNC);
	glLightf(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glLightf_FUNC);
}
#endif

#ifndef NO_glLightfv
JNIEXPORT void JNICALL GL_NATIVE(glLightfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glLightfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glLightfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glLightfv_FUNC);
}
#endif

#ifndef NO_glLighti
JNIEXPORT void JNICALL GL_NATIVE(glLighti)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glLighti_FUNC);
	glLighti(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glLighti_FUNC);
}
#endif

#ifndef NO_glLightiv
JNIEXPORT void JNICALL GL_NATIVE(glLightiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glLightiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glLightiv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glLightiv_FUNC);
}
#endif

#ifndef NO_glLineStipple
JNIEXPORT void JNICALL GL_NATIVE(glLineStipple)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1)
{
	GL_NATIVE_ENTER(env, that, glLineStipple_FUNC);
	glLineStipple(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glLineStipple_FUNC);
}
#endif

#ifndef NO_glLineWidth
JNIEXPORT void JNICALL GL_NATIVE(glLineWidth)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glLineWidth_FUNC);
	glLineWidth(arg0);
	GL_NATIVE_EXIT(env, that, glLineWidth_FUNC);
}
#endif

#ifndef NO_glListBase
JNIEXPORT void JNICALL GL_NATIVE(glListBase)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glListBase_FUNC);
	glListBase(arg0);
	GL_NATIVE_EXIT(env, that, glListBase_FUNC);
}
#endif

#ifndef NO_glLoadIdentity
JNIEXPORT void JNICALL GL_NATIVE(glLoadIdentity)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glLoadIdentity_FUNC);
	glLoadIdentity();
	GL_NATIVE_EXIT(env, that, glLoadIdentity_FUNC);
}
#endif

#ifndef NO_glLoadMatrixd
JNIEXPORT void JNICALL GL_NATIVE(glLoadMatrixd)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glLoadMatrixd_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glLoadMatrixd(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glLoadMatrixd_FUNC);
}
#endif

#ifndef NO_glLoadMatrixf
JNIEXPORT void JNICALL GL_NATIVE(glLoadMatrixf)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glLoadMatrixf_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glLoadMatrixf(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glLoadMatrixf_FUNC);
}
#endif

#ifndef NO_glLoadName
JNIEXPORT void JNICALL GL_NATIVE(glLoadName)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glLoadName_FUNC);
	glLoadName(arg0);
	GL_NATIVE_EXIT(env, that, glLoadName_FUNC);
}
#endif

#ifndef NO_glLogicOp
JNIEXPORT void JNICALL GL_NATIVE(glLogicOp)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glLogicOp_FUNC);
	glLogicOp(arg0);
	GL_NATIVE_EXIT(env, that, glLogicOp_FUNC);
}
#endif

#ifndef NO_glMap1d
JNIEXPORT void JNICALL GL_NATIVE(glMap1d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdoubleArray arg5)
{
	jdouble *lparg5=NULL;
	GL_NATIVE_ENTER(env, that, glMap1d_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	} else
#endif
	{
		if (arg5) lparg5 = (*env)->GetDoubleArrayElements(env, arg5, NULL);
	}
	glMap1d(arg0, arg1, arg2, arg3, arg4, lparg5);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
	} else
#endif
	{
		if (arg5) (*env)->ReleaseDoubleArrayElements(env, arg5, lparg5, 0);
	}
	GL_NATIVE_EXIT(env, that, glMap1d_FUNC);
}
#endif

#ifndef NO_glMap1f
JNIEXPORT void JNICALL GL_NATIVE(glMap1f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jint arg4, jfloatArray arg5)
{
	jfloat *lparg5=NULL;
	GL_NATIVE_ENTER(env, that, glMap1f_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) lparg5 = (*env)->GetPrimitiveArrayCritical(env, arg5, NULL);
	} else
#endif
	{
		if (arg5) lparg5 = (*env)->GetFloatArrayElements(env, arg5, NULL);
	}
	glMap1f(arg0, arg1, arg2, arg3, arg4, lparg5);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg5) (*env)->ReleasePrimitiveArrayCritical(env, arg5, lparg5, 0);
	} else
#endif
	{
		if (arg5) (*env)->ReleaseFloatArrayElements(env, arg5, lparg5, 0);
	}
	GL_NATIVE_EXIT(env, that, glMap1f_FUNC);
}
#endif

#ifndef NO_glMap2d
JNIEXPORT void JNICALL GL_NATIVE(glMap2d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jint arg4, jdouble arg5, jdouble arg6, jint arg7, jint arg8, jdoubleArray arg9)
{
	jdouble *lparg9=NULL;
	GL_NATIVE_ENTER(env, that, glMap2d_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg9) lparg9 = (*env)->GetPrimitiveArrayCritical(env, arg9, NULL);
	} else
#endif
	{
		if (arg9) lparg9 = (*env)->GetDoubleArrayElements(env, arg9, NULL);
	}
	glMap2d(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, lparg9);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg9) (*env)->ReleasePrimitiveArrayCritical(env, arg9, lparg9, 0);
	} else
#endif
	{
		if (arg9) (*env)->ReleaseDoubleArrayElements(env, arg9, lparg9, 0);
	}
	GL_NATIVE_EXIT(env, that, glMap2d_FUNC);
}
#endif

#ifndef NO_glMap2f
JNIEXPORT void JNICALL GL_NATIVE(glMap2f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jint arg4, jfloat arg5, jfloat arg6, jint arg7, jint arg8, jfloatArray arg9)
{
	jfloat *lparg9=NULL;
	GL_NATIVE_ENTER(env, that, glMap2f_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg9) lparg9 = (*env)->GetPrimitiveArrayCritical(env, arg9, NULL);
	} else
#endif
	{
		if (arg9) lparg9 = (*env)->GetFloatArrayElements(env, arg9, NULL);
	}
	glMap2f(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, lparg9);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg9) (*env)->ReleasePrimitiveArrayCritical(env, arg9, lparg9, 0);
	} else
#endif
	{
		if (arg9) (*env)->ReleaseFloatArrayElements(env, arg9, lparg9, 0);
	}
	GL_NATIVE_EXIT(env, that, glMap2f_FUNC);
}
#endif

#ifndef NO_glMapGrid1d
JNIEXPORT void JNICALL GL_NATIVE(glMapGrid1d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glMapGrid1d_FUNC);
	glMapGrid1d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glMapGrid1d_FUNC);
}
#endif

#ifndef NO_glMapGrid1f
JNIEXPORT void JNICALL GL_NATIVE(glMapGrid1f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glMapGrid1f_FUNC);
	glMapGrid1f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glMapGrid1f_FUNC);
}
#endif

#ifndef NO_glMapGrid2d
JNIEXPORT void JNICALL GL_NATIVE(glMapGrid2d)
	(JNIEnv *env, jclass that, jint arg0, jdouble arg1, jdouble arg2, jint arg3, jdouble arg4, jdouble arg5)
{
	GL_NATIVE_ENTER(env, that, glMapGrid2d_FUNC);
	glMapGrid2d(arg0, arg1, arg2, arg3, arg4, arg5);
	GL_NATIVE_EXIT(env, that, glMapGrid2d_FUNC);
}
#endif

#ifndef NO_glMapGrid2f
JNIEXPORT void JNICALL GL_NATIVE(glMapGrid2f)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1, jfloat arg2, jint arg3, jfloat arg4, jfloat arg5)
{
	GL_NATIVE_ENTER(env, that, glMapGrid2f_FUNC);
	glMapGrid2f(arg0, arg1, arg2, arg3, arg4, arg5);
	GL_NATIVE_EXIT(env, that, glMapGrid2f_FUNC);
}
#endif

#ifndef NO_glMaterialf
JNIEXPORT void JNICALL GL_NATIVE(glMaterialf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glMaterialf_FUNC);
	glMaterialf(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glMaterialf_FUNC);
}
#endif

#ifndef NO_glMaterialfv
JNIEXPORT void JNICALL GL_NATIVE(glMaterialfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glMaterialfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glMaterialfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glMaterialfv_FUNC);
}
#endif

#ifndef NO_glMateriali
JNIEXPORT void JNICALL GL_NATIVE(glMateriali)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glMateriali_FUNC);
	glMateriali(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glMateriali_FUNC);
}
#endif

#ifndef NO_glMaterialiv
JNIEXPORT void JNICALL GL_NATIVE(glMaterialiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glMaterialiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glMaterialiv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glMaterialiv_FUNC);
}
#endif

#ifndef NO_glMatrixMode
JNIEXPORT void JNICALL GL_NATIVE(glMatrixMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glMatrixMode_FUNC);
	glMatrixMode(arg0);
	GL_NATIVE_EXIT(env, that, glMatrixMode_FUNC);
}
#endif

#ifndef NO_glMultMatrixd
JNIEXPORT void JNICALL GL_NATIVE(glMultMatrixd)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glMultMatrixd_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glMultMatrixd(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glMultMatrixd_FUNC);
}
#endif

#ifndef NO_glMultMatrixf
JNIEXPORT void JNICALL GL_NATIVE(glMultMatrixf)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glMultMatrixf_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glMultMatrixf(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glMultMatrixf_FUNC);
}
#endif

#ifndef NO_glNewList
JNIEXPORT void JNICALL GL_NATIVE(glNewList)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glNewList_FUNC);
	glNewList(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glNewList_FUNC);
}
#endif

#ifndef NO_glNormal3b
JNIEXPORT void JNICALL GL_NATIVE(glNormal3b)
	(JNIEnv *env, jclass that, jbyte arg0, jbyte arg1, jbyte arg2)
{
	GL_NATIVE_ENTER(env, that, glNormal3b_FUNC);
	glNormal3b(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glNormal3b_FUNC);
}
#endif

#ifndef NO_glNormal3bv
JNIEXPORT void JNICALL GL_NATIVE(glNormal3bv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glNormal3bv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glNormal3bv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormal3bv_FUNC);
}
#endif

#ifndef NO_glNormal3d
JNIEXPORT void JNICALL GL_NATIVE(glNormal3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glNormal3d_FUNC);
	glNormal3d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glNormal3d_FUNC);
}
#endif

#ifndef NO_glNormal3dv
JNIEXPORT void JNICALL GL_NATIVE(glNormal3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glNormal3dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glNormal3dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormal3dv_FUNC);
}
#endif

#ifndef NO_glNormal3f
JNIEXPORT void JNICALL GL_NATIVE(glNormal3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glNormal3f_FUNC);
	glNormal3f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glNormal3f_FUNC);
}
#endif

#ifndef NO_glNormal3fv
JNIEXPORT void JNICALL GL_NATIVE(glNormal3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glNormal3fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glNormal3fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormal3fv_FUNC);
}
#endif

#ifndef NO_glNormal3i
JNIEXPORT void JNICALL GL_NATIVE(glNormal3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glNormal3i_FUNC);
	glNormal3i(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glNormal3i_FUNC);
}
#endif

#ifndef NO_glNormal3iv
JNIEXPORT void JNICALL GL_NATIVE(glNormal3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glNormal3iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glNormal3iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormal3iv_FUNC);
}
#endif

#ifndef NO_glNormal3s
JNIEXPORT void JNICALL GL_NATIVE(glNormal3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glNormal3s_FUNC);
	glNormal3s(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glNormal3s_FUNC);
}
#endif

#ifndef NO_glNormal3sv
JNIEXPORT void JNICALL GL_NATIVE(glNormal3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glNormal3sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glNormal3sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormal3sv_FUNC);
}
#endif

#ifndef NO_glNormalPointer
JNIEXPORT void JNICALL GL_NATIVE(glNormalPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glNormalPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glNormalPointer(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glNormalPointer_FUNC);
}
#endif

#ifndef NO_glOrtho
JNIEXPORT void JNICALL GL_NATIVE(glOrtho)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3, jdouble arg4, jdouble arg5)
{
	GL_NATIVE_ENTER(env, that, glOrtho_FUNC);
	glOrtho(arg0, arg1, arg2, arg3, arg4, arg5);
	GL_NATIVE_EXIT(env, that, glOrtho_FUNC);
}
#endif

#ifndef NO_glPassThrough
JNIEXPORT void JNICALL GL_NATIVE(glPassThrough)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glPassThrough_FUNC);
	glPassThrough(arg0);
	GL_NATIVE_EXIT(env, that, glPassThrough_FUNC);
}
#endif

#ifndef NO_glPixelMapfv
JNIEXPORT void JNICALL GL_NATIVE(glPixelMapfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glPixelMapfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glPixelMapfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glPixelMapfv_FUNC);
}
#endif

#ifndef NO_glPixelMapuiv
JNIEXPORT void JNICALL GL_NATIVE(glPixelMapuiv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glPixelMapuiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glPixelMapuiv(arg0, arg1, (const GLuint *)lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glPixelMapuiv_FUNC);
}
#endif

#ifndef NO_glPixelMapusv
JNIEXPORT void JNICALL GL_NATIVE(glPixelMapusv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glPixelMapusv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	}
	glPixelMapusv(arg0, arg1, (const GLushort *)lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glPixelMapusv_FUNC);
}
#endif

#ifndef NO_glPixelStoref
JNIEXPORT void JNICALL GL_NATIVE(glPixelStoref)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glPixelStoref_FUNC);
	glPixelStoref(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPixelStoref_FUNC);
}
#endif

#ifndef NO_glPixelStorei
JNIEXPORT void JNICALL GL_NATIVE(glPixelStorei)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glPixelStorei_FUNC);
	glPixelStorei(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPixelStorei_FUNC);
}
#endif

#ifndef NO_glPixelTransferf
JNIEXPORT void JNICALL GL_NATIVE(glPixelTransferf)
	(JNIEnv *env, jclass that, jint arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glPixelTransferf_FUNC);
	glPixelTransferf(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPixelTransferf_FUNC);
}
#endif

#ifndef NO_glPixelTransferi
JNIEXPORT void JNICALL GL_NATIVE(glPixelTransferi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glPixelTransferi_FUNC);
	glPixelTransferi(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPixelTransferi_FUNC);
}
#endif

#ifndef NO_glPixelZoom
JNIEXPORT void JNICALL GL_NATIVE(glPixelZoom)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glPixelZoom_FUNC);
	glPixelZoom(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPixelZoom_FUNC);
}
#endif

#ifndef NO_glPointSize
JNIEXPORT void JNICALL GL_NATIVE(glPointSize)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glPointSize_FUNC);
	glPointSize(arg0);
	GL_NATIVE_EXIT(env, that, glPointSize_FUNC);
}
#endif

#ifndef NO_glPolygonMode
JNIEXPORT void JNICALL GL_NATIVE(glPolygonMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glPolygonMode_FUNC);
	glPolygonMode(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPolygonMode_FUNC);
}
#endif

#ifndef NO_glPolygonOffset
JNIEXPORT void JNICALL GL_NATIVE(glPolygonOffset)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glPolygonOffset_FUNC);
	glPolygonOffset(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glPolygonOffset_FUNC);
}
#endif

#ifndef NO_glPolygonStipple
JNIEXPORT void JNICALL GL_NATIVE(glPolygonStipple)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glPolygonStipple_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	}
	glPolygonStipple((GLubyte *)lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glPolygonStipple_FUNC);
}
#endif

#ifndef NO_glPopAttrib
JNIEXPORT void JNICALL GL_NATIVE(glPopAttrib)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glPopAttrib_FUNC);
	glPopAttrib();
	GL_NATIVE_EXIT(env, that, glPopAttrib_FUNC);
}
#endif

#ifndef NO_glPopClientAttrib
JNIEXPORT void JNICALL GL_NATIVE(glPopClientAttrib)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glPopClientAttrib_FUNC);
	glPopClientAttrib();
	GL_NATIVE_EXIT(env, that, glPopClientAttrib_FUNC);
}
#endif

#ifndef NO_glPopMatrix
JNIEXPORT void JNICALL GL_NATIVE(glPopMatrix)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glPopMatrix_FUNC);
	glPopMatrix();
	GL_NATIVE_EXIT(env, that, glPopMatrix_FUNC);
}
#endif

#ifndef NO_glPopName
JNIEXPORT void JNICALL GL_NATIVE(glPopName)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glPopName_FUNC);
	glPopName();
	GL_NATIVE_EXIT(env, that, glPopName_FUNC);
}
#endif

#ifndef NO_glPrioritizeTextures
JNIEXPORT void JNICALL GL_NATIVE(glPrioritizeTextures)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jfloatArray arg2)
{
	jint *lparg1=NULL;
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glPrioritizeTextures_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glPrioritizeTextures(arg0, (const GLuint *)lparg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	}
	GL_NATIVE_EXIT(env, that, glPrioritizeTextures_FUNC);
}
#endif

#ifndef NO_glPushAttrib
JNIEXPORT void JNICALL GL_NATIVE(glPushAttrib)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glPushAttrib_FUNC);
	glPushAttrib(arg0);
	GL_NATIVE_EXIT(env, that, glPushAttrib_FUNC);
}
#endif

#ifndef NO_glPushClientAttrib
JNIEXPORT void JNICALL GL_NATIVE(glPushClientAttrib)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glPushClientAttrib_FUNC);
	glPushClientAttrib(arg0);
	GL_NATIVE_EXIT(env, that, glPushClientAttrib_FUNC);
}
#endif

#ifndef NO_glPushMatrix
JNIEXPORT void JNICALL GL_NATIVE(glPushMatrix)
	(JNIEnv *env, jclass that)
{
	GL_NATIVE_ENTER(env, that, glPushMatrix_FUNC);
	glPushMatrix();
	GL_NATIVE_EXIT(env, that, glPushMatrix_FUNC);
}
#endif

#ifndef NO_glPushName
JNIEXPORT void JNICALL GL_NATIVE(glPushName)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glPushName_FUNC);
	glPushName(arg0);
	GL_NATIVE_EXIT(env, that, glPushName_FUNC);
}
#endif

#ifndef NO_glRasterPos2d
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	GL_NATIVE_ENTER(env, that, glRasterPos2d_FUNC);
	glRasterPos2d(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glRasterPos2d_FUNC);
}
#endif

#ifndef NO_glRasterPos2dv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos2dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glRasterPos2dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos2dv_FUNC);
}
#endif

#ifndef NO_glRasterPos2f
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glRasterPos2f_FUNC);
	glRasterPos2f(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glRasterPos2f_FUNC);
}
#endif

#ifndef NO_glRasterPos2fv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos2fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glRasterPos2fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos2fv_FUNC);
}
#endif

#ifndef NO_glRasterPos2i
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glRasterPos2i_FUNC);
	glRasterPos2i(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glRasterPos2i_FUNC);
}
#endif

#ifndef NO_glRasterPos2iv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos2iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glRasterPos2iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos2iv_FUNC);
}
#endif

#ifndef NO_glRasterPos2s
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	GL_NATIVE_ENTER(env, that, glRasterPos2s_FUNC);
	glRasterPos2s(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glRasterPos2s_FUNC);
}
#endif

#ifndef NO_glRasterPos2sv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos2sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glRasterPos2sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos2sv_FUNC);
}
#endif

#ifndef NO_glRasterPos3d
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glRasterPos3d_FUNC);
	glRasterPos3d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glRasterPos3d_FUNC);
}
#endif

#ifndef NO_glRasterPos3dv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos3dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glRasterPos3dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos3dv_FUNC);
}
#endif

#ifndef NO_glRasterPos3f
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glRasterPos3f_FUNC);
	glRasterPos3f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glRasterPos3f_FUNC);
}
#endif

#ifndef NO_glRasterPos3fv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos3fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glRasterPos3fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos3fv_FUNC);
}
#endif

#ifndef NO_glRasterPos3i
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glRasterPos3i_FUNC);
	glRasterPos3i(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glRasterPos3i_FUNC);
}
#endif

#ifndef NO_glRasterPos3iv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos3iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glRasterPos3iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos3iv_FUNC);
}
#endif

#ifndef NO_glRasterPos3s
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glRasterPos3s_FUNC);
	glRasterPos3s(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glRasterPos3s_FUNC);
}
#endif

#ifndef NO_glRasterPos3sv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos3sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glRasterPos3sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos3sv_FUNC);
}
#endif

#ifndef NO_glRasterPos4d
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glRasterPos4d_FUNC);
	glRasterPos4d(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRasterPos4d_FUNC);
}
#endif

#ifndef NO_glRasterPos4dv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos4dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glRasterPos4dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos4dv_FUNC);
}
#endif

#ifndef NO_glRasterPos4f
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glRasterPos4f_FUNC);
	glRasterPos4f(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRasterPos4f_FUNC);
}
#endif

#ifndef NO_glRasterPos4fv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos4fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glRasterPos4fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos4fv_FUNC);
}
#endif

#ifndef NO_glRasterPos4i
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glRasterPos4i_FUNC);
	glRasterPos4i(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRasterPos4i_FUNC);
}
#endif

#ifndef NO_glRasterPos4iv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos4iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glRasterPos4iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos4iv_FUNC);
}
#endif

#ifndef NO_glRasterPos4s
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glRasterPos4s_FUNC);
	glRasterPos4s(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRasterPos4s_FUNC);
}
#endif

#ifndef NO_glRasterPos4sv
JNIEXPORT void JNICALL GL_NATIVE(glRasterPos4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glRasterPos4sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glRasterPos4sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRasterPos4sv_FUNC);
}
#endif

#ifndef NO_glReadBuffer
JNIEXPORT void JNICALL GL_NATIVE(glReadBuffer)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glReadBuffer_FUNC);
	glReadBuffer(arg0);
	GL_NATIVE_EXIT(env, that, glReadBuffer_FUNC);
}
#endif

#ifndef NO_glReadPixels
JNIEXPORT void JNICALL GL_NATIVE(glReadPixels)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;
	GL_NATIVE_ENTER(env, that, glReadPixels_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
	} else
#endif
	{
		if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	}
	glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, lparg6);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
	} else
#endif
	{
		if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	}
	GL_NATIVE_EXIT(env, that, glReadPixels_FUNC);
}
#endif

#ifndef NO_glRectd
JNIEXPORT void JNICALL GL_NATIVE(glRectd)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glRectd_FUNC);
	glRectd(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRectd_FUNC);
}
#endif

#ifndef NO_glRectdv
JNIEXPORT void JNICALL GL_NATIVE(glRectdv)
	(JNIEnv *env, jclass that, jdoubleArray arg0, jdoubleArray arg1)
{
	jdouble *lparg0=NULL;
	jdouble *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glRectdv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetDoubleArrayElements(env, arg1, NULL);
	}
	glRectdv(lparg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseDoubleArrayElements(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRectdv_FUNC);
}
#endif

#ifndef NO_glRectf
JNIEXPORT void JNICALL GL_NATIVE(glRectf)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glRectf_FUNC);
	glRectf(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRectf_FUNC);
}
#endif

#ifndef NO_glRectfv
JNIEXPORT void JNICALL GL_NATIVE(glRectfv)
	(JNIEnv *env, jclass that, jfloatArray arg0, jfloatArray arg1)
{
	jfloat *lparg0=NULL;
	jfloat *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glRectfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetFloatArrayElements(env, arg1, NULL);
	}
	glRectfv(lparg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseFloatArrayElements(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRectfv_FUNC);
}
#endif

#ifndef NO_glRecti
JNIEXPORT void JNICALL GL_NATIVE(glRecti)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glRecti_FUNC);
	glRecti(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRecti_FUNC);
}
#endif

#ifndef NO_glRectiv
JNIEXPORT void JNICALL GL_NATIVE(glRectiv)
	(JNIEnv *env, jclass that, jintArray arg0, jintArray arg1)
{
	jint *lparg0=NULL;
	jint *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glRectiv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	}
	glRectiv(lparg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRectiv_FUNC);
}
#endif

#ifndef NO_glRects
JNIEXPORT void JNICALL GL_NATIVE(glRects)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glRects_FUNC);
	glRects(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRects_FUNC);
}
#endif

#ifndef NO_glRectsv
JNIEXPORT void JNICALL GL_NATIVE(glRectsv)
	(JNIEnv *env, jclass that, jshortArray arg0, jshortArray arg1)
{
	jshort *lparg0=NULL;
	jshort *lparg1=NULL;
	GL_NATIVE_ENTER(env, that, glRectsv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetPrimitiveArrayCritical(env, arg1, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
		if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	}
	glRectsv(lparg0, lparg1);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg1) (*env)->ReleasePrimitiveArrayCritical(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, 0);
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glRectsv_FUNC);
}
#endif

#ifndef NO_glRenderMode
JNIEXPORT jint JNICALL GL_NATIVE(glRenderMode)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	GL_NATIVE_ENTER(env, that, glRenderMode_FUNC);
	rc = (jint)glRenderMode(arg0);
	GL_NATIVE_EXIT(env, that, glRenderMode_FUNC);
	return rc;
}
#endif

#ifndef NO_glRotated
JNIEXPORT void JNICALL GL_NATIVE(glRotated)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glRotated_FUNC);
	glRotated(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRotated_FUNC);
}
#endif

#ifndef NO_glRotatef
JNIEXPORT void JNICALL GL_NATIVE(glRotatef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glRotatef_FUNC);
	glRotatef(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glRotatef_FUNC);
}
#endif

#ifndef NO_glScaled
JNIEXPORT void JNICALL GL_NATIVE(glScaled)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glScaled_FUNC);
	glScaled(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glScaled_FUNC);
}
#endif

#ifndef NO_glScalef
JNIEXPORT void JNICALL GL_NATIVE(glScalef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glScalef_FUNC);
	glScalef(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glScalef_FUNC);
}
#endif

#ifndef NO_glScissor
JNIEXPORT void JNICALL GL_NATIVE(glScissor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glScissor_FUNC);
	glScissor(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glScissor_FUNC);
}
#endif

#ifndef NO_glSelectBuffer
JNIEXPORT void JNICALL GL_NATIVE(glSelectBuffer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glSelectBuffer_FUNC);
	glSelectBuffer(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glSelectBuffer_FUNC);
}
#endif

#ifndef NO_glShadeModel
JNIEXPORT void JNICALL GL_NATIVE(glShadeModel)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glShadeModel_FUNC);
	glShadeModel(arg0);
	GL_NATIVE_EXIT(env, that, glShadeModel_FUNC);
}
#endif

#ifndef NO_glStencilFunc
JNIEXPORT void JNICALL GL_NATIVE(glStencilFunc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glStencilFunc_FUNC);
	glStencilFunc(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glStencilFunc_FUNC);
}
#endif

#ifndef NO_glStencilMask
JNIEXPORT void JNICALL GL_NATIVE(glStencilMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glStencilMask_FUNC);
	glStencilMask(arg0);
	GL_NATIVE_EXIT(env, that, glStencilMask_FUNC);
}
#endif

#ifndef NO_glStencilOp
JNIEXPORT void JNICALL GL_NATIVE(glStencilOp)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glStencilOp_FUNC);
	glStencilOp(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glStencilOp_FUNC);
}
#endif

#ifndef NO_glTexCoord1d
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1d)
	(JNIEnv *env, jclass that, jdouble arg0)
{
	GL_NATIVE_ENTER(env, that, glTexCoord1d_FUNC);
	glTexCoord1d(arg0);
	GL_NATIVE_EXIT(env, that, glTexCoord1d_FUNC);
}
#endif

#ifndef NO_glTexCoord1dv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord1dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glTexCoord1dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord1dv_FUNC);
}
#endif

#ifndef NO_glTexCoord1f
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1f)
	(JNIEnv *env, jclass that, jfloat arg0)
{
	GL_NATIVE_ENTER(env, that, glTexCoord1f_FUNC);
	glTexCoord1f(arg0);
	GL_NATIVE_EXIT(env, that, glTexCoord1f_FUNC);
}
#endif

#ifndef NO_glTexCoord1fv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord1fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glTexCoord1fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord1fv_FUNC);
}
#endif

#ifndef NO_glTexCoord1i
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1i)
	(JNIEnv *env, jclass that, jint arg0)
{
	GL_NATIVE_ENTER(env, that, glTexCoord1i_FUNC);
	glTexCoord1i(arg0);
	GL_NATIVE_EXIT(env, that, glTexCoord1i_FUNC);
}
#endif

#ifndef NO_glTexCoord1iv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord1iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glTexCoord1iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord1iv_FUNC);
}
#endif

#ifndef NO_glTexCoord1s
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1s)
	(JNIEnv *env, jclass that, jshort arg0)
{
	GL_NATIVE_ENTER(env, that, glTexCoord1s_FUNC);
	glTexCoord1s(arg0);
	GL_NATIVE_EXIT(env, that, glTexCoord1s_FUNC);
}
#endif

#ifndef NO_glTexCoord1sv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord1sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord1sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glTexCoord1sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord1sv_FUNC);
}
#endif

#ifndef NO_glTexCoord2d
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	GL_NATIVE_ENTER(env, that, glTexCoord2d_FUNC);
	glTexCoord2d(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glTexCoord2d_FUNC);
}
#endif

#ifndef NO_glTexCoord2dv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord2dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glTexCoord2dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord2dv_FUNC);
}
#endif

#ifndef NO_glTexCoord2f
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glTexCoord2f_FUNC);
	glTexCoord2f(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glTexCoord2f_FUNC);
}
#endif

#ifndef NO_glTexCoord2fv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord2fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glTexCoord2fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord2fv_FUNC);
}
#endif

#ifndef NO_glTexCoord2i
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glTexCoord2i_FUNC);
	glTexCoord2i(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glTexCoord2i_FUNC);
}
#endif

#ifndef NO_glTexCoord2iv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord2iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glTexCoord2iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord2iv_FUNC);
}
#endif

#ifndef NO_glTexCoord2s
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	GL_NATIVE_ENTER(env, that, glTexCoord2s_FUNC);
	glTexCoord2s(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glTexCoord2s_FUNC);
}
#endif

#ifndef NO_glTexCoord2sv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord2sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glTexCoord2sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord2sv_FUNC);
}
#endif

#ifndef NO_glTexCoord3d
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glTexCoord3d_FUNC);
	glTexCoord3d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexCoord3d_FUNC);
}
#endif

#ifndef NO_glTexCoord3dv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord3dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glTexCoord3dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord3dv_FUNC);
}
#endif

#ifndef NO_glTexCoord3f
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glTexCoord3f_FUNC);
	glTexCoord3f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexCoord3f_FUNC);
}
#endif

#ifndef NO_glTexCoord3fv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord3fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glTexCoord3fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord3fv_FUNC);
}
#endif

#ifndef NO_glTexCoord3i
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glTexCoord3i_FUNC);
	glTexCoord3i(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexCoord3i_FUNC);
}
#endif

#ifndef NO_glTexCoord3iv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord3iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glTexCoord3iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord3iv_FUNC);
}
#endif

#ifndef NO_glTexCoord3s
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glTexCoord3s_FUNC);
	glTexCoord3s(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexCoord3s_FUNC);
}
#endif

#ifndef NO_glTexCoord3sv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord3sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glTexCoord3sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord3sv_FUNC);
}
#endif

#ifndef NO_glTexCoord4d
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glTexCoord4d_FUNC);
	glTexCoord4d(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glTexCoord4d_FUNC);
}
#endif

#ifndef NO_glTexCoord4dv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord4dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glTexCoord4dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord4dv_FUNC);
}
#endif

#ifndef NO_glTexCoord4f
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glTexCoord4f_FUNC);
	glTexCoord4f(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glTexCoord4f_FUNC);
}
#endif

#ifndef NO_glTexCoord4fv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord4fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glTexCoord4fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord4fv_FUNC);
}
#endif

#ifndef NO_glTexCoord4i
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glTexCoord4i_FUNC);
	glTexCoord4i(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glTexCoord4i_FUNC);
}
#endif

#ifndef NO_glTexCoord4iv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord4iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glTexCoord4iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord4iv_FUNC);
}
#endif

#ifndef NO_glTexCoord4s
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glTexCoord4s_FUNC);
	glTexCoord4s(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glTexCoord4s_FUNC);
}
#endif

#ifndef NO_glTexCoord4sv
JNIEXPORT void JNICALL GL_NATIVE(glTexCoord4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoord4sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glTexCoord4sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoord4sv_FUNC);
}
#endif

#ifndef NO_glTexCoordPointer
JNIEXPORT void JNICALL GL_NATIVE(glTexCoordPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glTexCoordPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	glTexCoordPointer(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexCoordPointer_FUNC);
}
#endif

#ifndef NO_glTexEnvf
JNIEXPORT void JNICALL GL_NATIVE(glTexEnvf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glTexEnvf_FUNC);
	glTexEnvf(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexEnvf_FUNC);
}
#endif

#ifndef NO_glTexEnvfv
JNIEXPORT void JNICALL GL_NATIVE(glTexEnvfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexEnvfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glTexEnvfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexEnvfv_FUNC);
}
#endif

#ifndef NO_glTexEnvi
JNIEXPORT void JNICALL GL_NATIVE(glTexEnvi)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glTexEnvi_FUNC);
	glTexEnvi(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexEnvi_FUNC);
}
#endif

#ifndef NO_glTexEnviv
JNIEXPORT void JNICALL GL_NATIVE(glTexEnviv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexEnviv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glTexEnviv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexEnviv_FUNC);
}
#endif

#ifndef NO_glTexGend
JNIEXPORT void JNICALL GL_NATIVE(glTexGend)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glTexGend_FUNC);
	glTexGend(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexGend_FUNC);
}
#endif

#ifndef NO_glTexGendv
JNIEXPORT void JNICALL GL_NATIVE(glTexGendv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdoubleArray arg2)
{
	jdouble *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexGendv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetDoubleArrayElements(env, arg2, NULL);
	}
	glTexGendv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseDoubleArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexGendv_FUNC);
}
#endif

#ifndef NO_glTexGenf
JNIEXPORT void JNICALL GL_NATIVE(glTexGenf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glTexGenf_FUNC);
	glTexGenf(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexGenf_FUNC);
}
#endif

#ifndef NO_glTexGenfv
JNIEXPORT void JNICALL GL_NATIVE(glTexGenfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexGenfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glTexGenfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexGenfv_FUNC);
}
#endif

#ifndef NO_glTexGeni
JNIEXPORT void JNICALL GL_NATIVE(glTexGeni)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glTexGeni_FUNC);
	glTexGeni(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexGeni_FUNC);
}
#endif

#ifndef NO_glTexGeniv
JNIEXPORT void JNICALL GL_NATIVE(glTexGeniv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexGeniv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glTexGeniv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexGeniv_FUNC);
}
#endif

#ifndef NO_glTexImage1D
JNIEXPORT void JNICALL GL_NATIVE(glTexImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
{
	jint *lparg7=NULL;
	GL_NATIVE_ENTER(env, that, glTexImage1D_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg7) lparg7 = (*env)->GetPrimitiveArrayCritical(env, arg7, NULL);
	} else
#endif
	{
		if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	}
	glTexImage1D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, lparg7);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg7) (*env)->ReleasePrimitiveArrayCritical(env, arg7, lparg7, 0);
	} else
#endif
	{
		if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexImage1D_FUNC);
}
#endif

#ifndef NO_glTexImage2D
JNIEXPORT void JNICALL GL_NATIVE(glTexImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jbyteArray arg8)
{
	jbyte *lparg8=NULL;
	GL_NATIVE_ENTER(env, that, glTexImage2D_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) lparg8 = (*env)->GetPrimitiveArrayCritical(env, arg8, NULL);
	} else
#endif
	{
		if (arg8) lparg8 = (*env)->GetByteArrayElements(env, arg8, NULL);
	}
	glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) (*env)->ReleasePrimitiveArrayCritical(env, arg8, lparg8, 0);
	} else
#endif
	{
		if (arg8) (*env)->ReleaseByteArrayElements(env, arg8, lparg8, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexImage2D_FUNC);
}
#endif

#ifndef NO_glTexParameterf
JNIEXPORT void JNICALL GL_NATIVE(glTexParameterf)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glTexParameterf_FUNC);
	glTexParameterf(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexParameterf_FUNC);
}
#endif

#ifndef NO_glTexParameterfv
JNIEXPORT void JNICALL GL_NATIVE(glTexParameterfv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexParameterfv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetFloatArrayElements(env, arg2, NULL);
	}
	glTexParameterfv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseFloatArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexParameterfv_FUNC);
}
#endif

#ifndef NO_glTexParameteri
JNIEXPORT void JNICALL GL_NATIVE(glTexParameteri)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glTexParameteri_FUNC);
	glTexParameteri(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTexParameteri_FUNC);
}
#endif

#ifndef NO_glTexParameteriv
JNIEXPORT void JNICALL GL_NATIVE(glTexParameteriv)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	GL_NATIVE_ENTER(env, that, glTexParameteriv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) lparg2 = (*env)->GetPrimitiveArrayCritical(env, arg2, NULL);
	} else
#endif
	{
		if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	}
	glTexParameteriv(arg0, arg1, lparg2);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg2) (*env)->ReleasePrimitiveArrayCritical(env, arg2, lparg2, 0);
	} else
#endif
	{
		if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexParameteriv_FUNC);
}
#endif

#ifndef NO_glTexSubImage1D
JNIEXPORT void JNICALL GL_NATIVE(glTexSubImage1D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;
	GL_NATIVE_ENTER(env, that, glTexSubImage1D_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) lparg6 = (*env)->GetPrimitiveArrayCritical(env, arg6, NULL);
	} else
#endif
	{
		if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	}
	glTexSubImage1D(arg0, arg1, arg2, arg3, arg4, arg5, lparg6);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg6) (*env)->ReleasePrimitiveArrayCritical(env, arg6, lparg6, 0);
	} else
#endif
	{
		if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexSubImage1D_FUNC);
}
#endif

#ifndef NO_glTexSubImage2D
JNIEXPORT void JNICALL GL_NATIVE(glTexSubImage2D)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jintArray arg8)
{
	jint *lparg8=NULL;
	GL_NATIVE_ENTER(env, that, glTexSubImage2D_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) lparg8 = (*env)->GetPrimitiveArrayCritical(env, arg8, NULL);
	} else
#endif
	{
		if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	}
	glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg8) (*env)->ReleasePrimitiveArrayCritical(env, arg8, lparg8, 0);
	} else
#endif
	{
		if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	}
	GL_NATIVE_EXIT(env, that, glTexSubImage2D_FUNC);
}
#endif

#ifndef NO_glTranslated
JNIEXPORT void JNICALL GL_NATIVE(glTranslated)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glTranslated_FUNC);
	glTranslated(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTranslated_FUNC);
}
#endif

#ifndef NO_glTranslatef
JNIEXPORT void JNICALL GL_NATIVE(glTranslatef)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glTranslatef_FUNC);
	glTranslatef(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glTranslatef_FUNC);
}
#endif

#ifndef NO_glVertex2d
JNIEXPORT void JNICALL GL_NATIVE(glVertex2d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1)
{
	GL_NATIVE_ENTER(env, that, glVertex2d_FUNC);
	glVertex2d(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glVertex2d_FUNC);
}
#endif

#ifndef NO_glVertex2dv
JNIEXPORT void JNICALL GL_NATIVE(glVertex2dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex2dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glVertex2dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex2dv_FUNC);
}
#endif

#ifndef NO_glVertex2f
JNIEXPORT void JNICALL GL_NATIVE(glVertex2f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1)
{
	GL_NATIVE_ENTER(env, that, glVertex2f_FUNC);
	glVertex2f(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glVertex2f_FUNC);
}
#endif

#ifndef NO_glVertex2fv
JNIEXPORT void JNICALL GL_NATIVE(glVertex2fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex2fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glVertex2fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex2fv_FUNC);
}
#endif

#ifndef NO_glVertex2i
JNIEXPORT void JNICALL GL_NATIVE(glVertex2i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	GL_NATIVE_ENTER(env, that, glVertex2i_FUNC);
	glVertex2i(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glVertex2i_FUNC);
}
#endif

#ifndef NO_glVertex2iv
JNIEXPORT void JNICALL GL_NATIVE(glVertex2iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex2iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glVertex2iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex2iv_FUNC);
}
#endif

#ifndef NO_glVertex2s
JNIEXPORT void JNICALL GL_NATIVE(glVertex2s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1)
{
	GL_NATIVE_ENTER(env, that, glVertex2s_FUNC);
	glVertex2s(arg0, arg1);
	GL_NATIVE_EXIT(env, that, glVertex2s_FUNC);
}
#endif

#ifndef NO_glVertex2sv
JNIEXPORT void JNICALL GL_NATIVE(glVertex2sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex2sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glVertex2sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex2sv_FUNC);
}
#endif

#ifndef NO_glVertex3d
JNIEXPORT void JNICALL GL_NATIVE(glVertex3d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2)
{
	GL_NATIVE_ENTER(env, that, glVertex3d_FUNC);
	glVertex3d(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glVertex3d_FUNC);
}
#endif

#ifndef NO_glVertex3dv
JNIEXPORT void JNICALL GL_NATIVE(glVertex3dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex3dv_FUNC);
	if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	glVertex3dv(lparg0);
	if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	GL_NATIVE_EXIT(env, that, glVertex3dv_FUNC);
}
#endif

#ifndef NO_glVertex3f
JNIEXPORT void JNICALL GL_NATIVE(glVertex3f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2)
{
	GL_NATIVE_ENTER(env, that, glVertex3f_FUNC);
	glVertex3f(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glVertex3f_FUNC);
}
#endif

#ifndef NO_glVertex3fv
JNIEXPORT void JNICALL GL_NATIVE(glVertex3fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex3fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glVertex3fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex3fv_FUNC);
}
#endif

#ifndef NO_glVertex3i
JNIEXPORT void JNICALL GL_NATIVE(glVertex3i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	GL_NATIVE_ENTER(env, that, glVertex3i_FUNC);
	glVertex3i(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glVertex3i_FUNC);
}
#endif

#ifndef NO_glVertex3iv
JNIEXPORT void JNICALL GL_NATIVE(glVertex3iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex3iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glVertex3iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex3iv_FUNC);
}
#endif

#ifndef NO_glVertex3s
JNIEXPORT void JNICALL GL_NATIVE(glVertex3s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2)
{
	GL_NATIVE_ENTER(env, that, glVertex3s_FUNC);
	glVertex3s(arg0, arg1, arg2);
	GL_NATIVE_EXIT(env, that, glVertex3s_FUNC);
}
#endif

#ifndef NO_glVertex3sv
JNIEXPORT void JNICALL GL_NATIVE(glVertex3sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex3sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glVertex3sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex3sv_FUNC);
}
#endif

#ifndef NO_glVertex4d
JNIEXPORT void JNICALL GL_NATIVE(glVertex4d)
	(JNIEnv *env, jclass that, jdouble arg0, jdouble arg1, jdouble arg2, jdouble arg3)
{
	GL_NATIVE_ENTER(env, that, glVertex4d_FUNC);
	glVertex4d(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glVertex4d_FUNC);
}
#endif

#ifndef NO_glVertex4dv
JNIEXPORT void JNICALL GL_NATIVE(glVertex4dv)
	(JNIEnv *env, jclass that, jdoubleArray arg0)
{
	jdouble *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex4dv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetDoubleArrayElements(env, arg0, NULL);
	}
	glVertex4dv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseDoubleArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex4dv_FUNC);
}
#endif

#ifndef NO_glVertex4f
JNIEXPORT void JNICALL GL_NATIVE(glVertex4f)
	(JNIEnv *env, jclass that, jfloat arg0, jfloat arg1, jfloat arg2, jfloat arg3)
{
	GL_NATIVE_ENTER(env, that, glVertex4f_FUNC);
	glVertex4f(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glVertex4f_FUNC);
}
#endif

#ifndef NO_glVertex4fv
JNIEXPORT void JNICALL GL_NATIVE(glVertex4fv)
	(JNIEnv *env, jclass that, jfloatArray arg0)
{
	jfloat *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex4fv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetFloatArrayElements(env, arg0, NULL);
	}
	glVertex4fv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseFloatArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex4fv_FUNC);
}
#endif

#ifndef NO_glVertex4i
JNIEXPORT void JNICALL GL_NATIVE(glVertex4i)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glVertex4i_FUNC);
	glVertex4i(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glVertex4i_FUNC);
}
#endif

#ifndef NO_glVertex4iv
JNIEXPORT void JNICALL GL_NATIVE(glVertex4iv)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex4iv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	}
	glVertex4iv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex4iv_FUNC);
}
#endif

#ifndef NO_glVertex4s
JNIEXPORT void JNICALL GL_NATIVE(glVertex4s)
	(JNIEnv *env, jclass that, jshort arg0, jshort arg1, jshort arg2, jshort arg3)
{
	GL_NATIVE_ENTER(env, that, glVertex4s_FUNC);
	glVertex4s(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glVertex4s_FUNC);
}
#endif

#ifndef NO_glVertex4sv
JNIEXPORT void JNICALL GL_NATIVE(glVertex4sv)
	(JNIEnv *env, jclass that, jshortArray arg0)
{
	jshort *lparg0=NULL;
	GL_NATIVE_ENTER(env, that, glVertex4sv_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) lparg0 = (*env)->GetPrimitiveArrayCritical(env, arg0, NULL);
	} else
#endif
	{
		if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	}
	glVertex4sv(lparg0);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg0) (*env)->ReleasePrimitiveArrayCritical(env, arg0, lparg0, 0);
	} else
#endif
	{
		if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertex4sv_FUNC);
}
#endif

#ifndef NO_glVertexPointer
JNIEXPORT void JNICALL GL_NATIVE(glVertexPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	GL_NATIVE_ENTER(env, that, glVertexPointer_FUNC);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) lparg3 = (*env)->GetPrimitiveArrayCritical(env, arg3, NULL);
	} else
#endif
	{
		if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	}
	glVertexPointer(arg0, arg1, arg2, lparg3);
#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		if (arg3) (*env)->ReleasePrimitiveArrayCritical(env, arg3, lparg3, 0);
	} else
#endif
	{
		if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	}
	GL_NATIVE_EXIT(env, that, glVertexPointer_FUNC);
}
#endif

#ifndef NO_glViewport
JNIEXPORT void JNICALL GL_NATIVE(glViewport)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	GL_NATIVE_ENTER(env, that, glViewport_FUNC);
	glViewport(arg0, arg1, arg2, arg3);
	GL_NATIVE_EXIT(env, that, glViewport_FUNC);
}
#endif


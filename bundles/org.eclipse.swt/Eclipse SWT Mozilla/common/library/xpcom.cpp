/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcom_structs.h"
#include "xpcom_stats.h"

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_Call
JNIEXPORT jint JNICALL XPCOM_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6)
{
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, Call_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((nsWriteSegmentFun)arg0)((nsIInputStream *)arg1, (void *)arg2, (const char *)lparg3, arg4, arg5, (PRUint32 *)lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, Call_FUNC);
	return rc;
}
#endif

#ifndef NO_NS_1GetComponentManager
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1GetComponentManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1GetComponentManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetComponentManager((nsIComponentManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, NS_1GetComponentManager_FUNC);
	return rc;
}
#endif

#ifndef NO_NS_1GetServiceManager
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1GetServiceManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1GetServiceManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetServiceManager((nsIServiceManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, NS_1GetServiceManager_FUNC);
	return rc;
}
#endif

#ifndef NO_NS_1InitEmbedding
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1InitEmbedding)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1InitEmbedding_FUNC);
	rc = (jint)NS_InitEmbedding((nsILocalFile *)arg0, (nsIDirectoryServiceProvider *)arg1);
	XPCOM_NATIVE_EXIT(env, that, NS_1InitEmbedding_FUNC);
	return rc;
}
#endif

#ifndef NO_NS_1NewLocalFile
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1NewLocalFile)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1NewLocalFile_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)NS_NewLocalFile(*(nsAString *)arg0, arg1, (nsILocalFile**)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, NS_1NewLocalFile_FUNC);
	return rc;
}
#endif

#ifndef NO_NS_1TermEmbedding
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1TermEmbedding)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1TermEmbedding_FUNC);
	rc = (jint)NS_TermEmbedding();
	XPCOM_NATIVE_EXIT(env, that, NS_1TermEmbedding_FUNC);
	return rc;
}
#endif

#ifndef NO_PR_1Free
JNIEXPORT void JNICALL XPCOM_NATIVE(PR_1Free)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, PR_1Free_FUNC);
	PR_Free((void *)arg0);
	XPCOM_NATIVE_EXIT(env, that, PR_1Free_FUNC);
}
#endif

#ifndef NO_PR_1Malloc
JNIEXPORT jint JNICALL XPCOM_NATIVE(PR_1Malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, PR_1Malloc_FUNC);
	rc = (jint)PR_Malloc(arg0);
	XPCOM_NATIVE_EXIT(env, that, PR_1Malloc_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint))(*(jint **)arg1)[arg0])(arg1);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIF
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIF_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jfloat))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIIZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIIIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIZ_3CIIIIZ_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIZ_3CIIIIZ_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jboolean arg11, jintArray arg12, jintArray arg13)
{
	jchar *lparg6=NULL;
	jint *lparg12=NULL;
	jint *lparg13=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIIZ_3CIIIIZ_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg12) if ((lparg12 = env->GetIntArrayElements(arg12, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetIntArrayElements(arg13, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jboolean, jchar *, jint, jint, jint, jint, jboolean, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, arg7, arg8, arg9, arg10, arg11, lparg12, lparg13);
fail:
	if (arg13 && lparg13) env->ReleaseIntArrayElements(arg13, lparg13, 0);
	if (arg12 && lparg12) env->ReleaseIntArrayElements(arg12, lparg12, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIIZ_3CIIIIZ_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIII_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5)
{
	jchar *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIII_3C_FUNC);
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIII_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIII_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIJZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIJZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlong arg4, jboolean arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIJZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jlong, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIJZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIII_3C_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII_3CIJI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3CIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIII_3CIJI_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *, jint, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIII_3CIJI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIII_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII_3J
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlongArray arg4)
{
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIII_3J_FUNC);
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIII_3J_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIJJ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jlong arg3, jlong arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIJJ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, nsID *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIZZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIZZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZZII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIZZII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean, jboolean, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIZZII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZZIIIIIIZZZZSI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZIIIIIIZZZZSI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jboolean arg11, jboolean arg12, jboolean arg13, jboolean arg14, jshort arg15, jint arg16)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIZZIIIIIIZZZZSI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean, jboolean, jint, jint, jint, jint, jint, jint, jboolean, jboolean, jboolean, jboolean, jshort, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIZZIIIIIIZZZZSI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZ_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jbooleanArray arg4)
{
	jboolean *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIIZ_3Z_FUNC);
	if (arg4) if ((lparg4 = env->GetBooleanArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIIZ_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3BI_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3BI_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3B_3B_3BI_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3B_3B_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3B_3B_3BI_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jbyte *, jbyte *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3B_3B_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3B_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbooleanArray arg4)
{
	jbyte *lparg3=NULL;
	jboolean *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3B_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetBooleanArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3B_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3CI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jint arg4)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3CI_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3CI_3C_3C_3C_3C_3Z_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3CI_3C_3C_3C_3C_3Z_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jbooleanArray arg10, jintArray arg11)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg6=NULL;
	jchar *lparg7=NULL;
	jchar *lparg8=NULL;
	jchar *lparg9=NULL;
	jboolean *lparg10=NULL;
	jint *lparg11=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3CI_3C_3C_3C_3C_3Z_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetCharArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetBooleanArrayElements(arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = env->GetIntArrayElements(arg11, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint, jchar *, jchar *, jchar *, jchar *, jboolean *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8, lparg9, lparg10, lparg11);
fail:
	if (arg11 && lparg11) env->ReleaseIntArrayElements(arg11, lparg11, 0);
	if (arg10 && lparg10) env->ReleaseBooleanArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseCharArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3CI_3C_3C_3C_3C_3Z_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3CI_3I_3I_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3CI_3I_3I_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jbooleanArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jboolean *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3CI_3I_3I_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetBooleanArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint, jint *, jint *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseBooleanArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3CI_3I_3I_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3C_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3C_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jbooleanArray arg6)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jboolean *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3C_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetBooleanArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jchar *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseBooleanArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3C_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3C_3Z_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3C_3Z_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jbooleanArray arg6, jbooleanArray arg7)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jboolean *lparg6=NULL;
	jboolean *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3C_3Z_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetBooleanArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetBooleanArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jchar *, jboolean *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseBooleanArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseBooleanArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3C_3Z_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3I_3C_3Z_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3I_3C_3Z_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jbooleanArray arg7, jbooleanArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jchar *lparg6=NULL;
	jboolean *lparg7=NULL;
	jboolean *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3I_3C_3Z_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetBooleanArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetBooleanArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *, jchar *, jboolean *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseBooleanArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseBooleanArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3I_3C_3Z_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3I_3I_3C_3Z_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3I_3I_3C_3Z_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jbooleanArray arg8, jbooleanArray arg9)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jchar *lparg7=NULL;
	jboolean *lparg8=NULL;
	jboolean *lparg9=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3I_3I_3C_3Z_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetBooleanArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetBooleanArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *, jint *, jchar *, jboolean *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseBooleanArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseBooleanArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3I_3I_3C_3Z_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3C_3C_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jbooleanArray arg5)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jboolean *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3C_3C_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetBooleanArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseBooleanArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3C_3C_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3I_3I_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbooleanArray arg3)
{
	jboolean *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__III_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__III_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIJ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIJ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIJI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIJI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIJI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIJJ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIJJ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIJJI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIJJI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIJJI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIJJJJJ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJJJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIJJJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIJJJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	nsID _arg2, *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jintArray arg4)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jbooleanArray arg4)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jboolean *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetBooleanArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, nsID *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jbyte *, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jbyte *, jbyte *, jint, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	nsID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbooleanArray arg3)
{
	nsID _arg2, *lparg2=NULL;
	jboolean *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIZI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIZI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jboolean, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIZI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIZ_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZ_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jbooleanArray arg3)
{
	jboolean *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__IIZ_3Z_FUNC);
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jboolean, jboolean *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__IIZ_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BI
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BI_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BI_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BI_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BJ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BJ_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jbooleanArray arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jboolean *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetBooleanArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BZI_3I_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BZI_3I_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jboolean arg3, jint arg4, jintArray arg5, jbooleanArray arg6)
{
	jbyte *lparg2=NULL;
	jint *lparg5=NULL;
	jboolean *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BZI_3I_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetBooleanArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jboolean, jint, jint *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseBooleanArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BZI_3I_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3BZ_3I_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BZ_3I_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jboolean arg3, jintArray arg4, jbooleanArray arg5)
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jboolean *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3BZ_3I_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetBooleanArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jboolean, jint *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseBooleanArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3BZ_3I_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3B
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3B_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3BZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3BZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jboolean arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3BZ_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jboolean))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3BZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3B_3BZZ_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3B_3BZZ_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jboolean arg5, jboolean arg6, jintArray arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3B_3BZZ_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jbyte *, jboolean, jboolean, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3B_3BZZ_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3B_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3B_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3I_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3I_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jbooleanArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jboolean *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3I_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetBooleanArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3I_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3J
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlongArray arg3)
{
	jbyte *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3J_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbooleanArray arg3)
{
	jbyte *lparg2=NULL;
	jboolean *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3B_3Z_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3Z_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbooleanArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jboolean *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3B_3Z_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jboolean *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3B_3Z_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3C_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CIIII
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3CIIII_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3CIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CI_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jintArray arg4)
{
	jchar *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3CI_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3CI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jboolean arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3CZ_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jboolean))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3CZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C_3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3)
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3C_3C_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C_3C_3CZ
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3CZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3, jcharArray arg4, jboolean arg5)
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3C_3C_3CZ_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jchar *, jchar *, jboolean))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3C_3C_3CZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jbooleanArray arg3)
{
	jchar *lparg2=NULL;
	jboolean *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3C_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetBooleanArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseBooleanArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3C_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3F
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3F_FUNC);
	if (arg2) if ((lparg2 = env->GetFloatArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jfloat *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseFloatArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3F_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I_3I_3I_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_3I_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I_3J
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3)
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I_3J_3I
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3I_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3I_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3J
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2)
{
	jlong *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3J_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3S
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3S_FUNC);
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3S_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3Z
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbooleanArray arg2)
{
	jboolean *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, VtblCall__II_3Z_FUNC);
	if (arg2) if ((lparg2 = env->GetBooleanArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jboolean *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseBooleanArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, VtblCall__II_3Z_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	nsID _arg1, *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	if (arg1) if ((lparg1 = getnsIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) setnsIDFields(env, arg1, lparg1);
	XPCOM_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
}
#endif

#ifndef NO_memmove__I_3BI
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__I_3BI_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove__I_3BI_FUNC);
}
#endif

#ifndef NO_memmove__I_3CI
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__I_3CI_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove__I_3CI_FUNC);
}
#endif

#ifndef NO_memmove__I_3II
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__I_3II_FUNC);
	if (arg1) if ((lparg1 = env->GetIntArrayElements(arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove__I_3II_FUNC);
}
#endif

#ifndef NO_memmove__I_3JI
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3JI)
	(JNIEnv *env, jclass that, jint arg0, jlongArray arg1, jint arg2)
{
	jlong *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__I_3JI_FUNC);
	if (arg1) if ((lparg1 = env->GetLongArrayElements(arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseLongArrayElements(arg1, lparg1, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove__I_3JI_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	nsID _arg0, *lparg0=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II_FUNC);
	if (arg0) if ((lparg0 = getnsIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setnsIDFields(env, arg0, lparg0);
	XPCOM_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II_FUNC);
}
#endif

#ifndef NO_memmove___3BII
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove___3BII_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove___3BII_FUNC);
}
#endif

#ifndef NO_memmove___3B_3CI
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3B_3CI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jchar *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove___3B_3CI_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove___3B_3CI_FUNC);
}
#endif

#ifndef NO_memmove___3CII
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove___3CII_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove___3CII_FUNC);
}
#endif

#ifndef NO_memmove___3III
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove___3III_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove___3III_FUNC);
}
#endif

#ifndef NO_memmove___3JII
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3JII)
	(JNIEnv *env, jclass that, jlongArray arg0, jint arg1, jint arg2)
{
	jlong *lparg0=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove___3JII_FUNC);
	if (arg0) if ((lparg0 = env->GetLongArrayElements(arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) env->ReleaseLongArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, memmove___3JII_FUNC);
}
#endif

#ifndef NO_nsEmbedCString_1Length
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedCString_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedCString_1Length_FUNC);
	rc = (jint)((nsEmbedCString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedCString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedCString_1delete
JNIEXPORT void JNICALL XPCOM_NATIVE(nsEmbedCString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, nsEmbedCString_1delete_FUNC);
	delete (nsEmbedCString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, nsEmbedCString_1delete_FUNC);
}
#endif

#ifndef NO_nsEmbedCString_1get
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedCString_1get)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedCString_1get_FUNC);
	rc = (jint)((nsEmbedCString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedCString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedCString_1new__
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedCString_1new__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedCString_1new___FUNC);
	rc = (jint)new nsEmbedCString();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedCString_1new___FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedCString_1new___3BI
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedCString_1new___3BI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedCString_1new___3BI_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new nsEmbedCString((const char *)lparg0, arg1);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, nsEmbedCString_1new___3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedString_1Length
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedString_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedString_1Length_FUNC);
	rc = (jint)((nsEmbedString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedString_1delete
JNIEXPORT void JNICALL XPCOM_NATIVE(nsEmbedString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, nsEmbedString_1delete_FUNC);
	delete (nsEmbedString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, nsEmbedString_1delete_FUNC);
}
#endif

#ifndef NO_nsEmbedString_1get
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedString_1get)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedString_1get_FUNC);
	rc = (jint)((nsEmbedString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedString_1new__
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedString_1new__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedString_1new___FUNC);
	rc = (jint)new nsEmbedString();
	XPCOM_NATIVE_EXIT(env, that, nsEmbedString_1new___FUNC);
	return rc;
}
#endif

#ifndef NO_nsEmbedString_1new___3C
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsEmbedString_1new___3C)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsEmbedString_1new___3C_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new nsEmbedString(lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, nsEmbedString_1new___3C_FUNC);
	return rc;
}
#endif

#ifndef NO_nsID_1Equals
JNIEXPORT jboolean JNICALL XPCOM_NATIVE(nsID_1Equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsID_1Equals_FUNC);
	rc = (jboolean)((nsID *)arg0)->Equals(*(nsID *)arg1);
	XPCOM_NATIVE_EXIT(env, that, nsID_1Equals_FUNC);
	return rc;
}
#endif

#ifndef NO_nsID_1Parse
JNIEXPORT jboolean JNICALL XPCOM_NATIVE(nsID_1Parse)
	(JNIEnv *env, jclass that, jint arg0, jstring arg1)
{
	const char *lparg1= NULL;
	jboolean rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsID_1Parse_FUNC);
	if (arg1) if ((lparg1 = env->GetStringUTFChars(arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)((nsID *)arg0)->Parse((const char *)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseStringUTFChars(arg1, lparg1);
	XPCOM_NATIVE_EXIT(env, that, nsID_1Parse_FUNC);
	return rc;
}
#endif

#ifndef NO_nsID_1delete
JNIEXPORT void JNICALL XPCOM_NATIVE(nsID_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, nsID_1delete_FUNC);
	delete (nsID *)arg0;
	XPCOM_NATIVE_EXIT(env, that, nsID_1delete_FUNC);
}
#endif

#ifndef NO_nsID_1new
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsID_1new)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsID_1new_FUNC);
	rc = (jint)new nsID();
	XPCOM_NATIVE_EXIT(env, that, nsID_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((char *)arg0);
	XPCOM_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif

}
